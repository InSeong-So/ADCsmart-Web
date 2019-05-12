package kr.openbase.adcsmart.service.impl.dto;

import java.util.ArrayList;

public class OBDtoMonTotalAdc
{
	ArrayList<OBDtoMonTotalAdcOne> adcList;
	OBDtoMonTotalAdcCondition condition; //필터, 조회 조건 목록과 현재 선택
	
	@Override
	public String toString()
	{
		return "OBDtoMonTotalAdc [adcList=" + adcList + ", condition="
				+ condition + "]";
	}

	public ArrayList<OBDtoMonTotalAdcOne> getAdcList()
	{
		return adcList;
	}
	public void setAdcList(ArrayList<OBDtoMonTotalAdcOne> adcList)
	{
		this.adcList = adcList;
	}
	public OBDtoMonTotalAdcCondition getCondition()
	{
		return condition;
	}
	public void setCondition(OBDtoMonTotalAdcCondition condition)
	{
		this.condition = condition;
	}
}
