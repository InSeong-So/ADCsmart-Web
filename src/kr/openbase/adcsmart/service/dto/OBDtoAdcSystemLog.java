package kr.openbase.adcsmart.service.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.utility.OBDefine;

public class OBDtoAdcSystemLog
{
	public static final int LOG_LEVEL_HIGH = 0;
	public static final int LOG_LEVEL_MIDDLE = 1;
	public static final int LOG_LEVEL_LOW = 2;
	
	public static final int LOG_STATUS_UNAVAILABLE = OBDefine.DATA_UNAVAILABLE;//0
	public static final int LOG_STATUS_AVAILABLE = OBDefine.DATA_AVAILABLE;//1
	
	private Integer logLevel;
	private Date	occurTime;
	private Date	finishTime; //장애 해결 시각. 미해결 장애에는 null
	private Integer logType;
	private Integer adcIndex;
	private String 	adcName;
	private String 	vsIndex;
//	private Integer 	servicePort;
	private String 	event;
	private Integer status;
	
	
	@Override
	public String toString()
	{
		return "OBDtoAdcSystemLog [logLevel=" + logLevel + ", occurTime="
				+ occurTime + ", finishTime=" + finishTime + ", logType="
				+ logType + ", adcIndex=" + adcIndex + ", adcName=" + adcName
				+ ", vsIndex=" + vsIndex + ", event=" + event + ", status="
				+ status + "]";
	}
	public Integer getLogLevel()
	{
		return logLevel;
	}
	public void setLogLevel(Integer logLevel)
	{
		this.logLevel = logLevel;
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Integer getLogType()
	{
		return logType;
	}
	public void setLogType(Integer logType)
	{
		this.logType = logType;
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
	public String getVsIndex()
	{
		return vsIndex;
	}
	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
	}
	public String getEvent()
	{
		return event;
	}
	public void setEvent(String event)
	{
		this.event = event;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public Date getFinishTime()
	{
		return finishTime;
	}
	public void setFinishTime(Date finishTime)
	{
		this.finishTime = finishTime;
	}
}
