package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.impl.dto.OBDtoConnectionDataObj;

public class OBDtoMultiDataObj
{
	private Integer 			index;
	private String				name;
	private ArrayList<OBDtoConnectionDataObj> data;
	@Override
	public String toString()
	{
		return "OBDtoMultiDataObj [index=" + index + ", name=" + name + ", data=" + data + "]";
	}
	public Integer getIndex()
	{
		return index;
	}
	public void setIndex(Integer index)
	{
		this.index = index;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public ArrayList<OBDtoConnectionDataObj> getData()
	{
		return data;
	}
	public void setData(ArrayList<OBDtoConnectionDataObj> data)
	{
		this.data = data;
	}
}
