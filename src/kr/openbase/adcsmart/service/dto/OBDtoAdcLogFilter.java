package kr.openbase.adcsmart.service.dto;

public class OBDtoAdcLogFilter
{
	private Integer index;
	private String  regPattern;// 실제사용하는 패턴.
	private String  userPattern;//사용자 입력 패턴 
	private Integer type; //0: 부분일치, 1: 전체일치.	
	@Override
	public String toString()
	{
		return "OBDtoAdcLogFilter [index=" + index + ", regPattern="
				+ regPattern + ", userPattern=" + userPattern + ", type="
				+ type + "]";
	}
	public Integer getIndex()
	{
		return index;
	}
	public void setIndex(Integer index)
	{
		this.index = index;
	}
	public String getRegPattern()
	{
		return regPattern;
	}
	public void setRegPattern(String regPattern)
	{
		this.regPattern = regPattern;
	}
	public String getUserPattern()
	{
		return userPattern;
	}
	public void setUserPattern(String userPattern)
	{
		this.userPattern = userPattern;
	}
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
	}
}
