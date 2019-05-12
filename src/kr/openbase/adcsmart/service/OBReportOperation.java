package kr.openbase.adcsmart.service;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.report.OBDtoRptAdcInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptPortInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptSystemInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptTitle;
import kr.openbase.adcsmart.service.utility.OBException;

public interface OBReportOperation
{
	/**
	 * 보고서 생성을 위한 기초 데이터를 추출한다. (보고서 생성시간, 보고서 기간, 사용자, 보고서 대상)
	 * 
	 * @param rptIndex : 보고서 로그 인덱스. null 불가.
	 * @return OBDtoRptTitle
	 * @throws OBException
	 */
	public OBDtoRptTitle getTitle(String rptIndex) throws OBException;
	
	/**
	 * 시스템 기본 정보를 제공한다.
	 * 
	 * @param rptIndex : 보고서 인덱스. null 불가.
	 * @param adcIndex : 보고서에 포함된 ADC 장비의 index. null 불가.
	 * @return OBDtoRptAdcInfo
	 * @throws OBException
	 */
	public OBDtoRptAdcInfo getAdcInfo(String rptIndex, Integer adcIndex) throws OBException;
	
	/**
	 * 시스템 상태 정보를 제공한다.
	 * 
	 * @param rptIndex : 보고서 인덱스. null 불가.
	 * @param adcIndex : 보고서에 포함된 ADC 장비의 index. null 불가.
	 * @return OBDtoRptSystemInfo
	 * @throws OBException
	 */
	public OBDtoRptSystemInfo getSystemInfo(String rptIndex, Integer adcIndex) throws OBException;
	
	/**
	 * Port별 상태 정보를 제공한다. 
	 * 
	 * @param rptIndex : 보고서 인덱스. null 불가.
	 * @param adcIndex : 보고서에 포함된 ADC 장비의 index. null 불가.
	 * @param portNameList : port interface 이름 목록. null 불가.
	 * @return ArrayList<OBDtoRptPortInfo>
	 * @throws OBException
	 */
	public ArrayList<OBDtoRptPortInfo> getPortInfo(String rptIndex, Integer adcIndex, ArrayList<String>portNameList) throws OBException;
}
