package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidRptOPL3
{
	private String intIndex;
	private String intAddr;
	private String intNetmask;
	private String intBcastAddr;
	private String intVlan;
	private String intStatus;
	private String gwIndex;
	private String gwAddr;
	private String gwVlan;
	private String gwStatus;
	private String gwType;//for f5
	@Override
	public String toString()
	{
		return "DtoOidRptOPL3 [intIndex=" + intIndex + ", intAddr=" + intAddr
				+ ", intNetmask=" + intNetmask + ", intBcastAddr="
				+ intBcastAddr + ", intVlan=" + intVlan + ", intStatus="
				+ intStatus + ", gwIndex=" + gwIndex + ", gwAddr=" + gwAddr
				+ ", gwVlan=" + gwVlan + ", gwStatus=" + gwStatus + ", gwType="
				+ gwType + "]";
	}
	public String getGwType()
	{
		return gwType;
	}
	public void setGwType(String gwType)
	{
		this.gwType = gwType;
	}
	public String getIntIndex()
	{
		return intIndex;
	}
	public void setIntIndex(String intIndex)
	{
		this.intIndex = intIndex;
	}
	public String getIntAddr()
	{
		return intAddr;
	}
	public void setIntAddr(String intAddr)
	{
		this.intAddr = intAddr;
	}
	public String getIntNetmask()
	{
		return intNetmask;
	}
	public void setIntNetmask(String intNetmask)
	{
		this.intNetmask = intNetmask;
	}
	public String getIntBcastAddr()
	{
		return intBcastAddr;
	}
	public void setIntBcastAddr(String intBcastAddr)
	{
		this.intBcastAddr = intBcastAddr;
	}
	public String getIntVlan()
	{
		return intVlan;
	}
	public void setIntVlan(String intVlan)
	{
		this.intVlan = intVlan;
	}
	public String getIntStatus()
	{
		return intStatus;
	}
	public void setIntStatus(String intStatus)
	{
		this.intStatus = intStatus;
	}
	public String getGwIndex()
	{
		return gwIndex;
	}
	public void setGwIndex(String gwIndex)
	{
		this.gwIndex = gwIndex;
	}
	public String getGwAddr()
	{
		return gwAddr;
	}
	public void setGwAddr(String gwAddr)
	{
		this.gwAddr = gwAddr;
	}
	public String getGwVlan()
	{
		return gwVlan;
	}
	public void setGwVlan(String gwVlan)
	{
		this.gwVlan = gwVlan;
	}
	public String getGwStatus()
	{
		return gwStatus;
	}
	public void setGwStatus(String gwStatus)
	{
		this.gwStatus = gwStatus;
	}
}
