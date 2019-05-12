package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

public class OBDtoInfoStgAlteon
{
	private int	stgID;
	private	int	state;
	@Override
	public String toString()
	{
		return "OBDtoInfoStgAlteon [stgID=" + stgID + ", status=" + state + "]";
	}
	public int getStgID()
	{
		return stgID;
	}
	public void setStgID(int stgID)
	{
		this.stgID = stgID;
	}
	public int getState()
	{
		return state;
	}
	public void setState(int status)
	{
		this.state = status;
	}
}
