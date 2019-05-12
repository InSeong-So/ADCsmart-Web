package kr.openbase.adcsmart.service.snmp.dto;

public class DtoSnmpStatVServer extends DtoSnmpStat
{
	private Integer vsID;
	private Integer vsrvIndex;//
	private Integer nodeID;
	
	@Override
	public String toString()
	{
		return "DtoSnmpStatVServer [vsID=" + vsID + ", vsrvIndex=" + vsrvIndex
				+ ", nodeID=" + nodeID + super.toString() +"]";
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
}
