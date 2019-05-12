/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcSystemLog;

/**
 * @author paul
 *
 */
public class AdcSystemLogDto {

	private String occurredTime;
	private Integer logLevel;
	private Integer logType;
	private Integer adcIndex;
	private String 	adcName;
	private String 	vsIndex;
	private String 	event;
	private Integer status;
	private String finishTime; //장애 해결 시각. 미해결 장애에는 null
	
	public static AdcSystemLogDto toAdcSystemLogDto(OBDtoAdcSystemLog svcAdcSystemLog) {
		AdcSystemLogDto adcSystemLog = new AdcSystemLogDto();
		if (null != svcAdcSystemLog.getOccurTime()) {
			adcSystemLog.setOccurredTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
					svcAdcSystemLog.getOccurTime()));			
		}
		adcSystemLog.setLogType(svcAdcSystemLog.getLogType());
		adcSystemLog.setLogLevel(svcAdcSystemLog.getLogLevel());
		adcSystemLog.setAdcIndex(svcAdcSystemLog.getAdcIndex());
		adcSystemLog.setAdcName(svcAdcSystemLog.getAdcName());
		adcSystemLog.setVsIndex(svcAdcSystemLog.getVsIndex());
		adcSystemLog.setEvent(svcAdcSystemLog.getEvent());
		adcSystemLog.setStatus(svcAdcSystemLog.getStatus());	
		if (null != svcAdcSystemLog.getFinishTime()) {
			adcSystemLog.setFinishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(svcAdcSystemLog.getFinishTime()));
		}
		return adcSystemLog;
	}
	
	public static List<AdcSystemLogDto> toAdcSystemLogDto(List<OBDtoAdcSystemLog> systemLogsFromSvc) {
		if (systemLogsFromSvc == null)
			return null;
		
		List<AdcSystemLogDto> systemLogs = new ArrayList<AdcSystemLogDto>();
		for (OBDtoAdcSystemLog e : systemLogsFromSvc)
			systemLogs.add(AdcSystemLogDto.toAdcSystemLogDto(e));
		
		return systemLogs;
	}
	
	/**
	 * @return the occurredTime
	 */
	public String getOccurredTime() {
		return occurredTime;
	}
	/**
	 * @param occurredTime the occurredTime to set
	 */
	public void setOccurredTime(String occurredTime) {
		this.occurredTime = occurredTime;
	}
	/**
	 * @return the logLevel
	 */
	public Integer getLogLevel() {
		return logLevel;
	}
	/**
	 * @param logLevel the logLevel to set
	 */
	public void setLogLevel(Integer logLevel) {
		this.logLevel = logLevel;
	}
	/**
	 * @return the logType
	 */
	public Integer getLogType() {
		return logType;
	}
	/**
	 * @param logType the logType to set
	 */
	public void setLogType(Integer logType) {
		this.logType = logType;
	}
	/**
	 * @return the adcIndex
	 */
	public Integer getAdcIndex() {
		return adcIndex;
	}
	/**
	 * @param adcIndex the adcIndex to set
	 */
	public void setAdcIndex(Integer adcIndex) {
		this.adcIndex = adcIndex;
	}
	/**
	 * @return the adcName
	 */
	public String getAdcName() {
		return adcName;
	}
	/**
	 * @param adcName the adcName to set
	 */
	public void setAdcName(String adcName) {
		this.adcName = adcName;
	}
	/**
	 * @return the vsIndex
	 */
	public String getVsIndex() {
		return vsIndex;
	}
	/**
	 * @param vsIndex the vsIndex to set
	 */
	public void setVsIndex(String vsIndex) {
		this.vsIndex = vsIndex;
	}
	/**
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}
	/**
	 * @param event the event to set
	 */
	public void setEvent(String event) {
		this.event = event;
	}
	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getFinishTime()
	{
		return finishTime;
	}

	public void setFinishTime(String finishTime)
	{
		this.finishTime = finishTime;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AdcSystemLogDto [occurredTime=" + occurredTime + ", FinishTime=" + finishTime + ", logLevel="
				+ logLevel + ", logType=" + logType + ", adcIndex=" + adcIndex
				+ ", adcName=" + adcName + ", vsIndex=" + vsIndex + ", event="
				+ event + ", status=" + status + "]";
	}
	
}
