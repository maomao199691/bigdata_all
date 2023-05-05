package bigdata.spark.test01

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.util.AccumulatorV2

/**
 * @ClassName: Test01_CustomAccumulator
 * @Author: MaoMao
 * @date: 2022/7/3 11:27
 * @description: 使用自定义累加器进行求年龄平均值
 */
object Test01_CustomAccumulator {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
          .master("local[*]")
          .appName("SparkSQL")
          .getOrCreate()

    // 读取指定json
    val df: DataFrame = spark.read.json("data/user.json")

    // DataFrame -> RDD
    val rdd: RDD[Row] = df.rdd

    // 创建累加器
    val myAcc = new MyAcc()

    // 注册累加器
    spark.sparkContext.register(myAcc,"MyAvg")

    // 使用累加器
    rdd.foreach(a => myAcc.add(a.getLong(0)))

    // 获取累加器的值
    println(myAcc.value)

    // 关闭资源
    spark.stop()
  }

  // 自定义累加器
  class MyAcc extends AccumulatorV2[Long,Double]{

    // 缓冲区变量初始化
    var sum:Long = 0
    var count:Long = 0

    // 初始值是否为空
    override def isZero: Boolean = sum == 0 && count == 0

    // 复制累加器
    override def copy(): AccumulatorV2[Long, Double] = new MyAcc

    // 清空累加器
    override def reset(): Unit = {
      sum = 0
      count = 0
    }

    // 进行累加操作
    override def add(v: Long): Unit = {
      sum += v
      count += 1
    }

    // 合并累加器
    override def merge(other: AccumulatorV2[Long, Double]): Unit = {
      other match {
        case o:MyAcc => {
          sum += o.sum
          count += o.count
        }
      }
    }

    // 返回值
    override def value: Double = sum.toDouble / count
  }
}
