package kr.openbase.adcsmart.service.vdirect.dto;

import java.util.HashMap;

public class RunAlteonCreateTemplateDto
{
	private HashMap<String, Object> configuration;
	private String name;
	private String type;

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public HashMap<String, Object> getConfiguration()
	{
		return configuration;
	}

	public void setConfiguration(HashMap<String, Object> configuration)
	{
		this.configuration = configuration;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
