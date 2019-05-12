/*
 * ADC 장비의 시스템 자원 상태 로그이다.
 */
package kr.openbase.adcsmart.service.dto;

public class OBDtoAdcSystemResc
{
	private int adcIndex;// ADC 장비의 Index
	private int cpuUsage;
	private int memUsage;
	private int diskUsage;
	private int activeConnections;
	private int maxConnections;
	private int clientBps;
	private int clientPps;
	private int serverBps;
	private int serverPps;
	
	public void setAdcIndex(int adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public int getAdcIndex()
	{
		return this.adcIndex;
	}

	public void setServerPps(int serverPps)
	{
		this.serverPps = serverPps;
	}
	public int getServerPps()
	{
		return this.serverPps;
	}
	
	public void setServerBps(int serverBps)
	{
		this.serverBps = serverBps;
	}
	public int getServerBps()
	{
		return this.serverBps;
	}
	
	public void setClientPps(int clientPps)
	{
		this.clientPps = clientPps;
	}
	public int getClientPps()
	{
		return this.clientPps;
	}
	
	public void setClientBps(int clientBps)
	{
		this.clientBps = clientBps;
	}
	public int getClientBps()
	{
		return this.clientBps;
	}
	
	public void setMaxConnections(int maxConnections)
	{
		this.maxConnections = maxConnections;
	}
	public int getMaxConnections()
	{
		return this.maxConnections;
	}
	
	public void setActiveConnections(int activeConnections)
	{
		this.activeConnections = activeConnections;
	}
	public int getActiveConnections()
	{
		return this.activeConnections;
	}
	
	public void setCpuUsage(int cpuUsage)
	{
		this.cpuUsage = cpuUsage;
	}
	public int getCpuUsage()
	{
		return this.cpuUsage;
	}

	public void setMemUsage(int memUsage)
	{
		this.memUsage = memUsage;
	}
	public int getMemUsage()
	{
		return this.memUsage;
	}
	
	public void setDiskUsage(int diskUsage)
	{
		this.diskUsage = diskUsage;
	}
	public int getDiskUsage()
	{
		return this.diskUsage;
	}
}
