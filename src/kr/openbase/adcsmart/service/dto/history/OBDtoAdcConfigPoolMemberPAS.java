package kr.openbase.adcsmart.service.dto.history;

import java.io.Serializable;

import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolMemberPAS;

public class OBDtoAdcConfigPoolMemberPAS implements Serializable
{
	private static final long serialVersionUID = 10L;
	private int change;
	private int stateChange;
	
	private OBDtoAdcPoolMemberPAS memberOld;
	private OBDtoAdcPoolMemberPAS memberNew;
	
	@Override
	public String toString()
	{
		return "OBDtoAdcConfigPoolMemberPAS [change=" + change
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
	public OBDtoAdcPoolMemberPAS getMemberOld()
	{
		return memberOld;
	}
	public void setMemberOld(OBDtoAdcPoolMemberPAS memberOld)
	{
		this.memberOld = memberOld;
	}
	public OBDtoAdcPoolMemberPAS getMemberNew()
	{
		return memberNew;
	}
	public void setMemberNew(OBDtoAdcPoolMemberPAS memberNew)
	{
		this.memberNew = memberNew;
	}
}