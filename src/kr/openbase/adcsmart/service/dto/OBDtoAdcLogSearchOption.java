package kr.openbase.adcsmart.service.dto;

public class OBDtoAdcLogSearchOption
{
	// private ArrayList<String>  logType;
	// private ArrayList<String> logLevel;
	private Integer logType;	// 0 : all, 1: ADC, 2: ADCsmart
	private String logLevel;
	
	public Integer getLogType()
	{
		return logType;
	}
	public void setLogType(Integer logType)
	{
		this.logType = logType;
	}
	public String getLogLevel()
	{
		return logLevel;
	}
	public void setLogLevel(String logLevel)
	{
		this.logLevel = logLevel;
	}
	
	@Override
	public String toString()
	{
		return "OBDtoAdcLogSearchOption [logType=" + logType + ", logLevel=" + logLevel + "]";
	}
}