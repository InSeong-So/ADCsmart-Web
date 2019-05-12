package kr.openbase.adcsmart.service.impl.fault.dto;

import java.util.Date;

public class OBDtoPktdumpStatusInfo
{
	private String 		adcName;
	private Integer     adcIndex;
	private Integer		adcType;
	private Date		startTime;
	private Date		endTime;
	private Integer		elapsedTime;
	private	Long		fileSize;// byte 단위.
	private Integer     progressRate;
	private Integer     lastCpuUsage;
	private Integer     lastMemUsage;
	private Long		logKey;
	
	@Override
	public String toString()
	{
		return "OBDtoPktdumpStatusInfo [adcName=" + adcName + ", adcIndex=" + adcIndex + ", adcType=" + adcType + ", startTime=" + startTime + ", endTime=" + endTime + ", elapsedTime=" + elapsedTime + ", fileSize=" + fileSize + ", progressRate=" + progressRate + ", lastCpuUsage=" + lastCpuUsage + ", lastMemUsage=" + lastMemUsage + ", logKey=" + logKey + "]";
	}
	public Integer getLastCpuUsage()
	{
		return lastCpuUsage;
	}
	public void setLastCpuUsage(Integer lastCpuUsage)
	{
		this.lastCpuUsage = lastCpuUsage;
	}
	public Integer getLastMemUsage()
	{
		return lastMemUsage;
	}
	public void setLastMemUsage(Integer lastMemUsage)
	{
		this.lastMemUsage = lastMemUsage;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public Integer getProgressRate()
	{
		return progressRate;
	}
	public void setProgressRate(Integer progressRate)
	{
		this.progressRate = progressRate;
	}
	public Date getStartTime()
	{
		return startTime;
	}
	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}
	public Date getEndTime()
	{
		return endTime;
	}
	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
	public Integer getElapsedTime()
	{
		return elapsedTime;
	}
	public void setElapsedTime(Integer elapsedTime)
	{
		this.elapsedTime = elapsedTime;
	}
	public Long getFileSize()
	{
		return fileSize;
	}
	public void setFileSize(Long fileSize)
	{
		this.fileSize = fileSize;
	}
	public Long getLogKey()
	{
		return logKey;
	}
	public void setLogKey(Long logKey)
	{
		this.logKey = logKey;
	}
	public Integer getAdcType()
	{
		return adcType;
	}
	public void setAdcType(Integer adcType)
	{
		this.adcType = adcType;
	}	
}