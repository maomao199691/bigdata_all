package utils

import org.joda.time.DateTime

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import scala.collection.mutable.ListBuffer

/**
 * @ClassName: DateUtil
 * @Author: MaoMao
 * @date: 2022/11/2 17:20
 * @description: 日期工具类
 */
object DateUtil {
  def dateStr(milliseconds: Long,formatDate: String="yyyy-MM-dd"): String = {
    val dateTime = new DateTime(milliseconds)
    dateTime.toString(formatDate)
  }

  def getDateNow(formatDate: String="yyyy-MM-dd"): String={
    val now:Date = new Date()
    val dateFormat:SimpleDateFormat = new SimpleDateFormat()
    val dt: String = dateFormat.format(now)
    dt
  }

  def getDaysBefore(dt:Date,interval:Int,formatDate:String="yyyy-MM-dd"):String={
    val dateFormat = new SimpleDateFormat(formatDate)
    val cal: Calendar = Calendar.getInstance()
    cal.setTime(dt)
    cal.add(Calendar.DATE, - interval)
    val yesterday: String = dateFormat.format(cal.getTime())
    yesterday
  }

  def getDaysLater(dt:Date,interval:Int,formatDate:String="yyyy-MM-dd"):String={
    val dateFormat = new SimpleDateFormat(formatDate)
    val cal: Calendar = Calendar.getInstance()
    cal.setTime(dt)
    cal.add(Calendar.DATE, + interval)
    val tomorrow: String = dateFormat.format(cal.getTime())
    tomorrow
  }

  def getNextDateStr(reportDate:String,interval:Int,formatDate:String="yyyy-MM-dd"):String={
    val dateFormat = new SimpleDateFormat(formatDate)
    val date: Date = dateFormat.parse(reportDate)
    val str: String = getDaysLater(date, interval, formatDate)
    str
  }

  def getYesterday(formatDate:String="yyyy-MM-dd"): String={
    val dt: Date = new Date()
    val yesterday: String = getDaysBefore(dt, 1, formatDate)
    yesterday
  }

  def getDateStrArrBetween(start: String, end: String, formatDate: String= "yyyy-MM-dd")={
    val startDate: Date = new SimpleDateFormat(formatDate).parse(start)
    val endDate: Date = new SimpleDateFormat(formatDate).parse(end)
    val dateFormat = new SimpleDateFormat(formatDate)
    val buffer = new ListBuffer[String]
    buffer += dateFormat.format(startDate.getTime)
    val tempStart: Calendar = Calendar.getInstance()
    tempStart.setTime(startDate)
    tempStart.add(Calendar.DAY_OF_YEAR,1)
    val tempEnd: Calendar = Calendar.getInstance()
    tempEnd.setTime(endDate)
    while (tempStart.before(tempEnd)){
      buffer += dateFormat.format(tempStart.getTime)
      tempStart.add(Calendar.DAY_OF_YEAR,1)
    }
    buffer += dateFormat.format(endDate.getTime)
    buffer.toList
  }

  def validDateString(dateString: String, dateFormat: String = "yyyy-MM-dd"): Boolean = try{
    val format = new SimpleDateFormat(dateFormat)
    format.parse(dateString)
    true
  } catch {
    case _: Exception => false
  }

  def dayDiff(day1: String,day2: String,dateFormat: String = "yyyy-MM-dd") = {
    val dt1: Date = new SimpleDateFormat(dateFormat).parse(day1)
    val dt2: Date = new SimpleDateFormat(dateFormat).parse(day2)

    val between: Long = dt2.getTime - dt1.getTime
    val day: Long = between / 1000 / 3600 / 24
    day
  }

  def dateStr2Timestamp(reportDate: String): Long = {
    val format = new SimpleDateFormat("yyyy-MM-dd")
    format.parse(reportDate).getTime
  }

  def currentDate: String = {
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime)
  }
}
