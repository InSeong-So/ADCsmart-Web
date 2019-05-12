package kr.openbase.adcsmart.service.impl.pask.dto;

import java.io.Serializable;

public class OBDtoAdcHealthCheckPASK implements Serializable
{
	private static final long serialVersionUID = 10L;
	private String 	dbIndex;
	private Integer	id;
	private String  name;
	private Integer type; //healthcheck type, icmp, tcp, http
	private Integer port;
	private Integer interval;
	private Integer timeout;
	private Integer state;
	
	public OBDtoAdcHealthCheckPASK()
	{
	}
	OBDtoAdcHealthCheckPASK(OBDtoAdcHealthCheckPASK obj)
	{
		if(obj!=null)
		{
			this.dbIndex = obj.getDbIndex();
			this.id = obj.getId();
			this.name = obj.getName();
			this.type = obj.getType();
			this.port = obj.getPort();
			this.interval = obj.getInterval();
			this.timeout = obj.getTimeout();
			this.state = obj.getState();
		}
	}
	
	@Override
	public String toString()
	{
		return "OBDtoAdcHealthCheckPASK [dbIndex=" + dbIndex + ", id=" + id + ", name=" + name + ", type=" + type + ", port=" + port + ", interval=" + interval + ", timeout=" + timeout + ", state=" + state + "]";
	}
	public String getDbIndex()
	{
		return dbIndex;
	}
	public void setDbIndex(String dbIndex)
	{
		this.dbIndex = dbIndex;
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
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
	}
	public Integer getPort()
	{
		return port;
	}
	public void setPort(Integer port)
	{
		this.port = port;
	}
	public Integer getInterval()
	{
		return interval;
	}
	public void setInterval(Integer interval)
	{
		this.interval = interval;
	}
	public Integer getTimeout()
	{
		return timeout;
	}
	public void setTimeout(Integer timeout)
	{
		this.timeout = timeout;
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
