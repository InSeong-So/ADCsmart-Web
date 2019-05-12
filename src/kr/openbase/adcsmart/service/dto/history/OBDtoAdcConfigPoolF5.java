package kr.openbase.adcsmart.service.dto.history;

import java.io.Serializable;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolF5;

public class OBDtoAdcConfigPoolF5 implements Serializable
{
	private static final long serialVersionUID = 10L;
	private int change;
	private int memberChange;
	private int lbMethodChange;
	private int healthCheckChange;
	
	private OBDtoAdcPoolF5 poolOld;
	private OBDtoAdcPoolF5 poolNew;
	
	private ArrayList<OBDtoAdcConfigPoolMemberF5> memberConfigList;

	@Override
	public String toString()
	{
		return "OBDtoAdcConfigPoolF5 [change=" + change + ", memberChange="
				+ memberChange + ", lbMethodChange=" + lbMethodChange
				+ ", healthCheckChange=" + healthCheckChange + ", poolOld="
				+ poolOld + ", poolNew=" + poolNew + ", memberConfigList="
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
	public OBDtoAdcPoolF5 getPoolOld()
	{
		return poolOld;
	}
	public void setPoolOld(OBDtoAdcPoolF5 poolOld)
	{
		this.poolOld = poolOld;
	}
	public OBDtoAdcPoolF5 getPoolNew()
	{
		return poolNew;
	}
	public void setPoolNew(OBDtoAdcPoolF5 poolNew)
	{
		this.poolNew = poolNew;
	}
	public ArrayList<OBDtoAdcConfigPoolMemberF5> getMemberConfigList()
	{
		return memberConfigList;
	}
	public void setMemberConfigList(
			ArrayList<OBDtoAdcConfigPoolMemberF5> memberConfigList)
	{
		this.memberConfigList = memberConfigList;
	}
}