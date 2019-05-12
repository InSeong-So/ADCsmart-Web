package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidCfgNode
{
	private String nodeID		 ="";
	private String nodeIpAddr	 ="";
	private String nodeState	 ="";
	private String nodeName		 ="";
	private String nodeAvail	 ="";
	private String nodeStatus 	 ="";
	private String nodePort		 ="";
	private String nodeInterval  ="";
	private String nodeFailRetry ="";
	private String nodeBackup    ="";
	private String nodeWeight    ="";
	private String nodeMaxConns  ="";
	private String nodeMaxConMode="";
	private String nodeTimeout   ="";

	@Override
	public String toString()
	{
		return "DtoOidCfgNode [nodeID=" + nodeID + ", nodeIpAddr=" + nodeIpAddr
				+ ", nodeState=" + nodeState + ", nodeName=" + nodeName
				+ ", nodeAvail=" + nodeAvail + ", nodeStatus=" + nodeStatus
				+ ", nodePort=" + nodePort + ", nodeInterval=" + nodeInterval
				+ ", nodeFailRetry=" + nodeFailRetry + ", nodeBackup="
				+ nodeBackup + ", nodeWeight=" + nodeWeight + ", nodeMaxConns="
				+ nodeMaxConns + ", nodeMaxConMode=" + nodeMaxConMode
				+ ", nodeTimeout=" + nodeTimeout + "]";
	}

	public String getNodePort()
	{
		return nodePort;
	}
	public void setNodePort(String nodePort)
	{
		this.nodePort = nodePort;
	}
	public String getNodeID()
	{
		return nodeID;
	}
	public String getNodeStatus()
	{
		return nodeStatus;
	}
	public void setNodeStatus(String nodeStatus)
	{
		this.nodeStatus = nodeStatus;
	}
	public String getNodeState()
	{
		return nodeState;
	}
	public void setNodeState(String nodeState)
	{
		this.nodeState = nodeState;
	}
	public void setNodeID(String nodeID)
	{
		this.nodeID = nodeID;
	}
	public String getNodeIpAddr()
	{
		return nodeIpAddr;
	}
	public void setNodeIpAddr(String nodeIpAddr)
	{
		this.nodeIpAddr = nodeIpAddr;
	}
	public String getNodeMaxConns()
	{
		return nodeMaxConns;
	}
	public void setNodeMaxConns(String nodeMaxConns)
	{
		this.nodeMaxConns = nodeMaxConns;
	}
	public String getNodeName()
	{
		return nodeName;
	}
	public void setNodeName(String nodeName)
	{
		this.nodeName = nodeName;
	}
	public String getNodeAvail()
	{
		return nodeAvail;
	}
	public void setNodeAvail(String nodeAvail)
	{
		this.nodeAvail = nodeAvail;
	}
	public String getNodeInterval()
	{
		return nodeInterval;
	}
	public void setNodeInterval(String nodeInterval)
	{
		this.nodeInterval = nodeInterval;
	}
	public String getNodeFailRetry()
	{
		return nodeFailRetry;
	}
	public void setNodeFailRetry(String nodeFailRetry)
	{
		this.nodeFailRetry = nodeFailRetry;
	}
	public String getNodeBackup()
	{
		return nodeBackup;
	}
	public void setNodeBackup(String nodeBackup)
	{
		this.nodeBackup = nodeBackup;
	}
	public String getNodeWeight()
	{
		return nodeWeight;
	}
	public void setNodeWeight(String nodeWeight)
	{
		this.nodeWeight = nodeWeight;
	}
	public String getNodeMaxConMode()
	{
		return nodeMaxConMode;
	}
	public void setNodeMaxConMode(String nodeMaxConMode)
	{
		this.nodeMaxConMode = nodeMaxConMode;
	}
	public String getNodeTimeout()
	{
		return nodeTimeout;
	}
	public void setNodeTimeout(String nodeTimeout)
	{
		this.nodeTimeout = nodeTimeout;
	}
}
