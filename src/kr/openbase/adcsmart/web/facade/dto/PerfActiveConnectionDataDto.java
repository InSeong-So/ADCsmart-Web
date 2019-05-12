/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPerfConnection;

/**
 * @author paul
 *
 */
public class PerfActiveConnectionDataDto {
	
	private Date occurredTime;
	private Long connectionPerSec;
	
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
	 * @return the connectionPerSec
	 */
	public Long getConnectionPerSec() {
		return connectionPerSec;
	}
	/**
	 * @param connectionPerSec the connectionPerSec to set
	 */
	public void setConnectionPerSec(Long connectionPerSec) {
		this.connectionPerSec = connectionPerSec;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PerfActiveConnectionDataDto [occurredTime=" + occurredTime
				+ ", connectionPerSec=" + connectionPerSec + "]";
	}
	public static PerfActiveConnectionDataDto getPerfActiveConnectionData(OBDtoAdcPerfConnection adcPerfConnection)
			throws Exception {
		PerfActiveConnectionDataDto perfActiveConnectionData = new PerfActiveConnectionDataDto();
		perfActiveConnectionData.setOccurredTime(adcPerfConnection.getOccurTime());
		perfActiveConnectionData.setConnectionPerSec(adcPerfConnection.getConnsPerSecond());
		return perfActiveConnectionData;
	}

}
