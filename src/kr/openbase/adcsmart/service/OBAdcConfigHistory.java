package kr.openbase.adcsmart.service;

import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoConnectionData;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistoryAlteon;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistoryF5;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistoryPAS;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistoryPASK;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBLicenseExpiredException;

public interface OBAdcConfigHistory
{
	/**
	 * 지정된 장비의 virtual server 설정 변경 이력을 제공한다.
	 * @param adcIndex : 장비의 index 정보. null 불가.
	 * @param searchKey : 검색 키. virtual server 이름, IP주소, 변경 요약에서 검색한다.
	 * @param beginTime : 검색 시작 시각. null이면 조건 없음. 즉 최초
	 * @param endTime : 검색 종료 시각. null이면 조건 없음. 즉 최종
	 * @param beginIndex : 검색 범위 시작
	 * @param endIndex : 검색 범위 종료
	 * @return ArrayList<OBDtoAdcConfigHistory>
	 * @throws OBException
	 */
//	public ArrayList<OBDtoAdcConfigHistory> getAdcConfigHistoryList(Integer adcIndex, String searchKey, Date beginTime, Date endTime, Integer beginIndex, Integer endIndex) throws OBException;

	/**
	 * 
	 * @param adcIndex
	 * @param searchKey
	 * @param beginTime
	 * @param endTime
	 * @param beginIndex
	 * @param endIndex
	 * @param orderType, ORDER_TYPE_OCCURTIME, ORDER_TYPE_VSNAME, ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_CONTENT
	 * @param orderDir. OBDefine.ORDER_DIR_ASCEND, OBDefine.ORDER_DIR_DESCEND
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcConfigHistory> getAdcConfigHistoryList(Integer adcIndex, String searchKey, Date beginTime, Date endTime, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
	
	/**
	 * 	
	 * @param adcIndex : ADC 장비 index, null 불가
	 * @param searchKey : 검색 key, virtual server 이름, IP 주소, 변경 요약에서 검색
	 * @param beginTime : 검색 시작 시각. null이면 조건 없음. 즉 최초
	 * @param endTime : 검색 종료 시각. null이면 조건 없음. 즉 최종
	 * @return Integer
	 * @throws OBException
	 */
	public Integer getAdcConfigHistoryTotalRecordCount(Integer adcIndex, String searchKey, Date beginTime, Date endTime) throws OBException;

	/**
	 * 지정된 virtual server의 설정을 복구한다.
	 * @param adcIndex : 장비의 index 정보. null 불가.
	 * @param vsIndex : virtual server의 index 정보. null 불가.
	 * @param extraInfo
	 * @return 복구한 history 로그의 index 정보..
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 * @throws OBLicenseExpiredException
	 */
	public Long revertConfigF5(Integer adcIndex, String vsIndex, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException;
	public Long revertConfigAlteon(Integer adcIndex, String vsIndex, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException;
	public Long revertConfigPAS(Integer adcIndex, String vsIndex, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException;
	public Long revertConfigPASK(Integer adcIndex, String vsIndex, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException;

	/**
	 * Peer 장비에 대한 이력 복구 기능을 제공한다. 
	 * @param adcIndex. 복구할 장비의 index
	 * @param activeHistoryLogIndex. active 장비에서 복구한 이력의 로그 index. 해당 로그로부터 복구할 설정을 얻기 위한 용도.
	 * @param extraInfo
	 * @return
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 * @throws OBLicenseExpiredException
	 */
	public Long revertConfigF5Peer(Integer adcIndex, Long activeHistoryLogIndex, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException;
	public Long revertConfigAlteonPeer(Integer adcIndex, Long activeHistoryLogIndex, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException;
	public Long revertConfigPASPeer(Integer adcIndex, Long activeHistoryLogIndex, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException;
	public Long revertConfigPASKPeer(Integer adcIndex, Long activeHistoryLogIndex, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException;

	/**
	 * 지정된 virtual server의 변경 사항을 제공한다.
	 * @param adcIndex : 장비의 index 정보. null 불가.
	 * @param vsIndex : virtual server의 index 정보. null 불가.
	 * @return OBDtoVSConfigHistory
	 * @throws OBException
	 */
	public OBDtoAdcConfigHistoryF5 getVSConfigHistoryF5(Integer adcIndex, String vsIndex, Integer logSeq) throws OBException;
	public OBDtoAdcConfigHistoryAlteon getVSConfigHistoryAlteon(Integer adcIndex, String vsIndex, Integer logSeq) throws OBException;
	public OBDtoAdcConfigHistoryPAS getVSConfigHistoryPAS(Integer adcIndex, String vsIndex, Integer logSeq) throws OBException;
	public OBDtoAdcConfigHistoryPASK getVSConfigHistoryPASK(Integer adcIndex, String vsIndex, Integer logSeq) throws OBException;
	/**
	 * 지정된 장비의 지정된 virtual server에서 사용중인  concurrent connections 개수를 조회하여 제공한다.
	 * 
	 * @param adcIndex : 장비의 index 정보. null 불가.
	 * @param vsIndex : virtual server의 index 정보. null 불가.
	 * @return OBDtoConnectionInfo
	 * @throws OBException
	 */
	public OBDtoConnectionData getVSRealTimeCurrConns(Integer adcIndex, String vsIndex) throws OBException;
	
	/**
	 * 복구 실행 직전에 복구를 할 수 있는 조건인지 확인한다.
	 * 1. 사용자가 자격이 있는가?
	 * 2. 복구하려는 작업이 ADCSmart에서 일어난 것인가?
	 * 3. 복구하려는 객체가 virtual server인가?(persistence profile은 복구대상이 아님)
	 * 
	 * @param accountIndex : 사용자 계정 index
	 * @param adcIndex : 장비의 index 
	 * @param vsIndex : virtual server의 index
	 * @return : 
	 * 	zero-length String : 복구 가능
	 *  nonzero-length String : 복구 불가능, String에 이유 저장
	 * @throws OBException
	 */
	public String checkRecoverable(Integer accountIndex, Integer adcIndex, String vsIndex, Integer logSeq) throws OBException;
}
