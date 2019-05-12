package kr.openbase.adcsmart.service.impl;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.OBDtoExcludeVip;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardTraffic;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardVSConnHistory;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardVSSummary;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBDefineFault;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBDashboardBase {
	// object의 category 정의.
	// 트래픽 등 object 데이터 set의 대상이 전체, group, 개별 adc 인지를 정의하는데 쓴다. 한가지 dto로 여러 종류의
	// 데이터를 뽑는 dashboard에 주로 쓰인다.
	public static final int OBJECT_CATEGORY_TOTAL = 0;
	public static final int OBJECT_CATEGORY_GROUP = 1;
	public static final int OBJECT_CATEGORY_ADC = 2;
	public static final int OBJECT_CATEGORY_VIRTUALSERVER = 3;
	public static final int OBJECT_CATEGORY_VIRTUALSERVICE = 4;

	public ArrayList<OBDtoDataObj> getAdcConnectionGraph(ArrayList<Integer> adcIndexList, Date fromTime, Date toTime,
			OBDatabase db) throws Exception {
		if (fromTime == null || toTime == null) // 둘 중 한쪽 날짜가 null이면 날짜 설정이 안된 것으로 보고 최근 24시간으로 설정한다.
		{
			fromTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.getDateWithHourOffset(-24, null)));
			toTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now()));
		}
		String sqlFromTime = OBParser.sqlString(OBDateTime.toString(new Timestamp(fromTime.getTime())));
		String sqlToTime = OBParser.sqlString(OBDateTime.toString(new Timestamp(toTime.getTime())));
		String adcIndexClause = OBParser.convertList2CommaString(adcIndexList);
		if (adcIndexClause.isEmpty()) {
			adcIndexClause = "-1";
		}

		String sqlText = String.format(" SELECT BLOCK_TIME, SUM(CONN_CURR) SUM_CONNS    \n"
				+ " FROM LOG_ADC_PERF_STATS                  \n" + " WHERE OCCUR_TIME >= %s AND OCCUR_TIME <= %s    \n"
				+ "     AND ADC_INDEX IN (%s)                      \n"
				+ " GROUP BY BLOCK_TIME                            \n"
				+ " ORDER BY BLOCK_TIME ASC                        \n", sqlFromTime, sqlToTime, adcIndexClause);

//		System.out.println("sqlText=" + sqlText);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText=" + sqlText);
		ResultSet rs = db.executeQuery(sqlText);

		ArrayList<OBDtoDataObj> connList = new ArrayList<OBDtoDataObj>();
		Timestamp blockTime;
		while (rs.next()) {
			blockTime = db.getTimestamp(rs, "BLOCK_TIME");
			if (blockTime != null) {
				// Sysmon에서 모니터링 주기마다 LOG_ADC_PERF_STATS.BLOCK_TIME에 같은 시간 구간에 해당하는 로그들을 묶는데,
				// 이 기능이 되기 전에 필드값이 null이므로 null 인 것들의 sum은 제외시킨다.
				OBDtoDataObj conn = new OBDtoDataObj();
				conn.setOccurTime(blockTime);
				conn.setValue(db.getLong(rs, "SUM_CONNS"));
				connList.add(conn);
			}
		}
		return connList;
	}

	public ArrayList<OBDtoDataObj> getAdcThroughputGraph(ArrayList<Integer> adcIndexList, Date fromTime, Date toTime,
			OBDatabase db) throws Exception {
		if (fromTime == null || toTime == null) // 둘 중 한쪽 날짜가 null이면 날짜 설정이 안된 것으로 보고 최근 24시간으로 설정한다.
		{
			fromTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.getDateWithHourOffset(-24, null)));
			toTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now()));
		}

		String sqlFromTime = OBParser.sqlString(OBDateTime.toString(new Timestamp(fromTime.getTime())));
		String sqlToTime = OBParser.sqlString(OBDateTime.toString(new Timestamp(toTime.getTime())));
		String adcIndexClause = OBParser.convertList2CommaString(adcIndexList);
		if (adcIndexClause.isEmpty()) {
			adcIndexClause = "-1";
		}

		String sqlText = String.format(" SELECT BLOCK_TIME, SUM(BPS_IN) SUM_BPS \n"
				+ " FROM LOG_ADC_PERF_STATS                  \n" + " WHERE OCCUR_TIME >= %s AND OCCUR_TIME <= %s    \n"
				+ "     AND ADC_INDEX IN (%s)                      \n"
				+ " GROUP BY BLOCK_TIME                            \n"
				+ " ORDER BY BLOCK_TIME ASC                        \n", sqlFromTime, sqlToTime, adcIndexClause);
//		System.out.println("sqlText=" + sqlText);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText=" + sqlText);

		ResultSet rs = db.executeQuery(sqlText);

		ArrayList<OBDtoDataObj> throuputList = new ArrayList<OBDtoDataObj>();
		Timestamp blockTime;
		while (rs.next()) {
			blockTime = db.getTimestamp(rs, "BLOCK_TIME");
			if (blockTime != null) {
				// Sysmon에서 모니터링 주기마다 LOG_ADC_PERF_STATS.BLOCK_TIME에 같은 시간 구간에 해당하는 로그들을 묶는데,
				// 이 기능이 되기 전에 필드값이 null이므로 null 인 것들의 sum은 제외시킨다.
				OBDtoDataObj throuput = new OBDtoDataObj();
				throuput.setOccurTime(blockTime);
				throuput.setValue(db.getLong(rs, "SUM_BPS"));
				throuputList.add(throuput);
			}
		}
		return throuputList;
	}

	public ArrayList<OBDtoDataObj> getVirtualServerConnectionGraph(String vsIndex, Date fromTime, Date toTime,
			OBDatabase db) throws Exception {
		if (fromTime == null || toTime == null) // 둘 중 한쪽 날짜가 null이면 날짜 설정이 안된 것으로 보고 최근 24시간으로 설정한다.
		{
			fromTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.getDateWithHourOffset(-24, null)));
			toTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now()));
		}
		String sqlFromTime = OBParser.sqlString(OBDateTime.toString(new Timestamp(fromTime.getTime())));
		String sqlToTime = OBParser.sqlString(OBDateTime.toString(new Timestamp(toTime.getTime())));
		String sqlText = "";

		sqlText = String.format(
				" SELECT OCCUR_TIME, CONN_CURR                    \n" + " FROM LOG_SVC_PERF_STATS                   \n"
						+ " WHERE OBJ_INDEX=%s                              \n"
						+ "     AND OCCUR_TIME >= %s  AND OCCUR_TIME <= %s  \n" + " ORDER BY OCCUR_TIME ASC ;",
				OBParser.sqlString(vsIndex), sqlFromTime, sqlToTime);
//		System.out.println("sqlText=" + sqlText);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText=" + sqlText);
		ResultSet rs = db.executeQuery(sqlText);

		ArrayList<OBDtoDataObj> connList = new ArrayList<OBDtoDataObj>();

		while (rs.next()) {
			OBDtoDataObj conn = new OBDtoDataObj();

			conn.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
			conn.setValue(db.getLong(rs, "CONN_CURR"));
			connList.add(conn);
		}

		return connList;
	}

	public ArrayList<OBDtoDataObj> getVirtualServiceConnectionGraph(String vserviceIndex, Date fromTime, Date toTime,
			OBDatabase db) throws Exception {
		if (fromTime == null || toTime == null) // 둘 중 한쪽 날짜가 null이면 날짜 설정이 안된 것으로 보고 최근 24시간으로 설정한다.
		{
			fromTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.getDateWithHourOffset(-24, null)));
			toTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now()));
		}

		String sqlFromTime = OBParser.sqlString(OBDateTime.toString(new Timestamp(fromTime.getTime())));
		String sqlToTime = OBParser.sqlString(OBDateTime.toString(new Timestamp(toTime.getTime())));
		String sqlText = "";

		sqlText = String.format(
				" SELECT OCCUR_TIME, CONN_CURR                    \n" + " FROM LOG_SVC_PERF_STATS                   \n"
						+ " WHERE OBJ_INDEX=%s                              \n"
						+ "     AND OCCUR_TIME >= %s  AND OCCUR_TIME <= %s  \n" +
//			"     AND CONN_CURR > 0                           \n" +
						" ORDER BY OCCUR_TIME ASC ;",
				OBParser.sqlString(vserviceIndex), sqlFromTime, sqlToTime);

//		System.out.println("sqlText=" + sqlText);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText=" + sqlText);
		ResultSet rs = db.executeQuery(sqlText);

		ArrayList<OBDtoDataObj> connList = new ArrayList<OBDtoDataObj>();

		while (rs.next()) {
			OBDtoDataObj conn = new OBDtoDataObj();

			conn.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
			conn.setValue(db.getLong(rs, "CONN_CURR"));
			connList.add(conn);
		}

		return connList;
	}

	public ArrayList<OBDtoDataObj> getVirtualServerThroughputGraph(String vsIndex, Date fromTime, Date toTime,
			OBDatabase db) throws Exception {
		if (fromTime == null || toTime == null) // 둘 중 한쪽 날짜가 null이면 날짜 설정이 안된 것으로 보고 최근 24시간으로 설정한다.
		{
			fromTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.getDateWithHourOffset(-24, null)));
			toTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now()));
		}

		String sqlFromTime = OBParser.sqlString(OBDateTime.toString(new Timestamp(fromTime.getTime())));
		String sqlToTime = OBParser.sqlString(OBDateTime.toString(new Timestamp(toTime.getTime())));
		String sqlText = "";

		sqlText = String.format(
				" SELECT OCCUR_TIME, (BPS_IN + BPS_OUT) BPS     \n" + " FROM LOG_SVC_PERF_STATS                 \n"
						+ " WHERE OBJ_INDEX=%s                            \n"
						+ "     AND OCCUR_TIME >= %s AND OCCUR_TIME <= %s \n" + " ORDER BY OCCUR_TIME ASC ;",
				OBParser.sqlString(vsIndex), sqlFromTime, sqlToTime);
//		System.out.println("sqlText=" + sqlText);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText=" + sqlText);
		ResultSet rs = db.executeQuery(sqlText);

		ArrayList<OBDtoDataObj> connList = new ArrayList<OBDtoDataObj>();

		while (rs.next()) {
			OBDtoDataObj conn = new OBDtoDataObj();

			conn.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
			conn.setValue(db.getLong(rs, "BPS"));
			connList.add(conn);
		}

		return connList;
	}

	public ArrayList<OBDtoDataObj> getVirtualServiceThroughputGraph(String vsrvIndex, Date fromTime, Date toTime,
			OBDatabase db) throws Exception {
		if (fromTime == null || toTime == null) // 둘 중 한쪽 날짜가 null이면 날짜 설정이 안된 것으로 보고 최근 24시간으로 설정한다.
		{
			fromTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.getDateWithHourOffset(-24, null)));
			toTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now()));
		}

		String sqlFromTime = OBParser.sqlString(OBDateTime.toString(new Timestamp(fromTime.getTime())));
		String sqlToTime = OBParser.sqlString(OBDateTime.toString(new Timestamp(toTime.getTime())));
		String sqlText = "";

		sqlText = String.format( // SERVICE테이블은 ALTEON만 쓰는데, ALTEON은 IN/OUT을 나누지 않아서 IN필드만 있다.
				" SELECT OCCUR_TIME, BPS_IN                      \n" + " FROM LOG_SVC_PERF_STATS                  \n"
						+ " WHERE OBJ_INDEX=%s                        \n"
						+ "     AND OCCUR_TIME >= %s  AND OCCUR_TIME <= %s \n" + " ORDER BY OCCUR_TIME ASC ;",
				OBParser.sqlString(vsrvIndex), sqlFromTime, sqlToTime);
//		System.out.println("sqlText=" + sqlText);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText=" + sqlText);
		ResultSet rs = db.executeQuery(sqlText);

		ArrayList<OBDtoDataObj> connList = new ArrayList<OBDtoDataObj>();

		while (rs.next()) {
			OBDtoDataObj conn = new OBDtoDataObj();

			conn.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
			conn.setValue(db.getLong(rs, "BPS_IN"));
			connList.add(conn);
		}

		return connList;
	}

	/**
	 * Virtual Server들의 상태별 통계를 구하는 핵심 함수 - 구하는 값 : virtual server의 다음 상태별 개수
	 * 정상,단절,꺼짐, 단절 N일 이내, 단절 N일 초과 - 대상 vs를 vs index list로 받는다. - 특정 계정에 소속된 vs, 특정
	 * 그룹에 소속된 vs들의 통계를 구할 때는 밖에서 vs index list를 구해서 호출하게 된다.
	 * 
	 * @param                  virtualServerIndexList: virtual server index 목록
	 * @param unavailLessNDays : 오늘 포함 최근 N일 이내 장애를 뽑으려면 지정한다. 0이면 query를 하지 않고 -1
	 *                         리턴한다.
	 * @param unavailOverNDays : 지속기간이 N일을 초과한 장애를 뽑으려면 지정한다. 0이면 query를 하지 않고 -1
	 *                         리턴한다.
	 * @param db
	 * @return
	 * @throws OBException
	 */

	public OBDtoDashboardVSSummary getVSSummary(ArrayList<Integer> adcIndexList, int unavailLessNDays,
			int unavailOverNDays, OBDatabase db) throws Exception {
		OBDtoDashboardVSSummary vsSummary = new OBDtoDashboardVSSummary();

		String sqlText = "";
		String sqlTextOverNDays = "";
		String sqlTextLessNDays = "";
		String adcIndexClause = OBParser.convertList2CommaString(adcIndexList);
		if (adcIndexClause.isEmpty()) {
			adcIndexClause = "-1";
		}

		sqlText = String.format(" SELECT STATUS, COUNT(INDEX) AS COUNT, 0 AS COUNT2, 0 AS COUNT3 \n" + // 정상 vs,
																										// disable(단절)
																										// vs, 꺼짐 vs
																										// 상태별로 구함
				" FROM TMP_SLB_VSERVER               \n" + " WHERE INDEX IN (                   \n"
				+ "     SELECT INDEX                   \n" + "     FROM TMP_SLB_VSERVER           \n"
				+ "     WHERE ADC_INDEX IN (%s)        \n" + " )                                  \n" + // where-in:empty
																										// string 불가,
																										// null 불가
				" GROUP BY STATUS                    \n", adcIndexClause);

		if (unavailLessNDays > 0) // N일 이내 조건이 있다.
		{
			sqlTextLessNDays = String
					.format(" SELECT -90 AS STATUS, COUNT(INDEX) AS COUNT, 0 AS COUNT2, 0 AS COUNT3 \n" + // N일 이내
																											// unavailable,
																											// 임시로 식별
																											// status
																											// -98 지정.
																											// union에 필요
							" FROM TMP_SLB_VSERVER                      \n"
							+ " WHERE INDEX IN (                          \n" + // where-in:empty string 불가, null 불가
							"     SELECT VS_INDEX                       \n"
							+ "     FROM LOG_ADC_FAULT                    \n"
							+ "     WHERE VS_INDEX IN (                   \n"
							+ "         SELECT INDEX                      \n"
							+ "         FROM TMP_SLB_VSERVER              \n"
							+ "         WHERE ADC_INDEX IN (%s)           \n"
							+ "       ) AND TYPE = %d                     \n" + // 장애 유형이 virtual server
							"       AND STATUS = %d                     \n" + // 상태는 장애 미해결
							"       AND OCCUR_TIME >= CURRENT_DATE-%d   \n" + // 일자 '제시일'(포함) 이후
							" )                                         \n", adcIndexClause,
							OBDefineFault.TYPE.VIRTUALSRV_DOWN.getCode(), OBDefineFault.STATUS_UNSOLVED,
							unavailLessNDays - 1 // 오늘 포함 최근 N일 이내를 조회하려면 (N-1)일전부터 이기 때문에 (전달된날수-1)을 쓴다.
					);
		} else {
			sqlTextLessNDays = "";
		}

		if (unavailOverNDays > 0) // N일 초과 조건이 있다.
		{
			sqlTextOverNDays = String
					.format(" SELECT -91 AS STATUS, COUNT(INDEX) AS COUNT, 0 AS COUNT2, 0 AS COUNT3  \n" + // N일 초과
																											// unavailable,
																											// 임시로 식별
																											// status
																											// -99 지정.
																											// union에 필요
							" FROM TMP_SLB_VSERVER                       \n"
							+ " WHERE INDEX IN (                           \n"
							+ "     SELECT VS_INDEX                        \n"
							+ "     FROM LOG_ADC_FAULT                     \n"
							+ "     WHERE VS_INDEX IN (                    \n"
							+ "         SELECT INDEX                       \n"
							+ "         FROM TMP_SLB_VSERVER               \n"
							+ "         WHERE ADC_INDEX IN (%s)            \n"
							+ "         ) AND TYPE = %d                    \n" + // 장애 유형이 virtual server
							"         AND STATUS = %d                    \n" + // 상태는 장애 미해결
							"         AND OCCUR_TIME < CURRENT_DATE-%d   \n" + // 일자 '제시일' 이전
							" )                                          \n", adcIndexClause,
							OBDefineFault.TYPE.VIRTUALSRV_DOWN.getCode(), OBDefineFault.STATUS_UNSOLVED,
							unavailOverNDays - 1 // N일을 초과한 데이터를 조회하려면, (N-1)일 이전 날짜까지이므로, (전달된날수-1)을 쓴다.
					);
		} else {
			sqlTextOverNDays = "";
		}

		String sqlTextYesterday = String.format(" SELECT -80 AS STATUS,                     \n"
				+ "     SUM(VS_COUNT_AVAIL) AS COUNT,         \n" + "     SUM(VS_COUNT_UNAVAIL) AS COUNT2,      \n"
				+ "     SUM(VS_COUNT_DISABLED) AS COUNT3      \n" + " FROM LOG_ADC_PERF_STATS             \n"
				+ " WHERE BLOCK_TIME = (                      \n" + "     SELECT MAX(BLOCK_TIME)                \n"
				+ "     FROM LOG_ADC_PERF_STATS         \n" + "     WHERE BLOCK_TIME >= CURRENT_DATE -1   \n"
				+ "         AND BLOCK_TIME < CURRENT_DATE     \n" + "     )                                     \n"
				+ "     AND ADC_INDEX IN (%s)                 \n", adcIndexClause);

		if (sqlTextLessNDays.isEmpty() == false) {
			sqlText = sqlText + " UNION ALL " + sqlTextLessNDays;
		}
		if (sqlTextOverNDays.isEmpty() == false) {
			sqlText = sqlText + " UNION ALL " + sqlTextOverNDays;
		}
		if (sqlTextYesterday.isEmpty() == false) {
			sqlText = sqlText + " UNION ALL " + sqlTextYesterday;
		}

//		System.out.println("sqlText=" + sqlText);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText=" + sqlText);

		ResultSet rs = db.executeQuery(sqlText);

		OBDtoDataObj totalObject = new OBDtoDataObj();

		OBDtoDataObj availObject = new OBDtoDataObj();
		Long availCount = 0L;

		OBDtoDataObj disableObject = new OBDtoDataObj();
		Long disableCount = 0L;

		OBDtoDataObj unavailObject = new OBDtoDataObj();
		Long unavailCount = 0L;

		OBDtoDataObj unavailLessNDaysObject = new OBDtoDataObj();
		Long unavailLessNDaysCount = 0L;

		OBDtoDataObj unavailOverNDaysObject = new OBDtoDataObj();
		Long unavailOverNDaysCount = 0L;

		int status = OBDefine.VS_STATUS.UNAVAILABLE;
		int count = 0;
		int yesterdayAvail = 0;
		int yesterdayUnavail = 0;
		int yesterdayDisabled = 0;

		while (rs.next()) {
			status = db.getInteger(rs, "STATUS");
			count = db.getInteger(rs, "COUNT");
			if (status == OBDefine.VS_STATUS.AVAILABLE) {
				availCount += count;
			} else if (status == OBDefine.VS_STATUS.DISABLE) {
				disableCount += count;
			} else if (status == OBDefine.VS_STATUS.UNAVAILABLE) {
				unavailCount += count;
			} else if (status == -90)// -98: N일 이내 단절
			{
				unavailLessNDaysCount += count;
			} else if (status == -91)// -99: N일 초과 단절
			{
				unavailOverNDaysCount += count;
			} else if (status == -80)// -80: 어제 ADC 마지막 값
			{
				yesterdayAvail += count;
				yesterdayUnavail += db.getInteger(rs, "COUNT2");
				yesterdayDisabled += db.getInteger(rs, "COUNT3");
			}
		}
		totalObject.setValue(availCount + disableCount + unavailCount);
		totalObject.setDiff(totalObject.getValue() - (yesterdayAvail + yesterdayUnavail + yesterdayDisabled));
		availObject.setValue(availCount);
		availObject.setDiff(availCount - yesterdayAvail);
		disableObject.setValue(disableCount);
		disableObject.setDiff(disableCount - yesterdayDisabled);
		unavailObject.setValue(unavailCount);
		unavailObject.setDiff(unavailCount - yesterdayUnavail);
		unavailLessNDaysObject.setValue(unavailLessNDaysCount);
		unavailOverNDaysObject.setValue(unavailOverNDaysCount);
		vsSummary.setAvail(availObject);
		vsSummary.setDisable(disableObject);
		vsSummary.setTotal(totalObject);
		vsSummary.setUnavail(unavailObject);
		vsSummary.setUnavailLessNDays(unavailLessNDaysObject);
		vsSummary.setUnavailOverNDays(unavailOverNDaysObject);
		// vsSummary.setUnavailCount();
		// //count.setVsUnavail(unavailCnt-unavailMinDaysCnt); //모든 단절 vs개수

		return vsSummary;
	}

	public ArrayList<OBDtoDashboardTraffic> getVSTrafficListTopN(ArrayList<Integer> adcIndexList,
			ArrayList<OBDtoExcludeVip> excludeVipList, int rank, OBDtoOrdering orderOption, OBDatabase db)
			throws Exception {
		String sqlText = "";
		String adcIndexClause = "";
		String rankClause = "";
		String orderClause = "";
		String excludeQuery = "";
		OBDtoExcludeVip excludeVipInfo = new OBDtoExcludeVip();

		adcIndexClause = OBParser.convertList2CommaString(adcIndexList);
		if (adcIndexClause.isEmpty()) {
			adcIndexClause = "-1";
		}
		if (excludeVipList == null || excludeVipList.isEmpty()) {
			excludeQuery = "";
		} else {
			for (int i = 0; i < excludeVipList.size(); i++) {
				excludeVipInfo = excludeVipList.get(i);
				if (i == 0) {
					excludeQuery = "WHERE (RES.IPADDRESS, RES.VIP, RES.VPORT) NOT IN ((" + excludeVipInfo.toSQLString()
							+ "))";
				} else {
					excludeQuery += "AND (RES.IPADDRESS, RES.VIP, RES.VPORT) NOT IN ((" + excludeVipInfo.toSQLString()
							+ "))";
				}

			}
		}

		/*
		 * excludeVip = OBParser.convertList2SingleQuotedString(excludeVipList);
		 * if(excludeVip.isEmpty()) { excludeVip="''"; }
		 */

		if (rank > 0) // rank 제한이 있는 조회
		{
			rankClause = " OFFSET 0 LIMIT " + rank;
		} else {
			rankClause = "";
		}
		if (orderOption == null) {
			orderClause = " CONNS DESC, BPSTOTAL DESC ";
		} else {
			if (orderOption.getOrderType() == OBDtoOrdering.TYPE_5FIFTH) // Connection
			{
				orderClause = " CONNS DESC, BPSTOTAL DESC ";
			} else if (orderOption.getOrderType() == OBDtoOrdering.TYPE_6SIXTH) // BPS In
			{
				orderClause = " BPSIN DESC, CONNS DESC ";
			} else if (orderOption.getOrderType() == OBDtoOrdering.TYPE_7SEVENTH) // BPS Out
			{
				orderClause = " BPSOUT DESC, CONNS DESC ";
			} else if (orderOption.getOrderType() == OBDtoOrdering.TYPE_8EIGHTH) // BPS Total
			{
				orderClause = " BPSTOTAL DESC, CONNS DESC ";
			} else {
				orderClause = " CONNS DESC, BPSTOTAL DESC ";
			}
		}

		// virtual server, virtual service 통합 상위 10 트래픽을 받는 목록을 구하려면,
		// (a)TMP_ADC_VS_RESC(virtual server)중 F5것만 뽑음
		// (b)TMP_ADC_VSERVICE_RESC(virtual service, 여긴 원래 Alteon밖에 없어서 따로 식별 안 해도 됨) 전체
		// 뽑음
		// (a),(b) UNION 하고 상위 10개를 뽑음
		sqlText = String.format(" SELECT RES.* FROM (                                                         \n"
				+ " SELECT A.VS_INDEX, A.ADC_INDEX, M1.NAME, M1.IPADDRESS,                      \n"
				+ " A.CONNS, A.BPSIN, A.BPSOUT, A.BPSTOTAL,                                     \n"
				+ " A.TYPE, B.VIRTUAL_IP VIP, B.VIRTUAL_PORT VPORT, B.STATUS                    \n"
				+ " FROM (                                                                      \n"
				+ "     SELECT B.INDEX AS VS_INDEX, A.ADC_INDEX, A.CONN_CURR CONNS,             \n"
				+ "     A.BPS_IN BPSIN, A.BPS_OUT BPSOUT,                                       \n"
				+ "     (A.BPS_IN + A.BPS_OUT) AS BPSTOTAL, 'F5' AS TYPE                        \n"
				+ "     FROM TMP_FAULT_SVC_PERF_STATS  A                                        \n"
				+ "     INNER JOIN TMP_SLB_VSERVER     B                                        \n"
				+ "     ON B.INDEX = A.OBJ_INDEX                                                \n"
				+ "     WHERE B.ADC_INDEX IN (                                                  \n"
				+ "         SELECT INDEX FROM MNG_ADC WHERE INDEX IN (%s) AND TYPE = %d         \n" + // 지정한 ADC들, 그 중에도
																										// F5 만
				"     )                                                                       \n"
				+ " ) A                                                                         \n"
				+ " INNER JOIN TMP_SLB_VSERVER B                                                \n"
				+ " ON A.VS_INDEX = B.INDEX                                                     \n"
				+ " LEFT JOIN MNG_ADC M1                                                        \n"
				+ " ON A.ADC_INDEX = M1.INDEX                                                   \n"
				+ " UNION ALL                                                                   \n"
				+ " SELECT C.VS_INDEX, C.ADC_INDEX, M2.NAME, M2.IPADDRESS,                      \n"
				+ " C.CONNS, C.BPSIN, C.BPSOUT, C.BPSTOTAL,                                     \n"
				+ " C.TYPE, C.VIP, C.VPORT, D.STATUS                                            \n"
				+ " FROM (                                                                      \n"
				+ "     SELECT A.OBJ_INDEX VS_INDEX, A.ADC_INDEX, A.CONN_CURR CONNS,            \n"
				+ "     A.BPS_IN BPSIN, A.BPS_OUT BPSOUT, (A.BPS_IN + A.BPS_OUT) AS BPSTOTAL,   \n"
				+ "         C.VIRTUAL_IP VIP, B.VIRTUAL_PORT VPORT, 'ALTEON' AS TYPE            \n"
				+ "     FROM TMP_FAULT_SVC_PERF_STATS  A                                        \n"
				+ "     INNER JOIN TMP_SLB_VS_SERVICE  B                                        \n"
				+ "     ON B.INDEX = A.OBJ_INDEX                                                \n"
				+ "     INNER JOIN TMP_SLB_VSERVER     C                                        \n"
				+ "     ON C.INDEX = B.VS_INDEX                                                 \n"
				+ "     WHERE A.ADC_INDEX IN (                                                  \n"
				+ "         SELECT INDEX FROM MNG_ADC WHERE INDEX IN (%s) AND TYPE = %d         \n" + // 지정한 ADC들. 테이블에
																										// ALTEON뿐이라
																										// TYPE구별이 필요없지만
																										// 정확성을 위해 넣음
				"     )                                                                       \n"
				+ " ) C                                                                         \n"
				+ " INNER JOIN TMP_SLB_VS_SERVICE D                                             \n"
				+ " ON C.VS_INDEX = D.INDEX                                                     \n"
				+ " LEFT JOIN MNG_ADC M2                                                        \n"
				+ " ON C.ADC_INDEX = M2.INDEX                                                   \n"
				+ " UNION ALL                                                                   \n"
				+ " SELECT E.VS_INDEX, E.ADC_INDEX, M3.NAME, M3.IPADDRESS,                      \n"
				+ " E.CONNS, E.BPSIN, E.BPSOUT, E.BPSTOTAL,                                     \n"
				+ " E.TYPE, F.VIRTUAL_IP VIP, F.VIRTUAL_PORT VPORT, F.STATUS                    \n"
				+ " FROM (                                                                      \n"
				+ "     SELECT B.INDEX AS VS_INDEX, A.ADC_INDEX, A.CONN_CURR CONNS,             \n"
				+ "     A.BPS_IN BPSIN, A.BPS_OUT BPSOUT,                                       \n"
				+ "    (A.BPS_IN + A.BPS_OUT) AS BPSTOTAL, 'PAS' AS TYPE                        \n"
				+ "     FROM TMP_FAULT_SVC_PERF_STATS  A                                        \n"
				+ "     INNER JOIN TMP_SLB_VSERVER     B                                        \n"
				+ "     ON B.INDEX = A.OBJ_INDEX                                                \n"
				+ "     WHERE B.ADC_INDEX IN (                                                  \n"
				+ "         SELECT INDEX FROM MNG_ADC WHERE INDEX IN (%s) AND TYPE = %d         \n" + // 지정한 ADC들, 그 중에도
																										// PAS 만
				"     )                                                                       \n"
				+ " ) E                                                                         \n"
				+ " INNER JOIN TMP_SLB_VSERVER F                                                \n"
				+ " ON E.VS_INDEX = F.INDEX                                                     \n"
				+ " LEFT JOIN MNG_ADC M3                                                        \n"
				+ " ON E.ADC_INDEX = M3.INDEX                                                   \n"
				+ " UNION ALL                                                                   \n"
				+ " SELECT H.VS_INDEX, H.ADC_INDEX, M4.NAME, M4.IPADDRESS,                      \n"
				+ " H.CONNS, H.BPSIN, H.BPSOUT, H.BPSTOTAL,                                     \n"
				+ " H.TYPE, I.VIRTUAL_IP VIP, I.VIRTUAL_PORT VPORT, I.STATUS                    \n"
				+ " FROM (                                                                      \n"
				+ "     SELECT B.INDEX AS VS_INDEX, A.ADC_INDEX, A.CONN_CURR CONNS,             \n"
				+ "     A.BPS_IN BPSIN, A.BPS_OUT BPSOUT,                                       \n"
				+ "    (A.BPS_IN + A.BPS_OUT) AS BPSTOTAL, 'PASK' AS TYPE                       \n"
				+ "     FROM TMP_FAULT_SVC_PERF_STATS  A                                        \n"
				+ "     INNER JOIN TMP_SLB_VSERVER     B                                        \n"
				+ "     ON B.INDEX = A.OBJ_INDEX                                                \n"
				+ "     WHERE B.ADC_INDEX IN (                                                  \n"
				+ "         SELECT INDEX FROM MNG_ADC WHERE INDEX IN (%s) AND TYPE = %d         \n" + // 지정한 ADC들, 그 중에도
																										// PASK만
				"     )                                                                       \n"
				+ " ) H                                                                         \n"
				+ " INNER JOIN TMP_SLB_VSERVER I                                                \n"
				+ " ON H.VS_INDEX = I.INDEX                                                     \n"
				+ " LEFT JOIN MNG_ADC M4                                                        \n"
				+ " ON H.ADC_INDEX = M4.INDEX) RES                                              \n"
				+ " %s                                                                          \n"
				+ " ORDER BY %s, VIP ASC, VPORT ASC                                             \n"
				+ " %s                                                                          \n", adcIndexClause,
				OBDefine.ADC_TYPE_F5, adcIndexClause, OBDefine.ADC_TYPE_ALTEON, adcIndexClause,
				OBDefine.ADC_TYPE_PIOLINK_PAS, adcIndexClause, OBDefine.ADC_TYPE_PIOLINK_PASK, excludeQuery,
				orderClause, rankClause);
//		System.out.println("sqlText=" + sqlText);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText=" + sqlText);
		ResultSet rs = db.executeQuery(sqlText);

		String type = "";
		ArrayList<OBDtoDashboardTraffic> list = new ArrayList<OBDtoDashboardTraffic>();
		while (rs.next()) {
			OBDtoDashboardTraffic traffic = new OBDtoDashboardTraffic();
			OBDtoDataObj connection = new OBDtoDataObj();
			// OBDtoDataObj throughput = new OBDtoDataObj();
			OBDtoDataObj bpsIn = new OBDtoDataObj();
			OBDtoDataObj bpsOut = new OBDtoDataObj();
			OBDtoDataObj bpsTotal = new OBDtoDataObj();
			// throughput.setValue(db.getLong(rs, "BPS"));
			// traffic.setThroughput(throughput);
			traffic.setIndex(db.getString(rs, "VS_INDEX"));
			connection.setValue(db.getLong(rs, "CONNS"));
			traffic.setConnection(connection);

			bpsIn.setValue(db.getLong(rs, "BPSIN"));
			traffic.setBpsIn(bpsIn);
			bpsOut.setValue(db.getLong(rs, "BPSOUT"));
			traffic.setBpsOut(bpsOut);
			bpsTotal.setValue(db.getLong(rs, "BPSTOTAL"));
			traffic.setBpsTotal(bpsTotal);

			type = db.getString(rs, "TYPE");
			if (type.equals("F5")) {
				traffic.setVendor(OBDefine.ADC_TYPE_F5);
			} else if (type.equals("ALTEON")) {
				traffic.setVendor(OBDefine.ADC_TYPE_ALTEON);
			} else if (type.equals("PAS")) {
				traffic.setVendor(OBDefine.ADC_TYPE_PIOLINK_PAS);
			} else if (type.equals("PASK")) {
				traffic.setVendor(OBDefine.ADC_TYPE_PIOLINK_PASK);
			}
			traffic.setNameIp(db.getString(rs, "VIP"));
			traffic.setPort(db.getInteger(rs, "VPORT"));
			traffic.setStatus(db.getInteger(rs, "STATUS"));
			traffic.setAdcName(db.getString(rs, "NAME"));
			traffic.setAdcIp(db.getString(rs, "IPADDRESS"));
			list.add(traffic);
		}

		return list;
	}

	/**
	 * 현재 TopN Connection인 VirtualServer/VirtualServer의 지정 기간동안 connection 추이를 구한다.
	 * getVSTrafficListTopN()으로 같이 쓸 수 있을 것 같아보여도, 구하는 항목과 유형이 달라서 따로 만들었다.
	 * getVSTrafficListTopN()으로 topN vs를 구할 수 있지만, 불필요한 join이 생기고, 함수 호출이 한번 더 생겨서
	 * 직접 구한다.
	 */
	public ArrayList<OBDtoDashboardVSConnHistory> getVSConnectionGraphListTopN(ArrayList<Integer> adcIndexList,
			int rank, Date fromTime, Date toTime, OBDatabase db) throws Exception {
		String sqlText = "";

		String adcIndexClause = "";
		String rankClause = "";

		adcIndexClause = OBParser.convertList2CommaString(adcIndexList);
		if (adcIndexClause.isEmpty()) {
			adcIndexClause = "-1";
		}

		if (rank > 0) // rank 제한이 있는 조회
		{
			rankClause = " OFFSET 0 LIMIT " + rank;
		} else {
			rankClause = "";
		}

		// virtual server, virtual service 통합 connection 상위 N 목록을 구한다.
		// (a)TMP_ADC_VS_RESC(virtual server)중 F5것만 뽑음
		// (b)TMP_ADC_VSERVICE_RESC(virtual service, 여긴 원래 Alteon밖에 없어서 따로 식별 안 해도 됨) 전체
		// 뽑음
		// (a),(b) UNION 하고 상위 10개를 뽑음
		sqlText = String.format(" SELECT A.VS_INDEX, M1.NAME, M1.IPADDRESS, A.CONNS, A.TYPE,               \n"
				+ "     B.VIRTUAL_IP VIP, B.VIRTUAL_PORT VPORT                               \n"
				+ " FROM (                                                                   \n"
				+ "     SELECT B.INDEX AS VS_INDEX, B.ADC_INDEX, A.CONN_CURR CONNS, 'F5' AS TYPE      \n"
				+ "     FROM TMP_FAULT_SVC_PERF_STATS            A                           \n"
				+ "     INNER JOIN TMP_SLB_VSERVER               B                           \n"
				+ "     ON B.INDEX = A.OBJ_INDEX                                             \n"
				+ "     WHERE B.ADC_INDEX IN (                                               \n"
				+ "         SELECT INDEX FROM MNG_ADC WHERE INDEX IN (%s) AND TYPE = %d      \n" + // 지정한 ADC들, 그 중에도 F5
																									// 만
				"     )                                                                    \n"
				+ " ) A                                                                      \n"
				+ " INNER JOIN TMP_SLB_VSERVER B                                             \n"
				+ " ON A.VS_INDEX = B.INDEX                                                  \n"
				+ " LEFT JOIN MNG_ADC M1                                                     \n"
				+ " ON A.ADC_INDEX = M1.INDEX                                                \n"
				+ " UNION ALL                                                                \n"
				+ " SELECT C.VS_INDEX, M2.NAME, M2.IPADDRESS, C.CONNS,                       \n"
				+ "    C.TYPE, C.VIP, C.VPORT                                                \n"
				+ " FROM (                                                                   \n"
				+ "     SELECT B.INDEX VS_INDEX, A.CONN_CURR CONNS, 'ALTEON' AS TYPE,        \n"
				+ "         C.VIRTUAL_IP VIP, B.VIRTUAL_PORT VPORT, A.ADC_INDEX             \n"
				+ "     FROM TMP_FAULT_SVC_PERF_STATS           A                            \n"
				+ "     INNER JOIN TMP_SLB_VS_SERVICE           B                            \n"
				+ "     ON B.INDEX = A.OBJ_INDEX                                             \n"
				+ "     INNER JOIN TMP_SLB_VSERVER              C                            \n"
				+ "     ON C.INDEX = B.VS_INDEX                                              \n"
				+ "     WHERE A.ADC_INDEX IN (                                               \n"
				+ "         SELECT INDEX FROM MNG_ADC WHERE INDEX IN (%s) AND TYPE = %d      \n" + // 지정한 ADC들. 테이블에
																									// ALTEON뿐이라 TYPE구별이
																									// 필요없지만 정확성을 위해 넣음
				"     )                                                                    \n"
				+ " ) C                                                                      \n"
				+ " INNER JOIN TMP_SLB_VS_SERVICE D                                          \n"
				+ " ON C.VS_INDEX = D.INDEX                                                  \n"
				+ " LEFT JOIN MNG_ADC M2                                                     \n"
				+ " ON C.ADC_INDEX = M2.INDEX                                                \n"
				+ " UNION ALL                                                                \n"
				+ " SELECT E.VS_INDEX, M3.NAME, M3.IPADDRESS, E.CONNS, E.TYPE,               \n" + // PAS 시
				"     F.VIRTUAL_IP VIP, F.VIRTUAL_PORT VPORT                               \n"
				+ " FROM (                                                                   \n"
				+ "     SELECT B.INDEX AS VS_INDEX, B.ADC_INDEX, A.CONN_CURR CONNS, 'PAS' AS TYPE      \n"
				+ "     FROM TMP_FAULT_SVC_PERF_STATS            A                           \n"
				+ "     INNER JOIN TMP_SLB_VSERVER               B                           \n"
				+ "     ON B.INDEX = A.OBJ_INDEX                                             \n"
				+ "     WHERE B.ADC_INDEX IN (                                               \n"
				+ "         SELECT INDEX FROM MNG_ADC WHERE INDEX IN (%s) AND TYPE = %d      \n" + // 지정한 ADC들, 그 중에도
																									// PAS 만
				"     )                                                                    \n"
				+ " ) E                                                                      \n"
				+ " INNER JOIN TMP_SLB_VSERVER F                                              \n"
				+ " ON E.VS_INDEX = F.INDEX                                                  \n"
				+ " LEFT JOIN MNG_ADC M3                                                     \n"
				+ " ON E.ADC_INDEX = M3.INDEX                                                \n"
				+ " UNION ALL                                                                \n"
				+ " SELECT H.VS_INDEX, M4.NAME, M4.IPADDRESS, H.CONNS, H.TYPE,               \n" + // PASK 시
				"     I.VIRTUAL_IP VIP, I.VIRTUAL_PORT VPORT                               \n"
				+ " FROM (                                                                   \n"
				+ "     SELECT B.INDEX AS VS_INDEX, B.ADC_INDEX, A.CONN_CURR CONNS, 'PAS' AS TYPE      \n"
				+ "     FROM TMP_FAULT_SVC_PERF_STATS            A                           \n"
				+ "     INNER JOIN TMP_SLB_VSERVER               B                           \n"
				+ "     ON B.INDEX = A.OBJ_INDEX                                             \n"
				+ "     WHERE B.ADC_INDEX IN (                                               \n"
				+ "         SELECT INDEX FROM MNG_ADC WHERE INDEX IN (%s) AND TYPE = %d      \n"
				+ "     )                                                                    \n"
				+ " ) H                                                                      \n"
				+ " INNER JOIN TMP_SLB_VSERVER I                                             \n"
				+ " ON H.VS_INDEX = I.INDEX                                                  \n"
				+ " LEFT JOIN MNG_ADC M4                                                     \n"
				+ " ON H.ADC_INDEX = M4.INDEX                                                \n" +

				" ORDER BY CONNS DESC, VIP ASC                                             \n"
				+ " %s                                                                       \n", adcIndexClause,
				OBDefine.ADC_TYPE_F5, adcIndexClause, OBDefine.ADC_TYPE_ALTEON, adcIndexClause,
				OBDefine.ADC_TYPE_PIOLINK_PAS, adcIndexClause, OBDefine.ADC_TYPE_PIOLINK_PASK, rankClause);
//		System.out.println("sqlText=" + sqlText);
//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText=" + sqlText);
		ResultSet rs = db.executeQuery(sqlText);

		ArrayList<OBDtoDashboardTraffic> tempList = new ArrayList<OBDtoDashboardTraffic>(); // TopN을 임시로 저장한다. 각각의 그래프
																							// 데이터 구하는 loop에서 쓴다.
		String type = "";
		while (rs.next()) {
			OBDtoDashboardTraffic traffic = new OBDtoDashboardTraffic();
			traffic.setIndex(db.getString(rs, "VS_INDEX"));
			type = db.getString(rs, "TYPE");
			if (type.equals("F5")) {
				traffic.setVendor(OBDefine.ADC_TYPE_F5);
			} else if (type.equals("ALTEON")) {
				traffic.setVendor(OBDefine.ADC_TYPE_ALTEON);
			} else if (type.equals("PAS")) {
				traffic.setVendor(OBDefine.ADC_TYPE_PIOLINK_PAS);
			} else if (type.equals("PASK")) {
				traffic.setVendor(OBDefine.ADC_TYPE_PIOLINK_PASK);
			}
			traffic.setNameIp(db.getString(rs, "VIP"));
			traffic.setPort(db.getInteger(rs, "VPORT"));
			traffic.setAdcName(db.getString(rs, "NAME"));
			traffic.setAdcIp(db.getString(rs, "IPADDRESS"));
			tempList.add(traffic);
		}

		ArrayList<OBDtoDashboardVSConnHistory> resultList = new ArrayList<OBDtoDashboardVSConnHistory>();

		for (OBDtoDashboardTraffic temp : tempList) {
			OBDtoDashboardVSConnHistory data = new OBDtoDashboardVSConnHistory();
			data.setVsIPAddress(temp.getNameIp());
			data.setPort(temp.getPort());
			data.setAdcIPAddress(temp.getAdcIP());
			data.setAdcName(temp.getAdcName());
			if (temp.getVendor().equals(OBDefine.ADC_TYPE_ALTEON) == true) {
				data.setConnection(getVirtualServiceConnectionGraph(temp.getIndex(), fromTime, toTime, db));
			} else if (temp.getVendor().equals(OBDefine.ADC_TYPE_F5) == true) {
				data.setConnection(getVirtualServerConnectionGraph(temp.getIndex(), fromTime, toTime, db));
			} else if (temp.getVendor().equals(OBDefine.ADC_TYPE_PIOLINK_PAS) == true) {
				data.setConnection(getVirtualServerConnectionGraph(temp.getIndex(), fromTime, toTime, db));
			} else if (temp.getVendor().equals(OBDefine.ADC_TYPE_PIOLINK_PASK) == true) {
				data.setConnection(getVirtualServerConnectionGraph(temp.getIndex(), fromTime, toTime, db));
			}
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("ip=%s port=%d count=%d", data.getVsIPAddress(),
					data.getPort(), data.getConnection().size()));
			resultList.add(data);
		}
		return resultList;
	}

	public ArrayList<OBDtoDashboardTraffic> getAdcTrafficListTopN(ArrayList<Integer> adcIndexList, int rank,
			OBDatabase db) throws Exception // TODO 유보
	{
		String sqlText = "";
		String adcIndexClause = "";
		String rankClause = "";

		adcIndexClause = OBParser.convertList2CommaString(adcIndexList);
		if (adcIndexClause.isEmpty()) {
			adcIndexClause = "-1";
		}

		if (rank > 0) // rank 제한이 있는 조회
		{
			rankClause = "OFFSET 0 LIMIT " + rank;
		} else {
			rankClause = "";
		}
		sqlText = String.format(" SELECT ADC.NAME, ADC.IPADDRESS, ADC.TYPE, ADC.STATUS, DATA.BPS_IN \n"
				+ " FROM ( SELECT INDEX, NAME, IPADDRESS, TYPE, STATUS                \n"
				+ "     FROM MNG_ADC WHERE INDEX IN (%s)               \n"
				+ " ) ADC,                                             \n"
				+ " TMP_FAULT_ADC_PERF_STATS DATA                             \n"
				+ " WHERE ADC.INDEX = DATA.ADC_INDEX                   \n"
				+ " ORDER BY DATA.BPS_IN DESC, ADC.NAME                \n"
				+ " %s ;                                               \n", adcIndexClause, rankClause);

		ResultSet rs = db.executeQuery(sqlText);

		ArrayList<OBDtoDashboardTraffic> list = new ArrayList<OBDtoDashboardTraffic>();
		while (rs.next()) {
			OBDtoDataObj connection = new OBDtoDataObj();
			OBDtoDataObj throughput = new OBDtoDataObj();
			OBDtoDashboardTraffic traffic = new OBDtoDashboardTraffic();
			traffic.setVendor(db.getInteger(rs, "TYPE"));
			traffic.setNameIp(db.getString(rs, "NAME"));
			traffic.setPort(null); // ADC이므로 데이터가 없음
			traffic.setStatus(db.getInteger(rs, "STATUS"));
			traffic.setAdcName(db.getString(rs, "NAME"));
			traffic.setAdcIp(db.getString(rs, "IPADDRESS"));

			connection.setValue(null); // connection은 조회 대상이 아님, null 참조 방지용으로 만듦
			traffic.setConnection(connection);
			Long value = db.getLong(rs, "BPS_IN");
			if (value == -1)
				value = 0L;
			throughput.setValue(value);
			traffic.setThroughput(throughput);

			list.add(traffic);
		}
		return list;
	}

	/**
	 * @param args
	 */
//	public static void main(String[] args)
//	{
////		new OBDashboardBase().fillBlockTime();
//	}
//	
	private class TableTemp {
		Long index;
		Timestamp time;
		Integer adcIndex;

		public Long getIndex() {
			return index;
		}

		public void setIndex(Long index) {
			this.index = index;
		}

		public Timestamp getTime() {
			return time;
		}

		public void setTime(Timestamp time) {
			this.time = time;
		}

//		public Integer getAdcIndex()
//		{
//			return adcIndex;
//		}
		public void setAdcIndex(Integer adcIndex) {
			this.adcIndex = adcIndex;
		}

		@Override
		public String toString() {
			return "TableTemp [index=" + index + ", time=" + time + ", adcIndex=" + adcIndex + "]";
		}
	}

	// 3차 dashboard(우측)에서 ADC 통합 통계를 뽑으려면 ADC들의 시간을 맞춘 단위 block들을 만들 필요가 있다.
	// 이 time block은 데몬이 만들기 때문에 적용 이전 데이터에는 block time이 없을 수 있다. 그럴 경우 24시간 분량의
	// 데이터를 만드는 함수이다.
	public void fillBlockTime() {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		ArrayList<TableTemp> list = new ArrayList<TableTemp>();

		try {
			sqlText = " SELECT INDEX, OCCUR_TIME, ADC_INDEX                          \n"
					+ " FROM LOG_ADC_PERF_STATS                                \n"
					+ " WHERE OCCUR_TIME > CURRENT_TIMESTAMP - INTERVAL '24 hours'   \n"
					+ "     AND ADC_INDEX = 1 AND BLOCK_TIME IS NULL                 \n" +
					// " AND ADC_INDEX = 1004 \n" +
					" ORDER BY OCCUR_TIME ASC                                      \n";
			db.openDB();

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				TableTemp temp = new TableTemp();
				temp.setIndex(db.getLong(rs, "INDEX"));
				temp.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				temp.setTime(db.getTimestamp(rs, "OCCUR_TIME"));
				list.add(temp);
			}

			Long prevIndex = 0L;
			for (TableTemp tt : list) {
				if (prevIndex.equals(0L)) {
					prevIndex = tt.getIndex();
				} else {
					sqlText = String.format(
							" UPDATE LOG_ADC_PERF_STATS SET BLOCK_TIME = %s  \n"
									+ " WHERE INDEX > %d AND INDEX <= %d              \n"
									+ "     AND BLOCK_TIME IS NULL                    \n",
							OBParser.sqlString(OBDateTime.toString(tt.getTime())), prevIndex, tt.getIndex());
					db.executeUpdate(sqlText);
					prevIndex = tt.getIndex();
				}
			}
		} catch (Exception e) {
		} finally {
			if (db != null)
				db.closeDB();
		}
	}
}
