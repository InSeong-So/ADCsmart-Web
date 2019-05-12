package kr.openbase.adcsmart.service.snmp.alteon.dto;

public class DtoSnmpInterfaceAlteon
{
	private Integer intfIndex;
	private String intfAddr;
	private String intfMask;
	private Integer intfState;
	
	@Override
	public String toString()
	{
		return "DtoSnmpInterfaceAlteon [intfIndex=" + intfIndex + ", intfAddr="
				+ intfAddr + ", intfMask=" + intfMask + ", intfState="
				+ intfState + "]";
	}
	public Integer getIntfIndex()
	{
		return intfIndex;
	}
	public void setIntfIndex(Integer intfIndex)
	{
		this.intfIndex = intfIndex;
	}
	public String getIntfAddr()
	{
		return intfAddr;
	}
	public void setIntfAddr(String intfAddr)
	{
		this.intfAddr = intfAddr;
	}
	public String getIntfMask()
	{
		return intfMask;
	}
	public void setIntfMask(String intfMask)
	{
		this.intfMask = intfMask;
	}
	public Integer getIntfState()
	{
		return intfState;
	}
	public void setIntfState(Integer intfState)
	{
		this.intfState = intfState;
	}
}
