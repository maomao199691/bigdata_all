package bigdata.spark.test

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

/**
 * @ClassName: Test09_RDD_DataFrame_DataSet
 * @Author: MaoMao
 * @date: 2022/7/2 23:51
 * @description:
 */
object Test09_RDD_DataFrame_DataSet {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
          .master("local[*]")
          .appName("SparkSQL")
          .getOrCreate()

    //RDD -> DataFrame 需要隐式转换
    import spark.implicits._

    //读取 json 文件，创建 DataFrame
    val df: DataFrame = spark.read.json("data/test.json")

    //SQL风格语法
    df.createOrReplaceTempView("user")
    //spark.sql("select avg(age) from user").show()

    //DSL风格语法
    //df.select("username","age").show()

    // ****** RDD -> DataFrame -> DataSet ******
    val rdd: RDD[(Int, String, Int)] = spark.sparkContext
      .makeRDD(List((1, "zhangsan", 20), (2, "lisi", 30), (3, "wangwu", 40)))

    // RDD -> DataFrame
    val df1: DataFrame = rdd.toDF("id", "name", "age")

    // DataFrame -> DataSet
    val ds: Dataset[User] = df1.as[User]

    // ****** DataSet -> DataFrame -> RDD ******
    val df2: DataFrame = ds.toDF()

    // RDD 返回的RDD类型为Row，里面提供的 getXXX 方法可以获取字段值，类似 jdbc 处理结果集，但是索引从 0开始
    val rdd1: RDD[Row] = df2.rdd
    //rdd1.foreach(a => println(a.getString(1)))

    // ****** RDD => DataSet ******
    val ds1: Dataset[User] = rdd.map {
      case (id, name, age) => {
        User(id, name, age)
      }
    }.toDS()

    // ****** DataSet -> RDD ******
    ds.rdd.foreach(println)

    // 释放资源
    spark.stop()
  }
  case class User(id:Long,name:String,age:Long)
}
