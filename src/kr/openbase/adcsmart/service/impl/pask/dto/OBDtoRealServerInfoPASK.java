package kr.openbase.adcsmart.service.impl.pask.dto;

public class OBDtoRealServerInfoPASK
{
	private String  dbIndex;
	private Integer index;
	private String  name;
	private String  ipAddress;
	private Integer rport;
	private Integer state;
	
	@Override
	public String toString()
	{
		return "OBDtoRealServerInfoPASK [dbIndex=" + dbIndex + ", index="
				+ index + ", name=" + name + ", ipAddress=" + ipAddress
				+ ", rport=" + rport + ", state=" + state + "]";
	}

	public String getDbIndex()
	{
		return dbIndex;
	}
	public void setDbIndex(String dbIndex)
	{
		this.dbIndex = dbIndex;
	}
	public Integer getIndex()
	{
		return index;
	}
	public void setIndex(Integer index)
	{
		this.index = index;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public Integer getRport()
	{
		return rport;
	}
	public void setRport(Integer rport)
	{
		this.rport = rport;
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
