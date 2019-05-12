package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoUsageCpu
{
	private Date occurTime;
	private Integer usage;
	
	@Override
	public String toString()
	{
		return "OBDtoCpuUsage [occurTime=" + occurTime + ", usage=" + usage
				+ "]";
	}

	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Integer getUsage()
	{
		return usage;
	}
	public void setUsage(Integer usage)
	{
		this.usage = usage;
	}
}
