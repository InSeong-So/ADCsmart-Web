/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;


public class SystemStatusDto 
{
	private Date occurredTime;
	private Integer cpuUsage;
	private Integer memoryUsage;
	
	//알테온 cpu 그래프 에서 이용
	private Integer sp1Usage;
	private Integer sp2Usage;
	private Integer sp3Usage;
	private Integer sp4Usage;
	
	@Override
	public String toString()
	{
		return "SystemStatusDto [occurredTime=" + occurredTime + ", cpuUsage="
				+ cpuUsage + ", memoryUsage=" + memoryUsage + ", sp1Usage="
				+ sp1Usage + ", sp2Usage=" + sp2Usage + ", sp3Usage="
				+ sp3Usage + ", sp4Usage=" + sp4Usage + "]";
	}
	
	public Integer getSp1Usage()
	{
		return sp1Usage;
	}

	public void setSp1Usage(Integer sp1Usage)
	{
		this.sp1Usage = sp1Usage;
	}

	public Integer getSp2Usage()
	{
		return sp2Usage;
	}

	public void setSp2Usage(Integer sp2Usage)
	{
		this.sp2Usage = sp2Usage;
	}

	public Integer getSp3Usage()
	{
		return sp3Usage;
	}

	public void setSp3Usage(Integer sp3Usage)
	{
		this.sp3Usage = sp3Usage;
	}

	public Integer getSp4Usage()
	{
		return sp4Usage;
	}

	public void setSp4Usage(Integer sp4Usage)
	{
		this.sp4Usage = sp4Usage;
	}
	
	public Date getOccurredTime()
	{
		return occurredTime;
	}

	public void setOccurredTime(Date occurredTime)
	{
		this.occurredTime = occurredTime;
	}

	public Integer getCpuUsage()
	{
		return cpuUsage;
	}

	public void setCpuUsage(Integer cpuUsage)
	{
		this.cpuUsage = cpuUsage;
	}

	public Integer getMemoryUsage()
	{
		return memoryUsage;
	}


	public void setMemoryUsage(Integer memoryUsage) 
	{
		this.memoryUsage = memoryUsage;
	}
	
}
