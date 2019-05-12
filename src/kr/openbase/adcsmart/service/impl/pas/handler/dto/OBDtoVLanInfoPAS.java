package kr.openbase.adcsmart.service.impl.pas.handler.dto;

import java.util.ArrayList;

public class OBDtoVLanInfoPAS
{
	private 	String name		="";
	private		int		id		=0;
	private		int		status	=0;
	private		ArrayList<String>	taggedPortList		=null;
	private		ArrayList<String>	untaggedPortList	=null;;
	private		ArrayList<String>	unavailabledPortList=null;
	@Override
	public String toString()
	{
		return "OBDtoVLanInfoPAS [name=" + name + ", id=" + id + ", status="
				+ status + ", taggedPortList=" + taggedPortList
				+ ", untaggedPortList=" + untaggedPortList
				+ ", unavailabledPortList=" + unavailabledPortList + "]";
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public ArrayList<String> getTaggedPortList()
	{
		return taggedPortList;
	}
	public void setTaggedPortList(ArrayList<String> taggedPortList)
	{
		this.taggedPortList = taggedPortList;
	}
	public ArrayList<String> getUntaggedPortList()
	{
		return untaggedPortList;
	}
	public void setUntaggedPortList(ArrayList<String> untaggedPortList)
	{
		this.untaggedPortList = untaggedPortList;
	}
	public ArrayList<String> getUnavailabledPortList()
	{
		return unavailabledPortList;
	}
	public void setUnavailabledPortList(ArrayList<String> unavailabledPortList)
	{
		this.unavailabledPortList = unavailabledPortList;
	};
	
}
