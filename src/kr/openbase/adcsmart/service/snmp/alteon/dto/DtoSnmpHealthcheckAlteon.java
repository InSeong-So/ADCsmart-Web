package kr.openbase.adcsmart.service.snmp.alteon.dto;

public class DtoSnmpHealthcheckAlteon
{
	private String id;
	private String name;
	private String type;
	private String destinationIp; //v29에서 type = logexp일때는 expression
	
	@Override
	public String toString()
	{
		return "DtoSnmpHealthcheckAlteon [id=" + id + ", name=" + name
				+ ", type=" + type + ", destinationIp=" + destinationIp + "]";
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
}
