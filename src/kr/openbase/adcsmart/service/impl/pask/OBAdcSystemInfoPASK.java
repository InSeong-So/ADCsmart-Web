package kr.openbase.adcsmart.service.impl.pask;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBAdcSystemInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcSystemInfo;
import kr.openbase.adcsmart.service.impl.pask.handler.OBAdcPASKHandler;
import kr.openbase.adcsmart.service.impl.pask.handler.OBCLIParserPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoSystemInfoPASK;
import kr.openbase.adcsmart.service.snmp.pask.OBSnmpPASK;
import kr.openbase.adcsmart.service.utility.OBCipherAES;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBAdcSystemInfoPASK implements OBAdcSystemInfo {
//	public static void main(String[] args)
//	{
//		try
//		{
//			String name = new OBAdcSystemInfoPAS().getAdcHostName("192.168.200.120", "", "default");
//			System.out.println(name);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public String getAdcHostName(String ipAddress, String swVersion, OBDtoAdcSnmpInfo snmpInfo) throws OBException {
		try {
			String name = new OBSnmpPASK(ipAddress, snmpInfo).getAdcHostname(OBDefine.ADC_TYPE_PIOLINK_PASK, "");
			return name;
		} catch (OBException e) {
			throw e;
		}
	}

	private OBDtoAdcSystemInfo getAdcSystemInfoCLI(OBDtoAdcInfo adcInfo, int mode) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(index:%s)", adcInfo));
		OBDtoAdcSystemInfo info = new OBDtoAdcSystemInfo();
		OBCLIParserPASK parserClass = new OBCLIParserPASK();
		OBAdcPASKHandler adcHandler = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());

		try {
			adcHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
			adcHandler.login();

			// hostname 추출.
			String hostnameDump = adcHandler.cmndShowHostname();
			// system 정보 추출.
			String systemDump = adcHandler.cmndSystem();

			// apply time 추출.
			String slbDump = adcHandler.cmndSlbDump();

			String hostName = parserClass.parseHostname(hostnameDump);
			OBDtoSystemInfoPASK systemCfg = parserClass.parseSystem(systemDump);
			Timestamp applyTime = parserClass.parseApplyTime(slbDump);

			info.setAdcIndex(adcInfo.getIndex());
			info.setStatus(OBDefine.ADC_STATUS.REACHABLE);
			info.setHostName(hostName);

			if (systemCfg != null) {
				info.setModel(systemCfg.getProductName());
				info.setSwVersion(systemCfg.getVersion());
				info.setLastApplyTime(applyTime);
			}
			adcHandler.disconnect();
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", info));
			return info;
		} catch (OBException e) {
			adcHandler.disconnect();
			throw e;
		} catch (Exception e) {
			adcHandler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private OBDtoAdcSystemInfo getAdcSystemInfoCLI(Integer adcIndex, int mode, OBAdcPASKHandler adcHandler)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(index:%d)", adcIndex));
		OBDtoAdcSystemInfo info = new OBDtoAdcSystemInfo();
		OBCLIParserPASK parserClass = new OBCLIParserPASK();

		try {
			// hostname 추출.
			String hostnameDump = adcHandler.cmndShowHostname();
			// system 정보 추출.
			String systemDump = adcHandler.cmndSystem();
			// apply time 추출.
			String slbDump = adcHandler.cmndSlbDump();

			String hostName = parserClass.parseHostname(hostnameDump);
			OBDtoSystemInfoPASK systemCfg = parserClass.parseSystem(systemDump);
			Timestamp applyTime = parserClass.parseApplyTime(slbDump);

			info.setAdcIndex(adcIndex);
			info.setStatus(OBDefine.ADC_STATUS.REACHABLE);
			info.setHostName(hostName);

			if (systemCfg != null) {
				info.setModel(systemCfg.getProductName());
				info.setSwVersion(systemCfg.getVersion());
				info.setLastApplyTime(applyTime);
				if (uptimeNoEmpty(systemCfg)) {
					Timestamp upTime = transperUptime(systemCfg);
					info.setLastBootTime(upTime);
				}

			}
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", info));
			return info;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private boolean uptimeNoEmpty(OBDtoSystemInfoPASK systemCfg) {
		return systemCfg.getUpTime() != null && !systemCfg.getUpTime().isEmpty();
	}

	private Timestamp transperUptime(OBDtoSystemInfoPASK systemCfg) {
		Timestamp upTime = null;
		String[] time = systemCfg.getUpTime().split(" ");
		if (time.length > 6) {
			LocalDateTime date = LocalDateTime.now();
			date = date.minusDays(Long.parseLong(time[0])).minusHours(Long.parseLong(time[2]))
					.minusMinutes(Long.parseLong(time[4])).minusSeconds(Long.parseLong(time[6]));
			upTime = Timestamp.valueOf(date);
		}

		return upTime;
	}

	public OBDtoAdcSystemInfo getAdcSystemInfo(Integer adcIndex, String ipaddress, String account, String password,
			String swVersion, String community, int mode) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBDtoAdcSystemInfo info;

		try {
			OBDtoAdcInfo adcInfo = new OBDtoAdcInfo();
			adcInfo.setIndex(adcIndex);
			adcInfo.setAdcIpAddress(ipaddress);
			adcInfo.setAdcAccount(account);
			adcInfo.setAdcPassword(new OBCipherAES().Encrypt(password));
			adcInfo.setSwVersion(swVersion);
			adcInfo.setSnmpRComm(community);

			info = getAdcSystemInfoCLI(adcInfo, mode);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return info;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDtoAdcSystemInfo name = new OBAdcSystemInfoPASK().getAdcSystemInfo(5, "192.168.200.110", "root", "admin", "", "default");
//			System.out.println(name);
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
	public OBDtoAdcSystemInfo getAdcSystemInfo(Integer adcIndex, String ipaddress, String account, String password,
			int connService, int connPort, String swVersion, OBDtoAdcSnmpInfo snmpInfo, int opMode)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBDtoAdcSystemInfo info;

		try {
			OBDtoAdcInfo adcInfo = new OBDtoAdcInfo();
			adcInfo.setIndex(adcIndex);
			adcInfo.setAdcIpAddress(ipaddress);
			adcInfo.setAdcAccount(account);
			adcInfo.setAdcPassword(new OBCipherAES().Encrypt(password));
			adcInfo.setSwVersion(swVersion);
			adcInfo.setConnService(connService);
			adcInfo.setConnPort(connPort);
			adcInfo.setSnmpInfo(snmpInfo);
			info = getAdcSystemInfoCLI(adcInfo, 0);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return info;
	}

	public OBDtoAdcSystemInfo getAdcSystemInfo(Integer adcIndex, OBAdcPASKHandler adcHandler) throws OBException {
		OBDtoAdcSystemInfo info;

		try {
			info = getAdcSystemInfoCLI(adcIndex, 0, adcHandler);
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			getAdcSystemInfoSnmp(adcInfo, 0);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return info;
	}

	private OBDtoAdcSystemInfo getAdcSystemInfoSnmp(OBDtoAdcInfo adcInfo, int mode) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(index:%s)", adcInfo));
		OBDtoAdcSystemInfo info = new OBDtoAdcSystemInfo();

		try {
			info.setAdcIndex(adcInfo.getIndex());
			info.setStatus(OBDefine.ADC_STATUS.REACHABLE);

			new OBSnmpPASK(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo()).getAdcResc(OBDefine.ADC_TYPE_PIOLINK_PASK,
					adcInfo.getSwVersion());

		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", info));
		return info;
	}

	@Override
	public boolean isAvailableSystem(String ipaddress, String account, String password, int connService, int connPort,
			String cliAccount, String cliPassword) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		try {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start"));
			OBAdcPASKHandler pas = OBCommon.getValidPASKHandler("");
			pas.setConnectionInfo(ipaddress, account, password, connService, connPort);
			pas.login();
			pas.disconnect();
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:true"));

			return true;
		} catch (OBExceptionUnreachable e) {
			throw e;
		} catch (OBExceptionLogin e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}
	}

	public void saveAdcSystem(int adcIndex, String ipaddress, String account, String password, String swVersion,
			int connService, int connPort) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start"));
		OBAdcPASKHandler pasHandler = OBCommon.getValidPASKHandler(swVersion);
		try {
			pasHandler.setConnectionInfo(ipaddress, account, password, connService, connPort);
			pasHandler.login();
		} catch (OBExceptionUnreachable e) {
			throw new OBExceptionUnreachable(e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBExceptionLogin(e.getMessage());
		}
		try {
			pasHandler.writeMemory();
		} catch (Exception e) {
			pasHandler.disconnect();
			throw new OBException(e.getMessage());
		}
		pasHandler.disconnect();
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	public String getCfgDump(int adcIndex, String ipaddress, String account, String password, String swVersion,
			int connService, int connPort) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start"));
		OBAdcPASKHandler pasHandler = OBCommon.getValidPASKHandler(swVersion);
		try {
			pasHandler.setConnectionInfo(ipaddress, account, password, connService, connPort);
			pasHandler.login();
		} catch (OBExceptionUnreachable e) {
			throw new OBExceptionUnreachable(e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBExceptionLogin(e.getMessage());
		}
		String result = "";
		try {
			result = pasHandler.cmndCfgDump();
		} catch (Exception e) {
			pasHandler.disconnect();
			throw new OBException(e.getMessage());
		}
		pasHandler.disconnect();
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
		return result;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			String name = new OBAdcSystemInfoPASK().getAdcSWVersionCli("192.168.200.110", "root", "admin", "", OBDefine.SERVICE.TELNET, 23);
//			System.out.println(name);
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public String getAdcSWVersionCli(String adcIPAddress, String adcCLIAccount, String adcCLIPW, String swVersion,
			int connService, int connPort) throws OBException {
		// PASK 장비의 경우에는 CLI를 통해 sw version을 추출한다. snmp는 제공하지 않음.
		String retVal = "";
		OBCLIParserPASK parserClass = new OBCLIParserPASK();
		OBAdcPASKHandler adcHandler = null;
		try {
			adcHandler = OBCommon.getValidPASKHandler(swVersion);
			adcHandler.setConnectionInfo(adcIPAddress, adcCLIAccount, adcCLIPW, connService, connPort);
			adcHandler.login();

			// system 정보 추출.
			String systemDump = adcHandler.cmndSystem();

			OBDtoSystemInfoPASK systemCfg = parserClass.parseSystem(systemDump);

			if (systemCfg != null) {
				retVal = systemCfg.getVersion();
			}
			adcHandler.disconnect();
			return retVal;
		} catch (OBException e) {
			if (adcHandler != null)
				adcHandler.disconnect();
			throw e;
		} catch (Exception e) {
			if (adcHandler != null)
				adcHandler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public String getAdcSWVersionSnmp(String adcIPAddress, OBDtoAdcSnmpInfo snmpInfo) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}
}
