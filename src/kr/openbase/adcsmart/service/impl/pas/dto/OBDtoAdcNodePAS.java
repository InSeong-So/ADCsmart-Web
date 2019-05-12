package kr.openbase.adcsmart.service.impl.pas.dto;

public class OBDtoAdcNodePAS
{
	private Integer id;
	private String name;
	private String dbIndex;
	private String ipAddress;
	private Integer state;
	@Override
	public String toString()
	{
		return "OBDtoAdcNodePAS [id=" + id + ", name=" + name + ", dbIndex="
				+ dbIndex + ", ipAddress=" + ipAddress + ", state=" + state
				+ "]";
	}
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getDbIndex()
	{
		return dbIndex;
	}
	public void setDbIndex(String dbIndex)
	{
		this.dbIndex = dbIndex;
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
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
