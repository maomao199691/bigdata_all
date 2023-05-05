package bigdata.spark.test

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.util.AccumulatorV2

/**
 * @ClassName: Test10_AvgAge
 * @Author: MaoMao
 * @date: 2022/7/3 0:37
 * @description:
 */
object Test10_AvgAge {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
          .master("local[*]")
          .appName("SparkSQL")
          .getOrCreate()

    // User DataFrame read json file
    val df: DataFrame = spark.read.json("data/test.json")

    // 转为 RDD
    val rdd1: RDD[Row] = df.rdd

    // 创建新的累加器
    val acc = new MyAvgAcc()

    // 注册累加器
    spark.sparkContext.register(acc,"AvgAge")

    // 使用累加器
    rdd1.foreach(a => acc.add(a.getLong(0).toInt))

    // 输出累计器结果
    println(acc.value)

    spark.stop()
  }

  // custom accumulator
 class MyAvgAcc extends AccumulatorV2[Int,Double] {

    // 进行累加的变量
    var sum:Int = 0
    var count:Int = 0

    // 是否为初始化状态
    override def isZero: Boolean = sum == 0 && count == 0

    // 复制累加器
    override def copy(): AccumulatorV2[Int, Double] = new MyAvgAcc

    // 重置累加器
    override def reset(): Unit = {
      sum = 0
      count = 0
    }

    // 累加操作
    override def add(v: Int): Unit = {
      sum += v
      count += 1
    }

    // 合并累加器
    override def merge(other: AccumulatorV2[Int, Double]): Unit = {
      other match {
        case o:MyAvgAcc => {
          sum += o.sum
          count += o.count
        }
        case _ =>
      }
    }

    // 最后输出的结果
    override def value: Double = sum / count
  }
}
