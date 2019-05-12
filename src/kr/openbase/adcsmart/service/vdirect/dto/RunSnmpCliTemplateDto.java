package kr.openbase.adcsmart.service.vdirect.dto;

import java.util.HashMap;

public class RunSnmpCliTemplateDto
{
	private String service;
	private String protocol;
	private String host;
	private String description;
	private HashMap<String, String> credentials;

	public String getService()
	{
		return service;
	}

	public void setService(String service)
	{
		this.service = service;
	}

	public String getProtocol()
	{
		return protocol;
	}

	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public HashMap<String, String> getCredentials()
	{
		return credentials;
	}

	public void setCredentials(HashMap<String, String> credentials)
	{
		this.credentials = credentials;
	}
}
