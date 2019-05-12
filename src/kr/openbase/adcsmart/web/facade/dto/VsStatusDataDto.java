package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservStatus;

public class VsStatusDataDto {
	
	private Date occurredTime;
	private Integer vsAvailableCount;
	private Integer vsUnavailableCount;
	private Integer vsDisableCount;
	
	public Date getOccurredTime() {
		return occurredTime;
	}
	public void setOccurredTime(Date occurredTime) {
		this.occurredTime = occurredTime;
	}
	public Integer getVsAvailableCount() {
		return vsAvailableCount;
	}
	public void setVsAvailableCount(Integer vsAvailableCount) {
		this.vsAvailableCount = vsAvailableCount;
	}
	public Integer getVsUnavailableCount() {
		return vsUnavailableCount;
	}
	public void setVsUnavailableCount(Integer vsUnavailableCount) {
		this.vsUnavailableCount = vsUnavailableCount;
	}
	public Integer getVsDisableCount() {
		return vsDisableCount;
	}
	public void setVsDisableCount(Integer vsDisableCount) {
		this.vsDisableCount = vsDisableCount;
	}
	
	@Override
	public String toString() {
		return "VsStatusDataDto [occurredTime=" + occurredTime
				+ ", vsAvailableCount=" + vsAvailableCount
				+ ", vsUnavailableCount=" + vsUnavailableCount
				+ ", vsDisableCount=" + vsDisableCount + "]";
	}
	
	public static VsStatusDataDto getVsStatusData(
			OBDtoDashboardSdsVservStatus obDtoDashboardSdsVservStatus) {
		VsStatusDataDto vsStatusData = new VsStatusDataDto();
		vsStatusData.setOccurredTime(obDtoDashboardSdsVservStatus.getOccurTime());
		vsStatusData.setVsAvailableCount(obDtoDashboardSdsVservStatus.getVsAvail());
		vsStatusData.setVsUnavailableCount(obDtoDashboardSdsVservStatus.getVsUnavail());
		vsStatusData.setVsDisableCount(obDtoDashboardSdsVservStatus.getVsDisable());
		return vsStatusData;
	}

}
