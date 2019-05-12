/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoUsageCpu;

/**
 * @author paul
 *
 */
public class AdcCpuDataDto {
	
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
		return "AdcCpuUsageDataDto [occurredTime=" + occurredTime + ", usage="
				+ usage + "]";
	}
	
	public static AdcCpuDataDto getAdcCpuData(OBDtoUsageCpu obDtoUsageCpu) {
		AdcCpuDataDto adcCpuUsageData = new AdcCpuDataDto();
		adcCpuUsageData.setOccurredTime(obDtoUsageCpu.getOccurTime());
		adcCpuUsageData.setUsage(obDtoUsageCpu.getUsage());
		return adcCpuUsageData;
	}
	
}
