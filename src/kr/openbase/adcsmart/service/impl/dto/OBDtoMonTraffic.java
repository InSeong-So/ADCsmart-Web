package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoMonTraffic
{
	private long inBps;// Byte per second. 
	private long inPps;// packet per second. 
	private long outBps;// 
	private long outPps;
	private long currentConnections;
	private long maxConnections;
	private long totalConnections;
	
	public void setInBps(long inBps)
	{
		this.inBps = inBps;
	}
	public long getInBps()
	{
		return this.inBps;
	}

	public void setInPps(long inPps)
	{
		this.inPps = inPps;
	}
	public long getInPps()
	{
		return this.inPps;
	}

	public void setOutBps(long outBps)
	{
		this.outBps = outBps;
	}
	public long getOutBps()
	{
		return this.outBps;
	}

	public void setOutPps(long outPps)
	{
		this.outPps = outPps;
	}
	public long getOutPps()
	{
		return this.outPps;
	}

	public void setCurrentConnections(long currentConnections)
	{
		this.currentConnections = currentConnections;
	}
	public long getCurrentConnections()
	{
		return this.currentConnections;
	}

	public void setMaxConnections(long maxConnections)
	{
		this.maxConnections = maxConnections;
	}
	public long getMaxConnections()
	{
		return this.maxConnections;
	}

	public void setTotalConnections(long totalConnections)
	{
		this.totalConnections = totalConnections;
	}
	public long getTotalConnections()
	{
		return this.totalConnections;
	}
	
	@Override
	public String toString() 
	{
		return "OBDtoMonTraffic [inBps=" + this.inBps + ", inPps=" + this.inPps + ", outBps="
				+ this.outBps + ", outPps=" + this.outPps + ", currentConnections=" + this.currentConnections
				+ ", maxConnections=" + this.maxConnections + ", totalConnections=" + this.totalConnections + "]";
	}	
}
