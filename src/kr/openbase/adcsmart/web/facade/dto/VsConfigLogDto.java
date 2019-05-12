/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

/**
 * @author paul
 *
 */
public class VsConfigLogDto {
	
	private String occurredTime;
	private String adcType;
	private Integer adcIndex;
	private String adcName;
	private String index;
	private String name;
	private String ipAddress;
	private Integer status;
	
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VsConfigLogDto [occurredTime=" + occurredTime + ", adcType="
				+ adcType + ", adcIndex=" + adcIndex + ", adcName=" + adcName
				+ ", index=" + index + ", name=" + name + ", ipAddress="
				+ ipAddress + ", status=" + status + "]";
	}
	
}
