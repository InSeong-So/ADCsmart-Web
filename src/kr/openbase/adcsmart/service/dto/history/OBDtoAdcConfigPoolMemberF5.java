package kr.openbase.adcsmart.service.dto.history;

import java.io.Serializable;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberF5;

public class OBDtoAdcConfigPoolMemberF5 implements Serializable
{
	private static final long serialVersionUID = 10L;
	private int change;
	private int stateChange;
	
	private OBDtoAdcPoolMemberF5 memberOld;
	private OBDtoAdcPoolMemberF5 memberNew;
	
	@Override
	public String toString()
	{
		return "OBDtoAdcConfigPoolMemberF5 [change=" + change
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
	public OBDtoAdcPoolMemberF5 getMemberOld()
	{
		return memberOld;
	}
	public void setMemberOld(OBDtoAdcPoolMemberF5 memberOld)
	{
		this.memberOld = memberOld;
	}
	public OBDtoAdcPoolMemberF5 getMemberNew()
	{
		return memberNew;
	}
	public void setMemberNew(OBDtoAdcPoolMemberF5 memberNew)
	{
		this.memberNew = memberNew;
	}
}