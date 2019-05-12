/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPerfSslTrans;

/**
 * @author paul
 *
 */
public class PerfSslTransactionDataDto {

	private Date occurredTime;
	private Long sslTransactionPerSec;
	
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
	 * @return the sslTransactionPerSec
	 */
	public Long getSslTransactionPerSec() {
		return sslTransactionPerSec;
	}
	/**
	 * @param sslTransactionPerSec the sslTransactionPerSec to set
	 */
	public void setSslTransactionPerSec(Long sslTransactionPerSec) {
		this.sslTransactionPerSec = sslTransactionPerSec;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PerfSslTransactionDataDto [occurredTime=" + occurredTime
				+ ", sslTransactionPerSec=" + sslTransactionPerSec + "]";
	}
	
	public static PerfSslTransactionDataDto getPerfSslTransactionData(OBDtoAdcPerfSslTrans adcPerfSslTransaction)
			throws Exception {
		PerfSslTransactionDataDto perfSslTransactionData = new PerfSslTransactionDataDto();
		perfSslTransactionData.setOccurredTime(adcPerfSslTransaction.getOccurTime());
		perfSslTransactionData.setSslTransactionPerSec(adcPerfSslTransaction.getSslTransPerSecond());
		return perfSslTransactionData;
	}
	
}
