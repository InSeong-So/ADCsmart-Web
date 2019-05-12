package kr.openbase.adcsmart.service.impl.fault;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.snmp4j.smi.VariableBinding;

import kr.openbase.adcsmart.service.OBFaultMng;
import kr.openbase.adcsmart.service.OBSystemAudit;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVService;
import kr.openbase.adcsmart.service.dto.OBDtoElement;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoSlbNodeInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSlbPoolInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSlbVServerInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvNetwork;
import kr.openbase.adcsmart.service.dto.OBDtoVirtualServiceInfo;
import kr.openbase.adcsmart.service.dto.OBDtoVlanInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckLog;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResult;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResultContent;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResultElement;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckSchedule;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckStatus;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckTemplate;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultSolutionInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoObjectIndexInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoScheduleDateTime;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.impl.OBSystemAuditImpl;
import kr.openbase.adcsmart.service.impl.alteon.OBAdcVServerAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBAdcAlteonHandler;
import kr.openbase.adcsmart.service.impl.dto.OBDtoCpuMemStatus;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonL2Ports;
import kr.openbase.adcsmart.service.impl.f5.OBAdcVServerF5;
import kr.openbase.adcsmart.service.impl.f5.handler.OBAdcF5Handler;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoFaultCheckItemCountInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoFaultCheckPacketLossInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoFaultCheckResponseTimeInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoFaultCheckThreadInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoFaultMaxPerfInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoPktdumpInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoPktdumpOption;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoPktdumpStatusInfo;
import kr.openbase.adcsmart.service.impl.pas.OBAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.OBAdcPASHandler;
import kr.openbase.adcsmart.service.impl.pask.OBAdcVServerPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.OBAdcPASKHandler;
import kr.openbase.adcsmart.service.snmp.OBSnmp;
import kr.openbase.adcsmart.service.snmp.OBSnmpOidDB;
import kr.openbase.adcsmart.service.snmp.alteon.OBSnmpAlteon;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidFaultCheckHW;
import kr.openbase.adcsmart.service.snmp.f5.OBSnmpF5;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBFaultCheckMsg;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBFaultMngImpl implements OBFaultMng {
	// public static void main(String[] args)
	// {
	// ResourceBundle rb =
	// ResourceBundle.getBundle("/home/x15//web/resources/conf/MyResources", new
	// Locale("en"));
	// ResourceBundle korb =
	// ResourceBundle.getBundle("/home/x15//web/resources/conf/MyResources", new
	// Locale("ko", "kr"));
	//
	// int num = 10;
	//
	// System.out.println("영문");
	// printArgument(rb, num);
	// System.out.println("한글");
	// printArgument(korb, num);
	// }

	// private static void printArgument(ResourceBundle messages, int num) {
	// String format1 = messages.getString("printArgument");
	// String output = MessageFormat.format(format1, new Object[] { new Integer(num)
	// });
	// try {
	//
	// //이거 안하면 한글 깨짐
	// System.out.println(new String(output.getBytes("ISO-8859-1"), "UTF-8"));
	//
	// } catch (UnsupportedEncodingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	// private Long templateIndex=0L;
	// private int hwCheckTotal=0;
	// private int svcCheckTotal=0;
	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	//
	// OBDtoFaultCheckSchedule retVal = new
	// OBFaultMngImpl().getFaultCheckScheduleInfo(1L);
	// System.out.println(retVal);
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public OBDtoFaultCheckSchedule getFaultCheckScheduleInfo(Long index) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultCheckSchedule retVal = new OBDtoFaultCheckSchedule();
		try {
			db.openDB();
			retVal = getFaultCheckScheduleInfo(index, db);
		} catch (OBException e) {
			throw e;
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private OBDtoADCObject getADCObjectInfo(Integer category, Integer index) throws OBException {
		OBDtoADCObject retVal = new OBDtoADCObject();
		retVal.setCategory(category);
		retVal.setIndex(0);

		if (category == OBDtoADCObject.CATEGORY_ALL) {// 전체인 경우.
			retVal.setIndex(0);
			return retVal;
		}

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			retVal = getADCObjectInfo(category, index, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private OBDtoADCObject getADCObjectInfo(Integer category, Integer index, OBDatabase db) throws OBException {
		OBDtoADCObject retVal = new OBDtoADCObject();
		retVal.setCategory(category);
		retVal.setIndex(0);

		if (category == OBDtoADCObject.CATEGORY_ALL) {// 전체인 경우.
			retVal.setIndex(0);
			return retVal;
		}

		String sqlText = "";
		try {
			if (category == OBDtoADCObject.CATEGORY_GROUP) {
				sqlText = String.format(
						" SELECT INDEX, NAME, DESCRIPTION, AVAILABLE FROM MNG_ADC_GROUP   \n"
								+ " WHERE INDEX=%d AND AVAILABLE=%d LIMIT 1                         \n",
						index, OBDefine.DATA_AVAILABLE);
			} else if (category == OBDtoADCObject.CATEGORY_ADC) {
				sqlText = String.format(
						" SELECT INDEX, NAME, DESCRIPTION, AVAILABLE FROM MNG_ADC       \n"
								+ " WHERE INDEX=%d AND AVAILABLE=%d LIMIT 1                       \n",
						index, OBDefine.DATA_AVAILABLE);
			} else {
				throw new OBException(OBException.ERRCODE_SYSTEM_DATA_NOT_EXIST);
			}

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal.setIndex(db.getInteger(rs, "INDEX"));
				retVal.setName(db.getString(rs, "NAME"));
				retVal.setDesciption(db.getString(rs, "DESCRIPTION"));
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	private OBDtoFaultCheckSchedule getFaultCheckScheduleInfo(Long index, OBDatabase db) throws OBException {
		OBDtoFaultCheckSchedule retVal = new OBDtoFaultCheckSchedule();

		String sqlText = "";

		try {
			sqlText = String.format(
					" SELECT INDEX, NAME, DESCRIPTION, STATE, OCCUR_TIME, TARGET_TYPE, TARGET_INDEX, ACCNT_INDEX, CHECK_INTERVAL,\n"
							+ " TEMPLATE_INDEX, SCHEDULE_TYPE, EVERY_MINUTE, EVERY_HOUR, EVERY_DAYOFMONTH, EVERY_MONTH, EVERY_DAYOFWEEK    \n"
							+ " FROM MNG_FAULT_SCHEDULE                                                                                    \n"
							+ " WHERE INDEX = %d AND STATE = %d                                                                            \n"
							+ " LIMIT 1                                                                                                    \n",
					index, OBDefine.STATE_ENABLE);

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() != true) {
				throw new OBException(OBException.ERRCODE_SYSTEM_DATA_NOT_EXIST);
			}
			OBDtoADCObject targetObj = getADCObjectInfo(db.getInteger(rs, "TARGET_TYPE"),
					db.getInteger(rs, "TARGET_INDEX"));
			if (targetObj == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_DATA_NOT_EXIST);
			}
			retVal.setIndex(db.getLong(rs, "INDEX"));
			retVal.setName(db.getString(rs, "NAME"));
			retVal.setDescription(db.getString(rs, "DESCRIPTION"));
			retVal.setTargetObj(targetObj);
			retVal.setAccntIndex(db.getInteger(rs, "ACCNT_INDEX"));
			retVal.setCheckInterval(db.getInteger(rs, "CHECK_INTERVAL"));
			retVal.setTemplateIndex(db.getLong(rs, "TEMPLATE_INDEX"));
			retVal.setScheduleType(db.getInteger(rs, "SCHEDULE_TYPE"));
			retVal.setEveryMinute(db.getInteger(rs, "EVERY_MINUTE"));
			retVal.setEveryHour(db.getInteger(rs, "EVERY_HOUR"));
			retVal.setEveryDayOfMonth(db.getInteger(rs, "EVERY_DAYOFMONTH"));
			retVal.setEveryMonth(db.getInteger(rs, "EVERY_MONTH"));
			retVal.setEveryDayOfWeek(db.getInteger(rs, "EVERY_DAYOFWEEK"));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoADCObject object = new OBDtoADCObject();
	// object.setCategory(OBDtoADCObject.CATEGORY_ALL);
	// object.setDesciption("전체 대상 스케줄.");
	// OBDtoSearch searchObj = new OBDtoSearch();
	// OBDtoOrdering orderObj = new OBDtoOrdering();
	// ArrayList<OBDtoFaultCheckSchedule> retVal = new
	// OBFaultMngImpl().getFaultCheckScheduleList(object, searchObj, orderObj);
	// for(OBDtoFaultCheckSchedule info:retVal)
	// System.out.println(info);
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public ArrayList<OBDtoFaultCheckSchedule> getFaultCheckScheduleList(OBDtoADCObject object, OBDtoSearch searchObj,
			OBDtoOrdering orderObj) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDatabase db2 = new OBDatabase();
		ArrayList<OBDtoFaultCheckSchedule> retVal = new ArrayList<OBDtoFaultCheckSchedule>();
		try {
			db.openDB();
			db2.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				retVal = getFaultCheckScheduleListAll(searchObj, orderObj, db, db2);
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				retVal = getFaultCheckScheduleListAllTagert(searchObj, orderObj, db, db2);
				retVal.addAll(getFaultCheckScheduleList(object, searchObj, orderObj, db, db2));
				retVal.addAll(getFaultCheckScheduleListInvolvedAdc(object.getIndex(), searchObj, orderObj));// 그룹에 포함된
																											// adc 목록
																											// 추출.
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_ADC) {
				retVal = getFaultCheckScheduleListAllTagert(searchObj, orderObj, db, db2);
				retVal.addAll(getFaultCheckScheduleListInvolvedGroup(object.getIndex(), searchObj, orderObj, db, db2));// 포함된
																														// 그룹
																														// 목록
																														// 추출.
				retVal.addAll(getFaultCheckScheduleListAdcTarget(object.getIndex(), searchObj, orderObj, db, db2));
			}
		} catch (OBException e) {
			throw e;
		} finally {
			if (db != null)
				db.closeDB();
			if (db2 != null)
				db2.closeDB();
		}
		return retVal;
	}

	private ArrayList<OBDtoFaultCheckSchedule> getFaultCheckScheduleList(OBDtoADCObject object, OBDtoSearch searchObj,
			OBDtoOrdering orderObj, OBDatabase db, OBDatabase db2) throws OBException {
		ArrayList<OBDtoFaultCheckSchedule> retVal = new ArrayList<OBDtoFaultCheckSchedule>();

		String sqlText = "";

		try {
			// offset, limit 조건 생성.
			int offset = 0;
			if (searchObj != null && searchObj.getBeginIndex() != null)
				offset = searchObj.getBeginIndex().intValue();
			int limit = 0;
			if (searchObj != null && searchObj.getEndIndex() != null)
				limit = Math.abs(searchObj.getEndIndex().intValue() - offset) + 1;
			String sqlLimit = "";
			if (limit != 0)
				sqlLimit = String.format(" LIMIT %d ", limit);
			String sqlOffset = String.format(" OFFSET %d ", offset);
			/*
			 * // search 조건 생성. String sqlSearch=null; if(searchObj != null &&
			 * searchObj.getSearchKey() !=null && !searchObj.getSearchKey().isEmpty()) {
			 * String wildcardKey="%"+searchObj.getSearchKey()+"%";
			 * sqlSearch=String.format(" ( NAME LIKE %s OR DESCRIPTION LIKE %s ) ",
			 * OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey),
			 * OBParser.sqlString(wildcardKey)); } String sqlTime=""; if(searchObj != null
			 * && searchObj.getToTime()==null) sqlTime=String.format(" OCCUR_TIME <= %s ",
			 * OBParser.sqlString(OBDateTime.now())); else
			 * sqlTime=String.format(" OCCUR_TIME <= %s ",
			 * OBParser.sqlString(OBDateTime.toString(new
			 * Timestamp(searchObj.getToTime().getTime()))));
			 * 
			 * if(searchObj != null && searchObj.getFromTime()!=null)
			 * sqlTime+=String.format(" AND OCCUR_TIME >= %s ",
			 * OBParser.sqlString(OBDateTime.toString(new
			 * Timestamp(searchObj.getFromTime().getTime()))));
			 */

			sqlText = String.format(
					" SELECT INDEX, NAME, DESCRIPTION, STATE, OCCUR_TIME, TARGET_TYPE, TARGET_INDEX, ACCNT_INDEX, CHECK_INTERVAL,\n"
							+ " TEMPLATE_INDEX, SCHEDULE_TYPE, EVERY_MINUTE, EVERY_HOUR, EVERY_DAYOFMONTH, EVERY_MONTH, 					 \n"
							+ " EVERY_DAYOFWEEK, ITEM_TYPE																			     \n"
							+ " FROM MNG_FAULT_SCHEDULE                                                                                    \n"
							+ " WHERE TARGET_TYPE = %d AND TARGET_INDEX = %d AND STATE=%d                                                  \n",
					object.getCategory(), object.getIndex(), OBDefine.STATE_ENABLE);
			/*
			 * if(sqlTime!=null && !sqlTime.isEmpty()) sqlText += " AND" + sqlTime;
			 * 
			 * if(sqlSearch!=null && !sqlSearch.isEmpty()) sqlText += " AND " + sqlSearch;
			 */
			sqlText += " ORDER BY OCCUR_TIME DESC ";

			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			sqlText += sqlOffset;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoADCObject targetObj = getADCObjectInfo(db.getInteger(rs, "TARGET_TYPE"),
						db.getInteger(rs, "TARGET_INDEX"), db2);
				if (targetObj == null)
					continue;
				OBDtoFaultCheckSchedule obj = new OBDtoFaultCheckSchedule();
				obj.setIndex(db.getLong(rs, "INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setDescription(db.getString(rs, "DESCRIPTION"));
				obj.setTargetObj(targetObj);
				obj.setAccntIndex(db.getInteger(rs, "ACCNT_INDEX"));
				obj.setCheckInterval(db.getInteger(rs, "CHECK_INTERVAL"));
				obj.setTemplateIndex(db.getLong(rs, "TEMPLATE_INDEX"));
				obj.setScheduleType(db.getInteger(rs, "SCHEDULE_TYPE"));
				obj.setEveryMinute(db.getInteger(rs, "EVERY_MINUTE"));
				obj.setEveryHour(db.getInteger(rs, "EVERY_HOUR"));
				obj.setEveryDayOfMonth(db.getInteger(rs, "EVERY_DAYOFMONTH"));
				obj.setEveryMonth(db.getInteger(rs, "EVERY_MONTH"));
				obj.setEveryDayOfWeek(db.getInteger(rs, "EVERY_DAYOFWEEK"));
				obj.setCheckItem(db.getInteger(rs, "ITEM_TYPE"));
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}

		return retVal;
	}

	private ArrayList<OBDtoFaultCheckSchedule> getFaultCheckScheduleListAll(OBDtoSearch searchObj,
			OBDtoOrdering orderObj, OBDatabase db, OBDatabase db2) throws OBException {
		ArrayList<OBDtoFaultCheckSchedule> retVal = new ArrayList<OBDtoFaultCheckSchedule>();

		String sqlText = "";

		try {
			// offset, limit 조건 생성.
			int offset = 0;
			if (searchObj != null && searchObj.getBeginIndex() != null)
				offset = searchObj.getBeginIndex().intValue();
			int limit = 0;
			if (searchObj != null && searchObj.getEndIndex() != null)
				limit = Math.abs(searchObj.getEndIndex().intValue() - offset) + 1;
			String sqlLimit = "";
			if (limit != 0)
				sqlLimit = String.format(" LIMIT %d ", limit);
			String sqlOffset = String.format(" OFFSET %d ", offset);
			/*
			 * // search 조건 생성. String sqlSearch=null; if(searchObj != null &&
			 * searchObj.getSearchKey() !=null && !searchObj.getSearchKey().isEmpty()) {
			 * String wildcardKey="%"+searchObj.getSearchKey()+"%";
			 * sqlSearch=String.format(" ( NAME LIKE %s OR DESCRIPTION LIKE %s ) ",
			 * OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey),
			 * OBParser.sqlString(wildcardKey)); } String sqlTime=""; if(searchObj != null
			 * && searchObj.getToTime()==null) sqlTime=String.format(" OCCUR_TIME <= %s ",
			 * OBParser.sqlString(OBDateTime.now())); else
			 * sqlTime=String.format(" OCCUR_TIME <= %s ",
			 * OBParser.sqlString(OBDateTime.toString(new
			 * Timestamp(searchObj.getToTime().getTime()))));
			 * 
			 * if(searchObj != null && searchObj.getFromTime()!=null)
			 * sqlTime+=String.format(" AND OCCUR_TIME >= %s ",
			 * OBParser.sqlString(OBDateTime.toString(new
			 * Timestamp(searchObj.getFromTime().getTime()))));
			 */

			sqlText = String.format(
					" SELECT INDEX, NAME, DESCRIPTION, STATE, OCCUR_TIME, TARGET_TYPE, TARGET_INDEX, ACCNT_INDEX, CHECK_INTERVAL,\n"
							+ " TEMPLATE_INDEX, SCHEDULE_TYPE, EVERY_MINUTE, EVERY_HOUR, EVERY_DAYOFMONTH, EVERY_MONTH, 					 \n"
							+ " EVERY_DAYOFWEEK, ITEM_TYPE																			     \n"
							+ " FROM MNG_FAULT_SCHEDULE                                                                                    \n"
							+ " WHERE STATE=%d                                                                                             \n",
					OBDefine.STATE_ENABLE);
			/*
			 * if(sqlTime!=null && !sqlTime.isEmpty()) sqlText += " AND" + sqlTime;
			 * 
			 * if(sqlSearch!=null && !sqlSearch.isEmpty()) sqlText += " AND " + sqlSearch;
			 */
			sqlText += " ORDER BY OCCUR_TIME DESC ";

			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			sqlText += sqlOffset;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoADCObject targetObj = getADCObjectInfo(db.getInteger(rs, "TARGET_TYPE"),
						db.getInteger(rs, "TARGET_INDEX"), db2);
				if (targetObj == null)
					continue;
				OBDtoFaultCheckSchedule obj = new OBDtoFaultCheckSchedule();
				obj.setIndex(db.getLong(rs, "INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setDescription(db.getString(rs, "DESCRIPTION"));
				obj.setTargetObj(targetObj);
				obj.setAccntIndex(db.getInteger(rs, "ACCNT_INDEX"));
				obj.setCheckInterval(db.getInteger(rs, "CHECK_INTERVAL"));
				obj.setTemplateIndex(db.getLong(rs, "TEMPLATE_INDEX"));
				obj.setScheduleType(db.getInteger(rs, "SCHEDULE_TYPE"));
				obj.setEveryMinute(db.getInteger(rs, "EVERY_MINUTE"));
				obj.setEveryHour(db.getInteger(rs, "EVERY_HOUR"));
				obj.setEveryDayOfMonth(db.getInteger(rs, "EVERY_DAYOFMONTH"));
				obj.setEveryMonth(db.getInteger(rs, "EVERY_MONTH"));
				obj.setEveryDayOfWeek(db.getInteger(rs, "EVERY_DAYOFWEEK"));
				obj.setCheckItem(db.getInteger(rs, "ITEM_TYPE"));
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}

		return retVal;
	}

	/**
	 * 특정 adc에 한에서 예약 내용을 조회할 때.
	 * 
	 * @param targetObj
	 * @param searchObj
	 * @param orderObj
	 * @param db
	 * @return
	 * @throws OBException
	 */
	private ArrayList<OBDtoFaultCheckSchedule> getFaultCheckScheduleListAdcTarget(Integer adcIndex,
			OBDtoSearch searchObj, OBDtoOrdering orderObj, OBDatabase db, OBDatabase db2) throws OBException {
		ArrayList<OBDtoFaultCheckSchedule> retVal = new ArrayList<OBDtoFaultCheckSchedule>();

		String sqlText = "";

		try {
			// offset, limit 조건 생성.
			int offset = 0;
			if (searchObj != null && searchObj.getBeginIndex() != null)
				offset = searchObj.getBeginIndex().intValue();
			int limit = 0;
			if (searchObj != null && searchObj.getEndIndex() != null)
				limit = Math.abs(searchObj.getEndIndex().intValue() - offset) + 1;
			String sqlLimit = "";
			if (limit != 0)
				sqlLimit = String.format(" LIMIT %d ", limit);
			String sqlOffset = String.format(" OFFSET %d ", offset);
			/*
			 * // search 조건 생성. String sqlSearch=null; if(searchObj != null &&
			 * searchObj.getSearchKey() !=null && !searchObj.getSearchKey().isEmpty()) {
			 * String wildcardKey="%"+searchObj.getSearchKey()+"%";
			 * sqlSearch=String.format(" ( NAME LIKE %s OR DESCRIPTION LIKE %s ) ",
			 * OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey),
			 * OBParser.sqlString(wildcardKey)); } String sqlTime=""; if(searchObj != null
			 * && searchObj.getToTime()==null) sqlTime=String.format(" OCCUR_TIME <= %s ",
			 * OBParser.sqlString(OBDateTime.now())); else
			 * sqlTime=String.format(" OCCUR_TIME <= %s ",
			 * OBParser.sqlString(OBDateTime.toString(new
			 * Timestamp(searchObj.getToTime().getTime()))));
			 * 
			 * if(searchObj != null && searchObj.getFromTime()!=null)
			 * sqlTime+=String.format(" AND OCCUR_TIME >= %s ",
			 * OBParser.sqlString(OBDateTime.toString(new
			 * Timestamp(searchObj.getFromTime().getTime()))));
			 */

			sqlText = String.format(
					" SELECT INDEX, NAME, DESCRIPTION, STATE, OCCUR_TIME, TARGET_TYPE, TARGET_INDEX, ACCNT_INDEX, CHECK_INTERVAL,\n"
							+ " TEMPLATE_INDEX, SCHEDULE_TYPE, EVERY_MINUTE, EVERY_HOUR, EVERY_DAYOFMONTH, EVERY_MONTH, 					 \n"
							+ " EVERY_DAYOFWEEK, ITEM_TYPE																			     \n"
							+ " FROM MNG_FAULT_SCHEDULE                                                                                    \n"
							+ " WHERE TARGET_TYPE = %d AND TARGET_INDEX = %d AND STATE=%d                                                  \n",
					OBDtoADCObject.CATEGORY_ADC, adcIndex, OBDefine.STATE_ENABLE);
			/*
			 * if(sqlTime!=null && !sqlTime.isEmpty()) sqlText += " AND" + sqlTime;
			 * 
			 * if(sqlSearch!=null && !sqlSearch.isEmpty()) sqlText += " AND " + sqlSearch;
			 */
			sqlText += " ORDER BY OCCUR_TIME DESC ";

			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			sqlText += sqlOffset;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoADCObject targetObj = getADCObjectInfo(db.getInteger(rs, "TARGET_TYPE"),
						db.getInteger(rs, "TARGET_INDEX"), db2);
				if (targetObj == null)
					continue;
				OBDtoFaultCheckSchedule obj = new OBDtoFaultCheckSchedule();
				obj.setIndex(db.getLong(rs, "INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setDescription(db.getString(rs, "DESCRIPTION"));
				obj.setTargetObj(targetObj);
				obj.setAccntIndex(db.getInteger(rs, "ACCNT_INDEX"));
				obj.setCheckInterval(db.getInteger(rs, "CHECK_INTERVAL"));
				obj.setTemplateIndex(db.getLong(rs, "TEMPLATE_INDEX"));
				obj.setScheduleType(db.getInteger(rs, "SCHEDULE_TYPE"));
				obj.setEveryMinute(db.getInteger(rs, "EVERY_MINUTE"));
				obj.setEveryHour(db.getInteger(rs, "EVERY_HOUR"));
				obj.setEveryDayOfMonth(db.getInteger(rs, "EVERY_DAYOFMONTH"));
				obj.setEveryMonth(db.getInteger(rs, "EVERY_MONTH"));
				obj.setEveryDayOfWeek(db.getInteger(rs, "EVERY_DAYOFWEEK"));
				obj.setCheckItem(db.getInteger(rs, "ITEM_TYPE"));
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}

		return retVal;
	}

	/**
	 * ADC 전체를 대상으로 예약된 목록 추출.
	 * 
	 * @param targetObj
	 * @param searchObj
	 * @param orderObj
	 * @param db
	 * @return
	 * @throws OBException
	 */
	private ArrayList<OBDtoFaultCheckSchedule> getFaultCheckScheduleListAllTagert(OBDtoSearch searchObj,
			OBDtoOrdering orderObj, OBDatabase db, OBDatabase db2) throws OBException {
		ArrayList<OBDtoFaultCheckSchedule> retVal = new ArrayList<OBDtoFaultCheckSchedule>();

		String sqlText = "";

		try {
			// offset, limit 조건 생성.
			int offset = 0;
			if (searchObj != null && searchObj.getBeginIndex() != null)
				offset = searchObj.getBeginIndex().intValue();
			int limit = 0;
			if (searchObj != null && searchObj.getEndIndex() != null)
				limit = Math.abs(searchObj.getEndIndex().intValue() - offset) + 1;
			String sqlLimit = "";
			if (limit != 0)
				sqlLimit = String.format(" LIMIT %d ", limit);
			String sqlOffset = String.format(" OFFSET %d ", offset);
			/*
			 * // search 조건 생성. String sqlSearch=null; if(searchObj != null &&
			 * searchObj.getSearchKey() !=null && !searchObj.getSearchKey().isEmpty()) {
			 * String wildcardKey="%"+searchObj.getSearchKey()+"%";
			 * sqlSearch=String.format(" ( NAME LIKE %s OR DESCRIPTION LIKE %s ) ",
			 * OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey),
			 * OBParser.sqlString(wildcardKey)); } String sqlTime=""; if(searchObj != null
			 * && searchObj.getToTime()==null) sqlTime=String.format(" OCCUR_TIME <= %s ",
			 * OBParser.sqlString(OBDateTime.now())); else
			 * sqlTime=String.format(" OCCUR_TIME <= %s ",
			 * OBParser.sqlString(OBDateTime.toString(new
			 * Timestamp(searchObj.getToTime().getTime()))));
			 * 
			 * if(searchObj != null && searchObj.getFromTime()!=null)
			 * sqlTime+=String.format(" AND OCCUR_TIME >= %s ",
			 * OBParser.sqlString(OBDateTime.toString(new
			 * Timestamp(searchObj.getFromTime().getTime()))));
			 */
			sqlText = String.format(
					" SELECT INDEX, NAME, DESCRIPTION, STATE, OCCUR_TIME, TARGET_TYPE, TARGET_INDEX, ACCNT_INDEX, CHECK_INTERVAL,\n"
							+ " TEMPLATE_INDEX, SCHEDULE_TYPE, EVERY_MINUTE, EVERY_HOUR, EVERY_DAYOFMONTH, EVERY_MONTH, 					 \n"
							+ " EVERY_DAYOFWEEK, ITEM_TYPE																			     \n"
							+ " FROM MNG_FAULT_SCHEDULE                                                                                    \n"
							+ " WHERE TARGET_TYPE = %d AND STATE=%d                                                                        \n",
					OBDtoADCObject.CATEGORY_ALL, OBDefine.STATE_ENABLE);
			/*
			 * if(sqlTime!=null && !sqlTime.isEmpty()) sqlText += " AND" + sqlTime;
			 * 
			 * if(sqlSearch!=null && !sqlSearch.isEmpty()) sqlText += " AND " + sqlSearch;
			 */
			// sqlText += " ORDER BY A.OCCUR_TIME DESC ";
			sqlText += " ORDER BY OCCUR_TIME DESC ";

			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			sqlText += sqlOffset;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoADCObject targetObj = getADCObjectInfo(db.getInteger(rs, "TARGET_TYPE"),
						db.getInteger(rs, "TARGET_INDEX"), db2);
				if (targetObj == null)
					continue;
				OBDtoFaultCheckSchedule obj = new OBDtoFaultCheckSchedule();
				obj.setIndex(db.getLong(rs, "INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setDescription(db.getString(rs, "DESCRIPTION"));
				obj.setTargetObj(targetObj);
				obj.setAccntIndex(db.getInteger(rs, "ACCNT_INDEX"));
				obj.setCheckInterval(db.getInteger(rs, "CHECK_INTERVAL"));
				obj.setTemplateIndex(db.getLong(rs, "TEMPLATE_INDEX"));
				obj.setScheduleType(db.getInteger(rs, "SCHEDULE_TYPE"));
				obj.setEveryMinute(db.getInteger(rs, "EVERY_MINUTE"));
				obj.setEveryHour(db.getInteger(rs, "EVERY_HOUR"));
				obj.setEveryDayOfMonth(db.getInteger(rs, "EVERY_DAYOFMONTH"));
				obj.setEveryMonth(db.getInteger(rs, "EVERY_MONTH"));
				obj.setEveryDayOfWeek(db.getInteger(rs, "EVERY_DAYOFWEEK"));
				obj.setCheckItem(db.getInteger(rs, "ITEM_TYPE"));
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	public ArrayList<Integer> getAdcList(Integer groupIndex) throws OBException {
		ArrayList<Integer> retVal = new ArrayList<Integer>();

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT INDEX                                              \n"
							+ " FROM MNG_ADC                                              \n"
							+ " WHERE GROUP_INDEX = %d AND AVAILABLE = %d                 \n",
					groupIndex, OBDefine.ADC_STATE.AVAILABLE);

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				retVal.add(db.getInteger(rs, "INDEX"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	public ArrayList<Integer> getAdcListAll(Integer groupIndex) throws OBException {
		ArrayList<Integer> retVal = new ArrayList<Integer>();

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT INDEX                                              \n"
							+ " FROM MNG_ADC                                              \n"
							+ " WHERE AVAILABLE = %d                                      \n",
					OBDefine.ADC_STATE.AVAILABLE);

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				retVal.add(db.getInteger(rs, "INDEX"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	/**
	 * 그룹에 포함된 adc에 할당된 예약 목록을 추출한다.
	 * 
	 * @param groupIndex
	 * @param searchObj
	 * @param orderObj
	 * @param db
	 * @param db2
	 * @return
	 * @throws OBException
	 */
	private ArrayList<OBDtoFaultCheckSchedule> getFaultCheckScheduleListInvolvedAdc(Integer groupIndex,
			OBDtoSearch searchObj, OBDtoOrdering orderObj) throws OBException {
		ArrayList<OBDtoFaultCheckSchedule> retVal = new ArrayList<OBDtoFaultCheckSchedule>();

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			ArrayList<Integer> adcIndexList = getAdcList(groupIndex);
			if (adcIndexList.size() <= 0)
				return retVal;// group에 할당된 adc가 없는 경우.

			String adcListStr = "-1";
			for (Integer adcIndex : adcIndexList) {
				if (!adcListStr.isEmpty())
					adcListStr += ", ";
				adcListStr += adcIndex;
			}
			// offset, limit 조건 생성.
			int offset = 0;
			if (searchObj != null && searchObj.getBeginIndex() != null)
				offset = searchObj.getBeginIndex().intValue();
			int limit = 0;
			if (searchObj != null && searchObj.getEndIndex() != null)
				limit = Math.abs(searchObj.getEndIndex().intValue() - offset) + 1;
			String sqlLimit = "";
			if (limit != 0)
				sqlLimit = String.format(" LIMIT %d ", limit);
			String sqlOffset = String.format(" OFFSET %d ", offset);
			/*
			 * // search 조건 생성. String sqlSearch=null; if(searchObj != null &&
			 * searchObj.getSearchKey() !=null && !searchObj.getSearchKey().isEmpty()) {
			 * String wildcardKey="%"+searchObj.getSearchKey()+"%";
			 * sqlSearch=String.format(" ( NAME LIKE %s OR DESCRIPTION LIKE %s ) ",
			 * OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey),
			 * OBParser.sqlString(wildcardKey)); } String sqlTime=""; if(searchObj != null
			 * && searchObj.getToTime()==null) sqlTime=String.format(" OCCUR_TIME <= %s ",
			 * OBParser.sqlString(OBDateTime.now())); else
			 * sqlTime=String.format(" OCCUR_TIME <= %s ",
			 * OBParser.sqlString(OBDateTime.toString(new
			 * Timestamp(searchObj.getToTime().getTime()))));
			 * 
			 * if(searchObj != null && searchObj.getFromTime()!=null)
			 * sqlTime+=String.format(" AND OCCUR_TIME >= %s ",
			 * OBParser.sqlString(OBDateTime.toString(new
			 * Timestamp(searchObj.getFromTime().getTime()))));
			 */
			sqlText = String.format(
					" SELECT INDEX, NAME, DESCRIPTION, STATE, OCCUR_TIME, TARGET_TYPE, TARGET_INDEX, ACCNT_INDEX, CHECK_INTERVAL,\n"
							+ " TEMPLATE_INDEX, SCHEDULE_TYPE, EVERY_MINUTE, EVERY_HOUR, EVERY_DAYOFMONTH, EVERY_MONTH, 					 \n"
							+ " EVERY_DAYOFWEEK, ITEM_TYPE																			     \n"
							+ " FROM MNG_FAULT_SCHEDULE                                                                                    \n"
							+ " WHERE TARGET_TYPE = %d AND TARGET_INDEX IN ( %s ) AND STATE=%d                                             \n",
					OBDtoADCObject.CATEGORY_ADC, adcListStr, OBDefine.STATE_ENABLE);
			/*
			 * if(sqlTime!=null && !sqlTime.isEmpty()) sqlText += " AND" + sqlTime;
			 * 
			 * if(sqlSearch!=null && !sqlSearch.isEmpty()) sqlText += " AND " + sqlSearch;
			 */
			sqlText += " ORDER BY OCCUR_TIME DESC ";

			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			sqlText += sqlOffset;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoADCObject targetObj = getADCObjectInfo(db.getInteger(rs, "TARGET_TYPE"),
						db.getInteger(rs, "TARGET_INDEX"));
				if (targetObj == null)
					continue;
				OBDtoFaultCheckSchedule obj = new OBDtoFaultCheckSchedule();
				obj.setIndex(db.getLong(rs, "INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setDescription(db.getString(rs, "DESCRIPTION"));
				obj.setTargetObj(targetObj);
				obj.setAccntIndex(db.getInteger(rs, "ACCNT_INDEX"));
				obj.setCheckInterval(db.getInteger(rs, "CHECK_INTERVAL"));
				obj.setTemplateIndex(db.getLong(rs, "TEMPLATE_INDEX"));
				obj.setScheduleType(db.getInteger(rs, "SCHEDULE_TYPE"));
				obj.setEveryMinute(db.getInteger(rs, "EVERY_MINUTE"));
				obj.setEveryHour(db.getInteger(rs, "EVERY_HOUR"));
				obj.setEveryDayOfMonth(db.getInteger(rs, "EVERY_DAYOFMONTH"));
				obj.setEveryMonth(db.getInteger(rs, "EVERY_MONTH"));
				obj.setEveryDayOfWeek(db.getInteger(rs, "EVERY_DAYOFWEEK"));
				obj.setCheckItem(db.getInteger(rs, "ITEM_TYPE"));
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	/**
	 * tagetOject로 입력된 adc를 포함하는 group이 있는지 확인 후 해당 그룹으로 지정된 예약이 있는지 확인한다.
	 * 
	 * @param adcIndex  . adcIndex만 입력해야 함. group index 입력하면 안됨.
	 * @param searchObj
	 * @param orderObj
	 * @param db
	 * @param db2
	 * @return
	 * @throws OBException
	 */
	private ArrayList<OBDtoFaultCheckSchedule> getFaultCheckScheduleListInvolvedGroup(Integer adcIndex,
			OBDtoSearch searchObj, OBDtoOrdering orderObj, OBDatabase db, OBDatabase db2) throws OBException {
		ArrayList<OBDtoFaultCheckSchedule> retVal = new ArrayList<OBDtoFaultCheckSchedule>();

		String sqlText = "";
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_DATA_NOT_EXIST);
			}
			if (adcInfo.getGroupIndex() == 0) {
				return retVal;
			}

			// offset, limit 조건 생성.
			int offset = 0;
			if (searchObj != null && searchObj.getBeginIndex() != null)
				offset = searchObj.getBeginIndex().intValue();
			int limit = 0;
			if (searchObj != null && searchObj.getEndIndex() != null)
				limit = Math.abs(searchObj.getEndIndex().intValue() - offset) + 1;
			String sqlLimit = "";
			if (limit != 0)
				sqlLimit = String.format(" LIMIT %d ", limit);
			String sqlOffset = String.format(" OFFSET %d ", offset);
			/*
			 * // search 조건 생성. String sqlSearch=null; if(searchObj != null &&
			 * searchObj.getSearchKey() !=null && !searchObj.getSearchKey().isEmpty()) {
			 * String wildcardKey="%"+searchObj.getSearchKey()+"%";
			 * sqlSearch=String.format(" ( NAME LIKE %s OR DESCRIPTION LIKE %s ) ",
			 * OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey),
			 * OBParser.sqlString(wildcardKey)); } String sqlTime=""; if(searchObj != null
			 * && searchObj.getToTime()==null) sqlTime=String.format(" OCCUR_TIME <= %s ",
			 * OBParser.sqlString(OBDateTime.now())); else
			 * sqlTime=String.format(" OCCUR_TIME <= %s ",
			 * OBParser.sqlString(OBDateTime.toString(new
			 * Timestamp(searchObj.getToTime().getTime()))));
			 * 
			 * if(searchObj != null && searchObj.getFromTime()!=null)
			 * sqlTime+=String.format(" AND OCCUR_TIME >= %s ",
			 * OBParser.sqlString(OBDateTime.toString(new
			 * Timestamp(searchObj.getFromTime().getTime()))));
			 */
			sqlText = String.format(
					" SELECT INDEX, NAME, DESCRIPTION, STATE, OCCUR_TIME, TARGET_TYPE, TARGET_INDEX, ACCNT_INDEX, CHECK_INTERVAL,\n"
							+ " TEMPLATE_INDEX, SCHEDULE_TYPE, EVERY_MINUTE, EVERY_HOUR, EVERY_DAYOFMONTH, EVERY_MONTH, 					 \n"
							+ " EVERY_DAYOFWEEK, ITEM_TYPE																			     \n"
							+ " FROM MNG_FAULT_SCHEDULE                                                                                    \n"
							+ " WHERE TARGET_TYPE = %d AND TARGET_INDEX=%d AND STATE=%d                                                    \n",
					OBDtoADCObject.CATEGORY_GROUP, adcInfo.getGroupIndex(), OBDefine.STATE_ENABLE);
			/*
			 * if(sqlTime!=null && !sqlTime.isEmpty()) sqlText += " AND" + sqlTime;
			 * 
			 * if(sqlSearch!=null && !sqlSearch.isEmpty()) sqlText += " AND " + sqlSearch;
			 */
			sqlText += " ORDER BY OCCUR_TIME DESC ";

			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			sqlText += sqlOffset;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoADCObject targetObj = getADCObjectInfo(db.getInteger(rs, "TARGET_TYPE"),
						db.getInteger(rs, "TARGET_INDEX"), db2);
				if (targetObj == null)
					continue;
				OBDtoFaultCheckSchedule obj = new OBDtoFaultCheckSchedule();
				obj.setIndex(db.getLong(rs, "INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setDescription(db.getString(rs, "DESCRIPTION"));
				obj.setTargetObj(targetObj);
				obj.setAccntIndex(db.getInteger(rs, "ACCNT_INDEX"));
				obj.setCheckInterval(db.getInteger(rs, "CHECK_INTERVAL"));
				obj.setTemplateIndex(db.getLong(rs, "TEMPLATE_INDEX"));
				obj.setScheduleType(db.getInteger(rs, "SCHEDULE_TYPE"));
				obj.setEveryMinute(db.getInteger(rs, "EVERY_MINUTE"));
				obj.setEveryHour(db.getInteger(rs, "EVERY_HOUR"));
				obj.setEveryDayOfMonth(db.getInteger(rs, "EVERY_DAYOFMONTH"));
				obj.setEveryMonth(db.getInteger(rs, "EVERY_MONTH"));
				obj.setEveryDayOfWeek(db.getInteger(rs, "EVERY_DAYOFWEEK"));
				obj.setCheckItem(db.getInteger(rs, "ITEM_TYPE"));
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	@Override
	public void cancelFaultCheck(Long checkKey, OBDtoExtraInfo extraInfo) throws OBException {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			cancelFaultCheck(checkKey, extraInfo, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private void cancelFaultCheck(Long checkKey, OBDtoExtraInfo extraInfo, OBDatabase db) throws OBException {
		String sqlText = "";

		try {
			sqlText = String.format(
					" UPDATE LOG_FAULT                        \n" + " SET STATUS_TOTAL=%d, CANCEL_FLAG=%d     \n"
							+ " WHERE LOG_KEY=%d                        \n",
					OBDefine.FAULT_CHECK_STATUS_CANCEL, OBDefine.STATE_ENABLE, checkKey);

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
	}

	public boolean isFaultCheckCanceled(Long checkKey) throws OBException {
		String sqlText = "";
		boolean retVal = false;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT CANCEL_FLAG FROM LOG_FAULT       \n" + " WHERE LOG_KEY=%d                        \n",
					checkKey);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				if (db.getInteger(rs, "CANCEL_FLAG") > 0)
					retVal = true;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	public boolean isPktdumpCancelStopped(Long checkKey, OBDatabase db) throws OBException {
		String sqlText = "";
		boolean retVal = false;
		try {
			sqlText = String.format(" SELECT CANCEL_STOP_FLAGE FROM LOG_PKT_DUMP       \n"
					+ " WHERE LOG_KEY=%d                                 \n", checkKey);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				if (db.getInteger(rs, "CANCEL_STOP_FLAGE") > 0)
					retVal = true;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoADCObject object = new OBDtoADCObject();
	// object.setCategory(OBDtoADCObject.CATEGORY_ALL);
	// object.setDesciption("전체 대상 스케줄.");
	// object.setName("total schedule");
	// OBDtoScheduleDateTime scheduleObj = new OBDtoScheduleDateTime();
	// scheduleObj.setEveryDayOfMonth(1);
	// scheduleObj.setEveryHour(22);
	// scheduleObj.setEveryMinute(11);
	// OBDtoFaultCheckTemplate templateObj = new OBDtoFaultCheckTemplate();
	// templateObj.setIndex(0L);
	// templateObj.setName("");
	// templateObj.setHwCheckItems(new
	// OBFaultMngImpl().getElements(OBDefine.ELEMENT_TYPE_HW, db));
	// templateObj.setL23CheckItems(new
	// OBFaultMngImpl().getElements(OBDefine.ELEMENT_TYPE_L23, db));
	// templateObj.setL47CheckItems(new
	// OBFaultMngImpl().getElements(OBDefine.ELEMENT_TYPE_L47, db));
	// templateObj.setSvcCheckItems(new
	// OBFaultMngImpl().getElements(OBDefine.ELEMENT_TYPE_SVC, db));
	// templateObj.setSvcVSIndex("1234455");
	//
	// OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
	// extraInfo.setAccountIndex(1);
	// new OBFaultMngImpl().startFaultCheck(object, templateObj, 0, extraInfo);
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public ArrayList<OBDtoObjectIndexInfo> startFaultCheck(OBDtoADCObject object, OBDtoFaultCheckTemplate templateObj,
			Integer checkSpeed, OBDtoExtraInfo extraInfo) throws OBException {// 로그 테이블에 신규 등록 후 작업을 실시한다.
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoObjectIndexInfo> retVal = new ArrayList<OBDtoObjectIndexInfo>();
		try {
			db.openDB();
			// client ip 정보를 저장.
			addUsedClientIP(templateObj.getSvcClientIPAddress(), extraInfo.getAccountIndex());

			// 입력 받은 템플릿을 저장한다.
			if (templateObj.getIndex().intValue() == 0) {
				Long templateIndex = saveFaultCheckTemplate(templateObj, OBDefine.STATE_TEMPORARY,
						extraInfo.getAccountIndex(), db);
				templateObj.setIndex(templateIndex);
			}

			retVal = startFaultCheck(object, templateObj, checkSpeed, extraInfo, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}

		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// long index = new OBFaultMngImpl().makeCheckKey(123);
	// System.out.println(index);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	private long makeCheckKey(Integer adcIndex) throws OBException {
		long currentTime = OBDateTime.toTimestamp(OBDateTime.now()).getTime();
		long retVal = 0L;
		try {
			currentTime = currentTime & 0xffffffffffL;
			long temp = (long) adcIndex * 0x10000000000L;
			retVal = temp | currentTime;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	private ArrayList<Integer> getAdcIndexList(OBDtoADCObject object, OBDatabase db) throws OBException {
		ArrayList<Integer> retVal = new ArrayList<Integer>();

		String sqlText = "";
		try {
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {// 대상이 전체인 경우.
				sqlText = String.format(
						" SELECT INDEX                                                    \n"
								+ " FROM MNG_ADC                                                    \n"
								+ " WHERE AVAILABLE = %d AND STATUS = %d                            \n",
						OBDefine.ADC_STATE.AVAILABLE, OBDefine.ADC_STATUS.REACHABLE);

			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				sqlText = String.format(
						" SELECT INDEX                                                      \n"
								+ " FROM MNG_ADC                                                      \n"
								+ " WHERE GROUP_INDEX = %d AND AVAILABLE = %d AND STATUS = %d         \n",
						object.getIndex(), OBDefine.ADC_STATE.AVAILABLE, OBDefine.ADC_STATUS.REACHABLE);
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_ADC) {
				retVal.add(object.getIndex());
				return retVal;
			} else {
				return retVal;
			}

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				retVal.add(db.getInteger(rs, "INDEX"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}

		return retVal;
	}

	public ArrayList<OBDtoObjectIndexInfo> startFaultCheck(OBDtoADCObject object, OBDtoFaultCheckTemplate templateObj,
			Integer checkSpeed, OBDtoExtraInfo extraInfo, OBDatabase db) throws OBException {// 로그 테이블에 신규 등록 후 작업을
																								// 실시한다.
		ArrayList<OBDtoObjectIndexInfo> retVal = new ArrayList<OBDtoObjectIndexInfo>();
		ArrayList<OBDtoObjectIndexInfo> logIndexList = new ArrayList<OBDtoObjectIndexInfo>();
		try {
			OBDtoFaultCheckItemCountInfo countInfo = getCheckItemCountInfo(templateObj);

			long templateIndex = templateObj.getIndex();
			int hwCheckTotal = countInfo.getHwCheckTotal();
			// int svcCheckTotal = countInfo.getSvcCheckTotal();

			int checkItemFlag = 0;
			// if(hwCheckTotal>0)
			// checkItemFlag |= OBDtoFaultCheckLog.CHECK_ITEM_HW;

			int checkItemFlag_HW = 0;
			int checkItemFlag_SVC = 0;
			if (hwCheckTotal > 0)
				checkItemFlag_HW = OBDtoFaultCheckLog.CHECK_ITEM_HW;

			if (templateObj.getSvcCheckFlg() == 1)
				checkItemFlag_SVC = OBDtoFaultCheckLog.CHECK_ITEM_SVC;

			checkItemFlag = checkItemFlag_HW + checkItemFlag_SVC;

			if (templateObj.getIndex() == 0) {
				if (templateIndex == 0L) {
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "invalid template index");
				}
				templateObj.setIndex(templateIndex);
			}
			String sqlText = "";
			ArrayList<Integer> adcIndexList = getAdcIndexList(object, db);
			ArrayList<Long> checkKeyList = new ArrayList<Long>();

			for (Integer adcIndex : adcIndexList) {
				try {
					OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

					// 로그를 저장한다.
					long checkKey = makeCheckKey(adcIndex);

					// 동일 로그가 있으면 삭제한다.
					String sqlTextLatestDelete = String.format(" DELETE FROM LOG_FAULT WHERE LOG_KEY = %d;", checkKey);
					db.executeUpdate(sqlTextLatestDelete);
					sqlTextLatestDelete = String.format(" DELETE FROM LOG_FAULT_SUMMARY WHERE LOG_KEY = %d;", checkKey);
					db.executeUpdate(sqlTextLatestDelete);
					// sqlTextLatestDelete=String.format(" DELETE FROM LOG_SUMMARY_PKT_LOSS WHERE
					// LOG_KEY = %d;", checkKey);
					// db.executeUpdate(sqlTextLatestDelete);
					sqlTextLatestDelete = String.format(" DELETE FROM LOG_SUMMARY_RESP_TIME WHERE LOG_KEY = %d;",
							checkKey);
					db.executeUpdate(sqlTextLatestDelete);

					OBDtoObjectIndexInfo retObj = new OBDtoObjectIndexInfo();
					retObj.setLogKey(checkKey);
					retObj.setObj(getADCObjectInfo(OBDtoADCObject.CATEGORY_ADC, adcIndex, db));
					logIndexList.add(retObj);
					String clientIP = "";
					String vsvcIndex = "";
					if (templateObj.getSvcCheckFlg() > 0) {
						if (templateObj.getSvcClientIPAddress() != null)
							clientIP = templateObj.getSvcClientIPAddress();
						if (templateObj.getSvcVSIndex() != null)
							vsvcIndex = templateObj.getSvcVSIndex();
					}
					// 장애진단 팝업에서 사용하는 컬럼이 누락됨 임시추가
					sqlText = String.format(
							" INSERT INTO LOG_FAULT                                                                                			 \n"
									+ " (LOG_KEY, OCCUR_TIME, STATUS_TOTAL, ELAPSE_TIME, ADC_INDEX, ADC_NAME, ACCNT_INDEX, MSG_SUMMARY, TEMPLATE_INDEX, \n"
									+ " ITEM_HW_TOTAL, STATUS_HW_CHECK, STATUS_HW_FAIL, STATUS_SVC, ITEM_TYPE, CLIENT_IP, VSVC_INDEX, AVAILABLE)         \n"
									+ " VALUES                                                                                               				\n"
									+ " ( %d, %s, %d, %d, %d, %s, %d, %s, %d, %d, %d, %d, %d, %d, %s, %s, %d)                                            \n",
							checkKey, OBParser.sqlString(OBDateTime.now()), 0, 0, adcIndex,
							OBParser.sqlString(adcInfo.getName()), extraInfo.getAccountIndex(), OBParser.sqlString(""),
							templateObj.getIndex(), hwCheckTotal, 0, 0, 0, checkItemFlag, OBParser.sqlString(clientIP),
							OBParser.sqlString(vsvcIndex), OBDefine.ADC_STATE.AVAILABLE);

					db.executeUpdate(sqlText);
					checkKeyList.add(checkKey);
					// 감사로그 생성.
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAuditImpl.AUDIT_FAULT_CHECK_START_SUCCESS, adcInfo.getName());
				} catch (SQLException e) {
					OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAuditImpl.AUDIT_FAULT_CHECK_START_FAIL, adcInfo.getName());
					throw new OBException(OBException.ERRCODE_DB_QEURY,
							String.format("%s, sqlText:%s", e.getMessage(), sqlText));
				} catch (Exception e) {
					OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAuditImpl.AUDIT_FAULT_CHECK_START_FAIL, adcInfo.getName());
					throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
				}
			}

			// 위에서 추가한 로그에 대한 작업을 쓰레드로 실행한다.
			for (OBDtoObjectIndexInfo checkKey : logIndexList) {
				try {
					OBDtoFaultCheckThreadInfo threadInfo = new OBDtoFaultCheckThreadInfo();
					threadInfo.setCheckKey(checkKey.getLogKey());
					threadInfo.setAccntIndex(extraInfo.getAccountIndex());
					threadInfo.setClientIP(extraInfo.getClientIPAddress());
					threadInfo.setAdcIndex(checkKey.getObj().getIndex());
					Runnable r = new OBFaultCheckWorker(threadInfo);
					Thread t = new Thread(r);
					t.start();

					retVal.add(checkKey);
				} catch (Exception e) {
					updateDiagnosisStatus(checkKey.getLogKey(), OBDefine.FAULT_CHECK_STATUS_FAILURE, null, null, null,
							null, OBFaultCheckMsg.CHECK_STATUS.getProcessFailMsg(),
							OBException.getExceptionMessage(OBException.ERRCODE_SYSTEM_CREATE_THREAD),
							OBException.getExceptionMessage(OBException.ERRCODE_SYSTEM_CREATE_THREAD));

					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAuditImpl.AUDIT_FAULT_CHECK_FAIL, getAdcNameByLogKey(checkKey.getLogKey(), db));
				}
			}
		} catch (RuntimeException e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "RuntimeException: " + e.getMessage());//
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "Exception: " + e.getMessage());//
		}
		return retVal;
	}

	private String getAdcNameByLogKey(Long logKey, OBDatabase db) throws OBException {
		String retVal = "";
		try {
			OBDtoFaultCheckLog logInfo = new OBFaultMngImpl().getFaultCheckLogInfo(logKey, db);
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(logInfo.getAdcIndex());
			retVal = adcInfo.getName();
		} catch (Exception e) {

		}

		return retVal;
	}

	/**
	 * 현재 진행중인 item 정보를 업데이트 한다. 예: 하드웨어 항목을 진단하고 있습니다.(10/50)
	 * 
	 * @param logKey
	 * @param msg
	 * @param db
	 * @throws OBException
	 */
	// public void updateCheckItemInfo(Long logKey, String info, String msg,
	// OBDatabase db)
	// {
	// String sqlText="";
	//
	// try
	// {
	// sqlText = String.format(" UPDATE LOG_FAULT \n" +
	// " SET CHECK_ITEM_INFO=%s, CHECK_ITEM_MSG=%s \n" +
	// " WHERE LOG_KEY=%d \n",
	// OBParser.sqlString(info),
	// OBParser.sqlString(msg),
	// logKey);
	//
	// db.executeUpdate(sqlText);
	// }
	// catch(SQLException e)
	// {
	// // throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(OBException e)
	// {
	// // throw e;
	// }
	// catch(Exception e)
	// {
	// // throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
	// }
	// }

	/**
	 * 진단시 발생된 상세 데이터(CLI 명령)등을 업데이트한다.
	 * 
	 * @param logKey
	 * @param msg
	 * @param db
	 * @throws OBException
	 */
	// public void updateCheckItemMsg(Long logKey, String msg, OBDatabase db) throws
	// OBException
	// {
	// String sqlText="";
	//
	// try
	// {
	// sqlText = String.format(" UPDATE LOG_FAULT \n" +
	// " SET CHECK_ITEM_MSG=%s \n" +
	// " WHERE LOG_KEY=%d \n",
	// OBParser.sqlString(msg),
	// logKey);
	//
	// db.executeUpdate(sqlText);
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
	// }
	// }

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoADCObject object = new OBDtoADCObject();
	// object.setCategory(OBDtoADCObject.CATEGORY_ALL);
	// object.setDesciption("전체 대상 스케줄.");
	// object.setName("total schedule");
	// OBDtoScheduleDateTime scheduleObj = new OBDtoScheduleDateTime();
	// scheduleObj.setEveryDayOfMonth(1);
	// scheduleObj.setEveryHour(22);
	// scheduleObj.setEveryMinute(11);
	// OBDtoFaultCheckTemplate templateObj = new OBDtoFaultCheckTemplate();
	// templateObj.setIndex(3L);
	// templateObj.setName("");
	// templateObj.setHwCheckItems(new
	// OBFaultMngImpl().getElements(OBDefine.ELEMENT_TYPE_HW, db));
	// templateObj.setL23CheckItems(new
	// OBFaultMngImpl().getElements(OBDefine.ELEMENT_TYPE_L23, db));
	// templateObj.setL47CheckItems(new
	// OBFaultMngImpl().getElements(OBDefine.ELEMENT_TYPE_L47, db));
	// templateObj.setSvcCheckItems(new
	// OBFaultMngImpl().getElements(OBDefine.ELEMENT_TYPE_SVC, db));
	// templateObj.setSvcVSIndex("1234455");
	//
	// Integer checkSpeed = 0;
	// Integer repeatFlag = 1;
	// OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
	// extraInfo.setAccountIndex(1);
	// new OBFaultMngImpl().registerFaultCheckSchedule(object, "test", "test
	// description", templateObj, scheduleObj, checkSpeed, repeatFlag, extraInfo);
	//
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public ArrayList<OBDtoElement> getElements(Integer type, OBDatabase db) throws OBException {
		ArrayList<OBDtoElement> retVal = new ArrayList<OBDtoElement>();
		String sqlText = "";

		try {
			sqlText = String.format(
					" SELECT CODE, NAME, DESCRIPTION, TARGET  \n" + " FROM %s                                 \n"
							+ " WHERE TYPE=%d                           \n",
					OBCommon.makeProperTableName("MNG_FAULT_CHECK_ITEMS"), type);
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoElement obj = new OBDtoElement();
				obj.setCategory(type);
				obj.setDescription(db.getString(rs, "DESCRIPTION"));
				obj.setIndex(db.getInteger(rs, "CODE"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setState(OBDefine.STATE_DISABLE);
				// obj.setState(OBDefine.STATE_ENABLE);
				obj.setTarget(db.getInteger(rs, "TARGET"));
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	@Override
	public void registerFaultCheckSchedule(OBDtoADCObject object, String name, String description,
			OBDtoFaultCheckTemplate templateObj, OBDtoScheduleDateTime timeObj, Integer checkSpeed,
			Integer scheduleType, OBDtoExtraInfo extraInfo) throws OBException {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (isRegisteredSchedule(object, timeObj, scheduleType, db) == true) {
				throw new OBException(OBException.ERRCODE_FAULT_DUP_SCHEDULE);
			}

			// client ip 정보를 저장.
			addUsedClientIP(templateObj.getSvcClientIPAddress(), extraInfo.getAccountIndex());

			registerFaultCheckSchedule(object, name, description, templateObj, timeObj, checkSpeed, scheduleType,
					extraInfo, db);
			// 감사로그 생성.
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_FAULT_SAVE_SCHEDULE_SUCCESS, name);
		} catch (OBException e) {
			// 실패 감사로그 생성.
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_FAULT_SAVE_SCHEDULE_FAIL, name, e.getErrorMessage());
			throw e;
		} catch (Exception e) {
			// 실패 감사로그 생성.
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_FAULT_SAVE_SCHEDULE_FAIL, name, OBException.ERRCODE_SYSTEM_ILLEGALNULL);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private void registerFaultCheckSchedule(OBDtoADCObject object, String name, String description,
			OBDtoFaultCheckTemplate templateObj, OBDtoScheduleDateTime timeObj, Integer checkSpeed,
			Integer scheduleTypeValue, OBDtoExtraInfo extraInfo, OBDatabase db) throws OBException {
		if (object == null || templateObj == null || timeObj == null || extraInfo == null || db == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER);
		}

		String sqlText = "";

		try {
			int targetType = 0;
			int targetIndex = 0;
			int accntIndex = 0;
			int checkInterval = 0;
			long templateIndex = 0;
			int scheduleType = 0;
			int everyMin = 0;
			int everyHour = 0;
			int everyDayOfMonth = 0;
			int everyMonth = 0;
			int everyDayOfWeek = 0;

			targetType = object.getCategory();
			targetIndex = object.getIndex();
			accntIndex = extraInfo.getAccountIndex();
			if (checkSpeed != null)
				checkInterval = checkSpeed;
			if (scheduleTypeValue != null)
				scheduleType = scheduleTypeValue;
			if (timeObj.getEveryDayOfMonth() != null)
				everyDayOfMonth = timeObj.getEveryDayOfMonth();
			if (timeObj.getEveryDayOfWeek() != null)
				everyDayOfWeek = timeObj.getEveryDayOfWeek();
			if (timeObj.getEveryHour() != null)
				everyHour = timeObj.getEveryHour();
			if (timeObj.getEveryMinute() != null)
				everyMin = timeObj.getEveryMinute();
			if (timeObj.getEveryMonth() != null)
				everyMonth = timeObj.getEveryMonth();

			OBDtoFaultCheckItemCountInfo countInfo = getCheckItemCountInfo(templateObj);

			int hwCheckTotal = countInfo.getHwCheckTotal();

			int checkItemFlag = 0;

			int checkItemFlag_HW = 0;
			int checkItemFlag_SVC = 0;
			if (hwCheckTotal > 0)
				checkItemFlag_HW = OBDtoFaultCheckLog.CHECK_ITEM_HW;
			if (templateObj.getSvcCheckFlg() == 1)
				checkItemFlag_SVC = OBDtoFaultCheckLog.CHECK_ITEM_SVC;

			checkItemFlag = checkItemFlag_HW + checkItemFlag_SVC;

			// 템플릿 index 처리.
			if (templateObj.getIndex() != 0) {
				templateIndex = templateObj.getIndex();
			} else {
				// 한시적인 형태로 템플릿 정보를 저장한다.
				templateIndex = saveFaultCheckTemplate(templateObj, OBDefine.STATE_TEMPORARY, accntIndex, db);
			}

			sqlText = String.format(
					" INSERT INTO MNG_FAULT_SCHEDULE                                                                          \n"
							+ " (NAME, DESCRIPTION, STATE, OCCUR_TIME, TARGET_TYPE, TARGET_INDEX, ACCNT_INDEX, CHECK_INTERVAL,          \n"
							+ " TEMPLATE_INDEX, SCHEDULE_TYPE, EVERY_MINUTE, EVERY_HOUR, EVERY_DAYOFMONTH, EVERY_MONTH, 				  \n"
							+ " EVERY_DAYOFWEEK, ITEM_TYPE ) 																			  \n"
							+ " VALUES                                                                                                  \n"
							+ " ( %s, %s, %d, %s, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d )                                          \n",
					OBParser.sqlString(name), OBParser.sqlString(description), OBDefine.STATE_ENABLE,
					OBParser.sqlString(OBDateTime.now()), targetType, targetIndex, accntIndex, checkInterval,
					templateIndex, scheduleType, everyMin, everyHour, everyDayOfMonth, everyMonth, everyDayOfWeek,
					checkItemFlag);

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
	}

	/**
	 * 등록된 스케줄인지 검사한다. type,시간이 동일하면 동일 스케줄로 간주한다.
	 * 
	 * @param object
	 * @param timeObj
	 * @param db
	 * @return
	 * @throws OBException
	 */
	private boolean isRegisteredSchedule(OBDtoADCObject object, OBDtoScheduleDateTime timeObj, Integer scheduleType,
			OBDatabase db) throws OBException {
		boolean retVal = false;
		String sqlText = "";

		try {
			int everyMin = 0;
			int everyHour = 0;
			int everyDayOfMonth = 0;
			int everyMonth = 0;
			int everyDayOfWeek = 0;

			if (timeObj.getEveryDayOfMonth() != null)
				everyDayOfMonth = timeObj.getEveryDayOfMonth();
			if (timeObj.getEveryDayOfWeek() != null)
				everyDayOfWeek = timeObj.getEveryDayOfWeek();
			if (timeObj.getEveryHour() != null)
				everyHour = timeObj.getEveryHour();
			if (timeObj.getEveryMinute() != null)
				everyMin = timeObj.getEveryMinute();
			if (timeObj.getEveryMonth() != null)
				everyMonth = timeObj.getEveryMonth();

			sqlText = String.format(
					" SELECT INDEX FROM MNG_FAULT_SCHEDULE        \n"
							+ " WHERE                                       \n"
							+ " TARGET_TYPE=%d AND TARGET_INDEX=%d          \n"
							+ " AND SCHEDULE_TYPE=%d AND EVERY_MINUTE=%d      \n"
							+ " AND EVERY_HOUR=%d AND EVERY_DAYOFMONTH=%d   \n"
							+ " AND EVERY_MONTH=%d AND EVERY_DAYOFWEEK=%d   \n"
							+ " AND STATE=%d                                \n"
							+ " LIMIT 1                                     \n",
					object.getCategory(), object.getIndex(), scheduleType, everyMin, everyHour, everyDayOfMonth,
					everyMonth, everyDayOfWeek, OBDefine.STATE_ENABLE);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = true;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}

		return retVal;
	}

	@Override
	public void saveFaultCheckTemplate(OBDtoFaultCheckTemplate templateObj, OBDtoExtraInfo extraInfo)
			throws OBException {
		// this.templateIndex = 0L;

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			// client ip 정보를 저장.
			addUsedClientIP(templateObj.getSvcClientIPAddress(), extraInfo.getAccountIndex());

			saveFaultCheckTemplate(templateObj, OBDefine.STATE_ENABLE, extraInfo.getAccountIndex(), db);

			// 감사로그 생성.
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_FAULT_SAVE_TEMPLATE_SUCCESS, templateObj.getName());
		} catch (OBException e) {// 실패 감사로그 생성.
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_FAULT_SAVE_TEMPLATE_FAIL, templateObj.getName(), e.getErrorMessage());
			throw e;
		} catch (Exception e) {// 실패 감사로그 생성.
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_FAULT_SAVE_TEMPLATE_FAIL, templateObj.getName(),
					OBException.ERRCODE_SYSTEM_ILLEGALNULL);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private long convertElementToLong(ArrayList<OBDtoElement> itemList) throws OBException {
		long retVal = 0L;
		for (OBDtoElement obj : itemList) {
			if (obj.getState() == OBDefine.STATE_ENABLE)
				retVal += (1 << (obj.getIndex() - 1));
		}
		return retVal;
	}

	private ArrayList<OBDtoElement> convertElementToArray(Integer type, Long onoffValue, OBDatabase db)
			throws OBException {
		ArrayList<OBDtoElement> retVal = getElements(type, db);

		for (OBDtoElement obj : retVal) {
			long onoff = (1 << (obj.getIndex() - 1));
			if ((onoffValue & onoff) > 0)
				obj.setState(OBDefine.STATE_ENABLE);
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// System.out.println(new OBFaultMngImpl().getEnabledItemCount(1387906198867L));
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	private Integer getEnabledItemCount(Long onoffValue) throws OBException {
		Integer retVal = 0;
		for (int i = 0; i < 32; i++) {
			long onoff = (1 << i);
			if ((onoffValue & onoff) > 0)
				retVal++;
		}
		return retVal;
	}

	final static String THRESHOLD_SUFFIX_HW_CPU = "HW_CPU";
	final static String THRESHOLD_SUFFIX_HW_MEMORY = "HW_MEMORY";
	final static String THRESHOLD_SUFFIX_HW_FAN_MIN = "HW_FAN_MIN";
	final static String THRESHOLD_SUFFIX_HW_FAN_MAX = "HW_FAN_MAX";
	final static String THRESHOLD_SUFFIX_HW_ADCLOG = "HW_ADC_LOG";
	final static String THRESHOLD_SUFFIX_L47_SLEEP_VS_DAY = "L47_SLEEP_VS";
	final static String THRESHOLD_SUFFIX_SVC_CLIENT_IP = "SVC_CLIENT_IP";
	final static String THRESHOLD_SUFFIX_SVC_VS_INDEX = "SVC_VS_INDEX";
	final static String THRESHOLD_SUFFIX_SVC_VS_NAME = "SVC_VS_NAME";
	final static String THRESHOLD_SUFFIX_SVC_VS_IP = "SVC_VS_IP";

	final static int THRESHOLD_DEFAULT_HW_CPU = 80;
	final static int THRESHOLD_DEFAULT_HW_MEMORY = 85;
	final static int THRESHOLD_DEFAULT_HW_FAN_MIN = 3500;
	final static int THRESHOLD_DEFAULT_HW_FAN_MAX = 15000;
	final static int THRESHOLD_DEFAULT_HW_ADC_LOG = 10;
	final static int THRESHOLD_DEFAULT_L47_SLEEP_VS_DAY = 7;

	private String convertThresholdToString(OBDtoFaultCheckTemplate templateObj) throws OBException {
		int thresholdHWCpuUsage = THRESHOLD_DEFAULT_HW_CPU;
		int thresholdHWMemoryUsage = THRESHOLD_DEFAULT_HW_MEMORY;
		int thresholdHWFanMin = THRESHOLD_DEFAULT_HW_FAN_MIN;// min rpm.
		int thresholdHWFanMax = THRESHOLD_DEFAULT_HW_FAN_MAX;// max rpm.
		int thresholdHWAdcLogCount = THRESHOLD_DEFAULT_HW_ADC_LOG;
		int thresholdL47SleepVSDay = THRESHOLD_DEFAULT_L47_SLEEP_VS_DAY;// Virtual Server 유휴설정.

		if (templateObj.getThresholdHWCpuUsage() != null)
			thresholdHWCpuUsage = templateObj.getThresholdHWCpuUsage();
		if (templateObj.getThresholdHWMemoryUsage() != null)
			thresholdHWMemoryUsage = templateObj.getThresholdHWMemoryUsage();
		if (templateObj.getThresholdHWFanMin() != null)
			thresholdHWFanMin = templateObj.getThresholdHWFanMin();
		if (templateObj.getThresholdHWFanMax() != null)
			thresholdHWFanMax = templateObj.getThresholdHWFanMax();
		if (templateObj.getThresholdHWAdcLogCount() != null)
			thresholdHWAdcLogCount = templateObj.getThresholdHWAdcLogCount();
		if (templateObj.getThresholdL47SleepVSDay() != null)
			thresholdL47SleepVSDay = templateObj.getThresholdL47SleepVSDay();

		String retVal = String.format(" %s=%d %s=%d %s=%d %s=%d %s=%d %s=%d ", THRESHOLD_SUFFIX_HW_CPU,
				thresholdHWCpuUsage, THRESHOLD_SUFFIX_HW_MEMORY, thresholdHWMemoryUsage, THRESHOLD_SUFFIX_HW_FAN_MIN,
				thresholdHWFanMin, THRESHOLD_SUFFIX_HW_FAN_MAX, thresholdHWFanMax, THRESHOLD_SUFFIX_HW_ADCLOG,
				thresholdHWAdcLogCount, THRESHOLD_SUFFIX_L47_SLEEP_VS_DAY, thresholdL47SleepVSDay);
		if (templateObj.getSvcClientIPAddress() != null && !templateObj.getSvcClientIPAddress().isEmpty())
			retVal += String.format(" %s=%s ", THRESHOLD_SUFFIX_SVC_CLIENT_IP, templateObj.getSvcClientIPAddress());
		if (templateObj.getSvcVSIndex() != null && !templateObj.getSvcVSIndex().isEmpty())
			retVal += String.format(" %s=%s ", THRESHOLD_SUFFIX_SVC_VS_INDEX, templateObj.getSvcVSIndex());
		if (templateObj.getSvcVSIPAddress() != null && !templateObj.getSvcVSIPAddress().isEmpty())
			retVal += String.format(" %s=%s ", THRESHOLD_SUFFIX_SVC_VS_IP, templateObj.getSvcVSIPAddress());
		if (templateObj.getSvcVSName() != null && !templateObj.getSvcVSName().isEmpty())
			retVal += String.format(" %s=%s ", THRESHOLD_SUFFIX_SVC_VS_NAME, templateObj.getSvcVSName());

		return retVal;
	}

	private OBDtoFaultCheckTemplate convertThresholdToTemplate(String threshold) throws OBException {
		OBDtoFaultCheckTemplate retVal = new OBDtoFaultCheckTemplate();

		String elements[] = threshold.split(" ");

		for (String element : elements) {
			String keyValue[] = element.split("=");
			if (keyValue.length != 2)
				continue;
			if (keyValue[0].contains(THRESHOLD_SUFFIX_HW_CPU) == true) {
				retVal.setThresholdHWCpuUsage(Integer.parseInt(keyValue[1]));
				continue;
			}
			if (keyValue[0].contains(THRESHOLD_SUFFIX_HW_MEMORY) == true) {
				retVal.setThresholdHWMemoryUsage(Integer.parseInt(keyValue[1]));
				continue;
			}
			if (keyValue[0].contains(THRESHOLD_SUFFIX_HW_FAN_MIN) == true) {
				retVal.setThresholdHWFanMin(Integer.parseInt(keyValue[1]));
				continue;
			}
			if (keyValue[0].contains(THRESHOLD_SUFFIX_HW_FAN_MAX) == true) {
				retVal.setThresholdHWFanMax(Integer.parseInt(keyValue[1]));
				continue;
			}
			if (keyValue[0].contains(THRESHOLD_SUFFIX_HW_ADCLOG) == true) {
				retVal.setThresholdHWAdcLogCount(Integer.parseInt(keyValue[1]));
				continue;
			}
			if (keyValue[0].contains(THRESHOLD_SUFFIX_L47_SLEEP_VS_DAY) == true) {
				retVal.setThresholdL47SleepVSDay(Integer.parseInt(keyValue[1]));
				continue;
			}
			if (keyValue[0].contains(THRESHOLD_SUFFIX_SVC_CLIENT_IP) == true) {
				retVal.setSvcClientIPAddress(keyValue[1]);
				continue;
			}
			if (keyValue[0].contains(THRESHOLD_SUFFIX_SVC_VS_INDEX) == true) {
				retVal.setSvcVSIndex(keyValue[1]);
				continue;
			}
			if (keyValue[0].contains(THRESHOLD_SUFFIX_SVC_VS_NAME) == true) {
				retVal.setSvcVSName(keyValue[1]);
				continue;
			}
			if (keyValue[0].contains(THRESHOLD_SUFFIX_SVC_VS_IP) == true) {
				retVal.setSvcVSIPAddress(keyValue[1]);
				continue;
			}
		}

		return retVal;
	}

	private OBDtoFaultCheckItemCountInfo getCheckItemCountInfo(OBDtoFaultCheckTemplate templateObj) throws OBException {
		OBDtoFaultCheckItemCountInfo retVal = new OBDtoFaultCheckItemCountInfo();

		long hwItems = convertElementToLong(templateObj.getHwCheckItems());
		long l23Items = convertElementToLong(templateObj.getL23CheckItems());
		long l47Items = convertElementToLong(templateObj.getL47CheckItems());
		long svcItems = convertElementToLong(templateObj.getSvcCheckItems());

		retVal.setHwCheckTotal(
				getEnabledItemCount(hwItems) + getEnabledItemCount(l23Items) + getEnabledItemCount(l47Items));
		retVal.setSvcCheckTotal(getEnabledItemCount(svcItems));

		return retVal;
	}

	private Long saveFaultCheckTemplate(OBDtoFaultCheckTemplate templateObj, int state, int accntIndex, OBDatabase db)
			throws OBException {
		if (templateObj == null || db == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER);
		}

		Long retVal = templateObj.getIndex();

		String sqlText = "";

		try {
			long hwItems = convertElementToLong(templateObj.getHwCheckItems());
			long l23Items = convertElementToLong(templateObj.getL23CheckItems());
			long l47Items = convertElementToLong(templateObj.getL47CheckItems());
			long svcItems = templateObj.getSvcCheckFlg();// convertElementToLong(templateObj.getSvcCheckItems());
			// long svcItems = 0L;
			// if(templateObj.getSvcCheckFlg()==OBDefine.STATE_ENABLE)
			// svcItems = 0xFFFFFFFFFFFFFFFFL;
			String threshold = convertThresholdToString(templateObj);

			String time = OBDateTime.now();
			if (templateObj.getIndex() == 0)// 신규인경우. 신규추가 상태 또는 임시 정보 두가지 상태가 있음.
			{
				if (state == OBDefine.STATE_TEMPORARY) {
					sqlText = String.format(" INSERT INTO MNG_FAULT_TEMPLATE \n"
							+ " ( NAME, DESCRIPTION, STATE, OCCUR_TIME, ACCNT_INDEX, CHK_HW_ONOFF, CHK_L23_ONOFF, \n"
							+ "   CHK_L47_ONOFF, CHK_SVC_ONOFF, THRESHOLD		                                  ) \n"
							+ " VALUES                                                                            \n"
							+ " (%s, %s, %d, %s, %d, %d, %d, %d, %d, %s)                                          \n",
							OBParser.sqlString("temporary"), OBParser.sqlString("temporary template"), state,
							OBParser.sqlString(time), accntIndex, hwItems, l23Items, l47Items, svcItems,
							OBParser.sqlString(threshold));
					templateObj.setName("temporary");
				} else {
					sqlText = String.format(" INSERT INTO MNG_FAULT_TEMPLATE \n"
							+ " ( NAME, DESCRIPTION, STATE, OCCUR_TIME, ACCNT_INDEX, CHK_HW_ONOFF, CHK_L23_ONOFF, \n"
							+ "   CHK_L47_ONOFF, CHK_SVC_ONOFF, THRESHOLD		                                  ) \n"
							+ " VALUES                                                                            \n"
							+ " (%s, %s, %d, %s, %d, %d, %d, %d, %d, %s)                                          \n",
							OBParser.sqlString(templateObj.getName()), OBParser.sqlString(""), state,
							OBParser.sqlString(time), accntIndex, hwItems, l23Items, l47Items, svcItems,
							OBParser.sqlString(threshold));
				}
			} else {// 이미 있는 레코드인 경우. 업데이트함.
				sqlText = String.format(
						" UPDATE MNG_FAULT_TEMPLATE \n"
								+ " SET NAME=%s, DESCRIPTION=%s, STATE=%d, OCCUR_TIME=%s, ACCNT_INDEX=%d, \n"
								+ " CHK_HW_ONOFF=%d, CHK_L23_ONOFF=%d, CHK_L47_ONOFF=%d, CHK_SVC_ONOFF=%d,\n"
								+ " THRESHOLD=%s                                                          \n"
								+ " WHERE INDEX=%d                                                        \n",
						OBParser.sqlString(templateObj.getName()), OBParser.sqlString(""), state,
						OBParser.sqlString(time), accntIndex, hwItems, l23Items, l47Items, svcItems,
						OBParser.sqlString(threshold), templateObj.getIndex());
			}

			db.executeUpdate(sqlText);

			if (templateObj.getIndex() == 0)// 신규인경우에는 index를 추출하여 로컬 변수에 저장한다.
			{
				sqlText = String.format(
						" SELECT INDEX FROM MNG_FAULT_TEMPLATE             \n"
								+ " WHERE STATE=%d AND OCCUR_TIME=%s AND NAME=%s     \n"
								+ " ORDER BY OCCUR_TIME DESC                         \n"
								+ " LIMIT 1                                          \n",
						state, OBParser.sqlString(time), OBParser.sqlString(templateObj.getName()));
				ResultSet rs = db.executeQuery(sqlText);
				if (rs.next() == true) {
					retVal = db.getLong(rs, "INDEX");
				} else {// 위에서 저장했는데 조회 결과 없는 경우. 오류 상황.
					throw new OBException(OBException.ERRCODE_SYSTEM_DATA_NOT_EXIST);
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	@Override
	public OBDtoFaultCheckTemplate getFaultCheckTemplateInfo(Long templateIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultCheckTemplate retVal = new OBDtoFaultCheckTemplate();
		try {
			db.openDB();
			retVal = getFaultCheckTemplateInfo(templateIndex, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private OBDtoFaultCheckTemplate getFaultCheckDefaultTemplateInfo(OBDatabase db) throws OBException {
		try {
			OBDtoFaultCheckTemplate retVal = new OBDtoFaultCheckTemplate();
			retVal.setHwCheckItems(new OBFaultMngImpl().getElements(OBDefine.ELEMENT_TYPE_HW, db));
			retVal.setL23CheckItems(new OBFaultMngImpl().getElements(OBDefine.ELEMENT_TYPE_L23, db));
			retVal.setL47CheckItems(new OBFaultMngImpl().getElements(OBDefine.ELEMENT_TYPE_L47, db));
			retVal.setSvcCheckItems(new OBFaultMngImpl().getElements(OBDefine.ELEMENT_TYPE_SVC, db));
			retVal.setHwCheckItemCount(0);
			retVal.setL23CheckItemCount(0);
			retVal.setL47CheckItemCount(0);
			retVal.setSvcCheckItemCount(0);
			return retVal;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// System.out.println(new OBFaultMngImpl().getFaultCheckTemplateInfo(130L, db));
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public OBDtoFaultCheckTemplate getFaultCheckTemplateInfo(Long templateIndex, OBDatabase db) throws OBException {
		OBDtoFaultCheckTemplate retVal = new OBDtoFaultCheckTemplate();

		if (templateIndex == 0L)
			return getFaultCheckDefaultTemplateInfo(db);

		String sqlText = "";
		OBDatabase localDB = new OBDatabase();
		try {
			localDB.openDB();
			sqlText = String.format(
					" SELECT INDEX, NAME, DESCRIPTION, STATE, OCCUR_TIME, ACCNT_INDEX,     \n"
							+ " CHK_HW_ONOFF, CHK_L23_ONOFF, CHK_L47_ONOFF, CHK_SVC_ONOFF, THRESHOLD \n"
							+ " FROM MNG_FAULT_TEMPLATE                                              \n"
							+ " WHERE INDEX = %d                                                     \n",
					templateIndex);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal.setIndex(db.getLong(rs, "INDEX"));
				retVal.setName(db.getString(rs, "NAME"));
				long hwOnOff = db.getLong(rs, "CHK_HW_ONOFF");
				long l23OnOff = db.getLong(rs, "CHK_L23_ONOFF");
				long l47OnOff = db.getLong(rs, "CHK_L47_ONOFF");
				long svcOnOff = db.getLong(rs, "CHK_SVC_ONOFF");
				String threshold = db.getString(rs, "THRESHOLD");
				ArrayList<OBDtoElement> hwElementList = convertElementToArray(OBDefine.ELEMENT_TYPE_HW, hwOnOff,
						localDB);
				ArrayList<OBDtoElement> l23ElementList = convertElementToArray(OBDefine.ELEMENT_TYPE_L23, l23OnOff,
						localDB);
				ArrayList<OBDtoElement> l47EmentList = convertElementToArray(OBDefine.ELEMENT_TYPE_L47, l47OnOff,
						localDB);
				ArrayList<OBDtoElement> svcElementList = convertElementToArray(OBDefine.ELEMENT_TYPE_SVC, svcOnOff,
						localDB);

				OBDtoFaultCheckTemplate thresholdTemp = convertThresholdToTemplate(threshold);

				retVal.setSvcCheckFlg(1);
				if (svcOnOff == 0L) {
					retVal.setSvcCheckFlg(0);
				}
				retVal.setHwCheckItems(hwElementList);
				retVal.setL23CheckItems(l23ElementList);
				retVal.setL47CheckItems(l47EmentList);
				retVal.setSvcCheckItems(svcElementList);
				retVal.setHwCheckItemCount(getEnabledItemCount(hwOnOff));
				retVal.setL23CheckItemCount(getEnabledItemCount(l23OnOff));
				retVal.setL47CheckItemCount(getEnabledItemCount(l47OnOff));
				retVal.setSvcCheckItemCount(getEnabledItemCount(svcOnOff));

				if (thresholdTemp.getThresholdHWAdcLogCount() != null)
					retVal.setThresholdHWAdcLogCount(thresholdTemp.getThresholdHWAdcLogCount());
				if (thresholdTemp.getThresholdHWCpuUsage() != null)
					retVal.setThresholdHWCpuUsage(thresholdTemp.getThresholdHWCpuUsage());
				if (thresholdTemp.getThresholdHWFanMax() != null)
					retVal.setThresholdHWFanMax(thresholdTemp.getThresholdHWFanMax());
				if (thresholdTemp.getThresholdHWFanMin() != null)
					retVal.setThresholdHWFanMin(thresholdTemp.getThresholdHWFanMin());
				if (thresholdTemp.getThresholdHWMemoryUsage() != null)
					retVal.setThresholdHWMemoryUsage(thresholdTemp.getThresholdHWMemoryUsage());
				if (thresholdTemp.getThresholdL47SleepVSDay() != null)
					retVal.setThresholdL47SleepVSDay(thresholdTemp.getThresholdL47SleepVSDay());
				if (thresholdTemp.getSvcClientIPAddress() != null)
					retVal.setSvcClientIPAddress(thresholdTemp.getSvcClientIPAddress());
				if (thresholdTemp.getSvcVSIndex() != null)
					retVal.setSvcVSIndex(thresholdTemp.getSvcVSIndex());
				if (thresholdTemp.getSvcVSIPAddress() != null)
					retVal.setSvcVSIPAddress(thresholdTemp.getSvcVSIPAddress());
				if (thresholdTemp.getSvcVSName() != null)
					retVal.setSvcVSName(thresholdTemp.getSvcVSName());
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (localDB != null)
				localDB.closeDB();
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// ArrayList<OBDtoFaultCheckTemplate> retVal = new
	// OBFaultMngImpl().getFaultCheckTemplateList(null, null);
	// System.out.println(retVal);
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public ArrayList<OBDtoFaultCheckTemplate> getFaultCheckTemplateList(OBDtoSearch searchObj, OBDtoOrdering orderObj)
			throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoFaultCheckTemplate> retVal = new ArrayList<OBDtoFaultCheckTemplate>();
		try {
			db.openDB();
			retVal = getFaultCheckTemplateList(searchObj, orderObj, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private ArrayList<OBDtoFaultCheckTemplate> getFaultCheckTemplateList(OBDtoSearch searchObj, OBDtoOrdering orderObj,
			OBDatabase db) throws OBException {
		ArrayList<OBDtoFaultCheckTemplate> retVal = new ArrayList<OBDtoFaultCheckTemplate>();

		String sqlText = "";
		OBDatabase localDB = new OBDatabase();
		try {
			localDB.openDB();
			sqlText = String.format(
					" SELECT INDEX, NAME, DESCRIPTION, STATE, OCCUR_TIME, ACCNT_INDEX,     \n"
							+ " CHK_HW_ONOFF, CHK_L23_ONOFF, CHK_L47_ONOFF, CHK_SVC_ONOFF, THRESHOLD \n"
							+ " FROM MNG_FAULT_TEMPLATE                                              \n"
							+ " WHERE STATE=%d                                                       \n"
							+ " ORDER BY NAME ASC                                                    \n",
					OBDefine.STATE_ENABLE);

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoFaultCheckTemplate obj = new OBDtoFaultCheckTemplate();
				obj.setIndex(db.getLong(rs, "INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				long hwOnOff = db.getLong(rs, "CHK_HW_ONOFF");
				long l23OnOff = db.getLong(rs, "CHK_L23_ONOFF");
				long l47OnOff = db.getLong(rs, "CHK_L47_ONOFF");
				long svcOnOff = db.getLong(rs, "CHK_SVC_ONOFF");
				String threshold = db.getString(rs, "THRESHOLD");
				ArrayList<OBDtoElement> hwElementList = convertElementToArray(OBDefine.ELEMENT_TYPE_HW, hwOnOff,
						localDB);
				ArrayList<OBDtoElement> l23ElementList = convertElementToArray(OBDefine.ELEMENT_TYPE_L23, l23OnOff,
						localDB);
				ArrayList<OBDtoElement> l47EmentList = convertElementToArray(OBDefine.ELEMENT_TYPE_L47, l47OnOff,
						localDB);
				ArrayList<OBDtoElement> svcElementList = convertElementToArray(OBDefine.ELEMENT_TYPE_SVC, svcOnOff,
						localDB);

				OBDtoFaultCheckTemplate thresholdTemp = convertThresholdToTemplate(threshold);

				obj.setSvcCheckFlg(1);
				if (svcOnOff == 0L) {
					obj.setSvcCheckFlg(0);
				}
				obj.setHwCheckItems(hwElementList);
				obj.setL23CheckItems(l23ElementList);
				obj.setL47CheckItems(l47EmentList);
				obj.setSvcCheckItems(svcElementList);

				if (thresholdTemp.getThresholdHWAdcLogCount() != null)
					obj.setThresholdHWAdcLogCount(thresholdTemp.getThresholdHWAdcLogCount());
				if (thresholdTemp.getThresholdHWCpuUsage() != null)
					obj.setThresholdHWCpuUsage(thresholdTemp.getThresholdHWCpuUsage());
				if (thresholdTemp.getThresholdHWFanMax() != null)
					obj.setThresholdHWFanMax(thresholdTemp.getThresholdHWFanMax());
				if (thresholdTemp.getThresholdHWFanMin() != null)
					obj.setThresholdHWFanMin(thresholdTemp.getThresholdHWFanMin());
				if (thresholdTemp.getThresholdHWMemoryUsage() != null)
					obj.setThresholdHWMemoryUsage(thresholdTemp.getThresholdHWMemoryUsage());
				if (thresholdTemp.getThresholdL47SleepVSDay() != null)
					obj.setThresholdL47SleepVSDay(thresholdTemp.getThresholdL47SleepVSDay());
				if (thresholdTemp.getSvcClientIPAddress() != null)
					obj.setSvcClientIPAddress(thresholdTemp.getSvcClientIPAddress());
				if (thresholdTemp.getSvcVSIndex() != null)
					obj.setSvcVSIndex(thresholdTemp.getSvcVSIndex());
				if (thresholdTemp.getSvcVSIPAddress() != null)
					obj.setSvcVSIPAddress(thresholdTemp.getSvcVSIPAddress());
				if (thresholdTemp.getSvcVSName() != null)
					obj.setSvcVSName(thresholdTemp.getSvcVSName());
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (localDB != null)
				localDB.closeDB();
		}
		return retVal;
	}

	@Override
	public Integer getFaultCheckLogListTotalCount(OBDtoADCObject object, OBDtoSearch searchObj) throws OBException {
		OBDatabase db = new OBDatabase();
		Integer recordCount = 0;
		try {
			db.openDB();
			recordCount = getFaultCheckLogListTotalCount(object, searchObj, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}

		return recordCount;
	}

	public Integer getFaultCheckLogListTotalCount(OBDtoADCObject object, OBDtoSearch searchObj, OBDatabase db)
			throws OBException {
		Integer recordCount = 0;
		String sqlText = "";

		try {
			String sqlSearch = "";

			if (searchObj == null)
				throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "invalide param: searchObj");

			String sqlLimit = makeSqlLimitContext(searchObj);

			if (searchObj != null && searchObj.getSearchKey() != null && !searchObj.getSearchKey().isEmpty()) {
				// #3984-2 #1: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchObj.getSearchKey()) + "%";
				sqlSearch = String.format(" (A.MSG_SUMMARY LIKE %s OR A.ADC_NAME LIKE %s) ",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			String sqlTime = "";
			if (searchObj != null && searchObj.getToTime() == null)
				sqlTime = String.format(" OCCUR_TIME <= %s ", OBParser.sqlString(OBDateTime.now()));
			else
				sqlTime = String.format(" OCCUR_TIME <= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchObj.getToTime().getTime()))));

			if (searchObj != null && searchObj.getFromTime() != null)
				sqlTime += String.format(" AND OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchObj.getFromTime().getTime()))));

			String adcList = getInvolvedAdcIndexList(object, db);
			sqlText = String.format(
					" SELECT COUNT(*) CNT                               \n"
							+ " FROM LOG_FAULT 		A                           \n"
							+ " LEFT JOIN MNG_ACCNT 	B   						\n"
							+ " ON A.ACCNT_INDEX = B.INDEX                        \n"
							+ " WHERE A.ADC_INDEX IN ( %s )                       \n"
							+ " AND A.AVAILABLE = %d  							\n",
					adcList, OBDefine.ADC_STATE.AVAILABLE);

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			if (!sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			sqlText += sqlLimit;

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				recordCount = db.getInteger(rs, "CNT");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return recordCount;
	}

	@Override
	public ArrayList<OBDtoFaultCheckLog> getFaultCheckLogList(OBDtoADCObject object, OBDtoSearch searchObj,
			OBDtoOrdering orderObj) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoFaultCheckLog> retVal = null;
		try {
			db.openDB();
			retVal = getFaultCheckLogList(object, searchObj, orderObj, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private String getPktdumpInfoListOrderType(OBDtoOrdering orderObj) throws OBException {
		if (orderObj == null)
			return "";
		String retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST ";
		int orderDir = orderObj.getOrderDirection();
		switch (orderObj.getOrderType()) {
		case OBDefine.ORDER_TYPE_FIRST:// OCCUR_TIME
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY A.OCCUR_TIME ASC NULLS LAST, A.STATUS ASC NULLS LAST ";
			else
				retVal = " ORDER BY A.OCCUR_TIME DESC NULLS LAST, A.STATUS ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SECOND:// FILE_NAME
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY FILE_NAME ASC NULLS LAST, A.OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY FILE_NAME DESC NULLS LAST, A.OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_THIRD:// FILE_SIZE
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY FILE_SIZE ASC NULLS LAST, A.OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY FILE_SIZE DESC NULLS LAST, A.OCCUR_TIME DESC NULLS LAST ";
			break;
		}
		return retVal;
	}

	private String getFaultCheckLogListOrderType(OBDtoOrdering orderObj) throws OBException {
		if (orderObj == null)
			return "";
		String retVal = " ORDER BY A.OCCUR_TIME DESC NULLS LAST, A.STATUS_TOTAL ASC NULLS LAST ";
		int orderDir = orderObj.getOrderDirection();
		switch (orderObj.getOrderType()) {
		case OBDefine.ORDER_TYPE_FIRST:// OCCUR_TIME
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY A.OCCUR_TIME ASC NULLS LAST, A.STATUS_TOTAL ASC NULLS LAST ";
			else
				retVal = " ORDER BY A.OCCUR_TIME DESC NULLS LAST, A.STATUS_TOTAL ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SECOND:// STATUS
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY STATUS_TOTAL ASC NULLS LAST, A.OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY STATUS_TOTAL DESC NULLS LAST, A.OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_THIRD:// 대상 ADC, ADC_NAME
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY ADC_NAME ASC NULLS LAST, A.OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY ADC_NAME DESC NULLS LAST, A.OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_FOURTH: // 사용자. ACCNT_NAME
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY ACCNT_NAME ASC NULLS LAST, A.OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY ACCNT_NAME DESC NULLS LAST, A.OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_FIFTH: // 요약 , 진단결과 : 정상,비정상
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY MSG_SUMMARY ASC NULLS LAST, A.OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY MSG_SUMMARY DESC NULLS LAST, A.OCCUR_TIME DESC NULLS LAST ";
			break;
		}
		return retVal;
	}

	private String getInvolvedAdcIndexList(OBDtoADCObject object, OBDatabase db) throws OBException {
		String retVal = "";
		if (object == null)
			return retVal;
		if (object.getCategory() == OBDtoADCObject.CATEGORY_ADC) {
			retVal = object.getIndex().toString();
			return retVal;
		}
		if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
			ArrayList<Integer> adcIndexList = getAdcIndexListInGroup(0, db);
			for (Integer index : adcIndexList) {
				if (!retVal.isEmpty())
					retVal += ", ";
				retVal += index.toString();
			}
			return retVal;
		}
		if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
			ArrayList<Integer> adcIndexList = getAdcIndexListInGroup(object.getIndex(), db);
			for (Integer index : adcIndexList) {
				if (!retVal.isEmpty())
					retVal += ", ";
				retVal += index.toString();
			}
			return retVal;
		}
		return retVal;
	}

	// group에 속한 ADC index 목록만 뽑는다. getAdcIndexListInGroup()은 DB연결도 두개고, 조회에 join이
	// 있고, 데이터도 무겁다. 인덱스만 필요할 때 이 함수로 쓴다.
	private ArrayList<Integer> getAdcIndexListInGroup(int groupIndex, OBDatabase db) throws OBException {
		ArrayList<Integer> retVal = new ArrayList<Integer>();

		String sqlText = "";
		ResultSet rs;

		try {
			if (groupIndex == 0) {
				sqlText = String.format(" SELECT INDEX " + " FROM MNG_ADC " + " WHERE AVAILABLE = %d ", // where-in:empty
																										// string 불가,
																										// null 불가, OK
						OBDefine.ADC_STATE.AVAILABLE);
			} else {
				sqlText = String.format(
						" SELECT INDEX " + " FROM MNG_ADC " + " WHERE AVAILABLE = %d AND GROUP_INDEX = %d ", // where-in:empty
																												// string
																												// 불가,
																												// null
																												// 불가,
																												// OK
						OBDefine.ADC_STATE.AVAILABLE, groupIndex);
			}

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				retVal.add(db.getInteger(rs, "INDEX"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	private ArrayList<OBDtoFaultCheckLog> getFaultCheckLogList(OBDtoADCObject object, OBDtoSearch searchObj,
			OBDtoOrdering orderObj, OBDatabase db) throws OBException {
		String sqlText = "";
		ArrayList<OBDtoFaultCheckLog> retVal = new ArrayList<OBDtoFaultCheckLog>();
		try {
			String sqlSearch = "";
			if (searchObj == null)
				throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "invalide param: searchObj");

			String sqlLimit = makeSqlLimitContext(searchObj);

			if (searchObj != null && searchObj.getSearchKey() != null && !searchObj.getSearchKey().isEmpty()) {
				// #3984-2 #1: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchObj.getSearchKey()) + "%";
				sqlSearch = String.format(" (A.MSG_SUMMARY LIKE %s OR A.ADC_NAME LIKE %s) ",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			String sqlTime = "";
			if (searchObj != null && searchObj.getToTime() == null)
				sqlTime = String.format(" OCCUR_TIME <= %s ", OBParser.sqlString(OBDateTime.now()));
			else {
				sqlTime = String.format(" OCCUR_TIME <= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchObj.getToTime().getTime()))));
			}
			if (searchObj != null && searchObj.getFromTime() != null)
				sqlTime += String.format(" AND OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchObj.getFromTime().getTime()))));

			String adcList = getInvolvedAdcIndexList(object, db);
			sqlText = String.format(
					" SELECT A.INDEX, A.LOG_KEY, A.OCCUR_TIME AS OCCUR_TIME, A.STATUS_TOTAL, A.TEMPLATE_INDEX,                  \n"
							+ " A.ELAPSE_TIME, A.ADC_INDEX, A.ADC_NAME AS ADC_NAME, A.ACCNT_INDEX, A.ITEM_TYPE, A.MSG_SUMMARY,        \n"
							+ " B.ID AS ACCNT_NAME                                   \n"
							+ " FROM LOG_FAULT      A                                  \n"
							+ " LEFT JOIN MNG_ACCNT B				      	  			 \n"
							+ " ON A.ACCNT_INDEX = B.INDEX                             \n"
							+ " WHERE A.ADC_INDEX IN ( %s )                            \n"
							+ " AND A.AVAILABLE = %d  								 \n",
					adcList, OBDefine.ADC_STATE.AVAILABLE);

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			if (!sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;
			sqlText += getFaultCheckLogListOrderType(orderObj);
			sqlText += sqlLimit;

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoFaultCheckLog obj = new OBDtoFaultCheckLog();
				obj.setIndex(db.getLong(rs, "INDEX"));
				obj.setLogKey(db.getLong(rs, "LOG_KEY"));
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setStatus(db.getString(rs, "STATUS_TOTAL"));
				obj.setElapseTime(db.getInteger(rs, "ELAPSE_TIME"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setAdcName(db.getString(rs, "ADC_NAME"));
				obj.setAccntIndex(db.getInteger(rs, "ACCNT_INDEX"));
				obj.setAccntName(db.getString(rs, "ACCNT_NAME"));
				obj.setCheckItem(db.getInteger(rs, "ITEM_TYPE"));
				obj.setSummary(db.getString(rs, "MSG_SUMMARY"));
				obj.setTemplateIndex(db.getLong(rs, "TEMPLATE_INDEX"));

				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoADCObject obj = new OBDtoADCObject();
	// obj.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// obj.setIndex(3);
	// ArrayList<OBDtoCpuMemStatus> list = new
	// OBFaultMngImpl().getFaultAdcCpuMemoryHistory(3582054056353L);
	// System.out.println(list);
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public ArrayList<OBDtoCpuMemStatus> getFaultAdcCpuMemoryHistory(Long logKey) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoCpuMemStatus> retVal = null;
		try {
			db.openDB();
			OBDtoFaultCheckLog summLog = getFaultCheckLogInfo(logKey, db);

			Timestamp endTime = new Timestamp(summLog.getOccurTime().getTime() + summLog.getElapseTime());
			retVal = getFaultAdcCpuMemoryHistory(summLog.getAdcIndex(), null, endTime, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private ArrayList<OBDtoCpuMemStatus> getFaultAdcCpuMemoryHistory(Integer adcIndex, Timestamp startTime,
			Timestamp endTime, OBDatabase db) throws OBException {
		String sqlText = "";
		ArrayList<OBDtoCpuMemStatus> retVal = new ArrayList<OBDtoCpuMemStatus>();
		try {
			String sqlTime = makeTimeSqlText(endTime, "OCCUR_TIME");
			sqlText = String.format(
					" SELECT INDEX, OCCUR_TIME, MEM_USAGE,                                         \n"
							+ " CPU1_USAGE, CPU2_USAGE, CPU3_USAGE, CPU4_USAGE, CPU5_USAGE,                  \n"
							+ " CPU6_USAGE, CPU7_USAGE, CPU8_USAGE, CPU9_USAGE, CPU10_USAGE,                 \n"
							+ " CPU11_USAGE, CPU12_USAGE, CPU13_USAGE, CPU14_USAGE, CPU15_USAGE, CPU16_USAGE \n"
							+ " FROM LOG_RESC_CPUMEM                                                   \n"
							+ " WHERE ADC_INDEX = %d AND  %s                                                 \n"
							+ " ORDER BY OCCUR_TIME ASC                                                      \n",
					adcIndex, sqlTime);

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoCpuMemStatus obj = new OBDtoCpuMemStatus();
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setMemUsage(db.getInteger(rs, "MEM_USAGE"));
				obj.setCpu1Usage(db.getInteger(rs, "CPU1_USAGE"));
				obj.setCpu2Usage(db.getInteger(rs, "CPU2_USAGE"));
				obj.setCpu3Usage(db.getInteger(rs, "CPU3_USAGE"));
				obj.setCpu4Usage(db.getInteger(rs, "CPU4_USAGE"));
				obj.setCpu5Usage(db.getInteger(rs, "CPU5_USAGE"));
				obj.setCpu6Usage(db.getInteger(rs, "CPU6_USAGE"));
				obj.setCpu7Usage(db.getInteger(rs, "CPU7_USAGE"));
				obj.setCpu8Usage(db.getInteger(rs, "CPU8_USAGE"));
				obj.setCpu9Usage(db.getInteger(rs, "CPU9_USAGE"));
				obj.setCpu10Usage(db.getInteger(rs, "CPU10_USAGE"));
				obj.setCpu11Usage(db.getInteger(rs, "CPU11_USAGE"));
				obj.setCpu12Usage(db.getInteger(rs, "CPU12_USAGE"));
				obj.setCpu13Usage(db.getInteger(rs, "CPU13_USAGE"));
				obj.setCpu14Usage(db.getInteger(rs, "CPU14_USAGE"));
				obj.setCpu15Usage(db.getInteger(rs, "CPU15_USAGE"));
				obj.setCpu16Usage(db.getInteger(rs, "CPU16_USAGE"));
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoADCObject obj = new OBDtoADCObject();
	// obj.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// obj.setIndex(1);
	// OBDtoCpuMemStatus list = new
	// OBFaultMngImpl().getFaultAdcCpuMemoryUsage1Sec(obj, db);
	// System.out.println(list);
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 실시간적으로 CPU/Memory 정보를 추출한다.
	 */
	@Override
	public OBDtoCpuMemStatus getFaultAdcCpuMemoryUsage(OBDtoADCObject object) throws OBException {
		OBDtoCpuMemStatus retVal = null;
		try {
			retVal = getFaultAdcCpuMemoryUsage1Sec(object);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}

		return retVal;
	}

	public OBDtoCpuMemStatus getFaultAdcCpuMemoryUsage1Sec(OBDtoADCObject object) throws OBException {
		OBDtoCpuMemStatus retVal = new OBDtoCpuMemStatus();
		if (object.getCategory() != OBDtoADCObject.CATEGORY_ADC)
			return retVal;
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());
			if (adcInfo == null) {
				return retVal;
			}
			DtoOidFaultCheckHW snmpOidInfoHW = new OBSnmpOidDB().getFaultCheckHWInfo(adcInfo.getAdcType(),
					adcInfo.getSwVersion());

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
				return getFaultAdcCpuMemoryUsageAlteon1Sec(snmpOidInfoHW, adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5)
				return getFaultAdcCpuMemoryUsageF51Sec(snmpOidInfoHW, adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS)
				return getFaultAdcCpuMemoryUsagePAS1Sec(snmpOidInfoHW, adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK)
				return getFaultAdcCpuMemoryUsagePASK1Sec(snmpOidInfoHW, adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to get cpu/memory info");
		}
		return retVal;
	}

	private OBDtoCpuMemStatus getFaultAdcCpuMemoryUsageAlteon1Sec(DtoOidFaultCheckHW snmpOidInfoHW, String ipAddress,
			OBDtoAdcSnmpInfo snmpInfo) throws OBException {
		OBDtoCpuMemStatus retVal = new OBDtoCpuMemStatus();
		retVal.setOccurTime(OBDateTime.toTimestamp(OBDateTime.now()));

		OBSnmp ClassSnmp = new OBSnmpAlteon(ipAddress, snmpInfo);
		List<VariableBinding> tmpList = null;
		try {
			// memory usage;
			String oid = snmpOidInfoHW.getMpMemStatsTotal();
			tmpList = ClassSnmp.walk(oid);
			Long totalValue = 0L;
			if (tmpList != null && tmpList.size() > 0) {
				VariableBinding var = tmpList.get(0);
				totalValue = var.getVariable().toLong();//
			}
			oid = snmpOidInfoHW.getMpMemStatsFree();
			tmpList = ClassSnmp.walk(oid);
			Long freeValue = 0L;
			if (tmpList != null && tmpList.size() > 0) {
				VariableBinding var = tmpList.get(0);
				freeValue = var.getVariable().toLong();//
			}

			Long memUsage = 0L;
			if (totalValue.longValue() != 0)
				memUsage = 100L - (freeValue.longValue() * 100L / totalValue.longValue());
			retVal.setMemUsage(memUsage.intValue());

			// mp usage
			oid = snmpOidInfoHW.getMpCpuStats1Sec();
			tmpList = ClassSnmp.walk(oid);
			if (tmpList != null && tmpList.size() > 0) {
				VariableBinding var = tmpList.get(0);
				retVal.setCpu1Usage(var.getVariable().toInt());
			}

			// sp usage
			oid = snmpOidInfoHW.getSpCpuStats1Sec();
			tmpList = ClassSnmp.walk(oid);
			for (VariableBinding var : tmpList) {
				ArrayList<Integer> oidList = ClassSnmp.parseOid(var.getOid().toString(), oid);
				Integer spIndex = oidList.get(0);
				Integer spValue = var.getVariable().toInt();
				if (spIndex.intValue() == 1)
					retVal.setCpu2Usage(spValue);
				else if (spIndex.intValue() == 2)
					retVal.setCpu3Usage(spValue);
				else if (spIndex.intValue() == 3)
					retVal.setCpu4Usage(spValue);
				else if (spIndex.intValue() == 4)
					retVal.setCpu5Usage(spValue);
				else if (spIndex.intValue() == 5)
					retVal.setCpu6Usage(spValue);
				else if (spIndex.intValue() == 6)
					retVal.setCpu7Usage(spValue);
				else if (spIndex.intValue() == 7)
					retVal.setCpu8Usage(spValue);
				else if (spIndex.intValue() == 8)
					retVal.setCpu9Usage(spValue);
				else if (spIndex.intValue() == 9)
					retVal.setCpu10Usage(spValue);
				else if (spIndex.intValue() == 10)
					retVal.setCpu11Usage(spValue);
				else if (spIndex.intValue() == 11)
					retVal.setCpu12Usage(spValue);
				else if (spIndex.intValue() == 12)
					retVal.setCpu13Usage(spValue);
				else if (spIndex.intValue() == 13)
					retVal.setCpu14Usage(spValue);
				else if (spIndex.intValue() == 14)
					retVal.setCpu15Usage(spValue);
				else if (spIndex.intValue() == 15)
					retVal.setCpu16Usage(spValue);
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to query snmp");
		}

		return retVal;
	}

	private OBDtoCpuMemStatus getFaultAdcCpuMemoryUsageF51Sec(DtoOidFaultCheckHW snmpOidInfoHW, String ipAddress,
			OBDtoAdcSnmpInfo snmpInfo) throws OBException {
		OBDtoCpuMemStatus retVal = new OBDtoCpuMemStatus();
		retVal.setOccurTime(OBDateTime.toTimestamp(OBDateTime.now()));

		OBSnmp ClassSnmp = new OBSnmpF5(ipAddress, snmpInfo);
		List<VariableBinding> tmpList = null;
		try {
			// memory usage;
			String oid = snmpOidInfoHW.getMpMemStatsTotal();
			tmpList = ClassSnmp.walk(oid);
			Long totalValue = 0L;
			if (tmpList != null && tmpList.size() > 0) {
				VariableBinding var = tmpList.get(0);
				totalValue = var.getVariable().toLong();//
			}
			oid = snmpOidInfoHW.getMpMemStatsFree();
			tmpList = ClassSnmp.walk(oid);
			Long usedValue = 0L;
			if (tmpList != null && tmpList.size() > 0) {
				VariableBinding var = tmpList.get(0);
				usedValue = var.getVariable().toLong();//
			}

			Long memUsage = 0L;
			if (totalValue.longValue() != 0)
				memUsage = usedValue.longValue() * 100L / totalValue.longValue();
			retVal.setMemUsage(memUsage.intValue());

			// // mp usage
			// oid = snmpOidInfoHW.getMpCpuStats1Sec();
			// tmpList = ClassSnmp.walk(oid, community);
			// if(tmpList!=null && tmpList.size()>0)
			// {
			// VariableBinding var = tmpList.get(0);
			// retVal.setCpu1Usage(var.getVariable().toInt());
			// }

			// usage
			oid = snmpOidInfoHW.getMpCpuStats1Sec();
			tmpList = ClassSnmp.walk(oid);

			for (VariableBinding var : tmpList) {
				ArrayList<Integer> oidList = ClassSnmp.parseOid(var.getOid().toString(), oid);
				Integer spIndex = oidList.get(2) - 1;
				Integer spValue = var.getVariable().toInt();
				if (spIndex.intValue() == 0)
					retVal.setCpu1Usage(spValue);
				else if (spIndex.intValue() == 1)
					retVal.setCpu2Usage(spValue);
				else if (spIndex.intValue() == 2)
					retVal.setCpu3Usage(spValue);
				else if (spIndex.intValue() == 3)
					retVal.setCpu4Usage(spValue);
				else if (spIndex.intValue() == 4)
					retVal.setCpu5Usage(spValue);
				else if (spIndex.intValue() == 5)
					retVal.setCpu6Usage(spValue);
				else if (spIndex.intValue() == 6)
					retVal.setCpu7Usage(spValue);
				else if (spIndex.intValue() == 7)
					retVal.setCpu8Usage(spValue);
				else if (spIndex.intValue() == 8)
					retVal.setCpu9Usage(spValue);
				else if (spIndex.intValue() == 9)
					retVal.setCpu10Usage(spValue);
				else if (spIndex.intValue() == 10)
					retVal.setCpu11Usage(spValue);
				else if (spIndex.intValue() == 11)
					retVal.setCpu12Usage(spValue);
				else if (spIndex.intValue() == 12)
					retVal.setCpu13Usage(spValue);
				else if (spIndex.intValue() == 13)
					retVal.setCpu14Usage(spValue);
				else if (spIndex.intValue() == 14)
					retVal.setCpu15Usage(spValue);
				else if (spIndex.intValue() == 15)
					retVal.setCpu16Usage(spValue);
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to query snmp");
		}

		return retVal;
	}

	private OBDtoCpuMemStatus getFaultAdcCpuMemoryUsagePAS1Sec(DtoOidFaultCheckHW snmpOidInfoHW, String ipAddress,
			OBDtoAdcSnmpInfo snmpInfo) throws OBException {
		OBDtoCpuMemStatus retVal = new OBDtoCpuMemStatus();
		// TODO
		return retVal;
	}

	private OBDtoCpuMemStatus getFaultAdcCpuMemoryUsagePASK1Sec(DtoOidFaultCheckHW snmpOidInfoHW, String ipAddress,
			OBDtoAdcSnmpInfo snmpInfo) throws OBException {
		OBDtoCpuMemStatus retVal = new OBDtoCpuMemStatus();
		// TODO

		return retVal;
	}

	@Override
	public OBDtoCpuMemStatus getFaultAdcCpuMemoryUsageFromDB(OBDtoADCObject object) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoCpuMemStatus retVal = null;
		try {
			db.openDB();
			// DB에서 가져와서 제공하도록 한다.
			retVal = getLastCpuMemoryUsage1SecWithin10Sec(object, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	public OBDtoCpuMemStatus getLastCpuMemoryUsage1SecWithin10Sec(OBDtoADCObject object, OBDatabase db)
			throws OBException {
		OBDtoCpuMemStatus retVal = new OBDtoCpuMemStatus();
		if (object.getCategory() != OBDtoADCObject.CATEGORY_ADC)
			return retVal;
		try {
			retVal = new OBFaultMonitoringDB().getADCMonCPUMemoryLastInfoWithin10Sec(object, db);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to get cpu/memory info");
		}
		return retVal;
	}

	@Override
	public OBDtoFaultCheckStatus getFaultCheckStatus(Long logKey) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultCheckStatus retVal = null;
		try {
			db.openDB();
			retVal = getFaultCheckStatus(logKey, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	public OBDtoFaultCheckStatus getFaultCheckStatus(Long logKey, OBDatabase db) throws OBException {
		String sqlText = "";
		OBDtoFaultCheckStatus retVal = new OBDtoFaultCheckStatus();
		try {
			sqlText = String.format(" SELECT INDEX, LOG_KEY, OCCUR_TIME, STATUS_TOTAL,          \n"
					+ " ELAPSE_TIME, MSG_CUR_TITLE, MSG_CUR_RESULT,       \n"
					+ " ITEM_HW_TOTAL, STATUS_HW_CHECK, STATUS_HW_FAIL, STATUS_SVC \n"
					+ " FROM LOG_FAULT                                      \n"
					+ " WHERE LOG_KEY = %d LIMIT 1                          \n", logKey);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal.setCheckKey(logKey);
				Timestamp startTime = db.getTimestamp(rs, "OCCUR_TIME");
				retVal.setCliApiMessage(db.getString(rs, "MSG_CUR_RESULT"));
				retVal.setCurrentCheckItem(db.getString(rs, "MSG_CUR_TITLE"));
				retVal.setElapsedTime(db.getInteger(rs, "ELAPSE_TIME"));
				if (retVal.getElapsedTime() == null || retVal.getElapsedTime() == 0) {
					Timestamp occurTime = OBDateTime.toTimestamp(OBDateTime.now());
					Integer diffTime = (int) ((Math.abs(occurTime.getTime() - startTime.getTime())));
					retVal.setElapsedTime(diffTime);
				}
				int procRate = db.getInteger(rs, "STATUS_TOTAL");
				// if(procRate > OBDefine.FAULT_CHECK_STATUS_NORMAL)
				// procRate = 0;
				retVal.setProgressRate(procRate);
				retVal.setSvcCheckRate(db.getInteger(rs, "STATUS_SVC"));
				retVal.setHwCheckCompleteCount(db.getInteger(rs, "STATUS_HW_CHECK"));
				retVal.setHwCheckTotal(db.getInteger(rs, "ITEM_HW_TOTAL"));
				retVal.setHwCheckFailCount(db.getInteger(rs, "STATUS_HW_FAIL"));
				retVal.setHwCheckSuccCount(retVal.getHwCheckCompleteCount() - retVal.getHwCheckFailCount());
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	private HashMap<String, OBDtoFaultSolutionInfo> getSolutionInfo(OBDatabase db) throws OBException {
		String sqlText = "";
		HashMap<String, OBDtoFaultSolutionInfo> retVal = new HashMap<String, OBDtoFaultSolutionInfo>();
		try {
			sqlText = String.format(
					" SELECT ITEM_CATEGORY, ITEM_INDEX, DESCRIPTION       \n"
							+ " FROM %s                                             \n",
					OBCommon.makeProperTableName("MNG_FAULT_SOLUTION"));

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoFaultSolutionInfo obj = new OBDtoFaultSolutionInfo();
				obj.setCategory(db.getInteger(rs, "ITEM_CATEGORY"));
				obj.setIndex(db.getInteger(rs, "ITEM_INDEX"));
				obj.setDescription(db.getString(rs, "DESCRIPTION"));
				String hashKey = obj.getCategory() + "_" + obj.getIndex();
				retVal.put(hashKey, obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoADCObject obj = new OBDtoADCObject();
	// obj.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// obj.setIndex(3);
	// OBDtoFaultCheckResult list = new
	// OBFaultMngImpl().getFaultCheckLogDetail(3584397797636L, db);
	// System.out.println(list);
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public OBDtoFaultCheckResult getFaultCheckLogDetail(Long checkKey) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultCheckResult retVal = null;
		try {
			db.openDB();
			retVal = getFaultCheckLogDetail(checkKey, db);
			cleanUpPacketLossInfoImgFiles();
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private void cleanUpPacketLossInfoImgFiles() throws OBException {// 원본 파일을 ./img 폴더로 복사 후 복사한 img 파일 이름을 리턴한다.
		String targetFile = OBDefine.WEB_BASE_DIR + "imgs/" + "*.png";
		String cmnd = String.format("/bin/rm -rf %s", targetFile);
		try {
			Runtime.getRuntime().exec(cmnd).waitFor();
		} catch (Exception e) {
		}
	}

	private OBDtoFaultCheckResult getFaultCheckLogDetail(Long checkKey, OBDatabase db) throws OBException {
		OBDtoFaultCheckResult retVal = new OBDtoFaultCheckResult();
		retVal.setCheckKey(checkKey);

		try {
			OBDtoFaultCheckLog summaryLog = getFaultCheckLogInfo(checkKey, db);

			HashMap<String, OBDtoFaultSolutionInfo> solutionMap = getSolutionInfo(db);
			retVal.setHwResults(getFaultCheckLogDetailHW(checkKey, solutionMap, db));
			retVal.setL23Results(getFaultCheckLogDetailL23(checkKey, solutionMap, db));
			retVal.setL47Results(getFaultCheckLogDetailL47(checkKey, solutionMap, db));
			retVal.setSvcResults(getFaultCheckLogDetailSVC(checkKey, solutionMap, db));

			retVal.setVsvcName(summaryLog.getVsvcName());
			retVal.setVsvcIPAddress(summaryLog.getVsvcIPAddress());
			retVal.setVsvcPort(summaryLog.getVsvcPort());

			Date startTime = new Date(summaryLog.getOccurTime().getTime());
			retVal.setStartTime(startTime);
			Date endTime = new Date(summaryLog.getOccurTime().getTime() + summaryLog.getElapseTime());
			retVal.setEndTime(endTime);
			retVal.setElapseTime(summaryLog.getElapseTime() / 1000);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}

	private ArrayList<OBDtoFaultCheckResultElement> getFaultCheckLogDetailHW(Long checkKey,
			HashMap<String, OBDtoFaultSolutionInfo> solutionMap, OBDatabase db) throws OBException {
		String sqlText = "";
		ArrayList<OBDtoFaultCheckResultElement> retVal = new ArrayList<OBDtoFaultCheckResultElement>();
		try {
			HashMap<String, OBDtoElement> elementMap = getElementsToMap(OBDefine.ELEMENT_TYPE_HW, db);
			HashMap<String, OBDtoFaultCheckResultElement> logMap = new HashMap<String, OBDtoFaultCheckResultElement>();

			sqlText = String.format(
					" SELECT CATEGORY, TYPE, START_TIME, END_TIME, STATUS, CONTENT, DETAIL \n"
							+ " FROM LOG_FAULT_SUMMARY                                               \n"
							+ " WHERE LOG_KEY = %d AND CATEGORY = %d                                 \n"
							+ " ORDER BY START_TIME DESC                                             \n",
					checkKey, OBDefine.ELEMENT_TYPE_HW);

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				int iType = db.getInteger(rs, "TYPE");
				String hashKey = OBDefine.ELEMENT_TYPE_HW + "_" + iType;
				OBDtoFaultCheckResultElement resultElement = logMap.get(hashKey);
				if (resultElement != null) {
					OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
					content.setDetail(db.getString(rs, "DETAIL"));
					content.setSummary(db.getString(rs, "CONTENT"));
					resultElement.getResultList().add(content);
				} else {
					OBDtoElement obj = elementMap.get(hashKey);
					if (obj != null) {
						resultElement = new OBDtoFaultCheckResultElement();
						resultElement.setObj(obj);
						ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
						OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
						content.setDetail(db.getString(rs, "DETAIL"));
						content.setSummary(db.getString(rs, "CONTENT"));
						resultList.add(content);
						resultElement.setResultList(resultList);
						OBDtoFaultSolutionInfo solutionInfo = solutionMap.get(hashKey);
						if (solutionInfo != null)
							resultElement.setSolution(solutionInfo.getDescription());
						resultElement.setStartTime(db.getTimestamp(rs, "START_TIME"));
						resultElement.setEndTime(db.getTimestamp(rs, "END_TIME"));
						resultElement.setStatus(db.getInteger(rs, "STATUS"));

						logMap.put(hashKey, resultElement);
					}
				}
			}

			OBDtoFaultCheckResultElement result;

			// os 처리.
			String hashKey = OBDefine.ELEMENT_TYPE_HW + "_" + OBDefine.FAULT_CHECK_ELEMENT_HW_OSINFO;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			// 결과물을 생성한다.
			// power supply 처리.
			hashKey = OBDefine.ELEMENT_TYPE_HW + "_" + OBDefine.FAULT_CHECK_ELEMENT_HW_POWER;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			// uptime 처리.
			hashKey = OBDefine.ELEMENT_TYPE_HW + "_" + OBDefine.FAULT_CHECK_ELEMENT_HW_UPTIME;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			// license 처리.
			hashKey = OBDefine.ELEMENT_TYPE_HW + "_" + OBDefine.FAULT_CHECK_ELEMENT_HW_LICENSE;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			// interface 처리.
			hashKey = OBDefine.ELEMENT_TYPE_HW + "_" + OBDefine.FAULT_CHECK_ELEMENT_HW_INTERFACE;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			// cpu 처리.
			hashKey = OBDefine.ELEMENT_TYPE_HW + "_" + OBDefine.FAULT_CHECK_ELEMENT_HW_CPU;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			// memory 처리.
			hashKey = OBDefine.ELEMENT_TYPE_HW + "_" + OBDefine.FAULT_CHECK_ELEMENT_HW_MEMORY;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			// temperature 처리.
			hashKey = OBDefine.ELEMENT_TYPE_HW + "_" + OBDefine.FAULT_CHECK_ELEMENT_HW_TEMPERATURE;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			// fan 처리.
			hashKey = OBDefine.ELEMENT_TYPE_HW + "_" + OBDefine.FAULT_CHECK_ELEMENT_HW_FAN;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			// adc log 처리.
			hashKey = OBDefine.ELEMENT_TYPE_HW + "_" + OBDefine.FAULT_CHECK_ELEMENT_HW_ADCLOG;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	// private ArrayList<OBDtoFaultCheckResultElement> getFaultCheckLogDetailHW(Long
	// checkKey, HashMap<String, OBDtoFaultSolutionInfo> solutionMap, OBDatabase db)
	// throws OBException
	// {
	// String sqlText="";
	// ArrayList<OBDtoFaultCheckResultElement> retVal = new
	// ArrayList<OBDtoFaultCheckResultElement>();
	// try
	// {
	// HashMap<String, OBDtoElement> elementMap =
	// getElementsToMap(OBDefine.ELEMENT_TYPE_HW, db);
	//
	// sqlText = String.format(" SELECT RES_POWER, RES_POWER_SUMMARY,
	// RES_POWER_DETAIL, \n" +
	// " RES_UPTIME, RES_UPTIME_SUMMARY, RES_UPTIME_DETAIL, \n" +
	// " RES_LICENSE, RES_LICENSE_SUMMARY, RES_LICENSE_DETAIL, \n" +
	// " RES_INTERFACE, RES_INTERFACE_SUMMARY, RES_INTERFACE_DETAIL, \n" +
	// " RES_CPU, RES_CPU_SUMMARY, RES_CPU_DETAIL, \n" +
	// " RES_MEMORY, RES_MEMORY_SUMMARY, RES_MEMORY_DETAIL, \n" +
	// " RES_TEMPERATURE, RES_TEMPERATURE_SUMMARY, RES_TEMPERATURE_DETAIL, \n" +
	// " RES_FAN, RES_FAN_SUMMARY, RES_FAN_DETAIL, \n" +
	// " RES_ADCLOG, RES_ADCLOG_SUMMARY, RES_ADCLOG_DETAIL \n" +
	// " FROM LOG_FAULT_HW \n" +
	// " WHERE LOG_KEY = %d LIMIT 1 \n",
	// checkKey);
	//
	// ResultSet rs = db.executeQuery(sqlText);
	// int status = 0;
	// if(rs.next()==true)
	// {
	// // power supply 처리.
	// status = db.getInteger(rs, "RES_POWER");
	// if(status!=OBDtoFaultCheckResultElement.STATUS_NA)
	// {
	// OBDtoFaultCheckResultElement obj = new OBDtoFaultCheckResultElement();
	// String hashKey =
	// OBDefine.ELEMENT_TYPE_HW+"_"+OBDefine.FAULT_CHECK_ELEMENT_HW_POWER;
	// obj.setObj(elementMap.get(hashKey));
	// obj.setStatus(status);
	// obj.setSummary(db.getString(rs, "RES_POWER_SUMMARY"));
	// obj.setCliMessage(db.getString(rs, "RES_POWER_DETAIL"));
	// retVal.add(obj);
	// }
	//
	// // uptime 처리.
	// status = db.getInteger(rs, "RES_UPTIME");
	// if(status!=OBDtoFaultCheckResultElement.STATUS_NA)
	// {
	// OBDtoFaultCheckResultElement obj = new OBDtoFaultCheckResultElement();
	// String hashKey =
	// OBDefine.ELEMENT_TYPE_HW+"_"+OBDefine.FAULT_CHECK_ELEMENT_HW_UPTIME;
	// obj.setObj(elementMap.get(hashKey));
	// obj.setStatus(status);
	// obj.setSummary(db.getString(rs, "RES_UPTIME_SUMMARY"));
	// obj.setCliMessage(db.getString(rs, "RES_UPTIME_DETAIL"));
	// retVal.add(obj);
	// }
	//
	// // license 처리.
	// status = db.getInteger(rs, "RES_LICENSE");
	// if(status!=OBDtoFaultCheckResultElement.STATUS_NA)
	// {
	// OBDtoFaultCheckResultElement obj = new OBDtoFaultCheckResultElement();
	// String hashKey =
	// OBDefine.ELEMENT_TYPE_HW+"_"+OBDefine.FAULT_CHECK_ELEMENT_HW_LICENSE;
	// obj.setObj(elementMap.get(hashKey));
	// obj.setStatus(status);
	// obj.setSummary(db.getString(rs, "RES_LICENSE_SUMMARY"));
	// obj.setCliMessage(db.getString(rs, "RES_LICENSE_DETAIL"));
	// retVal.add(obj);
	// }
	//
	// // interface 처리.
	// status = db.getInteger(rs, "RES_INTERFACE");
	// if(status!=OBDtoFaultCheckResultElement.STATUS_NA)
	// {
	// OBDtoFaultCheckResultElement obj = new OBDtoFaultCheckResultElement();
	// String hashKey =
	// OBDefine.ELEMENT_TYPE_HW+"_"+OBDefine.FAULT_CHECK_ELEMENT_HW_INTERFACE;
	// obj.setObj(elementMap.get(hashKey));
	// obj.setStatus(status);
	// obj.setSummary(db.getString(rs, "RES_INTERFACE_SUMMARY"));
	// obj.setCliMessage(db.getString(rs, "RES_INTERFACE_DETAIL"));
	// retVal.add(obj);
	// }
	//
	// // cpu 처리.
	// status = db.getInteger(rs, "RES_CPU");
	// if(status!=OBDtoFaultCheckResultElement.STATUS_NA)
	// {
	// OBDtoFaultCheckResultElement obj = new OBDtoFaultCheckResultElement();
	// String hashKey =
	// OBDefine.ELEMENT_TYPE_HW+"_"+OBDefine.FAULT_CHECK_ELEMENT_HW_CPU;
	// obj.setObj(elementMap.get(hashKey));
	// obj.setStatus(status);
	// obj.setSummary(db.getString(rs, "RES_CPU_SUMMARY"));
	// obj.setCliMessage(db.getString(rs, "RES_CPU_DETAIL"));
	// retVal.add(obj);
	// }
	//
	// // memory 처리.
	// status = db.getInteger(rs, "RES_MEMORY");
	// if(status!=OBDtoFaultCheckResultElement.STATUS_NA)
	// {
	// OBDtoFaultCheckResultElement obj = new OBDtoFaultCheckResultElement();
	// String hashKey =
	// OBDefine.ELEMENT_TYPE_HW+"_"+OBDefine.FAULT_CHECK_ELEMENT_HW_MEMORY;
	// obj.setObj(elementMap.get(hashKey));
	// obj.setStatus(status);
	// obj.setSummary(db.getString(rs, "RES_MEMORY_SUMMARY"));
	// obj.setCliMessage(db.getString(rs, "RES_MEMORY_DETAIL"));
	// retVal.add(obj);
	// }
	//
	// // temperature 처리.
	// status = db.getInteger(rs, "RES_TEMPERATURE");
	// if(status!=OBDtoFaultCheckResultElement.STATUS_NA)
	// {
	// OBDtoFaultCheckResultElement obj = new OBDtoFaultCheckResultElement();
	// String hashKey =
	// OBDefine.ELEMENT_TYPE_HW+"_"+OBDefine.FAULT_CHECK_ELEMENT_HW_TEMPERATURE;
	// obj.setObj(elementMap.get(hashKey));
	// obj.setStatus(status);
	// obj.setSummary(db.getString(rs, "RES_TEMPERATURE_SUMMARY"));
	// obj.setCliMessage(db.getString(rs, "RES_TEMPERATURE_DETAIL"));
	// retVal.add(obj);
	// }
	//
	// // fan 처리.
	// status = db.getInteger(rs, "RES_FAN");
	// if(status!=OBDtoFaultCheckResultElement.STATUS_NA)
	// {
	// OBDtoFaultCheckResultElement obj = new OBDtoFaultCheckResultElement();
	// String hashKey =
	// OBDefine.ELEMENT_TYPE_HW+"_"+OBDefine.FAULT_CHECK_ELEMENT_HW_FAN;
	// obj.setObj(elementMap.get(hashKey));
	// obj.setStatus(status);
	// obj.setSummary(db.getString(rs, "RES_FAN_SUMMARY"));
	// obj.setCliMessage(db.getString(rs, "RES_FAN_DETAIL"));
	// retVal.add(obj);
	// }
	//
	// // adc log 처리.
	// status = db.getInteger(rs, "RES_ADCLOG");
	// if(status!=OBDtoFaultCheckResultElement.STATUS_NA)
	// {
	// OBDtoFaultCheckResultElement obj = new OBDtoFaultCheckResultElement();
	// String hashKey =
	// OBDefine.ELEMENT_TYPE_HW+"_"+OBDefine.FAULT_CHECK_ELEMENT_HW_ADCLOG;
	// obj.setObj(elementMap.get(hashKey));
	// obj.setStatus(status);
	// obj.setSummary(db.getString(rs, "RES_ADCLOG_SUMMARY"));
	// obj.setCliMessage(db.getString(rs, "RES_ADCLOG_DETAIL"));
	// retVal.add(obj);
	// }
	// }
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
	// }
	// return retVal;
	// }

	private ArrayList<OBDtoFaultCheckResultElement> getFaultCheckLogDetailL23(Long checkKey,
			HashMap<String, OBDtoFaultSolutionInfo> solutionMap, OBDatabase db) throws OBException {
		String sqlText = "";
		ArrayList<OBDtoFaultCheckResultElement> retVal = new ArrayList<OBDtoFaultCheckResultElement>();
		try {
			HashMap<String, OBDtoElement> elementMap = getElementsToMap(OBDefine.ELEMENT_TYPE_L23, db);
			HashMap<String, OBDtoFaultCheckResultElement> logMap = new HashMap<String, OBDtoFaultCheckResultElement>();

			sqlText = String.format(
					" SELECT CATEGORY, TYPE, START_TIME, END_TIME, STATUS, CONTENT, DETAIL \n"
							+ " FROM LOG_FAULT_SUMMARY                                               \n"
							+ " WHERE LOG_KEY = %d AND CATEGORY = %d                                 \n"
							+ " ORDER BY START_TIME DESC                                             \n",
					checkKey, OBDefine.ELEMENT_TYPE_L23);

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				int iType = db.getInteger(rs, "TYPE");
				String hashKey = OBDefine.ELEMENT_TYPE_L23 + "_" + iType;
				OBDtoFaultCheckResultElement resultElement = logMap.get(hashKey);
				if (resultElement != null) {
					OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
					content.setDetail(db.getString(rs, "DETAIL"));
					content.setSummary(db.getString(rs, "CONTENT"));
					resultElement.getResultList().add(content);
				} else {
					OBDtoElement obj = elementMap.get(hashKey);
					if (obj != null) {
						resultElement = new OBDtoFaultCheckResultElement();
						resultElement.setObj(obj);
						ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
						OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
						content.setDetail(db.getString(rs, "DETAIL"));
						content.setSummary(db.getString(rs, "CONTENT"));
						resultList.add(content);
						resultElement.setResultList(resultList);
						OBDtoFaultSolutionInfo solutionInfo = solutionMap.get(hashKey);
						if (solutionInfo != null)
							resultElement.setSolution(solutionInfo.getDescription());
						resultElement.setStartTime(db.getTimestamp(rs, "START_TIME"));
						resultElement.setEndTime(db.getTimestamp(rs, "END_TIME"));
						resultElement.setStatus(db.getInteger(rs, "STATUS"));

						logMap.put(hashKey, resultElement);
					}
				}
			}

			OBDtoFaultCheckResultElement result;
			// 결과물을 생성한다.
			// vlan 처리.
			String hashKey = OBDefine.ELEMENT_TYPE_L23 + "_" + OBDefine.FAULT_CHECK_ELEMENT_L23_VLAN;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			// stp 처리.
			hashKey = OBDefine.ELEMENT_TYPE_L23 + "_" + OBDefine.FAULT_CHECK_ELEMENT_L23_STP;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			// trunk 처리.
			hashKey = OBDefine.ELEMENT_TYPE_L23 + "_" + OBDefine.FAULT_CHECK_ELEMENT_L23_TRUNK;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			// vrrp 처리.
			hashKey = OBDefine.ELEMENT_TYPE_L23 + "_" + OBDefine.FAULT_CHECK_ELEMENT_L23_VRRP;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			// routing 처리.
			hashKey = OBDefine.ELEMENT_TYPE_L23 + "_" + OBDefine.FAULT_CHECK_ELEMENT_L23_ROUTING;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			// interface 처리.
			hashKey = OBDefine.ELEMENT_TYPE_L23 + "_" + OBDefine.FAULT_CHECK_ELEMENT_L23_INTERFACE;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			// arp 처리.

		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	private ArrayList<OBDtoFaultCheckResultElement> getFaultCheckLogDetailL47(Long checkKey,
			HashMap<String, OBDtoFaultSolutionInfo> solutionMap, OBDatabase db) throws OBException {
		String sqlText = "";
		ArrayList<OBDtoFaultCheckResultElement> retVal = new ArrayList<OBDtoFaultCheckResultElement>();
		try {
			HashMap<String, OBDtoElement> elementMap = getElementsToMap(OBDefine.ELEMENT_TYPE_L47, db);
			HashMap<String, OBDtoFaultCheckResultElement> logMap = new HashMap<String, OBDtoFaultCheckResultElement>();

			sqlText = String.format(
					" SELECT CATEGORY, TYPE, START_TIME, END_TIME, STATUS, CONTENT, DETAIL \n"
							+ " FROM LOG_FAULT_SUMMARY                                               \n"
							+ " WHERE LOG_KEY = %d AND CATEGORY = %d                                 \n"
							+ " ORDER BY START_TIME DESC                                             \n",
					checkKey, OBDefine.ELEMENT_TYPE_L47);

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				int iType = db.getInteger(rs, "TYPE");
				String hashKey = OBDefine.ELEMENT_TYPE_L47 + "_" + iType;
				OBDtoFaultCheckResultElement resultElement = logMap.get(hashKey);
				if (resultElement != null) {
					OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
					content.setDetail(db.getString(rs, "DETAIL"));
					content.setSummary(db.getString(rs, "CONTENT"));
					resultElement.getResultList().add(content);
				} else {
					OBDtoElement obj = elementMap.get(hashKey);
					if (obj != null) {
						resultElement = new OBDtoFaultCheckResultElement();
						resultElement.setObj(obj);
						ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
						OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
						content.setDetail(db.getString(rs, "DETAIL"));
						content.setSummary(db.getString(rs, "CONTENT"));
						resultList.add(content);
						resultElement.setResultList(resultList);
						OBDtoFaultSolutionInfo solutionInfo = solutionMap.get(hashKey);
						if (solutionInfo != null)
							resultElement.setSolution(solutionInfo.getDescription());
						resultElement.setStartTime(db.getTimestamp(rs, "START_TIME"));
						resultElement.setEndTime(db.getTimestamp(rs, "END_TIME"));
						resultElement.setStatus(db.getInteger(rs, "STATUS"));

						logMap.put(hashKey, resultElement);
					}
				}
			}

			OBDtoFaultCheckResultElement result;
			// 결과물을 생성한다.
			// 미사용 설정 처리.
			String hashKey = OBDefine.ELEMENT_TYPE_L47 + "_" + OBDefine.FAULT_CHECK_ELEMENT_L47_NOT_USED_CF;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			// 유휴 가상서버 처리 ...
			hashKey = OBDefine.ELEMENT_TYPE_L47 + "_" + OBDefine.FAULT_CHECK_ELEMENT_L47_SLEEP_VS;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			// 세션 테이블 처리 ...
			hashKey = OBDefine.ELEMENT_TYPE_L47 + "_" + OBDefine.FAULT_CHECK_ELEMENT_L47_SESSION_TABLE;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	private ArrayList<OBDtoFaultCheckResultElement> getFaultCheckLogDetailSVC(Long checkKey,
			HashMap<String, OBDtoFaultSolutionInfo> solutionMap, OBDatabase db) throws OBException {
		String sqlText = "";
		ArrayList<OBDtoFaultCheckResultElement> retVal = new ArrayList<OBDtoFaultCheckResultElement>();
		try {
			HashMap<String, OBDtoElement> elementMap = getElementsToMap(OBDefine.ELEMENT_TYPE_SVC, db);
			HashMap<String, OBDtoFaultCheckResultElement> logMap = new HashMap<String, OBDtoFaultCheckResultElement>();

			sqlText = String.format(
					" SELECT CATEGORY, TYPE, START_TIME, END_TIME, STATUS, CONTENT, DETAIL \n"
							+ " FROM LOG_FAULT_SUMMARY                                               \n"
							+ " WHERE LOG_KEY = %d AND CATEGORY = %d                                 \n"
							+ " ORDER BY START_TIME DESC                                             \n",
					checkKey, OBDefine.ELEMENT_TYPE_SVC);

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				int iType = db.getInteger(rs, "TYPE");
				String hashKey = OBDefine.ELEMENT_TYPE_SVC + "_" + iType;
				OBDtoFaultCheckResultElement resultElement = logMap.get(hashKey);
				if (resultElement != null) {
					OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
					content.setDetail(db.getString(rs, "DETAIL"));
					content.setSummary(db.getString(rs, "CONTENT"));
					resultElement.getResultList().add(content);
				} else {
					OBDtoElement obj = elementMap.get(hashKey);
					if (obj != null) {
						resultElement = new OBDtoFaultCheckResultElement();
						resultElement.setObj(obj);
						ArrayList<OBDtoFaultCheckResultContent> resultList = new ArrayList<OBDtoFaultCheckResultContent>();
						OBDtoFaultCheckResultContent content = new OBDtoFaultCheckResultContent();
						content.setDetail(db.getString(rs, "DETAIL"));
						content.setSummary(db.getString(rs, "CONTENT"));
						resultList.add(content);
						resultElement.setResultList(resultList);
						OBDtoFaultSolutionInfo solutionInfo = solutionMap.get(hashKey);
						if (solutionInfo != null)
							resultElement.setSolution(solutionInfo.getDescription());
						resultElement.setStartTime(db.getTimestamp(rs, "START_TIME"));
						resultElement.setEndTime(db.getTimestamp(rs, "END_TIME"));
						resultElement.setStatus(db.getInteger(rs, "STATUS"));

						logMap.put(hashKey, resultElement);
					}
				}
			}

			OBDtoFaultCheckResultElement result;
			// 결과물을 생성한다.
			// 서비스 진단 처리.
			String hashKey = OBDefine.ELEMENT_TYPE_SVC + "_" + OBDefine.FAULT_CHECK_ELEMENT_SVC_PKTDUMP;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			hashKey = OBDefine.ELEMENT_TYPE_SVC + "_" + OBDefine.FAULT_CHECK_ELEMENT_SVC_DOWNLOAD;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			hashKey = OBDefine.ELEMENT_TYPE_SVC + "_" + OBDefine.FAULT_CHECK_ELEMENT_SVC_RESPONSE;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);

			hashKey = OBDefine.ELEMENT_TYPE_SVC + "_" + OBDefine.FAULT_CHECK_ELEMENT_SVC_PKTLOSS;
			result = logMap.get(hashKey);
			if (result != null)
				retVal.add(result);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	public HashMap<String, OBDtoElement> getElementsToMap(Integer type, OBDatabase db) throws OBException {
		HashMap<String, OBDtoElement> retVal = new HashMap<String, OBDtoElement>();
		String sqlText = "";

		try {
			sqlText = String.format(
					" SELECT CODE, NAME, DESCRIPTION, TARGET   \n" + " FROM %s                                  \n"
							+ " WHERE TYPE=%d                            \n",
					OBCommon.makeProperTableName("MNG_FAULT_CHECK_ITEMS"), type);
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoElement obj = new OBDtoElement();
				obj.setDescription(db.getString(rs, "DESCRIPTION"));
				obj.setIndex(db.getInteger(rs, "CODE"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setState(OBDefine.STATE_DISABLE);
				obj.setTarget(db.getInteger(rs, "TARGET"));
				String key = type + "_" + obj.getIndex();
				retVal.put(key, obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// System.out.println(new
	// OBFaultMngImpl().getFaultCheckLogInfo(2486860368685L));
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public OBDtoFaultCheckLog getFaultCheckLogInfo(Long logKey) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultCheckLog retVal = null;
		try {
			db.openDB();
			retVal = getFaultCheckLogInfo(logKey, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	public OBDtoFaultCheckLog getFaultCheckLogInfo(Long logKey, OBDatabase db) throws OBException {
		OBDtoFaultCheckLog retVal = new OBDtoFaultCheckLog();

		String sqlText = "";

		try {
			// adc index를 먼저 추출한다.
			sqlText = String.format(" SELECT A.ADC_INDEX 	\n" + " FROM LOG_FAULT A      				\n"
					+ " WHERE A.LOG_KEY=%d                    \n", logKey);
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return retVal;
			}

			Integer adcIndex = db.getInteger(rs, "ADC_INDEX");
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				sqlText = String.format(
						" SELECT A.INDEX, A.LOG_KEY, A.OCCUR_TIME, A.STATUS_TOTAL, A.ELAPSE_TIME, A.ADC_INDEX,  			\n"
								+ " A.ACCNT_INDEX, A.MSG_SUMMARY, A.TEMPLATE_INDEX, A.CLIENT_IP, A.VSVC_INDEX, A.ITEM_TYPE,       	\n"
								+ " B.NAME AS ACCNT_NAME, C.NAME AS ADC_NAME, D.VSNAME, D.VSIP, D.VSPORT								\n"
								+ " FROM LOG_FAULT               A  																	\n"
								+ " LEFT  JOIN MNG_ACCNT         B  																	\n"
								+ " ON B.INDEX=A.ACCNT_INDEX																			\n"
								+ " INNER JOIN MNG_ADC C    																			\n"
								+ " ON C.INDEX=A.ADC_INDEX  																			\n"
								+ " LEFT JOIN (																						\n"
								+ "	SELECT B.INDEX AS VSINDEX, A.NAME AS VSNAME, A.VIRTUAL_IP AS VSIP, B.VIRTUAL_PORT AS VSPORT	    \n"
								+ "	FROM TMP_SLB_VSERVER A																			\n"
								+ "	LEFT join TMP_SLB_VS_SERVICE B																	\n"
								+ "	ON A.INDEX = B.VS_INDEX																			\n"
								+ " ) D																								\n"
								+ " ON A.VSVC_INDEX = D.VSINDEX																		\n"
								+ " WHERE A.LOG_KEY=%d                                                        						\n",
						logKey);
				// " AND A.ADC_INDEX=%d \n",
				// logKey, adcIndex);
			} else {
				sqlText = String.format(
						" SELECT A.INDEX, A.LOG_KEY, A.OCCUR_TIME, A.STATUS_TOTAL, A.ELAPSE_TIME, A.ADC_INDEX,  			\n"
								+ " A.ACCNT_INDEX, A.MSG_SUMMARY, A.TEMPLATE_INDEX, A.CLIENT_IP, A.VSVC_INDEX, A.ITEM_TYPE,       	\n"
								+ " B.NAME AS ACCNT_NAME, C.NAME AS ADC_NAME, D.VSNAME, D.VSIP, D.VSPORT								\n"
								+ " FROM LOG_FAULT              A      																\n"
								+ " LEFT  JOIN MNG_ACCNT        B  																	\n"
								+ " ON B.INDEX=A.ACCNT_INDEX																			\n"
								+ " INNER JOIN MNG_ADC C    																			\n"
								+ " ON C.INDEX=A.ADC_INDEX  																			\n"
								+ " LEFT JOIN ( 																						\n"
								+ "	SELECT A.INDEX AS VSINDEX, A.NAME AS VSNAME, A.VIRTUAL_IP AS VSIP, A.VIRTUAL_PORT AS VSPORT		\n"
								+ "	FROM TMP_SLB_VSERVER A																			\n"
								+ "	LEFT join TMP_SLB_VS_SERVICE B																	\n"
								+ "	ON A.INDEX = B.VS_INDEX																			\n"
								+ " ) D																								\n"
								+ " ON A.VSVC_INDEX = D.VSINDEX																		\n"
								+ " WHERE A.LOG_KEY=%d                                                        						\n",
						logKey);
			}

			rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal.setIndex(db.getLong(rs, "INDEX"));
				retVal.setLogKey(db.getLong(rs, "LOG_KEY"));
				retVal.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				retVal.setTemplateIndex(db.getLong(rs, "TEMPLATE_INDEX"));
				retVal.setStatus(db.getString(rs, "STATUS_TOTAL"));
				retVal.setElapseTime(db.getInteger(rs, "ELAPSE_TIME"));
				retVal.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				retVal.setAccntIndex(db.getInteger(rs, "ADC_INDEX"));
				retVal.setAccntName(db.getString(rs, "ACCNT_NAME"));
				retVal.setSummary(db.getString(rs, "MSG_SUMMARY"));
				retVal.setVsvcIndex(db.getString(rs, "VSVC_INDEX"));
				retVal.setClientIP(db.getString(rs, "CLIENT_IP"));
				retVal.setVsvcName(db.getString(rs, "VSNAME"));
				retVal.setVsvcIPAddress(db.getString(rs, "VSIP"));
				retVal.setVsvcPort(db.getInteger(rs, "VSPORT"));
				retVal.setCheckItem(db.getInteger(rs, "ITEM_TYPE"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	// #3926-3 #2: 삭제대상정보를 얻어오기 위함, indexList를 통해 ADC name 리스트를 얻어옴
	private ArrayList<String> getFaultCheckLogAdcName(ArrayList<String> indexList) throws OBException {
		ArrayList<String> logAdcName = new ArrayList<String>();
		OBDatabase db = new OBDatabase();

		StringBuilder stringBuilder = new StringBuilder("");
		for (String index : indexList) {
			stringBuilder.append(index + ", ");
		}

		String sqlText = String.format("SELECT ADC_NAME FROM LOG_FAULT WHERE LOG_KEY IN(%s)",
				stringBuilder.substring(0, stringBuilder.lastIndexOf(",")));

		db.openDB();
		try {
			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				logAdcName.add(rs.getString("ADC_NAME"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} finally {
			db.closeDB();
		}

		return logAdcName;
	}

	// TODO
	public void deleteFaultCheckLogInfo(ArrayList<String> indexList, OBDtoExtraInfo extraInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. indexList:%s, extraInfo:%s", indexList, extraInfo));

		// #3926-3 #2: 삭제대상정보를 얻어오기 위함
		ArrayList<String> indexAdcName = getFaultCheckLogAdcName(indexList);
		StringBuilder adcNames = new StringBuilder("");
		for (String adcName : indexAdcName) {
			adcNames.append(adcName + ", ");
		}

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String sqlIndexs = "'-1'"; // where-in empty 방지
			for (int i = 0; i < indexList.size(); i++) {
				String index = indexList.get(i);
				sqlIndexs += ", ";
				sqlIndexs += OBParser.sqlString(index);
			}

			// 삭제
			sqlText = String.format(" UPDATE LOG_FAULT " + " SET AVAILABLE=%d " + " WHERE LOG_KEY IN ( %s ) ;", // where-in:empty
																												// string
																												// 불가,
																												// null
																												// 불가,
																												// OK
					OBDefine.STATE_DISABLE, sqlIndexs);
			db.executeUpdate(sqlText);
			// 감사로그 저장.
			// #3926-3 #2: 삭제대상정보 extraMessage로 삽입
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_FAULT_DELETE_LIST_SUCCESS, adcNames.substring(0, adcNames.lastIndexOf(",")));
		} catch (SQLException e) {
			// #3926-3 #2: 삭제대상정보 extraMessage로 삽입
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_FAULT_DELETE_LIST_FAIL, adcNames.substring(0, adcNames.lastIndexOf(",")));
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			// #3926-3 #2: 삭제대상정보 extraMessage로 삽입
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_FAULT_DELETE_LIST_FAIL, adcNames.substring(0, adcNames.lastIndexOf(",")));
			throw e;
		} catch (Exception e) {
			// #3926-3 #2: 삭제대상정보 extraMessage로 삽입
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_FAULT_DELETE_LIST_FAIL, adcNames.substring(0, adcNames.lastIndexOf(",")));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	/**
	 * 경과 시간을 저장한다. msec 단위.
	 * 
	 * @param logKey
	 * @param elapseTime
	 * @param db
	 */
	// public void updateLogFaultElapseTime(long logKey, int elapseTime, OBDatabase
	// db)
	// {
	// String sqlText = "";
	// try
	// {
	// sqlText = String.format(" UPDATE LOG_FAULT \n" +
	// " SET ELAPSE_TIME=%d \n" +
	// " WHERE LOG_KEY=%d \n",
	// elapseTime,
	// logKey);
	//
	// db.executeUpdate(sqlText);
	// }
	// catch(Exception e)
	// {
	// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to update
	// log_fault status. key:%d", logKey));
	// }
	// }
	//
	// public void updateDiagnosisStatus(long logKey, int status, OBDatabase db)
	// {
	// String sqlText = "";
	// try
	// {
	// sqlText = String.format(" UPDATE LOG_FAULT \n" +
	// " SET STATUS=%d \n" +
	// " WHERE LOG_KEY=%d \n",
	// status,
	// logKey);
	//
	// db.executeUpdate(sqlText);
	// }
	// catch(Exception e)
	// {
	// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to update
	// log_fault status. key:%d", logKey));
	// }
	// }

	// public void updateDiagnosisStatus(long logKey, int progressRate, int
	// elapsedTime, int checkCount, int failCount, String cliMsg, OBDatabase db)
	// {
	// String sqlText = "";
	// try
	// {
	// sqlText = String.format(" UPDATE LOG_FAULT \n" +
	// " SET STATUS=%d, ELAPSE_TIME=%d, ITEM_HW_CHCK=%d, ITEM_HW_FAIL=%d,
	// CHECK_ITEM_MSG=%s \n" +
	// " WHERE LOG_KEY=%d \n",
	// progressRate,
	// elapsedTime,
	// checkCount,
	// failCount,
	// OBParser.sqlString(cliMsg),
	// logKey);
	//
	// db.executeUpdate(sqlText);
	// }
	// catch(Exception e)
	// {
	// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to update
	// log_fault status. key:%d", logKey));
	// }
	// }
	//
	// public void updateDiagnosisStatus(long logKey, int progressRate, int
	// elapsedTime, int svcRate, String cliMsg, OBDatabase db)
	// {
	// String sqlText = "";
	// try
	// {
	// sqlText = String.format(" UPDATE LOG_FAULT \n" +
	// " SET STATUS=%d, ELAPSE_TIME=%d, ITEM_SVC_RATE=%d, CHECK_ITEM_MSG=%s \n" +
	// " WHERE LOG_KEY=%d \n",
	// progressRate,
	// elapsedTime,
	// svcRate,
	// OBParser.sqlString(cliMsg),
	// logKey);
	//
	// db.executeUpdate(sqlText);
	// }
	// catch(Exception e)
	// {
	// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to update
	// log_fault status. key:%d", logKey));
	// }
	// }
	//
	// public void updateDiagnosisStatus(long logKey, int check, int fail,
	// OBDatabase db)
	// {
	// String sqlText = "";
	// try
	// {
	// sqlText = String.format(" UPDATE LOG_FAULT \n" +
	// " SET ITEM_HW_CHCK=%d, ITEM_HW_FAIL=%d \n" +
	// " WHERE LOG_KEY=%d \n",
	// check,
	// fail,
	// logKey);
	//
	// db.executeUpdate(sqlText);
	// }
	// catch(Exception e)
	// {
	// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to update
	// log_fault status. key:%d", logKey));
	// }
	// }

	// public void updateLogFaultStatus(long logKey, String keyName, int status,
	// OBDatabase db)
	// {
	// String sqlText = "";
	// try
	// {
	// sqlText = String.format(" UPDATE LOG_FAULT \n" +
	// " SET %s=%d \n" +
	// " WHERE LOG_KEY=%d \n",
	// keyName,
	// status,
	// logKey);
	//
	// db.executeUpdate(sqlText);
	// }
	// catch(Exception e)
	// {
	// OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to update
	// log_fault status. key:%d", logKey));
	// }
	// }

	public void updateDiagnosisStatus(long logKey, Integer status, Integer elapseTime, Integer hwCheck, Integer hwFail,
			Integer svcRate, String summary, String checkMsg, String resultMsg) {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String setText = "";

			if (status != null) {
				if (!setText.isEmpty())
					setText += ", ";
				setText += String.format("STATUS_TOTAL=%d", status);
			}

			if (elapseTime != null) {
				if (!setText.isEmpty())
					setText += ", ";
				setText += String.format("ELAPSE_TIME=%d", elapseTime);
			}

			if (hwCheck != null) {
				if (!setText.isEmpty())
					setText += ", ";
				setText += String.format("STATUS_HW_CHECK=%d", hwCheck);
			}

			if (hwFail != null) {
				if (!setText.isEmpty())
					setText += ", ";
				setText += String.format("STATUS_HW_FAIL=%d", hwFail);
			}

			if (svcRate != null) {
				if (!setText.isEmpty())
					setText += ", ";
				setText += String.format("STATUS_SVC=%d", svcRate);
			}

			if (summary != null && !summary.isEmpty()) {
				if (!setText.isEmpty())
					setText += ", ";
				setText += String.format("MSG_SUMMARY=%s", OBParser.sqlString(summary));
			}

			if (checkMsg != null && !checkMsg.isEmpty()) {
				if (!setText.isEmpty())
					setText += ", ";
				setText += String.format("MSG_CUR_TITLE=%s", OBParser.sqlString(checkMsg));
			}

			if (resultMsg != null && !resultMsg.isEmpty()) {
				if (!setText.isEmpty())
					setText += ", ";
				setText += String.format("MSG_CUR_RESULT=%s", OBParser.sqlString(resultMsg));
			}

			if (!setText.isEmpty()) {
				sqlText = String.format(" UPDATE LOG_FAULT             \n" + " SET %s                       \n"
						+ " WHERE LOG_KEY=%d             \n", setText, logKey);

				db.executeUpdate(sqlText);
			}
		} catch (RuntimeException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to update log_fault status. key:%d", logKey));
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to update log_fault status. key:%d", logKey));
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private String makeSqlLimitContext(OBDtoSearch searchObj) throws OBException {
		if (searchObj == null)
			return "";

		int offset = 0;
		if (searchObj.getBeginIndex() != null)
			offset = searchObj.getBeginIndex().intValue();

		int limit = 0;
		String sqlLimit = "";

		if (searchObj.getEndIndex() != null) {
			limit = Math.abs(searchObj.getEndIndex().intValue() - offset) + 1;
			sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
		}

		// if(searchKeys!=null && !searchKeys.isEmpty())
		// {
		// String wildcardKey = "%" + searchKeys + "%";
		// sqlSearch = String.format(" (NAME LIKE %s OR VIRTUAL_IP LIKE %s) ",
		// OBParser.sqlString(wildcardKey) , OBParser.sqlString(wildcardKey));
		// }
		// else
		// {
		// sqlSearch = " TRUE ";
		// }

		return sqlLimit;
	}

	public HashMap<String, String> getSolutionMap(OBDatabase db) throws OBException {
		HashMap<String, String> retVal = new HashMap<String, String>();

		String sqlText = "";

		try {
			sqlText = String.format(
					" SELECT ITEM_CATEGORY, ITEM_INDEX, DESCRIPTION	     \n"
							+ " FROM %s                                            \n",
					OBCommon.makeProperTableName("MNG_FAULT_SOLUTION"));

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				Integer category = db.getInteger(rs, "ITEM_CATEGORY");
				Integer index = db.getInteger(rs, "ITEM_INDEX");
				String key = category + "_" + index;
				retVal.put(key, db.getString(rs, "DESCRIPTION"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(1);
	// ArrayList<OBDtoSlbNodeInfo> list = new
	// OBFaultMngImpl().getUnusedNodeList(adcInfo, db);
	// System.out.println(list);
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 사용하지 않은 노드 목록 추출.
	 * 
	 * @param adcInfo
	 * @param db
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoSlbNodeInfo> getUnusedNodeList(OBDtoAdcInfo adcInfo) throws OBException {
		ArrayList<OBDtoSlbNodeInfo> retVal = new ArrayList<OBDtoSlbNodeInfo>();

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT ALTEON_ID, IP_ADDRESS, NAME, STATE, PORT        		\n"
							+ " FROM TMP_SLB_NODE                                     		\n"
							+ " WHERE ADC_INDEX = %d                                   		\n"
							+ " AND INDEX NOT IN                                       		\n"
							+ " (                                                      		\n"
							+ "    SELECT DISTINCT(NODE_INDEX) FROM TMP_SLB_POOLMEMBER 		\n"
							+ "    WHERE ADC_INDEX = %d                                		\n"
							+ "    UNION					                               		\n"
							+ "    SELECT BAK_ID FROM TMP_SLB_NODE 							\n"
							+ "    WHERE ADC_INDEX = %d AND BAK_TYPE = 1                 		\n"
							+ " )                                                      		",
					adcInfo.getIndex(), adcInfo.getIndex(), adcInfo.getIndex());
			// sqlText = String.format(" SELECT A.ALTEON_ID, A.IP_ADDRESS, A.NAME, A.STATE,
			// A.PORT, B.POOL_INDEX \n" +
			// " FROM TMP_SLB_NODE A \n" +
			// " LEFT JOIN TMP_SLB_POOLMEMBER B \n" +
			// " ON A.INDEX = B.NODE_INDEX \n" +
			// " WHERE A.ADC_INDEX = %d \n",
			// adcInfo.getIndex());
			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoSlbNodeInfo obj = new OBDtoSlbNodeInfo();
				obj.setIndex(db.getString(rs, "ALTEON_ID"));
				obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				obj.setName(db.getString(rs, "NAME"));

				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}

		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(1);
	// ArrayList<OBDtoSlbPoolInfo> list = new
	// OBFaultMngImpl().getNoMemberPoolList(adcInfo, db);
	// for(OBDtoSlbPoolInfo obj:list)
	// System.out.println(obj.getIndex());
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 멤버가 할당되지 않은 pool 목록을 추출한다.
	 * 
	 * @param adcInfo
	 * @param db
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoSlbPoolInfo> getNoMemberPoolList(OBDtoAdcInfo adcInfo) throws OBException {
		ArrayList<OBDtoSlbPoolInfo> retVal = new ArrayList<OBDtoSlbPoolInfo>();

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT INDEX, NAME, ALTEON_ID, LB_METHOD, HEALTH_CHECK        \n"
							+ " FROM TMP_SLB_POOL                                             \n"
							+ " WHERE ADC_INDEX = %d                                          \n"
							+ " AND INDEX NOT IN                                              \n"
							+ " (                                                             \n"
							+ "   SELECT DISTINCT(POOL_INDEX) FROM TMP_SLB_POOLMEMBER         \n"
							+ "   WHERE ADC_INDEX = %d                                        \n"
							+ " )                                                             \n",
					+adcInfo.getIndex(), adcInfo.getIndex());
			// sqlText = String.format(" SELECT A.INDEX, A.NAME, A.ALTEON_ID, A.LB_METHOD,
			// A.HEALTH_CHECK, \n" +
			// " B.POOL_INDEX \n" +
			// " FROM TMP_SLB_POOL A \n" +
			// " LEFT JOIN TMP_SLB_POOLMEMBER B \n" +
			// " ON A.INDEX = B.POOL_INDEX \n" +
			// " WHERE A.ADC_INDEX = %d \n",
			// adcInfo.getIndex());
			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoSlbPoolInfo obj = new OBDtoSlbPoolInfo();
				if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
					obj.setIndex(db.getString(rs, "ALTEON_ID"));
				} else {
					obj.setIndex(db.getString(rs, "NAME"));
				}
				obj.setLbMethod(db.getInteger(rs, "LB_METHOD"));
				obj.setHealthCheckMethod(db.getInteger(rs, "HEALTH_CHECK"));
				obj.setName(db.getString(rs, "NAME"));
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(1);
	// ArrayList<OBDtoSlbPoolInfo> list = new
	// OBFaultMngImpl().getUnusedPoolList(adcInfo, db);
	// for(OBDtoSlbPoolInfo obj:list)
	// System.out.println(obj.getIndex());
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }
	/**
	 * 사용하지 않은 Pool 목록을 추출한다.
	 * 
	 * @param adcInfo
	 * @param db
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoSlbPoolInfo> getUnusedPoolList(OBDtoAdcInfo adcInfo) throws OBException {
		ArrayList<OBDtoSlbPoolInfo> retVal = new ArrayList<OBDtoSlbPoolInfo>();

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				sqlText = String.format(" SELECT INDEX, NAME, ALTEON_ID, LB_METHOD, HEALTH_CHECK  \n"
						+ " FROM TMP_SLB_POOL                                       \n"
						+ " WHERE ADC_INDEX = %d                                    \n"
						+ "     AND CAST(ALTEON_ID AS VARCHAR) NOT IN (             \n" + // FLB 그룹이 아니다.
						"        SELECT GROUP_ID FROM TMP_FLB_FILTER              \n"
						+ "        WHERE ADC_INDEX = %d                             \n"
						+ "     )                                                   \n"
						+ "     AND INDEX NOT IN (                                  \n" + // SLB 그룹이 아니다.
						"        SELECT POOL_INDEX FROM TMP_SLB_VS_SERVICE        \n"
						+ "        WHERE ADC_INDEX = %d                             \n"
						+ "     )                                                   \n", adcInfo.getIndex(),
						adcInfo.getIndex(), adcInfo.getIndex());
				// sqlText = String.format(" SELECT A.INDEX, A.NAME, A.ALTEON_ID, A.LB_METHOD,
				// A.HEALTH_CHECK, \n" +
				// " B.INDEX AS VS_INDEX \n" +
				// " FROM TMP_SLB_POOL A \n" +
				// " LEFT JOIN TMP_SLB_VS_SERVICE B \n" +
				// " ON A.INDEX = B.POOL_INDEX \n" +
				// " WHERE A.ADC_INDEX = %d \n",
				// adcInfo.getIndex());
			} else {
				sqlText = String.format(
						" SELECT INDEX, NAME, ALTEON_ID, LB_METHOD, HEALTH_CHECK  \n"
								+ " FROM TMP_SLB_POOL A                                     \n"
								+ " WHERE ADC_INDEX = %d                                    \n"
								+ " AND INDEX NOT IN                                        \n"
								+ " (                                                       \n"
								+ "    SELECT POOL_INDEX FROM TMP_SLB_VSERVER               \n"
								+ "    WHERE ADC_INDEX = %d                                 \n"
								+ " )                                                       \n",
						adcInfo.getIndex(), adcInfo.getIndex());
				// sqlText = String.format(" SELECT A.INDEX, A.NAME, A.ALTEON_ID, A.LB_METHOD,
				// A.HEALTH_CHECK, \n" +
				// " B.INDEX AS VS_INDEX \n" +
				// " FROM TMP_SLB_POOL A \n" +
				// " LEFT JOIN TMP_SLB_VSERVER B \n" +
				// " ON A.INDEX = B.POOL_INDEX \n" +
				// " WHERE A.ADC_INDEX = %d \n",
				// adcInfo.getIndex());
			}
			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoSlbPoolInfo obj = new OBDtoSlbPoolInfo();
				if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
					obj.setIndex(db.getString(rs, "ALTEON_ID"));
				} else {
					obj.setIndex(db.getString(rs, "NAME"));
				}
				obj.setLbMethod(db.getInteger(rs, "LB_METHOD"));
				obj.setHealthCheckMethod(db.getInteger(rs, "HEALTH_CHECK"));
				obj.setName(db.getString(rs, "NAME"));
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(1);
	// ArrayList<OBDtoSlbVServerInfo> list = new
	// OBFaultMngImpl().getInvalidPoolVServerList(adcInfo, db);
	// for(OBDtoSlbVServerInfo obj:list)
	// System.out.println(obj.getIndex());
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	/**
	 * Virtual Server/Service에 할당된 pool에 멤버가 없는 경우의 vs 목록 추출.
	 * 
	 * @param adcInfo
	 * @param db
	 * @return
	 * @throws OBException
	 */
	// service/pool가 할당되지 않은 vs 목록 추출.
	public ArrayList<OBDtoSlbVServerInfo> getInvalidPoolVServerList(OBDtoAdcInfo adcInfo) throws OBException {
		ArrayList<OBDtoSlbVServerInfo> retVal = new ArrayList<OBDtoSlbVServerInfo>();

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			ArrayList<OBDtoSlbPoolInfo> unavailPoolList = getNoMemberPoolList(adcInfo);
			String poolList = OBParser.sqlString("-1");
			for (OBDtoSlbPoolInfo poolInfo : unavailPoolList) {
				if (!poolList.isEmpty())
					poolList += ", ";
				poolList += OBParser.sqlString(OBCommon.makePoolIndex(adcInfo.getIndex(), poolInfo.getIndex()));
			}

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				sqlText = String.format(
						" SELECT A.INDEX, A.NAME, A.ALTEON_ID, A.VIRTUAL_IP                       \n"
								+ " FROM TMP_SLB_VSERVER A                                                  \n"
								+ " LEFT JOIN TMP_SLB_VS_SERVICE B                                          \n"
								+ " ON A.INDEX = B.VS_INDEX                                                 \n"
								+ " WHERE A.ADC_INDEX = %d                                                  \n"
								+ " AND B.POOL_INDEX IN ( %s )                                              \n",
						adcInfo.getIndex(), poolList);
			} else {
				sqlText = String.format(
						" SELECT A.ADC_INDEX, A.ALTEON_ID, A.NAME, A.VIRTUAL_IP,                  \n"
								+ " FROM TMP_SLB_VSERVER A                                                  \n"
								+ " WHERE A.ADC_INDEX = %d AND A.POOL_INDEX IN (%s)                         \n",
						adcInfo.getIndex(), poolList);
			}
			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				String vsIndex = db.getString(rs, "VS_INDEX");
				if (vsIndex == null || vsIndex.isEmpty()) {
					OBDtoSlbVServerInfo obj = new OBDtoSlbVServerInfo();
					// if(adcInfo.getAdcType()==OBDefine.ADC_TYPE_ALTEON)
					// {
					// obj.setIndex(db.getString(rs, "ALTEON_ID"));
					// }
					// else
					// {
					// obj.setIndex(db.getString(rs, "NAME"));
					// }
					// obj.setLbMethod(db.getInteger(rs, "LB_METHOD"));
					// obj.setHealthCheckMethod(db.getInteger(rs, "HEALTH_CHECK"));
					// obj.setName(db.getString(rs, "NAME"));
					retVal.add(obj);
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(1);
	// ArrayList<OBDtoSlbVServerInfo> list = new
	// OBFaultMngImpl().getNoMemberVServerList(adcInfo, db);
	// for(OBDtoSlbVServerInfo obj:list)
	// System.out.println(obj.getIndex());
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	// pool 또는 vscv가 할당되지 않은 vs 목록 추출.
	public ArrayList<OBDtoSlbVServerInfo> getNoMemberVServerList(OBDtoAdcInfo adcInfo) throws OBException {
		ArrayList<OBDtoSlbVServerInfo> retVal = new ArrayList<OBDtoSlbVServerInfo>();

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				sqlText = String.format(
						" SELECT INDEX, NAME, ALTEON_ID, VIRTUAL_IP      \n"
								+ " FROM TMP_SLB_VSERVER                           \n"
								+ " WHERE ADC_INDEX = %d                           \n"
								+ " AND INDEX NOT IN                               \n"
								+ " (                                              \n"
								+ "    SELECT VS_INDEX FROM TMP_SLB_VS_SERVICE     \n"
								+ "    WHERE ADC_INDEX = %d                        \n"
								+ " )                                              \n",
						adcInfo.getIndex(), adcInfo.getIndex());
			} else {
				sqlText = String.format(
						" SELECT INDEX, NAME, ALTEON_ID, VIRTUAL_IP                   \n"
								+ " FROM TMP_SLB_VSERVER                                            \n"
								+ " WHERE ADC_INDEX = %d                                            \n"
								+ " AND ( POOL_INDEX IS NULL OR POOL_INDEX='' OR POOL_INDEX='%d_')  \n",
						adcInfo.getIndex(), adcInfo.getIndex());
			}
			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoSlbVServerInfo obj = new OBDtoSlbVServerInfo();
				if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
					obj.setIndex(db.getString(rs, "ALTEON_ID"));
				} else {
					obj.setIndex(db.getString(rs, "NAME"));
				}
				obj.setIpAddress(db.getString(rs, "VIRTUAL_IP"));
				obj.setName(db.getString(rs, "NAME"));
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(1);
	// ArrayList<OBDtoSlbVServerInfo> list = new
	// OBFaultMngImpl().getSleepVServerList(adcInfo, 7, db);
	// for(OBDtoSlbVServerInfo obj:list)
	// System.out.println(obj.getIndex());
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 일정 기간 사용하지 않은 vserver 목록을 추출한다.
	 * 
	 * @param adcInfo
	 * @param sleepDay
	 * @param db
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoSlbVServerInfo> getSleepVServerList(OBDtoAdcInfo adcInfo, Integer sleepDay)
			throws OBException {
		ArrayList<OBDtoSlbVServerInfo> retVal = new ArrayList<OBDtoSlbVServerInfo>();
		Timestamp currentTime = OBDateTime.toTimestamp(OBDateTime.now());
		Timestamp beginTime = new Timestamp(currentTime.getTime() - (sleepDay) * 24L * 60L * 60L * 1000L);

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				sqlText = String.format(
						" SELECT NAME, ALTEON_ID, VIRTUAL_IP                                      \n"
								+ " FROM TMP_SLB_VSERVER                                                    \n"
								+ " WHERE ADC_INDEX = %d AND INDEX NOT IN (                                 \n"
								+ "                    SELECT DISTINCT(B.VS_INDEX)                          \n"
								+ "                    FROM LOG_SVC_PERF_STATS A                      \n"
								+ "                    INNER JOIN TMP_SLB_VS_SERVICE B                      \n"
								+ "                    ON B.INDEX = A.OBJ_INDEX                             \n"
								+ "                    WHERE A.OCCUR_TIME >= %s AND A.ADC_INDEX = %d        \n"
								+ "                    AND ( A.BPS_IN > 0 )                                 \n"
								+ "                    )                                                    \n",
						adcInfo.getIndex(), OBParser.sqlString(beginTime), adcInfo.getIndex());
			} else {
				sqlText = String.format(
						" SELECT NAME, ALTEON_ID, VIRTUAL_IP                                      \n"
								+ " FROM TMP_SLB_VSERVER                                                    \n"
								+ " WHERE ADC_INDEX = %d AND INDEX NOT IN (                                 \n"
								+ "                    SELECT DISTINCT(B.INDEX)                             \n"
								+ "                    FROM LOG_SVC_PERF_STATS A                      \n"
								+ "                    INNER JOIN TMP_SLB_VSERVER B                         \n"
								+ "                    ON B.INDEX = B.OBJ_INDEX                             \n"
								+ "                    WHERE A.OCCUR_TIME >= %s AND A.ADC_INDEX = %d        \n"
								+ "                    AND ( A.BPS_IN > 0 OR A.BPS_OUT >0 )                 \n"
								+ "                    )                                                    \n",
						adcInfo.getIndex(), OBParser.sqlString(beginTime), adcInfo.getIndex());
			}
			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoSlbVServerInfo obj = new OBDtoSlbVServerInfo();
				if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
					obj.setIndex(db.getString(rs, "ALTEON_ID"));
				} else {
					obj.setIndex(db.getString(rs, "NAME"));
				}
				obj.setIpAddress(db.getString(rs, "VIRTUAL_IP"));
				obj.setName(db.getString(rs, "NAME"));
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoADCObject obj = new OBDtoADCObject();
	// obj.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// obj.setIndex(3);
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(12);
	// ArrayList<OBDtoSlbPoolInfo> list = new
	// OBFaultMngImpl().getSleepPoolList(adcInfo, 7, db);
	// System.out.println(list);
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 일정 기간 사용하지 않은 Pool 목록을 추출한다.
	 * 
	 * @param adcInfo
	 * @param sleepDay
	 * @param db
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoSlbPoolInfo> getSleepPoolList(OBDtoAdcInfo adcInfo, Integer sleepDay) throws OBException {
		ArrayList<OBDtoSlbPoolInfo> retVal = new ArrayList<OBDtoSlbPoolInfo>();
		Timestamp currentTime = OBDateTime.toTimestamp(OBDateTime.now());
		Timestamp beginTime = new Timestamp(currentTime.getTime() - (sleepDay) * 24L * 60L * 60L * 1000L);

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (adcInfo.getRegisterTime() != null && (beginTime.getTime() < adcInfo.getRegisterTime().getTime()))
				return retVal;

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				sqlText = String.format(" SELECT NAME, ALTEON_ID, LB_METHOD                              \n"
						+ " FROM TMP_SLB_POOL                                              \n"
						+ " WHERE INDEX IN                                                 \n"
						+ " (                                                              \n"
						+ "    SELECT DISTINCT(POOL_INDEX)                                 \n"
						+ "    FROM TMP_SLB_VS_SERVICE                                     \n"
						+ "    WHERE ADC_INDEX = %d                                        \n"
						+ "    AND POOL_INDEX NOT IN                                       \n"
						+ "    (                                                           \n"
						+ "       SELECT DISTINCT(POOL_INDEX) FROM TMP_SLB_POOLMEMBER      \n"
						+ "       WHERE INDEX IN                                           \n"
						+ "       (                                                        \n"
						+ "           SELECT DISTINCT(MEMBER_INDEX)                        \n" + // 쿼리 인덱스 MEMBER_INDEX로
																									// 수정. junhyun_ok.GS
						"           FROM LOG_SVC_MEMBER_PERF_STATS                       \n"
						+ "           WHERE OCCUR_TIME >= %s AND ADC_INDEX = %d            \n"
						+ "           AND ( BPS_IN > 0 )                                   \n"
						+ "       )                                                        \n"
						+ "    )                                                           \n"
						+ " )                                                              \n", adcInfo.getIndex(),
						OBParser.sqlString(beginTime), adcInfo.getIndex());
			} else {
				sqlText = String.format(" SELECT NAME, ALTEON_ID, LB_METHOD                              \n"
						+ " FROM TMP_SLB_POOL                                              \n"
						+ " WHERE INDEX IN                                                 \n"
						+ " (                                                              \n"
						+ "    SELECT DISTINCT(POOL_INDEX)                                 \n"
						+ "    FROM TMP_SLB_VSERVER                                        \n"
						+ "    WHERE ADC_INDEX = %d                                        \n"
						+ "    AND POOL_INDEX NOT IN                                       \n"
						+ "    (                                                           \n"
						+ "       SELECT DISTINCT(POOL_INDEX) FROM TMP_SLB_POOLMEMBER      \n"
						+ "       WHERE INDEX IN                                           \n"
						+ "       (                                                        \n"
						+ "           SELECT DISTINCT(MEMBER_INDEX)                        \n" + // 쿼리 인덱스 MEMBER_INDEX로
																									// 수정. junhyun_ok.GS
						"           FROM LOG_SVC_MEMBER_PERF_STATS                       \n"
						+ "           WHERE OCCUR_TIME >= %s AND ADC_INDEX = %d            \n"
						+ "           AND ( BPS_IN > 0 )                                   \n"
						+ "       )                                                        \n"
						+ "    )                                                           \n"
						+ " )                                                              \n", adcInfo.getIndex(),
						OBParser.sqlString(beginTime), adcInfo.getIndex());
			}

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoSlbPoolInfo obj = new OBDtoSlbPoolInfo();
				if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
					obj.setIndex(db.getString(rs, "ALTEON_ID"));
				} else {
					obj.setIndex(db.getString(rs, "NAME"));
				}
				obj.setName(db.getString(rs, "NAME"));
				obj.setLbMethod(db.getInteger(rs, "LB_METHOD"));
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	/**
	 * 일정 기간 사용하지 않은 Flb Pool 목록을 추출한다.
	 * 
	 * @param adcInfo
	 * @param sleepDay
	 * @param db
	 * @return
	 * @throws OBException
	 */
	// TODO
	public ArrayList<OBDtoSlbPoolInfo> getSleepFlbPoolList(OBDtoAdcInfo adcInfo, Integer sleepDay) throws OBException {
		ArrayList<OBDtoSlbPoolInfo> retVal = new ArrayList<OBDtoSlbPoolInfo>();
		Timestamp currentTime = OBDateTime.toTimestamp(OBDateTime.now());
		Timestamp beginTime = new Timestamp(currentTime.getTime() - (sleepDay) * 24L * 60L * 60L * 1000L);

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (adcInfo.getRegisterTime() != null && (beginTime.getTime() < adcInfo.getRegisterTime().getTime()))
				return retVal;

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				sqlText = String.format(
						" SELECT NAME, ALTEON_ID, LB_METHOD                              \n"
								+ " FROM TMP_SLB_POOL                                              \n"
								+ " WHERE INDEX IN                         						 \n"
								+ " (                                                              \n"
								+ "    SELECT DISTINCT(GROUP_INDEX)                                \n"
								+ "    FROM MNG_FLB_GROUP                         	             \n"
								+ "    WHERE ADC_INDEX = %d               						 \n"
								+ "    AND GROUP_INDEX NOT IN                                      \n"
								+ "    (                                                           \n"
								+ "           SELECT DISTINCT(GROUP_INDEX)                         \n"
								+ "           FROM LOG_POOLGROUP_PERF_STATS	                     \n"
								+ "           WHERE OCCUR_TIME >= %s AND ADC_INDEX = %d            \n"
								+ "           AND ( BPS_IN > 0 )                                   \n"
								+ "    )                                                           \n"
								+ " )                                                              \n",
						adcInfo.getIndex(), OBParser.sqlString(beginTime), adcInfo.getIndex());
			}

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoSlbPoolInfo obj = new OBDtoSlbPoolInfo();
				if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
					obj.setIndex(db.getString(rs, "ALTEON_ID"));
				} else {
					obj.setIndex(db.getString(rs, "NAME"));
				}
				obj.setName(db.getString(rs, "NAME"));
				obj.setLbMethod(db.getInteger(rs, "LB_METHOD"));
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	/**
	 * 사용중이지 않은 node 목록을 추출한다.
	 * 
	 * @param adcInfo
	 * @param db
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoSlbNodeInfo> getSleepNodeList(OBDtoAdcInfo adcInfo, Integer sleepDay) throws OBException {
		ArrayList<OBDtoSlbNodeInfo> retVal = new ArrayList<OBDtoSlbNodeInfo>();

		Timestamp currentTime = OBDateTime.toTimestamp(OBDateTime.now());
		Timestamp beginTime = new Timestamp(currentTime.getTime() - (sleepDay) * 24L * 60L * 60L * 1000L);

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (adcInfo.getRegisterTime() != null && (beginTime.getTime() < adcInfo.getRegisterTime().getTime()))
				return retVal;
			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				sqlText = String.format(
						" SELECT ALTEON_ID, IP_ADDRESS,  NAME	   					 \n"
								+ " FROM TMP_SLB_NODE                                     		     \n"
								+ " WHERE ADC_INDEX = %d                                   		     \n"
								+ " AND INDEX IN                                       		         \n"
								+ " (                                                      			 \n"
								+ " SELECT DISTINCT(A.NODE_INDEX) FROM TMP_SLB_POOLMEMBER A            \n"
								+ " LEFT JOIN TMP_SLB_POOL B							 		         \n"
								+ "      ON A.POOL_INDEX = B.INDEX								     \n"
								+ " WHERE B.ADC_INDEX = %d 						                     \n"
								+ " AND A.NODE_INDEX NOT IN                                            \n"
								+ "	(                                                                \n"
								+ "		SELECT DISTINCT(REAL_INDEX)                                  \n"
								+ "		FROM LOG_REAL_PERF_STATS		                             \n"
								+ "		WHERE OCCUR_TIME >= %s AND ADC_INDEX = %d                    \n"
								+ "		AND ( BPS_IN > 0 ) 	                                         \n"
								+ "	)																 \n"
								+ " AND A.INDEX IN													 \n"
								+ " 	(																 \n"
								+ "		SELECT DISTINCT(POOLMEMBER_INDEX) 							 \n"
								+ "		FROM TMP_SLB_POOLMEMBER_STATUS								 \n"
								+ "		WHERE ADC_INDEX = %d										 \n"
								+ " 	)	                                                             \n"
								+ " )                                                                  \n",
						adcInfo.getIndex(), adcInfo.getIndex(), OBParser.sqlString(beginTime), adcInfo.getIndex(),
						adcInfo.getIndex());
			} else {
				sqlText = String.format(
						" SELECT ALTEON_ID, IP_ADDRESS, NAME 			 	 \n"
								+ " FROM TMP_SLB_NODE                                     		     \n"
								+ " WHERE ADC_INDEX = %d                                   		     \n"
								+ " AND INDEX IN                                       		         \n"
								+ " (                                                      			 \n"
								+ " SELECT DISTINCT(A.NODE_INDEX) FROM TMP_SLB_POOLMEMBER A            \n"
								+ " LEFT JOIN TMP_SLB_POOL B							 		         \n"
								+ "      ON A.POOL_INDEX = B.INDEX								     \n"
								+ " WHERE B.ADC_INDEX = %d 						                     \n"
								+ " AND A.NODE_INDEX NOT IN                                            \n"
								+ "	(                                                                \n"
								+ "		SELECT DISTINCT(REAL_INDEX)                                  \n"
								+ "		FROM LOG_REAL_PERF_STATS		                             \n"
								+ "		WHERE OCCUR_TIME >= %s AND ADC_INDEX = %d                    \n"
								+ "		AND ( BPS_IN > 0 ) 	                                         \n"
								+ "	)																 \n"
								+ " AND A.INDEX IN													 \n"
								+ " 	(																 \n"
								+ "		SELECT DISTINCT(POOLMEMBER_INDEX) 							 \n"
								+ "		FROM TMP_SLB_POOLMEMBER_STATUS								 \n"
								+ "		WHERE ADC_INDEX = %d										 \n"
								+ " 	)	                                                             \n"
								+ " )                                                                  \n",
						adcInfo.getIndex(), adcInfo.getIndex(), OBParser.sqlString(beginTime), adcInfo.getIndex(),
						adcInfo.getIndex());
			}

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoSlbNodeInfo obj = new OBDtoSlbNodeInfo();
				if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
					obj.setIndex(db.getString(rs, "ALTEON_ID"));
					obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				} else {
					obj.setIndex(db.getString(rs, "IP_ADDRESS"));
				}
				// F5에서 빈 문자열이 null로 되어 있어서 빈문자열로 변경, Alteon은 빈문자열로 들어옴
				obj.setName(db.getString(rs, "NAME"));

				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	// TODO
	public ArrayList<OBDtoSlbNodeInfo> getFlbSleepNodeList(OBDtoAdcInfo adcInfo, Integer sleepDay) throws OBException {
		ArrayList<OBDtoSlbNodeInfo> retVal = new ArrayList<OBDtoSlbNodeInfo>();

		Timestamp currentTime = OBDateTime.toTimestamp(OBDateTime.now());
		Timestamp beginTime = new Timestamp(currentTime.getTime() - (sleepDay) * 24L * 60L * 60L * 1000L);

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			if (adcInfo.getRegisterTime() != null && (beginTime.getTime() < adcInfo.getRegisterTime().getTime()))
				return retVal;

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				sqlText = String.format(
						" SELECT ALTEON_ID, NAME, IP_ADDRESS	   					 \n"
								+ " FROM TMP_SLB_NODE                                     		     \n"
								+ " WHERE ADC_INDEX = %d                                   		     \n"
								+ " AND INDEX IN                                       		         \n"
								+ " (                                                      			 \n"
								+ " SELECT DISTINCT(A.NODE_INDEX) FROM TMP_SLB_POOLMEMBER A            \n"
								+ " LEFT JOIN TMP_SLB_POOL B							 		         \n"
								+ "      ON A.POOL_INDEX = B.INDEX								     \n"
								+ " WHERE B.ADC_INDEX = %d                   							 \n"
								+ "   AND A.NODE_INDEX NOT IN                                          \n"
								+ "	(                                                                \n"
								+ "		SELECT DISTINCT(REAL_INDEX)                                  \n"
								+ "		FROM LOG_REAL_PERF_STATS		                             \n"
								+ " 	 	WHERE OCCUR_TIME >= %s AND ADC_INDEX = %d                    \n"
								+ "		AND ( BPS_IN > 0 ) 		                                     \n"
								+ "	) 																 \n"
								+ "   AND B.INDEX IN													 \n"
								+ "   ( 																 \n"
								+ "	 	SELECT GROUP_INDEX											 \n"
								+ "   	FROM MNG_FLB_GROUP											 \n"
								+ "   	WHERE ADC_INDEX = %d                                         \n"
								+ "   )                                                                \n"
								+ " )                                                                  \n",
						adcInfo.getIndex(), adcInfo.getIndex(), OBParser.sqlString(beginTime), adcInfo.getIndex(),
						adcInfo.getIndex());
			}

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoSlbNodeInfo obj = new OBDtoSlbNodeInfo();
				if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
					obj.setIndex(db.getString(rs, "ALTEON_ID"));
				} else {
					obj.setIndex(db.getString(rs, "NAME"));
				}
				obj.setName(db.getString(rs, "NAME"));
				obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public ArrayList<OBDtoVirtualServiceInfo> getVServiceList(OBDtoADCObject obj) throws OBException {
		try {
			if (obj.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				return getVServiceListAll(obj);
			} else if (obj.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				return getVServiceListGroup(obj);
			} else {
				return getVServiceListAdc(obj);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
	}

	private ArrayList<OBDtoVirtualServiceInfo> getVServiceListAdc(OBDtoADCObject obj) throws OBException {
		ArrayList<OBDtoVirtualServiceInfo> retVal = new ArrayList<OBDtoVirtualServiceInfo>();
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(obj.getIndex());
			if (adcInfo == null)
				return retVal;

			if (adcInfo.getAdcType() == OBDtoAdcInfo.ADC_TYPE_ALTEON) {
				return getVServiceListAdcAlteon(adcInfo);
			} else if (adcInfo.getAdcType() == OBDtoAdcInfo.ADC_TYPE_F5) {
				return getVServiceListAdcF5(adcInfo);
			} else if (adcInfo.getAdcType() == OBDtoAdcInfo.ADC_TYPE_PIOLINK_PAS) {
				return getVServiceListAdcPAS(adcInfo);
			} else if (adcInfo.getAdcType() == OBDtoAdcInfo.ADC_TYPE_PIOLINK_PASK) {
				return getVServiceListAdcPASK(adcInfo);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	private ArrayList<OBDtoVirtualServiceInfo> getVServiceListAdcF5(OBDtoAdcInfo adcInfo) throws OBException {
		ArrayList<OBDtoVirtualServiceInfo> retVal = new ArrayList<OBDtoVirtualServiceInfo>();
		try {
			ArrayList<OBDtoAdcVServerF5> vsInfoList = new OBAdcVServerF5().getVServerListF5(adcInfo.getIndex());

			for (OBDtoAdcVServerF5 vsObj : vsInfoList) {
				OBDtoVirtualServiceInfo obj = new OBDtoVirtualServiceInfo();
				obj.setAdcIndex(adcInfo.getIndex());
				obj.setAdcType(OBDtoAdcInfo.ADC_TYPE_F5);
				obj.setIpAddress(vsObj.getvIP());
				obj.setSvcPort(vsObj.getServicePort());
				obj.setSvcIndex(vsObj.getIndex());
				obj.setSvcName(vsObj.getName());
				retVal.add(obj);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	private ArrayList<OBDtoVirtualServiceInfo> getVServiceListAdcPAS(OBDtoAdcInfo adcInfo) throws OBException {
		ArrayList<OBDtoVirtualServiceInfo> retVal = new ArrayList<OBDtoVirtualServiceInfo>();
		try {
			ArrayList<OBDtoAdcVServerPAS> vsInfoList = new OBAdcVServerPAS().getVServerListPAS(adcInfo.getIndex());

			for (OBDtoAdcVServerPAS vsObj : vsInfoList) {
				OBDtoVirtualServiceInfo obj = new OBDtoVirtualServiceInfo();
				obj.setAdcIndex(adcInfo.getIndex());
				obj.setAdcType(OBDtoAdcInfo.ADC_TYPE_PIOLINK_PAS);
				obj.setIpAddress(vsObj.getvIP());
				obj.setSvcPort(vsObj.getSrvPort());
				obj.setSvcIndex(vsObj.getDbIndex());
				obj.setSvcName(vsObj.getName());
				retVal.add(obj);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	private ArrayList<OBDtoVirtualServiceInfo> getVServiceListAdcPASK(OBDtoAdcInfo adcInfo) throws OBException {
		ArrayList<OBDtoVirtualServiceInfo> retVal = new ArrayList<OBDtoVirtualServiceInfo>();
		try {
			ArrayList<OBDtoAdcVServerPASK> vsInfoList = new OBAdcVServerPASK().getVServerListPASK(adcInfo.getIndex());

			for (OBDtoAdcVServerPASK vsObj : vsInfoList) {
				OBDtoVirtualServiceInfo obj = new OBDtoVirtualServiceInfo();
				obj.setAdcIndex(adcInfo.getIndex());
				obj.setAdcType(OBDtoAdcInfo.ADC_TYPE_PIOLINK_PASK);
				obj.setIpAddress(vsObj.getvIP());
				obj.setSvcPort(vsObj.getSrvPort());
				obj.setSvcIndex(vsObj.getDbIndex());
				obj.setSvcName(vsObj.getName());
				retVal.add(obj);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	private ArrayList<OBDtoVirtualServiceInfo> getVServiceListAdcAlteon(OBDtoAdcInfo adcInfo) throws OBException {
		ArrayList<OBDtoVirtualServiceInfo> retVal = new ArrayList<OBDtoVirtualServiceInfo>();
		try {
			ArrayList<OBDtoAdcVServerAlteon> vsInfoList = new OBAdcVServerAlteon()
					.getVServerListAlteon(adcInfo.getIndex());

			for (OBDtoAdcVServerAlteon vsObj : vsInfoList) {
				ArrayList<OBDtoAdcVService> vserviceList = vsObj.getVserviceList();
				for (OBDtoAdcVService vscvObj : vserviceList) {
					OBDtoVirtualServiceInfo obj = new OBDtoVirtualServiceInfo();
					obj.setAdcIndex(adcInfo.getIndex());
					obj.setAdcType(OBDtoAdcInfo.ADC_TYPE_ALTEON);
					obj.setIpAddress(vsObj.getvIP());
					obj.setSvcPort(vscvObj.getServicePort());
					obj.setSvcIndex(vscvObj.getServiceIndex());
					obj.setSvcName(vsObj.getName());
					retVal.add(obj);
				}
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	private ArrayList<OBDtoVirtualServiceInfo> getVServiceListGroup(OBDtoADCObject obj) throws OBException {
		ArrayList<OBDtoVirtualServiceInfo> retVal = new ArrayList<OBDtoVirtualServiceInfo>();
		try {
			ArrayList<Integer> adcList = getAdcList(obj.getIndex());
			for (Integer adcIndex : adcList) {
				OBDtoADCObject adcObj = new OBDtoADCObject();
				adcObj.setCategory(OBDtoADCObject.CATEGORY_ADC);
				adcObj.setIndex(adcIndex);
				ArrayList<OBDtoVirtualServiceInfo> vscvList = getVServiceListAdc(adcObj);
				retVal.addAll(vscvList);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	private ArrayList<OBDtoVirtualServiceInfo> getVServiceListAll(OBDtoADCObject obj) throws OBException {
		ArrayList<OBDtoVirtualServiceInfo> retVal = new ArrayList<OBDtoVirtualServiceInfo>();
		try {
			ArrayList<Integer> adcList = getAdcListAll(obj.getIndex());
			for (Integer adcIndex : adcList) {
				OBDtoADCObject adcObj = new OBDtoADCObject();
				adcObj.setCategory(OBDtoADCObject.CATEGORY_ADC);
				adcObj.setIndex(adcIndex);
				ArrayList<OBDtoVirtualServiceInfo> vscvList = getVServiceListAdc(adcObj);
				retVal.addAll(vscvList);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	@Override
	public ArrayList<String> getUsedClientIPList() throws OBException {
		String sqlText = "";
		HashMap<String, String> retMap = new HashMap<String, String>();
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT OCCUR_TIME, ACCNT_INDEX, CLIENT_IP                            \n"
					+ " FROM MNG_FAULT_USED_CLIENT_IP                                        \n");

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				String ipAddress = db.getString(rs, "CLIENT_IP");
				retMap.put(ipAddress, ipAddress);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return new ArrayList<String>(retMap.values());
	}

	private void addUsedClientIP(String ipAddress, Integer accntIndex) throws OBException {
		String sqlText = "";
		if (ipAddress == null || ipAddress.isEmpty())
			return;

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT OCCUR_TIME, ACCNT_INDEX, CLIENT_IP                            \n"
							+ " FROM MNG_FAULT_USED_CLIENT_IP                                        \n"
							+ " WHERE CLIENT_IP = %s LIMIT 1                                         \n",
					OBParser.sqlString(ipAddress));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false) {// 추가한다.
				sqlText = String.format(
						" INSERT INTO MNG_FAULT_USED_CLIENT_IP                            \n"
								+ " (OCCUR_TIME, ACCNT_INDEX, CLIENT_IP )                           \n"
								+ " VALUES                                                          \n"
								+ " ( %s, %d, %s )                                                  \n",
						OBParser.sqlString(OBDateTime.toTimestamp(OBDateTime.now())), accntIndex,
						OBParser.sqlString(ipAddress));
				db.executeUpdate(sqlText);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public OBDtoFaultMaxPerfInfo getMaxPerfInfo(OBDtoAdcInfo adcInfo) throws OBException {
		String sqlText = "";
		OBDtoFaultMaxPerfInfo retVal = null;
		if (adcInfo == null)
			return retVal;

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT ADC_TYPE, MODEL, SW_VERSION, MAX_THROUGHPUT, MAX_CONNECTION  \n"
							+ " FROM MNG_FAULT_MAX_PERF_INFO                                        \n"
							+ " WHERE ADC_TYPE = %d AND MODEL LIKE %s LIMIT 1                       \n",
					adcInfo.getAdcType(), OBParser.sqlString(adcInfo.getModel()));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {// 추가한다.
				retVal = new OBDtoFaultMaxPerfInfo();
				retVal.setAdcIndex(adcInfo.getIndex());
				retVal.setMaxConnection(db.getLong(rs, "MAX_CONNECTION"));
				retVal.setMaxThroughput(db.getLong(rs, "MAX_THROUGHPUT"));
				retVal.setModel(db.getString(rs, "MODEL"));
				retVal.setSwVersion(db.getString(rs, "MODEL"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public ArrayList<OBDtoFaultCheckPacketLossInfo> getFaultCheckPacketLossInfo(Long logKey) throws OBException {
		// OBDatabase db = new OBDatabase();
		// try
		// {
		// db.openDB();
		// }
		// catch(OBException e)
		// {
		// throw e;
		// }

		ArrayList<OBDtoFaultCheckPacketLossInfo> retVal = new ArrayList<OBDtoFaultCheckPacketLossInfo>();
		// try
		// {
		// retVal = getFaultCheckPacketLossInfo(logKey, db);
		// }
		// catch(OBException e)
		// {
		// db.closeDB();
		// throw e;
		// }
		// catch(Exception e)
		// {
		// db.closeDB();
		// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		// }
		// db.closeDB();
		return retVal;
	}

	// private ArrayList<OBDtoFaultCheckPacketLossInfo>
	// getFaultCheckPacketLossInfo(Long logKey, OBDatabase db) throws OBException
	// {
	// ArrayList<OBDtoFaultCheckPacketLossInfo> retVal = new
	// ArrayList<OBDtoFaultCheckPacketLossInfo>();
	//
	// String sqlText = "";
	// try
	// {
	// sqlText = String.format(" SELECT LOG_KEY, ADC_INDEX, VS_INDEX, STATUS,
	// SRC_IP, DST_IP, DIRECTION, RCV_TIME, \n" +
	// " SEQ_NO, ACK_NO, DATA_LENGTH, TIME_DIFF, SUMMARY \n" +
	// " FROM LOG_SUMMARY_PKT_LOSS \n" +
	// " WHERE LOG_KEY = %d \n" +
	// " ORDER BY LOG_SEQ ASC \n",
	// logKey);
	//
	// ResultSet rs = db.executeQuery(sqlText);
	// while(rs.next())
	// {// 추가한다.
	// OBDtoFaultCheckPacketLossInfo obj = new OBDtoFaultCheckPacketLossInfo();
	// obj.setiDirection(db.getInteger(rs, "DIRECTION"));
	// obj.setiTimeDiff(db.getInteger(rs, "TIME_DIFF"));
	// obj.setiType(db.getInteger(rs, "STATUS"));
	// obj.setRcvTime(db.getTimestamp(rs, "RCV_TIME"));
	// obj.setSzDstIPAddress(db.getString(rs, "DST_IP"));
	// obj.setSzSrcIPAddress(db.getString(rs, "SRC_IP"));
	// obj.setSzSummary(db.getString(rs, "SUMMARY"));
	// obj.setSeqNo(db.getLong(rs, "SEQ_NO"));
	// obj.setAckNo(db.getLong(rs, "ACK_NO"));
	// obj.setDataLength(db.getInteger(rs, "DATA_LENGTH"));
	// retVal.add(obj);
	// }
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
	// }
	// return retVal;
	// }

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoADCObject obj = new OBDtoADCObject();
	// obj.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// obj.setIndex(1);
	// OBDtoFaultCheckResponseTimeInfo list = new
	// OBFaultMngImpl().getFaultCheckResponseTimeInfo(3584020036234L);
	// System.out.println(list);
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public OBDtoFaultCheckResponseTimeInfo getFaultCheckResponseTimeInfo(Long logKey) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultCheckResponseTimeInfo retVal = null;
		try {
			db.openDB();
			OBDtoFaultCheckLog logInfo = getFaultCheckLogInfo(logKey);
			retVal = getFaultCheckResponseTimeInfo(logKey, logInfo.getAdcIndex(), logInfo.getVsvcIndex(),
					new Timestamp(logInfo.getOccurTime().getTime()), db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private OBDtoFaultCheckResponseTimeInfo getFaultCheckResponseTimeInfo(Long logKey, Integer adcIndex,
			String vsvcIndex, Timestamp occurTime, OBDatabase db) throws OBException {
		OBDtoFaultCheckResponseTimeInfo retVal = new OBDtoFaultCheckResponseTimeInfo();

		try {
			retVal = getFaultSummaryCurrRespTimeInfo(logKey, db);

			OBDtoFaultCheckResponseTimeInfo prevInfo = getPreviousRespTimeInfo(logKey, adcIndex, vsvcIndex, occurTime,
					db);

			if (prevInfo.getAvgTime() > 0)
				retVal.setAvgTime(prevInfo.getAvgTime());
			if (prevInfo.getPrevTime() > 0)
				retVal.setPrevTime(prevInfo.getPrevTime());

			// rate 계산.
			Integer maxValue = 0;
			if (retVal.getCurrTime() > maxValue)
				maxValue = retVal.getCurrTime();
			if (retVal.getAvgTime() > maxValue)
				maxValue = retVal.getAvgTime();
			if (retVal.getPrevTime() > maxValue)
				maxValue = retVal.getPrevTime();

			if (maxValue != 0) {
				retVal.setCurrRate(retVal.getCurrTime() * 100 / maxValue);
				retVal.setAvgRate(retVal.getAvgTime() * 100 / maxValue);
				retVal.setPrevRate(retVal.getPrevTime() * 100 / maxValue);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	// private String makeTimeSqlText(OBDtoSearch searchOption, String columnName)
	// throws OBException
	// {
	// String retVal = "";
	//
	// if(searchOption==null)
	// return retVal;
	//
	// if(searchOption.getToTime()==null)
	// searchOption.setToTime(new Date());
	// retVal=String.format(" %s <= %s ", columnName,
	// OBParser.sqlString(OBDateTime.toString(new
	// Timestamp(searchOption.getToTime().getTime()))));
	//
	// if(searchOption.getFromTime()==null)
	// {
	// searchOption.setFromTime(new
	// Date(searchOption.getToTime().getTime()-7*24*60*60*1000));// 7일전 시간.
	// }
	//
	// retVal+=String.format(" AND %s >= %s ", columnName,
	// OBParser.sqlString(OBDateTime.toString(new
	// Timestamp(searchOption.getFromTime().getTime()))));
	//
	//
	// return retVal;
	// }

	private String makeTimeSqlText(Timestamp endTime, String columnName) throws OBException {
		String retVal = "";

		retVal = String.format(" %s <= %s ", columnName, OBParser.sqlString(OBDateTime.toString(endTime)));

		Date fromTime = new Date(endTime.getTime() - 24 * 60 * 60 * 1000);

		retVal += String.format(" AND %s >= %s ", columnName,
				OBParser.sqlString(OBDateTime.toString(new Timestamp(fromTime.getTime()))));

		return retVal;
	}

	private OBDtoFaultCheckResponseTimeInfo getFaultSummaryCurrRespTimeInfo(Long logKey, OBDatabase db)
			throws OBException {
		OBDtoFaultCheckResponseTimeInfo retVal = new OBDtoFaultCheckResponseTimeInfo();

		String sqlText = "";
		try {
			sqlText = String.format(" SELECT END_POINT_AVG_TIME, END_POINT_MIN_TIME, END_POINT_MAX_TIME, \n"
					+ " DATA_CENTER_AVG_TIME, DATA_CENTER_MIN_TIME, DATA_CENTER_MAX_TIME   \n"
					+ " FROM LOG_SUMMARY_RESP_TIME                                   \n"
					+ " WHERE LOG_KEY = %d                                                 \n", logKey);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {// avg 타임만 구한다.
				retVal.setCurrTime(db.getInteger(rs, "END_POINT_AVG_TIME"));
			}

		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// System.out.println(new
	// OBFaultMngImpl().getPreviousRespTimeInfo(6884492151560L, 6, "6_MONITOR3",
	// null, db));
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	private OBDtoFaultCheckResponseTimeInfo getPreviousRespTimeInfo(Long logKey, Integer adcIndex, String vsvcIndex,
			Timestamp occurTime, OBDatabase db) throws OBException {
		OBDtoFaultCheckResponseTimeInfo retVal = new OBDtoFaultCheckResponseTimeInfo();
		retVal.setPrevTime(0);
		retVal.setAvgTime(0);
		String sqlText = "";
		try {
			sqlText = String.format(
					" SELECT A.OCCUR_TIME, A.END_POINT_AVG_TIME, B.AVG_TIME       \n"
							+ " FROM (                                                      \n"
							+ "       SELECT OCCUR_TIME, END_POINT_AVG_TIME                 \n"
							+ "       FROM LOG_SUMMARY_RESP_TIME                      \n"
							+ "       WHERE ADC_INDEX = %d AND OBJ_INDEX = %s               \n"
							+ "       AND LOG_KEY != %d                                     \n"
							+ "       ORDER BY OCCUR_TIME DESC LIMIT 1                      \n"
							+ "      ) A ,                                                  \n"
							+ "	   (                                                      \n"
							+ "        SELECT AVG(END_POINT_AVG_TIME) AS AVG_TIME           \n"
							+ "        FROM LOG_SUMMARY_RESP_TIME                     \n"
							+ "        WHERE ADC_INDEX = %d AND OBJ_INDEX = %s              \n"
							+ "        AND LOG_KEY != %d                                    \n"
							+ "      ) B                                                    \n"
							+ " ORDER BY A.OCCUR_TIME DESC  LIMIT 1                         \n ",
					adcIndex, OBParser.sqlString(vsvcIndex), logKey, adcIndex, OBParser.sqlString(vsvcIndex), logKey);

			// OBDtoSearch searchOption = new OBDtoSearch();
			// searchOption.setToTime(new Date(occurTime.getTime()));
			//
			// String timeSqlText = makeTimeSqlText(searchOption, "OCCUR_TIME");
			//
			// sqlText = String.format(" SELECT A.OCCUR_TIME, A.RESPONSE_TIME, B.AVG_TIME
			// \n" +
			// " FROM ( \n" +
			// " SELECT OCCUR_TIME, RESPONSE_TIME \n" +
			// " FROM LOG_SVC_PERF_RESP_TIME \n" +
			// " WHERE ADC_INDEX = %d AND OBJ_INDEX = %s \n" +
			// " ORDER BY OCCUR_TIME DESC LIMIT 1 \n" +
			// " ) A , \n" +
			// " ( \n" +
			// " SELECT AVG(RESPONSE_TIME) AS AVG_TIME \n" +
			// " FROM LOG_SVC_PERF_RESP_TIME \n" +
			// " WHERE ADC_INDEX = %d AND OBJ_INDEX = %s AND %s \n" +
			// " ) B \n" +
			// " ORDER BY A.OCCUR_TIME DESC LIMIT 1 \n ",
			// adcIndex,
			// OBParser.sqlString(vsvcIndex),
			// adcIndex,
			// OBParser.sqlString(vsvcIndex),
			// timeSqlText );

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {// 추가한다.
				retVal.setPrevTime(db.getInteger(rs, "END_POINT_AVG_TIME"));
				retVal.setAvgTime(db.getInteger(rs, "AVG_TIME"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	@Override
	public void deleteFaultCheckTemplate(OBDtoFaultCheckTemplate templateObj, OBDtoExtraInfo extraInfo)
			throws OBException {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			deleteFaultCheckTemplate(templateObj, extraInfo, db);
			// 감사로그 생성.
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_FAULT_DELETE_TEMPLATE_SUCCESS, templateObj.getName());
		} catch (OBException e) {// 실패 감사로그 생성.
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_FAULT_DELETE_TEMPLATE_FAIL, templateObj.getName(), e.getErrorMessage());
			throw e;
		} catch (Exception e) {// 실패 감사로그 생성.
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_FAULT_DELETE_TEMPLATE_FAIL, templateObj.getName(),
					OBException.ERRCODE_SYSTEM_ILLEGALNULL);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private void deleteFaultCheckTemplate(OBDtoFaultCheckTemplate templateObj, OBDtoExtraInfo extraInfo, OBDatabase db)
			throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(
					" UPDATE MNG_FAULT_TEMPLATE \n" + " SET STATE=%d              \n" + " WHERE INDEX=%d            \n",
					OBDefine.STATE_DISABLE, templateObj.getIndex());
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoADCObject obj = new OBDtoADCObject();
	// obj.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// obj.setIndex(3);
	// String list = new
	// OBFaultMngImpl().getFaultCheckPacketLossInfoImgFileName(1581705991391L,
	// null);
	// System.out.println(list);
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public String getFaultCheckPacketLossInfoImgFileName(Long logKey, Timestamp loginTime) throws OBException {// 원본 파일을
																												// ./img
																												// 폴더로
																												// 복사 후
																												// 복사한
																												// img
																												// 파일
																												// 이름을
																												// 리턴한다.
																												// return
																												// logKey+".png";aa
		String pngFullPathName = OBDefine.PKT_DUMP_FILE_PATH + logKey + ".png";

		String imgName = logKey + ".png";// "pktlossinfo.png";
		String targetFile = OBDefine.WEB_BASE_DIR + "imgs/" + imgName;
		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("logKey:%d, loginTime:%s", logKey, loginTime));
		try {
			// 이전 파일은 삭제한다.
			OBUtility.fileDelete(targetFile);

			// 파일을 복사한다.
			if (OBUtility.fileCopy(pngFullPathName, targetFile) == false)
				return "";

			return imgName;
		} catch (Exception e) {
			return "";
		}
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoADCObject obj = new OBDtoADCObject();
	// obj.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// obj.setIndex(3);
	// String list = new OBFaultMngImpl().makePacketLossInfoImgFile(6884468864557L);
	// System.out.println(list);
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	// public String makePacketLossInfoImgFile(Long logKey) throws OBException
	// {
	// String retVal = "";//OBDefine.PKT_DUMP_FILE_PATH+logKey+".pcap.tmp.png";
	//
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
	// try
	// {
	// OBDtoFaultCheckLog logInfo = getFaultCheckLogInfo(logKey, db);
	// OBDtoAdcInfo adcInfo = new
	// OBAdcManagementImpl().getAdcInfo(logInfo.getAdcIndex(), db);
	// ArrayList<OBDtoFaultCheckPacketLossInfo> pktInfoList =
	// getFaultCheckPacketLossInfo(logKey, db);
	// String vsvcIPAddress = new
	// OBFaultMonitoringDB().getVSvcIPAddress(logInfo.getAdcIndex(),
	// adcInfo.getAdcType(), logInfo.getVsvcIndex(), db);
	// retVal = makePacketLossInfoImgFile(logKey, logInfo, vsvcIPAddress,
	// pktInfoList);
	// }
	// catch(OBException e)
	// {
	// db.closeDB();
	// throw e;
	// }
	// catch(Exception e)
	// {
	// db.closeDB();
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
	// }
	// db.closeDB();
	// return retVal;
	// }

	@Override
	public boolean isSvcPktdumpFileAvailable(Long logKey) throws OBException {
		String fileName = OBDefine.PKT_DUMP_FILE_PATH + logKey + ".pcap";

		File f = new File(fileName);
		if (f.exists())
			return true;
		return false;
	}

	@Override
	public String getSvcPktdumpFileName(Long logKey) throws OBException {
		if (isSvcPktdumpFileAvailable(logKey) == false)
			return "";

		return OBDefine.PKT_DUMP_FILE_PATH + logKey + ".pcap";
	}

	@Override
	public void deleteFaultCheckScheduleInfo(Long index, OBDtoExtraInfo extraInfo) throws OBException {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			OBDtoFaultCheckSchedule schInfo = getFaultCheckScheduleInfo(index, db);
			deleteFaultCheckScheduleInfo(index, db);

			// 감사로그 생성.
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_FAULT_DELETE_SCHEDULE_SUCCESS, schInfo.getName());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void deleteFaultCheckScheduleInfo(Long index, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(
					" UPDATE MNG_FAULT_SCHEDULE \n" + " SET STATE=%d              \n" + " WHERE INDEX=%d            \n",
					OBDefine.STATE_DISABLE, index);
			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
	}

	@Override
	public Integer getPktdumpInfoListTotalCount(OBDtoADCObject object, OBDtoSearch searchObj) throws OBException {
		OBDatabase db = new OBDatabase();
		Integer recordCount = 0;
		try {
			db.openDB();
			recordCount = getPktdumpInfoListTotalCount(object, searchObj, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}

		return recordCount;
	}

	private Integer getPktdumpInfoListTotalCount(OBDtoADCObject object, OBDtoSearch searchObj, OBDatabase db)
			throws OBException {
		Integer recordCount = 0;
		String sqlText = "";
		try {
			String sqlSearch = "";

			if (searchObj == null)
				throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "invalide param: searchObj");

			String sqlLimit = makeSqlLimitContext(searchObj);

			if (searchObj != null && searchObj.getSearchKey() != null && !searchObj.getSearchKey().isEmpty()) {
				// #3984-2 #2: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchObj.getSearchKey()) + "%";
				sqlSearch = String.format(" (FILE_NAME LIKE %s) ", OBParser.sqlString(wildcardKey));
			}

			String sqlTime = "";
			if (searchObj != null && searchObj.getToTime() == null)
				sqlTime = String.format(" OCCUR_TIME <= %s ", OBParser.sqlString(OBDateTime.now()));
			else
				sqlTime = String.format(" OCCUR_TIME <= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchObj.getToTime().getTime()))));

			if (searchObj != null && searchObj.getFromTime() != null)
				sqlTime += String.format(" AND OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchObj.getFromTime().getTime()))));

			String adcList = getInvolvedAdcIndexList(object, db);

			sqlText = String.format(
					" SELECT COUNT(*) CNT			   				   \n"
							+ " FROM LOG_PKT_DUMP                                \n"
							+ " WHERE ADC_INDEX IN ( %s )                        \n"
							+ " AND STATUS != %d                                 \n",
					adcList, OBDtoPktdumpInfo.STATUS_DELETE);

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			if (!sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;
			sqlText += sqlLimit;

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				recordCount = db.getInteger(rs, "CNT");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return recordCount;
	}

	@Override
	public ArrayList<OBDtoPktdumpInfo> getPktdumpInfoList(OBDtoADCObject object, OBDtoSearch searchObj,
			OBDtoOrdering orderObj) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoPktdumpInfo> retVal = new ArrayList<OBDtoPktdumpInfo>();
		try {
			db.openDB();
			retVal = getPktdumpInfoList(object, searchObj, orderObj, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private ArrayList<OBDtoPktdumpInfo> getPktdumpInfoList(OBDtoADCObject object, OBDtoSearch searchObj,
			OBDtoOrdering orderObj, OBDatabase db) throws OBException {
		ArrayList<OBDtoPktdumpInfo> retVal = new ArrayList<OBDtoPktdumpInfo>();
		String sqlText = "";
		try {
			String sqlSearch = "";

			if (searchObj == null)
				throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "invalide param: searchObj");

			String sqlLimit = makeSqlLimitContext(searchObj);

			if (searchObj != null && searchObj.getSearchKey() != null && !searchObj.getSearchKey().isEmpty()) {
				// #3984-2 #2: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchObj.getSearchKey()) + "%";
				sqlSearch = String.format(" (FILE_NAME LIKE %s) ", OBParser.sqlString(wildcardKey));
			}

			String sqlTime = "";
			if (searchObj != null && searchObj.getToTime() == null)
				sqlTime = String.format(" OCCUR_TIME <= %s ", OBParser.sqlString(OBDateTime.now()));
			else
				sqlTime = String.format(" OCCUR_TIME <= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchObj.getToTime().getTime()))));

			if (searchObj != null && searchObj.getFromTime() != null)
				sqlTime += String.format(" AND OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchObj.getFromTime().getTime()))));

			String adcList = getInvolvedAdcIndexList(object, db);

			sqlText = String.format(
					" SELECT A.LOG_KEY, A.OCCUR_TIME, A.END_TIME, A.ADC_INDEX,  \n"
							+ " A.STATUS, A.FILE_NAME, A.FILE_SIZE, A.OPTION_FILTER,      \n"
							+ " A.OPTION_MAX_PKT, A.OPTION_MAX_TIME, A.OPTION_MAX_SIZE,   \n"
							+ " A.PORT_NAME, B.NAME AS ADC_NAME                           \n"
							+ " FROM LOG_PKT_DUMP     A                                   \n"
							+ " INNER JOIN MNG_ADC    B                                   \n"
							+ " ON B.INDEX = A.ADC_INDEX                                  \n"
							+ " WHERE ADC_INDEX IN ( %s )                                 \n"
							+ " AND A.STATUS != %d                                        \n",
					adcList, OBDtoPktdumpInfo.STATUS_DELETE);

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			if (!sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;
			sqlText += getPktdumpInfoListOrderType(orderObj);
			sqlText += sqlLimit;

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoPktdumpInfo obj = new OBDtoPktdumpInfo();

				obj.setLogIndex(db.getLong(rs, "LOG_KEY"));
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setEndTime(db.getTimestamp(rs, "END_TIME"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setAdcName(db.getString(rs, "ADC_NAME"));
				obj.setFileSize(db.getLong(rs, "FILE_SIZE"));
				obj.setFileName(db.getString(rs, "FILE_NAME"));
				obj.setStrFilter(db.getString(rs, "OPTION_FILTER"));
				obj.setInterfaceName(db.getString(rs, "PORT_NAME"));
				obj.setOptionMaxPkt(db.getInteger(rs, "OPTION_MAX_PKT"));
				obj.setOptionMaxSize(db.getLong(rs, "OPTION_MAX_SIZE"));
				obj.setOptionMaxTime(db.getInteger(rs, "OPTION_MAX_TIME"));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				if (obj.getStatus() == OBDtoPktdumpInfo.STATUS_SUCCESS) {
					obj.setElapsedTime((int) (obj.getEndTime().getTime() - obj.getOccurTime().getTime()));
				}
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	public OBDtoPktdumpInfo getPktdumpInfo(Long indexKey) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT LOG_KEY, OCCUR_TIME, END_TIME, ADC_INDEX,       \n"
							+ " STATUS, FILE_NAME, FILE_SIZE, OPTION_FILTER, PORT_NAME,\n"
							+ " OPTION_MAX_TIME, OPTION_MAX_PKT,  OPTION_MAX_SIZE      \n"
							+ " FROM LOG_PKT_DUMP                                      \n"
							+ " WHERE LOG_KEY = %d                                     \n"
							+ " AND STATUS != %d                                       \n"
							+ " ORDER BY OCCUR_TIME DESC LIMIT 1                       \n",
					indexKey, OBDtoPktdumpInfo.STATUS_DELETE);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				OBDtoPktdumpInfo retVal = new OBDtoPktdumpInfo();
				retVal.setLogIndex(db.getLong(rs, "LOG_KEY"));
				retVal.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				retVal.setEndTime(db.getTimestamp(rs, "END_TIME"));
				retVal.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				retVal.setFileSize(db.getLong(rs, "FILE_SIZE"));
				retVal.setFileName(db.getString(rs, "FILE_NAME"));
				retVal.setStrFilter(db.getString(rs, "OPTION_FILTER"));
				retVal.setInterfaceName(db.getString(rs, "PORT_NAME"));
				retVal.setOptionMaxPkt(db.getInteger(rs, "OPTION_MAX_PKT"));
				retVal.setOptionMaxSize(db.getLong(rs, "OPTION_MAX_SIZE"));
				retVal.setOptionMaxTime(db.getInteger(rs, "OPTION_MAX_TIME"));
				retVal.setStatus(db.getInteger(rs, "STATUS"));
				if (retVal.getStatus() == OBDtoPktdumpInfo.STATUS_SUCCESS) {
					retVal.setElapsedTime((int) (retVal.getEndTime().getTime() - retVal.getOccurTime().getTime()));
				}

				return retVal;
			}
			return null;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public OBDtoPktdumpStatusInfo getPktdumpStatus(Long logKey) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDatabase db2 = new OBDatabase();
		OBDtoPktdumpStatusInfo retVal = null;
		try {
			db.openDB();
			db2.openDB();
			ArrayList<Long> logKeyList = new ArrayList<Long>();
			ArrayList<OBDtoPktdumpStatusInfo> statusList = getPktdumpStatusList(logKeyList, db, db2);
			if (statusList.size() == 0)
				retVal = statusList.get(0);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
			if (db2 != null)
				db2.closeDB();
		}

		return retVal;
	}

	private ArrayList<OBDtoPktdumpStatusInfo> getPktdumpStatusList(ArrayList<Long> logKeyList, OBDatabase db,
			OBDatabase db2) throws OBException {
		ArrayList<OBDtoPktdumpStatusInfo> retVal = new ArrayList<OBDtoPktdumpStatusInfo>();
		String sqlText = "";
		try {
			String subText = "-1";
			for (Long logKey : logKeyList) {
				if (!subText.isEmpty())
					subText += ", ";
				subText += logKey;
			}
			sqlText = String.format(" SELECT  A.LOG_KEY,  A.OCCUR_TIME,  A.END_TIME,  A.ADC_INDEX, \n"
					+ "  A.STATUS,  A.FILE_NAME,  A.FILE_SIZE,                       \n"
					+ " A.ADC_INDEX, B.NAME AS ADC_NAME, B.TYPE AS ADC_TYPE          \n "
					+ " FROM LOG_PKT_DUMP     A                                      \n"
					+ " INNER JOIN MNG_ADC    B                                      \n"
					+ " ON B.INDEX = A.ADC_INDEX                                     \n"
					+ " WHERE LOG_KEY IN ( %s )                                      \n", subText);

			ResultSet rs = db.executeQuery(sqlText);
			Timestamp currTime = OBDateTime.toTimestamp(OBDateTime.now());
			while (rs.next()) {
				OBDtoPktdumpStatusInfo obj = new OBDtoPktdumpStatusInfo();
				obj.setStartTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setEndTime(db.getTimestamp(rs, "END_TIME"));
				obj.setFileSize(db.getLong(rs, "FILE_SIZE"));
				obj.setProgressRate(db.getInteger(rs, "STATUS"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setAdcName(db.getString(rs, "ADC_NAME"));
				obj.setAdcType(db.getInteger(rs, "ADC_TYPE"));
				if (obj.getEndTime() == null)
					// obj.setElapsedTime((int)(currTime.getTime()-obj.getStartTime().getTime()));
					obj.setElapsedTime(((int) (currTime.getTime() - obj.getStartTime().getTime())) / 1000);
				else
					// obj.setElapsedTime((int)(obj.getEndTime().getTime()-obj.getStartTime().getTime()));
					obj.setElapsedTime(((int) (obj.getEndTime().getTime() - obj.getStartTime().getTime())) / 1000);

				obj.setLogKey(db.getLong(rs, "LOG_KEY"));

				// cpu/memory 추출.
				OBDtoADCObject object = new OBDtoADCObject();
				object.setCategory(OBDtoADCObject.CATEGORY_ADC);
				object.setIndex(obj.getAdcIndex());
				OBDtoCpuMemStatus cpuMemStatus = getLastCpuMemoryUsage1SecWithin10Sec(object, db2);
				obj.setLastCpuUsage(cpuMemStatus.getCpu1Usage());
				obj.setLastMemUsage(cpuMemStatus.getMemUsage());
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	public Integer getPktdumpStatusFlag(Long logKey, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(
					" SELECT  STATUS                             \n " + " FROM LOG_PKT_DUMP                          \n"
							+ " WHERE LOG_KEY = %d                         \n",
					logKey);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				return db.getInteger(rs, "STATUS");
			}
			return OBDtoPktdumpInfo.STATUS_FAILURE;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// OBDtoADCObject obj = new OBDtoADCObject();
	// obj.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// obj.setIndex(3);
	// ArrayList<Long> logKeyList = new ArrayList<Long>();
	// logKeyList.add(3585342262214L);
	// ArrayList<OBDtoPktdumpStatusInfo> list = new
	// OBFaultMngImpl().getPktdumpStatus(logKeyList);
	// System.out.println(list);
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }
	//
	@Override
	public ArrayList<OBDtoPktdumpStatusInfo> getPktdumpStatusList(ArrayList<Long> logKeyList) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDatabase db2 = new OBDatabase();
		ArrayList<OBDtoPktdumpStatusInfo> retVal = new ArrayList<OBDtoPktdumpStatusInfo>();
		try {
			db.openDB();
			db2.openDB();
			retVal = getPktdumpStatusList(logKeyList, db, db2);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
			if (db2 != null)
				db2.closeDB();
		}

		return retVal;
	}

	@Override
	public ArrayList<OBDtoObjectIndexInfo> startPktdump(OBDtoADCObject object, OBDtoPktdumpInfo dumpInfo,
			OBDtoExtraInfo extraInfo) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoObjectIndexInfo> retVal = new ArrayList<OBDtoObjectIndexInfo>();
		try {
			db.openDB();
			retVal = startPktdump(object, dumpInfo, extraInfo, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}

		return retVal;
	}

	// private String makePktdumpOptionString(ArrayList<OBDtoPktdumpOption>
	// dumpOption) throws OBException
	// {
	// String retVal = "";
	// if(dumpOption==null)
	// return retVal;
	// for(OBDtoPktdumpOption option:dumpOption)
	// {
	// switch(option.getType())
	// {
	// case OBDtoPktdumpOption.OPTION_TYPE_SRC_IP:
	// if(!retVal.isEmpty())
	// retVal += ", ";
	// retVal += String.format("%s=%s", OBDefine.PKT_OPTION_SRC_IP,
	// option.getContent());
	// break;
	// case OBDtoPktdumpOption.OPTION_TYPE_SRC_PORT:
	// if(!retVal.isEmpty())
	// retVal += ", ";
	// retVal += String.format("%s=%s", OBDefine.PKT_OPTION_SRC_PORT,
	// option.getContent());
	// break;
	// case OBDtoPktdumpOption.OPTION_TYPE_DST_IP:
	// if(!retVal.isEmpty())
	// retVal += ", ";
	// retVal += String.format("%s=%s", OBDefine.PKT_OPTION_DST_IP,
	// option.getContent());
	// break;
	// case OBDtoPktdumpOption.OPTION_TYPE_DST_PORT:
	// if(!retVal.isEmpty())
	// retVal += ", ";
	// retVal += String.format("%s=%s", OBDefine.PKT_OPTION_DST_PORT,
	// option.getContent());
	// break;
	// case OBDtoPktdumpOption.OPTION_TYPE_PROTOCOL:
	// if(!retVal.isEmpty())
	// retVal += ", ";
	// retVal += String.format("%s=%s", OBDefine.PKT_OPTION_PROTOCOL,
	// option.getContent());
	// break;
	// }
	// }
	// return retVal;
	// }

	private ArrayList<OBDtoObjectIndexInfo> startPktdump(OBDtoADCObject object, OBDtoPktdumpInfo dumpInfo,
			OBDtoExtraInfo extraInfo, OBDatabase db) throws OBException {
		ArrayList<OBDtoObjectIndexInfo> newObjectList = new ArrayList<OBDtoObjectIndexInfo>();
		ArrayList<OBDtoObjectIndexInfo> retVal = new ArrayList<OBDtoObjectIndexInfo>();

		try {
			String sqlText = "";
			ArrayList<Integer> adcIndexList = getAdcIndexList(object, db);
			for (Integer adcIndex : adcIndexList) {
				OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
				try {
					// 로그를 저장한다.
					long checkKey = makeCheckKey(adcIndex);

					// 동일 로그가 있으면 삭제한다.
					String sqlTextLatestDelete = String.format(" DELETE FROM LOG_PKT_DUMP WHERE LOG_KEY = %d;",
							checkKey);
					db.executeUpdate(sqlTextLatestDelete);

					ArrayList<OBDtoPktdumpOption> filterOption = dumpInfo.getFilterList();
					String strOption = dumpInfo.toFilterString(filterOption);

					// 장애진단 팝업에서 사용하는 컬럼이 누락됨 임시추가
					sqlText = String.format(
							" INSERT INTO LOG_PKT_DUMP                                     \n"
									+ " (LOG_KEY, OCCUR_TIME, ADC_INDEX,                             \n"
									+ " STATUS, PORT_NAME, FILE_NAME, FILE_SIZE, OPTION_FILTER,      \n"
									+ " OPTION_MAX_PKT, OPTION_MAX_TIME, OPTION_MAX_SIZE)            \n"
									+ " VALUES                                                       \n"
									+ " ( %d, %s, %d, %d, %s, %s, %d, %s, %d, %d, %d )           \n",
							checkKey, OBParser.sqlString(OBDateTime.now()),
							// OBParser.sqlString(OBDateTime.now()),
							adcIndex, 0, OBParser.sqlString(dumpInfo.getInterfaceName()),
							OBParser.sqlString(dumpInfo.getFileName()), 0, OBParser.sqlString(strOption),
							dumpInfo.getOptionMaxPkt(), dumpInfo.getOptionMaxTime(), dumpInfo.getOptionMaxSize());

					db.executeUpdate(sqlText);

					OBDtoObjectIndexInfo retObj = new OBDtoObjectIndexInfo();
					retObj.setLogKey(checkKey);
					retObj.setObj(getADCObjectInfo(OBDtoADCObject.CATEGORY_ADC, adcIndex, db));
					newObjectList.add(retObj);

					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAuditImpl.AUDIT_PKT_DUMP_START_SUCCESS, adcInfo.getName());
				} catch (SQLException e) {
					e.printStackTrace();
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAuditImpl.AUDIT_PKT_DUMP_START_FAIL, adcInfo.getName());
					// throw new OBException(OBErrorCode.SQL_ERROR.getCode(), String.format("msg:%s,
					// sql:%s", OBErrorCode.SQL_ERROR.getCaption(), sqlText));
				} catch (Exception e) {
					e.printStackTrace();
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAuditImpl.AUDIT_PKT_DUMP_START_FAIL, adcInfo.getName());
					// throw new OBException(OBErrorCode.ILLEGAL_NULL_POINTER.getCode(),
					// String.format("msg:%s, detail:%s", OBErrorCode.SQL_ERROR.getCaption(),
					// e.getMessage()));
				}
			}

			// 처리 쓰레드를 실행한다.
			for (OBDtoObjectIndexInfo checkKey : newObjectList) {
				try {
					OBDtoFaultCheckThreadInfo threadInfo = new OBDtoFaultCheckThreadInfo();
					threadInfo.setCheckKey(checkKey.getLogKey());
					threadInfo.setAccntIndex(extraInfo.getAccountIndex());
					threadInfo.setClientIP(extraInfo.getClientIPAddress());
					threadInfo.setAdcIndex(checkKey.getObj().getIndex());

					Runnable r = new OBPktDumpWorker(threadInfo);
					Thread t = new Thread(r);
					t.start();

					retVal.add(checkKey);
				} catch (Exception e) {
					updateDiagnosisStatus(checkKey.getLogKey(), OBDefine.FAULT_CHECK_STATUS_FAILURE, null, null, null,
							null, OBFaultCheckMsg.CHECK_STATUS.getProcessFailMsg(),
							OBException.getExceptionMessage(OBException.ERRCODE_SYSTEM_CREATE_THREAD),
							OBException.getExceptionMessage(OBException.ERRCODE_SYSTEM_CREATE_THREAD));

					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAuditImpl.AUDIT_PKT_DUMP_START_FAIL, getAdcNameByLogKey(checkKey.getLogKey(), db));
				}
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	@Override
	public ArrayList<OBDtoMonL2Ports> getPortInterfaceNameList(OBDtoADCObject object) throws OBException {
		ArrayList<OBDtoMonL2Ports> retVal = new ArrayList<OBDtoMonL2Ports>();
		if (object.getCategory() != OBDtoADCObject.CATEGORY_ADC)
			return retVal;// ADC 단위로 선택되지 않았을 경우에는 포트 정보를 제공하지 않는다.

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());
			retVal = new OBFaultMonitoringImpl().getL2PortsInfo(object.getCategory(), adcInfo.getAdcType(),
					adcInfo.getAdcIpAddress(), adcInfo.getSwVersion(), adcInfo.getSnmpInfo());
			// 패킷 분석에서 Alteon의 경우 MGMT로 패킷 덤프가 실행되지 않아 예외처리
			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON && retVal != null) {
				int mgmtIndex = retVal.size();
				for (int i = 0; i < mgmtIndex; i++) {
					if (retVal.get(i).getPortName().equals("mgmt")) {
						retVal.remove(i);
					}
				}
			}

		} catch (OBException e) {
			throw e;
		} finally {
			if (db != null)
				db.closeDB();
		}

		return retVal;
	}

	@Override
	public void stopPktdump(ArrayList<Long> indexKeyList, OBDtoExtraInfo extraInfo) throws OBException {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			for (Long indexKey : indexKeyList) {
				stopPktdump(indexKey, extraInfo, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private void stopPktdump(Long indexKey, OBDtoExtraInfo extraInfo, OBDatabase db) throws OBException {
		OBDtoPktdumpInfo pktDumpInfo = getPktdumpInfo(indexKey);
		if (pktDumpInfo == null)
			return;

		OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(pktDumpInfo.getAdcIndex());
		try {
			Object CLIObj = openCLIChannel(adcInfo);
			Timestamp nowTime = OBDateTime.toTimestamp(OBDateTime.now());

			// stop flag 설정.
			// updatePktdumpStatus(indexKey, null, null, OBDtoPktdumpInfo.STATUS_STOP, null,
			// null, db);
			// updatePktdumpStopCancelFlag(indexKey, OBDtoPktdumpInfo.STATUS_STOP, db);

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5) {
				// send stop signal to ADC
				((OBAdcF5Handler) CLIObj).cmndStopTcpdump();

				String remoteFileName = indexKey + ".pcap";
				String localFileName = OBDefine.PKT_DUMP_FILE_PATH + remoteFileName;
				boolean isOk = ((OBAdcF5Handler) CLIObj).cmndScpDumpfile(remoteFileName, localFileName);
				if (isOk == false) {// 패킷 덤프에 실패한 경우.
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to rcv pkt dump file(%s)", localFileName));
					updatePktdumpStatus(indexKey, null, nowTime, OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);

					// 14.07.14 sw.jung: 감사로그 추가
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_PKT_DUMP_FAIL, adcInfo.getName());
				} else {// 패킷 수신에 성공한 경우.
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
							String.format("success to rcv pkt dump file(%s)", localFileName));
					// 수신한 패킷의 사이즈 추출.
					String fullPathName = OBDefine.PKT_DUMP_FILE_PATH + remoteFileName;
					Long fileSize = OBUtility.fileLength(fullPathName);
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
							String.format("success to rcv pkt dump file(%s). len:%d", fullPathName, fileSize));
					// updatePktdumpStatus(indexKey, null, nowTime, OBDtoPktdumpInfo.STATUS_SUCCESS,
					// null, fileSize, db);
					updatePktdumpStatus(indexKey, null, null, OBDtoPktdumpInfo.STATUS_STOP, null, fileSize, db);
					updatePktdumpStopCancelFlag(indexKey, OBDtoPktdumpInfo.STATUS_STOP, db);

					// 14.07.14 sw.jung: 감사로그 추가
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_PKT_DUMP_STOP, adcInfo.getName());
				}

				// 수집된 파일을 삭제한다.
				((OBAdcF5Handler) CLIObj).cmndRemoveTcpdumpFile(remoteFileName);
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				((OBAdcAlteonHandler) CLIObj).cmndPktDumpStop();
				// send download signal to ADC
				String localFileName = indexKey + ".pcap";
				OBDtoSystemEnvNetwork envInfo = new OBEnvManagementImpl().getNetworkConfig();
				Integer isOk = ((OBAdcAlteonHandler) CLIObj).cmndPktDumpSend(localFileName, envInfo.getIpAddress());
				if (isOk == 1) {// 패킷 덤프에 실패한 경우. error
					updatePktdumpStatus(indexKey, null, nowTime, OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);

					// 14.07.14 sw.jung: 감사로그 추가
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_PKT_DUMP_FAIL, adcInfo.getName());
				} else if (isOk == 2) {// 패킷 덤프에 실패한 경우. capture in progress
					((OBAdcAlteonHandler) CLIObj).cmndPktDumpStop();
					if (((OBAdcAlteonHandler) CLIObj).cmndPktDumpSend(localFileName, envInfo.getIpAddress()) != 0) {
						updatePktdumpStatus(indexKey, null, nowTime, OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);

						// 14.07.14 sw.jung: 감사로그 추가
						new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
								OBSystemAudit.AUDIT_PKT_DUMP_FAIL, adcInfo.getName());
					} else {// 패킷 수신에 성공한 경우.
						extractPktdumpAlteon(adcInfo, localFileName);
						// 수신한 패킷의 사이즈 추출.
						String fullPathName = OBDefine.PKT_DUMP_FILE_PATH + localFileName;
						Long fileSize = OBUtility.fileLength(fullPathName);
						// updatePktdumpStatus(indexKey, null, nowTime, OBDtoPktdumpInfo.STATUS_SUCCESS,
						// null, fileSize, db);
						updatePktdumpStatus(indexKey, null, null, OBDtoPktdumpInfo.STATUS_STOP, null, fileSize, db);
						updatePktdumpStopCancelFlag(indexKey, OBDtoPktdumpInfo.STATUS_STOP, db);

						// 14.07.14 sw.jung: 감사로그 추가
						new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
								OBSystemAudit.AUDIT_PKT_DUMP_STOP, adcInfo.getName());
					}
				} else if (isOk == 3) {// 패킷 덤프에 실패한 경우. scp transfer in progress
					int i = 0;
					for (i = 0; i < 30; i++) {
						OBDateTime.Sleep(1000);
						if (((OBAdcAlteonHandler) CLIObj).cmndPktDumpSend(localFileName, envInfo.getIpAddress()) == 0) {
							break;
						}
					}
					if (i == 30)
						updatePktdumpStatus(indexKey, null, nowTime, OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
					else {// 패킷 수신에 성공한 경우.
						extractPktdumpAlteon(adcInfo, localFileName);
						// 수신한 패킷의 사이즈 추출.
						String fullPathName = OBDefine.PKT_DUMP_FILE_PATH + localFileName;
						Long fileSize = OBUtility.fileLength(fullPathName);
						// updatePktdumpStatus(indexKey, null, nowTime, OBDtoPktdumpInfo.STATUS_SUCCESS,
						// null, fileSize, db);
						updatePktdumpStatus(indexKey, null, null, OBDtoPktdumpInfo.STATUS_STOP, null, fileSize, db);
						updatePktdumpStopCancelFlag(indexKey, OBDtoPktdumpInfo.STATUS_STOP, db);
					}

					// 14.07.14 sw.jung: 감사로그 추가
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_PKT_DUMP_FAIL, adcInfo.getName());
				} else {// 패킷 수신에 성공한 경우.
					extractPktdumpAlteon(adcInfo, localFileName);
					// 수신한 패킷의 사이즈 추출.
					String fullPathName = OBDefine.PKT_DUMP_FILE_PATH + localFileName;
					Long fileSize = OBUtility.fileLength(fullPathName);
					// updatePktdumpStatus(indexKey, null, nowTime, OBDtoPktdumpInfo.STATUS_SUCCESS,
					// null, fileSize, db);
					updatePktdumpStatus(indexKey, null, null, OBDtoPktdumpInfo.STATUS_STOP, null, fileSize, db);
					updatePktdumpStopCancelFlag(indexKey, OBDtoPktdumpInfo.STATUS_STOP, db);

					// 14.07.14 sw.jung: 감사로그 추가
					new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
							OBSystemAudit.AUDIT_PKT_DUMP_STOP, adcInfo.getName());
				}
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS) {

			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {

			}

			closeCLIChannel(adcInfo, CLIObj);
		} catch (OBException e) {
			// 14.07.14 sw.jung: 감사로그 추가
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_PKT_DUMP_FAIL, adcInfo.getName());
			e.printStackTrace();
			// throw e;
		}
	}

	public void updatePktdumpStatus(Long logKey, Timestamp occurTime, Timestamp endTime, Integer status,
			String fileName, Long fileSize, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			String subText = "";
			if (occurTime != null) {
				if (!subText.isEmpty())
					subText += ", ";
				subText += String.format(" OCCUR_TIME=%s ", OBParser.sqlString(occurTime));
			}
			if (endTime != null) {
				if (!subText.isEmpty())
					subText += ", ";
				subText += String.format(" END_TIME=%s ", OBParser.sqlString(endTime));
			}
			if (status != null) {
				if (!subText.isEmpty())
					subText += ", ";
				subText += String.format(" STATUS=%d ", status);
			}
			if (fileName != null) {
				if (!subText.isEmpty())
					subText += ", ";
				subText += String.format(" FILE_NAME=%s ", OBParser.sqlString(fileName));
			}
			if (fileSize != null) {
				if (!subText.isEmpty())
					subText += ", ";
				subText += String.format(" FILE_SIZE=%d ", fileSize);
			}

			if (subText.isEmpty())
				return;
			sqlText = String.format(
					" UPDATE LOG_PKT_DUMP   \n" + " SET %s                \n" + " WHERE LOG_KEY=%d      \n", subText,
					logKey);

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
	}

	public void updatePktdumpStopCancelFlag(Long logKey, Integer status, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(" UPDATE LOG_PKT_DUMP        \n" + " SET CANCEL_STOP_FLAGE= %d  \n"
					+ " WHERE LOG_KEY=%d           \n", status, logKey);

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(3);
	// Object obj = new OBFaultMngImpl().openCLIChannel(adcInfo);
	// new OBFaultMngImpl().closeCLIChannel(adcInfo, obj);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public Object openCLIChannel(OBDtoAdcInfo adcInfo) throws OBException {
		Object retVal = null;
		try {
			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				OBAdcAlteonHandler channel = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
				channel.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
						adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
				channel.login();
				retVal = channel;
				return retVal;
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5) {
				OBAdcF5Handler channel = OBCommon.getValidF5Handler(adcInfo.getSwVersion());
				channel.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcCliAccount(),
						adcInfo.getAdcCliPasswordDecrypt(), adcInfo.getConnPort());
				channel.sshLogin();
				retVal = channel;
				return retVal;
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS) {
				OBAdcPASHandler channel = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
				channel.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcCliAccount(),
						adcInfo.getAdcCliPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
				channel.login();
				retVal = channel;
				return retVal;
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {
				OBAdcPASKHandler channel = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
				channel.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcCliAccount(),
						adcInfo.getAdcCliPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
				channel.login();
				retVal = channel;
				return retVal;
			}
		} catch (OBExceptionUnreachable e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, adcInfo.getAdcIpAddress());
		} catch (OBExceptionLogin e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, adcInfo.getAdcIpAddress());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return null;
	}

	public void closeCLIChannel(OBDtoAdcInfo adcInfo, Object obj) {
		if (obj == null)
			return;
		try {
			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				OBAdcAlteonHandler channel = (OBAdcAlteonHandler) obj;
				channel.disconnect();
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5) {
				OBAdcF5Handler channel = (OBAdcF5Handler) obj;
				channel.disconnect();
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS) {
				OBAdcPASHandler channel = (OBAdcPASHandler) obj;
				channel.disconnect();
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {
				OBAdcPASKHandler channel = (OBAdcPASKHandler) obj;
				channel.disconnect();
			}
		} catch (Exception e) {

		}
	}

	@Override
	public void delPktDumpLog(ArrayList<Long> indexKeyList, OBDtoExtraInfo extraInfo) throws OBException {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			delPktDumpLog(indexKeyList, extraInfo, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private void delPktDumpLog(ArrayList<Long> indexKeyList, OBDtoExtraInfo extraInfo, OBDatabase db)
			throws OBException {
		// 14.07.14 sw.jung: 감사로그 추가: 삭제 파일명 추출
		String pktDumpNames = "";

		if (indexKeyList == null || indexKeyList.size() == 0)
			return;

		for (long indexKey : indexKeyList) {

			pktDumpNames += OBDefine.PKT_DUMP_FILE_PATH + indexKey + ".pcap, ";
		}

		String sqlText = "";

		try {
			String adcList = "-1";
			for (Long indexKey : indexKeyList) {
				if (!adcList.isEmpty())
					adcList += ", ";
				adcList += indexKey;

			}

			sqlText = String.format(" UPDATE LOG_PKT_DUMP               \n" + " SET STATUS=%d                     \n"
					+ " WHERE LOG_KEY IN ( %s )         \n", OBDtoPktdumpInfo.STATUS_DELETE, adcList);

			db.executeUpdate(sqlText);

			// 14.07.14 sw.jung: 감사로그 추가
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_PKT_DUMP_DELETE_SUCCESS,
					pktDumpNames.substring(0, pktDumpNames.lastIndexOf(", ")));
		} catch (SQLException e) {
			// 14.07.14 sw.jung: 감사로그 추가
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_PKT_DUMP_DELETE_FAIL,
					pktDumpNames.substring(0, pktDumpNames.lastIndexOf(", ")));
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			// 14.07.14 sw.jung: 감사로그 추가
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_PKT_DUMP_DELETE_FAIL,
					pktDumpNames.substring(0, pktDumpNames.lastIndexOf(", ")));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}

	}

	@Override
	public boolean isPktdumpFileAvailable(Long logKey) throws OBException {
		String fileName = OBDefine.PKT_DUMP_FILE_PATH + logKey + ".pcap";

		File f = new File(fileName);
		if (f.exists())
			return true;
		return false;
	}

	@Override
	public String getPktdumpFileName(Long logKey) throws OBException {// 파일 이름 규칙은 logKey+확장자 이다.
		if (isSvcPktdumpFileAvailable(logKey) == false)
			return "";

		return OBDefine.PKT_DUMP_FILE_PATH + logKey + ".pcap";
	}

	@Override
	public void cancelPktdump(ArrayList<Long> indexKeyList, OBDtoExtraInfo extraInfo) throws OBException {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			for (Long indexKey : indexKeyList) {
				cancelPktdump(indexKey, extraInfo, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private void cancelPktdump(Long indexKey, OBDtoExtraInfo extraInfo, OBDatabase db) throws OBException {
		OBDtoPktdumpInfo pktDumpInfo = getPktdumpInfo(indexKey);
		if (pktDumpInfo == null)
			return;

		OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(pktDumpInfo.getAdcIndex());
		try {
			Object CLIObj = openCLIChannel(adcInfo);

			// stop flag 설정.
			updatePktdumpStatus(indexKey, null, null, OBDtoPktdumpInfo.STATUS_CANCEL, null, null, db);
			updatePktdumpStopCancelFlag(indexKey, OBDtoPktdumpInfo.STATUS_CANCEL, db);
			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5) {
				// send stop signal to ADC
				((OBAdcF5Handler) CLIObj).cmndStopTcpdump();
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				((OBAdcAlteonHandler) CLIObj).cmndPktDumpStop();
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS) {

			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {

			}

			closeCLIChannel(adcInfo, CLIObj);

			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_PKT_DUMP_CANCEL_SUCCESS, adcInfo.getName());
		} catch (OBException e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_PKT_DUMP_CANCEL_FAIL, adcInfo.getName());
			e.printStackTrace();
			// throw e;
		}
	}

	private Integer getMajorVersion(String swVersion) {
		String[] verElements = swVersion.split("\\.", 2); // 버전 요소 두개까지 문자열로 건진다. 지금은 하나만 쓴다.
		return Integer.parseInt(verElements[0]);
	}

	public void extractPktdumpAlteon(OBDtoAdcInfo adcInfo, String localFileName) throws OBException {
		try {
			// 28버전 이상이면 압축을 해제해야 한다.
			Integer majorVersion = getMajorVersion(adcInfo.getSwVersion());

			if (majorVersion.intValue() >= 28) {// tar.gz 형태를 압축 해제 한다.
				String cmndExtract = String
						.format("tar -zxvf /var/lib/adcsmart/pcap/%s.tar.gz -C /var/lib/adcsmart/pcap", localFileName);
				String srcName = OBDefine.PKT_DUMP_FILE_PATH + "pkt.pcap";
				String dstName = OBDefine.PKT_DUMP_FILE_PATH + localFileName;
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("extract file:%s", cmndExtract));
				Runtime.getRuntime().exec(cmndExtract).waitFor();

				OBUtility.fileMove(srcName, dstName);
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("move file:src:%s, dst:%s", srcName, dstName));
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
	}

//    public static void main(String[] args)
//    {
//        try
//        {
//            OBDatabase db = new OBDatabase();
//            db.openDB();
//            OBDtoADCObject obj = new OBDtoADCObject();
//            obj.setCategory(OBDtoADCObject.CATEGORY_ADC);
//            obj.setIndex(6);
//            // OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(3);
//            ArrayList<OBDtoVlanInfo> list = new OBFaultMngImpl().getVlanInterfaceNameList(obj, db);
//            System.out.println(list);
//            db.closeDB();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

	@Override
	public ArrayList<OBDtoVlanInfo> getVlanInterfaceNameList(OBDtoADCObject object) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoVlanInfo> retVal = new ArrayList<OBDtoVlanInfo>();
		try {
			db.openDB();
			retVal = getVlanInterfaceNameList(object, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	public ArrayList<OBDtoVlanInfo> getVlanInterfaceNameList(OBDtoADCObject object, OBDatabase db) throws OBException {
		ArrayList<OBDtoVlanInfo> retVal = new ArrayList<OBDtoVlanInfo>();
		if (object.getCategory() != OBDtoADCObject.CATEGORY_ADC)
			return retVal;// ADC 단위로 선택되지 않았을 경우에는 포트 정보를 제공하지 않는다.

		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());
			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5) {
				return getVlanInterfaceNameListF5(adcInfo);
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				return getVlanInterfaceNameListAlteon(adcInfo);
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS) {
				return getVlanInterfaceNameListPAS(adcInfo);
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {
				return getVlanInterfaceNameListPASK(adcInfo);
			}
		} catch (OBException e) {
			throw e;
		}
		return retVal;
	}

	private ArrayList<OBDtoVlanInfo> getVlanInterfaceNameListAlteon(OBDtoAdcInfo adcInfo) throws OBException {
		ArrayList<OBDtoVlanInfo> retVal = new ArrayList<OBDtoVlanInfo>();

		return retVal;
	}

	private ArrayList<OBDtoVlanInfo> getVlanInterfaceNameListPAS(OBDtoAdcInfo adcInfo) throws OBException {
		ArrayList<OBDtoVlanInfo> retVal = new ArrayList<OBDtoVlanInfo>();

		return retVal;
	}

	private ArrayList<OBDtoVlanInfo> getVlanInterfaceNameListPASK(OBDtoAdcInfo adcInfo) throws OBException {
		ArrayList<OBDtoVlanInfo> retVal = new ArrayList<OBDtoVlanInfo>();

		return retVal;
	}

	private ArrayList<OBDtoVlanInfo> getVlanInterfaceNameListF5(OBDtoAdcInfo adcInfo) throws OBException {
		ArrayList<OBDtoVlanInfo> retVal = new ArrayList<OBDtoVlanInfo>();

		try {
			OBSnmpF5 snmpHandler = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());
			HashMap<String, String> vlanMap = snmpHandler.getVlanInterfaceName(adcInfo.getAdcType(),
					adcInfo.getSwVersion());

			ArrayList<String> vlanList = new ArrayList<String>(vlanMap.keySet());
			for (String name : vlanList) {
				OBDtoVlanInfo info = new OBDtoVlanInfo();
				info.setName(name);

				retVal.add(info);
			}
		} catch (OBException e) {
			throw e;
		}
		return retVal;
	}

	public String getFaultAdcName(Long logKey) throws OBException {
		OBDatabase db = new OBDatabase();

		String retVal = "";
		String sqlText = "";

		try {
			db.openDB();
			// adc index를 먼저 추출한다.
			sqlText = String.format(" SELECT ADC_NAME    \n" + " FROM LOG_FAULT                      \n"
					+ " WHERE LOG_KEY=%d                    \n", logKey);
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return null;
			}

			// OBDtoAdcInfo adc = new OBDtoAdcInfo();
			// adc.setName(db.getString(rs, "ADC_NAME"));
			//
			// return adc;

			retVal = db.getString(rs, "ADC_NAME");

			return retVal;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

}
