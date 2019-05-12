package kr.openbase.adcsmart.service.dto;

public class OBDtoNetworkMapMember
{
	private String ipAddress;
	private String nodeIndex;
	private Integer port;//for f5
	private String addPort;//for alteon
	private Integer status;
	private Integer bakupType;
	private String bakupIndex;
	
	@Override
    public String toString()
    {
        return "OBDtoNetworkMapMember [ipAddress=" + ipAddress + ", nodeIndex="
                + nodeIndex + ", port=" + port + ", addPort=" + addPort
                + ", status=" + status + ", bakupType=" + bakupType
                + ", bakupIndex=" + bakupIndex + "]";
    }

	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public String getNodeIndex()
	{
		return nodeIndex;
	}
	public void setNodeIndex(String nodeIndex)
	{
		this.nodeIndex = nodeIndex;
	}
	public Integer getPort()
	{
		return port;
	}
	public void setPort(Integer port)
	{
		this.port = port;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public Integer getBakupType()
	{
		return bakupType;
	}

	public void setBakupType(Integer bakupType)
	{
		this.bakupType = bakupType;
	}

	public String getBakupIndex()
	{
		return bakupIndex;
	}

	public void setBakupIndex(String bakupIndex)
	{
		this.bakupIndex = bakupIndex;
	}

    public String getAddPort()
    {
        return addPort;
    }

    public void setAddPort(String addPort)
    {
        this.addPort = addPort;
    }
}
