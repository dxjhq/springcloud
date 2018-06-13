package com.hhly.utils.date;

import com.hhly.utils.ValueUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


/**
 * 处理时间工具类
 * @author wangxianchen
 *
 */
public class DateUtil {
	private static String[] MONTHS = new String[] { "一月", "二月", "三月", "四月",
			"五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" };

	private static int[] DOMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31,
			30, 31 };
	private static int[] lDOMonth = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31,
			30, 31 };

	/** 当前时间 */
	public static Date now() {
		return new Date();
	}

	/**
	 * 格式化日期的字符串
	 * @param date
	 * @return
	 */
	public static String formatDateToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormatType.TZ.getValue());
		return sdf.format(date);
	}
	public static String formatDateToString(Date date,String dateFormat) {
		if(date == null){
			return "";
		}
		if(StringUtils.isEmpty(dateFormat)){
			return formatDateToString(date);
		}
		return new SimpleDateFormat(dateFormat).format(date);
	}
	public static String formatDateToString(Object dateObj, String dateFormat) {
		if(dateObj == null){
			return "";
		}

		if(dateObj instanceof Date){
			return formatDateToString((Date)dateObj,dateFormat);
		}else{
			return "";
		}
	}

	/**
	 * 取日期是一个月中的几号.
	 *
	 * @param date
	 * @return
	 */
	public static int getDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Object day = cal.get(Calendar.DATE);
		if (day != null) {
			return Integer.valueOf(day.toString());
		} else {
			return 0;
		}
	}

	/**
	 * 取日期是哪一年
	 *
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Object year = cal.get(Calendar.YEAR);
		if (year != null) {
			return Integer.valueOf(year.toString());
		} else {
			return 0;
		}
	}

	/**
	 * 取日期是月份.
	 *
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		//cal.add(Calendar.MONTH, 1);
		Object day = cal.get(Calendar.MONTH);
		if (day != null) {
			return Integer.valueOf(day.toString())+1;
		} else {
			return 0;
		}
	}

	/***得到某一日期是星期几***/
	public static String getWeekDay(String dateTime) {
		if(ValueUtil.isBlank(dateTime)) return "";
		String dayNames[] = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
		Calendar c = Calendar.getInstance();// 获得一个日历的实例
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormatType.YYYY_MM_DD.getValue());
		try {
			c.setTime(sdf.parse(dateTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dayNames[c.get(Calendar.DAY_OF_WEEK)-1];
	}

	/**
	 * 判断两个时间的大小.
	 *
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static boolean isCompareTime(Date startTime, Date endTime) {
		if (endTime.getTime() > startTime.getTime()) {
			return true;
		} else {
			return false;
		}

	}
	/**
	 * 得到两个日期之间的分钟差
	 * <pre>
	 * DateUtil.getMinuteInterval(  2017-5-4 15:00:08 ,2017-5-4 15:51:08  ) = 51
	 * DateUtil.getMinuteInterval(  2017-5-4 15:00:34 ,2017-5-4 15:51:34  ) = 51
	 * DateUtil.getMinuteInterval(  2016-5-4 15:56:30 ,2017-5-4 15:56:30  ) = 525600
	 * </pre>
	 *
	 * @param a : Date 类型,不分前后顺序
	 * @param b : Date 类型,不分前后顺序
	 * @return 日期之间的分钟间隔
	 */
	public static long getMinuteInterval ( Date a, Date b ) {
		return Math.abs( ( a.getTime() - b.getTime() ) / ( 1000 * 60 ) );
	}

	/**
	 * 一个月的天数
	 * @param month
	 * @return
	 */
	public static int getDaysOfmonth(int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, month);
		if ((cal.get(Calendar.YEAR) % 4) == 0) {
			if ((cal.get(Calendar.YEAR) % 100) == 0
					&& (cal.get(Calendar.YEAR) % 400) != 0)
				return DOMonth[cal.get(Calendar.MONTH)];
			return lDOMonth[cal.get(Calendar.MONTH)];
		} else
			return DOMonth[cal.get(Calendar.MONTH)];
	}

	public static Calendar getClearCalendar() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	public static Date getDateAfterDays(Date date, int duration) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, duration);
		return cal.getTime();
	}
	
	public static Date getDateEndAfterDays(Date date, int duration) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, duration);
        return getDayEnd(cal.getTime());
    }

	public static Date getDateBeforeHours(Date date, int duration) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, -duration);
		return cal.getTime();
	}

	public static Date getDateAfterMinutes(long duration) {
		long curr = System.currentTimeMillis();
		curr = curr + duration * 60 * 1000;
		return new Date(curr);
	}

	public static int getDayOfWeek(Calendar cal) {// 得到每月1号是星期几
		cal.set(Calendar.DATE, 1);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static Date getTheMiddle(Date date, int plus) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, plus);
		return cal.getTime();
	}

	/**
	 * 取得指定日期 N 天后的日期
	 *
	 * @param day 正数表示多少天后, 负数表示多少天前
	 */
	public static Date addDays(Date date, int day) {
		return new DateTime(date).plusDays(day).toDate();
	}
	/**
	 * 取得指定日期 N 个月后的日期
	 *
	 * @param month 正数表示多少月后, 负数表示多少月前
	 */
	public static Date addMonths(Date date, int month) {
		return new DateTime(date).plusMonths(month).toDate();
	}
	/**
	 * 取得指定日期 N 天后的日期
	 *
	 * @param year 正数表示多少年后, 负数表示多少年前
	 */
	public static Date addYears(Date date, int year) {
		return new DateTime(date).plusYears(year).toDate();
	}
	/**
	 * 取得指定日期 N 分钟后的日期
	 *
	 * @param minute 正数表示多少分钟后, 负数表示多少分钟前
	 */
	public static Date addMinute(Date date, int minute) {
		return new DateTime(date).plusMinutes(minute).toDate();
	}
	/**
	 * 取得指定日期 N 小时后的日期
	 *
	 * @param hour 正数表示多少小时后, 负数表示多少小时前
	 */
	public static Date addHours(Date date, int hour) {
		return new DateTime(date).plusHours(hour).toDate();
	}
	/**
	 * 取得指定日期 N 秒后的日期
	 *
	 * @param second 正数表示多少秒后, 负数表示多少秒前
	 */
	public static Date addSeconds(Date date, int second) {
		return new DateTime(date).plusSeconds(second).toDate();
	}
	/**
	 * 取得指定日期 N 周后的日期
	 *
	 * @param week 正数表示多少周后, 负数表示多少周前
	 */
	public static Date addWeeks(Date date, int week) {
		return new DateTime(date).plusWeeks(week).toDate();
	}

	private static Date clean(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static Date getAfterDay(Date date) {
		SimpleDateFormat simpleOldDate = new SimpleDateFormat(
				"yyyy-MM-dd 00:00:00");
		Calendar ca = Calendar.getInstance();
		try {
			ca.setTime(simpleOldDate.parse(simpleOldDate.format(date)));
		} catch (ParseException e) {
			ca.setTime(getDayStart(new Date()));
		}
		ca.add(Calendar.DATE, 1);
		return ca.getTime();

	}

	public static Date getBeforeDay(Date date) {
		SimpleDateFormat simpleOldDate = new SimpleDateFormat(
				"yyyy-MM-dd 00:00:00");
		Calendar ca = Calendar.getInstance();
		try {
			ca.setTime(simpleOldDate.parse(simpleOldDate.format(date)));
		} catch (ParseException e) {
			ca.setTime(getDayStart(new Date()));
		}
		ca.add(Calendar.DATE, -1);
		return ca.getTime();

	}

	/**
	 * 返回 当前凌晨时间
	 * @param date
	 * @return
	 */
	public static Date getDayStart(Date date) {
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	/**
	 * 返回 当前深夜时间
	 * @param date
	 * @return
	 */
	public static Date getDayEnd(Date date) {
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 将日期和时分的两个时间合并到一起
	 *
	 * @param date
	 * @param time
	 * @return
	 */
	public static Date mergeDateTime(Date date, Date time) {
		Calendar dateCalendar = Calendar.getInstance();
		dateCalendar.setTime(date);
		Calendar timeCalendar = Calendar.getInstance();
		timeCalendar.setTime(time);
		dateCalendar.set(Calendar.HOUR, timeCalendar.get(Calendar.HOUR));
		dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
		dateCalendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));
		return dateCalendar.getTime();
	}

	public static Date getDateByStr(String dateStr, String formate) {
		SimpleDateFormat sdf = new SimpleDateFormat(formate);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 字符串转换到时间格式
	 *
	 * @param dateStr
	 *            需要转换的字符串
	 * @param formatStr
	 *            需要格式的目标字符串 举例 yyyy-MM-dd
	 * @return Date 返回转换后的时间
	 * @throws ParseException
	 *             转换异常
	 */
	public static Date convertDate(String dateStr, String formatStr) {
		if(StringUtils.isEmpty(formatStr)){
			formatStr = DateFormatType.TZ.getValue();
		}
		DateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
            if(dateStr == null || dateStr.length() <= 0){
                return date;
            }
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	public static Date convertDate(String dateStr) {
		return convertDate(dateStr);
	}

	/**
	 * 获取相差的分
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Long getMinBetween(Date startDate, Date endDate) {
		Calendar d1 = Calendar.getInstance();
		d1.setTime(startDate);
		Calendar d2 = Calendar.getInstance();
		d2.setTime(endDate);
		Long min = 0L;
		if (d1.getTimeInMillis() > d2.getTimeInMillis()) {
			min = (d1.getTimeInMillis() - d2.getTimeInMillis()) / (1000 * 60);
		} else {
			min = (d2.getTimeInMillis() - d1.getTimeInMillis()) / (1000 * 60);
		}
		return min;
	}

	/**
	 * 获取相差的月
	 * @param startDate
	 * @param endDate
     * @return
     */
	public static int getMonBetween(Date startDate,Date endDate){
		int hours=(int) ((endDate.getTime() - startDate.getTime())/3600000);
		int hoursOneMon = 24*30;//一个月的小时数
		return hours/hoursOneMon;
	}
	/**
	 * 相差秒数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Long getMillisBetween(Date startDate, Date endDate) {
		Calendar d1 = Calendar.getInstance();
		d1.setTime(startDate);
		Calendar d2 = Calendar.getInstance();
		d2.setTime(endDate);
		Long min = 0L;
		if (d1.getTimeInMillis() > d2.getTimeInMillis()) {
			min = (d1.getTimeInMillis() - d2.getTimeInMillis())/1000;
		} else {
			min = (d2.getTimeInMillis() - d1.getTimeInMillis())/1000;
		}
		return min;
	}

	/**
	 * 相差秒数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Long getMillisBetweenSecod(Date startDate, Date endDate) {
		Calendar d1 = Calendar.getInstance();
		d1.setTime(startDate);
		Calendar d2 = Calendar.getInstance();
		d2.setTime(endDate);
		Long min = 0L;
		min = (d2.getTimeInMillis() - d1.getTimeInMillis())/1000;
		return min;
	}

	/**
	 * 获取今天的日期，去掉时、分、秒
	 */
	public static Date getTodayYMDDate() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);// 24小时制
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	/**
	 * 得到今天几点
	 * @return
	 */
	public static int getTodayHourDate(){
		Calendar cal = Calendar.getInstance();
		int hour=cal.get(Calendar.HOUR_OF_DAY);
		return hour;
	}

	/**
	 * 将分钟转成小时数,有小误差
	 */
	public static float convertToHours(Long minutes) {
		if (minutes == null) {
			return 0f;
		}
		String fStr = (new Float(minutes + "") / 60) + "";
		String res = fStr.substring(0, fStr.lastIndexOf(".") + 2);
		return Float.parseFloat(res);
	}

	/**
	 * 将小时数转为分钟,有小误差
	 */
	public static long convertToMinutes(Float hours) {
		if (hours == null) {
			return 0l;
		}
		long h = hours.intValue() * 60;
		Float f = (hours - new Float(hours.intValue())) * 60;
		String fStr = f.toString();
		return h + Long.parseLong(fStr.substring(0, fStr.indexOf(".")));
	}

	/**
	 * 本月第一天
	 */
	public static Date getFirstdayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 * 本月最后一天
	 */
	public static Date getLastdayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, value);
		date = cal.getTime();
		return date;
	}

	/**
	 * date 减去当前日期 . 剩余0天0时0分
	 *
	 * @return str
	 */
	public static String getRemainTimeByCurrentDate(Date date) {
		String str = "剩余0天0时0分";
		if (null != date) {
			Date d = new Date();
			long seconds = (date.getTime() - d.getTime()) / 1000;
			if (seconds > 0) { // 秒
				long day = seconds / (3600 * 24); // 天数
				long house = (seconds % (3600 * 24)) / 3600; // 小时
				long min = (seconds % (3600)) / 60;// 分
				return "剩余" + day + "天" + house + "时" + min + "分";
			}

		}
		return str;
	}

	/**
	 * 返回当前时间 毫秒
	 * @param date
	 * @return
	 */
	public static long getDateTime(String date) {
		if (StringUtils.isEmpty(date)) {
			return 0l;
		}
		Date d = convertDate(date, "yyyy-MM-dd");
		return d.getTime();
	}
	
	/**
	 * 时间比较
	 * @param dt1
	 * @param dt2
	 * @return
	 */
	public static int compareDate(Date dt1,Date dt2){
        if (dt1.getTime() > dt2.getTime()) {
            //System.out.println("dt1 在dt2前");
            return 1;
        } else if (dt1.getTime() < dt2.getTime()) {
            //System.out.println("dt1在dt2后");
            return -1;
        } else {//相等
            return 0;
        }
	}

	/**
	 * 在特定的日期上，加入特定的时间点，返回新的日期
	 * @param date 特定日期
	 * @param time 给入的时间点
	 * 				  time 的格式举例：09:00
	 * @return
	 */
	public static Date defineDate(Date date, String time) throws Exception{
		try {
			String []strs = time.split(":");
			int hour =Integer.parseInt(strs[0]);
			int minute = Integer.parseInt(strs[1]);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.set(Calendar.HOUR_OF_DAY, hour);
			c.set(Calendar.MINUTE, minute);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return c.getTime();
		} catch (Exception e) {
			throw new Exception("给入的时间点参数格式错误！");
		}
	}

	public static String minutesToDate(Long minutes) {
		if(minutes == null) {
			return "";
		}
		double time = (double)minutes;
		int day = 0;
		int hour = 0;
		int minute = 0;
		if (time > 0) {
			day = (int)Math.ceil(time / 1440);
			if (time % 1440 == 0) {
				hour = 0;
				minute = 0;
			} else {
				hour = (int)(1440 - time % 1440) / 60;
				minute = (int)(1440 - time % 1440) % 60;
			}
		} else if (time < 0) {
			time = -time;
			hour = (int)time / 60;
			minute = (int)time % 60;
		}
		String hourStr = "" + hour, minuteStr = "" + minute;
		if (hour < 10)
			hourStr = "0" + hour;
		if (minute < 10)
			minuteStr = "0" + minute;
		return day + "天" + hourStr + "点" +minuteStr + "分";
	}

	public static Date getTimeStamp(){
		Date date = new Date();
		try {
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			date = new SimpleDateFormat(DateFormatType.YYYY_MM_DD_HH_MM_SS.getValue()).parse(ts.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取某年最后一天日期
	 * @param year 年份
	 * @return Date
	 */
	public static Date getCurrYearLast(int year){
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();

		return currYearLast;
	}

	/**
	 * 获取上周开始时间。
	 * @param currentDate
	 * @return
	 */
	public static Date getLastWeekStart(Date currentDate){

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		//n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
		int n = -1;
		calendar.add(Calendar.DATE, n*7);
		//想周几，这里就传几Calendar.MONDAY（TUESDAY...）
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		//获取 当前周 周一的date。
		Date monday = calendar.getTime();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String mondayStr = new SimpleDateFormat("yyyyMMdd").format(monday);
		Date mondayStart = null;
		try {
			mondayStart = new SimpleDateFormat("yyyyMMddHHmmss").parse(mondayStr+"000000");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return mondayStart;
	}

	/**
	 * 获取上周结束时间。
	 * @param currentDate
	 * @return
	 */
	public static Date getLastWeekEnd(Date currentDate){
		Calendar calendar = Calendar.getInstance(Locale.CHINESE);
		calendar.setTime(currentDate);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		//n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
		int n = -1;
		calendar.add(Calendar.DATE, n*7);
		//想周几，这里就传几Calendar.MONDAY（TUESDAY...）
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		Date sunday = calendar.getTime();
		String sundayStr = new SimpleDateFormat("yyyyMMdd").format(sunday);
		Date sundayEnd = null;
		try {
			sundayEnd = new SimpleDateFormat("yyyyMMddHHmmss").parse(sundayStr+"235959");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sundayEnd;
	}

	public static Date getCurrentWeekStart(Date currentDate){

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		//n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
		int n = 0;
		calendar.add(Calendar.DATE, n*7);
		//想周几，这里就传几Calendar.MONDAY（TUESDAY...）
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		//获取 当前周 周一的date。
		Date monday = calendar.getTime();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String mondayStr = new SimpleDateFormat("yyyyMMdd").format(monday);
		Date mondayStart = null;
		try {
			mondayStart = new SimpleDateFormat("yyyyMMddHHmmss").parse(mondayStr+"000000");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return mondayStart;
	}

	public static Date getCurrentWeekEnd(Date currentDate){
		Calendar calendar = Calendar.getInstance(Locale.CHINESE);
		calendar.setTime(currentDate);
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		//n为推迟的周数，1本周，-1向前推迟一周，2下周，依次类推
		int n = 0;
		calendar.add(Calendar.DATE, n*7);
		//想周几，这里就传几Calendar.MONDAY（TUESDAY...）
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		Date sunday = calendar.getTime();
		String sundayStr = new SimpleDateFormat("yyyyMMdd").format(sunday);
		Date sundayEnd = null;
		try {
			sundayEnd = new SimpleDateFormat("yyyyMMddHHmmss").parse(sundayStr+"235959");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sundayEnd;
	}

	/**
	 * 获取 date 在一年中为第几周. 每周的起始日为 周一。
	 * @param date
	 * @return
     */
	public static int getWeekOfYear(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
		return weekOfYear;
	}


	public static LocalDate date2LocalDate(Date date) {
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		LocalDate localDate = localDateTime.toLocalDate();

		return localDate;
	}


}
