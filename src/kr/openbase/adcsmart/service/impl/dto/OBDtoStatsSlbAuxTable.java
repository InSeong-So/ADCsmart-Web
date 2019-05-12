package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoStatsSlbAuxTable
{
	private Integer index;
	private Long	currConn;
	private Long	maxConn;
	private Long 	allocFails;
	
	@Override
	public String toString()
	{
		return "OBDtoAuxTable [index=" + index + ", currConn=" + currConn + ", maxConn=" + maxConn + ", allocFails=" + allocFails + "]";
	}
	public Integer getIndex()
	{
		return index;
	}
	public void setIndex(Integer index)
	{
		this.index = index;
	}
	public Long getCurrConn()
	{
		return currConn;
	}
	public void setCurrConn(Long currConn)
	{
		this.currConn = currConn;
	}
	public Long getMaxConn()
	{
		return maxConn;
	}
	public void setMaxConn(Long maxConn)
	{
		this.maxConn = maxConn;
	}
	public Long getAllocFails()
	{
		return allocFails;
	}
	public void setAllocFails(Long allocFails)
	{
		this.allocFails = allocFails;
	}
}
