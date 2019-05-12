package kr.openbase.adcsmart.service.impl.fault.dto;

public class OBDtoBpsPpsEpsDps
{
	private long bpsIn=-1;
	private long ppsIn=-1;
	private long bpsOut=-1;
	private long ppsOut=-1;
	private long epsIn=-1;//errors per second
	private long epsOut=-1;
	private long dpsIn=-1;//drops per second
	private long dpsOut=-1;
	private long filterCount=-1;
	@Override
    public String toString()
    {
        return "OBDtoBpsPpsEpsDps [bpsIn=" + bpsIn + ", ppsIn=" + ppsIn
                + ", bpsOut=" + bpsOut + ", ppsOut=" + ppsOut + ", epsIn="
                + epsIn + ", epsOut=" + epsOut + ", dpsIn=" + dpsIn
                + ", dpsOut=" + dpsOut + ", filterCount=" + filterCount + "]";
    }
	public long getBpsIn()
	{
		return bpsIn;
	}
	public void setBpsIn(long bpsIn)
	{
		this.bpsIn = bpsIn;
	}
	public long getPpsIn()
	{
		return ppsIn;
	}
	public void setPpsIn(long ppsIn)
	{
		this.ppsIn = ppsIn;
	}
	public long getBpsOut()
	{
		return bpsOut;
	}
	public void setBpsOut(long bpsOut)
	{
		this.bpsOut = bpsOut;
	}
	public long getPpsOut()
	{
		return ppsOut;
	}
	public void setPpsOut(long ppsOut)
	{
		this.ppsOut = ppsOut;
	}
	public long getEpsIn()
	{
		return epsIn;
	}
	public void setEpsIn(long epsIn)
	{
		this.epsIn = epsIn;
	}
	public long getEpsOut()
	{
		return epsOut;
	}
	public void setEpsOut(long epsOut)
	{
		this.epsOut = epsOut;
	}
	public long getDpsIn()
	{
		return dpsIn;
	}
	public void setDpsIn(long dpsIn)
	{
		this.dpsIn = dpsIn;
	}
	public long getDpsOut()
	{
		return dpsOut;
	}
	public void setDpsOut(long dpsOut)
	{
		this.dpsOut = dpsOut;
	}
    public long getFilterCount()
    {
        return filterCount;
    }
    public void setFilterCount(long filterCount)
    {
        this.filterCount = filterCount;
    }
}
