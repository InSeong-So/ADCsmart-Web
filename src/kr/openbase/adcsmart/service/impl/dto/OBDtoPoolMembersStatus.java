package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoPoolMembersStatus
{
	private int 	adcIndex;
	private String 	poolIndex;
	private int		poolAlteonID;
	private int		nodeAlteonID;
	private String 	nodeIPAddress;
	private int		nodePort;
	
	private int		status;

	@Override
	public String toString()
	{
		return "OBDtoPoolMembersStatus [adcIndex=" + adcIndex + ", poolIndex="
				+ poolIndex + ", poolAlteonID=" + poolAlteonID
				+ ", nodeAlteonID=" + nodeAlteonID + ", nodeIPAddress="
				+ nodeIPAddress + ", nodePort=" + nodePort + ", status="
				+ status + "]";
	}

	public int getAdcIndex()
	{
		return adcIndex;
	}

	public int getNodeAlteonID()
	{
		return nodeAlteonID;
	}

	public void setNodeAlteonID(int nodeAlteonID)
	{
		this.nodeAlteonID = nodeAlteonID;
	}

	public void setAdcIndex(int adcIndex)
	{
		this.adcIndex = adcIndex;
	}

	public String getPoolIndex()
	{
		return poolIndex;
	}

	public void setPoolIndex(String poolIndex)
	{
		this.poolIndex = poolIndex;
	}

	public int getPoolAlteonID()
	{
		return poolAlteonID;
	}

	public void setPoolAlteonID(int poolAlteonID)
	{
		this.poolAlteonID = poolAlteonID;
	}

	public String getNodeIPAddress()
	{
		return nodeIPAddress;
	}

	public void setNodeIPAddress(String nodeIPAddress)
	{
		this.nodeIPAddress = nodeIPAddress;
	}

	public int getNodePort()
	{
		return nodePort;
	}

	public void setNodePort(int nodePort)
	{
		this.nodePort = nodePort;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}
}
