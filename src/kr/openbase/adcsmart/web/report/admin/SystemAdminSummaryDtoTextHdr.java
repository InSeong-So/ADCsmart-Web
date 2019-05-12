package kr.openbase.adcsmart.web.report.admin;

public class SystemAdminSummaryDtoTextHdr
{
	private String title;// ADC 시스템 운영 요약 
	private String adcIP;// ADC IP
	private String adcName;// ADC 이름
	private String adcInfo;// ADC 정보
	private String hostName;// Host 이름 
	private String adcModel;// ADC 모델명
	private String osVersion;// OS 버전
	private String licenseInfo;// License 정보
	private String serialNo;// Serial 번호
	private String macAddr;// MAC 주소.
	private String activeStandby;// Active/Standby
	private String runningTime;// 운영시간.
	private String systemInfo;// 시스템 상태 정보 
	
	public String getSystemInfo()
	{
		return systemInfo;
	}
	public void setSystemInfo(String systemInfo)
	{
		this.systemInfo = systemInfo;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getAdcIP()
	{
		return adcIP;
	}
	public void setAdcIP(String adcIP)
	{
		this.adcIP = adcIP;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public String getAdcInfo()
	{
		return adcInfo;
	}
	public void setAdcInfo(String adcInfo)
	{
		this.adcInfo = adcInfo;
	}
	public String getHostName()
	{
		return hostName;
	}
	public void setHostName(String hostName)
	{
		this.hostName = hostName;
	}
	public String getAdcModel()
	{
		return adcModel;
	}
	public void setAdcModel(String adcModel)
	{
		this.adcModel = adcModel;
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
	public String getMacAddr()
	{
		return macAddr;
	}
	public void setMacAddr(String macAddr)
	{
		this.macAddr = macAddr;
	}
	public String getActiveStandby()
	{
		return activeStandby;
	}
	public void setActiveStandby(String activeStandby)
	{
		this.activeStandby = activeStandby;
	}
	public String getRunningTime()
	{
		return runningTime;
	}
	public void setRunningTime(String runningTime)
	{
		this.runningTime = runningTime;
	}
	@Override
	public String toString()
	{
		return "SystemAdminSummaryDtoTextHdr [title=" + title + ", adcIP=" + adcIP + ", adcName=" + adcName + ", adcInfo=" + adcInfo + ", hostName=" + hostName + ", adcModel=" + adcModel + ", osVersion=" + osVersion + ", licenseInfo=" + licenseInfo + ", serialNo=" + serialNo + ", macAddr=" + macAddr + ", activeStandby=" + activeStandby + ", runningTime=" + runningTime + "]";
	}
}
