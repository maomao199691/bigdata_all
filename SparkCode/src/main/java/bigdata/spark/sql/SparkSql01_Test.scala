package bigdata.spark.sql

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @ClassName: SparkSql01_Test
 * @Author: MaoMao
 * @date: 2022/7/1 8:57
 * @description:
 */
object SparkSql01_Test {
  def main(args: Array[String]): Unit = {

    // TODO 读取JSON数据，计算用户的平均年龄
    // 所谓的JSON数据，其实就是符合JSON格式的数据
    // JSON：JavaScript Object Natation
    // JSON 文件：整个文件的内容要求符合JSON格式
    val conf = new SparkConf().setMaster("local[*]").setAppName("Test")
    val sc = new SparkContext(conf)

    //1.读取文件数据
    //  获取文件中的每一行的字符串内容
    val fileDatas: RDD[String] = sc.textFile("data/user.json")

    //2.分解字符串，获取年龄数据
    val ageDatas: RDD[(Int, Int)] = fileDatas.map(
      line => {
        //println(json.tail.init) 除去头部和尾部
        val datas: Array[String] = line.tail.init.split(",")
        val age: Int = datas.map(
          str => {
            val ds: Array[String] = str.split(":")
            (ds(0).trim.tail.init, ds(1).trim)
          }
        ).toMap.get("age").getOrElse(0).toString.toInt

        (age, 1)
      }
    )
    //3.将年龄汇总到一起，进行处理
    val tuple: (Int, Int) = ageDatas.reduce(
      (t1, t2) => {
        (t1._1 + t2._1, t1._2 + t2._2)
      }
    )

    //4.获取年龄平均值
    val i: Int = tuple._1 / tuple._2

    ageDatas.collect()
    println(i)

    sc.stop()
  }
}
