package bigdata.spark.sql

import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{DataFrame, Encoder, Encoders, SparkSession, functions}

/**
 * @ClassName: SparkSQL12_UDAF
 * @Author: MaoMao
 * @date: 2022/7/2 11:11
 * @description:
 */
object SparkSQL12_UDAF {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("test")
      .getOrCreate()

    val df: DataFrame = spark.read.json("data/user.json")

    df.createOrReplaceTempView("user")

    // TODO UDAF
    // 所谓的 UDAF 函数，其实就是用户自定义的聚合函数
    // 年龄的平均值。
    //用户自定的聚合函数，要遵循spark的约束和规则，应该使用函数类实现

    //TODO 将UDFA函数类传创建对象
    /*spark.sql(
      """
        |select avg(age) from user
        |""".stripMargin
    ).show()*/

    // TODO 使用UDAF实现求平均值
    // TODO 注册到 SparkSQL中
    spark.udf.register("avgAge",functions.udaf(new MyAvgAgeUDAF()))
    // TODO 在 SQL 中使用聚合函数

    // 定义用户的自定义聚合函数
    spark.sql(
      """
        |select avgAge(age) from user
        |""".stripMargin
    ).show()


    spark.stop()

  }

  case class AvgAgeUDAF(var total:Long, var cnt: Long)

  // TODO 自定义聚合函数（年龄的平均值）
  //  1、继承类 org.apache.spark.sql.expressions.Aggregator
  //  2、自定义泛型
  //    IN：输入数据的类型，Long
  //    BUF：缓冲区中进行处理的数据类型 AvgAgeBuffer
  //    OUT：输出数据的类型 Long
  //  3.重写方法 (4 + 2)
  class MyAvgAgeUDAF extends Aggregator[Long,AvgAgeUDAF,Long]{

    // TODO 设定缓冲区的初始化
    override def zero: AvgAgeUDAF = {
      AvgAgeUDAF(0L,0L)
    }

    // TODO 用输入的年龄参数更新缓冲区数据
    override def reduce(buff: AvgAgeUDAF, age: Long): AvgAgeUDAF = {
      buff.total += age
      buff.cnt += 1
      buff
    }

    // TODO 合并缓冲区
    override def merge(b1: AvgAgeUDAF, b2: AvgAgeUDAF): AvgAgeUDAF = {
      b1.total += b2.total
      b1.cnt += b2.cnt
      b1
    }

    // TODO 完成计算
    override def finish(reduction: AvgAgeUDAF): Long = {
      reduction.total / reduction.cnt
    }

    // TODO 对缓冲区的数据进行编码处理
    override def bufferEncoder: Encoder[AvgAgeUDAF] = {
      Encoders.product
    }

    // TODO 对输出的数据进行编码处理
    override def outputEncoder: Encoder[Long] = {
      Encoders.scalaLong
    }
  }
}
