package bigdata.spark.test01

import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, IntegerType, LongType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
 * @ClassName: Test03_UDAF_OldVersion
 * @Author: MaoMao
 * @date: 2022/7/3 11:28
 * @description:
 */
object Test03_UDAF_OldVersion {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
          .master("local[*]")
          .appName("SparkSQL")
          .getOrCreate()

    // 读取指定json文件
    val df: DataFrame = spark.read.json("data/user.json")

    // 创建自定义聚合函数
    val avgAge = new MyAvg()

    // 注册自定义聚合函数
    spark.udf.register("avgAge",avgAge)

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

  // custom UDAF-OldVersion
  class MyAvg extends UserDefinedAggregateFunction{

    // 输入数据类型
    override def inputSchema: StructType = StructType(Array(StructField("age",IntegerType)))

    // 设置缓冲区数据类型
    override def bufferSchema: StructType = {
      StructType(Array(StructField("sum",LongType),StructField("count",LongType)))
    }

    // 输出数据类型
    override def dataType: DataType = DoubleType

    // 相同数据输入时，是否保持相同的输出
    override def deterministic: Boolean = true

    // 缓冲区初始化
    override def initialize(buffer: MutableAggregationBuffer): Unit = {
      buffer(0) = 0L
      buffer(1) = 0L
    }

    // 更新缓冲区数据
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
      if(!input.isNullAt(0)){
        buffer(0) = buffer.getLong(0) + input.getInt(0)
        buffer(1) = buffer.getLong(1) + 1
      }
    }

    // 合并缓冲区
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0)
      buffer1(1) = buffer1.getLong(1) + buffer2.getLong(1)
    }

    // 返回值
    override def evaluate(buffer: Row): Double = buffer.getLong(0).toDouble / buffer.getLong(1)
  }
}