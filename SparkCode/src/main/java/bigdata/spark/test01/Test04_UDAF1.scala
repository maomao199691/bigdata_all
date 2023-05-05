package bigdata.spark.test01

import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{DataFrame, Dataset, Encoder, Encoders, SparkSession, TypedColumn}

/**
 * @ClassName: Test04_UDAF1
 * @Author: MaoMao
 * @date: 2022/7/3 11:28
 * @description: 使用强类型 UDAF (输入类型和缓冲区都用样例类)
 */
object Test04_UDAF1 {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
          .master("local[*]")
          .appName("SparkSQL")
          .getOrCreate()

    // DataFrame -> DataSet 需要隐式转换
    import spark.implicits._

    // 读取指定json文件
    val df: DataFrame = spark.read.json("data/user.json")

    // DataFrame -> DataSet 添加对象属性
    val ds: Dataset[User] = df.as[User]

    // 创建聚合函数
    val myAvg = new MyAvg()

    // 将聚合函数转换成列
    val col: TypedColumn[User, Double] = myAvg.toColumn

    // 使用 DSL 语法查询
    ds.select(col).show()

    // 关闭资源
    spark.stop()
  }
  // DataFrame -> DataSet 需要添加对象属性
  case class User(id:Long,name:String,age:Long)
  // 自定义聚合函数中用作缓冲区的样例类
  case class Buff(var sum:Long,var count:Long)

  // 自定义聚合函数
  class MyAvg extends Aggregator[User,Buff,Double]{

    // 初始化缓冲区
    override def zero: Buff = {
      Buff(0L,0L)
    }

    // 对数据进行处理
    override def reduce(buff: Buff, user: User): Buff = {
      buff.sum += user.age
      buff.count += 1
      buff
    }

    // 合并缓冲区
    override def merge(b1: Buff, b2: Buff): Buff = {
      b1.count += b2.count
      b1.sum += b2.sum
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
