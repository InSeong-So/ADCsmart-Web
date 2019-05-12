package kr.openbase.adcsmart.service.impl.dto;

import java.sql.Timestamp;

public class OBDtoMonTrafficAdc
{
	private Integer adcIndex;
	private Timestamp occurTime;
	private Long	connPerSecond;
	private Long    connCurrent;
	private Long    connMax;
	private Long    connTot;
	private Long    connSlbCurrent;
	private Long    pktsIn   =-1L;
	private Long    pktsOut  =-1L;
	private Long    bytesIn  =-1L;
	private Long    bytesOut =-1L;
	//Alteon v26 32비트 숫자 보정때문에 생긴 멤버들
	private Long    bytesInPerSec  =-1L;
	private Long    bytesOutPerSec =-1L;
	private Long    filterSum = -1L;

	@Override
    public String toString()
    {
        return "OBDtoMonTrafficAdc [adcIndex=" + adcIndex + ", occurTime="
                + occurTime + ", connPerSecond=" + connPerSecond
                + ", connCurrent=" + connCurrent + ", connMax=" + connMax
                + ", connTot=" + connTot + ", connSlbCurrent=" + connSlbCurrent
                + ", pktsIn=" + pktsIn + ", pktsOut=" + pktsOut + ", bytesIn="
                + bytesIn + ", bytesOut=" + bytesOut + ", bytesInPerSec="
                + bytesInPerSec + ", bytesOutPerSec=" + bytesOutPerSec
                + ", filterSum=" + filterSum + "]";
    }
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public Timestamp getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Timestamp occurTime)
	{
		this.occurTime = occurTime;
	}
	public Long getConnPerSecond()
	{
		return connPerSecond;
	}
	public void setConnPerSecond(Long connPerSecond)
	{
		this.connPerSecond = connPerSecond;
	}
	public Long getConnCurrent()
	{
		return connCurrent;
	}
	public void setConnCurrent(Long connCurrent)
	{
		this.connCurrent = connCurrent;
	}
	public Long getConnMax()
	{
		return connMax;
	}
	public void setConnMax(Long connMax)
	{
		this.connMax = connMax;
	}
	public Long getConnTot()
	{
		return connTot;
	}
	public void setConnTot(Long connTot)
	{
		this.connTot = connTot;
	}
	public Long getConnSlbCurrent()
	{
		return connSlbCurrent;
	}
	public void setConnSlbCurrent(Long connSlbCurrent)
	{
		this.connSlbCurrent = connSlbCurrent;
	}
	public Long getPktsIn()
	{
		return pktsIn;
	}
	public void setPktsIn(Long pktsIn)
	{
		this.pktsIn = pktsIn;
	}
	public Long getPktsOut()
	{
		return pktsOut;
	}
	public void setPktsOut(Long pktsOut)
	{
		this.pktsOut = pktsOut;
	}
	public Long getBytesIn()
	{
		return bytesIn;
	}
	public void setBytesIn(Long bytesIn)
	{
		this.bytesIn = bytesIn;
	}
	public Long getBytesOut()
	{
		return bytesOut;
	}
	public void setBytesOut(Long bytesOut)
	{
		this.bytesOut = bytesOut;
	}
	public Long getBytesInPerSec()
	{
		return bytesInPerSec;
	}
	public void setBytesInPerSec(Long bytesInPerSec)
	{
		this.bytesInPerSec = bytesInPerSec;
	}
	public Long getBytesOutPerSec()
	{
		return bytesOutPerSec;
	}
	public void setBytesOutPerSec(Long bytesOutPerSec)
	{
		this.bytesOutPerSec = bytesOutPerSec;
	}
    public Long getFilterSum()
    {
        return filterSum;
    }
    public void setFilterSum(Long filterSum)
    {
        this.filterSum = filterSum;
    }
}
