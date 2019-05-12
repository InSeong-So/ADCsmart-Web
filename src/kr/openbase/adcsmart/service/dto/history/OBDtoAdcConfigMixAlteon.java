package kr.openbase.adcsmart.service.dto.history;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVService;

public class OBDtoAdcConfigMixAlteon
{
	private ArrayList<OBDtoAdcVServerAlteon> vsList;
	private ArrayList<OBDtoAdcVService> vserviceList;
	private ArrayList<OBDtoAdcPoolAlteon> poolList;
	private ArrayList<OBDtoAdcPoolMemberAlteon> poolMemberList;
	private ArrayList<OBDtoAdcNodeAlteon> nodeList;
	
	public ArrayList<OBDtoAdcVServerAlteon> getVsList()
	{
		return vsList;
	}
	public void setVsList(ArrayList<OBDtoAdcVServerAlteon> vsList)
	{
		this.vsList = vsList;
	}
	public ArrayList<OBDtoAdcVService> getVserviceList()
	{
		return vserviceList;
	}
	public void setVserviceList(ArrayList<OBDtoAdcVService> vserviceList)
	{
		this.vserviceList = vserviceList;
	}
	public ArrayList<OBDtoAdcPoolAlteon> getPoolList()
	{
		return poolList;
	}
	public void setPoolList(ArrayList<OBDtoAdcPoolAlteon> poolList)
	{
		this.poolList = poolList;
	}
	public ArrayList<OBDtoAdcPoolMemberAlteon> getPoolMemberList()
	{
		return poolMemberList;
	}
	public void setPoolMemberList(ArrayList<OBDtoAdcPoolMemberAlteon> poolMemberList)
	{
		this.poolMemberList = poolMemberList;
	}
	public ArrayList<OBDtoAdcNodeAlteon> getNodeList()
	{
		return nodeList;
	}
	public void setNodeList(ArrayList<OBDtoAdcNodeAlteon> nodeList)
	{
		this.nodeList = nodeList;
	}
	
	@Override
	public String toString()
	{
		return "OBDtoAdcConfigMixAlteon [vsList=" + vsList + ", vserviceList="
				+ vserviceList + ", poolList=" + poolList + ", poolMemberList="
				+ poolMemberList + ", nodeList=" + nodeList + "]";
	}
}