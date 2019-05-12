package kr.openbase.adcsmart.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.OBAlert;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcAlertLog;
import kr.openbase.adcsmart.service.dto.OBDtoAlert;
import kr.openbase.adcsmart.service.dto.OBDtoAlertConfig;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBDefineFault;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBAlertImpl implements OBAlert {
	@Override
	public OBDtoAlert getAlertTicker(Integer accountIndex) throws OBException {
		OBDtoOrdering ordering = new OBDtoOrdering();
		ordering.setOrderDirection(OBDtoOrdering.DIR_DESCEND);
		ordering.setOrderType(OBDtoOrdering.TYPE_1FIRST); // occrur time desc

		OBDtoAlert result = getAlertCore(accountIndex, null, 1, ordering); // ticker 업데이트는 시간 이정표를 갱신하지 않는다.

		return result;
	}

	public OBDtoAlert getAlertCore(Integer accountIndex, Integer type, int alertCount, OBDtoOrdering ordering)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "start. accountIndex = " + accountIndex + " type = " + type);
		OBDtoAlert alert = new OBDtoAlert();
		ArrayList<OBDtoAdcAlertLog> alertList = new ArrayList<OBDtoAdcAlertLog>();
		alert.setAlertList(alertList);
		alert.setType(type);

		OBDatabase db = new OBDatabase();

		ArrayList<OBDtoAdcAlertLog> result = new ArrayList<OBDtoAdcAlertLog>();
		// 사용자의 유효 adcIndex를 구한다.
		String userAdcIndexString;

		String data = " OCCUR_TIME, ADC_INDEX, ADC_NAME, CLASS, TYPE, STATUS, EVENT, ACTION, OBJECT_TYPE, OBJECT, RELATIVE_OBJECT_TYPE, RELATIVE_OBJECT ";
		String count = " COUNT(*) ";
		String sqlTextData = "";
		String sqlTextCount = "";

		try {
			db.openDB();

			userAdcIndexString = new OBAdcManagementImpl().getUsersAdcIndexString(OBDtoADCObject.CATEGORY_ALL, null,
					accountIndex);

			if (userAdcIndexString.isEmpty() == true) { // 없으면 그만한다. 빈 list return
				alert.setAlertCount(0); // 0개다
				return alert;
			}

			sqlTextData = String.format(
					" SELECT %s FROM LOG_ADC_ALARM   \n"
							+ " WHERE ADC_INDEX IN ( %s ) AND OCCUR_TIME >= (SELECT CHECK_TIME FROM MNG_ALARM_TIME WHERE ACCOUNT_INDEX = %d) \n", // 마지막
																																					// 확인
																																					// 시간
																																					// 이후
					data, userAdcIndexString, accountIndex);

			sqlTextCount = String.format(
					" SELECT %s FROM LOG_ADC_ALARM   \n"
							+ " WHERE ADC_INDEX IN ( %s ) AND OCCUR_TIME >= (SELECT CHECK_TIME FROM MNG_ALARM_TIME WHERE ACCOUNT_INDEX = %d) \n", // 마지막
																																					// 확인
																																					// 시간
																																					// 이후
					count, userAdcIndexString, accountIndex);

			// type : 분류 선택 - 전체(null), 장애(0), 경고(1)
			if (type != null) // && type.equals(OBAlarmImpl.TYPE_FAULT)==false &&
								// type.equals(OBAlarmImpl.TYPE_WARN)==false)
			{
				sqlTextData += " AND TYPE= " + type;
				sqlTextCount += " AND TYPE= " + type;
			}

			sqlTextData += getAlertOrderby(ordering);

			if (alertCount > 0) {
				sqlTextData += (" LIMIT " + alertCount);
			}
			// data 구하기
			ResultSet rs = db.executeQuery(sqlTextData);
			while (rs.next()) {
				int classType = db.getInteger(rs, "CLASS");
				OBDtoAdcAlertLog log = new OBDtoAdcAlertLog();
				log.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				log.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				log.setAdcName(db.getString(rs, "ADC_NAME"));
				log.setType(db.getInteger(rs, "TYPE"));
				log.setTitle(new OBDefineFault().getTitle(classType));
				log.setStatus(db.getInteger(rs, "STATUS"));
				log.setEvent(db.getString(rs, "EVENT"));
				log.setActionSyslog(new OBAlarmImpl().int2Actions(db.getInteger(rs, "ACTION")).getSyslog());
				log.setObjectType(db.getInteger(rs, "OBJECT_TYPE"));
				log.setObject(db.getString(rs, "OBJECT"));
				log.setRelativeObjectType(db.getInteger(rs, "RELATIVE_OBJECT_TYPE"));
				log.setRelativeObject(db.getString(rs, "RELATIVE_OBJECT"));
				log.setNew(true);
				result.add(log);
			}
			alert.setAlertList(result);
			// count 구하기
			rs = db.executeQuery(sqlTextCount);
			if (rs.next()) {
				alert.setAlertCount(db.getInteger(rs, "COUNT"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlTextData:%s", e.getMessage(), sqlTextData));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return alert;
	}

	@Override
	public OBDtoAlert getAlert(Integer accountIndex, Integer type, int alertCount, OBDtoOrdering ordering)
			throws OBException {
		return getAlertCore(accountIndex, type, alertCount, ordering);
	}

//	private void testGetAlert() {
//		OBDtoOrdering ordering = new OBDtoOrdering();
//		ordering.setOrderType(33);
//		ordering.setOrderDirection(OBDtoOrdering.DIR_DESCEND);
//		try {
//			OBDtoAlert result = getAlert(1, 2, 100, ordering);
//			System.out.println("testGetAlert() = " + result);
//		} catch (OBException e) {
//			e.printStackTrace();
//		}
//	}

	private String getAlertOrderby(OBDtoOrdering ordering) {
		// DEFAULT = OCCUR_TIME
		// 1st = OCCUR_TIME
		// 2nd = ADC_NAME
		// 3rd = TYPE
		String orderSql = "";

		switch (ordering.getOrderType()) {
		case OBDtoOrdering.TYPE_1FIRST: // 1st = OCCUR_TIME
			if (ordering.getOrderDirection() == OBDtoOrdering.DIR_DESCEND) {
				orderSql = " ORDER BY OCCUR_TIME DESC, ADC_NAME ";
			} else {
				orderSql = " ORDER BY OCCUR_TIME, ADC_NAME ";
			}
			break;
		case OBDtoOrdering.TYPE_2SECOND: // 2nd = ADC_NAME
			if (ordering.getOrderDirection() == OBDtoOrdering.DIR_DESCEND) {
				orderSql = " ORDER BY ADC_NAME DESC, OCCUR_TIME ";
			} else {
				orderSql = " ORDER BY ADC_NAME, OCCUR_TIME ";
			}
			break;
		case OBDtoOrdering.TYPE_3THIRD: // 3rd = TYPE
			if (ordering.getOrderDirection() == OBDtoOrdering.DIR_DESCEND) {
				orderSql = " ORDER BY TYPE DESC, OCCUR_TIME, ADC_NAME ";
			} else {
				orderSql = " ORDER BY TYPE, OCCUR_TIME, ADC_NAME ";
			}
			break;
		default: // DEFAULT = OCCUR_TIME
			if (ordering.getOrderDirection() == OBDtoOrdering.DIR_DESCEND) {
				orderSql = " ORDER BY OCCUR_TIME DESC, ADC_NAME ";
			} else {
				orderSql = " ORDER BY OCCUR_TIME, ADC_NAME ";
			}
			break;
		}
		return orderSql;
	}

	/*
	 * public void testUpdateUserAlertTime() throws OBException { Integer
	 * accountIndex = 2; updateUserAlertTime(accountIndex); }
	 */
	public void updateUserAlertTimebyName(String accountName) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "accountName = " + accountName);
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();

			/* String currentCheckTimeStr = OBDateTime.now(); */
			sqlText = String.format(
					" INSERT INTO MNG_ALARM_TIME ( ACCOUNT_INDEX, CHECK_TIME )  \n"
							+ " SELECT INDEX, CURRENT_TIMESTAMP FROM MNG_ACCNT WHERE ID = %s ",
					OBParser.sqlString(accountName));
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void updateUserAlertTime(Integer accountIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "accountIndex = " + accountIndex);
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();

			String currentCheckTimeStr = OBDateTime.now();
			sqlText = String.format(" UPDATE MNG_ALARM_TIME SET CHECK_TIME = %s WHERE ACCOUNT_INDEX = %d ",
					OBParser.sqlString(currentCheckTimeStr), accountIndex);
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public Timestamp getUserAlertCheckTime(Integer accountIndex, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "accountIndex = " + accountIndex);

		String sqlText = String.format(" SELECT CHECK_TIME FROM MNG_ALARM_TIME WHERE ACCOUNT_INDEX = %d ",
				accountIndex);
		try {
			ResultSet rs = db.executeQuery(sqlText);
			Timestamp currentCheckTime = null;
			if (rs.next()) {
				currentCheckTime = db.getTimestamp(rs, "CHECK_TIME");
			}
			return currentCheckTime;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		}
	}
//	public static void main(String[] args) 
//	{
//		OBAlertImpl me = new OBAlertImpl();
//		try
//		{
//		//	me.testUpdateUserAlertTime();
//			me.testGetAlert();
//		//	me.testGetAlertTicker();
//		}
//		catch(Exception e)
//		{
//			System.out.println("error = " + e.getMessage());
//			e.printStackTrace();
//		}
//	}

	@Override
	public OBDtoAlertConfig getAlertConfig(Integer accountIndex) throws OBException {
		String sqlText = "";
		OBDtoAlertConfig alertConfig = new OBDtoAlertConfig();

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			sqlText = String.format(
					" SELECT ALERT_WINDOW AS WINDOW, ALERT_SOUND AS SOUND \n"
							+ " FROM MNG_ACCNT WHERE INDEX = %d AND AVAILABLE = %d  ",
					accountIndex, OBDefine.DATA_AVAILABLE);

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				alertConfig.setAlertType(db.getInteger(rs, "WINDOW"));
				alertConfig.setAlertSound(db.getInteger(rs, "SOUND"));
			}
			return alertConfig;
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
}
