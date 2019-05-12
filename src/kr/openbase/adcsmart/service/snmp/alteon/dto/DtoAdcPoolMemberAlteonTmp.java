package kr.openbase.adcsmart.service.snmp.alteon.dto;

public class DtoAdcPoolMemberAlteonTmp
{
	private int		adcIndex;
	private String 	poolIndex;
	private String  nodeIndex;
	
	private int alteonNodeID;
	private String ipAddress;// ip 주소가 입력된다.
	private int port;
	private int state;
	private int status;	
	
	public int getAdcIndex()
	{
		return adcIndex;
	}
	public int getAlteonNodeID()
	{
		return alteonNodeID;
	}
	public void setAlteonNodeID(int alteonNodeID)
	{
		this.alteonNodeID = alteonNodeID;
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public int getPort()
	{
		return port;
	}
	public void setPort(int port)
	{
		this.port = port;
	}
	public int getState()
	{
		return state;
	}
	public void setState(int state)
	{
		this.state = state;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
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
	public String getNodeIndex()
	{
		return nodeIndex;
	}
	public void setNodeIndex(String nodeIndex)
	{
		this.nodeIndex = nodeIndex;
	}
	@Override
	public String toString()
	{
		return "DtoAdcPoolMemberAlteonTmp [adcIndex=" + adcIndex
				+ ", poolIndex=" + poolIndex + ", nodeIndex=" + nodeIndex
				+ ", alteonNodeID=" + alteonNodeID + ", ipAddress=" + ipAddress
				+ ", port=" + port + ", state=" + state + ", status=" + status
				+ "]";
	}
}
