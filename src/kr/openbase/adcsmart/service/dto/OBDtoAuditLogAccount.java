package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoAuditLogAccount
{
	private Date		occurTime;
	private Integer 	accountIndex;
	private String 		accountID;
	private String 		accountName;
	private String 		clientIPAddress;
	private String 		contents;
	
	@Override
	public String toString()
	{
		return "OBDtoAuditLogAccount [occurTime=" + occurTime
				+ ", accountIndex=" + accountIndex + ", accountID=" + accountID
				+ ", accountName=" + accountName + ", clientIPAddress="
				+ clientIPAddress + ", contents=" + contents + "]";
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
	public String getAccountID()
	{
		return accountID;
	}
	public void setAccountID(String accountID)
	{
		this.accountID = accountID;
	}
	public String getAccountName()
	{
		return accountName;
	}
	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}
	public String getClientIPAddress()
	{
		return clientIPAddress;
	}
	public void setClientIPAddress(String clientIPAddress)
	{
		this.clientIPAddress = clientIPAddress;
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
