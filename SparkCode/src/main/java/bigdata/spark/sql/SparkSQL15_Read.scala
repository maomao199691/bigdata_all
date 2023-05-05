package bigdata.spark.sql

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @ClassName: SparkSQL15_Read
 * @Author: MaoMao
 * @date: 2022/7/2 15:45
 * @description:
 */
object SparkSQL15_Read {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("SparkSQL")
      .getOrCreate()

    // TODO SparkSQL 提供了通用的数据读取方式
    // 所谓的通用，其实指的就是相同的API
    // SparkSQL默认读取的文件格式为 Parquet(列式存储格式)
    val df: DataFrame = spark.read.load("data/user.parquet")

    df.show()

    spark.stop()


  }
}
