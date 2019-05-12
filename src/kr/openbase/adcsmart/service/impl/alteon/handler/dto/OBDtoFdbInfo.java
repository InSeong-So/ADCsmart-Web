package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

public class OBDtoFdbInfo
{
	private String macAddr;
	private Integer vlanID;
	@Override
	public String toString()
	{
		return "OBDtoFdbInfo [macAddr=" + macAddr + ", vlanID=" + vlanID + "]";
	}
	public String getMacAddr()
	{
		return macAddr;
	}
	public void setMacAddr(String macAddr)
	{
		this.macAddr = macAddr;
	}
	public Integer getVlanID()
	{
		return vlanID;
	}
	public void setVlanID(Integer vlanID)
	{
		this.vlanID = vlanID;
	}
}
