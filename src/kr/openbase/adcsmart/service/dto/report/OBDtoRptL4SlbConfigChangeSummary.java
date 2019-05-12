package kr.openbase.adcsmart.service.dto.report;

import java.util.ArrayList;


public class OBDtoRptL4SlbConfigChangeSummary
{
	private	Integer totalLogCount;// 변경 건수.
//	private Integer targetVSCount;// 변경이 이루어진 VS 개수.
//	private Integer totalVSCount;// 전체 대상 VS 개수.
	
	private ArrayList<OBDtoRptL4ConfigChange> configChangeList;

	@Override
	public String toString()
	{
		return "OBDtoRptL4SlbConfigChangeSummary [totalLogCount="
				+ totalLogCount + ", configChangeList=" + configChangeList
				+ "]";
	}

	public Integer getTotalLogCount()
	{
		return totalLogCount;
	}

	public void setTotalLogCount(Integer totalLogCount)
	{
		this.totalLogCount = totalLogCount;
	}

	public ArrayList<OBDtoRptL4ConfigChange> getConfigChangeList()
	{
		return configChangeList;
	}

	public void setConfigChangeList(ArrayList<OBDtoRptL4ConfigChange> configChangeList)
	{
		this.configChangeList = configChangeList;
	}
	
	
}
