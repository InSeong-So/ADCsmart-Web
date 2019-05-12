package kr.openbase.adcsmart.web.report.admin;

import kr.openbase.adcsmart.service.dto.report.OBDtoRptSysInfoL3;

public class SystemL3StatusDto {
	private ResultDetailDto interfaceInfo;
	private ResultDetailDto gateway;
	
	public static SystemL3StatusDto toSystemL3StatusDto(OBDtoRptSysInfoL3 statusFromSvc) {
		if (statusFromSvc == null)
			return null;
		
		SystemL3StatusDto status = new SystemL3StatusDto();
		status.setInterfaceInfo(ResultDetailDto.toResultDetailDto(statusFromSvc.getInterfaceInfo()));
		status.setGateway(ResultDetailDto.toResultDetailDto(statusFromSvc.getGatewayInfo()));
		return status;
	}
	
	public ResultDetailDto getInterfaceInfo() {
		return interfaceInfo;
	}
	public void setInterfaceInfo(ResultDetailDto interfaceInfo) {
		this.interfaceInfo = interfaceInfo;
	}
	public ResultDetailDto getGateway() {
		return gateway;
	}
	public void setGateway(ResultDetailDto gateway) {
		this.gateway = gateway;
	}
	@Override
	public String toString() {
		return "SystemL3StatusDto [interfaceInfo=" + interfaceInfo + ", gateway=" + gateway + "]";
	}
	
}
