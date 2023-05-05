package bigdata.spark.test

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @ClassName: Test03_DataFrame_RDD
 * @Author: MaoMao
 * @date: 2022/7/2 10:44
 * @description:
 */
object Test03_DataFrame_RDD {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("SparkSQL")
      .getOrCreate()

    // 创建一个DataFrame
    val df: DataFrame = spark.read.json("data/user.json")

    df.show()

    // DataFrame转换为RDD，直接 .rdd
    df.rdd.foreach(println)

    spark.stop()

  }
}
