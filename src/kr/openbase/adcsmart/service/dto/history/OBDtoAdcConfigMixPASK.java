package kr.openbase.adcsmart.service.dto.history;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcNodePASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolMemberPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;

public class OBDtoAdcConfigMixPASK
{
	private ArrayList<OBDtoAdcVServerPASK> vsList;
	private ArrayList<OBDtoAdcPoolPASK> poolList;
	private ArrayList<OBDtoAdcPoolMemberPASK> poolMemberList;
	private ArrayList<OBDtoAdcNodePASK> nodeList;
	@Override
	public String toString()
	{
		return "OBDtoAdcConfigMixPASK [vsList=" + vsList + ", poolList="
				+ poolList + ", poolMemberList=" + poolMemberList
				+ ", nodeList=" + nodeList + "]";
	}
	public ArrayList<OBDtoAdcVServerPASK> getVsList()
	{
		return vsList;
	}
	public void setVsList(ArrayList<OBDtoAdcVServerPASK> vsList)
	{
		this.vsList = vsList;
	}
	public ArrayList<OBDtoAdcPoolPASK> getPoolList()
	{
		return poolList;
	}
	public void setPoolList(ArrayList<OBDtoAdcPoolPASK> poolList)
	{
		this.poolList = poolList;
	}
	public ArrayList<OBDtoAdcPoolMemberPASK> getPoolMemberList()
	{
		return poolMemberList;
	}
	public void setPoolMemberList(ArrayList<OBDtoAdcPoolMemberPASK> poolMemberList)
	{
		this.poolMemberList = poolMemberList;
	}
	public ArrayList<OBDtoAdcNodePASK> getNodeList()
	{
		return nodeList;
	}
	public void setNodeList(ArrayList<OBDtoAdcNodePASK> nodeList)
	{
		this.nodeList = nodeList;
	}
}