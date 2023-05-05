package bigdata.spark.sql

import org.apache.spark.sql.SparkSession

/**
 * @ClassName: SparkSQL20_Hive
 * @Author: MaoMao
 * @date: 2022/7/3 19:16
 * @description:
 */
object SparkSQL20_Hive {
  def main(args: Array[String]): Unit = {

    System.setProperty("HADOOP_USER_NAME", "atguigu")

    val spark: SparkSession = SparkSession.builder()
      .enableHiveSupport()
      .master("local[*]")
      .appName("SparkSQL")
      .getOrCreate()

    spark.sql(
      """
        |select * from user_visit_action
        |""".stripMargin).show()

    spark.stop()
  }
}