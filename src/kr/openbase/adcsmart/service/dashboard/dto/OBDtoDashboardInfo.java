package kr.openbase.adcsmart.service.dashboard.dto;

import java.util.ArrayList;

public class OBDtoDashboardInfo
{
	private String indexKey;
	private String name;
	private ArrayList<OBDtoWidgetInfo> widgetList;
	@Override
	public String toString()
	{
		return String.format("OBDtoDashboardInfo [indexKey=%s, name=%s, widgetList=%s]", indexKey, name, widgetList);
	}
	public String getIndexKey()
	{
		return indexKey;
	}
	public void setIndexKey(String indexKey)
	{
		this.indexKey = indexKey;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public ArrayList<OBDtoWidgetInfo> getWidgetList()
	{
		return widgetList;
	}
	public void setWidgetList(ArrayList<OBDtoWidgetInfo> widgetList)
	{
		this.widgetList = widgetList;
	}
}
