package kr.openbase.adcsmart.service.impl.f5;

import java.util.ArrayList;

class DtoPoolMemberTraffic
{
	private String name;
	private ArrayList<DtoNodeTraffic> nodeList;
	
	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return this.name;
	}

	public void setNodeList(ArrayList<DtoNodeTraffic> nodeList)
	{
		this.nodeList = nodeList;
	}
	public ArrayList<DtoNodeTraffic> getNodeList()
	{
		return this.nodeList;
	}	
	
	@Override
	public String toString() 
	{
		return "OBDtoMonTrafficPool [name=" + this.name + ", nodeList="
				+ this.nodeList + "]";
	}	
}
