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
public class NetworkMapDto {

	private Integer totalCountVs;
	private Integer availableCountVs;
	private Integer unavailableCountVs;
	private Integer disabledCountVs;
	private Float unavailablePercentVs;
	private List<NetworkMapVsDto> networkMapVsList;
	
	/**
	 * 
	 */
	public NetworkMapDto() {
		totalCountVs = 0;
		availableCountVs = 0;
		unavailableCountVs = 0;
		disabledCountVs = 0;
		unavailablePercentVs = 0.0f;
		networkMapVsList = new ArrayList<NetworkMapVsDto>();
	}

	/**
	 * @return the totalCountVs
	 */
	public Integer getTotalCountVs() {
		return totalCountVs;
	}

	/**
	 * @param totalCountVs the totalCountVs to set
	 */
	public void setTotalCountVs(Integer totalCountVs) {
		this.totalCountVs = totalCountVs;
	}

	/**
	 * @return the availableCountVs
	 */
	public Integer getAvailableCountVs() {
		return availableCountVs;
	}

	/**
	 * @param availableCountVs the availableCountVs to set
	 */
	public void setAvailableCountVs(Integer availableCountVs) {
		this.availableCountVs = availableCountVs;
	}

	/**
	 * @return the unavailableCountVs
	 */
	public Integer getUnavailableCountVs() {
		return unavailableCountVs;
	}

	/**
	 * @param unavailableCountVs the unavailableCountVs to set
	 */
	public void setUnavailableCountVs(Integer unavailableCountVs) {
		this.unavailableCountVs = unavailableCountVs;
	}

	/**
	 * @return the disabledCountVs
	 */
	public Integer getDisabledCountVs() {
		return disabledCountVs;
	}

	/**
	 * @param disabledCountVs the disabledCountVs to set
	 */
	public void setDisabledCountVs(Integer disabledCountVs) {
		this.disabledCountVs = disabledCountVs;
	}

	/**
	 * @return the unavailablePercentVs
	 */
	public Float getUnavailablePercentVs() {
		return unavailablePercentVs;
	}

	/**
	 * @param unavailablePercentVs the unavailablePercentVs to set
	 */
	public void setUnavailablePercentVs(Float unavailablePercentVs) {
		this.unavailablePercentVs = unavailablePercentVs;
	}

	/**
	 * @return the networkMapVsList
	 */
	public List<NetworkMapVsDto> getNetworkMapVsList() {
		return networkMapVsList;
	}

	/**
	 * @param networkMapVsList the networkMapVsList to set
	 */
	public void setNetworkMapVsList(List<NetworkMapVsDto> networkMapVsList) {
		this.networkMapVsList = networkMapVsList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NetworkMapDto [totalCountVs=" + totalCountVs
				+ ", availableCountVs=" + availableCountVs
				+ ", unavailableCountVs=" + unavailableCountVs
				+ ", disabledCountVs=" + disabledCountVs
				+ ", unavailablePercentVs=" + unavailablePercentVs
				+ ", networkMapVsList=" + networkMapVsList + "]";
	}

}
