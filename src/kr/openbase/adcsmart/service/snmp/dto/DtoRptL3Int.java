package kr.openbase.adcsmart.service.snmp.dto;

public class DtoRptL3Int
{
	private int index;
	private String name;
	private String addr;
	private String netmask;
	private String bcastAddr;//for alteon
	private int vlanIndex;//for alteon
	private String vlanIndexName;//for f5
	private int status;//-up:1, down:2, disabled:3. for alteon
	@Override
	public String toString()
	{
		return "DtoRptL3Int [index=" + index + ", name=" + name + ", addr="
				+ addr + ", netmask=" + netmask + ", bcastAddr=" + bcastAddr
				+ ", vlanIndex=" + vlanIndex + ", vlanIndexName="
				+ vlanIndexName + ", status=" + status + "]";
	}
	public int getIndex()
	{
		return index;
	}
	public String getVlanIndexName()
	{
		return vlanIndexName;
	}
	public void setVlanIndexName(String vlanIndexName)
	{
		this.vlanIndexName = vlanIndexName;
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
	public String getAddr()
	{
		return addr;
	}
	public void setAddr(String addr)
	{
		this.addr = addr;
	}
	public String getNetmask()
	{
		return netmask;
	}
	public void setNetmask(String netmask)
	{
		this.netmask = netmask;
	}
	public String getBcastAddr()
	{
		return bcastAddr;
	}
	public void setBcastAddr(String bcastAddr)
	{
		this.bcastAddr = bcastAddr;
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
