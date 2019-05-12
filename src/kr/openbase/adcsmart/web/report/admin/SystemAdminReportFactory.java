package kr.openbase.adcsmart.web.report.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.openbase.adcsmart.service.OBReportOperation;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptAdcInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptPortInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptSystemInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptTitle;
import kr.openbase.adcsmart.service.impl.report.OBReportOperationImpl;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.fault.AdcDto;
import kr.openbase.adcsmart.web.report.impl.SystemReportDto;

public class SystemAdminReportFactory {
	private static transient Logger log = LoggerFactory.getLogger(SystemAdminReportFactory.class);

//	public static void main(String args[]) throws Exception 
//	{
//		List<SystemReportDto> systemReports = getSystemReport("1392791615158");
//		System.out.println(systemReports);
//		
//		List<SystemAdminSummaryDto> summaryList = getSystemAdminSummary("1392791615158");
//		for(SystemAdminSummaryDto info:summaryList)
//			System.out.println(info);
//		SystemStatusDto sysStatus = getSystemStatus("1392791615158", 1);
//		System.out.println(sysStatus);
//		
//		AdcInfoDto adcInfo = getAdc("1392791615158", 1);
//		System.out.println(adcInfo);
//		
////		AdcInfoDto adcInfo = getAdc("1392791615158", 1);
////		System.out.println(adcInfo);
////		"1392791615158"
////		if (!CollectionUtils.isEmpty(systemReports) && !CollectionUtils.isEmpty(systemReports.get(0).getAdcs())) 
////		{
////			List<SystemAdminSummaryDto> summaryList = getSystemAdminSummary("1353313205419", systemReports.get(0).getAdcs());
////			System.out.println(summaryList);
////		}
//	}

	public static List<SystemReportDto> getSystemReport() throws Exception {
		Class.forName("org.postgresql.Driver");
		OBReportOperation reportSvc = new OBReportOperationImpl();
		OBDtoRptTitle rptTitle = reportSvc.getTitle("1353313205419");
		SystemReportDto report = SystemReportDto.toSystemReportDto(rptTitle);
		List<SystemReportDto> reports = new ArrayList<SystemReportDto>();
		reports.add(report);
		return reports;
	}

	public static List<SystemReportDto> getSystemReport(String index) throws Exception {
		Class.forName("org.postgresql.Driver");
		OBReportOperation reportSvc = new OBReportOperationImpl();
		OBDtoRptTitle rptTitle = reportSvc.getTitle(index);
		SystemReportDto report = SystemReportDto.toSystemReportDto(rptTitle);
		List<SystemReportDto> reports = new ArrayList<SystemReportDto>();
		reports.add(report);
		return reports;
	}

	public static List<SystemAdminSummaryDto> getSystemAdminSummary(String index) throws Exception {
		try {
			List<SystemReportDto> systemReports = getSystemReport(index);

			if (!CollectionUtils.isEmpty(systemReports) && !CollectionUtils.isEmpty(systemReports.get(0).getAdcs())) {
				List<SystemAdminSummaryDto> summaryList = getSystemAdminSummary(index, systemReports.get(0).getAdcs());

				return summaryList;
			}
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get report data(SystemAdminSummary). error:%s", e.getMessage()));
			throw e;
		}
		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("invalid report data"));
		return null;
	}

	public static List<SystemAdminSummaryDto> getSystemAdminSummary(String reportIndex, List<AdcDto> adcs)
			throws Exception {
		try {
			log.debug("reportIndex:{}, adcs:{}", reportIndex, adcs);
			Class.forName("org.postgresql.Driver");
			List<SystemAdminSummaryDto> summaryList = new ArrayList<SystemAdminSummaryDto>();
			for (AdcDto adc : adcs) {
				SystemAdminSummaryDto summary = new SystemAdminSummaryDto();

				SystemAdminSummaryDtoTextHdr textHdr = new SystemAdminSummaryDtoTextHdr();

				textHdr.setTitle(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_TITLE));// "생성 시간");
				textHdr.setAdcIP(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_ADCIP));// "ADC IP");
				textHdr.setAdcName(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_ADCNAME));// "ADC 이름");
				textHdr.setAdcInfo(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_ADCINFO));// "ADC 정보");
				textHdr.setHostName(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_HOSTNAME));// "Host 이름");
				textHdr.setAdcModel(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_ADCMODEL));// "ADC 모델");
				textHdr.setOsVersion(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_OSVERSION));// "OS 버전");
				textHdr.setLicenseInfo(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_LICENSEINFO));// "라이센스
																											// 정보");
				textHdr.setSerialNo(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_SERIALNO));// "Serail 정보");
				textHdr.setMacAddr(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_MACADDR));// "MAC 주소");
				textHdr.setActiveStandby(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_ACTIVESTANDBY));// "Active/Standby");
				textHdr.setRunningTime(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_RUNNINGTIME));// "운영 시간");
				textHdr.setSystemInfo(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_SYSTEMINFO));// "시스템 상태
																											// 정보");
				summary.setTextHdr(textHdr);

				summary.setAdc(getAdc(reportIndex, adc.getIndex()));
				summary.setSystemStatus(getSystemStatus(reportIndex, adc.getIndex()));

				summary.setPortStatuses(
						getPortStatus(reportIndex, adc.getIndex(), summary.getSystemStatus().getL2().getPortNames()));
				summaryList.add(summary);
			}

			return summaryList;
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get report data(SystemAdminSummary). error:%s", e.getMessage()));
			throw e;
		}
	}

	public static AdcInfoDto getAdc(String reportIndex, int adcIndex) throws Exception {
		try {
			log.debug("reportIndex:{}, adcIndex:{}", reportIndex, adcIndex);
			OBReportOperation reportSvc = new OBReportOperationImpl();
			OBDtoRptAdcInfo adcFromSvc = reportSvc.getAdcInfo(reportIndex, adcIndex);
			log.debug("{}", adcFromSvc);
			return AdcInfoDto.toAdcDto(adcFromSvc);
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get adcInfo. reportIndex:%s, adcIndex:%d. error:%s", reportIndex, adcIndex,
							e.getMessage()));
			throw e;
		}
	}

//	public static void main(String args[]) throws Exception 
//	{
//		SystemStatusDto systemReports = getSystemStatus("1392957553015", 3);
//		
////		List<SystemAdminSummaryDto> summaryList = getSystemAdminSummary("1392791615158");
////		for(SystemAdminSummaryDto info:summaryList)
////			System.out.println(info);
////		SystemStatusDto sysStatus = getSystemStatus("1392791615158", 1);
////		System.out.println(sysStatus);
////		
////		AdcInfoDto adcInfo = getAdc("1392791615158", 1);
////		System.out.println(adcInfo);
//		
////		AdcInfoDto adcInfo = getAdc("1392791615158", 1);
////		System.out.println(adcInfo);
////		"1392791615158"
////		if (!CollectionUtils.isEmpty(systemReports) && !CollectionUtils.isEmpty(systemReports.get(0).getAdcs())) 
////		{
////			List<SystemAdminSummaryDto> summaryList = getSystemAdminSummary("1353313205419", systemReports.get(0).getAdcs());
////			System.out.println(summaryList);
////		}
//	}
//	
	public static SystemStatusDto getSystemStatus(String reportIndex, int adcIndex) throws Exception {
		try {
			OBReportOperation reportSvc = new OBReportOperationImpl();
			OBDtoRptSystemInfo statusFromSvc = reportSvc.getSystemInfo(reportIndex, adcIndex);
			return SystemStatusDto.toSystemStatusDto(statusFromSvc);
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get system status. reportIndex:%s, adcIndex:%d. error:%s", reportIndex,
							adcIndex, e.getMessage()));
			throw e;
		}
	}

	public static List<PortStatusDto> getPortStatus(String reportIndex, int adcIndex, List<String> portNames)
			throws Exception {
		try {
			OBReportOperation reportSvc = new OBReportOperationImpl();
			List<OBDtoRptPortInfo> statusesFromSvc = reportSvc.getPortInfo(reportIndex, adcIndex,
					new ArrayList<String>(portNames));
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
					String.format("statusesFromSvc. portNames:%s, data:%s", portNames, statusesFromSvc));
			return PortStatusDto.toPortStatusDto(statusesFromSvc);
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to get port status. reportIndex:%s, adcIndex:%d. error:%s", reportIndex,
							adcIndex, e.getMessage()));
			throw e;
		}
	}
}
