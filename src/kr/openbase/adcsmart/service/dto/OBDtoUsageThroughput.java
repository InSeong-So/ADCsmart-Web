package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoUsageThroughput
{
	private Date occurTime;
	private Long bps;
	private long pps;
	
	@Override
	public String toString()
	{
		return "OBDtoUsageThroughput [occurTime=" + occurTime + ", bps=" + bps
				+ ", pps=" + pps + "]";
	}
	
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Long getBps()
	{
		return bps;
	}
	public void setBps(Long bps)
	{
		this.bps = bps;
	}
	public long getPps()
	{
		return pps;
	}
	public void setPps(long pps)
	{
		this.pps = pps;
	}
}
