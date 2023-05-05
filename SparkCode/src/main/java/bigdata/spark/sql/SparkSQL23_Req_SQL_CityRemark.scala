package bigdata.spark.sql

import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{Encoder, Encoders, SparkSession, functions}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * @ClassName: SparkSQL23_Req_SQL_CityRemark
 * @Author: MaoMao
 * @date: 2022/7/4 11:45
 * @description:
 */
object SparkSQL23_Req_SQL_CityRemark {
  def main(args: Array[String]): Unit = {

    System.setProperty("HADOOP_USER_NAME", "root")

    val spark = SparkSession.builder
      .master("local[*]")
      .enableHiveSupport()
      .appName("SparkSQL")
      .getOrCreate()

    spark.sql("use atguigu220321")

    // TODO 将数据补全
    spark.sql(
      """
        |			select
        |				a.*,
        |				p.product_name,
        |				c.city_name,
        |				c.area
        |			from user_visit_action a
        |			join product_info p on a.click_product_id = p.product_id
        |			join city_info c on c.city_id = a.city_id
        |			where a.click_product_id != -1
        |""".stripMargin).createOrReplaceTempView("t1")

    // TODO 按照读取，产品进行数据分组
    spark.udf.register("cityRemark",functions.udaf(new MyCityRemarkUDAF))

    spark.sql(
      """
        |		select
        |			area,
        |			product_name,
        |			count(*) clickCnt,
        |   cityRemark(city_name)
        |		from t1 group by area, product_name
        |""".stripMargin).createOrReplaceTempView("t2")

    // TODO 组内进行排序
    spark.sql(
      """
        |	select
        |		*,
        |		rank() over ( partition by area order by clickCnt desc ) as rk
        |	from t2
        |""".stripMargin).createOrReplaceTempView("t3")

    // TODO 取前3名
    spark.sql(
      """
        |select
        |	*
        |from t3 where rk <= 3
        |""".stripMargin).show

    spark.stop()
  }
  case class CityRemarkBuffer(var total:Long,var cityMap : mutable.Map[String,Long])
  // TODO 城市备注 UDAF实现（新版本，强类型）
  //  1.继承类 Aggregator
  //  2.定义泛型
  //    IN：城市名称(Word)
  //    BUFF：CityRemarkBuffer缓冲类
  //    OUT： String
  class MyCityRemarkUDAF extends Aggregator[String,CityRemarkBuffer,String]{

    override def zero: CityRemarkBuffer = {
      CityRemarkBuffer(0L,mutable.Map[String,Long]())
    }

    override def reduce(b: CityRemarkBuffer, city: String): CityRemarkBuffer = {

      b.total += 1
      val map: mutable.Map[String, Long] = b.cityMap

      val oldCnt: Long = map.getOrElse(city, 0L)
      map.updated(city, oldCnt + 1)

      b.cityMap = map
      b
    }

    override def merge(b1: CityRemarkBuffer, b2: CityRemarkBuffer): CityRemarkBuffer = {
      b1.total += b2.total

      b2.cityMap.foreach {
        case (city, cnt2) => {
          val oldCnt: Long = b1.cityMap.getOrElse(city, 0L)
          b1.cityMap.updated(city, oldCnt + cnt2)
        }
      }
      b1
    }

    override def finish(buff: CityRemarkBuffer): String = {

      // 生成城市的字符串
      val listBuffer: ListBuffer[String] = ListBuffer[String]()

      // 将缓冲区中城市数据循环遍历，放置在 list集合中
      val total: Long = buff.total
      val cityMap: mutable.Map[String, Long] = buff.cityMap
      val sortedCityList: List[(String, Long)] = cityMap.toList.sortBy(_._2)(Ordering.Long.reverse)
      val top2: List[(String, Long)] = sortedCityList.take(2)

      top2.foreach{
        case(city, count) => {
          var r = count * 100 / total
          listBuffer.append(s"${city} ${r}")
        }
      }
      // 判断是否有其他
      if(sortedCityList.size > 2){
        listBuffer.append(s"其他 ${}")
      }
      listBuffer.mkString(",")
    }

    override def bufferEncoder: Encoder[CityRemarkBuffer] = Encoders.product

    override def outputEncoder: Encoder[String] = Encoders.STRING
  }
}
