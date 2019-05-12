package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

public class OBDtoLicenseInfoAlteon
{
	private 	String name;
	private		String status;
	@Override
	public String toString()
	{
		return "OBDtoLicenseInfoAlteon [name=" + name + ", status=" + status + "]";
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
}
