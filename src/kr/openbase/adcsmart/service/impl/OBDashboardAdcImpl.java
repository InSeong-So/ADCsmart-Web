package kr.openbase.adcsmart.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.OBDashboardAdc;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSystemLog;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.OBDtoExcludeVip;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardAdcSummary;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardFaultStatus;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardSlbChangeStatus;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardTraffic;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardTrafficStatus;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardVSConnHistory;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardVSSummary;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBDefineFault;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBDashboardAdcImpl implements OBDashboardAdc {
	static final int VS_RECENTFAULT_DAYS = 7; // 발생 시기가 7일 이내 장애 수를 뽑는다.
	static final int VS_TRAFFIC_RANK_SELECTIVE = 10;
	static final int FAULT_LOG_RANK = 3;
	static final int DYNAMIC_FAULT_LOG_RANK = 5;
	static final int CONFIG_LOG_RANK = 3;
	static final int DYNAMIC_CONFIG_LOG_RANK = 5;
	static final int ONE_WEEK_START_OFFSET = -6;
	static final int ADC_TRAFFIC_RANK = 5;
	static final int DYNAMIC_ADC_TRAFFIC_RANK = 10;
	static final int VS_TRAFFIC_RANK = 5;

	// object의 category 정의.
	// 트래픽 등 object 데이터 set의 대상이 전체, group, 개별 adc 인지를 정의하는데 쓴다. 한가지 dto로 여러 종류의
	// 데이터를 뽑는 dashboard에 주로 쓰인다.
	public static final int OBJECT_CATEGORY_TOTAL = 0;
	public static final int OBJECT_CATEGORY_GROUP = 1;
	public static final int OBJECT_CATEGORY_ADC = 2;
	public static final int OBJECT_CATEGORY_VIRTUALSERVER = 3;
	public static final int OBJECT_CATEGORY_VIRTUALSERVICE = 4;

	public static enum SORT_ORDER {
		CONNECTION, THROUGHPUT
	};

	private ArrayList<Integer> getTargetAdcList(Integer accountIndex, Integer objectCategory, Integer objectIndex)
			throws OBException {
		ArrayList<Integer> result = new ArrayList<Integer>();
		if (accountIndex != null && objectCategory != null) {
			if (objectCategory.equals(OBDashboardBase.OBJECT_CATEGORY_TOTAL) == true) // account가 접근 가능한 all ADC
			{
				result = new OBAccountImpl().getInvolvedAdcList(accountIndex);
			} else if (objectCategory.equals(OBDashboardBase.OBJECT_CATEGORY_GROUP) == true) // group내 account가 접근 가능한
																								// ADC
			{
				result = new OBAdcManagementImpl().getAdcIndexListInGroup(objectIndex, accountIndex);
			} else if (objectCategory.equals(OBDashboardBase.OBJECT_CATEGORY_ADC) == true) // 단일 ADC, account 고려 필요없음
			{
				result.add(objectIndex);
			}
		} else {
			// 비정상 조회, 데이터 없이 빈 list를 리턴한다.
		}
		return result;
	}

	private ArrayList<OBDtoExcludeVip> getExcludeVipList() throws OBException {
		String ExcludeVipFile = OBDefine.DIR_EXCLUDE_VS;
		String readLine = "";
		BufferedReader br = null;
		File file = new File(ExcludeVipFile);
		if (!file.exists()) {
			return null;
		}
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "euc-kr"));
			ArrayList<OBDtoExcludeVip> excludevipList = new ArrayList<OBDtoExcludeVip>();
			OBDtoExcludeVip excludevipInfo = new OBDtoExcludeVip();
			while ((readLine = br.readLine()) != null) {
				if (readLine.startsWith("[excludevip]")) {
					excludevipInfo = new OBDtoExcludeVip();
				} else if (readLine.startsWith("vip")) {
					String[] pair = readLine.trim().split("=");
					excludevipInfo.setVsIp(pair[1]);
				} else if (readLine.startsWith("vport")) {
					String[] pair = readLine.trim().split("=");
					excludevipInfo.setVsPort(pair[1]);

				} else if (readLine.startsWith("adcip")) {
					String[] pair = readLine.trim().split("=");
					excludevipInfo.setAdcIp(pair[1]);
				} else if (readLine.startsWith("[end]")) {
					excludevipList.add(excludevipInfo);
				} else if (readLine.isEmpty()) {
				} else {
					break;
				}
			}
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
					"Successfully end get ExcludeVip list, ExcludeVipList:" + excludevipList.size());
			return excludevipList;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, e.getMessage());
				}
			}
		}
	}

	@Override
	public OBDtoDashboardAdcSummary getAdcGroupSummary(Integer accountIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. accountIndex:%d", accountIndex));
		if (accountIndex == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "accountIndex");
		}

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			// account가 접근할 수 있는 ADC 중 지정한 object에 속하는 ADC index를 구한다.
			OBDtoADCObject object = new OBDtoADCObject();
			object.setCategory(OBJECT_CATEGORY_TOTAL);
			ArrayList<Integer> adcIndexList = getTargetAdcList(accountIndex, object.getCategory(), object.getIndex());
			OBDtoDashboardAdcSummary result = getAdcGroupSummary(adcIndexList, db);
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public OBDtoDashboardAdcSummary getAdcGroupSummary(ArrayList<Integer> adcIndexList, OBDatabase db)
			throws Exception {
		String sqlText = "";
		String adcIndexClause = "";

		adcIndexClause = OBParser.convertList2CommaString(adcIndexList);
		// 선택된 adc가 없으면 where-in clause의 오류를 막기위해 항상 false가 나는 조건을 준다.
		if (adcIndexClause.isEmpty()) {
			adcIndexClause = "-1";
		}

		sqlText = String.format(" SELECT GR.NAME GR_NAME, GR.INDEX GR_INDEX, GR.DESCRIPTION GR_DESCR, \n"
				+ "     ADC.STATUS ADC_STATUS, ADC.NAME ADC_NAME, ADC.INDEX ADC_INDEX,  \n"
				+ "     ADC.TYPE ADC_TYPE, ADC.DESCRIPTION ADC_DESCR,                   \n"
				+ "     DATA.OCCUR_TIME, DATA.CONN_CURR, DATA.BPS_IN                    \n"
				+ " FROM (SELECT INDEX, GROUP_INDEX, NAME, STATUS, TYPE, DESCRIPTION    \n"
				+ "       FROM MNG_ADC                                                  \n"
				+ "       WHERE INDEX IN (%s) AND AVAILABLE = 1                         \n"
				+ " ) ADC                                                               \n"
				+ " LEFT JOIN (SELECT INDEX, NAME, DESCRIPTION                          \n"
				+ "            FROM MNG_ADC_GROUP                                       \n"
				+ "            WHERE AVAILABLE=1                                        \n"
				+ " ) GR                                                                \n"
				+ " ON ADC.GROUP_INDEX = GR.INDEX                                       \n"
				+ " LEFT JOIN (SELECT OCCUR_TIME, ADC_INDEX, CONN_CURR, BPS_IN          \n"
				+ "            FROM TMP_FAULT_ADC_PERF_STATS                            \n"
				+ "            WHERE OCCUR_TIME > NOW() - INTERVAL '600 SECONDS'        \n"
				+ " ) DATA                                                              \n"
				+ " ON ADC.INDEX = DATA.ADC_INDEX                                       \n"
				+ " ORDER BY GR.NAME, ADC.NAME                                          \n", adcIndexClause);
//		System.out.println("sqlText=" + sqlText);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText=" + sqlText);

		ResultSet rs = db.executeQuery(sqlText);

		int groupIndexTemp = 0;
		boolean groupFound = false;
		ArrayList<OBDtoDashboardAdcSummary> grouplist = new ArrayList<OBDtoDashboardAdcSummary>();

		// head 준비
		OBDtoDashboardAdcSummary head = new OBDtoDashboardAdcSummary();
		// head.setAdcList(); //group을 여기에 붙인다.
		head.setConnectionInfo(new OBDtoDataObj()); // 없다.
		head.setThroughputInfo(new OBDtoDataObj()); // 역시 없다.
		OBDtoADCObject headInfo = new OBDtoADCObject();
		headInfo.setCategory(OBJECT_CATEGORY_TOTAL);
		// 아래 네 멤버의 값은 constructor 할당한 대로 쓰고, null을 주지 않는다.
//		headInfo.setIndex(null);
//		headInfo.setName(null);
//		headInfo.setStatus(null);
//		headInfo.setVendor(null);
		head.setObjectInfo(headInfo);
		OBDtoDashboardAdcSummary workGroup;

		while (rs.next()) {
			workGroup = null; // 작업할 group 선언, 기존 group이거나 새 group이거나

			// 일단 adc 낱개 데이터를 만든다.
			OBDtoDashboardAdcSummary oneAdc = new OBDtoDashboardAdcSummary();

			oneAdc.setAdcList(null); // adc에 해당없음

			OBDtoDataObj adcConnection = new OBDtoDataObj();
			adcConnection.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
			adcConnection.setValue(db.getLong(rs, "CONN_CURR"));
			oneAdc.setConnectionInfo(adcConnection);

			OBDtoDataObj adcThroughput = new OBDtoDataObj();
			adcThroughput.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
			adcThroughput.setValue(db.getLong(rs, "BPS_IN"));
			oneAdc.setThroughputInfo(adcThroughput);

			OBDtoADCObject adcInfo = new OBDtoADCObject();
			adcInfo.setDesciption(db.getString(rs, "ADC_DESCR"));
			adcInfo.setIndex(db.getInteger(rs, "ADC_INDEX"));
			adcInfo.setName(db.getString(rs, "ADC_NAME"));
			adcInfo.setStatus(db.getInteger(rs, "ADC_STATUS"));
			adcInfo.setVendor(db.getInteger(rs, "ADC_TYPE"));
			adcInfo.setCategory(OBJECT_CATEGORY_ADC);
			oneAdc.setObjectInfo(adcInfo);

			// group을 찾는다.
			groupFound = false;
			groupIndexTemp = db.getInteger(rs, "GR_INDEX");
			// group이 없으면, 새로 만든다.
			for (OBDtoDashboardAdcSummary gr : grouplist) {
				if (gr.getObjectInfo().getCategory().equals(OBJECT_CATEGORY_GROUP)) {
					if (gr.getObjectInfo().getIndex().equals(groupIndexTemp)) {
						groupFound = true;
						workGroup = gr; // 작업 그룹으로 지정
						break;
					}
				}
			}
			if (groupFound == false) // 없는 그룹이다.
			{
				// 새로 그룹을 만든다
				workGroup = new OBDtoDashboardAdcSummary(); // 작업 그룹을 새로 만듦
				// 빈 adcList를 붙인다.
				ArrayList<OBDtoDashboardAdcSummary> adcList = new ArrayList<OBDtoDashboardAdcSummary>();
				workGroup.setAdcList(adcList);
				// group info 저장, 해당없는 값은 null로 주는게 아니라 constructor 할당값을 유지 한다. 사실상 웹으로 null을
				// 내보내지 않는다.
				OBDtoADCObject info = new OBDtoADCObject();
				info.setIndex(groupIndexTemp);
				info.setName(db.getString(rs, "GR_NAME"));
				// info.setStatus(null);
				info.setCategory(OBJECT_CATEGORY_GROUP);
				// info.setVendor(null);
				info.setDesciption(db.getString(rs, "GR_DESCR"));
				workGroup.setObjectInfo(info);
				// connection/throughput도 나중에 계산하므로 빈 객체를 붙인다.
				OBDtoDataObj connection = new OBDtoDataObj();
				OBDtoDataObj throughput = new OBDtoDataObj();
				workGroup.setConnectionInfo(connection);
				workGroup.setThroughputInfo(throughput);
				// group list에 더한다.
				grouplist.add(workGroup);
			}

			if (workGroup != null) // flow상 workGroup이 null일 수는 없지만 확인사살 차원에서 조건을 붙였다.
			{
				// group 작업 끝(기존 그룹을 찾았거나 새로 만들었거나), adc를 workGroup에 붙인다.
				workGroup.getAdcList().add(oneAdc);
				// group connection 합에 새로 들어온 adc 것을 더한다.
				workGroup.getConnectionInfo()
						.setValue(workGroup.getConnectionInfo().getValue() + oneAdc.getConnectionInfo().getValue());
				workGroup.getThroughputInfo()
						.setValue(workGroup.getThroughputInfo().getValue() + oneAdc.getThroughputInfo().getValue());
			}
			// 전체 connection/throughput 계산
			head.getConnectionInfo()
					.setValue(head.getConnectionInfo().getValue() + oneAdc.getConnectionInfo().getValue());
			head.getThroughputInfo()
					.setValue(head.getThroughputInfo().getValue() + oneAdc.getThroughputInfo().getValue());
		}
		// 전체 통계 element를 list에 넣는다. 이것 말고는 모두 group element다. adcList는 각 그룹 하위에 멤버로 있다.
		head.setAdcList(grouplist);

		return head;
	}

	public void testGetAdcGroupSummary() {
		ArrayList<OBDtoAdcInfo> adcInfoList;
		OBDatabase db = new OBDatabase();
		try {
			adcInfoList = new OBAdcManagementImpl().getAdcInfoList(0);
			ArrayList<Integer> adcIndexList = new ArrayList<Integer>();
			for (OBDtoAdcInfo adcInfo : adcInfoList) {
				adcIndexList.add(adcInfo.getIndex());
			}
			db.openDB();
			OBDtoDashboardAdcSummary resu = getAdcGroupSummary(adcIndexList, db);

			System.out.println("TOTAL connection=" + resu.getConnectionInfo().getValue() + "; throughput="
					+ resu.getThroughputInfo().getValue());
			for (OBDtoDashboardAdcSummary gr : resu.getAdcList()) // group list
			{
				if (gr.getObjectInfo().getCategory().equals(1)) {
					System.out.println("GROUP name=" + gr.getObjectInfo().getName() + "; vendor="
							+ gr.getObjectInfo().getVendor() + "; connection=" + gr.getConnectionInfo().getValue()
							+ "; throughput=" + gr.getThroughputInfo().getValue());
					for (OBDtoDashboardAdcSummary adc : gr.getAdcList()) {
						System.out.println("\tADC name=" + adc.getObjectInfo().getName() + "; vendor="
								+ adc.getObjectInfo().getVendor() + "; connection=" + adc.getConnectionInfo().getValue()
								+ "; throughput=" + adc.getThroughputInfo().getValue());
					}
				} else if (gr.getObjectInfo().getCategory().equals(0)) {
					System.out.println("TOTAL" + "; connection=" + gr.getConnectionInfo().getValue() + "; throughput="
							+ gr.getThroughputInfo().getValue());
				}
			}
		} catch (Exception e) {
			db.closeDB();
			e.printStackTrace();
		}

		db.closeDB();
	}

	@Override
	public ArrayList<OBDtoDataObj> getAdcConnectionGraph(Integer accountIndex, OBDtoADCObject object, Date fromTime,
			Date toTime) throws OBException {
		if (accountIndex == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "accountIndex");
		}
		if (object == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "object");
		}
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			// account가 접근할 수 있는 ADC 중 지정한 object에 속하는 ADC index를 구한다.
			ArrayList<Integer> adcIndexList = getTargetAdcList(accountIndex, object.getCategory(), object.getIndex());
			ArrayList<OBDtoDataObj> result = new OBDashboardBase().getAdcConnectionGraph(adcIndexList, fromTime, toTime,
					db);
			db.closeDB();
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void testgetAdcConnectionGraph() {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			OBDtoADCObject object = new OBDtoADCObject(); // group, index = 1
			object.setCategory(OBJECT_CATEGORY_GROUP);
			object.setIndex(1);
			ArrayList<OBDtoDataObj> resList = getAdcConnectionGraph(0, // user account index
					object, // object
					null, OBDateTime.toDate(OBDateTime.toTimestamp("2013-03-29 02:01:01.000")));

			for (OBDtoDataObj res : resList) {
				System.out.println("occur_time=" + res.getOccurTime() + "; connection=" + res.getValue());
			}
		} catch (Exception e) {
			db.closeDB();
			e.printStackTrace();
			System.out.println("----------------------");
			System.out.println(e.getMessage());
		}
		db.closeDB();
	}

	@Override
	public ArrayList<OBDtoDataObj> getAdcThroughputGraph(Integer accountIndex, OBDtoADCObject object, Date fromTime,
			Date toTime) throws OBException {
		if (accountIndex == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "accountIndex");
		}
		if (object == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "object");
		}

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			// account가 접근할 수 있는 ADC 중 지정한 object에 속하는 ADC index를 구한다.
			ArrayList<Integer> adcIndexList = getTargetAdcList(accountIndex, object.getCategory(), object.getIndex());
			ArrayList<OBDtoDataObj> result = new OBDashboardBase().getAdcThroughputGraph(adcIndexList, fromTime, toTime,
					db);
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void testgetAdcThroughputGraph() {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			OBDtoADCObject object = new OBDtoADCObject(); // group, index = 1
			object.setCategory(OBJECT_CATEGORY_GROUP);
			object.setIndex(1);
			ArrayList<OBDtoDataObj> resList = getAdcThroughputGraph(0, // user account index
					object, // object
					null, OBDateTime.toDate(OBDateTime.toTimestamp("2013-03-29 02:01:01.000")));

			for (OBDtoDataObj res : resList) {
				System.out.println("occur_time=" + res.getOccurTime() + "; Throughput=" + res.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("----------------------");
			System.out.println(e.getMessage());
		} finally {
			db.closeDB();
		}
	}

	@Override
	public OBDtoDashboardVSSummary getVSSummary(Integer accountIndex, OBDtoADCObject object) throws OBException // TODO:
																												// DIFF
																												// 통계
																												// 테이블
																												// 필요
	{
		if (accountIndex == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "accountIndex");
		}
		if (object == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "object");
		}
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			// account, object에 해당하는 ADC의 index를 구한다.
			ArrayList<Integer> adcIndexList = getTargetAdcList(accountIndex, object.getCategory(), object.getIndex());
			OBDtoDashboardVSSummary result = new OBDashboardBase().getVSSummary(adcIndexList, VS_RECENTFAULT_DAYS, 0,
					db); // VS_RECENTFAULT_DAYS: 7일 이내 단절 VS 개수도 구한다.
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void testGetVSSummary() {
		OBDtoADCObject object = new OBDtoADCObject();
		object.setCategory(OBJECT_CATEGORY_TOTAL);
		object.setIndex(0);
		try {
			OBDtoDashboardVSSummary res = getVSSummary(0, object);
			System.out.println("  now  vs=" + res.getTotal().getValue() + "; avail=" + res.getAvail().getValue()
					+ "; unavail=" + res.getUnavail().getValue() + "; disable=" + res.getDisable().getValue()
					+ "; unavail less Ndays=" + res.getUnavailLessNDays().getValue() + "; unavail over Ndays="
					+ res.getUnavailOverNDays().getValue() + "\n  diff vs=" + res.getTotal().getDiff() + "; avail="
					+ res.getAvail().getDiff() + "; unavail=" + res.getUnavail().getDiff() + "; disable="
					+ res.getDisable().getDiff() + "; unavail less Ndays=" + res.getUnavailLessNDays().getDiff()
					+ "; unavail over Ndays=" + res.getUnavailOverNDays().getDiff());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public static void main(String[] args)
//	{
//		/*OBDashboardAdcImpl my = new OBDashboardAdcImpl();
//		//my.testGetAdcGroupSummary();
//		//my.testGetVSConnectionListTop10();
//		//my.testGetVSSummary();
//		//my.testgetAdcConnectionGraph();
//		//my.testgetAdcThroughputGraph();
//		//my.testGetTotalAdcTrafficGraph();
//		//my.testGetTotalAdcTrafficInfo();
//		//my.testGetFaultMonitoring();
//		//my.testGetSlbChangeMonitoring();
//		//my.testGetAdcTrafficListTop5();
//		OBDtoADCObject obj = new OBDtoADCObject();
//		obj.setCategory(2);
//		obj.setIndex(1);
//		try
//		{
//			ArrayList<OBDtoDashboardTraffic> list = my.getVSConnectionListTop10(1,obj);
//			System.out.println(list);
//		}
//		catch(OBException e)
//		{		
//			e.printStackTrace();
//		}
////		my.testGetVSConnectionGraphListTop5();
//*/	}
	@Override
	public ArrayList<OBDtoDashboardTraffic> getVSConnectionListTop10(Integer accountIndex, OBDtoADCObject object,
			OBDtoOrdering orderOption) throws OBException {
		if (accountIndex == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "accountIndex");
		}
		if (object == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "object");
		}

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			// account, object에 해당하는 ADC의 index를 구한다.
			ArrayList<Integer> adcIndexList = getTargetAdcList(accountIndex, object.getCategory(), object.getIndex());
			// 결과를 원치 않는 vs List를 구한다. //TODO
			ArrayList<OBDtoExcludeVip> excludeVipList = getExcludeVipList();
			ArrayList<OBDtoDashboardTraffic> result = new OBDashboardBase().getVSTrafficListTopN(adcIndexList,
					excludeVipList, VS_TRAFFIC_RANK_SELECTIVE, orderOption, db);
			db.closeDB();
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
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
	public ArrayList<OBDtoDataObj> getVirtualServerConnectionGraph(OBDtoDashboardTraffic object, Date fromTime,
			Date toTime) throws OBException {
		if (object == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "object");
		}
		ArrayList<OBDtoDataObj> result = null;
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			if (object.getVendor().equals(OBDefine.ADC_TYPE_ALTEON)) {
				result = new OBDashboardBase().getVirtualServiceConnectionGraph(object.getIndex(), fromTime, toTime,
						db);
			} else if (object.getVendor().equals(OBDefine.ADC_TYPE_F5)) {
				result = new OBDashboardBase().getVirtualServerConnectionGraph(object.getIndex(), fromTime, toTime, db);
			} else if (object.getVendor().equals(OBDefine.ADC_TYPE_PIOLINK_PAS)) {
				result = new OBDashboardBase().getVirtualServerConnectionGraph(object.getIndex(), fromTime, toTime, db);
			} else if (object.getVendor().equals(OBDefine.ADC_TYPE_PIOLINK_PASK)) {
				result = new OBDashboardBase().getVirtualServerConnectionGraph(object.getIndex(), fromTime, toTime, db);
			} else {
				result = new ArrayList<OBDtoDataObj>();
			}
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
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
	public ArrayList<OBDtoDataObj> getVirtualServerThroughputGraph(OBDtoDashboardTraffic object, Date fromTime,
			Date toTime) throws OBException {
		if (object == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "object");
		}
		ArrayList<OBDtoDataObj> result = null;
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			if (object.getVendor() != null) {
				if (object.getVendor().equals(OBDefine.ADC_TYPE_ALTEON)) {
					result = new OBDashboardBase().getVirtualServiceThroughputGraph(object.getIndex(), fromTime, toTime,
							db);
				} else if (object.getVendor().equals(OBDefine.ADC_TYPE_F5)) {
					result = new OBDashboardBase().getVirtualServerThroughputGraph(object.getIndex(), fromTime, toTime,
							db);
				} else if (object.getVendor().equals(OBDefine.ADC_TYPE_PIOLINK_PAS)) {
					result = new OBDashboardBase().getVirtualServerThroughputGraph(object.getIndex(), fromTime, toTime,
							db);
				} else if (object.getVendor().equals(OBDefine.ADC_TYPE_PIOLINK_PASK)) {
					result = new OBDashboardBase().getVirtualServerThroughputGraph(object.getIndex(), fromTime, toTime,
							db);
				}
			}
			if (result == null) {
				result = new ArrayList<OBDtoDataObj>(); // 해당 결과 없음
			}
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
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
	public OBDtoDashboardFaultStatus getFaultMonitoring(Integer accountIndex) throws OBException {
		// 기간 기본값 = 일주일
		Date fromTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.getDateWithDayOffset(-6, null)));
		Date toTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now()));
		return getFaultMonitoring(accountIndex, fromTime, toTime);
	}

	@Override
	public OBDtoDashboardFaultStatus getFaultMonitoring(Integer accountIndex, Date fromTime, Date toTime)
			throws OBException {
		if (accountIndex == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "accountIndex");
		}

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			// account, object에 해당하는 ADC의 index를 구한다.
			ArrayList<Integer> adcIndexList = getTargetAdcList(accountIndex, OBJECT_CATEGORY_TOTAL, null);
			OBDtoDashboardFaultStatus result = getFaultMonitoring(adcIndexList, fromTime, toTime, FAULT_LOG_RANK, db);
			db.closeDB();
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
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
	public OBDtoDashboardFaultStatus getDynamicFaultMonitoring(Integer accountIndex, Date fromTime, Date toTime)
			throws OBException {
		if (accountIndex == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "accountIndex");
		}

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			// account, object에 해당하는 ADC의 index를 구한다.
			ArrayList<Integer> adcIndexList = getTargetAdcList(accountIndex, OBJECT_CATEGORY_TOTAL, null);
			OBDtoDashboardFaultStatus result = getFaultMonitoring(adcIndexList, fromTime, toTime,
					DYNAMIC_FAULT_LOG_RANK, db);
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public OBDtoDashboardFaultStatus getFaultMonitoring(ArrayList<Integer> adcIndexList, Date fromTime, Date toTime,
			int logNum, OBDatabase db) throws Exception {
		if (fromTime == null || toTime == null) // 날짜 둘 중 하나라도 null이면 일자 설정이 안 된 것으로 보고 최근 일주일을 잡는다.
		{
			fromTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.getDateWithDayOffset(-6, null)));
			toTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now()));
		}
		String sqlText = "";
		String adcIndexClause = "";
		String logNumClause = "";
		String sqlFromTime = OBParser.sqlString(OBDateTime.toString(new Timestamp(fromTime.getTime())));
		String sqlToTime = OBParser.sqlString(OBDateTime.toString(new Timestamp(toTime.getTime())));

		adcIndexClause = OBParser.convertList2CommaString(adcIndexList);
		if (adcIndexClause.isEmpty()) {
			adcIndexClause = "-1";
		}
		// 주어진 기간내의 각 일별 장애수를 구한다.
		// 장애를 카운트하면, 새 장애가 생긴 날짜만 뽑힌다. 구간내 모든 날짜를 먼저 뽑고 거기에 LEFT JOIN하면 데이터가 없는 날도 뽑을 수
		// 있다.
		// 이러한 QUERY는 성능에 영향이 거의 없고, 이 QUERY는 성능 부하가 없다.
		// 장애가 일어나지 않은 날의 count는 null인데, db.getInteger()에서 null은 0으로 바뀌어 나온다.
		sqlText = String.format(
				" SELECT DAY._DATE, FAULT.COUNT                                           \n"
						+ " FROM (SELECT DATE_TRUNC('DAY', %s::DATE + i) AS _DATE                   \n"
						+ "    FROM GENERATE_SERIES(0, %s::DATE - %s::DATE) AS t(i)                 \n"
						+ " ) DAY                                                                   \n"
						+ " LEFT JOIN (SELECT DATE_TRUNC('DAY', OCCUR_TIME) AS _DATE, COUNT(*)      \n"
						+ "            FROM LOG_ADC_FAULT                                           \n"
						+ "            WHERE OCCUR_TIME >= %s AND OCCUR_TIME <= %s                  \n"
						+ "                AND ADC_INDEX IN (%s)                                    \n"
						+ "            GROUP BY _DATE                                               \n"
						+ " ) FAULT                                                                 \n"
						+ " ON DAY._DATE = FAULT._DATE                                              \n"
						+ " ORDER BY DAY._DATE ",
				sqlFromTime, sqlToTime, sqlFromTime, sqlFromTime, sqlToTime, adcIndexClause);

		ResultSet rs = db.executeQuery(sqlText);

		OBDtoDashboardFaultStatus result = new OBDtoDashboardFaultStatus();
		ArrayList<OBDtoDataObj> newFaultDailyCountList = new ArrayList<OBDtoDataObj>();

		while (rs.next()) {
			OBDtoDataObj newFaultDailyCount = new OBDtoDataObj();
			newFaultDailyCount.setOccurTime(db.getTimestamp(rs, "_DATE"));
			newFaultDailyCount.setValue(db.getLong(rs, "COUNT"));
			newFaultDailyCountList.add(newFaultDailyCount);
		}
		result.setCountHistory(newFaultDailyCountList); // 여기까지 각 일별 새 장애 집계가 끝났다.

		// 최근 logNum개의 장애 내역을 가져온다.
		if (logNum > 0) // log 개수 제한이 있는 조회
		{
			logNumClause = "OFFSET 0 LIMIT " + logNum;
		} else {
			logNumClause = "";
		}
		sqlText = String.format(
				" SELECT LEVEL, OCCUR_TIME, UPDATE_TIME, TYPE,      \n"
						+ "     ADC_INDEX, ADC_NAME, VS_INDEX, EVENT, STATUS  \n"
						+ " FROM LOG_ADC_FAULT                                \n"
						+ " WHERE OCCUR_TIME >= %s AND OCCUR_TIME <= %s       \n"
						+ "     AND ADC_INDEX IN (%s)                         \n"
						+ " ORDER BY OCCUR_TIME DESC                          \n" + " %s ",
				sqlFromTime, sqlToTime, adcIndexClause, logNumClause);

		rs = db.executeQuery(sqlText);
		ArrayList<OBDtoAdcSystemLog> newFaultLogList = new ArrayList<OBDtoAdcSystemLog>();
		while (rs.next()) {
			OBDtoAdcSystemLog newFaultLog = new OBDtoAdcSystemLog();
			newFaultLog.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
			newFaultLog.setAdcName(db.getString(rs, "ADC_NAME"));
			newFaultLog.setEvent(db.getString(rs, "EVENT"));
			newFaultLog.setLogLevel(db.getInteger(rs, "LEVEL"));
			newFaultLog.setLogType(db.getInteger(rs, "TYPE"));
			newFaultLog.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
			newFaultLog.setStatus(db.getInteger(rs, "STATUS"));
			if (newFaultLog.getStatus() == OBDefineFault.STATUS_UNSOLVED) // 안 끝난 장애는 finish time이 null
			{
				newFaultLog.setFinishTime(null);
			} else {
				newFaultLog.setFinishTime(db.getTimestamp(rs, "UPDATE_TIME"));
			}

			newFaultLog.setVsIndex(db.getString(rs, "VS_INDEX"));
			newFaultLogList.add(newFaultLog);
		}
		result.setLogList(newFaultLogList); // 로그 리스트를 결과set에 붙인다.
		return result;
	}

	public void testGetFaultMonitoring() {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			OBDtoDashboardFaultStatus result = getFaultMonitoring(0);
			for (OBDtoDataObj res : result.getCountHistory()) {
				System.out.println("time=" + res.getOccurTime() + "; new fault count =" + res.getValue());
			}
			for (OBDtoAdcSystemLog res : result.getLogList()) {
				System.out.println("time=" + res.getOccurTime() + "; 장애 event =" + res.getEvent());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.closeDB();
		}
	}

	@Override
	public OBDtoDashboardSlbChangeStatus getSlbChangeMonitoring(Integer accountIndex) throws OBException {
		// 일주일 날짜를 구한다.
		Date toTime = OBDateTime.toTimestamp(OBDateTime.now());
		Date fromTime = OBDateTime.toTimestamp(OBDateTime.getDateWithDayOffset(ONE_WEEK_START_OFFSET, null));
		return getSlbChangeMonitoring(accountIndex, fromTime, toTime);
	}

	@Override
	public OBDtoDashboardSlbChangeStatus getSlbChangeMonitoring(Integer accountIndex, Date fromTime, Date toTime)
			throws OBException {
		if (accountIndex == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "accountIndex");
		}

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			// account, object에 해당하는 ADC의 index를 구한다.
			ArrayList<Integer> adcIndexList = getTargetAdcList(accountIndex, OBJECT_CATEGORY_TOTAL, null);
			OBDtoDashboardSlbChangeStatus result = getSlbChangeMonitoring(adcIndexList, fromTime, toTime,
					CONFIG_LOG_RANK, db);
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
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
	public OBDtoDashboardSlbChangeStatus getDynamicSlbChangeMonitoring(Integer accountIndex, Date fromTime, Date toTime)
			throws OBException {
		if (accountIndex == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "accountIndex");
		}
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			// account, object에 해당하는 ADC의 index를 구한다.
			ArrayList<Integer> adcIndexList = getTargetAdcList(accountIndex, OBJECT_CATEGORY_TOTAL, null);
			OBDtoDashboardSlbChangeStatus result = getSlbChangeMonitoring(adcIndexList, fromTime, toTime,
					DYNAMIC_CONFIG_LOG_RANK, db);
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public OBDtoDashboardSlbChangeStatus getSlbChangeMonitoring(ArrayList<Integer> adcIndexList, Date fromTime,
			Date toTime, int logNum, OBDatabase db) throws Exception {
		if (fromTime == null || toTime == null) // 둘 중 한쪽 날짜가 null이면 날짜 설정이 안된 것으로 보고 최근 7일 구간으로 설정한다.
		{
			fromTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.getDateWithDayOffset(-6, null)));
			toTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now()));
		}
		String sqlText = "";
		String adcIndexClause = "";
		String logNumClause = "";
		String sqlFromTime = OBParser.sqlString(OBDateTime.toString(new Timestamp(fromTime.getTime())));
		String sqlToTime = OBParser.sqlString(OBDateTime.toString(new Timestamp(toTime.getTime())));

		adcIndexClause = OBParser.convertList2CommaString(adcIndexList);
		if (adcIndexClause.isEmpty()) {
			adcIndexClause = "-1";
		}
		// 주어진 기간내의 각 일별 설정회수 합을 구한다.
		// 카운트하면, 설정이 생긴 날짜만 뽑힌다. 구간내 모든 날짜를 먼저 뽑고 거기에 LEFT JOIN하면 데이터가 없는 날도 뽑을 수 있다.
		// 이러한 QUERY는 성능에 영향이 거의 없고, 이 QUERY는 성능 부하가 없다.
		// 설정이 없는 날의 count는 null인데, db.getInteger()에서 null은 0으로 바뀌어 나온다.
		sqlText = String.format(
				" SELECT DAY._DATE, HISTORY.COUNT                                         \n"
						+ " FROM (SELECT DATE_TRUNC('DAY', %s::DATE + i) AS _DATE                   \n"
						+ "    FROM GENERATE_SERIES(0, %s::DATE - %s::DATE) AS t(i)                 \n"
						+ " ) DAY                                                                   \n"
						+ " LEFT JOIN (SELECT DATE_TRUNC('DAY', OCCUR_TIME) AS _DATE, COUNT(*)      \n"
						+ "            FROM LOG_CONFIG_HISTORY                                      \n"
						+ "            WHERE OCCUR_TIME >= %s AND OCCUR_TIME <= %s                  \n"
						+ "                AND ADC_INDEX IN (%s)                                    \n"
						+ "            GROUP BY _DATE                                               \n"
						+ " ) HISTORY                                                               \n"
						+ " ON DAY._DATE = HISTORY._DATE                                            \n"
						+ " ORDER BY DAY._DATE ",
				sqlFromTime, sqlToTime, sqlFromTime, sqlFromTime, sqlToTime, adcIndexClause);

		ResultSet rs = db.executeQuery(sqlText);

		OBDtoDashboardSlbChangeStatus result = new OBDtoDashboardSlbChangeStatus();
		ArrayList<OBDtoDataObj> configDailyCountList = new ArrayList<OBDtoDataObj>();

		while (rs.next()) {
			OBDtoDataObj configDailyCount = new OBDtoDataObj();
			configDailyCount.setOccurTime(db.getTimestamp(rs, "_DATE"));
			configDailyCount.setValue(db.getLong(rs, "COUNT"));
			configDailyCountList.add(configDailyCount);
		}
		result.setChangeHistory(configDailyCountList); // 여기까지 각 일별 새 장애 집계가 끝났다.

		// 최근 logNum개의 설정 내역을 가져온다.
		if (logNum > 0) // log 개수 제한이 있는 조회
		{
			logNumClause = "OFFSET 0 LIMIT " + logNum;
		} else {
			logNumClause = "";
		}
		sqlText = String.format(
				" SELECT LOG_TYPE, ACCESS_TYPE, ACCNT_INDEX, ACCNT_NAME, ADC_INDEX, VS_INDEX,   \n"
						+ "     VS_NAME, VS_IP, OBJECT_TYPE, OCCUR_TIME, SUMMARY, MNG_ADC.NAME ADC_NAME   \n"
						+ " FROM LOG_CONFIG_HISTORY                                                       \n"
						+ " LEFT JOIN MNG_ADC                                                             \n"
						+ "     ON ADC_INDEX = MNG_ADC.INDEX                                              \n"
						+ " WHERE OCCUR_TIME >= %s AND OCCUR_TIME <= %s                                   \n"
						+ "     AND ADC_INDEX IN (%s)                                                     \n"
						+ " ORDER BY OCCUR_TIME DESC                                                      \n" + " %s ",
				sqlFromTime, sqlToTime, adcIndexClause, logNumClause);

		rs = db.executeQuery(sqlText);
		ArrayList<OBDtoAdcConfigHistory> configLogList = new ArrayList<OBDtoAdcConfigHistory>();
		String adcName;
		while (rs.next()) {
			OBDtoAdcConfigHistory configLog = new OBDtoAdcConfigHistory();
			adcName = db.getString(rs, "ADC_NAME");
			if (adcName == null) {
				adcName = "";
			}
			configLog.setAdcName(adcName);
			configLog.setUserType(db.getInteger(rs, "LOG_TYPE"));
			configLog.setAccessType(db.getInteger(rs, "ACCESS_TYPE"));
			configLog.setAdcType(-1); // 필요한 데이터가 아닌데 같은 테이블에 있지도 않아서 안 구함
			configLog.setAccountIndex(db.getInteger(rs, "ACCNT_INDEX"));
			configLog.setAccountName(db.getString(rs, "ACCNT_NAME"));
			configLog.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
			configLog.setVsIndex(db.getString(rs, "VS_INDEX"));
			configLog.setVsName(db.getString(rs, "VS_NAME"));
			configLog.setVsIp(db.getString(rs, "VS_IP"));
			configLog.setVsStatus(null); // 필요한 데이터가 아닌데 같은 테이블에 있지도 않아서 안 구함
			configLog.setVsAlive(null); // 필요한 데이터가 아닌데 같은 테이블에 있지도 않아서 안 구함
			configLog.setObjectType(db.getInteger(rs, "OBJECT_TYPE"));
			configLog.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
			configLog.setSummary(db.getString(rs, "SUMMARY"));

			configLogList.add(configLog);
		}
		result.setLogList(configLogList); // 로그 리스트를 결과set에 붙인다.
		return result;
	}

	public void testGetSlbChangeMonitoring() {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			OBDtoDashboardSlbChangeStatus result = getSlbChangeMonitoring(0);
			for (OBDtoDataObj res : result.getChangeHistory()) {
				System.out.println("time=" + res.getOccurTime() + "; config count =" + res.getValue());
			}
			for (OBDtoAdcConfigHistory res : result.getLogList()) {
				System.out.println("time=" + res.getOccurTime() + "; summary=" + res.getSummary());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.closeDB();
		}
	}

	@Override
	public ArrayList<OBDtoDataObj> getTotalAdcTrafficGraph(Integer accountIndex) throws OBException {
		// 날짜 기본 값 - 24시간
		Date fromTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.getDateWithHourOffset(-24, null)));
		Date toTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now()));
		return getTotalAdcTrafficGraph(accountIndex, fromTime, toTime);
	}

	@Override
	public ArrayList<OBDtoDataObj> getTotalAdcTrafficGraph(Integer accountIndex, Date fromTime, Date toTime)
			throws OBException {
		if (accountIndex == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "accountIndex");
		}
		// 이 함수는 지정한 adc들에 대한 트래픽을 뽑는 OBDashboardBase().getAdcThroughputGraph()에 전체 adc
		// list를 넘겨서 뽑을 수 있다.
		// 동작으로 보면 getAdcThroughputGraph()을 ADC목록만 바꿔서 호출한 셈이므로 getAdcThroughputGraph()를
		// 변형해서 쓴다.
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			// account가 접근할 수 있는 모든 ADC index를 구한다.
			ArrayList<Integer> adcIndexList = getTargetAdcList(accountIndex, OBJECT_CATEGORY_TOTAL, null);
			ArrayList<OBDtoDataObj> result = new OBDashboardBase().getAdcThroughputGraph(adcIndexList, fromTime, toTime,
					db);
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void testGetTotalAdcTrafficGraph() {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			ArrayList<OBDtoDataObj> resList = getTotalAdcTrafficGraph(0); // 0:internal super user account index

			for (OBDtoDataObj res : resList) {
				System.out.println("occur_time=" + res.getOccurTime() + "; Throughput=" + res.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("----------------------");
			System.out.println(e.getMessage());
		} finally {
			db.closeDB();
		}
	}

	@Override
	public OBDtoDashboardTrafficStatus getTotalAdcTrafficInfo(Integer accountIndex) throws OBException {
		if (accountIndex == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "accountIndex");
		}
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			// account에 소속된 모든 ADC index를 구한다.
			ArrayList<Integer> adcIndexList = getTargetAdcList(accountIndex, OBJECT_CATEGORY_TOTAL, null);
			OBDtoDashboardTrafficStatus result = getTotalAdcTrafficInfo(adcIndexList, db);
			db.closeDB();
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public OBDtoDashboardTrafficStatus getTotalAdcTrafficInfo(ArrayList<Integer> adcIndexList, OBDatabase db)
			throws Exception {
		String adcIndexClause = "";
		adcIndexClause = OBParser.convertList2CommaString(adcIndexList);
		if (adcIndexClause.isEmpty()) {
			adcIndexClause = "-1";
		}

		// config sync 주기를 구한다.
		Integer adcResourceUpdateInterval = new OBEnvManagementImpl().getAdcSyncInterval(db);// seconds

		Date todayRecent = OBDateTime.toDate(
				OBDateTime.toTimestamp(OBDateTime.getDateWithHourOffset((-1) * adcResourceUpdateInterval, null)));
		Date yesterdayRecent = OBDateTime
				.toDate(OBDateTime.getTimestampWithDayOffset(new Timestamp(todayRecent.getTime()), -1));

		String todayRecentStr = OBParser.sqlString(OBDateTime.toString(new Timestamp(todayRecent.getTime())));
		String yesterdayRecentStr = OBParser.sqlString(OBDateTime.toString(new Timestamp(yesterdayRecent.getTime())));

		String sqlText = String.format(
				" SELECT 'TODAY' AS TYPE, MIN(BLOCK_SUM_BPS), MAX(BLOCK_SUM_BPS), AVG(BLOCK_SUM_BPS)     \n"
						+ " FROM                                                                                   \n"
						+ " (                                                                                      \n"
						+ "     SELECT BLOCK_TIME, SUM(BPS_IN) BLOCK_SUM_BPS                                       \n"
						+ "     FROM LOG_ADC_PERF_STATS                                                      \n"
						+ "     WHERE BLOCK_TIME >= CURRENT_DATE AND ADC_INDEX IN (%s)                             \n"
						+ "     GROUP BY BLOCK_TIME                                                                \n"
						+ " ) T -- 오늘 각 블록당 전체 ADC BPS                                                          \n"
						+ " UNION                                                                                  \n"
						+ " SELECT 'YESTERDAY' AS TYPE, MIN(BLOCK_SUM_BPS), MAX(BLOCK_SUM_BPS), AVG(BLOCK_SUM_BPS) \n"
						+ " FROM                                                                                   \n"
						+ " (                                                                                      \n"
						+ "     SELECT BLOCK_TIME, SUM(BPS_IN) BLOCK_SUM_BPS                                       \n"
						+ "     FROM LOG_ADC_PERF_STATS                                                      \n"
						+ "     WHERE BLOCK_TIME >= CURRENT_DATE-1 AND BLOCK_TIME < CURRENT_DATE                   \n"
						+ "         AND ADC_INDEX IN (%s)                                                          \n"
						+ "     GROUP BY BLOCK_TIME                                                                \n"
						+ " ) Y -- 어제 각 블록당 전체 ADC BPS                                                           \n",
				adcIndexClause, adcIndexClause);
		ResultSet rs = db.executeQuery(sqlText);
		// System.out.println("sqlText=" + sqlText);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText=" + sqlText);

		OBDtoDashboardTrafficStatus res = new OBDtoDashboardTrafficStatus();
		OBDtoDataObj currBps = new OBDtoDataObj();
		OBDtoDataObj prevBps = new OBDtoDataObj(); // 어제 동 시간대 block의 전체 bps 트래픽량
		OBDtoDataObj currMax = new OBDtoDataObj();
		OBDtoDataObj prevMax = new OBDtoDataObj();
		OBDtoDataObj curMin = new OBDtoDataObj();
		OBDtoDataObj prevMin = new OBDtoDataObj();
		OBDtoDataObj currAvg = new OBDtoDataObj();
		OBDtoDataObj prevAvg = new OBDtoDataObj();

		String type = "";
		while (rs.next()) {
			type = db.getString(rs, "TYPE");
			if (type.equals("TODAY")) {
				Long value = db.getLong(rs, "MAX");
				if (value.longValue() < 0)
					value = 0L;
				currMax.setValue(value);

				value = db.getLong(rs, "MIN");
				if (value.longValue() < 0)
					value = 0L;
				curMin.setValue(value);
				value = db.getLong(rs, "AVG");

				if (value.longValue() < 0)
					value = 0L;
				currAvg.setValue(value);
			} else if (type.equals("YESTERDAY")) {
				Long value = db.getLong(rs, "MAX");
				if (value.longValue() < 0)
					value = 0L;
				prevMax.setValue(value);

				value = db.getLong(rs, "MIN");
				if (value.longValue() < 0)
					value = 0L;
				prevMin.setValue(value);

				value = db.getLong(rs, "AVG");
				if (value.longValue() < 0)
					value = 0L;
				prevAvg.setValue(value);
			}
		}

		sqlText = String.format(" SELECT 'TODAY' AS TYPE, SUM(BPS_IN) BLOCK_SUM_BPS        \n"
				+ " FROM LOG_ADC_PERF_STATS                            \n"
				+ " WHERE BLOCK_TIME =                                       \n"
				+ "     ( SELECT MAX(BLOCK_TIME)                             \n" + // ** 최신것으로 뽑으므로 MAX
				"       FROM LOG_ADC_PERF_STATS                      \n"
				+ "       WHERE BLOCK_TIME >= %s                             \n"
				+ "     )                                                    \n"
				+ "     AND ADC_INDEX IN (%s)                                \n"
				+ " UNION                                                    \n"
				+ " SELECT 'YESTERDAY' AS TYPE, SUM(BPS_IN) BLOCK_SUM_BPS    \n"
				+ " FROM LOG_ADC_PERF_STATS                            \n"
				+ " WHERE BLOCK_TIME =                                       \n"
				+ "     ( SELECT MIN(BLOCK_TIME)                             \n" + // ** MAX가 아니고 MIN 인 것에 주의!
				"       FROM LOG_ADC_PERF_STATS                      \n"
				+ "       WHERE BLOCK_TIME >= %s                             \n"
				+ "     )                                                    \n"
				+ "     AND ADC_INDEX IN (%s)                                \n", todayRecentStr, adcIndexClause,
				yesterdayRecentStr, adcIndexClause);
		// System.out.println("sqlText=" + sqlText);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText=" + sqlText);
		rs = db.executeQuery(sqlText);
		type = "";
		while (rs.next()) {
			type = db.getString(rs, "TYPE");
			if (type.equals("TODAY")) {
				Long value = db.getLong(rs, "BLOCK_SUM_BPS");
				if (value.longValue() < 0)
					value = 0L;
				currBps.setValue(value);
			} else if (type.equals("YESTERDAY")) {
				Long value = db.getLong(rs, "BLOCK_SUM_BPS");
				if (value.longValue() < 0)
					value = 0L;
				prevBps.setValue(value);
			}
		}
		res.setCurrTotal(currBps);
		res.setPrevTotal(prevBps);
		res.setCurrMax(currMax);
		res.setPrevMax(prevMax);
		res.setCurMin(curMin);
		res.setPrevMin(prevMin);
		res.setCurrAvg(currAvg);
		res.setPrevAvg(prevAvg);

		return res;
	}

	public void testGetTotalAdcTrafficInfo() {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			OBDtoDashboardTrafficStatus result = getTotalAdcTrafficInfo(0); // user account index
			System.out.println(" current bps=" + result.getCurrTotal().getValue() + "; current min="
					+ result.getCurMin().getValue() + "; current max=" + result.getCurrMax().getValue()
					+ "; current avg=" + result.getCurrAvg().getValue() + "\n previous bps="
					+ result.getPrevTotal().getValue() + "; previous min=" + result.getPrevMin().getValue()
					+ "; previous max=" + result.getPrevMax().getValue() + "; previous avg="
					+ result.getPrevAvg().getValue());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("----------------------");
			System.out.println(e.getMessage());
		} finally {
			db.closeDB();
		}
	}

	@Override
	public ArrayList<OBDtoDashboardTraffic> getAdcTrafficListTop5(Integer accountIndex) throws OBException {
		if (accountIndex == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "accountIndex");
		}
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			// account, object에 해당하는 ADC의 index를 구한다.
			ArrayList<Integer> adcIndexList = getTargetAdcList(accountIndex, OBJECT_CATEGORY_TOTAL, null);
			ArrayList<OBDtoDashboardTraffic> result = new OBDashboardBase().getAdcTrafficListTopN(adcIndexList,
					ADC_TRAFFIC_RANK, db);
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
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
	public ArrayList<OBDtoDashboardTraffic> getAdcTrafficListTop10(Integer accountIndex) throws OBException {
		if (accountIndex == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "accountIndex");
		}
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			// account, object에 해당하는 ADC의 index를 구한다.
			ArrayList<Integer> adcIndexList = getTargetAdcList(accountIndex, OBJECT_CATEGORY_TOTAL, null);
			ArrayList<OBDtoDashboardTraffic> result = new OBDashboardBase().getAdcTrafficListTopN(adcIndexList,
					DYNAMIC_ADC_TRAFFIC_RANK, db);
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void testGetAdcTrafficListTop5() {
		ArrayList<OBDtoAdcInfo> adcInfoList;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			adcInfoList = new OBAdcManagementImpl().getAdcInfoList(0);
			ArrayList<Integer> adcIndexList = new ArrayList<Integer>();
			for (OBDtoAdcInfo adcInfo : adcInfoList) {
				adcIndexList.add(adcInfo.getIndex());
			}
			ArrayList<OBDtoDashboardTraffic> resList = getAdcTrafficListTop5(0); // user account index

			for (OBDtoDashboardTraffic res : resList) {
				System.out.println(" name=" + res.getNameIp() + "; type=" + res.getVendor() + "; status="
						+ res.getStatus() + "; connection=" + res.getConnection().getValue() + "; throughput="
						+ res.getThroughput().getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.closeDB();
		}
	}

	@Override
	public ArrayList<OBDtoDashboardVSConnHistory> getVSConnectionGraphListTop5(Integer accountIndex)
			throws OBException {
		// 날짜 기본 값 - 24시간
		Date fromTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.getDateWithHourOffset(-24, null)));
		Date toTime = OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now()));

		return getVSConnectionGraphListTop5(accountIndex, fromTime, toTime);
	}

	@Override
	public ArrayList<OBDtoDashboardVSConnHistory> getVSConnectionGraphListTop5(Integer accountIndex, Date fromTime,
			Date toTime) throws OBException {
		if (accountIndex == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "accountIndex");
		}
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			// account가 접근 가능한 모든 ADC의 index를 구한다.
			ArrayList<Integer> adcIndexList = new OBAccountImpl().getInvolvedAdcList(accountIndex);
			ArrayList<OBDtoDashboardVSConnHistory> result = new OBDashboardBase()
					.getVSConnectionGraphListTopN(adcIndexList, VS_TRAFFIC_RANK, fromTime, toTime, db);
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void testGetVSConnectionGraphListTop5() {
		ArrayList<OBDtoAdcInfo> adcInfoList;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			adcInfoList = new OBAdcManagementImpl().getAdcInfoList(0);
			ArrayList<Integer> adcIndexList = new ArrayList<Integer>();
			for (OBDtoAdcInfo adcInfo : adcInfoList) {
				adcIndexList.add(adcInfo.getIndex());
			}

			ArrayList<OBDtoDashboardVSConnHistory> resList = getVSConnectionGraphListTop5(0);
			int i = 0;
			for (OBDtoDashboardVSConnHistory res : resList) {
				System.out.println(" vs ipaddress=" + res.getVsIPAddress() + "; vs port=" + res.getPort()
						+ "; adc ipaddress=" + res.getAdcIPAddress() + "; adc name=" + res.getAdcName());

				i = 0;
				for (OBDtoDataObj conn : res.getConnection()) {
					if (i++ < 5) // 5개만 찍는다.
						System.out.println(" - " + conn.getOccurTime() + "; " + conn.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.closeDB();
		}
	}

	@Override
	/**
	 * getVSConnectionListTop10(Integer, OBDtoADCObject)와 같은 데이터를 뽑는다. 다만, 다음 두 가지만
	 * 조건만 다르다. - 특정 object(group, adc) 선택 없이 계정에 해당하는 모든 virtual server/virtual
	 * service에서 뽑는다. - 상위 5위까지 뽑는다.
	 */
	public ArrayList<OBDtoDashboardTraffic> getVSTrafficListTop5(Integer accountIndex) throws OBException {
		if (accountIndex == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "accountIndex");
		}
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			// account에 해당하는 ADC의 index를 구한다.
			ArrayList<Integer> adcIndexList = getTargetAdcList(accountIndex, OBJECT_CATEGORY_TOTAL, null);
			// 결과를 원치 않는 vs List를 구한다. //TODO
			ArrayList<OBDtoExcludeVip> excludeVipList = getExcludeVipList();
			OBDtoOrdering orderOption = new OBDtoOrdering();
			ArrayList<OBDtoDashboardTraffic> result = new OBDashboardBase().getVSTrafficListTopN(adcIndexList,
					excludeVipList, VS_TRAFFIC_RANK, orderOption, db);
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void testGetVSTrafficListTop5() {
		OBDatabase db = new OBDatabase();
		OBDtoADCObject object = new OBDtoADCObject();
		object.setCategory(OBJECT_CATEGORY_TOTAL);
		try {
			db.openDB();
			ArrayList<OBDtoDashboardTraffic> resList = getVSTrafficListTop5(0);
			int i = 0;
			String vendor = "";
			for (OBDtoDashboardTraffic res : resList) {
				i++;
				if (res.getVendor().equals(OBDefine.ADC_TYPE_ALTEON)) {
					vendor = "ALTEON";
				} else if (res.getVendor().equals(OBDefine.ADC_TYPE_F5)) {
					vendor = "F5";
				} else if (res.getVendor().equals(OBDefine.ADC_TYPE_PIOLINK_PAS)) {
					vendor = "PAS";
				} else if (res.getVendor().equals(OBDefine.ADC_TYPE_PIOLINK_PASK)) {
					vendor = "PAS";
				}
				System.out.println(i + ") nameip =" + res.getNameIp() + "; port=" + res.getPort() + "; satus="
						+ res.getStatus() + "; vendor=" + vendor + "; connection=" + res.getConnection().getValue()
						+ "; throughput=" + res.getThroughput().getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.closeDB();
		}
	}
//	public static void main(String[] args)
//	{
//		OBDashboardAdcImpl my = new OBDashboardAdcImpl();
//		//my.testGetAdcGroupSummary();
//		//my.testGetVSConnectionListTop10();
//		//my.testGetVSSummary();
//		//my.testgetAdcConnectionGraph();
//		//my.testgetAdcThroughputGraph();
//		//my.testGetTotalAdcTrafficGraph();
//		//my.testGetTotalAdcTrafficInfo();
//		//my.testGetFaultMonitoring();
//		//my.testGetSlbChangeMonitoring();
//		//my.testGetAdcTrafficListTop5();
//		my.testGetVSTrafficListTop5();
//		my.testGetVSConnectionGraphListTop5();
//	}
}
