package kr.openbase.adcsmart.service.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;

public class OBSystemLog {
	private static final int FILE_FLAG_BARE = 0;
	public static final int FILE_FLAG_DEBUG = 1;
	public static final int FILE_FLAG_INFO = 2;
	public static final int FILE_FLAG_WARN = 3;
	public static final int FILE_FLAG_ERROR = 4;
	public static final int FILE_FLAG_FATAL = 5;
	public static final int FILE_FLAG_CALL = 6;
	public static final int FILE_FLAG_SQL = 7;
	public static final int FILE_FLAG_PAS = 8;
	public static final int FILE_FLAG_ALTEON = 9;
	public static final int FILE_FLAG_PASK = 10;
	public static final int FILE_FLAG_F5 = 11;
	public static final int FILE_FLAG_RESPT = 12;// response time 검사용.
	public static final int FILE_FLAG_SYSLOG_FILTER = 13;// syslog filter에 의해 drop된 로그.

	/**
	 * fatal 로그를 저장한다.
	 * 
	 * @return true: 저장에 성공. false: 저장에 실패.
	 * 
	 * @param filename -- 파일 이름.
	 *
	 * @param log      -- 저장하고자 하는 문자열.
	 */
	public static boolean fatal(String filename, String log)// fatal, error, warn, info, debug
	{
		try {
			if (isAvailable(OBSystemLog.FILE_FLAG_FATAL) == false)
				return true;

			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String classNameAll = stackTraceElements[2].getClassName();
			String methodName = stackTraceElements[2].getMethodName();
			String[] classNameList = classNameAll.split("\\.");
			String className = "";
			if (classNameList.length >= 1)
				className = classNameList[classNameList.length - 1];
			String logString = className + "::" + methodName + "::" + log;
			return write(filename, "fatal", logString);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * fatal 로그를 저장한다. 로그는 /opt/adcsmart/logs/system.log에 저장된다.
	 * 
	 * @return true: 저장에 성공. false: 저장에 실패.
	 * 
	 * @param log -- 저장하고자 하는 문자열.
	 */
	public static boolean fatal(String log)// fatal, error, warn, info, debug
	{
		try {
			if (isAvailable(OBSystemLog.FILE_FLAG_FATAL) == false)
				return true;

			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String classNameAll = stackTraceElements[2].getClassName();
			String methodName = stackTraceElements[2].getMethodName();
			String[] classNameList = classNameAll.split("\\.");
			String className = "";
			if (classNameList.length >= 1)
				className = classNameList[classNameList.length - 1];
			String logString = className + "::" + methodName + "::" + log;
			return write(OBDefine.LOGFILE_SYSTEM, "fatal", logString);
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * error 로그를 저장한다.
	 * 
	 * @return true: 저장에 성공. false: 저장에 실패.
	 * 
	 * @param filename -- 파일 이름.
	 *
	 * @param log      -- 저장하고자 하는 문자열.
	 */
	public synchronized static boolean error(String filename, String log)// fatal, error, warn, info, debug
	{
		try {
			if (isAvailable(OBSystemLog.FILE_FLAG_ERROR) == false)
				return true;

			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String classNameAll = stackTraceElements[2].getClassName();
			String methodName = stackTraceElements[2].getMethodName();
			String[] classNameList = classNameAll.split("\\.");
			String className = "";
			if (classNameList.length >= 1)
				className = classNameList[classNameList.length - 1];
			String logString = className + "::" + methodName + "::" + log;
			return write(filename, "error", logString);
		} catch (Exception e) {
			return false;
		}
	}

	public synchronized static boolean error3(String filename, String log)// fatal, error, warn, info, debug
	{
		try {
			if (isAvailable(OBSystemLog.FILE_FLAG_ERROR) == false)
				return true;

			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String classNameAll = stackTraceElements[2].getClassName();
			String methodName = stackTraceElements[2].getMethodName();
			String[] classNameList = classNameAll.split("\\.");
			String className = "";
			if (classNameList.length >= 1)
				className = classNameList[classNameList.length - 1];
			String logString = className + "::" + methodName + "::" + log;
			return write(filename, "error", logString);
		} catch (Exception e) {
			return false;
		}

	}

	/**
	 * error 로그를 저장한다. 로그는 /opt/adcsmart/logs/system.log에 저장된다.
	 * 
	 * @return true: 저장에 성공. false: 저장에 실패.
	 * 
	 * @param log -- 저장하고자 하는 문자열.
	 */
	public synchronized static boolean error(String log)// fatal, error, warn, info, debug
	{
		try {
			if (isAvailable(OBSystemLog.FILE_FLAG_ERROR) == false)
				return true;

			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String classNameAll = stackTraceElements[2].getClassName();
			String methodName = stackTraceElements[2].getMethodName();
			String[] classNameList = classNameAll.split("\\.");
			String className = "";
			if (classNameList.length >= 1)
				className = classNameList[classNameList.length - 1];
			String logString = className + "::" + methodName + "::" + log;
			return write(OBDefine.LOGFILE_SYSTEM, "error", logString);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * warn 로그를 저장한다.
	 * 
	 * @return true: 저장에 성공. false: 저장에 실패.
	 * 
	 * @param filename -- 파일 이름.
	 *
	 * @param log      -- 저장하고자 하는 문자열.
	 */
	public static boolean warn(String filename, String log)// fatal, error, warn, info, debug
	{
		try {
			if (isAvailable(OBSystemLog.FILE_FLAG_WARN) == false)
				return true;

			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String classNameAll = stackTraceElements[2].getClassName();
			String methodName = stackTraceElements[2].getMethodName();
			String[] classNameList = classNameAll.split("\\.");
			String className = "";
			if (classNameList.length >= 1)
				className = classNameList[classNameList.length - 1];
			String logString = className + "::" + methodName + "::" + log;
			return write(filename, "warn ", logString);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * warn 로그를 저장한다. 로그는 /opt/adcsmart/logs/system.log에 저장된다.
	 * 
	 * @return true: 저장에 성공. false: 저장에 실패.
	 * 
	 * @param log -- 저장하고자 하는 문자열.
	 */
	public static boolean warn(String log)// fatal, error, warn, info, debug
	{
		try {
			if (isAvailable(OBSystemLog.FILE_FLAG_WARN) == false)
				return true;

			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String classNameAll = stackTraceElements[2].getClassName();
			String methodName = stackTraceElements[2].getMethodName();
			String[] classNameList = classNameAll.split("\\.");
			String className = "";
			if (classNameList.length >= 1)
				className = classNameList[classNameList.length - 1];
			String logString = className + "::" + methodName + "::" + log;
			return write(OBDefine.LOGFILE_SYSTEM, "warn ", logString);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * info 로그를 저장한다.
	 * 
	 * @return true: 저장에 성공. false: 저장에 실패.
	 * 
	 * @param filename -- 파일 이름.
	 *
	 * @param log      -- 저장하고자 하는 문자열.
	 */
	public synchronized static boolean info(String filename, String log)// fatal, error, warn, info, debug
	{
		try {
			if (isAvailable(OBSystemLog.FILE_FLAG_INFO) == false)
				return true;

			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String classNameAll = stackTraceElements[2].getClassName();
			String methodName = stackTraceElements[2].getMethodName();
			String[] classNameList = classNameAll.split("\\.");
			String className = "";
			if (classNameList.length >= 1)
				className = classNameList[classNameList.length - 1];
			String logString = className + "::" + methodName + "::" + log;
			return write(filename, "info ", logString);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * info 로그를 저장한다. 로그는 /opt/adcsmart/logs/system.log에 저장된다.
	 * 
	 * @return true: 저장에 성공. false: 저장에 실패.
	 * 
	 * @param log -- 저장하고자 하는 문자열.
	 */
	public synchronized static boolean info(String log)// fatal, error, warn, info, debug
	{
		try {
			if (isAvailable(OBSystemLog.FILE_FLAG_INFO) == false)
				return true;

			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String classNameAll = stackTraceElements[2].getClassName();
			String methodName = stackTraceElements[2].getMethodName();
			String[] classNameList = classNameAll.split("\\.");
			String className = "";
			if (classNameList.length >= 1)
				className = classNameList[classNameList.length - 1];
			String logString = className + "::" + methodName + "::" + log;
			return write(OBDefine.LOGFILE_SYSTEM, "info ", logString);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean bare(String filename, String log)// fatal, error, warn, info, debug
	{
		if (isAvailable(OBSystemLog.FILE_FLAG_BARE) == false)
			return true;

		return write(filename, "info ", log);
	}

	public static boolean syslogFilter(String filename, String log)// fatal, error, warn, info, debug
	{
		if (isAvailable(OBSystemLog.FILE_FLAG_SYSLOG_FILTER) == false)
			return true;

		return write(filename, "info ", log);
	}

	public static boolean resptime(String filename, String log)// fatal, error, warn, info, debug
	{
		if (isAvailable(OBSystemLog.FILE_FLAG_RESPT) == false)
			return true;

		return write(filename, "info ", log);
	}

	/**
	 * debug 로그를 저장한다.
	 * 
	 * @return true: 저장에 성공. false: 저장에 실패.
	 * 
	 * @param filename -- 파일 이름.
	 *
	 * @param log      -- 저장하고자 하는 문자열.
	 */
	public static boolean debug(String filename, String log)// fatal, error, warn, info, debug
	{
		try {
			if (isAvailable(OBSystemLog.FILE_FLAG_DEBUG) == false)
				return true;

			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String classNameAll = stackTraceElements[2].getClassName();
			String methodName = stackTraceElements[2].getMethodName();
			String[] classNameList = classNameAll.split("\\.");
			String className = "";
			if (classNameList.length >= 1)
				className = classNameList[classNameList.length - 1];
			String logString = className + "::" + methodName + "::" + log;
			return write(filename, "debug", logString);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean writePASCmnd(String cmnd, String result)// fatal, error, warn, info, debug
	{
		if (isAvailable(OBSystemLog.FILE_FLAG_PAS) == false)
			return true;

		if (cmnd != null)
			write(OBDefine.LOGFILE_PAS_CMND, "pas::cmnd", cmnd);
		if (result != null)
			write(OBDefine.LOGFILE_PAS_CMND, "pas::rtrn", result);
		return true;
	}

	public static boolean writePASKCmnd(String cmnd, String result)// fatal, error, warn, info, debug
	{
		if (isAvailable(OBSystemLog.FILE_FLAG_PASK) == false)
			return true;
		if (cmnd != null)
			write(OBDefine.LOGFILE_PASK_CMND, "pask::cmnd", cmnd);
		if (result != null)
			write(OBDefine.LOGFILE_PASK_CMND, "pask::rtrn", result);
		return true;
	}

	/**
	 * debug 로그를 저장한다. 로그는 /opt/adcsmart/logs/system.log에 저장된다.
	 * 
	 * @return true: 저장에 성공. false: 저장에 실패.
	 * 
	 * @param log -- 저장하고자 하는 문자열.
	 */
	public static boolean debug(String log)// fatal, error, warn, info, debug
	{
		try {
			if (isAvailable(1) == false)
				return true;

			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String classNameAll = stackTraceElements[2].getClassName();
			String methodName = stackTraceElements[2].getMethodName();
			String[] classNameList = classNameAll.split("\\.");
			String className = "";
			if (classNameList.length >= 1)
				className = classNameList[classNameList.length - 1];
			String logString = className + "::" + methodName + "::" + log;
			return write(OBDefine.LOGFILE_SYSTEM, "debug", logString);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean call(String filename, String log)// fatal, error, warn, info, debug
	{
		try {
			if (isAvailable(6) == false)
				return true;

			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String classNameAll = stackTraceElements[2].getClassName();
			String methodName = stackTraceElements[2].getMethodName();
			String[] classNameList = classNameAll.split("\\.");
			String className = "";
			if (classNameList.length >= 1)
				className = classNameList[classNameList.length - 1];
			String logString = className + "::" + methodName + "::" + log;
			return write(filename, "call", logString);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean call(String log)// fatal, error, warn, info, debug
	{
		try {
			if (isAvailable(6) == false)
				return true;

			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String classNameAll = stackTraceElements[2].getClassName();
			String methodName = stackTraceElements[2].getMethodName();
			String[] classNameList = classNameAll.split("\\.");
			String className = "";
			if (classNameList.length >= 1)
				className = classNameList[classNameList.length - 1];
			String logString = className + "::" + methodName + "::" + log;
			return write(OBDefine.LOGFILE_SYSTEM, "call", logString);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean sql(String filename, String log)// fatal, error, warn, info, debug
	{
		try {
			if (isAvailable(7) == false)
				return true;

			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String classNameAll = stackTraceElements[2].getClassName();
			String methodName = stackTraceElements[2].getMethodName();
			String[] classNameList = classNameAll.split("\\.");
			String className = "";
			if (classNameList.length >= 1)
				className = classNameList[classNameList.length - 1];
			String logString = className + "::" + methodName + "::" + log;
			return write(filename, "sql", logString);
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean sql(String log)// fatal, error, warn, info, debug
	{
		try {
			if (isAvailable(7) == false)
				return true;

			StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
			String classNameAll = stackTraceElements[2].getClassName();
			String methodName = stackTraceElements[2].getMethodName();
			String[] classNameList = classNameAll.split("\\.");
			String className = "";
			if (classNameList.length >= 1)
				className = classNameList[classNameList.length - 1];
			String logString = className + "::" + methodName + "::" + log;
			return write(OBDefine.LOGFILE_SYSTEM, "sql", logString);
		} catch (Exception e) {
			return false;
		}
	}

//	public static void main(String[] args)
//    {
//        try
//        {
//            String aaa = "1234567890abcdefghijklmnopqrstuvwxyz";
//            System.out.println(OBSystemLog.trimString(aaa));
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

	private static final int MAX_STRING_LENGTH = 1000;

	private static synchronized String trimString(String log) {
		String retVal = log;
		if (log.length() >= MAX_STRING_LENGTH) {
			String tail = log.substring(log.length() - 10, log.length());
			retVal = log.substring(0, MAX_STRING_LENGTH - 15) + "....." + tail;
		}
		return retVal;
	}

	private synchronized static boolean write(String filename, String level, String log) {
		File f = new File(filename);
		BufferedWriter out = null;
		try {
			Long defaultSize = 20000000L;
			if (filename.equals(OBDefine.LOGFILE_ALTEON_CMND) || filename.equals(OBDefine.LOGFILE_F5_CMND)
					|| filename.equals(OBDefine.LOGFILE_PAS_CMND) || filename.equals(OBDefine.LOGFILE_PASK_CMND)) {
				defaultSize = 1000000L; // 1MByte 로 변경
			}

			if (f.length() > defaultSize) {// 10MBytes
				// boolean success = f.renameTo(new File(filename + ".bak"));
				boolean success = OBParser.moveFile(filename, filename + ".bak");
				if (!success) {
					System.out.println("file was not successfully moved");
					return false;
				}
			}

			FileWriter fileWriter = new FileWriter(f, true);
			out = new BufferedWriter(fileWriter);
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			dateformat.format(new java.util.Date());
			out.write(dateformat.format(new java.util.Date()) + "::" + level + "::" + trimString(log) + "\n");
//			out.write(dateformat.format(new java.util.Date()) + "::" + level + "::" + log + "\n");
			return true;
		} catch (Exception e) {
//			System.out.println(e.getMessage());
			return false;
		} finally {
			try {
				out.close();
			} catch (Exception e) {
			}
		}
	}

	public static boolean writeAlteonCmnd(String cmnd, String result) {
		if (isAvailable(OBSystemLog.FILE_FLAG_ALTEON) == false)
			return true;
		if (cmnd != null && !cmnd.isEmpty())
			write(OBDefine.LOGFILE_ALTEON_CMND, "alt::cmnd", cmnd);
		if (result != null && !result.isEmpty())
			write(OBDefine.LOGFILE_ALTEON_CMND, "alt::rtrn", result);
		return true;
	}

	public static boolean writeF5Cmnd(String cmnd, String result) {
		if (isAvailable(OBSystemLog.FILE_FLAG_F5) == false)
			return true;
		if (cmnd != null && !cmnd.isEmpty())
			write(OBDefine.LOGFILE_F5_CMND, "f5::cmnd", cmnd);
		if (result != null && !result.isEmpty())
			write(OBDefine.LOGFILE_F5_CMND, "f5::rtrn", result);
		return true;
	}

	private static boolean isAvailable(int type) {
		String fileName = "";
		switch (type) {
		case OBSystemLog.FILE_FLAG_CALL:
			fileName = OBDefine.CFG_DIR + "call";
			break;
		case OBSystemLog.FILE_FLAG_BARE:
			fileName = OBDefine.CFG_DIR + "bare";
			break;
		case OBSystemLog.FILE_FLAG_DEBUG:
			fileName = OBDefine.CFG_DIR + "debug";
			break;
		case OBSystemLog.FILE_FLAG_INFO:
			fileName = OBDefine.CFG_DIR + "info";
			break;
		case OBSystemLog.FILE_FLAG_WARN:
			fileName = OBDefine.CFG_DIR + "warn";
			break;
		case OBSystemLog.FILE_FLAG_ERROR:
			fileName = OBDefine.CFG_DIR + "error";
			break;
		case OBSystemLog.FILE_FLAG_FATAL:
			fileName = OBDefine.CFG_DIR + "fatal";
			break;
		case OBSystemLog.FILE_FLAG_SQL:
			fileName = OBDefine.CFG_DIR + "sql";
			break;
		case OBSystemLog.FILE_FLAG_PAS:
			fileName = OBDefine.CFG_DIR + "pas";
			break;
		case OBSystemLog.FILE_FLAG_PASK:
			fileName = OBDefine.CFG_DIR + "pask";
			break;
		case OBSystemLog.FILE_FLAG_ALTEON:
			fileName = OBDefine.CFG_DIR + "alteon";
			break;
		case OBSystemLog.FILE_FLAG_F5:
			fileName = OBDefine.CFG_DIR + "f5";
			break;
		case OBSystemLog.FILE_FLAG_RESPT:
			fileName = OBDefine.CFG_DIR + "resptime";
			break;
		case OBSystemLog.FILE_FLAG_SYSLOG_FILTER:
			fileName = OBDefine.CFG_DIR + "syslogfilter";
			break;
		default:
			fileName = OBDefine.CFG_DIR + "debug";
			break;
		}

		File f = new File(fileName);
		if (f.exists())
			return true;
		return false;
	}
}
