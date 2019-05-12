package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoUsageMem
{
	private Date occurTime;
	private Integer usage;
	private Long 	total;
	private Long 	used;
	
	@Override
	public String toString()
	{
		return "OBDtoMemUsage [occurTime=" + occurTime + ", usage=" + usage
				+ ", total=" + total + ", used=" + used + "]";
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
	public Long getTotal()
	{
		return total;
	}
	public void setTotal(Long total)
	{
		this.total = total;
	}
	public Long getUsed()
	{
		return used;
	}
	public void setUsed(Long used)
	{
		this.used = used;
	}
}
