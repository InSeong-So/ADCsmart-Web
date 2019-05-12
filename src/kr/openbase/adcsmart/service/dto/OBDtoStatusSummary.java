package kr.openbase.adcsmart.service.dto;

public class OBDtoStatusSummary
{
	private Integer adcCount;
	private Integer adcCountAvail;
	private Integer adcCountUnavail;
	private Integer vsCount;
	private Integer vsCountAvail;
	private Integer vsCountDisable;
	private Integer vsCountUnavali;
	private Integer sysCpuUsage;
	private Integer sysMemUsage;
	private Integer sysHddUsage;
	
	@Override
	public String toString()
	{
		return "OBDtoStatusSummary [adcCount=" + adcCount + ", adcCountAvail="
				+ adcCountAvail + ", adcCountUnavail=" + adcCountUnavail
				+ ", vsCount=" + vsCount + ", vsCountAvail=" + vsCountAvail
				+ ", vsCountDisable=" + vsCountDisable + ", vsCountUnavali="
				+ vsCountUnavali + ", sysCpuUsage=" + sysCpuUsage
				+ ", sysMemUsage=" + sysMemUsage + ", sysHddUsage="
				+ sysHddUsage + "]";
	}

	public Integer getAdcCount()
	{
		return adcCount;
	}
	public void setAdcCount(Integer adcCount)
	{
		this.adcCount = adcCount;
	}
	public Integer getAdcCountAvail()
	{
		return adcCountAvail;
	}
	public void setAdcCountAvail(Integer adcCountAvail)
	{
		this.adcCountAvail = adcCountAvail;
	}
	public Integer getVsCountDisable()
	{
		return vsCountDisable;
	}
	public void setVsCountDisable(Integer vsCountDisable)
	{
		this.vsCountDisable = vsCountDisable;
	}
	public Integer getAdcCountUnavail()
	{
		return adcCountUnavail;
	}
	public void setAdcCountUnavail(Integer adcCountUnavail)
	{
		this.adcCountUnavail = adcCountUnavail;
	}
	public Integer getVsCount()
	{
		return vsCount;
	}
	public void setVsCount(Integer vsCount)
	{
		this.vsCount = vsCount;
	}
	public Integer getVsCountAvail()
	{
		return vsCountAvail;
	}
	public void setVsCountAvail(Integer vsCountAvail)
	{
		this.vsCountAvail = vsCountAvail;
	}
	public Integer getVsCountUnavali()
	{
		return vsCountUnavali;
	}
	public void setVsCountUnavali(Integer vsCountUnavali)
	{
		this.vsCountUnavali = vsCountUnavali;
	}
	public Integer getSysCpuUsage()
	{
		return sysCpuUsage;
	}
	public void setSysCpuUsage(Integer sysCpuUsage)
	{
		this.sysCpuUsage = sysCpuUsage;
	}
	public Integer getSysMemUsage()
	{
		return sysMemUsage;
	}
	public void setSysMemUsage(Integer sysMemUsage)
	{
		this.sysMemUsage = sysMemUsage;
	}
	public Integer getSysHddUsage()
	{
		return sysHddUsage;
	}
	public void setSysHddUsage(Integer sysHddUsage)
	{
		this.sysHddUsage = sysHddUsage;
	}
}
