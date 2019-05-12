package kr.openbase.adcsmart.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import kr.openbase.adcsmart.service.OBSystemTools;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSysToolsPortUsage;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBAdcAlteonHandler;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonL2Ports;
import kr.openbase.adcsmart.service.impl.dto.OBDtoPoolNodeInfo;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;

public class OBSystemToolsImpl implements OBSystemTools {
//	public static void main(String[] args) 
//	{
//		try
//		{
//			System.out.println(new OBSystemToolsImpl().portUsageGetContent());
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}

	private String[] portUsageHederInfo = { "No", "ADC Name", "ADC IP", "Usage", "Total", "Used", "Used Ports",
			"Unused Ports" };

	private ArrayList<OBDtoSysToolsPortUsage> portUsageContents() throws OBException {
		// ADC 이름: 전체 개수: 사용개수: 미사용개수: 사용 상세정보: 미 사용상세정보: 시간.
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		HashMap<String, OBDtoSysToolsPortUsage> hashMap = new HashMap<String, OBDtoSysToolsPortUsage>();
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT A.NAME, A.IPADDRESS, C.NAME AS PORT_NAME, B.STATUS, B.OCCUR_TIME \n"
							+ " FROM MNG_ADC                   A                                        \n"
							+ " LEFT JOIN TMP_FAULT_LINK_STATS B                                        \n"
							+ " ON A.INDEX=B.ADC_INDEX                                                  \n"
							+ " LEFT JOIN TMP_FAULT_LINK_INFO   C                                       \n"
							+ " ON C.PORT_INDEX = B.PORT_INDEX                                          \n"
							+ " WHERE A.AVAILABLE = %d                                                  \n"
							+ " ORDER BY A.NAME DESC                                                    \n",
					OBDefine.ADC_STATE.AVAILABLE);
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoSysToolsPortUsage tmpInfo = hashMap.get(db.getString(rs, "NAME"));
				if (null == tmpInfo) {
					tmpInfo = new OBDtoSysToolsPortUsage();
					tmpInfo.setAdcIPAddress(db.getString(rs, "IPADDRESS"));
					tmpInfo.setAdcName(db.getString(rs, "NAME"));
					tmpInfo.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
					hashMap.put(tmpInfo.getAdcName(), tmpInfo);
				}

				if (OBDtoMonL2Ports.STATUS_UP == db.getInteger(rs, "STATUS")) {
					tmpInfo.setUsedCount(tmpInfo.getUsedCount() + 1);
					String tmpDetail = tmpInfo.getUsedInfo();
					String portName = db.getString(rs, "PORT_NAME");
					if (tmpDetail.isEmpty())
						tmpDetail += String.format("%s", portName);
					else
						tmpDetail += String.format(", %s", portName);
					tmpInfo.setUsedInfo(tmpDetail);
				} else {
					tmpInfo.setNotUsedCount(tmpInfo.getNotUsedCount() + 1);
					String tmpDetail = tmpInfo.getNotUsedInfo();
					String portName = db.getString(rs, "PORT_NAME");
					if (tmpDetail.isEmpty())
						tmpDetail += String.format("%s", portName);
					else
						tmpDetail += String.format(", %s", portName);
					tmpInfo.setNotUsedInfo(tmpDetail);
				}
			}

			ArrayList<OBDtoSysToolsPortUsage> list = new ArrayList<OBDtoSysToolsPortUsage>(hashMap.values());
			return list;
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
	public String portUsageGetContent() throws OBException {
		// ADC 이름: 전체 개수: 사용개수: 미사용개수: 사용 상세정보: 미 사용상세정보: 시간.
		String retVal = "";
		retVal += String.format("%-3s|%-20s|%-20s|%-5s|%-5s|%-4s|%s\n", portUsageHederInfo[0], portUsageHederInfo[1],
				portUsageHederInfo[2], portUsageHederInfo[3], portUsageHederInfo[4], portUsageHederInfo[5],
				portUsageHederInfo[7]);

		retVal += String.format(
				"===============================================================================================================\n");
		try {
			ArrayList<OBDtoSysToolsPortUsage> list = portUsageContents();

			for (int i = 0; i < list.size(); i++) {
				OBDtoSysToolsPortUsage tmp = list.get(i);
				int rate = tmp.getUsedCount() * 100 / (tmp.getNotUsedCount() + tmp.getUsedCount());
				retVal += String.format("%-3d|%-20s|%-20s|%-5d|%-5d|%-4d|%s\n", i, tmp.getAdcName(),
						tmp.getAdcIPAddress(), rate, tmp.getNotUsedCount() + tmp.getUsedCount(), tmp.getUsedCount(),
						tmp.getNotUsedInfo());
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	@Override
	public String[] portUsageMakeCsvHeader() throws OBException {
		return portUsageHederInfo;
	}

	@Override
	public ArrayList<String[]> portUsageMakeCsvBody() throws OBException {
		try {
			ArrayList<OBDtoSysToolsPortUsage> list = portUsageContents();
			ArrayList<String[]> data = new ArrayList<String[]>(list.size());

			for (int i = 0; i < list.size(); i++) {
				OBDtoSysToolsPortUsage tmp = list.get(i);
				// private String [] portUsageHederInfo = {"No", "ADC 이름", "ADC 주소", "사용율", "전체
				// 개수", "사용수", "사용포트정보", "미사용포트정보"};
				String[] row = new String[portUsageHederInfo.length];
				row[0] = Integer.toString(i);
				row[1] = tmp.getAdcName();
				row[2] = tmp.getAdcIPAddress();
				int rate = tmp.getUsedCount() * 100 / (tmp.getNotUsedCount() + tmp.getUsedCount());
				row[3] = Integer.toString(rate);
				row[4] = Integer.toString(tmp.getNotUsedCount() + tmp.getUsedCount());
				row[5] = Integer.toString(tmp.getUsedCount());
				row[6] = tmp.getUsedInfo();
				row[7] = tmp.getNotUsedInfo();

				data.add(row);
			}
			return data;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public String slbSessionGetContent(String adcIp, String adcType, String accountId, String password, int connService,
			int connPort, int ipType, String ip) throws OBException {// telnet 접속을 통한 alteon session 정보 추출
		OBAdcAlteonHandler alteon = new OBAdcAlteonHandler();
		alteon.setConnectionInfo(adcIp, accountId, password, connService, connPort);
		if (ipType == 0) {// 검색 조건이 없을 때
			try {
				alteon.login();
				String infoSess = alteon.cmndInfoSessDump();
				alteon.disconnect();
				String temp[] = infoSess.split("dump", 2);
				String temp1[] = temp[1].split(">>", 2);
				return temp1[0];
			} catch (OBExceptionUnreachable e) {
				e.printStackTrace();
			} catch (OBExceptionLogin e) {
				e.printStackTrace();
			}
		} else if (ipType == 1) {// 검색 조건에 Source IP가 들어 갈때
			try {
				alteon.login();
				String infoSess = alteon.cmndInfoSessCip(ip, 100); // 100개 결과 조회
				alteon.disconnect();
				String temp[] = infoSess.split("cip", 2);
				String temp1[] = temp[1].split(">>", 2);
				return temp1[0];
			} catch (OBExceptionUnreachable e) {
				e.printStackTrace();
			} catch (OBExceptionLogin e) {
				e.printStackTrace();
			}
		} else if (ipType == 2) {// 검색 조건에 Destination IP가 들어 갈때
			try {
				alteon.login();
				String infoSess = alteon.cmndInfoSessDip(ip, 100); // 100개 결과 조회
				alteon.disconnect();
				String temp[] = infoSess.split("dip", 2);
				String temp1[] = temp[1].split(">>", 2);
				return temp1[0];
			} catch (OBExceptionUnreachable e) {
				e.printStackTrace();
			} catch (OBExceptionLogin e) {
				e.printStackTrace();
			}
			return null;
		}
		String infoSess = "서비스 처리 중 오류가 발생 했습니다.";
		return infoSess;
	}

//	public static void main(String[] args) 
//	{
//		try
//		{
//			System.out.println(new OBSystemToolsImpl().getUnUsedPoolNodeList(4));
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}

	private ArrayList<OBDtoPoolNodeInfo> getUnUsedPoolNodeList(Integer adcIndex) throws OBException {
		// ADC 이름: 전체 개수: 사용개수: 미사용개수: 사용 상세정보: 미 사용상세정보: 시간.
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		HashMap<Integer, OBDtoPoolNodeInfo> hashMap = new HashMap<Integer, OBDtoPoolNodeInfo>();
		try {
			db.openDB();

			OBDtoAdcInfo adcInfo = null;
			if (adcIndex != null && !adcIndex.equals(0))
				adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			// pool name 추출 .
			if (adcIndex == null || adcIndex.equals(0)) {// 전체를 대상으로 조회.
				sqlText = String.format(
						" SELECT 'F5PIO' AS TYPE, B.NAME AS ADC_NAME, B.IPADDRESS AS ADC_IPADDRESS, A.ADC_INDEX AS ADC_INDEX, \n"
								+ "         A.INDEX AS POOL_INDEX, A.NAME AS POOL_NAME, -1 AS ALTEON_ID                                 \n"
								+ // -- 안 쓰는 POOL/GROUP , F5
								" FROM TMP_SLB_POOL A                                                                                  \n"
								+ " LEFT JOIN MNG_ADC B                                                                                  \n"
								+ " ON A.ADC_INDEX = B.INDEX                                                                             \n"
								+ " WHERE A.INDEX NOT IN (                                                                                 \n"
								+ "                    SELECT POOL_INDEX                                                                 \n"
								+ "                    FROM TMP_SLB_VSERVER                                                              \n"
								+ "                    WHERE ADC_INDEX IN (SELECT INDEX FROM MNG_ADC WHERE TYPE != %d AND AVAILABLE = %d)\n"
								+ "                     )                                                                                \n"
								+ " AND ADC_INDEX IN (SELECT INDEX FROM MNG_ADC WHERE TYPE != %d AND AVAILABLE = %d)                     \n"
								+ " UNION ALL                                                                                            \n"
								+ " SELECT 'ALTEON' AS TYPE, B.NAME AS ADC_NAME, B.IPADDRESS AS ADC_IPADDRESS, A.ADC_INDEX AS ADC_INDEX, \n"
								+ "         A.INDEX AS POOL_INDEX, A.NAME, A.ALTEON_ID AS POOL_NAME                                        \n"
								+ // -- 안 쓰는 POOL/GROUP , ALTEON
								" FROM TMP_SLB_POOL A                                                                                  \n"
								+ " LEFT JOIN MNG_ADC B                                                                                  \n"
								+ " ON A.ADC_INDEX = B.INDEX                                                                             \n"
								+ " WHERE A.INDEX NOT IN (                                                                                 \n"
								+ "                    SELECT POOL_INDEX                                                                 \n"
								+ "                    FROM TMP_SLB_VS_SERVICE                                                           \n"
								+ "                    WHERE ADC_INDEX IN (SELECT INDEX FROM MNG_ADC WHERE TYPE = %d AND AVAILABLE = %d) \n"
								+ "                    )                                                                                 \n"
								+ " AND ADC_INDEX IN (SELECT INDEX FROM MNG_ADC WHERE TYPE = %d AND AVAILABLE = %d)                      \n",
						OBDefine.ADC_TYPE_ALTEON, OBDefine.ADC_STATE.AVAILABLE, OBDefine.ADC_TYPE_ALTEON,
						OBDefine.ADC_STATE.AVAILABLE, OBDefine.ADC_TYPE_ALTEON, OBDefine.ADC_STATE.AVAILABLE,
						OBDefine.ADC_TYPE_ALTEON, OBDefine.ADC_STATE.AVAILABLE);
			} else {// 지정된 adc를 대상으로 조회.

				if (adcInfo.getAdcType() != OBDefine.ADC_TYPE_ALTEON) {
					sqlText = String.format(
							" SELECT 'F4PIO' AS TYPE, B.NAME AS ADC_NAME, B.IPADDRESS AS ADC_IPADDRESS, A.ADC_INDEX AS ADC_INDEX, \n"
									+ "         A.INDEX AS POOL_INDEX, A.NAME AS POOL_NAME, -1 AS ALTEON_ID                                 \n"
									+ // -- 안 쓰는 POOL/GROUP , F5
									" FROM TMP_SLB_POOL A                                                                                  \n"
									+ " LEFT JOIN MNG_ADC B                                                                                  \n"
									+ " ON A.ADC_INDEX = B.INDEX                                                                             \n"
									+ " WHERE A.INDEX NOT IN (                                                                               \n"
									+ "                    SELECT POOL_INDEX                                                                 \n"
									+ "                    FROM TMP_SLB_VSERVER                                                              \n"
									+ "                    WHERE ADC_INDEX = %d                                                              \n"
									+ "                     )                                                                                \n"
									+ " AND ADC_INDEX = %d                                                                                   \n",
							adcIndex, adcIndex);
				} else {
					sqlText = String.format(
							" SELECT 'ALTEON' AS TYPE, B.NAME AS ADC_NAME, B.IPADDRESS AS ADC_IPADDRESS, A.ADC_INDEX AS ADC_INDEX, \n"
									+ "         A.INDEX AS POOL_INDEX, A.NAME AS POOL_NAME, A.ALTEON_ID AS ALTEON_ID                                     \n"
									+ // -- 안 쓰는 POOL/GROUP , ALTEON
									" FROM TMP_SLB_POOL A                                                                                  \n"
									+ " LEFT JOIN MNG_ADC B                                                                                  \n"
									+ " ON A.ADC_INDEX = B.INDEX                                                                             \n"
									+ " WHERE A.INDEX NOT IN (                                                                               \n"
									+ "                    SELECT POOL_INDEX                                                                 \n"
									+ "                    FROM TMP_SLB_VS_SERVICE                                                           \n"
									+ "                    WHERE ADC_INDEX = %d                                                              \n"
									+ "                    )                                                                                 \n"
									+ " AND ADC_INDEX = %d                                                                                   \n",
							adcIndex, adcIndex);
				}
			}
			sqlText += " ORDER BY TYPE, ADC_INDEX, POOL_INDEX, POOL_NAME ";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoPoolNodeInfo tmpInfo = hashMap.get(db.getInteger(rs, "ADC_INDEX"));
				if (null == tmpInfo) {
					tmpInfo = new OBDtoPoolNodeInfo();
					tmpInfo.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
					tmpInfo.setAdcIPAddress(db.getString(rs, "ADC_IPADDRESS"));
					tmpInfo.setAdcName(db.getString(rs, "ADC_NAME"));
					tmpInfo.setUnUsedNodeNameList(new ArrayList<String>());
					tmpInfo.setUnUsedPoolNameList(new ArrayList<String>());
					tmpInfo.setUnUsedNodeIDList(new ArrayList<Integer>());
					tmpInfo.setUnUsedPoolIDList(new ArrayList<Integer>());

					tmpInfo.getUnUsedPoolNameList().add(db.getString(rs, "POOL_NAME"));
					tmpInfo.getUnUsedPoolIDList().add(db.getInteger(rs, "ALTEON_ID"));
					hashMap.put(adcIndex, tmpInfo);
					continue;
				}
				tmpInfo.getUnUsedPoolNameList().add(db.getString(rs, "POOL_NAME"));
				tmpInfo.getUnUsedPoolIDList().add(db.getInteger(rs, "ALTEON_ID"));
			}

			// node list 추출 .
			if (adcIndex == null || adcIndex.equals(0)) {// 전체를 대상으로 조회.
				sqlText = String.format(
						" SELECT 'F5PIO' AS TYPE, B.NAME AS ADC_NAME, B.IPADDRESS AS ADC_IPADDRESS, A.ADC_INDEX AS ADC_INDEX, \n"
								+ "         A.INDEX AS NODE_INDEX, A.NAME AS NODE_NAME, -1 AS ALTEON_ID                                 \n"
								+ // -- 안 쓰는 POOL/GROUP , F5
								" FROM TMP_SLB_NODE A                                                                                  \n"
								+ " LEFT JOIN MNG_ADC B                                                                                  \n"
								+ " ON A.ADC_INDEX = B.INDEX                                                                             \n"
								+ " WHERE A.INDEX NOT IN (                                                                                 \n"
								+ "                    SELECT NODE_INDEX                                                                 \n"
								+ "                    FROM TMP_SLB_POOLMEMBER                                                           \n"
								+ "                    WHERE POOL_INDEX IN (                                                             \n"
								+ "                                        SELECT POOL_INDEX FROM TMP_SLB_VSERVER                        \n"
								+ "                                        WHERE ADC_INDEX IN (                                          \n"
								+ "                                                            SELECT INDEX FROM MNG_ADC WHERE TYPE != %d AND AVAILABLE = %d \n"
								+ "                                                            )                                         \n"
								+ "                                        )                                                             \n"
								+ "                     )                                                                                \n"
								+ " AND ADC_INDEX IN (SELECT INDEX FROM MNG_ADC WHERE TYPE != %d AND AVAILABLE = %d)                     \n"
								+ " UNION ALL                                                                                            \n"
								+ " SELECT 'ALTEON' AS TYPE, B.NAME AS ADC_NAME, B.IPADDRESS AS ADC_IPADDRESS, A.ADC_INDEX AS ADC_INDEX, \n"
								+ "         A.INDEX AS NODE_INDEX, A.NAME AS NODE_NAME, ALTEON_ID                                        \n"
								+ // -- 안 쓰는 POOL/GROUP , ALTEON
								" FROM TMP_SLB_NODE A                                                                                  \n"
								+ " LEFT JOIN MNG_ADC B                                                                                  \n"
								+ " ON A.ADC_INDEX = B.INDEX                                                                             \n"
								+ " WHERE A.INDEX NOT IN (                                                                                 \n"
								+ "                    SELECT NODE_INDEX                                                                 \n"
								+ "                    FROM TMP_SLB_POOLMEMBER                                                           \n"
								+ "                    WHERE POOL_INDEX IN (                                                             \n"
								+ "                                        SELECT POOL_INDEX FROM TMP_SLB_VS_SERVICE                        \n"
								+ "                                        WHERE ADC_INDEX IN (                                          \n"
								+ "                                                            SELECT INDEX FROM MNG_ADC WHERE TYPE != %d AND AVAILABLE = %d \n"
								+ "                                                            )                                         \n"
								+ "                                        )                                                             \n"
								+ "                     )                                                                                \n"
								+ " AND ADC_INDEX IN (SELECT INDEX FROM MNG_ADC WHERE TYPE = %d AND AVAILABLE = %d)                      \n"
								+ " ORDER BY TYPE, ADC_INDEX, NODE_INDEX, NODE_NAME                                                                \n",
						OBDefine.ADC_TYPE_ALTEON, OBDefine.ADC_STATE.AVAILABLE, OBDefine.ADC_TYPE_ALTEON,
						OBDefine.ADC_STATE.AVAILABLE, OBDefine.ADC_TYPE_ALTEON, OBDefine.ADC_STATE.AVAILABLE,
						OBDefine.ADC_TYPE_ALTEON, OBDefine.ADC_STATE.AVAILABLE);
			} else {// 지정된 adc만을 대상으로 조회.
				if (adcInfo.getAdcType() != OBDefine.ADC_TYPE_ALTEON) {
					sqlText = String.format(
							" SELECT 'F5PIO' AS TYPE, B.NAME AS ADC_NAME, B.IPADDRESS AS ADC_IPADDRESS, A.ADC_INDEX AS ADC_INDEX, \n"
									+ "         A.INDEX AS NODE_INDEX, A.IP_ADDRESS AS NODE_NAME, -1 AS ALTEON_ID                                 \n"
									+ // -- 안 쓰는 POOL/GROUP , F5
									" FROM TMP_SLB_NODE A                                                                                  \n"
									+ " LEFT JOIN MNG_ADC B                                                                                  \n"
									+ " ON A.ADC_INDEX = B.INDEX                                                                             \n"
									+ " WHERE A.INDEX NOT IN (                                                                                 \n"
									+ "                    SELECT NODE_INDEX                                                                 \n"
									+ "                    FROM TMP_SLB_POOLMEMBER                                                           \n"
									+ "                    WHERE POOL_INDEX IN (                                                             \n"
									+ "                                        SELECT POOL_INDEX FROM TMP_SLB_VSERVER                        \n"
									+ "                                        WHERE ADC_INDEX = %d                                          \n"
									+ "                                        )                                                             \n"
									+ "                     )                                                                                \n"
									+ " AND ADC_INDEX = %d                                                                                   \n"
									+ " ORDER BY TYPE, ADC_INDEX, NODE_INDEX, NODE_NAME                                                      \n",
							adcIndex, adcIndex);
				} else {
					sqlText = String.format(
							" SELECT 'ALTEON' AS TYPE, B.NAME AS ADC_NAME, B.IPADDRESS AS ADC_IPADDRESS, A.ADC_INDEX AS ADC_INDEX, \n"
									+ "         A.INDEX AS NODE_INDEX, A.NAME AS NODE_NAME, ALTEON_ID                                        \n"
									+ // -- 안 쓰는 POOL/GROUP , ALTEON
									" FROM TMP_SLB_NODE A                                                                                  \n"
									+ " LEFT JOIN MNG_ADC B                                                                                  \n"
									+ " ON A.ADC_INDEX = B.INDEX                                                                             \n"
									+ " WHERE A.INDEX NOT IN (                                                                               \n"
									+ "                    SELECT NODE_INDEX                                                                 \n"
									+ "                    FROM TMP_SLB_POOLMEMBER                                                           \n"
									+ "                    WHERE POOL_INDEX IN (                                                             \n"
									+ "                                        SELECT POOL_INDEX FROM TMP_SLB_VS_SERVICE                     \n"
									+ "                                        WHERE ADC_INDEX = %d                                          \n"
									+ "                                        )                                                             \n"
									+ "                     )                                                                                \n"
									+ " AND ADC_INDEX = %d                                                                                  \n"
									+ " ORDER BY TYPE, ADC_INDEX, NODE_INDEX, NODE_NAME                                                                \n",
							adcIndex, adcIndex);
				}
			}

			rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoPoolNodeInfo tmpInfo = hashMap.get(db.getInteger(rs, "ADC_INDEX"));
				if (null == tmpInfo) {
					tmpInfo = new OBDtoPoolNodeInfo();
					tmpInfo.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
					tmpInfo.setAdcIPAddress(db.getString(rs, "ADC_IPADDRESS"));
					tmpInfo.setAdcName(db.getString(rs, "ADC_NAME"));
					tmpInfo.setUnUsedNodeNameList(new ArrayList<String>());
					tmpInfo.setUnUsedPoolNameList(new ArrayList<String>());
					tmpInfo.setUnUsedNodeIDList(new ArrayList<Integer>());
					tmpInfo.setUnUsedPoolIDList(new ArrayList<Integer>());

					tmpInfo.getUnUsedNodeNameList().add(db.getString(rs, "NODE_NAME"));
					tmpInfo.getUnUsedNodeIDList().add(db.getInteger(rs, "ALTEON_ID"));
					hashMap.put(tmpInfo.getAdcIndex(), tmpInfo);
					continue;
				}
				tmpInfo.getUnUsedNodeNameList().add(db.getString(rs, "NODE_NAME"));
				tmpInfo.getUnUsedNodeIDList().add(db.getInteger(rs, "ALTEON_ID"));
			}

			ArrayList<OBDtoPoolNodeInfo> list = new ArrayList<OBDtoPoolNodeInfo>(hashMap.values());
			return list;
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
	public String unUsedSlbInfoContent(Integer accntIndex, Integer adcIndex, Integer searchType) throws OBException {
		String retVal = "";
		retVal += String.format("%-20s| %-20s| %-15s| %-5s| %s\n\n", unUsedSlbInfoHeaderInfo[0],
				unUsedSlbInfoHeaderInfo[1], unUsedSlbInfoHeaderInfo[2], unUsedSlbInfoHeaderInfo[3],
				unUsedSlbInfoHeaderInfo[4]);

		retVal += String.format(
				"===============================================================================================================\n");
		try {
			ArrayList<OBDtoPoolNodeInfo> list = getUnUsedPoolNodeList(adcIndex);

			for (OBDtoPoolNodeInfo tmp : list) {
				if (searchType.equals(0) || searchType.equals(1)) {
					if (tmp.getUnUsedPoolNameList() != null) {
						for (int i = 0; i < tmp.getUnUsedPoolNameList().size(); i++) {
							String poolName = tmp.getUnUsedPoolNameList().get(i);
							Integer poolID = tmp.getUnUsedPoolIDList().get(i);
							if (poolID <= 0) {
								retVal += String.format("%-20s| %-20s| %-15s| %-5s| %s\n", tmp.getAdcName(),
										tmp.getAdcIPAddress(), "POOL", "-", poolName);
							} else {
								retVal += String.format("%-20s| %-20s| %-15s| %-5d| %s\n", tmp.getAdcName(),
										tmp.getAdcIPAddress(), "POOL", poolID, poolName);
							}
						}
					}
				}
				if (searchType.equals(0) || searchType.equals(2)) {
					if (tmp.getUnUsedNodeNameList() != null) {
						for (int i = 0; i < tmp.getUnUsedNodeNameList().size(); i++) {
							String nodeName = tmp.getUnUsedNodeNameList().get(i);
							Integer nodeID = tmp.getUnUsedNodeIDList().get(i);
							if (nodeID <= 0) {
								retVal += String.format("%-20s| %-20s| %-15s| %-5s| %s\n", tmp.getAdcName(),
										tmp.getAdcIPAddress(), "NODE", "-", nodeName);
							} else {
								retVal += String.format("%-20s| %-20s| %-15s| %-5d| %s\n", tmp.getAdcName(),
										tmp.getAdcIPAddress(), "NODE", nodeID, nodeName);
							}
						}
					}
				}
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private String[] unUsedSlbInfoHeaderInfo = { "ADC Name", "ADC IP", "TYPE", "ID", "NAME", };

	@Override
	public String[] unUsedSlbInfoCsvHeader(Integer accntIndex, Integer adcIndex) throws OBException {
		return unUsedSlbInfoHeaderInfo;
	}

	@Override
	public ArrayList<String[]> unUsedSlbInfoCsvBody(Integer accntIndex, Integer adcIndex) throws OBException {
		try {
			ArrayList<OBDtoPoolNodeInfo> list = getUnUsedPoolNodeList(adcIndex);
			ArrayList<String[]> data = new ArrayList<String[]>(list.size());

			for (OBDtoPoolNodeInfo tmp : list) {
				if (tmp.getUnUsedPoolNameList() != null) {
					for (int i = 0; i < tmp.getUnUsedPoolNameList().size(); i++) {
						String poolName = tmp.getUnUsedPoolNameList().get(i);
						Integer poolID = tmp.getUnUsedPoolIDList().get(i);
						String[] row = new String[unUsedSlbInfoHeaderInfo.length];
						row[0] = tmp.getAdcName();
						row[1] = tmp.getAdcIPAddress();
						row[2] = "POOL";
						row[3] = "-";
						row[4] = poolName;
						if (poolID.equals(-1) == false) {
							row[3] = poolID.toString();
						}
						data.add(row);
					}
				}
				if (tmp.getUnUsedNodeNameList() != null) {
					for (int i = 0; i < tmp.getUnUsedNodeNameList().size(); i++) {
						String nodeName = tmp.getUnUsedNodeNameList().get(i);
						Integer nodeID = tmp.getUnUsedNodeIDList().get(i);
						String[] row = new String[unUsedSlbInfoHeaderInfo.length];
						row[0] = tmp.getAdcName();
						row[1] = tmp.getAdcIPAddress();
						row[2] = "NODE";
						row[3] = "-";
						row[4] = nodeName;
						if (nodeID.equals(-1) == false) {
							row[3] = nodeID.toString();
						}
						data.add(row);
					}
				}
			}
			return data;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}
}
