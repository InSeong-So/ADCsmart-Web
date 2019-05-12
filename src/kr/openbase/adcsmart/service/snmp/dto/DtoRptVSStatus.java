package kr.openbase.adcsmart.service.snmp.dto;

public class DtoRptVSStatus
{
	private int index	=0;
	private String name ="";
	private String addr	="";
	private int status	=0;
	@Override
	public String toString()
	{
		return "DtoRptVSStatus [index=" + index + ", name=" + name + ", addr="
				+ addr + ", status=" + status + "]";
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
	public String getAddr()
	{
		return addr;
	}
	public void setAddr(String addr)
	{
		this.addr = addr;
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
