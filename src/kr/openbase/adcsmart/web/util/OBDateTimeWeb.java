package kr.openbase.adcsmart.web.util;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import kr.openbase.adcsmart.service.utility.OBException;

public class OBDateTimeWeb {
	/**
	 * 지정된 시간(msec)만큼 sleep한다.
	 * 
	 * @param interval
	 */
	public static void Sleep(int interval) {
		try {
			Thread.sleep(interval);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

//		OBSystem.INSTANCE.Sleep(interval);
//		try
//		{
//			Thread.sleep(interval);
//		}
//		catch(InterruptedException e)
//		{
//			OBSystemLog.info(OBDefine.LOGFILE_SYSLOG_STAT, String.format("xxxxxx:%s", e.getMessage()));
//		}
//		catch(Exception e)
//		{
//			OBSystemLog.info(OBDefine.LOGFILE_SYSLOG_STAT, String.format("1111:%s", e.getMessage()));
//		}
	}

	/**
	 * 현재의 시간을 yyyy-MM-dd HH:mm:ss.SSS 형태의 문자열로 제공한다.
	 * 
	 * @return yyyy-MM-dd HH:mm:ss.SSS 형태의 현재 시간.
	 * 
	 */
	public static String now() {
		String dateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
		return sdf.format(cal.getTime());
	}

	/**
	 * 어제 일자를 구한다. 일단위 조회 시 필요 yyyy-MM-dd
	 * 
	 * @return yyyy-MM-dd
	 * 
	 */
	public static String getDateYesterday(String format) {
		if (format == null) {
			format = "yyyy-MM-dd HH:mm:ss.SSS";
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
		cal.add(Calendar.DATE, -1);
		return sdf.format(cal.getTime());
	}

	public static String getDateToday(String format) {
		if (format == null) {
			format = "yyyy-MM-dd HH:mm:ss.SSS";
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
		return sdf.format(cal.getTime());
	}

	/**
	 * 오늘 날짜에서 offset 일수만큼 차이나는 날짜를 구한다. 양수면 미래, 음수면 과거
	 * 
	 * @param offset
	 * @return
	 */
	public static String getDateWithDayOffset(int offset, String format) {
		if (format == null) {
			format = "yyyy-MM-dd HH:mm:ss.SSS";
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
		cal.add(Calendar.DATE, offset);
		return sdf.format(cal.getTime());
	}

	/**
	 * 오늘 날짜에서 offset 시간만큼 차이나는 시간를 구한다. 양수면 미래, 음수 과거
	 * 
	 * @param offset
	 * @return
	 */
	public static String getDateWithHourOffset(int offset, String format) {
		if (format == null) {
			format = "yyyy-MM-dd HH:mm:ss.SSS";
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
		cal.add(Calendar.HOUR, offset);
		return sdf.format(cal.getTime());
	}

	/**
	 * 오늘 날짜에서 offset 초만큼 차이나는 시간를 구한다. 양수면 미래, 음수면 과거
	 * 
	 * @param offset
	 * @return
	 */
	public static String getDateWithSecondOffset(int offset, String format) {
		if (format == null) {
			format = "yyyy-MM-dd HH:mm:ss.SSS";
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
		cal.add(Calendar.SECOND, offset);
		return sdf.format(cal.getTime());
	}

	/**
	 * 주어진 timestamp에서 offset 일수 만큼 차이나는 시간를 구한다. 양수면 미래, 음수면 과거
	 * 
	 * @param offset
	 * @return
	 */
	public static Timestamp getTimestampWithDayOffset(Timestamp time, int offset) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time.getTime());
		cal.add(Calendar.DATE, offset);
		return new Timestamp(cal.getTime().getTime());
	}

	/**
	 * 주어진 timestamp에서 offset 시간만큼 차이나는 시간를 구한다. 양수면 미래, 음수면 과거
	 * 
	 * @param offset
	 * @return
	 */
	public static Timestamp getTimestampWithHourOffset(Timestamp time, int offset) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time.getTime());
		cal.add(Calendar.HOUR, offset);
		return new Timestamp(cal.getTime().getTime());
	}

	/**
	 * 주어진 timestamp에서 offset 초만큼 차이나는 시간를 구한다. 양수면 미래, 음수면 과거
	 * 
	 * @param offset
	 * @return
	 */
	public static Timestamp getTimestampWithSecondOffset(Timestamp time, int offset) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time.getTime());
		cal.add(Calendar.SECOND, offset);
		return new Timestamp(cal.getTime().getTime());
	}

	/**
	 * 시간 검색 조건 중 끝나는 날짜의 시간값이 없을때는 23시 59분 59초로 임의 설정한다.
	 * 
	 * @param isLast
	 * @throws OBException
	 * @return
	 */
	public static Date initTimeOfDate(Date date, boolean isLast) throws OBException {
		Calendar period = Calendar.getInstance();
		period.setTime(date);
		if (!isLast) {
			period.add(Calendar.DAY_OF_MONTH, -6);
			period.set(Calendar.HOUR_OF_DAY, 0);
			period.set(Calendar.MINUTE, 0);
			period.set(Calendar.SECOND, 0);
		} else {
			period.set(Calendar.HOUR_OF_DAY, 23);
			period.set(Calendar.MINUTE, 59);
			period.set(Calendar.SECOND, 59);
		}
		return period.getTime();
	}

	/**
	 * "yyyy-MM-dd HH:mm:ss.SSS" 형태로 입력된 시간을 Timestamp형태로 변환하여 제공한다.
	 * 
	 * @param time -- "yyyy-MM-dd HH:mm:ss.SSS" 형태의 시간.
	 *
	 * @return Timestamp
	 * 
	 */
	public static Timestamp toTimestamp(String time) {
		if (time == null)
			return null;
		return toTimestamp("yyyy-MM-dd HH:mm:ss.SSS", time);
	}

	public static int getHourOfDay(Timestamp time) {
		if (time == null)
			return 0;
		Date date = new Date(time.getTime());
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public static int getDayOfMonth(Timestamp time) {
		if (time == null)
			return 0;
		Date date = new Date(time.getTime());
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int getDayOfWeek(Timestamp time) {
		if (time == null)
			return 0;
		Date date = new Date(time.getTime());
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);// SUN, MON, TUE....
	}

	public static int getMinuteOfHour(Timestamp time) {
		if (time == null)
			return 0;
		Date date = new Date(time.getTime());
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal.get(Calendar.MINUTE);//
	}

	public static Date toDate(Timestamp time) {
		if (time == null)
			return null;
		else
			return new Date(time.getTime());
	}

	/**
	 * 지정된 포멧 형태로 입력된 시간을 Timestamp 형태로 변환하여 제공한다.
	 * 
	 * @param format -- 입력된 시간의 형태. 예: "yyyy-MM-dd HH:mm:ss.SSS"
	 *
	 * @param time   -- 시간.
	 *
	 * @return Timestamp. 오류시에는 null을 반환.
	 * 
	 */
	public static Timestamp toTimestamp(String format, String time) {
		if (time == null)
			return null;
		// format: "yyyy-MM-dd HH:mm:ss.SSS"
		// 17:48:05 Thu Feb 23, 2012 =-> HH:mm:ss EEE MMM dd, yyyy
		/*
		 * 캐릭터 일자 또는 시각의 컴퍼넌트 표시 예제 G 기원 텍스트 AD y 년 년 1996; 96 M 월 월 July; Jul; 07 w 해에
		 * 있어서의 주 수치 27 W 달에 있어서의 주 수치 2 D 해에 있어서의 날 수치 189 d 달에 있어서의 날 수치 10 F 달에 있어서의
		 * 요일 수치 2 E 요일 텍스트 Tuesday; Tue a 오전/오후 텍스트 PM H 하루에 있어서의 때 (0 ~ 23) 수치 0 k 하루에
		 * 있어서의 때 (1 ~ 24) 수치 24 K 오전/오후때 (0 ~ 11) 수치 0 h 오전/오후때 (1 ~ 12) 수치 12 m 분 수치
		 * 30 s 초 수치 55 S 밀리 세컨드 수치 978 z 타임 존 일반적인 타임 존 Pacific Standard Time; PST;
		 * GMT-08:00 Z 타임 존 RFC 822 타임 존 -0800
		 * 
		 * 일시/시간 패턴 결과 "yyyy.MM.dd G 'at' HH:mm:ss z" 2001.07.04 AD at 12:08:56 PDT
		 * "EEE, MMM d, ''yy" Wed, Jul 4, '01 "h:mm a" 12:08 PM "hh 'o''clock' a, zzzz"
		 * 12 o'clock PM, Pacific Daylight Time "K:mm a, z" 0:08 PM, PDT
		 * "yyyyy.MMMMM.dd GGG hh:mm aaa" 02001.July.04 AD 12:08 PM
		 * "EEE, d MMM yyyy HH:mm:ss Z" Wed, 4 Jul 2001 12:08:56 -0700 "yyMMddHHmmssZ"
		 * 010704120856-0700
		 */
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
		try {
			java.util.Date date = (java.util.Date) sdf.parse(time);
			Timestamp timestamp = new Timestamp(date.getTime());
			return timestamp;
		} catch (ParseException e) {
			// OBSystemLog.warn(String.format("parsing error: format:%s, time:%s,
			// message:%s", format, time, e.getMessage()));
			return null;
		}
	}

	/**
	 * 지정된 포멧 형태로 입력된 시간(Timestamp)을 변환하여 String 형태로 제공한다.
	 * 
	 * @param format -- 입력된 시간의 형태. 예: "yyyy-MM-dd HH:mm:ss.SSS"
	 *
	 * @param time   -- Timestamp 형태의 시간.
	 *
	 * @return String 형태의 문자열. 형태는 입력 파라메터 format을 통해 지정된 형태임.
	 * 
	 */
	public static String toString(String format, Timestamp time) {// format: "yyyy-MM-dd HH:mm:ss"
		if (time == null)
			return null;
		SimpleDateFormat dateformat = new SimpleDateFormat(format, Locale.ENGLISH);
		return dateformat.format(time);
	}

	/**
	 * 입력된 시간(Timestamp)을 변환하여 String 형태("yyyy-MM-dd HH:mm:ss")로 제공한다.
	 * 
	 * @param time
	 * @return String
	 */
	public static String toString(Timestamp time) {// format: "yyyy-MM-dd HH:mm:ss"

		if (time == null)
			return null;
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		return dateformat.format(time);
	}

	/**
	 * 시스템 시간을 지정된 시간으로 변경한다. 주의: root 권한으로 수행해야 시스템 시간을 변경할 수 있다.
	 * 
	 * @param time -- 수정하고자 하는 시간.
	 *
	 * @throws IOException          -- 내부 처리시 발생되는 오류
	 * 
	 * @throws InterruptedException -- 내부 처리시 발생되는 오류
	 * 
	 */
	public static void setSystemTime(Timestamp time) throws IOException, InterruptedException {
		SimpleDateFormat dateformat = new SimpleDateFormat("MMddHHmmyyyy", Locale.ENGLISH);
		String cmd = String.format("date %s", dateformat.format(time));
		Process process = Runtime.getRuntime().exec(cmd);
		process.waitFor();

//		BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));  
//		String line = null;  
//		while ((line = in.readLine()) != null) 
//		{  
//            System.out.println(line);  
//        }
		/*
		 * export env TZ=KST-09:00:00 rdate -s time.bora.net hwclock --systohc date
		 * hwclock --show
		 * 
		 * -------------------------- import socket sock = socket.socket(socket.AF_INET,
		 * socket.SOCK_STREAM) sock.connect(("ntp.ewha.net",123))
		 */
	}

	/**
	 * 시스템 시간을 설정한다. 시간은 "yyyy-MM-dd HH:mm:ss" 형태로 입력된다.
	 * 
	 * @param time -- "yyyy-MM-dd HH:mm:ss" 형태의 시간.
	 *
	 * @throws IOException          -- 내부 처리시 발생되는 오류
	 * 
	 * @throws InterruptedException -- 내부 처리시 발생되는 오류
	 * 
	 */
	public static void setSystemTime(String time) throws IOException, InterruptedException {
		setSystemTime(OBDateTimeWeb.toTimestamp("yyyy-MM-dd HH:mm:ss", time));
	}

	/**
	 * Ntp 서버를 이용하여 시스템 시간을 설정함. root 권한으로 실행되어야 함.
	 * 
	 * @param ntpServer -- ntp 서버 주소. 예: time.bora.net
	 *
	 * @throws IOException          -- 내부 처리시 발생되는 오류
	 * 
	 * @throws InterruptedException -- 내부 처리시 발생되는 오류
	 * 
	 */
}
