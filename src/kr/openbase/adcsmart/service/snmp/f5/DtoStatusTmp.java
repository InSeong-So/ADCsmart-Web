package kr.openbase.adcsmart.service.snmp.f5;

public class DtoStatusTmp
{
	private int avail;
	private int enable;
	private int status;
	private String vsName;//for f5
	private String poolName;//for f5
	private String nodeIP;//for f5
	private String nodeName;//for f5
	private int	   srvPort;//for f5
	@Override
	public String toString()
	{
		return "DtoStatusTmp [avail=" + avail + ", enable=" + enable + ", status=" + status + ", vsName=" + vsName + ", poolName=" + poolName + ", nodeIP=" + nodeIP + ", nodeName=" + nodeName + ", srvPort=" + srvPort + "]";
	}
	public String getNodeName()
	{
		return nodeName;
	}
	public void setNodeName(String nodeName)
	{
		this.nodeName = nodeName;
	}
	public String getVsName()
	{
		return vsName;
	}
	public void setVsName(String vsName)
	{
		this.vsName = vsName;
	}
	public String getPoolName()
	{
		return poolName;
	}
	public void setPoolName(String poolName)
	{
		this.poolName = poolName;
	}
	public String getNodeIP()
	{
		return nodeIP;
	}
	public void setNodeIP(String nodeIP)
	{
		this.nodeIP = nodeIP;
	}
	public int getSrvPort()
	{
		return srvPort;
	}
	public void setSrvPort(int srvPort)
	{
		this.srvPort = srvPort;
	}
	public int getAvail()
	{
		return avail;
	}
	public void setAvail(int avail)
	{
		this.avail = avail;
	}
	public int getEnable()
	{
		return enable;
	}
	public void setEnable(int enable)
	{
		this.enable = enable;
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
