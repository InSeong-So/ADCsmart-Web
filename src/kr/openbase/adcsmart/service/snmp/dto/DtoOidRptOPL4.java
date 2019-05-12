package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidRptOPL4
{
	private String poolMemberStatusAvail;
	private String poolMemberStatusEnabled;
	private String poolMemberAddr;
	private String poolMemberIndex;//for alteon
	private String vsIndex;
	private String vsAddr;
	private String vsStatusAvail;
	private String vsStatusEnabled;
	private String concurrentConn;
	private String maxConn;
	private String directMode;
	@Override
	public String toString()
	{
		return "DtoOidRptOPL4 [poolMemberStatusAvail=" + poolMemberStatusAvail
				+ ", poolMemberStatusEnabled=" + poolMemberStatusEnabled
				+ ", poolMemberAddr=" + poolMemberAddr + ", vsrvPoolIndex="
				+ poolMemberIndex + ", vsIndex=" + vsIndex + ", vsAddr=" + vsAddr
				+ ", vsStatusAvail=" + vsStatusAvail + ", vsStatusEnabled="
				+ vsStatusEnabled + ", concurrentConn=" + concurrentConn
				+ ", maxConn=" + maxConn + ", directMode=" + directMode + "]";
	}
	public String getPoolMemberStatusAvail()
	{
		return poolMemberStatusAvail;
	}
	public String getPoolMemberIndex()
	{
		return poolMemberIndex;
	}
	public void setPoolMemberIndex(String poolMemberIndex)
	{
		this.poolMemberIndex = poolMemberIndex;
	}
	public void setPoolMemberStatusAvail(String poolMemberStatusAvail)
	{
		this.poolMemberStatusAvail = poolMemberStatusAvail;
	}
	public String getPoolMemberStatusEnabled()
	{
		return poolMemberStatusEnabled;
	}
	public void setPoolMemberStatusEnabled(String poolMemberStatusEnabled)
	{
		this.poolMemberStatusEnabled = poolMemberStatusEnabled;
	}
	public String getPoolMemberAddr()
	{
		return poolMemberAddr;
	}
	public void setPoolMemberAddr(String poolMemberAddr)
	{
		this.poolMemberAddr = poolMemberAddr;
	}
	public String getVsIndex()
	{
		return vsIndex;
	}
	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
	}
	public String getVsAddr()
	{
		return vsAddr;
	}
	public void setVsAddr(String vsAddr)
	{
		this.vsAddr = vsAddr;
	}
	public String getVsStatusAvail()
	{
		return vsStatusAvail;
	}
	public void setVsStatusAvail(String vsStatusAvail)
	{
		this.vsStatusAvail = vsStatusAvail;
	}
	public String getVsStatusEnabled()
	{
		return vsStatusEnabled;
	}
	public void setVsStatusEnabled(String vsStatusEnabled)
	{
		this.vsStatusEnabled = vsStatusEnabled;
	}
	public String getConcurrentConn()
	{
		return concurrentConn;
	}
	public void setConcurrentConn(String concurrentConn)
	{
		this.concurrentConn = concurrentConn;
	}
	public String getMaxConn()
	{
		return maxConn;
	}
	public void setMaxConn(String maxConn)
	{
		this.maxConn = maxConn;
	}
	public String getDirectMode()
	{
		return directMode;
	}
	public void setDirectMode(String directMode)
	{
		this.directMode = directMode;
	}
}
