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
public class AdcMemoryInfoDto {

	private List<AdcMemoryDataDto> adcMemoryDataList;
	
	public AdcMemoryInfoDto() {
		adcMemoryDataList = new ArrayList<AdcMemoryDataDto>();
	}

	/**
	 * @return the adcMemoryDataList
	 */
	public List<AdcMemoryDataDto> getAdcMemoryDataList() {
		return adcMemoryDataList;
	}

	/**
	 * @param adcMemoryDataList the adcMemoryDataList to set
	 */
	public void setAdcMemoryDataList(List<AdcMemoryDataDto> adcMemoryDataList) {
		this.adcMemoryDataList = adcMemoryDataList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AdcMemoryInfoDto [adcMemoryDataList=" + adcMemoryDataList + "]";
	}
	
}
