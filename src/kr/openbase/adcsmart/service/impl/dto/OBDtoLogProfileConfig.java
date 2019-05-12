/*
 * profile 설정 이력 데이터
 */
package kr.openbase.adcsmart.service.impl.dto;

import java.sql.*;

public class OBDtoLogProfileConfig
{
	private String userName;
	private int adcIndex;
	private int type;
	private Timestamp occurTime;
	private String profileName;
	private String persistenceType;
	private String parentProfile;
	private int matchAcrossServiceYN;
	private int timeout;

	public String getUserName()
	{
		return this.userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	public int getAdcIndex()
	{
		return this.adcIndex;
	}
	public void setAdcIndex(int adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	
	public int getType()
	{
		return this.type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	
	public Timestamp getOccurTime()
	{
		return this.occurTime;
	}
	public void setOccurTime(Timestamp occurTime)
	{
		this.occurTime = occurTime;
	}
	
	public String getProfileName()
	{
		return this.profileName;
	}
	public void setProfileName(String profileName)
	{
		this.profileName = profileName;
	}
	
	public String getPersistenceType()
	{
		return this.persistenceType;
	}
	public void setPersistenceType(String persistenceType)
	{
		this.persistenceType = persistenceType;
	}
	
	public String getParentProfile()
	{
		return this.parentProfile;
	}
	public void setParentProfile(String parentProfile)
	{
		this.parentProfile = parentProfile;
	}
	
	public int getMatchAcrossServiceYN()
	{
		return this.matchAcrossServiceYN;
	}
	public void setMatchAcrossServiceYN(int matchAcrossServiceYN)
	{
		this.matchAcrossServiceYN = matchAcrossServiceYN;
	}
	
	public int getTimeout()
	{
		return this.timeout;
	}
	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}
}