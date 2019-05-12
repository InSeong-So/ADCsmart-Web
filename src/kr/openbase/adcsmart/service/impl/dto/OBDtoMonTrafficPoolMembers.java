package kr.openbase.adcsmart.service.impl.dto;

import java.sql.Timestamp;

public class OBDtoMonTrafficPoolMembers
{
	private Timestamp time		=null;
	private String 	vsIndex		="";
	private String 	vsName		="";
	private String 	vsIPAddress	="";
	private int 	srvPort		=0;
	private String 	poolIndex	="";
	private String 	poolName	="";
	private Integer nodeID		=0;
	private String  nodeAddress	="";
	private int		nodePort	=0;
	
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
		return "OBDtoMonTrafficPoolMembers [time=" + time + ", vsIndex="
				+ vsIndex + ", vsName=" + vsName + ", vsIPAddress="
				+ vsIPAddress + ", srvPort=" + srvPort + ", poolIndex="
				+ poolIndex + ", poolName=" + poolName + ", nodeID=" + nodeID
				+ ", nodeAddress=" + nodeAddress + ", nodePort=" + nodePort
				+ ", curConns=" + curConns + ", maxConns=" + maxConns
				+ ", totConns=" + totConns + ", pktsIn=" + pktsIn
				+ ", pktsOut=" + pktsOut + ", bytesIn=" + bytesIn
				+ ", bytesOut=" + bytesOut + "]";
	}
	public Integer getNodeID()
	{
		return nodeID;
	}
	public void setNodeID(Integer nodeID)
	{
		this.nodeID = nodeID;
	}
	public Timestamp getTime()
	{
		return time;
	}
	public void setTime(Timestamp time)
	{
		this.time = time;
	}
	public String getVsIndex()
	{
		return vsIndex;
	}
	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
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
	public int getSrvPort()
	{
		return srvPort;
	}
	public void setSrvPort(int srvPort)
	{
		this.srvPort = srvPort;
	}
	public String getPoolIndex()
	{
		return poolIndex;
	}
	public void setPoolIndex(String poolIndex)
	{
		this.poolIndex = poolIndex;
	}
	public String getPoolName()
	{
		return poolName;
	}
	public void setPoolName(String poolName)
	{
		this.poolName = poolName;
	}
	public String getNodeAddress()
	{
		return nodeAddress;
	}
	public void setNodeAddress(String nodeAddress)
	{
		this.nodeAddress = nodeAddress;
	}
	public int getNodePort()
	{
		return nodePort;
	}
	public void setNodePort(int nodePort)
	{
		this.nodePort = nodePort;
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
