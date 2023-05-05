package bigdata.spark.test

import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{Encoder, Encoders, SparkSession, functions}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * @ClassName: Test14_Req_SQL
 * @Author: MaoMao
 * @date: 2022/7/4 23:52
 * @description:
 */
object Test14_Req_SQL {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .enableHiveSupport()  // 连接 Hive 需要添加此配置
      .master("local[*]")
      .appName("SparkSQL")
      .getOrCreate()

    // 指定查询数据库
    spark.sql("use default")

    // 分步查询
    // 查出所有点击记录，city_name，area,product_name
    spark.sql(
      """
        |select
        |            a.*,
        |            p.product_name,
        |            c.area,
        |            c.city_name
        |        from user_visit_action a
        |                 join product_info p on a.click_product_id = p.product_id
        |                 join city_info c on a.city_id = c.city_id
        |                 where a.click_product_id != -1""".stripMargin
    ).createOrReplaceTempView("t1")

    // 注册自定义函数
    spark.udf.register("cityRemark",functions.udaf(new MyCityRemarkUDAF()))

    // 按照地区和商品id分组，统计出每个商品在每个地区的总点击次数
    spark.sql(
      """
        |select
        |    area,
        |    product_name,
        |    count(*) clickCnt,
        |    cityRemark(city_name) cityRemark
        |from t1 group by area, product_name""".stripMargin
    ).createOrReplaceTempView("t2")

    // 每个地区内按照点击次数降序排列
    spark.sql(
      """
        |select
        |    *,
        |    rank() over (partition by area order by clickCnt desc) as rn
        |from t2""".stripMargin
    ).createOrReplaceTempView("t3")

    // 取地区内前三名
    spark.sql(
      """
        |select
        |    *
        |from t3 where rn <= 3""".stripMargin
    ).show(false)

    // 关闭资源
    spark.stop()
  }
  case class CityRemarkBuffer(var total: Long,var cityMap: mutable.Map[String,Long])
  // TODO 城市备注UDAF实现(新版本，强类型)
  // 1.继承类型 Aggregator
  // 2.定义泛型
  //    IN：城市名称(Word)
  //    BUFF：CityRemarkBuffer
  //    OUT：String
  // 3.重写方法( 4 + 2)
  class MyCityRemarkUDAF extends Aggregator[String,CityRemarkBuffer,String]{

    // 初始化缓冲区
    override def zero: CityRemarkBuffer = {
      CityRemarkBuffer(0L,mutable.Map[String,Long]())
    }

    // 处理逻辑
    override def reduce(buff: CityRemarkBuffer, city: String): CityRemarkBuffer = {
      // 按照每个地区的每种商品，进行累加(总点击)
      buff.total += 1
      // 获取集合中相同k的v
      val oldCnt: Long = buff.cityMap.getOrElse(city, 0L)
      buff.cityMap.update(city,oldCnt + 1)

      buff
    }

    // 合并分区
    override def merge(b1: CityRemarkBuffer, b2: CityRemarkBuffer): CityRemarkBuffer = {
      // 合并总点击量
      b1.total += b2.total

      // 遍历b2的cityMap
      b2.cityMap.foreach{
        case(city,cnt) => {
          // 从b1中获取V
          val b1Cnt: Long = b1.cityMap.getOrElse(city, 0L)
          // 将b1中和b2中相同key的v相加
          b1.cityMap.update(city,b1Cnt + cnt)
        }
      }
      b1
    }

    // 对输出数据进行格式处理(计算每个城市的点击占比)
    override def finish(buffer: CityRemarkBuffer): String = {
      // 生成城市备注的字符串
      val list: ListBuffer[String] = ListBuffer[String]()

      // 倒序排序
      val sortCityList: List[(String, Long)] = buffer.cityMap.toList.sortBy(_._2)(Ordering.Long.reverse)
      // 取前2
      val top2: List[(String, Long)] = sortCityList.take(2)

      var rest = 100L

      top2.foreach{
        case (city,count) => {
          val r: Long = count * 100 / buffer.total
          rest -= r
          list.append(s"${city} ${r}%")
        }
      }
      // 判断是否有其他
      if(sortCityList.size > 2){
        list.append(s"其他 ${rest}%")
      }

      list.mkString("，")
    }

    override def bufferEncoder: Encoder[CityRemarkBuffer] = Encoders.product

    override def outputEncoder: Encoder[String] = Encoders.STRING
  }
}
