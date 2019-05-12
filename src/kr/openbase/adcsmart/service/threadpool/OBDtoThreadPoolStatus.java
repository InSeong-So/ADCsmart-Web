package kr.openbase.adcsmart.service.threadpool;

public class OBDtoThreadPoolStatus
{
	private int 	queueCount = 0;// 큐에 남아 있는 작업의 개수.
	private int 	minThreadCount = 0;// 최소한 생성되어 있어야 할 쓰레드의 개수
	private int 	maxThreadCount = 0;// 최대로 생성할 수 있는 쓰레드의 개수
	private int 	createdThreadCount = 0;// 현재 생성되어 있는 쓰레드의 개수
	private int 	workThreadCount = 0;// 현재 실제 작업을 수행하고 있는 쓰레드의 개수
	private long	totalWorkStart = 0; // 전체 작업 개수. 시작 개수.
	private long	totalWorkClose = 0; // 전체 작업 개수. 종료 개수.
	private long    totalWorkFail = 0;// 전체 작업 개수. 실패 개수.
	
	@Override
	public String toString()
	{
		return String.format("OBDtoThreadPoolStatus [queueCount=%s, minThreadCount=%s, maxThreadCount=%s, createdThreadCount=%s, workThreadCount=%s, totalWorkStart=%s, totalWorkClose=%s, totalWorkFail=%s]", queueCount, minThreadCount, maxThreadCount, createdThreadCount, workThreadCount, totalWorkStart, totalWorkClose, totalWorkFail);
	}
	
	public String toStringV2()
	{
		return String.format("queue=%s, min=%s, max=%s, created=%s, work=%s, totalStart=%s, totalClose=%s, totalFail=%s]", queueCount, minThreadCount, maxThreadCount, createdThreadCount, workThreadCount, totalWorkStart, totalWorkClose, totalWorkFail);
	}
	
	public long getTotalWorkFail()
	{
		return totalWorkFail;
	}
	public void setTotalWorkFail(long totalWorkFail)
	{
		this.totalWorkFail = totalWorkFail;
	}
	public int getQueueCount()
	{
		return queueCount;
	}
	public void setQueueCount(int queueCount)
	{
		this.queueCount = queueCount;
	}
	public int getMinThreadCount()
	{
		return minThreadCount;
	}
	public void setMinThreadCount(int minThreadCount)
	{
		this.minThreadCount = minThreadCount;
	}
	public int getMaxThreadCount()
	{
		return maxThreadCount;
	}
	public void setMaxThreadCount(int maxThreadCount)
	{
		this.maxThreadCount = maxThreadCount;
	}
	public int getCreatedThreadCount()
	{
		return createdThreadCount;
	}
	public void setCreatedThreadCount(int createdThreadCount)
	{
		this.createdThreadCount = createdThreadCount;
	}
	public int getWorkThreadCount()
	{
		return workThreadCount;
	}
	public void setWorkThreadCount(int workThreadCount)
	{
		this.workThreadCount = workThreadCount;
	}
	public long getTotalWorkStart()
	{
		return totalWorkStart;
	}
	public void setTotalWorkStart(long totalWorkStart)
	{
		this.totalWorkStart = totalWorkStart;
	}
	public long getTotalWorkClose()
	{
		return totalWorkClose;
	}
	public void setTotalWorkClose(long totalWorkClose)
	{
		this.totalWorkClose = totalWorkClose;
	}
}
