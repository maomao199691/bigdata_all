package bigdata.spark.sql

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @ClassName: SparkSQL05_DataFrame_SQL
 * @Author: MaoMao
 * @date: 2022/7/2 1:52
 * @description:
 */
object SparkSQL05_DataFrame_SQL {
  def main(args: Array[String]): Unit = {
    // TODO SparkSQL 的数据模型的构建和使用
    val spark: SparkSession = SparkSession.builder
      .master("local[*]")
      .appName("SparkSQl")
      .getOrCreate()

    // TODO 读取文件数据 构建新的数据模型： DataFrame
    val dataFrame: DataFrame = spark.read.json("data/user.json")

    dataFrame.createOrReplaceTempView("user")

    spark.sql(
      """
        |select avg(age) from user
        |""".stripMargin
    ).show()

    spark.stop()

  }
}
