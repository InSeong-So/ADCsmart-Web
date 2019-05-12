package kr.openbase.adcsmart.service.snmp.pask.dto;

public class OBDtoMonTrafficPoolMembersPASK
{
	private String 	vsName		="";
	private Integer realID		=0;
	
	private long curConns		=0;
	private long pktsIn			=0;
	private long pktsOut		=0;
	private long bytesIn		=0;
	private long bytesOut		=0;
	@Override
	public String toString()
	{
		return "OBDtoMonTrafficPoolMembersPASK [vsName=" + vsName + ", realID=" + realID + ", curConns=" + curConns + ", pktsIn=" + pktsIn + ", pktsOut=" + pktsOut + ", bytesIn=" + bytesIn + ", bytesOut=" + bytesOut + "]";
	}
	public String getVsName()
	{
		return vsName;
	}
	public void setVsName(String vsName)
	{
		this.vsName = vsName;
	}
	public Integer getRealID()
	{
		return realID;
	}
	public void setRealID(Integer realID)
	{
		this.realID = realID;
	}
	public long getCurConns()
	{
		return curConns;
	}
	public void setCurConns(long curConns)
	{
		this.curConns = curConns;
	}
	public long getPktsIn()
	{
		return pktsIn;
	}
	public void setPktsIn(long pktsIn)
	{
		this.pktsIn = pktsIn;
	}
	public long getPktsOut()
	{
		return pktsOut;
	}
	public void setPktsOut(long pktsOut)
	{
		this.pktsOut = pktsOut;
	}
	public long getBytesIn()
	{
		return bytesIn;
	}
	public void setBytesIn(long bytesIn)
	{
		this.bytesIn = bytesIn;
	}
	public long getBytesOut()
	{
		return bytesOut;
	}
	public void setBytesOut(long bytesOut)
	{
		this.bytesOut = bytesOut;
	}
}
