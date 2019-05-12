package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

public class OBDtoAdcPoolAlteonChanged
{
	private String name;
	private boolean isNameChanged;
	private String alteonId;
	private Integer lbMethod;
	private boolean isLbMethodChanged;
	private Integer healthCheck;
	private String healthCheckV2;
	private boolean isHealthCheckChanged;
	private ArrayList<OBDtoAdcPoolMemberAlteonChanged> memberList;

	@Override
	public String toString()
	{
		return "OBDtoAdcPoolAlteonChanged [name=" + name + ", isNameChanged="
				+ isNameChanged + ", alteonId=" + alteonId + ", lbMethod="
				+ lbMethod + ", isLbMethodChanged=" + isLbMethodChanged
				+ ", healthCheck=" + healthCheck + ", healthCheckV2="
				+ healthCheckV2 + ", isHealthCheckChanged="
				+ isHealthCheckChanged + ", memberList=" + memberList + "]";
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public boolean isNameChanged()
	{
		return isNameChanged;
	}
	public void setNameChanged(boolean isNameChanged)
	{
		this.isNameChanged = isNameChanged;
	}
	public String getAlteonId()
	{
		return alteonId;
	}
	public void setAlteonId(String alteonId)
	{
		this.alteonId = alteonId;
	}
	public Integer getLbMethod()
	{
		return lbMethod;
	}
	public void setLbMethod(Integer lbMethod)
	{
		this.lbMethod = lbMethod;
	}
	public boolean isLbMethodChanged()
	{
		return isLbMethodChanged;
	}
	public void setLbMethodChanged(boolean isLbMethodChanged)
	{
		this.isLbMethodChanged = isLbMethodChanged;
	}
	public Integer getHealthCheck()
	{
		return healthCheck;
	}
	public void setHealthCheck(Integer healthCheck)
	{
		this.healthCheck = healthCheck;
	}
	public boolean isHealthCheckChanged()
	{
		return isHealthCheckChanged;
	}
	public void setHealthCheckChanged(boolean isHealthCheckChanged)
	{
		this.isHealthCheckChanged = isHealthCheckChanged;
	}
	public ArrayList<OBDtoAdcPoolMemberAlteonChanged> getMemberList()
	{
		return memberList;
	}
	public void setMemberList(ArrayList<OBDtoAdcPoolMemberAlteonChanged> memberList)
	{
		this.memberList = memberList;
	}
	public String getHealthCheckV2()
	{
		return healthCheckV2;
	}
	public void setHealthCheckV2(String healthCheckV2)
	{
		this.healthCheckV2 = healthCheckV2;
	}
}
