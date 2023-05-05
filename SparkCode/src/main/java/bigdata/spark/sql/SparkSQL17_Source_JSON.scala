package bigdata.spark.sql

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @ClassName: SparkSQL17_Source_JSON
 * @Author: MaoMao
 * @date: 2022/7/2 16:09
 * @description:
 */
object SparkSQL17_Source_JSON {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
          .master("local[*]")
          .appName("SparkSQL")
          .getOrCreate()

    // TODO SparkSQL数据源 - JSON
    //      读取的文件是JSON文件，但是JSON文件的要求是，整个文件的数据格式为JSON
    //      SparkSQL基于SparkCore开发的，SparkCore读取文件是基于Hadoop实现的，而Hadoop是按行读取数据的
    val df: DataFrame = spark.read.json("data/user.json")

    df.show()

    spark.stop()
  }
}
