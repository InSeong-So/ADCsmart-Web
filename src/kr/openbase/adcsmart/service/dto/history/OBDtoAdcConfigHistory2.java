package kr.openbase.adcsmart.service.dto.history;

import java.util.Date;

public class OBDtoAdcConfigHistory2
{
	private Date occurTime;
	private String accountName;
	private String summary;
	private long logSeq;
	private String virtualSvrIndex;

	@Override
	public String toString()
	{
		return "OBDtoAdcConfigHistory2 [occurTime=" + occurTime + ", accountName=" + accountName + ", summary=" + summary + ", logSeq=" + logSeq + ", virtualSvrIndex=" + virtualSvrIndex + "]";
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public String getAccountName()
	{
		return accountName;
	}
	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
	public long getLogSeq()
	{
		return logSeq;
	}
	public void setLogSeq(long logSeq)
	{
		this.logSeq = logSeq;
	}
	public String getVirtualSvrIndex()
	{
		return virtualSvrIndex;
	}
	public void setVirtualSvrIndex(String virtualSvrIndex)
	{
		this.virtualSvrIndex = virtualSvrIndex;
	}
	
}
