/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

/**
 * @author paul
 *
 */
public class VsConnectionDataDto {

	private Date occurredTime;
	private Long maxConnections;
	private Long activeConnections;
	private Long totalConnections;
	
	/**
	 * @return the occurredTime
	 */
	public Date getOccurredTime() {
		return occurredTime;
	}
	
	/**
	 * @param occurredTime the occurredTime to set
	 */
	public void setOccurredTime(Date occurredTime) {
		this.occurredTime = occurredTime;
	}
	
	/**
	 * @return the maxConnections
	 */
	public Long getMaxConnections() {
		return maxConnections;
	}
	
	/**
	 * @param maxConnections the maxConnections to set
	 */
	public void setMaxConnections(Long maxConnections) {
		this.maxConnections = maxConnections;
	}
	
	/**
	 * @return the activeConnections
	 */
	public Long getActiveConnections() {
		return activeConnections;
	}
	
	/**
	 * @param activeConnections the activeConnections to set
	 */
	public void setActiveConnections(Long activeConnections) {
		this.activeConnections = activeConnections;
	}
	
	/**
	 * @return the totalConnections
	 */
	public Long getTotalConnections() {
		return totalConnections;
	}
	
	/**
	 * @param totalConnections the totalConnections to set
	 */
	public void setTotalConnections(Long totalConnections) {
		this.totalConnections = totalConnections;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VsConnectionDataDto [occurredTime=" + occurredTime
				+ ", maxConnections=" + maxConnections
				+ ", activeConnections=" + activeConnections
				+ ", totalConnections=" + totalConnections + "]";
	}
	
}
