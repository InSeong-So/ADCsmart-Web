package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

import java.util.ArrayList;


class OBDtoAdcVStatusPool
{
//	private String name;
	private int alteonID;
	private ArrayList<OBDtoAdcVStatus> nodeList;
	
//	public void setName(String name)
//	{
//		this.name = name;
//	}
//	public String getName()
//	{
//		return this.name;
//	}

	public void setAlteonID(int alteonID)
	{
		this.alteonID = alteonID;
	}
	public int getAlteonID()
	{
		return this.alteonID;
	}

	public void setNodeList(ArrayList<OBDtoAdcVStatus> nodeList)
	{
		this.nodeList = nodeList;
	}
	public ArrayList<OBDtoAdcVStatus> getNodeList()
	{
		return this.nodeList;
	}

	@Override
	public String toString() 
	{
		return "OBDtoAdcVStatusPool [alteonID=" + this.alteonID + ", nodeList=" + this.nodeList + "]";
	}
}
