package bigdata.spark.test

import org.apache.spark.sql.{DataFrame, Encoder, Encoders, SparkSession, functions}
import org.apache.spark.sql.expressions.Aggregator

/**
 * @ClassName: Test12_UDAF2
 * @Author: MaoMao
 * @date: 2022/7/3 10:46
 * @description:
 */
object Test12_UDAF2 {

  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
          .master("local[*]")
          .appName("SparkSQL")
          .getOrCreate()

    // 读取指定的json文件
    val df: DataFrame = spark.read.json("data/user.json")

    // 创建自定义聚合函数
    val myAvg = new MyAvg()

    // 注册自定义聚合函数
    spark.udf.register("avgAge",functions.udaf(myAvg))

    // 创建临时表
    df.createOrReplaceTempView("user")

    // 使用聚合函数
    spark.sql(
      """
        |select avgAge(age) from user
        |""".stripMargin
    ).show()

    // 关闭资源
    spark.stop()
  }
  // 缓冲区
  case class Buff(var sum:Long,var cnt:Long)

  // 自定义聚合函数
  class MyAvg extends Aggregator[Long,Buff,Double]{

    // 初始化缓冲区
    override def zero: Buff = {
      Buff(0L,0L)
    }

    // 进行数据处理
    override def reduce(buff: Buff, input: Long): Buff = {
      buff.sum += input
      buff.cnt += 1
      buff
    }

    // 合并缓冲区
    override def merge(b1: Buff, b2: Buff): Buff = {
      b1.sum += b2.sum
      b1.cnt += b2.cnt
      b1
    }

    // 返回处理的结果
    override def finish(reduction: Buff): Double = reduction.sum.toDouble / reduction.cnt

    // 对缓冲区做编码处理
    override def bufferEncoder: Encoder[Buff] = Encoders.product

    // 对输出数据做编码处理
    override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
  }
}