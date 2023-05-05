package bigdata.spark.sql

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @ClassName: SparkSQL06_DataFrame_SQL
 * @Author: MaoMao
 * @date: 2022/7/2 1:58
 * @description:
 */
object SparkSQL06_DataFrame_SQL {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder
      .master("local[*]")
      .appName("SparkSQl")
      .getOrCreate()

    // TODO 读取文件数据 构建新的数据模型： DataFrame
    val dataFrame: DataFrame = spark.read.json("data/user.json")

    // TODO 使用DSL语法访问数据
    //      将DataFrame当成对象进行访问
    // select方法中列的名称应该和DataFrame中的数据结构保持一致，如果名称不对，数据查询会出现错误
    // 如果想要使用查询结果，进行操作，需要做特殊处理，使用特殊符号将列的名称转换成列的值，这个转换称为隐式转换
    // 所以需要导入隐式转换规则
    import spark.implicits._
    dataFrame.select( 'age + 1).show()

    spark.stop()
  }
}
