package bigdata.spark.sql

import org.apache.spark.sql.SparkSession

/**
 * @ClassName: SparkSQL19_MySQL
 * @Author: MaoMao
 * @date: 2022/7/2 16:23
 * @description:
 */
object SparkSQL19_MySQL {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
          .master("local[*]")
          .appName("SparkSQL")
          .getOrCreate()
    spark.read.format("jdbc")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("user", "root")
      .option("password", "123456")
      .option("dbtable", "user")
      .load().show

  }
}
