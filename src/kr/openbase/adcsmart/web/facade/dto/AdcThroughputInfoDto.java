/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author paul
 *
 */
public class AdcThroughputInfoDto {

	private List<AdcThroughputDataDto> adcThroughputDataList;
	private List<VsConfigEventDto> vsConfigEventList;
	
	public AdcThroughputInfoDto() {
		adcThroughputDataList = new ArrayList<AdcThroughputDataDto>();
		vsConfigEventList = new ArrayList<VsConfigEventDto>();
	}

	/**
	 * @return the adcThroughputDataList
	 */
	public List<AdcThroughputDataDto> getAdcThroughputDataList() {
		return adcThroughputDataList;
	}

	/**
	 * @param adcThroughputDataList the adcThroughputDataList to set
	 */
	public void setAdcThroughputDataList(
			List<AdcThroughputDataDto> adcThroughputDataList) {
		this.adcThroughputDataList = adcThroughputDataList;
	}

	/**
	 * @return the vsConfigEventList
	 */
	public List<VsConfigEventDto> getVsConfigEventList() {
		return vsConfigEventList;
	}

	/**
	 * @param vsConfigEventList the vsConfigEventList to set
	 */
	public void setVsConfigEventList(List<VsConfigEventDto> vsConfigEventList) {
		this.vsConfigEventList = vsConfigEventList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AdcThroughputInfoDto [adcThroughputDataList="
				+ adcThroughputDataList + ", vsConfigEventList="
				+ vsConfigEventList + "]";
	}
	
}
