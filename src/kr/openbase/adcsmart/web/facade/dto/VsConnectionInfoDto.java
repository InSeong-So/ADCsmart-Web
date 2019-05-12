/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author paul
 *
 */
public class VsConnectionInfoDto {

	private Date maxDate;
	private Date minDate;
	private String maxConnections;
	private String minConnections;
	private String avgConnections;
	private List<VsConnectionDataDto> vsConnectionDataList;
	private List<VsConfigEventDto> vsConfigEventList;
	
	public VsConnectionInfoDto() {
		vsConnectionDataList = new ArrayList<VsConnectionDataDto>();
		vsConfigEventList = new ArrayList<VsConfigEventDto>();
	}
	
	/**
	 * @return the maxDate
	 */
	public Date getMaxDate() {
		return maxDate;
	}
	
	/**
	 * @param maxDate the maxDate to set
	 */
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}
	
	/**
	 * @return the minDate
	 */
	public Date getMinDate() {
		return minDate;
	}
	
	/**
	 * @param minDate the minDate to set
	 */
	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}
	
	/**
	 * @return the maxConnections
	 */
	public String getMaxConnections() {
		return maxConnections;
	}

	/**
	 * @param maxConnections the maxConnections to set
	 */
	public void setMaxConnections(String maxConnections) {
		this.maxConnections = maxConnections;
	}

	/**
	 * @return the minConnections
	 */
	public String getMinConnections() {
		return minConnections;
	}

	/**
	 * @param minConnections the minConnections to set
	 */
	public void setMinConnections(String minConnections) {
		this.minConnections = minConnections;
	}

	/**
	 * @return the avgConnections
	 */
	public String getAvgConnections() {
		return avgConnections;
	}

	/**
	 * @param avgConnections the avgConnections to set
	 */
	public void setAvgConnections(String avgConnections) {
		this.avgConnections = avgConnections;
	}

	/**
	 * @return the vsConnectionDataList
	 */
	public List<VsConnectionDataDto> getVsConnectionDataList() {
		return vsConnectionDataList;
	}
	
	/**
	 * @param vsConnectionDataList the vsConnectionDataList to set
	 */
	public void setVsConnectionDataList(
			List<VsConnectionDataDto> vsConnectionDataList) {
		this.vsConnectionDataList = vsConnectionDataList;
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
		return "VsConnectionInfoDto [maxDate=" + maxDate + ", minDate="
				+ minDate + ", maxConnections=" + maxConnections
				+ ", minConnections=" + minConnections + ", avgConnections="
				+ avgConnections + ", vsConnectionDataList="
				+ vsConnectionDataList + ", vsConfigEventList="
				+ vsConfigEventList + "]";
	}
	
}
