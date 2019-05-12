package kr.openbase.adcsmart.service.snmp.pask.dto;

public class DtoSnmpAdcRescPASK
{
	private Integer mpCpuUsage;
	private Integer mpMemUsage;
	private Long	mpMemTotal;//KB
	private Long   	mpMemUsed;
	private Long	mpMemFree;
	private Integer spCpuUsage;
	private Integer spMemUsage;
	private Long	spMemTotal;//KB
	private Long   	spMemUsed;
	private Long	spMemFree;
	@Override
	public String toString()
	{
		return "DtoSnmpAdcRescPASK [mpCpuUsage=" + mpCpuUsage + ", mpMemUsage=" + mpMemUsage + ", mpMemTotal=" + mpMemTotal + ", mpMemUsed=" + mpMemUsed + ", mpMemFree=" + mpMemFree + ", spCpuUsage=" + spCpuUsage + ", spMemUsage=" + spMemUsage + ", spMemTotal=" + spMemTotal + ", spMemUsed=" + spMemUsed + ", spMemFree=" + spMemFree + "]";
	}
	public Integer getMpCpuUsage()
	{
		return mpCpuUsage;
	}
	public void setMpCpuUsage(Integer mpCpuUsage)
	{
		this.mpCpuUsage = mpCpuUsage;
	}
	public Integer getMpMemUsage()
	{
		return mpMemUsage;
	}
	public void setMpMemUsage(Integer mpMemUsage)
	{
		this.mpMemUsage = mpMemUsage;
	}
	public Long getMpMemTotal()
	{
		return mpMemTotal;
	}
	public void setMpMemTotal(Long mpMemTotal)
	{
		this.mpMemTotal = mpMemTotal;
	}
	public Long getMpMemUsed()
	{
		return mpMemUsed;
	}
	public void setMpMemUsed(Long mpMemUsed)
	{
		this.mpMemUsed = mpMemUsed;
	}
	public Long getMpMemFree()
	{
		return mpMemFree;
	}
	public void setMpMemFree(Long mpMemFree)
	{
		this.mpMemFree = mpMemFree;
	}
	public Integer getSpCpuUsage()
	{
		return spCpuUsage;
	}
	public void setSpCpuUsage(Integer spCpuUsage)
	{
		this.spCpuUsage = spCpuUsage;
	}
	public Integer getSpMemUsage()
	{
		return spMemUsage;
	}
	public void setSpMemUsage(Integer spMemUsage)
	{
		this.spMemUsage = spMemUsage;
	}
	public Long getSpMemTotal()
	{
		return spMemTotal;
	}
	public void setSpMemTotal(Long spMemTotal)
	{
		this.spMemTotal = spMemTotal;
	}
	public Long getSpMemUsed()
	{
		return spMemUsed;
	}
	public void setSpMemUsed(Long spMemUsed)
	{
		this.spMemUsed = spMemUsed;
	}
	public Long getSpMemFree()
	{
		return spMemFree;
	}
	public void setSpMemFree(Long spMemFree)
	{
		this.spMemFree = spMemFree;
	}
}
