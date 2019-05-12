package kr.openbase.adcsmart.web.facade.dto;

import kr.openbase.adcsmart.service.dto.OBDtoAdcLogFilter;

public class AdcLogFilterDto
{
	private Integer index;
	private String 	regPattern;		//실제사용하는 패턴.
	private String	userPattern;	//사용자 입력 패턴 
	private Integer	type;			//0: 부분일치, 1: 전체일치.
	
//	private List<String>	userPatterns;	//사용자 입력 패턴 
//	private List<Integer>	types;			//0: 부분일치, 

	public String toString() {
		return "AdcLogFilterDto [index=" + index + ", regPattern=" 
				+ regPattern + ", userPattern=" + userPattern + ", type="
				+ type + "]";
	}
	
	//AdcLogFilterDto [index=null, regPattern=null, userPattern=login from host, ssl_acc, aaaaa, test, test2, type=null]
	public static OBDtoAdcLogFilter toAdcLogFilter(AdcLogFilterDto logFilter)
	{
		OBDtoAdcLogFilter logFilterFromSvc = new OBDtoAdcLogFilter();
//		logFilterFromSvc.setIndex(lfilter.getIndex());
		logFilterFromSvc.setUserPattern(logFilter.getUserPattern());
		logFilterFromSvc.setType(logFilter.getType());
		
		
//		logFilterFromSvc.setUserPattern("login filter");
//		logFilterFromSvc.setType(1);
//		logFilterFromSvc.setUserPattern("ssl_acctest");
//		logFilterFromSvc.setType(0);
		

		return logFilterFromSvc;
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

//	public List<String> getUserPatterns()
//	{
//		return userPatterns;
//	}
//
//	public void setUserPatterns(List<String> userPatterns)
//	{
//		this.userPatterns = userPatterns;
//	}
//
//	public List<Integer> getTypes()
//	{
//		return types;
//	}
//
//	public void setTypes(List<Integer> types)
//	{
//		this.types = types;
//	}
	

}