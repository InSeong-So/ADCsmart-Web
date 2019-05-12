package kr.openbase.adcsmart.service.dto;

public class OBDtoAdcName
{
	private Integer Index;
	private String name;
	private String ipaddress;
	@Override
	public String toString()
	{
		return "OBDtoAdcName [Index=" + Index + ", name=" + name + ", ipaddress=" + ipaddress + "]";
	}
	public String getIpaddress()
	{
		return ipaddress;
	}
	public void setIpaddress(String ipaddress)
	{
		this.ipaddress = ipaddress;
	}
	public Integer getIndex()
	{
		return Index;
	}
	public void setIndex(Integer index)
	{
		Index = index;
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
