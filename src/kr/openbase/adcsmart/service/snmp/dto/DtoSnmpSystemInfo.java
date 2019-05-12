package kr.openbase.adcsmart.service.snmp.dto;

import java.sql.Timestamp;

public class DtoSnmpSystemInfo
{
	private String sysDescr;
	private String sysObjectID;
	private Timestamp upTime;
	private Integer cpuIdle;
	private Integer memTotal=0;
	private Integer memUsed=0;
	private Integer memAvail=0;

	private Integer memBuffered=0;
	private Integer memCached=0;
	private String 	sysModel;
	private Long 	hddTotal=0L;//KBytes
	private Long 	hddUsed=0L;//KBytes
	
	@Override
	public String toString()
	{
		return "DtoSnmpSystemInfo [sysDescr=" + sysDescr + ", sysObjectID=" + sysObjectID + ", upTime=" + upTime + ", cpuIdle=" + cpuIdle + ", memTotal=" + memTotal + ", memUsed=" + memUsed + ", memAvail=" + memAvail + ", memBuffered=" + memBuffered + ", memCached=" + memCached + ", sysModel=" + sysModel + ", hddTotal=" + hddTotal + ", hddUsed=" + hddUsed + "]";
	}
	
	public Integer getMemAvail()
	{
		return memAvail;
	}

	public void setMemAvail(Integer memAvail)
	{
		this.memAvail = memAvail;
	}

	public Integer getMemBuffered()
	{
		return memBuffered;
	}

	public void setMemBuffered(Integer memBuffered)
	{
		this.memBuffered = memBuffered;
	}

	public Integer getMemCached()
	{
		return memCached;
	}

	public void setMemCached(Integer memCached)
	{
		this.memCached = memCached;
	}

	public String getSysDescr()
	{
		return sysDescr;
	}
	public void setSysDescr(String sysDescr)
	{
		this.sysDescr = sysDescr;
	}
	public String getSysObjectID()
	{
		return sysObjectID;
	}
	public void setSysObjectID(String sysObjectID)
	{
		this.sysObjectID = sysObjectID;
	}
	public Timestamp getUpTime()
	{
		return upTime;
	}
	public void setUpTime(Timestamp upTime)
	{
		this.upTime = upTime;
	}
	public Integer getCpuIdle()
	{
		return cpuIdle;
	}
	public void setCpuIdle(Integer cpuIdle)
	{
		this.cpuIdle = cpuIdle;
	}
	public Integer getMemTotal()
	{
		return memTotal;
	}
	public void setMemTotal(Integer memTotal)
	{
		this.memTotal = memTotal;
	}
	public Integer getMemUsed()
	{
		return memUsed;
	}
	public void setMemUsed(Integer memUsed)
	{
		this.memUsed = memUsed;
	}
	public String getSysModel()
	{
		return sysModel;
	}
	public void setSysModel(String sysModel)
	{
		this.sysModel = sysModel;
	}
	public Long getHddTotal()
	{
		return hddTotal;
	}
	public void setHddTotal(Long hddTotal)
	{
		this.hddTotal = hddTotal;
	}
	public Long getHddUsed()
	{
		return hddUsed;
	}
	public void setHddUsed(Long hddUsed)
	{
		this.hddUsed = hddUsed;
	}
}