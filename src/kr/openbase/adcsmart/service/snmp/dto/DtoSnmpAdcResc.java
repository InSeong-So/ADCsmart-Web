package kr.openbase.adcsmart.service.snmp.dto;

import java.sql.Timestamp;
import java.util.ArrayList;

public class DtoSnmpAdcResc
{
	private Timestamp occurTime;
	private long cpuUsed;//for alteon
	private ArrayList<Long> spUsedList;//for alteon
	private ArrayList<Long> cpuIdleTime;//for f5
	private long memTotal;
	private long memUsed;
	private long vsCurConns;
	private long adcCurConns; //adc level의 connection 체크 2014.4 ykkim 추가
	private long vsPktsIn;
	private long vsPktsOut;
	private long vsBytesIn;
	private long vsBytesOut;
	private String model;
	private String swVersion;
	private String name;
	private Timestamp upTime;
	private String descr;
	private int vsCount;
	private int vsCountAvailable;
	private int vsCountUnavailable;
	private int vsCountBlocked; //검색 : task:blocked_cancel - 안 쓴다. 
	private int vsCountDisabled;

	@Override
	public String toString()
	{
		return "DtoSnmpAdcResc [occurTime=" + occurTime + ", cpuUsed="
				+ cpuUsed + ", spUsedList=" + spUsedList + ", cpuIdleTime="
				+ cpuIdleTime + ", memTotal=" + memTotal + ", memUsed="
				+ memUsed + ", vsCurConns=" + vsCurConns + ", adcCurConns="
				+ adcCurConns + ", vsPktsIn=" + vsPktsIn + ", vsPktsOut="
				+ vsPktsOut + ", vsBytesIn=" + vsBytesIn + ", vsBytesOut="
				+ vsBytesOut + ", model=" + model + ", swVersion=" + swVersion
				+ ", name=" + name + ", upTime=" + upTime + ", descr=" + descr
				+ ", vsCount=" + vsCount + ", vsCountAvailable="
				+ vsCountAvailable + ", vsCountUnavailable="
				+ vsCountUnavailable + ", vsCountBlocked=" + vsCountBlocked
				+ ", vsCountDisabled=" + vsCountDisabled + "]";
	}
	public ArrayList<Long> getSpUsedList()
	{
		return spUsedList;
	}
	public void setSpUsedList(ArrayList<Long> spUsedList)
	{
		this.spUsedList = spUsedList;
	}
	public Timestamp getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Timestamp occurTime)
	{
		this.occurTime = occurTime;
	}
	public long getCpuUsed()
	{
		return cpuUsed;
	}
	public void setCpuUsed(long cpuUsed)
	{
		this.cpuUsed = cpuUsed;
	}
	public ArrayList<Long> getCpuIdleTime()
	{
		return cpuIdleTime;
	}
	public void setCpuIdleTime(ArrayList<Long> cpuIdleTime)
	{
		this.cpuIdleTime = cpuIdleTime;
	}
	public long getMemTotal()
	{
		return memTotal;
	}
	public void setMemTotal(long memTotal)
	{
		this.memTotal = memTotal;
	}
	public long getMemUsed()
	{
		return memUsed;
	}
	public void setMemUsed(long memUsed)
	{
		this.memUsed = memUsed;
	}
	public long getVsCurConns()
	{
		return vsCurConns;
	}
	public void setVsCurConns(long vsCurConns)
	{
		this.vsCurConns = vsCurConns;
	}
	public long getVsPktsIn()
	{
		return vsPktsIn;
	}
	public void setVsPktsIn(long vsPktsIn)
	{
		this.vsPktsIn = vsPktsIn;
	}
	public long getVsPktsOut()
	{
		return vsPktsOut;
	}
	public void setVsPktsOut(long vsPktsOut)
	{
		this.vsPktsOut = vsPktsOut;
	}
	public long getVsBytesIn()
	{
		return vsBytesIn;
	}
	public void setVsBytesIn(long vsBytesIn)
	{
		this.vsBytesIn = vsBytesIn;
	}
	public long getVsBytesOut()
	{
		return vsBytesOut;
	}
	public void setVsBytesOut(long vsBytesOut)
	{
		this.vsBytesOut = vsBytesOut;
	}
	public String getModel()
	{
		return model;
	}
	public void setModel(String model)
	{
		this.model = model;
	}
	public String getSwVersion()
	{
		return swVersion;
	}
	public void setSwVersion(String swVersion)
	{
		this.swVersion = swVersion;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Timestamp getUpTime()
	{
		return upTime;
	}
	public void setUpTime(Timestamp upTime)
	{
		this.upTime = upTime;
	}
	public String getDescr()
	{
		return descr;
	}
	public void setDescr(String descr)
	{
		this.descr = descr;
	}
	public int getVsCount()
	{
		return vsCount;
	}
	public void setVsCount(int vsCount)
	{
		this.vsCount = vsCount;
	}
	public int getVsCountAvailable()
	{
		return vsCountAvailable;
	}
	public void setVsCountAvailable(int vsCountAvailable)
	{
		this.vsCountAvailable = vsCountAvailable;
	}
	public int getVsCountUnavailable()
	{
		return vsCountUnavailable;
	}
	public void setVsCountUnavailable(int vsCountUnavailable)
	{
		this.vsCountUnavailable = vsCountUnavailable;
	}
	public int getVsCountBlocked()
	{
		return vsCountBlocked;
	}
	public void setVsCountBlocked(int vsCountBlocked)
	{
		this.vsCountBlocked = vsCountBlocked;
	}
	public int getVsCountDisabled()
	{
		return vsCountDisabled;
	}
	public void setVsCountDisabled(int vsCountDisabled)
	{
		this.vsCountDisabled = vsCountDisabled;
	}
	public long getAdcCurConns()
	{
		return adcCurConns;
	}
	public void setAdcCurConns(long adcCurConns)
	{
		this.adcCurConns = adcCurConns;
	}	
}
