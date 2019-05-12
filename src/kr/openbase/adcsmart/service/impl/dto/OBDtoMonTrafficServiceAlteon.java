package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoMonTrafficServiceAlteon
{
	private String 	vsID;
	private String 	vsIndex;
	private String 	vsName;
	private String 	vsIPAddress;
	private int 	srvPort;
	private String 	poolIndex;
	private String 	poolName;
	private String	nodeID;
	private String  nodeAddress;
	private long 	curConns;
	private long 	maxConns;
	private long 	totConns;
	private long 	bytesIn;
	@Override
	public String toString()
	{
		return "OBDtoMonTrafficServiceAlteon [vsID=" + vsID + ", vsIndex="
				+ vsIndex + ", vsName=" + vsName + ", vsIPAddress="
				+ vsIPAddress + ", srvPort=" + srvPort + ", poolIndex="
				+ poolIndex + ", poolName=" + poolName + ", nodeID=" + nodeID
				+ ", nodeAddress=" + nodeAddress + ", curConns=" + curConns
				+ ", maxConns=" + maxConns + ", totConns=" + totConns
				+ ", bytesIn=" + bytesIn + "]";
	}
	public String getNodeAddress()
	{
		return nodeAddress;
	}
	public long getBytesIn()
	{
		return bytesIn;
	}
	public void setBytesIn(long bytesIn)
	{
		this.bytesIn = bytesIn;
	}
	public void setNodeAddress(String nodeAddress)
	{
		this.nodeAddress = nodeAddress;
	}
	public String getVsID()
	{
		return vsID;
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
	public String getPoolName()
	{
		return poolName;
	}
	public void setPoolName(String poolName)
	{
		this.poolName = poolName;
	}
	public void setVsID(String vsID)
	{
		this.vsID = vsID;
	}
	public String getVsIndex()
	{
		return vsIndex;
	}
	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
	}
	public String getPoolIndex()
	{
		return poolIndex;
	}
	public void setPoolIndex(String poolIndex)
	{
		this.poolIndex = poolIndex;
	}
	public int getSrvPort()
	{
		return srvPort;
	}
	public void setSrvPort(int srvPort)
	{
		this.srvPort = srvPort;
	}
	public String getNodeID()
	{
		return nodeID;
	}
	public void setNodeID(String nodeID)
	{
		this.nodeID = nodeID;
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
}
