package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoVSConnectionCountInfo
{
	private Long currConn=0L;
	private Long maxConn=0L;
	private Long totConn=0L;
	@Override
	public String toString()
	{
		return String.format("OBDtoVSConnectionCountInfo [currConn=%s, maxConn=%s, totConn=%s]", currConn, maxConn, totConn);
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
	public Long getTotConn()
	{
		return totConn;
	}
	public void setTotConn(Long totConn)
	{
		this.totConn = totConn;
	}
}
