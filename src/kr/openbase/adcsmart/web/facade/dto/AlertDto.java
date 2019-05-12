package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAlert;

public class AlertDto {
	private Long index;
	private String title;
	private String occurTime;
	private String adcName;
	private String adcIp;
	private String virtualSvrName;
	private String virtualSvrIp;
	private String memberIp;
	private String linkNo;
	private String message;
	private Integer remainingAlertCount;

	public static AlertDto toAlertDto(OBDtoAlert alertFromSvc) {
		if (alertFromSvc == null)
			return null;
		
		AlertDto alert = new AlertDto();
//		alert.setIndex(alertFromSvc.getAlertIndex());
//		alert.setTitle(alertFromSvc.getTitle());
//		alert.setOccurTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(alertFromSvc.getOccurTime()));
//		alert.setAdcName(alertFromSvc.getAdcName());
//		alert.setVirtualSvrName(alertFromSvc.getVirtualServerName());
//		alert.setAdcIp(alertFromSvc.getAdcIp());
//		alert.setVirtualSvrIp(alertFromSvc.getVirtualServerIp());
//		alert.setMemberIp(alertFromSvc.getPoolMember());
//		alert.setLinkNo(alertFromSvc.getBrokenLink());
//		alert.setMessage(alertFromSvc.getMessage());
		alert.setRemainingAlertCount(alertFromSvc.getAlertCount());
		return alert;
	}
	
	public static List<AlertDto> toReportDto(List<OBDtoAlert> alertsFromSvc) {
		List<AlertDto> alerts = new ArrayList<AlertDto>();
		if (alertsFromSvc != null) {
			for (OBDtoAlert e : alertsFromSvc)
				alerts.add(toAlertDto(e));
		}
		
		return alerts;
	}
	
	public Long getIndex() {
		return index;
	}
	public void setIndex(Long index) {
		this.index = index;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(String occurTime)
	{
		this.occurTime = occurTime;
	}
	public String getAdcName() {
		return adcName;
	}
	public void setAdcName(String adcName) {
		this.adcName = adcName;
	}
	public String getAdcIp()
	{
		return adcIp;
	}
	public void setAdcIp(String adcIp)
	{
		this.adcIp = adcIp;
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
	public String getMemberIp() {
		return memberIp;
	}
	public void setMemberIp(String memberIp) {
		this.memberIp = memberIp;
	}
	public String getLinkNo()
	{
		return linkNo;
	}
	public void setLinkNo(String linkNo)
	{
		this.linkNo = linkNo;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Integer getRemainingAlertCount() {
		return remainingAlertCount;
	}
	public void setRemainingAlertCount(Integer remainingAlertCount) {
		this.remainingAlertCount = remainingAlertCount;
	}
	@Override
	public String toString()
	{
		return "AlertDto [index=" + index + ", title=" + title + ", occurTime=" + occurTime 
		+ ", adcName=" + adcName + ", adcIp=" + adcIp + ", virtualSvrName=" + virtualSvrName 
		+ ", virtualSvrIp=" + virtualSvrIp + ", memberIp=" + memberIp + ", linkNo=" + linkNo 
		+ ", message=" + message + ", remainingAlertCount=" + remainingAlertCount + "]";
	}
}
