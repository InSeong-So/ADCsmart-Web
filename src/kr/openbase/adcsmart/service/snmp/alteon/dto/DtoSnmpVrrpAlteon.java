package kr.openbase.adcsmart.service.snmp.alteon.dto;

public class DtoSnmpVrrpAlteon
{
	private Integer vrrpIndex;
	private Integer vrrpID;
	private String 	vrrpAddr;
	private Integer vrrpIfIndex;
//	private Integer vrrpPriority;
	private Integer vrrpState;
//	private Integer vrrpSharing;
//	private Integer vrrpTckvrrp;
//	private Integer vrrpTckIpIntf;
//	private Integer vrrpTckVlanPort;
//	private Integer vrrpTckL4Port;
//	private Integer vrrpTckRServer;
//	private Integer vrrpTckHsrp;
//	private Integer vrrpTckHsrv;

	@Override
	public String toString()
	{
		return "DtoSnmpVrrpAlteon [vrrpIndex=" + vrrpIndex + ", vrrpID="
				+ vrrpID + ", vrrpAddr=" + vrrpAddr + ", vrrpIfIndex="
				+ vrrpIfIndex + ", vrrpState=" + vrrpState + "]";
	}
	public Integer getVrrpIndex()
	{
		return vrrpIndex;
	}
	public void setVrrpIndex(Integer vrrpIndex)
	{
		this.vrrpIndex = vrrpIndex;
	}
	public Integer getVrrpID()
	{
		return vrrpID;
	}
	public void setVrrpID(Integer vrrpID)
	{
		this.vrrpID = vrrpID;
	}
	public String getVrrpAddr()
	{
		return vrrpAddr;
	}
	public void setVrrpAddr(String vrrpAddr)
	{
		this.vrrpAddr = vrrpAddr;
	}
	public Integer getVrrpIfIndex()
	{
		return vrrpIfIndex;
	}
	public void setVrrpIfIndex(Integer vrrpIfIndex)
	{
		this.vrrpIfIndex = vrrpIfIndex;
	}
	public Integer getVrrpState()
	{
		return vrrpState;
	}
	public void setVrrpState(Integer vrrpState)
	{
		this.vrrpState = vrrpState;
	}	
}
