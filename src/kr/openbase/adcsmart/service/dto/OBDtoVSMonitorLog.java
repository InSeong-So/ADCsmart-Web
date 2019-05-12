package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoVSMonitorLog
{
	private Integer adcType;
	private Date	occurTime;
	private Integer adcIndex;
	private String  adcName;
	private String 	vsIndex;
	private String 	vsName;
	private String 	vsIPAddress;
	private Integer vsStatus;
	
	@Override
	public String toString()
	{
		return "OBDtoVSMonitorLog [adcType=" + adcType + ", occurTime="
				+ occurTime + ", adcIndex=" + adcIndex + ", adcName=" + adcName
				+ ", vsIndex=" + vsIndex + ", vsName=" + vsName
				+ ", vsIPAddress=" + vsIPAddress + ", vsStatus=" + vsStatus
				+ "]";
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
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getVsIndex()
	{
		return vsIndex;
	}
	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
	}
	public String getVsName()
	{
		return vsName;
	}
	public void setVsName(String vsName)
	{
		this.vsName = vsName;
	}
	public String getVsIPAddress()
	{
		return vsIPAddress;
	}
	public void setVsIPAddress(String vsIPAddress)
	{
		this.vsIPAddress = vsIPAddress;
	}
	public Integer getVsStatus()
	{
		return vsStatus;
	}
	public void setVsStatus(Integer vsStatus)
	{
		this.vsStatus = vsStatus;
	}
}
