package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoAdcLogIgnorePattern
{
	private String index;
	private Date occurTime;
	private String pattern;
	private Integer accntIndex;
	private Integer accntID;
	private String comments;
	@Override
	public String toString()
	{
		return "OBDtoAdcLogIgnorePattern [index=" + index + ", occurTime="
				+ occurTime + ", pattern=" + pattern + ", accntIndex="
				+ accntIndex + ", accntID=" + accntID + ", comments="
				+ comments + "]";
	}
	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
	{
		this.index = index;
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public String getPattern()
	{
		return pattern;
	}
	public void setPattern(String pattern)
	{
		this.pattern = pattern;
	}
	public Integer getAccntIndex()
	{
		return accntIndex;
	}
	public void setAccntIndex(Integer accntIndex)
	{
		this.accntIndex = accntIndex;
	}
	public Integer getAccntID()
	{
		return accntID;
	}
	public void setAccntID(Integer accntID)
	{
		this.accntID = accntID;
	}
	public String getComments()
	{
		return comments;
	}
	public void setComments(String comments)
	{
		this.comments = comments;
	}
}
