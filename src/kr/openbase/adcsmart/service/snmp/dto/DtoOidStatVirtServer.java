package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidStatVirtServer 
{
	private String vsID;
	private String vsName;
	private String vsIPAddress;
	private String pktsIn;
	private String bytesIn;
	private String pktsOut;
	private String bytesOut;
	private String maxConns;
	private String totConns;
	private String curConns;
	private String status;
	private String enabled;
	
	@Override
	public String toString()
	{
		return "DtoOidStatVirtServer [vsID=" + vsID + ", vsName=" + vsName
				+ ", vsIPAddress=" + vsIPAddress + ", pktsIn=" + pktsIn
				+ ", bytesIn=" + bytesIn + ", pktsOut=" + pktsOut
				+ ", bytesOut=" + bytesOut + ", maxConns=" + maxConns
				+ ", totConns=" + totConns + ", curConns=" + curConns
				+ ", status=" + status + ", enabled=" + enabled + "]";
	}
	
	public void setPktsIn(String pktsIn)
	{
		this.pktsIn = pktsIn;
	}
	public String getEnabled()
	{
		return enabled;
	}

	public void setEnabled(String enabled)
	{
		this.enabled = enabled;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getVsID()
	{
		return vsID;
	}

	public void setVsID(String vsID)
	{
		this.vsID = vsID;
	}

	public String getVsName()
	{
		return vsName;
	}

	public void setVsName(String vsName)
	{
		this.vsName = vsName;
	}

	public String getVsIPAddress()
	{
		return vsIPAddress;
	}

	public void setVsIPAddress(String vsIPAddress)
	{
		this.vsIPAddress = vsIPAddress;
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
