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
public class AdcConnectionInfoDto {

	private List<AdcConnectionDataDto> adcConnectionDataList;
	private List<VsConfigEventDto> vsConfigEventList;
	
	public AdcConnectionInfoDto() {
		adcConnectionDataList = new ArrayList<AdcConnectionDataDto>();
		vsConfigEventList = new ArrayList<VsConfigEventDto>();
	}

	/**
	 * @return the adcConnectionDataList
	 */
	public List<AdcConnectionDataDto> getAdcConnectionDataList() {
		return adcConnectionDataList;
	}

	/**
	 * @param adcConnectionDataList the adcConnectionDataList to set
	 */
	public void setAdcConnectionDataList(
			List<AdcConnectionDataDto> adcConnectionDataList) {
		this.adcConnectionDataList = adcConnectionDataList;
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
		return "AdcConnectionInfoDto [adcConnectionDataList="
				+ adcConnectionDataList + ", vsConfigEventList="
				+ vsConfigEventList + "]";
	}
	
}
