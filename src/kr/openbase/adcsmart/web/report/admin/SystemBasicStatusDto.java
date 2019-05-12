package kr.openbase.adcsmart.web.report.admin;

import kr.openbase.adcsmart.service.dto.report.OBDtoRptSysInfoBasic;

public class SystemBasicStatusDto {
	private ResultDetailDto upTime;
	private ResultDetailDto lastApply;
	private ResultDetailDto cpu;
	private ResultDetailDto memory;
	private ResultDetailDto power;
	private ResultDetailDto fan;
	
	public static SystemBasicStatusDto toSystemBasicStatusDto(OBDtoRptSysInfoBasic statusFromSvc) {
		if (statusFromSvc == null)
			return null;
		
		SystemBasicStatusDto status = new SystemBasicStatusDto();
		status.setUpTime(ResultDetailDto.toResultDetailDto(statusFromSvc.getUpTime()));
		status.setLastApply(ResultDetailDto.toResultDetailDto(statusFromSvc.getLastApplyTime()));
		status.setCpu(ResultDetailDto.toResultDetailDto(statusFromSvc.getCpuInfo()));
		status.setMemory(ResultDetailDto.toResultDetailDto(statusFromSvc.getMemoryInfo()));
		status.setPower(ResultDetailDto.toResultDetailDto(statusFromSvc.getPowerInfo()));
		status.setFan(ResultDetailDto.toResultDetailDto(statusFromSvc.getFanInfo()));
		return status;
	}
	
	public ResultDetailDto getUpTime() {
		return upTime;
	}
	public void setUpTime(ResultDetailDto upTime) {
		this.upTime = upTime;
	}
	public ResultDetailDto getLastApply() {
		return lastApply;
	}
	public void setLastApply(ResultDetailDto lastApply) {
		this.lastApply = lastApply;
	}
	public ResultDetailDto getCpu() {
		return cpu;
	}
	public void setCpu(ResultDetailDto cpu) {
		this.cpu = cpu;
	}
	public ResultDetailDto getMemory() {
		return memory;
	}
	public void setMemory(ResultDetailDto memory) {
		this.memory = memory;
	}
	public ResultDetailDto getPower() {
		return power;
	}
	public void setPower(ResultDetailDto power) {
		this.power = power;
	}
	public ResultDetailDto getFan() {
		return fan;
	}
	public void setFan(ResultDetailDto fan) {
		this.fan = fan;
	}
	@Override
	public String toString() {
		return "SystemBasicStatusDto [upTime=" + upTime + ", lastApply=" + lastApply + ", cpu=" + cpu + ", memory="
				+ memory + ", power=" + power + ", fan=" + fan + "]";
	}
	
}
