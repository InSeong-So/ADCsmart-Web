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
public class AdcCpuInfoDto {
	
	private List<AdcCpuDataDto> adcCpuDataList;
	
	public AdcCpuInfoDto() {
		adcCpuDataList = new ArrayList<AdcCpuDataDto>();
	}

	/**
	 * @return the adcCpuDataList
	 */
	public List<AdcCpuDataDto> getAdcCpuDataList() {
		return adcCpuDataList;
	}

	/**
	 * @param adcCpuDataList the adcCpuDataList to set
	 */
	public void setAdcCpuDataList(List<AdcCpuDataDto> adcCpuDataList) {
		this.adcCpuDataList = adcCpuDataList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AdcCpuInfoDto [adcCpuDataList=" + adcCpuDataList + "]";
	}

}
