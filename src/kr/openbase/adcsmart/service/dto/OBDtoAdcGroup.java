package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

public class OBDtoAdcGroup
{
	private Integer index;
	private String name;
	private String description;
	private ArrayList<OBDtoAdcInfo> adcList;
	
	private Integer adcCount;	//group내 ADC 전체 개수
	private Integer adcUnavailCount;//group내 ADC중 비정상(연결안됨) 개수
	
	@Override
	public String toString()
	{
		return "OBDtoAdcGroup [index=" + index + ", name=" + name
				+ ", description=" + description + ", adcList=" + adcList
				+ ", adcCount=" + adcCount + ", adcUnavailCount="
				+ adcUnavailCount + "]";
	}
	public Integer getIndex()
	{
		return this.index;
	}
	public Integer getAdcCount()
	{
		return adcCount;
	}
	public void setAdcCount(Integer adcCount)
	{
		this.adcCount = adcCount;
	}
	public Integer getAdcUnavailCount()
	{
		return adcUnavailCount;
	}
	public void setAdcUnavailCount(Integer adcUnavailCount)
	{
		this.adcUnavailCount = adcUnavailCount;
	}
	public void setIndex(Integer index)
	{
		this.index = index;
	}
	
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}	
	
	public String getDescription()
	{
		return this.description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public ArrayList<OBDtoAdcInfo> getAdcList()
	{
		return adcList;
	}
	public void setAdcList(ArrayList<OBDtoAdcInfo> adcList)
	{
		this.adcList = adcList;
	}
	
}
