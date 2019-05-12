package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

public class OBDtoNetworkMapVService
{
	private String 	name;
	private Integer alteonID;// for only alteon
	private Integer port;// for only alteon
	private Integer status;
	private ArrayList<OBDtoNetworkMapMember> memberList;
	
	@Override
	public String toString()
	{
		return "OBDtoNetworkMapVService [name=" + name + ", alteonID="
				+ alteonID + ", port=" + port + ", status=" + status
				+ ", memberList=" + memberList + "]";
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Integer getAlteonID()
	{
		return alteonID;
	}
	public void setAlteonID(Integer alteonID)
	{
		this.alteonID = alteonID;
	}
	public Integer getPort()
	{
		return port;
	}
	public void setPort(Integer port)
	{
		this.port = port;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public ArrayList<OBDtoNetworkMapMember> getMemberList()
	{
		return memberList;
	}
	public void setMemberList(ArrayList<OBDtoNetworkMapMember> memberList)
	{
		this.memberList = memberList;
	}
}
