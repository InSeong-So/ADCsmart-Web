package kr.openbase.adcsmart.service.dto;

public class OBDtoDatabase
{
	private long totalDiskSize	=0;
	private long generalUsed	=0;
	private long indexUsed		=0;
	private long logUsed		=0;
	private long totalUsedSized	=0;
	
	@Override
	public String toString()
	{
		return "OBDtoDatabase [totalDiskSize=" + totalDiskSize
				+ ", generalUsed=" + generalUsed + ", indexUsed=" + indexUsed
				+ ", logUsed=" + logUsed + ", totalUsedSized=" + totalUsedSized
				+ "]";
	}
	public long getTotalUsedSized()
	{
		return totalUsedSized;
	}
	public void setTotalUsedSized(long totalUsedSized)
	{
		this.totalUsedSized = totalUsedSized;
	}
	public long getTotalDiskSize()
	{
		return totalDiskSize;
	}
	public void setTotalDiskSize(long totalDiskSize)
	{
		this.totalDiskSize = totalDiskSize;
	}
	public long getGeneralUsed()
	{
		return generalUsed;
	}
	public void setGeneralUsed(long generalUsed)
	{
		this.generalUsed = generalUsed;
	}
	public long getIndexUsed()
	{
		return indexUsed;
	}
	public void setIndexUsed(long indexUsed)
	{
		this.indexUsed = indexUsed;
	}
	public long getLogUsed()
	{
		return logUsed;
	}
	public void setLogUsed(long logUsed)
	{
		this.logUsed = logUsed;
	}
}
