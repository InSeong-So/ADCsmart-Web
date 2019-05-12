package kr.openbase.adcsmart.service.snmp.dto;

public class DtoRptConnStatus
{
	private long maxConn=0;
	private long curConn=0;
	
	@Override
	public String toString()
	{
		return "DtoRptConnStatus [maxConn=" + maxConn + ", curConn=" + curConn
				+ "]";
	}
	public long getMaxConn()
	{
		return maxConn;
	}
	public void setMaxConn(long maxConn)
	{
		this.maxConn = maxConn;
	}
	public long getCurConn()
	{
		return curConn;
	}
	public void setCurConn(long curConn)
	{
		this.curConn = curConn;
	}
}
