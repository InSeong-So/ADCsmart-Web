package kr.openbase.adcsmart.service.snmp.alteon.dto;

import java.util.ArrayList;

public class DtoSnmpNodeAlteon
{
	private String nodeID;
	private String  nodeIpAddr;
	private Integer nodeState;
	private String  nodeName;
	private Integer nodeAvail;
	private Integer nodeStatus;
	private ArrayList<Integer> nodePort;
	private Integer nodeInterval;
	private Integer nodeFailRetry;
	private String nodeBackup;
	private Integer nodeWeight;
	private Integer nodeMaxConns;
	private Integer nodeMaxConMode;
	private Integer nodeTimeout;
	private Integer bakType;
	private String 	bakID;
	
	@Override
	public String toString()
	{
		return "DtoSnmpNodeAlteon [nodeID=" + nodeID + ", nodeIpAddr=" + nodeIpAddr + ", nodeState=" + nodeState 
				+ ", nodeName=" + nodeName + ", nodeAvail=" + nodeAvail + ", nodeStatus=" + nodeStatus + ", nodePort=" 
				+ nodePort + ", nodeInterval=" + nodeInterval + ", nodeFailRetry=" + nodeFailRetry + ", nodeBackup=" 
				+ nodeBackup + ", nodeWeight=" + nodeWeight + ", nodeMaxConns=" + nodeMaxConns + ", nodeMaxConMode=" 
				+ nodeMaxConMode + ", nodeTimeout=" + nodeTimeout + ", bakType=" + bakType + ", bakID=" + bakID + "]";
	}
	public String getNodeID()
	{
		return nodeID;
	}
	public Integer getNodeStatus()
	{
		return nodeStatus;
	}
	public void setNodeStatus(Integer nodeStatus)
	{
		this.nodeStatus = nodeStatus;
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
	public Integer getNodeMaxConns()
	{
		return nodeMaxConns;
	}
	public void setNodeMaxConns(Integer nodeMaxConns)
	{
		this.nodeMaxConns = nodeMaxConns;
	}
	public Integer getNodeState()
	{
		return nodeState;
	}
	public void setNodeState(Integer nodeState)
	{
		this.nodeState = nodeState;
	}
	public String getNodeName()
	{
		return nodeName;
	}
	public void setNodeName(String nodeName)
	{
		this.nodeName = nodeName;
	}
	public Integer getNodeAvail()
	{
		return nodeAvail;
	}
	public void setNodeAvail(Integer nodeAvail)
	{
		this.nodeAvail = nodeAvail;
	}
	public ArrayList<Integer> getNodePort()
	{
		return nodePort;
	}
	public void setNodePort(ArrayList<Integer> nodePort)
	{
		this.nodePort = nodePort;
	}
	public Integer getNodeInterval()
	{
		return nodeInterval;
	}
	public void setNodeInterval(Integer nodeInterval)
	{
		this.nodeInterval = nodeInterval;
	}
	public Integer getNodeFailRetry()
	{
		return nodeFailRetry;
	}
	public void setNodeFailRetry(Integer nodeFailRetry)
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
	public Integer getNodeWeight()
	{
		return nodeWeight;
	}
	public void setNodeWeight(Integer nodeWeight)
	{
		this.nodeWeight = nodeWeight;
	}
	public Integer getNodeMaxConMode()
	{
		return nodeMaxConMode;
	}
	public void setNodeMaxConMode(Integer nodeMaxConMode)
	{
		this.nodeMaxConMode = nodeMaxConMode;
	}
	public Integer getNodeTimeout()
	{
		return nodeTimeout;
	}
	public void setNodeTimeout(Integer nodeTimeout)
	{
		this.nodeTimeout = nodeTimeout;
	}
	public Integer getBakType()
	{
		return bakType;
	}
	public void setBakType(Integer bakType)
	{
		this.bakType = bakType;
	}
	public String getBakID()
	{
		return bakID;
	}
	public void setBakID(String bakID)
	{
		this.bakID = bakID;
	}
}
