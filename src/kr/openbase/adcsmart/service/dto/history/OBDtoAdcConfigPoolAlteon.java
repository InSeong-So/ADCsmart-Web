package kr.openbase.adcsmart.service.dto.history;

import java.io.Serializable;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberAlteon;

public class OBDtoAdcConfigPoolAlteon implements Serializable
{
	private static final long serialVersionUID = 10L;
	private int change;
	private int memberChange;
	private int lbMethodChange;
	private int healthCheckChange;
	private int nameChange;
	
	private OBDtoAdcPoolAlteon poolOld;
	private OBDtoAdcPoolAlteon poolNew;

	private ArrayList<OBDtoAdcPoolMemberAlteon> poolMemberAddList; //TODO : 데모후 지우기
	private ArrayList<OBDtoAdcPoolMemberAlteon> poolMemberDelList; //TODO : 데모후 지우기
	private ArrayList<OBDtoAdcConfigPoolMemberAlteon> memberConfigList;
	
	@Override
	public String toString()
	{
		return "OBDtoAdcConfigPoolAlteon [change=" + change + ", memberChange="
				+ memberChange + ", lbMethodChange=" + lbMethodChange
				+ ", healthCheckChange=" + healthCheckChange + ", nameChange="
				+ nameChange + ", poolOld=" + poolOld + ", poolNew=" + poolNew
				+ ", poolMemberAddList=" + poolMemberAddList
				+ ", poolMemberDelList=" + poolMemberDelList
				+ ", memberConfigList=" + memberConfigList + "]";
	}
	public int getChange()
	{
		return change;
	}
	public void setChange(int change)
	{
		this.change = change;
	}
	public int getNameChange()
	{
		return nameChange;
	}
	public void setNameChange(int nameChange)
	{
		this.nameChange = nameChange;
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
	public ArrayList<OBDtoAdcPoolMemberAlteon> getPoolMemberAddList()
	{
		return poolMemberAddList;
	}
	public void setPoolMemberAddList(
			ArrayList<OBDtoAdcPoolMemberAlteon> poolMemberAddList)
	{
		this.poolMemberAddList = poolMemberAddList;
	}
	public ArrayList<OBDtoAdcPoolMemberAlteon> getPoolMemberDelList()
	{
		return poolMemberDelList;
	}
	public void setPoolMemberDelList(
			ArrayList<OBDtoAdcPoolMemberAlteon> poolMemberDelList)
	{
		this.poolMemberDelList = poolMemberDelList;
	}
	public ArrayList<OBDtoAdcConfigPoolMemberAlteon> getMemberConfigList()
	{
		return memberConfigList;
	}
	public void setMemberConfigList(
			ArrayList<OBDtoAdcConfigPoolMemberAlteon> memberConfigList)
	{
		this.memberConfigList = memberConfigList;
	}
	public OBDtoAdcPoolAlteon getPoolOld()
	{
		return poolOld;
	}
	public void setPoolOld(OBDtoAdcPoolAlteon poolOld)
	{
		this.poolOld = poolOld;
	}
	public OBDtoAdcPoolAlteon getPoolNew()
	{
		return poolNew;
	}
	public void setPoolNew(OBDtoAdcPoolAlteon poolNew)
	{
		this.poolNew = poolNew;
	}
}