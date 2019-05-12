package kr.openbase.adcsmart.service.dto;

public class OBDtoSlbNodeInfo
{
	private String 	index;
	private String 	ipAddress;
	private String 	name;
	@Override
	public String toString()
	{
		return String.format("OBDtoSlbNodeInfo [index=%s, ipAddress=%s, name=%s]", index, ipAddress, name);
	}
	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
	{
		this.index = index;
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
}
