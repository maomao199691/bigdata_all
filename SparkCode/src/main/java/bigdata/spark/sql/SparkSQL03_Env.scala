package bigdata.spark.sql

import org.apache.spark.sql.SparkSession

/**
 * @ClassName: SparkSQL03_Env
 * @Author: MaoMao
 * @date: 2022/7/1 11:20
 * @description:
 */
object SparkSQL03_Env {
  def main(args: Array[String]): Unit = {

    // TODO SparkSQL 的数据模型的构建和使用
    val spark: SparkSession = SparkSession.builder
      .master("local[*]")
      .appName("SparkSQl")
      .getOrCreate()

    spark.stop()
  }
}
