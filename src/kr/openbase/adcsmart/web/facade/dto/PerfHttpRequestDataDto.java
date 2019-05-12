/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPerfHttpReq;

/**
 * @author paul
 *
 */
public class PerfHttpRequestDataDto {
	
	private Date occurredTime;
	private Long httpRequestPerSec;
	
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
	 * @return the httpRequestPerSec
	 */
	public Long getHttpRequestPerSec() {
		return httpRequestPerSec;
	}
	/**
	 * @param httpRequestPerSec the httpRequestPerSec to set
	 */
	public void setHttpRequestPerSec(Long httpRequestPerSec) {
		this.httpRequestPerSec = httpRequestPerSec;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PerfHttpRequestDataDto [occurredTime=" + occurredTime
				+ ", httpRequestPerSec=" + httpRequestPerSec + "]";
	}
	
	public static PerfHttpRequestDataDto getPerfHttpRequestData(OBDtoAdcPerfHttpReq adcPerfHttpReq)
			throws Exception {
		PerfHttpRequestDataDto perfHttpRequestData = new PerfHttpRequestDataDto();
		perfHttpRequestData.setOccurredTime(adcPerfHttpReq.getOccurTime());
		perfHttpRequestData.setHttpRequestPerSec(adcPerfHttpReq.getHttpReqPerSecond());
		return perfHttpRequestData;
	}
	
}
