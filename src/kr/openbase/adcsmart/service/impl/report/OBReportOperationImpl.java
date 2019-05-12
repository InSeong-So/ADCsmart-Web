package kr.openbase.adcsmart.service.impl.report;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import kr.openbase.adcsmart.service.OBReportOperation;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.dto.OBDtoAuditLogAdcSystem;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptAdcInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptPortErrDiscard;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptPortInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptSysInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptSysInfoBasic;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptSysInfoEtc;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptSysInfoL2;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptSysInfoL3;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptSysInfoL4;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptSysInfoL7;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptSystemInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptTitle;
import kr.openbase.adcsmart.service.impl.OBAdcConfigHistoryImpl;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBTelnetCmndExecV2;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBAdcAlteonHandler;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBCLIParserAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoGateWayInfoAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoGeneralAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoIPAddrInfoAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoIPInfoAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoInfoLogsAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoInfoVlanAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoLicenseInfoAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoLinkInfoAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoMPResourceAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoNtpCfgAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoRSrvStatusAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoSnmpCfgAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoStatLinkAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoStatSlbSessionInfoAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoSyslogCfgAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoVSvcStatusAlteon;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolMemberPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.OBAdcPASHandler;
import kr.openbase.adcsmart.service.impl.pas.handler.OBCLIParserPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoGatewayInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoHWStatPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoInterfaceInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoLoggingBufferPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoNTPInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoPortInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoPortStatPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoResourceInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoSnmpInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoSyslogInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoSystemInfoPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoVLanInfoPAS;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolMemberPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.OBAdcPASKHandler;
import kr.openbase.adcsmart.service.impl.pask.handler.OBCLIParserPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.DtoRptStpInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.DtoRptTrunkInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoHWStatPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoInterfaceInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoLicenseInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoLoggingBufferPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoNTPInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoPortInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoPortStatPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoResourceInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoSnmpInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoSyslogInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoSystemInfoPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoVLanInfoPASK;
import kr.openbase.adcsmart.service.snmp.alteon.OBSnmpAlteon;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoRptOPL3;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoRptOPSys;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptConnStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptCpu;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptL3Gateway;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptL3Int;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptLinkStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptMem;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptNtp;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptOPEtc;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptOPGen;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptOPL2;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptOPL4;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptOPL7;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptOPL7iRule;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptPMStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptPortStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptStgStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptStpStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptSyslog;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptTrunkStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptVSStatus;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptVlanStatus;
import kr.openbase.adcsmart.service.snmp.f5.OBSnmpF5;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBReportOperationImpl implements OBReportOperation {
	@Override
	public OBDtoRptTitle getTitle(String rptIndex) throws OBException {
		try {
			OBReportInfo rptInfo = new OBReportImpl().getReportInfo(rptIndex);
			OBDtoRptTitle result = new OBDtoRptTitle();
			result.setIndex(rptInfo.getIndex());
			result.setAdcList(rptInfo.getAdcList());
			result.setBeginTime(rptInfo.getBeginTime());
			result.setEndTime(rptInfo.getEndTime());
			result.setOccurTime(rptInfo.getOccurTime());
			result.setUserIndex(rptInfo.getAccountIndex());
			result.setUserID(rptInfo.getAccountID());
			result.setExtraInfo(rptInfo.getExtraInfo());
			return result;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDtoRptAdcInfo info = new OBReportOperationImpl().getAdcInfo("1392957553015", 3);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public OBDtoRptAdcInfo getAdcInfo(String rptIndex, Integer adcIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. rptIndex:%s, adcIndex:%d", rptIndex, adcIndex));
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			if (adcInfo == null)
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "invalid adcIndex");

			OBDtoRptAdcInfo result;
			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
				result = getAdcInfoAlteon(adcInfo);
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5)
				result = getAdcInfoF5(adcInfo);
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS)
				result = getAdcInfoPAS(adcInfo);
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK)
				result = getAdcInfoPASK(adcInfo);
			else
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported vendor");

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
	}

	private OBDtoRptAdcInfo getAdcInfoF5(OBDtoAdcInfo adcInfo) throws OBException {
		OBDtoRptAdcInfo result = new OBDtoRptAdcInfo();
		result.setIndex(adcInfo.getIndex());
		result.setName(adcInfo.getName());
		result.setiPAddress(adcInfo.getAdcIpAddress());
		result.setModel(adcInfo.getModel());
		try {
			OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());// new OBSnmpF5(OBSnmp.VERSION_2C, adcInfo.getAdcIpAddress());

			DtoRptOPGen info = snmp.getRptOpGeneralInfo(adcInfo.getAdcType(), adcInfo.getSwVersion());
			result.setHostName(info.getHostName());
			result.setOsVersion(info.getOsVersion());
			result.setLicense(info.getLicenseInfo());
			result.setMacAddress(info.getMacAddress());
			result.setSerialNum(info.getSerialNum());
			result.setRunTime(info.getUpTime());
			result.setActiveStandby(info.getActiveStandby());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return result;
	}

	private OBDtoRptAdcInfo getAdcInfoPAS(OBDtoAdcInfo adcInfo) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoRptAdcInfo retVal = new OBDtoRptAdcInfo();

		retVal.setIndex(adcInfo.getIndex());
		retVal.setName(adcInfo.getName());
		retVal.setiPAddress(adcInfo.getAdcIpAddress());

		OBCLIParserPAS parserClass = new OBCLIParserPAS();

		OBAdcPASHandler adcHandler = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
		adcHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
				adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());

//		result.setModel(adcInfo.getModel());
		String infoDump = "";
		try {
			db.openDB();

			adcHandler.login();
			try {
				infoDump = adcHandler.cmndSystem();
				OBDtoSystemInfoPAS systemInfo = parserClass.parseSystem(infoDump);
				if (systemInfo != null) {
					retVal.setModel(systemInfo.getProductName());
					retVal.setSerialNum(systemInfo.getSerialNum());
					retVal.setRunTime(systemInfo.getUpTime());
					retVal.setOsVersion(systemInfo.getVersion());
				}

				infoDump = adcHandler.cmndSnmpInfo();
				OBDtoSnmpInfoPAS snmpInfo = parserClass.parseSnmpInfo(infoDump);
				if (snmpInfo != null) {
					retVal.setHostName(snmpInfo.getHostName());
				}

				infoDump = adcHandler.cmndInterface();
				ArrayList<OBDtoInterfaceInfoPAS> interfaceList = parserClass.parseInterface(infoDump);
				if (snmpInfo != null) {
					String macAddr = "";
					for (OBDtoInterfaceInfoPAS obj : interfaceList) {
						if (!macAddr.isEmpty())
							macAddr += "\n";
						macAddr += obj.getMacAddr();
					}
					retVal.setMacAddress(macAddr);
				}
			} catch (OBException e) {
				throw e;
			} catch (Exception e) {
				throw e;
			} finally {
				adcHandler.disconnect();
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private OBDtoRptAdcInfo getAdcInfoPASK(OBDtoAdcInfo adcInfo) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoRptAdcInfo retVal = new OBDtoRptAdcInfo();

		retVal.setIndex(adcInfo.getIndex());
		retVal.setName(adcInfo.getName());
		retVal.setiPAddress(adcInfo.getAdcIpAddress());

		OBCLIParserPASK parserClass = new OBCLIParserPASK();

		OBAdcPASKHandler adcHandler = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
		adcHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
				adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());

		String infoDump = "";
		try {
			db.openDB();

			adcHandler.login();
			try {
				infoDump = adcHandler.cmndSystem();
				OBDtoSystemInfoPASK systemInfo = parserClass.parseSystem(infoDump);
				if (systemInfo != null) {
					retVal.setModel(systemInfo.getProductName());
					retVal.setSerialNum(systemInfo.getSerialNum());
					retVal.setRunTime(systemInfo.getUpTime());
					retVal.setOsVersion(systemInfo.getVersion());
				}

				infoDump = adcHandler.cmndSnmpInfo();
				OBDtoSnmpInfoPASK snmpInfo = parserClass.parseSnmpInfo(infoDump);
				if (snmpInfo != null) {
					retVal.setHostName(snmpInfo.getHostName());
				}

				infoDump = adcHandler.cmndInterface();
				ArrayList<OBDtoInterfaceInfoPASK> interfaceList = parserClass.parseInterface(infoDump);
				if (snmpInfo != null) {
					String macAddr = "";
					for (OBDtoInterfaceInfoPASK obj : interfaceList) {
						if (!macAddr.isEmpty())
							macAddr += "\n";
						macAddr += obj.getMacAddr();
					}
					retVal.setMacAddress(macAddr);
				}

				// license 정보 추출.
				infoDump = adcHandler.cmndLicense();//
				if (infoDump != null && !infoDump.isEmpty()) {
					OBDtoLicenseInfoPASK licInfo = parserClass.parseLicense(infoDump);
					String licContent = "";
					licContent += "State      : " + licInfo.getStatus();
					licContent += "\n";
					licContent += "Expire Date: " + licInfo.getExpiredDate();
					retVal.setLicense(licContent);
				}
			} catch (OBException e) {
				throw e;
			} catch (Exception e) {
				throw e;
			} finally {
				adcHandler.disconnect();
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private OBDtoRptAdcInfo getAdcInfoAlteonSnmp(OBDtoAdcInfo adcInfo) throws OBException {
		OBDtoRptAdcInfo result = new OBDtoRptAdcInfo();

		result.setIndex(adcInfo.getIndex());
		result.setName(adcInfo.getName());
		result.setiPAddress(adcInfo.getAdcIpAddress());
		result.setModel(adcInfo.getModel());

		try {
			OBSnmpAlteon snmp = new OBSnmpAlteon(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());

			DtoRptOPGen info = snmp.getRptOpGeneralInfo(adcInfo.getAdcType(), adcInfo.getSwVersion());
			result.setHostName(info.getHostName());
			result.setOsVersion(info.getOsVersion());
			result.setLicense(info.getLicenseInfo());
			result.setMacAddress(info.getMacAddress());
			result.setSerialNum(info.getSerialNum());
			result.setRunTime(info.getUpTime());
			result.setActiveStandby(info.getActiveStandby());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return result;
	}

	// [CLI 접속-명령실행]으로 데이터를 구한다.
	private OBDtoRptAdcInfo getAdcInfoAlteonCLI(OBDtoAdcInfo adcInfo) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoRptAdcInfo retVal = new OBDtoRptAdcInfo();

		retVal.setIndex(adcInfo.getIndex());
		retVal.setName(adcInfo.getName());
		retVal.setiPAddress(adcInfo.getAdcIpAddress());
		retVal.setModel(adcInfo.getModel());

		OBCLIParserAlteon parserClass = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());// new
																									// OBCLIParserAlteon();

		OBAdcAlteonHandler adcHandler = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
		adcHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
				adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());

		String cfgDump = "";
		try {
			db.openDB();

			adcHandler.login();
			// host name 추출
			try {
				cfgDump = adcHandler.cmndCfgSnmp();//
				if (cfgDump != null && !cfgDump.isEmpty()) {
					OBDtoSnmpCfgAlteon snmpCfg = parserClass.parseSnmpCfg(cfgDump);
					if (snmpCfg != null) {
						retVal.setHostName(snmpCfg.getSysName());
					}
				}
				// general info 추출.
				cfgDump = adcHandler.cmndSysGeneral();//
				if (cfgDump != null && !cfgDump.isEmpty()) {
					OBDtoGeneralAlteon generalInfo = parserClass.parseGeneralInfo(cfgDump);
					if (generalInfo != null) {
						retVal.setOsVersion(generalInfo.getSwVersion());
						retVal.setMacAddress(generalInfo.getMacAddr());
						retVal.setSerialNum(generalInfo.getSerialNum());
						retVal.setRunTime(generalInfo.getUpTime());

					}
				}
				// license 정보 추출.
				cfgDump = adcHandler.cmndInfoLicense();//
				if (cfgDump != null && !cfgDump.isEmpty()) {
					ArrayList<OBDtoLicenseInfoAlteon> licList = parserClass.parseInfoLicense(cfgDump);
					String licContent = "";
					for (OBDtoLicenseInfoAlteon lic : licList) {
						if (!licContent.isEmpty())
							licContent += "\n";
						licContent += lic.getName() + ": " + lic.getStatus();
					}
					retVal.setLicense(licContent);

				}
			} catch (OBException e) {
				throw e;
			} catch (Exception e) {
				throw e;
			} finally {
				adcHandler.disconnect();
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private boolean isAvailableSnmpAlteon() {
		String fileName = OBDefine.CFG_DIR + "rpt.alteon.snmp";
		;
		File f = new File(fileName);
		if (f.exists())
			return true;
		return false;
	}

	private OBDtoRptAdcInfo getAdcInfoAlteon(OBDtoAdcInfo adcInfo) throws OBException {
		if (isAvailableSnmpAlteon() == true)
			return getAdcInfoAlteonSnmp(adcInfo);
		else
			return getAdcInfoAlteonCLI(adcInfo);
	}

//	public static void main(String[] args)
//	{
//		try
//		{
////			OBDtoRptSystemInfo info = new OBReportOperationImpl().getSystemInfo("1351500971602", 1);
//			OBDtoRptSystemInfo info = new OBReportOperationImpl().getSystemInfo("1392957553015", 1);
////			OBDtoRptSystemInfo info = new OBReportOperationImpl().getSystemInfo("1376012933353", 57);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public OBDtoRptSystemInfo getSystemInfo(String rptIndex, Integer adcIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. rptIndex:%s, adcIndex:%d", rptIndex, adcIndex));

		OBDatabase db = new OBDatabase();

		OBDtoRptSystemInfo result = null;
		try {
			db.openDB();

			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			OBReportInfo rptInfo = new OBReportImpl().getReportInfo(rptIndex);
			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
				result = getSystemInfoAlteon(adcInfo, rptInfo, db);
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5)
				result = getSystemInfoF5(adcInfo, rptInfo, db);
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS)
				result = getSystemInfoPAS(adcInfo, rptInfo, db);
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK)
				result = getSystemInfoPASK(adcInfo, rptInfo, db);
			else
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported vendor");
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end:%s", result));

		return result;
	}

	private OBDtoRptSysInfoBasic getSysInfoBasicAlteonSnmp(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBDatabase db)
			throws OBException {// 시스템 info 추출.
		OBDtoRptSysInfoBasic output = new OBDtoRptSysInfoBasic();
		String contents = "";
		try {
			OBSnmpAlteon snmp = new OBSnmpAlteon(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());

			DtoRptOPSys rptOPSysInfo = null;
			try {
				rptOPSysInfo = snmp.getRptOPSystemInfo(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} catch (OBException e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get rpt_op_system_info.%s", e.getErrorMessage()));
			}
			// uptime 구성
			OBDtoRptSysInfo upTimeInfo = new OBDtoRptSysInfo();
			contents = "";
			upTimeInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_UPTIME));// OBTerminologyDB.RPT_BASIC_UPTIME);
			if (rptOPSysInfo != null) {
				upTimeInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				upTimeInfo.setContents(rptOPSysInfo.getUpTime());
			}
			output.setUpTime(upTimeInfo);

			// last apply time 구성
			OBDtoRptSysInfo applyTimeInfo = new OBDtoRptSysInfo();
			contents = "";
			applyTimeInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_LASTAPPLY));// OBTerminologyDB.RPT_BASIC_LASTAPPLY);
			if (adcInfo.getApplyTime() != null) {
				applyTimeInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				long days = 0;
				Timestamp nowTime = OBDateTime.toTimestamp(OBDateTime.now());
				long diff = Math.abs(nowTime.getTime() - adcInfo.getApplyTime().getTime());
				days = diff / (1000 * 60 * 60 * 24);
				contents = String.format("%s (%d %s)", OBDateTime.toString(adcInfo.getApplyTime()), days,
						OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DAY));// OBTerminologyDB.TYPE_GENERAL_DAY);
			}
			applyTimeInfo.setContents(contents);
			output.setLastApplyTime(applyTimeInfo);

			// cpu 정보 구성
			OBDtoRptSysInfo cpuInfo = new OBDtoRptSysInfo();
			contents = "";
			cpuInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_CPUINFO));// OBTerminologyDB.RPT_BASIC_CPUINFO);
			if (rptOPSysInfo != null) {
				cpuInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				ArrayList<DtoRptCpu> cpuList = rptOPSysInfo.getCpuList();
				long cpuUsage = 0;
				int spIndex = 1;
				for (DtoRptCpu cpu : cpuList) {
					cpuUsage += cpu.getUsage();
					if (cpu.getType() == OBDefine.SYS_CPU_TYPE_MP) {
						if (!contents.isEmpty())
							contents += ", ";
						contents += String.format("%s: %d", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_MPCPU),
								cpu.getUsage());
					} else {
						if (!contents.isEmpty())
							contents += ", ";
						contents += String.format("%s[%d]: %d", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_SPCPU),
								spIndex, cpu.getUsage());
						spIndex++;
					}
				}
				String avgContent = String.format("%s: %d \n",
						OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_AVG), cpuUsage / cpuList.size());
				contents = avgContent + contents;
				cpuInfo.setContents(contents);
			}
			output.setCpuInfo(cpuInfo);

			// memory 정보 구성
			OBDtoRptSysInfo memInfo = new OBDtoRptSysInfo();
			contents = "";
			memInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_MEMINFO));// OBTerminologyDB.RPT_BASIC_MEMINFO);
			if (rptOPSysInfo != null) {
				memInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				ArrayList<DtoRptMem> memList = rptOPSysInfo.getMemList();
				long memUsage = 0;
				for (DtoRptMem mem : memList) {
					memUsage += (mem.getUsed() * 100 / mem.getTotal());
				}
				contents = String.format("%s: %d \n", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_AVG),
						memUsage / memList.size());
				memInfo.setContents(contents);
			}
			output.setMemoryInfo(memInfo);

			// power 정보 구성
//			OBDtoRptSysInfo powerInfo = new OBDtoRptSysInfo();
//			contents="";
//			powerInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_POWERINFO));//OBTerminologyDB.RPT_BASIC_POWERINFO);
//			powerInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));//OBTerminologyDB.TYPE_GENERAL_NORMAL);
//			rptOPSysInfo.getPowerSupplyStatus();
//			output.setPowerInfo(powerInfo);

			// fan 정보 구성.
			OBDtoRptSysInfo fanInfo = new OBDtoRptSysInfo();
			contents = "";
			fanInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_FANINFO));// OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_FANINFO));//OBTerminologyDB.RPT_BASIC_FANINFO);
			if (rptOPSysInfo != null) {
				if (rptOPSysInfo.getFanStatus() == OBDefine.SYS_FAN_STATUS_OK)
					fanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				else
					fanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
				fanInfo.setContents("");
			}
			output.setFanInfo(fanInfo);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return output;
	}

	private OBDtoRptSysInfoBasic getSysInfoBasicAlteonCLI(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo,
			OBAdcAlteonHandler adcHandler, OBDatabase db) throws OBException {// 시스템 info 추출.
		OBDtoRptSysInfoBasic retVal = new OBDtoRptSysInfoBasic();
		OBCLIParserAlteon parserClass = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
		String cfgDump = "";
		String contents = "";
		try {
			// uptime 구성
			cfgDump = adcHandler.cmndSysGeneral();//
			if (cfgDump != null && !cfgDump.isEmpty()) {
				OBDtoGeneralAlteon cfgInfo = parserClass.parseGeneralInfo(cfgDump);
				if (cfgInfo != null) {
					// uptime 구성.
					OBDtoRptSysInfo upTimeInfo = new OBDtoRptSysInfo();
					contents = "";
					upTimeInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_UPTIME));// OBTerminologyDB.RPT_BASIC_UPTIME);
					upTimeInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
					upTimeInfo.setContents(cfgInfo.getUpTime());
					retVal.setUpTime(upTimeInfo);

					// last apply time 구성
					OBDtoRptSysInfo applyTimeInfo = new OBDtoRptSysInfo();
					contents = "";
					applyTimeInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_LASTAPPLY));// OBTerminologyDB.RPT_BASIC_LASTAPPLY);
					if (adcInfo.getApplyTime() != null) {
						applyTimeInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
						long days = 0;
						Timestamp nowTime = OBDateTime.toTimestamp(OBDateTime.now());
						long diff = Math.abs(nowTime.getTime() - adcInfo.getApplyTime().getTime());
						days = diff / (1000 * 60 * 60 * 24);
						contents = String.format("%s (%d %s)", OBDateTime.toString(adcInfo.getApplyTime()), days,
								OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DAY));// OBTerminologyDB.TYPE_GENERAL_DAY);
					}
					applyTimeInfo.setContents(contents);
					retVal.setLastApplyTime(applyTimeInfo);
				}
			}

			cfgDump = adcHandler.cmndStatSPCpuResources();//
			String mpRescDump = adcHandler.cmndStatMPResources();//

			// cpu 정보 구성
			OBDtoRptSysInfo cpuInfo = new OBDtoRptSysInfo();
			contents = "";
			cpuInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_CPUINFO));// OBTerminologyDB.RPT_BASIC_CPUINFO);
			ArrayList<Integer> spList = parserClass.parseStatSPUsage(cfgDump);
			OBDtoMPResourceAlteon mpInfo = parserClass.parseStatMPResources(mpRescDump);
			if (spList != null && mpInfo != null) {
				cpuInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				long cpuUsage = 0;
				int spIndex = 1;
				contents = String.format("%s: %d", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_MPCPU),
						mpInfo.getCpuUsage());
				for (Integer spUsage : spList) {
					cpuUsage += spUsage;
					if (!contents.isEmpty())
						contents += ", ";
					contents += String.format("%s[%d]: %d", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_SPCPU),
							spIndex, spUsage);
					spIndex++;
				}
				cpuUsage += mpInfo.getCpuUsage();
				String avgContent = String.format("%s: %d \n",
						OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_AVG), cpuUsage / (spList.size() + 1));
				contents = avgContent + contents;
				cpuInfo.setContents(contents);
			}
			retVal.setCpuInfo(cpuInfo);

			// memory 정보 구성
			OBDtoRptSysInfo memInfo = new OBDtoRptSysInfo();
			contents = "";
			memInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_MEMINFO));// OBTerminologyDB.RPT_BASIC_MEMINFO);
			if (mpInfo != null) {
				memInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				contents = String.format("%s: %d \n", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_AVG),
						mpInfo.getMemUsage());
				memInfo.setContents(contents);
			}
			retVal.setMemoryInfo(memInfo);

			// power 정보 구성
//			OBDtoRptSysInfo powerInfo = new OBDtoRptSysInfo();
//			contents="";
//			powerInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_POWERINFO));//OBTerminologyDB.RPT_BASIC_POWERINFO);
//			powerInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));//OBTerminologyDB.TYPE_GENERAL_NORMAL);
//			rptOPSysInfo.getPowerSupplyStatus();
//			output.setPowerInfo(powerInfo);

			// fan 정보 구성.
			OBDtoRptSysInfo fanInfo = new OBDtoRptSysInfo();
			contents = "";
			fanInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_FANINFO));// OBTerminologyDB.RPT_BASIC_FANINFO);
			cfgDump = adcHandler.cmndStatFans();//
			int status = parserClass.parseFanStatus(cfgDump);
			if (status == OBDefine.SYS_FAN_STATUS_OK)
				fanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
			else
				fanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
			fanInfo.setContents("");
			retVal.setFanInfo(fanInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	private Timestamp getLastApplyTime(int adcIndex, OBDatabase db) throws OBException {
		HashMap<String, Timestamp> hashMap = new OBAdcConfigHistoryImpl().getLastConfigTimeList(adcIndex, db);
		ArrayList<Timestamp> timeList = new ArrayList<Timestamp>(hashMap.values());
		if (timeList.size() == 0)
			return null;

		Timestamp lastTime = timeList.get(0);
		for (Timestamp time : timeList) {
			if (time.getTime() > lastTime.getTime())
				lastTime = time;
		}
		return lastTime;
	}

	private OBDtoRptSysInfoBasic getSysInfoBasicPAS(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBAdcPASHandler handler,
			OBDatabase db) throws OBException {// 시스템 info 추출.
		OBCLIParserPAS parserClass = new OBCLIParserPAS();
		OBDtoRptSysInfoBasic retVal = new OBDtoRptSysInfoBasic();
		String contents = "";
		try {
			OBDtoSystemInfoPAS systemInfo = null;
			OBDtoResourceInfoPAS resourceInfo = null;
			OBDtoHWStatPAS hwInfo = null;
			try {
				String cfg = handler.cmndSystem();
				systemInfo = parserClass.parseSystem(cfg);

				cfg = handler.cmndResources();
				resourceInfo = parserClass.parseResources(cfg);

				cfg = handler.cmndHwStatistics();
				hwInfo = parserClass.parseHWStatistics(cfg);
			} catch (OBException e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get rpt_op_system_info.%s", e.getErrorMessage()));

			}
			// uptime 구성
			OBDtoRptSysInfo upTimeInfo = new OBDtoRptSysInfo();
			contents = "";
			upTimeInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_UPTIME));// OBTerminologyDB.RPT_BASIC_UPTIME);
			if (systemInfo != null) {
				upTimeInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				upTimeInfo.setContents(systemInfo.getUpTime());
			}
			retVal.setUpTime(upTimeInfo);

			// last apply time 구성. DB에서 추출한다. 즉 SLB 설정시간을 추출한다.
			OBDtoRptSysInfo applyTimeInfo = new OBDtoRptSysInfo();
			contents = "";
			applyTimeInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_LASTAPPLY));// OBTerminologyDB.RPT_BASIC_LASTAPPLY);
			Timestamp lastTime = getLastApplyTime(adcInfo.getIndex(), db);
			if (lastTime != null) {
				applyTimeInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				long days = 0;
				Timestamp nowTime = OBDateTime.toTimestamp(OBDateTime.now());
				long diff = Math.abs(nowTime.getTime() - lastTime.getTime());
				days = diff / (1000 * 60 * 60 * 24);
				contents = String.format("%s (%d %s)", OBDateTime.toString(lastTime), days,
						OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DAY));// OBTerminologyDB.TYPE_GENERAL_DAY);
			}
			applyTimeInfo.setContents(contents);
			retVal.setLastApplyTime(applyTimeInfo);

			// cpu 정보 구성
			OBDtoRptSysInfo cpuInfo = new OBDtoRptSysInfo();
			contents = "";
			cpuInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_CPUINFO));// OBTerminologyDB.RPT_BASIC_CPUINFO);
			if (resourceInfo != null) {
				cpuInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);

				long cpuUsage = (resourceInfo.getCpuUsageMP() + resourceInfo.getCpuUsageSP()) / 2;
				contents = String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_MPCPU),
						resourceInfo.getCpuUsageMP());
				contents += "%";
				contents += ", ";
				contents += String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_SPCPU),
						resourceInfo.getCpuUsageSP());
				contents += "%";

				String avgContent = String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_AVG),
						cpuUsage);
				avgContent += "%\n";
				contents = avgContent + contents;
				cpuInfo.setContents(contents);
			}
			retVal.setCpuInfo(cpuInfo);

			// memory 정보 구성
			OBDtoRptSysInfo memInfo = new OBDtoRptSysInfo();
			contents = "";
			memInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_MEMINFO));// OBTerminologyDB.RPT_BASIC_MEMINFO);
			if (resourceInfo != null) {
				memInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);

				long memUsage = (resourceInfo.getMemUsageMP() + resourceInfo.getMemUsageSP()) / 2;
				contents = String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_MPCPU),
						resourceInfo.getMemUsageMP());
				contents += "%";
				contents += ", ";
				contents += String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_SPCPU),
						resourceInfo.getMemUsageSP());
				contents += "%";

				String avgContent = String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_AVG),
						memUsage);
				avgContent += "%\n";
				contents = avgContent + contents;
				memInfo.setContents(contents);
			}
			retVal.setMemoryInfo(memInfo);

			// power 정보 구성
			OBDtoRptSysInfo powerInfo = new OBDtoRptSysInfo();
			contents = "";
			powerInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_POWERINFO));// OBTerminologyDB.RPT_BASIC_POWERINFO);
			if (hwInfo != null) {
				boolean status = true;
				for (int i = 0; i < OBDtoHWStatPAS.POWER_COUNT; i++) {
					if (!contents.isEmpty())
						contents += ", ";
					if (hwInfo.getPwStatusList(i) == OBDefine.SYS_POWERSUPPLY_STATUS_OK)
						contents += String.format("PW%d: ON", i + 1);
					else if (hwInfo.getPwStatusList(i) == OBDefine.SYS_POWERSUPPLY_STATUS_FAIL) {
						contents += String.format("PW%d: OFF", i + 1);
						status = false;
					}
				}

				if (status == true)
					powerInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				else
					powerInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);

				powerInfo.setContents(contents);
			}
			retVal.setPowerInfo(powerInfo);

			// fan 정보 구성.
			OBDtoRptSysInfo fanInfo = new OBDtoRptSysInfo();
			contents = "";
			fanInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_FANINFO));// OBTerminologyDB.RPT_BASIC_FANINFO);
			if (hwInfo != null) {
				boolean status = true;

				for (int i = 0; i < OBDtoHWStatPAS.FAN_COUNT; i++) {
					if (!contents.isEmpty())
						contents += ", ";
					if (hwInfo.getFanStatus(i) == OBDefine.SYS_FAN_STATUS_OK)
						contents += String.format("FAN%d: ON", i + 1);
					else if (hwInfo.getPwStatusList(i) == OBDefine.SYS_FAN_STATUS_FAIL) {
						contents += String.format("FAN%d: OFF", i + 1);
						status = false;
					}
				}

				if (status == true)
					fanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				else
					fanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);

				fanInfo.setContents(contents);
			}
			retVal.setFanInfo(fanInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	private OBDtoRptSysInfoBasic getSysInfoBasicPASK(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo,
			OBAdcPASKHandler handler, OBDatabase db) throws OBException {// 시스템 info 추출.
		OBCLIParserPASK parserClass = new OBCLIParserPASK();
		OBDtoRptSysInfoBasic retVal = new OBDtoRptSysInfoBasic();
		String contents = "";
		try {
			OBDtoSystemInfoPASK systemInfo = null;
			OBDtoResourceInfoPASK resourceInfo = null;
			OBDtoHWStatPASK hwInfo = null;
			Timestamp applyTime = null;
			try {
				String cfg = handler.cmndSystem();
				systemInfo = parserClass.parseSystem(cfg);

				cfg = handler.cmndResources();
				resourceInfo = parserClass.parseResources(cfg);

				cfg = handler.cmndHwStatistics();
				hwInfo = parserClass.parseHWStatistics(cfg);

				cfg = handler.cmndSlbDump();
				applyTime = parserClass.parseApplyTime(cfg);
			} catch (OBException e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get rpt_op_system_info.%s", e.getErrorMessage()));

			}
			// uptime 구성
			OBDtoRptSysInfo upTimeInfo = new OBDtoRptSysInfo();
			contents = "";
			upTimeInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_UPTIME));// OBTerminologyDB.RPT_BASIC_UPTIME);
			if (systemInfo != null) {
				upTimeInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				upTimeInfo.setContents(systemInfo.getUpTime());
			}
			retVal.setUpTime(upTimeInfo);

			// last apply time 구성. DB에서 추출한다. 즉 SLB 설정시간을 추출한다.
			OBDtoRptSysInfo applyTimeInfo = new OBDtoRptSysInfo();
			contents = "";
			applyTimeInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_LASTAPPLY));// OBTerminologyDB.RPT_BASIC_LASTAPPLY);
			Timestamp lastTime = applyTime;
			if (lastTime != null) {
				applyTimeInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				long days = 0;
				Timestamp nowTime = OBDateTime.toTimestamp(OBDateTime.now());
				long diff = Math.abs(nowTime.getTime() - lastTime.getTime());
				days = diff / (1000 * 60 * 60 * 24);
				contents = String.format("%s (%d %s)", OBDateTime.toString(lastTime), days,
						OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DAY));// OBTerminologyDB.TYPE_GENERAL_DAY);
			}
			applyTimeInfo.setContents(contents);
			retVal.setLastApplyTime(applyTimeInfo);

			// cpu 정보 구성
			OBDtoRptSysInfo cpuInfo = new OBDtoRptSysInfo();
			contents = "";
			cpuInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_CPUINFO));// OBTerminologyDB.RPT_BASIC_CPUINFO);
			if (resourceInfo != null) {
				cpuInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);

				long cpuUsage = (resourceInfo.getCpuUsageMP() + resourceInfo.getCpuUsageSP()) / 2;
				contents = String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_MPCPU),
						resourceInfo.getCpuUsageMP());
				contents += "%";
				contents += ", ";
				contents += String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_SPCPU),
						resourceInfo.getCpuUsageSP());
				contents += "%";

				String avgContent = String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_AVG),
						cpuUsage);
				avgContent += "%\n";
				contents = avgContent + contents;
				cpuInfo.setContents(contents);
			}
			retVal.setCpuInfo(cpuInfo);

			// memory 정보 구성
			OBDtoRptSysInfo memInfo = new OBDtoRptSysInfo();
			contents = "";
			memInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_MEMINFO));// OBTerminologyDB.RPT_BASIC_MEMINFO);
			if (resourceInfo != null) {
				memInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);

				long memUsage = (resourceInfo.getMemUsageMP() + resourceInfo.getMemUsageSP()) / 2;
				contents = String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_MPCPU),
						resourceInfo.getMemUsageMP());
				contents += "%";
				contents += ", ";
				contents += String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_SPCPU),
						resourceInfo.getMemUsageSP());
				contents += "%";

				String avgContent = String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_AVG),
						memUsage);
				avgContent += "%\n";
				contents = avgContent + contents;
				memInfo.setContents(contents);
			}
			retVal.setMemoryInfo(memInfo);

			// power 정보 구성
			OBDtoRptSysInfo powerInfo = new OBDtoRptSysInfo();
			contents = "";
			powerInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_POWERINFO));// OBTerminologyDB.RPT_BASIC_POWERINFO);
			if (hwInfo != null) {
				boolean status = true;
				if (hwInfo.getAc1Status() == OBDefine.SYS_POWERSUPPLY_STATUS_OK) {
					contents = String.format("AC1: ON");
				} else {
					status = false;
					contents = String.format("AC1: OFF");
				}

				if (hwInfo.getAc2Status() == OBDefine.SYS_POWERSUPPLY_STATUS_OK) {
					if (!contents.isEmpty())
						contents += ", ";
					contents += String.format("AC2: ON");
				} else {
					status = false;
					if (!contents.isEmpty())
						contents += ", ";
					contents += String.format("AC2: OFF");
				}

				if (status == true)
					powerInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				else
					powerInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);

				powerInfo.setContents(contents);
			}
			retVal.setPowerInfo(powerInfo);

			// fan 정보 구성.
			OBDtoRptSysInfo fanInfo = new OBDtoRptSysInfo();
			contents = "";
			fanInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_FANINFO));// OBTerminologyDB.RPT_BASIC_FANINFO);
			if (hwInfo != null) {
				boolean status = true;
				if (hwInfo.getFan1Status() == OBDefine.SYS_FAN_STATUS_OK) {
					contents = String.format("FAN1: ON");
				} else {
					status = false;
					contents = String.format("FAN1: OFF");
				}

				if (hwInfo.getFan2Status() == OBDefine.SYS_POWERSUPPLY_STATUS_OK) {
					if (!contents.isEmpty())
						contents += ", ";
					contents += String.format("FAN2: ON");
				} else {
					status = false;
					if (!contents.isEmpty())
						contents += ", ";
					contents += String.format("FAN2: OFF");
				}

				if (hwInfo.getFan3Status() == OBDefine.SYS_POWERSUPPLY_STATUS_OK) {
					if (!contents.isEmpty())
						contents += ", ";
					contents += String.format("FAN3: ON");
				} else {
					status = false;
					if (!contents.isEmpty())
						contents += ", ";
					contents += String.format("FAN3: OFF");
				}

				if (hwInfo.getFan4Status() == OBDefine.SYS_POWERSUPPLY_STATUS_OK) {
					if (!contents.isEmpty())
						contents += ", ";
					contents += String.format("FAN4: ON");
				} else {
					status = false;
					if (!contents.isEmpty())
						contents += ", ";
					contents += String.format("FAN4: OFF");
				}

				if (status == true)
					fanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				else
					fanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);

				fanInfo.setContents(contents);
			}
			retVal.setFanInfo(fanInfo);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	private OBDtoRptSysInfoBasic getSysInfoBasicF5(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo) throws OBException {// 시스템
																													// info
																													// 추출.
		OBDtoRptSysInfoBasic output = new OBDtoRptSysInfoBasic();
		String contents = "";
		try {
			OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());// new OBSnmpF5(adcInfo.getAdcIpAddress());

			DtoRptOPSys rptOPSysInfo = null;
			try {
				rptOPSysInfo = snmp.getRptOPSystemInfo(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} catch (OBException e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get rpt_op_system_info.%s", e.getErrorMessage()));

			}
			// uptime 구성
			OBDtoRptSysInfo upTimeInfo = new OBDtoRptSysInfo();
			contents = "";
			upTimeInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_UPTIME));// OBTerminologyDB.RPT_BASIC_UPTIME);
			if (rptOPSysInfo != null) {
				upTimeInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				upTimeInfo.setContents(rptOPSysInfo.getUpTime());
			}
			output.setUpTime(upTimeInfo);

			// last apply time 구성
			OBDtoRptSysInfo applyTimeInfo = new OBDtoRptSysInfo();
			contents = "";
			applyTimeInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_LASTAPPLY));// OBTerminologyDB.RPT_BASIC_LASTAPPLY);
			if (adcInfo.getApplyTime() != null) {
				applyTimeInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				long days = 0;
				Timestamp nowTime = OBDateTime.toTimestamp(OBDateTime.now());
				long diff = Math.abs(nowTime.getTime() - adcInfo.getApplyTime().getTime());
				days = diff / (1000 * 60 * 60 * 24);
				contents = String.format("%s (%d %s)", OBDateTime.toString(adcInfo.getApplyTime()), days,
						OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DAY));// OBTerminologyDB.TYPE_GENERAL_DAY);
			}
			applyTimeInfo.setContents(contents);
			output.setLastApplyTime(applyTimeInfo);

			// cpu 정보 구성
			OBDtoRptSysInfo cpuInfo = new OBDtoRptSysInfo();
			contents = "";
			cpuInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_CPUINFO));// OBTerminologyDB.RPT_BASIC_CPUINFO);
			if (rptOPSysInfo != null) {
				cpuInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				ArrayList<DtoRptCpu> cpuList = rptOPSysInfo.getCpuList();
				long cpuUsage = 0;
				int spIndex = 1;
				for (DtoRptCpu cpu : cpuList) {
					cpuUsage += cpu.getUsage();
					if (cpu.getType() == OBDefine.SYS_CPU_TYPE_MP) {
						if (!contents.isEmpty())
							contents += ", ";
						contents += String.format("%s: %d", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_MPCPU),
								cpu.getUsage());
					} else {
						if (!contents.isEmpty())
							contents += ", ";
						contents += String.format("%s[%d]: %d", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_SPCPU),
								spIndex, cpu.getUsage());
						spIndex++;
					}
				}
				String avgContent = String.format("%s: %d \n",
						OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_AVG), cpuUsage / cpuList.size());
				contents = avgContent + contents;
				cpuInfo.setContents(contents);
			}
			output.setCpuInfo(cpuInfo);

			// memory 정보 구성
			OBDtoRptSysInfo memInfo = new OBDtoRptSysInfo();
			contents = "";
			memInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_MEMINFO));// OBTerminologyDB.RPT_BASIC_MEMINFO);
			if (rptOPSysInfo != null) {
				memInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				ArrayList<DtoRptMem> memList = rptOPSysInfo.getMemList();
				long memUsage = 0;
				for (DtoRptMem mem : memList) {
					memUsage += (mem.getUsed() * 100 / mem.getTotal());
				}
				contents = String.format("%s: %d \n", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_AVG),
						memUsage / memList.size());
				memInfo.setContents(contents);
			}
			output.setMemoryInfo(memInfo);

			// power 정보 구성
			OBDtoRptSysInfo powerInfo = new OBDtoRptSysInfo();
			contents = "";
			powerInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_POWERINFO));// OBTerminologyDB.RPT_BASIC_POWERINFO);
//			powerInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));//OBTerminologyDB.TYPE_GENERAL_NORMAL);
			if (rptOPSysInfo != null) {
				if (rptOPSysInfo.getPowerSupplyStatus() == OBDefine.SYS_POWERSUPPLY_STATUS_OK)
					powerInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				else
					powerInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
				powerInfo.setContents("");
//				rptOPSysInfo.getPowerSupplyStatus();
			}
			output.setPowerInfo(powerInfo);

			// fan 정보 구성.
			OBDtoRptSysInfo fanInfo = new OBDtoRptSysInfo();
			contents = "";
			fanInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_FANINFO));// OBTerminologyDB.RPT_BASIC_FANINFO);
			if (rptOPSysInfo != null) {
				if (rptOPSysInfo.getFanStatus() == OBDefine.SYS_FAN_STATUS_OK)
					fanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				else
					fanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
				fanInfo.setContents("");
			}
			output.setFanInfo(fanInfo);
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return output;
	}

	private OBDtoRptSysInfoL2 getSysInfoL2AlteonSnmp(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBDatabase db)
			throws OBException {// L2 info 추출.
		OBDtoRptSysInfoL2 output = new OBDtoRptSysInfoL2();
		String contents = "";
		try {
			OBSnmpAlteon snmp = new OBSnmpAlteon(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());

			DtoRptOPL2 rptOPL2Info = null;
			try {
				rptOPL2Info = snmp.getRptOPL2Info(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} catch (OBException e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get getRptOPL2Info.%s", e.getErrorMessage()));
			}
			// link up 정보.
			OBDtoRptSysInfo linkupInfo = new OBDtoRptSysInfo();
			contents = "";
			linkupInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_LINKUP));// OBTerminologyDB.RPT_L2_LINKUP);
			ArrayList<String> portNameList = new ArrayList<String>();
			if (rptOPL2Info != null) {
				ArrayList<DtoRptLinkStatus> linkupList = rptOPL2Info.getLinkupList();
				int linkupCnt = 0;
				int linkTotal = 0;
				for (DtoRptLinkStatus linkup : linkupList) {
					linkTotal++;
					if (linkup.getStatus() == OBDefine.L2_LINK_STATUS_UP) {
						linkupCnt++;
						if (!contents.isEmpty())
							contents += ", ";
						contents += String.format("%s", linkup.getName());
						portNameList.add(linkup.getName());
					}
				}
				linkupInfo.setResult(String.format("%d/%d %s", linkupCnt, linkTotal,
						OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_COUNT)));// OBTerminologyDB.TYPE_GENERAL_COUNT));
				linkupInfo.setContents(contents);
			}

			// port 상태 정보.
			OBDtoRptSysInfo portInfo = new OBDtoRptSysInfo();
			contents = "";
			portInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_PORTSTATUS));// OBTerminologyDB.RPT_L2_PORTSTATUS);
			if (rptOPL2Info != null) {
				ArrayList<DtoRptPortStatus> portStatusList = rptOPL2Info.getPortStatusList();
				HashMap<String, OBDtoRptPortInfo> oldPortStatsMap = getPortInfo(adcInfo, portNameList,
						rptInfo.getBeginTime());
				portInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				for (DtoRptPortStatus rptPortStatus : portStatusList) {
					if (rptPortStatus.getStatus() != OBDefine.L2_LINK_STATUS_UP)
						continue;
					long diffDiscards = 0;
					long diffErrors = 0;
					long currDiscards = rptPortStatus.getDiscardsIn() + rptPortStatus.getDiscardsOut();
					long currErrors = rptPortStatus.getErrorIn() + rptPortStatus.getErrorOut();
					OBDtoRptPortInfo oldPortStats = oldPortStatsMap.get(rptPortStatus.getName());
					if (oldPortStats.getErrDiscardsList() != null && oldPortStats.getErrDiscardsList().size() > 0) {
						diffDiscards = Math.abs(currDiscards - oldPortStats.getErrDiscardsList().get(0).getDiscards());
						diffErrors = Math.abs(currErrors - oldPortStats.getErrDiscardsList().get(0).getErrors());
					}
					contents += String.format("%-6s: %s = %d(%d)\n", rptPortStatus.getName(),
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DISCARDS), diffDiscards,
							currDiscards);
					contents += String.format("       : %s = %d(%d)\n",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ERRORS), diffErrors, currErrors);

					if (diffDiscards != 0 || diffErrors != 0)
						portInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
				}
				portInfo.setContents(contents);
			}

			// vlan 정보.
			OBDtoRptSysInfo vlanInfo = new OBDtoRptSysInfo();
			contents = "";
			vlanInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_VLANINFO));// OBTerminologyDB.RPT_L2_VLANINFO);
			if (rptOPL2Info != null) {
				vlanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				ArrayList<DtoRptVlanStatus> rptVlanStatusList = rptOPL2Info.getVlanList();
				for (DtoRptVlanStatus vlanStatus : rptVlanStatusList) {
					if (vlanStatus.getStatus() != OBDefine.L2_VLAN_STATE_ENABLED)
						continue;
					String portList = "";
					for (Integer portNum : vlanStatus.getPortList()) {
						if (!portList.isEmpty())
							portList += ", ";
						portList += String.format("%d", portNum);
					}
					if (!contents.isEmpty())
						contents += "\n";
					contents += String.format("%s: %d, %s: %s, %s:%s",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ID), vlanStatus.getIndex(),
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NAME), vlanStatus.getName(),
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_PORT), portList);
				}
				vlanInfo.setContents(contents);
			}

//			// stp 정보. 
//			OBDtoRptSysInfo stpInfo = new OBDtoRptSysInfo();
//			contents="";
//			stpInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_STPINFO));//OBTerminologyDB.RPT_L2_STPINFO);
//			if(rptOPL2Info!=null)
//			{
//				DtoRptStpStatus stpStatus = rptOPL2Info.getStpInfo();
//				if(stpStatus.getState()==OBDefine.L2_STP_STATE_ENABLED)
//				{
//					stpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));//OBTerminologyDB.TYPE_GENERAL_USED);
//					// blocking 포트 리스트만 제공.
//					for(DtoRptStgStatus stgStatus:stpStatus.getStgList())
//					{
//						if(stgStatus.getStatus()!=OBDefine.L2_STG_STATUS_BLOCKING)
//							continue;
//						if(!contents.isEmpty())
//							contents += ", ";
//						contents += stgStatus.getIndex();
//					}
//					if(!contents.isEmpty())
//						contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_BLOCKING) + ": " +  contents;
//				}
//				else
//					stpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));//OBTerminologyDB.TYPE_GENERAL_NOTUSED);
//				stpInfo.setContents(contents);
//			}
//			
//			// trunk 정보. 사용중인 trunk 정보만 제공.
//			OBDtoRptSysInfo trunkInfo = new OBDtoRptSysInfo();
//			contents="";
//			trunkInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_TRUNKINFO));//OBTerminologyDB.RPT_L2_STPINFO);
//			if(rptOPL2Info!=null)
//			{
//				trunkInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));//OBTerminologyDB.TYPE_GENERAL_USED);
//				ArrayList<DtoRptTrunkStatus> rptTrunkList = rptOPL2Info.getTrunkList();
//				for(DtoRptTrunkStatus status:rptTrunkList)
//				{
//					if(status.getStatus()!=OBDefine.L2_TRUNK_STATE_ENABLED)
//						continue;
//					if(!contents.isEmpty())
//						contents += ", ";
//					contents += status.getIndex();
//				}
//				if(!contents.isEmpty())
//					contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TRUNK) + ": " +  contents;
//				trunkInfo.setContents(contents);
//			}

			output.setPortNameList(portNameList);
			output.setLinkup(linkupInfo);
			output.setPortStatus(portInfo);
			output.setVlanInfo(vlanInfo);
//			output.setStpInfo(stpInfo);
//			output.setTrunkInfo(trunkInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return output;
	}

	private OBDtoRptSysInfoL2 getSysInfoL2AlteonCLI(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo,
			OBAdcAlteonHandler adcHandler, OBDatabase db) throws OBException {// L2 info 추출.
		OBDtoRptSysInfoL2 retVal = new OBDtoRptSysInfoL2();
		OBCLIParserAlteon parserClass = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
		String cfgDump = "";
		String contents = "";
		try {
			cfgDump = adcHandler.cmndInfoLink();
			ArrayList<OBDtoLinkInfoAlteon> linkList = parserClass.parseInfoLink(cfgDump);

			// link up 정보.
			OBDtoRptSysInfo linkupInfo = new OBDtoRptSysInfo();
			contents = "";
			linkupInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_LINKUP));// OBTerminologyDB.RPT_L2_LINKUP);
			ArrayList<String> portNameList = new ArrayList<String>();
			if (linkList != null) {
				int linkupCnt = 0;
				int linkTotal = 0;
				for (OBDtoLinkInfoAlteon linkup : linkList) {
					linkTotal++;
					if (linkup.getStatus() == OBDefine.L2_LINK_STATUS_UP) {
						linkupCnt++;
						if (!contents.isEmpty())
							contents += ", ";
//						contents += String.format("%s", linkup.getPortName());
//						portNameList.add(linkup.getPortName());
						contents += String.format("%s", linkup.getAliasName());
						portNameList.add(linkup.getAliasName());
					}
				}
				linkupInfo.setResult(String.format("%d/%d %s", linkupCnt, linkTotal,
						OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_COUNT)));// OBTerminologyDB.TYPE_GENERAL_COUNT));
				linkupInfo.setContents(contents);
			}

			// port 상태 정보.
			OBDtoRptSysInfo portInfo = new OBDtoRptSysInfo();
			contents = "";
			portInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_PORTSTATUS));// OBTerminologyDB.RPT_L2_PORTSTATUS);
			if (portNameList != null) {
				HashMap<String, OBDtoRptPortInfo> oldPortStatsMap = getPortInfo(adcInfo, portNameList,
						rptInfo.getBeginTime());
				portInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				for (String portName : portNameList) {
					if (!portName.equals("mgmt")) // mgmt의 경우 포트상태를 확인할 수 없다.
					{
						cfgDump = adcHandler.cmndStatPort(Integer.parseInt(portName));
						OBDtoStatLinkAlteon linkStat = parserClass.parseStateLink(portName, cfgDump);

						long diffDiscards = 0;
						long diffErrors = 0;
						long currDiscards = linkStat.getDiscards();
						long currErrors = linkStat.getErrors();
						OBDtoRptPortInfo oldPortStats = oldPortStatsMap.get(portName);
						if (oldPortStats.getErrDiscardsList() != null && oldPortStats.getErrDiscardsList().size() > 0) {
							diffDiscards = Math
									.abs(currDiscards - oldPortStats.getErrDiscardsList().get(0).getDiscards());
							diffErrors = Math.abs(currErrors - oldPortStats.getErrDiscardsList().get(0).getErrors());
						}
						contents += String.format("%-6s: %s = %d(%d)\n", portName,
								OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DISCARDS), diffDiscards,
								currDiscards);
						contents += String.format("       : %s = %d(%d)\n",
								OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ERRORS), diffErrors, currErrors);

						if (diffDiscards != 0 || diffErrors != 0)
							portInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
					}
				}
				portInfo.setContents(contents);
			}

			// vlan 정보.
			OBDtoRptSysInfo vlanInfo = new OBDtoRptSysInfo();
			contents = "";
			vlanInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_VLANINFO));// OBTerminologyDB.RPT_L2_VLANINFO);
			adcHandler.cmndInfoVlan();
			cfgDump = adcHandler.getCmndRetString();// 원본 데이터를 이용해서 파싱한다.
			ArrayList<OBDtoInfoVlanAlteon> vlanList = parserClass.parseInfoVlan(cfgDump);
			if (vlanList != null) {
				vlanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				for (OBDtoInfoVlanAlteon vlanStatus : vlanList) {
					if (vlanStatus.getStatus() != OBDefine.L2_VLAN_STATE_ENABLED)
						continue;
					String portList = vlanStatus.getPorts();
					if (!contents.isEmpty())
						contents += "\n";
					contents += String.format("%s: %d, %s: %s, %s:%s",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ID), vlanStatus.getVlanId(),
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NAME), vlanStatus.getName(),
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_PORT), portList);
				}
				vlanInfo.setContents(contents);
			}

//			// stp 정보. 
//			OBDtoRptSysInfo stpInfo = new OBDtoRptSysInfo();
//			contents="";
//			stpInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_STPINFO));//OBTerminologyDB.RPT_L2_STPINFO);
//			cfgDump = adcHandler.cmndInfoStg();
//			ArrayList<OBDtoInfoStgAlteon> stgList = parserClass.parseInfoStg(cfgDump);
//			stpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));//OBTerminologyDB.TYPE_GENERAL_NOTUSED);
//			if(stgList!=null && stgList.size()>0)
//			{
//				stpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));//OBTerminologyDB.TYPE_GENERAL_USED);
//				OBDtoInfoStgAlteon stpStatus = stgList.get(0);
////				DtoRptStpStatus stpStatus = rptOPL2Info.getStpInfo();
//				if(stpStatus.getState()==OBDefine.L2_STP_STATE_ENABLED)
//				{
//					stpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));//OBTerminologyDB.TYPE_GENERAL_USED);
//					// blocking 포트 리스트만 제공.
//					for(DtoRptStgStatus stgStatus:stpStatus.getStgList())
//					{
//						if(stgStatus.getStatus()!=OBDefine.L2_STG_STATUS_BLOCKING)
//							continue;
//						if(!contents.isEmpty())
//							contents += ", ";
//						contents += stgStatus.getIndex();
//					}
//					if(!contents.isEmpty())
//						contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_BLOCKING) + ": " +  contents;
//				}
//				else
//					stpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));//OBTerminologyDB.TYPE_GENERAL_NOTUSED);
//				stpInfo.setContents(contents);
//			}
//			
//			// trunk 정보. 사용중인 trunk 정보만 제공.
//			OBDtoRptSysInfo trunkInfo = new OBDtoRptSysInfo();
//			contents="";
//			trunkInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_TRUNKINFO));//OBTerminologyDB.RPT_L2_STPINFO);
//			cfgDump = adcHandler.cmndInfoTrunk();
//			ArrayList<Integer> trunkList = parserClass.parseInfoTrunk(cfgDump);
//			trunkInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));//OBTerminologyDB.TYPE_GENERAL_NOTUSED);
//			if(trunkList!=null && trunkList.size()>0)
//			{
//				trunkInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));//OBTerminologyDB.TYPE_GENERAL_USED);
////				ArrayList<DtoRptTrunkStatus> rptTrunkList = rptOPL2Info.getTrunkList();
////				for(DtoRptTrunkStatus status:rptTrunkList)
////				{
////					if(status.getStatus()!=OBDefine.L2_TRUNK_STATE_ENABLED)
////						continue;
////					if(!contents.isEmpty())
////						contents += ", ";
////					contents += status.getIndex();
////				}
////				if(!contents.isEmpty())
////					contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TRUNK) + ": " +  contents;
////				trunkInfo.setContents(contents);
//			}

			retVal.setPortNameList(portNameList);
			retVal.setLinkup(linkupInfo);
			retVal.setPortStatus(portInfo);
			retVal.setVlanInfo(vlanInfo);
//			retVal.setStpInfo(stpInfo);
//			retVal.setTrunkInfo(trunkInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	private OBDtoRptSysInfoL2 getSysInfoL2PAS(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBAdcPASHandler handler,
			OBDatabase db) throws OBException {// L2 info 추출.
		OBCLIParserPAS parserClass = new OBCLIParserPAS();
		OBDtoRptSysInfoL2 retVal = new OBDtoRptSysInfoL2();
		String contents = "";
		try {
//			OBSnmpAlteon snmp = new OBSnmpAlteon(adcInfo.getAdcIpAddress());
//			
//			DtoRptOPL2  rptOPL2Info = null;
			ArrayList<OBDtoPortInfoPAS> portUpdownList = null;
			try {
				String cfg = handler.cmndPortInfo();
				portUpdownList = parserClass.parsePortUpdown(cfg);
			} catch (OBException e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get getRptOPL2Info.%s", e.getErrorMessage()));
			}

			// link up 정보.
			OBDtoRptSysInfo linkupInfo = new OBDtoRptSysInfo();
			contents = "";
			linkupInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_LINKUP));// OBTerminologyDB.RPT_L2_LINKUP);
			ArrayList<String> portNameList = new ArrayList<String>();
			if (portUpdownList != null) {
				int linkupCnt = 0;
				int linkTotal = 0;
				for (OBDtoPortInfoPAS linkup : portUpdownList) {
					linkTotal++;
					if (linkup.getLinkStatus() == OBDefine.L2_LINK_STATUS_UP) {
						linkupCnt++;
						if (!contents.isEmpty())
							contents += ", ";
						contents += String.format("%s", linkup.getPortName());
						portNameList.add(linkup.getPortName());
					}
				}
				linkupInfo.setResult(String.format("%d/%d %s", linkupCnt, linkTotal,
						OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_COUNT)));// OBTerminologyDB.TYPE_GENERAL_COUNT));
				linkupInfo.setContents(contents);
			}
			retVal.setLinkup(linkupInfo);

			// port 상태 정보.
			OBDtoRptSysInfo portInfo = new OBDtoRptSysInfo();
			contents = "";
			portInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_PORTSTATUS));// OBTerminologyDB.RPT_L2_PORTSTATUS);
			if (portNameList != null) {
				HashMap<String, OBDtoRptPortInfo> oldPortStatsMap = getPortInfo(adcInfo, portNameList,
						rptInfo.getBeginTime());
				portInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				for (String portName : portNameList) {
					String dumpInfo = handler.cmndPortStatistics(portName);
					OBDtoPortStatPAS portStat = parserClass.parsePortStatistics(dumpInfo);

					long diffDiscards = 0;
//					long diffUndersizes = 0;
//					long diffOversizes = 0;
//					long diffCRCErrors = 0;
//					long diffFragments = 0;
//					long diffCollission = 0;
					long currDiscards = portStat.getRxDiscards() + portStat.getTxDiscards();
					long currUndersizes = portStat.getUndersize();
					long currOversizes = portStat.getOversize();
					long currCRCErrors = portStat.getCrcErrors();
					long currFragments = portStat.getFragments();
					long currCollisions = portStat.getCollisions();
					OBDtoRptPortInfo oldPortStats = oldPortStatsMap.get(portName);
					if (oldPortStats.getErrDiscardsList() != null && oldPortStats.getErrDiscardsList().size() > 0) {
						diffDiscards = Math.abs(currDiscards - oldPortStats.getErrDiscardsList().get(0).getDiscards());
					}
					contents += String.format("%-6s: %s = %d(%d)\n", portName,
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DISCARDS), diffDiscards,
							currDiscards);
					contents += String.format("       : %s = %d\n",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_UNDERSIZE), currUndersizes);
					contents += String.format("       : %s = %d\n",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_OVERSIZE), currOversizes);
					contents += String.format("       : %s = %d\n",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_COLLISIONS), currCollisions);
					contents += String.format("       : %s = %d\n",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_CRCERRORS), currCRCErrors);
					contents += String.format("       : %s = %d\n",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_FRAGMENTS), currFragments);

					if (diffDiscards != 0)
						portInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
				}
				portInfo.setContents(contents);
			}
			retVal.setPortStatus(portInfo);

			// vlan 정보.
			String infoDump = handler.cmndVlanInfo();
			ArrayList<OBDtoVLanInfoPAS> vlanList = new OBCLIParserPAS().parseVlanInfo(infoDump);

			OBDtoRptSysInfo vlanInfo = new OBDtoRptSysInfo();
			contents = "";
			vlanInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_VLANINFO));// OBTerminologyDB.RPT_L2_VLANINFO);
			if (vlanList != null) {
				vlanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				for (OBDtoVLanInfoPAS vlan : vlanList) {
					if (vlan.getStatus() != OBDefine.L2_VLAN_STATE_ENABLED)
						continue;
					String portListTag = "";
					for (String portNum : vlan.getTaggedPortList()) {
						if (!portListTag.isEmpty())
							portListTag += ", ";
						portListTag += String.format("%s", portNum);
					}
					String portListUntag = "";
					for (String portNum : vlan.getUntaggedPortList()) {
						if (!portListUntag.isEmpty())
							portListUntag += ", ";
						portListUntag += String.format("%s", portNum);
					}
					String portListUnavaliTag = "";
					for (String portNum : vlan.getUnavailabledPortList()) {
						if (!portListUnavaliTag.isEmpty())
							portListUnavaliTag += ", ";
						portListUnavaliTag += String.format("%s", portNum);
					}
					if (!contents.isEmpty())
						contents += "\n";

					String portList = "";
					if (!portListTag.isEmpty()) {
						if (!portList.isEmpty())
							portList += ", ";
						portList += String.format("%s:%s",
								OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TAGPORT), portListTag);
					}
					if (!portListUntag.isEmpty()) {
						if (!portList.isEmpty())
							portList += ", ";
						portList += String.format("%s:%s",
								OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_UNTAGPORT), portListUntag);
					}
					if (!portListUnavaliTag.isEmpty()) {
						if (!portList.isEmpty())
							portList += ", ";
						portList += String.format("%s:%s",
								OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_UNAVAILPORT), portListUnavaliTag);
					}
					contents += String.format("%s: %d, %s: %s, %s",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ID), vlan.getId(),
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NAME), vlan.getName(), portList);
				}
				vlanInfo.setContents(contents);
			}
			retVal.setVlanInfo(vlanInfo);
			// stp 정보.
//			OBDtoRptSysInfo stpInfo = new OBDtoRptSysInfo();
//			contents="";
//			stpInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_STPINFO));//OBTerminologyDB.RPT_L2_STPINFO);
//			if(rptOPL2Info!=null)
//			{
//				DtoRptStpStatus stpStatus = rptOPL2Info.getStpInfo();
//				if(stpStatus.getState()==OBDefine.L2_STP_STATE_ENABLED)
//				{
//					stpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));//OBTerminologyDB.TYPE_GENERAL_USED);
//					// blocking 포트 리스트만 제공.
//					for(DtoRptStgStatus stgStatus:stpStatus.getStgList())
//					{
//						if(stgStatus.getStatus()!=OBDefine.L2_STG_STATUS_BLOCKING)
//							continue;
//						if(!contents.isEmpty())
//							contents += ", ";
//						contents += stgStatus.getIndex();
//					}
//					if(!contents.isEmpty())
//						contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_BLOCKING) + ": " +  contents;
//				}
//				else
//					stpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));//OBTerminologyDB.TYPE_GENERAL_NOTUSED);
//				stpInfo.setContents(contents);
//			}

			// trunk 정보. 사용중인 trunk 정보만 제공.
//			OBDtoRptSysInfo trunkInfo = new OBDtoRptSysInfo();
//			contents="";
//			trunkInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_TRUNKINFO));//OBTerminologyDB.RPT_L2_STPINFO);
//			if(rptOPL2Info!=null)
//			{
//				trunkInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));//OBTerminologyDB.TYPE_GENERAL_USED);
//				ArrayList<DtoRptTrunkStatus> rptTrunkList = rptOPL2Info.getTrunkList();
//				for(DtoRptTrunkStatus status:rptTrunkList)
//				{
//					if(status.getStatus()!=OBDefine.L2_TRUNK_STATE_ENABLED)
//						continue;
//					if(!contents.isEmpty())
//						contents += ", ";
//					contents += status.getIndex();
//				}
//				if(!contents.isEmpty())
//					contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TRUNK) + ": " +  contents;
//				trunkInfo.setContents(contents);
//			}

			retVal.setPortNameList(portNameList);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	private OBDtoRptSysInfoL2 getSysInfoL2PASK(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBAdcPASKHandler handler,
			OBDatabase db) throws OBException {// L2 info 추출.
		OBCLIParserPASK parserClass = new OBCLIParserPASK();
		OBDtoRptSysInfoL2 retVal = new OBDtoRptSysInfoL2();
		String contents = "";
		try {
			ArrayList<OBDtoPortInfoPASK> portUpdownList = null;
			ArrayList<DtoRptTrunkInfoPASK> trunkList = null;
			DtoRptStpInfoPASK stpInfoPASK = null;
			try {
				String cfg = handler.cmndPortInfo();
				portUpdownList = parserClass.parsePortUpdown(cfg);
				cfg = handler.cmndShowTrunkInfo();
				trunkList = parserClass.parseTrunkInfo(cfg);
				cfg = handler.cmndShowStpInfo();
				stpInfoPASK = parserClass.parseStpInfo(cfg);
			} catch (OBException e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get getRptOPL2Info.%s", e.getErrorMessage()));
			}

			// link up 정보.
			OBDtoRptSysInfo linkupInfo = new OBDtoRptSysInfo();
			contents = "";
			linkupInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_LINKUP));// OBTerminologyDB.RPT_L2_LINKUP);
			HashMap<String, String> portNameMap = new HashMap<String, String>();
			if (portUpdownList != null) {
				int linkupCnt = 0;
				int linkTotal = 0;
				for (OBDtoPortInfoPASK linkup : portUpdownList) {
					linkTotal++;
					if (linkup.getLinkStatus() == OBDefine.L2_LINK_STATUS_UP) {
						linkupCnt++;
						if (!contents.isEmpty())
							contents += ", ";
						contents += String.format("%s", linkup.getPortName());
						portNameMap.put(linkup.getPortName(), linkup.getPortName());
					}
				}
				linkupInfo.setResult(String.format("%d/%d %s", linkupCnt, linkTotal,
						OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_COUNT)));// OBTerminologyDB.TYPE_GENERAL_COUNT));
				linkupInfo.setContents(contents);
			}
			retVal.setLinkup(linkupInfo);

			// port 상태 정보.
			OBDtoRptSysInfo portInfo = new OBDtoRptSysInfo();
			contents = "";
			portInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_PORTSTATUS));// OBTerminologyDB.RPT_L2_PORTSTATUS);
			if (portNameMap.size() > 0) {
				String dumpInfo = handler.cmndPortStatistics();
				ArrayList<OBDtoPortStatPASK> portStatList = parserClass.parsePortStatistics(dumpInfo);
				HashMap<String, OBDtoRptPortInfo> oldPortStatsMap = getPortInfo(adcInfo,
						new ArrayList<String>(portNameMap.values()), rptInfo.getBeginTime());
				portInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				for (OBDtoPortStatPASK portStat : portStatList) {
					if (portNameMap.get(portStat.getPortName()) == null)
						continue;
					long diffDiscards = 0;
					long diffErrors = 0;
					long currDiscards = portStat.getRxDiscards() + portStat.getTxDiscards();
					long currErrors = portStat.getRxErrros() + portStat.getTxErrros();
					OBDtoRptPortInfo oldPortStats = oldPortStatsMap.get(portStat.getPortName());
					if (oldPortStats != null && oldPortStats.getErrDiscardsList() != null
							&& oldPortStats.getErrDiscardsList().size() > 0) {
						diffDiscards = Math.abs(currDiscards - oldPortStats.getErrDiscardsList().get(0).getDiscards());
						diffErrors = Math.abs(currErrors - oldPortStats.getErrDiscardsList().get(0).getErrors());

					}
					contents += String.format("%-6s: %s = %d(%d)\n", portStat.getPortName(),
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DISCARDS), diffDiscards,
							currDiscards);
					contents += String.format("       : %s = %d(%d)\n",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ERRORS), diffErrors, currErrors);

					if (diffDiscards != 0)
						portInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
				}
				portInfo.setContents(contents);
			}
			retVal.setPortStatus(portInfo);

			// vlan 정보.
			String infoDump = handler.cmndVlanInfo();
			ArrayList<OBDtoVLanInfoPASK> vlanList = parserClass.parseVlanInfo(infoDump);

			OBDtoRptSysInfo vlanInfo = new OBDtoRptSysInfo();
			contents = "";
			vlanInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_VLANINFO));// OBTerminologyDB.RPT_L2_VLANINFO);
			if (vlanList != null) {
				vlanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				for (OBDtoVLanInfoPASK vlan : vlanList) {
					if (vlan.getStatus() != OBDefine.L2_VLAN_STATE_ENABLED)
						continue;
					String portListTag = "";
					for (String portNum : vlan.getTaggedPortList()) {
						if (!portListTag.isEmpty())
							portListTag += ", ";
						portListTag += String.format("%s", portNum);
					}
					String portListUntag = "";
					for (String portNum : vlan.getUntaggedPortList()) {
						if (!portListUntag.isEmpty())
							portListUntag += ", ";
						portListUntag += String.format("%s", portNum);
					}
					String portListUnavaliTag = "";
					for (String portNum : vlan.getUnavailabledPortList()) {
						if (!portListUnavaliTag.isEmpty())
							portListUnavaliTag += ", ";
						portListUnavaliTag += String.format("%s", portNum);
					}
					if (!contents.isEmpty())
						contents += "\n";

					String portList = "";
					if (!portListTag.isEmpty()) {
						if (!portList.isEmpty())
							portList += ", ";
						portList += String.format("%s:%s",
								OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TAGPORT), portListTag);
					}
					if (!portListUntag.isEmpty()) {
						if (!portList.isEmpty())
							portList += ", ";
						portList += String.format("%s:%s",
								OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_UNTAGPORT), portListUntag);
					}
					if (!portListUnavaliTag.isEmpty()) {
						if (!portList.isEmpty())
							portList += ", ";
						portList += String.format("%s:%s",
								OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_UNAVAILPORT), portListUnavaliTag);
					}
					contents += String.format("%s: %d, %s: %s, %s",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ID), vlan.getId(),
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NAME), vlan.getName(), portList);
				}
				vlanInfo.setContents(contents);
			}
			retVal.setVlanInfo(vlanInfo);
			// stp 정보.
			OBDtoRptSysInfo stpInfo = new OBDtoRptSysInfo();
			contents = "";
			stpInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_STPINFO));// OBTerminologyDB.RPT_L2_STPINFO);
			if (stpInfoPASK != null) {
				if (stpInfoPASK.getState() == OBDefine.L2_STP_STATE_ENABLED) {
					stpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
					contents = stpInfoPASK.getPortList();
//					// blocking 포트 리스트만 제공.
//					for(DtoRptStgStatus stgStatus:stpStatus.getStgList())
//					{
//						if(stgStatus.getStatus()!=OBDefine.L2_STG_STATUS_BLOCKING)
//							continue;
//						if(!contents.isEmpty())
//							contents += ", ";
//						contents += stpInfo.gestgStatus.getIndex();
//					}
//					if(!contents.isEmpty())
//						contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_BLOCKING) + ": " +  contents;
				} else
					stpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));// OBTerminologyDB.TYPE_GENERAL_NOTUSED);
				stpInfo.setContents(contents);
			}

			// trunk 정보. 사용중인 trunk 정보만 제공.
			OBDtoRptSysInfo trunkInfo = new OBDtoRptSysInfo();
			contents = "";
			trunkInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_TRUNKINFO));// OBTerminologyDB.RPT_L2_STPINFO);
			if (trunkList != null && trunkList.size() > 0) {
				trunkInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
				for (DtoRptTrunkInfoPASK objInfo : trunkList) {
					if (!contents.isEmpty())
						contents += "\n";
					contents += String.format("%s:%s, %s:%s, %s:%s",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TRUNK_NAME), objInfo.getName(),
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TRUNK_ALG), objInfo.getAlgorithm(),
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TRUNK_PORT), objInfo.getPortList());
				}
				if (!contents.isEmpty())
					contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TRUNK) + ": " + contents;
				trunkInfo.setContents(contents);
			}

			retVal.setPortNameList(new ArrayList<String>(portNameMap.values()));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	private OBDtoRptSysInfoL2 getSysInfoL2F5(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo) throws OBException {// L2 info
																												// 추출.
		OBDtoRptSysInfoL2 output = new OBDtoRptSysInfoL2();
		String contents = "";
		try {
			OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());// new OBSnmpF5(adcInfo.getAdcIpAddress());

			DtoRptOPL2 rptOPL2Info = null;
			try {
				rptOPL2Info = snmp.getRptOPL2Info(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} catch (OBException e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get getRptOPL2Info.%s", e.getErrorMessage()));
			}
			// link up 정보.
			OBDtoRptSysInfo linkupInfo = new OBDtoRptSysInfo();
			contents = "";
			linkupInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_LINKUP));// OBTerminologyDB.RPT_L2_LINKUP);
			ArrayList<String> portNameList = new ArrayList<String>();
			if (rptOPL2Info != null) {
				ArrayList<DtoRptLinkStatus> linkupList = rptOPL2Info.getLinkupList();
				int linkupCnt = 0;
				int linkTotal = 0;
				for (DtoRptLinkStatus linkup : linkupList) {
					linkTotal++;
					if (linkup.getStatus() == OBDefine.L2_LINK_STATUS_UP) {
						linkupCnt++;
						if (!contents.isEmpty())
							contents += ", ";
						contents += String.format("%s", linkup.getName());
						portNameList.add(linkup.getName());
					}
				}
				linkupInfo.setResult(String.format("%d/%d %s", linkupCnt, linkTotal,
						OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_COUNT)));// OBTerminologyDB.TYPE_GENERAL_COUNT));
				linkupInfo.setContents(contents);
			}

			// port 상태 정보.
			OBDtoRptSysInfo portInfo = new OBDtoRptSysInfo();
			contents = "";
			portInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_PORTSTATUS));// OBTerminologyDB.RPT_L2_PORTSTATUS);
			if (rptOPL2Info != null) {
				ArrayList<DtoRptPortStatus> portStatusList = rptOPL2Info.getPortStatusList();
				HashMap<String, OBDtoRptPortInfo> oldPortStatsMap = getPortInfo(adcInfo, portNameList,
						rptInfo.getBeginTime());
				portInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				for (DtoRptPortStatus rptPortStatus : portStatusList) {
					if (rptPortStatus.getStatus() != OBDefine.L2_LINK_STATUS_UP)
						continue;
					long diffDiscards = 0;
					long diffErrors = 0;
					long currDiscards = rptPortStatus.getDiscardsIn() + rptPortStatus.getDiscardsOut();
					long currErrors = rptPortStatus.getErrorIn() + rptPortStatus.getErrorOut();
					OBDtoRptPortInfo oldPortStats = oldPortStatsMap.get(rptPortStatus.getName());
					if (oldPortStats.getErrDiscardsList() != null && oldPortStats.getErrDiscardsList().size() > 0) {
						diffDiscards = Math.abs(currDiscards - oldPortStats.getErrDiscardsList().get(0).getDiscards());
						diffErrors = Math.abs(currErrors - oldPortStats.getErrDiscardsList().get(0).getErrors());
					}
					contents += String.format("%-6s: %s = %d(%d)\n", rptPortStatus.getName(),
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DISCARDS), diffDiscards,
							currDiscards);
					contents += String.format("       : %s = %d(%d)\n",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ERRORS), diffErrors, currErrors);

					if (diffDiscards != 0 || diffErrors != 0)
						portInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
				}
				portInfo.setContents(contents);
			}
			// vlan 정보.
			OBDtoRptSysInfo vlanInfo = new OBDtoRptSysInfo();
			contents = "";
			vlanInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_VLANINFO));// OBTerminologyDB.RPT_L2_VLANINFO);
			if (rptOPL2Info != null) {
				vlanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				ArrayList<DtoRptVlanStatus> rptVlanStatusList = rptOPL2Info.getVlanList();
				for (DtoRptVlanStatus vlanStatus : rptVlanStatusList) {
					if (vlanStatus.getStatus() != OBDefine.L2_VLAN_STATE_ENABLED)
						continue;
					String portList = "";
					for (String portNum : vlanStatus.getPortNameList()) {
						if (!portList.isEmpty())
							portList += ", ";
						portList += portNum;
					}
					if (!contents.isEmpty())
						contents += "\n";
					contents += String.format("%s: %d, %s: %s, %s:%s",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ID), vlanStatus.getIndex(),
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NAME), vlanStatus.getName(),
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_PORT), portList);
				}
				vlanInfo.setContents(contents);
			}

			// stp 정보.
			OBDtoRptSysInfo stpInfo = new OBDtoRptSysInfo();
			contents = "";
			stpInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_STPINFO));// OBTerminologyDB.RPT_L2_STPINFO);
			if (rptOPL2Info != null) {
				DtoRptStpStatus stpStatus = rptOPL2Info.getStpInfo();
				if (stpStatus.getState() == OBDefine.L2_STP_STATE_ENABLED) {
					stpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
					// blocking 포트 리스트만 제공.
					for (DtoRptStgStatus stgStatus : stpStatus.getStgList()) {
						if (stgStatus.getStatus() != OBDefine.L2_STG_STATUS_BLOCKING)
							continue;
						if (!contents.isEmpty())
							contents += ", ";
						contents += stgStatus.getIndex();
					}
					if (!contents.isEmpty())
						contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_BLOCKING) + ": " + contents;
				} else
					stpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));// OBTerminologyDB.TYPE_GENERAL_NOTUSED);
				stpInfo.setContents(contents);
			}

			// trunk 정보. 사용중인 trunk 정보만 제공.
			OBDtoRptSysInfo trunkInfo = new OBDtoRptSysInfo();
			contents = "";
			trunkInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_TRUNKINFO));// OBTerminologyDB.RPT_L2_STPINFO);
			if (rptOPL2Info != null) {
				trunkInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
				ArrayList<DtoRptTrunkStatus> rptTrunkList = rptOPL2Info.getTrunkList();
				for (DtoRptTrunkStatus status : rptTrunkList) {
					if (status.getStatus() != OBDefine.L2_TRUNK_STATE_ENABLED)
						continue;
					if (!contents.isEmpty())
						contents += ", ";
					contents += status.getIndex();
				}
				if (!contents.isEmpty())
					contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TRUNK) + ": " + contents;
				trunkInfo.setContents(contents);
			}

			output.setPortNameList(portNameList);
			output.setLinkup(linkupInfo);
			output.setPortStatus(portInfo);
			output.setVlanInfo(vlanInfo);
			output.setStpInfo(stpInfo);
			output.setTrunkInfo(trunkInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return output;
	}

	private String convertLinkStatus(int status, OBDatabase db) throws OBException {
		switch (status) {
		case OBDefine.L2_LINK_STATUS_UP:// link up
			return OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_UP);// OBTerminologyDB.TYPE_GENERAL_UP;
		case OBDefine.L2_LINK_STATUS_DOWN:// down
			return OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DOWN);// OBTerminologyDB.TYPE_GENERAL_DOWN;
		case OBDefine.L2_LINK_STATUS_DISABLED:// disabled
			return OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DISALBED);// OBTerminologyDB.TYPE_GENERAL_DISALBED;
		case OBDefine.L2_LINK_STATUS_INOPERATIVE:// inoperative
			return OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_INOPERATIVE);// OBTerminologyDB.TYPE_GENERAL_INOPERATIVE;
		default:
			return OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_INOPERATIVE);// OBTerminologyDB.TYPE_GENERAL_INOPERATIVE;
		}
	}

	private OBDtoRptSysInfoL3 getSysInfoL3AlteonSnmp(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBDatabase db)
			throws OBException {// L3 info 추출.
		OBDtoRptSysInfoL3 output = new OBDtoRptSysInfoL3();
		String contents = "";
		try {
			OBSnmpAlteon snmp = new OBSnmpAlteon(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());

			DtoRptOPL3 rptOPL3Info = null;
			try {
				rptOPL3Info = snmp.getRptOPL3Info(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} catch (OBException e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get getRptOPL3Info.%s", e.getErrorMessage()));
			}
			// interface 정보
			OBDtoRptSysInfo intInfo = new OBDtoRptSysInfo();
			contents = "";
			intInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L3_INTERFACE));// OBTerminologyDB.RPT_L3_INTERFACE);
			if (rptOPL3Info != null) {
				intInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				ArrayList<DtoRptL3Int> rptIntList = rptOPL3Info.getIntList();
				for (DtoRptL3Int rptInt : rptIntList) {
					if (rptInt.getStatus() != OBDefine.L2_LINK_STATUS_UP)
						intInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
					if (!contents.isEmpty())
						contents += "\n";
					// [1] IP: 192.168.100.1 Netmask: 255.255.255.0 Bcast:192.168.1.0 VLAN: 1,2 상태:
					// Up
					contents += "[" + rptInt.getIndex() + "]"
							+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_IPADDRESS) + ": " + rptInt.getAddr()
							+ " " + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NETMASK) + ": "
							+ rptInt.getNetmask() + " " + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_BCAST)
							+ ": " + rptInt.getBcastAddr() + " "
							+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_VLAN) + ": " + rptInt.getVlanIndex()
							+ " " + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_STATUS) + ": "
							+ convertLinkStatus(rptInt.getStatus(), db);
				}
				intInfo.setContents(contents);
			}

			// gateway 정보.
			OBDtoRptSysInfo gwInfo = new OBDtoRptSysInfo();
			contents = "";
			gwInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L3_GATEWAY));// OBTerminologyDB.RPT_L3_GATEWAY);
			if (rptOPL3Info != null) {
				gwInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				ArrayList<DtoRptL3Gateway> rptL3GwList = rptOPL3Info.getGwList();
				for (DtoRptL3Gateway gateway : rptL3GwList) {
					if (!contents.isEmpty())
						contents += "\n";
					contents += "[" + gateway.getIndex() + "]"
							+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_IPADDRESS) + ": "
							+ gateway.getAddr() + " " + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_VLAN)
							+ ": " + gateway.getVlanIndex() + " "
							+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_STATUS) + ": "
							+ convertLinkStatus(gateway.getStatus(), db);
				}
				gwInfo.setContents(contents);
			}

			output.setInterfaceInfo(intInfo);
			output.setGatewayInfo(gwInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return output;
	}

	private OBDtoRptSysInfoL3 getSysInfoL3AlteonCLI(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo,
			OBAdcAlteonHandler adcHandler, OBDatabase db) throws OBException {// L3 info 추출.
		OBDtoRptSysInfoL3 retVal = new OBDtoRptSysInfoL3();
		OBCLIParserAlteon parserClass = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
		String cfgDump = "";
		String contents = "";
		try {
			cfgDump = adcHandler.cmndInfoL3IP();
			OBDtoIPInfoAlteon ipInfo = parserClass.parseIPGateway(cfgDump);

			// interface 정보
			OBDtoRptSysInfo intInfo = new OBDtoRptSysInfo();
			contents = "";
			intInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L3_INTERFACE));// OBTerminologyDB.RPT_L3_INTERFACE);
			if (ipInfo != null && ipInfo.getIpList() != null && ipInfo.getIpList().size() > 0) {
				intInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				ArrayList<OBDtoIPAddrInfoAlteon> rptIntList = ipInfo.getIpList();
				for (OBDtoIPAddrInfoAlteon rptInt : rptIntList) {
					if (rptInt.getStatus() != OBDefine.L2_LINK_STATUS_UP)
						intInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
					if (!contents.isEmpty())
						contents += "\n";
					// [1] IP: 192.168.100.1 Netmask: 255.255.255.0 Bcast:192.168.1.0 VLAN: 1,2 상태:
					// Up
					contents += OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_IPADDRESS) + ": "
							+ rptInt.getIpAddr() + " " + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NETMASK)
							+ ": " + rptInt.getNetmask() + " "
							+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_BCAST) + ": " + rptInt.getBcast()
							+ " " + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_VLAN) + ": "
							+ rptInt.getVlan() + " " + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_STATUS)
							+ ": " + convertLinkStatus(rptInt.getStatus(), db);
				}
				intInfo.setContents(contents);
			}

			// gateway 정보.
			OBDtoRptSysInfo gwInfo = new OBDtoRptSysInfo();
			contents = "";
			gwInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L3_GATEWAY));// OBTerminologyDB.RPT_L3_GATEWAY);;
			if (ipInfo != null && ipInfo.getGatewayList() != null && ipInfo.getGatewayList().size() > 0) {
				gwInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				ArrayList<OBDtoGateWayInfoAlteon> rptL3GwList = ipInfo.getGatewayList();
				for (OBDtoGateWayInfoAlteon gateway : rptL3GwList) {
					if (!contents.isEmpty())
						contents += "\n";
					contents += OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_IPADDRESS) + ": "
							+ gateway.getIpAddr() + " " + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_VLAN)
							+ ": " + gateway.getVlan() + " "
							+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_STATUS) + ": "
							+ convertLinkStatus(gateway.getStatus(), db);
				}
				gwInfo.setContents(contents);
			}

			retVal.setInterfaceInfo(intInfo);
			retVal.setGatewayInfo(gwInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	private OBDtoRptSysInfoL3 getSysInfoL3PAS(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBAdcPASHandler handler,
			OBDatabase db) throws OBException {// L3 info 추출.
		OBCLIParserPAS parserClass = new OBCLIParserPAS();
		OBDtoRptSysInfoL3 retVal = new OBDtoRptSysInfoL3();
		String contents = "";
		String infoDump = "";
		try {
			infoDump = handler.cmndInterface();
			ArrayList<OBDtoInterfaceInfoPAS> interfaceList = parserClass.parseInterface(infoDump);
			// interface 정보
			OBDtoRptSysInfo intInfo = new OBDtoRptSysInfo();
			contents = "";
			intInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L3_INTERFACE));// OBTerminologyDB.RPT_L3_INTERFACE);
			if (interfaceList != null) {
				intInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				for (OBDtoInterfaceInfoPAS rptInt : interfaceList) {
					if (rptInt.getStatus() != OBDefine.L2_LINK_STATUS_UP)
						intInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
					if (!contents.isEmpty())
						contents += "\n";
					// [1] IP: 192.168.100.1 Netmask: 255.255.255.0 Bcast:192.168.1.0 VLAN: 1,2 상태:
					// Up
					contents += "[" + rptInt.getName() + "]"
							+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_IPADDRESS) + ": "
							+ rptInt.getIpAddr() + " " + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_MAC)
							+ ": " + rptInt.getMacAddr() + " "
							+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_STATUS) + ": "
							+ convertLinkStatus(rptInt.getStatus(), db);
				}
				intInfo.setContents(contents);
			}
			retVal.setInterfaceInfo(intInfo);

			// gateway 정보.
			infoDump = handler.cmndGateway();
			ArrayList<OBDtoGatewayInfoPAS> gatewayList = new OBCLIParserPAS().parseGateway(infoDump);
			OBDtoRptSysInfo gwInfo = new OBDtoRptSysInfo();
			contents = "";
			gwInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L3_GATEWAY));// OBTerminologyDB.RPT_L3_GATEWAY);;
			if (gatewayList != null) {
				gwInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				for (OBDtoGatewayInfoPAS gateway : gatewayList) {
					if (!contents.isEmpty())
						contents += "\n";
					contents += OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DESTINATION) + ": "
							+ gateway.getDestination() + " "
							+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_GATEWAY) + ": "
							+ gateway.getGateway() + " "
							+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_INTERFACE) + ": "
							+ gateway.getInterfaceName();
				}
				gwInfo.setContents(contents);
			}

			retVal.setInterfaceInfo(intInfo);
			retVal.setGatewayInfo(gwInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
//		System.out.println(retVal);
		return retVal;
	}

	private OBDtoRptSysInfoL3 getSysInfoL3PASK(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBAdcPASKHandler handler,
			OBDatabase db) throws OBException {// L3 info 추출.
		OBCLIParserPASK parserClass = new OBCLIParserPASK();
		OBDtoRptSysInfoL3 retVal = new OBDtoRptSysInfoL3();
		String contents = "";
		String infoDump = "";
		try {
			infoDump = handler.cmndInterface();
			ArrayList<OBDtoInterfaceInfoPASK> interfaceList = parserClass.parseInterface(infoDump);
			// interface 정보
			OBDtoRptSysInfo intInfo = new OBDtoRptSysInfo();
			contents = "";
			intInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L3_INTERFACE));// OBTerminologyDB.RPT_L3_INTERFACE);
			if (interfaceList != null) {
				intInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				for (OBDtoInterfaceInfoPASK rptInt : interfaceList) {
					if (rptInt.getStatus() != OBDefine.L2_LINK_STATUS_UP)
						intInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
					if (!contents.isEmpty())
						contents += "\n";
					// [1] IP: 192.168.100.1 Netmask: 255.255.255.0 Bcast:192.168.1.0 VLAN: 1,2 상태:
					// Up
					contents += "[" + rptInt.getName() + "]"
							+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_IPADDRESS) + ": "
							+ rptInt.getIpAddr() + " " + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_MAC)
							+ ": " + rptInt.getMacAddr() + " "
							+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_STATUS) + ": "
							+ convertLinkStatus(rptInt.getStatus(), db);
				}
				intInfo.setContents(contents);
			}
			retVal.setInterfaceInfo(intInfo);

			// gateway 정보.
			infoDump = handler.cmndGateway();
			ArrayList<OBDtoGatewayInfoPAS> gatewayList = new OBCLIParserPAS().parseGateway(infoDump);
			OBDtoRptSysInfo gwInfo = new OBDtoRptSysInfo();
			contents = "";
			gwInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L3_GATEWAY));// OBTerminologyDB.RPT_L3_GATEWAY);;
			if (gatewayList != null) {
				gwInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				for (OBDtoGatewayInfoPAS gateway : gatewayList) {
					if (!contents.isEmpty())
						contents += "\n";
					contents += OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DESTINATION) + ": "
							+ gateway.getDestination() + " "
							+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_GATEWAY) + ": "
							+ gateway.getGateway() + " "
							+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_INTERFACE) + ": "
							+ gateway.getInterfaceName();
				}
				gwInfo.setContents(contents);
			}

			retVal.setInterfaceInfo(intInfo);
			retVal.setGatewayInfo(gwInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	private OBDtoRptSysInfoL3 getSysInfoL3F5(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBDatabase db)
			throws OBException {// L3 info 추출
		OBDtoRptSysInfoL3 output = new OBDtoRptSysInfoL3();
		String contents = "";
		try {
			OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());// new OBSnmpF5(adcInfo.getAdcIpAddress());

			DtoRptOPL3 rptOPL3Info = null;
			try {
				rptOPL3Info = snmp.getRptOPL3Info(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} catch (OBException e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get getRptOPL3Info.%s", e.getErrorMessage()));
			}

			// interface 정보
			OBDtoRptSysInfo intInfo = new OBDtoRptSysInfo();
			contents = "";
			intInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L3_INTERFACE));// OBTerminologyDB.RPT_L3_INTERFACE);
			int iIndex = 1;
			if (rptOPL3Info != null) {
				intInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				ArrayList<DtoRptL3Int> rptIntList = rptOPL3Info.getIntList();
				for (DtoRptL3Int rptInt : rptIntList) {
					// 임시대응:보고서 인터페이스 모두 비정상 나오는 현상 방지. 차후 보완 필요
//					if(rptInt.getStatus()!=OBDefine.L2_LINK_STATUS_UP)
//						intInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));//OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
					if (!contents.isEmpty())
						contents += "\n";
					// [1] IP: 192.168.100.1 Netmask: 255.255.255.0 Bcast:192.168.1.0 VLAN: 1,2 상태:
					// Up
					contents += "[" + iIndex++ + "]" + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_IPADDRESS)
							+ ": " + rptInt.getAddr() + " "
							+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NETMASK) + ": "
							+ rptInt.getNetmask() + " "
							// + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_BCAST)
							// + ": "
							// + rptInt.getBcastAddr()
							// + " "
							+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_VLAN) + ": "
							+ rptInt.getVlanIndexName();
					// + " "
					// + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_STATUS)
					// + ": "
					// + convertLinkStatus(rptInt.getStatus(), db);
				}
				intInfo.setContents(contents);
			}

			// gateway 정보.
			OBDtoRptSysInfo gwInfo = new OBDtoRptSysInfo();
			contents = "";
			gwInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L3_GATEWAY));// OBTerminologyDB.RPT_L3_GATEWAY);;
			if (rptOPL3Info != null) {
				gwInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				ArrayList<DtoRptL3Gateway> rptL3GwList = rptOPL3Info.getGwList();
				iIndex = 1;
				for (DtoRptL3Gateway gateway : rptL3GwList) {
					if (!contents.isEmpty())
						contents += "\n";
					contents += "[" + iIndex++ + "]" + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_IPADDRESS)
							+ ": " + gateway.getAddr();
				}
				gwInfo.setContents(contents);
			}
			output.setInterfaceInfo(intInfo);
			output.setGatewayInfo(gwInfo);
		}
//		catch(OBException e)
//		{
//			throw e;
//		}
		catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return output;
	}

	private OBDtoRptSysInfoL4 getSysInfoL4AlteonSnmp(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBDatabase db)
			throws OBException {// L4 info 추출.
		OBDtoRptSysInfoL4 output = new OBDtoRptSysInfoL4();
		String contents = "";
		String result = "";
		try {
			OBSnmpAlteon snmp = new OBSnmpAlteon(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());

			DtoRptOPL4 rptOPL4Info = null;
			try {
				rptOPL4Info = snmp.getRptOPL4Info(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} catch (OBException e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get getRptOPL4Info.%s", e.getErrorMessage()));
			}
			// pool member 상태 정보
			OBDtoRptSysInfo poolMemInfo = new OBDtoRptSysInfo();
			contents = "";
			result = "";
			poolMemInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_PMSTAT));// OBTerminologyDB.RPT_L4_PMSTAT);
			if (rptOPL4Info != null) {
				ArrayList<DtoRptPMStatus> pmList = rptOPL4Info.getPmList();
				long pmTotal = pmList.size();
				long pmAbnormal = 0;
				String tmpContent = "";
				HashMap<String, String> pmMap = new HashMap<String, String>();
				for (DtoRptPMStatus pm : pmList) {
					if (pm.getStatus() == OBDefine.STATUS_AVAILABLE)
						continue;
					if (pmMap.get(pm.getAddr()) == null)
						pmMap.put(pm.getAddr(), pm.getAddr());

//					if(!tmpContent.isEmpty())
//						tmpContent += ", ";
//					tmpContent += pm.getAddr();

				}
				ArrayList<String> pmAddrList = new ArrayList<String>(pmMap.values());
				for (String ipAddr : pmAddrList) {
					if (!tmpContent.isEmpty())
						tmpContent += ", ";
					tmpContent += ipAddr;
					pmAbnormal++;
				}

				result = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL) + ": " + pmTotal + "\n"
						+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + ": " + pmAbnormal;
				poolMemInfo.setResult(result);
				contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + "(" + pmAbnormal + ")"
						+ ": " + tmpContent;
				poolMemInfo.setContents(contents);
			}

			// vs 상태 정보.
			OBDtoRptSysInfo vsInfo = new OBDtoRptSysInfo();
			contents = "";
			result = "";
			vsInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_VSSTAT));// OBTerminologyDB.RPT_L4_VSSTAT);
			if (rptOPL4Info != null) {
				ArrayList<DtoRptVSStatus> vsList = rptOPL4Info.getVsList();
				long vsTotal = vsList.size();
				long vsAbnormal = 0;
				String tmpContent = "";
				HashMap<String, String> vsMap = new HashMap<String, String>();
				for (DtoRptVSStatus vs : vsList) {
					if (vs.getStatus() == OBDefine.STATUS_AVAILABLE)
						continue;
					if (vsMap.get(vs.getAddr()) == null)
						vsMap.put(vs.getAddr(), vs.getAddr());

//					if(!tmpContent.isEmpty())
//						tmpContent += ", ";
//					tmpContent += vs.getAddr();
				}
				ArrayList<String> vsAddrList = new ArrayList<String>(vsMap.values());
				for (String ipAddr : vsAddrList) {
					if (!tmpContent.isEmpty())
						tmpContent += ", ";
					vsAbnormal++;
					tmpContent += ipAddr;
				}
				result = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL) + ": " + vsTotal + "\n"
						+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + ": " + vsAbnormal;
				vsInfo.setResult(result);
				contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + "(" + vsAbnormal + ")"
						+ ": " + tmpContent;
				vsInfo.setContents(contents);
			}

			// connection 상태 정보
			OBDtoRptSysInfo connInfo = new OBDtoRptSysInfo();
			contents = "";
			connInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_CONNECTION));// OBTerminologyDB.RPT_L4_CONNECTION);
			if (rptOPL4Info != null) {
				connInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				DtoRptConnStatus connStatus = rptOPL4Info.getConnStatus();
				long usage = 0;
				if (connStatus.getMaxConn() > 0)
					usage = connStatus.getCurConn() * 100 / connStatus.getMaxConn();
				contents += OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_MAX) + ": " + connStatus.getMaxConn()
						+ "\n" + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_CURRENT) + ": "
						+ connStatus.getCurConn() + "\n" + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USAGE)
						+ ": " + usage + " %";
				connInfo.setContents(contents);
			}

			// direct 기능 상태 정보.
			OBDtoRptSysInfo directInfo = new OBDtoRptSysInfo();
			contents = "";
			directInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_DIRECT));// OBTerminologyDB.RPT_L4_DIRECT);
			if (rptOPL4Info != null) {
				if (rptOPL4Info.getDirectMode() == OBDefine.L4_DIRECT_ENABLED)
					directInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
				else
					directInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));// OBTerminologyDB.TYPE_GENERAL_NOTUSED);
				directInfo.setContents(contents);
			}

			output.setPoolMemberStatus(poolMemInfo);
			output.setVsStatus(vsInfo);
			output.setConntatus(connInfo);
			output.setDirectStatus(directInfo);
		}
//		catch(OBException e)
//		{
//			throw e;
//		}
		catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return output;
	}

	private OBDtoRptSysInfoL4 getSysInfoL4AlteonCLI(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo,
			OBAdcAlteonHandler adcHandler, OBDatabase db) throws OBException {// L4 info 추출.
		OBDtoRptSysInfoL4 retVal = new OBDtoRptSysInfoL4();
		OBCLIParserAlteon parserClass = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
		String cfgDump = "";
		String contents = "";
		String result = "";
		try {
			cfgDump = adcHandler.cmndDumpInfoSlb();
			ArrayList<OBDtoVSvcStatusAlteon> vsList = parserClass.parseInfoSlbDump(cfgDump);

			// pool member 상태 정보
			OBDtoRptSysInfo poolMemInfo = new OBDtoRptSysInfo();
			result = "";
			poolMemInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_PMSTAT));// OBTerminologyDB.RPT_L4_PMSTAT);
			OBDtoRptSysInfo vsInfo = new OBDtoRptSysInfo();
			vsInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_VSSTAT));// OBTerminologyDB.RPT_L4_VSSTAT);
			if (vsList != null && vsList.size() > 0) {
				int vscvCount = 0;
				int vscvFail = 0;
				int poolMemCount = 0;
				int poolFail = 0;
				String vscvResult = "";
				String poolMemResult = "";
				for (OBDtoVSvcStatusAlteon vscvObj : vsList) {
					vscvCount++;
					if (vscvObj.getStatus() != OBDefine.STATUS_AVAILABLE) {
						if (!vscvResult.isEmpty())
							vscvResult += ", ";
						vscvResult += vscvObj.getVsIP();
						vscvFail++;
					}
					ArrayList<OBDtoRSrvStatusAlteon> realList = vscvObj.getRealSrvStatusList();
					for (OBDtoRSrvStatusAlteon realObj : realList) {
						poolMemCount++;
						if (realObj.getStatus() != OBDefine.STATUS_AVAILABLE) {
							if (!poolMemResult.isEmpty())
								poolMemResult += ", ";
							poolMemResult += realObj.getRealIP();
							poolFail++;
						}
					}
				}

				// pool member 정보.
				result = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL) + ": " + poolMemCount + "\n"
						+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + ": " + poolFail;
				poolMemInfo.setResult(result);
				contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + "(" + poolFail + ")" + ": "
						+ poolMemResult;
				poolMemInfo.setContents(contents);

				// vservice 정보.
				result = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL) + ": " + vscvCount + "\n"
						+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + ": " + vscvFail;
				vsInfo.setResult(result);
				contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + "(" + vscvFail + ")" + ": "
						+ vscvResult;
				vsInfo.setContents(contents);
			}

			// connection 상태 정보
			OBDtoRptSysInfo connInfo = new OBDtoRptSysInfo();
			contents = "";
			connInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_CONNECTION));// OBTerminologyDB.RPT_L4_CONNECTION);
			cfgDump = adcHandler.cmndStatSlbDump();//
			OBDtoStatSlbSessionInfoAlteon statInfo = parserClass.parseStatSlbDump(cfgDump);
			if (statInfo != null) {
				connInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_MAX) + ": " + statInfo.getMaxSession()
						+ "\n" + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_CURRENT) + ": "
						+ statInfo.getSec4Session() + "\n"
						+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USAGE) + ": "
						+ statInfo.getSessionUsage() + " %";
				connInfo.setContents(contents);
			}

			// direct 기능 상태 정보.
//			OBDtoRptSysInfo directInfo = new OBDtoRptSysInfo();
//			contents="";
//			directInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_DIRECT));//OBTerminologyDB.RPT_L4_DIRECT);
//			if(rptOPL4Info!=null)
//			{
//				if(rptOPL4Info.getDirectMode()==OBDefine.L4_DIRECT_ENABLED)
//					directInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));//OBTerminologyDB.TYPE_GENERAL_USED);
//				else
//					directInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));//OBTerminologyDB.TYPE_GENERAL_NOTUSED);
//				directInfo.setContents(contents);
//			}
//			
			retVal.setPoolMemberStatus(poolMemInfo);
			retVal.setVsStatus(vsInfo);
			retVal.setConntatus(connInfo);
//			retVal.setDirectStatus(directInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	private OBDtoRptSysInfoL4 getSysInfoL4PAS(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBAdcPASHandler handler,
			OBDatabase db) throws OBException {// L4 info 추출.
		OBCLIParserPAS parserClass = new OBCLIParserPAS();
		OBDtoRptSysInfoL4 retVal = new OBDtoRptSysInfoL4();
		String contents = "";
		String result = "";
		String infoDump = "";
		try {
//			OBSnmpAlteon snmp = new OBSnmpAlteon(adcInfo.getAdcIpAddress());
//			
//			DtoRptOPL4  rptOPL4Info = null;
//			try
//			{
//				rptOPL4Info = snmp.getRptOPL4Info(adcInfo, db);
//			}
//			catch(OBException e)
//			{
//				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get getRptOPL4Info.%s", e.getErrorMessage()));
//			}
			infoDump = handler.cmndSlbStatus();
			ArrayList<OBDtoAdcVServerPAS> vsList = parserClass.parseSlbStatus(infoDump);

			// pool member 상태 정보
			OBDtoRptSysInfo poolMemInfo = new OBDtoRptSysInfo();
			contents = "";
			result = "";
			poolMemInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_PMSTAT));// OBTerminologyDB.RPT_L4_PMSTAT);
			if (vsList != null) {
//				ArrayList<DtoRptPMStatus> pmList = rptOPL4Info.getPmList();
				long pmTotal = 0;// pmList.size();
				long pmAbnormal = 0;
				String tmpContent = "";
//				HashMap<String, String> pmMap = new HashMap<String, String>();
//				for(DtoRptPMStatus pm:pmList)
//				{
//					if(pm.getStatus()==OBDefine.STATUS_AVAILABLE)
//						continue;
//					if(pmMap.get(pm.getAddr())==null)
//						pmMap.put(pm.getAddr(), pm.getAddr());
//				}
//				ArrayList<String> pmAddrList = new ArrayList<String>(pmMap.values());
				for (OBDtoAdcVServerPAS vsObj : vsList) {
					vsObj.setStatus(OBDefine.STATUS_AVAILABLE);
					if (vsObj.getPool() == null)
						continue;
					if (vsObj.getPool().getMemberList() == null)
						continue;
					for (OBDtoAdcPoolMemberPAS memObj : vsObj.getPool().getMemberList()) {
						pmTotal++;
						if (memObj.getStatus() != OBDefine.STATUS_AVAILABLE)
							pmAbnormal++;
						else
							vsObj.setStatus(OBDefine.STATUS_AVAILABLE);
						if (!tmpContent.isEmpty())
							tmpContent += ", ";
						tmpContent += memObj.getIpAddress();
					}
//					if(!tmpContent.isEmpty())
//						tmpContent += ", ";
//					tmpContent += ipAddr;
//					pmAbnormal++;
				}

				result = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL) + ": " + pmTotal + "\n"
						+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + ": " + pmAbnormal;
				poolMemInfo.setResult(result);
				contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + "(" + pmAbnormal + ")"
						+ ": " + tmpContent;
				poolMemInfo.setContents(contents);
			}
			retVal.setPoolMemberStatus(poolMemInfo);

			// vs 상태 정보.
			OBDtoRptSysInfo vsInfo = new OBDtoRptSysInfo();
			contents = "";
			result = "";
			vsInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_VSSTAT));// OBTerminologyDB.RPT_L4_VSSTAT);
			if (vsList != null) {
				long vsTotal = 0;// vsList.size();
				long vsAbnormal = 0;
				String tmpContent = "";
				for (OBDtoAdcVServerPAS vsObj : vsList) {
					vsTotal++;
					if (vsObj.getStatus() != OBDefine.STATUS_AVAILABLE)
						vsAbnormal++;
					if (!tmpContent.isEmpty())
						tmpContent += ", ";
					tmpContent += vsObj.getvIP();
				}
				result = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL) + ": " + vsTotal + "\n"
						+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + ": " + vsAbnormal;
				vsInfo.setResult(result);
				contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + "(" + vsAbnormal + ")"
						+ ": " + tmpContent;
				vsInfo.setContents(contents);
			}
			retVal.setVsStatus(vsInfo);

			// connection 상태 정보

			// direct 기능 상태 정보.
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	private OBDtoRptSysInfoL4 getSysInfoL4PASK(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBAdcPASKHandler handler,
			OBDatabase db) throws OBException {// L4 info 추출.
		OBCLIParserPASK parserClass = new OBCLIParserPASK();
		OBDtoRptSysInfoL4 retVal = new OBDtoRptSysInfoL4();
		String contents = "";
		String result = "";
		String infoDump = "";
		try {
			infoDump = handler.cmndShowInfoSlb();
			ArrayList<OBDtoAdcVServerPASK> vsList = parserClass.parseSlbStatus(adcInfo.getIndex(), infoDump);

			// pool member 상태 정보
			OBDtoRptSysInfo poolMemInfo = new OBDtoRptSysInfo();
			contents = "";
			result = "";
			poolMemInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_PMSTAT));// OBTerminologyDB.RPT_L4_PMSTAT);
			if (vsList != null) {
				long pmTotal = 0;// pmList.size();
				long pmAbnormal = 0;
				String tmpContent = "";
				for (OBDtoAdcVServerPASK vsObj : vsList) {
					vsObj.setStatus(OBDefine.STATUS_AVAILABLE);
					if (vsObj.getPool() == null)
						continue;
					if (vsObj.getPool().getMemberList() == null)
						continue;
					for (OBDtoAdcPoolMemberPASK memObj : vsObj.getPool().getMemberList()) {
						pmTotal++;
						if (memObj.getStatus() != OBDefine.STATUS_AVAILABLE)
							pmAbnormal++;
						else
							vsObj.setStatus(OBDefine.STATUS_AVAILABLE);
						if (!tmpContent.isEmpty())
							tmpContent += ", ";
						tmpContent += memObj.getIpAddress();
					}
				}

				result = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL) + ": " + pmTotal + "\n"
						+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + ": " + pmAbnormal;
				poolMemInfo.setResult(result);
				contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + "(" + pmAbnormal + ")"
						+ ": " + tmpContent;
				poolMemInfo.setContents(contents);
			}
			retVal.setPoolMemberStatus(poolMemInfo);

			// vs 상태 정보.
			OBDtoRptSysInfo vsInfo = new OBDtoRptSysInfo();
			contents = "";
			result = "";
			vsInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_VSSTAT));// OBTerminologyDB.RPT_L4_VSSTAT);
			if (vsList != null) {
				long vsTotal = 0;// vsList.size();
				long vsAbnormal = 0;
				String tmpContent = "";
				for (OBDtoAdcVServerPASK vsObj : vsList) {
					vsTotal++;
					if (vsObj.getStatus() != OBDefine.STATUS_AVAILABLE)
						vsAbnormal++;
					if (!tmpContent.isEmpty())
						tmpContent += ", ";
					tmpContent += vsObj.getvIP();
				}
				result = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL) + ": " + vsTotal + "\n"
						+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + ": " + vsAbnormal;
				vsInfo.setResult(result);
				contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + "(" + vsAbnormal + ")"
						+ ": " + tmpContent;
				vsInfo.setContents(contents);
			}
			retVal.setVsStatus(vsInfo);

			// connection 상태 정보

			// direct 기능 상태 정보.
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
//		System.out.println(retVal);
		return retVal;
	}

	private OBDtoRptSysInfoL4 getSysInfoL4F5(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBDatabase db)
			throws OBException {// L4 info 추출.
		OBDtoRptSysInfoL4 output = new OBDtoRptSysInfoL4();
		String contents = "";
		String result = "";
		try {
			OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());// new OBSnmpF5(adcInfo.getAdcIpAddress());

			DtoRptOPL4 rptOPL4Info = null;
			try {
				rptOPL4Info = snmp.getRptOPL4Info(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} catch (OBException e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get getRptOPL4Info.%s", e.getErrorMessage()));
			}
			// pool member 상태 정보
			OBDtoRptSysInfo poolMemInfo = new OBDtoRptSysInfo();
			contents = "";
			result = "";
			poolMemInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_PMSTAT));// OBTerminologyDB.RPT_L4_PMSTAT);
			if (rptOPL4Info != null) {
				ArrayList<DtoRptPMStatus> pmList = rptOPL4Info.getPmList();
				long pmTotal = pmList.size();
				long pmAbnormal = 0;
				String tmpContent = "";
				HashMap<String, String> pmMap = new HashMap<String, String>();
				for (DtoRptPMStatus pm : pmList) {
					if (pm.getStatus() == OBDefine.STATUS_AVAILABLE)
						continue;
					if (pmMap.get(pm.getAddr()) == null)
						pmMap.put(pm.getAddr(), pm.getAddr());

//					if(!tmpContent.isEmpty())
//						tmpContent += ", ";
//					tmpContent += pm.getAddr();
				}
				ArrayList<String> pmAddrList = new ArrayList<String>(pmMap.values());
				for (String ipAddr : pmAddrList) {
					if (!tmpContent.isEmpty())
						tmpContent += ", ";
					tmpContent += ipAddr;
					pmAbnormal++;
				}
				result = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL) + ": " + pmTotal + "\n"
						+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + ": " + pmAbnormal;
				poolMemInfo.setResult(result);
				contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + "(" + pmAbnormal + ")"
						+ ": " + tmpContent;
				poolMemInfo.setContents(contents);
			}

			// vs 상태 정보.
			OBDtoRptSysInfo vsInfo = new OBDtoRptSysInfo();
			contents = "";
			result = "";
			vsInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_VSSTAT));// OBTerminologyDB.RPT_L4_VSSTAT);
			if (rptOPL4Info != null) {
				ArrayList<DtoRptVSStatus> vsList = rptOPL4Info.getVsList();
				long vsTotal = vsList.size();
				long vsAbnormal = 0;
				String tmpContent = "";
				HashMap<String, String> vsMap = new HashMap<String, String>();
				for (DtoRptVSStatus vs : vsList) {
					if (vs.getStatus() == OBDefine.STATUS_AVAILABLE)
						continue;
					if (vsMap.get(vs.getAddr()) == null)
						vsMap.put(vs.getAddr(), vs.getAddr());
//					if(!tmpContent.isEmpty())
//						tmpContent += ", ";
//					tmpContent += vs.getAddr();
				}
				ArrayList<String> vsAddrList = new ArrayList<String>(vsMap.values());
				for (String ipAddr : vsAddrList) {
					if (!tmpContent.isEmpty())
						tmpContent += ", ";
					tmpContent += ipAddr;
					vsAbnormal++;
				}
				result = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL) + ": " + vsTotal + "\n"
						+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + ": " + vsAbnormal;
				vsInfo.setResult(result);
				contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + "(" + vsAbnormal + ")"
						+ ": " + tmpContent;
				vsInfo.setContents(contents);
			}

			// connection 상태 정보
			OBDtoRptSysInfo connInfo = new OBDtoRptSysInfo();
			contents = "";
			connInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_CONNECTION));// OBTerminologyDB.RPT_L4_CONNECTION);
			if (rptOPL4Info != null) {
				connInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
				DtoRptConnStatus connStatus = rptOPL4Info.getConnStatus();
				long usage = 0;
				if (connStatus.getMaxConn() > 0)
					usage = connStatus.getCurConn() * 100 / connStatus.getMaxConn();
				contents += OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_MAX) + ": " + connStatus.getMaxConn()
						+ "\n" + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_CURRENT) + ": "
						+ connStatus.getCurConn() + "\n" + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USAGE)
						+ ": " + usage + " %";
				connInfo.setContents(contents);
			}

			// direct 기능 상태 정보.
			OBDtoRptSysInfo directInfo = new OBDtoRptSysInfo();
			contents = "";
			directInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_DIRECT));// OBTerminologyDB.RPT_L4_DIRECT);
			if (rptOPL4Info != null) {
				if (rptOPL4Info.getDirectMode() == OBDefine.L4_DIRECT_ENABLED)
					directInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
				else
					directInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));// OBTerminologyDB.TYPE_GENERAL_NOTUSED);
				directInfo.setContents(contents);
			}

			output.setPoolMemberStatus(poolMemInfo);
			output.setVsStatus(vsInfo);
			output.setConntatus(connInfo);
			output.setDirectStatus(directInfo);
		}
//		catch(OBException e)
//		{
//			throw e;
//		}
		catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return output;
	}

	private OBDtoRptSysInfoL7 getSysInfoL7AlteonSnmp(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBDatabase db)
			throws OBException {// L7 info 추출.

		return null;
	}

	private OBDtoRptSysInfoL7 getSysInfoL7AlteonCLI(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo,
			OBAdcAlteonHandler adcHandler, OBDatabase db) throws OBException {// L7 info 추출.

		return null;
	}

	private OBDtoRptSysInfoL7 getSysInfoL7F5(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBDatabase db)
			throws OBException {// L7 info 추출.
		OBDtoRptSysInfoL7 output = new OBDtoRptSysInfoL7();
		String contents = "";
		String result = "";
		try {
			OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());// new OBSnmpF5(adcInfo.getAdcIpAddress());

			DtoRptOPL7 rptOPL7 = null;
			try {
				rptOPL7 = snmp.getRptOPL7Info(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} catch (OBException e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get getRptOPL7Info.%s", e.getErrorMessage()));
			}
			// irule 정보 구성
			OBDtoRptSysInfo iRuleInfo = new OBDtoRptSysInfo();
			contents = "";
			iRuleInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L7_IRULE));// OBTerminologyDB.RPT_L7_IRULE);
			if (rptOPL7 != null) {
				ArrayList<DtoRptOPL7iRule> iruleList = rptOPL7.getiRuleList();
				int totalRule = 0;
				int usedRule = 0;
				if (iruleList != null) {
					String usedInfo = "";
					String notUsedInfo = "";
					int usedIndex = 0;
					int notUsedIndex = 0;

					for (DtoRptOPL7iRule rule : iruleList) {
						totalRule++;
						if (rule.getStatus() == OBDefine.STATE_ENABLE) {
							usedRule++;
							if (!usedInfo.isEmpty())
								usedInfo += "\n";
							usedInfo += "[" + ++usedIndex + "]" + ": " + rule.getName() + ", "
									+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_VSNAME) + ": ";
							String vsInfo = "";
							for (OBDtoAdcVServerF5 vs : rule.getVsList()) {
								if (!vsInfo.isEmpty())
									vsInfo += ", ";
								vsInfo += vs.getName() + "(" + vs.getvIP() + ")";
							}
							usedInfo += vsInfo;
						} else {
							if (!notUsedInfo.isEmpty())
								notUsedInfo += "\n";
							notUsedInfo += "[" + ++notUsedIndex + "]" + ": " + rule.getName();
						}
					}
					if (!usedInfo.isEmpty())
						usedInfo += "\n";

					int notUsedRule = totalRule - usedRule;
					contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED) + ": " + usedRule + "\n"
							+ usedInfo + "\n\n" + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED) + ": "
							+ notUsedRule + "\n" + notUsedInfo;
				}
				result = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL) + ": " + totalRule + "\n"
						+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED) + ": " + usedRule;
				iRuleInfo.setResult(result);
				iRuleInfo.setContents(contents);
			}

			// oneconnect 구성
			OBDtoRptSysInfo oneconnectInfo = new OBDtoRptSysInfo();
			contents = "";
			oneconnectInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L7_ONECONNECT));// OBTerminologyDB.RPT_L7_ONECONNECT);
			if (rptOPL7 != null) {
				ArrayList<DtoRptOPL7iRule> oneConnectList = rptOPL7.getOneConnect();
				int totalOneconnect = 0;
				int usedOneconnect = 0;
				if (oneConnectList != null) {
					String usedInfo = "";
					int usedIndex = 0;

					for (DtoRptOPL7iRule one : oneConnectList) {
						totalOneconnect++;
						if (one.getStatus() == OBDefine.STATE_ENABLE) {
							usedOneconnect++;
							if (!usedInfo.isEmpty())
								usedInfo += "\n";
							usedInfo += "[" + ++usedIndex + "]" + ": " + one.getName() + ", "
									+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_VSNAME) + ": ";
							String vsInfo = "";
							for (OBDtoAdcVServerF5 vs : one.getVsList()) {
								if (!vsInfo.isEmpty())
									vsInfo += ", ";
								vsInfo += vs.getName() + "(" + vs.getvIP() + ")";
							}
							usedInfo += vsInfo;
						}
					}
					contents = usedInfo;
				}
				result = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL) + ": " + totalOneconnect + "\n"
						+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED) + ": " + usedOneconnect;
				oneconnectInfo.setResult(result);
				oneconnectInfo.setContents(contents);
			}

			// ramcache 구성
			OBDtoRptSysInfo ramcacheInfo = new OBDtoRptSysInfo();
			contents = "";
			ramcacheInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L7_RAMCACHE));// OBTerminologyDB.RPT_L7_RAMCACHE);
			if (rptOPL7 != null) {
				ArrayList<DtoRptOPL7iRule> ramcacheList = rptOPL7.getRamCache();
				int totalRamcache = 0;
				int usedRamcache = 0;
				if (ramcacheList != null) {
					String usedInfo = "";
					int usedIndex = 0;

					for (DtoRptOPL7iRule ramcache : ramcacheList) {
						totalRamcache++;
						if (ramcache.getStatus() == OBDefine.STATE_ENABLE) {
							usedRamcache++;
							if (!usedInfo.isEmpty())
								usedInfo += "\n";
							usedInfo += "[" + ++usedIndex + "]" + ": " + ramcache.getName() + ", "
									+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_VSNAME) + ": ";
							String vsInfo = "";
							for (OBDtoAdcVServerF5 vs : ramcache.getVsList()) {
								if (!vsInfo.isEmpty())
									vsInfo += ", ";
								vsInfo += vs.getName() + "(" + vs.getvIP() + ")";
							}
							usedInfo += vsInfo;
						}
					}
					contents = usedInfo;
				}
				result = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL) + ": " + totalRamcache + "\n"
						+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED) + ": " + usedRamcache;
				ramcacheInfo.setResult(result);
				ramcacheInfo.setContents(contents);
			}

			// compression 구성
			OBDtoRptSysInfo compressionInfo = new OBDtoRptSysInfo();
			contents = "";
			compressionInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L7_COMPRESSION));// OBTerminologyDB.RPT_L7_COMPRESSION);
			if (rptOPL7 != null) {
				ArrayList<DtoRptOPL7iRule> compressionList = rptOPL7.getCompression();
				int totalCompression = 0;
				int usedCompression = 0;
				if (compressionList != null) {
					String usedInfo = "";
					int usedIndex = 0;

					for (DtoRptOPL7iRule compression : compressionList) {
						totalCompression++;
						if (compression.getStatus() == OBDefine.STATE_ENABLE) {
							usedCompression++;
							if (!usedInfo.isEmpty())
								usedInfo += "\n";
							usedInfo += "[" + ++usedIndex + "]" + ": " + compression.getName() + ", "
									+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_VSNAME) + ": ";
							String vsInfo = "";
							for (OBDtoAdcVServerF5 vs : compression.getVsList()) {
								if (!vsInfo.isEmpty())
									vsInfo += ", ";
								vsInfo += vs.getName() + "(" + vs.getvIP() + ")";
							}
							usedInfo += vsInfo;
						}
					}
					contents = usedInfo;
				}
				result = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL) + ": " + totalCompression + "\n"
						+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED) + ": " + usedCompression;
				compressionInfo.setResult(result);
				compressionInfo.setContents(contents);
			}

			// ssl 가속 구성
			OBDtoRptSysInfo sslInfo = new OBDtoRptSysInfo();
			contents = "";
			sslInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L7_SSLACCEL));// OBTerminologyDB.RPT_L7_SSLACCEL);
			if (rptOPL7 != null) {
				ArrayList<DtoRptOPL7iRule> sslList = rptOPL7.getSslAccel();
				int totalSsl = 0;
				int usedSsl = 0;
				if (sslList != null) {
					String usedInfo = "";
					int usedIndex = 0;

					for (DtoRptOPL7iRule ssl : sslList) {
						totalSsl++;
						if (ssl.getStatus() == OBDefine.STATE_ENABLE) {
							usedSsl++;
							if (!usedInfo.isEmpty())
								usedInfo += "\n";
							usedInfo += "[" + ++usedIndex + "]" + ": " + ssl.getName() + ", "
									+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_VSNAME) + ": ";
							String vsInfo = "";
							for (OBDtoAdcVServerF5 vs : ssl.getVsList()) {
								if (!vsInfo.isEmpty())
									vsInfo += ", ";
								vsInfo += vs.getName() + "(" + vs.getvIP() + ")";
							}
							usedInfo += vsInfo;
						}
					}
					contents = usedInfo;
				}
				result = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL) + ": " + totalSsl + "\n"
						+ OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED) + ": " + usedSsl;
				sslInfo.setResult(result);
				sslInfo.setContents(contents);
			}

			output.setCompStatus(compressionInfo);
			output.setIruleStatus(iRuleInfo);
			output.setOneConnStatus(oneconnectInfo);
			output.setRamCacheStatus(ramcacheInfo);
			output.setSslStatus(sslInfo);
		}
//		catch(OBException e)
//		{
//			throw e;
//		}
		catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return output;
	}

	private OBDtoRptSysInfoEtc getSysInfoEtcAlteonSnmp(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBDatabase db)
			throws OBException {// etc info 추출.
		OBDtoRptSysInfoEtc output = new OBDtoRptSysInfoEtc();
		String contents = "";
//		String result = "";
		try {
			OBSnmpAlteon snmp = new OBSnmpAlteon(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());

			DtoRptOPEtc rptOPEtc = null;
			try {
				rptOPEtc = snmp.getRptOPEtcInfo(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} catch (OBException e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get getRptOPEtcInfo.%s", e.getErrorMessage()));
			}
			// log 정보 구성.
			OBDtoRptSysInfo logInfo = new OBDtoRptSysInfo();
			contents = "";
			logInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_LOGINFO));// OBTerminologyDB.RPT_ETC_LOGINFO);
			if (rptOPEtc != null) {
//				long logCnt = new OBAdcManagementImpl().getAdcAuditLogCount(adcInfo.getIndex(), "", rptInfo.getBeginTime(), rptInfo.getEndTime(), db);
				OBDtoAuditLogAdcSystem lastLog = new OBAdcManagementImpl().getAdcAuditLogLast(adcInfo.getIndex(), "",
						rptInfo.getBeginTime(), rptInfo.getEndTime(), db);
//				result = String.format("%s: %d", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL), logCnt);
//				logInfo.setResult(result);
				if (lastLog.getOccurTime() != null)
					contents = String.format("%s: %s\n",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_OCCURTIME), lastLog.getOccurTime());
				if (lastLog.getContents() != null)
					contents += String.format("%s: %s", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_CONTENT),
							lastLog.getContents());
				logInfo.setContents(contents);
			}
			// ntp 정보 구성.
			OBDtoRptSysInfo ntpInfo = new OBDtoRptSysInfo();
			contents = "";
			ntpInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTPINFO));// OBTerminologyDB.RPT_ETC_NTPINFO);
			if (rptOPEtc != null) {
				DtoRptNtp rptNtp = rptOPEtc.getNtpInfo();
				if (rptNtp.getEnabled() != null && rptNtp.getEnabled() == OBDefine.NTP_STATE_ENABLED)
					ntpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
				else
					ntpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));// OBTerminologyDB.TYPE_GENERAL_NOTUSED);
				contents = String.format("%s: %s\n", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_NAME),
						rptNtp.getName());
				contents += String.format("%s: %s\n", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_INTERVAL),
						rptNtp.getInterval());
				contents += String.format("%s: %s", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_TIMEZONE),
						rptNtp.getTimezone());
				ntpInfo.setContents(contents);
			}

			// syslog 정보 구성.
			OBDtoRptSysInfo syslogInfo = new OBDtoRptSysInfo();
			contents = "";
			syslogInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOGINFO));// OBTerminologyDB.RPT_ETC_SYSLOGINFO);
			if (rptOPEtc != null) {
				if (rptOPEtc.getSyslogList().size() >= 0)
					syslogInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
				else
					syslogInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));// OBTerminologyDB.TYPE_GENERAL_NOTUSED);
				for (int i = 0; i < rptOPEtc.getSyslogList().size(); i++) {
					DtoRptSyslog syslog = rptOPEtc.getSyslogList().get(i);
					if (i != 0)
						contents += "\n";
					contents += String.format("%d : %s, %s: %d, %s: %s", i + 1, syslog.getHost(),
							OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOG_SEVERITY), syslog.getSeverity() - 1,
							OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOG_FACILITY), syslog.getFacility());

				}
				syslogInfo.setContents(contents);
			}

			output.setLogInfo(logInfo);
			output.setNtpInfo(ntpInfo);
			output.setSyslogInf(syslogInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return output;
	}

	private OBDtoRptSysInfoEtc getSysInfoEtcAlteonCLI(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo,
			OBAdcAlteonHandler adcHandler, OBDatabase db) throws OBException {// etc info 추출.
		OBDtoRptSysInfoEtc retVal = new OBDtoRptSysInfoEtc();
		String contents = "";
		OBCLIParserAlteon parserClass = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
		String cfgDump = "";
		try {
			// log 정보 구성.
			cfgDump = adcHandler.cmndInfoLog();//
			ArrayList<OBDtoInfoLogsAlteon> logList = parserClass.parseInfoLogs(cfgDump);

			OBDtoRptSysInfo logInfo = new OBDtoRptSysInfo();
			contents = "";
			logInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_LOGINFO));// OBTerminologyDB.RPT_ETC_LOGINFO);
//			int lastIndex = logList.size() - 1;
			if (logList != null && logList.size() > 0) {
				int lastIndex = logList.size() - 1;
//				long logCnt = new OBAdcManagementImpl().getAdcAuditLogCount(adcInfo.getIndex(), "", rptInfo.getBeginTime(), rptInfo.getEndTime(), db);
//				OBDtoAuditLogAdcSystem lastLog = new OBAdcManagementImpl().getAdcAuditLogLast(adcInfo.getIndex(), "", rptInfo.getBeginTime(), rptInfo.getEndTime(), db);
//				result = String.format("%s: %d", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL), logCnt);
//				logInfo.setResult(result);
				contents = String.format("%s: %s\n", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_OCCURTIME),
						logList.get(lastIndex).getDateTime());
				contents += String.format("%s: %s", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_CONTENT),
						logList.get(lastIndex).getContent());
				logInfo.setContents(contents);
			}
			// ntp 정보 구성.
			OBDtoRptSysInfo ntpInfo = new OBDtoRptSysInfo();
			contents = "";
			ntpInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTPINFO));// OBTerminologyDB.RPT_ETC_NTPINFO);
			cfgDump = adcHandler.cmndCfgNtp();//
			OBDtoNtpCfgAlteon ntpCfg = parserClass.parseCfgNtp(cfgDump);
			if (ntpCfg != null) {
				if (ntpCfg.getState() == OBDefine.NTP_STATE_ENABLED)
					ntpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
				else
					ntpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));// OBTerminologyDB.TYPE_GENERAL_NOTUSED);
				contents = String.format("%s: %s\n", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_NAME),
						ntpCfg.getServer());
				contents += String.format("%s: %d\n", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_INTERVAL),
						ntpCfg.getInterval());
				contents += String.format("%s: %s", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_TIMEZONE),
						ntpCfg.getTimeZone());
				ntpInfo.setContents(contents);
			}

			// syslog 정보 구성.
			OBDtoRptSysInfo syslogInfo = new OBDtoRptSysInfo();
			contents = "";
			syslogInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOGINFO));// OBTerminologyDB.RPT_ETC_SYSLOGINFO);
			cfgDump = adcHandler.cmndCfgSyslog();//
			ArrayList<OBDtoSyslogCfgAlteon> syslogList = parserClass.parseCfgSyslog(cfgDump);
			syslogInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));// OBTerminologyDB.TYPE_GENERAL_NOTUSED);
			if (syslogList != null && syslogList.size() > 0) {
				syslogInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
				for (OBDtoSyslogCfgAlteon syslog : syslogList) {
					if (!contents.isEmpty())
						contents += "\n";
					contents += String.format("%s, %s: %s, %s: %s", syslog.getServerIP(),
							OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOG_SEVERITY), syslog.getSeverity(),
							OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOG_FACILITY), syslog.getFacility());

				}
				syslogInfo.setContents(contents);
			}

			retVal.setLogInfo(logInfo);
			retVal.setNtpInfo(ntpInfo);
			retVal.setSyslogInf(syslogInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	private OBDtoRptSysInfoEtc getSysInfoEtcPAS(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBAdcPASHandler handler,
			OBDatabase db) throws OBException {// etc info 추출.
		OBCLIParserPAS parserClass = new OBCLIParserPAS();
		OBDtoRptSysInfoEtc retVal = new OBDtoRptSysInfoEtc();
		String contents = "";
		String result = "";
		String infoDump = "";
		try {
			// syslog 정보 구성.
			infoDump = handler.cmndSyslogInfo();
			ArrayList<OBDtoSyslogInfoPAS> syslogInfoList = parserClass.parseSyslogInfo(infoDump);
			OBDtoRptSysInfo syslogInfo = new OBDtoRptSysInfo();
			contents = "";
			syslogInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOGINFO));// OBTerminologyDB.RPT_ETC_SYSLOGINFO);
			if (syslogInfoList != null) {
				for (int i = 0; i < syslogInfoList.size(); i++) {
					OBDtoSyslogInfoPAS syslog = syslogInfoList.get(i);
					if (syslog.getStatus() == OBDefine.STATE_ENABLE)
						syslogInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
					else
						syslogInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));// OBTerminologyDB.TYPE_GENERAL_NOTUSED);
					if (i != 0)
						contents += "\n";
					contents += String.format("%d : %-16s, %s: %-10s, %s: %s", i + 1, syslog.getIpAddress(),
							OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOG_SEVERITY), syslog.getLevel(),
							OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOG_FACILITY), syslog.getFacility());

				}
				syslogInfo.setContents(contents);
			}
			retVal.setSyslogInf(syslogInfo);

			// ntp 정보 구성.
			infoDump = handler.cmndNTPInfo();
			OBDtoNTPInfoPAS ntpDumpInfo = parserClass.parseNTPInfo(infoDump);
			OBDtoRptSysInfo ntpInfo = new OBDtoRptSysInfo();
//						DtoRptNtp rptNtp = rptOPEtc.getNtpInfo();
			contents = "";
			ntpInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTPINFO));// OBTerminologyDB.RPT_ETC_NTPINFO);
			if (ntpDumpInfo != null) {
				if (ntpDumpInfo.getStatus() == OBDefine.NTP_STATE_ENABLED)
					ntpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
				else
					ntpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));// OBTerminologyDB.TYPE_GENERAL_NOTUSED);
				contents = String.format("%s: %s\n", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_NAME),
						ntpDumpInfo.getPrimary());
				contents += String.format("%s: %s", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_INTERVAL),
						ntpDumpInfo.getInterval());
//							contents += String.format("%s: %s", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_TIMEZONE), rptNtp.getTimezone());
				ntpInfo.setContents(contents);
			}
			retVal.setNtpInfo(ntpInfo);

			infoDump = handler.cmndLoggingBuffer();

			ArrayList<OBDtoLoggingBufferPAS> syslogList = parserClass.parseLoggingBuffer(infoDump);
			// log 정보 구성.
			OBDtoRptSysInfo logInfo = new OBDtoRptSysInfo();
			contents = "";
			logInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_LOGINFO));// OBTerminologyDB.RPT_ETC_LOGINFO);
			if (syslogList != null && syslogList.size() > 0) {
				result = String.format("%s: %d", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL),
						syslogList.size());
				logInfo.setResult(result);

				// 제일 마지막 발생된 로그만 제공한다.
				OBDtoLoggingBufferPAS obj = syslogList.get(0);
				if (obj != null)
					contents = String.format("%s: %s\n",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_OCCURTIME), obj.getDate());
				if (obj != null)
					contents += String.format("%s: %s", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_CONTENT),
							obj.getContent());
				logInfo.setContents(contents);
			}
			retVal.setLogInfo(logInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	private OBDtoRptSysInfoEtc getSysInfoEtcPASK(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBAdcPASKHandler handler,
			OBDatabase db) throws OBException {// etc info 추출.
		OBCLIParserPASK parserClass = new OBCLIParserPASK();
		OBDtoRptSysInfoEtc retVal = new OBDtoRptSysInfoEtc();
		String contents = "";
		String result = "";
		String infoDump = "";
		try {
			// syslog 정보 구성.
			infoDump = handler.cmndSyslogInfo();
			ArrayList<OBDtoSyslogInfoPASK> syslogInfoList = parserClass.parseSyslogInfo(infoDump);
			OBDtoRptSysInfo syslogInfo = new OBDtoRptSysInfo();
			contents = "";
			syslogInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOGINFO));// OBTerminologyDB.RPT_ETC_SYSLOGINFO);
			if (syslogInfoList != null) {
				for (int i = 0; i < syslogInfoList.size(); i++) {
					OBDtoSyslogInfoPASK syslog = syslogInfoList.get(i);
					if (syslog.getStatus() == OBDefine.STATE_ENABLE)
						syslogInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
					else
						syslogInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));// OBTerminologyDB.TYPE_GENERAL_NOTUSED);
					if (i != 0)
						contents += "\n";
					contents += String.format("%d : %-16s, %s: %-10s, %s: %s", i + 1, syslog.getIpAddress(),
							OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOG_SEVERITY), syslog.getLevel(),
							OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOG_FACILITY), syslog.getFacility());

				}
				syslogInfo.setContents(contents);
			}
			retVal.setSyslogInf(syslogInfo);

			// ntp 정보 구성.
			infoDump = handler.cmndNTPInfo();
			OBDtoNTPInfoPASK ntpDumpInfo = parserClass.parseNTPInfo(infoDump);
			OBDtoRptSysInfo ntpInfo = new OBDtoRptSysInfo();
//						DtoRptNtp rptNtp = rptOPEtc.getNtpInfo();
			contents = "";
			ntpInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTPINFO));// OBTerminologyDB.RPT_ETC_NTPINFO);
			if (ntpDumpInfo != null) {
				if (ntpDumpInfo.getStatus() == OBDefine.NTP_STATE_ENABLED)
					ntpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
				else
					ntpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));// OBTerminologyDB.TYPE_GENERAL_NOTUSED);
				contents = String.format("%s: %s\n", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_NAME),
						ntpDumpInfo.getPrimary());
				contents += String.format("%s: %s", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_INTERVAL),
						ntpDumpInfo.getInterval());
//							contents += String.format("%s: %s", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_TIMEZONE), rptNtp.getTimezone());
				ntpInfo.setContents(contents);
			}
			retVal.setNtpInfo(ntpInfo);

			infoDump = handler.cmndLoggingBuffer();

			ArrayList<OBDtoLoggingBufferPASK> syslogList = parserClass.parseLoggingBuffer(infoDump);
			// log 정보 구성.
			OBDtoRptSysInfo logInfo = new OBDtoRptSysInfo();
			contents = "";
			logInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_LOGINFO));// OBTerminologyDB.RPT_ETC_LOGINFO);
			if (syslogList != null && syslogList.size() > 0) {
				result = String.format("%s: %d", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL),
						syslogList.size());
				logInfo.setResult(result);

				// 제일 마지막 발생된 로그만 제공한다.
				OBDtoLoggingBufferPASK obj = syslogList.get(0);
				if (obj != null)
					contents = String.format("%s: %s\n",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_OCCURTIME), obj.getDate());
				if (obj != null)
					contents += String.format("%s: %s", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_CONTENT),
							obj.getContent());
				logInfo.setContents(contents);
			}
			retVal.setLogInfo(logInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	private OBDtoRptSysInfoEtc getSysInfoEtcF5(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBDatabase db)
			throws OBException {// etc info 추출.
		OBDtoRptSysInfoEtc output = new OBDtoRptSysInfoEtc();
		String contents = "";
		String result = "";
		try {
			OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());// new OBSnmpF5(adcInfo.getAdcIpAddress());

			DtoRptOPEtc rptOPEtc = null;
			try {
				rptOPEtc = snmp.getRptOPEtcInfo(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} catch (OBException e) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get getRptOPEtcInfo.%s", e.getErrorMessage()));
			}
			// log 정보 구성.
			OBDtoRptSysInfo logInfo = new OBDtoRptSysInfo();
			contents = "";
			logInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_LOGINFO));// OBTerminologyDB.RPT_ETC_LOGINFO);
			if (rptOPEtc != null) {
				long logCnt = new OBAdcManagementImpl().getAdcAuditLogCount(adcInfo.getIndex(), "",
						rptInfo.getBeginTime(), rptInfo.getEndTime(), db);
				OBDtoAuditLogAdcSystem lastLog = new OBAdcManagementImpl().getAdcAuditLogLast(adcInfo.getIndex(), "",
						rptInfo.getBeginTime(), rptInfo.getEndTime(), db);
				result = String.format("%s: %d", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL), logCnt);
				logInfo.setResult(result);
				if (lastLog.getOccurTime() != null)
					contents = String.format("%s: %s\n",
							OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_OCCURTIME), lastLog.getOccurTime());
				if (lastLog.getContents() != null)
					contents += String.format("%s: %s", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_CONTENT),
							lastLog.getContents());
				logInfo.setContents(contents);
			}

			// ntp 정보 구성.
			OBDtoRptSysInfo ntpInfo = new OBDtoRptSysInfo();
			ntpInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTPINFO));// OBTerminologyDB.RPT_ETC_NTPINFO);
			if (rptOPEtc != null) {
				contents = "";
				DtoRptNtp rptNtp = rptOPEtc.getNtpInfo();
				if (rptNtp != null) {
					if (rptNtp.getEnabled() != null && rptNtp.getEnabled() == OBDefine.NTP_STATE_ENABLED)
						ntpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
					else
						ntpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));// OBTerminologyDB.TYPE_GENERAL_NOTUSED);

					contents = String.format("%s: %s\n", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_NAME),
							rptNtp.getName());
					contents += String.format("%s: %s\n", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_INTERVAL),
							rptNtp.getInterval());
					contents += String.format("%s: %s", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_TIMEZONE),
							rptNtp.getTimezone());
				} else {
					ntpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_NOT_APPLY));// OBTerminologyDB.TYPE_NOT_APPLY);
				}
				ntpInfo.setContents(contents);
			}

			// syslog 정보 구성.
			OBDtoRptSysInfo syslogInfo = new OBDtoRptSysInfo();
			syslogInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOGINFO));// OBTerminologyDB.RPT_ETC_SYSLOGINFO);
			contents = "";
			if (rptOPEtc != null) {
				ArrayList<DtoRptSyslog> syslogList = rptOPEtc.getSyslogList();
				if (syslogList != null) {
					if (syslogList.size() >= 0)
						syslogInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
					else
						syslogInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));// OBTerminologyDB.TYPE_GENERAL_NOTUSED);
					for (int i = 0; i < syslogList.size(); i++) {
						DtoRptSyslog syslog = rptOPEtc.getSyslogList().get(i);

						if (i != 0)
							contents += "\n";

						contents += String.format("%d : %s, %s: %d, %s: %s", i + 1, syslog.getHost(),
								OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOG_SEVERITY), syslog.getSeverity(),
								OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOG_FACILITY), syslog.getFacility());
					}
				} else {
					syslogInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_NOT_APPLY));// OBTerminologyDB.TYPE_NOT_APPLY);
				}
				syslogInfo.setContents(contents);
			}

			output.setLogInfo(logInfo);
			output.setNtpInfo(ntpInfo);
			output.setSyslogInf(syslogInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return output;
	}

	private OBDtoRptSystemInfo getSystemInfoAlteon(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBDatabase db)
			throws OBException {
		if (isAvailableSnmpAlteon() == true)
			return getSystemInfoAlteonSnmp(adcInfo, rptInfo, db);
		else
			return getSystemInfoAlteonCLI(adcInfo, rptInfo, db);
	}

	private OBDtoRptSystemInfo getSystemInfoAlteonCLI(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBDatabase db)
			throws OBException {
		OBDtoRptSystemInfo retVal = new OBDtoRptSystemInfo();

		OBAdcAlteonHandler adcHandler = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
		adcHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
				adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
		try {
			adcHandler.login();

			// 기본 정보 추출.
			retVal.setBasicInfo(getSysInfoBasicAlteonCLI(adcInfo, rptInfo, adcHandler, db));
			// L2 정보 추출.
			retVal.setL2Info(getSysInfoL2AlteonCLI(adcInfo, rptInfo, adcHandler, db));
			// L3 정보 추출.
			retVal.setL3Info(getSysInfoL3AlteonCLI(adcInfo, rptInfo, adcHandler, db));
			// L4 정보 추출
			retVal.setL4Info(getSysInfoL4AlteonCLI(adcInfo, rptInfo, adcHandler, db));
			// L7 정보 추출.
			retVal.setL7Info(getSysInfoL7AlteonCLI(adcInfo, rptInfo, adcHandler, db));
			// 기타 정보 추출.
			retVal.setEtcInfo(getSysInfoEtcAlteonCLI(adcInfo, rptInfo, adcHandler, db));
		} catch (OBException e) {
			adcHandler.disconnect();
			throw e;
		} catch (Exception e) {
			adcHandler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		adcHandler.disconnect();
		return retVal;
	}

	private OBDtoRptSystemInfo getSystemInfoAlteonSnmp(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBDatabase db)
			throws OBException {
		OBDtoRptSystemInfo systemInfo = new OBDtoRptSystemInfo();
		try {
			// 기본 정보 추출.
			systemInfo.setBasicInfo(getSysInfoBasicAlteonSnmp(adcInfo, rptInfo, db));
			// L2 정보 추출.
			systemInfo.setL2Info(getSysInfoL2AlteonSnmp(adcInfo, rptInfo, db));
			// L3 정보 추출.
			systemInfo.setL3Info(getSysInfoL3AlteonSnmp(adcInfo, rptInfo, db));
			// L4 정보 추출
			systemInfo.setL4Info(getSysInfoL4AlteonSnmp(adcInfo, rptInfo, db));
			// L7 정보 추출.
			systemInfo.setL7Info(getSysInfoL7AlteonSnmp(adcInfo, rptInfo, db));
			// 기타 정보 추출.
			systemInfo.setEtcInfo(getSysInfoEtcAlteonSnmp(adcInfo, rptInfo, db));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return systemInfo;
	}

//	public static void main(String[] args)
//	{
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			db.openDB();
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(2);
//			OBReportInfo rptInfo = new OBReportInfo();
//			new OBReportOperationImpl().getSystemInfoPAS(adcInfo, rptInfo, db);
//			db.closeDB();
//		}
//		catch(Exception e)
//		{
//			db.closeDB();
//			e.printStackTrace();
//		}
//	}

	private OBDtoRptSystemInfo getSystemInfoPAS(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBDatabase db)
			throws OBException {
		OBDtoRptSystemInfo systemInfo = new OBDtoRptSystemInfo();
		OBAdcPASHandler adcHandler = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
		adcHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
				adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
		try {
			adcHandler.login();
			// 기본 정보 추출.
			systemInfo.setBasicInfo(getSysInfoBasicPAS(adcInfo, rptInfo, adcHandler, db));
			OBDateTime.Sleep(1000);
			// L2 정보 추출.
			systemInfo.setL2Info(getSysInfoL2PAS(adcInfo, rptInfo, adcHandler, db));
			OBDateTime.Sleep(1000);
//			// L3 정보 추출.
			systemInfo.setL3Info(getSysInfoL3PAS(adcInfo, rptInfo, adcHandler, db));
			OBDateTime.Sleep(1000);
//			// L4 정보 추출 
			systemInfo.setL4Info(getSysInfoL4PAS(adcInfo, rptInfo, adcHandler, db));
			OBDateTime.Sleep(1000);
//			// L7 정보 추출.
//			systemInfo.setL7Info(getSysInfoL7PAS(adcInfo, rptInfo, adcHandler, db));
//			// 기타 정보 추출.
			systemInfo.setEtcInfo(getSysInfoEtcPAS(adcInfo, rptInfo, adcHandler, db));

			adcHandler.disconnect();
		} catch (OBExceptionUnreachable e) {
			adcHandler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE);
		} catch (OBExceptionLogin e) {
			adcHandler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN);
		} catch (OBException e) {
			adcHandler.disconnect();
			throw e;
		} catch (Exception e) {
			adcHandler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}

		return systemInfo;
	}

	private OBDtoRptSystemInfo getSystemInfoPASK(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBDatabase db)
			throws OBException {
		OBDtoRptSystemInfo systemInfo = new OBDtoRptSystemInfo();
		OBAdcPASKHandler adcHandler = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
		adcHandler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
				adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
		try {
			adcHandler.login();
			// 기본 정보 추출.
			systemInfo.setBasicInfo(getSysInfoBasicPASK(adcInfo, rptInfo, adcHandler, db));
			OBDateTime.Sleep(1000);
			// L2 정보 추출.
			systemInfo.setL2Info(getSysInfoL2PASK(adcInfo, rptInfo, adcHandler, db));
			OBDateTime.Sleep(1000);
			// L3 정보 추출.
			systemInfo.setL3Info(getSysInfoL3PASK(adcInfo, rptInfo, adcHandler, db));
			OBDateTime.Sleep(1000);
			// L4 정보 추출
			systemInfo.setL4Info(getSysInfoL4PASK(adcInfo, rptInfo, adcHandler, db));
			OBDateTime.Sleep(1000);
			// 기타 정보 추출.
			systemInfo.setEtcInfo(getSysInfoEtcPASK(adcInfo, rptInfo, adcHandler, db));

			adcHandler.disconnect();
		} catch (OBExceptionUnreachable e) {
			adcHandler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());

		} catch (OBExceptionLogin e) {
			adcHandler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());

		} catch (OBException e) {
			adcHandler.disconnect();
			throw e;
		} catch (Exception e) {
			adcHandler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}

		return systemInfo;
	}

	private OBDtoRptSystemInfo getSystemInfoF5(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBDatabase db)
			throws OBException {
		OBDtoRptSystemInfo systemInfo = new OBDtoRptSystemInfo();
		try {
			if (new OBTelnetCmndExecV2().isReachable(adcInfo.getAdcIpAddress(), adcInfo.getConnPort()) == false)
				throw new OBExceptionUnreachable("unreachable");

			// 기본 정보 추출.
			systemInfo.setBasicInfo(getSysInfoBasicF5(adcInfo, rptInfo));
			// L2 정보 추출.
			systemInfo.setL2Info(getSysInfoL2F5(adcInfo, rptInfo));
			// L3 정보 추출.
			systemInfo.setL3Info(getSysInfoL3F5(adcInfo, rptInfo, db));
			// L4 정보 추출
			systemInfo.setL4Info(getSysInfoL4F5(adcInfo, rptInfo, db));
			// L7 정보 추출.
			systemInfo.setL7Info(getSysInfoL7F5(adcInfo, rptInfo, db));
			// 기타 정보 추출.
			systemInfo.setEtcInfo(getSysInfoEtcF5(adcInfo, rptInfo, db));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return systemInfo;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			ArrayList<String> list = new ArrayList<String>();
//			list.add("1");
//			list.add("2");
////			ArrayList<OBDtoRptPortInfo> info = new OBReportOperationImpl().getPortInfo("1351500971603", 2, list);
//			OBDtoRptAdcInfo info = new OBReportOperationImpl().getAdcInfo("1368177056404", 2);
//			System.out.println(info);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	@Override
	public ArrayList<OBDtoRptPortInfo> getPortInfo(String rptIndex, Integer adcIndex, ArrayList<String> portNameList)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. rptIndex:%s, adcIndex:%d, portNameList:%s", rptIndex, adcIndex, portNameList));

		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoRptPortInfo> result = null;
		try {
			db.openDB();

			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			OBReportInfo rptInfo = new OBReportImpl().getReportInfo(rptIndex);
			result = getPortInfo(rptInfo, adcInfo, portNameList, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end:%s", result));

		return result;
	}

	private ArrayList<OBDtoRptPortInfo> getPortInfo(OBReportInfo rptInfo, OBDtoAdcInfo adcInfo,
			ArrayList<String> portNameList, OBDatabase db) throws OBException {
		String sqlText = "";
		ArrayList<OBDtoRptPortInfo> result = new ArrayList<OBDtoRptPortInfo>();
		try {
			for (String portName : portNameList) {
				sqlText = String.format(
						" SELECT " + " OCCUR_TIME, ERRORS_IN, ERRORS_OUT, DROPS_IN, DROPS_OUT " + " FROM LOG_ADC_PORTS "
								+ " WHERE ADC_INDEX=%d " + " AND PORT_NAME=%s ",
						adcInfo.getIndex(), OBParser.sqlString(portName));

				String sqlTime = "";
				if (rptInfo.getEndTime() == null)
					sqlTime = String.format(" OCCUR_TIME <= %s ", OBParser.sqlString(OBDateTime.now()));
				else
					sqlTime = String.format(" OCCUR_TIME <= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(rptInfo.getEndTime().getTime()))));

				if (rptInfo.getBeginTime() != null)
					sqlTime += String.format(" AND OCCUR_TIME >= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(rptInfo.getBeginTime().getTime()))));

				if (sqlTime != null && !sqlTime.isEmpty())
					sqlText += " AND" + sqlTime;

				sqlText += " ORDER BY OCCUR_TIME DESC ";

				sqlText += ";";

				ResultSet rs = db.executeQuery(sqlText);
				ArrayList<OBDtoRptPortErrDiscard> errDiscardList = new ArrayList<OBDtoRptPortErrDiscard>();
				while (rs.next()) {
					OBDtoRptPortErrDiscard errDiscard = new OBDtoRptPortErrDiscard();
					errDiscard.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
//					errDiscard.setDiscards((long)(10000L + Math.random() * (99999 - 10000)));
//					errDiscard.setErrors((long)(10000L + Math.random() * (99999 - 10000)));
					errDiscard.setDiscards(db.getLong(rs, "DROPS_IN") + db.getLong(rs, "DROPS_OUT"));
					errDiscard.setErrors(db.getLong(rs, "ERRORS_IN") + db.getLong(rs, "ERRORS_OUT"));
					errDiscardList.add(errDiscard);
				}

				OBDtoRptPortInfo portInfo = new OBDtoRptPortInfo();
				portInfo.setBeginTime(rptInfo.getBeginTime());
				portInfo.setEndTime(rptInfo.getEndTime());
				portInfo.setPortName(portName);
				portInfo.setErrDiscardsList(errDiscardList);
				result.add(portInfo);
			}
		} catch (SQLException e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}

		return result;
	}

	private HashMap<String, OBDtoRptPortInfo> getPortInfo(OBDtoAdcInfo adcInfo, ArrayList<String> portNameList,
			Date lastTime) throws OBException {
		String sqlText = "";
		HashMap<String, OBDtoRptPortInfo> result = new HashMap<String, OBDtoRptPortInfo>();
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			for (String portName : portNameList) {
				sqlText = String.format(
						" SELECT " + " OCCUR_TIME, ERRORS_IN, ERRORS_OUT, DROPS_IN, DROPS_OUT " + " FROM LOG_ADC_PORTS "
								+ " WHERE ADC_INDEX=%d " + " AND PORT_NAME=%s ",
						adcInfo.getIndex(), OBParser.sqlString(portName));

				String sqlTime = "";
				if (lastTime != null)
					sqlTime += String.format(" OCCUR_TIME >= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(lastTime.getTime()))));

				if (sqlTime != null && !sqlTime.isEmpty())
					sqlText += " AND" + sqlTime;

				sqlText += " ORDER BY OCCUR_TIME DESC ";

				sqlText += ";";

				ResultSet rs = db.executeQuery(sqlText);
				ArrayList<OBDtoRptPortErrDiscard> errDiscardList = new ArrayList<OBDtoRptPortErrDiscard>();
				while (rs.next()) {
					OBDtoRptPortErrDiscard errDiscard = new OBDtoRptPortErrDiscard();
					errDiscard.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
					errDiscard.setDiscards(db.getLong(rs, "DROPS_IN") + db.getLong(rs, "DROPS_OUT"));
					errDiscard.setErrors(db.getLong(rs, "ERRORS_IN") + db.getLong(rs, "ERRORS_OUT"));
					errDiscardList.add(errDiscard);
				}

				OBDtoRptPortInfo portInfo = new OBDtoRptPortInfo();
				portInfo.setPortName(portName);
				portInfo.setErrDiscardsList(errDiscardList);
				result.put(portName, portInfo);
			}
		} catch (SQLException e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));

		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}

		return result;
	}
}
