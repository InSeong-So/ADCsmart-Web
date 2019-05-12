/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPerfTroughput;

/**
 * @author paul
 *
 */
public class PerfThroughputDataDto {

	private Date occurredTime;
	private Long pps;
	private Long bps;
	
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
	 * @return the pps
	 */
	public Long getPps() {
		return pps;
	}
	/**
	 * @param pps the pps to set
	 */
	public void setPps(Long pps) {
		this.pps = pps;
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PerfThroughputDataDto [occurredTime=" + occurredTime + ", pps="
				+ pps + ", bps=" + bps + "]";
	}
	
	public static PerfThroughputDataDto getPerfThroughputData(OBDtoAdcPerfTroughput adcPerfThroughput)
			throws Exception {
		PerfThroughputDataDto perfThroughputData = new PerfThroughputDataDto();
		perfThroughputData.setOccurredTime(adcPerfThroughput.getOccurTime());
		perfThroughputData.setBps(adcPerfThroughput.getBps());
		perfThroughputData.setPps(adcPerfThroughput.getPps());
		return perfThroughputData;
	}
	
}