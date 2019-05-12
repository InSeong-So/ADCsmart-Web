/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPerfTroughput;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPerformance;
import kr.openbase.adcsmart.web.util.NumberUtil;

public class PerfThroughputInfoDto
{
	
	private String maxBps;
	private String minBps;
	private String avgBps;
	private String curBps;
	private Long maxPps;
	private Long minPps;
	private Long avgPps;
	private Long curPps;
	private List<PerfThroughputDataDto> perfThroughputDataList;
	
	public PerfThroughputInfoDto()
	{
		perfThroughputDataList = new ArrayList<PerfThroughputDataDto>();
	}

	public String getMaxBps() 
	{
		return maxBps;
	}
	
	public void setMaxBps(String maxBps)
	{
		this.maxBps = maxBps;
	}

	public String getMinBps()
	{
		return minBps;
	}

	public void setMinBps(String minBps)
	{
		this.minBps = minBps;
	}

	public String getAvgBps()
	{
		return avgBps;
	}

	public void setAvgBps(String avgBps)
	{
		this.avgBps = avgBps;
	}

	public String getCurBps()
	{
		return curBps;
	}

	public void setCurBps(String curBps)
	{
		this.curBps = curBps;
	}

	public Long getMaxPps() 
	{
		return maxPps;
	}

	public void setMaxPps(Long maxPps)
	{
		this.maxPps = maxPps;
	}

	public Long getMinPps() 
	{
		return minPps;
	}

	public void setMinPps(Long minPps)
	{
		this.minPps = minPps;
	}

	public Long getAvgPps() 
	{
		return avgPps;
	}

	public void setAvgPps(Long avgPps)
	{
		this.avgPps = avgPps;
	}

	public Long getCurPps() 
	{
		return curPps;
	}

	public void setCurPps(Long curPps)
	{
		this.curPps = curPps;
	}

	public List<PerfThroughputDataDto> getPerfThroughputDataList()
	{
		return perfThroughputDataList;
	}

	public void setPerfThroughputDataList(List<PerfThroughputDataDto> perfThroughputDataList) 
	{
		this.perfThroughputDataList = perfThroughputDataList;
	}

	@Override
	public String toString()
{
		return "PerfThroughputInfoDto [maxBps=" + maxBps + ", minBps=" + minBps
				+ ", avgBps=" + avgBps + ", curBps=" + curBps + ", maxPps="
				+ maxPps + ", minPps=" + minPps + ", avgPps=" + avgPps
				+ ", curPps=" + curPps + ", perfThroughputDataList="
				+ perfThroughputDataList + "]";
	}
	
	public static PerfThroughputInfoDto getPerfThroughputInfoDto(OBDtoAdcPerformance adcPerformance)throws Exception 
	{
		PerfThroughputInfoDto perfThroughputInfo = new PerfThroughputInfoDto();
		
		perfThroughputInfo.setMaxBps(NumberUtil.toStringWithUnit(adcPerformance.getBpsMax(), ""));
		perfThroughputInfo.setMinBps(NumberUtil.toStringWithUnit(adcPerformance.getBpsMin(), ""));
		perfThroughputInfo.setAvgBps(NumberUtil.toStringWithUnit(adcPerformance.getBpsAvg(), ""));
		perfThroughputInfo.setCurBps(NumberUtil.toStringWithUnit(adcPerformance.getBpsCurr(), ""));
		
		perfThroughputInfo.setMaxPps(adcPerformance.getPpsMax());
		perfThroughputInfo.setMinPps(adcPerformance.getPpsMin());
		perfThroughputInfo.setAvgPps(adcPerformance.getPpsAvg());
		perfThroughputInfo.setCurPps(adcPerformance.getPpsCurr());
		
		if (!CollectionUtils.isEmpty(adcPerformance.getThroughputList()))
		{
			for (OBDtoAdcPerfTroughput adcPerfThroughput : adcPerformance.getThroughputList())
			{
				perfThroughputInfo.getPerfThroughputDataList().add(PerfThroughputDataDto.getPerfThroughputData(adcPerfThroughput));
			}
		}
		return perfThroughputInfo;
	}
	
}
