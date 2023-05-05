package bigdata.spark.sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
 * @ClassName: SparkSQL09_RDD_DataSet
 * @Author: MaoMao
 * @date: 2022/7/2 10:07
 * @description:
 */
object SparkSQL09_RDD_DataSet {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .master("SparkSQL")
      .getOrCreate()

    import spark.implicits._

    val rdd: RDD[(Int, String, Int)] = spark.sparkContext.makeRDD(
      List(
        (1001, "hangsan", 20),
        (1002, "lisi", 30)
      )
    )

    rdd.toDS()
  }
  case class User1(id:Long,name:String,age:Long)
}
