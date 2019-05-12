package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidInfoAdcResc
{
	private String adcCpuIdle;
	private String adcMemTotal;
	private String adcMemUsed;
	private String adcMemFree;
	private String tmmMemTotal;//for f5
	private String tmmMemUsed;//for f5
	private String adcCurConns;
	private String adcPktsIn;
	private String adcPktsOut;
	private String adcBytesIn;
	private String adcBytesOut;
	private String adcModel;
	private String adcTypeModel;
	private String adcSwVersion;
	private String adcSerailNum;
	private String adcName;
	private String adcUpTime;
	private String adcDescr;
	private String vsAvaliableState;
	private String vssEnabledState;
	private String nodeState;// for alteon
	private String nodeAvaliableState;;//for f5
	private String nodeIPAddress;;//for f5
	private String nodeEnabledState;// for f5
	private String serviceVirtPort;//
	private String adcSPUsage;//mp usage for alteon
	private String adcSlbCurConns; //2014.4 ykkim. Alteon만 Slb와 전체(adcCurConns)를 구별하고 있다.
	private String adcMaxConns; //2014.4 ykkim. Alteon만.
	private String adcMgmtSyslog;// for alteon
	private String adcMgmtTftp;// for alteon
	@Override
    public String toString()
    {
        return "DtoOidInfoAdcResc [adcCpuIdle=" + adcCpuIdle + ", adcMemTotal="
                + adcMemTotal + ", adcMemUsed=" + adcMemUsed + ", adcMemFree="
                + adcMemFree + ", tmmMemTotal=" + tmmMemTotal + ", tmmMemUsed="
                + tmmMemUsed + ", adcCurConns=" + adcCurConns + ", adcPktsIn="
                + adcPktsIn + ", adcPktsOut=" + adcPktsOut + ", adcBytesIn="
                + adcBytesIn + ", adcBytesOut=" + adcBytesOut + ", adcModel="
                + adcModel + ", adcTypeModel=" + adcTypeModel
                + ", adcSwVersion=" + adcSwVersion + ", adcSerailNum="
                + adcSerailNum + ", adcName=" + adcName + ", adcUpTime="
                + adcUpTime + ", adcDescr=" + adcDescr + ", vsAvaliableState="
                + vsAvaliableState + ", vssEnabledState=" + vssEnabledState
                + ", nodeState=" + nodeState + ", nodeAvaliableState="
                + nodeAvaliableState + ", nodeIPAddress=" + nodeIPAddress
                + ", nodeEnabledState=" + nodeEnabledState
                + ", serviceVirtPort=" + serviceVirtPort + ", adcSPUsage="
                + adcSPUsage + ", adcSlbCurConns=" + adcSlbCurConns
                + ", adcMaxConns=" + adcMaxConns + ", adcMgmtSyslog="
                + adcMgmtSyslog + ", adcMgmtTftp=" + adcMgmtTftp + "]";
    }
	public String getAdcSPUsage()
	{
		return adcSPUsage;
	}
	public String getNodeIPAddress()
	{
		return nodeIPAddress;
	}
	public void setNodeIPAddress(String nodeIPAddress)
	{
		this.nodeIPAddress = nodeIPAddress;
	}
	public void setAdcSPUsage(String adcSPUsage)
	{
		this.adcSPUsage = adcSPUsage;
	}
	public String getAdcMemFree()
	{
		return adcMemFree;
	}
	public String getServiceVirtPort()
	{
		return serviceVirtPort;
	}
	public void setServiceVirtPort(String serviceVirtPort)
	{
		this.serviceVirtPort = serviceVirtPort;
	}
	public void setAdcMemFree(String adcMemFree)
	{
		this.adcMemFree = adcMemFree;
	}
	public String getTmmMemTotal()
	{
		return tmmMemTotal;
	}
	public void setTmmMemTotal(String tmmMemTotal)
	{
		this.tmmMemTotal = tmmMemTotal;
	}
	public String getTmmMemUsed()
	{
		return tmmMemUsed;
	}
	public void setTmmMemUsed(String tmmMemUsed)
	{
		this.tmmMemUsed = tmmMemUsed;
	}
	public String getNodeState()
	{
		return nodeState;
	}
	public String getNodeAvaliableState()
	{
		return nodeAvaliableState;
	}
	public void setNodeAvaliableState(String nodeAvaliableState)
	{
		this.nodeAvaliableState = nodeAvaliableState;
	}
	public String getNodeEnabledState()
	{
		return nodeEnabledState;
	}
	public void setNodeEnabledState(String nodeEnabledState)
	{
		this.nodeEnabledState = nodeEnabledState;
	}
	public void setNodeState(String nodeState)
	{
		this.nodeState = nodeState;
	}
	public String getAdcCpuIdle()
	{
		return adcCpuIdle;
	}
	public void setAdcCpuIdle(String adcCpuIdle)
	{
		this.adcCpuIdle = adcCpuIdle;
	}
	public String getAdcMemTotal()
	{
		return adcMemTotal;
	}
	public void setAdcMemTotal(String adcMemTotal)
	{
		this.adcMemTotal = adcMemTotal;
	}
	public String getAdcMemUsed()
	{
		return adcMemUsed;
	}
	public void setAdcMemUsed(String adcMemUsed)
	{
		this.adcMemUsed = adcMemUsed;
	}
	public String getAdcCurConns()
	{
		return adcCurConns;
	}
	public void setAdcCurConns(String adcCurConns)
	{
		this.adcCurConns = adcCurConns;
	}
	public String getAdcPktsIn()
	{
		return adcPktsIn;
	}
	public void setAdcPktsIn(String adcPktsIn)
	{
		this.adcPktsIn = adcPktsIn;
	}
	public String getAdcPktsOut()
	{
		return adcPktsOut;
	}
	public void setAdcPktsOut(String adcPktsOut)
	{
		this.adcPktsOut = adcPktsOut;
	}
	public String getAdcBytesIn()
	{
		return adcBytesIn;
	}
	public void setAdcBytesIn(String adcBytesIn)
	{
		this.adcBytesIn = adcBytesIn;
	}
	public String getAdcBytesOut()
	{
		return adcBytesOut;
	}
	public void setAdcBytesOut(String adcBytesOut)
	{
		this.adcBytesOut = adcBytesOut;
	}
	public String getAdcModel()
	{
		return adcModel;
	}
	public void setAdcModel(String adcModel)
	{
		this.adcModel = adcModel;
	}
	public String getAdcTypeModel()
	{
		return adcTypeModel;
	}
	public void setAdcTypeModel(String adcTypeModel)
	{
		this.adcTypeModel = adcTypeModel;
	}
	public String getAdcSwVersion()
	{
		return adcSwVersion;
	}
	public void setAdcSwVersion(String adcSwVersion)
	{
		this.adcSwVersion = adcSwVersion;
	}
	public String getAdcSerailNum()
	{
		return adcSerailNum;
	}
	public void setAdcSerailNum(String adcSerailNum)
	{
		this.adcSerailNum = adcSerailNum;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public String getAdcUpTime()
	{
		return adcUpTime;
	}
	public void setAdcUpTime(String adcUpTime)
	{
		this.adcUpTime = adcUpTime;
	}
	public String getAdcDescr()
	{
		return adcDescr;
	}
	public void setAdcDescr(String adcDescr)
	{
		this.adcDescr = adcDescr;
	}
	public String getVsAvaliableState()
	{
		return vsAvaliableState;
	}
	public void setVsAvaliableState(String vsAvaliableState)
	{
		this.vsAvaliableState = vsAvaliableState;
	}
	public String getVssEnabledState()
	{
		return vssEnabledState;
	}
	public void setVssEnabledState(String vssEnabledState)
	{
		this.vssEnabledState = vssEnabledState;
	}
	public String getAdcSlbCurConns()
	{
		return adcSlbCurConns;
	}
	public void setAdcSlbCurConns(String adcSlbCurConns)
	{
		this.adcSlbCurConns = adcSlbCurConns;
	}
	public String getAdcMaxConns()
	{
		return adcMaxConns;
	}
	public void setAdcMaxConns(String adcMaxConns)
	{
		this.adcMaxConns = adcMaxConns;
	}
    public String getAdcMgmtSyslog()
    {
        return adcMgmtSyslog;
    }
    public void setAdcMgmtSyslog(String adcMgmtSyslog)
    {
        this.adcMgmtSyslog = adcMgmtSyslog;
    }
    public String getAdcMgmtTftp()
    {
        return adcMgmtTftp;
    }
    public void setAdcMgmtTftp(String adcMgmtTftp)
    {
        this.adcMgmtTftp = adcMgmtTftp;
    }
}
