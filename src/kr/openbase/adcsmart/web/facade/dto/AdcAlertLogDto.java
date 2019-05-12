package kr.openbase.adcsmart.web.facade.dto;

public class AdcAlertLogDto {
	private String occurTime;
	private String alertTime;
	private Integer adcIndex;
	private String adcName;
	private Integer type;
	private Integer status;
	private String event;
	private Integer actionSyslog;
	private Integer actionSnmptrap;
	private Integer actionSMS;
	private boolean isNew;

	public String getOccurTime() {
		return occurTime;
	}

	public Integer getActionSMS() {
		return actionSMS;
	}

	public void setActionSMS(Integer actionSMS) {
		this.actionSMS = actionSMS;
	}

	public void setOccurTime(String occurTime) {
		this.occurTime = occurTime;
	}

	public Integer getAdcIndex() {
		return adcIndex;
	}

	public void setAdcIndex(Integer adcIndex) {
		this.adcIndex = adcIndex;
	}

	public String getAdcName() {
		return adcName;
	}

	public void setAdcName(String adcName) {
		this.adcName = adcName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Integer getActionSyslog() {
		return actionSyslog;
	}

	public void setActionSyslog(Integer actionSyslog) {
		this.actionSyslog = actionSyslog;
	}

	public Integer getActionSnmptrap() {
		return actionSnmptrap;
	}

	public void setActionSnmptrap(Integer actionSnmptrap) {
		this.actionSnmptrap = actionSnmptrap;
	}

	public boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}

	public String getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(String alertTime) {
		this.alertTime = alertTime;
	}

	@Override
	public String toString() {
		return "AdcAlertLogDto [occurTime=" + occurTime + ", alertTime=" + alertTime + ", adcIndex=" + adcIndex
				+ ", adcName=" + adcName + ", type=" + type + ", status=" + status + ", event=" + event
				+ ", actionSyslog=" + actionSyslog + ", actionSnmptrap=" + actionSnmptrap + ", isNew=" + isNew + "]";
	}
}