package bigdata.spark.test

import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, IntegerType, LongType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

/**
 * @ClassName: Test08_UDAF
 * @Author: MaoMao
 * @date: 2022/7/2 14:15
 * @description: 弱类型 UDAF 老版本
 */
object Test08_UDAF_OldVersion {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
          .master("local[*]")
          .appName("SparkSQL")
          .getOrCreate()

    // read the json file
    val df: DataFrame = spark.read.json("data/user.json")

    // create TmpView
    df.createOrReplaceTempView("user")

    // new UDAF
    val myAvgAge = new MyAvgUDAF()

    // register UDAF
    spark.udf.register("myAvg",myAvgAge)

    // use myAvg
    spark.sql(
      """
        |select myAvg(age) from user
        |""".stripMargin
    ).show()

    // close resource
    spark.stop()
  }

  // custom UDAF - Old Version
  class MyAvgUDAF extends UserDefinedAggregateFunction{

    // create buffer's variable
    var sum:Long = 0
    var count:Long = 0

    // input parameter type
    override def inputSchema: StructType = StructType(Array(StructField("age",IntegerType)))

    // buffer variable type
    override def bufferSchema: StructType = {
      StructType(Array(StructField("sum",LongType),StructField("count",LongType)))
    }

    // output data type
    override def dataType: DataType = DoubleType

    // whether keep the same output
    override def deterministic: Boolean = true

    // initialize buffer variable
    override def initialize(buffer: MutableAggregationBuffer): Unit = {
      // sum age
      buffer(0) = 0L
      // sum count
      buffer(1) = 0L
    }

    // update data
    override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
      if(!input.isNullAt(0)){
        buffer(0) = buffer.getLong(0) + input.getInt(0)
        buffer(1) = buffer.getLong(1) + 1
      }
    }

    // merge buffer's data
    override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
      buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0)
      buffer1(1) = buffer1.getLong(1) + buffer2.getLong(1)
    }

    // return the value
    override def evaluate(buffer: Row): Any = buffer.getLong(0).toDouble / buffer.getLong(1)
  }

}
