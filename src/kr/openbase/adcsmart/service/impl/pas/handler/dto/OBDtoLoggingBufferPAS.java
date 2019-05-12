package kr.openbase.adcsmart.service.impl.pas.handler.dto;

public class OBDtoLoggingBufferPAS
{
	private String	date	="";
	private	String	content	="";
	@Override
	public String toString()
	{
		return "OBDtoLoggingBufferPAS [date=" + date + ", content=" + content
				+ "]";
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
