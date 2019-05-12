/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoUsageConnection;

/**
 * @author paul
 *
 */
public class AdcConnectionDataDto {

	private Date occurredTime;
	private Long connections;
	
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
	 * @return the connections
	 */
	public Long getConnections() {
		return connections;
	}
	/**
	 * @param connections the connections to set
	 */
	public void setConnections(Long connections) {
		this.connections = connections;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AdcConnectionDataDto [occurredTime=" + occurredTime
				+ ", connections=" + connections + "]";
	}
	
	public static AdcConnectionDataDto getAdcConnectionData(OBDtoUsageConnection obDtoUsageConnection) {
		AdcConnectionDataDto adcConnectionData = new AdcConnectionDataDto();
		adcConnectionData.setOccurredTime(obDtoUsageConnection.getOccurTime());
		adcConnectionData.setConnections(obDtoUsageConnection.getConns());
		return adcConnectionData;
	}
	
}
