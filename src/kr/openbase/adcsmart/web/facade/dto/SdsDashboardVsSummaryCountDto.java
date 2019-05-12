package kr.openbase.adcsmart.web.facade.dto;

import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservSummaryCount;

public class SdsDashboardVsSummaryCountDto
{
	private Integer vsCount;//VirtualServer Count


	@Override
	public String toString()
	{
		return "SdsDashboardVsSummaryCountDto [vsCount=" + vsCount + "]";
	}
	public Integer getVsCount()
	{
		return vsCount;
	}
	public void setVsCount(Integer vsCount)
	{
		this.vsCount = vsCount;
	}
	
	public static SdsDashboardVsSummaryCountDto getSdsDashboardVsSummaryCount(OBDtoDashboardSdsVservSummaryCount obDtoDashboardSdsVservSummaryListCount)
	{
		SdsDashboardVsSummaryCountDto vsSummaryCount = new SdsDashboardVsSummaryCountDto();
		vsSummaryCount.setVsCount(obDtoDashboardSdsVservSummaryListCount.getVsSummaryCount());
		return vsSummaryCount;	
	}
}

