package kr.openbase.adcsmart.service.dto.history;

import java.io.Serializable;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberAlteon;

public class OBDtoAdcConfigPoolMemberAlteon implements Serializable
{
	private static final long serialVersionUID = 10L;
	private int change;
	private int stateChange;
	
	private OBDtoAdcPoolMemberAlteon memberOld;
	private OBDtoAdcPoolMemberAlteon memberNew;
	
	@Override
	public String toString()
	{
		return "OBDtoAdcConfigPoolMemberAlteon [change=" + change
				+ ", stateChange=" + stateChange + ", memberOld=" + memberOld
				+ ", memberNew=" + memberNew + "]";
	}
	public int getChange()
	{
		return change;
	}
	public void setChange(int change)
	{
		this.change = change;
	}
	public int getStateChange()
	{
		return stateChange;
	}
	public void setStateChange(int stateChange)
	{
		this.stateChange = stateChange;
	}
	public OBDtoAdcPoolMemberAlteon getMemberOld()
	{
		return memberOld;
	}
	public void setMemberOld(OBDtoAdcPoolMemberAlteon memberOld)
	{
		this.memberOld = memberOld;
	}
	public OBDtoAdcPoolMemberAlteon getMemberNew()
	{
		return memberNew;
	}
	public void setMemberNew(OBDtoAdcPoolMemberAlteon memberNew)
	{
		this.memberNew = memberNew;
	}
}