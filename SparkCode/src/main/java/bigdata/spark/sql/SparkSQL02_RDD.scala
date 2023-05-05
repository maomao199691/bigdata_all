package bigdata.spark.sql

/**
 * @ClassName: SparkSQL02_RDD
 * @Author: MaoMao
 * @date: 2022/7/1 9:58
 * @description:
 */
object SparkSQL02_RDD {
  def main(args: Array[String]): Unit = {

    // TODO Spark Core => RDD
    // RDD 关心的数据本身，数据的格式并不关心
    // (1,"zhangsan",30)
    // TODO Spark RDD 不适合对结构化数据，半结构化数据进行处理
    //                更擅长对非结构化的数据进行处理：String
    // 结构化数据：数据库中的表数据
    // 半结构化数据：文件 + 结构(xml，json，html)
    // TODO 1、所以为了能够适合处理不同的数据，Spark需要封装特殊的功能模块

    // Spark基于 MR 框架开发的，优化了其中的运行过程，让执行性能得到了很大的提升。
    // 开发效率：MR开发效率比较低，基于Java开发的。(对象 + 问题)
    // 开发效率：Spark开发效率也不高，因为考虑分布式处理的模型RDD

    // Hive => SQL => MR (简化ME的开发)
    // ??? => SQL => RDD(简化Spark的开发)
    // TODO 2、为了能够简化RDD的开发，Spark需要封装特殊的功能模块

    // Spark + Hive => Shark(框架) => SQL => RDD
    // Shark => SQL => RDD(简化Spark的开发)

    // 为了降低Hive对框架（Shark）的制约，Shark框架衍生出两个技术路线
    // 1、Spark + SQL => SparkSQL 解析SQL，转换成RDD，执行
    //                    当前需要学习
    // 2、Hive On Spark => 解析SQL，转换成Spark引擎中的REDD，执行
    //                    数仓中要学习的

    // TODO SparkSQL学习重点
    //  1.SQL是后面学习数仓的重点
    //  2.因为RDD不适合结构化和半结构化数据的处理，所以需要封装特殊的数据模型
    //    所以学习的重点就是掌握数据模型的关系
    //  3.SQL 不是万能的，所以有很多的功能SQL实现不了，或者不好实现，那么需要采用SparkSQL实现

    // TODO SparkSQL的数据模型
    //  1.DataFrame = Data + Frame(结构化数据) 主要处理SQL
    //    弱类型处理模型，面向SQL

    // TODO
    //  3.Dataset = Data + Set(数据集) => 主要处理结果
    //    强类型处理模型，面向对象
  }
}
