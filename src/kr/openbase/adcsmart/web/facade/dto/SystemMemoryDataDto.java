/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

/**
 * @author paul
 *
 */
public class SystemMemoryDataDto {
	
	private Date occurredTime;
	private Integer usage;
	private Long total;
	private Long used;
	
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
	/**
	 * @return the total
	 */
	public Long getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(Long total) {
		this.total = total;
	}
	/**
	 * @return the used
	 */
	public Long getUsed() {
		return used;
	}
	/**
	 * @param used the used to set
	 */
	public void setUsed(Long used) {
		this.used = used;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SystemMemoryDataDto [occurredTime=" + occurredTime + ", usage="
				+ usage + ", total=" + total + ", used=" + used + "]";
	}
	
}
