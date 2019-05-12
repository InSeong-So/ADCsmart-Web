package kr.openbase.adcsmart.service.impl.dto;

import java.sql.Timestamp;

public class OBDtoAdcResources
{
	private Long index;	//adc_index
	private Timestamp occurTime;
	private Integer adcIndex;
	private Integer cpuUsage;
	private Long cpuIdleTime;
	private Long memTotal;
	private Long memUsed;
	private Integer memUsage;
	private Long vsCurConns;
	private Long vsPktsIn;
	private Long vsPktsOut;
	private Long vsBytesIn;
	private Long vsBytesOut;
	private Long vsBps;
	private Long vsPps;
	private Integer vsCount;
	private Integer vsCountAvail;
	private Integer vsCountUnavail;
	private Integer vsCountBlocked;
	private Integer vsCountDisabled;
	public OBDtoAdcResources()
	{
		this.setIndex(0L);	//adc_index
		this.setOccurTime(null);
		this.setAdcIndex(0);
		this.setCpuUsage(0);
		this.setCpuIdleTime(0L);
		this.setMemTotal(0L);
		this.setMemUsed(0L);
		this.setMemUsage(0);
		this.setVsCurConns(0L);
		this.setVsPktsIn(0L);
		this.setVsPktsOut(0L);
		this.setVsBytesIn(0L);
		this.setVsBytesOut(0L);
		this.setVsBps(0L);
		this.setVsPps(0L);
		this.setVsCount(0);
		this.setVsCountAvail(0);
		this.setVsCountUnavail(0);
		this.setVsCountBlocked(0);
		this.setVsCountDisabled(0);
	}
	@Override
	public String toString()
	{
		return "OBDtoAdcResources [index=" + index + ", occurTime=" + occurTime
				+ ", adcIndex=" + adcIndex + ", cpuUsage=" + cpuUsage
				+ ", cpuIdleTime=" + cpuIdleTime + ", memTotal=" + memTotal
				+ ", memUsed=" + memUsed + ", memUsage=" + memUsage
				+ ", vsCurConns=" + vsCurConns + ", vsPktsIn=" + vsPktsIn
				+ ", vsPktsOut=" + vsPktsOut + ", vsBytesIn=" + vsBytesIn
				+ ", vsBytesOut=" + vsBytesOut + ", vsBps=" + vsBps
				+ ", vsPps=" + vsPps + ", vsCount=" + vsCount
				+ ", vsCountAvail=" + vsCountAvail + ", vsCountUnavail="
				+ vsCountUnavail + ", vsCountBlocked=" + vsCountBlocked
				+ ", vsCountDisabled=" + vsCountDisabled + "]";
	}
	public Long getIndex()
	{
		return index;
	}
	public void setIndex(Long index)
	{
		this.index = index;
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
	public Integer getCpuUsage()
	{
		return cpuUsage;
	}
	public void setCpuUsage(Integer cpuUsage)
	{
		this.cpuUsage = cpuUsage;
	}
	public Long getCpuIdleTime()
	{
		return cpuIdleTime;
	}
	public void setCpuIdleTime(Long cpuIdleTime)
	{
		this.cpuIdleTime = cpuIdleTime;
	}
	public Long getMemTotal()
	{
		return memTotal;
	}
	public void setMemTotal(Long memTotal)
	{
		this.memTotal = memTotal;
	}
	public Long getMemUsed()
	{
		return memUsed;
	}
	public void setMemUsed(Long memUsed)
	{
		this.memUsed = memUsed;
	}
	public Integer getMemUsage()
	{
		return memUsage;
	}
	public void setMemUsage(Integer memUsage)
	{
		this.memUsage = memUsage;
	}
	public Long getVsCurConns()
	{
		return vsCurConns;
	}
	public void setVsCurConns(Long vsCurConns)
	{
		this.vsCurConns = vsCurConns;
	}
	public Long getVsPktsIn()
	{
		return vsPktsIn;
	}
	public void setVsPktsIn(Long vsPktsIn)
	{
		this.vsPktsIn = vsPktsIn;
	}
	public Long getVsPktsOut()
	{
		return vsPktsOut;
	}
	public void setVsPktsOut(Long vsPktsOut)
	{
		this.vsPktsOut = vsPktsOut;
	}
	public Long getVsBytesIn()
	{
		return vsBytesIn;
	}
	public void setVsBytesIn(Long vsBytesIn)
	{
		this.vsBytesIn = vsBytesIn;
	}
	public Long getVsBytesOut()
	{
		return vsBytesOut;
	}
	public void setVsBytesOut(Long vsBytesOut)
	{
		this.vsBytesOut = vsBytesOut;
	}
	public Long getVsBps()
	{
		return vsBps;
	}
	public void setVsBps(Long vsBps)
	{
		this.vsBps = vsBps;
	}
	public Long getVsPps()
	{
		return vsPps;
	}
	public void setVsPps(Long vsPps)
	{
		this.vsPps = vsPps;
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
	public Integer getVsCountUnavail()
	{
		return vsCountUnavail;
	}
	public void setVsCountUnavail(Integer vsCountUnavail)
	{
		this.vsCountUnavail = vsCountUnavail;
	}
	public Integer getVsCountBlocked()
	{
		return vsCountBlocked;
	}
	public void setVsCountBlocked(Integer vsCountBlocked)
	{
		this.vsCountBlocked = vsCountBlocked;
	}
	public Integer getVsCountDisabled()
	{
		return vsCountDisabled;
	}
	public void setVsCountDisabled(Integer vsCountDisabled)
	{
		this.vsCountDisabled = vsCountDisabled;
	}
}
