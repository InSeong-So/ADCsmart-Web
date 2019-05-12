package kr.openbase.adcsmart.service.dto;

public class OBDtoFaultStatusCount
{
	private Integer fault;
	private Integer faultSolved;
	private Integer faultUnsolved;
	private Integer warn;
	
	public OBDtoFaultStatusCount()
	{
		fault = 0;
		faultSolved = 0;
		faultUnsolved = 0;
		warn = 0;
	}

	public Integer getFault()
	{
		return fault;
	}

	public void setFault(Integer fault)
	{
		this.fault = fault;
	}

	public Integer getFaultSolved()
	{
		return faultSolved;
	}

	public void setFaultSolved(Integer faultSolved)
	{
		this.faultSolved = faultSolved;
	}

	public Integer getFaultUnsolved()
	{
		return faultUnsolved;
	}

	public void setFaultUnsolved(Integer faultUnsolved)
	{
		this.faultUnsolved = faultUnsolved;
	}

	public Integer getWarn()
	{
		return warn;
	}

	public void setWarn(Integer warn)
	{
		this.warn = warn;
	}

	@Override
	public String toString()
	{
		return "OBDtoFaultStatusCount [fault=" + fault + ", faultSolved="
				+ faultSolved + ", faultUnsolved=" + faultUnsolved + ", warn="
				+ warn + "]";
	}

}
