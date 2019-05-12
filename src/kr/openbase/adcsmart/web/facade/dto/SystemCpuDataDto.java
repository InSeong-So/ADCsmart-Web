/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

/**
 * @author paul
 *
 */
public class SystemCpuDataDto {

	private Date occurredTime;
	private Integer usage;
	
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
	 * @return the usage
	 */
	public Integer getUsage() {
		return usage;
	}
	/**
	 * @param usage the usage to set
	 */
	public void setUsage(Integer usage) {
		this.usage = usage;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SystemCpuDataDto [occurredTime=" + occurredTime + ", usage="
				+ usage + "]";
	}
	
}
