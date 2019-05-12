package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidEntity
{
	private String name;
	private String value;
	private String swVersion;
	@Override
	public String toString()
	{
		return "DtoOidEntity [name=" + name + ", value=" + value
				+ ", swVersion=" + swVersion + "]";
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	public String getSwVersion()
	{
		return swVersion;
	}
	public void setSwVersion(String swVersion)
	{
		this.swVersion = swVersion;
	}
}
