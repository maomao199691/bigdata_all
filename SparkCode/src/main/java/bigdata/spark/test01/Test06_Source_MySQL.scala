package bigdata.spark.test01

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @ClassName: Test06_Source_MySQL
 * @Author: MaoMao
 * @date: 2022/7/3 17:08
 * @description:
 */
object Test06_Source_MySQL {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
          .master("local[*]")
          .appName("SparkSQL")
          .getOrCreate()

    // TODO 读取MySQL的数据
    val mySQLdf: DataFrame = spark.read.format("jdbc")
      .option("url", "jdbc:mysql://hadoop102:3306/spark-sql?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("user", "root")
      .option("password", "000000")
      .option("dbtable", "user")
      .load()

    // TODO 向MySQL 写入数据
    mySQLdf.write.format("jdbc")
      .mode("append")
      .option("url", "jdbc:mysql://hadoop102:3306/spark-sql?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("user", "root")
      .option("password", "000000")
      .option("dbtable", "user1")
      .save()


    spark.stop()

  }
}
