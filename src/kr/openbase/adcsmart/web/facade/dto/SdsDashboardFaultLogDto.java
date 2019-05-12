package kr.openbase.adcsmart.web.facade.dto;

import java.text.SimpleDateFormat;

import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsIssueSummary;

public class SdsDashboardFaultLogDto 
{	
	private Long id;
	private Integer type;
	private Integer adcIndex;
	private String adcName;
	private Integer adcType;
	private String vsIp;
	private String vsIndex;
	private String memberIp;
	private String interfaceNumber;
	private String occurredTime;
	private Integer level;
	private Integer status;
	private Integer cpuUsage;
	private Integer memoryUsage;	
	
	private String event;
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public Integer getAdcType()
	{
		return adcType;
	}
	public void setAdcType(Integer adcType)
	{
		this.adcType = adcType;
	}
	public String getVsIp()
	{
		return vsIp;
	}
	public void setVsIp(String vsIp)
	{
		this.vsIp = vsIp;
	}
	public String getVsIndex()
	{
		return vsIndex;
	}
	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
	}
	public String getMemberIp()
	{
		return memberIp;
	}
	public void setMemberIp(String memberIp)
	{
		this.memberIp = memberIp;
	}

	public String getInterfaceNumber()
	{
		return interfaceNumber;
	}
	public void setInterfaceNumber(String interfaceNumber)
	{
		this.interfaceNumber = interfaceNumber;
	}
	public String getOccurredTime()
	{
		return occurredTime;
	}
	public void setOccurredTime(String occurredTime)
	{
		this.occurredTime = occurredTime;
	}
	public Integer getLevel()
	{
		return level;
	}
	public void setLevel(Integer level)
	{
		this.level = level;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public Integer getCpuUsage()
	{
		return cpuUsage;
	}
	public void setCpuUsage(Integer cpuUsage)
	{
		this.cpuUsage = cpuUsage;
	}
	public Integer getMemoryUsage()
	{
		return memoryUsage;
	}
	public void setMemoryUsage(Integer memoryUsage)
	{
		this.memoryUsage = memoryUsage;
	}
	public String getEvent()
	{
		return event;
	}
	public void setEvent(String event)
	{
		this.event = event;
	}
	@Override
	public String toString()
	{
		return "SdsDashboardFaultLogDto [id=" + id + ", type=" + type
				+ ", adcIndex=" + adcIndex + ", adcName=" + adcName
				+ ", adcType=" + adcType + ", vsIp=" + vsIp + ", vsIndex="
				+ vsIndex + ", memberIp=" + memberIp + ", interfaceNumber="
				+ interfaceNumber + ", occurredTime=" + occurredTime
				+ ", level=" + level + ", status=" + status + ", cpuUsage="
				+ cpuUsage + ", memoryUsage=" + memoryUsage + ", evnet=" + event + "]";
	}
	
	public static SdsDashboardFaultLogDto getSdsDashboardFaultLog(
			OBDtoDashboardSdsIssueSummary obDtoDashboardSdsIssueSummary) 
	{
		SdsDashboardFaultLogDto faultLog = new SdsDashboardFaultLogDto();
		faultLog.setId(obDtoDashboardSdsIssueSummary.getIssueIndex());
		faultLog.setType(obDtoDashboardSdsIssueSummary.getIssueType());
		faultLog.setAdcIndex(obDtoDashboardSdsIssueSummary.getAdcIndex());
		faultLog.setAdcName(obDtoDashboardSdsIssueSummary.getAdcName());
		faultLog.setAdcType(obDtoDashboardSdsIssueSummary.getAdcType());
		faultLog.setVsIp(obDtoDashboardSdsIssueSummary.getVservIp());
		faultLog.setVsIndex(obDtoDashboardSdsIssueSummary.getVservInex());
		faultLog.setMemberIp(obDtoDashboardSdsIssueSummary.getMemberIp());
		faultLog.setInterfaceNumber(obDtoDashboardSdsIssueSummary.getInterfaceNumber());
		if (null != obDtoDashboardSdsIssueSummary.getOccurTime()) 
		{
			faultLog.setOccurredTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
					obDtoDashboardSdsIssueSummary.getOccurTime().getTime()));
		}
		faultLog.setLevel(obDtoDashboardSdsIssueSummary.getIssueLevel());
		faultLog.setStatus(obDtoDashboardSdsIssueSummary.getStatus());
		faultLog.setCpuUsage(obDtoDashboardSdsIssueSummary.getCpu());
		faultLog.setMemoryUsage(obDtoDashboardSdsIssueSummary.getMemory());
		faultLog.setEvent(obDtoDashboardSdsIssueSummary.getEvent());
		return faultLog;
	}
	
}
