/**
 * @Author: MaoMao
 * @date: 2022/10/31 20:31
 * @description:
 */
class Test {
  def sum(i:Int,j:Int): Unit ={
    val i1: Int = i + j


  }
}

object Test{
  def main(args: Array[String]): Unit = {

    //printf("name: %s age: %d\n", "lili",8)

    val name = "lihai"
    val age = 8

    val s = s"name: $name,age: ${age}"
    println(
      """我
        |是
        |一首
        |诗
        |""".stripMargin)
  }
}