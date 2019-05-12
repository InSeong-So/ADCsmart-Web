package kr.openbase.adcsmart.service;

import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.service.utility.OBException;

public interface OBReport
{
	/**
	 * 지정된 보고서 내용 조회.
	 * 
	 * @param rptIndex
	 * @return OBReportInfo
	 * @throws OBException
	 */
	public OBReportInfo getReportInfo(String rptIndex) throws OBException;
	
	/**
	 * 생성된 보고서 목록을 제공한다. 지정된 계정에 지정된 ADC 장비에서 생성된 보고서 목록을 제공한다.
     * @param adccountIndex : 로그인한 사용자 계정. null 불가.
	 * @param adcIndex : 조회하고자 하는 adc의 인덱스
	 * @param groupIndex : 조회하고자 하는 그룹의 인덱스
	 * @param searchKeys : 검색 키워드. 보고서 이름, ADC 이름. 사용자 ID를 대상으로 제공한다.
     * @param beginTime : 시간 범위로 보고서 목록을 조회하고자 할 경우의 시작 시각. null일 경우에는 최초 생성 시간부터로 간주함.
     * @param endTime : 시간 범위로 보고서 목록을 조회하고자 할 경위 종료 시각. nulll일 경우에는 현재 시각으로 간주함.
     * @param beginIndex : 조회 목록의 시작 인덱스.
     * @param endIdex : 조회 목록의 종료 인덱스. 
     * @return ArrayList<OBReportInfo>
     * @throws OBException
	 */
	public ArrayList<OBReportInfo> getReportInfoList(Integer adccountIndex, Integer adcIndex, Integer groupIndex, String searchKeys, Date beginTime, Date endTime, Integer beginIndex, Integer endIdex) throws OBException;
	
	/**
	 * 
	 * @param adccountIndex
	 * @param adcIndex
	 * @param groupIndex
	 * @param searchKeys
	 * @param beginTime
	 * @param endTime
	 * @param beginIndex
	 * @param endIdex
	 * @param orderType
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBReportInfo> getReportInfoList(Integer adccountIndex, Integer adcIndex, Integer groupIndex, String searchKeys, Date beginTime, Date endTime, Integer beginIndex, Integer endIdex, Integer orderType, Integer orderDir) throws OBException;

	public ArrayList<OBReportInfo> getReportInfoList(Integer Status) throws OBException;

	/**
	 * 
	 * 지정된 계정의 지정된 ADC 장비를 대상으로 생성된 목록의 총 개수를 제공한다.
     * @param adccountIndex : 로그인한 사용자 계정. null 불가.
	 * @param adcIndex : 조회하고자 하는 adc의 인덱스
	 * @param groupIndex : 조회하고자 하는 그룹의 인덱스
	 * @param searchKeys : 검색 키워드. 보고서 이름, ADC 이름. 사용자 ID를 대상으로 제공한다.
     * @param beginTime : 시간 범위로 보고서 목록을 조회하고자 할 경우의 시작 시각. null일 경우에는 최초 생성 시간부터로 간주함.
     * @param endTime : 시간 범위로 보고서 목록을 조회하고자 할 경위 종료 시각. nulll일 경우에는 현재 시각으로 간주함.
     * @return Integer
     * @throws OBException
	 */
	public Integer	getReportInfoListCount(Integer adccountIndex, Integer adcIndex, Integer groupIndex, String searchKeys, Date beginTime, Date endTime) throws OBException;
	
	/**
	 * 지정된 보고서의 상태 정보를 추출한다.
	 * @param index : 보고서 인덱스. null 불가.
	 * @return Integer
	 * @throws OBException
	 */
	public Integer	getReportStatus(String index) throws OBException;
	
	/**
	 * 지정된 보고서의 상태를 설정한다.
	 * @param index : 보고서의 index. null 불가.
	 * @param status : 상태 정보. null 불가.
	 * @throws OBException
	 */
	public void		updateReportStatus(String index, Integer status) throws OBException;
	
	/**
	 * 지정된 보고서를 삭제한다.
	 * @param indexList : 삭제하고자 하는 보고서 index 목록.
	 * @param extraInfo : 사용자 로그인 정보.
	 * @throws OBException
	 */
	public void 	delReport(ArrayList<String> indexList, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 * 신규 보고서를 생성한다.
	 * @param conf : 보고서 생성을 위한 기본 정보.
	 * @param extraInfo : 사용자 로그인 정보.
	 * @return String
	 * @throws OBException
	 */
	public String 	addReport(OBReportInfo conf, OBDtoExtraInfo extraInfo) throws OBException;
}
