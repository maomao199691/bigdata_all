package bigdata.spark.sql

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @ClassName: SparkSQL04_DataFrame
 * @Author: MaoMao
 * @date: 2022/7/2 1:50
 * @description:
 */
object SparkSQL04_DataFrame {
  def main(args: Array[String]): Unit = {
    // TODO SparkSQL 的数据模型的构建和使用
    val spark: SparkSession = SparkSession.builder
      .master("local[*]")
      .appName("SparkSQl")
      .getOrCreate()

    // TODO 读取文件数据 构建新的数据模型： DataFrame
    val dataFrame: DataFrame = spark.read.json("data/user.json")

    // TODO 展示数据
    //dataFrame.show()

    // TODO 使用SQL的方式来进行访问
    // 需要提前准备数据表或视图，只对当前的连接(Session)有效
    dataFrame.createTempView("user")
    //dataFrame.createOrReplaceTempView("user")

    // TODO 如果希望创建的表或视图可以全局有效，那么需要将表创建为全局表
    //      原理就是将数据放置在特殊的库中，所以访问是，需要指定库名：global_temp
    dataFrame.createGlobalTempView("emp")

    // TODO 使用DSL语法访问数据
    //  将DataFrame 当成对象进行访问
    //dataFrame.select("name","age").show()
    // select 方法中列的名称应该和DataFrame中的数据结构保持一致，如果名不对，数据查询会出现错误
    // 如果想要使用查询结果，进行操作，需要做特殊处理，使用特殊符号将列的名称转换成列的值，这个转换为隐式转换
    // 所以需要导入隐式转换规则

    spark.newSession().sql(
      """
        |select avg(age) from global_Temp.emp
        |""".stripMargin
    ).show()

    spark.stop()
  }
}
