package kr.openbase.adcsmart.service.impl.pask.handler.dto;

public class OBDtoLicenseInfoPASK
{
	private String status		="";
	private String expiredDate	="";
	@Override
	public String toString()
	{
		return "OBDtoLicenseInfoPASK [status=" + status + ", expiredDate=" + expiredDate + "]";
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
