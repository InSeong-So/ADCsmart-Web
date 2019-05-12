package kr.openbase.adcsmart.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.OBAccount;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAccount;
import kr.openbase.adcsmart.service.dto.OBDtoAccountRole;
import kr.openbase.adcsmart.service.dto.OBDtoAccountRoleMap;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcRSInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVSInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAuditLogAccount;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoRSInfo;
import kr.openbase.adcsmart.service.dto.OBDtoVSInfo;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBIpTree;
import kr.openbase.adcsmart.service.utility.OBLicenseExpiredException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBAccountImpl implements OBAccount {

	// public static void main(String[] args)
	// {
	// try
	// {
	// ArrayList<OBDtoAccount> list = new OBAccountImpl().getAccountList(1);
	// System.out.println(list);
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public ArrayList<OBDtoAccount> getAccountList(Integer accountIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. accountIndex:%d", accountIndex));

		String sqlText = "";
		ResultSet rs;
		ArrayList<OBDtoAccount> list = new ArrayList<OBDtoAccount>();

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (accountIndex == null || accountIndex.equals(0)) {
				sqlText = String.format(" SELECT INDEX, ID, NAME, CMT, PSWD, TEL_NO, MBPH_NO, EMAIL, \n"
						+ "    LST_CONN_DTM, ROLE_NO, ALERT_WINDOW, ALERT_SOUND,           \n"
						+ "    START_ALIVE_TIME, END_ALIVE_TIME, IP_FILTER,                \n"
						+ "    HISTORY, CHANGED_TIME						               \n"
						+ "    FROM MNG_ACCNT                                              \n"
						+ "    WHERE AVAILABLE=1;                                          ");
			} else {
				sqlText = String.format(" SELECT INDEX, ID, NAME, CMT, PSWD, TEL_NO, MBPH_NO, EMAIL,  \n"
						+ "    LST_CONN_DTM, ROLE_NO, ALERT_WINDOW, ALERT_SOUND,            \n"
						+ "    START_ALIVE_TIME, END_ALIVE_TIME, IP_FILTER,                 \n"
						+ "    HISTORY, CHANGED_TIME						               	\n"
						+ " FROM MNG_ACCNT                                                  \n"
						+ " WHERE INDEX=%d AND AVAILABLE=1;                                 \n", accountIndex);
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
				account.setPeriodStartTime(db.getLong(rs, "START_ALIVE_TIME"));
				account.setPeriodEndTime(db.getLong(rs, "END_ALIVE_TIME"));
				account.setAdcVsList(getInvolvedAdcNameList(account.getAccountIndex()));
				account.setIpFilter(db.getString(rs, "IP_FILTER"));
				account.setHistroy(db.getString(rs, "HISTORY"));
				account.setChangedTime(db.getTimestamp(rs, "CHANGED_TIME"));
				list.add(account);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list.toString()));

		return list;
	}

	public int getAccountTotalCount() throws OBException {
		String sqlText = "";
		ResultSet rs;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String
					.format("SELECT COUNT(*) AS CNT \n" + "FROM MNG_ACCNT         \n" + "WHERE AVAILABLE=1;     \n");

			rs = db.executeQuery(sqlText);

			if (rs.next() == true)
				return db.getInteger(rs, "CNT");
			else
				return 0;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public Integer getAccountRoleNo(Integer accountIndex) throws OBException {
		String sqlText = "";
		ResultSet rs;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (accountIndex == null || accountIndex.equals(0)) {
				return OBDefine.ACCNT_ROLE_ADMIN;
			} else {
				sqlText = " SELECT ROLE_NO FROM MNG_ACCNT WHERE INDEX=" + accountIndex + "AND AVAILABLE=1; ";
			}

			rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get account role no."));
			}

			return db.getInteger(rs, "ROLE_NO");
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public Integer getAccountRoleNoNew(Integer accountIndex) throws OBException {
		String sqlText = "";
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			if (accountIndex == null || accountIndex.equals(0)) {
				return OBDefine.ACCNT_ROLE_ADMIN;
			} else {
				sqlText = String.format(" SELECT ROLE_NO                 \n" + " FROM MNG_ACCNT                 \n"
						+ " WHERE INDEX=%d AND AVAILABLE=1;", accountIndex);
			}

			rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get account role no."));
			}

			return db.getInteger(rs, "ROLE_NO");
		} catch (SQLException e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public void setAccount(OBDtoAccount accountInfo, OBDtoExtraInfo extraInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. accountInfo:%s", accountInfo));
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		// 계정이 있는지 확인
		OBDtoAccount localInfo = null;
		try {
			db.openDB();

			localInfo = getAccountInfo(accountInfo.getAccountId());
			if (localInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "invalid account id");
			}

			// 데이터를 저장한다.
			String setText = "";
			boolean isFirst = true;
			if ((accountInfo.getAccountName() != null) && (!accountInfo.getAccountName().isEmpty())) {
				if (isFirst == true)
					setText += String.format("NAME=%s", OBParser.sqlString(accountInfo.getAccountName()));
				else
					setText += String.format(", NAME=%s", OBParser.sqlString(accountInfo.getAccountName()));
				isFirst = false;
			}
			if (accountInfo.getAccountComment() != null) {
				if (isFirst == true)
					setText += String.format("CMT=%s", OBParser.sqlString(accountInfo.getAccountComment()));
				else
					setText += String.format(", CMT=%s", OBParser.sqlString(accountInfo.getAccountComment()));
				isFirst = false;
			}
			if (accountInfo.getTelephone() != null) {
				if (isFirst == true)
					setText += String.format("TEL_NO=%s", OBParser.sqlString(accountInfo.getTelephone()));
				else
					setText += String.format(", TEL_NO=%s", OBParser.sqlString(accountInfo.getTelephone()));
				isFirst = false;
			}
			if (accountInfo.getMobilePhone() != null) {
				if (isFirst == true)
					setText += String.format("MBPH_NO=%s", OBParser.sqlString(accountInfo.getMobilePhone()));
				else
					setText += String.format(", MBPH_NO=%s", OBParser.sqlString(accountInfo.getMobilePhone()));
				isFirst = false;
			}
			if (accountInfo.getEmail() != null) {
				if (isFirst == true)
					setText += String.format("EMAIL=%s", OBParser.sqlString(accountInfo.getEmail()));
				else
					setText += String.format(", EMAIL=%s", OBParser.sqlString(accountInfo.getEmail()));
				isFirst = false;
			}
			if (accountInfo.getRoleNo() != null) {
				if (isFirst == true)
					setText += String.format("ROLE_NO=%d", accountInfo.getRoleNo());
				else
					setText += String.format(", ROLE_NO=%d", accountInfo.getRoleNo());
				isFirst = false;
			}
			if (accountInfo.getAlertWindow() != null) {
				if (isFirst == true)
					setText += String.format("ALERT_WINDOW=%d", accountInfo.getAlertWindow());
				else
					setText += String.format(", ALERT_WINDOW=%d", accountInfo.getAlertWindow());
				isFirst = false;
			}
			if (accountInfo.getAlertSound() != null) {
				if (isFirst == true)
					setText += String.format("ALERT_SOUND=%d", accountInfo.getAlertSound());
				else
					setText += String.format(", ALERT_SOUND=%d", accountInfo.getAlertSound());
				isFirst = false;
			}
			{
				if (isFirst == true)
					setText += String.format("START_ALIVE_TIME=%d", accountInfo.getPeriodStartTime());
				else
					setText += String.format(", START_ALIVE_TIME=%d", accountInfo.getPeriodStartTime());
				isFirst = false;
			}
			{
				if (isFirst == true)
					setText += String.format("END_ALIVE_TIME=%d", accountInfo.getPeriodEndTime());
				else
					setText += String.format(", END_ALIVE_TIME=%d", accountInfo.getPeriodEndTime());
				isFirst = false;
			}
			if (accountInfo.getIpFilter() != null) {
				if (isFirst == true)
					setText += String.format("IP_FILTER=%s", OBParser.sqlString(accountInfo.getIpFilter()));
				else
					setText += String.format(", IP_FILTER=%s", OBParser.sqlString(accountInfo.getIpFilter()));
				isFirst = false;
			}
			if (setText.length() == 0)
				setText += String.format("HISTORY=%s", OBParser.sqlString(accountInfo.getHistroy()));
			else
				setText += String.format(", HISTORY=%s", OBParser.sqlString(accountInfo.getHistroy()));

			if (setText.length() == 0)
				setText += String.format("CHANGED_TIME=%s", OBParser.sqlString(accountInfo.getChangedTime()));
			else
				setText += String.format(", CHANGED_TIME=%s", OBParser.sqlString(accountInfo.getChangedTime()));

			sqlText = String.format(" UPDATE MNG_ACCNT SET %s WHERE INDEX=%d; ", setText,
					accountInfo.getAccountIndex());

			db.executeUpdate(sqlText);

			// account 역할이 SystemAdmin이면 account-adc 정보를 업데이트 한다 SystemAdmin이 아니면 장비 할당 부분을
			// 건드리지 않는다:clear하고 등록
			Integer roleNo = getAccountRoleNo(extraInfo.getAccountIndex());

			if (roleNo != null && roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
				clearInvolvedAdcList(accountInfo.getAccountIndex());
				addInvolvedAdcList(accountInfo.getAccountIndex(), accountInfo.getAdcVsList());

				if (accountInfo.getRoleNo().equals(OBDefine.ACCNT_ROLE_VSADMIN)
						|| accountInfo.getRoleNo().equals(OBDefine.ACCNT_ROLE_RSADMIN)) {// 설정이 vsAdmin인 경우...
					clearInvolvedAdcVSList(accountInfo.getAccountIndex());
					clearInvolvedAdcRSList(accountInfo.getAccountIndex());
					if (accountInfo.getAdcVsList() != null) {
						addInvolvedAdcVSList(accountInfo.getAccountIndex(), accountInfo.getAdcVsList());
					}
					if (accountInfo.getAdcRsList() != null) {
						addInvolvedAdcRSList(accountInfo.getAccountIndex(), accountInfo.getAdcRsList());
					}
				}
			}

			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_ACCOUNT_SET_SUCCESS, accountInfo.getAccountId());
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
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

	private void clearInvolvedAdcList(Integer accountIndex) throws OBException {
		// account의 adc-account 관계 지움
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. accountIndex:%d", accountIndex));

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" DELETE                  \n" + " FROM MNG_ACCNT_ADC_MAP  \n" + " WHERE ACCNT_INDEX = %d; \n",
					accountIndex);
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	private void clearInvolvedAdcVSList(Integer accountIndex) throws OBException {
		// account의 adc-account 관계 지움
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. accountIndex:%d", accountIndex));

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" DELETE FROM MNG_ACCNT_VS_MAP WHERE ACCNT_INDEX = %d; ", accountIndex);
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	private void addInvolvedAdcList(Integer accountIndex, List<OBDtoAdcVSInfo> adcList) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. accountIndex:%d, adcList:%s", accountIndex, adcList));
		if (adcList == null) {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. adcList is null"));
			return;
		}
		Integer roleNo = getAccountRoleNo(accountIndex);
		if (roleNo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("accountIndex \"%d\" role error.", accountIndex));
		} else if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. "));
			return;
		}
		// accountd의 adc-account 관계 등록
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			for (int i = 0; i < adcList.size(); i++) {
				int index = adcList.get(i).getAdcIndex();

				sqlText = "INSERT INTO MNG_ACCNT_ADC_MAP (ACCNT_INDEX, ADC_INDEX) VALUES (" + accountIndex + ", "
						+ index + ");";

				db.executeUpdate(sqlText);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	private void addInvolvedAdcVSList(Integer accountIndex, List<OBDtoAdcVSInfo> adcList) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. accountIndex:%d, adcList:%s", accountIndex, adcList));
		if (adcList == null) {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "end. adcList is null");
			return;
		}
		Integer roleNo = getAccountRoleNo(accountIndex);
		if (roleNo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("accountIndex \"%d\" role error.", accountIndex));
		} else if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "end. ");
			return;
		}
		// accountd의 adc-account 관계 등록
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String sqlValues = "";
			for (OBDtoAdcVSInfo adcVsInfo : adcList) {
				ArrayList<OBDtoVSInfo> vsList = adcVsInfo.getVsInfoList();
				if (vsList == null || vsList.size() == 0)
					continue;
				for (OBDtoVSInfo obj : vsList) {
					if (!sqlValues.isEmpty())
						sqlValues += ", ";

					// FIXME

					sqlValues += String.format(" (%d, %d, %s) ", accountIndex, adcVsInfo.getAdcIndex(),
							OBParser.sqlString(obj.getVsIndex()));
				}
			}

			if (sqlValues.isEmpty()) {
				OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
				return;
			}

			sqlText = String.format("INSERT INTO MNG_ACCNT_VS_MAP (ACCNT_INDEX, ADC_INDEX, VS_INDEX) VALUES %s ;",
					sqlValues);

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	// TODO
	private void addInvolvedAdcRSList(Integer accountIndex, List<OBDtoAdcRSInfo> adcList) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. accountIndex:%d, adcList:%s", accountIndex, adcList));
		if (adcList == null) {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. adcList is null"));
			return;
		}
		Integer roleNo = getAccountRoleNo(accountIndex);
		if (roleNo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("accountIndex \"%d\" role error.", accountIndex));
		} else if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. "));
			return;
		}
		// accountd의 adc-account 관계 등록
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			String sqlValues = "";
			for (OBDtoAdcRSInfo adcRsInfo : adcList) {
				ArrayList<OBDtoRSInfo> rsList = adcRsInfo.getRsInfoList();
				if (rsList == null || rsList.size() == 0)
					continue;
				for (OBDtoRSInfo obj : rsList) {
					if (!sqlValues.isEmpty())
						sqlValues += ", ";

					sqlValues += String.format(" (%d, %d, %s) ", accountIndex, adcRsInfo.getAdcIndex(),
							OBParser.sqlString(obj.getRsIndex()));
				}
			}

			if (sqlValues.isEmpty()) {
				OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
				return;
			}

			sqlText = String.format(
					"INSERT INTO " + " MNG_ACCNT_RS_MAP" + " (ACCNT_INDEX, ADC_INDEX, RS_INDEX) " + " VALUES %s ;",
					sqlValues);

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

	private void clearInvolvedAdcRSList(Integer accountIndex) throws OBException {
		// account의 adc-account 관계 지움
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. accountIndex:%d", accountIndex));

		String sqlText = "";

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			sqlText = String.format("DELETE " + " FROM MNG_ACCNT_RS_MAP " + " WHERE ACCNT_INDEX = %d;", accountIndex);

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	private ArrayList<OBDtoAdcVSInfo> getInvolvedAdcNameList(Integer accountIndex) throws OBException {
		ArrayList<OBDtoAdcVSInfo> adcList = new ArrayList<OBDtoAdcVSInfo>();
		OBDatabase db = new OBDatabase();
		Integer accountRoleNo;
		String sqlText = "";

		try {
			db.openDB();

			accountRoleNo = getAccountRoleNo(accountIndex);
			if (accountRoleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
				sqlText = " SELECT INDEX, NAME FROM MNG_ADC WHERE AVAILABLE = " + OBDefine.ADC_STATE.AVAILABLE
						+ " ORDER BY INDEX; ";
			} else {
				sqlText = String.format(
						" SELECT A.ADC_INDEX AS INDEX, B.NAME FROM MNG_ACCNT_ADC_MAP A INNER JOIN MNG_ADC B						"
								+ // join 점검:필수
								" ON B.INDEX=A.ADC_INDEX WHERE A.ACCNT_INDEX = %d ORDER BY A.ADC_INDEX;",
						accountIndex);
			}

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcVSInfo name = new OBDtoAdcVSInfo();
				name.setAdcIndex(db.getInteger(rs, "INDEX"));
				name.setAdcName(db.getString(rs, "NAME"));
				adcList.add(name);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return adcList;
	}

	public ArrayList<Integer> getInvolvedAdcList(Integer accountIndex) throws OBException {
		ArrayList<Integer> adcList = new ArrayList<Integer>();
		String sqlText = "";

		Integer roleNo = getAccountRoleNo(accountIndex);
		if (roleNo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("accountIndex \"%d\" role error.", accountIndex));
		}

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) // 유효한(삭제되지 않은) 모든 장비
			{
				sqlText = " SELECT INDEX AS ADC_INDEX FROM MNG_ADC WHERE AVAILABLE = " + OBDefine.ADC_STATE.AVAILABLE
						+ " ORDER BY INDEX;";
			} else
			// 계정에 할당된 장비
			{
				sqlText = " SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = " + accountIndex
						+ " ORDER BY ADC_INDEX;";
			}

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				adcList.add(db.getInteger(rs, "ADC_INDEX"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return adcList;
	}

	private boolean isExistAccount(String accountID) throws OBException {
		String sqlText = " SELECT ID FROM MNG_ACCNT WHERE ID=" + OBParser.sqlString(accountID) + " AND AVAILABLE=1; ";

		// 동일 계정이 있는지 조사.
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			ResultSet rs = db.executeQuery(sqlText);

			return rs.next();

			// if(rs.next() == false)
			// {
			// return false;
			// }
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	//
	// @Override
	// public ArrayList<OBDtoAccount> searchAccountList(String searchKey) throws
	// OBException
	// {
	// return searchAccountList(searchKey, OBDefine.ORDER_TYPE_FIRST,
	// OBDefine.ORDER_DIR_DESCEND);
	// }
	//
//    public static void main(String[] args)
//    {
//        try
//        {
//            OBDtoAccount accountInfo = new OBDtoAccount();
//            OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
//
//            accountInfo.setAccountId("bwpark");
//            accountInfo.setAccountName("박병욱");
//            accountInfo.setAccountPassword("admin01");
//            accountInfo.setRoleNo(1);
//
//            extraInfo.setClientIPAddress("172.172.100.1");
//            new OBAccountImpl().addAccount(accountInfo, extraInfo);
//            // System.out.println(list);
//        }
//        catch(OBException e)
//        {
//            e.printStackTrace();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

	@Override
	public void addAccount(OBDtoAccount accountInfo, OBDtoExtraInfo extraInfo)
			throws OBLicenseExpiredException, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. accountInfo:%s", accountInfo.toString()));
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();

			// 패스워드 암호화.
			String passWord = encryptPassword(accountInfo.getAccountPassword());
			accountInfo.setAccountPassword(passWord);

			// 계정이 이미 있는지 확인
			if (isExistAccount(accountInfo.getAccountId()) == true) {
				// db.closeDB(); //아래 exception 블록에서 처리
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);// , String.format("AccountID:%s",
																				// accountInfo.getAccountId()));
			}

			if (accountInfo.getAccountPassword().isEmpty()) {
				// db.closeDB(); //아래 exception 블록에서 처리
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);// , "passwd empty");
			}

			// 데이터를 저장한다.
			sqlText = String.format(" INSERT INTO MNG_ACCNT (ID, NAME, CMT, PSWD, TEL_NO, MBPH_NO, EMAIL, ROLE_NO, "
					+ "AVAILABLE, MSTK_CNT, ALERT_WINDOW, ALERT_SOUND, START_ALIVE_TIME, END_ALIVE_TIME, IP_FILTER, HISTORY, CHANGED_TIME) "
					+ "VALUES (%s, %s, %s, %s, %s, %s, %s, %d, %d, %d, %d, %d, %d, %d, %s, %s, %s); ",
					OBParser.sqlString(accountInfo.getAccountId()), OBParser.sqlString(accountInfo.getAccountName()),
					OBParser.sqlString(accountInfo.getAccountComment()),
					OBParser.sqlString(accountInfo.getAccountPassword()),
					OBParser.sqlString(accountInfo.getTelephone()), OBParser.sqlString(accountInfo.getMobilePhone()),
					OBParser.sqlString(accountInfo.getEmail()), accountInfo.getRoleNo(), OBDefine.STATUS_AVAILABLE, 0,
					accountInfo.getAlertWindow(), accountInfo.getAlertSound(), accountInfo.getPeriodStartTime(),
					accountInfo.getPeriodEndTime(), OBParser.sqlString(accountInfo.getIpFilter()),
					OBParser.sqlString(accountInfo.getHistroy()), OBParser.sqlString(accountInfo.getChangedTime()));

			db.executeUpdate(sqlText);

			// account의 account-adc 관계 정보 업데이트:clear하고 등록
			clearInvolvedAdcList(accountInfo.getAccountIndex());

			if (accountInfo.getAdcVsList() != null) {
				OBDtoAccount currentAccountInfo = getAccountInfo(accountInfo.getAccountId());
				addInvolvedAdcList(currentAccountInfo.getAccountIndex(), accountInfo.getAdcVsList());
			}
			if (accountInfo.getRoleNo().equals(OBDefine.ACCNT_ROLE_VSADMIN)
					|| accountInfo.getRoleNo().equals(OBDefine.ACCNT_ROLE_RSADMIN)) {// VSAdmin
																						// account의 account-adc 관계 정보
																						// 업데이트:clear하고 등록
				clearInvolvedAdcVSList(accountInfo.getAccountIndex());
				clearInvolvedAdcRSList(accountInfo.getAccountIndex());
				OBDtoAccount currentAccountInfo = getAccountInfo(accountInfo.getAccountId());
				if (accountInfo.getAdcVsList() != null) {
					addInvolvedAdcVSList(currentAccountInfo.getAccountIndex(), accountInfo.getAdcVsList());
				}
				if (accountInfo.getAdcRsList() != null) {
					addInvolvedAdcRSList(currentAccountInfo.getAccountIndex(), accountInfo.getAdcRsList());
				}

			}
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_ACCOUNT_ADD_SUCCESS, accountInfo.getAccountId());
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
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
	public void delAccount(ArrayList<Integer> accountIndexList, OBDtoExtraInfo extraInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcList:%s", accountIndexList));
		OBDatabase db = new OBDatabase();
		String sqlText = "";

		try {
			db.openDB();

			// 계정 삭제 + 계정의 account-adc mapping도 삭제
			for (Integer accountIndex : accountIndexList) {
				if (accountIndex == 1)
					continue;// admin01인 시스템 기본 계정임. 삭제하지 않음.
				if (accountIndex == extraInfo.getAccountIndex())
					continue;// 자신의 계정임.

				String id = getAccountID(accountIndex);
				sqlText = String.format(" UPDATE MNG_ACCNT  \n" + " SET               \n" + " AVAILABLE=0       \n"
						+ " WHERE INDEX = %d; \n", accountIndex);

				db.executeUpdate(sqlText);
				clearInvolvedAdcList(accountIndex);
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAuditImpl.AUDIT_ACCOUNT_DEL_SUCCESS, id);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// , e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	public String getAccountID(Integer accountIndex) throws OBException {
		ResultSet rs;
		String sqlText = "";

		if (accountIndex.equals(OBDefine.SYSTEM_USER_INDEX)) // DB에 넣는 것이 좋은 방법이지만 넣을 수 없어서, index 0의 ID는 "System"
		{
			return OBDefine.SYSTEM_USER_NAME;
		}
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT ID                       \n" + " FROM MNG_ACCNT                  \n"
					+ " WHERE INDEX=%d AND AVAILABLE=1; ", accountIndex);
			rs = db.executeQuery(sqlText);
			if (rs.next() == false)
				return null;
			return db.getString(rs, "ID");

		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public ArrayList<OBDtoAccountRole> getAccountRoleList(Integer roleIndex) throws OBException {
		ArrayList<OBDtoAccountRole> list = new ArrayList<OBDtoAccountRole>();

		OBDatabase db = new OBDatabase();
		String sqlText = "";
		try {
			db.openDB();

			if (roleIndex == null) {
				sqlText = String.format(" SELECT INDEX, NAME, COMMENT FROM %s ORDER BY INDEX; ",
						OBCommon.makeProperTableName("MNG_ACCNT_ROLE"));
			} else {
				sqlText = String.format(" SELECT INDEX, NAME, COMMENT FROM %s WHERE INDEX=%d ORDER BY INDEX; ",
						OBCommon.makeProperTableName("MNG_ACCNT_ROLE"), roleIndex);
			}

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAccountRole role = new OBDtoAccountRole();
				role.setIndex(db.getInteger(rs, "INDEX"));
				role.setName(db.getString(rs, "NAME"));
				role.setComment(db.getString(rs, "COMMENT"));
				list.add(role);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return list;
	}

	@Override
	public ArrayList<OBDtoAccountRoleMap> getAccountRoleMapList(String searchKey) throws OBException {
		ArrayList<OBDtoAccountRoleMap> list = new ArrayList<OBDtoAccountRoleMap>();

		OBDatabase db = new OBDatabase();

		String sqlText = "";

		// role목록 추출.
		try {
			db.openDB();

			if (searchKey == null) {
				sqlText = " SELECT INDEX, NAME, COMMENT FROM " + OBCommon.makeProperTableName("MNG_ACCNT_ROLE")
						+ " ORDER BY ID; ";
			} else {
				String wildcardKey = "%" + searchKey + "%";
				sqlText = " SELECT INDEX, NAME, COMMENT FROM " + OBCommon.makeProperTableName("MNG_ACCNT_ROLE")
						+ " WHERE NAME LIKE " + OBParser.sqlString(wildcardKey) + " ORDER BY ID; ";
			}

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAccountRoleMap roleMap = new OBDtoAccountRoleMap();
				OBDtoAccountRole role = new OBDtoAccountRole();
				role.setIndex(db.getInteger(rs, "INDEX"));
				role.setName(db.getString(rs, "NAME"));
				role.setComment(db.getString(rs, "COMMENT"));

				roleMap.setRoleInfo(role);

				// 계정 정보를 추출한다.
				ArrayList<OBDtoAccount> accountList = getAccountInfoList(role.getIndex());
				roleMap.setAccountList(accountList);

				list.add(roleMap);
			}
			rs.close();
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		return list;
	}

	private ArrayList<OBDtoAccount> getAccountInfoList(int roleIndex) throws OBException {
		ArrayList<OBDtoAccount> accountList = new ArrayList<OBDtoAccount>();

		String sqlText = " SELECT INDEX, ID, NAME, CMT, PSWD, TEL_NO, MBPH_NO, EMAIL, ROLE_NO, ALERT_WINDOW, ALERT_SOUND FROM MNG_ACCNT WHERE ROLE_NO="
				+ roleIndex + " AND AVAILABLE=1 ORDER BY ID; ";

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			ResultSet rs = db.executeQuery(sqlText);

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
				account.setRoleNo(db.getInteger(rs, "ROLE_NO"));
				account.setAlertWindow(db.getInteger(rs, "ALERT_WINDOW"));
				account.setAlertSound(db.getInteger(rs, "ALERT_SOUND"));

				account.setAdcVsList(getInvolvedAdcNameList(account.getAccountIndex()));
				accountList.add(account);
			}

			rs.close();
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (OBException e) {
			throw e;
		} finally {
			if (db != null)
				db.closeDB();
		}

		return accountList;
	}

	@Override
	public ArrayList<OBDtoAdcInfo> searchAdcInfoNSList(Integer accountIndex, String searchKey) throws OBException // not
																													// selected(available)
																													// adc
																													// list
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. accountIndex:%d, searchKey:%s", accountIndex, searchKey));

		ArrayList<OBDtoAdcInfo> list = new ArrayList<OBDtoAdcInfo>();
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();

			Integer roleNo = getAccountRoleNo(accountIndex);
			if (roleNo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("accountIndex \"%d\" role error.", accountIndex));
			} else if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
				db.closeDB();
				OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));
				return list;
			}

			if (searchKey == null) {
				sqlText = String.format("SELECT "
						+ " INDEX, NAME, IPADDRESS, ACCNT, PASSWORD, MODEL, TYPE, GROUP_INDEX, DESCRIPTION, "
						+ " ACTIVE_PAIR_INDEX, SW_VERSION, HOST_NAME, APPLY_TIME, SAVE_TIME, LAST_BOOT_TIME, STATUS, "
						+ " SNMP_RCOMM, SNMP_VERSION, SNMP_USER, SNMP_AUTH_PASSWORD, SNMP_PRIV_PASSWORD, "
						+ " SNMP_AUTH_PROTOCOL, SNMP_PRIV_PROTOCOL " + " FROM MNG_ADC " + " WHERE INDEX NOT IN"
						+ "    ( SELECT ADC_INDEX " + "      FROM MNG_ACCNT_ADC_MAP " + "      WHERE ACCNT_INDEX = %d"
						+ "    ) " + " AND AVAILABLE=1 " + " ORDER BY NAME;", accountIndex);
			} else {
				String wildcardKey = "%" + searchKey + "%";
				sqlText = String.format("SELECT "
						+ " INDEX, NAME, IPADDRESS, ACCNT, PASSWORD, MODEL, TYPE, GROUP_INDEX, DESCRIPTION, "
						+ " ACTIVE_PAIR_INDEX, SW_VERSION, HOST_NAME, APPLY_TIME, SAVE_TIME, LAST_BOOT_TIME, STATUS, "
						+ " SNMP_RCOMM, SNMP_VERSION, SNMP_USER, SNMP_AUTH_PASSWORD, SNMP_PRIV_PASSWORD, "
						+ " SNMP_AUTH_PROTOCOL, SNMP_PRIV_PROTOCOL " + " FROM MNG_ADC " + " WHERE INDEX NOT IN"
						+ "    ( SELECT ADC_INDEX " + "      FROM MNG_ACCNT_ADC_MAP " + "      WHERE ACCNT_INDEX = %d"
						+ "    ) " + " AND AVAILABLE=1 " + " AND NAME LIKE %s " + " ORDER BY NAME;", accountIndex,
						OBParser.sqlString(wildcardKey));
			}

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcInfo adcInfo = new OBDtoAdcInfo();
				adcInfo.setIndex(db.getInteger(rs, "INDEX"));
				adcInfo.setName(db.getString(rs, "NAME"));
				adcInfo.setAdcIpAddress(db.getString(rs, "IPADDRESS"));
				adcInfo.setAdcAccount(db.getString(rs, "ACCNT"));
				adcInfo.setAdcPassword(db.getString(rs, "PASSWORD"));
				adcInfo.setModel(db.getString(rs, "MODEL"));
				adcInfo.setAdcType(db.getInteger(rs, "TYPE"));
				adcInfo.setGroupIndex(db.getInteger(rs, "GROUP_INDEX"));
				adcInfo.setDescription(db.getString(rs, "DESCRIPTION"));
				adcInfo.setActivePairIndex(db.getInteger(rs, "ACTIVE_PAIR_INDEX"));
				adcInfo.setSwVersion(db.getString(rs, "SW_VERSION"));
				adcInfo.setHostName(db.getString(rs, "HOST_NAME"));
				adcInfo.setApplyTime(db.getTimestamp(rs, "APPLY_TIME"));
				adcInfo.setSaveTime(db.getTimestamp(rs, "SAVE_TIME"));
				adcInfo.setLastBootTime(db.getTimestamp(rs, "LAST_BOOT_TIME"));
				adcInfo.setStatus(db.getInteger(rs, "STATUS"));
				list.add(adcInfo);

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
				adcInfo.setSnmpInfo(snmpInfo);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));

		return list;
	}

	@Override
	public void incrementLoginFailCount(Integer accountIndex, OBDtoExtraInfo extraInfo) throws OBException {
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();

			sqlText = String.format(" SELECT  MSTK_CNT \n" + " FROM MNG_ACCNT   \n" + " WHERE INDEX=%d;  \n",
					accountIndex);

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to get data."));
			}

			int failCnt;

			failCnt = db.getInteger(rs, "MSTK_CNT") + 1;

			sqlText = String.format(
					" UPDATE MNG_ACCNT SET \n" + " MSTK_CNT=%d          \n" + " WHERE INDEX=%d;      \n", failCnt,
					accountIndex);

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public void crearLoginFailCount(Integer accountIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";

		// 데이터를 저장한다.
		try {
			db.openDB();

			sqlText = String.format(" UPDATE MNG_ACCNT SET MSTK_CNT=%d WHERE INDEX=%d; ", 0, accountIndex);

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public void setlastLoginTime(Integer accountIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		// 계정이 있는지 확인
		String sqlText = "";

		// 데이터를 저장한다.
		try {
			db.openDB();

			sqlText = String.format(" UPDATE MNG_ACCNT SET LST_CONN_DTM=%s WHERE INDEX=%d; ",
					OBParser.sqlString(OBDateTime.now()), accountIndex);

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public Timestamp getLastloginTime(Integer accountIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		// 계정이 있는지 확인
		String sqlText = "";
		Timestamp time;

		// 데이터를 저장한다.
		try {
			db.openDB();

			sqlText = String.format(" SELECT LST_CONN_DTM FROM MNG_ACCNT WHERE INDEX=%d AND AVAILABLE=1 ",
					accountIndex);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("invalid account id:%d", accountIndex));
			}
			time = db.getTimestamp(rs, "LST_CONN_DTM");
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return time;
	}

	@Override
	public Integer getLoginFailCount(Integer accountIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		ResultSet rs;
		Integer count = 0;
		try {
			db.openDB();

			sqlText = String.format(" SELECT INDEX, MSTK_CNT FROM MNG_ACCNT WHERE INDEX=%d AND AVAILABLE=1 ",
					accountIndex);

			rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				count = db.getInteger(rs, "MSTK_CNT");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return count;
	}

	@Override
	public OBDtoAccount getAccountInfo(String accountID) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. accountID:%s", accountID));

		String sqlText = "";
		ResultSet rs;
		OBDtoAccount account = new OBDtoAccount();

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT INDEX, ID, NAME, CMT, PSWD, TEL_NO, MBPH_NO, EMAIL, ROLE_NO, 						\n"
							+ " ALERT_WINDOW, ALERT_SOUND, START_ALIVE_TIME, END_ALIVE_TIME, IP_FILTER,         \n"
							+ " HISTORY, CHANGED_TIME 															\n"
							+ " FROM MNG_ACCNT                                                      			\n"
							+ " WHERE ID=%s AND AVAILABLE=1;                                        			\n",
					OBParser.sqlString(accountID));

			rs = db.executeQuery(sqlText);

			if (rs.next() == false)
				return null;
			account.setAccountIndex(db.getInteger(rs, "INDEX"));
			account.setAccountId(db.getString(rs, "ID"));
			account.setAccountName(db.getString(rs, "NAME"));
			account.setAccountComment(db.getString(rs, "CMT"));
			account.setAccountPassword(db.getString(rs, "PSWD"));
			account.setTelephone(db.getString(rs, "TEL_NO"));
			account.setMobilePhone(db.getString(rs, "MBPH_NO"));
			account.setEmail(db.getString(rs, "EMAIL"));
			account.setRoleNo(db.getInteger(rs, "ROLE_NO"));
			account.setAlertWindow(db.getInteger(rs, "ALERT_WINDOW"));
			account.setAlertSound(db.getInteger(rs, "ALERT_SOUND"));
			account.setPeriodStartTime(db.getLong(rs, "START_ALIVE_TIME"));
			account.setPeriodEndTime(db.getLong(rs, "END_ALIVE_TIME"));
			account.setIpFilter(db.getString(rs, "IP_FILTER"));
			account.setHistroy(db.getString(rs, "HISTORY"));
			account.setChangedTime(db.getTimestamp(rs, "CHANGED_TIME"));

			String defaultPasswd = encryptPassword(OBDefine.defaultPassword);

			if (account.getAccountPassword().compareToIgnoreCase(defaultPasswd) == 0)
				account.setInitailizedPasswd(true);
			account.setAdcVsList(getInvolvedAdcNameList(account.getAccountIndex()));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
		return account;
	}

	@Override
	public OBDtoAccount getAccountInfo(Integer accountIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoAccount obj;
		try {
			db.openDB();

			obj = getAccountInfo(accountIndex, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		return obj;
	}

	public OBDtoAccount getAccountInfo(Integer accountIndex, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. accountIndex:%d", accountIndex));

		if (accountIndex == null) {
			return null;
		}

		String sqlText = "";
		ResultSet rs;
		OBDtoAccount account = new OBDtoAccount();

		try {
			sqlText = String.format(
					" SELECT INDEX, ID, NAME, CMT, PSWD, TEL_NO, MBPH_NO, EMAIL, 	\n"
							+ " ROLE_NO, ALERT_WINDOW, ALERT_SOUND, IP_FILTER ,                 			\n"
							+ " HISTORY, CHANGED_TIME 														\n"
							+ " FROM MNG_ACCNT                                                 				\n"
							+ " WHERE INDEX=%d AND AVAILABLE=1;                                				\n",
					accountIndex);

			rs = db.executeQuery(sqlText);

			if (rs.next() == false)
				return null;
			account.setAccountIndex(db.getInteger(rs, "INDEX"));
			account.setAccountId(db.getString(rs, "ID"));
			account.setAccountName(db.getString(rs, "NAME"));
			account.setAccountComment(db.getString(rs, "CMT"));
			account.setAccountPassword(db.getString(rs, "PSWD"));
			account.setTelephone(db.getString(rs, "TEL_NO"));
			account.setMobilePhone(db.getString(rs, "MBPH_NO"));
			account.setEmail(db.getString(rs, "EMAIL"));
			account.setRoleNo(db.getInteger(rs, "ROLE_NO"));
			account.setAlertWindow(db.getInteger(rs, "ALERT_WINDOW"));
			account.setAlertSound(db.getInteger(rs, "ALERT_SOUND"));
			account.setIpFilter(db.getString(rs, "IP_FILTER"));
			account.setHistroy(db.getString(rs, "HISTORY"));
			account.setChangedTime(db.getTimestamp(rs, "CHANGED_TIME"));

			String defaultPasswd = encryptPassword(OBDefine.defaultPassword);

			if (account.getAccountPassword().compareToIgnoreCase(defaultPasswd) == 0)
				account.setInitailizedPasswd(true);
			account.setAdcVsList(getInvolvedAdcNameList(account.getAccountIndex()));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", account));
		return account;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// // OBDatabase db = new OBDatabase();
	// // db.openDB();
	// //Integer allocVServerID(Integer adcIndex, OBDatabase db)
	// ArrayList<OBDtoAuditLogAccount> list = new
	// OBAccountImpl().getAuditLogAccount(1, "", null, null, null);
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
	public ArrayList<OBDtoAuditLogAccount> getAuditLogAccount(Integer accountIndex, String searchKey, Date beginTime,
			Date endTime, Integer recordCount) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. accountIndex:%d", accountIndex));

		ArrayList<OBDtoAuditLogAccount> result = new ArrayList<OBDtoAuditLogAccount>();
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
			if (searchKey != null && !searchKey.isEmpty()) {
				String wildcardKey = "%" + searchKey + "%";
				sqlSearch = String.format("CLIENT_IP LIKE %s", OBParser.sqlString(wildcardKey));
			}

			String sqlTime = "";
			if (endTime == null)
				sqlTime = String.format(" OCCUR_TIME <= %s ", OBParser.sqlString(OBDateTime.now()));
			else
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. accountIndex:%s", accountIndex));
			sqlTime = String.format(" OCCUR_TIME <= %s ", OBParser.sqlString(endTime.toString()));

			if (beginTime != null)
				sqlTime += String.format(" AND OCCUR_TIME >= %s ", OBParser.sqlString(beginTime.toString()));

			Integer roleNo = getAccountRoleNo(accountIndex);
			if (roleNo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("accountIndex \"%d\" role error.", accountIndex));
			}

			if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
				sqlText = String.format(
						" SELECT A.LOG_SEQ, A.OCCUR_TIME, A.ACCOUNT_INDEX, A.CLIENT_IP, A.TYPE, A.LEVEL, A.CONTENT, B.ID, B.NAME  \n"
								+ " FROM LOG_SYSTEM_AUDIT A INNER JOIN MNG_ACCNT B                                                          \n"
								+ " ON A.ACCOUNT_INDEX = B.INDEX                                                                            \n"
								+ " WHERE                                                                                                   \n");
			} else {
				sqlText = String.format(
						" SELECT A.LOG_SEQ, A.OCCUR_TIME, A.ACCOUNT_INDEX, A.CLIENT_IP, A.TYPE, A.LEVEL, A.CONTENT, B.ID, B.NAME    \n"
								+ " FROM LOG_SYSTEM_AUDIT A INNER JOIN MNG_ACCNT B                                                            \n"
								+ " ON A.ACCOUNT_INDEX = B.INDEX                                                                              \n"
								+ " WHERE A.ACCOUNT_INDEX = %d                                                                                \n",
						accountIndex);
			}

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += sqlTime;

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			sqlText += " ORDER BY A.OCCUR_TIME DESC ";

			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAuditLogAccount log = new OBDtoAuditLogAccount();

				log.setAccountID(db.getString(rs, "ID"));
				log.setAccountIndex(db.getInteger(rs, "ACCOUNT_INDEX"));
				log.setAccountName(db.getString(rs, "NAME"));
				log.setClientIPAddress(db.getString(rs, "CLIENT_IP"));
				log.setContents(db.getString(rs, "CONTENT"));
				log.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				result.add(log);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
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

	@Override
	public String encryptPassword(String password) throws OBException {
		String hexMessageEncode = "";
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance("SHA-512");
			messageDigest.update(password.getBytes());
			byte[] messageDigestBytes = messageDigest.digest();
			for (int index = 0; index < messageDigestBytes.length; index++) {
				int countEncode = messageDigestBytes[index] & 0xff;
				if (Integer.toHexString(countEncode).length() == 1)
					hexMessageEncode = hexMessageEncode + "0";
				hexMessageEncode = hexMessageEncode + Integer.toHexString(countEncode);
			}
			return hexMessageEncode;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_DATAENCRYPT, e.getMessage());// MD2, MD5, SHA-1, SHA-256,
																							// SHA-384, SHA-512
		}
	}

	private String getCurrentPasswdHistory(Integer accountIndex) throws Exception {
		OBDatabase db = new OBDatabase();
		String sqlText = "";

		try {
			db.openDB();
			sqlText = String.format(" SELECT PSWD, HISTORY FROM MNG_ACCNT WHERE INDEX=%d AND AVAILABLE=1 ",
					accountIndex);
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("invalid account id:%d", accountIndex));
			}
			String sqlPreviousPassword = "";
			if (db.getString(rs, "HISTORY") == null) {
				sqlPreviousPassword = db.getString(rs, "PSWD");
			} else {
				sqlPreviousPassword = db.getString(rs, "HISTORY");
			}
			return sqlPreviousPassword;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private String makePasswdHistory(String currentPasswd, String oldPasswdListTxt, int maxHistory) throws Exception {
		String[] oldPasswds = oldPasswdListTxt.split(",");
		List<String> oldPasswordList = new ArrayList<String>(Arrays.asList(oldPasswds));
		if (oldPasswordList.size() < maxHistory) {
			oldPasswordList.add(currentPasswd);
		} else {
			oldPasswordList.remove(0);
			oldPasswordList.add(currentPasswd);
		}

		// FIX ME
		// subString 부분이 복잡하여 수정 필요함.
		String passwordHistory = oldPasswordList.toString().substring(1, oldPasswordList.toString().length() - 1)
				.replaceAll(" ", "");
		return passwordHistory;
	}

	private int getPasswdHistoryMax() {
		int maxPasswdHistory = 5;
		String proertiesValue = OBCommon.getProperties("password.history.max");
		if (proertiesValue != null) {
			try {
				int max = Integer.parseInt(proertiesValue);
				if (max > 10)
					maxPasswdHistory = 10;
				if (max <= 0)
					maxPasswdHistory = 1;
				return maxPasswdHistory;
			} catch (Exception e) {
				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, "Failed to get max passwd history. error: " + e.getMessage());
			}
		}
		return maxPasswdHistory;
	}

	@Override
	public void changePassword(Integer accountIndex, String currentPassword, OBDtoExtraInfo extraInfo)
			throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
//		String passWord = encryptPassword(currentPassword);
		try {
			db.openDB();
			int maxPasswdHistory = getPasswdHistoryMax();
			String sqlPreviousPasswordList = getCurrentPasswdHistory(accountIndex);

			String passwdHistory = makePasswdHistory(currentPassword, sqlPreviousPasswordList, maxPasswdHistory);

			sqlText = String.format(" UPDATE MNG_ACCNT SET PSWD=%s, HISTORY=%s, CHANGED_TIME=%s WHERE INDEX=%d ",
					OBParser.sqlString(currentPassword), OBParser.sqlString(passwdHistory),
					OBParser.sqlString(
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime())),
					accountIndex);

			db.executeUpdate(sqlText);
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_ACCOUNT_PASSWD_SET_SUCCESS, getAccountID(accountIndex));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
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
	public void resetPassword(Integer accntIndex, OBDtoExtraInfo extraInfo) throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		try {
			db.openDB();

			String passWord = encryptPassword(OBDefine.defaultPassword);

			sqlText = String.format("UPDATE MNG_ACCNT SET PSWD=%s WHERE INDEX=%d ", OBParser.sqlString(passWord),
					accntIndex);

			db.executeUpdate(sqlText);
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_ACCOUNT_PASSWD_RESET_SUCCESS, getAccountID(accntIndex));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
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
	public String getLoginClientIP(Integer accountIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		String retVal = "";
		String sqlText = "";
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT CLIENT_IP    \n" + " FROM MNG_ACCNT      \n" + " WHERE AVAILABLE=%d  \n"
							+ " AND INDEX=%d;",
					OBDefine.DATA_AVAILABLE, // 계정 정보가 유효함을 의미.
					accountIndex);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next()) {
				retVal = db.getString(rs, "CLIENT_IP");
			}
			return retVal;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public void setLoginClientIP(Integer accountIndex, String ipAddress) throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		try {
			db.openDB();

			sqlText = String.format(" UPDATE MNG_ACCNT SET CLIENT_IP=%s WHERE INDEX=%d ", OBParser.sqlString(ipAddress),
					accountIndex);

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
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
	public Date getLastAliveTime(Integer accountIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		Date retVal = null;
		String sqlText = "";
		try {
			db.openDB();

			sqlText = String.format(" SELECT LST_ALIVE_DTM FROM MNG_ACCNT WHERE AVAILABLE=%d AND INDEX=%d; ",
					OBDefine.DATA_AVAILABLE, // 계정 정보가 유효함을 의미.
					accountIndex);
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next()) {
				retVal = db.getTimestamp(rs, "LST_ALIVE_DTM");
			}
			return retVal;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public void setLastAliveTime(Integer accountIndex, Date currentTime) throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = String.format(" UPDATE MNG_ACCNT SET LST_ALIVE_DTM=%s WHERE INDEX=%d;",
				OBParser.sqlString(new Timestamp(currentTime.getTime())), accountIndex);
		try {
			db.openDB();
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public ArrayList<String> getAssignedVSList(Integer adcIndex, Integer accntIndex) throws OBException {
		ArrayList<String> retVal = new ArrayList<String>();

		String sqlText = " SELECT VS_INDEX FROM MNG_ACCNT_VS_MAP WHERE ACCNT_INDEX=" + accntIndex + " AND ADC_INDEX="
				+ adcIndex + ";";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				retVal.add(db.getString(rs, "VS_INDEX"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	public ArrayList<String> getAssignedVSServiceList(Integer adcIndex, Integer accntIndex) throws OBException {
		ArrayList<String> retVal = new ArrayList<String>();

		String sqlText = " SELECT VS_INDEX FROM MNG_ACCNT_VS_MAP WHERE ACCNT_INDEX=" + accntIndex + " AND ADC_INDEX="
				+ adcIndex + ";";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				retVal.add(db.getString(rs, "VS_INDEX"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	public ArrayList<String> getAssignedVSList(Integer accntIndex) throws OBException {
		ArrayList<String> retVal = new ArrayList<String>();

		String sqlText = " SELECT VS_INDEX FROM MNG_ACCNT_VS_MAP WHERE ACCNT_INDEX=" + accntIndex + " ; ";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				retVal.add(db.getString(rs, "VS_INDEX"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	public ArrayList<String> getNotAssignedGRVSList(Integer accntIndex, Integer groupIndex) throws OBException {
		ArrayList<String> retVal = new ArrayList<String>();
		String sqlText = "";
		if (accntIndex == OBDefine.ACCNT_ROLE_ADMIN) {
			sqlText = " SELECT VS_INDEX FROM MNG_VSSERVICE_GROUP_MAP WHERE GR_INDEX = " + groupIndex + "; ";
		} else {
			sqlText = " SELECT VS_INDEX FROM MNG_ACCNT_VS_MAP WHERE ACCNT_INDEX=" + accntIndex
					+ "AND VS_INDEX IN (SELECT VS_INDEX FROM MNG_VSSERVICE_GROUP_MAP WHERE GR_INDEX = " + groupIndex
					+ "); ";
		}

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				retVal.add(db.getString(rs, "VS_INDEX"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	public ArrayList<String> getAssignedRSList(Integer accntIndex) throws OBException {
		ArrayList<String> retVal = new ArrayList<String>();

		String sqlText = "";

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();
			sqlText = String.format(" SELECT " + " RS_INDEX " + " FROM MNG_ACCNT_RS_MAP " + " WHERE ACCNT_INDEX=%d ;",
					accntIndex);

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				retVal.add(db.getString(rs, "RS_INDEX"));
			}
		} catch (SQLException e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public ArrayList<OBDtoAdcVSInfo> getAdcVSInfoList(Integer accntIndex, String searchKey) throws OBException {
		return new OBVServerDB().getVSNameList(accntIndex, searchKey);
	}

	@Override
	public ArrayList<OBDtoAdcVSInfo> getAdcVSInfoNSList(Integer accntIndex, String searchKey) throws OBException {
		return new OBVServerDB().getVSNameNSList(accntIndex, searchKey);
	}

	// TODO
	@Override
	public ArrayList<OBDtoAdcRSInfo> getAdcRSInfoList(Integer accntIndex, String searchKey) throws OBException {
		return new OBVServerDB().getRSNameList(accntIndex, searchKey);
	}

	@Override
	public ArrayList<OBDtoAdcRSInfo> getAdcRSInfoNSList(Integer accntIndex, String searchKey) throws OBException {
		return new OBVServerDB().getRSNameNSList(accntIndex, searchKey);
	}

	private String searchAccountOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST ";
		switch (orderType) {
		case OBDefine.ORDER_TYPE_FIRST:// 아이디
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY ID ASC NULLS LAST, NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY ID DESC NULLS LAST, NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SECOND:// 이름.
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY NAME ASC NULLS LAST, ID ASC NULLS LAST ";
			else
				retVal = " ORDER BY NAME DESC NULLS LAST, ID ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_THIRD:// 최종로그인시간.
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY LST_CONN_DTM ASC NULLS LAST, ID ASC NULLS LAST ";
			else
				retVal = " ORDER BY LST_CONN_DTM DESC NULLS LAST, ID ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_FOURTH:// 역할.
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY ROLE_NO ASC NULLS LAST, ID ASC NULLS LAST ";
			else
				retVal = " ORDER BY ROLE_NO DESC NULLS LAST, ID ASC NULLS LAST ";
			break;
		// case OBDefine.ORDER_TYPE_FIFTH:// 관리ADC.
		// if(orderDir==OBDefine.ORDER_DIR_ASCEND)
		// retVal = " ORDER BY TYPE ASC NULLS LAST, ID ASC NULLS LAST ";
		// else
		// retVal = " ORDER BY TYPE DESC NULLS LAST, ID ASC NULLS LAST ";
		// break;
		case OBDefine.ORDER_TYPE_SIXTH:// 설명.
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY CMT ASC NULLS LAST, ID ASC NULLS LAST ";
			else
				retVal = " ORDER BY CMT DESC NULLS LAST, ID ASC NULLS LAST ";
			break;
		}
		return retVal;
	}

	@Override
	public ArrayList<OBDtoAccount> searchAccountList(String searchKey, Integer orderType, Integer orderDir)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. searchKey:%s", searchKey));

		OBDatabase db = new OBDatabase();
		String sqlText = "";
		ArrayList<OBDtoAccount> list = new ArrayList<OBDtoAccount>();

		try {
			db.openDB();

			if (searchKey == null) // 전체 목록
			{
				sqlText = String.format(" SELECT INDEX, ID, NAME, CMT, PSWD, TEL_NO, MBPH_NO, EMAIL, ROLE_NO, \n"
						+ "     ALERT_WINDOW, ALERT_SOUND, LST_CONN_DTM                         \n"
						+ " FROM MNG_ACCNT                                                      \n"
						+ " WHERE AVAILABLE=1                                                   \n");
			} else {
				// #3984-2 #15: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlText = String.format(
						" SELECT INDEX, ID, NAME, CMT, PSWD, TEL_NO, MBPH_NO, EMAIL, ROLE_NO, \n"
								+ "     ALERT_WINDOW, ALERT_SOUND, LST_CONN_DTM                         \n"
								+ " FROM MNG_ACCNT                                                      \n"
								+ " WHERE (ID LIKE %s OR NAME LIKE %s ) AND AVAILABLE=1                 \n",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			ResultSet rs;
			sqlText += searchAccountOrderType(orderType, orderDir);
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
				account.setRoleNo(db.getInteger(rs, "ROLE_NO"));
				account.setLastLogin(db.getTimestamp(rs, "LST_CONN_DTM"));
				account.setAlertWindow(db.getInteger(rs, "ALERT_WINDOW"));
				account.setAlertSound(db.getInteger(rs, "ALERT_SOUND"));
				account.setAdcVsList(getInvolvedAdcNameList(account.getAccountIndex()));

				list.add(account);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + sqlText);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));

		return list;
	}

	@Override
	public String getLogContent(String fileName) throws OBException {
		try {
			String result = "";
			String fullFileName = OBDefine.ADC_LOG_FILE_PATH + fileName + ".log";
			String fullFileNameBak = OBDefine.ADC_LOG_FILE_PATH + fileName + ".log.bak";
			String[] fileNameList = null;
			if (checkPath(fullFileName) == false)
				return OBMessages.getMessage(OBMessages.MSG_DEFINE_NO_LOGFILE);
			if (checkPath(fullFileNameBak) == false) {
				fileNameList = new String[] { fullFileName };
			} else {
				fileNameList = new String[] { fullFileNameBak, fullFileName };
			}
			String line = "";
			BufferedReader in = null;

			for (int i = 0; i < fileNameList.length; i++) {
				result += "=========================== logfile path : " + fileNameList[i]
						+ "===========================\n";
				in = new BufferedReader(new FileReader(fileNameList[i]));
				while ((line = in.readLine()) != null) {
					result += line + "\n";
				}
				in.close();
			}

			return result;
			/*
			 * BufferedReader in = new BufferedReader(new FileReader(fullFileNameBak));
			 * String line; while((line = in.readLine()) != null { result +=line +"\n"; }
			 * in.close();
			 * 
			 * BufferedReader inBak = new BufferedReader(new FileReader(fullFileNameBak));
			 * String lineBak;
			 * 
			 * result +=
			 * "==============================================bak log data=============================================="
			 * ; while((lineBak = inBak.readLine()) != null) { result +=lineBak +"\n"; }
			 * inBak.close();
			 * 
			 * return result;
			 */
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public boolean checkPath(String fullFileName) throws OBException, IOException {
		// String fullFilePath = "C:\\opt\\adcsmart\\logs\\alteon22.log";
		// File myDir = new File(fullFilePath);

		File dirFile = new File(OBDefine.ADC_LOG_FILE_PATH);
		File[] fileList = dirFile.listFiles();
		// [C:\opt\adcsmart\logs\alteon.log, C:\opt\adcsmart\logs\alteon_cmnd.log,
		// C:\opt\adcsmart\logs\syslog.log, C:\opt\adcsmart\logs\system.log]
		for (File filepath : fileList) {
			if (filepath.toString().indexOf(fullFileName) != -1) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Boolean isIpFilter(Integer accountIndex, String ipAddress) throws OBException {
		String dbIpAddress = getAccountIpFilter(accountIndex);

		if (ipAddress == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, String.format("IPADDRESS is null."));
		}

		OBIpTree ipTree = new OBIpTree();

		if (dbIpAddress != null) {
			ipTree.add(dbIpAddress);
			if (ipTree.containsIp(ipAddress)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public String getAccountIpFilter(Integer accountIndex) throws OBException {
		String sqlText = "";
		String ipAddress = "";
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			if (accountIndex == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get account Index."));
			} else {
				sqlText = String.format(" SELECT IP_FILTER                \n"
						+ " FROM MNG_ACCNT                      \n" + " WHERE INDEX=%d AND AVAILABLE=1;", accountIndex);
			}

			rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get account role no."));
			}

			ipAddress = db.getString(rs, "IP_FILTER");
			return ipAddress;
		} catch (SQLException e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			db.closeDB();
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
	// String fullFilePath = "C:\\opt\\adcsmart\\logs\\alteon22.log";
	// File dirFile=new File(OBDefine.ADC_LOG_FILE_PATH);
	// File []fileList=dirFile.listFiles();
	//
	// for(File tempFile : fileList)
	// {
	// if(tempFile.isFile())
	// {
	// String tempPath=tempFile.getParent();
	// String tempFileName=tempFile.getName();
	//
	// String filePath = tempPath+tempFileName;
	//
	// if (filePath.indexOf(fullFilePath) == -1)
	// {
	// System.out.println("없음.");
	// }
	//
	// }
	//
	// }
	// }
	// catch(Exception e)
	// {
	// }
	// }
}
