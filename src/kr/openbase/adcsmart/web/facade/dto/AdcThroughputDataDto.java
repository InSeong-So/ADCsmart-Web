/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoUsageThroughput;

/**
 * @author paul
 *
 */
public class AdcThroughputDataDto {

	private Date occurredTime;
	private Long bps;
	private long pps;
	
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
	 * @return the bps
	 */
	public Long getBps() {
		return bps;
	}
	/**
	 * @param bps the bps to set
	 */
	public void setBps(Long bps) {
		this.bps = bps;
	}
	/**
	 * @return the pps
	 */
	public long getPps() {
		return pps;
	}
	/**
	 * @param pps the pps to set
	 */
	public void setPps(long pps) {
		this.pps = pps;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AdcThroughputDataDto [occurredTime=" + occurredTime + ", bps="
				+ bps + ", pps=" + pps + "]";
	}
	
	public static AdcThroughputDataDto getAdcThroughputData(OBDtoUsageThroughput obDtoUsageThroughput) {
		AdcThroughputDataDto adcThroughputData = new AdcThroughputDataDto();
		adcThroughputData.setOccurredTime(obDtoUsageThroughput.getOccurTime());
		adcThroughputData.setBps(obDtoUsageThroughput.getBps());
		adcThroughputData.setPps(obDtoUsageThroughput.getPps());
		return adcThroughputData;
	}
	
}
