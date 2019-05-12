package kr.openbase.adcsmart.service.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import kr.openbase.adcsmart.service.OBEnvManagement;
import kr.openbase.adcsmart.service.OBSystemAudit;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcLogFilter;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoScheduleBackupInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSnmpTrap;
import kr.openbase.adcsmart.service.dto.OBDtoSyncSystemTime;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnv;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvNetwork;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvView;
import kr.openbase.adcsmart.service.dto.OBDtoSystemSnmpInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoSchedule;
import kr.openbase.adcsmart.service.impl.dto.OBDtoTimesync;
import kr.openbase.adcsmart.service.jna.OBSystem;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBNetwork;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBEnvManagementImpl implements OBEnvManagement {
	private static final String ENV_KEY_LOGINACCESS = "LOGIN_ACCESS";
	private static final String ENV_KEY_ALARM_POPUP_YN = "ALARM_POPUP_YN";
	private static final String ENV_KEY_SMS_ACTION_YN = "SMS_ACTION_YN";
	private static final String ENV_KEY_SMS_ACTION_TYPE = "SMS_ACTION_TYPE";
	private static final String ENV_KEY_SMS_HP_NUMBERS = "SMS_HP_NUMBERS";
	private static final String PROC_SYS_KERNEL_HOSTNAME_FILE = "/proc/sys/kernel/hostname";
//	public static void main(String[] args)
//	{
//			OBDtoSystemEnv config;
//			try
//			{
//				config = new OBEnvManagementImpl().getSystemConfig();
//			}
//			catch(OBException e)
//			{
//				e.printStackTrace();
//				return;
//			}
//			System.out.println("aaa : " + config.getHostName());
//	}

//	public String getValue(String key, OBDatabase db) throws OBException
//	{
//		String sqlText;
//		try
//		{
//			sqlText = String.format(" SELECT " +
//									" VALUE " +
//									" FROM MNG_SETTING " +
//									" WHERE KEY=%s; ",
//									OBParser.sqlString(key));
//			ResultSet rs = db.executeQuery(sqlText);
//			if(rs.next()==false)
//				return null;
//			return db.getString(rs, "VALUE");
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}	
//	}

	/**
	 * (주기적으로) ADC에 접속해서 applytime을 가져올 지 확인한다. alteon 장비에만 있는 기능이다.
	 * 
	 * @return true: ADC에 접속해서 apply/save time을 구한다. \br false: ADC에서 시간을 확인하지 않는다.
	 *         설정 작업시 DB에 업데이트된 시간을 참조한다.
	 * @throws OBException
	 */
	public boolean isCheckApplyTimeAlteon(OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String
					.format(" SELECT ALTEON_CHECK_APPLYTIME " + " FROM MNG_ENV_ADDITIONAL " + " WHERE INDEX = 1 ");

			ResultSet rs;
			rs = db.executeQuery(sqlText);

			if (rs.next() == true) {// 항목을 업데이트.
				if (db.getInteger(rs, "ALTEON_CHECK_APPLYTIME") == 0)
					return false;
				else
					return true;
			} else {// 추가한다.
				return true;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public boolean isCheckApplyTimeAlteon() throws OBException {
		String sqlText = "";
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			sqlText = String
					.format(" SELECT ALTEON_CHECK_APPLYTIME " + " FROM MNG_ENV_ADDITIONAL " + " WHERE INDEX = 1 ");

			ResultSet rs;
			rs = db.executeQuery(sqlText);

			if (rs.next() == true) {// 항목을 업데이트.
				if (db.getInteger(rs, "ALTEON_CHECK_APPLYTIME") == 0)
					return false;
				else
					return true;
			} else {// 추가한다.
				return true;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public boolean isAlteonAutoSave() throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT ALTEON_AUTO_SAVE " + " FROM MNG_ENV_ADDITIONAL " + " WHERE INDEX = 1 ");

			ResultSet rs;
			rs = db.executeQuery(sqlText);

			if (rs.next() == true) {// 항목을 업데이트.
				if (1 == db.getInteger(rs, "ALTEON_AUTO_SAVE"))
					return true;
				else
					return false;
			} else {// 추가한다.
				return false;
			}
		} catch (SQLException e) {
			return false;// auto save 안하는 것으로 간주한다.
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void updateScheduleSlbSave(int adcIndex, OBDatabase db) throws OBException {
		// 먼저 등록된 항목이 있는지 검사한 후 있으면 업데이트 하고, 없으면 추가한다.
		String sqlText = "";
		try {
			sqlText = String.format(
					" SELECT INDEX, TYPE, ADC_INDEX " + " FROM MNG_SCHEDULE " + " WHERE TYPE=%d AND ADC_INDEX=%d;",
					OBDtoSchedule.TYPE_SLB_SAVE, adcIndex);
			ResultSet rs;

			rs = db.executeQuery(sqlText);

			if (rs.next() == true) {// 항목을 업데이트.
				OBDtoSchedule sch = new OBDtoSchedule();
				sch.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				sch.setDay(0);
				sch.setHour(4);
				sch.setIsCycle(0);
				sch.setIndex(db.getInteger(rs, "INDEX"));
				sch.setType(OBDtoSchedule.TYPE_SLB_SAVE);
				updateSchedule(sch, db);
			} else {// 추가한다.
				OBDtoSchedule sch = new OBDtoSchedule();
				sch.setAdcIndex(adcIndex);
				sch.setDay(0);
				sch.setHour(4);
				sch.setIsCycle(0);
				sch.setType(OBDtoSchedule.TYPE_SLB_SAVE);
				addSchedule(sch, db);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public void updateScheduleTimesync(OBDtoTimesync sync, OBDatabase db) throws OBException {
		// 먼저 등록된 항목이 있는지 검사한 후 있으면 업데이트 하고, 없으면 추가한다.
		String sqlText = "";
		try {
			sqlText = String.format(" SELECT INDEX, TYPE FROM MNG_SCHEDULE WHERE TYPE=%d;",
					OBDtoSchedule.TYPE_TIME_SYNC);

			ResultSet rs;
			rs = db.executeQuery(sqlText);

			if (rs.next() == true) {// 항목을 업데이트.
				if (sync.getUseYN() == 0) {// schedule을 삭제해야 한다.
					delSchedule(db.getInteger(rs, "INDEX"), db);
				} else {
					OBDtoSchedule sch = new OBDtoSchedule();
					sch.setAdcIndex(0);
					sch.setDay(sync.getDate());
					sch.setHour(sync.getTime());
					sch.setIsCycle(1);
					sch.setIndex(db.getInteger(rs, "INDEX"));
					sch.setType(db.getInteger(rs, "TYPE"));
					sch.setOpt1(sync.getNtpServer());
					updateSchedule(sch, db);
				}
			} else {// 추가한다.
				OBDtoSchedule sch = new OBDtoSchedule();
				sch.setAdcIndex(0);
				sch.setDay(sync.getDate());
				sch.setHour(sync.getTime());
				sch.setIsCycle(1);
				sch.setType(db.getInteger(rs, "TYPE"));
				sch.setOpt1(sync.getNtpServer());
				addSchedule(sch, db);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public int getLogViewCount(OBDatabase db) throws OBException {
		OBDtoSystemEnvView config = getViewConfig(db);
		return config.getLogViewCount();
	}

	public int getLogViewPeriod(OBDatabase db) throws OBException {
		OBDtoSystemEnvView config = getViewConfig(db);
		return config.getLogViewPeriodType();
	}

	public int getAdcSyncInterval(OBDatabase db) throws OBException {
		OBDtoSystemEnvAdditional config = getAdditionalConfig(db);
		return config.getIntervalAdcConfSync();
	}

	public int getAdcSyncInterval() throws OBException {
		OBDtoSystemEnvAdditional config = getAdditionalConfig();
		return config.getIntervalAdcConfSync();
	}

	public int getSystemInterval() throws OBException {
		OBDtoSystemEnvAdditional config = getAdditionalConfig();
		return config.getIntervalSystemInfo();
	}

	public int getSectionRespInterval() throws OBException {
		OBDtoSystemEnvAdditional config = getAdditionalConfig();
		return config.getRespTimeInterval();
	}

	public void updateSchedule(OBDtoSchedule sch, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. sch:%s", sch));

		String sqlText = "";
		try {
			sqlText = String.format("UPDATE MNG_SCHEDULE "
					+ " SET TYPE=%d, CYCLE_YN=%d, WKDY=%d, DAY=%d, HOUR=%d, ADC_INDEX=%d, Opt1=%s, Opt2=%s, Opt3=%s, Opt4=%s, Opt5=%s "
					+ " WHERE INDEX=%d;", sch.getType(), sch.getIsCycle(), sch.getWkdy(), sch.getDay(), sch.getHour(),
					sch.getAdcIndex(), OBParser.sqlString(sch.getOpt1()), OBParser.sqlString(sch.getOpt2()),
					OBParser.sqlString(sch.getOpt3()), OBParser.sqlString(sch.getOpt4()),
					OBParser.sqlString(sch.getOpt5()), sch.getIndex());

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	public void addSchedule(OBDtoSchedule sch, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. sch:%s", sch));

		String sqlText = "";
		try {
			sqlText = String.format(
					"INSERT INTO " + " MNG_SCHEDULE"
							+ " (TYPE, CYCLE_YN, WKDY, DAY, HOUR, ADC_INDEX, Opt1, Opt2, Opt3, Opt4, Opt5) "
							+ " VALUES (%d, %d, %d, %d, %d, %d, %s, %s, %s, %s, %s);",
					sch.getType(), sch.getIsCycle(), sch.getWkdy(), sch.getDay(), sch.getHour(), sch.getAdcIndex(),
					OBParser.sqlString(sch.getOpt1()), OBParser.sqlString(sch.getOpt2()),
					OBParser.sqlString(sch.getOpt3()), OBParser.sqlString(sch.getOpt4()),
					OBParser.sqlString(sch.getOpt5()));

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	public void delSchedule(int index, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format("DELETE FROM MNG_SCHEDULE" + " WHERE INDEX = %d;", index);

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public ArrayList<OBDtoSchedule> getScheduleList(OBDatabase db) throws OBException {
		ArrayList<OBDtoSchedule> list = new ArrayList<OBDtoSchedule>();

		String sqlText = "";

		try {
			sqlText = String
					.format("SELECT INDEX, TYPE, CYCLE_YN, WKDY, DAY, HOUR, ADC_INDEX, Opt1, Opt2, Opt3, Opt4, Opt5 "
							+ "FROM MNG_SCHEDULE;");

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoSchedule obj = new OBDtoSchedule();
				obj.setAdcIndex(db.getInteger(rs, "INDEX"));
				obj.setType(db.getInteger(rs, "TYPE"));
				obj.setIsCycle(db.getInteger(rs, "CYCLE_YN"));
				obj.setWkdy(db.getInteger(rs, "WKDY"));
				obj.setDay(db.getInteger(rs, "DAY"));
				obj.setHour(db.getInteger(rs, "HOUR"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setOpt1(db.getString(rs, "Opt1"));
				obj.setOpt2(db.getString(rs, "Opt2"));
				obj.setOpt3(db.getString(rs, "Opt3"));
				obj.setOpt4(db.getString(rs, "Opt4"));
				obj.setOpt5(db.getString(rs, "Opt5"));
				list.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return list;
	}

	private String convertByteArrayToString(byte[] byteArray) {
		int lastGoodChar = 0;

		for (int i = 0; i < byteArray.length; i++) {
			if (byteArray[i] == 0x00) {
				lastGoodChar = i;
				break;
			}
		}
		String value = new String(byteArray, 0, lastGoodChar);
		return value;
	}

	public OBDtoSystemEnvNetwork getNetworkConfig() throws OBException {
//		String sqlText = "";
		OBDtoSystemEnvNetwork result = new OBDtoSystemEnvNetwork();

		ByteBuffer szIPAddr = ByteBuffer.allocate(32);
		ByteBuffer szMask = ByteBuffer.allocate(32);
		ByteBuffer szGateway = ByteBuffer.allocate(32);

		try {
			if (OBDateTime.isWindows()) {
				try {
					result.setGateway("0.0.0.0");
					result.setHostName("Adcsmart");
					result.setIpAddress(Inet4Address.getLocalHost().getHostAddress());
					result.setNetmask("255.255.255.0");
				} catch (Exception e) {
				}
			} else {
				OBSystem.INSTANCE.OBGetSystemNetworkInfo(szIPAddr, szMask, szGateway);
				result.setGateway(convertByteArrayToString(szGateway.array()));
				result.setHostName(getKernelHostName()); // 시스템에 적용되어 있는 hostname을 읽어온다. /proc/sys/kernel/hostname에 있음
				result.setIpAddress(convertByteArrayToString(szIPAddr.array()));
				result.setNetmask(convertByteArrayToString(szMask.array()));
			}
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		return result;
	}

//	private void updateNetworkConfigHostname(String hostname, OBDatabase db) throws OBException
//	{
//		String sqlText = "";
//		try
//		{
//			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
//			sqlText = String.format(" UPDATE MNG_ENV_NETWORK " +
//									" SET OCCUR_TIME = %s, HOSTNAME = %s " +
//									" WHERE INDEX > 0;",
//									OBParser.sqlString(now),
//									OBParser.sqlString(hostname));
//			db.executeUpdate(sqlText);
//		}
//		catch(SQLException e)
//		{
//			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sqlText));
//		}		
//		catch(OBException e)
//		{
//			throw e;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//	}
//	
//	private void updateNetworkConfigGateway(String gateway, OBDatabase db) throws OBException
//	{
//		String sqlText = "";
//		try
//		{
//			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
//			sqlText = String.format(" UPDATE MNG_ENV_NETWORK " +
//									" SET OCCUR_TIME = %s, GATEWAY = %s " +
//									" WHERE INDEX > 0;",
//									OBParser.sqlString(now),
//									OBParser.sqlString(gateway));
//			db.executeUpdate(sqlText);
//		}
//		catch(SQLException e)
//		{
//			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sqlText));
//		}		
//		catch(OBException e)
//		{
//			throw e;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//	}
//	
//	private void updateNetworkConfigIPAddress(String ipAddress, String netmask, OBDatabase db) throws OBException
//	{
//		String sqlText = "";
//		try
//		{
//			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
//			sqlText = String.format(" UPDATE MNG_ENV_NETWORK " +
//									" SET OCCUR_TIME = %s, IPADDRESS = %s, NETMASK = %s " +
//									" WHERE INDEX > 0;",
//									OBParser.sqlString(now),
//									OBParser.sqlString(ipAddress),
//									OBParser.sqlString(netmask));
//			db.executeUpdate(sqlText);
//		}
//		catch(SQLException e)
//		{
//			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sqlText));
//		}		
//		catch(OBException e)
//		{
//			throw e;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//	}
//	
	public OBDtoSystemEnvAdditional getAdditionalConfig(OBDatabase db) throws OBException {
		OBDtoSystemEnvAdditional result = new OBDtoSystemEnvAdditional();
		try {
			result = getAdditionalConfigCore(db);
			result = getAdditionalConfigExtend(db, result);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return result;
	}

	private OBDtoSystemEnvAdditional getAdditionalConfigCore(OBDatabase db) throws OBException {
		String sqlText = "";
		OBDtoSystemEnvAdditional result = new OBDtoSystemEnvAdditional();
		try {
			result.setDoubleLoginAccess(getEnvLoginAccess(db));// 중복 로그인 허용 여부 검사.

			sqlText = String.format(
					" SELECT OCCUR_TIME, INTERVAL_CONF_SYNC, INTERVAL_SYSTEM_INFO, IS_USE_TIMESYNC, TIME_SERVER, \n"
							+ " SYSLOG_PORT, SYSLOG_SERVER, ALTEON_AUTO_SAVE, SERVICE_RESPONSE_TIME, RESP_TIME_SECTION, RESP_TIME_INTERVAL, \n"
							+ " SNMPTRAP_SERVER, SNMPTRAP_PORT, SNMPTRAP_COMMUNITY, SNMPTRAP_VERSION, SNMP_COMMUNITY, SNMP_ACCESSTYPE, \n"
							+ " SNMPV3_USERID, SNMPV3_AUTHPASSWORD, SNMPV3_PRIVPASSWORD, SNMPV3_ALGORITHM \n"
							+ " FROM MNG_ENV_ADDITIONAL \n" + " WHERE INDEX = 1 ");
			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				result.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				result.setIntervalAdcConfSync(db.getInteger(rs, "INTERVAL_CONF_SYNC"));
				result.setIntervalSystemInfo(db.getInteger(rs, "INTERVAL_SYSTEM_INFO"));
				result.setIsTimeSync(db.getInteger(rs, "IS_USE_TIMESYNC"));
				result.setSyslogPort(db.getInteger(rs, "SYSLOG_PORT"));
				result.setSyslogServerAddress(db.getString(rs, "SYSLOG_SERVER"));
				result.setTimeServerAddress(db.getString(rs, "TIME_SERVER"));
				result.setAlteonAutoSave(db.getInteger(rs, "ALTEON_AUTO_SAVE"));
				result.setServiceResponseTime(db.getInteger(rs, "SERVICE_RESPONSE_TIME"));
				result.setRespTimeSection(db.getInteger(rs, "RESP_TIME_SECTION"));
				result.setRespTimeInterval(db.getInteger(rs, "RESP_TIME_INTERVAL"));

				OBDtoSnmpTrap snmpTrap = new OBDtoSnmpTrap();
				if (db.getString(rs, "SNMPTRAP_SERVER") != null)
					snmpTrap.setSnmpTrapServerAddress(db.getString(rs, "SNMPTRAP_SERVER"));
				snmpTrap.setSnmpTrapPort(db.getInteger(rs, "SNMPTRAP_PORT"));
				snmpTrap.setSnmpTrapCommunity(db.getString(rs, "SNMPTRAP_COMMUNITY"));
				snmpTrap.setSnmpTrapVersion(db.getInteger(rs, "SNMPTRAP_VERSION"));
				result.setSnmpTrap(snmpTrap);

				OBDtoSystemSnmpInfo snmpCommunity = new OBDtoSystemSnmpInfo();
				snmpCommunity.setCommunity(db.getString(rs, "SNMP_COMMUNITY"));
				snmpCommunity.setAccessType(db.getString(rs, "SNMP_ACCESSTYPE"));
				snmpCommunity.setUserId(db.getString(rs, "SNMPV3_USERID"));
				snmpCommunity.setAuthPassword(db.getString(rs, "SNMPV3_AUTHPASSWORD"));
				snmpCommunity.setPrivPassword(db.getString(rs, "SNMPV3_PRIVPASSWORD"));
				snmpCommunity.setAlgorithm(db.getString(rs, "SNMPV3_ALGORITHM"));
				result.setSnmpCommunity(snmpCommunity);
			} else
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("not found data. sqlText:%s", sqlText));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return result;
	}

	// MNG_ENV_EXTEND에 정의된 내용을 읽어들여 설정한다.
	private OBDtoSystemEnvAdditional getAdditionalConfigExtend(OBDatabase db, OBDtoSystemEnvAdditional result)
			throws OBException {
		String sqlText = "";
		try {
			result.setDoubleLoginAccess(getEnvLoginAccess(db));// 중복 로그인 허용 여부 검사.

			sqlText = String.format(" SELECT ENV_KEY, ENV_VALUE FROM MNG_ENV_EXTEND");
			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				String key = db.getString(rs, "ENV_KEY");
				if (ENV_KEY_ALARM_POPUP_YN.equals(key)) {
					String value = db.getString(rs, "ENV_VALUE");
					if ("OFF".equals(value))
						result.setAlarmPopupYn(0);
					else
						result.setAlarmPopupYn(1);
				} else if (ENV_KEY_SMS_ACTION_YN.equals(key)) {
					String value = db.getString(rs, "ENV_VALUE");
					if ("OFF".equals(value))
						result.setSmsActionYn(0);
					else
						result.setSmsActionYn(1);
				} else if (ENV_KEY_SMS_ACTION_TYPE.equals(key)) {
					String value = db.getString(rs, "ENV_VALUE");
					result.setSmsActionType(value);
				} else if (ENV_KEY_SMS_HP_NUMBERS.equals(key)) {
					String value = db.getString(rs, "ENV_VALUE");
					result.setSmsHPNumbers(value);
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return result;
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public OBDtoSystemEnvAdditional getAdditionalConfig() throws OBException {
		String sqlText = "";
		OBDtoSystemEnvAdditional result = new OBDtoSystemEnvAdditional();
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			result.setDoubleLoginAccess(getEnvLoginAccessNew());// 중복 로그인 허용 여부 검사.

			sqlText = String.format(
					" SELECT OCCUR_TIME, INTERVAL_CONF_SYNC, INTERVAL_SYSTEM_INFO, IS_USE_TIMESYNC, TIME_SERVER, "
							+ " SYSLOG_PORT, SYSLOG_SERVER, ALTEON_AUTO_SAVE, SERVICE_RESPONSE_TIME, RESP_TIME_SECTION, RESP_TIME_INTERVAL, "
							+ " SNMPTRAP_SERVER, SNMPTRAP_PORT, SNMPTRAP_COMMUNITY, SNMPTRAP_VERSION, SNMP_COMMUNITY, SNMP_ACCESSTYPE "
							+ " FROM MNG_ENV_ADDITIONAL " + " WHERE INDEX = 1 ");
			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				result.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				result.setIntervalAdcConfSync(db.getInteger(rs, "INTERVAL_CONF_SYNC"));
				result.setIntervalSystemInfo(db.getInteger(rs, "INTERVAL_SYSTEM_INFO"));
				result.setIsTimeSync(db.getInteger(rs, "IS_USE_TIMESYNC"));
				result.setSyslogPort(db.getInteger(rs, "SYSLOG_PORT"));
				result.setSyslogServerAddress(db.getString(rs, "SYSLOG_SERVER"));
				result.setTimeServerAddress(db.getString(rs, "TIME_SERVER"));
				result.setAlteonAutoSave(db.getInteger(rs, "ALTEON_AUTO_SAVE"));
				result.setServiceResponseTime(db.getInteger(rs, "SERVICE_RESPONSE_TIME"));
				result.setRespTimeSection(db.getInteger(rs, "RESP_TIME_SECTION"));
				result.setRespTimeInterval(db.getInteger(rs, "RESP_TIME_INTERVAL"));

				OBDtoSnmpTrap snmpTrap = new OBDtoSnmpTrap();
				if (db.getString(rs, "SNMPTRAP_SERVER") != null)
					snmpTrap.setSnmpTrapServerAddress(db.getString(rs, "SNMPTRAP_SERVER"));
				snmpTrap.setSnmpTrapPort(db.getInteger(rs, "SNMPTRAP_PORT"));
				snmpTrap.setSnmpTrapCommunity(db.getString(rs, "SNMPTRAP_COMMUNITY"));
				snmpTrap.setSnmpTrapVersion(db.getInteger(rs, "SNMPTRAP_VERSION"));
				result.setSnmpTrap(snmpTrap);

				OBDtoSystemSnmpInfo snmpCommunity = new OBDtoSystemSnmpInfo();
				snmpCommunity.setCommunity(db.getString(rs, "SNMP_COMMUNITY"));
				snmpCommunity.setAccessType(db.getString(rs, "SNMP_ACCESSTYPE"));
				result.setSnmpCommunity(snmpCommunity);
			} else
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("not found data. sqlText:%s", sqlText));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return result;
	}

	public Integer getEnvLoginAccess(OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(" SELECT ENV_VALUE FROM MNG_ENV_EXTEND WHERE ENV_KEY = %s ;",
					OBParser.sqlString(ENV_KEY_LOGINACCESS));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				if (db.getString(rs, "ENV_VALUE").contentEquals("OFF"))// 중복 허용.
				{
					return 0;
				} else {
					return 1;// 중복허용하지 않음.
				}
			} else {
				return 0;// 중복 허용.
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public Integer getEnvAlarmPopupYn(OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(" SELECT ENV_VALUE FROM MNG_ENV_EXTEND WHERE ENV_KEY = %s ;",
					OBParser.sqlString(ENV_KEY_ALARM_POPUP_YN));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				if (db.getString(rs, "ENV_VALUE").contentEquals("OFF"))// 중복 허용.
				{
					return 0;
				} else {
					return 1;//
				}
			} else {
				return 0;//
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public Integer getEnvLoginAccessNew() throws OBException {
		String sqlText = "";

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			sqlText = String.format(" SELECT ENV_VALUE FROM MNG_ENV_EXTEND WHERE ENV_KEY = %s ;",
					OBParser.sqlString(ENV_KEY_LOGINACCESS));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				if (db.getString(rs, "ENV_VALUE").contentEquals("OFF"))// 중복 허용.
				{
					return 0;
				} else {
					return 1;// 중복허용하지 않음.
				}
			} else {
				return 0;// 중복 허용.
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private void setEnvLoginAccess(Integer isNotallowd, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			if (isNotallowd > 0) {
				sqlText = String.format(" UPDATE MNG_ENV_EXTEND " + " SET ENV_VALUE=%s " + " WHERE ENV_KEY = %s ;",
						OBParser.sqlString("ON"), OBParser.sqlString(ENV_KEY_LOGINACCESS));
			} else {
				sqlText = String.format(" UPDATE MNG_ENV_EXTEND " + " SET ENV_VALUE=%s " + " WHERE ENV_KEY = %s ;",
						OBParser.sqlString("OFF"), OBParser.sqlString(ENV_KEY_LOGINACCESS));
			}
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private void setEnvAlarmPopupYn(Integer isNotallowd, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			String value = "OFF";
			if (isNotallowd > 0) {
				value = "ON";
			} else {
				sqlText = String.format(" UPDATE MNG_ENV_EXTEND " + " SET ENV_VALUE=%s " + " WHERE ENV_KEY = %s ;",
						OBParser.sqlString("OFF"), OBParser.sqlString(ENV_KEY_ALARM_POPUP_YN));
			}

			sqlText = String.format(" SELECT ENV_KEY, ENV_VALUE FROM MNG_ENV_EXTEND WHERE ENV_KEY=%s;",
					OBParser.sqlString(ENV_KEY_ALARM_POPUP_YN));
			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				// update
				sqlText = String.format(" UPDATE MNG_ENV_EXTEND " + " SET ENV_VALUE=%s " + " WHERE ENV_KEY = %s ;",
						OBParser.sqlString(value), OBParser.sqlString(ENV_KEY_ALARM_POPUP_YN));
			} else {
				// insert
				sqlText = String.format(" INSERT INTO MNG_ENV_EXTEND (ENV_KEY, ENV_VALUE) VALUES (%s, %s);",
						OBParser.sqlString(ENV_KEY_ALARM_POPUP_YN), OBParser.sqlString(value));
			}
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private void setEnvSMSConfig(Integer isNotallowd, String actionType, String hpNumbers, OBDatabase db)
			throws OBException {
		String sqlText = "";
		try {
			String action = "OFF";
			if (isNotallowd > 0) {
				action = "ON";
			} else {
				sqlText = String.format(" UPDATE MNG_ENV_EXTEND " + " SET ENV_VALUE=%s " + " WHERE ENV_KEY = %s ;",
						OBParser.sqlString("OFF"), OBParser.sqlString(ENV_KEY_SMS_ACTION_YN));
			}

			sqlText = String.format(" SELECT ENV_KEY, ENV_VALUE FROM MNG_ENV_EXTEND WHERE ENV_KEY=%s;",
					OBParser.sqlString(ENV_KEY_SMS_ACTION_YN));
			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				// update
				sqlText = String.format(" UPDATE MNG_ENV_EXTEND " + " SET ENV_VALUE=%s " + " WHERE ENV_KEY = %s ;",
						OBParser.sqlString(action), OBParser.sqlString(ENV_KEY_SMS_ACTION_YN));
				sqlText += String.format(" UPDATE MNG_ENV_EXTEND " + " SET ENV_VALUE=%s " + " WHERE ENV_KEY = %s ;",
						OBParser.sqlString(actionType), OBParser.sqlString(ENV_KEY_SMS_ACTION_TYPE));
				sqlText += String.format(" UPDATE MNG_ENV_EXTEND " + " SET ENV_VALUE=%s " + " WHERE ENV_KEY = %s ;",
						OBParser.sqlString(hpNumbers), OBParser.sqlString(ENV_KEY_SMS_HP_NUMBERS));
			} else {
				// insert
				sqlText = String.format(" INSERT INTO MNG_ENV_EXTEND (ENV_KEY, ENV_VALUE) VALUES (%s, %s);",
						OBParser.sqlString(ENV_KEY_SMS_ACTION_YN), OBParser.sqlString(action));
				sqlText += String.format(" INSERT INTO MNG_ENV_EXTEND (ENV_KEY, ENV_VALUE) VALUES (%s, %s);",
						OBParser.sqlString(ENV_KEY_SMS_ACTION_TYPE), OBParser.sqlString(actionType));
				sqlText += String.format(" INSERT INTO MNG_ENV_EXTEND (ENV_KEY, ENV_VALUE) VALUES (%s, %s);",
						OBParser.sqlString(ENV_KEY_SMS_HP_NUMBERS), OBParser.sqlString(hpNumbers));
			}
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private void updateAdditionalConfigSyslog(int port, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(
					" UPDATE MNG_ENV_ADDITIONAL " + " SET OCCUR_TIME = %s, SYSLOG_PORT = %d " + " WHERE INDEX = 1;",
					OBParser.sqlString(now), port);
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private void updateAdditionalConfigTimeSync(int onoff, String address, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(" UPDATE MNG_ENV_ADDITIONAL "
					+ " SET OCCUR_TIME = %s, IS_USE_TIMESYNC = %d, TIME_SERVER = %s " + " WHERE INDEX = 1;",
					OBParser.sqlString(now), onoff, OBParser.sqlString(address));
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private void updateAdditionalConfigSysSync(int interval, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(" UPDATE MNG_ENV_ADDITIONAL " + " SET OCCUR_TIME = %s, INTERVAL_SYSTEM_INFO = %d "
					+ " WHERE INDEX = 1;", OBParser.sqlString(now), interval);
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

	}

	private void updateAdditionalConfigAdcSync(int interval, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(" UPDATE MNG_ENV_ADDITIONAL " + " SET OCCUR_TIME = %s, INTERVAL_CONF_SYNC = %d "
					+ " WHERE INDEX = 1;", OBParser.sqlString(now), interval);
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

	}

	public OBDtoSystemEnvView getViewConfig(OBDatabase db) throws OBException {
		String sqlText = "";
		OBDtoSystemEnvView result = new OBDtoSystemEnvView();
		try {
			sqlText = String.format(" SELECT OCCUR_TIME, LOGNUM, PERIOD, AUTO_REFRESH, LOGOUTTIME "
					+ " FROM MNG_ENV_VIEW " + " LIMIT 1 ");
			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				result.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				result.setAutoLogoutTime(db.getInteger(rs, "LOGOUTTIME"));
				result.setAutoRefrash(db.getInteger(rs, "AUTO_REFRESH"));
				result.setLogViewCount(db.getInteger(rs, "LOGNUM"));
				result.setLogViewPeriodType(db.getInteger(rs, "PERIOD"));
			} else
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("not found data. sqlText:%s", sqlText));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return result;
	}

	public OBDtoSyncSystemTime getSyncTimeConfig(OBDatabase db) throws OBException {
		String sqlText = "";
		OBDtoSyncSystemTime result = new OBDtoSyncSystemTime();
		try {
			sqlText = String.format(" SELECT USENTP_YN, PRIMARY_NTPIP, SECONDARY_NTPIP, INTERVAL_NTP_SYNC "
					+ " FROM MNG_ENV_ADDITIONAL " + " LIMIT 1 ");
			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				result.setUseNTP_YN(db.getInteger(rs, "USENTP_YN"));
				result.setPrimary_NTP(db.getString(rs, "PRIMARY_NTPIP"));
				result.setSecondary_NTP(db.getString(rs, "SECONDARY_NTPIP"));
				result.setIntervalNTPSync(db.getInteger(rs, "INTERVAL_NTP_SYNC"));
			} else
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("not found data. sqlText:%s", sqlText));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return result;
	}

	public OBDtoSyncSystemTime getSyncTimeConfigDB() throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		OBDtoSyncSystemTime result = new OBDtoSyncSystemTime();
		try {
			db.openDB();
			sqlText = String.format(" SELECT USENTP_YN, PRIMARY_NTPIP, SECONDARY_NTPIP, INTERVAL_NTP_SYNC "
					+ " FROM MNG_ENV_ADDITIONAL " + " LIMIT 1 ");
			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				result.setUseNTP_YN(db.getInteger(rs, "USENTP_YN"));
				result.setPrimary_NTP(db.getString(rs, "PRIMARY_NTPIP"));
				result.setSecondary_NTP(db.getString(rs, "SECONDARY_NTPIP"));
				result.setIntervalNTPSync(db.getInteger(rs, "INTERVAL_NTP_SYNC"));
			} else
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("not found data. sqlText:%s", sqlText));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return result;
	}

	private void updateUSENTPYN(int NTPYN) throws OBException {
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(
					" UPDATE MNG_ENV_ADDITIONAL " + " SET OCCUR_TIME = %s, USENTP_YN = %d " + " WHERE INDEX > 0;",
					OBParser.sqlString(now), NTPYN);
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private void updatePrimaryNTP(String PrimaryNTPIP, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(
					" UPDATE MNG_ENV_ADDITIONAL " + " SET OCCUR_TIME = %s, PRIMARY_NTPIP = %s " + " WHERE INDEX > 0;",
					OBParser.sqlString(now), OBParser.sqlString(PrimaryNTPIP));
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private void updateSecondaryNTP(String SecondaryNTPIP, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(
					" UPDATE MNG_ENV_ADDITIONAL " + " SET OCCUR_TIME = %s, SECONDARY_NTPIP = %s " + " WHERE INDEX > 0;",
					OBParser.sqlString(now), OBParser.sqlString(SecondaryNTPIP));
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private void updateIntervalNTP(int IntervalNTP, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(" UPDATE MNG_ENV_ADDITIONAL " + " SET OCCUR_TIME = %s, INTERVAL_NTP_SYNC = %d "
					+ " WHERE INDEX > 0;", OBParser.sqlString(now), IntervalNTP);
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private void updateViewConfigLogout(int interval, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(
					" UPDATE MNG_ENV_VIEW " + " SET OCCUR_TIME = %s, LOGOUTTIME = %d " + " WHERE INDEX > 0;",
					OBParser.sqlString(now), interval);
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

	}

	private void updateViewConfigRefresh(int interval, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(
					" UPDATE MNG_ENV_VIEW " + " SET OCCUR_TIME = %s, AUTO_REFRESH = %d " + " WHERE INDEX > 0;",
					OBParser.sqlString(now), interval);
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

	}

	private void updateViewConfigMonperiod(int period, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(
					" UPDATE MNG_ENV_VIEW " + " SET OCCUR_TIME = %s, PERIOD = %d " + " WHERE INDEX > 0 ;",
					OBParser.sqlString(now), period);
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

	}

	private void updateViewConfigLognum(int lognum, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(
					" UPDATE MNG_ENV_VIEW " + " SET OCCUR_TIME = %s, LOGNUM = %d " + " WHERE INDEX > 0;",
					OBParser.sqlString(now), lognum);
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

	}

	@Override
	public OBDtoSystemEnv getSystemConfig() throws OBException {
		OBDatabase db = new OBDatabase();

		OBDtoSystemEnv result = new OBDtoSystemEnv();
		try {
			db.openDB();

			result.setNetworkInfo(getNetworkConfig());
			result.setAdditionalInfo(getAdditionalConfig(db));
			result.setViewInfo(getViewConfig(db));
			result.setSchBackupInfo(getScheduleBackupInfo(db));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		return result;
	}

	@Override
	public void syncNTPServer(String ntpServer, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBException {
		try {
			if (ntpServer.isEmpty())
				throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "invalid ntp server address");

			OBDateTime.syncNTPTime(ntpServer);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private boolean isExistPattern(String pattern, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(" SELECT INDEX FROM MNG_ADCLOG_FILTER_PATTERN "
					+ " WHERE USER_PATTERN=%s AND STATE=%d " + " LIMIT 1 ", OBParser.sqlString(pattern),
					OBDefine.STATE_ENABLE);
			ResultSet rs = db.executeQuery(sqlText);

			boolean retVal = false;
			if (rs.next() == true)
				retVal = true;

			return retVal;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

//	public static void main(String[] args)
//	{
//			try
//			{
//				OBDtoAdcLogFilter info = new OBDtoAdcLogFilter();
//				info.setType(0);
//				info.setUserPattern("\\\\[ssl_req\\\\]");
//				new OBEnvManagementImpl().addAdcLogFitlerPattern(info);
//			}
//			catch(OBException e)
//			{
//				e.printStackTrace();
//				return;
//			}
//	}

	@Override
	public void addAdcLogFitlerPattern(OBDtoAdcLogFilter info, OBDtoExtraInfo extraInfo) throws OBException {
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();
			addAdcLogFitlerPattern(info, extraInfo, db);
			String matchType = "";
			if (info.getType() == 0)// 부분매칭..
			{
				matchType = "부분";
			} else {
				matchType = "전부";
			}
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_ENV_ADD_ADCLOG_FILTER_SUCCESS, info.getUserPattern(), matchType);
		} catch (PatternSyntaxException e) {
			throw new OBException(OBException.ERRCODE_SYSCFG_FILTERSYNTAX);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void addAdcLogFitlerPattern(OBDtoAdcLogFilter info, OBDtoExtraInfo extraInfo, OBDatabase db)
			throws PatternSyntaxException, OBException {
		try {
			if (isExistPattern(info.getUserPattern(), db) == true) {// 이미 등록된 경우. 조용히 종료한다.
				OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("already exist pattern"));
				return;
			}
			String regPattern = "";

			String userPattern = info.getUserPattern();
			// String userPattern = OBParser.escapeSql(info.getUserPattern());
			// ADC로그필터링시 싱글쿼터를 입력하면 더클쿼터가 나오는데 이유가 위의 esacpeSql에서 파싱해주고 밑에 한번더 파싱을 해주어서
			// 그렇게됨. 따라서 위 escapeSql부분을 주석처리. junhyun.ok_GS
			if (info.getType() == 0)// 부분매칭..
			{
				// 사용자 정의 패턴에는 regExp의 예약어를 이스케이프한다
				regPattern = ".*" + OBParser.escapeRegex(userPattern) + ".*";
			} else {
				regPattern = userPattern;
			}

			// syntax 오류 검사.
			Pattern.compile(regPattern, Pattern.DOTALL);
			String sqlText = "";
			sqlText = String.format(" INSERT INTO " + " MNG_ADCLOG_FILTER_PATTERN"
					+ " (OCCUR_TIME, REG_PATTERN, USER_PATTERN, TYPE, STATE) " + " VALUES (%s, %s, %s, %d, %d); ",
					OBParser.sqlString(OBDateTime.now()), OBParser.sqlString(regPattern),
					OBParser.sqlString(userPattern), info.getType(), OBDefine.STATE_ENABLE);

			db.executeUpdate(sqlText);
		} catch (PatternSyntaxException e) {
			throw new PatternSyntaxException(e.getMessage(), null, 0);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
	}

	@Override
	public void delAdcLogFilterPattern(ArrayList<Integer> delList, OBDtoExtraInfo extraInfo) throws OBException {
		if (delList.size() <= 0)
			return;

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			delAdcLogFilterPattern(delList, extraInfo, db);
			ArrayList<String> patternList = getUserPatternList(delList, db);
			for (String pattern : patternList)
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_ENV_DEL_ADCLOG_FILTER_SUCCESS, pattern);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private ArrayList<String> getUserPatternList(ArrayList<Integer> indexes, OBDatabase db) throws OBException {
		ArrayList<String> result = new ArrayList<String>();

		String sqlText = "";
		try {
			String subSql = "-1"; // where-in empty 방지
			for (int i = 0; i < indexes.size(); i++) {
				subSql += ",";
				subSql += indexes.get(i);
			}
			sqlText = String.format(
					" SELECT USER_PATTERN " + " FROM MNG_ADCLOG_FILTER_PATTERN " + " WHERE INDEX IN (%s) ;", // where-in:empty
																												// string
																												// 불가,
																												// null
																												// 불가,
																												// OK
					subSql);
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				result.add(db.getString(rs, "USER_PATTERN"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return result;
	}

	public void delAdcLogFilterPattern(ArrayList<Integer> delList, OBDtoExtraInfo extraInfo, OBDatabase db)
			throws OBException {
		if (delList.size() <= 0)
			return;

		String sqlText = "";
		try {
			String subSql = "-1"; // where-in empty 방지
			for (int i = 0; i < delList.size(); i++) {
				subSql += ",";
				subSql += delList.get(i);
			}

			sqlText = String.format(
					" UPDATE MNG_ADCLOG_FILTER_PATTERN " + " SET STATE = %d " + " WHERE INDEX IN (%s) ;", // where-in:empty
																											// string
																											// 불가, null
																											// 불가, OK
					OBDefine.STATE_DISABLE, subSql);
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

	}

	@Override
	public ArrayList<OBDtoAdcLogFilter> getAdcLogFilterPatternList() throws OBException {
		OBDatabase db = new OBDatabase();

		ArrayList<OBDtoAdcLogFilter> result = new ArrayList<OBDtoAdcLogFilter>();
		String sqlText = "";
		try {
			db.openDB();

			sqlText = String.format(" SELECT INDEX, OCCUR_TIME, REG_PATTERN, USER_PATTERN, TYPE, STATE "
					+ " FROM MNG_ADCLOG_FILTER_PATTERN " + " WHERE STATE=%d ", OBDefine.STATE_ENABLE);
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcLogFilter obj = new OBDtoAdcLogFilter();
				obj.setIndex(db.getInteger(rs, "INDEX"));
				obj.setRegPattern(db.getString(rs, "REG_PATTERN"));
				obj.setType(db.getInteger(rs, "TYPE"));
				obj.setUserPattern(db.getString(rs, "USER_PATTERN"));
				result.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return result;
	}

	@Override
	public OBDtoSyncSystemTime getSyncSystemTimeConfig() throws OBException {
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();

			sqlText = String.format(" SELECT USENTP_YN, PRIMARY_NTPIP, SECONDARY_NTPIP, INTERVAL_NTP_SYNC "
					+ " FROM MNG_ENV_ADDITIONAL " + " WHERE INDEX=1 ; ");
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return null;
			}
			OBDtoSyncSystemTime result = new OBDtoSyncSystemTime();
			result.setUseNTP_YN(db.getInteger(rs, "USENTP_YN"));
			result.setPrimary_NTP(db.getString(rs, "PRIMARY_NTPIP"));
			result.setSecondary_NTP(db.getString(rs, "SECONDARY_NTPIP"));
			result.setIntervalNTPSync(db.getInteger(rs, "INTERVAL_NTP_SYNC"));
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

	}

	@Override
	public boolean setSyncSystemTimeConfig(OBDtoSyncSystemTime newInfo, OBDtoExtraInfo extraInfo) throws OBException {
		try { // 시스템 시간 동기화 미 설정
			if (newInfo.getTimeSyncType() == OBDefine.SYSTEM_TIME_SYNC_TYPE_NONE) {
				updateUSENTPYN(OBDefine.NTP_NONE);
				return true;
			}
			// 시스템 시간 동기화 사용자 수동설정
			else if (newInfo.getTimeSyncType() == OBDefine.SYSTEM_TIME_SYNC_TYPE_MAUALLY) {
				updateUSENTPYN(OBDefine.NTP_NONE);
				setSystemTimeManually(newInfo);
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_ENV_SET_SYSTEM_TIME_MANUALLY);
				return true;
			}
			// 시스템 시간 동기화 NTP 설정
			else if (newInfo.getTimeSyncType() == OBDefine.SYSTEM_TIME_SYNC_TYPE_NTP) {
				updateUSENTPYN(OBDefine.NTP_USE);
				setSyncNTPConfig(newInfo, extraInfo);
				if (setSystemTimeNTP(newInfo) == true) {
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_SYSTEM_TIME_NTP_SUCCESS);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// System time sync 타입이 NTP 일 경우
	public void setSyncNTPConfig(OBDtoSyncSystemTime newInfo, OBDtoExtraInfo extraInfo) throws OBException {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			// 이하 호출하는 업데이트 함수들에서 업데이트 대상 record의 index 조건을 주지 않는데, 테이블에 1개의 레코드만 존재하기 때문이다.
			// UPDATE QUERY에서 WHERE INDEX > 0으로 처리하기 때문에 테이블의 레코드가 2개 이상이 되는 상황이 되지 않도록 신경써야
			// 한다.

			OBDtoSyncSystemTime oldSyncConfig = getSyncTimeConfig(db);

			if (oldSyncConfig.getPrimary_NTP() == null && newInfo.getPrimary_NTP() != null) {
				updatePrimaryNTP(newInfo.getPrimary_NTP(), db);
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_ENV_SET_PRIMARY_NTP_SUCCESS, newInfo.getPrimary_NTP());
			} else if (oldSyncConfig.getPrimary_NTP() != null && newInfo.getPrimary_NTP() != null) {
				if (oldSyncConfig.getPrimary_NTP().equals(newInfo.getPrimary_NTP()) == false) {
					updatePrimaryNTP(newInfo.getPrimary_NTP(), db);
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_PRIMARY_NTP_SUCCESS, newInfo.getPrimary_NTP());
				}
			}

			if (oldSyncConfig.getSecondary_NTP() == null && newInfo.getSecondary_NTP() != null) {
				updateSecondaryNTP(newInfo.getSecondary_NTP(), db);
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_ENV_SET_SECONDARY_NTP_SUCCESS, newInfo.getSecondary_NTP());
			} else if (oldSyncConfig.getSecondary_NTP() != null && newInfo.getSecondary_NTP() != null) {
				if (oldSyncConfig.getSecondary_NTP().equals(newInfo.getSecondary_NTP()) == false) {
					updateSecondaryNTP(newInfo.getSecondary_NTP(), db);
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_SECONDARY_NTP_SUCCESS, newInfo.getSecondary_NTP());
				}
			}

			if (oldSyncConfig.getIntervalNTPSync() == null && newInfo.getIntervalNTPSync() != null) {
				updateIntervalNTP(newInfo.getIntervalNTPSync(), db);
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_ENV_SET_NTP_INTERVAL, newInfo.getIntervalNTPSync().toString());
			} else if (oldSyncConfig.getIntervalNTPSync() != null && newInfo.getIntervalNTPSync() != null) {
				if (oldSyncConfig.getIntervalNTPSync().equals(newInfo.getIntervalNTPSync()) == false) {
					updateIntervalNTP(newInfo.getIntervalNTPSync(), db);
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_NTP_INTERVAL, newInfo.getIntervalNTPSync().toString());
				}
			}
			setEnvLastUpdateTime(db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void setSystemTimeManually(OBDtoSyncSystemTime newInfo) throws OBException {
		try {
			OBDateTime.setSystemTime(newInfo.getManuallyTime());
			OBDateTime.setHwClock();
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public boolean setSystemTimeNTP(OBDtoSyncSystemTime newInfo) throws OBException {
		try {
			if (newInfo.getPrimary_NTP() == null || newInfo.getPrimary_NTP().equals("")) {
				return false;
			}
			if (OBDateTime.syncNTPTime(newInfo.getPrimary_NTP()) == false) {
				if (newInfo.getSecondary_NTP() == null || newInfo.getSecondary_NTP().equals("")) {
					return false;
				}
				if (OBDateTime.syncNTPTime(newInfo.getSecondary_NTP()) == false) {
					return false;
				}
			}
			return true;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public void setViewConfig(OBDtoSystemEnvView newEnv, OBDtoExtraInfo extraInfo) throws OBException {
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			// 이하 호출하는 업데이트 함수들에서 업데이트 대상 record의 index 조건을 주지 않는데, 테이블에 1개의 레코드만 존재하기 때문이다.
			// UPDATE QUERY에서 WHERE INDEX > 0으로 처리하기 때문에 테이블의 레코드가 2개 이상이 되는 상황이 되지 않도록 신경써야
			// 한다.

			OBDtoSystemEnvView oldEnv = getViewConfig(db);

			if (oldEnv.getAutoLogoutTime() != null && newEnv.getAutoLogoutTime() != null) {
				if (oldEnv.getAutoLogoutTime().equals(newEnv.getAutoLogoutTime()) == false) {
					updateViewConfigLogout(newEnv.getAutoLogoutTime().intValue(), db);
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_LOGOUT_TIME_SUCCESS, newEnv.getAutoLogoutTime().toString());
				}
			}
			if (oldEnv.getAutoRefrash() != null && newEnv.getAutoRefrash() != null) {
				if (oldEnv.getAutoRefrash().equals(newEnv.getAutoRefrash()) == false) {
					updateViewConfigRefresh(newEnv.getAutoRefrash().intValue(), db);
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_REFRESH_TIME_SUCCESS, newEnv.getAutoRefrash().toString());
				}
			}
			if (oldEnv.getLogViewCount() != null && newEnv.getLogViewCount() != null) {
				if (oldEnv.getLogViewCount().equals(newEnv.getLogViewCount()) == false) {
					updateViewConfigLognum(newEnv.getLogViewCount().intValue(), db);
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_MAX_LOG_SUCCESS, newEnv.getLogViewCount().toString());
				}
			}
			if (oldEnv.getLogViewPeriodType() != null && newEnv.getLogViewPeriodType() != null) {
				if (oldEnv.getLogViewPeriodType().equals(newEnv.getLogViewPeriodType()) == false) {
					updateViewConfigMonperiod(newEnv.getLogViewPeriodType().intValue(), db);
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_MONITOR_TIME_SUCCESS, newEnv.getLogViewPeriodType().toString());
				}
			}
			setEnvLastUpdateTime(db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public void setAdditionalConfig(OBDtoSystemEnvAdditional newEnv, OBDtoExtraInfo extraInfo) throws OBException {
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			OBDtoSystemEnvAdditional oldEnv = getAdditionalConfig(db);

			boolean isTimeSync = false;
			if (oldEnv.getIsTimeSync() != null && newEnv.getIsTimeSync() != null) {
				if (oldEnv.getIsTimeSync().equals(newEnv.getIsTimeSync()) == false) {
					updateAdditionalConfigTimeSync(newEnv.getIsTimeSync().intValue(), newEnv.getTimeServerAddress(),
							db);

					String onoff = "disabled";
					if (newEnv.getIsTimeSync().equals(1))
						onoff = "enabled";
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_TIME_SYNC_SUCCESS, onoff, newEnv.getTimeServerAddress());
					isTimeSync = true;
				}
			}

			if (isTimeSync == false && oldEnv.getTimeServerAddress() != null && newEnv.getTimeServerAddress() != null) {
				if (oldEnv.getIsTimeSync().equals(newEnv.getIsTimeSync()) == false) {
					updateAdditionalConfigTimeSync(newEnv.getIsTimeSync().intValue(), newEnv.getTimeServerAddress(),
							db);

					String onoff = "disabled";
					if (newEnv.getIsTimeSync().equals(1))
						onoff = "enabled";
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_TIME_SYNC_SUCCESS, onoff, newEnv.getTimeServerAddress());
				}
			}
			if (oldEnv.getIntervalAdcConfSync() != null && newEnv.getIntervalAdcConfSync() != null) {
				if (oldEnv.getIntervalAdcConfSync().equals(newEnv.getIntervalAdcConfSync()) == false) {
					updateAdditionalConfigAdcSync(newEnv.getIntervalAdcConfSync().intValue(), db);
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_ADC_SYNC_TIME_SUCCESS,
							newEnv.getIntervalAdcConfSync().toString());
				}
			}
			if (oldEnv.getIntervalSystemInfo() != null && newEnv.getIntervalSystemInfo() != null) {
				if (oldEnv.getIntervalSystemInfo().equals(newEnv.getIntervalSystemInfo()) == false) {
					updateAdditionalConfigSysSync(newEnv.getIntervalSystemInfo().intValue(), db);
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_SYSTEM_TIME_SUCCESS, newEnv.getIntervalSystemInfo().toString());
				}
			}
			if (oldEnv.getSyslogPort() != null && newEnv.getSyslogPort() != null) {
				if (oldEnv.getSyslogPort().equals(newEnv.getSyslogPort()) == false) {
					updateAdditionalConfigSyslog(newEnv.getSyslogPort().intValue(), db);
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_SYSLOG_PORT_SUCCESS, newEnv.getSyslogPort().toString());
				}
			}

			if (oldEnv.getAlteonAutoSave() != null && newEnv.getAlteonAutoSave() != null) {
				if (oldEnv.getAlteonAutoSave().equals(newEnv.getAlteonAutoSave()) == false) {
					updateAdditionalConfigAlteonAutoSave(newEnv.getAlteonAutoSave().intValue(), db);
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_ALTEON_AUTO_SAVE_SUCCESS, newEnv.getSyslogPort().toString());
				}
			}
			// snsmpTrap 서버 설정
			updateAdditionalConfigSnmpTrapAddress(newEnv.getSnmpTrap(), db);
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_ENV_SET_SNMPTRAP_ONOFF_SUCCESS,
					newEnv.getSnmpTrap().getSnmpTrapServerAddress().toString());
//			if(!oldEnv.getSnmpTrap().toString().equals(newEnv.getSnmpTrap().toString()))
//			{
//			    if(oldEnv.getSnmpTrap().getSnmpTrapServerAddress()!=null && newEnv.getSnmpTrap().getSnmpTrapServerAddress()!=null)
//			    {
//			        updateAdditionalConfigSnmpTrapAddress(newEnv.getSnmpTrap(), db);
//			        new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(), OBSystemAudit.AUDIT_ENV_SET_SNMPTRAP_ONOFF_SUCCESS, newEnv.getSnmpTrap().getSnmpTrapServerAddress().toString());
//			    }
//			}

			// ADCsmart snmpCommunity
			updateAdditionalConfigSnmpCommunity(newEnv.getSnmpCommunity(), db);
			setSnmpConfig(newEnv.getSnmpCommunity());
			setSnmpV3Config(newEnv.getSnmpCommunity());
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_ENV_SET_SNMPCOMMUNITY_ONOFF_SUCCESS,
					newEnv.getSnmpCommunity().getCommunity().toString());

			// syslog 서버 IP를 설정한다. 기존에 있는데(!null) 지금과 다르거나(!null), 기존에 없었는데(null) 새로
			// 생겼거나(!null), 있다가(!null) 없어지면(null) 설정한다.
			if (oldEnv.getSyslogServerAddress() != null && newEnv.getSyslogServerAddress() != null)// !null -> !null
			{
				if (oldEnv.getSyslogServerAddress().equals(newEnv.getSyslogServerAddress()) == false) {
					updateAdditionalConfigSyslogServerAddress(newEnv.getSyslogServerAddress(), db);
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_ALERT_ONOFF_SUCCESS,
							newEnv.getSyslogServerAddress().toString());
				}
			} else if (oldEnv.getSyslogServerAddress() == null && newEnv.getSyslogServerAddress() != null)// null ->
																											// !null
			{
				updateAdditionalConfigSyslogServerAddress(newEnv.getSyslogServerAddress(), db);
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_ENV_SET_ALERT_ONOFF_SUCCESS, newEnv.getSyslogServerAddress().toString());
			} else if (oldEnv.getSyslogServerAddress() != null && newEnv.getSyslogServerAddress() == null)// !null ->
																											// null
			{
				updateAdditionalConfigSyslogServerAddress(newEnv.getSyslogServerAddress(), db);
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_ENV_SET_ALERT_ONOFF_SUCCESS, newEnv.getSyslogServerAddress().toString());
			}

			if (oldEnv.getDoubleLoginAccess().equals(newEnv.getDoubleLoginAccess()) == false) {
				setEnvLoginAccess(newEnv.getDoubleLoginAccess(), db);
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_ENV_SET_LOGINACCESS_ONOFF_SUCCESS,
						newEnv.getSyslogServerAddress().toString());
			}

			if (oldEnv.getAlarmPopupYn().equals(newEnv.getAlarmPopupYn()) == false) {
				setEnvAlarmPopupYn(newEnv.getAlarmPopupYn(), db);
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_ENV_SET_LOGINACCESS_ONOFF_SUCCESS,
						newEnv.getSyslogServerAddress().toString());
			}

			if (oldEnv.getSmsActionYn().equals(newEnv.getSmsActionYn()) == false
					|| oldEnv.getSmsActionType().equals(newEnv.getSmsActionType()) == false
					|| oldEnv.getSmsHPNumbers().equals(newEnv.getSmsHPNumbers()) == false) {
				setEnvSMSConfig(newEnv.getSmsActionYn(), newEnv.getSmsActionType(), newEnv.getSmsHPNumbers(), db);
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_ENV_SET_LOGINACCESS_ONOFF_SUCCESS,
						newEnv.getSyslogServerAddress().toString());
			}

			if (oldEnv.getServiceResponseTime().equals(newEnv.getServiceResponseTime()) == false) {
				updateAdditionalConfigSvcRespTimeSave(newEnv.getServiceResponseTime(), db);
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_ENV_SET_SVCRESPTIME_ONOFF_SUCCESS,
						newEnv.getServiceResponseTime().toString());
			}

			if (oldEnv.getRespTimeSection().equals(newEnv.getRespTimeSection()) == false) {
				updateAdditionalConfigRespTimeSection(newEnv.getRespTimeSection(), newEnv.getRespTimeInterval(), db);
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_ENV_SET_SVCRESPTIME_ONOFF_SUCCESS,
						newEnv.getServiceResponseTime().toString());
			} else {
				if (oldEnv.getRespTimeInterval().equals(newEnv.getRespTimeInterval()) == false) {
					updateAdditionalConfigRespTimeSection(newEnv.getRespTimeSection(), newEnv.getRespTimeInterval(),
							db);
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_SVCRESPTIME_ONOFF_SUCCESS,
							newEnv.getServiceResponseTime().toString());
				}
			}

			setEnvLastUpdateTime(db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private void updateAdditionalConfigRespTimeSection(Integer respTimeSection, Integer respTimeInterval, OBDatabase db)
			throws OBException {
		String sqlText = "";
		try {
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());

			if (respTimeSection == 1) {
				sqlText = String.format(" UPDATE MNG_ENV_ADDITIONAL "
						+ " SET OCCUR_TIME = %s, RESP_TIME_SECTION = %d , RESP_TIME_INTERVAL = %d"
						+ " WHERE INDEX = 1;", OBParser.sqlString(now), respTimeSection, respTimeInterval);
			} else {
				sqlText = String.format(" UPDATE MNG_ENV_ADDITIONAL " + " SET OCCUR_TIME = %s, RESP_TIME_SECTION = %d "
						+ " WHERE INDEX = 1;", OBParser.sqlString(now), respTimeSection);
			}

			db.execute(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

	}

	@Override
	public void setNetworkConfig(OBDtoSystemEnvNetwork newEnv, OBDtoExtraInfo extraInfo) throws OBException {
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			OBDtoSystemEnvNetwork oldEnv = getNetworkConfig();
			if (oldEnv.getIpAddress() != null && newEnv.getIpAddress() != null) {
				if (oldEnv.getIpAddress().compareToIgnoreCase(newEnv.getIpAddress()) != 0) {
//					updateNetworkConfigIPAddress(newEnv.getIpAddress(), newEnv.getNetmask(), db);
					OBNetwork.setIPAddress(newEnv.getIpAddress(), newEnv.getNetmask());
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_IP_SUCCESS, newEnv.getIpAddress(), newEnv.getNetmask());
				}
			}
			if (oldEnv.getGateway() != null && newEnv.getGateway() != null) {
				if (oldEnv.getGateway().compareToIgnoreCase(newEnv.getGateway()) != 0) {
//					updateNetworkConfigGateway(newEnv.getGateway(), db);
//					OBNetwork.delDefaultGateway(oldEnv.getGateway());
					OBNetwork.setDefaultGateway(newEnv.getGateway());
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_ENV_SET_GATEWAY_SUCCESS, newEnv.getGateway());
				}
			}
//			if(oldEnv.getHostName()!=null && newEnv.getHostName()!=null)
//			{
//				if(oldEnv.getHostName().compareToIgnoreCase(newEnv.getHostName())!=0)
//				{
//					updateNetworkConfigHostname(newEnv.getHostName(), db);
//					OBNetwork.setHostName(newEnv.getHostName());
//					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(), OBSystemAudit.AUDIT_ENV_SET_HOSTNAME_SUCCESS, newEnv.getHostName(), db);
//				}
//			}
			setEnvLastUpdateTime(db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public Timestamp getEnvLastUpdateTime() throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "start.");
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format("SELECT OCCUR_TIME FROM MNG_TIME WHERE TYPE = %s;",
					OBParser.sqlString(OBDefine.MNG_TIME_TYPE_ENV));

			ResultSet rs;

			rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return new Timestamp(0L);
			}
			Timestamp result = db.getTimestamp(rs, "OCCUR_TIME");
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private void setEnvLastUpdateTime(OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format("SELECT OCCUR_TIME FROM MNG_TIME WHERE TYPE = %s;",
					OBParser.sqlString(OBDefine.MNG_TIME_TYPE_ENV));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false) {// insert
				sqlText = String.format("INSERT INTO MNG_TIME " + "(TYPE, OCCUR_TIME) " + "VALUES (%s, %s); ",
						OBParser.sqlString(OBDefine.MNG_TIME_TYPE_ENV), OBParser.sqlString(OBDateTime.now()));
				db.executeUpdate(sqlText);
			} else {// update
				sqlText = String.format("UPDATE MNG_TIME " + "SET " + "OCCUR_TIME=%s " + "WHERE TYPE=%s;",
						OBParser.sqlString(OBDateTime.now()), OBParser.sqlString(OBDefine.MNG_TIME_TYPE_ENV));
				db.executeUpdate(sqlText);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			new OBEnvManagementImpl().loadNetworkConfig();
////			System.out.println(config);
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//			return;
//		}
//	}
//	
	public void loadNetworkConfig() throws OBException {
		try {
			OBDtoSystemEnvNetwork env = getNetworkConfig();
			OBNetwork.setIPAddress(env.getIpAddress(), env.getNetmask());
			OBNetwork.setDefaultGateway(env.getGateway());
			OBNetwork.setHostName(env.getHostName());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//
//		} catch (Exception e)
//		{
//			e.printStackTrace();
//			return;
//		}
//	}

	@Override
	public void setScheduleBackupInfo(OBDtoScheduleBackupInfo backupInfo, OBDtoExtraInfo extraInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. backupInfo:%s, extraInfo:%s", backupInfo, extraInfo));
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			setScheduleBackupInfo(backupInfo, db);
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_ENV_SET_SCHBACKUP_SUCCESS);
		} catch (OBException e) {
//			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(), OBSystemAudit.AUDIT_ENV_SET_SCHBACKUP_FAIL, db);
			throw e;
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	private void setScheduleBackupInfo(OBDtoScheduleBackupInfo backupInfo, OBDatabase db) throws OBException {
		String sqlText = "";
		String sqlSub = "";
		try {
			if (backupInfo.getUseYN() != null) {
				if (!sqlSub.isEmpty())
					sqlSub += ", ";
				sqlSub += String.format("SCHBACKUP_USEYN = %d ", backupInfo.getUseYN());
			}

			if (backupInfo.getIntervalDay() != null) {
				if (!sqlSub.isEmpty())
					sqlSub += ", ";
				sqlSub += String.format("SCHBACKUP_INTERVALDAY = %d ", backupInfo.getIntervalDay());
			}

			if (backupInfo.getBackupTime() != null) {
				if (!sqlSub.isEmpty())
					sqlSub += ", ";
				sqlSub += String.format("SCHBACKUP_BACKUPTIME = %d ", backupInfo.getBackupTime());
			}

			if (backupInfo.getStartTime() != null) {
				if (!sqlSub.isEmpty())
					sqlSub += ", ";
				sqlSub += String.format("SCHBACKUP_STARTTIME = %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(backupInfo.getStartTime().getTime()))));
			}

			if (!sqlSub.isEmpty())
				sqlSub += ", ";
			if (backupInfo.getEndTime() != null) {
				sqlSub += String.format("SCHBACKUP_ENDTIME = %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(backupInfo.getEndTime().getTime()))));
			} else {
				sqlSub += String.format("SCHBACKUP_ENDTIME = null ");
			}

			if (backupInfo.getOption() != null) {
				if (!sqlSub.isEmpty())
					sqlSub += ", ";
				sqlSub += String.format("SCHBACKUP_OPTION = %d ", backupInfo.getOption());
			}

			if (backupInfo.getLogDelYN() != null) {
				if (!sqlSub.isEmpty())
					sqlSub += ", ";
				sqlSub += String.format("SCHBACKUP_LOGDELYN = %d ", backupInfo.getLogDelYN());
			}

			if (backupInfo.getFtpYN() != null) {
				if (!sqlSub.isEmpty())
					sqlSub += ", ";
				sqlSub += String.format("SCHBACKUP_FTPYN = %d ", backupInfo.getFtpYN());
			}

			if (backupInfo.getFtpIPAddress() != null) {
				if (!sqlSub.isEmpty())
					sqlSub += ", ";
				sqlSub += String.format("SCHBACKUP_FPTIPADDRESS = %s ",
						OBParser.sqlString(backupInfo.getFtpIPAddress()));
			}

			if (backupInfo.getFtpID() != null) {
				if (!sqlSub.isEmpty())
					sqlSub += ", ";
				sqlSub += String.format("SCHBACKUP_FTPID = %s ", OBParser.sqlString(backupInfo.getFtpID()));
			}

			if (backupInfo.getFtpPasswd() != null) {
				if (!sqlSub.isEmpty())
					sqlSub += ", ";
				sqlSub += String.format("SCHBACKUP_FTPPASSWD = %s ", OBParser.sqlString(backupInfo.getFtpPasswd()));
			}

			if (backupInfo.getFtpPort() != null) {
				if (!sqlSub.isEmpty())
					sqlSub += ", ";
				sqlSub += String.format("SCHBACKUP_FTPPORT = %d ", backupInfo.getFtpPort());
			}

			sqlText = String.format(" UPDATE MNG_ENV_ADDITIONAL " + " SET %s " + " WHERE INDEX = 1;", sqlSub);
			db.executeUpdate(sqlText);

			OBDtoScheduleBackupInfo info = getScheduleBackupInfo(db);
			updateSchedulBackup(info, db);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDtoScheduleBackupInfo info = new OBEnvManagementImpl().getScheduleBackupInfo();
//			System.out.println(info);
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//			return;
//		}
//	}
//	
	public OBDtoScheduleBackupInfo getScheduleBackupInfo() throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start"));

		OBDatabase db = new OBDatabase();

		OBDtoScheduleBackupInfo result;
		;
		try {
			db.openDB();

			result = getScheduleBackupInfo(db);
		} catch (OBException e) {
			throw e;
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));

		return result;
	}

	public OBDtoScheduleBackupInfo getScheduleBackupInfo(OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start."));

		String sqlText = "";
		OBDtoScheduleBackupInfo result = new OBDtoScheduleBackupInfo();
		try {
			sqlText = String.format(" SELECT SCHBACKUP_USEYN, SCHBACKUP_INTERVALDAY, SCHBACKUP_BACKUPTIME, "
					+ "     SCHBACKUP_STARTTIME, SCHBACKUP_ENDTIME, SCHBACKUP_OPTION, SCHBACKUP_LOGDELYN, "
					+ "     SCHBACKUP_FTPYN, SCHBACKUP_FPTIPADDRESS, SCHBACKUP_FTPID, SCHBACKUP_FTPPASSWD, SCHBACKUP_FTPPORT "
					+ " FROM MNG_ENV_ADDITIONAL " + " WHERE INDEX=1 ; ");
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return null;
			}
			result.setUseYN(db.getInteger(rs, "SCHBACKUP_USEYN"));
			result.setIntervalDay(db.getInteger(rs, "SCHBACKUP_INTERVALDAY"));
			result.setBackupTime(db.getInteger(rs, "SCHBACKUP_BACKUPTIME"));
			result.setStartTime(db.getTimestamp(rs, "SCHBACKUP_STARTTIME"));
			result.setEndTime(db.getTimestamp(rs, "SCHBACKUP_ENDTIME"));
			result.setOption(db.getInteger(rs, "SCHBACKUP_OPTION"));
			result.setLogDelYN(db.getInteger(rs, "SCHBACKUP_LOGDELYN"));
			result.setFtpYN(db.getInteger(rs, "SCHBACKUP_FTPYN"));
			result.setFtpIPAddress(db.getString(rs, "SCHBACKUP_FPTIPADDRESS"));
			result.setFtpID(db.getString(rs, "SCHBACKUP_FTPID"));
			result.setFtpPasswd(db.getString(rs, "SCHBACKUP_FTPPASSWD"));
			result.setFtpPort(db.getInteger(rs, "SCHBACKUP_FTPPORT"));

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));

			return result;
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
	}

	public void updateSchedulBackup(OBDtoScheduleBackupInfo info, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. backupInfo:%s", info));
		// 먼저 등록된 항목이 있는지 검사한 후 있으면 업데이트 하고, 없으면 추가한다.
		String sqlText = "";
		Timestamp nowTime = OBDateTime.toTimestamp(OBDateTime.now());
		try {
			sqlText = String.format("SELECT INDEX, TYPE FROM MNG_SCHEDULE WHERE TYPE=%d; ",
					OBDtoSchedule.TYPE_SCHBACKUP);

			ResultSet rs;
			rs = db.executeQuery(sqlText);

			if (rs.next() == true) {// 항목을 업데이트.
				if (info.getUseYN() != OBDtoScheduleBackupInfo.USE_YES) {// schedule을 삭제해야 한다.
					delSchedule(db.getInteger(rs, "INDEX"), db);
				} else {
					OBDtoSchedule sch = new OBDtoSchedule();
					// 발생 날짜 지정.
					// 발생 날짜 지정.
					int backupHour = info.getBackupTime();
					int nowHour = OBDateTime.getHourOfDay(new Timestamp(nowTime.getTime()));
					Timestamp occurTime;
					if (backupHour <= nowHour)
						occurTime = new Timestamp(nowTime.getTime() + info.getIntervalDay() * 24 * 60 * 60 * 1000);
					else
						occurTime = new Timestamp(nowTime.getTime());
					sch.setDay(OBDateTime.getDayOfMonth(occurTime));
					sch.setHour(OBDateTime.getHourOfDay(occurTime));
					sch.setAdcIndex(0);
					sch.setIsCycle(1);
					sch.setIndex(db.getInteger(rs, "INDEX"));
					sch.setType(db.getInteger(rs, "TYPE"));
					updateSchedule(sch, db);
				}
			} else {// 추가한다.
				OBDtoSchedule sch = new OBDtoSchedule();
				// 발생 날짜 지정.
				int backupHour = info.getBackupTime();
				int nowHour = OBDateTime.getHourOfDay(new Timestamp(nowTime.getTime()));
				Timestamp occurTime;
				if (backupHour <= nowHour)
					occurTime = new Timestamp(nowTime.getTime() + info.getIntervalDay() * 24 * 60 * 60 * 1000);
				else
					occurTime = new Timestamp(nowTime.getTime());

				sch.setDay(OBDateTime.getDayOfMonth(occurTime));
				sch.setHour(OBDateTime.getHourOfDay(occurTime));
				sch.setAdcIndex(0);
				sch.setIsCycle(1);
				sch.setType(OBDtoSchedule.TYPE_SCHBACKUP);
				addSchedule(sch, db);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	private void updateAdditionalConfigSyslogServerAddress(String ipAddress, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(
					" UPDATE MNG_ENV_ADDITIONAL " + " SET OCCUR_TIME = %s, SYSLOG_SERVER = %s " + " WHERE INDEX = 1;",
					OBParser.sqlString(now), OBParser.sqlString(ipAddress));
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private void updateAdditionalConfigSnmpTrapAddress(OBDtoSnmpTrap snmpTrap, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(" UPDATE MNG_ENV_ADDITIONAL "
					+ " SET OCCUR_TIME = %s, SNMPTRAP_SERVER = %s, SNMPTRAP_PORT = %d, SNMPTRAP_COMMUNITY = %s, SNMPTRAP_VERSION = %d "
					+ " WHERE INDEX = 1;", OBParser.sqlString(now),
					OBParser.sqlString(snmpTrap.getSnmpTrapServerAddress()), snmpTrap.getSnmpTrapPort(),
					OBParser.sqlString(snmpTrap.getSnmpTrapCommunity()), snmpTrap.getSnmpTrapVersion());
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private void updateAdditionalConfigSnmpCommunity(OBDtoSystemSnmpInfo snmpCommunity, OBDatabase db)
			throws OBException {
		String sqlText = "";
		try {
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(
					" UPDATE MNG_ENV_ADDITIONAL                     "
							+ " SET OCCUR_TIME = %s, SNMP_COMMUNITY = %s, SNMP_ACCESSTYPE = %s, "
							+ " SNMPV3_USERID = %s, SNMPV3_AUTHPASSWORD = %s, "
							+ " SNMPV3_PRIVPASSWORD = %s, SNMPV3_ALGORITHM = %s " + " WHERE INDEX = 1;",
					OBParser.sqlString(now), OBParser.sqlString(snmpCommunity.getCommunity()),
					OBParser.sqlString(snmpCommunity.getAccessType()), OBParser.sqlString(snmpCommunity.getUserId()),
					OBParser.sqlString(snmpCommunity.getAuthPassword()),
					OBParser.sqlString(snmpCommunity.getPrivPassword()),
					OBParser.sqlString(snmpCommunity.getAlgorithm()));
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private void updateAdditionalConfigAlteonAutoSave(Integer flag, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(" UPDATE MNG_ENV_ADDITIONAL " + " SET OCCUR_TIME = %s, ALTEON_AUTO_SAVE = %d "
					+ " WHERE INDEX = 1;", OBParser.sqlString(now), flag);
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public void updateAdditionalConfigSvcRespTimeSave(Integer flag, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			sqlText = String.format(" UPDATE MNG_ENV_ADDITIONAL " + " SET OCCUR_TIME = %s, SERVICE_RESPONSE_TIME = %d "
					+ " WHERE INDEX = 1;", OBParser.sqlString(now), flag);
			db.execute(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public Integer getEnvLoginAccess() throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start"));

		OBDatabase db = new OBDatabase();

		Integer retValue = 0;
		try {
			db.openDB();

			retValue = getEnvLoginAccess(db);
		} catch (OBException e) {
			throw e;
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", retValue));

		return retValue;
	}

	@Override
	public Integer getEnvAlarmPopup() throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start"));

		OBDatabase db = new OBDatabase();

		Integer retValue = 0;
		try {
			db.openDB();

			retValue = getEnvAlarmPopupYn(db);
		} catch (OBException e) {
			throw e;
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", retValue));

		return retValue;
	}

	// 시스템에 적용되어 있는 hostname을 읽어온다. /proc/sys/kernel/hostname에 있음
	public String getKernelHostName() {
		String line = "";

		// config file 처리용 objects
		FileInputStream fstream = null;
		DataInputStream in = null;
		BufferedReader reader = null;

		try {
			fstream = new FileInputStream(PROC_SYS_KERNEL_HOSTNAME_FILE);
		} catch (Exception e) // file not found exception
		{
			OBSystemLog.error(OBDefine.LOGFILE_DEBUG,
					String.format("hostname file \"%s\" not found.", PROC_SYS_KERNEL_HOSTNAME_FILE));
			return "-";
		}
		try {
			in = new DataInputStream(fstream);
			reader = new BufferedReader(new InputStreamReader(in));

			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.length() > 0) {
					break;
				}
			}
		} catch (Exception e) {
			OBSystemLog.warn(OBDefine.LOGFILE_DEBUG,
					String.format("hostname read error in file:\"%s\"", PROC_SYS_KERNEL_HOSTNAME_FILE));
		} finally {
			try {
				reader.close();
				in.close();
				fstream.close();
			} catch (Exception e) {
			}
		}
		return line;
	}

	public void setSnmpConfig(OBDtoSystemSnmpInfo newEnv) throws OBException {
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "ADCsmart snmp setting.");
		String file = "/opt/etc/snmpd.conf";
		String cmnd = "";
		String snmpConfig = newEnv.getAccessType() + " " + newEnv.getCommunity() + " " + "default";
		try {
			if (OBCommon.isLockFileExist(file) == false) {
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
						String.format("ADCsmart snmp file not exist. location : %s", file));
				return;
			}
			String[] cmdline = { "sh", "-c", String.format("sed -i \"61s/.*/%s/g\" /opt/etc/snmpd.conf", snmpConfig) };
			runSystemCommand(cmdline);
			cmnd = " /etc/init.d/snmpd restart";
			new OBNetwork().runSystemCommand(cmnd);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("snmpd restart done"));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "ADCsmart snmp setting end.");
	}

	public String runSystemCommand(String[] cmnd) throws OBException {
		try {
//            Process p=Runtime.getRuntime().exec(cmnd); 
//            p.waitFor(); 
//            BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream()));
			String result = "";
//            String line=reader.readLine(); 
//            while(line!=null) 
//            { 
//                line=reader.readLine(); 
//                result+=line;
//            } 
			return result;
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
	}

	public String runDumpConfig() throws OBException {
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "ADCsmart sysviewdump start.");
		try {
			String deleteDumpCommand = " rm -rf /var/sysrpt.tgz";
			new OBNetwork().runSystemCommand(deleteDumpCommand);

			String createDumpCommand = " /opt/adcsmart/scripts/sysreport.sh sysrpt";
			new OBNetwork().runSystemCommand(createDumpCommand);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("ADCsmart sysviewdump done"));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "ADCsmart sysviewdump setting end.");

//        return result;
		return "test";
	}

	public void setSnmpV3Config(OBDtoSystemSnmpInfo newEnv) throws OBException {
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "ADCsmart snmpV3 setting.");
		String cmnd = "";
		try {

			cmnd = " /etc/init.d/snmpd stop";
			new OBNetwork().runSystemCommand(cmnd);
			OBDateTime.Sleep(100);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("snmpd stop done"));

			cmnd = " sed -e '/usmUser/d' /var/net-snmp/snmpd.conf > /var/net-snmp/snmpd.conf.bak";
			new OBNetwork().runSystemCommand(cmnd);

			OBDateTime.Sleep(100);

			cmnd = " cp -rf /var/net-snmp/snmpd.conf.bak /var/net-snmp/snmpd.conf";
			new OBNetwork().runSystemCommand(cmnd);

			OBDateTime.Sleep(100);

			try {
				cmnd = String.format(" /usr/bin/net-snmp-config --create-snmpv3-user -a %s %s",
						newEnv.getAuthPassword(), newEnv.getUserId());

				new OBNetwork().runSystemCommand(cmnd);
			} catch (Exception e) {
				OBSystemLog.warn(String.format("Failed to run command. error: %s", cmnd, e.getMessage()));
			}
			OBDateTime.Sleep(100);

			cmnd = " /etc/init.d/snmpd start";
			new OBNetwork().runSystemCommand(cmnd);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("snmpd start done"));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "ADCsmart snmp setting end.");
	}
}