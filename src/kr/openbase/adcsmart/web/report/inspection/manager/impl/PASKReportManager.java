package kr.openbase.adcsmart.web.report.inspection.manager.impl;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReporHeader;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.manager.AbstractReportDataManager;

/**
 * PASK 장비의 정기점검 보고서를 위한 자료를 수집한다. 사용하지 않는다.
 * 
 * @author 최영조
 */
public class PASKReportManager extends AbstractReportDataManager {
//	private OBDtoAdcInfo adcInfo;

	public PASKReportManager(OBDtoAdcInfo adcInfo) {
//		this.adcInfo = adcInfo;
	}

	@Override
	public OBDtoInspectionReporHeader getAdcInfo() {
		OBDtoInspectionReporHeader retVal = new OBDtoInspectionReporHeader();

		// OBDatabaseWithDBCP db = new OBDatabaseWithDBCP();
		//
		// // retVal.setIndex(adcInfo.getIndex());
		//
		// retVal.setAdcType(adcInfo.getAdcType());
		// retVal.setAdcName(adcInfo.getName());
		// retVal.setIpAddr(adcInfo.getAdcIpAddress());
		//
		// OBAdcPASKHandler adcHandler;
		// try
		// {
		// OBCLIParserPASK parserClass = new OBCLIParserPASK();
		// adcHandler = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
		// adcHandler.setConnectionInfo(adcInfo.getAdcIpAddress(),
		// adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
		// adcInfo.getConnService(), adcInfo.getConnPort());
		//
		// String infoDump = "";
		// db.openDB();
		//
		// adcHandler.login();
		// try
		// {
		// infoDump = adcHandler.cmndSystem();
		// OBDtoSystemInfoPASK systemInfo = parserClass.parseSystem(infoDump);
		// if(systemInfo != null)
		// {
		// retVal.setModelName(systemInfo.getProductName());
		// retVal.setSerialNumber(systemInfo.getSerialNum());
		// // retVal.setRunTime(systemInfo.getUpTime());
		// retVal.setOsVersion(systemInfo.getVersion());
		// }
		//
		// infoDump = adcHandler.cmndSnmpInfo();
		// OBDtoSnmpInfoPASK snmpInfo = parserClass.parseSnmpInfo(infoDump);
		// if(snmpInfo != null)
		// {
		// retVal.setHostName(snmpInfo.getHostName());
		// }
		//
		// infoDump = adcHandler.cmndInterface();
		// ArrayList<OBDtoInterfaceInfoPASK> interfaceList =
		// parserClass.parseInterface(infoDump);
		// if(snmpInfo != null)
		// {
		// String macAddr = "";
		// for(OBDtoInterfaceInfoPASK obj : interfaceList)
		// {
		// if(!macAddr.isEmpty())
		// macAddr += "\n";
		// macAddr += obj.getMacAddr();
		// }
		// retVal.setMacAddr(macAddr);
		// }
		//
		// // license 정보 추출.
		// infoDump = adcHandler.cmndLicense();//
		// if(infoDump != null && !infoDump.isEmpty())
		// {
		// OBDtoLicenseInfoPASK licInfo = parserClass.parseLicense(infoDump);
		// String licContent = "";
		// licContent += "State : " + licInfo.getStatus();
		// licContent += "\n";
		// licContent += "Expire Date: " + licInfo.getExpiredDate();
		// // retVal.setLicense(licContent);
		// }
		// }
		// catch(OBException e)
		// {
		// throw e;
		// }
		// catch(Exception e)
		// {
		// throw e;
		// }
		// finally
		// {
		// adcHandler.disconnect();
		// }
		// }
		// catch(OBException e)
		// {
		// // TODO Auto-generated catch block
		// // throw e;
		// }
		// catch(Exception e)
		// {
		// // TODO Auto-generated catch block
		// // throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		// }
		// finally
		// {
		// if(db != null)
		// db.closeDB();
		// }
		return retVal;
	}

	@Override
	public List<OBDtoInspectionReportRow> getSystemInfo(OBReportInfo rptInfo) {
		List<OBDtoInspectionReportRow> systemInfo = new ArrayList<OBDtoInspectionReportRow>();
		// OBDtoRptSystemInfo systemInfo = new OBDtoRptSystemInfo();
		// OBAdcPASKHandler adcHandler = null;
		// try
		// {
		// adcHandler = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
		// adcHandler.setConnectionInfo(adcInfo.getAdcIpAddress(),
		// adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
		// adcInfo.getConnService(), adcInfo.getConnPort());
		// adcHandler.login();
		// // 기본 정보 추출.
		// systemInfo.setBasicInfo(getSysInfoBasicPASK(adcInfo, rptInfo, adcHandler));
		// OBDateTime.Sleep(1000);
		// // L2 정보 추출.
		// systemInfo.setL2Info(getSysInfoL2PASK(adcInfo, rptInfo, adcHandler));
		// OBDateTime.Sleep(1000);
		// // L3 정보 추출.
		// systemInfo.setL3Info(getSysInfoL3PASK(adcInfo, rptInfo, adcHandler));
		// OBDateTime.Sleep(1000);
		// // L4 정보 추출
		// systemInfo.setL4Info(getSysInfoL4PASK(adcInfo, rptInfo, adcHandler));
		// OBDateTime.Sleep(1000);
		// // 기타 정보 추출.
		// systemInfo.setEtcInfo(getSysInfoEtcPASK(adcInfo, rptInfo, adcHandler));
		// }
		// catch(OBExceptionUnreachable e)
		// {
		// // TODO Auto-generated catch block
		// // throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE,
		// e.getMessage());
		//
		// }
		// catch(OBExceptionLogin e)
		// {
		// // TODO Auto-generated catch block
		// // throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		//
		// }
		// catch(OBException e)
		// {
		// // TODO Auto-generated catch block
		// // throw e;
		// }
		// catch(Exception e)
		// {
		// // TODO Auto-generated catch block
		// // throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		// }
		// finally
		// {
		// if(adcHandler != null)
		// {
		// adcHandler.disconnect();
		// }
		// }
		//
		// return systemInfo;
		return systemInfo;
	}

//    private OBDtoRptSysInfoBasic getSysInfoBasicPASK(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBAdcPASKHandler handler) throws OBException
//    {// 시스템 info 추출.
//        OBCLIParserPASK parserClass = new OBCLIParserPASK();
//        OBDtoRptSysInfoBasic retVal = new OBDtoRptSysInfoBasic();
//        String contents = "";
//        try
//        {
//            OBDtoSystemInfoPASK systemInfo = null;
//            OBDtoResourceInfoPASK resourceInfo = null;
//            OBDtoHWStatPASK hwInfo = null;
//            Timestamp applyTime = null;
//            try
//            {
//                String cfg = handler.cmndSystem();
//                systemInfo = parserClass.parseSystem(cfg);
//
//                cfg = handler.cmndResources();
//                resourceInfo = parserClass.parseResources(cfg);
//
//                cfg = handler.cmndHwStatistics();
//                hwInfo = parserClass.parseHWStatistics(cfg);
//
//                cfg = handler.cmndSlbDump();
//                applyTime = parserClass.parseApplyTime(cfg);
//            }
//            catch(OBException e)
//            {
//                OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get rpt_op_system_info.%s", e.getErrorMessage()));
//
//            }
//            // uptime 구성
//            OBDtoRptSysInfo upTimeInfo = new OBDtoRptSysInfo();
//            contents = "";
//            upTimeInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_UPTIME));// OBTerminologyDB.RPT_BASIC_UPTIME);
//            if(systemInfo != null)
//            {
//                upTimeInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
//                upTimeInfo.setContents(systemInfo.getUpTime());
//            }
//            retVal.setUpTime(upTimeInfo);
//
//            // last apply time 구성. DB에서 추출한다. 즉 SLB 설정시간을 추출한다.
//            OBDtoRptSysInfo applyTimeInfo = new OBDtoRptSysInfo();
//            contents = "";
//            applyTimeInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_LASTAPPLY));// OBTerminologyDB.RPT_BASIC_LASTAPPLY);
//            Timestamp lastTime = applyTime;
//            if(lastTime != null)
//            {
//                applyTimeInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
//                long days = 0;
//                Timestamp nowTime = OBDateTime.toTimestamp(OBDateTime.now());
//                long diff = Math.abs(nowTime.getTime() - lastTime.getTime());
//                days = diff / (1000 * 60 * 60 * 24);
//                contents = String.format("%s (%d %s)", OBDateTime.toString(lastTime), days, OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DAY));// OBTerminologyDB.TYPE_GENERAL_DAY);
//            }
//            applyTimeInfo.setContents(contents);
//            retVal.setLastApplyTime(applyTimeInfo);
//
//            // cpu 정보 구성
//            OBDtoRptSysInfo cpuInfo = new OBDtoRptSysInfo();
//            contents = "";
//            cpuInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_CPUINFO));// OBTerminologyDB.RPT_BASIC_CPUINFO);
//            if(resourceInfo != null)
//            {
//                cpuInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
//
//                long cpuUsage = (resourceInfo.getCpuUsageMP() + resourceInfo.getCpuUsageSP()) / 2;
//                contents = String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_MPCPU), resourceInfo.getCpuUsageMP());
//                contents += "%";
//                contents += ", ";
//                contents += String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_SPCPU), resourceInfo.getCpuUsageSP());
//                contents += "%";
//
//                String avgContent = String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_AVG), cpuUsage);
//                avgContent += "%\n";
//                contents = avgContent + contents;
//                cpuInfo.setContents(contents);
//            }
//            retVal.setCpuInfo(cpuInfo);
//
//            // memory 정보 구성
//            OBDtoRptSysInfo memInfo = new OBDtoRptSysInfo();
//            contents = "";
//            memInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_MEMINFO));// OBTerminologyDB.RPT_BASIC_MEMINFO);
//            if(resourceInfo != null)
//            {
//                memInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
//
//                long memUsage = (resourceInfo.getMemUsageMP() + resourceInfo.getMemUsageSP()) / 2;
//                contents = String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_MPCPU), resourceInfo.getMemUsageMP());
//                contents += "%";
//                contents += ", ";
//                contents += String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_SPCPU), resourceInfo.getMemUsageSP());
//                contents += "%";
//
//                String avgContent = String.format("%s: %d ", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_AVG), memUsage);
//                avgContent += "%\n";
//                contents = avgContent + contents;
//                memInfo.setContents(contents);
//            }
//            retVal.setMemoryInfo(memInfo);
//
//            // power 정보 구성
//            OBDtoRptSysInfo powerInfo = new OBDtoRptSysInfo();
//            contents = "";
//            powerInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_POWERINFO));// OBTerminologyDB.RPT_BASIC_POWERINFO);
//            if(hwInfo != null)
//            {
//                boolean status = true;
//                if(hwInfo.getAc1Status() == OBDefine.SYS_POWERSUPPLY_STATUS_OK)
//                {
//                    contents = String.format("AC1: ON");
//                }
//                else
//                {
//                    status = false;
//                    contents = String.format("AC1: OFF");
//                }
//
//                if(hwInfo.getAc2Status() == OBDefine.SYS_POWERSUPPLY_STATUS_OK)
//                {
//                    if(!contents.isEmpty())
//                        contents += ", ";
//                    contents += String.format("AC2: ON");
//                }
//                else
//                {
//                    status = false;
//                    if(!contents.isEmpty())
//                        contents += ", ";
//                    contents += String.format("AC2: OFF");
//                }
//
//                if(status == true)
//                    powerInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
//                else
//                    powerInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
//
//                powerInfo.setContents(contents);
//            }
//            retVal.setPowerInfo(powerInfo);
//
//            // fan 정보 구성.
//            OBDtoRptSysInfo fanInfo = new OBDtoRptSysInfo();
//            contents = "";
//            fanInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_FANINFO));// OBTerminologyDB.RPT_BASIC_FANINFO);
//            if(hwInfo != null)
//            {
//                boolean status = true;
//                if(hwInfo.getFan1Status() == OBDefine.SYS_FAN_STATUS_OK)
//                {
//                    contents = String.format("FAN1: ON");
//                }
//                else
//                {
//                    status = false;
//                    contents = String.format("FAN1: OFF");
//                }
//
//                if(hwInfo.getFan2Status() == OBDefine.SYS_POWERSUPPLY_STATUS_OK)
//                {
//                    if(!contents.isEmpty())
//                        contents += ", ";
//                    contents += String.format("FAN2: ON");
//                }
//                else
//                {
//                    status = false;
//                    if(!contents.isEmpty())
//                        contents += ", ";
//                    contents += String.format("FAN2: OFF");
//                }
//
//                if(hwInfo.getFan3Status() == OBDefine.SYS_POWERSUPPLY_STATUS_OK)
//                {
//                    if(!contents.isEmpty())
//                        contents += ", ";
//                    contents += String.format("FAN3: ON");
//                }
//                else
//                {
//                    status = false;
//                    if(!contents.isEmpty())
//                        contents += ", ";
//                    contents += String.format("FAN3: OFF");
//                }
//
//                if(hwInfo.getFan4Status() == OBDefine.SYS_POWERSUPPLY_STATUS_OK)
//                {
//                    if(!contents.isEmpty())
//                        contents += ", ";
//                    contents += String.format("FAN4: ON");
//                }
//                else
//                {
//                    status = false;
//                    if(!contents.isEmpty())
//                        contents += ", ";
//                    contents += String.format("FAN4: OFF");
//                }
//
//                if(status == true)
//                    fanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
//                else
//                    fanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
//
//                fanInfo.setContents(contents);
//            }
//            retVal.setFanInfo(fanInfo);
//        }
//        catch(Exception e)
//        {
//            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
//        }
//        return retVal;
//    }
//
//    private OBDtoRptSysInfoEtc getSysInfoEtcPASK(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBAdcPASKHandler handler) throws OBException
//    {// etc info 추출.
//        OBCLIParserPASK parserClass = new OBCLIParserPASK();
//        OBDtoRptSysInfoEtc retVal = new OBDtoRptSysInfoEtc();
//        String contents = "";
//        String result = "";
//        String infoDump = "";
//        try
//        {
//            // syslog 정보 구성.
//            infoDump = handler.cmndSyslogInfo();
//            ArrayList<OBDtoSyslogInfoPASK> syslogInfoList = parserClass.parseSyslogInfo(infoDump);
//            OBDtoRptSysInfo syslogInfo = new OBDtoRptSysInfo();
//            contents = "";
//            syslogInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOGINFO));// OBTerminologyDB.RPT_ETC_SYSLOGINFO);
//            if(syslogInfoList != null)
//            {
//                for(int i = 0; i < syslogInfoList.size(); i++)
//                {
//                    OBDtoSyslogInfoPASK syslog = syslogInfoList.get(i);
//                    if(syslog.getStatus() == OBDefine.STATE_ENABLE)
//                        syslogInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
//                    else
//                        syslogInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));// OBTerminologyDB.TYPE_GENERAL_NOTUSED);
//                    if(i != 0)
//                        contents += "\n";
//                    contents += String.format("%d : %-16s, %s: %-10s, %s: %s", i + 1, syslog.getIpAddress(), OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOG_SEVERITY), syslog.getLevel(), OBMessages.getMessage(OBMessages.MSG_RPT_ETC_SYSLOG_FACILITY), syslog.getFacility());
//
//                }
//                syslogInfo.setContents(contents);
//            }
//            retVal.setSyslogInf(syslogInfo);
//
//            // ntp 정보 구성.
//            infoDump = handler.cmndNTPInfo();
//            OBDtoNTPInfoPASK ntpDumpInfo = parserClass.parseNTPInfo(infoDump);
//            OBDtoRptSysInfo ntpInfo = new OBDtoRptSysInfo();
//            // DtoRptNtp rptNtp = rptOPEtc.getNtpInfo();
//            contents = "";
//            ntpInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTPINFO));// OBTerminologyDB.RPT_ETC_NTPINFO);
//            if(ntpDumpInfo != null)
//            {
//                if(ntpDumpInfo.getStatus() == OBDefine.NTP_STATE_ENABLED)
//                    ntpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
//                else
//                    ntpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));// OBTerminologyDB.TYPE_GENERAL_NOTUSED);
//                contents = String.format("%s: %s\n", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_NAME), ntpDumpInfo.getPrimary());
//                contents += String.format("%s: %s", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_INTERVAL), ntpDumpInfo.getInterval());
//                // contents += String.format("%s: %s", OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_TIMEZONE), rptNtp.getTimezone());
//                ntpInfo.setContents(contents);
//            }
//            retVal.setNtpInfo(ntpInfo);
//
//            infoDump = handler.cmndLoggingBuffer();
//
//            ArrayList<OBDtoLoggingBufferPASK> syslogList = parserClass.parseLoggingBuffer(infoDump);
//            // log 정보 구성.
//            OBDtoRptSysInfo logInfo = new OBDtoRptSysInfo();
//            contents = "";
//            logInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_LOGINFO));// OBTerminologyDB.RPT_ETC_LOGINFO);
//            if(syslogList != null && syslogList.size() > 0)
//            {
//                result = String.format("%s: %d", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL), syslogList.size());
//                logInfo.setResult(result);
//
//                // 제일 마지막 발생된 로그만 제공한다.
//                OBDtoLoggingBufferPASK obj = syslogList.get(0);
//                if(obj != null)
//                    contents = String.format("%s: %s\n", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_OCCURTIME), obj.getDate());
//                if(obj != null)
//                    contents += String.format("%s: %s", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_CONTENT), obj.getContent());
//                logInfo.setContents(contents);
//            }
//            retVal.setLogInfo(logInfo);
//        }
//        catch(OBException e)
//        {
//            throw e;
//        }
//        catch(Exception e)
//        {
//            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
//        }
//        return retVal;
//    }
//
//    private OBDtoRptSysInfoL3 getSysInfoL3PASK(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBAdcPASKHandler handler) throws OBException
//    {// L3 info 추출.
//        OBCLIParserPASK parserClass = new OBCLIParserPASK();
//        OBDtoRptSysInfoL3 retVal = new OBDtoRptSysInfoL3();
//        String contents = "";
//        String infoDump = "";
//        try
//        {
//            infoDump = handler.cmndInterface();
//            ArrayList<OBDtoInterfaceInfoPASK> interfaceList = parserClass.parseInterface(infoDump);
//            // interface 정보
//            OBDtoRptSysInfo intInfo = new OBDtoRptSysInfo();
//            contents = "";
//            intInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L3_INTERFACE));// OBTerminologyDB.RPT_L3_INTERFACE);
//            if(interfaceList != null)
//            {
//                intInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
//                for(OBDtoInterfaceInfoPASK rptInt : interfaceList)
//                {
//                    if(rptInt.getStatus() != OBDefine.L2_LINK_STATUS_UP)
//                        intInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
//                    if(!contents.isEmpty())
//                        contents += "\n";
//                    // [1] IP: 192.168.100.1 Netmask: 255.255.255.0 Bcast:192.168.1.0 VLAN: 1,2 상태: Up
//                    contents += "[" + rptInt.getName() + "]" + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_IPADDRESS) + ": " + rptInt.getIpAddr() + " " + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_MAC) + ": " + rptInt.getMacAddr() + " " + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_STATUS) + ": " + convertLinkStatus(rptInt.getStatus());
//                }
//                intInfo.setContents(contents);
//            }
//            retVal.setInterfaceInfo(intInfo);
//
//            // gateway 정보.
//            infoDump = handler.cmndGateway();
//            ArrayList<OBDtoGatewayInfoPAS> gatewayList = new OBCLIParserPAS().parseGateway(infoDump);
//            OBDtoRptSysInfo gwInfo = new OBDtoRptSysInfo();
//            contents = "";
//            gwInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L3_GATEWAY));// OBTerminologyDB.RPT_L3_GATEWAY);;
//            if(gatewayList != null)
//            {
//                gwInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
//                for(OBDtoGatewayInfoPAS gateway : gatewayList)
//                {
//                    if(!contents.isEmpty())
//                        contents += "\n";
//                    contents += OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DESTINATION) + ": " + gateway.getDestination() + " " + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_GATEWAY) + ": " + gateway.getGateway() + " " + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_INTERFACE) + ": " + gateway.getInterfaceName();
//                }
//                gwInfo.setContents(contents);
//            }
//
//            retVal.setInterfaceInfo(intInfo);
//            retVal.setGatewayInfo(gwInfo);
//        }
//        catch(OBException e)
//        {
//            throw e;
//        }
//        catch(Exception e)
//        {
//            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
//        }
//        return retVal;
//    }
//
//    private OBDtoRptSysInfoL2 getSysInfoL2PASK(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBAdcPASKHandler handler) throws OBException
//    {// L2 info 추출.
//        OBCLIParserPASK parserClass = new OBCLIParserPASK();
//        OBDtoRptSysInfoL2 retVal = new OBDtoRptSysInfoL2();
//        String contents = "";
//        try
//        {
//            ArrayList<OBDtoPortInfoPASK> portUpdownList = null;
//            ArrayList<DtoRptTrunkInfoPASK> trunkList = null;
//            DtoRptStpInfoPASK stpInfoPASK = null;
//            try
//            {
//                String cfg = handler.cmndPortInfo();
//                portUpdownList = parserClass.parsePortUpdown(cfg);
//                cfg = handler.cmndShowTrunkInfo();
//                trunkList = parserClass.parseTrunkInfo(cfg);
//                cfg = handler.cmndShowStpInfo();
//                stpInfoPASK = parserClass.parseStpInfo(cfg);
//            }
//            catch(OBException e)
//            {
//                OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get getRptOPL2Info.%s", e.getErrorMessage()));
//            }
//
//            // link up 정보.
//            OBDtoRptSysInfo linkupInfo = new OBDtoRptSysInfo();
//            contents = "";
//            linkupInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_LINKUP));// OBTerminologyDB.RPT_L2_LINKUP);
//            HashMap<String, String> portNameMap = new HashMap<String, String>();
//            if(portUpdownList != null)
//            {
//                int linkupCnt = 0;
//                int linkTotal = 0;
//                for(OBDtoPortInfoPASK linkup : portUpdownList)
//                {
//                    linkTotal++;
//                    if(linkup.getLinkStatus() == OBDefine.L2_LINK_STATUS_UP)
//                    {
//                        linkupCnt++;
//                        if(!contents.isEmpty())
//                            contents += ", ";
//                        contents += String.format("%s", linkup.getPortName());
//                        portNameMap.put(linkup.getPortName(), linkup.getPortName());
//                    }
//                }
//                linkupInfo.setResult(String.format("%d/%d %s", linkupCnt, linkTotal, OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_COUNT)));// OBTerminologyDB.TYPE_GENERAL_COUNT));
//                linkupInfo.setContents(contents);
//            }
//            retVal.setLinkup(linkupInfo);
//
//            // port 상태 정보.
//            OBDtoRptSysInfo portInfo = new OBDtoRptSysInfo();
//            contents = "";
//            portInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_PORTSTATUS));// OBTerminologyDB.RPT_L2_PORTSTATUS);
//            if(portNameMap.size() > 0)
//            {
//                String dumpInfo = handler.cmndPortStatistics();
//                ArrayList<OBDtoPortStatPASK> portStatList = parserClass.parsePortStatistics(dumpInfo);
//                HashMap<String, OBDtoRptPortInfo> oldPortStatsMap = getPortInfo(adcInfo, new ArrayList<String>(portNameMap.values()), rptInfo.getBeginTime());
//                portInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
//                for(OBDtoPortStatPASK portStat : portStatList)
//                {
//                    if(portNameMap.get(portStat.getPortName()) == null)
//                        continue;
//                    long diffDiscards = 0;
//                    long diffErrors = 0;
//                    long currDiscards = portStat.getRxDiscards() + portStat.getTxDiscards();
//                    long currErrors = portStat.getRxErrros() + portStat.getTxErrros();
//                    OBDtoRptPortInfo oldPortStats = oldPortStatsMap.get(portStat.getPortName());
//                    if(oldPortStats != null && oldPortStats.getErrDiscardsList() != null && oldPortStats.getErrDiscardsList().size() > 0)
//                    {
//                        diffDiscards = Math.abs(currDiscards - oldPortStats.getErrDiscardsList().get(0).getDiscards());
//                        diffErrors = Math.abs(currErrors - oldPortStats.getErrDiscardsList().get(0).getErrors());
//
//                    }
//                    contents += String.format("%-6s: %s = %d(%d)\n", portStat.getPortName(), OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DISCARDS), diffDiscards, currDiscards);
//                    contents += String.format("       : %s = %d(%d)\n", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ERRORS), diffErrors, currErrors);
//
//                    if(diffDiscards != 0)
//                        portInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL));// OBTerminologyDB.TYPE_GENERAL_ABNORMAL);
//                }
//                portInfo.setContents(contents);
//            }
//            retVal.setPortStatus(portInfo);
//
//            // vlan 정보.
//            String infoDump = handler.cmndVlanInfo();
//            ArrayList<OBDtoVLanInfoPASK> vlanList = parserClass.parseVlanInfo(infoDump);
//
//            OBDtoRptSysInfo vlanInfo = new OBDtoRptSysInfo();
//            contents = "";
//            vlanInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_VLANINFO));// OBTerminologyDB.RPT_L2_VLANINFO);
//            if(vlanList != null)
//            {
//                vlanInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NORMAL));// OBTerminologyDB.TYPE_GENERAL_NORMAL);
//                for(OBDtoVLanInfoPASK vlan : vlanList)
//                {
//                    if(vlan.getStatus() != OBDefine.L2_VLAN_STATE_ENABLED)
//                        continue;
//                    String portListTag = "";
//                    for(String portNum : vlan.getTaggedPortList())
//                    {
//                        if(!portListTag.isEmpty())
//                            portListTag += ", ";
//                        portListTag += String.format("%s", portNum);
//                    }
//                    String portListUntag = "";
//                    for(String portNum : vlan.getUntaggedPortList())
//                    {
//                        if(!portListUntag.isEmpty())
//                            portListUntag += ", ";
//                        portListUntag += String.format("%s", portNum);
//                    }
//                    String portListUnavaliTag = "";
//                    for(String portNum : vlan.getUnavailabledPortList())
//                    {
//                        if(!portListUnavaliTag.isEmpty())
//                            portListUnavaliTag += ", ";
//                        portListUnavaliTag += String.format("%s", portNum);
//                    }
//                    if(!contents.isEmpty())
//                        contents += "\n";
//
//                    String portList = "";
//                    if(!portListTag.isEmpty())
//                    {
//                        if(!portList.isEmpty())
//                            portList += ", ";
//                        portList += String.format("%s:%s", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TAGPORT), portListTag);
//                    }
//                    if(!portListUntag.isEmpty())
//                    {
//                        if(!portList.isEmpty())
//                            portList += ", ";
//                        portList += String.format("%s:%s", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_UNTAGPORT), portListUntag);
//                    }
//                    if(!portListUnavaliTag.isEmpty())
//                    {
//                        if(!portList.isEmpty())
//                            portList += ", ";
//                        portList += String.format("%s:%s", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_UNAVAILPORT), portListUnavaliTag);
//                    }
//                    contents += String.format("%s: %d, %s: %s, %s", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ID), vlan.getId(), OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NAME), vlan.getName(), portList);
//                }
//                vlanInfo.setContents(contents);
//            }
//            retVal.setVlanInfo(vlanInfo);
//            // stp 정보.
//            OBDtoRptSysInfo stpInfo = new OBDtoRptSysInfo();
//            contents = "";
//            stpInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_STPINFO));// OBTerminologyDB.RPT_L2_STPINFO);
//            if(stpInfoPASK != null)
//            {
//                if(stpInfoPASK.getState() == OBDefine.L2_STP_STATE_ENABLED)
//                {
//                    stpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
//                    contents = stpInfoPASK.getPortList();
//                    // // blocking 포트 리스트만 제공.
//                    // for(DtoRptStgStatus stgStatus:stpStatus.getStgList())
//                    // {
//                    // if(stgStatus.getStatus()!=OBDefine.L2_STG_STATUS_BLOCKING)
//                    // continue;
//                    // if(!contents.isEmpty())
//                    // contents += ", ";
//                    // contents += stpInfo.gestgStatus.getIndex();
//                    // }
//                    // if(!contents.isEmpty())
//                    // contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_BLOCKING) + ": " + contents;
//                }
//                else
//                    stpInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_NOTUSED));// OBTerminologyDB.TYPE_GENERAL_NOTUSED);
//                stpInfo.setContents(contents);
//            }
//
//            // trunk 정보. 사용중인 trunk 정보만 제공.
//            OBDtoRptSysInfo trunkInfo = new OBDtoRptSysInfo();
//            contents = "";
//            trunkInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L2_TRUNKINFO));// OBTerminologyDB.RPT_L2_STPINFO);
//            if(trunkList != null && trunkList.size() > 0)
//            {
//                trunkInfo.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_USED));// OBTerminologyDB.TYPE_GENERAL_USED);
//                for(DtoRptTrunkInfoPASK objInfo : trunkList)
//                {
//                    if(!contents.isEmpty())
//                        contents += "\n";
//                    contents += String.format("%s:%s, %s:%s, %s:%s", OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TRUNK_NAME), objInfo.getName(), OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TRUNK_ALG), objInfo.getAlgorithm(), OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TRUNK_PORT), objInfo.getPortList());
//                }
//                if(!contents.isEmpty())
//                    contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TRUNK) + ": " + contents;
//                trunkInfo.setContents(contents);
//            }
//
//            retVal.setPortNameList(new ArrayList<String>(portNameMap.values()));
//        }
//        catch(OBException e)
//        {
//            throw e;
//        }
//        catch(Exception e)
//        {
//            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
//        }
//        return retVal;
//    }
//
//    private OBDtoRptSysInfoL4 getSysInfoL4PASK(OBDtoAdcInfo adcInfo, OBReportInfo rptInfo, OBAdcPASKHandler handler) throws OBException
//    {// L4 info 추출.
//        OBCLIParserPASK parserClass = new OBCLIParserPASK();
//        OBDtoRptSysInfoL4 retVal = new OBDtoRptSysInfoL4();
//        String contents = "";
//        String result = "";
//        String infoDump = "";
//        try
//        {
//            infoDump = handler.cmndShowInfoSlb();
//            ArrayList<OBDtoAdcVServerPASK> vsList = parserClass.parseSlbStatus(adcInfo.getIndex(), infoDump);
//
//            // pool member 상태 정보
//            OBDtoRptSysInfo poolMemInfo = new OBDtoRptSysInfo();
//            contents = "";
//            result = "";
//            poolMemInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_PMSTAT));// OBTerminologyDB.RPT_L4_PMSTAT);
//            if(vsList != null)
//            {
//                long pmTotal = 0;// pmList.size();
//                long pmAbnormal = 0;
//                String tmpContent = "";
//                for(OBDtoAdcVServerPASK vsObj : vsList)
//                {
//                    vsObj.setStatus(OBDefine.STATUS_AVAILABLE);
//                    if(vsObj.getPool() == null)
//                        continue;
//                    if(vsObj.getPool().getMemberList() == null)
//                        continue;
//                    for(OBDtoAdcPoolMemberPASK memObj : vsObj.getPool().getMemberList())
//                    {
//                        pmTotal++;
//                        if(memObj.getStatus() != OBDefine.STATUS_AVAILABLE)
//                            pmAbnormal++;
//                        else
//                            vsObj.setStatus(OBDefine.STATUS_AVAILABLE);
//                        if(!tmpContent.isEmpty())
//                            tmpContent += ", ";
//                        tmpContent += memObj.getIpAddress();
//                    }
//                }
//
//                result = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL) + ": " + pmTotal + "\n" + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + ": " + pmAbnormal;
//                poolMemInfo.setResult(result);
//                contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + "(" + pmAbnormal + ")" + ": " + tmpContent;
//                poolMemInfo.setContents(contents);
//            }
//            retVal.setPoolMemberStatus(poolMemInfo);
//
//            // vs 상태 정보.
//            OBDtoRptSysInfo vsInfo = new OBDtoRptSysInfo();
//            contents = "";
//            result = "";
//            vsInfo.setName(OBMessages.getMessage(OBMessages.MSG_RPT_L4_VSSTAT));// OBTerminologyDB.RPT_L4_VSSTAT);
//            if(vsList != null)
//            {
//                long vsTotal = 0;// vsList.size();
//                long vsAbnormal = 0;
//                String tmpContent = "";
//                for(OBDtoAdcVServerPASK vsObj : vsList)
//                {
//                    vsTotal++;
//                    if(vsObj.getStatus() != OBDefine.STATUS_AVAILABLE)
//                        vsAbnormal++;
//                    if(!tmpContent.isEmpty())
//                        tmpContent += ", ";
//                    tmpContent += vsObj.getvIP();
//                }
//                result = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_TOTAL) + ": " + vsTotal + "\n" + OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + ": " + vsAbnormal;
//                vsInfo.setResult(result);
//                contents = OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_ABNORMAL) + "(" + vsAbnormal + ")" + ": " + tmpContent;
//                vsInfo.setContents(contents);
//            }
//            retVal.setVsStatus(vsInfo);
//
//            // connection 상태 정보
//
//            // direct 기능 상태 정보.
//        }
//        catch(OBException e)
//        {
//            throw e;
//        }
//        catch(Exception e)
//        {
//            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
//        }
//        // System.out.println(retVal);
//        return retVal;
//    }

}
