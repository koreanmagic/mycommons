package kr.co.koreanmagic.commons;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;




public class DateUtils {
	
	
	private static SimpleDateFormat SQL_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	// 2000-00-00
	public static String toString(Date date) {
		return SQL_FORMAT.format(date);
	}
	public static String toString(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}
	
	// 특정 양식으로
	public static String dateFormatter(LocalDate localDate, String format) {
		DateTimeFormatter formmater = DateTimeFormatter.ofPattern(format);
		return localDate.format(formmater);
	}
	
	// Date  ==>  LocalDateTime
	public static LocalDateTime dateToLocalDateTime(Date date) {
		return getZone(date).toLocalDateTime();
	}
	// Date  ==>  LocalDate
	public static LocalDate dateToLocalDate(Date date) {
		return getZone(date).toLocalDate();
	}
	// Date  ==>  LocalTime
	public static LocalTime dateToLocalTime(Date date) {
		return getZone(date).toLocalTime();
	}
	// 공용메서드
	private static ZonedDateTime getZone(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault());
	}
	
	// LocalDateTime  ==>  Date
	public static Date localToDate(LocalDateTime time) {
		return Date.from( 
								time.atZone(ZoneId.systemDefault()).toInstant()
							);
	}
	// LocalDate  ==>  Date
	public static Date localToDate(LocalDate date) {
		return Date.from( 
				date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()
				);
	}
	
	private static final DateTimeFormatter ISO_LOCAL_DATE_TIME_20 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	// String ==> Date
	public static Date stringToDate(String date) {
		
		if(date.length() == 10) // yyyy-MM--dd
			return localToDate( LocalDate.parse(date) );
		else {
			return date.contains("T") ?
					localToDate( LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME) ) :	// yyyy-MM-ddTHH:mm:ss
						localToDate( LocalDateTime.parse(date, ISO_LOCAL_DATE_TIME_20) );						// // yyyy-MM-dd HH:mm:ss
		}
	}
	
	// 내일
	public static Date tomorrow() {
		return localToDate( LocalDate.now().plusDays(1) );
	}
	// 오늘 날짜
	public static Date today() {
		return localToDate( LocalDate.now() );
	}
	// 어제 구하기
	public static Date testerday() {
		return localToDate( LocalDate.now().minusDays(1) );
	}
	// 이번달 2015-{month}-01
	public static Date thisMonth() {
		return thisMonth(new Date());
	}
	public static Date thisMonth(Date date) {
		return localToDate( dateToLocalDate(date).withDayOfMonth(1) );
	}
	// 해당 1일
	public static Date addDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, day);
		
		return cal.getTime();
	}
	// 해당 1일
	public static Date addMonth(Date date, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);
		
		return cal.getTime();
	}
	
	
	
}
