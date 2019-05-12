package kr.openbase.adcsmart.web.facade.dto;

public class LicenseInfoDto
{
	private String version;
	private String model;
	private String serial;
	private String maxAdcNum;
	private String maxVSnum;
	private String maxUserNum;
	private String period;
	private String macAddress;
	private String issueDate;
	private String state;
	public String getVersion()
	{
		return version;
	}
	public void setVersion(String version)
	{
		this.version = version;
	}
	public String getModel()
	{
		return model;
	}
	public void setModel(String model)
	{
		this.model = model;
	}
	public String getSerial()
	{
		return serial;
	}
	public void setSerial(String serial)
	{
		String serialString = serial.substring(0, 39);// 39 == max length of serial string// TODO. for temp
		this.serial = serialString;
	}
	public String getMaxAdcNum()
	{
		return maxAdcNum;
	}
	public void setMaxAdcNum(String maxAdcNum)
	{
		this.maxAdcNum = maxAdcNum;
	}
	public String getMaxVSnum()
	{
		return maxVSnum;
	}
	public void setMaxVSnum(String maxVSnum)
	{
		this.maxVSnum = maxVSnum;
	}
	public String getMaxUserNum()
	{
		return maxUserNum;
	}
	public void setMaxUserNum(String maxUserNum)
	{
		this.maxUserNum = maxUserNum;
	}
	public String getPeriod()
	{
		return period;
	}
	public void setPeriod(String period)
	{
		this.period = period;
	}
	public String getMacAddress()
	{
		return macAddress;
	}
	public void setMacAddress(String macAddress)
	{
		this.macAddress = macAddress;
	}
	public String getIssueDate()
	{
		return issueDate;
	}
	public void setIssueDate(String issueDate)
	{
		this.issueDate = issueDate;
	}
	public String getState()
	{
		return state;
	}
	public void setState(String state)
	{
		this.state = state;
	}
	@Override
	public String toString()
	{
		return "LicenseInfoDto [version=" + version + ", model=" + model + ", serial=" + serial + ", maxAdcNum=" + maxAdcNum + ", maxVSnum=" + maxVSnum + ", maxUserNum=" + maxUserNum + ", period=" + period + ", macAddress=" + macAddress + ", issueDate=" + issueDate + ", state=" + state + "]";
	}
		
//	public static OBDtoLicenseInfo toLicenseInfoDto(LicenseInfoDto lcsinfo) {
//	OBDtoLicenseInfo lcsinfoFromSvc = new OBDtoLicenseInfo();		
//	lcsinfoFromSvc.setVersion(lcsinfo.getVersion());
//	lcsinfoFromSvc.setModel(lcsinfo.getModel());
//	lcsinfoFromSvc.setSerial(lcsinfo.getSerial());
//	lcsinfoFromSvc.setMaxAdcNum(lcsinfo.getMaxAdcNum());
//	lcsinfoFromSvc.setMaxVSnum(lcsinfo.getMaxVSnum());
//	lcsinfoFromSvc.setMaxUserNum(lcsinfo.getMaxUserNum());
//	lcsinfoFromSvc.setPeriod(lcsinfo.getPeriod());
//	lcsinfoFromSvc.setMacAddress(lcsinfo.getMacAddress());
//	lcsinfoFromSvc.setIssueDate(lcsinfo.getIssueDate());
//
//	return lcsinfoFromSvc;
//}	
}
