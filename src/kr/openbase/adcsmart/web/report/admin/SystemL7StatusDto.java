package kr.openbase.adcsmart.web.report.admin;

import kr.openbase.adcsmart.service.dto.report.OBDtoRptSysInfoL7;

public class SystemL7StatusDto {
	private ResultDetailDto iRule;
	private ResultDetailDto oneConnect;
	private ResultDetailDto ramCache;
	private ResultDetailDto compression;
	private ResultDetailDto sslAccel;
	
	public static SystemL7StatusDto toSystemL7StatusDto(OBDtoRptSysInfoL7 statusFromSvc) {
		if (statusFromSvc == null)
			return null;
		
		SystemL7StatusDto status = new SystemL7StatusDto();
		status.setiRule(ResultDetailDto.toResultDetailDto(statusFromSvc.getIruleStatus()));
		status.setOneConnect(ResultDetailDto.toResultDetailDto(statusFromSvc.getOneConnStatus()));
		status.setRamCache(ResultDetailDto.toResultDetailDto(statusFromSvc.getRamCacheStatus()));
		status.setCompression(ResultDetailDto.toResultDetailDto(statusFromSvc.getCompStatus()));
		status.setSslAccel(ResultDetailDto.toResultDetailDto(statusFromSvc.getSslStatus()));
		return status;
	}
	
	public ResultDetailDto getiRule() {
		return iRule;
	}
	public void setiRule(ResultDetailDto iRule) {
		this.iRule = iRule;
	}
	public ResultDetailDto getOneConnect() {
		return oneConnect;
	}
	public void setOneConnect(ResultDetailDto oneConnect) {
		this.oneConnect = oneConnect;
	}
	public ResultDetailDto getRamCache() {
		return ramCache;
	}
	public void setRamCache(ResultDetailDto ramCache) {
		this.ramCache = ramCache;
	}
	public ResultDetailDto getCompression() {
		return compression;
	}
	public void setCompression(ResultDetailDto compression) {
		this.compression = compression;
	}
	public ResultDetailDto getSslAccel() {
		return sslAccel;
	}
	public void setSslAccel(ResultDetailDto sslAccel) {
		this.sslAccel = sslAccel;
	}
	@Override
	public String toString() {
		return "SystemL7StatusDto [iRule=" + iRule + ", oneConnect=" + oneConnect + ", ramCache=" + ramCache
				+ ", compression=" + compression + ", sslAccel=" + sslAccel + "]";
	}
	
}
