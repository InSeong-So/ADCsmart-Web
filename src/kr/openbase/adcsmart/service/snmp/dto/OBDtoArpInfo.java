package kr.openbase.adcsmart.service.snmp.dto;

public class OBDtoArpInfo
{
	private String 	dstIPAddr;
	private String 	macAddr;
	private String 	portNum;
	private String  vlanInfo;
	
	@Override
	public String toString()
	{
		return String.format("OBDtoArpInfo [dstIPAddr=%s, macAddr=%s, portNum=%s, vlanInfo=%s]", dstIPAddr, macAddr, portNum, vlanInfo);
	}
	public String getPortNum()
	{
		return portNum;
	}
	public String getVlanInfo()
	{
		return vlanInfo;
	}
	public void setVlanInfo(String vlanInfo)
	{
		this.vlanInfo = vlanInfo;
	}
	public void setPortNum(String portNum)
	{
		this.portNum = portNum;
	}
	public String getDstIPAddr()
	{
		return dstIPAddr;
	}
	public void setDstIPAddr(String dstIPAddr)
	{
		this.dstIPAddr = dstIPAddr;
	}
	public String getMacAddr()
	{
		return macAddr;
	}
	public void setMacAddr(String macAddr)
	{
		this.macAddr = macAddr;
	}
}
