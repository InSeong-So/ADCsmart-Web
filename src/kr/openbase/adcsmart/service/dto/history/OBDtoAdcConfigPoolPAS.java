package kr.openbase.adcsmart.service.dto.history;

import java.io.Serializable;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolPAS;

public class OBDtoAdcConfigPoolPAS implements Serializable
{
	private static final long serialVersionUID = 10L;
	private int change;
	private int lbMethodChange;
	private int healthCheckChange;
	private int memberChange;
	
	private OBDtoAdcPoolPAS poolOld;
	private OBDtoAdcPoolPAS poolNew;
	private ArrayList<OBDtoAdcConfigPoolMemberPAS> memberConfigList;

	@Override
	public String toString()
	{
		return "OBDtoAdcConfigPoolPAS [change=" + change + ", lbMethodChange="
				+ lbMethodChange + ", healthCheckChange=" + healthCheckChange
				+ ", memberChange=" + memberChange + ", poolOld=" + poolOld
				+ ", poolNew=" + poolNew + ", memberConfigList="
				+ memberConfigList + "]";
	}

	public int getChange()
	{
		return change;
	}
	public void setChange(int change)
	{
		this.change = change;
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
	public int getMemberChange()
	{
		return memberChange;
	}
	public void setMemberChange(int memberChange)
	{
		this.memberChange = memberChange;
	}
	public OBDtoAdcPoolPAS getPoolOld()
	{
		return poolOld;
	}
	public void setPoolOld(OBDtoAdcPoolPAS poolOld)
	{
		this.poolOld = poolOld;
	}
	public OBDtoAdcPoolPAS getPoolNew()
	{
		return poolNew;
	}
	public void setPoolNew(OBDtoAdcPoolPAS poolNew)
	{
		this.poolNew = poolNew;
	}
	public ArrayList<OBDtoAdcConfigPoolMemberPAS> getMemberConfigList()
	{
		return memberConfigList;
	}
	public void setMemberConfigList(
			ArrayList<OBDtoAdcConfigPoolMemberPAS> memberConfigList)
	{
		this.memberConfigList = memberConfigList;
	}
}