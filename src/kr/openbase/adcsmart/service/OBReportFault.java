package kr.openbase.adcsmart.service;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.report.OBDtoRptAdcFaultInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptAdcLogInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptTitle;
import kr.openbase.adcsmart.service.utility.OBException;

public interface OBReportFault
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
	 * ADC 장비의 장애 모니터링 로그를 분석한다.
	 * 
	 * @param rptIndex : 보고서 로그 인덱스. null 불가.
	 * @return ArrayList<OBDtoRptAdcFaultInfo>
	 * @throws OBException
	 */
	public ArrayList<OBDtoRptAdcFaultInfo> getAdcFaultList(String rptIndex) throws OBException;

	/**
	 * ADC 장비의 로그를 분석한다. 
	 * 
	 * @param rptIndex : 보고서 로그 인덱스. null 불가.
	 * @return ArrayList<OBDtoRptAdcLogInfo>
	 * @throws OBException
	 */
	public ArrayList<OBDtoRptAdcLogInfo> getAdcLogList(String rptIndex) throws OBException;
}
