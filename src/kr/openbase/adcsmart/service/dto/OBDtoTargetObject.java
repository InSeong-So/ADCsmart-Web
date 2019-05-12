package kr.openbase.adcsmart.service.dto;

public class OBDtoTargetObject
{
	//인덱스는 ADC range가 group일 때 group인덱스, single일 때 adc 인덱스이다. 만약을 위해 strIndex를 준비했다. 
	private int adcRange; //OBDefine.ADC_RANGE
	private Integer index=null;
	private String  strIndex=null;

	@Override
	public String toString()
	{
		return "OBDtoTargetObject [adcRange=" + adcRange + ", index=" + index
				+ ", strIndex=" + strIndex + "]";
	}
	public int getAdcRange()
	{
		return adcRange;
	}
	public void setAdcRange(int adcRange)
	{
		this.adcRange = adcRange;
	}
	public Integer getIndex()
	{
		return index;
	}
	public void setIndex(Integer index)
	{
		this.index = index;
	}
	public String getStrIndex()
	{
		return strIndex;
	}
	public void setStrIndex(String strIndex)
	{
		this.strIndex = strIndex;
	}
}
