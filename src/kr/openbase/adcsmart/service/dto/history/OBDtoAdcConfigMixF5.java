package kr.openbase.adcsmart.service.dto.history;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;

public class OBDtoAdcConfigMixF5
{
	private ArrayList<OBDtoAdcVServerF5> vsList;
	private ArrayList<OBDtoAdcPoolF5> poolList;
	private ArrayList<OBDtoAdcPoolMemberF5> poolMemberList;
	private ArrayList<OBDtoAdcNodeF5> nodeList;
	@Override
	public String toString()
	{
		return "OBDtoAdcHistoryMixF5 [vsList=" + vsList + ", poolList="
				+ poolList + ", poolMemberList=" + poolMemberList
				+ ", nodeList=" + nodeList + "]";
	}
	public ArrayList<OBDtoAdcVServerF5> getVsList()
	{
		return vsList;
	}
	public void setVsList(ArrayList<OBDtoAdcVServerF5> vsList)
	{
		this.vsList = vsList;
	}
	public ArrayList<OBDtoAdcPoolF5> getPoolList()
	{
		return poolList;
	}
	public void setPoolList(ArrayList<OBDtoAdcPoolF5> poolList)
	{
		this.poolList = poolList;
	}
	public ArrayList<OBDtoAdcPoolMemberF5> getPoolMemberList()
	{
		return poolMemberList;
	}
	public void setPoolMemberList(ArrayList<OBDtoAdcPoolMemberF5> poolMemberList)
	{
		this.poolMemberList = poolMemberList;
	}
	public ArrayList<OBDtoAdcNodeF5> getNodeList()
	{
		return nodeList;
	}
	public void setNodeList(ArrayList<OBDtoAdcNodeF5> nodeList)
	{
		this.nodeList = nodeList;
	}
}