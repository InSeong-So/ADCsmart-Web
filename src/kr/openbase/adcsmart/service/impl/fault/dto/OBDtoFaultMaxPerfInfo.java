package kr.openbase.adcsmart.service.impl.fault.dto;

public class OBDtoFaultMaxPerfInfo
{
	private Integer adcIndex;
	private String model;
	private String swVersion;
	private Long	maxThroughput;
	private Long 	maxConnection;
	@Override
	public String toString()
	{
		return String.format("OBDtoFaultMaxPerfInfo [adcIndex=%s, model=%s, swVersion=%s, maxThroughput=%s, maxConnection=%s]", adcIndex, model, swVersion, maxThroughput, maxConnection);
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getModel()
	{
		return model;
	}
	public void setModel(String model)
	{
		this.model = model;
	}
	public String getSwVersion()
	{
		return swVersion;
	}
	public void setSwVersion(String swVersion)
	{
		this.swVersion = swVersion;
	}
	public Long getMaxThroughput()
	{
		return maxThroughput;
	}
	public void setMaxThroughput(Long maxThroughput)
	{
		this.maxThroughput = maxThroughput;
	}
	public Long getMaxConnection()
	{
		return maxConnection;
	}
	public void setMaxConnection(Long maxConnection)
	{
		this.maxConnection = maxConnection;
	}
}
