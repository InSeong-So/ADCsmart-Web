/*
 * syslog 필터링을 위한 하나의 정책.
 */
package kr.openbase.adcsmart.service.dto;

public class OBDtoSyslogFilter
{
	private String hostName;
	private int level;
	private String filter;
	private int action;
	
	public void setFilter(String filter)
	{
		this.filter = filter;
	}
	public String getFilter()
	{
		return this.filter;
	}
	
	public void setAction(int action)
	{
		this.action = action;
	}
	public int getAction()
	{
		return this.action;
	}
	
	public void setHostName(String hostName)
	{
		this.hostName = hostName;
	}
	public String getHostName()
	{
		return this.hostName;
	}
	
	public void setLevel(int level)
	{
		this.level = level;
	}
	public int getLevel()
	{
		return this.level;
	}
}
