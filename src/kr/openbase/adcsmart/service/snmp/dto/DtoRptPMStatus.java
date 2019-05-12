package kr.openbase.adcsmart.service.snmp.dto;

public class DtoRptPMStatus
{
	private int memberIndex		=0;//for alteon
	private int srvIndex		=0;//for alteon
	private int vsIndex			=0;//for alteon
	private String poolName		="";// f5
	private int srvPort			=0;//for f5
	private String addr			="";
	private int status			=0;
	@Override
	public String toString()
	{
		return "DtoRptPMStatus [memberIndex=" + memberIndex + ", srvIndex="
				+ srvIndex + ", vsIndex=" + vsIndex + ", poolName=" + poolName
				+ ", srvPort=" + srvPort + ", addr=" + addr + ", status="
				+ status + "]";
	}
	public String getPoolName()
	{
		return poolName;
	}
	public void setPoolName(String poolName)
	{
		this.poolName = poolName;
	}
	public int getSrvPort()
	{
		return srvPort;
	}
	public void setSrvPort(int srvPort)
	{
		this.srvPort = srvPort;
	}	
	public int getMemberIndex()
	{
		return memberIndex;
	}
	public void setMemberIndex(int memberIndex)
	{
		this.memberIndex = memberIndex;
	}
	public int getSrvIndex()
	{
		return srvIndex;
	}
	public void setSrvIndex(int srvIndex)
	{
		this.srvIndex = srvIndex;
	}
	public int getVsIndex()
	{
		return vsIndex;
	}
	public void setVsIndex(int vsIndex)
	{
		this.vsIndex = vsIndex;
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
