package kr.openbase.adcsmart.service.dto.history;

import java.io.Serializable;

import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolMemberPASK;

public class OBDtoAdcConfigPoolMemberPASK implements Serializable
{
	private static final long serialVersionUID = 10L;
	private int change;
	private int stateChange;
	
	private OBDtoAdcPoolMemberPASK memberOld;
	private OBDtoAdcPoolMemberPASK memberNew;
	
	@Override
	public String toString()
	{
		return "OBDtoAdcConfigPoolMemberPASK [change=" + change
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
	public OBDtoAdcPoolMemberPASK getMemberOld()
	{
		return memberOld;
	}
	public void setMemberOld(OBDtoAdcPoolMemberPASK memberOld)
	{
		this.memberOld = memberOld;
	}
	public OBDtoAdcPoolMemberPASK getMemberNew()
	{
		return memberNew;
	}
	public void setMemberNew(OBDtoAdcPoolMemberPASK memberNew)
	{
		this.memberNew = memberNew;
	}
}