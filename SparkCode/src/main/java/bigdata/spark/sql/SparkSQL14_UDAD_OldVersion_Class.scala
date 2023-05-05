package bigdata.spark.sql

import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{DataFrame, Dataset, Encoder, Encoders, SparkSession}

/**
 * @ClassName: SparkSQL14_UDAD_OldVersion_Class
 * @Author: MaoMao
 * @date: 2022/7/2 14:49
 * @description:
 */
object SparkSQL14_UDAD_OldVersion_Class {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("SparkSQL")
      .getOrCreate()

    import spark.implicits._

    val df: DataFrame = spark.read.json("data/user.json")

    // TODO Spark3.0以前的版本，强类型UDAF函数不能应用于SQl文件
    // DataFrame => 弱类型UDAF => SQL
    // DataSet => 强类型UDAF => DSL => Object(Class)
    val ds: Dataset[User] = df.as[User]

    // TODO 因为我们使用的是UDAF函数，所以聚合结果应该只有一列，所以select方法中传递一列
    //      但时我们这一列的数据是通过UDAF计算出来，所以需要将UDAF


    spark.stop()
  }
  case class User(id:Long,name: String,age: Long)
  case class AvgAgeBuffer(var total:Long,var cnt:Long)

  // TODO 自定义聚合函数 UDAF(早期版本，强类型)
  // 1.继承类：Aggregator
  // 2.定义泛型 In: User对象 BUF:AvgAgeBuffer OUT:Long
  class MyAvgAgeUDAF extends Aggregator[User,AvgAgeBuffer,Long]{

    // 缓冲区初始化
    override def zero: AvgAgeBuffer = AvgAgeBuffer(0L,0L)

    // 输入的数据
    override def reduce(buff: AvgAgeBuffer, user: User): AvgAgeBuffer = {
      buff.total += user.age
      buff.cnt += 1
      buff
    }

    // 合并缓冲区
    override def merge(b1: AvgAgeBuffer, b2: AvgAgeBuffer): AvgAgeBuffer = {
      b1.total += b2.total
      b1.cnt += b2.cnt
      b1
    }

    // 完成计算
    override def finish(reduction: AvgAgeBuffer): Long = {
      reduction.total / reduction.cnt
    }

    // 对输入数据的进行编码处理
    override def bufferEncoder: Encoder[AvgAgeBuffer] = {
      Encoders.product
    }

    // 对输出数据进行编码处理
    override def outputEncoder: Encoder[Long] = {
      Encoders.scalaLong
    }
  }
}
