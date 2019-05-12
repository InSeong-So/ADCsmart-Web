/*
 * VServer 정보를 DB에 저장, 추출하는 기능을 제공한다.
 */
package kr.openbase.adcsmart.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.database.OBDatabaseMssql;
import kr.openbase.adcsmart.service.dto.OBDtoADCGroupInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcAdditional;
import kr.openbase.adcsmart.service.dto.OBDtoAdcHealthCheckAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeDetail;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNoticeGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcProfile;
import kr.openbase.adcsmart.service.dto.OBDtoAdcRSInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcRealServerGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSchedule;
import kr.openbase.adcsmart.service.dto.OBDtoAdcScope;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSslCertificate;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVSInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerNotice;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVService;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoNetworkInterface;
import kr.openbase.adcsmart.service.dto.OBDtoRSInfo;
import kr.openbase.adcsmart.service.dto.OBDtoRespGroup;
import kr.openbase.adcsmart.service.dto.OBDtoRespInfo;
import kr.openbase.adcsmart.service.dto.OBDtoRespMultiChartData;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoSlbUser;
import kr.openbase.adcsmart.service.dto.OBDtoVSGroupInfo;
import kr.openbase.adcsmart.service.dto.OBDtoVSInfo;
import kr.openbase.adcsmart.service.dto.OBDtoVServerStatus;
import kr.openbase.adcsmart.service.dto.OBDtoVSservice;
import kr.openbase.adcsmart.service.dto.flb.OBDtoFlbFilterInfo;
import kr.openbase.adcsmart.service.dto.flb.OBDtoFlbFilterSummary;
import kr.openbase.adcsmart.service.dto.flb.OBDtoFlbGroupMonitorInfo;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigChunkAlteon;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigChunkF5;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcNodeSimple;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcPoolSimple;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcVServerAll;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcVlan;
import kr.openbase.adcsmart.service.impl.f5.DtoPoolMember;
import kr.openbase.adcsmart.service.impl.f5.DtoVirtualServer;
import kr.openbase.adcsmart.service.impl.f5.DtoVlanTunnelFilter;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcHealthCheckPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcNodePAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolMemberPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcHealthCheckPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcNodePASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolMemberPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoRealServerInfoPASK;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoSnmpFilterPortAlteon;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBDefineHealthcheckAlteon;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBVServerDB {
//     public static void main(String[] args)
//     {
//         try
//         {
//             OBDtoVSGroupInfo vsGroupInfo = new OBDtoVSGroupInfo();
//             vsGroupInfo.setName("asdfasdf");
//             new OBVServerDB().addVSServiceGroup(vsGroupInfo); 
//             
//         }
//         catch(OBException e)
//         {
//         e.printStackTrace();
//         }
//     }

	private enum QueryConditionType {
		NONE, VS_NAME, VS_IPADDRESS, VS_ALTEON_ID
	}

	public HashMap<String, String> getNodeIDListAlteon(Integer adcIndex) throws OBException// hash key: ipaddress
	{
		HashMap<String, String> retVal = new HashMap<String, String>();

		String sqlText = "";

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = " SELECT IP_ADDRESS, ALTEON_ID FROM TMP_SLB_NODE WHERE ADC_INDEX=" + adcIndex + ";";

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				String ipAddress = db.getString(rs, "IP_ADDRESS");
				String alteonID = db.getString(rs, "ALTEON_ID");
				retVal.put(ipAddress, alteonID);
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

	// public OBDtoVRRP getVrrpID(String vsIndex, OBDatabase db) throws OBException
	// {
	// OBDtoVRRP obj = new OBDtoVRRP();
	// String sqlText="";
	//
	// try
	// {
	// sqlText = String.format(
	// " SELECT ROUTER_INDEX, VR_INDEX, IF_NUM " +
	// " FROM TMP_SLB_VSERVER " +
	// " WHERE INDEX=%s AND (VRRP_STATE=%d OR VRRP_STATE=%d) ;"
	// , OBParser.sqlString(vsIndex)
	// , OBDefine.VRRP_STATE.ENABLE
	// , OBDefine.VRRP_STATE.DISABLE
	// );
	//
	// ResultSet rs;
	//
	// rs = db.executeQuery(sqlText);
	//
	// if(rs.next() == false)
	// {
	// return obj;
	// }
	//
	// obj.setIfNum(db.getInteger(rs, "IF_NUM"));
	// obj.setRouterIndex(db.getInteger(rs, "ROUTER_INDEX"));
	// obj.setVrIndex(db.getInteger(rs, "VR_INDEX"));
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	//
	// return obj;
	// }

	/**
	 * ADC와 관계없이 모든 등록된 VS 개수를 추출한다.
	 * 
	 * @param db
	 * @return
	 * @throws OBException
	 */
	public int getTotalVServerCount() throws OBException {

		String sqlText = " SELECT COUNT(*) AS CNT FROM TMP_SLB_VSERVER WHERE ADC_INDEX IN (SELECT INDEX FROM MNG_ADC WHERE AVAILABLE = "
				+ OBDefine.ADC_STATE.AVAILABLE + " ) ";

		int result = 0;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next())
				result = db.getInteger(rs, "CNT");

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", result));
			return result;
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
	 * 지정된 ADC Index에 해당되는 Virtual Server 목록을 가져온다. 테이블명:TMP_SLB_VSERVER
	 * 
	 * @param adcIndex -- 조회하고자하는 ADC 장비의 index 번호.
	 * @param db       -- 디비 인스턴스.
	 * @return ArrayList<OBDtoAdcVirtualServer>
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public ArrayList<OBDtoAdcVServerAlteon> getVServerListAllAlteon(Integer adcIndex) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT A.INDEX, A.ADC_INDEX, A.STATUS, A.NAME, A.ALTEON_ID, A.VIRTUAL_IP, \n"
					+ "   A.VRRP_STATE, A.ROUTER_INDEX, A.VR_INDEX, A.IF_NUM, A.USE_YN, A.SUB_INFO, \n"
					+ "   A.NWCLASS_ID, B.OCCUR_TIME  \n"
					+ " FROM                                                                      \n"
					+ "   (SELECT * FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d) A                  \n"
					+ " LEFT JOIN (                                                               \n"
					+ "   SELECT VS_INDEX, OCCUR_TIME                                             \n"
					+ "   FROM LOG_CONFIG_HISTORY                                                 \n"
					+ "   WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX = %d GROUP BY VS_INDEX)  \n"
					+ // where-in:empty string 불가, null 불가, OK
					" ) B                                                                       \n"
					+ " ON A.INDEX = B.VS_INDEX                                                   \n"
					+ " ORDER BY A.NAME, A.VIRTUAL_IP ;", adcIndex, adcIndex);

			ResultSet rs;
			ArrayList<OBDtoAdcVServerAlteon> list = new ArrayList<OBDtoAdcVServerAlteon>();

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcVServerAlteon obj = new OBDtoAdcVServerAlteon();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				obj.setState(db.getInteger(rs, "USE_YN"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setAlteonId(db.getString(rs, "ALTEON_ID"));
				obj.setvIP(db.getString(rs, "VIRTUAL_IP"));
				obj.setVrrpYN(false); // 안 쓴다. 그냥 채워놓음. vrrpState로 대체
				obj.setVrrpState(db.getInteger(rs, "VRRP_STATE")); // vrrpYN을 대체
				obj.setRouterIndex(db.getInteger(rs, "ROUTER_INDEX"));
				obj.setVrIndex(db.getInteger(rs, "VR_INDEX"));
				obj.setIfNum(db.getInteger(rs, "IF_NUM"));
				obj.setUseYN(db.getInteger(rs, "USE_YN"));
				obj.setVserviceList(getVServiceList(obj.getIndex()));
				obj.setApplyTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setNwclassId(db.getInteger(rs, "NWCLASS_ID"));
				obj.setSubInfo(db.getString(rs, "SUB_INFO"));
				list.add(obj);
			}
			return list;
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

	public HashMap<String, String> getVSIDListAlteon(Integer adcIndex) throws OBException {
		HashMap<String, String> retVal = new HashMap<String, String>();
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT ALTEON_ID                           "
					+ " FROM TMP_SLB_VSERVER                       " + " WHERE ADC_INDEX = %d                       ",
					adcIndex);

			ResultSet rs;
			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				String id = db.getString(rs, "ALTEON_ID");
				retVal.put(id, id);
			}
			return retVal;
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

	public HashMap<String, String> getPoolIDListAlteon(Integer adcIndex) throws OBException {
		HashMap<String, String> retVal = new HashMap<String, String>();
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" SELECT ALTEON_ID                           "
					+ " FROM TMP_SLB_POOL                          " + " WHERE ADC_INDEX = %d                       ",
					adcIndex);

			ResultSet rs;
			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				String id = db.getString(rs, "ALTEON_ID");
				retVal.put(id, id);
			}
			return retVal;
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

	public ArrayList<String> getPoolIndexListAlteon(Integer adcIndex) throws OBException {
		String sqlText = "";
		ArrayList<String> retVal = new ArrayList<String>();

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT ALTEON_ID                                \n"
					+ " FROM TMP_SLB_POOL                               \n"
					+ " WHERE ADC_INDEX = %d                             ", adcIndex);

			ResultSet rs;
			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				retVal.add(db.getString(rs, "ALTEON_ID"));
			}
			return retVal;
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

	public ArrayList<OBDtoAdcVServerAlteon> getVServerListAllAlteon(Integer adcIndex, Integer beginIndex,
			Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		String sqlText = "";

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			int offset = 0;
			if (beginIndex != null && beginIndex.intValue() >= 0)
				offset = beginIndex.intValue();

			int limit = 0;
			if (endIndex != null && endIndex.intValue() >= 0)
				limit = Math.abs(endIndex.intValue() - offset) + 1;
			else
				limit = 10000;

			String sqlLimit = String.format(" LIMIT %d ", limit);

			String sqlOffset = String.format(" OFFSET %d ", offset);

			sqlText = String
					.format(" SELECT A.INDEX AS INDEX, A.ADC_INDEX, A.STATUS, A.NAME AS VS_NAME, A.ALTEON_ID, \n"
							+ "     A.VIRTUAL_IP AS VS_IPADDRESS, A.VRRP_STATE, A.ROUTER_INDEX, A.VR_INDEX,     \n"
							+ "     A.IF_NUM, A.USE_YN, A.SUB_INFO, A.NWCLASS_ID, B.OCCUR_TIME AS OCCUR_TIME    \n"
							+ " FROM                                                                      \n"
							+ "   (SELECT * FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d) A                  \n"
							+ " LEFT JOIN (                                                               \n"
							+ "   SELECT VS_INDEX, OCCUR_TIME                                             \n"
							+ "   FROM LOG_CONFIG_HISTORY                                                 \n"
							+ "   WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX = %d GROUP BY VS_INDEX)   \n"
							+ // where-in:empty string 불가, null 불가, OK
							" ) B                                                                       \n"
							+ " ON A.INDEX = B.VS_INDEX                                                   \n", adcIndex,
							adcIndex);

			sqlText += searchVServerListOrderType(orderType, orderDir);

			sqlText += sqlLimit;

			sqlText += sqlOffset;

			sqlText += ";";

			ResultSet rs;
			ArrayList<OBDtoAdcVServerAlteon> list = new ArrayList<OBDtoAdcVServerAlteon>();

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcVServerAlteon obj = new OBDtoAdcVServerAlteon();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				obj.setName(db.getString(rs, "VS_NAME"));
				obj.setAlteonId(db.getString(rs, "ALTEON_ID"));
				obj.setvIP(db.getString(rs, "VS_IPADDRESS"));
				obj.setVrrpYN(false); // 안 쓴다. 그냥 값 채워둠. vrrpState로 대체
				obj.setVrrpState(db.getInteger(rs, "VRRP_STATE")); // vrrpYN을 대체
				obj.setRouterIndex(db.getInteger(rs, "ROUTER_INDEX"));
				obj.setVrIndex(db.getInteger(rs, "VR_INDEX"));
				obj.setIfNum(db.getInteger(rs, "IF_NUM"));
				obj.setUseYN(db.getInteger(rs, "USE_YN"));
				obj.setVserviceList(getVServiceList(obj.getIndex()));
				obj.setApplyTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setNwclassId(db.getInteger(rs, "NWCLASS_ID"));
				obj.setSubInfo(db.getString(rs, "SUB_INFO"));

				list.add(obj);
			}

			return list;
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

	/**
	 * 지정된 ADC Index에 해당되는 Virtual Server 목록을 가져온다. 테이블명:TMP_SLB_VSERVER
	 * 
	 * @param adcIndex -- 조회하고자하는 ADC 장비의 index 번호.
	 * @param db       -- 디비 인스턴스.
	 * @return ArrayList<OBDtoAdcVirtualServer>
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public ArrayList<OBDtoAdcVServerF5> getVServerListAllF5(Integer adcIndex) throws OBException {
		String sqlText = String.format(
				" SELECT A.INDEX, A.ADC_INDEX, A.STATUS, A.USE_YN, A.NAME, A.VIRTUAL_IP, A.VIRTUAL_PORT,                    \n"
						+ " A.POOL_INDEX, A.PERSISTENCE_INDEX, B.OCCUR_TIME                                                           \n"
						+ " FROM                                                                                                      \n"
						+ "   (SELECT * FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d) A                                                  \n"
						+ " LEFT JOIN (                                                                                               \n"
						+ "   SELECT VS_INDEX, OCCUR_TIME                                                                             \n"
						+ "   FROM LOG_CONFIG_HISTORY                                                                                 \n"
						+ "   WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX = %d GROUP BY VS_INDEX)   \n"
						+ " ) B                                                                                                       \n"
						+ " ON A.INDEX = B.VS_INDEX                                                                                   \n"
						+ " ORDER BY A.NAME, A.VIRTUAL_IP ;",
				adcIndex, adcIndex);

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			ResultSet rs = null;
			ArrayList<OBDtoAdcVServerF5> list = new ArrayList<OBDtoAdcVServerF5>();

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcVServerF5 obj = new OBDtoAdcVServerF5();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				obj.setUseYN(db.getInteger(rs, "USE_YN"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setvIP(db.getString(rs, "VIRTUAL_IP"));
				obj.setServicePort(db.getInteger(rs, "VIRTUAL_PORT"));
				obj.setPersistence(db.getString(rs, "PERSISTENCE_INDEX"));
				obj.setPool(getPoolInfoF5(db.getString(rs, "POOL_INDEX")));
				obj.setApplyTime(db.getTimestamp(rs, "OCCUR_TIME"));

				list.add(obj);
			}
			return list;
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

	/**
	 * getVServerListAllF5()와 거의 유사하지만, LOG_CONFIG_HISTORY join을 제거한 버전이다. 서비스 내부
	 * 처리용으로 vs 데이터를 뽑을 때는 최종 업데이트 시간이 필요 없으니 불필요한 join을 없앴다. 또한 파라미터로 vsIndex 목록을
	 * 주면 전체 vs가 아니라 해당 vs정보만 구할 수 있어 부분업데이트 등의 처리에 유용하다.
	 * 
	 * @param adcIndex
	 * @return vsIndex가 null이면 전체 vs를 구한다.
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcVServerF5> getVServerListF5InternalUse(Integer adcIndex, ArrayList<String> vsIndexList)
			throws OBException {
		String sqlText;
		if (vsIndexList == null) // 모든 vs목록을 구한다.
		{

			sqlText = String.format(" SELECT INDEX, ADC_INDEX, STATUS, USE_YN, NAME, VIRTUAL_IP, VIRTUAL_PORT,  \n"
					+ "   POOL_INDEX, PERSISTENCE_INDEX                                         \n"
					+ " FROM TMP_SLB_VSERVER                                                    \n"
					+ " WHERE ADC_INDEX = %d                                                    \n"
					+ " ORDER BY NAME, VIRTUAL_IP ;", adcIndex);
		} else if (vsIndexList.size() == 0) // vs개수가 없다면 빈 목록을 준다.
		{
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Virtual server list must have at least one element.");
			return new ArrayList<OBDtoAdcVServerF5>();
		} else // vsIndexList gets somethin.
		{
			String whereVsIndex = OBParser.convertList2SingleQuotedString(vsIndexList);
			sqlText = String.format(" SELECT INDEX, ADC_INDEX, STATUS, USE_YN, NAME, VIRTUAL_IP, VIRTUAL_PORT,  \n"
					+ "   POOL_INDEX, PERSISTENCE_INDEX                                         \n"
					+ " FROM TMP_SLB_VSERVER                                                    \n"
					+ " WHERE ADC_INDEX = %d AND INDEX IN (%s)                                  \n"
					+ " ORDER BY NAME, VIRTUAL_IP ;", adcIndex, whereVsIndex);
		}
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			ResultSet rs = null;
			ArrayList<OBDtoAdcVServerF5> list = new ArrayList<OBDtoAdcVServerF5>();

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcVServerF5 obj = new OBDtoAdcVServerF5();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				obj.setUseYN(db.getInteger(rs, "USE_YN"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setvIP(db.getString(rs, "VIRTUAL_IP"));
				obj.setServicePort(db.getInteger(rs, "VIRTUAL_PORT"));
				obj.setPersistence(db.getString(rs, "PERSISTENCE_INDEX"));
				obj.setPool(getPoolInfoF5(db.getString(rs, "POOL_INDEX")));

				list.add(obj);
			}
			return list;
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

	public ArrayList<OBDtoAdcVServerPAS> getVServerListAllPAS(Integer adcIndex, Integer beginIndex, Integer endIndex,
			Integer orderType, Integer orderDir) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			int offset = 0;
			if (beginIndex != null && beginIndex.intValue() >= 0)
				offset = beginIndex.intValue();

			int limit = 0;
			if (endIndex != null && endIndex.intValue() >= 0)
				limit = Math.abs(endIndex.intValue() - offset) + 1;
			else
				limit = 10000;

			String sqlLimit = String.format(" LIMIT %d ", limit);

			String sqlOffset = String.format(" OFFSET %d ", offset);

			sqlText = String.format(
					" SELECT A.INDEX AS INDEX, A.ADC_INDEX, A.STATUS AS STATUS, A.NAME AS VS_NAME, A.ALTEON_ID, A.VIRTUAL_IP AS VS_IPADDRESS,        \n"
							+ "   A.VRRP_STATE, A.ROUTER_INDEX, A.VR_INDEX, A.IF_NUM, A.USE_YN, B.OCCUR_TIME AS OCCUR_TIME  \n"
							+ " FROM                                                                           \n"
							+ "   (SELECT * FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d) A                       \n"
							+ " LEFT JOIN (                                                                    \n"
							+ "   SELECT VS_INDEX, OCCUR_TIME                                                  \n"
							+ "   FROM LOG_CONFIG_HISTORY                                                      \n"
							+ "   WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX = %d GROUP BY VS_INDEX)   \n"
							+ // where-in:empty string 불가, null 불가, OK
							" ) B                                                                            \n"
							+ " ON A.INDEX = B.VS_INDEX                                                        \n",
					adcIndex, adcIndex);

			sqlText += searchVServerListOrderType(orderType, orderDir);

			sqlText += sqlLimit;

			sqlText += sqlOffset;

			sqlText += ";";

			ResultSet rs;
			ArrayList<OBDtoAdcVServerPAS> list = new ArrayList<OBDtoAdcVServerPAS>();

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcVServerPAS obj = new OBDtoAdcVServerPAS();
				obj.setDbIndex(db.getString(rs, "INDEX"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				obj.setName(db.getString(rs, "VS_NAME"));
				obj.setvIP(db.getString(rs, "VS_IPADDRESS"));
				obj.setState(db.getInteger(rs, "USE_YN"));

				OBDtoAdcPoolPAS poolObj = getPoolInfoPAS(db.getString(rs, "POOL_INDEX"));
				obj.setPool(poolObj);
				obj.setApplyTime(db.getTimestamp(rs, "OCCUR_TIME"));

				list.add(obj);
			}

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

	private complexInfoPASK getComplexInfoPASK(String vipInfo) {
		complexInfoPASK ret = new complexInfoPASK(); // ip, port, configurable(true/false)
		if (vipInfo == null || vipInfo.isEmpty() == true) {
			return ret;
		}
		String lines[] = vipInfo.split("\n");

		ArrayList<String> vipList = new ArrayList<String>();
		ArrayList<String> vportList = new ArrayList<String>();
		vipList = new ArrayList<String>();
		vportList = new ArrayList<String>();
		int vindex = 0;
		boolean isHealthcheckMultiple = false;
		ret.setConfigurable(true); // 초기값 = 설정가능

		for (String line : lines) {
			if (line == null || line.isEmpty() == true) {
				continue;
			}
			String tokens[] = line.split(" ");
			if (tokens == null || tokens.length == 0) {
				continue;
			}
			if (tokens[0].equals("vip")) {
				vipList.add(vindex++, tokens[1]); // vip, 꼭 있다.
				if (tokens.length >= 6) // 없을 수도 잇다.
				{ // port가 comma로 연결된 여러개일 수 있다. space는 없다.
					String vports[] = tokens[5].split(",");
					if (vports == null || vports.length == 0) {
						continue;
					}
					vportList.addAll(Arrays.asList(vports)); // port들을 arraylist로 붙인다.
				}
			} else if (tokens[0].equals("health-check")) {
				isHealthcheckMultiple = tokens[1].contains(","); // healthcheck index string에 comma가 있으면 2개 이상이다.
			}
		}
		Set<String> vipSet = new HashSet<String>(vipList); // 중복을 제거하기 위해 HashSet으로 바꾼다.
		Set<String> vportSet = new HashSet<String>(vportList);// 중복을 제거하기 위해 HashSet으로 바꾼다.
		ret.setVip(OBParser.convertList2CommaString(vipSet));
		ret.setVport(OBParser.convertList2CommaString(vportSet));
		if (vipSet.size() > 1 || vportSet.size() > 1 || isHealthcheckMultiple == true) {
			ret.setConfigurable(false);
		}
		return ret;
	}

	private class complexInfoPASK {
		boolean configurable;
		String vip;
		String vport;

		@Override
		public String toString() {
			return "SubInfoPASK [configurable=" + configurable + ", vip=" + vip + ", vport=" + vport + "]";
		}

		public boolean isConfigurable() {
			return configurable;
		}

		public void setConfigurable(boolean configurable) {
			this.configurable = configurable;
		}

		public String getVip() {
			return vip;
		}

		public void setVip(String vip) {
			this.vip = vip;
		}

		public String getVport() {
			return vport;
		}

		public void setVport(String vport) {
			this.vport = vport;
		}
	}

	public ArrayList<OBDtoAdcVServerPASK> getVServerListAllPASK(Integer adcIndex, Integer beginIndex, Integer endIndex,
			Integer orderType, Integer orderDir) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			int offset = 0;
			if (beginIndex != null && beginIndex.intValue() >= 0)
				offset = beginIndex.intValue();

			int limit = 0;
			if (endIndex != null && endIndex.intValue() >= 0)
				limit = Math.abs(endIndex.intValue() - offset) + 1;
			else
				limit = 10000;

			String sqlLimit = String.format(" LIMIT %d ", limit);

			String sqlOffset = String.format(" OFFSET %d ", offset);

			sqlText = String.format(
					" SELECT A.INDEX AS INDEX, A.ADC_INDEX, A.STATUS AS STATUS, A.NAME AS VS_NAME, A.ALTEON_ID, A.VIRTUAL_IP AS VS_IPADDRESS,        \n"
							+ "   A.VIRTUAL_PORT AS SERVICE_PORT, A.USE_YN, B.OCCUR_TIME AS OCCUR_TIME, A.POOL_INDEX, A.SUB_INFO             \n"
							+ " FROM                                                                           \n"
							+ "   (SELECT * FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d) A                       \n"
							+ " LEFT JOIN (                                                                    \n"
							+ "   SELECT VS_INDEX, OCCUR_TIME                                                  \n"
							+ "   FROM LOG_CONFIG_HISTORY                                                      \n"
							+ "   WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX = %d GROUP BY VS_INDEX)   \n"
							+ // where-in:empty string 불가, null 불가, OK
							" ) B                                                                            \n"
							+ " ON A.INDEX = B.VS_INDEX                                                        \n",
					adcIndex, adcIndex);

			sqlText += searchVServerListOrderType(orderType, orderDir);
			sqlText += sqlLimit;
			sqlText += sqlOffset;
			sqlText += ";";

			ResultSet rs;
			ArrayList<OBDtoAdcVServerPASK> list = new ArrayList<OBDtoAdcVServerPASK>();

			rs = db.executeQuery(sqlText);
			complexInfoPASK info;
			while (rs.next()) {
				OBDtoAdcVServerPASK obj = new OBDtoAdcVServerPASK();
				obj.setDbIndex(db.getString(rs, "INDEX"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				obj.setName(db.getString(rs, "VS_NAME"));
				obj.setvIP(db.getString(rs, "VS_IPADDRESS"));
				obj.setState(db.getInteger(rs, "USE_YN"));
				obj.setSrvPort(db.getInteger(rs, "SERVICE_PORT"));
				OBDtoAdcPoolPASK poolObj = getPoolInfoPASK(db.getString(rs, "POOL_INDEX"));
				obj.setPool(poolObj);
				obj.setApplyTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setSubInfo(db.getString(rs, "SUB_INFO"));

				info = getComplexInfoPASK(obj.getSubInfo());
				obj.setvIPView(info.getVip());
				obj.setSrvPortView(info.getVport());
				obj.setConfigurable(info.isConfigurable());

				list.add(obj);
			}
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

	public Integer searchVServerListAlteonCount(Integer adcIndex, String searchKey) throws OBException {
		return searchVSListCountCore(adcIndex, 0, searchKey);
	}

	// public ArrayList<OBDtoAdcVServerAlteon> searchVServerListAlteon(Integer
	// adcIndex, String searchKey) throws OBException
	// {
	// return searchVServerListAlteon(adcIndex, 0, searchKey, null, null,
	// OBDefine.ORDER_TYPE_VSIPADDRESS, OBDefine.ORDER_DIR_ASCEND);
	// }

	public ArrayList<OBDtoAdcVServerAlteon> searchVServerListAlteon(Integer adcIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		return searchVServerListAlteon(adcIndex, 0, searchKey, beginIndex, endIndex, orderType, orderDir);
	}

	private String convertSqlVSList(ArrayList<String> vsList) throws OBException {
		String retVal = "";
		try {
			if (vsList == null || vsList.size() == 0)
				return OBParser.sqlString(retVal);
			for (String vsName : vsList) {
				if (!retVal.isEmpty())
					retVal += ", ";
				retVal += OBParser.sqlString(vsName);
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	private String convertSqlRSList(ArrayList<String> vsList) throws OBException {
		String retVal = "";
		try {
			if (vsList == null || vsList.size() == 0)
				return OBParser.sqlString(retVal);
			for (String vsName : vsList) {
				if (!retVal.isEmpty())
					retVal += ", ";
				retVal += OBParser.sqlString(vsName);
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// OBVServerDB tester = new OBVServerDB();
	// // try
	// // {
	// // ArrayList<OBDtoAdcVServerF5> list = new
	// OBVServerDB().searchVServerListF5(2, 0, "", null, null,
	// OBDefine.ORDER_TYPE_OCCURTIME, OBDefine.ORDER_DIR_DESCEND);
	// // System.out.println(list);
	// // }
	// // catch(OBException e)
	// // {
	// // e.printStackTrace();
	// // }
	// tester.searchNodeListF5Test();
	// tester.searchNodeListCountCoreTest();
	// }

	public ArrayList<OBDtoAdcVServerAlteon> searchVServerListAlteon(Integer adcIndex, Integer accntIndex,
			String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir)
			throws OBException {
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d,
		// searchKey:%s", adcIndex, searchKey));
		OBDatabase db = new OBDatabase();

		if (searchKey == null) {
			searchKey = "";
		}

		String sqlText = "";
		String sqlWhere = "";
		ArrayList<OBDtoAdcVServerAlteon> retVal = new ArrayList<OBDtoAdcVServerAlteon>();
		try {
			db.openDB();

			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accntIndex);

			int offset = 0;
			if (beginIndex != null && beginIndex.intValue() >= 0)
				offset = beginIndex.intValue();

			int limit = 0;
			String sqlLimit = "";
			if (endIndex != null && endIndex.intValue() >= 0) {
				limit = Math.abs(endIndex.intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}

			if (searchKey.isEmpty() == false) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlWhere = String.format(" WHERE A.NAME LIKE %s OR A.VIRTUAL_IP LIKE %s \n",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			if (roleNo.equals(OBDefine.ACCNT_ROLE_VSADMIN) || roleNo.equals(OBDefine.ACCNT_ROLE_RSADMIN)) {
				ArrayList<String> vsList = new OBAccountImpl().getAssignedVSList(adcIndex, accntIndex);
				String sqlTextVS = convertSqlVSList(vsList);
				if (sqlTextVS == null || sqlTextVS.isEmpty()) {
					db.closeDB();
					return retVal;
				}
				sqlText = String.format(
						" SELECT A.INDEX AS INDEX, A.ADC_INDEX, A.STATUS AS STATUS, A.USE_YN, A.NAME AS VS_NAME,  \n"
								+ "    A.ALTEON_ID, A.VIRTUAL_IP AS VS_IPADDRESS, A.VRRP_STATE, A.ROUTER_INDEX, A.VR_INDEX, \n"
								+ "    A.IF_NUM, A.SUB_INFO, A.NWCLASS_ID, B.OCCUR_TIME AS OCCUR_TIME                       \n"
								+ " FROM (SELECT * FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d AND INDEX IN ( %s ) ) A        \n"
								+ " LEFT JOIN ( SELECT VS_INDEX, OCCUR_TIME                                       \n"
								+ "   FROM LOG_CONFIG_HISTORY                                                     \n"
								+ "   WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX = %d GROUP BY VS_INDEX)   \n"
								+ // where-in:empty string 불가, null 불가, OK
								" ) B                                                                           \n"
								+ " ON A.INDEX = B.VS_INDEX                                                       \n"
								+ " %s                                                                            \n",
						adcIndex, sqlTextVS, adcIndex, sqlWhere);
			} else {
				sqlText = String.format(
						" SELECT A.INDEX AS INDEX, A.ADC_INDEX, A.STATUS AS STATUS, A.USE_YN, A.NAME AS VS_NAME,  \n"
								+ "    A.ALTEON_ID, A.VIRTUAL_IP AS VS_IPADDRESS, A.VRRP_STATE, A.ROUTER_INDEX, A.VR_INDEX, \n"
								+ "    A.IF_NUM, A.SUB_INFO, A.NWCLASS_ID, B.OCCUR_TIME AS OCCUR_TIME                       \n"
								+ " FROM (SELECT * FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d) A                   \n"
								+ " LEFT JOIN ( SELECT VS_INDEX, OCCUR_TIME                                       \n"
								+ "   FROM LOG_CONFIG_HISTORY                                                     \n"
								+ "   WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX = %d GROUP BY VS_INDEX)   \n"
								+ // where-in:empty string 불가, null 불가, OK
								" ) B                                                                           \n"
								+ " ON A.INDEX = B.VS_INDEX                                                       \n"
								+ " %s                                                                            \n",
						adcIndex, adcIndex, sqlWhere);
			}

			sqlText += searchVServerListOrderType(orderType, orderDir);// ";";

			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcVServerAlteon obj = new OBDtoAdcVServerAlteon();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				obj.setUseYN(db.getInteger(rs, "USE_YN"));
				obj.setName(db.getString(rs, "VS_NAME"));
				obj.setAlteonId(db.getString(rs, "ALTEON_ID"));
				obj.setvIP(db.getString(rs, "VS_IPADDRESS"));
				obj.setVrrpYN(false); // 안 쓰는데 그냥 값 채움. vrrpState로 대체함
				obj.setVrrpState(db.getInteger(rs, "VRRP_STATE")); // vrrpYN을 대체
				obj.setRouterIndex(db.getInteger(rs, "ROUTER_INDEX"));
				obj.setVrIndex(db.getInteger(rs, "VR_INDEX"));
				obj.setIfNum(db.getInteger(rs, "IF_NUM"));
				obj.setVserviceList(getVServiceList(obj.getIndex()));
				obj.setSubInfo(db.getString(rs, "SUB_INFO"));
				obj.setNwclassId(db.getInteger(rs, "NWCLASS_ID"));
				obj.setApplyTime(db.getTimestamp(rs, "OCCUR_TIME"));

				retVal.add(obj);
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
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
		// list.toString()));

		return retVal;
	}

	public ArrayList<String> getVsNameList(Integer adcIndex) throws OBException {
		ArrayList<String> retVal = new ArrayList<String>();

		String sqlText = "SELECT NAME FROM TMP_SLB_VSERVER " + " WHERE ADC_INDEX = " + adcIndex + ";";

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				retVal.add(db.getString(rs, "NAME"));
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

	public Integer searchVServerListF5Count(Integer adcIndex, String searchKey) throws OBException {
		return searchVSListCountCore(adcIndex, 0, searchKey);
	}

	private Integer searchVSListCountCore(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, searchKey:%s", adcIndex, searchKey));

		OBDatabase db = new OBDatabase();

		if (searchKey == null) {
			searchKey = "";
		}

		String sqlText = "";
		int retVal = 0;

		try {
			db.openDB();

			String sqlSearch = "";
			if (searchKey != null && !searchKey.isEmpty()) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlSearch = String.format(" ( NAME LIKE %s OR VIRTUAL_IP LIKE %s ) ", OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			}

			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accntIndex);

			if (roleNo == OBDefine.ACCNT_ROLE_VSADMIN || roleNo == OBDefine.ACCNT_ROLE_RSADMIN) {
				ArrayList<String> vsList = new OBAccountImpl().getAssignedVSList(adcIndex, accntIndex);
				String sqlTextVS = convertSqlVSList(vsList);
				if (sqlTextVS == null || sqlTextVS.isEmpty() == true) {
					OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", retVal));
					return retVal;
				}
				sqlText = String.format(" SELECT COUNT(INDEX) AS CNT " + " FROM TMP_SLB_VSERVER "
						+ " WHERE ADC_INDEX = %d AND INDEX IN ( %s )", adcIndex, sqlTextVS);
			} else {
				sqlText = String.format(
						" SELECT COUNT(INDEX) AS CNT " + " FROM TMP_SLB_VSERVER " + " WHERE ADC_INDEX = %d ", adcIndex);
			}

			if (sqlSearch != null && !sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				retVal = db.getInteger(rs, "CNT");
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

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", retVal));
		return retVal;
	}

	public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex, String searchKey, Integer beginIndex,
			Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		return searchVServerListF5(adcIndex, 0, searchKey, beginIndex, endIndex, orderType, orderDir);
	}

	public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start2. adcIndex:%d, searchKey:%s, beginIndex:%d, endIndex:%d", adcIndex, searchKey,
						beginIndex, endIndex));

		OBDatabase db = new OBDatabase();

		if (searchKey == null) {
			searchKey = "";
		}

		String sqlText = "";
		ArrayList<OBDtoAdcVServerF5> retVal = new ArrayList<OBDtoAdcVServerF5>();

		try {
			db.openDB();

			int offset = 0;
			if (beginIndex != null && beginIndex.intValue() >= 0)
				offset = beginIndex.intValue();

			int limit = 0;
			String sqlLimit = "";
			if (endIndex != null && endIndex.intValue() >= 0) {
				limit = Math.abs(endIndex.intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}

			String sqlWhere = "";
			if (searchKey.isEmpty() == false) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlWhere = String.format(" WHERE A.NAME LIKE %s OR A.VIRTUAL_IP LIKE %s \n",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accntIndex);

			if (roleNo.equals(OBDefine.ACCNT_ROLE_VSADMIN) || roleNo.equals(OBDefine.ACCNT_ROLE_RSADMIN)) {
				ArrayList<String> vsList = new OBAccountImpl().getAssignedVSList(adcIndex, accntIndex);
				String sqlTextVS = convertSqlVSList(vsList);
				if (sqlTextVS == null || sqlTextVS.isEmpty() == true) {
					db.closeDB();
					OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result list count:%d", retVal.size()));
					return retVal;
				}
				sqlText = String.format(
						" SELECT A.INDEX AS INDEX, A.ADC_INDEX, A.STATUS AS STATUS, A.USE_YN, A.NAME AS VS_NAME, A.VIRTUAL_IP AS VS_IPADDRESS, A.VIRTUAL_PORT AS SERVICE_PORT, "
								+ " A.POOL_INDEX, A.PERSISTENCE_INDEX, B.OCCUR_TIME AS OCCUR_TIME                                             \n"
								+ " FROM (SELECT * FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d AND INDEX IN ( %s ) ) A                          \n"
								+ " LEFT JOIN ( SELECT VS_INDEX, OCCUR_TIME                                                                   \n"
								+ "   FROM LOG_CONFIG_HISTORY                                                                                 \n"
								+ "   WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX = %d GROUP BY VS_INDEX)   \n"
								+ // where-in:empty string 불가, null 불가, OK
								" ) B                                                                                                       \n"
								+ " ON A.INDEX = B.VS_INDEX                                                                                   \n"
								+ " %s                                                                                                        \n",
						adcIndex, sqlTextVS, adcIndex, sqlWhere);
			} else {
				sqlText = String.format(
						" SELECT A.INDEX AS INDEX, A.ADC_INDEX, A.STATUS AS STATUS, A.USE_YN, A.NAME AS VS_NAME, A.VIRTUAL_IP AS VS_IPADDRESS, A.VIRTUAL_PORT AS SERVICE_PORT, "
								+ " A.POOL_INDEX, A.PERSISTENCE_INDEX, B.OCCUR_TIME AS OCCUR_TIME                                            \n"
								+ " FROM (SELECT * FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d) A                                               \n"
								+ " LEFT JOIN ( SELECT VS_INDEX, OCCUR_TIME                                                                   \n"
								+ "   FROM LOG_CONFIG_HISTORY                                                                                 \n"
								+ "   WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX = %d GROUP BY VS_INDEX)   \n"
								+ // where-in:empty string 불가, null 불가, OK
								" ) B                                                                                                       \n"
								+ " ON A.INDEX = B.VS_INDEX                                                                                   \n"
								+ " %s                                                                                                        \n",
						adcIndex, adcIndex, sqlWhere);
			}

			sqlText += searchVServerListOrderType(orderType, orderDir);

			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcVServerF5 obj = new OBDtoAdcVServerF5();

				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				obj.setUseYN(db.getInteger(rs, "USE_YN"));
				obj.setName(db.getString(rs, "VS_NAME"));
				obj.setvIP(db.getString(rs, "VS_IPADDRESS"));
				obj.setServicePort(db.getInteger(rs, "SERVICE_PORT"));
				obj.setPool(getPoolInfoF5(db.getString(rs, "POOL_INDEX")));
				obj.setPersistence(db.getString(rs, "PERSISTENCE_INDEX"));
				obj.setApplyTime(db.getTimestamp(rs, "OCCUR_TIME"));

				retVal.add(obj);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result list count:%d", retVal.size()));
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	// db.openDB();
	// db2.openDB();
	// ArrayList<OBDtoAdcVServerPAS> list = new
	// OBVServerDB().searchVServerListPAS(5, "yk");
	// System.out.println(list);
	// db.closeDB();
	// db2.closeDB();
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	public Integer searchVServerListPASCount(Integer adcIndex, String searchKey) throws OBException {
		return searchVSListCountCore(adcIndex, 0, searchKey);
	}

	public Integer searchVServerListPASKCount(Integer adcIndex, String searchKey) throws OBException {
		return searchVSListCountCore(adcIndex, 0, searchKey);
	}

	public Integer searchVServerListAlteonCount(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		return searchVSListCountCore(adcIndex, accntIndex, searchKey);
	}

	public Integer searchVServerListF5Count(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException {
		return searchVSListCountCore(adcIndex, accntIndex, searchKey);
	}

	public Integer searchVServerListPASCount(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		return searchVSListCountCore(adcIndex, accntIndex, searchKey);
	}

	public Integer searchVServerListPASKCount(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		return searchVSListCountCore(adcIndex, accntIndex, searchKey);
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	// db.openDB();
	// db2.openDB();
	// ArrayList<OBDtoAdcVServerPAS> list = new
	// OBVServerDB().searchVServerListPAS(5, "yk");
	// System.out.println(list);
	// db.closeDB();
	// db2.closeDB();
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(Integer adcIndex, String searchKey, Integer beginIndex,
			Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		return searchVServerListPAS(adcIndex, 0, searchKey, beginIndex, endIndex, orderType, orderDir);
	}

	public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start2. adcIndex:%d, searchKey:%s, beginIndex:%d, endIndex:%d", adcIndex, searchKey,
						beginIndex, endIndex));

		OBDatabase db = new OBDatabase();

		if (searchKey == null) {
			searchKey = "";
		}

		String sqlText = "";
		ArrayList<OBDtoAdcVServerPAS> retVal = new ArrayList<OBDtoAdcVServerPAS>();

		try {
			db.openDB();

			int offset = 0;
			if (beginIndex != null && beginIndex.intValue() >= 0)
				offset = beginIndex.intValue();

			int limit = 0;
			String sqlLimit = "";
			if (endIndex != null && endIndex.intValue() >= 0) {
				limit = Math.abs(endIndex.intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}

			String sqlWhere = "";
			if (searchKey.isEmpty() == false) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlWhere = String.format(" WHERE A.NAME LIKE %s OR A.VIRTUAL_IP LIKE %s \n",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accntIndex);

			if (roleNo.equals(OBDefine.ACCNT_ROLE_VSADMIN) || roleNo.equals(OBDefine.ACCNT_ROLE_RSADMIN)) {
				ArrayList<String> vsList = new OBAccountImpl().getAssignedVSList(adcIndex, accntIndex);
				String sqlTextVS = convertSqlVSList(vsList);
				if (sqlTextVS == null || sqlTextVS.isEmpty()) {
					db.closeDB();
					OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result list count:%d", retVal.size()));
					return retVal;
				}
				sqlText = String.format(
						" SELECT A.INDEX AS INDEX, A.ADC_INDEX, A.STATUS AS STATUS, A.USE_YN, A.NAME AS VS_NAME, A.VIRTUAL_IP AS VS_IPADDRESS, A.VIRTUAL_PORT AS SERVICE_PORT, "
								+ " A.POOL_INDEX, A.PERSISTENCE_INDEX, B.OCCUR_TIME AS OCCUR_TIME "
								+ " FROM (SELECT * FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d AND INDEX IN ( %s ) ) A                                              \n"
								+ " LEFT JOIN ( SELECT VS_INDEX, OCCUR_TIME                                                                   \n"
								+ "   FROM LOG_CONFIG_HISTORY                                                                                 \n"
								+ "   WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX = %d GROUP BY VS_INDEX)   \n"
								+ // where-in:empty string 불가, null 불가, OK
								" ) B                                                                                                       \n"
								+ " ON A.INDEX = B.VS_INDEX                                                                                   \n"
								+ " %s                                                                                                        \n",
						adcIndex, sqlTextVS, adcIndex, sqlWhere);
			} else {
				sqlText = String.format(
						" SELECT A.INDEX AS INDEX, A.ADC_INDEX, A.STATUS AS STATUS, A.USE_YN, A.NAME AS VS_NAME, A.VIRTUAL_IP AS VS_IPADDRESS, A.VIRTUAL_PORT AS SERVICE_PORT, "
								+ " A.POOL_INDEX, A.PERSISTENCE_INDEX, B.OCCUR_TIME AS OCCUR_TIME "
								+ " FROM (SELECT * FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d) A                                               \n"
								+ " LEFT JOIN ( SELECT VS_INDEX, OCCUR_TIME                                                                   \n"
								+ "   FROM LOG_CONFIG_HISTORY                                                                                 \n"
								+ "   WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX = %d GROUP BY VS_INDEX)   \n"
								+ // where-in:empty string 불가, null 불가, OK
								" ) B                                                                                                       \n"
								+ " ON A.INDEX = B.VS_INDEX                                                                                   \n"
								+ " %s                                                                                                        \n",
						adcIndex, adcIndex, sqlWhere);
			}

			sqlText += searchVServerListOrderType(orderType, orderDir);
			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcVServerPAS obj = new OBDtoAdcVServerPAS();

				obj.setDbIndex(db.getString(rs, "INDEX"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				obj.setState(db.getInteger(rs, "USE_YN"));
				obj.setName(db.getString(rs, "VS_NAME"));
				obj.setvIP(db.getString(rs, "VS_IPADDRESS"));
				obj.setSrvPort(db.getInteger(rs, "SERVICE_PORT"));
				OBDtoAdcPoolPAS poolObj = getPoolInfoPAS(db.getString(rs, "POOL_INDEX"));
				obj.setPool(poolObj);
				obj.setApplyTime(db.getTimestamp(rs, "OCCUR_TIME"));
				retVal.add(obj);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result list count:%d", retVal.size()));
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	// db.openDB();
	// db2.openDB();
	// ArrayList<OBDtoAdcVServerPAS> list = new
	// OBVServerDB().searchVServerListPAS(5, "yk");
	// System.out.println(list);
	// db.closeDB();
	// db2.closeDB();
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	// public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(Integer adcIndex,
	// String searchKey) throws OBException
	// {
	// return searchVServerListPAS(adcIndex, 0, searchKey, null, null,
	// OBDefine.ORDER_TYPE_VSNAME, OBDefine.ORDER_DIR_ASCEND);
	// }

	// public ArrayList<OBDtoAdcVServerPASK> searchVServerListPASK(Integer adcIndex,
	// String searchKey, Integer beginIndex, Integer endIndex) throws OBException
	// {
	// return searchVServerListPASK(adcIndex, 0, searchKey, beginIndex, endIndex,
	// OBDefine.ORDER_TYPE_VSNAME, OBDefine.ORDER_DIR_ASCEND);
	// }

	public ArrayList<OBDtoAdcVServerPASK> searchVServerListPASK(Integer adcIndex, String searchKey, Integer beginIndex,
			Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		return searchVServerListPASK(adcIndex, 0, searchKey, beginIndex, endIndex, orderType, orderDir);
	}

	private String searchVServerListOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST ";

		switch (orderType) {
		case OBDefine.ORDER_TYPE_STATUS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY STATUS ASC NULLS LAST , VS_NAME ASC  NULLS LAST ";
			else
				retVal = " ORDER BY STATUS DESC NULLS LAST , VS_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_INDEX:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INDEX ASC NULLS LAST , VS_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY INDEX DESC NULLS LAST , VS_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_VSNAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY VS_NAME ASC NULLS LAST , INET(A.VIRTUAL_IP) ASC NULLS LAST ";
			else
				retVal = " ORDER BY VS_NAME DESC NULLS LAST , INET(A.VIRTUAL_IP) ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_VSIPADDRESS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INET(A.VIRTUAL_IP) ASC NULLS LAST , VS_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY INET(A.VIRTUAL_IP) DESC NULLS LAST , VS_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SERVICEPORT:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY SERVICE_PORT ASC NULLS LAST , VS_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY SERVICE_PORT DESC NULLS LAST , VS_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_OCCURTIME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY OCCUR_TIME ASC NULLS LAST , VS_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST , VS_NAME ASC NULLS LAST ";
			break;
		}
		return retVal;
	}

	private String searchSlbScheduleListOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST ";

		switch (orderType) {
		case OBDefine.ORDER_TYPE_STATUS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY STATE ASC NULLS LAST , OCCUR_TIME ASC  NULLS LAST ";
			else
				retVal = " ORDER BY STATE DESC NULLS LAST , OCCUR_TIME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_INDEX:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY RESERVATION_TIME ASC NULLS LAST , OCCUR_TIME ASC NULLS LAST ";
			else
				retVal = " ORDER BY RESERVATION_TIME DESC NULLS LAST , OCCUR_TIME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_VSNAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY VS_NAME ASC NULLS LAST , INET(A.VS_IP) ASC NULLS LAST ";
			else
				retVal = " ORDER BY VS_NAME DESC NULLS LAST , INET(A.VS_IP) ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_VSIPADDRESS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INET(A.VS_IP) ASC NULLS LAST , VS_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY INET(A.VS_IP) DESC NULLS LAST , VS_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_OCCURTIME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY NAME ASC NULLS LAST , OCCUR_TIME ASC NULLS LAST ";
			else
				retVal = " ORDER BY NAME DESC NULLS LAST , OCCUR_TIME ASC NULLS LAST ";
			break;
		}
		return retVal;
	}

	private String searchSlbUserListOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY NAME DESC NULLS LAST ";

		switch (orderType) {
		case OBDefine.ORDER_TYPE_STATUS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY UPDATE_TIME ASC NULLS LAST ";
			else
				retVal = " ORDER BY UPDATE_TIME DESC NULLS LAST ";
			break;
//        case OBDefine.ORDER_TYPE_INDEX:
//            if(orderDir == OBDefine.ORDER_DIR_ASCEND)
//                retVal = " ORDER BY INDEX ASC NULLS LAST , VS_NAME ASC NULLS LAST ";
//            else
//                retVal = " ORDER BY INDEX DESC NULLS LAST , VS_NAME ASC NULLS LAST ";
//            break;
//        case OBDefine.ORDER_TYPE_VSNAME:
//            if(orderDir == OBDefine.ORDER_DIR_ASCEND)
//                retVal = " ORDER BY VS_NAME ASC NULLS LAST , INET(A.VIRTUAL_IP) ASC NULLS LAST ";
//            else
//                retVal = " ORDER BY VS_NAME DESC NULLS LAST , INET(A.VIRTUAL_IP) ASC NULLS LAST ";
//            break;
//        case OBDefine.ORDER_TYPE_VSIPADDRESS:
//            if(orderDir == OBDefine.ORDER_DIR_ASCEND)
//                retVal = " ORDER BY INET(A.VIRTUAL_IP) ASC NULLS LAST , VS_NAME ASC NULLS LAST ";
//            else
//                retVal = " ORDER BY INET(A.VIRTUAL_IP) DESC NULLS LAST , VS_NAME ASC NULLS LAST ";
//            break;
//        case OBDefine.ORDER_TYPE_SERVICEPORT:
//            if(orderDir == OBDefine.ORDER_DIR_ASCEND)
//                retVal = " ORDER BY SERVICE_PORT ASC NULLS LAST , VS_NAME ASC NULLS LAST ";
//            else
//                retVal = " ORDER BY SERVICE_PORT DESC NULLS LAST , VS_NAME ASC NULLS LAST ";
//            break;
//        case OBDefine.ORDER_TYPE_OCCURTIME:
//            if(orderDir == OBDefine.ORDER_DIR_ASCEND)
//                retVal = " ORDER BY OCCUR_TIME ASC NULLS LAST , VS_NAME ASC NULLS LAST ";
//            else
//                retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST , VS_NAME ASC NULLS LAST ";
//            break;
		}
		return retVal;
	}

	private String searchRespListOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY NAME DESC NULLS LAST ";

		switch (orderType) {
		case OBDefine.ORDER_TYPE_NAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY NAME DESC NULLS LAST ";
			break;
		}
		return retVal;
	}

	private String searchGroupNameListOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY INDEX ASC NULLS LAST ";

		switch (orderType) {
		case OBDefine.ORDER_TYPE_NAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY NAME DESC NULLS LAST ";
			break;
		}
		return retVal;
	}

	private ArrayList<OBDtoAdcVServerPASK> searchVServerListPASK(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start2. adcIndex:%d, searchKey:%s, beginIndex:%d, endIndex:%d", adcIndex, searchKey,
						beginIndex, endIndex));

		OBDatabase db = new OBDatabase();

		if (searchKey == null) {
			searchKey = "";
		}

		String sqlText = "";
		ArrayList<OBDtoAdcVServerPASK> retVal = new ArrayList<OBDtoAdcVServerPASK>();

		try {
			db.openDB();

			int offset = 0;
			if (beginIndex != null && beginIndex.intValue() >= 0)
				offset = beginIndex.intValue();

			int limit = 0;
			String sqlLimit = "";
			if (endIndex != null && endIndex.intValue() >= 0) {
				limit = Math.abs(endIndex.intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}

			String sqlWhere = "";
			if (searchKey.isEmpty() == false) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlWhere = String.format(" WHERE A.NAME LIKE %s OR A.VIRTUAL_IP LIKE %s \n",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accntIndex);

			if (roleNo.equals(OBDefine.ACCNT_ROLE_VSADMIN) || roleNo.equals(OBDefine.ACCNT_ROLE_RSADMIN)) {
				ArrayList<String> vsList = new OBAccountImpl().getAssignedVSList(adcIndex, accntIndex);
				String sqlTextVS = convertSqlVSList(vsList);
				if (sqlTextVS == null || sqlTextVS.isEmpty()) {
					db.closeDB();
					OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result list count:%d", retVal.size()));
					return retVal;
				}
				sqlText = String.format(
						" SELECT A.INDEX AS INDEX, A.ADC_INDEX, A.STATUS AS STATUS, A.USE_YN, A.NAME AS VS_NAME, A.VIRTUAL_IP AS VS_IPADDRESS, A.VIRTUAL_PORT AS SERVICE_PORT, "
								+ " A.POOL_INDEX, A.PERSISTENCE_INDEX, A.SUB_INFO, B.OCCUR_TIME AS OCCUR_TIME"
								+ " FROM (SELECT * FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d AND INDEX IN ( %s ) ) A                                              \n"
								+ " LEFT JOIN ( SELECT VS_INDEX, OCCUR_TIME                                                                   \n"
								+ "   FROM LOG_CONFIG_HISTORY                                                                                 \n"
								+ "   WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX = %d GROUP BY VS_INDEX)   \n"
								+ // where-in:empty string 불가, null 불가, OK
								" ) B                                                                                                       \n"
								+ " ON A.INDEX = B.VS_INDEX                                                                                   \n"
								+ " %s                                                                                                        \n",
						adcIndex, sqlTextVS, adcIndex, sqlWhere);
			} else {
				sqlText = String.format(
						" SELECT A.INDEX AS INDEX, A.ADC_INDEX, A.STATUS AS STATUS, A.USE_YN, A.NAME AS VS_NAME, A.VIRTUAL_IP AS VS_IPADDRESS, A.VIRTUAL_PORT AS SERVICE_PORT, "
								+ " A.POOL_INDEX, A.PERSISTENCE_INDEX, A.SUB_INFO, B.OCCUR_TIME AS OCCUR_TIME"
								+ " FROM (SELECT * FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d) A                                               \n"
								+ " LEFT JOIN ( SELECT VS_INDEX, OCCUR_TIME                                                                   \n"
								+ "   FROM LOG_CONFIG_HISTORY                                                                                 \n"
								+ "   WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX = %d GROUP BY VS_INDEX)   \n"
								+ // where-in:empty string 불가, null 불가, OK
								" ) B                                                                                                       \n"
								+ " ON A.INDEX = B.VS_INDEX                                                                                   \n"
								+ " %s                                                                                                        \n",
						adcIndex, adcIndex, sqlWhere);
			}

			sqlText += searchVServerListOrderType(orderType, orderDir);
			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);
			complexInfoPASK info;
			while (rs.next()) {
				OBDtoAdcVServerPASK obj = new OBDtoAdcVServerPASK();

				obj.setDbIndex(db.getString(rs, "INDEX"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				obj.setState(db.getInteger(rs, "USE_YN"));
				obj.setName(db.getString(rs, "VS_NAME"));
				obj.setvIP(db.getString(rs, "VS_IPADDRESS"));
				obj.setSrvPort(db.getInteger(rs, "SERVICE_PORT"));
				OBDtoAdcPoolPASK poolObj = getPoolInfoPASK(db.getString(rs, "POOL_INDEX"));
				obj.setPool(poolObj);
				obj.setApplyTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setSubInfo(db.getString(rs, "SUB_INFO"));

				info = getComplexInfoPASK(obj.getSubInfo());
				obj.setvIPView(info.getVip());
				obj.setSrvPortView(info.getVport());
				obj.setConfigurable(info.isConfigurable());
				retVal.add(obj);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result list count:%d", retVal.size()));
		return retVal;
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	// db.openDB();
	// db2.openDB();
	// ArrayList<OBDtoAdcVServerPAS> list = new
	// OBVServerDB().searchVServerListPAS(5, "yk");
	// System.out.println(list);
	// db.closeDB();
	// db2.closeDB();
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	// db.openDB();
	// db2.openDB();
	// OBDtoAdcVServerPAS list = new OBVServerDB().getVServerInfoPAS("6_bwpark");
	// System.out.println(list);
	// db.closeDB();
	// db2.closeDB();
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	public String getVServerName(String vsIndex) throws SQLException, OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT NAME " + " FROM TMP_SLB_VSERVER " + " WHERE INDEX=%s ;",
					OBParser.sqlString(vsIndex));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false)
				return null;

			return db.getString(rs, "NAME");
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

	public String getVServerID(String vsIndex) throws OBException {
		String sqlText = " SELECT ALTEON_ID FROM TMP_SLB_VSERVER WHERE INDEX=" + OBParser.sqlString(vsIndex) + ";";

		ResultSet rs;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sqlText);

			if (rs.next() == false)
				return null;

			return db.getString(rs, "ALTEON_ID");
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

	public OBDtoAdcVServerAlteon getVServerInfoByAlteonID(Integer adcIndex, String alteonID) throws OBException {
		OBDtoAdcVServerAlteon obj;
		try {
			obj = getVServerInfoCoreAlteon(adcIndex, QueryConditionType.VS_ALTEON_ID, alteonID);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return obj;
	}

	private OBDtoAdcVServerAlteon getVServerInfoCoreAlteon(Integer adcIndex, QueryConditionType conditionType,
			String conditionWord) throws OBException {
		String sqlWhereClause = "";
		String sqlText = "";
		OBDtoAdcVServerAlteon obj = new OBDtoAdcVServerAlteon();

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String sqlBase;
			sqlBase = " SELECT INDEX, ADC_INDEX, STATUS, NAME, ALTEON_ID, VIRTUAL_IP, "
					+ "     VRRP_STATE, ROUTER_INDEX, VR_INDEX, IF_NUM, USE_YN, SUB_INFO, NWCLASS_ID "
					+ " FROM TMP_SLB_VSERVER";

			if (conditionType == QueryConditionType.VS_NAME) {
				sqlWhereClause = String.format(" WHERE ADC_INDEX=%d AND NAME=%s", adcIndex,
						OBParser.sqlString(conditionWord));
			} else if (conditionType == QueryConditionType.VS_IPADDRESS) {
				sqlWhereClause = String.format(" WHERE ADC_INDEX=%d AND VIRTUAL_IP=%s", adcIndex,
						OBParser.sqlString(conditionWord));
			} else if (conditionType == QueryConditionType.VS_ALTEON_ID) {
				sqlWhereClause = String.format(" WHERE ADC_INDEX=%d AND ALTEON_ID=%s", adcIndex,
						OBParser.sqlString(conditionWord));
			} else
			// conditionType == QueryConditionType.NONE
			{
				sqlWhereClause = String.format(" WHERE ADC_INDEX = %d", adcIndex);
			}

			sqlText = String.format("%s" + "%s ;", sqlBase, sqlWhereClause);

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return null;
			}
			obj.setIndex(db.getString(rs, "INDEX"));
			obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
			Timestamp applyTime = new OBAdcConfigHistoryImpl().getLastConfigTime(obj.getAdcIndex(), obj.getIndex());
			if (applyTime == null)
				applyTime = new OBAdcManagementImpl().getApplyTimeFromDB(obj.getAdcIndex());
			obj.setApplyTime(applyTime);
			obj.setStatus(db.getInteger(rs, "STATUS"));
			obj.setName(db.getString(rs, "NAME"));
			obj.setAlteonId(db.getString(rs, "ALTEON_ID"));
			obj.setvIP(db.getString(rs, "VIRTUAL_IP"));
			obj.setVrrpYN(false); // 안 쓰는데 그냥 값 채움. vrrpState로 대체함
			obj.setVrrpState(db.getInteger(rs, "VRRP_STATE")); // vrrpYN을 대체
			obj.setRouterIndex(db.getInteger(rs, "ROUTER_INDEX"));
			obj.setVrIndex(db.getInteger(rs, "VR_INDEX"));
			obj.setIfNum(db.getInteger(rs, "IF_NUM"));
			obj.setUseYN(db.getInteger(rs, "USE_YN"));
			obj.setNwclassId(db.getInteger(rs, "NWCLASS_ID"));
			obj.setSubInfo(db.getString(rs, "SUB_INFO"));
			obj.setVserviceList(getVServiceList(obj.getIndex()));
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
			// String.format("getVServerInfo-status:%d", obj.getStatus()));
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
		return obj;
	}

	/**
	 * 지정된 장비의 virtual server 정보를 제공한다.
	 * 
	 * @param adcIndex
	 * @param vsName
	 * @param db
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public OBDtoAdcVServerAlteon getVServerInfoAlteon(String vsIndex) throws OBException {
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. vsIndex:%s",
		// vsIndex));

		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT INDEX, ADC_INDEX, STATUS, NAME, ALTEON_ID, VIRTUAL_IP, VRRP_STATE, "
							+ "     ROUTER_INDEX, VR_INDEX, IF_NUM, USE_YN, SUB_INFO, NWCLASS_ID "
							+ " FROM TMP_SLB_VSERVER " + " WHERE INDEX=%s " + " ORDER BY VIRTUAL_IP;",
					OBParser.sqlString(vsIndex));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return null;
			}
			OBDtoAdcVServerAlteon obj = new OBDtoAdcVServerAlteon();
			obj.setIndex(db.getString(rs, "INDEX"));
			obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
			Timestamp applyTime = new OBAdcConfigHistoryImpl().getLastConfigTime(obj.getAdcIndex(), obj.getIndex());
			if (applyTime == null)
				applyTime = new OBAdcManagementImpl().getApplyTimeFromDB(obj.getAdcIndex());
			obj.setApplyTime(applyTime);
			obj.setStatus(db.getInteger(rs, "STATUS"));
			obj.setName(db.getString(rs, "NAME"));
			obj.setAlteonId(db.getString(rs, "ALTEON_ID"));
			obj.setvIP(db.getString(rs, "VIRTUAL_IP"));
			obj.setVrrpYN(false); // 안 쓰는데 그냥 값 채움. vrrpState로 대체함
			obj.setVrrpState(db.getInteger(rs, "VRRP_STATE")); // vrrpYN을 대체
			obj.setRouterIndex(db.getInteger(rs, "ROUTER_INDEX"));
			obj.setVrIndex(db.getInteger(rs, "VR_INDEX"));
			obj.setIfNum(db.getInteger(rs, "IF_NUM"));
			obj.setUseYN(db.getInteger(rs, "USE_YN"));
			obj.setSubInfo(db.getString(rs, "SUB_INFO"));
			obj.setNwclassId(db.getInteger(rs, "NWCLASS_ID"));
			obj.setVserviceList(getVServiceList(obj.getIndex()));
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
			// obj));
			return obj;
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

	public OBDtoAdcVServerF5 getVServerInfoF5(String vsIndex) throws OBException {
		String sqlText = String.format(
				" SELECT A.INDEX, A.ADC_INDEX, A.APPLY_TIME, A.STATUS, A.USE_YN, A.NAME, A.VIRTUAL_IP, A.VIRTUAL_PORT, "
						+ "     A.POOL_INDEX, A.PERSISTENCE_INDEX, B.PERSISTENCE_TYPE " + " FROM TMP_SLB_VSERVER A "
						+ " LEFT JOIN TMP_SLB_PROFILE B " + " ON A.PERSISTENCE_INDEX = B.INDEX " + " WHERE A.INDEX=%s "
						+ " ORDER BY A.VIRTUAL_IP; ",
				OBParser.sqlString(vsIndex));

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return null;
			}

			OBDtoAdcVServerF5 obj = new OBDtoAdcVServerF5();

			obj.setIndex(db.getString(rs, "INDEX"));
			obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
			Timestamp applyTime = new OBAdcConfigHistoryImpl().getLastConfigTime(obj.getAdcIndex(), obj.getIndex());
			if (applyTime == null)
				applyTime = new OBAdcManagementImpl().getApplyTimeFromDB(obj.getAdcIndex());
			obj.setApplyTime(applyTime);
			obj.setStatus(db.getInteger(rs, "STATUS"));
			obj.setUseYN(db.getInteger(rs, "USE_YN"));
			obj.setName(db.getString(rs, "NAME"));
			obj.setvIP(db.getString(rs, "VIRTUAL_IP"));
			obj.setServicePort(db.getInteger(rs, "VIRTUAL_PORT"));
			obj.setPersistence(db.getString(rs, "PERSISTENCE_INDEX"));

			if (db.getInteger(rs, "PERSISTENCE_TYPE") == OBDefine.COMMON_NOT_ALLOWED) {
				obj.setPersistence(OBDefine.COMMON_NOT_ALLOWED_STR);
			}
			obj.setPool(getPoolInfoF5(db.getString(rs, "POOL_INDEX")));
			obj.setVlanFilter(getVlanFilterF5(obj.getAdcIndex(), obj.getIndex(), db));
			return obj;
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

	public OBDtoAdcVServerPAS getVServerInfoPAS(String vsIndex) throws OBException {
		String sqlText = String.format(
				" SELECT INDEX, ADC_INDEX, APPLY_TIME, STATUS, USE_YN, NAME, VIRTUAL_IP, VIRTUAL_PORT, SRV_PROTOCOL, POOL_INDEX "
						+ " FROM TMP_SLB_VSERVER " + " WHERE INDEX=%s " + " ORDER BY VIRTUAL_IP; ",
				OBParser.sqlString(vsIndex));

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return null;
			}

			OBDtoAdcVServerPAS obj = new OBDtoAdcVServerPAS();

			obj.setDbIndex(db.getString(rs, "INDEX"));
			obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
			Timestamp applyTime = new OBAdcConfigHistoryImpl().getLastConfigTime(obj.getAdcIndex(), obj.getDbIndex());
			if (applyTime == null)
				applyTime = new OBAdcManagementImpl().getApplyTimeFromDB(obj.getAdcIndex());
			obj.setApplyTime(applyTime);
			obj.setStatus(db.getInteger(rs, "STATUS"));
			obj.setState(db.getInteger(rs, "USE_YN"));
			obj.setName(db.getString(rs, "NAME"));
			obj.setvIP(db.getString(rs, "VIRTUAL_IP"));
			obj.setSrvPort(db.getInteger(rs, "VIRTUAL_PORT"));
			obj.setSrvProtocol(db.getInteger(rs, "SRV_PROTOCOL"));
			OBDtoAdcPoolPAS poolObj = getPoolInfoPAS(db.getString(rs, "POOL_INDEX"));
			obj.setPool(poolObj);

			return obj;
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

	public OBDtoAdcVServerPASK getVServerInfoPASK(String vsIndex) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX, ADC_INDEX, APPLY_TIME, STATUS, USE_YN, NAME, VIRTUAL_IP, "
					+ "     VIRTUAL_PORT, SRV_PROTOCOL, POOL_INDEX, SUB_INFO " + " FROM TMP_SLB_VSERVER "
					+ " WHERE INDEX=%s " + " ORDER BY VIRTUAL_IP; ", OBParser.sqlString(vsIndex));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return null;
			}
			OBDtoAdcVServerPASK obj = new OBDtoAdcVServerPASK();
			obj.setDbIndex(db.getString(rs, "INDEX"));
			obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
			Timestamp applyTime = new OBAdcConfigHistoryImpl().getLastConfigTime(obj.getAdcIndex(), obj.getDbIndex());
			if (applyTime == null)
				applyTime = new OBAdcManagementImpl().getApplyTimeFromDB(obj.getAdcIndex());
			obj.setApplyTime(applyTime);
			obj.setStatus(db.getInteger(rs, "STATUS"));
			obj.setState(db.getInteger(rs, "USE_YN"));
			obj.setName(db.getString(rs, "NAME"));
			obj.setvIP(db.getString(rs, "VIRTUAL_IP"));
			obj.setSrvPort(db.getInteger(rs, "VIRTUAL_PORT"));
			obj.setSrvProtocol(db.getInteger(rs, "SRV_PROTOCOL"));
			obj.setSubInfo(db.getString(rs, "SUB_INFO"));

			complexInfoPASK info = getComplexInfoPASK(obj.getSubInfo());
			obj.setvIPView(info.getVip());
			obj.setSrvPortView(info.getVport());
			obj.setConfigurable(info.isConfigurable());

			OBDtoAdcPoolPASK poolObj = getPoolInfoPASK(db.getString(rs, "POOL_INDEX"));
			obj.setPool(poolObj);

			return obj;
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

	// public void addVServerInfoF5(Integer adcIndex, Timestamp applyTime, Integer
	// status, Integer state, String name, String virtIP, Integer virtPort, String
	// persistenceIndex, String poolIndex) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// db.openDB();
	// addVServerInfoF5(adcIndex, applyTime, status, state, name, virtIP, virtPort,
	// persistenceIndex, poolIndex, db);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// }

	/**
	 * DB에 virtual server 정보를 추가한다. F5용 함수 엔트리를 하나씩 insert 한다. 이 다음의
	 * addVServerInfoF5All()은 전체 엔트리를 한번 insert 한다.
	 * 
	 */
	public void addVServerInfoF5(Integer adcIndex, Timestamp applyTime, Integer status, Integer state, String name,
			String virtIP, Integer virtPort, String persistenceIndex, String poolIndex) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String index = OBCommon.makeVSIndexF5(adcIndex, name);
			sqlText = String.format("INSERT INTO TMP_SLB_VSERVER "
					+ "( INDEX, ADC_INDEX, APPLY_TIME, STATUS, USE_YN, NAME, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX, PERSISTENCE_INDEX ) "
					+ "VALUES " + "(%s, %d, %s, %d, %d, %s, %s, %d, %s, %s);", OBParser.sqlString(index), adcIndex,
					OBParser.sqlString(applyTime), status, state, OBParser.sqlString(name), OBParser.sqlString(virtIP),
					virtPort, OBParser.sqlString(poolIndex), OBParser.sqlString(persistenceIndex));

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

	/**
	 * DB에 virtual server 정보를 추가한다. F5용 함수 엔트리 전체를 한번에 insert 한다. 앞의
	 * addVServerInfoF5()와 기능은 같고 insert 단위만 다르다.
	 */
	// for(DtoVirtualServer obj:vsList)
	// {
	// vserverDB.addVServerInfoF5(adcIndex, obj.getApplyTime(), obj.getStatus(),
	// obj.getUseYN(), obj.getName(), obj.getvIP(), obj.getServicePort(),
	// vserverDB.getProfileIndex(adcIndex, obj.getPersistenceName(), db),
	// vserverDB.getPoolIndex(adcIndex, obj.getPoolName(), db), db);
	// }
	// public void addVServerInfoF5All(Integer adcIndex, ArrayList<DtoVirtualServer>
	// vsList, OBDatabase db) throws OBException
	// {
	// int vsCount = vsList.size();
	// boolean bVirtualServerQueryDone = false;
	//
	// if(vsCount==0)
	// {
	// return;
	// }
	//
	// String index;
	// DtoVirtualServer obj;
	// String poolIndex;
	// String persistenceProfileIndex;
	// int i;
	// String sqlText="";
	// try
	// {
	// sqlText =
	// String.format(" INSERT INTO TMP_SLB_VSERVER " +
	// "(INDEX, ADC_INDEX, APPLY_TIME, STATUS, USE_YN, NAME, VIRTUAL_IP,
	// VIRTUAL_PORT, POOL_INDEX, PERSISTENCE_INDEX ) " +
	// "VALUES ");
	//
	// for(i=0; i<vsCount; i++)
	// {
	// obj = vsList.get(i);
	// index = OBCommon.makeVSIndexF5(adcIndex, obj.getName());
	// poolIndex = getPoolIndex(adcIndex, obj.getPoolName(), db);
	// persistenceProfileIndex = getProfileIndex(adcIndex, obj.getPersistenceName(),
	// db);
	// sqlText += String.format(" (%s, %d, %s, %d, %d, %s, %s, %d, %s, %s)",
	// OBParser.sqlString(index),
	// adcIndex,
	// OBParser.sqlString(obj.getApplyTime()),
	// obj.getStatus(),
	// obj.getUseYN(),
	// OBParser.sqlString(obj.getName()),
	// OBParser.sqlString(obj.getvIP()),
	// obj.getServicePort(),
	// OBParser.sqlString(poolIndex),
	// OBParser.sqlString(persistenceProfileIndex));
	//
	// if(i==(vsCount-1))
	// {
	// sqlText += ";";
	// bVirtualServerQueryDone = true;
	// }
	// else
	// {
	// sqlText += ",";
	// }
	// }
	//
	// if(bVirtualServerQueryDone)
	// {
	// db.executeUpdate(sqlText);
	// }
	// else
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("sqlText =
	// %s, VirtualServer insert query error", sqlText));
	// }
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	public void addVServerInfoF5(Integer adcIndex, ArrayList<DtoVirtualServer> vsList, StringBuilder query)
			throws OBException {
		if (vsList == null || vsList.size() == 0) {
			return;
		}

		final String delimiter = ", ";
		String prefix = "";

		query.append(" INSERT INTO TMP_SLB_VSERVER ").append(
				"(INDEX, ADC_INDEX, APPLY_TIME, STATUS, USE_YN, NAME, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX, PERSISTENCE_INDEX ) ")
				.append(" VALUES ");

		for (DtoVirtualServer obj : vsList) {
			String index = OBCommon.makeVSIndexF5(adcIndex, obj.getName());
			String poolIndex = OBCommon.makePoolIndex(adcIndex, obj.getPoolName());
			String persistenceProfileIndex = getProfileIndex(adcIndex, obj.getPersistenceName());

			query.append(prefix).append("(").append(OBParser.sqlString(index)).append(delimiter).append(adcIndex)
					.append(delimiter).append(OBParser.sqlString(obj.getApplyTime())).append(delimiter)
					.append(obj.getStatus()).append(delimiter).append(obj.getUseYN()).append(delimiter)
					.append(OBParser.sqlString(obj.getName())).append(delimiter)
					.append(OBParser.sqlString(obj.getvIP())).append(delimiter).append(obj.getServicePort())
					.append(delimiter).append(OBParser.sqlString(poolIndex)).append(delimiter)
					.append(OBParser.sqlString(persistenceProfileIndex)).append(")");

			prefix = ", ";
		}

		query.append(";");
	}

	public void updateVServerInfoF5(Integer adcIndex, ArrayList<OBDtoAdcVServerF5> vsList, StringBuilder query)
			throws OBException {
		if (vsList.size() == 0) {
			return;
		}

		String prefix = "";

		for (OBDtoAdcVServerF5 obj : vsList) {
			String index = OBCommon.makeVSIndexF5(adcIndex, obj.getName());
			String poolIndex = OBCommon.makeVSIndexF5(adcIndex, obj.getPool().getName());
			query.append(prefix).append(" UPDATE TMP_SLB_VSERVER SET ").append(" POOL_INDEX = ")
					.append(OBParser.sqlString(poolIndex)).append(" WHERE INDEX = ").append(OBParser.sqlString(index))
					.append(";");
		}
	}

	// /**
	// * DB에 virtual server 정보를 추가한다.
	// *
	// * @param obj
	// * @param db
	// * @throws SQLException
	// * @throws NullPointerException
	// * @throws IllegalArgumentException
	// */
	// public void addVServerInfoAlteon(Integer adcIndex, Timestamp applyTime,
	// Integer status, Integer state, String name, Integer alteonID, String virtIP,
	// Integer vrrpState, Integer routerIndex,
	// Integer vrIndex, Integer ifNum, String subInfo, Integer nwclassId, OBDatabase
	// db) throws OBException
	// {
	// //OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start.
	// adcIndex:%d, name:%s, id:%d, virtIP:%s, routerIndex:%d, vrIndex:%d,
	// ifNum:%d", adcIndex, name, alteonID, virtIP, routerIndex, vrIndex, ifNum));
	//
	// String sqlText="";
	// try
	// {
	// String index=OBCommon.makeVSIndexAlteon(adcIndex, alteonID);
	// sqlText = String.format(
	// " INSERT INTO TMP_SLB_VSERVER " +
	// " (INDEX, ADC_INDEX, APPLY_TIME, STATUS, USE_YN, NAME, ALTEON_ID, VIRTUAL_IP,
	// \n" +
	// " VRRP_YN, VRRP_STATE, ROUTER_INDEX, VR_INDEX, IF_NUM, SUB_INFO, NWCLASS_ID
	// \n" +
	// " ) \n" +
	// " VALUES (%s, %d, %s, %d, %d, %s, %d, %s, %d, %d, %d, %d, %d, %s, %d); "
	// , OBParser.sqlString(index)
	// , adcIndex
	// , OBParser.sqlString(applyTime)
	// , status
	// , state
	// , OBParser.sqlString(name)
	// , alteonID
	// , OBParser.sqlString(virtIP)
	// , OBDefine.VRRP_STATE.NONE //VRRP_YN
	// , vrrpState //VRRP_STATE
	// , routerIndex
	// , vrIndex
	// , ifNum
	// , OBParser.sqlString(subInfo)
	// , nwclassId
	// );
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	/**
	 * @param adcIndex
	 * @param vsList
	 * @param db
	 * @throws OBException 1. pool 추가 2. virtual server 목록을 가져다 모아서 DB에 업데이트 한다.
	 *                     vrrp enable/disable 여부에 따라 분리해서 insert한다.
	 */
	// public void addPoolVServerVServiceAlteon(Integer adcIndex,
	// ArrayList<OBDtoAdcPoolAlteon> poolList, ArrayList<OBDtoAdcVServerAlteon>
	// vsList, OBDatabase db) throws OBException
	// {
	// // 아래 작업은 순서를 지켜야 한다. 각 테이블간의 relation 이 존재.
	// // pool 추가
	// addPoolInfoAlteon(adcIndex, poolList, db);
	//
	// String sqlVrrp = String.format(
	// " INSERT INTO TMP_SLB_VSERVER \n" +
	// " (INDEX, ADC_INDEX, APPLY_TIME, STATUS, USE_YN, NAME, ALTEON_ID, VIRTUAL_IP,
	// VRRP_YN, VRRP_STATE, SUB_INFO, NWCLASS_ID, " +
	// " ROUTER_INDEX, VR_INDEX, IF_NUM) \n" +
	// " VALUES "
	// );
	// String sqlNonVrrp = String.format(
	// " INSERT INTO TMP_SLB_VSERVER \n" +
	// " (INDEX, ADC_INDEX, APPLY_TIME, STATUS, USE_YN, NAME, ALTEON_ID, VIRTUAL_IP,
	// VRRP_YN, VRRP_STATE, SUB_INFO, NWCLASS_ID ) " +
	// " VALUES "
	// );
	//
	// String sqlService = String.format(
	// " INSERT INTO TMP_SLB_VS_SERVICE \n"
	// + " (INDEX, ADC_INDEX, VS_INDEX, VIRTUAL_PORT, REAL_PORT, POOL_INDEX, STATUS)
	// \n"
	// + " VALUES "
	// );
	//
	// String sqlVrrpValues = "";
	// String sqlNonVrrpValues = "";
	// String sqlServiceValues = "";
	//
	// int vrrpCount=0;
	// int nonVrrpCount=0;
	// int serviceCount=0;
	//
	// try
	// {
	// for(OBDtoAdcVServerAlteon vsObj:vsList)
	// {
	// String vsIndex=OBCommon.makeVSIndexAlteon(adcIndex, vsObj.getAlteonId());
	// //vrrp yes/no 상관없이 index는 공통
	// if(vsObj.getVrrpState().equals(OBDefine.VRRP_STATE.NONE))//VRRP 설정이 없음
	// {
	// sqlNonVrrpValues += String.format("(%s, %d, %s, %d, %d, %s, %d, %s, %d, %d,
	// %s, %d), \n",
	// OBParser.sqlString(vsIndex),
	// adcIndex,
	// OBParser.sqlString(vsObj.getApplyTime()),
	// vsObj.getStatus(),
	// vsObj.getState(),
	// OBParser.sqlString(vsObj.getName()),
	// vsObj.getAlteonId(),
	// OBParser.sqlString(vsObj.getvIP()),
	// OBDefine.COMMON_NOT_DEFINED, //VRRP_YN
	// vsObj.getVrrpState(), //VRRP_STATE : vrrp 없음
	// OBParser.sqlString(vsObj.getSubInfo()),
	// vsObj.getNwclassId()
	// );
	// nonVrrpCount++;
	// }
	// else //vrrp 설정이 있음 : enable or disable
	// {
	// sqlVrrpValues += String.format("(%s, %d, %s, %d, %d, %s, %d, %s, %d, %d, %s,
	// %d, %d, %d, %d), \n",
	// OBParser.sqlString(vsIndex),
	// adcIndex,
	// OBParser.sqlString(vsObj.getApplyTime()),
	// vsObj.getStatus(),
	// vsObj.getState(),
	// OBParser.sqlString(vsObj.getName()),
	// vsObj.getAlteonId(),
	// OBParser.sqlString(vsObj.getvIP()),
	// OBDefine.COMMON_NOT_DEFINED, //VRRP_YN
	// vsObj.getVrrpState(), //VRRP_STATE, vrrp enable or disable
	// OBParser.sqlString(vsObj.getSubInfo()),
	// vsObj.getNwclassId(),
	// vsObj.getRouterIndex(),
	// vsObj.getVrIndex(),
	// vsObj.getIfNum()
	// );
	// vrrpCount++;
	// }
	//
	// int virtualPort;
	// int realPort;
	// int status;
	// String poolIndex;
	// String serviceIndex;
	//
	// ArrayList<OBDtoAdcVService> serviceList = vsObj.getVserviceList();
	// for(int i=0, size=serviceList.size(); i<size; i++)
	// {
	// OBDtoAdcVService serviceObj=serviceList.get(i);
	// OBDtoAdcPoolAlteon poolObj = serviceObj.getPool();
	//
	// virtualPort = serviceObj.getServicePort();
	// realPort = serviceObj.getRealPort();
	// status = serviceObj.getStatus();
	//
	// poolIndex = getPoolIndex(adcIndex, poolObj.getAlteonId(), db);
	// serviceIndex = OBCommon.makeVSrcIndexAlteon(adcIndex, vsIndex,
	// virtualPort);//adcIndex+"_"+vsIndex+"_"+virtualPort;
	//
	// sqlServiceValues += String.format("(%s, %d, %s, %d, %d, %s, %d), \n",
	// OBParser.sqlString(serviceIndex),
	// adcIndex,
	// OBParser.sqlString(vsIndex),
	// virtualPort,
	// realPort,
	// OBParser.sqlString(poolIndex),
	// status
	// );
	// serviceCount++;
	// }
	// }
	//
	// //OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlVrrpValues = " +
	// sqlVrrpValues);
	// //OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlNonVrrpValues = " +
	// sqlNonVrrpValues);
	// //OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlServiceValues = " +
	// sqlServiceValues);
	//
	// //마지막 comma를 제거한다.
	// if(sqlVrrpValues.lastIndexOf( ',')>=0)
	// {
	// sqlVrrpValues = sqlVrrpValues.substring(0, sqlVrrpValues.lastIndexOf( ','));
	// }
	// if(sqlNonVrrpValues.lastIndexOf( ',')>=0)
	// {
	// sqlNonVrrpValues = sqlNonVrrpValues.substring(0,
	// sqlNonVrrpValues.lastIndexOf( ','));
	// }
	// if(sqlServiceValues.lastIndexOf( ',')>=0)
	// {
	// sqlServiceValues = sqlServiceValues.substring(0,
	// sqlServiceValues.lastIndexOf( ','));
	// }
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	//
	// //vrrp virtualserver를 추가한다.
	// try
	// {
	// if(vrrpCount>0)
	// {
	// sqlVrrp = sqlVrrp + sqlVrrpValues;
	// db.executeUpdate(sqlVrrp);
	// }
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlVrrp));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	//
	// //non-vrrp virtualserver를 추가한다.
	// try
	// {
	// if(nonVrrpCount>0)
	// {
	// sqlNonVrrp = sqlNonVrrp + sqlNonVrrpValues;
	// db.executeUpdate(sqlNonVrrp);
	// }
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlNonVrrp));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	//
	// //virtualservice를 추가한다.
	// try
	// {
	// if(serviceCount>0)
	// {
	// sqlService = sqlService + sqlServiceValues;
	// db.executeUpdate(sqlService);
	// }
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlService));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void addPoolVServerVServiceAlteon(Integer adcIndex, ArrayList<OBDtoAdcPoolAlteon> poolList,
			ArrayList<OBDtoAdcVServerAlteon> vsList, StringBuilder query) throws OBException {
		// 아래 작업은 순서를 지켜야 한다. 각 테이블간의 relation 이 존재.
		// pool 추가
		addPoolInfoAlteon(adcIndex, poolList, query);

		final String delimiter = ", ";

		StringBuilder queryVrrpValues = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);
		StringBuilder queryNonVrrpValues = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);
		StringBuilder queryServiceValues = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);

		String vrrpQueryPrefix = "";
		String nonVrrpQueryPrefix = "";
		String serviceQueryPrefix = "";

		for (OBDtoAdcVServerAlteon vsObj : vsList) {
			String vsIndex = OBCommon.makeVSIndexAlteon(adcIndex, vsObj.getAlteonId()); // vrrp yes/no 상관없이 index는 공통

			if (vsObj.getVrrpState().equals(OBDefine.VRRP_STATE.NONE))// VRRP 설정이 없음
			{
				queryNonVrrpValues.append(nonVrrpQueryPrefix).append("(").append(OBParser.sqlString(vsIndex))
						.append(delimiter).append(adcIndex).append(delimiter)
						.append(OBParser.sqlString(vsObj.getApplyTime())).append(delimiter).append(vsObj.getStatus())
						.append(delimiter).append(vsObj.getState()).append(delimiter)
						.append(OBParser.sqlString(vsObj.getName())).append(delimiter)
						.append(OBParser.sqlString(vsObj.getAlteonId())).append(delimiter)
						.append(OBParser.sqlString(vsObj.getvIP())).append(delimiter)
						.append(OBDefine.COMMON_NOT_DEFINED).append(delimiter).append(vsObj.getVrrpState())
						.append(delimiter).append(OBParser.sqlString(vsObj.getSubInfo())).append(delimiter)
						.append(vsObj.getNwclassId()).append(")");

				nonVrrpQueryPrefix = ", ";
			} else
			// vrrp 설정이 있음 : enable or disable
			{
				queryVrrpValues.append(vrrpQueryPrefix).append("(").append(OBParser.sqlString(vsIndex))
						.append(delimiter).append(adcIndex).append(delimiter)
						.append(OBParser.sqlString(vsObj.getApplyTime())).append(delimiter).append(vsObj.getStatus())
						.append(delimiter).append(vsObj.getState()).append(delimiter)
						.append(OBParser.sqlString(vsObj.getName())).append(delimiter)
						.append(OBParser.sqlString(vsObj.getAlteonId())).append(delimiter)
						.append(OBParser.sqlString(vsObj.getvIP())).append(delimiter)
						.append(OBDefine.COMMON_NOT_DEFINED).append(delimiter).append(vsObj.getVrrpState())
						.append(delimiter).append(OBParser.sqlString(vsObj.getSubInfo())).append(delimiter)
						.append(vsObj.getNwclassId()).append(delimiter).append(vsObj.getRouterIndex()).append(delimiter)
						.append(vsObj.getVrIndex()).append(delimiter).append(vsObj.getIfNum()).append(")");

				vrrpQueryPrefix = ", ";
			}

			for (OBDtoAdcVService serviceObj : vsObj.getVserviceList()) {
				OBDtoAdcPoolAlteon poolObj = serviceObj.getPool();

				int virtualPort = serviceObj.getServicePort();
				int realPort = serviceObj.getRealPort();
				int status = serviceObj.getStatus();

				String poolIndex = OBCommon.makeVSIndexAlteon(adcIndex, poolObj.getAlteonId());
				String serviceIndex = OBCommon.makeVSrcIndexAlteon(adcIndex, vsIndex, virtualPort);// adcIndex+"_"+vsIndex+"_"+virtualPort;

				queryServiceValues.append(serviceQueryPrefix).append("(").append(OBParser.sqlString(serviceIndex))
						.append(delimiter).append(adcIndex).append(delimiter).append(OBParser.sqlString(vsIndex))
						.append(delimiter).append(virtualPort).append(delimiter).append(realPort).append(delimiter)
						.append(OBParser.sqlString(poolIndex)).append(delimiter).append(status).append(")");

				serviceQueryPrefix = ", ";
			}

		}

		queryNonVrrpValues.append(";");
		queryVrrpValues.append(";");
		queryServiceValues.append(";");

		// 무조건 ";" 문자 하나는 들어간다.
		if (queryVrrpValues.length() > 1) {
			query.append(" INSERT INTO TMP_SLB_VSERVER ").append(
					" (INDEX, ADC_INDEX, APPLY_TIME, STATUS, USE_YN, NAME, ALTEON_ID, VIRTUAL_IP, VRRP_YN, VRRP_STATE, SUB_INFO, NWCLASS_ID, ROUTER_INDEX, VR_INDEX, IF_NUM) ")
					.append(" VALUES ").append(queryVrrpValues.toString());
		}

		if (queryNonVrrpValues.length() > 1) {
			query.append(" INSERT INTO TMP_SLB_VSERVER ").append(
					" (INDEX, ADC_INDEX, APPLY_TIME, STATUS, USE_YN, NAME, ALTEON_ID, VIRTUAL_IP, VRRP_YN, VRRP_STATE, SUB_INFO, NWCLASS_ID ) ")
					.append(" VALUES ").append(queryNonVrrpValues.toString());
		}

		if (queryServiceValues.length() > 1) {
			query.append(" INSERT INTO TMP_SLB_VS_SERVICE ")
					.append(" (INDEX, ADC_INDEX, VS_INDEX, VIRTUAL_PORT, REAL_PORT, POOL_INDEX, STATUS) ")
					.append(" VALUES ").append(queryServiceValues.toString());
		}
	}

	// /**
	// * DB에 virtual server 정보를 추가한다.
	// *
	// * @param obj
	// * @param db
	// * @throws SQLException
	// * @throws NullPointerException
	// * @throws IllegalArgumentException
	// */
	// public void addVServerInfoNoVrrp(Integer adcIndex, Timestamp applyTime,
	// Integer status, Integer state, String name, Integer alteonID, String virtIP,
	// String subInfo, Integer nwclassId, OBDatabase db) throws OBException
	// {
	// //OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start.
	// adcIndex:%d, name:%s, id:%d, virtIP:%s", adcIndex, name, alteonID, virtIP));
	//
	// String sqlText="";
	// try
	// {
	// String index=OBCommon.makeVSIndexAlteon(adcIndex, alteonID);
	// sqlText = String.format(
	// " INSERT INTO TMP_SLB_VSERVER " +
	// " (INDEX, ADC_INDEX, APPLY_TIME, STATUS, USE_YN, NAME, ALTEON_ID, VIRTUAL_IP,
	// VRRP_YN, VRRP_STATE, SUB_INFO, NWCLASS_ID) " +
	// " VALUES (%s, %d, %s, %d, %d, %s, %d, %s, %d, %d, %s, %d);"
	// , OBParser.sqlString(index)
	// , adcIndex
	// , OBParser.sqlString(applyTime)
	// , status
	// , state
	// , OBParser.sqlString(name)
	// , alteonID
	// , OBParser.sqlString(virtIP)
	// , OBDefine.VRRP_STATE.NONE //VRRP_YN, 안 쓰는 값
	// , OBDefine.VRRP_STATE.NONE //VRRP_STATE
	// , OBParser.sqlString(subInfo)
	// , nwclassId
	// );
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// /**
	// * 지정된 ADC 장비의 Virtual server 정보를 삭제한다.
	// *
	// * @param adcIndex
	// * -- ADC 장비 index
	// * @throws SQLException
	// * @throws NullPointerException
	// * @throws IllegalArgumentException
	// */
	// public void delVServerAll(Integer adcIndex) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// try
	// {
	// db.openDB();
	// delAdcConfigAll(adcIndex, "TMP_SLB_VSERVER", db);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// }

	/**
	 * 지정된 ADC 장비의 Virtual Server 정보를 삭제한다.
	 * 
	 * @param adcIndex -- ADC 장비 index
	 * @param db       -- DB 인스턴스.
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	// public void delVServerAll(Integer adcIndex, OBDatabase db) throws OBException
	// {
	// delAdcConfigAll(adcIndex, "TMP_SLB_VSERVER", db);
	// }

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void delVServerAll(Integer adcIndex, StringBuilder query) throws OBException {
		delTableAll(adcIndex, "TMP_SLB_VSERVER", query);
	}

	public void delVServerPartial(Integer adcIndex, ArrayList<String> vsIndexList, StringBuilder query)
			throws OBException {
		delTablePartial(adcIndex, "TMP_SLB_VSERVER", "INDEX", vsIndexList, query);
	}

	public void delF5VlanFilterAll(Integer adcIndex, StringBuilder query) throws OBException {
		delTableAll(adcIndex, "TMP_SLB_VLANTUNNEL_FILTER", query);
	}

	public void delF5VlanFilter(Integer adcIndex, String vsName, StringBuilder query) throws OBException {
		delVSFilter(adcIndex, "TMP_SLB_VLANTUNNEL_FILTER", vsName, query);
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public void delVServerAll(Integer adcIndex) throws OBException {
		delTableAll(adcIndex, "TMP_SLB_VSERVER");
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// db.openDB();
	// new OBVServerDB().updateAdcAllStatus(5, db);
	// db.closeDB();
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	// public void updateAdcAllStatus(int adcIndex, OBDatabase db) throws
	// OBException
	// {
	// String sqlText="";
	// try
	// {
	// //update vserver status.
	// sqlText = String.format(" UPDATE TMP_SLB_VSERVER " +
	// " SET STATUS = %d " +
	// " WHERE ADC_INDEX=%d ;",
	// OBDefine.VS_STATUS.DISABLE,
	// adcIndex);
	//
	//
	// db.executeUpdate(sqlText);
	//
	// //update vservice status.
	// sqlText = String.format(" UPDATE TMP_SLB_VS_SERVICE " +
	// " SET STATUS = %d " +
	// " WHERE ADC_INDEX=%d ;",
	// OBDefine.VS_STATUS.DISABLE,
	// adcIndex);
	//
	// db.executeUpdate(sqlText);
	//
	// //update pool member's status.
	// // sqlText = String.format(" UPDATE TMP_SLB_POOLMEMBER " +
	// sqlText = String.format(" UPDATE TMP_SLB_POOLMEMBER_STATUS " +
	// " SET MEMBER_STATUS = %d " +
	// " WHERE ADC_INDEX=%d ;",
	// OBDefine.MEMBER_STATUS.DISABLE,
	// adcIndex);
	//
	// db.executeUpdate(sqlText);
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public void updateAdcAllStatus(int adcIndex) throws OBException {
		String sqlText = "";

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			// update vserver status.
			sqlText = String.format(" UPDATE TMP_SLB_VSERVER " + " SET STATUS = %d " + " WHERE ADC_INDEX=%d ;",
					OBDefine.VS_STATUS.DISABLE, adcIndex);

			db.executeUpdate(sqlText);

			// update vservice status.
			sqlText = String.format(" UPDATE TMP_SLB_VS_SERVICE " + " SET STATUS = %d " + " WHERE ADC_INDEX=%d ;",
					OBDefine.VS_STATUS.DISABLE, adcIndex);

			db.executeUpdate(sqlText);

			// update pool member's status.
			// sqlText = String.format(" UPDATE TMP_SLB_POOLMEMBER " +
			sqlText = String.format(
					" UPDATE TMP_SLB_POOLMEMBER_STATUS " + " SET MEMBER_STATUS = %d " + " WHERE ADC_INDEX=%d ;",
					OBDefine.MEMBER_STATUS.DISABLE, adcIndex);

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

	// public void updateVServerStatusList(ArrayList<OBDtoVServerStatus> statusList)
	// throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// db.openDB();
	// }
	// catch(Exception e)
	// {
	// throw new OBException(e.getMessage());
	// }
	//
	// try
	// {
	// updateVServerStatusList(statusList, db);
	// }
	// catch(Exception e)
	// {
	// db.closeDB();
	// throw new OBException(e.getMessage());
	// }
	// db.closeDB();
	// }
	//
	// public void updateVServerStatusList(ArrayList<OBDtoVServerStatus> statusList,
	// OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// for(int i=0;i<statusList.size();i++)
	// {
	// OBDtoVServerStatus obj = statusList.get(i);
	// try
	// {
	// sqlText = String.format(" UPDATE TMP_SLB_VSERVER " +
	// " SET STATUS = %d" +
	// " WHERE INDEX=%s;",
	// obj.getStatus(),
	// OBParser.sqlString(obj.getVsIndex()));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(e.getMessage());
	// }
	//
	// try
	// {
	// db.executeUpdate(sqlText);
	// }
	// catch(Exception e)
	// {
	// throw new OBException(e.getMessage());
	// }
	// }
	// }

	public ArrayList<OBDtoNetworkInterface> getL3InterfaceAll(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();

		ArrayList<OBDtoNetworkInterface> list;
		try {
			db.openDB();
			list = getL3InterfaceAll(adcIndex, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return list;
	}

	public ArrayList<OBDtoNetworkInterface> getL3InterfaceAll(Integer adcIndex, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format("SELECT IF_NUM, IPADDRESS, NETMASK " + "FROM TMP_L3_INTERFACE "
					+ "WHERE ADC_INDEX=%d " + "ORDER BY IPADDRESS;", adcIndex);

			ResultSet rs = db.executeQuery(sqlText);

			ArrayList<OBDtoNetworkInterface> list = new ArrayList<OBDtoNetworkInterface>();
			while (rs.next()) {
				OBDtoNetworkInterface obj = new OBDtoNetworkInterface();
				obj.setIfNum(db.getInteger(rs, "IF_NUM"));
				obj.setIpAddress(db.getString(rs, "IPADDRESS"));
				obj.setNetmask(db.getString(rs, "NETMASK"));
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// public void addL3InterfaceInfo(Integer adcIndex, Integer ifNum, String
	// ipAddress) throws OBException
	// {
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
	// addL3InterfaceInfo(adcIndex, ifNum, ipAddress, db);
	// }
	// catch(OBException e)
	// {
	// db.closeDB();
	// throw e;
	// }
	// catch(Exception e)
	// {
	// db.closeDB();
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// db.closeDB();
	// }

	// public void addL3InterfaceInfo(Integer adcIndex, Integer ifNum, String
	// ipAddress, OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// try
	// {
	// sqlText = String.format("INSERT INTO TMP_L3_INTERFACE " +
	// "(ADC_INDEX, IF_NUM, IPADDRESS) " +
	// "VALUES " +
	// "(%d, %d, %s); ",
	// adcIndex,
	// ifNum,
	// OBParser.sqlString(ipAddress));
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// public void addL3InterfaceInfo(Integer adcIndex,
	// ArrayList<OBDtoNetworkInterface> ifList, OBDatabase db) throws OBException
	// {
	// String sqlText=String.format("INSERT INTO TMP_L3_INTERFACE " +
	// "(ADC_INDEX, IF_NUM, IPADDRESS, NETMASK) " +
	// "VALUES ");
	// try
	// {
	// String subText="";
	// for(OBDtoNetworkInterface obj:ifList)
	// {
	// if(!subText.isEmpty())
	// subText += ", ";
	// subText += String.format("(%d, %d, %s, %s)",
	// adcIndex,
	// obj.getIfNum(),
	// OBParser.sqlString(obj.getIpAddress()),
	// OBParser.sqlString(obj.getNetmask()));
	// }
	// if(!subText.isEmpty())
	// {
	// sqlText += subText + ";";
	// db.executeUpdate(sqlText);
	// }
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void addL3InterfaceInfo(Integer adcIndex, ArrayList<OBDtoNetworkInterface> ifList, StringBuilder query)
			throws OBException {
		if (ifList.size() == 0) {
			return;
		}

		query.append(" INSERT INTO TMP_L3_INTERFACE ").append(" (ADC_INDEX, IF_NUM, IPADDRESS, NETMASK) ")
				.append(" VALUES ");

		final String delimiter = ", ";
		String prefix = "";

		for (OBDtoNetworkInterface obj : ifList) {
			query.append(prefix).append("(").append(adcIndex).append(delimiter).append(obj.getIfNum()).append(delimiter)
					.append(OBParser.sqlString(obj.getIpAddress())).append(delimiter)
					.append(OBParser.sqlString(obj.getNetmask())).append(")");

			prefix = ", ";
		}

		query.append(" ; ");
	}

	// public void delL3InterfaceAll(Integer adcIndex) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// try
	// {
	// db.openDB();
	// delAdcConfigAll(adcIndex, "TMP_L3_INTERFACE", db);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// }

	// public void delL3InterfaceAll(Integer adcIndex, OBDatabase db) throws
	// OBException
	// {
	// delAdcConfigAll(adcIndex, "TMP_L3_INTERFACE", db);
	// }

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void delL3InterfaceAll(Integer adcIndex, StringBuilder query) {
		delTableAll(adcIndex, "TMP_L3_INTERFACE", query);
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public void delL3InterfaceAll(Integer adcIndex) throws OBException {
		delTableAll(adcIndex, "TMP_L3_INTERFACE");
	}

	// public ArrayList<OBDtoAdcVService> getVServiceListAll(Integer adcIndex)
	// throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// ArrayList<OBDtoAdcVService> list;
	// try
	// {
	// db.openDB();
	// list = getVServiceListAll(adcIndex, db);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// return list;
	// }

	// public ArrayList<OBDtoAdcVService> getVServiceListAll(Integer adcIndex,
	// OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// try
	// {
	// sqlText = String.format("SELECT VIRTUAL_PORT, REAL_PORT, POOL_INDEX " +
	// "FROM TMP_SLB_VS_SERVICE " +
	// "WHERE ADC_INDEX=%d;" +
	// adcIndex);
	// ArrayList<OBDtoAdcVService> list = new ArrayList<OBDtoAdcVService>();
	//
	// ResultSet rs = db.executeQuery(sqlText);
	//
	// while(rs.next())
	// {
	// OBDtoAdcVService obj = new OBDtoAdcVService();
	// obj.setRealPort(db.getInteger(rs, "REAL_PORT"));
	// obj.setServicePort(db.getInteger(rs, "VIRTUAL_PORT"));
	// obj.setPool(getPoolInfoAlteon(db.getString(rs, "POOL_INDEX")));
	// list.add(obj);
	// }
	// return list;
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// public ArrayList<OBDtoAdcVService> getVServiceList(String vsIndex) throws
	// OBException
	// {
	// OBDatabase db = new OBDatabase();
	// ArrayList<OBDtoAdcVService> list;
	// try
	// {
	// db.openDB();
	//
	// list = getVServiceList(vsIndex, db);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// return list;
	// }

	public ArrayList<OBDtoAdcVService> getVServiceList(String vsIndex) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX, VIRTUAL_PORT, REAL_PORT, POOL_INDEX, STATUS "
					+ " FROM TMP_SLB_VS_SERVICE " + " WHERE VS_INDEX=%s; ", OBParser.sqlString(vsIndex));

			ArrayList<OBDtoAdcVService> list = new ArrayList<OBDtoAdcVService>();

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcVService obj = new OBDtoAdcVService();

				obj.setServiceIndex(db.getString(rs, "INDEX"));
				obj.setServicePort(db.getInteger(rs, "VIRTUAL_PORT"));
				obj.setRealPort(db.getInteger(rs, "REAL_PORT"));
				obj.setPool(getPoolInfoAlteon(db.getString(rs, "POOL_INDEX")));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				list.add(obj);
			}
			return list;
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

	// public OBDtoAdcVService getVServiceInfo(Integer adcIndex, String vsIndex,
	// Integer serviceNum) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	//
	// OBDtoAdcVService info;
	// try
	// {
	// db.openDB();
	// db2.openDB();
	//
	// info = getVServiceInfo(adcIndex, vsIndex, serviceNum, db, db2);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// if(db2!=null) db2.closeDB();
	// }
	// return info;
	// }

	public OBDtoAdcVService getVServiceInfo(Integer adcIndex, String vsIndex, Integer serviceNum) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					"SELECT VIRTUAL_PORT, REAL_PORT, POOL_INDEX " + "FROM TMP_SLB_VS_SERVICE "
							+ "WHERE ADC_INDEX=%d AND VS_INDEX=%s AND VIRTUAL_PORT=%d " + ";",
					adcIndex, OBParser.sqlString(vsIndex), serviceNum);

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false)
				return null;

			OBDtoAdcVService obj = new OBDtoAdcVService();
			obj.setRealPort(db.getInteger(rs, "REAL_PORT"));
			obj.setServicePort(db.getInteger(rs, "VIRTUAL_PORT"));
			obj.setPool(getPoolInfoAlteon(db.getString(rs, "POOL_INDEX")));
			return obj;
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

	// public void addVServiceInfoAlteon(int adcIndex, String vsIndex, int virtPort,
	// int realPort, String poolIndex, int status) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// db.openDB();
	// addVServiceInfoAlteon(adcIndex, vsIndex, virtPort, realPort, poolIndex,
	// status, db);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// }

	// public void addVServiceInfoAlteon(int adcIndex, String vsIndex, int virtPort,
	// int realPort, String poolIndex, int status, OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// try
	// {
	// String index=OBCommon.makeVSrcIndexAlteon(adcIndex, vsIndex,
	// virtPort);//adcIndex+"_"+vsIndex+"_"+virtPort;
	// sqlText = String.format("INSERT INTO TMP_SLB_VS_SERVICE " +
	// "(INDEX, ADC_INDEX, VS_INDEX, VIRTUAL_PORT, REAL_PORT, POOL_INDEX, STATUS) "
	// +
	// "VALUES " +
	// "(%s, %d, %s, %d, %d, %s, %d); ",
	// OBParser.sqlString(index),
	// adcIndex,
	// OBParser.sqlString(vsIndex),
	// virtPort,
	// realPort,
	// OBParser.sqlString(poolIndex),
	// status);
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }
	/**
	 * DB에 Filter를 추가한다.
	 */
	// public void addFilterAndPortMappingAlteon(Integer adcIndex,
	// ArrayList<OBDtoFlbFilterInfo> filters, ArrayList<DtoSnmpFilterPortAlteon>
	// physicalPorts, OBDatabase db) throws OBException
	// {
	// int filterCount = filters.size();
	// if(filterCount==0)
	// {
	// return;
	// }
	// String filterDbIndex;
	// String sqlText="";
	// int i, j;
	// OBDtoFlbFilterInfo filter;
	// try
	// {
	// sqlText = String.format(
	// " INSERT INTO TMP_FLB_FILTER ( INDEX, ID, ADC_INDEX, STATE, NAME, SRC_IP,
	// SRC_MASK, " +
	// " DST_IP, DST_MASK, PROTOCOL, SRC_PORT_FROM, SRC_PORT_TO, DST_PORT_FROM,
	// DST_PORT_TO, ACTION, GROUP_ID, REDIRECTION ) " +
	// " VALUES " );
	// for(i=0; i<filterCount; i++)
	// {
	// filter = filters.get(i);
	// filterDbIndex=OBCommon.makeFilterIndexAlteon(adcIndex, filter.getFilterId());
	// sqlText += String.format(" (%s, %d, %d, %d, %s, %s, %s, %s, %s, %d, %d, %d,
	// %d, %d, %s, %s, %s), ",
	// OBParser.sqlString(filterDbIndex),
	// filter.getFilterId(),
	// adcIndex,
	// filter.getState(),
	// OBParser.sqlString(filter.getName()),
	// OBParser.sqlString(filter.getSrcIP()),
	// OBParser.sqlString(filter.getSrcMask()),
	// OBParser.sqlString(filter.getDstIP()),
	// OBParser.sqlString(filter.getDstMask()),
	// filter.getProtocol(),
	// filter.getSrcPortFrom(),
	// filter.getSrcPortTo(),
	// filter.getDstPortFrom(),
	// filter.getDstPortTo(),
	// OBParser.sqlString(filter.getAction()),
	// OBParser.sqlString(filter.getGroup()),
	// OBParser.sqlString(filter.getRedirection())
	// );
	// }
	//
	// //마지막 comma를 제거한다.
	// if(sqlText.lastIndexOf( ',')>=0)
	// {
	// sqlText = sqlText.substring(0, sqlText.lastIndexOf( ','));
	// }
	// db.executeUpdate(sqlText);
	// //physical-port mapping 저장
	// sqlText = String.format(
	// " INSERT INTO TMP_FLB_PORT_FILTER_MAP ( PORT_ALTEON_ID, FILTER_ALTEON_ID,
	// FILTER_INDEX, ADC_INDEX ) " +
	// " VALUES " );
	// int portCount = physicalPorts.size();
	// int involvedFilterCount = 0;
	// int totalInvolvedFilterCount = 0;
	//
	// DtoSnmpFilterPortAlteon port;
	// for(i=0; i<portCount; i++)
	// {
	// port = physicalPorts.get(i);
	// involvedFilterCount = port.getFilterIndexList().size();
	// totalInvolvedFilterCount += involvedFilterCount;
	//
	// for(j=0; j<involvedFilterCount; j++)
	// {
	// filterDbIndex=OBCommon.makeFilterIndexAlteon(adcIndex,
	// port.getFilterIndexList().get(j));
	// sqlText += String.format(" (%d, %d, %s, %d ), ",
	// port.getPhysicalPortIndex(),
	// port.getFilterIndexList().get(j),
	// OBParser.sqlString(filterDbIndex),
	// adcIndex
	// );
	// }
	// }
	//
	// if(totalInvolvedFilterCount>0) //포트에 연관된 필터가 있으면 insert. 정상적으로 없을 수도 있다.
	// {
	// //마지막 comma를 제거한다.
	// if(sqlText.lastIndexOf( ',')>=0)
	// {
	// sqlText = sqlText.substring(0, sqlText.lastIndexOf( ','));
	// }
	// db.executeUpdate(sqlText);
	// }
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	public void addFilterAndPortMappingAlteon(Integer adcIndex, ArrayList<OBDtoFlbFilterInfo> filters,
			ArrayList<DtoSnmpFilterPortAlteon> physicalPorts, StringBuilder query) throws OBException {
		if (filters.size() == 0) {
			return;
		}

		query.append(" INSERT INTO TMP_FLB_FILTER ").append(
				" ( INDEX, ID, ADC_INDEX, STATE, NAME, SRC_IP, SRC_MASK, DST_IP, DST_MASK, PROTOCOL, SRC_PORT_FROM, SRC_PORT_TO, DST_PORT_FROM, DST_PORT_TO, ACTION, GROUP_ID, REDIRECTION )  ")
				.append(" VALUES ");

		final String delimiter = ", ";
		String prefix = "";

		for (OBDtoFlbFilterInfo filter : filters) {
			String filterDbIndex = OBCommon.makeFilterIndexAlteon(adcIndex, filter.getFilterId());

			query.append(prefix).append("(").append(OBParser.sqlString(filterDbIndex)).append(delimiter)
					.append(filter.getFilterId()).append(delimiter).append(adcIndex).append(delimiter)
					.append(filter.getState()).append(delimiter).append(OBParser.sqlString(filter.getName()))
					.append(delimiter).append(OBParser.sqlString(filter.getSrcIP())).append(delimiter)
					.append(OBParser.sqlString(filter.getSrcMask())).append(delimiter)
					.append(OBParser.sqlString(filter.getDstIP())).append(delimiter)
					.append(OBParser.sqlString(filter.getDstMask())).append(delimiter).append(filter.getProtocol())
					.append(delimiter).append(filter.getSrcPortFrom()).append(delimiter).append(filter.getSrcPortTo())
					.append(delimiter).append(filter.getDstPortFrom()).append(delimiter).append(filter.getDstPortTo())
					.append(delimiter).append(OBParser.sqlString(filter.getAction())).append(delimiter)
					.append(OBParser.sqlString(filter.getGroup())).append(delimiter)
					.append(OBParser.sqlString(filter.getRedirection())).append(")");

			prefix = ", ";
		}

		query.append(" ; ");

		prefix = "";
		boolean queryTrigger = true;

		for (DtoSnmpFilterPortAlteon port : physicalPorts) {
			ArrayList<Integer> filterList = port.getFilterIndexList();
			for (int filterIndex : filterList) {
				if (queryTrigger) {
					query.append(
							" INSERT INTO TMP_FLB_PORT_FILTER_MAP ( PORT_ALTEON_ID, FILTER_ALTEON_ID, FILTER_INDEX, ADC_INDEX ) ")
							.append(" VALUES ");
					queryTrigger = false;
				}

				String filterDbIndex = OBCommon.makeFilterIndexAlteon(adcIndex, filterIndex);

				query.append(prefix).append("(").append(port.getPhysicalPortIndex()).append(delimiter)
						.append(filterIndex).append(delimiter).append(OBParser.sqlString(filterDbIndex))
						.append(delimiter).append(adcIndex).append(")");

				prefix = ", ";
			}
		}

		if (!queryTrigger) {
			query.append(";");
		}
	}

	// //아래 함수 삭제 금지. 유효 redirection 필터 그룹 목록을 뽑는 함수인데, Alteon 26.x에서 1번 정보가 부정확하여
	// 쓸수 없게 됐다. 그러나 제대로 된 구현이므로 참고할 필요가 있다.
	// // 1. filter enabled 된 포트에 매핑된 필터들 구함
	// // 2. 1의 필터들 중 enabled 상태이고 action이 redirect인 필터들 고름
	// // 3. 2의 필터들에 할당된 그룹의 목록을 뽑음
	// public ArrayList<OBDtoFlbGroupMonitorInfo> getFlbGroupMonitorInfoOriginal(int
	// adcIndex, OBDatabase db) throws OBException
	// {
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex =
	// %d", adcIndex));
	//
	// ArrayList<OBDtoFlbGroupMonitorInfo> resultGroups = new
	// ArrayList<OBDtoFlbGroupMonitorInfo>();
	// String sqlText="";
	// ResultSet rs;
	// try
	// {
	// sqlText = String.format(
	// " SELECT G.INDEX GROUP_INDEX, G.ALTEON_ID, G.NAME GROUP_NAME, FG.GROUP_INDEX
	// GROUP_INDEX2, \n" +
	// " FILTER.INDEX FILTER_INDEX, FILTER.ID FILTER_ID, FILTER.NAME FILTER_NAME,
	// P.CONN_CURR \n" +
	// " FROM (SELECT INDEX, ALTEON_ID, NAME FROM TMP_SLB_POOL WHERE ADC_INDEX = %d)
	// G \n" +
	// " INNER JOIN (SELECT GROUP_ID, INDEX, ID, NAME \n" +
	// " FROM TMP_FLB_FILTER \n" +
	// " WHERE ADC_INDEX = %d AND STATE = %d AND ACTION = 'redirect' \n" +
	// " AND INDEX IN (SELECT DISTINCT(FILTER_INDEX) \n" +
	// " FROM TMP_FLB_PORT_FILTER_MAP \n" +
	// " WHERE ADC_INDEX = %d) \n" +
	// " ) FILTER \n" +
	// " ON CAST(G.ALTEON_ID AS VARCHAR) = FILTER.GROUP_ID \n" +
	// " LEFT JOIN (SELECT GROUP_INDEX FROM MNG_FLB_GROUP WHERE ADC_INDEX = %d) FG
	// \n" +
	// " ON G.INDEX = FG.GROUP_INDEX \n" +
	// " LEFT JOIN (SELECT GROUP_INDEX, CONN_CURR FROM TMP_POOLGROUP_PERF_STATS
	// WHERE ADC_INDEX = %d) P \n" +
	// " ON G.INDEX = P.GROUP_INDEX \n" +
	// " ORDER BY FG.GROUP_INDEX DESC, G.ALTEON_ID, FILTER.ID "
	// , adcIndex, adcIndex, OBDefine.STATE_ENABLE, adcIndex, adcIndex, adcIndex);
	// rs = db.executeQuery(sqlText);
	//
	// //1개 그룹당 filter가 복수일 수 있는데, DB query를 loop안에서 돌리지 않으려고 filter 단위로 뽑고, group으로
	// sorting했다.
	// //group을 순환하면서 group단위로 filter들을 묶어서 ArrayList로 만들어야 한다.
	// String currentGroupIndex = "EMPTY";
	// String newGroupIndex = "EMPTY";
	// ArrayList<String> groupIdList = new ArrayList<String>();
	//
	// ArrayList<OBDtoFlbFilterSummary> currentFilterList = null;
	// OBDtoFlbGroupMonitorInfo newGroup=null;
	//
	// while(rs.next())
	// {
	// newGroupIndex = (db.getString(rs, "GROUP_INDEX"));
	//
	// if(newGroupIndex.equals(currentGroupIndex)==false) //새 그룹이다.
	// {
	// newGroup = new OBDtoFlbGroupMonitorInfo(); //새 그룹을 만든다.
	// newGroup.setDbIndex(newGroupIndex);
	// newGroup.setGroupId(db.getString(rs, "ALTEON_ID"));
	// if(db.getString(rs, "GROUP_INDEX2")==null)
	// {
	// newGroup.setIsMonitoringOn(OBDefine.STATE_DISABLE);
	// }
	// else
	// {
	// newGroup.setIsMonitoringOn(OBDefine.STATE_ENABLE);
	// }
	// newGroup.setCurrentConnection(db.getLong(rs, "CONN_CURR"));
	// newGroup.setFilterList(new ArrayList<OBDtoFlbFilterSummary>()); //빈 filter
	// list 만들어 둠
	// newGroup.setRealList(new ArrayList<OBDtoAdcNodeAlteon>()); //빈 real list 만들어둠
	//
	// currentFilterList = newGroup.getFilterList();
	// resultGroups.add(newGroup);
	// groupIdList.add(newGroupIndex); //group index들을 모았다가 나중에 그룹 소속 real을 구할 때 쓴다.
	// currentGroupIndex = newGroupIndex;
	// }
	//
	// OBDtoFlbFilterSummary newFilter = new OBDtoFlbFilterSummary();
	//
	// newFilter.setDbIndex(db.getString(rs, "FILTER_INDEX"));
	// newFilter.setFilterId(db.getInteger(rs, "FILTER_ID"));
	// newFilter.setName(db.getString(rs, "FILTER_NAME"));
	//
	// if(currentFilterList!=null)
	// {
	// currentFilterList.add(newFilter);
	// }
	// }
	//
	// if(groupIdList!=null && groupIdList.size()>0)
	// {
	// String groupIds = OBParser.convertList2SingleQuotedString(groupIdList);
	// sqlText = String.format(
	// " SELECT G.POOL_INDEX, R.INDEX REAL_DBINDEX, R.ALTEON_ID REAL_ID,
	// R.IP_ADDRESS \n" +
	// " FROM (SELECT POOL_INDEX, NODE_INDEX FROM TMP_SLB_POOLMEMBER WHERE
	// POOL_INDEX IN (%s)) G \n" +
	// " LEFT JOIN (SELECT INDEX, ALTEON_ID, IP_ADDRESS FROM TMP_SLB_NODE WHERE
	// ADC_INDEX = %d) R \n" +
	// " ON G.NODE_INDEX = R.INDEX \n" +
	// " ORDER BY G.POOL_INDEX, REAL_ID ",
	// groupIds, adcIndex);
	// rs = db.executeQuery(sqlText);
	//
	// String groupIndex = null;
	// OBDtoAdcNodeAlteon newReal = null;
	// int i=0, foundGroupIndex = 0;
	// int groupNum=resultGroups.size();
	// while(rs.next())
	// {
	// groupIndex = db.getString(rs, "POOL_INDEX");
	// foundGroupIndex = -1; //그룹이 없다고 초기값 설정
	//
	// for(i=0; i<groupNum; i++)
	// {
	// if(resultGroups.get(i).getDbIndex().equals(groupIndex)==true)
	// {
	// foundGroupIndex = i;
	// break;
	// }
	// }
	// if(foundGroupIndex>=0) //그룹이 목록에 있다.
	// {
	// newReal = new OBDtoAdcNodeAlteon(); //새 real을 만든다.
	// newReal.setIndex(db.getString(rs, "REAL_DBINDEX")); //real DB index
	// newReal.setIpAddress(db.getString(rs, "IP_ADDRESS"));
	// newReal.setAlteonId(db.getInteger(rs, "REAL_ID"));
	// resultGroups.get(foundGroupIndex).getRealList().add(newReal);
	// }
	// }
	// }
	//
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. resultGroup
	// count = %d", resultGroups.size()));
	// return resultGroups;
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// 위 getFlbGroupMonitorInfoOriginal()을 쓸 수 없어 대안으로 구성한 함수.
	// 원래 유효한 redirection group을 구하는 규칙은 다음과 같다.
	// 1. filter enabled 된 포트에 매핑된 필터들 구함
	// 2. 1의 필터들 중 enabled 상태이고 action이 redirect인 필터들 고름
	// 3. 2의 필터들에 할당된 그룹의 목록을 뽑음
	// 그런데 1번 정보가 Alteon 26.x에서 부정확하므로 다음과 같이 느슨하게 재구성할 수 밖에 없다.
	// 1. 모든 필터들 중 enabled 상태이고, action이 redirect인 필터들
	// 2. 1의 필터들에 할당된 그룹의 목록 뽑음
	ArrayList<OBDtoFlbGroupMonitorInfo> getFlbGroupMonitorInfo(int adcIndex, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex = %d", adcIndex));

		ArrayList<OBDtoFlbGroupMonitorInfo> resultGroups = new ArrayList<OBDtoFlbGroupMonitorInfo>();
		String sqlText = "";
		ResultSet rs;
		try {
			sqlText = String.format(
					" SELECT G.INDEX GROUP_INDEX, G.ALTEON_ID, G.NAME GROUP_NAME, FG.GROUP_INDEX GROUP_INDEX2,   \n"
							+ "     FILTER.INDEX FILTER_INDEX, FILTER.ID FILTER_ID, FILTER.NAME FILTER_NAME, P.CONN_CURR   \n"
							+ " FROM (SELECT INDEX, ALTEON_ID, NAME FROM TMP_SLB_POOL WHERE ADC_INDEX = %d) G              \n"
							+ " INNER JOIN (SELECT GROUP_ID, INDEX, ID, NAME                                               \n"
							+ "             FROM TMP_FLB_FILTER                                                            \n"
							+ "             WHERE ADC_INDEX = %d AND STATE = %d AND ACTION = 'redirect'                    \n"
							+ "            ) FILTER                                                                        \n"
							+ " ON CAST(G.ALTEON_ID AS VARCHAR) = FILTER.GROUP_ID                                          \n"
							+ " LEFT JOIN (SELECT GROUP_INDEX FROM MNG_FLB_GROUP WHERE ADC_INDEX = %d) FG                  \n"
							+ " ON G.INDEX = FG.GROUP_INDEX                                                                \n"
							+ " LEFT JOIN (SELECT GROUP_INDEX, CONN_CURR FROM TMP_POOLGROUP_PERF_STATS WHERE ADC_INDEX = %d) P \n"
							+ " ON G.INDEX = P.GROUP_INDEX                                                                 \n"
							+ " ORDER BY FG.GROUP_INDEX DESC, G.ALTEON_ID, FILTER.ID                                       ",
					adcIndex, adcIndex, OBDefine.STATE_ENABLE, adcIndex, adcIndex, adcIndex);
			rs = db.executeQuery(sqlText);

			// 1개 그룹당 filter가 복수일 수 있는데, DB query를 loop안에서 돌리지 않으려고 filter 단위로 뽑고, group으로
			// sorting했다.
			// group을 순환하면서 group단위로 filter들을 묶어서 ArrayList로 만들어야 한다.
			String currentGroupIndex = "EMPTY";
			String newGroupIndex = "EMPTY";
			ArrayList<String> groupIdList = new ArrayList<String>();

			ArrayList<OBDtoFlbFilterSummary> currentFilterList = null;
			OBDtoFlbGroupMonitorInfo newGroup = null;

			while (rs.next()) {
				newGroupIndex = (db.getString(rs, "GROUP_INDEX"));

				if (newGroupIndex.equals(currentGroupIndex) == false) // 새 그룹이다.
				{
					newGroup = new OBDtoFlbGroupMonitorInfo(); // 새 그룹을 만든다.
					newGroup.setDbIndex(newGroupIndex);
					newGroup.setGroupId(db.getString(rs, "ALTEON_ID"));
					if (db.getString(rs, "GROUP_INDEX2") == null) {
						newGroup.setIsMonitoringOn(OBDefine.STATE_DISABLE);
					} else {
						newGroup.setIsMonitoringOn(OBDefine.STATE_ENABLE);
					}
					newGroup.setCurrentConnection(db.getLong(rs, "CONN_CURR"));
					newGroup.setFilterList(new ArrayList<OBDtoFlbFilterSummary>()); // 빈 filter list 만들어 둠
					newGroup.setRealList(new ArrayList<OBDtoAdcNodeAlteon>()); // 빈 real list 만들어둠

					currentFilterList = newGroup.getFilterList();
					resultGroups.add(newGroup);
					groupIdList.add(newGroupIndex); // group index들을 모았다가 나중에 그룹 소속 real을 구할 때 쓴다.
					currentGroupIndex = newGroupIndex;
				}

				OBDtoFlbFilterSummary newFilter = new OBDtoFlbFilterSummary();

				newFilter.setDbIndex(db.getString(rs, "FILTER_INDEX"));
				newFilter.setFilterId(db.getInteger(rs, "FILTER_ID"));
				newFilter.setName(db.getString(rs, "FILTER_NAME"));

				if (currentFilterList != null) {
					currentFilterList.add(newFilter);
				}
			}

			if (groupIdList != null && groupIdList.size() > 0) {
				String groupIds = OBParser.convertList2SingleQuotedString(groupIdList);
				sqlText = String.format(
						" SELECT G.POOL_INDEX, R.INDEX REAL_DBINDEX, R.ALTEON_ID REAL_ID, R.IP_ADDRESS             \n"
								+ " FROM (SELECT POOL_INDEX, NODE_INDEX FROM TMP_SLB_POOLMEMBER WHERE POOL_INDEX IN (%s)) G  \n"
								+ " LEFT JOIN (SELECT INDEX, ALTEON_ID, IP_ADDRESS FROM TMP_SLB_NODE WHERE ADC_INDEX = %d) R \n"
								+ " ON G.NODE_INDEX = R.INDEX                                                                \n"
								+ " ORDER BY G.POOL_INDEX, REAL_ID                                                           ",
						groupIds, adcIndex);
				rs = db.executeQuery(sqlText);

				String groupIndex = null;
				OBDtoAdcNodeAlteon newReal = null;
				int i = 0, foundGroupIndex = 0;
				int groupNum = resultGroups.size();
				while (rs.next()) {
					groupIndex = db.getString(rs, "POOL_INDEX");
					foundGroupIndex = -1; // 그룹이 없다고 초기값 설정

					for (i = 0; i < groupNum; i++) {
						if (resultGroups.get(i).getDbIndex().equals(groupIndex) == true) {
							foundGroupIndex = i;
							break;
						}
					}
					if (foundGroupIndex >= 0) // 그룹이 목록에 있다.
					{
						newReal = new OBDtoAdcNodeAlteon(); // 새 real을 만든다.
						newReal.setIndex(db.getString(rs, "REAL_DBINDEX")); // real DB index
						newReal.setIpAddress(db.getString(rs, "IP_ADDRESS"));
						newReal.setAlteonId(db.getString(rs, "REAL_ID"));
						resultGroups.get(foundGroupIndex).getRealList().add(newReal);
					}
				}
			}

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. resultGroup count = %d", resultGroups.size()));
			return resultGroups;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	void setFlbGroupMonitorOn(Integer adcIndex, ArrayList<String> selectedGroupIndexList) throws OBException {
		String sqlText = "";
		String sqlInsert = ""; // list에 아무것도 없을 경우 WHERE-IN clause 구문 오류가 나는 것을 방지하려고 기능에 영향을 미치지 않는 기본항을 하나
								// 집어넣는다. ??
		// indexList = OBParser.convertList2SingleQuotedString(selectedGroupIndexList);
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();
			sqlText = String.format(" DELETE FROM MNG_FLB_GROUP WHERE ADC_INDEX = %d; ", adcIndex);
			db.executeUpdate(sqlText);

			if (selectedGroupIndexList != null && selectedGroupIndexList.size() > 0) {
				sqlInsert = String.format(" INSERT INTO MNG_FLB_GROUP (ADC_INDEX, GROUP_INDEX) " + " VALUES ");

				for (String groupIndex : selectedGroupIndexList) {
					sqlInsert += String.format("(%d, %s), ", adcIndex, OBParser.sqlString(groupIndex));
				}
				// 마지막 comma를 제거한다.
				if (sqlInsert.lastIndexOf(',') >= 0) {
					sqlInsert = sqlInsert.substring(0, sqlInsert.lastIndexOf(','));
				}
				sqlText += sqlInsert;
			}
			db.executeUpdate(sqlText);
			new OBAdcManagementImpl().setAdcRoleFlb(adcIndex, db); // flb 그룹을 설정했으면, adc의 role파트 중 FLB 속성을 업데이트 한다.
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	// public ArrayList<OBDtoAdcPoolSimple> getFlbGroupSelected(int adcIndex) throws
	// OBException
	// {
	// OBDatabase db = new OBDatabase();
	// ArrayList<OBDtoAdcPoolSimple> result = new ArrayList<OBDtoAdcPoolSimple>();
	//
	// try
	// {
	// db.openDB();
	// result = getFlbGroupSelected(adcIndex, db);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// return result;
	// }

	// public ArrayList<OBDtoAdcPoolSimple> getFlbGroupSelected(int adcIndex,
	// OBDatabase db) throws OBException
	// {
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex =
	// %d", adcIndex));
	//
	// ArrayList<OBDtoAdcPoolSimple> resultGroups = new
	// ArrayList<OBDtoAdcPoolSimple>();
	// String sqlText="";
	// ResultSet rs;
	// try
	// {
	// sqlText = String.format(
	// " SELECT POOL.INDEX, POOL.ALTEON_ID, POOL.NAME, GR.GROUP_INDEX " +
	// " FROM (SELECT INDEX, ALTEON_ID, NAME FROM TMP_SLB_POOL WHERE ADC_INDEX = %d)
	// POOL " +
	// " LEFT JOIN MNG_FLB_GROUP GR " +
	// " ON POOL.INDEX = GR.GROUP_INDEX " +
	// " ORDER BY POOL.INDEX "
	// , adcIndex);
	//
	// rs = db.executeQuery(sqlText);
	//
	// while(rs.next())
	// {
	// OBDtoAdcPoolSimple group = new OBDtoAdcPoolSimple();
	// group = new OBDtoAdcPoolSimple();
	// group.setDbIndex(db.getString(rs, "INDEX"));
	// group.setAdcIndex(adcIndex);
	// group.setAlteonId(db.getString(rs, "ALTEON_ID"));
	// group.setName(db.getString(rs, "NAME"));
	// if(db.getString(rs, "GROUP_INDEX")==null)
	// {
	// group.setFlbMonitorOn(OBDefine.STATE_DISABLE);
	// }
	// else
	// {
	// group.setFlbMonitorOn(OBDefine.STATE_ENABLE);
	// }
	// resultGroups.add(group);
	// }
	//
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. resultGroup
	// count = %d", resultGroups.size()));
	// return resultGroups;
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public ArrayList<OBDtoAdcPoolSimple> getFlbGroupSelected(int adcIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex = %d", adcIndex));

		ArrayList<OBDtoAdcPoolSimple> resultGroups = new ArrayList<OBDtoAdcPoolSimple>();
		String sqlText = "";
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" SELECT POOL.INDEX, POOL.ALTEON_ID, POOL.NAME, GR.GROUP_INDEX "
					+ " FROM (SELECT INDEX, ALTEON_ID, NAME FROM TMP_SLB_POOL WHERE ADC_INDEX = %d) POOL "
					+ " LEFT JOIN MNG_FLB_GROUP GR " + " ON POOL.INDEX = GR.GROUP_INDEX " + " ORDER BY POOL.INDEX ",
					adcIndex);

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcPoolSimple group = new OBDtoAdcPoolSimple();
				group = new OBDtoAdcPoolSimple();
				group.setDbIndex(db.getString(rs, "INDEX"));
				group.setAdcIndex(adcIndex);
				group.setAlteonId(db.getString(rs, "ALTEON_ID"));
				group.setName(db.getString(rs, "NAME"));
				if (db.getString(rs, "GROUP_INDEX") == null) {
					group.setFlbMonitorOn(OBDefine.STATE_DISABLE);
				} else {
					group.setFlbMonitorOn(OBDefine.STATE_ENABLE);
				}
				resultGroups.add(group);
			}

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. resultGroup count = %d", resultGroups.size()));
			return resultGroups;
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

	// FLB 모니터링한다고 선택한 group의 real 정보를 가져온다.
	public ArrayList<OBDtoAdcNodeSimple> getRealInFlbSelectedGroups(int adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoAdcNodeSimple> result = new ArrayList<OBDtoAdcNodeSimple>();

		try {
			db.openDB();
			result = getRealInFlbSelectedGroups(adcIndex, db);
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

	private ArrayList<OBDtoAdcNodeSimple> getRealInFlbSelectedGroups(int adcIndex, OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex = %d", adcIndex));

		ArrayList<OBDtoAdcNodeSimple> resultReals = new ArrayList<OBDtoAdcNodeSimple>();
		String sqlText = "";
		ResultSet rs;
		try {
			sqlText = String.format(" SELECT INDEX, ALTEON_ID, NAME FROM TMP_SLB_NODE "
					+ " WHERE INDEX IN (                                "
					+ "     SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER   "
					+ "     WHERE POOL_INDEX IN (                       "
					+ "         SELECT GROUP_INDEX FROM MNG_FLB_GROUP   "
					+ "         WHERE ADC_INDEX = %d                    "
					+ "     )                                           "
					+ " )                                               " + " ORDER BY ALTEON_ID ", adcIndex);

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcNodeSimple real = new OBDtoAdcNodeSimple();
				real = new OBDtoAdcNodeSimple();
				real.setDbIndex(db.getString(rs, "INDEX"));
				real.setAdcIndex(adcIndex);
				real.setAlteonId(db.getString(rs, "ALTEON_ID"));
				real.setName(db.getString(rs, "NAME"));
				resultReals.add(real);
			}
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. resultGroup count = %d", resultReals.size()));
			return resultReals;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// public void delVServiceAll(Integer adcIndex, OBDatabase db) throws
	// OBException
	// {
	// delAdcConfigAll(adcIndex, "TMP_SLB_VS_SERVICE", db);
	// }

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void delVServiceAll(Integer adcIndex, StringBuilder query) {
		delTableAll(adcIndex, "TMP_SLB_VS_SERVICE", query);
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public void delVServiceAll(Integer adcIndex) throws OBException {
		delTableAll(adcIndex, "TMP_SLB_VS_SERVICE");
	}

	// public void delVServiceAll(Integer adcIndex) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// try
	// {
	// db.openDB();
	// delAdcConfigAll(adcIndex, "TMP_SLB_VS_SERVICE", db);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// }

	/**
	 * DB에서 특정 adc의 모든 node목록을 구한다.
	 * 
	 * @param adcIndex
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	// public ArrayList<OBDtoAdcNodeAlteon> getNodeListAllAlteon(Integer adcIndex)
	// throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// ArrayList<OBDtoAdcNodeAlteon> list;
	//
	// try
	// {
	// db.openDB();
	// list = getNodeListAllAlteon(adcIndex, db);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// return list;
	// }

	/**
	 * DB에서 특정 adc의 모든 node목록을 구한다.
	 * 
	 * @param adcIndex
	 * @param db
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	private ArrayList<OBDtoAdcNodeAlteon> getNodeListAllAlteon(Integer adcIndex) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX, ALTEON_ID, IP_ADDRESS, NAME, STATE, STATUS, EXTRA "
					+ " FROM TMP_SLB_NODE " + " WHERE ADC_INDEX = %d " + " ORDER BY IP_ADDRESS ;", adcIndex);

			ResultSet rs = db.executeQuery(sqlText);

			ArrayList<OBDtoAdcNodeAlteon> list = new ArrayList<OBDtoAdcNodeAlteon>();
			while (rs.next()) {
				OBDtoAdcNodeAlteon obj = new OBDtoAdcNodeAlteon();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setAlteonId(db.getString(rs, "ALTEON_ID"));
				obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setState(db.getInteger(rs, "STATE"));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				obj.setExtra(db.getString(rs, "EXTRA"));
				list.add(obj);
			}
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

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	// db.openDB();
	// db2.openDB();
	// ArrayList<OBDtoAdcNodePAS> list = new OBVServerDB().getNodeListAllPAS(5);
	// System.out.println(list);
	// db.closeDB();
	// db2.closeDB();
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	// public ArrayList<OBDtoAdcNodePAS> getNodeListAllPAS(Integer adcIndex) throws
	// OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// ArrayList<OBDtoAdcNodePAS> list;
	//
	// try
	// {
	// db.openDB();
	// list = getNodeListAllPAS(adcIndex, db);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// return list;
	// }

	// public ArrayList<OBDtoAdcNodePAS> getNodeListAllPAS(Integer adcIndex,
	// OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(" SELECT INDEX, ALTEON_ID, NAME, IP_ADDRESS, STATE "
	// +
	// " FROM TMP_SLB_NODE " +
	// " WHERE ADC_INDEX = %d " +
	// " ORDER BY IP_ADDRESS ;",
	// adcIndex);
	//
	// ResultSet rs = db.executeQuery(sqlText);
	//
	// ArrayList<OBDtoAdcNodePAS> list = new ArrayList<OBDtoAdcNodePAS>();
	// while(rs.next())
	// {
	// OBDtoAdcNodePAS obj = new OBDtoAdcNodePAS();
	// obj.setDbIndex(db.getString(rs, "INDEX"));
	// obj.setId(db.getInteger(rs, "ALTEON_ID"));
	// obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
	// obj.setState(db.getInteger(rs, "STATE"));
	// obj.setName(db.getString(rs, "NAME"));
	// list.add(obj);
	// }
	// return list;
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// public ArrayList<OBDtoAdcNodeF5> getNodeListAllF5(Integer adcIndex) throws
	// OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// ArrayList<OBDtoAdcNodeF5> list;
	//
	// try
	// {
	// db.openDB();
	// list = getNodeListAllF5(adcIndex, db);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// return list;
	// }

	/**
	 * DB에서 특정 adc의 모든 node목록을 구한다.
	 * 
	 * @param adcIndex
	 * @param db
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	// public ArrayList<OBDtoAdcNodeF5> getNodeListAllF5(Integer adcIndex,
	// OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(" SELECT INDEX, IP_ADDRESS, STATE, NAME, RATIO " +
	// " FROM TMP_SLB_NODE " +
	// " WHERE ADC_INDEX = %d " +
	// " ORDER BY IP_ADDRESS ;",
	// adcIndex);
	//
	// ResultSet rs = db.executeQuery(sqlText);
	//
	// ArrayList<OBDtoAdcNodeF5> list = new ArrayList<OBDtoAdcNodeF5>();
	// while(rs.next())
	// {
	// OBDtoAdcNodeF5 obj = new OBDtoAdcNodeF5();
	// obj.setIndex(db.getString(rs, "INDEX"));
	// obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
	// obj.setState(db.getInteger(rs, "STATE"));
	// obj.setName(db.getString(rs, "NAME"));
	// obj.setRatio(db.getInteger(rs, "RATIO"));
	// list.add(obj);
	// }
	// return list;
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	/**
	 * 주어진 pool에 member로 속하지 않은 node목록을 DB에서 가져온다.
	 * 
	 * @param adcIndex -- ADC 장비 index
	 * @param poolName -- 이 pool에 소속된 node(member)를 제외한다.
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public ArrayList<OBDtoAdcNodeAlteon> getNodeAvailableListAlteon(Integer adcIndex, String poolIndex)
			throws OBException {
		OBDatabase db = new OBDatabase();

		ArrayList<OBDtoAdcNodeAlteon> list;
		try {
			db.openDB();
			list = getNodeAvailableListAlteon(adcIndex, poolIndex, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return list;
	}

	/**
	 * 주어진 pool에 member로 속하지 않은 node목록을 DB에서 가져온다.
	 * 
	 * @param adcIndex -- ADC 장비 index
	 * @param poolName -- 이 pool에 소속된 node(member)를 제외한다.
	 * @param db       -- DB 인스턴스.
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	private ArrayList<OBDtoAdcNodeAlteon> getNodeAvailableListAlteon(Integer adcIndex, String poolIndex, OBDatabase db)
			throws OBException {
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d,
		// poolIndex:%s", adcIndex, poolIndex));
		String sqlText = "";
		ResultSet rs;
		ArrayList<OBDtoAdcNodeAlteon> list = new ArrayList<OBDtoAdcNodeAlteon>();

		try {
			sqlText = String.format(" SELECT INDEX, ALTEON_ID, IP_ADDRESS, NAME, STATE, EXTRA \n"
					+ " FROM TMP_SLB_NODE                                       \n"
					+ " WHERE ADC_INDEX=%d                                      \n"
					+ " AND INDEX NOT IN (                                      \n" + // where-in:empty string 불가, null
																						// 불가, OK
					"             SELECT NODE_INDEX                           \n"
					+ "             FROM TMP_SLB_POOLMEMBER                     \n"
					+ "             WHERE POOL_INDEX=%s                         \n"
					+ "             )                                           \n"
					+ " ORDER BY IP_ADDRESS ASC;                                \n", adcIndex,
					OBParser.sqlString(poolIndex));

			rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcNodeAlteon obj = new OBDtoAdcNodeAlteon();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setAlteonId(db.getString(rs, "ALTEON_ID"));
				obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setState(db.getInteger(rs, "STATE"));
				obj.setExtra(db.getString(rs, "EXTRA"));
				list.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
		// list));

		return list;
	}

	// public HashMap<String, Integer> getAvailableNodeIDsAlteon(Integer adcIndex,
	// OBDatabase db) throws OBException
	// {
	// // OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start.
	// adcIndex:%d, poolIndex:%s", adcIndex, poolIndex));
	// String sqlText="";
	// ResultSet rs;
	// HashMap<String, Integer> result = new HashMap<String, Integer>();
	//
	// try
	// {
	// sqlText = String.format(" SELECT ALTEON_ID, IP_ADDRESS " +
	// " FROM TMP_SLB_NODE " +
	// " WHERE ADC_INDEX=%d " +
	// " ORDER BY IP_ADDRESS ASC;",
	// adcIndex);
	//
	// rs = db.executeQuery(sqlText);
	// while(rs.next())
	// {
	// result.put(db.getString(rs, "IP_ADDRESS"), db.getInteger(rs, "ALTEON_ID"));
	// }
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// // OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
	// list));
	//
	// return result;
	// }

	// public HashMap<String, Integer> getAvailableNodeIDsPAS(Integer adcIndex,
	// String vsName, OBDatabase db) throws OBException
	// {
	// // OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start.
	// adcIndex:%d, poolIndex:%s", adcIndex, poolIndex));
	// String sqlText="";
	// ResultSet rs;
	// HashMap<String, Integer> result = new HashMap<String, Integer>();
	//
	// try
	// {
	// sqlText = String.format(" SELECT ALTEON_ID, IP_ADDRESS " +
	// " FROM TMP_SLB_NODE " +
	// " WHERE ADC_INDEX=%d " +
	// " AND NAME=%s " +
	// " ORDER BY IP_ADDRESS ASC;",
	// adcIndex,
	// OBParser.sqlString(vsName));
	//
	// rs = db.executeQuery(sqlText);
	// while(rs.next())
	// {
	// result.put(db.getString(rs, "IP_ADDRESS"), db.getInteger(rs, "ALTEON_ID"));
	// }
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// // OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
	// list));
	//
	// return result;
	// }

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	// db.openDB();
	// db2.openDB();
	// ArrayList<OBDtoAdcNodePAS> list = new
	// OBVServerDB().getNodeAvailableListPAS(5, "5_bwpark");
	// System.out.println(list);
	// db.closeDB();
	// db2.closeDB();
	// }
	// // catch(SQLException e)
	// // {
	// //
	// // }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	public ArrayList<OBDtoAdcNodePAS> getNodeAvailableListPAS(Integer adcIndex, String poolIndex) throws OBException {
		OBDatabase db = new OBDatabase();

		ArrayList<OBDtoAdcNodePAS> list;
		try {
			db.openDB();
			list = getNodeAvailableListPAS(adcIndex, poolIndex, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return list;
	}

	private ArrayList<OBDtoAdcNodePAS> getNodeAvailableListPAS(Integer adcIndex, String poolIndex, OBDatabase db)
			throws OBException {
		String sqlText = "";
		ResultSet rs;
		ArrayList<OBDtoAdcNodePAS> list = new ArrayList<OBDtoAdcNodePAS>();

		try {
			// sqlText = String.format(
			// " SELECT INDEX, IP_ADDRESS, STATE, NAME, ALTEON_ID " +
			// " FROM TMP_SLB_NODE " +
			// " WHERE ADC_INDEX=%d " +
			// " ORDER BY IP_ADDRESS ASC;",
			// adcIndex);
			// while(rs.next())
			// {
			// OBDtoAdcNodePAS obj = new OBDtoAdcNodePAS();
			// obj.setDbIndex(db.getString(rs, "INDEX"));
			// obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
			// obj.setState(db.getInteger(rs, "STATE"));
			// obj.setName(db.getString(rs, "NAME"));
			// obj.setId(db.getInteger(rs, "ALTEON_ID"));
			//
			// list.add(obj);
			// }
			// pool에 이미 들어간 node는 빼야 한다. 위 원본을 아래와 같이 수정한다.
			sqlText = String.format(" SELECT DISTINCT(IP_ADDRESS)              \n"
					+ " FROM TMP_SLB_NODE                        \n" + " WHERE ADC_INDEX = %d                     \n"
					+ "     AND IP_ADDRESS NOT IN (              \n" + "         SELECT IP_ADDRESS                \n"
					+ "         FROM TMP_SLB_NODE                \n" + "         WHERE INDEX IN (                 \n"
					+ "             SELECT NODE_INDEX            \n" + "             FROM TMP_SLB_POOLMEMBER      \n"
					+ "             WHERE POOL_INDEX = %s        \n" + "         )                                \n"
					+ "    )                                     ", adcIndex, OBParser.sqlString(poolIndex));

			rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcNodePAS obj = new OBDtoAdcNodePAS();
				obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				obj.setDbIndex(""); // 필요없는 값인데 null이면 웹에서 에러가 나므로 초기화는 해 준다. 아래 3 멤버도 같은 이유로
				obj.setState(0);
				obj.setName("");
				obj.setId(0);

				list.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return list;
	}

	public ArrayList<OBDtoAdcNodePASK> getNodeAvailableListPASK(Integer adcIndex, String poolIndex) throws OBException {
		OBDatabase db = new OBDatabase();

		ArrayList<OBDtoAdcNodePASK> list;
		try {
			db.openDB();
			list = getNodeAvailableListPASK(adcIndex, poolIndex, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return list;
	}

	private ArrayList<OBDtoAdcNodePASK> getNodeAvailableListPASK(Integer adcIndex, String poolIndex, OBDatabase db)
			throws OBException {
		String sqlText = "";
		ResultSet rs;
		ArrayList<OBDtoAdcNodePASK> list = new ArrayList<OBDtoAdcNodePASK>();

		try {
			// pool에 이미 들어간 node는 빼야 한다. 위 원본을 아래와 같이 수정한다.
			sqlText = String.format(
					" SELECT INDEX, ALTEON_ID, IP_ADDRESS, NAME, STATE, PORT  \n"
							+ " FROM TMP_SLB_NODE                                 \n"
							+ " WHERE ADC_INDEX = %d                              \n"
							+ "     AND INDEX NOT IN (                            \n"
							+ "         SELECT NODE_INDEX                         \n"
							+ "         FROM TMP_SLB_POOLMEMBER                   \n"
							+ "         WHERE POOL_INDEX = %s                     \n"
							+ "    )                                              \n",
					adcIndex, OBParser.sqlString(poolIndex));

			rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcNodePASK obj = new OBDtoAdcNodePASK();
				obj.setDbIndex(db.getString(rs, "INDEX"));
				obj.setId(db.getInteger(rs, "ALTEON_ID"));
				obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setState(db.getInteger(rs, "STATE"));
				obj.setPort(db.getInteger(rs, "PORT"));
				list.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return list;
	}

	public ArrayList<OBDtoAdcNodeF5> getNodeAvailableListF5(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();

		ArrayList<OBDtoAdcNodeF5> list;
		try {
			db.openDB();
			list = getNodeAvailableListF5(adcIndex, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return list;
	}

	private ArrayList<OBDtoAdcNodeF5> getNodeAvailableListF5(Integer adcIndex, OBDatabase db) throws OBException {
		String sqlText = "";
		ResultSet rs;
		ArrayList<OBDtoAdcNodeF5> list = new ArrayList<OBDtoAdcNodeF5>();

		try {
			// pool에 이미 들어간 node는 빼야할 것 같지만 F5는 port가 다르면 member로 다시 들어올 수 있으므로 전부 뽑는다.
			sqlText = String.format(" SELECT INDEX, IP_ADDRESS, STATE, NAME, RATIO " + " FROM TMP_SLB_NODE "
					+ " WHERE ADC_INDEX=%d " + " ORDER BY INET(IP_ADDRESS) ASC;", adcIndex);

			rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcNodeF5 obj = new OBDtoAdcNodeF5();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				obj.setState(db.getInteger(rs, "STATE"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setRatio(db.getInteger(rs, "RATIO"));
				list.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return list;
	}

	// 노드 전체 목록을 가져온다. 페이징, search key
	// pool에 이미 들어간 node는 빼야할 것 같지만 F5는 port가 다르면 member로 다시 들어올 수 있으므로 전부 뽑는다.
	public ArrayList<OBDtoAdcNodeDetail> searchNodeListF5(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		ResultSet rs;
		ArrayList<OBDtoAdcNodeDetail> list = new ArrayList<OBDtoAdcNodeDetail>();
		ArrayList<String> vsOwnList = null; // 계정이 소유한 vs list

		try {
			db.openDB();
			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accntIndex);

			if (roleNo.equals(OBDefine.ACCNT_ROLE_VSADMIN) || roleNo.equals(OBDefine.ACCNT_ROLE_RSADMIN)) // vs admin,
																											// 할당된 vs를
																											// 구해놓는다.
			{
				vsOwnList = new OBAccountImpl().getAssignedVSList(adcIndex, accntIndex);
			}

			int offset = 0;
			if (beginIndex != null && beginIndex.intValue() >= 0) {
				offset = beginIndex.intValue();
			}

			int limit = 0;
			String sqlLimit = "";
			if (endIndex != null && endIndex.intValue() >= 0) {
				limit = Math.abs(endIndex.intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}

			String sqlWhere = "";
			if (searchKey == null) {
				searchKey = "";
			} else if (searchKey.isEmpty() == false) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlWhere = String.format(" AND (NAME LIKE %s OR IP_ADDRESS LIKE %s) \n",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			// sqlText = String.format(" SELECT * FROM TMP_SLB_NODE WHERE ADC_INDEX = %d ",
			// adcIndex);
			// sqlText = String.format(
			// " SELECT * FROM TMP_SLB_NODE WHERE ADC_INDEX = %d \n" +
			// " AND INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER \n" +
			// " WHERE ADC_INDEX = %d) ",
			// adcIndex, adcIndex);

			if (roleNo == OBDefine.ACCNT_ROLE_VSADMIN || roleNo == OBDefine.ACCNT_ROLE_RSADMIN) {
				ArrayList<String> vsList = new OBAccountImpl().getAssignedVSList(adcIndex, accntIndex);
				ArrayList<String> rsList = new OBAccountImpl().getAssignedRSList(accntIndex);
				String sqlTextRS = convertSqlRSList(rsList);
				String sqlTextVS = convertSqlVSList(vsList);
				if (sqlTextVS == null || sqlTextVS.isEmpty() == true) {
					db.closeDB();
					OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result list count:%d", list.size()));
					return list;
				}
				sqlText = String.format(
						" SELECT A.INDEX, A.IP_ADDRESS, A.STATE, A.STATUS, A.ALTEON_ID, A.NAME, A.RATIO, B.GROUP_INDEX FROM     \n"
								+ " (SELECT * FROM TMP_SLB_NODE WHERE ADC_INDEX = %d                                                    \n"
								+ " AND INDEX IN (%s) OR INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER                            \n"
								+ " WHERE POOL_INDEX IN ( SELECT POOL_INDEX FROM TMP_SLB_VSERVER                                        \n"
								+ " WHERE INDEX IN (%s)))) A                                                                            \n"
								+ " LEFT JOIN (SELECT RS_INDEX, GROUP_INDEX FROM MNG_REALSERVER_GROUP_MAP                               \n"
								+ " WHERE GROUP_INDEX IN (SELECT INDEX FROM MNG_REALSERVER_GROUP WHERE ADC_INDEX = %d AND ACCNT_INDEX = %d)) B \n"
								+ " ON A.INDEX = B.RS_INDEX                                                                             \n"
								+ " WHERE B.GROUP_INDEX IS NULL ",
						adcIndex, sqlTextRS, sqlTextVS, adcIndex, accntIndex);
			} else {
				sqlText = String.format(
						" SELECT A.INDEX, A.IP_ADDRESS, A.STATE, A.STATUS, A.ALTEON_ID, A.NAME, A.RATIO, B.GROUP_INDEX FROM     \n"
								+ " (SELECT * FROM TMP_SLB_NODE WHERE ADC_INDEX = %d                                                    \n"
								+ " AND INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER                                             \n"
								+ " WHERE ADC_INDEX = %d)) A                                                                            \n"
								+ " LEFT JOIN (SELECT RS_INDEX, GROUP_INDEX FROM MNG_REALSERVER_GROUP_MAP                               \n"
								+ " WHERE GROUP_INDEX IN (SELECT INDEX FROM MNG_REALSERVER_GROUP WHERE ADC_INDEX = %d AND ACCNT_INDEX = %d)) B \n"
								+ " ON A.INDEX = B.RS_INDEX                                                                             \n"
								+ " WHERE B.GROUP_INDEX IS NULL ",
						adcIndex, adcIndex, adcIndex, accntIndex);
			}
			sqlText += sqlWhere;
			sqlText += getNodeListF5OrderType(orderType, orderDir);
			sqlText += sqlLimit;

			rs = db.executeQuery(sqlText);

			OBDtoAdcNodeDetail node = null;
			String nodeIndexList = ""; // node index 목록 "'a', 'b', 'c' ..." 다음 sql의 where절에 필요
			while (rs.next()) {
				node = new OBDtoAdcNodeDetail();
				node.setVserverAllowed(new ArrayList<String>());
				node.setVserverNotAllowed(new ArrayList<String>());

				node.setIndex(db.getString(rs, "INDEX"));
				node.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				node.setState(db.getInteger(rs, "STATE"));
				node.setStatus(db.getInteger(rs, "STATUS"));
				node.setName(db.getString(rs, "NAME"));
				node.setAlteonID(db.getString(rs, "ALTEON_ID"));
				node.setRatio(db.getInteger(rs, "RATIO"));
				node.setGroupIndex(db.getInteger(rs, "GROUP_INDEX"));
				node.setType(OBDefine.REALSERVER_NOTGROUP);
				list.add(node);
				nodeIndexList += (", " + OBParser.sqlString(node.getIndex()));
			}

			if (list != null || !list.isEmpty()) {
				sqlText = "";
				for (OBDtoAdcNodeDetail nodeList : list) {
					sqlText = String.format(
							" SELECT SUM(CONN_CURR) AS CONN_CURR FROM TMP_FAULT_SVC_MEMBER_PERF_STATS       \n"
									+ " WHERE MEMBER_INDEX IN (                                                                     \n"
									+ " SELECT INDEX FROM TMP_SLB_POOLMEMBER WHERE NODE_INDEX = %s)                                 \n",
							OBParser.sqlString(nodeList.getIndex()));

					rs = db.executeQuery(sqlText);
					if (rs.next())
						nodeList.setSession(db.getLong(rs, "CONN_CURR"));
				}
			}

			if (list.size() > 0) // node가 있을때만, node가 속한 vs를 뽑아낸다. 없으면 빈 list 들고 나감
			{
				nodeIndexList = nodeIndexList.substring(1, nodeIndexList.length()); // 맨앞의 comma 제거
				String sqlWhere2 = String.format(" AND N.INDEX IN ( %s ) ", nodeIndexList);
				sqlText = String.format(
						" SELECT DISTINCT N.INDEX NODE_INDEX, V.NAME VS_NAME, V.INDEX VS_INDEX                       \n"
								+ " FROM (SELECT INDEX FROM TMP_SLB_NODE WHERE ADC_INDEX = %d) N                               \n"
								+ " INNER JOIN (SELECT NODE_INDEX, POOL_INDEX FROM TMP_SLB_POOLMEMBER WHERE ADC_INDEX = %d) P  \n"
								+ " ON N.INDEX = P.NODE_INDEX                                                                  \n"
								+ " INNER JOIN ( SELECT INDEX, NAME, POOL_INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d) V   \n"
								+ " ON P.POOL_INDEX = V.POOL_INDEX                                                             \n"
								+ " %s ",
						adcIndex, adcIndex, adcIndex, sqlWhere2);
				rs = db.executeQuery(sqlText);

				String nodeIndex = "";
				String vsIndex = "";
				String vsName = "";
				boolean isVsAllowed = false;
				int i;
				OBDtoAdcNodeDetail currentNode = null; // loop 처리시 코딩 간편화를 위한 container
				while (rs.next()) // 각 node의 vs엔트리를 앞서 조사한 node목록에 붙인다.
				{
					// inner로 join한 값이므로, node_index와 vs_index는 null이 나오지 않고,
					// vs_name은 f5에서 key이므로 null이 없다. 따라서 아래 3개값은 null체크를 생략한다.
					nodeIndex = db.getString(rs, "NODE_INDEX");
					vsIndex = db.getString(rs, "VS_INDEX");
					vsName = db.getString(rs, "VS_NAME");

					// vs의 소유권 확인
					if (roleNo.equals(OBDefine.ACCNT_ROLE_VSADMIN) == false
							&& roleNo.equals(OBDefine.ACCNT_ROLE_RSADMIN) == false) {// VS_ADMIN, RS_ADMIN 아니면 모든 vs에
																						// 소유권이 있으므로 소유권 검사없이 allowed
						isVsAllowed = true;
					} else
					// vs를 소유했는지 확인한다.
					{
						isVsAllowed = false;
						for (String vsOwn : vsOwnList) {
							if (vsIndex.equals(vsOwn)) // 권한을 받은 virtual server
							{
								isVsAllowed = true;
								break;
							}
						}
					}

					// node list의 맞는 node를 찾아 vs정보 넣기
					for (i = 0; i < list.size(); i++) {
						currentNode = list.get(i);
						if (currentNode == null) // 그럴리가 없지만 체크
						{
							continue;
						}
						if (currentNode.getIndex().equals(nodeIndex)) // 같은 node이면 vs정보를 넣는다.
						{
							if (isVsAllowed == true) {
								currentNode.getVserverAllowed().add(vsName);
							} else {
								currentNode.getVserverNotAllowed().add(vsName);
							}
							break; // 찾았으니 그만하고 다음~~~
						}
					}
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
			if (db != null) {
				db.closeDB();
			}
		}

		return list;
	}

	// 노드 전체 목록을 가져온다. 페이징, search key
	// pool에 이미 들어간 node는 빼야할 것 같지만 alteon은 port가 다르면 member로 다시 들어올 수 있으므로 전부 뽑는다.
	public ArrayList<OBDtoAdcNodeDetail> searchNodeListAlteon(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		ResultSet rs;
		ArrayList<OBDtoAdcNodeDetail> list = new ArrayList<OBDtoAdcNodeDetail>();
//		ArrayList<String> vsOwnList = null; // 계정이 소유한 vs list

		try {
			db.openDB();
			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accntIndex);

			if (roleNo.equals(OBDefine.ACCNT_ROLE_VSADMIN) || roleNo.equals(OBDefine.ACCNT_ROLE_RSADMIN)) // vs admin,
																											// 할당된 vs를
																											// 구해놓는다.
			{
//				vsOwnList = new OBAccountImpl().getAssignedVSList(adcIndex, accntIndex);
			}

			int offset = 0;
			if (beginIndex != null && beginIndex.intValue() >= 0) {
				offset = beginIndex.intValue();
			}

			int limit = 0;
			String sqlLimit = "";
			if (endIndex != null && endIndex.intValue() >= 0) {
				limit = Math.abs(endIndex.intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}

			String sqlWhere = "";
			if (searchKey == null) {
				searchKey = "";
			} else if (searchKey.isEmpty() == false) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlWhere = String.format(" AND (NAME LIKE %s OR IP_ADDRESS LIKE %s) \n",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			// sqlText = String.format(" SELECT * FROM TMP_SLB_NODE WHERE ADC_INDEX = %d ",
			// adcIndex);
			// sqlText = String.format(
			// " SELECT * FROM TMP_SLB_NODE WHERE ADC_INDEX = %d \n" +
			// " AND INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER \n" +
			// " WHERE ADC_INDEX = %d) ",
			// adcIndex, adcIndex);

			if (roleNo == OBDefine.ACCNT_ROLE_VSADMIN || roleNo == OBDefine.ACCNT_ROLE_RSADMIN) {
				ArrayList<String> vsList = new OBAccountImpl().getAssignedVSList(adcIndex, accntIndex);
				ArrayList<String> rsList = new OBAccountImpl().getAssignedRSList(accntIndex);
				String sqlTextRS = convertSqlRSList(rsList);
				String sqlTextVS = convertSqlVSList(vsList);
				if (sqlTextVS == null || sqlTextVS.isEmpty() == true) {
					db.closeDB();
					OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result list count:%d", list.size()));
					return list;
				}
				sqlText = String.format(
						" SELECT A.INDEX, A.IP_ADDRESS, A.STATE, A.STATUS, A.ALTEON_ID, A.NAME, A.RATIO, B.GROUP_INDEX FROM             \n"
								+ " (SELECT * FROM TMP_SLB_NODE WHERE ADC_INDEX = %d                                                            \n"
								+ " AND INDEX IN (%s) OR INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER                                    \n"
								+ " WHERE POOL_INDEX IN ( SELECT POOL_INDEX FROM TMP_SLB_VS_SERVICE                                             \n"
								+ " WHERE VS_INDEX IN (%s)))) A                                                                                    \n"
								+ " LEFT JOIN (SELECT RS_INDEX, GROUP_INDEX FROM MNG_REALSERVER_GROUP_MAP                                       \n"
								+ " WHERE GROUP_INDEX IN (SELECT INDEX FROM MNG_REALSERVER_GROUP WHERE ADC_INDEX = %d AND ACCNT_INDEX = %d)) B  \n"
								+ " ON A.INDEX = B.RS_INDEX                                                                                     \n"
								+ " WHERE B.GROUP_INDEX IS NULL ",
						adcIndex, sqlTextRS, sqlTextVS, adcIndex, accntIndex);
			} else {
				sqlText = String.format(
						" SELECT A.INDEX, A.IP_ADDRESS, A.STATE, A.STATUS, A.ALTEON_ID, A.NAME, A.RATIO, B.GROUP_INDEX FROM             \n"
								+ " (SELECT * FROM TMP_SLB_NODE WHERE ADC_INDEX = %d                                                            \n"
								+ " AND INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER                                                     \n"
								+ " WHERE ADC_INDEX = %d)) A                                                                                    \n"
								+ " LEFT JOIN (SELECT RS_INDEX, GROUP_INDEX FROM MNG_REALSERVER_GROUP_MAP                                       \n"
								+ " WHERE GROUP_INDEX IN (SELECT INDEX FROM MNG_REALSERVER_GROUP WHERE ADC_INDEX = %d AND ACCNT_INDEX = %d)) B  \n"
								+ " ON A.INDEX = B.RS_INDEX                                                                                     \n"
								+ " WHERE B.GROUP_INDEX IS NULL ",
						adcIndex, adcIndex, adcIndex, accntIndex);
			}
			sqlText += sqlWhere;
			sqlText += getNodeListF5OrderType(orderType, orderDir);
			sqlText += sqlLimit;

			rs = db.executeQuery(sqlText);

			OBDtoAdcNodeDetail node = null;
			String nodeIndexList = ""; // node index 목록 "'a', 'b', 'c' ..." 다음 sql의 where절에 필요
			while (rs.next()) {
				node = new OBDtoAdcNodeDetail();
				node.setVserverAllowed(new ArrayList<String>());
				node.setVserverNotAllowed(new ArrayList<String>());

				node.setIndex(db.getString(rs, "INDEX"));
				node.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				node.setState(db.getInteger(rs, "STATE"));
				node.setStatus(db.getInteger(rs, "STATUS"));
				node.setName(db.getString(rs, "NAME"));
				node.setRatio(db.getInteger(rs, "RATIO"));
				node.setAlteonID(db.getString(rs, "ALTEON_ID"));
				node.setGroupIndex(db.getInteger(rs, "GROUP_INDEX"));
				node.setType(OBDefine.REALSERVER_NOTGROUP);
				list.add(node);
				nodeIndexList += (", " + OBParser.sqlString(node.getIndex()));
			}

			if (list != null || !list.isEmpty()) {
				sqlText = "";
				for (OBDtoAdcNodeDetail nodeList : list) {
					sqlText = String.format(
							" SELECT SUM(CONN_CURR) AS CONN_CURR FROM TMP_FAULT_SVC_MEMBER_PERF_STATS       \n"
									+ " WHERE MEMBER_INDEX IN (                                                                     \n"
									+ " SELECT INDEX FROM TMP_SLB_POOLMEMBER WHERE NODE_INDEX = %s)                                 \n",
							OBParser.sqlString(nodeList.getIndex()));

					rs = db.executeQuery(sqlText);
					if (rs.next())
						nodeList.setSession(db.getLong(rs, "CONN_CURR"));
				}
			}

			if (list.size() > 0) // node가 있을때만, node가 속한 vs를 뽑아낸다. 없으면 빈 list 들고 나감
			{
				nodeIndexList = nodeIndexList.substring(1, nodeIndexList.length()); // 맨앞의 comma 제거
				String sqlWhere2 = String.format(" AND N.INDEX IN ( %s ) ", nodeIndexList);
				sqlText = String.format(
						" SELECT DISTINCT N.INDEX NODE_INDEX, COALESCE(NULLIF(W.NAME, ''), W.VIRTUAL_IP) VS_NAME, V.INDEX VS_INDEX \n"
								+ " FROM (SELECT INDEX FROM TMP_SLB_NODE WHERE ADC_INDEX = %d) N                                \n"
								+ " INNER JOIN (SELECT NODE_INDEX, POOL_INDEX FROM TMP_SLB_POOLMEMBER WHERE ADC_INDEX = %d) P   \n"
								+ " ON N.INDEX = P.NODE_INDEX                                                                   \n"
								+ " INNER JOIN ( SELECT INDEX, VS_INDEX, POOL_INDEX FROM TMP_SLB_VS_SERVICE WHERE ADC_INDEX = %d) V \n"
								+ " ON P.POOL_INDEX = V.POOL_INDEX                                                              \n"
								+ " INNER JOIN ( SELECT INDEX, NAME, VIRTUAL_IP, POOL_INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d) W \n"
								+ " ON V.VS_INDEX = W.INDEX                                                                     \n"
								+ " %s ",
						adcIndex, adcIndex, adcIndex, adcIndex, sqlWhere2);
				rs = db.executeQuery(sqlText);

				String nodeIndex = "";
//				String vsIndex = "";
				String vsName = "";
				boolean isVsAllowed = false;
				int i;
				OBDtoAdcNodeDetail currentNode = null; // loop 처리시 코딩 간편화를 위한 container
				while (rs.next()) // 각 node의 vs엔트리를 앞서 조사한 node목록에 붙인다.
				{
					// inner로 join한 값이므로, node_index와 vs_index는 null이 나오지 않고,
					// vs_name은 f5에서 key이므로 null이 없다. 따라서 아래 3개값은 null체크를 생략한다.
					nodeIndex = db.getString(rs, "NODE_INDEX");
//					vsIndex = db.getString(rs, "VS_INDEX");
					vsName = db.getString(rs, "VS_NAME");

					// vs의 소유권 확인
					isVsAllowed = true;

					// node list의 맞는 node를 찾아 vs정보 넣기
					for (i = 0; i < list.size(); i++) {
						currentNode = list.get(i);
						if (currentNode == null) // 그럴리가 없지만 체크
						{
							continue;
						}
						if (currentNode.getIndex().equals(nodeIndex)) // 같은 node이면 vs정보를 넣는다.
						{
							if (isVsAllowed == true) {
								currentNode.getVserverAllowed().add(vsName);
							} else {
								currentNode.getVserverNotAllowed().add(vsName);
							}
							break; // 찾았으니 그만하고 다음~~~
						}
					}
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
			if (db != null) {
				db.closeDB();
			}
		}

		return list;
	}

	// 노드 전체 목록을 가져온다. 페이징, search key
	// pool에 이미 들어간 node는 빼야할 것 같지만 alteon은 port가 다르면 member로 다시 들어올 수 있으므로 전부 뽑는다.
	public ArrayList<OBDtoAdcNodeDetail> searchNodeListAll(OBDtoAdcScope scope, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		String sqlTextAlteonList = "";
		String sqlTextF5PASPASKList = "";
		String sqlTextRS = "";
		String sqlTextVSService = "";
		String sqlTextVS = "";
		String sqlGroup = "";
		ResultSet rs;
		ArrayList<OBDtoAdcNodeDetail> list = new ArrayList<OBDtoAdcNodeDetail>();
//		ArrayList<String> vsOwnList = null; // 계정이 소유한 vs list

		try {
			db.openDB();
			ArrayList<Integer> adcIndexList = new OBAdcManagementImpl().getUsersAdcIndexInteger(scope.getLevel(),
					scope.getIndex(), accntIndex);

			if (adcIndexList == null || adcIndexList.isEmpty()) {
				return list;
			}

			String adcAllList = OBParser.convertSqlGrpIndexList(adcIndexList);

			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accntIndex);

			int offset = 0;
			if (beginIndex != null && beginIndex.intValue() >= 0) {
				offset = beginIndex.intValue();
			}

			int limit = 0;
			String sqlLimit = "";
			if (endIndex != null && endIndex.intValue() >= 0) {
				limit = Math.abs(endIndex.intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}

			String sqlWhere = "";
			if (searchKey == null) {
				searchKey = "";
			} else if (searchKey.isEmpty() == false) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlWhere = String.format(" WHERE (A.NAME LIKE %s OR IP_ADDRESS LIKE %s) \n",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			// sqlText = String.format(" SELECT * FROM TMP_SLB_NODE WHERE ADC_INDEX = %d ",
			// adcIndex);
			// sqlText = String.format(
			// " SELECT * FROM TMP_SLB_NODE WHERE ADC_INDEX = %d \n" +
			// " AND INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER \n" +
			// " WHERE ADC_INDEX = %d) ",
			// adcIndex, adcIndex);

			for (int i = 0; i < adcIndexList.size(); i++) {
				int adcType = new OBAdcManagementImpl().getAdcType(adcIndexList.get(i));

				if (adcType == OBDefine.ADC_TYPE_ALTEON) {
					if (sqlTextAlteonList != "") {
						sqlTextAlteonList += ",";
					}
					sqlTextAlteonList += adcIndexList.get(i);
				} else {
					if (sqlTextF5PASPASKList != "") {
						sqlTextF5PASPASKList += ",";
					}
					sqlTextF5PASPASKList += adcIndexList.get(i);
				}
			}

			if (roleNo == OBDefine.ACCNT_ROLE_VSADMIN || roleNo == OBDefine.ACCNT_ROLE_RSADMIN) {
				for (int i = 0; i < adcIndexList.size(); i++) {
					int adcType = new OBAdcManagementImpl().getAdcType(adcIndexList.get(i));

					if (adcType == OBDefine.ADC_TYPE_ALTEON) {
						if (sqlTextVSService != "") {
							sqlTextVSService += ",";
						}
						new OBAccountImpl().getAssignedVSList(adcIndexList.get(i), accntIndex);
						ArrayList<String> vsList = new OBAccountImpl().getAssignedVSServiceList(adcIndexList.get(i),
								accntIndex);
						sqlTextVSService += convertSqlVSList(vsList);
					} else {
						new OBAccountImpl().getAssignedVSList(adcIndexList.get(i), accntIndex);
						ArrayList<String> vsList = new OBAccountImpl().getAssignedVSList(adcIndexList.get(i),
								accntIndex);
						ArrayList<String> rsList = new OBAccountImpl().getAssignedRSList(adcIndexList.get(i));
						sqlTextRS += convertSqlRSList(rsList);
						sqlTextVS += convertSqlVSList(vsList);
					}
				}

				if ((sqlTextVS == null || sqlTextVS.isEmpty())
						&& (sqlTextVSService == null || sqlTextVSService.isEmpty())) {
					db.closeDB();
					OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result list count:%d", list.size()));
					return list;
				}

				if (sqlTextF5PASPASKList != "") {
					sqlText = String.format(
							" SELECT SUM(A.CONN_CURR) AS CONN_CURR, C.INDEX,  INET(C.IP_ADDRESS) AS IP_ADDRESS, C.STATE, C.STATUS, C.ALTEON_ID, C.NAME, C.RATIO, E.NAME AS ADC_NAME,    \n"
									+ " E.INDEX AS ADC_INDEX, E.TYPE AS ADC_TYPE, E.STATUS AS ADC_STATUS, E.OP_MODE FROM                                                    \n"
									+ " (SELECT MEMBER_INDEX, CONN_CURR FROM TMP_FAULT_SVC_MEMBER_PERF_STATS                                                                \n"
									+ " WHERE ADC_INDEX IN (%s)) A                                                                                                          \n"
									+ " RIGHT JOIN (SELECT NODE_INDEX, INDEX FROM TMP_SLB_POOLMEMBER                                                                        \n"
									+ " WHERE ADC_INDEX IN (%s)) B                                                                                                          \n"
									+ " ON A.MEMBER_INDEX = B.INDEX                                                                                                         \n"
									+ " LEFT JOIN (SELECT INDEX, IP_ADDRESS, STATE, STATUS, ALTEON_ID, NAME, RATIO, ADC_INDEX                                               \n"
									+ " FROM TMP_SLB_NODE WHERE ADC_INDEX IN (%s) OR INDEX IN (%s) ) C                                                                     \n"
									+ " ON B.NODE_INDEX = C.INDEX                                                                                                           \n"
									+ " LEFT JOIN (SELECT  POOL_INDEX FROM TMP_SLB_VSERVER  WHERE INDEX IN (%s)) D                                                          \n"
									+ " ON B.INDEX = D.POOL_INDEX                                                                                                           \n"
									+ " LEFT JOIN (SELECT INDEX, NAME, TYPE, STATUS, OP_MODE FROM MNG_ADC WHERE INDEX IN(%s)) E                                             \n"
									+ " ON C.ADC_INDEX = E.INDEX                                                                                                            \n",
							sqlTextF5PASPASKList, sqlTextF5PASPASKList, sqlTextF5PASPASKList, sqlTextRS, sqlTextVS,
							sqlTextF5PASPASKList);
				}
				if (sqlTextF5PASPASKList != "" && sqlTextAlteonList != "") {
					sqlText += " GROUP BY C.INDEX,  C.IP_ADDRESS, C.STATE, C.STATUS, C.ALTEON_ID, C.NAME, C.RATIO, E.NAME, E.INDEX, \n"
							+ " E.TYPE, E.STATUS, E.OP_MODE                                                                         \n";
					sqlText += " UNION                                                                                              \n";
				}
				if (sqlTextAlteonList != "") {
					sqlText += String.format(
							" SELECT SUM(A.CONN_CURR) AS CONN_CURR, C.INDEX,  INET(C.IP_ADDRESS) AS IP_ADDRESS, C.STATE, C.STATUS, C.ALTEON_ID, C.NAME, C.RATIO, E.NAME AS ADC_NAME,    \n"
									+ " E.INDEX AS ADC_INDEX, E.TYPE AS ADC_TYPE, E.STATUS AS ADC_STATUS, E.OP_MODE FROM                                                    \n"
									+ " (SELECT MEMBER_INDEX, CONN_CURR FROM TMP_FAULT_SVC_MEMBER_PERF_STATS                                                                \n"
									+ " WHERE ADC_INDEX IN (%s)) A                                                                                                          \n"
									+ " RIGHT JOIN (SELECT NODE_INDEX, INDEX FROM TMP_SLB_POOLMEMBER                                                                        \n"
									+ " WHERE ADC_INDEX IN (%s)) B                                                                                                          \n"
									+ " ON A.MEMBER_INDEX = B.INDEX                                                                                                         \n"
									+ " LEFT JOIN (SELECT INDEX, IP_ADDRESS, STATE, STATUS, ALTEON_ID, NAME, RATIO, ADC_INDEX                                               \n"
									+ " FROM TMP_SLB_NODE WHERE ADC_INDEX IN (%s)) C                                                                                        \n"
									+ " ON B.NODE_INDEX = C.INDEX                                                                                                           \n"
									+ " LEFT JOIN (SELECT  POOL_INDEX FROM TMP_SLB_VS_SERVICE  WHERE INDEX IN (%s)) D                                                       \n"
									+ " ON B.INDEX = D.POOL_INDEX                                                                                                           \n"
									+ " LEFT JOIN (SELECT INDEX, NAME, TYPE, STATUS, OP_MODE FROM MNG_ADC WHERE INDEX IN(%s)) E                                             \n"
									+ " ON C.ADC_INDEX = E.INDEX                                                                                                            \n",
							sqlTextAlteonList, sqlTextAlteonList, sqlTextAlteonList, sqlTextVSService,
							sqlTextAlteonList);
				}

				sqlGroup = " GROUP BY C.INDEX,  C.IP_ADDRESS, C.STATE, C.STATUS, C.ALTEON_ID, C.NAME, C.RATIO, E.NAME, E.INDEX, \n"
						+ " E.TYPE, E.STATUS, E.OP_MODE                                                                     \n";
			} else {
				sqlText = String.format(
						"  SELECT SUM(A.CONN_CURR) AS CONN_CURR, C.INDEX,  INET(C.IP_ADDRESS) AS IP_ADDRESS, C.STATE, C.STATUS, C.ALTEON_ID, C.NAME, C.RATIO, D.NAME AS ADC_NAME,\n"
								+ " D.INDEX AS ADC_INDEX, D.TYPE AS ADC_TYPE, D.STATUS AS ADC_STATUS, D.OP_MODE FROM                                    \n"
								+ " (SELECT MEMBER_INDEX, CONN_CURR FROM TMP_FAULT_SVC_MEMBER_PERF_STATS                                                \n"
								+ " WHERE ADC_INDEX IN (%s)) A                                                                                          \n"
								+ " RIGHT JOIN (SELECT NODE_INDEX, INDEX FROM TMP_SLB_POOLMEMBER                                                        \n"
								+ " WHERE ADC_INDEX IN (%s)) B                                                                                          \n"
								+ " ON A.MEMBER_INDEX = B.INDEX                                                                                         \n"
								+ " LEFT JOIN (SELECT INDEX, IP_ADDRESS, STATE, STATUS, ALTEON_ID, NAME, RATIO, ADC_INDEX                               \n"
								+ " FROM TMP_SLB_NODE WHERE ADC_INDEX IN (%s)) C                                                                        \n"
								+ " ON B.NODE_INDEX = C.INDEX                                                                                           \n"
								+ " LEFT JOIN (SELECT INDEX, NAME, TYPE, STATUS, OP_MODE FROM MNG_ADC WHERE INDEX IN(%s)) D                             \n"
								+ " ON C.ADC_INDEX = D.INDEX                                                                                            \n",
						adcAllList, adcAllList, adcAllList, adcAllList);

				sqlGroup = " GROUP BY C.INDEX,  C.IP_ADDRESS, C.STATE, C.STATUS, C.ALTEON_ID, C.NAME, C.RATIO, D.NAME, D.INDEX,                 \n"
						+ " D.TYPE, D.STATUS, D.OP_MODE                                                                                     \n";
			}
			sqlText += sqlWhere;
			sqlText += sqlGroup;
			sqlText += getNodeListF5OrderType(orderType, orderDir);
			sqlText += sqlLimit;

			rs = db.executeQuery(sqlText);

			OBDtoAdcNodeDetail node = null;
			String nodeIndexList = ""; // node index 목록 "'a', 'b', 'c' ..." 다음 sql의 where절에 필요
			while (rs.next()) {
				node = new OBDtoAdcNodeDetail();
				node.setVserverAllowed(new ArrayList<String>());
				node.setVserverNotAllowed(new ArrayList<String>());

				node.setIndex(db.getString(rs, "INDEX"));
				node.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				node.setState(db.getInteger(rs, "STATE"));
				node.setName(db.getString(rs, "NAME"));
				node.setStatus(db.getInteger(rs, "STATUS"));
				node.setAlteonID(db.getString(rs, "ALTEON_ID"));
				node.setRatio(db.getInteger(rs, "RATIO"));
				node.setAdcName(db.getString(rs, "ADC_NAME"));
				node.setSession(db.getLong(rs, "CONN_CURR"));
				node.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				node.setAdcType(db.getInteger(rs, "ADC_TYPE"));
				node.setAdcStatus(db.getInteger(rs, "ADC_STATUS"));
				node.setAdcMode(db.getInteger(rs, "OP_MODE"));
//                node.setGroupIndex(db.getInteger(rs, "GROUP_INDEX"));
				node.setType(-1);
				list.add(node);
				nodeIndexList += (", " + OBParser.sqlString(node.getIndex()));
			}

			if (list.size() > 0) // node가 있을때만, node가 속한 vs를 뽑아낸다. 없으면 빈 list 들고 나감
			{
				sqlText = "";
				nodeIndexList = nodeIndexList.substring(1, nodeIndexList.length()); // 맨앞의 comma 제거
				String sqlWhere2 = String.format(" AND N.INDEX IN ( %s ) ", nodeIndexList);

				if (sqlTextF5PASPASKList != "") {
					sqlText = String.format(
							" SELECT DISTINCT N.INDEX NODE_INDEX, COALESCE(NULLIF(V.NAME, ''), V.VIRTUAL_IP) VS_NAME, V.INDEX VS_INDEX  \n"
									+ " FROM (SELECT INDEX FROM TMP_SLB_NODE WHERE ADC_INDEX IN (%s)) N                                         \n"
									+ " INNER JOIN (SELECT NODE_INDEX, POOL_INDEX FROM TMP_SLB_POOLMEMBER WHERE ADC_INDEX IN (%s)) P            \n"
									+ " ON N.INDEX = P.NODE_INDEX                                                                               \n"
									+ " INNER JOIN ( SELECT INDEX, NAME, VIRTUAL_IP, POOL_INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX IN (%s)) V \n"
									+ " ON P.POOL_INDEX = V.POOL_INDEX                                                                          \n"
									+ " %s                                                                                                      \n",
							sqlTextF5PASPASKList, sqlTextF5PASPASKList, sqlTextF5PASPASKList, sqlWhere2);
				}
				if (sqlTextF5PASPASKList != "" && sqlTextAlteonList != "") {
					sqlText += " UNION                                                                                       \n";
				}
				if (sqlTextAlteonList != "") {
					sqlText += String.format(
							" SELECT DISTINCT N.INDEX NODE_INDEX, COALESCE(NULLIF(W.NAME, ''), W.VIRTUAL_IP) VS_NAME, V.INDEX VS_INDEX  \n"
									+ " FROM (SELECT INDEX FROM TMP_SLB_NODE WHERE ADC_INDEX IN (%s)) N                                         \n"
									+ " INNER JOIN (SELECT NODE_INDEX, POOL_INDEX FROM TMP_SLB_POOLMEMBER WHERE ADC_INDEX IN (%s)) P            \n"
									+ " ON N.INDEX = P.NODE_INDEX                                                                               \n"
									+ " INNER JOIN ( SELECT INDEX, VS_INDEX, POOL_INDEX FROM TMP_SLB_VS_SERVICE WHERE ADC_INDEX IN (%s)) V      \n"
									+ " ON P.POOL_INDEX = V.POOL_INDEX                                                                          \n"
									+ " INNER JOIN ( SELECT INDEX, NAME, VIRTUAL_IP, POOL_INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX IN (%s)) W \n"
									+ " ON V.VS_INDEX = W.INDEX                                                                                 \n"
									+ " %s                                                                                                      \n",
							sqlTextAlteonList, sqlTextAlteonList, sqlTextAlteonList, sqlTextAlteonList, sqlWhere2);
				}
				rs = db.executeQuery(sqlText);

				String nodeIndex = "";
				String vsName = "";
				boolean isVsAllowed = false;
				int i;
				OBDtoAdcNodeDetail currentNode = null; // loop 처리시 코딩 간편화를 위한 container
				while (rs.next()) // 각 node의 vs엔트리를 앞서 조사한 node목록에 붙인다.
				{
					// inner로 join한 값이므로, node_index와 vs_index는 null이 나오지 않고,
					// vs_name은 f5에서 key이므로 null이 없다. 따라서 아래 3개값은 null체크를 생략한다.
					nodeIndex = db.getString(rs, "NODE_INDEX");
					vsName = db.getString(rs, "VS_NAME");

					// vs의 소유권 확인
					isVsAllowed = true;

					// node list의 맞는 node를 찾아 vs정보 넣기
					for (i = 0; i < list.size(); i++) {
						currentNode = list.get(i);
						if (currentNode == null) // 그럴리가 없지만 체크
						{
							continue;
						}
						if (currentNode.getIndex().equals(nodeIndex)) // 같은 node이면 vs정보를 넣는다.
						{
							if (isVsAllowed == true) {
								currentNode.getVserverAllowed().add(vsName);
							} else {
								currentNode.getVserverNotAllowed().add(vsName);
							}
							break; // 찾았으니 그만하고 다음~~~
						}
					}
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
			if (db != null) {
				db.closeDB();
			}
		}
		return list;
	}

	// VS 전체 목록을 가져온다. 페이징, search key
	// pool에 이미 들어간 node는 빼야할 것 같지만 alteon은 port가 다르면 member로 다시 들어올 수 있으므로 전부 뽑는다.
	public ArrayList<OBDtoAdcVServerAll> searchVServerListAll(OBDtoAdcScope scope, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		ResultSet rs;
		ArrayList<OBDtoAdcVServerAll> list = new ArrayList<OBDtoAdcVServerAll>();
		ArrayList<OBDtoAdcVServerAll> list2 = new ArrayList<OBDtoAdcVServerAll>();

		try {
			db.openDB();
			ArrayList<Integer> adcIndexList = new OBAdcManagementImpl().getUsersAdcIndexInteger(scope.getLevel(),
					scope.getIndex(), accntIndex);

			if (adcIndexList == null || adcIndexList.isEmpty()) {
				return list;
			}

			String adcAllList = OBParser.convertSqlGrpIndexList(adcIndexList);

			int offset = 0;
			if (beginIndex != null && beginIndex.intValue() >= 0) {
				offset = beginIndex.intValue();
			}

			int limit = 0;
			String sqlLimit = "";
			if (endIndex != null && endIndex.intValue() >= 0) {
				limit = Math.abs(endIndex.intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}
			int rescInterval = new OBEnvManagementImpl().getAdcSyncInterval(db);// 초 단위.
			String sqlWhere = "";
			if (searchKey == null) {
				searchKey = "";
			} else if (searchKey.isEmpty() == false) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlWhere = String.format(" AND (NAME VS_NAME %s OR VIRTUAL_IP LIKE %s) \n",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}
			sqlText = String.format(
					" SELECT V.VS_INDEX, V.OCCUR_TIME, V.STATUS, V.STATUS_SORT, V.VS_NAME, V.VIRTUAL_IP, V.VIRTUAL_PORT,                        \n"
							+ " V.ADC_INDEX, V.ADC_NAME, V.ADC_IP, V.ADC_TYPE, V.ADC_STATUS, V.CONN_CURR, COALESCE(V.CONN_CURR, -1) CONN_CURR_VALUE,      \n"
							+ " V.BPS_IN, COALESCE(V.BPS_IN, -1) BPS_IN_VALUE, V.BPS_OUT, COALESCE(V.BPS_OUT, -1) BPS_OUT_VALUE,                          \n"
							+ " V.BPS_TOT, COALESCE(V.BPS_TOT, -1) BPS_TOT_VALUE                                                                          \n"
							+ " FROM (  SELECT A.INDEX VS_INDEX, A.APPLY_TIME, A.ADC_INDEX, A.STATUS,                                                     \n"
							+ " CASE WHEN A.STATUS=0 THEN 3 ELSE A.STATUS END STATUS_SORT,                                                                \n"
							+ " A.NAME VS_NAME, A.VIRTUAL_IP, A.VIRTUAL_PORT,                                                                             \n"
							+ " A.PERSISTENCE_INDEX, B.NAME ADC_NAME, B.IPADDRESS ADC_IP, B.TYPE ADC_TYPE, B.STATUS ADC_STATUS, C.CONN_CURR,              \n"
							+ " C.BPS_IN, C.BPS_OUT, C.BPS_TOT, D.OCCUR_TIME                                                                              \n"
							+ " FROM (SELECT INDEX, ADC_INDEX, STATUS, NAME, VIRTUAL_IP, VIRTUAL_PORT, PERSISTENCE_INDEX,                                 \n"
							+ " APPLY_TIME                                                                                                                \n"
							+ " FROM TMP_SLB_VSERVER WHERE ADC_INDEX IN (%s)) A                                                                           \n"
							+ " INNER JOIN (SELECT INDEX, NAME, IPADDRESS, TYPE, STATUS FROM MNG_ADC WHERE INDEX IN (%s) AND TYPE != %d) B                \n"
							+ " ON A.ADC_INDEX = B.INDEX                                                                                                  \n"
							+ " LEFT JOIN (  SELECT OBJ_INDEX, CONN_CURR, BPS_IN, BPS_OUT, BPS_TOT                                                        \n"
							+ " FROM TMP_FAULT_SVC_PERF_STATS                                                                                             \n"
							+ " WHERE ADC_INDEX IN (%s) AND OCCUR_TIME > NOW() - INTERVAL '%d SECONDS'  ) C                                               \n"
							+ " ON A.INDEX = C.OBJ_INDEX                                                                                                  \n"
							+ " LEFT JOIN (  SELECT VS_INDEX, OCCUR_TIME                                                                                  \n"
							+ " FROM LOG_CONFIG_HISTORY                                                                                                   \n"
							+ " WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX IN(%s)  GROUP BY VS_INDEX)) D               \n"
							+ " ON A.INDEX = D.VS_INDEX                                                                                                   \n",
					adcAllList, adcAllList, OBDefine.ADC_TYPE_ALTEON, adcAllList, rescInterval * 2, adcAllList);
			sqlText += " UNION                                                                                       \n";

			sqlText += String.format(
					" SELECT A.INDEX VS_INDEX, A.APPLY_TIME, A.ADC_INDEX, A.STATUS,                                                                     \n"
							+ " CASE WHEN A.STATUS=0 THEN 3 ELSE A.STATUS END STATUS_SORT,                                                                        \n"
							+ " A.NAME VS_NAME, A.VIRTUAL_IP, A.VIRTUAL_PORT,                                                                                     \n"
							+ " NULL PERSISTENCE_INDEX, B.NAME ADC_NAME, B.IPADDRESS ADC_IP, B.TYPE ADC_TYPE, B.STATUS ADC_STATUS,                                \n"
							+ " C.CONN_CURR, C.BPS_IN, C.BPS_OUT, C.BPS_TOT, D.OCCUR_TIME                                                                         \n"
							+ " FROM (SELECT INDEX, ADC_INDEX, VIRTUAL_IP, VIRTUAL_PORT, NAME, APPLY_TIME, STATUS FROM TMP_SLB_VSERVER WHERE ADC_INDEX IN (%s)) A \n"
							+ " INNER JOIN (SELECT INDEX, NAME, IPADDRESS, TYPE, STATUS FROM MNG_ADC WHERE INDEX IN (%s)  AND TYPE = %d ) B                       \n"
							+ " ON A.ADC_INDEX = B.INDEX                                                                                                          \n"
							+ " LEFT JOIN (  SELECT OBJ_INDEX, CONN_CURR, BPS_IN, BPS_OUT, BPS_TOT                                                                \n"
							+ " FROM TMP_FAULT_SVC_PERF_STATS                                                                                                     \n"
							+ " WHERE ADC_INDEX IN (%s) AND OCCUR_TIME > NOW() - INTERVAL '%d SECONDS'  ) C                                                       \n"
							+ " ON A.INDEX = C.OBJ_INDEX                                                                                                          \n"
							+ " LEFT JOIN (  SELECT VS_INDEX, OCCUR_TIME                                                                                          \n"
							+ " FROM LOG_CONFIG_HISTORY                                                                                                           \n"
							+ " WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX IN(%s)  GROUP BY VS_INDEX)) D                       \n"
							+ " ON A.INDEX = D.VS_INDEX     ) V                                                                                                   \n",
					adcAllList, adcAllList, OBDefine.ADC_TYPE_ALTEON, adcAllList, rescInterval * 2, adcAllList);
			sqlText += sqlWhere;
			sqlText += getVServerListAllOrderType(orderType, orderDir);
			sqlText += sqlLimit;

			rs = db.executeQuery(sqlText);

			OBDtoAdcVServerAll svc = null;

			while (rs.next()) {
				svc = new OBDtoAdcVServerAll();
				svc.setIndex(db.getString(rs, "VS_INDEX"));
				svc.setStatus(db.getInteger(rs, "STATUS"));
				svc.setName(db.getString(rs, "VS_NAME"));
				svc.setIp(db.getString(rs, "VIRTUAL_IP"));
				svc.setPort(Integer.toString(db.getInteger(rs, "VIRTUAL_PORT")));
				svc.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				svc.setAdcName(db.getString(rs, "ADC_NAME"));
				svc.setAdcIp(db.getString(rs, "ADC_IP"));
				svc.setAdcType(db.getInteger(rs, "ADC_TYPE"));
				svc.setAdcStatus(db.getInteger(rs, "ADC_STATUS"));
				svc.setConcurrentSession(db.getLong(rs, "CONN_CURR_VALUE"));
				svc.setBpsIn(db.getLong(rs, "BPS_IN_VALUE"));
				svc.setBpsOut(db.getLong(rs, "BPS_OUT_VALUE"));
				svc.setBpsTotal(db.getLong(rs, "BPS_TOT_VALUE"));
				svc.setUpdateTime(db.getTimestamp(rs, "OCCUR_TIME"));

				list.add(svc);
			}

			if (endIndex != null && endIndex.intValue() >= 0) {
				limit = Math.abs(endIndex.intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d ", limit * 2 * (offset + 1));
			}
			sqlText = "";

			sqlText += String.format(
					" SELECT A2.INDEX VS_INDEX, A2.APPLY_TIME, A2.ADC_INDEX, A1.STATUS,                                                     \n"
							+ " CASE WHEN A1.STATUS=0 THEN 3 ELSE A1.STATUS END STATUS_SORT,                                                          \n"
							+ " A2.NAME VS_NAME, A2.VIRTUAL_IP, A1.VIRTUAL_PORT,                                                                      \n"
							+ " A1.POOL_INDEX, NULL PERSISTENCE_INDEX, B.NAME ADC_NAME, B.IPADDRESS ADC_IP, B.TYPE ADC_TYPE, B.STATUS ADC_STATUS,     \n"
							+ " C.CONN_CURR, C.BPS_IN, C.BPS_OUT, C.BPS_TOT, D.OCCUR_TIME                                                             \n"
							+ " FROM (SELECT INDEX, VS_INDEX, POOL_INDEX, VIRTUAL_PORT, STATUS                                                        \n"
							+ " FROM TMP_SLB_VS_SERVICE WHERE ADC_INDEX IN (%s)) A1                                                                   \n"
							+ " LEFT JOIN (SELECT INDEX, ADC_INDEX, VIRTUAL_IP, NAME, APPLY_TIME FROM TMP_SLB_VSERVER) A2                             \n"
							+ " ON A1.VS_INDEX = A2.INDEX                                                                                             \n"
							+ " INNER JOIN (SELECT INDEX, NAME, IPADDRESS, TYPE, STATUS FROM MNG_ADC WHERE INDEX IN (%s)  AND TYPE = %d ) B           \n"
							+ " ON A2.ADC_INDEX = B.INDEX                                                                                             \n"
							+ " LEFT JOIN (  SELECT OBJ_INDEX, CONN_CURR, BPS_IN, BPS_OUT, BPS_TOT                                                    \n"
							+ " FROM TMP_FAULT_SVC_PERF_STATS                                                                                         \n"
							+ " WHERE ADC_INDEX IN (%s) AND OCCUR_TIME > NOW() - INTERVAL '%d SECONDS'  ) C                                           \n"
							+ " ON A1.INDEX = C.OBJ_INDEX                                                                                             \n"
							+ " LEFT JOIN (  SELECT VS_INDEX, OCCUR_TIME                                                                              \n"
							+ " FROM LOG_CONFIG_HISTORY                                                                                               \n"
							+ " WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX IN(%s)  GROUP BY VS_INDEX)) D           \n"
							+ " ON A2.INDEX = D.VS_INDEX                                                                                              \n",
					adcAllList, adcAllList, OBDefine.ADC_TYPE_ALTEON, adcAllList, rescInterval * 2, adcAllList);
			sqlText += sqlWhere;
			sqlText += getVServerListAllOrderType(orderType, orderDir);

			sqlText += sqlLimit;

			rs = db.executeQuery(sqlText);

			OBDtoAdcVServerAll port = null;

			while (rs.next()) {
				port = new OBDtoAdcVServerAll();
				port.setIndex(db.getString(rs, "VS_INDEX"));
				port.setStatus(db.getInteger(rs, "STATUS"));
				port.setName(db.getString(rs, "VS_NAME"));
				port.setIp(db.getString(rs, "VIRTUAL_IP"));
				port.setPort(Integer.toString(db.getInteger(rs, "VIRTUAL_PORT")));
				port.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				port.setAdcName(db.getString(rs, "ADC_NAME"));
				port.setAdcIp(db.getString(rs, "ADC_IP"));
				port.setAdcType(db.getInteger(rs, "ADC_TYPE"));
				port.setAdcStatus(db.getInteger(rs, "ADC_STATUS"));
				port.setUpdateTime(db.getTimestamp(rs, "OCCUR_TIME"));

				list2.add(port);
			}

			// Alteon만 VS index를 이용하여 Port를 합치는 작업을 진행한다.
			int resultSize = list.size();
			int portSize = list2.size();
			for (int i = 0; i < resultSize; i++) {
				if (list.get(i).getAdcType() != OBDefine.ADC_TYPE_ALTEON) {
					continue;
				}
				for (int j = 0; j < portSize; j++) {
					if (list.get(i).getIndex().equals(list2.get(j).getIndex())) {
						if (list.get(i).getPort().equals("0")) {
							list.get(i).setPort(list2.get(j).getPort());
						} else {
							list.get(i).setPort(list.get(i).getPort() + "," + list2.get(j).getPort());
						}

					}
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
			if (db != null) {
				db.closeDB();
			}
		}
		return list;
	}

	// VS 전체 목록을 가져온다. 페이징, search key
	// pool에 이미 들어간 node는 빼야할 것 같지만 alteon은 port가 다르면 member로 다시 들어올 수 있으므로 전부 뽑는다.
	public Integer searchVServerAllListCoreCount(OBDtoAdcScope scope, Integer accntIndex, String searchKey)
			throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		ResultSet rs;
		int count = 0;

		try {
			db.openDB();
			ArrayList<Integer> adcIndexList = new OBAdcManagementImpl().getUsersAdcIndexInteger(scope.getLevel(),
					scope.getIndex(), accntIndex);

			if (adcIndexList == null || adcIndexList.isEmpty()) {
				return count;
			}

			String adcAllList = OBParser.convertSqlGrpIndexList(adcIndexList);

			String sqlWhere = "";
			if (searchKey == null) {
				searchKey = "";
			} else if (searchKey.isEmpty() == false) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlWhere = String.format(" AND (NAME VS_NAME %s OR VIRTUAL_IP LIKE %s) \n",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			sqlText = String.format(
					"  SELECT COUNT(V.VS_INDEX) AS CNT                                                                      \n"
							+ " FROM (  SELECT A.INDEX VS_INDEX, A.ADC_INDEX, A.STATUS,                                               \n"
							+ " CASE WHEN A.STATUS=0 THEN 3 ELSE A.STATUS END STATUS_SORT,                                            \n"
							+ " A.NAME VS_NAME, A.VIRTUAL_IP, A.VIRTUAL_PORT,                                                         \n"
							+ " A.PERSISTENCE_INDEX, B.NAME ADC_NAME, B.IPADDRESS ADC_IP, B.TYPE ADC_TYPE                             \n"
							+ " FROM (SELECT INDEX, ADC_INDEX, STATUS, NAME, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX, PERSISTENCE_INDEX  \n"
							+ " FROM TMP_SLB_VSERVER WHERE ADC_INDEX IN (%s)) A                                                       \n"
							+ " INNER JOIN (SELECT INDEX, NAME, IPADDRESS, TYPE FROM MNG_ADC WHERE INDEX IN (%s) AND TYPE != %d) B    \n"
							+ " ON A.ADC_INDEX = B.INDEX                                                                              \n",
					adcAllList, adcAllList, OBDefine.ADC_TYPE_ALTEON);
			sqlText += " UNION                                                                                       \n";

			sqlText += String.format(
					" SELECT A.INDEX VS_INDEX, A.ADC_INDEX, A.STATUS,                                                       \n"
							+ " CASE WHEN A.STATUS=0 THEN 3 ELSE A.STATUS END STATUS_SORT,                                            \n"
							+ " A.NAME VS_NAME, A.VIRTUAL_IP, A.VIRTUAL_PORT,                                                         \n"
							+ " NULL PERSISTENCE_INDEX, B.NAME ADC_NAME, B.IPADDRESS ADC_IP, B.TYPE ADC_TYPE                          \n"
							+ " FROM (SELECT INDEX, ADC_INDEX, VIRTUAL_IP, NAME, STATUS, VIRTUAL_PORT FROM TMP_SLB_VSERVER) A         \n"
							+ " INNER JOIN (SELECT INDEX, NAME, IPADDRESS, TYPE FROM MNG_ADC WHERE INDEX IN (%s)  AND TYPE = %d ) B   \n"
							+ " ON A.ADC_INDEX = B.INDEX  ) V                                                                         \n"
							+ " LEFT JOIN (  SELECT OBJ_INDEX, CONN_CURR, BPS_IN, BPS_OUT, BPS_TOT                                    \n"
							+ " FROM TMP_FAULT_SVC_PERF_STATS                                                                         \n"
							+ " WHERE ADC_INDEX IN (%s) AND OCCUR_TIME > NOW() - INTERVAL '120 SECONDS'  ) C                          \n"
							+ " ON V.VS_INDEX = C.OBJ_INDEX                                                                           \n",
					adcAllList, OBDefine.ADC_TYPE_ALTEON, adcAllList);
			sqlText += sqlWhere;

			rs = db.executeQuery(sqlText);

			if (rs.next()) {
				count = db.getInteger(rs, "CNT");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}
		return count;
	}

	// 그룹 전체 목록을 가져온다. 페이징, search key
	// pool에 이미 들어간 node는 빼야할 것 같지만 F5는 port가 다르면 member로 다시 들어올 수 있으므로 전부 뽑는다.
	public ArrayList<OBDtoAdcNodeDetail> searchGroupListF5(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer orderType, Integer orderDir) throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		ResultSet rs;
		ArrayList<OBDtoAdcNodeDetail> list = new ArrayList<OBDtoAdcNodeDetail>();
		ArrayList<String> vsOwnList = null; // 계정이 소유한 vs list

		try {
			db.openDB();
			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accntIndex);

			if (roleNo.equals(OBDefine.ACCNT_ROLE_VSADMIN) || roleNo.equals(OBDefine.ACCNT_ROLE_RSADMIN)) // vs admin,
																											// 할당된 vs를
																											// 구해놓는다.
			{
				vsOwnList = new OBAccountImpl().getAssignedVSList(adcIndex, accntIndex);
			}

			String sqlWhere = "";
			if (searchKey == null) {
				searchKey = "";
			} else if (searchKey.isEmpty() == false) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlWhere = String.format(" AND (NAME LIKE %s OR IP_ADDRESS LIKE %s) \n",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			if (roleNo == OBDefine.ACCNT_ROLE_VSADMIN || roleNo == OBDefine.ACCNT_ROLE_RSADMIN) {
				ArrayList<String> vsList = new OBAccountImpl().getAssignedVSList(adcIndex, accntIndex);
				String sqlTextVS = convertSqlVSList(vsList);
				if (sqlTextVS == null || sqlTextVS.isEmpty() == true) {
					db.closeDB();
					OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result list count:%d", list.size()));
					return list;
				}
				sqlText = String.format(
						" SELECT A.INDEX, A.IP_ADDRESS, A.STATE, A.STATUS, A.ALTEON_ID, A.NAME, A.RATIO, B.GROUP_INDEX, C.RS_GROUP_NAME FROM      \n"
								+ " (SELECT * FROM TMP_SLB_NODE WHERE ADC_INDEX = %d                                                            \n"
								+ " AND INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER                                                     \n"
								+ " WHERE POOL_INDEX IN ( SELECT POOL_INDEX FROM TMP_SLB_VSERVER                                                \n"
								+ " WHERE INDEX IN (%s)))) A                                                                                    \n"
								+ " INNER JOIN (SELECT RS_INDEX, GROUP_INDEX FROM MNG_REALSERVER_GROUP_MAP) B                                   \n"
								+ " ON A.INDEX = B.RS_INDEX                                                                                     \n"
								+ " LEFT JOIN (SELECT INDEX, RS_GROUP_NAME FROM MNG_REALSERVER_GROUP WHERE ADC_INDEX = %d AND ACCNT_INDEX = %d) C \n"
								+ " ON B.GROUP_INDEX = C.INDEX                                                                                  \n"
								+ " WHERE B.GROUP_INDEX IS NOT NULL ",
						adcIndex, sqlTextVS, adcIndex, accntIndex);
			} else {
				sqlText = String.format(
						" SELECT A.INDEX, A.IP_ADDRESS, A.STATE, A.STATUS, A.ALTEON_ID, A.NAME, A.RATIO, B.GROUP_INDEX, C.RS_GROUP_NAME FROM                   \n"
								+ " (SELECT * FROM TMP_SLB_NODE WHERE ADC_INDEX = %d                                                            \n"
								+ " AND INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER                                                     \n"
								+ " WHERE ADC_INDEX = %d)) A                                                                                    \n"
								+ " INNER JOIN (SELECT RS_INDEX, GROUP_INDEX FROM MNG_REALSERVER_GROUP_MAP) B                                   \n"
								+ " ON A.INDEX = B.RS_INDEX                                                                                     \n"
								+ " LEFT JOIN (SELECT INDEX, RS_GROUP_NAME FROM MNG_REALSERVER_GROUP WHERE ADC_INDEX = %d AND ACCNT_INDEX = %d) C \n"
								+ " ON B.GROUP_INDEX = C.INDEX                                                                                  \n"
								+ " WHERE B.GROUP_INDEX IS NOT NULL ",
						adcIndex, adcIndex, adcIndex, accntIndex);
			}
			sqlText += sqlWhere;
			sqlText += getNodeListF5OrderType(orderType, orderDir);

			rs = db.executeQuery(sqlText);

			OBDtoAdcNodeDetail node = null;
			String nodeIndexList = ""; // node index 목록 "'a', 'b', 'c' ..." 다음 sql의 where절에 필요
			while (rs.next()) {
				node = new OBDtoAdcNodeDetail();
				node.setVserverAllowed(new ArrayList<String>());
				node.setVserverNotAllowed(new ArrayList<String>());

				node.setIndex(db.getString(rs, "INDEX"));
				node.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				node.setState(db.getInteger(rs, "STATE"));
				node.setStatus(db.getInteger(rs, "STATUS"));
				node.setName(db.getString(rs, "NAME"));
				node.setAlteonID(db.getString(rs, "ALTEON_ID"));
				node.setRatio(db.getInteger(rs, "RATIO"));
				node.setGroupIndex(db.getInteger(rs, "GROUP_INDEX"));
				node.setGroupName(db.getString(rs, "RS_GROUP_NAME"));
				node.setType(OBDefine.REALSERVER_GROUP);

				list.add(node);
				nodeIndexList += (", " + OBParser.sqlString(node.getIndex()));
			}

			if (list.size() > 0) // node가 있을때만, node가 속한 vs를 뽑아낸다. 없으면 빈 list 들고 나감
			{
				nodeIndexList = nodeIndexList.substring(1, nodeIndexList.length()); // 맨앞의 comma 제거
				String sqlWhere2 = String.format(" AND N.INDEX IN ( %s ) ", nodeIndexList);
				sqlText = String.format(
						" SELECT DISTINCT N.INDEX NODE_INDEX, V.NAME VS_NAME, V.INDEX VS_INDEX                       \n"
								+ " FROM (SELECT INDEX FROM TMP_SLB_NODE WHERE ADC_INDEX = %d) N                               \n"
								+ " INNER JOIN (SELECT NODE_INDEX, POOL_INDEX FROM TMP_SLB_POOLMEMBER WHERE ADC_INDEX = %d) P  \n"
								+ " ON N.INDEX = P.NODE_INDEX                                                                  \n"
								+ " INNER JOIN ( SELECT INDEX, NAME, POOL_INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d) V   \n"
								+ " ON P.POOL_INDEX = V.POOL_INDEX                                                             \n"
								+ " %s ",
						adcIndex, adcIndex, adcIndex, sqlWhere2);
				rs = db.executeQuery(sqlText);

				String nodeIndex = "";
				String vsIndex = "";
				String vsName = "";
				boolean isVsAllowed = false;
				int i;
				OBDtoAdcNodeDetail currentNode = null; // loop 처리시 코딩 간편화를 위한 container
				while (rs.next()) // 각 node의 vs엔트리를 앞서 조사한 node목록에 붙인다.
				{
					// inner로 join한 값이므로, node_index와 vs_index는 null이 나오지 않고,
					// vs_name은 f5에서 key이므로 null이 없다. 따라서 아래 3개값은 null체크를 생략한다.
					nodeIndex = db.getString(rs, "NODE_INDEX");
					vsIndex = db.getString(rs, "VS_INDEX");
					vsName = db.getString(rs, "VS_NAME");

					// vs의 소유권 확인
					if (roleNo.equals(OBDefine.ACCNT_ROLE_VSADMIN) == false
							&& roleNo.equals(OBDefine.ACCNT_ROLE_RSADMIN) == false) {// VS_ADMIN, RS_ADMIN 아니면 모든 vs에
																						// 소유권이 있으므로 소유권 검사없이 allowed
						isVsAllowed = true;
					} else
					// vs를 소유했는지 확인한다.
					{
						isVsAllowed = false;
						for (String vsOwn : vsOwnList) {
							if (vsIndex.equals(vsOwn)) // 권한을 받은 virtual server
							{
								isVsAllowed = true;
								break;
							}
						}
					}

					// node list의 맞는 node를 찾아 vs정보 넣기
					for (i = 0; i < list.size(); i++) {
						currentNode = list.get(i);
						if (currentNode == null) // 그럴리가 없지만 체크
						{
							continue;
						}
						if (currentNode.getIndex().equals(nodeIndex)) // 같은 node이면 vs정보를 넣는다.
						{
							if (isVsAllowed == true) {
								currentNode.getVserverAllowed().add(vsName);
							} else {
								currentNode.getVserverNotAllowed().add(vsName);
							}
							break; // 찾았으니 그만하고 다음~~~
						}
					}
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
			if (db != null) {
				db.closeDB();
			}
		}

		return list;
	}

	// 그룹 전체 목록을 가져온다. 페이징, search key
	// pool에 이미 들어간 node는 빼야할 것 같지만 F5는 port가 다르면 member로 다시 들어올 수 있으므로 전부 뽑는다.
	public ArrayList<OBDtoAdcNodeDetail> searchGroupListAlteon(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer orderType, Integer orderDir) throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		ResultSet rs;
		ArrayList<OBDtoAdcNodeDetail> list = new ArrayList<OBDtoAdcNodeDetail>();
		ArrayList<String> vsOwnList = null; // 계정이 소유한 vs list

		try {
			db.openDB();
			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accntIndex);

			if (roleNo.equals(OBDefine.ACCNT_ROLE_VSADMIN) || roleNo.equals(OBDefine.ACCNT_ROLE_RSADMIN)) // vs admin,
																											// 할당된 vs를
																											// 구해놓는다.
			{
				vsOwnList = new OBAccountImpl().getAssignedVSList(adcIndex, accntIndex);
			}

			String sqlWhere = "";
			if (searchKey == null) {
				searchKey = "";
			} else if (searchKey.isEmpty() == false) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlWhere = String.format(" AND (NAME LIKE %s OR IP_ADDRESS LIKE %s) \n",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			if (roleNo == OBDefine.ACCNT_ROLE_VSADMIN || roleNo == OBDefine.ACCNT_ROLE_RSADMIN) {
				ArrayList<String> vsList = new OBAccountImpl().getAssignedVSList(adcIndex, accntIndex);
				String sqlTextVS = convertSqlVSList(vsList);
				if (sqlTextVS == null || sqlTextVS.isEmpty() == true) {
					db.closeDB();
					OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result list count:%d", list.size()));
					return list;
				}
				sqlText = String.format(
						" SELECT A.INDEX, A.IP_ADDRESS, A.STATE, A.STATUS, A.ALTEON_ID, A.NAME, A.RATIO, B.GROUP_INDEX, C.RS_GROUP_NAME FROM      \n"
								+ " (SELECT * FROM TMP_SLB_NODE WHERE ADC_INDEX = %d                                                            \n"
								+ " AND INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER                                                     \n"
								+ " WHERE POOL_INDEX IN ( SELECT POOL_INDEX FROM TMP_SLB_VS_SERVICE                                             \n"
								+ " WHERE VS_INDEX IN (%s)))) A                                                                                 \n"
								+ " INNER JOIN (SELECT RS_INDEX, GROUP_INDEX FROM MNG_REALSERVER_GROUP_MAP) B                                   \n"
								+ " ON A.INDEX = B.RS_INDEX                                                                                     \n"
								+ " LEFT JOIN (SELECT INDEX, RS_GROUP_NAME FROM MNG_REALSERVER_GROUP WHERE ADC_INDEX = %d AND ACCNT_INDEX = %d) C \n"
								+ " ON B.GROUP_INDEX = C.INDEX                                                                                  \n"
								+ " WHERE B.GROUP_INDEX IS NOT NULL ",
						adcIndex, sqlTextVS, adcIndex, accntIndex);
			} else {
				sqlText = String.format(
						" SELECT A.INDEX, A.IP_ADDRESS, A.STATE, A.STATUS, A.ALTEON_ID, A.NAME, A.RATIO, B.GROUP_INDEX, C.RS_GROUP_NAME FROM      \n"
								+ " (SELECT * FROM TMP_SLB_NODE WHERE ADC_INDEX = %d                                                            \n"
								+ " AND INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER                                                     \n"
								+ " WHERE ADC_INDEX = %d)) A                                                                                    \n"
								+ " INNER JOIN (SELECT RS_INDEX, GROUP_INDEX FROM MNG_REALSERVER_GROUP_MAP) B                                   \n"
								+ " ON A.INDEX = B.RS_INDEX                                                                                     \n"
								+ " LEFT JOIN (SELECT INDEX, RS_GROUP_NAME FROM MNG_REALSERVER_GROUP WHERE ADC_INDEX = %d AND ACCNT_INDEX = %d) C \n"
								+ " ON B.GROUP_INDEX = C.INDEX                                                                                  \n"
								+ " WHERE B.GROUP_INDEX IS NOT NULL ",
						adcIndex, adcIndex, adcIndex, accntIndex);
			}
			sqlText += sqlWhere;
			sqlText += getNodeListF5OrderType(orderType, orderDir);

			rs = db.executeQuery(sqlText);

			OBDtoAdcNodeDetail node = null;
			String nodeIndexList = ""; // node index 목록 "'a', 'b', 'c' ..." 다음 sql의 where절에 필요
			while (rs.next()) {
				node = new OBDtoAdcNodeDetail();
				node.setVserverAllowed(new ArrayList<String>());
				node.setVserverNotAllowed(new ArrayList<String>());

				node.setIndex(db.getString(rs, "INDEX"));
				node.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				node.setState(db.getInteger(rs, "STATE"));
				node.setName(db.getString(rs, "NAME"));
				node.setStatus(db.getInteger(rs, "STATUS"));
				node.setAlteonID(db.getString(rs, "ALTEON_ID"));
				node.setRatio(db.getInteger(rs, "RATIO"));
				node.setGroupIndex(db.getInteger(rs, "GROUP_INDEX"));
				node.setGroupName(db.getString(rs, "RS_GROUP_NAME"));
				node.setType(OBDefine.REALSERVER_GROUP);

				list.add(node);
				nodeIndexList += (", " + OBParser.sqlString(node.getIndex()));
			}

			if (list.size() > 0) // node가 있을때만, node가 속한 vs를 뽑아낸다. 없으면 빈 list 들고 나감
			{
				nodeIndexList = nodeIndexList.substring(1, nodeIndexList.length()); // 맨앞의 comma 제거
				String sqlWhere2 = String.format(" AND N.INDEX IN ( %s ) ", nodeIndexList);
				sqlText = String.format(
						" SELECT DISTINCT N.INDEX NODE_INDEX, V.NAME VS_NAME, V.INDEX VS_INDEX                          \n"
								+ " FROM (SELECT INDEX FROM TMP_SLB_NODE WHERE ADC_INDEX = %d) N                                \n"
								+ " INNER JOIN (SELECT NODE_INDEX, POOL_INDEX FROM TMP_SLB_POOLMEMBER WHERE ADC_INDEX = %d) P   \n"
								+ " ON N.INDEX = P.NODE_INDEX                                                                   \n"
								+ " INNER JOIN ( SELECT INDEX, POOL_INDEX FROM TMP_SLB_VS_SERVICE WHERE ADC_INDEX = %d) V \n"
								+ " ON P.POOL_INDEX = V.POOL_INDEX                                                              \n"
								+ " %s ",
						adcIndex, adcIndex, adcIndex, sqlWhere2);
				rs = db.executeQuery(sqlText);

				String nodeIndex = "";
				String vsIndex = "";
				String vsName = "";
				boolean isVsAllowed = false;
				int i;
				OBDtoAdcNodeDetail currentNode = null; // loop 처리시 코딩 간편화를 위한 container
				while (rs.next()) // 각 node의 vs엔트리를 앞서 조사한 node목록에 붙인다.
				{
					// inner로 join한 값이므로, node_index와 vs_index는 null이 나오지 않고,
					// vs_name은 f5에서 key이므로 null이 없다. 따라서 아래 3개값은 null체크를 생략한다.
					nodeIndex = db.getString(rs, "NODE_INDEX");
					vsIndex = db.getString(rs, "VS_INDEX");
					vsName = db.getString(rs, "VS_NAME");

					// vs의 소유권 확인
					if (roleNo.equals(OBDefine.ACCNT_ROLE_VSADMIN) == false
							&& roleNo.equals(OBDefine.ACCNT_ROLE_RSADMIN) == false) {// VS_ADMIN, RS_ADMIN 아니면 모든 vs에
																						// 소유권이 있으므로 소유권 검사없이 allowed
						isVsAllowed = true;
					} else
					// vs를 소유했는지 확인한다.
					{
						isVsAllowed = false;
						for (String vsOwn : vsOwnList) {
							if (vsIndex.equals(vsOwn)) // 권한을 받은 virtual server
							{
								isVsAllowed = true;
								break;
							}
						}
					}

					// node list의 맞는 node를 찾아 vs정보 넣기
					for (i = 0; i < list.size(); i++) {
						currentNode = list.get(i);
						if (currentNode == null) // 그럴리가 없지만 체크
						{
							continue;
						}
						if (currentNode.getIndex().equals(nodeIndex)) // 같은 node이면 vs정보를 넣는다.
						{
							if (isVsAllowed == true) {
								currentNode.getVserverAllowed().add(vsName);
							} else {
								currentNode.getVserverNotAllowed().add(vsName);
							}
							break; // 찾았으니 그만하고 다음~~~
						}
					}
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
			if (db != null) {
				db.closeDB();
			}
		}

		return list;
	}

	private String getNodeListF5OrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY INET(IP_ADDRESS) ASC NULLS LAST ";

		switch (orderType) {
		case OBDefine.ORDER_TYPE_FIRST:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY IP_ADDRESS ASC NULLS LAST ";
			else
				retVal = " ORDER BY IP_ADDRESS DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SECOND:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY NAME ASC NULLS LAST , INET(IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY NAME DESC NULLS LAST , INET(IP_ADDRESS) ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_THIRD:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY RATIO ASC NULLS LAST , INET(IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY RATIO DESC NULLS LAST , INET(IP_ADDRESS) ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_FOURTH:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY STATE ASC NULLS LAST , INET(IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY STATE DESC NULLS LAST , INET(IP_ADDRESS) ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SIXTH:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY STATUS ASC NULLS LAST , INET(IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY STATUS DESC NULLS LAST , INET(IP_ADDRESS) ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SEVENTH:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY ADC_NAME ASC NULLS LAST , INET(IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY ADC_NAME DESC NULLS LAST , INET(IP_ADDRESS) ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_RESPONSE:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY CONN_CURR ASC NULLS LAST , INET(IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY CONN_CURR DESC NULLS LAST , INET(IP_ADDRESS) ASC NULLS LAST ";
			break;
		}
		return retVal;
	}

	private String getVServerListAllOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY BPS_TOT DESC  NULLS LAST ,STATUS_SORT NULLS LAST ,INET(VIRTUAL_IP) NULLS LAST "
				+ ", VS_NAME NULLS LAST ,BPS_IN NULLS LAST ,BPS_OUT NULLS LAST ,CONN_CURR NULLS LAST ,"
				+ "ADC_NAME NULLS LAST ,ADC_TYPE NULLS LAST ";
		switch (orderType) {
		case OBDefine.ORDER_TYPE_STATUS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY STATUS ASC NULLS LAST , VS_NAME ASC  NULLS LAST ";
			else
				retVal = " ORDER BY STATUS DESC NULLS LAST , VS_NAME ASC NULLS LAST ";
			break;
//        case OBDefine.ORDER_TYPE_INDEX:
//            if(orderDir == OBDefine.ORDER_DIR_ASCEND)
//                retVal = " ORDER BY INDEX ASC NULLS LAST , VS_NAME ASC NULLS LAST ";
//            else
//                retVal = " ORDER BY INDEX DESC NULLS LAST , VS_NAME ASC NULLS LAST ";
//            break;
		case OBDefine.ORDER_TYPE_SECOND:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INET(VIRTUAL_IP) ASC NULLS LAST , VS_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY INET(VIRTUAL_IP) DESC NULLS LAST , VS_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_THIRD:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY VIRTUAL_PORT ASC NULLS LAST , VS_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY VIRTUAL_PORT DESC NULLS LAST , VS_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_FOURTH:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY VS_NAME ASC NULLS LAST , INET(VIRTUAL_IP) ASC NULLS LAST ";
			else
				retVal = " ORDER BY VS_NAME DESC NULLS LAST , INET(VIRTUAL_IP) ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SIXTH:// BPS_IN
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY BPS_IN ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, VIRTUAL_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY BPS_IN DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, VIRTUAL_PORT ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SEVENTH:// BPS_OUT
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY BPS_OUT ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, VIRTUAL_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY BPS_OUT DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, VIRTUAL_PORT ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_RESPONSE:// bps total
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY BPS_TOT ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, VIRTUAL_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY BPS_TOT DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, VIRTUAL_PORT ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_STATE:// Concurrent Session
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY CONN_CURR ASC NULLS LAST, INET(VIRTUAL_IP) ASC NULLS LAST ";
			else
				retVal = " ORDER BY CONN_CURR DESC NULLS LAST, INET(VIRTUAL_IP) ASC NULLS LAST ";
			break;

		case OBDefine.ORDER_TYPE_OCCURTIME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY APPLY_TIME ASC NULLS LAST , VS_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY APPLY_TIME DESC NULLS LAST , VS_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_NAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY ADC_NAME ASC NULLS LAST , VS_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY ADC_NAME DESC NULLS LAST , VS_NAME ASC NULLS LAST ";
			break;
		}
		return retVal;
	}

	// 공지 화면
	public ArrayList<OBDtoAdcVServerNotice> getVServerNoticeList(Integer adcIndex, Integer accntIndex, String searchKey,
			boolean bNoticeStatusOn, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("adcIndex:%d, searchKey:%s", adcIndex, searchKey));

		if (searchKey == null) {
			searchKey = "";
		}

		String sqlText = "";

		ResultSet rs;
		ArrayList<OBDtoAdcVServerNotice> list = new ArrayList<OBDtoAdcVServerNotice>();

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			String sqlWhere = "";
			String sqlTextVS = "";
			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accntIndex);
			Integer adcType = new OBAdcManagementImpl().getAdcType(adcIndex);

			if (roleNo.equals(OBDefine.ACCNT_ROLE_VSADMIN) || roleNo.equals(OBDefine.ACCNT_ROLE_RSADMIN)) {
				ArrayList<String> vsList = new OBAccountImpl().getAssignedVSList(adcIndex, accntIndex);
				sqlTextVS = convertSqlVSList(vsList);
				if (sqlTextVS == null || sqlTextVS.isEmpty()) {
					return list;
				} else {
					if (adcType == OBDefine.ADC_TYPE_ALTEON) {
						sqlTextVS = String.format(" VS_INDEX IN (%s) ", sqlTextVS);
					} else {
						sqlTextVS = String.format(" INDEX IN (%s) ", sqlTextVS);
					}

				}
			} else
			// 모든 vs에 권한이 있는 user
			{
				sqlTextVS = " TRUE ";
			}

			if (searchKey.isEmpty() == false) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				if (bNoticeStatusOn == true) {
					sqlWhere = String.format(" AND V.NAME LIKE %s OR V.VIRTUAL_IP LIKE %s \n",
							OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
				} else {
					sqlWhere = String.format(" WHERE V.NAME LIKE %s OR V.VIRTUAL_IP LIKE %s \n",
							OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
				}

			}

			// 페이징
			int offset = 0;
			if (beginIndex != null && beginIndex.intValue() >= 0) {
				offset = beginIndex.intValue();
			}
			int limit = 0;
			String sqlLimit = "";
			if (endIndex != null && endIndex.intValue() >= 0) {
				limit = Math.abs(endIndex.intValue() - offset) + 1;
				sqlLimit = String.format(" OFFSET %d LIMIT %d ", offset, limit);
			}

			if (bNoticeStatusOn == true) // notice 상태인 vs
			{
				if (adcType == OBDefine.ADC_TYPE_ALTEON) {
					sqlText = String.format(
							" SELECT VS.INDEX VINDEX, V.NAME VNAME, VS.STATUS VSTATUS, V.VIRTUAL_IP VIP, VS.VIRTUAL_PORT VPORT,     \n"
									+ "    A.SERVICE_POOL_INDEX SERVICE_POOL_INDEX, B.NAME SERVICE_POOL_NAME, A.ACCNT_INDEX ACCNT_INDEX,    \n"
									+ "    CASE WHEN A.NOTICE_POOL_INDEX IS NULL THEN D.INDEX WHEN A.NOTICE_POOL_INDEX IS NOT NULL THEN A.NOTICE_POOL_INDEX END NOTICE_POOL_INDEX, \n"
									+ "    CASE WHEN A.NOTICE_POOL_INDEX IS NULL THEN D.NAME  WHEN A.NOTICE_POOL_INDEX IS NOT NULL THEN C.NAME END NOTICE_POOL_NAME                \n"
									+ " FROM (SELECT INDEX, VS_INDEX, POOL_INDEX, VIRTUAL_PORT, STATUS FROM TMP_SLB_VS_SERVICE WHERE ADC_INDEX = %d                               \n"
//                                            + " AND POOL_INDEX IN (SELECT POOL_INDEX FROM MNG_NOTICE_GROUP WHERE ADC_INDEX = %d) \n"
									+ " AND %s) VS                                                \n"
									+ " LEFT JOIN (SELECT INDEX, NAME, ALTEON_ID, VIRTUAL_IP FROM TMP_SLB_VSERVER  WHERE ADC_INDEX = %d) V                                       \n"
									+ "    ON VS.VS_INDEX = V.INDEX                                                                                                                \n"
									+ " LEFT JOIN (SELECT * FROM TMP_VS_NOTICE A WHERE ADC_INDEX = %d) A                                                                         \n"
//                                            + " LEFT JOIN (SELECT * FROM TMP_VS_NOTICE A WHERE ADC_INDEX = %d AND ACCNT_INDEX = %d) A                                                                         \n"
									+ "    ON VS.INDEX = A.VS_INDEX                                                                                                                \n"
									+ " LEFT JOIN (SELECT NAME, INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX = %d) B                                                                  \n"
									+ "    ON A.SERVICE_POOL_INDEX = B.INDEX                                                                                                       \n"
									+ " LEFT JOIN (SELECT NAME, INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX = %d) C                                                                  \n"
									+ "    ON A.NOTICE_POOL_INDEX = C.INDEX                                                                                                        \n"
									+ " LEFT JOIN (SELECT NAME, INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX = %d) D                                                                  \n"
									+ "    ON VS.POOL_INDEX = D.INDEX                                                                                                              \n"
									+ " WHERE A.SERVICE_POOL_INDEX IS NOT NULL                                                                                                  \n"
									+ " %s ", // like
							adcIndex, sqlTextVS, adcIndex, adcIndex, adcIndex, adcIndex, adcIndex, sqlWhere,
//                                            adcIndex, adcIndex, sqlTextVS, adcIndex, adcIndex, accntIndex, adcIndex, adcIndex, adcIndex, sqlWhere,
							sqlLimit);
				} else {
					sqlText = String.format(
							" SELECT V.INDEX VINDEX, V.NAME VNAME, V.STATUS VSTATUS, V.VIRTUAL_IP VIP, V.VIRTUAL_PORT VPORT,      \n"
									+ "     A.SERVICE_POOL_INDEX SERVICE_POOL_INDEX, B.NAME SERVICE_POOL_NAME, A.ACCNT_INDEX ACCNT_INDEX, \n"
									+ "     CASE WHEN A.NOTICE_POOL_INDEX IS NULL THEN D.INDEX WHEN A.NOTICE_POOL_INDEX IS NOT NULL THEN A.NOTICE_POOL_INDEX END NOTICE_POOL_INDEX, \n"
									+ "     CASE WHEN A.NOTICE_POOL_INDEX IS NULL THEN D.NAME  WHEN A.NOTICE_POOL_INDEX IS NOT NULL THEN C.NAME END NOTICE_POOL_NAME                \n"
									+ " FROM (SELECT INDEX, NAME, STATUS, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX                             \n"
									+ "        FROM TMP_SLB_VSERVER                                                                        \n"
									+ "        WHERE ADC_INDEX = %d                                                                        \n"
//                                            + "        AND POOL_INDEX IN (SELECT NOTICE_POOL_INDEX FROM TMP_VS_NOTICE WHERE ADC_INDEX = %d) \n"
									+ " AND %s     \n"
//                                            + "        AND POOL_INDEX IN (SELECT NOTICE_POOL_INDEX FROM TMP_VS_NOTICE WHERE ADC_INDEX = %d AND ACCNT_INDEX = %d) AND %s     \n"
									+ "       ) V                                                                                          \n"
									+ " LEFT JOIN (SELECT * FROM TMP_VS_NOTICE A WHERE ADC_INDEX = %d) A                                   \n"
//                                            + " LEFT JOIN (SELECT * FROM TMP_VS_NOTICE A WHERE ADC_INDEX = %d AND ACCNT_INDEX = %d) A                                   \n"
									+ "     ON V.INDEX = A.VS_INDEX                                                                        \n"
									+ " LEFT JOIN (SELECT NAME, INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX = %d) B                            \n"
									+ "     ON A.SERVICE_POOL_INDEX = B.INDEX                                                              \n"
									+ " LEFT JOIN (SELECT NAME, INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX = %d) C                            \n"
									+ "     ON A.NOTICE_POOL_INDEX = C.INDEX                                                               \n"
									+ " LEFT JOIN (SELECT NAME, INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX = %d) D                            \n"
									+ "     ON V.POOL_INDEX = D.INDEX                                                                      \n"
									+ " WHERE A.SERVICE_POOL_INDEX IS NOT NULL                                                             \n"
									+ " %s ", // like
							adcIndex, sqlTextVS, adcIndex, adcIndex, adcIndex, adcIndex, sqlWhere,
//                                            adcIndex, adcIndex, accntIndex, sqlTextVS, adcIndex, accntIndex, adcIndex, adcIndex, adcIndex, sqlWhere,
							sqlLimit);
				}

			} else
			// notice 상태가 아닌 그룹 뽑기
			{
				if (adcType == OBDefine.ADC_TYPE_ALTEON) {
					sqlText = String.format(
							"    SELECT VS.INDEX VINDEX, V.NAME VNAME, VS.STATUS VSTATUS, V.VIRTUAL_IP VIP, VS.VIRTUAL_PORT VPORT,               \n"
									+ " VS.POOL_INDEX SERVICE_POOL_INDEX, P.NAME SERVICE_POOL_NAME                                                      \n"
									+ " FROM ( SELECT INDEX, VS_INDEX, POOL_INDEX, VIRTUAL_PORT, STATUS FROM TMP_SLB_VS_SERVICE WHERE ADC_INDEX = %d    \n"
									+ " AND POOL_INDEX NOT IN (SELECT POOL_INDEX FROM MNG_NOTICE_GROUP WHERE ADC_INDEX = %d) AND %s ) VS                \n"
									+ " LEFT JOIN (SELECT INDEX, NAME, ALTEON_ID, VIRTUAL_IP FROM TMP_SLB_VSERVER  WHERE ADC_INDEX = %d) V              \n"
									+ "    ON VS.VS_INDEX = V.INDEX                                                                                     \n"
									+ " LEFT JOIN (SELECT INDEX, ALTEON_ID, NAME FROM TMP_SLB_POOL  WHERE ADC_INDEX = %d)P                              \n"
									+ "    ON VS.POOL_INDEX = P.INDEX                                                           \n"
									+ " %s ", // where
							adcIndex, adcIndex, sqlTextVS, adcIndex, adcIndex, sqlWhere);
				} else {
					sqlText = String.format(
							" SELECT V.INDEX VINDEX, V.NAME VNAME, V.STATUS VSTATUS, V.VIRTUAL_IP VIP, V.VIRTUAL_PORT VPORT,    \n"
									+ " V.POOL_INDEX SERVICE_POOL_INDEX, B.NAME SERVICE_POOL_NAME                                         \n"
									+ " FROM (SELECT INDEX, NAME, STATUS, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX                            \n"
									+ "       FROM TMP_SLB_VSERVER                                                                        \n"
									+ "       WHERE ADC_INDEX = %d                                                                        \n"
//                                            + "       AND POOL_INDEX NOT IN (SELECT POOL_INDEX FROM MNG_NOTICE_GROUP WHERE ADC_INDEX = %d) "
									+ "       AND INDEX NOT IN (SELECT VS_INDEX FROM TMP_VS_NOTICE WHERE ADC_INDEX = %d) "
									+ "       AND %s \n"
//                                            + "       AND POOL_INDEX NOT IN (SELECT NOTICE_POOL_INDEX FROM TMP_VS_NOTICE WHERE ADC_INDEX = %d) AND %s \n"
									+ "      ) V                                                                                          \n"
									+ " LEFT JOIN (SELECT NAME, INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX = %d) B                           \n"
									+ "      ON V.POOL_INDEX = B.INDEX                                                                    \n"
									+ " %s ", // where
//                                            adcIndex, adcIndex, adcIndex, sqlTextVS, adcIndex, sqlWhere);
							adcIndex, adcIndex, sqlTextVS, adcIndex, sqlWhere);
				}
			}
			// ordering
			sqlText += getNoticeListF5OrderType(orderType, orderDir);
			// paging - offset+limit
			sqlText += sqlLimit;

			// System.out.println("sql="+ sqlText);

			rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcVServerNotice retVal = new OBDtoAdcVServerNotice();
				retVal.setIndex(db.getString(rs, "VINDEX"));
				retVal.setVsStatus(db.getInteger(rs, "VSTATUS"));
				retVal.setVsName(db.getString(rs, "VNAME"));
				retVal.setVirtualIp(db.getString(rs, "VIP"));
				retVal.setServicePort(db.getString(rs, "VPORT"));

				if (bNoticeStatusOn == true) {
					retVal.setAccntIndex(db.getInteger(rs, "ACCNT_INDEX"));
					retVal.setIsNotice(bNoticeStatusOn);
					if (adcType == OBDefine.ADC_TYPE_ALTEON) {

						retVal.setNoticePoolIndex(db.getString(rs, "NOTICE_POOL_INDEX"));
						String noticeIndex[] = retVal.getNoticePoolIndex().split("_");
						retVal.setNoticePoolName(noticeIndex[1] + "(" + db.getString(rs, "NOTICE_POOL_NAME") + ")");
						retVal.setServicePoolIndex(db.getString(rs, "SERVICE_POOL_INDEX"));
						retVal.setServicePoolName(db.getString(rs, "SERVICE_POOL_NAME"));
					} else {
						retVal.setServicePoolIndex(db.getString(rs, "SERVICE_POOL_INDEX"));
						retVal.setServicePoolName(db.getString(rs, "SERVICE_POOL_NAME"));
						retVal.setNoticePoolIndex(db.getString(rs, "NOTICE_POOL_INDEX"));
						retVal.setNoticePoolName(db.getString(rs, "NOTICE_POOL_NAME"));
					}
				} else {
					retVal.setIsNotice(bNoticeStatusOn);
					retVal.setServicePoolIndex(db.getString(rs, "SERVICE_POOL_INDEX"));
					retVal.setServicePoolName(db.getString(rs, "SERVICE_POOL_NAME"));
					retVal.setNoticePoolIndex(null);
					retVal.setNoticePoolName(null);
				}

				if (adcType == OBDefine.ADC_TYPE_ALTEON) {
					String[] vsAlteonID = retVal.getIndex().split("_");
					retVal.setAlteonID(vsAlteonID[2]);

					if (retVal.getServicePoolIndex() != null) {
						String[] poolAlteonID = retVal.getServicePoolIndex().split("_");
						retVal.setServicePoolAlteonID(poolAlteonID[1]);
					} else {
						retVal.setServicePoolAlteonID("-");
					}
				}
				list.add(retVal);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}
		return list;
	}

	public int getVServerNoticeListCount(Integer adcIndex, Integer accntIndex, String searchKey,
			boolean bNoticeStatusOn) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("adcIndex:%d, searchKey:%s", adcIndex, searchKey));

		OBDatabase db = new OBDatabase();

		if (searchKey == null) {
			searchKey = "";
		}

		String sqlText = "";

		ResultSet rs;
		int retVal = 0;
		try {
			db.openDB();
			String sqlTextVS = "";
			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accntIndex);
			Integer adcType = new OBAdcManagementImpl().getAdcType(adcIndex);

			if (roleNo.equals(OBDefine.ACCNT_ROLE_VSADMIN) || roleNo.equals(OBDefine.ACCNT_ROLE_RSADMIN)) {
				ArrayList<String> vsList = new OBAccountImpl().getAssignedVSList(adcIndex, accntIndex);
				sqlTextVS = convertSqlVSList(vsList);
				if (sqlTextVS == null || sqlTextVS.isEmpty()) {
					db.closeDB(); // 할당된 virtual server가 없다.
					return 0;
				} else {
					sqlTextVS = String.format(" INDEX IN (%s) ", sqlTextVS);
				}
			} else
			// 모든 vs에 권한이 있는 user
			{
				sqlTextVS = " TRUE ";
			}
//			String sqlWhere = "";
//			if (searchKey.isEmpty() == false) {
//				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
//				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
//				sqlWhere = String.format(" AND NAME LIKE %s OR VIRTUAL_IP LIKE %s \n", OBParser.sqlString(wildcardKey),
//						OBParser.sqlString(wildcardKey));
//			}
			/*
			 * if(bNoticeStatusOn == true) { if(adcType == OBDefine.ADC_TYPE_ALTEON) {
			 * sqlText = String.format(" SELECT COUNT(INDEX) VCOUNT  \n" +
			 * " FROM TMP_SLB_VS_SERVICE        \n" + " WHERE ADC_INDEX = %d AND %s \n" +
			 * "     AND POOL_INDEX IN (SELECT POOL_INDEX FROM MNG_NOTICE_GROUP WHERE ADC_INDEX = %d        \n"
			 * +
			 * "     AND POOL_INDEX IN (SELECT NOTICE_POOL_INDEX FROM TMP_VS_NOTICE WHERE ADC_INDEX = %d))  \n"
			 * + " %s ", // where adcIndex, sqlTextVS, adcIndex, adcIndex, sqlWhere); } else
			 * { sqlText = String.format(" SELECT COUNT(INDEX) VCOUNT  \n" +
			 * " FROM TMP_SLB_VSERVER        \n" + " WHERE ADC_INDEX = %d AND %s \n" // +
			 * "     AND POOL_INDEX IN (SELECT POOL_INDEX FROM MNG_NOTICE_GROUP WHERE ADC_INDEX = %d        \n"
			 * // +
			 * "     AND POOL_INDEX IN (SELECT NOTICE_POOL_INDEX FROM TMP_VS_NOTICE WHERE ADC_INDEX = %d))  \n"
			 * +
			 * "     AND POOL_INDEX IN (SELECT NOTICE_POOL_INDEX FROM TMP_VS_NOTICE WHERE ADC_INDEX = %d)  \n"
			 * + " %s ", // where // adcIndex, sqlTextVS, adcIndex, adcIndex, sqlWhere);
			 * adcIndex, sqlTextVS, adcIndex, sqlWhere); }
			 * 
			 * } else { if(adcType == OBDefine.ADC_TYPE_ALTEON) { sqlText = String
			 * .format(" SELECT COUNT(INDEX) VCOUNT  \n" + " FROM TMP_SLB_VS_SERVICE   \n" +
			 * " WHERE ADC_INDEX = %d AND %s \n" +
			 * "     AND POOL_INDEX NOT IN (SELECT POOL_INDEX FROM MNG_NOTICE_GROUP WHERE ADC_INDEX = %d)  \n"
			 * + " %s ", // where adcIndex, sqlTextVS, adcIndex, sqlWhere); } else { sqlText
			 * = String .format(" SELECT COUNT(INDEX) VCOUNT  \n" +
			 * " FROM TMP_SLB_VSERVER        \n" + " WHERE ADC_INDEX = %d AND %s \n" // +
			 * "     AND POOL_INDEX NOT IN (SELECT POOL_INDEX FROM MNG_NOTICE_GROUP WHERE ADC_INDEX = %d)  \n"
			 * + " %s ", // where adcIndex, sqlTextVS, sqlWhere); } }
			 */
			if (bNoticeStatusOn == true) // notice 상태인 vs
			{
				if (adcType == OBDefine.ADC_TYPE_ALTEON) {
					sqlText = String.format(" SELECT COUNT(V.INDEX) VCOUNT  \n"
//                                            + "    CASE WHEN A.NOTICE_POOL_INDEX IS NULL THEN D.NAME  WHEN A.NOTICE_POOL_INDEX IS NOT NULL THEN C.NAME END NOTICE_POOL_NAME                \n"
							+ " FROM (SELECT INDEX, VS_INDEX, POOL_INDEX, VIRTUAL_PORT, STATUS FROM TMP_SLB_VS_SERVICE WHERE ADC_INDEX = %d                               \n"
							+ " AND POOL_INDEX IN (SELECT POOL_INDEX FROM MNG_NOTICE_GROUP WHERE ADC_INDEX = %d) AND %s) VS                                                \n"
							+ " LEFT JOIN (SELECT INDEX, NAME, ALTEON_ID, VIRTUAL_IP FROM TMP_SLB_VSERVER  WHERE ADC_INDEX = %d) V                                       \n"
							+ "    ON VS.VS_INDEX = V.INDEX                                                                                                                \n"
							+ " LEFT JOIN (SELECT * FROM TMP_VS_NOTICE A WHERE ADC_INDEX = %d) A                                                                         \n"
							+ "    ON VS.INDEX = A.VS_INDEX                                                                                                                \n"
							+ " LEFT JOIN (SELECT NAME, INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX = %d) B                                                                  \n"
							+ "    ON A.SERVICE_POOL_INDEX = B.INDEX                                                                                                       \n"
							+ " LEFT JOIN (SELECT NAME, INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX = %d) C                                                                  \n"
							+ "    ON A.NOTICE_POOL_INDEX = C.INDEX                                                                                                        \n"
							+ " LEFT JOIN (SELECT NAME, INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX = %d) D                                                                  \n"
							+ "    ON VS.POOL_INDEX = D.INDEX                                                                                                              \n"
							+ " WHERE A.SERVICE_POOL_INDEX IS NOT NULL    ", // like
							adcIndex, adcIndex, sqlTextVS, adcIndex, adcIndex, adcIndex, adcIndex, adcIndex);
				} else {
					sqlText = String.format(" SELECT COUNT(V.INDEX) VCOUNT  \n"
							+ " FROM (SELECT INDEX, NAME, STATUS, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX                             \n"
							+ "        FROM TMP_SLB_VSERVER                                                                        \n"
							+ "        WHERE ADC_INDEX = %d                                                                        \n"
//                                            + "        AND POOL_INDEX IN (SELECT NOTICE_POOL_INDEX FROM TMP_VS_NOTICE WHERE ADC_INDEX = %d) "
							+ "        AND %s     \n"
							+ "       ) V                                                                                          \n"
							+ " LEFT JOIN (SELECT * FROM TMP_VS_NOTICE A WHERE ADC_INDEX = %d) A                                   \n"
							+ "     ON V.INDEX = A.VS_INDEX                                                                        \n"
							+ " LEFT JOIN (SELECT NAME, INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX = %d) B                            \n"
							+ "     ON A.SERVICE_POOL_INDEX = B.INDEX                                                              \n"
							+ " LEFT JOIN (SELECT NAME, INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX = %d) C                            \n"
							+ "     ON A.NOTICE_POOL_INDEX = C.INDEX                                                               \n"
							+ " LEFT JOIN (SELECT NAME, INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX = %d) D                            \n"
							+ "     ON V.POOL_INDEX = D.INDEX                                                                      \n"
							+ " WHERE A.SERVICE_POOL_INDEX IS NOT NULL                                                             ",
							adcIndex, sqlTextVS, adcIndex, adcIndex, adcIndex, adcIndex);
				}

			} else
			// notice 상태가 아닌 그룹 뽑기
			{
				if (adcType == OBDefine.ADC_TYPE_ALTEON) {
					sqlText = String.format(" SELECT COUNT(V.INDEX) VCOUNT  \n"
							+ " FROM ( SELECT INDEX, VS_INDEX, POOL_INDEX, VIRTUAL_PORT, STATUS FROM TMP_SLB_VS_SERVICE WHERE ADC_INDEX = %d    \n"
							+ " AND POOL_INDEX NOT IN (SELECT POOL_INDEX FROM MNG_NOTICE_GROUP WHERE ADC_INDEX = %d) AND %s ) VS                \n"
							+ " LEFT JOIN (SELECT INDEX, NAME, ALTEON_ID, VIRTUAL_IP FROM TMP_SLB_VSERVER  WHERE ADC_INDEX = %d) V              \n"
							+ "    ON VS.VS_INDEX = V.INDEX                                                                                     \n"
							+ " LEFT JOIN (SELECT INDEX, ALTEON_ID, NAME FROM TMP_SLB_POOL  WHERE ADC_INDEX = %d)P                              \n"
							+ "    ON VS.POOL_INDEX = P.INDEX  ", adcIndex, adcIndex, sqlTextVS, adcIndex, adcIndex);
				} else {
					sqlText = String.format(" SELECT COUNT(V.INDEX) VCOUNT  \n"
							+ " FROM (SELECT INDEX, NAME, STATUS, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX                            \n"
							+ "       FROM TMP_SLB_VSERVER                                                                        \n"
							+ "       WHERE ADC_INDEX = %d                                                                        \n"
							+ "       AND INDEX NOT IN (SELECT VS_INDEX FROM TMP_VS_NOTICE WHERE ADC_INDEX = %d) "
							+ "       AND %s \n"
							+ "      ) V                                                                                          \n"
							+ " LEFT JOIN (SELECT NAME, INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX = %d) B                           \n"
							+ "      ON V.POOL_INDEX = B.INDEX", adcIndex, adcIndex, sqlTextVS, adcIndex);
				}
			}
			// System.out.println("sql="+ sqlText);
			rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				retVal = db.getInteger(rs, "VCOUNT");
			}
			return retVal;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}
	}

	private String getNoticeListF5OrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY VNAME ASC NULLS LAST ";
		switch (orderType) {
		case OBDefine.ORDER_TYPE_STATUS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY VSTATUS ASC NULLS LAST , VNAME ASC  NULLS LAST ";
			else
				retVal = " ORDER BY VSTATUS DESC NULLS LAST , VNAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_VSIPADDRESS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INET(V.VIRTUAL_IP) ASC NULLS LAST , VNAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY INET(V.VIRTUAL_IP) DESC NULLS LAST , VNAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SERVICEPORT:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY VPORT ASC NULLS LAST , VNAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY VPORT DESC NULLS LAST , VNAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_FIRST:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY SERVICE_POOL_NAME ASC NULLS LAST , VNAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY SERVICE_POOL_NAME DESC NULLS LAST , VNAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SECOND:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY NOTICE_POOL_NAME ASC NULLS LAST , VNAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY NOTICE_POOL_NAME DESC NULLS LAST , VNAME ASC NULLS LAST ";
			break;
		}
		return retVal;
	}

	// 공지 그룹 지정 - MNG_NOTICE_GROUP 테이블에 insert 및 delete
	void setNoticeGroup(Integer adcIndex, ArrayList<OBDtoAdcNoticeGroup> noticeGroupList, Integer accntIndex,
			OBDtoExtraInfo extraInfo) throws OBException {
		String sqlText = "";

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String subText = "";
			sqlText = String.format(" DELETE FROM MNG_NOTICE_GROUP WHERE ADC_INDEX = %d AND ACCNT_INDEX = %d ",
					adcIndex, accntIndex);

			db.executeUpdate(sqlText);

			sqlText = String
					.format(" INSERT INTO MNG_NOTICE_GROUP " + " (ADC_INDEX, POOL_INDEX, ACCNT_INDEX) " + " VALUES ");

			for (OBDtoAdcNoticeGroup noticeGroup : noticeGroupList) {
				if (!noticeGroup.getPoolIndex().equals(OBDefine.PORT_NA_STR)) {
					subText += String.format("(%d, %s, %d)",
							// adcIndex,
							noticeGroup.getAdcIndex(), OBParser.sqlString(noticeGroup.getPoolIndex()), accntIndex);
				}
			}

			if (!subText.isEmpty()) {
				sqlText += subText + ";";
				db.executeUpdate(sqlText);
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
	}

	// 그룹 전체 목록을 가져온다.
	ArrayList<OBDtoAdcNoticeGroup> getNoticeGrpList(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		ResultSet rs;
		ArrayList<OBDtoAdcNoticeGroup> list = new ArrayList<OBDtoAdcNoticeGroup>();
		Integer adcType = new OBAdcManagementImpl().getAdcType(adcIndex);

		try {
			db.openDB();

			sqlText = String.format(
					" SELECT NOTICEGRP.ADC_INDEX, NOTICEGRP.POOL_INDEX, POOL.NAME POOL_NAME, NOTICEGRP.ACCNT_INDEX     \n"
							+ " FROM MNG_NOTICE_GROUP NOTICEGRP, TMP_SLB_POOL POOL                           \n"
							+ " WHERE NOTICEGRP.ADC_INDEX = POOL.ADC_INDEX                                    \n"
							+ " AND NOTICEGRP.POOL_INDEX = POOL.INDEX                                         \n"
							+ " AND NOTICEGRP.ADC_INDEX = %d ",
					adcIndex);
//            sqlText =
//                    String.format(" SELECT VSNOTICE.ADC_INDEX, VSNOTICE.NOTICE_POOL_INDEX, POOL.NAME POOL_NAME     \n"
//                            + " FROM TMP_VS_NOTICE VSNOTICE, TMP_SLB_POOL POOL                           \n"
//                            + " WHERE VSNOTICE.ADC_INDEX = POOL.ADC_INDEX                                    \n"
//                            + " AND VSNOTICE.NOTICE_POOL_INDEX = POOL.INDEX                                         \n"
//                            + " AND VSNOTICE.ADC_INDEX = %d ", adcIndex);	
			rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcNoticeGroup retVal = new OBDtoAdcNoticeGroup();
				retVal.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				retVal.setPoolIndex(db.getString(rs, "POOL_INDEX"));
				retVal.setPoolName(db.getString(rs, "POOL_NAME"));
				retVal.setAccntIndex(db.getInteger(rs, "ACCNT_INDEX"));
				if (adcType == OBDefine.ADC_TYPE_ALTEON) {
					String index[] = retVal.getPoolIndex().split("_");
					retVal.setAlteonID(index[1]);
				}

				list.add(retVal);
			}

		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}
		return list;
	}

	// /**
	// * searchNodeListF5() 테스트 함수
	// */
	// public void searchNodeListF5Test()
	// {
	// Integer adcIndex = 3;
	// Integer accntIndex = 4;
	// String searchKey = "";
	// Integer beginIndex = null;
	// Integer endIndex = null;
	// Integer orderType = 0;
	// Integer orderDir = 0;
	// try
	// {
	// ArrayList<OBDtoAdcNodeF5Detail> result = searchNodeListF5(adcIndex,
	// accntIndex, searchKey, beginIndex, endIndex, orderType, orderDir);
	// for(OBDtoAdcNodeF5Detail node:result)
	// {
	// System.out.println(String.format("node = %s -- allowed: %s -- not allowed :
	// %s", node.getNodeInfo().getIpAddress()
	// , node.getVserverAllowed(), node.getVserverNotAllowed()));
	// }
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	public Integer searchNodeF5ListCountCore(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, searchKey:%s", adcIndex, searchKey));

		OBDatabase db = new OBDatabase();

		if (searchKey == null) {
			searchKey = "";
		}

		String sqlText = "";
		int retVal = 0;

		try {
			db.openDB();

			String sqlSearch = "";
			if (searchKey != null && !searchKey.isEmpty()) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlSearch = String.format(" ( NAME LIKE %s OR IP_ADDRESS LIKE %s ) ", OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			}

			// sqlText = String.format(" SELECT COUNT(INDEX) AS CNT FROM TMP_SLB_NODE WHERE
			// ADC_INDEX = %d ", adcIndex);

			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accntIndex);

			if (roleNo == OBDefine.ACCNT_ROLE_VSADMIN || roleNo == OBDefine.ACCNT_ROLE_RSADMIN) {
				ArrayList<String> vsList = new OBAccountImpl().getAssignedVSList(adcIndex, accntIndex);
				ArrayList<String> rsList = new OBAccountImpl().getAssignedRSList(accntIndex);
				String sqlTextRS = convertSqlRSList(rsList);
				String sqlTextVS = convertSqlVSList(vsList);
				if (sqlTextVS == null || sqlTextVS.isEmpty() == true) {
					OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", retVal));
					return retVal;
				}
				sqlText = String.format(" SELECT COUNT(INDEX) AS CNT " + " FROM TMP_SLB_NODE "
						+ " WHERE ADC_INDEX = %d "
						+ " AND INDEX IN (%s) OR INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER "
						+ " WHERE POOL_INDEX IN ( SELECT POOL_INDEX FROM TMP_SLB_VSERVER " + " WHERE INDEX IN (%s))) ",
						adcIndex, sqlTextRS, sqlTextVS);
			} else {
				sqlText = String.format(" SELECT COUNT(INDEX) AS CNT " + " FROM TMP_SLB_NODE "
						+ " WHERE ADC_INDEX = %d " + " AND INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER    "
						+ " WHERE ADC_INDEX = %d) ", adcIndex, adcIndex);
			}

			if (sqlSearch != null && !sqlSearch.isEmpty()) {
				sqlText += " AND " + sqlSearch;
			}

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				retVal = db.getInteger(rs, "CNT");
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

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", retVal));
		return retVal;
	}

	public Integer searchNodeAlteonListCountCore(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, searchKey:%s", adcIndex, searchKey));

		OBDatabase db = new OBDatabase();

		if (searchKey == null) {
			searchKey = "";
		}

		String sqlText = "";
		int retVal = 0;

		try {
			db.openDB();

			String sqlSearch = "";
			if (searchKey != null && !searchKey.isEmpty()) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlSearch = String.format(" ( NAME LIKE %s OR IP_ADDRESS LIKE %s ) ", OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			}

			// sqlText = String.format(" SELECT COUNT(INDEX) AS CNT FROM TMP_SLB_NODE WHERE
			// ADC_INDEX = %d ", adcIndex);

			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accntIndex);

			if (roleNo == OBDefine.ACCNT_ROLE_VSADMIN || roleNo == OBDefine.ACCNT_ROLE_RSADMIN) {
				ArrayList<String> vsList = new OBAccountImpl().getAssignedVSList(adcIndex, accntIndex);
				ArrayList<String> rsList = new OBAccountImpl().getAssignedRSList(accntIndex);
				String sqlTextRS = convertSqlRSList(rsList);
				String sqlTextVS = convertSqlVSList(vsList);
				if (sqlTextVS == null || sqlTextVS.isEmpty() == true) {
					OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", retVal));
					return retVal;
				}
				sqlText = String
						.format(" SELECT COUNT(INDEX) AS CNT " + " FROM TMP_SLB_NODE " + " WHERE ADC_INDEX = %d "
								+ " AND INDEX IN (%s) OR INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER "
								+ " WHERE POOL_INDEX IN ( SELECT POOL_INDEX FROM TMP_SLB_VS_SERVICE "
								+ " WHERE INDEX IN (%s))) ", adcIndex, sqlTextRS, sqlTextVS);
			} else {
				sqlText = String.format(" SELECT COUNT(INDEX) AS CNT " + " FROM TMP_SLB_NODE "
						+ " WHERE ADC_INDEX = %d " + " AND INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER    "
						+ " WHERE ADC_INDEX = %d) ", adcIndex, adcIndex);
			}

			if (sqlSearch != null && !sqlSearch.isEmpty()) {
				sqlText += " AND " + sqlSearch;
			}

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				retVal = db.getInteger(rs, "CNT");
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

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", retVal));
		return retVal;
	}

	public Integer searchNodeAllListCountCore(OBDtoAdcScope scope, Integer accntIndex, String searchKey)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. scope:%s, searchKey:%s", scope, searchKey));

		OBDatabase db = new OBDatabase();
		String sqlText = "";
		String sqlTextAlteonList = "";
		String sqlTextF5PASPASKList = "";
		String sqlTextRS = "";
		String sqlTextVSService = "";
		String sqlTextVS = "";
		int retVal = 0;
		try {
			db.openDB();
			ArrayList<Integer> adcIndexList = new OBAdcManagementImpl().getUsersAdcIndexInteger(scope.getLevel(),
					scope.getIndex(), accntIndex);

			String sqlSearch = "";
			if (searchKey != null && !searchKey.isEmpty()) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlSearch = String.format(" ( NAME LIKE %s OR IP_ADDRESS LIKE %s ) ", OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			}

			String adcAllList = OBParser.convertSqlGrpIndexList(adcIndexList);

			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accntIndex);

			if (searchKey == null) {
				searchKey = "";
			}

			for (int i = 0; i < adcIndexList.size(); i++) {
				int adcType = new OBAdcManagementImpl().getAdcType(adcIndexList.get(i));

				if (adcType == OBDefine.ADC_TYPE_ALTEON) {
					sqlTextAlteonList += adcIndexList.get(i);
					sqlTextAlteonList += ",";
				} else {
					sqlTextF5PASPASKList += adcIndexList.get(i);
					sqlTextF5PASPASKList += ",";
				}
			}

			if (roleNo == OBDefine.ACCNT_ROLE_VSADMIN || roleNo == OBDefine.ACCNT_ROLE_RSADMIN) {
				for (int i = 0; i < adcIndexList.size(); i++) {
					int adcType = new OBAdcManagementImpl().getAdcType(adcIndexList.get(i));

					if (adcType == OBDefine.ADC_TYPE_ALTEON) {
						ArrayList<String> vsList = new OBAccountImpl().getAssignedVSServiceList(adcIndexList.get(i),
								accntIndex);
						sqlTextVSService += convertSqlVSList(vsList);
					} else {
						ArrayList<String> vsList = new OBAccountImpl().getAssignedVSList(adcIndexList.get(i),
								accntIndex);
						ArrayList<String> rsList = new OBAccountImpl().getAssignedRSList(adcIndexList.get(i));
						sqlTextRS += convertSqlRSList(rsList);
						sqlTextVS += convertSqlVSList(vsList);
					}
				}

				if ((sqlTextVS == null || sqlTextVS.isEmpty())
						&& (sqlTextVSService == null || sqlTextVSService.isEmpty())) {
					db.closeDB();
					OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result list count:%d", retVal));
					return retVal;
				}

				if (sqlTextF5PASPASKList != "") {
					sqlTextF5PASPASKList = sqlTextF5PASPASKList.substring(0, sqlTextF5PASPASKList.length() - 1);

					sqlText = String.format(
							" SELECT COUNT(INDEX) AS CNT                                                        \n"
									+ " FROM TMP_SLB_NODE WHERE ADC_INDEX IN (%s)                                       \n"
									+ " AND INDEX IN (%s) OR INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER        \n"
									+ " WHERE POOL_INDEX IN ( SELECT POOL_INDEX FROM TMP_SLB_VSERVER                    \n"
									+ " WHERE INDEX IN (%s)))                                                           \n",
							sqlTextF5PASPASKList, sqlTextRS, sqlTextVS);
				}
				if (sqlTextF5PASPASKList != "" && sqlTextAlteonList != "") {
					sqlText += " UNION                                                                                       \n";
				}
				if (sqlTextAlteonList != "") {
					sqlTextAlteonList = sqlTextAlteonList.substring(0, sqlTextAlteonList.length() - 1);

					sqlText += String.format(
							" SELECT COUNT(A.INDEX) AS CNT FROM                                                                 \n"
									+ " (SELECT INDEX, IP_ADDRESS, STATE, STATUS, ALTEON_ID, NAME, RATIO, ADC_INDEX                     \n"
									+ " FROM TMP_SLB_NODE WHERE ADC_INDEX IN (%s)                                                       \n"
									+ " AND INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER                                         \n"
									+ " WHERE POOL_INDEX IN ( SELECT POOL_INDEX FROM TMP_SLB_VS_SERVICE                                 \n"
									+ " WHERE INDEX IN (%s)))) A                                                                        \n"
									+ " LEFT JOIN (SELECT INDEX, NAME, TYPE, STATUS, OP_MODE FROM MNG_ADC WHERE INDEX IN(%s)) B         \n"
									+ " ON A.ADC_INDEX = B.INDEX                                                                        \n",
							sqlTextAlteonList, sqlTextVSService, sqlTextAlteonList);
				}
			} else {
				sqlText = String.format(
						" SELECT COUNT(INDEX) AS CNT FROM TMP_SLB_NODE WHERE ADC_INDEX IN (%s)                      \n"
								+ " AND INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER                                 \n"
								+ " WHERE ADC_INDEX IN (%s))",
						adcAllList, adcAllList, adcAllList);
			}

			if (sqlSearch != null && !sqlSearch.isEmpty()) {
				sqlText += " AND " + sqlSearch;
			}

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				retVal += db.getInteger(rs, "CNT");
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

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", retVal));
		return retVal;
	}

	/**
	 * searchNodeListCountCore() 테스트 함수
	 */
	// public void searchNodeListCountCoreTest()
	// {
	// Integer accntIndex = 5;
	// Integer adcIndex = 3;
	// String searchKey = "";
	// try
	// {
	// Integer result = searchNodeListCountCore(accntIndex, adcIndex, searchKey);
	// // System.out.println("node count = " + result);
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }
	public ArrayList<OBDtoAdcPoolMemberAlteon> getNodeBackupInfo(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();

		ArrayList<OBDtoAdcPoolMemberAlteon> info;
		try {
			db.openDB();
			info = getNodeInfoBackupAlteon(adcIndex, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return info;
	}

	// public OBDtoAdcNodeAlteon getNodeInfo(Integer adcIndex, String ipAddress)
	// throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	// OBDtoAdcNodeAlteon info;
	// try
	// {
	// db.openDB();
	// info = getNodeInfoAlteon(adcIndex, ipAddress, db);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// return info;
	// }

	/**
	 * 지정된 ADC 장비에 할당된 node목록을 DB에서 가져온다.
	 * 
	 * @param adcInidex -- ADC 장비 index *
	 * @param db        -- DB 인스턴스.
	 * @return ArrayList<OBDtoAdcNode>
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	// public OBDtoAdcNodeAlteon getNodeInfoAlteon(Integer adcIndex, String
	// ipAddress, OBDatabase db) throws OBException
	// {
	// String sqlText="";
	//
	// try
	// {
	// sqlText = String.format(" SELECT INDEX, ADC_INDEX, ALTEON_ID, IP_ADDRESS,
	// NAME, STATE, EXTRA " +
	// " FROM TMP_SLB_NODE " +
	// " WHERE ADC_INDEX=%d AND IP_ADDRESS=%s;",
	// adcIndex,
	// OBParser.sqlString(ipAddress));
	//
	// ResultSet rs = db.executeQuery(sqlText);
	//
	// if(rs.next() == false)
	// {
	// return null;
	// }
	//
	// OBDtoAdcNodeAlteon obj = new OBDtoAdcNodeAlteon();
	// obj.setIndex(db.getString(rs, "INDEX"));
	// obj.setAlteonId(db.getInteger(rs, "ALTEON_ID"));
	// obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
	// obj.setName(db.getString(rs, "NAME"));
	// obj.setState(db.getInteger(rs, "STATE"));
	// obj.setExtra(db.getString(rs, "EXTRA"));
	// return obj;
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	// db.openDB();
	// db2.openDB();
	// OBDtoAdcNodePAS list = new OBVServerDB().getNodeInfoPAS(5, "10.10.10.1", db);
	// System.out.println(list);
	// db.closeDB();
	// db2.closeDB();
	// }
	// // catch(SQLException e)
	// // {
	// //
	// // }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	// public OBDtoAdcNodeF5 getNodeInfoF5(Integer adcIndex, String ipAddress,
	// OBDatabase db) throws OBException
	// {
	// String sqlText="";
	//
	// try
	// {
	// sqlText = String.format(" SELECT INDEX, IP_ADDRESS, STATE, NAME, RATIO " +
	// " FROM TMP_SLB_NODE " +
	// " WHERE ADC_INDEX=%d AND IP_ADDRESS=%s;",
	// adcIndex,
	// OBParser.sqlString(ipAddress));
	//
	// ResultSet rs = db.executeQuery(sqlText);
	//
	// if(rs.next() == false)
	// {
	// return null;
	// }
	//
	// OBDtoAdcNodeF5 obj = new OBDtoAdcNodeF5();
	// obj.setIndex(db.getString(rs, "INDEX"));
	// obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
	// obj.setState(db.getInteger(rs, "STATE"));
	// obj.setName(db.getString(rs, "NAME"));
	// obj.setRatio(db.getInteger(rs, "RATIO"));
	// return obj;
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// public OBDtoAdcNodePAS getNodeInfoPAS(Integer adcIndex, String ipAddress,
	// OBDatabase db) throws OBException
	// {
	// String sqlText="";
	//
	// try
	// {
	// sqlText = String.format(" SELECT INDEX, ADC_INDEX, ALTEON_ID, IP_ADDRESS,
	// NAME, STATE " +
	// " FROM TMP_SLB_NODE " +
	// " WHERE ADC_INDEX=%d AND IP_ADDRESS=%s;",
	// adcIndex,
	// OBParser.sqlString(ipAddress));
	//
	// ResultSet rs = db.executeQuery(sqlText);
	//
	// if(rs.next() == false)
	// {
	// return null;
	// }
	//
	// OBDtoAdcNodePAS obj = new OBDtoAdcNodePAS();
	// obj.setDbIndex(db.getString(rs, "INDEX"));
	// obj.setId(db.getInteger(rs, "ALTEON_ID"));
	// obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
	// obj.setName(db.getString(rs, "NAME"));
	// obj.setState(db.getInteger(rs, "STATE"));
	// return obj;
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }
	/**
	 * F5 node 정보를 구한다. 조건 : node 인덱스 목록. 복수로 뽑을 경우도 있어서 ArrayList로 함.
	 * 
	 * @param nodeIndexList
	 * @param db
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcNodeF5> getNodeInfoF5(ArrayList<String> nodeIndexList) throws OBException {
		String sqlText = "";
		String whereNodeIndexList = OBParser.convertList2SingleQuotedString(nodeIndexList);
		final OBDatabase db = new OBDatabase();
		try {
			sqlText = String.format(
					" SELECT INDEX, IP_ADDRESS, STATE, NAME, RATIO" + " FROM TMP_SLB_NODE " + " WHERE INDEX IN (%s) ",
					whereNodeIndexList);

			db.openDB();
			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoAdcNodeF5> list = new ArrayList<OBDtoAdcNodeF5>();
			while (rs.next()) {
				OBDtoAdcNodeF5 obj = new OBDtoAdcNodeF5();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				obj.setState(db.getInteger(rs, "STATE"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setRatio(db.getInteger(rs, "RATIO"));
				list.add(obj);
			}
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

	/**
	 * PASK node 정보를 구한다. 조건 : adcIndex, IP, port, state가 일치
	 * 
	 * @param adcIndex
	 * @param ipAddress
	 * @param port
	 * @param state
	 * @param db
	 * @return
	 * @throws OBException
	 */
	OBDtoAdcNodePASK getNodeInfoPASK(Integer adcIndex, String ipAddress, Integer port, Integer state, OBDatabase db)
			throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(
					" SELECT INDEX, ADC_INDEX, ALTEON_ID, IP_ADDRESS, PORT, NAME, STATE " + " FROM TMP_SLB_NODE "
							+ " WHERE ADC_INDEX=%d AND IP_ADDRESS=%s AND PORT=%d AND STATE=%d "
							+ " ORDER BY ALTEON_ID ", // PASK는 real이 중복 상관없이 마구 들어가므로 같은 ip/port/state인 real이 여러개 있을 수
														// 있다.
					adcIndex, OBParser.sqlString(ipAddress), port, state);

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return null;
			}

			OBDtoAdcNodePASK obj = new OBDtoAdcNodePASK();
			obj.setDbIndex(db.getString(rs, "INDEX"));
			obj.setId(db.getInteger(rs, "ALTEON_ID"));
			obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
			obj.setPort(db.getInteger(rs, "PORT"));
			obj.setName(db.getString(rs, "NAME"));
			obj.setState(db.getInteger(rs, "STATE"));
			return obj;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	/**
	 * PASK node 정보를 구한다. 조건 : node(real)의 id
	 * 
	 * @param adcIndex
	 * @param ipAddress
	 * @param port
	 * @param state
	 * @param db
	 * @return
	 * @throws OBException
	 */
	OBDtoAdcNodePASK getNodeInfoPASK(Integer adcIndex, Integer id, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(" SELECT INDEX, ADC_INDEX, ALTEON_ID, IP_ADDRESS, PORT, NAME, STATE "
					+ " FROM TMP_SLB_NODE " + " WHERE ADC_INDEX=%d AND ALTEON_ID=%s ", adcIndex,
					OBParser.sqlString(id.toString()));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return null;
			}

			OBDtoAdcNodePASK obj = new OBDtoAdcNodePASK();
			obj.setDbIndex(db.getString(rs, "INDEX"));
			obj.setId(db.getInteger(rs, "ALTEON_ID"));
			obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
			obj.setPort(db.getInteger(rs, "PORT"));
			obj.setName(db.getString(rs, "NAME"));
			obj.setState(db.getInteger(rs, "STATE"));
			return obj;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private ArrayList<OBDtoAdcPoolMemberAlteon> getNodeInfoBackupAlteon(Integer adcIndex, OBDatabase db)
			throws OBException {
		ArrayList<OBDtoAdcPoolMemberAlteon> result = new ArrayList<OBDtoAdcPoolMemberAlteon>();
		String sqlText = "";
		try {
			sqlText = String.format(
					" SELECT INDEX, IP_ADDRESS, ALTEON_ID, STATE, STATUS                                                    "
							+ " FROM TMP_SLB_NODE WHERE ADC_INDEX = %d AND NOT BAK_TYPE = %d                              "
							+ " OR INDEX IN (SELECT BAK_ID FROM TMP_SLB_NODE WHERE ADC_INDEX = %d AND NOT BAK_TYPE = %d)  "
							+ " OR INDEX IN (SELECT BAK_ID FROM TMP_SLB_POOL WHERE ADC_INDEX = %d AND BAK_TYPE = %d)      ",
					adcIndex, OBDefine.BACKUP_STATE.EMPTY, adcIndex, OBDefine.BACKUP_STATE.EMPTY, adcIndex,
					OBDefine.BACKUP_STATE.REALBAK);
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcPoolMemberAlteon obj = new OBDtoAdcPoolMemberAlteon();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				obj.setPort(0);
				obj.setAlteonNodeID(db.getString(rs, "ALTEON_ID"));
				obj.setState(db.getInteger(rs, "STATE"));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				obj.setExtra("");
				obj.setBackupType(0);
				obj.setBackupId("");
				result.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return result;
	}

	public OBDtoAdcNodeAlteon getNodeInfoAlteon(Integer adcIndex, String alteonID) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX, ALTEON_ID, IP_ADDRESS, NAME, STATE, EXTRA " + " FROM TMP_SLB_NODE "
					+ " WHERE ADC_INDEX=%d AND ALTEON_ID=%s;", adcIndex, OBParser.sqlString(alteonID));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return null;
			}

			OBDtoAdcNodeAlteon obj = new OBDtoAdcNodeAlteon();
			obj.setIndex(db.getString(rs, "INDEX"));
			obj.setAlteonId(db.getString(rs, "ALTEON_ID"));
			obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
			obj.setName(db.getString(rs, "NAME"));
			obj.setState(db.getInteger(rs, "STATE"));
			obj.setExtra(db.getString(rs, "EXTRA"));
			return obj;
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

	// public OBDtoAdcNodePAS getNodeInfoPAS(Integer adcIndex, String vsName,
	// Integer id, OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(" SELECT INDEX, ALTEON_ID, IP_ADDRESS, NAME, STATE "
	// +
	// " FROM TMP_SLB_NODE " +
	// " WHERE ADC_INDEX=%d AND ALTEON_ID=%d;",
	// adcIndex,
	// id);
	//
	// ResultSet rs = db.executeQuery(sqlText);
	// if(rs.next() == false)
	// {
	// return null;
	// }
	//
	// OBDtoAdcNodePAS obj = new OBDtoAdcNodePAS();
	// obj.setDbIndex(db.getString(rs, "INDEX"));
	// obj.setId(db.getInteger(rs, "ALTEON_ID"));// id 필드임. 동일하게 사용함.
	// obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
	// obj.setName(db.getString(rs, "NAME"));
	// obj.setState(db.getInteger(rs, "STATE"));
	// return obj;
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// public void addNodeInfoAlteon(Integer adcIndex, ArrayList<OBDtoAdcNodeAlteon>
	// nodeList, OBDatabase db) throws OBException
	// {
	// String sqlText=String.format("INSERT INTO TMP_SLB_NODE " +
	// "(INDEX, ADC_INDEX, ALTEON_ID, IP_ADDRESS, NAME, STATE, STATUS, BAK_TYPE,
	// BAK_ID, EXTRA, RATIO) " +
	// "VALUES ");
	// try
	// {
	// String subText="";
	// String bakIDText="";
	// for(OBDtoAdcNodeAlteon obj:nodeList)
	// {
	// if(obj.getBakId().equals(OBDefine.BACKUP_STATE.EMPTY))
	// {
	// bakIDText = obj.getBakId();
	// }
	// else
	// {
	// bakIDText = OBParser.sqlString(OBCommon.makePoolBackupIndexAlteon(adcIndex,
	// obj.getBakId()));
	// }
	// if(!subText.isEmpty())
	// subText += ", ";
	// String index=OBCommon.makeNodeIndexAlteon(adcIndex, obj.getAlteonId());
	// subText += String.format("(%s, %d, %d, %s, %s, %d, %d, %d, %s, %s, %d)",
	// OBParser.sqlString(index),
	// adcIndex,
	// obj.getAlteonId(),
	// OBParser.sqlString(obj.getIpAddress()),
	// OBParser.sqlString(obj.getName()),
	// obj.getState(),
	// obj.getStatus(),
	// obj.getBakType(),
	// bakIDText,
	// OBParser.sqlString(obj.getExtra()),
	// obj.getRatio()
	// );
	// }
	// if(!subText.isEmpty())
	// {
	// sqlText += subText + ";";
	// db.executeUpdate(sqlText);
	// }
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void addNodeInfoAlteon(Integer adcIndex, ArrayList<OBDtoAdcNodeAlteon> nodeList, StringBuilder query)
			throws OBException {

		if (nodeList.size() == 0) {
			return;
		}

		query.append(" INSERT INTO TMP_SLB_NODE ").append(
				"(INDEX, ADC_INDEX, ALTEON_ID, IP_ADDRESS, NAME, STATE, STATUS, BAK_TYPE, BAK_ID, EXTRA, RATIO) ")
				.append(" VALUES ");

		final String delimiter = ", ";
		String prefix = "";

		for (OBDtoAdcNodeAlteon obj : nodeList) {
			String bakIDText = "";
			if (obj.getBakId().equals(OBDefine.BACKUP_STATE.EMPTY)) {
				bakIDText = obj.getBakId();
			} else {
				bakIDText = OBParser.sqlString(OBCommon.makePoolBackupIndexAlteon(adcIndex, obj.getBakId()));
			}

			String index = OBCommon.makeNodeIndexAlteon(adcIndex, obj.getAlteonId());

			query.append(prefix).append("(").append(OBParser.sqlString(index)).append(delimiter).append(adcIndex)
					.append(delimiter).append(OBParser.sqlString(obj.getAlteonId())).append(delimiter)
					.append(OBParser.sqlString(obj.getIpAddress())).append(delimiter)
					.append(OBParser.sqlString(obj.getName())).append(delimiter).append(obj.getState())
					.append(delimiter).append(obj.getStatus()).append(delimiter).append(obj.getBakType())
					.append(delimiter).append(bakIDText).append(delimiter).append(OBParser.sqlString(obj.getExtra()))
					.append(delimiter).append(obj.getRatio()).append(")");

			prefix = ", ";
		}

		query.append(" ; ");
	}

	/**
	 * node 정보를 db에 저장한다. F5용 function 엔트리 전체를 한번에 insert하는 함수.
	 */
	// INSERT INTO TMP_SLB_NODE (INDEX, ADC_INDEX, IP_ADDRESS, NAME, STATE) VALUES
	// (....), (....), ....;
	// public void addNodeInfoF5All(Integer adcIndex, ArrayList<OBDtoAdcNodeF5>
	// nodeList, OBDatabase db) throws OBException
	// {
	// int nodeCount = nodeList.size();
	// boolean bNodeQueryDone = false;
	//
	// if(nodeCount==0)
	// {
	// return;
	// }
	//
	// String index;
	// OBDtoAdcNodeF5 obj;
	// int i;
	//
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(
	// " INSERT INTO TMP_SLB_NODE (INDEX, ADC_INDEX, IP_ADDRESS, STATE, STATUS,
	// NAME, RATIO) " +
	// " VALUES ");
	// for(i=0; i<nodeCount; i++)
	// {
	// obj = nodeList.get(i);
	// index = OBCommon.makeNodeIndexF5(adcIndex, obj.getIpAddress());
	// sqlText += String.format("(%s, %d, %s, %d, %d, %s, %d) ",
	// OBParser.sqlString(index),
	// adcIndex,
	// OBParser.sqlString(obj.getIpAddress()),
	// obj.getState(),
	// obj.getStatus(),
	// OBParser.sqlString(obj.getName()), //node name
	// obj.getRatio()
	// );
	// if(i==(nodeCount-1))
	// {
	// sqlText += ";";
	// bNodeQueryDone = true;
	// }
	// else
	// {
	// sqlText += ",";
	// }
	// }
	//
	// if(bNodeQueryDone)
	// {
	// db.executeUpdate(sqlText);
	// }
	// else
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("sqlText =
	// %s, Node insert query error", sqlText));
	// }
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void addNodeInfoF5(Integer adcIndex, ArrayList<OBDtoAdcNodeF5> nodeList, StringBuilder query)
			throws OBException {
		if (nodeList.size() == 0) {
			return;
		}

		final String delimiter = ", ";
		String prefix = "";

		query.append(" INSERT INTO TMP_SLB_NODE (INDEX, ADC_INDEX, IP_ADDRESS, STATE, STATUS, NAME, RATIO) ")
				.append(" VALUES ");

		for (OBDtoAdcNodeF5 obj : nodeList) {
			String index = OBCommon.makeNodeIndexF5(adcIndex, obj.getIpAddress());

			StringBuilder tmpQuery = new StringBuilder();

			tmpQuery.append(prefix).append("(").append(OBParser.sqlString(index)).append(delimiter).append(adcIndex)
					.append(delimiter).append(OBParser.sqlString(obj.getIpAddress())).append(delimiter)
					.append(obj.getState()).append(delimiter).append(obj.getStatus()).append(delimiter)
					.append(OBParser.sqlString(obj.getName())).append(delimiter).append(obj.getRatio()).append(")");

			query.append(tmpQuery.toString());

			prefix = ", ";
		}

		query.append(";");
	}

	/**
	 * 지정된 ADC 장비의 모든 노드를 DB에서 삭제한다.
	 * 
	 * @param adcIndex -- ADC 장비 index
	 * @param db       -- DB 인스턴스.
	 */
	// public void delNodeAll(Integer adcIndex, OBDatabase db) throws OBException
	// {
	// delAdcConfigAll(adcIndex, "TMP_SLB_NODE", db);
	// }

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void delNodeAll(Integer adcIndex, StringBuilder query) throws OBException {
		delTableAll(adcIndex, "TMP_SLB_NODE", query);
	}

	public void delNodePartial(Integer adcIndex, ArrayList<String> nodeIndexList, StringBuilder query)
			throws OBException {
		delTablePartial(adcIndex, "TMP_SLB_NODE", "INDEX", nodeIndexList, query);
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public void delNodeAll(Integer adcIndex) throws OBException {
		delTableAll(adcIndex, "TMP_SLB_NODE");
	}

	/**
	 * 지정된 ADC 장비의 모든 노드를 DB에서 삭제한다.
	 * 
	 * @param adcIndex -- ADC 장비 index *
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */

	// public void delNodeAll(Integer adcIndex) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	// try
	// {
	// db.openDB();
	//
	// delAdcConfigAll(adcIndex, "TMP_SLB_NODE", db);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// }

	/**
	 * 지정된 ADC 장비에서 모든 pool를 추출한다.
	 * 
	 * @param adcIndex -- ADC 장비 index
	 * @param db       -- DB 인스턴스.
	 * @return ArrayList<OBDtoAdcPool>
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public ArrayList<OBDtoAdcPoolAlteon> getPoolListAllAlteon(Integer adcIndex) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT INDEX, NAME, ALTEON_ID, LB_METHOD, HEALTH_CHECK, HEALTH_CHECK_INDEX, BAK_TYPE, BAK_ID "
							+ " FROM TMP_SLB_POOL " + " WHERE ADC_INDEX=%d " + " ORDER BY ALTEON_ID;",
					adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoAdcPoolAlteon> list = new ArrayList<OBDtoAdcPoolAlteon>();
			while (rs.next()) {
				OBDtoAdcPoolAlteon obj = new OBDtoAdcPoolAlteon();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setAlteonId(db.getString(rs, "ALTEON_ID"));
				obj.setLbMethod(db.getInteger(rs, "LB_METHOD"));
				obj.setHealthCheck(db.getInteger(rs, "HEALTH_CHECK"));
				OBDtoAdcHealthCheckAlteon healthObj = getHealthCheckInfoAlteon(db.getString(rs, "HEALTH_CHECK_INDEX"));
				obj.setBakType(db.getInteger(rs, "BAK_TYPE"));
				obj.setBakID(db.getString(rs, "BAK_ID"));
				obj.setHealthCheckV2(healthObj);
				obj.setMemberList(getPoolmemberInfoListAlteon(obj.getIndex()));
				list.add(obj);
			}
			return list;
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

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	// db.openDB();
	// db2.openDB();
	// ArrayList<OBDtoAdcPoolPAS> list = new OBVServerDB().getPoolListAllPAS(5);
	// System.out.println(list);
	// db.closeDB();
	// db2.closeDB();
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 지정된 ADC 장비에서 모든 pool를 추출한다.
	 * 
	 * @param adcIndex -- ADC 장비 index
	 * @param db       -- DB 인스턴스.
	 * @return ArrayList<OBDtoAdcPool>
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public ArrayList<OBDtoAdcPoolF5> getPoolListAllF5(Integer adcIndex) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX, NAME, LB_METHOD, HEALTH_CHECK " + " FROM TMP_SLB_POOL "
					+ " WHERE ADC_INDEX=%d " + " ORDER BY NAME;", adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoAdcPoolF5> list = new ArrayList<OBDtoAdcPoolF5>();
			while (rs.next()) {
				OBDtoAdcPoolF5 obj = new OBDtoAdcPoolF5();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setLbMethod(db.getInteger(rs, "LB_METHOD"));
				obj.setHealthCheck(db.getInteger(rs, "HEALTH_CHECK"));
				obj.setMemberList(getPoolmemberInfoListF5(obj.getIndex()));
				list.add(obj);
			}
			return list;
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

	public ArrayList<OBDtoAdcPoolPAS> getPoolListAllPAS(Integer adcIndex) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX, NAME, LB_METHOD, HEALTH_CHECK, HEALTH_CHECK_INDEX"
					+ " FROM TMP_SLB_POOL " + " WHERE ADC_INDEX=%d " + " ORDER BY NAME;", adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoAdcPoolPAS> list = new ArrayList<OBDtoAdcPoolPAS>();
			while (rs.next()) {
				OBDtoAdcPoolPAS obj = new OBDtoAdcPoolPAS();
				obj.setDbIndex(db.getString(rs, "INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setLbMethod(db.getInteger(rs, "LB_METHOD"));
				OBDtoAdcHealthCheckPAS healthObj = getHealthCheckInfoPAS(db.getString(rs, "HEALTH_CHECK_INDEX"));
				obj.setHealthCheckInfo(healthObj);
				ArrayList<OBDtoAdcPoolMemberPAS> memberList = getPoolmemberInfoListPAS(obj.getDbIndex());
				obj.setMemberList(memberList);
				list.add(obj);
			}
			return list;
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

	public ArrayList<OBDtoAdcPoolPASK> getPoolListAllPASK(Integer adcIndex) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX, NAME, LB_METHOD, HEALTH_CHECK, HEALTH_CHECK_INDEX"
					+ " FROM TMP_SLB_POOL " + " WHERE ADC_INDEX=%d " + " ORDER BY NAME;", adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoAdcPoolPASK> list = new ArrayList<OBDtoAdcPoolPASK>();
			while (rs.next()) {
				OBDtoAdcPoolPASK obj = new OBDtoAdcPoolPASK();
				obj.setDbIndex(db.getString(rs, "INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setLbMethod(db.getInteger(rs, "LB_METHOD"));
				OBDtoAdcHealthCheckPASK healthObj = getHealthCheckInfoPASK(db.getString(rs, "HEALTH_CHECK_INDEX"));
				obj.setHealthCheckInfo(healthObj);
				ArrayList<OBDtoAdcPoolMemberPASK> memberList = getPoolmemberInfoListPASK(obj.getDbIndex());
				obj.setMemberList(memberList);
				list.add(obj);
			}
			return list;
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

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	String getNodeIndex(Integer adcIndex, String ipAddress) throws OBException {
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX " + " FROM TMP_SLB_NODE " + " WHERE ADC_INDEX=%d AND IP_ADDRESS=%s;",
					adcIndex, OBParser.sqlString(ipAddress));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false)
				return null;

			return db.getString(rs, "INDEX");
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

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public String getProfileIndex(Integer adcIndex, String profileName) throws OBException {
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT INDEX " + " FROM TMP_SLB_PROFILE " + " WHERE ADC_INDEX=%d AND PROFILE_NAME=%s;", adcIndex,
					OBParser.sqlString(profileName));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false)
				return null;

			return db.getString(rs, "INDEX");
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

	public ArrayList<OBDtoAdcHealthCheckPASK> getHealthCheckListPASK(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();

		ArrayList<OBDtoAdcHealthCheckPASK> list;
		try {
			db.openDB();
			list = getHealthCheckListPASK(adcIndex, db);
		} catch (OBException e) {
			db.closeDB();
			throw e;
		} catch (Exception e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		db.closeDB();
		return list;
	}

	private ArrayList<OBDtoAdcHealthCheckPASK> getHealthCheckListPASK(Integer adcIndex, OBDatabase db)
			throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(" SELECT INDEX, NAME, ID, TYPE, PORT, CHK_INTERVAL, TIMEOUT, STATE "
					+ " FROM TMP_SLB_HEALTHCHECK " + " WHERE ADC_INDEX=%d " + " ORDER BY NAME;", adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoAdcHealthCheckPASK> list = new ArrayList<OBDtoAdcHealthCheckPASK>();
			while (rs.next()) {
				OBDtoAdcHealthCheckPASK obj = new OBDtoAdcHealthCheckPASK();
				obj.setDbIndex(db.getString(rs, "INDEX"));
				obj.setId(db.getInteger(rs, "ID"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setType(db.getInteger(rs, "TYPE"));
				obj.setPort(db.getInteger(rs, "PORT"));
				obj.setInterval(db.getInteger(rs, "CHK_INTERVAL"));
				obj.setTimeout(db.getInteger(rs, "TIMEOUT"));
				obj.setState(db.getInteger(rs, "STATE"));
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public ArrayList<OBDtoAdcHealthCheckAlteon> getHealthCheckListAlteon(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();

		ArrayList<OBDtoAdcHealthCheckAlteon> list;
		try {
			db.openDB();
			list = getHealthCheckListAlteon(adcIndex, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return list;
	}

	private ArrayList<OBDtoAdcHealthCheckAlteon> getHealthCheckListAlteon(Integer adcIndex, OBDatabase db)
			throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(
					" SELECT INDEX, ID_NEW, NAME, TYPE, EXTRA, DESTINATION_IP" + " FROM TMP_SLB_HEALTHCHECK "
							+ " WHERE ADC_INDEX=%d AND ID_NEW != %s " + " ORDER BY EXTRA;",
					adcIndex, OBParser.sqlString(OBDefineHealthcheckAlteon.NOT_ALLOWED_ID));

			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoAdcHealthCheckAlteon> list = new ArrayList<OBDtoAdcHealthCheckAlteon>();
			while (rs.next()) {
				OBDtoAdcHealthCheckAlteon obj = new OBDtoAdcHealthCheckAlteon();
				obj.setDbIndex(db.getString(rs, "INDEX"));
				obj.setId(db.getString(rs, "ID_NEW"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setType(db.getString(rs, "TYPE"));
				obj.setExtra(db.getString(rs, "EXTRA"));
				obj.setDestinationIp(db.getString(rs, "EXTRA"));
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// public ArrayList<String> getVirtualServerIndexByNodeIPAlteon(Integer
	// adcIndex, String nodeIPAddress, OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// ArrayList<String> vsIndexes = new ArrayList<String>();
	// try
	// {
	// sqlText = String.format(" SELECT VS_INDEX \n" +
	// " FROM TMP_SLB_VS_SERVICE \n" +
	// " WHERE POOL_INDEX IN ( \n" + //where-in:empty string 불가, null 불가, OK
	// " SELECT POOL_INDEX \n" +
	// " FROM TMP_SLB_POOLMEMBER \n" +
	// " WHERE NODE_INDEX IN ( \n" +
	// " SELECT INDEX \n" +
	// " FROM TMP_SLB_NODE \n" +
	// " WHERE IP_ADDRESS = %s \n" +
	// " AND ADC_INDEX = %d \n" +
	// " ) \n" +
	// " ) ; ",
	// OBParser.sqlString(nodeIPAddress),
	// adcIndex);
	//
	// ResultSet rs = db.executeQuery(sqlText);
	//
	// while(rs.next())
	// {
	// vsIndexes.add(db.getString(rs, "VS_INDEX"));
	// }
	// return vsIndexes;
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }
	//
	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public ArrayList<String> getVirtualServerIndexByNodeIPAlteon(Integer adcIndex, String nodeIPAddress)
			throws OBException {
		String sqlText = "";
		ArrayList<String> vsIndexes = new ArrayList<String>();
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT VS_INDEX                            \n" + " FROM TMP_SLB_VS_SERVICE                    \n"
							+ " WHERE POOL_INDEX IN (                      \n" + // where-in:empty string 불가, null 불가,
																					// OK
							"          SELECT POOL_INDEX                 \n"
							+ "          FROM TMP_SLB_POOLMEMBER           \n"
							+ "          WHERE NODE_INDEX IN (             \n"
							+ "                     SELECT INDEX           \n"
							+ "                     FROM TMP_SLB_NODE      \n"
							+ "                     WHERE IP_ADDRESS = %s  \n"
							+ "                       AND ADC_INDEX = %d   \n"
							+ "                     )                      \n"
							+ "          ) ;                               ",
					OBParser.sqlString(nodeIPAddress), adcIndex);

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				vsIndexes.add(db.getString(rs, "VS_INDEX"));
			}
			return vsIndexes;
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

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	// db.openDB();
	// db2.openDB();
	// String list = new OBVServerDB().getVirtualServerIndexByNodeIPPAS(5,
	// "10.10.10.1", db);
	// System.out.println(list);
	// db.closeDB();
	// db2.closeDB();
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	// public ArrayList<String> getVirtualServerIndexByNodeIPPAS(Integer adcIndex,
	// String nodeIPAddress, OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// ArrayList<String> vsIndexes = new ArrayList<String>();
	// try
	// {
	// sqlText = String.format(
	// " SELECT INDEX \n" +
	// " FROM TMP_SLB_VSERVER \n" +
	// " WHERE POOL_INDEX IN ( \n" + //where-in:empty string 불가, null 불가, OK
	// " SELECT POOL_INDEX \n" +
	// " FROM TMP_SLB_POOLMEMBER \n" +
	// " WHERE NODE_INDEX = ( \n" +
	// " SELECT INDEX \n" +
	// " FROM TMP_SLB_NODE \n" +
	// " WHERE IP_ADDRESS = %s \n" +
	// " AND ADC_INDEX = %d \n" +
	// " ) \n" +
	// " ) ; ",
	// OBParser.sqlString(nodeIPAddress),
	// adcIndex);
	//
	// ResultSet rs = db.executeQuery(sqlText);
	//
	// while(rs.next())
	// {
	// vsIndexes.add(db.getString(rs, "INDEX"));
	// }
	// return vsIndexes;
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public ArrayList<String> getVirtualServerIndexByNodeIPPAS(Integer adcIndex, String nodeIPAddress)
			throws OBException {
		String sqlText = "";
		ArrayList<String> vsIndexes = new ArrayList<String>();
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" SELECT INDEX                           \n"
					+ " FROM TMP_SLB_VSERVER                   \n" + " WHERE POOL_INDEX IN (                  \n" + // where-in:empty
																													// string
																													// 불가,
																													// null
																													// 불가,
																													// OK
					"         SELECT POOL_INDEX              \n" + "         FROM TMP_SLB_POOLMEMBER        \n"
					+ "         WHERE NODE_INDEX = (           \n" + "                  SELECT INDEX          \n"
					+ "                  FROM TMP_SLB_NODE     \n" + "                  WHERE IP_ADDRESS = %s \n"
					+ "                     AND ADC_INDEX = %d \n" + "                  )                     \n"
					+ "         ) ;                            ", OBParser.sqlString(nodeIPAddress), adcIndex);

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				vsIndexes.add(db.getString(rs, "INDEX"));
			}
			return vsIndexes;
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

	// public ArrayList<String> getVirtualServerIndexByNodeIPPASK(Integer adcIndex,
	// String nodeIPAddress, OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// ArrayList<String> vsIndexes = new ArrayList<String>();
	// try
	// {
	// sqlText = String.format(
	// " SELECT INDEX \n" +
	// " FROM TMP_SLB_VSERVER \n" +
	// " WHERE POOL_INDEX IN ( \n" + //where-in:empty string 불가, null 불가, OK
	// " SELECT POOL_INDEX \n" +
	// " FROM TMP_SLB_POOLMEMBER \n" +
	// " WHERE NODE_INDEX = ( \n" +
	// " SELECT INDEX \n" +
	// " FROM TMP_SLB_NODE \n" +
	// " WHERE IP_ADDRESS = %s \n" +
	// " AND ADC_INDEX = %d \n" +
	// " ) \n" +
	// " ) ; ",
	// OBParser.sqlString(nodeIPAddress),
	// adcIndex);
	//
	// ResultSet rs = db.executeQuery(sqlText);
	//
	// while(rs.next())
	// {
	// vsIndexes.add(db.getString(rs, "INDEX"));
	// }
	// return vsIndexes;
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public ArrayList<String> getVirtualServerIndexByNodeIPPASK(Integer adcIndex, String nodeIPAddress)
			throws OBException {
		String sqlText = "";
		ArrayList<String> vsIndexes = new ArrayList<String>();
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" SELECT INDEX                           \n"
					+ " FROM TMP_SLB_VSERVER                   \n" + " WHERE POOL_INDEX IN (                  \n" + // where-in:empty
																													// string
																													// 불가,
																													// null
																													// 불가,
																													// OK
					"         SELECT POOL_INDEX              \n" + "         FROM TMP_SLB_POOLMEMBER        \n"
					+ "         WHERE NODE_INDEX = (           \n" + "                  SELECT INDEX          \n"
					+ "                  FROM TMP_SLB_NODE     \n" + "                  WHERE IP_ADDRESS = %s \n"
					+ "                     AND ADC_INDEX = %d \n" + "                  )                     \n"
					+ "         ) ;                            ", OBParser.sqlString(nodeIPAddress), adcIndex);

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				vsIndexes.add(db.getString(rs, "INDEX"));
			}
			return vsIndexes;
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

	// public ArrayList<String> getVirtualServerIndexByNodeIPF5(Integer adcIndex,
	// String nodeIPAddress, OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// ArrayList<String> vsIndexes = new ArrayList<String>();
	//
	// try
	// {
	// sqlText = String.format(
	// " SELECT INDEX \n" +
	// " FROM TMP_SLB_VSERVER \n" +
	// " WHERE POOL_INDEX IN ( \n" + //where-in:empty string 불가, null 불가, OK
	// " SELECT POOL_INDEX \n" +
	// " FROM TMP_SLB_POOLMEMBER \n" +
	// " WHERE NODE_INDEX = ( \n" +
	// " SELECT INDEX \n" +
	// " FROM TMP_SLB_NODE \n" +
	// " WHERE IP_ADDRESS = %s \n" +
	// " AND ADC_INDEX = %d \n" +
	// " ) \n" +
	// " ) ; ",
	// OBParser.sqlString(nodeIPAddress),
	// adcIndex);
	// ResultSet rs = db.executeQuery(sqlText);
	//
	// while(rs.next())
	// {
	// vsIndexes.add(db.getString(rs, "INDEX"));
	// }
	// return vsIndexes;
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// }

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public ArrayList<String> getVirtualServerIndexByNodeIPF5(Integer adcIndex, String nodeIPAddress)
			throws OBException {
		String sqlText = "";
		ArrayList<String> vsIndexes = new ArrayList<String>();

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX                           \n"
					+ " FROM TMP_SLB_VSERVER                   \n" + " WHERE POOL_INDEX IN (                  \n" + // where-in:empty
																													// string
																													// 불가,
																													// null
																													// 불가,
																													// OK
					"         SELECT POOL_INDEX              \n" + "         FROM TMP_SLB_POOLMEMBER        \n"
					+ "         WHERE NODE_INDEX = (           \n" + "                  SELECT INDEX          \n"
					+ "                  FROM TMP_SLB_NODE     \n" + "                  WHERE IP_ADDRESS = %s \n"
					+ "                     AND ADC_INDEX = %d \n" + "                  )                     \n"
					+ "         ) ;                            ", OBParser.sqlString(nodeIPAddress), adcIndex);
			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				vsIndexes.add(db.getString(rs, "INDEX"));
			}
			return vsIndexes;
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

	// public HashMap<String, OBDtoVServerStatus> getVirtualServerStatus(Integer
	// adcIndex, OBDatabase db) throws OBException
	// {
	// HashMap<String, OBDtoVServerStatus> hashMap = new HashMap<String,
	// OBDtoVServerStatus>();
	//
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(" SELECT INDEX, NAME, STATUS " +
	// " FROM TMP_SLB_VSERVER " +
	// " WHERE ADC_INDEX=%d ;",
	// adcIndex);
	//
	// ResultSet rs = db.executeQuery(sqlText);
	//
	// while(rs.next())
	// {
	// OBDtoVServerStatus status = new OBDtoVServerStatus();
	// status.setStatus(db.getInteger(rs, "STATUS"));
	// status.setVsIndex(db.getString(rs, "INDEX"));
	// status.setVsName(db.getString(rs, "NAME"));
	// hashMap.put(status.getVsIndex(), status);
	// }
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	//
	// return hashMap;
	// }
	//
	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public HashMap<String, OBDtoVServerStatus> getVirtualServerStatus(Integer adcIndex) throws OBException {
		HashMap<String, OBDtoVServerStatus> hashMap = new HashMap<String, OBDtoVServerStatus>();

		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX, NAME, STATUS " + " FROM TMP_SLB_VSERVER " + " WHERE ADC_INDEX=%d ;",
					adcIndex);

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoVServerStatus status = new OBDtoVServerStatus();
				status.setStatus(db.getInteger(rs, "STATUS"));
				status.setVsIndex(db.getString(rs, "INDEX"));
				status.setVsName(db.getString(rs, "NAME"));
				hashMap.put(status.getVsIndex(), status);
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
		return hashMap;
	}

	// public String getVirtualServerIndex(Integer adcIndex, Integer alteonID,
	// OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(" SELECT INDEX " +
	// " FROM TMP_SLB_VSERVER " +
	// " WHERE ADC_INDEX=%d AND ALTEON_ID=%d",
	// adcIndex,
	// alteonID);
	//
	// ResultSet rs = db.executeQuery(sqlText);
	//
	// if(rs.next() == false)
	// return null;
	//
	// return db.getString(rs, "INDEX");
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public String getVirtualServerIndex(Integer adcIndex, String alteonID) throws OBException {
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT INDEX " + " FROM TMP_SLB_VSERVER " + " WHERE ADC_INDEX=%d AND ALTEON_ID=%s", adcIndex,
					OBParser.sqlString(alteonID));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false)
				return null;

			return db.getString(rs, "INDEX");
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

	public ArrayList<String> getVirtualServerIPAddress(ArrayList<String> vsIndexes, OBDatabase db) throws OBException {
		String sqlText = "";
		ArrayList<String> addresses = new ArrayList<String>();
		if (vsIndexes == null || vsIndexes.size() == 0) // 인덱스가 없으면 빈리스트 리턴
		{
			return addresses;
		}

		try {
			sqlText = String.format(" SELECT VIRTUAL_IP FROM TMP_SLB_VSERVER WHERE INDEX IN (%s) ORDER BY VIRTUAL_IP ",
					OBParser.convertList2SingleQuotedString(vsIndexes));
			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				addresses.add(db.getString(rs, "VIRTUAL_IP"));
			}
			return addresses;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public ArrayList<String> getVirtualServerIPAddress(ArrayList<String> vsIndexes) throws OBException {
		String sqlText = "";
		ArrayList<String> addresses = new ArrayList<String>();
		if (vsIndexes == null || vsIndexes.size() == 0) // 인덱스가 없으면 빈리스트 리턴
		{
			return addresses;
		}

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT VIRTUAL_IP FROM TMP_SLB_VSERVER WHERE INDEX IN (%s) ORDER BY VIRTUAL_IP ",
					OBParser.convertList2SingleQuotedString(vsIndexes));
			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				addresses.add(db.getString(rs, "VIRTUAL_IP"));
			}
			return addresses;
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

	// //F5, virtual server index로 virtual port를 구한다.
	// public Integer getVirtualServerPortF5(String vsIndex, OBDatabase db) throws
	// OBException
	// {
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(" SELECT VIRTUAL_PORT " +
	// " FROM TMP_SLB_VSERVER " +
	// " WHERE INDEX=%s;",
	// OBParser.sqlString(vsIndex));
	//
	// ResultSet rs= db.executeQuery(sqlText);
	//
	// if(rs.next() == false)
	// return null;
	//
	// return db.getInteger(rs, "VIRTUAL_PORT");
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	// db.openDB();
	// db2.openDB();
	// Integer list = new OBVServerDB().getVirtualServerPortPAS("5_bwpark", db);
	// System.out.println(list);
	// db.closeDB();
	// db2.closeDB();
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	// public Integer getVirtualServerPortPAS(String vsIndex, OBDatabase db) throws
	// OBException
	// {
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(" SELECT VIRTUAL_PORT " +
	// " FROM TMP_SLB_VSERVER " +
	// " WHERE INDEX=%s;",
	// OBParser.sqlString(vsIndex));
	//
	// ResultSet rs= db.executeQuery(sqlText);
	//
	// if(rs.next() == false)
	// return null;
	//
	// return db.getInteger(rs, "VIRTUAL_PORT");
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// public Integer getVirtualServerPortPASK(String vsIndex, OBDatabase db) throws
	// OBException
	// {
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(" SELECT VIRTUAL_PORT " +
	// " FROM TMP_SLB_VSERVER " +
	// " WHERE INDEX=%s;",
	// OBParser.sqlString(vsIndex));
	//
	// ResultSet rs= db.executeQuery(sqlText);
	//
	// if(rs.next() == false)
	// return null;
	//
	// return db.getInteger(rs, "VIRTUAL_PORT");
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// virtual server의 index 목록으로 name list를 뽑는다.
	public ArrayList<String> getVirtualServerNameList(ArrayList<String> indexList) throws OBException {
		OBDatabase db = new OBDatabase();

		ArrayList<String> list;
		try {
			db.openDB();
			list = getVirtualServerNameList(indexList, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return list;
	}

	// virtual server의 index 목록으로 name list를 뽑는다.
	private ArrayList<String> getVirtualServerNameList(ArrayList<String> indexList, OBDatabase db) throws OBException {
		String sqlText = "";
		String whereText = "''"; // where-in empty방지
		try {
			for (int i = 0; i < indexList.size(); i++) {
				whereText += ", ";
				whereText += OBParser.sqlString(indexList.get(i));
			}

			sqlText = String.format(
					" SELECT NAME " + " FROM TMP_SLB_VSERVER " + " WHERE INDEX IN ( %s ) ORDER BY NAME;", whereText); // where-in:empty
																														// string
																														// 불가,
																														// null
																														// 불가,
																														// OK

			ResultSet rs = db.executeQuery(sqlText);

			ArrayList<String> list = new ArrayList<String>();
			while (rs.next()) {
				list.add(db.getString(rs, "NAME"));
			}
			return list;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// virtual server index로 name을 뽑는다.
	String getVirtualServerName(String vsIndex) throws OBException {
		OBDatabase db = new OBDatabase();

		String vsName = null;
		try {
			db.openDB();
			vsName = getVirtualServerName(vsIndex, db);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return vsName;
	}

	private String getVirtualServerName(String vsIndex, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(
					" SELECT NAME " + " FROM TMP_SLB_VSERVER " + " WHERE INDEX = %s " + " ORDER BY NAME;",
					OBParser.sqlString(vsIndex));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true)
				return db.getString(rs, "NAME");
			else
				return null;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public String getPoolIndex(Integer adcIndex, String poolName) throws OBException {
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" SELECT INDEX " + " FROM TMP_SLB_POOL " + " WHERE ADC_INDEX=%d AND NAME=%s;",
					adcIndex, OBParser.sqlString(poolName));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false)
				return null;

			return db.getString(rs, "INDEX");
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

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	// db.openDB();
	// db2.openDB();
	// OBDtoAdcPoolPAS list = new OBVServerDB().getPoolInfoPAS("5_bwpark");
	// System.out.println(list);
	// db.closeDB();
	// db2.closeDB();
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	public OBDtoAdcPoolF5 getPoolInfoF5(String poolIndex) throws OBException {
		String sqlText = "";
		OBDtoAdcPoolF5 obj = new OBDtoAdcPoolF5();
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX, NAME, LB_METHOD, HEALTH_CHECK " + " FROM TMP_SLB_POOL "
					+ " WHERE INDEX=%s LIMIT 1;", OBParser.sqlString(poolIndex));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return null;
			}
			obj.setIndex(db.getString(rs, "INDEX"));
			obj.setName(db.getString(rs, "NAME"));
			obj.setLbMethod(db.getInteger(rs, "LB_METHOD"));
			obj.setHealthCheck(db.getInteger(rs, "HEALTH_CHECK"));
			obj.setMemberList(getPoolmemberInfoListF5(obj.getIndex()));
			return obj;
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

	private DtoVlanTunnelFilter getVlanFilterF5(Integer adcIndex, String vsIndex, OBDatabase db) throws OBException {
		DtoVlanTunnelFilter retVal = new DtoVlanTunnelFilter();
		ArrayList<String> vlanName = new ArrayList<String>();
		String sqlText = "";
		if (vsIndex == null) {
			return retVal;
		}
		try {
			sqlText = String.format(
					" SELECT VLAN_NAME, STATUS FROM TMP_SLB_VLANTUNNEL_FILTER       "
							+ " WHERE ADC_INDEX = %d AND INDEX = %s                                         ",
					adcIndex, OBParser.sqlString(vsIndex));
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				retVal.setStatus(db.getInteger(rs, "STATUS"));
				if (db.getString(rs, "VLAN_NAME") != null) {
					vlanName.add(db.getString(rs, "VLAN_NAME"));
				}
			}
			retVal.setVlanName(vlanName);
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

	public OBDtoAdcPoolPAS getPoolInfoPAS(String poolIndex) throws OBException {
		String sqlText = "";
		OBDtoAdcPoolPAS obj = new OBDtoAdcPoolPAS();
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX, NAME, LB_METHOD, HEALTH_CHECK_INDEX " + " FROM TMP_SLB_POOL "
					+ " WHERE INDEX=%s LIMIT 1;", OBParser.sqlString(poolIndex));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return null;
			}
			obj.setDbIndex(db.getString(rs, "INDEX"));
			obj.setName(db.getString(rs, "NAME"));
			obj.setLbMethod(db.getInteger(rs, "LB_METHOD"));
			OBDtoAdcHealthCheckPAS healthObj = getHealthCheckInfoPAS(db.getString(rs, "HEALTH_CHECK_INDEX"));
			obj.setHealthCheckInfo(healthObj);
			ArrayList<OBDtoAdcPoolMemberPAS> memberList = getPoolmemberInfoListPAS(poolIndex);
			obj.setMemberList(memberList);
			return obj;
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

	public OBDtoAdcPoolPASK getPoolInfoPASK(String poolIndex) throws OBException {
		String sqlText = "";
		OBDtoAdcPoolPASK obj = new OBDtoAdcPoolPASK();
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX, NAME, LB_METHOD, HEALTH_CHECK_INDEX " + " FROM TMP_SLB_POOL "
					+ " WHERE INDEX=%s LIMIT 1;", OBParser.sqlString(poolIndex));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return null;
			}
			obj.setDbIndex(db.getString(rs, "INDEX"));
			obj.setName(db.getString(rs, "NAME"));
			obj.setLbMethod(db.getInteger(rs, "LB_METHOD"));
			OBDtoAdcHealthCheckPASK healthObj = getHealthCheckInfoPASK(db.getString(rs, "HEALTH_CHECK_INDEX"));
			obj.setHealthCheckInfo(healthObj);
			ArrayList<OBDtoAdcPoolMemberPASK> memberList = getPoolmemberInfoListPASK(poolIndex);
			obj.setMemberList(memberList);
			return obj;
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

	private OBDtoAdcHealthCheckPAS getHealthCheckInfoPAS(String healthIndex) throws OBException {
		String sqlText = String.format(
				" SELECT INDEX, ADC_INDEX, POOL_INDEX, ID, NAME, TYPE, PORT, CHK_INTERVAL, TIMEOUT, SEND_STRING, RCV_STRING, STATE, EXTRA "
						+ " FROM TMP_SLB_HEALTHCHECK " + " WHERE INDEX=%s LIMIT 1;",
				OBParser.sqlString(healthIndex));

		OBDtoAdcHealthCheckPAS obj = new OBDtoAdcHealthCheckPAS();
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return null;
			}
			obj.setDbIndex(db.getString(rs, "INDEX"));
			// String id = db.getString(rs, "ID_NAME");
			obj.setId(db.getInteger(rs, "ID"));
			obj.setName(db.getString(rs, "NAME"));
			obj.setInterval(db.getInteger(rs, "CHK_INTERVAL"));
			obj.setPort(db.getInteger(rs, "PORT"));
			obj.setTimeout(db.getInteger(rs, "TIMEOUT"));
			obj.setType(db.getInteger(rs, "TYPE"));
			return obj;
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

	public OBDtoAdcHealthCheckPASK getHealthCheckInfoPASK(String healthcheckDbIndex) throws OBException {
		String sqlText = "";
		OBDtoAdcHealthCheckPASK obj = new OBDtoAdcHealthCheckPASK();
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT INDEX, ADC_INDEX, POOL_INDEX, ID, NAME, TYPE, PORT, CHK_INTERVAL, TIMEOUT, SEND_STRING, RCV_STRING, STATE, EXTRA "
							+ " FROM TMP_SLB_HEALTHCHECK " + " WHERE INDEX=%s LIMIT 1;",
					OBParser.sqlString(healthcheckDbIndex));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return null;
			}
			obj.setDbIndex(db.getString(rs, "INDEX"));
			// String id = db.getString(rs, "ID_NAME");
			obj.setId(db.getInteger(rs, "ID"));
			obj.setName(db.getString(rs, "NAME"));
			obj.setInterval(db.getInteger(rs, "CHK_INTERVAL"));
			obj.setPort(db.getInteger(rs, "PORT"));
			obj.setTimeout(db.getInteger(rs, "TIMEOUT"));
			obj.setType(db.getInteger(rs, "TYPE"));
			obj.setState(db.getInteger(rs, "STATE"));
			return obj;
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

	// public ArrayList<OBDtoAdcPoolAlteon> getPoolBackupInfoAlteon(Integer
	// adcIndex) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	//
	// ArrayList<OBDtoAdcPoolAlteon> obj;
	// try
	// {
	// db.openDB();
	// db2.openDB();
	//
	// obj = getPoolBackupInfoAlteon(adcIndex, db, db2);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// if(db2!=null) db2.closeDB();
	// }
	// return obj;
	// }

	public OBDtoAdcPoolAlteon getPoolInfoAlteon(String poolIndex) throws OBException {
		String sqlText = "";
		OBDtoAdcPoolAlteon obj = new OBDtoAdcPoolAlteon();
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT INDEX, NAME, ALTEON_ID, LB_METHOD, HEALTH_CHECK, HEALTH_CHECK_INDEX, BAK_TYPE, BAK_ID "
							+ " FROM TMP_SLB_POOL " + " WHERE INDEX=%s;",
					OBParser.sqlString(poolIndex));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return null;
			}
			obj.setIndex(db.getString(rs, "INDEX"));
			obj.setName(db.getString(rs, "NAME"));
			obj.setAlteonId(db.getString(rs, "ALTEON_ID"));
			obj.setLbMethod(db.getInteger(rs, "LB_METHOD"));
			obj.setHealthCheck(db.getInteger(rs, "HEALTH_CHECK"));
			obj.setIndex(db.getString(rs, "INDEX"));
			obj.setHealthCheckV2(getHealthCheckInfoAlteon(db.getString(rs, "HEALTH_CHECK_INDEX")));
			obj.setBakType(db.getInteger(rs, "BAK_TYPE"));
			obj.setBakID(db.getString(rs, "BAK_ID"));
			obj.setMemberList(getPoolmemberInfoListAlteon(obj.getIndex()));

			return obj;
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

	// DBCP
	public ArrayList<OBDtoAdcPoolAlteon> getPoolBackupInfoAlteon(Integer adcIndex) throws OBException {
		String sqlText = "";
		ArrayList<OBDtoAdcPoolAlteon> backupPoolList = new ArrayList<OBDtoAdcPoolAlteon>();
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT INDEX, NAME, ALTEON_ID, LB_METHOD, HEALTH_CHECK, HEALTH_CHECK_INDEX, BAK_TYPE, BAK_ID                      "
							+ " FROM TMP_SLB_POOL                                                                                                 "
							+ " WHERE ADC_INDEX = %d AND INDEX IN (SELECT BAK_ID FROM TMP_SLB_POOL WHERE ADC_INDEX = %d AND BAK_TYPE = %d) ;      ",
					adcIndex, adcIndex, OBDefine.BACKUP_STATE.GROUPBAK);

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcPoolAlteon obj = new OBDtoAdcPoolAlteon();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setAlteonId(db.getString(rs, "ALTEON_ID"));
				obj.setLbMethod(db.getInteger(rs, "LB_METHOD"));
				obj.setHealthCheck(db.getInteger(rs, "HEALTH_CHECK"));
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setHealthCheckV2(getHealthCheckInfoAlteon(db.getString(rs, "HEALTH_CHECK_INDEX")));
				obj.setMemberList(getPoolmemberInfoListAlteon(obj.getIndex()));
				backupPoolList.add(obj);
			}

			return backupPoolList;
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

	/**
	 * pool 정보를 db에 저장한다.
	 * 
	 * @param obj
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */

	// public void addPoolInfoAlteon(Integer adcIndex, String name, Integer
	// alteonID, Integer lbMethod, String healthCheckId) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// try
	// {
	// db.openDB();
	// addPoolInfoAlteon(adcIndex, name, alteonID, lbMethod, healthCheckId, db);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// }

	// public void updateFlbMonitoringGroup(Integer adcIndex, OBDatabase db) throws
	// OBException
	// {
	// String sqlText="";
	// try
	// { //삭제된 그룹을 모니터링하고 있는지 확인해서 같이 지운다.
	// sqlText = String.format(
	// " DELETE FROM MNG_FLB_GROUP " +
	// " WHERE GROUP_INDEX NOT IN (SELECT INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX =
	// %d) AND ADC_INDEX = %d ", adcIndex, adcIndex);
	// db.executeUpdate(sqlText);
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public void updateFlbMonitoringGroup(Integer adcIndex, StringBuilder query) throws OBException {
		query.append(" DELETE FROM MNG_FLB_GROUP ").append(" WHERE GROUP_INDEX NOT IN ")
				.append("(SELECT INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX = ").append(adcIndex).append(")")
				.append(" AND ADC_INDEX = ").append(adcIndex).append(";");
	}

	// private void addPoolInfoAlteon(Integer adcIndex,
	// ArrayList<OBDtoAdcPoolAlteon> poolList, OBDatabase db) throws OBException
	// {
	// String sqlText=String.format(
	// " INSERT INTO TMP_SLB_POOL (INDEX, ADC_INDEX, NAME, ALTEON_ID, LB_METHOD,
	// HEALTH_CHECK, HEALTH_CHECK_INDEX, BAK_TYPE, BAK_ID) " +
	// " VALUES ");
	// try
	// {
	// String subText="";
	// String bakIDText="";
	// for(OBDtoAdcPoolAlteon obj:poolList)
	// {
	// if(obj.getBakID().equals("0"))
	// {
	// bakIDText = obj.getBakID();
	// }
	// else
	// {
	// bakIDText = OBParser.sqlString(OBCommon.makePoolBackupIndexAlteon(adcIndex,
	// obj.getBakID()));
	// }
	// if(!subText.isEmpty())
	// {
	// subText += ", ";
	// }
	// subText+=String.format(
	// "(%s, %d, %s, %d, %d, %d, %s, %d, %s)",
	// OBParser.sqlString(OBCommon.makePoolIndexAlteon(adcIndex,
	// obj.getAlteonId())),
	// adcIndex,
	// OBParser.sqlString(obj.getName()),
	// obj.getAlteonId(),
	// obj.getLbMethod(),
	// obj.getHealthCheck(),
	// OBParser.sqlString(OBCommon.makeHealthDbIndexAlteon(adcIndex,
	// obj.getHealthCheckV2().getId())),
	// obj.getBakType(),
	// bakIDText);
	// }
	//
	// if(!subText.isEmpty())
	// {
	// sqlText += subText + ";";
	// db.executeUpdate(sqlText);
	// }
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	private void addPoolInfoAlteon(Integer adcIndex, ArrayList<OBDtoAdcPoolAlteon> poolList, StringBuilder query)
			throws OBException {
		if (poolList.size() > 0) {
			query.append(
					" INSERT INTO TMP_SLB_POOL (INDEX, ADC_INDEX, NAME, ALTEON_ID, LB_METHOD, HEALTH_CHECK, HEALTH_CHECK_INDEX, BAK_TYPE, BAK_ID) ")
					.append(" VALUES ");
		}

		final String delimiter = ", ";
		String prefix = "";
		String bakIDText = "";
		for (OBDtoAdcPoolAlteon obj : poolList) {
			if (obj.getBakID().equals("0")) {
				bakIDText = obj.getBakID();
			} else {
				bakIDText = OBParser.sqlString(OBCommon.makePoolBackupIndexAlteon(adcIndex, obj.getBakID()));
			}

			query.append(prefix).append("(")
					.append(OBParser.sqlString(OBCommon.makePoolIndexAlteon(adcIndex, obj.getAlteonId())))
					.append(delimiter).append(adcIndex).append(delimiter).append(OBParser.sqlString(obj.getName()))
					.append(delimiter).append(OBParser.sqlString(obj.getAlteonId())).append(delimiter)
					.append(obj.getLbMethod()).append(delimiter).append(obj.getHealthCheck()).append(delimiter)
					.append(OBParser
							.sqlString(OBCommon.makeHealthDbIndexAlteon(adcIndex, obj.getHealthCheckV2().getId())))
					.append(delimiter).append(obj.getBakType()).append(delimiter).append(bakIDText).append(")");

			prefix = ", ";
		}

		query.append(";");
	}

	private OBDtoAdcHealthCheckAlteon getHealthCheckInfoAlteon(String healthcheckDbIndex) throws OBException {
		String sqlText = "";
		OBDtoAdcHealthCheckAlteon obj = new OBDtoAdcHealthCheckAlteon();
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT ID_NEW, NAME, TYPE, EXTRA, DESTINATION_IP " + " FROM TMP_SLB_HEALTHCHECK "
					+ " WHERE INDEX=%s LIMIT 1;", OBParser.sqlString(healthcheckDbIndex));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return null;
			}
			obj.setDbIndex(healthcheckDbIndex);
			obj.setId(db.getString(rs, "ID_NEW"));
			obj.setName(db.getString(rs, "NAME"));
			obj.setType(db.getString(rs, "TYPE"));
			obj.setExtra(db.getString(rs, "EXTRA"));
			obj.setDestinationIp(db.getString(rs, "DESTINATION_IP"));
			return obj;
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

	// 리얼 그룹 지정 - 리얼 서버 그룹 테이블에 그룹을 추가한다.
	public void addRealServerGroup(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup) throws OBException {
		// OBSystemLog.info("start functioin: poolnum = " + poolList.size() + " /
		// poolMemListList = " + memberListList.size());

		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(
					" INSERT INTO MNG_REALSERVER_GROUP "
							+ " (ADC_INDEX, RS_GROUP_NAME, ACCNT_INDEX, AVAILABLE, DESCRIPTION) " + " VALUES "
							+ " (%d, %s, %d, %d, %s);",
					adcIndex, OBParser.sqlString(rsGroup.getGroupName()), rsGroup.getAccntIndex(),
					rsGroup.getAvailable(), OBParser.sqlString(rsGroup.getDescription()));

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

	// 리얼 그룹 지정 - 리얼 서버 그룹 테이블에 그룹이름을 변경한다.
	public void setRealServerGroup(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup) throws OBException {

		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(
					" UPDATE MNG_REALSERVER_GROUP " + " SET RS_GROUP_NAME=%s "
							+ " WHERE INDEX=%d AND ADC_INDEX=%d AND ACCNT_INDEX=%d ;",
					OBParser.sqlString(rsGroup.getGroupName()), rsGroup.getGroupIndex(), adcIndex,
					rsGroup.getAccntIndex());

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

	// 리얼 그룹 지정 - 리얼 서버 그룹 테이블에 그룹이름을 삭제 한다.
	public void delRealServerGroup(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" UPDATE MNG_REALSERVER_GROUP " + " SET AVAILABLE = %d "
							+ " WHERE ADC_INDEX = %d AND INDEX = %d AND ACCNT_INDEX = %d; ",
					OBDefine.DATA_UNAVAILABLE, adcIndex, rsGroup.getGroupIndex(), rsGroup.getAccntIndex());

			db.executeUpdate(sqlText);

			sqlText = String.format(" SELECT GROUP_INDEX							\n"
					+ " FROM MNG_REALSERVER_GROUP_MAP                      			\n"
					+ " WHERE GROUP_INDEX = %d						   				", rsGroup.getGroupIndex());

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			if (rs.next() == true) {

				sqlText = String.format(" DELETE FROM MNG_REALSERVER_GROUP_MAP " + " " + "WHERE GROUP_INDEX = %d; ",
						rsGroup.getGroupIndex());

				db.executeUpdate(sqlText);
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
	}

	// 리얼 그룹 지정 - 리얼 서버 그룹 테이블에 그룹리스트를 조회 한다.
	public ArrayList<OBDtoAdcRealServerGroup> getRealServerList(Integer adcIndex, Integer accntIndex, Integer orderType,
			Integer orderDir) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("adcIndex:%d, accntIndex:%d", adcIndex, accntIndex));
		ArrayList<OBDtoAdcRealServerGroup> retVal = new ArrayList<OBDtoAdcRealServerGroup>();

		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT INDEX, RS_GROUP_NAME, ACCNT_INDEX, AVAILABLE, DESCRIPTION	\n"
							+ " FROM MNG_REALSERVER_GROUP                               							\n"
							+ " WHERE ADC_INDEX = %d AND ACCNT_INDEX = %d                							",
					adcIndex, accntIndex);

			sqlText += getGroupListF5OrderType(orderType, orderDir);

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcRealServerGroup rsGroup = new OBDtoAdcRealServerGroup();
				rsGroup.setGroupIndex(db.getInteger(rs, "INDEX"));
				rsGroup.setGroupName(db.getString(rs, "RS_GROUP_NAME"));
				rsGroup.setAccntIndex(db.getInteger(rs, "ACCNT_INDEX"));
				rsGroup.setAvailable(db.getInteger(rs, "AVAILABLE"));
				rsGroup.setDescription(db.getString(rs, "DESCRIPTION"));
				retVal.add(rsGroup);
			}
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

	private String getGroupListF5OrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY INDEX ASC NULLS LAST ";
		switch (orderType) {
		case OBDefine.ORDER_TYPE_FIFTH:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY RS_GROUP_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY RS_GROUP_NAME DESC NULLS LAST ";
			break;
		}
		return retVal;
	}

	public void addRealServerMap(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup, ArrayList<OBDtoAdcNodeF5> nodeList)
			throws OBException {
		String sqlText = "";

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			for (int i = 0; i < nodeList.size(); i++) {
				// 그룹인덱스가 없으면 새로운 nodeList로 보고 아래 쿼리를 실행하지 앟는다.
				if (nodeList.get(i).getGroupIndex() == null) {
					continue;
				}
				// 기존 그룹인지 검사한다.
				sqlText = String.format(
						" SELECT * FROM MNG_REALSERVER_GROUP_MAP 				"
								+ " WHERE GROUP_INDEX = %d AND RS_INDEX = %s;	 						",
						nodeList.get(i).getGroupIndex(), OBParser.sqlString(nodeList.get(i).getIndex()));

				ResultSet rs;

				rs = db.executeQuery(sqlText);
				// 기존 그룹이 있으면 삭제한다.
				if (rs.next()) {
					sqlText = String.format(
							" DELETE FROM MNG_REALSERVER_GROUP_MAP 		"
									+ " WHERE GROUP_INDEX = %d AND RS_INDEX = %s;				",
							nodeList.get(i).getGroupIndex(), OBParser.sqlString(nodeList.get(i).getIndex()));
					db.executeUpdate(sqlText);
				}
			}

			// 노드들을 삽입한다.
			sqlText = String
					.format(" INSERT INTO MNG_REALSERVER_GROUP_MAP " + " (GROUP_INDEX, RS_INDEX) " + " VALUES ");

			String subText = "";
			for (OBDtoAdcNodeF5 obj : nodeList) {
				if (!subText.isEmpty())
					subText += ", ";
				subText += String.format("(%d, %s)", rsGroup.getGroupIndex(), OBParser.sqlString(obj.getIndex()));
			}
			if (!subText.isEmpty()) {
				sqlText += subText + ";";
				db.executeUpdate(sqlText);
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
	}

	public void delRealServerMap(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup, ArrayList<OBDtoAdcNodeF5> nodeList)
			throws OBException {
		String sqlText = "";
		String sqlTextRS = OBParser.convertSqlRealServerList(nodeList);
		if (sqlTextRS == null || sqlTextRS.isEmpty()) {
			return;
		}
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" DELETE FROM MNG_REALSERVER_GROUP_MAP 			"
					+ "WHERE RS_INDEX IN (%s); 										", sqlTextRS);
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

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void addPoolAndPoolMemberF5(Integer adcIndex, ArrayList<OBDtoAdcPoolF5> poolList,
			ArrayList<ArrayList<DtoPoolMember>> memberListList, StringBuilder query) throws OBException {
		if (poolList.size() == 0) {
			return;
		}

		ArrayList<String> indexList = new ArrayList<String>();
		final String delimiter = ", ";
		String prefix = "";

		query.append(" INSERT INTO TMP_SLB_POOL (INDEX, ADC_INDEX, NAME, LB_METHOD, HEALTH_CHECK) ").append(" VALUES ");

		for (OBDtoAdcPoolF5 pool : poolList) {
			String index = OBCommon.makePoolIndex(adcIndex, pool.getName());
			indexList.add(index);

			query.append(prefix).append("(").append(OBParser.sqlString(index)).append(delimiter).append(adcIndex)
					.append(delimiter).append(OBParser.sqlString(pool.getName())).append(delimiter)
					.append(pool.getLbMethod()).append(delimiter).append(pool.getHealthCheck()).append(")");

			prefix = ", ";
		}

		query.append(";");

		// prefix 리셋
		prefix = "";

		boolean queryTrigger = true;

		for (int i = 0; i < memberListList.size(); i++) {
			ArrayList<DtoPoolMember> poolMemList = memberListList.get(i);
			String poolIndex = indexList.get(i);

			for (DtoPoolMember poolMem : poolMemList) {
				String nodeIndex = OBCommon.makeNodeIndexF5(adcIndex, poolMem.getIpAddress());
				String index = OBCommon.makePoolMemberIndexF5ByIndex(adcIndex, poolIndex, nodeIndex, poolMem.getPort());

				if (queryTrigger) {
					query.append(
							" INSERT INTO TMP_SLB_POOLMEMBER (INDEX, ADC_INDEX, POOL_INDEX, NODE_INDEX, PORT, STATE, RATIO) ")
							.append(" VALUES ");

					queryTrigger = false;
				}

				query.append(prefix).append("(").append(OBParser.sqlString(index)).append(delimiter).append(adcIndex)
						.append(delimiter).append(OBParser.sqlString(poolIndex)).append(delimiter)
						.append(OBParser.sqlString(nodeIndex)).append(delimiter).append(poolMem.getPort())
						.append(delimiter).append(poolMem.getState()).append(delimiter).append(poolMem.getRatio())
						.append(")");

				prefix = ", ";
			}
		}

		if (!queryTrigger) {
			query.append(";");
		}
	}

	/**
	 * 지정된 ADC 장비의 pool 목록을 삭제한다.
	 * 
	 * @param adcIndex
	 * @param db
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void delPoolAll(Integer adcIndex, StringBuilder query) throws OBException {
		delTableAll(adcIndex, "TMP_SLB_POOL", query);
	}

	public void delPoolPartial(Integer adcIndex, ArrayList<String> poolIndexList, StringBuilder query)
			throws OBException {
		delTablePartial(adcIndex, "TMP_SLB_POOL", "INDEX", poolIndexList, query);
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public void delPoolAll(Integer adcIndex) throws OBException {
		delTableAll(adcIndex, "TMP_SLB_POOL");
	}

	public ArrayList<OBDtoAdcPoolMemberAlteon> getPoolmemberInfoListAlteon(String poolIndex) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT A.INDEX, B.ALTEON_ID, B.IP_ADDRESS, A.PORT,              \n"
					+ "     A.STATE, B.STATUS, B.EXTRA, B.BAK_TYPE, B.BAK_ID            \n" + // 아래 줄과 같이 MEMBER_STATUS를
																								// join해서 넣었다가 성능부하로 뺌
																								// " A.STATE, B.EXTRA
																								// --, S.MEMBER_STATUS
																								// \n" +
					" FROM (SELECT * FROM TMP_SLB_POOLMEMBER WHERE POOL_INDEX=%s ) A  \n"
					+ " INNER JOIN TMP_SLB_NODE B                                       \n"
					+ " ON A.NODE_INDEX = B.INDEX                                       \n" +
					// " LEFT JOIN TMP_SLB_POOLMEMBER_STATUS S \n" +
					// " ON A.INDEX = S.POOLMEMBER_INDEX \n" +
					" ORDER BY B.IP_ADDRESS, A.PORT, A.STATE ; ", OBParser.sqlString(poolIndex));

			ResultSet rs = db.executeQuery(sqlText);

			ArrayList<OBDtoAdcPoolMemberAlteon> list = new ArrayList<OBDtoAdcPoolMemberAlteon>();
			while (rs.next()) {
				OBDtoAdcPoolMemberAlteon obj = new OBDtoAdcPoolMemberAlteon();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				obj.setPort(db.getInteger(rs, "PORT"));
				obj.setAlteonNodeID(db.getString(rs, "ALTEON_ID"));
				obj.setState(db.getInteger(rs, "STATE"));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				obj.setExtra(db.getString(rs, "EXTRA"));
				obj.setBackupType(db.getInteger(rs, "BAK_TYPE"));
				obj.setBackupId(db.getString(rs, "BAK_ID"));
				list.add(obj);
			}
			return list;
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

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	// db.openDB();
	// db2.openDB();
	// ArrayList<OBDtoAdcPoolMemberPAS> list = new
	// OBVServerDB().getPoolmemberInfoListPAS("5_ykkim7");
	// System.out.println(list);
	// db.closeDB();
	// db2.closeDB();
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	private ArrayList<OBDtoAdcPoolMemberF5> getPoolmemberInfoListF5(String poolIndex) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT A.INDEX, B.IP_ADDRESS, A.PORT, A.STATE, A.RATIO  \n"
					+ " FROM TMP_SLB_POOLMEMBER A                      \n"
					+ " INNER JOIN TMP_SLB_NODE B                      \n"
					+ " ON A.NODE_INDEX = B.INDEX                      \n"
					+ " WHERE A.POOL_INDEX= %s                         \n"
					+ " ORDER BY B.IP_ADDRESS, A.PORT, A.STATE ;      ", OBParser.sqlString(poolIndex));

			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoAdcPoolMemberF5> list = new ArrayList<OBDtoAdcPoolMemberF5>();
			while (rs.next()) {
				OBDtoAdcPoolMemberF5 obj = new OBDtoAdcPoolMemberF5();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				obj.setPort(db.getInteger(rs, "PORT"));
				obj.setState(db.getInteger(rs, "STATE"));
				obj.setRatio(db.getInteger(rs, "RATIO"));
				list.add(obj);
			}
			return list;
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

	private ArrayList<OBDtoAdcPoolMemberPAS> getPoolmemberInfoListPAS(String poolIndex) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT A.INDEX, B.IP_ADDRESS, B.ALTEON_ID, A.PORT, A.STATE  \n"
					+ " FROM TMP_SLB_POOLMEMBER A                      \n"
					+ " INNER JOIN TMP_SLB_NODE B                      \n"
					+ " ON A.NODE_INDEX = B.INDEX                      \n"
					+ " WHERE A.POOL_INDEX=%s                          \n"
					+ " ORDER BY B.IP_ADDRESS, A.PORT, A.STATE ;      ", OBParser.sqlString(poolIndex));

			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoAdcPoolMemberPAS> list = new ArrayList<OBDtoAdcPoolMemberPAS>();
			while (rs.next()) {
				OBDtoAdcPoolMemberPAS obj = new OBDtoAdcPoolMemberPAS();
				obj.setDbIndex(db.getString(rs, "INDEX"));
				obj.setId(db.getInteger(rs, "ALTEON_ID")); // pas real id를 ALTEON_ID에 저장했다.
				obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				obj.setPort(db.getInteger(rs, "PORT"));
				obj.setState(db.getInteger(rs, "STATE"));
				list.add(obj);
			}
			return list;
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

	// public ArrayList<OBDtoAdcPoolMemberPASK> getPoolmemberInfoListPASK(String
	// poolIndex) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// ArrayList<OBDtoAdcPoolMemberPASK> list;
	// try
	// {
	// db.openDB();
	// list = getPoolmemberInfoListPASK(poolIndex, db);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// return list;
	// }
	private ArrayList<OBDtoAdcPoolMemberPASK> getPoolmemberInfoListPASK(String poolIndex) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" SELECT A.INDEX, B.IP_ADDRESS, B.NAME, B.ALTEON_ID, B.STATE, B.PORT \n"
					+ " FROM TMP_SLB_POOLMEMBER A                      \n"
					+ " INNER JOIN TMP_SLB_NODE B                      \n"
					+ " ON A.NODE_INDEX = B.INDEX                      \n"
					+ " WHERE A.POOL_INDEX=%s                          \n"
					+ " ORDER BY B.IP_ADDRESS, A.PORT, A.STATE ;      ", OBParser.sqlString(poolIndex));

			ResultSet rs = db.executeQuery(sqlText);
			ArrayList<OBDtoAdcPoolMemberPASK> list = new ArrayList<OBDtoAdcPoolMemberPASK>();
			while (rs.next()) {
				OBDtoAdcPoolMemberPASK obj = new OBDtoAdcPoolMemberPASK();
				obj.setDbIndex(db.getString(rs, "INDEX"));
				obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setId(db.getInteger(rs, "ALTEON_ID")); // pas real id를 ALTEON_ID에 저장했다.
				obj.setPort(db.getInteger(rs, "PORT"));
				obj.setState(db.getInteger(rs, "STATE"));
				list.add(obj);
			}
			return list;
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

	// public OBDtoAdcPoolMemberAlteon getPoolmemberInfoAlteon(Integer adcIndex,
	// String poolName, String ipAddress, OBDatabase db) throws OBException
	// {
	// String poolIndex = getPoolIndex(adcIndex, poolName, db);
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(
	// " SELECT A.INDEX, B.ALTEON_ID, B.IP_ADDRESS, A.PORT, A.STATE, B.EXTRA \n" +
	// " FROM TMP_SLB_POOLMEMBER A \n" +
	// " INNER JOIN TMP_SLB_NODE B \n" +
	// " ON A.NODE_INDEX = B.INDEX \n" +
	// " WHERE A.ADC_INDEX=%d AND A.POOL_INDEX=%s AND B.IP_ADDRESS=%s; \n"
	// , adcIndex
	// , OBParser.sqlString(poolIndex)
	// , ipAddress);
	//
	// ResultSet rs = db.executeQuery(sqlText);
	//
	// if(rs.next() == false)
	// return null;
	//
	// OBDtoAdcPoolMemberAlteon obj = new OBDtoAdcPoolMemberAlteon();
	// obj.setIndex(db.getString(rs, "INDEX"));
	// obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
	// obj.setPort(db.getInteger(rs, "PORT"));
	// obj.setAlteonNodeID(db.getInteger(rs, "ALTEON_ID"));
	// obj.setState(db.getInteger(rs, "STATE"));
	// obj.setExtra(db.getString(rs, "EXTRA"));
	// return obj;
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }
	//
	// public OBDtoAdcPoolMemberAlteon getPoolmemberInfoAlteon(Integer adcIndex,
	// Integer poolID, Integer nodeID) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// OBDtoAdcPoolMemberAlteon member;
	// try
	// {
	// db.openDB();
	// member = getPoolmemberInfoAlteon(adcIndex, poolID, nodeID, db);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// return member;
	// }

	public OBDtoAdcPoolMemberAlteon getPoolmemberInfoAlteon(Integer adcIndex, String poolID, String nodeID)
			throws OBException {
		String poolIndex = getPoolIndex(adcIndex, poolID);
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT A.INDEX, B.ALTEON_ID, B.IP_ADDRESS, A.PORT, A.STATE, B.EXTRA  \n"
							+ " FROM TMP_SLB_POOLMEMBER A                                            \n"
							+ " INNER JOIN TMP_SLB_NODE B                                            \n"
							+ " ON A.NODE_INDEX = B.INDEX                                            \n"
							+ " WHERE A.ADC_INDEX=%d AND A.POOL_INDEX=%s AND B.ALTEON_ID=%s;         \n",
					adcIndex, OBParser.sqlString(poolIndex), OBParser.sqlString(nodeID));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == false)
				return null;

			OBDtoAdcPoolMemberAlteon obj = new OBDtoAdcPoolMemberAlteon();
			obj.setIndex(db.getString(rs, "INDEX"));
			obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
			obj.setPort(db.getInteger(rs, "PORT"));
			obj.setAlteonNodeID(db.getString(rs, "ALTEON_ID"));
			obj.setState(db.getInteger(rs, "STATE"));
			obj.setExtra(db.getString(rs, "EXTRA"));
			return obj;
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

	public ArrayList<OBDtoAdcPoolAlteon> getFlbGroupInfoAlteon(Integer adcIndex) throws OBException {
		String sqlText = "";
		ArrayList<OBDtoAdcPoolAlteon> ret = new ArrayList<OBDtoAdcPoolAlteon>();
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT POOL.INDEX POOL_INDEX, POOL.NAME POOL_NAME, POOL.ALTEON_ID POOL_ID, POOL.LB_METHOD,                        "
							+ "     MEMBER.INDEX MEMBER_INDEX, REAL.IP_ADDRESS REAL_ADDRESS,                                                      "
							+ "     MEMBER.PORT MEMBER_PORT, REAL.ALTEON_ID REAL_ID, REAL.STATUS REAL_STATUS                                      "
							+ " FROM ( SELECT INDEX, NAME, ALTEON_ID, LB_METHOD, HEALTH_CHECK, HEALTH_CHECK_INDEX                                 "
							+ "        FROM TMP_SLB_POOL WHERE ADC_INDEX=%d ) POOL                                                                "
							+ " LEFT JOIN ( SELECT INDEX, POOL_INDEX, NODE_INDEX, PORT FROM TMP_SLB_POOLMEMBER WHERE ADC_INDEX = %d ) MEMBER      "
							+ " ON POOL.INDEX = MEMBER.POOL_INDEX                                                                                 "
							+ " LEFT JOIN ( SELECT INDEX, ALTEON_ID, IP_ADDRESS, STATUS FROM TMP_SLB_NODE WHERE ADC_INDEX = %d ) REAL             "
							+ " ON MEMBER.NODE_INDEX = REAL.INDEX                                                                                 "
							+ " ORDER BY POOL_INDEX, MEMBER_INDEX;",
					adcIndex, adcIndex, adcIndex);
			ResultSet rs = db.executeQuery(sqlText);

			String curPoolIndex = "empty";
			String newPoolIndex = "empty";
			OBDtoAdcPoolAlteon pool = null;
			OBDtoAdcPoolMemberAlteon member = null;
			while (rs.next()) {
				newPoolIndex = db.getString(rs, "POOL_INDEX");
				if (curPoolIndex.equals(newPoolIndex) == false) // new pool
				{
					if (pool != null) {
						ret.add(pool);
					}
					// 새 pool로 시작
					pool = new OBDtoAdcPoolAlteon();
					pool.setIndex(newPoolIndex);
					pool.setName(db.getString(rs, "POOL_NAME"));
					pool.setAlteonId(db.getString(rs, "POOL_ID"));
					pool.setLbMethod(db.getInteger(rs, "LB_METHOD"));
					pool.setHealthCheck(null); // 안 쓰는 값이라 뺌
					pool.setHealthCheckV2(null); // 안 쓰는 값이라 뺌
					pool.setMemberList(new ArrayList<OBDtoAdcPoolMemberAlteon>()); // member 리스트 준비
					curPoolIndex = newPoolIndex;
				}
				member = new OBDtoAdcPoolMemberAlteon();
				member.setIndex(db.getString(rs, "MEMBER_INDEX"));
				member.setAlteonNodeID(db.getString(rs, "REAL_ID"));
				member.setIpAddress(db.getString(rs, "REAL_ADDRESS"));
				member.setPort(db.getInteger(rs, "MEMBER_PORT"));
				member.setStatus(db.getInteger(rs, "REAL_STATUS"));
				member.setState(null);
				member.setExtra(null);
				pool.getMemberList().add(member);
			}
			if (pool != null) {
				ret.add(pool); // last
			}
			return ret;
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

	// public OBDtoAdcPoolMemberF5 getPoolmemberInfoF5(Integer adcIndex, String
	// poolName, String ipAddress, Integer port) throws OBException
	// {
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
	// OBDtoAdcPoolMemberF5 member;
	// try
	// {
	// member = getPoolmemberInfoF5(adcIndex, poolName, ipAddress, port, db);
	// }
	// catch(OBException e)
	// {
	// db.closeDB();
	// throw e;
	// }
	// catch(Exception e)
	// {
	// db.closeDB();
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	//
	// db.closeDB();
	// return member;
	// }

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	// db.openDB();
	// db2.openDB();
	// OBDtoAdcPoolMemberPAS list = new OBVServerDB().getPoolmemberInfoPAS(5,
	// "ykkim7", "10.10.10.1", 80);
	// System.out.println(list);
	// db.closeDB();
	// db2.closeDB();
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	// public OBDtoAdcPoolMemberPAS getPoolmemberInfoPAS(Integer adcIndex, String
	// poolName, String ipAddress, Integer port) throws OBException
	// {
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
	// OBDtoAdcPoolMemberPAS member;
	// try
	// {
	// member = getPoolmemberInfoPAS(adcIndex, poolName, ipAddress, port, db);
	// }
	// catch(OBException e)
	// {
	// db.closeDB();
	// throw e;
	// }
	// catch(Exception e)
	// {
	// db.closeDB();
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	//
	// db.closeDB();
	// return member;
	// }
	//
	// public OBDtoAdcPoolMemberPAS getPoolmemberInfoPAS(Integer adcIndex, String
	// poolName, String ipAddress, Integer port, OBDatabase db) throws OBException
	// {
	// String poolIndex = getPoolIndex(adcIndex, poolName, db);
	//
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(
	// " SELECT A.INDEX, B.IP_ADDRESS, A.PORT, A.STATE \n" +
	// " FROM TMP_SLB_POOLMEMBER A \n" +
	// " INNER JOIN TMP_SLB_NODE B \n" +
	// " ON A.NODE_INDEX = B.INDEX \n" +
	// " WHERE A.ADC_INDEX=%d AND A.POOL_INDEX=%s AND B.IP_ADDRESS=%s AND
	// A.PORT=%d;"
	// , adcIndex
	// , OBParser.sqlString(poolIndex)
	// , OBParser.sqlString(ipAddress)
	// , port);
	// ResultSet rs = db.executeQuery(sqlText);
	//
	// if(rs.next() == false)
	// return null;
	//
	// OBDtoAdcPoolMemberPAS obj = new OBDtoAdcPoolMemberPAS();
	// obj.setDbIndex(db.getString(rs, "INDEX"));
	// obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
	// obj.setPort(db.getInteger(rs, "PORT"));
	// obj.setState(db.getInteger(rs, "STATE"));
	// return obj;
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// public OBDtoAdcPoolMemberF5 getPoolmemberInfoF5(Integer adcIndex, String
	// poolName, String ipAddress, Integer port, OBDatabase db) throws OBException
	// {
	// String poolIndex = getPoolIndex(adcIndex, poolName, db);
	//
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(
	// " SELECT A.INDEX, B.IP_ADDRESS, A.PORT, A.STATE \n" +
	// " FROM TMP_SLB_POOLMEMBER A \n" +
	// " INNER JOIN TMP_SLB_NODE B \n" +
	// " ON A.NODE_INDEX = B.INDEX \n" +
	// " WHERE A.ADC_INDEX=%d AND A.POOL_INDEX=%s AND B.IP_ADDRESS=%s AND
	// A.PORT=%d;"
	// , adcIndex
	// , OBParser.sqlString(poolIndex)
	// , OBParser.sqlString(ipAddress)
	// , port);
	// ResultSet rs = db.executeQuery(sqlText);
	//
	// if(rs.next() == false)
	// return null;
	//
	// OBDtoAdcPoolMemberF5 obj = new OBDtoAdcPoolMemberF5();
	// obj.setIndex(db.getString(rs, "INDEX"));
	// obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
	// obj.setPort(db.getInteger(rs, "PORT"));
	// obj.setState(db.getInteger(rs, "STATE"));
	// return obj;
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// /**
	// * 지정된 ADC 장비의 pool member정보를 추출한다.
	// *
	// * @param adcIndex
	// * @return
	// * @throws SQLException
	// * @throws NullPointerException
	// * @throws IllegalArgumentException
	// */
	// public ArrayList<OBDtoAdcPoolMemberAlteon> getPoolmemberListAlteon(Integer
	// adcIndex, String poolName) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// ArrayList<OBDtoAdcPoolMemberAlteon> list;
	// try
	// {
	// db.openDB();
	// list = getPoolmemberListAlteon(adcIndex, poolName, db);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// }
	// return list;
	// }

	// /**
	// * pool member를 추가한다.
	// *
	// * @param obj
	// * @throws SQLException
	// * @throws NullPointerException
	// * @throws IllegalArgumentException
	// */
	// public void addPoolmemberInfo(Integer adcIndex, String poolIndex, String
	// nodeIndex, Integer portNum, Integer state, Integer status) throws OBException
	// {
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
	// addPoolmemberInfo(adcIndex, poolIndex, nodeIndex, portNum, state, status,
	// db);
	// }
	// catch(OBException e)
	// {
	// db.closeDB();
	// throw e;
	// }
	// catch(Exception e)
	// {
	// db.closeDB();
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	//
	// db.closeDB();
	// }

	// /**
	// * pool member를 추가한다.
	// *
	// * @param obj
	// * @param db
	// * @throws SQLException
	// * @throws NullPointerException
	// * @throws IllegalArgumentException
	// */
	// public void addPoolmemberInfo(Integer adcIndex, String poolIndex, String
	// nodeIndex, Integer portNum, Integer state, Integer status, OBDatabase db)
	// throws OBException
	// {
	// String sqlText="";
	// try
	// {
	// String index=adcIndex+"_"+poolIndex+"_"+nodeIndex+"_"+portNum;//--
	// adcIndex+poolIndex+nodeIndex+port 조합.
	// sqlText = String.format(
	// " INSERT INTO TMP_SLB_POOLMEMBER " +
	// " (INDEX, ADC_INDEX, POOL_INDEX, NODE_INDEX, PORT, STATE, STATUS) " +
	// " VALUES " +
	// " (%s, %d, %s, %s, %d, %d, %d);",
	// OBParser.sqlString(index),
	// adcIndex,
	// OBParser.sqlString(poolIndex),
	// OBParser.sqlString(nodeIndex),
	// portNum,
	// state,
	// status);
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void addPoolmemberInfoAlteon(Integer adcIndex, ArrayList<OBDtoAdcPoolAlteon> serverPools,
			StringBuilder query) throws OBException {
		if (serverPools.size() == 0) {
			return;
		}

		final String delimiter = ", ";
		String prefix = "";
		boolean queryTrigger = true;

		for (OBDtoAdcPoolAlteon pool : serverPools) {
			final ArrayList<OBDtoAdcPoolMemberAlteon> memberList = pool.getMemberList();
			for (OBDtoAdcPoolMemberAlteon member : memberList) {
				if (queryTrigger) {
					query.append(" INSERT INTO TMP_SLB_POOLMEMBER ")
							.append("     (INDEX, ADC_INDEX, POOL_INDEX, NODE_INDEX, PORT, STATE) ").append(" VALUES ");

					queryTrigger = false;
				}

				String poolIndex = OBCommon.makePoolIndex(adcIndex, pool.getAlteonId());// adcIndex+"_"+pool.getAlteonId();
				String nodeIndex = OBCommon.makeNodeIndexAlteon(adcIndex, member.getAlteonNodeID());// adcIndex+"_"+member.getAlteonNodeID();
				String index = OBCommon.makePoolMemberIndexAlteon(adcIndex, pool.getAlteonId(),
						member.getAlteonNodeID(), member.getPort());// adcIndex+"_"+poolIndex+"_"+nodeIndex+"_"+member.getPort();//--
																	// adcIndex+poolIndex+nodeIndex+port 조합.

				query.append(prefix).append("(").append(OBParser.sqlString(index)).append(delimiter).append(adcIndex)
						.append(delimiter).append(OBParser.sqlString(poolIndex)).append(delimiter)
						.append(OBParser.sqlString(nodeIndex)).append(delimiter).append(member.getPort())
						.append(delimiter).append(member.getState()).append(")");

				prefix = ", ";
			}
		}

		if (!queryTrigger) {
			query.append(";");
		}
	}

	public void updateAdcAdditionalInfo(OBDtoAdcAdditional info) throws OBException {
		StringBuilder query = new StringBuilder();

		query.append(" BEGIN; ").append(" DELETE FROM TMP_ADC_ADDITIONAL ").append(" WHERE ADC_INDEX = ")
				.append(info.getAdcIndex()).append(";");

		final String delimiter = ", ";

		query.append(" INSERT INTO TMP_ADC_ADDITIONAL ").append(
				" (ADC_INDEX, SYNC_STATE, PEER_IP, VRRP_IPVER, VRRP_PRIORITY, VRRP_SHARE, VRRP_TRACK_VRS, VRRP_TRACK_IFS, VRRP_TRACK_PORTS, VRRP_TRACK_L4PTS, VRRP_TRACK_REALS, VRRP_TRACK_HSRP, VRRP_TRACK_HSRV) ")
				.append(" VALUES ").append("(").append(info.getAdcIndex()).append(delimiter).append(info.getSyncState())
				.append(delimiter).append(OBParser.sqlString(info.getPeerIP())).append(delimiter)
				.append(info.getVrrpIPVer()).append(delimiter).append(info.getVrrpPriority()).append(delimiter)
				.append(info.getVrrpShare()).append(delimiter).append(info.getVrrpTrackVrs()).append(delimiter)
				.append(info.getVrrpTrackIfs()).append(delimiter).append(info.getVrrpTrackPorts()).append(delimiter)
				.append(info.getVrrpTrackL4pts()).append(delimiter).append(info.getVrrpTrackReals()).append(delimiter)
				.append(info.getVrrpTrackHsrp()).append(delimiter).append(info.getVrrpTrackHsrv()).append(");");

		query.append(" COMMIT; ");

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			db.executeUpdate(query.toString());
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + " sqlQuery: " + query.toString());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void updateAdcAdditionalInfo(OBDtoAdcAdditional info, StringBuilder query) throws OBException {
		query.append(" DELETE FROM TMP_ADC_ADDITIONAL ").append(" WHERE ADC_INDEX = ").append(info.getAdcIndex())
				.append(";");

		final String delimiter = ", ";

		query.append(" INSERT INTO TMP_ADC_ADDITIONAL ").append(
				" (ADC_INDEX, SYNC_STATE, PEER_IP, VRRP_IPVER, VRRP_PRIORITY, VRRP_SHARE, VRRP_TRACK_VRS, VRRP_TRACK_IFS, VRRP_TRACK_PORTS, VRRP_TRACK_L4PTS, VRRP_TRACK_REALS, VRRP_TRACK_HSRP, VRRP_TRACK_HSRV) ")
				.append(" VALUES ").append("(").append(info.getAdcIndex()).append(delimiter).append(info.getSyncState())
				.append(delimiter).append(OBParser.sqlString(info.getPeerIP())).append(delimiter)
				.append(info.getVrrpIPVer()).append(delimiter).append(info.getVrrpPriority()).append(delimiter)
				.append(info.getVrrpShare()).append(delimiter).append(info.getVrrpTrackVrs()).append(delimiter)
				.append(info.getVrrpTrackIfs()).append(delimiter).append(info.getVrrpTrackPorts()).append(delimiter)
				.append(info.getVrrpTrackL4pts()).append(delimiter).append(info.getVrrpTrackReals()).append(delimiter)
				.append(info.getVrrpTrackHsrp()).append(delimiter).append(info.getVrrpTrackHsrv()).append("); ");
	}

	/**
	 * 지정된 장비에서 pool memeber 정보를 삭제한다.
	 * 
	 * @param adcIndex
	 * @param db
	 */
	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void delPoolmemberAll(Integer adcIndex, StringBuilder query) throws OBException {
		delTableAll(adcIndex, "TMP_SLB_POOLMEMBER", query);
	}

	public void delPoolmemberPartial(Integer adcIndex, ArrayList<String> poolIndexList, StringBuilder query)
			throws OBException { // 주의: POOL_INDEX로 지운다. 멤버지만 POOL을 통으로 날리고 업데이트 하기 때문
		delTablePartial(adcIndex, "TMP_SLB_POOLMEMBER", "POOL_INDEX", poolIndexList, query);
	}

	public void delPoolmemberAll(Integer adcIndex) throws OBException {
		delTableAll(adcIndex, "TMP_SLB_POOLMEMBER");
	}

	public void delPoolmemberStatusAll(Integer adcIndex, StringBuilder query) throws OBException {
		delTableAll(adcIndex, "TMP_SLB_POOLMEMBER_STATUS", query);
	}

	public void delPoolmemberStatusPartial(Integer adcIndex, ArrayList<String> vsIndexList, StringBuilder query)
			throws OBException {
		delTablePartial(adcIndex, "TMP_SLB_POOLMEMBER_STATUS", "VS_INDEX", vsIndexList, query);
	}

	private String getF5ProfileOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY PROFILE_NAME ASC NULLS LAST ";
		switch (orderType) {
		case OBDefine.ORDER_TYPE_FIRST:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY PROFILE_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY PROFILE_NAME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_SECOND:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY TIMEOUT ASC NULLS LAST , PROFILE_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY TIMEOUT DESC NULLS LAST , PROFILE_NAME ASC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_THIRD:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY MATCH_ACROSS_SERVICE_YN ASC NULLS LAST , PROFILE_NAME ASC NULLS LAST ";
			else
				retVal = " ORDER BY MATCH_ACROSS_SERVICE_YN DESC NULLS LAST , PROFILE_NAME ASC NULLS LAST ";
			break;
		}
		return retVal;
	}

	/**
	 * F5용 프로파일 목록을 지정된 장비에서 추출한다.
	 * 
	 * @param adcIndex
	 * @return
	 * @throws SQLException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public ArrayList<OBDtoAdcProfile> getF5ProfileList(Integer adcIndex, String profileIndex, Integer orderType,
			Integer orderDir) throws OBException {
		String sqlText = "";
		if (profileIndex == null) {
			profileIndex = "";
		}

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (profileIndex.isEmpty()) {
				sqlText = String.format(
						" SELECT INDEX, ADC_INDEX, PROFILE_NAME, PERSISTENCE_TYPE, PARENT_PROFILE, MATCH_ACROSS_SERVICE_YN, TIMEOUT "
								+ " FROM TMP_SLB_PROFILE " + " WHERE ADC_INDEX = %d AND PERSISTENCE_TYPE > -1 ",
						adcIndex);
			} else {
				sqlText = String.format(
						" SELECT INDEX, ADC_INDEX, PROFILE_NAME, PERSISTENCE_TYPE, PARENT_PROFILE, MATCH_ACROSS_SERVICE_YN, TIMEOUT "
								+ " FROM TMP_SLB_PROFILE "
								+ " WHERE ADC_INDEX = %d AND INDEX = %s AND PERSISTENCE_TYPE > -1 ",
						adcIndex, OBParser.sqlString(profileIndex));
			}

			sqlText += getF5ProfileOrderType(orderType, orderDir);
			ResultSet rs = db.executeQuery(sqlText);

			ArrayList<OBDtoAdcProfile> list = new ArrayList<OBDtoAdcProfile>();
			while (rs.next()) {
				OBDtoAdcProfile obj = new OBDtoAdcProfile();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setProfileName(db.getString(rs, "PROFILE_NAME"));
				obj.setPersistenceType(db.getInteger(rs, "PERSISTENCE_TYPE"));
				obj.setParentProfile(db.getString(rs, "PARENT_PROFILE"));
				obj.setMatchAcrossServiceYN(db.getInteger(rs, "MATCH_ACROSS_SERVICE_YN"));
				obj.setTimeout(db.getLong(rs, "TIMEOUT"));
				list.add(obj);
			}
			return list;
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

	// F5 ADC의 persistence profile 목록을 가져온다.
	public ArrayList<OBDtoAdcProfile> searchF5ProfileList(Integer adcIndex, String searchKey, Integer orderType,
			Integer orderDir) throws OBException {
		String sqlText = "";
		String wildcardKey;
		if (searchKey == null) {
			searchKey = "";
		}

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			// if(profileIndex.isEmpty())
			if (searchKey.isEmpty() == true) {
				sqlText = String.format(
						" SELECT INDEX, ADC_INDEX, PROFILE_NAME, PERSISTENCE_TYPE, PARENT_PROFILE, MATCH_ACROSS_SERVICE_YN, TIMEOUT "
								+ " FROM TMP_SLB_PROFILE " + " WHERE ADC_INDEX = %d AND PERSISTENCE_TYPE > -1 ",
						adcIndex);
			} else {
				// #3984-2 #12: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlText = String.format(
						" SELECT INDEX, ADC_INDEX, PROFILE_NAME, PERSISTENCE_TYPE, PARENT_PROFILE, MATCH_ACROSS_SERVICE_YN, TIMEOUT "
								+ " FROM TMP_SLB_PROFILE "
								+ " WHERE ADC_INDEX = %d AND PROFILE_NAME LIKE %s AND PERSISTENCE_TYPE > -1 ",
						adcIndex, OBParser.sqlString(wildcardKey));
			}

			sqlText += getF5ProfileOrderType(orderType, orderDir);

			ResultSet rs = db.executeQuery(sqlText);

			ArrayList<OBDtoAdcProfile> list = new ArrayList<OBDtoAdcProfile>();
			while (rs.next()) {
				OBDtoAdcProfile obj = new OBDtoAdcProfile();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setProfileName(db.getString(rs, "PROFILE_NAME"));
				obj.setPersistenceType(db.getInteger(rs, "PERSISTENCE_TYPE"));
				obj.setParentProfile(db.getString(rs, "PARENT_PROFILE"));
				obj.setMatchAcrossServiceYN(db.getInteger(rs, "MATCH_ACROSS_SERVICE_YN"));
				obj.setTimeout(db.getLong(rs, "TIMEOUT"));
				list.add(obj);
			}
			return list;
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

	public String getF5ProfileName(String profileIndex) throws OBException {
		if (profileIndex == null || profileIndex.isEmpty() == true) {
			return null;
		}

		OBDatabase db = new OBDatabase();

		ArrayList<String> profileIndexList = new ArrayList<String>();
		profileIndexList.add(profileIndex);
		ArrayList<String> list = null;
		String profileName = null;
		try {
			db.openDB();
			list = getF5ProfileNameList(profileIndexList);
			if (list == null || list.get(0) == null) {
				profileName = null;
			} else {
				profileName = list.get(0);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		return profileName;
	}

	// F5 ADC의 persistence profile 이름목록을 가져온다.
	public ArrayList<String> getF5ProfileNameList(ArrayList<String> profileIndexList) throws OBException {
		String sqlText = "";
		String whereText = "''"; // where-in empty방지

		if (profileIndexList.size() == 0) {
			return null;
		}

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			for (int i = 0; i < profileIndexList.size(); i++) {
				whereText += ", ";
				whereText += OBParser.sqlString(profileIndexList.get(i));
			}

			sqlText = String.format(" SELECT PROFILE_NAME " + " FROM TMP_SLB_PROFILE "
					+ " WHERE INDEX IN ( %s ) ORDER BY PROFILE_NAME;", whereText); // where-in:empty string 불가, null 불가,
																					// OK

			ResultSet rs = db.executeQuery(sqlText);

			ArrayList<String> list = new ArrayList<String>();
			while (rs.next()) {
				list.add(db.getString(rs, "PROFILE_NAME"));
			}
			return list;
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

	/**
	 * F5의 persistence profile을 추가한다. 엔트리를 하나씩 insert한다. 한꺼번에 넣는 함수는 이 다음의
	 * addF5ProfileAll()이다.
	 */
	public void addF5ProfileInfo(Integer adcIndex, OBDtoAdcProfile obj) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			String index = OBCommon.makeProfileIndexF5(adcIndex, obj.getProfileName());
			sqlText = String.format(" INSERT INTO TMP_SLB_PROFILE "
					+ "     (INDEX, ADC_INDEX, PROFILE_NAME, PERSISTENCE_TYPE, PARENT_PROFILE, MATCH_ACROSS_SERVICE_YN, TIMEOUT) "
					+ " VALUES " + "     (%s, %d, %s, %d, %s, %d, %d);", OBParser.sqlString(index), adcIndex,
					OBParser.sqlString(obj.getProfileName()), obj.getPersistenceType(),
					OBParser.sqlString(obj.getParentProfile()), obj.getMatchAcrossServiceYN(), obj.getTimeout());

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

	/**
	 * F5의 persistence profile을 추가한다. 엔트리들 전체를 한번에 insert한다. 앞의 addF5ProfileInfo()와
	 * 같은 기능인데 작업 단위가 다르다.
	 */

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void addF5ProfileInfoAll(Integer adcIndex, ArrayList<OBDtoAdcProfile> profileList, StringBuilder query)
			throws OBException {
		if (profileList.size() == 0) {
			return;
		}

		final String delimiter = ", ";
		String prefix = "";

		query.append(" INSERT INTO TMP_SLB_PROFILE ").append(
				" (INDEX, ADC_INDEX, PROFILE_NAME, PERSISTENCE_TYPE, PARENT_PROFILE, MATCH_ACROSS_SERVICE_YN, TIMEOUT) ")
				.append(" VALUES ");

		for (OBDtoAdcProfile profile : profileList) {
			String index = OBCommon.makeProfileIndexF5(adcIndex, profile.getProfileName());
			query.append(prefix).append("(").append(OBParser.sqlString(index)).append(delimiter).append(adcIndex)
					.append(delimiter).append(OBParser.sqlString(profile.getProfileName())).append(delimiter)
					.append(profile.getPersistenceType()).append(delimiter)
					.append(OBParser.sqlString(profile.getParentProfile())).append(delimiter)
					.append(profile.getMatchAcrossServiceYN()).append(delimiter).append(profile.getTimeout())
					.append(")");

			prefix = ", ";
		}

		query.append(" ; ");
	}

	public void addF5VlanAll(Integer adcIndex, ArrayList<OBDtoAdcVlan> vlanList, StringBuilder query)
			throws OBException {
		int vlanCount = vlanList.size();
		boolean bProfileQueryDone = false;

		if (vlanCount == 0) {
			return;
		}

		String sqlText = "";
		try {
			sqlText = String.format(" INSERT INTO TMP_SLB_VLANTUNNEL " + "     (ADC_INDEX, VLAN_NAME) " + " VALUES ");
			OBDtoAdcVlan obj;
			int i;

			for (i = 0; i < vlanCount; i++) {
				obj = vlanList.get(i);
				sqlText += String.format(" (%d, %s)", adcIndex, OBParser.sqlString(obj.getVlanName()));
				if (i == (vlanCount - 1)) {
					sqlText += ";";
					bProfileQueryDone = true;
				} else {
					sqlText += ",";
				}
			}

			if (bProfileQueryDone) {
				query.append(sqlText);
			} else {
				throw new OBException(OBException.ERRCODE_DB_QEURY,
						String.format("sqlText = %s, vlan insert query error", sqlText));
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public void addF5VlanFilterAll(Integer adcIndex, ArrayList<DtoVirtualServer> vlanFilterList, StringBuilder query)
			throws OBException {
		if (vlanFilterList.size() == 0) {
			return;
		}

		query.append(" INSERT INTO TMP_SLB_VLANTUNNEL_FILTER ").append("     (INDEX, ADC_INDEX, STATUS, VLAN_NAME) ")
				.append(" VALUES ");

		String prefix = "";

		for (DtoVirtualServer vlanFilter : vlanFilterList) {
			String index = OBCommon.makeVSIndexF5(adcIndex, vlanFilter.getName());
			ArrayList<String> vlanNameList = vlanFilter.getVlanTunnel().getVlanName();

			if (vlanNameList == null || vlanNameList.isEmpty()) {
				query.append(prefix).append("(").append(OBParser.sqlString(index)) // index
						.append(",").append(adcIndex) // adc_index
						.append(",").append(vlanFilter.getVlanTunnel().getStatus()) // status
						.append(",").append("null") // vlan_name
						.append(")");

				prefix = ", ";
			} else {
				for (String vlanName : vlanNameList) {
					query.append(prefix).append("(").append(OBParser.sqlString(index)) // index
							.append(",").append(adcIndex) // adc_index
							.append(",").append(vlanFilter.getVlanTunnel().getStatus()) // status
							.append(",").append(OBParser.sqlString(vlanName)) // vlan_name
							.append(")");

					prefix = ", ";
				}
			}
		}

		query.append(";");
	}

	public void addF5VlanFilter(Integer adcIndex, OBDtoAdcVServerF5 vlanFilterList, StringBuilder query)
			throws OBException {
		if (vlanFilterList == null) {
			return;
		}

		query.append(" INSERT INTO TMP_SLB_VLANTUNNEL_FILTER ").append("     (INDEX, ADC_INDEX, STATUS, VLAN_NAME) ")
				.append(" VALUES ");

		String prefix = "";

		String index = OBCommon.makeVSIndexF5(adcIndex, vlanFilterList.getName());
		ArrayList<String> vlanNameList = vlanFilterList.getVlanFilter().getVlanName();

		if (vlanNameList == null || vlanNameList.isEmpty()) {
			query.append(prefix).append("(").append(OBParser.sqlString(index)) // index
					.append(",").append(adcIndex) // adc_index
					.append(",").append(vlanFilterList.getVlanFilter().getStatus()) // status
					.append(",").append("null") // vlan_name
					.append(")");

		} else {
			for (String vlanName : vlanNameList) {
				query.append(prefix).append("(").append(OBParser.sqlString(index)) // index
						.append(",").append(adcIndex) // adc_index
						.append(",").append(vlanFilterList.getVlanFilter().getStatus()) // status
						.append(",").append(OBParser.sqlString(vlanName)) // vlan_name
						.append(")");
				prefix = ", ";
			}
		}

		query.append(";");
	}

	/**
	 * F5용 프로파일 목록을 DB에서 삭제한다. profileName이 null이면 전체 삭제
	 */
	public void delF5ProfileAll(Integer adcIndex) throws OBException {
		delTableAll(adcIndex, "TMP_SLB_PROFILE");
	}

	/**
	 * F5용 프로파일 목록을 DB에서 삭제한다. profileName이 null이면 전체 삭제
	 */

	public void delF5VlanAll(Integer adcIndex, StringBuilder query) throws OBException {
		delTableAll(adcIndex, "TMP_SLB_VLANTUNNEL", query);
	}

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void delF5ProfileAll(Integer adcIndex, StringBuilder query) throws OBException {
		delTableAll(adcIndex, "TMP_SLB_PROFILE", query);
	}

	/**
	 * F5 SSL 인증서 DB테이블을 clear한다.
	 */
	// public void delF5SslCertAll(Integer adcIndex, OBDatabase db) throws
	// OBException
	// {
	// String sqlText="";
	// try
	// {
	// sqlText = String.format("DELETE FROM TMP_ADC_CERTIFICATE WHERE ADC_INDEX =
	// %d; ", adcIndex);
	//
	// db.executeUpdate(sqlText);
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void delF5SslCertAll(Integer adcIndex, StringBuilder query) {
		query.append(" DELETE FROM TMP_ADC_CERTIFICATE WHERE ADC_INDEX = ").append(adcIndex).append(";");
	}

	/**
	 * 유효하지 않은 virtual server 공지그룹정보 삭제
	 * 
	 * @param adcIndex
	 * @param db
	 * @throws OBException
	 */
	public void delDeadVServerNotice(Integer adcIndex) throws OBException {
		// 현재 유효 notice 그룹을 갖지 않은 엔트리들을 뺀다.
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" DELETE FROM TMP_VS_NOTICE " + " WHERE ADC_INDEX = %d "
					+ "     AND NOTICE_POOL_INDEX NOT IN (SELECT POOL_INDEX FROM MNG_NOTICE_GROUP WHERE ADC_INDEX = %d) ",
					adcIndex, adcIndex);
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

	public void updateVServerNoticeChange(Integer adcIndex, int changeType,
			ArrayList<OBDtoAdcConfigChunkF5> configChunkList, Integer accntIndex) throws OBException {
		if (configChunkList == null || configChunkList.size() == 0) {
			return;
		}
		String sqlText = "";
		String sqlValues = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			// 변경을 반영한다.
			if (changeType == OBDefine.CHANGE_TYPE_EDIT_NOTICEON) // notice 추가
			{
				for (OBDtoAdcConfigChunkF5 chunk : configChunkList) {
					sqlValues += String.format(", (%d, %s, %s, %s, %d)", adcIndex,
							OBParser.sqlString(chunk.getVsConfig().getVsNew().getIndex()),
							OBParser.sqlString(chunk.getVsConfig().getVsOld().getPool().getIndex()) // service pool,
																									// unused
							, OBParser.sqlString(chunk.getVsConfig().getVsNew().getPool().getIndex()) // notice pool,
																										// currently
																										// effective
							, accntIndex);
				}
				sqlValues = sqlValues.substring(1, sqlValues.length()); // 머리의 comma 제거

				sqlText = String.format(" INSERT INTO TMP_VS_NOTICE                                        \n"
						+ "     (ADC_INDEX, VS_INDEX, SERVICE_POOL_INDEX, NOTICE_POOL_INDEX, ACCNT_INDEX) \n"
						+ " VALUES %s                                                        \n", sqlValues);

				db.executeUpdate(sqlText);
			} else
			// notice 제거
			{
				for (OBDtoAdcConfigChunkF5 chunk : configChunkList) {
					sqlValues += (", " + OBParser.sqlString(chunk.getVsConfig().getVsNew().getIndex()));
				}
				sqlValues = sqlValues.substring(1, sqlValues.length()); // 머리의 comma 제거
				sqlText = String.format(" DELETE FROM TMP_VS_NOTICE WHERE VS_INDEX IN (%s) AND ACCNT_INDEX = %d",
						sqlValues, accntIndex);
				db.executeUpdate(sqlText);
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
	}

	public void updateVServerNoticeChangeAlteon(Integer adcIndex, int changeType,
			ArrayList<OBDtoAdcVServerNotice> vsList, Integer accntIndex) throws OBException {
		if (vsList == null || vsList.size() == 0) {
			return;
		}
		String sqlText = "";
		String sqlValues = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			// 변경을 반영한다.
			if (changeType == OBDefine.CHANGE_TYPE_EDIT_NOTICEON) // notice 추가
			{
				for (OBDtoAdcVServerNotice vs : vsList) {
					sqlValues += String.format(", (%d, %s, %s, %s, %d)", adcIndex, OBParser.sqlString(vs.getIndex()),
							OBParser.sqlString(vs.getServicePoolIndex()) // service pool, unused
							, OBParser.sqlString(vs.getNoticePoolIndex()) // notice pool, currently effective
							, accntIndex);
				}
				sqlValues = sqlValues.substring(1, sqlValues.length()); // 머리의 comma 제거

				sqlText = String.format(" INSERT INTO TMP_VS_NOTICE                                        \n"
						+ "     (ADC_INDEX, VS_INDEX, SERVICE_POOL_INDEX, NOTICE_POOL_INDEX, ACCNT_INDEX) \n"
						+ " VALUES %s                                                        \n", sqlValues);

				db.executeUpdate(sqlText);
			} else
			// notice 제거
			{
				for (OBDtoAdcVServerNotice vs : vsList) {
					sqlValues += (", " + OBParser.sqlString(vs.getIndex()));
				}
				sqlValues = sqlValues.substring(1, sqlValues.length()); // 머리의 comma 제거
				sqlText = String.format(" DELETE FROM TMP_VS_NOTICE WHERE VS_INDEX IN (%s) ", sqlValues);
				db.executeUpdate(sqlText);
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
	}

	/**
	 * F5 SSL 인증서를 DB 테이블에 넣는다.
	 * 
	 * @param adcIndex
	 * @param vsList
	 * @param db
	 * @throws OBException
	 */
	// public void addF5SslCertAll(Integer adcIndex,
	// ArrayList<OBDtoAdcSslCertificate> sslCertList, OBDatabase db) throws
	// OBException
	// {
	// int sslCertCount = sslCertList.size();
	// boolean bQueryDone = false;
	//
	// if(sslCertCount==0)
	// {
	// return;
	// }
	//
	// String index;
	// OBDtoAdcSslCertificate obj;
	// int i;
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(
	// " INSERT INTO TMP_ADC_CERTIFICATE \n" +
	// " (INDEX, ADC_INDEX, NAME, ORGANIZATION, EXPIRATION_DATE) \n" +
	// " VALUES \n"
	// );
	//
	// for(i=0; i<sslCertCount; i++)
	// {
	// obj = sslCertList.get(i);
	// index = OBCommon.makeCertIndexF5(adcIndex, obj.getCertificateName());
	// sqlText += String.format(" (%s, %d, %s, %s, %s) ",
	// OBParser.sqlString(index),
	// adcIndex,
	// OBParser.sqlString(obj.getCertificateName()),
	// OBParser.sqlString(obj.getOrganizationName()),
	// OBParser.sqlString(obj.getExpirationDate())
	// );
	//
	// if(i==(sslCertCount-1))
	// {
	// sqlText += ";";
	// bQueryDone = true;
	// }
	// else
	// {
	// sqlText += ",";
	// }
	// }
	//
	// if(bQueryDone==true)
	// {
	// db.executeUpdate(sqlText);
	// }
	// else
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("sqlText =
	// %s, SSL Certificate insert query error", sqlText));
	// }
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void addF5SslCertAll(Integer adcIndex, ArrayList<OBDtoAdcSslCertificate> sslCertList, StringBuilder query)
			throws OBException {
		if (sslCertList.size() == 0) {
			return;
		}

		final String delimiter = ", ";
		String prefix = "";

		query.append(" INSERT INTO TMP_ADC_CERTIFICATE ")
				.append(" (INDEX, ADC_INDEX, NAME, ORGANIZATION, EXPIRATION_DATE) ").append(" VALUES ");

		for (OBDtoAdcSslCertificate obj : sslCertList) {
			String index = OBCommon.makeCertIndexF5(adcIndex, obj.getCertificateName());

			query.append(prefix).append("(").append(OBParser.sqlString(index)).append(delimiter).append(adcIndex)
					.append(delimiter).append(OBParser.sqlString(obj.getCertificateName())).append(delimiter)
					.append(OBParser.sqlString(obj.getOrganizationName())).append(delimiter)
					.append(OBParser.sqlString(obj.getExpirationDate())).append(")");

			prefix = ", ";
		}

		query.append(";");
	}

	public Integer allocNodeID(Integer adcIndex) throws OBException {
		ArrayList<OBDtoAdcNodeAlteon> list;
		try {
			list = getNodeListAllAlteon(adcIndex);

			for (int i = 1; i < 1024; i++) {
				int j = 0;
				for (j = 0; j < list.size(); j++) {
					if (Integer.toString(i) == list.get(j).getAlteonId()) {
						break;
					}
				}
				if (j == list.size())
					return i;
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return list.size() + 1;
	}

	public boolean isUnusedNodeID(Integer adcIndex, Integer alteonID) throws OBException {
		String sqlText = "";
		boolean retVal = false;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" SELECT INDEX " + " FROM TMP_SLB_NODE " + " WHERE ADC_INDEX=%d AND ALTEON_ID=%s;",
					adcIndex, OBParser.sqlString(Integer.toString(alteonID)));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false)
				retVal = true;
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

	// 중복 기능 존재, getValidPoolIndex에서 동일하게 1부터 1024 까지 순차적으로 비교하는 기능 존재
	public Integer allocPoolID(Integer adcIndex) throws OBException {
		ArrayList<OBDtoAdcPoolAlteon> list;
		try {
			list = getPoolListAllAlteon(adcIndex);

			for (int i = 1; i < 1024; i++) {
				int j = 0;
				for (j = 0; j < list.size(); j++) {
					if (Integer.toString(i) == list.get(j).getAlteonId()) {
						break;
					}
				}
				if (j == list.size())
					return i;
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return list.size() + 1;
	}

	public boolean isUnusedPoolID(Integer adcIndex, Integer alteonID) throws OBException {
		String sqlText = "";
		boolean retVal = false;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX " + " FROM TMP_SLB_POOL " + " WHERE ADC_INDEX=%d AND ALTEON_ID=%s;",
					adcIndex, OBParser.sqlString(Integer.toString(alteonID)));

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false)
				retVal = true;
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

	void delAllTable(int adcIndex) throws OBException {
		try {
			delVServerAll(adcIndex);
			delNodeAll(adcIndex);
			delPoolAll(adcIndex);
			delVServiceAll(adcIndex);
			delPoolmemberAll(adcIndex);
			delL3InterfaceAll(adcIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// Virtual server 이름(만) 중복 확인
	public boolean isExistVirtualServer(Integer adcIndex, String vsName) throws OBException {
		OBDatabase db = new OBDatabase();

		boolean result;
		try {
			db.openDB();
			result = isExistVirtualServer(adcIndex, vsName, db);
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

	private boolean isExistVirtualServer(Integer adcIndex, String vsName, OBDatabase db) throws OBException {
		String sqlText = "";

		if (vsName == null) {
			return false;
		}

		try {
			sqlText = String.format(" SELECT NAME " + " FROM TMP_SLB_VSERVER " + " WHERE ADC_INDEX=%d AND NAME=%s;",
					adcIndex, OBParser.sqlString(vsName));

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// Virtual server 이름(만) 중복 확인
	public boolean isExistVirtualServer(Integer adcIndex, ArrayList<String> vsIndexList) throws OBException {
		if (vsIndexList == null) {
			return false;
		}

		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			for (String vsIndex : vsIndexList) {
				ResultSet rs = db.executeQuery(" SELECT NAME FROM TMP_SLB_VSERVER WHERE ADC_INDEX=" + adcIndex
						+ " AND INDEX=" + OBParser.sqlString(vsIndex) + "; ");

				if (rs.next() == false) {
					return false;
				}
			}
			return true;
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

	public boolean isExistVirtualServerAlteon(Integer adcIndex, String alteonID, String ipAddress) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, alteonID:%s, ipAddress:%s", adcIndex, alteonID, ipAddress));

		if (ipAddress == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "virtual ipaddress is null");
		}
		if (ipAddress.isEmpty()) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "virtual ipaddress is empty");
		}

		if (alteonID == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "virtual server index is null");
		}

		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT INDEX " + " FROM TMP_SLB_VSERVER " + " WHERE ADC_INDEX=%d AND ALTEON_ID = %s ;", adcIndex,
					OBParser.sqlString(alteonID));

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return false;
			}
			return true;
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

	public boolean isExistVirtualServerF5(Integer adcIndex, String vsName, String address, Integer port)
			throws OBException {
		OBDatabase db = new OBDatabase();

		boolean result;
		try {
			db.openDB();
			result = isExistVirtualServerF5(adcIndex, vsName, address, port, db);
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

	private boolean isExistVirtualServerF5(Integer adcIndex, String vsName, String address, Integer port, OBDatabase db)
			throws OBException {
		String sqlText = "";

		if (vsName == null || address == null || port == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
					String.format("virtual server name or address or port is null"));
		}

		if (vsName.isEmpty() || address.isEmpty()) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
					String.format("virtual server name or address is empty"));
		}

		try {
			sqlText = String.format(
					" SELECT * " + " FROM TMP_SLB_VSERVER "
							+ " WHERE ADC_INDEX=%d AND (NAME=%s OR (VIRTUAL_IP = %s AND VIRTUAL_PORT = %d));",
					adcIndex, OBParser.sqlString(vsName), OBParser.sqlString(address), port);

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return false;
			}
			return true;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	// db.openDB();
	// db2.openDB();
	// boolean list = new OBVServerDB().isExistVirtualServerPAS(5, "5_bwpark",
	// "10.10.10.10", 8080);
	// System.out.println(list);
	// db.closeDB();
	// db2.closeDB();
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	// db.openDB();
	// db2.openDB();
	// int list = new OBVServerDB().getVServerStatusPAS(5, "5_bwpark", db);
	// System.out.println(list);
	// db.closeDB();
	// db2.closeDB();
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	// private void addVServerAllPAS(Integer adcIndex, ArrayList<OBDtoAdcVServerPAS>
	// vsList, OBDatabase db) throws OBException
	// {
	// int vsCount = vsList.size();
	//
	// if(vsCount==0)
	// {
	// return;
	// }
	//
	// Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
	//
	// String vsIndex;
	// String poolIndex;
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(
	// " INSERT INTO TMP_SLB_VSERVER " +
	// " (INDEX, ADC_INDEX, APPLY_TIME, STATUS, USE_YN, NAME, VIRTUAL_IP,
	// VIRTUAL_PORT, POOL_INDEX, SRV_PROTOCOL ) " +
	// " VALUES ");
	//
	// String valString = "";
	// for(OBDtoAdcVServerPAS obj:vsList)
	// {
	// if(!valString.isEmpty())
	// valString += ", ";
	// vsIndex = OBCommon.makeVSIndexPAS(adcIndex, obj.getName());
	// poolIndex = OBCommon.makePoolIndex(adcIndex, obj.getName());// PAS의 경우에는
	// poolIndex는 adcIndex+adcName으로 구성한다.
	// valString += String.format(" (%s, %d, %s, %d, %d, %s, %s, %d, %s, %s)",
	// OBParser.sqlString(vsIndex),
	// adcIndex,
	// OBParser.sqlString(now),
	// obj.getStatus(),
	// obj.getState(),
	// OBParser.sqlString(obj.getName()),
	// OBParser.sqlString(obj.getvIP()),
	// obj.getSrvPort(),
	// OBParser.sqlString(poolIndex),
	// obj.getSrvProtocol());
	//
	// }
	//
	// sqlText += valString + ";";
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private void addVServerAllPAS(Integer adcIndex, ArrayList<OBDtoAdcVServerPAS> vsList, StringBuilder query)
			throws OBException {
		if (vsList.size() == 0) {
			return;
		}

		Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());

		final String delimiter = ", ";
		String prefix = "";

		query.append(" INSERT INTO TMP_SLB_VSERVER ").append(
				" (INDEX, ADC_INDEX, APPLY_TIME, STATUS, USE_YN, NAME, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX, SRV_PROTOCOL ) ")
				.append(" VALUES ");

		for (OBDtoAdcVServerPAS obj : vsList) {
			String vsIndex = OBCommon.makeVSIndexPAS(adcIndex, obj.getName());
			String poolIndex = OBCommon.makePoolIndex(adcIndex, obj.getName()); // PAS의 경우에는 poolIndex는
																				// adcIndex+adcName으로 구성한다.

			query.append(prefix).append("(").append(OBParser.sqlString(vsIndex)).append(delimiter).append(adcIndex)
					.append(delimiter).append(OBParser.sqlString(now)).append(delimiter).append(obj.getStatus())
					.append(delimiter).append(obj.getState()).append(delimiter)
					.append(OBParser.sqlString(obj.getName())).append(delimiter)
					.append(OBParser.sqlString(obj.getvIP())).append(delimiter).append(obj.getSrvPort())
					.append(delimiter).append(OBParser.sqlString(poolIndex)).append(delimiter)
					.append(obj.getSrvProtocol()).append(")");

			prefix = ", ";
		}

		query.append(" ; ");
	}

	// private void addVServerAllPASK(Integer adcIndex,
	// ArrayList<OBDtoAdcVServerPASK> vsList, OBDatabase db) throws OBException
	// {
	// int vsCount = vsList.size();
	//
	// if(vsCount==0)
	// {
	// return;
	// }
	//
	// Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
	//
	// String vsIndex;
	// String poolIndex;
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(
	// " INSERT INTO TMP_SLB_VSERVER " +
	// " (INDEX, ADC_INDEX, APPLY_TIME, STATUS, USE_YN, NAME, VIRTUAL_IP,
	// VIRTUAL_PORT, POOL_INDEX, SRV_PROTOCOL, SUB_INFO) " +
	// " VALUES ");
	//
	// String valString = "";
	// for(OBDtoAdcVServerPASK obj:vsList)
	// {
	// if(!valString.isEmpty())
	// {
	// valString += ", ";
	// }
	// vsIndex = OBCommon.makeVSIndexPASK(adcIndex, obj.getName());
	// poolIndex = OBCommon.makePoolIndex(adcIndex, obj.getName());// PAS의 경우에는
	// poolIndex는 adcIndex+adcName으로 구성한다.
	// valString += String.format(" (%s, %d, %s, %d, %d, %s, %s, %d, %s, %s, %s)",
	// OBParser.sqlString(vsIndex),
	// adcIndex,
	// OBParser.sqlString(now),
	// obj.getStatus(),
	// obj.getState(),
	// OBParser.sqlString(obj.getName()),
	// OBParser.sqlString(obj.getvIP()),
	// obj.getSrvPort(),
	// OBParser.sqlString(poolIndex),
	// obj.getSrvProtocol(),
	// OBParser.sqlString(obj.getSubInfo())
	// );
	// }
	// if(valString.isEmpty()==false)
	// {
	// sqlText += valString + ";";
	// db.executeUpdate(sqlText);
	// }
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	private void addVServerAllPASK(Integer adcIndex, ArrayList<OBDtoAdcVServerPASK> vsList, StringBuilder query)
			throws OBException {
		if (vsList.size() == 0) {
			return;
		}

		query.append(" INSERT INTO TMP_SLB_VSERVER ").append(
				" (INDEX, ADC_INDEX, APPLY_TIME, STATUS, USE_YN, NAME, VIRTUAL_IP, VIRTUAL_PORT, POOL_INDEX, SRV_PROTOCOL, SUB_INFO) ")
				.append(" VALUES ");

		final String delimiter = ", ";
		String prefix = "";

		Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());

		for (OBDtoAdcVServerPASK obj : vsList) {
			String vsIndex = OBCommon.makeVSIndexPASK(adcIndex, obj.getName());
			String poolIndex = OBCommon.makePoolIndex(adcIndex, obj.getName());// PAS의 경우에는 poolIndex는
																				// adcIndex+adcName으로 구성한다.

			query.append(prefix).append("(").append(OBParser.sqlString(vsIndex)).append(delimiter).append(adcIndex)
					.append(delimiter).append(OBParser.sqlString(now)).append(delimiter).append(obj.getStatus())
					.append(delimiter).append(obj.getState()).append(delimiter)
					.append(OBParser.sqlString(obj.getName())).append(delimiter)
					.append(OBParser.sqlString(obj.getvIP())).append(delimiter).append(obj.getSrvPort())
					.append(delimiter).append(OBParser.sqlString(poolIndex)).append(delimiter)
					.append(obj.getSrvProtocol()).append(delimiter).append(OBParser.sqlString(obj.getSubInfo()))
					.append(")");

			prefix = ", ";
		}

		query.append(";");
	}

	// private void addPoolAllPAS(Integer adcIndex, ArrayList<OBDtoAdcVServerPAS>
	// vsList, OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// try
	// {
	// sqlText =
	// String.format(" INSERT INTO TMP_SLB_POOL (INDEX, ADC_INDEX, NAME, ALTEON_ID,
	// LB_METHOD, HEALTH_CHECK_INDEX) " +
	// " VALUES ");
	//
	// String valString = "";
	// for(OBDtoAdcVServerPAS obj:vsList)
	// {
	// if(!valString.isEmpty())
	// valString += ", ";
	// String index = OBCommon.makePoolIndex(adcIndex, obj.getPool().getName());
	// String healthIndex = null;//OBDefine.makeHealthDbIndexPAS(adcIndex,
	// obj.getName(), );
	// if(obj.getPool().getHealthCheckInfo()!=null)
	// healthIndex = obj.getPool().getHealthCheckInfo().getDbIndex();
	// valString += String.format(" (%s, %d, %s, %d, %d, %s)",
	// OBParser.sqlString(index),
	// adcIndex,
	// OBParser.sqlString(obj.getPool().getName()),
	// 0,
	// obj.getPool().getLbMethod(),
	// OBParser.sqlString(healthIndex));
	// }
	//
	// sqlText += valString + ";";
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	private void addPoolAllPAS(Integer adcIndex, ArrayList<OBDtoAdcVServerPAS> vsList, StringBuilder query)
			throws OBException {
		if (vsList.size() == 0) {
			return;
		}

		final String delimiter = ", ";
		String prefix = "";

		query.append(" INSERT INTO TMP_SLB_POOL ")
				.append(" (INDEX, ADC_INDEX, NAME, ALTEON_ID, LB_METHOD, HEALTH_CHECK_INDEX) ").append(" VALUES ");

		for (OBDtoAdcVServerPAS obj : vsList) {
			String index = OBCommon.makePoolIndex(adcIndex, obj.getPool().getName());
			String healthIndex = null;

			query.append(prefix).append("(").append(OBParser.sqlString(index)).append(delimiter).append(adcIndex)
					.append(delimiter).append(OBParser.sqlString(obj.getPool().getName())).append(delimiter).append(0)
					.append(delimiter).append(obj.getPool().getLbMethod()).append(delimiter)
					.append(OBParser.sqlString(healthIndex)).append(")");

			prefix = ", ";
		}

		query.append(" ; ");
	}

	//
	// private void addPoolAllPASK(Integer adcIndex, ArrayList<OBDtoAdcVServerPASK>
	// vsList, OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(
	// " INSERT INTO TMP_SLB_POOL (INDEX, ADC_INDEX, NAME, ALTEON_ID, LB_METHOD,
	// HEALTH_CHECK_INDEX) " +
	// " VALUES ");
	//
	// String valString = "";
	// for(OBDtoAdcVServerPASK obj:vsList)
	// {
	// if(!valString.isEmpty())
	// valString += ", ";
	// String index = OBCommon.makePoolIndex(adcIndex, obj.getPool().getName());
	// String healthIndex = null;//OBDefine.makeHealthDbIndexPASK(adcIndex,
	// obj.getName(), );
	// if(obj.getPool().getHealthCheckInfo()!=null)
	// healthIndex = obj.getPool().getHealthCheckInfo().getDbIndex();
	// valString += String.format(" (%s, %d, %s, %d, %d, %s)",
	// OBParser.sqlString(index),
	// adcIndex,
	// OBParser.sqlString(obj.getPool().getName()),
	// 0,
	// obj.getPool().getLbMethod(),
	// OBParser.sqlString(healthIndex));
	// }
	//
	// sqlText += valString + ";";
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	private void addPoolAllPASK(Integer adcIndex, ArrayList<OBDtoAdcVServerPASK> vsList, StringBuilder query)
			throws OBException {
		if (vsList.size() == 0) {
			return;
		}

		query.append(" INSERT INTO TMP_SLB_POOL (INDEX, ADC_INDEX, NAME, ALTEON_ID, LB_METHOD, HEALTH_CHECK_INDEX) ")
				.append(" VALUES ");

		final String delimiter = ", ";
		String prefix = "";

		for (OBDtoAdcVServerPASK obj : vsList) {
			String index = OBCommon.makePoolIndex(adcIndex, obj.getPool().getName());
			String healthIndex = null;
			if (obj.getPool().getHealthCheckInfo() != null) {
				healthIndex = obj.getPool().getHealthCheckInfo().getDbIndex();
			}

			query.append(prefix).append("(").append(OBParser.sqlString(index)).append(delimiter).append(adcIndex)
					.append(delimiter).append(OBParser.sqlString(obj.getPool().getName())).append(delimiter).append(0)
					.append(delimiter).append(obj.getPool().getLbMethod()).append(delimiter)
					.append(OBParser.sqlString(healthIndex)).append(")");

			prefix = ", ";
		}

		query.append(";");
	}

	// private void addPoolMemberAllPAS(Integer adcIndex,
	// ArrayList<OBDtoAdcVServerPAS> vsList, OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(
	// " INSERT INTO TMP_SLB_POOLMEMBER (INDEX, ADC_INDEX, POOL_INDEX, NODE_INDEX,
	// PORT, STATE) " +
	// " VALUES ");
	//
	// String valString = "";
	// for(OBDtoAdcVServerPAS vsObj:vsList)
	// {
	// for(OBDtoAdcPoolMemberPAS memObj:vsObj.getPool().getMemberList())
	// {
	// if(!valString.isEmpty())
	// valString += ", ";
	//
	// String poolIndex = OBCommon.makePoolIndex(adcIndex,
	// vsObj.getPool().getName());
	// String nodeIndex = OBCommon.makeNodeIndexPAS(adcIndex,
	// vsObj.getPool().getName(), memObj.getId());
	// String index= OBCommon.makePoolMemberIndexPAS(adcIndex, vsObj.getName(),
	// vsObj.getPool().getName(), memObj.getId(), 0);//memObj.getPort());
	// valString += String.format(" (%s, %d, %s, %s, %d, %d)",
	// OBParser.sqlString(index),
	// adcIndex,
	// OBParser.sqlString(poolIndex),
	// OBParser.sqlString(nodeIndex),
	// memObj.getPort(),
	// memObj.getState());
	// }
	// }
	//
	// sqlText += valString + ";";
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	private void addPoolMemberAllPAS(Integer adcIndex, ArrayList<OBDtoAdcVServerPAS> vsList, StringBuilder query)
			throws OBException {
		final String delimiter = ", ";
		String prefix = "";
		boolean queryTrigger = true;

		for (OBDtoAdcVServerPAS obj : vsList) {
			OBDtoAdcPoolPAS pool = obj.getPool();
			for (OBDtoAdcPoolMemberPAS memObj : pool.getMemberList()) {
				if (queryTrigger) {
					query.append(
							" INSERT INTO TMP_SLB_POOLMEMBER (INDEX, ADC_INDEX, POOL_INDEX, NODE_INDEX, PORT, STATE) ")
							.append(" VALUES ");

					queryTrigger = false;
				}

				String poolIndex = OBCommon.makePoolIndex(adcIndex, pool.getName());
				String nodeIndex = OBCommon.makeNodeIndexPAS(adcIndex, pool.getName(), memObj.getId());
				String index = OBCommon.makePoolMemberIndexPAS(adcIndex, obj.getDbIndex(), nodeIndex, memObj.getPort());

				query.append(prefix).append("(").append(OBParser.sqlString(index)).append(delimiter).append(adcIndex)
						.append(delimiter).append(OBParser.sqlString(poolIndex)).append(delimiter)
						.append(OBParser.sqlString(nodeIndex)).append(delimiter).append(memObj.getPort())
						.append(delimiter).append(memObj.getState()).append(")");

				prefix = ", ";
			}
		}

		if (!queryTrigger) {
			query.append(";");
		}
	}

	// private void addPoolMemberAllPASK(Integer adcIndex,
	// ArrayList<OBDtoAdcVServerPASK> vsList, OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(
	// " INSERT INTO TMP_SLB_POOLMEMBER (INDEX, ADC_INDEX, POOL_INDEX, NODE_INDEX,
	// PORT, STATE) " +
	// " VALUES ");
	//
	// String valString = "";
	// for(OBDtoAdcVServerPASK vsObj:vsList)
	// {
	// for(OBDtoAdcPoolMemberPASK memObj:vsObj.getPool().getMemberList())
	// {
	// if(!valString.isEmpty())
	// valString += ", ";
	//
	// String poolIndex = OBCommon.makePoolIndex(adcIndex,
	// vsObj.getPool().getName());
	// String nodeIndex = OBCommon.makeNodeIndexPASK(adcIndex, memObj.getId());
	// String index= OBCommon.makePoolMemberIndexPASK(adcIndex, vsObj.getName(),
	// vsObj.getPool().getName(), memObj.getId(), 0);//memObj.getPort());
	// valString += String.format(" (%s, %d, %s, %s, %d, %d)",
	// OBParser.sqlString(index),
	// adcIndex,
	// OBParser.sqlString(poolIndex),
	// OBParser.sqlString(nodeIndex),
	// memObj.getPort(),
	// memObj.getState());
	// }
	// }
	// if(valString.isEmpty()==false) //멤버가 있을 때만 insert한다.
	// {
	// sqlText += valString + ";";
	// db.executeUpdate(sqlText);
	// }
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	private void addPoolMemberAllPASK(Integer adcIndex, ArrayList<OBDtoAdcVServerPASK> vsList, StringBuilder query)
			throws OBException {
		if (vsList.size() == 0) {
			return;
		}

		final String delimiter = ", ";
		String prefix = "";
		boolean queryTrigger = true;

		for (OBDtoAdcVServerPASK vsObj : vsList) {
			for (OBDtoAdcPoolMemberPASK memObj : vsObj.getPool().getMemberList()) {
				if (queryTrigger) {
					query.append(
							" INSERT INTO TMP_SLB_POOLMEMBER (INDEX, ADC_INDEX, POOL_INDEX, NODE_INDEX, PORT, STATE) ")
							.append(" VALUES ");

					queryTrigger = false;
				}

				String poolIndex = OBCommon.makePoolIndex(adcIndex, vsObj.getPool().getName());
				String nodeIndex = OBCommon.makeNodeIndexPASK(adcIndex, memObj.getId());
				String index = OBCommon.makePoolMemberIndexPASK(adcIndex, vsObj.getDbIndex(), nodeIndex,
						memObj.getPort());// memObj.getPort());

				query.append(prefix).append("(").append(OBParser.sqlString(index)).append(delimiter).append(adcIndex)
						.append(delimiter).append(OBParser.sqlString(poolIndex)).append(delimiter)
						.append(OBParser.sqlString(nodeIndex)).append(delimiter).append(memObj.getPort())
						.append(delimiter).append(memObj.getState()).append(")");

				prefix = ", ";
			}
		}

		if (!queryTrigger) {
			query.append(";");
		}
	}

	// private void addNodeAllPAS(Integer adcIndex, ArrayList<OBDtoAdcVServerPAS>
	// vsList, OBDatabase db) throws OBException
	// {
	// String sqlText="";
	// try
	// {
	// sqlText = String.format("INSERT INTO TMP_SLB_NODE " +
	// "(INDEX, ADC_INDEX, ALTEON_ID, IP_ADDRESS, NAME, STATE) " +
	// "VALUES ");
	//
	// String valString = "";
	// for(OBDtoAdcVServerPAS vsObj:vsList)
	// {
	// for(OBDtoAdcPoolMemberPAS memObj:vsObj.getPool().getMemberList())
	// {
	// if(!valString.isEmpty())
	// valString += ", ";
	//
	// String nodeIndex = OBCommon.makeNodeIndexPAS(adcIndex,vsObj.getName(),
	// memObj.getId());
	// valString += String.format(" (%s, %d, %d, %s, %s, %d) ",
	// OBParser.sqlString(nodeIndex),
	// adcIndex,
	// memObj.getId(),
	// OBParser.sqlString(memObj.getIpAddress()),
	// OBParser.sqlString(memObj.getName()),
	// memObj.getState());
	// }
	// }
	//
	// sqlText += valString + ";";
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	private void addNodeAllPAS(Integer adcIndex, ArrayList<OBDtoAdcVServerPAS> vsList, StringBuilder query)
			throws OBException {
		final String delimiter = ", ";
		String prefix = "";
		boolean queryTrigger = true;

		for (OBDtoAdcVServerPAS vsObj : vsList) {
			for (OBDtoAdcPoolMemberPAS memObj : vsObj.getPool().getMemberList()) {
				if (queryTrigger) {
					query.append(" INSERT INTO TMP_SLB_NODE ")
							.append(" (INDEX, ADC_INDEX, ALTEON_ID, IP_ADDRESS, NAME, STATE) ").append(" VALUES ");

					queryTrigger = false;
				}

				String nodeIndex = OBCommon.makeNodeIndexPAS(adcIndex, vsObj.getName(), memObj.getId());

				query.append(prefix).append("(").append(OBParser.sqlString(nodeIndex)).append(delimiter)
						.append(adcIndex).append(delimiter).append(memObj.getId()).append(delimiter)
						.append(OBParser.sqlString(memObj.getIpAddress())).append(delimiter)
						.append(OBParser.sqlString(memObj.getName())).append(delimiter).append(memObj.getState())
						.append(")");

				prefix = ", ";
			}
		}

		if (!queryTrigger) {
			query.append(";");
		}
	}

	private void addNodeAllPASK(Integer adcIndex, ArrayList<OBDtoRealServerInfoPASK> realList, StringBuilder query)
			throws OBException {
		if (realList.size() == 0) {
			return;
		}

		query.append(" INSERT INTO TMP_SLB_NODE ")
				.append(" (INDEX, ADC_INDEX, ALTEON_ID, IP_ADDRESS, NAME, STATE, PORT) ").append(" VALUES ");

		final String delimiter = ", ";
		String prefix = "";

		for (OBDtoRealServerInfoPASK realObj : realList) {
			String nodeIndex = OBCommon.makeNodeIndexPASK(adcIndex, realObj.getIndex());

			query.append(prefix).append("(").append(OBParser.sqlString(nodeIndex)).append(delimiter).append(adcIndex)
					.append(delimiter).append(realObj.getIndex()).append(delimiter)
					.append(OBParser.sqlString(realObj.getIpAddress())).append(delimiter)
					.append(OBParser.sqlString(realObj.getName())).append(delimiter).append(realObj.getState())
					.append(delimiter).append(realObj.getRport()).append(")");

			prefix = ", ";
		}

		query.append(";");
	}

	public void addPoolVServerPAS(Integer adcIndex, ArrayList<OBDtoAdcVServerPAS> vsList, StringBuilder query)
			throws OBException {
		int vsCount = vsList.size();

		if (vsCount == 0) {
			return;
		}

		addVServerAllPAS(adcIndex, vsList, query);
		addPoolAllPAS(adcIndex, vsList, query);
		addPoolMemberAllPAS(adcIndex, vsList, query);
		addNodeAllPAS(adcIndex, vsList, query);
	}

	public void addPoolVServerPASK(Integer adcIndex, ArrayList<OBDtoAdcVServerPASK> vsList,
			ArrayList<OBDtoRealServerInfoPASK> realList, StringBuilder query) throws OBException {
		int vsCount = vsList.size();

		if (vsCount == 0) {
			return;
		}

		addVServerAllPASK(adcIndex, vsList, query);
		addPoolAllPASK(adcIndex, vsList, query);
		addPoolMemberAllPASK(adcIndex, vsList, query);
		addNodeAllPASK(adcIndex, realList, query);
	}

	// public static void main(String[] args)
	// {
	// try
	// {
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	// db.openDB();
	// db2.openDB();
	// ArrayList<OBDtoAdcVServerPAS> list = new
	// OBVServerDB().getVServerListAllPAS(5);
	// System.out.println(list);
	// db.closeDB();
	// db2.closeDB();
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	// ArrayList<OBDtoAdcVServerPAS> getVServerListAllPAS(Integer adcIndex) throws
	// OBException
	// {
	// // OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start.
	// adcIndex:%d", adcIndex));
	//
	// OBDatabase db = new OBDatabase();
	// OBDatabase db2 = new OBDatabase();
	//
	// ArrayList<OBDtoAdcVServerPAS> list;
	// try
	// {
	// db.openDB();
	// db2.openDB();
	//
	// list = getVServerListAllPAS(adcIndex, db, db2);
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// finally
	// {
	// if(db!=null) db.closeDB();
	// if(db2!=null) db2.closeDB();
	// }
	// return list;
	// }

	public ArrayList<OBDtoAdcVServerPAS> getVServerListAllPAS(Integer adcIndex) throws OBException {
		ArrayList<OBDtoAdcVServerPAS> list = new ArrayList<OBDtoAdcVServerPAS>();
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT A.INDEX, A.ADC_INDEX, A.STATUS, A.NAME, A.VIRTUAL_IP,              \n"
					+ "   A.VIRTUAL_PORT, A.USE_YN, A.SRV_PROTOCOL, B.OCCUR_TIME                  \n"
					+ " FROM                                                                      \n"
					+ "   (SELECT * FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d) A                  \n"
					+ " LEFT JOIN (                                                               \n"
					+ "   SELECT VS_INDEX, OCCUR_TIME                                             \n"
					+ "   FROM LOG_CONFIG_HISTORY                                                 \n"
					+ "   WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX = %d GROUP BY VS_INDEX)  \n"
					+ // where-in:empty string 불가, null 불가, OK
					" ) B                                                                       \n"
					+ " ON A.INDEX = B.VS_INDEX                                                   \n"
					+ " ORDER BY A.NAME, A.VIRTUAL_IP ;", adcIndex, adcIndex);

			ResultSet rs;

			// virtual server 정보 추출.
			rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcVServerPAS obj = new OBDtoAdcVServerPAS();
				obj.setDbIndex(db.getString(rs, "INDEX"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				obj.setState(db.getInteger(rs, "USE_YN"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setvIP(db.getString(rs, "VIRTUAL_IP"));
				obj.setSrvPort(db.getInteger(rs, "VIRTUAL_PORT"));
				obj.setSrvProtocol(db.getInteger(rs, "SRV_PROTOCOL"));
				obj.setApplyTime(db.getTimestamp(rs, "OCCUR_TIME"));
				list.add(obj);
			}

			// virtual server에 할당된 pool memeber 정보 추출.
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

	public ArrayList<OBDtoAdcVServerPASK> getVServerListAllPASK(Integer adcIndex) throws OBException {
		ArrayList<OBDtoAdcVServerPASK> list = new ArrayList<OBDtoAdcVServerPASK>();
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT A.INDEX, A.ADC_INDEX, A.STATUS, A.NAME, A.VIRTUAL_IP,              \n"
					+ "   A.VIRTUAL_PORT, A.USE_YN, A.SRV_PROTOCOL, A.SUB_INFO, B.OCCUR_TIME      \n"
					+ " FROM                                                                      \n"
					+ "   (SELECT * FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d) A                  \n"
					+ " LEFT JOIN (                                                               \n"
					+ "   SELECT VS_INDEX, OCCUR_TIME                                             \n"
					+ "   FROM LOG_CONFIG_HISTORY                                                 \n"
					+ "   WHERE LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY WHERE ADC_INDEX = %d GROUP BY VS_INDEX)  \n"
					+ // where-in:empty string 불가, null 불가, OK
					" ) B                                                                       \n"
					+ " ON A.INDEX = B.VS_INDEX                                                   \n"
					+ " ORDER BY A.NAME, A.VIRTUAL_IP ;", adcIndex, adcIndex);

			ResultSet rs;

			// virtual server 정보 추출.
			rs = db.executeQuery(sqlText);
			complexInfoPASK info;
			while (rs.next()) {
				OBDtoAdcVServerPASK obj = new OBDtoAdcVServerPASK();
				obj.setDbIndex(db.getString(rs, "INDEX"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setStatus(db.getInteger(rs, "STATUS"));
				obj.setState(db.getInteger(rs, "USE_YN"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setvIP(db.getString(rs, "VIRTUAL_IP"));
				obj.setSrvPort(db.getInteger(rs, "VIRTUAL_PORT"));
				obj.setSrvProtocol(db.getInteger(rs, "SRV_PROTOCOL"));
				obj.setApplyTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setSubInfo(db.getString(rs, "SUB_INFO"));

				info = getComplexInfoPASK(obj.getSubInfo());
				obj.setvIPView(info.getVip());
				obj.setSrvPortView(info.getVport());
				obj.setConfigurable(info.isConfigurable());

				list.add(obj);
			}
			// virtual server에 할당된 pool memeber 정보 추출.
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

	//
	// public void delHealthCheckAll(Integer adcIndex, OBDatabase db) throws
	// OBException
	// {
	// delAdcConfigAll(adcIndex, "TMP_SLB_HEALTHCHECK", db); // 지정된 ADC의 health
	// check 목록을 삭제함.
	// }

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void delHealthCheckAll(Integer adcIndex, StringBuilder query) {
		delTableAll(adcIndex, "TMP_SLB_HEALTHCHECK", query); // 지정된 ADC의 health check 목록을 삭제함.
	}

	// public void delFilterAndPortMappingAll(Integer adcIndex, OBDatabase db)
	// throws OBException
	// {
	// delAdcConfigAll(adcIndex, "TMP_FLB_FILTER", db);
	// delAdcConfigAll(adcIndex, "TMP_FLB_PORT_FILTER_MAP", db);
	// }

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void delFilterAndPortMappingAll(Integer adcIndex, StringBuilder query) {
		delTableAll(adcIndex, "TMP_FLB_FILTER", query);
		delTableAll(adcIndex, "TMP_FLB_PORT_FILTER_MAP", query);
	}

	// private void delAdcConfigAll(Integer adcIndex, String configTableName,
	// OBDatabase db) throws OBException
	// {
	// //지정한 ADC config 테이블에서, 특정 ADC config를 clear한다.
	// String sqlText="";
	// try
	// {
	// sqlText = String.format(" DELETE FROM %s WHERE ADC_INDEX=%d; ",
	// configTableName, adcIndex);
	// db.executeUpdate(sqlText);
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	private void delTablePartial(Integer adcIndex, String tableName, String indexColumnName,
			ArrayList<String> indexList, StringBuilder query) {
		// 지정한 ADC config 테이블에서, 특정 ADC config를 clear한다.
		if (indexList == null || indexList.size() == 0) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
					String.format(
							"Database deletion canceled. Deletion list is null or empty. Table: %s, index column:%s",
							tableName, indexColumnName));
			return;
		}
		String whereIndexList = OBParser.convertList2SingleQuotedString(indexList);
		query.append(" DELETE FROM ").append(tableName).append(" WHERE ").append(indexColumnName).append(" IN (")
				.append(whereIndexList).append(") ; ");
	}

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	private void delTableAll(Integer adcIndex, String configTableName, StringBuilder query) {
		// 지정한 ADC config 테이블에서, 특정 ADC config를 clear한다.
		query.append(" DELETE FROM ").append(configTableName).append(" WHERE ADC_INDEX = ").append(adcIndex)
				.append(" ; ");
	}

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	private void delVSFilter(Integer adcIndex, String configTableName, String vsName, StringBuilder query)
			throws OBException {
		// 지정한 ADC config 테이블에서, 특정 ADC config를 clear한다.
		query.append(" DELETE FROM ").append(configTableName).append(" WHERE ADC_INDEX = ").append(adcIndex)
				.append(" AND INDEX = ").append(OBParser.sqlString(OBCommon.makeVSIndexF5(adcIndex, vsName)))
				.append(" ; ");
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private void delTableAll(Integer adcIndex, String configTableName) throws OBException {
		OBDatabase db = new OBDatabase();

		// 지정한 ADC config 테이블에서, 특정 ADC config를 clear한다.
		String sqlText = "";

		try {
			db.openDB();
			sqlText = " DELETE FROM " + configTableName + " WHERE ADC_INDEX=" + adcIndex + "; ";
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

	// public void delHealthCheckPAS(Integer adcIndex, OBDatabase db) throws
	// OBException
	// {
	// String sqlText="";
	// try
	// {
	// // 지정된 ADC의 health check 목록을 삭제함.
	// sqlText = String.format(" DELETE FROM TMP_SLB_HEALTHCHECK WHERE ADC_INDEX=%d;
	// ", adcIndex);
	// db.executeUpdate(sqlText);
	// }
	// catch(SQLException e)
	// {
	// throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s,
	// sqlText:%s", e.getMessage(), sqlText));
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }
	// public void delHealthCheckPASK(Integer adcIndex, OBDatabase db) throws
	// OBException
	// {
	// delHealthCheckPAS(adcIndex, db);
	// }
	// public void addHealthCheckAlteon(Integer adcIndex,
	// ArrayList<OBDtoAdcHealthCheckAlteon> hcList, OBDatabase db) throws
	// OBException
	// {
	// String sqlText="";
	// try
	// {
	// if(hcList.size()<=0)
	// return;
	//
	// sqlText = String.format(" INSERT INTO TMP_SLB_HEALTHCHECK " +
	// " ( INDEX, ADC_INDEX, POOL_INDEX, ID_NEW, NAME, TYPE, PORT, CHK_INTERVAL,
	// TIMEOUT, SEND_STRING, RCV_STRING, STATE, EXTRA, DESTINATION_IP ) " +
	// " VALUES ");
	// String valueString = "";
	// for(OBDtoAdcHealthCheckAlteon obj: hcList)
	// {
	// if(!valueString.isEmpty())
	// valueString += ", ";
	// valueString += String.format(
	// " (%s, %d, %d, %s, %s, %s, %d, %d, %d, %s, %s, %d, %s, %s) ",
	// OBParser.sqlString(OBCommon.makeHealthDbIndexAlteon(adcIndex, obj.getId())),
	// adcIndex,
	// 0,
	// OBParser.sqlString(obj.getId()),
	// OBParser.sqlString(obj.getName()),
	// OBParser.sqlString(obj.getType().toString()),
	// 0, //port
	// 0, //interval
	// 0, //timeout
	// OBParser.sqlString(""), //SEND_STRING
	// OBParser.sqlString(""), //RCV_STRING
	// OBDefine.STATE_ENABLE, //STTE
	// OBParser.sqlString(obj.getExtra()), //EXTRA
	// OBParser.sqlString(obj.getDestinationIp()) //destination IP or logical
	// expression
	// );
	// }
	// sqlText += valueString + ";";
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
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
	// e.getMessage());
	// }
	// }

	// setAutcommit(false) 작업을 위한 메소드, 하나의 트랜잭션을 구현하기 위해 필요하다.
	public void addHealthCheckAlteon(Integer adcIndex, ArrayList<OBDtoAdcHealthCheckAlteon> hcList, StringBuilder query)
			throws OBException {
		if (hcList.size() == 0) {
			return;
		}

		query.append(" INSERT INTO TMP_SLB_HEALTHCHECK ").append(
				" ( INDEX, ADC_INDEX, POOL_INDEX, ID_NEW, NAME, TYPE, PORT, CHK_INTERVAL, TIMEOUT, SEND_STRING, RCV_STRING, STATE, EXTRA, DESTINATION_IP ) ")
				.append(" VALUES ");

		final String delimiter = ", ";
		String prefix = "";

		for (OBDtoAdcHealthCheckAlteon obj : hcList) {
			query.append(prefix).append("(")
					.append(OBParser.sqlString(OBCommon.makeHealthDbIndexAlteon(adcIndex, obj.getId())))
					.append(delimiter).append(adcIndex).append(delimiter).append(0).append(delimiter)
					.append(OBParser.sqlString(obj.getId())).append(delimiter).append(OBParser.sqlString(obj.getName()))
					.append(delimiter).append(OBParser.sqlString(obj.getType().toString())).append(delimiter).append(0)
					// port
					.append(delimiter).append(0)
					// interval
					.append(delimiter).append(0)
					// timeout
					.append(delimiter).append(OBParser.sqlString(""))
					// SEND_STRING
					.append(delimiter).append(OBParser.sqlString(""))
					// RCV_STRING
					.append(delimiter).append(OBDefine.STATE_ENABLE)
					// STTE
					.append(delimiter).append(OBParser.sqlString(obj.getExtra()))
					// EXTRA
					.append(delimiter).append(OBParser.sqlString(obj.getDestinationIp()))
					// destination IP or logical expression
					.append(")");

			prefix = ", ";
		}

		query.append(" ; ");
	}

	public void addHealthCheckPAS(Integer adcIndex, ArrayList<OBDtoAdcHealthCheckPAS> hcList, StringBuilder query)
			throws OBException {
		if (hcList.size() == 0) {
			return;
		}

		query.append(" INSERT INTO TMP_SLB_HEALTHCHECK ").append(
				" ( INDEX, ADC_INDEX, POOL_INDEX, ID, NAME, TYPE, PORT, CHK_INTERVAL, TIMEOUT, SEND_STRING, RCV_STRING, STATE, EXTRA ) ")
				.append(" VALUES ");

		final String delimiter = ", ";
		String prefix = "";

		for (OBDtoAdcHealthCheckPAS obj : hcList) {
			// String index = adcIndex + "_" + obj.getName();

			query.append(prefix).append("(").append(OBParser.sqlString(obj.getDbIndex())).append(delimiter)
					.append(adcIndex).append(delimiter).append(0).append(delimiter).append(obj.getId())
					.append(delimiter).append(OBParser.sqlString(obj.getName())).append(delimiter)
					.append(OBParser.sqlString(obj.getType().toString())).append(delimiter).append(obj.getPort())
					.append(delimiter).append(obj.getInterval()).append(delimiter).append(obj.getTimeout())
					.append(delimiter).append(OBParser.sqlString("")).append(delimiter).append(OBParser.sqlString(""))
					.append(delimiter).append(OBDefine.STATE_ENABLE).append(delimiter).append(OBParser.sqlString(""))
					.append(")");

			prefix = ", ";
		}

		query.append(" ; ");
	}

	public void addHealthCheckPASK(Integer adcIndex, ArrayList<OBDtoAdcHealthCheckPASK> hcList, StringBuilder query)
			throws OBException {
		if (hcList.size() == 0) {
			return;
		}

		query.append(" INSERT INTO TMP_SLB_HEALTHCHECK ").append(
				" ( INDEX, ADC_INDEX, POOL_INDEX, ID, NAME, TYPE, PORT, CHK_INTERVAL, TIMEOUT, SEND_STRING, RCV_STRING, STATE, EXTRA ) ")
				.append(" VALUES ");

		final String delimiter = ", ";
		String prefix = "";

		for (OBDtoAdcHealthCheckPASK obj : hcList) {
			query.append(prefix).append("(").append(OBParser.sqlString(obj.getDbIndex())).append(delimiter)
					.append(adcIndex).append(delimiter).append(0).append(delimiter).append(obj.getId())
					.append(delimiter).append(OBParser.sqlString(obj.getName())).append(delimiter)
					.append(OBParser.sqlString(obj.getType().toString())).append(delimiter).append(obj.getPort())
					.append(delimiter).append(obj.getInterval()).append(delimiter).append(obj.getTimeout())
					.append(delimiter).append(OBParser.sqlString("")).append(delimiter).append(OBParser.sqlString(""))
					.append(delimiter).append(OBDefine.STATE_ENABLE).append(delimiter).append(OBParser.sqlString(""))
					.append(")");

			prefix = ", ";
		}

		query.append(";");
	}

	// 비어 있는 health check index. 범위는 1~16
	public ArrayList<Integer> getAvailableHealthCheckIndexPAS(String vsIndex) throws OBException {
		ArrayList<Integer> idList = new ArrayList<Integer>();
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT id FROM GENERATE_SERIES(1,16,1) AS NUM(id)                \n"
							+ " WHERE id NOT IN (                                                \n"
							+ "     SELECT ID FROM TMP_SLB_HEALTHCHECK                           \n"
							+ "     WHERE INDEX = ( SELECT HEALTH_CHECK_INDEX FROM TMP_SLB_POOL  \n"
							+ "                     WHERE INDEX = ( SELECT POOL_INDEX            \n"
							+ "                                     FROM TMP_SLB_VSERVER         \n"
							+ "                                     WHERE INDEX = %s             \n"
							+ "                     )                                            \n"
							+ "     )                                                            \n"
							+ " )                                                                \n",
					OBParser.sqlString(vsIndex));

			ResultSet rs;
			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				idList.add(db.getInteger(rs, "id"));
			}
			return idList;
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

	// PAS real server index중 할당 가능한 index를 최소값부터 N개 준다.
	// PAS real server index는 각 VS마다 1~1024 사이에서 이미 쓰이지 않은 것들을 쓸 수 있다.
	public ArrayList<Integer> getAvailableRealServerIndexPAS(String vsIndex, int getCount) throws OBException {
		String sqlText = "";
		ArrayList<Integer> idList = new ArrayList<Integer>();
		int maxPASRealId = 1024; // PAS default
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT id FROM GENERATE_SERIES(1, %d, 1) AS NUM(id)               \n"
							+ " WHERE id NOT IN (                                                 \n"
							+ "    SELECT ALTEON_ID FROM TMP_SLB_NODE                             \n"
							+ "    WHERE INDEX IN ( SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER     \n"
							+ "                     WHERE POOL_INDEX = ( SELECT POOL_INDEX        \n"
							+ "                                          FROM TMP_SLB_VSERVER     \n"
							+ "                                          WHERE INDEX = %s         \n"
							+ "                     )                                             \n"
							+ "   )                                                               \n"
							+ " ) LIMIT %d                                                        \n",
					maxPASRealId, OBParser.sqlString(vsIndex), getCount);

			ResultSet rs;
			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				idList.add(db.getInteger(rs, "id"));
			}
			return idList;
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

	// PASK real server index중 할당 가능한 index를 최소값부터 N개 준다.
	// PASK real server index는 ADC에서 1~2048 중 이미 쓰이지 않은 것들을 쓸 수 있다.
	public ArrayList<Integer> getAvailableRealServerIndexPASK(Integer adcIndex, int getCount) throws OBException {
		String sqlText = "";
		ArrayList<Integer> idList = new ArrayList<Integer>();

		int maxPASRealId = 2048; // PASK default
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT id FROM GENERATE_SERIES(1, %d, 1) AS NUM(id)               \n"
							+ " WHERE id NOT IN (                                                 \n"
							+ "    SELECT ALTEON_ID FROM TMP_SLB_NODE                             \n"
							+ "    WHERE ADC_INDEX = %d                                           \n"
							+ " ) LIMIT %d                                                        \n",
					maxPASRealId, adcIndex, getCount);

			ResultSet rs;
			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				idList.add(db.getInteger(rs, "id"));
			}
			return idList;
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

	public boolean isExistVSIPAddress(Integer adcIndex, String vsIPAddress) throws OBException {
		String sqlText = "";

		if (vsIPAddress == null) {
			return false;
		}

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT NAME " + " FROM TMP_SLB_VSERVER " + " WHERE ADC_INDEX=%d AND VIRTUAL_IP=%s;", adcIndex,
					OBParser.sqlString(vsIPAddress));

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			if (rs.next() == false) {
				return false;
			}
			return true;
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

	public ArrayList<OBDtoAdcVServerAlteon> searchVSListUsedByPoolAlteon(Integer adcIndex, Integer poolID)
			throws OBException {
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d,
		// searchKey:%s", adcIndex, searchKey));
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		String poolIndex = OBCommon.makePoolIndex(adcIndex, poolID);
		ArrayList<OBDtoAdcVServerAlteon> list = new ArrayList<OBDtoAdcVServerAlteon>();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT INDEX, ADC_INDEX, NAME, VIRTUAL_IP           \n"
							+ " FROM TMP_SLB_VSERVER                                \n"
							+ " WHERE INDEX IN (                                    \n"
							+ "     SELECT VS_INDEX FROM TMP_SLB_VS_SERVICE         \n"
							+ "     WHERE POOL_INDEX = %s                           \n"
							+ " )                                                   \n",
					adcIndex, OBParser.sqlString(poolIndex));

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcVServerAlteon obj = new OBDtoAdcVServerAlteon();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setvIP(db.getString(rs, "VIRTUAL_IP"));

				list.add(obj);
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
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
		// list.toString()));

		return list;
	}

	public ArrayList<OBDtoAdcVServerF5> searchVSListUsedByPoolF5(Integer adcIndex, String poolName) throws OBException {
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d,
		// searchKey:%s", adcIndex, searchKey));
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		String poolIndex = OBCommon.makePoolIndex(adcIndex, poolName);
		ArrayList<OBDtoAdcVServerF5> list = new ArrayList<OBDtoAdcVServerF5>();
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX, ADC_INDEX, NAME, VIRTUAL_IP, POOL_INDEX " + " FROM TMP_SLB_VSERVER "
					+ " WHERE ADC_INDEX=%d AND POOL_INDEX=%s ", adcIndex, OBParser.sqlString(poolIndex));

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcVServerF5 obj = new OBDtoAdcVServerF5();
				obj.setIndex(db.getString(rs, "INDEX"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setvIP(db.getString(rs, "VIRTUAL_IP"));

				list.add(obj);
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

		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
		// list.toString()));

		return list;
	}

	public ArrayList<OBDtoAdcVServerPAS> searchVSListUsedByPoolPAS(Integer adcIndex, String poolName)
			throws OBException {
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d,
		// searchKey:%s", adcIndex, searchKey));
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		String poolIndex = OBCommon.makePoolIndex(adcIndex, poolName);
		ArrayList<OBDtoAdcVServerPAS> list = new ArrayList<OBDtoAdcVServerPAS>();
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX, ADC_INDEX, NAME, VIRTUAL_IP, POOL_INDEX " + " FROM TMP_SLB_VSERVER "
					+ " WHERE ADC_INDEX=%d AND POOL_INDEX=%s ", adcIndex, OBParser.sqlString(poolIndex));

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcVServerPAS obj = new OBDtoAdcVServerPAS();
				obj.setDbIndex(db.getString(rs, "INDEX"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setvIP(db.getString(rs, "VIRTUAL_IP"));

				list.add(obj);
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
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s",
		// list.toString()));

		return list;
	}

	public ArrayList<OBDtoAdcVSInfo> getVSNameList(Integer accntIndex, String searchKey) throws OBException {
		HashMap<String, OBDtoAdcVSInfo> vsMap = new HashMap<String, OBDtoAdcVSInfo>();
		String sqlText = "";

		if (searchKey == null) {
			searchKey = "";
		}

		final OBDatabase db = new OBDatabase();
		try {
			String searchSql = "";
			if (searchKey.isEmpty() == false) {
				// #3984-2 #11: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				searchSql = String.format(" AND ( B.NAME LIKE %s OR B.VIRTUAL_IP LIKE %s ) \n",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accntIndex);

			sqlText = String.format(
					" SELECT A.INDEX AS ADC_INDEX, A.TYPE AS ADC_TYPE, A.NAME AS ADC_NAME, B.INDEX AS VS_INDEX, B.NAME AS VS_NAME, B.VIRTUAL_IP, B.VIRTUAL_PORT \n"
							+ " FROM MNG_ADC A                                                               \n"
							+ " INNER JOIN TMP_SLB_VSERVER B                                                 \n"
							+ " ON A.INDEX = B.ADC_INDEX                                                     \n"
							+ " WHERE A.AVAILABLE = %d                                                       \n",
					OBDefine.ADC_STATE.AVAILABLE);

			if (roleNo.equals(OBDefine.ACCNT_ROLE_VSADMIN) || roleNo.equals(OBDefine.ACCNT_ROLE_RSADMIN)) {
				ArrayList<String> vsList = new OBAccountImpl().getAssignedVSList(accntIndex);
				String sqlTextVS = convertSqlVSList(vsList);
				if (sqlTextVS == null || sqlTextVS.isEmpty()) {
					return new ArrayList<OBDtoAdcVSInfo>(vsMap.values());
				}
				sqlText += String.format(" AND " + " B.INDEX IN ( %s ) ", sqlTextVS);
			} else if (roleNo.equals(OBDefine.ACCNT_ROLE_CONFIG) || roleNo.equals(OBDefine.ACCNT_ROLE_READONLY)) {
				sqlText += String.format(
						" AND " + " A.INDEX IN (SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %s) ",
						accntIndex);
			}
			sqlText += searchSql;

			sqlText += " ORDER BY A.NAME ";

			db.openDB();
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				Integer adcIndex = db.getInteger(rs, "ADC_INDEX");// OBDtoAdcVSInfo obj = new OBDtoAdcVSInfo();
				String adcName = db.getString(rs, "ADC_NAME");

				String vsIndex = db.getString(rs, "VS_INDEX");
				String vsName = db.getString(rs, "VS_NAME");
				String ipAddress = db.getString(rs, "VIRTUAL_IP");
				Integer port = db.getInteger(rs, "VIRTUAL_PORT");
				String dispName = "";
				Integer adcType = db.getInteger(rs, "ADC_TYPE");
				if (vsName != null && !vsName.isEmpty()) {
					dispName = vsName;
				} else {
					if (adcType == OBDefine.ADC_TYPE_ALTEON) {
						dispName = ipAddress;
					} else {
						dispName = ipAddress + ":" + port;
					}
				}
				String keyName = adcIndex.toString();
				OBDtoAdcVSInfo objVSInfo = vsMap.get(keyName);
				if (objVSInfo == null) {
					objVSInfo = new OBDtoAdcVSInfo();
					objVSInfo.setAdcIndex(adcIndex);
					objVSInfo.setAdcName(adcName);
					objVSInfo.setVsInfoList(new ArrayList<OBDtoVSInfo>());
					OBDtoVSInfo vsInfo = new OBDtoVSInfo();
					vsInfo.setVsIndex(vsIndex);
					vsInfo.setVsIPAddress(ipAddress);
					vsInfo.setVsName(dispName);
					vsInfo.setVsPort(port);
					objVSInfo.getVsInfoList().add(vsInfo);
					vsMap.put(keyName, objVSInfo);
				} else {
					OBDtoVSInfo vsInfo = new OBDtoVSInfo();
					vsInfo.setVsIndex(vsIndex);
					vsInfo.setVsIPAddress(ipAddress);
					vsInfo.setVsName(dispName);
					vsInfo.setVsPort(port);
					objVSInfo.getVsInfoList().add(vsInfo);
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

		return new ArrayList<OBDtoAdcVSInfo>(vsMap.values());
	}

	public ArrayList<OBDtoAdcRSInfo> getRSNameList(Integer accntIndex, String searchKey) throws OBException {
		HashMap<String, OBDtoAdcRSInfo> rsMap = new HashMap<String, OBDtoAdcRSInfo>();
		String sqlText = "";

		if (searchKey == null) {
			searchKey = "";
		}

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			String searchSql = "";
			if (searchKey.isEmpty() == false) {
				// #3984-2 #11: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				searchSql = String.format(" AND ( B.NAME LIKE %s OR B.VIRTUAL_IP LIKE %s ) \n",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			Integer roleNo = new OBAccountImpl().getAccountRoleNoNew(accntIndex);

			sqlText = String.format(
					"  SELECT A.INDEX AS ADC_INDEX, A.NAME AS ADC_NAME,                                                 \n"
							+ " A.TYPE AS ADC_TYPE, A.NAME AS ADC_NAME, B.INDEX AS RS_INDEX, B.NAME AS RS_NAME, B.IP_ADDRESS      \n"
							+ " FROM MNG_ADC A                                                                                    \n"
							+ " LEFT JOIN (SELECT * FROM TMP_SLB_NODE) B                                                          \n"
							+ " ON A.INDEX = B.ADC_INDEX                                                                          \n"
							+ " WHERE A.AVAILABLE = %d                                                                            \n",
					OBDefine.ADC_STATE.AVAILABLE);

			if (roleNo.equals(OBDefine.ACCNT_ROLE_VSADMIN) || roleNo.equals(OBDefine.ACCNT_ROLE_RSADMIN)) {
				ArrayList<String> rsList = new OBAccountImpl().getAssignedRSList(accntIndex);
				String sqlTextRS = convertSqlVSList(rsList);
				if (sqlTextRS == null || sqlTextRS.isEmpty()) {
					return new ArrayList<OBDtoAdcRSInfo>(rsMap.values());
				}
				sqlText += String.format(" AND " + " B.INDEX IN ( %s ) ", sqlTextRS);
			} else if (roleNo.equals(OBDefine.ACCNT_ROLE_CONFIG) || roleNo.equals(OBDefine.ACCNT_ROLE_READONLY)) {
				sqlText += String.format(
						" AND " + " A.INDEX IN (SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %s) ",
						accntIndex);
			}
			sqlText += searchSql;

			sqlText += " ORDER BY A.NAME ";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				Integer adcIndex = db.getInteger(rs, "ADC_INDEX");// OBDtoAdcVSInfo obj = new OBDtoAdcVSInfo();
				String adcName = db.getString(rs, "ADC_NAME");

				String rsIndex = db.getString(rs, "RS_INDEX");
				String rsName = db.getString(rs, "RS_NAME");
				String ipAddress = db.getString(rs, "IP_ADDRESS");
				String dispName = "";
				if (rsName != null && !rsName.isEmpty()) {
					dispName = rsName;
				}
				String keyName = adcIndex.toString();
				OBDtoAdcRSInfo objRSInfo = rsMap.get(keyName);
				if (objRSInfo == null) {
					objRSInfo = new OBDtoAdcRSInfo();
					objRSInfo.setAdcIndex(adcIndex);
					objRSInfo.setAdcName(adcName);
					objRSInfo.setRsInfoList(new ArrayList<OBDtoRSInfo>());
					OBDtoRSInfo rsInfo = new OBDtoRSInfo();
					rsInfo.setRsIndex(rsIndex);
					rsInfo.setRsIPAddress(ipAddress);
					rsInfo.setRsName(dispName);
					objRSInfo.getRsInfoList().add(rsInfo);
					rsMap.put(keyName, objRSInfo);
				} else {
					OBDtoRSInfo rsInfo = new OBDtoRSInfo();
					rsInfo.setRsIndex(rsIndex);
					rsInfo.setRsIPAddress(ipAddress);
					rsInfo.setRsName(dispName);
					objRSInfo.getRsInfoList().add(rsInfo);
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

		return new ArrayList<OBDtoAdcRSInfo>(rsMap.values());
	}

	public ArrayList<OBDtoAdcVSInfo> getVSNameNSList(Integer accntIndex, String searchKey) throws OBException {
		HashMap<String, OBDtoAdcVSInfo> vsMap = new HashMap<String, OBDtoAdcVSInfo>();
		String sqlText = "";

		if (searchKey == null) {
			searchKey = "";
		}

		final OBDatabase db = new OBDatabase();
		try {
			String searchSql = "";
			if (searchKey.isEmpty() == false) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				searchSql = String.format(" AND ( B.NAME LIKE %s OR B.VIRTUAL_IP LIKE %s ) \n",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accntIndex);
			if (roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) {
				return new ArrayList<OBDtoAdcVSInfo>(vsMap.values());
			}

			sqlText = String.format(
					" SELECT A.INDEX AS ADC_INDEX, A.TYPE AS ADC_TYPE, A.NAME AS ADC_NAME, B.INDEX AS VS_INDEX, B.NAME AS VS_NAME, B.VIRTUAL_IP, B.VIRTUAL_PORT \n"
							+ " FROM MNG_ADC A                                                               \n"
							+ " INNER JOIN TMP_SLB_VSERVER B                                                 \n"
							+ " ON A.INDEX = B.ADC_INDEX                                                     \n"
							+ " WHERE A.AVAILABLE = %d                                                       \n",
					OBDefine.ADC_STATE.AVAILABLE);

			if (roleNo.equals(OBDefine.ACCNT_ROLE_VSADMIN) || roleNo.equals(OBDefine.ACCNT_ROLE_RSADMIN)) {
				ArrayList<String> vsList = new OBAccountImpl().getAssignedVSList(accntIndex);
				String sqlTextVS = convertSqlVSList(vsList);
				if (sqlTextVS == null || sqlTextVS.isEmpty()) {
					return new ArrayList<OBDtoAdcVSInfo>(vsMap.values());
				}
				sqlText += String.format(" AND B.INDEX NOT IN ( %s ) ", sqlTextVS);
			} else if (roleNo.equals(OBDefine.ACCNT_ROLE_CONFIG) || roleNo.equals(OBDefine.ACCNT_ROLE_READONLY)) {
				sqlText += String.format(
						" AND A.INDEX NOT IN (SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %s) ",
						accntIndex);
			}
			sqlText += searchSql;

			sqlText += " ORDER BY A.NAME ";

			db.openDB();
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				Integer adcIndex = db.getInteger(rs, "ADC_INDEX");// OBDtoAdcVSInfo obj = new OBDtoAdcVSInfo();
				String adcName = db.getString(rs, "ADC_NAME");

				String vsIndex = db.getString(rs, "VS_INDEX");
				String vsName = db.getString(rs, "VS_NAME");
				String ipAddress = db.getString(rs, "VIRTUAL_IP");
				Integer port = db.getInteger(rs, "VIRTUAL_PORT");
				String dispName = "";
				Integer adcType = db.getInteger(rs, "ADC_TYPE");
				if (vsName != null && !vsName.isEmpty()) {
					dispName = vsName;
				} else {
					if (adcType == OBDefine.ADC_TYPE_ALTEON) {
						dispName = ipAddress;
					} else {
						dispName = ipAddress + ":" + port;
					}
				}
				String keyName = adcIndex.toString();
				OBDtoAdcVSInfo objVSInfo = vsMap.get(keyName);
				if (objVSInfo == null) {
					objVSInfo = new OBDtoAdcVSInfo();
					objVSInfo.setAdcIndex(adcIndex);
					objVSInfo.setAdcName(adcName);
					objVSInfo.setVsInfoList(new ArrayList<OBDtoVSInfo>());
					OBDtoVSInfo vsInfo = new OBDtoVSInfo();
					vsInfo.setVsIndex(vsIndex);
					vsInfo.setVsIPAddress(ipAddress);
					vsInfo.setVsName(dispName);
					vsInfo.setVsPort(port);
					objVSInfo.getVsInfoList().add(vsInfo);
					vsMap.put(keyName, objVSInfo);
				} else {
					OBDtoVSInfo vsInfo = new OBDtoVSInfo();
					vsInfo.setVsIndex(vsIndex);
					vsInfo.setVsIPAddress(ipAddress);
					vsInfo.setVsName(dispName);
					vsInfo.setVsPort(port);
					objVSInfo.getVsInfoList().add(vsInfo);
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

		return new ArrayList<OBDtoAdcVSInfo>(vsMap.values());
	}

	public ArrayList<OBDtoAdcRSInfo> getRSNameNSList(Integer accntIndex, String searchKey) throws OBException {
		HashMap<String, OBDtoAdcRSInfo> rsMap = new HashMap<String, OBDtoAdcRSInfo>();
		String sqlText = "";

		if (searchKey == null) {
			searchKey = "";
		}

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			String searchSql = "";
			if (searchKey.isEmpty() == false) {
				// #3984-2 #11: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				searchSql = String.format(" AND ( B.NAME LIKE %s OR B.VIRTUAL_IP LIKE %s ) \n",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			Integer roleNo = new OBAccountImpl().getAccountRoleNoNew(accntIndex);

			sqlText = String.format(
					"  SELECT A.INDEX AS ADC_INDEX, A.NAME AS ADC_NAME,                                 \n"
							+ " A.TYPE AS ADC_TYPE, A.NAME AS ADC_NAME, B.INDEX AS RS_INDEX, B.NAME AS RS_NAME, B.IP_ADDRESS      \n"
							+ " FROM MNG_ADC A                                                                                    \n"
							+ " LEFT JOIN (SELECT * FROM TMP_SLB_NODE) B                                                          \n"
							+ " ON A.INDEX = B.ADC_INDEX                                                                          \n"
							+ " WHERE A.AVAILABLE = %d                                                                            \n",
					OBDefine.ADC_STATE.AVAILABLE);

			if (roleNo.equals(OBDefine.ACCNT_ROLE_VSADMIN) || roleNo.equals(OBDefine.ACCNT_ROLE_RSADMIN)) {
				ArrayList<String> rsList = new OBAccountImpl().getAssignedRSList(accntIndex);
				String sqlTextRS = convertSqlVSList(rsList);
				if (sqlTextRS != null && !sqlTextRS.isEmpty()) {
					sqlText += String.format(" AND " + " B.INDEX NOT IN ( %s ) ", sqlTextRS);
				}

			} else if (roleNo.equals(OBDefine.ACCNT_ROLE_CONFIG) || roleNo.equals(OBDefine.ACCNT_ROLE_READONLY)) {
				sqlText += String.format(
						" AND " + " A.INDEX NOT IN (SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP WHERE ACCNT_INDEX = %s) ",
						accntIndex);
			}
			sqlText += searchSql;

			sqlText += " ORDER BY A.NAME ";

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				Integer adcIndex = db.getInteger(rs, "ADC_INDEX");// OBDtoAdcVSInfo obj = new OBDtoAdcVSInfo();
				String adcName = db.getString(rs, "ADC_NAME");

				String rsIndex = db.getString(rs, "RS_INDEX");
				String rsName = db.getString(rs, "RS_NAME");
				String ipAddress = db.getString(rs, "IP_ADDRESS");
				String dispName = "";
				if (rsName != null && !rsName.isEmpty()) {
					dispName = rsName;
				}
				String keyName = adcIndex.toString();
				OBDtoAdcRSInfo objRSInfo = rsMap.get(keyName);
				if (objRSInfo == null) {
					objRSInfo = new OBDtoAdcRSInfo();
					objRSInfo.setAdcIndex(adcIndex);
					objRSInfo.setAdcName(adcName);
					objRSInfo.setRsInfoList(new ArrayList<OBDtoRSInfo>());
					OBDtoRSInfo rsInfo = new OBDtoRSInfo();
					rsInfo.setRsIndex(rsIndex);
					rsInfo.setRsIPAddress(ipAddress);
					rsInfo.setRsName(dispName);
					objRSInfo.getRsInfoList().add(rsInfo);
					rsMap.put(keyName, objRSInfo);
				} else {
					OBDtoRSInfo rsInfo = new OBDtoRSInfo();
					rsInfo.setRsIndex(rsIndex);
					rsInfo.setRsIPAddress(ipAddress);
					rsInfo.setRsName(dispName);
					objRSInfo.getRsInfoList().add(rsInfo);
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

		return new ArrayList<OBDtoAdcRSInfo>(rsMap.values());
	}

	public void addRespSectionCheck(OBDtoRespGroup respInfo) throws OBException {
		final String delimiter = ", ";
		String prefix = "";
		String sqlText = "";
		Integer index = 0;
		StringBuilder query = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(
					" INSERT INTO MNG_RESP_SECTION_GROUP      \n"
							+ " (NAME, LAST_TIME) VALUES (%s, %s);                         \n",
					OBParser.sqlString(respInfo.getName()), OBParser.sqlString(OBDateTime.now()));

			db.executeUpdate(sqlText);

			sqlText = String.format(
					" SELECT INDEX FROM MNG_RESP_SECTION_GROUP    \n"
							+ " WHERE NAME = %s;                                \n",
					OBParser.sqlString(respInfo.getName()));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				index = db.getInteger(rs, "INDEX");
			}

			if (index == 0)
				return;

			query.append(" INSERT INTO MNG_RESP_SECTION_CHECK                               \n")
					.append(" (IPADDRESS, TYPE, PORT, PATH, COMMENT, GROUP_INDEX, RESP_ORDER)  \n")
					.append(" VALUES                                                           \n");
			int i = 0;
			for (OBDtoRespInfo resp : respInfo.getRespInfo()) {

				if (resp == null)
					continue;
				query.append(prefix).append("(").append(OBParser.sqlString(resp.getIpaddress())) // 응답시간
						.append(delimiter).append(resp.getType()) // HTTP TYPE
						.append(delimiter).append(resp.getPort()) // Port Number
						.append(delimiter).append(OBParser.sqlString(resp.getPath())) // Path
						.append(delimiter).append(OBParser.sqlString(resp.getComment())) // Comment
						.append(delimiter).append(index) // RESP 그룹인덱스
						.append(delimiter).append(i) // RESP 순서
						.append(")");
				prefix = ", ";
				i++;
			}

			query.append(";");
			db.executeUpdate(query.toString());
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), query));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void addRespSectionCheckEach(OBDtoRespInfo newResp, Integer groupIndex) throws OBException {
		String sqlText = "";

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(
					" INSERT INTO MNG_RESP_SECTION_CHECK                                              \n"
							+ " (IPADDRESS, TYPE, PORT, PATH, COMMENT, GROUP_INDEX)                                 \n"
							+ " VALUES (%s, %d, %d, %s, %s, %d);                                                        \n",
					OBParser.sqlString(newResp.getIpaddress()), newResp.getType(), newResp.getPort(),
					OBParser.sqlString(newResp.getPath()), OBParser.sqlString(newResp.getComment()), groupIndex);

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

	public void setRespSectionCheck(Integer respGroupIndex, String respName, OBDtoRespInfo respUpdate)
			throws OBException {
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(
					" UPDATE MNG_RESP_SECTION_GROUP       \n" + " SET NAME=%s                             \n"
							+ " WHERE INDEX=%d ;                        \n",
					OBParser.sqlString(respName), respGroupIndex);

			db.executeUpdate(sqlText);

			sqlText = String.format(
					" UPDATE MNG_RESP_SECTION_CHECK                   \n"
							+ " SET TYPE=%d, PATH=%s, COMMENT=%s, RESP_ORDER=%d     \n"
							+ " WHERE INDEX=%d;                                     \n",
					respUpdate.getType(), OBParser.sqlString(respUpdate.getComment()),
					OBParser.sqlString(respUpdate.getPath()), respUpdate.getRespOrder(), respUpdate.getIndex());

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

	public void delRespSectionCheck(ArrayList<String> respIndexList) throws OBException {
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			for (int i = 0; i < respIndexList.size(); i++) {
				String respInteger = respIndexList.get(i);

				sqlText = String.format(" SELECT NAME                                                    \n"
						+ " FROM MNG_RESP_SECTION_GROUP                                        \n"
						+ " WHERE INDEX = %s                                                   ", respInteger);

				ResultSet rs;

				rs = db.executeQuery(sqlText);

				if (rs.next() == true) {
					ArrayList<Integer> respList = new ArrayList<Integer>();
					sqlText = String.format(" SELECT INDEX FROM MNG_RESP_SECTION_CHECK WHERE GROUP_INDEX = %s; ",
							respInteger);

					rs = db.executeQuery(sqlText);

					while (rs.next()) {
						respList.add(db.getInteger(rs, "INDEX"));
					}

					for (Integer resp : respList) {
						sqlText = String.format(" DELETE FROM LOG_RESP_SECTION_CHECK WHERE RESP_INDEX = %d; ", resp);

						db.executeUpdate(sqlText);

						sqlText = String.format(" DELETE FROM TMP_RESP_SECTION_CHECK WHERE RESP_INDEX = %d; ", resp);

						db.executeUpdate(sqlText);
					}

					sqlText = String.format(" DELETE FROM MNG_RESP_SECTION_GROUP WHERE INDEX = %s; ", respInteger);

					db.executeUpdate(sqlText);

					sqlText = String.format(" DELETE FROM MNG_RESP_SECTION_CHECK WHERE GROUP_INDEX = %s; ",
							respInteger);

					db.executeUpdate(sqlText);

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
	}

	public void delRespSectionCheckEach(Integer respIndex) throws OBException {
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" SELECT NAME                                                    \n"
					+ " FROM MNG_RESP_SECTION_CHECK                                        \n"
					+ " WHERE INDEX = %d                                                    ", respIndex);

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			if (rs.next() == true) {

				if (respIndex == OBDefine.DATA_UNAVAILABLE) {
					return;
				}

				sqlText = String.format(" DELETE FROM LOG_RESP_SECTION_CHECK WHERE RESP_INDEX = %d; ", respIndex);

				db.executeUpdate(sqlText);

				sqlText = String.format(" DELETE FROM TMP_RESP_SECTION_CHECK WHERE RESP_INDEX = %d; ", respIndex);

				db.executeUpdate(sqlText);

				sqlText = String.format(" DELETE FROM MNG_RESP_SECTION_CHECK WHERE INDEX = %d; ", respIndex);

				db.executeUpdate(sqlText);
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
	}

	public OBDtoRespGroup getRespSectionCheck(Integer respIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("response Group Index:%d", respIndex));
		OBDtoRespGroup retVal = new OBDtoRespGroup();
		ArrayList<OBDtoRespInfo> respList = new ArrayList<OBDtoRespInfo>();
		retVal.setRespInfo(respList);
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT A.INDEX AS GROUP_INDEX, A.NAME AS GROUP_NAME, A.LAST_TIME AS LAST_TIME, B.INDEX AS B_INDEX,  \n"
							+ " B.IPADDRESS AS IPADDRESS, B.TYPE AS TYPE, B.PORT AS PORT, B.PATH AS PATH, B.COMMENT AS COMMENT      \n"
							+ " FROM (SELECT * FROM MNG_RESP_SECTION_GROUP WHERE INDEX = %d) A                                      \n"
							+ " LEFT JOIN (SELECT * FROM MNG_RESP_SECTION_CHECK ORDER BY RESP_ORDER ASC ) B                         \n"
							+ " ON A.INDEX = B.GROUP_INDEX                                                                          \n",

					respIndex);

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				if (db.getInteger(rs, "B_INDEX") == 0) {
					continue;
				}
				retVal.setIndex(db.getInteger(rs, "GROUP_INDEX"));
				retVal.setName(db.getString(rs, "GROUP_NAME"));
				retVal.setLastTime(db.getTimestamp(rs, "LAST_TIME"));
				OBDtoRespInfo resp = new OBDtoRespInfo();
				resp.setIndex(db.getInteger(rs, "B_INDEX"));
				resp.setIpaddress(db.getString(rs, "IPADDRESS"));
				resp.setType(db.getInteger(rs, "TYPE"));
				resp.setPort(db.getInteger(rs, "PORT"));
				resp.setPath(db.getString(rs, "PATH"));
				resp.setComment(db.getString(rs, "COMMENT"));
				resp.setGroupIndex(db.getInteger(rs, "GROUP_INDEX"));
				respList.add(resp);
			}

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

	public ArrayList<OBDtoRespMultiChartData> getLogRespSectionCheck(Integer respIndex, OBDtoSearch searchOption)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("response Group Index:%d", respIndex));
		if (respIndex == null) {
			return null;
		}
		OBDtoRespGroup respGroup = getRespSectionCheck(respIndex);
		ArrayList<Integer> respList = new ArrayList<Integer>();
		ArrayList<OBDtoRespMultiChartData> retVal = new ArrayList<OBDtoRespMultiChartData>();
		int size = respGroup.getRespInfo().size();
		for (OBDtoRespInfo resp : respGroup.getRespInfo()) {
			respList.add(resp.getIndex());
		}

		String sqlSearch = makeTimeSqlText(searchOption, "OCCUR_TIME");
		String tableName = "LOG_RESP_SECTION_CHECK";
		String tableColumn = ", INDEX, RESP_TIME";

		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (respList.size() == 1) {
				sqlText = String.format(
						" SELECT INDEX, OCCUR_TIME, RESP_TIME AS A_RESP_TIME FROM LOG_RESP_SECTION_CHECK WHERE RESP_INDEX = %d    \n",
						respGroup.getRespInfo().get(0).getIndex());
				sqlText += sqlSearch;
				sqlText += "ORDER BY OCCUR_TIME ASC";
			} else {
				respList.remove(0);
				String joinSql = OBCommon.makeTableSql(respList, tableName, "RESP_INDEX", tableColumn, sqlSearch);
				String sqlColumn = OBCommon.makeColumnSql(respList, "RESP_TIME");

				sqlText = String.format(
						" SELECT A.OCCUR_TIME, A.RESP_TIME AS A_RESP_TIME %s                                                  \n"
								+ "  FROM (SELECT INDEX, OCCUR_TIME, RESP_TIME FROM LOG_RESP_SECTION_CHECK WHERE RESP_INDEX = %d %s) A  \n"
								+ "  %s                                                                                                 \n"
								+ "  ORDER BY OCCUR_TIME ASC                                                                            \n",
						sqlColumn, respGroup.getRespInfo().get(0).getIndex(), sqlSearch, joinSql);
			}

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoRespMultiChartData obj = new OBDtoRespMultiChartData();
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				if (size >= 1) {
					obj.setValue1(db.getInteger(rs, "A_RESP_TIME"));
					if (respGroup.getRespInfo().get(0).getIpaddress() != null)
						obj.setName1(respGroup.getRespInfo().get(0).getIpaddress() + ":"
								+ respGroup.getRespInfo().get(0).getPort());
				}
				if (size >= 2) {
					obj.setValue2(db.getInteger(rs, "B_RESP_TIME"));
					if (respGroup.getRespInfo().get(1).getIpaddress() != null)
						obj.setName2(respGroup.getRespInfo().get(1).getIpaddress() + ":"
								+ respGroup.getRespInfo().get(1).getPort());
				}
				if (size >= 3) {
					obj.setValue3(db.getInteger(rs, "C_RESP_TIME"));
					if (respGroup.getRespInfo().get(2).getIpaddress() != null)
						obj.setName3(respGroup.getRespInfo().get(2).getIpaddress() + ":"
								+ respGroup.getRespInfo().get(2).getPort());
				}
				if (size >= 4) {
					obj.setValue4(db.getInteger(rs, "D_RESP_TIME"));
					if (respGroup.getRespInfo().get(3).getIpaddress() != null)
						obj.setName4(respGroup.getRespInfo().get(3).getIpaddress() + ":"
								+ respGroup.getRespInfo().get(3).getPort());
				}
				if (size >= 5) {
					obj.setValue5(db.getInteger(rs, "E_RESP_TIME"));
					if (respGroup.getRespInfo().get(4).getIpaddress() != null)
						obj.setName5(respGroup.getRespInfo().get(4).getIpaddress() + ":"
								+ respGroup.getRespInfo().get(4).getPort());
				}
				if (size >= 6) {
					obj.setValue6(db.getInteger(rs, "F_RESP_TIME"));
					if (respGroup.getRespInfo().get(5).getIpaddress() != null)
						obj.setName6(respGroup.getRespInfo().get(5).getIpaddress() + ":"
								+ respGroup.getRespInfo().get(5).getPort());
				}

				retVal.add(obj);
			}

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

	public ArrayList<Integer> getRespSectionCheckList(Integer respIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("response Group Index:%d", respIndex));
		ArrayList<Integer> retVal = new ArrayList<Integer>();

		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT INDEX FROM MNG_RESP_SECTION_CHECK                                         \n"
							+ " WHERE GROUP_INDEX = %d                                                           \n",
					respIndex);

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				retVal.add(db.getInteger(rs, "INDEX"));
			}

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

	// TODO
	public ArrayList<OBDtoRespGroup> getResponseTimeList(OBDtoSearch searchOption, Integer orderType, Integer orderDir)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("get response time"));
		ArrayList<OBDtoRespGroup> retVal = new ArrayList<OBDtoRespGroup>();

		String sqlText = "";
		String sqlTime = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" SELECT INDEX, NAME, LAST_TIME FROM MNG_RESP_SECTION_GROUP ");

			sqlText += searchRespListOrderType(orderType, orderDir);

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoRespGroup respInfo = new OBDtoRespGroup();
				respInfo.setIndex(db.getInteger(rs, "INDEX"));
				respInfo.setName(db.getString(rs, "NAME"));
				respInfo.setLastTime(db.getTimestamp(rs, "LAST_TIME"));
				retVal.add(respInfo);
			}

			for (OBDtoRespGroup respGroup : retVal) {
				ArrayList<OBDtoRespInfo> resp = new ArrayList<OBDtoRespInfo>();

				sqlText = String.format(
						" SELECT * FROM MNG_RESP_SECTION_CHECK WHERE GROUP_INDEX = %d " + " ORDER BY RESP_ORDER ASC",
						respGroup.getIndex());

				rs = db.executeQuery(sqlText);

				while (rs.next()) {

					OBDtoRespInfo respInfo = new OBDtoRespInfo();
					respInfo.setIndex(db.getInteger(rs, "INDEX"));
					respInfo.setGroupIndex(db.getInteger(rs, "GROUP_INDEX"));
					resp.add(respInfo);
				}
				respGroup.setRespInfo(resp);

				sqlTime = String.format(" OCCUR_TIME <= %s AND OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getToTime().getTime()))),
						OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getFromTime().getTime()))));

				for (OBDtoRespInfo respInfo : respGroup.getRespInfo()) {
					sqlText = String.format(
							" SELECT A.INDEX, A.IPADDRESS, A.PORT, A.TYPE,  A.COMMENT,                                    \n"
									+ " C.RESP_TIME, B.AVG, B.MAX, B.MIN                                                            \n"
									+ " FROM (SELECT * FROM MNG_RESP_SECTION_CHECK WHERE INDEX = %d) A                              \n"
									+ " LEFT JOIN                                                                                   \n"
									+ " (SELECT AVG(RESP_TIME) AS AVG, MAX(RESP_TIME) AS MAX, MIN(RESP_TIME) AS MIN                 \n"
									+ " FROM LOG_RESP_SECTION_CHECK WHERE %s AND RESP_INDEX = %d AND RESP_TIME >= 0) B              \n"
									+ " ON 1=1                                                                                      \n"
									+ " LEFT JOIN (SELECT * FROM TMP_RESP_SECTION_CHECK) C                                          \n"
									+ " ON A.INDEX = C.RESP_INDEX                                                                   \n"
									+ " GROUP BY A.INDEX, A.IPADDRESS, A.PORT, A.TYPE, A.COMMENT, C.RESP_TIME, B.AVG, B.MAX, B.MIN  ",
							respInfo.getIndex(), sqlTime, respInfo.getIndex());

					rs = db.executeQuery(sqlText);
					while (rs.next()) {
						respInfo.setIpaddress(db.getString(rs, "IPADDRESS"));
						respInfo.setPort(db.getInteger(rs, "PORT"));
						respInfo.setType(db.getInteger(rs, "TYPE"));
						respInfo.setCurrRespTime(db.getInteger(rs, "RESP_TIME"));
						respInfo.setAvgRespTime(db.getInteger(rs, "AVG"));
						respInfo.setMaxRespTime(db.getInteger(rs, "MAX"));
						respInfo.setMinRespTime(db.getInteger(rs, "MIN"));
						respInfo.setComment(db.getString(rs, "COMMENT"));
					}
				}
			}

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

	public ArrayList<OBDtoRespGroup> getResponseTimeList(Integer orderType, Integer orderDir) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("get response time"));
		ArrayList<OBDtoRespGroup> retVal = new ArrayList<OBDtoRespGroup>();

		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" SELECT INDEX, NAME, LAST_TIME FROM MNG_RESP_SECTION_GROUP ");

			sqlText += searchRespListOrderType(orderType, orderDir);

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoRespGroup respInfo = new OBDtoRespGroup();
				respInfo.setIndex(db.getInteger(rs, "INDEX"));
				respInfo.setName(db.getString(rs, "NAME"));
				respInfo.setLastTime(db.getTimestamp(rs, "LAST_TIME"));
				retVal.add(respInfo);
			}

			for (OBDtoRespGroup respGroup : retVal) {
				ArrayList<OBDtoRespInfo> resp = new ArrayList<OBDtoRespInfo>();

				sqlText = String.format(
						" SELECT * FROM MNG_RESP_SECTION_CHECK WHERE GROUP_INDEX = %d " + " ORDER BY RESP_ORDER ASC",
						respGroup.getIndex());

				rs = db.executeQuery(sqlText);

				while (rs.next()) {

					OBDtoRespInfo respInfo = new OBDtoRespInfo();
					respInfo.setIndex(db.getInteger(rs, "INDEX"));
					respInfo.setGroupIndex(db.getInteger(rs, "GROUP_INDEX"));
					respInfo.setIpaddress(db.getString(rs, "IPADDRESS"));
					respInfo.setPort(db.getInteger(rs, "PORT"));
					respInfo.setType(db.getInteger(rs, "TYPE"));
					respInfo.setComment(db.getString(rs, "COMMENT"));
					resp.add(respInfo);
				}
				respGroup.setRespInfo(resp);
			}

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

	public ArrayList<OBDtoRespInfo> getRespList() throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("get response time"));
		ArrayList<OBDtoRespInfo> retVal = new ArrayList<OBDtoRespInfo>();

		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" SELECT * FROM MNG_RESP_INTERVAL_CHECK ");

			ResultSet rs;

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoRespInfo respInfo = new OBDtoRespInfo();
				respInfo.setIndex(db.getInteger(rs, "INDEX"));
				respInfo.setIpaddress(db.getString(rs, "IPADDRESS"));
				respInfo.setType(db.getInteger(rs, "TYPE"));
				respInfo.setPort(db.getInteger(rs, "PORT"));
				respInfo.setPath(db.getString(rs, "PATH"));
				respInfo.setComment(db.getString(rs, "COMMENT"));
				retVal.add(respInfo);
			}

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

	public void addVSServiceGroup(OBDtoVSGroupInfo vsGroupInfo) throws OBException {
		final String delimiter = ", ";
		String prefix = "";
		String sqlText = "";
		Integer index = 0;
		StringBuilder query = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" INSERT INTO MNG_VSSERVICE_GROUP " + " (NAME) VALUES (%s);                 ",
					OBParser.sqlString(vsGroupInfo.getName()));

			db.executeUpdate(sqlText);

			sqlText = String.format(
					" SELECT INDEX FROM MNG_VSSERVICE_GROUP    " + " WHERE NAME = %s;                        ",
					OBParser.sqlString(vsGroupInfo.getName()));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				index = db.getInteger(rs, "INDEX");
			}

			if (index == 0)
				return;

			query.append(" INSERT INTO MNG_VSSERVICE_GROUP_MAP                             ")
					.append(" (GR_INDEX, ADC_INDEX, VS_INDEX)                                     ")
					.append(" VALUES                                                           ");
			for (OBDtoADCGroupInfo adcGroup : vsGroupInfo.getAdcList()) {
				for (OBDtoVSservice vsGroup : adcGroup.getVsList()) {
					query.append(prefix).append("(").append(index).append(delimiter).append(adcGroup.getIndex())
							.append(delimiter).append(OBParser.sqlString(vsGroup.getVsIndex())).append(")");
					prefix = ", ";
				}

			}

			query.append(";");
			db.executeUpdate(query.toString());
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), query));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public ArrayList<OBDtoVSGroupInfo> getVSServiceGroupList() throws OBException {
		ArrayList<OBDtoVSGroupInfo> retVal = new ArrayList<OBDtoVSGroupInfo>();

		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT DISTINCT ON (B.ADC_INDEX) A.INDEX, A.NAME, B.GR_INDEX, B.ADC_INDEX, C.IPADDRESS, C.NAME AS ADC_NAME    \n"
							+ " FROM (SELECT INDEX, NAME FROM MNG_VSSERVICE_GROUP) A                                        \n"
							+ " LEFT JOIN (SELECT * FROM MNG_VSSERVICE_GROUP_MAP) B                                         \n"
							+ " ON A.INDEX = B.GR_INDEX                                                                     \n"
							+ " LEFT JOIN (SELECT * FROM MNG_ADC ) C                                                        \n"
							+ " ON B.ADC_INDEX = C.INDEX;                                                                   \n");
			ResultSet rs;

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoVSGroupInfo groupList = new OBDtoVSGroupInfo();
				groupList.setName(db.getString(rs, "NAME"));
				groupList.setIndex(db.getInteger(rs, "INDEX"));
				retVal.add(groupList);
			}

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

	public OBDtoVSGroupInfo getVSServiceGroup(Integer vsGroupIndex, Integer accountIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("VSService Index:%d", vsGroupIndex));
		OBDtoVSGroupInfo retVal = new OBDtoVSGroupInfo();

		ArrayList<OBDtoADCGroupInfo> adcList = new ArrayList<OBDtoADCGroupInfo>();
		Integer count = 0;
		String sqlText = "";
		String accntVsSql = "";
		String accntAdcSql = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (accountIndex != OBDefine.ACCNT_ROLE_ADMIN) {
				accntVsSql = String.format(
						" WHERE INDEX IN (SELECT VS_INDEX FROM MNG_ACCNT_VS_MAP WHERE ACCNT_INDEX = %d) ",
						accountIndex);
				accntAdcSql = String.format(
						" AND INDEX IN (SELECT ADC_INDEX FROM MNG_ACCNT_ADC_MAP  WHERE ACCNT_INDEX = %d) ",
						accountIndex);

			}

			sqlText = String.format(
					" SELECT DISTINCT ON (B.ADC_INDEX) A.INDEX, A.NAME, B.GR_INDEX, B.ADC_INDEX,                \n"
							+ " C.IPADDRESS, C.NAME AS ADC_NAME                                                             \n"
							+ " FROM (SELECT INDEX, NAME FROM MNG_VSSERVICE_GROUP WHERE INDEX = %d) A                       \n"
							+ " LEFT JOIN (SELECT * FROM MNG_VSSERVICE_GROUP_MAP) B                                         \n"
							+ " ON A.INDEX = B.GR_INDEX                                                                     \n"
							+ " LEFT JOIN (SELECT * FROM MNG_ADC WHERE AVAILABLE = %d %s) C                                 \n"
							+ " ON B.ADC_INDEX = C.INDEX;                                                                   \n",
					vsGroupIndex, OBDefine.ADC_STATE.AVAILABLE, accntAdcSql);
			ResultSet rs;

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				retVal.setName(db.getString(rs, "NAME"));
				retVal.setIndex(db.getInteger(rs, "INDEX"));
				OBDtoADCGroupInfo adc = new OBDtoADCGroupInfo();
				adc.setIndex(db.getInteger(rs, "ADC_INDEX"));
				adc.setAdcName(db.getString(rs, "ADC_NAME"));
				adc.setIpAddress(db.getString(rs, "IPADDRESS"));
				adcList.add(adc);

			}

			retVal.setAdcList(adcList);
			for (OBDtoADCGroupInfo adcInfo : retVal.getAdcList()) {
				ArrayList<OBDtoVSservice> vsList = new ArrayList<OBDtoVSservice>();
				OBDtoAdcInfo adcType = new OBAdcManagementImpl().getAdcInfo(adcInfo.getIndex());
				if (adcType.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
					sqlText = String.format(
							" SELECT A.INDEX AS GR_INDEX, A.NAME, B.ADC_INDEX, D.NAME AS VS_NAME, C.INDEX AS VSVC_INDEX,      \n"
									+ " D.VIRTUAL_IP AS VS_IPADDRESS, C.VIRTUAL_PORT AS PORT                                            \n"
									+ " FROM (SELECT * FROM MNG_VSSERVICE_GROUP WHERE INDEX = %d) A                                     \n"
									+ " RIGHT JOIN (SELECT * FROM MNG_VSSERVICE_GROUP_MAP WHERE GR_INDEX = %d AND ADC_INDEX = %d) B     \n"
									+ " ON A.INDEX = B.GR_INDEX                                                                         \n"
									+ " LEFT JOIN (SELECT * FROM TMP_SLB_VS_SERVICE %s) C                                               \n"
									+ " ON B.VS_INDEX = C.INDEX                                                                         \n"
									+ " INNER JOIN TMP_SLB_VSERVER D                                                                    \n"
									+ " ON C.VS_INDEX = D.INDEX                                                                         \n"
									+ " ORDER BY GR_INDEX, NAME                                                                      \n",
							vsGroupIndex, vsGroupIndex, adcInfo.getIndex(), accntVsSql);
				} else {
					sqlText = String.format(
							" SELECT A.INDEX AS GR_INDEX, A.NAME, B.ADC_INDEX, C.NAME AS VS_NAME, C.INDEX AS VSVC_INDEX,      \n"
									+ " C.VIRTUAL_IP AS VS_IPADDRESS, C.VIRTUAL_PORT AS PORT                                            \n"
									+ " FROM (SELECT * FROM MNG_VSSERVICE_GROUP WHERE INDEX = %d) A                                     \n"
									+ " RIGHT JOIN (SELECT * FROM MNG_VSSERVICE_GROUP_MAP WHERE GR_INDEX = %d AND ADC_INDEX = %d) B     \n"
									+ " ON A.INDEX = B.GR_INDEX                                                                         \n"
									+ " LEFT JOIN (SELECT * FROM TMP_SLB_VSERVER  %s) C                                                 \n"
									+ " ON B.VS_INDEX = C.INDEX                                                                         \n"
									+ " ORDER BY GR_INDEX, NAME                                                                      \n",
							vsGroupIndex, vsGroupIndex, adcInfo.getIndex(), accntVsSql);
				}

				rs = db.executeQuery(sqlText);

				while (rs.next()) {
					count++;

					OBDtoVSservice vsService = new OBDtoVSservice();
					vsService.setVsIndex(db.getString(rs, "VSVC_INDEX"));
					vsService.setGroupIndex(db.getInteger(rs, "GR_INDEX"));
					vsService.setVsName(db.getString(rs, "VS_NAME"));
					vsService.setVsPort(db.getInteger(rs, "PORT"));
					vsService.setVsIP(db.getString(rs, "VS_IPADDRESS"));
					vsList.add(vsService);
				}

				adcInfo.setVsList(vsList);
			}
			retVal.setCount(count);

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

	public ArrayList<OBDtoADCGroupInfo> getVSServiceGroupAll(Integer vsGroupIndex, Integer accountIndex)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("VSService Index:%d", vsGroupIndex));
		HashMap<String, OBDtoADCGroupInfo> vsMap = new HashMap<String, OBDtoADCGroupInfo>();
		String sqlText = "";
		String excludeVs = "";

		final OBDatabase db = new OBDatabase();
		try {
			ArrayList<String> vsList = new OBAccountImpl().getNotAssignedGRVSList(accountIndex, vsGroupIndex);
			String sqlTextVS = convertSqlVSList(vsList);
			if (sqlTextVS == null || sqlTextVS.isEmpty()) {
				return new ArrayList<OBDtoADCGroupInfo>(vsMap.values());
			}
			if (vsGroupIndex != OBDefine.DATA_UNAVAILABLE) {
				excludeVs = String.format(" AND A.INDEX NOT IN ( %s ) ", sqlTextVS);
			}

			sqlText = String.format(
					" SELECT MNG_ADC.INDEX AS ADC_INDEX, MNG_ADC.NAME AS ADC_NAME, MNG_ADC.TYPE,            \n"
							+ " VS.VS_INDEX AS VSVC_INDEX, VS.VS_NAME, VS.VS_IPADDRESS, VS.PORT                     \n"
							+ " FROM ( SELECT A.ADC_INDEX, A.INDEX AS VS_INDEX, A.NAME AS VS_NAME,                  \n"
							+ " A.VIRTUAL_IP AS VS_IPADDRESS, A.VIRTUAL_PORT AS PORT                                \n"
							+ " FROM TMP_SLB_VSERVER A                                                              \n"
							+ " WHERE ADC_INDEX IN (SELECT INDEX FROM MNG_ADC                                       \n"
							+ "   WHERE AVAILABLE = %d AND ( TYPE = %d OR TYPE = %d OR TYPE = %d) ) %s              \n"
							+ " ORDER BY ADC_INDEX,VS_IPADDRESS, PORT                                               \n"
							+ " ) VS                                                                                \n"
							+ " LEFT JOIN MNG_ADC                                                                   \n"
							+ " ON VS.ADC_INDEX = MNG_ADC.INDEX                                                     \n"
							+ " UNION ALL                                                                           \n"
							+ " SELECT MNG_ADC.INDEX AS ADC_INDEX, MNG_ADC.NAME ADC_NAME, MNG_ADC.TYPE,             \n"
							+ " VSS.VSVC_INDEX, VSS.VS_NAME, VSS.VS_IPADDRESS, VSS.PORT                             \n"
							+ " FROM ( SELECT A.ADC_INDEX, A.INDEX AS VSVC_INDEX, B.NAME AS VS_NAME,                \n"
							+ " B.VIRTUAL_IP AS VS_IPADDRESS, A.VIRTUAL_PORT AS PORT                                \n"
							+ " FROM TMP_SLB_VS_SERVICE A                                                           \n"
							+ " INNER JOIN TMP_SLB_VSERVER B                                                        \n"
							+ " ON A.VS_INDEX = B.INDEX                                                             \n"
							+ " WHERE B.ADC_INDEX IN                                                                \n"
							+ "     (SELECT INDEX FROM MNG_ADC WHERE  AVAILABLE = %d AND TYPE = %d) %s              \n"
							+ " ORDER BY ADC_INDEX,VS_IPADDRESS, PORT                                               \n"
							+ " ) VSS                                                                               \n"
							+ " LEFT JOIN MNG_ADC                                                                   \n"
							+ " ON VSS.ADC_INDEX = MNG_ADC.INDEX                                                    \n"
							+ " ORDER BY ADC_NAME, VS_NAME, PORT                                                    \n",
					OBDefine.ADC_STATE.AVAILABLE, OBDefine.ADC_TYPE_F5, OBDefine.ADC_TYPE_PIOLINK_PAS,
					OBDefine.ADC_TYPE_PIOLINK_PASK, excludeVs, OBDefine.ADC_STATE.AVAILABLE, OBDefine.ADC_TYPE_ALTEON,
					excludeVs);

			db.openDB();
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				Integer adcIndex = db.getInteger(rs, "ADC_INDEX");// OBDtoAdcVSInfo obj = new OBDtoAdcVSInfo();
				String adcName = db.getString(rs, "ADC_NAME");

				String vsIndex = db.getString(rs, "VSVC_INDEX");
				String vsName = db.getString(rs, "VS_NAME");
				String vsIp = db.getString(rs, "VS_IPADDRESS");
				Integer vsPort = db.getInteger(rs, "PORT");

				String keyName = adcIndex.toString();
				OBDtoADCGroupInfo objVSInfo = vsMap.get(keyName);
				if (objVSInfo == null) {
					objVSInfo = new OBDtoADCGroupInfo();
					objVSInfo.setIndex(adcIndex);
					objVSInfo.setAdcName(adcName);
					objVSInfo.setVsList(new ArrayList<OBDtoVSservice>());
					OBDtoVSservice vsInfo = new OBDtoVSservice();
					vsInfo.setAdcIndex(adcIndex);
					vsInfo.setVsIndex(vsIndex);
					vsInfo.setVsName(vsName);
					vsInfo.setVsIP(vsIp);
					vsInfo.setVsPort(vsPort);
					objVSInfo.getVsList().add(vsInfo);
					vsMap.put(keyName, objVSInfo);
				} else {
					OBDtoVSservice vsInfo = new OBDtoVSservice();
					vsInfo.setAdcIndex(adcIndex);
					vsInfo.setVsIndex(vsIndex);
					vsInfo.setVsName(vsName);
					vsInfo.setVsIP(vsIp);
					vsInfo.setVsPort(vsPort);
					objVSInfo.getVsList().add(vsInfo);
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

		return new ArrayList<OBDtoADCGroupInfo>(vsMap.values());
	}

	public ArrayList<OBDtoVSGroupInfo> getVSServiceGroup(String searchKey, Integer beginIndex, Integer endIndex,
			Integer orderType, Integer orderDir) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("get VSService Group Start"));
		ArrayList<OBDtoVSGroupInfo> retVal = new ArrayList<OBDtoVSGroupInfo>();

		String sqlText = "";
		OBDatabase db = new OBDatabase();
		if (searchKey == null) {
			searchKey = "";
		}

		try {
			int offset = 0;
			if (beginIndex != null && beginIndex.intValue() >= 0)
				offset = beginIndex.intValue();

			int limit = 0;
			String sqlLimit = "";
			if (endIndex != null && endIndex.intValue() >= 0) {
				limit = Math.abs(endIndex.intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}

			String sqlWhere = "";
			if (searchKey.isEmpty() == false) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlWhere = String.format(" WHERE NAME LIKE %s \n", OBParser.sqlString(wildcardKey));
			}
			db.openDB();

			sqlText = String.format(" SELECT INDEX, NAME    FROM MNG_VSSERVICE_GROUP   "
					+ " %s                                                ", sqlWhere);

			sqlText += searchGroupNameListOrderType(orderType, orderDir);

			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			sqlText += ";";
			ResultSet rs;

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoVSGroupInfo vsGroup = new OBDtoVSGroupInfo();
				vsGroup.setIndex(db.getInteger(rs, "INDEX"));
				vsGroup.setName(db.getString(rs, "NAME"));
				retVal.add(vsGroup);
			}

			for (OBDtoVSGroupInfo vsGroupInfo : retVal) {

				sqlText = String.format(
						" SELECT COUNT(VS_INDEX) AS VS_COUNT FROM MNG_VSSERVICE_GROUP_MAP WHERE GR_INDEX = %d; ",
						vsGroupInfo.getIndex());

				rs = db.executeQuery(sqlText);

				while (rs.next()) {
					vsGroupInfo.setCount(db.getInteger(rs, "VS_COUNT"));
				}
			}

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("get VSService Group End"));

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

	public void delVSServiceGroup(ArrayList<Integer> groupIndexList) throws OBException {
		String sqlText = "";
		String sqlTextRS = OBParser.convertSqlGrpIndexList(groupIndexList);
		if (sqlTextRS == null || sqlTextRS.isEmpty()) {
			return;
		}
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" DELETE FROM MNG_VSSERVICE_GROUP WHERE INDEX IN (%s);                                        ",
					sqlTextRS);
			db.executeUpdate(sqlText);

			sqlText = String.format(
					" DELETE FROM MNG_VSSERVICE_GROUP_MAP WHERE GR_INDEX IN (%s);                                 ",
					sqlTextRS);
			db.executeUpdate(sqlText);

			sqlText = String.format(
					" DELETE FROM MNG_DASHBOARD_WIDGET WHERE TARGET_INDEX IN (%s);                                  ",
					OBParser.sqlString(sqlTextRS));
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

	public void setVSServiceGroup(OBDtoVSGroupInfo vsGroupInfo) throws OBException {
		final String delimiter = ", ";
		String prefix = "";
		String sqlText = "";

		StringBuilder query = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(
					" DELETE FROM MNG_VSSERVICE_GROUP_MAP     \n" + " WHERE GR_INDEX = %d;                        \n",
					vsGroupInfo.getIndex());

			db.executeUpdate(sqlText);

			sqlText = String.format(
					" UPDATE MNG_VSSERVICE_GROUP                          \n"
							+ " SET NAME=%s                                             \n"
							+ " WHERE INDEX=%d ;                                        \n",
					OBParser.sqlString(vsGroupInfo.getName()), vsGroupInfo.getIndex());

			db.executeUpdate(sqlText);

			query.append(" INSERT INTO MNG_VSSERVICE_GROUP_MAP                              \n")
					.append(" (GR_INDEX, ADC_INDEX, VS_INDEX)                                       \n")
					.append(" VALUES                                                                \n");
			for (OBDtoADCGroupInfo adcGroup : vsGroupInfo.getAdcList()) {
				for (OBDtoVSservice vsGroup : adcGroup.getVsList()) {
					query.append(prefix).append("(").append(vsGroupInfo.getIndex()).append(delimiter)
							.append(adcGroup.getIndex()).append(delimiter)
							.append(OBParser.sqlString(vsGroup.getVsIndex())).append(")");
					prefix = ", ";
				}

			}

			query.append(";");
			db.executeUpdate(query.toString());

		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), query));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public Integer getVSServiceGroupTotalCount(String searchKey) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("get VSService Group Count Start"));
		Integer retVal = 0;

		String sqlText = "";
		OBDatabase db = new OBDatabase();
		if (searchKey == null) {
			searchKey = "";
		}

		try {

			String sqlWhere = "";
			if (searchKey.isEmpty() == false) {
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlWhere = String.format(" WHERE NAME LIKE %s \n", OBParser.sqlString(wildcardKey));
			}
			db.openDB();

			sqlText = String.format(" SELECT COUNT(INDEX) AS INDEX    FROM MNG_VSSERVICE_GROUP     "
					+ " %s                                                   ", sqlWhere);

			sqlText += ";";
			ResultSet rs;

			rs = db.executeQuery(sqlText);

			if (rs.next()) {
				retVal = db.getInteger(rs, "INDEX");
			}

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("get VSService Group Count End"));

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

	private String makeTimeSqlText(OBDtoSearch searchOption, String columnName) throws OBException {
		String retVal = "";

		if (searchOption == null)
			return retVal;

		if (searchOption.getToTime() == null)
			searchOption.setToTime(new Date());
		retVal = String.format(" AND %s <= %s ", columnName,
				OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getToTime().getTime()))));

		if (searchOption.getFromTime() == null) {
			searchOption.setFromTime(new Date(searchOption.getToTime().getTime() - 7 * 24 * 60 * 60 * 1000));// 7일전 시간.
		}

		retVal += String.format(" AND %s >= %s ", columnName,
				OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getFromTime().getTime()))));

		return retVal;
	}

	public void addSlbUser(OBDtoSlbUser slbUser) throws OBException {
		final String delimiter = ", ";
		String prefix = "";
		StringBuilder query = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);

		if (slbUser == null)
			return;

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			query.append(" INSERT INTO MNG_SLB_USER                                         \n")
					.append(" (NAME, TEAM, PHONE, TYPE, UPDATE_TIME)                           \n")
					.append(" VALUES                                                           \n");
			query.append(prefix).append("(").append(OBParser.sqlString(slbUser.getName())) // User 이름
					.append(delimiter).append(OBParser.sqlString(slbUser.getTeam())) // User Team
					.append(delimiter).append(OBParser.sqlString(slbUser.getPhone())) // User phone
					.append(delimiter).append(slbUser.getType()).append(delimiter)
					.append(OBParser.sqlString(slbUser.getUpdateTime())) // User phone
					.append(")");
			query.append(";");
			db.executeUpdate(query.toString());
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), query));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void setSlbUser(OBDtoSlbUser slbUser) throws OBException {
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" UPDATE MNG_SLB_USER                                                     \n"
//                            + " SET NAME=%s, TEAM=%s, PHONE=%s, TYPE=%d, UPDATE_TIME=%s                     \n"
					+ " SET NAME=%s, TEAM=%s, PHONE=%s, UPDATE_TIME=%s                     \n"
					+ " WHERE INDEX=%d                                                              \n",
					OBParser.sqlString(slbUser.getName()), OBParser.sqlString(slbUser.getTeam()),
					OBParser.sqlString(slbUser.getPhone()), OBParser.sqlString(slbUser.getUpdateTime()),
					slbUser.getIndex());

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

	public void delSlbUser(ArrayList<Integer> slbUserIndex) throws OBException {
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			db.openDB();
			if (slbUserIndex == null || slbUserIndex.isEmpty()) {
				return;
			}

			String indexList = OBParser.convertSqlGrpIndexList(slbUserIndex);

			sqlText = String.format(" DELETE FROM MNG_SLB_USER WHERE INDEX IN (%s); ", indexList);

			db.executeUpdate(sqlText);

		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		}

		catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void delSlbSchedule(ArrayList<Integer> slbUserIndex) throws OBException {
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (slbUserIndex == null || slbUserIndex.isEmpty()) {
				return;
			}

			String indexList = OBParser.convertSqlGrpIndexList(slbUserIndex);

			sqlText = String.format(" DELETE FROM MNG_SLB_SCHEDULE WHERE INDEX IN (%s); ", indexList);

			db.executeUpdate(sqlText);
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		}

		catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public Integer getSlbScheduleListCount(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, searchKey:%s", adcIndex, searchKey));

		OBDatabase db = new OBDatabase();

		if (searchKey == null) {
			searchKey = "";
		}

		String sqlText = "";
		int retVal = 0;

		try {
			db.openDB();

//			String sqlWhere = "";
//            if(searchKey.isEmpty() == false)
//            {
//                // #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
//                String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
//                sqlWhere =
//                        String.format(" WHERE A.NAME LIKE %s OR A.VIRTUAL_IP LIKE %s \n", OBParser.sqlString(wildcardKey),
//                                OBParser.sqlString(wildcardKey));
//            }

			sqlText = String
					.format(" SELECT COUNT(INDEX) AS CNT                                                        \n"
							+ " FROM MNG_SLB_SCHEDULE                                                           \n"
							+ " WHERE ADC_INDEX = %d ", adcIndex);

//            sqlText += searchSlbScheduleListOrderType(orderType, orderDir);

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				retVal = db.getInteger(rs, "CNT");
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result count:%d", retVal));
		return retVal;
	}

	public OBDtoAdcSchedule getSlbSchedule(Integer scheduleIndex) throws OBException {
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		OBDtoAdcSchedule retVal = new OBDtoAdcSchedule();
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT A.INDEX, B.NAME, B.TEAM, B.PHONE, A.STATE, A.OCCUR_TIME, A.NOTICE,                     \n"
							+ " A.RECIEVE_USER, A.RESERVATION_TIME, A.ADC_INDEX, A.ADC_TYPE, A.VS_INDEX, A.VS_NAME,           \n"
							+ " A.VS_IP, A.SUMMARY, A.CHANGE_TYPE, A.CHANGE_YN, A.CHANGE_OBJECT_TYPE, A.ACCNT_IP,             \n"
							+ "  A.ACCNT_INDEX, A.CONFIG_CHUNK FROM (SELECT INDEX, ORIGIN_USER, STATE, OCCUR_TIME, NOTICE,    \n"
							+ "  RECIEVE_USER, RESERVATION_TIME, ADC_INDEX, ADC_TYPE, VS_INDEX, VS_NAME,                      \n"
							+ "  VS_IP, SUMMARY, CHANGE_TYPE, CHANGE_YN, CHANGE_OBJECT_TYPE, ACCNT_IP,                        \n"
							+ "  ACCNT_INDEX, CONFIG_CHUNK                                                                    \n"
							+ "  FROM MNG_SLB_SCHEDULE) A                                                                     \n"
							+ "  LEFT JOIN                                                                                    \n"
							+ "  (SELECT INDEX, NAME, TEAM, PHONE                                                             \n"
							+ "  FROM MNG_SLB_USER) B                                                                         \n"
							+ "  ON A.ORIGIN_USER = B.INDEX                                                                   \n"
							+ "  WHERE A.INDEX = %d;",
					scheduleIndex);

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				retVal.setIndex(db.getInteger(rs, "INDEX"));
				retVal.setName(db.getString(rs, "NAME"));
				retVal.setTeam(db.getString(rs, "TEAM"));
				retVal.setPhone(db.getString(rs, "PHONE"));
				retVal.setState(db.getInteger(rs, "STATE"));
				retVal.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				retVal.setNotice(db.getInteger(rs, "NOTICE"));
				retVal.setSmsReceive(db.getString(rs, "RECIEVE_USER"));
				retVal.setReservationTime(db.getTimestamp(rs, "RESERVATION_TIME"));
				retVal.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				retVal.setAdcType(db.getInteger(rs, "ADC_TYPE"));
				retVal.setVsIndex(db.getString(rs, "VS_INDEX"));
				retVal.setVsName(db.getString(rs, "VS_NAME"));
				retVal.setVsIp(db.getString(rs, "VS_IP"));
				retVal.setSummary(db.getString(rs, "SUMMARY"));
				retVal.setChangeType(db.getInteger(rs, "CHANGE_TYPE"));
				retVal.setChangeObjectType(db.getInteger(rs, "CHANGE_OBJECT_TYPE"));
				retVal.setChangeYN(db.getInteger(rs, "CHANGE_YN"));
				retVal.setAccntIp(db.getString(rs, "ACCNT_IP"));
				retVal.setAccntIndex(db.getInteger(rs, "ACCNT_INDEX"));
				ByteArrayInputStream bisConfigChunk = new ByteArrayInputStream(rs.getBytes("CONFIG_CHUNK"));
				ObjectInputStream oisConfigChunk = new ObjectInputStream(bisConfigChunk);

				retVal.setChunkAlteon(null);
				retVal.setChunkF5(null);
				int adcType = db.getInteger(rs, "ADC_TYPE");

				if (adcType == OBDefine.ADC_TYPE_F5) {
					retVal.setChunkF5((OBDtoAdcConfigChunkF5) oisConfigChunk.readObject());
				} else if (adcType == OBDefine.ADC_TYPE_ALTEON) {
					retVal.setChunkAlteon((OBDtoAdcConfigChunkAlteon) oisConfigChunk.readObject());
				}
			}
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

	public ArrayList<OBDtoAdcSchedule> getSlbScheduleList(Integer adcIndex, String searchKey, Integer beginIndex,
			Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start2. searchKey:%s, beginIndex:%d, endIndex:%d", searchKey, beginIndex, endIndex));
		ArrayList<OBDtoAdcSchedule> retVal = new ArrayList<OBDtoAdcSchedule>();

		OBDatabase db = new OBDatabase();

		if (searchKey == null) {
			searchKey = "";
		}

		String sqlText = "";

		try {
			db.openDB();

			int offset = 0;
			if (beginIndex != null && beginIndex.intValue() >= 0)
				offset = beginIndex.intValue();

			int limit = 0;
			String sqlLimit = "";
			if (endIndex != null && endIndex.intValue() >= 0) {
				limit = Math.abs(endIndex.intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}

			String sqlWhere = "";
			if (searchKey.isEmpty() == false) {
				// #3984-2 #10: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				String wildcardKey = "%" + OBParser.removeWildcard(searchKey) + "%";
				sqlWhere = String.format(" AND A.VS_NAME LIKE %s OR A.VS_IP LIKE %s \n",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
			}

			sqlText = String.format(
					" SELECT A.INDEX, B.NAME, B.TEAM, B.PHONE, A.STATE, A.OCCUR_TIME, A.NOTICE,                     \n"
							+ " A.RECIEVE_USER, A.RESERVATION_TIME, A.ADC_INDEX, A.ADC_TYPE, A.VS_INDEX, A.VS_NAME,           \n"
							+ " A.VS_IP, A.SUMMARY, A.CHANGE_TYPE, A.CHANGE_YN, A.CHANGE_OBJECT_TYPE, A.ACCNT_IP,             \n"
							+ "  A.ACCNT_INDEX, A.CONFIG_CHUNK FROM (SELECT INDEX, ORIGIN_USER, STATE, OCCUR_TIME, NOTICE,    \n"
							+ "  RECIEVE_USER, RESERVATION_TIME, ADC_INDEX, ADC_TYPE, VS_INDEX, VS_NAME,                      \n"
							+ "  VS_IP, SUMMARY, CHANGE_TYPE, CHANGE_YN, CHANGE_OBJECT_TYPE, ACCNT_IP,                        \n"
							+ "  ACCNT_INDEX, CONFIG_CHUNK                                                                    \n"
							+ "  FROM MNG_SLB_SCHEDULE) A                                                                     \n"
							+ "  LEFT JOIN                                                                                    \n"
							+ "  (SELECT INDEX, NAME, TEAM, PHONE                                                             \n"
							+ "  FROM MNG_SLB_USER) B                                                                         \n"
							+ "  ON A.ORIGIN_USER = B.INDEX                                                                   \n"
							+ "  WHERE ADC_INDEX = %d",
					adcIndex);
			sqlText += sqlWhere;
			sqlText += searchSlbScheduleListOrderType(orderType, orderDir);

			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcSchedule obj = new OBDtoAdcSchedule();

				obj.setIndex(db.getInteger(rs, "INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setTeam(db.getString(rs, "TEAM"));
				obj.setPhone(db.getString(rs, "PHONE"));
				obj.setState(db.getInteger(rs, "STATE"));
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setNotice(db.getInteger(rs, "NOTICE"));
				obj.setSmsReceive(db.getString(rs, "RECIEVE_USER"));
				obj.setReservationTime(db.getTimestamp(rs, "RESERVATION_TIME"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setAdcType(db.getInteger(rs, "ADC_TYPE"));
				obj.setVsIndex(db.getString(rs, "VS_INDEX"));
				obj.setVsName(db.getString(rs, "VS_NAME"));
				obj.setVsIp(db.getString(rs, "VS_IP"));
				obj.setSummary(db.getString(rs, "SUMMARY"));
				obj.setChangeType(db.getInteger(rs, "CHANGE_TYPE"));
				obj.setChangeObjectType(db.getInteger(rs, "CHANGE_OBJECT_TYPE"));
				obj.setChangeYN(db.getInteger(rs, "CHANGE_YN"));
				obj.setAccntIp(db.getString(rs, "ACCNT_IP"));
				obj.setAccntIndex(db.getInteger(rs, "ACCNT_INDEX"));

				obj.setReservedHour(db.getTimestamp(rs, "RESERVATION_TIME").getHours());
				obj.setReservedMin(db.getTimestamp(rs, "RESERVATION_TIME").getMinutes());
//                ByteArrayInputStream bisConfigChunk = new ByteArrayInputStream(rs.getBytes("CONFIG_CHUNK"));
//                ObjectInputStream oisConfigChunk = new ObjectInputStream(bisConfigChunk);

				obj.setChunkAlteon(null);
				obj.setChunkF5(null);

//                int adcType = db.getInteger(rs, "ADC_TYPE");
//
//                if(adcType == OBDefine.ADC_TYPE_F5)
//                {
//                    obj.setChunkF5((OBDtoAdcConfigChunkF5) oisConfigChunk.readObject());
//                }
//                else if(adcType == OBDefine.ADC_TYPE_ALTEON)
//                {
//                    obj.setChunkAlteon((OBDtoAdcConfigChunkAlteon) oisConfigChunk.readObject());
//                }
//                else if(adcType == OBDefine.ADC_TYPE_PIOLINK_PAS)
//                {
//                    obj.setChunkPAS((OBDtoAdcConfigChunkPAS) oisConfigChunk.readObject());
//                }
//                else if(adcType == OBDefine.ADC_TYPE_PIOLINK_PASK)
//                {
//                    obj.setChunkPASK((OBDtoAdcConfigChunkPASK) oisConfigChunk.readObject());
//                }
				retVal.add(obj);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result list count:%d", retVal.size()));
		return retVal;
	}

	public ArrayList<OBDtoAdcSchedule> getAllSlbScheduleList() throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "All slb schedule start");
		ArrayList<OBDtoAdcSchedule> retVal = new ArrayList<OBDtoAdcSchedule>();

		OBDatabase db = new OBDatabase();

		String sqlText = "";

		try {
			db.openDB();
			// All incomplete reservation schedule
			sqlText = String.format(
					" SELECT A.INDEX, B.NAME, B.TEAM, B.PHONE, A.STATE, A.OCCUR_TIME, A.NOTICE,                     \n"
							+ " A.RECIEVE_USER, A.RESERVATION_TIME, A.ADC_INDEX, A.ADC_TYPE, A.VS_INDEX, A.VS_NAME,           \n"
							+ " A.VS_IP, A.SUMMARY, A.CHANGE_TYPE, A.CHANGE_YN, A.CHANGE_OBJECT_TYPE, A.ACCNT_IP,             \n"
							+ "  A.ACCNT_INDEX, A.CONFIG_CHUNK FROM (SELECT INDEX, ORIGIN_USER, STATE, OCCUR_TIME, NOTICE,    \n"
							+ "  RECIEVE_USER, RESERVATION_TIME, ADC_INDEX, ADC_TYPE, VS_INDEX, VS_NAME,                      \n"
							+ "  VS_IP, SUMMARY, CHANGE_TYPE, CHANGE_YN, CHANGE_OBJECT_TYPE, ACCNT_IP,                        \n"
							+ "  ACCNT_INDEX, CONFIG_CHUNK                                                                    \n"
							+ "  FROM MNG_SLB_SCHEDULE) A                                                                     \n"
							+ "  LEFT JOIN                                                                                    \n"
							+ "  (SELECT INDEX, NAME, TEAM, PHONE                                                             \n"
							+ "  FROM MNG_SLB_USER) B                                                                         \n"
							+ "  ON A.ORIGIN_USER = B.INDEX                                                                   \n"
							+ "  WHERE A.STATE = %d                                                                           \n",
					OBDefine.CONFIG_STATE.INCOMPLETE);

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcSchedule obj = new OBDtoAdcSchedule();

				obj.setIndex(db.getInteger(rs, "INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setTeam(db.getString(rs, "TEAM"));
				obj.setPhone(db.getString(rs, "PHONE"));
				obj.setState(db.getInteger(rs, "STATE"));
				obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				obj.setNotice(db.getInteger(rs, "NOTICE"));
				obj.setSmsReceive(db.getString(rs, "RECIEVE_USER"));
				obj.setReservationTime(db.getTimestamp(rs, "RESERVATION_TIME"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setAdcType(db.getInteger(rs, "ADC_TYPE"));
				obj.setVsIndex(db.getString(rs, "VS_INDEX"));
				obj.setVsName(db.getString(rs, "VS_NAME"));
				obj.setVsIp(db.getString(rs, "VS_IP"));
				obj.setSummary(db.getString(rs, "SUMMARY"));
				obj.setChangeType(db.getInteger(rs, "CHANGE_TYPE"));
				obj.setChangeObjectType(db.getInteger(rs, "CHANGE_OBJECT_TYPE"));
				obj.setChangeYN(db.getInteger(rs, "CHANGE_YN"));
				obj.setAccntIp(db.getString(rs, "ACCNT_IP"));
				obj.setAccntIndex(db.getInteger(rs, "ACCNT_INDEX"));

				ByteArrayInputStream bisConfigChunk = new ByteArrayInputStream(rs.getBytes("CONFIG_CHUNK"));
				ObjectInputStream oisConfigChunk = new ObjectInputStream(bisConfigChunk);

				obj.setChunkAlteon(null);
				obj.setChunkF5(null);

				int adcType = db.getInteger(rs, "ADC_TYPE");

				if (adcType == OBDefine.ADC_TYPE_F5) {
					obj.setChunkF5((OBDtoAdcConfigChunkF5) oisConfigChunk.readObject());
				} else if (adcType == OBDefine.ADC_TYPE_ALTEON) {
					obj.setChunkAlteon((OBDtoAdcConfigChunkAlteon) oisConfigChunk.readObject());
				}
				retVal.add(obj);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result list count:%d", retVal.size()));
		return retVal;
	}

	public Integer getSlbUserListCount(Integer userType, Integer accntIndex, String searchKey) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. userType:%d, searchKey:%s", userType, searchKey));

		OBDatabase db = new OBDatabase();

		if (searchKey == null) {
			searchKey = "";
		}

		String sqlText = "";
		int retVal = 0;

		try {
			db.openDB();

			sqlText = String.format(" SELECT COUNT(INDEX) AS CNT FROM MNG_SLB_USER          \n");
			if (userType == OBDefine.SLBUSER_TYPE.APPLICANT) {
				sqlText += String.format(" WHERE TYPE = %d \n", userType);
			} else if (userType == OBDefine.SLBUSER_TYPE.RECEIVE) {
				sqlText += String.format(" WHERE TYPE = %d \n", userType);
			}

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				retVal = db.getInteger(rs, "CNT");
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result count:%d", retVal));
		return retVal;
	}

	public ArrayList<OBDtoSlbUser> getSlbUserList(Integer userType, Integer beginIndex, Integer endIndex,
			Integer orderType, Integer orderDir) throws OBException {
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		ArrayList<OBDtoSlbUser> retVal = new ArrayList<OBDtoSlbUser>();
		try {
			db.openDB();

			int offset = 0;
			if (beginIndex != null && beginIndex.intValue() >= 0)
				offset = beginIndex.intValue();

			int limit = 0;
			String sqlLimit = "";
			if (endIndex != null && endIndex.intValue() >= 0) {
				limit = Math.abs(endIndex.intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}

			sqlText = String.format(" SELECT INDEX, NAME, TEAM, PHONE, TYPE FROM MNG_SLB_USER          \n");
			if (userType == OBDefine.SLBUSER_TYPE.APPLICANT) {
				sqlText += String.format(" WHERE TYPE = %d \n", userType);
			} else if (userType == OBDefine.SLBUSER_TYPE.RECEIVE) {
				sqlText += String.format(" WHERE TYPE = %d \n", userType);
			}

			sqlText += searchSlbUserListOrderType(orderType, orderDir);// ";";

			if (sqlLimit != null && !sqlLimit.isEmpty())
				sqlText += sqlLimit;

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoSlbUser obj = new OBDtoSlbUser();
				obj.setIndex(db.getInteger(rs, "INDEX"));
				obj.setName(db.getString(rs, "NAME"));
				obj.setTeam(db.getString(rs, "TEAM"));
				obj.setPhone(db.getString(rs, "PHONE"));
				obj.setType(db.getInteger(rs, "TYPE"));
				retVal.add(obj);
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

	public void addSlbSchedule(OBDtoSlbUser slbUser, OBDtoAdcSchedule slbSchedule) throws OBException {
		String sqlText = "";

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			if (getSlbUser(slbUser.getPhone()) == 0) {
				return;
			}

			slbSchedule.setOriginUser(getSlbUser(slbUser.getPhone()));

			sqlText = " INSERT INTO MNG_SLB_SCHEDULE                                                                \n"
					+ " (ORIGIN_USER, STATE, OCCUR_TIME, NOTICE, RECIEVE_USER, RESERVATION_TIME,                \n"
					+ " ADC_INDEX, ADC_TYPE, VS_INDEX, VS_NAME, VS_IP, SUMMARY,                                 \n"
					+ " CHANGE_TYPE, CHANGE_YN, CHANGE_OBJECT_TYPE, ACCNT_IP, ACCNT_INDEX, CONFIG_CHUNK)        \n"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);                             \n";

			db.initPreparedStatement(sqlText);
			db.setPreparedStatementInt(1, slbSchedule.getOriginUser());
			db.setPreparedStatementInt(2, slbSchedule.getState());
			db.setPreparedStatementTimestamp(3, OBDateTime.toTimestamp(OBDateTime.now()));
			db.setPreparedStatementInt(4, slbSchedule.getNotice());
			db.setPreparedStatementString(5, slbSchedule.getSmsReceive());
			db.setPreparedStatementTimestamp(6, slbSchedule.getReservationTime());
			db.setPreparedStatementInt(7, slbSchedule.getAdcIndex());
			db.setPreparedStatementInt(8, slbSchedule.getAdcType());
			db.setPreparedStatementString(9, slbSchedule.getVsIndex());
			db.setPreparedStatementString(10, slbSchedule.getVsName()); // description
			db.setPreparedStatementString(11, slbSchedule.getVsIp());
			db.setPreparedStatementString(12, slbSchedule.getSummary());
			db.setPreparedStatementInt(13, slbSchedule.getChangeType());
			db.setPreparedStatementInt(14, slbSchedule.getChangeYN());
			db.setPreparedStatementInt(15, slbSchedule.getChangeObjectType());
			db.setPreparedStatementString(16, slbSchedule.getAccntIp());
			db.setPreparedStatementInt(17, slbSchedule.getAccntIndex());
			if (slbSchedule.getChunkF5() != null) {
				db.setPreparedStatementObject(18, slbSchedule.getChunkF5());
			} else if (slbSchedule.getChunkAlteon() != null) {
				db.setPreparedStatementObject(18, slbSchedule.getChunkAlteon());
			}
			db.executeUpdatePreparedStreamt();
			db.deInitPreparedStatement();
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

	public void setSlbSchedule(OBDtoAdcSchedule slbSchedule) throws OBException {
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(
					" UPDATE MNG_SLB_SCHEDULE                                                             \n"
							+ " SET ORIGIN_USER=?, STATE=?, OCCUR_TIME=?, NOTICE=?,                                     \n"
							+ " RECIEVE_USER=?, RESERVATION_TIME=?, ADC_INDEX=?, ADC_TYPE=?, VS_INDEX=?, VS_NAME=?, \n"
							+ " VS_IP=?, SUMMARY=?, CHANGE_TYPE=?, CHANGE_YN=?, CHANGE_OBJECT_TYPE=?,                   \n"
							+ " ACCNT_IP=?, ACCNT_INDEX=?, CONFIG_CHUNK=?                                               \n"
							+ " WHERE INDEX=%d;                                                                         \n",
					slbSchedule.getIndex());

			db.initPreparedStatement(sqlText);
			db.setPreparedStatementInt(1, slbSchedule.getOriginUser());
			db.setPreparedStatementInt(2, slbSchedule.getState());
			db.setPreparedStatementTimestamp(3, OBDateTime.toTimestamp(OBDateTime.now()));
			db.setPreparedStatementInt(4, slbSchedule.getNotice());
			db.setPreparedStatementString(5, slbSchedule.getSmsReceive());
			db.setPreparedStatementTimestamp(6, slbSchedule.getReservationTime());
			db.setPreparedStatementInt(7, slbSchedule.getAdcIndex());
			db.setPreparedStatementInt(8, slbSchedule.getAdcType());
			db.setPreparedStatementString(9, slbSchedule.getVsIndex());
			db.setPreparedStatementString(10, slbSchedule.getVsName()); // description
			db.setPreparedStatementString(11, slbSchedule.getVsIp()); // description
			db.setPreparedStatementString(12, slbSchedule.getSummary());
			db.setPreparedStatementInt(13, slbSchedule.getChangeType());
			db.setPreparedStatementInt(14, slbSchedule.getChangeYN());
			db.setPreparedStatementInt(15, slbSchedule.getChangeObjectType());
			db.setPreparedStatementString(16, slbSchedule.getAccntIp());
			db.setPreparedStatementInt(17, slbSchedule.getAccntIndex());
			if (slbSchedule.getChunkF5() != null) {
				db.setPreparedStatementObject(18, slbSchedule.getChunkF5());
			} else if (slbSchedule.getChunkAlteon() != null) {
				db.setPreparedStatementObject(18, slbSchedule.getChunkAlteon());
			} else {
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, String.format("SLB Schedule Data null"));
			}
			db.executeUpdatePreparedStreamt();
			db.deInitPreparedStatement();
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

	public OBDtoSlbUser getSlbUser(Integer slbUserIndex) throws OBException {
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		OBDtoSlbUser retVal = new OBDtoSlbUser();
		try {
			db.openDB();

			sqlText = String.format(
					"   SELECT INDEX, NAME, TEAM, PHONE, TYPE, UPDATE_TIME FROM MNG_SLB_USER    \n"
							+ " WHERE INDEX = %d                                                        \n",
					slbUserIndex);

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				retVal.setIndex(db.getInteger(rs, "INDEX"));
				retVal.setName(db.getString(rs, "NAME"));
				retVal.setTeam(db.getString(rs, "TEAM"));
				retVal.setPhone(db.getString(rs, "PHONE"));
				retVal.setType(db.getInteger(rs, "TYPE"));
				retVal.setUpdateTime(db.getTimestamp(rs, "UPDATE_TIME"));
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

	public Integer getSlbUser(String phone) throws OBException {
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		Integer retVal = 0;
		try {
			db.openDB();

			sqlText = String.format("   SELECT INDEX FROM MNG_SLB_USER             \n"
					+ " WHERE PHONE = %s                           \n", OBParser.sqlString(phone));

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next()) {
				retVal = db.getInteger(rs, "INDEX");
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

	public OBDtoSlbUser getLastRespUserInfo() throws OBException {
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		OBDtoSlbUser retVal = new OBDtoSlbUser();
		try {
			db.openDB();

//            sqlText =
//                    String
//                            .format(" SELECT INDEX, NAME, TEAM, PHONE, TYPE FROM MNG_SLB_USER          \n");
//            
//            sqlText += String.format(" ORDER BY UPDATE_TIME DESC LIMIT 1 \n");

			sqlText = String.format(" SELECT B.INDEX, B.NAME, B.TEAM, B.PHONE, B.TYPE                 \n"
					+ " FROM MNG_SLB_SCHEDULE A                                         \n"
					+ " LEFT JOIN                                                       \n"
					+ " (SELECT INDEX, NAME, TEAM, PHONE, TYPE FROM MNG_SLB_USER) B     \n"
					+ " ON A.ORIGIN_USER = B.INDEX                                      \n"
					+ " ORDER BY A.OCCUR_TIME DESC LIMIT 1                              ");

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				retVal.setIndex(db.getInteger(rs, "INDEX"));
				retVal.setName(db.getString(rs, "NAME"));
				retVal.setTeam(db.getString(rs, "TEAM"));
				retVal.setPhone(db.getString(rs, "PHONE"));
				retVal.setType(db.getInteger(rs, "TYPE"));
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

	public void addMessageToSMS(String message, String smsReciever) throws OBException {
		if (message == null || message == "" || smsReciever == null || smsReciever == "") {
			return;
		}

		List<String> recieverList = convertJsontoPhoneList(smsReciever);

		String sqlText = "";
		OBDatabaseMssql db = new OBDatabaseMssql();
		try {
			db.openDB();

			sqlText = " INSERT INTO SMS_SEND                \n" + "     (SMS_MSG, TEL_NUM)          \n"
					+ "     VALUES                      \n";
			int recieveLength = recieverList.size();
			for (int i = 0; i < recieveLength; i++) {
				sqlText += String.format("  (%s, %s)        \n", OBParser.sqlString(message),
						OBParser.sqlString(recieverList.get(i)));

				if (recieveLength != i + 1) {
					sqlText += ",";
				}

			}

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

	private ArrayList<String> convertJsontoPhoneList(String smsString) {
		JsonParser parser = new JsonParser();
		JsonArray jarray = parser.parse(smsString).getAsJsonArray();
		int jsonSize = jarray.size();
		ArrayList<String> phoneList = new ArrayList<String>();
		for (int j = 0; j < jsonSize; j++) {
			JsonObject personObject = (JsonObject) jarray.get(j);
			phoneList.add(personObject.get("hp").getAsString());
		}
		return phoneList;
	}
}