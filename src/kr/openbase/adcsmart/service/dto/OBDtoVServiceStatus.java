package kr.openbase.adcsmart.service.dto;

public class OBDtoVServiceStatus
{
	private int		vsID;
	private int 	vsrvID;
	private int		nodeID;
	
	private int 	status;

	@Override
	public String toString()
	{
		return "OBDtoVServiceStatus [vsID=" + vsID + ", vsrvID=" + vsrvID
				+ ", nodeID=" + nodeID + ", status=" + status + "]";
	}

	public int getNodeID()
	{
		return nodeID;
	}

	public void setNodeID(int nodeID)
	{
		this.nodeID = nodeID;
	}

	public int getVsID()
	{
		return vsID;
	}

	public void setVsID(int vsID)
	{
		this.vsID = vsID;
	}

	public int getVsrvID()
	{
		return vsrvID;
	}

	public void setVsrvID(int vsrvID)
	{
		this.vsrvID = vsrvID;
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
