package kr.openbase.adcsmart.service.snmp.dto;

public class DtoRptTrunkStatus
{
	private int index;
	private String name;
	private int status;
	@Override
	public String toString()
	{
		return "DtoRptTrunkStatus [index=" + index + ", name=" + name
				+ ", status=" + status + "]";
	}
	public int getIndex()
	{
		return index;
	}
	public void setIndex(int index)
	{
		this.index = index;
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
