package kr.openbase.adcsmart.web.report.admin;

import kr.openbase.adcsmart.service.dto.report.OBDtoRptSysInfoL4;

public class SystemL4StatusDto 
{
	private ResultDetailDto poolMember;
	private ResultDetailDto virtualSvr;
	private ResultDetailDto connection;
	private ResultDetailDto directFunc;
	
	public static SystemL4StatusDto toSystemL4StatusDto(OBDtoRptSysInfoL4 statusFromSvc) 
	{
		if (statusFromSvc == null)
			return null;
		
		SystemL4StatusDto status = new SystemL4StatusDto();
		status.setPoolMember(ResultDetailDto.toResultDetailDto(statusFromSvc.getPoolMemberStatus()));
		status.setVirtualSvr(ResultDetailDto.toResultDetailDto(statusFromSvc.getVsStatus()));
		status.setConnection(ResultDetailDto.toResultDetailDto(statusFromSvc.getConntatus()));
		status.setDirectFunc(ResultDetailDto.toResultDetailDto(statusFromSvc.getDirectStatus()));
		return status;
	}
	
	public ResultDetailDto getPoolMember()
	{
		return poolMember;
	}
	public void setPoolMember(ResultDetailDto poolMember)
	{
		this.poolMember = poolMember;
	}
	public ResultDetailDto getVirtualSvr() 
	{
		return virtualSvr;
	}
	public void setVirtualSvr(ResultDetailDto virtualSvr) 
	{
		this.virtualSvr = virtualSvr;
	}
	public ResultDetailDto getConnection()
	{
		return connection;
	}
	public void setConnection(ResultDetailDto connection) 
	{
		this.connection = connection;
	}
	public ResultDetailDto getDirectFunc()
	{
		return directFunc;
	}
	public void setDirectFunc(ResultDetailDto directFunc)
	{
		this.directFunc = directFunc;
	}
	@Override
	public String toString() 
	{
		return "SystemL4StatusDto [poolMember=" + poolMember + ", virtualSvr=" + virtualSvr + ", connection="
				+ connection + ", directFunc=" + directFunc + "]";
	}
	
}
