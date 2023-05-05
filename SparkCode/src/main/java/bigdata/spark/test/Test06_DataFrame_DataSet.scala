package bigdata.spark.test

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

/**
 * @ClassName: Test06_DataFrame_DataSet
 * @Author: MaoMao
 * @date: 2022/7/2 14:03
 * @description:
 */
object Test06_DataFrame_DataSet {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("test")
      .getOrCreate()

    import spark.implicits._

    val df: DataFrame = spark.read.json("data/user.json")

    // TODO DataFrame -> DataSet
    val ds: Dataset[User] = df.as[User]

    ds.show()

    spark.stop()
  }
  case class User(id:Long,name:String,age:Long)
}
