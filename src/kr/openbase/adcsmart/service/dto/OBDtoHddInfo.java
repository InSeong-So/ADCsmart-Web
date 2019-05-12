package kr.openbase.adcsmart.service.dto;

public class OBDtoHddInfo
{
	private Long hddTotal=-1L;// kbyte 단위.
	private Long hddUsed=-1L;
	private Long hddFree=-1L;
	private Long hddUsage=-1L;
	private Long hddTotal30daysBefore = -1L;
	private Long hddUsed30daysBefore = -1L;	

    @Override
    public String toString()
    {
        return "OBDtoHddInfo [hddTotal=" + hddTotal + ", hddUsed=" + hddUsed + ", hddFree=" + hddFree + ", hddUsage=" + hddUsage + ", hddTotal30daysBefore=" + hddTotal30daysBefore + ", hddUsed30daysBefore=" + hddUsed30daysBefore + "]";
    }
    public Long getHddTotal()
	{
		return hddTotal;
	}
	public void setHddTotal(Long hddTotal)
	{
		this.hddTotal = hddTotal;
	}
	public Long getHddUsed()
	{
		return hddUsed;
	}
	public void setHddUsed(Long hddUsed)
	{
		this.hddUsed = hddUsed;
	}
	public Long getHddFree()
	{
		return hddFree;
	}
	public void setHddFree(Long hddFree)
	{
		this.hddFree = hddFree;
	}
	public Long getHddUsage()
	{
		return hddUsage;
	}
	public void setHddUsage(Long hddUsage)
	{
		this.hddUsage = hddUsage;
	}
    public Long getHddTotal30daysBefore()
    {
        return hddTotal30daysBefore;
    }
    public void setHddTotal30daysBefore(Long hddTotal30daysBefore)
    {
        this.hddTotal30daysBefore = hddTotal30daysBefore;
    }
    public Long getHddUsed30daysBefore()
    {
        return hddUsed30daysBefore;
    }
    public void setHddUsed30daysBefore(Long hddUsed30daysBefore)
    {
        this.hddUsed30daysBefore = hddUsed30daysBefore;
    }
}