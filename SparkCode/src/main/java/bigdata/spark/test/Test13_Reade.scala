package bigdata.spark.test

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @ClassName: Test13_Reade
 * @Author: MaoMao
 * @date: 2022/7/3 12:34
 * @description:
 */
object Test13_Reade {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder()
          .master("local[*]")
          .appName("SparkSQL")
          .getOrCreate()

    val df: DataFrame = spark.read.json("data/user.json")

    df.write.format("orc").save("output")

    spark.stop()
  }
}