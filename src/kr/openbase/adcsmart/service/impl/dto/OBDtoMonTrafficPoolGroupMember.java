package kr.openbase.adcsmart.service.impl.dto;

import java.sql.Timestamp;

public class OBDtoMonTrafficPoolGroupMember
{
	private String 	dbIndex;
	private String 	id;
	private Integer	adcIndex;
	private Integer groupDbIndex;
	private Timestamp occurTime =null;
	
	private long curConns		=0;
	private long maxConns		=0;
	private long totConns		=0;
	private long pktsIn			=0;
	private long pktsOut		=0;
	private long bytesIn		=0;
	private long bytesOut		=0;

	@Override
	public String toString()
	{
		return "OBDtoMonTrafficPoolGroupMember [dbIndex=" + dbIndex + ", id="
				+ id + ", adcIndex=" + adcIndex + ", groupDbIndex="
				+ groupDbIndex + ", occurTime=" + occurTime + ", curConns="
				+ curConns + ", maxConns=" + maxConns + ", totConns="
				+ totConns + ", pktsIn=" + pktsIn + ", pktsOut=" + pktsOut
				+ ", bytesIn=" + bytesIn + ", bytesOut=" + bytesOut + "]";
	}
	public String getDbIndex()
	{
		return dbIndex;
	}
	public void setDbIndex(String dbIndex)
	{
		this.dbIndex = dbIndex;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public Integer getGroupDbIndex()
	{
		return groupDbIndex;
	}
	public void setGroupDbIndex(Integer groupDbIndex)
	{
		this.groupDbIndex = groupDbIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public Timestamp getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Timestamp occurTime)
	{
		this.occurTime = occurTime;
	}
	public long getCurConns()
	{
		return curConns;
	}
	public void setCurConns(long curConns)
	{
		this.curConns = curConns;
	}
	public long getMaxConns()
	{
		return maxConns;
	}
	public void setMaxConns(long maxConns)
	{
		this.maxConns = maxConns;
	}
	public long getTotConns()
	{
		return totConns;
	}
	public void setTotConns(long totConns)
	{
		this.totConns = totConns;
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
