package kr.openbase.adcsmart.service.impl.f5;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBAdcCheckResult;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.impl.OBAdcSystemInfo;
import kr.openbase.adcsmart.service.impl.OBCheckAdcStatus;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcSystemInfo;
import kr.openbase.adcsmart.service.impl.f5.handler.OBAdcF5Handler;
import kr.openbase.adcsmart.service.snmp.dto.DtoSnmpAdcResc;
import kr.openbase.adcsmart.service.utility.OBCipherAES;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;

public class OBAdcSystemInfoF5 implements OBAdcSystemInfo {
//	public static void main(String[] args)
//	{
//		new OBAdcSystemInfoF5().isAvailableSystem("192.168.200.14", "admin", "admin", 0, "");
//	}

	@Override
	public OBDtoAdcSystemInfo getAdcSystemInfo(Integer adcIndex, String ipaddress, String account, String password,
			int connService, int connPort, String swVersion, OBDtoAdcSnmpInfo snmpInfo, int opMode)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		iControl.Interfaces interfaces = CommonF5.initInterfaces(ipaddress, account, password);

		OBDtoAdcSystemInfo info = null;

		try {
			info = SystemF5.getSummary(interfaces);

			DtoSnmpAdcResc snmp;
			OBDtoAdcInfo adcInfo = new OBDtoAdcInfo();
			adcInfo.setIndex(adcIndex);
			adcInfo.setAdcIpAddress(ipaddress);
			adcInfo.setAdcAccount(account);
			adcInfo.setAdcPassword(new OBCipherAES().Encrypt(password));
			adcInfo.setSwVersion(swVersion);
			snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(), snmpInfo)
					.getAdcResc(OBDefine.ADC_TYPE_F5, adcInfo.getSwVersion());

			if (snmp != null) {
				info.setAdcIndex(adcIndex);
				info.setHostName(snmp.getName());
				info.setModel(snmp.getModel());
				info.setSwVersion(snmp.getSwVersion());
				info.setLastBootTime(snmp.getUpTime());
			}
			OBAdcCheckResult retVal = new OBAdcCheckResult();
			retVal = new OBCheckAdcStatus().checkADCStatusSyslog(adcInfo, true);
			info.setStatus(retVal.getStatus());
			info.setAdcIndex(adcIndex);
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
		return info;
	}

//	@Override
//	public boolean isAvailableSystem(String ipaddress, String account,
//			String password, Integer vendor, String swVersion)
//	{
//		try
//		{
//			iControl.Interfaces interfaces = CommonF5.initInterfaces(ipaddress, account, password);
//			SystemF5.checkInterface(interfaces);
//			//System.out.println("interface version = " + SystemF5.checkInterface(interfaces));		
//			return true;
//		}
//		catch(Exception e)
//		{
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check if system is available(%s)", e.getMessage()));
//			return false;
//		}	
//	}

	@Override
	public String getAdcHostName(String ipAddress, String swVersion, OBDtoAdcSnmpInfo snmpInfo) throws OBException {
		OBDatabase db = new OBDatabase();
		String name = "";
		try {
			db.openDB();
			name = OBCommon.getValidSnmpF5Handler(swVersion, ipAddress, snmpInfo).getAdcHostname(OBDefine.ADC_TYPE_F5,
					"", db);// TODO. why ""?
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return name;
	}

	// TODO F5 GUI LOGIN
	@Override
	public boolean isAvailableSystem(String ipaddress, String account, String password, int connService, int connPort,
			String cliAccount, String cliPassword) throws OBExceptionUnreachable, OBExceptionLogin, OBException { // F5는
																													// service,
																													// port를
																													// 바꿀
																													// 수
																													// 없다.
																													// 인터페이스
																													// 파라미터를
																													// 맞춰야
																													// 하기
																													// 때문에
																													// null로
																													// 전달받는다.
		try {
			iControl.Interfaces interfaces = CommonF5.initInterfaces(ipaddress, account, password); // ykk___
			SystemF5.checkInterface(interfaces);
		} catch (Exception e) {
			try {
				CommonF5.Exception("", e.getMessage());
			} catch (OBExceptionUnreachable e1) {
				throw e1;
			} catch (OBExceptionLogin e1) {
				throw e1;
			} catch (OBException e1) {
				throw e1;
			} catch (Exception e1) {
				throw new OBException(e.getMessage());
			}
			return false;
		}
		try {
			OBAdcF5Handler handler = new OBAdcF5Handler();
			handler.setConnectionInfo(ipaddress, cliAccount, cliPassword, connPort);
			handler.sshLogin();
			handler.disconnect();
			return true;
		} catch (Exception e) {
			try {
				CommonF5.Exception("", e.getMessage());
			} catch (OBExceptionUnreachable e1) {
				throw e1;
			} catch (OBExceptionLogin e1) {
				throw e1;
			} catch (OBException e1) {
				throw e1;
			} catch (Exception e1) {
				throw new OBException(e.getMessage());
			}
			return false;
		}
	}

//	public static void main(String[] args) //throws OBException
//	{
//		try
//		{
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
		OBDatabase db = new OBDatabase();
		String retVal = "";
		try {
			db.openDB();
			retVal = OBCommon.getValidSnmpF5Handler("", adcIPAddress, snmpInfo).getAdcSWVersion(OBDefine.ADC_TYPE_F5,
					"", db);
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
}