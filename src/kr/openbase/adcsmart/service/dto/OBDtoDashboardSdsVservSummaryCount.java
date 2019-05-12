package kr.openbase.adcsmart.service.dto;

public class OBDtoDashboardSdsVservSummaryCount
{
	private Integer vsSummaryCount;

	@Override
	public String toString()
	{
		return "OBDtoDashboardSdsVservSummaryCount [vsSummaryCount=" + vsSummaryCount + "]";
	}

	public Integer getVsSummaryCount()
	{
		return vsSummaryCount;
	}

	public void setVsSummaryCount(Integer vsSummaryCount)
	{
		this.vsSummaryCount = vsSummaryCount;
	}	
}
