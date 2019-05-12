package kr.openbase.adcsmart.service.vdirect.dto;

import java.util.HashMap;

public class CommandDto
{
	private String alteonName;
	private HashMap<String, String> command;
	
	public String getAlteonName()
	{
		return alteonName;
	}
	public void setAlteonName(String alteonName)
	{
		this.alteonName = alteonName;
	}
	public HashMap<String, String> getCommand()
	{
		return command;
	}
	public void setCommand(HashMap<String, String> command)
	{
		this.command = command;
	}
	
	
	
}
