package bigdata.spark.test

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @ClassName: Test01_SparkSQL
 * @Author: MaoMao
 * @date: 2022/7/2 1:14
 * @description:
 */
object Test01_SparkSQL {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder
      .master("local[*]")
      .appName("SparkSQL")
      //.config("spark.some.config.option", "some-value")
      .getOrCreate()

    // TODO 读取文件
    val DataFrame: DataFrame = spark.read.json("data/user.json")

    // 展示数据
    //DataFrame.show()

    // TODO 使用SQL方式进行访问
    // 需要创建临时表
    DataFrame.createOrReplaceTempView("user")

    spark.sql(
      """
        |select avg(age) from user
        |""".stripMargin
    ).show()

    spark.stop()
  }
}
