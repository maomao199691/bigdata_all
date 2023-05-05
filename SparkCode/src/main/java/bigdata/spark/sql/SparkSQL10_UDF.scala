package bigdata.spark.sql

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @ClassName: SparkSQL10_UDF
 * @Author: MaoMao
 * @date: 2022/7/2 10:23
 * @description:
 */
object SparkSQL10_UDF {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .master("SparkSQL")
      .getOrCreate()

    val df: DataFrame = spark.read.json("data/user.json")

    //TODO 使用自定义的函数实现SQL实现不了的功能
    //    用户自定义的函数称之为： UDF


    // TODO 自己定义一个函数，在
    spark.udf.register("addName",(s:String) => "Name:" + s)

    df.createOrReplaceTempView("people")

    spark.sql("select addName(name),age from people").show()
  }
}
