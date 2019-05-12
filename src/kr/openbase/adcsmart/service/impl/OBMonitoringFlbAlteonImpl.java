package kr.openbase.adcsmart.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.OBMonitoringFlb;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoInterfaceSummary;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.flb.OBDtoFlbFilterInfo;
import kr.openbase.adcsmart.service.dto.flb.OBDtoFlbGroupMonitorInfo;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBMonitoringFlbAlteonImpl implements OBMonitoringFlb {
	@Override
	public ArrayList<OBDtoFlbGroupMonitorInfo> getFlbGroupMonitorInfo(int adcIndex) throws OBException {
		ArrayList<OBDtoFlbGroupMonitorInfo> flbGroups = new ArrayList<OBDtoFlbGroupMonitorInfo>(); // 결과값

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			flbGroups = new OBVServerDB().getFlbGroupMonitorInfo(adcIndex, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			OBException e1 = new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			throw e1;
		} finally {
			if (db != null)
				db.closeDB();
		}
		return flbGroups;
	}

	@Override
	public void setFlbGroupMonitorInfo(Integer adcIndex, ArrayList<String> selectedGroupIndexList) throws OBException {
		new OBVServerDB().setFlbGroupMonitorOn(adcIndex, selectedGroupIndexList);
	}

//	private int               adcIndex;  //ADC index
//	private String            dbIndex;   //ADCsmart에서 index
//	private String            filterId;  //alteon에서 filter index
//	private int               state;     //filter enable? 1:yes, 0:disable
//	private String            name;      //filter name
//	private String            srcIP;
//	private String            srcMask;
//	private String            dstIP;
//	private String            dstMask;
//	private int               protocol;
//	private int               srcPort;
//	private int               dstPort;
//	private int               action;
//	private String            group;
//	private int               redirection;
	@Override
	public Integer getFilterCount(int adcIndex, OBDtoSearch condition) throws OBException // TODO
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%s", adcIndex));
		String sqlText = "";
		String sqlSearch = "";
		int result = 0;

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			if (condition != null && condition.getSearchKey() != null && !condition.getSearchKey().isEmpty()) { // physical
																												// port로
																												// filter조회
				sqlSearch = String.format(
						" INDEX IN ( SELECT FILTER_INDEX FROM TMP_FLB_PORT_FILTER_MAP "
								+ "     WHERE ADC_INDEX = %d AND PORT_ALTEON_ID = %s )          ",
						adcIndex, condition.getSearchKey()); // physical port로 검색한다. integer.
			} else {
				sqlSearch = " TRUE ";
			}

			sqlText = String.format(" SELECT COUNT(INDEX) FROM TMP_FLB_FILTER  WHERE ADC_INDEX = %d AND %s ", adcIndex,
					sqlSearch);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				result = db.getInteger(rs, "COUNT");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", result));
		return result;
	}

	@Override
	public ArrayList<OBDtoFlbFilterInfo> getFilterInfo(int adcIndex, OBDtoSearch condition, OBDtoOrdering orderOption)
			throws OBException {
		ArrayList<OBDtoFlbFilterInfo> filters = new ArrayList<OBDtoFlbFilterInfo>(); // 결과값

		OBDatabase db = new OBDatabase();
		String sqlText = "";
		String sqlSearch = "";
		int offset = 0;
		int limit = 0;
		String sqlLimit = "";
		try {
			db.openDB();

			if (condition.getBeginIndex() != null) {
				offset = condition.getBeginIndex().intValue();
			}
			if (condition.getEndIndex() != null) {
				limit = Math.abs(condition.getEndIndex().intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}
			if (condition != null && condition.getSearchKey() != null && !condition.getSearchKey().isEmpty()) { // physical
																												// port로
																												// filter조회
				sqlSearch = String.format(
						" INDEX IN ( SELECT FILTER_INDEX FROM TMP_FLB_PORT_FILTER_MAP "
								+ "     WHERE ADC_INDEX = %d AND PORT_ALTEON_ID = %s )          ",
						adcIndex, condition.getSearchKey()); // physical port로 검색한다. integer.
			} else {
				sqlSearch = " TRUE ";
			}
			sqlText = String.format(" SELECT * FROM TMP_FLB_FILTER  WHERE ADC_INDEX = %d AND %s ", adcIndex, sqlSearch);

			sqlText += getFilterInfoOrderType(orderOption);
			sqlText += sqlLimit;
			ResultSet rs = db.executeQuery(sqlText);
			OBDtoFlbFilterInfo filter = null;

			while (rs.next()) {
				filter = new OBDtoFlbFilterInfo();

				filter.setDbIndex(db.getString(rs, "INDEX"));
				filter.setFilterId(db.getInteger(rs, "ID"));
				filter.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				filter.setState(db.getInteger(rs, "STATE"));
				filter.setName(db.getString(rs, "NAME"));
				filter.setSrcIP(db.getString(rs, "SRC_IP"));
				filter.setSrcMask(db.getString(rs, "SRC_MASK"));
				filter.setDstIP(db.getString(rs, "DST_IP"));
				filter.setDstMask(db.getString(rs, "DST_MASK"));
				filter.setProtocol(db.getInteger(rs, "PROTOCOL"));
				filter.setSrcPortFrom(db.getInteger(rs, "SRC_PORT_FROM"));
				filter.setSrcPortTo(db.getInteger(rs, "SRC_PORT_TO"));
				filter.setDstPortFrom(db.getInteger(rs, "DST_PORT_FROM"));
				filter.setDstPortTo(db.getInteger(rs, "DST_PORT_TO"));
				filter.setAction(db.getString(rs, "ACTION"));
				filter.setGroup(db.getString(rs, "GROUP_ID"));
				filter.setRedirection(db.getString(rs, "REDIRECTION"));
				filters.add(filter);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			OBException e1 = new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			throw e1;
		} finally {
			if (db != null)
				db.closeDB();
		}
		return filters;
	}

	public ArrayList<OBDtoFlbFilterInfo> getFilterInfoByGroupId(int adcIndex, OBDtoSearch condition,
			OBDtoOrdering orderOption) throws OBException {
		ArrayList<OBDtoFlbFilterInfo> filters = new ArrayList<OBDtoFlbFilterInfo>(); // 결과값
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		String sqlSearch = "";
		int offset = 0;
		int limit = 0;
		String sqlLimit = "";
		try {
			db.openDB();

			if (condition.getBeginIndex() != null) {
				offset = condition.getBeginIndex().intValue();
			}
			if (condition.getEndIndex() != null) {
				limit = Math.abs(condition.getEndIndex().intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}
			if (condition != null && condition.getSearchKey() != null && !condition.getSearchKey().isEmpty()) {
				sqlSearch = String.format(
						" F.GROUP_ID = CAST((SELECT ALTEON_ID FROM TMP_SLB_POOL WHERE INDEX = %s) AS VARCHAR) ",
						OBParser.sqlString(condition.getSearchKey())); // FILTER의 GROUP_ID로 검색한다.
			} else {
				sqlSearch = " TRUE ";
			}

			sqlText = String.format(
					" SELECT F.INDEX, F.ID, F.ADC_INDEX, F.STATE, F.NAME, F.SRC_IP, F.SRC_MASK, F.DST_IP, F.DST_MASK,    										  \n"
							+ " 	F.PROTOCOL, F.SRC_PORT_FROM, F.SRC_PORT_TO, F.DST_PORT_FROM, F.DST_PORT_TO, F.ACTION, F.GROUP_ID, F.REDIRECTION, P.PORT_ALTEON_ID         \n"
							+ " FROM (SELECT * FROM TMP_FLB_FILTER WHERE ADC_INDEX = %d) F                                         										  \n"
							+ " LEFT JOIN (SELECT PORT_ALTEON_ID, FILTER_INDEX FROM TMP_FLB_PORT_FILTER_MAP WHERE ADC_INDEX = %d) P 										  \n"
							+ " ON F.INDEX = P.FILTER_INDEX                                                                         										  \n"
							+ " WHERE %s " + " ORDER BY F.INDEX, P.PORT_ALTEON_ID ",
					adcIndex, adcIndex, sqlSearch);

			sqlText += getFilterInfoOrderType(orderOption);
			sqlText += sqlLimit;
			ResultSet rs = db.executeQuery(sqlText);
			String currentFilterId = "";
			String newFilterId = "";
			OBDtoFlbFilterInfo filter = null;
			ArrayList<Integer> physicalPortList = null;
			while (rs.next()) {
				newFilterId = db.getString(rs, "INDEX");
				if (newFilterId.equals(currentFilterId) == false) // new filter
				{
					if (filter != null) // 처음이 아니면 filter를 추가한다.
					{
						filters.add(filter);
					}
					currentFilterId = newFilterId;
					filter = new OBDtoFlbFilterInfo();
					physicalPortList = new ArrayList<Integer>();

					filter.setDbIndex(db.getString(rs, "INDEX"));
					filter.setFilterId(db.getInteger(rs, "ID"));
					filter.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
					filter.setState(db.getInteger(rs, "STATE"));
					filter.setName(db.getString(rs, "NAME"));
					filter.setSrcIP(db.getString(rs, "SRC_IP"));
					filter.setSrcMask(db.getString(rs, "SRC_MASK"));
					filter.setDstIP(db.getString(rs, "DST_IP"));
					filter.setDstMask(db.getString(rs, "DST_MASK"));
					filter.setProtocol(db.getInteger(rs, "PROTOCOL"));
					filter.setSrcPortFrom(db.getInteger(rs, "SRC_PORT_FROM"));
					filter.setSrcPortTo(db.getInteger(rs, "SRC_PORT_TO"));
					filter.setDstPortFrom(db.getInteger(rs, "DST_PORT_FROM"));
					filter.setDstPortTo(db.getInteger(rs, "DST_PORT_TO"));
					filter.setAction(db.getString(rs, "ACTION"));
					filter.setGroup(db.getString(rs, "GROUP_ID"));
					filter.setRedirection(db.getString(rs, "REDIRECTION"));
					filter.setPhysicalPortList(physicalPortList);
				}
				filter.getPhysicalPortList().add(db.getInteger(rs, "PORT_ALTEON_ID")); // physical port 추가
			}
			// 마지막 남은 filter 추가
			filters.add(filter);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			OBException e1 = new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			throw e1;
		} finally {
			if (db != null)
				db.closeDB();
		}
		return filters;
	}

	private String getFilterInfoOrderType(OBDtoOrdering orderObj) throws OBException {
		if (orderObj == null) {
			return "";
		}
		String retVal = " ORDER BY ID ASC NULLS LAST ";
		int orderDir = orderObj.getOrderDirection();
		switch (orderObj.getOrderType()) {
		case OBDtoOrdering.TYPE_2SECOND:// STATE : ENABLE/DISABLE
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY STATE ASC NULLS LAST, ID NULLS LAST ";
			else
				retVal = " ORDER BY STATE DESC NULLS LAST, ID NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_3THIRD:// ACTION
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY ACTION ASC NULLS LAST, ID NULLS LAST ";
			else
				retVal = " ORDER BY ACTION DESC NULLS LAST, ID NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_4FOURTH:// GROUP_ID
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY GROUP_ID ASC NULLS LAST, ID NULLS LAST ";
			else
				retVal = " ORDER BY GROUP_ID DESC NULLS LAST, ID NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_5FIFTH:// ID
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY ID ASC NULLS LAST ";
			else
				retVal = " ORDER BY ID DESC NULLS LAST ";
			break;
		}

		return retVal;
	}

	@Override
	public ArrayList<OBDtoInterfaceSummary> getPhysicalPortForFilter(int adcIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%s", adcIndex));
		String sqlText = "";

		OBDatabase db = new OBDatabase();

		ArrayList<OBDtoInterfaceSummary> result = new ArrayList<OBDtoInterfaceSummary>();
		try {
			db.openDB();
			sqlText = String.format(" SELECT PA.PORT, PB.ALIAS_NAME "
					+ " FROM (SELECT DISTINCT(PORT_ALTEON_ID) PORT FROM TMP_FLB_PORT_FILTER_MAP WHERE ADC_INDEX = %d ) PA "
					+ " LEFT JOIN (SELECT PORT_INDEX, ALIAS_NAME FROM TMP_FAULT_LINK_INFO WHERE ADC_INDEX = %d ) PB       "
					+ " ON PA.PORT = PB.PORT_INDEX                                                                        "
					+ " ORDER BY PA.PORT ", adcIndex, adcIndex);
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoInterfaceSummary port = new OBDtoInterfaceSummary();
				port.setIndex(db.getInteger(rs, "PORT"));
				port.setDescription(db.getString(rs, "ALIAS_NAME"));
				result.add(port);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	// ---------------------------------------------------------------
	// 여기부터 테스트 함수 영역

	public void zTestGetFlbGroupMonitorInfo() {
		int adcIndex = 4;
		try {
			ArrayList<OBDtoFlbGroupMonitorInfo> result = getFlbGroupMonitorInfo(adcIndex);
			System.out.println("zTestGetFlbGroupMonitorInfo() result = " + result);
		} catch (OBException e) {
			e.printStackTrace();
		}
	}

	public void zTestSetFlbGroupMonitorInfo() {
		ArrayList<String> groupIndexList = new ArrayList<String>();
		groupIndexList.add("6_1");
		groupIndexList.add("6_2");
		groupIndexList.add("6_10");
		try {
			setFlbGroupMonitorInfo(6, groupIndexList);
		} catch (OBException e) {
			e.printStackTrace();
		}
	}

	public void zTestGetPhysicalPortForFilter() {
		Integer adcIndex = 6;
		try {
			System.out.println("Filter count for search condition === " + getPhysicalPortForFilter(adcIndex));
		} catch (OBException e) {
			e.printStackTrace();
		}
	}

	public void zTestGetFilterCount() {
		OBDtoSearch condition = new OBDtoSearch();
		Integer adcIndex = 4;
		condition.setSearchKey("2");
		try {
			System.out.println("Filter count for search condition === " + getFilterCount(adcIndex, condition));
		} catch (OBException e) {
			e.printStackTrace();
		}
	}

	public void zTestGetFilterInfo() {
		OBDtoSearch condition = new OBDtoSearch();
		Integer adcIndex = 4;
		condition.setSearchKey(null);
		try {
			System.out.println("Filters for search condition === " + getFilterInfo(adcIndex, condition, null));
		} catch (OBException e) {
			e.printStackTrace();
		}
	}

	public void zTestGetFilterInfoByGroupId() {
		OBDtoSearch condition = new OBDtoSearch();
		Integer adcIndex = 6;
		condition.setSearchKey("6_1");
		try {
			System.out.println(getFilterInfoByGroupId(adcIndex, condition, null));
		} catch (OBException e) {
			e.printStackTrace();
		}
	}
//	public static void main(String[] args)
//	{
//		OBMonitoringFlbAlteonImpl me = new OBMonitoringFlbAlteonImpl();
////		me.zTestSetFlbGroupMonitorInfo(); //1. set하고 
//		me.zTestGetFlbGroupMonitorInfo(); //2. 적용됐는지 뽑아본다.
////		me.zTestGetPhysicalPortForFilter();
////		me.zTestGetFilterCount();
////		me.zTestGetFilterInfo();
////		System.out.println("----zTestGetFilterInfoByGroupId()------------");
////		me.zTestGetFilterInfoByGroupId(); //group id로 정보 구하기
//		
//	}
}
