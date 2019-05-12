package kr.openbase.adcsmart.service.impl.alteon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import kr.openbase.adcsmart.service.OBAdcVServer;
import kr.openbase.adcsmart.service.OBSystemAudit;
import kr.openbase.adcsmart.service.adcmond.OBAdcMonitorDB;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcHealthCheckAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeDetail;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteonChanged;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberAlteonChanged;
import kr.openbase.adcsmart.service.dto.OBDtoAdcProfile;
import kr.openbase.adcsmart.service.dto.OBDtoAdcRealServerGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSchedule;
import kr.openbase.adcsmart.service.dto.OBDtoAdcScope;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerNotice;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVService;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServiceChanged;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoNetworkInterface;
import kr.openbase.adcsmart.service.dto.OBDtoSLBUpdateStatus;
import kr.openbase.adcsmart.service.dto.OBDtoVrrpInfo;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigChunkAlteon;
import kr.openbase.adcsmart.service.impl.OBAdcConfigHistoryImpl;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.impl.OBSystemAuditImpl;
import kr.openbase.adcsmart.service.impl.OBTelnetCmndExecV2;
import kr.openbase.adcsmart.service.impl.OBVServerDB;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBAdcAlteonHandler;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBCLIParserAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoAdcTimeAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoFdbInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcConfigSlbAlteon;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcSystemInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcVServerAll;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcVlan;
import kr.openbase.adcsmart.service.impl.dto.OBDtoVrrpIndexInfo;
import kr.openbase.adcsmart.service.impl.f5.DtoVlanTunnelFilter;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcNodePAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcHealthCheckPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcNodePASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.snmp.OBSnmpOidDB;
import kr.openbase.adcsmart.service.snmp.alteon.OBSnmpAlteon;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoSnmpVrrpAlteon;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidCfgAdcAdditional;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBLicenseExpiredException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBNetwork;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBAdcVServerAlteon implements OBAdcVServer {
	private HashMap<String, String> groupMap = new HashMap<String, String>();
	private HashMap<String, String> realServerMap = new HashMap<String, String>();

//	public static void main(String[] args)
//	{
//		try
//		{
//			ArrayList<String> vsIndexList = new ArrayList<String>();
//			vsIndexList.add("29_51");
//			OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
//			extraInfo.setAccountIndex(1);
//			extraInfo.setClientIPAddress("172.172.2.2");
//			new OBAdcVServerAlteon().delVServer(29, vsIndexList, extraInfo);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	private void addVServerAlteon(Integer adcIndex, OBDtoAdcInfo adcInfo, OBDtoAdcVServerAlteon vServerInfo,
			ArrayList<OBDtoAdcVService> vsrvList, OBDtoExtraInfo extraInfo, OBVServerDB vDB)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. index:%d, vServerInfo:%s vsrvList:%s", adcIndex, vServerInfo, vsrvList));

		ArrayList<OBDtoAdcVService> vServiceList;
		ArrayList<OBDtoAdcPoolAlteon> newPoolList;
		ArrayList<OBDtoAdcNodeAlteon> newNodeList;
		ArrayList<OBDtoAdcVService> newDelVSerivceList;
		ArrayList<OBDtoAdcVService> newAddVSerivceList;
		ArrayList<OBDtoAdcPoolAlteon> newDelPoolMemberList;
		ArrayList<OBDtoAdcPoolAlteon> newAddPoolMemberList;
		OBDtoAdcConfigChunkAlteon configChunk;
		OBAdcConfigHistoryImpl historyManager;

		try {
			vServerInfo.setUseYN(OBDefine.STATE_ENABLE);
			vServerInfo.setStatus(OBDefine.VS_STATUS.AVAILABLE);// default

			if (vServerInfo.getAlteonId() == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "invalid alteon id");
			}

			if (vDB.getVServerInfoByAlteonID(adcIndex, vServerInfo.getAlteonId()) != null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "already exist alteon id");
			}

			// pool id, node id 지정.
			vServiceList = allocAlteonIDs(adcIndex, vServerInfo, vsrvList, vDB);

			// 신규 추가된 pool인지 검사하여 정보를 추출한다.
			newPoolList = getNewPoolList(adcIndex, vServerInfo, vServiceList, vDB);

			// 신규 추가된 노드가 있는지 검사하여 정보를 추출한다.
			newNodeList = getNewNodeList(adcIndex, vServerInfo, vServiceList, vDB);

			// pool에 추가된 node가 있는지 검사하여 추출한다.
			newAddPoolMemberList = getNewAddPoolmemberList(adcIndex, vServerInfo, vServiceList, vDB);

			// pool에서 삭제된 node가 있는지 검사하여 정보를 추출한다.
			newDelPoolMemberList = getNewDelPoolmemberList(adcIndex, vServerInfo, vServiceList, vDB);

			// 추가된 virtual service가 있는지 검사한다.
			newAddVSerivceList = getNewAddVServiceList(adcIndex, vServerInfo.getIndex(), vServiceList, vDB);

			// 삭제된 virtual service가 있는지 검사한다.
			newDelVSerivceList = getNewDelVServiceList(adcIndex, vServerInfo.getIndex(), vServiceList, vDB);

			// 변경이력 준비
			historyManager = new OBAdcConfigHistoryImpl();
			configChunk = historyManager.MakeConfigChunkAlteon(vServerInfo, OBDefine.CHANGE_BY_USER,
					OBDefine.CHANGE_TYPE_ADD, OBDefine.CHANGE_OBJECT_VIRTUALSERVER, extraInfo.getAccountIndex());
			// 변경이력 준비 끝
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// 텔넷 연결.
		OBAdcAlteonHandler alteon = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
		alteon.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
				adcInfo.getConnService(), adcInfo.getConnPort());
		try {
			alteon.login();
		} catch (OBExceptionUnreachable e) {
			throw new OBExceptionUnreachable(e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBExceptionLogin(e.getMessage());
		}

		try {
			// vrrp info 을 추출한다.
			OBDtoVrrpInfo vrrpInfo = new OBAdcManagementImpl().getVrrpInfo(adcIndex);

			Integer vrid = 0;
			Integer vrrp = 0;
			Integer vrrpInterface = 0;

			if (vrrpInfo != null && vrrpInfo.getPriority() != 0) {
				vrrpInterface = getProperInterface(adcIndex, vServerInfo.getvIP());
				if (vrrpInterface == null || vrrpInterface.equals(0)) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to add vrrp info. invalid interface num."));
					throw new OBException(OBException.ERRCODE_INVALID_PORT_INFO);// , String.format("failed to get
																					// interface number. adcIndex:%d,
																					// vIP:%s\n", adcIndex,
																					// vServerInfo.getvIP()));
				}

				// vrrp, vrid값을 계산하여 부여한다.
				OBDtoVrrpIndexInfo vrrpIndex = calProperVrrpIndex(adcInfo, Integer.parseInt(vServerInfo.getAlteonId()),
						Integer.parseInt(vServerInfo.getAlteonId()), alteon);
				vrid = vrrpIndex.getVrid();
				vrrp = vrrpIndex.getVrrp();
			}

			// 신규 pool 추가
			if (newPoolList.size() != 0) {
				alteon.addServerGroupList(newPoolList);
			}

			// 신규 node 추가
			if (newNodeList.size() != 0) {
				alteon.addRealserverList(newNodeList, adcInfo.getAdcIpAddress());
			}

			// pool에 node 삭제.
			if (newDelPoolMemberList.size() != 0) {
				for (int i = 0; i < newDelPoolMemberList.size(); i++) {
					OBDtoAdcPoolAlteon obj = newDelPoolMemberList.get(i);
					alteon.delPoolMember(obj.getAlteonId(), obj.getMemberList(), adcInfo.getAdcIpAddress());
				}
			}

			// pool에 node 추가
			if (newAddPoolMemberList.size() != 0) {
				for (int i = 0; i < newAddPoolMemberList.size(); i++) {
					OBDtoAdcPoolAlteon obj = newAddPoolMemberList.get(i);
					alteon.addPoolMemberList(obj.getAlteonId(), obj.getMemberList(), adcInfo.getAdcIpAddress());
				}
			}

			// virtual server 추가
			alteon.addVirtualServer(vServerInfo.getAlteonId(), vServerInfo.getName(), vServerInfo.getvIP(),
					vServerInfo.getUseYN(), adcInfo.getAdcIpAddress());

			// virtual service 추가.
			if (newDelVSerivceList.size() != 0) {
				alteon.delVirtualService(vServerInfo.getAlteonId(), newDelVSerivceList, adcInfo.getAdcIpAddress());
			}

			if (newAddVSerivceList.size() != 0) {
				alteon.addVirtualServiceList(vServerInfo.getAlteonId(), newAddVSerivceList, adcInfo.getAdcIpAddress());
			}

			boolean isVrrpAddress = new OBAdcManagementImpl().getVrrpInfo(vServerInfo.getvIP(), adcIndex,
					adcInfo.getAdcIpAddress(), OBDefine.ADC_TYPE_ALTEON, adcInfo.getSwVersion(), adcInfo.getSnmpInfo());

			if (adcInfo.getPeerAdcIPAddress() != null && !adcInfo.getPeerAdcIPAddress().isEmpty()) {
				if (!isVrrpAddress) {
					// vrrp 추가한다.
					if (vrid != null && vrid.intValue() != 0) {
						alteon.addVrrp(vServerInfo.getvIP(), vrrp, vrid, vrrpInterface, vrrpInfo,
								adcInfo.getAdcIpAddress());
					}
				}
			}

			// apply.
			alteon.cmndApply();
		} catch (OBException e) {
			alteon.cmndRevert();
			alteon.disconnect();
			throw e;
		} catch (Exception e) {
			alteon.cmndRevert();
			alteon.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		try {
			downloadSlbConfig(adcIndex, true);
//			for(int i=0; i<3; i++)
//			{
//				if(downloadSlbConfig(adcIndex, false)==true)
//					break;
//				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("%s: %s, callstack:%s", sqlText, e.getMessage(), new OBUtility().getStackTrace()));
//				OBDateTime.Sleep(1000);
//			}

			// 변경이력 기록
			vDB.getVServerInfoAlteon(vServerInfo.getIndex()); // 기존 상태확인
			historyManager.writeConfigHistoryAlteon(configChunk);

			if (true == new OBEnvManagementImpl().isAlteonAutoSave()) {
				alteon.cmndSave();
			}
		} catch (OBException e) {
			alteon.cmndRevertApply();
			alteon.disconnect();
			throw e;
		} catch (Exception e) {
			alteon.cmndRevertApply();
			alteon.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		alteon.disconnect();

//		try
//		{
//			downloadSlbConfig(adcIndex, false);	
//
//			//변경이력 기록
//			vDB.getVServerInfoAlteon(vServerInfo.getIndex()); //기존 상태확인
//			historyManager.writeConfigHistoryAlteon(configChunk);
//
//			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. index:%d, %s %s %s", adcIndex, adcInfo.toString(), vServerInfo.toString(), vServiceList.toString()));
//		}
//		catch(OBException e)
//		{
//			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(), OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName(), db);
//			throw e;
//		}
//		catch(Exception e)
//		{
//			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(), OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName(), db);
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}		
	}

//	public static void main(String[] args)
//	{
//		try
//		{
////			OBAdcAlteonHandler handler = new OBAdcAlteonHandler("192.168.100.13", "admin", "admin", OBDefine.SERVICE.TELNET, OBDefine.SERVICE.TELNET);
////			//handler.enter();
////			handler.login();
////
////			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(19);
//			OBDatabase db = new OBDatabase();
//			db.openDB();
////			System.out.println(new OBAdcVServerAlteon().calProperVrrpIndex(adcInfo, 1, 1, handler, db));
////			handler.disconnect();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	private OBDtoVrrpIndexInfo calProperVrrpIndex(OBDtoAdcInfo adcInfo, Integer vrrp, Integer vrid,
			OBAdcAlteonHandler alteon) throws OBException {
		try {
			// vrrp list을 추출한다.
			DtoOidCfgAdcAdditional oidVrrp = new OBSnmpOidDB().getCfgAdcAdditional(adcInfo.getAdcType(),
					adcInfo.getSwVersion());
			ArrayList<DtoSnmpVrrpAlteon> vrrpList = new OBSnmpAlteon(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
					.getVrrpList(oidVrrp);

			// vrid, vrrp id의 값 중복 검사한다. 이전에 중복된 ID값이 있으면 없는 것으로 할당한다.
			// /info/l2/fdb/dump 의 mac 주소값의 00:00:5e:00:01:##(vrid 16진수 값) 부분이 설정할 값과 중복인 값이
			// 있는지 확인한다.
			String fdbResult = alteon.cmndDumpInfoFdb();
			OBCLIParserAlteon parser = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
			HashMap<String, OBDtoFdbInfo> fdbMap = parser.parseFdbDump(fdbResult);
			OBDtoVrrpIndexInfo retVal = new OBDtoVrrpIndexInfo();

			retVal.setVrid(vrid);
			retVal.setVrrp(vrrp);
			return calProperVrrpIndex(retVal, vrrpList, fdbMap);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
	}

	private OBDtoVrrpIndexInfo calProperVrrpIndex(OBDtoVrrpIndexInfo vrrpIndexInfo,
			ArrayList<DtoSnmpVrrpAlteon> vrrpList, HashMap<String, OBDtoFdbInfo> fdbMap) throws OBException {
		OBDtoVrrpIndexInfo retVal = vrrpIndexInfo;
		try {
			HashMap<Integer, DtoSnmpVrrpAlteon> vrrpMap = new HashMap<Integer, DtoSnmpVrrpAlteon>();
			HashMap<Integer, DtoSnmpVrrpAlteon> vridMap = new HashMap<Integer, DtoSnmpVrrpAlteon>();
			for (DtoSnmpVrrpAlteon vrrpInfo : vrrpList) {
				vrrpMap.put(vrrpInfo.getVrrpIndex(), vrrpInfo);
				vridMap.put(vrrpInfo.getVrrpID(), vrrpInfo);
			}
			for (int i = 0; i < 1024; i++) {// 최대치 1024.
				if (vrrpIndexInfo.getVrrp() > 1024) {
					vrrpIndexInfo.setVrrp(1);
//					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to calc vrrp/vrid index.Exceeded 1204"));
//					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);
				}
				if (vrrpIndexInfo.getVrid() > 1024) {
					vrrpIndexInfo.setVrid(1);
				}
				if (vrrpMap.get(vrrpIndexInfo.getVrrp()) == null) {
					if (vridMap.get(vrrpIndexInfo.getVrid()) == null) {
						String macAddr = String.format("%s:%02x", "00:00:5e:00:01:", vrrpIndexInfo.getVrid());
						if (fdbMap.get(macAddr.toLowerCase()) == null) {
							return retVal;
						} else {// fdb mac 에 등록된 경ㅇ. vrid값을 변경해 주어야 한다. vrid값을 1 증가시켜 준다.
							vrrpIndexInfo.setVrid(vrrpIndexInfo.getVrid() + 1);
						}
					} else {// vrid가 충돌인 경우. vrid값을 1 증가시켜 준다.
						vrrpIndexInfo.setVrid(vrrpIndexInfo.getVrid() + 1);
					}
				} else {// vrrp가 충돌인 경우. vrrp을 증가시키고, vrid 는 vrrp값으로 통일시킨다.
					vrrpIndexInfo.setVrrp(vrrpIndexInfo.getVrrp() + 1);
					vrrpIndexInfo.setVrid(vrrpIndexInfo.getVrrp());// vrrp 와 vrid을 동기화 한다.
				}
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		// 값을 찾지 못한 경우임.
		OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to calc vrrp/vrid index. Exceeded 1204"));
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);
	}

	private Integer getProperInterface(Integer adcIndex, String ipAddress) throws OBException {
		Integer result = 0;
		ArrayList<OBDtoNetworkInterface> ifList = new OBVServerDB().getL3InterfaceAll(adcIndex);
		long vAddr = OBNetwork.getIpConvertToLong(ipAddress);
		for (OBDtoNetworkInterface ifObj : ifList) {
			long mask = OBNetwork.getIpConvertToLong(ifObj.getNetmask());
			long ifAddr = OBNetwork.getIpConvertToLong(ifObj.getIpAddress());
			if ((vAddr & mask) == (ifAddr & mask)) {
				return ifObj.getIfNum();
			}
		}
		return result;
	}
//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDtoAdcVServerAlteon vserver = new OBDtoAdcVServerAlteon();
//			vserver.setAdcIndex(8);
//			vserver.setAlteonId(123);
////			vserver.setApplyTime(0);
//			vserver.setIfNum(1);
//			vserver.setName("vs_group_123");
//			vserver.setRouterIndex(123);
//			vserver.setStatus(1);
//			vserver.setUseYN(1);
//			vserver.setvIP("192.168.1.123");
//			vserver.setVrIndex(123);
//			vserver.setVrrpYN(true);
//			vserver.setIfNum(1);
//			ArrayList<OBDtoAdcVService> vserviceList=new ArrayList<OBDtoAdcVService>();
//			for(int i=0;i<1;i++)
//			{
//				OBDtoAdcVService vsrvObj = new OBDtoAdcVService();
//				OBDtoAdcPoolAlteon pool = new OBDtoAdcPoolAlteon();
//				ArrayList<OBDtoAdcPoolMemberAlteon> memberList = new ArrayList<OBDtoAdcPoolMemberAlteon>();
//
//				OBDtoAdcPoolMemberAlteon obj = new OBDtoAdcPoolMemberAlteon();
//				obj.setAlteonNodeID(101);
//				obj.setIpAddress("101.101.101.1");
//				obj.setPort(0);
//				obj.setState(OBDefine.STATE_ENABLE);
//				memberList.add(obj);
//				
//				obj = new OBDtoAdcPoolMemberAlteon();
//				obj.setAlteonNodeID(123);
//				obj.setIpAddress("10.0.0.123");
//				obj.setPort(0);
//				obj.setState(OBDefine.STATE_ENABLE);
//				memberList.add(obj);
//				
//				pool.setAlteonId(1010);
//				pool.setLbMethod(OBDefine.LB_METHOD_HASH);
//				pool.setHealthCheck(OBDefine.HEALTH_CHECK_ARP);
//				pool.setMemberList(memberList);
//				pool.setName("vs_group_1_Pool_2");
//				vsrvObj.setPool(pool);
//				vsrvObj.setRealPort(23);
//				vsrvObj.setServicePort(23);
//				vserviceList.add(vsrvObj);
//			}
//			
//			vserver.setVserviceList(vserviceList);
//			OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
//			extraInfo.setAccountIndex(1);
//			extraInfo.setClientIPAddress("172.172.2.2");
//			new OBAdcVServerAlteon().addVServerAlteon(vserver, extraInfo);
//		}
//		catch(OBExceptionUnreachable e)
//		{
//			e.printStackTrace();
//		}
//		catch(OBExceptionLogin e)
//		{
//			e.printStackTrace();
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public void addVServerAlteon(OBDtoAdcVServerAlteon virtualServer, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		if (virtualServer == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER);
		}
		if (extraInfo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER);
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. %s, threadID:%d", virtualServer, Thread.currentThread().getId()));
		// adcInfo 정보를 추출한다. active/standby 구성인지 확인한다.
		OBDatabase db = new OBDatabase();
		OBDatabase db2 = new OBDatabase();

		try {
			db.openDB();
			db2.openDB();

			OBVServerDB vDB = new OBVServerDB();

			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(virtualServer.getAdcIndex());

			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						"failed to get adcInfo. adcIndex: " + virtualServer.getAdcIndex());
			}
			if (timeSync(adcInfo) == true) {
				throw new OBException(OBException.ERRCODE_SLB_VS_TIME, "Sync failed. adc_index: " + adcInfo.getIndex());
			}

			new OBAdcManagementImpl().waitUntilAdcAvailable(virtualServer.getAdcIndex());
			try {
				addVServerAlteon(adcInfo.getIndex(), adcInfo, virtualServer, virtualServer.getVserviceList(), extraInfo,
						vDB);
			} catch (OBExceptionUnreachable e) {
				throw e;
			} catch (OBExceptionLogin e) {
				throw e;
			} catch (OBException e) {
				throw e;
			} catch (Exception e) {
				throw e;
			} finally {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServer.getAdcIndex(), db);
			}
			// system info 정보 추출하여 저장한다.
			try {
				OBDtoAdcSystemInfo info = new OBAdcSystemInfoAlteon().getAdcSystemInfo(adcInfo);
				new OBAdcMonitorDB().writeAdcSystemInfo(adcInfo.getIndex(), info);
			} catch (Exception e) {

			}
			new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServer.getAdcIndex(), db);
			// 감사로그.
			if (extraInfo != null && virtualServer != null && adcInfo != null && db != null)
				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), virtualServer.getAdcIndex(),
						extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_ADD_SUCCESS, adcInfo.getName(),
						virtualServer.getName(), null);
		} catch (OBExceptionUnreachable e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE);
		} catch (OBExceptionLogin e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
			if (db2 != null)
				db2.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	/**
	 * node, pool 의 ID를 할당한다.
	 * 
	 * @param adcIndex
	 * @param adcInfo
	 * @param vServerInfo
	 * @param vServiceList
	 * @param db
	 * @param vDB
	 * @throws OBException
	 */
	private ArrayList<OBDtoAdcVService> allocAlteonIDs(Integer adcIndex, OBDtoAdcVServerAlteon vServerInfo,
			ArrayList<OBDtoAdcVService> vsrvList, OBVServerDB vDB) throws OBException {
		// pool ID, node ID를 할당한다.
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, vServerInfo:%s, vsrvList:%s", adcIndex, vServerInfo, vsrvList));

		ArrayList<OBDtoAdcVService> vServiceList = new ArrayList<OBDtoAdcVService>();// (ArrayList<OBDtoAdcVService>)
																						// vsrvList.clone();

		HashMap<String, String> nodeList = vDB.getNodeIDListAlteon(adcIndex);

		try {
			Integer poolID = vDB.allocPoolID(adcIndex);
			Integer nodeID = vDB.allocNodeID(adcIndex);

			for (int i = 0; i < vsrvList.size(); i++) {
				OBDtoAdcVService serviceObj = new OBDtoAdcVService();
				OBDtoAdcPoolAlteon newPoolObj = new OBDtoAdcPoolAlteon();
				ArrayList<OBDtoAdcPoolMemberAlteon> newMemberList = new ArrayList<OBDtoAdcPoolMemberAlteon>();
				serviceObj.setRealPort(vsrvList.get(i).getRealPort());
				serviceObj.setServicePort(vsrvList.get(i).getServicePort());
				serviceObj.setPool(newPoolObj);

				OBDtoAdcPoolAlteon poolObj = vsrvList.get(i).getPool();
				if (poolObj.getAlteonId() != null)
					newPoolObj.setAlteonId(poolObj.getAlteonId());

				if (poolObj.getIndex() != null)
					newPoolObj.setIndex(poolObj.getIndex());

				if (poolObj.getLbMethod() != null)
					newPoolObj.setLbMethod(poolObj.getLbMethod());

				if (poolObj.getHealthCheck() != null) // v29 적용 시점부터 이건 안 쓴다.
					newPoolObj.setHealthCheck(poolObj.getHealthCheck());
				if (poolObj.getHealthCheckV2() != null) {
					newPoolObj.setHealthCheckV2(new OBDtoAdcHealthCheckAlteon(poolObj.getHealthCheckV2()));
				}

				if (poolObj.getName() != null)
					newPoolObj.setName(poolObj.getName());

				newPoolObj.setMemberList(newMemberList);

				if ((poolObj.getAlteonId() == null) || (poolObj.getAlteonId().isEmpty())) {
					// 기존에 등록된 녀석들이 있는지 조사하여 있으면 사용한다.
					OBDtoAdcPoolAlteon poolInfo = vDB.getPoolInfoAlteon(poolObj.getIndex());
					if (poolInfo == null) {// 신규 할당해야 한다.
						while (vDB.isUnusedPoolID(adcIndex, poolID) == false)
							poolID++;
						newPoolObj.setAlteonId(Integer.toString(poolID));
						poolID++;
					} else {
						newPoolObj.setAlteonId(poolInfo.getAlteonId());
					}
				}

				ArrayList<OBDtoAdcPoolMemberAlteon> memberList = poolObj.getMemberList();
				for (int ii = 0; ii < memberList.size(); ii++) {
					OBDtoAdcPoolMemberAlteon newMemObj = new OBDtoAdcPoolMemberAlteon();
					OBDtoAdcPoolMemberAlteon memObj = memberList.get(ii);
					if (memObj.getAlteonNodeID() != null)
						newMemObj.setAlteonNodeID(memObj.getAlteonNodeID());

					if (memObj.getIpAddress() != null)
						newMemObj.setIpAddress(memObj.getIpAddress());

					if (memObj.getPort() != null)
						newMemObj.setPort(memObj.getPort());

					if (memObj.getState() != null)
						newMemObj.setState(memObj.getState());

					if ((memObj.getAlteonNodeID() == null) || (memObj.getAlteonNodeID().isEmpty())) {
						// IP로 기존에 있던 node를 찾아 자동 할당하는 부분이다. Alteon v29에서는 node IP 중복일 수 있기때문에 자동 식별하면
						// 안된다. 웹에서 선택하면 이미 id를 준다.
//						Integer tmpNodeID = nodeList.get(memObj.getIpAddress());
//						if(tmpNodeID!=null && tmpNodeID>0)
//						{
//							newMemObj.setAlteonNodeID(tmpNodeID);		
//						}
//						else
						{ // node id가 없으면 무조건 새로 입력한 node다. 단, 중복 입력이 안 되게 입력시 체크해야 한다.
							while (vDB.isUnusedNodeID(adcIndex, nodeID) == false)
								nodeID++;
							newMemObj.setAlteonNodeID(Integer.toString(nodeID));
							nodeList.put(memObj.getIpAddress(), Integer.toString(nodeID));
							nodeID++;
						}
//						OBDtoAdcNodeAlteon nodeInfo = vDB.getNodeInfoAlteon(adcIndex, memObj.getIpAddress(), db);
//						if(nodeInfo == null)
//						{
////							// 동일한 IP로 등록할 수 없음.
////							if(vDB.getNodeInfo(adcIndex, memObj.getNodeName(), db) != null)
////								throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("alread existed node(%s)", memObj.getNodeName()));
//							while(vDB.isAlivedNodeID(adcIndex, nodeID, db) == false)
//								nodeID++;
////							OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("allocAlteonIDs-after while(vDB.isAlivedNodeID(adcIndex, poolID, db) == false)"));
//							newMemObj.setAlteonNodeID(nodeID);		
//							nodeID++;
//						}
//						else
//						{
//							newMemObj.setAlteonNodeID(nodeInfo.getAlteonId());
//						}
					}
					newMemberList.add(newMemObj);
				}
				vServiceList.add(serviceObj);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", vServiceList));
		return vServiceList;
	}

	@Override
	public boolean checkVrrp(String ipaddress, String accountID, String password, String swVersion, String vsIP,
			Integer routerIndex, Integer vrIndex, Integer ifNum)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		return false;
//		OBAdcAlteon alteon;
//		// 모델에 따라 다른 처리.
//		if(swVersion == null || swVersion.isEmpty())
//		{
//			alteon = new OBAdcAlteon();
//		}
//		else if(swVersion.compareToIgnoreCase("25.3.4.0")==0)
//		{
//			alteon = new OBAdcAlteon250304();
//		}
//		else if(swVersion.compareToIgnoreCase("23.2.9.0")==0)
//		{
//			alteon = new OBAdcAlteon230209();
//		}
//		else if(swVersion.compareToIgnoreCase("28.1.5.0")==0)
//		{
//			alteon = new OBAdcAlteon28010500();
//		}
//		else
//		{
//			alteon = new OBAdcAlteon();
//		}
//		
//		try
//		{
//			alteon.setConnectionInfo(ipaddress, accountID, password);
//			alteon.login();
//			
//			boolean result = alteon.isAvaliableVrrpID(vsIP, vrIndex, routerIndex, ifNum);
//			alteon.disconnect();
//			return result;
//		}
//		catch(OBException e)
//		{
//			alteon.disconnect();
//			throw new OBException(OBException.ERRCODE_SLB_CHECKVRRP, e);
//		}
//		catch(Exception e)
//		{
//			alteon.disconnect();
//			OBException e1 = new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//			throw new OBException(OBException.ERRCODE_SLB_CHECKVRRP, e1);
//		}
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			ArrayList<String> vsIndexList = new ArrayList<String>();
//			vsIndexList.add("29_51");
//			OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
//			extraInfo.setAccountIndex(1);
//			extraInfo.setClientIPAddress("172.172.2.2");
//			new OBAdcVServerAlteon().delVServer(29, vsIndexList, extraInfo);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public void delVServer(Integer adcIndex, ArrayList<String> vsIndexList, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBDatabase db = new OBDatabase();
		OBVServerDB vDB = new OBVServerDB();
		try {
			db.openDB();
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			ArrayList<String> vsNameList = new ArrayList<String>();
			for (String vsIndex : vsIndexList) {
				vsNameList.add(new OBVServerDB().getVServerName(vsIndex));
			}

			new OBAdcManagementImpl().waitUntilAdcAvailable(adcIndex, db);
			try {
				delVServer(adcInfo.getIndex(), vsIndexList, db, vDB, extraInfo);
			} catch (OBExceptionUnreachable e) {
				throw e;
			} catch (OBExceptionLogin e) {
				throw e;
			} catch (OBException e) {
				throw e;
			} catch (Exception e) {
				throw e;
			} finally {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex, db);
			}

			for (String vsName : vsNameList) {
				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcInfo.getIndex(),
						extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_DEL_SUCCESS, adcInfo.getName(),
						vsName, null);
			}

			try {// system info 정보 추출하여 저장한다.
				OBDtoAdcSystemInfo info = new OBAdcSystemInfoAlteon().getAdcSystemInfo(adcInfo);
				new OBAdcMonitorDB().writeAdcSystemInfo(adcInfo.getIndex(), info);
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("%s: %s, callstack:%s", "failed to update a system's time(apply, save..)",
								e.getMessage(), new OBUtility().getStackTrace()));
			}
		} catch (OBExceptionUnreachable e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

//	private OBAdcAlteon getValidAlteon(OBDtoAdcInfo adcInfo) throws OBException
//	{
//		OBAdcAlteon alteon=null;
//		if(adcInfo.getSwVersion() == null || adcInfo.getSwVersion().isEmpty())
//		{
//			alteon = new OBAdcAlteon();
//		}
//		else if(adcInfo.getSwVersion().compareToIgnoreCase("22.0.3.0")==0)
//		{
//			alteon = new OBAdcAlteon22064();
//		}
//		else if(adcInfo.getSwVersion().compareToIgnoreCase("22.0.4.0")==0)
//		{
//			alteon = new OBAdcAlteon22064();
//		}
//		else if(adcInfo.getSwVersion().compareToIgnoreCase("22.0.6.4")==0)
//		{
//			alteon = new OBAdcAlteon22064();
//		}
//		else if(adcInfo.getSwVersion().compareToIgnoreCase("25.3.4.0")==0)
//		{
//			alteon = new OBAdcAlteon250304();
//		}
//		else if(adcInfo.getSwVersion().compareToIgnoreCase("23.2.9.0")==0)
//		{
//			alteon = new OBAdcAlteon230209();
//		}
//		else if(true == adcInfo.getSwVersion().contains("28.1."))
//		{
//			alteon = new OBAdcAlteon28010500();
//		}
//		else if(true == adcInfo.getSwVersion().contains("28.0."))
//		{
//			alteon = new OBAdcAlteon28010500();
//		}
//		else
//		{
//			alteon = new OBAdcAlteon();
//		}
//		return alteon;
//	}

	private void delVServer(Integer adcIndex, ArrayList<String> vsIndexList, OBDatabase db, OBVServerDB vDB,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		// ADC 정보를 DB에서 읽어 들여 장비 접속 정보를 추출한다.
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, vsIndex:%s", adcIndex, vsIndexList));

		OBDtoAdcInfo adcInfo;
		try {
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
		} catch (OBException e1) {
			throw e1;
		}

		// alteon 모델별 설정.
		OBAdcAlteonHandler alteon = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());

		alteon.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
				adcInfo.getConnService(), adcInfo.getConnPort());
		try {
			alteon.login();
		} catch (OBExceptionUnreachable e1) {
			throw e1;
		} catch (OBExceptionLogin e1) {
			throw e1;
		}

		// 변경이력 준비1
		OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
		ArrayList<OBDtoAdcConfigChunkAlteon> configChunkList = new ArrayList<OBDtoAdcConfigChunkAlteon>();

		try {
			for (int i = 0; i < vsIndexList.size(); i++) {
				String vsIndex = vsIndexList.get(i);

				OBDtoAdcVServerAlteon vsInfo;
				vsInfo = vDB.getVServerInfoAlteon(vsIndex);

				if (vsInfo == null) {
					continue;
				}

				// 변경이력 준비2
				configChunkList.add(historyManager.MakeConfigChunkAlteon(vsInfo, OBDefine.CHANGE_BY_USER,
						OBDefine.CHANGE_TYPE_DELETE, OBDefine.CHANGE_OBJECT_VIRTUALSERVER,
						extraInfo.getAccountIndex()));
				// 변경이력 준비 끝

				if (vsInfo.getVrIndex().equals(0) == false)
					alteon.delVrrp(vsInfo.getVrIndex(), adcInfo.getAdcIpAddress());
				alteon.delVirtualServer(vsInfo, adcInfo.getAdcIpAddress());
			}

			alteon.cmndApply();
		} catch (OBException e) {
//			if(adcInfo.getActivePairIndex()!=null && adcInfo.getActivePairIndex().equals(0)==false)
//				cmdRevertApply(adcInfo.getActivePairIndex());

			alteon.cmndRevertApply();
			alteon.disconnect();
			throw e;
		} catch (Exception e) {
//			if(adcInfo.getActivePairIndex()!=null && adcInfo.getActivePairIndex().equals(0)==false)
//				cmdRevertApply(adcInfo.getActivePairIndex());

			alteon.cmndRevertApply();
			alteon.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("failed to get vserver info(index:%d). message:%s", adcIndex, e.getMessage()));
		}

		try {
			downloadSlbConfig(adcIndex, true);

			for (OBDtoAdcConfigChunkAlteon configChunk : configChunkList) {
				historyManager.writeConfigHistoryAlteon(configChunk);
			}

			// save 작업을 위한 스케줄 등록.
			if (true == new OBEnvManagementImpl().isAlteonAutoSave()) {
				alteon.cmndSave();
			}
		} catch (OBException e) {
//			if(adcInfo.getActivePairIndex()!=null && adcInfo.getActivePairIndex().equals(0)==false)
//				cmdRevertApply(adcInfo.getActivePairIndex());
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			alteon.cmndRevert();
			alteon.disconnect();
			throw e;
		} catch (Exception e1) {
//			if(adcInfo.getActivePairIndex()!=null && adcInfo.getActivePairIndex().equals(0)==false)
//				cmdRevertApply(adcInfo.getActivePairIndex());
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			alteon.cmndRevert();
			alteon.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("failed to get vserver info(index:%d). message:%s", adcIndex, e1.getMessage()));
		}

		alteon.disconnect();

//		try
//		{
//			downloadSlbConfig(adcIndex, false);
//		}
//		catch(OBException e1)
//		{
//			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(), OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName(), db);
//			throw e1;
//		}		
//		catch(Exception e1)
//		{
//			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(), OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName(), db);
//			throw new OBExceptionUnreachable(e1.getMessage());
//		}
//		
//		//변경기록
//		for(OBDtoAdcConfigChunkAlteon configChunk: configChunkList)
//		{
//			historyManager.writeConfigHistoryAlteon(configChunk);	
//		}
//		OBSystemLog.debug("[vs del] history done");
//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			ArrayList<String> vsIndexList = new ArrayList<String>();
//			vsIndexList.add("1_100");
//			 OBDtoExtraInfo extraInfo = new  OBDtoExtraInfo();
//			 extraInfo.setAccountIndex(1);
//			 extraInfo.setClientIPAddress("172.172.2.11");
//			new OBAdcVServerAlteon().disableVServer(1, vsIndexList, extraInfo);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public void disableVServer(Integer adcIndex, ArrayList<String> vsIndexList, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d, vsIndexList:%s, threadID:%d",
				adcIndex, vsIndexList.toString(), Thread.currentThread().getId()));
		OBDatabase db = new OBDatabase();
		OBVServerDB vDB = new OBVServerDB();

		try {
			db.openDB();
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			new OBAdcManagementImpl().waitUntilAdcAvailable(adcIndex, db);
			try {
				disableVServer(adcInfo.getIndex(), vsIndexList, db, vDB, extraInfo);
			} catch (OBExceptionUnreachable e) {
				throw e;
			} catch (OBExceptionLogin e) {
				throw e;
			} catch (OBException e) {
				throw e;
			} catch (Exception e) {
				throw e;
			} finally {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex, db);
			}

			for (String vsIndex : vsIndexList) {
				String vsName = new OBVServerDB().getVServerName(vsIndex);
				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcInfo.getIndex(),
						extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_DISABLE_SUCCESS,
						adcInfo.getName(), vsName, null);
			}

			try {// system info 정보 추출하여 저장한다.
				OBDtoAdcSystemInfo info = new OBAdcSystemInfoAlteon().getAdcSystemInfo(adcInfo);
				new OBAdcMonitorDB().writeAdcSystemInfo(adcInfo.getIndex(), info);
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("%s: %s, callstack:%s", "failed to update a system's time(apply, save..)",
								e.getMessage(), new OBUtility().getStackTrace()));
			}
		} catch (OBExceptionUnreachable e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	private void disableVServer(Integer adcIndex, ArrayList<String> vsIndexList, OBDatabase db, OBVServerDB vDB,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		// virtual 서버 상태를 disable 상태로 설정한다.
		// ADC 정보를 DB에서 읽어 들여 장비 접속 정보를 추출한다.
		OBDtoAdcInfo adcInfo;
		try {
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
		} catch (OBException e1) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("failed to get adc info. message:%s", e1.getMessage()));
		}
		if (adcInfo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "invalid adc info");
		}

		// alteon 모델별 설정.
		OBAdcAlteonHandler alteon = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());

		alteon.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
				adcInfo.getConnService(), adcInfo.getConnPort());
		try {
			alteon.login();
		} catch (OBExceptionUnreachable e1) {
			throw new OBExceptionUnreachable(e1.getMessage());
		} catch (OBExceptionLogin e1) {
			throw new OBExceptionLogin(e1.getMessage());
		}

		// 변경이력 준비1
		OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
		ArrayList<OBDtoAdcConfigChunkAlteon> configChunkList = new ArrayList<OBDtoAdcConfigChunkAlteon>();

		try {
			for (int i = 0; i < vsIndexList.size(); i++) {
				String vsIndex = vsIndexList.get(i);

				OBDtoAdcVServerAlteon vsInfo;
				try {
					vsInfo = vDB.getVServerInfoAlteon(vsIndex);
				} catch (Exception e) {
					alteon.disconnect();
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format("failed to get the vserver info(index:%d, vsIndex:%s).", adcIndex, vsIndex));
				}
				if (vsInfo == null) {
					continue;
					// alteon.disconnect();
					// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					// String.format("failed to get the vserver info(index:%d, vsIndex:%s).",
					// adcIndex, vsIndex));
				}

				// 변경이력 준비2
				try {
					vsInfo.setUseYN(OBDefine.STATE_DISABLE); // 이력 전/후 비교를 위해 새 vs가 갖게 될 status를 설정한다. 웹에서는 vsIndex리스트만
																// 주지 newInfo를 주지 않기 때문에 DB에서 읽으면 기대 값을 설정해 줘야 한다.
					configChunkList.add(historyManager.MakeConfigChunkAlteon(vsInfo, OBDefine.CHANGE_BY_USER,
							OBDefine.CHANGE_TYPE_EDIT, OBDefine.CHANGE_OBJECT_VIRTUALSERVER,
							extraInfo.getAccountIndex()));
				} catch (Exception e) {
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format(
							"[vs disable] history prepare error. Old/new difference check fail. (messages = %s)",
							e.getMessage()));
				}
				// 변경이력 준비 끝

				alteon.changeVServerStatus(vsInfo.getAlteonId(), OBDefine.STATE_DISABLE, adcInfo.getAdcIpAddress());
				if (vsInfo.getVrrpState().equals(OBDefine.VRRP_STATE.ENABLE)) {
					alteon.changeVrrpStatus(vsInfo.getRouterIndex(), OBDefine.VRRP_STATE.DISABLE);
				}
			}
			alteon.cmndApply();
		} catch (OBException e) {
			alteon.cmndRevert();
			alteon.disconnect();
			throw e;
		} catch (Exception e) {
			alteon.cmndRevert();
			alteon.disconnect();
			throw new OBException(e.getMessage());
		}

		try {
			downloadSlbConfig(adcIndex, true);
			OBSystemLog.debug("[vs disable] history save start");
			// 변경기록
			for (OBDtoAdcConfigChunkAlteon configChunk : configChunkList) {
				historyManager.writeConfigHistoryAlteon(configChunk);
			}
			OBSystemLog.debug("[vs disable] history save done");

			if (true == new OBEnvManagementImpl().isAlteonAutoSave()) {
				alteon.cmndSave();
			}
		} catch (OBException e1) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			alteon.cmndRevertApply();
			alteon.disconnect();
			throw new OBExceptionLogin(e1.getMessage());
		} catch (Exception e1) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			alteon.cmndRevertApply();
			alteon.disconnect();
			throw new OBExceptionLogin(e1.getMessage());
		}

		alteon.disconnect();
	}

	@Override
	public OBDtoSLBUpdateStatus downloadSlbConfig(Integer adcIndex, boolean isforce)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		String lockFile = OBDefine.DIR_LOCKFILE_SYSLOGD + adcIndex + "_slb.lock";
		OBDtoAdcInfo adcInfo;
		OBDtoSLBUpdateStatus slbUpdateInfo = new OBDtoSLBUpdateStatus();

		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
				String.format("try to download Slb. threadID:%d, adcIndex:%d, isforce:%b",
						Thread.currentThread().getId(), adcIndex, isforce));

		if (OBCommon.isLockFileExist(lockFile) == true) {
			// LockFileExcetion 처리
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
					String.format("download slb config.(already running). threadID:%d, isforce:%b",
							Thread.currentThread().getId(), isforce));
			slbUpdateInfo.setUpdateSuccess(false);
			slbUpdateInfo.setExtraMsg(OBException.getExceptionMessage(OBException.ERRCODE_LOCK_FILE_EXCEPTION));
			slbUpdateInfo.setExtraMsg2(OBException.getExceptionMessage(OBException.ERRCODE_ADCADD_SLB_LOCK_EXCEPTION));
			return slbUpdateInfo;
		}
		try {
			// lock file를 생성한다.
			// lock file은 ADC 단위로 생성한다. adcIndex_slb.lock 형태로 지정한다.
			OBCommon.createLockFile(lockFile);
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			Timestamp deviceTime = null;
			// 장비의 applyTime 을 추출한다.
			// 모니터링 모드일 경우에는 applyTime 값이 현재 시간으로 설정되도록 한다.

			if (isforce == true) {
				slbUpdateInfo = downloadCheckSlb(adcInfo, null, slbUpdateInfo);
				return slbUpdateInfo;
			}
			deviceTime = new OBAdcVServerAlteon().getDeviceApplyTime(adcInfo);

			Timestamp localTime = new OBAdcManagementImpl().getApplyTimeFromDB(adcIndex);

			// 로컬에 저장된 값과 장비에서 추출된 applyTime 값을 비교한다.
			// 만역 값이 동일하면 SLB 업데이트를 하지 않는다. lock file을 삭제한 후 작업을 종료한다.
			if ((deviceTime != null) && (deviceTime.equals(localTime) == true)) {
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String
						.format("The most recent SLB-date information. adcIPAddress:%s", adcInfo.getAdcIpAddress()));
				slbUpdateInfo.setExtraKey(OBDefine.RECENT_SLB);
				slbUpdateInfo.setUpdateSuccess(false);
				return slbUpdateInfo;
			} else {
				slbUpdateInfo = downloadCheckSlb(adcInfo, deviceTime, slbUpdateInfo);
			}

			slbUpdateInfo = downloadCheckSlb(adcInfo, deviceTime, slbUpdateInfo);

			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
					String.format("end. threadID:%d, adcIndex:%d", Thread.currentThread().getId(), adcIndex));
			return slbUpdateInfo;
		} catch (OBExceptionUnreachable e) {
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to download slb config. OBExceptionUnreachable, adcIndex:%d", adcIndex));
			throw new OBExceptionUnreachable(OBException.ERRCODE_SYSTEM_UNREACHABLE);
		} catch (OBExceptionLogin e) {
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to download slb config. OBExceptionLogin, adcIndex:%d", adcIndex));
			throw new OBExceptionLogin(OBException.ERRCODE_SYSTEM_LOGIN);
		} catch (OBException e) {
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to download slb config. OBException, adcIndex:%d", adcIndex));
			throw e;
		} catch (Exception e) {
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to download slb config. Exception, adcIndex:%d", adcIndex));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			OBCommon.deleteLockFile(lockFile);
		}
	}

	private OBDtoSLBUpdateStatus downloadCheckSlb(OBDtoAdcInfo adcInfo, Timestamp deviceTime,
			OBDtoSLBUpdateStatus slbUpdateInfo) throws OBException, OBExceptionUnreachable, OBExceptionLogin {
		boolean bDownload = false;
		if (adcInfo.getOpMode() != OBDefine.OP_MODE_MONITORING) {
			int i = 0;
			do {
				bDownload = downloadSnmpSlb(adcInfo, deviceTime);
				Timestamp currentApplyTime;
				currentApplyTime = new OBAdcVServerAlteon().getDeviceApplyTime(adcInfo);

				if (deviceTime == null || ((deviceTime != null) && (deviceTime.equals(currentApplyTime) == true))) {
					deviceTime = currentApplyTime;
					break;
				}

				if (i == 2) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to download slb(snmp) 3times."));
					slbUpdateInfo.setUpdateSuccess(false);
					return slbUpdateInfo;
				}
				i++;
			} while (i < 3);
		} else
			bDownload = downloadSnmpSlb(adcInfo, deviceTime);

		// applyTime을 로컬에 저장한다.
		new OBAdcMonitorDB().updateAdcApplyTime(adcInfo.getIndex(), deviceTime);

		slbUpdateInfo.setUpdateSuccess(bDownload);
		return slbUpdateInfo;
	}

	public boolean downloadSnmpSlb(OBDtoAdcInfo adcInfo, Timestamp applyTime) throws OBException {
		boolean bDownload = false;

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, applyTime:%s", adcInfo.getIndex(), applyTime));

		long tt0 = 0, tt1 = 0, tt2 = 0, tt3 = 0, tt4 = 0;
		tt0 = System.currentTimeMillis();

		OBVServerDB vDB = new OBVServerDB();

		OBDtoAdcConfigSlbAlteon config;
		OBDatabase db = new OBDatabase();
		try {
			StringBuilder transactionQuery = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);

			transactionQuery.append(" BEGIN; ");

			config = OBCommon
					.getValidSnmpAlteonHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
					.downloadSlbConf(adcInfo.getIndex(), applyTime, adcInfo.getAdcType(), adcInfo.getSwVersion()); // real,
																													// group,
																													// health,
																													// vservice,
																													// vserver
																													// 구하기
			tt1 = System.currentTimeMillis();
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "<time> download slb: " + (tt1 - tt0) / 1000.0);

			// 기존 db 삭제.
			vDB.delVServerAll(adcInfo.getIndex(), transactionQuery);
			vDB.delNodeAll(adcInfo.getIndex(), transactionQuery);
			vDB.delPoolAll(adcInfo.getIndex(), transactionQuery);
			vDB.delVServiceAll(adcInfo.getIndex(), transactionQuery);
			vDB.delPoolmemberAll(adcInfo.getIndex(), transactionQuery);
			vDB.delL3InterfaceAll(adcInfo.getIndex(), transactionQuery);
			vDB.delHealthCheckAll(adcInfo.getIndex(), transactionQuery);
			vDB.delFilterAndPortMappingAll(adcInfo.getIndex(), transactionQuery);

			tt2 = System.currentTimeMillis();
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "<time> delete db: " + (tt2 - tt1) / 1000.0);

			// 아래 작업은 순서를 지켜야 한다. 각 테이블간의 relation 이 존재.
			// node 정보 추가
			ArrayList<OBDtoAdcNodeAlteon> nodeList = config.getRealServers();
			vDB.addNodeInfoAlteon(adcInfo.getIndex(), nodeList, transactionQuery);

			// l3 interface 정보 추가.
			ArrayList<OBDtoNetworkInterface> ifList = config.getIfList();
			vDB.addL3InterfaceInfo(adcInfo.getIndex(), ifList, transactionQuery);

			ArrayList<OBDtoAdcPoolAlteon> poolList = config.getServerPools();
			ArrayList<OBDtoAdcVServerAlteon> vsList = config.getVirtServers();

			// healthcheck 정보 추가
			vDB.addHealthCheckAlteon(adcInfo.getIndex(), config.getHealthcheckList(), transactionQuery);

			// pool, virtual service, virtual server를 추가한다.
			// object별로 나누지 않고 한번에 하는 이유 :
			// 1. 이 3개 구성요소는 추가 순서를 지켜서 해야 한다. 작업이 서로 의존적이다. pool->virtual service->virtual
			// server
			// 2. 성능을 높이려면 insert의 value를 모아서 query를 한번만 실행해야 한다.그러려면 query를 미리 구성해야 하고,
			// object별로 작업을 나누기 어렵다.
			vDB.addPoolVServerVServiceAlteon(adcInfo.getIndex(), poolList, vsList, transactionQuery);

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end of adding vs.cnt:%d", vsList.size()));
			// pool member를 추가한다. insert를 한번만 하도록 구성되어 있다.
			vDB.addPoolmemberInfoAlteon(adcInfo.getIndex(), poolList, transactionQuery);

			// filter를 추가한다.
			// physical port - filter 정보 mapping을 저장한다.
			vDB.addFilterAndPortMappingAlteon(adcInfo.getIndex(), config.getFilterList(),
					config.getFilterPhysicalPortList(), transactionQuery);
			vDB.updateFlbMonitoringGroup(adcInfo.getIndex(), transactionQuery); // 그룹이 지워졌을 수 있는데, flb 모니터링 그룹에도 확인해서
																				// 지운다.

			// adcAdditional Info 추가.
			vDB.updateAdcAdditionalInfo(config.getAdcAdditionalInfo(), transactionQuery);

			// peer ip에 대한 active pair 인덱스 추가.
//				if(config.getAdcAdditionalInfo().getSyncState()==OBDefine.STATE_ENABLE)
//					vDB.updateAdcPairIndex(adcInfo, config.getAdcAdditionalInfo().getPeerIP(), db);
//				else
//					vDB.updateAdcPairIndex(adcInfo, "", db);
			transactionQuery.append(" COMMIT; ");

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, transactionQuery.toString());

			db.openDB();
			db.executeUpdate(transactionQuery.toString());

			tt3 = System.currentTimeMillis();
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "<time> write slb: " + (tt3 - tt2) / 1000.0);

			// status update
			ArrayList<OBDtoAdcVServerAlteon> list = new OBAdcMonitorAlteon().getSlbStatusAlteon(adcInfo.getIndex());
			new OBAdcMonitorDB().writeSlbStatusAlteon(adcInfo.getIndex(), list);

			bDownload = true;

			tt4 = System.currentTimeMillis();
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "<time> refresh status: " + (tt4 - tt3) / 1000.0);

		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		return bDownload;
	}

//	private boolean timeSync(Integer adcIndex, OBDatabase db)
//			throws OBExceptionUnreachable, OBExceptionLogin, OBException
//	{
//		Timestamp localTime = null;
//		Timestamp deviceTime = null;
//		OBDtoAdcInfo adcInfo;
//		boolean bDownload = false;
//		try
//		{
//			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
//			localTime = getApplyTimeFromDB(adcIndex, db);  //DB의 설정 시각 확인
//			if(new OBTelnetCmndExecV2().isReachable(adcInfo.getAdcIpAddress(), adcInfo.getConnPort())==false)
//				throw new OBExceptionUnreachable("unreachable");
//			
//			deviceTime = getDeviceApplyTime(adcIndex);     //장비의 설정 시각 확인, 장비 연결 테스트도 겸함. 여기서 연결 실패하면 OBExceptionLogin 날림.
//		
//			if(false==OBCommon.checkVersionAlteon(adcInfo.getSwVersion()))
//			{
//				throw new OBException(OBException.ERRCODE_ADC_VERSION);
//			}
//			
//			if((deviceTime != null) && (deviceTime.equals(localTime)==false))    //로컬시간과 장비의 시간이 다르면 true 반환
//			{
//				bDownload = true;
//			}
//			return bDownload;
//		}
//		catch(OBExceptionUnreachable e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
//		}
//		catch(OBExceptionLogin e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
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

	private boolean timeSync(OBDtoAdcInfo adcInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		Timestamp localTime = null;
		Timestamp deviceTime = null;
		boolean bDownload = false;
		try {
			localTime = new OBAdcManagementImpl().getApplyTimeFromDB(adcInfo.getIndex()); // DB의 설정 시각 확인

			if (new OBTelnetCmndExecV2().isReachable(adcInfo.getAdcIpAddress(), adcInfo.getConnPort()) == false)
				throw new OBExceptionUnreachable("unreachable");

			deviceTime = getDeviceApplyTime(adcInfo); // 장비의 설정 시각 확인, 장비 연결 테스트도 겸함. 여기서 연결 실패하면 OBExceptionLogin 날림.

			if (false == OBCommon.checkVersionAlteon(adcInfo.getSwVersion())) {
				throw new OBException(OBException.ERRCODE_ADC_VERSION);
			}
			// localTime에 null값이 존재 할 경우 바로 다운로드를 시작한다.
			if (localTime == null) {
				return bDownload;
			}

			if ((deviceTime != null) && (deviceTime.equals(localTime) == false)) // 로컬시간과 장비의 시간이 다르면 true 반환
			{
				bDownload = true;
			}
			return bDownload;
		} catch (OBExceptionUnreachable e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public void enableVServer(Integer adcIndex, ArrayList<String> vsIndexList, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d, vsIndexList:%s, threadID:%d",
				adcIndex, vsIndexList.toString(), Thread.currentThread().getId()));

		OBDatabase db = new OBDatabase();
		OBVServerDB vDB = new OBVServerDB();

		try {
			db.openDB();

			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			new OBAdcManagementImpl().waitUntilAdcAvailable(adcIndex, db);
			try {
				enableVServer(adcInfo.getIndex(), vsIndexList, db, vDB, extraInfo);
			} catch (OBExceptionUnreachable e) {
				throw e;
			} catch (OBExceptionLogin e) {
				throw e;
			} catch (OBException e) {
				throw e;
			} catch (Exception e) {
				throw e;
			} finally {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex, db);
			}

			for (String vsIndex : vsIndexList) {
				String vsName = new OBVServerDB().getVServerName(vsIndex);
				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcInfo.getIndex(),
						extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_ENABLE_SUCCESS,
						adcInfo.getName(), vsName, null);
			}

			try {// system info 정보 추출하여 저장한다.
				OBDtoAdcSystemInfo info = new OBAdcSystemInfoAlteon().getAdcSystemInfo(adcInfo);
				new OBAdcMonitorDB().writeAdcSystemInfo(adcInfo.getIndex(), info);
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("%s: %s, callstack:%s", "failed to update a system's time(apply, save..)",
								e.getMessage(), new OBUtility().getStackTrace()));
			}
		} catch (OBExceptionUnreachable e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	private void enableVServer(Integer adcIndex, ArrayList<String> vsIndexList, OBDatabase db, OBVServerDB vDB,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		// ADC 정보를 DB에서 읽어 들여 장비 접속 정보를 추출한다.
		OBDtoAdcInfo adcInfo;
		try {
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
		} catch (OBException e1) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("failed to get adc info. message:%s", e1.getMessage()));
		}
		if (adcInfo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "invalid adc info");
		}

		// alteon 모델별 설정.
		OBAdcAlteonHandler alteon = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());

		alteon.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
				adcInfo.getConnService(), adcInfo.getConnPort());
		try {
			alteon.login();
		} catch (OBExceptionUnreachable e1) {
			throw new OBExceptionUnreachable(e1.getMessage());
		} catch (OBExceptionLogin e1) {
			throw new OBExceptionLogin(e1.getMessage());
		}

		// 변경이력 준비1
		OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
		ArrayList<OBDtoAdcConfigChunkAlteon> configChunkList = new ArrayList<OBDtoAdcConfigChunkAlteon>();

		try {
			for (int i = 0; i < vsIndexList.size(); i++) {
				String vsInteger = vsIndexList.get(i);

				OBDtoAdcVServerAlteon vsInfo = vDB.getVServerInfoAlteon(vsInteger);
				if (vsInfo == null) {
					OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to get vsInfo(index:%s)", vsInteger));
					continue;
				}
				// 변경이력 준비2
				vsInfo.setUseYN(OBDefine.STATE_ENABLE); // 이력 전/후 비교를 위해 새 vs가 갖게 될 status를 설정한다. 웹에서는 vsIndex리스트만 주지
														// newInfo를 주지 않기 때문에 DB에서 읽으면 기대 값을 설정해 줘야 한다.
				configChunkList.add(historyManager.MakeConfigChunkAlteon(vsInfo, OBDefine.CHANGE_BY_USER,
						OBDefine.CHANGE_TYPE_EDIT, OBDefine.CHANGE_OBJECT_VIRTUALSERVER, extraInfo.getAccountIndex()));
				// 변경이력 준비 끝
				alteon.changeVServerStatus(vsInfo.getAlteonId(), OBDefine.STATE_ENABLE, adcInfo.getAdcIpAddress());
				if (vsInfo.getVrrpState().equals(OBDefine.VRRP_STATE.DISABLE)) {
					alteon.changeVrrpStatus(vsInfo.getRouterIndex(), OBDefine.VRRP_STATE.ENABLE);
				}
			}
			alteon.cmndApply();
		} catch (OBException e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			alteon.cmndRevert();
			alteon.disconnect();
			throw new OBException(e.getMessage());
		} catch (Exception e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			alteon.cmndRevert();
			alteon.disconnect();
			throw new OBException(e.getMessage());
		}

//		try
//		{
//			alteon.cmndCRLF();
//		}
//		catch(OBException e)
//		{
//			if(adcInfo.getActivePairIndex()!=null && adcInfo.getActivePairIndex().equals(0)==false)
//				cmdRevertApply(adcInfo.getActivePairIndex());
//			
//			alteon.cmndRevert();
//			alteon.disconnect();
//			throw new OBException(e.getMessage());
//		}
//		
		try {
			downloadSlbConfig(adcIndex, true);
			// 변경기록
			for (OBDtoAdcConfigChunkAlteon configChunk : configChunkList) {
				historyManager.writeConfigHistoryAlteon(configChunk);
			}

			if (true == new OBEnvManagementImpl().isAlteonAutoSave()) {
				alteon.cmndSave();
			}
		} catch (OBException e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			alteon.cmndRevertApply();
			alteon.disconnect();
			throw e;
		} catch (Exception e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			alteon.cmndRevertApply();
			alteon.disconnect();
			throw new OBExceptionLogin(e.getMessage());
		}

		alteon.disconnect();

//		try
//		{
//			downloadSlbConfig(adcIndex, false);	
//		}
//		catch(Exception e)
//		{
//			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(), OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName(), db);
//			throw new OBExceptionLogin(e.getMessage());
//		}
//		OBSystemLog.debug("[vs enable] history save start");
//		//변경기록
//		for(OBDtoAdcConfigChunkAlteon configChunk: configChunkList)
//		{
//			historyManager.writeConfigHistoryAlteon(configChunk);	
//		}
//		OBSystemLog.debug("[vs enable] history save done");
	}

	private void setState(Integer adcIndex, ArrayList<OBDtoAdcNodeF5> nodeList, Integer action,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		// ADC 정보를 DB에서 읽어 들여 장비 접속 정보를 추출한다.
		OBDtoAdcInfo adcInfo;
		try {
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
		} catch (OBException e1) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("failed to get adc info. message:%s", e1.getMessage()));
		}
		if (adcInfo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "invalid adc info");
		}

		// alteon 모델별 설정.
		OBAdcAlteonHandler alteon = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());

		alteon.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
				adcInfo.getConnService(), adcInfo.getConnPort());
		try {
			alteon.login();
		} catch (OBExceptionUnreachable e1) {
			throw new OBExceptionUnreachable(e1.getMessage());
		} catch (OBExceptionLogin e1) {
			throw new OBExceptionLogin(e1.getMessage());
		}

		// 변경이력 준비1
		OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
		ArrayList<OBDtoAdcConfigChunkAlteon> configChunkList = new ArrayList<OBDtoAdcConfigChunkAlteon>();

		try {
			for (int i = 0; i < nodeList.size(); i++) {
				String realAlteonId = nodeList.get(i).getAlteonID();

				// 변경이력 준비2
//	                vsInfo.setUseYN(OBDefine.STATE_ENABLE); //이력 전/후 비교를 위해 새 vs가 갖게 될 status를 설정한다. 웹에서는 vsIndex리스트만 주지 newInfo를 주지 않기 때문에 DB에서 읽으면 기대 값을 설정해 줘야 한다.
//	                configChunkList.add(
//	                        historyManager.MakeConfigChunkAlteon(
//	                        vsInfo, 
//	                        OBDefine.CHANGE_BY_USER, 
//	                        OBDefine.CHANGE_TYPE_EDIT, 
//	                        OBDefine.CHANGE_OBJECT_VIRTUALSERVER, 
//	                        extraInfo.getAccountIndex())
//	                ); 
//	                //변경이력 준비 끝
				alteon.changeRealServerStatus(realAlteonId, action, adcInfo.getAdcIpAddress());
//	                if(vsInfo.getVrrpState().equals(OBDefine.VRRP_STATE.DISABLE))
//	                {
//	                    alteon.changeVrrpStatus(vsInfo.getRouterIndex(), OBDefine.VRRP_STATE.ENABLE);
//	                }
			}
			alteon.cmndApply();
		} catch (OBException e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			alteon.cmndRevert();
			alteon.disconnect();
			throw new OBException(e.getMessage());
		} catch (Exception e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			alteon.cmndRevert();
			alteon.disconnect();
			throw new OBException(e.getMessage());
		}

		try {
			downloadSlbConfig(adcIndex, true);
			// 변경기록
			for (OBDtoAdcConfigChunkAlteon configChunk : configChunkList) {
				historyManager.writeConfigHistoryAlteon(configChunk);
			}

			if (true == new OBEnvManagementImpl().isAlteonAutoSave()) {
				alteon.cmndSave();
			}
		} catch (OBException e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			alteon.cmndRevertApply();
			alteon.disconnect();
			throw e;
		} catch (Exception e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			alteon.cmndRevertApply();
			alteon.disconnect();
			throw new OBExceptionLogin(e.getMessage());
		}

		alteon.disconnect();

	}

	/**
	 * DB에 저장되어 있는 applyTime을 추출하여 제공한다. DB에 저장되어 있지 않을 경우에는 null을 리턴한다.
	 * 
	 * @param adcIndex -- 접속하고자하는 장비 정보.
	 * @param db       -- 디비 인스턴
	 * @return Timestamp
	 */
//	private Timestamp getApplyTimeFromDB(int adcIndex, OBDatabase db)
//	{
//		String sqlText;
//		sqlText = String.format(" SELECT APPLY_TIME               \n" +
//								" FROM MNG_ADC                    \n" +
//								" WHERE INDEX=%d AND AVAILABLE=%d \n" +
//								" ORDER BY APPLY_TIME DESC        \n" +
//								" LIMIT 1;                          ",
//								adcIndex,
//								OBDefine.STATUS_AVAILABLE);
//		ResultSet rs;
//		try
//		{
//			rs = db.executeQuery(sqlText);
//			if(rs.next() == false)
//			{
//				return null;
//			}
//			return db.getTimestamp(rs, "APPLY_TIME");
//		}
//		catch(Exception e)
//		{
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("DB open fails(%s)", e.getMessage()));
//			return null;
//		}
//	}
//	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할 예정이다.
//	public Timestamp getApplyTimeFromDB(int adcIndex) throws OBException
//	{
//		String sqlText;
//		sqlText = String.format(" SELECT APPLY_TIME               \n" +
//								" FROM MNG_ADC                    \n" +
//								" WHERE INDEX=%d AND AVAILABLE=%d \n" +
//								" ORDER BY APPLY_TIME DESC        \n" +
//								" LIMIT 1;                          ",
//								adcIndex,
//								OBDefine.STATUS_AVAILABLE);
//		ResultSet rs;
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			db.openDB();
//			
//			rs = db.executeQuery(sqlText);
//			if(rs.next() == false)
//			{
//				return null;
//			}
//			return db.getTimestamp(rs, "APPLY_TIME");
//		}
//		catch(SQLException e)
//		{
//			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sqlText));
//		}
//		catch(Exception e)
//		{
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("DB open fails(%s)", e.getMessage()));
//			return null;
//		}
//		finally
//		{
//			if(db!=null) db.closeDB();
//		}
//	}

	private OBDtoAdcVServiceChanged makeNewVServiceChanged(OBDtoAdcVService vsrvInfo) {
		OBDtoAdcVServiceChanged result = new OBDtoAdcVServiceChanged();
		OBDtoAdcPoolAlteonChanged newPool = new OBDtoAdcPoolAlteonChanged();
		OBDtoAdcPoolAlteon pool = vsrvInfo.getPool();
		if (pool != null) {
			newPool.setAlteonId(pool.getAlteonId());
			newPool.setHealthCheck(pool.getHealthCheck());
			newPool.setHealthCheckV2(pool.getHealthCheckV2().getId()); // id만 식별할 수 있으면 된다.
			newPool.setHealthCheckChanged(false);
			newPool.setLbMethod(pool.getLbMethod());
			newPool.setLbMethodChanged(false);
			newPool.setName(pool.getName());
			newPool.setNameChanged(false);
			ArrayList<OBDtoAdcPoolMemberAlteon> memberList = pool.getMemberList();
			ArrayList<OBDtoAdcPoolMemberAlteonChanged> newMemberList = new ArrayList<OBDtoAdcPoolMemberAlteonChanged>();
			for (OBDtoAdcPoolMemberAlteon member : memberList) {
				OBDtoAdcPoolMemberAlteonChanged newMember = new OBDtoAdcPoolMemberAlteonChanged();
				newMember.setAlteonNodeID(member.getAlteonNodeID());
				newMember.setIpAddress(member.getIpAddress());
				newMember.setIpAddressChanged(false);
				newMember.setState(member.getState());
				newMember.setStateChanged(false);
				newMemberList.add(newMember);
			}
			newPool.setMemberList(newMemberList);
		}
		result.setPool(newPool);
		result.setPoolChanged(false);
		result.setRealPort(vsrvInfo.getRealPort());
		result.setRealPortChanged(false);
		result.setServicePort(vsrvInfo.getServicePort());
		return result;
	}

	private ArrayList<OBDtoAdcVServiceChanged> getChangedServiceListV2(Integer adcIndex,
			OBDtoAdcVServerAlteon newVSInfo, OBDtoAdcVServerAlteon oldVSInfo, OBVServerDB vDB) throws OBException {// 변경된
																													// 항목만
																													// 추출한다.
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, newVSInfo:%s, oldVSInfo:%s", adcIndex, newVSInfo, oldVSInfo));
		ArrayList<OBDtoAdcVServiceChanged> result = new ArrayList<OBDtoAdcVServiceChanged>();

		ArrayList<OBDtoAdcVService> newVSrvList = newVSInfo.getVserviceList();
		ArrayList<OBDtoAdcVService> oldVSrvList = oldVSInfo.getVserviceList();
		try {
			for (OBDtoAdcVService newVSrv : newVSrvList) {
				OBDtoAdcVServiceChanged newChanged = makeNewVServiceChanged(newVSrv);
				OBDtoAdcPoolAlteon newPool = newVSrv.getPool();
				if (newPool != null) {
					if (newPool.getAlteonId() == null || newPool.getAlteonId().equals(0)) {
						String alteonId = groupMap.get(newPool.getName());
						if (alteonId == null)
							throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to get pool id");
						newPool.setAlteonId(alteonId);
						newChanged.getPool().setAlteonId(alteonId);
					}
				}

				for (OBDtoAdcVService oldVSrv : oldVSrvList) {
					boolean isChanged = false;

					if (newVSrv.getServicePort().equals(oldVSrv.getServicePort())) {
						// real port 변경 감지.
						if (newVSrv.getRealPort().equals(oldVSrv.getRealPort()) == false) {
							newChanged.setRealPort(newVSrv.getRealPort().intValue());
							newChanged.setRealPortChanged(true);
							isChanged = true;
						}
					} else // if(newVSrv.getServicePort().equals(oldVSrv.getServicePort())==false)
					{
						continue;
					}

					// pool 변경감지.
					OBDtoAdcPoolAlteon oldPool = oldVSrv.getPool();
					if (newPool != null && oldPool != null) {
						if (newPool.getAlteonId().equals(oldPool.getAlteonId()) == false) {
							newChanged.setPoolChanged(true);
							isChanged = true;
						} else {
							if (new OBAdcConfigHistoryImpl().isHealthcheckAlteonChanged(newPool.getHealthCheckV2(),
									oldPool.getHealthCheckV2()) == true) {
								newChanged.getPool().setHealthCheck(newPool.getHealthCheck());
								newChanged.getPool().setHealthCheckV2(newPool.getHealthCheckV2().getId());
								newChanged.getPool().setHealthCheckChanged(true);
								isChanged = true;
							}
							if (newPool.getLbMethod().equals(oldPool.getLbMethod()) == false) {
								newChanged.getPool().setLbMethod(newPool.getLbMethod());
								newChanged.getPool().setLbMethodChanged(true);
								isChanged = true;
							}

							// pool 이름 변경 감지.
							if (newPool.getName() == null || newPool.getName().isEmpty()) {
								if (oldPool.getName() != null && !oldPool.getName().isEmpty()) {
									newChanged.getPool().setName(newPool.getName());
									newChanged.getPool().setNameChanged(true);
									isChanged = true;
								}
							} else if (newPool.getName().equals(oldPool.getName()) == false) {
								newChanged.getPool().setName(newPool.getName());
								newChanged.getPool().setNameChanged(true);
								isChanged = true;
							}

							if (!newPool.getName().isEmpty()) {
								if (newPool.getName().compareToIgnoreCase(oldPool.getName()) != 0) {
									newChanged.getPool().setName(newPool.getName());
									newChanged.getPool().setNameChanged(true);
									isChanged = true;
								}
							}
							// pool member 변경 사항 점검.
							ArrayList<OBDtoAdcPoolMemberAlteon> newMemberList = newPool.getMemberList();
							ArrayList<OBDtoAdcPoolMemberAlteon> oldMemberList = oldPool.getMemberList();
							for (int i = 0; i < newMemberList.size(); i++) {
								OBDtoAdcPoolMemberAlteon newMember = newMemberList.get(i);

								if (newMember.getAlteonNodeID() == null || newMember.getAlteonNodeID().equals(0)) {
									String alteonId = realServerMap.get(newMember.getIpAddress());
									if (alteonId == null)
										throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
												"failed to get pool id");
									newMember.setAlteonNodeID(alteonId);

									ArrayList<OBDtoAdcPoolMemberAlteonChanged> list = newChanged.getPool()
											.getMemberList();
									list.get(i).setState(newMember.getState().intValue());
									list.get(i).setStateChanged(true);
									list.get(i).setAlteonNodeID(alteonId);
									isChanged = true;
									continue;
								}
								for (OBDtoAdcPoolMemberAlteon oldMember : oldMemberList) {
									if (newMember.getAlteonNodeID().equals(oldMember.getAlteonNodeID())) {
										if (newMember.getState().equals(oldMember.getState()) == false) {
											ArrayList<OBDtoAdcPoolMemberAlteonChanged> list = newChanged.getPool()
													.getMemberList();
											list.get(i).setState(newMember.getState().intValue());
											list.get(i).setStateChanged(true);
											isChanged = true;
										}
									}
								}
							}
						}

						if (isChanged == true)
							result.add(newChanged);
					}
				}
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	/*
	 * // virtual server 수정과정에서 vrrp변경을 처리하던 함수인데, active/standby 처리 방식을 바꾸면서
	 * virtual server 수정에서 vrrp 처리를 하지 않게 되어 쓰이지 않는다. 2012.12.5 ykkim. private
	 * OBDtoVRRP getDelVrrpInfo(String vsIndex, OBDtoVRRP currInfo, OBDatabase db,
	 * OBVServerDB vDB) throws OBException { OBDtoVRRP obj; try { obj =
	 * vDB.getVrrpID(vsIndex, db); } catch(Exception e) { throw new
	 * OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
	 * String.format("failed to get vrrp id(vsIndex:%s). message:%s", vsIndex,
	 * e.getMessage())); } if(obj.getVrIndex() == 0) return null;
	 * if(currInfo.getVrIndex() == 0)// 설정된 놈이 삭제된 경우에 해당. return obj;
	 * 
	 * if(obj.getIfNum() != currInfo.getIfNum()) return obj; if(obj.getRouterIndex()
	 * != currInfo.getRouterIndex()) return obj; if(obj.getVrIndex() !=
	 * currInfo.getVrIndex()) return obj; return null; }
	 */

	public Timestamp getDeviceApplyTime(OBDtoAdcInfo adcInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		if (adcInfo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("invalid adc index:%d", adcInfo));
		}

		if (adcInfo.getOpMode() == OBDefine.OP_MODE_MONITORING)
			return OBDateTime.toTimestamp(OBDateTime.now());

		OBAdcAlteonHandler alteon = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());

		alteon.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
				adcInfo.getConnService(), adcInfo.getConnPort());

		try {
			alteon.login();
		} catch (OBExceptionUnreachable e) {
			throw new OBExceptionUnreachable(e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBExceptionLogin(e.getMessage());
		}

		Timestamp deviceTime;
		try {
			deviceTime = alteon.getApplytime();
		} catch (Exception e) {
			alteon.disconnect();
			throw new OBExceptionLogin(e.getMessage());
		}

		alteon.disconnect();
		return deviceTime;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(1);
//			System.out.println(new OBAdcVServerAlteon().getSystemTime(adcInfo));
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	public OBDtoAdcTimeAlteon getSystemTime(OBDtoAdcInfo adcInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcInfo:%s", adcInfo));
		OBAdcAlteonHandler alteon = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());

		alteon.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
				adcInfo.getConnService(), adcInfo.getConnPort());

		try {
			alteon.login();
		} catch (OBExceptionUnreachable e) {
			throw new OBExceptionUnreachable(e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBExceptionLogin(e.getMessage());
		}
		OBDtoAdcTimeAlteon deviceTime;
		try {
			deviceTime = alteon.getSystemTimeInfo();
		} catch (Exception e) {
			throw new OBExceptionLogin(e.getMessage());
		} finally {
			alteon.disconnect();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("result:%s", deviceTime));
		return deviceTime;
	}

	@Override
	public ArrayList<OBDtoNetworkInterface> getL3InterfaceList(Integer adcIndex) throws OBException {
		try {
			return new OBVServerDB().getL3InterfaceAll(adcIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private OBDtoAdcPoolAlteon makeNewPool(String alteonId, OBDtoAdcPoolAlteon pool) throws OBException {
		OBDtoAdcPoolAlteon newPool = new OBDtoAdcPoolAlteon();

		newPool.setAlteonId(alteonId);
		newPool.setHealthCheck(pool.getHealthCheck());
		newPool.setHealthCheckV2(new OBDtoAdcHealthCheckAlteon(pool.getHealthCheckV2()));
		newPool.setIndex(pool.getIndex());
		newPool.setLbMethod(pool.getLbMethod());
		newPool.setName(pool.getName());
		ArrayList<OBDtoAdcPoolMemberAlteon> newMemberList = new ArrayList<OBDtoAdcPoolMemberAlteon>();
		ArrayList<OBDtoAdcPoolMemberAlteon> memList = pool.getMemberList();
		for (OBDtoAdcPoolMemberAlteon member : memList) {
			OBDtoAdcPoolMemberAlteon newMember = new OBDtoAdcPoolMemberAlteon();
			if (member.getAlteonNodeID() == null || member.getAlteonNodeID().equals(0)) {
//				Integer alteonNodeID = realServerMap.get(member.getIpAddress());
//				if(alteonNodeID==null)
//					throw new OBException("failed to get real server id");
//				newMember.setAlteonNodeID(alteonNodeID);
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "real server id error. You must not be here!!! ");
			} else
				newMember.setAlteonNodeID(member.getAlteonNodeID());
			newMember.setIndex(member.getIndex());
			newMember.setIpAddress(member.getIpAddress());
			newMember.setPort(member.getPort());
			newMember.setState(member.getState());
			newMember.setStatus(member.getStatus());
			newMemberList.add(newMember);
		}
		newPool.setMemberList(newMemberList);
		if (newMemberList.size() == 0)
			return null;
		return newPool;
	}

	private OBDtoAdcPoolAlteon makeNewPool(OBDtoAdcPoolAlteon newPool, OBDtoAdcPoolAlteon oldPool) throws OBException {
		OBDtoAdcPoolAlteon returnPool = new OBDtoAdcPoolAlteon();
		returnPool.setAlteonId(newPool.getAlteonId());
		returnPool.setHealthCheck(newPool.getHealthCheck());
		returnPool.setHealthCheckV2(new OBDtoAdcHealthCheckAlteon(newPool.getHealthCheckV2()));
		returnPool.setIndex(newPool.getIndex());
		returnPool.setLbMethod(newPool.getLbMethod());
		returnPool.setName(newPool.getName());
		ArrayList<OBDtoAdcPoolMemberAlteon> newMemberList = new ArrayList<OBDtoAdcPoolMemberAlteon>();
		ArrayList<OBDtoAdcPoolMemberAlteon> newList = newPool.getMemberList();
		ArrayList<OBDtoAdcPoolMemberAlteon> oldList = oldPool.getMemberList();
		boolean isAdd;
		for (OBDtoAdcPoolMemberAlteon member : newList) {
			isAdd = true;
//			Integer alteonNodeID=0;
			if (member.getAlteonNodeID() == null || member.getAlteonNodeID().equals(0)) {// ID 부여가 안된 경우임.
//				alteonNodeID = realServerMap.get(member.getIpAddress());
//				if(alteonNodeID==null)
//					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to get real server id");
//				member.setAlteonNodeID(alteonNodeID);
//				isAdd=true;
				isAdd = false;
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "real server id error. You must not be here!!! ");
			} else {
				for (OBDtoAdcPoolMemberAlteon old : oldList) {
					if (member.getAlteonNodeID().equals(old.getAlteonNodeID())) {
						isAdd = false;
						break;
					}
				}
			}

			if (isAdd == true) {
				OBDtoAdcPoolMemberAlteon newMember = new OBDtoAdcPoolMemberAlteon();
				newMember.setAlteonNodeID(member.getAlteonNodeID());
				newMember.setIndex(member.getIndex());
				newMember.setIpAddress(member.getIpAddress());
				newMember.setPort(member.getPort());
				newMember.setState(member.getState());
				newMember.setStatus(member.getStatus());
				newMemberList.add(newMember);
			}
		}
		returnPool.setMemberList(newMemberList);
		if (newMemberList.size() == 0)
			return null;
		return returnPool;
	}

	private OBDtoAdcPoolAlteon makeDelPool(OBDtoAdcPoolAlteon newPool, OBDtoAdcPoolAlteon oldPool) throws OBException {
		OBDtoAdcPoolAlteon returnPool = new OBDtoAdcPoolAlteon();
		returnPool.setAlteonId(newPool.getAlteonId());
		returnPool.setHealthCheck(newPool.getHealthCheck());
		returnPool.setHealthCheckV2(new OBDtoAdcHealthCheckAlteon(newPool.getHealthCheckV2()));
		returnPool.setIndex(newPool.getIndex());
		returnPool.setLbMethod(newPool.getLbMethod());
		returnPool.setName(newPool.getName());
		ArrayList<OBDtoAdcPoolMemberAlteon> newMemberList = new ArrayList<OBDtoAdcPoolMemberAlteon>();
		ArrayList<OBDtoAdcPoolMemberAlteon> newList = newPool.getMemberList();
		ArrayList<OBDtoAdcPoolMemberAlteon> oldList = oldPool.getMemberList();
		for (OBDtoAdcPoolMemberAlteon member : oldList) {
			boolean isAdd = false;
			String alteonNodeID = null;
			if (member.getAlteonNodeID() == null || member.getAlteonNodeID().equals(0)) {// ID 부여가 안된 경우임.
				alteonNodeID = realServerMap.get(member.getIpAddress());
				if (alteonNodeID == null)
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to get real server id");
				isAdd = true;
			} else {
				isAdd = true;
				for (OBDtoAdcPoolMemberAlteon old : newList) {
					if (member.getAlteonNodeID().equals(old.getAlteonNodeID())) {
						isAdd = false;
						break;
					}
				}
			}

			if (isAdd == true) {
				OBDtoAdcPoolMemberAlteon newMember = new OBDtoAdcPoolMemberAlteon();
				newMember.setAlteonNodeID(member.getAlteonNodeID());
				newMember.setIndex(member.getIndex());
				newMember.setIpAddress(member.getIpAddress());
				newMember.setPort(member.getPort());
				newMember.setState(member.getState());
				newMember.setStatus(member.getStatus());
				newMemberList.add(newMember);
			}
		}
		returnPool.setMemberList(newMemberList);
		if (newMemberList.size() == 0)
			return null;
		return returnPool;
	}

	private OBDtoAdcPoolAlteon getPoolMembersAlteon(String poolID, OBDtoAdcVServerAlteon vsInfo) throws OBException {// pool에
																														// 등록된
																														// 신규
																														// 멤버
																														// 목록을
																														// 추출한다.
		if (vsInfo == null)
			return null;
		ArrayList<OBDtoAdcVService> oldVSrvList = vsInfo.getVserviceList();
		if (oldVSrvList == null)
			return null;

		for (OBDtoAdcVService vsrv : oldVSrvList) {
			OBDtoAdcPoolAlteon pool = vsrv.getPool();
			if (pool == null)
				continue;
			if (pool.getAlteonId() == null)
				continue;
			if (pool.getAlteonId().equals(poolID))
				return pool;
		}
		return null;
	}

	private ArrayList<OBDtoAdcPoolAlteon> getNewAddPoolmemberListV2(Integer adcIndex, OBDtoAdcVServerAlteon newVSInfo,
			OBDtoAdcVServerAlteon oldVSInfo, OBVServerDB vDB) throws OBException {// pool에 새롭게 등록된 멤버 목록을 조사한다. 새로운
																					// pool에 등록된 멤버는 무조건 등록한다.
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, newVSInfo:%s, oldVSInfo:%s", adcIndex, newVSInfo, oldVSInfo));
		ArrayList<OBDtoAdcPoolAlteon> result = new ArrayList<OBDtoAdcPoolAlteon>();

		ArrayList<OBDtoAdcVService> vsrvList = newVSInfo.getVserviceList();
		if (vsrvList == null) {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		}
		for (OBDtoAdcVService vsrv : vsrvList) {
			OBDtoAdcPoolAlteon pool = vsrv.getPool();
			if (pool == null)
				continue;

			if (pool.getAlteonId() == null || pool.getAlteonId().equals(0)) {// 신규 등록된 pool
				String alteonId = groupMap.get(pool.getName());
				if (alteonId == null)
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to get pool id");

				OBDtoAdcPoolAlteon newPool = makeNewPool(alteonId, pool);
				if (newPool != null) {
//					OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("00000:%s", newPool));		
					result.add(newPool);
				}
				continue;
			}
			// 기존에 등록된 pool인 경우. 이전과 멤버가 같은지 확인. 추가/제거가 있을 수 있음

			OBDtoAdcPoolAlteon oldPool = getPoolMembersAlteon(pool.getAlteonId(), oldVSInfo);
			if (oldPool == null) { // 기존 멤버가 없었다. 지금 pool 멤버로 pool을 구성한다.
				OBDtoAdcPoolAlteon newPool = makeNewPool(pool.getAlteonId(), pool);
				if (newPool != null) {
					result.add(newPool);
				}
				continue;
			}
			// pool에 멤버가 있었으니 과거-현재 대조해서 차이를 반영한다.
			OBDtoAdcPoolAlteon newPool = makeNewPool(pool, oldPool);
			if (newPool != null) {
//				OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("22222:%s", newPool));		
				result.add(newPool);
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	/**
	 * 지정된 pool에 할당된 node 정보를 추출한다.
	 * 
	 * @param adcIndex
	 * @param poolInfo
	 * @param memberlist
	 * @param newNodeList
	 * @return
	 * @throws Exception
	 */
	private ArrayList<OBDtoAdcPoolAlteon> getNewAddPoolmemberList(Integer adcIndex, OBDtoAdcVServerAlteon virtualServer,
			ArrayList<OBDtoAdcVService> serviceList, OBVServerDB vDB) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start."));

		ArrayList<OBDtoAdcPoolAlteon> list = new ArrayList<OBDtoAdcPoolAlteon>();

		for (int i = 0; i < serviceList.size(); i++) {
			OBDtoAdcPoolAlteon poolObj = serviceList.get(i).getPool();
			if (poolObj == null)
				continue;

			ArrayList<OBDtoAdcPoolMemberAlteon> memberlist = poolObj.getMemberList();
			OBDtoAdcPoolAlteon adcPool;
			try {
				adcPool = vDB.getPoolInfoAlteon(poolObj.getIndex());
			} catch (Exception e) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("message:%s", e.getMessage()));
			}

			ArrayList<OBDtoAdcPoolMemberAlteon> newMemberList = new ArrayList<OBDtoAdcPoolMemberAlteon>();
			boolean flag = false;

			if (adcPool == null) {// 신규 추가된 항목. 모든 멤버를 등록한다.
				newMemberList = poolObj.getMemberList();
				flag = true;
			} else {// 기존에 등록된 pool.
				for (int ii = 0; ii < memberlist.size(); ii++) {
					OBDtoAdcPoolMemberAlteon memObj = memberlist.get(ii);
					try {
						if (vDB.getPoolmemberInfoAlteon(adcIndex, poolObj.getAlteonId(),
								memObj.getAlteonNodeID()) == null) {// 신규 할당된 멤버.
							newMemberList.add(memObj);
							flag = true;
						}
					} catch (Exception e) {
						throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
								String.format("message:%s", e.getMessage()));
					}
				}
			}
			if (flag == true) {
				OBDtoAdcPoolAlteon newPoolObj = new OBDtoAdcPoolAlteon();
				newPoolObj.setAlteonId(poolObj.getAlteonId());
				newPoolObj.setIndex(poolObj.getIndex());
				newPoolObj.setLbMethod(poolObj.getLbMethod());
				newPoolObj.setMemberList(newMemberList);
				newPoolObj.setName(poolObj.getName());
				list.add(newPoolObj);
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));

		return list;
	}

	private ArrayList<OBDtoAdcVService> getNewAddVServiceListV2(Integer adcIndex, OBDtoAdcVServerAlteon newVSInfo,
			OBDtoAdcVServerAlteon oldVSInfo, OBVServerDB vDB) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, newVSInfo:%s, oldVSInfo:%s", adcIndex, newVSInfo, oldVSInfo));
		ArrayList<OBDtoAdcVService> result = new ArrayList<OBDtoAdcVService>();

		ArrayList<OBDtoAdcVService> newVsrvList = newVSInfo.getVserviceList();
		ArrayList<OBDtoAdcVService> oldVsrvList = oldVSInfo.getVserviceList();

		for (OBDtoAdcVService newVsrv : newVsrvList) {
			boolean isAdd = true;
			for (OBDtoAdcVService oldVsrv : oldVsrvList) {
				if (newVsrv.getServicePort().equals(oldVsrv.getServicePort())) {
					isAdd = false;
					break;
				}
			}
			if (isAdd == true) {
				OBDtoAdcVService newObj = new OBDtoAdcVService();
				OBDtoAdcPoolAlteon newPool = new OBDtoAdcPoolAlteon();
				OBDtoAdcPoolAlteon currPool = newVsrv.getPool();
				if (currPool != null) {
					if (currPool.getAlteonId() == null || currPool.getAlteonId().equals(0)) {
						String alteonId = groupMap.get(currPool.getName());
						if (alteonId == null)
							throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to get pool id");
						newPool.setAlteonId(alteonId);
					} else {
						newPool.setAlteonId(currPool.getAlteonId());
					}
					newPool.setHealthCheck(currPool.getHealthCheck());
					newPool.setHealthCheckV2(new OBDtoAdcHealthCheckAlteon(currPool.getHealthCheckV2()));
					newPool.setIndex(currPool.getIndex());
					newPool.setLbMethod(currPool.getLbMethod());
					newPool.setName(currPool.getName());
					newPool.setMemberList(null);// 사용하지 않아야 함.
				}
				newObj.setPool(newPool);
				newObj.setRealPort(newVsrv.getRealPort());
				newObj.setServiceIndex(newVsrv.getServiceIndex());
				newObj.setServicePort(newVsrv.getServicePort());
				newObj.setStatus(newVsrv.getStatus());
				result.add(newObj);
			}
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));

		return result;
	}

	/**
	 * 새롭게 추가되거나 변경된 virtual service 항목을 추출한다.
	 * 
	 * @param adcIndex
	 * @param alteonID
	 * @param serviceList
	 * @return
	 * @throws Exception
	 */
	private ArrayList<OBDtoAdcVService> getNewAddVServiceList(Integer adcIndex, String vsIndex,
			ArrayList<OBDtoAdcVService> serviceList, OBVServerDB vDB) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start"));
		ArrayList<OBDtoAdcVService> list = new ArrayList<OBDtoAdcVService>();

		ArrayList<OBDtoAdcVService> currList;
		try {
			currList = vDB.getVServiceList(vsIndex);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("failed to get vservice list(adcIndex:%d, vsIndex: %s). message:%s", adcIndex,
							vsIndex, e.getMessage()));
		}
		for (int i = 0; i < serviceList.size(); i++) {
			OBDtoAdcVService obj1 = serviceList.get(i);
			int k = 0;
			for (k = 0; k < currList.size(); k++) {
				OBDtoAdcVService obj2 = currList.get(k);
				if (obj1.getRealPort().equals(obj2.getRealPort()) && obj1.getServicePort().equals(obj2.getServicePort())
						&& obj1.getPool().getAlteonId().equals(obj2.getPool().getAlteonId()))
					break;
			}
			if (k == currList.size()) {
//				obj1.setVsId(alteonID);
				list.add(obj1);
			}
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));

		return list;
	}

	private ArrayList<OBDtoAdcPoolAlteon> getNewDelPoolmemberListV2(Integer adcIndex, OBDtoAdcVServerAlteon newVSInfo,
			OBDtoAdcVServerAlteon oldVSInfo, OBVServerDB vDB) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d, newVSInfo:%s, oldVSInfo:%s",
				adcIndex, newVSInfo.toString(), oldVSInfo.toString()));
		ArrayList<OBDtoAdcPoolAlteon> result = new ArrayList<OBDtoAdcPoolAlteon>();

		ArrayList<OBDtoAdcVService> vsrvList = oldVSInfo.getVserviceList();
		if (vsrvList == null) {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		}
		for (OBDtoAdcVService vsrv : vsrvList) {
			OBDtoAdcPoolAlteon pool = vsrv.getPool();
			// 기존에 등록된 pool인 경우. 이전 설정과 동일한지? 새롭게 추가된 pool인지 검사.
			if (pool == null)
				continue;
			OBDtoAdcPoolAlteon delPool = getPoolMembersAlteon(pool.getAlteonId(), newVSInfo);
			if (delPool != null) {// 삭제된 pool.
				OBDtoAdcPoolAlteon newPool = makeDelPool(delPool, pool);
				if (newPool != null) {
					result.add(newPool);
				}
			}
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	/**
	 * 지정된 pool에서 삭제된 member 목록을 추출한다.
	 * 
	 * @param adcIndex
	 * @param virtualServer
	 * @param newPoolList
	 * @param newNodeList
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	private ArrayList<OBDtoAdcPoolAlteon> getNewDelPoolmemberList(Integer adcIndex, OBDtoAdcVServerAlteon virtualServer,
			ArrayList<OBDtoAdcVService> serviceList, OBVServerDB vDB) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start."));
		ArrayList<OBDtoAdcPoolAlteon> list = new ArrayList<OBDtoAdcPoolAlteon>();
		for (int i = 0; i < serviceList.size(); i++) {
			OBDtoAdcPoolAlteon poolObj = serviceList.get(i).getPool();
			if (poolObj == null)
				continue;
			if (poolObj.getAlteonId() == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("invalid group id(adcIndex:%d)", adcIndex));
			}

			try {
				if (vDB.getPoolInfoAlteon(poolObj.getIndex()) == null)
					continue;// 이전에 설정된 항목에서만 검사한다. 신규 추가된 pool은 검사 대상에서 제외..
			} catch (Exception e) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("message:%s", e.getMessage()));
			} // 신규 추가항목.

			ArrayList<OBDtoAdcPoolMemberAlteon> newMemberList = poolObj.getMemberList();
			ArrayList<OBDtoAdcPoolMemberAlteon> oldMemberList;
			try {
				oldMemberList = vDB.getPoolmemberInfoListAlteon(poolObj.getIndex());
			} catch (Exception e) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("message:%s", e.getMessage()));
			}

			boolean flag = false;
			ArrayList<OBDtoAdcPoolMemberAlteon> delMemberList = new ArrayList<OBDtoAdcPoolMemberAlteon>();
			for (int ii = 0; ii < oldMemberList.size(); ii++) {//
				OBDtoAdcPoolMemberAlteon tmpMember = oldMemberList.get(ii);
				int kk = 0;
				for (kk = 0; kk < newMemberList.size(); kk++) {
					OBDtoAdcPoolMemberAlteon tempObj = newMemberList.get(kk);
					if (tmpMember.getAlteonNodeID().equals(tempObj.getAlteonNodeID()))
						break;
				}
				if (kk == newMemberList.size()) {
					delMemberList.add(tmpMember);
					flag = true;
				}
			}
			if (flag == true) {
				OBDtoAdcPoolAlteon newPoolObj = new OBDtoAdcPoolAlteon();
				newPoolObj.setAlteonId(poolObj.getAlteonId());
				newPoolObj.setIndex(poolObj.getIndex());
				newPoolObj.setLbMethod(poolObj.getLbMethod());
				newPoolObj.setMemberList(delMemberList);
				newPoolObj.setName(poolObj.getName());
				list.add(newPoolObj);
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));

		return list;
	}

	private ArrayList<OBDtoAdcVService> getNewDelVServiceListV2(Integer adcIndex, OBDtoAdcVServerAlteon newVSInfo,
			OBDtoAdcVServerAlteon oldVSInfo, OBVServerDB vDB) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start, adcIndex:%d, newVSInfo:%s, oldVSInfo:%s", adcIndex, newVSInfo, oldVSInfo));
		ArrayList<OBDtoAdcVService> result = new ArrayList<OBDtoAdcVService>();

		ArrayList<OBDtoAdcVService> newVsrvList = newVSInfo.getVserviceList();
		ArrayList<OBDtoAdcVService> oldVsrvList = oldVSInfo.getVserviceList();

		for (OBDtoAdcVService oldVsrv : oldVsrvList) {
			boolean isAdd = true;
			for (OBDtoAdcVService newVsrv : newVsrvList) {
				if (newVsrv.getServicePort().equals(oldVsrv.getServicePort())) {
					isAdd = false;
					break;
				}
			}
			if (isAdd == true) {
				OBDtoAdcVService newObj = new OBDtoAdcVService();

				newObj.setRealPort(oldVsrv.getRealPort());
				newObj.setServiceIndex(oldVsrv.getServiceIndex());
				newObj.setServicePort(oldVsrv.getServicePort());
				newObj.setStatus(oldVsrv.getStatus());
				result.add(newObj);
			}
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	private ArrayList<OBDtoAdcVService> getNewDelVServiceList(Integer adcIndex, String vsIndex,
			ArrayList<OBDtoAdcVService> serviceList, OBVServerDB vDB) throws OBException {
		ArrayList<OBDtoAdcVService> list = new ArrayList<OBDtoAdcVService>();
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start"));

		ArrayList<OBDtoAdcVService> currList;
		try {
			currList = vDB.getVServiceList(vsIndex);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("failed to get vservice list(adcIndex:%d, vsIndex: %s). message:%s", adcIndex,
							vsIndex, e.getMessage()));
		}
		for (int i = 0; i < currList.size(); i++) {
			OBDtoAdcVService obj1 = currList.get(i);
			int k = 0;
			for (k = 0; k < serviceList.size(); k++) {
				OBDtoAdcVService obj2 = serviceList.get(k);
				if (obj1.getRealPort().equals(obj2.getRealPort()) && obj1.getServicePort().equals(obj2.getServicePort())
						&& obj1.getPool().getAlteonId().equals(obj2.getPool().getAlteonId())) {
					break;
				}
			}
			if (k == serviceList.size()) {
				list.add(obj1);
			}
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));
		return list;
	}

	private class ChangeInfo {
		ArrayList<OBDtoAdcNodeAlteon> nodeList;
		ArrayList<OBDtoAdcVService> serviceList;

		@Override
		public String toString() {
			return "Change [nodeList=" + nodeList + ", serviceList=" + serviceList + "]";
		}

		public ArrayList<OBDtoAdcNodeAlteon> getNodeList() {
			return nodeList;
		}

		public void setNodeList(ArrayList<OBDtoAdcNodeAlteon> nodeList) {
			this.nodeList = nodeList;
		}

		public ArrayList<OBDtoAdcVService> getServiceList() {
			return serviceList;
		}

		public void setServiceList(ArrayList<OBDtoAdcVService> serviceList) {
			this.serviceList = serviceList;
		}
	}

	private ChangeInfo getNewNodeListV2(Integer adcIndex, OBDtoAdcVServerAlteon newVSInfo, OBVServerDB vDB)
			throws OBException {// group(pool) Alteon ID가 으면 신규 pool이다.
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, newVSInfo:%s", adcIndex, newVSInfo));
		ArrayList<OBDtoAdcNodeAlteon> nodeList = new ArrayList<OBDtoAdcNodeAlteon>();
		ChangeInfo result = new ChangeInfo();

		ArrayList<OBDtoAdcVService> serviceList = newVSInfo.getVserviceList();
		if (serviceList == null) {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "end. result:nothing");
			return result;
		}
		realServerMap.clear();
		Integer nodeID = vDB.allocNodeID(adcIndex); // 쓰지 않는 node id 최소값 찾음
		for (OBDtoAdcVService service : serviceList) {
			OBDtoAdcPoolAlteon pool = service.getPool();
			if (pool == null)
				continue;

			ArrayList<OBDtoAdcPoolMemberAlteon> memberList = pool.getMemberList();
			if (memberList == null)
				continue;
			for (OBDtoAdcPoolMemberAlteon member : memberList) {
				if (member == null)
					continue;
				if (member.getAlteonNodeID() == null || member.getAlteonNodeID().equals(0)) // 새로 들어온 node
				{
					String tmpNodeID = realServerMap.get(member.getIpAddress());
					if (tmpNodeID != null)
						continue;// 다른 그룹에서 겹친 새 node이다.v29는 중복 허용이지만, adcsmart에서는 IP만 넣으므로 허용하지 않기로.

					while (vDB.isUnusedNodeID(adcIndex, nodeID) == false)
						nodeID++;

					// member에 node id 할당
					member.setAlteonNodeID(Integer.toString(nodeID));
					// 새로 생긴 노드 정보 추가
					OBDtoAdcNodeAlteon newNode = new OBDtoAdcNodeAlteon();
					newNode.setAlteonId(Integer.toString(nodeID));
					newNode.setIndex(member.getIndex());
					newNode.setIpAddress(member.getIpAddress());
					newNode.setName(member.getIpAddress());
					newNode.setState(member.getState());
					newNode.setStatus(0);

					// ID 저장.
					realServerMap.put(newNode.getIpAddress(), newNode.getAlteonId()); // 노드를 모두 hash에 기재한다.
					nodeList.add(newNode);
					nodeID++;
				}
			}
		}
		result.setNodeList(nodeList);
		result.setServiceList(serviceList);
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	/**
	 * 신규 추가된 node 정보를 추출한다. 입력된 정보와 DB에 저장된 정보의 차이를 비교하여 신규 추가된 node를 추출한다.
	 * 
	 * @param adcIndex
	 * @param virtualServer
	 * @param memberlist
	 * @param lbMethod
	 * @return
	 * @throws Exception
	 */
	private ArrayList<OBDtoAdcNodeAlteon> getNewNodeList(Integer adcIndex, OBDtoAdcVServerAlteon virtualServer,
			ArrayList<OBDtoAdcVService> vserviceList, OBVServerDB vDB) throws OBException {
		ArrayList<OBDtoAdcNodeAlteon> list = new ArrayList<OBDtoAdcNodeAlteon>();

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start."));

		for (int i = 0; i < vserviceList.size(); i++) {
			OBDtoAdcPoolAlteon poolObj = vserviceList.get(i).getPool();
			if (poolObj == null)
				continue;
			ArrayList<OBDtoAdcPoolMemberAlteon> memberlist = poolObj.getMemberList();
			for (int k = 0; k < memberlist.size(); k++) {
				OBDtoAdcPoolMemberAlteon memObj = memberlist.get(k);

				try {
					if (vDB.getNodeInfoAlteon(adcIndex, memObj.getAlteonNodeID()) == null) {
						OBDtoAdcNodeAlteon obj = new OBDtoAdcNodeAlteon();
						obj.setAlteonId(memObj.getAlteonNodeID());
						obj.setIpAddress(memObj.getIpAddress());
						obj.setName(memObj.getIpAddress());
						obj.setState(memObj.getState());
						list.add(obj);
					}
				} catch (Exception e1) {
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format("failed to add node(index:%d). message:%s", adcIndex, e1.getMessage()));
				}
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));
		return list;
	}

	private ArrayList<OBDtoAdcPoolAlteon> getNewPoolListV2(Integer adcIndex, OBDtoAdcVServerAlteon newVSInfo,
			OBVServerDB vDB) throws OBException {// Alteon ID가 없는 경우에는 신규 추가된 pool로 간주한다.
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex:%d, newVSInfo:%ss", adcIndex, newVSInfo));
		ArrayList<OBDtoAdcPoolAlteon> result = new ArrayList<OBDtoAdcPoolAlteon>();

		ArrayList<OBDtoAdcVService> vsrvList = newVSInfo.getVserviceList();
		if (vsrvList == null) {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		}
		groupMap.clear();
		Integer poolID = vDB.allocPoolID(adcIndex);
		for (OBDtoAdcVService vsrv : vsrvList) {
			OBDtoAdcPoolAlteon pool = vsrv.getPool();
			if (pool == null)
				continue;
			if (pool.getIndex() == null || pool.getIndex().isEmpty()) // 원래는 Alteon id가 비어 있으면 새 pool로 판단했었는데, pool id는
																		// 새 pool에도 지정되어 온다. DB index가 없으면 새 pool이다. 나중에
																		// history 비교 방식으로 바꿀 거니까 이 방법은 임시다.
			{ // pool ID가 지정되기 때문에 pool ID할당 부분을 지운다.
//				Integer tmpPoolID = groupMap.get(pool.getName());
//				if(tmpPoolID!=null)
//					continue;// 신규 중복된 경우임. 
//				while(vDB.isUnusedPoolID(adcIndex, poolID, db) == false)
//					poolID++;

				OBDtoAdcPoolAlteon newPool = new OBDtoAdcPoolAlteon();
				newPool.setIndex(pool.getIndex());
				newPool.setName(pool.getName());
				newPool.setHealthCheck(pool.getHealthCheck());
				newPool.setHealthCheckV2(new OBDtoAdcHealthCheckAlteon(pool.getHealthCheckV2()));
				newPool.setAlteonId(pool.getAlteonId());
				newPool.setLbMethod(pool.getLbMethod());
				groupMap.put(newPool.getName(), Integer.toString(poolID));
				result.add(newPool);
				poolID++;
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
		return result;
	}

	/**
	 * 신규 추가된 pool 정보를 추출한다. 입력된 정보와 DB에 저장된 정보를 비교하여 추출한다.
	 * 
	 * @param adcIndex
	 * @param virtualServer
	 * @param memberlist
	 * @param lbMethod
	 * @return
	 * @throws Exception
	 */
	private ArrayList<OBDtoAdcPoolAlteon> getNewPoolList(Integer adcIndex, OBDtoAdcVServerAlteon vServerInfo,
			ArrayList<OBDtoAdcVService> vserviceList, OBVServerDB vDB) throws OBException {
		ArrayList<OBDtoAdcPoolAlteon> list = new ArrayList<OBDtoAdcPoolAlteon>();
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start."));
		for (int i = 0; i < vserviceList.size(); i++) {
			OBDtoAdcPoolAlteon poolObj = vserviceList.get(i).getPool();
			OBDtoAdcPoolAlteon adcPool;
			try {
				adcPool = vDB.getPoolInfoAlteon(poolObj.getIndex());
			} catch (Exception e) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get pool info(index:%d). message:%s", adcIndex, e.getMessage()));
			}
			if (adcPool == null) {// 신규 추가된 항목임.
				adcPool = new OBDtoAdcPoolAlteon();
				adcPool.setAlteonId(poolObj.getAlteonId());
				adcPool.setLbMethod(poolObj.getLbMethod());
				adcPool.setHealthCheck(poolObj.getHealthCheck());
				adcPool.setHealthCheckV2(new OBDtoAdcHealthCheckAlteon(poolObj.getHealthCheckV2()));
				adcPool.setMemberList(poolObj.getMemberList());
				adcPool.setName(poolObj.getName());
				list.add(adcPool);
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));
		return list;
	}

	/*
	 * // virtual server 수정과정에서 vrrp변경을 처리하던 함수인데, active/standby 처리 방식을 바꾸면서
	 * virtual server 수정에서 vrrp 처리를 하지 않게 되어 쓰이지 않는다. 2012.12.5 ykkim. private
	 * OBDtoVRRP getNewVrrpInfo(String vsIndex, OBDtoVRRP currInfo, OBDatabase db,
	 * OBVServerDB vDB) throws OBException { if(currInfo.getVrIndex() == 0)// 설정이 안된
	 * 경우. return null;
	 * 
	 * OBDtoVRRP obj; try { obj = vDB.getVrrpID(vsIndex, db); } catch(Exception e) {
	 * throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
	 * String.format("failed to get vrrp id(vsIndex:%s). message:%s", vsIndex,
	 * e.getMessage())); } if(obj.getVrIndex() == 0) return currInfo;
	 * if(obj.getIfNum() != currInfo.getIfNum()) return currInfo;
	 * if(obj.getRouterIndex() != currInfo.getRouterIndex()) return currInfo;
	 * if(obj.getVrIndex() != currInfo.getVrIndex()) return currInfo; return null; }
	 */
	@Override
	public ArrayList<OBDtoAdcNodeAlteon> getNodeAvailableListAlteon(Integer adcIndex, String poolIndex)
			throws OBException {
		try {
			return new OBVServerDB().getNodeAvailableListAlteon(adcIndex, poolIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public ArrayList<OBDtoAdcNodeF5> getNodeAvailableListF5(Integer adcIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcNodePAS> getNodeAvailableListPAS(Integer adcIndex, String poolIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public OBDtoAdcPoolAlteon getPoolAlteon(String poolIndex) throws OBException {
		// 로컬DB에서 읽어 들여 처리한다.
		try {
			return new OBVServerDB().getPoolInfoAlteon(poolIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public ArrayList<OBDtoAdcPoolAlteon> getPoolAlteonList(Integer adcIndex) throws OBException {
		// 로컬DB에서 읽어 들여 처리한다.
		try {
			return new OBVServerDB().getPoolListAllAlteon(adcIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public OBDtoAdcPoolF5 getPoolF5(String poolIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	public ArrayList<OBDtoAdcPoolF5> getPoolF5List(Integer adcIndex) throws OBException {
		throw new OBException("not supported function");
	}

//	@Override
//	public ArrayList<OBDtoAdcProfile> getProfileList(Integer adcIndex) throws OBException
//	{
//		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
//	}

	@Override
	public OBDtoAdcVServerAlteon getVServerAlteonInfo(Integer adcIndex, String index)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBDatabase db = new OBDatabase();
		OBDtoAdcVServerAlteon obj;
		try {
			db.openDB();
			obj = new OBVServerDB().getVServerInfoAlteon(index);
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

	@Override
	public OBDtoAdcVServerF5 getVServerF5Info(Integer adcIndex, String index)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	/**
	 * DB로부터 데이터를 읽어들인다.
	 * 
	 * @param adcIndex -- ADC 장비 index.
	 * @param db       -- DB 인스턴스.
	 * @return ArrayList<OBDtoAdcVirtualServer>
	 * @throws Exception
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	private ArrayList<OBDtoAdcVServerAlteon> getVServerList(Integer adcIndex, OBDatabase db, OBDatabase db2)
			throws OBException {
		try {
			return new OBVServerDB().getVServerListAllAlteon(adcIndex);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("failed to get vserver list(adcIndex:%d). message:%s", adcIndex, e.getMessage()));
		}
	}

	@Override
	public ArrayList<OBDtoAdcVServerAlteon> getVServerListAlteon(Integer adcIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		// DB에서 ADC 정보를 읽어들인다.
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start to getVServerList"));

		OBDatabase db = new OBDatabase();
		OBDatabase db2 = new OBDatabase();

		// 장비의 시간과 로컬시간이 같은 경우에는 로컬 DB에서 읽어들여 제공한다.
		ArrayList<OBDtoAdcVServerAlteon> retVal = new ArrayList<OBDtoAdcVServerAlteon>();
		try {
			db.openDB();
			db2.openDB();
			retVal = getVServerList(adcIndex, db, db2);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end to getVServerList-status"));
		return retVal;
	}

	@Override
	public ArrayList<OBDtoAdcVServerF5> getVServerListF5(Integer adcIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean isExistVirtualServer(Integer adcIndex, String vsName) throws OBException {
		try {
			return new OBVServerDB().isExistVirtualServer(adcIndex, vsName);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public boolean isAvailableVirtualServerAlteon(Integer adcIndex, Integer port, String alteonID, String ipAddress)
			throws OBException {
		// 로컬DB에서 확인하는 함수를 불러 처리
		try {
			return new OBVServerDB().isExistVirtualServerAlteon(adcIndex, alteonID, ipAddress);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public boolean isAvailableVirtualServerF5(Integer adcIndex, String vsName, String ipAddress, Integer port,
			String alteonID) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

//	@Override
//	public ArrayList<OBDtoAdcVServerAlteon> searchVServerListAlteon(Integer adcIndex, String searchKey) throws OBException
//	{
//		try
//		{
//			return new OBVServerDB().searchVServerListAlteon(adcIndex, searchKey);
//		}
//		catch(OBException e)
//		{
//			throw new OBException(OBException.ERRCODE_SLB_VS_GETINFO, e);
//		}
//		catch(Exception e)
//		{
//			OBException e1 = new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//			throw new OBException(OBException.ERRCODE_SLB_VS_GETINFO, e1);
//		}
//	}

//	@Override
//	public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex, String searchKey) throws OBException
//	{
//		throw new OBException("not supported function");
//	}

	private void setVServerAlteon(Integer adcIndex, OBDtoAdcVServerAlteon vServerInfo,
			ArrayList<OBDtoAdcVService> vsrvList, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		if (isAvailableVirtualServerAlteon(adcIndex, null, vServerInfo.getAlteonId(), vServerInfo.getvIP()) == false)
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("Virtual server not found(adcIndex:%d, alteonID:%s, ip:%s)", adcIndex,
							vServerInfo.getAlteonId(), vServerInfo.getvIP()));

		OBVServerDB vDB = new OBVServerDB();

		// 지정된 virtual server를 삭제하고, 재 등록한다.
		OBDtoAdcInfo adcInfo;
		try {
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
		} catch (OBException e1) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("todo %s", e1.getMessage()));
		}
		if (adcInfo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "invalid adc info");
		}

		if (vServerInfo.getVrrpState().equals(OBDefine.VRRP_STATE.NONE)) // vrrp 설정이 없으면
		{
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
					String.format(" vrrp state none / vrrpstate = %d, routerIndex = %d, vrIndex = %d",
							vServerInfo.getVrrpState(), vServerInfo.getRouterIndex(), vServerInfo.getVrIndex()));
			vServerInfo.setRouterIndex(0);
			vServerInfo.setVrIndex(0);
		} else {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
					String.format(" vrrp state none / vrrpstate = %d, routerIndex = %d, vrIndex = %d, IfNum = %d",
							vServerInfo.getVrrpState(), vServerInfo.getRouterIndex(), vServerInfo.getVrIndex(),
							vServerInfo.getIfNum()));

			if (vServerInfo.getVrIndex().equals(0)) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("invalid vr index(%d).", vServerInfo.getVrIndex()));
			}
			if (vServerInfo.getRouterIndex().equals(0)) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("invalid router index(%d).", vServerInfo.getRouterIndex()));
			}
			if (vServerInfo.getIfNum().equals(0)) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("invalid if num(%d).", vServerInfo.getIfNum()));
			}
		}

		// alteon 모델별 설정.
		OBAdcAlteonHandler alteon = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());

		OBDtoAdcVServerAlteon virtualServerOld = vDB.getVServerInfoAlteon(vServerInfo.getIndex()); // 기존 상태확인
		if (virtualServerOld == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					"VirtualServer edit error. VirtualServer doesn't exist.");
		}

//		HashMap<String, Integer> nodeAvailIDs = vDB.getAvailableNodeIDsAlteon(adcIndex, db); //쓰고 있는 node id:ipaddr 목록을 구한다. 이름이 반대다.
//		
//		setNodeIDs(vsrvList, nodeAvailIDs); //들어온 virtual server에 id를 찾아 준다. id를 갖고 오지 않았기 때문.
		// 신규 추가된 pool인지 검사하여 정보를 추출한다.
		ArrayList<OBDtoAdcPoolAlteon> newPoolList;
		ArrayList<OBDtoAdcNodeAlteon> newNodeList;
		ArrayList<OBDtoAdcPoolAlteon> newAddPoolMemberList;
		ArrayList<OBDtoAdcPoolAlteon> newDelPoolMemberList;
		ArrayList<OBDtoAdcVService> newAddVSerivceList;
		ArrayList<OBDtoAdcVService> newDelVSerivceList;
		ArrayList<OBDtoAdcVServiceChanged> changedServiceList;
		try {
			newPoolList = getNewPoolListV2(adcIndex, vServerInfo, vDB);
			// 신규 추가된 노드가 있는지 검사하여 정보를 추출한다.

			ChangeInfo newInfo = getNewNodeListV2(adcIndex, vServerInfo, vDB);
			newNodeList = newInfo.getNodeList();
			vServerInfo.setVserviceList(newInfo.getServiceList());
			// pool에 추가된 node가 있는지 검사하여 추출한다.
			newAddPoolMemberList = getNewAddPoolmemberListV2(adcIndex, vServerInfo, virtualServerOld, vDB);

			// pool에서 삭제된 node가 있는지 검사하여 정보를 추출한다.
			newDelPoolMemberList = getNewDelPoolmemberListV2(adcIndex, vServerInfo, virtualServerOld, vDB);

			// 추가된 virtual service가 있는지 검사한다.
			newAddVSerivceList = getNewAddVServiceListV2(adcIndex, vServerInfo, virtualServerOld, vDB);
			// 삭제된 virtual service가 있는지 검사한다.
			newDelVSerivceList = getNewDelVServiceListV2(adcIndex, vServerInfo, virtualServerOld, vDB);

			changedServiceList = getChangedServiceListV2(adcIndex, vServerInfo, virtualServerOld, vDB);
		} catch (OBException e1) {
			throw new OBException(e1.getMessage());
		}

		// 변경이력 준비
		OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
		OBDtoAdcConfigChunkAlteon configChunk = null;

		try {
			configChunk = historyManager.MakeConfigChunkAlteon(vServerInfo, OBDefine.CHANGE_BY_USER,
					OBDefine.CHANGE_TYPE_EDIT, OBDefine.CHANGE_OBJECT_VIRTUALSERVER, extraInfo.getAccountIndex());

			if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_NONE) // 바뀐 것이 없으면 변경 작업을 하지 않는다.
			{
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
						"Nothing to be changed. Quitting configuration process normally.");
				OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end adcIndex:%d", adcIndex));
				return;
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String
					.format("VirtualServer edit error. Config difference check fail. (messages = %s)", e.getMessage()));
		}
		// 변경이력 준비 끝
		// 텔넷 연결.
		alteon.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
				adcInfo.getConnService(), adcInfo.getConnPort());
		try {
			alteon.login();
		} catch (OBExceptionUnreachable e1) {
			throw new OBExceptionUnreachable(e1.getMessage());
		} catch (OBExceptionLogin e1) {
			throw new OBExceptionLogin(e1.getMessage());
		}
//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("successful telnet login"));
		// 신규 pool 추가
		try {
			if (newPoolList.size() != 0) {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("try to add new server groups"));
				alteon.addServerGroupList(newPoolList);
			}
			// 신규 node 추가
			if (newNodeList.size() != 0) {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("try to add new real servers"));
				alteon.addRealserverList(newNodeList, adcInfo.getAdcIpAddress());
			}
			// pool에 node 삭제.
			if (newDelPoolMemberList.size() != 0) {
				for (int i = 0; i < newDelPoolMemberList.size(); i++) {
					OBDtoAdcPoolAlteon obj = newDelPoolMemberList.get(i);
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("try to delete a pool members"));
					alteon.delPoolMember(obj.getAlteonId(), obj.getMemberList(), adcInfo.getAdcIpAddress());
				}
			}
			// pool에 node 추가
			if (newAddPoolMemberList.size() != 0) {
				for (int i = 0; i < newAddPoolMemberList.size(); i++) {
					OBDtoAdcPoolAlteon obj = newAddPoolMemberList.get(i);
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("try to add a pool members"));
					alteon.addPoolMemberList(obj.getAlteonId(), obj.getMemberList(), adcInfo.getAdcIpAddress());
				}
			}

			// virtual server 추가
			alteon.setVirtualServer(vServerInfo.getAlteonId(), vServerInfo.getName(), vServerInfo.getvIP(),
					vServerInfo.getUseYN(), adcInfo.getAdcIpAddress());
			// virtual service 추가.
			if (newDelVSerivceList.size() != 0) {
				alteon.delVirtualService(vServerInfo.getAlteonId(), newDelVSerivceList, adcInfo.getAdcIpAddress());
			}
			if (newAddVSerivceList.size() != 0) {
				alteon.addVirtualServiceList(vServerInfo.getAlteonId(), newAddVSerivceList, adcInfo.getAdcIpAddress());
			}

			if (changedServiceList.size() != 0) {
				alteon.changeServiceList(vServerInfo.getAlteonId(), changedServiceList, adcInfo.getAdcIpAddress());
			}

			alteon.cmndApply();
		} catch (OBException e1) {
			alteon.cmndRevert();
			alteon.disconnect();
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw e1;
		} catch (Exception e1) {
			alteon.cmndRevert();
			alteon.disconnect();
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw new OBException(e1.getMessage());
		}

		try {
			downloadSlbConfig(adcIndex, true);

			// 변경이력 기록
			historyManager.writeConfigHistoryAlteon(configChunk);

			// save 작업을 위한 스케줄 등록.
			if (true == new OBEnvManagementImpl().isAlteonAutoSave()) {
				alteon.cmndSave();
			}
		} catch (OBException e1) {
			alteon.cmndRevertApply();
			alteon.disconnect();
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw e1;
		} catch (Exception e1) {
			alteon.cmndRevertApply();
			alteon.disconnect();
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw new OBException(e1.getMessage());
		}

		alteon.disconnect();

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end adcIndex:%d, %s", adcIndex, vServerInfo));
	}

	@Override
	public void setVServerAlteon(OBDtoAdcVServerAlteon virtualServer, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. vs:%s, extra:%s", virtualServer, extraInfo));

		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(virtualServer.getAdcIndex());

			if (timeSync(adcInfo) == true) {
//				throw new OBException(OBException.ERRCODE_SLB_VS_TIME, "adc_index: "+adcInfo.getIndex());
				throw new OBException(OBException.ERRCODE_SLB_VS_TIME, "Sync failed. adc_index: " + adcInfo.getIndex());
			}

//			new OBAdcManagementImpl().waitUntilAdcAvailable(virtualServer.getAdcIndex());
			try {
				setVServerAlteon(adcInfo.getIndex(), virtualServer, virtualServer.getVserviceList(), extraInfo);
			} catch (OBExceptionUnreachable e) {
				throw e;
			} catch (OBExceptionLogin e) {
				throw e;
			} catch (OBException e) {
				throw e;
			} catch (Exception e) {
				throw e;
			} finally {
//				new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServer.getAdcIndex());
			}

			// new OBEnvManagementImpl().updateScheduleSlbSave(tmpAdcInfo.getIndex(), db);
			new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcInfo.getIndex(),
					extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_SET_SUCCESS, adcInfo.getName(),
					virtualServer.getName(), 0L);

			// ykkim comment : Active/Standby peer 장비를 인식하여 동기화하는 코드가 있었는데 제거했다.
			// Active/Standby 제어는 웹에서 동기화 확인 여부를 물어서 할 것이기 때문에 자동으로 하는 루틴은 쓰지 않는다.
			try {// system info 정보 추출하여 저장한다.
				OBDtoAdcSystemInfo info = new OBAdcSystemInfoAlteon().getAdcSystemInfo(adcInfo);
				new OBAdcMonitorDB().writeAdcSystemInfo(adcInfo.getIndex(), info);
			} catch (Exception e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("%s: %s, callstack:%s", "failed to update a system's time(apply, save..)",
								e.getMessage(), new OBUtility().getStackTrace()));
			}
		} catch (OBExceptionUnreachable e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBException e) {
			StackTraceElement[] ste = e.getStackTrace();
			String className = ste[0].getClassName();
			String meThodName = ste[0].getMethodName();
			int lineNumber = ste[0].getLineNumber();
			String fileName = ste[0].getFileName();
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("line:%s. msg:%s",
					className + "." + meThodName + " " + fileName + " " + lineNumber + "line", e.getErrorMessage()));
			throw e;
		} catch (Exception e) {
			StackTraceElement[] ste = e.getStackTrace();
			String className = ste[0].getClassName();
			String meThodName = ste[0].getMethodName();
			int lineNumber = ste[0].getLineNumber();
			String fileName = ste[0].getFileName();
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("line:%s. msg:%s",
					className + "." + meThodName + " " + fileName + " " + lineNumber + "line", e.getMessage()));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public void setVServerF5(OBDtoAdcVServerF5 virtualServer, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

//	private void cmdRevertApply(Integer adcIndex) throws OBExceptionUnreachable, OBExceptionLogin, OBException
//	{
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			db.openDB();
//		}
//		catch(Exception e1)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("message:%s", e1.getMessage()));
//		}	
//		
//		OBDtoAdcInfo adcInfo;
//		try
//		{
//			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex, db);
//		}
//		catch(Exception e1)
//		{
//			db.closeDB();
//			throw new OBException(e1.getMessage());
//		}
//		// alteon 모델별 설정.
//		OBAdcAlteon alteon = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
//
//		alteon.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt());
//		try
//		{
//			alteon.login();
//		}
//		catch(OBExceptionUnreachable e)
//		{
//			db.closeDB();
//			throw new OBExceptionUnreachable(e.getMessage());
//		}
//		catch(OBExceptionLogin e)
//		{
//			db.closeDB();
//			throw new OBExceptionLogin(e.getMessage());
//		}
//		catch(Exception e)
//		{
//			db.closeDB();
//			throw new OBException(e.getMessage());
//		}
//		
//		try
//		{
//			alteon.cmndRevertApply();
//		}
//		catch(Exception e)
//		{
//			db.closeDB();
//			alteon.disconnect();
//			throw new OBException(e.getMessage());
//		}
//		
//		db.closeDB();
//		alteon.disconnect();
//	}

	public static OBDtoAdcVServerAlteon CloneVServer(OBDtoAdcVServerAlteon original) {
		OBDtoAdcVServerAlteon clone = null;
		if (original != null) {
			clone = new OBDtoAdcVServerAlteon(original);
		}

		return clone;
	}

	public void Revert(Integer adcIndex, OBDtoAdcConfigChunkAlteon configChunk, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException {
		if (configChunk.getChangeObject() == OBDefine.CHANGE_OBJECT_VIRTUALSERVER) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "calling Revert()...");
			// int adcIndex = configChunk.getVsConfig().getVsNew().getAdcIndex();
			ArrayList<String> vsIndexList = new ArrayList<String>();

			// OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
			// extraInfo.setAccountIndex(0);
			// extraInfo.setClientIPAddress("-");
			// extraInfo.setExtraMsg1("");
			// extraInfo.setExtraMsg2("");

			if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_ADD) {
				vsIndexList.add(configChunk.getVsConfig().getVsNew().getIndex());
				delVServer(adcIndex, vsIndexList, extraInfo);
			} else if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_DELETE) {
				addVServerAlteon(configChunk.getVsConfig().getVsOld(), extraInfo);
			} else if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_EDIT) {
				// enable/disable은 set에 포함되지 않고 따로 있으므로 확인해서 분기한다. F5에서도 enable/disable이 분리되어
				// 있어서 비슷한 문제가 있었다.
				if (configChunk.getVsConfig().getUseYNChange() == OBDefine.CHANGE_TYPE_EDIT) {
					vsIndexList.add(configChunk.getVsConfig().getVsNew().getIndex()); // vsIndex를 목록으로 만든다.
					if (configChunk.getVsConfig().getVsOld().getUseYN() == OBDefine.STATE_DISABLE) // 과거가 disable
					{
						disableVServer(adcIndex, vsIndexList, extraInfo);
					} else // 과거가 enable
					{
						enableVServer(adcIndex, vsIndexList, extraInfo);
					}
				} else {
					setVServerAlteon(configChunk.getVsConfig().getVsOld(), extraInfo);
				}
			} else {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Error : Nothing to revert.");
			}
			vsIndexList = null;
		}
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Revert() done");
	}

	@Override
	public void revertSlbConfig(Integer adcIndex, String revertConfig, String currentConfig, String newVServiceList,
			String newPoolList, String newNodeList) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
	}

	@Override
	public void addVServerF5(OBDtoAdcVServerF5 virtualServer, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

//	@Override
//	public Integer getVSTotalCount(Integer adcIndex) throws OBException
//	{
//		OBDatabase db = new OBDatabase();
//		int retVal = 0;
//		try
//		{
//			db.openDB();
//			retVal = new OBVServerDB().getTotalVServerCount(adcIndex, db);
//		}
//		catch(OBException e)
//		{
//			throw e;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		finally
//		{
//			if(db!=null) db.closeDB();
//		}
//		return retVal;
//	}

//	@Override
//	public ArrayList<OBDtoAdcVServerAlteon> getVServerListAlteon(Integer adcIndex, Integer beginIndex, Integer endIndex) throws OBExceptionUnreachable, OBExceptionLogin, OBException
//	{
//		// DB에서 ADC 정보를 읽어들인다.
//		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start"));
//		
//		OBDatabase db = new OBDatabase();
//		OBDatabase db2 = new OBDatabase();
//		try
//		{
//			db.openDB();
//			db2.openDB();
//		}
//		catch(OBException e1)
//		{
//			throw new OBException(OBException.ERRCODE_SLB_VS_COUNT, e1);
//		}
////		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start to getVServerList-after db open"));
//
//		ArrayList<OBDtoAdcVServerAlteon> list;
//		try
//		{
//			list = new OBVServerDB().getVServerListAllAlteon(adcIndex, beginIndex, endIndex, OBDefine.ORDER_TYPE_VSNAME, OBDefine.ORDER_DIR_ASCEND, db, db2);
//		}
//		catch(OBException e)
//		{
//			db.closeDB();
//			db2.closeDB();
//			throw new OBException(OBException.ERRCODE_SLB_VS_COUNT, e);
//		}
//		catch(Exception e)
//		{
//			db.closeDB();
//			db2.closeDB();
//			OBException e1 = new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//			throw new OBException(OBException.ERRCODE_SLB_VS_COUNT, e1);
//		}
//		
//		// 장비의 시간과 로컬시간이 같은 경우에는 로컬 DB에서 읽어들여 제공한다.
//		db.closeDB();
//		db2.closeDB();
//		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", list));
//		
//		return list;
//	}
//
//	@Override
//	public ArrayList<OBDtoAdcVServerF5> getVServerListF5(Integer adcIndex,
//			Integer beginIndex, Integer endIndex)
//			throws OBExceptionUnreachable, OBExceptionLogin, OBException
//	{
//		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
//	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			new OBAdcVServerAlteon().updateSLBStatus(1);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	@Override
//	public void updateSLBStatus(Integer adcIndex) throws OBException
//	{
//		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
//		OBDatabase db = new OBDatabase();
//		
//		try
//		{
//			db.openDB();
//			
//			new OBAdcManagementImpl().waitUntilAdcAvailable(adcIndex, db);
//			try
//			{
//				downloadSlbConfig(adcIndex, true, db);
//			}
//			catch(OBException e)
//			{
//				throw e;
//			}
//			catch(Exception e)
//			{
//				throw e;
//			}
//			finally
//			{
//				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex, db);
//			}
//			
//			// ADC 장비의 상태 업데이트.
//			new OBAdcMonitorDB().writeAdcStatus(adcIndex, OBDefine.ADC_STATUS.REACHABLE, db);
//		}
//		catch(OBException e)
//		{
//			throw e;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}		
//		finally
//		{
//			if(db!=null) db.closeDB();
//		}
//
//		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
//	}

	@Override
	public OBDtoSLBUpdateStatus updateSLBStatus(Integer adcIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		OBDtoSLBUpdateStatus retVal = new OBDtoSLBUpdateStatus();
		try {
			retVal = new OBAdcVServerAlteon().downloadSlbConfig(adcIndex, false);
			// ADC 장비의 상태 업데이트.
			new OBAdcMonitorDB().writeAdcStatus(adcIndex, OBDefine.ADC_STATUS.REACHABLE);
		} catch (OBExceptionUnreachable e) {
			new OBAdcMonitorDB().writeAdcStatus(adcIndex, OBDefine.ADC_STATUS.UNREACHABLE);
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			new OBAdcMonitorDB().writeAdcStatus(adcIndex, OBDefine.ADC_STATUS.UNREACHABLE);
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
		return retVal;
	}

	@Override
	public Integer searchVServerListAlteonCount(Integer adcIndex, String searchKey) throws OBException {
		return new OBVServerDB().searchVServerListAlteonCount(adcIndex, searchKey);
	}

	@Override
	public Integer searchVServerListF5Count(Integer adcIndex, String searchKey) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public Integer searchVServerListPASCount(Integer adcIndex, String searchKey) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void syncConifgF5(Integer adcIndex, OBDtoExtraInfo extraInfo) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean isExistVirtualServerAlteon(Integer adcIndex, Integer alteonID) throws OBException {
		OBDatabase db = new OBDatabase();
		boolean retVal = false;
		String sqlText = "";
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX FROM TMP_SLB_VSERVER " + " WHERE ADC_INDEX=%d AND ALTEON_ID=%d ",
					adcIndex, alteonID);
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				retVal = true;
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

	@Override
	public boolean isExistVirtualServerF5(Integer adcIndex, String vsName) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPAS> getVServerListPAS(Integer adcIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

//	@Override
//	public ArrayList<OBDtoAdcVServerPAS> getVServerListPAS(
//			Integer adcIndex, Integer beginIndex, Integer endIndex)
//			throws OBExceptionUnreachable, OBExceptionLogin, OBException
//	{
//		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
//	}

	@Override
	public OBDtoAdcVServerPAS getVServerPASInfo(Integer adcIndex, String vsIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void addVServerPAS(OBDtoAdcVServerPAS virtualServer, OBDtoExtraInfo extraInfo)
			throws OBLicenseExpiredException, OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void setVServerPAS(OBDtoAdcVServerPAS virtualServer, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcPoolPAS> getPoolPASList(Integer adcIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public OBDtoAdcPoolPAS getPoolPAS(String poolIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean isExistVirtualServerPAS(Integer adcIndex, String vsName) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPASK> getVServerListPASK(Integer adcIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

//	@Override
//	public ArrayList<OBDtoAdcVServerPASK> getVServerListPASK(Integer adcIndex,
//			Integer beginIndex, Integer endIndex)
//			throws OBExceptionUnreachable, OBExceptionLogin, OBException
//	{
//		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
//	}

	@Override
	public OBDtoAdcVServerPASK getVServerPASKInfo(Integer adcIndex, String vsIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void addVServerPASK(OBDtoAdcVServerPASK virtualServer, OBDtoExtraInfo extraInfo)
			throws OBLicenseExpiredException, OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void setVServerPASK(OBDtoAdcVServerPASK virtualServer, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcPoolPASK> getPoolPASKList(Integer adcIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public OBDtoAdcPoolPASK getPoolPASK(String poolIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcNodePASK> getNodeAvailableListPASK(Integer adcIndex, String poolIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean isAvailableVirtualServerPASK(Integer adcIndex, String vsName, String ipAddress, Integer port)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

//	@Override
//	public ArrayList<OBDtoAdcVServerPASK> searchVServerListPASK(
//			Integer adcIndex, String searchKey, Integer beginIndex,
//			Integer endIndex) throws OBException
//	{
//		throw new OBException("not supported function");
//	}

	@Override
	public Integer searchVServerListPASKCount(Integer adcIndex, String searchKey) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean isExistVirtualServerPASK(Integer adcIndex, String vsName) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean isExistVirtualServer(Integer adcIndex, ArrayList<String> vsIndexList) throws OBException {
		try {
			return new OBVServerDB().isExistVirtualServer(adcIndex, vsIndexList);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public String getValidVSIndex(Integer adcIndex, String vsIndex) throws OBException {
		String retVal = vsIndex;
		try {
			HashMap<String, String> vsMap = new OBVServerDB().getVSIDListAlteon(adcIndex);
			if (vsMap.get(vsIndex) != null) {// 현재 사용하고 있는 ID인 경우임. 255부터 순차적으로 비교하여 비어 있는 아이디를 찾는다.
				for (int i = 255; i < 1024; i++) {
					if (vsMap.get(i) == null) {
						retVal = Integer.toString(i);
						break;
					}
				}
			}
		} catch (OBException e) {
			throw e;
		}
		return retVal;
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public String getValidPoolIndex(Integer adcIndex, String poolIndex) throws OBException {
		String retVal = poolIndex;
		try {
			HashMap<String, String> vsMap = new OBVServerDB().getPoolIDListAlteon(adcIndex);
			if (vsMap.get(poolIndex) != null) {// 현재 사용하고 있는 ID인 경우임. 255부터 순차적으로 비교하여 비어 있는 아이디를 찾는다.
												// 2014.03.17 255-> 1로 수정하여 1부터 순처적으로 비교하도록 변경함.
				for (int i = 1; i < 1024; i++) {
					if (vsMap.get(i) == null) {
						retVal = Integer.toString(i);
						break;
					}
				}
			}
		} catch (OBException e) {
			throw e;
		}
		return retVal;
	}

	@Override
	public boolean isExistVSIPAddress(Integer adcIndex, String vsIPAddress) throws OBException {
		boolean retVal = new OBVServerDB().isExistVSIPAddress(adcIndex, vsIPAddress);
		return retVal;
	}

	@Override
	public ArrayList<OBDtoAdcVServerAlteon> searchVSListUsedByPoolAlteon(Integer adcIndex, Integer poolID)
			throws OBException {
		return new OBVServerDB().searchVSListUsedByPoolAlteon(adcIndex, poolID);
	}

	@Override
	public ArrayList<OBDtoAdcVServerF5> searchVSListUsedByPoolF5(Integer adcIndex, String poolName) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPAS> searchVSListUsedByPoolPAS(Integer adcIndex, String poolName)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPASK> searchVSListUsedByPoolPASK(Integer adcIndex, String poolName)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

//	@Override
//	public ArrayList<OBDtoAdcVServerAlteon> searchVServerListAlteon(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException
//	{
//		return new OBVServerDB().searchVServerListAlteon(adcIndex, accntIndex, searchKey, beginIndex, endIndex, OBDefine.ORDER_TYPE_VSIPADDRESS, OBDefine.ORDER_DIR_ASCEND);
//	}
//
//	@Override
//	public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException
//	{
//		throw new OBException("not supported function");
//	}
//
//	@Override
//	public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException
//	{
//		throw new OBException("not supported function");
//	}
//
//	@Override
//	public ArrayList<OBDtoAdcVServerPASK> searchVServerListPASK(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException
//	{
//		throw new OBException("not supported function");
//	}
	@Override
	public ArrayList<OBDtoAdcHealthCheckAlteon> getHealthCheckListAlteon(Integer adcIndex) throws OBException {
		try {
			// 로컬DB에서 읽어 처리한다.
			return new OBVServerDB().getHealthCheckListAlteon(adcIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public ArrayList<OBDtoAdcHealthCheckPASK> getHealthCheckListPASK(Integer adcIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public Integer searchVServerListAlteonCount(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		return new OBVServerDB().searchVServerListAlteonCount(adcIndex, accntIndex, searchKey);
	}

	@Override
	public Integer searchVServerListAllListCount(OBDtoAdcScope scope, Integer accntIndex, String searchKey)
			throws OBException {
		return new OBVServerDB().searchVServerAllListCoreCount(scope, accntIndex, searchKey);
	}

	@Override
	public Integer searchVServerListF5Count(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public Integer searchVServerListPASCount(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public Integer searchVServerListPASKCount(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public OBDtoAdcHealthCheckPASK getHealthCheckPASK(String healthcheckIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerAlteon> getVServerListAlteon(Integer adcIndex, Integer beginIndex, Integer endIndex,
			Integer orderType, Integer orderDir) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		// DB에서 ADC 정보를 읽어들인다.
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start"));

		ArrayList<OBDtoAdcVServerAlteon> retVal;
		try {
			retVal = new OBVServerDB().getVServerListAllAlteon(adcIndex, beginIndex, endIndex, orderType, orderDir);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", retVal));

		return retVal;
	}

	@Override
	public ArrayList<OBDtoAdcVServerF5> getVServerListF5(Integer adcIndex, Integer beginIndex, Integer endIndex,
			Integer orderType, Integer orderDir) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPAS> getVServerListPAS(Integer adcIndex, Integer beginIndex, Integer endIndex,
			Integer orderType, Integer orderDir) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPASK> getVServerListPASK(Integer adcIndex, Integer beginIndex, Integer endIndex,
			Integer orderType, Integer orderDir) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerAlteon> searchVServerListAlteon(Integer adcIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		return new OBVServerDB().searchVServerListAlteon(adcIndex, searchKey, beginIndex, endIndex, orderType,
				orderDir);
	}

	@Override
	public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex, String searchKey, Integer beginIndex,
			Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(Integer adcIndex, String searchKey, Integer beginIndex,
			Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPASK> searchVServerListPASK(Integer adcIndex, String searchKey, Integer beginIndex,
			Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerAlteon> searchVServerListAlteon(Integer adcIndex, Integer accntIndex,
			String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir)
			throws OBException {
		return new OBVServerDB().searchVServerListAlteon(adcIndex, accntIndex, searchKey, beginIndex, endIndex,
				orderType, orderDir);
	}

	@Override
	public ArrayList<String> getVsNameList(Integer adcIndex) throws OBException {
		return new OBVServerDB().getVsNameList(adcIndex);
	}

	@Override
	public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPASK> searchVServerListPASK(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcProfile> getProfileList(Integer adcIndex, Integer orderType, Integer orderDir)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<String> getPoolIndexListAlteon(Integer adcIndex) throws OBException {
		ArrayList<String> retVal;
		try {
			retVal = new OBVServerDB().getPoolIndexListAlteon(adcIndex);
		} catch (OBException e) {
			throw e;
		}
		return retVal;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			System.out.println(new OBAdcVServerAlteon().isPeerOSEqual(3, 4));
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public boolean isPeerOSEqual(Integer adcIndex, Integer peerIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		boolean retVal = true;
		try {
			db.openDB();
			retVal = isPeerOSEqual(adcIndex, peerIndex, db);
		} catch (OBException e) {
			throw e;
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private boolean isPeerOSEqual(Integer adcIndex, Integer peerIndex, OBDatabase db) throws OBException {
		// peer index가 설정되지 않은 경우에는 동일한 경우로, false 를 리턴한다.
		if (peerIndex == null || peerIndex.equals(0))
			return false;

		boolean retVal = true;
		String sqlText;
		sqlText = String.format(" SELECT INDEX, SW_VERSION    \n" + " FROM MNG_ADC                \n"
				+ " WHERE INDEX IN (%d, %d)     \n", adcIndex, peerIndex);
		try {
			ResultSet rs = db.executeQuery(sqlText);
			String activeOS = "";
			String standbyOS = "";
			if (rs.next() == true) {
				activeOS = db.getString(rs, "SW_VERSION");
			} else {
				throw new OBException(OBException.ERRCODE_SYSTEM_DATA_NOT_EXIST, "sw version");
			}
			if (rs.next() == true) {
				standbyOS = db.getString(rs, "SW_VERSION");
			} else {
				throw new OBException(OBException.ERRCODE_SYSTEM_DATA_NOT_EXIST, "sw version");
			}

			if (activeOS.equals(standbyOS) == true)
				retVal = true;
			else
				retVal = false;
		} catch (Exception e) {
			return retVal;
		}
		return retVal;
	}

	@Override
	public boolean isPeerVrrpValid(Integer adcIndex, Integer peerIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		boolean retVal = true;
		try {
			db.openDB();
			retVal = isPeerVrrpValid(adcIndex, peerIndex, db);
		} catch (OBException e) {
			throw e;
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private boolean isPeerVrrpValid(Integer adcIndex, Integer peerIndex, OBDatabase db) throws OBException {
		return false;
	}

	@Override
	public ArrayList<OBDtoAdcNodeDetail> searchNodeListF5(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void setNodeState(Integer adcIndex, ArrayList<OBDtoAdcNodeF5> nodeList, Integer action,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d, vsIndexList:%s, threadID:%d",
				adcIndex, nodeList.toString(), Thread.currentThread().getId()));
		boolean isSuccessful = false;
		OBDtoAdcInfo adcInfo = null;
		String nodeString = "";
		try {

			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			// 기본적인 값 유효성 확인
			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "setNodeState() error: adcInfo null.");
			}
			if (nodeList == null || nodeList.size() == 0) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						"setNodeState() error: nodeList is null or empty.");
			}

			// 작업 중간에 설정 간섭이 있었는지 확인
			if (timeSync(adcInfo)) {
//              throw new OBException(OBException.ERRCODE_SLB_VS_TIME, "adc_index: "+adcInfo.getIndex());
				throw new OBException(OBException.ERRCODE_SLB_VS_TIME, "Sync failed. adc_index: " + adcInfo.getIndex());
			}

			setState(adcInfo.getIndex(), nodeList, action, extraInfo);

			// 감사로그 기록
			for (OBDtoAdcNodeF5 node : nodeList) {
				String nodeState = OBParser.getstate(action);
				nodeString += (", " + node.getIpAddress() + "(" + nodeState + ")");
			}
			nodeString = nodeString.substring(1, nodeString.length());

			isSuccessful = true;
		} catch (OBExceptionUnreachable e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
			// 감사로그
			if (adcInfo != null) {
				if (isSuccessful == true) {
					new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcIndex,
							extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_RSERVER_STATE_SET_SUCCESS,
							adcInfo.getName(), nodeString, null);
				} else {
					new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcIndex,
							extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_RSERVER_STATE_SET_FAIL,
							adcInfo.getName(), nodeString, null);
				}
			}
		}
	}

	@Override
	public ArrayList<OBDtoAdcVServerNotice> searchVServerNoticeOnList(Integer adcIndex, Integer accntIndex,
			String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir)
			throws OBException {
		return new OBVServerDB().getVServerNoticeList(adcIndex, accntIndex, searchKey, true, beginIndex, endIndex,
				orderType, orderDir);
	}

	@Override
	public Integer searchVServerNoticeOnListCount(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		return new OBVServerDB().getVServerNoticeListCount(adcIndex, accntIndex, searchKey, true);
	}

	@Override
	public ArrayList<OBDtoAdcVServerNotice> searchVServerNoticeOffList(Integer adcIndex, Integer accntIndex,
			String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir)
			throws OBException {
		return new OBVServerDB().getVServerNoticeList(adcIndex, accntIndex, searchKey, false, beginIndex, endIndex,
				orderType, orderDir);
	}

	@Override
	public Integer searchVServerNoticeOffListCount(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		return new OBVServerDB().getVServerNoticeListCount(adcIndex, accntIndex, searchKey, false);
	}

	@Override
	public void setVServerNoticeOnF5(Integer adcIndex, ArrayList<OBDtoAdcVServerNotice> vsList,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void setVServerNoticeOffF5(Integer adcIndex, ArrayList<OBDtoAdcVServerNotice> vsList,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean addRealServerGroup(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean setRealServerGroup(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void delRealServerGroup(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcRealServerGroup> searchNodeGrpListF5(Integer adcIndex, Integer accntIndex,
			Integer orderType, Integer orderDir) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean updateRealServerMap(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup,
			ArrayList<OBDtoAdcNodeF5> nodeList) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcNodeDetail> searchGroupListF5(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer orderType, Integer orderDir) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void relashVServerF5(OBDtoAdcVServerF5 virtualServerNew, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcNodeDetail> searchGroupListAlteon(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer orderType, Integer orderDir) throws OBException {
		return new OBVServerDB().searchGroupListAlteon(adcIndex, accntIndex, searchKey, orderType, orderDir);
	}

	@Override
	public ArrayList<OBDtoAdcVlan> getF5VlanList(Integer adcIndex, String vsIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public DtoVlanTunnelFilter getF5VlanFilterList(Integer adcIndex, String vsIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVlan> getF5VlanListAll(Integer adcIndex) throws OBException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<OBDtoAdcNodeDetail> searchNodeListAlteon(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		return new OBVServerDB().searchNodeListAlteon(adcIndex, accntIndex, searchKey, beginIndex, endIndex, orderType,
				orderDir);
	}

	@Override
	public ArrayList<OBDtoAdcNodeDetail> searchNodeListAll(OBDtoAdcScope scope, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		return new OBVServerDB().searchNodeListAll(scope, accntIndex, searchKey, beginIndex, endIndex, orderType,
				orderDir);
	}

	@Override
	public ArrayList<OBDtoAdcVServerAll> searchVServerListAll(OBDtoAdcScope scope, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		return new OBVServerDB().searchVServerListAll(scope, accntIndex, searchKey, beginIndex, endIndex, orderType,
				orderDir);
	}

	@Override
	public Integer searchNodeF5ListCount(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	public Integer searchNodeAlteonListCount(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		return new OBVServerDB().searchNodeAlteonListCountCore(adcIndex, accntIndex, searchKey);
	}

	@Override
	public Integer searchNodeAllListCount(OBDtoAdcScope scope, Integer accntIndex, String searchKey)
			throws OBException {
		return new OBVServerDB().searchNodeAllListCountCore(scope, accntIndex, searchKey);
	}

	@Override
	public boolean timeSyncCheck(Integer adcIndex) {
		Timestamp deviceTime = null;

		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			Timestamp localTime = new OBAdcManagementImpl().getApplyTimeFromDB(adcInfo.getIndex());

			deviceTime = new OBAdcVServerAlteon().getDeviceApplyTime(adcInfo);

			if ((deviceTime != null) && (deviceTime.equals(localTime) == true)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void configSchedule(OBDtoAdcSchedule adcSchedule)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException {
		if (adcSchedule.getChangeObjectType() == OBDefine.CHANGE_OBJECT_VIRTUALSERVER) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
					String.format("calling Alteon configSchedule()... adcIndex : %d", adcSchedule.getAdcIndex()));

			ArrayList<String> vsIndexList = new ArrayList<String>();

			String msg = "";

			OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
			extraInfo.setAccountIndex(adcSchedule.getAccntIndex());
			extraInfo.setClientIPAddress(adcSchedule.getAccntIp());
			extraInfo.setExtraMsg1("");
			extraInfo.setExtraMsg2("");

			try {

				if (adcSchedule.getChangeType() == OBDefine.CHANGE_TYPE_ADD) {
					OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
							String.format("Excute Alteon configSchedule Start-  vs add %s", adcSchedule.getVsIp()));
					addVServerAlteon(adcSchedule.getChunkAlteon().getVsConfig().getVsNew(), extraInfo);
					new OBAdcMonitorDB().writeAdcSlbScheduleStatus(adcSchedule.getIndex(),
							OBDefine.CONFIG_STATE.COMPLETE);

					msg = OBMessages.MSG_SLB_SCHEDULE_ADD;

					OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
							String.format("Excute Alteon configSchedule End-  vs add %s", adcSchedule.getVsIp()));
					OBSystemLog.info(OBDefine.LOGFILE_ADCMON,
							String.format("Excute Alteon SLBSchedule  - vs add %s", adcSchedule.getVsIp()));
				} else if (adcSchedule.getChangeType() == OBDefine.CHANGE_TYPE_DELETE) {
					OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
							String.format("Excute Alteon configSchedule Start-  vs delete %s", adcSchedule.getVsIp()));
					vsIndexList.add(adcSchedule.getChunkAlteon().getVsConfig().getVsNew().getIndex());
					delVServer(adcSchedule.getAdcIndex(), vsIndexList, extraInfo);
					new OBAdcMonitorDB().writeAdcSlbScheduleStatus(adcSchedule.getIndex(),
							OBDefine.CONFIG_STATE.COMPLETE);

					msg = OBMessages.MSG_SLB_SCHEDULE_DELETE;

					OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
							String.format("Excute Alteon configSchedule End-  vs delete %s", adcSchedule.getVsIp()));
					OBSystemLog.info(OBDefine.LOGFILE_ADCMON,
							String.format("Excute Alteon SLBSchedule  - vs delete %s", adcSchedule.getVsIp()));
				} else if (adcSchedule.getChangeType() == OBDefine.CHANGE_TYPE_EDIT) {
					if (adcSchedule.getChangeYN() == OBDefine.CHANGE_TYPE_EDIT) {
						vsIndexList.add(adcSchedule.getChunkAlteon().getVsConfig().getVsNew().getIndex()); // vsIndex를
																											// 목록으로 만든다.
						if (adcSchedule.getChangeYN() == OBDefine.STATE_DISABLE) // 과거가 disable
						{
							OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format(
									"Excute Alteon configSchedule Start-  vs edit(disable) %s", adcSchedule.getVsIp()));
							disableVServer(adcSchedule.getAdcIndex(), vsIndexList, extraInfo);
							new OBAdcMonitorDB().writeAdcSlbScheduleStatus(adcSchedule.getIndex(),
									OBDefine.CONFIG_STATE.COMPLETE);

							msg = OBMessages.MSG_SLB_SCHEDULE_MODIFY;

							OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format(
									"Excute Alteon configSchedule End-  vs edit(disable) %s", adcSchedule.getVsIp()));
							OBSystemLog.info(OBDefine.LOGFILE_ADCMON, String
									.format("Excute Alteon SLBSchedule  - vs edit(disable) %s", adcSchedule.getVsIp()));
						} else {
							OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format(
									"Excute Alteon configSchedule Start-  vs edit(enable) %s", adcSchedule.getVsIp()));
							enableVServer(adcSchedule.getAdcIndex(), vsIndexList, extraInfo);
							new OBAdcMonitorDB().writeAdcSlbScheduleStatus(adcSchedule.getIndex(),
									OBDefine.CONFIG_STATE.COMPLETE);

							msg = OBMessages.MSG_SLB_SCHEDULE_MODIFY;

							OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format(
									"Excute Alteon configSchedule End-  vs edit(enable) %s", adcSchedule.getVsIp()));
							OBSystemLog.info(OBDefine.LOGFILE_ADCMON, String
									.format("Excute Alteon SLBSchedule  - vs edit(enable) %s", adcSchedule.getVsIp()));
						}
					} else {
						OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String
								.format("Excute Alteon configSchedule Start-  vs modify %s", adcSchedule.getVsIp()));
						setVServerAlteon(adcSchedule.getChunkAlteon().getVsConfig().getVsNew(), extraInfo);
						new OBAdcMonitorDB().writeAdcSlbScheduleStatus(adcSchedule.getIndex(),
								OBDefine.CONFIG_STATE.COMPLETE);

						msg = OBMessages.MSG_SLB_SCHEDULE_MODIFY;

						OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String
								.format("Excute Alteon configSchedule End-  vs modify %s", adcSchedule.getVsIp()));
						OBSystemLog.info(OBDefine.LOGFILE_ADCMON,
								String.format("Excute Alteon SLBSchedule  - vs modify %s", adcSchedule.getVsIp()));
					}
				} else {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String
							.format("Alteon configSchedule - Invalid config type vsip : %s", adcSchedule.getVsIp()));
					new OBAdcMonitorDB().writeAdcSlbScheduleStatus(adcSchedule.getIndex(),
							OBDefine.CONFIG_STATE.FAILED);
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							"Error : Nothing to Alteon configSchedule.");
				}

				if (adcSchedule.getNotice() != 0) {
					new OBVServerDB().addMessageToSMS(
							String.format(OBMessages.getMessage(msg), adcSchedule.getNotice()),
							adcSchedule.getSmsReceive());
				}

				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
						String.format("Alteon configSchedule - end : %d", adcSchedule.getAdcIndex()));
			} catch (OBExceptionUnreachable e1) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("Alteon configSchedule - failed system unreachable error : %s vsip : %s",
								e1.getMessage(), adcSchedule.getVsIp()));
				new OBAdcMonitorDB().writeAdcSlbScheduleStatus(adcSchedule.getIndex(), OBDefine.CONFIG_STATE.FAILED);
				throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e1.getMessage());
			} catch (OBExceptionLogin e1) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("Alteon configSchedule - failed system login error : %s vsip : %s",
								e1.getMessage(), adcSchedule.getVsIp()));
				new OBAdcMonitorDB().writeAdcSlbScheduleStatus(adcSchedule.getIndex(), OBDefine.CONFIG_STATE.FAILED);
				throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e1.getMessage());
			} catch (OBException e1) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("Alteon configSchedule - failed system error : %s vsip : %s", e1.getMessage(),
								adcSchedule.getVsIp()));
				new OBAdcMonitorDB().writeAdcSlbScheduleStatus(adcSchedule.getIndex(), OBDefine.CONFIG_STATE.FAILED);
				throw e1;
			} catch (Exception e1) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("Alteon configSchedule - failed system format or null point error : %s vsip : %s",
								e1.getMessage(), adcSchedule.getVsIp()));
				new OBAdcMonitorDB().writeAdcSlbScheduleStatus(adcSchedule.getIndex(), OBDefine.CONFIG_STATE.FAILED);
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e1.getMessage());
			}
		}
	}

	@Override
	public void setVServerNoticeOnAlteon(Integer adcIndex, ArrayList<OBDtoAdcVServerNotice> vsList,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("virtual server notice-on BEGIN: vs list = %s", vsList));
		setVServerNoticeOnOff(adcIndex, vsList, extraInfo, OBDefine.CHANGE_TYPE_EDIT_NOTICEON); // notice on
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "virtual server notice-on END");
	}

	@Override
	public void setVServerNoticeOffAlteon(Integer adcIndex, ArrayList<OBDtoAdcVServerNotice> vsList,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("virtual server notice-off BEGIN: vs list = %s", vsList));
		setVServerNoticeOnOff(adcIndex, vsList, extraInfo, OBDefine.CHANGE_TYPE_EDIT_NOTICEOFF); // notice off
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "virtual server notice-off END");
	}

	private void setVServerNoticeOnOff(int adcIndex, ArrayList<OBDtoAdcVServerNotice> vsList, OBDtoExtraInfo extraInfo,
			int changeType) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		// 지정된 virtual server를 삭제하고, 재 등록한다.
		OBDtoAdcInfo adcInfo;
		try {
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
		} catch (OBException e1) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("todo %s", e1.getMessage()));
		}
		if (adcInfo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "invalid adc info");
		}

		if (timeSync(adcInfo) == true) {
			throw new OBException(OBException.ERRCODE_SLB_VS_TIME, "Sync failed. adc_index: " + adcInfo.getIndex());
		}

		// alteon 모델별 설정.
		OBAdcAlteonHandler alteon = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());

		// 변경이력 준비 끝
		// 텔넷 연결.
		alteon.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
				adcInfo.getConnService(), adcInfo.getConnPort());
		try {
			alteon.login();
		} catch (OBExceptionUnreachable e1) {
			throw new OBExceptionUnreachable(e1.getMessage());
		} catch (OBExceptionLogin e1) {
			throw new OBExceptionLogin(e1.getMessage());
		}

		try {
			int vsLength = vsList.size();
			if (changeType == OBDefine.CHANGE_TYPE_EDIT_NOTICEON) {
				for (int i = 0; i < vsLength; i++) {
					String[] noticeIndex = vsList.get(i).getNoticePoolIndex().split("_");
					alteon.changeServicePool(vsList.get(i).getAlteonID(),
							Integer.parseInt(vsList.get(i).getServicePort()), noticeIndex[1]);
				}
			} else {
				for (int i = 0; i < vsLength; i++) {

					alteon.changeServicePool(vsList.get(i).getAlteonID(),
							Integer.parseInt(vsList.get(i).getServicePort()), vsList.get(i).getServicePoolAlteonID());
				}
			}

			alteon.cmndApply();
		} catch (OBException e1) {
			alteon.cmndRevert();
			alteon.disconnect();
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw e1;
		} catch (Exception e1) {
			alteon.cmndRevert();
			alteon.disconnect();
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw new OBException(e1.getMessage());
		}

		try {
			downloadSlbConfig(adcIndex, true);

			// save 작업을 위한 스케줄 등록.
			if (true == new OBEnvManagementImpl().isAlteonAutoSave()) {
				alteon.cmndSave();
			}
		} catch (OBException e1) {
			alteon.cmndRevertApply();
			alteon.disconnect();
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw e1;
		} catch (Exception e1) {
			alteon.cmndRevertApply();
			alteon.disconnect();
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw new OBException(e1.getMessage());
		}

		alteon.disconnect();

		writeVServerNoticeToDb(adcIndex, vsList, changeType, extraInfo.getAccountIndex()); // 공지pool 변경을 DB에 저장

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end adcIndex:%d, %s", adcIndex, vsList));
	}

	// 공지pool 업데이트
	private void writeVServerNoticeToDb(Integer adcIndex, ArrayList<OBDtoAdcVServerNotice> configChunkList,
			int changeType, Integer accntIndex) throws OBException {
		OBVServerDB vserverDB = new OBVServerDB();
		try {
			vserverDB.delDeadVServerNotice(adcIndex);
			vserverDB.updateVServerNoticeChangeAlteon(adcIndex, changeType, configChunkList, accntIndex);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
	}

	// searchVServerNoticeOnList() 테스트 함수, 카운트 테스트 포함
	public void searchVServerNoticeOnListTest() throws OBException {
		Integer adcIndex = 3;
		Integer accntIndex = 1;
		ArrayList<OBDtoAdcVServerNotice> noticeList = null;
		try {
			noticeList = searchVServerNoticeOnList(adcIndex, accntIndex, null, 0, 20, 34, 1);
			System.out.println("count = " + searchVServerNoticeOnListCount(adcIndex, accntIndex, null));
		} catch (OBException e) {
			e.printStackTrace();
			throw e;
		}

		for (OBDtoAdcVServerNotice notice : noticeList) {
			System.out.println(String.format("%s", notice.getVsName()));
		}
	}

	// searchVServerNoticeOffList() 테스트 함수, 카운트 테스트 포함
	public void searchVServerNoticeOffListTest() throws OBException {
		Integer adcIndex = 3;
		Integer accntIndex = 1;
		ArrayList<OBDtoAdcVServerNotice> noticeList = null;
		try {
			noticeList = searchVServerNoticeOffList(adcIndex, accntIndex, null, 0, 19, 15, 1);
			System.out.println("count = " + searchVServerNoticeOffListCount(adcIndex, accntIndex, null));
		} catch (OBException e) {
			e.printStackTrace();
			throw e;
		}

		for (OBDtoAdcVServerNotice notice : noticeList) {
			System.out.println(String.format("%s", notice.getVsName()));
		}
	}

	@Override
	public boolean isVServerSyncNotice(ArrayList<OBDtoAdcVServerNotice> vsList, Integer adcIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

}
