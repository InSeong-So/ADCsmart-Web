package kr.openbase.adcsmart.service.impl.dto;

import java.util.ArrayList;

public class OBDtoMonTotalReal
{
	ArrayList<OBDtoMonTotalRealOne> realList;
	OBDtoMonTotalRealCondition condition; //필터, 조회 조건 목록과 현재 선택
	
	@Override
	public String toString()
	{
		return "OBDtoMonTotalReal [realList=" + realList + ", condition="
				+ condition + "]";
	}
	public ArrayList<OBDtoMonTotalRealOne> getRealList()
	{
		return realList;
	}
	public void setRealList(ArrayList<OBDtoMonTotalRealOne> realList)
	{
		this.realList = realList;
	}
	public OBDtoMonTotalRealCondition getCondition()
	{
		return condition;
	}
	public void setCondition(OBDtoMonTotalRealCondition condition)
	{
		this.condition = condition;
	}
}
