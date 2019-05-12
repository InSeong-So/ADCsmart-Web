package kr.openbase.adcsmart.service.dto.flb;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeAlteon;

/**
 * FLB에 쓰이는 그룹의 모니터링 선택 정보
 * : 필터 그룹의 요약목록. 모니터링 대상 선택 여부와 필수 요약정보 
 */

public class OBDtoFlbGroupMonitorInfo
{
	private String            dbIndex;
	private String            groupId;
	private Integer           isMonitoringOn; //모니터링 대상으로 선택됐는가. 1:선택함, 0:선택안함
	private Long              currentConnection;
	private ArrayList<OBDtoFlbFilterSummary> filterList;
	private ArrayList<OBDtoAdcNodeAlteon> realList; //그룹 소속의 real들, OBDtoAdcName 클래스가 딱 맞는 자료형이어서 차용해 쓴다

	@Override
	public String toString()
	{
		return "OBDtoFlbGroupMonitorInfo [dbIndex=" + dbIndex + ", groupId="
				+ groupId + ", isMonitoringOn=" + isMonitoringOn
				+ ", currentConnection=" + currentConnection + ", filterList="
				+ filterList + ", realList=" + realList + "]";
	}
	public String getDbIndex()
	{
		return dbIndex;
	}
	public void setDbIndex(String dbIndex)
	{
		this.dbIndex = dbIndex;
	}
	public String getGroupId()
	{
		return groupId;
	}
	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}
	public Integer getIsMonitoringOn()
	{
		return isMonitoringOn;
	}
	public void setIsMonitoringOn(Integer isMonitoringOn)
	{
		this.isMonitoringOn = isMonitoringOn;
	}
	public Long getCurrentConnection()
	{
		return currentConnection;
	}
	public void setCurrentConnection(Long currentConnection)
	{
		this.currentConnection = currentConnection;
	}
	public ArrayList<OBDtoFlbFilterSummary> getFilterList()
	{
		return filterList;
	}
	public void setFilterList(ArrayList<OBDtoFlbFilterSummary> filterList)
	{
		this.filterList = filterList;
	}
	public ArrayList<OBDtoAdcNodeAlteon> getRealList()
	{
		return realList;
	}
	public void setRealList(ArrayList<OBDtoAdcNodeAlteon> realList)
	{
		this.realList = realList;
	}
}
