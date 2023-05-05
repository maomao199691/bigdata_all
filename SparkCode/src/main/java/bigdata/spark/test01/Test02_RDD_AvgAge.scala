package bigdata.spark.test01

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
 * @ClassName: Test02_UDF
 * @Author: MaoMao
 * @date: 2022/7/3 11:27
 * @description: 使用 RDD 对年龄求平均值
 */
object Test02_RDD_AvgAge {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("SparkSQL")
      .getOrCreate()

    // 读取json文件
    val df: DataFrame = spark.read.json("data/user.json")

    // DataFrame -> RDD
    val rdd: RDD[Row] = df.rdd

    // 对RDD做数据处理
    val rdd1: RDD[(Long, Int)] = rdd.map {
      a => (a.getLong(0), 1)
    }

    // 聚合处理
    val tuple: (Long, Int) = rdd1.reduce(
      (t1, t2) => {
        (t1._1 + t2._1, t1._2 + t2._2)
      }
    )

    println(tuple._1 / tuple._2)

    spark.stop()
  }
}
