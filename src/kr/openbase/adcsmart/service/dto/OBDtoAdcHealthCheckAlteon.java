package kr.openbase.adcsmart.service.dto;

import java.io.Serializable;

public class OBDtoAdcHealthCheckAlteon implements Serializable
{
	private static final long serialVersionUID = 20131113L;
	private String 	dbIndex;
	private String	id;
	private String  name;
	private String  type; //healthcheck type
	private String  destinationIp; //v29에서 type = logexp일때는 expression
	private String	extra;
	
	public OBDtoAdcHealthCheckAlteon()
	{
	}
	public OBDtoAdcHealthCheckAlteon(OBDtoAdcHealthCheckAlteon obj)
	{
		if(obj!=null)
		{
			this.dbIndex = obj.getDbIndex();
			this.id = obj.getId();
			this.name = obj.getName();
			this.type = obj.getType();
			this.destinationIp = obj.getDestinationIp();
			this.extra= obj.getExtra();
		}
	}
	@Override
	public String toString()
	{
		return "OBDtoAdcHealthCheckAlteon [dbIndex=" + dbIndex + ", id=" + id
				+ ", name=" + name + ", type=" + type + ", destinationIp="
				+ destinationIp + ", extra=" + extra + "]";
	}
	public String getDbIndex()
	{
		return dbIndex;
	}
	public void setDbIndex(String dbIndex)
	{
		this.dbIndex = dbIndex;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
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
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getDestinationIp()
	{
		return destinationIp;
	}
	public void setDestinationIp(String destinationIp)
	{
		this.destinationIp = destinationIp;
	}
	public String getExtra()
	{
		return extra;
	}
	public void setExtra(String extra)
	{
		this.extra = extra;
	}
}
