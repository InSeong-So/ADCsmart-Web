package kr.openbase.adcsmart.service.snmp.alteon.dto;

import java.util.ArrayList;

public class DtoSnmpPoolAlteon
{
	private String poolID;
	private String poolName;
	private Integer poolLbMethod;
	private String poolHealthCheck;
	private Integer bakType;
	private String bakID;
	private ArrayList<String> memberIDs;
	private ArrayList<Integer> memberState;

	
	@Override
	public String toString()
	{
		return "DtoSnmpPoolAlteon [poolID=" + poolID + ", poolName=" + poolName 
				+ ", poolLbMethod=" + poolLbMethod + ", poolHealthCheck=" + poolHealthCheck 
				+ ", bakType=" + bakType + ", bakID=" + bakID + ", memberIDs=" + memberIDs 
				+ ", memberState=" + memberState + "]";
	}
	public String getPoolID()
	{
		return poolID;
	}
	public ArrayList<Integer> getMemberState()
	{
		return memberState;
	}
	public void setMemberState(ArrayList<Integer> memberState)
	{
		this.memberState = memberState;
	}
	public String getPoolHealthCheck()
	{
		return poolHealthCheck;
	}
	public void setPoolHealthCheck(String poolHealthCheck)
	{
		this.poolHealthCheck = poolHealthCheck;
	}
	public void setPoolID(String poolID)
	{
		this.poolID = poolID;
	}
	public String getPoolName()
	{
		return poolName;
	}
	public void setPoolName(String poolName)
	{
		this.poolName = poolName;
	}
	public Integer getPoolLbMethod()
	{
		return poolLbMethod;
	}
	public void setPoolLbMethod(Integer poolLbMethod)
	{
		this.poolLbMethod = poolLbMethod;
	}
	public ArrayList<String> getMemberIDs()
	{
		return memberIDs;
	}
	public void setMemberIDs(ArrayList<String> memberIDs)
	{
		this.memberIDs = memberIDs;
	}
	public Integer getBakType()
	{
		return bakType;
	}
	public void setBakType(Integer bakType)
	{
		this.bakType = bakType;
	}
	public String getBakID()
	{
		return bakID;
	}
	public void setBakID(String bakID)
	{
		this.bakID = bakID;
	}
}
