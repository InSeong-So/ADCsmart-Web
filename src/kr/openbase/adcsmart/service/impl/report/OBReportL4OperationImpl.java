package kr.openbase.adcsmart.service.impl.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.OBReportL4Operation;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcName;
import kr.openbase.adcsmart.service.dto.OBDtoUsageThroughput;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptL4ConfigChange;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptL4PerformanceInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptL4PerformanceSummary;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptL4PerformanceTrend;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptL4SlbConfigChangeSummary;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptTitle;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBReportL4OperationImpl implements OBReportL4Operation {

//	public static void main(String[] args) throws OBException
//	{
////		OBDtoRptL4PerformanceSummary summary = new OBReportL4OperationImpl().getPerformanceInfo("1361499108159", 1);
//		
//		OBDtoRptL4SlbConfigChangeSummary config = new OBReportL4OperationImpl().getSlbConfigChangeInfo("1361499108159", 1); 
//		System.out.println(config);
//	}

	@Override
	public OBDtoRptTitle getTitle(String rptIndex) throws OBException {
		try {
			OBReportInfo rptInfo = new OBReportImpl().getReportInfo(rptIndex);
			OBDtoRptTitle result = new OBDtoRptTitle();
			result.setIndex(rptInfo.getIndex());
			result.setAdcList(rptInfo.getAdcList());
			result.setBeginTime(rptInfo.getBeginTime());
			result.setEndTime(rptInfo.getEndTime());
			result.setOccurTime(rptInfo.getOccurTime());
			result.setUserIndex(rptInfo.getAccountIndex());
			result.setUserID(rptInfo.getAccountID());
			return result;
		} catch (OBException e) {
			throw e;// new OBException(OBException.ERRCODE_REPORT_GETTITLE, e.getErrorMessage());
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
	}

	private ArrayList<Integer> calTargetAdcIndex(OBReportInfo rptInfo, Integer accntIndex) throws OBException {
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		// TODO targetObject 을 이용해서 adcList를 구성하는 부분 추가 필요.

		for (OBDtoAdcName adcName : rptInfo.getAdcList()) {
			retVal.add(adcName.getIndex());
		}
		return retVal;
	}

	private ArrayList<OBDtoRptL4ConfigChange> getConfigChangeList(ArrayList<Integer> adcIndexList, String searchKey,
			Date beginTime, Date endTime) throws OBException {
		ArrayList<OBDtoRptL4ConfigChange> retVal = new ArrayList<OBDtoRptL4ConfigChange>();

		if (adcIndexList.size() == 0)
			return retVal;

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			sqlText = String.format(" SELECT B.NAME, B.IPADDRESS, C.ID, A.VS_NAME, A.VS_IP,A.OCCUR_TIME, A.SUMMARY \n"
					+ " FROM LOG_CONFIG_HISTORY A                                                    \n"
					+ " LEFT JOIN MNG_ADC       B                                                    \n"
					+ " ON A.ADC_INDEX = B.INDEX                                                     \n"
					+ " LEFT JOIN MNG_ACCNT C                                                        \n"
					+ " ON C.INDEX = A.ACCNT_INDEX                                                   \n");

			String adcList = "";
			for (Integer adcIndex : adcIndexList) {
				if (false == adcList.isEmpty())
					adcList += ", " + adcIndex;
				else
					adcList += adcIndex;
			}

			sqlText += String.format("WHERE A.ADC_INDEX IN ( %s ) ", adcList);

			String wildcardKey = "";
			String whereClauseKey = "";
			String whereClauseTime = "";

			if (searchKey != null && !searchKey.isEmpty()) {
				wildcardKey = OBParser.sqlString("%" + searchKey + "%");
				whereClauseKey = String.format(" AND (A.VS_NAME LIKE %s OR A.VS_IP LIKE %s OR A.SUMMARY LIKE %s) ",
						wildcardKey, wildcardKey, wildcardKey);
			}

			if (beginTime != null) {
				whereClauseTime = String.format(" AND A.OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));
				if (endTime != null) {
					whereClauseTime += String.format(" AND A.OCCUR_TIME <= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));
				}
			} else {
				if (endTime != null) {
					whereClauseTime += String.format(" AND A.OCCUR_TIME <= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));
				}
			}

			sqlText += String.format("%s \n" + " %s \n" + " ORDER BY A.OCCUR_TIME DESC ;", whereClauseKey,
					whereClauseTime);

			ResultSet rs;

			db.openDB();
			rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoRptL4ConfigChange obj = new OBDtoRptL4ConfigChange();

				obj.setAdcIPAddress(db.getString(rs, "IPADDRESS"));
				obj.setAdcName(db.getString(rs, "NAME"));
				obj.setContents(db.getString(rs, "SUMMARY"));
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				String userID = db.getString(rs, "ID");
//                if(userID == null || !userID.isEmpty() || userID.equals(0))
				if (userID == null || userID.isEmpty()) {
					obj.setUserID("System");
				} else {
					obj.setUserID(userID);
				}

				obj.setVsIPAddress(db.getString(rs, "VS_IP"));
				obj.setVsName(db.getString(rs, "VS_NAME"));

				retVal.add(obj);
			}

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. list count = %d", retVal.size()));
			return retVal;
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

	private Integer getConfigChangeTotalCount(ArrayList<Integer> adcIndexList, String searchKey, Date beginTime,
			Date endTime) throws OBException {
		Integer retVal = 0;

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			sqlText = " SELECT COUNT(OCCUR_TIME) AS COUNT  " + " FROM LOG_CONFIG_HISTORY A ";

			String adcList = "";
			for (Integer adcIndex : adcIndexList) {
				if (false == adcList.isEmpty())
					adcList += ", " + adcIndex;
				else
					adcList += adcIndex;
			}

			sqlText += String.format("WHERE A.OBJECT_TYPE = %d AND A.ADC_INDEX IN ( %s ) ",
					OBDefine.HISTORY_OBJECT_VIRTUALSERVER, adcList);

			String wildcardKey = "";
			String whereClauseKey = "";
			String whereClauseTime = "";

			if (searchKey != null && !searchKey.isEmpty()) {
				wildcardKey = OBParser.sqlString("%" + searchKey + "%");
				whereClauseKey = String.format(" AND (A.VS_NAME LIKE %s OR A.VS_IP LIKE %s OR A.SUMMARY LIKE %s) ",
						wildcardKey, wildcardKey, wildcardKey);
			}

			if (beginTime != null) {
				whereClauseTime = String.format(" AND A.OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));
				if (endTime != null) {
					whereClauseTime += String.format(" AND A.OCCUR_TIME <= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));
				}
			} else {
				if (endTime != null) {
					whereClauseTime += String.format(" AND A.OCCUR_TIME <= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));
				}
			}

			sqlText += String.format("%s \n" + " %s \n", whereClauseKey, whereClauseTime);

			ResultSet rs;

			db.openDB();
			rs = db.executeQuery(sqlText);
			if (rs.next()) {
				retVal = db.getInteger(rs, "COUNT");
			}

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. list count = %d", retVal));
			return retVal;
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
	public OBDtoRptL4SlbConfigChangeSummary getSlbConfigChangeInfo(String rptIndex, Integer accntIndex)
			throws OBException {
		OBDtoRptL4SlbConfigChangeSummary retVal = new OBDtoRptL4SlbConfigChangeSummary();

		try {
			OBReportInfo rptInfo = new OBReportImpl().getReportInfo(rptIndex);

			ArrayList<Integer> adcList = calTargetAdcIndex(rptInfo, accntIndex);

			// 설정 변경 이력을 조회한다.
			ArrayList<OBDtoRptL4ConfigChange> configChangeList = getConfigChangeList(adcList, "",
					rptInfo.getBeginTime(), rptInfo.getEndTime());
			retVal.setConfigChangeList(configChangeList);

			// 설정 변경 이력 개수를 조회한다.
			Integer totalCount = getConfigChangeTotalCount(adcList, "", rptInfo.getBeginTime(), rptInfo.getEndTime());
			retVal.setTotalLogCount(totalCount);
		} catch (OBException e) {
			throw e;
		}
		return retVal;
	}

	@Override
	public OBDtoRptL4PerformanceSummary getPerformanceInfo(String rptIndex, Integer accntIndex) throws OBException {
		OBDtoRptL4PerformanceSummary retVal = new OBDtoRptL4PerformanceSummary();

		try {
			OBReportInfo rptInfo = new OBReportImpl().getReportInfo(rptIndex);

			ArrayList<Integer> adcList = calTargetAdcIndex(rptInfo, accntIndex);

			ArrayList<OBDtoRptL4PerformanceInfo> top10ThroughputList = getTop10ThroughputInfoList(adcList, "",
					rptInfo.getBeginTime(), rptInfo.getEndTime());
			retVal.setTop10ThroughputList(top10ThroughputList);
			ArrayList<OBDtoRptL4PerformanceInfo> top10ConnectionList = getTop10ConnectionInfoList(adcList, "",
					rptInfo.getBeginTime(), rptInfo.getEndTime());
			retVal.setTop10ConnectionList(top10ConnectionList);
		} catch (OBException e) {
			throw e;
		}
		return retVal;
	}

	private Integer calcTimeInterval(Date startTime, Date endTime) {
		if (null == endTime) {
			// 현재 시간으로 치환함.
			endTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now()));
		}

		if (null == startTime) {
			// 현재 시간으로 치환함.
			Timestamp yesterDay = new Timestamp(OBDateTime.toTimestamp(OBDateTime.now()).getTime() - 24 * 3600 * 1000);// millisecond
																														// 단위.
			startTime = OBDateTime.toDate(yesterDay);
		}
		long diff = Math.abs(endTime.getTime() - startTime.getTime());
		Integer retVal = 0;
		if (diff <= 24 * 3600 * 1000) {// 일간 보고서 의미.
			retVal = 0;
		} else if (diff <= 7 * 24 * 3600 * 1000) {// 주간 보고서 의미.
			retVal = 1;
		} else {// 월간 보고서 의미.
			retVal = 2;
		}
		return retVal;//
	}

//	private OBDtoRptVServiceInfo getVServiceInfo(Integer adcType, String vsrvIndex, OBDatabase db) throws OBException
//	{
//		OBDtoRptVServiceInfo retVal = new OBDtoRptVServiceInfo();
//		
//		String sqlText="";
//		
//		try
//		{
//			// TOP 10 리스트 추출.
//			if(OBDefine.ADC_TYPE_F5 == adcType)
//			{
//				sqlText =String.format(" SELECT A.INDEX, A.NAME, A.TYPE, B.VIRTUAL_IP, B.NAME AS VIRTUAL_NAME, B.VIRTUAL_PORT " +
//										 " FROM MNG_ADC A " +
//										 " INNER JOIN TMP_SLB_VSERVER B " +
//										 " ON B.ADC_INDEX = A.INDEX " +
//										 " WHERE B.INDEX=%s AND A.TYPE=%d ",
//										 OBParser.sqlString(vsrvIndex),
//										 OBDefine.ADC_TYPE_F5);
//			}
//			else if(OBDefine.ADC_TYPE_ALTEON == adcType)
//			{
//				sqlText =String.format(" SELECT A.INDEX, A.NAME, A.TYPE, B.VIRTUAL_IP, B.NAME AS VIRTUAL_NAME, C.VIRTUAL_PORT " +
//										 " FROM MNG_ADC A " +
//										 " INNER JOIN TMP_SLB_VSERVER B " +
//										 " ON B.ADC_INDEX = A.INDEX " +
//										 " INNER JOIN TMP_SLB_VS_SERVICE C " +
//										 " ON B.INDEX = C.VS_INDEX " +
//										 " WHERE C.INDEX=%s AND A.TYPE=%d ",
//										 OBParser.sqlString(vsrvIndex),
//										 OBDefine.ADC_TYPE_ALTEON);
//			}
//			else if(OBDefine.ADC_TYPE_PIOLINK_PAS == adcType)
//			{
//				sqlText =String.format(" SELECT A.INDEX, A.NAME, A.TYPE, B.VIRTUAL_IP, B.NAME AS VIRTUAL_NAME, B.VIRTUAL_PORT " +
//										 " FROM MNG_ADC A " +
//										 " INNER JOIN TMP_SLB_VSERVER B " +
//										 " ON B.ADC_INDEX = A.INDEX " +
//										 " WHERE B.INDEX=%s AND A.TYPE=%d ",
//										 OBParser.sqlString(vsrvIndex),
//										 OBDefine.ADC_TYPE_F5);
//			}			
//			else
//			{
//				throw new OBException(OBException.ERRCODE_ADC_VENDOR);
//			}
//	
//			ResultSet rs = db.executeQuery(sqlText);
//			if(rs.next())
//			{
//				retVal.setAdcIndex(db.getInteger(rs, "INDEX"));
//				retVal.setAdcName(db.getString(rs, "NAME"));
//				retVal.setAdcType(db.getInteger(rs, "TYPE"));
//				retVal.setVsIPAddress(db.getString(rs, "VIRTUAL_IP"));
//				retVal.setVsName(db.getString(rs, "VIRTUAL_NAME"));
//				retVal.setVsPort(db.getInteger(rs, "VIRTUAL_PORT"));
//				return retVal;
//			}
//			
//			return null;
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

	private ArrayList<OBDtoRptL4PerformanceInfo> getTop10ThroughputInfoList(ArrayList<Integer> adcIndexList,
			String searchKey, Date beginTime, Date endTime) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adc list:%s, search:%s", adcIndexList, searchKey));

		ArrayList<OBDtoRptL4PerformanceInfo> retVal = new ArrayList<OBDtoRptL4PerformanceInfo>();

		if (adcIndexList.size() == 0)
			return retVal;

		String sqlText = "";

//		String vsLogTableName = "LOG_ADC_VS_RESC";
//		String vsvsLogTalbeName = "LOG_SVC_PERF_STATS";
//		
//		Integer rptPeriodType = calcTimeInterval(beginTime, endTime);//0: 일간, 1:주간, 2: 월간.
//		
//		if(rptPeriodType >= 3)
//		{
////			vsLogTableName = "LOG_ADC_VS_RESC";// 통계 테이블로 변경해야 함. TODO
//			vsvsLogTalbeName = "LOG_SVC_PERF_STATS";// 통계 테이블로 변경해야 함. TODO
//		}

		final OBDatabase db = new OBDatabase();
		try {
			// adc 목록을 작성한다. 예: INDEX=1 OR INDEX=2.....
			String szAdcList = "";
			for (Integer adcIndex : adcIndexList) {
				if (false == szAdcList.isEmpty()) {
					szAdcList += " OR ";
				}
				szAdcList += "INDEX=" + adcIndex;
			}

			String whereClauseTime = "";

			if (beginTime != null) {
				whereClauseTime = String.format(" AND OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));
				if (endTime != null) {
					whereClauseTime += String.format(" AND OCCUR_TIME <= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));
				}
			} else {
				if (endTime != null) {
					whereClauseTime += String.format(" AND OCCUR_TIME <= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));
				}
			}

			// TOP 10 리스트 추출.
			sqlText = String.format(
					" SELECT MNG_ADC.NAME ADC_NAME, MNG_ADC.TYPE, VS.VS_INDEX AS VSVC_INDEX, VS.VS_NAME,           \n"
							+ "     VS.VS_IPADDRESS, VS.PORT, VS.AVG_BPS, VS.AVG_CUR_CONNS                     \n"
							+ " FROM ( SELECT A.ADC_INDEX, A.INDEX AS VS_INDEX, A.NAME AS VS_NAME,             \n"
							+ "            A.VIRTUAL_IP AS VS_IPADDRESS, A.VIRTUAL_PORT AS PORT,               \n"
							+ "            AVG(B.BPS_IN + B.BPS_OUT) AS AVG_BPS, AVG(B.CONN_CURR) AVG_CUR_CONNS \n"
							+ "        FROM TMP_SLB_VSERVER A                                                  \n"
							+ "        LEFT JOIN LOG_SVC_PERF_STATS B                                    \n"
							+ "        ON B.OBJ_INDEX = A.INDEX                                                \n"
							+ "        WHERE ADC_INDEX IN (SELECT INDEX FROM MNG_ADC WHERE (%s) AND AVAILABLE = %d AND ( TYPE = %d OR TYPE = %d OR TYPE = %d) ) \n"
							+ // F5 먼저 조회.
							"        %s                                                                      \n" + // 시간
																													// 조건.
							"        GROUP BY ADC_INDEX, VS_INDEX, VS_NAME, VS_IPADDRESS, PORT               \n"
							+ " ) VS                                                                           \n"
							+ " LEFT JOIN MNG_ADC                                                              \n"
							+ " ON VS.ADC_INDEX = MNG_ADC.INDEX                                                \n"
							+ " UNION ALL                                                                      \n"
							+ " SELECT MNG_ADC.NAME ADC_NAME, MNG_ADC.TYPE, VSS.VS_INDEX, VSS.VS_NAME,         \n"
							+ "    VSS.VS_IPADDRESS, VSS.PORT, VSS.AVG_BPS, VSS.AVG_CUR_CONNS                  \n"
							+ " FROM ( SELECT A.ADC_INDEX, A.INDEX AS VSVC_INDEX, B.NAME AS VS_NAME,           \n"
							+ "        B.VIRTUAL_IP AS VS_IPADDRESS, A.VIRTUAL_PORT AS PORT,                   \n"
							+ "            AVG(C.BPS_IN) AS AVG_BPS, AVG(C.CONN_CURR) AVG_CUR_CONNS            \n"
							+ "        FROM TMP_SLB_VS_SERVICE A                                               \n"
							+ "        INNER JOIN TMP_SLB_VSERVER B                                            \n"
							+ "        ON A.VS_INDEX == B.INDEX                                                \n"
							+ "        LEFT JOIN LOG_SVC_PERF_STATS C                                    \n"
							+ "        ON A.INDEX = C.OBJ_INDEX                                                \n"
							+ "        WHERE ADC_INDEX IN (SELECT INDEX FROM MNG_ADC WHERE (%s) AND AVAILABLE = %d AND TYPE = %d) \n"
							+ // Alteon 조회.
							"        %s                                                                      \n" + // 시간
																													// 조건.
							"        GROUP BY ADC_INDEX, VSERVICE_INDEX, VS_NAME, VS_IPADDRESS, SERVICE_PORT \n"
							+ " ) VSS                                                                          \n"
							+ " LEFT JOIN MNG_ADC                                                              \n"
							+ " ON VSS.ADC_INDEX = MNG_ADC.INDEX                                               \n"
							+ " ORDER BY AVG_BPS DESC, AVG_CUR_CONNS DESC, ADC_NAME, VS_NAME, PORT             \n"
							+ " LIMIT 10                                                                       \n",
					szAdcList, OBDefine.ADC_STATE.AVAILABLE, OBDefine.ADC_TYPE_F5, OBDefine.ADC_TYPE_PIOLINK_PAS,
					OBDefine.ADC_TYPE_PIOLINK_PASK, whereClauseTime, szAdcList, OBDefine.ADC_STATE.AVAILABLE,
					OBDefine.ADC_TYPE_ALTEON, whereClauseTime);

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText THUR=" + sqlText);

			db.openDB();
			ResultSet rs = db.executeQuery(sqlText);
			int rank = 1;
			while (rs.next()) {
				OBDtoRptL4PerformanceInfo obj = new OBDtoRptL4PerformanceInfo();
				obj.setAdcName(db.getString(rs, "ADC_NAME"));
				obj.setAdcType(db.getInteger(rs, "TYPE"));
				obj.setObjName(db.getString(rs, "VS_INDEX"));
				obj.setVsName(db.getString(rs, "VS_NAME"));
				obj.setVsIPAddress(db.getString(rs, "VS_IPADDRESS"));
				obj.setVsPort(db.getInteger(rs, "PORT"));
				obj.setAvgBps(db.getLong(rs, "AVG_BPS"));
				obj.setAvgConnections(db.getLong(rs, "AVG_CUR_CONNS"));

				obj.setRank(rank++);
				retVal.add(obj);
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "rank=" + obj.getRank() + " ; vsname=" + obj.getVsName());
			}

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. list count = %d", retVal.size()));
			return retVal;
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

	private ArrayList<OBDtoRptL4PerformanceInfo> getTop10ConnectionInfoList(ArrayList<Integer> adcIndexList,
			String searchKey, Date beginTime, Date endTime) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adc list:%s, search:%s", adcIndexList, searchKey));

		ArrayList<OBDtoRptL4PerformanceInfo> retVal = new ArrayList<OBDtoRptL4PerformanceInfo>();

		if (adcIndexList.size() == 0)
			return retVal;

		String sqlText = "";

//		String vsLogTableName = "LOG_ADC_VS_RESC";
//		String vsvsLogTalbeName = "LOG_SVC_PERF_STATS";

//		Integer rptPeriodType = calcTimeInterval(beginTime, endTime);//0: 일간, 1:주간, 2: 월간.

//		if(rptPeriodType >= 3)
//		{
//			vsLogTableName = "LOG_ADC_VS_RESC";// 통계 테이블로 변경해야 함. TODO
//			vsvsLogTalbeName = "LOG_SVC_PERF_STATS";// 통계 테이블로 변경해야 함. TODO
//		}

		final OBDatabase db = new OBDatabase();
		try {
			// adc 목록을 작성한다. 예: INDEX=1 OR INDEX=2.....
			String szAdcList = "";
			for (Integer adcIndex : adcIndexList) {
				if (false == szAdcList.isEmpty()) {
					szAdcList += " OR ";
				}
				szAdcList += "INDEX=" + adcIndex;
			}

			String whereClauseTime = "";

			if (beginTime != null) {
				whereClauseTime = String.format(" AND OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));
				if (endTime != null) {
					whereClauseTime += String.format(" AND OCCUR_TIME <= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));
				}
			} else {
				if (endTime != null) {
					whereClauseTime += String.format(" AND OCCUR_TIME <= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));
				}
			}

			// TOP 10 리스트 추출.
			sqlText = String.format(
					" SELECT MNG_ADC.NAME ADC_NAME, MNG_ADC.TYPE, VS.VS_INDEX AS VSVC_INDEX, VS.VS_NAME,           \n"
							+ "     VS.VS_IPADDRESS, VS.PORT, VS.AVG_BPS, VS.AVG_CUR_CONNS                     \n"
							+ " FROM ( SELECT A.ADC_INDEX, A.INDEX AS VS_INDEX, A.NAME AS VS_NAME,             \n"
							+ "            A.VIRTUAL_IP AS VS_IPADDRESS, A.VIRTUAL_PORT AS PORT,               \n"
							+ "            AVG(B.BPS_IN + B.BPS_OUT) AS AVG_BPS, AVG(B.CONN_CURR) AVG_CUR_CONNS \n"
							+ "        FROM TMP_SLB_VSERVER A                                                  \n"
							+ "        LEFT JOIN LOG_SVC_PERF_STATS B                                    \n"
							+ "        ON B.OBJ_INDEX = A.INDEX                                                \n"
							+ "        WHERE ADC_INDEX IN (SELECT INDEX FROM MNG_ADC WHERE (%s) AND AVAILABLE = %d AND ( TYPE = %d OR TYPE = %d OR TYPE = %d) ) \n"
							+ // F5 먼저 조회.
							"        %s                                                                      \n" + // 시간
																													// 조건.
							"        GROUP BY ADC_INDEX, VS_INDEX, VS_NAME, VS_IPADDRESS, PORT               \n"
							+ " ) VS                                                                           \n"
							+ " LEFT JOIN MNG_ADC                                                              \n"
							+ " ON VS.ADC_INDEX = MNG_ADC.INDEX                                                \n"
							+ " UNION ALL                                                                      \n"
							+ " SELECT MNG_ADC.NAME ADC_NAME, MNG_ADC.TYPE, VSS.VS_INDEX, VSS.VS_NAME,         \n"
							+ "    VSS.VS_IPADDRESS, VSS.PORT, VSS.AVG_BPS, VSS.AVG_CUR_CONNS                  \n"
							+ " FROM ( SELECT A.ADC_INDEX, A.INDEX AS VSVC_INDEX, B.NAME AS VS_NAME,           \n"
							+ "        B.VIRTUAL_IP AS VS_IPADDRESS, A.VIRTUAL_PORT AS PORT,                   \n"
							+ "            AVG(C.BPS_IN) AS AVG_BPS, AVG(C.CONN_CURR) AVG_CUR_CONNS            \n"
							+ "        FROM TMP_SLB_VS_SERVICE A                                               \n"
							+ "        INNER JOIN TMP_SLB_VSERVER B                                            \n"
							+ "        ON A.VS_INDEX == B.INDEX                                                \n"
							+ "        LEFT JOIN LOG_SVC_PERF_STATS C                                    \n"
							+ "        ON A.INDEX = C.OBJ_INDEX                                                \n"
							+ "        WHERE ADC_INDEX IN (SELECT INDEX FROM MNG_ADC WHERE (%s) AND AVAILABLE = %d AND TYPE = %d) \n"
							+ // Alteon 조회.
							"        %s                                                                      \n" + // 시간
																													// 조건.
							"        GROUP BY ADC_INDEX, VSERVICE_INDEX, VS_NAME, VS_IPADDRESS, SERVICE_PORT \n"
							+ " ) VSS                                                                          \n"
							+ " LEFT JOIN MNG_ADC                                                              \n"
							+ " ON VSS.ADC_INDEX = MNG_ADC.INDEX                                               \n"
							+ " ORDER BY AVG_CUR_CONNS DESC, AVG_BPS DESC, ADC_NAME, VS_NAME, PORT             \n"
							+ " LIMIT 10 ",
					szAdcList, OBDefine.ADC_STATE.AVAILABLE, OBDefine.ADC_TYPE_F5, OBDefine.ADC_TYPE_PIOLINK_PAS,
					OBDefine.ADC_TYPE_PIOLINK_PASK, whereClauseTime, szAdcList, OBDefine.ADC_STATE.AVAILABLE,
					OBDefine.ADC_TYPE_ALTEON, whereClauseTime);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText CONN =" + sqlText);

			db.openDB();
			ResultSet rs = db.executeQuery(sqlText);
			int rank = 1;
			while (rs.next()) {
				OBDtoRptL4PerformanceInfo obj = new OBDtoRptL4PerformanceInfo();

				obj.setAdcName(db.getString(rs, "ADC_NAME"));
				obj.setAdcType(db.getInteger(rs, "TYPE"));
				obj.setObjName(db.getString(rs, "VSVC_INDEX"));
				obj.setVsName(db.getString(rs, "VS_NAME"));
				obj.setVsIPAddress(db.getString(rs, "VS_IPADDRESS"));
				obj.setVsPort(db.getInteger(rs, "PORT"));
				obj.setAvgBps(db.getLong(rs, "AVG_BPS"));
				obj.setAvgConnections(db.getLong(rs, "AVG_CUR_CONNS"));

				obj.setRank(rank++);
				retVal.add(obj);
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "rank=" + obj.getRank() + " ; vsname=" + obj.getVsName());
			}

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. list count = %d", retVal.size()));
			return retVal;
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

	private ArrayList<OBDtoRptL4PerformanceTrend> getTop10ThroughputList(ArrayList<OBDtoRptL4PerformanceInfo> itemList,
			Date beginTime, Date endTime) throws OBException {
		ArrayList<OBDtoRptL4PerformanceTrend> retVal = new ArrayList<OBDtoRptL4PerformanceTrend>();

		String sqlText = "";

		String tableName = "";
		Integer rptPeriodType = calcTimeInterval(beginTime, endTime);// 0: 일간, 1:주간, 2: 월간.

		final OBDatabase db = new OBDatabase();
		try {
			String whereClauseTime = "";
			String whereClauseIndex = "";

			if (beginTime != null) {
				whereClauseTime = String.format(" AND OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));
				if (endTime != null) {
					whereClauseTime += String.format(" AND OCCUR_TIME <= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));
				}
			} else {
				if (endTime != null) {
					whereClauseTime += String.format(" AND OCCUR_TIME <= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));
				}
			}

			Integer rank = 1;
			for (OBDtoRptL4PerformanceInfo item : itemList) {
				tableName = "LOG_SVC_PERF_STATS";
				if (item.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
					if (rptPeriodType <= 2) {// 일간, 주간인 경우.
						tableName = "LOG_SVC_PERF_STATS";
					} else {
						tableName = "LOG_SVC_PERF_STATS";
					}

					whereClauseIndex = String.format(" VSERVICE_INDEX = %s ", OBParser.sqlString(item.getObjName()));
				} else if (item.getAdcType() == OBDefine.ADC_TYPE_F5) {
					if (rptPeriodType <= 2) {// 일간, 주간인 경우.
						tableName = "LOG_SVC_PERF_STATS";
					} else {
						tableName = "LOG_SVC_PERF_STATS";
					}
					whereClauseIndex = String.format(" VS_INDEX = %s ", OBParser.sqlString(item.getObjName()));
				} else if (item.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS) {
					if (rptPeriodType <= 2) {// 일간, 주간인 경우.
						tableName = "LOG_SVC_PERF_STATS";
					} else {
						tableName = "LOG_SVC_PERF_STATS";
					}
					whereClauseIndex = String.format(" VS_INDEX = %s ", OBParser.sqlString(item.getObjName()));
				} else if (item.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {
					if (rptPeriodType <= 2) {// 일간, 주간인 경우.
						tableName = "LOG_SVC_PERF_STATS";
					} else {
						tableName = "LOG_SVC_PERF_STATS";
					}
					whereClauseIndex = String.format(" VS_INDEX = %s ", OBParser.sqlString(item.getObjName()));
				} else {
					throw new OBException(OBException.ERRCODE_ADC_VENDOR);
				}
				sqlText = String.format(" SELECT OCCUR_TIME, (BPS_IN + BPS_OUT) AS BPS " + " FROM %s " + // 테이블 조건.
						" WHERE %s " + // 테이블 필드 조건.
						" %s " + // 시간 조건.
						" ORDER BY OCCUR_TIME; ", tableName, whereClauseIndex, whereClauseTime);

				OBDtoRptL4PerformanceTrend obj = new OBDtoRptL4PerformanceTrend();
				ArrayList<OBDtoUsageThroughput> usageList = new ArrayList<OBDtoUsageThroughput>();

				db.openDB();
				ResultSet rs = db.executeQuery(sqlText);
				while (rs.next()) {
					OBDtoUsageThroughput usage = new OBDtoUsageThroughput();

					usage.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
					usage.setBps(db.getLong(rs, "BPS"));
//					usage.setBps((long)(10000L + Math.random() * (99999 - 10000)));
					usage.setPps(0L);
					usageList.add(usage);
				}
				obj.setRank(rank++);
				obj.setVsIPAddress(item.getVsIPAddress());
				obj.setVsPort(item.getVsPort());
				obj.setUsage(usageList);
				obj.setVsName(item.getVsName());
				retVal.add(obj);
			}
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. list count = %d", retVal.size()));
			return retVal;
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
	public ArrayList<OBDtoRptL4PerformanceTrend> getTop10ThroughputList(String rptIndex, Integer accntIndex,
			ArrayList<OBDtoRptL4PerformanceInfo> itemList) throws OBException {
		ArrayList<OBDtoRptL4PerformanceTrend> retVal = new ArrayList<OBDtoRptL4PerformanceTrend>();

		try {
			OBReportInfo rptInfo = new OBReportImpl().getReportInfo(rptIndex);
			retVal = getTop10ThroughputList(itemList, rptInfo.getBeginTime(), rptInfo.getEndTime());
		} catch (OBException e) {
			throw e;
		}
		return retVal;
	}

	private ArrayList<OBDtoRptL4PerformanceTrend> getTop10ConnectionList(ArrayList<OBDtoRptL4PerformanceInfo> itemList,
			Date beginTime, Date endTime) throws OBException {
		ArrayList<OBDtoRptL4PerformanceTrend> retVal = new ArrayList<OBDtoRptL4PerformanceTrend>();

		String sqlText = "";

		String tableName = "";
		Integer rptPeriodType = calcTimeInterval(beginTime, endTime);// 0: 일간, 1:주간, 2: 월간.

		final OBDatabase db = new OBDatabase();
		try {
			String whereClauseTime = "";
			String whereClauseIndex = "";

			if (beginTime != null) {
				whereClauseTime = String.format(" AND OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));
				if (endTime != null) {
					whereClauseTime += String.format(" AND OCCUR_TIME <= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));
				}
			} else {
				if (endTime != null) {
					whereClauseTime += String.format(" AND OCCUR_TIME <= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));
				}
			}

			Integer rank = 1;
			for (OBDtoRptL4PerformanceInfo item : itemList) {
				tableName = "LOG_SVC_PERF_STATS";
				if (item.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
					if (rptPeriodType <= 2) {// 일간, 주간인 경우.
						tableName = "LOG_SVC_PERF_STATS";
					} else {
						tableName = "LOG_SVC_PERF_STATS";
					}

					whereClauseIndex = String.format(" OBJ_INDEX = %s ", OBParser.sqlString(item.getObjName()));
				} else if (item.getAdcType() == OBDefine.ADC_TYPE_F5) {
					if (rptPeriodType <= 2) {// 일간, 주간인 경우.
						tableName = "LOG_SVC_PERF_STATS";
					} else {
						tableName = "LOG_SVC_PERF_STATS";
					}
					whereClauseIndex = String.format(" OBJ_INDEX = %s ", OBParser.sqlString(item.getObjName()));
				} else if (item.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS) {
					if (rptPeriodType <= 2) {// 일간, 주간인 경우.
						tableName = "LOG_SVC_PERF_STATS";
					} else {
						tableName = "LOG_SVC_PERF_STATS";
					}
					whereClauseIndex = String.format(" OBJ_INDEX = %s ", OBParser.sqlString(item.getObjName()));
				} else if (item.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {
					if (rptPeriodType <= 2) {// 일간, 주간인 경우.
						tableName = "LOG_SVC_PERF_STATS";
					} else {
						tableName = "LOG_SVC_PERF_STATS";
					}
					whereClauseIndex = String.format(" OBJ_INDEX = %s ", OBParser.sqlString(item.getObjName()));
				} else {
					throw new OBException(OBException.ERRCODE_ADC_VENDOR);
				}
				sqlText = String.format(" SELECT OCCUR_TIME, CONN_CURR " + " FROM %s " + // 테이블 조건.
						" WHERE %s " + // 테이블 필드 조건.
						" %s " + // 시간 조건.
						" ORDER BY OCCUR_TIME; ", tableName, whereClauseIndex, whereClauseTime);

				OBDtoRptL4PerformanceTrend obj = new OBDtoRptL4PerformanceTrend();
				ArrayList<OBDtoUsageThroughput> usageList = new ArrayList<OBDtoUsageThroughput>();

				db.openDB();
				ResultSet rs = db.executeQuery(sqlText);
				while (rs.next()) {
					OBDtoUsageThroughput usage = new OBDtoUsageThroughput();

					usage.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
					usage.setPps(db.getLong(rs, "CONN_CURR"));
//					usage.setPps((long)(10000L + Math.random() * (99999 - 10000)));
					usage.setBps(0L);
					usageList.add(usage);
				}
				obj.setRank(rank++);
				obj.setVsIPAddress(item.getVsIPAddress());
				obj.setVsPort(item.getVsPort());
				obj.setUsage(usageList);
				obj.setVsName(item.getVsName());
				retVal.add(obj);
			}
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. list count = %d", retVal.size()));
			return retVal;
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
	public ArrayList<OBDtoRptL4PerformanceTrend> getTop10ConnectionList(String rptIndex, Integer accntIndex,
			ArrayList<OBDtoRptL4PerformanceInfo> itemList) throws OBException {
		ArrayList<OBDtoRptL4PerformanceTrend> retVal = new ArrayList<OBDtoRptL4PerformanceTrend>();

		try {
			OBReportInfo rptInfo = new OBReportImpl().getReportInfo(rptIndex);
			retVal = getTop10ConnectionList(itemList, rptInfo.getBeginTime(), rptInfo.getEndTime());
		} catch (OBException e) {
			throw e;
		}
		return retVal;
	}

}
