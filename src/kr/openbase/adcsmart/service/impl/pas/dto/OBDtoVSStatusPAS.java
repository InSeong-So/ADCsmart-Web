package kr.openbase.adcsmart.service.impl.pas.dto;

public class OBDtoVSStatusPAS
{
	private String vsName;
	private int status;
	@Override
	public String toString()
	{
		return "OBDtoVSStatusPAS [vsName=" + vsName + ", status=" + status
				+ "]";
	}
	public String getVsName()
	{
		return vsName;
	}
	public void setVsName(String vsName)
	{
		this.vsName = vsName;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
}
