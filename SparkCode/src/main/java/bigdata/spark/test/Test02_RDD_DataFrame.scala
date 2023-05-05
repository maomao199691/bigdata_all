package bigdata.spark.test

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

/**
 * @ClassName: Test02_RDD_DataFrame
 * @Author: MaoMao
 * @date: 2022/7/2 10:38
 * @description:
 */
object Test02_RDD_DataFrame {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("SparkSQL")
      .getOrCreate()

    import spark.implicits._

    // 创建一个RDD
    val rdd: RDD[Int] = spark.sparkContext.makeRDD(List(1, 2, 3, 4))

    // 在转换时指定列名
    rdd.toDF("id").show()

    // 实际开发中一般通过样例类将 RDD 转换为 DataFrame
    val rdd1: RDD[User] = spark.sparkContext.makeRDD(
      List(
        (1001, "zhangsan", 20),
        (1002, "lisi", 30)
      )
    ).map { case (id, name, age) => User(id, name, age) }

    //通过样例类转换，样例类有列名，不用添加列名
    rdd1.toDF().show()

  }
  case class User(id:Long,name:String,age:Long)
}
