/*
 * a virtual server 설정 이력 데이터.
 */
package kr.openbase.adcsmart.service.impl.dto;

import java.sql.*;

public class OBDtoLogVServerConfig
{
	private String accountId;
	private int adcIndex;
	private int type;//0: old, 1: new
	private Timestamp occurTime;
	private String name;
	private String ipAddress;
	private int alteonId;
	private int servicePort;
	private String poolName;
	private String poolMembers;
	private int lbMethod;//1:RoundRobin, 1: LeastConnections, 2: Hash, 3: not defined
	private String persistence;
	
	public String getAccountId()
	{
		return this.accountId;
	}
	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
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
	
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getIpAddress()
	{
		return this.ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	
	public int getAlteonId()
	{
		return this.alteonId;
	}
	public void setAlteonId(int alteonId)
	{
		this.alteonId = alteonId;
	}
	
	public int getServicePort()
	{
		return this.servicePort;
	}
	public void setServicePort(int servicePort)
	{
		this.servicePort = servicePort;
	}
	
	public String getPoolName()
	{
		return this.poolName;
	}
	public void setPoolName(String poolName)
	{
		this.poolName = poolName;
	}
	
	public String getPoolMembers()
	{
		return this.poolMembers;
	}
	public void setPoolMembers(String poolMembers)
	{
		this.poolMembers = poolMembers;
	}
	
	public int getLbMethod()
	{
		return this.lbMethod;
	}
	public void setLbMethod(int lbMethod)
	{
		this.lbMethod = lbMethod;
	}
	
	public String getPersistence()
	{
		return this.persistence;
	}
	public void setPersistence(String persistence)
	{
		this.persistence = persistence;
	}
}
