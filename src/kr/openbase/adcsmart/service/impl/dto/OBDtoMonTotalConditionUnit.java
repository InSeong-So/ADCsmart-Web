package kr.openbase.adcsmart.service.impl.dto;

import java.util.ArrayList;

public class OBDtoMonTotalConditionUnit
{
	private boolean isSelect;
	private ArrayList<OBDtoMonTotalFilterUnit> filter;
	
	@Override
	public String toString()
	{
		return "OBDtoMonConditionUnit [isSelect=" + isSelect + ", filter="
				+ filter + "]";
	}
	
	public OBDtoMonTotalConditionUnit(boolean isSelect, ArrayList<OBDtoMonTotalFilterUnit> filter)
	{
		this.setSelect(isSelect);
		this.setFilter(filter);
	}
	
	public boolean isSelect()
	{
		return isSelect;
	}
	public void setSelect(boolean isSelect)
	{
		this.isSelect = isSelect;
	}
	public ArrayList<OBDtoMonTotalFilterUnit> getFilter()
	{
		return filter;
	}
	public void setFilter(ArrayList<OBDtoMonTotalFilterUnit> filter)
	{
		this.filter = filter;
	}
}
