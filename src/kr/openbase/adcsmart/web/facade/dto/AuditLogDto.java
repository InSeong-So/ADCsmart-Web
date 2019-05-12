package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAuditLogAdcConfig;

public class AuditLogDto
{
	private Integer log_seq;
	private Date occur_time;
	private String generator;
	private String client_ip;
	private String type;
	private String level;
	private String content;
	
	public static AuditLogDto fromOBDtoAuditLogAdcConfig(OBDtoAuditLogAdcConfig configLogFromSvc)
	{
		AuditLogDto auditLog = new AuditLogDto();
		auditLog.setOccur_time(configLogFromSvc.getOccurTime());
		auditLog.setGenerator(configLogFromSvc.getAccountID());
		auditLog.setClient_ip(configLogFromSvc.getClientIPAddress());
		auditLog.setType(configLogFromSvc.getType());
		auditLog.setLevel(configLogFromSvc.getLevel());
		auditLog.setContent(configLogFromSvc.getContents());
		return auditLog;
	}
	
	public static List<AuditLogDto> fromOBDtoAuditLogAdcConfig(List<OBDtoAuditLogAdcConfig> configLogsFromSvc)
	{
		List<AuditLogDto> auditLogs = new ArrayList<AuditLogDto>();
		for (OBDtoAuditLogAdcConfig e : configLogsFromSvc)
		{
			auditLogs.add(AuditLogDto.fromOBDtoAuditLogAdcConfig(e));
		}	
		return auditLogs;
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
	public String getGenerator()
	{
		return generator;
	}
	public void setGenerator(String generator)
	{
		this.generator = generator;
	}
	public String getClient_ip()
	{
		return client_ip;
	}
	public void setClient_ip(String client_ip)
	{
		this.client_ip = client_ip;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getLevel()
	{
		return level;
	}
	public void setLevel(String level)
	{
		this.level = level;
	}
	public String getContent() 
	{
		return content;
	}
	public void setContent(String content) 
	{
		this.content = content;
	}
	@Override
	public String toString()
	{
		return "AuditLogDto [log_seq=" + log_seq + ", occur_time=" + occur_time + ", generator=" + generator
				+ ", client_ip=" + client_ip + ", type=" + type + ", level=" + level + ", content=" + content + "]";
	}
	
}
