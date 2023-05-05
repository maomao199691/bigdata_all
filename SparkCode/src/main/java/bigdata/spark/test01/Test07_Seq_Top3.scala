package bigdata.spark.test01

import org.apache.spark.sql.{Encoder, Encoders, SparkSession, functions}
import org.apache.spark.sql.expressions.Aggregator

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * @ClassName: Test07_Seq_Top3
 * @Author: MaoMao
 * @date: 2022/7/5 16:17
 * @description:
 */
object Test07_Seq_Top3 {
  def main(args: Array[String]): Unit = {

    // 连接Hive数据库
    val spark: SparkSession = SparkSession.builder()
      .enableHiveSupport()
      .master("local[*]")
      .appName("SparkSQL")
      .getOrCreate()

    // 指定数据库
    spark.sql("use default")

    //1.查询user表所有信息，product表product_name,city表city_name,area
    spark.sql(
      """
        |select
        |    a.*,
        |    p.product_name,
        |    c.city_name,
        |    c.area
        |from user_visit_action a
        |join product_info p on a.click_product_id = p.product_id
        |join city_info c on a.city_id = c.city_id
        |where a.click_product_id != -1""".stripMargin
    ).createOrReplaceTempView("t1")

    // 注册自定义函数UDAF
    spark.udf.register("cityRemark",functions.udaf(new MyCityRemarkUDAF()))

    //2.从t1中获得area，product_name，count(click)
    spark.sql(
      """
        |select
        |    area,
        |    product_name,
        |    count(click_product_id) clickCnt,
        |    cityRemark(city_name)
        |from t1 group by area, product_name""".stripMargin
    ).createOrReplaceTempView("t2")

    //3.在地区内按照点击次数倒序排序
    spark.sql(
      """
        |select
        |    *,
        |    rank() over (partition by area order by clickCnt desc) as rn
        |from t2""".stripMargin
    ).createOrReplaceTempView("t3")

    //4.获取排序后每个地区的前三名
    spark.sql(
      """
        |select
        |    *
        |from t3 where rn <= 3""".stripMargin
    ).show()

    // 关闭资源
    spark.stop()
  }
  // 自定义函数缓冲区
  case class CityRemarkBuffer(var total:Long,var cityMap:mutable.Map[String,Long])

  // 自定义聚合函数
  // 计算每个城市占总点击量的占比
  class MyCityRemarkUDAF extends Aggregator[String,CityRemarkBuffer,String]{

    // 缓冲区初始化
    override def zero: CityRemarkBuffer = {
      CityRemarkBuffer(0L,mutable.Map[String,Long]())
    }

    // 累加总点击量，并记录每个城市的点击次数
    override def reduce(buff: CityRemarkBuffer, city: String): CityRemarkBuffer = {

      // 总点击次数
      buff.total += 1L

      // 每个城市的点击次数
      // 获取集合中存储的点击数
      val oldCnt: Long = buff.cityMap.getOrElse(city, 0L)
      buff.cityMap.update(city,oldCnt + 1L)
      buff
    }

    // 合并分区
    override def merge(b1: CityRemarkBuffer, b2: CityRemarkBuffer): CityRemarkBuffer = {

      b1.total += b2.total

      // 遍历b2中集合存储的count，使用b1累加
      b2.cityMap.foreach{
        case(city,cnt2) => {
          val cnt1: Long = b1.cityMap.getOrElse(city, 0L)
          b1.cityMap.update(city,cnt1 + cnt2)
        }
      }
      b1
    }

    // 对每个城市中的count,进行做除
    override def finish(reduction: CityRemarkBuffer): String = {

      // 将处理后的数据放入可变list集合中,定义listBuffer
      val listBuffer: ListBuffer[String] = ListBuffer()

      var rest = 100L

      // 对缓冲区集合进行倒序排序，取前2
      val sortCityRemarkList: List[(String, Long)] = reduction.cityMap
        .toList.sortBy(_._2)(Ordering.Long.reverse)
      val top2: List[(String, Long)] = sortCityRemarkList.take(2)

      // 遍历top2,获取占比数
      top2.foreach{
        case (city,cnt) => {
          // 每个城市点击量占总点击量的比例
          val r: Long = cnt * 100 / reduction.total

          // 计算除去前2名城市的其他占比
          rest -= r

          // 放入ListBuffer
          listBuffer.append(s"${city} ${r}%")
        }
      }
      // 判断是否有其他
      if(sortCityRemarkList.size > 2){
        listBuffer.append(s"其他 ${rest}")
      }

      listBuffer.mkString("，")
    }

    override def bufferEncoder: Encoder[CityRemarkBuffer] = Encoders.product

    override def outputEncoder: Encoder[String] = Encoders.STRING
  }
}