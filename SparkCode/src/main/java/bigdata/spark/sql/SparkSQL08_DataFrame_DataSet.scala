package bigdata.spark.sql

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

/**
 * @ClassName: SparkSQL08_DataFrame_DataSet
 * @Author: MaoMao
 * @date: 2022/7/2 9:16
 * @description:
 */
object SparkSQL08_DataFrame_DataSet {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder
      .master("local[*]")
      .appName("SparkSQl")
      .getOrCreate()

    import spark.implicits._

    // TODO 读取文件数据 构建新的数据模型： DataFrame
    val df: DataFrame = spark.read.json("data/user.json")

    // TODO 将DataFrame转换为DataFrame
    // DataSet 是面向对象进行操作的，所以DataFrame想要转换DataSet，需要补充类型
    // DataSet 数据模型出现的比较晚，所以DataFrame买有办法直接转换为DataSet
    // 如果DataFrame的结构信息和类型信息不匹配
    //  1.名称需要保持一致
    //  2.数据存在，但是如果匹配类型属性时，少了一些参数，那么访问类型时，会不方便
    //  3.如果转换时，属性的类型不匹配，也会出现错误
    //     转换时，有一个bigint类型，这个类型也可以理解为就是Long类型
    //     如果将文件中的整型数据，无论取值多少，转换为样例类中的
    val ds: Dataset[User] = df.as[User]
    ds.show()

    ds.foreach(
      user => {
        println(user.id + "," + user.name)
      }
    )
    spark.stop()
  }
  case class User(id:Long,name:String,age:Long)
}
