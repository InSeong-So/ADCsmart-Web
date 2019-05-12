package kr.openbase.adcsmart.service.dto;

public class OBDtoAdcScope
{
	public static final  int	LEVEL_ALL	 =0;
	public static final  int	LEVEL_GROUP	 =1;
	public static final  int	LEVEL_ADC	 =2;

	private Integer level=0; //0: 전체, 1: 그룹, 2: 개별 adc들 pick
	private Integer index; //그룹 인덱스 또는 adcIndex. level에 맞게.
	
	public OBDtoAdcScope()
	{
	}
	
	public OBDtoAdcScope(Integer level, Integer index)
	{
		super();
		this.level = level;
		this.index = index;
	}

	@Override
	public String toString()
	{
		return "OBDtoAdcScope [level=" + level + ", index=" + index + "]";
	}
	public Integer getLevel()
	{
		return level;
	}
	public void setLevel(Integer level)
	{
		this.level = level;
	}
	public Integer getIndex()
	{
		return index;
	}
	public void setIndex(Integer index)
	{
		this.index = index;
	}
}
