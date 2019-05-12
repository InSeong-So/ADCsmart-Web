package kr.openbase.adcsmart.service.snmp.dto;

public class DtoRptLinkStatus
{
	private String name;
	private int status;
	@Override
	public String toString()
	{
		return "DtoRptLinkStatus [name=" + name + ", status=" + status + "]";
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
}
