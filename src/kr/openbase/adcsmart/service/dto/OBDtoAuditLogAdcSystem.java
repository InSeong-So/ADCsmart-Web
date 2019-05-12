package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoAuditLogAdcSystem
{
	private Integer		log_seq;
	private Date		occurTime;
	private Integer 	adcIndex;
	private String 		adcName;
	private String 		adcIPAddress;
	private String		type;//로그 종류.
	private String		level;
	private String 		contents;
	private String		contents_extra;
	
	public Integer getLog_seq()
	{
		return log_seq;
	}
	@Override
	public String toString()
	{
		return "OBDtoAuditLogAdcSystem [log_seq=" + log_seq + ", occurTime="
				+ occurTime + ", adcIndex=" + adcIndex + ", adcName=" + adcName
				+ ", adcIPAddress=" + adcIPAddress + ", type=" + type
				+ ", level=" + level + ", contents=" + contents
				+ ", contents_extra=" + contents_extra + "]";
	}
	public void setLog_seq(Integer log_seq)
	{
		this.log_seq = log_seq;
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public String getAdcIPAddress()
	{
		return adcIPAddress;
	}
	public void setAdcIPAddress(String adcIPAddress)
	{
		this.adcIPAddress = adcIPAddress;
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

	public String getContents_extra()
	{
		return contents_extra;
	}

	public void setContents_extra(String contents_extra)
	{
		this.contents_extra = contents_extra;
	}
}