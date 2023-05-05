import scala.util.Random

/**
 * @ClassName: SortDemo
 * @Author: MaoMao
 * @date: 2022/11/1 11:05
 * @description: 冒泡、选择、快排、归并、二分查找
 */
object SortDemo {
  def main(args: Array[String]): Unit = {
    val ints = new Array[Int](10)
    val random = new Random()
    for (elem <- 0 to 9) {
      ints(elem) = random.nextInt(10 - 1)
    }

    val demo = new SortDemo

    //demo.BubleSort(ints)
    demo.ChoiceSort(ints)

    for (elem <- ints) {
      print(" " + elem)
    }
  }
}


class SortDemo{
  //冒泡
  def BubleSort(array: Array[Int]){
    for (i <- 0 until array.length) {
      for (j <- 0 until array.length - i - 1) {
         if (array(j) > array(j + 1)){
           val temp = array(j + 1)
           array(j + 1) = array(j)
           array(j) = temp
         }
      }
    }
  }

  //选择
  def ChoiceSort(array: Array[Int]): Unit ={
    for (i <- 0 until array.length - 1) {
      var min = i
      for (j <- i + 1 until array.length) {
        if (array(min) > array(j)){
          min = j
        }
      }
      if (min != i){
        val temp = array(min)
        array(min) = array(i)
        array(i) = temp
      }
    }
  }

  //快排
  def QuickSort(array: Array[Int]): Unit ={

  }

  //归并
  def MergeSort(array: Array[Int]): Unit ={

  }
}