package kr.openbase.adcsmart.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.openbase.adcsmart.service.OBAdcManagement;
import kr.openbase.adcsmart.service.OBAdcVServer;
import kr.openbase.adcsmart.service.OBSystemAudit;
import kr.openbase.adcsmart.service.adcmond.OBAdcMonitorDB;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBAdcCfgInfo;
import kr.openbase.adcsmart.service.dto.OBAdcCheckResult;
import kr.openbase.adcsmart.service.dto.OBAdcConfigInfo;
import kr.openbase.adcsmart.service.dto.OBDtoADCGroupInfo;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAccount;
import kr.openbase.adcsmart.service.dto.OBDtoAdcGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcLogSearchOption;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNoticeGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSchedule;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVSInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.dto.OBDtoAuditLogAdcConfig;
import kr.openbase.adcsmart.service.dto.OBDtoAuditLogAdcSystem;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoLastAdcCheckTime;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoRespGroup;
import kr.openbase.adcsmart.service.dto.OBDtoRespInfo;
import kr.openbase.adcsmart.service.dto.OBDtoRespMultiChartData;
import kr.openbase.adcsmart.service.dto.OBDtoSLBUpdateStatus;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoSlbUser;
import kr.openbase.adcsmart.service.dto.OBDtoVSGroupInfo;
import kr.openbase.adcsmart.service.dto.OBDtoVrrpInfo;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigChunkAlteon;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigChunkF5;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigVServerAlteon;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigVServerF5;
import kr.openbase.adcsmart.service.impl.alteon.OBAdcSystemInfoAlteon;
import kr.openbase.adcsmart.service.impl.alteon.OBAdcVServerAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBAdcAlteonHandler;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoAdcTimeAlteon;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcSystemInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoProcessLockSpin;
import kr.openbase.adcsmart.service.impl.f5.CommonF5;
import kr.openbase.adcsmart.service.impl.f5.OBAdcSystemInfoF5;
import kr.openbase.adcsmart.service.impl.f5.OBAdcVServerF5;
import kr.openbase.adcsmart.service.impl.f5.SystemF5;
import kr.openbase.adcsmart.service.impl.f5.handler.OBAdcF5Handler;
import kr.openbase.adcsmart.service.impl.pas.OBAdcSystemInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.OBAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.OBAdcPASHandler;
import kr.openbase.adcsmart.service.impl.pask.OBAdcSystemInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.OBAdcVServerPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.OBAdcPASKHandler;
import kr.openbase.adcsmart.service.snmp.alteon.OBSnmpAlteon;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoSnmpVrrpAlteon;
import kr.openbase.adcsmart.service.snmp.pas.OBSnmpPAS;
import kr.openbase.adcsmart.service.snmp.pask.OBSnmpPASK;
import kr.openbase.adcsmart.service.utility.OBCipherAES;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBLicenseExpiredException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBAdcManagementImpl implements OBAdcManagement {
	// File(System.getProperty("java.io.tmpdir") + File.separator + "locks");
//	private File lockTempDir = new File(System.getProperty("java.home"));
	private String lockTempDir = "/opt/adcsmart/lock";

	// private File lockTempDir = new File("/tmp/locks");
	// private FileLock fileLock = null;
	// private FileChannel fileChannel = null;
	// private File file = null;

	// public static void main(String[] args)
	// {
	// // try
	// {
	// OBAdcManagementImpl adc_manager = new OBAdcManagementImpl();
	//
	// //ArrayList<OBDtoAdcInfo> adc = adc_manager.getAdcInfoList();
	//
	// //System.out.println(adc);
	//
	// // ArrayList<OBDtoAccount> acc = adc_manager.getAccountListAll();
	// // ArrayList<String> accnt = new ArrayList<String>(0);
	// // accnt.add(acc.get(0).getAccountName());
	// // adc_manager.addAdcBasicInfo(adc.get(0), accnt);
	// // OBDtoAdcInfo adcInfo = new OBDtoAdcInfo();
	// // adcInfo.setAdcAccount("admin");
	// // adcInfo.setAdcPassword("admin");
	// // adcInfo.setAdcType(OBDefine.ADC_TYPE_ALTEON);
	// // adcInfo.setDescription("192.168.100/1.11");
	// // adcInfo.setAdcIpAddress("192.168.100.11");
	// // adcInfo.setName("alteon_11");
	// // adcInfo.setGroupIndex(1);
	// // adcInfo.setAdcType(OBDefine.ADC_TYPE_ALTEON);
	// //
	// // ArrayList<Integer> accountIndexList=new ArrayList<Integer>();
	// // accountIndexList.add(1);
	// // adcInfo.setAccountIndexList(accountIndexList);
	// //
	// // OBDtoAdcInfo adcInfo2 = new OBDtoAdcInfo();
	// // adcInfo2.setAdcAccount("admin");
	// // adcInfo2.setAdcPassword("admin");
	// // adcInfo2.setAdcType(OBDefine.ADC_TYPE_ALTEON);
	// // adcInfo2.setDescription("192.168.100/1.11");
	// // adcInfo2.setAdcIpAddress("192.168.101.11");
	// // adcInfo2.setName("alteon_11");
	// // adcInfo2.setGroupIndex(1);
	// // adcInfo2.setAdcType(OBDefine.ADC_TYPE_ALTEON);
	// //
	// // ArrayList<Integer> accountIndexList2=new ArrayList<Integer>();
	// // accountIndexList2.add(1);
	// // adcInfo2.setAccountIndexList(accountIndexList2);
	// // adc_manager.addAdcInfo(adcInfo, adcInfo2);
	//
	// adc_manager.getTrivialAccountList(1, null);
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	public OBDtoAdcTimeAlteon getAdcTimeAlteon(int adcIndex, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));

		OBDtoAdcTimeAlteon result = new OBDtoAdcTimeAlteon();

		String sqlText = "";
		try {
			sqlText = String.format(
					" SELECT APPLY_TIME, SAVE_TIME, LAST_BOOT_TIME FROM MNG_ADC WHERE INDEX=%d LIMIT 1; ", adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				result.setApplyTime(db.getTimestamp(rs, "APPLY_TIME"));
				result.setSaveTime(db.getTimestamp(rs, "SAVE_TIME"));
				result.setBootTime(db.getTimestamp(rs, "LAST_BOOT_TIME"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public OBDtoAdcTimeAlteon getAdcTimeAlteon(int adcIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));

		OBDtoAdcTimeAlteon result = new OBDtoAdcTimeAlteon();

		String sqlText = "";

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT APPLY_TIME, SAVE_TIME, LAST_BOOT_TIME FROM MNG_ADC WHERE INDEX=%d LIMIT 1; ", adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				result.setApplyTime(db.getTimestamp(rs, "APPLY_TIME"));
				result.setSaveTime(db.getTimestamp(rs, "SAVE_TIME"));
				result.setBootTime(db.getTimestamp(rs, "LAST_BOOT_TIME"));
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	@Override
	public ArrayList<OBDtoAdcInfo> getAdcInfoList(Integer accountIndex) throws OBException {
		// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start.
		// accountIndex:%d", accountIndex));

		ArrayList<OBDtoAdcInfo> list;
		try {
			list = getAdcInfoList(accountIndex, OBDefine.ORDER_TYPE_NAME, OBDefine.ORDER_DIR_ASCEND);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
		// list));
		return list;
	}

	public ArrayList<OBDtoAdcInfo> getAdcInfoList(Integer accountIndex, Integer orderType, Integer orderDir)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. accountIndex:%d", accountIndex));

		ArrayList<OBDtoAdcInfo> list;
		try {
			list = getAdcInfoListCore(accountIndex, null, orderType, orderDir);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));
		return list;
	}

	@Override
	public ArrayList<OBDtoAdcInfo> searchAdcInfoList(Integer accountIndex, String searchKey) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. accountIndex:%d, searchKey:%s", accountIndex, searchKey));

		ArrayList<OBDtoAdcInfo> list;
		try {
			list = getAdcInfoListCore(accountIndex, searchKey, OBDefine.ORDER_TYPE_NAME, OBDefine.ORDER_DIR_ASCEND);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));
		return list;
	}

	public ArrayList<OBDtoAdcInfo> getAdcIPAddressList() throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start"));

		ArrayList<OBDtoAdcInfo> list = new ArrayList<OBDtoAdcInfo>();

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT NAME, INDEX, IPADDRESS, TYPE, SYSLOG_IP FROM MNG_ADC WHERE AVAILABLE=%d ",
					OBDefine.ADC_STATE.AVAILABLE);

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcInfo info = new OBDtoAdcInfo();
				info.setIndex(db.getInteger(rs, "INDEX"));
				info.setAdcIpAddress(db.getString(rs, "IPADDRESS"));
				info.setAdcType(db.getInteger(rs, "TYPE"));
				info.setName(db.getString(rs, "NAME"));
				info.setSyslogIpAddress(db.getString(rs, "SYSLOG_IP"));
				list.add(info);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));
		return list;
	}

	public ArrayList<OBDtoAdcInfo> getBasicAdcInfoList() throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start"));

		OBDatabase db = new OBDatabase();

		String sqlText = "";

		ResultSet rs;

		ArrayList<OBDtoAdcInfo> list = new ArrayList<OBDtoAdcInfo>();
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT INDEX, NAME, IPADDRESS, ACCNT, PASSWORD, CLI_ACCNT, CLI_PASSWORD, MODEL, TYPE, GROUP_INDEX,                        \n"
							+ " DESCRIPTION, ACTIVE_PAIR_INDEX, MGMT_MODE, SSO_MODE, CONN_PROTOCOL,                                                     \n"
							+ " SW_VERSION, HOST_NAME, APPLY_TIME, SAVE_TIME, LAST_BOOT_TIME, STATUS, PEER_IPADDRESS, CONN_SERVICE, CONN_PORT,          \n"
							+ " SNMP_RCOMM, SNMP_VERSION, SNMP_USER, SNMP_AUTH_PASSWORD, SNMP_PRIV_PASSWORD, SNMP_AUTH_PROTOCOL, SNMP_PRIV_PROTOCOL     \n"
							+ " FROM MNG_ADC ");
			sqlText += String.format(" WHERE AVAILABLE = %d ORDER BY NAME;", OBDefine.ADC_STATE.AVAILABLE);

			rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcInfo info = new OBDtoAdcInfo();

				info.setIndex(db.getInteger(rs, "INDEX"));
				info.setName(db.getString(rs, "NAME"));
				info.setAdcIpAddress(db.getString(rs, "IPADDRESS"));
				info.setAdcAccount(db.getString(rs, "ACCNT"));
				info.setAdcPassword(db.getString(rs, "PASSWORD"));
				info.setAdcCliAccount(db.getString(rs, "CLI_ACCNT"));
				info.setAdcCliPassword(db.getString(rs, "CLI_PASSWORD"));
				info.setModel(db.getString(rs, "MODEL"));
				info.setAdcType(db.getInteger(rs, "TYPE"));
				info.setGroupIndex(db.getInteger(rs, "GROUP_INDEX"));
				info.setDescription(db.getString(rs, "DESCRIPTION"));
				info.setActivePairIndex(db.getInteger(rs, "ACTIVE_PAIR_INDEX"));
				info.setSwVersion(db.getString(rs, "SW_VERSION"));
				info.setHostName(db.getString(rs, "HOST_NAME"));
				info.setApplyTime(db.getTimestamp(rs, "APPLY_TIME"));
				info.setSaveTime(db.getTimestamp(rs, "SAVE_TIME"));
				info.setLastBootTime(db.getTimestamp(rs, "LAST_BOOT_TIME"));
				info.setStatus(db.getInteger(rs, "STATUS"));
				info.setSnmpRComm(db.getString(rs, "SNMP_RCOMM"));
				info.setPeerAdcIPAddress(db.getString(rs, "PEER_IPADDRESS"));
				info.setConnService(db.getInteger(rs, "CONN_SERVICE"));
				info.setConnPort(db.getInteger(rs, "CONN_PORT"));
				info.setConnProtocol(db.getInteger(rs, "CONN_PROTOCOL"));
				info.setMgmtMode(db.getInteger(rs, "MGMT_MODE"));
				info.setSsoMode(db.getInteger(rs, "SSO_MODE"));

				OBDtoAdcSnmpInfo snmpInfo = new OBDtoAdcSnmpInfo();
				snmpInfo.setVersion(db.getInteger(rs, "SNMP_VERSION"));
				snmpInfo.setRcomm(db.getString(rs, "SNMP_RCOMM"));
				if (snmpInfo.getRcomm() == null || snmpInfo.getRcomm().isEmpty()) {
					snmpInfo.setRcomm(OBDefine.DEFAULT_SNMP_RCOMM);
				}
				snmpInfo.setSecurityName(db.getString(rs, "SNMP_USER"));
				snmpInfo.setAuthProto(db.getString(rs, "SNMP_AUTH_PROTOCOL"));
				snmpInfo.setAuthPassword(db.getString(rs, "SNMP_AUTH_PASSWORD"));
				snmpInfo.setPrivProto(db.getString(rs, "SNMP_PRIV_PROTOCOL"));
				snmpInfo.setPrivPassword(db.getString(rs, "SNMP_PRIV_PASSWORD"));
				info.setSnmpInfo(snmpInfo);

				list.add(info);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));
		return list;
	}

	private String getAdcInfoListCoreOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY A.NAME ASC NULLS LAST ";
		switch (orderType) {
		case OBDefine.ORDER_TYPE_ADCNAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY A.NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY A.NAME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_ADCSTATUS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY A.STATUS ASC NULLS LAST , A.NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY A.STATUS DESC NULLS LAST , A.NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_ADCIPADDRESS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INET(A.IPADDRESS) ASC NULLS LAST , A.NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY INET(A.IPADDRESS) DESC NULLS LAST , A.NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_OCCURTIME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY A.APPLY_TIME ASC NULLS LAST , A.NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY A.APPLY_TIME DESC NULLS LAST , A.NAME ASC NULLS LAST ";
			break;
		}
		return retVal;
	}

	private ArrayList<OBDtoAdcInfo> getAdcInfoListCore(Integer accountIndex, String searchKey, Integer orderType,
			Integer orderDir) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. accountIndex:%d, searchKey:%s", accountIndex, searchKey));

		String sqlText = "";

		ResultSet rs, rs2;
		String wildcardKey;

		int roleNo = 0;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (accountIndex == null || accountIndex.equals(0)) // 비공식 루트, 문서상 규정과 다르게 데몬은 accountIndex null로 호출 할 수 있다.
			{
				roleNo = OBDefine.ACCNT_ROLE_ADMIN;
			} else {

				sqlText = String.format("SELECT ROLE_NO FROM MNG_ACCNT WHERE INDEX = %d AND AVAILABLE = %d ; ",
						accountIndex, OBDefine.DATA_AVAILABLE);

				rs = db.executeQuery(sqlText);
				if (rs.next() == false) {
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String
							.format("Error: account has no role. accountInex=%d (sqlText=%s)", accountIndex, sqlText));
				}

				roleNo = db.getInteger(rs, "ROLE_NO");
			}

			sqlText = String.format(
					" SELECT A.INDEX, A.NAME, A.IPADDRESS, A.ACCNT, A.PASSWORD, A.TYPE, A.MODEL, A.MGMT_MODE, A.SSO_MODE,  \n"
							+ " A.GROUP_INDEX, A.DESCRIPTION, A.ACTIVE_PAIR_INDEX, A.PEER_IPADDRESS, A.CONN_SERVICE, A.CONN_PORT,  \n"
							+ " A.SW_VERSION, A.HOST_NAME, A.APPLY_TIME, A.SAVE_TIME, A.LAST_BOOT_TIME, A.STATUS, A.PURCHASE_TIME, \n"
							+ " A.CLI_ACCNT, A.CLI_PASSWORD, COALESCE(B.ACTIVE_BACKUP_STATE, 0) AS ACTIVE_BACKUP_STATE, A.ROLE,    \n"
							+ " A.SNMP_RCOMM, A.SNMP_VERSION, A.SNMP_USER, A.SNMP_AUTH_PASSWORD, A.SNMP_PRIV_PASSWORD,             \n"
							+ " A.SNMP_AUTH_PROTOCOL, A.SNMP_PRIV_PROTOCOL, A.CONN_PROTOCOL                                        \n"
							+ " FROM MNG_ADC                 A                                                                     \n"
							+ " LEFT JOIN MNG_ADC_ADDITIONAL B                                                                     \n"
							+ " ON B.ADC_INDEX = A.INDEX                                                                           \n");

			if (roleNo == OBDefine.ACCNT_ROLE_ADMIN) {
				if (searchKey == null || searchKey.isEmpty() == true) // 비검색, 계정 기반 전체 조회
				{
					sqlText += String.format(" WHERE A.AVAILABLE = %d ", OBDefine.ADC_STATE.AVAILABLE);
				} else
				// 검색쪽에서 들어온 경우
				{
					wildcardKey = "%" + searchKey + "%";
					sqlText += String.format(
							" WHERE (A.NAME LIKE %s OR A.IPADDRESS LIKE %s OR A.SW_VERSION LIKE %s OR A.DESCRIPTION LIKE %s) AND A.AVAILABLE = %d ",
							OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey),
							OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey),
							OBDefine.ADC_STATE.AVAILABLE);
				}
			} else {
				if (searchKey == null || searchKey.isEmpty() == true) // 비검색, 계정 기반 전체 조회
				{
					sqlText += String.format(" WHERE "
							+ " A.INDEX IN (SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %s) " + // where-in:empty
																												// string
																												// 불가,
																												// null
																												// 불가,
																												// OK
							" AND A.AVAILABLE = %d ", accountIndex, OBDefine.DATA_AVAILABLE);
				} else
				// 검색쪽에서 들어온 경우
				{
					wildcardKey = "%" + searchKey + "%";
					sqlText += String.format(" WHERE "
							+ " A.INDEX IN (SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %s) " + // where-in:empty
																												// string
																												// 불가,
																												// null
																												// 불가,
																												// OK
							" AND (A.NAME LIKE %s OR A.IPADDRESS LIKE %s) AND A.AVAILABLE = %d ", accountIndex,
							OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey), OBDefine.DATA_AVAILABLE);
				}
			}

			sqlText += getAdcInfoListCoreOrderType(orderType, orderDir);

			ArrayList<OBDtoAdcInfo> list = new ArrayList<OBDtoAdcInfo>();
			String adcIndexList = "-1"; // comma로 구분한 adc index 목록을 만들어 2번째 sql의 in-절에 쓴다. 머리에 query 결과에 영향이 없는 값 1개를
										// 넣으면 comma 붙이기 편하다.

			rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcInfo info = new OBDtoAdcInfo();

				info.setIndex(db.getInteger(rs, "INDEX"));
				info.setName(db.getString(rs, "NAME"));
				info.setAdcIpAddress(db.getString(rs, "IPADDRESS"));
				info.setAdcAccount(db.getString(rs, "ACCNT"));
				info.setAdcPassword(db.getString(rs, "PASSWORD"));
				info.setAdcCliAccount(db.getString(rs, "CLI_ACCNT"));
				info.setAdcCliPassword(db.getString(rs, "CLI_PASSWORD"));
				info.setAdcType(db.getInteger(rs, "TYPE"));
				info.setSnmpRComm(db.getString(rs, "SNMP_RCOMM"));
				info.setModel(db.getString(rs, "MODEL"));
				info.setGroupIndex(db.getInteger(rs, "GROUP_INDEX"));
				info.setDescription(db.getString(rs, "DESCRIPTION"));
				info.setActivePairIndex(db.getInteger(rs, "ACTIVE_PAIR_INDEX"));
				info.setConnService(db.getInteger(rs, "CONN_SERVICE"));
				info.setConnPort(db.getInteger(rs, "CONN_PORT"));
				info.setConnProtocol(db.getInteger(rs, "CONN_PROTOCOL"));
				info.setSwVersion(db.getString(rs, "SW_VERSION"));
				info.setHostName(db.getString(rs, "HOST_NAME"));
				info.setApplyTime(db.getTimestamp(rs, "APPLY_TIME"));
				info.setSaveTime(db.getTimestamp(rs, "SAVE_TIME"));
				info.setLastBootTime(db.getTimestamp(rs, "LAST_BOOT_TIME"));
				info.setStatus(db.getInteger(rs, "STATUS"));
				info.setActiveStandbyState(db.getInteger(rs, "ACTIVE_BACKUP_STATE"));
				info.setRoleFlbYn(getFlbYesNo(db.getInteger(rs, "ROLE")));
				info.setMgmtMode(db.getInteger(rs, "MGMT_MODE"));
				info.setSsoMode(db.getInteger(rs, "SSO_MODE"));

				OBDtoAdcSnmpInfo snmpInfo = new OBDtoAdcSnmpInfo();
				snmpInfo.setVersion(db.getInteger(rs, "SNMP_VERSION"));
				snmpInfo.setRcomm(db.getString(rs, "SNMP_RCOMM"));
				if (snmpInfo.getRcomm() == null || snmpInfo.getRcomm().isEmpty()) {
					snmpInfo.setRcomm(OBDefine.DEFAULT_SNMP_RCOMM);
				}
				snmpInfo.setSecurityName(db.getString(rs, "SNMP_USER"));
				snmpInfo.setAuthProto(db.getString(rs, "SNMP_AUTH_PROTOCOL"));
				snmpInfo.setAuthPassword(db.getString(rs, "SNMP_AUTH_PASSWORD"));
				snmpInfo.setPrivProto(db.getString(rs, "SNMP_PRIV_PROTOCOL"));
				snmpInfo.setPrivPassword(db.getString(rs, "SNMP_PRIV_PASSWORD"));
				info.setSnmpInfo(snmpInfo);

				adcIndexList += ("," + db.getInteger(rs, "INDEX"));
				/*
				 * // 아래와 같이 loop안에서 2차로 query를 순환시키는 것은 성능에 좋지 않다. 한번에 구해서 넣는다. sqlText2 =
				 * String.
				 * format("SELECT ACCNT_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ADC_INDEX = %d;",
				 * info.getIndex()); rs2 = db2.executeQuery(sqlText2);
				 * 
				 * ArrayList<Integer> temp = new ArrayList<Integer>(); while(rs2.next()) {
				 * temp.add(db2.getInteger(rs2, "ACCNT_INDEX")); }
				 * info.setAccountIndexList(temp);
				 */
				info.setAccountIndexList(new ArrayList<Integer>()); // 관련 계정을 넣을 리스트는 모두 만들어 놓는다.
				list.add(info);
			}
			// ADC들에 연결된 계정을 구한다.
			sqlText = String.format(
					"SELECT ADC_INDEX, ACCNT_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ADC_INDEX in (%s) ORDER BY ADC_INDEX ",
					adcIndexList);
			rs2 = db.executeQuery(sqlText);

			Integer adcIndexTemp = 0;
			Integer accountIndexTemp = 0;

			while (rs2.next()) {
				adcIndexTemp = db.getInteger(rs2, "ADC_INDEX");
				accountIndexTemp = db.getInteger(rs2, "ACCNT_INDEX");
				for (OBDtoAdcInfo adc : list) {
					if (adcIndexTemp.equals(adc.getIndex())) {
						adc.getAccountIndexList().add(accountIndexTemp);
						break;
					}
				}
			}
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));
			return list;
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

	public ArrayList<OBDtoAdcInfo> getBasicAdcInfoListForReport(ArrayList<Integer> indexList) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String indexText = "";
			for (Integer index : indexList) {
				if (!indexText.isEmpty())
					indexText += ", ";
				indexText += index;
			}
			sqlText = String.format(
					" SELECT INDEX, NAME, IPADDRESS, MODEL, TYPE, DESCRIPTION FROM MNG_ADC WHERE INDEX IN ( %s )                     \n",
					indexText);

			ArrayList<OBDtoAdcInfo> list = new ArrayList<OBDtoAdcInfo>();

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcInfo info = new OBDtoAdcInfo();

				info.setIndex(db.getInteger(rs, "INDEX"));
				info.setName(db.getString(rs, "NAME"));
				info.setAdcIpAddress(db.getString(rs, "IPADDRESS"));
				info.setModel(db.getString(rs, "MODEL"));
				info.setAdcType(db.getInteger(rs, "TYPE"));
				info.setDescription(db.getString(rs, "DESCRIPTION"));

				list.add(info);
			}
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));
			return list;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}
	}

	@Override
	public OBDtoAdcInfo getAdcInfo(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		String sqlText2 = "";
		ResultSet rs, rs2;

		try {
			db.openDB();

			sqlText = String.format(
					" SELECT A.INDEX, A.NAME, A.IPADDRESS, A.ACCNT, A.PASSWORD, A.CLI_ACCNT, A.CLI_PASSWORD, A.MODEL,     \n"
							+ " A.TYPE, A.GROUP_INDEX, A.DESCRIPTION, A.ACTIVE_PAIR_INDEX, A.SW_VERSION, A.HOST_NAME, A.APPLY_TIME, \n"
							+ " A.SAVE_TIME, A.LAST_BOOT_TIME, A.SNMP_RCOMM, A.STATUS, A.PURCHASE_TIME, A.PEER_IPADDRESS,           \n"
							+ " A.CONN_SERVICE, A.CONN_PORT, A.REGISTER_TIME, A.SYSLOG_IP, A.OP_MODE,                               \n"
							+ " COALESCE(B.ACTIVE_BACKUP_STATE, 0) AS ACTIVE_BACKUP_STATE, A.ROLE, A.CONN_PROTOCOL,                 \n"
							+ " A.SNMP_VERSION, A.SNMP_USER, A.SNMP_AUTH_PASSWORD, A.SNMP_PRIV_PASSWORD,                            \n"
							+ " A.SNMP_AUTH_PROTOCOL, A.SNMP_PRIV_PROTOCOL, A.SP_SESSION_MAX, A.MGMT_MODE, A.SSO_MODE               \n"
							+ " FROM MNG_ADC A                                                                                      \n"
							+ " LEFT JOIN MNG_ADC_ADDITIONAL B                                                                      \n"
							+ " ON B.ADC_INDEX = A.INDEX                                                                            \n"
							+ " WHERE A.INDEX = %d                                                                                  \n",
					adcIndex);

			rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return null;
			}

			OBDtoAdcInfo info = new OBDtoAdcInfo();

			info.setIndex(db.getInteger(rs, "INDEX"));
			info.setName(db.getString(rs, "NAME"));
			info.setAdcIpAddress(db.getString(rs, "IPADDRESS"));
			info.setAdcAccount(db.getString(rs, "ACCNT"));
			info.setAdcPassword(db.getString(rs, "PASSWORD"));
			info.setAdcCliAccount(db.getString(rs, "CLI_ACCNT"));
			info.setAdcCliPassword(db.getString(rs, "CLI_PASSWORD"));
			info.setModel(db.getString(rs, "MODEL"));
			info.setAdcType(db.getInteger(rs, "TYPE"));
			info.setGroupIndex(db.getInteger(rs, "GROUP_INDEX"));
			info.setDescription(db.getString(rs, "DESCRIPTION"));
			info.setActivePairIndex(db.getInteger(rs, "ACTIVE_PAIR_INDEX"));
			info.setSwVersion(db.getString(rs, "SW_VERSION"));
			info.setHostName(db.getString(rs, "HOST_NAME"));
			info.setApplyTime(db.getTimestamp(rs, "APPLY_TIME"));
			info.setSaveTime(db.getTimestamp(rs, "SAVE_TIME"));
			info.setLastBootTime(db.getTimestamp(rs, "LAST_BOOT_TIME"));
			info.setSnmpRComm(db.getString(rs, "SNMP_RCOMM"));
			info.setStatus(db.getInteger(rs, "STATUS"));
			info.setPurchaseDate(db.getTimestamp(rs, "PURCHASE_TIME"));
			info.setPeerAdcIPAddress(db.getString(rs, "PEER_IPADDRESS"));
			info.setActiveStandbyState(db.getInteger(rs, "ACTIVE_BACKUP_STATE"));
			info.setConnService(db.getInteger(rs, "CONN_SERVICE"));
			info.setConnPort(db.getInteger(rs, "CONN_PORT"));
			info.setConnProtocol(db.getInteger(rs, "CONN_PROTOCOL"));
			info.setRegisterTime(db.getTimestamp(rs, "REGISTER_TIME"));
			info.setSyslogIpAddress(db.getString(rs, "SYSLOG_IP"));
			info.setOpMode(db.getInteger(rs, "OP_MODE"));
			info.setRoleFlbYn(getFlbYesNo(db.getInteger(rs, "ROLE")));

			OBDtoAdcSnmpInfo snmpInfo = new OBDtoAdcSnmpInfo();
			snmpInfo.setVersion(db.getInteger(rs, "SNMP_VERSION"));
			snmpInfo.setRcomm(db.getString(rs, "SNMP_RCOMM"));
			if (snmpInfo.getRcomm() == null || snmpInfo.getRcomm().isEmpty()) {
				snmpInfo.setRcomm(OBDefine.DEFAULT_SNMP_RCOMM);
			}
			snmpInfo.setSecurityName(db.getString(rs, "SNMP_USER"));
			snmpInfo.setAuthProto(db.getString(rs, "SNMP_AUTH_PROTOCOL"));
			snmpInfo.setAuthPassword(db.getString(rs, "SNMP_AUTH_PASSWORD"));
			snmpInfo.setPrivProto(db.getString(rs, "SNMP_PRIV_PROTOCOL"));
			snmpInfo.setPrivPassword(db.getString(rs, "SNMP_PRIV_PASSWORD"));
			info.setSnmpInfo(snmpInfo);

			info.setSpSessionMax(db.getInteger(rs, "SP_SESSION_MAX"));
			info.setMgmtMode(db.getInteger(rs, "MGMT_MODE"));
			info.setSsoMode(db.getInteger(rs, "SSO_MODE"));

			sqlText2 = String.format("SELECT ACCNT_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ADC_INDEX = %d; ", adcIndex);
			rs2 = db.executeQuery(sqlText2);

			ArrayList<Integer> temp = new ArrayList<Integer>();
			while (rs2.next()) {
				temp.add(db.getInteger(rs2, "ACCNT_INDEX"));
			}
			info.setAccountIndexList(temp);

			return info;
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

	public Integer getAdcType(int adcIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT TYPE FROM MNG_ADC WHERE INDEX=%d AND AVAILABLE = %d; ", adcIndex,
					OBDefine.ADC_STATE.AVAILABLE);
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false)
				return null;
			int result = db.getInteger(rs, "TYPE");

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", result));
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

	public String getAdcName(int adcIndex, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		String sqlText = "";
		try {
			sqlText = String.format("SELECT NAME FROM MNG_ADC WHERE INDEX=%d AND AVAILABLE = %d; ", adcIndex,
					OBDefine.ADC_STATE.AVAILABLE);
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false)
				return null;
			String result = db.getString(rs, "NAME");

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

	}

	public ArrayList<String> getAdcStatusAndNameList(String adcIndexs, OBDatabase db) throws OBException {
		ArrayList<String> result = new ArrayList<String>();
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%s", adcIndexs));
		String sqlText = "";
		StringBuilder indexListString = new StringBuilder();
		StringBuilder nameListString = new StringBuilder();
		try {
			sqlText = String.format(
					" SELECT INDEX, NAME FROM MNG_ADC WHERE INDEX IN (%s) AND AVAILABLE = 1 AND STATUS = 1 ; ",
					adcIndexs);
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				indexListString.append(db.getInteger(rs, "INDEX")).append(",");
				nameListString.append(db.getString(rs, "NAME")).append(",");
			}
			result.add(indexListString.toString().substring(0, indexListString.length() - 1));
			result.add(nameListString.toString().substring(0, nameListString.length() - 1));
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result.toString()));
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public String getAdcNameNew(int adcIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		String sqlText = "";

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			sqlText = String.format("SELECT IPADDRESS FROM MNG_ADC WHERE INDEX=%d AND AVAILABLE = %d; ", adcIndex,
					OBDefine.ADC_STATE.AVAILABLE);
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false)
				return null;
			String result = db.getString(rs, "IPADDRESS");

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
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

	public String getAdcIPAddress(int adcIndex, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		String sqlText = "";
		try {
			sqlText = String.format("SELECT IPADDRESS " + " FROM MNG_ADC " + " WHERE INDEX=%d AND AVAILABLE = %d;",
					adcIndex, OBDefine.ADC_STATE.AVAILABLE);
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false)
				return null;
			String result = db.getString(rs, "IPADDRESS");

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public Integer getPeerAdcIPAddress(int adcIndex, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		String sqlText = "";
		try {
			sqlText = String.format(" SELECT INDEX FROM MNG_ADC "
					+ " WHERE PEER_IPADDRESS IN (SELECT IPADDRESS FROM MNG_ADC WHERE INDEX = %d) AND AVAILABLE = %d ",
					adcIndex, OBDefine.ADC_STATE.AVAILABLE);
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false)
				return null;
			Integer result = db.getInteger(rs, "INDEX");

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public Integer getPeerAdcIPAddress(int adcIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" SELECT INDEX FROM MNG_ADC "
					+ " WHERE PEER_IPADDRESS IN (SELECT IPADDRESS FROM MNG_ADC WHERE INDEX = %d) AND AVAILABLE = %d ",
					adcIndex, OBDefine.ADC_STATE.AVAILABLE);
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false)
				return null;
			Integer result = db.getInteger(rs, "INDEX");

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
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

	public Integer getAdcIndex(String adcIPAddress, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format("SELECT  INDEX " + " FROM MNG_ADC " + " WHERE IPADDRESS=%s AND AVAILABLE = %d;",
					OBParser.sqlString(adcIPAddress), OBDefine.ADC_STATE.AVAILABLE);
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false)
				return 0;
			Integer result = db.getInteger(rs, "INDEX");

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public String getAdcName(int adcIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			String adcName = getAdcName(adcIndex, db);
			db.closeDB();

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", adcName));
			return adcName;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public OBDtoAdcInfo getAdcInfoBasicByIPAddress(String ipAddress) throws OBException {
		String sqlText = "";
		ResultSet rs;
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			sqlText = String.format(
					" SELECT A.INDEX, A.NAME, A.IPADDRESS, A.ACCNT, A.PASSWORD, A.CLI_ACCNT, A.CLI_PASSWORD, A.MODEL,          \n"
							+ " A.TYPE, A.GROUP_INDEX, A.DESCRIPTION, A.ACTIVE_PAIR_INDEX, A.SW_VERSION, A.HOST_NAME, A.APPLY_TIME,      \n"
							+ " A.SAVE_TIME, A.LAST_BOOT_TIME, A.STATUS, A.PURCHASE_TIME, A.PEER_IPADDRESS, A.CONN_SERVICE, A.CONN_PORT, \n"
							+ " COALESCE(B.ACTIVE_BACKUP_STATE, 0) AS ACTIVE_BACKUP_STATE, A.ROLE, A.CONN_PROTOCOL,                      \n"
							+ " A.SNMP_RCOMM, A.SNMP_VERSION, A.SNMP_USER, A.SNMP_AUTH_PASSWORD, A.SNMP_PRIV_PASSWORD, A.SNMP_AUTH_PROTOCOL, A.SNMP_PRIV_PROTOCOL \n"
							+ " FROM MNG_ADC                 A                                                                           \n"
							+ " LEFT JOIN MNG_ADC_ADDITIONAL B                                                                           \n"
							+ " ON B.ADC_INDEX = A.INDEX                                                                                 \n"
							+ " WHERE A.IPADDRESS = %s                                                                                   \n"
							+ " AND A.AVAILABLE = %d                                                                                     \n",
					OBParser.sqlString(ipAddress), OBDefine.ADC_STATE.AVAILABLE);

			rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:null"));
				return null;
			}

			OBDtoAdcInfo info = new OBDtoAdcInfo();

			info.setIndex(db.getInteger(rs, "INDEX"));
			info.setName(db.getString(rs, "NAME"));
			info.setAdcIpAddress(db.getString(rs, "IPADDRESS"));
			info.setAdcAccount(db.getString(rs, "ACCNT"));
			info.setAdcPassword(db.getString(rs, "PASSWORD"));
			info.setAdcCliAccount(db.getString(rs, "CLI_ACCNT"));
			info.setAdcCliPassword(db.getString(rs, "CLI_PASSWORD"));
			info.setModel(db.getString(rs, "MODEL"));
			info.setAdcType(db.getInteger(rs, "TYPE"));
			info.setGroupIndex(db.getInteger(rs, "GROUP_INDEX"));
			info.setDescription(db.getString(rs, "DESCRIPTION"));
			info.setActivePairIndex(db.getInteger(rs, "ACTIVE_PAIR_INDEX"));
			info.setSwVersion(db.getString(rs, "SW_VERSION"));
			info.setHostName(db.getString(rs, "HOST_NAME"));
			info.setApplyTime(db.getTimestamp(rs, "APPLY_TIME"));
			info.setSaveTime(db.getTimestamp(rs, "SAVE_TIME"));
			info.setLastBootTime(db.getTimestamp(rs, "LAST_BOOT_TIME"));
			info.setSnmpRComm(db.getString(rs, "SNMP_RCOMM"));
			info.setStatus(db.getInteger(rs, "STATUS"));
			info.setPurchaseDate(db.getTimestamp(rs, "PURCHASE_TIME"));
			info.setPeerAdcIPAddress(db.getString(rs, "PEER_IPADDRESS"));
			info.setActiveStandbyState(db.getInteger(rs, "ACTIVE_BACKUP_STATE"));
			info.setConnService(db.getInteger(rs, "CONN_SERVICE"));
			info.setConnPort(db.getInteger(rs, "CONN_PORT"));
			info.setConnProtocol(db.getInteger(rs, "CONN_PROTOCOL"));
			info.setRoleFlbYn(getFlbYesNo(db.getInteger(rs, "ROLE")));

			OBDtoAdcSnmpInfo snmpInfo = new OBDtoAdcSnmpInfo();
			snmpInfo.setVersion(db.getInteger(rs, "SNMP_VERSION"));
			snmpInfo.setRcomm(db.getString(rs, "SNMP_RCOMM"));
			snmpInfo.setSecurityName(db.getString(rs, "SNMP_USER"));
			snmpInfo.setAuthProto(db.getString(rs, "SNMP_AUTH_PROTOCOL"));
			snmpInfo.setAuthPassword(db.getString(rs, "SNMP_AUTH_PASSWORD"));
			snmpInfo.setPrivProto(db.getString(rs, "SNMP_PRIV_PROTOCOL"));
			snmpInfo.setPrivPassword(db.getString(rs, "SNMP_PRIV_PASSWORD"));
			info.setSnmpInfo(snmpInfo);

			return info;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result adc
		// name:%s", info.getName()));
		// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
		// info));
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public HashMap<String, OBDtoAdcInfo> getAdcInfoBasicMap() throws OBException {
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d",
		// adcIndex));
		// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%s",
		// adcIndex));
		String sqlText = "";
		ResultSet rs;
		HashMap<String, OBDtoAdcInfo> retVal = new HashMap<String, OBDtoAdcInfo>();

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT A.INDEX, A.NAME, A.IPADDRESS, A.ACCNT, A.PASSWORD, A.CLI_ACCNT, A.CLI_PASSWORD, A.MODEL,       \n"
							+ " A.TYPE, A.GROUP_INDEX, A.DESCRIPTION, A.ACTIVE_PAIR_INDEX, A.SW_VERSION, A.HOST_NAME, A.APPLY_TIME,                 \n"
							+ " A.SAVE_TIME, A.LAST_BOOT_TIME, A.STATUS, A.PURCHASE_TIME, A.PEER_IPADDRESS, A.CONN_SERVICE, A.CONN_PORT,            \n"
							+ " COALESCE(B.ACTIVE_BACKUP_STATE, 0) AS ACTIVE_BACKUP_STATE, A.ROLE, A.CONN_PROTOCOL,                                 \n"
							+ " A.SNMP_RCOMM, A.SNMP_VERSION, A.SNMP_USER, A.SNMP_AUTH_PASSWORD, A.SNMP_PRIV_PASSWORD, A.SNMP_AUTH_PROTOCOL, A.SNMP_PRIV_PROTOCOL \n"
							+ " FROM MNG_ADC                 A                                                                                      \n"
							+ " LEFT JOIN MNG_ADC_ADDITIONAL B                                                                                      \n"
							+ " ON B.ADC_INDEX = A.INDEX                                                                                            \n"
							+ " WHERE A.AVAILABLE = %d                                                                                              \n",
					OBDefine.ADC_STATE.AVAILABLE);

			rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcInfo info = new OBDtoAdcInfo();

				info.setIndex(db.getInteger(rs, "INDEX"));
				info.setName(db.getString(rs, "NAME"));
				info.setAdcIpAddress(db.getString(rs, "IPADDRESS"));
				info.setAdcAccount(db.getString(rs, "ACCNT"));
				info.setAdcPassword(db.getString(rs, "PASSWORD"));
				info.setAdcCliAccount(db.getString(rs, "CLI_ACCNT"));
				info.setAdcCliPassword(db.getString(rs, "CLI_PASSWORD"));
				info.setModel(db.getString(rs, "MODEL"));
				info.setAdcType(db.getInteger(rs, "TYPE"));
				info.setGroupIndex(db.getInteger(rs, "GROUP_INDEX"));
				info.setDescription(db.getString(rs, "DESCRIPTION"));
				info.setActivePairIndex(db.getInteger(rs, "ACTIVE_PAIR_INDEX"));
				info.setSwVersion(db.getString(rs, "SW_VERSION"));
				info.setHostName(db.getString(rs, "HOST_NAME"));
				info.setApplyTime(db.getTimestamp(rs, "APPLY_TIME"));
				info.setSaveTime(db.getTimestamp(rs, "SAVE_TIME"));
				info.setLastBootTime(db.getTimestamp(rs, "LAST_BOOT_TIME"));
				info.setSnmpRComm(db.getString(rs, "SNMP_RCOMM"));
				info.setStatus(db.getInteger(rs, "STATUS"));
				info.setPurchaseDate(db.getTimestamp(rs, "PURCHASE_TIME"));
				info.setPeerAdcIPAddress(db.getString(rs, "PEER_IPADDRESS"));
				info.setActiveStandbyState(db.getInteger(rs, "ACTIVE_BACKUP_STATE"));
				info.setConnService(db.getInteger(rs, "CONN_SERVICE"));
				info.setConnPort(db.getInteger(rs, "CONN_PORT"));
				info.setConnProtocol(db.getInteger(rs, "CONN_PROTOCOL"));
				info.setRoleFlbYn(getFlbYesNo(db.getInteger(rs, "ROLE")));

				OBDtoAdcSnmpInfo snmpInfo = new OBDtoAdcSnmpInfo();
				snmpInfo.setVersion(db.getInteger(rs, "SNMP_VERSION"));
				snmpInfo.setRcomm(db.getString(rs, "SNMP_RCOMM"));
				snmpInfo.setSecurityName(db.getString(rs, "SNMP_USER"));
				snmpInfo.setAuthProto(db.getString(rs, "SNMP_AUTH_PROTOCOL"));
				snmpInfo.setAuthPassword(db.getString(rs, "SNMP_AUTH_PASSWORD"));
				snmpInfo.setPrivProto(db.getString(rs, "SNMP_PRIV_PROTOCOL"));
				snmpInfo.setPrivPassword(db.getString(rs, "SNMP_PRIV_PASSWORD"));
				info.setSnmpInfo(snmpInfo);

				retVal.put(info.getAdcIpAddress(), info);
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
		return retVal;
	}

	private OBDtoAdcSystemInfo getAdcSystemInfo(int adcIndex, String ipaddress, String account, String password,
			int connService, int connPort, int adcType, String swVersion, OBDtoAdcSnmpInfo snmpInfo, int opMode) {
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format(
				"start. adcIndex:%d, ip:%s, id:%s, passwd:%s, service:%d, port:%d, vendor:%d, swVersion:%s, opMode:%d",
				adcIndex, ipaddress, account, password, connService, connPort, adcType, swVersion, opMode));
		OBAdcSystemInfo infoClass;
		if (adcType == OBDefine.ADC_TYPE_F5) {
			infoClass = new OBAdcSystemInfoF5();
		} else if (adcType == OBDefine.ADC_TYPE_ALTEON) {
			infoClass = new OBAdcSystemInfoAlteon();
		} else if (adcType == OBDefine.ADC_TYPE_PIOLINK_PAS) {
			infoClass = new OBAdcSystemInfoPAS();
		} else if (adcType == OBDefine.ADC_TYPE_PIOLINK_PASK) {
			infoClass = new OBAdcSystemInfoPASK();
		} else {
			return null;
		}
		OBDtoAdcSystemInfo info;
		try {
			info = infoClass.getAdcSystemInfo(adcIndex, ipaddress, account, password, connService, connPort, swVersion,
					snmpInfo, opMode);
		} catch (OBExceptionUnreachable e) {
			return null;
		} catch (OBExceptionLogin e) {
			return null;
		} catch (OBException e) {
			return null;
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", info));
		return info;
	}

	/**
	 * ADC 의 version, os, time정보등을 업데이트한다.
	 * 
	 * @param adcInfo
	 * @throws OBException
	 */
	private boolean updateAdcModelVersionTimeInfo(OBDtoAdcInfo adcInfo) throws OBException {
		try {
			OBDtoAdcSystemInfo info = null;

			info = getAdcSystemInfo(0, adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort(),
					adcInfo.getAdcType(), "", adcInfo.getSnmpInfo(), adcInfo.getOpMode());

			if (info != null) {
				adcInfo.setSwVersion(info.getSwVersion());
				adcInfo.setHostName(info.getHostName());
				adcInfo.setSaveTime(info.getLastSaveTime());
				adcInfo.setLastBootTime(info.getLastBootTime());
				adcInfo.setModel(info.getModel());
				adcInfo.setStatus(info.getStatus());
				writeAdcModelVersionTimeInfoToDB(adcInfo);
				return true;
			}
			return false;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public Integer addAdcInfo(OBDtoAdcInfo adcInfo, OBDtoExtraInfo extraInfo, boolean isReachabled, int isReachable)
			throws OBLicenseExpiredException, OBException {
		Integer adcIndex = 0;

		try {
			if (isExistAdcCore(adcInfo.getName(), adcInfo.getAdcIpAddress()) == true) {
				throw new OBException(OBException.ERRCODE_ADC_EXIST);
			}
			// Piolink면 실제 type을 구한다.
			Integer adcType = OBCommon.checkAdcType(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getAdcCliAccount(), adcInfo.getAdcCliPasswordDecrypt(),
					adcInfo.getAdcType(), adcInfo.getConnService(), adcInfo.getConnPort());
			adcInfo.setAdcType(adcType);

//            if(adcType == OBDefine.ADC_TYPE_ALTEON)
//            {
//        		String[] verElements = adcInfo.getSwVersion().split("\\.", 2); //버전 요소 두개까지 문자열로 건진다. 지금은 하나만 쓴다.
//                int version = Integer.parseInt(verElements[0]);
//            	if(version >= 30)
//            	{
//            		ConfigAlteonDto alteon = addVdirectAlteon(adcInfo);
//            		
//            		String resultCode = new OBVdirectServiceImpl().createAlteon(alteon);
//            		if(resultCode != "201")
//            		{
//            			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "vdirect alteon create failed");
//            		}
//            		
//            		resultCode = new OBVdirectServiceImpl().configSnmp(alteon);
//            		if(resultCode != "201")
//            		{
//            			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, "vdirect alteon snmp setting failed");
//            		}
//            		
//            		resultCode = new OBVdirectServiceImpl().configCli(alteon);
//            		if(resultCode != "201")
//            		{
//            			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, "vdirect alteon cli setting failed");
//            		}
//            	}
//            }

			addAdcInfoBase(adcInfo); // ADC 기본 정보 저장

			adcIndex = getAdcIndex(adcInfo.getName(), adcInfo.getAdcIpAddress());

			// active/stand-by 상태를 추가함.
			new OBAdcMonitorDB().writeAdcActiveStandbyState(adcIndex, OBDtoAdcInfo.ACTIVE_STANDBY_STATE_UNKNOWN);

			// adc-account mapping 테이블 수정.
			addAccountList(adcIndex, adcInfo.getAccountIndexList());

			// alarm 설정을 default로 만들어 준다.
			new OBAlarmImpl().addAdcAlarmConfiguration(adcIndex, adcInfo.getAdcType());

			setAdcLastUpdateTime();

			new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcIndex, extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_ADC_ADD_SUCCESS, adcInfo.getName(), null);

			// adc 연결 상태 검사를 위한 테이블 생성한다.
			new OBCheckAdcStatus().makeLastADCCheckTimeTable(adcIndex);

			return adcIndex;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private void addAdcInfoBase(OBDtoAdcInfo adcInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("call. adcInfo:%s", adcInfo));
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();
			// 값 유효성 검사한다.
			if (adcInfo.getName().isEmpty())
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "illegal argument(ADC name)");
			if (adcInfo.getAdcIpAddress().isEmpty())
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "illegal argument(ADC IPAddress)");
			if (adcInfo.getAdcPassword().isEmpty())
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "illegal argument(ADC password)");
			// if(adcInfo.getAdcAccount().isEmpty())
			// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "illegal
			// argument(ADC account)");

			String snmpCommunity = "";
			if (adcInfo.getSnmpInfo().getRcomm() == null || adcInfo.getSnmpInfo().getRcomm().isEmpty())
				snmpCommunity = OBDefine.DEFAULT_SNMP_RCOMM;
			else
				snmpCommunity = adcInfo.getSnmpInfo().getRcomm();

			Timestamp purchaseTime;
			if (adcInfo.getPurchaseDate() == null) {
				purchaseTime = null;
			} else {
				purchaseTime = new Timestamp(adcInfo.getPurchaseDate().getTime());
			}

			String snmpUser = "";
			String snmpAuthPw = "";
			String snmpPrivPw = "";
			String snmpAuthProto = "none";
			String snmpPrivProto = "none";
			if (adcInfo.getSnmpInfo().getVersion() == 3) {
				snmpUser = adcInfo.getSnmpInfo().getSecurityName();
				snmpAuthPw = adcInfo.getSnmpInfo().getAuthPassword();
				snmpPrivPw = adcInfo.getSnmpInfo().getPrivPassword();
				snmpAuthProto = adcInfo.getSnmpInfo().getAuthProto();
				snmpPrivProto = adcInfo.getSnmpInfo().getPrivProto();
			}
			// else
			// {
			// snmpUser = "";
			// snmpAuthPw = "";
			// snmpPrivPw = "";
			// }

			sqlText = String.format(" INSERT INTO MNG_ADC "
					+ " (NAME, IPADDRESS, ACCNT, PASSWORD, CLI_ACCNT, CLI_PASSWORD, TYPE, GROUP_INDEX, SNMP_RCOMM, "
					+ "  DESCRIPTION, ACTIVE_PAIR_INDEX, AVAILABLE, "
					+ "  STATUS, PURCHASE_TIME, PEER_IPADDRESS, CONN_SERVICE, CONN_PORT, REGISTER_TIME, SYSLOG_IP, ROLE, "
					+ "  SNMP_VERSION, SNMP_USER, SNMP_AUTH_PASSWORD, SNMP_PRIV_PASSWORD, SNMP_AUTH_PROTOCOL, SNMP_PRIV_PROTOCOL, OP_MODE, MGMT_MODE, SSO_MODE, CONN_PROTOCOL) "
					+ " VALUES "
					+ " (%s, %s, %s, %s, %s, %s, %d, %d, %s, %s, %d, %d, %d, %s, %s, %d, %d, CURRENT_TIMESTAMP, %s, %d, %d, %s, %s, %s, %s, %s, %d, %d, %d, %d); ",
					OBParser.sqlString(adcInfo.getName()), OBParser.sqlString(adcInfo.getAdcIpAddress()),
					OBParser.sqlString(adcInfo.getAdcAccount()), OBParser.sqlString(adcInfo.getAdcPassword()),
					OBParser.sqlString(adcInfo.getAdcCliAccount()), OBParser.sqlString(adcInfo.getAdcCliPassword()),
					adcInfo.getAdcType(), adcInfo.getGroupIndex(), OBParser.sqlString(snmpCommunity),
					OBParser.sqlString(adcInfo.getDescription()), adcInfo.getActivePairIndex(),
					OBDefine.ADC_STATE.AVAILABLE, adcInfo.getStatus(),
					OBParser.sqlString(OBDateTime.toString(purchaseTime)),
					OBParser.sqlString(adcInfo.getPeerAdcIPAddress()), adcInfo.getConnService(), adcInfo.getConnPort(),
					OBParser.sqlString(adcInfo.getSyslogIpAddress()), OBDefine.ADC_ROLE_MASK.SLB, // default
					// -
					// slb
					// only,
					// 사용자가
					// 입력하는
					// 값이
					// 아니므로,
					// 추가할
					// 때만직접
					// 넣어주고,
					// ADC
					// 수정부터는
					// 건드리지
					// 않는다.
					adcInfo.getSnmpInfo().getVersion(), OBParser.sqlString(snmpUser), OBParser.sqlString(snmpAuthPw),
					OBParser.sqlString(snmpPrivPw), OBParser.sqlString(snmpAuthProto),
					OBParser.sqlString(snmpPrivProto), adcInfo.getOpMode(), adcInfo.getMgmtMode(), adcInfo.getSsoMode(),
					adcInfo.getConnProtocol());
			db.executeUpdate(sqlText);
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

	private void writeAdcModelVersionTimeInfoToDB(OBDtoAdcInfo adcInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("call. adcInfo:%s", adcInfo));
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();
			// a값 유효성 검사한다.
			if (adcInfo.getName().isEmpty())
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "illegal argument(ADC name)");
			if (adcInfo.getAdcIpAddress().isEmpty())
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "illegal argument(ADC IPAddress)");
			if (adcInfo.getAdcPassword().isEmpty())
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "illegal argument(ADC password)");
			// if(adcInfo.getAdcAccount().isEmpty())
			// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "illegal
			// argument(ADC account)");

			sqlText = String.format(" UPDATE MNG_ADC "
					+ " SET MODEL=%s, SW_VERSION=%s, HOST_NAME=%s, APPLY_TIME=%s, SAVE_TIME=%s, LAST_BOOT_TIME=%s "
					+ " WHERE INDEX=%d; ", OBParser.sqlString(adcInfo.getModel()),
					OBParser.sqlString(adcInfo.getSwVersion()), OBParser.sqlString(adcInfo.getHostName()),
					OBParser.sqlString(adcInfo.getApplyTime()), OBParser.sqlString(adcInfo.getSaveTime()),
					OBParser.sqlString(adcInfo.getLastBootTime()), adcInfo.getIndex());
			db.executeUpdate(sqlText);
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

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private void addAccountList(int adcIndex, ArrayList<Integer> accountList) throws OBException {
		OBDatabase db = new OBDatabase();

		if (accountList == null) {
			return;
		}

		String sqlText = "";
		try {
			db.openDB();
			// 기존에 저장된 목록을 지운다.
			delAccountList(adcIndex);
			for (int i = 0; i < accountList.size(); i++) {
				Integer accountIndex = accountList.get(i);

				sqlText = String.format(
						" INSERT INTO " + " MNG_ACCNT_ADC_MAP " + " (ACCNT_INDEX, ADC_INDEX) " + " VALUES (%d, %d);",
						accountIndex, adcIndex);
				db.executeUpdate(sqlText);
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

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private void delAccountList(int adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		// 기존에 저장된 사용자-ADC 관계를 adcIndex를 기준으로 지운다. 진짜 삭제
		String sqlText = "";
		try {
			db.openDB();
			sqlText = String.format(" DELETE FROM MNG_ACCNT_ADC_MAP WHERE ADC_INDEX=%d;", adcIndex);

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

	@Override
	public void delAdcInfoList(ArrayList<Integer> adcIndexList, OBDtoExtraInfo extraInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndexList:%s, extraInfo:%s", adcIndexList, extraInfo));
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			for (int i = 0; i < adcIndexList.size(); i++) {
				Integer index = adcIndexList.get(i).intValue();

				OBDtoAdcInfo adcInfo;
				// adcInfo = getAdcInfo(index, db);
				adcInfo = getAdcInfo(index);
//				if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
//				{
//					String[] verElements = adcInfo.getSwVersion().split("\\.", 2); //버전 요소 두개까지 문자열로 건진다. 지금은 하나만 쓴다.
//					int version = Integer.parseInt(verElements[0]);
//					if (version >= 30)
//					{
//						ConfigAlteonDto alteon = delVdirectAlteon(adcInfo);
//
//						String resultCode = new OBVdirectServiceImpl().deleteAlteon(alteon);
//						if (!resultCode.equals("204"))
//						{
//							OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("vdirect alteon delete failed"));
//						}
//
//						resultCode = new OBVdirectServiceImpl().deleteSnmp(alteon);
//						if (!resultCode.equals("204") || !resultCode.equals("404"))
//						{
//							OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, "vdirect alteon snmp setting failed");
//						}
//
//						resultCode = new OBVdirectServiceImpl().deleteCli(alteon);
//						if (!resultCode.equals("204") || !resultCode.equals("404"))
//						{
//							OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, "vdirect alteon cli setting failed");
//						}
//					}
//				}
				delAdcInfoListCore(index);
				// 감사로그 생성.
				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), index, extraInfo.getClientIPAddress(),
						OBSystemAuditImpl.AUDIT_ADC_DEL_SUCCESS, adcInfo.getName(), null);

				new OBVServerDB().delAllTable(index);
				new OBAlarmImpl().delAdcAlarmConfiguration(index); // 경보지움
			}

			setAdcLastUpdateTime();
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public void delAdcInfoListCore(Integer adcIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		try
		// 장비는 제거하지 안고 DB에 둔다.
		{
			db.openDB();

			// 계정-adc 관계를 제거한다. 진짜 삭제
			delAccountList(adcIndex);

			sqlText = String.format(" UPDATE MNG_ADC " + " SET AVAILABLE = %d " + " WHERE INDEX=%d; ",
					OBDefine.ADC_STATE.UNAVAILABLE, adcIndex);

			db.executeUpdate(sqlText);

			OBVServerDB vDB = new OBVServerDB();
			vDB.delVServerAll(adcIndex);
			vDB.delNodeAll(adcIndex);
			vDB.delPoolAll(adcIndex);
			vDB.delVServiceAll(adcIndex);
			vDB.delPoolmemberAll(adcIndex);
			vDB.delL3InterfaceAll(adcIndex);
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

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public void setAdcType(Integer adcIndex, Integer adcType) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" UPDATE MNG_ADC " + " SET TYPE = %d " + " WHERE INDEX=%d; ", adcType, adcIndex);

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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	// DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할 예정이다.
	private void setAdcLastUpdateTime() throws OBException {
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();
			sqlText = String.format("SELECT OCCUR_TIME " + "FROM MNG_TIME " + "WHERE TYPE = %s;",
					OBParser.sqlString(OBDefine.MNG_TIME_TYPE_ADC));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false) {// insert
				sqlText = String.format("INSERT INTO MNG_TIME " + "(TYPE, OCCUR_TIME) " + "VALUES (%s, %s); ",
						OBParser.sqlString(OBDefine.MNG_TIME_TYPE_ADC), OBParser.sqlString(OBDateTime.now()));
				db.executeUpdate(sqlText);
			} else {// update
				sqlText = String.format("UPDATE MNG_TIME " + "SET " + "OCCUR_TIME=%s " + "WHERE TYPE=%s;",
						OBParser.sqlString(OBDateTime.now()), OBParser.sqlString(OBDefine.MNG_TIME_TYPE_ADC));
				db.executeUpdate(sqlText);
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

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public Timestamp getAdcLastUpdateTime() throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start."));
		String sqlText = "";
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			sqlText = String.format("SELECT " + "OCCUR_TIME " + "FROM MNG_TIME " + "WHERE TYPE = %s;",
					OBParser.sqlString(OBDefine.MNG_TIME_TYPE_ADC));

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

	@Override
	public void setAdcInfo(OBDtoAdcInfo adcInfo, OBDtoExtraInfo extraInfo, int isReachable) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcInfo:%s, extraInfo:%s", adcInfo, extraInfo));

		try {
			OBDtoAdcInfo oldAdcInfo = getAdcInfo(adcInfo.getIndex());

			adcInfo = setPassword(adcInfo);

			setAdcInfoBase(adcInfo); // ADC 기본 정보 저장

			// adc-account mapping 테이블 수정.
			addAccountList(adcInfo.getIndex(), adcInfo.getAccountIndexList());

			// a그룹의 alarm을 쓰는 경우, 그룹 alarm 교체
			if (oldAdcInfo.getGroupIndex().equals(adcInfo.getGroupIndex()) == false) // group이 바뀌었으면
			{
				new OBAlarmImpl().changeAdcGroupAlarm(adcInfo.getIndex(), adcInfo.getAdcType(),
						adcInfo.getGroupIndex());
			}

			setAdcLastUpdateTime();
			new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcInfo.getIndex(),
					extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_ADC_SET_SUCCESS, adcInfo.getName(), null);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	private OBDtoAdcInfo setPassword(OBDtoAdcInfo adcInfo) throws OBException {
		OBDatabase db = new OBDatabase();

		String password = "";
		String passwordCli = "";
		String sqlText = "";

		try {
			db.openDB();

			if (adcInfo.getOpMode() != OBDefine.OP_MODE_MONITORING) {
				// 값 유효성 검사한다.
				if (adcInfo.getName().isEmpty() || adcInfo.getAdcIpAddress().isEmpty()
						|| adcInfo.getAdcPassword().isEmpty())
					throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER);// "illegal argument(ADC name)");
			} else {
				if (adcInfo.getAdcType() != OBDefine.ADC_TYPE_ALTEON) {
					// 값 유효성 검사한다.
					if (adcInfo.getName().isEmpty() || adcInfo.getAdcIpAddress().isEmpty()
							|| adcInfo.getAdcPassword().isEmpty())
						throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER);// "illegal argument(ADC name)");
				}
			}

			sqlText = String.format(
					" SELECT INDEX, PASSWORD, CLI_PASSWORD" + " FROM MNG_ADC " + " WHERE INDEX=%d AND AVAILABLE = %d;",
					adcInfo.getIndex(), OBDefine.ADC_STATE.AVAILABLE);
			ResultSet rs;
			rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				throw new OBException(OBException.ERRCODE_DB_QEURY);
			}
			password = db.getString(rs, "PASSWORD");
			passwordCli = db.getString(rs, "CLI_PASSWORD");

			// 데이터 수정.
			if (!password.isEmpty() && password.compareTo(adcInfo.getAdcPassword()) != 0) {
				password = new OBCipherAES().Encrypt(adcInfo.getAdcPassword());
			}

			if (!passwordCli.isEmpty() && passwordCli.compareTo(adcInfo.getAdcCliPassword()) != 0) {
				passwordCli = new OBCipherAES().Encrypt(adcInfo.getAdcCliPassword());
			}

			adcInfo.setAdcPassword(password);
			adcInfo.setAdcCliPassword(passwordCli);
			return adcInfo;
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

	private void setAdcInfoBase(OBDtoAdcInfo adcInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("call. adcInfo:%s", adcInfo));
		OBDatabase db = new OBDatabase();

		String sqlText = "";

		try {
			db.openDB();

			String snmpCommunity = "";
			String snmpUser = "";
			String snmpAuthPw = "";
			String snmpPrivPw = "";
			String snmpAuthProto = "none";
			String snmpPrivProto = "none";
			if (adcInfo.getSnmpInfo().getVersion() == 3) {
				snmpUser = adcInfo.getSnmpInfo().getSecurityName();
				snmpAuthPw = adcInfo.getSnmpInfo().getAuthPassword();
				snmpPrivPw = adcInfo.getSnmpInfo().getPrivPassword();
				snmpAuthProto = adcInfo.getSnmpInfo().getAuthProto();
				snmpPrivProto = adcInfo.getSnmpInfo().getPrivProto();
			} else {
				snmpCommunity = adcInfo.getSnmpInfo().getRcomm();
				snmpUser = "";
				snmpAuthPw = "";
				snmpPrivPw = "";
			}

			Timestamp purchaseTime;
			if (adcInfo.getPurchaseDate() == null) {
				purchaseTime = null;
			} else {
				purchaseTime = new Timestamp(adcInfo.getPurchaseDate().getTime());
			}

			sqlText = String.format(" UPDATE MNG_ADC " + " SET "
					+ " NAME=%s, IPADDRESS=%s, ACCNT=%s, PASSWORD=%s, CLI_ACCNT=%s, CLI_PASSWORD=%s, TYPE=%d, GROUP_INDEX=%d,               "
					+ " SNMP_RCOMM=%s, DESCRIPTION=%s, ACTIVE_PAIR_INDEX = %d, PURCHASE_TIME=%s, PEER_IPADDRESS=%s, CONN_SERVICE=%d,        "
					+ " CONN_PORT=%d, SYSLOG_IP=%s, SNMP_VERSION = %d, SNMP_USER = %s, SNMP_AUTH_PASSWORD = %s,                             "
					+ " SNMP_PRIV_PASSWORD = %s, SNMP_AUTH_PROTOCOL = %s, SNMP_PRIV_PROTOCOL = %s, OP_MODE=%d, MGMT_MODE=%d, SSO_MODE=%d,   "
					+ " CONN_PROTOCOL=%d                                                                                                    ",
					OBParser.sqlString(adcInfo.getName()), OBParser.sqlString(adcInfo.getAdcIpAddress()),
					OBParser.sqlString(adcInfo.getAdcAccount()), OBParser.sqlString(adcInfo.getAdcPassword()),
					OBParser.sqlString(adcInfo.getAdcCliAccount()), OBParser.sqlString(adcInfo.getAdcCliPassword()),
					adcInfo.getAdcType(), adcInfo.getGroupIndex(), OBParser.sqlString(snmpCommunity),
					OBParser.sqlString(adcInfo.getDescription()), 0,
					OBParser.sqlString(OBDateTime.toString(purchaseTime)),
					OBParser.sqlString(adcInfo.getPeerAdcIPAddress()), adcInfo.getConnService(), adcInfo.getConnPort(),
					OBParser.sqlString(adcInfo.getSyslogIpAddress()), adcInfo.getSnmpInfo().getVersion(),
					OBParser.sqlString(snmpUser), OBParser.sqlString(snmpAuthPw), OBParser.sqlString(snmpPrivPw),
					OBParser.sqlString(snmpAuthProto), OBParser.sqlString(snmpPrivProto), adcInfo.getOpMode(),
					adcInfo.getMgmtMode(), adcInfo.getSsoMode(), adcInfo.getConnProtocol());

			sqlText += String.format(" WHERE INDEX=%d;", adcInfo.getIndex());

			db.executeUpdate(sqlText);
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

	public ArrayList<OBDtoAdcGroup> getAdcGroupListByAccount(Integer accountIndex, String searchKey)
			throws OBException {
		return getAdcGroupListByAccount(accountIndex, 0, searchKey);
	}

	@Override
	public ArrayList<OBDtoAdcGroup> getAdcGroupList(Integer groupIndex) throws OBException {
		// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start.
		// groupIndex:%d", groupIndex));
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		ArrayList<OBDtoAdcGroup> list = new ArrayList<OBDtoAdcGroup>();
		try {
			db.openDB();

			sqlText = String.format("SELECT INDEX, NAME, DESCRIPTION FROM MNG_ADC_GROUP ");

			if (groupIndex != null) {
				sqlText += String.format(" WHERE AVAILABLE = %d AND INDEX = %d ", OBDefine.DATA_AVAILABLE, groupIndex);
			} else {
				sqlText += String.format(" WHERE AVAILABLE = %d ", OBDefine.DATA_AVAILABLE);
			}
			sqlText += " ORDER BY NAME;";

			ResultSet rs;
			rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcGroup group = new OBDtoAdcGroup();
				group.setIndex(db.getInteger(rs, "INDEX"));
				group.setName(db.getString(rs, "NAME"));
				group.setDescription(db.getString(rs, "DESCRIPTION"));
				list.add(group);
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
		// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
		// list));
		return list;
	}

	@Override
	public void addAdcGroup(OBDtoAdcGroup adcGroup, OBDtoExtraInfo extraInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcGroup:%s, extraInfo:%s", adcGroup, extraInfo));

		// 동일한 항목이 존재하는지 확인한다. IP And Name 을 기준으로 한다.
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		// 추가한다.
		try {
			db.openDB();

			// 값 유효성 검사한다.
			if (adcGroup.getName().isEmpty())
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "illegal argument(ADC group name)");

			if (isExistGroupName(adcGroup.getName(), db) == true) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						"illegal argument(already exist ADC group)");
			}

			sqlText = String.format(
					" INSERT INTO MNG_ADC_GROUP      \n" + " (NAME, DESCRIPTION, AVAILABLE) \n"
							+ " VALUES                         \n" + " (%s, %s, %d);                  \n",
					OBParser.sqlString(adcGroup.getName()), OBParser.sqlString(adcGroup.getDescription()),
					OBDefine.DATA_AVAILABLE);

			db.executeUpdate(sqlText);

			// alarm 설정을 default로 만들어 준다.
			Integer groupIndex = getGroupIndexByName(adcGroup.getName(), db);
			new OBAlarmImpl().addGroupAlarmConfiguration(groupIndex);
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_ACCNTGROUP_ADD_SUCCESS, adcGroup.getName());
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	@Override
	public boolean isExistGroup(String groupName) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. groupName:%s", groupName));
		OBDatabase db = new OBDatabase();
		boolean result;
		try {
			db.openDB();

			result = isExistGroupName(groupName, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	private Integer getGroupIndexByName(String name, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(" SELECT INDEX FROM MNG_ADC_GROUP WHERE AVAILABLE = %d AND NAME = %s \n",
					OBDefine.DATA_AVAILABLE, OBParser.sqlString(name));
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				return db.getInteger(rs, "INDEX");
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public boolean isExistGroupName(String groupName, OBDatabase db) throws OBException {
		String sqlText = "";

		if (groupName == null) {
			return false;
		}

		try {
			sqlText = String.format(" SELECT INDEX FROM MNG_ADC_GROUP WHERE NAME=%s AND AVAILABLE = %d ",
					OBParser.sqlString(groupName), OBDefine.STATUS_AVAILABLE);

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return false;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return true;
	}

	public boolean isExistGroupIndex(Integer groupIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. groupIndex:%s", groupIndex));
		OBDatabase db = new OBDatabase();

		boolean result;
		try {
			db.openDB();
			result = isExistGroupIndex(groupIndex, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	public boolean isExistGroupIndex(Integer groupIndex, OBDatabase db) throws OBException {
		String sqlText = "";
		if (groupIndex == null) {
			return false;
		}

		try {
			sqlText = String.format(" SELECT INDEX FROM MNG_ADC_GROUP WHERE INDEX=%d AND AVAILABLE=%d ", groupIndex,
					OBDefine.STATUS_AVAILABLE);

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return false;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return true;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// ArrayList<Integer> groupIndexList = new ArrayList<Integer>();
	// groupIndexList.add(3);
	// groupIndexList.add(4);
	// OBDtoExtraInfo info = new OBDtoExtraInfo();
	// info.setAccountIndex(1);
	// new OBAdcManagementImpl().delAdcGroupList(groupIndexList, info);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public void delAdcGroupList(ArrayList<Integer> groupIndexList, OBDtoExtraInfo extraInfo) throws OBException {
		// 동일한 항목이 존재하는지 확인한다. IP And Name 을 기준으로 한다.
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		try {
			db.openDB();
			int groupNum = groupIndexList.size(); // 삭제 대상 그룹 수
			int i = 0;
			int adcCount = 0; // 그룹 소속 ADC 수

			// 삭제 대상 group 중, 소속 장비가 있는 group이 하나라도 있으면 작업하지 않는다.
			for (i = 0; i < groupNum; i++) {
				Integer groupIndex = groupIndexList.get(i);
				adcCount = getGroupAdcCountCore(groupIndex, db);
				if (adcCount > 0) {
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format(
							"Group(group_index=%d) has %d ADCs. Can't delete the group.", groupIndex, adcCount));
				}
			}

			// 소속 장비가 있는 그룹이 하나도 없음. 이제 그룹 삭제
			for (Integer groupIndex : groupIndexList) {
				String name = getGroupName(groupIndex);
				sqlText = String.format(" UPDATE MNG_ADC_GROUP     \n" + " SET AVAILABLE = %d       \n"
						+ " WHERE INDEX=%d;          \n", OBDefine.DATA_UNAVAILABLE, groupIndex);
				db.executeUpdate(sqlText);
				new OBAlarmImpl().delGroupAlarmConfiguration(groupIndex); // 경보지움
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAuditImpl.AUDIT_ACCNTGROUP_DEL_SUCCESS, name);
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

	private String getGroupName(Integer index) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" SELECT NAME FROM MNG_ADC_GROUP WHERE AVAILABLE = %d AND INDEX=%d; ",
					OBDefine.DATA_AVAILABLE, index);
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false)
				return null;
			return db.getString(rs, "NAME");
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
	public void setAdcGroup(OBDtoAdcGroup adcGroup, OBDtoExtraInfo extraInfo) throws OBException {
		// 수정 대상 group 존재 확인
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		try {
			db.openDB();

			// 값 유효성 검사한다.
			if (adcGroup.getIndex() > 0)
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "illegal argument(ADC group index)");
			if (adcGroup.getName().isEmpty())
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "illegal argument(ADC group name)");

			sqlText = String.format(" SELECT INDEX                      \n" + " FROM MNG_ADC_GROUP                \n"
					+ " WHERE INDEX=%d AND AVAILABLE = %d;\n", adcGroup.getIndex(), OBDefine.DATA_AVAILABLE);

			ResultSet rs;
			rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "illegal argument(not exist ADC group)");
			}

			// 데이터 수정.
			sqlText = String.format(
					" UPDATE MNG_ADC_GROUP            \n" + " SET DESCRIPTION=%s              \n"
							+ " WHERE INDEX=%d;                 \n",
					OBParser.sqlString(adcGroup.getDescription()), adcGroup.getIndex());
			db.executeUpdate(sqlText);
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_ACCNTGROUP_SET_SUCCESS, adcGroup.getName());
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

	@Override
	public ArrayList<OBDtoAccount> getTrivialAccountList(Integer adcIndex, String searchKey) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, searchKey:%s", adcIndex, searchKey));

		String wildcardKey = "";
		ResultSet rs, rs2;
		String sqlText = "";
		String sqlText2 = "";

		OBDatabase db = new OBDatabase();
		OBDatabase db2 = new OBDatabase();

		// 역할이 'SystemAdmin' 아닌 계정만 가져온다.
		ArrayList<OBDtoAccount> list = new ArrayList<OBDtoAccount>();
		try {
			db.openDB();
			db2.openDB();

			if (searchKey != null && searchKey.isEmpty() == false) {
				wildcardKey = "%" + searchKey + "%";
			}

			if (wildcardKey.isEmpty() == true) // 전체 조회
			{
				sqlText = String
						.format(" SELECT INDEX, ID, NAME, CMT, PSWD, TEL_NO, MBPH_NO, EMAIL, LST_CONN_DTM, ROLE_NO, \n"
								+ " MSTK_CNT, ALERT_WINDOW, ALERT_SOUND                                               \n"
								+ " FROM MNG_ACCNT                                                                    \n"
								+ " WHERE AVAILABLE = %d                                                              \n"
								+ " AND ROLE_NO IN (SELECT INDEX FROM %s WHERE NAME <> 'SystemAdmin')                 \n"
								+ // where-in:empty
									// string
									// 불가,
									// null
									// 불가,
									// OK
								" AND INDEX NOT IN (SELECT ACCNT_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ADC_INDEX = %d) \n"
								+ // where-in:empty string 불가, null 불가, OK
								" ORDER BY ID;                                                                      \n",
								OBDefine.DATA_AVAILABLE, OBCommon.makeProperTableName("MNG_ACCNT_ROLE"), adcIndex);
			} else
			// keyword 검색
			{
				sqlText = String
						.format(" SELECT INDEX, ID, NAME, CMT, PSWD, TEL_NO, MBPH_NO, EMAIL, LST_CONN_DTM, ROLE_NO,  \n"
								+ " MSTK_CNT, ALERT_WINDOW, ALERT_SOUND                                                \n"
								+ " FROM MNG_ACCNT                                                                     \n"
								+ " WHERE AVAILABLE = %d                                                               \n"
								+ " AND ROLE_NO IN (SELECT INDEX FROM %s WHERE NAME <> 'SystemAdmin')                  \n"
								+ // where-in:empty
									// string
									// 불가,
									// null
									// 불가,
									// OK
								" AND INDEX NOT IN (SELECT ACCNT_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ADC_INDEX = %d)  \n"
								+ // where-in:empty string 불가, null 불가, OK
								" AND NAME LIKE %s                                                                   \n"
								+ " ORDER BY ID ;                                                                      \n",
								OBDefine.DATA_AVAILABLE, OBCommon.makeProperTableName("MNG_ACCNT_ROLE"), adcIndex,
								OBParser.sqlString(wildcardKey));
			}

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAccount account = new OBDtoAccount();
				account.setAccountIndex(db.getInteger(rs, "INDEX"));
				account.setAccountId(db.getString(rs, "ID"));
				account.setAccountName(db.getString(rs, "NAME"));
				account.setAccountComment(db.getString(rs, "CMT"));
				account.setAccountPassword(db.getString(rs, "PSWD"));
				account.setTelephone(db.getString(rs, "TEL_NO"));
				account.setMobilePhone(db.getString(rs, "MBPH_NO"));
				account.setEmail(db.getString(rs, "EMAIL"));
				account.setLastLogin(db.getTimestamp(rs, "LST_CONN_DTM"));
				account.setRoleNo(db.getInteger(rs, "ROLE_NO"));
				account.setAlertWindow(db.getInteger(rs, "ALERT_WINDOW"));
				account.setAlertSound(db.getInteger(rs, "ALERT_SOUND"));

				sqlText2 = String.format(
						" SELECT A.ADC_INDEX, B.NAME " + " FROM MNG_ACCNT_ADC_MAP A " + " INNER JOIN MNG_ADC B "
								+ " ON B.INDEX=A.ADC_INDEX " + " WHERE A.ACCNT_INDEX = %d " + " ORDER BY A.ADC_INDEX;",
						account.getAccountIndex());

				rs2 = db2.executeQuery(sqlText2);

				ArrayList<OBDtoAdcVSInfo> adcList = new ArrayList<OBDtoAdcVSInfo>();

				while (rs2.next()) {
					OBDtoAdcVSInfo name = new OBDtoAdcVSInfo();
					name.setAdcIndex(db2.getInteger(rs2, "ADC_INDEX"));
					name.setAdcName(db2.getString(rs2, "NAME"));
					adcList.add(name);
				}

				account.setAdcVsList(adcList);

				list.add(account);
			}
		} catch (SQLException e) {
			db.closeDB();
			db2.closeDB();
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			db.closeDB();
			db2.closeDB();
			throw e;
		} catch (Exception e) {
			db.closeDB();
			db2.closeDB();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
			if (db2 != null)
				db2.closeDB();
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));
		return list;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// Integer adcType = new
	// OBAdcManagementImpl().isReachable(OBDefine.ADC_TYPE_PIOLINK_UNKNOWN,
	// "192.168.200.110", "root", "admin", "", "openbase", OBDefine.SERVICE.SSH,
	// 1025);
	// System.out.println(adcType);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }
	/**
	 * 시스템 가용성 검사: 1) 네트워크 확인 2) 로그인 확인 3) snmp 확인 4) 버전 확인
	 */
	// @Override
	// public Integer isReachable(Integer type, String ipaddress, String account,
	// String password, String cliAccount, String cliPassword, String swVersion,
	// String snmpCommunity, int connService, int connPort) throws OBException
	// {
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. ipaddress:%s,
	// password:%s, snmp:%s", ipaddress, password, snmpCommunity));
	//
	// try
	// {
	// if(ipaddress.isEmpty())
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "invalid
	// parameter");
	// }
	//
	// // 네트워크 접근성만 확인하기 때문에 서비스를 파악하지 않고 telnet으로 포트 연결 확인만한다.
	// if(new OBTelnetCmndExecV2().isReachable(ipaddress, connPort)==false)
	// {
	// return OBDefine.CHECK_ADC_UNREACHABLE;
	// }
	//
	// Integer adcType = OBCommon.checkAdcType(ipaddress, account, password,
	// cliAccount, cliPassword, type, connService, connPort);
	// // 로그인 시도하여 결과를 리턴한다.
	// OBAdcSystemInfo info;
	// switch(adcType)
	// {
	// case OBDefine.ADC_TYPE_F5:
	// info = new OBAdcSystemInfoF5();
	// break;
	// case OBDefine.ADC_TYPE_ALTEON:
	// info = new OBAdcSystemInfoAlteon();
	// break;
	// case OBDefine.ADC_TYPE_PIOLINK_PAS:
	// info = new OBAdcSystemInfoPAS();
	// break;
	// case OBDefine.ADC_TYPE_PIOLINK_PASK:
	// info = new OBAdcSystemInfoPASK();
	// break;
	// default:
	// throw new OBException(OBException.ERRCODE_NOT_SUPPORT_VENDOR);
	// // throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not
	// supported version"); //"지원되지 않은 장비입니다. 다시 확인하시기 바랍니다.";
	// }
	//
	// try
	// {
	// if(info.isAvailableSystem(ipaddress, account, password, connService,
	// connPort, cliAccount, cliPassword) == false)
	// {
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
	// "login fail"));
	// return OBDefine.CHECK_ADC_LOGINFAIL;
	// }
	// }
	// catch(OBExceptionUnreachable e1)
	// {
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%b",
	// false));
	// return OBDefine.CHECK_ADC_UNREACHABLE;
	// }
	// catch(OBExceptionLogin e1)
	// {
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
	// "login fail"));
	// if(e1.getMessage() == OBException.ERRCODE_SYSTEM_CLILOGIN)
	// {
	// return OBDefine.CHECK_ADC_CLILOGINFAIL;
	// }
	// return OBDefine.CHECK_ADC_LOGINFAIL;
	// }
	// catch(OBException e1)
	// {
	// throw e1;
	// }
	// catch(Exception e1)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//,
	// e1.getMessage());
	// }
	//
	// // snmp 조회.
	// try
	// {
	// info.getAdcHostName(ipaddress, swVersion, snmpCommunity);
	// }
	// catch(OBException e)
	// {
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
	// "snmp error"));
	// return OBDefine.CHECK_ADC_SNMPCOMMERR;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//,
	// e.getMessage());
	// }
	//
	// // version 검사.
	// try
	// {
	// String adcSWVersion;
	// boolean isValidVersion = true;
	// switch(adcType)
	// {
	// case OBDefine.ADC_TYPE_F5:
	// adcSWVersion = info.getAdcSWVersionSnmp(ipaddress, snmpCommunity);
	// isValidVersion = OBCommon.checkVersionF5(adcSWVersion);
	//
	// break;
	// case OBDefine.ADC_TYPE_ALTEON:
	// adcSWVersion = info.getAdcSWVersionSnmp(ipaddress, snmpCommunity);
	// isValidVersion = OBCommon.checkVersionAlteon(adcSWVersion);
	// break;
	// case OBDefine.ADC_TYPE_PIOLINK_PAS:
	// adcSWVersion = info.getAdcSWVersionCli(ipaddress, account, password, "",
	// connService, connPort);
	// isValidVersion = OBCommon.checkVersionPAS(adcSWVersion);
	// break;
	// case OBDefine.ADC_TYPE_PIOLINK_PASK:
	// adcSWVersion = info.getAdcSWVersionCli(ipaddress, account, password, "",
	// connService, connPort);
	// isValidVersion = OBCommon.checkVersionPASK(adcSWVersion);
	// break;
	// default:
	// return OBDefine.CHECK_ADC_VERSIONERR;
	// }
	// if(isValidVersion==false)
	// return OBDefine.CHECK_ADC_VERSIONERR;
	// }
	// catch(OBException e)
	// {
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
	// "snmp error"));
	// return OBDefine.CHECK_ADC_VERSIONERR;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//,
	// e.getMessage());
	// }
	//
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
	// "normal"));
	// return OBDefine.CHECK_ADC_NORMAL;
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	@Override
	public Integer getGroupAdcCount(Integer groupIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		int adcCount;
		try {
			db.openDB();

			adcCount = getGroupAdcCountCore(groupIndex, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return adcCount;
	}

	public int getGroupAdcCountCore(Integer groupIndex, OBDatabase db) throws OBException {
		String sqlText = "";
		int adcCount = 0;

		try {
			sqlText = String.format(" SELECT COUNT(INDEX) FROM MNG_ADC WHERE GROUP_INDEX = %d AND AVAILABLE = %d ;",
					groupIndex, OBDefine.ADC_STATE.AVAILABLE);

			ResultSet rs;
			rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return 0;
			}

			adcCount = db.getInteger(rs, "COUNT");

		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return adcCount;
	}

	// group에 속한 ADC index 목록만 뽑는다. getAdcIndexListInGroup()은 DB연결도 두개고, 조회에 join이
	// 있고, 데이터도 무겁다. 인덱스만 필요할 때 이 함수로 쓴다.
	public ArrayList<Integer> getAdcIndexListInGroup(Integer groupIndex, Integer accountIndex) throws OBException {
		ArrayList<Integer> list = new ArrayList<Integer>();

		String sqlText = "";
		ResultSet rs;
		String sqlAdcInSubquery = "";

		Integer roleNo = new OBAccountImpl().getAccountRoleNo(accountIndex);
		if (roleNo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);// , String.format( "accountIndex =%d",
																			// accountIndex));
		}
		if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
			sqlAdcInSubquery = String.format(" SELECT INDEX FROM MNG_ADC ");
		} else {
			sqlAdcInSubquery = String.format(" SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %d ",
					accountIndex);
		}

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT INDEX " + " FROM MNG_ADC "
							+ " WHERE INDEX IN (%s) AND AVAILABLE = %d AND GROUP_INDEX = %d ", // where-in:empty string
																								// 불가, null 불가, OK
					sqlAdcInSubquery, OBDefine.ADC_STATE.AVAILABLE, groupIndex);

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				list.add(db.getInteger(rs, "INDEX"));
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
		return list;
	}

	// group에 속한 ADC index 목록만 뽑는다. getAdcIndexListInGroup()은 DB연결도 두개고, 조회에 join이
	// 있고, 데이터도 무겁다. 인덱스만 필요할 때 이 함수로 쓴다.
	public ArrayList<Integer> getAdcIndexListInGroup(Integer groupIndex) throws OBException {
		ArrayList<Integer> list = new ArrayList<Integer>();

		String sqlText = "";
		ResultSet rs;

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT INDEX " + " FROM MNG_ADC " + " WHERE  AVAILABLE = %d AND GROUP_INDEX = %d ", // where-in:empty
																											// string
																											// 불가, null
																											// 불가, OK
					OBDefine.ADC_STATE.AVAILABLE, groupIndex);

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				list.add(db.getInteger(rs, "INDEX"));
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
		return list;
	}

	// group에 속한 ADC index 목록만 뽑는다. 비슷한 이름의 함수 getAdcIndexListInGroup()과 같은 기능을 하나
	// 문자열을 리턴한다.
	// 그룹단위의 ADC 작업을 하는 sql에서 IN-clause에 유용할 수 있다.
	public String getAdcIndexListStringInGroup(Integer groupIndex, Integer accountIndex) throws OBException {
		ArrayList<Integer> groups = getAdcIndexListInGroup(groupIndex, accountIndex);
		return OBParser.convertList2CommaString(groups); // "1,2,3,4"를 리턴한다. 없으면 ""
	}

	/**
	 * 현재 사용자의 전체, 선택 그룹, 개별 ADCIndex를 리턴한다. 개별 ADC인 경우 이 함수를 쓸 필요가 없지만 구색을 맞춘다.
	 * 
	 * @param category     : 전체, group, (single)ADC
	 * @param objectIndex  : group의 index, adc의 index
	 * @param accountIndex : 사용자 계정
	 * @param adcType      : adc유형
	 * @return ADCIndex(Integer) ArrayList
	 */
	public ArrayList<Integer> getUsersAdcIndex(Integer category, Integer objectIndex, Integer accountIndex)
			throws OBException {
		return getUsersAdcIndexForAdcType(category, objectIndex, accountIndex, null);
	}

	public ArrayList<Integer> getUsersAdcIndexForAdcType(Integer category, Integer objectIndex, Integer accountIndex,
			ArrayList<Integer> adcType) throws OBException {
		OBDatabase db = new OBDatabase();

		ArrayList<Integer> list = new ArrayList<Integer>();
		String sqlText = "";
		String sqlAdcScope = "";
		try {
			sqlText = " SELECT INDEX FROM MNG_ADC WHERE AVAILABLE = 1 "; // base sql

			// 대상이 되는 ADC 조건. CATEGORY_ALL 인 경우는 대상의 식별이 필요 없다.
			if (category.equals(OBDtoADCObject.CATEGORY_GROUP)) {
				sqlAdcScope = String.format(" GROUP_INDEX IN (%d) ", objectIndex);
			} else if (category.equals(OBDtoADCObject.CATEGORY_ADC)) {
				sqlAdcScope = String.format(" INDEX = %d ", objectIndex);
			}

			// 사용자 조건
			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accountIndex);
			if (roleNo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);// , String.format( "accountIndex =%d",
																				// accountIndex));
			}
			if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN) == false) // admin이 아닌 사용자라면
			{
				sqlText += String.format(
						" AND INDEX IN (SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %d ) ",
						accountIndex);
			}

			// adcType 조건
			String sqlType = "";
			if (adcType != null && adcType.size() > 0) // adc 유형 조건 유효함 . null이거나 없으면 adc 유형 조건 없음. 패스
			{
				for (Integer type : adcType) {
					if (sqlType.isEmpty() == false) {
						sqlType += ", ";
					}
					sqlType += type;
				}
				if (sqlType.isEmpty() == false) {
					sqlText += String.format(" AND TYPE IN (%s) ", sqlType);
				}
			}

			if (sqlAdcScope.isEmpty() == false) {
				sqlText += (" AND " + sqlAdcScope);
			}

			ResultSet rs;

			db.openDB();

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				list.add(db.getInteger(rs, "INDEX"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}
		return list;
	}

	// category와 사용자 계정(자격)에 맞는 ADC index 목록만 뽑는다. 비슷한 이름의 함수 getUsersAdcIndex()와 같은
	// 기능을 하나 문자열을 리턴한다.
	// 그룹/전체단위의 ADC 작업을 하는 sql에서 IN-clause에 유용할 수 있다.
	public String getUsersAdcIndexString(Integer category, Integer objectIndex, Integer accountIndex)
			throws OBException {
		ArrayList<Integer> adcs = getUsersAdcIndex(category, objectIndex, accountIndex); // adcType = null, type상관없음
		return OBParser.convertList2CommaString(adcs); // "1,2,3,4"를 리턴한다. 없으면 ""
	}

	// category와 사용자 계정(자격)에 맞는 ADC index 목록만 뽑는다. 비슷한 이름의 함수 getUsersAdcIndex()와 같은
	// 기능을 하나 문자열을 리턴한다.
	// 그룹/전체단위의 ADC 작업을 하는 sql에서 IN-clause에 유용할 수 있다.
	public ArrayList<Integer> getUsersAdcIndexInteger(Integer category, Integer objectIndex, Integer accountIndex)
			throws OBException {
		ArrayList<Integer> adcs = getUsersAdcIndex(category, objectIndex, accountIndex); // adcType = null, type상관없음
		return adcs; // "1,2,3,4"를 리턴한다. 없으면 ""
	}

	// 위 getUsersAdcIndexString()과 같은 기능인데 ADC 유형 조건을 추가로 넣었다. 일부는 유형별로 뽑을 필요도 있다.
	// 내부에서 처리하는 함수는 getUsersAdcIndexString()과 같다.
	public String getUsersAdcIndexStringForAdcType(Integer category, Integer objectIndex, Integer accountIndex,
			ArrayList<Integer> adcType, OBDatabase db) throws OBException {
		ArrayList<Integer> adcs = getUsersAdcIndexForAdcType(category, objectIndex, accountIndex, adcType); // adcType =
																											// null,
																											// type상관없음
		return OBParser.convertList2CommaString(adcs); // "1,2,3,4"를 리턴한다. 없으면 ""
	}

	@Override
	public ArrayList<OBDtoAdcInfo> getAdcInfoListInGroup(Integer groupIndex, Integer accountIndex) throws OBException {
		ArrayList<OBDtoAdcInfo> list = new ArrayList<OBDtoAdcInfo>();

		OBDatabase db = new OBDatabase();
		OBDatabase db2 = new OBDatabase();

		try {
			db.openDB();
			db2.openDB();
			list = getAdcInfoListInGroup(groupIndex, accountIndex, "", OBDefine.ORDER_TYPE_SECOND,
					OBDefine.ORDER_DIR_ASCEND, null, db, db2);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
			if (db2 != null)
				db2.closeDB();
		}
		return list;
	}

	private String getAdcInfoOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY A.OCCUR_TIME DESC NULLS LAST ";

		switch (orderType) {
		case OBDefine.ORDER_TYPE_FIRST:// 상태
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY A.STATUS ASC NULLS LAST , A.NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY A.STATUS DESC NULLS LAST , A.NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SECOND:// ADC이름.
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY A.NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY A.NAME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_THIRD:// 종류.
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY A.TYPE ASC NULLS LAST, A.NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY A.TYPE DESC NULLS LAST, A.NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_FOURTH:// IP.
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INET(A.IPADDRESS) ASC NULLS LAST, A.NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY INET(A.IPADDRESS) DESC NULLS LAST, A.NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_FIFTH:// 최근업데이트.
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY A.APPLY_TIME ASC NULLS LAST, A.NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY A.APPLY_TIME DESC NULLS LAST, A.NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SIXTH:// 버전.
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY A.SW_VERSION ASC NULLS LAST, A.NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY A.SW_VERSION DESC NULLS LAST, A.NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SEVENTH:// 설명.
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY A.CMT ASC NULLS LAST, A.NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY A.CMT DESC NULLS LAST, A.NAME AS NULLS LAST ";
			break;
		}
		return retVal;
	}

	private ArrayList<OBDtoAdcInfo> getAdcInfoListInGroup(Integer groupIndex, Integer accountIndex, String searchKey,
			Integer orderType, Integer orderDir, Integer adcListPageOption, OBDatabase db, OBDatabase db2)
			throws OBException {
		ArrayList<OBDtoAdcInfo> tmpAdcList = new ArrayList<OBDtoAdcInfo>();
		ArrayList<OBDtoAdcInfo> retVal = new ArrayList<OBDtoAdcInfo>();
		String sqlText = "";
		String sqlText2 = "";
		ResultSet rs;
		ResultSet rs2;
		String sqlAdcInSubquery = "";

		Integer roleNo = new OBAccountImpl().getAccountRoleNo(accountIndex);
		if (roleNo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);// , String.format( "accountIndex =%d",
																			// accountIndex));
		}

		try {
			if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
				sqlAdcInSubquery = String.format(" SELECT INDEX FROM MNG_ADC ");
			} else {
				sqlAdcInSubquery = String.format(" SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %d ",
						accountIndex);
			}

			sqlText = String.format(
					" SELECT A.INDEX, A.NAME, A.IPADDRESS, A.ACCNT, A.PASSWORD, A.CLI_ACCNT, A.MODEL, A.CLI_ACCNT, A.CLI_PASSWORD, \n"
							+ " A.TYPE, A.GROUP_INDEX, A.DESCRIPTION, A.ACTIVE_PAIR_INDEX, A.SW_VERSION, A.HOST_NAME, A.APPLY_TIME,          \n"
							+ " A.SAVE_TIME, A.LAST_BOOT_TIME, A.STATUS, A.PURCHASE_TIME, A.PEER_IPADDRESS,                                  \n"
							+ " COALESCE(B.ACTIVE_BACKUP_STATE, 0) AS ACTIVE_BACKUP_STATE, B.DISCONN_TIME, A.ROLE,                           \n"
							+ " A.SNMP_RCOMM, A.SNMP_VERSION, A.SNMP_USER, A.SNMP_AUTH_PASSWORD, A.SNMP_PRIV_PASSWORD, A.SNMP_AUTH_PROTOCOL, \n"
							+ " A.SNMP_PRIV_PROTOCOL, A.OP_MODE, A.CONN_PROTOCOL                                                             \n"
							+ " FROM MNG_ADC                 A                                                                               \n"
							+ " LEFT JOIN MNG_ADC_ADDITIONAL B                                                                               \n"
							+ " ON B.ADC_INDEX = A.INDEX                                                                                     \n"
							+ " WHERE A.INDEX IN (%s) AND A.AVAILABLE = %d AND A.GROUP_INDEX = %d                                            \n",
					sqlAdcInSubquery, OBDefine.ADC_STATE.AVAILABLE, groupIndex);

			if (adcListPageOption != null) {
				if (searchKey != null && searchKey.isEmpty() == false) {
					// #3984-2 #9: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
					String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
					sqlText += String.format(" AND ( A.NAME LIKE %s ) ", OBParser.sqlString(wildcardKey));
				}
			} else {
				if (searchKey != null && searchKey.isEmpty() == false) {
					// #3984-2 #9: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
					String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
					sqlText += String.format(" AND ( A.NAME LIKE %s OR  A.IPADDRESS LIKE %s ) ",
							OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
				}
			}

			sqlText += getAdcInfoOrderType(orderType, orderDir);// " ORDER BY NAME;";

			rs = db.executeQuery(sqlText);

			HashMap<String, OBDtoAdcInfo> peerMap = new HashMap<String, OBDtoAdcInfo>();
			while (rs.next()) {
				OBDtoAdcInfo info = new OBDtoAdcInfo();

				info.setIndex(db.getInteger(rs, "INDEX"));
				info.setName(db.getString(rs, "NAME"));
				info.setAdcIpAddress(db.getString(rs, "IPADDRESS"));
				info.setAdcAccount(db.getString(rs, "ACCNT"));
				info.setAdcPassword(db.getString(rs, "PASSWORD"));
				info.setAdcCliAccount(db.getString(rs, "CLI_ACCNT"));
				info.setAdcCliPassword(db.getString(rs, "CLI_PASSWORD"));
				info.setModel(db.getString(rs, "MODEL"));
				info.setAdcType(db.getInteger(rs, "TYPE"));
				info.setGroupIndex(db.getInteger(rs, "GROUP_INDEX"));
				info.setDescription(db.getString(rs, "DESCRIPTION"));
				info.setActivePairIndex(db.getInteger(rs, "ACTIVE_PAIR_INDEX"));
				info.setSwVersion(db.getString(rs, "SW_VERSION"));
				info.setHostName(db.getString(rs, "HOST_NAME"));
				info.setApplyTime(db.getTimestamp(rs, "APPLY_TIME"));
				info.setSaveTime(db.getTimestamp(rs, "SAVE_TIME"));
				info.setLastBootTime(db.getTimestamp(rs, "LAST_BOOT_TIME"));
				info.setStatus(db.getInteger(rs, "STATUS"));
				info.setPeerAdcIPAddress(db.getString(rs, "PEER_IPADDRESS"));
				info.setActiveStandbyState(db.getInteger(rs, "ACTIVE_BACKUP_STATE"));
				info.setLastDisconnectedTime(db.getTimestamp(rs, "DISCONN_TIME"));
				info.setRoleFlbYn(getFlbYesNo(db.getInteger(rs, "ROLE")));

				OBDtoAdcSnmpInfo snmpInfo = new OBDtoAdcSnmpInfo();
				snmpInfo.setVersion(db.getInteger(rs, "SNMP_VERSION"));
				snmpInfo.setRcomm(db.getString(rs, "SNMP_RCOMM"));
				if (snmpInfo.getRcomm() == null || snmpInfo.getRcomm().isEmpty()) {
					snmpInfo.setRcomm(OBDefine.DEFAULT_SNMP_RCOMM);
				}
				snmpInfo.setSecurityName(db.getString(rs, "SNMP_USER"));
				snmpInfo.setAuthProto(db.getString(rs, "SNMP_AUTH_PROTOCOL"));
				snmpInfo.setAuthPassword(db.getString(rs, "SNMP_AUTH_PASSWORD"));
				snmpInfo.setPrivProto(db.getString(rs, "SNMP_PRIV_PROTOCOL"));
				snmpInfo.setPrivPassword(db.getString(rs, "SNMP_PRIV_PASSWORD"));
				info.setSnmpInfo(snmpInfo);
				info.setOpMode(db.getInteger(rs, "OP_MODE"));
				info.setConnProtocol(db.getInteger(rs, "CONN_PROTOCOL"));

				sqlText2 = String.format("SELECT ACCNT_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ADC_INDEX = %d;",
						info.getIndex());
				rs2 = db2.executeQuery(sqlText2);

				ArrayList<Integer> temp = new ArrayList<Integer>();
				while (rs2.next()) {
					temp.add(db2.getInteger(rs2, "ACCNT_INDEX"));
				}
				info.setAccountIndexList(temp);
				tmpAdcList.add(info);

				peerMap.put(info.getAdcIpAddress(), info);
			}

			// active/standby를 한쌍으로 묶는다.
			for (OBDtoAdcInfo info : tmpAdcList) {
				if (info.getPeerAdcIPAddress() != null && !info.getPeerAdcIPAddress().isEmpty()) {// peer ip가 설정되어 있는
																									// 경우..
					if (peerMap.get(info.getAdcIpAddress()) != null) {
						OBDtoAdcInfo peerInfo = peerMap.get(info.getPeerAdcIPAddress());
						if (peerInfo != null) {
							if (peerInfo.getPeerAdcIPAddress() == null || peerInfo.getPeerAdcIPAddress().isEmpty()) {// peer
																														// 장비의
																														// peer
																														// IP가
																														// 설정
																														// 안된
																														// 상태.
																														// 한쪽
																														// 장비만
																														// 설정되어
																														// 있는
																														// 상태.
								info.setActiveStandbyDirection(OBDtoAdcInfo.ACTIVE_STANDBY_DIR_UNKNOWN);
								retVal.add(info);
								String msg = String.format("%s<->%s", info.getAdcIpAddress(),
										peerInfo.getAdcIpAddress());
								new OBSystemAuditImpl().writeLog(0, "localhost",
										OBSystemAudit.AUDIT_INVALID_ADCINFO_PEERIP, msg);
								OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("invalid peer ip(%s)", msg));
								continue;
							}

							if (!peerInfo.getPeerAdcIPAddress().equals(info.getAdcIpAddress())) {// peer 장비간 설정된 peer IP
																									// 정보가 불일치할 때.
								info.setActiveStandbyDirection(OBDtoAdcInfo.ACTIVE_STANDBY_DIR_UNKNOWN);
								retVal.add(info);
								String msg = String.format("%s<->%s", info.getAdcIpAddress(),
										peerInfo.getAdcIpAddress());
								new OBSystemAuditImpl().writeLog(0, "localhost",
										OBSystemAudit.AUDIT_INVALID_ADCINFO_PEERIP, msg);
								OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("invalid peer ip(%s)", msg));
								continue;
							}
							info.setActiveStandbyDirection(OBDtoAdcInfo.ACTIVE_STANDBY_DIR_DOWN);
							peerInfo.setActiveStandbyDirection(OBDtoAdcInfo.ACTIVE_STANDBY_DIR_UP);
							retVal.add(info);
							retVal.add(peerInfo);
							peerMap.remove(info.getPeerAdcIPAddress());
						} else {
							info.setActiveStandbyDirection(OBDtoAdcInfo.ACTIVE_STANDBY_DIR_UNKNOWN);
							retVal.add(info);
						}
						peerMap.remove(info.getAdcIpAddress());
					}
				} else {
					info.setActiveStandbyDirection(OBDtoAdcInfo.ACTIVE_STANDBY_DIR_UNKNOWN);
					retVal.add(info);
				}
			}

		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return retVal;
	}

	private Integer getAdcIndex(String adcName, String ipaddress) throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT INDEX " + " FROM MNG_ADC " + " WHERE NAME=%s AND IPADDRESS=%s AND AVAILABLE = %d; ",
					OBParser.sqlString(adcName), OBParser.sqlString(ipaddress), OBDefine.ADC_STATE.AVAILABLE);

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("not existed adc(%s)", adcName));
			}
			return db.getInteger(rs, "INDEX");
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

	@Override
	public boolean isExistAdc(String adcNameIP) throws OBException // dbcp 적용
	{
		boolean result;
		try {
			result = isExistAdcCore(adcNameIP, adcNameIP);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return result;
	}

	// DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할 예정이다.
	public boolean isExistAdcCore(String adcName, String adcIPAddress) throws OBException {
		OBDatabase db = new OBDatabase();

		String sqlText = "";

		if (adcName == null) {
			adcName = ""; // 검색 query에 null이 들어가는 것을 방지
		}
		if (adcIPAddress == null) {
			adcIPAddress = ""; // 검색 query에 null이 들어가는 것을 방지
		}

		if (adcName.isEmpty() == true && adcIPAddress.isEmpty() == true) {
			return false;
		}

		try {
			db.openDB();

			sqlText = String.format(
					" SELECT INDEX " + " FROM MNG_ADC " + " WHERE (NAME=%s OR IPADDRESS=%s) AND AVAILABLE = %d;",
					OBParser.sqlString(adcName), OBParser.sqlString(adcIPAddress), OBDefine.ADC_STATE.AVAILABLE);

			ResultSet rs;

			rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return false;
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

		return true;
	}

	// ADC index가 있는지 확인한다.
	// ADC index만 확인하고 싶을 때, getAdcInfo()로도 확인할 수 있지만 모든 정보를 무겁게 구하지 않고 index 확인만 할
	// 때 쓴다.
	// 그리고 getAdcInfo()는 true/false를 주지 않기 때문에 확인이 복잡하기도 하다.
	public boolean isExistAdcIndex(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();

		boolean result;
		try {
			db.openDB();
			result = isExistAdcIndex(adcIndex, db);
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

	public boolean isExistAdcIndex(Integer adcIndex, OBDatabase db) throws OBException {
		String sqlText = "";

		if (adcIndex == null) {
			return false;
		}

		try {
			sqlText = String.format(" SELECT INDEX FROM MNG_ADC " + " WHERE INDEX=%d AND AVAILABLE = %d;", adcIndex,
					OBDefine.ADC_STATE.AVAILABLE);

			ResultSet rs;

			rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return false;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return true;
	}

	public void decreaseAdcCheckCfg(Integer adcIndex, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. threadID:%d", Thread.currentThread().getId()));

		// 데이터 수정.
		String sqlText = "";
		try {
			int retVal = 0;
			sqlText = String.format(" UPDATE MNG_ADC SET CHECK_CFG=%d WHERE INDEX=%d; ", retVal, adcIndex);

			db.executeUpdate(sqlText);
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("ADC [%d] -
			// MNG_ADC.CHECK_CFG = %d", adcIndex, retVal)); //유용한 로그, 지우지 말기!
		} catch (SQLException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("%s: %s, callstack:%s", sqlText, e.getMessage(), new OBUtility().getStackTrace()));
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("%s: %s, callstack:%s", "failed to update db",
					e.getMessage(), new OBUtility().getStackTrace()));
		}
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public void decreaseAdcCheckCfgNew(Integer adcIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. threadID:%d", Thread.currentThread().getId()));

		// 데이터 수정.
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			int retVal = 0;
			sqlText = String.format(" UPDATE MNG_ADC SET CHECK_CFG=%d WHERE INDEX=%d; ", retVal, adcIndex);

			db.executeUpdate(sqlText);
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("ADC [%d] -
			// MNG_ADC.CHECK_CFG = %d", adcIndex, retVal)); //유용한 로그, 지우지 말기!
		} catch (SQLException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("%s: %s, callstack:%s", sqlText, e.getMessage(), new OBUtility().getStackTrace()));
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("%s: %s, callstack:%s", "failed to update db",
					e.getMessage(), new OBUtility().getStackTrace()));
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void decreaseAdcCheckCfg(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			decreaseAdcCheckCfg(adcIndex, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public Integer getAdcCheckCfg(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		Integer flag;
		try {
			db.openDB();
			flag = getAdcCheckCfg(adcIndex, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return flag;
	}

	private void crossProcessLockRelease(Integer adcIndex, OBDtoProcessLockSpin lockSpin) {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. threadID:%d", Thread.currentThread().getId()));
		if (lockSpin != null && lockSpin.getFileLock() != null) {
			try {
				lockSpin.getFileLock().release();
				if (lockSpin.getFileChannel() != null)
					lockSpin.getFileChannel().close();
				if (lockSpin.getFile() != null)
					lockSpin.getFile().delete();
				if (lockSpin.getRandomAccessFile() != null)
					lockSpin.getRandomAccessFile().close();
			} catch (Exception e) {
				if (lockSpin.getFile() != null)
					lockSpin.getFile().delete();
				e.printStackTrace();
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. threadID:%d", Thread.currentThread().getId()));
	}

	// Acquire - Returns success ( true/false )
	private OBDtoProcessLockSpin crossProcessLockAcquire(long waitMS, Integer adcIndex, boolean stopFlag) {
		OBDtoProcessLockSpin retVal = new OBDtoProcessLockSpin();
		FileLock fileLock = null;
		FileChannel fileChannel = null;
		File file = null;
		RandomAccessFile randomAccessFile = null;
		if (stopFlag == true)
			return null;
		// if (fileLock == null && waitMS > 0)
		{
			try {
				String fileName = String.format("%s/.adcms.lock.%d", lockTempDir, adcIndex);
				long dropDeadTime = System.currentTimeMillis() + waitMS;
				file = new File(lockTempDir, fileName);
				randomAccessFile = new RandomAccessFile(file, "rw");
				fileChannel = randomAccessFile.getChannel();
				while (System.currentTimeMillis() < dropDeadTime) {
					fileLock = fileChannel.tryLock();
					if (fileLock != null) {
						break;
					}
					Thread.sleep(250); // 4 attempts/sec
				}
				if (fileLock == null) {
					fileChannel.close();
				}
				randomAccessFile.close();
			} catch (Exception e) {
				OBSystemLog.info(OBDefine.LOGFILE_ADCMON,
						String.format("failed to get a lock spin. %s", OBUtility.getStackTrace(e)));
				try {
					if (fileChannel != null)
						fileChannel.close();
					if (fileLock != null)
						fileLock.release();
					if (randomAccessFile != null)
						randomAccessFile.close();
				} catch (IOException e1) {
				}

				if (stopFlag == false)
					return crossProcessLockAcquire(waitMS, adcIndex, true);

			}
		}

		if (fileLock != null) {
			retVal.setFile(file);
			retVal.setFileChannel(fileChannel);
			retVal.setFileLock(fileLock);
			retVal.setRandomAccessFile(randomAccessFile);
			return retVal;
		} else
			return null;
		// return fileLock == null ? false : true;
	}

	private Integer increaseAdcCheckCfg(Integer adcIndex, OBDatabase db) throws OBException {
		// 데이터 수정.
		String sqlText = "";
		int retVal = 0;
		OBDtoProcessLockSpin lockSpin = null;
		try {
			lockSpin = crossProcessLockAcquire(60000, adcIndex, false);
			if (lockSpin == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						"failed to acquire process lock. adcIndex: " + adcIndex);
			}

			sqlText = String.format(" SELECT CHECK_CFG FROM MNG_ADC WHERE INDEX=%d;", adcIndex);

			ResultSet rs;
			rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				crossProcessLockRelease(adcIndex, lockSpin);
				return 0;
			}
			retVal = db.getInteger(rs, "CHECK_CFG");
			if (retVal != 0) {
				crossProcessLockRelease(adcIndex, lockSpin);
				return retVal;
			}
			sqlText = String.format(" UPDATE MNG_ADC SET CHECK_CFG=%d WHERE INDEX=%d; ", retVal + 1, adcIndex);
			db.executeUpdate(sqlText);
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("ADC [%d] -
			// MNG_ADC.CHECK_CFG = %d", adcIndex, (retVal+1))); //유용한 로그, 지우지 말기!
		} catch (SQLException e) {
			crossProcessLockRelease(adcIndex, lockSpin);
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			crossProcessLockRelease(adcIndex, lockSpin);
			throw e;
		} catch (Exception e) {
			crossProcessLockRelease(adcIndex, lockSpin);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		crossProcessLockRelease(adcIndex, lockSpin);
		return retVal;
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private Integer increaseAdcCheckCfg(Integer adcIndex) throws OBException {
		// 데이터 수정.
		String sqlText = "";
		int retVal = 0;
		OBDtoProcessLockSpin lockSpin = null;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			lockSpin = crossProcessLockAcquire(60000, adcIndex, false);
			if (lockSpin == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						"failed to acquire process lock. adcIndex: " + adcIndex);
			}

			sqlText = String.format(" SELECT CHECK_CFG FROM MNG_ADC WHERE INDEX=%d;", adcIndex);

			ResultSet rs;
			rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				crossProcessLockRelease(adcIndex, lockSpin);
				return 0;
			}
			retVal = db.getInteger(rs, "CHECK_CFG");
			if (retVal != 0) {
				crossProcessLockRelease(adcIndex, lockSpin);
				return retVal;
			}
			sqlText = String.format(" UPDATE MNG_ADC SET CHECK_CFG=%d WHERE INDEX=%d; ", retVal + 1, adcIndex);
			db.executeUpdate(sqlText);
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("ADC [%d] -
			// MNG_ADC.CHECK_CFG = %d", adcIndex, (retVal+1))); //유용한 로그, 지우지 말기!
		} catch (SQLException e) {
			crossProcessLockRelease(adcIndex, lockSpin);
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			crossProcessLockRelease(adcIndex, lockSpin);
			throw e;
		} catch (Exception e) {
			crossProcessLockRelease(adcIndex, lockSpin);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		crossProcessLockRelease(adcIndex, lockSpin);
		return retVal;
	}

	private Integer getAdcCheckCfg(Integer adcIndex, OBDatabase db) throws OBException {
		// 데이터 수정.
		String sqlText = "";
		try {
			sqlText = String.format(" SELECT CHECK_CFG FROM MNG_ADC WHERE INDEX=%d;", adcIndex);

			ResultSet rs;

			rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return 0;
			}
			return db.getInteger(rs, "CHECK_CFG");
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private Integer getAdcCheckCfgNew(Integer adcIndex) throws OBException {
		// 데이터 수정.
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" SELECT CHECK_CFG FROM MNG_ADC WHERE INDEX=%d;", adcIndex);

			ResultSet rs;

			rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return 0;
			}
			return db.getInteger(rs, "CHECK_CFG");
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

	public void waitUntilAdcAvailable(Integer adcIndex, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. threadID:%d", Thread.currentThread().getId()));
		Integer checkFlag = increaseAdcCheckCfg(adcIndex, db);
		if (checkFlag == 0) {
			return;
		}
		for (int i = 0; i < 120; i++) {
			try {
				checkFlag = getAdcCheckCfg(adcIndex, db);
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			if (checkFlag == 0) {
				OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
						String.format("end. threadID:%d", Thread.currentThread().getId()));
				return;
			}
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
					String.format("wait #%d. threadID:%d", i, Thread.currentThread().getId()));
			OBDateTime.Sleep(500);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("timeout. threadID:%d", Thread.currentThread().getId()));
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public void waitUntilAdcAvailableNew(Integer adcIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. threadID:%d", Thread.currentThread().getId()));
		Integer checkFlag = increaseAdcCheckCfg(adcIndex);
		if (checkFlag == 0) {
			return;
		}
		for (int i = 0; i < 120; i++) {
			try {
				checkFlag = getAdcCheckCfgNew(adcIndex);
			} catch (Exception e) {
				throw new OBException(e.getMessage());
			}
			if (checkFlag == 0) {
				OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
						String.format("end. threadID:%d", Thread.currentThread().getId()));
				return;
			}
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
					String.format("wait #%d. threadID:%d", i, Thread.currentThread().getId()));
			OBDateTime.Sleep(500);
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("timeout. threadID:%d", Thread.currentThread().getId()));
	}

	public void waitUntilAdcAvailable(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			waitUntilAdcAvailable(adcIndex, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// // OBDatabase db = new OBDatabase();
	// // db.openDB();
	// //Integer allocVServerID(Integer adcIndex, OBDatabase db)
	// ArrayList<OBDtoAuditLogAdcSystem> list = new
	// OBAdcManagementImpl().getAuditLogAdcSystem(1, "", null, null, null, null);
	// // OBDtoAdcVServerF5 getVServerInfoF5(Integer vsIndex, OBDatabase db)
	// // OBDtoAdcVServerF5 obj = new OBVServerDB().getVServerInfoF5(17, db);
	// System.out.println(list);
	// // db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public ArrayList<OBDtoAuditLogAdcSystem> getAdcAuditLog(Integer adcIndex, String searchKeys, Date beginTime,
			Date endTime, Integer recordCount) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. accountIndex:%d, search:%s, begine:%s, end:%s",
				adcIndex, searchKeys, beginTime, endTime));
		ArrayList<OBDtoAuditLogAdcSystem> result = new ArrayList<OBDtoAuditLogAdcSystem>();
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		try {
			db.openDB();

			String sqlLimit = "";
			if (recordCount == null || recordCount == 0)
				sqlLimit = String.format("LIMIT %d", 20);
			else if (recordCount.equals(0))
				sqlLimit = "";
			else
				sqlLimit = String.format("LIMIT %d", recordCount.intValue());

			String sqlSearch = null;
			if (searchKeys != null && !searchKeys.isEmpty()) {
				// #3984-2 #8: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKeys) + "%";
				sqlSearch = String.format(" ( B.NAME LIKE %s OR B.IPADDRESS LIKE %s OR A.EVENT LIKE %s ) ",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			}

			String sqlTime = "";
			if (endTime == null)
				sqlTime = String.format(" OCCUR_TIME <= %s ", OBParser.sqlString(OBDateTime.now()));
			else
				sqlTime = String.format(" OCCUR_TIME <= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));

			if (beginTime != null)
				sqlTime += String.format(" AND OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			sqlText = String
					.format("SELECT " + " A.OCCUR_TIME, A.ADC_INDEX, A.TYPE, A.LOG_LEVEL, A.EVENT, B.NAME, B.IPADDRESS "
							+ " FROM LOG_ADC_SYSLOG A INNER JOIN MNG_ADC B " + " ON A.ADC_INDEX = B.INDEX "
							+ " WHERE B.AVAILABLE = 1 AND A.ADC_INDEX=%d ", adcIndex);

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND" + sqlTime;

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			sqlText += " ORDER BY A.OCCUR_TIME DESC ";

			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAuditLogAdcSystem log = new OBDtoAuditLogAdcSystem();

				log.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				log.setAdcIPAddress(db.getString(rs, "IPADDRESS"));
				log.setAdcName(db.getString(rs, "NAME"));
				log.setContents(db.getString(rs, "EVENT"));
				log.setLevel(db.getString(rs, "LOG_LEVEL"));
				log.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				log.setType(convertLogType(db.getInteger(rs, "TYPE")));
				result.add(log);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", result.size()));
		return result;
	}

	private Integer getFlbYesNo(Integer role) {
		if ((role & OBDefine.ADC_ROLE_MASK.FLB) == OBDefine.ADC_ROLE_MASK.FLB) {
			return OBDefine.STATE_ENABLE;
		} else {
			return OBDefine.STATE_DISABLE;
		}
	}

	private String convertLogType(Integer type) {
		if (type.equals(OBDefine.LOG_TYPE_CONFIG))
			return "CONFIG";
		else if (type.equals(OBDefine.LOG_TYPE_LOGINOUT))
			return "LOGIN/LOGOUT";
		else if (type.equals(OBDefine.LOG_TYPE_SYSTEM))
			return "SYSTEM";
		else if (type.equals(OBDefine.LOG_TYPE_OTHERS))
			return "OTHERS";
		else
			return "SYSTEM";
	}

	private String convertLogLevel(Integer type) {
		if (type.equals(OBDefine.LOG_LEVEL_ERROR))
			return "ERROR";
		else if (type.equals(OBDefine.LOG_LEVEL_INFO))
			return "INFO";
		else if (type.equals(OBDefine.LOG_LEVEL_RISK))
			return "RISK";
		else if (type.equals(OBDefine.LOG_LEVEL_WARNING))
			return "WARNING";
		else
			return "INFO";
	}

	public void setAdcRoleFlb(Integer adcIndex, OBDatabase db) throws Exception // MNG_ADC.ROLE의 FLB 속성을 설정한다.
	{
		String sqlText = "";
		int groupNum = 0, role = 0;
		ResultSet rs;

		try {
			sqlText = String.format(" SELECT COUNT(*) FROM MNG_FLB_GROUP WHERE ADC_INDEX = %d; ", adcIndex);
			rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				groupNum = db.getInteger(rs, "COUNT");
			}

			// 지금의 role을 구해 놓는다.
			sqlText = String.format(" SELECT ROLE FROM MNG_ADC WHERE INDEX = %d; ", adcIndex);
			rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				role = db.getInteger(rs, "ROLE");
			}

			if (groupNum > 0) // 그룹이 있으면, ADC의 ROLE을 (지금slb가 아니라면) FLB로 설정한다.
			{
				if ((role & OBDefine.ADC_ROLE_MASK.FLB) != OBDefine.ADC_ROLE_MASK.FLB) // 아직 FLB가 아니라면 갱신한다. 이미 FLB면 할
																						// 필요 없다.
				{
					role = (role | OBDefine.ADC_ROLE_MASK.FLB);
					sqlText = String.format(" UPDATE MNG_ADC SET ROLE = %d WHERE INDEX = %d; ", role, adcIndex);
					db.executeUpdate(sqlText);
				}
			} else
			// 그룹이 없다. 지금 ADC가 FLB면 해제한다.
			{
				if ((role & OBDefine.ADC_ROLE_MASK.FLB) == OBDefine.ADC_ROLE_MASK.FLB) // FLB면 없앤다.
				{
					role = (role & OBDefine.ADC_ROLE_MASK.FLB_CLEAR);
					sqlText = String.format(" UPDATE MNG_ADC SET ROLE = %d WHERE INDEX = %d; ", role, adcIndex);
					db.executeUpdate(sqlText);
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public Integer getAdcTotalCount(Integer accountIndex) throws OBException {
		int result = 0;

		OBDatabase db = new OBDatabase();

		String sqlText = "";

		ResultSet rs;

		int roleNo = 0;

		try {
			db.openDB();
			if (accountIndex == null || accountIndex.equals(0)) // 비공식 루트, 문서상 규정과 다르게 데몬은 accountIndex null로 호출 할 수 있다.
			{
				roleNo = OBDefine.ACCNT_ROLE_ADMIN;
			} else {
				sqlText = String.format(" SELECT ROLE_NO FROM MNG_ACCNT WHERE INDEX = %d AND AVAILABLE = %d ; ",
						accountIndex, OBDefine.DATA_AVAILABLE);

				rs = db.executeQuery(sqlText);
				if (rs.next() == false) {
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String
							.format("Error: account has no role. accountInex=%d (sqlText=%s)", accountIndex, sqlText));
				}

				roleNo = db.getInteger(rs, "ROLE_NO");
			}

			sqlText = String.format(" SELECT COUNT(*) AS CNT " + " FROM MNG_ADC ");
			if (roleNo == OBDefine.ACCNT_ROLE_ADMIN) {
				sqlText += String.format(" WHERE AVAILABLE = %d ", OBDefine.ADC_STATE.AVAILABLE);
			} else {
				sqlText += String.format(
						" WHERE " + " INDEX IN (SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %s) " + // where-in:empty
																													// string
																													// 불가,
																													// null
																													// 불가,
																													// OK
								" AND AVAILABLE = %d ",
						accountIndex, OBDefine.ADC_STATE.AVAILABLE);
			}
			sqlText += ";";

			rs = db.executeQuery(sqlText);
			if (rs.next() == false)
				result = 0;
			else
				result = db.getInteger(rs, "CNT");

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

	// public static void main(String[] args)
	// {
	// try
	// {
	// ArrayList<OBDtoAuditLogAdcSystem> list = new
	// OBAdcManagementImpl().getAuditLogAdcSystem(1, 12, null, null, null, null,
	// null);
	// System.out.println(list);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public ArrayList<OBDtoAuditLogAdcSystem> getAdcAuditLogExOrdering(OBDtoADCObject adcObject,
			OBDtoSearch searchOption, OBDtoAdcLogSearchOption selectOption, Integer accountIndex) throws OBException {
		OBDtoOrdering orderOption = new OBDtoOrdering();
		orderOption.setOrderDirection(OBDefine.ORDER_DIR_DESCEND);
		orderOption.setOrderType(OBDefine.ORDER_TYPE_OCCURTIME);
		return getAdcAuditLog(adcObject, searchOption, orderOption, selectOption, accountIndex);
	}

	public OBDtoAuditLogAdcSystem getAdcAuditLogLast(Integer adcIndex, String searchKeys, Date beginTime, Date endTime,
			OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, searchKey:%s, beginTime:%s, endTime:%s", adcIndex, searchKeys,
						beginTime, endTime));
		OBDtoAuditLogAdcSystem result = new OBDtoAuditLogAdcSystem();
		String sqlText = "";
		try {
			int limit = 1;
			String sqlLimit = "";
			if (limit != 0)
				sqlLimit = String.format(" LIMIT %d ", limit);

			String sqlSearch = null;
			if (searchKeys != null && !searchKeys.isEmpty()) {
				// #3984-2 #8: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKeys) + "%";
				sqlSearch = String.format(" ( B.NAME LIKE %s OR B.IPADDRESS LIKE %s OR A.EVENT LIKE %s ) ",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			}

			String sqlTime = "";
			if (endTime == null)
				sqlTime = String.format(" OCCUR_TIME <= %s ", OBParser.sqlString(OBDateTime.now()));
			else
				sqlTime = String.format(" OCCUR_TIME <= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));

			if (beginTime != null)
				sqlTime += String.format(" AND OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			sqlText = String
					.format("SELECT " + " A.OCCUR_TIME, A.ADC_INDEX, A.TYPE, A.LOG_LEVEL, A.EVENT, B.NAME, B.IPADDRESS "
							+ " FROM LOG_ADC_SYSLOG A " + " INNER JOIN MNG_ADC B " + " ON A.ADC_INDEX = B.INDEX "
							+ " WHERE B.AVAILABLE = 1 AND A.ADC_INDEX=%d ", adcIndex);

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND" + sqlTime;

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			sqlText += " ORDER BY A.OCCUR_TIME DESC ";

			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				result.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				result.setAdcIPAddress(db.getString(rs, "IPADDRESS"));
				result.setAdcName(db.getString(rs, "NAME"));
				result.setContents(db.getString(rs, "EVENT"));
				result.setLevel(db.getString(rs, "LOG_LEVEL"));
				result.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				result.setType(convertLogType(db.getInteger(rs, "TYPE")));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	// DBCP 적용을 위해서 새롭게 만들었다.
	public OBDtoAuditLogAdcSystem getAdcAuditLogLast(Integer adcIndex, String searchKeys, Date beginTime, Date endTime)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, searchKey:%s, beginTime:%s, endTime:%s", adcIndex, searchKeys,
						beginTime, endTime));
		OBDtoAuditLogAdcSystem result = new OBDtoAuditLogAdcSystem();
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			int limit = 1;
			String sqlLimit = "";
			if (limit != 0)
				sqlLimit = String.format(" LIMIT %d ", limit);

			String sqlSearch = null;
			if (searchKeys != null && !searchKeys.isEmpty()) {
				// #3984-2 #8: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKeys) + "%";
				sqlSearch = String.format(" ( B.NAME LIKE %s OR B.IPADDRESS LIKE %s OR A.EVENT LIKE %s ) ",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			}

			String sqlTime = "";
			if (endTime == null)
				sqlTime = String.format(" OCCUR_TIME <= %s ", OBParser.sqlString(OBDateTime.now()));
			else
				sqlTime = String.format(" OCCUR_TIME <= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));

			if (beginTime != null)
				sqlTime += String.format(" AND OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			sqlText = String
					.format("SELECT " + " A.OCCUR_TIME, A.ADC_INDEX, A.TYPE, A.LOG_LEVEL, A.EVENT, B.NAME, B.IPADDRESS "
							+ " FROM LOG_ADC_SYSLOG A " + " INNER JOIN MNG_ADC B " + " ON A.ADC_INDEX = B.INDEX "
							+ " WHERE B.AVAILABLE = 1 AND A.ADC_INDEX=%d ", adcIndex);

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND" + sqlTime;

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			sqlText += " ORDER BY A.OCCUR_TIME DESC ";

			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			sqlText += ";";

			db.openDB();
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				result.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				result.setAdcIPAddress(db.getString(rs, "IPADDRESS"));
				result.setAdcName(db.getString(rs, "NAME"));
				result.setContents(db.getString(rs, "EVENT"));
				result.setLevel(db.getString(rs, "LOG_LEVEL"));
				result.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				result.setType(convertLogType(db.getInteger(rs, "TYPE")));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// System.out.println(new OBAdcManagementImpl().getAdcAuditLogCount(null, null,
	// null));
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public Integer getAdcAuditLogCount(OBDtoADCObject adcObject, OBDtoSearch searchOption,
			OBDtoAdcLogSearchOption selectOption, Integer accountIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcObject:%s, searchOption:%s", adcObject, searchOption));
		OBDatabase db = new OBDatabase();
		// 사용자의 유효 adcIndex를 구한다.
		String userAdcIndexString;
		int result = 0;
		String sqlText = "";

		try {
			db.openDB();

			userAdcIndexString = new OBAdcManagementImpl().getUsersAdcIndexString(adcObject.getCategory(),
					adcObject.getIndex(), accountIndex);

			if (userAdcIndexString.isEmpty() == true) { // 없으면 그만한다. 빈 list return
				return 0;
			}
			String sqlSearch = null;
			if (searchOption.getSearchKey() != null && !searchOption.getSearchKey().isEmpty()) {
				// #3984-2 #8: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchOption.getSearchKey()) + "%";
				sqlSearch = String.format(" ( B.NAME LIKE %s OR B.IPADDRESS LIKE %s OR A.EVENT LIKE %s ) ",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			}

			String sqlSelectType = "";
			String sqlSelectLevel = "";
			/*
			 * if(selectOption.getLogType() != 0 && selectOption.getLogType() != null) {
			 * sqlSelectType=String.format(" ( A.TYPE = %d ) ", selectOption.getLogType());
			 * }
			 */
			if (!selectOption.getLogLevel().equals("all") && !selectOption.getLogLevel().isEmpty()) {
				String wildcardKey = "%" + selectOption.getLogLevel() + "%";
				sqlSelectLevel = String.format(" ( A.LOG_LEVEL LIKE %s ) ", OBParser.sqlString(wildcardKey));
			}

			String sqlTime = "";
			if (searchOption.getToTime() == null)
				sqlTime = String.format(" A.OCCUR_TIME <= %s ", OBParser.sqlString(OBDateTime.now()));
			else
				sqlTime = String.format(" A.OCCUR_TIME <= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getToTime().getTime()))));

			if (searchOption.getFromTime() != null)
				sqlTime += String.format(" AND A.OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getFromTime().getTime()))));

			sqlText = String.format(
					"SELECT COUNT(A.LOG_SEQ) AS CNT " + " FROM LOG_ADC_SYSLOG A " + " INNER JOIN MNG_ADC B "
							+ " ON A.ADC_INDEX = B.INDEX " + " WHERE B.AVAILABLE = %d AND A.ADC_INDEX IN ( %s ) ",
					OBDefine.ADC_STATE.AVAILABLE, userAdcIndexString);

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND" + sqlTime;

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			if (sqlSelectType != null && !sqlSelectType.isEmpty()) {
				sqlText += " AND " + sqlSelectType;
			}

			if (sqlSelectLevel != null && !sqlSelectLevel.isEmpty()) {
				sqlText += " AND " + sqlSelectLevel;
			}

			sqlText += ";";

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("sql:%s", sqlText));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				result = db.getInteger(rs, "CNT");
				// TODO
				int limit = getPropertyLogLimitInfo();
				if (result > limit)
					result = limit;
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", result));
		return result;
	}

	public Integer getAdcAuditLogCount(Integer adcIndex, String searchKeys, Date beginTime, Date endTime, OBDatabase db)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. searchKeys:%s, begin:%s, end:%s", searchKeys, beginTime, endTime));

		int result = 0;
		String sqlText = "";
		try {
			String sqlSearch = null;
			if (searchKeys != null && !searchKeys.isEmpty()) {
				// #3984-2 #8: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKeys) + "%";
				sqlSearch = String.format(" ( B.NAME LIKE %s OR B.IPADDRESS LIKE %s OR A.EVENT LIKE %s ) ",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			}

			String sqlTime = "";
			if (endTime == null)
				sqlTime = String.format(" A.OCCUR_TIME <= %s ", OBParser.sqlString(OBDateTime.now()));
			else
				sqlTime = String.format(" A.OCCUR_TIME <= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));

			if (beginTime != null)
				sqlTime += String.format(" AND A.OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			sqlText = String.format(
					" SELECT COUNT(A.LOG_SEQ) AS CNT " + " FROM LOG_ADC_SYSLOG A " + " INNER JOIN MNG_ADC B "
							+ " ON A.ADC_INDEX = B.INDEX " + " WHERE A.ADC_INDEX=%d AND B.AVAILABLE=%d ",
					adcIndex, OBDefine.ADC_STATE.AVAILABLE);

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND" + sqlTime;

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				result = db.getInteger(rs, "CNT");
				if (result > 10000)
					result = 10000;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", result));
		return result;
	}

	// DBCP 적용을 위해서 새롭게 만들었다.
	public Integer getAdcAuditLogCount(Integer adcIndex, String searchKeys, Date beginTime, Date endTime)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. searchKeys:%s, begin:%s, end:%s", searchKeys, beginTime, endTime));

		int result = 0;
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String sqlSearch = null;
			if (searchKeys != null && !searchKeys.isEmpty()) {
				// #3984-2 #8: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKeys) + "%";
				sqlSearch = String.format(" ( B.NAME LIKE %s OR B.IPADDRESS LIKE %s OR A.EVENT LIKE %s ) ",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			}

			String sqlTime = "";
			if (endTime == null)
				sqlTime = String.format(" A.OCCUR_TIME <= %s ", OBParser.sqlString(OBDateTime.now()));
			else
				sqlTime = String.format(" A.OCCUR_TIME <= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));

			if (beginTime != null)
				sqlTime += String.format(" AND A.OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			sqlText = String.format(
					" SELECT COUNT(A.LOG_SEQ) AS CNT " + " FROM LOG_ADC_SYSLOG A " + " INNER JOIN MNG_ADC B "
							+ " ON A.ADC_INDEX = B.INDEX " + " WHERE A.ADC_INDEX=%d AND B.AVAILABLE=%d ",
					adcIndex, OBDefine.ADC_STATE.AVAILABLE);

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND" + sqlTime;

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				result = db.getInteger(rs, "CNT");
				if (result > 10000)
					result = 10000;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", result));
		return result;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// System.out.println(new OBAdcManagementImpl().getAdcConfigLogCount(1));
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public Integer getSystemAuditLogCount(String searchKeys, Date beginTime, Date endTime) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. searchKeys:%s, beginTime:%s, endTime:%s", searchKeys, beginTime, endTime));
		OBDatabase db = new OBDatabase();
		int result = 0;

		String sqlText = "";
		try {
			db.openDB();
			String sqlTime = "";

			if (beginTime != null) {
				if (endTime == null)
					endTime = new Date();
				sqlTime = String.format(" OCCUR_TIME <= %s AND OCCUR_TIME >= %s",
						OBParser.sqlString(endTime.toString()), OBParser.sqlString(beginTime.toString()));
			}

			String sqlSearch = null;
			if (searchKeys != null && !searchKeys.isEmpty()) {
				// #3984-2 #16: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKeys) + "%";
				sqlSearch = String.format(" ( B.ID LIKE %s OR A.CONTENT LIKE %s ) ", OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			} // 위에 쿼리에서 검색 조건이 C.NAME으로 되어있었음. B.NAME으로 수정. junhyun.ok_GS
				// 감사로그 사용자 컬럼에 사용자 아이디를 사용. 따라서 사용자 이름은 사용안하므로 검색 필요 X junhyun.ok_GS
			sqlText = String.format(" SELECT COUNT(A.LOG_SEQ) AS CNT   \n" + " FROM LOG_SYSTEM_AUDIT A         \n"
					+ " LEFT JOIN MNG_ACCNT   B         \n" + " ON A.ACCOUNT_INDEX = B.INDEX    \n"
					+ " LEFT JOIN MNG_ADC C 			   \n" + " ON A.ADC_INDEX = C.INDEX ");

			if (sqlTime != null && !sqlTime.isEmpty()) {
				sqlText += " WHERE " + sqlTime;

				if (sqlSearch != null && !sqlSearch.isEmpty())
					sqlText += " AND " + sqlSearch;
			} else {
				if (sqlSearch != null && !sqlSearch.isEmpty())
					sqlText += " WHERE " + sqlSearch;
			}

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				result = db.getInteger(rs, "CNT");
//TODO
				int limit = getPropertyLogLimitInfo();
				if (result > limit)
					result = limit;
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// ArrayList<OBDtoAuditLogAdcConfig> list = new
	// OBAdcManagementImpl().getAuditLogAdcConfig(1, null, null, null, null, null);
	// System.out.println(list);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public ArrayList<OBDtoAuditLogAdcConfig> getSystemAuditLog(String searchKeys, Date beginTime, Date endTime,
			Integer beginIndex, Integer endIndex) throws OBException {
		return getSystemAuditLog(searchKeys, beginTime, endTime, beginIndex, endIndex, OBDefine.ORDER_TYPE_OCCURTIME,
				OBDefine.ORDER_DIR_DESCEND);
	}

	public Timestamp getApplyTimeFromDB(int adcIndex) {
		String sqlText;
		sqlText = String.format(
				" SELECT APPLY_TIME FROM MNG_ADC WHERE INDEX=%d AND AVAILABLE=%d 	\n"
						+ " ORDER BY APPLY_TIME DESC                                       	\n" + " LIMIT 1; ",
				adcIndex, OBDefine.STATUS_AVAILABLE);
		ResultSet rs = null;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return null;
			}
			Timestamp result = db.getTimestamp(rs, "APPLY_TIME");

			return result;
		} catch (Exception e) {
//            e.printStackTrace();
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("DB open fails(%s)", e.getMessage()));
			return null;
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public void setAdcGroupInfo(Integer index, OBDtoAdcGroup groupInfo, OBDtoExtraInfo extraInfo) throws OBException {
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();
			setAdcGroupInfo(index, groupInfo, db);
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_ACCNTGROUP_SET_SUCCESS, groupInfo.getName());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void setAdcGroupInfo(Integer index, OBDtoAdcGroup groupInfo, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(
					" UPDATE MNG_ADC_GROUP SET         	\n" + " NAME = %s,            			\n"
							+ " DESCRIPTION = %s,     			\n" + " WHERE INDEX = %d;     			\n",
					OBParser.sqlString(groupInfo.getName()), OBParser.sqlString(groupInfo.getDescription()), index);
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

	@Override
	public void saveConfigAlteon(Integer adcIndex, OBDtoExtraInfo extraInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("adcIndex:%d.", adcIndex));
		OBDtoAdcInfo adcInfo;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			if (adcInfo.getSaveTime() != null && adcInfo.getApplyTime() != null
					&& adcInfo.getSaveTime().getTime() == adcInfo.getApplyTime().getTime()) {
				OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. save_time equals with apply_time"));
				db.closeDB();
				return;
			}
			new OBAdcSystemInfoAlteon().saveAdcSystem(adcInfo.getIndex(), adcInfo.getAdcIpAddress(),
					adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(), adcInfo.getSwVersion(),
					adcInfo.getConnService(), adcInfo.getConnPort());
			OBDtoAdcTimeAlteon alteonTime = new OBAdcVServerAlteon().getSystemTime(adcInfo);// throws
																							// OBExceptionUnreachable,
																							// OBExceptionLogin,
																							// OBException
			updateAlteonTime(adcInfo, alteonTime, db);
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_ADC_SAVE_SUCCESS, adcInfo.getName());
		} catch (OBExceptionUnreachable e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE);
		} catch (OBExceptionLogin e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN);
		} catch (OBException e) {
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// , e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private void updateAlteonTime(OBDtoAdcInfo adcInfo, OBDtoAdcTimeAlteon timeInfo, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(
					" UPDATE MNG_ADC " + " SET APPLY_TIME = %s, SAVE_TIME = %s " + " WHERE INDEX=%d AND AVAILABLE=%d; ",
					OBParser.sqlString(timeInfo.getApplyTime()), OBParser.sqlString(timeInfo.getSaveTime()),
					adcInfo.getIndex(), OBDefine.ADC_STATE.AVAILABLE);
			db.executeUpdate(sqlText);

		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public Integer getActivePairIndex(Integer adcIndex, OBDatabase db) throws OBException {
		String sqlText;
		sqlText = String.format(
				" SELECT PEER_IPADDRESS " + " FROM MNG_ADC " + " WHERE INDEX=%d " + " AND AVAILABLE=%d ;", adcIndex,
				OBDefine.ADC_STATE.AVAILABLE);
		ResultSet rs = null;
		try {
			rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return 0;
			}
			String peerIPAddress = db.getString(rs, "PEER_IPADDRESS");
			if (peerIPAddress != null && !peerIPAddress.isEmpty()) {
				sqlText = String.format(
						" SELECT INDEX " + " FROM MNG_ADC " + " WHERE IPADDRESS=%s " + " AND AVAILABLE=%d ;",
						OBParser.sqlString(peerIPAddress), OBDefine.ADC_STATE.AVAILABLE);
				rs = db.executeQuery(sqlText);
				if (rs.next() == false)
					return 0;
				return db.getInteger(rs, "INDEX");
			} else {
				return 0;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public Integer getActivePairIndexOld(Integer adcIndex, OBDatabase db) throws OBException {
		String sqlText;
		sqlText = String.format(
				" SELECT ACTIVE_PAIR_INDEX " + " FROM MNG_ADC " + " WHERE INDEX=%d " + " AND AVAILABLE=%d ;", adcIndex,
				OBDefine.ADC_STATE.AVAILABLE);
		Integer result = 0;
		ResultSet rs = null;
		try {
			rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return null;
			}
			result = db.getInteger(rs, "ACTIVE_PAIR_INDEX");

			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public Integer getActivePairIndex(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		Integer result = 0;
		try {
			db.openDB();
			result = getActivePairIndex(adcIndex, db);
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

	public boolean isExistPeerIP(Integer adcIndex, OBDatabase db) throws OBException {
		String sqlText;
		sqlText = String.format(" SELECT " + " SYNC_STATE " + " FROM TMP_ADC_ADDITIONAL " + " WHERE ADC_INDEX=%d ;",
				adcIndex);
		boolean result = false;
		ResultSet rs = null;
		try {
			rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				Integer syncState = db.getInteger(rs, "SYNC_STATE");
				if (syncState != null && syncState == OBDefine.STATE_ENABLE)
					result = true;
			}
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public boolean isExistPeerIP(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		boolean result = false;
		try {
			db.openDB();

			result = isExistPeerIP(adcIndex, db);
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

	public OBDtoVrrpInfo getVrrpInfo(Integer adcIndex, OBDatabase db) throws OBException {
		String sqlText;
		sqlText = String
				.format(" SELECT " + " ADC_INDEX, SYNC_STATE, PEER_IP, VRRP_PRIORITY, VRRP_SHARE, VRRP_TRACK_VRS, "
						+ " VRRP_TRACK_IFS, VRRP_TRACK_PORTS, VRRP_TRACK_L4PTS, VRRP_TRACK_REALS, VRRP_TRACK_HSRP, "
						+ " VRRP_TRACK_HSRV " + " FROM TMP_ADC_ADDITIONAL " + " WHERE ADC_INDEX=%d ;", adcIndex);
		OBDtoVrrpInfo result = new OBDtoVrrpInfo();
		ResultSet rs = null;
		try {
			rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				result.setAdcIndex(adcIndex);
				result.setSyncStatus(db.getInteger(rs, "SYNC_STATE"));
				result.setPeerIP(db.getString(rs, "PEER_IP"));
				result.setPriority(db.getInteger(rs, "VRRP_PRIORITY"));
				result.setSharing(db.getInteger(rs, "VRRP_SHARE"));
				result.setTrackVrs(db.getInteger(rs, "VRRP_TRACK_VRS"));
				result.setTrackInt(db.getInteger(rs, "VRRP_TRACK_IFS"));
				result.setTrackPorts(db.getInteger(rs, "VRRP_TRACK_PORTS"));
				result.setTrackL4pts(db.getInteger(rs, "VRRP_TRACK_L4PTS"));
				result.setTrackReals(db.getInteger(rs, "VRRP_TRACK_REALS"));
				result.setTrackHsrp(db.getInteger(rs, "VRRP_TRACK_HSRP"));
				result.setTrackHsrv(db.getInteger(rs, "VRRP_TRACK_HSRV"));
			}
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public Boolean getVrrpInfo(String address, Integer adcIndex, String adcIpaddress, Integer vendorType,
			String swVersion, OBDtoAdcSnmpInfo snmpInfo) throws OBException {
		Boolean result = false;
		try {
			ArrayList<DtoSnmpVrrpAlteon> vrrpList = new OBSnmpAlteon(adcIpaddress, snmpInfo)
					.getVrrpAddressList(adcIndex, vendorType, swVersion);
			if (vrrpList != null && vrrpList.isEmpty() == false) {
				for (DtoSnmpVrrpAlteon dtoSnmpVrrpAlteon : vrrpList) {
					if (dtoSnmpVrrpAlteon.getVrrpAddr().equals(address) && dtoSnmpVrrpAlteon.getVrrpState() == 1) {
						result = true;
					}
				}
			}

		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return result;
	}

	@Override
	public OBDtoVrrpInfo getVrrpInfo(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoVrrpInfo result = null;
		try {
			db.openDB();

			result = getVrrpInfo(adcIndex, db);
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

	// public static void main(String[] args)
	// {
	// try
	// {
	// new OBAdcManagementImpl().downloadSlbConfig(4);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

//    @Override
//    public boolean downloadSlbConfig(Integer adcIndex) throws OBException
//    {
//        OBDtoAdcInfo adcInfo = null;
//        try
//        {
//            adcInfo = getAdcInfo(adcIndex);
//
//            OBAdcVServer vServer;
//            if(adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
//                vServer = new OBAdcVServerAlteon();
//            else if(adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5)
//                vServer = new OBAdcVServerF5();
//            else if(adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS)
//                vServer = new OBAdcVServerPAS();
//            else if(adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK)
//                vServer = new OBAdcVServerPASK();
//            else
//            {
//                return false;
//            }
//            /*
//             * ADC 추가, ADC 수정 시 발생하는 SLB Config Download 는 모두 Applay Time과 비교하지 않고 강제적으로 업데이트 한다. isforce = true 2015.02.04 yh.yang
//             */
//           vServer.downloadSlbConfig(adcIndex, true);
//        }
//        catch(OBExceptionUnreachable e)
//        {
//            throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE);
//        }
//        catch(OBExceptionLogin e)
//        {
//            throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN);
//        }
//        catch(OBException e)
//        {
//            throw e;
//        }
//        catch(Exception e)
//        {
//            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//        }
//
//        return true;
//    }

	@Override
	public OBDtoSLBUpdateStatus downloadSlbConfig(Integer adcIndex) throws OBException {
		OBDtoAdcInfo adcInfo = null;
		OBDtoSLBUpdateStatus retVal = new OBDtoSLBUpdateStatus();
		try {
			adcInfo = getAdcInfo(adcIndex);

//			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
//			{
//				String[] verElements = adcInfo.getSwVersion().split("\\.", 2); //버전 요소 두개까지 문자열로 건진다. 지금은 하나만 쓴다.
//				int version = Integer.parseInt(verElements[0]);
//				if (version >= 30)
//				{
//					ConfigAlteonDto alteon = addVdirectAlteon(adcInfo);
//
//					String resultCode = new OBVdirectServiceImpl().createAlteon(alteon);
//					if (!resultCode.equals("201"))
//					{
//						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "vdirect alteon create failed");
//					}
//
//					resultCode = new OBVdirectServiceImpl().configSnmp(alteon);
//					if (!resultCode.equals("201"))
//					{
//						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
//								"vdirect alteon snmp setting failed");
//					}
//
//					resultCode = new OBVdirectServiceImpl().configCli(alteon);
//					if (!resultCode.equals("201"))
//					{
//						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
//								"vdirect alteon cli setting failed");
//					}
//				}
//			}

			OBAdcVServer vServer;
			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
				vServer = new OBAdcVServerAlteon();
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5)
				vServer = new OBAdcVServerF5();
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS)
				vServer = new OBAdcVServerPAS();
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK)
				vServer = new OBAdcVServerPASK();
			else {
				return null;
			}
			/*
			 * Applay Time과 비교하지 않고 강제적으로 업데이트 한다. isforce = true 2015.02.04 yh.yang
			 */
			retVal = vServer.downloadSlbConfig(adcIndex, true);
		} catch (

		OBExceptionUnreachable e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE);
		} catch (OBExceptionLogin e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	@Override
	public ArrayList<OBDtoAdcInfo> getAdcInfoList(Integer accountIndex, Integer beginIndex, Integer endIndex,
			Integer orderType, Integer orderDir) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. accountIndex:%d", accountIndex));

		ArrayList<OBDtoAdcInfo> list;
		try {
			list = getAdcInfoList(accountIndex, orderType, orderDir);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));
		return list;
	}

	private String getSystemAuditLogOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY NAME ASC NULLS LAST ";
		switch (orderType) {
		case OBDefine.ORDER_TYPE_TYPE:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY TYPE ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY TYPE DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_ADCNAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY ADC_NAME ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY ADC_NAME DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SEVERITY:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY LEVEL ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY LEVEL DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_IPADDRESS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY CLIENT_IP ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY CLIENT_IP DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_USERNAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY ACCNT_ID ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY ACCNT_ID DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_OCCURTIME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY OCCUR_TIME ASC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			break;
		}
		return retVal;
	}

	@Override
	public ArrayList<OBDtoAuditLogAdcConfig> getSystemAuditLog(String searchKeys, Date beginTime, Date endTime,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. searchKeys:%s, Time:%s/%s, index:%d/%d",
				searchKeys, beginTime, endTime, beginIndex, endIndex));
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoAuditLogAdcConfig> result = new ArrayList<OBDtoAuditLogAdcConfig>();
		String sqlText = "";
		try {
			db.openDB();

			String sqlTime = "";

			if (beginTime != null) {
				if (endTime == null)
					endTime = new Date();
				sqlTime = String.format(" OCCUR_TIME <= %s AND OCCUR_TIME >= %s",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))),
						OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));
			}

			String sqlLimit = "";

			if (endIndex != null) {
				int limit = 0;
				int offset = 0;
				if (beginIndex != null) {
					limit = Math.abs(endIndex.intValue() - beginIndex.intValue()) + 1;
					offset = Math.abs(beginIndex.intValue());
				} else {
					limit = endIndex.intValue();
					offset = 0;
				}

				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}

			String sqlSearch = null;
			if (searchKeys != null && !searchKeys.isEmpty()) {
				// #3984-2 #16: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKeys) + "%";
				sqlSearch = String.format(" ( B.ID LIKE %s OR A.CONTENT LIKE %s ) ", OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			} // 감사로그 사용자 컬럼에 사용자 아이디를 사용. 따라서 사용자 이름은 사용안하므로 검색 필요 X junhyun.ok_GS
				// 사용자 이름아 아니라 사용자 아이디를 셀렉트하는 것으로 쿼리 첫번째 줄 수정. junhyun.ok_GS
			sqlText = String.format(
					" SELECT COALESCE(B.ID,'SYSTEM') AS ACCNT_ID, 																\n"
							+ " C.NAME AS ADC_NAME, A.OCCUR_TIME AS OCCUR_TIME, A.ACCOUNT_INDEX, A.TYPE AS TYPE_VAL,		  				\n"
							+ " CASE WHEN A.TYPE=1 THEN 'SYSTEM' WHEN A.TYPE=2 THEN 'CONFIG' WHEN A.TYPE=3 THEN 'LOGIN/LOGOUT' 			\n"
							+ " WHEN A.TYPE=4 THEN 'SYSTEM' WHEN A.TYPE=999 THEN 'OTHERS' ELSE 'SYSTEM' END AS TYPE, 				    	\n"
							+ " A.ADC_INDEX, A.CLIENT_IP AS CLIENT_IP, A.LEVEL AS LEVEL, A.RINDEX,          								\n"
							+ " A.CONTENT AS CONTENT, B.ID                                                                  				\n"
							+ " FROM LOG_SYSTEM_AUDIT A                                                                    				\n"
							+ " LEFT JOIN MNG_ACCNT   B                                                                    				\n"
							+ " ON A.ACCOUNT_INDEX = B.INDEX                                                               				\n"
							+ " LEFT JOIN MNG_ADC C                                                                        				\n"
							+ " ON A.ADC_INDEX = C.INDEX                                                                   				\n");

			if (sqlTime != null && !sqlTime.isEmpty()) {
				sqlText += " WHERE " + sqlTime;

				if (sqlSearch != null && !sqlSearch.isEmpty())
					sqlText += " AND " + sqlSearch;
			} else {
				if (sqlSearch != null && !sqlSearch.isEmpty())
					sqlText += " WHERE " + sqlSearch;
			}

			// if(sqlSearch!=null && !sqlSearch.isEmpty())
			// sqlText += " AND " + sqlSearch;

			sqlText += getSystemAuditLogOrderType(orderType, orderDir);
			sqlText += sqlLimit;
			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAuditLogAdcConfig log = new OBDtoAuditLogAdcConfig();

				String accntName = db.getString(rs, "ACCNT_ID");
				log.setAccountID(accntName);// 첫번째 컬럼.
				// if(accntName == null || accntName.isEmpty())
				// log.setAccountID("SYSTEM");// 첫번째 컬럼.
				log.setAdcName(db.getString(rs, "ADC_NAME"));// 두번째 컬럼.
				log.setAccountIndex(db.getInteger(rs, "ACCOUNT_INDEX"));
				log.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				log.setClientIPAddress(db.getString(rs, "CLIENT_IP"));
				log.setContents(db.getString(rs, "CONTENT"));
				log.setLevel(convertLogLevel(db.getInteger(rs, "LEVEL")));
				log.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				// log.setType(convertLogType(db.getInteger(rs, "TYPE")));
				log.setType(convertLogType(db.getInteger(rs, "TYPE_VAL"))); // integer 형이라서 order by 할때 문제가 발생 (type이
																			// 1,4 경우의 값이 같음)
				// log.setType(db.getString(rs, "TYPE")); // integer 형을 쿼리에서 string으로
				// converting없이 바로 받아옴. (쿼리에서 해결)
				result.add(log);
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
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	@Override
	public Integer getUsedAdcCnt() throws OBException {
		OBDatabase db = new OBDatabase();
		Integer retVal = 0;
		String sqlText = "";
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT COUNT(*) AS CNT 	" + " FROM MNG_ADC              " + " WHERE AVAILABLE = %d      ",
					OBDefine.ADC_STATE.AVAILABLE);

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true)
				retVal = db.getInteger(rs, "CNT");
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public Integer getUsedVSCnt() throws OBException {
		Integer retVal = 0;
		try {
			retVal = new OBVServerDB().getTotalVServerCount();
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	@Override
	public Integer getUsedUserCnt() throws OBException {
		Integer retVal = 0;
		try {
			retVal = new OBAccountImpl().getAccountTotalCount();
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public ArrayList<OBDtoAdcGroup> getAdcGroupListByAccount(Integer accountIndex, Integer adcGroupIndex,
			String searchKey) throws OBException {
		return getAdcGroupListByAccount(accountIndex, adcGroupIndex, searchKey, OBDefine.ORDER_TYPE_SECOND,
				OBDefine.ORDER_DIR_ASCEND, null);
	}

	@Override
	public OBAdcCfgInfo getAdcConfigInfo(Integer adcIndex) throws OBException {
		OBAdcCfgInfo adc = new OBAdcCfgInfo();
		OBDatabase db = new OBDatabase();
		ResultSet rs;

		String sqlText = "";
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX, IPADDRESS, ACCNT, PASSWORD, TYPE, SNMP_RCOMM, APPLY_TIME \n"
					+ " FROM MNG_ADC WHERE INDEX = %d                                          \n", adcIndex);

			// System.out.println(sqlText);
			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				adc.setSnmpRCommunityDB(db.getString(rs, "SNMP_RCOMM"));
				adc.setAdcIpaddress(db.getString(rs, "IPADDRESS"));
				adc.setAdcId(db.getString(rs, "ACCNT"));
				adc.setAdcPassword(db.getString(rs, "PASSWORD"));
				adc.setAdcType(db.getInteger(rs, "TYPE"));
			}
			return adc;
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

	public void setSnmpReadCommunityString(String rcomm, Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();

			sqlText = String.format(" UPDATE MNG_ADC SET " + "SNMP_RCOMM=%s " + "WHERE INDEX=%d ",
					OBParser.sqlString(rcomm), adcIndex);

			OBSystemLog.sql(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
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

	private String getAdcAuditLogOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST ";
		switch (orderType) {
		case OBDefine.ORDER_TYPE_OCCURTIME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY OCCUR_TIME ASC NULLS LAST ";
			else
				retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_USERNAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY USER_NAME ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY USER_NAME DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_IPADDRESS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INET(B.IPADDRESS) ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY INET(B.IPADDRESS) DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SEVERITY:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY SEVERITY ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY SEVERITY DESC NULLS LAST, OCCUR_TIME DESC  NULLS LAST";
			break;
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// System.out.println(new OBAdcManagementImpl().getAdcAuditLog(2, "", null,
	// null, null, null));
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public ArrayList<OBDtoAuditLogAdcSystem> getAdcAuditLog(OBDtoADCObject adcObject, OBDtoSearch searchOption,
			OBDtoOrdering orderOption, OBDtoAdcLogSearchOption selectOption, Integer accountIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcObject:%s, searchOption:%s, orderOption:%s",
				adcObject, searchOption, orderOption));
		ArrayList<OBDtoAuditLogAdcSystem> result = new ArrayList<OBDtoAuditLogAdcSystem>();
		OBDatabase db = new OBDatabase();
		// 사용자의 유효 adcIndex를 구한다.
		String userAdcIndexString;
		String sqlText = "";
		String sqlSearch = "";
		String sqlSelectType = "";
		String sqlSelectLevel = "";
		String sqlTime = "";
		String sqlLimit = "";

		try {
			db.openDB();
			userAdcIndexString = new OBAdcManagementImpl().getUsersAdcIndexString(adcObject.getCategory(),
					adcObject.getIndex(), accountIndex);

			if (searchOption.getSearchKey() != null && !searchOption.getSearchKey().isEmpty()) {
				// #3984-2 #8: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchOption.getSearchKey()) + "%";
				sqlSearch = String.format(" ( B.NAME LIKE %s OR B.IPADDRESS LIKE %s OR A.EVENT LIKE %s ) ",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			}
			if (selectOption != null && !selectOption.getLogLevel().equals("all")) {
				String wildcardKey = "%" + selectOption.getLogLevel() + "%";
				sqlSelectLevel = String.format(" ( A.LOG_LEVEL LIKE %s ) ", OBParser.sqlString(wildcardKey));
			}
			if (searchOption.getToTime() == null) {
				sqlTime = String.format(" OCCUR_TIME <= %s ", OBParser.sqlString(OBDateTime.now()));
			} else {
				sqlTime = String.format(" OCCUR_TIME <= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getToTime().getTime()))));
			}
			if (searchOption.getFromTime() != null) {
				sqlTime += String.format(" AND OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getFromTime().getTime()))));
			}

			int offset = 0;
			if (searchOption.getBeginIndex() != null) {
				offset = searchOption.getBeginIndex().intValue();
			}

			int limit = 0;
			if (searchOption.getEndIndex() != null) {
				limit = Math.abs(searchOption.getEndIndex().intValue() - offset) + 1;
			}

			if (limit != 0) {
				sqlLimit = String.format(" LIMIT %d ", limit);
			}
			String sqlOffset = String.format(" OFFSET %d ", offset);

			sqlText = String.format("SELECT "
					+ " A.OCCUR_TIME AS OCCUR_TIME, A.ADC_INDEX, A.TYPE AS TYPE, A.LOG_LEVEL AS SEVERITY, A.EVENT AS CONTENT, B.NAME AS USER_NAME, B.IPADDRESS AS IPADDRESS "
					+ " FROM LOG_ADC_SYSLOG A " + " INNER JOIN MNG_ADC B " + " ON A.ADC_INDEX = B.INDEX "
					+ " WHERE B.AVAILABLE = 1 AND A.ADC_INDEX IN ( %s ) ", userAdcIndexString);

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND" + sqlTime;

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			if (sqlSelectType != null && !sqlSelectType.isEmpty()) {
				sqlText += " AND " + sqlSelectType;
			}

			if (sqlSelectLevel != null && !sqlSelectLevel.isEmpty()) {
				sqlText += " AND " + sqlSelectLevel;
			}

			sqlText += getAdcAuditLogOrderType(orderOption.getOrderType(), orderOption.getOrderDirection());// " ORDER
																											// BY
																											// A.OCCUR_TIME
																											// DESC ";

			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			sqlText += sqlOffset;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAuditLogAdcSystem log = new OBDtoAuditLogAdcSystem();

				log.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				log.setAdcIPAddress(db.getString(rs, "IPADDRESS"));
				log.setAdcName(db.getString(rs, "USER_NAME"));
				log.setContents(db.getString(rs, "CONTENT"));
				log.setContents_extra("");
				log.setLevel(db.getString(rs, "SEVERITY"));
				log.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				log.setType(convertLogType(db.getInteger(rs, "TYPE")));
				result.add(log);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result-size:%d", result.size()));
		return result;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// ArrayList<OBDtoAdcGroup> list = new
	// OBAdcManagementImpl().getAdcGroupListByAccount(0, null, null,
	// OBDefine.ORDER_TYPE_FIRST, OBDefine.ORDER_DIR_ASCEND);
	// for(OBDtoAdcGroup info:list)
	// {
	// // System.out.println(info);
	// for(OBDtoAdcInfo adcIfo:info.getAdcList())
	// System.out.println(adcIfo);
	// }
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public ArrayList<OBDtoAdcGroup> getAdcGroupListByAccount(Integer accountIndex, Integer adcGroupIndex,
			String searchKey, Integer orderType, Integer orderDir, Integer adcListPageOption) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. accountIndex:%d", accountIndex));

		ArrayList<OBDtoAdcGroup> groupList = new ArrayList<OBDtoAdcGroup>();

		OBDatabase db = new OBDatabase();
		OBDatabase db2 = new OBDatabase();
		OBDatabase db3 = new OBDatabase();
		ResultSet rs;

		String sqlText = "";
		try {
			db.openDB();
			db2.openDB();
			db3.openDB();

			String adcIndexSubqueryAdmin = String.format(" SELECT INDEX FROM MNG_ADC WHERE AVAILABLE = %d ",
					OBDefine.ADC_STATE.AVAILABLE);
			String adcIndexSubqueryNonAdmin = String
					.format(" SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %d ", accountIndex);
			String adcIndexSubquery = "";
			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accountIndex);
			if (roleNo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("accountIndex \"%d\" role error.", accountIndex));
			}

			if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
				adcIndexSubquery = adcIndexSubqueryAdmin;
			} else {
				adcIndexSubquery = adcIndexSubqueryNonAdmin;
			}

			sqlText = String.format(
					" SELECT A.GROUP_INDEX, B.NAME, B.DESCRIPTION, COUNT(A.INDEX) 	\n"
							+ " FROM MNG_ADC A 												\n"
							+ " INNER JOIN MNG_ADC_GROUP B 									\n"
							+ " ON A.GROUP_INDEX = B.INDEX	            					\n"
							+ " WHERE A.INDEX IN (%s) 										\n", // where-in:empty
					// string
					// 불가,
					// null
					// 불가,
					// OK
					adcIndexSubquery);
			if (adcGroupIndex != null && (adcGroupIndex.intValue() > 0))
				sqlText += String.format(" AND A.GROUP_INDEX = %d ", adcGroupIndex);

			sqlText += " GROUP BY A.GROUP_INDEX, B.NAME, B.DESCRIPTION ORDER BY B.NAME;\n";
			rs = db.executeQuery(sqlText);
			Integer groupIndex = 0;
			while (rs.next()) {
				OBDtoAdcGroup group = new OBDtoAdcGroup();
				int unavailCount = 0;
				groupIndex = db.getInteger(rs, "GROUP_INDEX");
				group.setIndex(groupIndex);
				group.setName(db.getString(rs, "NAME"));
				group.setDescription(db.getString(rs, "DESCRIPTION"));
				group.setAdcCount(db.getInteger(rs, "COUNT"));
				ArrayList<OBDtoAdcInfo> adcList = getAdcInfoListInGroup(groupIndex, accountIndex, searchKey, orderType,
						orderDir, adcListPageOption, db2, db3);
				for (OBDtoAdcInfo adc : adcList) {
					if (adc.getStatus().equals(OBDefine.ADC_STATUS.REACHABLE) == false) {
						unavailCount++;
					}
				}
				// System.out.println("Unavail = " + unavailCount);
				group.setAdcList(adcList);
				group.setAdcUnavailCount(unavailCount);
				// ADC가 하나 이상없을 경우에도 제공하도록 수정합니다.
				// if(adcList != null && adcList.size() > 0) // ADC가 하나 이상있을 경우에만 제공한다.
				groupList.add(group);
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
			if (db2 != null)
				db2.closeDB();
			if (db3 != null)
				db3.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", groupList.toString()));
		return groupList;
	}

	@Override
	public ArrayList<String> getAdcNameList() throws OBException {
		ArrayList<String> retVal = new ArrayList<String>();

		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();

			sqlText = String.format("SELECT NAME FROM MNG_ADC WHERE AVAILABLE = %d;", OBDefine.ADC_STATE.AVAILABLE);
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				retVal.add(db.getString(rs, "NAME"));
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
		return retVal;
	}

	// public OBAdcCfgInfo getSnmpConfigInfo(OBDtoAdcInfo adcInfo) throws
	// OBException, Exception
	// {
	// Integer adcType = adcInfo.getAdcType();
	// try
	// {
	// OBAdcCfgInfo adc = new OBAdcCfgInfo();
	// adc.setSnmpRCommunityDB(adcInfo.getSnmpRComm());
	// adc.setAdcIpaddress(adcInfo.getAdcIpAddress());
	// adc.setAdcPassword(adcInfo.getAdcPassword());
	// adc.setAdcId(adcInfo.getAdcAccount());
	// adc.setAdcType(adcInfo.getAdcType());
	// //adc = getAdcConfigInfo(adcIndex);
	//
	// if(adcType.equals(OBDefine.ADC_TYPE_ALTEON))
	// {
	// OBAdcAlteonHandler alteon =
	// OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
	// alteon.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
	// adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(),
	// adcInfo.getConnPort());
	// alteon.login();
	// String snmp = alteon.cmndCfgSnmp();
	// alteon.disconnect();
	//
	// if (snmp == null)
	// {
	// adc.setSnmpState("");
	// }
	// else
	// {
	// String snmpState[] = snmp.split("\n");
	// String snmpStateText = "Current v1/v2 access enabled";
	// int trimSnmpStateLength = snmpState.length;
	// for(int i = 0; i < trimSnmpStateLength; i++)
	// {
	// if(snmpState[i].equals(snmpStateText))
	// {
	// adc.setSnmpState(snmpStateText);
	// break;
	// }
	// else
	// {
	// adc.setSnmpState("-");
	// }
	// }
	// }
	//
	// return adc;
	// }
	// else if(adcType.equals(OBDefine.ADC_TYPE_F5))
	// {
	// // iControl.Interfaces interfaces =
	// CommonF5.initInterfaces(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
	// adcInfo.getAdcPasswordDecrypt());
	// //
	// // adc.setSnmpState(SystemF5.getSnmpState(interfaces));
	// // return adc;
	//
	// }
	// else if(adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PAS))
	// {
	// // OBAdcPASHandler pas = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
	// // pas.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
	// adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(),
	// adcInfo.getConnPort());
	// // pas.login();
	// // String snmp = pas.cmndSnmpInfo();
	// // pas.disconnect();
	// // String temp[] = snmp.split("SNMP State = ");
	// // String snmpState[] = temp[1].split("\n");
	// //
	// // for(String line:snmpState)
	// // {
	// // if(line.isEmpty())
	// // continue;
	// // if(line.equals(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SNMP_PAS)))
	// // {
	// // adc.setSnmpState(line);
	// // break;
	// // }
	// // else if(line.equals("disabled"))
	// // {
	// // adc.setSnmpState(line);
	// // break;
	// // }
	// // }
	// // return adc;
	// }
	// else if(adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PASK))
	// {
	// // OBAdcPASKHandler pask =
	// OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
	// // pask.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
	// adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(),
	// adcInfo.getConnPort());
	// // pask.login();
	// // String snmp = pask.cmndSnmpInfo();
	// // pask.disconnect();
	// // String temp[] = snmp.split(" Status : ", 2);
	// // String snmpState[] = temp[1].split("\n");
	// //
	// // for(String line:snmpState)
	// // {
	// // if(line.isEmpty())
	// // continue;
	// // if(line.equals(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SNMP_PASK)))
	// // {
	// // adc.setSnmpState(line);
	// // break;
	// // }
	// // }
	// // return adc;
	// }
	// }
	// catch(OBExceptionUnreachable e)
	// {
	// e.printStackTrace();
	// }
	// catch(OBExceptionLogin e)
	// {
	// e.printStackTrace();
	// }
	// return null;
	// }

	public OBAdcCfgInfo getSyslogListConfigInfo(OBDtoAdcInfo adcInfo) throws OBException, Exception {
		Integer adcType = adcInfo.getAdcType();

		OBAdcCfgInfo adc = new OBAdcCfgInfo();
		adc.setSnmpRCommunityDB(adcInfo.getSnmpRComm());
		adc.setAdcIpaddress(adcInfo.getAdcIpAddress());
		adc.setAdcPassword(adcInfo.getAdcPassword());
		adc.setAdcId(adcInfo.getAdcAccount());
		adc.setAdcType(adcInfo.getAdcType());

		ArrayList<String> syslogList = new ArrayList<String>();
		OBEnvManagementImpl adcsmartIpaddress = new OBEnvManagementImpl();
		String adcsmartIp = adcsmartIpaddress.getSystemConfig().getNetworkInfo().getIpAddress();
		if (adcType.equals(OBDefine.ADC_TYPE_ALTEON)) {
			OBAdcAlteonHandler alteon = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
			alteon.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
			alteon.login();
			String syslog = alteon.cmndCfgSyslog();
			alteon.disconnect();
			String ipRegex = "\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b";
			Pattern pSyslog = Pattern.compile(ipRegex);
			Matcher mc = pSyslog.matcher(syslog);
			// Syslog를 가져온 데이터를 :로 시작하고 ,로끝나는 구문을 데이터 가공 처리함 text1에 삽입
			int startIndex1 = syslog.indexOf(":");
			int lastIndex1 = syslog.lastIndexOf(",");
			String trimString = syslog.substring(startIndex1 + 1, lastIndex1);
			syslogList.add(trimString);
			while (mc.find()) {
				String ipMatch = mc.group();
				if (ipMatch.equals(adcsmartIp)) {
					adc.setSyslogServerListResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
					adc.setSyslogStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
					break;
				} else {
					adc.setSyslogServerListResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
					adc.setSyslogStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
				}
			}
			adc.setSyslogServerList(syslogList);
			return adc;
		} else if (adcType.equals(OBDefine.ADC_TYPE_F5)) {
			OBSShCmndExec sce = new OBSShCmndExec();
			sce.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcCliAccount(),
					adcInfo.getAdcCliPasswordDecrypt(), adcInfo.getConnPort());
			sce.sshLogin();
			String syslog;
			String adcVersion = adcInfo.getSwVersion().substring(0, 1);
			if (adcVersion.equals("9")) {
				syslog = sce.sendCommand("b syslog list");
			} else {
				syslog = sce.sendCommand("tmsh list sys syslog");
			}
			sce.sshDisconnect();
			String syslogPattern = "\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b";
			Pattern pSyslog = Pattern.compile(syslogPattern);
			Matcher mc = pSyslog.matcher(syslog);
			while (mc.find()) {
				String ipMatch = mc.group();
				syslogList.add(ipMatch);
			}
			if (!syslogList.isEmpty()) {
				if (syslogList.indexOf(adcsmartIp) != -1) {
					adc.setSyslogServerListResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
					adc.setSyslogStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
				} else {
					adc.setSyslogServerListResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
					adc.setSyslogStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
				}
			} else {
				syslogList.add("-");
			}

			adc.setSyslogServerList(syslogList);
			return adc;
		} else if (adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PAS)) {
			OBAdcPASHandler pas = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
			pas.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
					adcInfo.getConnService(), adcInfo.getConnPort());
			pas.login();
			String snmp = pas.cmndSyslogInfo();
			pas.disconnect();
			String test[] = snmp.split("Index IP Address Facility Level Agent Facility", 2);
			String test1[] = test[1].split("------------------------------------------------------------------", 2);
			String Syslog[] = test1[0].split("\n");

			for (String line : Syslog) {
				if (line.isEmpty())
					continue;
				String test4[] = line.split(" ", 3);
				if (test4[1].equals(adcsmartIp)) {
					adc.setSyslogServerListResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
					adc.setSyslogStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
				} else if (adc.getSyslogServerListResult() == null
						|| adc.getSyslogServerListResult() != OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS)) {
					adc.setSyslogServerListResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
					adc.setSyslogStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
				}
				syslogList.add(test4[1]);
			}
			adc.setSyslogServerList(syslogList);
			return adc;
		} else if (adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PASK)) {
			OBAdcPASKHandler pask = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
			pask.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
					adcInfo.getConnService(), adcInfo.getConnPort());
			pask.login();
			String snmp = pask.cmndSyslogInfo();
			pask.disconnect();
			String test[] = snmp.split(" IP Address Facility Level Agent Facility", 2);
			String test1[] = test[1]
					.split("================================================================================", 2);
			String Syslog[] = test1[0].split("\n");
			for (String line : Syslog) {
				if (line.isEmpty())
					continue;
				String test4[] = line.split(" ", 3);
				if (test4[1].equals(adcsmartIp)) {
					adc.setSyslogServerListResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
					adc.setSyslogStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
				} else if (adc.getSyslogServerListResult() == null
						|| adc.getSyslogServerListResult() != OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS)) {
					adc.setSyslogServerListResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
					adc.setSyslogStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
				}
				syslogList.add(line);
			}
			adc.setSyslogServerList(syslogList);
			return adc;
		}
		return null;
	}

	// public OBAdcCfgInfo getVstatConfigInfo(OBDtoAdcInfo adcInfo) throws
	// OBException, Exception
	// {
	// Integer adcType = adcInfo.getAdcType();
	//
	// OBAdcCfgInfo adc = new OBAdcCfgInfo();
	// adc.setSnmpRCommunityDB(adcInfo.getSnmpRComm());
	// adc.setAdcIpaddress(adcInfo.getAdcIpAddress());
	// adc.setAdcPassword(adcInfo.getAdcPassword());
	// adc.setAdcId(adcInfo.getAdcAccount());
	// adc.setAdcType(adcInfo.getAdcType());
	// // adc = getAdcConfigInfo(adcIndex);
	// if(adcType.equals(OBDefine.ADC_TYPE_ALTEON))
	// {
	// OBAdcAlteonHandler alteon =
	// OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
	// alteon.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
	// adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(),
	// adcInfo.getConnPort());
	// alteon.login();
	// String vstat = alteon.cmndCfgSlbVstatCur();
	// alteon.disconnect();
	// String pVstat = ".+vstat enabled.+";
	// String trimVstat[] = vstat.split("\n");
	//
	// if(trimVstat[3].matches(pVstat))
	// {
	// adc.setVstatState("enabled");
	// }
	// else
	// adc.setVstatState("disabled");
	// return adc;
	// }
	// else if(adcType.equals(OBDefine.ADC_TYPE_F5))
	// {
	// return adc;
	// }
	// else if(adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PAS))
	// {
	// return adc;
	// }
	// else if(adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PASK))
	// {
	// return adc;
	// }
	// return null;
	// }

	@Override
	public OBAdcCfgInfo getAllConfigInfo(Integer adcIndex) throws OBException {
		// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
		//
		// OBAdcCfgInfo allConfigInfo = new OBAdcCfgInfo();
		// allConfigInfo = getAdcConfigInfo(adcIndex);
		//
		// OBAdcCfgInfo ConfigInfo = new OBAdcCfgInfo();
		// try
		// {
		// ConfigInfo = getSnmpConfigInfo(adcInfo);
		// allConfigInfo.setSnmpState(ConfigInfo.getSnmpState());// snmp 상태 정보
		//
		// ConfigInfo = getSnmpRCommConfigInfo(adcInfo);
		// allConfigInfo.setSnmpRcommunityADC(ConfigInfo.getSnmpRcommunityADC());//
		// ADC에서 추출한 ReadCommunity
		// allConfigInfo.setSnmpRCommunityDB(ConfigInfo.getSnmpRCommunityDB());// DB에
		// 저장된 ReadCommunity (adcIndex를 이용하여 추출)
		//
		// if(ConfigInfo.getSnmpRcommunityADC().equals(ConfigInfo.getSnmpRCommunityDB()))
		// {// ADC 에서 가져온 snmp Community String 값과 DB에 저장된 snmp Community
		// // String 값이 같으면 "성공" 표시
		// allConfigInfo.setSnmpStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
		// }
		// else
		// {// ADC 에서 가져온 snmp Community String 값과 DB에 저장된 snmp Community
		// // String 값이 같으면 "실패" 표시
		// allConfigInfo.setSnmpStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
		// }
		// ConfigInfo = getSnmpAllowListConfigInfo(adcInfo);
		// allConfigInfo.setAllowList(ConfigInfo.getAllowList());
		// allConfigInfo.setAllowListResult(ConfigInfo.getAllowListResult());
		//
		// ConfigInfo = getSyslogConfigInfo(adcInfo);
		// allConfigInfo.setSyslogState(ConfigInfo.getSyslogState());// syslog 상태 정보
		//
		// ConfigInfo = getSyslogListConfigInfo(adcInfo);
		// allConfigInfo.setSyslogServerListResult(ConfigInfo.getSyslogServerListResult());
		// allConfigInfo.setSyslogServerList(ConfigInfo.getSyslogServerList());// ADC에
		// 등록된 IP List
		//
		// // ConfigInfo = getVstatConfigInfo(adcInfo);
		// // allConfigInfo.setVstatState(ConfigInfo.getVstatState());// vstat 상태 정보
		//
		// ConfigInfo = getLoginConfigState(adcInfo);
		// allConfigInfo.setFunction_loginState(ConfigInfo.getFunction_loginState());//
		// login 동작 상태
		// allConfigInfo.setFunction_loginStateTime(ConfigInfo.getFunction_loginStateTime());
		// // login 동작이 정상 일때 시간을 나타낸다.
		//
		// ConfigInfo = getSnmpConfigState(adcInfo);
		// allConfigInfo.setFunction_snmpState(ConfigInfo.getFunction_snmpState());//
		// snmp 동작 상태
		//
		// ConfigInfo = getSyslogConfigState(adcIndex);
		// allConfigInfo.setFunction_syslogState(ConfigInfo.getFunction_syslogState());//
		// syslog 동작 상태
		// allConfigInfo.setFunction_syslogStateTime(ConfigInfo.getFunction_syslogStateTime());//
		// syslog 동작이 정상 일때 시간을 나타낸다.
		//
		// switch(allConfigInfo.getAdcType())
		// {
		// case OBDefine.ADC_TYPE_F5:
		// ConfigInfo = getLoginConfigState(adcInfo);
		// allConfigInfo.setFunction_loginState(ConfigInfo.getFunction_loginState());//
		// login
		// allConfigInfo.setFunction_loginStateTime(ConfigInfo.getFunction_loginStateTime());
		// //
		// allConfigInfo.setFunction_cliLoginState(ConfigInfo.getFunction_cliLoginState());
		// allConfigInfo.setFunction_cliLoginStateTime(ConfigInfo.getFunction_cliLoginStateTime());
		// if(allConfigInfo.getFunction_loginState() == null)
		// {
		// allConfigInfo.setFunction_cliLoginStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
		// }
		// else
		// {
		// allConfigInfo.setFunction_cliLoginStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
		// }
		// commonSysState(allConfigInfo);
		// String[] trimSnmpSet = allConfigInfo.getSnmpRcommunityADC().split("\n");
		// for(int i = 0; i < trimSnmpSet.length; i++)
		// {
		// if(trimSnmpSet[i].equals(allConfigInfo.getSnmpRCommunityDB()))
		// {
		// allConfigInfo.setSnmpRcommunityResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
		// break;
		// }
		// else
		// {
		// allConfigInfo.setSnmpRcommunityResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
		// }
		//
		// }
		// if(allConfigInfo.getSnmpState().equals(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SNMP_F5)))
		// {// snmp State 값이 enable 이면 "성공" 표시
		// allConfigInfo.setSnmpStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
		// }
		// else
		// {// snmp State 값이 enable이 아니면 "실패" 표시
		// allConfigInfo.setSnmpStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
		// }
		// break;
		// case OBDefine.ADC_TYPE_ALTEON:
		// commonSysState(allConfigInfo);
		// if(allConfigInfo.getSnmpState().equals(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SNMP_ALTEON)))
		// {// snmp State 값이 enable 이면 "성공" 표시
		// allConfigInfo.setSnmpStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
		// }
		// else
		// {// snmp State 값이 enable이 아니면 "실패" 표시
		// allConfigInfo.setSnmpStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
		// }
		//
		// if(allConfigInfo.getSyslogState() == null)
		// {// syslog State 값이 없을 때 "-"로 표시
		// allConfigInfo.setSyslogStateResult("-");
		// }
		// else
		// {// syslog State 값이 있을 때
		// if(allConfigInfo.getSyslogState().equals(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SYSLOG_ALTEON)))
		// {//
		// allConfigInfo.setSyslogStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
		// }
		// else
		// {
		// allConfigInfo.setSyslogStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
		// }
		// }
		// /* Alteon 장비만 해당 됨 */
		// if(allConfigInfo.getVstatState() == null)
		// {
		// allConfigInfo.setVstatStateResult("-");
		// }
		// else
		// {
		// if(allConfigInfo.getVstatState().equals(OBMessages.getMessage(OBMessages.MSG_SYSTEM_VSTAT)))
		// {
		// allConfigInfo.setVstatStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
		// }
		// else
		// {
		// allConfigInfo.setVstatStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
		// }
		// }
		//
		// break;
		// case OBDefine.ADC_TYPE_PIOLINK_PAS:
		// // commonSysState(allConfigInfo);
		// //
		// if(allConfigInfo.getSnmpState().equals(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SNMP_PAS)))
		// // {// snmp State 값이 enable 이면 "성공" 표시
		// //
		// allConfigInfo.setSnmpStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
		// // }
		// // {// snmp State 값이 enable이 아니면 "실패" 표시
		// //
		// allConfigInfo.setSnmpStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
		// // }
		// //
		// if(allConfigInfo.getSyslogState().equals(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SYSLOG_PAS)))
		// // {//
		// //
		// allConfigInfo.setSyslogStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
		// // }
		// // else
		// // {
		// //
		// allConfigInfo.setSyslogStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
		// // }
		// break;
		// case OBDefine.ADC_TYPE_PIOLINK_PASK:
		// // commonSysState(allConfigInfo);
		// //
		// if(allConfigInfo.getSnmpState().equals(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SNMP_PASK)))
		// // {// snmp State 값이 enable 이면 "성공" 표시
		// //
		// allConfigInfo.setSnmpStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
		// // }
		// // else
		// // {// snmp State 값이 enable이 아니면 "실패" 표시
		// //
		// allConfigInfo.setSnmpStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
		// // }
		// //
		// if(allConfigInfo.getSyslogState().equals(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SYSLOG_PASK)))
		// // {//
		// //
		// allConfigInfo.setSyslogStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
		// // }
		// // else
		// // {
		// //
		// allConfigInfo.setSyslogStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
		// // }
		// break;
		// default:
		// break;
		// }
		// }
		// catch(Exception e)
		// {
		// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
		// e.getMessage());
		// }
		// return allConfigInfo;
		return null;
	}

	public OBAdcCfgInfo commonSysState(OBAdcCfgInfo configCheck) throws OBException, Exception {
		if (configCheck.getSyslogServerList() != null) {
			ArrayList<String> syslogList = configCheck.getSyslogServerList();
			String nameListString = "";
			if (syslogList.size() > 0 && syslogList != null) {
				for (String e : syslogList) {
					nameListString += e + "\n";
				}
				configCheck.setNameList(nameListString);
			} else {
				configCheck.setNameList("");
			}
		}
		if (configCheck.getSnmpState() == null) {// snmp State 값이 없으면 "-"로 표시
			configCheck.setSnmpStateResult("-");
		}
		if (configCheck.getSnmpRcommunityADC() == null) {// ADC 에서 가져온 snmp Community String 값이 없으면 "-"로 표시
			configCheck.setSnmpRcommunityResult("-");
		} else {// ADC 에서 가져온 snmp Community String 값이 있을 때

			if (configCheck.getSnmpRcommunityADC().equals(configCheck.getSnmpRCommunityDB())) {// ADC 에서 가져온 snmp
																								// Community String 값과
																								// DB에 저장된 snmp
																								// Community
																								// String 값이 같으면 "성공" 표시
				configCheck.setSnmpRcommunityResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
			} else {// ADC 에서 가져온 snmp Community String 값과 DB에 저장된 snmp Community
					// String 값이 같으면 "실패" 표시
				configCheck.setSnmpRcommunityResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
			}
		}
		if (configCheck.getFunction_loginState() == null) {
			configCheck.setFunction_loginStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
		} else {
			configCheck.setFunction_loginStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
		}

		if (configCheck.getFunction_snmpState() == null) {
			configCheck.setFunction_snmpStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
		} else {
			configCheck.setFunction_snmpStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
			configCheck.setFunction_snmpStateTime(new Date());
		}

		if (configCheck.getFunction_syslogState() == null) {
			configCheck.setFunction_syslogStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SYSLOG_FAIL));
		} else {
			configCheck.setFunction_syslogStateResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SYSLOG_SUCCESS));
			configCheck.setFunction_syslogStateTime(configCheck.getFunction_syslogStateTime());
		}

		return configCheck;
	}

	@Override
	public OBAdcCfgInfo checkAdcConfigState(Integer adcIndex) throws OBException {
		return null;
	}

	@Override
	public void configFaill(Integer adcIndex, String configType) throws OBException {
		OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

		try {
			// TODO
			OBAdcAlteonHandler alteon = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
			alteon.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
			OBAdcPASHandler pas = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
			pas.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
					adcInfo.getConnService(), adcInfo.getConnPort());
			OBAdcPASKHandler pask = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
			pask.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
					adcInfo.getConnService(), adcInfo.getConnPort());

			OBEnvManagementImpl adcsmartIpaddress = new OBEnvManagementImpl();
			String adcsmartIp = adcsmartIpaddress.getSystemConfig().getNetworkInfo().getIpAddress();
			if (configType.equals("10")) {// snmp 상태 설정
				if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_ALTEON)) {
					String config = "enable";
					alteon.login();
					alteon.cmndCfgSnmpSnmpv3(config);
					alteon.cmndApply();
					alteon.disconnect();
				} else if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_F5)) {
					iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo.getAdcIpAddress(),
							adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt());
					SystemF5.setSnmpState(interfaces);
				} else if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_PIOLINK_PAS)) {
					String config = "enable";
					pas.login();
					pas.cmndSnmpStatus(config);
					/* pas.apply(); */
					pas.disconnect();
				} else if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_PIOLINK_PASK)) {
					String config = "enable";
					pask.login();
					pask.cmndSnmpStatus(config);
					pask.apply();
					pask.disconnect();
				}

			} else if (configType.equals("11")) {// snmpRCommunity 설정
				OBAdcCfgInfo snmpRCommConfigInfo = new OBAdcCfgInfo();
				snmpRCommConfigInfo = getSnmpRCommConfigInfo(adcInfo);
				setSnmpReadCommunityString(snmpRCommConfigInfo.getSnmpRcommunityADC(), adcIndex);
			} else if (configType.equals("12")) {// syslogstate 설정
				if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_ALTEON)) {
					String config = "all enable";
					alteon.login();
					alteon.cmndCfgSysSyslog(config);
					alteon.cmndApply();
					alteon.disconnect();
				} else if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_PIOLINK_PAS)) {
					String config = "enable";
					pas.login();
					pas.cmndSyslogStatus(config);
					/* pas.apply(); */
					pas.disconnect();
				} else if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_PIOLINK_PASK)) {
					String config = "enable";
					pask.login();
					pask.cmndSyslogStatus(config);
					pask.apply();
					pask.disconnect();
				}
			} else if (configType.equals("13")) {// syslogList 설정

				if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_ALTEON)) {
					String config = "all enable";
					alteon.login();
					alteon.cmndCfgSysSyslog(config);
					alteon.cmndApply();
					alteon.disconnect();
				} else if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_F5)) {
					OBSShCmndExec sce = new OBSShCmndExec();
					sce.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
							adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
					sce.sshLogin();
					sce.sendCommand(
							"b syslog remote server" + " '" + adcInfo.getAdcAccount() + "' " + "host " + adcsmartIp);
					sce.sshDisconnect();
				} else if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_PIOLINK_PAS)) {
					pas.login();
					pas.cmndSyslogStatus(adcsmartIp + " " + "all notice");
					/* pas.apply(); */
					pas.disconnect();
				} else if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_PIOLINK_PASK)) {
					pask.login();
					pask.cmndSyslogList(adcsmartIp);
					pask.apply();
					pask.disconnect();
				}

			} else if (configType.equals("14")) {
				iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo.getAdcIpAddress(),
						adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt());

				SystemF5.setSnmpAllowList(interfaces, adcsmartIp);
			} else if (configType.equals("15")) {// vstat 설정
				String config = "e";
				alteon.login();
				alteon.cmndCfgSlbVstat(config);
				alteon.cmndApply();
				alteon.disconnect();

			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public OBAdcCfgInfo getLoginConfigState(OBDtoAdcInfo adcInfo) throws OBException, Exception {
		Integer adcType = adcInfo.getAdcType();
		OBAdcCfgInfo adc = new OBAdcCfgInfo();

		if (adcType.equals(OBDefine.ADC_TYPE_ALTEON)) {
			OBAdcSystemInfoAlteon loginConfigState = new OBAdcSystemInfoAlteon();
			loginConfigState.isAvailableSystem(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort(),
					adcInfo.getAdcCliAccount(), adcInfo.getAdcCliPasswordDecrypt());
			adc.setFunction_loginState(OBDefine.LOGIN_SUCCES);
			adc.setFunction_loginStateTime(new Date());
			return adc;
		} else if (adcType.equals(OBDefine.ADC_TYPE_F5)) {
			iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt());
			SystemF5.checkInterface(interfaces);
			adc.setFunction_loginState(OBDefine.LOGIN_SUCCES);
			adc.setFunction_loginStateTime(new Date());
			if (getCliLoginState(adcInfo.getAdcIpAddress(), adcInfo.getAdcCliAccount(),
					adcInfo.getAdcCliPasswordDecrypt(), adcInfo.getConnPort()) == true) {
				adc.setFunction_cliLoginState(OBDefine.LOGIN_SUCCES);
				adc.setFunction_cliLoginStateTime(new Date());
			}
			return adc;
		} else if (adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PAS)) {
			OBAdcSystemInfoPAS loginConfigState = new OBAdcSystemInfoPAS();
			loginConfigState.isAvailableSystem(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort(),
					adcInfo.getAdcCliAccount(), adcInfo.getAdcCliPasswordDecrypt());
			adc.setFunction_loginState(OBDefine.LOGIN_SUCCES);
			adc.setFunction_loginStateTime(new Date());
			return adc;
		} else if (adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PASK)) {
			OBAdcSystemInfoPASK loginConfigState = new OBAdcSystemInfoPASK();
			loginConfigState.isAvailableSystem(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort(),
					adcInfo.getAdcCliAccount(), adcInfo.getAdcCliPasswordDecrypt());
			adc.setFunction_loginState(OBDefine.LOGIN_SUCCES);
			adc.setFunction_loginStateTime(new Date());
			return adc;
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", "login fail"));
		adc.setFunction_loginState(OBDefine.CHECK_ADC_LOGINFAIL);
		return adc;
	}

	public boolean getCliLoginState(String ipAddress, String cliAcount, String cliPassword, int connPort)
			throws OBException, OBExceptionLogin, OBExceptionUnreachable {
		OBAdcF5Handler handler = new OBAdcF5Handler();

		try {
			handler.setConnectionInfo(ipAddress, cliAcount, cliPassword, connPort);
			handler.sshLogin();
			handler.disconnect();
			return true;
		}

		catch (OBExceptionUnreachable e) {
			OBSystemLog.error(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
			return false;
		} catch (OBExceptionLogin e) {
			OBSystemLog.error(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
			return false;
		} catch (Exception e) {
			OBSystemLog.error(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			return false;
		}
	}

	public OBAdcCfgInfo getSnmpConfigState(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCfgInfo snmpConfigState = new OBAdcCfgInfo();
		try {
			snmpConfigState = getAdcConfigInfo(adcInfo.getIndex());
			if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_ALTEON)) {
				snmpConfigState.setFunction_snmpState(new OBSnmpAlteon(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
						.getAdcHostname(OBDefine.ADC_TYPE_ALTEON, ""));
				return snmpConfigState;
			} else if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_F5)) {
				String swVersion = null;
				snmpConfigState.setFunction_snmpState(
						OBCommon.getValidSnmpF5Handler(swVersion, adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
								.getAdcHostname(OBDefine.ADC_TYPE_F5, ""));
				return snmpConfigState;
			} else if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_PIOLINK_PAS)) {
				snmpConfigState.setFunction_snmpState(new OBSnmpPAS(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
						.getAdcHostname(OBDefine.ADC_TYPE_PIOLINK_PAS, ""));
				return snmpConfigState;
			} else if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_PIOLINK_PASK)) {
				snmpConfigState.setFunction_snmpState(new OBSnmpPASK(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
						.getAdcHostname(OBDefine.ADC_TYPE_PIOLINK_PASK, ""));
				return snmpConfigState;
			}
		} catch (OBException e) {
			throw e;
		}
		return snmpConfigState;
	}

	public OBAdcCfgInfo getSyslogConfigState(Integer adcIndex) throws OBException {
		OBAdcCfgInfo adc = new OBAdcCfgInfo();
		OBDatabase db = new OBDatabase();
		ResultSet rs;

		String sqlText = "";
		try {
			db.openDB();

			sqlText = String.format(" SELECT LOG_SEQ, OCCUR_TIME, LOG_TIME, ADC_INDEX, LOG_LEVEL, EVENT	\n"
					+ " FROM LOG_ADC_SYSLOG WHERE ADC_INDEX = %d  				        \n"
					+ " AND OCCUR_TIME >= NOW() - interval'7 day'					        \n"
					+ " ORDER BY OCCUR_TIME DESC LIMIT 1                                  \n", adcIndex);

			rs = db.executeQuery(sqlText);
			while (rs.next()) {
				adc.setFunction_syslogState(db.getString(rs, "LOG_SEQ"));
				adc.setFunction_syslogStateTime(db.getTimestamp(rs, "OCCUR_TIME"));
			}
			return adc;
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

	public OBAdcCfgInfo getSyslogConfigInfo(OBDtoAdcInfo adcInfo) throws OBException, Exception {
		Integer adcType = adcInfo.getAdcType();
		OBAdcCfgInfo adc = new OBAdcCfgInfo();
		adc.setSnmpRCommunityDB(adcInfo.getSnmpRComm());
		adc.setAdcIpaddress(adcInfo.getAdcIpAddress());
		adc.setAdcPassword(adcInfo.getAdcPassword());
		adc.setAdcId(adcInfo.getAdcAccount());
		adc.setAdcType(adcInfo.getAdcType());
		// adc = getAdcConfigInfo(adcIndex);
		if (adcType.equals(OBDefine.ADC_TYPE_ALTEON)) {
			OBAdcAlteonHandler alteon = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
			alteon.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
			alteon.login();
			String syslogState = alteon.cmndCfgSyslog();
			alteon.disconnect();
			String trimSyslogState[] = syslogState.split("\n");
			String syslogText = "syslogging all features";
			int trimSyslogLength = trimSyslogState.length;
			for (int i = 0; i < trimSyslogLength; i++) {
				if (trimSyslogState[i].equals(syslogText)) {
					adc.setSyslogState(syslogText);
					break;
				} else
					adc.setSyslogState("Not syslogging all");
			}
			return adc;
		} else if (adcType.equals(OBDefine.ADC_TYPE_F5)) {
			return adc;
		} else if (adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PAS)) {
			OBAdcPASHandler pas = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
			pas.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
					adcInfo.getConnService(), adcInfo.getConnPort());
			pas.login();
			String syslogState = pas.cmndSyslogInfo();
			pas.disconnect();
			String temp[] = syslogState.split("Logging Server = ");
			String temp2[] = temp[1].split("\n");
			adc.setSyslogState(temp2[0]);
			return adc;
		} else if (adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PASK)) {
			// OBAdcPASKHandler pask = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
			// pask.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
			// adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(),
			// adcInfo.getConnPort());
			// pask.login();
			// String snmp = pask.cmndSyslogInfo();
			// pask.disconnect();
			// String temp[] = snmp.split(" Server Status : ", 2);
			// String snmpState[] = temp[1].split("\n");
			//
			// if(snmpState[0].equals(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SNMP_PASK)))
			// {
			// adc.setSyslogState(snmpState[0]);
			// }
			// else
			// {
			// adc.setSyslogState(snmpState[0]);
			// }
			return adc;
		}
		return null;
	}

	public OBAdcCfgInfo getSnmpRCommConfigInfo(OBDtoAdcInfo adcInfo) throws OBException, Exception {
		Integer adcType = adcInfo.getAdcType();

		OBAdcCfgInfo adc = new OBAdcCfgInfo();
		adc.setSnmpRCommunityDB(adcInfo.getSnmpRComm());
		adc.setAdcIpaddress(adcInfo.getAdcIpAddress());
		adc.setAdcPassword(adcInfo.getAdcPassword());
		adc.setAdcId(adcInfo.getAdcAccount());
		adc.setAdcType(adcInfo.getAdcType());
		// adc = getAdcConfigInfo(adcIndex);

		if (adcType.equals(OBDefine.ADC_TYPE_ALTEON)) {
			OBAdcAlteonHandler alteon = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
			alteon.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());

			alteon.login();
			String snmpRComm = alteon.cmndCfgSnmp();
			alteon.disconnect();
			String snmpRCommunityResult = "";
			if (snmpRComm != "") {
				String snmpRCommunity[] = snmpRComm.split("\n");
				String trimSnmpRCommunity = "";
				String regex1 = "\\b(?<=Read community string: \").*(?=\")\\b";
				for (int i = 0; i < snmpRCommunity.length; i++) {
					trimSnmpRCommunity = snmpRCommunity[i];
					Pattern pSyslog = Pattern.compile(regex1);
					Matcher mc = pSyslog.matcher(trimSnmpRCommunity);
					while (mc.find()) {
						snmpRCommunityResult = mc.group();
					}
				}
				adc.setSnmpRcommunityADC(snmpRCommunityResult);
				return adc;
			}
			adc.setSnmpRcommunityADC("");
			return adc;
		} else if (adcType.equals(OBDefine.ADC_TYPE_F5)) {
			// iControl.Interfaces interfaces =
			// CommonF5.initInterfaces(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
			// adcInfo.getAdcPasswordDecrypt());
			// adc.setSnmpRcommunityADC(SystemF5.getSnmpReadCommunity(interfaces));
			// return adc;

		} else if (adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PAS)) {
			OBAdcPASHandler pas = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
			pas.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
					adcInfo.getConnService(), adcInfo.getConnPort());
			pas.login();
			String snmpRcomm = pas.cmndSnmpInfo();
			pas.disconnect();
			String temp[] = snmpRcomm.split("Community Name = ");
			String snmpRcommADC[] = temp[1].split("\n");
			adc.setSnmpRcommunityADC(snmpRcommADC[0]);
			return adc;

		} else if (adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PASK)) {
			OBAdcPASKHandler pask = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
			pask.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
					adcInfo.getConnService(), adcInfo.getConnPort());

			pask.login();
			String snmpRcomm = pask.cmndSnmpRComm();
			pask.disconnect();
			String temp[] = snmpRcomm.split(" Name Policy Limit-Oid", 2);
			String temp1[] = temp[1]
					.split("================================================================================", 2);
			String temp2[] = temp1[0].split("\n");

			for (String line : temp2) {
				if (line.isEmpty())
					continue;
				String snmpRcommADC[] = line.split(" ");
				if (snmpRcommADC[2].equals("read-only")) {
					adc.setSnmpRcommunityADC(snmpRcommADC[1]);
					break;
				}
			}
			return adc;
		}
		return adc;
	}

	// public OBAdcCfgInfo getSnmpAllowListConfigInfo(OBDtoAdcInfo adcInfo) throws
	// OBException, Exception
	// {
	// Integer adcType = adcInfo.getAdcType();
	//
	// OBAdcCfgInfo adc = new OBAdcCfgInfo();
	// adc.setSnmpRCommunityDB(adcInfo.getSnmpRComm());
	// adc.setAdcIpaddress(adcInfo.getAdcIpAddress());
	// adc.setAdcPassword(adcInfo.getAdcPassword());
	// adc.setAdcId(adcInfo.getAdcAccount());
	// adc.setAdcType(adcInfo.getAdcType());
	//
	// OBEnvManagementImpl adcsmartIpaddress = new OBEnvManagementImpl();
	// String adcsmartIp =
	// adcsmartIpaddress.getSystemConfig().getNetworkInfo().getIpAddress();
	// iControl.Interfaces interfaces =
	// CommonF5.initInterfaces(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
	// adcInfo.getAdcPasswordDecrypt());
	//
	//
	// if(adcType.equals(OBDefine.ADC_TYPE_F5))
	// {
	// ArrayList<String> allowList = SystemF5.getSnmpAllowList(interfaces);
	// String setAllowList = "";
	// for(int i = 0; i < allowList.size(); i++)
	// {
	// setAllowList += allowList.get(i) + "\n";
	// }
	// adc.setAllowList(setAllowList);
	// int allowListSize = allowList.size();
	// for(int i = 0; i < allowListSize; i++)
	// {
	// int Cidr = getCidr(allowList.get(i));
	// if(Comparison(allowList.get(i), Cidr, adcsmartIp) == 1)
	// {
	// adc.setAllowListResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_SUCCESS));
	// break;
	// }
	// else
	// {
	// adc.setAllowListResult(OBMessages.getMessage(OBMessages.MSG_SYSTEM_FAIL));
	// }
	// }
	// return adc;
	// }
	// else
	// {
	// return adc;
	// }
	// }

	// public int IPv4(String ip) throws OBException
	// {
	// String[] IpCidr = ip.split("/");
	// String ipaddress = IpCidr[0];
	// IpCidr = ipaddress.split("\\.");
	//
	// int i = 24;
	// int baseIPnumeric = 0;
	// for(int n = 0; n < IpCidr.length; n++)
	// {
	// int valuet = Integer.parseInt(IpCidr[n]);
	// baseIPnumeric += valuet << i;
	// i -= 8;
	// }
	// return baseIPnumeric;
	// }

	// public int Comparison(String snmpClientAllowIp, int Cidr, String adcsmartIP)
	// throws OBException
	// {
	// int snmpip = IPv4(snmpClientAllowIp);
	// int adcsmartip = IPv4(adcsmartIP);
	// int count = 0;
	// String[] IpCidr = snmpClientAllowIp.split("/");
	// String ipaddress = IpCidr[0];
	// String[] trimIpCidr = ipaddress.split("\\.");
	// String[] trimAdcIp = adcsmartIP.split("\\.");
	// for(int i = 0; i < trimIpCidr.length; i++)
	// {
	// if(trimIpCidr[i].equals(trimAdcIp[i]))
	// {
	// count++;
	// }
	// if(count == trimIpCidr.length)
	// {
	// return 1;
	// }
	// }
	// int Subtraction = 32 - Cidr;
	// int ip1 = (snmpip >> Subtraction);
	// int ip2 = (adcsmartip >> Subtraction);
	//
	// if(ip1 == ip2)
	// {
	// return 1;
	// }
	// else
	// {
	// return 2;
	// }
	// }
	//
	// public long getBit(String netNotation) throws OBException
	// {
	// String[] values = netNotation.split("\\.");
	// long result = 0;
	// for(int i = 0; i < values.length; i++)
	// {
	// result |= Integer.valueOf(values[i]);
	// if(i + 1 < values.length)
	// result <<= 8;
	// }
	// return result;
	// }

	// public int maskValueOf(String maskString) throws OBException
	// {
	// long mask = getBit(maskString);
	// int zeroCnt = 0;
	// for(int i = 0; i < 32; i++)
	// {
	// if((mask & 1) == 0)
	// {
	// zeroCnt++;
	// mask >>= 1;
	// }
	// else
	// {
	// return 32 - zeroCnt;
	// }
	// }
	// return 32 - zeroCnt;
	// }
	//
	// public int getCidr(String ipaddress) throws OBException
	// {
	// String[] parts = ipaddress.split("/");
	//
	// int prefix;
	// if(parts.length < 2)
	// {
	// String[] clientIp = parts[0].split("\\.");
	// prefix = 0;
	// prefix = clientIp.length * 8;
	//
	// }
	// else if(parts[1].matches(".*[.].*"))
	// {
	// prefix = maskValueOf(parts[1]);
	// }
	// else
	// {
	// prefix = Integer.parseInt(parts[1]);
	// }
	// return prefix;
	// }

	// public Long startConnectionTest(OBDtoAdcInfo adcInfo) throws OBException
	// {
	// return null;
	// }

	// public OBDtoConnTestStatus getConnectionTestStatus(Long testID) throws
	// OBException
	// {
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// db.openDB();
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	//
	// OBDtoConnTestStatus retVal = null;
	// try
	// {
	// retVal = getConnectionTestStatus(testID, db);
	// }
	// catch(OBException e)
	// {
	// db.closeDB();
	// throw e;
	// }
	// catch(Exception e)
	// {
	// db.closeDB();
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// db.closeDB();
	// return retVal;
	// }
	//
	// private OBDtoConnTestStatus getConnectionTestStatus(Long testID, OBDatabase
	// db) throws OBException
	// {
	// OBDtoConnTestStatus retVal = new OBDtoConnTestStatus();
	// String sqlText="";
	// try
	// {
	// sqlText=String.format(" SELECT INDEX, OCCUR_TIME, ADC_INDEX, ITEM_ID, STATUS,
	// SUMMARY \n" +
	// " FROM LOG_TEST_CONNECTION \n" +
	// " WHERE INDEX = %d \n" +
	// " ORDER BY ITEM_ID ASC \n",
	// testID);
	//
	// // default value
	// retVal.setTestID(OBDtoConnTestStatus.TEST_ID_MAIN);
	// retVal.setTestResult(OBDtoConnTestStatus.TEST_STATUS_INIT);
	// retVal.setTestResultStr("");
	// ArrayList<OBDtoConnTestStatus> subList = new
	// ArrayList<OBDtoConnTestStatus>();
	// retVal.setTestItemList(subList);
	// ResultSet rs = db.executeQuery(sqlText);
	// while(rs.next())
	// {
	// int itemID = db.getInteger(rs, "ITEM_ID");
	// if(itemID == OBDtoConnTestStatus.TEST_ID_MAIN)
	// {
	// retVal.setTestID(OBDtoConnTestStatus.TEST_ID_MAIN);
	// retVal.setTestResult(db.getInteger(rs, "STATUS"));
	// retVal.setTestResultStr(db.getString(rs, "SUMMARY"));
	// continue;
	// }
	// OBDtoConnTestStatus obj = new OBDtoConnTestStatus();
	// obj.setTestID(db.getInteger(rs, "ITEM_ID"));
	// obj.setTestResult(db.getInteger(rs, "STATUS"));
	// obj.setTestResultStr(db.getString(rs, "SUMMARY"));
	// subList.add(obj);
	// }
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// return retVal;
	// }
	//
	// private void addConnectionTestStatus(Integer adcIndex, Long testID, Integer
	// itemID, Timestamp occurTime, Integer status, String summary, OBDatabase db)
	// throws OBException
	// {
	// String sqlText="";
	// try
	// {
	// sqlText=String.format(" INSERT INTO LOG_TEST_CONNECTION \n" +
	// " ( INDEX, OCCUR_TIME, ADC_INDEX, ITEM_ID, STATUS, SUMMARY ) \n" +
	// " VALUES \n" +
	// " (%d, %s, %d, %d, %d, %s) \n",
	// testID,
	// OBParser.sqlString(occurTime),
	// adcIndex,
	// itemID,
	// status,
	// OBParser.sqlString(summary));
	//
	// db.executeUpdate(sqlText);
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }
	//
	// private void updateConnectionTestStatus(Long testID, Integer itemID, Integer
	// status, String summary, OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// try
	// {
	// String setText = "";
	// if(status!=null)
	// setText += String.format("STATUS = %d ", status);
	// if(summary!=null)
	// {
	// if(!setText.isEmpty())
	// setText += ", ";
	// setText += String.format("STATUS = %d ", status);
	// }
	// if(setText.isEmpty())
	// return;
	//
	// sqlText = String.format(" UPDATE LOG_TEST_CONNECTION \n" +
	// " SET %s \n" +
	// " WHERE INDEX=%d AND ITEM_ID = %d ; \n",
	// setText,
	// testID, itemID);
	//
	// db.executeUpdate(sqlText);
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	/**
	 * perform connection test
	 * 
	 * @param adcInfo
	 * @param logIndex . index value of LOG_TEST_CONNECTION table. if logIndex is
	 *                 null or 0, does not leave any logs on LOG_TEST_CONNECTION
	 * @return 정상. 단절, 비정상 제공. OBDefine.ADC_STATUS에 정의됨.
	 * @throws OBException
	 */
	// public Integer connectionTest(OBDtoAdcInfo adcInfo, Long logIndex, OBDatabase
	// db) throws OBException
	// {
	// if(logIndex!=null && logIndex.longValue()!=0)
	// {// 사용자에 의해 진행되고 있는 접속 테스트임. 화면에 진행상태를 표시하기 위해서 로그를 남김.
	// Timestamp occurTime = OBDateTime.toTimestamp(OBDateTime.now());
	// // addConnectionTestStatus(adcInfo.getIndex(), logIndex,
	// OBDtoConnTestStatus.TEST_ID_MAIN, occurTime,
	// OBDtoConnTestStatus.TEST_STATUS_ING, "", db);
	// }
	// Integer retVal = OBDefine.ADC_STATUS.REACHABLE;
	// switch (adcInfo.getAdcType())
	// {
	// case OBDefine.ADC_TYPE_ALTEON:
	// retVal = connectionTestAlteon(adcInfo, logIndex, db);
	// break;
	// case OBDefine.ADC_TYPE_F5:
	// retVal = connectionTestF5(adcInfo, logIndex, db);
	// break;
	// case OBDefine.ADC_TYPE_PIOLINK_PAS:
	// retVal = connectionTestPAS(adcInfo, logIndex, db);
	// break;
	// case OBDefine.ADC_TYPE_PIOLINK_PASK:
	// retVal = connectionTestPASK(adcInfo, logIndex, db);
	// break;
	// case OBDefine.ADC_TYPE_PIOLINK_UNKNOWN:
	// retVal = connectionTestUnknownPAS(adcInfo, logIndex, db);
	// break;
	// default:
	// throw new OBException(OBException.ERRCODE_NOT_SUPPORT_VENDOR);
	// }
	//
	// if(logIndex!=null && logIndex.longValue()!=0)
	// {// 사용자에 의해 진행되고 있는 접속 테스트임. 화면에 진행상태를 표시하기 위해서 로그를 남김.
	// // updateConnectionTestStatus(logIndex, OBDtoConnTestStatus.TEST_ID_MAIN,
	// OBDtoConnTestStatus.TEST_STATUS_DONE, retVal.toString(), db);
	// }
	// return retVal;
	// }

	/**
	 * 웹에서 호출되는 interface임. 입력된 패스워드는 plain text 형태로 입력된다.
	 */
	@Override
	public OBAdcCheckResult checkADCStatus(OBDtoAdcInfo adcInfo, Integer checkItem, boolean isNewADC)
			throws OBException {
		OBAdcCheckResult retVal = null;
		try {
			// 입력된 패스워드는 plain text 형태로 입력된다. 이를 암호화 하여 처리한다.
			String passWord = new OBCipherAES().Encrypt(adcInfo.getAdcPassword());
			adcInfo.setAdcPassword(passWord);
			if (adcInfo.getAdcType() != OBDefine.ADC_TYPE_ALTEON) {
				adcInfo.setAdcCliAccount(adcInfo.getAdcCliAccount());
				adcInfo.setAdcCliPassword(new OBCipherAES().Encrypt(adcInfo.getAdcCliPassword()));
			} else {
				adcInfo.setAdcCliAccount(adcInfo.getAdcAccount());
				adcInfo.setAdcCliPassword(new OBCipherAES().Encrypt(adcInfo.getAdcPassword()));
			}
			retVal = new OBCheckAdcStatus().checkADCStatus(adcInfo, checkItem, isNewADC);

		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return retVal;
	}

	@Override
	public ArrayList<OBAdcConfigInfo> checkAdcConfigInfo(Integer adcIndex) throws OBException {

		ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>();
		try {
			retVal = new OBCheckAdcStatus().checkAdcConfigInfo(adcIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return retVal;
	}

	@Override
	public boolean setAdcConfigInfo(Integer adcIndex, OBAdcConfigInfo adcInfo) throws OBException {
		OBDatabase db = new OBDatabase();
		boolean retVal = false;
		try {
			db.openDB();
			retVal = new OBCheckAdcConfig().setAdcConfigInfo(adcIndex, adcInfo, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	/**
	 * ADC 연결 테스트를 진행한다. 모든 항목을 확인한다.
	 */
	@Override
	public ArrayList<OBAdcCheckResult> checkADCStatusAll(Integer adcIndex) throws OBException {
		ArrayList<OBAdcCheckResult> retVal = new ArrayList<OBAdcCheckResult>();
		try {
			retVal = new OBCheckAdcStatus().checkADCStatusAll(adcIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	@Override
	public ArrayList<OBDtoLastAdcCheckTime> getLastAdcCheckTime(Integer adcIndex) throws OBException {
		ArrayList<OBDtoLastAdcCheckTime> retVal = new ArrayList<OBDtoLastAdcCheckTime>();
		try {
			retVal = new OBCheckAdcStatus().getLastAdcCheckTime(adcIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return retVal;
	}

	@Override
	public ArrayList<OBDtoAdcNoticeGroup> getNoticeGrp(Integer adcIndex) throws OBException {
		return new OBVServerDB().getNoticeGrpList(adcIndex);
	}

	@Override
	public void setNoticeGroup(Integer adcIndex, ArrayList<OBDtoAdcNoticeGroup> noticeGroupList, Integer accntIndex,
			OBDtoExtraInfo extraInfo) throws OBException {
		boolean isSuccessful = false;
		try {
			new OBVServerDB().setNoticeGroup(adcIndex, noticeGroupList, accntIndex, extraInfo);
//			new OBVServerDB().delDeadVServerNotice(adcIndex);
			isSuccessful = true;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			OBDtoAdcInfo adcInfo = getAdcInfo(adcIndex);

			if (isSuccessful == true) {
				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcIndex,
						extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_ADC_SET_NOTICE_GROUP_SUCCESS,
						adcInfo.getName(), null);
			} else {
				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcIndex,
						extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_ADC_SET_NOTICE_GROUP_FAIL,
						adcInfo.getName(), null);
			}
		}
	}

	//
	// public boolean isNotice(Integer adcIndex, OBDatabase db) throws OBException
	// {
	// String sqlText;
	// sqlText = String.format(" SELECT " +
	// " SYNC_STATE " +
	// " FROM TMP_ADC_ADDITIONAL " +
	// " WHERE ADC_INDEX=%d ;",
	// adcIndex);
	// boolean result = false;
	// ResultSet rs=null;
	// try
	// {
	// rs = db.executeQuery(sqlText);
	// if(rs.next() == true)
	// {
	// Integer syncState = db.getInteger(rs, "SYNC_STATE");
	// if(syncState!=null && syncState==OBDefine.STATE_ENABLE)
	// result = true;
	// }
	// return result;
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }
	//
	public boolean isGroupNotice(String groupIndex) throws OBException {
		if (groupIndex == null) {
			return false;
		}
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		ResultSet rs;
		try {
			db.openDB();
			sqlText = String.format(" SELECT POOL_INDEX FROM MNG_NOTICE_GROUP WHERE POOL_INDEX = %s",
					OBParser.sqlString(groupIndex));

			rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}
	}

	public boolean isNoticePoolUsed(String poolIndex) throws OBException {
		if (poolIndex == null) {
			return false;
		}
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		ResultSet rs;
		try {
			db.openDB();
			sqlText = String.format(" SELECT NOTICE_POOL_INDEX FROM TMP_VS_NOTICE WHERE NOTICE_POOL_INDEX = %s ",
					OBParser.sqlString(poolIndex));

			rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}
	}

	@Override
	public void updateAdcStatusReachable(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();

			sqlText = String.format(" UPDATE MNG_ADC " + " SET STATUS=%d " + " WHERE INDEX=%d; ",
					OBDefine.ADC_STATUS.REACHABLE, adcIndex);
			db.executeUpdate(sqlText);
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

	@Override
	public void updateAdcStatusUnReachable(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();

			sqlText = String.format(" UPDATE MNG_ADC " + " SET STATUS=%d " + " WHERE INDEX=%d;",
					OBDefine.ADC_STATUS.UNREACHABLE, adcIndex);
			db.executeUpdate(sqlText);
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

	/**
	 * ADC 연결 테스트를 진행한다. 네트워크 접근, snmp 테스트만 진행한다.
	 */
	@Override
	public boolean checkADCStatusMonitoring(Integer adcIndex) throws OBException {
		boolean retVal;
		try {
			retVal = new OBCheckAdcStatus().checkADCStatusMonitoring(adcIndex, false);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {// a일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	@Override
	public boolean snmpInfoCheck(Integer adcIndex) throws OBException {
		boolean result = false;
		OBDtoAdcInfo adcInfo;
		adcInfo = getAdcInfo(adcIndex);

		if (adcInfo.getSwVersion() != null) {
			result = true;
		} else {
			result = updateAdcModelVersionTimeInfo(adcInfo); // snmp를 이용하여 장비 정보 추출
		}

		return result;
	}

	public Integer getPropertyLogLimitInfo() {
		String PROPERTIES_PATH = OBUtility.getPropOsPath();
		String PROPERTIES_BASE = OBDefine.PROPERTIES_BASE;
		int retVal = 0;

		try {
			Properties props = new Properties();
			FileInputStream fis = new FileInputStream(PROPERTIES_PATH + PROPERTIES_BASE);
			;
			props.load(fis);
			retVal = Integer.parseInt(props.getProperty("log_data_limit"));

		} catch (FileNotFoundException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					"not found obapps conf file. please check your " + PROPERTIES_PATH + PROPERTIES_BASE + " file.");
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
		} catch (IOException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, "failed to open obapps conf file. please check your "
					+ PROPERTIES_PATH + PROPERTIES_BASE + " file.");
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
		}

		return retVal;
	}

	public boolean addRespSectionCheck(OBDtoRespGroup OBDtoRespGroup) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. respInfo:%s", OBDtoRespGroup));

		try {
			if (OBDtoRespGroup == null) {
				return false;
			}
			// 응답시간 이름이 있는지 확인 한다.
			if (isExistRespSectionCheck(OBDtoRespGroup.getName())) {
				return false;
			}
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
					String.format("Response Interval Check Add: resp list = %s", OBDtoRespGroup));
			new OBVServerDB().addRespSectionCheck(OBDtoRespGroup);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "Response Interval Check Add END");
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
		return true;
	}

	public ArrayList<OBDtoRespInfo> getRespList() throws OBException {
		ArrayList<OBDtoRespInfo> retVal = new ArrayList<OBDtoRespInfo>();
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "All Response List START");
//        retVal = new OBVServerDB().get();
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "All Response List END");
		return retVal;
	}

	public ArrayList<OBDtoRespGroup> getResponseTimeList(OBDtoSearch searchOption, Integer orderType, Integer orderDir)
			throws OBException {
		ArrayList<OBDtoRespGroup> retVal = new ArrayList<OBDtoRespGroup>();
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "All Response time List START");
		retVal = new OBVServerDB().getResponseTimeList(searchOption, orderType, orderDir);
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "All Response time List END");
		return retVal;
	}

	@Override
	public void delRespSectionCheck(ArrayList<String> respIndex) throws OBException {
		try {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
					String.format("Response Interval Check Delete: resp Index = %s", respIndex));
			new OBVServerDB().delRespSectionCheck(respIndex); // Real
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "Response Interval Check Delete END");
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
	}

	@Override
	public OBDtoRespGroup getRespSectionCheck(Integer respIndex) throws OBException {
		OBDtoRespGroup retVal = new OBVServerDB().getRespSectionCheck(respIndex);
		return retVal;
	}

	@Override
	public boolean isExistRespSectionCheck(String respName) throws OBException {
		boolean retVal = false;
		String sqlText = "";
		if (respName == null || respName.isEmpty()) {
			return retVal;
		}
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT NAME FROM MNG_RESP_SECTION_GROUP                       \n"
							+ " WHERE NAME=%s;                                                              \n",
					OBParser.sqlString(respName));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = true;
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
		return retVal;
	}

	@Override
	public boolean isExistSlbUserCheck(OBDtoSlbUser slbUser) throws OBException {
		boolean retVal = false;
		String sqlText = "";
		if (slbUser == null) {
			return retVal;
		}
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
//            sqlText = String.format(" SELECT * FROM MNG_SLB_USER                                \n" 
//                    + " WHERE INDEX = %d AND PHONE = %s;                                        \n"
//                    , slbUser.getIndex(), OBParser.sqlString(slbUser.getPhone()));
			sqlText = String.format(
					" SELECT * FROM MNG_SLB_USER                                \n"
							+ " WHERE INDEX = %d;                                                       \n",
					slbUser.getIndex());

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = true;
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
		return retVal;
	}

	@Override
	public boolean setRespSectionCheck(OBDtoRespGroup respInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. respInfo:%s", respInfo));

		try {
			if (respInfo == null) {
				return false;
			}

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
					String.format("Response Interval Check Update: resp list = %s", respInfo));
			OBDtoRespGroup oldRespGroup = getRespSectionCheck(respInfo.getIndex());
			ArrayList<OBDtoRespInfo> updateList = new ArrayList<OBDtoRespInfo>();

			int newSize = respInfo.getRespInfo().size();
			for (int i = 0; i < newSize; i++) {
				if (respInfo.getRespInfo().get(i) == null) {
					updateList.add(respInfo.getRespInfo().get(i));
					continue;
				}
				if (respInfo.getRespInfo().get(i).getIndex() == null || respInfo.getRespInfo().get(i).getIndex() == 0) {
					// Insert
					new OBVServerDB().addRespSectionCheckEach(respInfo.getRespInfo().get(i), respInfo.getIndex());
					updateList.add(respInfo.getRespInfo().get(i));
				}
			}
			if (updateList != null && !updateList.isEmpty()) {
				respInfo.getRespInfo().removeAll(updateList);
			}

			updateList.clear();

			int oldSize = oldRespGroup.getRespInfo().size();
			for (int i = 0; i < oldSize; i++) {
				for (OBDtoRespInfo newResp : respInfo.getRespInfo()) {
					if (oldRespGroup.getRespInfo().get(i).getIndex().equals(newResp.getIndex())) {
						// Update
						new OBVServerDB().setRespSectionCheck(respInfo.getIndex(), respInfo.getName(), newResp);
						updateList.add(oldRespGroup.getRespInfo().get(i));
					}
				}
			}

			oldRespGroup.getRespInfo().removeAll(updateList);

			if (oldRespGroup.getRespInfo() != null && !oldRespGroup.getRespInfo().isEmpty()) {
				for (OBDtoRespInfo delResp : oldRespGroup.getRespInfo()) {
					// Delete
					new OBVServerDB().delRespSectionCheckEach(delResp.getIndex());
				}
			}

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "Response Interval Check Update END");
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("respInfo end."));
		return true;
	}

	@Override
	public boolean addVSServiceGroup(OBDtoVSGroupInfo vsGroupInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. vsGroupInfo:%s", vsGroupInfo));

		try {
			if (vsGroupInfo == null) {
				return false;
			}
			// 응답시간 이름이 있는지 확인 한다.
			if (isExistVSServiceGroup(vsGroupInfo.getName())) {
				return false;
			}
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("vs Group Add: vsGroup list = %s", vsGroupInfo));
			new OBVServerDB().addVSServiceGroup(vsGroupInfo);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "vs Group Add END");
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
		return true;
	}

	@Override
	public boolean setVSServiceGroup(OBDtoVSGroupInfo vsGroupInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. Modify vsGroupInfo:%s", vsGroupInfo));

		try {
			if (vsGroupInfo == null) {
				return false;
			}

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("vs Group Modify: vsGroup list = %s", vsGroupInfo));
			new OBVServerDB().setVSServiceGroup(vsGroupInfo);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "vs Group Modify END");
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("Modify end."));
		return true;
	}

	@Override
	public void delVSServiceGroup(ArrayList<Integer> groupIndexList) throws OBException {
		try {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
					String.format("Group List Delete: Group List = %s", groupIndexList));
			new OBVServerDB().delVSServiceGroup(groupIndexList); // Real
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "Group List Delete END");
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}

	}

	@Override
	public boolean isExistVSServiceGroup(String vsGroupName) throws OBException {
		String sqlText = "";

		if (vsGroupName == null) {
			return false;
		}
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX FROM MNG_VSSERVICE_GROUP WHERE NAME=%s; ",
					OBParser.sqlString(vsGroupName));

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return false;
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
		return true;
	}

	@Override
	public OBDtoVSGroupInfo getVSServiceGroup(Integer vsGroupIndex, Integer accountIndex) throws OBException {
		OBDtoVSGroupInfo retVal = new OBVServerDB().getVSServiceGroup(vsGroupIndex, accountIndex);
		return retVal;
	}

	@Override
	public ArrayList<OBDtoADCGroupInfo> getVSServiceGroupAll(Integer vsGroupIndex, Integer accountIndex)
			throws OBException {
		ArrayList<OBDtoADCGroupInfo> retVal = new OBVServerDB().getVSServiceGroupAll(vsGroupIndex, accountIndex);
		return retVal;
	}

	@Override
	public ArrayList<OBDtoVSGroupInfo> getVSServiceGroup(String searchKey, Integer beginIndex, Integer endIndex,
			Integer orderType, Integer orderDir) throws OBException {
		ArrayList<OBDtoVSGroupInfo> retVal = new OBVServerDB().getVSServiceGroup(searchKey, beginIndex, endIndex,
				orderType, orderDir);
		return retVal;
	}

	@Override
	public Integer getVSServiceGroupTotalCount(String searchKey) throws OBException {
		return new OBVServerDB().getVSServiceGroupTotalCount(searchKey);
	}

	@Override
	public ArrayList<OBDtoRespMultiChartData> getResponseTimeHistory(Integer respIndex, OBDtoSearch searchOption)
			throws OBException {
		ArrayList<OBDtoRespMultiChartData> retVal = new OBVServerDB().getLogRespSectionCheck(respIndex, searchOption);
		return retVal;
	}

	@Override
	public void addSlbUser(OBDtoSlbUser slbUser) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. slbUser:%s", slbUser));

		try {
			if (slbUser == null) {
				return;
			}
			// 응답시간 이름이 있는지 확인 한다.
			if (isExistSlbUserCheck(slbUser)) {
				return;
			}
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("SLB User Add: slbUser = %s", slbUser));
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			slbUser.setUpdateTime(now);
			new OBVServerDB().addSlbUser(slbUser);
//            new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_ACCNTGROUP_SET_SUCCESS, groupInfo.getName());
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "SLB User Add END");
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	@Override
	public void setSlbUser(OBDtoSlbUser slbUser) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. Set slbUser:%s", slbUser));
		try {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("SLB User Set: slbUser = %s", slbUser));
			Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
			slbUser.setUpdateTime(now);
			new OBVServerDB().setSlbUser(slbUser);
//            new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_ACCNTGROUP_SET_SUCCESS, groupInfo.getName());
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("SLB User Set End"));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("Set slbUser End."));
	}

	@Override
	public void delSlbUser(ArrayList<Integer> slbUserIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. Del slbUser:%s", slbUserIndex));
		try {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("SLB User Del: slbUser = %s", slbUserIndex));
			new OBVServerDB().delSlbUser(slbUserIndex);
//            new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_ACCNTGROUP_SET_SUCCESS, groupInfo.getName());
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("SLB User Del End"));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("Del slbUser End."));
	}

	@Override
	public Integer getSlbUserListCount(Integer userType, Integer accntIndex, String searchKey) throws OBException {
		return new OBVServerDB().getSlbUserListCount(userType, accntIndex, searchKey);
	}

	@Override
	public ArrayList<OBDtoSlbUser> getSlbUserList(Integer userType, Integer beginIndex, Integer endIndex,
			Integer orderType, Integer orderDir) throws OBException {
		ArrayList<OBDtoSlbUser> retVal = new OBVServerDB().getSlbUserList(userType, beginIndex, endIndex, orderType,
				orderDir);
		return retVal;
	}

	@Override
	public boolean isExistSlbSchedule(OBDtoAdcSchedule adcSchedule) throws OBException {
		boolean retVal = false;
		String sqlText = "";
		if (adcSchedule == null) {
			return retVal;
		}
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT * FROM MNG_SLB_SCHEDULE                             \n"
							+ " WHERE RESERVATION_TIME = %s                                              \n",
					OBParser.sqlString(adcSchedule.getReservationTime()));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = true;
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
		return retVal;
	}

	@Override
	public void addSlbSchedule(OBDtoSlbUser slbUser, OBDtoAdcSchedule slbSchedule, OBDtoAdcVServerF5 configF5,
			OBDtoAdcVServerAlteon configAlteon) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. slbSchedule:%s", slbSchedule));

		try {
			if (slbUser == null || slbSchedule == null) {
				return;
			}

			if (configF5 != null) {
				OBDtoAdcConfigChunkF5 chunkF5 = new OBDtoAdcConfigChunkF5();
				OBDtoAdcConfigVServerF5 f5 = new OBDtoAdcConfigVServerF5();
				f5.setVsNew(configF5);
				if (slbSchedule.getChangeType() == OBDefine.CHANGE_TYPE_ADD) {
					chunkF5.setChangeType(OBDefine.CHANGE_TYPE_ADD);
				} else if (slbSchedule.getChangeType() == OBDefine.CHANGE_TYPE_EDIT) {
					chunkF5.setChangeType(OBDefine.CHANGE_TYPE_EDIT);
				} else if (slbSchedule.getChangeType() == OBDefine.CHANGE_TYPE_DELETE) {
					chunkF5.setChangeType(OBDefine.CHANGE_TYPE_DELETE);
				} else {
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format("Change Type another Number : %d", slbSchedule.getChangeType()));
				}
				chunkF5.setVsConfig(f5);
				chunkF5.setChangeObject(OBDefine.CHANGE_OBJECT_VIRTUALSERVER);
				slbSchedule.setChunkF5(chunkF5);
			}
			if (configAlteon != null) {
				OBDtoAdcConfigChunkAlteon chunkAlteon = new OBDtoAdcConfigChunkAlteon();
				OBDtoAdcConfigVServerAlteon alteon = new OBDtoAdcConfigVServerAlteon();
				alteon.setVsNew(configAlteon);
				if (slbSchedule.getChangeType() == OBDefine.CHANGE_TYPE_ADD) {
					chunkAlteon.setChangeType(OBDefine.CHANGE_TYPE_ADD);
				} else if (slbSchedule.getChangeType() == OBDefine.CHANGE_TYPE_EDIT) {
					chunkAlteon.setChangeType(OBDefine.CHANGE_TYPE_EDIT);
				} else if (slbSchedule.getChangeType() == OBDefine.CHANGE_TYPE_DELETE) {
					chunkAlteon.setChangeType(OBDefine.CHANGE_TYPE_DELETE);
				} else {
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format("Change Type another Number : %d", slbSchedule.getChangeType()));
				}
				if (slbSchedule.getChangeYN() == OBDefine.STATE_DISABLE) {
					alteon.setUseYNChange(OBDefine.STATE_DISABLE);
				} else if (slbSchedule.getChangeYN() == OBDefine.STATE_ENABLE) {
					alteon.setUseYNChange(OBDefine.STATE_ENABLE);
				}
				chunkAlteon.setVsConfig(alteon);
				chunkAlteon.setChangeObject(OBDefine.CHANGE_OBJECT_VIRTUALSERVER);
				slbSchedule.setChunkAlteon(chunkAlteon);
			}

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("slbSchedule Add: slbSchedule = %s", slbSchedule));
			new OBVServerDB().addSlbSchedule(slbUser, slbSchedule);
//            new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_ACCNTGROUP_SET_SUCCESS, groupInfo.getName());
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "slbSchedule END");
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("slbSchedule end."));
	}

	@Override
	public void setSlbSchedule(OBDtoAdcSchedule slbSchedule, OBDtoAdcVServerF5 configF5,
			OBDtoAdcVServerAlteon configAlteon) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. Set slbSchedule:%s", slbSchedule));
		try {
			if (slbSchedule == null) {
				return;
			}

			if (configF5 != null) {
				OBDtoAdcConfigChunkF5 chunkF5 = new OBDtoAdcConfigChunkF5();
				OBDtoAdcConfigVServerF5 f5 = new OBDtoAdcConfigVServerF5();
				f5.setVsNew(configF5);
				if (slbSchedule.getChangeType() == OBDefine.CHANGE_TYPE_ADD) {
					chunkF5.setChangeType(OBDefine.CHANGE_TYPE_ADD);
				} else if (slbSchedule.getChangeType() == OBDefine.CHANGE_TYPE_EDIT) {
					chunkF5.setChangeType(OBDefine.CHANGE_TYPE_EDIT);
				} else if (slbSchedule.getChangeType() == OBDefine.CHANGE_TYPE_DELETE) {
					chunkF5.setChangeType(OBDefine.CHANGE_TYPE_DELETE);
				} else {
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format("Change Type another Number : %d", slbSchedule.getChangeType()));
				}
				chunkF5.setVsConfig(f5);
				chunkF5.setChangeObject(OBDefine.CHANGE_OBJECT_VIRTUALSERVER);
				slbSchedule.setChunkF5(chunkF5);
			}
			if (configAlteon != null) {
				OBDtoAdcConfigChunkAlteon chunkAlteon = new OBDtoAdcConfigChunkAlteon();
				OBDtoAdcConfigVServerAlteon alteon = new OBDtoAdcConfigVServerAlteon();
				alteon.setVsNew(configAlteon);
				if (slbSchedule.getChangeType() == OBDefine.CHANGE_TYPE_ADD) {
					chunkAlteon.setChangeType(OBDefine.CHANGE_TYPE_ADD);
				} else if (slbSchedule.getChangeType() == OBDefine.CHANGE_TYPE_EDIT) {
					chunkAlteon.setChangeType(OBDefine.CHANGE_TYPE_EDIT);
				} else if (slbSchedule.getChangeType() == OBDefine.CHANGE_TYPE_DELETE) {
					chunkAlteon.setChangeType(OBDefine.CHANGE_TYPE_DELETE);
				} else {
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format("Change Type another Number : %d", slbSchedule.getChangeType()));
				}
				if (slbSchedule.getChangeYN() == OBDefine.STATE_DISABLE) {
					alteon.setUseYNChange(OBDefine.STATE_DISABLE);
				} else if (slbSchedule.getChangeYN() == OBDefine.STATE_ENABLE) {
					alteon.setUseYNChange(OBDefine.STATE_ENABLE);
				}
				chunkAlteon.setVsConfig(alteon);
				chunkAlteon.setChangeObject(OBDefine.CHANGE_OBJECT_VIRTUALSERVER);
				slbSchedule.setChunkAlteon(chunkAlteon);
			}

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
					String.format("SLB slbSchedule Set: slbSchedule = %s", slbSchedule));
			new OBVServerDB().setSlbSchedule(slbSchedule);
//            new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_ACCNTGROUP_SET_SUCCESS, groupInfo.getName());
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("SLB slbSchedule Set End"));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("Set slbSchedule End."));
	}

	@Override
	public void delSlbSchedule(ArrayList<Integer> adcScheduleIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. Del slbUser:%s", adcScheduleIndex));
		try {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("SLB User Del: slbUser = %s", adcScheduleIndex));
			new OBVServerDB().delSlbSchedule(adcScheduleIndex);
//            new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_ACCNTGROUP_SET_SUCCESS, groupInfo.getName());
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("SLB User Del End"));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("Del slbUser End."));
	}

	@Override
	public Integer getSlbScheduleListTotal(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException {
		return new OBVServerDB().getSlbScheduleListCount(adcIndex, accntIndex, searchKey);
	}

	@Override
	public ArrayList<OBDtoAdcSchedule> getSlbScheduleList(Integer adcIndex, String searchKey, Integer beginIndex,
			Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		ArrayList<OBDtoAdcSchedule> retVal = new OBVServerDB().getSlbScheduleList(adcIndex, searchKey, beginIndex,
				endIndex, orderType, orderDir);
		return retVal;
	}

	@Override
	public OBDtoSlbUser getSlbUser(Integer slbUserIndex) throws OBException {
		OBDtoSlbUser retVal = new OBVServerDB().getSlbUser(slbUserIndex);
		return retVal;
	}

	@Override
	public OBDtoSlbUser getLastRespUserInfo() throws OBException {
		OBDtoSlbUser retVal = new OBVServerDB().getLastRespUserInfo();
		return retVal;
	}

	@Override
	public void addMessageToSMS(String phone) throws OBException {
		// TODO Auto-generated method stub

	}

	@Override
	public OBDtoAdcSchedule getSlbSchedule(Integer scheduleIndex) throws OBException {
		OBDtoAdcSchedule retVal = new OBVServerDB().getSlbSchedule(scheduleIndex);
		return retVal;
	}

}