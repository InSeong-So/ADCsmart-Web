package kr.openbase.adcsmart.service.snmp.dto;

public class DtoSnmpStatPoolMembers extends DtoSnmpStat
{
	private String poolName;
	private String memberIP;
	private int servicePort;
	private String nodeName;
	private int state;

	@Override
	public String toString()
	{
		return "DtoSnmpStatPoolMembers [poolName=" + poolName + ", memberIP="
				+ memberIP + ", servicePort=" + servicePort + ", nodeName="
				+ nodeName + ", state=" + state + "]";
	}

	public int getState()
	{
		return state;
	}
	public void setState(int state)
	{
		this.state = state;
	}
	public String getPoolName()
	{
		return poolName;
	}
	public void setPoolName(String poolName)
	{
		this.poolName = poolName;
	}
	public String getMemberIP()
	{
		return memberIP;
	}
	public void setMemberIP(String memberIP)
	{
		this.memberIP = memberIP;
	}
	public int getServicePort()
	{
		return servicePort;
	}
	public void setServicePort(int servicePort)
	{
		this.servicePort = servicePort;
	}
	public String getNodeName()
	{
		return nodeName;
	}
	public void setNodeName(String nodeName)
	{
		this.nodeName = nodeName;
	}
}
