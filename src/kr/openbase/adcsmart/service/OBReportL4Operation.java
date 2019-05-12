package kr.openbase.adcsmart.service;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.report.OBDtoRptL4PerformanceInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptL4PerformanceSummary;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptL4PerformanceTrend;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptL4SlbConfigChangeSummary;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptTitle;
import kr.openbase.adcsmart.service.utility.OBException;

/**
 * L4 운영 보고서 
 * 
 * @author root
 *
 */
public interface OBReportL4Operation
{
	/**
	 * 보고서의 기본 정보를 제공한다.
	 * 
	 * @param rptIndex
	 * @return
	 * @throws OBException
	 */
	public OBDtoRptTitle getTitle(String rptIndex) throws OBException;
	
	/**
	 * SLB 설정 변경 이력 정보를 제공한다.
	 * 
	 * @param rptIndex
	 * @param accntIndex
	 * @return
	 * @throws OBException
	 */
	public OBDtoRptL4SlbConfigChangeSummary getSlbConfigChangeInfo(String rptIndex, Integer accntIndex) throws OBException;
	
	/**
	 * 성능 정보를 제공한다.
	 * 
	 * @param rptIndex
	 * @param accntIndex
	 * @return
	 * @throws OBException
	 */
	public OBDtoRptL4PerformanceSummary getPerformanceInfo(String rptIndex, Integer accntIndex) throws OBException;

	/** 
	 * 성능 추이 정보를 제공한다.
	 * 
	 * @param rptIndex
	 * @param accntIndex
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoRptL4PerformanceTrend> getTop10ThroughputList(String rptIndex, Integer accntIndex, ArrayList<OBDtoRptL4PerformanceInfo> itemList) throws OBException;

	/**
	 * 성능 추이 정보를 제공한다.
	 * 
	 * @param rptIndex
	 * @param accntIndex
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoRptL4PerformanceTrend> getTop10ConnectionList(String rptIndex, Integer accntIndex, ArrayList<OBDtoRptL4PerformanceInfo> itemList) throws OBException;
}
