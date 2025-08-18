package com.ocbg.xyz.apps.common.util

import android.content.pm.PackageManager
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * 有关时间的工具类
 * User: Tom
 * Date: 2024/4/2 16:42
 */
object MTimeUtil {

    private val tempCalendar1: Calendar? = null
    private val tempCalendar2: Calendar? = null

    /**
     * 格式化日期
     * @param pattern 样式
     * @param date 日期时间，单位毫秒
     * @return 格式化后的日期样式或null
     */
    fun formatData(pattern: String?, date: Long?): String? {
        if (pattern.isNullOrEmpty() || date == null) return null
        return try {
            SimpleDateFormat(pattern, Locale.CHINA).format(Date(date))
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    /**
     * 格式化日期
     * @param pattern 样式
     * @param date 日期时间，单位毫秒
     * @return 格式化后的日期样式或null
     */
    fun formatData(pattern: String?, date: Date?): String? {
        return formatData(pattern, date?.time)
    }

    /**
     * 是否是同一天
     */
    fun isSameDate(time1: Long, time2: Long): Boolean {
        val c1 = Calendar.getInstance()
        c1.timeInMillis = time1
        c1.set(Calendar.HOUR_OF_DAY, 0)
        c1.set(Calendar.MINUTE, 0)
        c1.set(Calendar.SECOND, 0)
        c1.set(Calendar.MILLISECOND, 0)

        val c2 = Calendar.getInstance()
        c2.timeInMillis = time2
        c2.set(Calendar.HOUR_OF_DAY, 0)
        c2.set(Calendar.MINUTE, 0)
        c2.set(Calendar.SECOND, 0)
        c2.set(Calendar.MILLISECOND, 0)

        return c1.compareTo(c2) == 0
    }

    /**
     * 是否是同一天
     */
    fun isSameDate(date1: Date, date2: Date): Boolean {
        return isSameDate(date1.time, date2.time)
    }

    /**
     * 获取指定时间的格式化结果
     * @param time 要比对的时间戳（毫秒）
     * @param df 日期格式化
     * @return 如果今天则返回"今天"，昨天则返回"昨天"，其他则根据[df]返回日期格式化后的字符串结果
     */
    fun getDateFormat(time: Long, df: DateFormat) = try {
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        val yesterday = Calendar.getInstance()
        yesterday.time = today.time
        yesterday.add(Calendar.DATE, -1)

        val target = Calendar.getInstance()
        target.timeInMillis = time
        target.set(Calendar.HOUR_OF_DAY, 0)
        target.set(Calendar.MINUTE, 0)
        target.set(Calendar.SECOND, 0)
        target.set(Calendar.MILLISECOND, 0)

        if (target.compareTo(today) == 0) {
            "今天"
        } else if (target.compareTo(yesterday) == 0) {
            "昨天"
        } else {
            df.format(Date(time))
        }
    } catch (tr: Throwable) {
        ""
    }

    fun getDateFormat(date: Date, df: DateFormat): String {
        return getDateFormat(date.time, df)
    }

    /**
     * 将毫秒时长格式为“MM:SS”的字符串格式。
     * @param millis 毫秒时长
     * @return 格式为“MM:SS”的字符串。
     */
    fun formatMillisToMinutesAndSeconds(millis: Long): String {
        val totalSeconds = millis / 1000
        val minutes = (totalSeconds / 60).toInt()
        val seconds = (totalSeconds % 60).toInt()
        return String.format("%02d:%02d", minutes, seconds)
    }

    /**
     * 将秒时长格式为“MM:SS”的字符串格式。
     * @param seconds 秒时长
     * @return 格式为“MM:SS”的字符串。
     */
    fun formatSecondsToMinutesAndSeconds(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    /**
     * 计算今天距离未来某天还差多少天
     *
     * @param dateL 未来某天
     * @return 相差的天数
     */
    fun daysToSomeday(dateL: Long): Int {
        val date = Date(dateL)
        //未来日期
        val somedayLocalDate = date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
        //今天的日期
        val todayLocalDate = LocalDate.now();
        // 计算两个日期之间的天数差
        val daysDifference = ChronoUnit.DAYS.between(todayLocalDate, somedayLocalDate);
        return daysDifference.toInt()
    }

    /**
     * 时间戳转为日期格式（09.26）
     *
     * @param timestamp 时间戳
     * @return 09.26这种格式的日期
     */
    fun formatTimestampToDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("MM.dd", Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }

    /**
     * 毫秒数转为“20分18秒”格式
     *
     * @param millis 毫秒数
     * @return “20分18秒”格式
     */
    fun formatMillisToMinutesAndSeconds2(millis: Long): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "${minutes}分${seconds}秒"
    }

    /**
     * 将秒数转为“08:18”格式
     *
     * @param seconds 秒数
     * @return “08:18”格式
     */
    fun formatSecondsToMinuteAndSecond(seconds: Long): String {
        try {
            val minute = seconds / 60
            val second = seconds % 60
            return "%02d:%02d".format(minute, second)
        } catch (tr: Throwable) {
            return ""
        }
    }

    /**
     * 获取今天的日期
     *
     * @return
     */
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }


    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("MM-dd", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun formatMillisecondsToDate(milliseconds: Long): String {
        val date = Date(milliseconds)
        val dateFormat = SimpleDateFormat("MM-dd", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun formatMillisecondsToDate1(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    /**
     * 毫秒数转换
     *
     * @param milliseconds 毫秒数
     * @return 如果超过1小时，返回“hh:mm:ss”；不超过1小时，返回“mm:ss”
     */
    fun formatDuration(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0) {
            // 如果超过一小时，返回 hh:mm:ss 格式
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            // 如果没有超过一小时，返回 mm:ss 格式
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    /**
     * 毫秒数转换
     *
     * @param milliseconds 毫秒数
     * @return 如果超过1小时，返回“hh:mm:ss”；不超过1小时，返回“mm:ss”
     */
    fun formatDuration2(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0) {
            // 如果超过一小时，返回 hh:mm:ss 格式
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            // 如果没有超过一小时，返回 mm:ss 格式
            String.format("00:%02d:%02d", minutes, seconds)
        }
    }

}