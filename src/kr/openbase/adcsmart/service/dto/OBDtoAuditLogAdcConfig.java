package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoAuditLogAdcConfig
{
	private Date		occurTime;
	private Integer 	accountIndex;
	private Integer 	adcIndex;
	private String		accountID;
	private String 		adcName;
	private String 		clientIPAddress;
	private	String 		type;//1. SLB, 2: CONFIG, 3: LOGIN/OUT, 4: SYSTEM, 999: OTHERS 
	private String		level;
	private String 		contents;
	@Override
	public String toString()
	{
		return "OBDtoAuditLogAdcConfig [occurTime=" + occurTime
				+ ", accountIndex=" + accountIndex + ", adcIndex=" + adcIndex
				+ ", accountID=" + accountID + ", adcName=" + adcName
				+ ", clientIPAddress=" + clientIPAddress + ", type=" + type
				+ ", level=" + level + ", contents=" + contents + "]";
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Integer getAccountIndex()
	{
		return accountIndex;
	}
	public void setAccountIndex(Integer accountIndex)
	{
		this.accountIndex = accountIndex;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getAccountID()
	{
		return accountID;
	}
	public void setAccountID(String accountID)
	{
		this.accountID = accountID;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public String getClientIPAddress()
	{
		return clientIPAddress;
	}
	public void setClientIPAddress(String clientIPAddress)
	{
		this.clientIPAddress = clientIPAddress;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getLevel()
	{
		return level;
	}
	public void setLevel(String level)
	{
		this.level = level;
	}
	public String getContents()
	{
		return contents;
	}
	public void setContents(String contents)
	{
		this.contents = contents;
	}
}
