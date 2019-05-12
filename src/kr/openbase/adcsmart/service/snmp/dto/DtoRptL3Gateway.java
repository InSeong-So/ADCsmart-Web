package kr.openbase.adcsmart.service.snmp.dto;

public class DtoRptL3Gateway
{
	private int index;
	private String addr;
	private int vlanIndex;
	private int status;//up:1, failed:2
	@Override
	public String toString()
	{
		return "DotRptL3Gateway [index=" + index + ", addr=" + addr
				+ ", vlanIndex=" + vlanIndex + ", status=" + status + "]";
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
	public int getVlanIndex()
	{
		return vlanIndex;
	}
	public void setVlanIndex(int vlanIndex)
	{
		this.vlanIndex = vlanIndex;
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
