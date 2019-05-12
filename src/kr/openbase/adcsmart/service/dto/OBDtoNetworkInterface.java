package kr.openbase.adcsmart.service.dto;

public class OBDtoNetworkInterface
{
	private Integer ifNum;
	private String ipAddress;
	private String netmask;

	public String getNetmask()
	{
		return netmask;
	}
	public void setNetmask(String netmask)
	{
		this.netmask = netmask;
	}
	public Integer getIfNum()
	{
		return this.ifNum;
	}
	public void setIfNum(Integer ifNum)
	{
		this.ifNum = ifNum;
	}

	public String getIpAddress()
	{
		return this.ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	@Override
	public String toString()
	{
		return "OBDtoNetworkInterface [ifNum=" + ifNum + ", ipAddress="
				+ ipAddress + ", netmask=" + netmask + "]";
	}
	
}
