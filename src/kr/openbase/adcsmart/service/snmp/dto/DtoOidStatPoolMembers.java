package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidStatPoolMembers
{
	private String nodeIPAddress;
	private String pktsIn;
	private String bytesIn;
	private String pktsOut;
	private String bytesOut;
	private String maxConns;
	private String totConns;
	private String curConns;
	@Override
	public String toString()
	{
		return "DtoOidStatPoolMembers [nodeIPAddress=" + nodeIPAddress + ", pktsIn=" + pktsIn + ", bytesIn=" + bytesIn + ", pktsOut=" + pktsOut + ", bytesOut=" + bytesOut + ", maxConns=" + maxConns + ", totConns=" + totConns + ", curConns=" + curConns + "]";
	}
	public String getNodeIPAddress()
	{
		return nodeIPAddress;
	}
	public void setNodeIPAddress(String nodeIPAddress)
	{
		this.nodeIPAddress = nodeIPAddress;
	}
	public String getPktsIn()
	{
		return pktsIn;
	}
	public void setPktsIn(String pktsIn)
	{
		this.pktsIn = pktsIn;
	}
	public String getBytesIn()
	{
		return bytesIn;
	}
	public void setBytesIn(String bytesIn)
	{
		this.bytesIn = bytesIn;
	}
	public String getPktsOut()
	{
		return pktsOut;
	}
	public void setPktsOut(String pktsOut)
	{
		this.pktsOut = pktsOut;
	}
	public String getBytesOut()
	{
		return bytesOut;
	}
	public void setBytesOut(String bytesOut)
	{
		this.bytesOut = bytesOut;
	}
	public String getMaxConns()
	{
		return maxConns;
	}
	public void setMaxConns(String maxConns)
	{
		this.maxConns = maxConns;
	}
	public String getTotConns()
	{
		return totConns;
	}
	public void setTotConns(String totConns)
	{
		this.totConns = totConns;
	}
	public String getCurConns()
	{
		return curConns;
	}
	public void setCurConns(String curConns)
	{
		this.curConns = curConns;
	}
}
