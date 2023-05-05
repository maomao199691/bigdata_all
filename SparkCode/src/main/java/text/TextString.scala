package text

import utils.DateUtil

/**
 * @ClassName: TextString
 * @Author: MaoMao
 * @date: 2022/7/1 9:35
 * @description:
 */
object TextString {
  def main(args: Array[String]): Unit = {
    val time = 1667381710787L
    //print(DateUtil.dateStr(time))
    //print(DateUtil.getDateNow())
    //print(DateUtil.getDaysBefore(new Date(),1))
    //print(DateUtil.getDaysLater(new Date(),1))
    //print(DateUtil.getNextDateStr("2022-11-02 20:09:14",2))
    //print(DateUtil.getYesterday())
    //print(DateUtil.getDateStrArrBetween("2022-11-01","2022-11-05"))
    //print(DateUtil.validDateString("2022-02-01"))
    //print(DateUtil.dayDiff("1996-09-11","2022-11-02"))
    //print(DateUtil.dateStr2Timestamp("2022-11-02"))
    print(DateUtil.currentDate)
  }
}