package kr.openbase.adcsmart.service.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kr.openbase.adcsmart.service.dashboard.dto.OBDtoADCList;
import kr.openbase.adcsmart.service.dashboard.dto.OBDtoVSList;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoVSFilterInfo;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBIntegratedDashboardDB {

	public ArrayList<OBDtoADCList> getAdcInfo() throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		ResultSet rs;

		ArrayList<OBDtoADCList> adcList = new ArrayList<OBDtoADCList>();

		try {
			db.openDB();

			sqlText = String.format(" SELECT INDEX, NAME, IPADDRESS, TYPE, STATUS	FROM MNG_ADC WHERE  AVAILABLE = "
					+ OBDefine.ADC_STATE.AVAILABLE + "\n");

			rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoADCList info = new OBDtoADCList();

				info.setIndex(db.getInteger(rs, "INDEX"));
				info.setName(db.getString(rs, "NAME"));
				info.setIpaddress(db.getString(rs, "IPADDRESS"));
				info.setStatus(db.getInteger(rs, "STATUS"));
				info.setType(db.getInteger(rs, "TYPE"));

				adcList.add(info);
			}

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

	/**
	 * 지정된 장비의 네트워크 맵 구성을 위한 virtual server의 상태 및 pool member의 상태 정보를 제공한다. F5의 경우에는
	 * virtual server 단위로, alteon인 경우에는 virtual service 단위로 구성한다. tmp_slb_vserver,
	 * tmp_slb_vs_service, tmp_slb_poolmember을 참조하여 구성한다.
	 * 
	 * @param searchKeys : null 가능. 지정된 검색어에 해당되는 정보만 추출하고자 할 경우에 사용. virtual 서버의
	 *                   이름, IP주소를 대상으로 검색한다.
	 */
	public OBDtoADCList getNetworkMapsF5(Integer adcIndex, String adcName, OBDtoADCList result,
			Map<String, OBDtoVSFilterInfo> vsFilterMap) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "start. adcIndex:" + adcIndex);

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String sqlLimit = "";

			String globalWhere = "";

			// virtual server, poolmember로 구성한다.
			sqlText = " SELECT ADC.MODEL, ADC.NAME, VS2.VS_INDEX VS_INDEX, VS2.VS_STATUS VS_STATUS, VS2.VS_NAME VS_NAME, VS2.VIRTUAL_IP VIRTUAL_IP, VS2.VIRTUAL_PORT VIRTUAL_PORT, \n"
					+ " VS2.GROUP, VS2.GROUP_NAME, VS2.POOL_INDEX, VS2.USE_YN, VS2.MEMBER_INDEX MEMBER_INDEX, VS2.MEMBER_PORT MEMBER_PORT, POOL.METRIC, \n"
					+ " VS2.NODE_IP_ADDRESS NODE_IP_ADDRESS, VS2.NODE_INDEX NODE_INDEX, MSTATUS2.MEMBER_STATUS MEMBER_STATUS \n"
					+ " FROM \n" + " (\n" + "    SELECT \n"
					+ "    VS.ADC_INDEX, VS.INDEX VS_INDEX, VS.STATUS VS_STATUS, VS.NAME VS_NAME, VS.VIRTUAL_IP VIRTUAL_IP, VS.VIRTUAL_PORT, VS.USE_YN, MEMBER.INDEX MEMBER_INDEX, \n"
					+ "    VS.POOL_INDEX, MEMBER.NODE_INDEX NODE_INDEX, MEMBER.PORT MEMBER_PORT, NODE.IP_ADDRESS NODE_IP_ADDRESS, \n"
					+ "    NODE.ALTEON_ID AS GROUP, VS.POOL_INDEX AS GROUP_NAME \n" + "    FROM \n"
					+ "        (SELECT ADC_INDEX, INDEX, STATUS, NAME, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX, USE_YN FROM TMP_SLB_VSERVER WHERE ADC_INDEX = "
					+ adcIndex + ") VS\n" + "        RIGHT JOIN \n"
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

			if (sqlLimit != null && !sqlLimit.isEmpty()) {
				sqlText += sqlLimit;
			}
			ResultSet rs = db.executeQuery(sqlText);
			String currentVsIndex = "";
			String newVsIndex = "";
			ArrayList<OBDtoVSList> vsList = new ArrayList<OBDtoVSList>();
			Integer vsPartDownCount = 0;
			Integer vsDownCount = 0;
			Integer vsFilteredCount = 0;
			Integer vsTotalCount = 0;
			boolean flag = false;

			Integer vsStatus = null;
			Integer memberStatus = null;

			while (rs.next()) {
				OBDtoVSList vs = new OBDtoVSList();

				newVsIndex = db.getString(rs, "VS_INDEX");
				vsStatus = db.getInteger(rs, "VS_STATUS");
				memberStatus = db.getInteger(rs, "MEMBER_STATUS");
				if (rs.wasNull()) {
					continue;
				}
				if (currentVsIndex.equals(newVsIndex) == false) // 새 vs 데이터 시작
				{
					flag = false;
					currentVsIndex = newVsIndex;
					if (vsStatus == OBDefine.VS_STATUS.UNAVAILABLE) {
						vs.setVsIndex(newVsIndex);
						vs.setVsName(db.getString(rs, "VS_NAME"));
						vs.setVsIP(db.getString(rs, "VIRTUAL_IP"));
						vs.setVsPort(db.getInteger(rs, "VIRTUAL_PORT"));
						vs.setVsStatus(vsStatus);

						String filterKey = makeVSFilterInfoIndex(adcIndex, vs.getVsIndex(), vs.getVsIP(),
								vs.getVsPort());
						if (vsFilterMap.get(filterKey) == null) {
							vsList.add(vs);
							vsDownCount++;
						} else {
							vsFilteredCount++;
						}

//						vsList.add(vs);
//						vsDownCount++;
						continue;
					}

					if (vsStatus == OBDefine.VS_STATUS.AVAILABLE
							&& memberStatus == OBDefine.MEMBER_STATUS.UNAVAILABLE) {
						vs.setVsIndex(newVsIndex);
						vs.setVsName(db.getString(rs, "VS_NAME"));
						vs.setVsIP(db.getString(rs, "VIRTUAL_IP"));
						vs.setVsPort(db.getInteger(rs, "VIRTUAL_PORT"));
						vs.setVsStatus(OBDefine.VS_STATUS.ERRORAVAILABLE);

						String filterKey = makeVSFilterInfoIndex(adcIndex, vs.getVsIndex(), vs.getVsIP(),
								vs.getVsPort());
						if (vsFilterMap.get(filterKey) == null) {
							vsList.add(vs);
							vsPartDownCount++;
						} else {
							vsFilteredCount++;
						}

//						vsList.add(vs);
//						vsPartDownCount++;
						flag = true;
					}

				} else {
					if (vsStatus == OBDefine.VS_STATUS.AVAILABLE && memberStatus == OBDefine.MEMBER_STATUS.UNAVAILABLE
							&& flag == false) {
						vs.setVsIndex(newVsIndex);
						vs.setVsName(db.getString(rs, "VS_NAME"));
						vs.setVsIP(db.getString(rs, "VIRTUAL_IP"));
						vs.setVsPort(db.getInteger(rs, "VIRTUAL_PORT"));
						vs.setVsStatus(OBDefine.VS_STATUS.ERRORAVAILABLE);

						String filterKey = makeVSFilterInfoIndex(adcIndex, vs.getVsIndex(), vs.getVsIP(),
								vs.getVsPort());
						if (vsFilterMap.get(filterKey) == null) {
							vsList.add(vs);
							vsPartDownCount++;
						} else {
							vsFilteredCount++;
						}
//						vsList.add(vs);
//						vsPartDownCount++;
						flag = true;
					}
				}
			}

			result.setVsList(vsList);
			result.setVsDownCount(vsDownCount);
			result.setVsPartDownCount(vsPartDownCount);
			result.setVsFilteredCount(vsFilteredCount);
			result.setVsCount(vsDownCount + vsPartDownCount);
			vsTotalCount = new OBIntegratedDashboardDB().getSvcPerfInfoTotalCountExceptAlteon(adcIndex);
			result.setVsTotalCount(vsTotalCount);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result :%s", result));
		return result;
	}

	public OBDtoADCList getNetworkMapsPASAndPASK(Integer adcIndex, String adcName, OBDtoADCList result,
			Map<String, OBDtoVSFilterInfo> vsFilterMap) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));

		OBDatabase db = new OBDatabase();

		String sqlText = "";
		try {
			db.openDB();
			// virtual server, poolmember로 구성한다.
			sqlText = String.format(
					"  SELECT A.INDEX, A.VIRTUAL_IP, A.VIRTUAL_PORT, A.STATUS, B.NODE_INDEX, B.PORT, D.MEMBER_STATUS		\n"
							+ " FROM (SELECT INDEX, STATUS, USE_YN, NAME, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d ) A   \n"
							+ " RIGHT JOIN (SELECT INDEX, POOL_INDEX, NODE_INDEX, PORT FROM TMP_SLB_POOLMEMBER WHERE ADC_INDEX = %d) B	\n"
							+ " ON B.POOL_INDEX = A.POOL_INDEX																			\n"
							+ " LEFT JOIN (SELECT INDEX, IP_ADDRESS FROM TMP_SLB_NODE WHERE ADC_INDEX = %d ) C        					\n"
							+ " ON B.NODE_INDEX=C.INDEX                                                                					\n"
							+ " LEFT JOIN (SELECT * FROM TMP_SLB_POOLMEMBER_STATUS WHERE ADC_INDEX = %d )D                               	\n"
							+ " ON B.INDEX=D.POOLMEMBER_INDEX                                                          					\n"
							+ " ORDER BY A.INDEX, D.MEMBER_STATUS, C.IP_ADDRESS ;     ",
					adcIndex, adcIndex, adcIndex, adcIndex);
			sqlText += ";";
			ResultSet rs = db.executeQuery(sqlText);

			ArrayList<OBDtoVSList> vsList = new ArrayList<OBDtoVSList>();
			Integer vsPartDownCount = 0;
			Integer vsDownCount = 0;
			Integer vsTotalCount = 0;
			String currentVsIndex = "";
			Integer vsFilteredCount = 0;
			String newVsIndex = "";
			boolean flag = false;

			Integer vsStatus = null;
			Integer memberStatus = null;

			while (rs.next()) {
				OBDtoVSList vs = new OBDtoVSList();

				newVsIndex = db.getString(rs, "INDEX");
				if (rs.wasNull()) {
					continue;
				}
				vsStatus = db.getInteger(rs, "STATUS");
				memberStatus = db.getInteger(rs, "MEMBER_STATUS");

				if (currentVsIndex.equals(newVsIndex) == false) // 새 vs 데이터 시작
				{
					flag = false;
					currentVsIndex = newVsIndex;
					if (vsStatus == OBDefine.VS_STATUS.UNAVAILABLE) {
						vs.setVsIndex(newVsIndex);
						vs.setVsName(db.getString(rs, "VIRTUAL_IP"));
						vs.setVsIP(db.getString(rs, "VIRTUAL_IP"));
						vs.setVsPort(db.getInteger(rs, "VIRTUAL_PORT"));
						vs.setVsStatus(vsStatus);

						String filterKey = makeVSFilterInfoIndex(adcIndex, vs.getVsIndex(), vs.getVsIP(),
								vs.getVsPort());
						if (vsFilterMap.get(filterKey) == null) {
							vsList.add(vs);
							vsDownCount++;
						} else {
							vsFilteredCount++;
						}
//						vsList.add(vs);
//						vsDownCount++;
						continue;
					}

					if (vsStatus == OBDefine.VS_STATUS.AVAILABLE
							&& memberStatus == OBDefine.MEMBER_STATUS.UNAVAILABLE) {
						vs.setVsIndex(newVsIndex);
						vs.setVsName(db.getString(rs, "VIRTUAL_IP"));
						vs.setVsIP(db.getString(rs, "VIRTUAL_IP"));
						vs.setVsPort(db.getInteger(rs, "VIRTUAL_PORT"));
						vs.setVsStatus(OBDefine.VS_STATUS.ERRORAVAILABLE);

						String filterKey = makeVSFilterInfoIndex(adcIndex, vs.getVsIndex(), vs.getVsIP(),
								vs.getVsPort());
						if (vsFilterMap.get(filterKey) == null) {
							vsList.add(vs);
							vsPartDownCount++;
						} else {
							vsFilteredCount++;
						}
//						vsList.add(vs);
//						vsPartDownCount++;
						flag = true;
					}
				} else {
					if (vsStatus == OBDefine.VS_STATUS.AVAILABLE && memberStatus == OBDefine.MEMBER_STATUS.UNAVAILABLE
							&& flag == false) {
						vs.setVsIndex(newVsIndex);
						vs.setVsName(db.getString(rs, "VIRTUAL_IP"));
						vs.setVsIP(db.getString(rs, "VIRTUAL_IP"));
						vs.setVsPort(db.getInteger(rs, "VIRTUAL_PORT"));
						vs.setVsStatus(OBDefine.VS_STATUS.ERRORAVAILABLE);

						String filterKey = makeVSFilterInfoIndex(adcIndex, vs.getVsIndex(), vs.getVsIP(),
								vs.getVsPort());
						if (vsFilterMap.get(filterKey) == null) {
							vsList.add(vs);
							vsPartDownCount++;
						} else {
							vsFilteredCount++;
						}
//						vsList.add(vs);
//						vsPartDownCount++;
						flag = true;
					}
				}
			}
			result.setVsList(vsList);
			result.setVsDownCount(vsDownCount);
			result.setVsPartDownCount(vsPartDownCount);
			result.setVsFilteredCount(vsFilteredCount);
			result.setVsCount(vsDownCount + vsPartDownCount);
			vsTotalCount = new OBIntegratedDashboardDB().getSvcPerfInfoTotalCountExceptAlteon(adcIndex);
			result.setVsTotalCount(vsTotalCount);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result :%s", result));
		return result;
	}

	public Integer getSvcPerfInfoTotalCountAlteon(Integer adcIndex) throws OBException {
		String sqlText = "";
		Integer result = 0;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT COUNT(A.ADC_INDEX) AS CNT \n"
					+ " FROM TMP_SLB_VS_SERVICE    A                                                    \n"
					+ " INNER JOIN TMP_SLB_VSERVER B                                                    \n"
					+ " ON B.INDEX = A.VS_INDEX           		                                      \n"
					+ " WHERE A.ADC_INDEX = %d                    \n", adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true)
				result = db.getInteger(rs, "CNT");
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", result));
		return result;
	}

	public Integer getSvcPerfInfoTotalCountExceptAlteon(Integer adcIndex) throws OBException {
		String sqlText = "";
		Integer result = 0;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT COUNT(ADC_INDEX) AS CNT	             \n"
					+ "     FROM TMP_SLB_VSERVER        				 \n"
					+ " WHERE ADC_INDEX = %d                    \n", adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true)
				result = db.getInteger(rs, "CNT");
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", result));
		return result;
	}

	public OBDtoADCList getNetworkMapsAlteonSlbVService(Integer adcIndex, String adcName, OBDtoADCList result,
			Map<String, OBDtoVSFilterInfo> vsFilterMap) throws OBException {// virtual
		// service,
		// poolmember로
		// 구성한다.
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			// vs - index, alteon id, ip / vss - index, status, port / member - port, ip,
			// status
			// 2개의 서비스에서 동일 그룹을 사용하고 있음 -> INDEX 조건 추가 (Alteon service : index ,
			// poolmemberStatus : vsrc_index) _ 2015.02.24 추가
			sqlText = String.format(
					" SELECT VS.INDEX VS_INDEX, VS.NAME VS_NAME, VS.VIRTUAL_IP VIRTUAL_IP, VS.ALTEON_ID, POOL.ALTEON_ID AS GROUP_ID,                     \n"
							+ "     VSS.INDEX SERVICE_INDEX, VSS.STATUS, VSS.VIRTUAL_PORT, --서비스 바뀌는 것을 확인하는데만 쓴다.         \n"
							+ "     VSS.REAL_PORT AS RPORT, MEMBER.NODE_INDEX, VSS.REAL_PORT MEMBER_PORT,                                               		 \n"
							+ "     POOL.BAK_TYPE POOL_BAK_TYPE, POOL.BAK_ID POOL_BAK_ID,                                                                        \n"
							+ "     NODE.BAK_TYPE NODE_BAK_TYPE, NODE.BAK_ID NODE_BAK_ID,                                                \n"
							+ "     NODE.IP_ADDRESS NODE_IP_ADDRESS, NODE.INDEX NODE_INDEX, MSTATUS.MEMBER_STATUS             						             \n"
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

			ResultSet rs = db.executeQuery(sqlText);

			ArrayList<OBDtoVSList> vsList = new ArrayList<OBDtoVSList>();
			Integer vsPartDownCount = 0;
			Integer vsDownCount = 0;
			Integer vsTotalCount = 0;
			Integer vsFilteredCount = 0;
			String currentVsIndex = "";
			String newVsIndex = "";
			boolean flag = false;

			Integer vsStatus = null;
			Integer memberStatus = null;

			while (rs.next()) {
				OBDtoVSList vs = new OBDtoVSList();

				newVsIndex = db.getString(rs, "SERVICE_INDEX");
				if (rs.wasNull()) {
					continue;
				}
				vsStatus = db.getInteger(rs, "STATUS");
				memberStatus = db.getInteger(rs, "MEMBER_STATUS");

				if (currentVsIndex.equals(newVsIndex) == false) // 새 vs 데이터 시작
				{
					flag = false;
					currentVsIndex = newVsIndex;
					if (vsStatus == OBDefine.VS_STATUS.UNAVAILABLE) {
						vs.setVsIndex(newVsIndex);
						vs.setVsName(db.getString(rs, "VS_NAME"));
						vs.setVsIP(db.getString(rs, "VIRTUAL_IP"));
						vs.setVsPort(db.getInteger(rs, "VIRTUAL_PORT"));
						vs.setVsStatus(vsStatus);

						String filterKey = makeVSFilterInfoIndex(adcIndex, vs.getVsIndex(), vs.getVsIP(),
								vs.getVsPort());
						if (vsFilterMap.get(filterKey) == null) {
							vsList.add(vs);
							vsDownCount++;
						} else {
							vsFilteredCount++;
						}
						continue;
					}

					if (vsStatus == OBDefine.VS_STATUS.AVAILABLE
							&& memberStatus == OBDefine.MEMBER_STATUS.UNAVAILABLE) {
						vs.setVsIndex(newVsIndex);
						vs.setVsName(db.getString(rs, "VS_NAME"));
						vs.setVsIP(db.getString(rs, "VIRTUAL_IP"));
						vs.setVsPort(db.getInteger(rs, "VIRTUAL_PORT"));
						vs.setVsStatus(OBDefine.VS_STATUS.ERRORAVAILABLE);

						String filterKey = makeVSFilterInfoIndex(adcIndex, vs.getVsIndex(), vs.getVsIP(),
								vs.getVsPort());
						if (vsFilterMap.get(filterKey) == null) {
							vsList.add(vs);
							vsPartDownCount++;
						} else {
							vsFilteredCount++;
						}

						flag = true;
					}
				} else {
					if (vsStatus == OBDefine.VS_STATUS.AVAILABLE && memberStatus == OBDefine.MEMBER_STATUS.UNAVAILABLE
							&& flag == false) {
						vs.setVsIndex(newVsIndex);
						vs.setVsName(db.getString(rs, "VS_NAME"));
						vs.setVsIP(db.getString(rs, "VIRTUAL_IP"));
						vs.setVsPort(db.getInteger(rs, "VIRTUAL_PORT"));
						vs.setVsStatus(OBDefine.VS_STATUS.ERRORAVAILABLE);

						String filterKey = makeVSFilterInfoIndex(adcIndex, vs.getVsIndex(), vs.getVsIP(),
								vs.getVsPort());
						if (vsFilterMap.get(filterKey) == null) {
							vsList.add(vs);
							vsPartDownCount++;
						} else {
							vsFilteredCount++;
						}
						flag = true;
					}
				}
			}
			result.setVsList(vsList);
			result.setVsDownCount(vsDownCount);
			result.setVsFilteredCount(vsFilteredCount);
			result.setVsPartDownCount(vsPartDownCount);
			result.setVsCount(vsDownCount + vsPartDownCount);

			vsTotalCount = new OBIntegratedDashboardDB().getSvcPerfInfoTotalCountAlteon(adcIndex);
			result.setVsTotalCount(vsTotalCount);

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

	private String makeVSFilterInfoIndex(Integer adcIndex, String vsIndex, String vsIP, Integer vsPort) {
		return adcIndex + "_" + vsIndex + "_" + vsIP + "_" + vsPort;
	}

	private String makeVSFilterInfoIndex(OBDtoVSFilterInfo filterInfo) {
		if (filterInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
			return filterInfo.getAdcIndex() + "_" + filterInfo.getVsvcIndex() + "_" + filterInfo.getVsIPAddress() + "_"
					+ filterInfo.getVsPort();
		else
			return filterInfo.getAdcIndex() + "_" + filterInfo.getVsIndex() + "_" + filterInfo.getVsIPAddress() + "_"
					+ filterInfo.getVsPort();
	}

	public List<OBDtoVSFilterInfo> getDashboardVSFilterList() throws OBException {// virtual service, poolmember로 구성한다.
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		List<OBDtoVSFilterInfo> results = new ArrayList<OBDtoVSFilterInfo>();
		try {
			db.openDB();
			sqlText = String.format(" SELECT * FROM MNG_DASHBOARD_VS_FILTER");

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoVSFilterInfo filter = new OBDtoVSFilterInfo();
				filter.setIndex(db.getString(rs, "INDEX"));
				filter.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				filter.setVsIndex(db.getString(rs, "VS_INDEX"));
				filter.setAdcType(db.getInteger(rs, "ADC_TYPE"));
				filter.setVsIPAddress(db.getString(rs, "VIRTUAL_IP"));
				filter.setVsvcIndex(db.getString(rs, "VSVS_INDEX"));
				filter.setVsPort(db.getInteger(rs, "VIRTUAL_PORT"));
				filter.setShowHideState(db.getInteger(rs, "STATE"));
				results.add(filter);
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

		return results;
	}

	public void removeDashboardVSFilters(List<String> indexList) throws OBException {
		if (indexList.size() <= 0)
			return;

		// where clause 구성
		String whereClause = "";
		for (String index : indexList) {
			if (!whereClause.isEmpty()) {
				whereClause += ", ";
			}
			whereClause += String.format("%s", OBParser.sqlString(index));
		}
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" DELETE FROM MNG_DASHBOARD_VS_FILTER WHERE INDEX IN (%s);", whereClause);
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

	public int getDashboardVSFilterCount(String searchKey) throws Exception {
		List<OBDtoVSFilterInfo> list = getAbnormalVSStatusList(searchKey, null, null, null, null);
		return list.size();
//		
//		String sqlText = "";
//		Integer result = 0;
//		final OBDatabase db = new OBDatabase();
//		try {
//			db.openDB();
//
//			sqlText = String.format("                                                                     \n"
//					+ "SELECT COUNT(*) AS CNT                                                             \n"
//					+ "FROM (                                                                             \n"
//					+ "     SELECT VSVC.ADC_INDEX, MNGADC.NAME, VSERVER.VIRTUAL_IP                        \n"
//					+ "     FROM TMP_SLB_VS_SERVICE VSVC                                                  \n"
//					+ "     INNER JOIN TMP_SLB_VSERVER VSERVER                                            \n"
//					+ "           ON VSERVER.INDEX = VSVC.VS_INDEX                                        \n"
//					+ "     INNER JOIN (                                                                  \n"
//					+ "                 SELECT INDEX                                                      \n"
//					+ "                 FROM MNG_ADC                                                      \n"
//					+ "                 WHERE AVAILABLE =1 AND TYPE =2                                    \n"
//					+ "                ) MNGADC                                                           \n"
//					+ "           ON MNGADC.INDEX=VSERVER.ADC_INDEX                                       \n"
//					+ "   UNION                                                                           \n"
//					+ "     SELECT VSERVER.ADC_INDEX, MNGADC.NAME, VSERVER.VIRTUAL_IP                        \n"
//					+ "     FROM TMP_SLB_VSERVER VSERVER                                                  \n"
//					+ "     INNER JOIN (                                                                  \n"
//					+ "                 SELECT INDEX                                                      \n"
//					+ "                 FROM MNG_ADC                                                      \n"
//					+ "                 WHERE AVAILABLE =1 AND TYPE !=2) MNGADC                           \n"
//					+ "            ON MNGADC.INDEX=VSERVER.ADC_INDEX                                      \n"
//					+ ") TOTAL                                                                            \n");
//			if (searchKey != null && !searchKey.isEmpty()) {
//				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
//				sqlText += String.format("WHERE TOTAL.NAME LIKE %s OR TOTAL.VIRTUAL_IP LIKE %s",
//						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
//			}
//
//			ResultSet rs = db.executeQuery(sqlText);
//			if (rs.next() == true)
//				result = db.getInteger(rs, "CNT");
//		} catch (SQLException e) {
//			throw new OBException(OBException.ERRCODE_DB_QEURY,
//					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
//		} catch (OBException e) {
//			throw e;
//		} catch (Exception e) {
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
//		} finally {
//			if (db != null)
//				db.closeDB();
//		}
//		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", result));
//		return result;
	}

	public Map<String, OBDtoVSFilterInfo> getDashboardVSFilterMap() throws Exception {
		List<OBDtoVSFilterInfo> allList = getDashboardVSFilterList();
		Map<String, OBDtoVSFilterInfo> filterMap = allList.stream()
				.collect(Collectors.toMap(OBDtoVSFilterInfo::getIndex, x -> x));

		return filterMap;
	}

	private List<OBDtoVSFilterInfo> getInsertFilterInfoList(List<OBDtoVSFilterInfo> targetList) throws Exception {
		Map<String, OBDtoVSFilterInfo> filterMap = getDashboardVSFilterMap();
		List<OBDtoVSFilterInfo> results = new ArrayList<OBDtoVSFilterInfo>();
		for (OBDtoVSFilterInfo filterInfo : targetList) {
			if (filterMap.get(filterInfo.getIndex()) == null) {
				results.add(filterInfo);
			}
		}
		return results;
	}

	public void addDashboardVSFilters(List<OBDtoVSFilterInfo> filterList) throws Exception {
		if (filterList.size() <= 0)
			return;

		String sqlBaseText = String.format(" INSERT INTO MNG_DASHBOARD_VS_FILTER "
				+ " (INDEX, ADC_INDEX, VS_INDEX, VSVS_INDEX, VIRTUAL_IP, VIRTUAL_PORT, ADC_TYPE, STATE) " + " VALUES ");

		// where clause 구성
		String sqlDataText = "";
		List<OBDtoVSFilterInfo> addList = getInsertFilterInfoList(filterList);
		if (addList.size() <= 0)
			return;

		for (OBDtoVSFilterInfo filterInfo : addList) {
			if (!sqlDataText.isEmpty())
				sqlDataText += ", ";
			sqlDataText += String.format(" (%s, %d, %s, %s, %s, %d, %d, %d) ",
					OBParser.sqlString(filterInfo.getIndex()), filterInfo.getAdcIndex(),
					OBParser.sqlString(filterInfo.getVsIndex()), OBParser.sqlString(filterInfo.getVsvcIndex()),
					OBParser.sqlString(filterInfo.getVsIPAddress()), filterInfo.getVsPort(), filterInfo.getAdcType(),
					OBDefine.HIDE_FLAG);
		}

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = sqlBaseText + sqlDataText;
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

	private String makeKeywordSearchClause(String searchKey) throws OBException {
		if (searchKey == null || searchKey.isEmpty())
			return "";
		String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
		return String.format("TOTAL.ADC_NAME LIKE %s OR TOTAL.VIRTUAL_IP LIKE %s", OBParser.sqlString(wildcardKey),
				OBParser.sqlString(wildcardKey));
	}

	private String makeOrderTypeNDirClause(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY TOTAL.ADC_NAME ASC NULLS LAST, TOTAL.VIRTUAL_IP ASC NULLS LAST, TOTAL.VIRTUAL_PORT ASC NULLS LAST ";
		if (orderType == null)
			return retVal;
		switch (orderType) {
		case OBDefine.ORDER_TYPE_FIRST:// VS 상태.
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY TOTAL.VS_STATUS ASC NULLS LAST, TOTAL.ADC_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY TOTAL.VS_STATUS DESC NULLS LAST, TOTAL.ADC_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SECOND:// ADC 이름
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY TOTAL.ADC_NAME ASC NULLS LAST, TOTAL.VIRTUAL_IP ASC NULLS LAST ";
			else
				retVal = " ORDER BY TOTAL.ADC_NAME DESC NULLS LAST, TOTAL.VIRTUAL_IP ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_THIRD:// VS IP
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY TOTAL.VIRTUAL_IP ASC NULLS LAST, TOTAL.ADC_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY TOTAL.VIRTUAL_IP DESC NULLS LAST, TOTAL.ADC_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_FOURTH:// VS PORT
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY TOTAL.VIRTUAL_PORT ASC NULLS LAST, TOTAL.ADC_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY TOTAL.VIRTUAL_PORT DESC NULLS LAST, TOTAL.ADC_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_FIFTH:// state. show/hide.
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY TOTAL.FILTER_STATE ASC NULLS LAST, TOTAL.ADC_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY TOTAL.FILTER_STATE DESC NULLS LAST, TOTAL.ADC_NAME ASC NULLS LAST ";
			break;
		}
		return retVal;
	}

	public List<OBDtoVSFilterInfo> getAbnormalVSStatusList(String searchKey, Integer fromRow, Integer toRow,
			Integer orderType, Integer orderDirw) throws Exception {// virtual
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		List<OBDtoVSFilterInfo> results = new ArrayList<OBDtoVSFilterInfo>();

		try {
			db.openDB();
//			SELECT total.index, total.vs_index, total.virtual_ip, total.virtual_port, total.adc_index, total.adc_name, total.adc_type, total.vs_status, total.member_status, total.FILTER_STATE, null AS VSVS_INDEX from (
//					 SELECT VSFILTER.index, ADC.INDEX ADC_INDEX, ADC.NAME ADC_NAME, ADC.TYPE ADC_TYPE, VSERVER.INDEX VS_INDEX, VSERVER.VIRTUAL_IP, VSERVER.VIRTUAL_PORT, VSERVER.STATUS VS_STATUS, D.MEMBER_STATUS, COALESCE(VSFILTER.STATE, 0) AS FILTER_STATE		
//					 FROM (SELECT INDEX, STATUS, USE_YN, NAME, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX, ADC_INDEX FROM TMP_SLB_VSERVER) VSERVER   
//					 RIGHT JOIN (SELECT INDEX, POOL_INDEX, NODE_INDEX, PORT FROM TMP_SLB_POOLMEMBER) POOLMEMBER	
//					 ON POOLMEMBER.POOL_INDEX = VSERVER.POOL_INDEX																			
//					 LEFT JOIN (SELECT INDEX, IP_ADDRESS FROM TMP_SLB_NODE) C        					
//					 ON POOLMEMBER.NODE_INDEX=C.INDEX                                                                					
//					 LEFT JOIN (SELECT * FROM TMP_SLB_POOLMEMBER_STATUS)D                               	
//					 ON POOLMEMBER.INDEX=D.POOLMEMBER_INDEX                              
//					 LEFT JOIN (SELECT * FROM MNG_ADC WHERE STATUS=1 AND AVAILABLE=1) ADC                               	
//					ON ADC.INDEX=VSERVER.ADC_INDEX		
//					FULL OUTER JOIN MNG_DASHBOARD_VS_FILTER VSFILTER   
//					ON VSERVER.INDEX = VSFILTER.VS_INDEX                                                                        
//					) TOTAL
//					where TOTAL.adc_type !=2
//					--and total.vs_status !=1 
//					and total.member_status=2
//					group by total.index, total.vs_index, total.virtual_ip, total.virtual_port, total.adc_index, total.adc_name, total.adc_type, total.vs_status, total.member_status, total.filter_state			

			sqlText = String.format(// vs_status=2인 목록 추출.
					"SELECT * from (                                                                                           \n"
							+ "SELECT INDEX, ADC_INDEX, ADC_NAME, ADC_TYPE, VS_INDEX, VIRTUAL_IP, VIRTUAL_PORT, VS_STATUS, FILTER_STATE, NULL AS VSVS_INDEX FROM ("
							+ "  SELECT * FROM (                                                                               \n"
							+ "  SELECT VSFILTER.INDEX, ADC.INDEX ADC_INDEX, ADC.NAME ADC_NAME, ADC.TYPE ADC_TYPE, VSERVER.INDEX VS_INDEX, VSERVER.VIRTUAL_IP, VSERVER.VIRTUAL_PORT, VSERVER.STATUS VS_STATUS, POOLMEMBER_STATUS.MEMBER_STATUS, COALESCE(VSFILTER.STATE, 0) AS FILTER_STATE		\n"
							+ "   FROM (SELECT INDEX, STATUS, USE_YN, NAME, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX, ADC_INDEX FROM TMP_SLB_VSERVER) VSERVER   \n"
							+ "   RIGHT JOIN (SELECT INDEX, POOL_INDEX, NODE_INDEX, PORT FROM TMP_SLB_POOLMEMBER) POOLMEMBER   \n"
							+ "        ON POOLMEMBER.POOL_INDEX = VSERVER.POOL_INDEX						                   \n"
							+ "   LEFT JOIN (SELECT INDEX, IP_ADDRESS FROM TMP_SLB_NODE) SLB_NODE        					   \n"
							+ "        ON POOLMEMBER.NODE_INDEX=SLB_NODE.INDEX                                                 \n"
							+ "   LEFT JOIN (SELECT * FROM TMP_SLB_POOLMEMBER_STATUS) POOLMEMBER_STATUS                        \n"
							+ "        ON POOLMEMBER.INDEX=POOLMEMBER_STATUS.POOLMEMBER_INDEX                                  \n"
							+ "   LEFT JOIN (SELECT * FROM MNG_ADC WHERE STATUS=1 AND AVAILABLE=1) ADC                         \n"
							+ "       ON ADC.INDEX=VSERVER.ADC_INDEX		                                                   \n"
							+ "   FULL OUTER JOIN MNG_DASHBOARD_VS_FILTER VSFILTER                                             \n"
							+ "       ON VSERVER.INDEX = VSFILTER.VS_INDEX                                                     \n"
							+ "  ) NOTALTEON                                                                                   \n"
							+ "  WHERE NOTALTEON.ADC_TYPE != 2                                                                 \n"
							+ "  AND (( NOTALTEON.VS_STATUS =1 AND NOTALTEON.MEMBER_STATUS= 2 ) OR (NOTALTEON.VS_STATUS =2))   \n"
							+ "  GROUP BY NOTALTEON.INDEX, NOTALTEON.VS_INDEX, NOTALTEON.VIRTUAL_IP, NOTALTEON.VIRTUAL_PORT, NOTALTEON.ADC_INDEX, NOTALTEON.ADC_NAME, NOTALTEON.ADC_TYPE, NOTALTEON.VS_STATUS, NOTALTEON.MEMBER_STATUS, NOTALTEON.FILTER_STATE\n"
							+ ")NOTALTEON_TOTAL                                                                                \n"
							+ "GROUP BY NOTALTEON_TOTAL.INDEX, NOTALTEON_TOTAL.VS_INDEX, NOTALTEON_TOTAL.VIRTUAL_IP, NOTALTEON_TOTAL.VIRTUAL_PORT, NOTALTEON_TOTAL.ADC_INDEX, NOTALTEON_TOTAL.ADC_NAME, NOTALTEON_TOTAL.ADC_TYPE, NOTALTEON_TOTAL.VS_STATUS, NOTALTEON_TOTAL.FILTER_STATE\n"
							+ "                                                                                              \n"
							+ "union                                                                                         \n"
							+ "                                                                                              \n"
							+ "SELECT INDEX, ADC_INDEX, ADC_NAME, ADC_TYPE, VS_INDEX, VIRTUAL_IP, VIRTUAL_PORT, VS_STATUS, FILTER_STATE, VSVS_INDEX FROM ( \n"
							+ "  SELECT * FROM(                                                                                \n"
							+ "  SELECT VSFILTER.INDEX, ADC.INDEX ADC_INDEX, ADC.NAME ADC_NAME, ADC.TYPE ADC_TYPE, VSERVER.INDEX VS_INDEX, VSERVER.VIRTUAL_IP VIRTUAL_IP,                     \n"
							+ "         VSS.VIRTUAL_PORT, VSS.STATUS VS_STATUS, --서비스 바뀌는 것을 확인하는데만 쓴다.                   \n"
							+ "       MSTATUS.MEMBER_STATUS, COALESCE(VSFILTER.STATE, 0) AS FILTER_STATE, VSS.INDEX VSVS_INDEX\n"
							+ "   FROM (SELECT ADC_INDEX, INDEX, ALTEON_ID, NAME, VIRTUAL_IP FROM TMP_SLB_VSERVER) VSERVER 	    \n"
							+ "   LEFT JOIN (SELECT INDEX, STATUS, VIRTUAL_PORT, VS_INDEX, REAL_PORT, POOL_INDEX FROM TMP_SLB_VS_SERVICE) VSS \n"
							+ "       ON VSERVER.INDEX = VSS.VS_INDEX                                                           \n"
							+ "   LEFT JOIN (SELECT INDEX FROM TMP_SLB_POOL) POOL                                               \n"
							+ "       ON POOL.INDEX = VSS.POOL_INDEX                                                            \n"
							+ "   LEFT JOIN (SELECT INDEX, POOL_INDEX, NODE_INDEX, PORT FROM TMP_SLB_POOLMEMBER) MEMBER         \n"
							+ "       ON MEMBER.POOL_INDEX = VSS.POOL_INDEX                                                     \n"
							+ "   LEFT JOIN (SELECT INDEX, IP_ADDRESS FROM TMP_SLB_NODE) NODE                                   \n"
							+ "       ON MEMBER.NODE_INDEX = NODE.INDEX                                                         \n"
							+ "   LEFT JOIN (SELECT VS_INDEX, POOLMEMBER_INDEX, MEMBER_STATUS, VSRC_INDEX FROM TMP_SLB_POOLMEMBER_STATUS ) MSTATUS                 \n"
							+ "       ON VSERVER.INDEX = MSTATUS.VS_INDEX AND MEMBER.INDEX = MSTATUS.POOLMEMBER_INDEX  AND MSTATUS.VSRC_INDEX = VSS.INDEX               \n"
							+ "   LEFT JOIN (SELECT INDEX, NAME, TYPE, MODEL FROM MNG_ADC WHERE TYPE=2 AND AVAILABLE=1 AND STATUS=1) ADC 														 \n"
							+ "  -- LEFT JOIN (SELECT INDEX, NAME, TYPE, MODEL FROM MNG_ADC WHERE TYPE=2 AND AVAILABLE=1) ADC    \n"
							+ "       ON ADC.INDEX = VSERVER.ADC_INDEX                                                           \n"
							+ "   FULL OUTER JOIN MNG_DASHBOARD_VS_FILTER VSFILTER                                               \n"
							+ "       ON VSS.INDEX = VSFILTER.VSVS_INDEX                                                         \n"
							+ "  )ALTEON                                                                                         \n"
							+ "  WHERE ALTEON.ADC_TYPE = 2                                                                       \n"
							+ "  AND ALTEON.MEMBER_STATUS = 2                                                                    \n"
							+ "  GROUP BY ALTEON.INDEX, ALTEON.VS_INDEX, ALTEON.VIRTUAL_IP, ALTEON.VIRTUAL_PORT, ALTEON.ADC_INDEX, ALTEON.ADC_NAME, ALTEON.ADC_TYPE, ALTEON.VS_STATUS, ALTEON.MEMBER_STATUS, ALTEON.FILTER_STATE, ALTEON.VSVS_INDEX\n"
							+ ") ALTEON_TOTAL\n"
							+ "GROUP BY ALTEON_TOTAL.INDEX, ALTEON_TOTAL.VS_INDEX, ALTEON_TOTAL.VIRTUAL_IP, ALTEON_TOTAL.VIRTUAL_PORT, ALTEON_TOTAL.ADC_INDEX, ALTEON_TOTAL.ADC_NAME, ALTEON_TOTAL.ADC_TYPE, ALTEON_TOTAL.VS_STATUS, ALTEON_TOTAL.FILTER_STATE, ALTEON_TOTAL.VSVS_INDEX\n"
							+ "                                                                                                  \n"
							+ ")TOTAL                                                                                            \n"
							+ "WHERE TOTAL.ADC_NAME <> ''                                                                         ");

//					
//					
//					
//					
//					
//					"SELECT INDEX, ADC_INDEX, ADC_TYPE, NAME, VIRTUAL_IP, VIRTUAL_PORT, VS_STATUS, VS_INDEX, VSVS_INDEX, FILTER_STATE FROM (                      \n"
//							+ "	SELECT COALESCE(VSFILTER.INDEX, null) AS INDEX, MNGADC.TYPE AS ADC_TYPE, VSERVER.ADC_INDEX, MNGADC.NAME, VSERVER.VIRTUAL_IP, VSERVER.VIRTUAL_PORT, VSERVER.STATUS AS VS_STATUS, VSERVER.INDEX AS VS_INDEX, null AS VSVS_INDEX, COALESCE(VSFILTER.STATE, 0) AS FILTER_STATE \n"
//							+ "	FROM TMP_SLB_VSERVER VSERVER                                                                                              \n"
//							+ "	INNER JOIN (                                                                                                              \n"
//							+ "	SELECT INDEX, NAME, TYPE                                                                                                  \n"
//							+ "	FROM MNG_ADC                                                                                                              \n"
//							+ "	WHERE AVAILABLE =1 AND TYPE !=2                                                                                           \n"
//							+ "	) MNGADC                                                                                                                  \n"
//							+ "	ON MNGADC.INDEX=VSERVER.ADC_INDEX                                                                                         \n"
//							+ "	FULL OUTER JOIN MNG_DASHBOARD_VS_FILTER VSFILTER                                                                           \n"
//							+ "	ON VSERVER.INDEX = VSFILTER.VS_INDEX                                                                                      \n"
//							+ "	UNION                                                                                                                     \n"
//							+ "	SELECT COALESCE(VSFILTER.INDEX, null) AS INDEX, MNGADC.TYPE AS ADC_TYPE, VSVC.ADC_INDEX, MNGADC.NAME, VSERVER.VIRTUAL_IP, VSVC.VIRTUAL_PORT, VSVC.STATUS AS VS_STATUS, VSERVER.INDEX AS VS_INDEX, VSVC.INDEX AS VSVS_INDEX, COALESCE(VSFILTER.STATE, 0) AS FILTER_STATE\n"
//							+ "	FROM TMP_SLB_VS_SERVICE VSVC                                                                                              \n"
//							+ "	INNER JOIN TMP_SLB_VSERVER VSERVER ON VSERVER.INDEX = VSVC.VS_INDEX                                                       \n"
//							+ "	INNER JOIN (                                                                                                              \n"
//							+ "		SELECT INDEX, NAME, TYPE                                                                                              \n"
//							+ "		FROM MNG_ADC                                                                                                          \n"
//							+ "		WHERE AVAILABLE =1 AND TYPE = 2                                                                                       \n"
//							+ "		) MNGADC                                                                                                              \n"
//							+ "		ON MNGADC.INDEX=VSERVER.ADC_INDEX                                                                                     \n"
//							+ "	FULL OUTER JOIN MNG_DASHBOARD_VS_FILTER VSFILTER                                                                          \n"
//							+ "		ON VSVC.INDEX = VSFILTER.VSVS_INDEX                                                                                   \n"
//							+ ")TOTAL                                                                                                                     \n"
//							+ "WHERE TOTAL.NAME <> ''                                                                                                     \n"
//							+ "AND TOTAL.VS_STATUS = 2                                                                                                    \n");

			if (searchKey != null && !searchKey.isEmpty()) {
				sqlText += "AND " + makeKeywordSearchClause(searchKey);
			}
			sqlText += makeOrderTypeNDirClause(orderType, orderDirw);

			if (toRow != null && toRow > 0) {
				int limit = toRow - fromRow + 1;// toRow, fromRow값이 포함관계임. 따라서 +1
				String sqlLimit = String.format(" LIMIT %d OFFSET %d", limit, fromRow);
				sqlText += sqlLimit;
			}

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoVSFilterInfo filterInfo = new OBDtoVSFilterInfo();

				filterInfo.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				filterInfo.setAdcName(db.getString(rs, "ADC_NAME"));
				filterInfo.setAdcType(db.getInteger(rs, "ADC_TYPE"));
				filterInfo.setVsIndex(db.getString(rs, "VS_INDEX"));
				filterInfo.setVsIPAddress(db.getString(rs, "VIRTUAL_IP"));
				filterInfo.setVsPort(db.getInteger(rs, "VIRTUAL_PORT"));
				filterInfo.setVsvcIndex(db.getString(rs, "VSVS_INDEX"));
				filterInfo.setVsStatus(db.getInteger(rs, "VS_STATUS"));
				filterInfo.setShowHideState(db.getInteger(rs, "FILTER_STATE"));
				filterInfo.setIndex(db.getString(rs, "INDEX"));
				if (filterInfo.getIndex() == null || filterInfo.getIndex().isEmpty()) {
					filterInfo.setIndex(makeVSFilterInfoIndex(filterInfo));
				}
				results.add(filterInfo);
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		return results;
	}

}
