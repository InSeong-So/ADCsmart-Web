package kr.openbase.adcsmart.service.impl.dto;

import java.util.ArrayList;

public class OBDtoMonTotalGroup
{
	ArrayList<OBDtoMonTotalGroupOne> groupList;
	OBDtoMonTotalGroupCondition condition; //필터, 조회 조건 목록과 현재 선택

	@Override
	public String toString()
	{
		return "OBDtoMonTotalGroup [groupList=" + groupList + ", condition="
				+ condition + "]";
	}
	public ArrayList<OBDtoMonTotalGroupOne> getGroupList()
	{
		return groupList;
	}
	public void setGroupList(ArrayList<OBDtoMonTotalGroupOne> groupList)
	{
		this.groupList = groupList;
	}
	public OBDtoMonTotalGroupCondition getCondition()
	{
		return condition;
	}
	public void setCondition(OBDtoMonTotalGroupCondition condition)
	{
		this.condition = condition;
	}
}
