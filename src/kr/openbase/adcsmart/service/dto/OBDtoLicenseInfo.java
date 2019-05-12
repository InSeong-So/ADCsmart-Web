package kr.openbase.adcsmart.service.dto;

public class OBDtoLicenseInfo
{
	private String version;
	private String model;
	private String serial;
	private Integer maxAdcNum;
	private Integer maxVSnum;
	private Integer maxUserNum;
	private String  period;//사용 기간.
	private String  macAddress;
	private String  issueDate;
	private Integer licStatus;//라이선스

	@Override
	public String toString()
	{
		return "OBDtoLicenseInfo [version=" + version + ", model=" + model
				+ ", serial=" + serial + ", maxAdcNum=" + maxAdcNum
				+ ", maxVSnum=" + maxVSnum + ", maxUserNum=" + maxUserNum
				+ ", period=" + period + ", macAddress=" + macAddress
				+ ", issueDate=" + issueDate + ", licStatus=" + licStatus + "]";
	}

	public Integer getLicStatus()
	{
		return licStatus;
	}

	public void setLicStatus(Integer licStatus)
	{
		this.licStatus = licStatus;
	}

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
		this.serial = serial;
	}
	public Integer getMaxAdcNum()
	{
		return maxAdcNum;
	}
	public void setMaxAdcNum(Integer maxAdcNum)
	{
		this.maxAdcNum = maxAdcNum;
	}
	public Integer getMaxVSnum()
	{
		return maxVSnum;
	}
	public void setMaxVSnum(Integer maxVSnum)
	{
		this.maxVSnum = maxVSnum;
	}
	public Integer getMaxUserNum()
	{
		return maxUserNum;
	}
	public void setMaxUserNum(Integer maxUserNum)
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
}
