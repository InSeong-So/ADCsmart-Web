package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvNetwork;

public class SystemEnvNetworkDto {
	
	private Date   occurTime;
	private String ipAddress;
	private String netmask;
	private String gateway;
	private String hostName;

//	public static SystemEnvNetworkDto toSystemEnvNetworkDto(OBDtoSystemEnvNetwork envNetworkFromSvc) throws Exception {
//		SystemEnvNetworkDto enetwork = new SystemEnvNetworkDto();
//		enetwork.setIpAddress(envNetworkFromSvc.getIpAddress());
//		enetwork.setGateway(envNetworkFromSvc.getGateway());
//		enetwork.setHostName(envNetworkFromSvc.getHostName());
//		
//		try {
//			OBEnvManagement envNetworkSvc = new OBEnvManagementImpl();
//			enetwork.setIpAddress(envNetworkSvc.getSystemConfig().getNetworkInfo().getIpAddress());		
//		} catch (OBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			throw e;
//		}
//		
//		return enetwork;
//	}
	public static OBDtoSystemEnvNetwork toOBDtoSystemEnvNetwork(SystemEnvNetworkDto enetwork) {
		OBDtoSystemEnvNetwork envNetworkFromSvc = new OBDtoSystemEnvNetwork();
		envNetworkFromSvc.setIpAddress(enetwork.getIpAddress());
		envNetworkFromSvc.setGateway(enetwork.getGateway());
		envNetworkFromSvc.setHostName(enetwork.getHostName());
		envNetworkFromSvc.setNetmask(enetwork.getNetmask());

		return envNetworkFromSvc;
	}
	
	public Date getOccurTime()
	{
		return occurTime;
	}

	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}

	public String getIpAddress()
	{
		return ipAddress;
	}

	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	public String getNetmask()
	{
		return netmask;
	}

	public void setNetmask(String netmask)
	{
		this.netmask = netmask;
	}

	public String getGateway()
	{
		return gateway;
	}

	public void setGateway(String gateway)
	{
		this.gateway = gateway;
	}

	public String getHostName()
	{
		return hostName;
	}

	public void setHostName(String hostName)
	{
		this.hostName = hostName;
	}

	@Override
	public String toString() {
		return "SystemEnvNetworkDto [occurTime=" + occurTime + ", ipAddress="
				+ ipAddress + ", netmask=" + netmask + ", gateway=" + gateway
				+ ", hostName=" + hostName + "]";
	}	
	
}
