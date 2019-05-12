package kr.openbase.adcsmart.service.impl.pask.handler.dto;

public class OBDtoLoggingBufferPASK
{
	private String	date	="";
	private	String	content	="";
	@Override
	public String toString()
	{
		return "OBDtoLoggingBufferPASK [date=" + date + ", content=" + content + "]";
	}
	public String getDate()
	{
		return date;
	}
	public void setDate(String date)
	{
		this.date = date;
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
