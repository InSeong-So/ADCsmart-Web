package kr.openbase.adcsmart.web.report.admin;

import kr.openbase.adcsmart.service.dto.report.OBDtoRptAdcInfo;
import kr.openbase.adcsmart.web.report.impl.ReportConstants;

public class AdcInfoDto 
{
	private String ip;
	private String name;
	private String hostName;
	private String model;
	private String osVersion;
	private String licenseInfo;
	private String serialNo;
	private String macAddress;
	private String activeOrStandby;
	private String daysAlive;
	
	public static AdcInfoDto toAdcDto(OBDtoRptAdcInfo adcFromSvc) 
	{
		if (adcFromSvc == null)
			return null;
		
		AdcInfoDto adc = new AdcInfoDto();
//		adc.setIp(adcFromSvc.getiPAddress());
//		adc.setName(adcFromSvc.getName());
//		adc.setHostName(adcFromSvc.getHostName());
//		adc.setModel(adcFromSvc.getModel());
//		adc.setOsVersion(adcFromSvc.getOsVersion());
//		adc.setLicenseInfo(adcFromSvc.getLicense());
//		adc.setSerialNo(adcFromSvc.getSerialNum());
//		adc.setMacAddress(adcFromSvc.getMacAddress());
//		adc.setActiveOrStandby(adcFromSvc.getActiveStandby());
//		adc.setDaysAlive(adcFromSvc.getRunTime());
		/** report에서 값이 null 일때 N/A찍히지 않도록 처리  **/		
        if(adcFromSvc.getiPAddress() == null)
        {
             adc.setIp(ReportConstants.NULL_TO_STRING);
        }else
        {
             adc.setIp(adcFromSvc.getiPAddress());
        }
        if(adcFromSvc.getName() == null)
        {
             adc.setName(ReportConstants.NULL_TO_STRING);
        }else
        {
             adc.setName(adcFromSvc.getName());
        }
        if(adcFromSvc.getHostName() == null)
        {
             adc.setHostName(ReportConstants.NULL_TO_STRING);
        }else
        {
             adc.setHostName(adcFromSvc.getHostName());
        }
        if(adcFromSvc.getModel() == null)
        {
             adc.setModel(ReportConstants.NULL_TO_STRING);
        }else
        {
             adc.setModel(adcFromSvc.getModel());
        }
        if(adcFromSvc.getOsVersion() == null)
        {
             adc.setOsVersion(ReportConstants.NULL_TO_STRING);
        }else
        {
             adc.setOsVersion(adcFromSvc.getOsVersion());
        }
        if(adcFromSvc.getLicense() == null)
        {
             adc.setLicenseInfo(ReportConstants.NULL_TO_STRING);
        }else
        {
             adc.setLicenseInfo(adcFromSvc.getLicense());
        }
        if(adcFromSvc.getSerialNum() == null)
        {
             adc.setSerialNo(ReportConstants.NULL_TO_STRING);
        }else
        {
             adc.setSerialNo(adcFromSvc.getSerialNum());
        }
        if(adcFromSvc.getMacAddress() == null)
        {
             adc.setMacAddress(ReportConstants.NULL_TO_STRING);
        }else
        {
             adc.setMacAddress(adcFromSvc.getMacAddress());
        }
        if(adcFromSvc.getActiveStandby() == null)
        {
             adc.setActiveOrStandby(ReportConstants.NULL_TO_STRING);
        }else
        {
             adc.setActiveOrStandby(adcFromSvc.getActiveStandby());
        }
       	if(adcFromSvc.getRunTime() == null)
        {
             adc.setDaysAlive(ReportConstants.NULL_TO_STRING);
        }else
        {
             adc.setDaysAlive(adcFromSvc.getRunTime());
        }
                   		
		return adc;
	}
	
	public String getIp() 
	{
		return ip;
	}
	public void setIp(String ip) 
	{
		this.ip = ip;
	}
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public String getHostName() 
	{
		return hostName;
	}
	public void setHostName(String hostName) 
	{
		this.hostName = hostName;
	}
	public String getModel() 
	{
		return model;
	}
	public void setModel(String model) 
	{
		this.model = model;
	}
	public String getOsVersion() 
	{
		return osVersion;
	}
	public void setOsVersion(String osVersion) 
	{
		this.osVersion = osVersion;
	}
	public String getLicenseInfo() 
	{
		return licenseInfo;
	}
	public void setLicenseInfo(String licenseInfo) 
	{
		this.licenseInfo = licenseInfo;
	}
	public String getSerialNo() 
	{
		return serialNo;
	}
	public void setSerialNo(String serialNo) 
	{
		this.serialNo = serialNo;
	}
	public String getMacAddress() 
	{
		return macAddress;
	}
	public void setMacAddress(String macAddress) 
	{
		this.macAddress = macAddress;
	}
	public String getActiveOrStandby() 
	{
		return activeOrStandby;
	}
	public void setActiveOrStandby(String activeOrStandby) 
	{
		this.activeOrStandby = activeOrStandby;
	}
	
	public String getDaysAlive() 
	{
		return daysAlive;
	}
	public void setDaysAlive(String daysAlive) 
	{
		this.daysAlive = daysAlive;
	}
	@Override
	public String toString() 
	{
		return "AdcDto [ip=" + ip + ", name=" + name + ", hostName=" + hostName + ", model=" + model + ", osVersion="
				+ osVersion + ", licenseInfo=" + licenseInfo + ", serialNo=" + serialNo + ", macAddress=" + macAddress
				+ ", activeOrStandby=" + activeOrStandby + ", daysAlive=" + daysAlive + "]";
	}
	
	
}
