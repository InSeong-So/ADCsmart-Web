package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;


public class VirtualSvrDto {
	protected String index;
	protected Integer adcIndex;
	protected String status;
	protected String state;
	protected String name;
	protected String virtualIp;
	protected Date lastUpdateTime;
	
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public Integer getAdcIndex() {
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex) {
		this.adcIndex = adcIndex;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVirtualIp() {
		return virtualIp;
	}
	public void setVirtualIp(String virtualIp) {
		this.virtualIp = virtualIp;
	}
	
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	@Override
	public String toString() {
		return "VirtualSvrDto [index=" + index + ", adcIndex=" + adcIndex
				+ ", status=" + status + ", state=" + state + ", name=" + name
				+ ", virtualIp=" + virtualIp + ", lastUpdateTime="
				+ lastUpdateTime + "]";
	}
	
}
