package kr.openbase.adcsmart.service.impl.pas.handler.dto;

public class OBDtoLicenseInfoPAS
{
	private String status		="";
	private String expiredDate	="";
	
	@Override
	public String toString()
	{
		return "OBDtoLicenseInfoPAS [status=" + status + ", expiredDate="
				+ expiredDate + "]";
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getExpiredDate()
	{
		return expiredDate;
	}
	public void setExpiredDate(String expiredDate)
	{
		this.expiredDate = expiredDate;
	}
}
