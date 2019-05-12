package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

public class OBDtoGateWayInfoAlteon
{
	private	String	ipAddr;
	private	String  vlan;
	private	int		status;
	@Override
	public String toString()
	{
		return "OBDtoGateWayInfoAlteon [ipAddr=" + ipAddr + ", vlan=" + vlan + ", status=" + status + "]";
	}
	public String getIpAddr()
	{
		return ipAddr;
	}
	public void setIpAddr(String ipAddr)
	{
		this.ipAddr = ipAddr;
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
