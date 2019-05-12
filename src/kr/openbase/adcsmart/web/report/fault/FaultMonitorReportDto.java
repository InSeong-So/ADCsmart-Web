package kr.openbase.adcsmart.web.report.fault;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.report.OBDtoRptAdcFaultInfo;
import kr.openbase.adcsmart.service.utility.OBMessages;

public class FaultMonitorReportDto 
{
	private String adcName;
	private String adcIp;
	private Integer occurCount;
	private List<AdcSystemLogDto> systemLogs;
	
	private FaultMonitorReportDtoTextHdr textHdr;
	
	public static FaultMonitorReportDto toFaultMonitorReportDto(OBDtoRptAdcFaultInfo faultInfoFromSvc) 
	{
		FaultMonitorReportDto faultMonitor = new FaultMonitorReportDto();
		faultMonitor.setAdcName(faultInfoFromSvc.getAdcName());
		faultMonitor.setAdcIp(faultInfoFromSvc.getAdcIPAddress());
		FaultMonitorReportDtoTextHdr textHdr = new FaultMonitorReportDtoTextHdr();
		textHdr.setTitle(OBMessages.getMessage(OBMessages.MSG_RPT_FAULT_MONITOR_TITLE));//"장애 모니터링");
		textHdr.setAdcName(OBMessages.getMessage(OBMessages.MSG_RPT_FAULT_MONITOR_ADCNAME));//"ADC 이름");
		textHdr.setOccurTime(OBMessages.getMessage(OBMessages.MSG_RPT_FAULT_MONITOR_OCCURTIME));//"발생 건수");

		faultMonitor.setTextHdr(textHdr);
		faultMonitor.setSystemLogs(AdcSystemLogDto.toAdcSystemLogDto(faultInfoFromSvc.getLogList()));
		return faultMonitor;
	}
	
	public static List<FaultMonitorReportDto> toFaultMonitorReportDto(List<OBDtoRptAdcFaultInfo> faultInfosFromSvc) 
	{
		if (faultInfosFromSvc == null)
			return null;
		
		List<FaultMonitorReportDto> faultMonitors = new ArrayList<FaultMonitorReportDto>();
		for (OBDtoRptAdcFaultInfo e : faultInfosFromSvc)
			faultMonitors.add(toFaultMonitorReportDto(e));
		
		return faultMonitors;
	}
	
	public FaultMonitorReportDtoTextHdr getTextHdr()
	{
		return textHdr;
	}

	public void setTextHdr(FaultMonitorReportDtoTextHdr textHdr)
	{
		this.textHdr = textHdr;
	}

	public String getAdcName() 
	{
		return adcName;
	}
	public void setAdcName(String adcName) 
	{
		this.adcName = adcName;
	}
	public String getAdcIp() 
	{
		return adcIp;
	}
	public void setAdcIp(String adcIp) 
	{
		this.adcIp = adcIp;
	}
	public Integer getOccurCount() 
	{
		return occurCount;
	}
	public void setOccurCount(Integer occurCount) 
	{
		this.occurCount = occurCount;
	}
	public List<AdcSystemLogDto> getSystemLogs() 
	{
		return systemLogs;
	}
	public void setSystemLogs(List<AdcSystemLogDto> systemLogs) 
	{
		this.systemLogs = systemLogs;
	}
	@Override
	public String toString()
	{
		return "FaultMonitorReportDto [adcName=" + adcName + ", adcIp=" + adcIp + ", occurCount=" + occurCount + ", systemLogs=" + systemLogs + ", textHdr=" + textHdr + "]";
	}
	
}
