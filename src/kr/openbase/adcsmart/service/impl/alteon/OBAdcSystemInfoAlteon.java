package kr.openbase.adcsmart.service.impl.alteon;

import java.sql.Timestamp;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBAdcSystemInfo;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBAdcAlteonHandler;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoAdcTimeAlteon;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcSystemInfo;
import kr.openbase.adcsmart.service.snmp.alteon.OBSnmpAlteon;
import kr.openbase.adcsmart.service.snmp.dto.DtoSnmpAdcResc;
import kr.openbase.adcsmart.service.utility.OBCipherAES;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBLocalConfig;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBAdcSystemInfoAlteon implements OBAdcSystemInfo {
//	public static void main(String[] args)
//	{
//		try
//		{
////			new OBAdcSystemInfoAlteon().isAvailableSystem("192.168.100.11", "alteon", "alteon", OBDefine.ADC_TYPE_ALTEON, "");
//			OBDtoAdcSystemInfo info = new OBAdcSystemInfoAlteon().getAdcSystemInfo(1, "192.168.100.11", "admin", "admin", "");
//			System.out.println(info);
//		}
//		catch(Exception e)
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
			adcInfo.setConnService(connService);
			adcInfo.setConnPort(connPort);
			adcInfo.setSwVersion(swVersion);
			adcInfo.setSnmpInfo(snmpInfo);
			adcInfo.setOpMode(opMode);

			info = getAdcSystemInfoSnmpV2(adcInfo);

			// TODO 시간 설정 부분 추가 필요.
			OBDtoAdcTimeAlteon time = null;
			time = getAdcSystemTime(adcInfo);
			if (time != null) {
				info.setLastApplyTime(time.getApplyTime());
				info.setLastBootTime(time.getBootTime());
				info.setLastSaveTime(time.getSaveTime());
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return info;
	}

//	public OBDtoAdcSystemInfo getAdcSystemInfoxx(OBDtoAdcInfo adcInfo, int mode) throws OBExceptionUnreachable, OBExceptionLogin, OBException
//	{
//		OBDtoAdcSystemInfo retVal=null;
//		
//		try
//		{
//			retVal = getAdcSystemInfoSnmp(adcInfo, mode);
//		}
//		catch(OBException e)
//		{
//			throw e;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//	
//		return retVal;
//	}

	private OBDtoAdcTimeAlteon getAdcSystemTime(OBDtoAdcInfo adcInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBDtoAdcTimeAlteon retVal = new OBDtoAdcTimeAlteon();

		try {
			if (adcInfo.getOpMode() != OBDefine.OP_MODE_MONITORING) {
				retVal = new OBAdcManagementImpl().getAdcTimeAlteon(adcInfo.getIndex());// DB에서 데이터 추출함.//TODO

				String config = OBLocalConfig.getData(OBLocalConfig.CONFIG_KEY_GET_TIME_METHOD_DB);
				if ((retVal != null && retVal.getSaveTime() == null) || config.equals("false")) {
					retVal = new OBAdcVServerAlteon().getSystemTime(adcInfo);// ADC 시스템 접속하여 데이터 추출함.
				}
			} else {// a. 모니터링 모드인 경우에는 현재 시간을 applyTime, saveTime는 null로 설정한다.
				Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
				retVal.setApplyTime(now);
				retVal.setSaveTime(null);
			}
			// boot Time를 추출. snmp를 이용한다.
			Timestamp bootTime = new OBSnmpAlteon(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
					.getAdcBootTime(OBDefine.ADC_TYPE_ALTEON, adcInfo.getSwVersion());
			retVal.setBootTime(bootTime);
		} catch (OBExceptionUnreachable e) {
			throw e;
		} catch (OBExceptionLogin e) {
			throw e;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return retVal;
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public OBDtoAdcSystemInfo getAdcSystemInfo(OBDtoAdcInfo adcInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBDtoAdcSystemInfo retVal = new OBDtoAdcSystemInfo();

		try {
			retVal = getAdcSystemInfoSnmpV2(adcInfo);

			// 시간 정보를 추출하여 설정한다.
			OBDtoAdcTimeAlteon time = null;
			try {
				time = getAdcSystemTime(adcInfo);
				if (time != null) {
					retVal.setLastApplyTime(time.getApplyTime());
					retVal.setLastBootTime(time.getBootTime());
					retVal.setLastSaveTime(time.getSaveTime());
				}
			} catch (OBExceptionUnreachable e) {
				retVal.setStatus(OBDefine.ADC_STATUS.UNREACHABLE);
			} catch (OBExceptionLogin e) {
				retVal.setStatus(OBDefine.ADC_STATUS.UNREACHABLE);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return retVal;
	}

//	private OBDtoAdcSystemInfo getAdcSystemInfoTelnet(Integer adcIndex, String ipaddress, String account, String password, String swVersion) throws OBExceptionUnreachable, OBExceptionLogin, OBException
//	{
//		OBDtoAdcSystemInfo info;
//		OBAdcAlteon alteon;
//		
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
//		alteon.setConnectionInfo(ipaddress, account, password);
//		try
//		{
//			alteon.login();
//		}
//		catch(OBExceptionUnreachable e)
//		{
//			throw new OBExceptionUnreachable(e.getMessage());
//		}
//		catch(OBExceptionLogin e)
//		{
//			throw new OBExceptionLogin(e.getMessage());
//		}
//		try
//		{
//			info = alteon.getGeneralInfo();
//		}
//		catch(Exception e)
//		{
//			alteon.disconnect();
//			throw new OBException(e.getMessage());
//		}
//		alteon.disconnect();
//
//		info.setAdcIndex(adcIndex);
//		info.setStatus(OBDefine.ADC_STATUS.REACHABLE);
//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("getAdcSystemInfo end"));
//		return info;
//	}

	@Override
	public String getAdcHostName(String ipAddress, String swVersion, OBDtoAdcSnmpInfo snmpInfo) throws OBException {
		String retVal = "";
		try {
			retVal = new OBSnmpAlteon(ipAddress, snmpInfo).getAdcHostname(OBDefine.ADC_TYPE_ALTEON, "");
		} catch (OBException e) {
			throw e;
		}
		return retVal;
	}

//	private OBDtoAdcSystemInfo getAdcSystemInfoSnmp(OBDtoAdcInfo adcInfo, int mode) throws OBException
//	{
//		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(index:%s)", adcInfo));
//		OBDtoAdcSystemInfo retVal = new OBDtoAdcSystemInfo();
//
//    	OBDatabase db = new OBDatabase();
//		
//		try
//		{
//			db.openDB();
//			retVal = getAdcSystemInfoSnmp(adcInfo, mode, db);
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
//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", retVal));
//		return retVal;
//	}

//	private OBDtoAdcSystemInfo getAdcSystemInfoSnmp(OBDtoAdcInfo adcInfo, int mode) throws OBException {
//		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(index:%s)", adcInfo));
//		OBDtoAdcSystemInfo info = new OBDtoAdcSystemInfo();
//
//		try {
//			info.setAdcIndex(adcInfo.getIndex());
////			OBAdcCheckResult retVal = new OBAdcCheckResult();//gstest
//
////			retVal = new OBCheckAdcStatus().checkADCStatusSyslog(adcInfo, true, db);
////			info.setStatus(retVal.getStatus());
//			OBDtoAdcTimeAlteon time;
//			try {
//				if (mode == 0)
//					time = new OBAdcVServerAlteon().getSystemTime(adcInfo);// ADC 시스템 접속하여 데이터 추출함.
//				else
//					time = new OBAdcManagementImpl().getAdcTimeAlteon(adcInfo.getIndex());// DB에서 데이터 추출함.
//				if (time != null) {
//					info.setLastApplyTime(time.getApplyTime());
//					info.setLastBootTime(time.getBootTime());
//					info.setLastSaveTime(time.getSaveTime());
//				}
//			} catch (OBExceptionUnreachable e) {
//				info.setStatus(OBDefine.ADC_STATUS.UNREACHABLE);
//			} catch (OBExceptionLogin e) {
//				info.setStatus(OBDefine.ADC_STATUS.UNREACHABLE);
//			}
//
//			DtoSnmpAdcResc snmpInfo;
//			snmpInfo = new OBSnmpAlteon(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
//					.getAdcResc(OBDefine.ADC_TYPE_ALTEON, adcInfo.getSwVersion());
//
//			if (snmpInfo != null) {
//				info.setHostName(snmpInfo.getName());
//				info.setModel(snmpInfo.getModel());
//				info.setSwVersion(snmpInfo.getSwVersion());
//			}
//			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", info));
//			return info;
//		} catch (OBException e) {
//			throw e;
//		} catch (Exception e) {
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//	}

	private OBDtoAdcSystemInfo getAdcSystemInfoSnmpV2(OBDtoAdcInfo adcInfo) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(index:%s)", adcInfo));
		OBDtoAdcSystemInfo info = new OBDtoAdcSystemInfo();

		try {
			info.setAdcIndex(adcInfo.getIndex());
			DtoSnmpAdcResc snmpInfo;
			snmpInfo = new OBSnmpAlteon(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
					.getAdcResc(OBDefine.ADC_TYPE_ALTEON, adcInfo.getSwVersion());

			if (snmpInfo != null) {
				info.setHostName(snmpInfo.getName());
				info.setModel(snmpInfo.getModel());
				info.setSwVersion(snmpInfo.getSwVersion());
			}
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", info));
			return info;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

//	@Override
//	public boolean isAvailableSystem(String ipaddress, String account,
//			String password, Integer vendor, String swVersion)
//	{
//		try
//		{
//			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("start"));
//			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start"));
//			OBAdcAlteon alteon;
//			if(swVersion == null || swVersion.isEmpty())
//			{
//				alteon = new OBAdcAlteon();
//			}
//			else if(swVersion.compareToIgnoreCase("25.3.4.0")==0)
//			{
//				alteon = new OBAdcAlteon250304();
//			}
//			else if(swVersion.compareToIgnoreCase("23.2.9.0")==0)
//			{
//				alteon = new OBAdcAlteon230209();
//			}
//			else if(swVersion.compareToIgnoreCase("28.1.5.0.0")==0)
//			{
//				alteon = new OBAdcAlteon28010500();
//			}
//			else
//			{
//				alteon = new OBAdcAlteon();
//			}
//			alteon.setConnectionInfo(ipaddress, account, password);
//			alteon.login();
//			alteon.disconnect();
//			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:true"));
//			
//			return true;
//		}
//		catch(OBExceptionUnreachable e)
//		{
//			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:false. unreachable"));
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check if system is available(%s)", e.getMessage()));
//			return false;
//		}
//		catch(OBExceptionLogin e)
//		{
//			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:false. login failed"));
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check if system is available(%s)", e.getMessage()));
//			return false;
//		}
//		catch(Exception e)
//		{
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check if system is available(%s)", e.getMessage()));
//			return false;
//		}		
//	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			new OBAdcSystemInfoAlteon().saveAdcSystem(1, "192.168.100.11", "admin", "admin", "");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	public void saveAdcSystem(int adcIndex, String ipaddress, String account, String password, String swVersion,
			int connService, int connPort) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBAdcAlteonHandler alteon = OBCommon.getValidAlteonHandler(swVersion);
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start"));
		alteon.setConnectionInfo(ipaddress, account, password, connService, connPort);
		try {
			alteon.setSocketTimeout(10000);// 10초로 변경.
			alteon.login();
		} catch (OBExceptionUnreachable e) {
			throw new OBExceptionUnreachable(e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBExceptionLogin(e.getMessage());
		}
		try {
			alteon.cmndCRLF();// 삭제하지 마세요.. 이전 데이터 clear용.
			alteon.cmndCRLF();
			alteon.cmndSave();
		} catch (Exception e) {
			alteon.disconnect();
			throw new OBException(e.getMessage());
		}
		alteon.disconnect();
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
	}

	public String getCfgDump(int adcIndex, String ipaddress, String account, String password, String swVersion,
			int connService, int connPort) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start"));
		OBAdcAlteonHandler alteon = OBCommon.getValidAlteonHandler(swVersion);
		alteon.setConnectionInfo(ipaddress, account, password, connService, connPort);
		try {
			alteon.setSocketTimeout(10000);// 10초로 변경.
			alteon.login();
		} catch (OBExceptionUnreachable e) {
			throw new OBExceptionUnreachable(e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBExceptionLogin(e.getMessage());
		}
		String result = "";
		try {
			result = alteon.cmndDumpcfg();
		} catch (Exception e) {
			alteon.disconnect();
			throw new OBException(e.getMessage());
		}
		alteon.disconnect();
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
		return result;
	}

	@Override
	public boolean isAvailableSystem(String ipaddress, String account, String password, int connService, int connPort,
			String cliAccount, String cliPassword) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		try {
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start"));
			OBAdcAlteonHandler alteon;
			alteon = new OBAdcAlteonHandler();
			alteon.setConnectionInfo(ipaddress, account, password, connService, connPort);
			alteon.login();
			alteon.disconnect();
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:true"));

			return true;
		} catch (OBExceptionUnreachable e) {
			throw e;
		} catch (OBExceptionLogin e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

//	public static void main(String[] args)
//	{
//		int mode= 0; //0:adc접속
//		OBAdcSystemInfoAlteon thisclass = new OBAdcSystemInfoAlteon();
//		try
//		{
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(8);
//			OBDtoAdcSystemInfo result = thisclass.getAdcSystemInfoSnmp(adcInfo, mode);
//			System.out.println("result = " + result);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public String getAdcSWVersionCli(String adcIPAddress, String adcCLIAccount, String adcCLIPW, String swVersion,
			int connService, int connPort) throws OBException {
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
	}

	@Override
	public String getAdcSWVersionSnmp(String adcIPAddress, OBDtoAdcSnmpInfo snmpInfo) throws OBException {
		String retVal = "";
		try {
			retVal = new OBSnmpAlteon(adcIPAddress, snmpInfo).getAdcSWVersion(OBDefine.ADC_TYPE_ALTEON, "");
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}
}
