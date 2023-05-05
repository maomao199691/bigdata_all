package bigdata.spark.test

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
 * @ClassName: Test04_RDD_DataSet
 * @Author: MaoMao
 * @date: 2022/7/2 10:53
 * @description:
 */
object Test04_RDD_DataSet {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("SparkSQL")
      .getOrCreate()

    import spark.implicits._

    // 创建一个RDD
    val rdd: RDD[(Int, String, Int)] = spark.sparkContext.makeRDD(
      List(
        (1001, "zhangsan", 20),
        (1002, "lsii", 30)
      )
    )

    // RDD -> DataSet 需要添加列名和类型,通过样例类
    val rdd1: RDD[User] = rdd.map {
      case (id, name, age) => {
        User(id, name, age)
      }
    }

    // 通过 .toDS
    rdd1.toDS().show()

    spark.stop()

  }
  case class User(id:Long,name:String,age:Long)
}
