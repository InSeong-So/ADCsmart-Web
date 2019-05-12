package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoUsageConnection
{
	private Date occurTime;
	private Long	conns;
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Long getConns()
	{
		return conns;
	}
	public void setConns(Long conns)
	{
		this.conns = conns;
	}
	@Override
	public String toString()
	{
		return "OBDtoUsageConnection [occurTime=" + occurTime + ", conns="
				+ conns + "]";
	}
}
