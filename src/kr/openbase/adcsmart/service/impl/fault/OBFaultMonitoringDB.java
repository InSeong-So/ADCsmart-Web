package kr.openbase.adcsmart.service.impl.fault;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPortStatus;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.OBDtoHddInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.dto.fault.OBDtoAdcCurrentSession;
import kr.openbase.adcsmart.service.dto.fault.OBDtoAdcSTGStatus;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultBpsConnData;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResultContent;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResultElement;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCpuDataObj;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCpuHistory;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultDataObj;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultHWStatus;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultPreBpsConnChart;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultSessionInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultVlanInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFlbSlbSession;
import kr.openbase.adcsmart.service.dto.fault.OBDtoL2SearchInfo;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcMonCpuDataObj;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcMonCpuHistory;
import kr.openbase.adcsmart.service.impl.dto.OBDtoCpuMemStatus;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonL2Ports;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficAdc;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficPoolGroup;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficPoolGroupMember;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficVSvcInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficVSvcMemberInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoPowerSupplyInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoSTGStatus;
import kr.openbase.adcsmart.service.impl.dto.OBDtoVSStatusCountInfo;
import kr.openbase.adcsmart.service.impl.dto.OBHostInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoBpsPpsEpsDps;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoRespTimeAnalInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoVSvcRespTimeInfo;
import kr.openbase.adcsmart.service.snmp.dto.OBDtoArpInfo;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBFaultMonitoringDB {
	private Integer cpuMaxUsage = 80;
	private Integer hddMaxUsage = 80;

	public OBFaultMonitoringDB() {
		// 환경 변수를 읽어 들인다.
		try {
			String propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
					OBDefine.PROPERTY_KEY_SYSTEM_CPU_MAX_USAGE);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.cpuMaxUsage = Integer.parseInt(propertyValue);

			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
					OBDefine.PROPERTY_KEY_SYSTEM_HDD_MAX_USAGE);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.hddMaxUsage = Integer.parseInt(propertyValue);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateSessionLog(ArrayList<Integer> adcIndexList, ArrayList<OBDtoFaultSessionInfo> logList)
			throws OBException {
		Timestamp occurTime = OBDateTime.toTimestamp(OBDateTime.now());
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			deleteSessionLog(adcIndexList);

			String valueText = "";
			for (OBDtoFaultSessionInfo log : logList) {
				if (!valueText.isEmpty())
					valueText += ", ";
				valueText += String.format("( %d, %s, %s, %s, %s, %s, %s, %s, %s, %d, %d, %s )", log.getAdcIndex(),
						OBParser.sqlString(occurTime), OBParser.sqlString(log.getSrcIP()),
						OBParser.sqlString(log.getDstIP()), OBParser.sqlString(log.getRealIP()),
						OBParser.sqlString(log.getSrcPort()), OBParser.sqlString(log.getDstPort()),
						OBParser.sqlString(log.getRealPort()), OBParser.sqlString(log.getDamPort()), log.getProtocol(),
						log.getAgingTime(), OBParser.sqlString(log.getSpNumber()));
			}

			// 로그를 저장한다.
			if (!valueText.isEmpty()) {
				sqlText = String.format(" INSERT INTO TMP_SESSION_INFO                                               \n"
						+ " (ADC_INDEX, OCCUR_TIME, CLIENT_IP, VIRTUAL_IP, REAL_IP,                    \n"
						+ " CLIENT_PORT, VIRTUAL_PORT, REAL_PORT, DAM_PORT, PROTOCOL, AGE_TIME, SP_NO) \n"
						+ " VALUES                                                                                  \n"
						+ " %s ", valueText);

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

	public void deleteSessionLog(ArrayList<Integer> adcIndexList) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String valueText = "-1";
			for (Integer adcIndex : adcIndexList) {
				valueText += ", " + adcIndex;
			}

			// 로그를 저장한다.
			sqlText = String.format(" DELETE FROM TMP_SESSION_INFO                   \n"
					+ " WHERE ADC_INDEX IN ( %s )                      \n", valueText);

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

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	//
	// Date start = new
	// Date(OBDateTime.toTimestamp(OBDateTime.now()).getTime()-100000);
	// Timestamp startTime = new Timestamp(start.getTime());
	// Timestamp endTime = OBDateTime.toTimestamp(OBDateTime.now());
	//
	// ArrayList<OBDtoElement> hwItemList = new
	// OBFaultMngImpl().getElements(OBDefine.ELEMENT_TYPE_HW, db);
	// HashMap<Integer, OBDtoElement> hashMap = new HashMap<Integer,
	// OBDtoElement>();
	// for(OBDtoElement obj: hwItemList)
	// {
	// hashMap.put(obj.getIndex(), obj);
	// }
	// ArrayList<OBDtoFaultCheckResultElement> resultList = new
	// ArrayList<OBDtoFaultCheckResultElement>();
	//
	// OBDtoFaultCheckResultElement resultElement = new
	// OBDtoFaultCheckResultElement();
	// ArrayList<OBDtoFaultCheckResultContent> contentList = new
	// ArrayList<OBDtoFaultCheckResultContent>();
	// OBDtoFaultCheckResultContent content1 = new OBDtoFaultCheckResultContent();
	// content1.setDetail("detail.......");
	// content1.setSummary("summaryu......");
	// contentList.add(content1);
	// OBDtoFaultCheckResultContent content2 = new OBDtoFaultCheckResultContent();
	// content2.setDetail("detail222.......");
	// content2.setSummary("summaryu..2222....");
	// contentList.add(content2);
	// resultElement.setResultList(contentList);
	// OBDtoElement obj = hashMap.get(OBDefine.FAULT_CHECK_ELEMENT_L23_VLAN);
	// resultElement.setObj(obj);
	// resultElement.setStatus(OBDtoFaultCheckResultElement.STATUS_SUCC);
	// resultElement.setEndTime(endTime);
	// resultElement.setStartTime(startTime);
	// resultList.add(resultElement);
	//
	// new OBFaultMonitoringDB().writeFaultLogSummary(1387906198867L, resultList,
	// db);
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public void clearFaultLogSummary(long logKey) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" DELETE  FROM LOG_FAULT_SUMMARY WHERE LOG_KEY = %d \n", logKey);

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

	public void writeFaultLogSummary(long logKey, ArrayList<OBDtoFaultCheckResultElement> logList) throws OBException {
		if (logList.size() == 0)
			return;

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String valueText = "";
			for (OBDtoFaultCheckResultElement obj : logList) {
				for (OBDtoFaultCheckResultContent content : obj.getResultList()) {
					if (!valueText.isEmpty())
						valueText += ", ";
					valueText += String.format("( %d, %d, %d, %s, %s, %d, %s, %s )", logKey, obj.getObj().getCategory(),
							obj.getObj().getIndex(), OBParser.sqlString(obj.getStartTime()),
							OBParser.sqlString(obj.getEndTime()), obj.getStatus(),
							OBParser.sqlString(content.getSummary()),
							OBParser.sqlString(OBParser.escapeSql(content.getDetail())));
				}
			}

			// 로그를 저장한다.
			sqlText = String.format(
					" INSERT INTO LOG_FAULT_SUMMARY                                                           \n"
							+ " (LOG_KEY, CATEGORY, TYPE, START_TIME, END_TIME, STATUS, CONTENT, DETAIL)                \n"
							+ " VALUES                                                                                  \n"
							+ " %s ",
					valueText);

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

	public void writeFaultLogSummaryResponseTime(long logKey, Timestamp occurTime, Integer adcIndex, String vsIndex,
			String clientIP, OBDtoRespTimeAnalInfo responseInfo) throws OBException {
		if (responseInfo == null)
			return;

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			// 로그를 저장한다.
			sqlText = String.format(
					" INSERT INTO LOG_SUMMARY_RESP_TIME                                                           \n"
							+ " (OCCUR_TIME, LOG_KEY, ADC_INDEX, OBJ_INDEX, CLIENT_IP, END_POINT_AVG_TIME, END_POINT_MIN_TIME, END_POINT_MAX_TIME,"
							+ " DATA_CENTER_AVG_TIME, DATA_CENTER_MIN_TIME, DATA_CENTER_MAX_TIME)                                 \n"
							+ " VALUES                                                                                            \n"
							+ " ( %s, %d, %d, %s, %s, %d, %d, %d, %d, %d, %d ) ",
					OBParser.sqlString(occurTime), logKey, adcIndex, OBParser.sqlString(vsIndex),
					OBParser.sqlString(clientIP), responseInfo.getEndPointAvgTime(), responseInfo.getEndPointMinTime(),
					responseInfo.getEndPointMaxTime(), responseInfo.getDataCenterAvgTime(),
					responseInfo.getDataCenterMinTime(), responseInfo.getDataCenterMaxTime());

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

	private void deleteArpInfoList(Integer adcIndex) throws OBException {
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" DELETE FROM TMP_FAULT_ARP_INFO \n" + " WHERE ADC_INDEX = %d           \n",
					adcIndex);

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

	public void deleteL2SearchInfoList(ArrayList<Integer> adcIndexList) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String subText = "-1";
			for (Integer adcIndex : adcIndexList) {
				if (!subText.isEmpty())
					subText += ", ";
				subText += adcIndex;
			}
			sqlText = String.format(" DELETE FROM TMP_L2_SEARCH_INFO \n" + " WHERE ADC_INDEX IN ( %s )      \n",
					subText);

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

	// arp 정보를 저장한다.
	public void writeArpInfoList(Timestamp occurTime, Integer adcIndex, ArrayList<OBDtoArpInfo> arpInfoList)
			throws OBException {
		if (arpInfoList == null || arpInfoList.isEmpty())
			return;

		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			// 이전 로그는 삭제한다.
			deleteArpInfoList(adcIndex);

			// 로그를 저장한다.
			String subText = "";
			for (OBDtoArpInfo info : arpInfoList) {
				if (!subText.isEmpty())
					subText += ", ";
				subText += String.format("(%s, %d, %s, %s, %s)", OBParser.sqlString(occurTime), adcIndex,
						OBParser.sqlString(info.getDstIPAddr()), OBParser.sqlString(info.getMacAddr()),
						OBParser.sqlString(info.getPortNum()));
			}

			sqlText = String.format(" INSERT INTO TMP_FAULT_ARP_INFO                                   \n"
					+ " (OCCUR_TIME, ADC_INDEX, IP_ADDR, MAC_ADDR, PHY_PORT_NUM)         \n"
					+ " VALUES                                                           \n"
					+ " %s                                                                                            \n",
					subText);

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

	// arp 정보를 저장한다.
	public void writeL2SearchInfoList(Timestamp occurTime, ArrayList<Integer> adcIndexList,
			ArrayList<OBDtoL2SearchInfo> l2SearchInfoList) throws OBException {
		if (l2SearchInfoList == null || l2SearchInfoList.isEmpty()) {
			deleteL2SearchInfoList(adcIndexList);
			return;
		}

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			// 이전 로그는 삭제한다.
			deleteL2SearchInfoList(adcIndexList);

			// 로그를 저장한다.
			String subText = "";
			for (OBDtoL2SearchInfo info : l2SearchInfoList) {
				if (!subText.isEmpty())
					subText += ", ";
				subText += String.format("(%s, %d, %s, %s, %s, %s)", OBParser.sqlString(occurTime), info.getAdcIndex(),
						OBParser.sqlString(info.getIpAddress()), OBParser.sqlString(info.getMacAddress()),
						OBParser.sqlString(info.getVlanInfo()), OBParser.sqlString(info.getInterfaceInfo()));
			}

			sqlText = String.format(" INSERT INTO TMP_L2_SEARCH_INFO                                   \n"
					+ " (OCCUR_TIME, ADC_INDEX, IP_ADDRESS, MAC_ADDRESS, VLAN_INFO, INTERFACE)         \n"
					+ " VALUES                                                           \n"
					+ " %s                                                                                            \n",
					subText);

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

	// arp 정보를 저장한다.
	public HashMap<String, OBDtoArpInfo> getArpInfoList(Integer adcIndex) throws OBException {
		HashMap<String, OBDtoArpInfo> retVal = new HashMap<String, OBDtoArpInfo>();
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT  OCCUR_TIME, ADC_INDEX, IP_ADDR, MAC_ADDR, PHY_PORT_NUM         \n"
					+ " FROM TMP_FAULT_ARP_INFO                                                \n"
					+ " WHERE ADC_INDEX = %d                                                   \n", adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoArpInfo obj = new OBDtoArpInfo();
				obj.setDstIPAddr(db.getString(rs, "IP_ADDR"));
				obj.setMacAddr(db.getString(rs, "MAC_ADDR"));
				obj.setPortNum(db.getString(rs, "PHY_PORT_NUM"));
				retVal.put(obj.getDstIPAddr(), obj);
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

	public HashMap<String, OBDtoAdcPortStatus> getLastPortInterfaceInfo(Integer adcIndex) throws OBException {
		HashMap<String, OBDtoAdcPortStatus> retVal = new HashMap<String, OBDtoAdcPortStatus>();

		String sqlText = "";

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			OBDtoADCObject object = new OBDtoADCObject();
			object.setCategory(OBDtoADCObject.CATEGORY_ADC);
			object.setIndex(adcIndex);
			HashMap<Integer, OBDtoAdcPortStatus> basicInfoMap = getBasicPortInfoMapByIndex(object);

			Integer monInteval = new OBEnvManagementImpl().getAdcSyncInterval();// 초 단위.

			Timestamp beginTime = OBDateTime.getTimestampInterval(OBDateTime.toTimestamp(OBDateTime.now()),
					-monInteval * 2);

			// LOG_LINK_STATUS 에서 링크 상태 조회.
			sqlText = String.format(
					" SELECT OCCUR_TIME,                                                     \n"
							+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
							+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
							+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
							+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36                         \n"
							+ " FROM LOG_LINK_STATUS                                             \n"
							+ " WHERE ADC_INDEX = %d                                                   \n"
							+ " AND OCCUR_TIME >= %s                                                   \n"
							+ " ORDER BY OCCUR_TIME DESC                                               \n"
							+ " LIMIT 1                                                                \n",
					adcIndex, OBParser.sqlString(beginTime));
			ResultSet rs;
			rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				Date occurTime = db.getTimestamp(rs, "OCCUR_TIME");
				for (int portIndex = 1; portIndex <= 36; portIndex++) {
					OBDtoAdcPortStatus portInfo = new OBDtoAdcPortStatus();
					String portName = "PORT" + portIndex;
					Integer status = db.getInteger(rs, portName);

					portInfo.setLinkStatus(status);
					portInfo.setOccurTime(occurTime);

					OBDtoAdcPortStatus basicInfo = basicInfoMap.get(portIndex);
					if (basicInfo != null) {
						portInfo.setDispName(basicInfo.getDispName());
						portInfo.setIntfName(basicInfo.getIntfName());
						portInfo.setSpeed(basicInfo.getSpeed());
						portInfo.setPhyConnType(basicInfo.getPhyConnType());
						retVal.put(basicInfo.getIntfName(), portInfo);
					}
				}
			}

			// TMP_FAULT_LINK_INFO 에서 INTERFACE 이름 추출
			sqlText = String.format(" SELECT ADC_INDEX, PORT_INDEX, NAME, ALIAS_NAME                         \n"
					+ " FROM TMP_FAULT_LINK_INFO                                               \n"
					+ " WHERE ADC_INDEX = %d                                                   \n", adcIndex);
			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				Integer portIndex = db.getInteger(rs, "PORT_INDEX");
				String name = db.getString(rs, "NAME");
				// String portName = "PORT"+portIndex;
				OBDtoAdcPortStatus basicInfo = basicInfoMap.get(portIndex);
				if (basicInfo != null) {
					OBDtoAdcPortStatus portInfo = retVal.get(basicInfo.getIntfName());
					if (portInfo != null) {
						portInfo.setPortIndex(portIndex);
						portInfo.setIntfName(name);
					}
				}
			}

			// LOG_FAULT_LINK_PKT_RCV 에서 수신 패킷 개수
			sqlText = String.format(
					" SELECT OCCUR_TIME,                                                     \n"
							+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
							+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
							+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
							+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36                         \n"
							+ " FROM LOG_LINK_PKT_IN                                         \n"
							+ " WHERE ADC_INDEX = %d                                                   \n"
							+ " AND OCCUR_TIME >= %s                                                   \n"
							+ " ORDER BY OCCUR_TIME DESC                                               \n"
							+ " LIMIT 1                                                                \n",
					adcIndex, OBParser.sqlString(beginTime));
			rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				for (int portIndex = 1; portIndex <= 36; portIndex++) {
					String portName = "PORT" + portIndex;
					Long value = db.getLong(rs, portName);

					OBDtoAdcPortStatus basicInfo = basicInfoMap.get(portIndex);
					if (basicInfo != null) {
						OBDtoAdcPortStatus portInfo = retVal.get(basicInfo.getIntfName());
						if (portInfo != null) {
							portInfo.setPktsIn(value);
							if (portInfo.getPktsIn() != -1)
								portInfo.setPktsTot(portInfo.getPktsIn());
						}
					}
				}
			}

			// LOG_LINK_MODE 에서 수신 패킷 개수
			sqlText = String.format(
					" SELECT OCCUR_TIME,                                                     \n"
							+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
							+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
							+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
							+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36                         \n"
							+ " FROM LOG_LINK_MODE                                               \n"
							+ " WHERE ADC_INDEX = %d                                                   \n"
							+ " AND OCCUR_TIME >= %s                                                   \n"
							+ " ORDER BY OCCUR_TIME DESC                                               \n"
							+ " LIMIT 1                                                                \n",
					adcIndex, OBParser.sqlString(beginTime));
			rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				for (int portIndex = 1; portIndex <= 36; portIndex++) {
					String portName = "PORT" + portIndex;
					Integer value = db.getInteger(rs, portName);

					OBDtoAdcPortStatus basicInfo = basicInfoMap.get(portIndex);
					if (basicInfo != null) {
						OBDtoAdcPortStatus portInfo = retVal.get(basicInfo.getIntfName());
						if (portInfo != null) {
							portInfo.setMode(value);
						}
					}
				}
			}

			// LOG_LINK_PKT_OUT 에서 수신 패킷 개수
			sqlText = String.format(
					" SELECT OCCUR_TIME,                                                     \n"
							+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
							+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
							+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
							+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36                         \n"
							+ " FROM LOG_LINK_PKT_OUT                                        \n"
							+ " WHERE ADC_INDEX = %d                                                   \n"
							+ " AND OCCUR_TIME >= %s                                                   \n"
							+ " ORDER BY OCCUR_TIME DESC                                               \n"
							+ " LIMIT 1                                                                \n",
					adcIndex, OBParser.sqlString(beginTime));
			rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				for (int portIndex = 1; portIndex <= 36; portIndex++) {
					String portName = "PORT" + portIndex;
					Long value = db.getLong(rs, portName);

					OBDtoAdcPortStatus basicInfo = basicInfoMap.get(portIndex);
					if (basicInfo != null) {
						OBDtoAdcPortStatus portInfo = retVal.get(basicInfo.getIntfName());
						if (portInfo != null) {
							portInfo.setPktsOut(value);
							if (portInfo.getPktsOut() != -1)
								portInfo.setPktsTot(portInfo.getPktsTot() + portInfo.getPktsOut());
						}
					}
				}
			}

			// LOG_LINK_PKT_ERR 에서 에러 패킷 개수 조회.
			sqlText = String.format(
					" SELECT OCCUR_TIME,                                                     \n"
							+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
							+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
							+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
							+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36                         \n"
							+ " FROM LOG_LINK_PKT_ERR                                            \n"
							+ " WHERE ADC_INDEX = %d                                                   \n"
							+ " AND OCCUR_TIME >= %s                                                   \n"
							+ " ORDER BY OCCUR_TIME DESC                                               \n"
							+ " LIMIT 1                                                                \n",
					adcIndex, OBParser.sqlString(beginTime));
			rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				for (int portIndex = 1; portIndex <= 36; portIndex++) {
					String portName = "PORT" + portIndex;
					Long value = db.getLong(rs, portName);

					OBDtoAdcPortStatus basicInfo = basicInfoMap.get(portIndex);
					if (basicInfo != null) {
						OBDtoAdcPortStatus portInfo = retVal.get(basicInfo.getIntfName());
						if (portInfo != null) {
							portInfo.setErrorsIn(value);
						}
					}
				}
			}

			// LOG_LINK_PKT_DROP 에서 drop 패킷 개수 조회.
			sqlText = String.format(
					" SELECT OCCUR_TIME,                                                     \n"
							+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
							+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
							+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
							+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36                         \n"
							+ " FROM LOG_LINK_PKT_DROP                                            \n"
							+ " WHERE ADC_INDEX = %d                                                   \n"
							+ " AND OCCUR_TIME >= %s                                                   \n"
							+ " ORDER BY OCCUR_TIME DESC                                               \n"
							+ " LIMIT 1                                                                \n",
					adcIndex, OBParser.sqlString(beginTime));
			rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				for (int portIndex = 1; portIndex <= 36; portIndex++) {
					String portName = "PORT" + portIndex;
					Long value = db.getLong(rs, portName);

					OBDtoAdcPortStatus basicInfo = basicInfoMap.get(portIndex);
					if (basicInfo != null) {
						OBDtoAdcPortStatus portInfo = retVal.get(basicInfo.getIntfName());
						if (portInfo != null) {
							portInfo.setDropsIn(value);
						}
					}
				}
			}

			// LOG_LINK_BYTE_IN 에서 수신 바이트 조회.
			sqlText = String.format(
					" SELECT OCCUR_TIME,                                                     \n"
							+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
							+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
							+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
							+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36                         \n"
							+ " FROM LOG_LINK_BYTE_IN                                        \n"
							+ " WHERE ADC_INDEX = %d                                                   \n"
							+ " AND OCCUR_TIME >= %s                                                   \n"
							+ " ORDER BY OCCUR_TIME DESC                                               \n"
							+ " LIMIT 1                                                                \n",
					adcIndex, OBParser.sqlString(beginTime));
			rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				for (int portIndex = 1; portIndex <= 36; portIndex++) {
					String portName = "PORT" + portIndex;
					Long value = db.getLong(rs, portName);

					OBDtoAdcPortStatus basicInfo = basicInfoMap.get(portIndex);
					if (basicInfo != null) {
						OBDtoAdcPortStatus portInfo = retVal.get(basicInfo.getIntfName());
						if (portInfo != null) {
							portInfo.setBytesIn(value);
							if (portInfo.getBytesIn() != -1)
								portInfo.setBytesTot(portInfo.getBytesIn());
						}
					}
				}
			}

			// LOG_LINK_BYTE_OUT 에서 수신 바이트 조회.
			sqlText = String.format(
					" SELECT OCCUR_TIME,                                                     \n"
							+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
							+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
							+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
							+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36                         \n"
							+ " FROM LOG_LINK_BYTE_OUT                                       \n"
							+ " WHERE ADC_INDEX = %d                                                   \n"
							+ " AND OCCUR_TIME >= %s                                                   \n"
							+ " ORDER BY OCCUR_TIME DESC                                               \n"
							+ " LIMIT 1                                                                \n",
					adcIndex, OBParser.sqlString(beginTime));
			rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				for (int portIndex = 1; portIndex <= 36; portIndex++) {
					String portName = "PORT" + portIndex;
					Long value = db.getLong(rs, portName);

					OBDtoAdcPortStatus basicInfo = basicInfoMap.get(portIndex);
					if (basicInfo != null) {
						OBDtoAdcPortStatus portInfo = retVal.get(basicInfo.getIntfName());
						if (portInfo != null) {
							portInfo.setBytesOut(value);
							if (portInfo.getBytesOut() != -1)
								portInfo.setBytesTot(portInfo.getBytesTot() + portInfo.getBytesOut());
						}
					}
				}
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

	private HashMap<String, OBDtoAdcPortStatus> getLastPortInterfaceStatus(Integer adcIndex) throws OBException {
		HashMap<String, OBDtoAdcPortStatus> retVal = new HashMap<String, OBDtoAdcPortStatus>();

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			OBDtoADCObject object = new OBDtoADCObject();
			object.setCategory(OBDtoADCObject.CATEGORY_ADC);
			object.setIndex(adcIndex);
			HashMap<Integer, OBDtoAdcPortStatus> basicInfoMap = getBasicPortInfoMapByIndex(object);
			HashMap<String, Timestamp> lastLinkUptimeMap = getLastLinkUptime(adcIndex);

			Integer monInteval = new OBEnvManagementImpl().getAdcSyncInterval(db);// 초 단위.

			Timestamp beginTime = OBDateTime.getTimestampInterval(OBDateTime.toTimestamp(OBDateTime.now()),
					-monInteval * 2);

			// LOG_LINK_STATUS 에서 링크 상태 조회.
			sqlText = String.format(
					" SELECT OCCUR_TIME,                                                     \n"
							+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
							+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
							+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
							+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36                         \n"
							+ " FROM LOG_LINK_STATUS                                             \n"
							+ " WHERE ADC_INDEX = %d                                                   \n"
							+ " AND OCCUR_TIME >= %s                                                   \n"
							+ " ORDER BY OCCUR_TIME DESC                                               \n"
							+ " LIMIT 1                                                                \n",
					adcIndex, OBParser.sqlString(beginTime));
			ResultSet rs;
			rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				Date occurTime = db.getTimestamp(rs, "OCCUR_TIME");
				for (int portIndex = 1; portIndex <= 36; portIndex++) {
					OBDtoAdcPortStatus portInfo = new OBDtoAdcPortStatus();
					String portName = "PORT" + portIndex;
					Integer status = db.getInteger(rs, portName);

					portInfo.setLinkStatus(status);

					// down status를 상세 분석. 사용된 적이 있는 경우인가 확인한다.
					if (status.equals(OBDtoAdcPortStatus.STATUS_DOWN)) {
						if (lastLinkUptimeMap.get(portName) == null) {// 사용된적이 없는 경우.
							portInfo.setLinkStatus(OBDtoAdcPortStatus.STATUS_UNCONNECT);
						}
					}

					portInfo.setOccurTime(occurTime);

					OBDtoAdcPortStatus basicInfo = basicInfoMap.get(portIndex);
					if (basicInfo != null) {
						portInfo.setDispName(basicInfo.getDispName());
						portInfo.setIntfName(basicInfo.getIntfName());
						portInfo.setSpeed(basicInfo.getSpeed());
						portInfo.setPhyConnType(basicInfo.getPhyConnType());
						retVal.put(basicInfo.getIntfName(), portInfo);
					}
				}
			}

			// TMP_FAULT_LINK_INFO 에서 INTERFACE 이름 추출
			sqlText = String.format(" SELECT ADC_INDEX, PORT_INDEX, NAME, ALIAS_NAME                         \n"
					+ " FROM TMP_FAULT_LINK_INFO                                               \n"
					+ " WHERE ADC_INDEX = %d                                                   \n", adcIndex);
			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				Integer portIndex = db.getInteger(rs, "PORT_INDEX");
				String name = db.getString(rs, "NAME");
				// String portName = "PORT"+portIndex;
				OBDtoAdcPortStatus basicInfo = basicInfoMap.get(portIndex);
				if (basicInfo != null) {
					OBDtoAdcPortStatus portInfo = retVal.get(basicInfo.getIntfName());
					if (portInfo != null) {
						portInfo.setPortIndex(portIndex);
						portInfo.setIntfName(name);
					}
				}
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

	private HashMap<String, Timestamp> getLastLinkUptime(Integer adcIndex) throws OBException {
		HashMap<String, Timestamp> retVal = new HashMap<String, Timestamp>();

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
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
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	public HashMap<Integer, OBDtoAdcSTGStatus> getLastSTGInfo(Integer adcIndex) throws OBException {
		HashMap<Integer, OBDtoAdcSTGStatus> retVal = new HashMap<Integer, OBDtoAdcSTGStatus>();

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			// LOG_LINK_STATUS 에서 링크 상태 조회.
			sqlText = String.format(" SELECT OCCUR_TIME, GROUP_INDEX                                         \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16                         \n"
					+ " FROM LOG_L23_STG_INFO                                            \n"
					+ " WHERE INDEX IN (                                                       \n"
					+ "                 SELECT INDEX FROM (                                    \n"
					+ "                                    SELECT INDEX, GROUP_INDEX, MAX(OCCUR_TIME) \n"
					+ "                                    FROM LOG_L23_STG_INFO                \n"
					+ "                                    WHERE ADC_INDEX = %d                       \n"
					+ "                                    GROUP BY GROUP_INDEX, INDEX                \n"
					+ "                                    )                                          \n"
					+ "                )                                                       \n", adcIndex);
			ResultSet rs;
			rs = db.executeQuery(sqlText);
			while (rs.next()) {
				Date occurTime = db.getTimestamp(rs, "OCCUR_TIME");
				Integer groupIndex = db.getInteger(rs, "GROUP_INDEX");
				OBDtoAdcSTGStatus stgInfo = new OBDtoAdcSTGStatus();
				stgInfo.setGroupIndex(groupIndex);
				stgInfo.setOccurTime(occurTime);
				stgInfo.setPort1Status(db.getInteger(rs, "PORT1"));
				stgInfo.setPort2Status(db.getInteger(rs, "PORT2"));
				stgInfo.setPort3Status(db.getInteger(rs, "PORT3"));
				stgInfo.setPort4Status(db.getInteger(rs, "PORT4"));
				stgInfo.setPort5Status(db.getInteger(rs, "PORT5"));
				stgInfo.setPort6Status(db.getInteger(rs, "PORT6"));
				stgInfo.setPort7Status(db.getInteger(rs, "PORT7"));
				stgInfo.setPort8Status(db.getInteger(rs, "PORT8"));
				stgInfo.setPort9Status(db.getInteger(rs, "PORT9"));
				stgInfo.setPort10Status(db.getInteger(rs, "PORT10"));
				stgInfo.setPort11Status(db.getInteger(rs, "PORT11"));
				stgInfo.setPort12Status(db.getInteger(rs, "PORT12"));
				stgInfo.setPort13Status(db.getInteger(rs, "PORT13"));
				stgInfo.setPort14Status(db.getInteger(rs, "PORT14"));
				stgInfo.setPort15Status(db.getInteger(rs, "PORT15"));
				stgInfo.setPort16Status(db.getInteger(rs, "PORT16"));
				stgInfo.setPort17Status(db.getInteger(rs, "PORT17"));
				stgInfo.setPort18Status(db.getInteger(rs, "PORT18"));
				stgInfo.setPort19Status(db.getInteger(rs, "PORT19"));
				stgInfo.setPort20Status(db.getInteger(rs, "PORT20"));
				stgInfo.setPort21Status(db.getInteger(rs, "PORT21"));
				stgInfo.setPort22Status(db.getInteger(rs, "PORT22"));
				stgInfo.setPort23Status(db.getInteger(rs, "PORT23"));
				stgInfo.setPort24Status(db.getInteger(rs, "PORT24"));
				stgInfo.setPort25Status(db.getInteger(rs, "PORT25"));
				stgInfo.setPort26Status(db.getInteger(rs, "PORT26"));
				stgInfo.setPort27Status(db.getInteger(rs, "PORT27"));
				stgInfo.setPort28Status(db.getInteger(rs, "PORT28"));
				stgInfo.setPort29Status(db.getInteger(rs, "PORT29"));
				stgInfo.setPort30Status(db.getInteger(rs, "PORT30"));

				retVal.put(groupIndex, stgInfo);
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

	/**
	 * 최근에 입력된 데이터를 제공한다.
	 * 
	 * @param object
	 * @param db
	 * @return
	 * @throws OBException
	 */
	public ArrayList<Integer> getCpuLastStatus(OBDtoADCObject object, Integer monInteval) throws OBException {// 총 16개.
																												// 데이터가
																												// 없으면
																												// -1설정.
		ArrayList<Integer> retVal = new ArrayList<Integer>();

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (monInteval == null)
				monInteval = new OBEnvManagementImpl().getAdcSyncInterval();// 초 단위.

			Timestamp beginTime = OBDateTime.getTimestampInterval(OBDateTime.toTimestamp(OBDateTime.now()),
					-monInteval * 2);

			Integer cpuThreshold = this.cpuMaxUsage;

			String propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
					OBDefine.PROPERTY_KEY_SYSTEM_CPU_MAX_USAGE);
			if (propertyValue != null && !propertyValue.isEmpty())
				cpuThreshold = Integer.parseInt(propertyValue);

			sqlText = String.format(
					" SELECT OCCUR_TIME,                                                           \n"
							+ " CPU1_USAGE, CPU2_USAGE, CPU3_USAGE, CPU4_USAGE, CPU5_USAGE,                      \n"
							+ " CPU6_USAGE, CPU7_USAGE, CPU8_USAGE, CPU9_USAGE, CPU10_USAGE,                     \n"
							+ " CPU11_USAGE, CPU12_USAGE, CPU13_USAGE, CPU14_USAGE, CPU15_USAGE, CPU16_USAGE,    \n"
							+ " CPU17_USAGE, CPU18_USAGE, CPU19_USAGE, CPU20_USAGE, CPU21_USAGE, CPU22_USAGE,    \n"
							+ " CPU23_USAGE, CPU24_USAGE, CPU25_USAGE, CPU26_USAGE, CPU27_USAGE, CPU28_USAGE,    \n"
							+ " CPU29_USAGE, CPU30_USAGE, CPU31_USAGE, CPU32_USAGE                               \n"
							+ " FROM LOG_RESC_CPUMEM        				                                     \n"
							+ " WHERE ADC_INDEX = %d                                                             \n"
							+ " AND OCCUR_TIME >= %s                                                             \n"
							+ " ORDER BY OCCUR_TIME DESC  LIMIT 1                                                \n",
					object.getIndex(), OBParser.sqlString(beginTime));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				Integer value;
				value = db.getInteger(rs, "CPU1_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);

				value = db.getInteger(rs, "CPU2_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU3_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU4_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU5_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU6_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU7_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU8_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU9_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU10_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU11_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU12_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU13_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU14_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU15_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU16_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU17_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU18_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU19_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU20_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU21_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU22_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU23_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU24_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU25_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU26_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU27_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU28_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU29_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU30_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU31_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
				value = db.getInteger(rs, "CPU32_USAGE");
				if (value == -1)
					retVal.add(-1);
				else if (value > cpuThreshold)
					retVal.add(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.add(OBDtoFaultHWStatus.STATUS_NORMAL);
			} else {
				// -1로 16개 추가해서 전달.
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
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
		return retVal;
	}

	public ArrayList<Integer> getPowerSupplyStatus(OBDtoADCObject object, Integer monInteval) throws OBException {//
		ArrayList<Integer> retVal = new ArrayList<Integer>();

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (monInteval == null)
				monInteval = new OBEnvManagementImpl().getAdcSyncInterval(db);// 초 단위.

			Timestamp beginTime = OBDateTime.getTimestampInterval(OBDateTime.toTimestamp(OBDateTime.now()),
					-monInteval * 2);

			sqlText = String.format(
					" SELECT OCCUR_TIME,                                \n"
							+ " PS1_STATUS, PS2_STATUS, PS3_STATUS, PS4_STATUS    \n"
							+ " FROM LOG_POWERSUPPLY_STATUS        		    \n"
							+ " WHERE ADC_INDEX = %d                              \n"
							+ " AND OCCUR_TIME >= %s                              \n"
							+ " ORDER BY OCCUR_TIME DESC  LIMIT 1                 \n",
					object.getIndex(), OBParser.sqlString(beginTime));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal.add(db.getInteger(rs, "PS1_STATUS"));
				retVal.add(db.getInteger(rs, "PS2_STATUS"));
				retVal.add(db.getInteger(rs, "PS3_STATUS"));
				retVal.add(db.getInteger(rs, "PS4_STATUS"));
			} else {
				// -1로 4개 추가해서 전달.
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
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
		return retVal;
	}

	public ArrayList<Integer> getFanLastStatus(OBDtoADCObject object, Integer monInteval) throws OBException {//
		ArrayList<Integer> retVal = new ArrayList<Integer>();

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (monInteval == null)
				monInteval = new OBEnvManagementImpl().getAdcSyncInterval(db);// 초 단위.

			Timestamp beginTime = OBDateTime.getTimestampInterval(OBDateTime.toTimestamp(OBDateTime.now()),
					-monInteval * 2);

			sqlText = String.format(
					" SELECT OCCUR_TIME,                                                           \n"
							+ " FAN1_STATUS, FAN2_STATUS, FAN3_STATUS, FAN4_STATUS                           \n"
							+ " FROM LOG_FAN_STATUS        				                               \n"
							+ " WHERE ADC_INDEX = %d                                                         \n"
							+ " AND OCCUR_TIME >= %s                                                         \n"
							+ " ORDER BY OCCUR_TIME DESC  LIMIT 1                                            \n",
					object.getIndex(), OBParser.sqlString(beginTime));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal.add(db.getInteger(rs, "FAN1_STATUS"));
				retVal.add(db.getInteger(rs, "FAN2_STATUS"));
				retVal.add(db.getInteger(rs, "FAN3_STATUS"));
				retVal.add(db.getInteger(rs, "FAN4_STATUS"));
			} else {
				// -1로 4개 추가해서 전달.
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
				retVal.add(-1);
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
		return retVal;
	}

	private HashMap<Integer, OBDtoAdcPortStatus> getBasicPortInfoMapByIndex(OBDtoADCObject object) throws OBException {
		HashMap<Integer, OBDtoAdcPortStatus> retVal = new HashMap<Integer, OBDtoAdcPortStatus>();

		String sqlText = "";

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT PORT_INDEX, NAME, ALIAS_NAME, PHY_CONNECTOR_TYPE, SPEED               \n"
							+ " FROM TMP_FAULT_LINK_INFO        				                               \n"
							+ " WHERE ADC_INDEX = %d                                                         \n",
					object.getIndex());

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcPortStatus obj = new OBDtoAdcPortStatus();
				Integer index = db.getInteger(rs, "PORT_INDEX");
				obj.setIntfName(db.getString(rs, "NAME"));
				obj.setDispName(db.getString(rs, "ALIAS_NAME"));
				obj.setPhyConnType(db.getInteger(rs, "PHY_CONNECTOR_TYPE"));
				obj.setSpeed(db.getInteger(rs, "SPEED"));

				retVal.put(index, obj);
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

	private ArrayList<OBDtoAdcPortStatus> getPortInfoLastStatus(OBDtoADCObject object, Integer monInteval)
			throws OBException {// 총 16개. 데이터가 없으면 -1설정.
		ArrayList<OBDtoAdcPortStatus> retVal = new ArrayList<OBDtoAdcPortStatus>();

		try {
			HashMap<String, OBDtoAdcPortStatus> portInfoMap = getLastPortInterfaceStatus(object.getIndex());
			OBDtoAdcPortStatus[] portInfoArray = new OBDtoAdcPortStatus[portInfoMap.size()];
			ArrayList<OBDtoAdcPortStatus> portInfoList = new ArrayList<OBDtoAdcPortStatus>(portInfoMap.values());
			for (OBDtoAdcPortStatus info : portInfoList) {
				portInfoArray[info.getPortIndex() - 1] = info;
			}
			Collections.addAll(retVal, portInfoArray);
			for (int i = portInfoList.size(); i < MAX_PORT_COUNT; i++) {
				// 빈 껍데기 추가함.
				retVal.add(new OBDtoAdcPortStatus());
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public ArrayList<OBDtoFaultVlanInfo> getVlanInfoLastStatus(OBDtoADCObject object, Integer monInteval)
			throws OBException {
		ArrayList<OBDtoFaultVlanInfo> retVal = new ArrayList<OBDtoFaultVlanInfo>();

		return retVal;
	}

	private final static Integer MAX_PORT_COUNT = 36;

	private void writePortInterfaceLogBytes(Integer adcIndex, Timestamp occurTime, ArrayList<OBDtoMonL2Ports> list)
			throws OBException {
		String sqlText = "";
		String headText = "";
		String valueText = "";
		int listSize = list.size();

		if (listSize == 0)
			return;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			// LOG_LINK_BYTE_IN
			headText = String
					.format(" INSERT INTO LOG_LINK_BYTE_IN                                                   \n"
							+ " (ADC_INDEX, OCCUR_TIME,                                                         \n"
							+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10,          \n"
							+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
							+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
							+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                                 \n"
							+ " VALUES ");
			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", list.get(i).getBytesIn());
			}
			sqlText = String.format("%s ( %d, %s, %s )", headText, adcIndex, OBParser.sqlString(occurTime), valueText);

			db.executeUpdate(sqlText);

			// LOG_LINK_BYTE_OUT
			headText = String.format(" INSERT INTO LOG_LINK_BYTE_OUT                               \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
					+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
					+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                                 \n"
					+ " VALUES ");
			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", list.get(i).getBytesOut());
			}
			sqlText = String.format("%s ( %d, %s, %s )", headText, adcIndex, OBParser.sqlString(occurTime), valueText);

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

	private long getLastestPortTotalDataFromDb(String tableName, Integer adcIndex, OBDatabase db)
			throws OBException, SQLException {
		StringBuffer sqlText = new StringBuffer();
//    	OBDatabase db = new OBDatabase();

		sqlText.append("select total_data").append(" from ").append(tableName).append(" where adc_index = ")
				.append(adcIndex).append(" order by occur_time desc").append(" limit 1");

		ResultSet rs = db.executeQuery(sqlText.toString());
		if (rs != null) {
			if (rs.next() == true) {
				Long result = db.getLong(rs, "total_data");
				if (result != null) {
					return result;
				}
			}
		}
		return 0;

	}

	private long getErrPortTotalData(ArrayList<OBDtoMonL2Ports> list) {
		long totalValue = 0;
		long inData = 0;
		long outData = 0;

		for (OBDtoMonL2Ports port : list) {
			inData = port.getErrorsIn();
			outData = port.getErrorsOut();
			if (inData > 0) {
				totalValue += inData;
			}
			if (outData > 0) {
				totalValue += outData;
			}
		}
		return totalValue;
	}

	private long getDropPortTotalData(ArrayList<OBDtoMonL2Ports> list) {
		long totalValue = 0;
		long inData = 0;
		long outData = 0;

		for (OBDtoMonL2Ports port : list) {
			inData = port.getDropsIn();
			outData = port.getDropsOut();
			if (inData > 0) {
				totalValue += inData;
			}
			if (outData > 0) {
				totalValue += outData;
			}
		}
		return totalValue;
	}

	private void writePortInterfaceLogPkts(Integer adcIndex, Timestamp occurTime, ArrayList<OBDtoMonL2Ports> list)
			throws OBException {
		String sqlText = "";
		String headText = "";
		String valueText = "";
		long lastestTotalData = 0;
		long currentTotalData = 0;

		int listSize = list.size();

		if (listSize == 0)
			return;

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			// ------------------------------------------------------------------------------
			// LOG_LINK_PKT_DROP
			// ------------------------------------------------------------------------------
			headText = String.format(" INSERT INTO LOG_LINK_PKT_DROP                                   \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
					+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
					+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                                 \n"
					+ " VALUES ");
			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", list.get(i).getDropsIn() + list.get(i).getDropsOut());
			}

			sqlText = String.format("%s ( %d, %s, %s)", headText, adcIndex, OBParser.sqlString(occurTime), valueText);

			db.executeUpdate(sqlText);
			// ------------------------------------------------------------------------------
			// LOG_LINK_PKT_ERR_CHART
			// ------------------------------------------------------------------------------
			headText = String.format(" INSERT INTO LOG_LINK_PKT_DROP_CHART                                    \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " TOTAL_DATA, CHART_DATA)                                 \n" + " VALUES ");

			lastestTotalData = getLastestPortTotalDataFromDb("LOG_LINK_PKT_DROP_CHART", adcIndex, db);
			currentTotalData = getDropPortTotalData(list);
			long chartData = Math.abs(currentTotalData - lastestTotalData);

			valueText = String.format("%d,%d", currentTotalData, chartData);

			sqlText = String.format("%s ( %d, %s, %s)", headText, adcIndex, OBParser.sqlString(occurTime), valueText);

			db.executeUpdate(sqlText);

			// ------------------------------------------------------------------------------
			// LOG_LINK_PKT_ERR
			// ------------------------------------------------------------------------------
			headText = String.format(" INSERT INTO LOG_LINK_PKT_ERR                                    \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
					+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
					+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                                 \n"
					+ " VALUES ");
			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", (list.get(i).getErrorsIn() + list.get(i).getErrorsOut()));
			}

			sqlText = String.format("%s ( %d, %s, %s)", headText, adcIndex, OBParser.sqlString(occurTime), valueText);

			db.executeUpdate(sqlText);

			// ------------------------------------------------------------------------------
			// LOG_LINK_PKT_ERR_CHART
			// ------------------------------------------------------------------------------
			headText = String.format(" INSERT INTO LOG_LINK_PKT_ERR_CHART                                    \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " TOTAL_DATA, CHART_DATA)                                 \n" + " VALUES ");

			lastestTotalData = getLastestPortTotalDataFromDb("LOG_LINK_PKT_ERR_CHART", adcIndex, db);
			currentTotalData = getErrPortTotalData(list);
			chartData = Math.abs(currentTotalData - lastestTotalData);
			valueText = String.format("%d,%d", currentTotalData, chartData);

			sqlText = String.format("%s ( %d, %s, %s)", headText, adcIndex, OBParser.sqlString(occurTime), valueText);

			db.executeUpdate(sqlText);

			// ------------------------------------------------------------------------------
			// LOG_LINK_PKT_IN
			// ------------------------------------------------------------------------------
			headText = String.format(" INSERT INTO LOG_LINK_PKT_IN                                 \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
					+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
					+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                                 \n"
					+ " VALUES ");
			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", list.get(i).getPktsIn());
			}
			sqlText = String.format("%s ( %d, %s, %s )", headText, adcIndex, OBParser.sqlString(occurTime), valueText);

			db.executeUpdate(sqlText);

			// ------------------------------------------------------------------------------
			// LOG_LINK_PKT_OUT
			// ------------------------------------------------------------------------------
			headText = String.format(" INSERT INTO LOG_LINK_PKT_OUT                                 \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
					+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
					+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                                 \n"
					+ " VALUES ");
			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", list.get(i).getPktsOut());
			}
			sqlText = String.format("%s ( %d, %s, %s )", headText, adcIndex, OBParser.sqlString(occurTime), valueText);

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

	private void writePortInterfaceLogStatus(Integer adcIndex, Timestamp occurTime, ArrayList<OBDtoMonL2Ports> list)
			throws OBException {
		String sqlText = "";
		String headText = "";
		String valueText = "";
		int listSize = list.size();

		if (listSize == 0)
			return;

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();
			// LOG_LINK_STATUS
			headText = String.format(" INSERT INTO LOG_LINK_STATUS                                     \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
					+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
					+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                                 \n"
					+ " VALUES ");
			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", list.get(i).getStatus());
			}
			sqlText = String.format("%s ( %d, %s, %s )", headText, adcIndex, OBParser.sqlString(occurTime), valueText);

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

	// 가장 최근에 사용된 포트 정보를 제공하기 위해 uplink 정보만 저장한다.
	private void updateLinkupTime(Integer adcIndex, Timestamp occurTime, ArrayList<OBDtoMonL2Ports> list)
			throws OBException {
		String sqlText = "";
		String headText = "";
		String valueText = "";
		int listSize = list.size();

		if (listSize == 0)
			return;

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();
			// update or insert?

			sqlText = String.format(
					" SELECT * FROM LOG_LINK_UPTIME_STATUS \n" + " WHERE ADC_INDEX = %d  LIMIT 1        \n", adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {// update
				for (int i = 0; i < MAX_PORT_COUNT; i++) {
					if (i >= listSize)
						break;
					if (list.get(i).getStatus() == OBDtoMonL2Ports.STATUS_UP) {
						if (!headText.isEmpty())
							headText += ", ";
						headText += String.format(" PORT%d = %s ", i + 1, OBParser.sqlString(occurTime));
					}
				}

				if (valueText.isEmpty())
					return;

				sqlText = String.format(" UPDATE LOG_LINK_UPTIME_STATUS        \n"
						+ " SET %s                               \n" + " WHERE ADC_INDEX=%d ;                 \n",
						valueText, adcIndex);

				db.executeUpdate(sqlText);
			} else {// insert
				for (int i = 0; i < MAX_PORT_COUNT; i++) {
					if (i >= listSize)
						break;
					if (list.get(i).getStatus() == OBDtoMonL2Ports.STATUS_UP) {
						if (!headText.isEmpty())
							headText += ", ";
						headText += String.format(" PORT%d ", i + 1);
						if (!valueText.isEmpty())
							valueText += ",";
						valueText += String.format(" %s ", OBParser.sqlString(occurTime));
					}
				}
				if (valueText.isEmpty())
					return;

				sqlText = String.format(
						" INSERT INTO LOG_LINK_UPTIME_STATUS                           \n"
								+ " (ADC_INDEX, OCCUR_TIME, %s )                                 \n"
								+ " VALUES                                                       \n"
								+ " ( %d, %s, %s )                                               \n",
						headText, adcIndex, OBParser.sqlString(occurTime), valueText);
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

	private void writePortInterfaceLogMode(Integer adcIndex, Timestamp occurTime, ArrayList<OBDtoMonL2Ports> list)
			throws OBException {
		String sqlText = "";
		String headText = "";
		String valueText = "";
		int listSize = list.size();

		if (listSize == 0)
			return;

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			// LOG_LINK_MODE
			headText = String.format(" INSERT INTO LOG_LINK_MODE                                       \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
					+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
					+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                                 \n"
					+ " VALUES ");
			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", list.get(i).getDuplex());
			}
			sqlText = String.format("%s ( %d, %s, %s )", headText, adcIndex, OBParser.sqlString(occurTime), valueText);

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

	private void writePortInterfaceLogConnType(Integer adcIndex, Timestamp occurTime, ArrayList<OBDtoMonL2Ports> list)
			throws OBException {
		String sqlText = "";
		String headText = "";
		String valueText = "";
		int listSize = list.size();

		if (listSize == 0)
			return;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			// LOG_LINK_CONNECTOR_TYPE
			headText = String.format(" INSERT INTO LOG_LINK_CONNECTOR_TYPE                             \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
					+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
					+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                                 \n"
					+ " VALUES ");
			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", list.get(i).getConnType());
			}
			sqlText = String.format("%s ( %d, %s, %s )", headText, adcIndex, OBParser.sqlString(occurTime), valueText);

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

	private void writePortInterfaceLogTmpInfo(Integer adcIndex, Timestamp occurTime, ArrayList<OBDtoMonL2Ports> list)
			throws OBException {
		final int listSize = list.size();

		if (list.size() == 0)
			return;

		StringBuilder query = new StringBuilder();
		StringBuilder valuesQuery = new StringBuilder();

		String prefix = "";
		for (int i = 0; i < listSize; i++) {
			OBDtoMonL2Ports obj = list.get(i);

			valuesQuery.append(prefix).append("(").append(adcIndex).append(",").append(i + 1).append(",")
					.append(OBParser.sqlString(obj.getPortName())).append(",")
					.append(OBParser.sqlString(obj.getAliasName())).append(",").append(obj.getConnType()).append(",")
					.append(obj.getSpeed()).append(",").append(obj.getDuplex()).append(")");

			prefix = ", ";
		}

		// 테이블에 저장된 데이터를 삭제.
		query.append(" DELETE FROM TMP_FAULT_LINK_INFO WHERE ADC_INDEX = ").append(adcIndex).append(";");

		query.append(" INSERT INTO TMP_FAULT_LINK_INFO ")
				.append(" (ADC_INDEX, PORT_INDEX, NAME, ALIAS_NAME, PHY_CONNECTOR_TYPE, SPEED, DUPLEX) ")
				.append(" VALUES ").append(valuesQuery.toString()).append(";");

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			db.executeUpdate(query.toString());
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sqlText:" + query.toString());
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private HashMap<Integer, OBDtoMonL2Ports> getLastPortInterfaceLogBpsPps(Integer adcIndex) throws OBException {
		HashMap<Integer, OBDtoMonL2Ports> result = new HashMap<Integer, OBDtoMonL2Ports>();
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		try {

			db.openDB();

			// 위 comment와 동치 query로 수정, EXISTS가 뽑는 데이터는 중요하지 않다.

			sqlText = String.format(" SELECT OCCUR_TIME, PORT_INDEX,                                    \n"
					+ " PKTS_IN, PKTS_OUT, BYTES_IN, BYTES_OUT,                           \n"
					+ " ERRORS_IN, ERRORS_OUT, DROPS_IN, DROPS_OUT                        \n"
					+ " FROM TMP_FAULT_LINK_STATS                                         \n"
					+ " WHERE ADC_INDEX=%d                                                \n", adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoMonL2Ports log = new OBDtoMonL2Ports();

				Integer index = db.getInteger(rs, "PORT_INDEX");
				log.setTime(db.getTimestamp(rs, "OCCUR_TIME"));
				log.setPktsIn(db.getLong(rs, "PKTS_IN"));
				log.setBytesIn(db.getLong(rs, "BYTES_IN"));
				log.setPktsOut(db.getLong(rs, "PKTS_OUT"));
				log.setBytesOut(db.getLong(rs, "BYTES_OUT"));
				log.setErrorsIn(db.getLong(rs, "ERRORS_IN"));
				log.setErrorsOut(db.getLong(rs, "ERRORS_OUT"));
				log.setDropsIn(db.getLong(rs, "DROPS_IN"));
				log.setDropsOut(db.getLong(rs, "DROPS_OUT"));
				result.put(index, log);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			result = null;
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
					String.format("adcIndex:%d. message:%s", adcIndex, e.getMessage()));
		} finally {
			if (db != null)
				db.closeDB();
		}
		return result;
	}

	private OBDtoBpsPpsEpsDps calcBpsPpsPortInterface(Timestamp nowTime, OBDtoMonL2Ports old, OBDtoMonL2Ports now,
			int adcType) {
		OBDtoBpsPpsEpsDps retVal = new OBDtoBpsPpsEpsDps();

		if (old == null)
			return retVal;

		if (old.getBytesIn() == 0 && old.getPktsIn() == 0)
			return retVal;// 초기화 상태로 간주.

		long value;
		long diffValue;
		long diffTime = Math.abs(nowTime.getTime() - old.getTime().getTime());// millisec

		diffValue = now.getBytesIn() - old.getBytesIn();
		if (now.getBytesIn() == -1 || old.getBytesIn() == -1 || diffValue < 0) // 음수면, limit을 초과해서 순환했거나, 장비 재부팅 등.. 예외
																				// 상황. 값 무시처리
		{
			value = -1;
		} else {
			value = (long) (((float) diffValue) * 8 * 1000 / diffTime);
		}
		retVal.setBpsIn(value);

		diffValue = now.getBytesOut() - old.getBytesOut();
		if (now.getBytesOut() == -1 || old.getBytesOut() == -1 || diffValue < 0) {
			value = -1;
		} else {
			value = (long) (((float) diffValue) * 8 * 1000 / diffTime);
		}
		retVal.setBpsOut(value);

		diffValue = now.getPktsIn() - old.getPktsIn();
		if (now.getPktsIn() == -1 || old.getPktsIn() == -1 || diffValue < 0) {
			value = -1;
		} else {
			value = (long) (((float) diffValue) * 1000 / diffTime);
		}
		retVal.setPpsIn(value);

		diffValue = now.getPktsOut() - old.getPktsOut();
		if (now.getPktsOut() == -1 || old.getPktsOut() == -1 || diffValue < 0) {
			value = -1;
		} else {
			value = (long) (((float) diffValue) * 1000 / diffTime);
		}

		retVal.setPpsOut(value);

		diffValue = now.getErrorsIn() - old.getErrorsIn();
		if (now.getErrorsIn() == -1 || old.getErrorsIn() == -1 || diffValue < 0) {
			value = -1;
		} else {
			value = diffValue;
		}
		retVal.setEpsIn(value);

		diffValue = now.getErrorsOut() - old.getErrorsOut();
		if (now.getErrorsOut() == -1 || old.getErrorsOut() == -1 || diffValue < 0) {
			value = -1;
		} else {
			value = diffValue;
		}
		retVal.setEpsOut(value);

		diffValue = now.getDropsIn() - old.getDropsIn();
		if (now.getDropsIn() == -1 || old.getDropsIn() == -1 || diffValue < 0) {
			value = -1;
		} else {
			value = diffValue;
		}
		retVal.setDpsIn(value);

		diffValue = now.getDropsOut() - old.getDropsOut();
		if (now.getDropsOut() == -1 || old.getDropsOut() == -1 || diffValue < 0) {
			value = -1;
		} else {
			value = diffValue;
		}
		retVal.setDpsOut(value);
		return retVal;
	}

	private HashMap<Integer, OBDtoBpsPpsEpsDps> writePortInterfaceLogBpsPps(Integer adcIndex, int adcType,
			String swVersion, Timestamp occurTime, ArrayList<OBDtoMonL2Ports> list) throws OBException {
		String sqlText = "";
		String headText = "";
		String valueText = "";
		int listSize = list.size();

		HashMap<Integer, OBDtoBpsPpsEpsDps> bpsMap = new HashMap<Integer, OBDtoBpsPpsEpsDps>();

		// list(=포트별 트래픽)은 OBFaultMonitoringImpl().getL2PortsInfo()에서 구하는데, null을 리턴하지
		// 않으므로, 아래와 같이 size만 체크해서 없으면 처리를 skip한다.
		if (listSize == 0)
			return bpsMap;

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			// hash key: 1부터 시작하는 interface index
			HashMap<Integer, OBDtoMonL2Ports> portMap = getLastPortInterfaceLogBpsPps(adcIndex);

			// 각 포트별로 bps, pps, eps, dps를 계산.
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				OBDtoBpsPpsEpsDps obj = new OBDtoBpsPpsEpsDps();
				if (i < listSize) {
					OBDtoMonL2Ports oldInfo = portMap.get(i + 1);
					obj = calcBpsPpsPortInterface(occurTime, oldInfo, list.get(i), adcType);

					// 계산 다 했지만, Alteon v26은 bps, pps를 보정한다.
					if (adcType == OBDefine.ADC_TYPE_ALTEON && swVersion != null && swVersion.startsWith("26.")) // 26
																													// 버전
																													// 처리
					{
						obj.setBpsIn(list.get(i).getBytesInPerSec() * 8); // bit 변환
						obj.setBpsOut(list.get(i).getBytesOutPerSec() * 8); // bit 변환
					}
				}
				bpsMap.put(i + 1, obj);
			}

			// LOG_LINK_BPS_IN
			headText = String.format(" INSERT INTO LOG_LINK_BPS_IN                                 \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10,          \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
					+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
					+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                                 \n"
					+ " VALUES ");
			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", bpsMap.get(i + 1).getBpsIn());
			}
			sqlText = String.format("%s ( %d, %s, %s )", headText, adcIndex, OBParser.sqlString(occurTime), valueText);
			db.executeUpdate(sqlText);

			// LOG_LINK_BPS_OUT
			headText = String.format(" INSERT INTO LOG_LINK_BPS_OUT                                \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10,          \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
					+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
					+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                                 \n"
					+ " VALUES ");
			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", bpsMap.get(i + 1).getBpsOut());
			}
			sqlText = String.format("%s ( %d, %s, %s )", headText, adcIndex, OBParser.sqlString(occurTime), valueText);
			db.executeUpdate(sqlText);

			// LOG_LINK_PPS_IN
			headText = String.format(" INSERT INTO LOG_LINK_PPS_IN                                 \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10,          \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
					+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
					+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                                 \n"
					+ " VALUES ");
			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", bpsMap.get(i + 1).getPpsIn());
			}
			sqlText = String.format("%s ( %d, %s, %s )", headText, adcIndex, OBParser.sqlString(occurTime), valueText);
			db.executeUpdate(sqlText);

			// LOG_LINK_PPS_OUT
			headText = String.format(" INSERT INTO LOG_LINK_PPS_OUT                                \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
					+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
					+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                                 \n"
					+ " VALUES ");
			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", bpsMap.get(i + 1).getPpsOut());
			}
			sqlText = String.format("%s ( %d, %s, %s )", headText, adcIndex, OBParser.sqlString(occurTime), valueText);
			db.executeUpdate(sqlText);

			// LOG_LINK_EPS_OUT
			headText = String.format(" INSERT INTO LOG_LINK_EPS_OUT                                \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10,          \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
					+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
					+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                                 \n"
					+ " VALUES ");
			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", bpsMap.get(i + 1).getEpsOut());
			}
			sqlText = String.format("%s ( %d, %s, %s )", headText, adcIndex, OBParser.sqlString(occurTime), valueText);
			db.executeUpdate(sqlText);

			// LOG_LINK_EPS_IN
			headText = String.format(" INSERT INTO LOG_LINK_EPS_IN                                \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10,          \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
					+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
					+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                                 \n"
					+ " VALUES ");
			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", bpsMap.get(i + 1).getEpsIn());
			}
			sqlText = String.format("%s ( %d, %s, %s )", headText, adcIndex, OBParser.sqlString(occurTime), valueText);
			db.executeUpdate(sqlText);

			// LOG_LINK_DPS_IN
			headText = String.format(" INSERT INTO LOG_LINK_DPS_IN                                           \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10,          \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
					+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
					+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                                 \n"
					+ " VALUES ");
			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", bpsMap.get(i + 1).getDpsIn());
			}
			sqlText = String.format("%s ( %d, %s, %s )", headText, adcIndex, OBParser.sqlString(occurTime), valueText);
			db.executeUpdate(sqlText);

			// LOG_LINK_DPS_OUT
			headText = String.format(" INSERT INTO LOG_LINK_DPS_OUT                                \n"
					+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
					+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10,          \n"
					+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
					+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
					+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                                 \n"
					+ " VALUES ");
			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", bpsMap.get(i + 1).getDpsOut());
			}
			sqlText = String.format("%s ( %d, %s, %s )", headText, adcIndex, OBParser.sqlString(occurTime), valueText);
			db.executeUpdate(sqlText);

			return bpsMap;
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

	private void writePortInterfaceLogTmpStats(Integer adcIndex, Timestamp occurTime, ArrayList<OBDtoMonL2Ports> list,
			HashMap<Integer, OBDtoBpsPpsEpsDps> bpsMap) throws OBException {
		String sqlText = "";
		String valueText = "";
		int listSize = list.size();

		if (listSize == 0)
			return;

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			// 테이블에 저장된 데이터를 삭제.
			String sqlTextLatestDelete = String.format(" DELETE FROM TMP_FAULT_LINK_STATS WHERE ADC_INDEX = %d;",
					adcIndex);

			valueText = "";
			for (int i = 0; i < MAX_PORT_COUNT; i++) {

				if (!valueText.isEmpty())
					valueText += ", ";

				if (i >= listSize) {
					valueText += String.format(
							"( %s, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d )",
							OBParser.sqlString(occurTime), adcIndex, i + 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
							-1, -1, -1, -1, -1, -1);
				} else {
					OBDtoMonL2Ports obj = list.get(i);
					OBDtoBpsPpsEpsDps bpsObj = bpsMap.get(i + 1); // key는 1부터 시작하는 index.
					valueText += String.format(
							"( %s, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d )",
							OBParser.sqlString(occurTime), adcIndex, i + 1, obj.getStatus(), obj.getPktsIn(),
							obj.getPktsOut(), obj.getBytesIn(), obj.getBytesOut(), obj.getErrorsIn(),
							obj.getErrorsOut(), obj.getDropsIn(), obj.getDropsOut(), bpsObj.getPpsIn(),
							bpsObj.getPpsOut(), bpsObj.getBpsIn(), bpsObj.getBpsOut(), bpsObj.getEpsIn(),
							bpsObj.getEpsOut(), bpsObj.getDpsIn(), bpsObj.getDpsOut());
				}
			}

			sqlText = String.format(
					" INSERT INTO TMP_FAULT_LINK_STATS                                                             \n"
							+ " (OCCUR_TIME, ADC_INDEX, PORT_INDEX,                                                          \n"
							+ " STATUS, PKTS_IN, PKTS_OUT, BYTES_IN, BYTES_OUT, ERRORS_IN, ERRORS_OUT, DROPS_IN, DROPS_OUT,  \n"
							+ " PPS_IN, PPS_OUT, BPS_IN, BPS_OUT, EPS_IN, EPS_OUT, DPS_IN, DPS_OUT)                          \n"
							+ " VALUES                                                                                       \n"
							+ " %s                                                                                           \n",
					valueText);

			db.executeUpdate(sqlTextLatestDelete + sqlText);
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

	public void writePortInterfaceLog(Integer adcIndex, int adcType, String swVersion, Timestamp occurTime,
			ArrayList<OBDtoMonL2Ports> list) throws OBException {
		if (list.size() == 0)
			return;

		try {
			writePortInterfaceLogTmpInfo(adcIndex, occurTime, list);
			writePortInterfaceLogConnType(adcIndex, occurTime, list);

			HashMap<Integer, OBDtoBpsPpsEpsDps> bpsMap = writePortInterfaceLogBpsPps(adcIndex, adcType, swVersion,
					occurTime, list);// 자리 이동 금지. 제일 먼저 작업해야 함.
			writePortInterfaceLogBytes(adcIndex, occurTime, list);
			writePortInterfaceLogPkts(adcIndex, occurTime, list);
			writePortInterfaceLogStatus(adcIndex, occurTime, list);
			writePortInterfaceLogMode(adcIndex, occurTime, list);
			updateLinkupTime(adcIndex, occurTime, list);
			writePortInterfaceLogTmpStats(adcIndex, occurTime, list, bpsMap);
		} catch (OBException e) {
			throw e;
		}
	}

	public void writeVSvcRespTime(Integer adcIndex, Integer adcType, Timestamp occurTime,
			OBDtoVSvcRespTimeInfo respTimeInfo) throws OBException {
		String sqlText = "";
		String valueText = "";

		// OBDatabase db = new OBDatabase();
		// db.openDB();
		//
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			// 테이블에 저장된 데이터를 삭제.
			String sqlTextLatestDelete = String.format(
					" DELETE FROM TMP_FAULT_SVC_PERF_RESP_TIME WHERE ADC_INDEX = %d AND OBJ_INDEX = %s;", adcIndex,
					OBParser.sqlString(respTimeInfo.getVsvcIndex()));
			db.executeUpdate(sqlTextLatestDelete);

			valueText = "";
			valueText += String.format("( %s, %d, %d, %s, %d )", OBParser.sqlString(respTimeInfo.getVsvcIndex()),
					adcIndex, adcType, OBParser.sqlString(respTimeInfo.getOccurTime()), respTimeInfo.getResponseTime());

			sqlText = String.format(" INSERT INTO LOG_SVC_PERF_RESP_TIME                                      \n"
					+ " (OBJ_INDEX, ADC_INDEX, OBJ_TYPE, OCCUR_TIME, RESPONSE_TIME) \n"
					+ " VALUES                                                      \n"
					+ " %s                                                          \n", valueText);

			db.executeUpdate(sqlText);

			sqlText = String.format(" INSERT INTO TMP_FAULT_SVC_PERF_RESP_TIME                                      \n"
					+ " (OBJ_INDEX, ADC_INDEX, OBJ_TYPE, OCCUR_TIME, RESPONSE_TIME) \n"
					+ " VALUES                                                      \n"
					+ " %s                                                          \n", valueText);

			db.executeUpdate(sqlText);
			// db.closeDB();
		} catch (SQLException e) {
			// db.closeDB();
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			// db.closeDB();
			throw e;
		} catch (Exception e) {
			// db.closeDB();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void writeSectionRespTime(Integer respIndex, Timestamp occurTime, OBDtoVSvcRespTimeInfo respTimeInfo,
			Integer groupIndex) throws OBException {
		String sqlText = "";
		String valueText = "";

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			// 테이블에 저장된 데이터를 삭제.
			String sqlTextLatestDelete = String.format(" DELETE FROM TMP_RESP_SECTION_CHECK WHERE RESP_INDEX = %d;",
					respIndex, OBParser.sqlString(respTimeInfo.getVsvcIndex()));
			db.executeUpdate(sqlTextLatestDelete);

			valueText = "";
			valueText += String.format("( %d, %s, %d)", respIndex, OBParser.sqlString(respTimeInfo.getOccurTime()),
					respTimeInfo.getResponseTime());

			sqlText = String.format(" INSERT INTO LOG_RESP_SECTION_CHECK                      \n"
					+ " (RESP_INDEX, OCCUR_TIME, RESP_TIME)                         \n"
					+ " VALUES                                                      \n"
					+ " %s                                                          \n", valueText);

			db.executeUpdate(sqlText);

			sqlText = String.format(" INSERT INTO TMP_RESP_SECTION_CHECK                      \n"
					+ " (RESP_INDEX, OCCUR_TIME, RESP_TIME)                         \n"
					+ " VALUES                                                      \n"
					+ " %s                                                          \n", valueText);

			db.executeUpdate(sqlText);

			sqlText = String.format(
					" UPDATE MNG_RESP_SECTION_GROUP                       \n"
							+ " SET LAST_TIME = %s                                      \n"
							+ " WHERE INDEX = %d                                        \n",
					OBParser.sqlString(respTimeInfo.getOccurTime()), groupIndex);

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

	private HashMap<String, OBDtoMonTrafficVSvcInfo> getSvcPertInfoMap(Integer adcIndex, Integer adcType)
			throws OBException {
		HashMap<String, OBDtoMonTrafficVSvcInfo> retMap = new HashMap<String, OBDtoMonTrafficVSvcInfo>();

		String sqlText = "";

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			if (adcType == OBDefine.ADC_TYPE_ALTEON) {
				sqlText = String.format(
						" SELECT A.VS_INDEX AS VS_INDEX, A.INDEX AS VSVC_INDEX, A.VIRTUAL_PORT AS SVC_PORT,                                \n"
								+ " B.ALTEON_ID AS VS_NAME_ID,                                                                                       \n"
								+ " C.OCCUR_TIME AS OCCUR_TIME, C.OBJ_TYPE AS OBJ_TYPE,                                                              \n"
								+ " COALESCE(C.BYTE_IN,-1) AS BYTE_IN, COALESCE(C.BYTE_OUT,-1) AS BYTE_OUT, COALESCE(C.BYTE_TOT,-1) AS BYTE_TOT,     \n"
								+ " COALESCE(C.BPS_IN,-1) AS BPS_IN, COALESCE(C.BPS_OUT,-1) AS BPS_OUT, COALESCE(C.BPS_TOT,-1) AS BPS_TOT,           \n"
								+ " COALESCE(C.CONN_CURR,-1) AS CONN_CURR, COALESCE(C.CONN_MAX,-1) AS CONN_MAX, COALESCE(C.CONN_TOT,-1) AS CONN_TOT, \n"
								+ " COALESCE(C.PKT_IN,-1) AS PKT_IN, COALESCE(C.PKT_OUT,-1) AS PKT_OUT, COALESCE(C.PKT_TOT,-1) AS PKT_TOT,           \n"
								+ " COALESCE(C.PPS_IN,-1) AS PPS_IN, COALESCE(C.PPS_OUT,-1) AS PPS_OUT, COALESCE(C.PPS_TOT,-1) AS PPS_TOT            \n"
								+ " FROM TMP_SLB_VS_SERVICE               A                                                                          \n"
								+ " INNER JOIN TMP_SLB_VSERVER            B                                                                          \n"
								+ " ON B.INDEX = A.VS_INDEX                                                                                          \n"
								+ " LEFT JOIN TMP_FAULT_SVC_PERF_STATS    C                                                                          \n"
								+ " ON C.OBJ_INDEX = A.INDEX                                                                                         \n"
								+ " WHERE A.ADC_INDEX = %d                                                                                           \n",
						adcIndex);
			} else {
				sqlText = String.format(
						" SELECT A.INDEX AS VS_INDEX, A.INDEX AS VSVC_INDEX, A.VIRTUAL_PORT AS SVC_PORT,                                   \n"
								+ " A.NAME AS VS_NAME_ID,                                                                                            \n"
								+ " C.OCCUR_TIME AS OCCUR_TIME, C.OBJ_TYPE AS OBJ_TYPE,                                                              \n"
								+ " COALESCE(C.BYTE_IN,-1) AS BYTE_IN, COALESCE(C.BYTE_OUT,-1) AS BYTE_OUT, COALESCE(C.BYTE_TOT,-1) AS BYTE_TOT,     \n"
								+ " COALESCE(C.BPS_IN,-1) AS BPS_IN, COALESCE(C.BPS_OUT,-1) AS BPS_OUT, COALESCE(C.BPS_TOT,-1) AS BPS_TOT,           \n"
								+ " COALESCE(C.CONN_CURR,-1) AS CONN_CURR, COALESCE(C.CONN_MAX,-1) AS CONN_MAX, COALESCE(C.CONN_TOT,-1) AS CONN_TOT, \n"
								+ " COALESCE(C.PKT_IN,-1) AS PKT_IN, COALESCE(C.PKT_OUT,-1) AS PKT_OUT, COALESCE(C.PKT_TOT,-1) AS PKT_TOT,           \n"
								+ " COALESCE(C.PPS_IN,-1) AS PPS_IN, COALESCE(C.PPS_OUT,-1) AS PPS_OUT, COALESCE(C.PPS_TOT,-1) AS PPS_TOT            \n"
								+ " FROM TMP_SLB_VSERVER                  A                                                                          \n"
								+ " LEFT JOIN TMP_FAULT_SVC_PERF_STATS    C                                                                          \n"
								+ " ON C.OBJ_INDEX = A.INDEX                                                                                         \n"
								+ " WHERE A.ADC_INDEX = %d                                                                                           \n",
						adcIndex);
			}

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoMonTrafficVSvcInfo log = new OBDtoMonTrafficVSvcInfo();
				String objIndex = db.getString(rs, "VSVC_INDEX");

				log.setAdcIndex(adcIndex);
				log.setObjIndex(objIndex);
				log.setBytesIn(db.getLong(rs, "BYTE_IN"));
				log.setBytesOut(db.getLong(rs, "BYTE_OUT"));
				log.setCurConns(db.getLong(rs, "CONN_CURR"));
				log.setMaxConns(db.getLong(rs, "CONN_MAX"));
				log.setObjType(db.getInteger(rs, "OBJ_TYPE"));
				log.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				log.setPktsIn(db.getLong(rs, "PKT_IN"));
				log.setPktsOut(db.getLong(rs, "PKT_OUT"));
				log.setSvcPort(db.getInteger(rs, "SVC_PORT"));
				log.setTotConns(db.getLong(rs, "CONN_TOT"));
				log.setVsIndex(db.getString(rs, "VS_INDEX"));
				log.setVsNameID(db.getString(rs, "VS_NAME_ID"));
				log.setVsvcIndex(db.getString(rs, "VSVC_INDEX"));

				retMap.put(objIndex, log);
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
		return retMap;
	}

	public HashMap<String, OBDtoMonTrafficVSvcMemberInfo> getSvcMemberPertInfoMap(Integer adcIndex) throws OBException {
		HashMap<String, OBDtoMonTrafficVSvcMemberInfo> retMap = new HashMap<String, OBDtoMonTrafficVSvcMemberInfo>();

		String sqlText = "";
		OBDatabase db = null;
		try {

			sqlText = String.format(" SELECT OBJ_INDEX, OCCUR_TIME, ADC_INDEX, \n"
					+ " BYTE_IN, BYTE_OUT, BYTE_TOT,             \n" + " BPS_IN, BPS_OUT, BPS_TOT,                \n"
					+ " CONN_CURR, CONN_MAX, CONN_TOT,           \n" + " PKT_IN, PKT_OUT, PKT_TOT,                \n"
					+ " PPS_IN, PPS_OUT, PPS_TOT                 \n" + " FROM TMP_FAULT_SVC_MEMBER_PERF_STATS     \n"
					+ " WHERE ADC_INDEX = %d                     \n", adcIndex);
			db = new OBDatabase();
			db.openDB();
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoMonTrafficVSvcMemberInfo log = new OBDtoMonTrafficVSvcMemberInfo();
				String objIndex = db.getString(rs, "OBJ_INDEX");

				log.setAdcIndex(adcIndex);
				log.setObjIndex(objIndex);
				log.setBytesIn(db.getLong(rs, "BYTE_IN"));
				log.setBytesOut(db.getLong(rs, "BYTE_OUT"));
				log.setCurConns(db.getLong(rs, "CONN_CURR"));
				log.setMaxConns(db.getLong(rs, "CONN_MAX"));
				log.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				log.setPktsIn(db.getLong(rs, "PKT_IN"));
				log.setPktsOut(db.getLong(rs, "PKT_OUT"));
				// log.setSvcPort(db.getInteger(rs, "SVC_PORT"));
				log.setTotConns(db.getLong(rs, "CONN_TOT"));

				retMap.put(objIndex, log);
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
		return retMap;
	}

	private HashMap<String, OBDtoMonTrafficVSvcInfo> getSvcGroupPertInfoMap(Integer adcIndex, Integer adcType)
			throws OBException {
		HashMap<String, OBDtoMonTrafficVSvcInfo> retMap = new HashMap<String, OBDtoMonTrafficVSvcInfo>();
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			if (adcType == OBDefine.ADC_TYPE_ALTEON) {
				sqlText = String.format(
						" SELECT B.INDEX AS VS_INDEX, B.INDEX AS VSVC_INDEX,                                                               \n"
								+ " B.ALTEON_ID AS VS_NAME_ID,                                                                                       \n"
								+ " C.OCCUR_TIME AS OCCUR_TIME, C.OBJ_TYPE AS OBJ_TYPE,                                                              \n"
								+ " COALESCE(C.BYTE_IN,-1) AS BYTE_IN, COALESCE(C.BYTE_OUT,-1) AS BYTE_OUT, COALESCE(C.BYTE_TOT,-1) AS BYTE_TOT,     \n"
								+ " COALESCE(C.BPS_IN,-1) AS BPS_IN, COALESCE(C.BPS_OUT,-1) AS BPS_OUT, COALESCE(C.BPS_TOT,-1) AS BPS_TOT,           \n"
								+ " COALESCE(C.CONN_CURR,-1) AS CONN_CURR, COALESCE(C.CONN_MAX,-1) AS CONN_MAX, COALESCE(C.CONN_TOT,-1) AS CONN_TOT, \n"
								+ " COALESCE(C.PKT_IN,-1) AS PKT_IN, COALESCE(C.PKT_OUT,-1) AS PKT_OUT, COALESCE(C.PKT_TOT,-1) AS PKT_TOT,           \n"
								+ " COALESCE(C.PPS_IN,-1) AS PPS_IN, COALESCE(C.PPS_OUT,-1) AS PPS_OUT, COALESCE(C.PPS_TOT,-1) AS PPS_TOT            \n"
								+ " FROM TMP_SLB_VSERVER            B                                                                          \n"
								+ " LEFT JOIN TMP_FAULT_SVC_GROUP_PERF_STATS    C                                                                          \n"
								+ " ON C.OBJ_INDEX = B.INDEX                                                                                         \n"
								+ " WHERE B.ADC_INDEX = %d                                                                                           \n",
						adcIndex);
			} else {
				return retMap;
			}

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoMonTrafficVSvcInfo log = new OBDtoMonTrafficVSvcInfo();
				String objIndex = db.getString(rs, "VSVC_INDEX");

				log.setAdcIndex(adcIndex);
				log.setObjIndex(objIndex);
				log.setBytesIn(db.getLong(rs, "BYTE_IN"));
				log.setBytesOut(db.getLong(rs, "BYTE_OUT"));
				log.setCurConns(db.getLong(rs, "CONN_CURR"));
				log.setMaxConns(db.getLong(rs, "CONN_MAX"));
				log.setObjType(db.getInteger(rs, "OBJ_TYPE"));
				log.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				log.setPktsIn(db.getLong(rs, "PKT_IN"));
				log.setPktsOut(db.getLong(rs, "PKT_OUT"));
				// log.setSvcPort(db.getInteger(rs, "SVC_PORT"));
				log.setTotConns(db.getLong(rs, "CONN_TOT"));
				log.setVsIndex(db.getString(rs, "VS_INDEX"));
				log.setVsNameID(db.getString(rs, "VS_NAME_ID"));
				log.setVsvcIndex(db.getString(rs, "VSVC_INDEX"));

				retMap.put(objIndex, log);
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
		return retMap;
	}

	private HashMap<String, OBDtoMonTrafficPoolGroup> getLastPoolGroupPerformanceData(Integer adcIndex, Integer adcType)
			throws OBException {
		HashMap<String, OBDtoMonTrafficPoolGroup> result = new HashMap<String, OBDtoMonTrafficPoolGroup>();
		String sqlText = "";

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (adcType == OBDefine.ADC_TYPE_ALTEON) {
				sqlText = String.format(
						" SELECT A.INDEX GROUP_INDEX, A.ADC_INDEX, A.ALTEON_ID, B.OCCUR_TIME AS OCCUR_TIME,  \n"
								+ " COALESCE(B.BYTE_IN,-1)   BYTE_IN,   COALESCE(B.BYTE_OUT,-1) BYTE_OUT, COALESCE(B.BYTE_TOT,-1) BYTE_TOT,     \n"
								+ " COALESCE(B.BPS_IN,-1)    BPS_IN,    COALESCE(B.BPS_OUT,-1)  BPS_OUT,  COALESCE(B.BPS_TOT,-1)  BPS_TOT,      \n"
								+ " COALESCE(B.CONN_CURR,-1) CONN_CURR, COALESCE(B.CONN_MAX,-1) CONN_MAX, COALESCE(B.CONN_TOT,-1) CONN_TOT,     \n"
								+ " COALESCE(B.PKT_IN,-1)    PKT_IN,    COALESCE(B.PKT_OUT,-1)  PKT_OUT,  COALESCE(B.PKT_TOT,-1)  PKT_TOT,      \n"
								+ " COALESCE(B.PPS_IN,-1)    PPS_IN,    COALESCE(B.PPS_OUT,-1)  PPS_OUT,  COALESCE(B.PPS_TOT,-1)  PPS_TOT       \n"
								+ " FROM (SELECT INDEX, ADC_INDEX, ALTEON_ID                                               \n"
								+ "       FROM TMP_SLB_POOL                                                                \n"
								+ "       WHERE INDEX IN (SELECT GROUP_INDEX FROM MNG_FLB_GROUP WHERE ADC_INDEX = %d)) A   \n"
								+ " LEFT JOIN (SELECT * FROM TMP_POOLGROUP_PERF_STATS WHERE ADC_INDEX = %d) B              \n"
								+ " ON A.INDEX = B.GROUP_INDEX                                                             \n",
						adcIndex, adcIndex);
			} else {
				return result;
			}

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoMonTrafficPoolGroup data = new OBDtoMonTrafficPoolGroup();
				String groupIndex = db.getString(rs, "GROUP_INDEX");

				data.setDbIndex(groupIndex);// group index
				data.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				data.setId(db.getString(rs, "ALTEON_ID"));
				data.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));

				data.setBytesIn(db.getLong(rs, "BYTE_IN"));
				data.setBytesOut(db.getLong(rs, "BYTE_OUT"));
				data.setCurConns(db.getLong(rs, "CONN_CURR"));
				data.setMaxConns(db.getLong(rs, "CONN_MAX"));
				data.setTotConns(db.getLong(rs, "CONN_TOT"));
				data.setPktsIn(db.getLong(rs, "PKT_IN"));
				data.setPktsOut(db.getLong(rs, "PKT_OUT"));

				result.put(groupIndex, data);
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
		return result;
	}

	private HashMap<String, OBDtoMonTrafficPoolGroupMember> getLastRealPerformanceData(Integer adcIndex,
			Integer adcType) throws OBException {
		HashMap<String, OBDtoMonTrafficPoolGroupMember> result = new HashMap<String, OBDtoMonTrafficPoolGroupMember>();
		String sqlText = "";

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			if (adcType == OBDefine.ADC_TYPE_ALTEON) {
				sqlText = String.format(
						" SELECT A.INDEX REAL_INDEX, A.ADC_INDEX, A.ALTEON_ID, B.OCCUR_TIME AS OCCUR_TIME,  \n"
								+ " COALESCE(B.BYTE_IN,-1)   BYTE_IN,   COALESCE(B.BYTE_OUT,-1) BYTE_OUT, COALESCE(B.BYTE_TOT,-1) BYTE_TOT,     \n"
								+ " COALESCE(B.BPS_IN,-1)    BPS_IN,    COALESCE(B.BPS_OUT,-1)  BPS_OUT,  COALESCE(B.BPS_TOT,-1)  BPS_TOT,      \n"
								+ " COALESCE(B.CONN_CURR,-1) CONN_CURR, COALESCE(B.CONN_MAX,-1) CONN_MAX, COALESCE(B.CONN_TOT,-1) CONN_TOT,     \n"
								+ " COALESCE(B.PKT_IN,-1)    PKT_IN,    COALESCE(B.PKT_OUT,-1)  PKT_OUT,  COALESCE(B.PKT_TOT,-1)  PKT_TOT,      \n"
								+ " COALESCE(B.PPS_IN,-1)    PPS_IN,    COALESCE(B.PPS_OUT,-1)  PPS_OUT,  COALESCE(B.PPS_TOT,-1)  PPS_TOT       \n"
								+ " FROM (SELECT INDEX, ADC_INDEX, ALTEON_ID FROM TMP_SLB_NODE WHERE ADC_INDEX = %d) A  \n"
								+ " LEFT JOIN (SELECT * FROM TMP_REAL_PERF_STATS WHERE ADC_INDEX = %d) B               \n"
								+ " ON A.INDEX = B.REAL_INDEX                                                       \n",
						adcIndex, adcIndex);
			} else {
				return result;
			}

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoMonTrafficPoolGroupMember data = new OBDtoMonTrafficPoolGroupMember();
				String groupIndex = db.getString(rs, "REAL_INDEX");

				data.setDbIndex(groupIndex);// group index
				data.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				data.setId(db.getString(rs, "ALTEON_ID"));
				data.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));

				data.setBytesIn(db.getLong(rs, "BYTE_IN"));
				data.setBytesOut(db.getLong(rs, "BYTE_OUT"));
				data.setCurConns(db.getLong(rs, "CONN_CURR"));
				data.setMaxConns(db.getLong(rs, "CONN_MAX"));
				data.setTotConns(db.getLong(rs, "CONN_TOT"));
				data.setPktsIn(db.getLong(rs, "PKT_IN"));
				data.setPktsOut(db.getLong(rs, "PKT_OUT"));

				result.put(groupIndex, data);
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
		return result;
	}

	private OBDtoBpsPpsEpsDps calcBpsPpsVSvcPerf(Timestamp nowTime, OBDtoMonTrafficVSvcInfo old,
			OBDtoMonTrafficVSvcInfo now, int adcType, String swVersion) {
		// comment: now 멤버변수 -1인 경우 무시 처리 필요

		OBDtoBpsPpsEpsDps retVal = new OBDtoBpsPpsEpsDps();
		if (old == null || old.getOccurTime() == null)
			return retVal;

		if (old.getBytesIn() == 0 && old.getPktsIn() == 0)
			return retVal;// 초기화 상태로 간주.

		long diffTime = Math.abs(nowTime.getTime() - old.getOccurTime().getTime());// millisec
		long diffValue;
		long value;
		diffValue = now.getBytesIn() - old.getBytesIn();
		if (now.getBytesIn() == -1 || old.getBytesIn() == -1 || diffValue < 0) // byte-in 누적값이 -1이면 수집 실패, 계산 skip
		{
			value = 0;
		} else {
			value = (long) ((float) diffValue * 8 * 1000 / diffTime);
		}
		retVal.setBpsIn(value);

		diffValue = now.getBytesOut() - old.getBytesOut();
		if (now.getBytesOut() == -1 || old.getBytesOut() == -1 || diffValue < 0) // byte-out 누적값이 -1이면 수집 실패, 계산 skip
		{
			value = 0;
		} else {
			value = (long) ((float) diffValue * 8 * 1000 / diffTime);
		}
		retVal.setBpsOut(value);

		diffValue = now.getPktsIn() - old.getPktsIn();
		if (now.getPktsIn() == -1 || old.getPktsIn() == -1 || diffValue < 0) // packets-in 누적값이 -1이면 수집 실패, 계산 skip
		{
			value = 0;
		} else {
			value = (long) ((float) diffValue * 1000 / diffTime);
		}
		retVal.setPpsIn(value);

		diffValue = now.getPktsOut() - old.getPktsOut();
		if (now.getPktsOut() == -1 || old.getPktsOut() == -1 || diffValue < 0) // packets-out 누적값이 -1이면 수집 실패, 계산 skip
		{
			value = 0;
		} else {
			value = (long) ((float) diffValue * 1000 / diffTime);
		}
		retVal.setPpsOut(value);

		diffValue = now.getFilterTot() - old.getFilterTot();
		if (now.getFilterTot() == -1 || old.getFilterTot() == -1 || diffValue < 0) // packets-out 누적값이 -1이면 수집 실패, 계산
																					// skip
		{
			value = 0;
		} else {
			value = (long) ((float) diffValue * 1000 / diffTime);
		}
		retVal.setFilterCount(value);

		return retVal;
	}

	private OBDtoBpsPpsEpsDps calcBpsPpsPoolGroupPerf(Timestamp nowTime, OBDtoMonTrafficPoolGroup old,
			OBDtoMonTrafficPoolGroup now, int adcType, String swVersion) {
		OBDtoBpsPpsEpsDps retVal = new OBDtoBpsPpsEpsDps();
		if (old == null || old.getOccurTime() == null)
			return retVal;

		if (old.getBytesIn() == 0 && old.getPktsIn() == 0)
			return retVal;// 초기화 상태로 간주.

		long diffTime = Math.abs(nowTime.getTime() - old.getOccurTime().getTime());// millisec
		long diffValue;
		long value;

		diffValue = now.getBytesIn() - old.getBytesIn();
		if (now.getBytesIn() == -1 || old.getBytesIn() == -1 || diffValue < 0) {
			value = 0;
		} else {
			value = (long) ((float) diffValue * 8 * 1000 / diffTime);
		}
		retVal.setBpsIn(value);

		retVal.setBpsOut(0);

		retVal.setPpsIn(0);

		retVal.setPpsOut(0);

		return retVal;
	}

	private OBDtoBpsPpsEpsDps calcBpsPpsRealPerf(Timestamp nowTime, OBDtoMonTrafficPoolGroupMember old,
			OBDtoMonTrafficPoolGroupMember now, int adcType, String swVersion) {
		OBDtoBpsPpsEpsDps retVal = new OBDtoBpsPpsEpsDps();
		if (old == null || old.getOccurTime() == null)
			return retVal;

		if (old.getBytesIn() == 0 && old.getPktsIn() == 0)
			return retVal;// 초기화 상태로 간주.

		long diffTime = Math.abs(nowTime.getTime() - old.getOccurTime().getTime());// millisec
		long diffValue;
		long value;

		diffValue = now.getBytesIn() - old.getBytesIn();
		if (now.getBytesIn() == -1 || old.getBytesIn() == -1 || diffValue < 0) {
			value = 0;
		} else {
			value = (long) ((float) diffValue * 8 * 1000 / diffTime);
		}
		retVal.setBpsIn(value);

		retVal.setBpsOut(0);

		retVal.setPpsIn(0);

		retVal.setPpsOut(0);

		return retVal;
	}

	private OBDtoBpsPpsEpsDps calcBpsPpsVSvcMemberPerf(Timestamp nowTime, OBDtoMonTrafficVSvcMemberInfo old,
			OBDtoMonTrafficVSvcMemberInfo now, int adcType, String swVersion) {
		OBDtoBpsPpsEpsDps retVal = new OBDtoBpsPpsEpsDps();
		if (old == null || old.getOccurTime() == null)
			return retVal;

		if (old.getBytesIn() == 0 && old.getPktsIn() == 0)
			return retVal;// 초기화 상태로 간주.

		float diffTime = (float) ((Math.abs(nowTime.getTime() - old.getOccurTime().getTime())) / 1000.0);// millisec
		float diffValue;
		float value;

		diffValue = now.getBytesIn() - old.getBytesIn();
		if (now.getBytesIn() == -1 || old.getBytesIn() == -1 || diffValue < 0) {
			value = 0;
		} else {
			value = (float) (diffValue * 8.0 / diffTime);
		}
		retVal.setBpsIn((long) value);

		diffValue = now.getBytesOut() - old.getBytesOut();
		if (now.getBytesOut() == -1 || old.getBytesOut() == -1 || diffValue < 0) {
			value = 0;
		} else {
			value = (float) (diffValue * 8.0 / diffTime);
		}
		retVal.setBpsOut((long) value);

		diffValue = now.getPktsIn() - old.getPktsIn();
		if (now.getPktsIn() == -1 || old.getPktsIn() == -1 || diffValue < 0) {
			value = 0;
		} else {
			value = (float) (diffValue / diffTime);
		}
		retVal.setPpsIn((long) value);

		diffValue = now.getPktsOut() - old.getPktsOut();
		if (now.getPktsOut() == -1 || old.getPktsOut() == -1 || diffValue < 0) {
			value = 0;
		} else {
			value = (float) (diffValue / diffTime);
		}
		retVal.setPpsOut((long) value);

		return retVal;
	}

	public void writeSvcPerfInfoList(Integer adcIndex, Integer adcType, String swVersion, Timestamp occurTime,
			ArrayList<OBDtoMonTrafficVSvcInfo> perfInfolist) throws OBException {
		if (perfInfolist.size() == 0) {
			return;
		}

		String sqlText = "";
		String sqlTextLatestDelete = "";
		String sqlTextLatestInsert = "";
		String sqlValues = "";
		OBDatabase db = null;

		try {

			HashMap<String, OBDtoMonTrafficVSvcInfo> hashMap = getSvcPertInfoMap(adcIndex, adcType);

			Integer slbLayer;
			if (adcType == OBDefine.ADC_TYPE_ALTEON) {
				slbLayer = OBDtoMonTrafficVSvcInfo.SLBLAYER_SERVICE;
			} else {
				slbLayer = OBDtoMonTrafficVSvcInfo.SLBLAYER_SERVER;
			}

			sqlValues = "";
			for (OBDtoMonTrafficVSvcInfo vsvcLog : perfInfolist) {
				String vsvcIndex = vsvcLog.getObjIndex();

				OBDtoMonTrafficVSvcInfo old = hashMap.get(vsvcIndex);
				OBDtoBpsPpsEpsDps bpsPps = calcBpsPpsVSvcPerf(occurTime, old, vsvcLog, adcType, swVersion);

				if (adcType == OBDefine.ADC_TYPE_PIOLINK_PASK || adcType == OBDefine.ADC_TYPE_PIOLINK_PAS) // PASK 트래픽
																											// 보정 처리
				{
					bpsPps.setBpsIn(vsvcLog.getBytesIn());
					bpsPps.setBpsOut(vsvcLog.getBytesOut());
					bpsPps.setPpsIn(vsvcLog.getPktsIn());
					bpsPps.setPpsOut(vsvcLog.getPktsOut());
				}

				Long byteTotal = calcSumOrEmpty(vsvcLog.getBytesIn(), vsvcLog.getBytesOut());
				Long bpsTotal = calcSumOrEmpty(bpsPps.getBpsIn(), bpsPps.getBpsOut());
				Long pktTotal = calcSumOrEmpty(vsvcLog.getPktsIn(), vsvcLog.getPktsOut());
				Long ppsTotal = calcSumOrEmpty(bpsPps.getPpsIn(), bpsPps.getPpsOut());

				if (bpsTotal == 0 && vsvcLog.getCurConns() == 0) {
					continue;
				}

				if (!sqlValues.isEmpty())
					sqlValues += ", ";

				sqlValues += String.format(
						" (%s, %d, %d, %s, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d)",
						OBParser.sqlString(vsvcIndex), adcIndex, slbLayer, OBParser.sqlString(occurTime),
						vsvcLog.getBytesIn(), vsvcLog.getBytesOut(), byteTotal, bpsPps.getBpsIn(), bpsPps.getBpsOut(),
						bpsTotal, vsvcLog.getCurConns(), vsvcLog.getMaxConns(), vsvcLog.getTotConns(),
						vsvcLog.getPktsIn(), vsvcLog.getPktsOut(), pktTotal, bpsPps.getPpsIn(), bpsPps.getPpsOut(),
						ppsTotal);
			}
			sqlText = String.format(
					" INSERT INTO LOG_SVC_PERF_STATS                         \n"
							+ " (OBJ_INDEX, ADC_INDEX, OBJ_TYPE, OCCUR_TIME,                 \n"
							+ " BYTE_IN, BYTE_OUT, BYTE_TOT, BPS_IN, BPS_OUT, BPS_TOT,       \n"
							+ " CONN_CURR, CONN_MAX, CONN_TOT,                               \n"
							+ " PKT_IN, PKT_OUT, PKT_TOT, PPS_IN, PPS_OUT, PPS_TOT)          \n" + " VALUES %s ",
					sqlValues);
			sqlTextLatestDelete = String.format(" DELETE FROM TMP_FAULT_SVC_PERF_STATS WHERE ADC_INDEX = %d ;",
					adcIndex);
			sqlTextLatestInsert = String.format(
					" INSERT INTO TMP_FAULT_SVC_PERF_STATS                         \n"
							+ " (OBJ_INDEX, ADC_INDEX, OBJ_TYPE, OCCUR_TIME,                 \n"
							+ " BYTE_IN, BYTE_OUT, BYTE_TOT, BPS_IN, BPS_OUT, BPS_TOT,       \n"
							+ " CONN_CURR, CONN_MAX, CONN_TOT,                               \n"
							+ " PKT_IN, PKT_OUT, PKT_TOT, PPS_IN, PPS_OUT, PPS_TOT)          \n" + " VALUES %s ",
					sqlValues);
			db = new OBDatabase();
			db.openDB();
			if (sqlValues != "") {
				db.executeUpdate(sqlText);
				db.executeUpdate(sqlTextLatestDelete);
				db.executeUpdate(sqlTextLatestInsert);
			} else {
				db.executeUpdate(sqlTextLatestDelete);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.info(OBDefine.LOGFILE_ADCMON, String.format("end. adcIndex:%s", adcIndex));
	}

	public void writeSvcMemberPerfInfoList(Integer adcIndex, int adcType, String swVersion, Timestamp occurTime,
			ArrayList<OBDtoMonTrafficVSvcMemberInfo> perfInfolist,
			HashMap<String, OBDtoMonTrafficVSvcMemberInfo> hashMap) throws OBException {
		if (perfInfolist.size() == 0) {
			return;
		}

		String sqlText = "";
		String sqlTextLatestDelete = "";
		String sqlTextLatestInsert = "";
		String sqlValues = "";

		OBDatabase db = null;

		try {

			sqlValues = "";
			for (OBDtoMonTrafficVSvcMemberInfo vsvcLog : perfInfolist) {
				String objIndex = vsvcLog.getObjIndex();

				OBDtoMonTrafficVSvcMemberInfo old = hashMap.get(objIndex);
				OBDtoBpsPpsEpsDps bpsPps = calcBpsPpsVSvcMemberPerf(occurTime, old, vsvcLog, adcType, swVersion);

				if (adcType == OBDefine.ADC_TYPE_PIOLINK_PASK || adcType == OBDefine.ADC_TYPE_PIOLINK_PAS) // PASK 트래픽
																											// 보정 처리
				{
					bpsPps.setBpsIn(vsvcLog.getBytesIn());
					bpsPps.setBpsOut(vsvcLog.getBytesOut());
					bpsPps.setPpsIn(vsvcLog.getPktsIn());
					bpsPps.setPpsOut(vsvcLog.getPktsOut());
				}

				Long byteTotal = calcSumOrEmpty(vsvcLog.getBytesIn(), vsvcLog.getBytesOut());
				Long bpsTotal = calcSumOrEmpty(bpsPps.getBpsIn(), bpsPps.getBpsOut());
				Long pktTotal = calcSumOrEmpty(vsvcLog.getPktsIn(), vsvcLog.getPktsOut());
				Long ppsTotal = calcSumOrEmpty(bpsPps.getPpsIn(), bpsPps.getPpsOut());

				if (bpsTotal == 0 && vsvcLog.getCurConns() == 0) {
					continue;
				}

				if (!sqlValues.isEmpty())
					sqlValues += ", ";

				sqlValues += String.format(
						" ( %s, %s, %s, %s, %d, %s, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d ) ",
						OBParser.sqlString(objIndex), OBParser.sqlString(vsvcLog.getVsIndex()),
						OBParser.sqlString(vsvcLog.getPoolIndex()), OBParser.sqlString(vsvcLog.getPoolMemberIndex()),
						adcIndex, OBParser.sqlString(occurTime), vsvcLog.getBytesIn(), vsvcLog.getBytesOut(), byteTotal,
						bpsPps.getBpsIn(), bpsPps.getBpsOut(), bpsTotal, vsvcLog.getCurConns(), vsvcLog.getMaxConns(),
						vsvcLog.getTotConns(), vsvcLog.getPktsIn(), vsvcLog.getPktsOut(), pktTotal, bpsPps.getPpsIn(),
						bpsPps.getPpsOut(), ppsTotal);
			}
			sqlText = String.format(" INSERT INTO LOG_SVC_MEMBER_PERF_STATS                                  \n"
					+ " (OBJ_INDEX, VS_INDEX, POOL_INDEX, MEMBER_INDEX, ADC_INDEX, OCCUR_TIME, \n"
					+ " BYTE_IN, BYTE_OUT, BYTE_TOT, BPS_IN, BPS_OUT, BPS_TOT,                 \n"
					+ " CONN_CURR, CONN_MAX, CONN_TOT,                                         \n"
					+ " PKT_IN, PKT_OUT, PKT_TOT, PPS_IN, PPS_OUT, PPS_TOT)                    \n" + " VALUES %s ",
					sqlValues);
			sqlTextLatestDelete = String.format(" DELETE FROM TMP_FAULT_SVC_MEMBER_PERF_STATS WHERE ADC_INDEX = %d ;",
					adcIndex);
			sqlTextLatestInsert = String
					.format(" INSERT INTO TMP_FAULT_SVC_MEMBER_PERF_STATS                            \n"
							+ " (OBJ_INDEX, VS_INDEX, POOL_INDEX, MEMBER_INDEX, ADC_INDEX, OCCUR_TIME, \n"
							+ " BYTE_IN, BYTE_OUT, BYTE_TOT, BPS_IN, BPS_OUT, BPS_TOT,                 \n"
							+ " CONN_CURR, CONN_MAX, CONN_TOT,                                         \n"
							+ " PKT_IN, PKT_OUT, PKT_TOT, PPS_IN, PPS_OUT, PPS_TOT)                    \n"
							+ " VALUES %s ", sqlValues);

			db = new OBDatabase();
			db.openDB();
			if (sqlValues != "") {
				db.executeUpdate(sqlText);
				db.executeUpdate(sqlTextLatestDelete);
				db.executeUpdate(sqlTextLatestInsert);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.info(OBDefine.LOGFILE_ADCMON, String.format("end. adcIndex:%s", adcIndex));
	}

	public void writeSvcGroupPerfInfoList(Integer adcIndex, Integer adcType, String swVersion, Timestamp occurTime,
			ArrayList<OBDtoMonTrafficVSvcInfo> perfInfolist) throws OBException {
		if (perfInfolist.size() == 0) {
			return;
		}

		String sqlText = "";
		String sqlTextLatestDelete = "";
		String sqlTextLatestInsert = "";
		String sqlValues = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			HashMap<String, OBDtoMonTrafficVSvcInfo> hashMap = getSvcGroupPertInfoMap(adcIndex, adcType);

			Integer objType = OBDtoMonTrafficVSvcInfo.SLBLAYER_SERVER;

			sqlValues = "";
			for (OBDtoMonTrafficVSvcInfo vsvcLog : perfInfolist) {
				String vsvcIndex = vsvcLog.getObjIndex();

				OBDtoMonTrafficVSvcInfo old = hashMap.get(vsvcIndex);
				OBDtoBpsPpsEpsDps bpsPps = calcBpsPpsVSvcPerf(occurTime, old, vsvcLog, adcType, swVersion);

				Long byteTotal = calcSumOrEmpty(vsvcLog.getBytesIn(), vsvcLog.getBytesOut());
				Long bpsTotal = calcSumOrEmpty(bpsPps.getBpsIn(), bpsPps.getBpsOut());
				Long pktTotal = calcSumOrEmpty(vsvcLog.getPktsIn(), vsvcLog.getPktsOut());
				Long ppsTotal = calcSumOrEmpty(bpsPps.getPpsIn(), bpsPps.getPpsOut());

				if (!sqlValues.isEmpty())
					sqlValues += ", ";

				sqlValues += String.format(
						" (%s, %d, %d, %s, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d)",
						OBParser.sqlString(vsvcIndex), adcIndex, objType, OBParser.sqlString(occurTime),
						vsvcLog.getBytesIn(), vsvcLog.getBytesOut(), byteTotal, bpsPps.getBpsIn(), bpsPps.getBpsOut(),
						bpsTotal, vsvcLog.getCurConns(), vsvcLog.getMaxConns(), vsvcLog.getTotConns(),
						vsvcLog.getPktsIn(), vsvcLog.getPktsOut(), pktTotal, bpsPps.getPpsIn(), bpsPps.getPpsOut(),
						ppsTotal);
			}
			sqlText = String.format(
					" INSERT INTO LOG_SVC_GROUP_PERF_STATS                         \n"
							+ " (OBJ_INDEX, ADC_INDEX, OBJ_TYPE, OCCUR_TIME,                 \n"
							+ " BYTE_IN, BYTE_OUT, BYTE_TOT, BPS_IN, BPS_OUT, BPS_TOT,       \n"
							+ " CONN_CURR, CONN_MAX, CONN_TOT,                               \n"
							+ " PKT_IN, PKT_OUT, PKT_TOT, PPS_IN, PPS_OUT, PPS_TOT)          \n" + " VALUES %s ",
					sqlValues);
			sqlTextLatestDelete = String.format(" DELETE FROM TMP_FAULT_SVC_GROUP_PERF_STATS WHERE ADC_INDEX = %d ;",
					adcIndex);
			sqlTextLatestInsert = String.format(
					" INSERT INTO TMP_FAULT_SVC_GROUP_PERF_STATS                         \n"
							+ " (OBJ_INDEX, ADC_INDEX, OBJ_TYPE, OCCUR_TIME,                 \n"
							+ " BYTE_IN, BYTE_OUT, BYTE_TOT, BPS_IN, BPS_OUT, BPS_TOT,       \n"
							+ " CONN_CURR, CONN_MAX, CONN_TOT,                               \n"
							+ " PKT_IN, PKT_OUT, PKT_TOT, PPS_IN, PPS_OUT, PPS_TOT)          \n" + " VALUES %s ",
					sqlValues);
			if (sqlValues != "") {
				db.executeUpdate(sqlText);
				db.executeUpdate(sqlTextLatestDelete);
				db.executeUpdate(sqlTextLatestInsert);
			} else {
				db.executeUpdate(sqlTextLatestDelete);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.info(OBDefine.LOGFILE_ADCMON, String.format("end. adcIndex:%s", adcIndex));
	}

	private long calcSumOrEmpty(long val1, long val2) // 두 값을 계산하는데, 값이 없을 때 -1 표시임을 감안한다.
	{
		long sum = -1;
		if (val1 < 0 && val2 < 0) // 값이 없을 때, -1로 표시하기로 한 규칙때문에 이런 계산이 필요하다.
		{
			return 0;
		} else {
			sum = 0;
			if (val1 >= 0) {
				sum += val1;
			}
			if (val2 >= 0) {
				sum += val2;
			}
		}
		return sum;
	}

	public void writePoolGroupPerformance(Integer adcIndex, Integer adcType, String swVersion, Timestamp occurTime,
			ArrayList<OBDtoMonTrafficPoolGroup> groupPerfList) throws OBException {
		// 트래픽 수집 오류거나, 수집할 게 없을 경우 처리를 건너뛴다.
		// groupPerfList를 구할 때 null 리턴을 방지했기 때문에 null은 사실상 나오지 않는다.
		if (groupPerfList == null || groupPerfList.size() == 0) {
			return;
		}
		String sqlText = "";
		String sqlTextLatestDelete = "";
		String sqlTextLatestInsert = "";
		String sqlValues = "";
		String groupIndex;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			HashMap<String, OBDtoMonTrafficPoolGroup> lastData = getLastPoolGroupPerformanceData(adcIndex, adcType);

			sqlValues = "";
			for (OBDtoMonTrafficPoolGroup data : groupPerfList) {
				groupIndex = data.getDbIndex();

				OBDtoMonTrafficPoolGroup old = lastData.get(groupIndex);
				OBDtoBpsPpsEpsDps bpsPps = calcBpsPpsPoolGroupPerf(occurTime, old, data, adcType, swVersion);

				if (sqlValues.isEmpty() == false) {
					sqlValues += ", ";
				}

				sqlValues += String.format(
						" (%s, %s, %d, %s, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d)",
						OBParser.sqlString(groupIndex), OBParser.sqlString(data.getId()), data.getAdcIndex(),
						OBParser.sqlString(occurTime), data.getBytesIn(), data.getBytesOut(),
						calcSumOrEmpty(data.getBytesIn(), data.getBytesOut()), bpsPps.getBpsIn(), bpsPps.getBpsOut(),
						calcSumOrEmpty(bpsPps.getBpsIn(), bpsPps.getBpsOut()), data.getCurConns(), data.getMaxConns(),
						data.getTotConns(), data.getPktsIn(), data.getPktsOut(),
						calcSumOrEmpty(data.getPktsIn(), data.getPktsOut()), bpsPps.getPpsIn(), bpsPps.getPpsOut(),
						calcSumOrEmpty(bpsPps.getPpsIn(), bpsPps.getPpsOut()), -1 // CPS
				);
			}
			sqlText = String.format(" INSERT INTO LOG_POOLGROUP_PERF_STATS (           \n"
					+ "    GROUP_INDEX, GROUP_ID, ADC_INDEX, OCCUR_TIME, \n"
					+ "    BYTE_IN, BYTE_OUT, BYTE_TOT,                  \n"
					+ "    BPS_IN, BPS_OUT, BPS_TOT,                     \n"
					+ "    CONN_CURR, CONN_MAX, CONN_TOT,                \n"
					+ "    PKT_IN, PKT_OUT, PKT_TOT,                     \n"
					+ "    PPS_IN, PPS_OUT, PPS_TOT, CPS )               \n" + " VALUES %s ", sqlValues);
			sqlTextLatestDelete = String.format(" DELETE FROM TMP_POOLGROUP_PERF_STATS WHERE ADC_INDEX = %d ;",
					adcIndex);
			sqlTextLatestInsert = String.format(" INSERT INTO TMP_POOLGROUP_PERF_STATS (  \n"
					+ "    GROUP_INDEX, GROUP_ID, ADC_INDEX, OCCUR_TIME, \n"
					+ "    BYTE_IN, BYTE_OUT, BYTE_TOT,                  \n"
					+ "    BPS_IN, BPS_OUT, BPS_TOT,                     \n"
					+ "    CONN_CURR, CONN_MAX, CONN_TOT,                \n"
					+ "    PKT_IN, PKT_OUT, PKT_TOT,                     \n"
					+ "    PPS_IN, PPS_OUT, PPS_TOT, CPS )               \n" + " VALUES %s ", sqlValues);
			db.executeUpdate(sqlText);
			db.executeUpdate(sqlTextLatestDelete);
			db.executeUpdate(sqlTextLatestInsert);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.info(OBDefine.LOGFILE_ADCMON, String.format("end. adcIndex:%s", adcIndex));
	}

	public void writeRealPerformance(Integer adcIndex, Integer adcType, String swVersion, Timestamp occurTime,
			ArrayList<OBDtoMonTrafficPoolGroupMember> realPerfList) throws OBException {
		// 트래픽 수집 오류거나, 수집할 게 없을 경우 처리를 건너뛴다.
		// real의 트래픽은 OBFaultMonitoringImpl().getRealPerfDataFromADC()에서 구하는데 사실상 null을
		// 리턴하지 않는다.
		if (realPerfList == null || realPerfList.size() == 0) {
			return;
		}
		String sqlText = "";
		String sqlTextLatestDelete = "";
		String sqlTextLatestInsert = "";
		String sqlValues = "";
		String realIndex;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			HashMap<String, OBDtoMonTrafficPoolGroupMember> lastData = getLastRealPerformanceData(adcIndex, adcType);

			sqlValues = "";
			for (OBDtoMonTrafficPoolGroupMember data : realPerfList) {
				realIndex = data.getDbIndex();

				OBDtoMonTrafficPoolGroupMember old = lastData.get(realIndex);
				OBDtoBpsPpsEpsDps bpsPps = calcBpsPpsRealPerf(occurTime, old, data, adcType, swVersion);

				if (sqlValues.isEmpty() == false) {
					sqlValues += ", ";
				}

				sqlValues += String.format(
						" (%s, %s, %d, %s, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d)",
						OBParser.sqlString(realIndex), OBParser.sqlString(data.getId()), data.getAdcIndex(),
						OBParser.sqlString(occurTime), data.getBytesIn(), data.getBytesOut(),
						calcSumOrEmpty(data.getBytesIn(), data.getBytesOut()), bpsPps.getBpsIn(), bpsPps.getBpsOut(),
						calcSumOrEmpty(bpsPps.getBpsIn(), bpsPps.getBpsOut()), data.getCurConns(), data.getMaxConns(),
						data.getTotConns(), data.getPktsIn(), data.getPktsOut(),
						calcSumOrEmpty(data.getPktsIn(), data.getPktsOut()), bpsPps.getPpsIn(), bpsPps.getPpsOut(),
						calcSumOrEmpty(bpsPps.getPpsIn(), bpsPps.getPpsOut()), -1 // CPS
				);
			}
			sqlText = String.format(
					" INSERT INTO LOG_REAL_PERF_STATS (                            \n"
							+ "    REAL_INDEX, REAL_ID, ADC_INDEX, OCCUR_TIME,               \n"
							+ "    BYTE_IN, BYTE_OUT, BYTE_TOT, BPS_IN, BPS_OUT, BPS_TOT,    \n"
							+ "    CONN_CURR, CONN_MAX, CONN_TOT, PKT_IN, PKT_OUT, PKT_TOT,  \n"
							+ "    PPS_IN, PPS_OUT, PPS_TOT, CPS )                           \n" + " VALUES %s ",
					sqlValues);
			sqlTextLatestDelete = String.format(" DELETE FROM TMP_REAL_PERF_STATS WHERE ADC_INDEX = %d ;", adcIndex);
			sqlTextLatestInsert = String.format(
					" INSERT INTO TMP_REAL_PERF_STATS (                            \n"
							+ "    REAL_INDEX, REAL_ID, ADC_INDEX, OCCUR_TIME,               \n"
							+ "    BYTE_IN, BYTE_OUT, BYTE_TOT, BPS_IN, BPS_OUT, BPS_TOT,    \n"
							+ "    CONN_CURR, CONN_MAX, CONN_TOT, PKT_IN, PKT_OUT, PKT_TOT,  \n"
							+ "    PPS_IN, PPS_OUT, PPS_TOT, CPS )                           \n" + " VALUES %s ",
					sqlValues);
			db.executeUpdate(sqlText);
			db.executeUpdate(sqlTextLatestDelete);
			db.executeUpdate(sqlTextLatestInsert);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.info(OBDefine.LOGFILE_ADCMON, String.format("end. adcIndex:%s", adcIndex));
	}

	public void writePowerSupplyStatus(Integer adcIndex, Timestamp occurTime, OBDtoPowerSupplyInfo psInfo)
			throws OBException {
		String sqlText = "";

		OBDatabase db = new OBDatabase();

		if (psInfo == null)
			return;

		try {
			db.openDB();

			sqlText = String.format(
					" INSERT INTO LOG_POWERSUPPLY_STATUS                                     \n"
							+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
							+ " PS1_STATUS, PS2_STATUS, PS3_STATUS, PS4_STATUS)                        \n"
							+ " VALUES                                                                 \n"
							+ " (%d, %s, %d, %d, %d, %d)                                               \n",
					adcIndex, OBParser.sqlString(occurTime), psInfo.getPan1Status(), psInfo.getPan2Status(),
					psInfo.getPan3Status(), psInfo.getPan4Status());

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

	public void writeHddStatus(Integer adcIndex, Timestamp occurTime, OBDtoHddInfo hddInfo) throws OBException {
		String sqlText = "";

		OBDatabase db = new OBDatabase();

		if (hddInfo == null)
			return;

		try {
			db.openDB();

			sqlText = String.format(
					" INSERT INTO LOG_RESC_HDD                               \n"
							+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
							+ " HDD_TOTAL, HDD_USAGE)                        \n"
							+ " VALUES                                                                 \n"
							+ " (%d, %s, %d, %d)                                               \n",
					adcIndex, OBParser.sqlString(occurTime), hddInfo.getHddTotal(), hddInfo.getHddUsage());

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

	private final static Integer MAX_FAN_COUNT = 4;

	public void writeFanStatus(Integer adcIndex, Timestamp occurTime, ArrayList<Integer> statusList)
			throws OBException {
		String sqlText = "";
		String valueText = "";

		OBDatabase db = new OBDatabase();

		int listSize = statusList.size();

		if (listSize == 0)
			return;

		try {
			db.openDB();

			valueText = "";
			for (int i = 0; i < MAX_FAN_COUNT; i++) {
				if (!valueText.isEmpty())
					valueText += ", ";
				if (i >= listSize)
					valueText += (-1);
				else
					valueText += String.format("%d", statusList.get(i));
			}

			sqlText = String.format(
					" INSERT INTO LOG_FAN_STATUS                                         \n"
							+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
							+ " FAN1_STATUS, FAN2_STATUS, FAN3_STATUS, FAN4_STATUS)                    \n"
							+ " VALUES                                                                 \n"
							+ " (%d, %s, %s)                                                           \n",
					adcIndex, OBParser.sqlString(occurTime), valueText);

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

	public void writeSTGStatus(Integer adcIndex, Timestamp occurTime, OBDtoSTGStatus status, OBDatabase db)
			throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(
					" INSERT INTO LOG_L23_STG_INFO                                     \n"
							+ " (ADC_INDEX, OCCUR_TIME, GROUP_INDEX,                                   \n"
							+ " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10, \n"
							+ " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
							+ " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
							+ " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36)                        \n"
							+ " VALUES                                                                 \n"
							+ " (%d, %s, %d,                                                           \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d)                                                \n",
					adcIndex, OBParser.sqlString(occurTime), status.getGroupIndex(), status.getPort1Status(),
					status.getPort2Status(), status.getPort3Status(), status.getPort4Status(), status.getPort5Status(),
					status.getPort6Status(), status.getPort7Status(), status.getPort8Status(), status.getPort9Status(),
					status.getPort10Status(), status.getPort11Status(), status.getPort12Status(),
					status.getPort13Status(), status.getPort14Status(), status.getPort15Status(),
					status.getPort16Status(), status.getPort17Status(), status.getPort18Status(),
					status.getPort19Status(), status.getPort20Status(), status.getPort21Status(),
					status.getPort22Status(), status.getPort23Status(), status.getPort24Status(),
					status.getPort25Status(), status.getPort26Status(), status.getPort27Status(),
					status.getPort28Status(), status.getPort29Status(), status.getPort30Status(),
					status.getPort31Status(), status.getPort32Status(), status.getPort33Status(),
					status.getPort34Status(), status.getPort35Status(), status.getPort36Status());

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public void writeCpuMemStatus(Integer adcIndex, Timestamp occurTime, OBDtoCpuMemStatus cpuMemStatus)
			throws OBException {
		String sqlText = "";

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			sqlText = String.format(
					" INSERT INTO LOG_RESC_CPUMEM                                      		 \n"
							+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
							+ " CPU1_USAGE, CPU2_USAGE, CPU3_USAGE, CPU4_USAGE, CPU5_USAGE,            \n"
							+ " CPU6_USAGE, CPU7_USAGE, CPU8_USAGE, CPU9_USAGE, CPU10_USAGE,           \n"
							+ " CPU11_USAGE, CPU12_USAGE, CPU13_USAGE, CPU14_USAGE, CPU15_USAGE, 		 \n"
							+ " CPU16_USAGE, MEM_USAGE,												 \n"
							+ " CPU17_USAGE, CPU18_USAGE, CPU19_USAGE, 		 						 \n"
							+ " CPU20_USAGE, CPU21_USAGE, CPU22_USAGE, CPU23_USAGE, CPU24_USAGE, 		 \n"
							+ " CPU25_USAGE, CPU26_USAGE, CPU27_USAGE, CPU28_USAGE, CPU29_USAGE, 		 \n"
							+ " CPU30_USAGE, CPU31_USAGE, CPU32_USAGE, CPU2_CONNS, CPU3_CONNS, 		 \n"
							+ " CPU4_CONNS,  CPU5_CONNS, CPU6_CONNS, CPU7_CONNS, CPU8_CONNS, 			 \n"
							+ " CPU9_CONNS,  CPU10_CONNS, CPU11_CONNS, CPU12_CONNS, CPU13_CONNS, 		 \n"
							+ " CPU14_CONNS, CPU15_CONNS, CPU16_CONNS, CPU17_CONNS, CPU18_CONNS, 		 \n"
							+ " CPU19_CONNS, CPU20_CONNS, CPU21_CONNS, CPU22_CONNS, CPU23_CONNS, 		 \n"
							+ " CPU24_CONNS, CPU25_CONNS, CPU26_CONNS, CPU27_CONNS, CPU28_CONNS, 		 \n"
							+ " CPU29_CONNS, CPU30_CONNS, CPU31_CONNS, CPU32_CONNS                     \n"
							+ " )                                                             		 \n"
							+ " VALUES                                                                 \n"
							+ " (%d, %s,                                                               \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d)                                                        \n",
					adcIndex, OBParser.sqlString(occurTime), cpuMemStatus.getCpu1Usage(), cpuMemStatus.getCpu2Usage(),
					cpuMemStatus.getCpu3Usage(), cpuMemStatus.getCpu4Usage(), cpuMemStatus.getCpu5Usage(),
					cpuMemStatus.getCpu6Usage(), cpuMemStatus.getCpu7Usage(), cpuMemStatus.getCpu8Usage(),
					cpuMemStatus.getCpu9Usage(), cpuMemStatus.getCpu10Usage(), cpuMemStatus.getCpu11Usage(),
					cpuMemStatus.getCpu12Usage(), cpuMemStatus.getCpu13Usage(), cpuMemStatus.getCpu14Usage(),
					cpuMemStatus.getCpu15Usage(), cpuMemStatus.getCpu16Usage(), cpuMemStatus.getMemUsage(),
					cpuMemStatus.getCpu17Usage(), cpuMemStatus.getCpu18Usage(), cpuMemStatus.getCpu19Usage(),
					cpuMemStatus.getCpu20Usage(), cpuMemStatus.getCpu21Usage(), cpuMemStatus.getCpu22Usage(),
					cpuMemStatus.getCpu23Usage(), cpuMemStatus.getCpu24Usage(), cpuMemStatus.getCpu25Usage(),
					cpuMemStatus.getCpu26Usage(), cpuMemStatus.getCpu27Usage(), cpuMemStatus.getCpu28Usage(),
					cpuMemStatus.getCpu29Usage(), cpuMemStatus.getCpu30Usage(), cpuMemStatus.getCpu31Usage(),
					cpuMemStatus.getCpu32Usage(), cpuMemStatus.getCpu2Conns(), cpuMemStatus.getCpu3Conns(),
					cpuMemStatus.getCpu4Conns(), cpuMemStatus.getCpu5Conns(), cpuMemStatus.getCpu6Conns(),
					cpuMemStatus.getCpu7Conns(), cpuMemStatus.getCpu8Conns(), cpuMemStatus.getCpu9Conns(),
					cpuMemStatus.getCpu10Conns(), cpuMemStatus.getCpu11Conns(), cpuMemStatus.getCpu12Conns(),
					cpuMemStatus.getCpu13Conns(), cpuMemStatus.getCpu14Conns(), cpuMemStatus.getCpu15Conns(),
					cpuMemStatus.getCpu16Conns(), cpuMemStatus.getCpu17Conns(), cpuMemStatus.getCpu18Conns(),
					cpuMemStatus.getCpu19Conns(), cpuMemStatus.getCpu20Conns(), cpuMemStatus.getCpu21Conns(),
					cpuMemStatus.getCpu22Conns(), cpuMemStatus.getCpu23Conns(), cpuMemStatus.getCpu24Conns(),
					cpuMemStatus.getCpu25Conns(), cpuMemStatus.getCpu26Conns(), cpuMemStatus.getCpu27Conns(),
					cpuMemStatus.getCpu28Conns(), cpuMemStatus.getCpu29Conns(), cpuMemStatus.getCpu30Conns(),
					cpuMemStatus.getCpu31Conns(), cpuMemStatus.getCpu32Conns());

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

	public void writeTmpCpuMemStatus(Integer adcIndex, Timestamp occurTime, OBDtoCpuMemStatus cpuMemStatus,
			OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			String sqlTextLatestDelete = String.format(" DELETE FROM TMP_FAULT_RESC_CPUMEM WHERE ADC_INDEX = %d;",
					adcIndex);
			db.executeUpdate(sqlTextLatestDelete);

			sqlText = String.format(
					" INSERT INTO TMP_FAULT_RESC_CPUMEM                                      \n"
							+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
							+ " CPU1_USAGE, CPU2_USAGE, CPU3_USAGE, CPU4_USAGE, CPU5_USAGE,            \n"
							+ " CPU6_USAGE, CPU7_USAGE, CPU8_USAGE, CPU9_USAGE, CPU10_USAGE, 			 \n"
							+ " CPU11_USAGE, CPU12_USAGE, CPU13_USAGE, CPU14_USAGE, CPU15_USAGE, 		 \n"
							+ " CPU16_USAGE, MEM_USAGE, CPU17_USAGE, CPU18_USAGE, CPU19_USAGE, 		 \n"
							+ " CPU20_USAGE, CPU21_USAGE, CPU22_USAGE, CPU23_USAGE, CPU24_USAGE, 		 \n"
							+ " CPU25_USAGE, CPU26_USAGE, CPU27_USAGE, CPU28_USAGE, CPU29_USAGE, 		 \n"
							+ " CPU30_USAGE, CPU31_USAGE, CPU32_USAGE, CPU2_CONNS, CPU3_CONNS, 		 \n"
							+ " CPU4_CONNS,  CPU5_CONNS, CPU6_CONNS, CPU7_CONNS, CPU8_CONNS, 			 \n"
							+ " CPU9_CONNS,  CPU10_CONNS, CPU11_CONNS, CPU12_CONNS, CPU13_CONNS, 		 \n"
							+ " CPU14_CONNS, CPU15_CONNS, CPU16_CONNS, CPU17_CONNS, CPU18_CONNS, 		 \n"
							+ " CPU19_CONNS, CPU20_CONNS, CPU21_CONNS, CPU22_CONNS, CPU23_CONNS, 		 \n"
							+ " CPU24_CONNS, CPU25_CONNS, CPU26_CONNS, CPU27_CONNS, CPU28_CONNS, 		 \n"
							+ " CPU29_CONNS, CPU30_CONNS, CPU31_CONNS, CPU32_CONNS)                    \n"
							+ " VALUES                                                                 \n"
							+ " (%d, %s,                                                               \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d)                                                        \n",
					adcIndex, OBParser.sqlString(occurTime), cpuMemStatus.getCpu1Usage(), cpuMemStatus.getCpu2Usage(),
					cpuMemStatus.getCpu3Usage(), cpuMemStatus.getCpu4Usage(), cpuMemStatus.getCpu5Usage(),
					cpuMemStatus.getCpu6Usage(), cpuMemStatus.getCpu7Usage(), cpuMemStatus.getCpu8Usage(),
					cpuMemStatus.getCpu9Usage(), cpuMemStatus.getCpu10Usage(), cpuMemStatus.getCpu11Usage(),
					cpuMemStatus.getCpu12Usage(), cpuMemStatus.getCpu13Usage(), cpuMemStatus.getCpu14Usage(),
					cpuMemStatus.getCpu15Usage(), cpuMemStatus.getCpu16Usage(), cpuMemStatus.getMemUsage(),
					cpuMemStatus.getCpu17Usage(), cpuMemStatus.getCpu18Usage(), cpuMemStatus.getCpu19Usage(),
					cpuMemStatus.getCpu20Usage(), cpuMemStatus.getCpu21Usage(), cpuMemStatus.getCpu22Usage(),
					cpuMemStatus.getCpu23Usage(), cpuMemStatus.getCpu24Usage(), cpuMemStatus.getCpu25Usage(),
					cpuMemStatus.getCpu26Usage(), cpuMemStatus.getCpu27Usage(), cpuMemStatus.getCpu28Usage(),
					cpuMemStatus.getCpu29Usage(), cpuMemStatus.getCpu30Usage(), cpuMemStatus.getCpu31Usage(),
					cpuMemStatus.getCpu32Usage(), cpuMemStatus.getCpu2Conns(), cpuMemStatus.getCpu3Conns(),
					cpuMemStatus.getCpu4Conns(), cpuMemStatus.getCpu5Conns(), cpuMemStatus.getCpu6Conns(),
					cpuMemStatus.getCpu7Conns(), cpuMemStatus.getCpu8Conns(), cpuMemStatus.getCpu9Conns(),
					cpuMemStatus.getCpu10Conns(), cpuMemStatus.getCpu11Conns(), cpuMemStatus.getCpu12Conns(),
					cpuMemStatus.getCpu13Conns(), cpuMemStatus.getCpu14Conns(), cpuMemStatus.getCpu15Conns(),
					cpuMemStatus.getCpu16Conns(), cpuMemStatus.getCpu17Conns(), cpuMemStatus.getCpu18Conns(),
					cpuMemStatus.getCpu19Conns(), cpuMemStatus.getCpu20Conns(), cpuMemStatus.getCpu21Conns(),
					cpuMemStatus.getCpu22Conns(), cpuMemStatus.getCpu23Conns(), cpuMemStatus.getCpu24Conns(),
					cpuMemStatus.getCpu25Conns(), cpuMemStatus.getCpu26Conns(), cpuMemStatus.getCpu27Conns(),
					cpuMemStatus.getCpu28Conns(), cpuMemStatus.getCpu29Conns(), cpuMemStatus.getCpu30Conns(),
					cpuMemStatus.getCpu31Conns(), cpuMemStatus.getCpu32Conns());

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public void writeTmpCpuMemStatus(Integer adcIndex, Timestamp occurTime, OBDtoCpuMemStatus cpuMemStatus)
			throws OBException {
		String sqlText = "";

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			String sqlTextLatestDelete = String.format(" DELETE FROM TMP_FAULT_RESC_CPUMEM WHERE ADC_INDEX = %d;",
					adcIndex);
			db.executeUpdate(sqlTextLatestDelete);

			sqlText = String.format(
					" INSERT INTO TMP_FAULT_RESC_CPUMEM                                      \n"
							+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
							+ " CPU1_USAGE, CPU2_USAGE, CPU3_USAGE, CPU4_USAGE, CPU5_USAGE,            \n"
							+ " CPU6_USAGE, CPU7_USAGE, CPU8_USAGE, CPU9_USAGE, CPU10_USAGE, 			 \n"
							+ " CPU11_USAGE, CPU12_USAGE, CPU13_USAGE, CPU14_USAGE, CPU15_USAGE, 		 \n"
							+ " CPU16_USAGE, MEM_USAGE, CPU17_USAGE, CPU18_USAGE, CPU19_USAGE, 		 \n"
							+ " CPU20_USAGE, CPU21_USAGE, CPU22_USAGE, CPU23_USAGE, CPU24_USAGE, 		 \n"
							+ " CPU25_USAGE, CPU26_USAGE, CPU27_USAGE, CPU28_USAGE, CPU29_USAGE, 		 \n"
							+ " CPU30_USAGE, CPU31_USAGE, CPU32_USAGE, CPU2_CONNS, CPU3_CONNS, 		 \n"
							+ " CPU4_CONNS,  CPU5_CONNS, CPU6_CONNS, CPU7_CONNS, CPU8_CONNS, 			 \n"
							+ " CPU9_CONNS,  CPU10_CONNS, CPU11_CONNS, CPU12_CONNS, CPU13_CONNS, 		 \n"
							+ " CPU14_CONNS, CPU15_CONNS, CPU16_CONNS, CPU17_CONNS, CPU18_CONNS, 		 \n"
							+ " CPU19_CONNS, CPU20_CONNS, CPU21_CONNS, CPU22_CONNS, CPU23_CONNS, 		 \n"
							+ " CPU24_CONNS, CPU25_CONNS, CPU26_CONNS, CPU27_CONNS, CPU28_CONNS, 		 \n"
							+ " CPU29_CONNS, CPU30_CONNS, CPU31_CONNS, CPU32_CONNS)                    \n"
							+ " VALUES                                                                 \n"
							+ " (%d, %s,                                                               \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                                \n"
							+ " %d, %d, %d, %d)                                                        \n",
					adcIndex, OBParser.sqlString(occurTime), cpuMemStatus.getCpu1Usage(), cpuMemStatus.getCpu2Usage(),
					cpuMemStatus.getCpu3Usage(), cpuMemStatus.getCpu4Usage(), cpuMemStatus.getCpu5Usage(),
					cpuMemStatus.getCpu6Usage(), cpuMemStatus.getCpu7Usage(), cpuMemStatus.getCpu8Usage(),
					cpuMemStatus.getCpu9Usage(), cpuMemStatus.getCpu10Usage(), cpuMemStatus.getCpu11Usage(),
					cpuMemStatus.getCpu12Usage(), cpuMemStatus.getCpu13Usage(), cpuMemStatus.getCpu14Usage(),
					cpuMemStatus.getCpu15Usage(), cpuMemStatus.getCpu16Usage(), cpuMemStatus.getMemUsage(),
					cpuMemStatus.getCpu17Usage(), cpuMemStatus.getCpu18Usage(), cpuMemStatus.getCpu19Usage(),
					cpuMemStatus.getCpu20Usage(), cpuMemStatus.getCpu21Usage(), cpuMemStatus.getCpu22Usage(),
					cpuMemStatus.getCpu23Usage(), cpuMemStatus.getCpu24Usage(), cpuMemStatus.getCpu25Usage(),
					cpuMemStatus.getCpu26Usage(), cpuMemStatus.getCpu27Usage(), cpuMemStatus.getCpu28Usage(),
					cpuMemStatus.getCpu29Usage(), cpuMemStatus.getCpu30Usage(), cpuMemStatus.getCpu31Usage(),
					cpuMemStatus.getCpu32Usage(), cpuMemStatus.getCpu2Conns(), cpuMemStatus.getCpu3Conns(),
					cpuMemStatus.getCpu4Conns(), cpuMemStatus.getCpu5Conns(), cpuMemStatus.getCpu6Conns(),
					cpuMemStatus.getCpu7Conns(), cpuMemStatus.getCpu8Conns(), cpuMemStatus.getCpu9Conns(),
					cpuMemStatus.getCpu10Conns(), cpuMemStatus.getCpu11Conns(), cpuMemStatus.getCpu12Conns(),
					cpuMemStatus.getCpu13Conns(), cpuMemStatus.getCpu14Conns(), cpuMemStatus.getCpu15Conns(),
					cpuMemStatus.getCpu16Conns(), cpuMemStatus.getCpu17Conns(), cpuMemStatus.getCpu18Conns(),
					cpuMemStatus.getCpu19Conns(), cpuMemStatus.getCpu20Conns(), cpuMemStatus.getCpu21Conns(),
					cpuMemStatus.getCpu22Conns(), cpuMemStatus.getCpu23Conns(), cpuMemStatus.getCpu24Conns(),
					cpuMemStatus.getCpu25Conns(), cpuMemStatus.getCpu26Conns(), cpuMemStatus.getCpu27Conns(),
					cpuMemStatus.getCpu28Conns(), cpuMemStatus.getCpu29Conns(), cpuMemStatus.getCpu30Conns(),
					cpuMemStatus.getCpu31Conns(), cpuMemStatus.getCpu32Conns());

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

	public void writeCpuSPSessionMax(Integer adcIndex, Timestamp occurTime, OBDtoCpuMemStatus cpuMemStatus,
			OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(
					" UPDATE MNG_ADC SET SP_SESSION_MAX = %d                                 \n"
							+ " WHERE INDEX = %d                                                       \n",
					cpuMemStatus.getSpSessionMax(), adcIndex);

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public void writeCpuSPSessionMax(Integer adcIndex, Timestamp occurTime, OBDtoCpuMemStatus cpuMemStatus)
			throws OBException {
		String sqlText = "";

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			sqlText = String.format(
					" UPDATE MNG_ADC SET SP_SESSION_MAX = %d                                 \n"
							+ " WHERE INDEX = %d                                                       \n",
					cpuMemStatus.getSpSessionMax(), adcIndex);

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

	public void writeHddStatus(Integer adcIndex, Timestamp occurTime, Long hddTotal, Integer hddUsage, OBDatabase db)
			throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(
					" INSERT INTO LOG_RESC_HDD                                         \n"
							+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
							+ " HDD_TOTAL, HDD_USAGE)                                                  \n"
							+ " VALUES                                                                 \n"
							+ " (%d, %s,                                                               \n"
							+ " %d, %d)                                                                \n",
					adcIndex, OBParser.sqlString(occurTime), hddTotal, hddUsage);

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public void writeTemperatureStatus(Integer adcIndex, Timestamp occurTime, Integer status, OBDatabase db)
			throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(
					" INSERT INTO LOG_RESC_TEMPERATURE                                 \n"
							+ " (ADC_INDEX, OCCUR_TIME,                                                \n"
							+ " TEMP_STATUS)                                                           \n"
							+ " VALUES                                                                 \n"
							+ " (%d, %s,                                                               \n"
							+ " %d)                                                                    \n",
					adcIndex, OBParser.sqlString(occurTime), status);

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

//	public static void main(String[] args) {
//		try {
//			OBDtoADCObject object = new OBDtoADCObject();
//			object.setIndex(6);
//			object.setCategory(OBDtoADCObject.CATEGORY_ADC);
//
//			OBDtoFaultHWStatus data = new OBFaultMonitoringDB().getADCMonHWStatus(object);
//			System.out.println(data);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public OBDtoFaultHWStatus getADCMonHWStatus(OBDtoADCObject object) throws OBException {
		OBDtoFaultHWStatus retVal = new OBDtoFaultHWStatus();
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			Integer monInteval = new OBEnvManagementImpl().getAdcSyncInterval();// 초 단위.
			ArrayList<Integer> cpuList = getCpuLastStatus(object, monInteval);
			int cpuSize = cpuList.size();
			ArrayList<String> cpuNormal = new ArrayList<String>();
			ArrayList<String> cpuAbNormal = new ArrayList<String>();
//            ArrayList<String> cpuNot = new ArrayList<String>();

			Integer adcType = new OBAdcManagementImpl().getAdcType(object.getIndex());
			if (adcType != null) {
				if (adcType == OBDefine.ADC_TYPE_ALTEON) {
					for (int i = 1; i < cpuSize; i++) {
						String cpuName = "SP";
						int cpuVal = cpuList.get(i);

						if (cpuVal == 1) {
							cpuName += i;
							cpuNormal.add(cpuName);
						} else if (cpuVal == 2) {
							cpuName += i;
							cpuAbNormal.add(cpuName);
						}
					}
				} else {
					for (int i = 0; i < cpuSize; i++) {
						String cpuName = "CPU";
						int cpuVal = cpuList.get(i);

						if (cpuVal == 1) {
							cpuName += i;
							cpuNormal.add(cpuName);
						} else if (cpuVal == 2) {
							cpuName += i;
							cpuAbNormal.add(cpuName);
						}
					}
				}

				retVal.setCpuNormalList(cpuNormal);
				retVal.setCpuAbNormalList(cpuAbNormal);
			}

//            retVal.setCpuNotList(cpuNot);            

			ArrayList<Integer> fanStatusList = getFanLastStatus(object, monInteval);
			ArrayList<Integer> powerSupplyStatusList = getPowerSupplyStatus(object, monInteval);
			ArrayList<OBDtoFaultVlanInfo> vlanInfoList = getVlanInfoLastStatus(object, monInteval);
			ArrayList<OBDtoAdcPortStatus> portStatusList = getPortInfoLastStatus(object, monInteval);

			Timestamp beginTime = OBDateTime.getTimestampInterval(OBDateTime.toTimestamp(OBDateTime.now()),
					-monInteval * 2);

			sqlText = String.format(
					" SELECT A.OCCUR_TIME AS OCCUR_TIME, A.HDD_USAGE, B.TEMP_STATUS    \n"
							+ " FROM LOG_RESC_HDD              A                           \n"
							+ " LEFT JOIN LOG_RESC_TEMPERATURE B                           \n"
							+ " ON A.OCCUR_TIME=B.OCCUR_TIME                                     \n"
							+ " WHERE A.ADC_INDEX = %d                                             \n"
							+ " AND A.OCCUR_TIME >= %s                                           \n"
							+ " ORDER BY A.OCCUR_TIME DESC  LIMIT 1                              \n",
					object.getIndex(), OBParser.sqlString(beginTime));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal.setHddUsage(db.getInteger(rs, "HDD_USAGE"));
				retVal.setTemperatureStatus(db.getInteger(rs, "TEMP_STATUS"));
			} else {
				retVal.setHddUsage(-1);
				retVal.setTemperatureStatus(-1);
			}

			Integer hddThreshold = this.hddMaxUsage;
			String propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
					OBDefine.PROPERTY_KEY_SYSTEM_HDD_MAX_USAGE);
			if (propertyValue != null && !propertyValue.isEmpty())
				hddThreshold = Integer.parseInt(propertyValue);

			if (retVal.getHddUsage() >= 0) {
				if (retVal.getHddUsage() > hddThreshold)
					retVal.setHddStatus(OBDtoFaultHWStatus.STATUS_ABNORMAL);
				else
					retVal.setHddStatus(OBDtoFaultHWStatus.STATUS_NORMAL);
			} else {
				retVal.setHddStatus(-1);
			}

			retVal.setPowerSupplyStatusList(powerSupplyStatusList);
			retVal.setFanStatusList(fanStatusList);
			retVal.setPortStatusList(portStatusList);
			retVal.setVlanInfoList(vlanInfoList);
			retVal.setCpuStatusList(cpuList);
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

	private String makeTimePreSqlText(OBDtoSearch searchOption, String columnName) throws OBException {
		String retVal = "";

		if (searchOption == null)
			return retVal;

		if (searchOption.getPreToTime() == null) {
			retVal = String.format(" %s <= %s ", columnName,
					OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getToTime().getTime()))));
		} else {
			retVal = String.format(" %s <= %s ", columnName,
					OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getPreToTime().getTime()))));
		}

		if (searchOption.getPreFromTime() == null) {
			retVal += String.format(" AND %s >= %s ", columnName,
					OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getFromTime().getTime()))));
		} else {
			retVal += String.format(" AND %s >= %s ", columnName,
					OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getPreFromTime().getTime()))));
		}

		return retVal;
	}

	public OBDtoFaultPreBpsConnChart getBpsConnChartHistory(OBDtoADCObject object, String vsIndex, Integer svcPort,
			OBDtoSearch searchOption, OBDatabase db) throws OBException {
		OBDtoFaultPreBpsConnChart retVal = new OBDtoFaultPreBpsConnChart();
		ArrayList<OBDtoFaultBpsConnData> bpsConnData = new ArrayList<OBDtoFaultBpsConnData>();
		ArrayList<OBDtoFaultBpsConnData> preBpsConnData = new ArrayList<OBDtoFaultBpsConnData>();
		OBDtoFaultBpsConnData tmp = new OBDtoFaultBpsConnData();
		String sqlText = "";

		try {
			OBDtoSystemEnvAdditional env = new OBEnvManagementImpl().getAdditionalConfig();
			int interval = env.getIntervalAdcConfSync();
			Integer adcType = new OBAdcManagementImpl().getAdcType(object.getIndex());
			String index = vsIndex;
			if (adcType == OBDefine.ADC_TYPE_ALTEON)
				index = OBCommon.makeVSvcIndex(object.getIndex(), vsIndex, svcPort);

			Long difftime = searchOption.getFromTime().getTime() - searchOption.getPreFromTime().getTime();

			String sqlSearch = makeTimeSqlText(searchOption, "OCCUR_TIME");

			sqlText = String.format(
					" SELECT (to_timestamp(floor((extract('epoch' from OCCUR_TIME) / %d )) * %d) )  \n"
							+ " AS INTERVAL_TIME, BPS_IN, BPS_OUT, BPS_TOT, CONN_CURR                       \n"
							+ " FROM LOG_SVC_PERF_STATS                                                     \n"
							+ " WHERE OBJ_INDEX = %s AND %s                                                 \n"
							+ " GROUP BY INTERVAL_TIME, BPS_IN, BPS_OUT, BPS_TOT, CONN_CURR                 \n"
							+ " ORDER BY INTERVAL_TIME ASC                                                  \n",
					interval, interval, OBParser.sqlString(index), sqlSearch);

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				if (bpsConnData != null && !bpsConnData.isEmpty()) {
					Long diffValue = db.getTimestamp(rs, "INTERVAL_TIME").getTime() - tmp.getOccurTime().getTime();
					if (diffValue > (interval * 2500)) {
						tmp.getOccurTime().setTime(tmp.getOccurTime().getTime() + interval * 1000);
						tmp.setBpsInValue(0L);
						tmp.setBpsOutValue(0L);
						tmp.setBpsTotValue(0L);
						tmp.setConnCurrValue(0L);
						bpsConnData.add(tmp);

						tmp = new OBDtoFaultBpsConnData();
						tmp.setOccurTime(db.getTimestamp(rs, "INTERVAL_TIME"));
						tmp.getOccurTime().setTime(db.getTimestamp(rs, "INTERVAL_TIME").getTime() - interval * 1000);
						tmp.setBpsInValue(0L);
						tmp.setBpsOutValue(0L);
						tmp.setBpsTotValue(0L);
						tmp.setConnCurrValue(0L);

						bpsConnData.add(tmp);
					}
				}
				tmp = new OBDtoFaultBpsConnData();
				OBDtoFaultBpsConnData obj = new OBDtoFaultBpsConnData();
				obj.setOccurTime(db.getTimestamp(rs, "INTERVAL_TIME"));
				obj.setBpsInValue(db.getLong(rs, "BPS_IN"));
				obj.setBpsOutValue(db.getLong(rs, "BPS_OUT"));
				obj.setBpsTotValue(db.getLong(rs, "BPS_TOT"));
				obj.setConnCurrValue(db.getLong(rs, "CONN_CURR"));
				tmp.setOccurTime(db.getTimestamp(rs, "INTERVAL_TIME"));

				bpsConnData.add(obj);
			}

			String preSqlSearch = makeTimePreSqlText(searchOption, "OCCUR_TIME");

			sqlText = String.format(
					" SELECT (to_timestamp(floor((extract('epoch' from OCCUR_TIME) / %d )) * %d) )  \n"
							+ " AS INTERVAL_TIME, BPS_IN, BPS_OUT, BPS_TOT, CONN_CURR                       \n"
							+ " FROM LOG_SVC_PERF_STATS                                                     \n"
							+ " WHERE OBJ_INDEX = %s AND %s                                                 \n"
							+ " GROUP BY INTERVAL_TIME, BPS_IN, BPS_OUT, BPS_TOT, CONN_CURR                 \n"
							+ " ORDER BY INTERVAL_TIME ASC                                                  \n",
					interval, interval, OBParser.sqlString(index), preSqlSearch);

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoFaultBpsConnData obj = new OBDtoFaultBpsConnData();
				Timestamp time = new Timestamp(db.getTimestamp(rs, "INTERVAL_TIME").getTime() + difftime);
				obj.setOccurTime(time);
				obj.setBpsInValue(db.getLong(rs, "BPS_IN"));
				obj.setBpsOutValue(db.getLong(rs, "BPS_OUT"));
				obj.setBpsTotValue(db.getLong(rs, "BPS_TOT"));
				obj.setConnCurrValue(db.getLong(rs, "CONN_CURR"));

				preBpsConnData.add(obj);
			}

			int currentSize = bpsConnData.size();
			int preSize = preBpsConnData.size();
			if (preBpsConnData != null && !preBpsConnData.isEmpty()) {
				int sum = 0;
				for (int i = 0; i < currentSize; i++) {
					for (int j = sum; j < preSize; j++) {
						if (bpsConnData.get(i).getOccurTime().equals(preBpsConnData.get(j).getOccurTime())) {
							bpsConnData.get(i).setPreBpsInValue(preBpsConnData.get(j).getBpsInValue());
							bpsConnData.get(i).setPreBpsOutValue(preBpsConnData.get(j).getBpsOutValue());
							bpsConnData.get(i).setPreBpsTotValue(preBpsConnData.get(j).getBpsTotValue());
							bpsConnData.get(i).setPreConnCurrValue(preBpsConnData.get(j).getConnCurrValue());
							sum = j;
							break;
						}
					}
				}
			}
			retVal.setBpsConnDate(bpsConnData);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public OBDtoFaultPreBpsConnChart getBpsConnChartMaxAvgHistory(OBDtoADCObject object, String vsIndex,
			Integer svcPort, OBDtoSearch searchOption, OBDatabase db) throws OBException {
		OBDtoFaultPreBpsConnChart retVal = new OBDtoFaultPreBpsConnChart();
		String sqlText = "";

		try {
			Integer adcType = new OBAdcManagementImpl().getAdcType(object.getIndex());
			String index = vsIndex;
			if (adcType == OBDefine.ADC_TYPE_ALTEON)
				index = OBCommon.makeVSvcIndex(object.getIndex(), vsIndex, svcPort);

			String sqlSearch = makeTimeSqlText(searchOption, "OCCUR_TIME");
			String preSqlSearch = makeTimePreSqlText(searchOption, "OCCUR_TIME");

			sqlText = String.format(
					" SELECT A.MAX_BPS_IN, A.MAX_BPS_OUT, A.MAX_BPS_TOT, A.MAX_CONN_CURR,               \n"
							+ " A.AVG_BPS_IN, A.AVG_BPS_OUT, A.AVG_BPS_TOT, A.AVG_CONN_CURR,                    \n"
							+ " B.PRE_MAX_BPS_IN, B.PRE_MAX_BPS_OUT, B.PRE_MAX_BPS_TOT, B.PRE_MAX_CONN_CURR,    \n"
							+ " B.PRE_AVG_BPS_IN, B.PRE_AVG_BPS_OUT, B.PRE_AVG_BPS_TOT, B.PRE_AVG_CONN_CURR     \n"
							+ " FROM                                                                            \n"
							+ " (SELECT MAX(BPS_IN) AS MAX_BPS_IN, MAX(BPS_OUT) AS MAX_BPS_OUT,                 \n"
							+ " MAX(BPS_TOT) AS MAX_BPS_TOT, MAX(CONN_CURR) AS MAX_CONN_CURR,                   \n"
							+ " AVG(BPS_IN) AS AVG_BPS_IN, AVG(BPS_OUT) AS AVG_BPS_OUT,                         \n"
							+ " AVG(BPS_TOT) AS AVG_BPS_TOT, AVG(CONN_CURR) AS AVG_CONN_CURR                    \n"
							+ " FROM LOG_SVC_PERF_STATS                                                         \n"
							+ " WHERE OBJ_INDEX = %s AND BPS_IN >= 0 AND %s) A                                  \n"
							+ " LEFT JOIN                                                                       \n"
							+ "     (SELECT MAX(BPS_IN) AS PRE_MAX_BPS_IN, MAX(BPS_OUT) AS PRE_MAX_BPS_OUT,     \n"
							+ "     MAX(BPS_TOT) AS PRE_MAX_BPS_TOT, MAX(CONN_CURR) AS PRE_MAX_CONN_CURR,       \n"
							+ "     AVG(BPS_IN) AS PRE_AVG_BPS_IN, AVG(BPS_OUT) AS PRE_AVG_BPS_OUT,             \n"
							+ "     AVG(BPS_TOT) AS PRE_AVG_BPS_TOT, AVG(CONN_CURR) AS PRE_AVG_CONN_CURR        \n"
							+ "     FROM LOG_SVC_PERF_STATS                                                     \n"
							+ "     WHERE OBJ_INDEX = %s AND BPS_IN >= 0 AND %s) B                              \n"
							+ " ON 1=1                                                                          ",
					OBParser.sqlString(index), sqlSearch, OBParser.sqlString(index), preSqlSearch);

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next()) {
				retVal.setMaxBpsIn(db.getLong(rs, "MAX_BPS_IN"));
				retVal.setMaxBpsOut(db.getLong(rs, "MAX_BPS_OUT"));
				retVal.setMaxBpsTot(db.getLong(rs, "MAX_BPS_TOT"));
				retVal.setMaxConnCurr(db.getLong(rs, "MAX_CONN_CURR"));
				retVal.setAvgBpsIn(db.getLong(rs, "AVG_BPS_IN"));
				retVal.setAvgBpsOut(db.getLong(rs, "AVG_BPS_OUT"));
				retVal.setAvgBpsTot(db.getLong(rs, "AVG_BPS_TOT"));
				retVal.setAvgConnCurr(db.getLong(rs, "AVG_CONN_CURR"));

				retVal.setPreMaxBpsIn(db.getLong(rs, "PRE_MAX_BPS_IN"));
				retVal.setPreMaxBpsOut(db.getLong(rs, "PRE_MAX_BPS_OUT"));
				retVal.setPreMaxBpsTot(db.getLong(rs, "PRE_MAX_BPS_TOT"));
				retVal.setPreMaxConnCurr(db.getLong(rs, "PRE_MAX_CONN_CURR"));
				retVal.setPreAvgBpsIn(db.getLong(rs, "PRE_AVG_BPS_IN"));
				retVal.setPreAvgBpsOut(db.getLong(rs, "PRE_AVG_BPS_OUT"));
				retVal.setPreAvgBpsTot(db.getLong(rs, "PRE_AVG_BPS_TOT"));
				retVal.setPreAvgConnCurr(db.getLong(rs, "PRE_AVG_CONN_CURR"));
			}

			sqlText = String.format(
					" SELECT A.BPS_IN, A.BPS_OUT, A.BPS_TOT, A.CONN_CURR,                               \n"
							+ " B.PRE_BPS_IN, B.PRE_BPS_OUT, B.PRE_BPS_TOT, B.PRE_CONN_CURR                     \n"
							+ " FROM                                                                            \n"
							+ " (SELECT BPS_IN, BPS_OUT, BPS_TOT, CONN_CURR                                     \n"
							+ " FROM TMP_FAULT_SVC_PERF_STATS                                                   \n"
							+ " WHERE OBJ_INDEX = %s AND BPS_IN >= 0 AND %s) A                                  \n"
							+ " LEFT JOIN                                                                       \n"
							+ "     (SELECT BPS_IN AS PRE_BPS_IN, BPS_OUT AS PRE_BPS_OUT,                       \n"
							+ "     BPS_TOT AS PRE_BPS_TOT, CONN_CURR AS PRE_CONN_CURR                          \n"
							+ "     FROM LOG_SVC_PERF_STATS                                                     \n"
							+ "     WHERE OBJ_INDEX = %s AND BPS_IN >= 0 AND %s                                 \n"
							+ "     ORDER BY OCCUR_TIME DESC LIMIT 1) B                                         \n"
							+ " ON 1=1                                                                          \n",
					OBParser.sqlString(index), sqlSearch, OBParser.sqlString(index), preSqlSearch);

			rs = db.executeQuery(sqlText);

			if (rs.next()) {
				retVal.setCurrBpsIn(db.getLong(rs, "BPS_IN"));
				retVal.setCurrBpsOut(db.getLong(rs, "BPS_OUT"));
				retVal.setCurrBpsTot(db.getLong(rs, "BPS_TOT"));
				retVal.setCurrConnCurr(db.getLong(rs, "CONN_CURR"));

				retVal.setPreCurrBpsIn(db.getLong(rs, "PRE_BPS_IN"));
				retVal.setPreCurrBpsOut(db.getLong(rs, "PRE_BPS_OUT"));
				retVal.setPreCurrBpsTot(db.getLong(rs, "PRE_BPS_TOT"));
				retVal.setPreCurrConnCurr(db.getLong(rs, "PRE_CONN_CURR"));
			}

			if (retVal.getAvgBpsIn() >= 0 && retVal.getPreAvgBpsIn() >= 0) {
				retVal.setSubtractionAvgBpsIn(retVal.getAvgBpsIn() - retVal.getPreAvgBpsIn());
				retVal.setSubtractionAvgBpsInPercent(OBCommon.calcRateOfChange(retVal.getAvgBpsIn(),
						retVal.getPreAvgBpsIn(), retVal.getSubtractionAvgBpsIn()));
			}
			if (retVal.getMaxBpsIn() >= 0 && retVal.getPreMaxBpsIn() >= 0) {
				retVal.setSubtractionMaxBpsIn(retVal.getMaxBpsIn() - retVal.getPreMaxBpsIn());
				retVal.setSubtractionMaxBpsInPercent(OBCommon.calcRateOfChange(retVal.getMaxBpsIn(),
						retVal.getPreMaxBpsIn(), retVal.getSubtractionMaxBpsIn()));
			}
			if (retVal.getCurrBpsIn() >= 0 && retVal.getPreCurrBpsIn() >= 0) {
				retVal.setSubtractionCurrBpsIn(retVal.getCurrBpsIn() - retVal.getPreCurrBpsIn());
				retVal.setSubtractionCurrBpsInPercent(OBCommon.calcRateOfChange(retVal.getCurrBpsIn(),
						retVal.getPreCurrBpsIn(), retVal.getSubtractionCurrBpsIn()));
			}

			if (retVal.getAvgBpsOut() >= 0 && retVal.getPreAvgBpsOut() >= 0) {
				retVal.setSubtractionAvgBpsOut(retVal.getAvgBpsOut() - retVal.getPreAvgBpsOut());
				retVal.setSubtractionAvgBpsOutPercent(OBCommon.calcRateOfChange(retVal.getAvgBpsOut(),
						retVal.getPreAvgBpsOut(), retVal.getSubtractionAvgBpsOut()));
			}
			if (retVal.getMaxBpsOut() >= 0 && retVal.getPreMaxBpsOut() >= 0) {
				retVal.setSubtractionMaxBpsOut(retVal.getMaxBpsOut() - retVal.getPreMaxBpsOut());
				retVal.setSubtractionMaxBpsOutPercent(OBCommon.calcRateOfChange(retVal.getMaxBpsOut(),
						retVal.getPreMaxBpsOut(), retVal.getSubtractionMaxBpsOut()));
			}
			if (retVal.getCurrBpsOut() >= 0 && retVal.getPreCurrBpsOut() >= 0) {
				retVal.setSubtractionCurrBpsOut(retVal.getCurrBpsOut() - retVal.getPreCurrBpsOut());
				retVal.setSubtractionCurrBpsOutPercent(OBCommon.calcRateOfChange(retVal.getCurrBpsOut(),
						retVal.getPreCurrBpsOut(), retVal.getSubtractionCurrBpsOut()));
			}
			if (retVal.getAvgBpsTot() >= 0 && retVal.getPreAvgBpsTot() >= 0) {
				retVal.setSubtractionAvgBpsTot(retVal.getAvgBpsTot() - retVal.getPreAvgBpsTot());
				retVal.setSubtractionAvgBpsTotPercent(OBCommon.calcRateOfChange(retVal.getAvgBpsTot(),
						retVal.getPreAvgBpsTot(), retVal.getSubtractionAvgBpsTot()));
			}
			if (retVal.getMaxBpsTot() >= 0 && retVal.getPreMaxBpsTot() >= 0) {
				retVal.setSubtractionMaxBpsTot(retVal.getMaxBpsTot() - retVal.getPreMaxBpsTot());
				retVal.setSubtractionMaxBpsTotPercent(OBCommon.calcRateOfChange(retVal.getMaxBpsTot(),
						retVal.getPreMaxBpsTot(), retVal.getSubtractionMaxBpsTot()));
			}
			if (retVal.getCurrBpsTot() >= 0 && retVal.getPreCurrBpsTot() >= 0) {
				retVal.setSubtractionCurrBpsTot(retVal.getCurrBpsTot() - retVal.getPreCurrBpsTot());
				retVal.setSubtractionCurrBpsTotPercent(OBCommon.calcRateOfChange(retVal.getCurrBpsTot(),
						retVal.getPreCurrBpsTot(), retVal.getSubtractionCurrBpsTot()));
			}

			if (retVal.getAvgConnCurr() >= 0 && retVal.getPreAvgConnCurr() >= 0) {
				retVal.setSubtractionAvgConnCurr(retVal.getAvgConnCurr() - retVal.getPreAvgConnCurr());
				retVal.setSubtractionAvgConnCurrPercent(OBCommon.calcRateOfChange(retVal.getAvgConnCurr(),
						retVal.getPreAvgConnCurr(), retVal.getSubtractionAvgConnCurr()));
			}
			if (retVal.getMaxConnCurr() >= 0 && retVal.getPreMaxConnCurr() >= 0) {
				retVal.setSubtractionMaxConnCurr(retVal.getMaxConnCurr() - retVal.getPreMaxConnCurr());
				retVal.setSubtractionMaxConnCurrPercent(OBCommon.calcRateOfChange(retVal.getMaxConnCurr(),
						retVal.getPreMaxConnCurr(), retVal.getSubtractionMaxConnCurr()));
			}
			if (retVal.getCurrConnCurr() >= 0 && retVal.getPreCurrConnCurr() >= 0) {
				retVal.setSubtractionCurrConnCurr(retVal.getCurrConnCurr() - retVal.getPreCurrConnCurr());
				retVal.setSubtractionCurrConnCurrPercent(OBCommon.calcRateOfChange(retVal.getCurrConnCurr(),
						retVal.getPreCurrConnCurr(), retVal.getSubtractionCurrConnCurr()));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public OBDtoFaultPreBpsConnChart getSvcPerfVSrvMemberChartHistory(OBDtoADCObject object, OBDtoSearch searchOption,
			OBDatabase db) throws OBException {
		OBDtoFaultPreBpsConnChart retVal = new OBDtoFaultPreBpsConnChart();
		ArrayList<OBDtoFaultBpsConnData> bpsConnData = new ArrayList<OBDtoFaultBpsConnData>();
		ArrayList<OBDtoFaultBpsConnData> preBpsConnData = new ArrayList<OBDtoFaultBpsConnData>();
		String sqlText = "";

		try {
			OBDtoSystemEnvAdditional env = new OBEnvManagementImpl().getAdditionalConfig();
			int interval = env.getIntervalAdcConfSync();

			Long difftime = searchOption.getFromTime().getTime() - searchOption.getPreFromTime().getTime();

			String sqlSearch = makeTimeSqlText(searchOption, "OCCUR_TIME");

			sqlText = String.format(
					" SELECT (to_timestamp(floor((extract('epoch' from OCCUR_TIME) / %d )) * %d) )  \n"
							+ " AS INTERVAL_TIME, BPS_IN, BPS_OUT, BPS_TOT, CONN_CURR                       \n"
							+ " FROM LOG_SVC_MEMBER_PERF_STATS                                                     \n"
							+ " WHERE OBJ_INDEX = %s AND %s                                                 \n"
							+ " GROUP BY INTERVAL_TIME, BPS_IN, BPS_OUT, BPS_TOT, CONN_CURR                 \n"
							+ " ORDER BY INTERVAL_TIME ASC                                                  \n",
					interval, interval, OBParser.sqlString(object.getStrIndex()), sqlSearch);

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoFaultBpsConnData obj = new OBDtoFaultBpsConnData();
				obj.setOccurTime(db.getTimestamp(rs, "INTERVAL_TIME"));
				obj.setBpsInValue(db.getLong(rs, "BPS_IN"));
				obj.setBpsOutValue(db.getLong(rs, "BPS_OUT"));
				obj.setBpsTotValue(db.getLong(rs, "BPS_TOT"));
				obj.setConnCurrValue(db.getLong(rs, "CONN_CURR"));

				bpsConnData.add(obj);
			}

			String preSqlSearch = makeTimePreSqlText(searchOption, "OCCUR_TIME");

			sqlText = String.format(
					" SELECT (to_timestamp(floor((extract('epoch' from OCCUR_TIME) / %d )) * %d) )  \n"
							+ " AS INTERVAL_TIME, BPS_IN, BPS_OUT, BPS_TOT, CONN_CURR                       \n"
							+ " FROM LOG_SVC_MEMBER_PERF_STATS                                                     \n"
							+ " WHERE OBJ_INDEX = %s AND %s                                                 \n"
							+ " GROUP BY INTERVAL_TIME, BPS_IN, BPS_OUT, BPS_TOT, CONN_CURR                 \n"
							+ " ORDER BY INTERVAL_TIME ASC                                                  \n",
					interval, interval, OBParser.sqlString(object.getStrIndex()), preSqlSearch);

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoFaultBpsConnData obj = new OBDtoFaultBpsConnData();
				Timestamp time = new Timestamp(db.getTimestamp(rs, "INTERVAL_TIME").getTime() + difftime);
				obj.setOccurTime(time);
				obj.setBpsInValue(db.getLong(rs, "BPS_IN"));
				obj.setBpsOutValue(db.getLong(rs, "BPS_OUT"));
				obj.setBpsTotValue(db.getLong(rs, "BPS_TOT"));
				obj.setConnCurrValue(db.getLong(rs, "CONN_CURR"));

				preBpsConnData.add(obj);
			}

			int currentSize = bpsConnData.size();
			int preSize = preBpsConnData.size();
			if (preBpsConnData != null && !preBpsConnData.isEmpty()) {
				int sum = 0;
				for (int i = 0; i < currentSize; i++) {
					for (int j = sum; j < preSize; j++) {
						if (bpsConnData.get(i).getOccurTime().equals(preBpsConnData.get(j).getOccurTime())) {
							bpsConnData.get(i).setPreBpsInValue(preBpsConnData.get(j).getBpsInValue());
							bpsConnData.get(i).setPreBpsOutValue(preBpsConnData.get(j).getBpsOutValue());
							bpsConnData.get(i).setPreBpsTotValue(preBpsConnData.get(j).getBpsTotValue());
							bpsConnData.get(i).setPreConnCurrValue(preBpsConnData.get(j).getConnCurrValue());
							sum = j;
							break;
						}
					}
				}
			}
			retVal.setBpsConnDate(bpsConnData);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public OBDtoFaultPreBpsConnChart getSvcPerfVSrvMemberChartMaxAvgHistory(OBDtoADCObject object,
			OBDtoSearch searchOption, OBDatabase db) throws OBException {
		OBDtoFaultPreBpsConnChart retVal = new OBDtoFaultPreBpsConnChart();
		String sqlText = "";

		try {

			String sqlSearch = makeTimeSqlText(searchOption, "OCCUR_TIME");
			String preSqlSearch = makeTimePreSqlText(searchOption, "OCCUR_TIME");

			sqlText = String.format(
					" SELECT A.MAX_BPS_IN, A.MAX_BPS_OUT, A.MAX_BPS_TOT, A.MAX_CONN_CURR,               \n"
							+ " A.AVG_BPS_IN, A.AVG_BPS_OUT, A.AVG_BPS_TOT, A.AVG_CONN_CURR,                    \n"
							+ " B.PRE_MAX_BPS_IN, B.PRE_MAX_BPS_OUT, B.PRE_MAX_BPS_TOT, B.PRE_MAX_CONN_CURR,    \n"
							+ " B.PRE_AVG_BPS_IN, B.PRE_AVG_BPS_OUT, B.PRE_AVG_BPS_TOT, B.PRE_AVG_CONN_CURR     \n"
							+ " FROM                                                                            \n"
							+ " (SELECT MAX(BPS_IN) AS MAX_BPS_IN, MAX(BPS_OUT) AS MAX_BPS_OUT,                 \n"
							+ " MAX(BPS_TOT) AS MAX_BPS_TOT, MAX(CONN_CURR) AS MAX_CONN_CURR,                   \n"
							+ " AVG(BPS_IN) AS AVG_BPS_IN, AVG(BPS_OUT) AS AVG_BPS_OUT,                         \n"
							+ " AVG(BPS_TOT) AS AVG_BPS_TOT, AVG(CONN_CURR) AS AVG_CONN_CURR                    \n"
							+ " FROM LOG_SVC_MEMBER_PERF_STATS                                                         \n"
							+ " WHERE OBJ_INDEX = %s AND BPS_IN >= 0 AND %s) A                                  \n"
							+ " LEFT JOIN                                                                       \n"
							+ "     (SELECT MAX(BPS_IN) AS PRE_MAX_BPS_IN, MAX(BPS_OUT) AS PRE_MAX_BPS_OUT,     \n"
							+ "     MAX(BPS_TOT) AS PRE_MAX_BPS_TOT, MAX(CONN_CURR) AS PRE_MAX_CONN_CURR,       \n"
							+ "     AVG(BPS_IN) AS PRE_AVG_BPS_IN, AVG(BPS_OUT) AS PRE_AVG_BPS_OUT,             \n"
							+ "     AVG(BPS_TOT) AS PRE_AVG_BPS_TOT, AVG(CONN_CURR) AS PRE_AVG_CONN_CURR        \n"
							+ "     FROM LOG_SVC_MEMBER_PERF_STATS                                                     \n"
							+ "     WHERE OBJ_INDEX = %s AND BPS_IN >= 0 AND %s) B                              \n"
							+ " ON 1=1                                                                          ",
					OBParser.sqlString(object.getStrIndex()), sqlSearch, OBParser.sqlString(object.getStrIndex()),
					preSqlSearch);

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next()) {
				retVal.setMaxBpsIn(db.getLong(rs, "MAX_BPS_IN"));
				retVal.setMaxBpsOut(db.getLong(rs, "MAX_BPS_OUT"));
				retVal.setMaxBpsTot(db.getLong(rs, "MAX_BPS_TOT"));
				retVal.setMaxConnCurr(db.getLong(rs, "MAX_CONN_CURR"));
				retVal.setAvgBpsIn(db.getLong(rs, "AVG_BPS_IN"));
				retVal.setAvgBpsOut(db.getLong(rs, "AVG_BPS_OUT"));
				retVal.setAvgBpsTot(db.getLong(rs, "AVG_BPS_TOT"));
				retVal.setAvgConnCurr(db.getLong(rs, "AVG_CONN_CURR"));

				retVal.setPreMaxBpsIn(db.getLong(rs, "PRE_MAX_BPS_IN"));
				retVal.setPreMaxBpsOut(db.getLong(rs, "PRE_MAX_BPS_OUT"));
				retVal.setPreMaxBpsTot(db.getLong(rs, "PRE_MAX_BPS_TOT"));
				retVal.setPreMaxConnCurr(db.getLong(rs, "PRE_MAX_CONN_CURR"));
				retVal.setPreAvgBpsIn(db.getLong(rs, "PRE_AVG_BPS_IN"));
				retVal.setPreAvgBpsOut(db.getLong(rs, "PRE_AVG_BPS_OUT"));
				retVal.setPreAvgBpsTot(db.getLong(rs, "PRE_AVG_BPS_TOT"));
				retVal.setPreAvgConnCurr(db.getLong(rs, "PRE_AVG_CONN_CURR"));
			}

			sqlText = String.format(
					" SELECT A.BPS_IN, A.BPS_OUT, A.BPS_TOT, A.CONN_CURR,                               \n"
							+ " B.PRE_BPS_IN, B.PRE_BPS_OUT, B.PRE_BPS_TOT, B.PRE_CONN_CURR                     \n"
							+ " FROM                                                                            \n"
							+ " (SELECT BPS_IN, BPS_OUT, BPS_TOT, CONN_CURR                                     \n"
							+ " FROM TMP_FAULT_SVC_MEMBER_PERF_STATS                                            \n"
							+ " WHERE OBJ_INDEX = %s AND BPS_IN >= 0 AND %s) A                                  \n"
							+ " LEFT JOIN                                                                       \n"
							+ "     (SELECT BPS_IN AS PRE_BPS_IN, BPS_OUT AS PRE_BPS_OUT,                       \n"
							+ "     BPS_TOT AS PRE_BPS_TOT, CONN_CURR AS PRE_CONN_CURR                          \n"
							+ "     FROM LOG_SVC_MEMBER_PERF_STATS                                              \n"
							+ "     WHERE OBJ_INDEX = %s AND BPS_IN >= 0 AND %s                                 \n"
							+ "     ORDER BY OCCUR_TIME DESC LIMIT 1) B                                         \n"
							+ " ON 1=1                                                                          \n",
					OBParser.sqlString(object.getStrIndex()), sqlSearch, OBParser.sqlString(object.getStrIndex()),
					preSqlSearch);

			rs = db.executeQuery(sqlText);

			if (rs.next()) {
				retVal.setCurrBpsIn(db.getLong(rs, "BPS_IN"));
				retVal.setCurrBpsOut(db.getLong(rs, "BPS_OUT"));
				retVal.setCurrBpsTot(db.getLong(rs, "BPS_TOT"));
				retVal.setCurrConnCurr(db.getLong(rs, "CONN_CURR"));

				retVal.setPreCurrBpsIn(db.getLong(rs, "PRE_BPS_IN"));
				retVal.setPreCurrBpsOut(db.getLong(rs, "PRE_BPS_OUT"));
				retVal.setPreCurrBpsTot(db.getLong(rs, "PRE_BPS_TOT"));
				retVal.setPreCurrConnCurr(db.getLong(rs, "PRE_CONN_CURR"));
			}

			if (retVal.getAvgBpsIn() >= 0 && retVal.getPreAvgBpsIn() >= 0) {
				retVal.setSubtractionAvgBpsIn(retVal.getAvgBpsIn() - retVal.getPreAvgBpsIn());
				retVal.setSubtractionAvgBpsInPercent(OBCommon.calcRateOfChange(retVal.getAvgBpsIn(),
						retVal.getPreAvgBpsIn(), retVal.getSubtractionAvgBpsIn()));
			}
			if (retVal.getMaxBpsIn() >= 0 && retVal.getPreMaxBpsIn() >= 0) {
				retVal.setSubtractionMaxBpsIn(retVal.getMaxBpsIn() - retVal.getPreMaxBpsIn());
				retVal.setSubtractionMaxBpsInPercent(OBCommon.calcRateOfChange(retVal.getMaxBpsIn(),
						retVal.getPreMaxBpsIn(), retVal.getSubtractionMaxBpsIn()));
			}
			if (retVal.getCurrBpsIn() >= 0 && retVal.getPreCurrBpsIn() >= 0) {
				retVal.setSubtractionCurrBpsIn(retVal.getCurrBpsIn() - retVal.getPreCurrBpsIn());
				retVal.setSubtractionCurrBpsInPercent(OBCommon.calcRateOfChange(retVal.getCurrBpsIn(),
						retVal.getPreCurrBpsIn(), retVal.getSubtractionCurrBpsIn()));
			}

			if (retVal.getAvgBpsOut() >= 0 && retVal.getPreAvgBpsOut() >= 0) {
				retVal.setSubtractionAvgBpsOut(retVal.getAvgBpsOut() - retVal.getPreAvgBpsOut());
				retVal.setSubtractionAvgBpsOutPercent(OBCommon.calcRateOfChange(retVal.getAvgBpsOut(),
						retVal.getPreAvgBpsOut(), retVal.getSubtractionAvgBpsOut()));
			}
			if (retVal.getMaxBpsOut() >= 0 && retVal.getPreMaxBpsOut() >= 0) {
				retVal.setSubtractionMaxBpsOut(retVal.getMaxBpsOut() - retVal.getPreMaxBpsOut());
				retVal.setSubtractionMaxBpsOutPercent(OBCommon.calcRateOfChange(retVal.getMaxBpsOut(),
						retVal.getPreMaxBpsOut(), retVal.getSubtractionMaxBpsOut()));
			}
			if (retVal.getCurrBpsOut() >= 0 && retVal.getPreCurrBpsOut() >= 0) {
				retVal.setSubtractionCurrBpsOut(retVal.getCurrBpsOut() - retVal.getPreCurrBpsOut());
				retVal.setSubtractionCurrBpsOutPercent(OBCommon.calcRateOfChange(retVal.getCurrBpsOut(),
						retVal.getPreCurrBpsOut(), retVal.getSubtractionCurrBpsOut()));
			}
			if (retVal.getAvgBpsTot() >= 0 && retVal.getPreAvgBpsTot() >= 0) {
				retVal.setSubtractionAvgBpsTot(retVal.getAvgBpsTot() - retVal.getPreAvgBpsTot());
				retVal.setSubtractionAvgBpsTotPercent(OBCommon.calcRateOfChange(retVal.getAvgBpsTot(),
						retVal.getPreAvgBpsTot(), retVal.getSubtractionAvgBpsTot()));
			}
			if (retVal.getMaxBpsTot() >= 0 && retVal.getPreMaxBpsTot() >= 0) {
				retVal.setSubtractionMaxBpsTot(retVal.getMaxBpsTot() - retVal.getPreMaxBpsTot());
				retVal.setSubtractionMaxBpsTotPercent(OBCommon.calcRateOfChange(retVal.getMaxBpsTot(),
						retVal.getPreMaxBpsTot(), retVal.getSubtractionMaxBpsTot()));
			}
			if (retVal.getCurrBpsTot() >= 0 && retVal.getPreCurrBpsTot() >= 0) {
				retVal.setSubtractionCurrBpsTot(retVal.getCurrBpsTot() - retVal.getPreCurrBpsTot());
				retVal.setSubtractionCurrBpsTotPercent(OBCommon.calcRateOfChange(retVal.getCurrBpsTot(),
						retVal.getPreCurrBpsTot(), retVal.getSubtractionCurrBpsTot()));
			}

			if (retVal.getAvgConnCurr() >= 0 && retVal.getPreAvgConnCurr() >= 0) {
				retVal.setSubtractionAvgConnCurr(retVal.getAvgConnCurr() - retVal.getPreAvgConnCurr());
				retVal.setSubtractionAvgConnCurrPercent(OBCommon.calcRateOfChange(retVal.getAvgConnCurr(),
						retVal.getPreAvgConnCurr(), retVal.getSubtractionAvgConnCurr()));
			}
			if (retVal.getMaxConnCurr() >= 0 && retVal.getPreMaxConnCurr() >= 0) {
				retVal.setSubtractionMaxConnCurr(retVal.getMaxConnCurr() - retVal.getPreMaxConnCurr());
				retVal.setSubtractionMaxConnCurrPercent(OBCommon.calcRateOfChange(retVal.getMaxConnCurr(),
						retVal.getPreMaxConnCurr(), retVal.getSubtractionMaxConnCurr()));
			}
			if (retVal.getCurrConnCurr() >= 0 && retVal.getPreCurrConnCurr() >= 0) {
				retVal.setSubtractionCurrConnCurr(retVal.getCurrConnCurr() - retVal.getPreCurrConnCurr());
				retVal.setSubtractionCurrConnCurrPercent(OBCommon.calcRateOfChange(retVal.getCurrConnCurr(),
						retVal.getPreCurrConnCurr(), retVal.getSubtractionCurrConnCurr()));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public ArrayList<OBDtoDataObj> getResponseTimeHistory(OBDtoADCObject object, String vsIndex, Integer svcPort,
			OBDtoSearch searchOption, OBDatabase db) throws OBException {
		ArrayList<OBDtoDataObj> retVal = new ArrayList<OBDtoDataObj>();
		String sqlText = "";

		try {
			String sqlSearch = makeTimeSqlText(searchOption, "OCCUR_TIME");
			Integer adcType = new OBAdcManagementImpl().getAdcType(object.getIndex());
			String index = vsIndex;
			if (adcType == OBDefine.ADC_TYPE_ALTEON)
				index = OBCommon.makeVSvcIndex(object.getIndex(), vsIndex, svcPort);

			// String index = OBCommon.makeVSvcIndex(object.getIndex(), vsIndex, svcPort);
			sqlText = String.format(" SELECT OBJ_INDEX, OCCUR_TIME, RESPONSE_TIME     \n"
					+ " FROM LOG_SVC_PERF_RESP_TIME                         \n"
					+ " WHERE OBJ_INDEX = %s                                \n", OBParser.sqlString(index));

			if (sqlSearch != null)
				sqlText += " AND " + sqlSearch;

			sqlText += " ORDER BY OCCUR_TIME ASC ";

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {

				OBDtoDataObj obj = new OBDtoDataObj();
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				Long respValue = db.getLong(rs, "RESPONSE_TIME");
				if (respValue == -1)
					continue;
				obj.setValue(db.getLong(rs, "RESPONSE_TIME"));

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

	public OBDtoFaultPreBpsConnChart getResponseTimeChartHistory(OBDtoADCObject object, String vsIndex, Integer svcPort,
			OBDtoSearch searchOption, OBDatabase db) throws OBException {
		OBDtoFaultPreBpsConnChart retVal = new OBDtoFaultPreBpsConnChart();
		ArrayList<OBDtoFaultBpsConnData> responseData = new ArrayList<OBDtoFaultBpsConnData>();
		ArrayList<OBDtoFaultBpsConnData> preResponseData = new ArrayList<OBDtoFaultBpsConnData>();
		String sqlText = "";

		try {
			OBDtoSystemEnvAdditional env = new OBEnvManagementImpl().getAdditionalConfig();
			int interval = env.getIntervalAdcConfSync();

			Integer adcType = new OBAdcManagementImpl().getAdcType(object.getIndex());
			String index = vsIndex;
			if (adcType == OBDefine.ADC_TYPE_ALTEON)
				index = OBCommon.makeVSvcIndex(object.getIndex(), vsIndex, svcPort);

			Long difftime = searchOption.getFromTime().getTime() - searchOption.getPreFromTime().getTime();

			String sqlSearch = makeTimeSqlText(searchOption, "OCCUR_TIME");

			sqlText = String.format(
					" SELECT (to_timestamp(floor((extract('epoch' from OCCUR_TIME) / %d )) * %d) )  \n"
							+ " AS INTERVAL_TIME, RESPONSE_TIME                                             \n"
							+ " FROM LOG_SVC_PERF_RESP_TIME                                                 \n"
							+ " WHERE OBJ_INDEX = %s AND %s                                                 \n"
							+ " GROUP BY INTERVAL_TIME, RESPONSE_TIME                                       \n"
							+ " ORDER BY INTERVAL_TIME ASC                                                  \n",
					interval, interval, OBParser.sqlString(index), sqlSearch);

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoFaultBpsConnData obj = new OBDtoFaultBpsConnData();
				obj.setOccurTime(db.getTimestamp(rs, "INTERVAL_TIME"));
				obj.setRespTimeValue(db.getInteger(rs, "RESPONSE_TIME"));

				responseData.add(obj);
			}

			String preSqlSearch = makeTimePreSqlText(searchOption, "OCCUR_TIME");

			sqlText = String.format(
					" SELECT (to_timestamp(floor((extract('epoch' from OCCUR_TIME) / %d )) * %d) )  \n"
							+ " AS INTERVAL_TIME, RESPONSE_TIME                                             \n"
							+ " FROM LOG_SVC_PERF_RESP_TIME                                                 \n"
							+ " WHERE OBJ_INDEX = %s AND %s                                                 \n"
							+ " GROUP BY INTERVAL_TIME, RESPONSE_TIME                                       \n"
							+ " ORDER BY INTERVAL_TIME ASC                                                  \n",
					interval, interval, OBParser.sqlString(index), preSqlSearch);

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoFaultBpsConnData obj = new OBDtoFaultBpsConnData();
				Timestamp time = new Timestamp(db.getTimestamp(rs, "INTERVAL_TIME").getTime() + difftime);
				obj.setOccurTime(time);
				obj.setRespTimeValue(db.getInteger(rs, "RESPONSE_TIME"));

				preResponseData.add(obj);
			}

			int currentSize = responseData.size();
			int preSize = preResponseData.size();
			if (preResponseData != null && !preResponseData.isEmpty()) {
				int sum = 0;
				for (int i = 0; i < currentSize; i++) {
					for (int j = sum; j < preSize; j++) {
						if (responseData.get(i).getOccurTime().equals(preResponseData.get(j).getOccurTime())) {
							responseData.get(i).setPreBpsInValue(preResponseData.get(j).getBpsInValue());
							responseData.get(i).setPreBpsOutValue(preResponseData.get(j).getBpsOutValue());
							responseData.get(i).setPreBpsTotValue(preResponseData.get(j).getBpsTotValue());
							responseData.get(i).setPreConnCurrValue(preResponseData.get(j).getConnCurrValue());
							sum = j;
							break;
						}
					}
				}
			}
			retVal.setBpsConnDate(responseData);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public OBDtoFaultPreBpsConnChart getResponseTimeChartMaxAvgHistory(OBDtoADCObject object, String vsIndex,
			Integer svcPort, OBDtoSearch searchOption, OBDatabase db) throws OBException {
		OBDtoFaultPreBpsConnChart retVal = new OBDtoFaultPreBpsConnChart();
		String sqlText = "";

		try {
			Integer adcType = new OBAdcManagementImpl().getAdcType(object.getIndex());
			String index = vsIndex;
			if (adcType == OBDefine.ADC_TYPE_ALTEON)
				index = OBCommon.makeVSvcIndex(object.getIndex(), vsIndex, svcPort);

			String sqlSearch = makeTimeSqlText(searchOption, "OCCUR_TIME");
			String preSqlSearch = makeTimePreSqlText(searchOption, "OCCUR_TIME");

			sqlText = String.format(
					" SELECT A.MAX_RESP, A.AVG_RESP, B.PRE_MAX_RESP, B.PRE_AVG_RESP                       \n"
							+ " FROM                                                                                \n"
							+ " (SELECT MAX(RESPONSE_TIME) AS MAX_RESP, AVG(RESPONSE_TIME) AS AVG_RESP              \n"
							+ " FROM LOG_SVC_PERF_RESP_TIME                                                         \n"
							+ " WHERE OBJ_INDEX = %s AND RESPONSE_TIME >= 0 AND %s) A                               \n"
							+ " LEFT JOIN                                                                           \n"
							+ "     (SELECT MAX(RESPONSE_TIME) AS PRE_MAX_RESP, AVG(RESPONSE_TIME) AS PRE_AVG_RESP  \n"
							+ "     FROM LOG_SVC_PERF_RESP_TIME                                                     \n"
							+ "     WHERE OBJ_INDEX = %s AND RESPONSE_TIME >= 0 AND %s) B                           \n"
							+ "     ON 1=1                                                                          \n",
					OBParser.sqlString(index), sqlSearch, OBParser.sqlString(index), preSqlSearch);

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next()) {
				retVal.setMaxRespTime(db.getInteger(rs, "MAX_RESP"));
				retVal.setAvgRespTime(db.getInteger(rs, "AVG_RESP"));

				retVal.setPreMaxRespTime(db.getInteger(rs, "PRE_MAX_RESP"));
				retVal.setPreAvgRespTime(db.getInteger(rs, "PRE_AVG_RESP"));
			}

			sqlText = String.format(
					" SELECT A.RESPONSE_TIME, B.PRE_RESPONSE_TIME                                       \n"
							+ " FROM                                                                            \n"
							+ " (SELECT RESPONSE_TIME                                                           \n"
							+ " FROM TMP_FAULT_SVC_PERF_RESP_TIME                                               \n"
							+ " WHERE OBJ_INDEX = %s AND RESPONSE_TIME >= 0 AND %s) A                           \n"
							+ " LEFT JOIN                                                                       \n"
							+ " (SELECT RESPONSE_TIME AS PRE_RESPONSE_TIME                                      \n"
							+ " FROM LOG_SVC_PERF_RESP_TIME                                                     \n"
							+ " WHERE OBJ_INDEX = %s AND RESPONSE_TIME >= 0 AND %s                              \n"
							+ " ORDER BY OCCUR_TIME DESC LIMIT 1) B                                             \n"
							+ " ON 1=1                                                                          \n",
					OBParser.sqlString(index), sqlSearch, OBParser.sqlString(index), preSqlSearch);

			rs = db.executeQuery(sqlText);

			if (rs.next()) {
				retVal.setCurrRespTime(db.getInteger(rs, "RESPONSE_TIME"));
				retVal.setPreCurrRespTime(db.getInteger(rs, "RESPONSE_TIME"));
			}

			if (retVal.getAvgRespTime() >= 0 && retVal.getPreAvgRespTime() >= 0) {

				retVal.setSubtractionAvgRespTime(retVal.getAvgRespTime() - retVal.getPreAvgRespTime());
				retVal.setSubtractionAvgRespTimePercent(OBCommon.calcRateOfChange(retVal.getAvgRespTime(),
						retVal.getPreAvgRespTime(), retVal.getSubtractionAvgRespTime()));
			}
			if (retVal.getMaxRespTime() >= 0 && retVal.getPreMaxRespTime() >= 0) {
				retVal.setSubtractionMaxRespTime(retVal.getMaxRespTime() - retVal.getPreMaxRespTime());
				retVal.setSubtractionMaxRespTimePercent(OBCommon.calcRateOfChange(retVal.getMaxRespTime(),
						retVal.getPreMaxRespTime(), retVal.getSubtractionMaxRespTime()));

			}
			if (retVal.getCurrRespTime() >= 0 && retVal.getPreCurrRespTime() >= 0) {
				retVal.setSubtractionCurrRespTime(retVal.getCurrRespTime() - retVal.getPreCurrRespTime());
				retVal.setSubtractionCurrRespTimePercent(OBCommon.calcRateOfChange(retVal.getCurrRespTime(),
						retVal.getPreCurrRespTime(), retVal.getSubtractionCurrRespTime()));
			}

		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public OBDtoFaultDataObj getADCMonBpsHistory(OBDtoADCObject object, OBDtoSearch searchOption, OBDatabase db)
			throws OBException {
		OBDtoFaultDataObj retVal = new OBDtoFaultDataObj();
		String sqlText = "";
		int adcType = new OBAdcManagementImpl().getAdcType(object.getIndex());
		try {
			String sqlTime = makeTimeSqlText(searchOption, "OCCUR_TIME");
			if (adcType == OBDefine.ADC_TYPE_ALTEON) // Alteon만 ADC 트래픽 = (in+out)/2
			{
				sqlText = String.format(" SELECT OCCUR_TIME, (BPS_IN+BPS_OUT)/2 BPS \n"
						+ " FROM LOG_ADC_PERF_STATS 			        \n"
						+ " WHERE ADC_INDEX = %d                      \n", object.getIndex());
			} else {
				sqlText = String.format(" SELECT OCCUR_TIME, BPS_IN BPS             \n"
						+ " FROM LOG_ADC_PERF_STATS 			        \n"
						+ " WHERE ADC_INDEX = %d                      \n", object.getIndex());
			}

			if (!sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			sqlText += "ORDER BY OCCUR_TIME ASC ";

			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoDataObj> history = new ArrayList<OBDtoDataObj>();

			// Timestamp prevDate = OBDateTime.getPrevDayTimestamp();
			// if(searchOption!=null && searchOption.getToTime()!=null)
			// prevDate = OBDateTime.getPrevDayTimestamp(new
			// Timestamp(searchOption.getToTime().getTime()));

			Long currValue = -1L;
			// Long prevValue=-1L;
			Long maxValue = -1L;
			while (rs.next()) {
				OBDtoDataObj obj = new OBDtoDataObj();
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setValue(db.getLong(rs, "BPS"));
				currValue = obj.getValue();
				if (maxValue < obj.getValue())
					maxValue = obj.getValue();
				// if(prevValue==-1L)
				// {
				// if(obj.getOccurTime().getTime()<prevDate.getTime())
				// prevValue = obj.getValue();
				// }
				history.add(obj);
			}
			retVal.setCurrValue(currValue);
			retVal.setHistory(history);
			retVal.setMaxValue(maxValue);

			Date now = new Date();
			if (searchOption.getToTime() != null)
				now = searchOption.getToTime();
			retVal.setPrevValue(getADCMonBpsPrevAvgData(object.getIndex(), new Timestamp(now.getTime()), db));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private Long getADCMonBpsPrevAvgData(Integer adcIndex, Timestamp occurTime, OBDatabase db) throws OBException {
		Long retVal = -1L;
		String sqlText = "";
		try {
			Timestamp prevEndTime = OBDateTime.getPrevDayTimestamp();
			Timestamp prevStartTime = new Timestamp(prevEndTime.getTime() - 24 * 60 * 60 * 1000);

			sqlText = String.format(
					" SELECT AVG(BPS_IN) AS AVG                   \n" + " FROM LOG_ADC_PERF_STATS 			  \n"
							+ " WHERE ADC_INDEX = %d                        \n"
							+ " AND OCCUR_TIME >= %s AND OCCUR_TIME <= %s \n",
					adcIndex, OBParser.sqlString(prevStartTime), OBParser.sqlString(prevEndTime));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = db.getLong(rs, "AVG");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public OBDtoCpuMemStatus getADCMonCPUMemoryLastInfoWithin10Sec(OBDtoADCObject object, OBDatabase db)
			throws OBException {
		OBDtoCpuMemStatus retVal = null;
		String sqlText = "";
		try {
			Timestamp nowTime = OBDateTime.toTimestamp(OBDateTime.now());
			Timestamp startTime = new Timestamp(nowTime.getTime() - 10000);// 10*1000 means 10sec
			String sqlTime = String.format("OCCUR_TIME >= %s", OBParser.sqlString(startTime));
			sqlText = String.format(
					" SELECT OCCUR_TIME,                                                           \n"
							+ " CPU1_USAGE, CPU2_USAGE, CPU3_USAGE, CPU4_USAGE, CPU5_USAGE,                  \n"
							+ " CPU6_USAGE, CPU7_USAGE, CPU8_USAGE, CPU9_USAGE, CPU10_USAGE,                 \n"
							+ " CPU11_USAGE, CPU12_USAGE, CPU13_USAGE, CPU14_USAGE, CPU15_USAGE, CPU16_USAGE,\n"
							+ " MEM_USAGE                                                                    \n"
							+ " FROM LOG_RESC_CPUMEM                                                   \n"
							+ " WHERE ADC_INDEX = %d                                                         \n",
					object.getIndex());

			if (!sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			sqlText += "ORDER BY OCCUR_TIME DESC LIMIT 1 ";

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				retVal = new OBDtoCpuMemStatus();
				retVal.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				retVal.setCpu1Usage(db.getInteger(rs, "CPU1_USAGE"));
				retVal.setCpu2Usage(db.getInteger(rs, "CPU2_USAGE"));
				retVal.setCpu3Usage(db.getInteger(rs, "CPU3_USAGE"));
				retVal.setCpu4Usage(db.getInteger(rs, "CPU4_USAGE"));
				retVal.setCpu5Usage(db.getInteger(rs, "CPU5_USAGE"));
				retVal.setCpu6Usage(db.getInteger(rs, "CPU6_USAGE"));
				retVal.setCpu7Usage(db.getInteger(rs, "CPU7_USAGE"));
				retVal.setCpu8Usage(db.getInteger(rs, "CPU8_USAGE"));
				retVal.setCpu9Usage(db.getInteger(rs, "CPU9_USAGE"));
				retVal.setCpu10Usage(db.getInteger(rs, "CPU10_USAGE"));
				retVal.setCpu11Usage(db.getInteger(rs, "CPU11_USAGE"));
				retVal.setCpu12Usage(db.getInteger(rs, "CPU12_USAGE"));
				retVal.setCpu13Usage(db.getInteger(rs, "CPU13_USAGE"));
				retVal.setCpu14Usage(db.getInteger(rs, "CPU14_USAGE"));
				retVal.setCpu15Usage(db.getInteger(rs, "CPU15_USAGE"));
				retVal.setCpu16Usage(db.getInteger(rs, "CPU16_USAGE"));
				retVal.setMemUsage(db.getInteger(rs, "MEM_USAGE"));
			} else {
				retVal = new OBDtoCpuMemStatus();
				retVal.setOccurTime(nowTime);
				retVal.setCpu1Usage(-1);
				retVal.setCpu2Usage(-1);
				retVal.setCpu3Usage(-1);
				retVal.setCpu4Usage(-1);
				retVal.setCpu5Usage(-1);
				retVal.setCpu6Usage(-1);
				retVal.setCpu7Usage(-1);
				retVal.setCpu8Usage(-1);
				retVal.setCpu9Usage(-1);
				retVal.setCpu10Usage(-1);
				retVal.setCpu11Usage(-1);
				retVal.setCpu12Usage(-1);
				retVal.setCpu13Usage(-1);
				retVal.setCpu14Usage(-1);
				retVal.setCpu15Usage(-1);
				retVal.setCpu16Usage(-1);
				retVal.setMemUsage(-1);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public OBDtoFaultDataObj getADCMonMemoryHistory(OBDtoADCObject object, OBDtoSearch searchOption, OBDatabase db)
			throws OBException {
		OBDtoFaultDataObj retVal = new OBDtoFaultDataObj();
		String sqlText = "";
		try {
			String sqlTime = makeTimeSqlText(searchOption, "OCCUR_TIME");
			sqlText = String.format(" SELECT OCCUR_TIME, MEM_USAGE	             \n"
					+ " FROM LOG_RESC_CPUMEM  				 \n" + " WHERE ADC_INDEX = %d                       \n",
					object.getIndex());

			if (!sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			sqlText += "ORDER BY OCCUR_TIME ASC ";

			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoDataObj> history = new ArrayList<OBDtoDataObj>();

			Long currValue = -1L;
			Long maxValue = -1L;
			while (rs.next()) {
				OBDtoDataObj obj = new OBDtoDataObj();
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setValue(db.getLong(rs, "MEM_USAGE"));
				currValue = obj.getValue();
				if (maxValue < obj.getValue())
					maxValue = obj.getValue();
				history.add(obj);
			}
			retVal.setCurrValue(currValue);
			retVal.setHistory(history);
			retVal.setMaxValue(maxValue);

			Date now = new Date();
			if (searchOption.getToTime() != null)
				now = searchOption.getToTime();

			retVal.setPrevValue(getADCMonMemoryPrevAvgData(object.getIndex(), new Timestamp(now.getTime()), db));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private Long getADCMonMemoryPrevAvgData(Integer adcIndex, Timestamp occurTime, OBDatabase db) throws OBException {
		Long retVal = -1L;
		String sqlText = "";
		try {
			Timestamp prevEndTime = OBDateTime.getPrevDayTimestamp();
			Timestamp prevStartTime = new Timestamp(prevEndTime.getTime() - 24 * 60 * 60 * 1000);

			sqlText = String.format(
					" SELECT AVG(MEM_USAGE) AS AVG                \n" + " FROM LOG_RESC_CPUMEM 			      \n"
							+ " WHERE ADC_INDEX = %d                        \n"
							+ " AND OCCUR_TIME >= %s AND OCCUR_TIME <= %s   \n",
					adcIndex, OBParser.sqlString(prevStartTime), OBParser.sqlString(prevEndTime));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = db.getLong(rs, "AVG");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public OBDtoFaultDataObj getADCMonHttpReqestHistory(OBDtoADCObject object, OBDtoSearch searchOption, OBDatabase db)
			throws OBException {
		OBDtoFaultDataObj retVal = new OBDtoFaultDataObj();
		String sqlText = "";
		try {
			String sqlTime = makeTimeSqlText(searchOption, "OCCUR_TIME");
			sqlText = String.format(" SELECT OCCUR_TIME, HTTP_REQ_PS	             \n"
					+ " FROM LOG_ADC_PERFORMANCE  				 \n" + " WHERE ADC_INDEX = %d                       \n",
					object.getIndex());

			if (!sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			sqlText += "ORDER BY OCCUR_TIME ASC ";

			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoDataObj> history = new ArrayList<OBDtoDataObj>();

			Long currValue = -1L;
			Long maxValue = -1L;
			while (rs.next()) {
				OBDtoDataObj obj = new OBDtoDataObj();
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setValue(db.getLong(rs, "HTTP_REQ_PS"));
				currValue = obj.getValue();
				if (maxValue < obj.getValue())
					maxValue = obj.getValue();
				history.add(obj);
			}
			retVal.setCurrValue(currValue);
			retVal.setHistory(history);
			retVal.setMaxValue(maxValue);

			Date now = new Date();
			if (searchOption.getToTime() != null)
				now = searchOption.getToTime();

			retVal.setPrevValue(getADCMonHttpReqestPrevAvgData(object.getIndex(), new Timestamp(now.getTime()), db));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private Long getADCMonHttpReqestPrevAvgData(Integer adcIndex, Timestamp occurTime, OBDatabase db)
			throws OBException {
		Long retVal = -1L;
		String sqlText = "";
		try {
			Timestamp prevEndTime = OBDateTime.getPrevDayTimestamp();
			Timestamp prevStartTime = new Timestamp(prevEndTime.getTime() - 24 * 60 * 60 * 1000);

			sqlText = String.format(
					" SELECT AVG(HTTP_REQ_PS) AS AVG              \n"
							+ " FROM LOG_ADC_PERFORMANCE      		      \n"
							+ " WHERE ADC_INDEX = %d                        \n"
							+ " AND OCCUR_TIME >= %s AND OCCUR_TIME <= %s   \n",
					adcIndex, OBParser.sqlString(prevStartTime), OBParser.sqlString(prevEndTime));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = db.getLong(rs, "AVG");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public OBDtoFaultDataObj getADCMonSSLTransactionHistory(OBDtoADCObject object, OBDtoSearch searchOption,
			OBDatabase db) throws OBException {
		OBDtoFaultDataObj retVal = new OBDtoFaultDataObj();
		String sqlText = "";
		try {
			String sqlTime = makeTimeSqlText(searchOption, "OCCUR_TIME");
			sqlText = String.format(" SELECT OCCUR_TIME, SSL_TRANS_PS            \n"
					+ " FROM LOG_ADC_PERFORMANCE  				 \n" + " WHERE ADC_INDEX = %d                       \n",
					object.getIndex());

			if (!sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			sqlText += "ORDER BY OCCUR_TIME ASC ";

			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoDataObj> history = new ArrayList<OBDtoDataObj>();

			Long currValue = -1L;
			Long maxValue = -1L;
			while (rs.next()) {
				OBDtoDataObj obj = new OBDtoDataObj();
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setValue(db.getLong(rs, "SSL_TRANS_PS"));
				currValue = obj.getValue();
				if (maxValue < obj.getValue())
					maxValue = obj.getValue();
				history.add(obj);
			}
			retVal.setCurrValue(currValue);
			retVal.setHistory(history);
			retVal.setMaxValue(maxValue);

			Date now = new Date();
			if (searchOption.getToTime() != null)
				now = searchOption.getToTime();

			retVal.setPrevValue(
					getADCMonSSLTransactionPrevAvgData(object.getIndex(), new Timestamp(now.getTime()), db));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private Long getADCMonSSLTransactionPrevAvgData(Integer adcIndex, Timestamp occurTime, OBDatabase db)
			throws OBException {
		Long retVal = -1L;
		String sqlText = "";
		try {
			Timestamp prevEndTime = OBDateTime.getPrevDayTimestamp();
			Timestamp prevStartTime = new Timestamp(prevEndTime.getTime() - 24 * 60 * 60 * 1000);

			sqlText = String.format(
					" SELECT AVG(SSL_TRANS_PS) AS AVG             \n"
							+ " FROM LOG_ADC_PERFORMANCE      		      \n"
							+ " WHERE ADC_INDEX = %d                        \n"
							+ " AND OCCUR_TIME >= %s AND OCCUR_TIME <= %s   \n",
					adcIndex, OBParser.sqlString(prevStartTime), OBParser.sqlString(prevEndTime));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = db.getLong(rs, "AVG");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public ArrayList<OBDtoAdcMonCpuDataObj> getADCMonCpuHistoryGroup(OBDtoADCObject object, OBDtoSearch searchOption,
			OBDatabase db) throws OBException {
		ArrayList<OBDtoAdcMonCpuDataObj> retVal = new ArrayList<OBDtoAdcMonCpuDataObj>();

		ArrayList<Integer> adcList = new OBAdcManagementImpl().getAdcIndexListInGroup(object.getIndex());
		for (Integer adcIndex : adcList) {
			object.setIndex(adcIndex);
			retVal.add(getADCMonCpuData(object, searchOption, db));
		}

		return retVal;
	}

	public OBDtoAdcMonCpuDataObj getADCMonCpuData(OBDtoADCObject object, OBDtoSearch searchOption, OBDatabase db)
			throws OBException {
		OBDtoAdcMonCpuDataObj retVal = new OBDtoAdcMonCpuDataObj();
		String sqlText = "";
		try {
			String sqlTime = makeTimeSqlText(searchOption, "OCCUR_TIME");
			sqlText = String.format(
					" SELECT OCCUR_TIME, CPU1_USAGE,  CPU2_USAGE,  CPU3_USAGE,  CPU4_USAGE,  CPU5_USAGE,  CPU6_USAGE, \n"
							+ "      CPU7_USAGE,  CPU8_USAGE,  CPU9_USAGE,  CPU10_USAGE, CPU11_USAGE, CPU12_USAGE, CPU13_USAGE, \n"
							+ "      CPU14_USAGE, CPU15_USAGE, CPU16_USAGE, CPU17_USAGE, CPU18_USAGE, CPU19_USAGE, CPU20_USAGE, \n"
							+ "      CPU21_USAGE, CPU22_USAGE, CPU23_USAGE, CPU24_USAGE, CPU25_USAGE, CPU26_USAGE, CPU27_USAGE, \n"
							+ "      CPU28_USAGE, CPU29_USAGE, CPU30_USAGE, CPU31_USAGE, CPU32_USAGE                            \n"
							+ " FROM LOG_RESC_CPUMEM                                                   						  \n"
							+ " WHERE ADC_INDEX = %d 																			  \n",
					object.getIndex());

			if (!sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			sqlText += "ORDER BY OCCUR_TIME ASC ";

			ResultSet rs = db.executeQuery(sqlText);

			ArrayList<OBDtoAdcMonCpuHistory> data = new ArrayList<OBDtoAdcMonCpuHistory>();
			while (rs.next()) {
				OBDtoAdcMonCpuHistory obj = new OBDtoAdcMonCpuHistory();
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				getAvgCpuData(db, rs, obj, object.getVendor());
				data.add(obj);
			}

			Date now = new Date();
			if (searchOption.getToTime() != null)
				now = searchOption.getToTime();

			if (data == null || data.isEmpty()) {
				retVal.setPrevValue(getADCMonCpuPrevAvgData(object.getIndex(), new Timestamp(now.getTime()), db));
				retVal.setHistory(data);
				return retVal;
			}

			retVal.setCurrValue(getDataLastValue(data));
			retVal.setHistory(data);
			retVal.setPrevValue(getADCMonCpuPrevAvgData(object.getIndex(), new Timestamp(now.getTime()), db));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private long getDataLastValue(ArrayList<OBDtoAdcMonCpuHistory> data) {
		return data.get(data.size() - 1).getSpCpuAvg().longValue();
	}

	private void getAvgCpuData(OBDatabase db, ResultSet rs, OBDtoAdcMonCpuHistory obj, int vendor) throws SQLException {
		int cpuAvg = 0;
		int cpuCount = 0;
		for (OBDefine.CPU cpu : OBDefine.CPU.values()) {
			int value = db.getInteger(rs, cpu.name());
			if (isAlteonMp(vendor, cpuCount)) {
				obj.setMpCpu(value);
				cpuCount++;
			} else if (value > -1) {
				cpuAvg += value;
				cpuCount++;
			}
		}
		obj.setSpCpuAvg(cpuAvg / cpuCount);
	}

	public OBDtoAdcMonCpuDataObj getADCMonCpuHistory(OBDtoADCObject object, OBDtoSearch searchOption, OBDatabase db)
			throws OBException {
		OBDtoAdcMonCpuDataObj retVal = new OBDtoAdcMonCpuDataObj();
		String sqlText = "";
		try {
			String sqlTime = makeTimeSqlText(searchOption, "OCCUR_TIME");
			sqlText = String.format(
					" SELECT OCCUR_TIME, CPU1_USAGE,  CPU2_USAGE,  CPU3_USAGE,  CPU4_USAGE,  CPU5_USAGE,  CPU6_USAGE, \n"
							+ "      CPU7_USAGE,  CPU8_USAGE,  CPU9_USAGE,  CPU10_USAGE, CPU11_USAGE, CPU12_USAGE, CPU13_USAGE, \n"
							+ "      CPU14_USAGE, CPU15_USAGE, CPU16_USAGE, CPU17_USAGE, CPU18_USAGE, CPU19_USAGE, CPU20_USAGE, \n"
							+ "      CPU21_USAGE, CPU22_USAGE, CPU23_USAGE, CPU24_USAGE, CPU25_USAGE, CPU26_USAGE, CPU27_USAGE, \n"
							+ "      CPU28_USAGE, CPU29_USAGE, CPU30_USAGE, CPU31_USAGE, CPU32_USAGE                            \n"
							+ " FROM LOG_RESC_CPUMEM                                                   						  \n"
							+ " WHERE ADC_INDEX = %d 																			  \n",
					object.getIndex());

			if (!sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			sqlText += "ORDER BY OCCUR_TIME ASC ";

			ResultSet rs = db.executeQuery(sqlText);

			Long currValue = -1L;

			ArrayList<OBDtoAdcMonCpuHistory> data = new ArrayList<OBDtoAdcMonCpuHistory>();
			while (rs.next()) {
				OBDtoAdcMonCpuHistory obj = new OBDtoAdcMonCpuHistory();
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				getCpuData(db, rs, obj);
				data.add(obj);
				if (rs.isLast()) {
					currValue = Integer.toUnsignedLong(db.getInteger(rs, "CPU1_USAGE"));
				}
			}

			Date now = new Date();
			if (searchOption.getToTime() != null)
				now = searchOption.getToTime();

			if (data == null || data.isEmpty()) {
				retVal.setPrevValue(getADCMonCpuPrevAvgData(object.getIndex(), new Timestamp(now.getTime()), db));
				retVal.setHistory(data);
				return retVal;
			}

			retVal.setCurrValue(currValue);
			retVal.setHistory(data);
			retVal.setPrevValue(getADCMonCpuPrevAvgData(object.getIndex(), new Timestamp(now.getTime()), db));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private void getCpuData(OBDatabase db, ResultSet rs, OBDtoAdcMonCpuHistory obj) throws SQLException {
		ArrayList<Integer> cpuValue = new ArrayList<Integer>();
		for (OBDefine.CPU cpu : OBDefine.CPU.values()) {
			int value = db.getInteger(rs, cpu.name());
			if (value > -1) {
				cpuValue.add(value);
			}
		}
		obj.setCpus(cpuValue);
	}

	private boolean isAlteonMp(int vendor, int cpuCount) {
		return cpuCount == 0 && vendor == OBDefine.ADC_TYPE_ALTEON;
	}

	public static String convertSPCpuAddString(Integer val) {
		if (val == null) {
			return "";
		}

		return "CPU" + val + "_USAGE AS CPU_USAGE" + ", CPU" + val + "_CONNS AS CPU_CONNS";
	}

	public static String convertSPCpuUsageString(Integer val) {
		if (val == null) {
			return "";
		}

		return "CPU" + val + "_USAGE";
	}

	public static String convertSPCpuSessionString(Integer val) {
		if (val == null) {
			return "";
		}

		return "CPU" + val + "_CONNS";
	}

	// TODO
	public ArrayList<OBDtoFaultCpuDataObj> getADCMonCpuSPListHistory(OBDtoADCObject object, OBDtoSearch searchOption,
			Integer cpuNum, OBDatabase db) throws OBException {
		ArrayList<OBDtoFaultCpuDataObj> retVal = new ArrayList<OBDtoFaultCpuDataObj>();

		new OBFaultMonitoringDB().getADCMonCpuSPHistory(object, searchOption, cpuNum, db);

		return retVal;
	}

	public OBDtoFaultCpuDataObj getADCMonCpuSPHistory(OBDtoADCObject object, OBDtoSearch searchOption, Integer cpuNum,
			OBDatabase db) throws OBException {
		OBDtoFaultCpuDataObj retVal = new OBDtoFaultCpuDataObj();
		String sqlText = "";
		try {
			// String selColumn = convertSPCpuAddString(object.getVendor());
			String sqlTime = makeTimeSqlText(searchOption, "OCCUR_TIME");
			if (cpuNum == 1) {
				sqlText = String.format(
						" SELECT OCCUR_TIME, CPU1_USAGE AS CPU_USAGE, -1 AS CPU_CONNS   \n"
								+ " FROM LOG_RESC_CPUMEM                                                   		\n"
								+ " WHERE ADC_INDEX = %d                                                        	\n",
						object.getIndex());
			} else {
				String selColumn = convertSPCpuAddString(cpuNum);
				sqlText = String.format(
						" SELECT OCCUR_TIME, %s                                         \n"
								+ " FROM LOG_RESC_CPUMEM                                                   		\n"
								+ " WHERE ADC_INDEX = %d                                                        	\n",
						selColumn, object.getIndex());
			}

			if (!sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			sqlText += "ORDER BY OCCUR_TIME ASC ";

			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoFaultCpuHistory> history = new ArrayList<OBDtoFaultCpuHistory>();

			while (rs.next()) {
				OBDtoFaultCpuHistory obj = new OBDtoFaultCpuHistory();
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setCpuValue(db.getInteger(rs, "CPU_USAGE"));
				obj.setCpuConns(db.getInteger(rs, "CPU_CONNS"));
				history.add(obj);
			}
			retVal.setHistory(history);
			retVal.setAvgUsageValue(getADCpuSPAvgData(object, searchOption, cpuNum, db)); // SP Usage Avg
			retVal.setAvgConnsValue(getADCpuSPSessionAvgData(object, searchOption, cpuNum, db)); // SP Session Avg
			retVal.setMaxUsageValue(getADCpuSPMaxData(object, searchOption, cpuNum, db)); // SP Usage Max
			retVal.setMaxConnsValue(getADCpuSPSessionMaxData(object, searchOption, cpuNum, db)); // SP Session Max
			retVal.setPrevValue(getADCpuSPPrevAvgData(object, searchOption, cpuNum, db)); // SP USAGE PrevDay Avg
			retVal.setCurrValue(getADCpuSPSessionPrevAvgData(object, searchOption, cpuNum, db)); // SP Session PrevDay
																									// Avg
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public OBDtoFaultCpuHistory getADCMonCpuSpConnectionInfo(OBDtoADCObject object, OBDtoSearch searchOption,
			OBDatabase db) throws OBException {
		OBDtoFaultCpuHistory retVal = new OBDtoFaultCpuHistory();
		String sqlText = "";
		try {
			sqlText = String.format(
					" SELECT OCCUR_TIME,                                                            \n"
							+ " CPU1_USAGE, CPU2_USAGE, CPU3_USAGE, CPU4_USAGE, CPU5_USAGE,                   \n"
							+ " CPU6_USAGE, CPU7_USAGE, CPU8_USAGE, CPU9_USAGE, CPU10_USAGE,                  \n"
							+ " CPU11_USAGE, CPU12_USAGE, CPU13_USAGE, CPU14_USAGE, CPU15_USAGE, CPU16_USAGE, \n"
							+ " CPU17_USAGE, CPU18_USAGE, CPU19_USAGE, CPU20_USAGE,                  			\n"
							+ " CPU21_USAGE, CPU22_USAGE, CPU23_USAGE, CPU24_USAGE, CPU25_USAGE,              \n"
							+ " CPU26_USAGE, CPU27_USAGE, CPU28_USAGE, CPU29_USAGE, CPU30_USAGE,              \n"
							+ " CPU31_USAGE, CPU32_USAGE, 													\n"
							+ " CPU2_CONNS, CPU3_CONNS, CPU4_CONNS, CPU5_CONNS,                   			\n"
							+ " CPU6_CONNS, CPU7_CONNS, CPU8_CONNS, CPU9_CONNS, CPU10_CONNS,                  \n"
							+ " CPU11_CONNS, CPU12_CONNS, CPU13_CONNS, CPU14_CONNS, CPU15_CONNS, CPU16_CONNS, \n"
							+ " CPU17_CONNS, CPU18_CONNS, CPU19_CONNS, CPU20_CONNS,                  			\n"
							+ " CPU21_CONNS, CPU22_CONNS, CPU23_CONNS, CPU24_CONNS, CPU25_CONNS,              \n"
							+ " CPU26_CONNS, CPU27_CONNS, CPU28_CONNS, CPU29_CONNS, CPU30_CONNS,              \n"
							+ " CPU31_CONNS, CPU32_CONNS														\n"
							+ " FROM TMP_FAULT_RESC_CPUMEM                                                   	    \n"
							+ " WHERE ADC_INDEX = %d                                                          \n",
					object.getIndex());

			sqlText += "ORDER BY OCCUR_TIME ASC ";

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false)
				return null;

			retVal.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
			retVal.setCpu1Value(db.getInteger(rs, "CPU1_USAGE"));
			retVal.setCpu2Value(db.getInteger(rs, "CPU2_USAGE"));
			retVal.setCpu3Value(db.getInteger(rs, "CPU3_USAGE"));
			retVal.setCpu4Value(db.getInteger(rs, "CPU4_USAGE"));
			retVal.setCpu5Value(db.getInteger(rs, "CPU5_USAGE"));
			retVal.setCpu6Value(db.getInteger(rs, "CPU6_USAGE"));
			retVal.setCpu7Value(db.getInteger(rs, "CPU7_USAGE"));
			retVal.setCpu8Value(db.getInteger(rs, "CPU8_USAGE"));
			retVal.setCpu9Value(db.getInteger(rs, "CPU9_USAGE"));
			retVal.setCpu10Value(db.getInteger(rs, "CPU10_USAGE"));
			retVal.setCpu11Value(db.getInteger(rs, "CPU11_USAGE"));
			retVal.setCpu12Value(db.getInteger(rs, "CPU12_USAGE"));
			retVal.setCpu13Value(db.getInteger(rs, "CPU13_USAGE"));
			retVal.setCpu14Value(db.getInteger(rs, "CPU14_USAGE"));
			retVal.setCpu15Value(db.getInteger(rs, "CPU15_USAGE"));
			retVal.setCpu16Value(db.getInteger(rs, "CPU16_USAGE"));
			retVal.setCpu17Value(db.getInteger(rs, "CPU17_USAGE"));
			retVal.setCpu18Value(db.getInteger(rs, "CPU18_USAGE"));
			retVal.setCpu19Value(db.getInteger(rs, "CPU19_USAGE"));
			retVal.setCpu20Value(db.getInteger(rs, "CPU20_USAGE"));
			retVal.setCpu21Value(db.getInteger(rs, "CPU21_USAGE"));
			retVal.setCpu22Value(db.getInteger(rs, "CPU22_USAGE"));
			retVal.setCpu23Value(db.getInteger(rs, "CPU23_USAGE"));
			retVal.setCpu24Value(db.getInteger(rs, "CPU24_USAGE"));
			retVal.setCpu25Value(db.getInteger(rs, "CPU25_USAGE"));
			retVal.setCpu26Value(db.getInteger(rs, "CPU26_USAGE"));
			retVal.setCpu27Value(db.getInteger(rs, "CPU27_USAGE"));
			retVal.setCpu28Value(db.getInteger(rs, "CPU28_USAGE"));
			retVal.setCpu29Value(db.getInteger(rs, "CPU29_USAGE"));
			retVal.setCpu30Value(db.getInteger(rs, "CPU30_USAGE"));
			retVal.setCpu31Value(db.getInteger(rs, "CPU31_USAGE"));
			retVal.setCpu32Value(db.getInteger(rs, "CPU32_USAGE"));
			retVal.setCpu2Conns(db.getInteger(rs, "CPU2_CONNS"));
			retVal.setCpu3Conns(db.getInteger(rs, "CPU3_CONNS"));
			retVal.setCpu4Conns(db.getInteger(rs, "CPU4_CONNS"));
			retVal.setCpu5Conns(db.getInteger(rs, "CPU5_CONNS"));
			retVal.setCpu6Conns(db.getInteger(rs, "CPU6_CONNS"));
			retVal.setCpu7Conns(db.getInteger(rs, "CPU7_CONNS"));
			retVal.setCpu8Conns(db.getInteger(rs, "CPU8_CONNS"));
			retVal.setCpu9Conns(db.getInteger(rs, "CPU9_CONNS"));
			retVal.setCpu10Conns(db.getInteger(rs, "CPU10_CONNS"));
			retVal.setCpu11Conns(db.getInteger(rs, "CPU11_CONNS"));
			retVal.setCpu12Conns(db.getInteger(rs, "CPU12_CONNS"));
			retVal.setCpu13Conns(db.getInteger(rs, "CPU13_CONNS"));
			retVal.setCpu14Conns(db.getInteger(rs, "CPU14_CONNS"));
			retVal.setCpu15Conns(db.getInteger(rs, "CPU15_CONNS"));
			retVal.setCpu16Conns(db.getInteger(rs, "CPU16_CONNS"));
			retVal.setCpu17Conns(db.getInteger(rs, "CPU17_CONNS"));
			retVal.setCpu18Conns(db.getInteger(rs, "CPU18_CONNS"));
			retVal.setCpu19Conns(db.getInteger(rs, "CPU19_CONNS"));
			retVal.setCpu20Conns(db.getInteger(rs, "CPU20_CONNS"));
			retVal.setCpu21Conns(db.getInteger(rs, "CPU21_CONNS"));
			retVal.setCpu22Conns(db.getInteger(rs, "CPU22_CONNS"));
			retVal.setCpu23Conns(db.getInteger(rs, "CPU23_CONNS"));
			retVal.setCpu24Conns(db.getInteger(rs, "CPU24_CONNS"));
			retVal.setCpu25Conns(db.getInteger(rs, "CPU25_CONNS"));
			retVal.setCpu26Conns(db.getInteger(rs, "CPU26_CONNS"));
			retVal.setCpu27Conns(db.getInteger(rs, "CPU27_CONNS"));
			retVal.setCpu28Conns(db.getInteger(rs, "CPU28_CONNS"));
			retVal.setCpu29Conns(db.getInteger(rs, "CPU29_CONNS"));
			retVal.setCpu30Conns(db.getInteger(rs, "CPU30_CONNS"));
			retVal.setCpu31Conns(db.getInteger(rs, "CPU31_CONNS"));
			retVal.setCpu32Conns(db.getInteger(rs, "CPU32_CONNS"));

			// usage Minimum
			int sp1Usage = 0;
			int sp2Usage = 0;
			int sp3Usage = 0;
			int sp4Usage = 0;
			int sp5Usage = 0;
			int sp6Usage = 0;
			int sp7Usage = 0;
			int sp8Usage = 0;
			int sp9Usage = 0;
			int sp10Usage = 0;
			int sp11Usage = 0;
			int sp12Usage = 0;
			int sp13Usage = 0;
			int sp14Usage = 0;
			int sp15Usage = 0;
			int sp16Usage = 0;
			int sp17Usage = 0;
			int sp18Usage = 0;
			int sp19Usage = 0;
			int sp20Usage = 0;
			int sp21Usage = 0;
			int sp22Usage = 0;
			int sp23Usage = 0;
			int sp24Usage = 0;
			int sp25Usage = 0;
			int sp26Usage = 0;
			int sp27Usage = 0;
			int sp28Usage = 0;
			int sp29Usage = 0;
			int sp30Usage = 0;
			int sp31Usage = 0;
			sp1Usage = db.getInteger(rs, "CPU2_USAGE");
			sp2Usage = db.getInteger(rs, "CPU3_USAGE");
			sp3Usage = db.getInteger(rs, "CPU4_USAGE");
			sp4Usage = db.getInteger(rs, "CPU5_USAGE");
			sp5Usage = db.getInteger(rs, "CPU6_USAGE");
			sp6Usage = db.getInteger(rs, "CPU7_USAGE");
			sp7Usage = db.getInteger(rs, "CPU8_USAGE");
			sp8Usage = db.getInteger(rs, "CPU9_USAGE");
			sp9Usage = db.getInteger(rs, "CPU10_USAGE");
			sp10Usage = db.getInteger(rs, "CPU11_USAGE");
			sp11Usage = db.getInteger(rs, "CPU12_USAGE");
			sp12Usage = db.getInteger(rs, "CPU13_USAGE");
			sp13Usage = db.getInteger(rs, "CPU14_USAGE");
			sp14Usage = db.getInteger(rs, "CPU15_USAGE");
			sp15Usage = db.getInteger(rs, "CPU16_USAGE");
			sp16Usage = db.getInteger(rs, "CPU17_USAGE");
			sp17Usage = db.getInteger(rs, "CPU18_USAGE");
			sp18Usage = db.getInteger(rs, "CPU19_USAGE");
			sp19Usage = db.getInteger(rs, "CPU20_USAGE");
			sp20Usage = db.getInteger(rs, "CPU21_USAGE");
			sp21Usage = db.getInteger(rs, "CPU22_USAGE");
			sp22Usage = db.getInteger(rs, "CPU23_USAGE");
			sp23Usage = db.getInteger(rs, "CPU24_USAGE");
			sp24Usage = db.getInteger(rs, "CPU25_USAGE");
			sp25Usage = db.getInteger(rs, "CPU26_USAGE");
			sp26Usage = db.getInteger(rs, "CPU27_USAGE");
			sp27Usage = db.getInteger(rs, "CPU28_USAGE");
			sp28Usage = db.getInteger(rs, "CPU29_USAGE");
			sp29Usage = db.getInteger(rs, "CPU30_USAGE");
			sp30Usage = db.getInteger(rs, "CPU31_USAGE");
			sp31Usage = db.getInteger(rs, "CPU32_USAGE");
			ArrayList<Integer> spUsage = new ArrayList<>();
			if (sp1Usage > -1)
				spUsage.add(sp1Usage);
			if (sp2Usage > -1)
				spUsage.add(sp2Usage);
			if (sp3Usage > -1)
				spUsage.add(sp3Usage);
			if (sp4Usage > -1)
				spUsage.add(sp4Usage);
			if (sp5Usage > -1)
				spUsage.add(sp5Usage);
			if (sp6Usage > -1)
				spUsage.add(sp6Usage);
			if (sp7Usage > -1)
				spUsage.add(sp7Usage);
			if (sp8Usage > -1)
				spUsage.add(sp8Usage);
			if (sp9Usage > -1)
				spUsage.add(sp9Usage);
			if (sp10Usage > -1)
				spUsage.add(sp10Usage);
			if (sp11Usage > -1)
				spUsage.add(sp11Usage);
			if (sp12Usage > -1)
				spUsage.add(sp12Usage);
			if (sp13Usage > -1)
				spUsage.add(sp13Usage);
			if (sp14Usage > -1)
				spUsage.add(sp14Usage);
			if (sp15Usage > -1)
				spUsage.add(sp15Usage);
			if (sp16Usage > -1)
				spUsage.add(sp16Usage);
			if (sp17Usage > -1)
				spUsage.add(sp17Usage);
			if (sp18Usage > -1)
				spUsage.add(sp18Usage);
			if (sp19Usage > -1)
				spUsage.add(sp19Usage);
			if (sp20Usage > -1)
				spUsage.add(sp20Usage);
			if (sp21Usage > -1)
				spUsage.add(sp21Usage);
			if (sp22Usage > -1)
				spUsage.add(sp22Usage);
			if (sp23Usage > -1)
				spUsage.add(sp23Usage);
			if (sp24Usage > -1)
				spUsage.add(sp24Usage);
			if (sp25Usage > -1)
				spUsage.add(sp25Usage);
			if (sp26Usage > -1)
				spUsage.add(sp26Usage);
			if (sp27Usage > -1)
				spUsage.add(sp27Usage);
			if (sp28Usage > -1)
				spUsage.add(sp28Usage);
			if (sp29Usage > -1)
				spUsage.add(sp29Usage);
			if (sp30Usage > -1)
				spUsage.add(sp30Usage);
			if (sp31Usage > -1)
				spUsage.add(sp31Usage);

			// session Minimum
			int sp1Session = 0;
			int sp2Session = 0;
			int sp3Session = 0;
			int sp4Session = 0;
			int sp5Session = 0;
			int sp6Session = 0;
			int sp7Session = 0;
			int sp8Session = 0;
			int sp9Session = 0;
			int sp10Session = 0;
			int sp11Session = 0;
			int sp12Session = 0;
			int sp13Session = 0;
			int sp14Session = 0;
			int sp15Session = 0;
			int sp16Session = 0;
			int sp17Session = 0;
			int sp18Session = 0;
			int sp19Session = 0;
			int sp20Session = 0;
			int sp21Session = 0;
			int sp22Session = 0;
			int sp23Session = 0;
			int sp24Session = 0;
			int sp25Session = 0;
			int sp26Session = 0;
			int sp27Session = 0;
			int sp28Session = 0;
			int sp29Session = 0;
			int sp30Session = 0;
			int sp31Session = 0;
			sp1Session = db.getInteger(rs, "CPU2_CONNS");
			sp2Session = db.getInteger(rs, "CPU3_CONNS");
			sp3Session = db.getInteger(rs, "CPU4_CONNS");
			sp4Session = db.getInteger(rs, "CPU5_CONNS");
			sp5Session = db.getInteger(rs, "CPU6_CONNS");
			sp6Session = db.getInteger(rs, "CPU7_CONNS");
			sp7Session = db.getInteger(rs, "CPU8_CONNS");
			sp8Session = db.getInteger(rs, "CPU9_CONNS");
			sp9Session = db.getInteger(rs, "CPU10_CONNS");
			sp10Session = db.getInteger(rs, "CPU11_CONNS");
			sp11Session = db.getInteger(rs, "CPU12_CONNS");
			sp12Session = db.getInteger(rs, "CPU13_CONNS");
			sp13Session = db.getInteger(rs, "CPU14_CONNS");
			sp14Session = db.getInteger(rs, "CPU15_CONNS");
			sp15Session = db.getInteger(rs, "CPU16_CONNS");
			sp16Session = db.getInteger(rs, "CPU17_CONNS");
			sp17Session = db.getInteger(rs, "CPU18_CONNS");
			sp18Session = db.getInteger(rs, "CPU19_CONNS");
			sp19Session = db.getInteger(rs, "CPU20_CONNS");
			sp20Session = db.getInteger(rs, "CPU21_CONNS");
			sp21Session = db.getInteger(rs, "CPU22_CONNS");
			sp22Session = db.getInteger(rs, "CPU23_CONNS");
			sp23Session = db.getInteger(rs, "CPU24_CONNS");
			sp24Session = db.getInteger(rs, "CPU25_CONNS");
			sp25Session = db.getInteger(rs, "CPU26_CONNS");
			sp26Session = db.getInteger(rs, "CPU27_CONNS");
			sp27Session = db.getInteger(rs, "CPU28_CONNS");
			sp28Session = db.getInteger(rs, "CPU29_CONNS");
			sp29Session = db.getInteger(rs, "CPU30_CONNS");
			sp30Session = db.getInteger(rs, "CPU31_CONNS");
			sp31Session = db.getInteger(rs, "CPU32_CONNS");
			ArrayList<Integer> spSession = new ArrayList<>();
			if (sp1Session > -1)
				spSession.add(sp1Session);
			if (sp2Session > -1)
				spSession.add(sp2Session);
			if (sp3Session > -1)
				spSession.add(sp3Session);
			if (sp4Session > -1)
				spSession.add(sp4Session);
			if (sp5Session > -1)
				spSession.add(sp5Session);
			if (sp6Session > -1)
				spSession.add(sp6Session);
			if (sp7Session > -1)
				spSession.add(sp7Session);
			if (sp8Session > -1)
				spSession.add(sp8Session);
			if (sp9Session > -1)
				spSession.add(sp9Session);
			if (sp10Session > -1)
				spSession.add(sp10Session);
			if (sp11Session > -1)
				spSession.add(sp11Session);
			if (sp12Session > -1)
				spSession.add(sp12Session);
			if (sp13Session > -1)
				spSession.add(sp13Session);
			if (sp14Session > -1)
				spSession.add(sp14Session);
			if (sp15Session > -1)
				spSession.add(sp15Session);
			if (sp16Session > -1)
				spSession.add(sp16Session);
			if (sp17Session > -1)
				spSession.add(sp17Session);
			if (sp18Session > -1)
				spSession.add(sp18Session);
			if (sp19Session > -1)
				spSession.add(sp19Session);
			if (sp20Session > -1)
				spSession.add(sp20Session);
			if (sp21Session > -1)
				spSession.add(sp21Session);
			if (sp22Session > -1)
				spSession.add(sp22Session);
			if (sp23Session > -1)
				spSession.add(sp23Session);
			if (sp24Session > -1)
				spSession.add(sp24Session);
			if (sp25Session > -1)
				spSession.add(sp25Session);
			if (sp26Session > -1)
				spSession.add(sp26Session);
			if (sp27Session > -1)
				spSession.add(sp27Session);
			if (sp28Session > -1)
				spSession.add(sp28Session);
			if (sp29Session > -1)
				spSession.add(sp29Session);
			if (sp30Session > -1)
				spSession.add(sp30Session);
			if (sp31Session > -1)
				spSession.add(sp31Session);
			// Integer maxSession = Collections.max(spSssion);
			Integer minUsage = Collections.min(spUsage);
			Integer minSession = Collections.min(spSession);

			retVal.setSpUsageMin(minUsage);
			retVal.setSpSessionMin(minSession);
			// retVal.setSpUsageMin(45);
			// retVal.setSpSessionMin(240000);
			// Long cpuAvgValue = -1L;
			// Long cpuMaxValue = -1L;
			//
			// cpuAvgValue = retVal.getCpu1Value().longValue();
			// if(cpuMaxValue < retVal.getCpu1Value())
			// cpuMaxValue = retVal.getCpu1Value().longValue();
			//
			// retVal.setCpuAvgValue(cpuAvgValue);
			// retVal.setCpuMaxValue(cpuMaxValue);

			// retVal.setCpuAvgValue(getADCpuSPAvgData(object.getIndex(), db));
			// retVal.setCpuMaxValue(getADCpuSPMaxData(object.getIndex(), db));
			retVal.setCpuAvg(db.getInteger(rs, "CPU1_USAGE"));

			Date now = new Date();
			if (searchOption.getToTime() != null)
				now = searchOption.getToTime();

			retVal.setCpuPreAvg(getADCMonCpuPrevAvgData(object.getIndex(), new Timestamp(now.getTime()), db));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private Long getADCpuSPAvgData(OBDtoADCObject object, OBDtoSearch searchOption, Integer cpuNum, OBDatabase db)
			throws OBException {
		Long retVal = -1L;
		String sqlText = "";
		try {
			// String selColumn = convertSPCpuUsageString(object.getVendor());
			String selColumn = convertSPCpuUsageString(cpuNum);
			String sqlTime = makeTimeSqlText(searchOption, "OCCUR_TIME");

			sqlText = String.format(" SELECT AVG(%s) AS AVG               		  \n"
					+ " FROM LOG_RESC_CPUMEM 			      		  \n"
					+ " WHERE ADC_INDEX = %d                        \n", selColumn, object.getIndex());

			if (!sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = db.getLong(rs, "AVG");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private Long getADCpuSPPrevAvgData(OBDtoADCObject object, OBDtoSearch searchOption, Integer cpuNum, OBDatabase db)
			throws OBException {
		Long retVal = -1L;
		String sqlText = "";
		try {
			// String selColumn = convertSPCpuUsageString(object.getVendor());
			String selColumn = convertSPCpuUsageString(cpuNum);
			Timestamp prevEndTime = OBDateTime.getPrevDayTimestamp();
			Timestamp prevStartTime = new Timestamp(prevEndTime.getTime() - 24 * 60 * 60 * 1000);

			sqlText = String.format(
					" SELECT AVG(%s) AS AVG               \n" + " FROM LOG_RESC_CPUMEM 			      \n"
							+ " WHERE ADC_INDEX = %d                        \n"
							+ " AND OCCUR_TIME >= %s AND OCCUR_TIME <= %s   \n",
					selColumn, object.getIndex(), OBParser.sqlString(prevStartTime), OBParser.sqlString(prevEndTime));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = db.getLong(rs, "AVG");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private Long getADCpuSPMaxData(OBDtoADCObject object, OBDtoSearch searchOption, Integer cpuNum, OBDatabase db)
			throws OBException {
		Long retVal = -1L;
		String sqlText = "";
		try {
			// String selColumn = convertSPCpuUsageString(object.getVendor());
			String selColumn = convertSPCpuUsageString(cpuNum);
			String sqlTime = makeTimeSqlText(searchOption, "OCCUR_TIME");

			sqlText = String.format(" SELECT MAX(%s) AS MAX               		  \n"
					+ " FROM LOG_RESC_CPUMEM 			      		  \n"
					+ " WHERE ADC_INDEX = %d                        \n", selColumn, object.getIndex());

			if (!sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = db.getLong(rs, "MAX");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private Long getADCpuSPSessionAvgData(OBDtoADCObject object, OBDtoSearch searchOption, Integer cpuNum,
			OBDatabase db) throws OBException {
		Long retVal = -1L;
		String sqlText = "";
		try {
			// String selColumn = convertSPCpuSessionString(object.getVendor());
			String sqlTime = makeTimeSqlText(searchOption, "OCCUR_TIME");

			if (cpuNum == 1) {
				sqlText = String
						.format(" SELECT AVG(-1) AS AVG       \n" + " FROM LOG_RESC_CPUMEM 			      		  \n"
								+ " WHERE ADC_INDEX = %d                        \n", object.getIndex());
			} else {
				String selColumn = convertSPCpuSessionString(cpuNum);
				sqlText = String
						.format(" SELECT AVG(%s) AS AVG       \n" + " FROM LOG_RESC_CPUMEM 			      		  \n"
								+ " WHERE ADC_INDEX = %d                        \n", selColumn, object.getIndex());
			}

			if (!sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = db.getLong(rs, "AVG");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private Long getADCpuSPSessionPrevAvgData(OBDtoADCObject object, OBDtoSearch searchOption, Integer cpuNum,
			OBDatabase db) throws OBException {
		Long retVal = -1L;
		String sqlText = "";
		try {
			// String selColumn = convertSPCpuSessionString(object.getVendor());
			Timestamp prevEndTime = OBDateTime.getPrevDayTimestamp();
			Timestamp prevStartTime = new Timestamp(prevEndTime.getTime() - 24 * 60 * 60 * 1000);

			if (cpuNum == 1) {
				sqlText = String.format(
						" SELECT AVG(-1) AS AVG       \n" + " FROM LOG_RESC_CPUMEM 			      		  \n"
								+ " WHERE ADC_INDEX = %d                        \n"
								+ " AND OCCUR_TIME >= %s AND OCCUR_TIME <= %s   \n",
						object.getIndex(), OBParser.sqlString(prevStartTime), OBParser.sqlString(prevEndTime));
			} else {
				String selColumn = convertSPCpuSessionString(cpuNum);
				sqlText = String.format(
						" SELECT AVG(%s) AS AVG       \n" + " FROM LOG_RESC_CPUMEM 			      		  \n"
								+ " WHERE ADC_INDEX = %d                        \n"
								+ " AND OCCUR_TIME >= %s AND OCCUR_TIME <= %s   \n",
						selColumn, object.getIndex(), OBParser.sqlString(prevStartTime),
						OBParser.sqlString(prevEndTime));
			}

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = db.getLong(rs, "AVG");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private Long getADCpuSPSessionMaxData(OBDtoADCObject object, OBDtoSearch searchOption, Integer cpuNum,
			OBDatabase db) throws OBException {
		Long retVal = -1L;
		String sqlText = "";
		try {
			// String selColumn = convertSPCpuSessionString(object.getVendor());
			String sqlTime = makeTimeSqlText(searchOption, "OCCUR_TIME");
			if (cpuNum == 1) {
				sqlText = String
						.format(" SELECT MAX(-1) AS MAX       \n" + " FROM LOG_RESC_CPUMEM 			      		  \n"
								+ " WHERE ADC_INDEX = %d                        \n", object.getIndex());
			} else {
				String selColumn = convertSPCpuSessionString(cpuNum);
				sqlText = String
						.format(" SELECT MAX(%s) AS MAX       \n" + " FROM LOG_RESC_CPUMEM 			      		  \n"
								+ " WHERE ADC_INDEX = %d                        \n", selColumn, object.getIndex());
			}

			if (!sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = db.getLong(rs, "MAX");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private Long getADCMonCpuPrevAvgData(Integer adcIndex, Timestamp occurTime, OBDatabase db) throws OBException {
		Long retVal = -1L;
		String sqlText = "";
		try {
			Timestamp prevEndTime = OBDateTime.getPrevDayTimestamp();
			Timestamp prevStartTime = new Timestamp(prevEndTime.getTime() - 24 * 60 * 60 * 1000);

			sqlText = String.format(
					" SELECT AVG(CPU1_USAGE) AS AVG               \n" + " FROM LOG_RESC_CPUMEM 			      \n"
							+ " WHERE ADC_INDEX = %d                        \n"
							+ " AND OCCUR_TIME >= %s AND OCCUR_TIME <= %s   \n",
					adcIndex, OBParser.sqlString(prevStartTime), OBParser.sqlString(prevEndTime));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = db.getLong(rs, "AVG");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public OBDtoFaultDataObj getADCMonSessionHistory(OBDtoADCObject object, OBDtoSearch searchOption, OBDatabase db)
			throws OBException {
		OBDtoFaultDataObj retVal = new OBDtoFaultDataObj();
		String sqlText = "";
		try {
			String sqlTime = makeTimeSqlText(searchOption, "OCCUR_TIME");
			sqlText = String.format(" SELECT OCCUR_TIME, CONN_CURR	        \n"
					+ " FROM LOG_ADC_PERF_STATS        		\n" + " WHERE ADC_INDEX = %d                  ",
					object.getIndex());

			if (!sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			sqlText += "ORDER BY OCCUR_TIME ASC ";

			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoDataObj> history = new ArrayList<OBDtoDataObj>();

			Long currValue = -1L;
			Long maxValue = -1L;
			while (rs.next()) {
				OBDtoDataObj obj = new OBDtoDataObj();
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setValue(db.getLong(rs, "CONN_CURR"));
				currValue = obj.getValue();
				if (maxValue < obj.getValue())
					maxValue = obj.getValue();
				history.add(obj);
			}
			retVal.setCurrValue(currValue);
			retVal.setHistory(history);
			retVal.setMaxValue(maxValue);

			Date now = new Date();
			if (searchOption.getToTime() != null)
				now = searchOption.getToTime();

			retVal.setPrevValue(getADCMonSessionPrevAvgData(object.getIndex(), new Timestamp(now.getTime()), db));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	// private Long current;
	// private Long yesterdayAvg;
	// private int lbClass; //세션 데이터 구성: ALL, SLB, FLB / OBDefine.ADC_LB_CLASS
	// private ArrayList<OBDtoFlbSlbSession> history; //시간마다 FLB/SLB connection 값

	public OBDtoAdcCurrentSession getADCMonSessionHistoryNew(OBDtoADCObject object, OBDtoSearch searchOption,
			OBDatabase db) throws OBException {
		OBDtoAdcCurrentSession retVal = new OBDtoAdcCurrentSession();
		ArrayList<OBDtoFlbSlbSession> history = new ArrayList<OBDtoFlbSlbSession>();
		String sqlText = "";
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());
			String sqlTime = makeTimeSqlText(searchOption, "OCCUR_TIME");
			sqlText = String.format(
					" SELECT OCCUR_TIME, CONN_CURR, CONN_SLB_CURR FROM LOG_ADC_PERF_STATS WHERE ADC_INDEX = %d ",
					object.getIndex());

			if (!sqlTime.isEmpty()) {
				sqlText += " AND " + sqlTime;
			}
			sqlText += "ORDER BY OCCUR_TIME ASC ";

			ResultSet rs = db.executeQuery(sqlText);

			Long adcConn = -1L;
			Long slbConn = -1L;
			Long maxConn = -1L;
			while (rs.next()) {
				adcConn = db.getLong(rs, "CONN_CURR");
				slbConn = db.getLong(rs, "CONN_SLB_CURR");
				OBDtoFlbSlbSession connValue = new OBDtoFlbSlbSession();
				connValue.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				connValue.setSlbSession(slbConn);
				if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_ALTEON)
						&& adcInfo.getRoleFlbYn() == OBDefine.STATE_ENABLE) // Alteon and FLB 경우만 flb 트래픽 계산
				{
					connValue.setSlbSession(slbConn);
					if ((adcConn - slbConn) >= 0) // concurrent값이라 slb 값이 전체값과 계산시차가 있을 수 있음. 음수값 방지
					{
						connValue.setFlbSession(adcConn - slbConn);
					} else {
						connValue.setFlbSession(0L);
					}
				} else {
					connValue.setSlbSession(adcConn);
					connValue.setFlbSession(0L);
				}
				if (maxConn < adcConn) {
					maxConn = adcConn;
				}
				history.add(connValue);
			}
			retVal.setCurrent(adcConn); // 마지막 것이 최근값
			retVal.setHistory(history);
			Date now = new Date();
			if (searchOption.getToTime() != null) {
				now = searchOption.getToTime();
			}
			retVal.setYesterdayAvg(getADCMonSessionPrevAvgData(object.getIndex(), new Timestamp(now.getTime()), db));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private Long getADCMonSessionPrevAvgData(Integer adcIndex, Timestamp occurTime, OBDatabase db) throws OBException {
		Long retVal = -1L;
		String sqlText = "";
		try {
			Timestamp prevEndTime = OBDateTime.getPrevDayTimestamp();
			Timestamp prevStartTime = new Timestamp(prevEndTime.getTime() - 24 * 60 * 60 * 1000);

			sqlText = String.format(
					" SELECT AVG(CONN_CURR) AS AVG FROM LOG_ADC_PERF_STATS "
							+ " WHERE ADC_INDEX = %d AND OCCUR_TIME >= %s AND OCCUR_TIME <= %s \n",
					adcIndex, OBParser.sqlString(prevStartTime), OBParser.sqlString(prevEndTime));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = db.getLong(rs, "AVG");
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
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	//
	// OBDtoADCObject object = new OBDtoADCObject();
	// object.setIndex(1);
	// object.setCategory(OBDtoADCObject.CATEGORY_ADC);
	//
	// OBDtoSearch searchOption = new OBDtoSearch();
	//
	// OBDtoFaultDataObj data = new
	// OBFaultMonitoringDB().getADCMonPktErrHistory(object, searchOption, db);
	// System.out.println(data);
	// db.closeDB();
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

	public OBDtoFaultDataObj getADCMonPktDropHistory(OBDtoADCObject object, OBDtoSearch searchOption, OBDatabase db)
			throws OBException {
		OBDtoFaultDataObj retVal = new OBDtoFaultDataObj();
		String sqlText = "";
		try {
			String sqlTime = makeTimeSqlText(searchOption, "OCCUR_TIME");
//            sqlText =
//                    String.format(" SELECT OCCUR_TIME,     	                     \n"
//                            + " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10,          \n"
//                            + " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
//                            + " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
//                            + " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36                                  \n"
//                            + " FROM LOG_LINK_PKT_DROP    				 \n"
//                            + " WHERE ADC_INDEX = %d                           \n", object.getIndex());
			sqlText = String.format(" SELECT OCCUR_TIME,     	                     \n" + " CHART_DATA \n"
					+ " FROM LOG_LINK_PKT_DROP_CHART    				 \n"
					+ " WHERE ADC_INDEX = %d                           \n", object.getIndex());

			if (!sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			sqlText += "ORDER BY OCCUR_TIME ASC ";

			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoDataObj> history = new ArrayList<OBDtoDataObj>();

			Long currValue = -1L;
			Long maxValue = -1L;

			while (rs.next()) {
				OBDtoDataObj obj = new OBDtoDataObj();
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));

				obj.setValue(db.getLong(rs, "CHART_DATA"));

				currValue = obj.getValue();
				if (maxValue < obj.getValue())
					maxValue = obj.getValue();
				// if(prevValue==-1L)
				// {
				// if(obj.getOccurTime().getTime()<prevDate.getTime())
				// prevValue = obj.getValue();
				// }
				history.add(obj);
			}
			retVal.setCurrValue(currValue);
			retVal.setHistory(history);
			retVal.setMaxValue(maxValue);
			Date now = new Date();
			if (searchOption.getToTime() != null)
				now = searchOption.getToTime();

			retVal.setPrevValue(getADCMonPktDropPrevAvgData(object.getIndex(), new Timestamp(now.getTime()), db));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private Long getADCMonPktDropPrevAvgData(Integer adcIndex, Timestamp occurTime, OBDatabase db) throws OBException {
		Long retVal = -1L;
		String sqlText = "";
		try {
			Timestamp prevEndTime = OBDateTime.getPrevDayTimestamp();
			Timestamp prevStartTime = new Timestamp(prevEndTime.getTime() - 24 * 60 * 60 * 1000);

			sqlText = String.format(" SELECT     	                     \n"
					+ " AVG(PORT1) AS PORT1, AVG(PORT2) AS PORT2, AVG(PORT3) AS PORT3, AVG(PORT4) AS PORT4, AVG(PORT5) AS PORT5, AVG(PORT6) AS PORT6, AVG(PORT7) AS PORT7, AVG(PORT8) AS PORT8, AVG(PORT9) AS PORT9, AVG(PORT10) AS PORT10,          \n"
					+ " AVG(PORT11) AS PORT11, AVG(PORT12) AS PORT12, AVG(PORT13) AS PORT13, AVG(PORT14) AS PORT14, AVG(PORT15) AS PORT15, AVG(PORT16) AS PORT16, AVG(PORT17) AS PORT17, AVG(PORT18) AS PORT18, AVG(PORT19) AS PORT19, AVG(PORT20) AS PORT20, \n"
					+ " AVG(PORT21) AS PORT21, AVG(PORT22) AS PORT22, AVG(PORT23) AS PORT23, AVG(PORT24) AS PORT24, AVG(PORT25) AS PORT25, AVG(PORT26) AS PORT26, AVG(PORT27) AS PORT27, AVG(PORT28) AS PORT28, AVG(PORT29) AS PORT29, AVG(PORT30) AS PORT30, \n"
					+ " AVG(PORT31) AS PORT31, AVG(PORT32) AS PORT32, AVG(PORT33) AS PORT33, AVG(PORT34) AS PORT34, AVG(PORT35) AS PORT35, AVG(PORT36) AS PORT36                                  \n"
					+ " FROM LOG_LINK_PKT_DROP    				 \n"
					+ " WHERE ADC_INDEX = %d                           \n"
					+ " AND OCCUR_TIME >= %s AND OCCUR_TIME <= %s   \n", adcIndex, OBParser.sqlString(prevStartTime),
					OBParser.sqlString(prevEndTime));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				long totalValue = 0;
				long value = 0;
				value = db.getLong(rs, "PORT1");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT2");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT3");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT4");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT5");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT6");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT7");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT8");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT9");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT10");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT11");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT12");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT13");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT14");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT15");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT16");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT17");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT18");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT19");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT20");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT21");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT22");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT23");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT24");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT25");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT26");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT27");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT28");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT29");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT30");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT31");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT32");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT33");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT34");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT35");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT36");
				if (value > 0)
					totalValue += value;

				retVal = totalValue;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public OBDtoFaultDataObj getADCMonPktErrHistory(OBDtoADCObject object, OBDtoSearch searchOption, OBDatabase db)
			throws OBException {
		OBDtoFaultDataObj retVal = new OBDtoFaultDataObj();
		String sqlText = "";
		try {
			String sqlTime = makeTimeSqlText(searchOption, "OCCUR_TIME");
//            sqlText =
//                    String.format(" SELECT OCCUR_TIME,     	                     \n"
//                            + " PORT1, PORT2, PORT3, PORT4, PORT5, PORT6, PORT7, PORT8, PORT9, PORT10,          \n"
//                            + " PORT11, PORT12, PORT13, PORT14, PORT15, PORT16, PORT17, PORT18, PORT19, PORT20, \n"
//                            + " PORT21, PORT22, PORT23, PORT24, PORT25, PORT26, PORT27, PORT28, PORT29, PORT30, \n"
//                            + " PORT31, PORT32, PORT33, PORT34, PORT35, PORT36                                  \n"
//                            + " FROM LOG_LINK_PKT_ERR    				 \n"
//                            + " WHERE ADC_INDEX = %d                           \n", object.getIndex());
			sqlText = String.format(" SELECT OCCUR_TIME,     	                     \n" + " CHART_DATA \n"
					+ " FROM LOG_LINK_PKT_ERR_CHART    				 \n"
					+ " WHERE ADC_INDEX = %d                           \n", object.getIndex());

			if (!sqlTime.isEmpty())
				sqlText += " AND " + sqlTime;

			sqlText += "ORDER BY OCCUR_TIME ASC ";

			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoDataObj> history = new ArrayList<OBDtoDataObj>();

			Long currValue = -1L;
			Long maxValue = -1L;

			while (rs.next()) {
				OBDtoDataObj obj = new OBDtoDataObj();
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));

				obj.setValue(db.getLong(rs, "CHART_DATA"));
				currValue = obj.getValue();
				if (maxValue < obj.getValue())
					maxValue = obj.getValue();

				history.add(obj);
			}

			retVal.setCurrValue(currValue);
			retVal.setHistory(history);
			retVal.setMaxValue(maxValue);

			Date now = new Date();
			if (searchOption.getToTime() != null)
				now = searchOption.getToTime();

			retVal.setPrevValue(getADCMonPktErrPrevAvgData(object.getIndex(), new Timestamp(now.getTime()), db));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private Long getADCMonPktErrPrevAvgData(Integer adcIndex, Timestamp occurTime, OBDatabase db) throws OBException {
		Long retVal = -1L;
		String sqlText = "";
		try {
			Timestamp prevEndTime = OBDateTime.getPrevDayTimestamp();
			Timestamp prevStartTime = new Timestamp(prevEndTime.getTime() - 24 * 60 * 60 * 1000);

			sqlText = String.format(" SELECT     	                     \n"
					+ " AVG(PORT1) AS PORT1, AVG(PORT2) AS PORT2, AVG(PORT3) AS PORT3, AVG(PORT4) AS PORT4, AVG(PORT5) AS PORT5, AVG(PORT6) AS PORT6, AVG(PORT7) AS PORT7, AVG(PORT8) AS PORT8, AVG(PORT9) AS PORT9, AVG(PORT10) AS PORT10,          \n"
					+ " AVG(PORT11) AS PORT11, AVG(PORT12) AS PORT12, AVG(PORT13) AS PORT13, AVG(PORT14) AS PORT14, AVG(PORT15) AS PORT15, AVG(PORT16) AS PORT16, AVG(PORT17) AS PORT17, AVG(PORT18) AS PORT18, AVG(PORT19) AS PORT19, AVG(PORT20) AS PORT20, \n"
					+ " AVG(PORT21) AS PORT21, AVG(PORT22) AS PORT22, AVG(PORT23) AS PORT23, AVG(PORT24) AS PORT24, AVG(PORT25) AS PORT25, AVG(PORT26) AS PORT26, AVG(PORT27) AS PORT27, AVG(PORT28) AS PORT28, AVG(PORT29) AS PORT29, AVG(PORT30) AS PORT30, \n"
					+ " AVG(PORT31) AS PORT31, AVG(PORT32) AS PORT32, AVG(PORT33) AS PORT33, AVG(PORT34) AS PORT34, AVG(PORT35) AS PORT35, AVG(PORT36) AS PORT36                                  \n"
					+ " FROM LOG_LINK_PKT_ERR    				 \n"
					+ " WHERE ADC_INDEX = %d                           \n"
					+ " AND OCCUR_TIME >= %s AND OCCUR_TIME <= %s   \n", adcIndex, OBParser.sqlString(prevStartTime),
					OBParser.sqlString(prevEndTime));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				long totalValue = 0;
				long value = 0;
				value = db.getLong(rs, "PORT1");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT2");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT3");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT4");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT5");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT6");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT7");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT8");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT9");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT10");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT11");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT12");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT13");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT14");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT15");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT16");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT17");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT18");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT19");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT20");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT21");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT22");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT23");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT24");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT25");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT26");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT27");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT28");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT29");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT30");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT31");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT32");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT33");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT34");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT35");
				if (value > 0)
					totalValue += value;
				value = db.getLong(rs, "PORT36");
				if (value > 0)
					totalValue += value;

				retVal = totalValue;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private OBDtoMonTrafficVSvcInfo getLastAdcPerfInfo(Integer adcIndex) throws OBException {
		OBDtoMonTrafficVSvcInfo retVal = new OBDtoMonTrafficVSvcInfo();

		String sqlText = "";

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT ADC_INDEX, OCCUR_TIME,              \n"
					+ " BYTE_IN, BYTE_OUT,                             \n"
					+ " CONN_CURR, CONN_MAX, CONN_TOT,                 \n"
					+ " PKT_IN, PKT_OUT, FILTER_TOT                    \n"
					+ " FROM TMP_FAULT_ADC_PERF_STATS                  \n"
					+ " WHERE ADC_INDEX = %d                           \n"
					+ " ORDER BY OCCUR_TIME DESC LIMIT 1               \n", adcIndex);
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal.setBytesIn(db.getLong(rs, "BYTE_IN"));
				retVal.setBytesOut(db.getLong(rs, "BYTE_OUT"));
				retVal.setCurConns(db.getLong(rs, "CONN_CURR"));
				retVal.setMaxConns(db.getLong(rs, "CONN_MAX"));
				retVal.setTotConns(db.getLong(rs, "CONN_TOT"));
				retVal.setPktsIn(db.getLong(rs, "PKT_IN"));
				retVal.setPktsOut(db.getLong(rs, "PKT_OUT"));
				retVal.setFilterTot(db.getLong(rs, "FILTER_TOT"));
				retVal.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			e.printStackTrace();
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	public void writeAdcPerfInfoList(Integer adcIndex, int adcType, String swVersion, Timestamp occurTime,
			OBDtoVSStatusCountInfo statusCount, OBDtoMonTrafficAdc trafficInfo) throws OBException {
		// comment: trafficInfo 멤버 -1인 경우 무시 처리 필요
		if (trafficInfo == null || statusCount == null)
			return;

		String sqlText = "";
		String sqlTextLatestDelete = "";
		String sqlTextLatestInsert = "";

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			OBDtoMonTrafficVSvcInfo lastPerfInfo = getLastAdcPerfInfo(adcIndex);
			OBDtoMonTrafficVSvcInfo tempBytePacket = new OBDtoMonTrafficVSvcInfo();
			tempBytePacket.setOccurTime(trafficInfo.getOccurTime());
			tempBytePacket.setBytesIn(trafficInfo.getBytesIn());
			tempBytePacket.setBytesOut(trafficInfo.getBytesOut());
			tempBytePacket.setPktsIn(trafficInfo.getPktsIn());
			tempBytePacket.setPktsOut(trafficInfo.getPktsOut());
			tempBytePacket.setFilterTot(trafficInfo.getFilterSum());

			OBDtoBpsPpsEpsDps bpsPps = calcBpsPpsVSvcPerf(occurTime, lastPerfInfo, tempBytePacket, adcType, swVersion);

			if (adcType == OBDefine.ADC_TYPE_ALTEON && swVersion != null && swVersion.startsWith("26.")) // Alteon v26
																											// SNMP
																											// small
																											// number 보정
																											// 처리
			{
				bpsPps.setBpsIn(trafficInfo.getBytesInPerSec() * 8);
				bpsPps.setBpsOut(trafficInfo.getBytesOutPerSec() * 8);
			}

			Long bpsTot = calcSumOrEmpty(bpsPps.getBpsIn(), bpsPps.getBpsOut());
			Long ppsTot = calcSumOrEmpty(bpsPps.getPpsIn(), bpsPps.getPpsOut());
			Long byteTot = calcSumOrEmpty(trafficInfo.getBytesIn(), trafficInfo.getBytesOut());
			Long pktTot = calcSumOrEmpty(trafficInfo.getPktsIn(), trafficInfo.getPktsOut());

			sqlText = String.format(
					" INSERT INTO LOG_ADC_PERF_STATS (ADC_INDEX, OCCUR_TIME,                               \n"
							+ "    BYTE_IN, BYTE_OUT, BYTE_TOT, BPS_IN, BPS_OUT, BPS_TOT,                            \n"
							+ "    CONN_CURR, CONN_MAX, CONN_TOT, CONN_SLB_CURR,                                     \n"
							+ "    PKT_IN, PKT_OUT, PKT_TOT, PPS_IN, PPS_OUT, PPS_TOT,                               \n"
							+ "    VS_COUNT, VS_COUNT_AVAIL, VS_COUNT_UNAVAIL, VS_COUNT_BLOCKED, VS_COUNT_DISABLED)  \n"
							+ " VALUES (%d, %s, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d) \n",
					adcIndex, OBParser.sqlString(occurTime), trafficInfo.getBytesIn(), trafficInfo.getBytesOut(),
					byteTot, bpsPps.getBpsIn(), bpsPps.getBpsOut(), bpsTot, trafficInfo.getConnCurrent(),
					trafficInfo.getConnMax(), trafficInfo.getConnTot(), trafficInfo.getConnSlbCurrent(),
					trafficInfo.getPktsIn(), trafficInfo.getPktsOut(), pktTot, bpsPps.getPpsIn(), bpsPps.getPpsOut(),
					ppsTot, statusCount.getVsCount(), statusCount.getVsCountAvailable(),
					statusCount.getVsCountUnavailable(), statusCount.getVsCountBlocked(),
					statusCount.getVsCountDisabled());
			sqlTextLatestDelete = String.format(" DELETE FROM TMP_FAULT_ADC_PERF_STATS WHERE ADC_INDEX = %d ;",
					adcIndex);
			sqlTextLatestInsert = String.format(
					" INSERT INTO TMP_FAULT_ADC_PERF_STATS (ADC_INDEX, OCCUR_TIME,                              \n"
							+ "    BYTE_IN, BYTE_OUT, BYTE_TOT, BPS_IN, BPS_OUT, BPS_TOT,                               \n"
							+ "    CONN_CURR, CONN_MAX, CONN_TOT, CONN_SLB_CURR,                                        \n"
							+ "    PKT_IN, PKT_OUT, PKT_TOT, PPS_IN, PPS_OUT, PPS_TOT,                                  \n"
							+ "    VS_COUNT, VS_COUNT_AVAIL, VS_COUNT_UNAVAIL, VS_COUNT_BLOCKED, VS_COUNT_DISABLED,     \n"
							+ "    FILTER_TOT, FILTER_CURR)                                                             \n" // only
																															// Alteon
							+ " VALUES (%d, %s, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, \n"
							+ " %d, %d, %d, %d, %d)                                                                     \n",
					adcIndex, OBParser.sqlString(occurTime), trafficInfo.getBytesIn(), trafficInfo.getBytesOut(),
					byteTot, bpsPps.getBpsIn(), bpsPps.getBpsOut(), bpsTot, trafficInfo.getConnCurrent(),
					trafficInfo.getConnMax(), trafficInfo.getConnTot(), trafficInfo.getConnSlbCurrent(),
					trafficInfo.getPktsIn(), trafficInfo.getPktsOut(), pktTot, bpsPps.getPpsIn(), bpsPps.getPpsOut(),
					ppsTot, statusCount.getVsCount(), statusCount.getVsCountAvailable(),
					statusCount.getVsCountUnavailable(), statusCount.getVsCountBlocked(),
					statusCount.getVsCountDisabled(), trafficInfo.getFilterSum(), bpsPps.getFilterCount());
			db.executeUpdate(sqlText);
			db.executeUpdate(sqlTextLatestDelete);
			db.executeUpdate(sqlTextLatestInsert);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			e.printStackTrace();
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.info(OBDefine.LOGFILE_ADCMON, String.format("end. adcIndex:%s", adcIndex));
	}

	public String getVSvcIPAddress(Integer adcIndex, Integer adcType, String vsvcIndex) throws OBException {
		String sqlText = "";

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (adcType == OBDefine.ADC_TYPE_ALTEON) {
				sqlText = String.format(
						" SELECT A.ADC_INDEX, B.VIRTUAL_IP               \n"
								+ " FROM TMP_SLB_VS_SERVICE      A                 \n"
								+ " INNER JOIN TMP_SLB_VSERVER   B                 \n"
								+ " ON B.INDEX = A.VS_INDEX                        \n"
								+ " WHERE A.ADC_INDEX = %d AND A.INDEX = %s        \n",
						adcIndex, OBParser.sqlString(vsvcIndex));
			} else {
				sqlText = String.format(
						" SELECT A.ADC_INDEX, A.VIRTUAL_IP               \n"
								+ " FROM TMP_SLB_VSERVER         A                 \n"
								+ " WHERE A.ADC_INDEX = %d AND A.INDEX = %s        \n",
						adcIndex, OBParser.sqlString(vsvcIndex));
			}
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				return db.getString(rs, "VIRTUAL_IP");
			}
			return "";
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			e.printStackTrace();
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public ArrayList<OBHostInfo> getVSvcMemberInfo(Integer adcIndex, Integer adcType, String vsvcIndex)
			throws OBException {
		String sqlText = "";
		ArrayList<OBHostInfo> retVal = new ArrayList<OBHostInfo>();
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (adcType == OBDefine.ADC_TYPE_ALTEON) {
				sqlText = String.format(
						" SELECT A.IP_ADDRESS, A.PORT                    \n"
								+ " FROM TMP_SLB_NODE             A                \n"
								+ " INNER JOIN TMP_SLB_POOLMEMBER B                \n"
								+ " ON B.NODE_INDEX = A.INDEX                      \n"
								+ " INNER JOIN TMP_SLB_VS_SERVICE C                \n"
								+ " ON C.POOL_INDEX = B.POOL_INDEX                 \n"
								+ " WHERE C.INDEX = %s AND C.ADC_INDEX = %d        \n",
						OBParser.sqlString(vsvcIndex), adcIndex);
			} else {
				sqlText = String.format(
						" SELECT A.IP_ADDRESS, VIRTUAL_PORT AS PORT      \n"
								+ " FROM TMP_SLB_NODE             A                \n"
								+ " INNER JOIN TMP_SLB_POOLMEMBER B                \n"
								+ " ON B.NODE_INDEX = A.INDEX                      \n"
								+ " INNER JOIN TMP_SLB_VSERVER C                   \n"
								+ " ON C.POOL_INDEX = B.POOL_INDEX                 \n"
								+ " WHERE C.INDEX = %s AND C.ADC_INDEX = %d        \n",
						OBParser.sqlString(vsvcIndex), adcIndex);
			}
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBHostInfo obj = new OBHostInfo();
				obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				obj.setPort(db.getInteger(rs, "PORT"));
				retVal.add(obj);
			}
			return retVal;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			e.printStackTrace();
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public Integer getSvcPortNum(OBDtoAdcInfo adcInfo, String svcIndexStr) throws OBException {
		String sqlText = "";
		Integer retVal = 0;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				sqlText = String.format(" SELECT VIRTUAL_PORT                   \n"
						+ " FROM TMP_SLB_VS_SERVICE               \n" + " WHERE INDEX = %s                      \n",
						OBParser.sqlString(svcIndexStr));
			} else {
				sqlText = String.format(" SELECT VIRTUAL_PORT                   \n"
						+ " FROM TMP_SLB_VSERVER               \n" + " WHERE INDEX = %s                      \n",
						OBParser.sqlString(svcIndexStr));
			}
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = db.getInteger(rs, "VIRTUAL_PORT");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}
}
