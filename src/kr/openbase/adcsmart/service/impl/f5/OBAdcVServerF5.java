package kr.openbase.adcsmart.service.impl.f5;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;

import iControl.CommonVLANFilterList;
import kr.openbase.adcsmart.service.OBAdcVServer;
import kr.openbase.adcsmart.service.OBSystemAudit;
import kr.openbase.adcsmart.service.adcmond.OBAdcMonitorDB;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcAdditional;
import kr.openbase.adcsmart.service.dto.OBDtoAdcHealthCheckAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeDetail;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcProfile;
import kr.openbase.adcsmart.service.dto.OBDtoAdcRealServerGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSchedule;
import kr.openbase.adcsmart.service.dto.OBDtoAdcScope;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerNotice;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoNetworkInterface;
import kr.openbase.adcsmart.service.dto.OBDtoSLBUpdateStatus;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigChunkF5;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigNodeF5;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigPoolMemberF5;
import kr.openbase.adcsmart.service.impl.OBAccountImpl;
import kr.openbase.adcsmart.service.impl.OBAdcConfigHistoryImpl;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBSystemAuditImpl;
import kr.openbase.adcsmart.service.impl.OBVServerDB;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcConfigSlbF5;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcVServerAll;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcVlan;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcNodePAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcHealthCheckPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcNodePASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBLicenseExpiredException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBAdcVServerF5 implements OBAdcVServer {
	@Override
	public void addVServerF5(OBDtoAdcVServerF5 virtualServerNew, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("virtualServer = %s / extraInfo = %s",
				virtualServerNew.toString(), extraInfo.toString()));

		try {
			// 장비 확인
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(virtualServerNew.getAdcIndex());
			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Fail to get adcInfo.(adcInfo is null)");
			}
			// F5 icontrol interface 준비
			iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);

			// virtual server 중복인지 확인
			if (isAvailableVirtualServerF5(virtualServerNew.getAdcIndex(), virtualServerNew.getName(),
					virtualServerNew.getvIP(), virtualServerNew.getServicePort(), null) == true) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						"VirtualServer add error. VirtualServer exists.");
			}
			validateBaseValues(virtualServerNew);

//			new OBAdcManagementImpl().waitUntilAdcAvailable(virtualServerNew.getAdcIndex());
			// 중요!! 여기부터 decreaseAdcCheckCfg()를 호출 부분까지는 일어날 수 있는 모든 exception에 대해 명시적으로
			// 처리한다.거기서 decreaseAdcCheckCfg()를 호출해야 한다.

			// 변경기록 준비
			OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
			OBDtoAdcConfigChunkF5 configChunk = null;

			configChunk = historyManager.MakeConfigChunkF5(virtualServerNew, OBDefine.CHANGE_BY_USER,
					OBDefine.CHANGE_TYPE_ADD, OBDefine.CHANGE_OBJECT_VIRTUALSERVER, extraInfo.getAccountIndex());
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
					String.format("[vs add] vs name = %s", virtualServerNew.getName()));

			int change = configSlb(configChunk, interfaces);

			if (change != OBDefine.CHANGE_TYPE_NONE) // 바뀐 것이 있을 때만 갱신 및 기록 작업을 한다.
			{
				// 디스크에 저장
				SystemF5.saveHighConfig(interfaces);

				// DB 업데이트
				ArrayList<OBDtoAdcVServerF5> vsUpdateList = new ArrayList<OBDtoAdcVServerF5>();
				vsUpdateList.add(virtualServerNew);
				updateAdcConfigSlbVServerAddSet(adcInfo, vsUpdateList); // 변경 영향 부분만 업데이트

				// 변경기록
				historyManager.writeConfigHistoryF5(configChunk);

				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), virtualServerNew.getAdcIndex(),
						extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_ADD_SUCCESS, adcInfo.getName(),
						virtualServerNew.getName(), null);
			} else {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] nothing to do.");
			}
//			new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServerNew.getAdcIndex());
			// 여기까지 decreaseAdcCheckCfg()가 포함된 exception을 처리한다.
		} catch (OBException e) {
			new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServerNew.getAdcIndex());
			throw e;
		} catch (Exception e) {
			new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServerNew.getAdcIndex());
			try {
				CommonF5.Exception("", e.getMessage());
			} catch (OBExceptionUnreachable e1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e1.getMessage());
			} catch (OBExceptionLogin e1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e1.getMessage());
			} catch (OBException e1) {
				throw e1;
			} catch (Exception e1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e1.getMessage());
			}
		}
	}

	// TODO : 여기서부터 exception 작업 다시 해야함. 김윤경
	@Override
	public void setVServerF5(OBDtoAdcVServerF5 virtualServerNew, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "[vs change] begin: new virtual server = " + virtualServerNew);
		OBDatabase db = new OBDatabase();
		try {
			// 장비 확인
			db.openDB();
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(virtualServerNew.getAdcIndex());
			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Fail to get adcInfo.(adcInfo is null)");
			}

			// F5 icontrol interface 준비
			iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);

			// 기본적인 값 유효성 확인
			validateBaseValues(virtualServerNew);

			// 작업 중간에 설정 간섭이 있었는지 확인
			if (timeSyncCheck(adcInfo.getIndex()) == false) {
				// vs 설정 동기화 체크
				OBDtoAdcVServerF5 poolListToUpdate = checkConfigSync(interfaces, virtualServerNew);
				if (poolListToUpdate != null) {
					if (updateSlbPoolmemberCompact(adcInfo.getIndex(), poolListToUpdate) == true) {
						throw new OBException(OBException.ERRCODE_SLB_VS_TIME,
								"Sync finished. ADC: " + adcInfo.getName());
					} else {
						throw new OBException(OBException.ERRCODE_SLB_VS_TIME,
								String.format("Sync failed. local VS != device VS. ADC: %s VS: %s", adcInfo.getName(),
										virtualServerNew.getName()));
					}
				} else // poolListToUpdate == null, 업데이트할게 없음. 설정이 일치함
				{
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format(
							"Apply times are different between local and ADC. But VS(%s)s are identical so process is going!",
							virtualServerNew.getName()));
				}

			} else {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs change] config sync check OK");
			}

//			new OBAdcManagementImpl().waitUntilAdcAvailable(virtualServerNew.getAdcIndex());
			try {
				// 변경기록 준비
				OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
				OBDtoAdcConfigChunkF5 configChunk = null;

				configChunk = historyManager.MakeConfigChunkF5(virtualServerNew, OBDefine.CHANGE_BY_USER,
						OBDefine.CHANGE_TYPE_EDIT, OBDefine.CHANGE_OBJECT_VIRTUALSERVER, extraInfo.getAccountIndex());

				int change = configSlb(configChunk, interfaces);
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs change] config finished well");

				if (change != OBDefine.CHANGE_TYPE_NONE) {
					// 디스크에 저장
					SystemF5.saveHighConfig(interfaces);

					// DB 업데이트
					ArrayList<OBDtoAdcVServerF5> vsUpdateList = new ArrayList<OBDtoAdcVServerF5>();
					vsUpdateList.add(virtualServerNew);
					updateAdcConfigSlbVServerAddSet(adcInfo, vsUpdateList); // 변경 영향 부분만 업데이트

					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs change] udpate config done");
					// 변경기록
					historyManager.writeConfigHistoryF5(configChunk);
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs change] history done.");
					new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), virtualServerNew.getAdcIndex(),
							extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_SET_SUCCESS,
							adcInfo.getName(), virtualServerNew.getName(), null);
				} else {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs change] nothing to do.");
				}
			} catch (OBException e) {
				throw e;
			} catch (Exception e) {
				throw e;
			} finally {
//				new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServerNew.getAdcIndex());
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			try {
				CommonF5.Exception("", e.getMessage());
			} catch (OBExceptionUnreachable e1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e1.getMessage());
			} catch (OBExceptionLogin e1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e1.getMessage());
			} catch (OBException e1) {
				throw e1;
			} catch (Exception e1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e1.getMessage());
			}
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void relashVServerF5(OBDtoAdcVServerF5 virtualServerNew, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "[vs change] begin: new virtual server = " + virtualServerNew);
		OBDatabase db = new OBDatabase();
		try {
			// 장비 확인
			db.openDB();
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(virtualServerNew.getAdcIndex());
			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Fail to get adcInfo.(adcInfo is null)");
			}

			if (adcInfo.getStatus() != OBDefine.ADC_STATUS.REACHABLE) {
				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
						String.format("ADC is not reachable status. can't refresh VS info. adcIndex: %d, adcIP: %s",
								adcInfo.getIndex(), adcInfo.getAdcIpAddress()));
				return;
			}

			// F5 icontrol interface 준비
			iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);

			// 기본적인 값 유효성 확인
			validateBaseValues(virtualServerNew);

			// 작업 중간에 설정 간섭이 있었는지 확인
			if (timeSyncCheck(adcInfo.getIndex()) == false) {
				// vs 설정 동기화 체크
				OBDtoAdcVServerF5 poolListToUpdate = checkConfigSync(interfaces, virtualServerNew);
				if (poolListToUpdate != null) {
					if (updateSlbPoolmemberCompact(adcInfo.getIndex(), poolListToUpdate) == true) {
						OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, "Sync finished. ADC: " + adcInfo.getName());
					} else {
						OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
								String.format("Sync failed. local VS != device VS. ADC: %s VS: %s", adcInfo.getName(),
										virtualServerNew.getName()));
					}
				} else // poolListToUpdate == null, 업데이트할게 없음. 설정이 일치함
				{
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format(
							"Apply times are different between local and ADC. But VS(%s)s are identical so process is going!",
							virtualServerNew.getName()));
				}

			} else {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs change] config sync check OK");
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			try {
				CommonF5.Exception("", e.getMessage());
			} catch (OBExceptionUnreachable e1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e1.getMessage());
			} catch (OBExceptionLogin e1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e1.getMessage());
			} catch (OBException e1) {
				throw e1;
			} catch (Exception e1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e1.getMessage());
			}
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

//	public static void testAddAdc()
//	{
//		OBAdcManagementImpl adcManager = new OBAdcManagementImpl();
//		OBDtoAdcInfo adcInfo = new OBDtoAdcInfo();
//		
//		adcInfo.setName("abc adc");
//		adcInfo.setAdcIpAddress("192.168.200.11");
//		adcInfo.setAdcAccount("admin");
//		adcInfo.setAdcPassword("admin");
//		adcInfo.setAdcType(1); //F5
//		adcInfo.setGroupIndex(1);
//		
//		try
//		{
//			adcManager.addAdcInfo(adcInfo);
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}

//	public static void testUpdateSLBConfig(OBDtoAdcInfo adcInfo)
//	{
//		CommonF5.initInterfaces(adcInfo);
//		
//		try
//		{
//			new OBAdcVServerF5().updateAdcConfigSlb(adcInfo);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//	
//	public static OBDtoAdcInfo testGetFirstF5Adc(Integer accountIndex)
//	{
//		OBAdcManagementImpl adcManager = new OBAdcManagementImpl();
//		int i;
//		
//		ArrayList<OBDtoAdcInfo> adcList=null;
//		try
//		{
//			adcList = adcManager.getAdcInfoList(accountIndex);
//		}
//		catch(OBException e)
//		{
//			System.out.println("testGetFirstF5Adc() Error");
//			e.printStackTrace();
//		}
//		OBDtoAdcInfo adc = null	;
//
//		for(i=0; i<adcList.size(); i++)
//		{
//			adc = adcList.get(i);
//			if(adc.getAdcType() == OBDefine.ADC_TYPE_F5)
//			{
//				break;
//			}
//		}
//		System.out.println("first F5 ADC info = " + adc.getAdcIpAddress() + " / " + adc.getAdcAccount() + " / " + adc.getAdcPasswordDecrypt());
//		return adc;
//	}

//	public static void testGetProfileList(OBDtoAdcInfo adc)
//	{
//		OBAdcVServer vsManager = new OBAdcVServerF5();
//		try
//		{
//			ArrayList<OBDtoAdcProfile> pl = vsManager.getProfileList(adc.getIndex());
//			
//			System.out.println("--- START profile list ---");
//			for(OBDtoAdcProfile p:pl)
//			{
//				System.out.println("   " + p.toString());
//			}
//			System.out.println("--- DONE profile list ---");
//		}
//		catch(OBException e)
//		{
//			System.out.println("testGetProfileList() Error");
//			e.printStackTrace();
//		}
//	}
//
//	public static void testSearchVS(OBDtoAdcInfo adc, String searchKey)
//	{
//		OBAdcVServer vsManager = new OBAdcVServerF5();
//		ArrayList<OBDtoAdcVServerF5> searchRec=null;
//		try
//		{
//			searchRec = vsManager.searchVServerListF5(adc.getIndex(), searchKey);
//		}
//		catch(OBException e)
//		{
//			System.out.println("testSearchVS() Error");
//			e.printStackTrace();
//		}
//		System.out.println("--- START search vritual server ---");
//		for(int i =0; i<searchRec.size(); i++)
//		{
//			System.out.println("   " + i + ")" + searchRec.get(i).getName() + " / " + searchRec.get(i).getvIP());
//		}
//		System.out.println("--- DONE search virtual server ---");
//	}

//	public static void testAddVirtualServer(OBDtoAdcInfo adc)
//	{
//		OBAdcVServer vsManager = new OBAdcVServerF5();
//		long tt1=0, tt2=0;
//		
//		//F5 adc의 첫번째 virtual server를 가져온다.
//		OBDtoAdcVServerF5 vsFirst=null;
//		try
//		{
//			vsFirst = vsManager.getVServerListF5(adc.getIndex()).get(1);
//		}
//		catch(Exception e)
//		{
//			System.out.println("testAddVirtualServer() Error1");
//			e.printStackTrace();
//		}
//	
//		OBDtoAdcVServerF5 new_vs = new OBDtoAdcVServerF5();
//		
//		new_vs.setAdcIndex(vsFirst.getAdcIndex());
//		new_vs.setApplyTime(null);
//		new_vs.setStatus(null);
//		new_vs.setName("a_new_vs");
//		new_vs.setvIP("10.10.10.101");
//		new_vs.setServicePort(25);
//		new_vs.setPersistence(null);
//		new_vs.setUseYN(null);
//		new_vs.setPool(vsFirst.getPool());
//		
//		try
//		{
//			//vsManager.delVServer(adcIndex, newVS.getIndex());
//			tt1 = System.currentTimeMillis();
//			vsManager.addVServerF5(new_vs);
//			tt2 = System.currentTimeMillis();
//			System.out.println("<time> virtual server add: " + (tt2 - tt1)/1000.0 );
//		}
//		catch(Exception e)
//		{
//			System.out.println("testAddVirtualServer() Error2");
//			e.printStackTrace();
//		}
//	}

//	public static void testToggleVirtualServer(OBDtoAdcInfo adc)
//	{
//		OBAdcVServer vsManager = new OBAdcVServerF5();
//		long tt1=0, tt2=0;
//		
//		//F5 adc의 첫번째 virtual server를 가져온다.
//		OBDtoAdcVServerF5 vsFirst=null;
//		try
//		{
//			vsFirst = vsManager.getVServerListF5(adc.getIndex()).get(1);
//		}
//		catch(Exception e)
//		{
//			System.out.println("testToggleVirtualServer() Error1");
//			e.printStackTrace();
//		}
//		
//		//toggle enable/disable
//		ArrayList<String> vsIndexList = new ArrayList<String>();
//		vsIndexList.add(vsFirst.getIndex());
//		
//		try
//		{
//			tt1 = System.currentTimeMillis();
//			if(vsFirst.getUseYN()==OBDefine.STATE_DISABLE)
//			{
//				vsManager.enableVServer(adc.getIndex(), vsIndexList);
//			}
//			else
//			{
//				vsManager.disableVServer(adc.getIndex(), vsIndexList);
//			}
//			tt2 = System.currentTimeMillis();
//			System.out.println("<time> virtual server enable/disable: " + (tt2 - tt1)/1000.0 );
//		}
//		catch(Exception e)
//		{
//			System.out.println("testToggleVirtualServer() Error2");
//			e.printStackTrace();
//		}
//	}
//	public static void testPrintVServerList(ArrayList<OBDtoAdcVServerF5> res)
//	{
//		System.out.println("\nADC index: " + res.get(0).getAdcIndex());
//		System.out.println("config time: " + res.get(0).getApplyTime().toString());
//		
//		for(OBDtoAdcVServerF5 vs: res)
//		{
//			String persist = vs.getPersistence();
//
//			System.out.println("---------------------------------------------");
//			System.out.println(vs.getName() + "(status:"+ vs.getStatus() + ")" 
//				+ "\n\t" + vs.getvIP() + ":" + vs.getServicePort()
//				+ "\n\tdefault pool: " + vs.getPool().getName() 
//				+ "\n\tdefault persistence profile: " + persist 
//			);
//		}
//	}
	private void PoolMemberReplace(Pool poolWorker, PoolMember poolMemberWorker, OBDtoAdcPoolF5 newPool)
			throws Exception {
		// pool 멤버는 전체를 새 조합으로 대체한다. 삭제->추가
		poolWorker.removeMemberAll();
		poolWorker.addMembers(newPool.getMemberList());
	}

	private void PoolMemberReplaceCompact(Pool poolWorker, PoolMember poolMemberWorker,
			ArrayList<OBDtoAdcConfigPoolMemberF5> poolMemberConfigList) throws Exception {
		// pool 멤버를 바뀐것만 빼고 넣는다.
		ArrayList<OBDtoAdcPoolMemberF5> memberDelList = new ArrayList<OBDtoAdcPoolMemberF5>();
		ArrayList<OBDtoAdcPoolMemberF5> memberAddList = new ArrayList<OBDtoAdcPoolMemberF5>();

		for (OBDtoAdcConfigPoolMemberF5 member : poolMemberConfigList) {
			if (member.getChange() == OBDefine.CHANGE_TYPE_DELETE) {
				memberDelList.add(member.getMemberOld());
			} else if (member.getChange() == OBDefine.CHANGE_TYPE_ADD) {
				memberAddList.add(member.getMemberNew());
			}
		}

		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "pool member del list = " +
		// memberDelList.toString());
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "pool member add list = " +
		// memberAddList.toString());

		if (memberDelList != null) {
			poolWorker.removeMembers(memberDelList);
		}
		if (memberAddList != null) {
			poolWorker.addMembers(memberAddList); // 추가할 것만 추가한다.
		}
	}

	private void PoolMemberStateReset(PoolMember poolMemberWorker, ArrayList<OBDtoAdcPoolMemberF5> memberList)
			throws Exception {
		// pool member들의 상태를 쭉 다시 설정한다.
		poolMemberWorker.setMemberState(memberList);
	}

	private int configSlb(OBDtoAdcConfigChunkF5 configChunk, iControl.Interfaces interfaces)
			throws OBException, Exception {
		VirtualServer vs;
		Pool poolWorker;
		PoolMember poolMemberWorker;
		OBVServerDB vsDB = new OBVServerDB();

		OBDtoAdcPoolF5 poolNew = configChunk.getVsConfig().getPoolConfig().getPoolNew();
		Integer roleNo = new OBAccountImpl().getAccountRoleNo(configChunk.getAccountIndex());
		try // 1 - pool 작업
		{
			if (configChunk.getVsConfig().getPoolConfig().getChange() == OBDefine.CHANGE_TYPE_ADD) // pool 추가
			{
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[pool add]add new pool");

				poolWorker = new Pool(interfaces, poolNew.getName());
				poolMemberWorker = new PoolMember(interfaces, poolNew.getName());

				poolWorker.create(CommonF5.valueInteger2LBMethod(poolNew.getLbMethod()), null);

				// pool member 변경을 먼저 적용해야한다. health check에서 member port의 영향을 받기 때문에 먼저 해야한다.
				PoolMemberReplace(poolWorker, poolMemberWorker, poolNew); // compact하게 할 것이 없으므로 무조건 넣는 방법을 호출한다.
				// state 재설정
				PoolMemberStateReset(poolMemberWorker, poolNew.getMemberList());

				// health monitor
				if (poolNew.getHealthCheck().equals(OBDefine.HEALTH_CHECK.NONE) == false
						&& poolNew.getHealthCheck().equals(OBDefine.COMMON_NOT_ALLOWED) == false) {
					String[] monitorTemplates = { CommonF5.valueInt2LocalLBMonitorTemplate(poolNew.getHealthCheck()) };
					poolWorker.setHealthMonitor(iControl.LocalLBMonitorRuleType.MONITOR_RULE_TYPE_SINGLE, 0,
							monitorTemplates);
				}
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "pool [" + poolNew.getName() + "] done.");
			} else if (configChunk.getVsConfig().getPoolConfig().getChange() == OBDefine.CHANGE_TYPE_EDIT) // 있는 pool 수정
			{
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[pool edit]edit existing pool");
				poolWorker = new Pool(interfaces, poolNew.getName());
				poolMemberWorker = new PoolMember(interfaces, poolNew.getName());

				// health monitor를 잠깐 clear한다. 놔 두면 poolmember의 port wildcard와 충돌 날 수 있다.
				if (poolNew.getHealthCheck().equals(OBDefine.COMMON_NOT_ALLOWED) == false) {
					poolWorker.removeHealthMonitor();
				}

				if (configChunk.getVsConfig().getPoolConfig().getMemberChange() == OBDefine.CHANGE_TYPE_EDIT) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[pool edit]member edit");

					if (roleNo == OBDefine.ACCNT_ROLE_RSADMIN) {
						// pool member 변경을 적용한다. 바뀐 것들만
						PoolMemberStateReset(poolMemberWorker, poolNew.getSelectMemberList());
					} else {

						PoolMemberReplaceCompact(poolWorker, poolMemberWorker,
								configChunk.getVsConfig().getPoolConfig().getMemberConfigList());
						PoolMemberStateReset(poolMemberWorker, poolNew.getMemberList());
					}
				}

				// health monitor는 앞에서 이미 지웠기때문에 무조건 작업해야 한다.
				if (configChunk.getVsConfig().getPoolConfig().getHealthCheckChange() == OBDefine.CHANGE_TYPE_ADD) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[pool edit]health check add"); // 추가는 Not allowed 확인 안 한다
					String[] monitorTemplates = { CommonF5.valueInt2LocalLBMonitorTemplate(poolNew.getHealthCheck()) };
					poolWorker.setHealthMonitor(iControl.LocalLBMonitorRuleType.MONITOR_RULE_TYPE_SINGLE, 0,
							monitorTemplates);
				} else if (configChunk.getVsConfig().getPoolConfig()
						.getHealthCheckChange() == OBDefine.CHANGE_TYPE_DELETE) // 이미 지워서 할 건 없다.
				{
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[pool edit]health check DELETE");
				} else // edit / none 모두 작업한다.
				{
					if (configChunk.getVsConfig().getPoolConfig().getHealthCheckChange() == OBDefine.CHANGE_TYPE_EDIT) {
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[pool edit]health check edit");
					}

					if (poolNew.getHealthCheck() == OBDefine.HEALTH_CHECK.NONE) {
						// poolWorker.removeHealthMonitor(); //앞에서 이미 지웠다.
					} else if (poolNew.getHealthCheck().equals(OBDefine.COMMON_NOT_ALLOWED) == false) {
						String[] monitorTemplates = {
								CommonF5.valueInt2LocalLBMonitorTemplate(poolNew.getHealthCheck()) };
						poolWorker.setHealthMonitor(iControl.LocalLBMonitorRuleType.MONITOR_RULE_TYPE_SINGLE, 0,
								monitorTemplates);
					}
				}

				if (configChunk.getVsConfig().getPoolConfig().getLbMethodChange() == OBDefine.CHANGE_TYPE_EDIT) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[pool edit]lbmethod edit");
					poolWorker.setLBMethod(CommonF5.valueInteger2LBMethod(poolNew.getLbMethod())); // load balancing
																									// method만 다시 설정
				}
			} else if (configChunk.getVsConfig().getPoolConfig().getChange() == OBDefine.CHANGE_TYPE_DELETE) // 있는 pool
																												// 제거,
																												// pool
																												// 제거는
																												// ADC
																												// Smart에
																												// 없지만
																												// revert
																												// 때문에
																												// 있어야
																												// 한다.
			{
				poolWorker = new Pool(interfaces, configChunk.getVsConfig().getPoolConfig().getPoolOld().getName());
				poolWorker.delete();
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "pool delete - done");
				// poolMemberWorker = new PoolMember(interfaces,
				// configChunk.getVsConfig().getPoolConfig().getPoolOld().getName());
			} else {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "nothing todo for pool");
			}

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "pool done");
		} catch (OBException e) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
					String.format("pool & pool member config error. (message = %s)", e.getMessage()));
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Start rollback.");
			UndoConfig(configChunk, interfaces, 1); // pool 작업 복원하고 //TODO
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Rollback done.");
			throw e;
		} catch (Exception e) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
					String.format("pool & pool member config error. (message = %s)", e.getMessage()));
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Start rollback.");
			UndoConfig(configChunk, interfaces, 1); // pool 작업 복원하고 //TODO
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Rollback done.");
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("pool & pool member config error. (message = %s)", e.getMessage()));
		}

		// name, port, pool, persistence
		try // 2 - virtual server add/edit
		{
			vs = new VirtualServer(interfaces, configChunk.getVsConfig().getVsNew().getName());
			if (configChunk.getVsConfig().getChange() == OBDefine.CHANGE_TYPE_ADD) // virtual server 추가
			{
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add]start");
				// pool은 만들 때 넣어도 되지만 안 지정한다. pool이 없으면 null이 아니라 pool= ""으로 설정해야한다. 명심!!!!!!
				vs.create(configChunk.getVsConfig().getVsNew().getvIP(),
						(long) configChunk.getVsConfig().getVsNew().getServicePort(), "");

				// pool을 create에서 지정해도, vs type이 fastL4이면 create때 준 pool이 날아가서 어차피 다시 해야하므로 따로
				// 설정 한다.
				vs.setDefaultPoolName(configChunk.getVsConfig().getVsNew().getPool().getName());

				// persistence profile 설정
				if (configChunk.getVsConfig().getVsNew().getPersistence() != null) {
					vs.setDefaultPersistenceProfile(
							vsDB.getF5ProfileName(configChunk.getVsConfig().getVsNew().getPersistence()));
				} else // uhurue debug
				{
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs edit]persistence empty");
				}

				// vlan filter 설정
				if (configChunk.getVsConfig().getVsNew().getVlanFilter() != null) {
					ArrayList<String> filterList = configChunk.getVsConfig().getVsNew().getVlanFilter().vlanName;
					String[] vlanList = {};
					if (filterList != null) {
						vlanList = filterList.toArray(new String[filterList.size()]);
					}
					vs.setVlanFilter(configChunk.getVsConfig().getVsNew().getName(),
							configChunk.getVsConfig().getVsNew().getVlanFilter().status, vlanList);
				} else {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs edit]vlan filter empty");
				}
			} else if (configChunk.getVsConfig().getChange() == OBDefine.CHANGE_TYPE_EDIT) // 있는 virtual server 수정
			{
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs edit]start");
				// virtual IP나 port 한 쪽이라도 바뀌었으면 설정한다.
				if (configChunk.getVsConfig().getIpChange() == OBDefine.CHANGE_TYPE_EDIT
						|| configChunk.getVsConfig().getPortChange() == OBDefine.CHANGE_TYPE_EDIT) // vip 바꿨다.
				{
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs edit]vip or vport");
					vs.setVirtualPPort(configChunk.getVsConfig().getVsNew().getvIP(),
							(long) configChunk.getVsConfig().getVsNew().getServicePort());
				}
				// pool 수정
				if (configChunk.getVsConfig().getPoolChange() == OBDefine.CHANGE_TYPE_EDIT
						|| configChunk.getVsConfig().getPoolChange() == OBDefine.CHANGE_TYPE_EDIT_NOTICEON
						|| configChunk.getVsConfig().getPoolChange() == OBDefine.CHANGE_TYPE_EDIT_NOTICEOFF) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
							"[vs edit]pool edit = " + configChunk.getVsConfig().getVsNew().getPool().getName());
					vs.setDefaultPoolName(configChunk.getVsConfig().getVsNew().getPool().getName());
				}
				// persistence profile
				if (configChunk.getVsConfig().getPersistenceChange() == OBDefine.CHANGE_TYPE_ADD) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
							"[vs edit]persistence add = " + configChunk.getVsConfig().getVsNew().getPersistence());
					vs.setDefaultPersistenceProfile(
							vsDB.getF5ProfileName(configChunk.getVsConfig().getVsNew().getPersistence())); // 기존
																											// persistence
																											// profile을
																											// 제거하고 새로
																											// default
																											// 설정
				} else if (configChunk.getVsConfig().getPersistenceChange() == OBDefine.CHANGE_TYPE_DELETE) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
							"[vs edit]persistence delete = " + configChunk.getVsConfig().getVsOld().getPersistence());
					vs.removeAllPersistenceProfile(); // 기존 persistence profile을 제거
				} else if (configChunk.getVsConfig().getPersistenceChange() == OBDefine.CHANGE_TYPE_EDIT) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
							"[vs edit]persistence edit = " + configChunk.getVsConfig().getVsNew().getPersistence());

					if (configChunk.getVsConfig().getVsNew().getPersistence()
							.equals(OBDefine.COMMON_NOT_ALLOWED_STR) == false) // not allowed면 건드리지 않음.
					{
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "edit persistence profile=>"
								+ vsDB.getF5ProfileName(configChunk.getVsConfig().getVsNew().getPersistence()));
						vs.removeAllPersistenceProfile();
						vs.setDefaultPersistenceProfile(
								vsDB.getF5ProfileName(configChunk.getVsConfig().getVsNew().getPersistence()));
					}
				}
				// virtual server enable/disable - 기존 Enable/Disable을 여기로 흡수 필요
				if (configChunk.getVsConfig().getUseynChange() == OBDefine.CHANGE_TYPE_EDIT) {
					if (configChunk.getVsConfig().getVsNew().getUseYN() != null) {
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs edit]enable/disable edit : change to "
								+ configChunk.getVsConfig().getVsNew().getUseYN());
						String[] vsNames = { configChunk.getVsConfig().getVsNew().getName() };
						// State 설정
						VirtualServer.setStatusEnableDisable(interfaces, vsNames,
								configChunk.getVsConfig().getVsNew().getUseYN());// enable:state=1, disable:state=0
					} else // edit이라고 찍히더라도, config 설정 자체 변경인 경우에는 useyn 값이 없게 되고, 이 때는 사실상 edit이 아니므로 안
							// 한다.
					{
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs edit]enable/disable edit : nothing to do");
					}
					if (roleNo != OBDefine.ACCNT_ROLE_RSADMIN) {
						// vlan filter 설정
						if (configChunk.getVsConfig().getVsNew().getVlanFilter() != null) {
							ArrayList<String> filterList = configChunk.getVsConfig().getVsNew()
									.getVlanFilter().vlanName;
							String[] vlanList = {};
							if (filterList != null) {
								vlanList = filterList.toArray(new String[filterList.size()]);
							}

							vs.setVlanFilter(configChunk.getVsConfig().getVsNew().getName(),
									configChunk.getVsConfig().getVsNew().getVlanFilter().status, vlanList);
						} else {
							OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs edit]vlan filter empty");
						}
					}
				}
			}
			// snat automap 설정 - 미확정 기능
			// vs.setSnatAutomap();
			if (configChunk.getVsConfig().getChange() != OBDefine.CHANGE_TYPE_NONE
					|| configChunk.getVsConfig().getPoolConfig().getChange() != OBDefine.CHANGE_TYPE_NONE) // assigned
																											// pool이 안
																											// 바뀌어도
																											// pool의 속성이
																											// 바뀔 수 있으므로
																											// 이렇게 체크
			{
				return OBDefine.CHANGE_TYPE_EDIT;
			} else {
				return OBDefine.CHANGE_TYPE_NONE;
			}
		} catch (OBException e) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
					String.format("virtual server config error. (message = %s)", e.getMessage()));
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Start rollback.");
			UndoConfig(configChunk, interfaces, 2); // pool 작업 복원하고 //TODO
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Rollback done.");
			throw e;
		} catch (Exception e) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
					String.format("virtual server config error. (message = %s)", e.getMessage()));
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Start rollback.");
			UndoConfig(configChunk, interfaces, 2); // pool 작업 복원하고 //TODO
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Rollback done.");
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("virtual server config error. (message = %s)", e.getMessage()));
		}
	}

	private void UndoConfig(OBDtoAdcConfigChunkF5 configChunk, iControl.Interfaces interfaces, int undoLevel)
			throws OBLicenseExpiredException, OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "start");

		VirtualServer vs = null;
		OBVServerDB vsDB = new OBVServerDB();

		if (configChunk.getChangeObject() != OBDefine.CHANGE_OBJECT_VIRTUALSERVER) {
			return;
		}

		if (configChunk.getUserType() != OBDefine.CHANGE_BY_USER) {
			return;
		}

		if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_NONE) {
			return;
		}
		// revert 조건 검사 끝. 사실 revertable 검사가 빠져있다.

		// 본격적인 revert를 시작한다.
//		if(configChunk.getVsConfig().getChange()==OBDefine)
		if (undoLevel == 2) // all
		{
			try {
				vs = new VirtualServer(interfaces, configChunk.getVsConfig().getVsNew().getName());
			} catch (Exception e) {
				CommonF5.Exception("Virtual server undo preparation error", e.getMessage());
			}
			if (configChunk.getVsConfig().getChange() == OBDefine.CHANGE_TYPE_ADD) // virtual server 추가했으면 지운다.
			{
				try {
					vs.delete();
				} catch (Exception e) // vs가 추가되지 않았으면 지울 수 없으므로 exception을 에러로 처리하지 않는다.
				{
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
							String.format("undo(add) virtual server del \"%s\" - pass, no such virtual server",
									configChunk.getVsConfig().getVsNew().getName()));
				}
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("undo(add) virtual server del \"%s\" - done",
						configChunk.getVsConfig().getVsNew().getName()));
			} else if (configChunk.getVsConfig().getChange() == OBDefine.CHANGE_TYPE_EDIT) // virtual server 수정을 되돌린다.
			{
				// virtual IP나 port 한 쪽이라도 바뀌었으면 설정한다.
				if (configChunk.getVsConfig().getIpChange() == OBDefine.CHANGE_TYPE_EDIT
						|| configChunk.getVsConfig().getPortChange() == OBDefine.CHANGE_TYPE_EDIT) // vip 바꿨다.
				{
					try {
						vs.setVirtualPPort(configChunk.getVsConfig().getVsOld().getvIP(),
								(long) configChunk.getVsConfig().getVsOld().getServicePort());
					} catch (Exception e) {
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
								String.format("undo(edit) virtual server edit \"%s\" - pass",
										configChunk.getVsConfig().getVsNew().getName()));
					}
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
							String.format("undo(edit) virtual server edit \"%s\" - done",
									configChunk.getVsConfig().getVsNew().getName()));
				}
				// pool 수정
				if (configChunk.getVsConfig().getPoolChange() == OBDefine.CHANGE_TYPE_EDIT
						|| configChunk.getVsConfig().getPoolChange() == OBDefine.CHANGE_TYPE_EDIT_NOTICEON
						|| configChunk.getVsConfig().getPoolChange() == OBDefine.CHANGE_TYPE_EDIT_NOTICEOFF) {
					try {
						vs.setDefaultPoolName(configChunk.getVsConfig().getVsOld().getPool().getName());
					} catch (Exception e) {
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
								String.format("undo(edit) pool replace \"%s\" --> \"%s\" - pass",
										configChunk.getVsConfig().getVsNew().getPool().getName(),
										configChunk.getVsConfig().getVsOld().getPool().getName()));
					}
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
							String.format("undo(edit) pool replace \"%s\" --> \"%s\" - done",
									configChunk.getVsConfig().getVsNew().getPool().getName(),
									configChunk.getVsConfig().getVsOld().getPool().getName()));
				}
				// persistence profile
				if (configChunk.getVsConfig().getPersistenceChange() == OBDefine.CHANGE_TYPE_ADD) {
					try {
						vs.removeAllPersistenceProfile();
					} catch (Exception e) {
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(edit) persistence remove - pass");
					} // 기존 persistence profile을 제거
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(set) persistence remove - done");
				} else if (configChunk.getVsConfig().getPersistenceChange() == OBDefine.CHANGE_TYPE_DELETE) {
					try {
						vs.setDefaultPersistenceProfile(
								vsDB.getF5ProfileName(configChunk.getVsConfig().getVsOld().getPersistence()));
					} catch (Exception e) {
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(edit) persistence add - pass");
					}
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(edit) persistence add - done");
				} else if (configChunk.getVsConfig().getPersistenceChange() == OBDefine.CHANGE_TYPE_EDIT) {
					if (configChunk.getVsConfig().getVsOld().getPersistence()
							.equals(OBDefine.COMMON_NOT_ALLOWED_STR) == false) // not allowed면 건드리지 않음.
					{
						try {
							vs.removeAllPersistenceProfile();
							vs.setDefaultPersistenceProfile(
									vsDB.getF5ProfileName(configChunk.getVsConfig().getVsOld().getPersistence()));
						} catch (Exception e) {
							OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(edit) persistence change - pass");
						}
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(edit) persistence change - done");
					}
				}
				// snat automap 설정 - 미확정 기능
				// vs.setSnatAutomap();
			} else if (configChunk.getVsConfig().getChange() == OBDefine.CHANGE_TYPE_DELETE) // 지운 것을 다시 등록한다.
			{
				OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
				extraInfo.setAccountIndex(0);
				extraInfo.setClientIPAddress("");

				addVServerF5(configChunk.getVsConfig().getVsOld(), extraInfo);
			}
		}

		if (undoLevel >= 1) {
			OBDtoAdcPoolF5 poolNew;
			Pool poolWorker = null;
			PoolMember poolMemberWorker = null;
			if (configChunk.getVsConfig().getPoolConfig().getChange() == OBDefine.CHANGE_TYPE_ADD
					|| configChunk.getVsConfig().getPoolConfig().getChange() == OBDefine.CHANGE_TYPE_EDIT) {
				try {
					poolNew = configChunk.getVsConfig().getPoolConfig().getPoolNew();
					poolWorker = new Pool(interfaces, poolNew.getName());
					poolMemberWorker = new PoolMember(interfaces, poolNew.getName());
				} catch (Exception e) {
					CommonF5.Exception("pool/pool member undo preparation error", e.getMessage());
				}
			}
			// 1 - pool 객체 자체에 대한 작업
			// pool이 추가됐으면 지운다. 멤버도 같이 사라지기 때문에 일부러 지울 필요 없다. 새로 만들어진 노드들은 남는다.
			if (configChunk.getVsConfig().getPoolConfig().getChange() == OBDefine.CHANGE_TYPE_ADD) {
				try {
					poolWorker.delete();
				} catch (Exception e) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(add) pool delete - pass");
				}
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(add) pool delete - done");
			} else if (configChunk.getVsConfig().getPoolConfig().getChange() == OBDefine.CHANGE_TYPE_EDIT) // 있는 pool
																											// 수정됐으면 수정만
																											// 되돌린다.
			{
				// health monitor를 잠깐 clear한다. 놔 두면 poolmember의 port wildcard와 충돌 날 수 있다.
				try {
					poolWorker.removeHealthMonitor();
				} catch (Exception e) {
					OBSystemLog.error(OBDefine.LOGFILE_DEBUG, "undo perperation error - clear health monitor error");
				}

				if (configChunk.getVsConfig().getPoolConfig().getMemberChange() == OBDefine.CHANGE_TYPE_EDIT) {
					// pool member 변경을 반대로 적용한다. del을 add, add는 del
					try {
						PoolMemberReplaceCompact(poolWorker, poolMemberWorker,
								configChunk.getVsConfig().getPoolConfig().getMemberConfigList());
						PoolMemberStateReset(poolMemberWorker,
								configChunk.getVsConfig().getPoolConfig().getPoolOld().getMemberList()); // 과거의 state로
																											// 돌린다.
					} catch (Exception e) {
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(change) poolmember change - pass");
					}
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(change) poolmember change - done");
				}

				// if(configChunk.getPoolHealthCheck()==OBDefine.CHANGE_TYPE_EDIT) //health
				// monitor는 앞에서 이미 지웠기때문에 변경 확인 안하고 바로 작업
				if (configChunk.getVsConfig().getPoolConfig().getPoolOld()
						.getHealthCheck() == OBDefine.HEALTH_CHECK.NONE) {
					// poolWorker.removeHealthMonitor(); //앞에서 이미 지웠다.
				} else if (configChunk.getVsConfig().getPoolConfig().getPoolOld().getHealthCheck()
						.equals(OBDefine.COMMON_NOT_ALLOWED) == false) {
					String[] monitorTemplates = { CommonF5.valueInt2LocalLBMonitorTemplate(
							configChunk.getVsConfig().getPoolConfig().getPoolOld().getHealthCheck()) };
					try {
						poolWorker.setHealthMonitor(iControl.LocalLBMonitorRuleType.MONITOR_RULE_TYPE_SINGLE, 0,
								monitorTemplates);
					} catch (Exception e) {
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(change) pool health monitor change - pass");
					}
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(change) pool health monitor change - done");
				}

				if (configChunk.getVsConfig().getPoolConfig().getLbMethodChange() == OBDefine.CHANGE_TYPE_EDIT) {
					try {
						poolWorker.setLBMethod(CommonF5.valueInteger2LBMethod(
								configChunk.getVsConfig().getPoolConfig().getPoolOld().getLbMethod()));
					} catch (Exception e) {
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(change) pool lbmethod change - pass");
					} // load balancing method만 다시 설정
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(change) pool lbmethod change - done");
				}
			} else if (configChunk.getVsConfig().getPoolConfig().getChange() == OBDefine.CHANGE_TYPE_DELETE) // 있는 pool
																												// 제거,
																												// 근데,
																												// pool
																												// 제거는
																												// ADC
																												// Smart에
																												// 없다.
			{
				// poolWorker = new Pool(interfaces,
				// configChunk.getVsOld().getPool().getName());
				// poolMemberWorker = new PoolMember(interfaces,
				// configChunk.getVsOld().getPool().getName());
			}

			// node 생긴것들 제거
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo node 제거");
			ArrayList<String> nodeDelList = new ArrayList<String>();
			ArrayList<String> nodeAddList = new ArrayList<String>();

			for (OBDtoAdcConfigNodeF5 nodeConfig : configChunk.getVsConfig().getNodeConfigList()) {
				if (nodeConfig.getChange() == OBDefine.CHANGE_TYPE_ADD) {
					nodeDelList.add(nodeConfig.getNodeNew().getIpAddress()); // 추가한 노드들 삭제리스트에 넣음
				} else if (nodeConfig.getChange() == OBDefine.CHANGE_TYPE_DELETE) {
					nodeAddList.add(nodeConfig.getNodeOld().getIpAddress()); // 삭제한 노드들 추가리스트에 넣음
				}
				// else if(nodeConfig.getChange()==OBDefine.CHANGE_TYPE_EDIT) //ADCSmart에는 node
				// 수정이 없으므로 수정에 대한 복원은 일어나지 않는다.
				// {
				// }
			}
			// node 복원작업
			try {
				NodeAddress.delete(interfaces, nodeDelList.toArray(new String[0]));
			} catch (Exception e) {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(add) node delete - pass");
			}
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(add) node delete - done");

			try {
				NodeAddress.create(interfaces, nodeAddList.toArray(new String[0]));
			} catch (Exception e) {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(delete) node create - pass");
			}
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "undo(delete) node create - done");

			poolWorker = null;
			poolMemberWorker = null;
		}
		vs = null;
	}

	public void revertSlbConfig(Integer adcIndex, OBDtoAdcConfigChunkF5 configChunk, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException {
		if (configChunk.getChangeObject() == OBDefine.CHANGE_OBJECT_VIRTUALSERVER) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Revert...");
			// int adcIndex = configChunk.getVsConfig().getVsNew().getAdcIndex();
			ArrayList<String> vsIndexList = new ArrayList<String>();

			if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_ADD) {
				vsIndexList.add(configChunk.getVsConfig().getVsNew().getIndex()); // ADD는 NEW에서 빼서 지운다.
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Revert - vs delete");
				delVServer(adcIndex, vsIndexList, extraInfo);
			} else if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_DELETE) {
				// vsIndexList.add(configChunk.getVsConfig().getVsOld().getIndex()); //add는 복수로
				// 하지 않기 때문에 필요없다.
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Revert - vs add");
				addVServerF5(configChunk.getVsConfig().getVsOld(), extraInfo); // DELETE는 OLD에서 빼서 살린다.
			} else if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_EDIT) {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Revert - edit(including enable/disable)");
				setVServerF5(configChunk.getVsConfig().getVsOld(), extraInfo);
			} else {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Error : Nothing to revert.");
			}
		}
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Revert - end");
	}

	@Override
	public void delVServer(Integer adcIndex, ArrayList<String> vsIndexList, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		try {
			// 장비 기본 확인
			OBDtoAdcInfo adcInfo;
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "invalid adc info");
			}

			// F5 icontrol interface
			iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);

			// 변경기록 준비
			OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
			OBVServerDB vsDB = new OBVServerDB();

			new OBAdcManagementImpl().waitUntilAdcAvailable(adcIndex);
			// 중요!! 여기부터 decreaseAdcCheckCfg()를 호출 부분까지는 일어날 수 있는 모든 exception에 대해 명시적으로
			// 처리한다.거기서 decreaseAdcCheckCfg()를 호출해야 한다

			ArrayList<OBDtoAdcConfigChunkF5> configChunkList = new ArrayList<OBDtoAdcConfigChunkF5>();
			ArrayList<OBDtoAdcVServerF5> changeVsList = new ArrayList<OBDtoAdcVServerF5>(); // 업데이트함수에 넘겨줄 vs 목록
			OBDtoAdcVServerF5 changeVs; // loop 순환 처리용 임시 변수

			for (String vsIndex : vsIndexList) // virtual server old 상태를 잡아 놓는다.
			{
				changeVs = vsDB.getVServerInfoF5(vsIndex);
				changeVsList.add(changeVs);
				configChunkList.add(
						historyManager.MakeConfigChunkF5(changeVs, OBDefine.CHANGE_BY_USER, OBDefine.CHANGE_TYPE_DELETE,
								OBDefine.CHANGE_OBJECT_VIRTUALSERVER, extraInfo.getAccountIndex()));
			}
			// 변경기록 준비 끝
			ArrayList<String> vsNameList = new OBVServerDB().getVirtualServerNameList(vsIndexList);
			String[] vsNames = vsNameList.toArray(new String[vsNameList.size()]);

			VirtualServer.delete(interfaces, vsNames);

			// 디스크에 저장
			SystemF5.saveHighConfig(interfaces);
			changeVsList.get(0).setDeleteYN(1);
			// DB 업데이트
			updateAdcConfigSlbVServerEnaDisDel(adcInfo, changeVsList); // 변경 영향 부분만 업데이트

			for (String name : vsNameList)
				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcIndex,
						extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_DEL_SUCCESS, adcInfo.getName(),
						name, null);

			// 변경기록
			for (OBDtoAdcConfigChunkF5 configChunk : configChunkList) {
				historyManager.writeConfigHistoryF5(configChunk);
			}
			new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex); // 여기까지 decreaseAdcCheckCfg()가 포함된 exception을 처리한다.
		} catch (OBException e) {
			new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
			throw e;
		} catch (Exception e) {
			new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
			try {
				CommonF5.Exception("", e.getMessage());
			} catch (OBExceptionUnreachable e1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e1.getMessage());
			} catch (OBExceptionLogin e1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e1.getMessage());
			} catch (OBException e1) {
				throw e1;
			} catch (Exception e1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e1.getMessage());
			}
		}
	}

	@Override
	public void enableVServer(Integer adcIndex, ArrayList<String> vsIndexList, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		try {

			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			setVServerEnableDisable(adcIndex, vsIndexList, OBDefine.STATE_ENABLE, extraInfo);
			ArrayList<String> tempName = new OBVServerDB().getVirtualServerNameList(vsIndexList);
			for (String name : tempName)
				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcIndex,
						extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_ENABLE_SUCCESS,
						adcInfo.getName(), name, null);
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
	public boolean timeSyncCheck(Integer adcIndex) {
		Timestamp deviceTime = null;
		Timestamp localTime = null;
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			localTime = new OBAdcManagementImpl().getApplyTimeFromDB(adcInfo.getIndex());
			deviceTime = new OBAdcVServerF5().getApplyTimeFromF5(adcInfo);

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "deviceTime = " + deviceTime);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "localTime  = " + localTime);

			if (deviceTime != null && deviceTime.equals(localTime) == true) {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "time synced");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void disableVServer(Integer adcIndex, ArrayList<String> vsIndexList, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		try {
			setVServerEnableDisable(adcIndex, vsIndexList, OBDefine.STATE_DISABLE, extraInfo);
			ArrayList<String> tempName = new OBVServerDB().getVirtualServerNameList(vsIndexList);
			OBDtoAdcInfo adcInfo;
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			for (String name : tempName) {
				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcIndex,
						extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_DISABLE_SUCCESS,
						adcInfo.getName(), name, null);
			}
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

//	@Override
//	public boolean downloadSlbConfig(Integer adcIndex, boolean force)	throws OBExceptionUnreachable, OBExceptionLogin, OBException
//	{
//		boolean bDownload = false;
//		
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			db.openDB();
//			bDownload = downloadSlbConfig(adcIndex, force, db);
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
//		finally
//		{
//			if(db!=null) db.closeDB();
//		}
//		return bDownload;
//	}

	public OBDtoSLBUpdateStatus downloadSlbConfig(Integer adcIndex, boolean isforce)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		String lockFile = getUpdateLockFileName(adcIndex);

		Timestamp deviceTime = null;
		OBDtoAdcInfo adcInfo;
		OBDtoSLBUpdateStatus slbUpdateInfo = new OBDtoSLBUpdateStatus();

		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("try to download Slb. threadID:%d, adcIndex:%d",
				Thread.currentThread().getId(), adcIndex));

		if (OBCommon.isLockFileExist(lockFile) == true) {
			// LockFileExcetion 처리
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
					String.format("download slb config.(already running). threadID:%d, isforce:%b, adcIndex:%d",
							Thread.currentThread().getId(), isforce, adcIndex));
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
			// 통신 상태를 검사할 필요는 없을 듯. 이 함수의 호출 조건은 통신이 된다는 가정임. bwpark. 2015.02.25
//			if(new OBTelnetCmndExecV2().isReachable(adcInfo.getAdcIpAddress(), adcInfo.getConnPort())==false)
//			{
//			  //throw new OBExceptionUnreachable("unreachable");
//                slbUpdateInfo.setUpdateSuccess(false);
//                return slbUpdateInfo;
//			}

			if (false == OBCommon.checkVersionF5(adcInfo.getSwVersion())) {
				// throw new OBException(OBException.ERRCODE_ADC_VERSION);
				slbUpdateInfo.setUpdateSuccess(false);
				return slbUpdateInfo;
			}

			// 강제 다운로드 진행 applytime을 비교하지 않는다.
			if (isforce == true) {
				slbUpdateInfo = downloadCheckSlb(adcInfo, slbUpdateInfo);
				return slbUpdateInfo;
			}

			// 장비의 applyTime 을 추출한다.
			// 모니터링 모드일 경우에는 applyTime 값이 현재 시간으로 설정되도록 한다.

			deviceTime = new OBAdcVServerF5().getApplyTimeFromF5(adcInfo); // 장비의 설정 시각 확인, 장비 연결 테스트도 겸함. 여기서 연결 실패하면
																			// OBExceptionLogin 날림.

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
				slbUpdateInfo = downloadCheckSlb(adcInfo, slbUpdateInfo);
			}

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

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private boolean downloadiControlSlb(OBDtoAdcInfo adcInfo) throws OBException {
		boolean bDownload = false;
		Timestamp deviceTime = null, newApplyTime;

		int adcIndex = (int) adcInfo.getIndex();
		int pool_count = 0;

		long tt0, tt1, tt2, tt3, tt4, tt5, tt6, tt7, tt8, tt9, tt10, tt11, tt12;
		OBVServerDB vserverDB = new OBVServerDB();
		iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);
		OBDtoAdcConfigSlbF5 config = new OBDtoAdcConfigSlbF5();
		ArrayList<ArrayList<DtoPoolMember>> memberListList = new ArrayList<ArrayList<DtoPoolMember>>();
		ArrayList<DtoVirtualServer> vsList = new ArrayList<DtoVirtualServer>();
		int readCount = 0; // 읽은 회수
		tt0 = System.currentTimeMillis();
		try {
			do {
				// 기준점 포착을 위해 구해준다. 시작 직전 시간으로 잡아야 한다. 업데이트가 오래 걸리면 50초도 넘으므로
				// downloadiControlSlb() 뒤에 잡으면 놓쳐서 설정 불일치가 생길 확률이 비교적 높다.
				deviceTime = new OBAdcVServerF5().getApplyTimeFromF5(adcInfo);

				tt1 = System.currentTimeMillis();
				OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("<time> ID# %d: START / ADC=%s(%s)", tt1 / 100,
						adcInfo.getName(), adcInfo.getAdcIpAddress()));

				// ADC에서 데이터 추출
				// OBDtoAdcConfigSlbF5 config getVServerListFromF5(adcInfo);
				config = new OBDtoAdcConfigSlbF5();

				config.setVlanList(Vlan.getAll(interfaces));

				tt2 = System.currentTimeMillis();
				OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time> --- Vlan.getAll(): " + (tt2 - tt1) / 1000.0);

				// persistence profile
				config.setPersistenceProfiles(ProfilePersistence.getAll(interfaces));

				tt3 = System.currentTimeMillis();
				OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
						"<time> --- ProfilePersistence.getAll(): " + (tt3 - tt2) / 1000.0);

				// node
				config.setRealServers(NodeAddress.getAll(interfaces));
				tt4 = System.currentTimeMillis();
				OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time> --- NodeAddress.getAll(): " + (tt4 - tt3) / 1000.0);

				// pool
				config.setServerPools(Pool.getAll(interfaces));
				tt5 = System.currentTimeMillis();
				OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time> --- Pool.getAll(): " + (tt5 - tt4) / 1000.0);

				pool_count = config.getServerPools().size();
				// pool 이름 Array 만들기, pool member 한꺼번에 구하려면 필요
				String[] pool_names = new String[pool_count];

				for (int i = 0; i < pool_count; i++) {
					pool_names[i] = config.getServerPools().get(i).getName();
				}
				// pool member - 리스트의 리스트(2중ArrayList)로 들고와야 한다.

				memberListList = PoolMember.get(interfaces, pool_names);
				tt6 = System.currentTimeMillis();
				OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time> --- Pool.getAllMembers(): " + (tt6 - tt5) / 1000.0);

				// virtual server 추가
				vsList = VirtualServer.getAll(interfaces);
				tt7 = System.currentTimeMillis();
				OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time> --- VirtualServer.getAll(): " + (tt7 - tt6) / 1000.0);

				// ssl 인증서 목록 가져오기 : 2013.1.22 ykkim 새 기능으로 추가
				config.setSslCertificates(ManagementKeyCertificate.getAll(interfaces));
				tt8 = System.currentTimeMillis();
				OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
						"<time> --- ManagementKeyCertificate.getAll(): " + (tt8 - tt7) / 1000.0);

				// peer ip에 대한 active pair 인덱스 추가. 참조.
				// if(ADC에 동기화가 설정된 경우)
//				String[] peers = SystemF5.getPeerAddress(interfaces);

				// peer address list를 가져온다. 없으면 첫번째를 ""으로 한다. Alteon하고 맞춘 것.
//				if (peers.length == 0)
//				{
//					peers[0] = "";
//				}
//				config.setPeerAddress(peers);

				// F5의 설정을 다 읽었으면 그 사이에 applyTime이 바뀌었는지 체크한다.
				newApplyTime = getApplyTimeFromF5(adcInfo); // 업데이트 하고 새로 읽어본다.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format(
						"Download slb [#%d] deviceTime = %s, newApplyTime = %s", readCount, deviceTime, newApplyTime));

				tt9 = System.currentTimeMillis();
				OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
						"<time> --- getPeerAddress(), ApplyTime check: " + (tt9 - tt8) / 1000.0);

				if (adcInfo.getOpMode() == OBDefine.OP_MODE_MONITORING) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
							String.format("Download slb [#%d] stop. Monitoring Mode.", readCount));
					bDownload = true;
					break;
				} else if (deviceTime.equals(newApplyTime) == true) // 같으면 그만한다. 다르면 다시 읽어야 함
				{
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
							String.format("Download slb [#%d] stop. Apply time synched.", readCount));
					bDownload = true;
					break;
				} else {
					OBDateTime.Sleep(30000);
				}
				readCount++;
			} while (true);
		} catch (Exception e) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "SLB config read error:" + e.getMessage());
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, e.getMessage());
		}

		// DB 쓰기. 최신 것이 아니어도 쓴다.
		OBDatabase db = new OBDatabase();
		try {
			tt10 = System.currentTimeMillis();
			StringBuilder transactionQuery = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);
			transactionQuery.append("BEGIN;");

			// Vlan 쓰기 - F5에서만 하는 작업
			vserverDB.delF5VlanAll(adcIndex, transactionQuery);
			vserverDB.addF5VlanAll(adcIndex, config.getVlanList(), transactionQuery);

			// persistence profile 쓰기 - F5에서만 하는 작업
			vserverDB.delF5ProfileAll(adcIndex, transactionQuery);
			vserverDB.addF5ProfileInfoAll(adcIndex, config.getPersistenceProfiles(), transactionQuery);

			// node 쓰기
			vserverDB.delNodeAll(adcIndex, transactionQuery);
			vserverDB.addNodeInfoF5(adcIndex, config.getRealServers(), transactionQuery);

			// pool & poolmember 쓰기 - 중첩 loop을 쓰면 cycle 소모가 적으므로 같이 함.
			vserverDB.delPoolAll(adcIndex, transactionQuery);
			vserverDB.delPoolmemberAll(adcIndex, transactionQuery);
			vserverDB.addPoolAndPoolMemberF5(adcIndex, config.getServerPools(), memberListList, transactionQuery);

			// virtual server 쓰기
			vserverDB.delVServerAll(adcIndex, transactionQuery);
			vserverDB.addVServerInfoF5(adcIndex, vsList, transactionQuery);

			// virtual server vlan Filter 쓰기
			vserverDB.delF5VlanFilterAll(adcIndex, transactionQuery);
			vserverDB.addF5VlanFilterAll(adcIndex, vsList, transactionQuery);

			// ssl certificate 새로 쓰기 - F5만 한다.
			vserverDB.delF5SslCertAll(adcIndex, transactionQuery);
			vserverDB.addF5SslCertAll(adcIndex, config.getSslCertificates(), transactionQuery);

			transactionQuery.append("COMMIT;");

			// DB 작업
			db.openDB();
			db.executeUpdate(transactionQuery.toString());

			tt11 = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time> --- DB write: " + (tt11 - tt10) / 1000.0);

		} catch (OBException e) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "DB write exception1:" + e.getMessage());
			throw e;
		} catch (Exception e) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "DB write exception2:" + e.getMessage());
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		// applyTime을 로컬에 저장한다.
		new OBAdcMonitorDB().updateAdcApplyTime(adcInfo.getIndex(), deviceTime);

		try {
			// status update
			new OBAdcMonitorF5().getSlbStatusAllF5(adcIndex);
			// new OBAdcMonitorDB().writeSlbStatusAllF5(adcIndex, list); //TODO 오류검증 필요

			tt12 = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time> --- status Update: " + (tt12 - tt11) / 1000.0);

			// adcAdditional Info 추가.
			OBDtoAdcAdditional addInfo = new OBDtoAdcAdditional();

			addInfo.setAdcIndex(adcIndex);
			addInfo.setSyncState(OBDefine.STATE_DISABLE);

			vserverDB.updateAdcAdditionalInfo(addInfo);

			// peer ip에 인덱스 추가. 참조.
			// if(ADC에 동기화가 설정된 경우)
//			if(!config.getPeerAddress()[0].isEmpty())
//			{
//				addInfo.setSyncState(OBDefine.STATE_ENABLE);
//				addInfo.setPeerIP(config.getPeerAddress()[0]);
//				vserverDB.updateAdcPairIndex(adcInfo, config.getPeerAddress()[0], db);
//			}

//			else
//			vDB.updateAdcPairIndex(adcInfo, "", db);

			tt12 = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("<time> ID# %d: END, Elapsed=%f / ADC=%s(%s)",
					tt0 / 100, (tt12 - tt0) / 1000.0, adcInfo.getName(), adcInfo.getAdcIpAddress()));
		} catch (OBException e) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "DB write exception1:" + e.getMessage());
			throw e;
		} catch (Exception e) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "DB write exception2:" + e.getMessage());
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, e.getMessage());
		}

		return bDownload;
	}

	private OBDtoSLBUpdateStatus downloadCheckSlb(OBDtoAdcInfo adcInfo, OBDtoSLBUpdateStatus slbUpdateInfo)
			throws Exception {
		boolean bDownload = false;

		if (adcInfo.getOpMode() != OBDefine.OP_MODE_MONITORING) {
			bDownload = downloadiControlSlb(adcInfo);
		} else {
			bDownload = downloadiControlSlb(adcInfo);
		}
		slbUpdateInfo.setUpdateSuccess(bDownload);
		return slbUpdateInfo;
	}

//	private Timestamp getDeviceApplyTime(int adcIndex, OBDatabase db) throws Exception
//	{
//		String sqlText;
//		sqlText = String.format(" SELECT APPLY_TIME                       \n" +
//		                        " FROM TMP_SLB_VSERVER                    \n" +
//		                        " WHERE ADC_INDEX=%d                      \n" +
//		                        " ORDER BY APPLY_TIME DESC LIMIT 1;       \n",
//		                        adcIndex);
//
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
//			throw new Exception(String.format("sqlText:%s. message:%s", sqlText, e.getMessage()));
//		}
//	}
//	
//	// dbcp
//	public Timestamp getDeviceApplyTime(int adcIndex) throws Exception
//	{
//		String sqlText;
//		sqlText = String.format(" SELECT APPLY_TIME                       \n" +
//		                        " FROM TMP_SLB_VSERVER                    \n" +
//		                        " WHERE ADC_INDEX=%d                      \n" +
//		                        " ORDER BY APPLY_TIME DESC LIMIT 1;       \n",
//		                        adcIndex);
//
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
//			throw new Exception(String.format("sqlText:%s. message:%s", sqlText, e.getMessage()));
//		}
//		finally
//		{
//			if(db!=null) db.closeDB();
//		}
//	}
//	
	public Timestamp getApplyTimeFromF5(OBDtoAdcInfo adcInfo) throws Exception {
		if (adcInfo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("invalid adc index:%d", adcInfo));
		}

		if (adcInfo.getOpMode() == OBDefine.OP_MODE_MONITORING)
			return OBDateTime.toTimestamp(OBDateTime.now());

		iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);

		return ManagementDBVariable.getLocalConfigTime(interfaces);
	}

//	private int updateVServerStatusFromF5(Integer adcIndex) throws Exception
//	{
//		OBDtoAdcInfo adcInfo;
//
//		//virtualserver 이름 목록을 DB에서 가져온다. F5에서 가져와도 되는데 시간이 길게 걸린다.
//		String [] vsNames = getVirtualServerNamesFromDB(adcIndex);
//		
//		adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
//
//		if(adcInfo == null)
//		{
//			throw new OBException("Get VirtualServer status error-1. (adcInfo is null)");
//		}
//		
//		//장비에서 VirtualServer 상태를 구하고 DB에 갱신
//		iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);
//
//		ArrayList<OBDtoVServerStatus> statusList = VirtualServer.getVirtualServerStatusList(interfaces, vsNames);
//
//		OBVServerDB vserverDB = new OBVServerDB();
//		
//		vserverDB.updateVServerStatusList(statusList);
//
//		return 0;
//	}

	@Override
	public ArrayList<OBDtoAdcNodeF5> getNodeAvailableListF5(Integer adcIndex) throws OBException {
		try {
			return new OBVServerDB().getNodeAvailableListF5(adcIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public OBDtoAdcPoolF5 getPoolF5(String poolIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "info. poolIndex = " + poolIndex);
		try {
			return new OBVServerDB().getPoolInfoF5(poolIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public ArrayList<OBDtoAdcPoolF5> getPoolF5List(Integer adcIndex) throws OBException {
		try {
			// 로컬DB에서 읽어 처리한다.
			return new OBVServerDB().getPoolListAllF5(adcIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

//	@Override
//	public ArrayList<OBDtoAdcProfile> getProfileList(Integer adcIndex) throws OBException
//	{
//		try
//		{
//			//로컬DB에서 읽어 처리한다. 
//			return new OBVServerDB().getF5ProfileList(adcIndex, null, OBDefine.ORDER_TYPE_FIRST, OBDefine.ORDER_DIR_DESCEND);
//		}
//		catch(OBException e)
//		{
//			throw new OBException(OBException.ERRCODE_SLB_PROFILEGET, e);
//		}
//		catch(Exception e)
//		{
//			OBException e1 = new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//			throw new OBException(OBException.ERRCODE_SLB_PROFILEGET, e1);
//		}
//	}

	@Override
	public OBDtoAdcVServerF5 getVServerF5Info(Integer adcIndex, String index)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		try {
			return new OBVServerDB().getVServerInfoF5(index);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public ArrayList<OBDtoAdcVServerF5> getVServerListF5(Integer adcIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		try {
			return new OBVServerDB().getVServerListAllF5(adcIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
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
	public boolean isExistVirtualServerF5(Integer adcIndex, String vsName) throws OBException {
		OBDatabase db = new OBDatabase();
		boolean retVal = false;
		String sqlText = "";
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX  " + " FROM TMP_SLB_VSERVER " + " WHERE ADC_INDEX=%d AND NAME=%s ",
					adcIndex, OBParser.sqlString(vsName));
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
	public boolean isAvailableVirtualServerF5(Integer adcIndex, String vsName, String ipAddress, Integer port,
			String alteonId) throws OBException {
		try {
			return new OBVServerDB().isExistVirtualServerF5(adcIndex, vsName, ipAddress, port);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

//	@Override
//	public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex,
//			String searchKey) throws OBException
//	{
//		try
//		{
//			return new OBVServerDB().searchVServerListF5(adcIndex, searchKey);
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

	private void setVServerEnableDisable(int adcIndex, ArrayList<String> vsIndexList, int state,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBVServerDB vsDB = new OBVServerDB();
		ArrayList<String> vsTodoList = new ArrayList<String>(); // 실제로 enable/disable 상태가 바뀐 virtual server들의 이름 리스트

		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						"setVServerEnableDisable() error.: adcInfo error(null).");
			}

			iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);

			new OBAdcManagementImpl().waitUntilAdcAvailable(adcIndex);
			// 중요!! 여기부터 decreaseAdcCheckCfg()를 호출 부분까지는 일어날 수 있는 모든 exception에 대해 명시적으로
			// 처리한다.거기서 decreaseAdcCheckCfg()를 호출해야 한다.
			try {
				// 변경기록 준비
				OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
				ArrayList<OBDtoAdcConfigChunkF5> configChunkList = new ArrayList<OBDtoAdcConfigChunkF5>();
				ArrayList<OBDtoAdcVServerF5> changeVsList = new ArrayList<OBDtoAdcVServerF5>(); // 업데이트함수에 넘겨줄 vs 목록
				OBDtoAdcVServerF5 changeVs; // loop 순환 처리용 임시 변수

				try {
					for (String vsIndex : vsIndexList) // virtual server old 상태를 잡아 놓는다.
					{
						changeVs = vsDB.getVServerInfoF5(vsIndex);
						changeVsList.add(changeVs);
						changeVs.setUseYN(state); // 이력 비교를 위해 새 vs가 갖게 될 status를 설정한다. 웹에서는 vsIndex리스트만 주지 newInfo를 주지
													// 않기 때문에 DB에서 읽으면 기대 값을 설정해 줘야 한다.

						OBDtoAdcConfigChunkF5 tempChunk = new OBDtoAdcConfigChunkF5();
						tempChunk = historyManager.MakeConfigChunkF5(changeVs, OBDefine.CHANGE_BY_USER,
								OBDefine.CHANGE_TYPE_EDIT, OBDefine.CHANGE_OBJECT_VIRTUALSERVER,
								extraInfo.getAccountIndex()
						// 0
						);
						if (tempChunk.getVsConfig().getUseynChange() == OBDefine.CHANGE_TYPE_EDIT) // 실제로 변경할 대상만 이력을
																									// 보관한다.
						{
							vsTodoList.add(changeVs.getName());
							configChunkList.add(tempChunk);
						}
					}
				} catch (Exception e) {
					CommonF5.Exception("VirtualServer enable/disable error. Config difference check fail.",
							e.getMessage());
				}

				// 실제로 state가 바뀌는 것들만 작업한다. enable->enable은 안함
				// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "enable/disable todo list = " +
				// vsTodoList.toString());
				String[] vsNames = vsTodoList.toArray(new String[vsTodoList.size()]);
				// 변경기록 준비 끝

				try {
					// State 설정
					VirtualServer.setStatusEnableDisable(interfaces, vsNames, state);// enable:state=1, disable:state=0
					// 디스크에 저장
					SystemF5.saveHighConfig(interfaces);

					// virtual server만 정보 업데이트
					updateAdcConfigSlbVServerEnaDisDel(adcInfo, changeVsList); // 변경 영향 부분만 업데이트
					// 변경기록
					for (OBDtoAdcConfigChunkF5 configChunk : configChunkList) {
						historyManager.writeConfigHistoryF5(configChunk);
					}
				} catch (Exception e) {
					CommonF5.Exception("setVServerEnableDisable() error.", e.getMessage());
				}
			} catch (OBException e) {
				throw e;
			} catch (Exception e) {
				throw e;
			} finally {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}

	public OBDtoAdcVServerF5 checkConfigSync(iControl.Interfaces interfaces, OBDtoAdcVServerF5 virtualServer)
			throws Exception {
		boolean ret = false;
		OBVServerDB vdb = new OBVServerDB();
		OBDtoAdcVServerF5 result = new OBDtoAdcVServerF5();
		OBDtoAdcVServerF5 dbVs = vdb.getVServerInfoF5(virtualServer.getIndex());
		if (dbVs == null) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
					String.format("vs %s doesn't exists on DB.", virtualServer.getName()));
			return result;
		}

		String[] vsNameArray = { virtualServer.getName() }; // device에서 vs 정보를 읽으려면 ip를 array로 바꾼다.
		ArrayList<DtoVirtualServer> deviceVsTempList = VirtualServer.get(interfaces, vsNameArray); // device에서 vs 읽음
		if (deviceVsTempList.size() == 0) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
					String.format("vs %s doesn't exists on F5.", virtualServer.getName()));
			return result; // vs가 device에 없으므로 설정이 불일치
		}

		DtoVirtualServer deviceVsTemp = deviceVsTempList.get(0); // 1개만 구했으므로 1번째를 읽는다.
		OBDtoAdcPoolF5 devicePool = new OBDtoAdcPoolF5();
		ArrayList<OBDtoAdcPoolMemberF5> devicePoolMemberListTemp = new ArrayList<OBDtoAdcPoolMemberF5>();

		if (deviceVsTemp.getPoolName() != null) {
			String[] pools = { deviceVsTemp.getPoolName() };
			ArrayList<OBDtoAdcPoolF5> devicePoolTempList = Pool.get(interfaces, pools);
			ArrayList<ArrayList<DtoPoolMember>> deviceMembersList = PoolMember.get(interfaces, pools);
			devicePool = devicePoolTempList.get(0);
			if (deviceMembersList.get(0) != null) {
				OBDtoAdcPoolMemberF5 devicePoolMemberTemp;
				devicePool.setDtoMemberList(deviceMembersList.get(0));
				for (int i = 0; i < deviceMembersList.get(0).size(); i++) {
					devicePoolMemberTemp = new OBDtoAdcPoolMemberF5(deviceMembersList.get(0).get(i));
					if (devicePoolMemberTemp != null) {
						devicePoolMemberTemp.setIndex(dbVs.getPool().getIndex());
						devicePoolMemberListTemp.add(devicePoolMemberTemp);

					}
				}
			}
		}
		OBDtoAdcVServerF5 deviceVs = new OBDtoAdcVServerF5(deviceVsTemp);
		devicePool.setMemberList(devicePoolMemberListTemp);
		deviceVs.setPool(devicePool);
		// 비교하기 전 persistence profile 보정. DB에는 profile 인덱스로 들어가 있음.
		if (dbVs.getPersistence() != null) {
			dbVs.setPersistence(vdb.getF5ProfileName(dbVs.getPersistence()));
		}

		if (dbVs.getVlanFilter() != null) {
			CommonVLANFilterList[] vlanFilter = VirtualServer.getVlanAndTunnel(interfaces, vsNameArray);

			DtoVlanTunnelFilter vlanTunnel = new DtoVlanTunnelFilter();

			if (vlanFilter[0].getState().toString() == OBDefine.VLAN_ENABLED)
				vlanTunnel.setStatus(OBDefine.VLAN_ENABLE);
			else
				vlanTunnel.setStatus(OBDefine.VLAN_DISABLE);

			String[] vlanList = vlanFilter[0].getVlans();

			if (vlanFilter[0].getState().toString() == OBDefine.VLAN_DISABLED && vlanList.length == 0) {
				vlanTunnel.setStatus(OBDefine.VLAN_ALL);
				ArrayList<String> vlanName = new ArrayList<String>(); // 없어도 빈 vlan으로 리턴한다.
				vlanTunnel.setVlanName(vlanName);
			} else {
				ArrayList<String> vlanName = new ArrayList<String>();
				for (int j = 0; j < vlanList.length; j++) {
					vlanName.add(vlanList[j]);
				}
				vlanTunnel.setVlanName(vlanName);
			}

			deviceVs.setVlanFilter(vlanTunnel);
		}

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "vs in DB = " + dbVs);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "vs in F5 = " + deviceVs);

		ret = dbVs.configEquals(deviceVs); // 두개 vs가 같은지 비교한다. 장비에서 읽어오는 설정 항목들만 체크한다. 인덱스 등 나중에 만들어지는 항목은 device에는 없다.
		if (ret == false) {

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
					String.format("VS '%s' config comparison: device vs != DB vs", dbVs.getName()));
			deviceVs.setIndex(dbVs.getIndex());
			deviceVs.getPool().setIndex(dbVs.getPool().getIndex());
			result = deviceVs;
		} else {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
					String.format("VS '%s' config comparison: device vs == DB vs", dbVs.getName()));
			result = null;
		}
		return result;
	}

	// base value validate
	private void validateBaseValues(OBDtoAdcVServerF5 virtualServer) throws Exception {
		String zeroPortPoolMember = "";
		boolean isPoolMemberAllPort = false;

		// 새 virtual server ip 확인
		if (virtualServer.getvIP() == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "VirtualServer Address is null.");
		}
		if (virtualServer.getvIP().isEmpty()) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "VirtualServer Address is empty.");
		}
		// 새 virtual server port
		if (virtualServer.getServicePort() == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "VirtualServer Port is null.");
		}

		// poolmember가 wildcard port를 갖고 있을 때 특정 healthcheck monitor를 쓸수 없으므로 검사
		// pool이 있을 때만 코드를 탄다.pool없으면 null이므로 참조오류
		if (virtualServer.getPool() != null) {
			ArrayList<OBDtoAdcPoolMemberF5> memberList = virtualServer.getPool().getMemberList();
			if (memberList != null) {
				for (OBDtoAdcPoolMemberF5 member : memberList) {
					if (member != null) {
						if (member.getPort() == 0) {
							isPoolMemberAllPort = true;
							zeroPortPoolMember = member.getIpAddress() + ":" + member.getPort().toString();
							break;
						}
					}
				}
				if (isPoolMemberAllPort == true) {
					if (virtualServer.getPool().getHealthCheck().equals(OBDefine.HEALTH_CHECK.NONE) == false
							&& virtualServer.getPool().getHealthCheck()
									.equals(OBDefine.HEALTH_CHECK.GATEWAY_ICMP) == false) {
						throw new Exception(String.format(
								"Pool member [%s] has a zero-port which can't be associated with defined heath monitor \"%s\".",
								zeroPortPoolMember,
								CommonF5.valueInt2LocalLBMonitorTemplate(virtualServer.getPool().getHealthCheck())));
					}
				}
			}
		}
	}

//	private void updateAdcConfigSlb(OBDtoAdcInfo adcInfo) throws OBException
//	{
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			db.openDB();
//			updateAdcConfigSlb(adcInfo, db);
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}
//		finally
//		{
//			if(db!=null) db.closeDB();
//		}
//	}
//	private static void updateSLBConfigTestF5() //F5 update test
//	{
//		Integer adcIndex = 3;
//		
//		try
//		{
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
//			new OBAdcVServerF5().downloadiControlSlb(adcInfo);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	/**
	 * 원격의 장비에서 SLB 설정을 읽어 들인 후 DB에 저장하여 DB의 내용과 장비의 설정이 같도록 업데이트한다.
	 * 
	 * @param adcInfo -- ADC 장비 정보.
	 * @return true: 업데이트 성공. false: 업데이트 실패.
	 */
	private void updateAdcConfigSlb(OBDtoAdcInfo adcInfo, DtoSlbUpdate updateOption,
			ArrayList<OBDtoAdcVServerF5> vsList, ArrayList<OBDtoAdcNodeF5> nodeList) throws OBException {
		long tt0, tt9;
		tt0 = System.currentTimeMillis();
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("<time> ID# %d:START / ADC=%s(%s)", tt0 / 100,
				adcInfo.getName(), adcInfo.getAdcIpAddress()));

		if (updateOption.isDoNothing() == true) // 선택된 옵션이 없음
		{
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
					String.format("Update condition invalid: no option is selected. Stop update."));
			return;
		}
		OBDatabase db = new OBDatabase();
		try {
			StringBuilder transactionQuery = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);
			transactionQuery.append(" BEGIN; ");

			// node
			if (updateOption.isNodeInfo() == true) {
				updateSlbNode(adcInfo, nodeList, transactionQuery); // 노드만 업데이트, list가 null이면 전체 업데이트
			}
//			tt2 = System.currentTimeMillis();

			// vs
			if (updateOption.isVsInfo() == true) // virtual server만 업데이트, list가 null이면 전체 업데이트
			{
				updateSlbVServer(adcInfo, vsList, transactionQuery);
			}
//			tt3 = System.currentTimeMillis();

			// pool & pool menber
			if (updateOption.isPoolmemberInfo() == true) {
				updateSlbPoolAndMember(adcInfo, vsList, transactionQuery);
			}
			transactionQuery.append(" COMMIT; ");

//			tt4 = System.currentTimeMillis();
			// DB 작업
			db.openDB();
			db.executeUpdate(transactionQuery.toString());
//			tt5 = System.currentTimeMillis();
//			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time> --- slb write to DB: " + (tt5 - tt4) / 1000.0);

			if (updateOption.getStatus().equals(updateOption.STATUS_UPDATE_ALL) == true) {
				updateSlbStatus(adcInfo, vsList);
			} else if (updateOption.getStatus().equals(updateOption.STATUS_UPDATE_VS) == true) {
				// updateSlbStatusVsOnly(adcInfo, vsList);
			}

			tt9 = System.currentTimeMillis();
			// OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time> --- status Update: " +
			// (tt9-tt8)/1000.0 );

			// applyTime 추가.
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("<time> ID# %d: END, Elapsed=%f / ADC=%s(%s)",
					tt0 / 100, (tt9 - tt0) / 1000.0, adcInfo.getName(), adcInfo.getAdcIpAddress()));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
//		new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex, db);
	}

	/**
	 * @param adcInfo
	 * @param vsList
	 * @param transactionQuery
	 * @throws OBException
	 */
	private void updateSlbPoolAndMember(OBDtoAdcInfo adcInfo, ArrayList<OBDtoAdcVServerF5> vsList,
			StringBuilder transactionQuery) throws OBException {
		Integer adcIndex = adcInfo.getIndex();
		int i;
		long tt0, tt1, tt2, tt3;
		boolean bUpdateFull = false;
		if (vsList == null) {
			bUpdateFull = true;
		}

		tt0 = System.currentTimeMillis();// 시작
		iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);

		OBVServerDB vserverDB = new OBVServerDB();
		ArrayList<OBDtoAdcPoolF5> pools;
		String[] pool_names;
		ArrayList<String> poolIndexList = new ArrayList<String>();
		try {
			if (bUpdateFull == true) // update all vs
			{
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
						String.format("Update all pool and member info.(VS list is empty or null.)"));
				pools = Pool.getAll(interfaces);
				// 멤버 구하기 작업에 필요한 pool 이름 목록을 구성
				pool_names = new String[pools.size()];
				for (i = 0; i < pools.size(); i++) {
					pool_names[i] = pools.get(i).getName();
				}
			} else // 지정한 pool만 device에서 update
			{
				ArrayList<String> poolNameList = new ArrayList<String>();
				for (OBDtoAdcVServerF5 vs : vsList) {
					if (vs.getPool() != null) {
						poolNameList.add(vs.getPool().getName());
						poolIndexList.add(vs.getPool().getIndex());
					}
				}
				pool_names = new String[poolNameList.size()];
				for (i = 0; i < poolNameList.size(); i++) {
					pool_names[i] = poolNameList.get(i);
				}
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
						String.format("Update partial pools' info.(pool to update = %s)", poolNameList));

				pools = Pool.get(interfaces, pool_names);
			}
			tt1 = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
					String.format("<time> --- Pool.get(): %f / pool#: %d", (tt1 - tt0) / 1000.0, pools.size()));

			// 멤버 구하는 방법은 공통
			ArrayList<ArrayList<DtoPoolMember>> members = PoolMember.get(interfaces, pool_names);
			tt2 = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
					String.format("<time> --- PoolMember.get(): %f", (tt2 - tt1) / 1000.0));

			if (bUpdateFull == true) // update all vs
			{
				// pool & poolmember 쓰기 - 중첩 loop을 쓰면 cycle 소모가 적으므로 같이 함.
				vserverDB.delPoolAll(adcIndex, transactionQuery);
				vserverDB.delPoolmemberAll(adcIndex, transactionQuery);
				vserverDB.addPoolAndPoolMemberF5(adcIndex, pools, members, transactionQuery);
			} else {
				// pool & poolmember 쓰기 - 중첩 loop을 쓰면 cycle 소모가 적으므로 같이 함.
				vserverDB.delPoolPartial(adcIndex, poolIndexList, transactionQuery);
				vserverDB.delPoolmemberPartial(adcIndex, poolIndexList, transactionQuery);
				vserverDB.addPoolAndPoolMemberF5(adcIndex, pools, members, transactionQuery);
			}
			tt3 = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("<time> --- build query: %f", (tt3 - tt2) / 1000.0));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, e.getMessage());
		}
	}

	/**
	 * 부분업데이트: virtual server 정보, status 업데이트 공지전환, vs enable/disable에 적용
	 * 
	 * @param adcIndex
	 * @param configChunkList
	 * @param changeType
	 * @throws OBException
	 */
	private void updateSlbVServer(OBDtoAdcInfo adcInfo, ArrayList<OBDtoAdcVServerF5> vsList,
			StringBuilder transactionQuery) throws OBException {
		Integer adcIndex = adcInfo.getIndex();

		long tt0, tt1;
		tt0 = System.currentTimeMillis();// 시작
		iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);

		ArrayList<DtoVirtualServer> vsListUpdated;
		OBVServerDB vserverDB = new OBVServerDB();

		try {
			if (vsList == null) // update all vs
			{
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
						String.format("Update all virtual server info.(VS list is null.)"));
				vsListUpdated = VirtualServer.getAll(interfaces);
				// virtual server sql
				vserverDB.delVServerAll(adcIndex, transactionQuery);
				vserverDB.addVServerInfoF5(adcIndex, vsListUpdated, transactionQuery);
			} else // 지정한 vs만 device에서 update
			{
				ArrayList<String> vsIndexList = new ArrayList<String>();
				ArrayList<String> vsNameList = new ArrayList<String>();
				for (OBDtoAdcVServerF5 vs : vsList) {
					vsIndexList.add(vs.getIndex());
					vsNameList.add(vs.getName());
				}
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
						String.format("Update partial virtual server info.(VS to update = %s)", vsNameList));
				// virtual server 업데이트
				String[] vsNameArray = vsNameList.toArray(new String[vsNameList.size()]);
				if (vsList.get(0).getDeleteYN() == 1) {
					vsListUpdated = new ArrayList<DtoVirtualServer>();
				} else {
					vsListUpdated = VirtualServer.get(interfaces, vsNameArray);
				}

				// virtual server sql
				vserverDB.delVServerPartial(adcIndex, vsIndexList, transactionQuery);
				vserverDB.addVServerInfoF5(adcIndex, vsListUpdated, transactionQuery);
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, e.getMessage());
		}
		tt1 = System.currentTimeMillis();
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("<time> --- VirtualServer.get(): %f / #vs: %d",
				(tt1 - tt0) / 1000.0, vsListUpdated.size()));

		// 설정한 노드가 속한 vs들을 구한다. 노드가 바뀐 vs들의 소속 pool 멤버들 status만 업데이트하게.
		// 바뀐 vs의 상태만 업데이트
		// updateSlbStatus(adcInfo, vsIndexList); //실행 시간 로그는 함수 안에서 기록하므로 안한다.
	}

	/**
	 * real server 수정 부분 업데이트. real, memberstatus를 업데이트 한다.
	 * 
	 * @param adcInfo, nodeList
	 */
	private void updateAdcConfigSlbNode(OBDtoAdcInfo adcInfo, ArrayList<OBDtoAdcNodeF5> nodeList) throws OBException {
		// 설정한 노드가 속한 vs들을 구한다. 노드가 바뀐 vs들의 소속 pool 멤버들 status만 업데이트하게.
		ArrayList<String> vsIndexListTemp = new ArrayList<String>();
		OBVServerDB vserverDB = new OBVServerDB();
		for (OBDtoAdcNodeF5 tnode : nodeList) {
			vsIndexListTemp.addAll(vserverDB.getVirtualServerIndexByNodeIPF5(adcInfo.getIndex(), tnode.getIpAddress()));
			// node가 멤버에 쓰였다고 해도 그 pool이 vs에 할당되지 않았다면 vsIndexList는 empty일 수 있다.
		}
		HashSet<String> hsTemp = new HashSet<String>(vsIndexListTemp); // 중복을 제거한다.
		ArrayList<String> vsIndexList = new ArrayList<String>(hsTemp); // ArrayList로 돌린다.

		ArrayList<OBDtoAdcVServerF5> vsList = vserverDB.getVServerListF5InternalUse(adcInfo.getIndex(), vsIndexList);

		// vs를 구했으므로, 진짜로 업데이트 하는 함수를 호출한다.
		DtoSlbUpdate updateOption = new DtoSlbUpdate();
		updateOption.setNodeInfo(true);
		if (vsList == null || vsList.isEmpty() == true) // node에 연관된 vs가 없으면 status update는 생략한다.
		{
			updateOption.setStatus(updateOption.STATUS_UPDATE_NONE);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "No related vs with changed nodes. Skip status update.");
		} else // 연관된 vs들의 status, pool의 member status를 업데이트한다.
		{
			updateOption.setStatus(updateOption.STATUS_UPDATE_ALL);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
					"Nodes are related with this virtua servers(to update status): " + vsIndexList);
		}

		updateAdcConfigSlb(adcInfo, updateOption, vsList, nodeList);
	}

	private void updateAdcConfigSlbVServerAddSet(OBDtoAdcInfo adcInfo, ArrayList<OBDtoAdcVServerF5> vsList)
			throws OBException {
		DtoSlbUpdate updateOption = new DtoSlbUpdate();
		updateOption.setVsInfo(true);
		updateOption.setNodeInfo(true); // node 신규 추가가 있어서 작업한다.
		updateOption.setPoolmemberInfo(true);
		updateOption.setStatus(updateOption.STATUS_UPDATE_ALL);

		updateAdcConfigSlb(adcInfo, updateOption, vsList, null);
	}

	private void updateAdcConfigSlbVServerEnaDisDel(OBDtoAdcInfo adcInfo, ArrayList<OBDtoAdcVServerF5> vsList)
			throws OBException {
		DtoSlbUpdate updateOption = new DtoSlbUpdate();
		updateOption.setVsInfo(true);
		updateOption.setStatus(updateOption.STATUS_UPDATE_NONE); // VS가 사라지므로 업데이트 필요없음

		updateAdcConfigSlb(adcInfo, updateOption, vsList, null);
	}

	private void updateSlbNode(OBDtoAdcInfo adcInfo, ArrayList<OBDtoAdcNodeF5> nodeList, StringBuilder transactionQuery)
			throws OBException {
		Integer adcIndex = adcInfo.getIndex();
		long tt0, tt1;
		tt0 = System.currentTimeMillis();// 시작
		int nodeCount = 0;
		iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);

		OBVServerDB vserverDB = new OBVServerDB();
		ArrayList<OBDtoAdcNodeF5> nodeListChanged;
		// node IP만 모아서 array로 저장, iControl API에서 String[]로 받기 때문에 변환
		try {
			if (nodeList == null) // 모든 노드 업데이트
			{
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
						String.format("Update all node info.(Node list is empty or null.)"));
				nodeListChanged = NodeAddress.getAll(interfaces);
				vserverDB.delNodeAll(adcIndex, transactionQuery);
				vserverDB.addNodeInfoF5(adcIndex, nodeListChanged, transactionQuery);
				nodeCount = nodeListChanged.size();
			} else {
				nodeCount = nodeList.size();
				String[] nodeIpArray = new NodeAddress().getNodeIpArrayFromNodeList(nodeList);
				nodeListChanged = NodeAddress.get(interfaces, nodeIpArray);

				// 부분업데이트: 수정한 노드를 삭제/추가
				ArrayList<String> nodeIndexList = new ArrayList<String>(); // 노드 업데이트시 삭제할 인덱스 목록을 만듦
				for (OBDtoAdcNodeF5 node : nodeList) {
					nodeIndexList.add(node.getIndex());
				}
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
						String.format("Update partial node info.(nodes to update = %s)", nodeIndexList));
				vserverDB.delNodePartial(adcIndex, nodeIndexList, transactionQuery);
				vserverDB.addNodeInfoF5(adcIndex, nodeListChanged, transactionQuery);
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, e.getMessage());
		}
		tt1 = System.currentTimeMillis();

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
				String.format("<time> --- NodeAddress.get(): %f (node# = %d)", (tt1 - tt0) / 1000.0, nodeCount));
	}

	/**
	 * F5 device에서 읽어온 Poolmember 정보를 DB에 쓴다.
	 */
	private boolean updateSlbPoolmemberCompact(Integer adcIndex, OBDtoAdcVServerF5 vs) { // F5에서 구한 노드를 삭제/추가
		OBVServerDB vserverDB = new OBVServerDB();
		ArrayList<OBDtoAdcPoolF5> poolNameList = new ArrayList<OBDtoAdcPoolF5>();
		ArrayList<String> poolIndexList = new ArrayList<String>();
		ArrayList<ArrayList<DtoPoolMember>> poolMemberList = new ArrayList<ArrayList<DtoPoolMember>>();
		ArrayList<DtoPoolMember> poolMember = new ArrayList<DtoPoolMember>();
		ArrayList<OBDtoAdcVServerF5> vsList = new ArrayList<OBDtoAdcVServerF5>();
		boolean ret = false;

		if (vs.getPool() != null) {
			if (!vs.getPool().getIndex().contains(vs.getPool().getName())) {
				poolIndexList.add(vs.getPool().getIndex());
				poolIndexList.add(OBCommon.makePoolIndex(adcIndex, vs.getPool().getName()));
			} else {
				poolIndexList.add(vs.getPool().getIndex());
			}
			poolNameList.add(vs.getPool());
			poolNameList.get(0).setMemberList(vs.getPool().getMemberList());
			poolMember = vs.getPool().getDtoMemberList();
			poolMemberList.add(poolMember);
			vsList.add(vs);
		}
		OBDatabase db = new OBDatabase();
		try {
			StringBuilder transactionQuery = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);
			transactionQuery.append(" BEGIN; ");
			vserverDB.delPoolPartial(adcIndex, poolIndexList, transactionQuery);
			vserverDB.delPoolmemberPartial(adcIndex, poolIndexList, transactionQuery);
			vserverDB.addPoolAndPoolMemberF5(adcIndex, poolNameList, poolMemberList, transactionQuery);
			vserverDB.updateVServerInfoF5(adcIndex, vsList, transactionQuery);
			// virtual server vlan Filter 쓰기
			vserverDB.delF5VlanFilter(adcIndex, vs.getName(), transactionQuery);
			vserverDB.addF5VlanFilter(adcIndex, vs, transactionQuery);
			transactionQuery.append(" COMMIT; ");

			db.openDB();
			db.executeUpdate(transactionQuery.toString());
			ret = true;
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, "vs write to DB error. failed vs is: " + vs);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "vs write to DB error. failed vs is: " + vs);
			ret = false;
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "vs write to DB error. updated vs is: " + vs);
		return ret;
	}

	/**
	 * F5 device에서 읽어온 node 정보를 DB에 쓴다.
	 */
	private boolean updateSlbNodeCompact(Integer adcIndex, ArrayList<OBDtoAdcNodeF5> nodeList,
			ArrayList<String> nodeIndexList) { // F5에서 구한 노드를 삭제/추가
		OBVServerDB vserverDB = new OBVServerDB();
		boolean ret = false;
		OBDatabase db = new OBDatabase();
		try {
			StringBuilder transactionQuery = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);
			transactionQuery.append(" BEGIN; ");
			vserverDB.delNodePartial(adcIndex, nodeIndexList, transactionQuery);
			vserverDB.addNodeInfoF5(adcIndex, nodeList, transactionQuery);
			transactionQuery.append(" COMMIT; ");

			db.openDB();
			db.executeUpdate(transactionQuery.toString());
			ret = true;
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, "Node write to DB error. failed nodes are: " + nodeIndexList);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Node write to DB error. failed nodes are: " + nodeIndexList);
			ret = false;
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Node write to DB error. updated nodes are: " + nodeIndexList);
		return ret;
	}

	/**
	 * @param adcInfo
	 * @param         vsIndexList: 멤버 상태를 업데이트할 pool 목록이 담긴 vs들. vs 기준으로 업데이트를 한다.
	 * @throws OBException
	 */
	private void updateSlbStatus(OBDtoAdcInfo adcInfo, ArrayList<OBDtoAdcVServerF5> vsList) throws OBException {
		long tt0, tt1;
		tt0 = System.currentTimeMillis();// 시작

		if (vsList == null) // 모든 vs 업데이트
		{
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
					"Update all virtual server and related member status.(VS list is null.)");
			// status snmp로 읽고, DB에 기록
			new OBAdcMonitorF5().getSlbStatusAllF5(adcInfo.getIndex()); // return이 있으나 쓰이지 않음. 함수 안에서 업데이트까지 다 함.
		} else // node가 멤버에 쓰였다고 해도 그 pool이 vs에 할당되지 않았다면 vsIndexList는 empty일 수 있다.
		{
			ArrayList<String> vsIndexList = new ArrayList<String>();
			for (OBDtoAdcVServerF5 vs : vsList) {
				vsIndexList.add(vs.getIndex());
			}
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String
					.format("Upate partial virtual server and related member status.(VS to update = %s)", vsIndexList));
			// status snmp로 읽고, DB에 기록
			new OBAdcMonitorF5().getSlbStatusPartialF5(adcInfo.getIndex(), vsIndexList);
		}
		tt1 = System.currentTimeMillis();
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
				String.format("<time> --- status read & write: %f", (tt1 - tt0) / 1000.0));
	}

	@Override
	public void revertSlbConfig(Integer adcIndex, String revertConfig, String currentConfig, String newVServiceList,
			String newPoolList, String newNodeList) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		// F5에서는 다음 함수로 대체한다.
		// Revert(adcIndex, lastConfig.getChunkF5(), extraInfo);
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function. Call revert() instead");
	}

//	@Override
//	public Integer getVSTotalCount(Integer adcIndex) throws OBException
//	{
//		OBDatabase db = new OBDatabase();
//		int result = 0;
//		try
//		{
//			db.openDB();
//			result = new OBVServerDB().getTotalVServerCount(adcIndex, db);
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
//		return result;
//	}

//	@Override
//	public ArrayList<OBDtoAdcVServerF5> getVServerListF5(Integer adcIndex,
//			Integer beginIndex, Integer endIndex)
//			throws OBExceptionUnreachable, OBExceptionLogin, OBException
//	{
//		// DB에서 ADC 정보를 읽어들인다.
////		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start to getVServerList"));
//		
//		OBDatabase db = new OBDatabase();
//		OBDatabase db2 = new OBDatabase();
//		OBDatabase db3 = new OBDatabase();
//		try
//		{
//			db.openDB();
//			db2.openDB();
//			db3.openDB();
//		}
//		catch(OBException e1)
//		{
//			throw new OBException(OBException.ERRCODE_SLB_VS_COUNT, e1);
//		}
////		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start to getVServerList-after db open"));
//
//		ArrayList<OBDtoAdcVServerF5> list;
//		try
//		{
//			list = new OBVServerDB().getVServerListAllF5(adcIndex, beginIndex, endIndex, db, db2, db3);
//		}
//		catch(OBException e)
//		{
//			db.closeDB();
//			db2.closeDB();
//			db3.closeDB();
//			throw new OBException(OBException.ERRCODE_SLB_VS_COUNT, e);
//		}
//		catch(Exception e)
//		{
//			db.closeDB();
//			db2.closeDB();
//			db3.closeDB();
//			OBException e1 = new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//			throw new OBException(OBException.ERRCODE_SLB_VS_COUNT, e1);
//		}
//		// 장비의 시간과 로컬시간이 같은 경우에는 로컬 DB에서 읽어들여 제공한다.
//		db.closeDB();
//		db2.closeDB();
//		db3.closeDB();
////		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end to getVServerList-status"));
//		
//		return list;
//	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			int adcIndex =3;
//			OBDtoAdcRealServerGroup rsGroup = new OBDtoAdcRealServerGroup();
//			rsGroup.setGroupIndex(3);
//			rsGroup.setAccntIndex(1);
//			rsGroup.setGroupName("hii1asdfasdf");
//			ArrayList<OBDtoAdcNodeF5> nodeList =new ArrayList<OBDtoAdcNodeF5>();
//			OBDtoAdcNodeF5 f5Test1 = new OBDtoAdcNodeF5();
//			f5Test1.setIndex("3_1");
//			nodeList.add(f5Test1);
//			OBDtoAdcNodeF5 f5Test2 = new OBDtoAdcNodeF5();
//			f5Test2.setIndex("3_2");
//			nodeList.add(f5Test2);
//			new OBAdcVServerF5().updateRealServerMap(adcIndex, rsGroup, nodeList);
//			
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		OBDateTime.Sleep(2000);
//		//new OBAdcVServerF5().setVServerNoticeOnOffTest(); // 공지 전환/복구 테스트
//		//new OBAdcVServerF5().searchVServerNoticeOnListTest(); //공지 그룹 화면의 "공지 vs 목록"
//		//new OBAdcVServerF5().searchVServerNoticeOffListTest(); //공지 그룹 화면의 "안공지 vs 목록"
//	}

	@Override
	public OBDtoSLBUpdateStatus updateSLBStatus(Integer adcIndex) throws OBException {
		OBDtoSLBUpdateStatus retVal = new OBDtoSLBUpdateStatus();
		try {
			retVal = new OBAdcVServerF5().downloadSlbConfig(adcIndex, false);
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
		return retVal;
	}

//	@Override
//	public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException
//	{
//		return new OBVServerDB().searchVServerListF5(adcIndex, searchKey, beginIndex, endIndex, OBDefine.ORDER_TYPE_VSNAME, OBDefine.ORDER_DIR_ASCEND);
//	}

	@Override
	public Integer searchVServerListF5Count(Integer adcIndex, String searchKey) throws OBException {
		return new OBVServerDB().searchVServerListF5Count(adcIndex, searchKey);
	}

	private void syncConfig(Integer adcIndex, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
		if (adcInfo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "ADC 정보 에러 (adcInfo null).");
		}

		try {
			iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);
			SystemF5.synchronizeFullConfig(interfaces); // full sync
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_ADC_CONFIGSYNC_SUCCESS, adcInfo.getName());
		} catch (Exception e) {
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
					OBSystemAudit.AUDIT_ADC_CONFIGSYNC_FAIL, adcInfo.getName());
			CommonF5.Exception(null, e.getMessage());
		}
	}

	@Override
	public void syncConifgF5(Integer adcIndex, OBDtoExtraInfo extraInfo) throws OBException {
		try {
			syncConfig(adcIndex, extraInfo);
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

	////////////////////////////////////////////////////////////////
	// FROM HERE, UNSUPPORTED FUNCTIONS FOR F5
	@Override
	public void addVServerAlteon(OBDtoAdcVServerAlteon virtualServer, OBDtoExtraInfo extraInfo)
			throws OBLicenseExpiredException, OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "F5 don't support function addVServerAlteon().");
	}

	@Override
	public void addVServerPAS(OBDtoAdcVServerPAS virtualServer, OBDtoExtraInfo extraInfo)
			throws OBLicenseExpiredException, OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void addVServerPASK(OBDtoAdcVServerPASK virtualServer, OBDtoExtraInfo extraInfo)
			throws OBLicenseExpiredException, OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void setVServerPAS(OBDtoAdcVServerPAS virtualServer, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void setVServerPASK(OBDtoAdcVServerPASK virtualServer, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcNodePASK> getNodeAvailableListPASK(Integer adcIndex, String poolIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean isAvailableVirtualServerAlteon(Integer adcIndex, Integer port, String alteonID, String ipAddress)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
				"F5 doesn't support this function isExistVirtualServerAlteon().");
	}

	@Override
	public boolean isAvailableVirtualServerPASK(Integer adcIndex, String vsName, String ipAddress, Integer port)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean isExistVirtualServerAlteon(Integer adcIndex, Integer alteonID) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean isExistVirtualServerPAS(Integer adcIndex, String vsName) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean isExistVirtualServerPASK(Integer adcIndex, String vsName) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void setVServerAlteon(OBDtoAdcVServerAlteon virtualServer, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "F5 don't support function setVServerAlteon().");
	}

	@Override
	public OBDtoAdcPoolAlteon getPoolAlteon(String poolIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "F5 don't support function getPoolAlteon().");
	}

	@Override
	public OBDtoAdcPoolPAS getPoolPAS(String poolIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public OBDtoAdcPoolPASK getPoolPASK(String poolIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcPoolAlteon> getPoolAlteonList(Integer adcIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "F5 don't support function getPoolAlteonList().");
	}

	@Override
	public ArrayList<OBDtoAdcPoolPAS> getPoolPASList(Integer adcIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcPoolPASK> getPoolPASKList(Integer adcIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public OBDtoAdcVServerAlteon getVServerAlteonInfo(Integer adcIndex, String index)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
				"F5 don't support function getVServerAlteonInfo().");
	}

	@Override
	public OBDtoAdcVServerPAS getVServerPASInfo(Integer adcIndex, String vsIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public OBDtoAdcVServerPASK getVServerPASKInfo(Integer adcIndex, String vsIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerAlteon> getVServerListAlteon(Integer adcIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
				"F5 don't support function getVServerListAlteon().");
	}

//	@Override
//	public ArrayList<OBDtoAdcVServerAlteon> getVServerListAlteon(
//			Integer adcIndex, Integer beginIndex, Integer endIndex)
//			throws OBExceptionUnreachable, OBExceptionLogin, OBException
//	{
//		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
//	}
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

//	@Override
//	public ArrayList<OBDtoAdcVServerAlteon> searchVServerListAlteon(Integer adcIndex, String searchKey, Integer beginIndex,	Integer endIndex) throws OBException
//	{
//		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
//	}

	@Override
	public Integer searchVServerListAlteonCount(Integer adcIndex, String searchKey) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

//	@Override
//	public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(
//			Integer adcIndex, String searchKey, Integer beginIndex,
//			Integer endIndex) throws OBException
//	{
//		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
//	}

	@Override
	public Integer searchVServerListPASCount(Integer adcIndex, String searchKey) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

//	@Override
//	public ArrayList<OBDtoAdcVServerPASK> searchVServerListPASK(
//			Integer adcIndex, String searchKey, Integer beginIndex,
//			Integer endIndex) throws OBException
//	{
//		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
//	}

	@Override
	public Integer searchVServerListPASKCount(Integer adcIndex, String searchKey) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcNodeAlteon> getNodeAvailableListAlteon(Integer adcIndex, String poolIndex)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
				"F5 don't support function getNodeAvailableListAlteon().");
	}

	@Override
	public ArrayList<OBDtoAdcNodePAS> getNodeAvailableListPAS(Integer adcIndex, String poolIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean checkVrrp(String ipaddress, String accountID, String password, String swVersion, String vsIP,
			Integer routerIndex, Integer vrIndex, Integer ifNum)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "F5 don't support function checkVrrp().");
	}

	@Override
	public ArrayList<OBDtoNetworkInterface> getL3InterfaceList(Integer adcIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
				"F5 don't support function getL3InterfaceList().");
	}

//	public static void main(String[] args)
//	{
//		while(true)
//		{
//			try
//			{
////				OBDatabase db=new OBDatabase();
////				db.openDB();
////				
////				db.closeDB();
//				new OBAdcVServerF5().downloadSlbConfig(20);
////				System.out.println(list);
//			}
//			catch(Exception e)
//			{
//				e.printStackTrace();
//			}
//			OBDateTime.Sleep(2000);
//		}
//	}
//	public static void main(String[] args)
//	{
//		//public OBDtoAdcVServerF5 getVServerF5Info(Integer adcIndex, String index)
//		
//		OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
//		extraInfo.setAccountIndex(1);
//		
////		OBDtoAdcVServerF5 temp;
////		try
////		{
////			temp = new OBAdcVServerF5().getVServerF5Info(1, "1_a10.10.10.111");
////			System.out.println(temp.toString());
////		}
////		catch(Exception e)
////		{
////			e.printStackTrace();
////		}
//			
//		//testAddAdc(); ADC 추가하기 테스트. 처음에 한번 등록하고, 그 다음에는 있으니까 하지 않는다.
//
//		OBDtoAdcInfo F5adc = testGetFirstF5Adc(1); //account index = 1의 첫번째 F5 장비 정보를 가져온다. 테스트에 계속 쓴다.
//		if(F5adc == null)
//		{
//			return;
//		}
//		
//		// 여기부터 테스트
//		
//		testUpdateSLBConfig(F5adc); // ADC에서 설정 가져오기 테스트. 시간 많이 걸린다.
//
//		testGetProfileList(F5adc);  // ADC에서 persistence profile 전체 가져오기
//
//		//testSearchVS(F5adc, "waf"); // ADC(1st arg)에서 검색어(2nd arg)로 virtual server를 검색한다.
//		
//		//testAddVirtualServer(F5adc); // ADC에 virtual server를 추가한다.
//		
//		testEditVirtualServer(F5adc, extraInfo); //ADC의 virtual server하나를 수정한다.
//		
//		testToggleVirtualServer(F5adc, extraInfo); // ADC의 first virtual server state를 enable/disable
//	}

//	public static void testAddAdc(OBDtoExtraInfo extraInfo)
//	{
//		OBAdcManagementImpl adcManager = new OBAdcManagementImpl();
//		OBDtoAdcInfo adcInfo = new OBDtoAdcInfo();
//		
//		adcInfo.setName("abc adc");
//		adcInfo.setAdcIpAddress("192.168.200.10");
//		adcInfo.setAdcAccount("admin");
//		adcInfo.setAdcPassword("admin");
//		adcInfo.setAdcType(1); //F5
//		adcInfo.setGroupIndex(1);
//		
//		try
//		{
//			adcManager.addAdcInfo(adcInfo, extraInfo);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	public static OBDtoAdcInfo testGetFirstF5Adc(Integer accountIndex)
//	{
//		OBAdcManagementImpl adcManager = new OBAdcManagementImpl();
//		int i;
//		
//		ArrayList<OBDtoAdcInfo> adcList=null;
//		try
//		{
//			adcList = adcManager.getAdcInfoList(accountIndex);
//		}
//		catch(OBException e)
//		{
//			System.out.println("testGetFirstF5Adc() Error");
//			e.printStackTrace();
//		}
//		OBDtoAdcInfo adc = null	;
//
//		for(i=0; i<adcList.size(); i++)
//		{
//			adc = adcList.get(i);
//			if(adc.getAdcType() == OBDefine.ADC_TYPE_F5)
//			{
//				break;
//			}
//		}
//		System.out.println("first F5 ADC info = " + adc.getAdcIpAddress() + " / " + adc.getAdcAccount() + " / " + adc.getAdcPasswordDecrypt());
//		return adc;
//	}

//	public static void testGetProfileList(OBDtoAdcInfo adc)
//	{
//		OBAdcVServer vsManager = new OBAdcVServerF5();
//		try
//		{
//			ArrayList<OBDtoAdcProfile> pl = vsManager.getProfileList(adc.getIndex());
//			
//			System.out.println("--- START profile list ---");
//			for(OBDtoAdcProfile p:pl)
//			{
//				System.out.println("   " + p.toString());
//			}
//			System.out.println("--- DONE profile list ---");
//		}
//		catch(OBException e)
//		{
//			System.out.println("testGetProfileList() Error");
//			e.printStackTrace();
//		}
//	}

	public static void testSearchVS(OBDtoAdcInfo adc, String searchKey) {
//		OBAdcVServer vsManager = new OBAdcVServerF5();
//		ArrayList<OBDtoAdcVServerF5> searchRec=null;
//		try
//		{
//			searchRec = vsManager.searchVServerListF5(adc.getIndex(), searchKey);
//		}
//		catch(OBException e)
//		{
//			System.out.println("testSearchVS() Error");
//			e.printStackTrace();
//		}
//		System.out.println("--- START search vritual server ---");
//		for(int i =0; i<searchRec.size(); i++)
//		{
//			System.out.println("   " + i + ")" + searchRec.get(i).getName() + " / " + searchRec.get(i).getvIP());
//		}
//		System.out.println("--- DONE search virtual server ---");
	}

	public static void testAddVirtualServer(OBDtoAdcInfo adc, OBDtoExtraInfo extraInfo) throws Exception {
		OBAdcVServer vsManager = new OBAdcVServerF5();
		long tt1 = 0, tt2 = 0;

		// F5 adc의 첫번째 virtual server를 가져온다.
		OBDtoAdcVServerF5 vsFirst = null;
		try {
			vsFirst = vsManager.getVServerListF5(adc.getIndex()).get(1);
		} catch (Exception e) {
			System.out.println("testAddVirtualServer() Error1");
			e.printStackTrace();
			throw e;
		}

		OBDtoAdcVServerF5 new_vs = new OBDtoAdcVServerF5();

		new_vs.setAdcIndex(vsFirst.getAdcIndex());
		new_vs.setApplyTime(null);
		new_vs.setStatus(null);
		new_vs.setName("a_new_vs");
		new_vs.setvIP("10.10.10.101");
		new_vs.setServicePort(25);
		new_vs.setPersistence(null);
		new_vs.setUseYN(null);
		new_vs.setPool(vsFirst.getPool());

		try {
			// vsManager.delVServer(adcIndex, newVS.getIndex());
			tt1 = System.currentTimeMillis();
			vsManager.addVServerF5(new_vs, extraInfo);
			tt2 = System.currentTimeMillis();
			System.out.println("<time> virtual server add: " + (tt2 - tt1) / 1000.0);
		} catch (Exception e) {
			System.out.println("testAddVirtualServer() Error2");
			e.printStackTrace();
		}
	}

	public static void testEditVirtualServer(OBDtoAdcInfo adc, OBDtoExtraInfo extraInfo) throws Exception {
		OBAdcVServer vsManager = new OBAdcVServerF5();
		long tt1 = 0, tt2 = 0;

		// F5 adc의 첫번째 virtual server를 가져온다.
		OBDtoAdcVServerF5 vsFirst = null;
		try {
			vsFirst = vsManager.getVServerListF5(adc.getIndex()).get(1);
		} catch (Exception e) {
			System.out.println("testEditVirtualServer() Error1");
			e.printStackTrace();
			throw e;
		}

		System.out.println("virtual server / before ==> " + vsFirst.toString());

		// edit lbmethod

		if (vsFirst.getPool().getLbMethod() == OBDefine.LB_METHOD_ROUND_ROBIN) {
			vsFirst.getPool().setLbMethod(OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER);
		} else {
			vsFirst.getPool().setLbMethod(OBDefine.LB_METHOD_ROUND_ROBIN);
		}

		tt1 = System.currentTimeMillis();
		try {
			tt1 = System.currentTimeMillis();
			vsManager.setVServerF5(vsFirst, extraInfo);
			tt2 = System.currentTimeMillis();

			vsFirst = vsManager.getVServerListF5(adc.getIndex()).get(1);
			System.out.println("virtual server / after ==> " + vsFirst.toString());
			System.out.println("<time> virtual server edit time: " + (tt2 - tt1) / 1000.0);
		} catch (Exception e) {
			System.out.println("testEditVirtualServer() Error2");
			e.printStackTrace();
		}
	}

	public static void testToggleVirtualServer(OBDtoAdcInfo adc, OBDtoExtraInfo extraInfo) throws Exception {
		OBAdcVServer vsManager = new OBAdcVServerF5();
		long tt1 = 0, tt2 = 0;

		// F5 adc의 첫번째 virtual server를 가져온다.
		OBDtoAdcVServerF5 vsFirst = null;
		try {
			vsFirst = vsManager.getVServerListF5(adc.getIndex()).get(1);
		} catch (Exception e) {
			System.out.println("testToggleVirtualServer() Error1");
			e.printStackTrace();
			throw e;
		}

		// toggle enable/disable
		ArrayList<String> vsIndexList = new ArrayList<String>();
		vsIndexList.add(vsFirst.getIndex());

		try {
			tt1 = System.currentTimeMillis();
			if (vsFirst.getUseYN() == OBDefine.STATE_DISABLE) {
				vsManager.enableVServer(adc.getIndex(), vsIndexList, extraInfo);
			} else {
				vsManager.disableVServer(adc.getIndex(), vsIndexList, extraInfo);
			}
			tt2 = System.currentTimeMillis();
			System.out.println("<time> virtual server enable/disable: " + (tt2 - tt1) / 1000.0);
		} catch (Exception e) {
			System.out.println("testToggleVirtualServer() Error2");
			e.printStackTrace();
		}
	}

	public static void testPrintVServerList(ArrayList<OBDtoAdcVServerF5> res) {
		System.out.println("\nADC index: " + res.get(0).getAdcIndex());
		System.out.println("config time: " + res.get(0).getApplyTime().toString());

		for (OBDtoAdcVServerF5 vs : res) {
			String persist = vs.getPersistence();

			System.out.println("---------------------------------------------");
			System.out.println(vs.getName() + "(status:" + vs.getStatus() + ")" + "\n\t" + vs.getvIP() + ":"
					+ vs.getServicePort() + "\n\tdefault pool: " + vs.getPool().getName()
					+ "\n\tdefault persistence profile: " + persist);
		}
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

	@Override
	public String getValidVSIndex(Integer adcIndex, String vsIndex) throws OBException {
		// F5의 경우에는 아무런 작업이 없다.
		return vsIndex;
	}

	@Override
	public String getValidPoolIndex(Integer adcIndex, String poolIndex) throws OBException {
		// F5의 경우에는 아무런 작업이 없다.
		return poolIndex;
	}

	@Override
	public boolean isExistVSIPAddress(Integer adcIndex, String vsIPAddress) throws OBException {
		boolean retVal = new OBVServerDB().isExistVSIPAddress(adcIndex, vsIPAddress);
		return retVal;
	}

	@Override
	public ArrayList<OBDtoAdcVServerAlteon> searchVSListUsedByPoolAlteon(Integer adcIndex, Integer poolID)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerF5> searchVSListUsedByPoolF5(Integer adcIndex, String poolName) throws OBException {
		return new OBVServerDB().searchVSListUsedByPoolF5(adcIndex, poolName);
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
//		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
//	}
//
//	@Override
//	public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException
//	{
//		return new OBVServerDB().searchVServerListF5(adcIndex, accntIndex, searchKey, beginIndex, endIndex, OBDefine.ORDER_TYPE_VSNAME, OBDefine.ORDER_DIR_ASCEND);
//	}

//	@Override
//	public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException
//	{
//		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
//	}
//
//	@Override
//	public ArrayList<OBDtoAdcVServerPASK> searchVServerListPASK(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException
//	{
//		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
//	}

	@Override
	public ArrayList<OBDtoAdcHealthCheckPASK> getHealthCheckListPASK(Integer adcIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public Integer searchVServerListAlteonCount(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public Integer searchVServerListF5Count(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException {
		return new OBVServerDB().searchVServerListF5Count(adcIndex, accntIndex, searchKey);
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerF5> getVServerListF5(Integer adcIndex, Integer beginIndex, Integer endIndex,
			Integer orderType, Integer orderDir) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		return new OBVServerDB().searchVServerListF5(adcIndex, null, beginIndex, endIndex, orderType, orderDir);
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex, String searchKey, Integer beginIndex,
			Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		return new OBVServerDB().searchVServerListF5(adcIndex, searchKey, beginIndex, endIndex, orderType, orderDir);
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		return new OBVServerDB().searchVServerListF5(adcIndex, accntIndex, searchKey, beginIndex, endIndex, orderType,
				orderDir);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getPoolIndexListAlteon(Integer adcIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<String> getVsNameList(Integer adcIndex) throws OBException {
		return new OBVServerDB().getVsNameList(adcIndex);
	}

	@Override
	public ArrayList<OBDtoAdcHealthCheckAlteon> getHealthCheckListAlteon(Integer adcIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean isPeerOSEqual(Integer adcIndex, Integer peerIndex) throws OBException {
		// F5는 본 기능을 제공하지 않는다. 무조건 true한다.
		return true;
	}

	@Override
	public boolean isPeerVrrpValid(Integer adcIndex, Integer peerIndex) throws OBException {
		// F5는 본 기능을 제공하지 않는다. 무조건 true한다.
		return true;
	}

	@Override
	public ArrayList<OBDtoAdcNodeDetail> searchNodeListF5(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		return new OBVServerDB().searchNodeListF5(adcIndex, accntIndex, searchKey, beginIndex, endIndex, orderType,
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
	public Integer searchVServerListAllListCount(OBDtoAdcScope scope, Integer accntIndex, String searchKey)
			throws OBException {
		return new OBVServerDB().searchVServerAllListCoreCount(scope, accntIndex, searchKey);
	}

	@Override
	public ArrayList<OBDtoAdcNodeDetail> searchNodeListAlteon(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcNodeDetail> searchGroupListAlteon(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer orderType, Integer orderDir) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	public ArrayList<OBDtoAdcNodeDetail> searchGroupListF5(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer orderType, Integer orderDir) throws OBException {
		return new OBVServerDB().searchGroupListF5(adcIndex, accntIndex, searchKey, orderType, orderDir);
	}

	@Override
	public Integer searchNodeF5ListCount(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException {
		return new OBVServerDB().searchNodeF5ListCountCore(adcIndex, accntIndex, searchKey);
	}

	@Override
	public void setNodeState(Integer adcIndex, ArrayList<OBDtoAdcNodeF5> nodeList, Integer action,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		boolean isSuccessful = false;
		OBDtoAdcInfo adcInfo = null;
		String nodeString = "";
		try {
//			new OBAdcManagementImpl().waitUntilAdcAvailable(adcIndex);
			// 중요!! 여기부터 decreaseAdcCheckCfg()를 호출 부분까지는 일어날 수 있는 모든 exception에 대해 명시적으로
			// 처리한다.거기서 decreaseAdcCheckCfg()를 호출해야 한다.

			// 기본적인 값 유효성 확인
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "setNodeState() error: adcInfo null.");
			}
			if (nodeList == null || nodeList.size() == 0) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						"setNodeState() error: nodeList is null or empty.");
			}
			// F5 icontrol interface 준비
			iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);

			// 장비 apply 시간을 ADCsmart의 시간과 비교해서 업데이트가 있는지 확인한다.
			if (timeSyncCheck(adcIndex) == false) {
				// node 설정 동기화 체크
				// DB에서 node들의 현재 상태를 구한다.
				ArrayList<String> nodeIndexList = new NodeAddress().getNodeIndexListFromNodeList(nodeList);
				ArrayList<OBDtoAdcNodeF5> dbNodes = new OBVServerDB().getNodeInfoF5(nodeIndexList);

				ArrayList<OBDtoAdcNodeF5> nodeListToUpdate = new NodeAddress().checkConfigSync(interfaces, nodeList,
						dbNodes);
				if (nodeListToUpdate != null) // 설정이 틀린게 있어서 업데이트가 필요함
				{
					if (updateSlbNodeCompact(adcIndex, nodeListToUpdate, nodeIndexList) == true) {
						throw new OBException(OBException.ERRCODE_SLB_VS_TIME,
								"Sync finished. ADC: " + adcInfo.getName());
					} else {
						throw new OBException(OBException.ERRCODE_SLB_VS_TIME,
								"Sync failed. local nodes != device nodes. ADC: " + adcInfo.getName());
					}
				} else // nodeListToUpdate == null, 업데이트할게 없음. 설정이 일치함
				{
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format(
							"Apply times are different between local and ADC. But nodes are identical so process is going!"));
				}
			}
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[node change] config sync check OK");

			// 변경이력 준비 : 일단 이력 기록은 건너뜀
			// F5에 설정
			NodeAddress.setState(interfaces, nodeList, action);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[node change] node config done");
			// 디스크에 저장
			SystemF5.saveHighConfig(interfaces);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[node change] save to disk");
			// 설정 DB에 업데이트
			updateAdcConfigSlbNode(adcInfo, nodeList);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[node change] udpate config");

			// 변경 이력 기록: 이력 기록은 건너뜀

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
	public Integer searchVServerNoticeOffListCount(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		return new OBVServerDB().getVServerNoticeListCount(adcIndex, accntIndex, searchKey, false);
	}

	@Override
	public void setVServerNoticeOnF5(Integer adcIndex, ArrayList<OBDtoAdcVServerNotice> vsList,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("virtual server notice-on BEGIN: vs list = %s", vsList));
		setVServerNoticeOnOff(adcIndex, vsList, extraInfo, OBDefine.CHANGE_TYPE_EDIT_NOTICEON); // notice on
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "virtual server notice-on END");
	}

	@Override
	public void setVServerNoticeOffF5(Integer adcIndex, ArrayList<OBDtoAdcVServerNotice> vsList,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("virtual server notice-off BEGIN: vs list = %s", vsList));
		setVServerNoticeOnOff(adcIndex, vsList, extraInfo, OBDefine.CHANGE_TYPE_EDIT_NOTICEOFF); // notice off
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "virtual server notice-off END");
	}

	private void setVServerNoticeOnOff(int adcIndex, ArrayList<OBDtoAdcVServerNotice> vsList, OBDtoExtraInfo extraInfo,
			int changeType) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBVServerDB vsdb = new OBVServerDB();
		String newPoolIndex = null;
		OBDtoAdcPoolF5 newPool = null;
		boolean isSuccessful = false;
		String vsNameString = "";
		ArrayList<OBDtoAdcVServerF5> vsWorkList = new ArrayList<OBDtoAdcVServerF5>();
		OBDtoAdcVServerF5 tempVs;

		try {
			// 기본적인 값 유효성 확인
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "adcInfo is null");
			}
			if (vsList == null || vsList.size() == 0) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Virtual server list is null or empty.");
			}
			if (changeType != OBDefine.CHANGE_TYPE_EDIT_NOTICEON && changeType != OBDefine.CHANGE_TYPE_EDIT_NOTICEOFF) // notice
																														// 설정이
																														// 아님
			{
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						"Invalid action. (none of notice on/off)");
			}
			for (OBDtoAdcVServerNotice vs : vsList) {
				vsNameString += (", " + vs.getVsName());
				tempVs = vsdb.getVServerInfoF5(vs.getIndex());
				if (tempVs != null) {
					vsWorkList.add(tempVs);
				}
			}
			vsNameString = vsNameString.substring(1, vsNameString.length()); // 머리의 comma 제거

			// F5 icontrol interface 준비
			iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);

			// 작업 중간에 설정 간섭이 있었는지 확인
			if (timeSyncCheck(adcIndex) == false) {
				for (OBDtoAdcVServerF5 vs : vsWorkList) {
					// vs 설정 동기화 체크
					OBDtoAdcVServerF5 poolListToUpdate = checkConfigSync(interfaces, vs);
					if (poolListToUpdate != null) {
						throw new OBException(OBException.ERRCODE_SLB_VS_TIME, String.format(
								"Sync failed. local VS != device VS. ADC: %s VS: %s", adcInfo.getName(), vs.getName()));
					}
				}
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format(
						"Apply times are different between local and ADC. But vs(changed) are identical so process is going!"));
			}

			try {
//				new OBAdcManagementImpl().waitUntilAdcAvailable(adcIndex);
				// 중요!! 여기부터 decreaseAdcCheckCfg()를 호출 부분까지는 일어날 수 있는 모든 exception에 대해 명시적으로
				// 처리한다.거기서 decreaseAdcCheckCfg()를 호출해야 한다.

				// 변경기록 준비
				OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
				ArrayList<OBDtoAdcConfigChunkF5> configChunkList = new ArrayList<OBDtoAdcConfigChunkF5>();
				OBDtoAdcConfigChunkF5 configChunk = null;

				for (OBDtoAdcVServerNotice server : vsList) {
					// virtual server 상태를 잡아 놓는다.
					tempVs = vsdb.getVServerInfoF5(server.getIndex());

					// changeType 함수 시작시 체크함
					if (changeType == OBDefine.CHANGE_TYPE_EDIT_NOTICEOFF) // (original) service group으로 복귀
					{
						newPoolIndex = server.getServicePoolIndex();
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[notice on/off] notice off");
					} else // if(changeType==OBDefine.CHANGE_TYPE_EDIT_NOTICEON) // notice group으로 설정
					{
						newPoolIndex = server.getNoticePoolIndex();
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[notice on/off] notice on");
					}

					// getPoolInfoF5() call은 파라미터로 DB 인스턴스를 줘서 연결을 절약할 수 있지만, 자주 하는 작업이 아니고, 2개를
					// 넘겨줘야해서 필요해 어차피 만들게된다.
					newPool = vsdb.getPoolInfoF5(newPoolIndex); // null을 반환하지 않는다.

					if (newPool == null || newPool.getIndex() == null || newPool.getIndex().isEmpty() == true) { // pool을
																													// 못
																													// 찾았다.
																													// 발생한다면
																													// 설정
																													// 관리/업데이트
																													// 오류
																													// 가능성
						CommonF5.Exception(
								String.format("VirtualServer notice on/off error. destination pool(%s) not found.",
										newPoolIndex),
								"none");
					}

					// vs의 pool을 새로 갖게 될 pool로 바꾼다.
					// 변경 체크는, 목적 형상을 넘겨주고 현재와 비교하게 되는데 이 함수에 넘어온 정보는 최종 형상이 아니다. DB에서 구한 기대 값으로
					// 구성한다.
					tempVs.setPool(newPool);

					configChunk = historyManager.MakeConfigChunkF5(tempVs, OBDefine.CHANGE_BY_USER,
							OBDefine.CHANGE_TYPE_EDIT, OBDefine.CHANGE_OBJECT_VIRTUALSERVER,
							extraInfo.getAccountIndex());
					if (configChunk.getChangeType() != OBDefine.CHANGE_TYPE_NONE) { // 실제로 변경되는 것만 작업한다.
																					// noticeOn-->noticeOn,
																					// noticeOff-->noticeOff 작업하지 않는다.
						configChunkList.add(configChunk);
					}
				}

				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[notice on/off] - Analysis done.");
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[notice on/off] todo list = " + configChunkList);
				// 변경기록 준비 끝

				int change = OBDefine.CHANGE_TYPE_NONE;
				for (OBDtoAdcConfigChunkF5 chunk : configChunkList) {
					change = configSlb(chunk, interfaces); // 설정
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[notice on/off] config done");

					if (change != OBDefine.CHANGE_TYPE_NONE) {
						// 변경기록
						chunk.getVsConfig().setPoolChange(changeType); // 공지 변경을 알리기 위해 부득이 외부에서 변경했다... 어쩔 수 없었다..
						historyManager.writeConfigHistoryF5(chunk);
					} else {
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs notice] nothing to do.");
					}
				}

				SystemF5.saveHighConfig(interfaces); // 디스크에 저장
				// updateAdcConfigSlb(adcInfo); //DB 업데이트

				ArrayList<OBDtoAdcVServerF5> changeVsList = new ArrayList<OBDtoAdcVServerF5>();
				for (OBDtoAdcVServerNotice vs : vsList) {
					OBDtoAdcVServerF5 vsUp = new OBDtoAdcVServerF5();
					vsUp.setIndex(vs.getIndex());
					vsUp.setName(vs.getVsName());
					changeVsList.add(vsUp);
				}

				updateAdcConfigSlbVServerEnaDisDel(adcInfo, changeVsList); // 변경 영향 부분만 업데이트
				writeVServerNoticeToDb(adcIndex, configChunkList, changeType, extraInfo.getAccountIndex()); // 공지pool
																											// 변경을 DB에
																											// 저장
				isSuccessful = true;
			} catch (OBException e) {
				throw e;
			}

			finally {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
				if (isSuccessful == true) {
					if (changeType == OBDefine.CHANGE_TYPE_EDIT_NOTICEOFF) {
						new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcIndex,
								extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_NOTICE_REVERT_SUCCESS,
								adcInfo.getName(), vsNameString, null);
					} else {
						new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcIndex,
								extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_NOTICE_SET_SUCCESS,
								adcInfo.getName(), vsNameString, null);
					}
				} else {
					if (changeType == OBDefine.CHANGE_TYPE_EDIT_NOTICEOFF) {
						new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcIndex,
								extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_NOTICE_REVERT_FAIL,
								adcInfo.getName(), vsNameString, null);
					} else {
						new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcIndex,
								extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_NOTICE_SET_FAIL,
								adcInfo.getName(), vsNameString, null);
					}
				}
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			try {
				CommonF5.Exception("", e.getMessage());
			} catch (OBExceptionUnreachable e1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e1.getMessage());
			} catch (OBExceptionLogin e1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e1.getMessage());
			} catch (OBException e1) {
				throw e1;
			} catch (Exception e1) {
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e1.getMessage());
			}
		}
	}

	// 공지pool 업데이트
	private void writeVServerNoticeToDb(Integer adcIndex, ArrayList<OBDtoAdcConfigChunkF5> configChunkList,
			int changeType, int accntIndex) throws OBException {
		OBVServerDB vserverDB = new OBVServerDB();
		try {
//			vserverDB.delDeadVServerNotice(adcIndex);
			vserverDB.updateVServerNoticeChange(adcIndex, changeType, configChunkList, accntIndex);
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
	}

	private String getUpdateLockFileName(Integer adcIndex) {
		return OBDefine.DIR_LOCKFILE_SYSLOGD + adcIndex + "_slb.lock";
	}

	// setVServerNoticeOnF5() && setVServerNoticeOffF5() 테스트 함수
//	private void setVServerNoticeOnOffTest() {
//		Integer adcIndex = 3;
//		ArrayList<OBDtoAdcVServerNotice> vsList = new ArrayList<OBDtoAdcVServerNotice>();
//		OBDtoAdcVServerNotice vs1 = new OBDtoAdcVServerNotice();
//
//		vs1.setIndex("3_test5");
//		vs1.setServicePoolIndex("3_MONITOR_POOL1"); // service
//		vs1.setNoticePoolIndex("3_MONITOR_POOL2"); // notice
//		vsList.add(vs1);
//		String job = "on";
//
//		OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
//		extraInfo.setAccountIndex(1);
//		try {
//			if (job.equals("on"))
//				new OBAdcVServerF5().setVServerNoticeOnF5(adcIndex, vsList, extraInfo);
//			else
//				new OBAdcVServerF5().setVServerNoticeOffF5(adcIndex, vsList, extraInfo);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	// RealServerGroup 추가
	public boolean addRealServerGroup(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup) throws OBException {

		try {
			// Real server Group 이름이 중복인지 확인
			if (isExistRealServerGroup(adcIndex, rsGroup)) {
				return false;
			}
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
					String.format("Real server Group Add: rsGroup list = %s", rsGroup));
			new OBVServerDB().addRealServerGroup(adcIndex, rsGroup);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "Real server Group Add END");

		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		return true;
	}

	// RealServerGroup 이름 변경
	public boolean setRealServerGroup(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup) throws OBException {
		try {
			// Real server Group 이름이 중복인지 확인
			if (isExistRealServerGroup(adcIndex, rsGroup)) {
				return false;
			}
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
					String.format("Real server Group Modify: rsGroup list = %s", rsGroup));
			new OBVServerDB().setRealServerGroup(adcIndex, rsGroup); // Real
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "Real server Group Modify END");
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		return true;
	}

	// RealServerGroup 삭제
	public void delRealServerGroup(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup) throws OBException {
		try {
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
					String.format("Real server Group Delete: rsGroup list = %s", rsGroup));
			new OBVServerDB().delRealServerGroup(adcIndex, rsGroup); // Real
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "Real server Group Delete END");
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
	}

	// RealServerGroup List 조회
	public ArrayList<OBDtoAdcRealServerGroup> searchNodeGrpListF5(Integer adcIndex, Integer accntIndex,
			Integer orderType, Integer orderDir) throws OBException {
		return new OBVServerDB().getRealServerList(adcIndex, accntIndex, orderType, orderDir);
	}

	// RealServerMap 추가 함수
	public boolean updateRealServerMap(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup,
			ArrayList<OBDtoAdcNodeF5> nodeList) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("Real server Map Add: rsGroup list = %s", rsGroup));
		try {
			if (rsGroup.getGroupIndex() == OBDefine.REALSERVER_DELETE) {
				OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "Real server Map Del START");
				new OBVServerDB().delRealServerMap(adcIndex, rsGroup, nodeList);
				OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "Real server Map Del END");
			} else {
				// Real server Group 이름이 중복인지 확인
				if (isExistRealServerMap(adcIndex, rsGroup, nodeList)) {
					return false;
				}
				OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
						String.format("Real server Group Add: rsGroup list = %s", rsGroup));
				new OBVServerDB().addRealServerMap(adcIndex, rsGroup, nodeList);
				OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "Real server Map Add END");
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
		return true;
	}

	// RealServerGroup 이름 존재하는지 체크
	public static boolean isExistRealServerGroup(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup) throws OBException {
		boolean retVal = false;
		String sqlText = "";
		if (rsGroup == null || rsGroup.getGroupName().isEmpty()) {
			return retVal;
		}
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT INDEX FROM MNG_REALSERVER_GROUP 			"
							+ " WHERE ADC_INDEX=%d AND RS_GROUP_NAME=%s AND ACCNT_INDEX=%d 		"
							+ " AND AVAILABLE=%d; 												",
					adcIndex, OBParser.sqlString(rsGroup.getGroupName()), rsGroup.getAccntIndex(),
					OBDefine.DATA_AVAILABLE);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
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

	// RealServerMap Real Server 존재하는지 체크
	public boolean isExistRealServerMap(Integer adcIndex, OBDtoAdcRealServerGroup rsGroup,
			ArrayList<OBDtoAdcNodeF5> nodeList) throws OBException {
		boolean retVal = false;
		String sqlText = "";
		String sqlTextRS = OBParser.convertSqlRealServerList(nodeList);
		if (sqlTextRS == null || sqlTextRS.isEmpty()) {
			return retVal;
		}
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT A.GROUP_INDEX FROM															"
							+ " (SELECT * FROM MNG_REALSERVER_GROUP_MAP WHERE GROUP_INDEX = %d AND RS_INDEX IN (%s) ) A 		"
							+ "	LEFT JOIN  MNG_REALSERVER_GROUP B 																"
							+ " ON A.GROUP_INDEX = B.INDEX AND B.ACCNT_INDEX = %d AND B.ADC_INDEX = %d;							",
					rsGroup.getGroupIndex(), sqlTextRS, rsGroup.getAccntIndex(), adcIndex);
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
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

	public ArrayList<OBDtoAdcVlan> getF5VlanListAll(Integer adcIndex) throws OBException {
		ArrayList<OBDtoAdcVlan> retVal = new ArrayList<OBDtoAdcVlan>();
		String sqlText = "";

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(" SELECT VLAN_NAME FROM TMP_SLB_VLANTUNNEL              "
					+ " WHERE ADC_INDEX = %d                                                ", adcIndex);
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcVlan vlan = new OBDtoAdcVlan();
				vlan.setVlanName(db.getString(rs, "VLAN_NAME"));
				retVal.add(vlan);
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

	public ArrayList<OBDtoAdcVlan> getF5VlanList(Integer adcIndex, String vsIndex) throws OBException {
		ArrayList<OBDtoAdcVlan> retVal = new ArrayList<OBDtoAdcVlan>();
		String sqlText = "";
		if (vsIndex == null) {
			return retVal;
		}
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT VLAN_NAME FROM TMP_SLB_VLANTUNNEL              "
							+ " WHERE ADC_INDEX = %d AND VLAN_NAME NOT IN                           "
							+ " (SELECT VLAN_NAME FROM TMP_SLB_VLANTUNNEL_FILTER                    "
							+ " WHERE ADC_INDEX = %d AND INDEX = %s AND VLAN_NAME IS NOT NULL)      ",
					adcIndex, adcIndex, OBParser.sqlString(vsIndex));
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcVlan vlan = new OBDtoAdcVlan();
				vlan.setVlanName(db.getString(rs, "VLAN_NAME"));
				retVal.add(vlan);
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
	public DtoVlanTunnelFilter getF5VlanFilterList(Integer adcIndex, String vsIndex) throws OBException {
		DtoVlanTunnelFilter retVal = new DtoVlanTunnelFilter();
		ArrayList<String> vlanName = new ArrayList<String>();
		String sqlText = "";
		if (vsIndex == null) {
			return retVal;
		}
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
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
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public Integer searchNodeAlteonListCount(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public Integer searchNodeAllListCount(OBDtoAdcScope scope, Integer accntIndex, String searchKey)
			throws OBException {
		return new OBVServerDB().searchNodeAllListCountCore(scope, accntIndex, searchKey);
	}

	public void configSchedule(OBDtoAdcSchedule adcSchedule)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException {
		if (adcSchedule.getChangeObjectType() == OBDefine.CHANGE_OBJECT_VIRTUALSERVER) {
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
					String.format("calling F5 configSchedule()... adcIndex : %d", adcSchedule.getAdcIndex()));

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
							String.format("Excute F5 configSchedule Start-  vs add %s", adcSchedule.getVsIp()));
					addVServerF5(adcSchedule.getChunkF5().getVsConfig().getVsNew(), extraInfo);
					new OBAdcMonitorDB().writeAdcSlbScheduleStatus(adcSchedule.getIndex(),
							OBDefine.CONFIG_STATE.COMPLETE);

					msg = OBMessages.MSG_SLB_SCHEDULE_ADD;

					OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
							String.format("Excute F5 configSchedule End-  vs add %s", adcSchedule.getVsIp()));
					OBSystemLog.info(OBDefine.LOGFILE_ADCMON,
							String.format("Excute F5 SLBSchedule  - vs add %s", adcSchedule.getVsIp()));

				} else if (adcSchedule.getChangeType() == OBDefine.CHANGE_TYPE_DELETE) {
					OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
							String.format("Excute F5 configSchedule Start-  vs delete %s", adcSchedule.getVsIp()));
					vsIndexList.add(adcSchedule.getChunkF5().getVsConfig().getVsNew().getIndex());
					delVServer(adcSchedule.getAdcIndex(), vsIndexList, extraInfo);
					new OBAdcMonitorDB().writeAdcSlbScheduleStatus(adcSchedule.getIndex(),
							OBDefine.CONFIG_STATE.COMPLETE);

					msg = OBMessages.MSG_SLB_SCHEDULE_DELETE;

					OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
							String.format("Excute F5 configSchedule End-  vs delete %s", adcSchedule.getVsIp()));
					OBSystemLog.info(OBDefine.LOGFILE_ADCMON,
							String.format("Excute F5 SLBSchedule  - vs delete %s", adcSchedule.getVsIp()));
				} else if (adcSchedule.getChangeType() == OBDefine.CHANGE_TYPE_EDIT) {
					OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
							String.format("Excute F5 configSchedule Start-  vs modify %s", adcSchedule.getVsIp()));
					setVServerF5(adcSchedule.getChunkF5().getVsConfig().getVsNew(), extraInfo);
					new OBAdcMonitorDB().writeAdcSlbScheduleStatus(adcSchedule.getIndex(),
							OBDefine.CONFIG_STATE.COMPLETE);

					msg = OBMessages.MSG_SLB_SCHEDULE_MODIFY;

					OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
							String.format("Excute F5 configSchedule End-  vs modify %s", adcSchedule.getVsIp()));
					OBSystemLog.info(OBDefine.LOGFILE_ADCMON,
							String.format("Excute F5 SLBSchedule  - vs modify %s", adcSchedule.getVsIp()));
				} else {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("F5 configSchedule - Invalid config type vsip : %s", adcSchedule.getVsIp()));
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							"Error : Nothing to F5 configSchedule.");
				}

				if (adcSchedule.getNotice() != 0) {
					new OBVServerDB().addMessageToSMS(
							String.format(OBMessages.getMessage(msg), adcSchedule.getNotice()),
							adcSchedule.getSmsReceive());
				}

				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
						String.format("F5 configSchedule - end : %d", adcSchedule.getAdcIndex()));
			} catch (OBException e) {
				throw e;
			} catch (Exception e) {
				try {
					CommonF5.Exception("", e.getMessage());
				} catch (OBExceptionUnreachable e1) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("F5 configSchedule - failed system unreachable error : %s vsip : %s",
									e1.getMessage(), adcSchedule.getVsIp()));
					new OBAdcMonitorDB().writeAdcSlbScheduleStatus(adcSchedule.getIndex(),
							OBDefine.CONFIG_STATE.FAILED);
					throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e1.getMessage());
				} catch (OBExceptionLogin e1) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("F5 configSchedule - failed system login error : %s vsip : %s",
									e1.getMessage(), adcSchedule.getVsIp()));
					new OBAdcMonitorDB().writeAdcSlbScheduleStatus(adcSchedule.getIndex(),
							OBDefine.CONFIG_STATE.FAILED);
					throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e1.getMessage());
				} catch (OBException e1) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("F5 configSchedule - failed system error : %s vsip : %s", e1.getMessage(),
									adcSchedule.getVsIp()));
					new OBAdcMonitorDB().writeAdcSlbScheduleStatus(adcSchedule.getIndex(),
							OBDefine.CONFIG_STATE.FAILED);
					throw e1;
				} catch (Exception e1) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("F5 configSchedule - failed system format or null point error : %s vsip : %s",
									e1.getMessage(), adcSchedule.getVsIp()));
					new OBAdcMonitorDB().writeAdcSlbScheduleStatus(adcSchedule.getIndex(),
							OBDefine.CONFIG_STATE.FAILED);
					throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e1.getMessage());
				}
			}
		}

	}

	@Override
	public void setVServerNoticeOnAlteon(Integer adcIndex, ArrayList<OBDtoAdcVServerNotice> vsList,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void setVServerNoticeOffAlteon(Integer adcIndex, ArrayList<OBDtoAdcVServerNotice> vsList,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean isVServerSyncNotice(ArrayList<OBDtoAdcVServerNotice> vsList, Integer adcIndex) throws OBException {
		boolean result = false;

		if (vsList.isEmpty()) {
			return false;
		}

		int retVal = 0;

		ResultSet rs;

		String sqlText = "";

		String sqlNotice = "";
		ArrayList<String> parseNoticeName = new ArrayList<String>();

		for (OBDtoAdcVServerNotice noticeNames : vsList) {
			parseNoticeName.add(noticeNames.getVsName());
		}

		sqlNotice = OBParser.convertList2SingleQuotedString(parseNoticeName);

		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			sqlText = String.format(
					" SELECT COUNT(INDEX) AS CNT FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d AND NAME IN (%s) ", adcIndex,
					sqlNotice);

			rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				retVal = db.getInteger(rs, "CNT");
			}

			String indexNotice = OBParser.makeIndexString(adcIndex, vsList.get(0).getNoticePoolName());

			sqlText = String.format(" SELECT ADC_INDEX FROM TMP_SLB_POOL WHERE ADC_INDEX = %d AND INDEX = %s ",
					adcIndex, OBParser.sqlString(indexNotice));

			rs = db.executeQuery(sqlText);

			if (retVal == vsList.size() && rs.next() == true) {
				result = true;
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
		return result;
	}
}