package kr.openbase.adcsmart.web.report.admin;

import java.util.List;

import kr.openbase.adcsmart.web.report.impl.SystemReportDto;

public class SystemAdminSummaryDto 
{
	private AdcInfoDto adc;
	private SystemStatusDto systemStatus;
	private List<PortStatusDto> portStatuses;
	private SystemReportDto systemRerpotDto;
	
	private SystemAdminSummaryDtoTextHdr textHdr;
	
	public AdcInfoDto getAdc() 
	{
		return adc;
	}
	public SystemAdminSummaryDtoTextHdr getTextHdr()
	{
		return textHdr;
	}
	public void setTextHdr(SystemAdminSummaryDtoTextHdr textHdr)
	{
		this.textHdr = textHdr;
	}
	public void setAdc(AdcInfoDto adc) 
	{
		this.adc = adc;
	}
	public SystemStatusDto getSystemStatus() 
	{
		return systemStatus;
	}
	public void setSystemStatus(SystemStatusDto systemStatus) 
	{
		this.systemStatus = systemStatus;
	}
	public List<PortStatusDto> getPortStatuses() 
	{
		return portStatuses;
	}
	public void setPortStatuses(List<PortStatusDto> portStatuses) 
	{
		this.portStatuses = portStatuses;
	}
	@Override
	public String toString() 
	{
		return "SystemAdminSummaryDto [adc=" + adc + ", systemStatus=" + systemStatus + ", portStatuses="
				+ portStatuses + "]";
	}
    public SystemReportDto getSystemRerpotDto()
    {
        return systemRerpotDto;
    }
    public void setSystemRerpotDto(SystemReportDto systemRerpotDto)
    {
        this.systemRerpotDto = systemRerpotDto;
    }
	
	
}
