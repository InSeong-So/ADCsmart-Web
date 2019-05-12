/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;
import kr.openbase.adcsmart.service.utility.OBDefine;

/**
 * @author paul
 *
 */
public class VsConfigEventDto {

	private Integer userType;
	private Integer accessType;
	private Integer objectType;
	private Date occurredTime;
	private Integer accountIndex;
	private String accountName;
	private Integer adcIndex;
	private String adcType;
	private String index;
	private String name;
	private String ipAddress;
	private Integer status;
	private String summary;
	
	/**
	 * @return the userType
	 */
	public Integer getUserType() {
		return userType;
	}
	/**
	 * @param userType the userType to set
	 */
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	/**
	 * @return the accessType
	 */
	public Integer getAccessType() {
		return accessType;
	}
	/**
	 * @param accessType the accessType to set
	 */
	public void setAccessType(Integer accessType) {
		this.accessType = accessType;
	}
	/**
	 * @return the objectType
	 */
	public Integer getObjectType() {
		return objectType;
	}
	/**
	 * @param objectType the objectType to set
	 */
	public void setObjectType(Integer objectType) {
		this.objectType = objectType;
	}
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
	 * @return the accountIndex
	 */
	public Integer getAccountIndex() {
		return accountIndex;
	}
	/**
	 * @param accountIndex the accountIndex to set
	 */
	public void setAccountIndex(Integer accountIndex) {
		this.accountIndex = accountIndex;
	}
	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}
	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
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
	 * @return the adcType
	 */
	public String getAdcType() {
		return adcType;
	}
	/**
	 * @param adcType the adcType to set
	 */
	public void setAdcType(String adcType) {
		this.adcType = adcType;
	}
	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}
	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
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
	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}
	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VsConfigEventDto [userType=" + userType + ", accessType="
				+ accessType + ", objectType=" + objectType + ", occurredTime="
				+ occurredTime + ", accountIndex=" + accountIndex
				+ ", accountName=" + accountName + ", adcIndex=" + adcIndex
				+ ", adcType=" + adcType + ", index=" + index + ", name="
				+ name + ", ipAddress=" + ipAddress + ", status=" + status
				+ ", summary=" + summary + "]";
	}
	
	public static VsConfigEventDto getVsConfigEvent(OBDtoAdcConfigHistory obDtoAdcConfigHistory) {
		VsConfigEventDto vsConfigEvent = new VsConfigEventDto();
		vsConfigEvent.setUserType(obDtoAdcConfigHistory.getUserType());
		vsConfigEvent.setAccessType(obDtoAdcConfigHistory.getAccessType());
		vsConfigEvent.setObjectType(obDtoAdcConfigHistory.getObjectType());
		vsConfigEvent.setOccurredTime(obDtoAdcConfigHistory.getOccurTime());
		vsConfigEvent.setAccountIndex(obDtoAdcConfigHistory.getAccountIndex());
		vsConfigEvent.setAccountName(obDtoAdcConfigHistory.getAccountName());
		vsConfigEvent.setAdcIndex(obDtoAdcConfigHistory.getAdcIndex());
		if (obDtoAdcConfigHistory.getAdcType() == OBDefine.ADC_TYPE_F5) {
			vsConfigEvent.setAdcType("F5");	
		} else if (obDtoAdcConfigHistory.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
			vsConfigEvent.setAdcType("Alteon");
		}
		vsConfigEvent.setIndex(obDtoAdcConfigHistory.getVsIndex());
		vsConfigEvent.setName(obDtoAdcConfigHistory.getVsName());
		vsConfigEvent.setIpAddress(obDtoAdcConfigHistory.getVsIp());
		vsConfigEvent.setStatus(obDtoAdcConfigHistory.getVsStatus());
		vsConfigEvent.setSummary(obDtoAdcConfigHistory.getSummary());
		return vsConfigEvent;	
	}
	
}
