package kr.openbase.adcsmart.service.snmp.f5;

public class DtoSnmpNodeF5
{
	private String nodeIpAddr;
	private Integer nodeMaxConns;
	private Integer nodeState;
	private String nodeName;
	private Integer nodeAvail;
	
	@Override
	public String toString()
	{
		return "DtoSnmpNodeF5 [nodeIpAddr=" + nodeIpAddr + ", nodeMaxConns="
				+ nodeMaxConns + ", nodeState=" + nodeState + ", nodeName="
				+ nodeName + ", nodeAvail=" + nodeAvail + "]";
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
}
