package kr.openbase.adcsmart.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import kr.openbase.adcsmart.service.OBSystemAudit;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBSystemAuditImpl implements OBSystemAudit {
	public static final String AUDIT_SYSLOG_HOST_BIND_FAIL[] = { "9001", "4", "INFO", "syslog를 보낼 서버 주소 확인에 실패했습니다." };

	private static final String PROPERTIES_PATH = "/opt/apache-tomcat/webapps/adcms/WEB-INF/classes/conf/";
	private static final String PROPERTIES_BASE = "adcsmart.properties";
	private static final String PROPERTIES_MESSAGES = "Audit_%s_%s.properties";
	private static final int MAX_AUDIT_LOG_CONTENT_LENGTH = 65000;

	public OBSystemAuditImpl() {
	}

	private String getLocalInfo() {
		String retVal = "ko_KR";
		try {
			FileInputStream fis = new FileInputStream(PROPERTIES_PATH + PROPERTIES_BASE);
			;
			Properties props = new Properties();
			props.load(fis);
			retVal = props.getProperty("lang.code", "ko_KR");
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return retVal;
	}

	private String getLanguageCode(String langCode) {
		String elements[] = langCode.split("_");
		if (elements.length != 2)
			return "ko";// default language is korean
		return elements[0];
	}

	private String getCountryCode(String langCode) {
		String elements[] = langCode.split("_");
		if (elements.length != 2)
			return "KR";// default language is korean
		return elements[1];
	}

	private static Properties msgProps;// = LoggerFactory.getLogger(AlertFacade.class);

	public static String getAuditMessage(String code) {
		try {
			if (msgProps == null) {
				String langCode = new OBSystemAuditImpl().getLocalInfo();
				String language = new OBSystemAuditImpl().getLanguageCode(langCode);
				String country = new OBSystemAuditImpl().getCountryCode(langCode);
				String fileName = String.format(PROPERTIES_MESSAGES, language, country);
				BufferedReader in = new BufferedReader(
						new InputStreamReader(new FileInputStream(PROPERTIES_PATH + fileName), "UTF8"));
				msgProps = new Properties();
				msgProps.load(in);
			}
			String retVal = msgProps.getProperty(code).trim();
			if (retVal == null) {
				OBSystemLog.error3(OBDefine.LOGFILE_SYSTEM, String.format("Exception. not defined. code:%s", code));
				return "not defined";
			}
			return retVal;
		} catch (Exception e) {
			OBSystemLog.error3(OBDefine.LOGFILE_SYSTEM,
					String.format("Exception. not defined. code:%s. exception occured", code));
			return "not defined";
		}
	}

//	public static void main(String[] args)
//	{
//		new OBSystemAuditImpl().writeLog(1, "172.172.1.211", OBSystemAudit.AUDIT_LOGIN_SUCCESS, "test");
//		new OBSystemAuditImpl().writeLog(1, "172.172.1.212", OBSystemAudit.AUDIT_LOGIN_FAIL, "test");
//		new OBSystemAuditImpl().writeLog(1, "172.172.1.224", OBSystemAudit.AUDIT_LOGOUT_SUCCESS, "test");
//		new OBSystemAuditImpl().writeLog(1, "172.172.1.225", OBSystemAudit.AUDIT_DB_CONNECTION_FAIL, "test");
//	}

	public void writeLog(Integer accountIndex, String clientIP, String[] auditCode) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String content = String.format(getAuditMessage(auditCode[3]));
			sqlText = String.format(
					"INSERT INTO " + "LOG_SYSTEM_AUDIT "
							+ "(OCCUR_TIME, ACCOUNT_INDEX, CLIENT_IP, TYPE, LEVEL, CONTENT, CODE) " + "VALUES "
							+ "(%s, %d, %s, %d, %d, %s, %d);",
					OBParser.sqlString(OBDateTime.now()), accountIndex, OBParser.sqlString(clientIP),
					Integer.parseInt(auditCode[1]), convertLogLevel(auditCode[2]),
					OBParser.sqlString(StringUtils.abbreviate(content, MAX_AUDIT_LOG_CONTENT_LENGTH)), // CONTENT 필드 길이는
																										// 65535,
																										// 65000에서 생략하게
																										// 처리한다.
					Integer.parseInt(auditCode[0]));

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("sql query error: %s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("%s", e.getMessage()));
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("null pointer:%s", e.getMessage()));
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public void writeLog(Integer accountIndex, String clientIP, int auditCode, String extraMessage) throws OBException {
		try {
			switch (auditCode) {
			case AUDIT_LOGIN_SUCCESS_CODE:
				writeLog(accountIndex, clientIP, AUDIT_LOGIN_SUCCESS, extraMessage);
				break;
			case AUDIT_LOGIN_FAIL_CODE:
				writeLog(accountIndex, clientIP, AUDIT_LOGIN_FAIL, extraMessage);
				break;
			case AUDIT_LOGOUT_SUCCESS_CODE:
				writeLog(accountIndex, clientIP, AUDIT_LOGOUT_SUCCESS, extraMessage);
				break;
			case AUDIT_LOGOUT_FAIL_CODE:
				writeLog(accountIndex, clientIP, AUDIT_LOGOUT_FAIL, extraMessage);
				break;
			}
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("errmsg%s: %s", e.getMessage()));
		}
	}

	public void writeLog(Integer accountIndex, String clientIP, String[] auditCode, String extraMessage) {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String content = String.format(getAuditMessage(auditCode[3]), extraMessage);
			sqlText = String.format(
					"INSERT INTO " + "LOG_SYSTEM_AUDIT "
							+ "(OCCUR_TIME, ACCOUNT_INDEX, CLIENT_IP, TYPE, LEVEL, CONTENT, CODE) " + "VALUES "
							+ "(%s, %d, %s, %d, %d, %s, %d);",
					OBParser.sqlString(OBDateTime.now()), accountIndex, OBParser.sqlString(clientIP),
					Integer.parseInt(auditCode[1]), convertLogLevel(auditCode[2]),
					OBParser.sqlString(StringUtils.abbreviate(content, MAX_AUDIT_LOG_CONTENT_LENGTH)), // CONTENT 필드 길이는
																										// 65535,
																										// 65000에서 생략하게
																										// 처리한다.
					Integer.parseInt(auditCode[0]));

			OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("sql query error: %s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("%s: %s, callstack:%s",
					"failed to write a audit log", e.getMessage(), new OBUtility().getStackTrace()));
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("%s: %s, callstack:%s",
					"failed to write a audit log", e.getMessage(), new OBUtility().getStackTrace()));
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public void writeLogNew(Integer accountIndex, String clientIP, String[] auditCode, String extraMessage) {
		String sqlText = "";

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			String content = String.format(getAuditMessage(auditCode[3]), extraMessage);
			sqlText = String.format(
					"INSERT INTO " + "LOG_SYSTEM_AUDIT "
							+ "(OCCUR_TIME, ACCOUNT_INDEX, CLIENT_IP, TYPE, LEVEL, CONTENT, CODE) " + "VALUES "
							+ "(%s, %d, %s, %d, %d, %s, %d);",
					OBParser.sqlString(OBDateTime.now()), accountIndex, OBParser.sqlString(clientIP),
					Integer.parseInt(auditCode[1]), convertLogLevel(auditCode[2]),
					OBParser.sqlString(StringUtils.abbreviate(content, MAX_AUDIT_LOG_CONTENT_LENGTH)), // CONTENT 필드 길이는
																										// 65535,
																										// 65000에서 생략하게
																										// 처리한다.
					Integer.parseInt(auditCode[0]));

			OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("sql query error: %s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("%s: %s, callstack:%s",
					"failed to write a audit log", e.getMessage(), new OBUtility().getStackTrace()));
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("%s: %s, callstack:%s",
					"failed to write a audit log", e.getMessage(), new OBUtility().getStackTrace()));
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void writeLog(Integer accountIndex, String clientIP, String[] auditCode, String msg1, String msg2) {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String content = String.format(getAuditMessage(auditCode[3]), msg1, msg2);
			sqlText = String.format(
					"INSERT INTO " + "LOG_SYSTEM_AUDIT "
							+ "(OCCUR_TIME, ACCOUNT_INDEX, CLIENT_IP, TYPE, LEVEL, CONTENT, CODE) " + "VALUES "
							+ "(%s, %d, %s, %d, %d, %s, %d);",
					OBParser.sqlString(OBDateTime.now()), accountIndex, OBParser.sqlString(clientIP),
					Integer.parseInt(auditCode[1]), convertLogLevel(auditCode[2]),
					OBParser.sqlString(StringUtils.abbreviate(content, MAX_AUDIT_LOG_CONTENT_LENGTH)), // CONTENT 필드 길이는
																										// 65535,
																										// 65000에서 생략하게
																										// 처리한다.
					Integer.parseInt(auditCode[0]));

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("sql query error: %s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("%s: %s, callstack:%s",
					"failed to write a audit log", e.getMessage(), new OBUtility().getStackTrace()));
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("%s: %s, callstack:%s",
					"failed to write a audit log", e.getMessage(), new OBUtility().getStackTrace()));
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private Integer convertLogLevel(String level) {
		if (level.compareToIgnoreCase("INFO") == 0)
			return OBDefine.LOG_LEVEL_INFO;
		else if (level.compareToIgnoreCase("WARNING") == 0)
			return OBDefine.LOG_LEVEL_WARNING;
		else if (level.compareToIgnoreCase("RISK") == 0)
			return OBDefine.LOG_LEVEL_RISK;
		else if (level.compareToIgnoreCase("ERROR") == 0)
			return OBDefine.LOG_LEVEL_ERROR;
		else
			return OBDefine.LOG_LEVEL_INFO;
	}

//	public void writeLogAdc(Integer accountIndex, Integer adcIndex, String clientIP, String [] auditCode, String extraMessage, Long rIndex) throws OBException
//	{
//		OBDatabase db = new OBDatabase();
//		try
//		{		
//			db.openDB();
//			writeLogAdc(accountIndex, adcIndex, clientIP, auditCode, extraMessage, rIndex, db);
//		}
//		catch(OBException e)
//		{
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("errmsg: %s", e.getErrorMessage()));
//			return;
//		}
//		catch(Exception e)
//		{
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("errmsg: %s", e.getMessage()));
//			return;
//		}
//		finally
//		{
//			if(db!=null) db.closeDB();
//		}
//	}

	public void writeLogAdc(Integer accountIndex, Integer adcIndex, String clientIP, String[] auditCode,
			String extraMessage, Long rIndex) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String content = String.format(getAuditMessage(auditCode[3]), extraMessage);
			sqlText = String.format(
					"INSERT INTO " + "LOG_SYSTEM_AUDIT "
							+ "(OCCUR_TIME, ADC_INDEX, ACCOUNT_INDEX, CLIENT_IP, TYPE, LEVEL, RINDEX, CONTENT, CODE) "
							+ "VALUES " + "(%s, %d, %d, %s, %d, %d, %d, %s, %d);",
					OBParser.sqlString(OBDateTime.now()), adcIndex, accountIndex, OBParser.sqlString(clientIP),
					Integer.parseInt(auditCode[1]), convertLogLevel(auditCode[2]), rIndex,
					OBParser.sqlString(StringUtils.abbreviate(content, MAX_AUDIT_LOG_CONTENT_LENGTH)), // CONTENT 필드 길이는
																										// 65535,
																										// 65000에서 생략하게
																										// 처리한다.
					Integer.parseInt(auditCode[0]));

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("%s: %s, callstack:%s", sqlText, e.getMessage(), new OBUtility().getStackTrace()));
		} catch (OBException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("%s: %s, callstack:%s",
					"failed to write a audit log", e.getMessage(), new OBUtility().getStackTrace()));
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("%s: %s, callstack:%s",
					"failed to write a audit log", e.getMessage(), new OBUtility().getStackTrace()));
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void writeLogAdc(Integer accountIndex, Integer adcIndex, String clientIP, String[] auditCode,
			String extraMsg1, String extraMsg2, Long rIndex) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String content = String.format(getAuditMessage(auditCode[3]), extraMsg1, extraMsg2);

//          obj = getAuditString(auditCode, db);
//          String[] msg = obj.getExtraMessage().split(",");
//          String content;
//          if(msg.length==2)
//              content = String.format("%s(%s : %s, %s : %s)", obj.getContent(), msg[0], extraMsg1, msg[1], extraMsg2);
//          else
//              content = String.format("%s(%s : %s)", obj.getContent(), msg[0], extraMsg1);

			sqlText = String.format(
					"INSERT INTO " + "LOG_SYSTEM_AUDIT "
							+ "(OCCUR_TIME, ADC_INDEX, ACCOUNT_INDEX, CLIENT_IP, TYPE, LEVEL, RINDEX, CONTENT, CODE) "
							+ "VALUES " + "(%s, %d, %d, %s, %d, %d, %d, %s, %d);",
					OBParser.sqlString(OBDateTime.now()), adcIndex, accountIndex, OBParser.sqlString(clientIP),
					Integer.parseInt(auditCode[1]), convertLogLevel(auditCode[2]), rIndex,
					OBParser.sqlString(StringUtils.abbreviate(content, MAX_AUDIT_LOG_CONTENT_LENGTH)), // CONTENT 필드 길이는
																										// 65535,
																										// 65000에서 생략하게
																										// 처리한다.
					Integer.parseInt(auditCode[0]));

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("%s: %s, callstack:%s", sqlText, e.getMessage(), new OBUtility().getStackTrace()));
		} catch (OBException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("%s: %s, callstack:%s",
					"failed to write a audit log", e.getMessage(), new OBUtility().getStackTrace()));
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("%s: %s, callstack:%s",
					"failed to write a audit log", e.getMessage(), new OBUtility().getStackTrace()));
		} finally {
			if (db != null)
				db.closeDB();
		}
	}
}
