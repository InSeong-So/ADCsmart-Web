package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoConnStatus
{
	private boolean activeStatus;
	private boolean standbyStatus;
	
	public void setActiveStatus(boolean activeStatus)
	{
		this.activeStatus = activeStatus;
	}

	public boolean getActiveStatus()
	{
		return this.activeStatus;
	}
	
	public void setStandbyStatus(boolean standbyStatus)
	{
		this.standbyStatus = standbyStatus;
	}

	public boolean getStandbyStatus()
	{
		return this.standbyStatus;
	}
}
