package kr.openbase.adcsmart.service.impl.pask;

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
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigChunkPASK;
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
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcConfigSlbPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcHealthCheckPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcNodePASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.OBAdcPASKHandler;
import kr.openbase.adcsmart.service.impl.pask.handler.OBCLIParserPASK;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBLicenseExpiredException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBAdcVServerPASK implements OBAdcVServer {
	// public static void main(String [] args)
	// {
	// OBAdcVServerPASK tester = new OBAdcVServerPASK();
	// tester.test_getVServerListPASK(10);
	// }

	// private void testAddVerver()
	// {
	// try
	// {
	// OBDtoExtraInfo info = new OBDtoExtraInfo();
	// info.setAccountIndex(1);
	// info.setClientIPAddress("172.172.2.151");
	// info.setExtraMsg1("ykkim's add virtual server test");
	//
	// OBDtoAdcHealthCheckPASK health = new OBDtoAdcHealthCheckPASK();
	//
	// health.setName("100");
	// health.setDbIndex("");
	//
	// health.setInterval(7);
	// health.setPort(80);
	// health.setTimeout(6);
	// health.setType(OBDefine.HEALTH_CHECK_HTTP);
	//
	// ArrayList<OBDtoAdcPoolMemberPASK> memberList = new
	// ArrayList<OBDtoAdcPoolMemberPASK>();
	// int i;
	// String ipBase = "10.10.10.";
	// for(i=1;i<=3;i++)
	// {
	// OBDtoAdcPoolMemberPASK member = new OBDtoAdcPoolMemberPASK();
	// member.setDbIndex(null);
	// member.setIpAddress(ipBase+i);
	// member.setName("r"+i);
	// member.setPort(80);
	// if(i%2==0)
	// {
	// member.setState(OBDefine.STATE_ENABLE);
	// }
	// else
	// {
	// member.setState(OBDefine.STATE_DISABLE);
	// }
	// memberList.add(member);
	// }
	//
	// OBDtoAdcPoolPASK pool = new OBDtoAdcPoolPASK();
	// pool.setDbIndex(null);
	// pool.setHealthCheckInfo(health);
	// pool.setLbMethod(OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER);
	// pool.setName(null);
	// pool.setMemberList(memberList);
	//
	// OBDtoAdcVServerPASK virtualServer = new OBDtoAdcVServerPASK();
	// virtualServer.setAdcIndex(4);
	// virtualServer.setName("ykkim7");
	// virtualServer.setPool(pool);
	// virtualServer.setSrvProtocol(OBDefine.PROTOCOL_TCP);
	// virtualServer.setSrvPort(8080);
	// virtualServer.setState(OBDefine.STATE_ENABLE);
	// virtualServer.setvIP("10.10.10.111");
	//
	// new OBAdcVServerPASK().addVServerPASK(virtualServer, info);
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }
	// private void testGetVServerList(Integer adcIndex)
	// {
	// OBAdcVServerPASK tester = new OBAdcVServerPASK();
	// try
	// {
	// ArrayList<OBDtoAdcVServerPASK> res = tester.getVServerListPASK(adcIndex);
	// for(OBDtoAdcVServerPASK pask:res)
	// {
	// System.out.println( "dbIndex=" + pask.getDbIndex());
	// System.out.println( "" + pask.getDbIndex());
	// }
	// }
	// catch(Exception e)
	// {
	// e.printStackTrace();
	// }
	// }

//    public static void main(String[] args)
//    {
//        try
//        {
//            OBAdcVServerPASK tester = new OBAdcVServerPASK();
//            // new OBAdcVServerPASK().updateSLBStatus(9);
//
//            // healthcheck 구하는 함수 테스트
//            // OBDtoAdcHealthCheckPASK hc = tester.getHealthCheckPASK("2_1");
//            // System.out.println("healthcheck = " + hc);
//
//            // 가용 node list 가져오기
//            ArrayList<OBDtoAdcNodePASK> nodelist = tester.getNodeAvailableListPASK(2, "2_ykk12");
//            System.out.println("node list = " + nodelist);
//        }
//        catch(OBException e)
//        {
//            e.printStackTrace();
//        }
//    }

	@Override
	public OBDtoSLBUpdateStatus updateSLBStatus(Integer adcIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		OBDtoSLBUpdateStatus retVal = new OBDtoSLBUpdateStatus();
		boolean bDownload = false;

		try {
			new OBAdcManagementImpl().waitUntilAdcAvailable(adcIndex);
			// CLI를 통해 SLB 데이터 업데이트함.
			try {
				bDownload = downloadSlbConfigCLI(adcIndex);
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
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

	// @Override
	// public Integer getVSTotalCount(Integer adcIndex) throws OBException
	// {
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("begin"));
	// OBDatabase db = new OBDatabase();
	// int result = 0;
	// try
	// {
	// db.openDB();
	// result = new OBVServerDB().getTotalVServerCount(adcIndex, db);
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
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	// return result;
	// }

	@Override
	public ArrayList<OBDtoAdcVServerPASK> getVServerListPASK(Integer adcIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("begin"));
		ArrayList<OBDtoAdcVServerPASK> list = new OBVServerDB().getVServerListAllPASK(adcIndex);
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
		return list;
	}

	// @Override
	public ArrayList<OBDtoAdcVServerPASK> getVServerListPASK(Integer adcIndex, Integer beginIndex, Integer endIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("begin"));

		try {
			ArrayList<OBDtoAdcVServerPASK> list = new OBVServerDB().getVServerListAllPASK(adcIndex, beginIndex,
					endIndex, OBDefine.ORDER_TYPE_VSNAME, OBDefine.ORDER_DIR_ASCEND);

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
			return list;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public boolean downloadSlbConfigCLI(Integer adcIndex) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			// telnet 접속 시도..
			OBAdcPASKHandler configHandler = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
			configHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());

			configHandler.login();
			String slbDump = "";
			String realDump = "";
			String healthDump = "";
			String infoSlbDump = "";
			try {
				// dump fileS
				slbDump = configHandler.cmndSlbDump();
				// OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("show slb done"));
				realDump = configHandler.cmndRealDump();
				// OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("show real done"));
				healthDump = configHandler.cmndHealthDump();
				// OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("show health done"));
				infoSlbDump = configHandler.cmndShowInfoSlb();
				// OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("show info-slb
				// done"));
				configHandler.disconnect();
			} catch (OBException e) {
				configHandler.disconnect();
				throw e;
			}

			// db 업데이트...
			writeSlbConfig(adcInfo, slbDump, realDump, healthDump, infoSlbDump);
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

	// @Override
	// public boolean downloadSlbConfig(Integer adcIndex, boolean force) throws
	// OBExceptionUnreachable, OBExceptionLogin, OBException
	// {
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d
	// isforce:%b", adcIndex, force));
	// OBDatabase db = new OBDatabase();
	// boolean bDownload = false;
	//
	// try
	// {
	// db.openDB();
	// bDownload = downloadSlbConfig(adcIndex, force, db);
	// }
	// catch(OBExceptionUnreachable e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE,
	// e.getMessage());
	// }
	// catch(OBExceptionLogin e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
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
	// OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%b",
	// bDownload));
	// return bDownload;
	// }

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public OBDtoSLBUpdateStatus downloadSlbConfig(Integer adcIndex, boolean force)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. threadID:%d, isforce:%b", Thread.currentThread().getId(), force));

		OBDtoSLBUpdateStatus slbUpdateInfo = new OBDtoSLBUpdateStatus();

		try {
			Timestamp deviceTime;

			deviceTime = new OBAdcVServerPASK().getDeviceApplyTime(adcIndex);

			boolean bDownload = false;
			slbUpdateInfo.setUpdateSuccess(
					bDownload = new OBAdcVServerPASK().downloadSlbConfig(adcIndex, force, deviceTime));

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
		}
	}

	public boolean downloadSlbConfig(Integer adcIndex, boolean force, Timestamp applyTime)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
				String.format("try to download Slb. threadID:%d, adcIndex:%d, isforce:%b",
						Thread.currentThread().getId(), adcIndex, force));
		OBDtoAdcInfo adcInfo;

		boolean bDownload = false;
		String lockFile = OBDefine.DIR_LOCKFILE_SYSLOGD + adcIndex + "_slb.lock";
		if (OBCommon.isLockFileExist(lockFile) == true) {
			// LockFileExcetion 처리
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
					String.format("download slb config.(already running). threadID:%d, isforce:%b",
							Thread.currentThread().getId(), force));
			return false;
		}
		try {
			OBCommon.createLockFile(lockFile);
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("invalid adc index:%d", adcIndex));
			}

			if (new OBTelnetCmndExecV2().isReachable(adcInfo.getAdcIpAddress(), adcInfo.getConnPort()) == false)
				throw new OBExceptionUnreachable("unreachable");

			if (false == OBCommon.checkVersionPASK(adcInfo.getSwVersion())) {
				throw new OBException(OBException.ERRCODE_ADC_VERSION, adcInfo.getSwVersion());
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
		} finally {
			OBCommon.deleteLockFile(lockFile);
		}
	}

	public boolean downloadSlbConfig(Integer adcIndex, boolean force, Timestamp applyTime, OBAdcPASKHandler adcHandler)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. threadID:%d, isforce:%b", Thread.currentThread().getId(), force));
		OBDtoAdcInfo adcInfo;

		boolean bDownload = false;
		try {
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("invalid adc index:%d", adcIndex));
			}
			if (force == true || applyTime == null) {// 무조건 다운로드한다.
				bDownload = downloadSlbConfigCLI(adcInfo.getIndex());
			} else {
				// apply time 추출.
				String slbDump = adcHandler.cmndSlbDump();
				Timestamp curApplyTime = new OBCLIParserPASK().parseApplyTime(slbDump);
				if (applyTime.equals(curApplyTime) == false)
					bDownload = downloadSlbConfigCLI(adcInfo.getIndex());
			}
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. threadID:%d", Thread.currentThread().getId()));
			return bDownload;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public OBDtoAdcVServerPASK getVServerPASKInfo(Integer adcIndex, String vsIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("begin. adcIndex=%d, vsIndex=%s", adcIndex, vsIndex));

		OBDtoAdcVServerPASK obj;
		try {
			obj = new OBVServerDB().getVServerInfoPASK(vsIndex);
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

	public void setVServerEnableDisable(int adcIndex, ArrayList<String> vsIndexList, int state,
			OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBVServerDB vsDB = new OBVServerDB();
		ArrayList<String> vsTodoList = new ArrayList<String>(); // 실제로 enable/disable 상태가 바뀐 virtual server들의 이름 리스트

		// 장비 기본 확인
		OBDtoAdcInfo adcInfo = null;
		try {

			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						"setVServerEnableDisable() error.: adcInfo error(null).");
			}

			// 변경기록 준비
			OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
			ArrayList<OBDtoAdcConfigChunkPASK> configChunkList = new ArrayList<OBDtoAdcConfigChunkPASK>();
			new OBAdcManagementImpl().waitUntilAdcAvailable(adcIndex);
			// 중요!! 여기부터 decreaseAdcCheckCfg() 호출 부분까지는 일어날 수 있는 모든 exception에 대해 명시적으로
			// 처리한다.exception에서도 decreaseAdcCheckCfg()를 호출해야 한다.

			try {
				OBDtoAdcVServerPASK tempVsOld, tempVsNew;
				for (String vsIndex : vsIndexList) // virtual server old 상태를 잡아 놓는다.
				{
					tempVsOld = vsDB.getVServerInfoPASK(vsIndex);
					tempVsNew = OBAdcVServerPASK.cloneVServer(tempVsOld);
					tempVsNew.setState(state); // 이력 비교를 위해 새 vs가 갖게 될 state를 설정한다. 웹에서는 vsIndex리스트만 주지 newInfo를 주지 않기
												// 때문에 DB에서 읽으면 기대 값을 설정해 줘야 한다.

					OBDtoAdcConfigChunkPASK tempChunk = new OBDtoAdcConfigChunkPASK();
					tempChunk = historyManager.MakeConfigChunkPASK(tempVsOld, tempVsNew, OBDefine.CHANGE_BY_USER,
							OBDefine.CHANGE_TYPE_EDIT, OBDefine.CHANGE_OBJECT_VIRTUALSERVER,
							extraInfo.getAccountIndex());
					if (tempChunk.getVsConfig().getStateChange() == OBDefine.CHANGE_TYPE_EDIT) // 실제로 변경할 대상만 이력을 보관한다.
					{
						vsTodoList.add(tempVsOld.getName());
						configChunkList.add(tempChunk);
					}
				}

				// State 설정 //실제로 state가 바뀌는 것들만 작업한다. enable->enable은 안함
				OBAdcPASKHandler configHandler = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
				configHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
						adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());

				String slbDump = "";
				String realDump = "";
				String healthDump = "";
				String infoSlbDump = "";
				try {
					configHandler.login();

					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "login OK");
					configHandler.enableDisableVirtualServer(vsTodoList, state);
					// 설정이 끝났으면 slb 데이터를 받는다. DB업데이트 준비
					slbDump = configHandler.cmndSlbDump();
					realDump = configHandler.cmndRealDump();
					healthDump = configHandler.cmndHealthDump();
					infoSlbDump = configHandler.cmndShowInfoSlb();

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

				// from here: 이력 기록, DB갱신
				for (OBDtoAdcConfigChunkPASK configChunk : configChunkList) {
					historyManager.writeConfigHistoryPASK(configChunk);
				}
				// 설정 전부 갱신, F5는 vs정보만 업데이트 할 수 있는데, 얘는 안 된다. //TODO???
				// downloadSlbConfig(adcIndex, true);
				// db에 SLB 업데이트
				writeSlbConfig(adcInfo, slbDump, realDump, healthDump, infoSlbDump);

				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs en/dis] DONE");
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
			} catch (OBException e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
				throw e;
			} catch (Exception e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
				throw e;
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	// public static void main(String [] args)
	// {
	// OBDtoAdcVServerPASK vsInfo = new OBDtoAdcVServerPASK();
	// vsInfo.setAdcIndex(1);
	// vsInfo.setName("bwpark_test");
	// vsInfo.setSrvPort(8080);
	// vsInfo.setvIP("2.2.2.2");
	// vsInfo.setState(OBDefine.STATE_ENABLE);
	// vsInfo.getPool().setName("bwpark_test");
	// OBDtoAdcPoolMemberPASK memObj = new OBDtoAdcPoolMemberPASK();
	// memObj.setId(1);
	// memObj.setIpAddress("1.1.1.2");
	// vsInfo.getPool().getMemberList().add(memObj);
	// OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
	// extraInfo.setAccountIndex(0);
	// extraInfo.setClientIPAddress("172.172.2.206");
	// try
	// {
	// new OBAdcVServerPASK().addVServerPASK(vsInfo, extraInfo);
	// }
	// catch(OBExceptionUnreachable e)
	// {
	// //
	// e.printStackTrace();
	// }
	// catch(OBExceptionLogin e)
	// {
	// //
	// e.printStackTrace();
	// }
	// catch(OBLicenseExpiredException e)
	// {
	// //
	// e.printStackTrace();
	// }
	// catch(OBException e)
	// {
	// //
	// e.printStackTrace();
	// }
	// }
	//
	@Override
	public void addVServerPASK(OBDtoAdcVServerPASK virtualServer, OBDtoExtraInfo extraInfo)
			throws OBLicenseExpiredException, OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. virtualServer :%s, extraInfo:%s", virtualServer, extraInfo));

//        OBVServerDB vDB = new OBVServerDB();
		try {
			new OBAdcManagementImpl().waitUntilAdcAvailable(virtualServer.getAdcIndex());

			try {
				// OBDtoAdcInfo adcInfo = new
				// OBAdcManagementImpl().getAdcInfo(virtualServer.getAdcIndex(), db);
				OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(virtualServer.getAdcIndex());
				addVirtualServer(adcInfo, virtualServer, extraInfo);

				// 감사로그
				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), virtualServer.getAdcIndex(),
						extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_ADD_SUCCESS, adcInfo.getName(),
						virtualServer.getName(), null);
				new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServer.getAdcIndex());
			} catch (OBExceptionUnreachable e) {
				throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
			} catch (OBExceptionLogin e) {
				throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
			} catch (OBException e) {
				throw e;
			} catch (Exception e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServer.getAdcIndex());
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	public void addVirtualServer(OBDtoAdcInfo adcInfo, OBDtoAdcVServerPASK virtualServer, OBDtoExtraInfo extraInfo)
			throws OBLicenseExpiredException, OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
		OBDtoAdcConfigChunkPASK configChunk = null;
		int change = OBDefine.CHANGE_TYPE_NONE;
		try {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] prepare history begin");
			// 변경이력 준비
			configChunk = historyManager.MakeConfigChunkPASK(null, virtualServer, OBDefine.CHANGE_BY_USER,
					OBDefine.CHANGE_TYPE_ADD, OBDefine.CHANGE_OBJECT_VIRTUALSERVER, extraInfo.getAccountIndex());
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] prepare history done");
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] telnet login");
		// 텔넷 연결.
		OBAdcPASKHandler configHandler = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
		configHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
				adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
		try {
			configHandler.login();
		} catch (OBExceptionUnreachable e) {
			throw new OBExceptionUnreachable(e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBExceptionLogin(e.getMessage());
		}

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "login OK");
		String slbDump = "";
		String realDump = "";
		String healthDump = "";
		String infoSlbDump = "";

		try {
			change = configHandler.configSlb(configChunk);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] done WELL");
		} catch (OBException e) {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] partially");
			throw e;
		} finally {
			// 설정이 끝났으면 slb 데이터를 받는다. DB업데이트 준비
			slbDump = configHandler.cmndSlbDump();
			realDump = configHandler.cmndRealDump();
			healthDump = configHandler.cmndHealthDump();
			infoSlbDump = configHandler.cmndShowInfoSlb();
			configHandler.disconnect();
		}
		if (change != OBDefine.CHANGE_TYPE_NONE) {
			try {
				// 변경이력 기록
				historyManager.writeConfigHistoryPASK(configChunk);
				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), virtualServer.getAdcIndex(),
						extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_ADD_SUCCESS, adcInfo.getName(),
						virtualServer.getName(), null);
			} catch (OBException e) {
				throw e;
			} catch (Exception e) {
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			}

			try {
				writeSlbConfig(adcInfo, slbDump, realDump, healthDump, infoSlbDump); // db에 SLB 업데이트
			} catch (OBException e1) {
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
				throw e1;
			} catch (Exception e1) {
				new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
						OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
				throw new OBException(e1.getMessage());
			}
		} else {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs add] nothing to do.");
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "end.");
	}

	private void writeSlbConfig(OBDtoAdcInfo adcInfo, String slbDump, String realDump, String healthDump,
			String slbStatDump) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcInfo.getIndex()));

		long tt0 = 0, tt1 = 0, tt2 = 0, tt3 = 0, tt4 = 0;
		tt0 = System.currentTimeMillis();
		OBDatabase db = new OBDatabase();
		try {
			StringBuilder transactionQuery = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);
			transactionQuery.append(" BEGIN; ");

			// cfg dump 파일에서 slb 정보를 파싱.
			OBDtoAdcConfigSlbPASK slbCfg = new OBCLIParserPASK().getSlbInfo(adcInfo.getIndex(), slbDump, realDump,
					healthDump);
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
			vDB.addHealthCheckPASK(adcInfo.getIndex(), slbCfg.getHealthList(), transactionQuery);
			vDB.addPoolVServerPASK(adcInfo.getIndex(), slbCfg.getVsList(), slbCfg.getRealList(), transactionQuery);// vs,
																													// pool,
																													// poolmember
																													// 정보를
																													// 저장한다.

			transactionQuery.append(" COMMIT; ");

			db.openDB();
			db.executeUpdate(transactionQuery.toString());

			// status update
			ArrayList<OBDtoAdcVServerPASK> list = new OBCLIParserPASK().parseSlbStatus(adcInfo.getIndex(), slbStatDump);
			new OBAdcMonitorDB().writeSlbStatusPASK(adcInfo.getIndex(), list);

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

	// private void writeSlbConfig(OBDtoAdcInfo adcInfo, String slbDump, String
	// realDump, String healthDump, String slbStatDump) throws OBException
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
	// writeSlbConfig(adcInfo, slbDump, realDump, healthDump, slbStatDump, db);
	// }
	// catch(OBException e)
	// {
	// db.closeDB();
	// throw e;
	// }
	//
	// db.closeDB();
	// }

	@Override
	public void setVServerPASK(OBDtoAdcVServerPASK virtualServer, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "virtual server edit: " + virtualServer);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "virtual server edit: " + virtualServer);
		try {
			// 장비 확인
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(virtualServer.getAdcIndex());
			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Fail to get adcInfo.(adcInfo is null)");
			}

			new OBAdcManagementImpl().waitUntilAdcAvailable(virtualServer.getAdcIndex());
			// 중요!! 여기부터 decreaseAdcCheckCfg()를 호출 부분까지는 일어날 수 있는 모든 exception에 대해 명시적으로
			// 처리한다.거기서 decreaseAdcCheckCfg()를 호출해야 한다.
			try {
				// 기본적인 값 유효성 확인
				// validateBaseValues(virtualServer);
				OBVServerDB vDB = new OBVServerDB();
				// old Virtual 서버 확인
				OBDtoAdcVServerPASK virtualServerOld = vDB.getVServerInfoPASK(virtualServer.getDbIndex());
				if (virtualServerOld == null) {
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "VirtualServer doesn't exist.");
				}

				// newVS의 subInfo 구성. 미리 알 수 없는데, 수작업으로 구성한다
				virtualServer.setSubInfo(String.format("vip %s protocol %s vport %d", virtualServer.getvIP(),
						OBCommon.convertProtocolInteger2String(virtualServer.getSrvProtocol()),
						virtualServer.getSrvPort()));
				if (virtualServer.getPool().getHealthCheckInfo() != null) // healthcheck가 없을 수도 있다.
				{
					virtualServer.setSubInfo(String.format("%s\nhealth-check %d", virtualServer.getSubInfo(),
							virtualServer.getPool().getHealthCheckInfo().getId()));
				}
				// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "old subinfo = " +
				// virtualServerOld.getSubInfo());
				// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "new subinfo = " +
				// virtualServer.getSubInfo());

				// 변경기록 준비
				OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
				OBDtoAdcConfigChunkPASK configChunk = null;
				configChunk = historyManager.MakeConfigChunkPASK(virtualServerOld, virtualServer,
						OBDefine.CHANGE_BY_USER, OBDefine.CHANGE_TYPE_EDIT, OBDefine.CHANGE_OBJECT_VIRTUALSERVER,
						extraInfo.getAccountIndex());
				int change = OBDefine.CHANGE_TYPE_NONE;

				// 텔넷 연결.
				// OBAdcPAS configHandler = new OBAdcPAS(adcInfo.getAdcIpAddress(),
				// adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt());
				OBAdcPASKHandler configHandler = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
				configHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
						adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
				configHandler.login();
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "login OK");
				String slbDump = "";
				String realDump = "";
				String healthDump = "";
				String infoSlbDump = "";

				try {
					change = configHandler.configSlb(configChunk);
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs set] logout WELL");
					// 설정이 끝났으면 slb 데이터를 받는다. DB업데이트 준비
					slbDump = configHandler.cmndSlbDump();
					realDump = configHandler.cmndRealDump();
					healthDump = configHandler.cmndHealthDump();
					infoSlbDump = configHandler.cmndShowInfoSlb();

					configHandler.disconnect();
				} catch (OBException e) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs set] logout BAD");
					configHandler.disconnect();
					throw e;
				}

				if (change != OBDefine.CHANGE_TYPE_NONE) { // 변경기록
					historyManager.writeConfigHistoryPASK(configChunk);
					new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), virtualServer.getAdcIndex(),
							extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_SET_SUCCESS,
							adcInfo.getName(), virtualServer.getName(), null);

					try {
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs set] update CONFIG");
						writeSlbConfig(adcInfo, slbDump, realDump, healthDump, infoSlbDump); // db에 SLB 업데이트
					} catch (OBException e1) {
						new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
								OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
						throw e1;
					} catch (Exception e1) {
						new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
								OBSystemAudit.AUDIT_GET_SLBCONFIG_FAIL, adcInfo.getName());
						throw new OBException(e1.getMessage());
					}
				} else {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "[vs edit] nothing to do.");
				}
				// 여기까지 decreaseAdcCheckCfg()가 포함된 exception을 처리한다.
				new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServer.getAdcIndex());
			} catch (OBExceptionUnreachable e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServer.getAdcIndex());
				throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
			} catch (OBExceptionLogin e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServer.getAdcIndex());
				throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
			} catch (OBException e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServer.getAdcIndex());
				throw e;
			} catch (Exception e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(virtualServer.getAdcIndex());
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
	}

	@Override
	public void delVServer(Integer adcIndex, ArrayList<String> vsIndexList, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		// 장비 기본 확인
		OBDtoAdcInfo adcInfo;
		OBAdcPASKHandler configHandler = null;
		try {
			// adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex, db);
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			if (adcInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						"Can't find target ADC info in database.");
			}
			// 변경기록 준비
			OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
			OBVServerDB vsDB = new OBVServerDB();
			new OBAdcManagementImpl().waitUntilAdcAvailable(adcIndex);
			// 중요!! 여기부터 decreaseAdcCheckCfg()를 호출 부분까지는 일어날 수 있는 모든 exception에 대해 명시적으로
			// 처리한다.거기서 decreaseAdcCheckCfg()를 호출해야 한다
			ArrayList<OBDtoAdcConfigChunkPASK> configChunkList = new ArrayList<OBDtoAdcConfigChunkPASK>();

			try {
				for (String vsIndex : vsIndexList) // virtual server old 상태를 잡아 놓는다.
				{
					configChunkList.add(historyManager.MakeConfigChunkPASK(vsDB.getVServerInfoPASK(vsIndex), null,
							OBDefine.CHANGE_BY_USER, OBDefine.CHANGE_TYPE_DELETE, OBDefine.CHANGE_OBJECT_VIRTUALSERVER,
							extraInfo.getAccountIndex()));
				} // 변경기록 준비 끝

				ArrayList<String> vsNameList = new OBVServerDB().getVirtualServerNameList(vsIndexList);
				configHandler = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
				if (configHandler == null)
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							"invalid data. configHandler is null");

				configHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
						adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
				configHandler.login();
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "login OK");

				String slbDump = "";
				String realDump = "";
				String healthDump = "";
				String infoSlbDump = "";

				configHandler.delVirtualServer(vsNameList);

				// 설정이 끝났으면 slb 데이터를 받는다. DB업데이트 준비
				slbDump = configHandler.cmndSlbDump();
				realDump = configHandler.cmndRealDump();
				healthDump = configHandler.cmndHealthDump();
				infoSlbDump = configHandler.cmndShowInfoSlb();

				for (String name : vsNameList) {
					new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcIndex,
							extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_VSERVER_DEL_SUCCESS,
							adcInfo.getName(), name, null);
				}
				// 변경기록
				for (OBDtoAdcConfigChunkPASK configChunk : configChunkList) {
					historyManager.writeConfigHistoryPASK(configChunk);
				}

				writeSlbConfig(adcInfo, slbDump, realDump, healthDump, infoSlbDump); // db에 SLB 업데이트
				configHandler.disconnect();
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
			} catch (OBExceptionUnreachable e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
				if (configHandler != null)
					configHandler.disconnect();
				throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
			} catch (OBExceptionLogin e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
				if (configHandler != null)
					configHandler.disconnect();
				throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
			} catch (OBException e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
				if (configHandler != null)
					configHandler.disconnect();
				throw e;
			} catch (Exception e) {
				new OBAdcManagementImpl().decreaseAdcCheckCfg(adcIndex);
				if (configHandler != null)
					configHandler.disconnect();
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public ArrayList<OBDtoAdcPoolPASK> getPoolPASKList(Integer adcIndex) throws OBException {
		try {
			return new OBVServerDB().getPoolListAllPASK(adcIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public OBDtoAdcPoolPASK getPoolPASK(String poolIndex) throws OBException {
		try {
			return new OBVServerDB().getPoolInfoPASK(poolIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public ArrayList<OBDtoAdcNodePASK> getNodeAvailableListPASK(Integer adcIndex, String poolIndex) throws OBException {
		try {
			return new OBVServerDB().getNodeAvailableListPASK(adcIndex, poolIndex); // TODO
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// @Override
	// public ArrayList<OBDtoAdcVServerPASK> searchVServerListPASK(Integer adcIndex,
	// String searchKey, Integer beginIndex, Integer endIndex) throws OBException
	// {
	// return new OBVServerDB().searchVServerListPASK(adcIndex, searchKey,
	// beginIndex, endIndex);
	// }

	@Override
	public Integer searchVServerListPASKCount(Integer adcIndex, String searchKey) throws OBException {
		return new OBVServerDB().searchVServerListPASKCount(adcIndex, searchKey);
	}

	@Override
	public OBDtoAdcHealthCheckPASK getHealthCheckPASK(String healthcheckDbIndex) throws OBException {
		try {
			return new OBVServerDB().getHealthCheckInfoPASK(healthcheckDbIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public void revertSlbConfig(Integer adcIndex, String revertConfig, String currentConfig, String newVServiceList,
			String newPoolList, String newNodeList) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		// TODO Auto-generated method stub
	}

	public void revertSlbConfig(Integer adcIndex, OBDtoAdcConfigChunkPASK configChunk, OBDtoExtraInfo extraInfo)
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
				addVServerPASK(configChunk.getVsConfig().getVsOld(), extraInfo); // DELETE는 OLD에서 빼서 살린다.
			} else if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_EDIT) {
				if (configChunk.getVsConfig().getStateChange() == OBDefine.CHANGE_TYPE_EDIT) // vs enable/disable은 edit
																								// 중에도 단독으로 하기 때문에 갈라서
																								// 처리해야 한다.
				{
					vsIndexList.add(configChunk.getVsConfig().getVsNew().getDbIndex()); // vsIndex 파라미터를 맞춘다.
					// enable/disable은 old의 state로 설정한다. 즉 되돌린다.
					setVServerEnableDisable(adcIndex, vsIndexList, configChunk.getVsConfig().getVsOld().getState(),
							extraInfo);
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Revert - vs enable/disable");
				} else
				// enable/disable 외 virtual server 수정
				{
					setVServerPASK(configChunk.getVsConfig().getVsOld(), extraInfo);
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Revert - vs edit");
				}
			} else {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "Error: Nothing to revert.");
			}
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "Revert done well");
		}
	}

	@Override
	public boolean isAvailableVirtualServerPASK(Integer adcIndex, String vsName, String ipAddress, Integer port)
			throws OBException {
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
	public boolean isExistVirtualServerPASK(Integer adcIndex, String vsName) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "start");
		OBDatabase db = new OBDatabase();
		boolean retVal = true;
		String sqlText = "";
		try {
			db.openDB();
			sqlText = String.format(" SELECT INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX=%d AND NAME=%s ", adcIndex,
					OBParser.sqlString(vsName));
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText = " + sqlText);
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

	public static OBDtoAdcVServerPASK cloneVServer(OBDtoAdcVServerPASK original) {
		OBDtoAdcVServerPASK clone = null;
		if (original != null) {
			clone = new OBDtoAdcVServerPASK(original);
		}

		return clone;
	}

	public static OBDtoAdcPoolPASK clonePool(OBDtoAdcPoolPASK original) {
		OBDtoAdcPoolPASK clone = new OBDtoAdcPoolPASK(original);

		return clone;
	}

	public Timestamp getDeviceApplyTime(int adcIndex) throws Exception {
		String sqlText = "";
		ResultSet rs;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT APPLY_TIME FROM TMP_SLB_VSERVER WHERE ADC_INDEX=%d ORDER BY APPLY_TIME DESC LIMIT 1; ",
					adcIndex);

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

	// ///////////////////////////////////////////////////////////////////////////////////////////
	// FROM HERE, NOT SUPPORTED FUNCTIONS FOR PASK
	@Override
	public boolean checkVrrp(String ipaddress, String accountID, String password, String swVersion, String vsIP,
			Integer routerIndex, Integer vrIndex, Integer ifNum)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerAlteon> getVServerListAlteon(Integer adcIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	// @Override
	// public ArrayList<OBDtoAdcVServerAlteon> getVServerListAlteon(Integer
	// adcIndex, Integer beginIndex, Integer endIndex)
	// throws OBExceptionUnreachable, OBExceptionLogin, OBException
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported
	// function");
	// }
	@Override
	public ArrayList<OBDtoAdcVServerF5> getVServerListF5(Integer adcIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	// @Override
	// public ArrayList<OBDtoAdcVServerF5> getVServerListF5(Integer adcIndex,
	// Integer beginIndex, Integer endIndex)
	// throws OBExceptionUnreachable, OBExceptionLogin, OBException
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported
	// function");
	// }
	@Override
	public ArrayList<OBDtoAdcVServerPAS> getVServerListPAS(Integer adcIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	// @Override
	// public ArrayList<OBDtoAdcVServerPAS> getVServerListPAS(Integer adcIndex,
	// Integer beginIndex, Integer endIndex)
	// throws OBExceptionUnreachable, OBExceptionLogin, OBException
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported
	// function");
	// }
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
	public void relashVServerF5(OBDtoAdcVServerF5 virtualServerNew, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public OBDtoAdcVServerPAS getVServerPASInfo(Integer adcIndex, String vsIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
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
	public void addVServerPAS(OBDtoAdcVServerPAS virtualServer, OBDtoExtraInfo extraInfo)
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
	public void setVServerPAS(OBDtoAdcVServerPAS virtualServer, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
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
	public ArrayList<OBDtoAdcPoolPAS> getPoolPASList(Integer adcIndex) throws OBException {
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
	public OBDtoAdcPoolPAS getPoolPAS(String poolIndex) throws OBException {
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
	public ArrayList<OBDtoAdcNodePAS> getNodeAvailableListPAS(Integer adcIndex, String poolIndex) throws OBException {
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

	// @Override
	// public ArrayList<OBDtoAdcVServerAlteon> searchVServerListAlteon(
	// Integer adcIndex, String searchKey, Integer beginIndex,
	// Integer endIndex) throws OBException
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported
	// function");
	// }
	@Override
	public Integer searchVServerListAlteonCount(Integer adcIndex, String searchKey) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	// @Override
	// public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex,
	// String searchKey, Integer beginIndex, Integer endIndex)
	// throws OBException
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported
	// function");
	// }
	@Override
	public Integer searchVServerListF5Count(Integer adcIndex, String searchKey) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	// @Override
	// public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(
	// Integer adcIndex, String searchKey, Integer beginIndex,
	// Integer endIndex) throws OBException
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported
	// function");
	// }
	@Override
	public Integer searchVServerListPASCount(Integer adcIndex, String searchKey) throws OBException {
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
	public boolean isExistVirtualServerPAS(Integer adcIndex, String vsName) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	// @Override
	// public ArrayList<OBDtoAdcProfile> getProfileList(Integer adcIndex)
	// throws OBException
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported
	// function");
	// }
	@Override
	public void syncConifgF5(Integer adcIndex, OBDtoExtraInfo extraInfo) throws OBException {
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
		// PASK의 경우에는 아무런 작업을 하지 않는다.
		return vsIndex;
	}

	@Override
	public String getValidPoolIndex(Integer adcIndex, String poolIndex) throws OBException {
		// PASK의 경우에는 아무런 작업을 하지 않는다.
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPASK> searchVSListUsedByPoolPASK(Integer adcIndex, String poolName)
			throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcHealthCheckPASK> getHealthCheckListPASK(Integer adcIndex) throws OBException {
		try {
			// 로컬DB에서 읽어 처리한다.
			return new OBVServerDB().getHealthCheckListPASK(adcIndex);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// @Override
	// public ArrayList<OBDtoAdcVServerAlteon> searchVServerListAlteon(Integer
	// adcIndex, Integer accntIndex, String searchKey, Integer beginIndex, Integer
	// endIndex) throws OBException
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported
	// function");
	// }
	// @Override
	// public ArrayList<OBDtoAdcVServerF5> searchVServerListF5(Integer adcIndex,
	// Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex)
	// throws OBException
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported
	// function");
	// }
	// @Override
	// public ArrayList<OBDtoAdcVServerPAS> searchVServerListPAS(Integer adcIndex,
	// Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex)
	// throws OBException
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported
	// function");
	// }
	// @Override
	// public ArrayList<OBDtoAdcVServerPASK> searchVServerListPASK(Integer adcIndex,
	// Integer accntIndex, String searchKey, Integer beginIndex, Integer endIndex)
	// throws OBException
	// {
	// return new OBVServerDB().searchVServerListPASK(adcIndex, accntIndex,
	// searchKey, beginIndex, endIndex, OBDefine.ORDER_TYPE_VSNAME,
	// OBDefine.ORDER_DIR_ASCEND);
	// }
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public Integer searchVServerListPASKCount(Integer adcIndex, Integer accntIndex, String searchKey)
			throws OBException {
		return new OBVServerDB().searchVServerListPASKCount(adcIndex, accntIndex, searchKey);
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPASK> getVServerListPASK(Integer adcIndex, Integer beginIndex, Integer endIndex,
			Integer orderType, Integer orderDir) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("begin"));

		try {
			ArrayList<OBDtoAdcVServerPASK> list = new OBVServerDB().getVServerListAllPASK(adcIndex, beginIndex,
					endIndex, orderType, orderDir);

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
			return list;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPASK> searchVServerListPASK(Integer adcIndex, String searchKey, Integer beginIndex,
			Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		return new OBVServerDB().searchVServerListPASK(adcIndex, searchKey, beginIndex, endIndex, orderType, orderDir);
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
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public ArrayList<OBDtoAdcVServerPASK> searchVServerListPASK(Integer adcIndex, Integer accntIndex, String searchKey,
			Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException {
		return new OBVServerDB().searchVServerListPASK(adcIndex, searchKey, beginIndex, endIndex, orderType, orderDir);
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
		// PASK는 본 기능을 제공하지 않는다. 무조건 true한다.
		return true;
	}

	@Override
	public boolean isPeerVrrpValid(Integer adcIndex, Integer peerIndex) throws OBException {
		// PASK는 본 기능을 제공하지 않는다. 무조건 true한다.
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
	public Integer searchVServerListAllListCount(OBDtoAdcScope scope, Integer accntIndex, String searchKey)
			throws OBException {
		return new OBVServerDB().searchVServerAllListCoreCount(scope, accntIndex, searchKey);
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