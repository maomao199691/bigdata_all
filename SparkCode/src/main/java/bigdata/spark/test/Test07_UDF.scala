package bigdata.spark.test

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @ClassName: Test07_UDF
 * @Author: MaoMao
 * @date: 2022/7/2 14:08
 * @description:
 */
object Test07_UDF {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .master("local[*]")
      .appName("tes")
      .getOrCreate()

    // reade json file
    val df: DataFrame = spark.read.json("data/user.json")

    // register UDF
    spark.udf.register("addName",(x:String) => "Name:" + x)

    // create TmpView
    df.createOrReplaceTempView("user")

    // use UDF
    spark.sql(
      """
        |select addName(name) from user
        |""".stripMargin
    ).show()

    spark.stop()
  }
}
