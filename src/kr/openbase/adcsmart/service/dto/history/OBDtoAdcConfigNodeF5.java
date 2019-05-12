package kr.openbase.adcsmart.service.dto.history;

import java.io.Serializable;

import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeF5;

public class OBDtoAdcConfigNodeF5 implements Serializable
{
	private static final long serialVersionUID = 10L;
	private int change;
	private int ipAddressChange;
	private int nameChange;

	private OBDtoAdcNodeF5 nodeOld;
	private OBDtoAdcNodeF5 nodeNew;
	
	public int getChange()
	{
		return change;
	}
	public void setChange(int change)
	{
		this.change = change;
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
	public OBDtoAdcNodeF5 getNodeOld()
	{
		return nodeOld;
	}
	public void setNodeOld(OBDtoAdcNodeF5 nodeOld)
	{
		this.nodeOld = nodeOld;
	}
	public OBDtoAdcNodeF5 getNodeNew()
	{
		return nodeNew;
	}
	public void setNodeNew(OBDtoAdcNodeF5 nodeNew)
	{
		this.nodeNew = nodeNew;
	}
}