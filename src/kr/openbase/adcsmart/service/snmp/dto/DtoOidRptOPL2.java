package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidRptOPL2
{
	private String linkUpInfo;
	private String linkStateChanged;
	private String linkDiscardsIn;
	private String linkDiscardsOut;
	private String linkErrorIn;
	private String linkErrorOut;
	private String vlanId;
	private String vlanName;
	private String vlanPorts;
	private String vlanState;
	private String stpState;
	private String stpInfo;
	private String trunkName;
	private String trunkStatus;
	@Override
	public String toString()
	{
		return "DtoOidRptOPL2 [linkUpInfo=" + linkUpInfo
				+ ", linkStateChanged=" + linkStateChanged
				+ ", linkDiscardsIn=" + linkDiscardsIn + ", linkDiscardsOut="
				+ linkDiscardsOut + ", linkErrorIn=" + linkErrorIn
				+ ", linkErrorOut=" + linkErrorOut + ", vlanId=" + vlanId
				+ ", vlanName=" + vlanName + ", vlanPorts=" + vlanPorts
				+ ", vlanState=" + vlanState + ", stpState=" + stpState
				+ ", stpInfo=" + stpInfo + ", trunkName=" + trunkName
				+ ", trunkStatus=" + trunkStatus + "]";
	}
	public String getLinkUpInfo()
	{
		return linkUpInfo;
	}
	public String getStpState()
	{
		return stpState;
	}
	public void setStpState(String stpState)
	{
		this.stpState = stpState;
	}
	public void setLinkUpInfo(String linkUpInfo)
	{
		this.linkUpInfo = linkUpInfo;
	}
	public String getLinkStateChanged()
	{
		return linkStateChanged;
	}
	public void setLinkStateChanged(String linkStateChanged)
	{
		this.linkStateChanged = linkStateChanged;
	}
	public String getLinkDiscardsIn()
	{
		return linkDiscardsIn;
	}
	public void setLinkDiscardsIn(String linkDiscardsIn)
	{
		this.linkDiscardsIn = linkDiscardsIn;
	}
	public String getLinkDiscardsOut()
	{
		return linkDiscardsOut;
	}
	public void setLinkDiscardsOut(String linkDiscardsOut)
	{
		this.linkDiscardsOut = linkDiscardsOut;
	}
	public String getLinkErrorIn()
	{
		return linkErrorIn;
	}
	public void setLinkErrorIn(String linkErrorIn)
	{
		this.linkErrorIn = linkErrorIn;
	}
	public String getLinkErrorOut()
	{
		return linkErrorOut;
	}
	public void setLinkErrorOut(String linkErrorOut)
	{
		this.linkErrorOut = linkErrorOut;
	}
	public String getVlanId()
	{
		return vlanId;
	}
	public void setVlanId(String vlanId)
	{
		this.vlanId = vlanId;
	}
	public String getVlanName()
	{
		return vlanName;
	}
	public void setVlanName(String vlanName)
	{
		this.vlanName = vlanName;
	}
	public String getVlanPorts()
	{
		return vlanPorts;
	}
	public void setVlanPorts(String vlanPorts)
	{
		this.vlanPorts = vlanPorts;
	}
	public String getVlanState()
	{
		return vlanState;
	}
	public void setVlanState(String vlanState)
	{
		this.vlanState = vlanState;
	}
	public String getStpInfo()
	{
		return stpInfo;
	}
	public void setStpInfo(String stpInfo)
	{
		this.stpInfo = stpInfo;
	}
	public String getTrunkName()
	{
		return trunkName;
	}
	public void setTrunkName(String trunkName)
	{
		this.trunkName = trunkName;
	}
	public String getTrunkStatus()
	{
		return trunkStatus;
	}
	public void setTrunkStatus(String trunkStatus)
	{
		this.trunkStatus = trunkStatus;
	}
}
