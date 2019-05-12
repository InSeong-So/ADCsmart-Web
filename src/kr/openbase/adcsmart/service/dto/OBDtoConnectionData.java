package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoConnectionData
{
	private Date occurTime;
	private Long maxConns;
	private Long curConns;
	private Long totConns;
	@Override
	public String toString()
	{
		return "OBDtoConnectionData [occurTime=" + occurTime + ", maxConns="
				+ maxConns + ", curConns=" + curConns + ", totConns="
				+ totConns + "]";
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Long getMaxConns()
	{
		return maxConns;
	}
	public void setMaxConns(Long maxConns)
	{
		this.maxConns = maxConns;
	}
	public Long getCurConns()
	{
		return curConns;
	}
	public void setCurConns(Long curConns)
	{
		this.curConns = curConns;
	}
	public Long getTotConns()
	{
		return totConns;
	}
	public void setTotConns(Long totConns)
	{
		this.totConns = totConns;
	}
}
