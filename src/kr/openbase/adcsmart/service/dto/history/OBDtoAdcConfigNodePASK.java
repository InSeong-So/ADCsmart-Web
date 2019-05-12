package kr.openbase.adcsmart.service.dto.history;

import java.io.Serializable;

import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcNodePASK;

public class OBDtoAdcConfigNodePASK implements Serializable
{
	private static final long serialVersionUID = 10L;
	private int change;
	private int ipAddressChange;
	private int nameChange;
	private int portChange;
	private int stateChange;

	private OBDtoAdcNodePASK nodeOld;
	private OBDtoAdcNodePASK nodeNew;
	
	@Override
	public String toString()
	{
		return "OBDtoAdcConfigNodePASK [change=" + change
				+ ", ipAddressChange=" + ipAddressChange + ", nameChange="
				+ nameChange + ", portChange=" + portChange + ", stateChange="
				+ stateChange + ", nodeOld=" + nodeOld + ", nodeNew=" + nodeNew
				+ "]";
	}
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
	public OBDtoAdcNodePASK getNodeOld()
	{
		return nodeOld;
	}
	public void setNodeOld(OBDtoAdcNodePASK nodeOld)
	{
		this.nodeOld = nodeOld;
	}
	public OBDtoAdcNodePASK getNodeNew()
	{
		return nodeNew;
	}
	public void setNodeNew(OBDtoAdcNodePASK nodeNew)
	{
		this.nodeNew = nodeNew;
	}
	public int getPortChange()
	{
		return portChange;
	}
	public void setPortChange(int portChange)
	{
		this.portChange = portChange;
	}
	public int getStateChange()
	{
		return stateChange;
	}
	public void setStateChange(int stateChange)
	{
		this.stateChange = stateChange;
	}
}