package kr.openbase.adcsmart.service.dto;


public class OBDtoActiveStandbyState
{
	public static final int ACTIVE_STANDBY_STATE_UNKNOWN 	= OBDtoAdcInfo.ACTIVE_STANDBY_STATE_UNKNOWN;
	public static final int ACTIVE_STANDBY_STATE_ACTIVE 	= OBDtoAdcInfo.ACTIVE_STANDBY_STATE_ACTIVE;
	public static final int ACTIVE_STANDBY_STATE_STANDBY 	= OBDtoAdcInfo.ACTIVE_STANDBY_STATE_STANDBY;
	
	private Integer prevState = ACTIVE_STANDBY_STATE_UNKNOWN;
	private Integer currState = ACTIVE_STANDBY_STATE_UNKNOWN;
	@Override
	public String toString()
	{
		return String.format("OBDtoActiveStandbyState [prevState=%s, currState=%s]", prevState, currState);
	}
	public Integer getPrevState()
	{
		return prevState;
	}
	public void setPrevState(Integer prevState)
	{
		this.prevState = prevState;
	}
	public Integer getCurrState()
	{
		return currState;
	}
	public void setCurrState(Integer currState)
	{
		this.currState = currState;
	}
}
