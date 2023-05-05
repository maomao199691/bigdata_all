package bigdata.spark.sql

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @ClassName: SparkSQL16_Write
 * @Author: MaoMao
 * @date: 2022/7/2 15:56
 * @description:
 */
object SparkSQL16_Write {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("SparkSQL")
      .getOrCreate()

    // TODO SparkSQL 提供了通用的数据读取方式
    // 所谓的通用，其实指的就是相同的API
    // SparkSQL默认读取的文件格式为 Parquet(列式存储格式)
    val df: DataFrame = spark.read.json("data/user.json")

    // TODO 保存数据
    // 如果想要改变文件存储的格式，需要进行格式化操作
    df.write.format("json").mode("overwrite").save("output")

    spark.stop()
  }
}
