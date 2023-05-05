package text

import java.text.SimpleDateFormat
import java.util.Date

/**
 * @ClassName: DateUtils
 * @Author: MaoMao
 * @date: 2022/11/26 11:30
 * @description:
 */
object DateUtils {

def pubTimeToDateStr(pub:String,dateFormat: String = "yyyy.MM.dd HH:mm:ss"):String ={
  val simpleDateFormat = new SimpleDateFormat(dateFormat)
  val date: Date = simpleDateFormat.parse(pub)

  val dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  val str: String = dateTime.format(date)

  str
}

  def main(args: Array[String]): Unit = {
    val dateStr: String = "2016.01.11 15:48:44"
    val str: String = pubTimeToDateStr(dateStr)
    print(str)
  }
}
