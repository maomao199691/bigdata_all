package bigdata.spark.sql

import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, LongType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
 * @ClassName: SparkSQL13_UDAD
 * @Author: MaoMao
 * @date: 2022/7/2 14:24
 * @description:
 */
object SparkSQL13_UDAD_OldVersion {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("SparkSQL")
      .getOrCreate()

    val df: DataFrame = spark.read.json("data/user.json")

    df.createOrReplaceTempView("user")

    spark.sql(
      """
        |""".stripMargin
    )

    spark.stop()
  }

  //TODO 自定义聚合函数 UDAF(早期版本，弱类型)
  //  1.继承类UserDefinedAggregateFunction
  //  2.重写方法(8个)
  class MyAvgAgeUDAF extends UserDefinedAggregateFunction{

    // TODO 输入数据的结构
    override def inputSchema: StructType = {
      StructType(Array(StructField("age", LongType)))
    }

    // TODO 缓冲区的结构
    override def bufferSchema: StructType = {
      StructType(
        Array(StructField("sum",LongType),
              StructField("cnt",LongType)
        )
      )
    }

    // TODO 函数返回值的数据类型
    override def dataType: DataType = DoubleType

    // TODO 稳定性：对于相同的输入是否一直返回相同的输出。
    override def deterministic: Boolean = true

    // TODO 缓冲区的初始化
    override def initialize(buffer: MutableAggregationBuffer): Unit = {
      // 年龄总和
      buffer(0) = 0L
      // 次数总和
      buffer(1) = 0L
    }

    // TODO 将输入的数据更新到缓冲区中
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
      if(!input.isNullAt(0)){
        buffer(0) = buffer.getLong(0) + input.getLong(0)
        buffer(1) = buffer.getLong(1) + 1
      }
    }

    // 合并分区
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0)
      buffer1(0) = buffer1.getLong(1) + buffer2.getLong(1)
    }

    // 计算最终结果
    override def evaluate(buffer: Row): Any = {
      buffer.getLong(0).toDouble / buffer.getLong(1)
    }
  }
}
