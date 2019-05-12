package kr.openbase.adcsmart.web.facade.dto;

public class NetworkMapVsBackupDto
{
	private Integer bakType; //group Backup과 real Backup구분
	private String ipAddress;
	private String nodeIndex;
	private Integer idType; // Group과 Real 구분
	private String poolIndex; // Pool 인덱스 저장
	private Integer port;//for f5
	private Integer status;
	@Override
	public String toString()
	{
		return "NetworkMapVsBackupDto [bakType=" + bakType + ", ipAddress=" + ipAddress + ", nodeIndex=" 
				+ nodeIndex + ", idType=" + idType + ", poolIndex=" + poolIndex + ", port=" + port + ", status=" + status + "]";
	}
	public Integer getBakType()
	{
		return bakType;
	}
	public void setBakType(Integer bakType)
	{
		this.bakType = bakType;
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
	public Integer getIdType()
	{
		return idType;
	}
	public void setIdType(Integer idType)
	{
		this.idType = idType;
	}
	public String getPoolIndex()
	{
		return poolIndex;
	}
	public void setPoolIndex(String poolIndex)
	{
		this.poolIndex = poolIndex;
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
}
