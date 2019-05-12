package kr.openbase.adcsmart.service.snmp.dto;

public class DtoSnmpVirtServicesInfoState
{
	private Integer vsID;
	private Integer vsrvIndex;
	private Integer nodeID;
	private Integer state;
	
	@Override
	public String toString()
	{
		return "DtoSnmpVirtServicesInfoState [vsID=" + vsID + ", vsrvIndex="
				+ vsrvIndex + ", nodeID=" + nodeID + ", state=" + state + "]";
	}

	public Integer getVsID()
	{
		return vsID;
	}

	public void setVsID(Integer vsID)
	{
		this.vsID = vsID;
	}

	public Integer getVsrvIndex()
	{
		return vsrvIndex;
	}

	public void setVsrvIndex(Integer vsrvIndex)
	{
		this.vsrvIndex = vsrvIndex;
	}

	public Integer getNodeID()
	{
		return nodeID;
	}

	public void setNodeID(Integer nodeID)
	{
		this.nodeID = nodeID;
	}

	public Integer getState()
	{
		return state;
	}

	public void setState(Integer state)
	{
		this.state = state;
	}
}
