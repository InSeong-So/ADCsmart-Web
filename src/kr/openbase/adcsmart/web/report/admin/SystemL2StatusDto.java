package kr.openbase.adcsmart.web.report.admin;

import java.util.List;

import kr.openbase.adcsmart.service.dto.report.OBDtoRptSysInfoL2;

public class SystemL2StatusDto {
	private ResultDetailDto linkUp;
	private ResultDetailDto port;
	private ResultDetailDto vlan;
	private ResultDetailDto stp;
	private ResultDetailDto trunk;
	private List<String> portNames;
	
	public static SystemL2StatusDto toSystemL2StatusDto(OBDtoRptSysInfoL2 statusFromSvc) {
		if (statusFromSvc == null)
			return null;
		
		SystemL2StatusDto status = new SystemL2StatusDto();
		status.setLinkUp(ResultDetailDto.toResultDetailDto(statusFromSvc.getLinkup()));
		status.setPort(ResultDetailDto.toResultDetailDto(statusFromSvc.getPortStatus()));
		status.setVlan(ResultDetailDto.toResultDetailDto(statusFromSvc.getVlanInfo()));
		status.setStp(ResultDetailDto.toResultDetailDto(statusFromSvc.getStpInfo()));
		status.setTrunk(ResultDetailDto.toResultDetailDto(statusFromSvc.getTrunkInfo()));
		status.setPortNames(statusFromSvc.getPortNameList());
		return status;
	}
	
	public ResultDetailDto getLinkUp() {
		return linkUp;
	}
	public void setLinkUp(ResultDetailDto linkUp) {
		this.linkUp = linkUp;
	}
	public ResultDetailDto getPort() {
		return port;
	}
	public void setPort(ResultDetailDto port) {
		this.port = port;
	}
	public ResultDetailDto getVlan() {
		return vlan;
	}
	public void setVlan(ResultDetailDto vlan) {
		this.vlan = vlan;
	}
	public ResultDetailDto getStp() {
		return stp;
	}
	public void setStp(ResultDetailDto stp) {
		this.stp = stp;
	}
	public ResultDetailDto getTrunk() {
		return trunk;
	}
	public void setTrunk(ResultDetailDto trunk) {
		this.trunk = trunk;
	}
	public List<String> getPortNames() {
		return portNames;
	}
	public void setPortNames(List<String> portNames) {
		this.portNames = portNames;
	}
	@Override
	public String toString() {
		return "SystemL2StatusDto [linkUp=" + linkUp + ", port=" + port + ", vlan=" + vlan + ", stp=" + stp
				+ ", trunk=" + trunk + "]";
	}
}
