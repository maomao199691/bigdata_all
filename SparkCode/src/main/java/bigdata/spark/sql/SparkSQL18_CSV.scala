package bigdata.spark.sql

import org.apache.spark.sql.SparkSession

/**
 * @ClassName: SparkSQL18_CSV
 * @Author: MaoMao
 * @date: 2022/7/2 16:18
 * @description:
 */
object SparkSQL18_CSV {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
          .master("local[*]")
          .appName("SparkSQL")
          .getOrCreate()

    spark.read.format("csv")
      .option("inferSchema", "true")
      .option("header", "true")
      .load("data/user.csv")


  }
}
