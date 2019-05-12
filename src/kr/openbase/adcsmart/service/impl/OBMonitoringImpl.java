package kr.openbase.adcsmart.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.openbase.adcsmart.service.OBMonitoring;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcAlertLog;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPerfConnection;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPerfHttpReq;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPerfSslTrans;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPerfTroughput;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPerformance;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPortStatus;
import kr.openbase.adcsmart.service.dto.OBDtoAdcScope;
import kr.openbase.adcsmart.service.dto.OBDtoAdcStatusCount;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSysRescStatus;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSystemLog;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSystemResources;
import kr.openbase.adcsmart.service.dto.OBDtoBpsConnData;
import kr.openbase.adcsmart.service.dto.OBDtoConnectionData;
import kr.openbase.adcsmart.service.dto.OBDtoConnectionInfo;
import kr.openbase.adcsmart.service.dto.OBDtoMultiDataObj;
import kr.openbase.adcsmart.service.dto.OBDtoNameCurAvgMax;
import kr.openbase.adcsmart.service.dto.OBDtoNetworkMap;
import kr.openbase.adcsmart.service.dto.OBDtoNetworkMapBackup;
import kr.openbase.adcsmart.service.dto.OBDtoNetworkMapMember;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoServiceMonitoringChart;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.dto.OBDtoTrafficMapVServiceMembers;
import kr.openbase.adcsmart.service.dto.OBDtoVirtualServerSummary;
import kr.openbase.adcsmart.service.dto.OBDtoVservStatusCount;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultGroupMemberPerfInfo;
import kr.openbase.adcsmart.service.dto.flb.OBDtoFlbFilterInfo;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;
import kr.openbase.adcsmart.service.impl.dto.OBDtoConnectionDataObj;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalAdc;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalAdcCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalAdcOne;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalFilterUnit;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalGroup;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalGroupCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalGroupOne;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalReal;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalRealCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalRealOne;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalService;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalServiceCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalServiceOne;
import kr.openbase.adcsmart.service.impl.fault.OBFaultMonitoringImpl;
import kr.openbase.adcsmart.service.snmp.alteon.OBSnmpAlteon;
import kr.openbase.adcsmart.service.snmp.f5.OBSnmpF5;
import kr.openbase.adcsmart.service.snmp.pas.OBSnmpPAS;
import kr.openbase.adcsmart.service.snmp.pask.OBSnmpPASK;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBDefineFault;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.facade.dto.ServiceMapVsDescDto;

class FilterValue {
	private Long from;
	private Long to;
	private String word1;
	private String word2;

	public Long getFrom() {
		return from;
	}

	public void setFrom(Long from) {
		this.from = from;
	}

	public Long getTo() {
		return to;
	}

	public void setTo(Long to) {
		this.to = to;
	}

	public String getWord1() {
		return word1;
	}

	public void setWord1(String word1) {
		this.word1 = word1;
	}

	public String getWord2() {
		return word2;
	}

	public void setWord2(String word2) {
		this.word2 = word2;
	}
}

class Model implements Comparable<Model> // sorted set인 TreeSet에서 쓰이므로 compareble을 구현해야 한다.
{
	private Integer adcType;
	private String model;
	private String modelShort; // 축약한 모델 이름
	private String modelCode; // 모델 식별 핵심 코드

	public Model(Integer adcType, String model, String modelShort, String modelCode) {
		super();
		this.adcType = adcType;
		this.model = model;
		this.modelShort = modelShort;
		this.modelCode = modelCode;
	}

	@Override
	public int compareTo(Model other) // compareble - 축약하기 전의 model이름으로 compare
	{
		return this.getModelCode().compareTo(other.getModelCode());
	}

	@Override
	public String toString() {
		return "Model [adcType=" + adcType + ", model=" + model + ", modelShort=" + modelShort + ", modelCode="
				+ modelCode + "]";
	}

	public Integer getAdcType() {
		return adcType;
	}

	public void setAdcType(Integer adcType) {
		this.adcType = adcType;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModelShort() {
		return modelShort;
	}

	public void setModelShort(String modelShort) {
		this.modelShort = modelShort;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}
}

class Order {
	// ADC전체모니터링의 컬럼 순서 정의
	final String[] TOTAL_ADC_COLS = { null, // 0:순서없음
			"STATUS_SORT", // 1:단절을 맨 끝으로 보내려고 가공한 필드를 쓴다.
			"TYPE", // 2
			"NAME", // 3
			"ACTIVE_BACKUP_STATE", // 4
			"MODEL", // 5
			"SW_VERSION", // 6
			"BPS", // 7
			"CONN_CURR", // 8
			"UP_AGE", // 9
			"INET(IPADDRESS)", // 10
			"APPLY_TIME", // 11
			null, // 12:CPU 사용율인데, sorting할수 없어 ordering 기준이 못됨
			"MEM_USAGE", // 13
			"EPS_SUM", // 14
			"DPS_SUM", // 15
			"CERT_COUNT", // 16
			"INTERFACE_COUNT", // 17
			"FILTER_COUNT", // 18
			"VS_COUNT_AVAIL", // 19
			"LOG24H_COUNT", // 20
			"SLBCONFIG24H_COUNT" // 21
	};
	final String[] TOTAL_SERVICE_COLS = { null, "STATUS_SORT", // 1 '단절'을 맨 끝으로 보내려고 가공한 필드를 쓴다.
			"INET(VIRTUAL_IP)", // 2
			"VIRTUAL_PORT", // 3
			"VS_NAME", // 4
			"BPS_IN", // 5
			"BPS_OUT", // 6
			"BPS_TOT", // 7
			"CONN_CURR", // 8
			"ADC_NAME", // 9
			"ADC_TYPE", // 10
			"INET(ADC_IP)", // 11
			"COUNT_MEMBER", // 12
			"POOL_NAME", // 13
			"LB_METHOD", // 14
			"HEALTH_CHECK", // 15
			"PROFILE_NAME", // 16
			"IS_NOTICE", // 17
			"UPDATE_TIME", // 18
			"COUNT_CONFIG24H" // 19
	};
	final String[] TOTAL_GROUP_COLS = { null, "NAME", // 1
			"ALTEON_ID", // 2
			"BACKUP", // 3
			"COUNT_MEMBER", // 4
			"COUNT_FILTER", // 5
			"COUNT_VS", // 6
			"ADC_NAME", // 7
			"ADC_TYPE", // 8
			"INET(ADC_IP)" // 9
	};
	final String[] TOTAL_REAL_COLS = { null, "STATUS", // 1
			"STATE", // 2
			"INET(IP_ADDRESS)", // 3
			"NAME", // 4
			"IS_USED", // 5
			"COUNT_GROUP", // 6
			"ADC_NAME", // 7
			"ADC_TYPE", // 8
			"INET(ADC_IP)", // 9
			"RATIO" // 10
	};
}

public class OBMonitoringImpl implements OBMonitoring {
	private static final Integer BYTES = 1;
	private static final Integer PACKETS = 2;
	private static final Integer ERRORS = 3;
	private static final Integer DROPS = 4;

	private static final Integer PAGE_MAX_LINE = 10000;

	/**
	 * 
	 * @param adcIndex
	 * @param vsIndex
	 * @param poolOrServiceIndex : F5, PAS, PASK poolIndex, Aleton이면 virtual service
	 *                           index
	 * @param vsState            -- virtual server(service) state, vs가 disable이면 멤버
	 *                           상태도 disable로 한다.
	 * @return
	 * @throws OBException
	 */
//	private ArrayList<OBDtoNetworkMapMember> getNetworkMapMembers(Integer adcIndex, String vsIndex,
//			String poolOrServiceIndex, Integer virtualServerState) throws OBException {
//		ArrayList<OBDtoNetworkMapMember> result = new ArrayList<OBDtoNetworkMapMember>();
//		if (vsIndex == null || poolOrServiceIndex == null || vsIndex.isEmpty() || poolOrServiceIndex.isEmpty()) {
//			return result;
//		}
//		String sqlText = null;
//
//		OBDatabase db = new OBDatabase();
//		try {
//			db.openDB();
//
//			sqlText = String.format(
//					" SELECT A.NODE_INDEX, A.PORT, B.IP_ADDRESS, C.VS_INDEX, C.MEMBER_STATUS                 \n"
//							+ " FROM (SELECT INDEX, NODE_INDEX, PORT FROM TMP_SLB_POOLMEMBER WHERE ADC_INDEX = %d ) A  \n"
//							+ " INNER JOIN (SELECT INDEX, IP_ADDRESS FROM TMP_SLB_NODE WHERE ADC_INDEX = %d ) B        \n"
//							+ " ON A.NODE_INDEX=B.INDEX                                                                \n"
//							+ " LEFT JOIN TMP_SLB_POOLMEMBER_STATUS C                                                  \n"
//							+ " ON A.INDEX=C.POOLMEMBER_INDEX                                                          \n"
//							+ " WHERE C.VS_INDEX=%s AND C.VSRC_INDEX = %s  -- F5,PAS,PASK에서 VSRC_INDEX=pool index      \n"
//							+ " ORDER BY C.MEMBER_STATUS, B.IP_ADDRESS ;                                               \n",
//					adcIndex, adcIndex, OBParser.sqlString(vsIndex), OBParser.sqlString(poolOrServiceIndex));
//
//			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "vsStatus === " + virtualServerState);
//			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText === " + sqlText);
//			ResultSet rs = db.executeQuery(sqlText);
//			while (rs.next()) {
//				OBDtoNetworkMapMember map = new OBDtoNetworkMapMember();
//				map.setIpAddress(db.getString(rs, "IP_ADDRESS"));
//				map.setNodeIndex(db.getString(rs, "NODE_INDEX"));
//				map.setPort(db.getInteger(rs, "PORT"));
//				if (virtualServerState.equals(OBDefine.VS_STATUS.DISABLE)) // virtual server가 disable이면 member도 disable로
//																			// 표시
//				{
//					map.setStatus(OBDefine.MEMBER_STATUS.DISABLE);
//				} else {
//					map.setStatus(db.getInteger(rs, "MEMBER_STATUS"));
//				}
//				result.add(map);
//			}
//		} catch (SQLException e) {
//			throw new OBException(OBException.ERRCODE_DB_QEURY,
//					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
//		} catch (OBException e) {
//			throw e;
//		} catch (Exception e) {
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
//		} finally {
//			if (db != null)
//				db.closeDB();
//		}
//		return result;
//	}

	/**
	 * 지정된 장비의 네트워크 맵 구성을 위한 virtual server의 상태 및 pool member의 상태 정보를 제공한다. F5의 경우에는
	 * virtual server 단위로, alteon인 경우에는 virtual service 단위로 구성한다. tmp_slb_vserver,
	 * tmp_slb_vs_service, tmp_slb_poolmember을 참조하여 구성한다.
	 * 
	 * @param searchKeys : null 가능. 지정된 검색어에 해당되는 정보만 추출하고자 할 경우에 사용. virtual 서버의
	 *                   이름, IP주소를 대상으로 검색한다.
	 */
	private ArrayList<OBDtoNetworkMap> getNetworkMapsF5(Integer adcIndex, Integer status, String searchKeys,
			Integer beginIndex, Integer endIndex, Integer accountIndex, String accountRole, OBDtoOrdering orderOption)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "start. adcIndex:" + adcIndex);

		ArrayList<OBDtoNetworkMap> result = new ArrayList<OBDtoNetworkMap>();

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			int offset = 0;
			int limit = 0;
			String sqlLimit = "";

			if (beginIndex != null)
				offset = beginIndex.intValue();

			if (endIndex != null) {
				limit = Math.abs(endIndex.intValue() - offset) + 1;
				sqlLimit = " LIMIT " + limit + " OFFSET " + offset + " ";
			}

			// 조회결과 전체에 적용할 where절 구성 시작
			String globalWhere = "";
			if (status != null) {
				globalWhere = " VS_STATUS= " + status + " "; // al
			}
			if (searchKeys != null && !searchKeys.isEmpty()) {
				if (globalWhere.length() > 0) {
					globalWhere += " AND ";
				}

				// #3984-2 #5: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKeys) + "%";
				globalWhere += " ( VS_NAME LIKE " + OBParser.sqlString(wildcardKey) + " OR VIRTUAL_IP LIKE "
						+ OBParser.sqlString(wildcardKey) + " OR NODE_IP_ADDRESS LIKE "
						+ OBParser.sqlString(wildcardKey) + ") ";

			}
			if (accountRole.equals("vsAdmin")) {
				if (globalWhere.length() > 0) {
					globalWhere += " AND ";
				}
				globalWhere += " VS2.VS_INDEX IN (SELECT VS_INDEX FROM MNG_ACCNT_VS_MAP WHERE ADC_INDEX = " + adcIndex
						+ " AND ACCNT_INDEX = " + accountIndex + ") ";
			}
			if (globalWhere.length() > 0) {
				globalWhere = " WHERE " + globalWhere;
			}
			// 조회결과 전체에 적용할 where절 구성 끝

			// virtual server, poolmember로 구성한다.
			sqlText = " SELECT ADC.MODEL, ADC.NAME, VS2.VS_INDEX VS_INDEX, VS2.VS_STATUS VS_STATUS, VS2.VS_NAME VS_NAME, VS2.VIRTUAL_IP VIRTUAL_IP, VS2.VIRTUAL_PORT VIRTUAL_PORT, \n"
					+ " VS2.GROUP, VS2.GROUP_NAME, VS2.POOL_INDEX, VS2.USE_YN, VS2.MEMBER_INDEX MEMBER_INDEX, VS2.MEMBER_PORT MEMBER_PORT, POOL.METRIC, \n"
					+ " VS2.NODE_IP_ADDRESS NODE_IP_ADDRESS, VS2.NODE_INDEX NODE_INDEX, MSTATUS2.MEMBER_STATUS MEMBER_STATUS \n"
					+ " FROM \n" + " (\n" + "    SELECT \n"
					+ "    VS.ADC_INDEX, VS.INDEX VS_INDEX, VS.STATUS VS_STATUS, VS.NAME VS_NAME, VS.VIRTUAL_IP VIRTUAL_IP, VS.VIRTUAL_PORT, VS.USE_YN, MEMBER.INDEX MEMBER_INDEX, \n"
					+ "    VS.POOL_INDEX, MEMBER.NODE_INDEX NODE_INDEX, MEMBER.PORT MEMBER_PORT, NODE.IP_ADDRESS NODE_IP_ADDRESS, \n"
					+ "    NODE.ALTEON_ID AS GROUP, VS.POOL_INDEX AS GROUP_NAME \n" + "    FROM \n"
					+ "        (SELECT ADC_INDEX, INDEX, STATUS, NAME, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX, USE_YN FROM TMP_SLB_VSERVER WHERE ADC_INDEX = "
					+ adcIndex + ") VS\n" + "        LEFT JOIN \n"
					+ "        (SELECT INDEX, POOL_INDEX, NODE_INDEX, PORT FROM TMP_SLB_POOLMEMBER WHERE ADC_INDEX = "
					+ adcIndex + ") MEMBER          \n" + "        ON MEMBER.POOL_INDEX = VS.POOL_INDEX\n"
					+ "        LEFT JOIN (SELECT INDEX, IP_ADDRESS, ALTEON_ID FROM TMP_SLB_NODE WHERE ADC_INDEX = "
					+ adcIndex + ") NODE\n" + "        ON MEMBER.NODE_INDEX = NODE.INDEX\n" + " ) VS2\n"
					+ " LEFT JOIN\n" + "(\n" + " SELECT INDEX, MODEL, NAME FROM MNG_ADC WHERE INDEX = " + adcIndex
					+ ") ADC \n" + "         ON ADC.INDEX = VS2.ADC_INDEX\n" + " LEFT JOIN\n" + "(\n"
					+ "SELECT INDEX, LB_METHOD AS METRIC FROM TMP_SLB_POOL WHERE ADC_INDEX = " + adcIndex + ") POOL\n"
					+ "         ON POOL.INDEX = VS2.POOL_INDEX\n" + " LEFT JOIN\n" + " (\n"
					+ " SELECT VM.VS_INDEX VS_INDEX, VM.MEMBER_INDEX MEMBER_INDEX, MSTATUS.MEMBER_STATUS\n" + " FROM \n"
					+ " (\n" + "    SELECT VS.INDEX VS_INDEX, MEMBER.INDEX MEMBER_INDEX\n" + "    FROM \n"
					+ "    (SELECT INDEX, STATUS, NAME, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX, USE_YN FROM TMP_SLB_VSERVER WHERE ADC_INDEX = "
					+ adcIndex + ") VS\n" + "    LEFT JOIN\n"
					+ "    (SELECT INDEX, POOL_INDEX, NODE_INDEX, PORT FROM TMP_SLB_POOLMEMBER WHERE ADC_INDEX = "
					+ adcIndex + ") MEMBER\n" + "    ON MEMBER.POOL_INDEX = VS.POOL_INDEX\n" + " ) VM\n"
					+ " LEFT JOIN \n" + " (\n"
					+ "    SELECT VS_INDEX, POOLMEMBER_INDEX, MEMBER_STATUS FROM TMP_SLB_POOLMEMBER_STATUS \n"
					+ " ) MSTATUS\n"
					+ " ON VM.VS_INDEX = MSTATUS.VS_INDEX AND VM.MEMBER_INDEX = MSTATUS.POOLMEMBER_INDEX\n"
					+ " GROUP BY VM.VS_INDEX, VM.MEMBER_INDEX, MSTATUS.MEMBER_STATUS\n" + " ) MSTATUS2\n"
					+ " ON VS2.VS_INDEX = MSTATUS2.VS_INDEX AND VS2.MEMBER_INDEX = MSTATUS2.MEMBER_INDEX\n"
					+ globalWhere;
//                            + " ORDER BY VS2.VS_INDEX, NODE_IP_ADDRESS, MEMBER_PORT "
//                            + sqlLimit
//                            + " ; ";

			sqlText += getNetworkMapF5OrderTypeList(orderOption);
			if (sqlLimit != null && !sqlLimit.isEmpty()) {
				sqlText += sqlLimit;
			}

			ResultSet rs = db.executeQuery(sqlText);
			String currentVsIndex = "";
			String newVsIndex = "";
			OBDtoNetworkMap obj = null;
			ArrayList<OBDtoNetworkMapMember> memList = null;
			Integer vsState = null;
			while (rs.next()) {
				newVsIndex = db.getString(rs, "VS_INDEX");
				if (currentVsIndex.equals(newVsIndex) == false) // 새 vs 데이터 시작
				{
					if (obj != null) {
						obj.setMemberList(memList); // 모은 멤버리스트를 vs에 붙인다.
						result.add(obj);
						obj = null;
						memList = null;
					}
					obj = new OBDtoNetworkMap();
					memList = new ArrayList<OBDtoNetworkMapMember>(); // 빈 memList를 만들어놓는다.
					obj.setModel(db.getString(rs, "MODEL"));
					obj.setHostName(db.getString(rs, "NAME"));
					obj.setLoadBalancingType(db.getInteger(rs, "METRIC"));
					obj.setAlteonId(db.getString(rs, "GROUP"));
					obj.setGroupId(db.getString(rs, "GROUP_NAME"));
					obj.setRport(db.getString(rs, "MEMBER_PORT"));
					obj.setVsIndex(newVsIndex);
					obj.setLbClass(OBDefine.ADC_LB_CLASS.SLB);
					obj.setServicePort(db.getInteger(rs, "VIRTUAL_PORT"));
					obj.setServiceStatus(db.getInteger(rs, "VS_STATUS"));
					obj.setVsIPAddress(db.getString(rs, "VIRTUAL_IP"));
					obj.setVsName(db.getString(rs, "VS_NAME"));
					vsState = db.getInteger(rs, "USE_YN");

					currentVsIndex = newVsIndex;
				}
				// 앞과 같은 vs의 다른 멤버 데이터
				if (obj != null && memList != null && db.getString(rs, "NODE_IP_ADDRESS") != null) {
					OBDtoNetworkMapMember mem = new OBDtoNetworkMapMember();
					mem.setIpAddress(db.getString(rs, "NODE_IP_ADDRESS"));
					mem.setNodeIndex(db.getString(rs, "NODE_INDEX"));
					mem.setPort(db.getInteger(rs, "MEMBER_PORT"));
					if (vsState.equals(OBDefine.VS_STATUS.DISABLE)) // virtual server가 disable이면 member도 disable로 표시
					{
						mem.setStatus(OBDefine.MEMBER_STATUS.DISABLE);
					} else {
						mem.setStatus(db.getInteger(rs, "MEMBER_STATUS"));
					}
					memList.add(mem);
				}
			}
			if (obj != null) // 마지막 vs 종결 처리한다.
			{
				obj.setMemberList(memList); // 모은 멤버리스트를 vs에 붙인다.
				result.add(obj);
				obj = null;
				memList = null;
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result count:%s", result.size()));
		return result;
	}

	private String getNetworkMapF5OrderTypeList(OBDtoOrdering orderObj) throws OBException {
		if (orderObj == null)
			return "";

//        String retVal = " ORDER BY VS_INDEX ASC NULLS LAST, NODE_IP_ADDRESS ASC NULLS LAST, MEMBER_PORT ASC NULLS LAST ";
		String retVal = " ORDER BY INET(VIRTUAL_IP) ASC NULLS LAST, VIRTUAL_PORT ASC NULLS LAST, VS_INDEX ASC NULLS LAST, INET(NODE_IP_ADDRESS) ASC NULLS LAST, MEMBER_PORT ASC NULLS LAST ";
		int orderDir = orderObj.getOrderDirection();
		switch (orderObj.getOrderType()) {
		case OBDtoOrdering.TYPE_2SECOND:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INET(VIRTUAL_IP) ASC NULLS LAST, VIRTUAL_PORT ASC NULLS LAST, VS_INDEX ASC NULLS LAST, INET(NODE_IP_ADDRESS) ASC NULLS LAST, MEMBER_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY INET(VIRTUAL_IP) DESC NULLS LAST, VIRTUAL_PORT DESC NULLS LAST, VS_INDEX ASC NULLS LAST, INET(NODE_IP_ADDRESS) ASC NULLS LAST, MEMBER_PORT ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_3THIRD:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY VIRTUAL_PORT ASC NULLS LAST, INET(VIRTUAL_IP) ASC NULLS LAST, VS_INDEX ASC NULLS LAST, INET(NODE_IP_ADDRESS) ASC NULLS LAST, MEMBER_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY VIRTUAL_PORT DESC NULLS LAST, INET(VIRTUAL_IP) DESC NULLS LAST, VS_INDEX ASC NULLS LAST, INET(NODE_IP_ADDRESS) ASC NULLS LAST, MEMBER_PORT DESC NULLS LAST ";
			break;
		}

		return retVal;
	}

//    private String getNetworkMapOrderTypeList(OBDtoOrdering orderObj) throws OBException
//    {
//        if(orderObj == null)
//            return "";
//        
//        String retVal = " ORDER BY VS_NAME DESC NULLS LAST, VIRTUAL_IP ASC NULLS LAST, VIRTUAL_PORT ASC NULLS LAST, VS_INDEX ASC NULLS LAST ";
//        int orderDir = orderObj.getOrderDirection();
//        switch(orderObj.getOrderType())
//        {
//        case OBDtoOrdering.TYPE_2SECOND:// vip  
//            if(orderDir == OBDefine.ORDER_DIR_ASCEND)
//                retVal = " ORDER BY VIRTUAL_IP ASC NULLS LAST, VS_NAME DESC NULLS LAST, VIRTUAL_PORT DESC NULLS LAST, VS_INDEX ASC NULLS LAST ";
//            else
//                retVal = " ORDER BY VIRTUAL_IP DESC NULLS LAST, VS_NAME DESC NULLS LAST, VIRTUAL_PORT DESC NULLS LAST, VS_INDEX ASC NULLS LAST ";
//            break;
//        case OBDtoOrdering.TYPE_3THIRD:// service port
//            if(orderDir == OBDefine.ORDER_DIR_ASCEND)
//                retVal = " ORDER BY VIRTUAL_PORT ASC NULLS LAST, VS_NAME DESC NULLS LAST, VIRTUAL_IP DESC NULLS LAST, VS_INDEX ASC NULLS LAST ";
//            else
//                retVal = " ORDER BY VIRTUAL_PORT DESC NULLS LAST, VS_NAME DESC NULLS LAST, VIRTUAL_IP DESC NULLS LAST, VS_INDEX ASC NULLS LAST ";
//            break;
//        }   
//        
//        return retVal;
//    }

//	private ArrayList<OBDtoNetworkMap> getNetworkMapsPAS(Integer adcIndex, Integer status, String searchKeys,
//			Integer beginIndex, Integer endIndex, Integer accountIndex, String accountRole, OBDatabase db)
//			throws OBException {
//		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
//
//		ArrayList<OBDtoNetworkMap> result = new ArrayList<OBDtoNetworkMap>();
//
//		String sqlText = "";
//		try {
//			int offset = 0;
//			if (beginIndex != null)
//				offset = beginIndex.intValue();
//
//			int limit = 0;
//			String sqlLimit = "";
//			if (endIndex != null) {
//				limit = Math.abs(endIndex.intValue() - offset) + 1;
//				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
//			}
//
//			// virtual server, poolmember로 구성한다.
//			sqlText = String.format(" SELECT INDEX, STATUS, USE_YN, NAME, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX "
//					+ " FROM TMP_SLB_VSERVER " + " WHERE ADC_INDEX=%d ", adcIndex);
//
//			if (status != null) {
//				String sqlStatus = String.format(" STATUS=%d ", status);
//				sqlText += " AND " + sqlStatus;
//			}
//			if (searchKeys != null && !searchKeys.isEmpty()) {
//				// #3984-2 #5: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
//				String wildcardKey = "%" + OBParser.removeWildcard(searchKeys) + "%";
//				String sqlSearch = String.format(" ( NAME LIKE %s OR VIRTUAL_IP LIKE %s ) ",
//						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
//				sqlText += " AND " + sqlSearch;
//			}
//			if (accountRole.equals("vsAdmin")) {
//				String sqlVsadmin = String.format(
//						" INDEX IN (SELECT VS_INDEX FROM MNG_ACCNT_VS_MAP WHERE ADC_INDEX = %d AND ACCNT_INDEX = %d) ",
//						adcIndex, accountIndex);
//				sqlText += " AND " + sqlVsadmin;
//			}
//			sqlText += "  ORDER BY NAME ASC  ";
//
//			if (sqlLimit != null && !sqlLimit.isEmpty())
//				sqlText += sqlLimit;
//
//			sqlText += ";";
//			ResultSet rs = db.executeQuery(sqlText);
//			while (rs.next()) {
//				OBDtoNetworkMap obj = new OBDtoNetworkMap();
//				obj.setLbClass(OBDefine.ADC_LB_CLASS.SLB);
//				obj.setServicePort(db.getInteger(rs, "VIRTUAL_PORT"));
//				obj.setServiceStatus(db.getInteger(rs, "STATUS"));
//				obj.setVsIndex(db.getString(rs, "INDEX"));
//				obj.setVsIPAddress(db.getString(rs, "VIRTUAL_IP"));
//				obj.setVsName(db.getString(rs, "NAME"));
//				String poolIndex = db.getString(rs, "POOL_INDEX");
//				Integer vsState = db.getInteger(rs, "USE_YN");
//				// obj.setMemberList(getNetworkMapMembersPAS(adcIndex, obj.getVsName(),
//				// obj.getServicePort(), poolIndex, vsState, db2));
//				obj.setMemberList(getNetworkMapMembers(adcIndex, obj.getVsIndex(), poolIndex, vsState));
//				result.add(obj);
//			}
//		} catch (SQLException e) {
//			throw new OBException(OBException.ERRCODE_DB_QEURY,
//					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
//		} catch (OBException e) {
//			throw e;
//		} catch (Exception e) {
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
//		}
//		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result count:%s", result.size()));
//		return result;
//	}
//
//	private ArrayList<OBDtoNetworkMap> getNetworkMapsPASK(Integer adcIndex, Integer status, String searchKeys,
//			Integer beginIndex, Integer endIndex, Integer accountIndex, String accountRole, OBDatabase db)
//			throws OBException {
//		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
//
//		ArrayList<OBDtoNetworkMap> result = new ArrayList<OBDtoNetworkMap>();
//
//		String sqlText = "";
//		try {
//			int offset = 0;
//			if (beginIndex != null)
//				offset = beginIndex.intValue();
//
//			int limit = 0;
//			String sqlLimit = "";
//			if (endIndex != null) {
//				limit = Math.abs(endIndex.intValue() - offset) + 1;
//				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
//			}
//
//			// virtual server, poolmember로 구성한다.
//			sqlText = String.format(" SELECT INDEX, STATUS, USE_YN, NAME, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX "
//					+ " FROM TMP_SLB_VSERVER " + " WHERE ADC_INDEX=%d ", adcIndex);
//
//			if (status != null) {
//				String sqlStatus = String.format(" STATUS=%d ", status);
//				sqlText += " AND " + sqlStatus;
//			}
//			if (searchKeys != null && !searchKeys.isEmpty()) {
//				// #3984-2 #5: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
//				String wildcardKey = "%" + OBParser.removeWildcard(searchKeys) + "%";
//				String sqlSearch = String.format(" ( NAME LIKE %s OR VIRTUAL_IP LIKE %s ) ",
//						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
//				sqlText += " AND " + sqlSearch;
//			}
//			if (accountRole.equals("vsAdmin")) {
//				String sqlVsadmin = String.format(
//						" INDEX IN (SELECT VS_INDEX FROM MNG_ACCNT_VS_MAP WHERE ADC_INDEX = %d AND ACCNT_INDEX = %d) ",
//						adcIndex, accountIndex);
//				sqlText += " AND " + sqlVsadmin;
//			}
//			sqlText += "  ORDER BY NAME ASC  ";
//
//			if (sqlLimit != null && !sqlLimit.isEmpty())
//				sqlText += sqlLimit;
//
//			sqlText += ";";
//			ResultSet rs = db.executeQuery(sqlText);
//			while (rs.next()) {
//				OBDtoNetworkMap obj = new OBDtoNetworkMap();
//				obj.setLbClass(OBDefine.ADC_LB_CLASS.SLB);
//				obj.setServicePort(db.getInteger(rs, "VIRTUAL_PORT"));
//				obj.setServiceStatus(db.getInteger(rs, "STATUS"));
//				obj.setVsIndex(db.getString(rs, "INDEX"));
//				obj.setVsIPAddress(db.getString(rs, "VIRTUAL_IP"));
//				obj.setVsName(db.getString(rs, "NAME"));
//				String poolIndex = db.getString(rs, "POOL_INDEX");
//				Integer vsState = db.getInteger(rs, "USE_YN");
//				// obj.setMemberList(getNetworkMapMembersPASK(adcIndex, obj.getVsName(),
//				// obj.getServicePort(), poolIndex, vsState, db2));
//				obj.setMemberList(getNetworkMapMembers(adcIndex, obj.getVsIndex(), poolIndex, vsState));
//				result.add(obj);
//			}
//		} catch (SQLException e) {
//			throw new OBException(OBException.ERRCODE_DB_QEURY,
//					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
//		} catch (OBException e) {
//			throw e;
//		} catch (Exception e) {
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
//		}
//		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result count:%s", result.size()));
//		return result;
//	}

	private ArrayList<OBDtoNetworkMap> getNetworkMapsAlteon(Integer adcIndex, Integer lbClass, Integer status,
			String searchKeys, Integer beginIndex, Integer endIndex, Integer accountIndex, String accountRole,
			OBDtoOrdering orderOption) throws OBException {// virtual service, poolmember로 구성한다.
		if (lbClass.equals(OBDefine.ADC_LB_CLASS.SLB)) {
			return getNetworkMapsAlteonSlbVService(adcIndex, status, searchKeys, beginIndex, endIndex, accountIndex,
					accountRole, orderOption);
		} else if (lbClass.equals(OBDefine.ADC_LB_CLASS.FLB)) {
			return getNetworkMapsAlteonFlbGroup(adcIndex, status, searchKeys, beginIndex, endIndex, orderOption);
		} else if (lbClass.equals(OBDefine.ADC_LB_CLASS.ALL)) {
			ArrayList<OBDtoNetworkMap> slb = getNetworkMapsAlteonSlbVService(adcIndex, status, searchKeys, beginIndex,
					endIndex, accountIndex, accountRole, orderOption);
			ArrayList<OBDtoNetworkMap> flb = getNetworkMapsAlteonFlbGroup(adcIndex, status, searchKeys, beginIndex,
					endIndex, orderOption);
			ArrayList<OBDtoNetworkMap> allService = new ArrayList<OBDtoNetworkMap>();
			if (slb != null && slb.size() > 0) {
				allService.addAll(slb);
			}
			if (flb != null && flb.size() > 0) {
				allService.addAll(flb);
			}
			return allService;
		} else {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL); // TODO EXCEPTION 처리 필요
		}
	}

	// TODO
	private ArrayList<OBDtoNetworkMap> getNetworkMapsAlteonSlbVService(Integer adcIndex, Integer status,
			String searchKeys, Integer beginIndex, Integer endIndex, Integer accountIndex, String accountRole,
			OBDtoOrdering orderOption) throws OBException {// virtual service, poolmember로 구성한다.
		ArrayList<OBDtoNetworkMap> result = new ArrayList<OBDtoNetworkMap>();
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			int offset = 0;
			if (beginIndex != null)
				offset = beginIndex.intValue();

			int limit = 0;
			String sqlLimit = "";
			if (endIndex != null) {
				limit = Math.abs(endIndex.intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}
			// vs - index, alteon id, ip / vss - index, status, port / member - port, ip,
			// status
			// 2개의 서비스에서 동일 그룹을 사용하고 있음 -> INDEX 조건 추가 (Alteon service : index ,
			// poolmemberStatus : vsrc_index) _ 2015.02.24 추가
			sqlText = String.format(
					" SELECT VS.INDEX VS_INDEX, VS.NAME VS_NAME, VS.VIRTUAL_IP VIRTUAL_IP, VS.ALTEON_ID, POOL.ALTEON_ID AS GROUP_ID,                     \n"
							+ "     ADC.NAME AS HOSTNAME, ADC.MODEL, VSS.INDEX SERVICE_INDEX, VSS.STATUS, VSS.VIRTUAL_PORT, --서비스 바뀌는 것을 확인하는데만 쓴다.         \n"
							+ "     VSS.REAL_PORT AS RPORT, MEMBER.NODE_INDEX, VSS.REAL_PORT MEMBER_PORT,                                               		 \n"
							+ "     POOL.BAK_TYPE POOL_BAK_TYPE, POOL.BAK_ID POOL_BAK_ID,                                                                        \n"
							+ "     NODE.BAK_TYPE NODE_BAK_TYPE, NODE.BAK_ID NODE_BAK_ID, NODE.EXTRA NODE_EXTRA,                                                 \n"
							+ "     NODE.IP_ADDRESS NODE_IP_ADDRESS, NODE.INDEX NODE_INDEX, MSTATUS.MEMBER_STATUS,             						             \n"
							+ "     POOL.LB_METHOD, HEALTHCHECK.ID_NEW                                                                                           \n"
							+ " FROM (SELECT ADC_INDEX, INDEX, ALTEON_ID, NAME, VIRTUAL_IP FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d) VS 						 \n"
							+ " LEFT JOIN (SELECT INDEX, STATUS, VIRTUAL_PORT, VS_INDEX, REAL_PORT, POOL_INDEX FROM TMP_SLB_VS_SERVICE WHERE ADC_INDEX = %d) VSS \n"
							+ "     ON VS.INDEX = VSS.VS_INDEX                                                                                                   \n"
							+ " LEFT JOIN (SELECT INDEX, BAK_TYPE, BAK_ID, LB_METHOD, HEALTH_CHECK_INDEX, ALTEON_ID FROM TMP_SLB_POOL WHERE ADC_INDEX = %d) POOL \n"
							+ "     ON POOL.INDEX = VSS.POOL_INDEX                                                                          	                 \n"
							+ " LEFT JOIN (SELECT INDEX, ID_NEW FROM TMP_SLB_HEALTHCHECK WHERE ADC_INDEX = %d) HEALTHCHECK                                       \n"
							+ "     ON POOL.HEALTH_CHECK_INDEX = HEALTHCHECK.INDEX                                                                               \n"
							+ " LEFT JOIN (SELECT INDEX, POOL_INDEX, NODE_INDEX, PORT FROM TMP_SLB_POOLMEMBER WHERE ADC_INDEX = %d) MEMBER                       \n"
							+ "     ON MEMBER.POOL_INDEX = VSS.POOL_INDEX                                                                                        \n"
							+ " LEFT JOIN (SELECT INDEX, IP_ADDRESS, BAK_TYPE, BAK_ID, EXTRA FROM TMP_SLB_NODE WHERE ADC_INDEX = %d) NODE                        \n"
							+ "     ON MEMBER.NODE_INDEX = NODE.INDEX                                                                                            \n"
							+ " LEFT JOIN (SELECT VS_INDEX, POOLMEMBER_INDEX, MEMBER_STATUS, VSRC_INDEX FROM TMP_SLB_POOLMEMBER_STATUS ) MSTATUS                 \n"
							+ "     ON VS.INDEX = MSTATUS.VS_INDEX AND MEMBER.INDEX = MSTATUS.POOLMEMBER_INDEX  AND MSTATUS.VSRC_INDEX = VSS.INDEX               \n"
							+ " LEFT JOIN (SELECT INDEX, NAME, MODEL FROM MNG_ADC WHERE INDEX = %d) ADC 														 \n"
							+ "     ON ADC.INDEX = VS.ADC_INDEX ",
					adcIndex, adcIndex, adcIndex, adcIndex, adcIndex, adcIndex, adcIndex);

			String condition = "";

			if (searchKeys != null && !searchKeys.isEmpty()) {
				// #3984-2 #5: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKeys) + "%";
				String sqlSearch = String.format(
						" ( VS.NAME LIKE %s OR VS.VIRTUAL_IP LIKE %s OR NODE.IP_ADDRESS LIKE %s) ",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
				if (sqlSearch.length() > 0) {
					if (condition.length() > 0) {
						condition += (" AND " + sqlSearch);
					} else {
						condition += sqlSearch;
					}
				}
			}
			if (accountRole.equals("vsAdmin")) {
				if (condition.length() > 0) {
					condition += " AND ";
				}
				condition += String.format(
						" VS.INDEX IN (SELECT VS_INDEX FROM MNG_ACCNT_VS_MAP WHERE ADC_INDEX = %d AND ACCNT_INDEX = %d) ",
						adcIndex, accountIndex);
			}

			if (status != null) {
				if (condition.length() > 0) {
					condition += " AND ";
				}
				if (status == 0) {
					condition += String.format(" VSS.STATUS=%d ", status);
				} else {
					condition += String.format(" VSS.STATUS=%d ", status);
				}
			} else {
				if (condition.length() > 0) {
					condition += " AND ";
				}
				condition += String.format(" STATUS>%d  ", OBDefine.DATA_NOT_EXIST);
			}

			if (condition.length() > 0) {
				sqlText += (" WHERE " + condition);
			}

//            sqlText += " ORDER BY VS_NAME, VIRTUAL_IP, VIRTUAL_PORT ";
//            sqlText += " ORDER BY VIRTUAL_IP ASC NULLS LAST, VS_NAME DESC NULLS LAST, VIRTUAL_PORT DESC NULLS LAST ";
			sqlText += getNetworkMapOrderTypeList(orderOption);
			if (sqlLimit != null && !sqlLimit.isEmpty()) {
				sqlText += sqlLimit;
			}

			ResultSet rs = db.executeQuery(sqlText);
			String currentVsIndex = "";
			String currentVssIndex = "";
			String newVsIndex = "";
			String newVssIndex = "";
			OBDtoNetworkMap obj = null;
			ArrayList<OBDtoNetworkMapMember> memList = null;

			while (rs.next()) {
				newVsIndex = db.getString(rs, "VS_INDEX");
				newVssIndex = db.getString(rs, "SERVICE_INDEX");
				if (currentVsIndex.equals(newVsIndex) == false || currentVssIndex.equals(newVssIndex) == false) {
					if (obj != null) {
						obj.setMemberList(memList); // 모든 멤버 리스트를 vs에 붙인다.
						result.add(obj);
						obj = null;
						memList = null;
					}
					obj = new OBDtoNetworkMap();
					memList = new ArrayList<OBDtoNetworkMapMember>(); // 빈 memList를 만들어놓는다.
					obj.setVsIndex(newVsIndex);
					obj.setVsvcIndex(newVssIndex);
					obj.setServicePort(db.getInteger(rs, "VIRTUAL_PORT"));
					obj.setServiceStatus(db.getInteger(rs, "STATUS"));
					obj.setVsIPAddress(db.getString(rs, "VIRTUAL_IP"));
					obj.setAlteonId(db.getString(rs, "ALTEON_ID"));
					obj.setGroupId(db.getString(rs, "GROUP_ID"));
					obj.setRport(db.getString(rs, "RPORT"));
					obj.setModel(db.getString(rs, "MODEL"));
					obj.setHostName(db.getString(rs, "HOSTNAME"));
					obj.setVsName(db.getString(rs, "VS_NAME"));
					obj.setLbClass(OBDefine.ADC_LB_CLASS.SLB); // SLB virtual service 임을 표시한다.
					obj.setBackupStatus(OBDefine.STATE_DISABLE);
					obj.setGroupBakupType(db.getInteger(rs, "POOL_BAK_TYPE"));
					obj.setGroupBakupId(db.getString(rs, "POOL_BAK_ID"));
					obj.setLoadBalancingType(db.getInteger(rs, "LB_METHOD"));
					obj.setHealthCheckType(db.getString(rs, "ID_NEW"));

					// service 가 null일 (service 가 없는 vs 일때) 때는 다음 service 비교를 위해 ""로 set
					if (newVssIndex == null) {
						currentVssIndex = "";
					} else {
						currentVssIndex = newVssIndex;
					}
					currentVsIndex = newVsIndex;
				}

				// 같은 vss인데, 다른 멤버데이터이다.
				if (obj != null && memList != null && db.getString(rs, "NODE_IP_ADDRESS") != null) {
					String memberPort = "";
					OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
					String[] verElements = adcInfo.getSwVersion().split("\\."); // 버전 요소 두개까지 문자열로 건진다. 지금은 하나만 쓴다.
					if (Integer.parseInt(verElements[0]) >= 30
							|| (Integer.parseInt(verElements[0]) == 29 && Integer.parseInt(verElements[1]) == 5)) {
						memberPort = OBParser.extraAddPort29V(db.getString(rs, "NODE_EXTRA"));
					} else {
						memberPort = OBParser.extraAddPort(db.getString(rs, "NODE_EXTRA"));
					}
					OBDtoNetworkMapMember mem = new OBDtoNetworkMapMember();
					mem.setIpAddress(db.getString(rs, "NODE_IP_ADDRESS"));
					mem.setNodeIndex(db.getString(rs, "NODE_INDEX"));
					mem.setAddPort(memberPort);
					mem.setPort(db.getInteger(rs, "MEMBER_PORT"));

					mem.setBakupType(db.getInteger(rs, "NODE_BAK_TYPE"));
					mem.setBakupIndex(db.getString(rs, "NODE_BAK_ID"));
					if (obj.getServiceStatus() == OBDefine.VS_STATUS.DISABLE) // virtual service가 disable이면 member도
																				// disable로 표시
					{
						mem.setStatus(OBDefine.MEMBER_STATUS.DISABLE);
					} else {
						mem.setStatus(db.getInteger(rs, "MEMBER_STATUS"));
					}
					memList.add(mem);
				}
			}

			if (obj != null) // 마지막 vs 종결 처리한다.
			{
				obj.setMemberList(memList); // 모은 멤버리스트를 vs에 붙인다.
				result.add(obj);
				obj = null;
				memList = null;
			}

			ArrayList<OBDtoNetworkMapBackup> backupMem = null;
			if (result != null) {
				ArrayList<OBDtoNetworkMapBackup> backupList = getNetworkMapsAlteonBackup(adcIndex, db);

				for (OBDtoNetworkMap objList : result) {
					backupMem = new ArrayList<OBDtoNetworkMapBackup>();
					int backupListSize = backupList.size();
					int memberListSize = objList.getMemberList().size();
					for (int i = 0; i < backupListSize; i++) {
						if (objList.getGroupBakupType() == OBDefine.BACKUP_STATE.GROUPBAK
								&& objList.getGroupBakupId().equals(backupList.get(i).getPoolIndex())
								&& objList.getGroupBakupType().equals(backupList.get(i).getBakType())) {
							if (backupList.get(i).getStatus() == OBDefine.STATE_ENABLE) {
								objList.setBackupStatus(OBDefine.STATE_ENABLE);
							}
							backupMem.add(backupList.get(i));
						}
						if (objList.getGroupBakupType() == OBDefine.BACKUP_STATE.REALBAK
								&& objList.getGroupBakupId().equals(backupList.get(i).getNodeIndex())
								&& objList.getGroupBakupType().equals(backupList.get(i).getBakType())) {
							if (backupList.get(i).getStatus() == OBDefine.STATE_ENABLE) {
								objList.setBackupStatus(OBDefine.STATE_ENABLE);
							}
							backupList.get(i).setIdType(OBDefine.BACKUP_STATE.REALBAK);
							backupMem.add(backupList.get(i));
						}
						for (int j = 0; j < memberListSize; j++) {
							if (objList.getMemberList().get(j).getBakupType() == OBDefine.BACKUP_STATE.REALBAK
									&& objList.getMemberList().get(j).getBakupIndex()
											.equals(backupList.get(i).getNodeIndex())
									&& objList.getMemberList().get(j).getBakupType()
											.equals(backupList.get(i).getBakType())) {
								backupMem.add(backupList.get(i));
							}
						}
					}
					objList.setBackupList(backupMem);
				}
			}
			// obj.setMemberList(getNetworkMapMembers(adcIndex, obj.getVsIndex(),
			// serviceIndex, obj.getServiceStatus()));
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

	private String getNetworkMapOrderTypeList(OBDtoOrdering orderObj) throws OBException {
		if (orderObj == null)
			return "";

		String retVal = " ORDER BY VS_NAME DESC NULLS LAST, VIRTUAL_IP ASC NULLS LAST, VIRTUAL_PORT ASC NULLS LAST, VS_INDEX ASC NULLS LAST, NODE_IP_ADDRESS ASC NULLS LAST ";
		int orderDir = orderObj.getOrderDirection();
		switch (orderObj.getOrderType()) {
		case OBDtoOrdering.TYPE_2SECOND:// vip
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY VIRTUAL_IP ASC NULLS LAST, VS_NAME DESC NULLS LAST, VIRTUAL_PORT DESC NULLS LAST, VS_INDEX ASC NULLS LAST, NODE_IP_ADDRESS ASC NULLS LAST ";
			else
				retVal = " ORDER BY VIRTUAL_IP DESC NULLS LAST, VS_NAME DESC NULLS LAST, VIRTUAL_PORT DESC NULLS LAST, VS_INDEX ASC NULLS LAST, NODE_IP_ADDRESS ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_3THIRD:// service port
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY VIRTUAL_PORT ASC NULLS LAST, VS_NAME DESC NULLS LAST, VIRTUAL_IP DESC NULLS LAST, VS_INDEX ASC NULLS LAST, NODE_IP_ADDRESS ASC NULLS LAST ";
			else
				retVal = " ORDER BY VIRTUAL_PORT DESC NULLS LAST, VS_NAME DESC NULLS LAST, VIRTUAL_IP DESC NULLS LAST, VS_INDEX ASC NULLS LAST, NODE_IP_ADDRESS ASC NULLS LAST ";
			break;
		}

		return retVal;
	}

	private ArrayList<OBDtoNetworkMapBackup> getNetworkMapsAlteonBackup(Integer adcIndex, OBDatabase db)
			throws OBException {
		ArrayList<OBDtoNetworkMapBackup> result = new ArrayList<OBDtoNetworkMapBackup>();
		String sqlText = "";
		try {
			sqlText = String.format(
					" SELECT POOL.BAK_ID POOL_BAK_ID, NODE.INDEX NODE_INDEX, NODE.IP_ADDRESS NODE_IP_ADDRESS, NODE.STATUS NODE_STATUS 	"
							+ " FROM (SELECT INDEX, BAK_TYPE, BAK_ID, STATUS FROM TMP_SLB_POOL WHERE ADC_INDEX = %d AND BAK_TYPE = %d) POOL		"
							+ " LEFT JOIN (SELECT NODE_INDEX, POOL_INDEX FROM TMP_SLB_POOLMEMBER WHERE ADC_INDEX = %d) MEMBER						"
							+ " ON MEMBER.POOL_INDEX = POOL.BAK_ID																				"
							+ " INNER JOIN (SELECT INDEX, IP_ADDRESS, BAK_ID, STATUS FROM TMP_SLB_NODE WHERE ADC_INDEX = %d) NODE					"
							+ " ON MEMBER.NODE_INDEX = NODE.INDEX ORDER BY NODE_INDEX, POOL_BAK_ID												",
					adcIndex, OBDefine.BACKUP_STATE.GROUPBAK, adcIndex, adcIndex);
			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoNetworkMapBackup backupList = new OBDtoNetworkMapBackup();
				backupList.setBakType(OBDefine.BACKUP_STATE.GROUPBAK);
				backupList.setPoolIndex(db.getString(rs, "POOL_BAK_ID"));
				backupList.setNodeIndex(db.getString(rs, "NODE_INDEX"));
				backupList.setIpAddress(db.getString(rs, "NODE_IP_ADDRESS"));
				backupList.setStatus(db.getInteger(rs, "NODE_STATUS"));
				result.add(backupList);
			}

			sqlText = String.format(
					" SELECT BAK_ID, INDEX, IP_ADDRESS, STATUS 													"
							+ " FROM TMP_SLB_NODE WHERE ADC_INDEX = %d AND NOT BAK_TYPE = %d								"
							+ " OR INDEX IN (SELECT BAK_ID FROM TMP_SLB_NODE WHERE ADC_INDEX = %d AND NOT BAK_TYPE = %d) 	"
							+ " OR INDEX IN (SELECT BAK_ID FROM TMP_SLB_POOL WHERE ADC_INDEX = %d AND BAK_TYPE = %d) 		",
					adcIndex, OBDefine.BACKUP_STATE.EMPTY, adcIndex, OBDefine.BACKUP_STATE.EMPTY, adcIndex,
					OBDefine.BACKUP_STATE.REALBAK);
			ResultSet rsNode = db.executeQuery(sqlText);
			while (rsNode.next()) {
				OBDtoNetworkMapBackup backupList = new OBDtoNetworkMapBackup();
				backupList.setBakType(OBDefine.BACKUP_STATE.REALBAK);
				backupList.setPoolIndex(db.getString(rsNode, "BAK_ID"));
				backupList.setNodeIndex(db.getString(rsNode, "INDEX"));
				backupList.setIpAddress(db.getString(rsNode, "IP_ADDRESS"));
				backupList.setStatus(db.getInteger(rsNode, "STATUS"));
				result.add(backupList);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return result;
	}

	private ArrayList<OBDtoNetworkMap> getNetworkMapsAlteonFlbGroup(Integer adcIndex, Integer status, String searchKeys,
			Integer beginIndex, Integer endIndex, OBDtoOrdering orderOption) throws OBException {// FLB 모니터링한다고 지정한
																									// group들 상태를 구한다.
		ArrayList<OBDtoNetworkMap> result = new ArrayList<OBDtoNetworkMap>();
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			int offset = 0;
			if (beginIndex != null)
				offset = beginIndex.intValue();

			int limit = 0;
			String sqlLimit = "";
			if (endIndex != null) {
				limit = Math.abs(endIndex.intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}

			sqlText = String.format(
					" SELECT GR.INDEX GROUP_INDEX, GR.ALTEON_ID GROUP_ID, GR.NAME GROUP_NAME, GR.STATUS GROUP_STATUS,     				    \n"
							+ " GR.BAK_TYPE GROUP_BAK_TYPE, GR.BAK_ID GROUP_BAK_ID, 												  				\n"
							+ " MEMBER.NODE_INDEX REAL_INDEX, NODE.IP_ADDRESS REAL_IP, NODE.NAME REAL_NAME, NODE.STATUS REAL_STATUS,				\n"
							+ " NODE.BAK_TYPE REAL_BAK_TYPE, NODE.BAK_ID REAL_BAK_ID																\n"
							+ " FROM (SELECT INDEX, ALTEON_ID, NAME, STATUS, BAK_TYPE, BAK_ID FROM TMP_SLB_POOL                     				\n"
							+ "    WHERE INDEX IN (SELECT GROUP_INDEX FROM MNG_FLB_GROUP WHERE ADC_INDEX = %d)) GR                  				\n"
							+ " LEFT JOIN (SELECT NODE_INDEX, POOL_INDEX FROM TMP_SLB_POOLMEMBER WHERE ADC_INDEX = %d) MEMBER       				\n"
							+ "    ON GR.INDEX = MEMBER.POOL_INDEX                                                                  				\n"
							+ " LEFT JOIN (SELECT INDEX, IP_ADDRESS, NAME, BAK_ID, BAK_TYPE, STATUS FROM TMP_SLB_NODE WHERE ADC_INDEX = %d) NODE    \n"
							+ "    ON MEMBER.NODE_INDEX = NODE.INDEX                                                                				\n",
					adcIndex, adcIndex, adcIndex);

			String condition = "";
			if (status != null) {
				condition += String.format(" GR.STATUS=%d ", status);
			}
			if (searchKeys != null && !searchKeys.isEmpty()) {
				// #3984-2 #5: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKeys) + "%";
				String sqlSearch = String.format(
						" ( GR.NAME LIKE %s OR CAST(GR.ALTEON_ID AS VARCHAR) LIKE %s OR NODE.IP_ADDRESS LIKE %s) ",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
				if (sqlSearch.length() > 0) {
					if (condition.length() > 0) {
						condition += (" AND " + sqlSearch);
					} else {
						condition += sqlSearch;
					}
				}
			}
			if (condition.length() > 0) {
				sqlText += (" WHERE " + condition);
			}

			sqlText += " ORDER BY GROUP_ID, GROUP_NAME ";
//            sqlText += getNetworkMapAlteonFlbOrderTypeList(orderOption);
			if (sqlLimit != null && !sqlLimit.isEmpty()) {
				sqlText += sqlLimit;
			}

			ResultSet rs = db.executeQuery(sqlText);
			String currentGroupIndex = "";
			String newGroupIndex = "";
			OBDtoNetworkMap obj = null;
			ArrayList<OBDtoNetworkMapMember> memList = null;

			while (rs.next()) {
				newGroupIndex = db.getString(rs, "GROUP_INDEX");
				if (currentGroupIndex.equals(newGroupIndex) == false) // 새 group
				{
					if (obj != null) {
						obj.setMemberList(memList); // 멤버 리스트를 group에 붙인다.
						result.add(obj);
						obj = null;
						memList = null;
					}
					obj = new OBDtoNetworkMap();
					memList = new ArrayList<OBDtoNetworkMapMember>(); // 빈 memList를 만들어놓는다.
					obj.setVsIndex(newGroupIndex); // GROUP INDEX
					obj.setServicePort(-1);
					obj.setServiceStatus(db.getInteger(rs, "GROUP_STATUS"));
					obj.setVsIPAddress("");
					obj.setGroupBakupType(db.getInteger(rs, "GROUP_BAK_TYPE"));
					obj.setGroupBakupId(db.getString(rs, "GROUP_BAK_ID"));
					obj.setBackupStatus(OBDefine.STATE_DISABLE);
					if (!db.getString(rs, "GROUP_NAME").isEmpty()) {
						obj.setVsName(db.getString(rs, "GROUP_ID") + " : " + db.getString(rs, "GROUP_NAME"));
					} else
						obj.setVsName(db.getString(rs, "GROUP_ID"));
					obj.setLbClass(OBDefine.ADC_LB_CLASS.FLB); // flb group이다.

					currentGroupIndex = newGroupIndex;
				}

				// 같은 group의 다른 real
				if (obj != null && memList != null && db.getString(rs, "REAL_IP") != null) {
					OBDtoNetworkMapMember mem = new OBDtoNetworkMapMember();
					mem.setIpAddress(db.getString(rs, "REAL_IP"));
					mem.setNodeIndex(db.getString(rs, "REAL_INDEX"));
					mem.setBakupType(db.getInteger(rs, "REAL_BAK_TYPE"));
					mem.setBakupIndex(db.getString(rs, "REAL_BAK_ID"));
					mem.setPort(-1);
					mem.setStatus(db.getInteger(rs, "REAL_STATUS"));
					memList.add(mem);
				}
			}

			if (obj != null) // 마지막 vs 종결 처리한다.
			{
				obj.setMemberList(memList); // 모은 멤버리스트를 vs에 붙인다.
				result.add(obj);
				obj = null;
				memList = null;
			}
			ArrayList<OBDtoNetworkMapBackup> backupMem = null;
			if (result != null) {
				ArrayList<OBDtoNetworkMapBackup> backupList = getNetworkMapsAlteonBackup(adcIndex, db);

				for (OBDtoNetworkMap objList : result) {
					backupMem = new ArrayList<OBDtoNetworkMapBackup>();
					int backupListSize = backupList.size();
					int memberListSize = objList.getMemberList().size();
					for (int i = 0; i < backupListSize; i++) {
						if (objList.getGroupBakupType() == OBDefine.BACKUP_STATE.GROUPBAK
								&& objList.getGroupBakupId().equals(backupList.get(i).getPoolIndex())) {
							if (backupList.get(i).getStatus() == OBDefine.STATE_ENABLE) {
								objList.setBackupStatus(OBDefine.STATE_ENABLE);
							}
							backupMem.add(backupList.get(i));
						}
						if (objList.getGroupBakupType() == OBDefine.BACKUP_STATE.REALBAK
								&& objList.getGroupBakupId().equals(backupList.get(i).getNodeIndex())) {
							if (backupList.get(i).getStatus() == OBDefine.STATE_ENABLE) {
								objList.setBackupStatus(OBDefine.STATE_ENABLE);
							}
							backupList.get(i).setIdType(OBDefine.BACKUP_STATE.REALBAK);
							backupMem.add(backupList.get(i));
						}
						for (int j = 0; j < memberListSize; j++) {
							if (objList.getMemberList().get(j).getBakupType() == OBDefine.BACKUP_STATE.REALBAK
									&& objList.getMemberList().get(j).getBakupIndex()
											.equals(backupList.get(i).getNodeIndex())) {
								backupMem.add(backupList.get(i));
							}
						}
					}
					objList.setBackupList(backupMem);
				}
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
		return result;
	}

//    private String getNetworkMapAlteonFlbOrderTypeList(OBDtoOrdering orderObj) throws OBException
//    {
//        if(orderObj == null)
//            return "";
//        
//        String retVal = " ORDER BY GROUP_ID, GROUP_NAME ";
//        int orderDir = orderObj.getOrderDirection();
//        switch(orderObj.getOrderType())
//        {
//        case OBDtoOrdering.TYPE_2SECOND:
//            if(orderDir == OBDefine.ORDER_DIR_ASCEND)
//                retVal = "";
//            else
//                retVal = "";
//            break;
//        case OBDtoOrdering.TYPE_3THIRD:
//            if(orderDir == OBDefine.ORDER_DIR_DESCEND)
//                retVal = "";
//            else
//                retVal = "";
//            break;
//        }
//        return retVal;
//    }

	// public static void main(String[] args)
	// {
	// OBMonitoringImpl mon = new OBMonitoringImpl();
	//
	// try
	// {
	// ArrayList<OBDtoNetworkMap> list = mon.getNetworkMaps(1, null, null, 0, 100);
	// for(OBDtoNetworkMap map: list)
	// System.out.println(map);
	//
	// System.out.println(list.size());
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

//    public static void main(String[] args)
//    {
//        OBMonitoringImpl me = new OBMonitoringImpl();
//
//        // me.testGetNetworkMapsNew();
//        // me.testGetAlertLog();
//        // me.testGetTotalAdcList(); //전체 모니터링 - ADC
//        // me.testGetTotalServiceList(); //전체 모니터링 - virtual server
//        // me.testGetTotalGroupList(); //전체 모니터링 - group
//        // me.testGetTotalRealList(); //전체 모니터링 - real
//
//        Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
//        Timestamp upTime = new Timestamp(1421741435915L - 4612184090L);
//        System.out.println(String.format("date = %s", OBDateTime.toString(upTime)));
//        // info.setUpTime(upTime);
//    }

	@Override
	public ArrayList<OBDtoNetworkMap> getNetworkMapsNew(Integer adcIndex, Integer lbClass, Integer status,
			String searchKeys, Integer fromIndex, Integer endIndex, Integer accountIndex, String accountRole,
			OBDtoOrdering orderOption) throws OBException { // lbClass - All, FLB, SLB : 알테온만 유효
		ArrayList<OBDtoNetworkMap> result;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5) {// virtual server, poolmember로 구성한다.
				result = getNetworkMapsF5(adcIndex, status, searchKeys, fromIndex, endIndex, accountIndex, accountRole,
						orderOption);
				result = (ArrayList<OBDtoNetworkMap>) appendVsDescription(adcIndex, adcInfo.getAdcType(), result);
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {// virtual service, poolmember로 구성한다.
				result = getNetworkMapsAlteon(adcIndex, lbClass, status, searchKeys, fromIndex, endIndex, accountIndex,
						accountRole, orderOption);
				result = (ArrayList<OBDtoNetworkMap>) appendVsDescription(adcIndex, adcInfo.getAdcType(), result);
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS) {// virtual service, poolmember로 구성한다.
				result = getNetworkMapsF5(adcIndex, status, searchKeys, fromIndex, endIndex, accountIndex, accountRole,
						orderOption);
				result = (ArrayList<OBDtoNetworkMap>) appendVsDescription(adcIndex, adcInfo.getAdcType(), result);
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {// virtual service, poolmember로 구성한다.
				result = getNetworkMapsF5(adcIndex, status, searchKeys, fromIndex, endIndex, accountIndex, accountRole,
						orderOption);
				result = (ArrayList<OBDtoNetworkMap>) appendVsDescription(adcIndex, adcInfo.getAdcType(), result);
			} else {
				result = new ArrayList<OBDtoNetworkMap>();
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		return result;
	}

	private List<OBDtoNetworkMap> appendVsDescription(Integer adcIndex, Integer adcType,
			List<OBDtoNetworkMap> networkMapList) throws Exception {
		Map<String, ServiceMapVsDescDto> descriptionMap = getVsDescriptionInfoList(adcIndex);
		for (OBDtoNetworkMap obj : networkMapList) {
			String index = getPrimaryIndex(adcType, obj.getVsIndex(), obj.getVsvcIndex());
			String description = "";
			if (descriptionMap.get(index) != null) {
				description = descriptionMap.get(index).getDescription();
			}
			obj.setVsDescription(description);
		}
		return networkMapList;
	}

	@Override
	public OBDtoConnectionData getVSRealTimeCurrConns(Integer adcIndex, String vsIndex, Integer srvPort)
			throws OBException {
		OBDatabase db = new OBDatabase();

		OBDtoConnectionData result;
		try {
			db.openDB();
			int type = new OBAdcManagementImpl().getAdcType(adcIndex);
			if (type == OBDefine.ADC_TYPE_ALTEON) {
				String vsID = new OBVServerDB().getVServerID(vsIndex);
				if (vsID == null)
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);// , String.format("invalid
																					// vsIndex(%s). failed to found the
																					// vs id.", vsIndex));
				result = getVSRealTimeCurrConnsAlteon(adcIndex, vsID, srvPort);
			} else if (type == OBDefine.ADC_TYPE_F5) {
				String vsName = new OBVServerDB().getVServerName(vsIndex);
				if (vsName == null)
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);// , String.format("invalid
																					// vsIndex(%s). failed to found the
																					// vs name.", vsIndex));
				result = getVSRealTimeCurrConnsF5(adcIndex, vsName);
			} else if (type == OBDefine.ADC_TYPE_PIOLINK_PAS) {
				String vsName = new OBVServerDB().getVServerName(vsIndex);
				if (vsName == null)
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);// , String.format("invalid
																					// vsIndex(%s). failed to found the
																					// vs name.", vsIndex));
				result = getVSRealTimeCurrConnsPAS(adcIndex, vsName);
			} else if (type == OBDefine.ADC_TYPE_PIOLINK_PASK) {
				String vsName = new OBVServerDB().getVServerName(vsIndex);
				if (vsName == null)
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);// , String.format("invalid
																					// vsIndex(%s). failed to found the
																					// vs name.", vsIndex));
				result = getVSRealTimeCurrConnsPASK(adcIndex, vsName);
			} else {
				throw new OBException(OBException.ERRCODE_NOT_SUPPORT_VENDOR);// , String.format("invalid vendor
																				// type:%d", type));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s", e.getMessage()));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
		return result;
	}

	private OBDtoConnectionData getVSRealTimeCurrConnsF5(Integer adcIndex, String vsName) throws OBException {// alteon인
																												// 경우에는
																												// vsIndex는
																												// virtual
																												// service의
																												// 인덱스..
		OBDtoConnectionData result;

		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			// virtual server, poolmember로 구성한다.
			OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());// new OBSnmpF5(adcInfo.getAdcIpAddress());
			result = snmp.getVServerConns(adcInfo.getAdcType(), adcInfo.getSwVersion(), vsName);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return result;
	}

	private OBDtoConnectionData getVSRealTimeCurrConnsPAS(Integer adcIndex, String vsName) throws OBException {// alteon인
																												// 경우에는
																												// vsIndex는
																												// virtual
																												// service의
																												// 인덱스..
		OBDtoConnectionData result;

		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			// virtual server, poolmember로 구성한다.
			OBSnmpPAS snmp = new OBSnmpPAS(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
			result = snmp.getVServerConns(adcInfo.getAdcType(), adcInfo.getSwVersion(), vsName);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return result;
	}

	private OBDtoConnectionData getVSRealTimeCurrConnsPASK(Integer adcIndex, String vsName) throws OBException {
		OBDtoConnectionData result;

		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			// virtual server, poolmember로 구성한다.
			OBSnmpPASK snmp = new OBSnmpPASK(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
			result = snmp.getVServerConns(adcInfo.getAdcType(), adcInfo.getSwVersion(), vsName);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return result;
	}

	// public static void main(String[] args)
	// {
	// OBMonitoringImpl mon = new OBMonitoringImpl();
	//
	// try
	// {
	// for(int i=0;i<100;i++)
	// {
	// OBDtoConnectionInfo info = mon.getVSRealTimeCurrConnsAlteon(3, 122, 23);
	// System.out.println(info);
	// OBDateTime.Sleep(5000);
	// }
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	private OBDtoConnectionData getVSRealTimeCurrConnsAlteon(Integer adcIndex, String vsID, Integer virtPort)
			throws OBException {// alteon인 경우에는 vsIndex는 virtual service의 인덱스..
		OBDtoConnectionData result;

		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			// virtual server, poolmember로 구성한다.
			OBSnmpAlteon snmp = OBCommon.getValidSnmpAlteonHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());
			result = snmp.getVServiceConns(adcInfo.getAdcType(), adcInfo.getSwVersion(), vsID, virtPort);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return result;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDtoConnectionInfo list = new OBMonitoringImpl().getVSConnections(3,
	// "3_vs_210.118.63.0", null, null, null);
	// System.out.println(list);
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public OBDtoConnectionInfo getVSConnections(Integer adcIndex, String vsIndex, Integer srvPort, Date beginTime,
			Date endTime) throws OBException {
		OBDtoConnectionInfo result;

		try {
			int type = new OBAdcManagementImpl().getAdcType(adcIndex);
			if (type == OBDefine.ADC_TYPE_ALTEON) {
				String vsID = new OBVServerDB().getVServerID(vsIndex);
				if (vsID == null)
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);
				result = getVSConnectionsAlteon(adcIndex, vsIndex, vsID, srvPort, beginTime, endTime);
			} else if (type == OBDefine.ADC_TYPE_F5) {
				result = getVSConnectionsF5(adcIndex, vsIndex, beginTime, endTime);
			} else if (type == OBDefine.ADC_TYPE_PIOLINK_PAS) {
				result = getVSConnectionsPAS(adcIndex, vsIndex, beginTime, endTime);
			} else if (type == OBDefine.ADC_TYPE_PIOLINK_PASK) {
				result = getVSConnectionsPASK(adcIndex, vsIndex, beginTime, endTime);
			} else {
				throw new OBException(OBException.ERRCODE_NOT_SUPPORT_VENDOR);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return result;
	}

	private OBDtoConnectionInfo getVSConnectionsF5(Integer adcIndex, String vsIndex, Date beginTime, Date endTime)
			throws OBException {
		OBDtoConnectionInfo result = new OBDtoConnectionInfo();

		ArrayList<OBDtoAdcConfigHistory> confEventList = new OBAdcConfigHistoryImpl()
				.getVsConfigHistoryListNoPagingNoSearch(adcIndex, vsIndex, beginTime, endTime);
		ArrayList<OBDtoConnectionData> data = new ArrayList<OBDtoConnectionData>();

		result.setConfEventList(confEventList);
		result.setData(data);

		OBDatabase db = new OBDatabase();
		String sqlText = "";
		String sqlTime = "";
		try {
			db.openDB();

			if (endTime == null)
				endTime = new Date();

			if (beginTime == null)
				beginTime = new Date(endTime.getTime() - 7 * 24 * 60 * 60 * 1000);// 7일전 시간.

			sqlTime = String.format(" OCCUR_TIME <= %s AND OCCUR_TIME >= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))),
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			sqlText = String.format(
					" SELECT OCCUR_TIME, CONN_CURR, CONN_MAX, CONN_TOT \n"
							+ " FROM LOG_SVC_PERF_STATS                               \n"
							+ " WHERE ADC_INDEX=%d AND VS_INDEX=%s AND %s          \n" + " ORDER BY OCCUR_TIME ASC ;",
					adcIndex, OBParser.sqlString(vsIndex), sqlTime);

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			ResultSet rs = db.executeQuery(sqlText);

			long minConns = 0x7FFFFFFFL;
			long maxConns = 0;
			Date minDate = null;
			Date maxDate = null;
			long logCnt = 0;
			long totConns = 0;
			if (rs.next() == false) {// 데이터 없는 경우.
				result.setAvgConns(new Long(0));
				result.setMaxConns(new Long(0));
				result.setMaxDate(null);
				result.setMinConns(new Long(0));
				result.setMinDate(null);
			} else {
				do {
					OBDtoConnectionData info = new OBDtoConnectionData();
					info.setCurConns(db.getLong(rs, "CONN_CURR"));
					info.setMaxConns(db.getLong(rs, "CONN_MAX"));
					info.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
					info.setTotConns(db.getLong(rs, "CONN_TOT"));
					data.add(info);
					if (minConns >= info.getCurConns()) {
						minConns = info.getCurConns();
						minDate = info.getOccurTime();
					}
					if (maxConns <= info.getCurConns()) {
						maxConns = info.getCurConns();
						maxDate = info.getOccurTime();
					}
					totConns += info.getCurConns();
					logCnt++;
					// System.out.println(DateFormat.getDateTimeInstance( DateFormat.LONG,
					// DateFormat.LONG).format(info.getOccurTime()));
				} while (rs.next());

				if (totConns > 0)
					result.setAvgConns(totConns / logCnt);
				else
					result.setAvgConns(totConns);
				result.setMaxConns(maxConns);
				result.setMaxDate(maxDate);
				result.setMinConns(minConns);
				result.setMinDate(minDate);
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
		return result;
	}

	private OBDtoConnectionInfo getVSConnectionsPAS(Integer adcIndex, String vsIndex, Date beginTime, Date endTime)
			throws OBException {
		OBDtoConnectionInfo result = new OBDtoConnectionInfo();

		ArrayList<OBDtoAdcConfigHistory> confEventList = new OBAdcConfigHistoryImpl()
				.getVsConfigHistoryListNoPagingNoSearch(adcIndex, vsIndex, beginTime, endTime);
		ArrayList<OBDtoConnectionData> data = new ArrayList<OBDtoConnectionData>();

		result.setConfEventList(confEventList);
		result.setData(data);

		OBDatabase db = new OBDatabase();
		String sqlText = "";
		String sqlTime = "";
		try {
			db.openDB();
			if (endTime == null)
				endTime = new Date();

			if (beginTime == null)
				beginTime = new Date(endTime.getTime() - 7 * 24 * 60 * 60 * 1000);// 7일전 시간.

			sqlTime = String.format(" OCCUR_TIME <= %s AND OCCUR_TIME >= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))),
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			sqlText = String.format(
					" SELECT OCCUR_TIME, CONN_CURR, CONN_MAX, CONN_TOT \n"
							+ " FROM LOG_SVC_PERF_STATS                               \n"
							+ " WHERE ADC_INDEX=%d AND VS_INDEX=%s AND %s           \n" + " ORDER BY OCCUR_TIME ASC ;",
					adcIndex, OBParser.sqlString(vsIndex), sqlTime);

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			ResultSet rs = db.executeQuery(sqlText);

			long minConns = 0x7FFFFFFFL;
			long maxConns = 0;
			Date minDate = null;
			Date maxDate = null;
			long logCnt = 0;
			long totConns = 0;
			if (rs.next() == false) {// 데이터 없는 경우.
				result.setAvgConns(new Long(0));
				result.setMaxConns(new Long(0));
				result.setMaxDate(null);
				result.setMinConns(new Long(0));
				result.setMinDate(null);
			} else {
				do {
					OBDtoConnectionData info = new OBDtoConnectionData();
					info.setCurConns(db.getLong(rs, "CONN_CURR"));
					info.setMaxConns(db.getLong(rs, "CONN_MAX"));
					info.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
					info.setTotConns(db.getLong(rs, "CONN_TOT"));
					data.add(info);
					if (minConns >= info.getCurConns()) {
						minConns = info.getCurConns();
						minDate = info.getOccurTime();
					}
					if (maxConns <= info.getCurConns()) {
						maxConns = info.getCurConns();
						maxDate = info.getOccurTime();
					}
					totConns += info.getCurConns();
					logCnt++;
					// System.out.println(DateFormat.getDateTimeInstance( DateFormat.LONG,
					// DateFormat.LONG).format(info.getOccurTime()));
				} while (rs.next());

				if (totConns > 0)
					result.setAvgConns(totConns / logCnt);
				else
					result.setAvgConns(totConns);
				result.setMaxConns(maxConns);
				result.setMaxDate(maxDate);
				result.setMinConns(minConns);
				result.setMinDate(minDate);
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
		return result;
	}

	private OBDtoConnectionInfo getVSConnectionsPASK(Integer adcIndex, String vsIndex, Date beginTime, Date endTime)
			throws OBException {
		OBDtoConnectionInfo result = new OBDtoConnectionInfo();

		ArrayList<OBDtoAdcConfigHistory> confEventList = new OBAdcConfigHistoryImpl()
				.getVsConfigHistoryListNoPagingNoSearch(adcIndex, vsIndex, beginTime, endTime);
		ArrayList<OBDtoConnectionData> data = new ArrayList<OBDtoConnectionData>();

		result.setConfEventList(confEventList);
		result.setData(data);

		OBDatabase db = new OBDatabase();
		String sqlText = "";
		String sqlTime = "";
		try {
			db.openDB();
			if (endTime == null)
				endTime = new Date();

			if (beginTime == null)
				beginTime = new Date(endTime.getTime() - 7 * 24 * 60 * 60 * 1000);// 7일전 시간.

			sqlTime = String.format(" OCCUR_TIME <= %s AND OCCUR_TIME >= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))),
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			sqlText = String.format(
					" SELECT OCCUR_TIME, CONN_CURR, CONN_MAX, CONN_TOT \n"
							+ " FROM LOG_SVC_PERF_STATS                               \n"
							+ " WHERE ADC_INDEX=%d AND VS_INDEX=%s AND %s          \n" + " ORDER BY OCCUR_TIME ASC ;",
					adcIndex, OBParser.sqlString(vsIndex), sqlTime);

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			ResultSet rs = db.executeQuery(sqlText);

			long minConns = 0x7FFFFFFFL;
			long maxConns = 0;
			Date minDate = null;
			Date maxDate = null;
			long logCnt = 0;
			long totConns = 0;
			if (rs.next() == false) {// 데이터 없는 경우.
				result.setAvgConns(new Long(0));
				result.setMaxConns(new Long(0));
				result.setMaxDate(null);
				result.setMinConns(new Long(0));
				result.setMinDate(null);
			} else {
				do {
					OBDtoConnectionData info = new OBDtoConnectionData();
					info.setCurConns(db.getLong(rs, "CONN_CURR"));
					info.setMaxConns(db.getLong(rs, "CONN_MAX"));
					info.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
					info.setTotConns(db.getLong(rs, "CONN_TOT"));
					data.add(info);
					if (minConns >= info.getCurConns()) {
						minConns = info.getCurConns();
						minDate = info.getOccurTime();
					}
					if (maxConns <= info.getCurConns()) {
						maxConns = info.getCurConns();
						maxDate = info.getOccurTime();
					}
					totConns += info.getCurConns();
					logCnt++;
					// System.out.println(DateFormat.getDateTimeInstance( DateFormat.LONG,
					// DateFormat.LONG).format(info.getOccurTime()));
				} while (rs.next());

				if (totConns > 0)
					result.setAvgConns(totConns / logCnt);
				else
					result.setAvgConns(totConns);
				result.setMaxConns(maxConns);
				result.setMaxDate(maxDate);
				result.setMinConns(minConns);
				result.setMinDate(minDate);
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
		return result;
	}

	// public static void main(String[] args)
	// {
	// OBMonitoringImpl mon = new OBMonitoringImpl();
	//
	// try
	// {
	// OBDtoConnectionInfo list = mon.getVSConnectionsAlteon(1, 1, 2, null, null);
	// System.out.println(list);
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	private OBDtoConnectionInfo getVSConnectionsAlteon(Integer adcIndex, String vsIndex, String vsID, Integer virtPort,
			Date beginTime, Date endTime) throws OBException {
		OBDtoConnectionInfo result = new OBDtoConnectionInfo();

		// 이력은 VS단위로 관리하기 때문에 Alteon에서 뽑는 connection이 vservice단위지만 vs의 이력을 가져온다.
		ArrayList<OBDtoAdcConfigHistory> confEventList = new OBAdcConfigHistoryImpl()
				.getVsConfigHistoryListNoPagingNoSearch(adcIndex, vsIndex, beginTime, endTime);
		ArrayList<OBDtoConnectionData> data = new ArrayList<OBDtoConnectionData>();

		result.setConfEventList(confEventList);
		result.setData(data);

		OBDatabase db = new OBDatabase();
		String sqlText = "";
		String sqlTime = "";
		try {
			db.openDB();

			if (endTime == null)
				endTime = new Date();
			if (beginTime == null) {
				beginTime = new Date(endTime.getTime() - 7 * 24 * 60 * 60 * 1000);// 7일전 시간.
			}
			sqlTime = String.format(" OCCUR_TIME <= %s  AND OCCUR_TIME >= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))),
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			String vsvcIndex = OBCommon.makeVSvcIndex(adcIndex, vsIndex, virtPort);
			sqlText = String.format(" SELECT OCCUR_TIME, CONN_CURR, CONN_MAX, CONN_TOT    \n"
					+ " FROM LOG_SVC_PERF_STATS                       \n"
					+ " WHERE ADC_INDEX=%d                                  \n"
					+ "     AND OBJ_INDEX = %s                              \n"
					+ "     AND %s                                          \n" + // time
																					// range
					" ORDER BY OCCUR_TIME ASC ;                            ", adcIndex, OBParser.sqlString(vsvcIndex),
					sqlTime);
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s",
			// sqlText));
			ResultSet rs = db.executeQuery(sqlText);

			long minConns = 0x7FFFFFFFL;
			long maxConns = 0;
			Date minDate = null;
			Date maxDate = null;
			;
			long logCnt = 0;
			long totConns = 0;

			if (rs.next() == false) {// 데이터 없는 경우.
				result.setAvgConns(new Long(0));
				result.setMaxConns(new Long(0));
				result.setMaxDate(null);
				result.setMinConns(new Long(0));
				result.setMinDate(null);
			} else {
				do {
					OBDtoConnectionData info = new OBDtoConnectionData();
					info.setCurConns(db.getLong(rs, "CONN_CURR"));
					info.setMaxConns(db.getLong(rs, "CONN_MAX"));
					info.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
					info.setTotConns(db.getLong(rs, "CONN_TOT"));
					data.add(info);
					if (minConns >= info.getCurConns()) {
						minConns = info.getCurConns();
						minDate = info.getOccurTime();
					}
					if (maxConns <= info.getCurConns()) {
						maxConns = info.getCurConns();
						maxDate = info.getOccurTime();
					}
					totConns += info.getCurConns();
					logCnt++;
					// System.out.println(DateFormat.getDateTimeInstance( DateFormat.LONG,
					// DateFormat.LONG).format(info.getOccurTime()));
				} while (rs.next());

				if (totConns > 0)
					result.setAvgConns(totConns / logCnt);
				else
					result.setAvgConns(totConns);
				result.setMaxConns(maxConns);
				result.setMaxDate(maxDate);
				result.setMinConns(minConns);
				result.setMinDate(minDate);
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
		return result;
	}

	@Override
	public OBDtoVirtualServerSummary getVirtualServerStatusSummary(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoVirtualServerSummary result;
		try {
			db.openDB();
			int type = new OBAdcManagementImpl().getAdcType(adcIndex);
			if (type == OBDefine.ADC_TYPE_ALTEON)
				result = getVirtualServiceStatusSummaryAlteon(adcIndex, db);
			else if (type == OBDefine.ADC_TYPE_F5)
				result = getVirtualServerStatusSummaryF5(adcIndex, db);
			else if (type == OBDefine.ADC_TYPE_PIOLINK_PAS)
				result = getVirtualServerStatusSummaryPAS(adcIndex, db);
			else if (type == OBDefine.ADC_TYPE_PIOLINK_PASK)
				result = getVirtualServerStatusSummaryPASK(adcIndex, db);
			else {
				throw new OBException(OBException.ERRCODE_NOT_SUPPORT_VENDOR);// , String.format("invalid vendor
																				// type:%d", type));
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		return result;
	}

	private OBDtoVirtualServerSummary getVirtualServerStatusSummaryF5(Integer adcIndex, OBDatabase db)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		OBDtoVirtualServerSummary result = new OBDtoVirtualServerSummary();
		result.setVsAvail(new Long(0));
		result.setVsTotal(new Long(0));
		result.setVsUnavail(new Long(0));

		String sqlText = "";
		try {
			sqlText = String.format(" SELECT STATUS, COUNT(INDEX) AS SUM                                         \n"
					+ " FROM TMP_SLB_VSERVER                                                       \n"
					+ " WHERE ADC_INDEX = %d                                                       \n"
					+ "     AND EXISTS (SELECT 1 FROM MNG_ADC WHERE INDEX = %d AND AVAILABLE = %d) \n"
					+ " GROUP BY STATUS \n", adcIndex, adcIndex, OBDefine.ADC_STATE.AVAILABLE);

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				Integer status = db.getInteger(rs, "STATUS");
				Integer count = db.getInteger(rs, "SUM");
				if (status == OBDefine.VS_STATUS.AVAILABLE)
					result.setVsAvail(result.getVsAvail() + count);
				else
					result.setVsUnavail(result.getVsUnavail() + count);
			}
			result.setVsTotal(result.getVsAvail() + result.getVsUnavail());
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	private OBDtoVirtualServerSummary getVirtualServerStatusSummaryPAS(Integer adcIndex, OBDatabase db)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		OBDtoVirtualServerSummary result = new OBDtoVirtualServerSummary();
		result.setVsAvail(new Long(0));
		result.setVsTotal(new Long(0));
		result.setVsUnavail(new Long(0));

		String sqlText = "";
		try {
			sqlText = String.format(" SELECT STATUS, COUNT(INDEX) AS SUM                                         \n"
					+ " FROM TMP_SLB_VSERVER                                                       \n"
					+ " WHERE ADC_INDEX = %d                                                       \n"
					+ "     AND EXISTS (SELECT 1 FROM MNG_ADC WHERE INDEX = %d AND AVAILABLE = %d) \n"
					+ " GROUP BY STATUS \n", adcIndex, adcIndex, OBDefine.ADC_STATE.AVAILABLE);

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				Integer status = db.getInteger(rs, "STATUS");
				Integer count = db.getInteger(rs, "SUM");
				if (status == OBDefine.VS_STATUS.AVAILABLE)
					result.setVsAvail(result.getVsAvail() + count);
				else
					result.setVsUnavail(result.getVsUnavail() + count);
			}
			result.setVsTotal(result.getVsAvail() + result.getVsUnavail());
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	private OBDtoVirtualServerSummary getVirtualServerStatusSummaryPASK(Integer adcIndex, OBDatabase db)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		OBDtoVirtualServerSummary result = new OBDtoVirtualServerSummary();
		result.setVsAvail(new Long(0));
		result.setVsTotal(new Long(0));
		result.setVsUnavail(new Long(0));

		String sqlText = "";
		try {
			sqlText = String.format(" SELECT STATUS, COUNT(INDEX) AS SUM                                         \n"
					+ " FROM TMP_SLB_VSERVER                                                       \n"
					+ " WHERE ADC_INDEX = %d                                                       \n"
					+ "     AND EXISTS (SELECT 1 FROM MNG_ADC WHERE INDEX = %d AND AVAILABLE = %d) \n"
					+ " GROUP BY STATUS \n", adcIndex, adcIndex, OBDefine.ADC_STATE.AVAILABLE);

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				Integer status = db.getInteger(rs, "STATUS");
				Integer count = db.getInteger(rs, "SUM");
				if (status == OBDefine.VS_STATUS.AVAILABLE)
					result.setVsAvail(result.getVsAvail() + count);
				else
					result.setVsUnavail(result.getVsUnavail() + count);
			}
			result.setVsTotal(result.getVsAvail() + result.getVsUnavail());
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	// public static void main(String[] args)
	// {
	// OBMonitoringImpl mon = new OBMonitoringImpl();
	//
	// try
	// {
	// OBDtoVirtualServerSummary list = mon.getVirtualServiceStatusSummaryAlteon(8);
	// System.out.println(list);
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	private OBDtoVirtualServerSummary getVirtualServiceStatusSummaryAlteon(Integer adcIndex, OBDatabase db)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		OBDtoVirtualServerSummary result = new OBDtoVirtualServerSummary();
		result.setVsAvail(new Long(0));
		result.setVsTotal(new Long(0));
		result.setVsUnavail(new Long(0));

		String sqlText = "";
		try {
			sqlText = String.format(" SELECT STATUS, COUNT(INDEX) AS SUM " + " FROM TMP_SLB_VS_SERVICE "
					+ " WHERE ADC_INDEX = %d " + " GROUP BY STATUS ", adcIndex);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				Integer status = db.getInteger(rs, "STATUS");
				Integer count = db.getInteger(rs, "SUM");
				if (status == OBDefine.VS_STATUS.AVAILABLE)
					result.setVsAvail(result.getVsAvail() + count);
				else
					result.setVsUnavail(result.getVsUnavail() + count);
			}
			result.setVsTotal(result.getVsAvail() + result.getVsUnavail());
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	// public static void main(String[] args)
	// {
	// OBMonitoringImpl mon = new OBMonitoringImpl();
	//
	// try
	// {
	// OBDtoTrafficMapVServiceMembers list = mon.getTrafficMapsVServiceDetailF5(3,
	// "3_waf_112.106.11.45_443");
	// System.out.println(list.getMemberList().size());
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public OBDtoTrafficMapVServiceMembers getTrafficMapsVServiceDetailF5(Integer adcIndex, String vsIndex)
			throws OBException {
		return getTrafficMapsVServiceDetailF5(adcIndex, vsIndex, OBDefine.ORDER_TYPE_CPS, OBDefine.ORDER_DIR_DESCEND);
	}

	// public static void main(String[] args)
	// {
	// OBMonitoringImpl mon = new OBMonitoringImpl();
	//
	// try
	// {
	// OBDtoTrafficMapVServiceMembers list =
	// mon.getTrafficMapsVServiceDetailAlteon(5, "5_152", 80);
	// System.out.println(list);
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public OBDtoTrafficMapVServiceMembers getTrafficMapsVServiceDetailAlteon(Integer adcIndex, String vsIndex,
			Integer srvPort) throws OBException {
		return getTrafficMapsVServiceDetailAlteon(adcIndex, vsIndex, srvPort, OBDefine.ORDER_TYPE_CPS,
				OBDefine.ORDER_DIR_DESCEND);
	}

	// public static void main(String[] args)
	// {
	// OBMonitoringImpl mon = new OBMonitoringImpl();
	//
	// try
	// {
	// ArrayList<OBDtoAdcPortStatus> list = mon.getPortStatus(8);
	// System.out.println(list);
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	// @Override
	// public ArrayList<OBDtoAdcPortStatus> getPortStatus(Integer adcIndex) throws
	// OBException
	// {
	// return getPortStatus(adcIndex, OBDefine.ORDER_TYPE_NAME,
	// OBDefine.ORDER_DIR_ASCEND);
	// }

	// public static void main(String[] args)
	// {
	// OBMonitoringImpl mon = new OBMonitoringImpl();
	//
	// try
	// {
	// ArrayList<OBDtoAdcSysRescStatus> list = mon.getAdcSysRescStatus(8, null,
	// null);
	// System.out.println(list);
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public OBDtoAdcSysRescStatus getAdcSysRescStatus(Integer adcIndex, Date beginTime, Date endTime)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, beginTime:%s, endTime:%s", adcIndex, beginTime, endTime));

		OBDtoAdcSysRescStatus result = new OBDtoAdcSysRescStatus();
		ArrayList<OBDtoAdcSystemResources> rescList = new ArrayList<OBDtoAdcSystemResources>();
		ArrayList<OBDtoAdcConfigHistory> confEventList = new OBAdcConfigHistoryImpl()
				.getAdcConfigHistoryListNoPagingNoSearch(adcIndex, beginTime, endTime);

		result.setConfEventList(confEventList);
		result.setRescList(rescList);

		OBDatabase db = new OBDatabase();
		String sqlText = "";
		try {
			db.openDB();
			String sqlTime = "";
			if (endTime == null)
				endTime = new Date();

			if (beginTime == null) {
				beginTime = new Date(endTime.getTime() - 7 * 24 * 60 * 60 * 1000);// 7일전 시간.
			}

			sqlTime = String.format(" OCCUR_TIME <= %s AND OCCUR_TIME >= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))),
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));
			sqlText = String.format(
					" SELECT OCCUR_TIME,                                                                                                       \n"
							+ " CPU1_USAGE, CPU2_USAGE, CPU3_USAGE, CPU4_USAGE, CPU5_USAGE, CPU6_USAGE, CPU7_USAGE, CPU8_USAGE, CPU9_USAGE, CPU10_USAGE, \n"
							+ " CPU11_USAGE, CPU12_USAGE, CPU13_USAGE, CPU14_USAGE, CPU15_USAGE, CPU16_USAGE,                                            \n"
							+ " MEM_USAGE                                                                                                                \n"
							+ " FROM LOG_RESC_CPUMEM                                                                                             		 \n"
							+ " WHERE ADC_INDEX=%d AND %s ORDER BY OCCUR_TIME;                                                                                              \n",
					adcIndex, sqlTime);

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("sql:%s", sqlText));
			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcSystemResources status = new OBDtoAdcSystemResources();
				status.setCpu1Usage(db.getInteger(rs, "CPU1_USAGE"));
				status.setCpu2Usage(db.getInteger(rs, "CPU2_USAGE"));
				status.setCpu3Usage(db.getInteger(rs, "CPU3_USAGE"));
				status.setCpu4Usage(db.getInteger(rs, "CPU4_USAGE"));
				status.setCpu5Usage(db.getInteger(rs, "CPU5_USAGE"));
				status.setCpu6Usage(db.getInteger(rs, "CPU6_USAGE"));
				status.setCpu7Usage(db.getInteger(rs, "CPU7_USAGE"));
				status.setCpu8Usage(db.getInteger(rs, "CPU8_USAGE"));
				status.setCpu9Usage(db.getInteger(rs, "CPU9_USAGE"));
				status.setCpu10Usage(db.getInteger(rs, "CPU10_USAGE"));
				status.setCpu11Usage(db.getInteger(rs, "CPU11_USAGE"));
				status.setCpu12Usage(db.getInteger(rs, "CPU12_USAGE"));
				status.setCpu13Usage(db.getInteger(rs, "CPU13_USAGE"));
				status.setCpu14Usage(db.getInteger(rs, "CPU14_USAGE"));
				status.setCpu15Usage(db.getInteger(rs, "CPU15_USAGE"));
				status.setCpu16Usage(db.getInteger(rs, "CPU16_USAGE"));
				status.setMemUsage(db.getInteger(rs, "MEM_USAGE"));
				status.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				rescList.add(status);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	// public static void main(String[] args)
	// {
	// OBMonitoringImpl mon = new OBMonitoringImpl();
	//
	// try
	// {
	// System.out.println(mon.getTrafficMapsVServerTotalRecordCount(1));
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	// @Override
	public ArrayList<OBDtoAdcSystemLog> getAdcSystemFaultLog(OBDtoADCObject adcObject, OBDtoSearch searchOption)
			throws OBException {
		OBDtoOrdering orderOptionBasic = new OBDtoOrdering();
		orderOptionBasic.setOrderType(OBDefine.ORDER_TYPE_OCCURTIME);
		orderOptionBasic.setOrderDirection(OBDefine.ORDER_DIR_DESCEND);
		return getAdcSystemFaultLog(adcObject, searchOption, orderOptionBasic, null);
	}

	@Override
	public Integer getAdcSystemFaultLogCount(OBDtoADCObject adcObject, OBDtoSearch searchOption, Integer accountIndex)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcObject:%s, searchOption:%s", adcObject, searchOption));

		OBDatabase db = new OBDatabase();

		String sqlText = "";
		String sqlSearch = "";
		String sqlTime = "";
		try {
			db.openDB();

			// 사용자의 유효 adcIndex를 구한다.
			String userAdcIndexString = new OBAdcManagementImpl().getUsersAdcIndexString(adcObject.getCategory(),
					adcObject.getIndex(), accountIndex);
			if (userAdcIndexString.isEmpty() == true) { // 없으면 그만한다. 빈 list return
				return 0;
			}

			if (searchOption.getSearchKey() != null && searchOption.getSearchKey().isEmpty() == false) {
				// #3984-2 #6: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchOption.getSearchKey()) + "%";
				sqlSearch = String.format(" ( ADC_NAME LIKE %s OR EVENT LIKE %s ) ", OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			}

			if (searchOption.getToTime() != null) {
				sqlTime = String.format(" OCCUR_TIME <= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getToTime().getTime()))));
			}
			if (searchOption.getFromTime() != null) {
				if (sqlTime.isEmpty() == false) {
					sqlTime += " AND ";
				}
				sqlTime += String.format(" OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getFromTime().getTime()))));
			}

			sqlText = String.format(
					" SELECT " + " COUNT(ADC_INDEX) AS CNT " + " FROM LOG_ADC_FAULT " + " WHERE ADC_INDEX IN ( %s ) ",
					userAdcIndexString);

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND" + sqlTime;

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return 0;
			}
			int result = db.getInteger(rs, "CNT");
			// TODO
			int limit = new OBAdcManagementImpl().getPropertyLogLimitInfo();
			if (result > limit)
				result = limit;

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", result));
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public OBDtoAdcPerformance getAdcPerformance(Integer adcIndex, Date beginTime, Date endTime) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, beginTime:%s, endTime:%s", adcIndex, beginTime, endTime));

		OBDtoAdcPerformance result = new OBDtoAdcPerformance();
		ArrayList<OBDtoAdcPerfHttpReq> httpReqList = new ArrayList<OBDtoAdcPerfHttpReq>();
		ArrayList<OBDtoAdcPerfSslTrans> sshTransList = new ArrayList<OBDtoAdcPerfSslTrans>();
		ArrayList<OBDtoAdcPerfConnection> connList = new ArrayList<OBDtoAdcPerfConnection>();
		ArrayList<OBDtoAdcPerfTroughput> throughputList = new ArrayList<OBDtoAdcPerfTroughput>();

		result.setConnList(connList);
		result.setHttpReqList(httpReqList);
		result.setSshTransList(sshTransList);
		result.setThroughputList(throughputList);

		result.setBpsAvg(new Long(0));
		result.setBpsCurr(new Long(0));
		result.setBpsMax(new Long(0));
		result.setBpsMin(new Long(0));
		result.setConnAvg(new Long(0));
		result.setConnCurr(new Long(0));
		result.setConnMax(new Long(0));
		result.setConnMin(new Long(0));
		result.setHttpReqAvg(new Long(0));
		result.setHttpReqCurr(new Long(0));
		result.setHttpReqMax(new Long(0));
		result.setHttpReqMin(new Long(0));
		result.setPpsAvg(new Long(0));
		result.setPpsCurr(new Long(0));
		result.setPpsMax(new Long(0));
		result.setPpsMin(new Long(0));
		result.setSslTransAvg(new Long(0));
		result.setSslTransCurr(new Long(0));
		result.setSslTransMax(new Long(0));
		result.setSslTransMin(new Long(0));

		OBDatabase db = new OBDatabase();

		String sqlText = "";

		try {
			db.openDB();
			sqlText = String
					.format(" SELECT " + " OCCUR_TIME, HTTP_REQ_PS, SSL_TRANS_PS, CONNS_PS,  PACKETS_PS, BYTES_PS"
							+ " FROM LOG_ADC_PERFORMANCE " + " WHERE ADC_INDEX=%d ", adcIndex);

			String sqlTime = "";
			if (endTime == null)
				endTime = new Date();
			sqlTime = String.format(" OCCUR_TIME <= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));

			if (beginTime == null) {
				beginTime = new Date(endTime.getTime() - 7 * 24 * 60 * 60 * 1000);// 7일전 시간.
			}

			sqlTime += String.format(" AND OCCUR_TIME >= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			sqlText += " AND " + sqlTime;
			sqlText += " ORDER BY OCCUR_TIME ASC ";

			ResultSet rs = db.executeQuery(sqlText);

			long bpsMin = 0x7FFFFFFFL;
			long bpsMax = 0;
			long bpsAvg = 0;
			long bpsSum = 0;
			long ppsMin = 0x7FFFFFFFL;
			long ppsMax = 0;
			long ppsAvg = 0;
			long ppsSum = 0;
			long connMin = 0x7FFFFFFFL;
			long connMax = 0;
			long connAvg = 0;
			long connSum = 0;
			long httpReqMin = 0x7FFFFFFFL;
			long httpReqMax = 0;
			long httpReqAvg = 0;
			long httpReqSum = 0;
			long sslTransMin = 0x7FFFFFFFL;
			long sslTransMax = 0;
			long sslTransAvg = 0;
			long sslTransSum = 0;
			long logCnt = 0;

			while (rs.next()) {
				OBDtoAdcPerfHttpReq httpReq = new OBDtoAdcPerfHttpReq();
				OBDtoAdcPerfSslTrans sshTrans = new OBDtoAdcPerfSslTrans();
				OBDtoAdcPerfConnection conn = new OBDtoAdcPerfConnection();
				OBDtoAdcPerfTroughput throughput = new OBDtoAdcPerfTroughput();

				httpReq.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				httpReq.setHttpReqPerSecond(db.getLong(rs, "HTTP_REQ_PS"));
				httpReqList.add(httpReq);

				httpReqSum += httpReq.getHttpReqPerSecond();
				if (httpReqMin >= httpReq.getHttpReqPerSecond())
					httpReqMin = httpReq.getHttpReqPerSecond();
				if (httpReqMax <= httpReq.getHttpReqPerSecond())
					httpReqMax = httpReq.getHttpReqPerSecond();

				sshTrans.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				sshTrans.setSslTransPerSecond(db.getLong(rs, "SSL_TRANS_PS"));
				sshTransList.add(sshTrans);

				sslTransSum += sshTrans.getSslTransPerSecond();
				if (sslTransMin >= sshTrans.getSslTransPerSecond())
					sslTransMin = sshTrans.getSslTransPerSecond();
				if (sslTransMax <= sshTrans.getSslTransPerSecond())
					sslTransMax = sshTrans.getSslTransPerSecond();

				conn.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				conn.setConnsPerSecond(db.getLong(rs, "CONNS_PS"));
				connList.add(conn);

				connSum += conn.getConnsPerSecond();
				if (connMin >= conn.getConnsPerSecond())
					connMin = conn.getConnsPerSecond();
				if (connMax <= conn.getConnsPerSecond())
					connMax = conn.getConnsPerSecond();

				throughput.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				throughput.setBps(db.getLong(rs, "BYTES_PS"));
				throughput.setPps(db.getLong(rs, "PACKETS_PS"));
				throughputList.add(throughput);
				bpsSum += throughput.getBps();
				if (bpsMin >= throughput.getBps())
					bpsMin = throughput.getBps();
				if (bpsMax <= throughput.getBps())
					bpsMax = throughput.getBps();
				ppsSum += throughput.getPps();
				if (ppsMin >= throughput.getPps())
					ppsMin = throughput.getPps();
				if (ppsMax <= throughput.getPps())
					ppsMax = throughput.getPps();

				logCnt++;
			}
			if (logCnt > 0) {
				bpsAvg = bpsSum / logCnt;
				ppsAvg = ppsSum / logCnt;
				connAvg = connSum / logCnt;
				httpReqAvg = httpReqSum / logCnt;
				sslTransAvg = sslTransSum / logCnt;
			}

			result.setBpsAvg(bpsAvg);
			if (result.getThroughputList().size() > 0)
				result.setBpsCurr(result.getThroughputList().get(0).getBps());
			result.setBpsMax(bpsMax);
			result.setBpsMin(bpsMin);

			result.setConnAvg(connAvg);
			if (result.getConnList().size() > 0)
				result.setConnCurr(result.getConnList().get(0).getConnsPerSecond());
			result.setConnMax(connMax);
			result.setConnMin(connMin);

			result.setHttpReqAvg(httpReqAvg);
			if (result.getHttpReqList().size() > 0)
				result.setHttpReqCurr(result.getHttpReqList().get(0).getHttpReqPerSecond());
			result.setHttpReqMax(httpReqMax);
			result.setHttpReqMin(httpReqMin);

			result.setPpsAvg(ppsAvg);
			if (result.getThroughputList().size() > 0)
				result.setPpsCurr(result.getThroughputList().get(0).getPps());
			result.setPpsMax(ppsMax);
			result.setPpsMin(ppsMin);

			result.setSslTransAvg(sslTransAvg);
			if (result.getSshTransList().size() > 0)
				result.setSslTransCurr(result.getSshTransList().get(0).getSslTransPerSecond());
			result.setSslTransMax(sslTransMax);
			result.setSslTransMin(sslTransMin);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", result.getConnList().size()));
		return result;
	}

	/**
	 * 계정에 할당된 ADC들 상태별 개수를 파악한다. </br>
	 * - 정상 몇개, 단절 몇개..
	 * 
	 * @param accountIndex
	 * @param db
	 * @return
	 * @throws OBException
	 */
	public OBDtoAdcStatusCount GetAdcStatusCount(Integer accountIndex, OBDatabase db) throws OBException {
		OBDtoAdcStatusCount count = new OBDtoAdcStatusCount();
		String sqlText = "";
		Integer roleNo = new OBAccountImpl().getAccountRoleNo(accountIndex);

		if (roleNo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);// , String.format( "accountIndex =%d",
																			// accountIndex));
		}

		try {
			if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
				sqlText = String.format(" SELECT STATUS, COUNT(INDEX) AS SUM " + " FROM MNG_ADC "
						+ " WHERE AVAILABLE=%d " + " GROUP BY STATUS ", OBDefine.ADC_STATE.AVAILABLE);
			} else {
				sqlText = String.format(" SELECT STATUS, COUNT(INDEX) AS SUM " + " FROM MNG_ADC WHERE AVAILABLE = %d "
						+ "	AND INDEX IN "
						+ "	(SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %d ORDER BY ADC_INDEX) "
						+ " GROUP BY STATUS; ", OBDefine.ADC_STATE.AVAILABLE, accountIndex);
			}
			ResultSet rs = db.executeQuery(sqlText);
			int sum = 0;
			int availCnt = 0;
			int unavailCnt = 0;
			while (rs.next()) {
				if (db.getInteger(rs, "STATUS") == OBDefine.ADC_STATUS.REACHABLE)
					availCnt += db.getInteger(rs, "SUM");
				else
					unavailCnt += db.getInteger(rs, "SUM");
				sum += db.getInteger(rs, "SUM");
				;
			}
			count.setAdc(sum);
			count.setAdcAvail(availCnt);
			count.setAdcUnavail(unavailCnt);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return count;
	}

	/**
	 * 계정에 할당된 ADC들의 Virtual Server를 한데 모아 상태별 개수를 취합한다.</br>
	 * 정상 몇개, 단절 몇개,..
	 * 
	 * @param accountIndex
	 * @param db
	 * @return
	 * @throws OBException
	 */
	public OBDtoVservStatusCount GetVservStatusCount(Integer accountIndex, OBDatabase db) throws OBException {
		OBDtoVservStatusCount count = new OBDtoVservStatusCount();
		String sqlText = "";
		try {
			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accountIndex);
			if (roleNo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);
			}
			String sqlVSStatusCount = "";
			if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
				sqlVSStatusCount = String.format(" SELECT A.STATUS, COUNT(A.INDEX) AS SUM \n"
						+ " FROM TMP_SLB_VSERVER A 	             \n"
						+ " WHERE EXISTS (SELECT 1 FROM MNG_ADC WHERE INDEX = A.ADC_INDEX AND AVAILABLE = %d) \n"
						+ " GROUP BY A.STATUS; \n", OBDefine.ADC_STATE.AVAILABLE);
			} else {
				// sqlVSStatusCount=String.format(
				// " SELECT A.STATUS, COUNT(A.INDEX) AS SUM \n" +
				// " FROM TMP_SLB_VSERVER A \n" +
				// " INNER JOIN ( \n" +
				// " SELECT INDEX FROM MNG_ADC \n" +
				// " WHERE AVAILABLE = %d \n" +
				// " AND INDEX IN (SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX =
				// %d) ) B \n" +
				// " ON A.ADC_INDEX=B.INDEX \n" +
				// " GROUP BY A.STATUS ",
				// OBDefine.ADC_STATE.AVAILABLE, accountIndex);

				sqlVSStatusCount = String.format( // 필요없는 join을 제거한 동치 sql
						" SELECT 2, A.STATUS, COUNT(A.INDEX) AS SUM  \n"
								+ " FROM TMP_SLB_VSERVER A 					 \n"
								+ " WHERE EXISTS (                             \n"
								+ "     SELECT 1 FROM MNG_ADC                  \n"
								+ "     WHERE INDEX = A.ADC_INDEX              \n"
								+ "         AND AVAILABLE = %d                 \n"
								+ "         AND INDEX IN (SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX=%d) \n"
								+ // where-in:empty
									// string
									// 불가,
									// null
									// 불가,
									// OK
								"     )                                      \n" + " GROUP BY A.STATUS \n",
						OBDefine.ADC_STATE.AVAILABLE, accountIndex);
			}

			ResultSet rs = db.executeQuery(sqlVSStatusCount);
			int totalCnt = 0;
			int availCnt = 0;
			int unavailCnt = 0;
			int disableCnt = 0;
			int status = OBDefine.VS_STATUS.UNAVAILABLE;
			int sum = 0;

			while (rs.next()) {
				status = db.getInteger(rs, "STATUS");
				sum = db.getInteger(rs, "SUM");

				if (status == OBDefine.VS_STATUS.AVAILABLE)
					availCnt += sum;
				else if (status == OBDefine.VS_STATUS.DISABLE)
					disableCnt += sum;
				else
					// OBDefine.VS_STATUS.UNAVAILABLE
					unavailCnt += sum;

				totalCnt += sum;
			}
			count.setVs(totalCnt);
			count.setVsAvail(availCnt);
			count.setVsDisable(disableCnt);
			count.setVsUnavail(unavailCnt);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return count;
	}

	@Override
	public OBDtoTrafficMapVServiceMembers getTrafficMapsVServiceDetailPAS(Integer adcIndex, String vsIndex)
			throws OBException {
		return getTrafficMapsVServiceDetailPAS(adcIndex, vsIndex, OBDefine.ORDER_TYPE_CPS, OBDefine.ORDER_DIR_DESCEND);
	}

	@Override
	public OBDtoTrafficMapVServiceMembers getTrafficMapsVServiceDetailPASK(Integer adcIndex, String vsIndex)
			throws OBException {
		return getTrafficMapsVServiceDetailPASK(adcIndex, vsIndex, OBDefine.ORDER_TYPE_CPS, OBDefine.ORDER_DIR_DESCEND);
	}

	@Override
	public List<OBDtoAdcPortStatus> getPortStatusForDownload(Integer adcIndex, Date beginTime, Date endTime)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, beginTime:%s, endTime:%s", adcIndex, beginTime, endTime));

		if (beginTime != null) {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("begin:%d", beginTime.getTime()));
		}

		if (endTime != null) {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end:%d", endTime.getTime()));
		}

		ArrayList<OBDtoAdcPortStatus> result = new ArrayList<OBDtoAdcPortStatus>();
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();

			String sqlTime = "";
			if (endTime == null)
				endTime = new Date();

			if (beginTime == null) {
				beginTime = new Date(endTime.getTime() - 12 * 60 * 60 * 1000);
			}

			sqlTime = String.format(" OCCUR_TIME <= %s AND OCCUR_TIME >= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))),
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			sqlText = String.format(
					" SELECT A.OCCUR_TIME, B.NAME AS PORT_NAME, B.ALIAS_NAME AS PORT_ALIAS_NAME, B.SPEED, A.STATUS,  \n"
							+ "     A.PPS_IN, A.PPS_OUT, A.BPS_IN, A.BPS_OUT, A.EPS_IN, A.EPS_OUT, A.DPS_IN, A.DPS_OUT         \n"
							+ " FROM TMP_FAULT_LINK_STATS         A                                                            \n"
							+ " INNER JOIN TMP_FAULT_LINK_INFO    B                                                            \n"
							+ " ON B.PORT_INDEX = A.PORT_INDEX AND A.ADC_INDEX = B.ADC_INDEX                                   \n"
							+ " WHERE ADC_INDEX=%d ;",
					adcIndex, sqlTime);

			ResultSet rs = db.executeQuery(sqlText);
			String aliasName;
			while (rs.next()) {
				OBDtoAdcPortStatus status = new OBDtoAdcPortStatus();
				status.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				status.setBytesIn(db.getLong(rs, "BPS_IN"));
				status.setBytesOut(db.getLong(rs, "BPS_OUT"));
				status.setBytesTot(status.getBytesIn() + status.getBytesOut());
				status.setDropsIn(db.getLong(rs, "DPS_IN"));
				status.setDropsOut(db.getLong(rs, "DPS_OUT"));
				status.setDropsTot(status.getDropsIn() + status.getDropsOut());
				status.setErrorsIn(db.getLong(rs, "EPS_IN"));
				status.setErrorsOut(db.getLong(rs, "EPS_OUT"));
				status.setErrorsTot(status.getErrorsIn() + status.getErrorsOut());
				status.setIntfName(db.getString(rs, "PORT_NAME"));
				status.setLinkStatus(db.getInteger(rs, "STATUS"));
				status.setPktsIn(db.getLong(rs, "PPS_IN"));
				status.setPktsOut(db.getLong(rs, "PPS_OUT"));
				status.setPktsTot(status.getPktsIn() + status.getPktsOut());
				aliasName = db.getString(rs, "PORT_ALIAS_NAME");
				if (aliasName != null && aliasName.isEmpty() == false) // null 참조 방지!!
				{
					status.setDispName(status.getIntfName() + "(" + aliasName + ")");
				} else {
					status.setDispName(status.getIntfName());
				}
				result.add(status);
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
		return result;
	}

	@Override
	public OBDtoMultiDataObj getStatisticsGraphData(Integer adcIndex, String interfaceName, Integer queryType,
			Date beginTime, Date endTime) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoMultiDataObj retVal = new OBDtoMultiDataObj();
		try {
			db.openDB();
			retVal = getStatisticsGraphData(adcIndex, interfaceName, queryType, beginTime, endTime, db); // adcIndex
																											// null로
																											// 호출하면 모든
																											// 유효 adc의
																											// config
																											// alarm을
																											// 가져온다.
		} catch (OBException e) {
			throw e;
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private Integer getPortInterfaceIndexByName(Integer adcIndex, String name, OBDatabase db) throws OBException {
		Integer retVal = 0;
		String sqlText = null;
		try {
			sqlText = String.format(
					" SELECT PORT_INDEX                  \n" + " FROM TMP_FAULT_LINK_INFO           \n"
							+ " WHERE ADC_INDEX = %d AND NAME = %s \n",
					adcIndex, OBParser.sqlString(name.toLowerCase()));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				retVal = db.getInteger(rs, "PORT_INDEX");
			}
		} catch (SQLException e) {
			throw new OBException(e.getMessage());
		} catch (OBException e) {
			throw e;
		}
		return retVal;
	}

	public OBDtoMultiDataObj getStatisticsGraphData(Integer adcIndex, String interfaceName, Integer queryType,
			Date beginTime, Date endTime, OBDatabase db) throws OBException {
		if (beginTime != null) {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("begin:%d", beginTime.getTime()));
		}

		if (endTime != null) {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end:%d", endTime.getTime()));
		}

		OBDtoMultiDataObj mtObj = new OBDtoMultiDataObj();
		ArrayList<OBDtoConnectionDataObj> dtObj = new ArrayList<OBDtoConnectionDataObj>();
		mtObj.setName(interfaceName);

		String sqlText = null;
		String sqlTime = null;
		try {
			if (endTime == null) {
				endTime = new Date();
			}

			if (beginTime == null) {
				beginTime = new Date(endTime.getTime() - 1 * 12 * 60 * 60 * 1000);
			}

			sqlTime = String.format(" A.OCCUR_TIME <= %s AND A.OCCUR_TIME >= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))),
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			Integer portIndex = getPortInterfaceIndexByName(adcIndex, interfaceName, db);
			String columnName = "PORT" + portIndex;
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s",
			// sqlText));

			if (queryType == BYTES) {
				sqlText = String
						.format(" SELECT A.OCCUR_TIME AS OCCUR_TIME, A.ADC_INDEX, A.%s AS IN, B.%s AS OUT     \n"
								+ " FROM LOG_LINK_BPS_IN                      A                                 \n"
								+ " INNER JOIN LOG_LINK_BPS_OUT     B                                           \n"
								+ " ON B.OCCUR_TIME = A.OCCUR_TIME AND B.ADC_INDEX = A.ADC_INDEX                \n"
								+ " WHERE A.ADC_INDEX=%d                                                        \n"
								+ " AND %s                                                                      \n"
								+ " ORDER BY OCCUR_TIME ASC ;", columnName, columnName, adcIndex, sqlTime);

			} else if (queryType == PACKETS) {
				sqlText = String
						.format(" SELECT A.OCCUR_TIME AS OCCUR_TIME, A.ADC_INDEX, A.%s AS IN, B.%s AS OUT       \n"
								+ " FROM LOG_LINK_PPS_IN                      A                                 \n"
								+ " INNER JOIN LOG_LINK_PPS_OUT               B                                 \n"
								+ " ON B.OCCUR_TIME = A.OCCUR_TIME AND B.ADC_INDEX = A.ADC_INDEX                \n"
								+ " WHERE A.ADC_INDEX=%d                                                        \n"
								+ " AND %s                                                                      \n"
								+ " ORDER BY OCCUR_TIME ASC ;", columnName, columnName, adcIndex, sqlTime);
			} else if (queryType == ERRORS) {
				sqlText = String
						.format(" SELECT A.OCCUR_TIME AS OCCUR_TIME, A.ADC_INDEX, A.%s AS IN, 0 AS OUT          \n"
								+ " FROM LOG_LINK_EPS_IN                      A                                 \n"
								+ " WHERE A.ADC_INDEX=%d                                                        \n"
								+ " AND %s                                                                      \n"
								+ " ORDER BY OCCUR_TIME ASC ;", columnName, adcIndex, sqlTime);
			} else if (queryType == DROPS) {
				sqlText = String
						.format(" SELECT A.OCCUR_TIME AS OCCUR_TIME, A.ADC_INDEX, A.%s AS IN, 0 AS OUT          \n"
								+ " FROM LOG_LINK_DPS_IN             A                                          \n"
								+ " WHERE A.ADC_INDEX=%d                                                        \n"
								+ " AND %s                                                                      \n"
								+ " ORDER BY OCCUR_TIME ASC ;", columnName, adcIndex, sqlTime);
			}

			ResultSet rs = db.executeQuery(sqlText);

			// Date intoRSTime = new Date();

			while (rs.next()) {
				OBDtoConnectionDataObj dtInfo = new OBDtoConnectionDataObj();

				dtInfo.setDiff(0L);

				Long inValue = 0L;
				inValue = db.getLong(rs, "IN");
				if (inValue == -1)
					inValue = 0L;

				Long outValue = 0L;
				outValue = db.getLong(rs, "OUT");
				if (outValue == -1)
					outValue = 0L;

				dtInfo.setInValue(inValue);
				dtInfo.setOutValue(outValue);
				dtInfo.setTotalValue(inValue + outValue);
				dtInfo.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));

				dtObj.add(dtInfo);
			}
			mtObj.setData(dtObj);
		} catch (SQLException e) {
			if (db != null)
				db.closeDB();
			throw new OBException(e.getMessage());
		} catch (OBException e) {
			if (db != null)
				db.closeDB();
			throw e;
		}
		return mtObj;
	}

	@Override
	public ArrayList<OBDtoMultiDataObj> getStatisticsGraphDataList(Integer adcIndex,
			ArrayList<String> interfaceNameList, Integer queryType, Date beginTime, Date endTime) throws OBException {
		ArrayList<OBDtoMultiDataObj> retVal = new ArrayList<OBDtoMultiDataObj>();

		if (interfaceNameList == null | interfaceNameList.size() == 0) // 인터페이스 체크 된 것이 없을 때 처리
			return retVal;

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			retVal = getStatisticsGraphDataList(adcIndex, interfaceNameList, queryType, beginTime, endTime, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private ArrayList<String> getPortNameList(Integer adcIndex, OBDatabase db) throws OBException {
		ArrayList<String> result = new ArrayList<String>();
		String sqlText = "";
		try {
			sqlText = String.format(
					" SELECT NAME AS PORT_NAME, ALIAS_NAME AS PORT_ALIAS_NAME FROM TMP_FAULT_LINK_INFO \n"
							+ " WHERE ADC_INDEX=%d                                                               \n"
							+ " ORDER BY PORT_INDEX ASC                                                          \n",
					adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				result.add(db.getString(rs, "PORT_NAME"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}

		return result;
	}

	private int findPortNameIndex(ArrayList<String> portNameList, String portName) {
		for (int i = 0; i < portNameList.size(); i++) {
			String tmpName = portNameList.get(i);

			if (tmpName.equals(portName))
				return i;
		}
		return -1;
	}

	public ArrayList<OBDtoMultiDataObj> getStatisticsGraphDataList(Integer adcIndex,
			ArrayList<String> interfaceNameList, Integer queryType, Date beginTime, Date endTime, OBDatabase db)
			throws OBException {
		Date dbFirst = new Date();
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. dbFirst:%s", dbFirst));

		ArrayList<OBDtoMultiDataObj> retVal = new ArrayList<OBDtoMultiDataObj>();
		try {
			ArrayList<String> portNameList = getPortNameList(adcIndex, db);
			for (String str : interfaceNameList) {
				OBDtoMultiDataObj mtObj = getStatisticsGraphData(adcIndex, str, queryType, beginTime, endTime, db);
				mtObj.setIndex(findPortNameIndex(portNameList, mtObj.getName()));
				retVal.add(mtObj);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private String getAdcSystemFaultLogOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST ";

		switch (orderType) {
		case OBDefine.ORDER_TYPE_OCCURTIME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY OCCUR_TIME ASC NULLS LAST ";
			else
				retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_CONTENT:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY EVENT ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY EVENT DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SOLVEDTIME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY UPDATE_TIME ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY UPDATE_TIME DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_ADCNAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY ADC_NAME ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY ADC_NAME DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_STATUS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY STATUS ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY STATUS DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		}
		return retVal;
	}

	@Override
	public ArrayList<OBDtoAdcSystemLog> getAdcSystemFaultLog(OBDtoADCObject adcObject, OBDtoSearch searchOption,
			OBDtoOrdering orderOption, Integer accountIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcObject:%s, searchOption:%s, orderOption:%s",
				adcObject, searchOption, orderOption));

		ArrayList<OBDtoAdcSystemLog> result = new ArrayList<OBDtoAdcSystemLog>();

		OBDatabase db = new OBDatabase();

		String sqlText = "";
		String sqlSearch = "";
		String sqlTime = "";
		String sqlLimit = "";
		try {
			db.openDB();

			// 사용자의 유효 adcIndex를 구한다.
			String userAdcIndexString = new OBAdcManagementImpl().getUsersAdcIndexString(adcObject.getCategory(),
					adcObject.getIndex(), accountIndex);

			if (userAdcIndexString.isEmpty() == true) { // 없으면 그만한다. 빈 list return
				return result;
			}

			if (searchOption.getSearchKey() != null && searchOption.getSearchKey().isEmpty() == false) {
				// #3984-2 #6: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchOption.getSearchKey()) + "%";
				sqlSearch = String.format(" ( ADC_NAME LIKE %s OR EVENT LIKE %s ) ", OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			}

			if (searchOption.getToTime() != null) {
				sqlTime = String.format(" OCCUR_TIME <= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getToTime().getTime()))));
			}
			if (searchOption.getFromTime() != null) {
				if (sqlTime.isEmpty() == false) {
					sqlTime += " AND ";
				}
				sqlTime += String.format(" OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getFromTime().getTime()))));
			}

			int offset = 0;
			if (searchOption.getBeginIndex() != null) {
				offset = searchOption.getBeginIndex().intValue();
			}

			int limit = 0;
			if (searchOption.getEndIndex() != null) {
				limit = Math.abs(searchOption.getEndIndex().intValue() - offset) + 1;
				if (limit > PAGE_MAX_LINE) {
					limit = PAGE_MAX_LINE;
				}
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			} else {
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", PAGE_MAX_LINE, offset);
			}

			sqlText = String.format(
					" SELECT OCCUR_TIME, UPDATE_TIME, LEVEL, TYPE, ADC_INDEX, ADC_NAME, VS_INDEX, EVENT, STATUS \n"
							+ " FROM LOG_ADC_FAULT \n" + " WHERE ADC_INDEX IN ( %s ) \n",
					userAdcIndexString);

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND" + sqlTime;

			sqlText += getAdcSystemFaultLogOrderType(orderOption.getOrderType(), orderOption.getOrderDirection());

			sqlText += sqlLimit;

			sqlText += ";";
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcSystemLog log = new OBDtoAdcSystemLog();
				log.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				log.setStatus(db.getInteger(rs, "STATUS"));
				if (log.getStatus().equals(OBDefineFault.STATUS_UNSOLVED) == false) // 미해결 장애가 아니면(해결/경고) 해결 시각을 준다.
				{
					log.setFinishTime(OBDateTime.toDate(db.getTimestamp(rs, "UPDATE_TIME")));
				} else
				// 미해결 장애는 해결 시각이 null
				{
					log.setFinishTime(null);
				}
				log.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				log.setAdcName(db.getString(rs, "ADC_NAME"));
				log.setEvent(db.getString(rs, "EVENT"));
				log.setLogLevel(db.getInteger(rs, "LEVEL"));
				log.setLogType(db.getInteger(rs, "TYPE"));
				// log.setServicePort(db.getInteger(rs, "SERVICE_PORT"));

				log.setVsIndex(db.getString(rs, "VS_INDEX"));
				result.add(log);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	public Long getAdcSystemFaultLogSize(Integer adcIndex, String searchKey, Date beginTime, Date endTime,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. begin:%s, endTime:%s, index:%d/%d", beginTime, endTime, beginIndex, endIndex));
		if (beginTime != null)
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("begin:%d", beginTime.getTime()));
		if (endTime != null)
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end:%d", endTime.getTime()));

		Long result = new Long(0L);

		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();
			int offset = 0;
			if (beginIndex != null)
				offset = beginIndex.intValue();

			int limit = 0;
			String sqlLimit = "";
			if (endIndex != null) {
				limit = Math.abs(endIndex.intValue() - offset) + 1;
				if (limit > 10000)
					limit = 10000;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			} else {
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", 10000, offset);
			}

			String sqlSearch = "";
			if (searchKey != null && !searchKey.isEmpty()) {
				// #3984-2 #6: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlSearch = String.format(" ( ADC_NAME LIKE %s OR EVENT LIKE %s ) ", OBParser.sqlString(wildcardKey),
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

			sqlText = String.format(
					" SELECT OCCUR_TIME, UPDATE_TIME, LEVEL, TYPE, ADC_INDEX, ADC_NAME, VS_INDEX, EVENT, STATUS \n"
							+ " FROM LOG_ADC_FAULT \n" + " WHERE ADC_INDEX = %d \n",
					adcIndex);

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND" + sqlTime;

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			sqlText += getAdcSystemFaultLogOrderType(orderType, orderDir);

			sqlText += sqlLimit;

			sqlText += ";";
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				result += db.getString(rs, "EVENT").length();
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	// @Override
	// public ArrayList<OBDtoTrafficMapVServer> getTrafficMapsVServerF5(Integer
	// adcIndex, String searchKeys, Integer fromIndex, Integer endIndex, Integer
	// orderType, Integer orderDir) throws OBException
	// {
	// ArrayList<OBDtoTrafficMapVServer> result;
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// db.openDB();
	// }
	// catch(OBException e)
	// {
	// throw new OBException(OBException.ERRCODE_MON_GETVSTRFAFFIC, e);
	// }
	//
	// try
	// {
	// result=getTrafficMapsVServerF5(adcIndex, searchKeys, fromIndex, endIndex,
	// orderType, orderDir, db);
	// }
	// catch(OBException e)
	// {
	// db.closeDB();
	// throw new OBException(OBException.ERRCODE_MON_GETVSTRFAFFIC, e);
	// }
	// catch(Exception e)
	// {
	// db.closeDB();
	// OBException e1 = new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
	// throw new OBException(OBException.ERRCODE_MON_GETVSTRFAFFIC, e1);
	// }
	// db.closeDB();
	// return result;
	// }

	// @Override
	// public ArrayList<OBDtoTrafficMapVServer> getTrafficMapsVServerPAS(Integer
	// adcIndex, String searchKeys, Integer fromIndex, Integer endIndex, Integer
	// orderType, Integer orderDir) throws OBException
	// {
	// ArrayList<OBDtoTrafficMapVServer> result;
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// db.openDB();
	// }
	// catch(OBException e)
	// {
	// throw new OBException(OBException.ERRCODE_MON_GETVSTRFAFFIC, e);
	// }
	//
	// try
	// {
	// result=getTrafficMapsVServerPAS(adcIndex, searchKeys, fromIndex, endIndex,
	// orderType, orderDir, db);
	// }
	// catch(OBException e)
	// {
	// db.closeDB();
	// throw new OBException(OBException.ERRCODE_MON_GETVSTRFAFFIC, e);
	// }
	// catch(Exception e)
	// {
	// db.closeDB();
	// OBException e1 = new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
	// throw new OBException(OBException.ERRCODE_MON_GETVSTRFAFFIC, e1);
	// }
	// db.closeDB();
	// return result;
	// }

	// @Override
	// public ArrayList<OBDtoTrafficMapVServer> getTrafficMapsVServerPASK(Integer
	// adcIndex, String searchKeys, Integer fromIndex, Integer endIndex, Integer
	// orderType, Integer orderDir) throws OBException
	// {
	// ArrayList<OBDtoTrafficMapVServer> result;
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// db.openDB();
	// }
	// catch(OBException e)
	// {
	// throw new OBException(OBException.ERRCODE_MON_GETVSTRFAFFIC, e);
	// }
	//
	// try
	// {
	// result=getTrafficMapsVServerPASK(adcIndex, searchKeys, fromIndex, endIndex,
	// orderType, orderDir, db);
	// }
	// catch(OBException e)
	// {
	// db.closeDB();
	// throw new OBException(OBException.ERRCODE_MON_GETVSTRFAFFIC, e);
	// }
	// catch(Exception e)
	// {
	// db.closeDB();
	// OBException e1 = new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
	// throw new OBException(OBException.ERRCODE_MON_GETVSTRFAFFIC, e1);
	// }
	// db.closeDB();
	// return result;
	// }

	// @Override
	// public ArrayList<OBDtoTrafficMapVService>
	// getTrafficMapsVServiceAlteon(Integer adcIndex, String searchKeys, Integer
	// fromIndex, Integer endIndex, Integer orderType, Integer orderDir) throws
	// OBException
	// {
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d,
	// searchKeys:%s", adcIndex, searchKeys));
	//
	// ArrayList<OBDtoTrafficMapVService> result = new
	// ArrayList<OBDtoTrafficMapVService> ();
	// // OBDatabase db = new OBDatabase();
	// // try
	// // {
	// // db.openDB();
	// // }
	// // catch(OBException e)
	// // {
	// // throw new OBException(OBException.ERRCODE_MON_GETVSTRFAFFIC, e);
	// // }
	// //
	// // try
	// // {
	// // result=getTrafficMapsVServiceAlteon(adcIndex, searchKeys, fromIndex,
	// endIndex, orderType, orderDir, db);
	// // }
	// // catch(OBException e)
	// // {
	// // db.closeDB();
	// // throw new OBException(OBException.ERRCODE_MON_GETVSTRFAFFIC, e);
	// // }
	// // catch(Exception e)
	// // {
	// // db.closeDB();
	// // OBException e1 = new
	// OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
	// // throw new OBException(OBException.ERRCODE_MON_GETVSTRFAFFIC, e1);
	// // }
	// // db.closeDB();
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "end. result count = " +
	// result.size());
	// return result;
	// }

	@Override
	public OBDtoTrafficMapVServiceMembers getTrafficMapsVServiceDetailF5(Integer adcIndex, String vsIndex,
			Integer orderType, Integer orderDir) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d, vsIndex:%s", adcIndex, vsIndex));

		OBDtoTrafficMapVServiceMembers retVal = new OBDtoTrafficMapVServiceMembers();

		try {
			OBDtoADCObject object = new OBDtoADCObject();
			object.setCategory(OBDtoADCObject.CATEGORY_VS);
			object.setStrIndex(vsIndex);
			retVal = new OBFaultMonitoringImpl().getSvcPerfVSrvMemberInfo(object, null, null);
			return retVal;
		} catch (OBException e) {
			throw e;// new OBException(OBException.ERRCODE_MON_GETVSTRFAFFIC, e);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
	}

	@Override
	public OBDtoFaultGroupMemberPerfInfo getTrafficMapsFlbGroupDetailAlteon(Integer adcIndex, String groupIndex)
			throws OBException {
		return getTrafficMapsFlbGroupDetailAlteon(adcIndex, groupIndex, OBDefine.ORDER_TYPE_CPS,
				OBDefine.ORDER_DIR_DESCEND);// TODO
	}

	public OBDtoFaultGroupMemberPerfInfo getTrafficMapsFlbGroupDetailAlteon(Integer adcIndex, String groupIndex,
			Integer orderType, Integer orderDir) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, groupIndex:%s", adcIndex, groupIndex));

		OBDtoFaultGroupMemberPerfInfo retVal = new OBDtoFaultGroupMemberPerfInfo();

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();
			retVal = new OBFaultMonitoringImpl().getGroupMemberPerformanceList(groupIndex, null, null);
			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public ArrayList<OBDtoFlbFilterInfo> getTrafficMapsFlbGroupFilterDetailAlteon(Integer adcIndex, String groupIndex)
			throws OBException {
		return getTrafficMapsFlbGroupFilterDetailAlteon(adcIndex, groupIndex, null);
	}

	public ArrayList<OBDtoFlbFilterInfo> getTrafficMapsFlbGroupFilterDetailAlteon(Integer adcIndex, String groupIndex,
			OBDtoOrdering order) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, groupIndex:%s", adcIndex, groupIndex));

		ArrayList<OBDtoFlbFilterInfo> retVal = new ArrayList<OBDtoFlbFilterInfo>();

		OBDatabase db = new OBDatabase();
		OBDtoSearch condition = new OBDtoSearch();
		condition.setSearchKey(groupIndex);
		condition.setBeginIndex(null);
		condition.setEndIndex(null);
		// OBDtoOrdering orderOption = new OBDtoOrdering();
		// orderOption.setOrderDirection(orderDir);
		// orderOption.setOrderType(orderType);
		try {
			db.openDB();
			retVal = new OBMonitoringFlbAlteonImpl().getFilterInfoByGroupId(adcIndex, condition, null);
			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public OBDtoTrafficMapVServiceMembers getTrafficMapsVServiceDetailPAS(Integer adcIndex, String vsIndex,
			Integer orderType, Integer orderDir) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d, vsIndex:%s", adcIndex, vsIndex));

		OBDtoTrafficMapVServiceMembers retVal = new OBDtoTrafficMapVServiceMembers();

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			OBDtoADCObject object = new OBDtoADCObject();
			object.setCategory(OBDtoADCObject.CATEGORY_VS);
			object.setStrIndex(vsIndex);
			retVal = new OBFaultMonitoringImpl().getSvcPerfVSrvMemberInfo(object, null, null, db);
			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public OBDtoTrafficMapVServiceMembers getTrafficMapsVServiceDetailPASK(Integer adcIndex, String vsIndex,
			Integer orderType, Integer orderDir) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d, vsIndex:%s", adcIndex, vsIndex));

		OBDtoTrafficMapVServiceMembers retVal = new OBDtoTrafficMapVServiceMembers();

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			OBDtoADCObject object = new OBDtoADCObject();
			object.setCategory(OBDtoADCObject.CATEGORY_VS);
			object.setStrIndex(vsIndex);
			retVal = new OBFaultMonitoringImpl().getSvcPerfVSrvMemberInfo(object, null, null, db);

			return retVal;
		} catch (OBException e) {
			throw e;// new OBException(OBException.ERRCODE_MON_GETVSTRFAFFIC, e);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public OBDtoTrafficMapVServiceMembers getTrafficMapsVServiceDetailAlteon(Integer adcIndex, String vsIndex,
			Integer srvPort, Integer orderType, Integer orderDir) throws OBException {
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, vsIndex:%s, srvPort:%d", adcIndex, vsIndex, srvPort));

		OBDtoTrafficMapVServiceMembers retVal = new OBDtoTrafficMapVServiceMembers();

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String vsvcIndex = OBCommon.makeVSrcIndexAlteon(adcIndex, vsIndex, srvPort);
			OBDtoADCObject object = new OBDtoADCObject();
			object.setCategory(OBDtoADCObject.CATEGORY_VSVC);
			object.setStrIndex(vsvcIndex);
			retVal = new OBFaultMonitoringImpl().getSvcPerfVSrvMemberInfo(object, null, null, db);

			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private String getPortStatusOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER PORT_INDEX DESC NULLS LAST ";
		switch (orderType) {
		case OBDefine.ORDER_TYPE_NAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY PORT_INDEX ASC NULLS LAST ";
			else
				retVal = " ORDER BY PORT_INDEX DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_BPS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY BPS_SUM ASC NULLS LAST ";
			else
				retVal = " ORDER BY BPS_SUM DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_PPS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY PPS_SUM ASC NULLS LAST ";
			else
				retVal = " ORDER BY PPS_SUM DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_STATUS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY STATUS ASC NULLS LAST ";
			else
				retVal = " ORDER BY STATUS DESC NULLS LAST ";
			break;
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// OBMonitoringImpl mon = new OBMonitoringImpl();
	//
	// try
	// {
	// ArrayList<OBDtoAdcPortStatus> list = mon.getPortStatus(1,
	// OBDefine.ORDER_TYPE_NAME, OBDefine.ORDER_DIR_ASCEND);
	// for(OBDtoAdcPortStatus vs:list)
	// System.out.println(vs);
	// System.out.println(list);
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	@Override
	public ArrayList<OBDtoAdcPortStatus> getPortStatus(Integer adcIndex, Integer orderType, Integer orderDir)
			throws OBException {
		ArrayList<OBDtoAdcPortStatus> result = new ArrayList<OBDtoAdcPortStatus>();
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();
			HashMap<String, Timestamp> lastLinkUptimeMap = getLastLinkUptime(adcIndex, db);

			int rescInterval = new OBEnvManagementImpl().getAdcSyncInterval(db);
			sqlText = String.format(
					" SELECT A.PORT_INDEX AS PORT_INDEX, B.NAME AS PORT_NAME, B.ALIAS_NAME AS PORT_ALIAS_NAME, B.SPEED, A.STATUS, \n"
							+ "     A.PPS_IN, A.PPS_OUT, (A.PPS_IN+A.PPS_OUT) AS PPS_SUM,                       \n"
							+ "     A.BPS_IN, A.BPS_OUT, (A.BPS_IN+A.BPS_OUT) AS BPS_SUM,                       \n"
							+ "     A.EPS_IN, A.EPS_OUT, A.DPS_IN, A.DPS_OUT, A.OCCUR_TIME                      \n"
							+ " FROM TMP_FAULT_LINK_STATS            A                                          \n"
							+ " INNER JOIN TMP_FAULT_LINK_INFO       B                                          \n"
							+ " ON B.PORT_INDEX = A.PORT_INDEX   AND A.ADC_INDEX = B.ADC_INDEX                  \n"
							+ " WHERE A.ADC_INDEX=%d                                                            \n",
					adcIndex);

			sqlText += getPortStatusOrderType(orderType, orderDir);

			ResultSet rs = db.executeQuery(sqlText);

			String aliasName;
			while (rs.next()) {
				OBDtoAdcPortStatus status = new OBDtoAdcPortStatus();

				String occur = db.getString(rs, "OCCUR_TIME");

				if (occur != null && OBDateTime.getTimeIntervalCheck(occur, rescInterval)) {
					status.setBytesIn(db.getLong(rs, "BPS_IN"));
					status.setBytesOut(db.getLong(rs, "BPS_OUT"));
					status.setBytesTot(status.getBytesIn() + status.getBytesOut());
					status.setDropsIn(db.getLong(rs, "DPS_IN"));
					status.setDropsOut(db.getLong(rs, "DPS_OUT"));
					status.setDropsTot(status.getDropsIn() + status.getDropsOut());
					status.setErrorsIn(db.getLong(rs, "EPS_IN"));
					status.setErrorsOut(db.getLong(rs, "EPS_OUT"));
					status.setErrorsTot(status.getErrorsIn() + status.getErrorsOut());
					status.setPktsIn(db.getLong(rs, "PPS_IN"));
					status.setPktsOut(db.getLong(rs, "PPS_OUT"));
					status.setPktsTot(status.getPktsIn() + status.getPktsOut());
				} else {
					status.setBytesIn(-1l);
					status.setBytesOut(-1l);
					status.setBytesTot(-1l);
					status.setDropsIn(-1l);
					status.setDropsOut(-1l);
					status.setDropsTot(-1l);
					status.setErrorsIn(-1l);
					status.setErrorsOut(-1l);
					status.setErrorsTot(-1l);
					status.setPktsIn(-1l);
					status.setPktsOut(-1l);
					status.setPktsTot(-1l);
				}

				status.setIntfName(db.getString(rs, "PORT_NAME").toUpperCase());
				aliasName = db.getString(rs, "PORT_ALIAS_NAME");
				if (aliasName != null && aliasName.isEmpty() == false) // null 참조 방지!!
				{
					status.setDispName(status.getIntfName() + "(" + aliasName + ")");
				} else {
					status.setDispName(status.getIntfName());
				}

				// String portName = db.getString(rs, "PORT_INDEX");
				String portName = "PORT" + db.getString(rs, "PORT_INDEX");
				Integer portStatus = db.getInteger(rs, "STATUS");
				status.setLinkStatus(portStatus);
				if (portStatus.equals(OBDtoAdcPortStatus.STATUS_DOWN)) {
					if (lastLinkUptimeMap.get(portName) == null) {// 사용된적이 없는 경우.
						status.setLinkStatus(OBDtoAdcPortStatus.STATUS_UNCONNECT);
					}
				}
				result.add(status);
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
		return result;
	}

	private final static Integer MAX_PORT_COUNT = 36;

	private HashMap<String, Timestamp> getLastLinkUptime(Integer adcIndex, OBDatabase db) throws OBException {
		HashMap<String, Timestamp> retVal = new HashMap<String, Timestamp>();

		String sqlText = "";
		try {
			// LOG_LINK_UPTIME_STATUS 에서 링크 상태 조회.
			sqlText = String.format(" SELECT OCCUR_TIME,                                                     \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
					+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
					+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36                         \n"
					+ " FROM LOG_LINK_UPTIME_STATUS                                            \n"
					+ " WHERE ADC_INDEX = %d                                                   \n"
					+ " LIMIT 1                                                                \n", adcIndex);
			ResultSet rs;
			rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				for (int portIndex = 1; portIndex <= MAX_PORT_COUNT; portIndex++) {
					String portName = "PORT" + portIndex;
					Timestamp upTime = db.getTimestamp(rs, portName);
					if (upTime != null) {
						retVal.put(portName, upTime);
					}
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

	public void testGetAlertLog() {
		OBDtoADCObject adcObject = new OBDtoADCObject(OBDtoADCObject.CATEGORY_GROUP, 1, null);
		OBDtoSearch searchOption = new OBDtoSearch();
		Integer accountIndex = 0;
		try {
			getAlertLog(adcObject, searchOption, null, accountIndex);
		} catch (OBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<OBDtoAdcAlertLog> getAlertLog(OBDtoADCObject adcObject, OBDtoSearch searchOption,
			OBDtoOrdering orderOption, Integer accountIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcObject:%s, searchOption:%s, orderOption:%s",
				adcObject, searchOption, orderOption));
		ArrayList<OBDtoAdcAlertLog> result = new ArrayList<OBDtoAdcAlertLog>();

		OBDatabase db = new OBDatabase();

		String sqlText = "";
		String sqlSearch = "";
		String sqlTime = "";
		String sqlLimit = "";

		try {
			db.openDB();
			Timestamp checkTime = new OBAlertImpl().getUserAlertCheckTime(accountIndex, db);

			// 사용자의 유효 adcIndex를 구한다.
			String userAdcIndexString = new OBAdcManagementImpl().getUsersAdcIndexString(adcObject.getCategory(),
					adcObject.getIndex(), accountIndex);

			if (userAdcIndexString.isEmpty() == true) { // 없으면 그만한다. 빈 list return
				return result;
			}

			if (searchOption.getSearchKey() != null && searchOption.getSearchKey().isEmpty() == false) {
				// #3984-2 #7: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchOption.getSearchKey()) + "%";
				sqlSearch = String.format(" ( ADC_NAME LIKE %s OR EVENT LIKE %s ) ", OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			}
			if (searchOption.getToTime() != null) {
				sqlTime = String.format(" OCCUR_TIME <= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getToTime().getTime()))));
			}
			if (searchOption.getFromTime() != null) {
				if (sqlTime.isEmpty() == false) {
					sqlTime += " AND ";
				}
				sqlTime += String.format(" OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getFromTime().getTime()))));
			}

			int offset = 0;
			if (searchOption.getBeginIndex() != null) {
				offset = searchOption.getBeginIndex().intValue();
			}
			int limit = 0;
			if (searchOption.getEndIndex() != null) {
				limit = Math.abs(searchOption.getEndIndex().intValue() - offset) + 1;
				if (limit > PAGE_MAX_LINE) {
					limit = PAGE_MAX_LINE;
				}
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			} else {
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", PAGE_MAX_LINE, offset);
			}

			sqlText = String
					.format(" SELECT OCCUR_TIME, ALARM_TIME, ADC_INDEX, ADC_NAME, TYPE, STATUS, EVENT, ACTION \n"
							+ " FROM LOG_ADC_ALARM   \n" + " WHERE ADC_INDEX IN ( %s ) \n", userAdcIndexString);

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			sqlText += getAlertLogOrderby(orderOption);

			sqlText += sqlLimit;

			ResultSet rs = db.executeQuery(sqlText);
			Timestamp occurTime = null;
			Timestamp alertTime = null;
			while (rs.next()) {
				OBDtoAdcAlertLog log = new OBDtoAdcAlertLog();
				occurTime = db.getTimestamp(rs, "OCCUR_TIME");
				alertTime = db.getTimestamp(rs, "ALARM_TIME");
				log.setOccurTime(OBDateTime.toDate(occurTime));
				log.setAlertTime(OBDateTime.toDate(alertTime));
				log.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				log.setAdcName(db.getString(rs, "ADC_NAME"));
				log.setType(db.getInteger(rs, "TYPE"));
				log.setStatus(db.getInteger(rs, "STATUS"));
				log.setEvent(db.getString(rs, "EVENT"));
				log.setActionSyslog(new OBAlarmImpl().int2Actions(db.getInteger(rs, "ACTION")).getSyslog());
				log.setActionSnmptrap(new OBAlarmImpl().int2Actions(db.getInteger(rs, "ACTION")).getSnmptrap());
				log.setActionSMS(new OBAlarmImpl().int2Actions(db.getInteger(rs, "ACTION")).getSms());

				if (occurTime.after(checkTime)) {
					log.setNew(true); // CURRENT_CHECK_TIME 나중이면 NEW로 표시한다.
				} else {
					log.setNew(false);
				}
				result.add(log);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	private String getAlertLogOrderby(OBDtoOrdering ordering) throws OBException {
		// DEFAULT = OCCUR_TIME
		// 1st = OCCUR_TIME
		// 2nd = ALARM_TIME
		// 3rd = ADC_NAME
		// 4rd = TYPE
		String orderSql = "";

		switch (ordering.getOrderType()) {
		case OBDtoOrdering.TYPE_1FIRST: // 1st = OCCUR_TIME
			if (ordering.getOrderDirection() == OBDtoOrdering.DIR_DESCEND) {
				orderSql = " ORDER BY OCCUR_TIME DESC, ADC_NAME ";
			} else {
				orderSql = " ORDER BY OCCUR_TIME, ADC_NAME ";
			}
			break;
		case OBDtoOrdering.TYPE_2SECOND: // 2nd = ALARM_TIME
			if (ordering.getOrderDirection() == OBDtoOrdering.DIR_DESCEND) {
				orderSql = " ORDER BY ALARM_TIME DESC, ADC_NAME ";
			} else {
				orderSql = " ORDER BY ALARM_TIME, ADC_NAME ";
			}
			break;
		case OBDtoOrdering.TYPE_3THIRD: // 3rd = ADC_NAME
			if (ordering.getOrderDirection() == OBDtoOrdering.DIR_DESCEND) {
				orderSql = " ORDER BY ADC_NAME DESC, OCCUR_TIME ";
			} else {
				orderSql = " ORDER BY ADC_NAME, OCCUR_TIME ";
			}
			break;
		case OBDtoOrdering.TYPE_4FOURTH: // 4rd = TYPE
			// 화면에 보이는 TYPE은 DB의 TYPE과 STATUS가 조합된 것이다. 따라서 주 컬럼인 type, 부 컬럼 status 순으로 조합
			// 정렬한다.TYPE:(장애 OR 경보), STATUS: 발생/해결
			if (ordering.getOrderDirection() == OBDtoOrdering.DIR_DESCEND) {
				orderSql = " ORDER BY TYPE DESC NULLS LAST, STATUS DESC NULLS LAST, OCCUR_TIME, ADC_NAME ";
			} else {
				orderSql = " ORDER BY TYPE NULLS LAST, STATUS NULLS LAST, OCCUR_TIME, ADC_NAME ";
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

	@Override
	public Integer getAlertLogCount(OBDtoADCObject adcObject, OBDtoSearch searchOption, Integer accountIndex)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcObject:%s, searchOption:%s", adcObject, searchOption));

		OBDatabase db = new OBDatabase();

		Integer result = 0;
		String sqlText = "";
		String sqlSearch = "";
		String sqlTime = "";

		try {
			db.openDB();

			// 사용자의 유효 adcIndex를 구한다.
			String userAdcIndexString = new OBAdcManagementImpl().getUsersAdcIndexString(adcObject.getCategory(),
					adcObject.getIndex(), accountIndex);
			if (userAdcIndexString.isEmpty() == true) { // 없으면 그만한다. 빈 list return
				return 0;
			}

			if (searchOption.getSearchKey() != null && searchOption.getSearchKey().isEmpty() == false) {
				// #3984-2 #7: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchOption.getSearchKey()) + "%";
				sqlSearch = String.format(" ( ADC_NAME LIKE %s OR EVENT LIKE %s ) ", OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			}
			if (searchOption.getToTime() != null) {
				sqlTime = String.format(" OCCUR_TIME <= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getToTime().getTime()))));
			}
			if (searchOption.getFromTime() != null) {
				if (sqlTime.isEmpty() == false) {
					sqlTime += " AND ";
				}
				sqlTime += String.format(" OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getFromTime().getTime()))));
			}

			sqlText = String.format(" SELECT COUNT(*) LOG_NUM FROM LOG_ADC_ALARM WHERE ADC_INDEX IN ( %s ) \n",
					userAdcIndexString);

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			if (sqlTime != null && !sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("sqlText:%s", sqlText));
			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next()) {
				result = db.getInteger(rs, "LOG_NUM");
			}
			// TODO
			int limit = new OBAdcManagementImpl().getPropertyLogLimitInfo();
			if (result > limit)
				result = limit;
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

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	@Override
	public ArrayList<OBDtoAdcSystemLog> getAdcSystemFaultLogExOrdering(OBDtoADCObject adcObject,
			OBDtoSearch searchOption, Integer accountIndex) throws OBException {
		OBDtoOrdering orderOption = new OBDtoOrdering();
		orderOption.setOrderDirection(OBDefine.ORDER_DIR_DESCEND);
		orderOption.setOrderType(OBDefine.ORDER_TYPE_OCCURTIME);
		return getAdcSystemFaultLog(adcObject, searchOption, orderOption, accountIndex);
	}

	// single 선택 필터 값을 구한다.
	private FilterValue getFilterNumberSingle(ArrayList<OBDtoMonTotalFilterUnit> filterList) {
		ArrayList<FilterValue> result = getFilterNumber(filterList, false); // isMulti=false, 1개 필터만 구한다.
		if (result.size() == 0) {
			FilterValue everyone = new FilterValue();
			everyone.setFrom(-1L);
			everyone.setTo(-1L);
			return everyone;
		} else {
			return result.get(0);
		}
	}

	// multi 선택 필터 값을 구한다. size = 0 then "전체"
	private ArrayList<FilterValue> getFilterNumberMulti(ArrayList<OBDtoMonTotalFilterUnit> filterList) {
		return getFilterNumber(filterList, true); // isMulti=true, 모든 선택 필터를 구한다. "전체"이면 필터가 없음이므로 목록 size=0
	}

	// 선택 필터 값을 구하는 함수 복수,단수 결과를 낸다. "전체"를 선택하면 빈 list를 리턴한다.
	private ArrayList<FilterValue> getFilterNumber(ArrayList<OBDtoMonTotalFilterUnit> filterList, boolean isMulti) { // 유의:
																														// 멀티
																														// 선택인
																														// 경우
																														// "전체"가
																														// 선택되면
																														// 다른
																														// 선택이
																														// 의미가
																														// 없으므로
																														// "전체"가
																														// 선택됐는지
																														// 체크해서
																														// 처리한다.
		ArrayList<FilterValue> result = new ArrayList<FilterValue>();

		if (filterList == null || filterList.size() == 0) {
			return result;
		}

		String[] temp; // from, to value 임시
		FilterValue valueTemp; // 범위 임시

		for (OBDtoMonTotalFilterUnit filter : filterList) {
			if (filter.isSelect() == true && filter.getValue() != null) {
				temp = filter.getValue().split(":");

				if (temp.length > 0 && temp[0].equals("-1")) // "전체"항목이다. 더 확인하지 않는다.
				{
					result.clear(); // 목록 비운다. 전체!
					break;
				}

				valueTemp = new FilterValue();
				if (temp.length == 1) // one value, 범위가 아님
				{
					valueTemp.setFrom(Long.parseLong(temp[0]));
					result.add(valueTemp);
					if (isMulti == false) // 단일 선택 필터이므로 더 확인하지 않는다.
					{
						break;
					}
				} else if (temp.length == 2) // from~to
				{
					valueTemp.setFrom(Long.parseLong(temp[0]));
					valueTemp.setTo(Long.parseLong(temp[1]));
					result.add(valueTemp);
					if (isMulti == false) // 단일 선택 필터이므로 더 확인하지 않는다.
					{
						break;
					}
				}
				// else = "전체"로 처리. 아무것도 없으면 전체를 의미하므로 처리가 필요 없다.
			}
		}
		return result;
	}

	// multi 선택 필터 값을 구한다. size = 0 then "전체"
	private ArrayList<FilterValue> getFilterStringMulti(ArrayList<OBDtoMonTotalFilterUnit> filterList) {
		return getFilterString(filterList, true); // isMulti=true, 모든 선택 필터를 구한다. "전체"이면 필터가 없음이므로 목록 size=0
	}

	// 선택 필터 값을 구하는 함수 복수,단수 결과를 낸다. "전체"를 선택하면 빈 list를 리턴한다. - 문자열 값 return용. 범위가
	// 없다.
	private ArrayList<FilterValue> getFilterString(ArrayList<OBDtoMonTotalFilterUnit> filterList, boolean isMulti) { // 유의:
																														// 멀티
																														// 선택인
																														// 경우
																														// "전체"가
																														// 선택되면
																														// 다른
																														// 선택이
																														// 의미가
																														// 없으므로
																														// "전체"가
																														// 선택됐는지
																														// 체크해서
																														// 처리한다.
		ArrayList<FilterValue> result = new ArrayList<FilterValue>();

		if (filterList == null || filterList.size() == 0) {
			return result;
		}
		String[] temp;
		FilterValue valueTemp;
		for (OBDtoMonTotalFilterUnit filter : filterList) {
			if (filter.isSelect() == true && filter.getValue() != null) {
				temp = filter.getValue().split(":");
				if (filter.getValue().equals("-1")) // "전체"항목이다. 더 확인하지 않는다.
				{
					result.clear(); // 목록 비운다. 전체!
					break;
				}

				valueTemp = new FilterValue();
				if (temp.length == 1) // single value
				{
					valueTemp.setWord1(temp[0]);
				} else if (temp.length == 2) // multi-value
				{
					valueTemp.setWord1(temp[0]);
					valueTemp.setWord2(temp[1]);
				}
				// else = "전체"로 처리. 아무것도 없으면 전체를 의미하므로 처리가 없다.

				result.add(valueTemp);

				if (isMulti == false) // 단일 선택 필터이므로 더 확인하지 않는다.
				{
					break;
				}
			}
		}
		return result;
	}

	public Model makeModelCodeAndShortName(Integer adcType, String model) // 모델 코드와 축약이름을 딴다.
	{
		if (model == null || model.isEmpty() == true) {
			return new Model(adcType, "", "Unknown", adcType + ":Unknown"); // modelCode = Unknown
		}

		// 모델 끝 부분의 모델코드(숫자파트+Alphabet) 뽑아서 'Alteon'을 붙여야 하므로 코드를 찾는다.
		// "Nortel Application Switch 2208 E", "Alteon Application Switch 3408", "Alteon
		// Application Switch 6420p"
		Pattern alteonCode = Pattern.compile("^\\D+(\\d+\\D*)");

		// 대부분 F5는 모델이 숫자로만 돼 있는데 일부는 'BIG-IP '가 붙어 있어서 뗀다.
		// 일부 "BIG-IP 3400"에서 모델 번호를 capturing group 1번으로 빼려면 앞의 'BIG-IP ' 파트를
		// non-capturing group으로 처리한다.
		Pattern f5CodeOnly = Pattern.compile("^(?:BIG-IP\\s*)?(\\d+)$");

		// PAS full name이 긴데 PAS로 축약하려면 찾아야 한다. "PIOLINK Application Switch 5016 (PAS
		// 5000)"
		// PASK는 축약형태로 나오므로 그대로 쓴다. PAS-K1516
		Pattern pasName = Pattern.compile("^PIOLINK Application Switch (.+)");

		Matcher matcher;
		String modelCode = "";
		// String modelShort = "";
		// model명 줄임 및 모델코드 따기
		if (adcType.equals(OBDefine.ADC_TYPE_ALTEON)) {
			if ((matcher = alteonCode.matcher(model)).matches() == true) {
				modelCode = matcher.group(1);
				// modelShort = "Alteon " + modelCode;
			} // else는 없다. 모두 맞아야 함
		} else if (adcType.equals(OBDefine.ADC_TYPE_F5)) {
			if ((matcher = f5CodeOnly.matcher(model)).matches() == true) {
				modelCode = matcher.group(1);
				// modelShort = "BIG-IP " + modelCode;
			} else
			// "BIG-IP"가 붙은 모델명
			{
				modelCode = model;
				// modelShort = model;
			}
		} else
		// PAS, PASK
		{
			if ((matcher = pasName.matcher(model)).matches() == true) {
				modelCode = matcher.group(1);
				// modelShort = "PAS " + modelCode;
			} else {
				modelCode = model;
				// modelShort = model;
			}
		}
		return new Model(adcType, model, modelCode, adcType + ":" + modelCode); // 원래 세번째 modelCode에 modelShort가 와야
																				// 했는데...그렇게 안 쓰기로 했다.
	}

	public ArrayList<OBDtoMonTotalFilterUnit> makeModelFilter(String userAdcIndexString, OBDatabase db) {
		ArrayList<OBDtoMonTotalFilterUnit> result = new ArrayList<OBDtoMonTotalFilterUnit>();
		OBDtoMonTotalFilterUnit tempFilter = new OBDtoMonTotalFilterUnit(0, "전체", "-1", true); // 모델 엔트리가 없어도 "전체"는 있다.
		result.add(tempFilter);
		Integer adcType;
		String model;
		Set<Model> modelList = new TreeSet<Model>();

		try {
			String sqlText = String.format(
					" SELECT TYPE, MODEL FROM MNG_ADC WHERE INDEX IN (%s) ORDER BY TYPE, MODEL NULLS LAST",
					userAdcIndexString); // 모델 없는게 제일 마지막 NULLS LAST
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				adcType = db.getInteger(rs, "TYPE");
				model = db.getString(rs, "MODEL");
				modelList.add(makeModelCodeAndShortName(adcType, model)); // 중복제거 and save and sort
			}

			for (Model entry : modelList) {
				result.add(new OBDtoMonTotalFilterUnit(entry.getAdcType(), entry.getModelShort(), entry.getModelCode(),
						false));
			}
		} catch (SQLException e) { // throw 하지 않고 모델을 못 뽑아도 "전체"로 내보낸다.
		}
		return result;
	}

	public ArrayList<OBDtoMonTotalFilterUnit> makeVersionFilter(String userAdcIndexString, OBDatabase db) {
		ArrayList<OBDtoMonTotalFilterUnit> result = new ArrayList<OBDtoMonTotalFilterUnit>();
		OBDtoMonTotalFilterUnit tempFilter = new OBDtoMonTotalFilterUnit(0, "전체", "-1", true); // 엔트리가 없어도 "전체"는 있다.
		result.add(tempFilter);
		Integer adcType;
		String version;
		Set<Model> versionList = new TreeSet<Model>(); // 임시로 모델 클래스를 빌려쓴다.

		try {
			String sqlText = String.format(
					" SELECT TYPE, SW_VERSION FROM MNG_ADC WHERE INDEX IN (%s) ORDER BY TYPE, SW_VERSION NULLS LAST",
					userAdcIndexString); // 버전 없는 게 제일 마지막으로. NULLS LAST
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				adcType = db.getInteger(rs, "TYPE");
				version = db.getString(rs, "SW_VERSION");
				if (version == null || version.isEmpty() == true) {
					version = "Unknown";
				}
				versionList.add(new Model(adcType, version, version, adcType + version)); // 중복제거 and save and sort
			}
			for (Model entry : versionList) { // adcType을 index에 저장한다. 할 곳이 없어서...
				result.add(new OBDtoMonTotalFilterUnit(entry.getAdcType(), entry.getModel(), entry.getModel(), false));
			}
		} catch (SQLException e) { // throw 하지 않고 모델을 못 뽑아도 "전체"로 내보낸다.
		}
		return result;
	}

	public ArrayList<OBDtoMonTotalFilterUnit> makeAdcTypeFilter(String userAdcIndexString, OBDatabase db) {
		ArrayList<OBDtoMonTotalFilterUnit> result = new ArrayList<OBDtoMonTotalFilterUnit>();
		OBDtoMonTotalFilterUnit tempFilter = new OBDtoMonTotalFilterUnit(0, "전체", "-1", true); // 엔트리가 없어도 "전체"는 있다.
		result.add(tempFilter);
		Integer adcType;
		String adcTypeS;
		try { // 유효한 모든 ADC TYPE을 구한다. alias 'TYPE'을 쓰면 SQL에러가 나므로 'ADC_TYPE'로 한다.
			String sqlText = String.format(
					" SELECT DISTINCT(TYPE) ADC_TYPE FROM MNG_ADC WHERE INDEX IN (%s) ORDER BY ADC_TYPE ",
					userAdcIndexString);
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				adcType = db.getInteger(rs, "ADC_TYPE");
				if (adcType.equals(OBDefine.ADC_TYPE_F5)) {
					adcTypeS = "F5";
				} else if (adcType.equals(OBDefine.ADC_TYPE_ALTEON)) {
					adcTypeS = "Alteon";
				} else if (adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PAS)) {
					adcTypeS = "PAS";
				} else if (adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PASK)) {
					adcTypeS = "PASK";
				} else {
					adcTypeS = "Unknown";
				}
				result.add(new OBDtoMonTotalFilterUnit(adcType, adcTypeS, adcType.toString(), false));
			}
		} catch (SQLException e) { // throw 하지 않고 모델을 못 뽑아도 "전체"로 내보낸다.
		}
		return result;
	}

	public ArrayList<OBDtoMonTotalFilterUnit> makePortFilter(String userAdcIndexString, OBDatabase db) {
		ArrayList<OBDtoMonTotalFilterUnit> result = new ArrayList<OBDtoMonTotalFilterUnit>();
		OBDtoMonTotalFilterUnit tempFilter = new OBDtoMonTotalFilterUnit(0, "전체", "-1", true); // 엔트리가 없어도 "전체"는 있다.
		result.add(tempFilter);
		Integer port;
		ArrayList<String> portList = new ArrayList<String>();

		try {// F5와 ALTEON을 따로 조회하고 컬럼을 ALIAS로 맞춰서 UNION해야한다, union후 중복을 제거하려고 겉에서 한번더
				// distinct로 뽑는다.
			String sqlText = String.format(
					" SELECT DISTINCT(port) FROM \n" + " ( SELECT DISTINCT(VIRTUAL_PORT) PORT FROM TMP_SLB_VSERVER \n"
							+ "   WHERE VIRTUAL_PORT IS NOT NULL \n" + // F5, PAS, PASK
							"   UNION ALL SELECT DISTINCT(VIRTUAL_PORT) PORT FROM TMP_SLB_VS_SERVICE \n"
							+ "   WHERE VIRTUAL_PORT IS NOT NULL ) A \n" + // ALTEON
							" ORDER BY PORT ",
					userAdcIndexString, userAdcIndexString);

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				port = db.getInteger(rs, "PORT");
				if (port != null) {
					portList.add(Integer.toString(port));
				}
			}
			int i = 0;
			for (String strport : portList) {
				result.add(new OBDtoMonTotalFilterUnit(i++, strport, strport, false));
			}
		} catch (SQLException e) { // throw 하지 않고 모델을 못 뽑아도 "전체"로 내보낸다.
		}
		return result;
	}

	@Override
	public OBDtoMonTotalAdc getTotalAdcListToExport(OBDtoAdcScope scope, Integer accountIndex,
			OBDtoMonTotalAdcCondition condition, Integer beginIndex, Integer endIndex) throws OBException {
		OBDtoOrdering orderOption = new OBDtoOrdering();
		orderOption.setOrderDirection(OBDefine.ORDER_DIR_DESCEND);
		orderOption.setOrderType(OBDefine.ORDER_TYPE_VSNAME);
		return getTotalAdcList(scope, accountIndex, condition, beginIndex, endIndex, orderOption.getOrderType(),
				orderOption.getOrderDirection());
	}

	@Override
	public OBDtoMonTotalAdc getTotalAdcList(OBDtoAdcScope scope, Integer accountIndex,
			OBDtoMonTotalAdcCondition condition, Integer beginIndex, Integer endIndex, Integer orderType,
			Integer orderDir) throws OBException {
		OBDtoMonTotalAdc result = new OBDtoMonTotalAdc();
		OBDatabase db = new OBDatabase();
		String userAdcIndexString;

		String sqlText = "";
		String sqlAdc = "";
		String sqlActive = "";
		String sqlPerformance = "";
		String sqlCert = "";
		String sqlFilter = "";
		String sqlInterface = "";
		String sqlDpsEps = "";
		String sqlSyslog24h = "";
		String sqlSlbConfig24h = "";
		String sqlMemory = "";
		String sqlCpu = "";

		String extra = "";
		String sqlWhere = "";
		ArrayList<String> whereItem = new ArrayList<String>();
		FilterValue singleFilterValue = null;
		ArrayList<FilterValue> multiFilterValue = null;
		int dataValidTerm; // seconds, 실시간 모니터링 데이터 유효시간

		db.openDB();
		try {
			// 데이터 유효 시간 체크에 쓰일 업데이트 주기, 못 구하면 기본 주기인 300초로 한다.
			dataValidTerm = new OBEnvManagementImpl().getAdcSyncInterval(db) * 2;
			if (dataValidTerm <= 0) {
				dataValidTerm = 300;
			}
			// 사용자의 유효 adcIndex를 구한다.
			userAdcIndexString = new OBAdcManagementImpl().getUsersAdcIndexString(scope.getLevel(), scope.getIndex(),
					accountIndex);
			// 최초 검색이어서 조건이 없으면 검색조건을 초기화 한다.
			if (condition == null) // 기존 condition이 없으면 기본값 채운다.
			{
				condition = new OBDtoMonTotalAdcCondition();
				condition.getModel().setFilter(makeModelFilter(userAdcIndexString, db)); // 모델 필터: 실제 ADC들을 취합
				condition.getSwVersion().setFilter(makeVersionFilter(userAdcIndexString, db)); // sw version 필터: 실제
																								// ADC들을 취합
				condition.getType().setFilter(makeAdcTypeFilter(userAdcIndexString, db)); // type 필터: 실제 있는 장비 유형만 붙인다.
			}
			result.setCondition(condition);
			if (userAdcIndexString.isEmpty() == true) { // 선택된 adc가 없으면 빈 result return
				return result;
			}

			// ADC 정보 - sql
			sqlAdc = String.format(" SELECT INDEX, IPADDRESS, TYPE, STATUS, \n"
					+ "     CASE WHEN STATUS=0 THEN 3 ELSE STATUS END STATUS_SORT, NAME, MODEL, SW_VERSION, \n"
					+ "     CASE WHEN (now()-LAST_BOOT_TIME) >= INTERVAL '24 HOURS' THEN DATE_TRUNC('DAY',(now()-LAST_BOOT_TIME))::VARCHAR ELSE '0 days' END UP_AGE, APPLY_TIME \n"
					+ " FROM MNG_ADC WHERE INDEX IN (%s) ", userAdcIndexString);

			// ADC 정보 - status 필터
			if (condition.getStatus().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, status는 필수선택이므로 선택돼 있다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getStatus().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}
					extra += String.format(" STATUS = %d ", entry.getFrom());
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					sqlAdc += String.format(" AND (%s) ", extra); // mng_adc는 left-join이 아닌 메인 테이블이므로 바로 필터를 적용할 수 있다.
				}
			}
			// ADC 정보 - type 필터
			if (condition.getType().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, type은 필수선택이므로 선택돼 있다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getType().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}
					extra += String.format(" TYPE = %d ", entry.getFrom());
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					sqlAdc += String.format(" AND (%s) ", extra); // mng_adc는 left-join이 아닌 메인 테이블이므로 바로 필터를 적용할 수 있다.
				}
			}
			// ADC 정보 - uptime 필터
			if (condition.getUptimeAge().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, uptime은 옵션이므로 선택 안될 수 있다.
			{
				singleFilterValue = getFilterNumberSingle(condition.getUptimeAge().getFilter());
				if (singleFilterValue.getFrom() >= 0) // uptime N일 이상
				{ // mng_adc는 left-join이 아닌 메인 테이블이므로 바로 필터를 적용할 수 있다.
					sqlAdc += String.format(" AND LAST_BOOT_TIME <= CURRENT_DATE - INTERVAL '%d DAYS' ",
							singleFilterValue.getFrom());
				}
			}
			// ADC 정보 - model 필터
			if (condition.getModel().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, model은 필수이므로 선택돼 있다.
			{
				extra = "";
				multiFilterValue = getFilterStringMulti(condition.getModel().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}
					// 모델은 축약됐기 때문에 like로 검색
					if (entry.getWord2() == null || entry.getWord2().equals("Unknown")) // 모델을 모르는 adc
					{
						extra += String.format(" (TYPE = %d AND (MODEL IS NULL OR MODEL = '')) ",
								Integer.parseInt(entry.getWord1()));
					} else { // 주의! 축약된 모델명은 full모델명의 맨 끝에 있다. 전후에 모두 %를 붙인 like를 쓰면 '3408'로 검색시 '3408 E'까지
								// 같이 나오므로 머리에만 %처리한다.
						extra += String.format(" (TYPE = %d AND MODEL like %s) ", Integer.parseInt(entry.getWord1()),
								OBParser.sqlString("%" + entry.getWord2()));
					}
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					sqlAdc += String.format(" AND (%s) ", extra); // mng_adc는 left-join이 아닌 메인 테이블이므로 바로 필터를 적용할 수 있다.
				}
			}
			// ADC 정보 - sw_version 필터
			if (condition.getSwVersion().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, swVersion은 필수이므로 선택돼 있다.
			{
				extra = "";
				multiFilterValue = getFilterStringMulti(condition.getSwVersion().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}
					if (entry.getWord1() == null || entry.getWord1().equals("Unknown")) // 버전을 모르는 adc
					{
						extra += String.format(" (SW_VERSION IS NULL OR SW_VERSION = '') ",
								OBParser.sqlString(entry.getWord1()));
					} else {
						extra += String.format(" SW_VERSION = %s ", OBParser.sqlString(entry.getWord1()));
					}
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					sqlAdc += String.format(" AND (%s) ", extra); // mng_adc는 left-join이 아닌 메인 테이블이므로 바로 필터를 적용할 수 있다.
				}
			}

			// ADC 정보 - 검색어: ADC이름
			if (condition.getSearchKeyword() != null && condition.getSearchKeyword().isEmpty() == false) // 검색어가 유효하면
																											// ADC이름
																											// 검색한다.
			{
				extra = "%" + OBParser.removeWildcard(condition.getSearchKeyword()) + "%";
				extra = String.format(" NAME LIKE %s ", OBParser.sqlString(extra));
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					sqlAdc += String.format(" AND %s ", extra); // mng_adc는 left-join이 아닌 메인 테이블이므로 조건을 즉시 적용할 수 있다.
				}
			}

			// active-standby - sql
			sqlActive = String.format(
					" SELECT ADC_INDEX, ACTIVE_BACKUP_STATE \n" + " FROM MNG_ADC_ADDITIONAL WHERE ADC_INDEX IN (%s) ",
					userAdcIndexString);
			// active-standby - 위상 필터
			if (condition.getActiveBackupState().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, Active-Standby는 필수선택이므로
																		// 선택돼 있다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getActiveBackupState().getFilter()); // 1:active,
																										// 2:backup,
																										// 0:Unknown
																										// (DB값 0, join후
																										// 없는 경우도 있으므로
																										// null-->0처리)
				for (FilterValue value : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{// active, standby 선택됨. 둘다 선택했을 수도 있음
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}
					extra += String.format(" ACTIVE_BACKUP_STATE = %d ", value.getFrom());
					if (value.getFrom() == 0) // unknown: join하면 null인 경우도 unknown으로 쳐야하므로 조건을 추가해야 한다.
					{
						if (extra.isEmpty() == false) {
							extra += " OR "; // 각 조건항을 logical-or로 연결한다.
						}
						extra += String.format(" ACTIVE_BACKUP_STATE IS NULL ", value.getFrom());
					}
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}

			// adc 트래픽, vs 상태별 개수
			sqlPerformance = String.format(
					" SELECT ADC_INDEX, BPS_TOT/2 BPS, CONN_CURR, VS_COUNT_AVAIL, VS_COUNT_UNAVAIL, VS_COUNT_DISABLED \n"
							+ "            FROM TMP_FAULT_ADC_PERF_STATS WHERE ADC_INDEX IN (%s) AND OCCUR_TIME > NOW() - INTERVAL '%d SECONDS' ",
					userAdcIndexString, dataValidTerm);

			String extra2 = "";
			if (condition.getConcurrentSession().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, 옵션이어서 선택 안됐을 수 있다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getConcurrentSession().getFilter());
				for (FilterValue value : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					extra2 = "";
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}

					if (value.getFrom() != null && value.getFrom() >= 0) {
						extra2 += String.format(" CONN_CURR >= %d ", value.getFrom());
					}
					if (value.getTo() != null && value.getTo() >= 0) {
						if (extra2.isEmpty() == false) {
							extra2 += " AND "; // 각 조건항을 logical-or로 연결한다.
						}
						extra2 += String.format(" CONN_CURR <= %d ", value.getTo());
					}
					if (extra2.isEmpty() == false) {
						extra2 = "(" + extra2 + ")";
					}
					extra += extra2;
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}
			if (condition.getThroughput().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, 옵션이어서 선택 안됐을 수 있다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getThroughput().getFilter());
				for (FilterValue value : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					extra2 = "";
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}

					if (value.getFrom() != null && value.getFrom() >= 0) {
						extra2 += String.format(" BPS >= %d ", value.getFrom());
					}
					if (value.getTo() != null && value.getTo() >= 0) {
						if (extra2.isEmpty() == false) {
							extra2 += " AND "; // 각 조건항을 logical-or로 연결한다.
						}
						extra2 += String.format(" BPS <= %d ", value.getTo());
					}
					if (extra2.isEmpty() == false) {
						extra2 = "(" + extra2 + ")";
					}
					extra += extra2;
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}

			// 인증서 유효기간 - 다른 항목들과 달리 aggregation function이 있어서 main sql이 filter값의 영향을 받으므로
			// filter를 먼저 확인한다.
			if (condition.getSslCertValidDays().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, 옵션이어서 선택 안됐을 수 있다.
			{
				extra = "";
				singleFilterValue = getFilterNumberSingle(condition.getSslCertValidDays().getFilter());
				if (singleFilterValue.getTo() >= 0) // 인증서 만료일 n일 이내
				{
					extra = String.format(" AND EXPIRATION_DATE < CURRENT_DATE + INTERVAL '%d DAYS' ",
							(singleFilterValue.getTo() + 1));
					whereItem.add(" CERT_COUNT > 0 "); // 최종적으로 필터가 적용되는 부분
				}
				sqlCert = String.format(" SELECT ADC_INDEX, COUNT(*) CERT_COUNT FROM TMP_ADC_CERTIFICATE \n"
						+ " WHERE ADC_INDEX IN (%s) %s \n" + " GROUP BY ADC_INDEX ", userAdcIndexString, extra);
			} else
			// 선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			{
				sqlCert = " SELECT -1 ADC_INDEX, -1 CERT_COUNT ";
			}

			// FLB filter 항목 처리. 이 항목은 검색필터가 없다.
			if (condition.getFilter().isSelect() == true) // 옵션이어서 선택 안됐을 수 있다. 필터 없음
			{
				sqlFilter = String.format(
						" SELECT ADC_INDEX, COUNT(*) FILTER_COUNT FROM TMP_FLB_FILTER \n"
								+ " WHERE ADC_INDEX IN (%s) AND STATE = %d \n" + " GROUP BY ADC_INDEX ",
						userAdcIndexString, OBDefine.STATE_ENABLE);
			} else
			// 선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			{
				sqlFilter = " SELECT -1 ADC_INDEX, 0 FILTER_COUNT ";
			}

			// 살아있는 포트 개수
			if (condition.getInterfaceCount().isSelect() == true) // 옵션이어서 선택 안됐을 수 있다. 필터 없음
			{
				sqlInterface = String.format(" SELECT ADC_INDEX, COUNT(*) INTERFACE_COUNT FROM TMP_FAULT_LINK_STATS \n"
						+ " WHERE ADC_INDEX IN ( %s ) AND STATUS = %d AND OCCUR_TIME > NOW() - INTERVAL '%d SECONDS' \n"
						+ " GROUP BY ADC_INDEX ", userAdcIndexString, OBDefine.STATUS_AVAILABLE, dataValidTerm);
			} else
			// 선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			{
				sqlInterface = " SELECT -1 ADC_INDEX, 0 INTERFACE_COUNT ";
			}

			// EPS, DPS //옵션이어서 선택 안됐을 수 있다. 필터 없음
			if (condition.getDropPackets().isSelect() == true || condition.getErrorPackets().isSelect() == true) {
				sqlDpsEps = String.format(" SELECT ADC_INDEX, SUM(DPSIN+DPSOUT) DPS_SUM, SUM(EPSIN+EPSOUT) EPS_SUM \n"
						+ " FROM (SELECT ADC_INDEX, \n"
						+ "     CASE WHEN DPS_IN>0  THEN DPS_IN  ELSE 0 END AS DPSIN,  \n"
						+ "     CASE WHEN DPS_OUT>0 THEN DPS_OUT ELSE 0 END AS DPSOUT, \n"
						+ "     CASE WHEN EPS_IN>0  THEN EPS_IN  ELSE 0 END AS EPSIN,  \n"
						+ "     CASE WHEN EPS_OUT>0 THEN EPS_OUT ELSE 0 END AS EPSOUT  \n"
						+ "     FROM TMP_FAULT_LINK_STATS \n"
						+ "     WHERE ADC_INDEX IN (%s) AND OCCUR_TIME > NOW() - INTERVAL '%d SECONDS' ) A \n"
						+ " GROUP BY ADC_INDEX ", userAdcIndexString, dataValidTerm);
			} else
			// eps, dps 둘다 선택 안됐으면 join 우회하는 대체 쿼리
			{
				sqlDpsEps = " SELECT -1 ADC_INDEX, 0 DPS_SUM, 0 EPS_SUM ";
			}

			// 24시간 syslog 수
			if (condition.getAdcLog24Hour().isSelect() == true) {
				sqlSyslog24h = String.format(" SELECT ADC_INDEX, COUNT(*) LOG24H_COUNT FROM LOG_ADC_SYSLOG \n"
						+ " WHERE ADC_INDEX IN ( %s ) AND OCCUR_TIME >= (CURRENT_TIMESTAMP - INTERVAL '24 HOURS') \n"
						+ " GROUP BY ADC_INDEX ", userAdcIndexString);
			} else
			// 선택 안됐으면 join 우회하는 대체 쿼리
			{
				sqlSyslog24h = " SELECT -1 ADC_INDEX, 0 LOG24H_COUNT ";
			}

			// 24시간 slb 설정 수
			if (condition.getSlbConfig24Hour().isSelect() == true) {
				sqlSlbConfig24h = String
						.format(" SELECT ADC_INDEX, COUNT(*) SLBCONFIG24H_COUNT FROM LOG_CONFIG_HISTORY \n"
								+ " WHERE ADC_INDEX IN ( %s ) AND OCCUR_TIME >= (CURRENT_TIMESTAMP - INTERVAL '24 HOURS') \n"
								+ " GROUP BY ADC_INDEX ", userAdcIndexString);
			} else
			// 선택 안됐으면 join 우회하는 대체 쿼리
			{
				sqlSlbConfig24h = " SELECT -1 ADC_INDEX, 0 SLBCONFIG24H_COUNT ";
			}

			// MEMORY USAGE
			if (condition.getMemory().isSelect() == true) {
				sqlMemory = String.format(
						" SELECT ADC_INDEX, MEM_USAGE FROM TMP_FAULT_RESC_CPUMEM \n"
								+ " WHERE ADC_INDEX IN ( %s ) AND OCCUR_TIME > NOW() - INTERVAL '%d SECONDS' ",
						userAdcIndexString, dataValidTerm);
			} else
			// 선택 안됐으면 join 우회하는 대체 쿼리
			{
				sqlMemory = " SELECT -1 ADC_INDEX, 0 MEM_USAGE ";
			}

			for (String item : whereItem) {
				if (sqlWhere.isEmpty() == true) {
					sqlWhere = " WHERE ";
				} else
				// 2번째 항부터 and로 연결
				{
					sqlWhere += " AND ";
				}
				sqlWhere += "(" + item + ")";
			}

			// 아래 SQL에서 숫자 데이터는 COALESCE()함수로 null을 -1로 만들고 "_VALUE"를 붙인 alias로 한번 더 뽑느다.
			// 원래 필드는 정렬에 필요하다. null last를 적용해야 하기 때문에 -1로 직접 바꿀 수 없다. 그러면 없는 값이 맨 위로 정렬될 수
			// 있다.
			// 표시하는 값은 '_VALUE'가 붙은 필드를 쓴다. 그러면 존재하지 않거나 구하지 못한 값은 -1로 나가는데, 화면에서는 복잡한 처리 없이
			// -1 --> '-' 처리하면 된다.
			// 안그러면 어떤 경우는 0이 나온데 대해 어떤 ADC는 '-'이고, 어떤 것은 진짜 0인지 식별 처리를 각 항목마다 다르게 해야한다.
			// 이 모든 문제는 record set의 getInteger() 함수가 null을 0으로 뽑는데서 기인했다. 지금에서 이 것을 null이
			// 나오게 바꾸면 기존에 자동으로 null-->0이 돼서
			// null 참조오류가 나지 않았던 부분들을 감당할 수 없을 수 있으므로 지금에 와서 바꾸는 것에는 신중해야 한다.

			sqlText = String.format(
					" SELECT A.INDEX, A.IPADDRESS, A.STATUS, A.STATUS_SORT, A.NAME, A.MODEL, A.SW_VERSION, A.UP_AGE, A.APPLY_TIME, A.TYPE, \n"
							+ " COALESCE(ACTIVE_BACKUP_STATE, 0) ACTIVE_BACKUP_STATE, C.BPS, COALESCE(C.BPS, -1) BPS_VALUE, \n"
							+ " C.CONN_CURR, COALESCE(C.CONN_CURR, -1) CONN_CURR_VALUE, C.VS_COUNT_AVAIL, \n"
							+ " COALESCE(C.VS_COUNT_AVAIL, -1) VS_COUNT_AVAIL_VALUE, D.CERT_COUNT, E.FILTER_COUNT, \n"
							+ " COALESCE(E.FILTER_COUNT, -1) FILTER_COUNT_VALUE, F.INTERFACE_COUNT, \n"
							+ " COALESCE(F.INTERFACE_COUNT, -1) INTERFACE_COUNT_VALUE, G.EPS_SUM, \n"
							+ " COALESCE(G.EPS_SUM, -1) EPS_SUM_VALUE, G.DPS_SUM, COALESCE(G.DPS_SUM, -1) DPS_SUM_VALUE, \n"
							+ " H.LOG24H_COUNT, I.SLBCONFIG24H_COUNT, J.MEM_USAGE, COALESCE(J.MEM_USAGE, -1) MEM_USAGE_VALUE \n"
							+ " FROM ( %s ) A                 \n" + // Adc
							" LEFT JOIN ( %s ) B            \n" + // AdcAdditional
							" ON A.INDEX = B.ADC_INDEX      \n" + " LEFT JOIN ( %s ) C            \n" + // ADCPerfStat
							" ON A.INDEX = C.ADC_INDEX      \n" + " LEFT JOIN ( %s ) D            \n" + // SSL 인증서 만료일
							" ON A.INDEX = D.ADC_INDEX      \n" + " LEFT JOIN ( %s ) E            \n" + // FLB filter
							" ON A.INDEX = E.ADC_INDEX      \n" + " LEFT JOIN ( %s ) F            \n" + // 살아 잇는
																										// interface 수
							" ON A.INDEX = F.ADC_INDEX      \n" + " LEFT JOIN ( %s ) G            \n" + // interface
																										// DPS, EPS
							" ON A.INDEX = G.ADC_INDEX      \n" + " LEFT JOIN ( %s ) H            \n" + // syslog 24시간
																										// 카운트
							" ON A.INDEX = H.ADC_INDEX      \n" + " LEFT JOIN ( %s ) I            \n" + // slb config
																										// 24시간 카운트
							" ON A.INDEX = I.ADC_INDEX      \n" + " LEFT JOIN ( %s ) J            \n" + // 메모리 사용율
							" ON A.INDEX = J.ADC_INDEX      \n" + " %s                            \n" // 각종 필터들때문에 들어가는
																										// where절 통으로
					, sqlAdc, sqlActive, sqlPerformance, sqlCert, sqlFilter, sqlInterface, sqlDpsEps, sqlSyslog24h,
					sqlSlbConfig24h, sqlMemory, sqlWhere);

			// order 처리
			sqlText += getListOrderByType(orderType, orderDir, new Order().TOTAL_ADC_COLS);

			// 페이징
			int offset = 0;
			String sqlRange = "";

			if (beginIndex != null && beginIndex.intValue() >= 0) {
				offset = beginIndex.intValue();
				sqlRange += " OFFSET " + beginIndex.intValue();
			}
			if (endIndex != null && endIndex.intValue() >= 0) {
				sqlRange += " LIMIT " + (Math.abs(endIndex.intValue() - offset) + 1);
			}
			sqlText += sqlRange;

			ArrayList<OBDtoMonTotalAdcOne> adcList = new ArrayList<OBDtoMonTotalAdcOne>();
			OBDtoMonTotalAdcOne tempAdc = null;

			ResultSet rs = db.executeQuery(sqlText);
			String tempS = "";
			while (rs.next()) {
				tempAdc = new OBDtoMonTotalAdcOne();
				tempAdc.setAdcIndex(db.getInteger(rs, "INDEX"));
				tempAdc.setAdcName(db.getString(rs, "NAME"));
				tempAdc.setAdcIp(db.getString(rs, "IPADDRESS"));
				tempAdc.setStatus(db.getInteger(rs, "STATUS"));
				tempAdc.setActiveBackupState(db.getInteger(rs, "ACTIVE_BACKUP_STATE"));
				tempAdc.setUptimeAge(db.getString(rs, "UP_AGE"));
				tempAdc.setConcurrentSession(db.getLong(rs, "CONN_CURR_VALUE"));
				tempAdc.setThroughput(db.getLong(rs, "BPS_VALUE"));
				tempAdc.setConfigTime(db.getTimestamp(rs, "APPLY_TIME"));
				tempAdc.setServiceAvailable(db.getInteger(rs, "VS_COUNT_AVAIL_VALUE"));
				// tempAdc.setServiceUnavailable(db.getInteger(rs, "VS_COUNT_UNAVAIL")); //안 써서
				// 안 뽑음
				// tempAdc.setServiceDisable(db.getInteger(rs, "VS_COUNT_DISABLED")); //안 써서 안
				// 뽑음
				tempAdc.setSslCertValidDays(db.getInteger(rs, "CERT_COUNT"));
				tempAdc.setFilterUse(db.getInteger(rs, "FILTER_COUNT_VALUE"));
				tempAdc.setInterfaceAvailable(db.getInteger(rs, "INTERFACE_COUNT_VALUE"));
				tempAdc.setDropPackets(db.getLong(rs, "DPS_SUM_VALUE"));
				tempAdc.setErrorPackets(db.getLong(rs, "EPS_SUM_VALUE"));
				tempAdc.setAdcLog24Hour(db.getInteger(rs, "LOG24H_COUNT"));
				tempAdc.setSlbConfig24Hour(db.getInteger(rs, "SLBCONFIG24H_COUNT"));
				tempAdc.setMemory(db.getInteger(rs, "MEM_USAGE_VALUE"));
				tempAdc.setAdcType(db.getInteger(rs, "TYPE"));

				// 모델 이름을 단축명으로 보여줬을 때의 처리 코드. 이후에 full name으로 바꿈
				// Model modelObject = null;
				// String model = db.getString(rs, "MODEL");
				// modelObject = makeModelCodeAndShortName(tempAdc.getAdcType(), model); //모델의
				// 단축 이름과 코드를 구한다. 단축 이름만 쓴다.
				// tempAdc.setModel(modelObject.getModelShort());
				tempAdc.setModel(db.getString(rs, "MODEL"));

				tempS = db.getString(rs, "SW_VERSION");
				if (tempS == null || tempS.isEmpty() == true) // 버전 모르는 ADC
				{
					tempS = "Unknown";
				}
				tempAdc.setSwVersion(tempS);

				adcList.add(tempAdc);
			}

			// CPU USAGE: CPU 개수가 유동적인데 고정 컬럼 테이블로 구성해서 평균을 query에서 계산할 수 없다. 코드로 계산한다. 그리고
			// Alteon은 MP를 뺀다.
			if (condition.getCpu().isSelect() == true) {
				sqlCpu = String.format(
						" SELECT ADC_INDEX, CPU1_USAGE,  CPU2_USAGE,  CPU3_USAGE,  CPU4_USAGE,  CPU5_USAGE,  CPU6_USAGE, \n"
								+ "      CPU7_USAGE,  CPU8_USAGE,  CPU9_USAGE,  CPU10_USAGE, CPU11_USAGE, CPU12_USAGE, CPU13_USAGE,\n"
								+ "      CPU14_USAGE, CPU15_USAGE, CPU16_USAGE, CPU17_USAGE, CPU18_USAGE, CPU19_USAGE, CPU20_USAGE,\n"
								+ "      CPU21_USAGE, CPU22_USAGE, CPU23_USAGE, CPU24_USAGE, CPU25_USAGE, CPU26_USAGE, CPU27_USAGE, \n"
								+ "      CPU28_USAGE, CPU29_USAGE, CPU30_USAGE, CPU31_USAGE, CPU32_USAGE  \n"
								+ " FROM TMP_FAULT_RESC_CPUMEM   \n"
								+ " WHERE ADC_INDEX IN ( %s ) AND OCCUR_TIME > NOW() - INTERVAL '%d SECONDS' \n",
						userAdcIndexString, dataValidTerm);

				ResultSet rs2 = db.executeQuery(sqlCpu);

				Integer[] cpu;
				ArrayList<Integer[]> cpuList = new ArrayList<Integer[]>();
				Integer cpuUsageSum;
				Integer cpuUsageAvg = -1;
				Integer cpuNum;
				while (rs2.next()) {
					cpu = new Integer[33]; // 0~32, 0에는 adcIndex
					cpu[0] = db.getInteger(rs2, "ADC_INDEX");
					cpu[1] = db.getInteger(rs2, "CPU1_USAGE");
					cpu[2] = db.getInteger(rs2, "CPU2_USAGE");
					cpu[3] = db.getInteger(rs2, "CPU3_USAGE");
					cpu[4] = db.getInteger(rs2, "CPU4_USAGE");
					cpu[5] = db.getInteger(rs2, "CPU5_USAGE");
					cpu[6] = db.getInteger(rs2, "CPU6_USAGE");
					cpu[7] = db.getInteger(rs2, "CPU7_USAGE");
					cpu[8] = db.getInteger(rs2, "CPU8_USAGE");
					cpu[9] = db.getInteger(rs2, "CPU9_USAGE");
					cpu[10] = db.getInteger(rs2, "CPU10_USAGE");
					cpu[11] = db.getInteger(rs2, "CPU11_USAGE");
					cpu[12] = db.getInteger(rs2, "CPU12_USAGE");
					cpu[13] = db.getInteger(rs2, "CPU13_USAGE");
					cpu[14] = db.getInteger(rs2, "CPU14_USAGE");
					cpu[15] = db.getInteger(rs2, "CPU15_USAGE");
					cpu[16] = db.getInteger(rs2, "CPU16_USAGE");
					cpu[17] = db.getInteger(rs2, "CPU17_USAGE");
					cpu[18] = db.getInteger(rs2, "CPU18_USAGE");
					cpu[19] = db.getInteger(rs2, "CPU19_USAGE");
					cpu[20] = db.getInteger(rs2, "CPU20_USAGE");
					cpu[21] = db.getInteger(rs2, "CPU21_USAGE");
					cpu[22] = db.getInteger(rs2, "CPU22_USAGE");
					cpu[23] = db.getInteger(rs2, "CPU23_USAGE");
					cpu[24] = db.getInteger(rs2, "CPU24_USAGE");
					cpu[25] = db.getInteger(rs2, "CPU25_USAGE");
					cpu[26] = db.getInteger(rs2, "CPU26_USAGE");
					cpu[27] = db.getInteger(rs2, "CPU27_USAGE");
					cpu[28] = db.getInteger(rs2, "CPU28_USAGE");
					cpu[29] = db.getInteger(rs2, "CPU29_USAGE");
					cpu[30] = db.getInteger(rs2, "CPU30_USAGE");
					cpu[31] = db.getInteger(rs2, "CPU31_USAGE");
					cpu[32] = db.getInteger(rs2, "CPU32_USAGE");

					cpuList.add(cpu);
				}

				for (OBDtoMonTotalAdcOne adc : adcList) {
					cpuUsageSum = 0;
					cpuNum = 0;
					for (Integer[] cpuOne : cpuList) {
						if (adc.getAdcIndex().equals(cpuOne[0])) {
							for (int i = 1; i <= 32; i++) {
								if (cpuOne[i] >= 0) // -1이면 cpu가 없는 것이므로 더하지 않는다.
								{
									cpuUsageSum += cpuOne[i];
									cpuNum++;
								}
							}
							if (adc.getAdcType().equals(OBDefine.ADC_TYPE_ALTEON) == true) // Alteon은 cpu1이 MP이므로 뺀다.
							{
								cpuUsageSum -= cpuOne[1];
								cpuNum--;
							}
							if (cpuNum > 0) {
								cpuUsageAvg = cpuUsageSum / cpuNum;
							} else
							// 개별 cpu가 없다고 인지된 경우. snmp 수집문제? cpu 컬럼이 모두 -1...
							{
								cpuUsageAvg = -1;
							}
							break;
						}
					}
					adc.setCpu(cpuUsageAvg); // 수집이 안 됐으면 초기값 -1을 갖고 있다.
				}
			}

			result.setAdcList(adcList);
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

	private String getListOrderByType(Integer orderType, Integer orderDir, String[] columns) throws OBException {
		String direction = "";
		String nullLast = " NULLS LAST ";

		// String cols [] = new Order().TOTAL_ADC_COLS;
		int colNum = columns.length;
		int i;

		if (orderDir == OBDefine.ORDER_DIR_DESCEND) {
			direction = " DESC ";
		}
		// 선택한 column을 최우선 정렬로 잡는다.
		String order = " ORDER BY " + columns[orderType] + direction + nullLast;

		for (i = 1; i < colNum; i++) // column 번호는 1부터 메겼다.
		{
			if (i != orderType && columns[i] != null) // order 컬럼으로 선택되지 않은 컬럼을 후속 order로 붙인다. cols[i]이 null이면 정렬 항이
														// 아니므로 건너뛴다.
			{
				order += "," + columns[i] + nullLast;
			}
		}
		return order;
	}

	public void testGetTotalAdcList() {
		OBDtoAdcScope scope = new OBDtoAdcScope();
		scope.setLevel(OBDtoAdcScope.LEVEL_ALL);
		scope.setIndex(2);
		Integer accountIndex = 1;
		OBDtoMonTotalAdcCondition condition = null;

		condition = new OBDtoMonTotalAdcCondition();

		// ssl 인증서 만료일 - 범위값 ~이내
		condition.getSslCertValidDays().setSelect(true);
		condition.getSslCertValidDays().getFilter().get(0).setSelect(true);
		// condition.getSslCertValidDays().getFilter().get(5).setSelect(true);
		// condition.getSslCertValidDays().getFilter().get(1).setValue("0:3");

		// ADC연결상태
		// condition.getStatus().getFilter().get(0).setSelect(true);
		// condition.getStatus().getFilter().get(1).setSelect(true);
		// condition.getStatus().getFilter().get(2).setSelect(true);

		// active/backup state
		// condition.getActiveBackupState().getFilter().get(0).setSelect(false);
		// condition.getActiveBackupState().getFilter().get(1).setSelect(true);
		// condition.getActiveBackupState().getFilter().get(2).setSelect(true);
		// condition.getActiveBackupState().getFilter().get(3).setSelect(true);
		// //unknown

		// 세션 구간값. 복수선택 가능
		// condition.getConcurrentSession().getFilter().get(0).setSelect(false);
		// condition.getConcurrentSession().getFilter().get(1).setSelect(true);

		// 트래픽 구간값. 복수선택 가능
		// condition.getThroughput().getFilter().get(0).setSelect(false);
		// condition.getThroughput().getFilter().get(2).setSelect(true);

		// uptime
		// condition.getUptimeAge().getFilter().get(0).setSelect(false);
		// condition.getUptimeAge().getFilter().get(1).setSelect(true);

		// interface port
		// condition.getInterfaceCount().setSelect(true);
		// condition.getErrorPackets().setSelect(false);
		// condition.getDropPackets().setSelect(false);

		// 24시간 syslog
		// condition.getAdcLog24Hour().setSelect(true);
		// 24시간 slb config
		// condition.getSlbConfig24Hour().setSelect(true);

		// memory 사용율
		condition.getMemory().setSelect(true);

		// CPU 사용율
		condition.getCpu().setSelect(true);

		// search - adcname
		// condition.setSearchKeyword("192.");

		Integer beginIndex = 0;
		Integer endIndex = 100;
		Integer orderType = OBDefine.ORDER_TYPE_ADCNAME;
		Integer orderDir = OBDefine.ORDER_DIR_ASCEND;
		OBDtoMonTotalAdc result = null;

		try {
			System.out.println("count = " + getTotalAdcListCount(scope, accountIndex, condition));
			result = getTotalAdcList(scope, accountIndex, condition, beginIndex, endIndex, orderType, orderDir);
			// ADC 주요 값 보기
			for (OBDtoMonTotalAdcOne adc : result.getAdcList()) {
				System.out.println(String.format(
						"%d:%s\t st %d\t %s\t v=%s\t " + "up=%s\t cert %d\t flb %d\t "
								+ "session %s\t bps %d\t if %s\t " + "ifd %d\t ife %d\t " + "serv %d/%d/%d\t "
								+ "log %d config %d mem %d cpu %d",
						adc.getAdcIndex(), adc.getAdcName(), adc.getStatus(), adc.getModel(), adc.getSwVersion(),
						adc.getUptimeAge(), adc.getSslCertValidDays(), adc.getFilterUse(), adc.getConcurrentSession(),
						adc.getThroughput(), adc.getInterfaceAvailable(), adc.getDropPackets(), adc.getErrorPackets(),
						adc.getServiceAvailable(), adc.getServiceUnavailable(), adc.getServiceDisable(),
						adc.getAdcLog24Hour(), adc.getSlbConfig24Hour(), adc.getMemory(), adc.getCpu()));
			}

			for (OBDtoMonTotalFilterUnit filter : result.getCondition().getModel().getFilter()) {
				System.out.println(String.format("%d - %s, %s, %b", filter.getIndex(), filter.getTitle(),
						filter.getValue(), filter.isSelect()));
			}
			for (OBDtoMonTotalFilterUnit filter : result.getCondition().getSwVersion().getFilter()) {
				System.out.println(String.format("%d - %s, %s, %b", filter.getIndex(), filter.getTitle(),
						filter.getValue(), filter.isSelect()));
			}
		} catch (OBException e) {
			e.printStackTrace();
		}
	}

//    protected AdcSession session;
//	public void testGetTotalServiceList() {
//		OBDtoAdcScope scope = new OBDtoAdcScope();
//		scope.setLevel(OBDtoAdcScope.LEVEL_ALL);
//		// scope.setLevel(OBDtoAdcScope.LEVEL_ADC);
//		// scope.setIndex(7);
//		Integer accountIndex = 1;
////        String accountRole = session.getAccountRole();
//		OBDtoMonTotalServiceCondition condition = null;
//
//		condition = new OBDtoMonTotalServiceCondition();
//		// 서비스 상태
//		// condition.getStatus().getFilter().get(0).setSelect(false);
//		// condition.getStatus().getFilter().get(1).setSelect(true);
//
//		// 세션 구간값. 복수선택 가능
//		// condition.getConcurrentSession().getFilter().get(0).setSelect(false);
//		// condition.getConcurrentSession().getFilter().get(1).setSelect(true);
//
//		// 트래픽 구간값. 복수선택 가능
//		// condition.getThroughput().getFilter().get(0).setSelect(false);
//		// condition.getThroughput().getFilter().get(2).setSelect(true);
//
//		// 그룹 이름
//		condition.getGroup().setSelect(true);
//
//		// F5 persistence profile
//		// condition.getPersistence().setSelect(true);
//
//		// virtual server update time
//		// condition.getUpdateTime().setSelect(true);
//
//		// 24시간 slb config
//		// condition.getConfigHistory().setSelect(true);
//
//		// member count
//		condition.getMember().setSelect(true);
//
//		// IS VS NOTICE?
//		condition.getNoticeGroup().setSelect(true);
//
//		// search - virtual service name
//		condition.setSearchKeyword("192.");
//
//		Integer beginIndex = 0;
//		Integer endIndex = 10000;
//		Integer orderType = OBDefine.ORDER_TYPE_ADCNAME;
//		Integer orderDir = OBDefine.ORDER_DIR_ASCEND;
//		OBDtoMonTotalService result = null;
////
////        try
////        {
////            condition = null;
//////            System.out.println("count = " + getTotalServiceListCount(scope, accountIndex, accountRole, condition));
//////            result = getTotalServiceList(scope, accountIndex, accountRole, condition, beginIndex, endIndex, orderType, orderDir);
////            condition = result.getCondition();
////            System.out.println("condition = " + condition);
////            // ADC 주요 값 보기
////            for(OBDtoMonTotalServiceOne s : result.getServiceList())
////            {
////                System.out.println(String.format(" %s(%s:%d) - status=%d member=%d bps=%d conn=%d ", s.getName(), s.getIp(),
////                        s.getPort(), s.getStatus(), s.getMember(), s.getBpsTotal(), s.getConcurrentSession()));
////            }
//
//		// //update time 검증
//		// if(condition.getUpdateTime().isSelect()==true)
//		// {
//		// System.out.println("=== update time ===");
//		// for(OBDtoMonTotalServiceOne s: result.getServiceList())
//		// {
//		// System.out.println(String.format(" %s\t %s", s.getIndex(),
//		// s.getUpdateTime()));
//		// }
//		// }
//		// //24시간 slb config
//		// if(condition.getConfigHistory().isSelect()==true)
//		// {
//		// System.out.println("=== 24h config count ===");
//		// for(OBDtoMonTotalServiceOne s: result.getServiceList())
//		// {
//		// System.out.println(String.format(" %s\t %s", s.getIndex(),
//		// s.getSlbConfig24Hour()));
//		// }
//		// }
////        }
////        catch(OBException e)
////        {
////            e.printStackTrace();
////        }
//	}

	@Override
	public Integer getTotalAdcListCount(OBDtoAdcScope scope, Integer accountIndex, OBDtoMonTotalAdcCondition condition)
			throws OBException {
		OBDatabase db = new OBDatabase();
		String userAdcIndexString;

		String sqlText = "";
		String sqlAdc = "";
		String sqlActive = "";
		String sqlPerformance = "";
		String sqlCert = "";

		String extra = "";
		String sqlWhere = "";
		ArrayList<String> whereItem = new ArrayList<String>();
		FilterValue singleFilterValue = null;
		ArrayList<FilterValue> multiFilterValue = null;
		Integer count = 0;
		int dataValidTerm; // seconds, 실시간 모니터링 데이터 유효시간

		db.openDB();
		try {
			// 데이터 유효 시간 체크에 쓰일 업데이트 주기, 못 구하면 기본 주기인 300초로 한다.
			dataValidTerm = new OBEnvManagementImpl().getAdcSyncInterval(db) * 2;
			if (dataValidTerm <= 0) {
				dataValidTerm = 300;
			}
			// 사용자의 유효 adcIndex를 구한다.
			userAdcIndexString = new OBAdcManagementImpl().getUsersAdcIndexString(scope.getLevel(), scope.getIndex(),
					accountIndex);
			// 최초 검색이어서 조건이 없으면 검색조건을 초기화 한다.
			if (condition == null) // 기존 condition이 없으면 기본값 채운다.
			{
				condition = new OBDtoMonTotalAdcCondition();
				condition.getModel().setFilter(makeModelFilter(userAdcIndexString, db)); // 모델 필터: 실제 ADC들을 취합
				condition.getSwVersion().setFilter(makeVersionFilter(userAdcIndexString, db)); // sw version 필터: 실제
																								// ADC들을 취합
				condition.getType().setFilter(makeAdcTypeFilter(userAdcIndexString, db)); // type 필터: 실제 있는 장비 유형만 붙인다.
			}
			if (userAdcIndexString.isEmpty() == true) { // 선택된 adc가 없으면 빈 result return
				return 0;
			}

			// ADC 정보 - sql
			sqlAdc = String.format(" SELECT INDEX FROM MNG_ADC WHERE INDEX IN (%s) ", userAdcIndexString);
			// ADC 정보 - status 필터
			if (condition.getStatus().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, status는 필수선택이므로 선택돼 있다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getStatus().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}
					extra += String.format(" STATUS = %d ", entry.getFrom());
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					sqlAdc += String.format(" AND (%s) ", extra); // mng_adc는 left-join이 아닌 메인 테이블이므로 바로 필터를 적용할 수 있다.
				}
			}
			// ADC 정보 - type 필터
			if (condition.getType().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, type은 필수선택이므로 선택돼 있다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getType().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}
					extra += String.format(" TYPE = %d ", entry.getFrom());
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					sqlAdc += String.format(" AND (%s) ", extra); // mng_adc는 left-join이 아닌 메인 테이블이므로 바로 필터를 적용할 수 있다.
				}
			}
			// ADC 정보 - uptime 필터
			if (condition.getUptimeAge().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, uptime은 옵션이므로 선택 안될 수 있다.
			{
				singleFilterValue = getFilterNumberSingle(condition.getUptimeAge().getFilter());
				if (singleFilterValue.getFrom() >= 0) // uptime N일 이상
				{ // mng_adc는 left-join이 아닌 메인 테이블이므로 바로 필터를 적용할 수 있다.
					sqlAdc += String.format(" AND LAST_BOOT_TIME <= CURRENT_DATE - INTERVAL '%d DAYS' ",
							singleFilterValue.getFrom());
				}
			}
			// ADC 정보 - model 필터
			if (condition.getModel().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, model은 필수이므로 선택돼 있다.
			{
				extra = "";
				multiFilterValue = getFilterStringMulti(condition.getModel().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}
					// 모델은 축약됐기 때문에 like로 검색
					if (entry.getWord2() == null || entry.getWord2().equals("Unknown")) // 모델을 모르는 adc
					{
						extra += String.format(" (TYPE = %d AND (MODEL IS NULL OR MODEL = '')) ",
								Integer.parseInt(entry.getWord1()));
					} else { // 주의! 축약된 모델명은 full모델명의 맨 끝에 있다. 전후에 모두 %를 붙인 like를 쓰면 '3408'로 검색시 '3408 E'까지
								// 같이 나오므로 머리에만 %처리한다.
						extra += String.format(" (TYPE = %d AND MODEL like %s) ", Integer.parseInt(entry.getWord1()),
								OBParser.sqlString("%" + entry.getWord2()));
					}
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					sqlAdc += String.format(" AND (%s) ", extra); // mng_adc는 left-join이 아닌 메인 테이블이므로 바로 필터를 적용할 수 있다.
				}
			}
			// ADC 정보 - sw_version 필터
			if (condition.getSwVersion().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, swVersion은 필수이므로 선택돼 있다.
			{
				extra = "";
				multiFilterValue = getFilterStringMulti(condition.getSwVersion().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}
					if (entry.getWord1() == null || entry.getWord1().equals("Unknown")) // 버전을 모르는 adc
					{
						extra += String.format(" (SW_VERSION IS NULL OR SW_VERSION = '') ",
								OBParser.sqlString(entry.getWord1()));
					} else {
						extra += String.format(" SW_VERSION = %s ", OBParser.sqlString(entry.getWord1()));
					}
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					sqlAdc += String.format(" AND (%s) ", extra); // mng_adc는 left-join이 아닌 메인 테이블이므로 바로 필터를 적용할 수 있다.
				}
			}

			// ADC 정보 - 검색어: ADC이름
			if (condition.getSearchKeyword() != null && condition.getSearchKeyword().isEmpty() == false) // 검색어가 유효하면
																											// ADC이름
																											// 검색한다.
			{
				extra = "%" + OBParser.removeWildcard(condition.getSearchKeyword()) + "%";
				extra = String.format(" NAME LIKE %s ", OBParser.sqlString(extra));
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					sqlAdc += String.format(" AND %s ", extra); // mng_adc는 left-join이 아닌 메인 테이블이므로 조건을 즉시 적용할 수 있다.
				}
			}

			// active-standby - sql
			sqlActive = String.format(
					" SELECT ADC_INDEX, ACTIVE_BACKUP_STATE " + " FROM MNG_ADC_ADDITIONAL WHERE ADC_INDEX IN (%s) ",
					userAdcIndexString);
			// active-standby - 위상 필터
			if (condition.getActiveBackupState().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, Active-Standby는 필수선택이므로
																		// 선택돼 있다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getActiveBackupState().getFilter()); // 1:active,
																										// 2:backup,
																										// 0:Unknown
																										// (DB값 0, join후
																										// 없는 경우도 있으므로
																										// null-->0처리)
				for (FilterValue value : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}
					extra += String.format(" ACTIVE_BACKUP_STATE = %d ", value.getFrom());
					if (value.getFrom() == 0) // unknown: join하면 null인 경우도 unknown으로 쳐야하므로 조건을 추가해야 한다.
					{
						if (extra.isEmpty() == false) {
							extra += " OR "; // 각 조건항을 logical-or로 연결한다.
						}
						extra += String.format(" ACTIVE_BACKUP_STATE IS NULL ", value.getFrom());
					}
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}

			// adc 트래픽, vs 상태별 개수
			sqlPerformance = String.format(
					" SELECT ADC_INDEX, BPS_TOT/2 BPS, CONN_CURR, VS_COUNT_AVAIL, VS_COUNT_UNAVAIL, VS_COUNT_DISABLED \n"
							+ "            FROM TMP_FAULT_ADC_PERF_STATS WHERE ADC_INDEX IN (%s) AND OCCUR_TIME > NOW() - INTERVAL '%d SECONDS' ",
					userAdcIndexString, dataValidTerm);

			String extra2 = "";
			if (condition.getConcurrentSession().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, 옵션이어서 선택 안됐을 수 있다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getConcurrentSession().getFilter());
				for (FilterValue value : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					extra2 = "";
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}

					if (value.getFrom() != null && value.getFrom() >= 0) {
						extra2 += String.format(" CONN_CURR >= %d ", value.getFrom());
					}
					if (value.getTo() != null && value.getTo() >= 0) {
						if (extra2.isEmpty() == false) {
							extra2 += " AND "; // 각 조건항을 logical-or로 연결한다.
						}
						extra2 += String.format(" CONN_CURR <= %d ", value.getTo());
					}
					if (extra2.isEmpty() == false) {
						extra2 = "(" + extra2 + ")";
					}
					extra += extra2;
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}
			if (condition.getThroughput().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, 옵션이어서 선택 안됐을 수 있다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getThroughput().getFilter());
				for (FilterValue value : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					extra2 = "";
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}

					if (value.getFrom() != null && value.getFrom() >= 0) {
						extra2 += String.format(" BPS >= %d ", value.getFrom());
					}
					if (value.getTo() != null && value.getTo() >= 0) {
						if (extra2.isEmpty() == false) {
							extra2 += " AND "; // 각 조건항을 logical-or로 연결한다.
						}
						extra2 += String.format(" BPS <= %d ", value.getTo());
					}
					if (extra2.isEmpty() == false) {
						extra2 = "(" + extra2 + ")";
					}
					extra += extra2;
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}

			// 인증서 유효기간 - 다른 항목들과 달리 aggregation function이 있어서 main sql이 filter값의 영향을 받으므로
			// filter를 먼저 확인한다.
			if (condition.getSslCertValidDays().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, 옵션이어서 선택 안됐을 수 있다.
			{
				extra = "";
				singleFilterValue = getFilterNumberSingle(condition.getSslCertValidDays().getFilter());
				if (singleFilterValue.getTo() >= 0) // 인증서 만료일 n일 이내
				{
					extra = String.format(" AND EXPIRATION_DATE < CURRENT_DATE + INTERVAL '%d DAYS' ",
							(singleFilterValue.getTo() + 1));
					whereItem.add(" CERT_COUNT > 0 "); // 최종적으로 필터가 적용되는 부분
				}
				sqlCert = String.format(" SELECT ADC_INDEX, COUNT(*) CERT_COUNT FROM TMP_ADC_CERTIFICATE \n"
						+ " WHERE ADC_INDEX IN (%s) %s \n" + " GROUP BY ADC_INDEX ", userAdcIndexString, extra);
			} else
			// 선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			{
				sqlCert = " SELECT -1 ADC_INDEX, 0 CERT_COUNT ";
			}

			for (String item : whereItem) {
				if (sqlWhere.isEmpty() == true) {
					sqlWhere = " WHERE ";
				} else
				// 2번째 항부터 and로 연결
				{
					sqlWhere += " AND ";
				}
				sqlWhere += "(" + item + ")";
			}

			sqlText = String.format(" SELECT COUNT(A.INDEX) COUNT   \n" + " FROM ( %s ) A                 \n" + // Adc
					" LEFT JOIN ( %s ) B            \n" + // AdcAdditional
					" ON A.INDEX = B.ADC_INDEX      \n" + " LEFT JOIN ( %s ) C            \n" + // ADCPerfStat
					" ON A.INDEX = C.ADC_INDEX      \n" + " LEFT JOIN ( %s ) D            \n" + // SSL 인증서 만료일
					" ON A.INDEX = D.ADC_INDEX      \n" + " %s                            \n" // 각종 필터들때문에 들어가는 where절
																								// 통으로
					, sqlAdc, sqlActive, sqlPerformance, sqlCert, sqlWhere);

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				count = db.getInteger(rs, "COUNT");
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
		return count;
	}

	@Override
	public OBDtoMonTotalService getTotalServiceListToExport(OBDtoAdcScope scope, Integer accountIndex,
			String accountRole, OBDtoMonTotalServiceCondition condition, Integer beginIndex, Integer endIndex)
			throws OBException {
		OBDtoOrdering orderOption = new OBDtoOrdering();
		orderOption.setOrderDirection(OBDefine.ORDER_DIR_DESCEND);
		orderOption.setOrderType(OBDefine.ORDER_TYPE_VSNAME);
		return getTotalServiceList(scope, accountIndex, accountRole, condition, beginIndex, endIndex,
				orderOption.getOrderType(), orderOption.getOrderDirection());
	}

	@Override
	public OBDtoMonTotalService getTotalServiceList(OBDtoAdcScope scope, Integer accountIndex, String accountRole,
			OBDtoMonTotalServiceCondition condition, Integer beginIndex, Integer endIndex, Integer orderType,
			Integer orderDir) throws OBException {
		OBDtoMonTotalService result = new OBDtoMonTotalService();
		OBDatabase db = new OBDatabase();
		String userAdcIndexString;
		String sqlText = "";
		String sqlMain = "";

		String sqlPerformance = "";
		String sqlMember = "";
		String sqlPool = "";
		String sqlPersistence = "";
		String sqlUpdateTime = "";
		String sqlConfig24h = "";
		String sqlNotice = "";

		String extra = "";
		String sqlWhere = "";
		ArrayList<String> whereItem = new ArrayList<String>();

		ArrayList<FilterValue> multiFilterValue = null;
		int dataValidTerm; // seconds, 실시간 모니터링 데이터 유효시간

		db.openDB();
		try {
			// 데이터 유효 시간 체크에 쓰일 업데이트 주기, 못 구하면 기본 주기인 300초로 한다.
			dataValidTerm = new OBEnvManagementImpl().getAdcSyncInterval(db) * 2;
			if (dataValidTerm <= 0) {
				dataValidTerm = 300;
			}
			// 사용자의 유효 adcIndex를 구한다.
			userAdcIndexString = new OBAdcManagementImpl().getUsersAdcIndexString(scope.getLevel(), scope.getIndex(),
					accountIndex);
			// 최초 검색이어서 조건이 없으면 검색조건을 초기화 한다.
			if (condition == null) // 기존 condition이 없으면 기본값 채운다.
			{
				condition = new OBDtoMonTotalServiceCondition();
				condition.getPort().setFilter(makePortFilter(userAdcIndexString, db)); // 모델 필터는 실제 ADC들을 취합해서 붙임
			}
			result.setCondition(condition);
			if (userAdcIndexString.isEmpty() == true) { // 선택된 adc가 없으면 빈 result return
				return result;
			}

			// table V: VS 정보 - main, 선택과 상관없이 query.
			// F5와 ALTEON을 따로 조회하고 컬럼을 ALIAS로 맞춰서 UNION해야한다.
			sqlMain = String.format(" SELECT A.INDEX VS_INDEX, A.ADC_INDEX, A.STATUS, \n"
					+ "    CASE WHEN A.STATUS=0 THEN 3 ELSE A.STATUS END STATUS_SORT, \n" + // 단절을 정렬의 맨 뒤로 보내는 처리
					"    A.NAME VS_NAME, A.VIRTUAL_IP, A.VIRTUAL_PORT, \n"
					+ "    A.POOL_INDEX, A.PERSISTENCE_INDEX, B.NAME ADC_NAME, B.IPADDRESS ADC_IP, B.TYPE ADC_TYPE \n"
					+ " FROM (SELECT INDEX, ADC_INDEX, STATUS, NAME, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX, PERSISTENCE_INDEX \n"
					+ "       FROM TMP_SLB_VSERVER WHERE ADC_INDEX IN (%s)) A  \n" + // F5,
																						// Alteon식별
																						// 불가
					" INNER JOIN (SELECT INDEX, NAME, IPADDRESS, TYPE FROM MNG_ADC WHERE INDEX IN (%s) AND TYPE != %d ) B  \n"
					+ // F5,PAS,PASK 식별이 여기서 된다. inner로 맞는 것만 건진다.
					" ON A.ADC_INDEX = B.INDEX \n" + " UNION ALL \n"
					+ " SELECT A1.INDEX VS_INDEX, A2.ADC_INDEX, A1.STATUS, \n" + // Alteon은 service인덱스를 써야하므로 A1.INDEX
																					// !!
					"     CASE WHEN A1.STATUS=0 THEN 3 ELSE A1.STATUS END STATUS_SORT, \n" + // 단절을 정렬의 맨 뒤로 보내는 처리
					"     A2.NAME VS_NAME, A2.VIRTUAL_IP, A1.VIRTUAL_PORT, \n"
					+ "     A1.POOL_INDEX, NULL PERSISTENCE_INDEX, B.NAME ADC_NAME, B.IPADDRESS ADC_IP, B.TYPE ADC_TYPE \n"
					+ " FROM (SELECT INDEX, VS_INDEX, POOL_INDEX, VIRTUAL_PORT, STATUS \n"
					+ "       FROM TMP_SLB_VS_SERVICE WHERE ADC_INDEX IN (%s)) A1 \n" + // TMP_SLB_VS_SERVICE에서
																						// 뽑으므로
																						// Alteon만
																						// 걸림
					" LEFT JOIN (SELECT INDEX, ADC_INDEX, VIRTUAL_IP, NAME FROM TMP_SLB_VSERVER) A2 \n"
					+ " ON A1.VS_INDEX = A2.INDEX \n"
					+ " LEFT JOIN (SELECT INDEX, NAME, IPADDRESS, TYPE FROM MNG_ADC WHERE INDEX IN (%s) AND TYPE = %d ) B \n"
					+ // 이미 테이블(서비스) 때문에
						// Alteon만 걸러졌지만
						// 쿼리 구조도 맞추고, 재확인
					" ON A2.ADC_INDEX = B.INDEX ", userAdcIndexString, userAdcIndexString, OBDefine.ADC_TYPE_ALTEON,
					userAdcIndexString, userAdcIndexString, OBDefine.ADC_TYPE_ALTEON);
			// VS 정보 - status 필터. 단일
			if (condition.getStatus().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, status는 필수선택이므로 선택돼 있다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getStatus().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}
					extra += String.format(" STATUS = %d ", entry.getFrom());
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}
			// VS 정보 - port 필터. 복수
			if (condition.getPort().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, port는 필수이므로 선택돼 있다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getPort().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += "OR"; // 각 조건항을 logical-or로 연결한다.
					}
					extra += String.format(" VIRTUAL_PORT = %d ", entry.getFrom());
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}
			// 검색어: virtual server IP, name
			if (condition.getSearchKeyword() != null && condition.getSearchKeyword().isEmpty() == false) // 검색어가 유효하면
																											// 검색한다.
			{
				extra = "%" + OBParser.removeWildcard(condition.getSearchKeyword()) + "%";
				extra = String.format(" VS_NAME LIKE %s OR VIRTUAL_IP LIKE %s", OBParser.sqlString(extra),
						OBParser.sqlString(extra));
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}
			// table C: performance
			sqlPerformance = String.format(
					" SELECT OBJ_INDEX, CONN_CURR, BPS_IN, BPS_OUT, BPS_TOT \n" + " FROM TMP_FAULT_SVC_PERF_STATS \n"
							+ " WHERE ADC_INDEX IN (%s) AND OCCUR_TIME > NOW() - INTERVAL '%d SECONDS' ",
					userAdcIndexString, dataValidTerm);

			String temp = "";
			if (condition.getConcurrentSession().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효하다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getConcurrentSession().getFilter());
				for (FilterValue value : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					temp = "";
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}
					if (value.getFrom() != null && value.getFrom() >= 0) {
						temp += String.format(" CONN_CURR >= %d ", value.getFrom());
					}
					if (value.getTo() != null && value.getTo() >= 0) {
						if (temp.isEmpty() == false) {
							temp += " AND "; // 각 조건항을 logical-or로 연결한다.
						}
						temp += String.format(" CONN_CURR <= %d ", value.getTo());
					}
					if (temp.isEmpty() == false) {
						temp = "(" + temp + ")";
					}
					extra += temp;
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}
			if (condition.getBpsTotal().isSelect() == true) // BPS TOTAL, 필드가 선택됐을 때만 필터가 유효하다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getBpsTotal().getFilter());
				for (FilterValue value : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					temp = "";
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}

					if (value.getFrom() != null && value.getFrom() >= 0) {
						temp += String.format(" BPS_TOT >= %d ", value.getFrom());
					}
					if (value.getTo() != null && value.getTo() >= 0) {
						if (temp.isEmpty() == false) {
							temp += " AND "; // 각 조건항을 logical-or로 연결한다.
						}
						temp += String.format(" BPS_TOT <= %d ", value.getTo());
					}
					if (temp.isEmpty() == false) {
						temp = "(" + temp + ")";
					}
					extra += temp;
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}

			// 백업: 옵션 제거됨
			// if(condition.getBackup().isSelect()==true) //옵션이어서 선택 안됐을 수 있다. 필터:백업있음, 백업없음
			// {
			// sqlBackup = String.format(
			// " SELECT INDEX _INDEX, BAK_TYPE FROM TMP_SLB_POOL WHERE ADC_INDEX IN ( %s ) "
			// //아래 대체 쿼리에서 INDEX로 쓰면 해석상 오류가 난다.
			// , userAdcIndexString);
			//
			// //필터 처리
			// extra = "";
			// multiFilterValue = getFilterNumberMulti(condition.getBackup().getFilter());
			// for(FilterValue entry: multiFilterValue) //"전체"를 선택한 경우 list.size()==0 이므로
			// 자연스럽게 where절이 붙지 않는다.
			// {
			// if(extra.isEmpty()==false)
			// {
			// extra += "OR"; // 각 조건항을 logical-or로 연결한다.
			// }
			// if(entry.getFrom()==0) //백업없음 필터
			// {
			// extra += String.format(" BAK_TYPE = %d OR BAK_TYPE IS NULL ",
			// OBDefine.BACKUP_STATE.EMPTY);
			// }
			// else //백없있음 필터
			// {
			// extra += String.format(" BAK_TYPE != %d ", OBDefine.BACKUP_STATE.EMPTY);
			// }
			// }
			// if(extra.isEmpty()==false) //유효 필터가 있으면 붙인다.
			// {
			// whereItem.add(extra);
			// }
			// }
			// else //선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			// {
			// sqlBackup = " SELECT 'NONE'::VARCHAR _INDEX, 0 BAK_TYPE "; //INDEX로 쓰면 해석상
			// 오류가 난다.
			// }

			// 멤버 개수
			if (condition.getMember().isSelect() == true) {
				sqlMember = String.format(" SELECT POOL_INDEX, COUNT(INDEX) COUNT_MEMBER FROM TMP_SLB_POOLMEMBER \n"
						+ " WHERE POOL_INDEX IN ( SELECT DISTINCT(POOL_INDEX) FROM TMP_SLB_VSERVER WHERE ADC_INDEX IN (%s) ) \n"
						+ " GROUP BY POOL_INDEX ", userAdcIndexString);
			} else
			// 선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			{
				sqlMember = " SELECT 'NONE'::VARCHAR POOL_INDEX, -1 COUNT_MEMBER ";
			}

			// 그룹: 이름, loadbalancing, health check, -- 셋다 옵션이어서 선택 안됐을 수 있다. 필터없음
			if (condition.getGroup().isSelect() == true || condition.getLoadbalancing().isSelect() == true
					|| condition.getHealthCheck().isSelect() == true) {
				sqlPool = String.format(
						" SELECT INDEX POOL_INDEX, NAME POOL_NAME, ALTEON_ID POOL_ID, HEALTH_CHECK, HEALTH_CHECK_INDEX, \n"
								+ "     CASE WHEN LB_METHOD = %d THEN '%s' \n"
								+ "          WHEN LB_METHOD = %d THEN '%s' \n"
								+ "          WHEN LB_METHOD = %d THEN '%s' \n" + "          ELSE '%s' \n"
								+ "     END LB_METHOD \n" + " FROM TMP_SLB_POOL WHERE ADC_INDEX IN ( %s ) ",
						OBDefine.LB_METHOD_ROUND_ROBIN, "Round Robin", OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER,
						"Least Connections", OBDefine.LB_METHOD_HASH, "Hash", /*
																				 * OBDefine . COMMON_NOT_ALLOWED ,
																				 */"etc", userAdcIndexString);
			} else
			// 선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			{
				sqlPool = " SELECT 'NONE'::VARCHAR POOL_INDEX, 'NONE'::VARCHAR POOL_NAME, 'NONE'::VARCHAR POOL_ID, 'NONE'::VARCHAR LB_METHOD, -1 HEALTH_CHECK, 'NONE'::VARCHAR HEALTH_CHECK_INDEX ";
			}
			// persistence profile
			if (condition.getPersistence().isSelect() == true) // 옵션이어서 선택 안됐을 수 있다. 필터없음
			{
				sqlPersistence = String.format(
						" SELECT INDEX _INDEX, PROFILE_NAME FROM TMP_SLB_PROFILE WHERE ADC_INDEX IN (%s) ",
						userAdcIndexString);
			} else
			// 선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			{ // INDEX로 쓰면 해석상 오류가 난다.
				sqlPersistence = " SELECT 'NONE'::VARCHAR _INDEX, 'NONE'::VARCHAR PROFILE_NAME ";
			}

			// virtual server 업데이트 시간
			if (condition.getUpdateTime().isSelect() == true) // 옵션이다. virtual server 설정 시간은 무거운 query이므로 특히나 선택했을 때만
																// 한다.
			{
				sqlUpdateTime = String.format(" SELECT VS_INDEX, OCCUR_TIME UPDATE_TIME FROM LOG_CONFIG_HISTORY \n"
						+ " WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY \n"
						+ "                   WHERE VS_INDEX IN (SELECT INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX IN (%s)) \n"
						+ "                   GROUP BY VS_INDEX) ", userAdcIndexString);
			} else
			// 선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			{
				sqlUpdateTime = " SELECT 'NONE'::VARCHAR VS_INDEX, NULL::TIMESTAMP UPDATE_TIME ";
			}

			// virtual server 업데이트 24시간 이내 건수
			if (condition.getConfigHistory().isSelect() == true) // 옵션이다. 무거운 query이므로 선택했을 때만 한다.
			{
				sqlConfig24h = String
						.format(" SELECT VS_INDEX, COUNT(VS_INDEX) COUNT_CONFIG24H FROM LOG_CONFIG_HISTORY \n"
								+ " WHERE VS_INDEX IN (SELECT INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX IN (%s)) \n"
								+ "     AND OCCUR_TIME >= (CURRENT_TIMESTAMP - INTERVAL '24 HOURS') \n"
								+ " GROUP BY VS_INDEX ", userAdcIndexString);
			} else
			// 선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			{
				sqlConfig24h = " SELECT NULL::VARCHAR VS_INDEX, NULL::INT COUNT_CONFIG24H ";
			}

			// pool이 공지그룹으로 돼 있는가? true 1, false 0
			if (condition.getNoticeGroup().isSelect() == true) {
				sqlNotice = String
						.format(" SELECT POOL_INDEX, CASE WHEN NOTICE_INDEX IS NULL THEN 0 ELSE 1 END IS_NOTICE \n"
								+ " FROM ( SELECT DISTINCT(INDEX) POOL_INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX IN (%s)) Y1 \n"
								+ " LEFT JOIN (SELECT POOL_INDEX NOTICE_INDEX FROM MNG_NOTICE_GROUP ) Y2 \n" + // ADC당
																												// 한개정도
																												// 있으니
																												// ADC
																												// index
																												// where
																												// 조건은
																												// 생략
								" ON Y1.POOL_INDEX = Y2.NOTICE_INDEX ", userAdcIndexString);
			} else
			// 선택 안됐으면 join 우회하는 대체 쿼리
			{
				sqlNotice = " SELECT NULL::VARCHAR POOL_INDEX, NULL::INT IS_NOTICE ";
			}

			for (String item : whereItem) {
				if (sqlWhere.isEmpty() == true) {
					sqlWhere = " WHERE ";
				} else
				// 2번째 항부터 and로 연결
				{
					sqlWhere += " AND ";
				}
				sqlWhere += "(" + item + ")";
			}

			sqlText = String.format(
					" SELECT V.VS_INDEX, V.STATUS, V.STATUS_SORT, V.VS_NAME, V.VIRTUAL_IP, V.VIRTUAL_PORT, V.POOL_INDEX,                        \n"
							+ "     V.ADC_INDEX, V.ADC_NAME, V.ADC_IP, V.ADC_TYPE, CASE WHEN C.CONN_CURR=-1 THEN NULL ELSE C.CONN_CURR END CONN_CURR,   \n"
							+ "     COALESCE(C.CONN_CURR, -1) CONN_CURR_VALUE, CASE WHEN C.BPS_IN=-1 THEN NULL ELSE C.BPS_IN END BPS_IN,                \n"
							+ "     COALESCE(C.BPS_IN, -1) BPS_IN_VALUE, CASE WHEN C.BPS_OUT=-1 THEN NULL ELSE C.BPS_OUT END BPS_OUT,                   \n"
							+ "     COALESCE(C.BPS_OUT, -1) BPS_OUT_VALUE, CASE WHEN C.BPS_TOT=-1 THEN NULL ELSE C.BPS_TOT END BPS_TOT,                 \n"
							+ "     COALESCE(C.BPS_TOT, -1) BPS_TOT_VALUE, D.POOL_INDEX,                                                                \n"
							+ "     CASE WHEN D.POOL_NAME='' THEN NULL ELSE D.POOL_NAME END POOL_NAME, D.POOL_ID, D.LB_METHOD, D.HEALTH_CHECK,          \n"
							+ "     D.HEALTH_CHECK_INDEX, E.PROFILE_NAME, F.UPDATE_TIME, G.COUNT_CONFIG24H, H.COUNT_MEMBER,                             \n"
							+ "     COALESCE(H.COUNT_MEMBER, -1) COUNT_MEMBER_VALUE, I.IS_NOTICE                                                          "
							+ " FROM ( %s ) V                                                                                                           \n"
							+ // virtual
								// server(Alteon은
								// 서비스)
								// &
								// ADC
							" LEFT JOIN ( %s ) C                \n" + // vs performance
							" ON V.VS_INDEX = C.OBJ_INDEX       \n" + " LEFT JOIN ( %s ) D                \n" + // 그룹
																												// 이름,
																												// loadbalancing,
																												// health_check(old,
																												// new)
							" ON V.POOL_INDEX = D.POOL_INDEX    \n" + " LEFT JOIN ( %s ) E                \n" + // persistence
																												// profile
							" ON V.PERSISTENCE_INDEX = E._INDEX \n" + " LEFT JOIN ( %s ) F                \n" + // virtual
																												// server
																												// 설정 시간
							" ON V.VS_INDEX = F.VS_INDEX        \n" + " LEFT JOIN ( %s ) G                \n" + // virtual
																												// server
																												// config
																												// 24시간
																												// 카운트
							" ON V.VS_INDEX = G.VS_INDEX        \n" + " LEFT JOIN ( %s ) H                \n" + // member
																												// count
							" ON V.POOL_INDEX = H.POOL_INDEX    \n" + " LEFT JOIN ( %s ) I                \n" + // 메모리
																												// 사용율
							" ON V.POOL_INDEX = I.POOL_INDEX    \n" + // notice pool 지정됐는지. true then 1, false 0
							" %s   \n" // 각종 필터들때문에 들어가는 where절 통으로
					, sqlMain, sqlPerformance, sqlPool, sqlPersistence, sqlUpdateTime, sqlConfig24h, sqlMember,
					sqlNotice, sqlWhere);

			if (accountRole.equals("vsAdmin")) {
				sqlText += String.format(
						" RIGHT JOIN (SELECT VS_INDEX FROM MNG_ACCNT_VS_MAP WHERE ADC_INDEX IN (%s) AND ACCNT_INDEX = %d) J "
								+ " ON V.VS_INDEX = J.VS_INDEX ",
						userAdcIndexString, accountIndex);
			}
			// order 처리
			sqlText += getListOrderByType(orderType, orderDir, new Order().TOTAL_SERVICE_COLS);

			// 페이징
			int offset = 0;
			String sqlRange = "";

			if (beginIndex != null && beginIndex.intValue() >= 0) {
				offset = beginIndex.intValue();
				sqlRange += " OFFSET " + beginIndex.intValue();
			}
			if (endIndex != null && endIndex.intValue() >= 0) {
				sqlRange += " LIMIT " + (Math.abs(endIndex.intValue() - offset) + 1);
			}
			sqlText += sqlRange;

			ArrayList<OBDtoMonTotalServiceOne> serviceList = new ArrayList<OBDtoMonTotalServiceOne>();
			OBDtoMonTotalServiceOne svc = null;

			ResultSet rs = db.executeQuery(sqlText);

			// Integer tempN; //number

			OBCommon common = new OBCommon();
			while (rs.next()) {
				svc = new OBDtoMonTotalServiceOne();
				svc.setIndex(db.getString(rs, "VS_INDEX"));
				svc.setStatus(db.getInteger(rs, "STATUS"));
				svc.setName(db.getString(rs, "VS_NAME"));
				svc.setIp(db.getString(rs, "VIRTUAL_IP"));
				svc.setPort(db.getInteger(rs, "VIRTUAL_PORT"));
				svc.setGroupIndex(db.getString(rs, "POOL_INDEX"));
				svc.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				svc.setAdcName(db.getString(rs, "ADC_NAME"));
				svc.setAdcIp(db.getString(rs, "ADC_IP"));
				svc.setAdcType(db.getInteger(rs, "ADC_TYPE"));
				svc.setConcurrentSession(db.getLong(rs, "CONN_CURR_VALUE"));
				svc.setBpsIn(db.getLong(rs, "BPS_IN_VALUE"));
				svc.setBpsOut(db.getLong(rs, "BPS_OUT_VALUE"));
				svc.setBpsTotal(db.getLong(rs, "BPS_TOT_VALUE"));
				svc.setUpdateTime(db.getTimestamp(rs, "UPDATE_TIME"));
				svc.setSlbConfig24Hour(db.getInteger(rs, "COUNT_CONFIG24H"));
				svc.setMember(db.getInteger(rs, "COUNT_MEMBER_VALUE"));
				svc.setNoticeGroup(db.getInteger(rs, "IS_NOTICE"));
				svc.setGroupIndex(db.getString(rs, "POOL_ID"));
				svc.setGroupName(db.getString(rs, "POOL_NAME"));
				svc.setPersistence(db.getString(rs, "PROFILE_NAME"));
				svc.setLoadbalancing(db.getString(rs, "LB_METHOD"));
				// health check
				// tempN = db.getInteger(rs, "HEALTH_CHECK");
				svc.setHealthCheck(common.convertHealthCheckTypeForDisplay(db.getInteger(rs, "HEALTH_CHECK")));

				serviceList.add(svc);
			}

			result.setServiceList(serviceList);
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

	@Override
	public Integer getTotalServiceListCount(OBDtoAdcScope scope, Integer accountIndex, String accountRole,
			OBDtoMonTotalServiceCondition condition) throws OBException {
		OBDatabase db = new OBDatabase();
		String userAdcIndexString;
		String sqlText = "";
		String sqlMain = "";
		String sqlPerformance = "";

		String extra = "";
		String sqlWhere = "";
		ArrayList<String> whereItem = new ArrayList<String>();
		ArrayList<FilterValue> multiFilterValue = null;
		int dataValidTerm; // seconds, 실시간 모니터링 데이터 유효시간
		Integer count = 0;
		db.openDB();

		try {
			// 데이터 유효 시간 체크에 쓰일 업데이트 주기, 못 구하면 기본 주기인 300초로 한다.
			dataValidTerm = new OBEnvManagementImpl().getAdcSyncInterval(db) * 2;
			if (dataValidTerm <= 0) {
				dataValidTerm = 300;
			}
			// 사용자의 유효 adcIndex를 구한다.
			userAdcIndexString = new OBAdcManagementImpl().getUsersAdcIndexString(scope.getLevel(), scope.getIndex(),
					accountIndex);
			// 최초 검색이어서 조건이 없으면 검색조건을 초기화 한다.
			if (condition == null) // 기존 condition이 없으면 기본값 채운다.
			{
				condition = new OBDtoMonTotalServiceCondition();
				condition.getPort().setFilter(makePortFilter(userAdcIndexString, db));
			}
			if (userAdcIndexString.isEmpty() == true) { // 선택된 adc가 없으면 빈 result return
				return 0;
			}

			// table V: VS 정보 - main, 선택과 상관없이 query.
			// F5와 ALTEON을 따로 조회하고 컬럼을 ALIAS로 맞춰서 UNION해야한다.
			sqlMain = String.format(
					" SELECT A.INDEX VS_INDEX, A.ADC_INDEX, A.STATUS, A.NAME VS_NAME, A.VIRTUAL_IP, A.VIRTUAL_PORT, A.POOL_INDEX \n"
							+ " FROM (SELECT INDEX, ADC_INDEX, STATUS, NAME, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX \n"
							+ "       FROM TMP_SLB_VSERVER WHERE ADC_INDEX IN (%s)) A  \n" + // F5,
																								// Alteon식별
																								// 불가
							" INNER JOIN (SELECT INDEX FROM MNG_ADC WHERE INDEX IN (%s) AND TYPE != %d ) B  \n" + // F5,PAS,PASK
																													// 식별이
																													// 여기서
																													// 된다.
																													// inner로
																													// 맞는
																													// 것만
																													// 건진다.
							" ON A.ADC_INDEX = B.INDEX \n" + " UNION ALL \n"
							+ " SELECT A1.INDEX VS_INDEX, A2.ADC_INDEX, A1.STATUS, A2.NAME VS_NAME, A2.VIRTUAL_IP, A1.VIRTUAL_PORT, A1.POOL_INDEX \n"
							+ // Alteon은 service인덱스를 써야하므로 A1.INDEX !!
							" FROM (SELECT INDEX, VS_INDEX, POOL_INDEX, VIRTUAL_PORT, STATUS \n"
							+ "       FROM TMP_SLB_VS_SERVICE WHERE ADC_INDEX IN (%s)) A1 \n" + // TMP_SLB_VS_SERVICE에서
																								// 뽑으므로 Alteon만 걸림
							" LEFT JOIN (SELECT INDEX, ADC_INDEX, VIRTUAL_IP, NAME FROM TMP_SLB_VSERVER) A2 \n"
							+ " ON A1.VS_INDEX = A2.INDEX \n"
							+ " LEFT JOIN (SELECT INDEX FROM MNG_ADC WHERE INDEX IN (%s) AND TYPE = %d ) B \n" + // 이미
																													// 테이블(서비스)
																													// 때문에
																													// Alteon만
																													// 걸러졌지만
																													// 쿼리
																													// 구조도
																													// 맞추고,
																													// 재확인
							" ON A2.ADC_INDEX = B.INDEX ",
					userAdcIndexString, userAdcIndexString, OBDefine.ADC_TYPE_ALTEON, userAdcIndexString,
					userAdcIndexString, OBDefine.ADC_TYPE_ALTEON);

			// VS 정보 - status 필터. 단일
			if (condition.getStatus().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, status는 필수선택이므로 선택돼 있다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getStatus().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}
					extra += String.format(" STATUS = %d ", entry.getFrom());
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}
			// VS 정보 - port 필터. 복수
			if (condition.getPort().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효한데, port는 필수이므로 선택돼 있다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getPort().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += "OR"; // 각 조건항을 logical-or로 연결한다.
					}
					extra += String.format(" VIRTUAL_PORT = %d ", entry.getFrom());
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}
			// 검색어: virtual server IP, name
			if (condition.getSearchKeyword() != null && condition.getSearchKeyword().isEmpty() == false) // 검색어가 유효하면
																											// 검색한다.
			{
				extra = "%" + OBParser.removeWildcard(condition.getSearchKeyword()) + "%";
				extra = String.format(" VS_NAME LIKE %s OR VIRTUAL_IP LIKE %s", OBParser.sqlString(extra),
						OBParser.sqlString(extra));
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}
			// table C: performance
			sqlPerformance = String.format(
					" SELECT OBJ_INDEX, CONN_CURR, BPS_IN, BPS_OUT, BPS_TOT \n" + " FROM TMP_FAULT_SVC_PERF_STATS \n"
							+ " WHERE ADC_INDEX IN (%s) AND OCCUR_TIME > NOW() - INTERVAL '%d SECONDS' ",
					userAdcIndexString, dataValidTerm);

			String temp = "";
			if (condition.getConcurrentSession().isSelect() == true) // 필드가 선택됐을 때만 필터가 유효하다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getConcurrentSession().getFilter());
				for (FilterValue value : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					temp = "";
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}
					if (value.getFrom() != null && value.getFrom() >= 0) {
						temp += String.format(" CONN_CURR >= %d ", value.getFrom());
					}
					if (value.getTo() != null && value.getTo() >= 0) {
						if (temp.isEmpty() == false) {
							temp += " AND "; // 각 조건항을 logical-or로 연결한다.
						}
						temp += String.format(" CONN_CURR <= %d ", value.getTo());
					}
					if (temp.isEmpty() == false) {
						temp = "(" + temp + ")";
					}
					extra += temp;
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}
			if (condition.getBpsTotal().isSelect() == true) // BPS TOTAL, 필드가 선택됐을 때만 필터가 유효하다.
			{
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getBpsTotal().getFilter());
				for (FilterValue value : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					temp = "";
					if (extra.isEmpty() == false) {
						extra += " OR "; // 각 조건항을 logical-or로 연결한다.
					}

					if (value.getFrom() != null && value.getFrom() >= 0) {
						temp += String.format(" BPS_TOT >= %d ", value.getFrom());
					}
					if (value.getTo() != null && value.getTo() >= 0) {
						if (temp.isEmpty() == false) {
							temp += " AND "; // 각 조건항을 logical-or로 연결한다.
						}
						temp += String.format(" BPS_TOT <= %d ", value.getTo());
					}
					if (temp.isEmpty() == false) {
						temp = "(" + temp + ")";
					}
					extra += temp;
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}

			for (String item : whereItem) {
				if (sqlWhere.isEmpty() == true) {
					sqlWhere = " WHERE ";
				} else
				// 2번째 항부터 and로 연결
				{
					sqlWhere += " AND ";
				}
				sqlWhere += "(" + item + ")";
			}

			sqlText = String.format(" SELECT COUNT(*) COUNT FROM (%s) V \n" + // virtual server(Alteon은 서비스)
					" LEFT JOIN ( %s ) C                \n" + // vs performance
					" ON V.VS_INDEX = C.OBJ_INDEX       \n" + " %s  \n" // 각종 필터들때문에 들어가는 where절 통으로
					, sqlMain, sqlPerformance, sqlWhere);

			if (accountRole.equals("vsAdmin")) {
				sqlText += String.format(
						" RIGHT JOIN (SELECT VS_INDEX FROM MNG_ACCNT_VS_MAP WHERE ADC_INDEX IN (%s) AND ACCNT_INDEX = %d) D "
								+ " ON V.VS_INDEX = D.VS_INDEX ",
						userAdcIndexString, accountIndex);
			}

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				count = db.getInteger(rs, "COUNT");
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
		return count;
	}

	@Override
	public OBDtoMonTotalGroup getTotalGroupListToExport(OBDtoAdcScope scope, Integer accountIndex,
			OBDtoMonTotalGroupCondition condition, Integer beginIndex, Integer endIndex) throws OBException {
		OBDtoOrdering orderOption = new OBDtoOrdering();
		orderOption.setOrderDirection(OBDefine.ORDER_DIR_DESCEND);
		orderOption.setOrderType(OBDefine.ORDER_TYPE_VSNAME);
		return getTotalGroupList(scope, accountIndex, condition, beginIndex, endIndex, orderOption.getOrderType(),
				orderOption.getOrderDirection());
	}

	@Override
	public OBDtoMonTotalGroup getTotalGroupList(OBDtoAdcScope scope, Integer accountIndex,
			OBDtoMonTotalGroupCondition condition, Integer beginIndex, Integer endIndex, Integer orderType,
			Integer orderDir) throws OBException {
		OBDtoMonTotalGroup result = new OBDtoMonTotalGroup();
		OBDatabase db = new OBDatabase();
		String userAdcIndexString;
		String sqlText = "";
		String sqlMain = "";
		String sqlMember = "";
		String sqlBackup = "";
		String sqlVirtualserver = "";
		String sqlFilter = "";
		String extra = "";
		String sqlWhere = "";
		ArrayList<String> whereItem = new ArrayList<String>();
		ArrayList<FilterValue> multiFilterValue = null;

		db.openDB();
		try {
			// 사용자의 유효 adcIndex를 구한다.
			userAdcIndexString = new OBAdcManagementImpl().getUsersAdcIndexString(scope.getLevel(), scope.getIndex(),
					accountIndex);
			// 최초 검색이어서 조건이 없으면 검색조건을 초기화 한다.
			if (condition == null) // 기존 condition이 없으면 기본값 채운다.
			{
				condition = new OBDtoMonTotalGroupCondition();
			}
			result.setCondition(condition);
			if (userAdcIndexString.isEmpty() == true) { // 선택된 adc가 없으면 빈 result return
				return result;
			}

			// 그룹 - main, 선택과 상관없이 query.
			sqlMain = String.format(
					" SELECT A1.INDEX, A1.NAME, A1.ALTEON_ID, A2.INDEX ADC_INDEX, A2.NAME ADC_NAME, A2.IPADDRESS ADC_IP, A2.TYPE ADC_TYPE \n"
							+ " FROM (SELECT INDEX, NAME, ALTEON_ID, ADC_INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX IN (%s)) A1 \n"
							+ " LEFT JOIN (SELECT INDEX, NAME, IPADDRESS, TYPE FROM MNG_ADC WHERE INDEX IN (%s)) A2 \n"
							+ " ON A1.ADC_INDEX = A2.INDEX ",
					userAdcIndexString, userAdcIndexString);

			// 검색어: group name, alteon id
			if (condition.getSearchKeyword() != null && condition.getSearchKeyword().isEmpty() == false) // 검색어가 유효하면
																											// 검색한다.
			{
				extra = "%" + OBParser.removeWildcard(condition.getSearchKeyword()) + "%";
				extra = String.format(" NAME LIKE %s ", OBParser.sqlString(extra));
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}

			// 백업
			if (condition.getBackup().isSelect() == true) // 옵션이어서 선택 안됐을 수 있다. 필터:백업있음, 백업없음
			{
				// 데이터 추출은 main에서 하므로 필터만 처리하면 된다.
				sqlBackup = String.format(" SELECT POOL.INDEX POOL_INDEX, NODE.ALTEON_ID||'(real)' BACKUP  \n"
						+ " FROM (SELECT INDEX, BAK_ID FROM TMP_SLB_POOL         \n"
						+ " WHERE BAK_TYPE IN (%d) AND ADC_INDEX IN (%s)) POOL   \n" + // real backup
						" LEFT JOIN TMP_SLB_NODE NODE                          \n"
						+ " ON POOL.BAK_ID = NODE.INDEX                          \n"
						+ " UNION                                                \n"
						+ " SELECT POOL.INDEX POOL_INDEX, POOL2.ALTEON_ID||'(group)' BACKUP \n"
						+ " FROM (SELECT INDEX, BAK_ID FROM TMP_SLB_POOL         \n"
						+ " WHERE BAK_TYPE IN (%d) AND ADC_INDEX IN (%s)) POOL  \n" + // group
																						// backup
						" LEFT JOIN TMP_SLB_POOL POOL2                         \n"
						+ " ON POOL.BAK_ID = POOL2.INDEX                         \n", OBDefine.BACKUP_STATE.REALBAK,
						userAdcIndexString, OBDefine.BACKUP_STATE.GROUPBAK, userAdcIndexString);
				// 필터 처리
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getBackup().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += "OR"; // 각 조건항을 logical-or로 연결한다.
					}
					if (entry.getFrom() == 0) // 백업없음 필터
					{
						extra += String.format(" BACKUP IS NULL ");
					} else
					// 백업있음 필터
					{
						extra += String.format(" BACKUP IS NOT NULL ");
					}
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			} else
			// 선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			{
				sqlBackup = " SELECT 'NONE'::VARCHAR POOL_INDEX, NULL::VARCHAR BACKUP ";
			}

			// 멤버 개수
			if (condition.getMember().isSelect() == true) {
				sqlMember = String.format(" SELECT POOL_INDEX, COUNT(INDEX) COUNT_MEMBER FROM TMP_SLB_POOLMEMBER \n"
						+ " WHERE POOL_INDEX IN ( SELECT INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX IN (%s) ) \n"
						+ " GROUP BY POOL_INDEX ", userAdcIndexString);
			} else
			// 선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			{
				sqlMember = " SELECT 'NONE'::VARCHAR POOL_INDEX, -1 COUNT_MEMBER ";
			}

			// 필터
			if (condition.getFilter().isSelect() == true) {
				sqlFilter = String.format(
						" SELECT ADC_INDEX, GROUP_ID, COUNT(*) COUNT_FILTER FROM TMP_FLB_FILTER  \n"
								+ " WHERE ADC_INDEX IN (%s) AND ACTION = 'redirect' GROUP BY ADC_INDEX, GROUP_ID ",
						userAdcIndexString);
			} else
			// 선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			{
				sqlFilter = " SELECT -1 ADC_INDEX, 'NONE'::VARCHAR GROUP_ID, -1 COUNT_FILTER ";
			}

			// 그룹이 묶인 virtual server 개수
			if (condition.getVsAssigned().isSelect() == true) {
				sqlVirtualserver = String.format(
						" SELECT POOL_INDEX, COUNT(POOL_INDEX) COUNT_VS FROM TMP_SLB_VSERVER \n"
								+ " WHERE ADC_INDEX IN (%s) GROUP BY POOL_INDEX \n"
								+ " UNION SELECT POOL_INDEX, COUNT(POOL_INDEX) COUNT_VS FROM TMP_SLB_VS_SERVICE \n"
								+ " WHERE ADC_INDEX IN (%s) GROUP BY POOL_INDEX ",
						userAdcIndexString, userAdcIndexString);
			} else
			// 선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			{
				sqlVirtualserver = " SELECT 'NONE'::VARCHAR POOL_INDEX, -1 COUNT_VS ";
			}

			for (String item : whereItem) {
				if (sqlWhere.isEmpty() == true) {
					sqlWhere = " WHERE ";
				} else
				// 2번째 항부터 and로 연결
				{
					sqlWhere += " AND ";
				}
				sqlWhere += "(" + item + ")";
			}
			sqlText = String.format(
					" SELECT A.INDEX, CASE WHEN A.NAME='' THEN null ELSE A.NAME END, A.ALTEON_ID, A.ADC_INDEX, A.ADC_NAME, A.ADC_IP, A.ADC_TYPE, "
							+ "     B.BACKUP, C.COUNT_MEMBER, COALESCE(C.COUNT_MEMBER, -1) COUNT_MEMBER_VALUE, D.COUNT_VS, "
							+ "     COALESCE(D.COUNT_VS, -1) COUNT_VS_VALUE, E.COUNT_FILTER, COALESCE(E.COUNT_FILTER, -1) COUNT_FILTER_VALUE \n"
							+ " FROM ( %s ) A                   \n" + // group
																		// &
																		// ADC
							" LEFT JOIN ( %s ) B              \n" + // backup
							" ON A.INDEX = B.POOL_INDEX       \n" + " LEFT JOIN ( %s ) C              \n" + // member
																											// count
							" ON A.INDEX = C.POOL_INDEX       \n" + " LEFT JOIN ( %s ) D              \n" + //
							" ON A.INDEX = D.POOL_INDEX       \n" + " LEFT JOIN ( %s ) E              \n" + // filter
							" ON A.ADC_INDEX = E.ADC_INDEX AND CAST(A.ALTEON_ID AS VARCHAR) = E.GROUP_ID \n"
							+ " %s   \n" // 각종 필터들때문에 들어가는 where절 통으로
					, sqlMain, sqlBackup, sqlMember, sqlVirtualserver, sqlFilter, sqlWhere

			);

			// order 처리
			sqlText += getListOrderByType(orderType, orderDir, new Order().TOTAL_GROUP_COLS);

			// 페이징
			int offset = 0;
			String sqlRange = "";

			if (beginIndex != null && beginIndex.intValue() >= 0) {
				offset = beginIndex.intValue();
				sqlRange += " OFFSET " + beginIndex.intValue();
			}
			if (endIndex != null && endIndex.intValue() >= 0) {
				sqlRange += " LIMIT " + (Math.abs(endIndex.intValue() - offset) + 1);
			}
			sqlText += sqlRange;

			ArrayList<OBDtoMonTotalGroupOne> groupList = new ArrayList<OBDtoMonTotalGroupOne>();
			OBDtoMonTotalGroupOne gr = null;

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				gr = new OBDtoMonTotalGroupOne();
				gr.setIndex(db.getString(rs, "INDEX"));
				gr.setName(db.getString(rs, "NAME"));
				gr.setId(db.getString(rs, "ALTEON_ID"));
				gr.setBackup(db.getString(rs, "BACKUP"));
				gr.setMember(db.getInteger(rs, "COUNT_MEMBER_VALUE"));
				gr.setFilter(db.getInteger(rs, "COUNT_FILTER_VALUE"));
				gr.setVsAssigned(db.getInteger(rs, "COUNT_VS_VALUE"));

				gr.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				gr.setAdcName(db.getString(rs, "ADC_NAME"));
				gr.setAdcIp(db.getString(rs, "ADC_IP"));
				gr.setAdcType(db.getInteger(rs, "ADC_TYPE"));

				groupList.add(gr);
			}

			result.setGroupList(groupList);
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

	@Override
	public Integer getTotalGroupListCount(OBDtoAdcScope scope, Integer accountIndex,
			OBDtoMonTotalGroupCondition condition) throws OBException {
		OBDatabase db = new OBDatabase();
		String userAdcIndexString;
		String sqlText = "";
		String sqlMain = "";
		String sqlBackup = "";
		String extra = "";
		String sqlWhere = "";
		ArrayList<String> whereItem = new ArrayList<String>();
		ArrayList<FilterValue> multiFilterValue = null;
		Integer count = 0;

		db.openDB();
		try {
			// 사용자의 유효 adcIndex를 구한다.
			userAdcIndexString = new OBAdcManagementImpl().getUsersAdcIndexString(scope.getLevel(), scope.getIndex(),
					accountIndex);
			// 최초 검색이어서 조건이 없으면 검색조건을 초기화 한다.
			if (condition == null) // 기존 condition이 없으면 기본값 채운다.
			{
				condition = new OBDtoMonTotalGroupCondition();
			}
			if (userAdcIndexString.isEmpty() == true) {// 선택된 adc가 없으면 빈 result return
				return 0;
			}

			// 그룹 - main, 선택과 상관없이 query.
			sqlMain = String.format(" SELECT A1.INDEX, A1.NAME, A1.ALTEON_ID \n"
					+ " FROM (SELECT INDEX, NAME, ALTEON_ID, ADC_INDEX  FROM TMP_SLB_POOL WHERE ADC_INDEX IN (%s)) A1 \n"
					+ " LEFT JOIN (SELECT INDEX FROM MNG_ADC WHERE INDEX IN (%s)) A2 \n"
					+ " ON A1.ADC_INDEX = A2.INDEX ", userAdcIndexString, userAdcIndexString);

			// 검색어: group name, alteon id
			if (condition.getSearchKeyword() != null && condition.getSearchKeyword().isEmpty() == false) // 검색어가 유효하면
																											// 검색한다.
			{
				extra = "%" + OBParser.removeWildcard(condition.getSearchKeyword()) + "%";
				extra = String.format(" NAME LIKE %s ", OBParser.sqlString(extra));
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}

			// 백업 - 궂이 이렇게 하지 않아도 되지만 getTotalGroupList()의 처리와 맞춘다.
			if (condition.getBackup().isSelect() == true) // 옵션이어서 선택 안됐을 수 있다. 필터:백업있음, 백업없음
			{
				// 데이터 추출은 main에서 하므로 필터만 처리하면 된다.
				sqlBackup = String.format(" SELECT POOL.INDEX POOL_INDEX, NODE.ALTEON_ID||'(real)' BACKUP  \n"
						+ " FROM (SELECT INDEX, BAK_ID FROM TMP_SLB_POOL         \n"
						+ " WHERE BAK_TYPE IN (%d) AND ADC_INDEX IN (%s)) POOL   \n" + // real backup
						" LEFT JOIN TMP_SLB_NODE NODE                          \n"
						+ " ON POOL.BAK_ID = NODE.INDEX                          \n"
						+ " UNION                                                \n"
						+ " SELECT POOL.INDEX POOL_INDEX, POOL2.ALTEON_ID||'(group)' BACKUP \n"
						+ " FROM (SELECT INDEX, BAK_ID FROM TMP_SLB_POOL         \n"
						+ " WHERE BAK_TYPE IN (%d) AND ADC_INDEX IN (%s)) POOL  \n" + // group
																						// backup
						" LEFT JOIN TMP_SLB_POOL POOL2                         \n"
						+ " ON POOL.BAK_ID = POOL2.INDEX                         \n", OBDefine.BACKUP_STATE.REALBAK,
						userAdcIndexString, OBDefine.BACKUP_STATE.GROUPBAK, userAdcIndexString);
				// 필터 처리
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getBackup().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += "OR"; // 각 조건항을 logical-or로 연결한다.
					}
					if (entry.getFrom() == 0) // 백업없음 필터
					{
						extra += String.format(" BACKUP IS NULL ");
					} else
					// 백업있음 필터
					{
						extra += String.format(" BACKUP IS NOT NULL ");
					}
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			} else
			// 선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			{
				sqlBackup = " SELECT 'NONE'::VARCHAR POOL_INDEX, NULL BACKUP ";
			}

			for (String item : whereItem) {
				if (sqlWhere.isEmpty() == true) {
					sqlWhere = " WHERE ";
				} else
				// 2번째 항부터 and로 연결
				{
					sqlWhere += " AND ";
				}
				sqlWhere += "(" + item + ")";
			}

			sqlText = String.format(" SELECT COUNT(*)  \n" + " FROM ( %s ) A    \n" + " LEFT JOIN (%s) B \n"
					+ " ON A.INDEX = B.POOL_INDEX \n" + " %s ", sqlMain, sqlBackup, sqlWhere);

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				count = db.getInteger(rs, "COUNT");
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
		return count;
	}

	public void testGetTotalGroupList() {
		OBDtoAdcScope scope = new OBDtoAdcScope();
		scope.setLevel(OBDtoAdcScope.LEVEL_ALL);
		// scope.setLevel(OBDtoAdcScope.LEVEL_ADC);
		// scope.setIndex(7);
		Integer accountIndex = 1;
		OBDtoMonTotalGroupCondition condition = null;

		condition = new OBDtoMonTotalGroupCondition();

		// 백업여부
		condition.getBackup().setSelect(true); // 필드 선택 여부
		condition.getBackup().getFilter().get(0).setSelect(false);
		condition.getBackup().getFilter().get(1).setSelect(false);
		condition.getBackup().getFilter().get(2).setSelect(true);

		// 그룹 이름
		condition.getName().setSelect(true);

		// member count
		condition.getMember().setSelect(true);

		// search - group name or group id
		// condition.setSearchKeyword("192.");

		Integer beginIndex = 0;
		Integer endIndex = 10000;
		Integer orderType = OBDefine.ORDER_TYPE_ADCNAME;
		Integer orderDir = OBDefine.ORDER_DIR_ASCEND;
		OBDtoMonTotalGroup result = null;

		try {
			System.out.println("count = " + getTotalGroupListCount(scope, accountIndex, condition));
			result = getTotalGroupList(scope, accountIndex, condition, beginIndex, endIndex, orderType, orderDir);

			// ADC 주요 값 보기
			int i = 0;
			for (OBDtoMonTotalGroupOne g : result.getGroupList()) {
				System.out.println(String.format(" %s:%s - backup=%s adc=%s(%d), member=%d, vs=%d, filter=%d ",
						g.getName(), g.getId(), g.getBackup(), g.getAdcIp(), g.getAdcIndex(), g.getMember(),
						g.getVsAssigned(), g.getFilter()));
				if (i++ > 1000) {
					break;
				}
			}
		} catch (OBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public OBDtoMonTotalReal getTotalRealListToExport(OBDtoAdcScope scope, Integer accountIndex,
			OBDtoMonTotalRealCondition condition, Integer beginIndex, Integer endIndex) throws OBException {
		OBDtoOrdering orderOption = new OBDtoOrdering();
		orderOption.setOrderDirection(OBDefine.ORDER_DIR_DESCEND);
		orderOption.setOrderType(OBDefine.ORDER_TYPE_VSNAME);
		return getTotalRealList(scope, accountIndex, condition, beginIndex, endIndex, orderOption.getOrderType(),
				orderOption.getOrderDirection());
	}

	@Override
	public OBDtoMonTotalReal getTotalRealList(OBDtoAdcScope scope, Integer accountIndex,
			OBDtoMonTotalRealCondition condition, Integer beginIndex, Integer endIndex, Integer orderType,
			Integer orderDir) throws OBException {
		OBDtoMonTotalReal result = new OBDtoMonTotalReal();
		OBDatabase db = new OBDatabase();
		String userAdcIndexString;
		String sqlText = "";
		String sqlMain = "";
		String sqlUsed = "";
		String sqlGroup = "";
		// String sqlFilter="";
		String extra = "";
		String sqlWhere = "";
		ArrayList<String> whereItem = new ArrayList<String>();
		ArrayList<FilterValue> multiFilterValue = null;

		db.openDB();
		try {
			// 사용자의 유효 adcIndex를 구한다.
			userAdcIndexString = new OBAdcManagementImpl().getUsersAdcIndexString(scope.getLevel(), scope.getIndex(),
					accountIndex);
			// 최초 검색이어서 조건이 없으면 검색조건을 초기화 한다.
			if (condition == null) // 기존 condition이 없으면 기본값 채운다.
			{
				condition = new OBDtoMonTotalRealCondition();
			}
			result.setCondition(condition);
			if (userAdcIndexString.isEmpty() == true) { // 선택된 adc가 없으면 빈 result return
				return result;
			}

			// 그룹 - main, 선택과 상관없이 query.
			sqlMain = String.format(
					" SELECT A1.INDEX, CASE WHEN A1.NAME='' THEN NULL ELSE A1.NAME END, A1.ALTEON_ID, A1.IP_ADDRESS, A1.STATUS, A1.STATE, A1.RATIO, \n"
							+ "     A2.INDEX ADC_INDEX, A2.NAME ADC_NAME, A2.IPADDRESS ADC_IP, A2.TYPE ADC_TYPE \n"
							+ " FROM (SELECT INDEX, NAME, ALTEON_ID, IP_ADDRESS, STATUS, STATE, ADC_INDEX, RATIO FROM TMP_SLB_NODE WHERE ADC_INDEX IN (%s)) A1 \n"
							+ " LEFT JOIN (SELECT INDEX, NAME, IPADDRESS, TYPE FROM MNG_ADC WHERE INDEX IN (%s)) A2 \n"
							+ " ON A1.ADC_INDEX = A2.INDEX ",
					userAdcIndexString, userAdcIndexString);

			// 검색어: ????
			if (condition.getSearchKeyword() != null && condition.getSearchKeyword().isEmpty() == false) // 검색어가 유효하면
																											// 검색한다.
			{
				extra = "%" + OBParser.removeWildcard(condition.getSearchKeyword()) + "%";
				extra = String.format(" NAME LIKE %s OR IP_ADDRESS LIKE %s", OBParser.sqlString(extra),
						OBParser.sqlString(extra));
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}
			// status 필터
			if (condition.getStatus().isSelect() == true) // 필수 항목. 필터도 있음
			{
				// sql은 기본에 있으므로 필터만 적용한다.
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getStatus().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += "OR"; // 각 조건항을 logical-or로 연결한다.
					}
					extra += String.format(" STATUS = %d ", entry.getFrom());
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}

			// state 필터
			if (condition.getState().isSelect() == true) // 필수 항목. 필터도 있음
			{
				// sql은 기본에 있으므로 필터만 적용한다.
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getState().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += "OR"; // 각 조건항을 logical-or로 연결한다.
					}
					extra += String.format(" STATE = %d ", entry.getFrom());
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}

			// real이 설정에 쓰이는가?
			if (condition.getUsed().isSelect() == true) // 옵션이어서 선택 안됐을 수 있다. 필터:백업있음, 백업없음
			{
				sqlUsed = String.format(
						" SELECT B1.INDEX NODE_INDEX, CASE WHEN B2.USED_INDEX IS NULL THEN 2 ELSE 1 END IS_USED \n"
								+ " FROM (SELECT INDEX FROM TMP_SLB_NODE WHERE ADC_INDEX IN (%s)) B1 \n"
								+ " LEFT JOIN (SELECT DISTINCT(NODE_INDEX) USED_INDEX FROM TMP_SLB_POOLMEMBER WHERE ADC_INDEX IN (%s) \n"
								+ "            UNION SELECT BAK_ID USED_INDEX FROM TMP_SLB_NODE WHERE ADC_INDEX IN (%s) AND BAK_TYPE = %d) B2 \n"
								+ " ON B1.INDEX = B2.USED_INDEX ",
						userAdcIndexString, userAdcIndexString, userAdcIndexString, OBDefine.BACKUP_STATE.REALBAK);

				// 필터 처리
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getUsed().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += "OR"; // 각 조건항을 logical-or로 연결한다.
					}
					if (entry.getFrom() == 0) // 안 사용
					{
						extra += String.format(" IS_USED = 2 "); // 안씀
					} else
					// 사용
					{
						extra += String.format(" IS_USED = 1 "); // 씀
					}
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			} else
			// 선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			{
				sqlUsed = " SELECT 'NONE'::VARCHAR NODE_INDEX, 2 IS_USED ";
			}

			// real이 참여한 group 수, 한 그룹에 real이 복수로 들어갈 가능성을 감안해 distinct를 count 전 처리
			if (condition.getGroup().isSelect() == true) {
				sqlGroup = String.format(" SELECT NODE_INDEX, COUNT(DISTINCT(POOL_INDEX)) COUNT_GROUP "
						+ " FROM TMP_SLB_POOLMEMBER " + " WHERE ADC_INDEX IN (%s) " + " GROUP BY NODE_INDEX ",
						userAdcIndexString);
			} else
			// 선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			{
				sqlGroup = " SELECT 'NONE'::VARCHAR NODE_INDEX, -1 COUNT_GROUP ";
			}

			for (String item : whereItem) {
				if (sqlWhere.isEmpty() == true) {
					sqlWhere = " WHERE ";
				} else
				// 2번째 항부터 and로 연결
				{
					sqlWhere += " AND ";
				}
				sqlWhere += "(" + item + ")";
			}
			sqlText = String.format(
					" SELECT A.INDEX, A.NAME, A.ALTEON_ID, A.IP_ADDRESS, A.STATUS, A.STATE, A.RATIO, COALESCE(A.RATIO, -1) RATIO_VALUE, \n"
							+ "     A.ADC_INDEX, A.ADC_NAME, A.ADC_IP, A.ADC_TYPE, B.IS_USED, C.COUNT_GROUP, COALESCE(C.COUNT_GROUP, -1) COUNT_GROUP_VALUE "
							+ " FROM ( %s ) A              \n" + // group
																	// &
																	// ADC
							" LEFT JOIN ( %s ) B         \n" + // is node used?
							" ON A.INDEX = B.NODE_INDEX  \n" + " LEFT JOIN ( %s ) C         \n" + // involved group
							" ON A.INDEX = C.NODE_INDEX  \n" + " %s   \n" // 각종 필터들때문에 들어가는 where절 통으로
					, sqlMain, sqlUsed, sqlGroup, sqlWhere);

			// order 처리
			sqlText += getListOrderByType(orderType, orderDir, new Order().TOTAL_REAL_COLS);

			// 페이징
			int offset = 0;
			String sqlRange = "";

			if (beginIndex != null && beginIndex.intValue() >= 0) {
				offset = beginIndex.intValue();
				sqlRange += " OFFSET " + beginIndex.intValue();
			}
			if (endIndex != null && endIndex.intValue() >= 0) {
				sqlRange += " LIMIT " + (Math.abs(endIndex.intValue() - offset) + 1);
			}
			sqlText += sqlRange;

			ArrayList<OBDtoMonTotalRealOne> realList = new ArrayList<OBDtoMonTotalRealOne>();
			OBDtoMonTotalRealOne real = null;

			ResultSet rs = db.executeQuery(sqlText);
			Integer tempInt = 0;
			while (rs.next()) {
				real = new OBDtoMonTotalRealOne();
				real.setIndex(db.getString(rs, "INDEX"));
				real.setName(db.getString(rs, "NAME"));
				real.setIp(db.getString(rs, "IP_ADDRESS"));
				real.setStatus(db.getInteger(rs, "STATUS"));
				real.setState(db.getInteger(rs, "STATE"));
				real.setRatio(db.getInteger(rs, "RATIO_VALUE"));
				real.setAlteonId(db.getString(rs, "ALTEON_ID"));

				tempInt = db.getInteger(rs, "IS_USED");

				if (tempInt.equals(1)) // 사용
				{
					real.setUsed(1);
				} else
				// =2
				{
					real.setUsed(0); // 안사용
				}
				real.setGroup(db.getInteger(rs, "COUNT_GROUP_VALUE"));
				real.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				real.setAdcName(db.getString(rs, "ADC_NAME"));
				real.setAdcIp(db.getString(rs, "ADC_IP"));
				real.setAdcType(db.getInteger(rs, "ADC_TYPE"));

				realList.add(real);
			}

			result.setRealList(realList);
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

	@Override
	public Integer getTotalRealListCount(OBDtoAdcScope scope, Integer accountIndex,
			OBDtoMonTotalRealCondition condition) throws OBException {
		OBDatabase db = new OBDatabase();
		String userAdcIndexString;
		String sqlText = "";
		String sqlMain = "";
		String sqlUsed = "";
		String extra = "";
		String sqlWhere = "";
		ArrayList<String> whereItem = new ArrayList<String>();
		ArrayList<FilterValue> multiFilterValue = null;
		Integer count = 0;

		db.openDB();
		try {
			// 사용자의 유효 adcIndex를 구한다.
			userAdcIndexString = new OBAdcManagementImpl().getUsersAdcIndexString(scope.getLevel(), scope.getIndex(),
					accountIndex);
			// 최초 검색이어서 조건이 없으면 검색조건을 초기화 한다.
			if (condition == null) // 기존 condition이 없으면 기본값 채운다.
			{
				condition = new OBDtoMonTotalRealCondition();
			}
			if (userAdcIndexString.isEmpty() == true) { // 선택된 adc가 없으면 빈 result return
				return 0;
			}

			// 그룹 - main, 선택과 상관없이 query.
			sqlMain = String.format(
					" SELECT INDEX, NAME, IP_ADDRESS, STATUS, STATE FROM TMP_SLB_NODE A1 WHERE ADC_INDEX IN (%s) ",
					userAdcIndexString);

			// 검색어: ????
			if (condition.getSearchKeyword() != null && condition.getSearchKeyword().isEmpty() == false) // 검색어가 유효하면
																											// 검색한다.
			{
				extra = "%" + OBParser.removeWildcard(condition.getSearchKeyword()) + "%";
				extra = String.format(" NAME LIKE %s OR IP_ADDRESS LIKE %s", OBParser.sqlString(extra),
						OBParser.sqlString(extra));
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}
			// status 필터
			if (condition.getStatus().isSelect() == true) // 필수 항목. 필터도 있음
			{
				// sql은 기본에 있으므로 필터만 적용한다.
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getStatus().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += "OR"; // 각 조건항을 logical-or로 연결한다.
					}
					extra += String.format(" STATUS = %d ", entry.getFrom());
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}

			// state 필터
			if (condition.getState().isSelect() == true) // 필수 항목. 필터도 있음
			{
				// sql은 기본에 있으므로 필터만 적용한다.
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getState().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += "OR"; // 각 조건항을 logical-or로 연결한다.
					}
					extra += String.format(" STATE = %d ", entry.getFrom());
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			}
			// real이 설정에 쓰이는가?
			if (condition.getUsed().isSelect() == true) // 옵션이어서 선택 안됐을 수 있다. 필터:백업있음, 백업없음
			{
				sqlUsed = String.format(
						" SELECT B1.INDEX NODE_INDEX, CASE WHEN B2.USED_INDEX IS NULL THEN 2 ELSE 1 END IS_USED \n"
								+ " FROM (SELECT INDEX FROM TMP_SLB_NODE WHERE ADC_INDEX IN (%s)) B1 \n"
								+ " LEFT JOIN (SELECT DISTINCT(NODE_INDEX) USED_INDEX FROM TMP_SLB_POOLMEMBER WHERE ADC_INDEX IN (%s) \n"
								+ "            UNION SELECT BAK_ID USED_INDEX FROM TMP_SLB_NODE WHERE ADC_INDEX IN (%s) AND BAK_TYPE = %d) B2 \n"
								+ " ON B1.INDEX = B2.USED_INDEX ",
						userAdcIndexString, userAdcIndexString, userAdcIndexString, OBDefine.BACKUP_STATE.REALBAK);

				// 필터 처리
				extra = "";
				multiFilterValue = getFilterNumberMulti(condition.getUsed().getFilter());
				for (FilterValue entry : multiFilterValue) // "전체"를 선택한 경우 list.size()==0 이므로 자연스럽게 where절이 붙지 않는다.
				{
					if (extra.isEmpty() == false) {
						extra += "OR"; // 각 조건항을 logical-or로 연결한다.
					}
					if (entry.getFrom() == 0) // 안 사용
					{
						extra += String.format(" IS_USED = 2 "); // 안씀
					} else
					// 사용
					{
						extra += String.format(" IS_USED = 1 "); // 씀
					}
				}
				if (extra.isEmpty() == false) // 유효 필터가 있으면 붙인다.
				{
					whereItem.add(extra);
				}
			} else
			// 선택되지 않았을 경우 실질적인 join을 우회하는 대체 쿼리
			{
				sqlUsed = " SELECT 'NONE'::VARCHAR NODE_INDEX, 2 IS_USED ";
			}

			for (String item : whereItem) {
				if (sqlWhere.isEmpty() == true) {
					sqlWhere = " WHERE ";
				} else
				// 2번째 항부터 and로 연결
				{
					sqlWhere += " AND ";
				}
				sqlWhere += "(" + item + ")";
			}
			sqlText = String.format(" SELECT COUNT(*) FROM ( %s ) A   \n" + // group & ADC
					" LEFT JOIN ( %s ) B              \n" + // is node used?
					" ON A.INDEX = B.NODE_INDEX  \n" + " %s   \n" // 각종 필터들때문에 들어가는 where절 통으로
					, sqlMain, sqlUsed, sqlWhere);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				count = db.getInteger(rs, "COUNT");
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
		return count;
	}

	public void testGetTotalRealList() {
		OBDtoAdcScope scope = new OBDtoAdcScope();
		scope.setLevel(OBDtoAdcScope.LEVEL_ALL);
		Integer accountIndex = 1;
		OBDtoMonTotalRealCondition condition = null;

		condition = new OBDtoMonTotalRealCondition();

		// 백업여부
		// condition.getBackup().setSelect(true); //필드 선택 여부
		// condition.getBackup().getFilter().get(0).setSelect(true);
		// condition.getBackup().getFilter().get(1).setSelect(true);
		// condition.getBackup().getFilter().get(2).setSelect(true);

		// 그룹 이름
		// condition.getName().setSelect(true);
		//
		// condition.getStatus().setSelect(true);
		// condition.getStatus().getFilter().get(0).setSelect(false);
		// condition.getStatus().getFilter().get(2).setSelect(true);

		condition.getState().setSelect(true);
		condition.getState().getFilter().get(0).setSelect(false);
		condition.getState().getFilter().get(1).setSelect(true);

		// is real used?
		// condition.getUsed().setSelect(true);
		// condition.getUsed().getFilter().get(0).setSelect(false);
		// condition.getUsed().getFilter().get(1).setSelect(true);

		// involved group count
		condition.getGroup().setSelect(true);

		// search - group name or group id
		// condition.setSearchKeyword("192.");

		Integer beginIndex = 0;
		Integer endIndex = 10000;
		Integer orderType = OBDefine.ORDER_TYPE_ADCNAME;
		Integer orderDir = OBDefine.ORDER_DIR_ASCEND;
		OBDtoMonTotalReal result = null;

		try {
			System.out.println("count = " + getTotalRealListCount(scope, accountIndex, condition));
			result = getTotalRealList(scope, accountIndex, condition, beginIndex, endIndex, orderType, orderDir);

			// ADC 주요 값 보기
			for (OBDtoMonTotalRealOne r : result.getRealList()) {
				System.out.println(String.format(" %s(%s) - ratio=%d, adc=%s(%d), used=%d, group=(%d)", r.getIp(),
						r.getName(), r.getRatio(), r.getAdcIp(), r.getAdcIndex(), r.getUsed(), r.getGroup()));
			}
		} catch (OBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public OBDtoServiceMonitoringChart getServiceMonitoringChart(String target, String name, OBDtoSearch searchOption)
			throws OBException {
		OBDtoServiceMonitoringChart retVal = new OBDtoServiceMonitoringChart();
		try {
			retVal = new OBMonitoringImpl().getVsBpsHistoryByChart(target, name, searchOption);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private OBDtoServiceMonitoringChart getVsBpsHistoryByChart(String vsIndex, String name, OBDtoSearch searchOption)
			throws OBException {

		OBDtoServiceMonitoringChart retVal = new OBDtoServiceMonitoringChart();
		String sqlText = "";
		OBDatabase db = new OBDatabase();

		try {
			if (vsIndex == null) {
				return retVal;
			}
			OBDtoSystemEnvAdditional env = new OBEnvManagementImpl().getAdditionalConfig();
			int interval = env.getIntervalAdcConfSync();
			String arg[] = vsIndex.replaceAll("\\s", "").split(",");
			ArrayList<String> vsIndexList = new ArrayList<String>(Arrays.asList(arg));
			arg = name.replaceAll("\\s", "").split(",");
			ArrayList<String> vsNameList = new ArrayList<String>(Arrays.asList(arg));
			db.openDB();
			int vsSize = vsIndexList.size();
			int retValSize = 0;

			ArrayList<OBDtoNameCurAvgMax> bpsList = new ArrayList<OBDtoNameCurAvgMax>();
			ArrayList<OBDtoNameCurAvgMax> connList = new ArrayList<OBDtoNameCurAvgMax>();
			ArrayList<OBDtoBpsConnData> bpsconnList = new ArrayList<OBDtoBpsConnData>();

			for (int i = 0; i < vsSize; i++) {

				String sqlSearch = makeTimeSqlText(searchOption, "OCCUR_TIME");

				sqlText = String.format(
						" SELECT (to_timestamp(floor((extract('epoch' from OCCUR_TIME) / %d )) * %d) )  \n"
								+ " AS INTERVAL_TIME, BPS_TOT, CONN_CURR                                        \n"
								+ " FROM LOG_SVC_PERF_STATS                                                     \n"
								+ " WHERE OBJ_INDEX = %s AND %s                                                 \n"
								+ " GROUP BY INTERVAL_TIME, BPS_TOT, CONN_CURR                                  \n"
								+ " ORDER BY INTERVAL_TIME ASC                                                  \n",
						interval, interval, OBParser.sqlString(vsIndexList.get(i)), sqlSearch);

				ResultSet rs = db.executeQuery(sqlText);
				if (rs.next()) {
					do {
						if (i == 0) {
							OBDtoBpsConnData chart = new OBDtoBpsConnData();
							chart.setOccurTime(db.getTimestamp(rs, "INTERVAL_TIME"));

							ArrayList<Long> valueBps = new ArrayList<Long>();
							ArrayList<Long> valueConn = new ArrayList<Long>();
							ArrayList<String> valueName = new ArrayList<String>();
							valueConn.add(db.getLong(rs, "CONN_CURR"));
							valueBps.add(db.getLong(rs, "BPS_TOT"));
							valueName.add(vsNameList.get(i));
							if (vsSize > 1) {
								for (int k = 1; k < vsSize; k++) {
									valueConn.add(-1L);
									valueBps.add(-1L);
									valueName.add(vsNameList.get(k));
								}
							}
							chart.setBpsInValue(valueBps);
							chart.setConnCurrValue(valueConn);
							chart.setName(valueName);
							bpsconnList.add(chart);
							retVal.setBpsConn(bpsconnList);
						} else {

							for (int j = 0; j < retValSize; j++) {
								if (retVal.getBpsConn().get(j).getOccurTime()
										.equals(db.getTimestamp(rs, "INTERVAL_TIME"))) {
									retVal.getBpsConn().get(j).getBpsInValue().set(i, db.getLong(rs, "BPS_TOT"));
									retVal.getBpsConn().get(j).getConnCurrValue().set(i, db.getLong(rs, "CONN_CURR"));
									retVal.getBpsConn().get(j).getName().set(i, vsNameList.get(i));
								}
							}
						}
					} while (rs.next());
				} else {
					if (retVal.getBpsConn() != null) {
						retVal.getBpsConn().get(0).getName().add(vsNameList.get(i));
						retVal.getBpsConn().get(0).getBpsInValue().add(-1L);
						retVal.getBpsConn().get(0).getConnCurrValue().add(-1L);

						OBDtoNameCurAvgMax bpsInfo = new OBDtoNameCurAvgMax();
						bpsInfo.setName(vsNameList.get(i));
						bpsInfo.setCurrent(-1L);
						bpsInfo.setMax(-1L);
						bpsInfo.setAvg(-1L);
						bpsList.add(bpsInfo);

						OBDtoNameCurAvgMax connInfo = new OBDtoNameCurAvgMax();
						connInfo.setName(vsNameList.get(i));
						connInfo.setCurrent(-1L);
						connInfo.setMax(-1L);
						connInfo.setAvg(-1L);
						connList.add(connInfo);
					}

					continue;
				}
				if (retVal.getBpsConn() == null)
					retValSize = 0;
				else
					retValSize = retVal.getBpsConn().size();
				sqlText = String.format(
						" SELECT COALESCE(MAX(BPS_TOT),-1) AS MAX_BPS_TOT, COALESCE(MAX(CONN_CURR), -1) AS MAX_CONN_CURR, \n"
								+ " COALESCE(AVG(BPS_TOT), -1) AS AVG_BPS_TOT, COALESCE(AVG(CONN_CURR), -1) AS AVG_CONN_CURR        \n"
								+ " FROM LOG_SVC_PERF_STATS                                                                         \n"
								+ " WHERE OBJ_INDEX = %s AND BPS_IN >= 0 AND %s                                                     \n",
						OBParser.sqlString(vsIndexList.get(i)), sqlSearch);

				rs = db.executeQuery(sqlText);

				if (rs.next() && retValSize != 0 && db.getLong(rs, "MAX_BPS_TOT") != -1L
						&& db.getLong(rs, "AVG_BPS_TOT") != -1L && db.getLong(rs, "MAX_CONN_CURR") != -1L
						&& db.getLong(rs, "AVG_CONN_CURR") != -1L) {

					OBDtoNameCurAvgMax bpsInfo = new OBDtoNameCurAvgMax();
					bpsInfo.setName(vsNameList.get(i));
					bpsInfo.setCurrent(retVal.getBpsConn().get(retValSize - 1).getBpsInValue().get(i));
					bpsInfo.setMax(db.getLong(rs, "MAX_BPS_TOT"));
					bpsInfo.setAvg(db.getLong(rs, "AVG_BPS_TOT"));
					bpsList.add(bpsInfo);

					OBDtoNameCurAvgMax connInfo = new OBDtoNameCurAvgMax();
					connInfo.setName(vsNameList.get(i));
					connInfo.setCurrent(retVal.getBpsConn().get(retValSize - 1).getConnCurrValue().get(i));
					connInfo.setMax(db.getLong(rs, "MAX_CONN_CURR"));
					connInfo.setAvg(db.getLong(rs, "AVG_CONN_CURR"));
					connList.add(connInfo);
				} else {
					OBDtoNameCurAvgMax bpsInfo = new OBDtoNameCurAvgMax();
					bpsInfo.setName(vsNameList.get(i));
					bpsInfo.setCurrent(-1L);
					bpsInfo.setMax(-1L);
					bpsInfo.setAvg(-1L);
					bpsList.add(bpsInfo);

					OBDtoNameCurAvgMax connInfo = new OBDtoNameCurAvgMax();
					connInfo.setName(vsNameList.get(i));
					connInfo.setCurrent(-1L);
					connInfo.setMax(-1L);
					connInfo.setAvg(-1L);
					connList.add(connInfo);
				}
			}
			retVal.setBpsInfo(bpsList);
			retVal.setConnInfo(connList);

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

	private String makeTimeSqlText(OBDtoSearch searchOption, String columnName) throws OBException {
		String retVal = "";

		if (searchOption == null)
			return retVal;

		if (searchOption.getToTime() == null)
			searchOption.setToTime(new Date());
		retVal = String.format(" %s <= %s ", columnName,
				OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getToTime().getTime()))));

		if (searchOption.getFromTime() == null) {
			searchOption.setFromTime(new Date(searchOption.getToTime().getTime() - 7 * 24 * 60 * 60 * 1000));// 7일전 시간.
		}

		retVal += String.format(" AND %s >= %s ", columnName,
				OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getFromTime().getTime()))));

		return retVal;
	}

	private String getPrimaryIndex(Integer adcType, String vsIndex, String vsvcIndex) {
		if (adcType == OBDefine.ADC_TYPE_ALTEON)
			return vsvcIndex;
		else
			return vsIndex;
	}

	private Map<String, ServiceMapVsDescDto> getVsDescriptionInfoList(Integer adcIndex) throws Exception {
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		Map<String, ServiceMapVsDescDto> resultMap = new HashMap<String, ServiceMapVsDescDto>();
		try {
			db.openDB();
			sqlText = String.format(" SELECT * FROM MNG_NETWORK_MAP_VS_DESCRIPTION WHERE ADC_INDEX=%s;", adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				ServiceMapVsDescDto obj = new ServiceMapVsDescDto();
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setAdcType(db.getInteger(rs, "ADC_TYPE"));
				obj.setDescription(db.getString(rs, "DESCRIPTION"));
				obj.setVsIndex(db.getString(rs, "VS_INDEX"));
				obj.setVsvcIndex(db.getString(rs, "VSVC_INDEX"));
				String index = getPrimaryIndex(obj.getAdcType(), obj.getVsIndex(), obj.getVsvcIndex());
				resultMap.put(index, obj);
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
		return resultMap;
	}

	@Override
	public void saveVsDescription(ServiceMapVsDescDto descDto) throws Exception {
		String sqlText = "";
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();
			String dataIndex = getPrimaryIndex(descDto.getAdcType(), descDto.getVsIndex(), descDto.getVsvcIndex());
			sqlText = String.format(" SELECT * FROM MNG_NETWORK_MAP_VS_DESCRIPTION WHERE INDEX=%s LIMIT 1;",
					OBParser.sqlString(dataIndex));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs != null && rs.next() == true) {
				// 데이터가 존재하는 경우임.
				String description = descDto.getDescription();
				if (descDto.getDescription().length() > 50)
					description = descDto.getDescription().substring(0, 50);// 20자까지만 저장.
				sqlText = String.format(
						" UPDATE MNG_NETWORK_MAP_VS_DESCRIPTION \n" + " SET DESCRIPTION=%s              \n"
								+ " WHERE INDEX=%s            \n",
						OBParser.sqlString(description), OBParser.sqlString(dataIndex));
				db.executeUpdate(sqlText);
			} else {
				// 데이터가 없는 신규인경우임.
				sqlText = String.format(
						" INSERT INTO MNG_NETWORK_MAP_VS_DESCRIPTION                                        \n"
								+ " (INDEX, ADC_INDEX, ADC_TYPE, VS_INDEX, VSVC_INDEX, DESCRIPTION)         \n"
								+ " VALUES                                                                  \n"
								+ " ( %s, %d, %d, %s, %s, %s);                                              \n",
						OBParser.sqlString(dataIndex), descDto.getAdcIndex(), descDto.getAdcType(),
						OBParser.sqlString(descDto.getVsIndex()), OBParser.sqlString(descDto.getVsvcIndex()),
						OBParser.sqlString(descDto.getDescription()));
				db.executeUpdate(sqlText);
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
}
