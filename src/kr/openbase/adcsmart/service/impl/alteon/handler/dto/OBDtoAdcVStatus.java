package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

class OBDtoAdcVStatus
{
	private int alteonID;
	private String name;
	private int status;
	
	public void setAlteonID(int alteonID)
	{
		this.alteonID = alteonID;
	}
	public int getAlteonID()
	{
		return this.alteonID;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return this.name;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}
	public int getStatus()
	{
		return this.status;
	}
	@Override
	public String toString() 
	{
		return "OBDtoAdcVStatus [alteonID=" + this.alteonID + ", name=" + this.name + ", status="
				+ this.status + "]";
	}
}
