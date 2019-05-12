package kr.openbase.adcsmart.service.dto;

public class OBDtoVirtualServerSummary
{
	private Long vsTotal;
	private Long vsAvail;
	private Long vsUnavail;
	@Override
	public String toString()
	{
		return "OBDtoVirtualServerSummary [vsTotal=" + vsTotal
				+ ", vsAvail=" + vsAvail + ", vsUnavail=" + vsUnavail + "]";
	}
	public Long getVsTotal()
	{
		return vsTotal;
	}
	public void setVsTotal(Long vsTotal)
	{
		this.vsTotal = vsTotal;
	}
	public Long getVsAvail()
	{
		return vsAvail;
	}
	public void setVsAvail(Long vsAvail)
	{
		this.vsAvail = vsAvail;
	}
	public Long getVsUnavail()
	{
		return vsUnavail;
	}
	public void setVsUnavail(Long vsUnavail)
	{
		this.vsUnavail = vsUnavail;
	}
}
