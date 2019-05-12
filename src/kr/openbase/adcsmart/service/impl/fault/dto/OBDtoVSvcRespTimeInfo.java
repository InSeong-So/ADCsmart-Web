package kr.openbase.adcsmart.service.impl.fault.dto;

import java.sql.Timestamp;

public class OBDtoVSvcRespTimeInfo
{
	private Timestamp occurTime;
	private Integer   adcIndex;
	private String    vsvcIndex;
	private Integer   type;// 
	private Integer   responseTime;
	
	@Override
	public String toString()
	{
		return String.format("OBDtoVSvcRespTimeInfo [occurTime=%s, adcIndex=%s, vsvcIndex=%s, type=%s, responseTime=%s]", occurTime, adcIndex, vsvcIndex, type, responseTime);
	}
	public Timestamp getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Timestamp occurTime)
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
	public String getVsvcIndex()
	{
		return vsvcIndex;
	}
	public void setVsvcIndex(String vsvcIndex)
	{
		this.vsvcIndex = vsvcIndex;
	}
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
	}
	public Integer getResponseTime()
	{
		return responseTime;
	}
	public void setResponseTime(Integer responseTime)
	{
		this.responseTime = responseTime;
	}
}
