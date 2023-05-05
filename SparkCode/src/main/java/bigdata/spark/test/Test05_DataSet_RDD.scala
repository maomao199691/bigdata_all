package bigdata.spark.test

import org.apache.spark.sql.{Dataset, SparkSession}

/**
 * @ClassName: Test05_DataSet_RDD
 * @Author: MaoMao
 * @date: 2022/7/2 13:50
 * @description:
 */
object Test05_DataSet_RDD {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("test")
      .getOrCreate()

    import spark.implicits._

    val ds: Dataset[User] = List(User(1001, "zhangsan", 20), User(1002, "lisi", 30)).toDS()

    ds.rdd.foreach(println)

    spark.stop()

  }
  case class User(id:Long,name:String,age:Long)
}
