package kr.openbase.adcsmart.web.report.fault;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAuditLogAdcSystem;
import kr.openbase.adcsmart.service.utility.OBMessages;

public class AdcLogDto 
{
	private Integer log_seq;
	private Date occur_time;
	private Integer adc_index;
	private String adc_name;
	private String adc_ipaddress;
	private String type;
	private String log_level;
	private String content;
	
	private AdcLogDtoTextHdr textHdr;
	
	public static AdcLogDto toAdcLogDto(OBDtoAuditLogAdcSystem systemLogFromSvc) 
	{
		AdcLogDto adcLog = new AdcLogDto();
		adcLog.setOccur_time(systemLogFromSvc.getOccurTime());
		adcLog.setAdc_index(systemLogFromSvc.getAdcIndex());
		adcLog.setAdc_name(systemLogFromSvc.getAdcName());
		adcLog.setAdc_ipaddress(systemLogFromSvc.getAdcIPAddress());
		adcLog.setType(systemLogFromSvc.getType());
		adcLog.setLog_level(systemLogFromSvc.getLevel());
		adcLog.setContent(systemLogFromSvc.getContents());
		
		AdcLogDtoTextHdr textHdr = new AdcLogDtoTextHdr();
		textHdr.setColumn1(OBMessages.getMessage(OBMessages.MSG_RPT_ADC_LOG_COLUMN1));//"생성 시간");
		textHdr.setColumn2(OBMessages.getMessage(OBMessages.MSG_RPT_ADC_LOG_COLUMN2));//"ADC 이름");
		textHdr.setColumn3(OBMessages.getMessage(OBMessages.MSG_RPT_ADC_LOG_COLUMN3));//"ADC IP");
		textHdr.setColumn4(OBMessages.getMessage(OBMessages.MSG_RPT_ADC_LOG_COLUMN4));//"중요도");
		textHdr.setColumn5(OBMessages.getMessage(OBMessages.MSG_RPT_ADC_LOG_COLUMN5));//"상세내용");
		adcLog.setTextHdr(textHdr);
		
		return adcLog;
	}
	
	public static List<AdcLogDto> toAdcLogDto(List<OBDtoAuditLogAdcSystem> systemLogsFromSvc) 
	{
		List<AdcLogDto> adcLogs = new ArrayList<AdcLogDto>();
		for (OBDtoAuditLogAdcSystem e : systemLogsFromSvc)
			adcLogs.add(AdcLogDto.toAdcLogDto(e));
		
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
	public void setOccur_time(Date occur_time) {
		
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

	public AdcLogDtoTextHdr getTextHdr()
	{
		return textHdr;
	}

	public void setTextHdr(AdcLogDtoTextHdr textHdr)
	{
		this.textHdr = textHdr;
	}

	@Override
	public String toString()
	{
		return "AdcLogDto [log_seq=" + log_seq + ", occur_time=" + occur_time + ", adc_index=" + adc_index
				+ ", adc_name=" + adc_name + ", adc_ipaddress=" + adc_ipaddress + ", type=" + type + ", log_level="
				+ log_level + ", content=" + content + "]";
	}
	
}
