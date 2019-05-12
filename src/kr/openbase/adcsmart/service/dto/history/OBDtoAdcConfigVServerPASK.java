package kr.openbase.adcsmart.service.dto.history;

import java.io.Serializable;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;

public class OBDtoAdcConfigVServerPASK implements Serializable
{
	private static final long serialVersionUID = 10L;
	private int change;
	private int IpChange;
	private int protocolChange;
	private int portChange;
	private int lbMethodChange;
	private int healthCheckChange;
	private int memberChange;
	private int stateChange;
	private int subinfoChange;

	private OBDtoAdcVServerPASK vsOld;
	private OBDtoAdcVServerPASK vsNew;

	private ArrayList<OBDtoAdcConfigPoolMemberPASK> memberConfigList;
	private ArrayList<OBDtoAdcConfigNodePASK> nodeConfigList;		//바뀐 node 정보 목록

	@Override
	public String toString()
	{
		return "OBDtoAdcConfigVServerPASK [change=" + change + ", IpChange="
				+ IpChange + ", protocolChange=" + protocolChange
				+ ", portChange=" + portChange + ", lbMethodChange="
				+ lbMethodChange + ", healthCheckChange=" + healthCheckChange
				+ ", memberChange=" + memberChange + ", stateChange="
				+ stateChange + ", subinfoChange=" + subinfoChange + ", vsOld="
				+ vsOld + ", vsNew=" + vsNew + ", memberConfigList="
				+ memberConfigList + ", nodeConfigList=" + nodeConfigList + "]";
	}
	public int getChange()
	{
		return change;
	}
	public void setChange(int change)
	{
		this.change = change;
	}
	public int getIpChange()
	{
		return IpChange;
	}
	public void setIpChange(int ipChange)
	{
		IpChange = ipChange;
	}
	public int getProtocolChange()
	{
		return protocolChange;
	}
	public void setProtocolChange(int protocolChange)
	{
		this.protocolChange = protocolChange;
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
	public OBDtoAdcVServerPASK getVsOld()
	{
		return vsOld;
	}
	public void setVsOld(OBDtoAdcVServerPASK vsOld)
	{
		this.vsOld = vsOld;
	}
	public OBDtoAdcVServerPASK getVsNew()
	{
		return vsNew;
	}
	public void setVsNew(OBDtoAdcVServerPASK vsNew)
	{
		this.vsNew = vsNew;
	}
	public int getSubinfoChange()
	{
		return subinfoChange;
	}
	public void setSubinfoChange(int subinfoChange)
	{
		this.subinfoChange = subinfoChange;
	}
	public int getMemberChange()
	{
		return memberChange;
	}
	public void setMemberChange(int memberChange)
	{
		this.memberChange = memberChange;
	}
	public int getLbMethodChange()
	{
		return lbMethodChange;
	}
	public void setLbMethodChange(int lbMethodChange)
	{
		this.lbMethodChange = lbMethodChange;
	}
	public int getHealthCheckChange()
	{
		return healthCheckChange;
	}
	public void setHealthCheckChange(int healthCheckChange)
	{
		this.healthCheckChange = healthCheckChange;
	}
	public ArrayList<OBDtoAdcConfigPoolMemberPASK> getMemberConfigList()
	{
		return memberConfigList;
	}
	public void setMemberConfigList(
			ArrayList<OBDtoAdcConfigPoolMemberPASK> memberConfigList)
	{
		this.memberConfigList = memberConfigList;
	}
	public ArrayList<OBDtoAdcConfigNodePASK> getNodeConfigList()
	{
		return nodeConfigList;
	}
	public void setNodeConfigList(ArrayList<OBDtoAdcConfigNodePASK> nodeConfigList)
	{
		this.nodeConfigList = nodeConfigList;
	}
}