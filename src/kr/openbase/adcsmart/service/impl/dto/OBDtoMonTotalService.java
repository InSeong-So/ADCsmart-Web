package kr.openbase.adcsmart.service.impl.dto;

import java.util.ArrayList;

public class OBDtoMonTotalService
{
	ArrayList<OBDtoMonTotalServiceOne> serviceList;
	OBDtoMonTotalServiceCondition condition; //필터, 조회 조건 목록과 현재 선택
	@Override
	public String toString()
	{
		return "OBDtoMonTotalService [serviceList=" + serviceList
				+ ", condition=" + condition + "]";
	}
	public ArrayList<OBDtoMonTotalServiceOne> getServiceList()
	{
		return serviceList;
	}
	public void setServiceList(ArrayList<OBDtoMonTotalServiceOne> serviceList)
	{
		this.serviceList = serviceList;
	}
	public OBDtoMonTotalServiceCondition getCondition()
	{
		return condition;
	}
	public void setCondition(OBDtoMonTotalServiceCondition condition)
	{
		this.condition = condition;
	}
}
