package bigdata.spark.sql

import org.apache.spark.sql.SparkSession

/**
 * @ClassName: SparkSQL22_SQL
 * @Author: MaoMao
 * @date: 2022/7/4 10:21
 * @description:
 */
object SparkSQL22_Seq_SQL {
  def main(args: Array[String]): Unit = {

    val spark: SparkSession = SparkSession.builder()
      .enableHiveSupport()
      .master("local[*]")
      .appName("SparkSQL")
      .getOrCreate()

    spark.sql("use default")

    spark.sql(
      """
        |select
        |	*
        |from(
        |	select
        |    	*,
        |    	rank() over (partition by area order by clickCnt desc) as rk
        |	from(
        |        select
        |            area,
        |            product_name,
        |            count(*) clickCnt
        |        from(
        |                select
        |                    a.*,
        |                    p.product_name,
        |                    c.city_name,
        |                    c.area
        |                from user_visit_action a
        |                         join product_info p on a.click_product_id = p.product_id
        |                         join city_info c on c.city_id = a.city_id
        |                where a.click_product_id != -1
        |            )t1 group by area, product_name
        |    )t2
        |)t3 where rk <=3""".stripMargin
    ).show()

    spark.stop()
  }
}
