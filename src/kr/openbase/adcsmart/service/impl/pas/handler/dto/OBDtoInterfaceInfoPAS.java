package kr.openbase.adcsmart.service.impl.pas.handler.dto;

public class OBDtoInterfaceInfoPAS
{
	private String name			="";
	private int	   status		=0;//up/down
	private	String macAddr		="";
	private String ipAddr		="";
	@Override
	public String toString()
	{
		return "OBDtoInterfaceInfoPAS [name=" + name + ", status=" + status
				+ ", macAddr=" + macAddr + ", ipAddr=" + ipAddr + "]";
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public String getMacAddr()
	{
		return macAddr;
	}
	public void setMacAddr(String macAddr)
	{
		this.macAddr = macAddr;
	}
	public String getIpAddr()
	{
		return ipAddr;
	}
	public void setIpAddr(String ipAddr)
	{
		this.ipAddr = ipAddr;
	}
}
