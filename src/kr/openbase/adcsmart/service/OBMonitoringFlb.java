package kr.openbase.adcsmart.service;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoInterfaceSummary;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.flb.OBDtoFlbFilterInfo;
import kr.openbase.adcsmart.service.dto.flb.OBDtoFlbGroupMonitorInfo;
import kr.openbase.adcsmart.service.utility.OBException;

public interface OBMonitoringFlb
{
	/**
	 * Filter에 쓰이고 있는 Group 요약 목록을 조회다. 요약에 모니터링 선택 여부를 포함한다.
	 * 
	 * @param adcIndex
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoFlbGroupMonitorInfo> getFlbGroupMonitorInfo(int adcIndex) throws OBException;
	
	/**
	 * Filter group을 모니터링 대상으로 선택한다. 선택하지 않은 group은 해제된다.
	 * 
	 * @param selectedGroupIndex
	 * @return
	 * @throws OBException
	 */
	public void setFlbGroupMonitorInfo(Integer adcIndex, ArrayList<String> selectedGroupIndexList) throws OBException;
	
	/**
	 * Filter 정보 목록을 구한다. 
	 * 조건: physical port
	 * 표시조건: 페이징, 정렬
	 * @param adcIndex
	 * @param condition
	 * @param orderType
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoFlbFilterInfo> getFilterInfo(int adcIndex, OBDtoSearch condition, OBDtoOrdering orderOption) throws OBException;
	/**
	 * Filter 정보 목록을 구한다. 
	 * 조건: group id(alteon id)
	 * 표시조건: 페이징, 정렬
	 * @param adcIndex
	 * @param condition
	 * @param orderType
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoFlbFilterInfo> getFilterInfoByGroupId(int adcIndex, OBDtoSearch condition, OBDtoOrdering orderOption) throws OBException;

	/**
	 * Filter가 설정되어 있는 physical port 목록을 구한다.
	 * @param adcIndex
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoInterfaceSummary> getPhysicalPortForFilter(int adcIndex) throws OBException;
	
	/**
	 * 
	 * @param adcIndex
	 * @return
	 * @throws OBException
	 */
	public Integer getFilterCount(int adcIndex, OBDtoSearch condition) throws OBException;
	
}
