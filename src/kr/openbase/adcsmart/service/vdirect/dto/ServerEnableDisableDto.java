package kr.openbase.adcsmart.service.vdirect.dto;

import java.util.ArrayList;

public class ServerEnableDisableDto
{
	private String alteonName;
	private String state;
	private ArrayList<String> serverID;
	private String groupID;
	
	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getAlteonName()
	{
		return alteonName;
	}

	public void setAlteonName(String alteonName)
	{
		this.alteonName = alteonName;
	}

	public ArrayList<String> getServerID()
	{
		return serverID;
	}

	public void setServerID(ArrayList<String> serverID)
	{
		this.serverID = serverID;
	}

	public String getGroupID()
	{
		return groupID;
	}

	public void setGroupID(String groupID)
	{
		this.groupID = groupID;
	}

}
