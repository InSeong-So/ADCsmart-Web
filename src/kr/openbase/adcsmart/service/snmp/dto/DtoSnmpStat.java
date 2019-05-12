package kr.openbase.adcsmart.service.snmp.dto;

public class DtoSnmpStat
{
	private long pktsIn;
	private long bytesIn;
	private long pktsOut;
	private long bytesOut;
	private long maxConns;
	private long totConns;
	private long curConns;

	@Override
	public String toString() 
	{
		return "OBDtoStat [pktsIn=" + this.pktsIn + ", bytesIn=" + this.bytesIn + ", pktsOut="
				+ this.pktsOut + ", bytesOut=" + this.bytesOut + ", maxConns=" + this.maxConns
				+ ", totConns=" + this.totConns + ", curConns=" + this.curConns + "]";
	}
	
	public void setPktsIn(long pktsIn)
	{
		this.pktsIn = pktsIn;
	}
	public long getPktsIn()
	{
		return this.pktsIn;
	}	

	public void setBytesIn(long bytesIn)
	{
		this.bytesIn = bytesIn;
	}
	public long getBytesIn()
	{
		return this.bytesIn;
	}	
	
	public void setPktsOut(long pktsOut)
	{
		this.pktsOut = pktsOut;
	}
	public long getPktsOut()
	{
		return this.pktsOut;
	}	

	public void setBytesOut(long bytesOut)
	{
		this.bytesOut = bytesOut;
	}
	public long getBytesOut()
	{
		return this.bytesOut;
	}	

	public void setMaxConns(long maxConns)
	{
		this.maxConns = maxConns;
	}
	public long getMaxConns()
	{
		return this.maxConns;
	}	

	public void setTotConns(long totConns)
	{
		this.totConns = totConns;
	}
	public long getTotConns()
	{
		return this.totConns;
	}	

	public void setCurConns(long curConns)
	{
		this.curConns = curConns;
	}
	public long getCurConns()
	{
		return this.curConns;
	}	
}
