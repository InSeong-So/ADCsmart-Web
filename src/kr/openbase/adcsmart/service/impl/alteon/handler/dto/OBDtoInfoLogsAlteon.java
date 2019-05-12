package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

import java.sql.Timestamp;

public class OBDtoInfoLogsAlteon
{
	private Timestamp	dateTime;
	private String	level;
	private	String	content;
	@Override
	public String toString()
	{
		return "OBDtoInfoLogsAlteon [dateTime=" + dateTime + ", level=" + level + ", content=" + content + "]";
	}
	public Timestamp getDateTime()
	{
		return dateTime;
	}
	public void setDateTime(Timestamp dateTime)
	{
		this.dateTime = dateTime;
	}
	public String getLevel()
	{
		return level;
	}
	public void setLevel(String level)
	{
		this.level = level;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
}
