package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoAdcPerfConnection
{
	private Date	occurTime;
	private Long	connsPerSecond;
	@Override
	public String toString()
	{
		return "OBDtoAdcPerfConnection [occurTime=" + occurTime
				+ ", connsPerSecond=" + connsPerSecond + "]";
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Long getConnsPerSecond()
	{
		return connsPerSecond;
	}
	public void setConnsPerSecond(Long connsPerSecond)
	{
		this.connsPerSecond = connsPerSecond;
	}
}
