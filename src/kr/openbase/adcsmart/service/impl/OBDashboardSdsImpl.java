package kr.openbase.adcsmart.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.OBDashboardSds;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcStatusCount;
import kr.openbase.adcsmart.service.dto.OBDtoConnection;
import kr.openbase.adcsmart.service.dto.OBDtoCpu;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsAdcSummary;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsIssueSummary;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsMemberConnection;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsStatusSummary;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservInfo;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservStatus;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservSummary;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservSummaryCount;
import kr.openbase.adcsmart.service.dto.OBDtoFaultStatusCount;
import kr.openbase.adcsmart.service.dto.OBDtoMemory;
import kr.openbase.adcsmart.service.dto.OBDtoThroughput;
import kr.openbase.adcsmart.service.dto.OBDtoVservStatusCount;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoGroupHistory;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBDefineFault;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBDashboardSdsImpl implements OBDashboardSds {
	static final int STATUS_ADC_ALL = 0;
	static final int STATUS_ADC_AVAILABLE = 1;
	static final int STATUS_ADC_UNAVAILABLE = 2;

	static final int STATUS_VS_ALL = 10;
	static final int STATUS_VS_AVAILABLE = 11;
	static final int STATUS_VS_UNAVAILABLE = 12;
	static final int STATUS_VS_UNAVAILABLE_LONG = 13; // N일 이상 단절 VS 뽑는 조건을 표현하는 코드
	static final int STATUS_VS_DISABLE = 14;

	static final int STATUS_ISSUE_ALL = 20;
	static final int STATUS_ISSUE_SOLVED = 21;
	static final int STATUS_ISSUE_UNSOLVED = 22;
	static final int STATUS_ISSUE_WARN = 23;

	static final int TARGET_ALL = 0;
	static final int TARGET_GROUP = 1;
	static final int TARGET_ADC = 2;

	static final int RECORD_TYPE_VSERVER = 0;
	static final int RECORD_TYPE_VSERVICE = 1;

	static final int VIRTUALSERVER_UNAVAILABLE_LIMIT_DAYS_DEFAULT = 1;

	@Override
	public OBDtoDashboardSdsStatusSummary getStatusSummary(Integer accountIndex, Integer faultUnsolvedLimitDays)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. accountIndex:%d, faultUnsolvedLimitDays = %d",
				accountIndex, faultUnsolvedLimitDays));

		OBDtoDashboardSdsStatusSummary summary = new OBDtoDashboardSdsStatusSummary();
		OBDatabase db = new OBDatabase();
		OBDashboardSdsImpl dashManager = new OBDashboardSdsImpl();

		try {
			db.openDB();

			OBDtoAdcStatusCount adcSummary = new OBMonitoringImpl().GetAdcStatusCount(accountIndex, db);
			OBDtoVservStatusCount vservSummary = dashManager.getVservStatusCount(accountIndex, faultUnsolvedLimitDays,
					db);
			// 아래 부분은 쓰이지 않는 데이터 조회이므로 삭제한다. 불필요한 기능인데 조회 시간도 길다. "장애 통계 현황" 위젯. #4526
			// OBDtoFaultStatusCount faultSummary =
			// dashManager.getFaultStatusCount(accountIndex, null, faultUnsolvedLimitDays,
			// db); //계정에 할당된 ADC에 대해 조회시 accountIndex>0, adcIndex = null

			summary.setAdc(adcSummary.getAdc());
			summary.setAdcAvail(adcSummary.getAdcAvail());
			summary.setAdcUnavail(adcSummary.getAdcUnavail());

			summary.setVs(vservSummary.getVs());
			summary.setVsAvail(vservSummary.getVsAvail());
			summary.setVsDisable(vservSummary.getVsDisable());
			summary.setVsUnavail(vservSummary.getVsUnavail());
			summary.setVsUnavailLessNDays(vservSummary.getVsUnavailLessNDays()); // N일 이내 단절
			summary.setVsUnavailOverNDays(vservSummary.getVsUnavailOverNDays()); // N일 이상 단절

			summary.setFault(-1);
			summary.setFaultSolved(-1);
			summary.setFaultUnsolved(-1);
			summary.setFaultWarn(-1);
		}
//		catch(SQLException e)
//		{
//			db.closeDB();
//			OBException e1 = new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sqlText));
//			throw new OBException(OBException.ERRCODE_DBOARD_ADCSUMMARY, e1);
//		}
		catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", summary));

		return summary;
	}

//	public static void main(String[] args)
//	{
//		OBDashboardSdsImpl dashManager = new OBDashboardSdsImpl();
//		//상단 - adc/virtual server/장애 상태별 카운트
//		int testUserAccountIndex = 1;
//		try
//		{
//			OBDtoDashboardSdsStatusSummary summary = dashManager.getStatusSummary(testUserAccountIndex, 1);
//			System.out.println("result = " + summary.toString());
//		}
//		catch(OBException e)
//		{
//			System.out.println("error = " + e.getMessage());
//			e.printStackTrace();
//		}
//	}

	/**
	 * 계정에 할당된 ADC들의 Virtual Server 상태별 통계를 구한다. </br>
	 * 
	 * @param accountIndex
	 * @param db
	 * @return
	 * @throws OBException
	 */
	public OBDtoVservStatusCount getVservStatusCount(Integer accountIndex, Integer faultUnsolvedLimitDays,
			OBDatabase db) throws OBException {
		OBDtoVservStatusCount count = new OBDtoVservStatusCount();
		String sqlText = "";
		String adcIndexSubquery = "";

		if (accountIndex != null) {
			String adcIndexSubqueryAdmin = String.format(" SELECT INDEX FROM MNG_ADC WHERE AVAILABLE = %d ",
					OBDefine.ADC_STATE.AVAILABLE);
			String adcIndexSubqueryNonAdmin = String
					.format(" SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %d ", accountIndex);

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
		}

//		2014.7.11 ykkim. 최근 N일 이상 단절은 꺼짐으로 보는 기능 제거. 현재 N일 이상 VS 단절을 파악할 수 없으므로 기능을 제거한다.		
//		String dateRange = "";
//		dateRange = " TRUE ";
//		if(faultUnsolvedLimitDays > 0) //일자 조건 적용
//		{   //발생 시각 최근 1일(사실은 시간)이내 조회
//			dateRange = String.format(" OCCUR_TIME > CURRENT_TIMESTAMP - INTERVAL '%d HOURS' ", (faultUnsolvedLimitDays * 24));
//		}

		try {
			sqlText = String.format(" SELECT STATUS, COUNT(INDEX) AS COUNT           \n"
					+ " FROM TMP_SLB_VSERVER                           \n"
					+ " WHERE ADC_INDEX IN ( %s )                      \n" + // where-in:empty string 불가, null 불가
					" GROUP BY STATUS                                \n" // 여기까지 일자 조건 없는 정상 vs, disable(단절) vs, 꺼짐 vs
//				" UNION ALL                                      \n" + 
//				" SELECT -9 AS STATUS, COUNT(INDEX) AS COUNT     \n" + //N일 이내 발생한 VS unavailable
//				" FROM TMP_SLB_VSERVER                           \n" +
//				" WHERE INDEX IN (                               \n" + //where-in:empty string 불가, null 불가
//				"     SELECT VS_INDEX                            \n" +
//				"     FROM LOG_ADC_FAULT                         \n" +
//				"     WHERE ADC_INDEX IN ( %s )                  \n" + //where-in:empty string 불가, null 불가
//				"         AND TYPE = %d                          \n" + //장애 유형이 virtual server인 것들만
//				"         AND STATUS = %d                        \n" + //상태 장애 미해결인 것들만
//				"         AND %s                                 \n" + //일자 조건
//				" )                                              \n" +
//				"AND STATUS = %d;                                    "
					, adcIndexSubquery, adcIndexSubquery, OBDefineFault.TYPE.VIRTUALSRV_DOWN.getCode(),
					OBDefineFault.STATUS_UNSOLVED);
			// System.out.println("sqlText = " + sqlText);
			ResultSet rs = db.executeQuery(sqlText);
			int totalCnt = 0;
			int availCnt = 0;
			int unavailTotalCnt = 0;
			int unavailRecentCnt = 0;
			int unavailLongCnt = 0;
			int disableCnt = 0;
			int status = OBDefine.STATUS_UNAVAILABLE;
			int num = 0;

			while (rs.next()) {
				status = db.getInteger(rs, "STATUS");
				num = db.getInteger(rs, "COUNT");

				if (status == OBDefine.VS_STATUS.AVAILABLE) {
					availCnt += num;
					totalCnt += num;
				} else if (status == OBDefine.VS_STATUS.DISABLE) {
					disableCnt += num;
					totalCnt += num;
				} else if (status == OBDefine.VS_STATUS.UNAVAILABLE) {
					unavailTotalCnt += num;
					totalCnt += num;
				}
//				else //-9:일자 조건이 있는 단절, 즉 N일 이내 단절
//				{
//					unavailRecentCnt += num;
//				}		
			}

			// 통계 재처리 1: N일 이상 단절 = 모든 단절 - N일 이내 단절
//			unavailLongCnt = unavailTotalCnt - unavailRecentCnt;
			// 통계 재처리 2: 꺼짐 보정 : N일 이상 단절은 단절이 아니라 꺼짐으로 본다.
//			disableCnt += unavailLongCnt;

			count.setVs(totalCnt); // 전체
			count.setVsAvail(availCnt); // 정상
			count.setVsDisable(disableCnt); // 꺼짐
			count.setVsUnavail(unavailTotalCnt); // 단절 전체
			count.setVsUnavailLessNDays(unavailRecentCnt); // 단절 N일 이내
			count.setVsUnavailOverNDays(unavailLongCnt); // 단절 N일 이상
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return count;
	}

	// accountIndex에 관련된 adc들 모두의 정보를 구하려면, accountIndex > 0, adcIndex = null
	// 특정 adc의 정보를 구하려면, accountIndex = null, adcIndex > 0
	// 전체 장애 (STATUS_ISSUE_ALL): 최근 issueUnsolvedMaxDays이내의 카운트를 구한다. 0이면 전체를 구한다.
	// 해결 (STATUS_ISSUE_SOLVED): 최근 issueUnsolvedMaxDays이내의 카운트를 구한다. 0이면 전체를 구한다.
	// 미해결(STATUS_ISSUE_UNSOLVED): issueUnsolvedMaxDays값에 상관없이 전체를 구한다.
	public OBDtoFaultStatusCount getFaultStatusCount(Integer accountIndex, Integer adcIndex,
			Integer faultUnsolvedLimitDays, OBDatabase db) throws OBException {
		OBDtoFaultStatusCount count = new OBDtoFaultStatusCount();

		String sqlText = "";
		String adcIndexSubquery = "";
		String dateRange = "";

		if (accountIndex != null) {
			String adcIndexSubqueryAdmin = String.format(" SELECT INDEX FROM MNG_ADC WHERE AVAILABLE = %d ",
					OBDefine.ADC_STATE.AVAILABLE);
			String adcIndexSubqueryNonAdmin = String
					.format(" SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %d ", accountIndex);

			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accountIndex);
			if (roleNo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);
			}

			if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
				adcIndexSubquery = adcIndexSubqueryAdmin;
			} else {
				adcIndexSubquery = adcIndexSubqueryNonAdmin;
			}
		} else if (adcIndex != null) // 특정 adc 하나를 지정한 경우
		{
			adcIndexSubquery = String.format(" %d ", adcIndex);
		}

		if (faultUnsolvedLimitDays == null || faultUnsolvedLimitDays.equals(0)) // 일자 조건을 무시한다.
		{
			dateRange = " TRUE ";
		} else {
			dateRange = String.format(" OCCUR_TIME >= CURRENT_TIMESTAMP - INTERVAL '%d HOURS' ",
					faultUnsolvedLimitDays * 24); // 오늘 포함 최근 7일을 조회하려면 6일전부터 이기 때문에 (전달된날짜 -1)을 쓴다.
		}
		try {
			sqlText = String.format(" SELECT -9 AS STATUS, COUNT(LOG_SEQ) AS COUNT             \n"
					+ " FROM LOG_ADC_FAULT                                       \n"
					+ " WHERE ADC_INDEX IN ( %s ) AND %s                         \n" + // DATE 조건, //where-in:empty
																						// string 불가, null 불가, OK
					" UNION ALL                                                \n"
					+ " SELECT STATUS, COUNT(LOG_SEQ) AS COUNT                   \n"
					+ " FROM LOG_ADC_FAULT                                       \n"
					+ " WHERE ADC_INDEX IN ( %s ) AND STATUS IN (%d) AND %s      \n" + // DATE 조건, //where-in:empty
																						// string 불가, null 불가, OK
					" GROUP BY STATUS                                          \n"
					+ " UNION ALL                                                \n"
					+ " SELECT STATUS, COUNT(LOG_SEQ) AS COUNT                   \n"
					+ " FROM LOG_ADC_FAULT                                       \n"
					+ " WHERE ADC_INDEX IN ( %s ) AND STATUS IN (%d) AND %s      \n" + // DATE 조건, //where-in:empty
																						// string 불가, null 불가, OK
					" GROUP BY STATUS                                          \n"
					+ " UNION ALL                                                \n"
					+ " SELECT STATUS, COUNT(LOG_SEQ) AS COUNT                   \n"
					+ " FROM LOG_ADC_FAULT                                       \n"
					+ " WHERE ADC_INDEX IN ( %s ) AND STATUS IN (%d) AND %s      \n" + // DATE 조건, //where-in:empty
																						// string 불가, null 불가, OK
					" GROUP BY STATUS;                                         \n", adcIndexSubquery, dateRange, // 전체
																													// 조건
					adcIndexSubquery, OBDefineFault.STATUS_SOLVED, dateRange, // 해결 조건
					adcIndexSubquery, OBDefineFault.STATUS_WARN, dateRange, // 경고 조건
					adcIndexSubquery, OBDefineFault.STATUS_UNSOLVED, dateRange); // 미해결 조건

			// System.out.println("sqlText = " + sqlText);
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sql = " + sqlText);
			ResultSet rs = db.executeQuery(sqlText);

			int fault_solved = 0;
			int fault_warn = 0;
			int fault_unsolved = 0;
			int fault_total = 0;

			while (rs.next()) {
				if (db.getInteger(rs, "STATUS") == OBDefineFault.STATUS_SOLVED) // 해결 장애
				{
					fault_solved += db.getInteger(rs, "COUNT");
				}
				if (db.getInteger(rs, "STATUS") == OBDefineFault.STATUS_WARN) // 경고 장애
				{
					fault_warn += db.getInteger(rs, "COUNT");
				} else if (db.getInteger(rs, "STATUS") == OBDefineFault.STATUS_UNSOLVED) // 미해결 장애
				{
					fault_unsolved += db.getInteger(rs, "COUNT");
				} else // -9:total fault
				{
					fault_total += db.getInteger(rs, "COUNT");
				}
			}
			count.setFaultSolved(fault_solved);
			count.setWarn(fault_warn);
			count.setFaultUnsolved(fault_unsolved);
			count.setFault(fault_total);

		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return count;
	}

	@Override
	public ArrayList<OBDtoAdcGroup> getGroupAdcList(Integer accountIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. accountIndex:%d", accountIndex));

		ArrayList<OBDtoAdcGroup> groupList = new ArrayList<OBDtoAdcGroup>();

		OBDatabase db = new OBDatabase();
		ResultSet rs;

		String sqlText = "";

		String adcIndexSubqueryAdmin = String.format(" SELECT INDEX FROM MNG_ADC WHERE AVAILABLE = %d ",
				OBDefine.ADC_STATE.AVAILABLE);
		String adcIndexSubqueryNonAdmin = String
				.format(" SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %d ", accountIndex);
		String adcIndexSubquery = "";
		Integer roleNo = null;

		try {
			db.openDB();

			roleNo = new OBAccountImpl().getAccountRoleNo(accountIndex);
			if (roleNo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);
			}

			if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
				adcIndexSubquery = adcIndexSubqueryAdmin;
			} else {
				adcIndexSubquery = adcIndexSubqueryNonAdmin;
			}

			sqlText = String.format(" SELECT A.GROUP_INDEX, B.NAME, B.DESCRIPTION, COUNT(A.INDEX)	\n"
					+ " FROM MNG_ADC A 												\n"
					+ " INNER JOIN MNG_ADC_GROUP B 									\n"
					+ " ON A.GROUP_INDEX = B.INDEX       	    						\n"
					+ " WHERE A.INDEX IN (%s) 										\n" + // where-in:empty string 불가,
																							// null 불가, OK
					" GROUP BY A.GROUP_INDEX, B.NAME, B.DESCRIPTION ;				\n", adcIndexSubquery);

//			String sqlTextAdc = String.format(
//					" SELECT INDEX, NAME, GROUP_INDEX, STATUS	\n" +
//					" FROM MNG_ADC 								\n" +
//					" WHERE INDEX IN (%s)						\n" +
//					" ORDER BY GROUP_INDEX; 					\n",
//					sqlAdcInSubquery);

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

				ArrayList<OBDtoAdcInfo> adcList = new OBAdcManagementImpl().getAdcInfoListInGroup(groupIndex,
						accountIndex);
				for (OBDtoAdcInfo adc : adcList) {
					if (adc.getStatus().equals(OBDefine.ADC_STATUS.REACHABLE) == false) {
						unavailCount++;
					}
				}
				// System.out.println("Unavail = " + unavailCount);
				group.setAdcList(adcList);
				group.setAdcUnavailCount(unavailCount);
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
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", groupList.toString()));
		return groupList;
	}
	/*
	 * public static void main(String[] args) { OBDashboardSdsImpl dashManager = new
	 * OBDashboardSdsImpl(); //상단 - int testUserAccountIndex = 1; try {
	 * ArrayList<OBDtoAdcGroup> groupList =
	 * dashManager.getGroupAdcList(testUserAccountIndex); for(OBDtoAdcGroup group:
	 * groupList) { System.out.println("group index/name/description = " +
	 * group.getIndex() + "/" + group.getName() + "(" + group.getDescription()
	 * +")"); System.out.println("\t all/unavailable = " + group.getAdcCount() + "/"
	 * + group.getAdcUnavailCount()); for(OBDtoAdcInfo adc:group.getAdcList()) {
	 * System.out.println("\t\t adc/status = " + adc.getName() + "/" +
	 * adc.getStatus()); } } } catch(OBException e) { System.out.println("error = "
	 * + e.getMessage()); e.printStackTrace(); } }
	 */
//	@Override
//	public ArrayList<OBDtoDashboardSdsAdcSummary> getAdcListAll(Integer accountIndex, Integer adcStatus, Integer faultUnsolvedLimitDays) throws OBException
//	{
//		try
//		{
//			return getAdcList(accountIndex, adcStatus, TARGET_ALL, null, faultUnsolvedLimitDays, OBDefine.ORDER_TYPE_ADCNAME, OBDefine.ORDER_DIR_ASCEND);
//		}
//		catch(OBException e)
//		{
//            throw new OBException(OBException.ERRCODE_DASHBOARD_ADCGET , e);
//		}
//	}

//	@Override
//	public ArrayList<OBDtoDashboardSdsAdcSummary> getAdcListGroup(Integer accountIndex, Integer adcStatus, Integer groupIndex, Integer faultUnsolvedLimitDays) throws OBException
//	{
//		try
//		{
//			return getAdcList(accountIndex, adcStatus, TARGET_GROUP, groupIndex, faultUnsolvedLimitDays, OBDefine.ORDER_TYPE_ADCNAME, OBDefine.ORDER_DIR_ASCEND);
//		}
//		catch(OBException e)
//		{
//			throw new OBException(OBException.ERRCODE_DASHBOARD_ADCGET , e);
//		}
//	}
//
//	@Override
//	public ArrayList<OBDtoDashboardSdsAdcSummary> getAdcListSingle(Integer adcStatus, Integer adcIndex, Integer faultUnsolvedLimitDays) throws OBException
//	{
//		try
//		{
//			return getAdcList(null, adcStatus, TARGET_ADC, adcIndex, faultUnsolvedLimitDays, OBDefine.ORDER_TYPE_ADCNAME, OBDefine.ORDER_DIR_ASCEND);
//		}
//		catch(OBException e)
//		{
//			throw new OBException(OBException.ERRCODE_DASHBOARD_ADCGET , e);
//		}
//	}
	/*
	 * public static void main(String[] args) { OBDashboardSdsImpl dashManager = new
	 * OBDashboardSdsImpl();
	 * 
	 * try { int testUserAccountIndex = 1; int testStatus = STATUS_ADC_AVAILABLE;
	 * int testGroup = 1; int testAdc = 13;
	 * 
	 * //all adc test ArrayList<OBDtoDashboardSdsAdcSummary> adcSummaryList =
	 * dashManager.getAdcListAll(testUserAccountIndex, testStatus);
	 * 
	 * // //group adc test // adcSummaryList =
	 * dashManager.getAdcListGroup(testUserAccountIndex, testStatus, testGroup); //
	 * // //single adc test // adcSummaryList =
	 * dashManager.getAdcListSingle(testStatus, testAdc);
	 * 
	 * //print results System.out.
	 * println("index, name, status, vs[all,available,unavailable,unavailableLong,disable], conn, throu, cpu, mem"
	 * );
	 * 
	 * for(OBDtoDashboardSdsAdcSummary adc: adcSummaryList) {
	 * System.out.println(adc.getIndex() + ", " + adc.getName() + ", " +
	 * adc.getStatus() + ", vs[" + adc.getCountVs() + ", " + adc.getCountVsAvail() +
	 * ", " + adc.getCountVsUnavail() + ", " + adc.getCountVsUnavailLong() + ", " +
	 * adc.getCountVsDisable() + "], " + adc.getConnection() + ", " +
	 * adc.getThroughput() + ", " + adc.getCpu() + ", " + adc.getMemory()); } }
	 * catch(OBException e) { System.out.println("error = " + e.getMessage());
	 * e.printStackTrace(); } }
	 */

//	@Override
//	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListAllAdc(Integer accountIndex, Integer vservStatus, Integer faultUnsolvedLimitDays) throws OBException
//	{
//		try
//		{
//			return getVservList(accountIndex, vservStatus, TARGET_ALL, null, faultUnsolvedLimitDays, OBDefine.ORDER_TYPE_VSIPADDRESS, OBDefine.ORDER_DIR_ASCEND);
//		}
//		catch(OBException e)
//		{
//			throw new OBException(OBException.ERRCODE_DASHBOARD_VSGET , e);
//		}
//	}
	/*
	 * public static void main(String[] args) { OBDashboardSdsImpl dashManager = new
	 * OBDashboardSdsImpl();
	 * 
	 * try { int testUserAccountIndex = 1; int testStatus = STATUS_VS_ALL;
	 * 
	 * //all vs test ArrayList<OBDtoDashboardSdsVservSummary> vservSumList =
	 * dashManager.getVservListAllAdc(testUserAccountIndex, testStatus); System.out.
	 * println("updatetime, adcname, vservIndex, vservIp, vservPort, vservStatus, vservConn, vservThrou"
	 * ); for(OBDtoDashboardSdsVservSummary vserv: vservSumList) {
	 * System.out.println(vserv.getUpdateTime() + ", " + vserv.getAdcName() + ", " +
	 * vserv.getVservIndex() + ", " + vserv.getVservIp() + ", " +
	 * vserv.getVservport() + ", " + vserv.getVservStatus() + ", " +
	 * vserv.getVservConnection() + ", " + vserv.getVservThroughput()); } }
	 * catch(OBException e) { System.out.println("error = " + e.getMessage());
	 * e.printStackTrace(); } }
	 */
//	@Override
//	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListGroupAdc(Integer accountIndex, Integer vservStatus, Integer groupIndex, Integer faultUnsolvedLimitDays) throws OBException
//	{
//		try
//		{
//			return getVservList(accountIndex, vservStatus, TARGET_GROUP, groupIndex, faultUnsolvedLimitDays, OBDefine.ORDER_TYPE_VSIPADDRESS, OBDefine.ORDER_DIR_ASCEND);
//		}
//		catch(OBException e)
//		{
//			throw new OBException(OBException.ERRCODE_DASHBOARD_VSGET , e);
//		}
//	}

	/*
	 * public static void main(String[] args) { OBDashboardSdsImpl dashManager = new
	 * OBDashboardSdsImpl();
	 * 
	 * try { int testUserAccountIndex = 1; int testStatus = STATUS_VS_DISABLE; int
	 * testGroup = 1;
	 * 
	 * //group vs test ArrayList<OBDtoDashboardSdsVservSummary> vservSumList =
	 * dashManager.getVservListGroupAdc(testUserAccountIndex, testStatus,
	 * testGroup); System.out.
	 * println("type(server=0/service=1), occurtime, adcname, vservIp, vservPort, vservStatus, vservConn, vservThrou"
	 * ); for(OBDtoDashboardSdsVservSummary vserv: vservSumList) {
	 * System.out.println(vserv.getRecordType() + ", " + vserv.getUpdateTime() +
	 * ", " + vserv.getAdcName() + ", " + vserv.getVservIp() + ", " +
	 * vserv.getVservport() + ", " + vserv.getVservStatus() + ", " +
	 * vserv.getVservConnection() + ", " + vserv.getVservThroughput()); } }
	 * catch(OBException e) { System.out.println("error = " + e.getMessage());
	 * e.printStackTrace(); } }
	 */
//	@Override
//	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListSingleAdc(Integer vservStatus, Integer adcIndex, Integer faultUnsolvedLimitDays) throws OBException
//	{
//		try
//		{
//			return getVservList(null, vservStatus, TARGET_ADC, adcIndex, faultUnsolvedLimitDays, OBDefine.ORDER_TYPE_VSIPADDRESS, OBDefine.ORDER_DIR_ASCEND);
//		}
//		catch(OBException e)
//		{
//			throw new OBException(OBException.ERRCODE_DASHBOARD_VSGET , e);
//		}
//	}
	/*
	 * public static void main(String[] args) { OBDashboardSdsImpl dashManager = new
	 * OBDashboardSdsImpl();
	 * 
	 * try { //int testUserAccountIndex = 1; int testStatus = STATUS_VS_DISABLE;
	 * //int testGroup = 1; int testAdc = 1;
	 * 
	 * //single vs test ArrayList<OBDtoDashboardSdsVservSummary> vservSumList =
	 * dashManager.getVservListSingleAdc(testStatus, testAdc); System.out.
	 * println("adcname, vservIp, vservPort, vservStatus, vservConn, vservThrou");
	 * for(OBDtoDashboardSdsVservSummary vserv: vservSumList) {
	 * System.out.println(vserv.getAdcName() + ", " + vserv.getVservIp() + ", " +
	 * vserv.getVservport() + ", " + vserv.getVservStatus() + ", " +
	 * vserv.getVservConnection() + ", " + vserv.getVservThroughput()); } }
	 * catch(OBException e) { System.out.println("error = " + e.getMessage());
	 * e.printStackTrace(); } }
	 */

//	@Override
//	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListAllAdcUnavailNDays(Integer accountIndex, Integer faultUnsolvedLimitDays) throws OBException
//	{
//		try
//		{
//			return getVservList(accountIndex, STATUS_VS_UNAVAILABLE_LONG, TARGET_ALL, null, faultUnsolvedLimitDays, OBDefine.ORDER_TYPE_VSIPADDRESS, OBDefine.ORDER_DIR_ASCEND);
//		}
//		catch(OBException e)
//		{
//			throw new OBException(OBException.ERRCODE_DASHBOARD_VSGET , e);
//		}
//	}
	/*
	 * public static void main(String[] args) { OBDashboardSdsImpl dashManager = new
	 * OBDashboardSdsImpl();
	 * 
	 * try { int testUserAccountIndex = 1;
	 * 
	 * ArrayList<OBDtoDashboardSdsVservSummary> vservSumList =
	 * dashManager.getVservListAllAdcUnavailNDays(testUserAccountIndex); System.out.
	 * println("adcname, vservIp, vservPort, vservStatus, vservConn, vservThrou");
	 * for(OBDtoDashboardSdsVservSummary vserv: vservSumList) {
	 * System.out.println(vserv.getAdcName() + ", " + vserv.getVservIp() + ", " +
	 * vserv.getVservport() + ", " + vserv.getVservStatus() + ", " +
	 * vserv.getVservConnection() + ", " + vserv.getVservThroughput()); } }
	 * catch(OBException e) { System.out.println("error = " + e.getMessage());
	 * e.printStackTrace(); } }
	 */
//	@Override
//	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListGroupAdcUnavailNDays(Integer accountIndex, Integer groupIndex, Integer faultUnsolvedLimitDays) throws OBException
//	{
//		try
//		{
//			return getVservList(accountIndex, STATUS_VS_UNAVAILABLE_LONG, TARGET_GROUP, groupIndex, faultUnsolvedLimitDays, OBDefine.ORDER_TYPE_VSIPADDRESS, OBDefine.ORDER_DIR_ASCEND);
//		}
//		catch(OBException e)
//		{
//			throw new OBException(OBException.ERRCODE_DASHBOARD_VSGET , e);
//		}
//	}
//	@Override
//	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListSingleAdcUnavailNDays(Integer adcIndex, Integer faultUnsolvedLimitDays) throws OBException
//	{
//		try
//		{
//			return getVservList(null, STATUS_VS_UNAVAILABLE_LONG, TARGET_ADC, adcIndex, faultUnsolvedLimitDays, OBDefine.ORDER_TYPE_VSIPADDRESS, OBDefine.ORDER_DIR_ASCEND);
//		}
//		catch(OBException e)
//		{
//			throw new OBException(OBException.ERRCODE_DASHBOARD_VSGET , e);
//		}
//	}

//	@Override
//	public ArrayList<OBDtoDashboardSdsIssueSummary> getIssueListAllAdc(Integer accountIndex, Integer issueStatus, Integer faultUnsolvedLimitDays) throws OBException
//	{
//		try
//		{
//			return getIssueList(accountIndex, issueStatus, TARGET_ALL, null, faultUnsolvedLimitDays, OBDefine.ORDER_TYPE_OCCURTIME, OBDefine.ORDER_DIR_DESCEND);
//		}
//		catch(OBException e)
//		{
//			throw new OBException(OBException.ERRCODE_DASHBOARD_FAULT , e);
//		}
//	}

	/*
	 * public static void main(String[] args) { OBDashboardSdsImpl dashManager = new
	 * OBDashboardSdsImpl(); try { int testAccountIndex = 1; int testIssueStatus =
	 * STATUS_ISSUE_UNSOLVED; int issueUnsolvedMaxDays = 0; //all issue
	 * ArrayList<OBDtoDashboardSdsIssueSummary> issueList =
	 * dashManager.getIssueListAllAdc(testAccountIndex, testIssueStatus,
	 * faultUnsolvedLimitDays); for(OBDtoDashboardSdsIssueSummary issue:issueList) {
	 * System.out.println("------------------------------------------" );
	 * System.out.println("index      = " + issue.getIssueIndex());
	 * System.out.println("issue type = " + issue.getIssueType());
	 * System.out.println("adc name   = " + issue.getAdcName());
	 * System.out.println("vir serv ip= " + issue.getVservIp());
	 * System.out.println("member ip  = " + issue.getMemberIp());
	 * System.out.println("interface  = " + issue.getInterfaceNumber());
	 * System.out.println("occur time = " + issue.getOccurTime());
	 * System.out.println("level      = " + issue.getIssueLevel());
	 * System.out.println("status     = " + issue.getStatus()); } }
	 * catch(OBException e) { System.out.println("error = " + e.getMessage());
	 * e.printStackTrace(); } }
	 */
//	@Override
//	public ArrayList<OBDtoDashboardSdsIssueSummary> getIssueListGroupAdc(Integer accountIndex, Integer issueStatus, Integer groupIndex, Integer faultUnsolvedLimitDays) throws OBException
//	{
//		try
//		{
//			return getIssueList(accountIndex, issueStatus, TARGET_GROUP, groupIndex, faultUnsolvedLimitDays, OBDefine.ORDER_TYPE_OCCURTIME, OBDefine.ORDER_DIR_DESCEND);
//		}
//		catch(OBException e)
//		{
//			throw new OBException(OBException.ERRCODE_DASHBOARD_FAULT , e);
//		}
//	}
	/*
	 * public static void main(String[] args) { OBDashboardSdsImpl dashManager = new
	 * OBDashboardSdsImpl(); try { int testAccountIndex = 1; int testIssueStatus =
	 * STATUS_ISSUE_ALL; int testGroupIndex = 5; //all issue
	 * ArrayList<OBDtoDashboardSdsIssueSummary> issueList =
	 * dashManager.getIssueListGroupAdc(testAccountIndex, testIssueStatus,
	 * testGroupIndex); System.out.println("list count = " + issueList.size());
	 * 
	 * for(OBDtoDashboardSdsIssueSummary issue:issueList) {
	 * System.out.println("------------------------------------------" );
	 * System.out.println("index      = " + issue.getIssueIndex());
	 * System.out.println("issue type = " + issue.getIssueType());
	 * System.out.println("adc name   = " + issue.getAdcName());
	 * System.out.println("vir serv ip= " + issue.getVservIp());
	 * System.out.println("member ip  = " + issue.getMemberIp());
	 * System.out.println("interface  = " + issue.getInterfaceNumber());
	 * System.out.println("occur time = " + issue.getOccurTime());
	 * System.out.println("level      = " + issue.getIssueLevel());
	 * System.out.println("status     = " + issue.getStatus()); } }
	 * catch(OBException e) { System.out.println("error = " + e.getMessage());
	 * e.printStackTrace(); } }
	 */
//	@Override
//	public ArrayList<OBDtoDashboardSdsIssueSummary> getIssueListSingleAdc(Integer issueStatus, Integer adcIndex, Integer faultUnsolvedLimitDays) throws OBException
//	{
//		try
//		{
//			return getIssueList(null, issueStatus, TARGET_ADC, adcIndex, faultUnsolvedLimitDays, OBDefine.ORDER_TYPE_OCCURTIME, OBDefine.ORDER_DIR_DESCEND);
//		}
//		catch(OBException e)
//		{
//			throw new OBException(OBException.ERRCODE_DASHBOARD_FAULT , e);
//		}
//	}
	/*
	 * public static void main(String[] args) { OBDashboardSdsImpl dashManager = new
	 * OBDashboardSdsImpl(); try { int testIssueStatus = STATUS_ISSUE_ALL; int
	 * testAdcIndex = 12; //all issue ArrayList<OBDtoDashboardSdsIssueSummary>
	 * issueList = dashManager.getIssueListSingleAdc(testIssueStatus, testAdcIndex);
	 * System.out.println("list count = " + issueList.size());
	 * 
	 * for(OBDtoDashboardSdsIssueSummary issue:issueList) {
	 * System.out.println("------------------------------------------" );
	 * System.out.println("index      = " + issue.getIssueIndex());
	 * System.out.println("issue type = " + issue.getIssueType());
	 * System.out.println("adc name   = " + issue.getAdcName());
	 * System.out.println("vir serv ip= " + issue.getVservIp());
	 * System.out.println("member ip  = " + issue.getMemberIp());
	 * System.out.println("interface  = " + issue.getInterfaceNumber());
	 * System.out.println("occur time = " + issue.getOccurTime());
	 * System.out.println("level      = " + issue.getIssueLevel());
	 * System.out.println("status     = " + issue.getStatus()); } }
	 * catch(OBException e) { System.out.println("error = " + e.getMessage());
	 * e.printStackTrace(); } }
	 */
	@Override
	public OBDtoConnection getAdcConnections(Integer adcIndex, Date beginTime, Date endTime) throws OBException {
		// 첫번째 dashboard의 ADC connection 함수를 그대로 쓴다. 4번째 파라미터는 logCount(Integer)인데, 쓰이지
		// 않으므로 0으로 줬다.
		return new OBDashboardImpl().getUsageConnections(adcIndex, beginTime, endTime, 0);
	}

	@Override
	public OBDtoThroughput getAdcThroughput(Integer adcIndex, Date beginTime, Date endTime) throws OBException {
		// 첫번째 dashboard의 ADC throughput 함수를 그대로 쓴다. 4번째 파라미터는 logCount(Integer)인데, 쓰이지
		// 않으므로 0으로 줬다.
		return new OBDashboardImpl().getUsageThroughput(adcIndex, beginTime, endTime, 0);
	}

	/*
	 * //getAdcConnections() && getAdcThroughput() public static void main(String[]
	 * args) { OBDashboardSdsImpl dashManager = new OBDashboardSdsImpl();
	 * 
	 * try { SimpleDateFormat formatter = new SimpleDateFormat
	 * ("yyyy-MM-dd hh:mm:ss"); String strTime = new String(); int testAdcIndex =
	 * 12;
	 * 
	 * OBDtoConnection connM = dashManager.getAdcConnections(testAdcIndex, null,
	 * null); for(OBDtoUsageConnection conn:connM.getConnectionList()) { strTime =
	 * formatter.format(conn.getOccurTime()); System.out.println("connection," +
	 * strTime + "," + conn.getConns()); } OBDtoThroughput throuM =
	 * dashManager.getAdcThroughput(testAdcIndex, null, null);
	 * for(OBDtoUsageThroughput throu:throuM.getThroughputList()) { strTime =
	 * formatter.format(throu.getOccurTime()); System.out.println("throughput," +
	 * strTime + "," + throu.getBps()); } } catch(OBException e) {
	 * System.out.println("error = " + e.getMessage()); e.printStackTrace(); } }
	 */
	@Override
	public OBDtoCpu getAdcUsageCpu(Integer adcIndex, Date beginTime, Date endTime) throws OBException {
		// 첫번째 dashboard의 ADC cpu 사용율 구하는 함수를 그대로 쓴다. 4번째 파라미터는 logCount(Integer)인데, 쓰이지
		// 않으므로 0으로 줬다.
		return new OBDashboardImpl().getUsageCpu(adcIndex, beginTime, endTime, 0);
	}

	@Override
	public ArrayList<OBDtoGroupHistory> getAdcUsageCpuGroup(Integer adcGroupIndex, Date beginTime, Date endTime)
			throws OBException {
		// 첫번째 dashboard의 ADC cpu 사용율 구하는 함수를 그대로 쓴다. 4번째 파라미터는 logCount(Integer)인데, 쓰이지
		// 않으므로 0으로 줬다.
		return new OBDashboardImpl().getUsageCpuGroup(adcGroupIndex, beginTime, endTime, 0);
	}

	@Override
	public OBDtoMemory getAdcUsageMem(Integer adcIndex, Date beginTime, Date endTime) throws OBException {
		// 첫번째 dashboard의 ADC memory 사용율 구하는 함수를 그대로 쓴다. 4번째 파라미터는 logCount(Integer)인데,
		// 쓰이지 않으므로 0으로 줬다.
		return new OBDashboardImpl().getUsageMem(adcIndex, beginTime, endTime, 0);
	}

	@Override
	public ArrayList<OBDtoGroupHistory> getAdcUsageMemGroup(Integer adcGroupIndex, Date beginTime, Date endTime)
			throws OBException {
		// 첫번째 dashboard의 ADC memory 사용율 구하는 함수를 그대로 쓴다. 4번째 파라미터는 logCount(Integer)인데,
		// 쓰이지 않으므로 0으로 줬다.
		return new OBDashboardImpl().getUsageMemGroup(adcGroupIndex, beginTime, endTime, 0);
	}

	// main for : getAdcUsageCpu() and getAdcUsageMem()
	/*
	 * public static void main(String[] args) { OBDashboardSdsImpl dashManager = new
	 * OBDashboardSdsImpl();
	 * 
	 * try { SimpleDateFormat formatter = new SimpleDateFormat
	 * ("yyyy-MM-dd hh:mm:ss"); String strTime = new String(); int testAdcIndex =
	 * 12;
	 * 
	 * OBDtoCpu cpuM = dashManager.getAdcUsageCpu(testAdcIndex, null, null);
	 * for(OBDtoUsageCpu cpu:cpuM.getCpuList()) { strTime =
	 * formatter.format(cpu.getOccurTime()); System.out.println("cpu," + strTime +
	 * "," + cpu.getUsage()); } OBDtoMemory memM =
	 * dashManager.getAdcUsageMem(testAdcIndex, null, null); for(OBDtoUsageMem
	 * mem:memM.getMemList()) { strTime = formatter.format(mem.getOccurTime());
	 * System.out.println("mem," + strTime + "," + mem.getUsage()); } }
	 * catch(OBException e) { System.out.println("error = " + e.getMessage());
	 * e.printStackTrace(); } }
	 */
	@Override
	public OBDtoConnection getVservConnections(Integer recordType, Integer adcIndex, String vservIndex,
			Integer vservPort, Date beginTime, Date endTime) throws OBException {
		// 첫번째 dashboard의 ADC connection 함수를 그대로 쓴다. 4번째 파라미터는 logCount(Integer)인데, 쓰이지
		// 않으므로 0으로 줬다.
		OBDashboardImpl dashboardOrg = new OBDashboardImpl();
		try {
			if (recordType.equals(RECORD_TYPE_VSERVER)) {
				return dashboardOrg.getUsageConnections(adcIndex, vservIndex, beginTime, endTime, 0);
			} else {
				return dashboardOrg.getUsageConnections(adcIndex, vservIndex, vservPort, beginTime, endTime, 0);
			}
		} catch (OBException e) {
			throw e;
		}
	}

	@Override
	public OBDtoThroughput getVservThroughput(Integer recordType, Integer adcIndex, String vservIndex,
			Integer vservPort, Date beginTime, Date endTime) throws OBException {
		// 첫번째 dashboard의 ADC throughput 함수를 그대로 쓴다. 4번째 파라미터는 logCount(Integer)인데, 쓰이지
		// 않으므로 0으로 줬다.
		OBDashboardImpl dashboardOrg = new OBDashboardImpl();
		try {
			if (recordType.equals(RECORD_TYPE_VSERVER)) {
				return dashboardOrg.getUsageThroughput(adcIndex, vservIndex, beginTime, endTime, 0);
			} else {
				return dashboardOrg.getUsageThroughput(adcIndex, vservIndex, vservPort, beginTime, endTime);
			}
		} catch (OBException e) {
			throw e;
		}
	}

	@Override
	public OBDtoDashboardSdsAdcInfo getAdcInfo(Integer adcIndex, Integer faultUnsolvedLimitDays) throws OBException {
		OBDtoDashboardSdsAdcInfo adc = new OBDtoDashboardSdsAdcInfo();
		OBDatabase db = new OBDatabase();
		ResultSet rs;

		String sqlText = "";
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT ADC.INDEX, ADC.NAME, ADC.STATUS, ADC.IPADDRESS, ADC.TYPE, ADC.MODEL, ADC.SW_VERSION, \n"
							+ "    ADC.LAST_BOOT_TIME, RES.CPU_USAGE, RES.MEM_USAGE, RES.VS_CUR_CONNS, RES.THROUGHPUT,      \n"
							+ "    RES.VS_COUNT, RES.VS_COUNT_AVAIL, RES.VS_COUNT_UNAVAIL, RES.VS_COUNT_DISABLED            \n"
							+ " FROM ( SELECT INDEX, NAME, STATUS, IPADDRESS, TYPE, MODEL, SW_VERSION, LAST_BOOT_TIME       \n"
							+ "    FROM MNG_ADC WHERE INDEX = %d                                                            \n"
							+ "    ) ADC                                                                                    \n"
							+ " LEFT JOIN (                                                                                 \n"
							+ "    SELECT A.ADC_INDEX, A.OCCUR_TIME, A.CPU1_USAGE AS CPU_USAGE, A.MEM_USAGE,                \n"
							+ "       B.CONN_CURR VS_CUR_CONNS, B.BPS_IN AS THROUGHPUT,        					          \n"
							+ "       B.VS_COUNT, B.VS_COUNT_AVAIL, B.VS_COUNT_UNAVAIL, B.VS_COUNT_DISABLED                 \n"
							+ "   FROM LOG_RESC_CPUMEM            A                                                   \n"
							+ "   INNER JOIN LOG_ADC_PERF_STATS   B                                                   \n"
							+ "   ON B.OCCUR_TIME = A.OCCUR_TIME AND B.ADC_INDEX = A.ADC_INDEX                              \n"
							+ "   WHERE A.ADC_INDEX = %d                                                                    \n"
							+ " ) RES                                                                                       \n"
							+ " ON ADC.INDEX = RES.ADC_INDEX                                                                \n"
							+ " ORDER BY ADC.NAME;                                                                          \n",
					adcIndex, adcIndex);

//			System.out.println(sqlText);
			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				adc.setIndex(db.getInteger(rs, "INDEX"));
				adc.setName(db.getString(rs, "NAME"));
				adc.setStatus(db.getInteger(rs, "STATUS"));
				adc.setIp(db.getString(rs, "IPADDRESS"));
				adc.setType(db.getInteger(rs, "TYPE"));
				adc.setModel(db.getString(rs, "MODEL"));
				adc.setOs(db.getString(rs, "SW_VERSION"));
				adc.setUptime(db.getTimestamp(rs, "LAST_BOOT_TIME"));
				adc.setCountVs(db.getInteger(rs, "VS_COUNT"));
				adc.setCountVsAvail(db.getInteger(rs, "VS_COUNT_AVAIL"));
				adc.setCountVsUnavail(db.getInteger(rs, "VS_COUNT_UNAVAIL"));
				adc.setCountVsDisable(db.getInteger(rs, "VS_COUNT_DISABLED"));
				adc.setConnection(db.getLong(rs, "VS_CUR_CONNS"));
				adc.setThroughput(db.getLong(rs, "THROUGHPUT"));
				adc.setCpu(db.getInteger(rs, "CPU_USAGE"));
				adc.setMemory(db.getInteger(rs, "MEM_USAGE"));
			}

			// 아래 장애 카운트 조회는 기능이 쓰이지 않고 시간이 오래 걸리므로 지운다. AX3.0에서 본 함수 getAdcInfo() 자체가 쓰이지
			// 않고 있다.
			// OBDtoFaultStatusCount fault = this.getFaultStatusCount(null, adcIndex,
			// faultUnsolvedLimitDays, db); //특정 ADC에 대해 조회시 accountIndex=null, adcIndex>0
			// adc.setCountNdaysFault(fault.getFault()); //최근 7일 발생 장애 총 건수. 해결+미해결
			adc.setCountNdaysFault(-1); // 윗줄에서 기능을 제거하여 없는 값으로 처리함
			return adc;
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
	public OBDtoDashboardSdsVservInfo getVservInfo(Integer recordType, String vsIndex, Integer port, Integer adcIndex)
			throws OBException {
//		OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("xxxxxxxxxxxxxxxxxxport:%d, vservIndex:%s", port, vservIndex));

		OBDtoDashboardSdsVservInfo vserv = new OBDtoDashboardSdsVservInfo();
		OBDatabase db = new OBDatabase();
		OBAdcManagementImpl adcManager = new OBAdcManagementImpl();

		final int recentNDays = 7; // 최근 7일간 조회. 오늘 포함
		ResultSet rs, rs2, rs3;
		String sqlBase = "";
		String sqlVserver = "";
		String sqlVserviceAlteon = "";
		String sqlText = "";

		try {
			db.openDB();
			sqlVserver = String.format(
					" SELECT VS.INDEX, VS.NAME, VS.VIRTUAL_IP, VS.VIRTUAL_PORT, VS.STATUS, VS.POOL_INDEX, VS.ADC_INDEX, \n"
							+ " VSRESC.CONN_CURR, (VSRESC.BPS_IN + VSRESC.BPS_OUT) as THROUGHPUT,                      \n"
							+ " ADCRESC.CPU1_USAGE, ADCRESC.MEM_USAGE                                                  \n"
							+ " FROM                                                                                   \n"
							+ " (    SELECT INDEX, NAME, VIRTUAL_IP, VIRTUAL_PORT, STATUS, POOL_INDEX, ADC_INDEX       \n"
							+ "      FROM TMP_SLB_VSERVER                                                              \n"
							+ "      WHERE INDEX = %s                                                                  \n"
							+ " ) VS                                                                                   \n"
							+ " LEFT JOIN                                                                              \n"
							+ " (    SELECT B.INDEX AS VS_INDEX, A.CONN_CURR, A.BPS_IN, A.BPS_OUT                      \n"
							+ "      FROM TMP_FAULT_SVC_PERF_STATS A                                                   \n"
							+ "      INNER JOIN TMP_SLB_VSERVER    B                                                   \n"
							+ "      ON A.OBJ_INDEX = B.INDEX                                                          \n"
							+ "      WHERE B.INDEX = %s                                                                \n"
							+ " ) VSRESC                                                                               \n"
							+ " ON VS.INDEX = VSRESC.VS_INDEX,                                                         \n"
							+ " ( SELECT ADC_INDEX, CPU1_USAGE, MEM_USAGE                                              \n"
							+ "   FROM LOG_RESC_CPUMEM                                                               \n"
							+ "   WHERE ADC_INDEX = %d                                                                 \n"
							+ " ) ADCRESC                                                                              \n"
							+ " WHERE                                                                                  \n"
							+ "     VS.ADC_INDEX = ADCRESC.ADC_INDEX                                                   \n"
							+ " ORDER BY VS.INDEX;                                                                     \n",
					OBParser.sqlString(vsIndex), OBParser.sqlString(vsIndex), adcIndex);

			sqlVserviceAlteon = String.format(
					" SELECT VS.INDEX, VS.NAME, VS.VIRTUAL_IP, VS.VIRTUAL_PORT, VS.STATUS, VS.POOL_INDEX, VS.ADC_INDEX, \n"
							+ " VSSRESC.CONN_CURR, VSSRESC.BPS_IN as THROUGHPUT ,                                      \n"
							+ " ADCRESC.CPU1_USAGE, ADCRESC.MEM_USAGE                                                   \n"
							+ " FROM                                                                                   \n"
							+ " (    SELECT INDEX, NAME, VIRTUAL_IP, VIRTUAL_PORT, STATUS, POOL_INDEX, ADC_INDEX       \n"
							+ "      FROM TMP_SLB_VSERVER                                                              \n"
							+ "      WHERE INDEX = %s                                                                  \n"
							+ " ) VS                                                                                   \n"
							+ " LEFT JOIN                                                                              \n"
							+ " (    SELECT B.INDEX AS VS_INDEX, A.CONN_CURR, A.BPS_IN                                 \n"
							+ "      FROM TMP_FAULT_SVC_PERF_STATS        A                                            \n"
							+ "      INNER JOIN TMP_SLB_VS_SERVICE        B                                            \n"
							+ "      ON B.INDEX = A.OBJ_INDEX                                                          \n"
							+ "      WHERE B.INDEX = %s AND B.VIRTUAL_PORT = %d                                        \n"
							+ " ) VSSRESC                                                                              \n"
							+ " ON VS.INDEX = VSSRESC.VS_INDEX,                                                        \n"
							+ " ( SELECT ADC_INDEX, CPU1_USAGE, MEM_USAGE                                              \n"
							+ "   FROM LOG_RESC_CPUMEM                                                           \n"
							+ "   WHERE ADC_INDEX = %d                                                                 \n"
							+ " ) ADCRESC                                                                              \n"
							+ " WHERE VS.ADC_INDEX = ADCRESC.ADC_INDEX                                                 \n"
							+ " ORDER BY VS.INDEX;                                                                     \n",
					OBParser.sqlString(vsIndex), OBParser.sqlString(vsIndex), port, adcIndex);

			sqlText = String.format(
					" SELECT T1.LAST_CONFIG_TIME, T2.CONFIG_COUNT, T3.FAULT_COUNT                                                                                       \n"
							+ " FROM                                                                                                                                              \n"
							+ " ( SELECT OCCUR_TIME AS LAST_CONFIG_TIME FROM LOG_CONFIG_HISTORY WHERE LOG_SEQ = (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE VS_INDEX = %s) \n"
							+ " ) T1,                                                                                                                                             \n"
							+ " ( SELECT COUNT(LOG_SEQ) AS CONFIG_COUNT FROM LOG_CONFIG_HISTORY WHERE VS_INDEX = %s AND OCCUR_TIME >= CURRENT_DATE - %d                           \n"
							+ " ) T2,                                                                                                                                             \n"
							+ " ( SELECT COUNT(LOG_SEQ) AS FAULT_COUNT FROM LOG_ADC_FAULT WHERE VS_INDEX = %s AND OCCUR_TIME >= CURRENT_DATE - %d                                 \n"
							+ " ) T3                                                                                                                                              \n",
					OBParser.sqlString(vsIndex), OBParser.sqlString(vsIndex), recentNDays - 1,
					OBParser.sqlString(vsIndex), recentNDays - 1); // 오늘 포함이므로 하루 뺀다. 6일전부터.

			if (recordType.equals(RECORD_TYPE_VSERVER)) {
				sqlBase = sqlVserver;
			} else // RECORD_TYPE_VSERVICE
			{
				sqlBase = sqlVserviceAlteon;
			}

			Integer vservPort = 0;
			Integer adcType = adcManager.getAdcType(adcIndex);
//			System.out.println(sqlBase);
			rs = db.executeQuery(sqlBase);

			while (rs.next()) {
				vserv.setIndex(db.getString(rs, "INDEX"));
				vserv.setName(db.getString(rs, "NAME"));
				vserv.setIp(db.getString(rs, "VIRTUAL_IP"));

				vservPort = db.getInteger(rs, "VIRTUAL_PORT");
				vserv.setPort(vservPort);

				vserv.setStatus(db.getInteger(rs, "STATUS"));
				vserv.setConnection(db.getLong(rs, "CONN_CURR"));
				vserv.setThroughput(db.getLong(rs, "THROUGHPUT"));
				vserv.setCpu(db.getInteger(rs, "CPU1_USAGE"));
				vserv.setMemory(db.getInteger(rs, "MEM_USAGE"));
//				poolIndex = db.getString(rs, "POOL_INDEX");
			}

//			System.out.println(sqlCommon);
			rs2 = db.executeQuery(sqlText);

			while (rs2.next()) // SELECT T1.LAST_CONFIG_TIME, T2.CONFIG_COUNT, T3.FAULT_COUNT ...
			{
				vserv.setLastConfigTime(db.getTimestamp(rs2, "LAST_CONFIG_TIME"));
				vserv.setCountNdaysConfig(db.getInteger(rs2, "CONFIG_COUNT"));
				vserv.setCountNdaysFault(db.getInteger(rs2, "FAULT_COUNT"));
			}

			int memberTotal = 0;
			int memberAvailable = 0;
			int memberUnavailable = 0;
			int memberDisable = 0;
			int memberStatus = 0;
			// F5 server거나, ALTEON service, 인 경우에 member를 구한다.
			if (adcType.equals(OBDefine.ADC_TYPE_F5) || adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PAS)
					|| adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PASK)
					|| (adcType.equals(OBDefine.ADC_TYPE_ALTEON) && recordType.equals(RECORD_TYPE_VSERVICE))) {
				if (adcType.equals(OBDefine.ADC_TYPE_ALTEON)) // Alteon: virtual service index
				{
					sqlText = String.format(
							" SELECT MEMBER_STATUS, COUNT(MEMBER_STATUS)                                    \n"
									+ " FROM TMP_SLB_POOLMEMBER_STATUS      		                                    \n"
									+ " WHERE VSRC_INDEX = (SELECT INDEX FROM TMP_SLB_VS_SERVICE WHERE VS_INDEX = %s AND VIRTUAL_PORT = %d) \n"
									+ " GROUP BY MEMBER_STATUS                                                        \n",
							OBParser.sqlString(vsIndex), port);
				} else // F5, PAS, PASK : 조건은 pool index
				{
					sqlText = String.format(
							" SELECT MEMBER_STATUS, COUNT(MEMBER_STATUS)                                    \n"
									+ " FROM TMP_SLB_POOLMEMBER_STATUS      		                                    \n"
									+ " WHERE VS_INDEX = %s AND VSRC_INDEX =	 (SELECT POOL_INDEX FROM TMP_SLB_VSERVER WHERE INDEX = %s)  \n"
									+ " GROUP BY MEMBER_STATUS                                                        \n",
							OBParser.sqlString(vsIndex), OBParser.sqlString(vsIndex));
				}
				rs3 = db.executeQuery(sqlText);

				while (rs3.next()) {
					memberStatus = db.getInteger(rs3, "MEMBER_STATUS");
					if (memberStatus == OBDefine.MEMBER_STATUS.AVAILABLE) {
						memberAvailable += db.getInteger(rs3, "COUNT");
					} else if (memberStatus == OBDefine.MEMBER_STATUS.UNAVAILABLE) {
						memberUnavailable += db.getInteger(rs3, "COUNT");
					} else if (memberStatus == OBDefine.MEMBER_STATUS.DISABLE) {
						memberDisable += db.getInteger(rs3, "COUNT");
					}
					memberTotal += db.getInteger(rs3, "COUNT");
				}
			}
			vserv.setCountMember(memberTotal);
			vserv.setCountMemberAvail(memberAvailable);
			vserv.setCountMemberUnavail(memberUnavailable);
			vserv.setCountMemberDisable(memberDisable);
			return vserv;
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

	/*
	 * public static void main(String[] args) { OBDashboardSdsImpl dashManager = new
	 * OBDashboardSdsImpl();
	 * 
	 * try { //int testRecordType = RECORD_TYPE_VSERVER; int testRecordType =
	 * RECORD_TYPE_VSERVICE; String testVservIndex = "7_160"; int testPort = 80; int
	 * testAdcIndex = 7;
	 * 
	 * //all vs test OBDtoDashboardSdsVservInfo vserv =
	 * dashManager.getVservInfo(testRecordType, testVservIndex, testPort,
	 * testAdcIndex); System.out.println("vs name      = " + vserv.getName());
	 * System.out.println("vs ip        = " + vserv.getIp());
	 * System.out.println("vs port      = " + vserv.getPort());
	 * System.out.println("vs status    = " + vserv.getStatus());
	 * System.out.println("member all   = " + vserv.getCountMember());
	 * System.out.println("member avail = " + vserv.getCountMemberAvail());
	 * System.out.println("member unava = " + vserv.getCountMemberUnavail());
	 * System.out.println("member disab = " + vserv.getCountMemberDisable());
	 * System.out.println("last config  = " + vserv.getLastConfigTime());
	 * System.out.println("nday config  = " + vserv.getCountNdaysConfig());
	 * System.out.println("nday fault   = " + vserv.getCountNdaysFault());
	 * System.out.println("connection   = " + vserv.getConnection());
	 * System.out.println("throughput   = " + vserv.getThroughput()); }
	 * catch(OBException e) { System.out.println("error = " + e.getMessage());
	 * e.printStackTrace(); } }
	 */
	@Override
	public ArrayList<OBDtoDashboardSdsMemberConnection> getVservMemberConnections(String vservIndex, Integer vservPort,
			Integer adcIndex) throws OBException {
		ArrayList<OBDtoDashboardSdsMemberConnection> memberConnList = new ArrayList<OBDtoDashboardSdsMemberConnection>();
		OBAdcManagementImpl adcManager = new OBAdcManagementImpl();
		Integer adcType = adcManager.getAdcType(adcIndex);

		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			if (adcType.equals(OBDefine.ADC_TYPE_F5) || adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PAS)
					|| adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PASK)) {
				sqlText = String.format(
						" SELECT MEMBER.IP_ADDRESS, MEMBER.PORT, RESC.CUR_CONNS, RESC.OCCUR_TIME                 \n"
								+ " FROM (                                                                                 \n"
								+ "     SELECT TMP_SLB_POOLMEMBER.PORT, TMP_SLB_POOLMEMBER.POOL_INDEX, NODE.IP_ADDRESS     \n"
								+ "     FROM TMP_SLB_POOLMEMBER                                                            \n"
								+ "     INNER JOIN TMP_SLB_NODE NODE                                                       \n"
								+ "     ON TMP_SLB_POOLMEMBER.NODE_INDEX = NODE.INDEX                                      \n"
								+ "     WHERE TMP_SLB_POOLMEMBER.POOL_INDEX = (SELECT POOL_INDEX FROM TMP_SLB_VSERVER WHERE INDEX = %s) \n"
								+ " ) MEMBER                                                                               \n"
								+ " LEFT JOIN (                                                                            \n"
								+ "     SELECT VS_INDEX, POOL_INDEX, PORT, ADDRESS, CUR_CONNS, OCCUR_TIME                  \n"
								+ "     FROM TMP_ADC_POOLMEM_RESC                                                          \n"
								+ "     WHERE VS_INDEX = %s                                                                \n"
								+ " ) RESC                                                                                 \n"
								+ " ON MEMBER.POOL_INDEX = RESC.POOL_INDEX                                                 \n"
								+ "     AND MEMBER.PORT = RESC.PORT                                                        \n"
								+ "     AND MEMBER.IP_ADDRESS = RESC.ADDRESS                                               \n",
						OBParser.sqlString(vservIndex), OBParser.sqlString(vservIndex));
			} else if (adcType.equals(OBDefine.ADC_TYPE_ALTEON)) {
				sqlText = String.format(
						" SELECT MEMBER.IP_ADDRESS, MEMBER.PORT, RESC.CUR_CONNS, RESC.OCCUR_TIME                \n"
								+ " FROM (                                                                                \n"
								+ "     SELECT TMP_SLB_POOLMEMBER.POOL_INDEX, TMP_SLB_POOLMEMBER.PORT, NODE.IP_ADDRESS    \n"
								+ "     FROM TMP_SLB_POOLMEMBER                                                           \n"
								+ "     INNER JOIN TMP_SLB_NODE NODE                                                      \n"
								+ "     ON TMP_SLB_POOLMEMBER.NODE_INDEX = NODE.INDEX                                     \n"
								+ "     WHERE TMP_SLB_POOLMEMBER.POOL_INDEX = (SELECT POOL_INDEX FROM TMP_SLB_VS_SERVICE WHERE VS_INDEX = %s AND VIRTUAL_PORT = %d)  \n"
								+ " ) MEMBER                                                                              \n"
								+ " LEFT JOIN (                                                                           \n"
								+ "     SELECT VS_INDEX, POOL_INDEX, ADDRESS, CUR_CONNS, OCCUR_TIME                       \n"
								+ "     FROM TMP_ADC_POOLMEM_RESC                                                         \n"
								+ "     WHERE VS_INDEX = %s                                                               \n"
								+ " ) RESC                                                                                \n"
								+ " ON MEMBER.POOL_INDEX = RESC.POOL_INDEX                                                \n"
								+ "     AND MEMBER.IP_ADDRESS = RESC.ADDRESS                                              \n",
						OBParser.sqlString(vservIndex), vservPort, OBParser.sqlString(vservIndex));
			}

//			System.out.println("sqlText = " + sqlText);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText = " + sqlText);

			long startA = System.currentTimeMillis();
			ResultSet rs = db.executeQuery(sqlText);
			long endA = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time>sql run: " + (endA - startA) / 1000.0);
			while (rs.next()) {
				OBDtoDashboardSdsMemberConnection memberConn = new OBDtoDashboardSdsMemberConnection();
				memberConn.setMemberIp(db.getString(rs, "IP_ADDRESS"));
				memberConn.setMemberPort(db.getInteger(rs, "PORT"));
				memberConn.setConns(db.getLong(rs, "CUR_CONNS"));
				memberConn.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				memberConnList.add(memberConn);
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

		return memberConnList;
	}

	/*
	 * public static void main(String[] args) { OBDashboardSdsImpl dashManager = new
	 * OBDashboardSdsImpl(); //ArrayList<OBDtoDashboardSdsMemberConnection>
	 * getVservMemberConnections(String vservIndex, Integer vservPort, Integer
	 * adcIndex) throws OBException try { String testVservIndex = "7_160"; int
	 * testPort = 80; int testAdcIndex = 7;
	 * 
	 * //all vs test ArrayList<OBDtoDashboardSdsMemberConnection> connList =
	 * dashManager.getVservMemberConnections(testVservIndex, testPort,
	 * testAdcIndex); for(OBDtoDashboardSdsMemberConnection conn:connList) {
	 * System.out.println("ip         = " + conn.getMemberIp());
	 * System.out.println("port       = " + conn.getMemberPort());
	 * System.out.println("connection = " + conn.getConns());
	 * System.out.println("time       = " + conn.getOccurTime()); } }
	 * catch(OBException e) { System.out.println("error = " + e.getMessage());
	 * e.printStackTrace(); } }
	 */
	@Override
	public ArrayList<OBDtoDashboardSdsVservStatus> getVservStatus(Integer adcIndex, Date beginTime, Date endTime)
			throws OBException {
		ArrayList<OBDtoDashboardSdsVservStatus> vservStatusList = new ArrayList<OBDtoDashboardSdsVservStatus>();

		OBDatabase db = new OBDatabase();
		String sqlText = "";
		try {
			db.openDB();

			String sqlTime = "";
			if (endTime == null)
				endTime = new Date();
			sqlTime = String.format(" OCCUR_TIME <= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));

			if (beginTime == null) {
				long count = new OBEnvManagementImpl().getLogViewPeriod(db);
				if (count == 0)
					count = 6 * 60 * 60 * 1000;// 6시간
				beginTime = new Date(endTime.getTime() - count * 60 * 60 * 1000);//
			}

			sqlTime += String.format(" AND OCCUR_TIME >= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			sqlText = String.format(
					" SELECT OCCUR_TIME, VS_COUNT_AVAIL, VS_COUNT_UNAVAIL, VS_COUNT_DISABLED \n"
							+ " FROM LOG_ADC_PERF_STATS                                                 \n"
							+ " WHERE ADC_INDEX=%d AND %s                                              \n"
							+ " ORDER BY OCCUR_TIME ASC ;                                             \n",
					adcIndex, sqlTime);

			// System.out.println("sqlText = " + sqlText);

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoDashboardSdsVservStatus vservStatus = new OBDtoDashboardSdsVservStatus();
				vservStatus.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				vservStatus.setVsAvail(db.getInteger(rs, "VS_COUNT_AVAIL"));
				vservStatus.setVsUnavail(db.getInteger(rs, "VS_COUNT_UNAVAIL"));
				vservStatus.setVsDisable(db.getInteger(rs, "VS_COUNT_DISABLED"));
				vservStatusList.add(vservStatus);
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

		return vservStatusList;
	}
//	public static void main(String[] args)
//	{
//		OBDashboardSdsImpl dashManager = new OBDashboardSdsImpl();
//
//		int testAdcIndex = 11;
//		try
//		{
//			ArrayList<OBDtoDashboardSdsVservStatus> vservStatusList = dashManager.getVservStatus(testAdcIndex, null, null);
//			System.out.println("vservStatus : Time, vservAvail, vservUnavail, vservDisable");
//			for(OBDtoDashboardSdsVservStatus status: vservStatusList)
//			{
//				System.out.println(status.getOccurTime() + ", " + status.getVsAvail() + ", " + status.getVsUnavail() +", " + status.getVsDisable());
//			}
//		}
//		catch(OBException e)
//		{
//			System.out.println("error = " + e.getMessage());
//			e.printStackTrace();
//		}
//	}

	public ArrayList<OBDtoDashboardSdsAdcSummary> getAdcList(Integer accountIndex, Integer targetStatus,
			Integer targetType, Integer targetIndex, Integer faultUnsolvedLimitDays, Integer orderType,
			Integer orderDir) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("accountIndex = %d, targetStatus = %d, targetType = %d, targetIndex = %d", accountIndex,
						targetStatus, targetType, targetIndex));

		ArrayList<OBDtoDashboardSdsAdcSummary> adcList = new ArrayList<OBDtoDashboardSdsAdcSummary>();
		String adcIndexSubqueryAdmin = "";
		String adcIndexSubqueryNonAdmin = "";
		String adcIndexSubquery = "";

		String sqlText = "";

		if (targetType.equals(TARGET_ALL)) {
			adcIndexSubqueryAdmin = String.format(" SELECT INDEX FROM MNG_ADC WHERE AVAILABLE = %d ",
					OBDefine.ADC_STATE.AVAILABLE);
			adcIndexSubqueryNonAdmin = String.format(" SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %d ",
					accountIndex);
		} else if (targetType.equals(TARGET_GROUP)) {
			adcIndexSubqueryAdmin = String.format(
					" SELECT INDEX FROM MNG_ADC WHERE AVAILABLE = %d AND GROUP_INDEX = %d ",
					OBDefine.ADC_STATE.AVAILABLE, targetIndex);
			adcIndexSubqueryNonAdmin = String.format(" SELECT INDEX FROM MNG_ADC \n"
					+ " WHERE INDEX IN ( SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %d ) \n" + // where-in:empty
																												// string
																												// 불가,
																												// null
																												// 불가,
																												// OK
					"     AND GROUP_INDEX = %d ", accountIndex, targetIndex);
		} else if (targetType.equals(TARGET_ADC)) // single adc 선택
		{
			adcIndexSubquery = String.format(" %d ", targetIndex);
			// adcIndexSubqueryNonAdmin = String.format(" %d ", targetIndex);
		}

		if (targetType != TARGET_ADC) // 특정 ADC를 지목한 경우는 계정 권한을 볼 필요가 없다. adc는 이미 하나로 정해졌기 때문
		{
			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accountIndex); // 최상단 함수가 아니므로 OBException을 catch하지 않고
																					// 던진다. 위에서 받는다.

			if (roleNo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);
			}
			if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
				adcIndexSubquery = adcIndexSubqueryAdmin;
			} else {
				adcIndexSubquery = adcIndexSubqueryNonAdmin;
			}
		}

		String adcStatusWhereClause = "";
		if (targetStatus == null) {
			// 에러, //TODO
		} else {
			if (targetStatus.equals(STATUS_ADC_ALL)) {
				adcStatusWhereClause = "true"; // 전부 다
			} else if (targetStatus.equals(STATUS_ADC_AVAILABLE)) {
				adcStatusWhereClause = String.format("ADC.STATUS = %d", OBDefine.ADC_STATUS.REACHABLE);
			} else if (targetStatus.equals(STATUS_ADC_UNAVAILABLE)) {
				adcStatusWhereClause = String.format("ADC.STATUS = %d", OBDefine.ADC_STATUS.UNREACHABLE);
			} else // 이렇게 값이 올리 없지만 오면 에러 //TODO
			{
			}
		}

		OBDatabase db = new OBDatabase();
		ResultSet rs;
		// N일 이내 조회 조건
		try {
			db.openDB();

			String dateRange = String.format(" OCCUR_TIME > CURRENT_TIMESTAMP - INTERVAL '%d HOURS' ",
					(faultUnsolvedLimitDays * 24));
			sqlText = String.format(
					" SELECT ADC.INDEX, ADC.NAME AS ADC_NAME, ADC.STATUS, ADC.TYPE, ADC.IPADDRESS, ADC.MODEL, ADC.SW_VERSION, 	   					\n"
							+ "    RES.OCCUR_TIME, RES.CPU_USAGE, RES.MEM_USAGE, RES.VS_CUR_CONNS, RES.THROUGHPUT, RES.VS_COUNT, 					\n"
							+ "    RES.VS_COUNT_AVAIL, RES.VS_COUNT_UNAVAIL, RES.VS_COUNT_DISABLED, ERRVS.COUNT AS ERRVS_COUNT 					\n"
							+ " FROM ( SELECT INDEX, NAME, STATUS, TYPE, IPADDRESS, MODEL, SW_VERSION FROM MNG_ADC WHERE INDEX IN (%s) ) ADC     	\n"
							+ // where-in:empty string 불가, null 불가, OK
							" LEFT JOIN (                                                                                    					\n"
							+ "    SELECT A.ADC_INDEX, A.OCCUR_TIME, A.CPU1_USAGE AS CPU_USAGE, A.MEM_USAGE,                                      \n"
							+ "       B.CONN_CURR VS_CUR_CONNS, B.BPS_IN AS THROUGHPUT,        					                                \n"
							+ "       B.VS_COUNT, B.VS_COUNT_AVAIL, B.VS_COUNT_UNAVAIL, B.VS_COUNT_DISABLED                            			\n"
							+ "   FROM TMP_FAULT_RESC_CPUMEM            A                                                                         \n"
							+ "   INNER JOIN LOG_ADC_PERF_STATS   B                                                        					\n"
							+ "   ON B.OCCUR_TIME = A.OCCUR_TIME AND B.ADC_INDEX = A.ADC_INDEX                                                    \n"
							+ "   WHERE A.ADC_INDEX IN (%s)                                                                      					\n"
							+ // where-in:empty string 불가, null 불가, OK
							" ) RES                                                                                          					\n"
							+ " ON ADC.INDEX = RES.ADC_INDEX                                                                   					\n"
							+ " LEFT JOIN (                                                                                    					\n"
							+ "     SELECT COUNT(INDEX), ADC_INDEX                                                             					\n"
							+ "     FROM TMP_SLB_VSERVER                                                                       					\n"
							+ "     WHERE INDEX IN (                                                                           					\n"
							+ "         SELECT VS_INDEX FROM LOG_ADC_FAULT                                                     					\n"
							+ "         WHERE ADC_INDEX IN ( %s )                                                              					\n"
							+ // where-in:empty string 불가, null 불가, OK
							"             AND TYPE = %d                                                                      					\n"
							+ "             AND STATUS = %d                                                                    					\n"
							+ "             AND %s                                                                             					\n"
							+ "      ) AND STATUS = %d                                                                         					\n"
							+ "     GROUP BY ADC_INDEX                                                                         					\n"
							+ " ) ERRVS                                                                                        					\n"
							+ " ON ADC.INDEX = ERRVS.ADC_INDEX                                                                 					\n"
							+ " WHERE %s                                                                                       					\n",
//				" ORDER BY ADC.NAME limit 300;                                                                   					\n", //TODO, TEMP
					adcIndexSubquery, adcIndexSubquery, adcIndexSubquery, OBDefineFault.TYPE.VIRTUALSRV_DOWN.getCode(), // vs
																														// 장애
					OBDefineFault.STATUS_UNSOLVED, // 미해결
					dateRange, // ADC의 VS 단절(장애) 카운트, N일 이내 발생만 유효함
					OBDefine.VS_STATUS.UNAVAILABLE, adcStatusWhereClause);

			sqlText += getAdcListOrderType(orderType, orderDir);
			sqlText += " LIMIT 300 ";
//			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText = " + sqlText);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sql = " + sqlText);
			long startA = System.currentTimeMillis();
			rs = db.executeQuery(sqlText);
			long endA = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time>sql run : " + (endA - startA) / 1000.0);

			while (rs.next()) {
				OBDtoDashboardSdsAdcSummary adc = new OBDtoDashboardSdsAdcSummary();
				adc.setIndex(db.getInteger(rs, "INDEX"));
				adc.setName(db.getString(rs, "ADC_NAME"));
				adc.setType(db.getInteger(rs, "TYPE"));
				adc.setStatus(db.getInteger(rs, "STATUS"));
				adc.setUpdateTime(db.getTimestamp(rs, "OCCUR_TIME"));
				adc.setCountVs(db.getInteger(rs, "VS_COUNT"));
				adc.setCountVsAvail(db.getInteger(rs, "VS_COUNT_AVAIL"));
				adc.setCountVsUnavail(db.getInteger(rs, "ERRVS_COUNT")); // N일 이내 단절
				adc.setCountVsUnavailLong(db.getInteger(rs, "VS_COUNT_UNAVAIL") - db.getInteger(rs, "ERRVS_COUNT")); // N일
																														// 이상
																														// 단절,
																														// 안씀,
																														// 꺼짐으로
																														// 넘김
				adc.setCountVsDisable(db.getInteger(rs, "VS_COUNT_DISABLED") + adc.getCountVsUnavailLong());
				adc.setConnection(db.getLong(rs, "VS_CUR_CONNS"));
				adc.setThroughput(db.getLong(rs, "THROUGHPUT"));
				adc.setCpu(db.getInteger(rs, "CPU_USAGE"));
				adc.setMemory(db.getInteger(rs, "MEM_USAGE"));
				adc.setIp(db.getString(rs, "IPADDRESS"));
				adc.setModel(db.getString(rs, "MODEL"));
				adc.setOs(db.getString(rs, "SW_VERSION"));
				adcList.add(adc);
			}
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "result # = " + adcList.size());
			return adcList;
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

	public ArrayList<OBDtoDashboardSdsVservSummary> getVservList(Integer accountIndex, Integer targetStatus,
			Integer targetType, Integer targetIndex, Integer faultUnsolvedLimitDays, Integer orderType,
			Integer orderDir) throws OBException {
		ArrayList<OBDtoDashboardSdsVservSummary> vservList = new ArrayList<OBDtoDashboardSdsVservSummary>();

		ResultSet rs, rs2;
		String sqlVserviceText = "";
		String adcIndexSubqueryAdmin = "";
		String adcIndexSubqueryNonAdmin = "";
		String adcIndexSubquery = "";

		String sqlText = "";

		if (targetType.equals(TARGET_ALL)) {
			adcIndexSubqueryAdmin = String.format(" SELECT INDEX FROM MNG_ADC WHERE AVAILABLE = %d ",
					OBDefine.ADC_STATE.AVAILABLE);
			adcIndexSubqueryNonAdmin = String.format(" SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %d ",
					accountIndex);
		} else if (targetType.equals(TARGET_GROUP)) {
			adcIndexSubqueryAdmin = String.format(
					" SELECT INDEX FROM MNG_ADC WHERE AVAILABLE = %d AND GROUP_INDEX = %d ",
					OBDefine.ADC_STATE.AVAILABLE, targetIndex);
			adcIndexSubqueryNonAdmin = String.format(
					" SELECT INDEX FROM MNG_ADC WHERE INDEX IN ( SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %d ) AND GROUP_INDEX = %d ",
					accountIndex, targetIndex); // where-in:empty string 불가, null 불가, OK
		} else if (targetType.equals(TARGET_ADC)) // single adc 선택
		{
			adcIndexSubquery = String.format(" %d ", targetIndex);
			// adcIndexSubqueryNonAdmin = String.format(" %d ", targetIndex);
		}

		if (targetType != TARGET_ADC) // 특정 ADC를 지목한 경우는 계정 권한을 볼 필요가 없다. adc는 이미 하나로 정해졌기 때문
		{
			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accountIndex);
			if (roleNo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);
			}
			if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
				adcIndexSubquery = adcIndexSubqueryAdmin;
			} else {
				adcIndexSubquery = adcIndexSubqueryNonAdmin;
			}
		}
		String vservStatusWhereClause = "";

		OBDatabase db = new OBDatabase();
		OBDatabase db2 = new OBDatabase();

		try {
			db.openDB();
			db2.openDB();

			if (targetStatus == null) {
				// 에러, //TODO
			} else {
				if (targetStatus.equals(STATUS_VS_ALL)) {
					vservStatusWhereClause = "true"; // 전부 다
				} else if (targetStatus.equals(STATUS_VS_AVAILABLE)) {
					vservStatusWhereClause = String.format("VS.STATUS = %d", OBDefine.VS_STATUS.AVAILABLE);
				} else if (targetStatus.equals(STATUS_VS_UNAVAILABLE)) // N일 이내 단절을 뽑는다.
				{
					vservStatusWhereClause = String
							.format(" VS.INDEX IN ( " + "     SELECT INDEX                                     \n"
									+ "     FROM TMP_SLB_VSERVER                             \n"
									+ "     WHERE INDEX IN (                                 \n"
									+ "         SELECT VS_INDEX FROM LOG_ADC_FAULT           \n"
									+ "         WHERE ADC_INDEX IN ( %s )                    \n" + // where-in:empty
																									// string 불가, null
																									// 불가, OK
									"             AND TYPE = %d                            \n"
									+ "             AND STATUS = %d                          \n"
									+ "             AND OCCUR_TIME > CURRENT_TIMESTAMP - INTERVAL '%d HOURS' \n"
									+ "     )                                                \n"
									+ " ) AND VS.STATUS = %d                                 \n", adcIndexSubquery,
									OBDefineFault.TYPE.VIRTUALSRV_DOWN.getCode(), OBDefineFault.STATUS_UNSOLVED,
									faultUnsolvedLimitDays * 24, // N일 이내 단절 VS
									OBDefine.VS_STATUS.UNAVAILABLE);
				} else if (targetStatus.equals(STATUS_VS_UNAVAILABLE_LONG)) {
					vservStatusWhereClause = String
							.format(" VS.INDEX IN ( " + "     SELECT INDEX                                     \n"
									+ "     FROM TMP_SLB_VSERVER                             \n"
									+ "     WHERE INDEX IN (                                 \n"
									+ "         SELECT VS_INDEX FROM LOG_ADC_FAULT           \n"
									+ "         WHERE ADC_INDEX IN ( %s )                    \n" + // where-in:empty
																									// string 불가, null
																									// 불가, OK
									"             AND TYPE = %d                            \n"
									+ "             AND STATUS = %d                          \n"
									+ "             AND OCCUR_TIME <= CURRENT_TIMESTAMP - INTERVAL '%d HOURS' \n"
									+ "     )                                                \n"
									+ " ) AND VS.STATUS = %d                                 \n", adcIndexSubquery,
									OBDefineFault.TYPE.VIRTUALSRV_DOWN.getCode(), OBDefineFault.STATUS_UNSOLVED,
									faultUnsolvedLimitDays * 24, // N일 이상 단절
									OBDefine.VS_STATUS.UNAVAILABLE);
				} else if (targetStatus.equals(STATUS_VS_DISABLE)) // disable에 N일 이상 단절도 포함해야 하므로,,,, 조회 조건이 "VS.STATUS
																	// = OBDefine.VS_STATUS.DISABLE"이 아니다.
				{
					vservStatusWhereClause = String.format(
							" (  VS.INDEX NOT IN ( " + "       SELECT INDEX                                     \n"
									+ "       FROM TMP_SLB_VSERVER                             \n"
									+ "       WHERE INDEX IN (                                 \n"
									+ "           SELECT VS_INDEX FROM LOG_ADC_FAULT           \n"
									+ "           WHERE ADC_INDEX IN ( %s )                    \n" + // where-in:empty
																										// string 불가,
																										// null 불가, OK
									"               AND TYPE = %d                            \n"
									+ "               AND STATUS = %d                          \n"
									+ "               AND OCCUR_TIME > CURRENT_TIMESTAMP - INTERVAL '%d HOURS' \n"
									+ "       )                                                \n"
									+ "   ) AND VS.STATUS = %d                                 \n"
									+ "   OR VS.STATUS = %d                                    \n"
									+ " )                                                      \n",
							adcIndexSubquery, OBDefineFault.TYPE.VIRTUALSRV_DOWN.getCode(),
							OBDefineFault.STATUS_UNSOLVED, faultUnsolvedLimitDays * 24, // N일 이상 단절
							OBDefine.VS_STATUS.UNAVAILABLE, OBDefine.VS_STATUS.DISABLE);
				} else // 이렇게 값이 올리 없지만 오면 에러 //TODO
				{
				}
			}

			sqlText = String.format(
					" SELECT VS.INDEX, VS.STATUS, VS.VIRTUAL_IP, VS.VIRTUAL_PORT, VS.ADC_INDEX,                    \n"
							+ "     VSRES.OCCUR_TIME, VSRES.CONN_CURR, VSRES.BPS_IN, VSRES.BPS_OUT, (VSRES.BPS_IN+VSRES.BPS_OUT) AS THROUGHPUT, \n"
							+ "     ADC.NAME AS ADC_NAME, ADC.TYPE AS ADC_TYPE, ADCRESC.CPU_USAGE, ADCRESC.MEM_USAGE,        \n"
							+ "	  ADC.MODEL AS MODEL, ADC.SW_VERSION SW_VERSION											   \n"
							+ " FROM                                                                                         \n"
							+ "     ( SELECT INDEX, STATUS, VIRTUAL_IP, VIRTUAL_PORT, ADC_INDEX                              \n"
							+ "       FROM TMP_SLB_VSERVER                                                                   \n"
							+ "       WHERE INDEX IN ( SELECT INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX IN (%s) )           \n"
							+ // where-in:empty string 불가, null 불가, OK
							"     ) VS                                                                                     \n"
							+ " LEFT JOIN                                                                                    \n"
							+ "     ( SELECT OBJ_INDEX, OCCUR_TIME, CONN_CURR, BPS_IN, BPS_OUT                               \n"
							+ "       FROM TMP_FAULT_SVC_PERF_STATS                                                          \n"
							+ "       WHERE OBJ_INDEX IN ( SELECT INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX IN (%s) )       \n"
							+ // where-in:empty string 불가, null 불가, OK
							"     ) VSRES                                                                                  \n"
							+ " ON VS.INDEX = VSRES.OBJ_INDEX                                                                \n"
							+ " LEFT JOIN                                                                                    \n"
							+ "     ( SELECT ADC_INDEX, CPU1_USAGE AS CPU_USAGE, MEM_USAGE                                   \n"
							+ "       FROM TMP_FAULT_RESC_CPUMEM                                                             \n"
							+ "       WHERE ADC_INDEX IN (%s)                                                                \n"
							+ // where-in:empty string 불가, null 불가, OK
							"     ) AS ADCRESC                                                                             \n"
							+ " ON VS.ADC_INDEX = ADCRESC.ADC_INDEX,                                                         \n"
							+ " MNG_ADC AS ADC                                                                               \n"
							+ " WHERE VS.ADC_INDEX = ADC.INDEX AND %s                                                        \n"
//				" ORDER BY VS.ADC_INDEX, VS.INDEX LIMIT 300;                                                   \n" //TODO : 300개만 가져온다. 엔트리 수를 별도 define 없이 직접 지정...
					, adcIndexSubquery, adcIndexSubquery, adcIndexSubquery, vservStatusWhereClause);

			sqlText += getVSListOrderType(orderType, orderDir);
			sqlText += " LIMIT 300 ";
			// System.out.println(sqlVserverText);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText = " + sqlText);
			long startA = System.currentTimeMillis();
			rs = db.executeQuery(sqlText);
			long endA = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time>sql run: " + (endA - startA) / 1000.0);
			Integer vservPort = 0;
			while (rs.next()) {
				int vsLength = 0;
				long vsConnSum = 0;
				long vsThrSum = 0;
				OBDtoDashboardSdsVservSummary vserv = new OBDtoDashboardSdsVservSummary();
				vserv.setRecordType(RECORD_TYPE_VSERVER); // =0, virtual server
				vserv.setVservIndex(db.getString(rs, "INDEX"));
				vserv.setVservStatus(db.getInteger(rs, "STATUS"));
				vserv.setVservIp(db.getString(rs, "VIRTUAL_IP"));
				vservPort = db.getInteger(rs, "VIRTUAL_PORT");

				vserv.setVservport(vservPort);
				vserv.setUpdateTime(db.getTimestamp(rs, "OCCUR_TIME"));
				vserv.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				vserv.setAdcName(db.getString(rs, "ADC_NAME"));
				vserv.setAdcType(db.getInteger(rs, "ADC_TYPE"));
				vserv.setVservConnection(db.getLong(rs, "CONN_CURR"));
				vserv.setVservThroughput(db.getLong(rs, "BPS_IN") + db.getLong(rs, "BPS_OUT"));
				vserv.setAdcCpu(db.getInteger(rs, "CPU_USAGE"));
				vserv.setAdcMemory(db.getInteger(rs, "MEM_USAGE"));
				vserv.setAdcModel(db.getString(rs, "MODEL"));
				vserv.setAdcOs(db.getString(rs, "SW_VERSION"));
				vservList.add(vserv);
				vsLength = vservList.size();
				if (vserv.getAdcType().equals(OBDefine.ADC_TYPE_ALTEON)) {
					sqlVserviceText = String.format(
							" SELECT B.VS_INDEX, C.VIRTUAL_IP AS VS_IPADDRESS, B.VIRTUAL_PORT AS SERVICE_PORT, \n"
									+ " A.OCCUR_TIME, A.CONN_CURR, A.BPS_IN                                        \n"
									+ " FROM TMP_FAULT_SVC_PERF_STATS A                                            \n"
									+ " INNER JOIN TMP_SLB_VS_SERVICE B                                            \n"
									+ " ON B.INDEX = A.OBJ_INDEX                                                   \n"
									+ " INNER JOIN TMP_SLB_VSERVER    C                                            \n"
									+ " ON C.INDEX = B.VS_INDEX                                                    \n"
									+ " WHERE B.VS_INDEX = %s                                                      \n",
							OBParser.sqlString(vserv.getVservIndex()));
					vservPort = 0;
//					System.out.println(sqlVserverText);
					sqlVserviceText += getVsrvListOrderType(orderType, orderDir);

					rs2 = db2.executeQuery(sqlVserviceText);
					while (rs2.next()) {
						OBDtoDashboardSdsVservSummary vserv2 = new OBDtoDashboardSdsVservSummary();
						vserv2.setRecordType(RECORD_TYPE_VSERVICE); // =1, 고정값, virtual service
						vserv2.setVservIndex(vserv.getVservIndex()); // 승계
						vserv2.setVservStatus(vserv.getVservStatus()); // 승계
						vserv2.setVservIp(vserv.getVservIp()); // 승계
						vservPort = db.getInteger(rs2, "SERVICE_PORT"); // port는 구한 것을 쓴다.

						vserv2.setVservport(vservPort);
						vserv2.setUpdateTime(db.getTimestamp(rs2, "OCCUR_TIME")); // 구한 값
						vserv2.setAdcIndex(vserv.getAdcIndex()); // 승계
						vserv2.setAdcName(vserv.getAdcName()); // 승계
						vserv2.setAdcType(vserv.getAdcType()); // 승계
						vserv2.setVservConnection(db.getLong(rs2, "CONN_CURR")); // 구한 값
						vsConnSum += vserv2.getVservConnection();
						vserv2.setVservThroughput(db.getLong(rs2, "BPS_IN")); // 구한 값, alteon은 in/out 구분이 없어 bps_in에
																				// throuput이 다 들어있다. 사실 bps_out이라는 필드가
																				// 아예 없다.
						vsThrSum += vserv2.getVservThroughput();
						vserv2.setAdcCpu(vserv.getAdcCpu());
						vserv2.setAdcMemory(vserv.getAdcMemory());
						vservList.add(vserv2);

					}
					vservList.get(vsLength - 1).setVservConnection(vsConnSum);
					vservList.get(vsLength - 1).setVservThroughput(vsThrSum);
				}
			}
			return vservList;
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
		}
	}

	public OBDtoDashboardSdsVservSummaryCount getVservCount(Integer accountIndex, Integer targetStatus,
			Integer targetType, Integer targetIndex, Integer faultUnsolvedLimitDays, Integer orderType,
			Integer orderDir) throws OBException {
		OBDtoDashboardSdsVservSummaryCount vservCount = new OBDtoDashboardSdsVservSummaryCount();
		ResultSet rs;
		String adcIndexSubqueryAdmin = "";
		String adcIndexSubqueryNonAdmin = "";
		String adcIndexSubquery = "";
		String sqlText = "";

		if (targetType.equals(TARGET_ALL)) {
			adcIndexSubqueryAdmin = String.format(" SELECT INDEX FROM MNG_ADC WHERE AVAILABLE = %d ",
					OBDefine.ADC_STATE.AVAILABLE);
			adcIndexSubqueryNonAdmin = String.format(" SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %d ",
					accountIndex);
		} else if (targetType.equals(TARGET_GROUP)) {
			adcIndexSubqueryAdmin = String.format(
					" SELECT INDEX FROM MNG_ADC WHERE AVAILABLE = %d AND GROUP_INDEX = %d ",
					OBDefine.ADC_STATE.AVAILABLE, targetIndex);
			adcIndexSubqueryNonAdmin = String.format(
					" SELECT INDEX FROM MNG_ADC WHERE INDEX IN ( SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %d ) AND GROUP_INDEX = %d ",
					accountIndex, targetIndex); // where-in:empty string 불가, null 불가, OK
		} else if (targetType.equals(TARGET_ADC)) // single adc 선택
		{
			adcIndexSubquery = String.format(" %d ", targetIndex);
			// adcIndexSubqueryNonAdmin = String.format(" %d ", targetIndex);
		}

		if (targetType != TARGET_ADC) // 특정 ADC를 지목한 경우는 계정 권한을 볼 필요가 없다. adc는 이미 하나로 정해졌기 때문
		{
			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accountIndex);
			if (roleNo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);
			}
			if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
				adcIndexSubquery = adcIndexSubqueryAdmin;
			} else {
				adcIndexSubquery = adcIndexSubqueryNonAdmin;
			}
		}
		String vservStatusWhereClause = "";

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			if (targetStatus == null) {
				// 에러, //TODO
			} else {
				if (targetStatus.equals(STATUS_VS_ALL)) {
					vservStatusWhereClause = "true"; // 전부 다
				} else if (targetStatus.equals(STATUS_VS_AVAILABLE)) {
					vservStatusWhereClause = String.format("VS.STATUS = %d", OBDefine.VS_STATUS.AVAILABLE);
				} else if (targetStatus.equals(STATUS_VS_UNAVAILABLE)) // N일 이내 단절을 뽑는다.
				{
					vservStatusWhereClause = String
							.format(" VS.INDEX IN ( " + "     SELECT INDEX                                     \n"
									+ "     FROM TMP_SLB_VSERVER                             \n"
									+ "     WHERE INDEX IN (                                 \n"
									+ "         SELECT VS_INDEX FROM LOG_ADC_FAULT           \n"
									+ "         WHERE ADC_INDEX IN ( %s )                    \n" + // where-in:empty
																									// string 불가, null
																									// 불가, OK
									"             AND TYPE = %d                            \n"
									+ "             AND STATUS = %d                          \n"
									+ "             AND OCCUR_TIME > CURRENT_TIMESTAMP - INTERVAL '%d HOURS' \n"
									+ "     )                                                \n"
									+ " ) AND VS.STATUS = %d                                 \n", adcIndexSubquery,
									OBDefineFault.TYPE.VIRTUALSRV_DOWN.getCode(), OBDefineFault.STATUS_UNSOLVED,
									faultUnsolvedLimitDays * 24, // N일 이내 단절 VS
									OBDefine.VS_STATUS.UNAVAILABLE);
				} else if (targetStatus.equals(STATUS_VS_UNAVAILABLE_LONG)) {
					vservStatusWhereClause = String
							.format(" VS.INDEX IN ( " + "     SELECT INDEX                                     \n"
									+ "     FROM TMP_SLB_VSERVER                             \n"
									+ "     WHERE INDEX IN (                                 \n"
									+ "         SELECT VS_INDEX FROM LOG_ADC_FAULT           \n"
									+ "         WHERE ADC_INDEX IN ( %s )                    \n" + // where-in:empty
																									// string 불가, null
																									// 불가, OK
									"             AND TYPE = %d                            \n"
									+ "             AND STATUS = %d                          \n"
									+ "             AND OCCUR_TIME <= CURRENT_TIMESTAMP - INTERVAL '%d HOURS' \n"
									+ "     )                                                \n"
									+ " ) AND VS.STATUS = %d                                 \n", adcIndexSubquery,
									OBDefineFault.TYPE.VIRTUALSRV_DOWN.getCode(), OBDefineFault.STATUS_UNSOLVED,
									faultUnsolvedLimitDays * 24, // N일 이상 단절
									OBDefine.VS_STATUS.UNAVAILABLE);
				} else if (targetStatus.equals(STATUS_VS_DISABLE)) // disable에 N일 이상 단절도 포함해야 하므로,,,, 조회 조건이 "VS.STATUS
																	// = OBDefine.VS_STATUS.DISABLE"이 아니다.
				{
					vservStatusWhereClause = String.format(
							" (  VS.INDEX NOT IN ( " + "       SELECT INDEX                                     \n"
									+ "       FROM TMP_SLB_VSERVER                             \n"
									+ "       WHERE INDEX IN (                                 \n"
									+ "           SELECT VS_INDEX FROM LOG_ADC_FAULT           \n"
									+ "           WHERE ADC_INDEX IN ( %s )                    \n" + // where-in:empty
																										// string 불가,
																										// null 불가, OK
									"               AND TYPE = %d                            \n"
									+ "               AND STATUS = %d                          \n"
									+ "               AND OCCUR_TIME > CURRENT_TIMESTAMP - INTERVAL '%d HOURS' \n"
									+ "       )                                                \n"
									+ "   ) AND VS.STATUS = %d                                 \n"
									+ "   OR VS.STATUS = %d                                    \n"
									+ " )                                                      \n",
							adcIndexSubquery, OBDefineFault.TYPE.VIRTUALSRV_DOWN.getCode(),
							OBDefineFault.STATUS_UNSOLVED, faultUnsolvedLimitDays * 24, // N일 이상 단절
							OBDefine.VS_STATUS.UNAVAILABLE, OBDefine.VS_STATUS.DISABLE);
				} else // 이렇게 값이 올리 없지만 오면 에러 //TODO
				{
				}
			}

			sqlText = String.format(
					" SELECT COUNT(VS.INDEX) AS VSCOUNT	                                                       \n"
							+ " FROM                                                                                         \n"
							+ "     ( SELECT INDEX, STATUS, VIRTUAL_IP, VIRTUAL_PORT, ADC_INDEX                              \n"
							+ "       FROM TMP_SLB_VSERVER                                                                   \n"
							+ "       WHERE INDEX IN ( SELECT INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX IN (%s) )           \n"
							+ // where-in:empty string 불가, null 불가, OK
							"     ) VS                                                                                     \n"
							+ " LEFT JOIN                                                                                    \n"
							+ "     ( SELECT VS_INDEX, OCCUR_TIME, CUR_CONNS, BPS_IN, BPS_OUT                                \n"
							+ "       FROM TMP_ADC_VS_RESC                                                                   \n"
							+ "       WHERE VS_INDEX IN ( SELECT INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX IN (%s) )        \n"
							+ // where-in:empty string 불가, null 불가, OK
							"     ) VSRES                                                                                  \n"
							+ " ON VS.INDEX = VSRES.VS_INDEX                                                                 \n"
							+ " LEFT JOIN                                                                                    \n"
							+ "     ( SELECT ADC_INDEX, CPU1_USAGE AS CPU_USAGE, MEM_USAGE                                   \n"
							+ "       FROM TMP_FAULT_RESC_CPUMEM                                                             \n"
							+ "       WHERE ADC_INDEX IN (%s)                                                                \n"
							+ // where-in:empty string 불가, null 불가, OK
							"     ) AS ADCRESC                                                                             \n"
							+ " ON VS.ADC_INDEX = ADCRESC.ADC_INDEX,                                                         \n"
							+ " MNG_ADC AS ADC                                                                               \n"
							+ " WHERE VS.ADC_INDEX = ADC.INDEX AND %s                                                        \n"
//				" ORDER BY VS.ADC_INDEX, VS.INDEX LIMIT 300;                                                   \n" //TODO : 300개만 가져온다. 엔트리 수를 별도 define 없이 직접 지정...
					, adcIndexSubquery, adcIndexSubquery, adcIndexSubquery, vservStatusWhereClause);

			sqlText += " LIMIT 300 ";
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText = " + sqlText);
			/* long startA = System.currentTimeMillis(); */
			rs = db.executeQuery(sqlText);
			/*
			 * long endA = System.currentTimeMillis();
			 * OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time>sql run: " + (endA -
			 * startA)/1000.0 );
			 */
			if (rs.next() == true) {
				vservCount.setVsSummaryCount(db.getInteger(rs, "VSCOUNT"));
			}

			return vservCount;
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

	public ArrayList<OBDtoDashboardSdsIssueSummary> getIssueList(Integer accountIndex, Integer targetStatus,
			Integer targetType, Integer targetIndex, Integer faultUnsolvedLimitDays, Integer orderType,
			Integer orderDir) throws OBException {
		ArrayList<OBDtoDashboardSdsIssueSummary> issueList = new ArrayList<OBDtoDashboardSdsIssueSummary>();
		String adcIndexSubqueryAdmin = "";
		String adcIndexSubqueryNonAdmin = "";
		String adcIndexSubquery = "";
		ResultSet rs;

		String dateRange = "";
		// if(targetStatus.equals(STATUS_ISSUE_UNSOLVED) ||
		// faultUnsolvedLimitDays.equals(0)) //미해결장애를 조회할 때는 일자 조건을 무시하고 모두 보여준다.
		if (faultUnsolvedLimitDays.equals(0)) {
			dateRange = " TRUE ";
		} else // 일자 조건을 적용함.
		{
			dateRange = String.format(" OCCUR_TIME >= CURRENT_TIMESTAMP - INTERVAL '%d HOURS' ",
					faultUnsolvedLimitDays * 24);
		}

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "faultUnsolvedLimitDays = " + faultUnsolvedLimitDays);
		String sqlText = "";

		if (targetType.equals(TARGET_ALL)) {
			adcIndexSubqueryAdmin = String.format(" SELECT INDEX FROM MNG_ADC WHERE AVAILABLE = %d ",
					OBDefine.ADC_STATE.AVAILABLE);
			adcIndexSubqueryNonAdmin = String.format(" SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %d ",
					accountIndex);
		} else if (targetType.equals(TARGET_GROUP)) {
			adcIndexSubqueryAdmin = String.format(
					" SELECT INDEX FROM MNG_ADC WHERE AVAILABLE = %d AND GROUP_INDEX = %d ",
					OBDefine.ADC_STATE.AVAILABLE, targetIndex);
			adcIndexSubqueryNonAdmin = String.format(
					" SELECT INDEX FROM MNG_ADC WHERE INDEX IN ( SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %d ) AND GROUP_INDEX = %d ",
					accountIndex, targetIndex);
		} else if (targetType.equals(TARGET_ADC)) // single adc 선택
		{
			adcIndexSubquery = String.format(" %d ", targetIndex);
			// adcIndexSubqueryNonAdmin = String.format(" %d ", targetIndex);
		}

		if (targetType != TARGET_ADC) // 특정 ADC를 지목한 경우는 계정 권한을 볼 필요가 없다. adc는 이미 하나로 정해졌기 때문
		{
			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accountIndex);
			if (roleNo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);
			}
			if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
				adcIndexSubquery = adcIndexSubqueryAdmin;
			} else {
				adcIndexSubquery = adcIndexSubqueryNonAdmin;
			}
		}

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String issueStatusWhereClause = "";
			if (targetStatus == null) {
				// 에러, //TODO
			} else {
				if (targetStatus.equals(STATUS_ISSUE_ALL)) {
					issueStatusWhereClause = "true"; // 전부 다
				} else if (targetStatus.equals(STATUS_ISSUE_SOLVED)) {
					issueStatusWhereClause = String.format("FAULT.STATUS = %d", OBDefineFault.STATUS_SOLVED);
				} else if (targetStatus.equals(STATUS_ISSUE_UNSOLVED)) {
					issueStatusWhereClause = String.format("FAULT.STATUS = %d", OBDefineFault.STATUS_UNSOLVED);
				} else if (targetStatus.equals(STATUS_ISSUE_WARN)) {
					issueStatusWhereClause = String.format("FAULT.STATUS = %d", OBDefineFault.STATUS_WARN);
				} else // 이렇게 값이 올리 없지만 오면 에러 //TODO
				{
				}
			}

			sqlText = String.format(
					" SELECT FAULT.LOG_SEQ, FAULT.ISSUE_TYPE, FAULT.ADC_INDEX, FAULT.ADC_NAME, FAULT.VS_INDEX,          \n"
							+ "     FAULT.TARGET_OBJECT, FAULT.OCCUR_TIME, FAULT.LEVEL, FAULT.STATUS, ADC.ADC_TYPE,               \n"
							+ "     VS.VIRTUAL_IP, RES.CPU_USAGE, RES.MEM_USAGE, FAULT.EVENT                                      \n"
							+ " FROM (                                                                                            \n"
							+ "     SELECT LOG_SEQ, TYPE AS ISSUE_TYPE, ADC_INDEX, ADC_NAME, VS_INDEX, TARGET_OBJECT,             \n"
							+ "         OCCUR_TIME, LEVEL, STATUS, EVENT                                                          \n"
							+ "     FROM LOG_ADC_FAULT                                                                            \n"
							+ "     WHERE ADC_INDEX IN (%s) AND %s                                                                \n"
							+ // where-in:empty string 불가, null 불가
							"     ) FAULT                                                                                       \n"
							+ " LEFT JOIN (                                                                                       \n"
							+ "     SELECT INDEX, TYPE AS ADC_TYPE FROM MNG_ADC WHERE INDEX IN (%s)                               \n"
							+ // where-in:empty string 불가, null 불가
							"     ) ADC                                                                                         \n"
							+ " ON  FAULT.ADC_INDEX = ADC.INDEX                                                                   \n"
							+ " LEFT JOIN (                                                                                       \n"
							+ "     SELECT INDEX, VIRTUAL_IP FROM TMP_SLB_VSERVER WHERE ADC_INDEX IN (%s)                         \n"
							+ // where-in:empty string 불가, null 불가
							"     ) VS                                                                                          \n"
							+ " ON FAULT.VS_INDEX = VS.INDEX                                                                      \n"
							+ " LEFT JOIN (   																					\n"
							+ "     SELECT ADC_INDEX, CPU1_USAGE AS CPU_USAGE, MEM_USAGE FROM TMP_FAULT_RESC_CPUMEM               \n"
							+ "     ) RES                                                                                         \n"
							+ " ON ADC.INDEX = RES.ADC_INDEX                                                                      \n"
							+ " WHERE                                                                                             \n"
							+ "     %s                                                                                            \n",
//				" ORDER BY                                                                                          \n" +
//				"     FAULT.OCCUR_TIME DESC, FAULT.ADC_NAME, FAULT.ISSUE_TYPE LIMIT 300                             \n", //TODO LIMIT 300
					adcIndexSubquery, dateRange, adcIndexSubquery, adcIndexSubquery, issueStatusWhereClause);

			sqlText += getIssueOrderType(orderType, orderDir);
			sqlText += " LIMIT 300 ";
//			System.out.println(sqlText);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText = " + sqlText);
			long startA = System.currentTimeMillis();
			rs = db.executeQuery(sqlText);
			long endA = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time>sql run: " + (endA - startA) / 1000.0);
			Integer issueType = 0;
			String targetObject = "";
			String virtualIp = "";
			while (rs.next()) {
				OBDtoDashboardSdsIssueSummary issue = new OBDtoDashboardSdsIssueSummary();

				issue.setIssueIndex(db.getLong(rs, "LOG_SEQ"));

				// issueType(장애 유형)에 따라 virtual server ip, member ip, interface#를 가려서 채운다.
				// class OBDefine에 장애 유형 정의
				// ADC_FAULT_SYSTEM : 1, adc 시스템 reachable, unreachable
				// ADC_FAULT_VIRTSRV : 2, virtual server up/down
				// ADC_FAULT_POOLMEMS : 3, pool member up/down
				// ADC_FAULT_LINKS : 4, link up/down
				issueType = db.getInteger(rs, "ISSUE_TYPE");
				targetObject = db.getString(rs, "TARGET_OBJECT");
				virtualIp = db.getString(rs, "VIRTUAL_IP");
				issue.setIssueType(issueType);

				if (issueType.equals(OBDefineFault.TYPE.SYSTEM_OFF.getCode())) // ADC
				{
					issue.setVservIp("-");
					issue.setMemberIp("-");
					issue.setInterfaceNumber("-");
				} else if (issueType.equals(OBDefineFault.TYPE.VIRTUALSRV_DOWN.getCode())) // virtual server
				{
					issue.setVservIp(virtualIp);
					issue.setMemberIp("-");
					issue.setInterfaceNumber("-");
				} else if (issueType.equals(OBDefineFault.TYPE.POOLMEMS_DOWN.getCode())) // member
				{
					issue.setVservIp(virtualIp);
					issue.setMemberIp(targetObject);
					issue.setInterfaceNumber("-");
				} else if (issueType.equals(OBDefineFault.TYPE.LINKS_DOWN.getCode())) // interface
				{
					issue.setVservIp("-");
					issue.setMemberIp("-");
					issue.setInterfaceNumber(targetObject);
				} else if (issueType.equals(OBDefineFault.TYPE.BOOT.getCode()))// ADC boot
				{
					issue.setVservIp("-");
					issue.setMemberIp("-");
					issue.setInterfaceNumber("-");
				} else if (issueType.equals(OBDefineFault.TYPE.STANDBY.getCode()))// ADC Standby 전환
				{
					issue.setVservIp("-");
					issue.setMemberIp("-");
					issue.setInterfaceNumber("-");
				} else // ADC Active 전환
				{
					issue.setVservIp("-");
					issue.setMemberIp("-");
					issue.setInterfaceNumber("-");
				}

				issue.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				issue.setAdcName(db.getString(rs, "ADC_NAME"));
				issue.setAdcType(db.getInteger(rs, "ADC_TYPE"));
				issue.setVservInex(db.getString(rs, "VS_INDEX"));
				issue.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				issue.setIssueLevel(db.getInteger(rs, "LEVEL"));
				issue.setStatus(db.getInteger(rs, "STATUS"));
				issue.setCpu(db.getInteger(rs, "CPU_USAGE"));
				issue.setMemory(db.getInteger(rs, "MEM_USAGE"));
				issue.setEvent(db.getString(rs, "EVENT"));

				issueList.add(issue);
			}
			return issueList;
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
	public void setFaultSolved(ArrayList<Long> faultIndexList) throws OBException {
		// 계획했다가 보류함
	}

	private String getAdcListOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY ADC_NAME ASC NULLS LAST ";
		switch (orderType) {
		case OBDefine.ORDER_TYPE_ADCNAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY ADC_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY ADC_NAME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_ADCIPADDRESS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY IPADDRESS ASC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY IPADDRESS DESC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_ADCSTATUS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY STATUS ASC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY STATUS DESC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_OCCURTIME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY OCCUR_TIME ASC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_PRODUCTNAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY MODEL ASC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY MODEL DESC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_VERSION:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY SW_VERSION ASC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY SW_VERSION DESC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_CPS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY VS_CUR_CONNS ASC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY VS_CUR_CONNS DESC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_BPS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY THROUGHPUT ASC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY THROUGHPUT DESC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_CPUUSAGE:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY CPU_USAGE ASC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY CPU_USAGE DESC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_MEMUSAGE:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY MEM_USAGE ASC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY MEM_USAGE DESC NULLS LAST, ADC_NAME ASC NULLS LAST ";
			break;
		}
		return retVal;
	}

	@Override
	public ArrayList<OBDtoDashboardSdsAdcSummary> getAdcListAll(Integer accountIndex, Integer adcStatus,
			Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException {
		return getAdcList(accountIndex, adcStatus, TARGET_ALL, null, faultUnsolvedLimitDays, orderType, orderDir);
	}

	@Override
	public ArrayList<OBDtoDashboardSdsAdcSummary> getAdcListGroup(Integer accountIndex, Integer adcStatus,
			Integer groupIndex, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir)
			throws OBException {
		return getAdcList(accountIndex, adcStatus, TARGET_GROUP, groupIndex, faultUnsolvedLimitDays, orderType,
				orderDir);
	}

	@Override
	public ArrayList<OBDtoDashboardSdsAdcSummary> getAdcListSingle(Integer adcStatus, Integer adcIndex,
			Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException {
		return getAdcList(null, adcStatus, TARGET_ADC, adcIndex, faultUnsolvedLimitDays, orderType, orderDir);
	}

	private String getVsrvListOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ";
		switch (orderType) {
		case OBDefine.ORDER_TYPE_SERVICEPORT:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY SERVICE_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY SERVICE_PORT DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_OCCURTIME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY OCCUR_TIME ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_CPS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY CUR_CONNS ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY CUR_CONNS DESC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_BPS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY BPS_IN ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY BPS_IN DESC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			break;
		}
		return retVal;
	}

	private String getVSListOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY VIRTUAL_IP ASC NULLS LAST ";
		switch (orderType) {
		case OBDefine.ORDER_TYPE_VSIPADDRESS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY VIRTUAL_IP ASC NULLS LAST ";
			else
				retVal = " ORDER BY VIRTUAL_IP DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SERVICEPORT:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY VIRTUAL_PORT ASC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			else
				retVal = " ORDER BY VIRTUAL_PORT DESC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_STATUS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY STATUS ASC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			else
				retVal = " ORDER BY STATUS DESC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_OCCURTIME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY OCCUR_TIME ASC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			else
				retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_ADCNAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY ADC_NAME ASC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			else
				retVal = " ORDER BY ADC_NAME DESC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_PRODUCTNAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY MODEL ASC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			else
				retVal = " ORDER BY MODEL DESC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_VERSION:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY SW_VERSION ASC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			else
				retVal = " ORDER BY SW_VERSION DESC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_CPS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY CUR_CONNS ASC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			else
				retVal = " ORDER BY CUR_CONNS DESC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_BPS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY THROUGHPUT ASC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			else
				retVal = " ORDER BY THROUGHPUT DESC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_CPUUSAGE:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY CPU_USAGE ASC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			else
				retVal = " ORDER BY CPU_USAGE DESC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_MEMUSAGE:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY MEM_USAGE ASC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			else
				retVal = " ORDER BY MEM_USAGE DESC NULLS LAST, VIRTUAL_IP ASC NULLS LAST ";
			break;
		}
		return retVal;
	}

	@Override
	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListAllAdc(Integer accountIndex, Integer vservStatus,
			Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException {
		return getVservList(accountIndex, vservStatus, TARGET_ALL, null, faultUnsolvedLimitDays, orderType, orderDir);
	}

	@Override
	public OBDtoDashboardSdsVservSummaryCount getVservCountAllAdc(Integer accountIndex, Integer vservStatus,
			Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException {
		return getVservCount(accountIndex, vservStatus, TARGET_ALL, null, faultUnsolvedLimitDays, orderType, orderDir);
	}

	@Override
	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListGroupAdc(Integer accountIndex, Integer vservStatus,
			Integer groupIndex, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir)
			throws OBException {
		return getVservList(accountIndex, vservStatus, TARGET_GROUP, groupIndex, faultUnsolvedLimitDays, orderType,
				orderDir);
	}

	@Override
	public OBDtoDashboardSdsVservSummaryCount getVservCountGroupAdc(Integer accountIndex, Integer vservStatus,
			Integer groupIndex, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir)
			throws OBException {
		return getVservCount(accountIndex, vservStatus, TARGET_GROUP, groupIndex, faultUnsolvedLimitDays, orderType,
				orderDir);
	}

	@Override
	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListSingleAdc(Integer vservStatus, Integer adcIndex,
			Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException {
		return getVservList(null, vservStatus, TARGET_ADC, adcIndex, faultUnsolvedLimitDays, orderType, orderDir);
	}

	@Override
	public OBDtoDashboardSdsVservSummaryCount getVservCountSingleAdc(Integer vservStatus, Integer adcIndex,
			Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException {
		return getVservCount(null, vservStatus, TARGET_ADC, adcIndex, faultUnsolvedLimitDays, orderType, orderDir);
	}

	@Override
	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListAllAdcUnavailNDays(Integer accountIndex,
			Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException {
		return getVservList(accountIndex, STATUS_VS_UNAVAILABLE_LONG, TARGET_ALL, null, faultUnsolvedLimitDays,
				orderType, orderDir);
	}

	@Override
	public OBDtoDashboardSdsVservSummaryCount getVservCountAllAdcUnavailNDays(Integer accountIndex,
			Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListGroupAdcUnavailNDays(Integer accountIndex,
			Integer groupIndex, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir)
			throws OBException {
		return getVservList(accountIndex, STATUS_VS_UNAVAILABLE_LONG, TARGET_GROUP, groupIndex, faultUnsolvedLimitDays,
				orderType, orderDir);
	}

	@Override
	public OBDtoDashboardSdsVservSummaryCount getVservCountGroupAdcUnavailNDays(Integer accountIndex,
			Integer groupIndex, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir)
			throws OBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListSingleAdcUnavailNDays(Integer adcIndex,
			Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException {
		return getVservList(null, STATUS_VS_UNAVAILABLE_LONG, TARGET_ADC, adcIndex, faultUnsolvedLimitDays, orderType,
				orderDir);
	}

	@Override
	public OBDtoDashboardSdsVservSummaryCount getVservCountSingleAdcUnavailNDays(Integer adcIndex,
			Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException {
		// TODO Auto-generated method stub
		return null;
	}

	private String getIssueOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST ";
		switch (orderType) {
		case OBDefine.ORDER_TYPE_OCCURTIME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY OCCUR_TIME ASC NULLS LAST ";
			else
				retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST";
			break;
		case OBDefine.ORDER_TYPE_ADCNAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY ADC_NAME ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY ADC_NAME DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_TYPE:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY ISSUE_TYPE ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY ISSUE_TYPE DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
//		case OBDefine.ORDER_TYPE_MEMBERIPADDRESS:
//			if(orderDir==OBDefine.ORDER_DIR_ASCEND)
//				retVal = " ORDER BY STATUS ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
//			else
//				retVal = " ORDER BY STATUS DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
//			break;
//		case OBDefine.ORDER_TYPE_INTERFACENAME:
//			if(orderDir==OBDefine.ORDER_DIR_ASCEND)
//				retVal = " ORDER BY OCCUR_TIME ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
//			else
//				retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
//			break;
		case OBDefine.ORDER_TYPE_SEVERITY:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY LEVEL ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY LEVEL DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_STATUS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY STATUS ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY STATUS DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_CPUUSAGE:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY CPU_USAGE ASC NULLS LAST, OCCUR_TIME DES NULLS LASTC NULLS LAST ";
			else
				retVal = " ORDER BY CPU_USAGE DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_MEMUSAGE:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY MEM_USAGE ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY MEM_USAGE DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		}
		return retVal;
	}

	@Override
	public ArrayList<OBDtoDashboardSdsIssueSummary> getIssueListAllAdc(Integer accountIndex, Integer issueStatus,
			Integer issueUnsolvedMaxDays, Integer orderType, Integer orderDir) throws OBException {
		return getIssueList(accountIndex, issueStatus, TARGET_ALL, null, issueUnsolvedMaxDays, orderType, orderDir);
	}

	@Override
	public ArrayList<OBDtoDashboardSdsIssueSummary> getIssueListGroupAdc(Integer accountIndex, Integer issueStatus,
			Integer groupIndex, Integer issueUnsolvedMaxDays, Integer orderType, Integer orderDir) throws OBException {
		return getIssueList(accountIndex, issueStatus, TARGET_GROUP, groupIndex, issueUnsolvedMaxDays, orderType,
				orderDir);
	}

	@Override
	public ArrayList<OBDtoDashboardSdsIssueSummary> getIssueListSingleAdc(Integer issueStatus, Integer adcIndex,
			Integer issueUnsolvedMaxDays, Integer orderType, Integer orderDir) throws OBException {
		return getIssueList(null, issueStatus, TARGET_ADC, adcIndex, issueUnsolvedMaxDays, orderType, orderDir);
	}
}