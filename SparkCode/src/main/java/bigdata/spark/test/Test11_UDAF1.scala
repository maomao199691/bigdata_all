package bigdata.spark.test

import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{DataFrame, Dataset, Encoder, Encoders, SparkSession, TypedColumn}

/**
 * @ClassName: Test11_UDAF1
 * @Author: MaoMao
 * @date: 2022/7/3 10:04
 * @description:
 */
object Test11_UDAF1 {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
          .master("local[*]")
          .appName("SparkSQL")
          .getOrCreate()

    // DataFrame -> DataSet 需要隐式转换
    import spark.implicits._

    // 读取json文件
    val df: DataFrame = spark.read.json("data/user.json")

    // 将DataFrame转换为DataSet
    val ds: Dataset[User] = df.as[User]

    // 创建新的聚合函数
    val myAvg = new MyAvg()

    // 将聚合函数转换成要查询的列
    val col: TypedColumn[User, Double] = myAvg.toColumn

    // 使用 DSL 语法查询
    ds.select(col).show()

    // 关闭资源
    spark.stop()
  }
  // 创建用于封装json的样例类
  case class User(id:Long,name:String,age:Long)

  // 用作缓冲区的样例类
  case class BufferAvg(var sum:Long,var count:Long)

  // 使用强类型的 UDAF 继承 org.apache.spark.sql.expressions.Aggregator
  class MyAvg extends  Aggregator[User,BufferAvg,Double]{

    // 初始化缓冲区
    override def zero: BufferAvg = BufferAvg(0L,0L)

    // 处理数据
    override def reduce(buff: BufferAvg, user: User): BufferAvg = {
      buff.sum += user.age
      buff.count += 1
      buff
    }

    // 合并缓冲区
    override def merge(b1: BufferAvg, b2: BufferAvg): BufferAvg = {
      b1.sum += b2.sum
      b1.count += b2.count
      b1
    }

    // 返回最后结果
    override def finish(reduction: BufferAvg): Double = reduction.sum.toDouble / reduction.count

    // 对缓冲区数据做编码处理
    override def bufferEncoder: Encoder[BufferAvg] = {
      Encoders.product
    }

    // 对输出数据做编码处理
    override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
  }
}
