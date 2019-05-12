package kr.openbase.adcsmart.service.dto.history;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcNodePAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolMemberPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;

public class OBDtoAdcConfigMixPAS
{
	private ArrayList<OBDtoAdcVServerPAS> vsList;
	private ArrayList<OBDtoAdcPoolPAS> poolList;
	private ArrayList<OBDtoAdcPoolMemberPAS> poolMemberList;
	private ArrayList<OBDtoAdcNodePAS> nodeList;
	@Override
	public String toString()
	{
		return "OBDtoAdcConfigMixPAS [vsList=" + vsList + ", poolList="
				+ poolList + ", poolMemberList=" + poolMemberList
				+ ", nodeList=" + nodeList + "]";
	}
	public ArrayList<OBDtoAdcVServerPAS> getVsList()
	{
		return vsList;
	}
	public void setVsList(ArrayList<OBDtoAdcVServerPAS> vsList)
	{
		this.vsList = vsList;
	}
	public ArrayList<OBDtoAdcPoolPAS> getPoolList()
	{
		return poolList;
	}
	public void setPoolList(ArrayList<OBDtoAdcPoolPAS> poolList)
	{
		this.poolList = poolList;
	}
	public ArrayList<OBDtoAdcPoolMemberPAS> getPoolMemberList()
	{
		return poolMemberList;
	}
	public void setPoolMemberList(ArrayList<OBDtoAdcPoolMemberPAS> poolMemberList)
	{
		this.poolMemberList = poolMemberList;
	}
	public ArrayList<OBDtoAdcNodePAS> getNodeList()
	{
		return nodeList;
	}
	public void setNodeList(ArrayList<OBDtoAdcNodePAS> nodeList)
	{
		this.nodeList = nodeList;
	}
}