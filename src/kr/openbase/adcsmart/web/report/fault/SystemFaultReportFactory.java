package kr.openbase.adcsmart.web.report.fault;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.OBReportFault;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptAdcFaultInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptAdcLogInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptTitle;
import kr.openbase.adcsmart.service.impl.report.OBReportFaultImpl;
import kr.openbase.adcsmart.web.report.impl.SystemReportDto;
import kr.openbase.adcsmart.web.report.impl.SystemReportDtoTextHdr;

public class SystemFaultReportFactory {
//	public static void main(String args[]) throws Exception
//	{
//		List<SystemReportDto> systemReports = getSystemReport();
//		System.out.println(systemReports);
//		
//		List<FaultMonitorReportDto> faultMonitor = getFaultMonitor("1348641182654");
//		System.out.println(faultMonitor);
//		
//		List<AdcLogReportDto> adcLogs = getAdcLogs("1346745632044");
//		System.out.println(adcLogs);
//	}
//	
	public static List<SystemReportDto> getSystemReport() throws Exception {
		Class.forName("org.postgresql.Driver");
		OBReportFault reportFaultSvc = new OBReportFaultImpl();
		OBDtoRptTitle rptTitle = reportFaultSvc.getTitle("1348641182654");
		System.out.println(rptTitle);
		SystemReportDto report = SystemReportDto.toSystemReportDto(rptTitle);
		List<SystemReportDto> reports = new ArrayList<SystemReportDto>();
		reports.add(report);
		return reports;
	}

	public static List<SystemReportDto> getSystemReport(String index) throws Exception {
		Class.forName("org.postgresql.Driver");
		OBReportFault reportFaultSvc = new OBReportFaultImpl();
		OBDtoRptTitle rptTitle = reportFaultSvc.getTitle(index);
		SystemReportDto report = SystemReportDto.toSystemReportDto(rptTitle);
		List<SystemReportDto> reports = new ArrayList<SystemReportDto>();
		reports.add(report);
		return reports;
	}

	public static List<FaultMonitorReportDto> getFaultMonitor(String index) throws Exception {
		Class.forName("org.postgresql.Driver");
		OBReportFault reportFaultSvc = new OBReportFaultImpl();
		List<OBDtoRptAdcFaultInfo> faultMonitorFromSvc = reportFaultSvc.getAdcFaultList(index);
		List<FaultMonitorReportDto> faultMonitor = FaultMonitorReportDto.toFaultMonitorReportDto(faultMonitorFromSvc);
		return faultMonitor;
	}

	public static List<AdcLogReportDto> getAdcLogs(String index) throws Exception {
		OBReportFault reportFaultSvc = new OBReportFaultImpl();
		List<OBDtoRptAdcLogInfo> adcLogsFromSvc = reportFaultSvc.getAdcLogList(index);
		List<AdcLogReportDto> adcLogs = AdcLogReportDto.toAdcLogReportDto(adcLogsFromSvc);
		return adcLogs;
	}

	public static SystemReportDtoTextHdr getTextHeader() throws Exception {
		SystemReportDtoTextHdr retVal = new SystemReportDtoTextHdr();
		retVal.setOccurTime("보고서 생성시간임다.");
		return retVal;
	}

//	private static SystemReportDto sampleSystemReportDto() {
//		SystemReportDto report = new SystemReportDto();
//		report.setAccountId("id1");
//		report.setCreationTime(new Date());
//		report.setFromPeriod(new Date());
//		report.setToPeriod(new Date());
//		List<AdcDto> adcs = new ArrayList<AdcDto>();
//		AdcDto adc = new AdcDto();
//		adc.setIndex(1);
//		adc.setName("adc1");
//		adcs.add(adc);
//		adc = new AdcDto();
//		adc.setIndex(2);
//		adc.setName("adc2");
//		adcs.add(adc);
//		report.setAdcs(adcs);
//		return report;
//	}
//	
//	private static List<FaultMonitorReportDto> sampleFaultMonitors() {
//		FaultMonitorReportDto faultMonitor = new FaultMonitorReportDto();
//		faultMonitor.setAdcIp("ip1");
//		faultMonitor.setAdcName("name1");
//		faultMonitor.setOccurCount(3);
//		
//		List<AdcSystemLogDto> logs = new ArrayList<AdcSystemLogDto>();
//		AdcSystemLogDto log = new AdcSystemLogDto();
//		log.setAdcIndex(1);
//		log.setAdcName("name1");
//		log.setEvent("event");
//		log.setLogLevel(1);
//		log.setLogType(1);
//		log.setOccurredTime("2012-08-28");
//		log.setStatus(1);
//		log.setVsIndex("vsIndex");
//		logs.add(log);
//		
//		log.setAdcIndex(2);
//		log.setAdcName("name2");
//		log.setEvent("event2");
//		log.setLogLevel(2);
//		log.setLogType(2);
//		log.setOccurredTime("2012-08-27");
//		log.setStatus(2);
//		log.setVsIndex("vsIndex2");
//		logs.add(log);
//		
//		faultMonitor.setSystemLogs(logs);
//		List<FaultMonitorReportDto> faultMonitors = new ArrayList<FaultMonitorReportDto>();
//		faultMonitors.add(faultMonitor);
//		return faultMonitors;
//	}

}
