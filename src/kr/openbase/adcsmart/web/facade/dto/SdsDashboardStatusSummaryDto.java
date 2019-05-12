/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsStatusSummary;

/**
 * @author paul
 *
 */
public class SdsDashboardStatusSummaryDto {

	private Integer adcTotalCount;
	private Integer adcAvailableCount;
	private Integer adcUnavailableCount;
	private Integer vsTotalCount;
	private Integer vsAvailableCount;
	private Integer vsUnavailableCount;
	private Integer vsUnavailableCountMinDays;
	private Integer vsDisableCount;
	private Integer faultTotalCount;
	private Integer faultSolvedCount;
	private Integer faultUnsolvedCount;
	private Integer faultWarning;	
	private Integer adcAvailable10Count;
	private Integer adcUnavailable10Count;	
	private Integer vsAvailable10Count;
	private Integer vsUnavailable10Count;
	private Integer vsUnavailable10CountMinDays;
	private Integer vsDisable10Count;
		
	public Integer getAdcTotalCount()
	{
		return adcTotalCount;
	}

	public void setAdcTotalCount(Integer adcTotalCount)
	{
		this.adcTotalCount = adcTotalCount;
	}

	public Integer getAdcAvailableCount()
	{
		return adcAvailableCount;
	}

	public void setAdcAvailableCount(Integer adcAvailableCount)
	{
		this.adcAvailableCount = adcAvailableCount;
	}

	public Integer getAdcUnavailableCount()
	{
		return adcUnavailableCount;
	}

	public void setAdcUnavailableCount(Integer adcUnavailableCount)
	{
		this.adcUnavailableCount = adcUnavailableCount;
	}

	public Integer getVsTotalCount()
	{
		return vsTotalCount;
	}

	public void setVsTotalCount(Integer vsTotalCount)
	{
		this.vsTotalCount = vsTotalCount;
	}

	public Integer getVsAvailableCount()
	{
		return vsAvailableCount;
	}

	public void setVsAvailableCount(Integer vsAvailableCount)
	{
		this.vsAvailableCount = vsAvailableCount;
	}

	public Integer getVsUnavailableCount()
	{
		return vsUnavailableCount;
	}

	public void setVsUnavailableCount(Integer vsUnavailableCount)
	{
		this.vsUnavailableCount = vsUnavailableCount;
	}

	public Integer getVsUnavailableCountMinDays()
	{
		return vsUnavailableCountMinDays;
	}

	public void setVsUnavailableCountMinDays(Integer vsUnavailableCountMinDays)
	{
		this.vsUnavailableCountMinDays = vsUnavailableCountMinDays;
	}

	public Integer getVsDisableCount()
	{
		return vsDisableCount;
	}

	public void setVsDisableCount(Integer vsDisableCount)
	{
		this.vsDisableCount = vsDisableCount;
	}

	public Integer getFaultTotalCount()
	{
		return faultTotalCount;
	}

	public void setFaultTotalCount(Integer faultTotalCount)
	{
		this.faultTotalCount = faultTotalCount;
	}

	public Integer getFaultSolvedCount()
	{
		return faultSolvedCount;
	}

	public void setFaultSolvedCount(Integer faultSolvedCount)
	{
		this.faultSolvedCount = faultSolvedCount;
	}

	public Integer getFaultUnsolvedCount()
	{
		return faultUnsolvedCount;
	}

	public void setFaultUnsolvedCount(Integer faultUnsolvedCount)
	{
		this.faultUnsolvedCount = faultUnsolvedCount;
	}

	public Integer getFaultWarning()
	{
		return faultWarning;
	}

	public void setFaultWarning(Integer faultWarning)
	{
		this.faultWarning = faultWarning;
	}
	
	public Integer getAdcAvailable10Count()
	{
		return adcAvailable10Count;
	}

	public void setAdcAvailable10Count(Integer adcAvailable10Count)
	{
		this.adcAvailable10Count = adcAvailable10Count;
	}

	public Integer getAdcUnavailable10Count()
	{
		return adcUnavailable10Count;
	}

	public void setAdcUnavailable10Count(Integer adcUnavailable10Count)
	{
		this.adcUnavailable10Count = adcUnavailable10Count;
	}

	public Integer getVsAvailable10Count()
	{
		return vsAvailable10Count;
	}

	public void setVsAvailable10Count(Integer vsAvailable10Count)
	{
		this.vsAvailable10Count = vsAvailable10Count;
	}

	public Integer getVsUnavailable10Count()
	{
		return vsUnavailable10Count;
	}

	public void setVsUnavailable10Count(Integer vsUnavailable10Count)
	{
		this.vsUnavailable10Count = vsUnavailable10Count;
	}

	public Integer getVsUnavailable10CountMinDays()
	{
		return vsUnavailable10CountMinDays;
	}

	public void setVsUnavailable10CountMinDays(Integer vsUnavailable10CountMinDays)
	{
		this.vsUnavailable10CountMinDays = vsUnavailable10CountMinDays;
	}

	public Integer getVsDisable10Count()
	{
		return vsDisable10Count;
	}

	public void setVsDisable10Count(Integer vsDisable10Count)
	{
		this.vsDisable10Count = vsDisable10Count;
	}

	@Override
	public String toString()
	{
		return "SdsDashboardStatusSummaryDto [adcTotalCount=" + adcTotalCount + ", adcAvailableCount=" + adcAvailableCount + ", adcUnavailableCount=" + adcUnavailableCount + ", vsTotalCount=" + vsTotalCount + ", vsAvailableCount=" + vsAvailableCount + ", vsUnavailableCount=" + vsUnavailableCount + ", vsUnavailableCountMinDays=" + vsUnavailableCountMinDays + ", vsDisableCount=" + vsDisableCount + ", faultTotalCount=" + faultTotalCount + ", faultSolvedCount=" + faultSolvedCount + ", faultUnsolvedCount=" + faultUnsolvedCount + ", faultWarning=" + faultWarning + ", adcAvailable10Count=" + adcAvailable10Count + ", adcUnavailable10Count=" + adcUnavailable10Count + ", vsAvailable10Count=" + vsAvailable10Count + ", vsUnavailable10Count=" + vsUnavailable10Count + ", vsUnavailable10CountMinDays=" + vsUnavailable10CountMinDays + ", vsDisable10Count=" + vsDisable10Count + "]";
	}
	
	public static SdsDashboardStatusSummaryDto getSdsDashboardStatusSummary(
			OBDtoDashboardSdsStatusSummary obDtoStatusSummary) {
		SdsDashboardStatusSummaryDto statusSummary = new SdsDashboardStatusSummaryDto();
		statusSummary.setAdcTotalCount(obDtoStatusSummary.getAdc());
		statusSummary.setAdcAvailableCount(obDtoStatusSummary.getAdcAvail());
		statusSummary.setAdcUnavailableCount(obDtoStatusSummary.getAdcUnavail());		
		statusSummary.setAdcAvailable10Count(toResult10Count(obDtoStatusSummary.getAdc(), obDtoStatusSummary.getAdcAvail()));
		statusSummary.setAdcUnavailable10Count(toResult10Count(obDtoStatusSummary.getAdc(), obDtoStatusSummary.getAdcUnavail()));		
		statusSummary.setVsTotalCount(obDtoStatusSummary.getVs());
		statusSummary.setVsAvailableCount(obDtoStatusSummary.getVsAvail());
		statusSummary.setVsUnavailableCount(obDtoStatusSummary.getVsUnavail());
		statusSummary.setVsUnavailableCountMinDays(obDtoStatusSummary.getVsUnavailOverNDays());
		statusSummary.setVsDisableCount(obDtoStatusSummary.getVsDisable());		
		statusSummary.setVsAvailable10Count(toResult10Count(obDtoStatusSummary.getVs(), obDtoStatusSummary.getVsAvail()));
		statusSummary.setVsUnavailable10Count(toResult10Count(obDtoStatusSummary.getVs(), obDtoStatusSummary.getVsUnavail()));
		statusSummary.setVsUnavailable10CountMinDays(toResult10Count(obDtoStatusSummary.getVs(), obDtoStatusSummary.getVsUnavailOverNDays()));
		statusSummary.setVsDisable10Count(toResult10Count(obDtoStatusSummary.getVs(), obDtoStatusSummary.getVsDisable()));		
		statusSummary.setFaultTotalCount(obDtoStatusSummary.getFault());
		statusSummary.setFaultSolvedCount(obDtoStatusSummary.getFaultSolved());
		statusSummary.setFaultUnsolvedCount(obDtoStatusSummary.getFaultUnsolved());
		statusSummary.setFaultWarning(obDtoStatusSummary.getFaultWarn());
		return statusSummary;
	}
	
	public static Integer toResult10Count(Integer total, Integer input)
	{		
		Integer totalValue = total;
		Integer inputValue = input;
		Integer retVal = (int)Math.round((double)inputValue / totalValue * 10);				
		return retVal;
	}	
}