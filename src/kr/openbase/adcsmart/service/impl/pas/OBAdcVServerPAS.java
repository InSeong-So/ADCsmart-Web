package kr.openbase.adcsmart.service.impl.pas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

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
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcProfile;
import kr.openbase.adcsmart.service.dto.OBDtoAdcRealServerGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcScope;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerNotice;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoNetworkInterface;
import kr.openbase.adcsmart.service.dto.OBDtoSLBUpdateStatus;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigChunkPAS;
import kr.openbase.adcsmart.service.impl.OBAdcConfigHistoryImpl;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBSystemAuditImpl;
import kr.openbase.adcsmart.service.impl.OBTelnetCmndExecV2;
import kr.openbase.adcsmart.service.impl.OBVServerDB;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcVServerAll;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcVlan;
import kr.openbase.adcsmart.service.impl.f5.DtoVlanTunnelFilter;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcNodePAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoConfigResultPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoConfigResultSetPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.OBAdcPASHandler;
import kr.openbase.adcsmart.service.impl.pas.handler.OBCLIParserPAS;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcHealthCheckPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcNodePASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.snmp.pas.OBSnmpPAS;
import kr.openbase.adcsmart.service.snmp.pas.dto.OBDtoAdcConfigSlbPAS;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBLicenseExpiredException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBAdcVServerPAS implements OBAdcVServer {
//	public static void main(String [] args) 
//	{
//		OBAdcVServerPAS tester = new OBAdcVServerPAS();
//		tester.test_getVServerListPAS(10);
//	}
//	private void testAddVerver()
//	{
//		try
//		{
//			OBDtoExtraInfo info = new OBDtoExtraInfo();
//			info.setAccountIndex(1);
//			info.setClientIPAddress("172.172.2.151");
//			info.setExtraMsg1("ykkim's add virtual server test");
//			
//			OBDtoAdcHealthCheckPAS health = new OBDtoAdcHealthCheckPAS();
//
//			health.setName("100");
//			health.setDbIndex("");
//
//			health.setInterval(7);
//			health.setPort(80);
//			health.setTimeout(6);
//			health.setType(OBDefine.HEALTH_CHECK_HTTP);
//			
//			ArrayList<OBDtoAdcPoolMemberPAS> memberList = new ArrayList<OBDtoAdcPoolMemberPAS>();
//			int i;
//			String ipBase = "10.10.10.";
//			for(i=1;i<=3;i++)
//			{
//				OBDtoAdcPoolMemberPAS member = new OBDtoAdcPoolMemberPAS();
//				member.setDbIndex(null);
//				member.setIpAddress(ipBase+i);
//				member.setName("r"+i);
//				member.setPort(80);
//				if(i%2==0)
//				{
//					member.setState(OBDefine.STATE_ENABLE);
//				}
//				else
//				{
//					member.setState(OBDefine.STATE_DISABLE);
//				}
//				memberList.add(member);
//			}
//			
//			OBDtoAdcPoolPAS pool = new OBDtoAdcPoolPAS();
//			pool.setDbIndex(null);
//			pool.setHealthCheckInfo(health);
//			pool.setLbMethod(OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER);
//			pool.setName(null);
//			pool.setMemberList(memberList);
//			
//			OBDtoAdcVServerPAS virtualServer = new OBDtoAdcVServerPAS();
//			virtualServer.setAdcIndex(4);
//			virtualServer.setName("ykkim7");
//			virtualServer.setPool(pool);
//			virtualServer.setSrvProtocol(OBDefine.PROTOCOL_TCP);
//			virtualServer.setSrvPort(8080);
//			virtualServer.setState(OBDefine.STATE_ENABLE);
//			virtualServer.setvIP("10.10.10.111");
//			
//			new OBAdcVServerPAS().addVServerPAS(virtualServer, info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//	private void testGetVServerList(Integer adcIndex)
//	{
//		OBAdcVServerPAS tester = new OBAdcVServerPAS();
//		try
//		{
//			ArrayList<OBDtoAdcVServerPAS> res = tester.getVServerListPAS(adcIndex);
//			for(OBDtoAdcVServerPAS pas:res)
//			{
//				System.out.println( "dbIndex=" + pas.getDbIndex());
//				System.out.println( "" + pas.getDbIndex());
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			new OBAdcVServerPAS().updateSLBStatus(2);
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public OBDtoSLBUpdateStatus updateSLBStatus(Integer adcIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		OBDtoSLBUpdateStatus retVal = new OBDtoSLBUpdateStatus();
		boolean bDownload = false;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			new OBAdcManagementImpl().waitUntilAdcAvailable(adcIndex, db);
			// CLI를 통해 SLB 데이터 업데이트함.
			try {
				bDownload = downloadSlbConfigCLI(adcIndex);
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex, db);
			} catch (OBExceptionUnreachable e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex, db);
				throw e;
			} catch (OBExceptionLogin e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex, db);
				throw e;
			} catch (OBException e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex, db);
				throw e;
			} catch (Exception e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex, db);
				throw e;
			} finally {
				if (db != null)
					db.closeDB();
			}

			// ADC 장비의 상태 업데이트.
			new OBAdcMonitorDB().writeAdcStatus(adcIndex, OBDefine.ADC_STATUS.REACHABLE);
		} catch (OBExceptionUnreachable e) {
			// ADC 장비의 상태 업데이트.
			new OBAdcMonitorDB().writeAdcStatus(adcIndex, OBDefine.ADC_STATUS.UNREACHABLE);
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			// ADC 장비의 상태 업데이트.
			new OBAdcMonitorDB().writeAdcStatus(adcIndex, OBDefine.ADC_STATUS.UNREACHABLE);
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
		retVal.setUpdateSuccess(bDownload);
		return retVal;
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

//	@Override
//	public Integer getVSTotalCount(Integer adcIndex) throws OBException
//	{
//		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("begin"));
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
//
//		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
//		return result;
//	}

	@Override
	public ArrayList<OBDtoAdcVServerPAS> getVServerListPAS(Integer adcIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("begin"));

		try {
			ArrayList<OBDtoAdcVServerPAS> list = new OBVServerDB().getVServerListAllPAS(adcIndex);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
			return list;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

//	@Override
//	public ArrayList<OBDtoAdcVServerPAS> getVServerListPAS(Integer adcIndex, Integer beginIndex, Integer endIndex) throws OBExceptionUnreachable, OBExceptionLogin, OBException
//	{
//		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("begin"));
//		
//		OBDatabase db = new OBDatabase();
//		OBDatabase db2 = new OBDatabase();
//		OBDatabase db3 = new OBDatabase();
//		try
//		{
//			db.openDB();
//		}
//		catch(OBException e)
//		{
//			throw new OBException(OBException.ERRCODE_SLB_VS_GETINFO, e);
//		}
//		try
//		{
//			db2.openDB();
//		}
//		catch(OBException e)
//		{
//			db.closeDB();
//			throw new OBException(OBException.ERRCODE_SLB_VS_GETINFO, e);
//		}
//		try
//		{
//			db3.openDB();
//		}
//		catch(OBException e)
//		{
//			db.closeDB();
//			db2.closeDB();
//			throw new OBException(OBException.ERRCODE_SLB_VS_GETINFO, e);
//		}
//		try
//		{
//			ArrayList<OBDtoAdcVServerPAS> list = new OBVServerDB().getVServerListAllPAS(adcIndex, beginIndex, endIndex, OBDefine.ORDER_TYPE_VSNAME, OBDefine.ORDER_DIR_ASCEND, db, db2, db3);
//			db.closeDB();
//			db2.closeDB();
//			db3.closeDB();
//			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
//			return list;
//		}
//		catch(OBException e)
//		{
//			db.closeDB();
//			db2.closeDB();
//			db3.closeDB();
//			throw new OBException(OBException.ERRCODE_SLB_VS_GETINFO, e);
//		}
//		catch(Exception e)
//		{
//			db.closeDB();
//			db2.closeDB();
//			db3.closeDB();
//			OBException e1 = new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//			throw new OBException(OBException.ERRCODE_SLB_VS_GETINFO, e1);
//		}
//	}

	private boolean downloadSlbConfigCLI(Integer adcIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			// telnet 접속 시도..
//			OBAdcPAS configHandler = new OBAdcPAS(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt());
			OBAdcPASHandler configHandler = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
			configHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());

			configHandler.login();
			String cfgDump = "";
			try {
				// dump file 가져옴...
				cfgDump = configHandler.cmndDumpcfg();
				configHandler.disconnect();
			} catch (OBException e) {
				configHandler.disconnect();
				throw e;
			}

			// db 업데이트...
			writeSlbConfig(adcInfo, cfgDump);
		} catch (OBExceptionUnreachable e) {
			throw e;
		} catch (OBExceptionLogin e) {
			throw e;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return true;
	}

//	@Override
//	public boolean downloadSlbConfig(Integer adcIndex, boolean force) throws OBExceptionUnreachable, OBExceptionLogin, OBException
//	{
//		OBDatabase db = new OBDatabase();
//		boolean bDownload = false;
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
//		
//		return bDownload;
//	}

	public OBDtoSLBUpdateStatus downloadSlbConfig(Integer adcIndex, boolean force)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. threadID:%d, isforce:%b", Thread.currentThread().getId(), force));

		OBDtoSLBUpdateStatus slbUpdateInfo = new OBDtoSLBUpdateStatus();

		String lockFile = OBDefine.DIR_LOCKFILE_SYSLOGD + adcIndex + "_slb.lock";
		if (OBCommon.isLockFileExist(lockFile) == true) {
			// LockFileExcetion 처리
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
					String.format("download slb config.(already running). threadID:%d, isforce:%b",
							Thread.currentThread().getId(), force));
			slbUpdateInfo.setUpdateSuccess(false);
			slbUpdateInfo.setExtraMsg(OBException.getExceptionMessage(OBException.ERRCODE_LOCK_FILE_EXCEPTION));
			slbUpdateInfo.setExtraMsg2(OBException.getExceptionMessage(OBException.ERRCODE_ADCADD_SLB_LOCK_EXCEPTION));
			return slbUpdateInfo;
		}
		try {
			OBCommon.createLockFile(lockFile);
			Timestamp deviceTime;

			deviceTime = new OBAdcVServerPAS().getDeviceApplyTime(adcIndex);

			boolean bDownload = false;
			slbUpdateInfo
					.setUpdateSuccess(bDownload = new OBAdcVServerPAS().downloadSlbConfig(adcIndex, force, deviceTime));

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
					String.format("end. result:%b, threadID:%d", bDownload, Thread.currentThread().getId()));
			return slbUpdateInfo;
		} catch (OBExceptionUnreachable e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			OBCommon.deleteLockFile(lockFile);
		}
	}

	public boolean downloadSlbConfig(Integer adcIndex, boolean force, Timestamp applyTime)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
				String.format("try to download Slb. threadID:%d, adcIndex:%d, isforce:%b",
						Thread.currentThread().getId(), adcIndex, force));

		OBDtoAdcInfo adcInfo;

		boolean bDownload = false;
//		String lockFile = OBDefine.DIR_LOCKFILE_SYSLOGD + adcIndex + "_slb.lock";
//        if(OBCommon.isLockFileExist(lockFile)==true)
//        {
//        	// LockFileExcetion 처리
//        	OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("download slb config.(already running). threadID:%d, isforce:%b", Thread.currentThread().getId(), force));
//        	return false;
//        }
		try {
			// OBCommon.createLockFile(lockFile);
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("invalid adc index:%d", adcIndex));
			}

			if (new OBTelnetCmndExecV2().isReachable(adcInfo.getAdcIpAddress(), adcInfo.getConnPort()) == false)
				throw new OBExceptionUnreachable("unreachable");

			if (false == OBCommon.checkVersionPAS(adcInfo.getSwVersion())) {
				throw new OBException(OBException.ERRCODE_ADC_VERSION);
			}

			bDownload = downloadSlbConfigCLI(adcInfo.getIndex());

			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
					String.format("end. threadID:%d, adcIndex:%d", Thread.currentThread().getId(), adcIndex));

			return bDownload;
		} catch (OBExceptionUnreachable e) {
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to download slb config. OBExceptionUnreachable, adcIndex:%d", adcIndex));
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to download slb config. OBExceptionLogin, adcIndex:%d", adcIndex));
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBException e) {
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to download slb config. OBException, adcIndex:%d", adcIndex));
			throw e;
		} catch (Exception e) {
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to download slb config. Exception, adcIndex:%d", adcIndex));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
//        finally
//        {
//            OBCommon.deleteLockFile(lockFile);
//        }
	}

	@Override
	public OBDtoAdcVServerPAS getVServerPASInfo(Integer adcIndex, String vsIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("begin. adcIndex=%d, vsIndex=%s", adcIndex, vsIndex));
		OBDtoAdcVServerPAS obj;
		try {
			obj = new OBVServerDB().getVServerInfoPAS(vsIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. vs = %s", obj));
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. vs = %s", obj));
		return obj;
	}

	@Override
	public void enableVServer(Integer adcIndex, ArrayList<String> vsIndexList, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("begin. virtualServers to enable :%s, extraInfo:%s", vsIndexList, extraInfo));
		try {
			setVServerEnableDisable(adcIndex, vsIndexList, OBDefine.STATE_ENABLE, extraInfo);
			ArrayList<String> tempName = new OBVServerDB().getVirtualServerNameList(vsIndexList);
			OBDtoAdcInfo adcInfo;
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "end.");
	}

	@Override
	public void disableVServer(Integer adcIndex, ArrayList<String> vsIndexList, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("begin. virtualServers to disable :%s", vsIndexList));
		try {
			setVServerEnableDisable(adcIndex, vsIndexList, OBDefine.STATE_DISABLE, extraInfo);
			ArrayList<String> tempName = new OBVServerDB().getVirtualServerNameList(vsIndexList);
			OBDtoAdcInfo adcInfo;
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "end.");
	}

	private void setVServerEnableDisable(int adcIndex, ArrayList<String> vsIndexList, int state,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBVServerDB vsDB = new OBVServerDB();
		ArrayList<String> vsTodoList = new ArrayList<String>(); // 실제로 enable/disable 상태가 바뀐 virtual server들의 이름 리스트

		// 장비 기본 확인
		OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
		if (adcInfo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					"setVServerEnableDisable() error.: adcInfo error(null).");
		}

		// 변경기록 준비
		OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
		ArrayList<OBDtoAdcConfigChunkPAS> configChunkList = new ArrayList<OBDtoAdcConfigChunkPAS>();
		new OBAdcManagementImpl().waitUntilAdcAvailable(adcIndex);
		// 중요!! 여기부터 decreaseAdcCheckCfg() 호출 부분까지는 일어날 수 있는 모든 exception에 대해 명시적으로
		// 처리한다.exception에서도 decreaseAdcCheckCfg()를 호출해야 한다.
		try {
			OBDtoAdcVServerPAS tempVs, tempVsNew;
			for (String vsIndex : vsIndexList) // virtual server old 상태를 잡아 놓는다.
			{
				tempVs = vsDB.getVServerInfoPAS(vsIndex);
				tempVsNew = OBAdcVServerPAS.cloneVServer(tempVs);
				tempVsNew.setState(state); // 이력 비교를 위해 새 vs가 갖게 될 state를 설정한다. 웹에서는 vsIndex리스트만 주지 newInfo를 주지 않기 때문에
											// DB에서 읽으면 기대 값을 설정해 줘야 한다.

				OBDtoAdcConfigChunkPAS tempChunk = new OBDtoAdcConfigChunkPAS();
				tempChunk = historyManager.MakeConfigChunkPAS(tempVs, tempVsNew, OBDefine.CHANGE_BY_USER,
						OBDefine.CHANGE_TYPE_EDIT, OBDefine.CHANGE_OBJECT_VIRTUALSERVER, extraInfo.getAccountIndex());
				if (tempChunk.getVsConfig().getStateChange() == OBDefine.CHANGE_TYPE_EDIT) // 실제로 변경할 대상만 이력을 보관한다.
				{
					vsTodoList.add(tempVs.getName());
					configChunkList.add(tempChunk);
				}
			}
		} catch (OBException e) {
			new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
			throw e;
		} catch (Exception e) {
			new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// State 설정 //실제로 state가 바뀌는 것들만 작업한다. enable->enable은 안함
		// OBAdcPAS configHandler = new OBAdcPAS(adcInfo.getAdcIpAddress(),
		// adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt());
		OBAdcPASHandler configHandler = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
		configHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
				adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());

		String cfgDump = "";
		try {
			configHandler.login();

			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "login OK");
			configHandler.enableDisableVirtualServer(vsTodoList, state);
			cfgDump = configHandler.cmndDumpcfg();
			configHandler.disconnect();
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs en/dis] logout FINE");
		} catch (OBExceptionUnreachable e) {
			new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBException e) {
			new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
			configHandler.disconnect();
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs en/dis] logout BAD");
			throw e;
		}
		try { // 변경기록
			for (OBDtoAdcConfigChunkPAS configChunk : configChunkList) {
				historyManager.writeConfigHistoryPAS(configChunk);
			}
			// 설정 전부 갱신, F5는 vs정보만 업데이트 할 수 있는데, 얘는 안 된다.
			// downloadSlbConfig(adcIndex, true);
			writeSlbConfig(adcInfo, cfgDump);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs en/dis] DONE");
			new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
			// 여기까지 decreaseAdcCheckCfg()가 포함된 exception을 한다.
		} catch (OBException e) {
			new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
			throw e;
		} catch (Exception e) {
			new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	// public static void main(String [] args)
//	{
//		OBDtoAdcVServerPAS vsInfo = new OBDtoAdcVServerPAS();
//		vsInfo.setAdcIndex(1);
//		vsInfo.setName("bwpark_test");
//		vsInfo.setSrvPort(8080);
//		vsInfo.setvIP("2.2.2.2");
//		vsInfo.setState(OBDefine.STATE_ENABLE);
//		vsInfo.getPool().setName("bwpark_test");
//		OBDtoAdcPoolMemberPAS memObj = new OBDtoAdcPoolMemberPAS();
//		memObj.setId(1);
//		memObj.setIpAddress("1.1.1.2");
//		vsInfo.getPool().getMemberList().add(memObj);
//		OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
//		extraInfo.setAccountIndex(0);
//		extraInfo.setClientIPAddress("172.172.2.206");
//		try
//		{
//			new OBAdcVServerPAS().addVServerPAS(vsInfo, extraInfo);
//		}
//		catch(OBExceptionUnreachable e)
//		{
//			e.printStackTrace();
//		}
//		catch(OBExceptionLogin e)
//		{
//			e.printStackTrace();
//		}
//		catch(OBLicenseExpiredException e)
//		{
//			e.printStackTrace();
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}
//	
	@Override
	public void addVServerPAS(OBDtoAdcVServerPAS virtualServer, OBDtoExtraInfo extraInfo)
			throws OBLicenseExpiredException, OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. virtualServer :%s, extraInfo:%s", virtualServer, extraInfo));

		OBDatabase db = new OBDatabase();
		OBDtoAdcInfo adcInfo = null;
		try {
			db.openDB();

			adcInfo = new OBAdcManagementImpl().getAdcInfo(virtualServer.getAdcIndex()); // ADC 먼저 확인해야 한다.
			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Fail to get adcInfo.(adcInfo is null)");
			}

			OBDtoConfigResultPAS result = null;
			new OBAdcManagementImpl().waitUntilAdcAvailable(virtualServer.getAdcIndex(), db);
			try {
				result = addVirtualServer(adcInfo, virtualServer, extraInfo, db);
				new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServer.getAdcIndex(), db);
			} catch (OBExceptionUnreachable e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServer.getAdcIndex(), db);
				throw e;
			} catch (OBExceptionLogin e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServer.getAdcIndex(), db);
				throw e;
			} catch (OBException e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServer.getAdcIndex(), db);
				throw e;
			} catch (Exception e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServer.getAdcIndex(), db);
				throw e;
			}

			if (result.getChange() == 0) // 바뀐 것이 없다.
			{
			} else if (result.getChange() == 1) // 설정 부분 적용
			{
				// 감사로그 = 부분 성공인데 성공으로 감사로그를 남긴다.
				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), virtualServer.getAdcIndex(),
						extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_ADD_SUCCESS, adcInfo.getName(),
						virtualServer.getName(), null);
			} else if (result.getChange() == 2) // 설정 완전 적용
			{
				// 감사로그 = 완전 성공
				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), virtualServer.getAdcIndex(),
						extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_ADD_SUCCESS, adcInfo.getName(),
						virtualServer.getName(), null);
			}
		} catch (OBExceptionUnreachable e) {
			// 실패 감사로그
			if (adcInfo != null)
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			// 실패 감사로그
			if (adcInfo != null)
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBException e) {
			// 실패 감사로그
			if (adcInfo != null)
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw e;
		} catch (Exception e) {
			// 실패 감사로그
			if (adcInfo != null)
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	private OBDtoConfigResultPAS addVirtualServer(OBDtoAdcInfo adcInfo, OBDtoAdcVServerPAS virtualServer,
			OBDtoExtraInfo extraInfo, OBDatabase db)
			throws OBLicenseExpiredException, OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
		OBDtoAdcConfigChunkPAS configChunk = null;
		OBDtoConfigResultPAS result = null;
		try {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] prepare history begin");
			// 변경이력 준비
			configChunk = historyManager.MakeConfigChunkPAS(null, virtualServer, OBDefine.CHANGE_BY_USER,
					OBDefine.CHANGE_TYPE_ADD, OBDefine.CHANGE_OBJECT_VIRTUALSERVER, extraInfo.getAccountIndex());
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] prepare history done");
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] telnet login");
		// 텔넷 연결.
		OBAdcPASHandler pasHandler = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
		pasHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
				adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());

		try {
			pasHandler.login();
		} catch (OBExceptionUnreachable e) {
			throw new OBExceptionUnreachable(e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBExceptionLogin(e.getMessage());
		}

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "login OK");

		try {
			result = pasHandler.configSlb(configChunk); // 사전 조사한 이력을 가지고 변경하러 간다.
			if (result != null) {
				result.setWriteHistory(false); // 아직 이력 기록 안 했음
				result.setWriteConfig(false); // 아직 설정 업데이트 안 했음
			}
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] config done WELL");
		} catch (OBException e) {// configSlb()는 근본적으로 결함이 있는 데이터를 받았을 때만 exception을 던진다. 여기를 타면 설정을 전혀 하지 않고
									// 시작부터 오류이다. 그러므로 바로 오류처리하고 나간다.
			pasHandler.disconnect(); // 실패일 때만 logout
			throw e;
		} catch (Exception e) {
			pasHandler.disconnect(); // 실패일 때만 logout
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// 설정을 마쳤기 때문에 여기부터는 exception으로 처리하지 않고 사유를 기록한 result class를 넘긴다.
		if (result != null && result.getChange() != 0) // 0:변경없음, 1:불완전 변경, 2:완전 변경
		{
			try // 이력 기록 구간 처리 try-catch block
			{
				if (result.getChange() == 1) // 불완전 변경이므로 이력을 다시 만든다.
				{
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] remake history begin");
					configChunk = historyManager.MakeConfigChunkPAS( // 변경이력 다시만들기
							null, result.getVirtualServer(), // 실제 적용한 virtual server
							OBDefine.CHANGE_BY_USER, OBDefine.CHANGE_TYPE_ADD, OBDefine.CHANGE_OBJECT_VIRTUALSERVER,
							extraInfo.getAccountIndex());
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] remake history done");
				}
				historyManager.writeConfigHistoryPAS(configChunk); // 변경이력 기록
				result.setWriteHistory(true); // 이력 기록 했음
			} catch (OBException e) {
				// pasHandler.disconnect();
				// throw e;
			} catch (Exception e) {
				// pasHandler.disconnect();
				// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
				// e.getMessage());
			}
			// DB 업데이트 구간 try-catch block
			String cfgDump = "";
			try {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] download start");
				cfgDump = pasHandler.cmndDumpcfg();
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] download middle");
				writeSlbConfig(adcInfo, cfgDump);
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] download done");
//				downloadSlbConfig(virtualServer.getAdcIndex(), true);
				result.setWriteConfig(true); // 설정 업데이트 했음
			} catch (OBException e1) {
				// pasHandler.disconnect();
				// throw e1;
			} catch (Exception e1) {
				// pasHandler.disconnect();
				// throw new OBException(e1.getMessage());
			}
		} else {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] nothing to do.");
		}

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] final telnet logout");
		pasHandler.disconnect(); // 정상 처리 했을 경우 telnet logout

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "end.");
		return result;
	}

	private void writeSlbConfig(OBDtoAdcInfo adcInfo, String cfgDump) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcInfo.getIndex()));

		long tt0 = 0, tt1 = 0, tt2 = 0, tt3 = 0, tt4 = 0;
		tt0 = System.currentTimeMillis();
		OBDatabase db = new OBDatabase();
		try {
			StringBuilder transactionQuery = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);
			transactionQuery.append(" BEGIN; ");

			// cfg dump 파일에서 slb 정보를 파싱.
			OBDtoAdcConfigSlbPAS slbCfg = new OBCLIParserPAS().getSlbInfo(adcInfo.getIndex(), cfgDump);

			OBVServerDB vDB = new OBVServerDB();

			tt1 = System.currentTimeMillis();
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "<time> download slb: " + (tt1 - tt0) / 1000.0);

			// 기존 db 삭제.
			vDB.delVServerAll(adcInfo.getIndex(), transactionQuery);
			vDB.delNodeAll(adcInfo.getIndex(), transactionQuery);
			vDB.delPoolAll(adcInfo.getIndex(), transactionQuery);
			vDB.delVServiceAll(adcInfo.getIndex(), transactionQuery);
			vDB.delPoolmemberAll(adcInfo.getIndex(), transactionQuery);
			vDB.delHealthCheckAll(adcInfo.getIndex(), transactionQuery);

			tt2 = System.currentTimeMillis();
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "<time> delete db: " + (tt2 - tt1) / 1000.0);

			// vs, pool, member 정보 저장.
			vDB.addPoolVServerPAS(adcInfo.getIndex(), slbCfg.getVsList(), transactionQuery);// vs, pool, poolmember 정보를
																							// 저장한다.
			vDB.addHealthCheckPAS(adcInfo.getIndex(), slbCfg.getHealthList(), transactionQuery);

			transactionQuery.append(" COMMIT; ");

			db.openDB();
			db.executeUpdate(transactionQuery.toString());

			// status update
			ArrayList<OBDtoAdcVServerPAS> list = new OBSnmpPAS(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
					.getSlbStatus(adcInfo.getIndex(), adcInfo.getAdcType(), adcInfo.getSwVersion());
			new OBAdcMonitorDB().writeSlbStatusPAS(adcInfo.getIndex(), list);

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
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "end. <time> update slb total: " + (tt4 - tt0) / 1000.0);
	}

	@Override
	public void setVServerPAS(OBDtoAdcVServerPAS virtualServer, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. virtualServer :%s, extraInfo:%s", virtualServer, extraInfo));
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "virtual server edit: " + virtualServer);

		OBDtoAdcInfo adcInfo = null;
		try {
			// adc 확인
			adcInfo = new OBAdcManagementImpl().getAdcInfo(virtualServer.getAdcIndex());
			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Fail to get adcInfo.(adcInfo is null)");
			}

			new OBAdcManagementImpl().waitUntilAdcAvailable(virtualServer.getAdcIndex());
			OBDtoConfigResultPAS result = null;
			try {
				// 중요!! 여기부터 decreaseAdcCheckCfg()를 호출 부분까지는 일어날 수 있는 모든 exception에 대해 명시적으로
				// 처리한다.거기서 decreaseAdcCheckCfg()를 호출해야 한다.
				result = setVirtualServer(adcInfo, virtualServer, extraInfo);
				new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServer.getAdcIndex());
			} catch (Exception e) {
				throw e;
			} finally {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServer.getAdcIndex());
			}

			if (result.getChange() == 0) // 바뀐 것이 없다.
			{
			} else if (result.getChange() == 1) // 설정 부분 적용
			{
				// 감사로그 = 부분 성공인데 성공으로 감사로그를 남긴다.
				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), virtualServer.getAdcIndex(),
						extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_SET_SUCCESS, adcInfo.getName(),
						virtualServer.getName(), null);
			} else if (result.getChange() == 2) // 설정 완전 적용
			{
				// 감사로그 = 완전 성공
				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), virtualServer.getAdcIndex(),
						extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_SET_SUCCESS, adcInfo.getName(),
						virtualServer.getName(), null);
			}
		} catch (OBExceptionUnreachable e) {
			// 실패 감사로그
			if (adcInfo != null)
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			// 실패 감사로그
			if (adcInfo != null)
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBException e) {
			// 실패 감사로그
			if (adcInfo != null)
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw e;
		} catch (Exception e) {
			// 실패 감사로그
			if (adcInfo != null)
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	private OBDtoConfigResultPAS setVirtualServer(OBDtoAdcInfo adcInfo, OBDtoAdcVServerPAS virtualServer,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "virtual server edit: " + virtualServer);

		// virtualServer.setState(OBDefine.STATE_ENABLE);//TEST CODE : 웹에서 처리가 안 돼고 0으로
		// 주기 때문에 테스트 목적으로 일시적 조작

		// 변경기록 준비
		OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
		OBDtoAdcConfigChunkPAS configChunk = null;
		OBDtoConfigResultPAS result = null;
		OBDtoAdcVServerPAS virtualServerOld = null;
		try {
			OBVServerDB vDB = new OBVServerDB();

			// old Virtual 서버 확인
			virtualServerOld = vDB.getVServerInfoPAS(virtualServer.getDbIndex());
			if (virtualServerOld == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "VirtualServer doesn't exist.");
			}
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs set] prepare history begin");
			configChunk = historyManager.MakeConfigChunkPAS(virtualServerOld, virtualServer, OBDefine.CHANGE_BY_USER,
					OBDefine.CHANGE_TYPE_EDIT, OBDefine.CHANGE_OBJECT_VIRTUALSERVER, extraInfo.getAccountIndex());
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] prepare history done");
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		// 텔넷 연결.
//		OBAdcPAS pasHandler = new OBAdcPAS(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt());
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs set] telnet login");
		OBAdcPASHandler pasHandler = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
		pasHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
				adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());

		try {
			pasHandler.login();
		} catch (OBExceptionUnreachable e) {
			throw new OBExceptionUnreachable(e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBExceptionLogin(e.getMessage());
		}

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "login OK");

		try {
			result = pasHandler.configSlb(configChunk); // 사전 조사한 이력을 가지고 변경하러 간다.
			if (result != null) {
				result.setWriteHistory(false); // 아직 이력 기록 안 했음
				result.setWriteConfig(false); // 아직 설정 업데이트 안 했음
			}
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs set] config done WELL");
		} catch (OBException e) {// configSlb()는 근본적으로 결함이 있는 데이터를 받았을 때만 exception을 던진다. 여기를 타면 설정을 전혀 하지 않고
									// 시작부터 오류이다. 그러므로 바로 오류처리하고 나간다.
			pasHandler.disconnect(); // 실패일 때만 logout
			throw e;
		} catch (Exception e) {
			pasHandler.disconnect(); // 실패일 때만 logout
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// 설정을 마쳤기 때문에 여기부터는 exception으로 처리하지 않고 사유를 기록한 result class를 넘긴다.
		if (result != null && result.getChange() != 0) // 0:변경없음, 1:불완전 변경, 2:완전 변경
		{
			try // 이력 기록 구간 처리 try-catch block
			{
				if (result.getChange() == 1) // 불완전 변경이므로 이력을 다시 만든다.
				{
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs set] remake history begin");
					configChunk = historyManager.MakeConfigChunkPAS( // 변경이력 다시만들기
							virtualServerOld, result.getVirtualServer(), // 실제 적용한 virtual server
							OBDefine.CHANGE_BY_USER, OBDefine.CHANGE_TYPE_EDIT, OBDefine.CHANGE_OBJECT_VIRTUALSERVER,
							extraInfo.getAccountIndex());
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs set] remake history done");
				}
				historyManager.writeConfigHistoryPAS(configChunk); // 변경기록
				result.setWriteHistory(true); // 이력 기록 했음
			} catch (OBException e) {
				// pasHandler.disconnect();
				// throw e;
			} catch (Exception e) {
				// pasHandler.disconnect();
				// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
				// e.getMessage());
			}

			// DB 업데이트 구간 try-catch block
			String cfgDump = "";
			try {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs set] download start");
				cfgDump = pasHandler.cmndDumpcfg();
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs set] download middle");
				writeSlbConfig(adcInfo, cfgDump);
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs set] download done");
//				downloadSlbConfig(virtualServer.getAdcIndex(), true);
				result.setWriteConfig(true); // 설정 업데이트 했음
			} catch (OBException e1) {
				// pasHandler.disconnect();
				// throw e1;
			} catch (Exception e1) {
				// pasHandler.disconnect();
				// throw new OBException(e1.getMessage());
			}
		} else {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs set] nothing to do.");
		}

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs set] final telnet logout");
		pasHandler.disconnect(); // 정상 처리 했을 경우 telnet logout

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "end.");
		return result;
	}

	@Override
	public void delVServer(Integer adcIndex, ArrayList<String> vsIndexList, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. vsIndexList:%s", vsIndexList));

		OBDtoAdcInfo adcInfo = null;
		try {
			// adc 확인
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Fail to get adcInfo.(adcInfo is null)");
			}

			if (vsIndexList.size() == 0) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						"No virtual server to delete. Select one or more virtual servers.");
			}

			new OBAdcManagementImpl().waitUntilAdcAvailable(adcIndex);
			OBDtoConfigResultSetPAS resultSet = null;
			// 중요!! 여기부터 decreaseAdcCheckCfg()를 호출 부분까지는 일어날 수 있는 모든 exception에 대해 명시적으로
			// 처리한다.거기서 decreaseAdcCheckCfg()를 호출해야 한다.
			try {
				resultSet = delVServer(adcInfo, vsIndexList, extraInfo);
			} catch (OBExceptionUnreachable e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
				throw e;
			} catch (OBExceptionLogin e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
				throw e;
			} catch (OBException e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
				throw e;
			} catch (Exception e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
				throw e;
			}
			if (resultSet.getChange() == 0) // 바뀐 것이 없다.
			{
			} else if (resultSet.getChange() == 1) // 설정 부분 적용
			{
			} else if (resultSet.getChange() == 2) // 설정 완전 적용
			{
			}
		} catch (OBExceptionUnreachable e) {
			// 실패 감사로그
			if (adcInfo != null)
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			// 실패 감사로그
			if (adcInfo != null)
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBException e) {
			// 실패 감사로그
			if (adcInfo != null)
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw e;
		} catch (Exception e) {
			// 실패 감사로그
			if (adcInfo != null)
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	private OBDtoConfigResultSetPAS delVServer(OBDtoAdcInfo adcInfo, ArrayList<String> vsIndexList,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		// 변경기록 준비
		OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
		OBVServerDB vsDB = new OBVServerDB();

		ArrayList<OBDtoAdcConfigChunkPAS> configChunkList = new ArrayList<OBDtoAdcConfigChunkPAS>();
		OBDtoConfigResultSetPAS resultSet = null;
		try {
			for (String vsIndex : vsIndexList) // virtual server old 상태를 잡아 놓는다.
			{
				configChunkList.add(historyManager.MakeConfigChunkPAS(vsDB.getVServerInfoPAS(vsIndex), null,
						OBDefine.CHANGE_BY_USER, OBDefine.CHANGE_TYPE_DELETE, OBDefine.CHANGE_OBJECT_VIRTUALSERVER,
						extraInfo.getAccountIndex()));
			} // 변경기록 준비 끝
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBAdcPASHandler configHandler = null;
		try {
			configHandler = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
			configHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
			configHandler.login();
		} catch (OBExceptionUnreachable e) {
			throw e;
		} catch (OBExceptionLogin e) {
			throw e;
		}

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "login OK");
		try {
			resultSet = configHandler.delVirtualServer(configChunkList);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs del] config done");
		} catch (Exception e) {// 근본적으로 결함이 있는 경우만 exception이 올텐데 사실상 없다. 향후 생길 수 있으므로 바로 오류처리하고 나가게 만들어 둔다.
			configHandler.disconnect(); // 실패일 때만 logout
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// 설정을 마쳤기 때문에 여기부터는 오류가 나도 exception으로 처리하지 않고 사유를 기록한 result class를 처리한다.

		if (resultSet.getChange() > 0) // 설정 한 것이 있다. 완전 or 불완전
		{
			int i = 0;
			int listSize = resultSet.getResultList().size();
			try // 이력 기록 구간 처리 try-catch block
			{
				// 변경기록
				for (i = 0; i < listSize; i++) {
					if (resultSet.getResultList().get(i) != null) { // 감사로그
						new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcInfo.getIndex(),
								extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_DEL_SUCCESS,
								adcInfo.getName(), configChunkList.get(i).getVsConfig().getVsOld().getName(), null);
						historyManager.writeConfigHistoryPAS(configChunkList.get(i)); // 이력 기록
						resultSet.getResultList().get(i).setWriteHistory(true); // 이력을 기록함
					}
				}
				resultSet.setWriteHistory(true);
			} catch (OBException e) {
				resultSet.setWriteHistory(false); // 이력 기록 불완전함
				// configHandler.disconnect();
				// throw e;
			} catch (Exception e) {
				resultSet.setWriteHistory(false); // 이력 기록 불완전함
				// configHandler.disconnect();
				// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL,
				// e.getMessage());
			}

			// DB 업데이트 구간 try-catch block
			String cfgDump = "";
			try {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs del] download start");
				cfgDump = configHandler.cmndDumpcfg();
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs del] download middle");
				writeSlbConfig(adcInfo, cfgDump);
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs del] download done");
				resultSet.setWriteConfig(true); // 설정 업데이트 했음. 맨 첫 항목에 대표로 기록한다.
			} catch (OBException e1) {
				resultSet.setWriteConfig(false);
				// configHandler.disconnect();
				// throw e1;
			} catch (Exception e1) {
				resultSet.setWriteConfig(false);
				// configHandler.disconnect();
				// throw new OBException(e1.getMessage());
			}
		} else {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs del] nothing done.");
		}
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs del] final telnet logout");
		configHandler.disconnect(); // 정상 처리 했을 경우 telnet logout

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "end.");
		return resultSet;
//		catch(Exception e)
//		{
//			OBException e1 = new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//			throw new OBException(OBException.ERRCODE_SLB_VS_DEL, e1);
//		}
	}

	@Override
	public ArrayList<OBDtoAdcPoolPAS> getPoolPASList(Integer adcIndex) throws OBException {
		try {
			return new OBVServerDB().getPoolListAllPAS(adcIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public OBDtoAdcPoolPAS getPoolPAS(String poolIndex) throws OBException {
		try {
			return new OBVServerDB().getPoolInfoPAS(poolIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public ArrayList<OBDtoAdcNodePAS> getNodeAvailableListPAS(Integer adcIndex, String poolIndex) throws OBException {
		try {
			return new OBVServerDB().getNodeAvailableListPAS(adcIndex, poolIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

//	@Override
//	public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(Integer adcIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException
//	{
//		return new OBVServerDB().searchVServerListPAS(adcIndex, searchKey, beginIndex, endIndex, OBDefine.ORDER_TYPE_VSNAME, OBDefine.ORDER_DIR_ASCEND);
//	}

	@Override
	public Integer searchVServerListPASCount(Integer adcIndex, String searchKey) throws OBException {
		return new OBVServerDB().searchVServerListPASCount(adcIndex, searchKey);
	}

	@Override
	public void revertSlbConfig(Integer adcIndex, String revertConfig, String currentConfig, String newVServiceList,
			String newPoolList, String newNodeList) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		// TODO Auto-generated method stub
	}

	public void revertSlbConfig(Integer adcIndex, OBDtoAdcConfigChunkPAS configChunk, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException {
		if (configChunk.getChangeObject() == OBDefine.CHANGE_OBJECT_VIRTUALSERVER) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Revert vs start");
			ArrayList<String> vsIndexList = new ArrayList<String>();

			if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_ADD) {
				vsIndexList.add(configChunk.getVsConfig().getVsNew().getDbIndex()); // ADD는 NEW에서 빼서 지운다.
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Revert - vs delete");
				delVServer(adcIndex, vsIndexList, extraInfo);
			} else if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_DELETE) {
				// vsIndexList.add(configChunk.getVsConfig().getVsOld().getIndex()); //add는 복수로
				// 하지 않기 때문에 필요없다.
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Revert - vs add");
				addVServerPAS(configChunk.getVsConfig().getVsOld(), extraInfo); // DELETE는 OLD에서 빼서 살린다.
			} else if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_EDIT) {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Revert - vs edit");
				setVServerPAS(configChunk.getVsConfig().getVsOld(), extraInfo);
			} else {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Error: Nothing to revert.");
			}
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Revert done well");
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
	public boolean isExistVirtualServerPAS(Integer adcIndex, String vsName) throws OBException {
		OBDatabase db = new OBDatabase();
		boolean retVal = true;
		String sqlText = "";
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX=%d AND NAME=%s ", adcIndex,
					OBParser.sqlString(vsName));
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				retVal = false;
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

	public static OBDtoAdcVServerPAS cloneVServer(OBDtoAdcVServerPAS original) {
		OBDtoAdcVServerPAS clone = null;
		if (original != null) {
			clone = new OBDtoAdcVServerPAS(original);
		}

		return clone;
	}

	public static OBDtoAdcPoolPAS clonePool(OBDtoAdcPoolPAS original) {
		OBDtoAdcPoolPAS clone = new OBDtoAdcPoolPAS(original);

		return clone;
	}

//	private Timestamp getDeviceApplyTime(int adcIndex, OBDatabase db) throws Exception
//	{
//		String sqlText;
//		sqlText = String.format(" SELECT APPLY_TIME                   \n" +
//		                        " FROM TMP_SLB_VSERVER                \n " +
//		                        " WHERE ADC_INDEX=%d                  \n" +
//		                        " ORDER BY APPLY_TIME DESC LIMIT 1;   \n",
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

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public Timestamp getDeviceApplyTime(int adcIndex) throws Exception {
		String sqlText;
		sqlText = String.format(
				" SELECT APPLY_TIME                   \n" + " FROM TMP_SLB_VSERVER                \n "
						+ " WHERE ADC_INDEX=%d                  \n" + " ORDER BY APPLY_TIME DESC LIMIT 1;   \n",
				adcIndex);

		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return null;
			}
			return db.getTimestamp(rs, "APPLY_TIME");
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new Exception(String.format("sqlText:%s. message:%s", sqlText, e.getMessage()));
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	//////////////////////////////////////////////////////////////////////////
	// FROM HERE, NOT SUPPORTED FUNCTIONS FOR PAS
	@Override
	public OBDtoAdcVServerAlteon getVServerAlteonInfo(Integer adcIndex, String vsIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public OBDtoAdcVServerF5 getVServerF5Info(Integer adcIndex, String vsIndex)
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

//	@Override
//	public ArrayList<OBDtoAdcVServerAlteon> getVServerListAlteon(Integer adcIndex, Integer beginIndex, Integer endIndex)
//			throws OBExceptionUnreachable, OBExceptionLogin, OBException
//	{
//		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
//	}
	@Override
	public ArrayList<OBDtoAdcVServerF5> getVServerListF5(Integer adcIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

//	@Override
//	public ArrayList<OBDtoAdcVServerF5> getVServerListF5(Integer adcIndex, Integer beginIndex, Integer endIndex)
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
//	public ArrayList<OBDtoAdcVServerAlteon> searchVServerListAlteon(
//			Integer adcIndex, String searchKey, Integer beginIndex,
//			Integer endIndex) throws OBException
//	{
//		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
//	}
	@Override
	public Integer searchVServerListAlteonCount(Integer adcIndex, String searchKey) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

//	@Override
//	public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex,
//			String searchKey, Integer beginIndex, Integer endIndex)
//			throws OBException
//	{
//		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");	}
	@Override
	public Integer searchVServerListF5Count(Integer adcIndex, String searchKey) throws OBException {
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
	public boolean checkVrrp(String ipaddress, String accountID, String password, String swVersion, String vsIP,
			Integer routerIndex, Integer vrIndex, Integer ifNum)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

//	@Override
//	public ArrayList<OBDtoAdcProfile> getProfileList(Integer adcIndex)
//			throws OBException
//	{
//		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
//	}
	@Override
	public void syncConifgF5(Integer adcIndex, OBDtoExtraInfo extraInfo) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean isAvailableVirtualServerAlteon(Integer adcIndex, Integer port, String alteonID, String ipAddress)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean isAvailableVirtualServerF5(Integer adcIndex, String vsName, String ipAddress, Integer port,
			String alteonId) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
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
	public boolean isExistVirtualServerF5(Integer adcIndex, String vsName) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public boolean isExistVirtualServerPASK(Integer adcIndex, String vsName) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcPoolAlteon> getPoolAlteonList(Integer adcIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcPoolF5> getPoolF5List(Integer adcIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcPoolPASK> getPoolPASKList(Integer adcIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public OBDtoAdcPoolAlteon getPoolAlteon(String poolIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public OBDtoAdcPoolF5 getPoolF5(String poolIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void relashVServerF5(OBDtoAdcVServerF5 virtualServerNew, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public OBDtoAdcPoolPASK getPoolPASK(String poolIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcNodeAlteon> getNodeAvailableListAlteon(Integer adcIndex, String poolIndex)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcNodeF5> getNodeAvailableListF5(Integer adcIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcNodePASK> getNodeAvailableListPASK(Integer adcIndex, String poolIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void addVServerAlteon(OBDtoAdcVServerAlteon virtualServer, OBDtoExtraInfo extraInfo)
			throws OBLicenseExpiredException, OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void addVServerF5(OBDtoAdcVServerF5 virtualServer, OBDtoExtraInfo extraInfo)
			throws OBLicenseExpiredException, OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void addVServerPASK(OBDtoAdcVServerPASK virtualServer, OBDtoExtraInfo extraInfo)
			throws OBLicenseExpiredException, OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void setVServerAlteon(OBDtoAdcVServerAlteon virtualServer, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void setVServerF5(OBDtoAdcVServerF5 virtualServer, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void setVServerPASK(OBDtoAdcVServerPASK virtualServer, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
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

	@Override
	public String getValidVSIndex(Integer adcIndex, String vsIndex) throws OBException {
		// PAS의 경우에는 아무런 작업을 하지 않는다.
		return vsIndex;
	}

	@Override
	public String getValidPoolIndex(Integer adcIndex, String poolIndex) throws OBException {
		// PAS의 경우에는 아무런 작업을 하지 않는다.
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPAS> searchVSListUsedByPoolPAS(Integer adcIndex, String poolName)
			throws OBException {
		return new OBVServerDB().searchVSListUsedByPoolPAS(adcIndex, poolName);
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
//	@Override
//	public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException
//	{
//		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
//	}
//	@Override
//	public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(Integer adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex) throws OBException
//	{
//		return new OBVServerDB().searchVServerListPAS(adcIndex, accntIndex, searchKey, beginIndex, endIndex, OBDefine.ORDER_TYPE_VSNAME, OBDefine.ORDER_DIR_ASCEND);
//	}
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
	public Integer searchVServerListAllListCount(OBDtoAdcScope scope, Integer accntIndex, String searchKey)
			throws OBException {
		return new OBVServerDB().searchVServerAllListCoreCount(scope, accntIndex, searchKey);
	}

	@Override
	public Integer searchVServerListAlteonCount(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public Integer searchVServerListF5Count(Integer adcIndex, Integer accntIndex, String searchKey) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public Integer searchVServerListPASCount(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		return new OBVServerDB().searchVServerListPASCount(adcIndex, accntIndex, searchKey);
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPAS> getVServerListPAS(Integer adcIndex, Integer beginIndex, Integer endIndex,
			Integer orderType, Integer orderDir) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("begin"));

		try {
			ArrayList<OBDtoAdcVServerPAS> list = new OBVServerDB().getVServerListAllPAS(adcIndex, beginIndex, endIndex,
					OBDefine.ORDER_TYPE_VSNAME, OBDefine.ORDER_DIR_ASCEND);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
			return list;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(Integer adcIndex, String searchKey, Integer beginIndex,
			Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		return new OBVServerDB().searchVServerListPAS(adcIndex, searchKey, beginIndex, endIndex, orderType, orderDir);
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		return new OBVServerDB().searchVServerListPAS(adcIndex, accntIndex, searchKey, beginIndex, endIndex, orderType,
				orderDir);
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
		// PAS는 본 기능을 제공하지 않는다. 무조건 true한다.
		return true;
	}

	@Override
	public boolean isPeerVrrpValid(Integer adcIndex, Integer peerIndex) throws OBException {
		// PAS는 본 기능을 제공하지 않는다. 무조건 true한다.
		return true;
	}

	@Override
	public ArrayList<OBDtoAdcNodeDetail> searchNodeListF5(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public void setNodeState(Integer adcIndex, ArrayList<OBDtoAdcNodeF5> nodeList, Integer action,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerNotice> searchVServerNoticeOnList(Integer adcIndex, Integer accntIndex,
			String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public Integer searchVServerNoticeOnListCount(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerNotice> searchVServerNoticeOffList(Integer adcIndex, Integer accntIndex,
			String searchKey, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public Integer searchVServerNoticeOffListCount(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
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
	public ArrayList<OBDtoAdcNodeDetail> searchGroupListAlteon(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer orderType, Integer orderDir) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcNodeDetail> searchNodeListAlteon(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public Integer searchNodeAllListCount(OBDtoAdcScope scope, Integer accntIndex, String searchKey)
			throws OBException {
		return new OBVServerDB().searchNodeAllListCountCore(scope, accntIndex, searchKey);
	}

	@Override
	public boolean timeSyncCheck(Integer adcIndex) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}
}