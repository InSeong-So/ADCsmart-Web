package kr.openbase.adcsmart.service.vdirect.dto;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigSlbDto
{
	private String virtualServerIp;
	private String virtualServerName;
	private String realServerID;
	private ArrayList<String> realServerIDs;
	private String realServerIp;
	private ArrayList<String> realServerIps;
	private String groupName;
	private String alteonName;
	private String virtualServerID;
	private String service;
	private String type;
	private String groupID;
	private String rport;
	private HashMap<String, String> vrrp;

	public HashMap<String, String> getVrrp()
	{
		return vrrp;
	}

	public void setVrrp(HashMap<String, String> vrrp)
	{
		this.vrrp = vrrp;
	}

	public String getVirtualServerIp()
	{
		return virtualServerIp;
	}

	public void setVirtualServerIp(String virtualServerIp)
	{
		this.virtualServerIp = virtualServerIp;
	}

	public String getVirtualServerName()
	{
		return virtualServerName;
	}

	public void setVirtualServerName(String virtualServerName)
	{
		this.virtualServerName = virtualServerName;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public String getAlteonName()
	{
		return alteonName;
	}

	public void setAlteonName(String alteonName)
	{
		this.alteonName = alteonName;
	}

	public String getVirtualServerID()
	{
		return virtualServerID;
	}

	public void setVirtualServerID(String virtualServerID)
	{
		this.virtualServerID = virtualServerID;
	}

	public String getService()
	{
		return service;
	}

	public void setService(String service)
	{
		this.service = service;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getGroupID()
	{
		return groupID;
	}

	public void setGroupID(String groupID)
	{
		this.groupID = groupID;
	}

	public String getRport()
	{
		return rport;
	}

	public void setRport(String rport)
	{
		this.rport = rport;
	}

	public String getRealServerID()
	{
		return realServerID;
	}

	public void setRealServerID(String realServerID)
	{
		this.realServerID = realServerID;
	}

	public ArrayList<String> getRealServerIDs()
	{
		return realServerIDs;
	}

	public void setRealServerIDs(ArrayList<String> realServerIDs)
	{
		this.realServerIDs = realServerIDs;
	}

	public String getRealServerIp()
	{
		return realServerIp;
	}

	public void setRealServerIp(String realServerIp)
	{
		this.realServerIp = realServerIp;
	}

	public ArrayList<String> getRealServerIps()
	{
		return realServerIps;
	}

	public void setRealServerIps(ArrayList<String> realServerIps)
	{
		this.realServerIps = realServerIps;
	}

}
