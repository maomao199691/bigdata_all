package bigdata.spark.sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
 * @ClassName: SparkSQL21_Req
 * @Author: MaoMao
 * @date: 2022/7/4 10:00
 * @description:
 */
object SparkSQL21_Req {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .enableHiveSupport()
      .master("local[*]")
      .appName("SparkSQL")
      .getOrCreate()

    spark.sql("use default")

    // TODO 需求的基本实现思路
    // TODO 1.数据从哪里来，格式是什么样？
    //      3个文件，数据使用tab键隔开（每一列有特殊含义）
    val rdd1: RDD[String] = spark.sparkContext.textFile("data/user_visit_action.txt")
    val rdd2: RDD[String] = spark.sparkContext.textFile("data/city_indo.txt")
    val rdd3: RDD[String] = spark.sparkContext.textFile("data/product_info.txt")

    rdd1.map(_.split("\t"))
    rdd2.map(_.split("\t"))
    rdd3.map(_.split("\t"))

    // TODO 2.数据处理逻辑？
    //      2.1缺什么，补什么!!!
    //      用户行为数据 + 地区 + 商品名称 + 城市名称

    //rdd1.join(rdd2).join(rdd3)

    //      2.2多什么，补什么!!!
    //      大数据处理过程中，数据量非常大，所以会导致执行性能下降
    //      只保留点击数据，其他数据过滤掉
    //      数据中含有的无效内容也过滤掉，只保留有效内容
    // TODO 3.数据结果是什么样的，存储到哪去？
    //    数据结果采用元组，存储到文件中

    spark.stop()


  }
}
