package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidInfoSystem
{
	private String sysDescr;
	private String sysObjectID;
	private String sysUpTime;
	private String sysCpuIdle;
	private String sysMemTotal;
	private String sysMemAvail;
	private String sysMemBuffered;
	private String sysMemCached;	
	private String sysModel;
	private String sysHddTotal;
	private String sysHddUsed;
	private String sysDiskPath;
	
	@Override
	public String toString()
	{
		return "DtoOidInfoSystem [sysDescr=" + sysDescr + ", sysObjectID=" + sysObjectID + ", sysUpTime=" + sysUpTime + ", sysCpuIdle=" + sysCpuIdle + ", sysMemTotal=" + sysMemTotal + ", sysMemAvail=" + sysMemAvail + ", sysMemBuffered=" + sysMemBuffered + ", sysMemCached=" + sysMemCached + ", sysModel=" + sysModel + ", sysHddTotal=" + sysHddTotal + ", sysHddUsed=" + sysHddUsed + ", sysDiskPath=" + sysDiskPath + "]";
	}
	
	public String getSysMemBuffered()
	{
		return sysMemBuffered;
	}

	public void setSysMemBuffered(String sysMemBuffered)
	{
		this.sysMemBuffered = sysMemBuffered;
	}

	public String getSysMemCached()
	{
		return sysMemCached;
	}

	public void setSysMemCached(String sysMemCached)
	{
		this.sysMemCached = sysMemCached;
	}

	public String getSysDiskPath()
	{
		return sysDiskPath;
	}

	public void setSysDiskPath(String sysDiskPath)
	{
		this.sysDiskPath = sysDiskPath;
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
	public String getSysUpTime()
	{
		return sysUpTime;
	}
	public void setSysUpTime(String sysUpTime)
	{
		this.sysUpTime = sysUpTime;
	}
	public String getSysCpuIdle()
	{
		return sysCpuIdle;
	}
	public void setSysCpuIdle(String sysCpuIdle)
	{
		this.sysCpuIdle = sysCpuIdle;
	}
	public String getSysMemTotal()
	{
		return sysMemTotal;
	}
	public void setSysMemTotal(String sysMemTotal)
	{
		this.sysMemTotal = sysMemTotal;
	}
	public String getSysMemAvail()
	{
		return sysMemAvail;
	}
	public void setSysMemAvail(String sysMemAvail)
	{
		this.sysMemAvail = sysMemAvail;
	}
	public String getSysModel()
	{
		return sysModel;
	}
	public void setSysModel(String sysModel)
	{
		this.sysModel = sysModel;
	}
	public String getSysHddTotal()
	{
		return sysHddTotal;
	}
	public void setSysHddTotal(String sysHddTotal)
	{
		this.sysHddTotal = sysHddTotal;
	}
	public String getSysHddUsed()
	{
		return sysHddUsed;
	}
	public void setSysHddUsed(String sysHddUsed)
	{
		this.sysHddUsed = sysHddUsed;
	}
}
