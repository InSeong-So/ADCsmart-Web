package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoAdcAlertLog {
	private Date occurTime;
	private Date alertTime;
	private Integer adcIndex;
	private String adcName;
	private Integer type;
	private Integer status;
	private String title;
	private String event;
	private int objectType;
	private String object;
	private int relativeObjectType;
	private String relativeObject;
	private Integer actionSyslog;
	private Integer actionSnmptrap;
	private Integer actionSMS;
	private boolean isNew; // 경보가 새 로그인가

	public Date getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(Date alertTime) {
		this.alertTime = alertTime;
	}

	public Date getOccurTime() {
		return occurTime;
	}

	public Integer getActionSMS() {
		return actionSMS;
	}

	public void setActionSMS(Integer actionSMS) {
		this.actionSMS = actionSMS;
	}

	public void setOccurTime(Date occurTime) {
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

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getObjectType() {
		return objectType;
	}

	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public int getRelativeObjectType() {
		return relativeObjectType;
	}

	public void setRelativeObjectType(int relativeObjectType) {
		this.relativeObjectType = relativeObjectType;
	}

	public String getRelativeObject() {
		return relativeObject;
	}

	public void setRelativeObject(String relativeObject) {
		this.relativeObject = relativeObject;
	}

	@Override
	public String toString() {
		return "OBDtoAdcAlertLog [occurTime=" + occurTime + ", alertTime=" + alertTime + ", adcIndex=" + adcIndex
				+ ", adcName=" + adcName + ", type=" + type + ", status=" + status + ", title=" + title + ", event="
				+ event + ", objectType=" + objectType + ", object=" + object + ", relativeObjectType="
				+ relativeObjectType + ", relativeObject=" + relativeObject + ", actionSyslog=" + actionSyslog
				+ ", actionSnmptrap=" + actionSnmptrap + ", isNew=" + isNew + "]";
	}
}