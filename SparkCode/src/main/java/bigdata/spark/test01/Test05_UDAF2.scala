package bigdata.spark.test01

import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{DataFrame, Encoder, Encoders, SparkSession, functions}

/**
 * @ClassName: Test05_UDAF2
 * @Author: MaoMao
 * @date: 2022/7/3 11:28
 * @description: 采用强类型的 aggregate 替代 UserDefinedAggregatorFunctions
 */
object Test05_UDAF2 {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
          .master("local[*]")
          .appName("SparkSQL")
          .getOrCreate()

    // 读取json文件
    val df: DataFrame = spark.read.json("data/user.json")

    // 创建临时表
    df.createOrReplaceTempView("user")

    // 创建自定义聚合函数
    val myAvg = new MyAvg()

    // 注册自定义聚合函数
    spark.udf.register("myAvg",functions.udaf(myAvg))

    // 使用自定义聚合函数
    spark.sql(
      """
        |select myAvg(age) from user
        |""".stripMargin
    ).show()

    // 关闭资源
    spark.stop()
  }
  // 用作自定义函数的缓冲区样例类
  case class Buff(var sum:Long,var count:Long)

  // 自定义聚合函数
  class MyAvg extends Aggregator[Long,Buff,Double]{

    // 初始化缓冲区
    override def zero: Buff = Buff(0L,0L)

    // 处理数据
    override def reduce(b: Buff, a: Long): Buff = {
      b.sum += a
      b.count += 1
      b
    }

    // 合并缓冲区
    override def merge(b1: Buff, b2: Buff): Buff = {
      b1.sum += b2.sum
      b1.count += b2.count
      b1
    }

    // 返回值
    override def finish(reduction: Buff): Double = reduction.sum.toDouble / reduction.count

    // 对缓冲区数据做编码处理
    override def bufferEncoder: Encoder[Buff] = Encoders.product

    // 对输出数据做编码处理
    override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
  }
}
