package kr.openbase.adcsmart.service.impl.pask.dto;

import java.io.Serializable;

public class OBDtoAdcPoolMemberPASK implements Serializable
{
	private static final long 	serialVersionUID = 10L;
	private String 				dbIndex; //pool에서의 member 인덱스, real의 db 인덱스가 아님.
	private String 				name; //real 이름
	private Integer 			id; //real id
	private String 				ipAddress;// ip 주소가 입력된다.
	private Integer 			port;
	private Integer 			state;
	private Integer 			status;

	public OBDtoAdcPoolMemberPASK()
	{
	}
	public OBDtoAdcPoolMemberPASK(OBDtoAdcPoolMemberPASK obj)
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
		return "OBDtoAdcPoolMemberPASK [dbIndex=" + dbIndex + ", name=" + name + ", id=" + id + ", ipAddress=" + ipAddress + ", port=" + port + ", state=" + state + ", status=" + status + "]";
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
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
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
