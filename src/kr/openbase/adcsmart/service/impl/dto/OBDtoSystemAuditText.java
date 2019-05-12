package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoSystemAuditText
{
	private int code;
	private int type;// 1. SLB 설정, 2: 시스템 설정, 3: 로그인/로그아웃, 4: 운영로그, 999: 기타 
	private String level;// 1: INFO, 2: WARNING, 3: RISK, 4: ERROR
	private String content;
	private String ext;
	
	public String getExtraMessage()
	{
		return this.ext;
	}
	public void setExtraMessage(String extraMessage)
	{
		this.ext = extraMessage;
	}	

	public int getCode()
	{
		return this.code;
	}
	public void setCode(int code)
	{
		this.code = code;
	}
	
	public int getType()
	{
		return this.type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	
	public String getLevel()
	{
		return this.level;
	}
	public void setLevel(String level)
	{
		this.level = level;
	}
	
	public String getContent()
	{
		return this.content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}	
}
