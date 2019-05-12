package kr.openbase.adcsmart.service.snmp.dto;

public class DtoRptStgStatus
{
	private int index;//for alteon
	private String name;//for f5
	private int status;
	@Override
	public String toString()
	{
		return "DtoRptStgStatus [index=" + index + ", name=" + name
				+ ", status=" + status + "]";
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getIndex()
	{
		return index;
	}
	public void setIndex(int index)
	{
		this.index = index;
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
