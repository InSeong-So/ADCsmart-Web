package kr.openbase.adcsmart.service.dto.history;

import java.io.Serializable;

import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeAlteon;

public class OBDtoAdcConfigNodeAlteon implements Serializable
{
	private static final long serialVersionUID = 10L;
	private int change;
	private int alteonIdChange;
	private int ipAddressChange;
	private int nameChange;

	private OBDtoAdcNodeAlteon nodeOld;
	private OBDtoAdcNodeAlteon nodeNew;
	@Override
	public String toString()
	{
		return "OBDtoAdcConfigNodeAlteon [change=" + change
				+ ", alteonIdChange=" + alteonIdChange + ", ipAddressChange="
				+ ipAddressChange + ", nameChange=" + nameChange + ", nodeOld="
				+ nodeOld + ", nodeNew=" + nodeNew + "]";
	}	
	public int getChange()
	{
		return change;
	}
	public void setChange(int change)
	{
		this.change = change;
	}
	public int getAlteonIdChange()
	{
		return alteonIdChange;
	}
	public void setAlteonIdChange(int alteonIdChange)
	{
		this.alteonIdChange = alteonIdChange;
	}
	public int getIpAddressChange()
	{
		return ipAddressChange;
	}
	public void setIpAddressChange(int ipAddressChange)
	{
		this.ipAddressChange = ipAddressChange;
	}
	public int getNameChange()
	{
		return nameChange;
	}
	public void setNameChange(int nameChange)
	{
		this.nameChange = nameChange;
	}
	public OBDtoAdcNodeAlteon getNodeOld()
	{
		return nodeOld;
	}
	public void setNodeOld(OBDtoAdcNodeAlteon nodeOld)
	{
		this.nodeOld = nodeOld;
	}
	public OBDtoAdcNodeAlteon getNodeNew()
	{
		return nodeNew;
	}
	public void setNodeNew(OBDtoAdcNodeAlteon nodeNew)
	{
		this.nodeNew = nodeNew;
	}
}