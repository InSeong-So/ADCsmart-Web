package kr.openbase.adcsmart.web.facade.dto.adcmon;

public class OBDtoAdcmonDataObj
{
	private String	value=""; //현재 데이터.
	private Long	diff=0L;

	@Override
	public String toString()
	{
		return "OBDtoAdcmonDataObj [value=" + value + ", diff=" + diff + "]";
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}
	
	public Long getDiff()
	{
		return diff;
	}

	public void setDiff(Long diff)
	{
		this.diff = diff;
	}	
 }