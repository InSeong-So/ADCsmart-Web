package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidCfgInterface
{
	private String intfIndex;
	private String intfAddr;
	private String intfMask;
	private String intfState;
	
	@Override
	public String toString()
	{
		return "DtoOidCfgInterface [intfIndex=" + intfIndex + ", intfAddr="
				+ intfAddr + ", intfMask=" + intfMask + ", intfState="
				+ intfState + "]";
	}
	public String getIntfIndex()
	{
		return intfIndex;
	}
	public void setIntfIndex(String intfIndex)
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
	public String getIntfState()
	{
		return intfState;
	}
	public void setIntfState(String intfState)
	{
		this.intfState = intfState;
	}
}
