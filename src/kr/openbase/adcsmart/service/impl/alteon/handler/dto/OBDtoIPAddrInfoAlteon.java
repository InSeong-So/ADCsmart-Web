package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

public class OBDtoIPAddrInfoAlteon
{
	private	String	ipAddr;
	private	String	netmask;
	private String	bcast;
	private	String  vlan;
	private	int		status;
	@Override
	public String toString()
	{
		return "OBDtoIPAddrInfoAlteon [ipAddr=" + ipAddr + ", netmask=" + netmask + ", bcast=" + bcast + ", vlan=" + vlan + ", status=" + status + "]";
	}
	public String getBcast()
	{
		return bcast;
	}
	public void setBcast(String bcast)
	{
		this.bcast = bcast;
	}
	public String getIpAddr()
	{
		return ipAddr;
	}
	public void setIpAddr(String ipAddr)
	{
		this.ipAddr = ipAddr;
	}
	public String getNetmask()
	{
		return netmask;
	}
	public void setNetmask(String netmask)
	{
		this.netmask = netmask;
	}
	public String getVlan()
	{
		return vlan;
	}
	public void setVlan(String vlan)
	{
		this.vlan = vlan;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
}
