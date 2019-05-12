package kr.openbase.adcsmart.service.impl.pas.dto;

import java.io.Serializable;

public class OBDtoAdcPoolMemberPAS implements Serializable
{
	private static final long serialVersionUID = 10L;
	private String 	dbIndex;
	private String 	name;
	private Integer id;
	private String 	ipAddress;// ip 주소가 입력된다.
	private Integer port;
	private Integer state;
	private Integer status;
	
	public OBDtoAdcPoolMemberPAS()
	{
	}
	public OBDtoAdcPoolMemberPAS(OBDtoAdcPoolMemberPAS obj)
	{
		if(obj!=null)
		{
			this.dbIndex = obj.dbIndex;
			this.id      = obj.id;
			this.name = obj.getName();
			this.ipAddress = obj.ipAddress;
			this.port = obj.port;
			this.state = obj.state;
			this.status = obj.status;
		}
	}
	
	@Override
	public String toString()
	{
		return "OBDtoAdcPoolMemberPAS [dbIndex=" + dbIndex + ", name=" + name
				+ ", id=" + id + ", ipAddress=" + ipAddress + ", port=" + port
				+ ", state=" + state + ", status=" + status + "]";
	}
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public String getDbIndex()
	{
		return dbIndex;
	}
	public void setDbIndex(String dbIndex)
	{
		this.dbIndex = dbIndex;
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
	public Integer getPort()
	{
		return port;
	}
	public void setPort(Integer port)
	{
		this.port = port;
	}
	public Integer getState()
	{
		return state;
	}
	public void setState(Integer state)
	{
		this.state = state;
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
