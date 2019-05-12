/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

/**
 * @author paul
 *
 */
public class SystemStatusSummaryDto {
	
	private Integer adcTotalCount;
	private Integer adcAvailableCount;
	private Integer adcUnavailableCount;
	private Integer vsTotalCount;
	private Integer vsAvailableCount;
	private Integer vsDisableCount;
	private Integer vsUnavailableCount;
	private Integer cpuUsage;
	private Integer memoryUsage;
	private Integer hddUsage;
	
	/**
	 * @return the adcTotalCount
	 */
	public Integer getAdcTotalCount() {
		return adcTotalCount;
	}
	/**
	 * @param adcTotalCount the adcTotalCount to set
	 */
	public void setAdcTotalCount(Integer adcTotalCount) {
		this.adcTotalCount = adcTotalCount;
	}
	/**
	 * @return the adcAvailableCount
	 */
	public Integer getAdcAvailableCount() {
		return adcAvailableCount;
	}
	/**
	 * @param adcAvailableCount the adcAvailableCount to set
	 */
	public void setAdcAvailableCount(Integer adcAvailableCount) {
		this.adcAvailableCount = adcAvailableCount;
	}
	/**
	 * @return the adcUnavailableCount
	 */
	public Integer getAdcUnavailableCount() {
		return adcUnavailableCount;
	}
	/**
	 * @param adcUnavailableCount the adcUnavailableCount to set
	 */
	public void setAdcUnavailableCount(Integer adcUnavailableCount) {
		this.adcUnavailableCount = adcUnavailableCount;
	}
	/**
	 * @return the vsTotalCount
	 */
	public Integer getVsTotalCount() {
		return vsTotalCount;
	}
	/**
	 * @param vsTotalCount the vsTotalCount to set
	 */
	public void setVsTotalCount(Integer vsTotalCount) {
		this.vsTotalCount = vsTotalCount;
	}
	/**
	 * @return the vsAvailableCount
	 */
	public Integer getVsAvailableCount() {
		return vsAvailableCount;
	}
	/**
	 * @param vsAvailableCount the vsAvailableCount to set
	 */
	public void setVsAvailableCount(Integer vsAvailableCount) {
		this.vsAvailableCount = vsAvailableCount;
	}
	/**
	 * @return the vsDisableCount
	 */
	public Integer getVsDisableCount() {
		return vsDisableCount;
	}
	/**
	 * @param vsDisableCount the vsDisableCount to set
	 */
	public void setVsDisableCount(Integer vsDisableCount) {
		this.vsDisableCount = vsDisableCount;
	}
	/**
	 * @return the vsUnavailableCount
	 */
	public Integer getVsUnavailableCount() {
		return vsUnavailableCount;
	}
	/**
	 * @param vsUnavailableCount the vsUnavailableCount to set
	 */
	public void setVsUnavailableCount(Integer vsUnavailableCount) {
		this.vsUnavailableCount = vsUnavailableCount;
	}
	/**
	 * @return the cpuUsage
	 */
	public Integer getCpuUsage() {
		return cpuUsage;
	}
	/**
	 * @param cpuUsage the cpuUsage to set
	 */
	public void setCpuUsage(Integer cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	/**
	 * @return the memoryUsage
	 */
	public Integer getMemoryUsage() {
		return memoryUsage;
	}
	/**
	 * @param memoryUsage the memoryUsage to set
	 */
	public void setMemoryUsage(Integer memoryUsage) {
		this.memoryUsage = memoryUsage;
	}
	/**
	 * @return the hddUsage
	 */
	public Integer getHddUsage() {
		return hddUsage;
	}
	/**
	 * @param hddUsage the hddUsage to set
	 */
	public void setHddUsage(Integer hddUsage) {
		this.hddUsage = hddUsage;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SystemStatusSummaryDto [adcTotalCount=" + adcTotalCount
				+ ", adcAvailableCount=" + adcAvailableCount
				+ ", adcUnavailableCount=" + adcUnavailableCount
				+ ", vsTotalCount=" + vsTotalCount + ", vsAvailableCount="
				+ vsAvailableCount + ", vsDisableCount=" + vsDisableCount
				+ ", vsUnavailableCount=" + vsUnavailableCount + ", cpuUsage="
				+ cpuUsage + ", memoryUsage=" + memoryUsage + ", hddUsage="
				+ hddUsage + "]";
	}
	
}
