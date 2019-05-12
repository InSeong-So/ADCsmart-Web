package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory2;
import kr.openbase.adcsmart.service.utility.OBDefine;

public class AdcHistoryDto {
	private Date lastChangeTime;
	private String virtualSvrIndex;
	private String virtualSvrName;
	private String virtualSvrIp;
	private String virtualSvrStatus;
	private String changeSummary;
	private String accountName;
	private Boolean isVirtualSvrDeleted;
	private long logSeq;
	
	public static AdcHistoryDto toAdcHistoryDto(OBDtoAdcConfigHistory historyFromSvc) {
		AdcHistoryDto history = new AdcHistoryDto();
		history.setLogSeq(historyFromSvc.getLogSeq());
		history.setLastChangeTime(historyFromSvc.getOccurTime());
		history.setVirtualSvrIndex(historyFromSvc.getVsIndex());
		history.setVirtualSvrName(historyFromSvc.getVsName());
		history.setVirtualSvrIp(historyFromSvc.getVsIp());
		history.setChangeSummary(historyFromSvc.getSummary());
		history.setAccountName(historyFromSvc.getAccountName());
		history.setIsVirtualSvrDeleted((historyFromSvc.getVsAlive() != null && historyFromSvc.getVsAlive() == OBDefine.OBJECT_ALIVE) ? false : true);
		
		return history;
	}
	
	public static List<AdcHistoryDto> toAdcHistoryDto(List<OBDtoAdcConfigHistory> historyListFromSvc) {
		List<AdcHistoryDto> historyList = new ArrayList<AdcHistoryDto>();
		if (historyListFromSvc != null) {
			for (OBDtoAdcConfigHistory e : historyListFromSvc)
				historyList.add(toAdcHistoryDto(e));
		}
		
		return historyList;
	}
	
	public static AdcHistoryDto toAdcHistoryDtoFromOBDtoAdcConfigHistory2(OBDtoAdcConfigHistory2 historyFromSvc) {
		AdcHistoryDto history = new AdcHistoryDto();
		history.setLogSeq(historyFromSvc.getLogSeq());
		history.setVirtualSvrIndex(historyFromSvc.getVirtualSvrIndex());
		history.setLastChangeTime(historyFromSvc.getOccurTime());
		history.setChangeSummary(historyFromSvc.getSummary());
		history.setAccountName(historyFromSvc.getAccountName());
		
		return history;
	}
	
	public static List<AdcHistoryDto> toAdcHistoryDtoFromOBDtoAdcConfigHistory2(List<OBDtoAdcConfigHistory2> historyListFromSvc) {
		List<AdcHistoryDto> historyList = new ArrayList<AdcHistoryDto>();
		if (historyListFromSvc != null) {
			for (OBDtoAdcConfigHistory2 e : historyListFromSvc)
				historyList.add(toAdcHistoryDtoFromOBDtoAdcConfigHistory2(e));
		}
		
		return historyList;
	}
	
	public long getLogSeq()
	{
		return logSeq;
	}

	public void setLogSeq(long logSeq)
	{
		this.logSeq = logSeq;
	}
	
	public Date getLastChangeTime() {
		return lastChangeTime;
	}

	public void setLastChangeTime(Date lastChangeTime) {
		this.lastChangeTime = lastChangeTime;
	}

	public String getVirtualSvrIndex() {
		return virtualSvrIndex;
	}

	public void setVirtualSvrIndex(String virtualSvrIndex) {
		this.virtualSvrIndex = virtualSvrIndex;
	}

	public String getVirtualSvrName() {
		return virtualSvrName;
	}

	public void setVirtualSvrName(String virtualSvrName) {
		this.virtualSvrName = virtualSvrName;
	}

	public String getVirtualSvrIp() {
		return virtualSvrIp;
	}

	public void setVirtualSvrIp(String virtualSvrIp) {
		this.virtualSvrIp = virtualSvrIp;
	}

	public String getVirtualSvrStatus() {
		return virtualSvrStatus;
	}

	public void setVirtualSvrStatus(String virtualSvrStatus) {
		this.virtualSvrStatus = virtualSvrStatus;
	}

	public String getChangeSummary() {
		return changeSummary;
	}

	public void setChangeSummary(String changeSummary) {
		this.changeSummary = changeSummary;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Boolean getIsVirtualSvrDeleted() {
		return isVirtualSvrDeleted;
	}

	public void setIsVirtualSvrDeleted(Boolean isVirtualSvrDeleted) {
		this.isVirtualSvrDeleted = isVirtualSvrDeleted;
	}

	@Override
	public String toString()
	{
		return "AdcHistoryDto [lastChangeTime=" + lastChangeTime
				+ ", virtualSvrIndex=" + virtualSvrIndex + ", virtualSvrName="
				+ virtualSvrName + ", virtualSvrIp=" + virtualSvrIp
				+ ", virtualSvrStatus=" + virtualSvrStatus + ", changeSummary="
				+ changeSummary + ", accountName=" + accountName
				+ ", isVirtualSvrDeleted=" + isVirtualSvrDeleted + ", logSeq="
				+ logSeq + "]";
	}
}
