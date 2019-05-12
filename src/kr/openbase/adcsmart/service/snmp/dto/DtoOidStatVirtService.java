package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidStatVirtService 
{
	private String srvPort;
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
		return "DtoOidStatVirtService [srvPort=" + srvPort + ", pktsIn="
				+ pktsIn + ", bytesIn=" + bytesIn + ", pktsOut=" + pktsOut
				+ ", bytesOut=" + bytesOut + ", maxConns=" + maxConns
				+ ", totConns=" + totConns + ", curConns=" + curConns + "]";
	}
	
	public void setPktsIn(String pktsIn)
	{
		this.pktsIn = pktsIn;
	}
	public String getSrvPort()
	{
		return srvPort;
	}

	public void setSrvPort(String srvPort)
	{
		this.srvPort = srvPort;
	}

	public String getPktsIn()
	{
		return this.pktsIn;
	}	

	public void setBytesIn(String bytesIn)
	{
		this.bytesIn = bytesIn;
	}
	public String getBytesIn()
	{
		return this.bytesIn;
	}	
	
	public void setPktsOut(String pktsOut)
	{
		this.pktsOut = pktsOut;
	}
	public String getPktsOut()
	{
		return this.pktsOut;
	}	

	public void setBytesOut(String bytesOut)
	{
		this.bytesOut = bytesOut;
	}
	public String getBytesOut()
	{
		return this.bytesOut;
	}	

	public void setMaxConns(String maxConns)
	{
		this.maxConns = maxConns;
	}
	public String getMaxConns()
	{
		return this.maxConns;
	}	

	public void setTotConns(String totConns)
	{
		this.totConns = totConns;
	}
	public String getTotConns()
	{
		return this.totConns;
	}	

	public void setCurConns(String curConns)
	{
		this.curConns = curConns;
	}
	public String getCurConns()
	{
		return this.curConns;
	}	
}
