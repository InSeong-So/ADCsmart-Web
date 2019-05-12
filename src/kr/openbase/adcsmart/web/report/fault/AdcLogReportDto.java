package kr.openbase.adcsmart.web.report.fault;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.report.OBDtoRptAdcLogInfo;
import kr.openbase.adcsmart.service.utility.OBMessages;

public class AdcLogReportDto 
{
	private String adcName;
	private String adcIp;
	private Integer occurCount;
	private List<AdcLogDto> adcLogs;
	
	private AdcLogReportDtoTextHdr textHdr;
	
	public static AdcLogReportDto toAdcLogReportDto(OBDtoRptAdcLogInfo adcLogFromSvc) 
	{
		AdcLogReportDto adcLog = new AdcLogReportDto();
		adcLog.setAdcName(adcLogFromSvc.getAdcName());
		adcLog.setAdcIp(adcLogFromSvc.getAdcIPAddress());
		
		AdcLogReportDtoTextHdr textHdr = new AdcLogReportDtoTextHdr();
		textHdr.setTitle(OBMessages.getMessage(OBMessages.MSG_RPT_ADC_LOG_TITLE));//"ADC 로그 분석");
		textHdr.setAdcName(OBMessages.getMessage(OBMessages.MSG_RPT_ADC_LOG_ADCNAME));//"ADC 이름");
		textHdr.setOccurTime(OBMessages.getMessage(OBMessages.MSG_RPT_ADC_LOG_OCCURTIME));//"발생 건수");

		adcLog.setTextHdr(textHdr);
		
		adcLog.setAdcLogs(AdcLogDto.toAdcLogDto(adcLogFromSvc.getLogList()));
		return adcLog;
	}
	
	public static List<AdcLogReportDto> toAdcLogReportDto(List<OBDtoRptAdcLogInfo> adcLogsFromSvc) 
	{
		if (adcLogsFromSvc == null)
			return null;
		
		List<AdcLogReportDto> adcLogs = new ArrayList<AdcLogReportDto>();
		for (OBDtoRptAdcLogInfo e : adcLogsFromSvc)
			adcLogs.add(toAdcLogReportDto(e));
		
		return adcLogs;
	}
	
	public AdcLogReportDtoTextHdr getTextHdr()
	{
		return textHdr;
	}

	public void setTextHdr(AdcLogReportDtoTextHdr textHdr)
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

	public List<AdcLogDto> getAdcLogs() 
	{
		return adcLogs;
	}

	public void setAdcLogs(List<AdcLogDto> adcLogs) 
	{
		this.adcLogs = adcLogs;
	}

	@Override
	public String toString() 
	{
		return "AdcLogReportDto [adcName=" + adcName + ", adcIp=" + adcIp + ", occurCount=" + occurCount + ", adcLogs="
				+ adcLogs + "]";
	}
	
	
}
