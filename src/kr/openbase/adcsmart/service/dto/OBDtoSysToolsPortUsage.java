package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoSysToolsPortUsage
{
	private String 	adcName="";
	private String 	adcIPAddress="";
	private String  portName="";
	private Date 	occurTime;
	private Integer usedCount=0;
	private Integer notUsedCount=0;
	private String 	usedInfo="";
	private String 	notUsedInfo="";
	@Override
	public String toString()
	{
		return "OBDtoSysToolsPortUsage [adcName=" + adcName + ", adcIPAddress=" + adcIPAddress + ", portName=" + portName + ", occurTime=" + occurTime + ", usedCount=" + usedCount + ", notUsedCount=" + notUsedCount + ", usedInfo=" + usedInfo + ", notUsedInfo=" + notUsedInfo + "]";
	}
	public String getPortName()
	{
		return portName;
	}
	public void setPortName(String portName)
	{
		this.portName = portName;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public String getAdcIPAddress()
	{
		return adcIPAddress;
	}
	public void setAdcIPAddress(String adcIPAddress)
	{
		this.adcIPAddress = adcIPAddress;
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Integer getUsedCount()
	{
		return usedCount;
	}
	public void setUsedCount(Integer usedCount)
	{
		this.usedCount = usedCount;
	}
	public Integer getNotUsedCount()
	{
		return notUsedCount;
	}
	public void setNotUsedCount(Integer notUsedCount)
	{
		this.notUsedCount = notUsedCount;
	}
	public String getUsedInfo()
	{
		return usedInfo;
	}
	public void setUsedInfo(String usedInfo)
	{
		this.usedInfo = usedInfo;
	}
	public String getNotUsedInfo()
	{
		return notUsedInfo;
	}
	public void setNotUsedInfo(String notUsedInfo)
	{
		this.notUsedInfo = notUsedInfo;
	}
}
