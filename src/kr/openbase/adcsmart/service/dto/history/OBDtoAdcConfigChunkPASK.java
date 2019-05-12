package kr.openbase.adcsmart.service.dto.history;

import java.io.Serializable;

public class OBDtoAdcConfigChunkPASK implements Serializable
{
	private static final long serialVersionUID = 10L;
	private int userType;
	private int changeType;
	private int changeObject;
	private int accountIndex;

	private OBDtoAdcConfigVServerPASK vsConfig;

	@Override
	public String toString()
	{
		return "OBDtoAdcConfigChunkPASK [userType=" + userType
				+ ", changeType=" + changeType + ", changeObject="
				+ changeObject + ", accountIndex=" + accountIndex
				+ ", vsConfig=" + vsConfig + "]";
	}

	public int getUserType()
	{
		return userType;
	}
	public void setUserType(int userType)
	{
		this.userType = userType;
	}
	public int getChangeType()
	{
		return changeType;
	}
	public void setChangeType(int changeType)
	{
		this.changeType = changeType;
	}
	public int getChangeObject()
	{
		return changeObject;
	}
	public void setChangeObject(int changeObject)
	{
		this.changeObject = changeObject;
	}
	public int getAccountIndex()
	{
		return accountIndex;
	}
	public void setAccountIndex(int accountIndex)
	{
		this.accountIndex = accountIndex;
	}
	public OBDtoAdcConfigVServerPASK getVsConfig()
	{
		return vsConfig;
	}
	public void setVsConfig(OBDtoAdcConfigVServerPASK vsConfig)
	{
		this.vsConfig = vsConfig;
	}
}