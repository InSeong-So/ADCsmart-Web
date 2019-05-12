package kr.openbase.adcsmart.service.snmp.alteon.dto;

public class OBDtoVirtServicesInfoEntry
{
	private String 	vsID;
	private String 	vsIndex;
	private int 	vsrvIndex;
	private String  vsName;
	private String  vsIPAddress;
	private String 	poolName;
	private String 	poolIndex;
	private int		srvPort;
	private String 	realID;
	private String  nodeAddress;

	@Override
	public String toString()
	{
		return "OBDtoVirtServicesInfoEntry [vsID=" + vsID + ", vsIndex="
				+ vsIndex + ", vsrvIndex=" + vsrvIndex + ", vsName=" + vsName
				+ ", vsIPAddress=" + vsIPAddress + ", poolName=" + poolName
				+ ", poolIndex=" + poolIndex + ", srvPort=" + srvPort
				+ ", realID=" + realID + ", nodeAddress=" + nodeAddress + "]";
	}
	public String getNodeAddress()
	{
		return nodeAddress;
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
	public int getVsrvIndex()
	{
		return vsrvIndex;
	}
	public void setVsrvIndex(int vsrvIndex)
	{
		this.vsrvIndex = vsrvIndex;
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
	public String getRealID()
	{
		return realID;
	}
	public void setRealID(String realID)
	{
		this.realID = realID;
	}
}
