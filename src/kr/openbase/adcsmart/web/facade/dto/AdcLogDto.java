package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAuditLogAdcSystem;

public class AdcLogDto {
	private Integer log_seq;
	private Date occur_time;
	private Integer adc_index;
	private String adc_name;
	private String adc_ipaddress;
	private String type;
	private String log_level;
	private String content;
	private String content_extra;
	private String content_view;
	private Integer	objectType;
	
	
	// 영문 Log를 원할때
	public static AdcLogDto toAdcLogDto(OBDtoAuditLogAdcSystem systemLogFromSvc) {
		AdcLogDto adcLog = new AdcLogDto();
		adcLog.setOccur_time(systemLogFromSvc.getOccurTime());
		adcLog.setAdc_index(systemLogFromSvc.getAdcIndex());
		adcLog.setAdc_name(systemLogFromSvc.getAdcName());
		adcLog.setAdc_ipaddress(systemLogFromSvc.getAdcIPAddress());
		//adcLog.setType(systemLogFromSvc.getType());
		adcLog.setLog_level(systemLogFromSvc.getLevel());
		adcLog.setContent(systemLogFromSvc.getContents());	
		adcLog.setContent_extra(systemLogFromSvc.getContents_extra());
		adcLog.setContent_view(systemLogFromSvc.getContents());		
		return adcLog;
	}
	
	// 한글 log를 원할때
	public static AdcLogDto toAdcLogDtoExtra(OBDtoAuditLogAdcSystem systemLogFromSvc) {
		AdcLogDto adcLog = new AdcLogDto();
		adcLog.setOccur_time(systemLogFromSvc.getOccurTime());
		adcLog.setAdc_index(systemLogFromSvc.getAdcIndex());
		adcLog.setAdc_name(systemLogFromSvc.getAdcName());
		adcLog.setAdc_ipaddress(systemLogFromSvc.getAdcIPAddress());
		adcLog.setType(systemLogFromSvc.getType());
		adcLog.setLog_level(systemLogFromSvc.getLevel());
		adcLog.setContent(systemLogFromSvc.getContents());	
		adcLog.setContent_extra(systemLogFromSvc.getContents_extra());
		if (systemLogFromSvc.getContents_extra() == null || systemLogFromSvc.getContents_extra() == "")
		{
			adcLog.setContent_view(systemLogFromSvc.getContents());
		}
		else
		{
			adcLog.setContent_view(systemLogFromSvc.getContents_extra());
		}
		return adcLog;
	}
	
	public static List<AdcLogDto> toAdcLogDto(List<OBDtoAuditLogAdcSystem> systemLogsFromSvc, Integer extraContentKey) {
		List<AdcLogDto> adcLogs = new ArrayList<AdcLogDto>();
		for (OBDtoAuditLogAdcSystem e : systemLogsFromSvc)
			adcLogs.add(AdcLogDto.toAdcLogDto(e));
		/*if(extraContentKey == 0) // 영문 Log만 원할때
		{
			for (OBDtoAuditLogAdcSystem e : systemLogsFromSvc)
				adcLogs.add(AdcLogDto.toAdcLogDto(e));
		}
		else // 국문으로 존재하는 log는 국문으로 보여지길 원할때
		{
			for (OBDtoAuditLogAdcSystem e : systemLogsFromSvc)
				adcLogs.add(AdcLogDto.toAdcLogDtoExtra(e));
		}*/
		
		return adcLogs;
	}

	public Integer getLog_seq()
	{
		return log_seq;
	}

	public void setLog_seq(Integer log_seq)
	{
		this.log_seq = log_seq;
	}

	public Date getOccur_time()
	{
		return occur_time;
	}

	public void setOccur_time(Date occur_time)
	{
		this.occur_time = occur_time;
	}

	public Integer getAdc_index()
	{
		return adc_index;
	}

	public void setAdc_index(Integer adc_index)
	{
		this.adc_index = adc_index;
	}

	public String getAdc_name()
	{
		return adc_name;
	}

	public void setAdc_name(String adc_name)
	{
		this.adc_name = adc_name;
	}

	public String getAdc_ipaddress()
	{
		return adc_ipaddress;
	}

	public void setAdc_ipaddress(String adc_ipaddress)
	{
		this.adc_ipaddress = adc_ipaddress;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getLog_level()
	{
		return log_level;
	}

	public void setLog_level(String log_level)
	{
		this.log_level = log_level;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getContent_extra()
	{
		return content_extra;
	}

	public void setContent_extra(String content_extra)
	{
		this.content_extra = content_extra;
	}

	public Integer getObjectType()
	{
		return objectType;
	}

	public void setObjectType(Integer objectType)
	{
		this.objectType = objectType;
	}	

	public String getContent_view()
	{
		return content_view;
	}

	public void setContent_view(String content_view)
	{
		this.content_view = content_view;
	}
	@Override
	public String toString()
	{
		return "AdcLogDto [log_seq=" + log_seq + ", occur_time=" + occur_time + ", adc_index=" + adc_index + ", adc_name=" + adc_name + ", adc_ipaddress=" + adc_ipaddress + ", type=" + type + ", log_level=" + log_level + ", content=" + content + ", content_extra=" + content_extra + ", content_view=" + content_view + ", objectType=" + objectType + "]";
	}
}