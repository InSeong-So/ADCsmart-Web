package kr.openbase.adcsmart.service;

import kr.openbase.adcsmart.service.dto.OBDtoAlert;
import kr.openbase.adcsmart.service.dto.OBDtoAlertConfig;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.utility.OBException;

public interface OBAlert
{
	/**
	 * 계정에 관련된 ADC 경보 중 새 경보를 구한다. </br>
	 * 새 alert의 전체개수를 리턴한다. 실제로 나가는 alert 수는 100개까지만 한다. </br>
	 * @param accountIndex: 사용자 계정 인덱스
	 * @param type : 분류 선택 - 전체(null), 장애(0), 경고(1)
	 * @param alertCount : 최근 N개의 alert을 구한다. 0이면 전체.
	 * @param ordering : 
	 * @return
	 * @throws OBException
	 */
	public OBDtoAlert getAlert(Integer accountIndex, Integer type, int alertCount, OBDtoOrdering ordering) throws OBException;
	
	/**
	 * alert사용여부와 (alert을 하는 경우) sound alarm도 같이 할 것인지를 확인한다. 시스템 설정에 "사용함"이면 계정별 alert 옵션을 보고 동작하고, 시스템에서 "안함"이면 안 한다.
	 * @param accountIndex : 계정 인덱스
	 * @return : OBDtoAlertConfig</br>
	 *      - alertEnabled : enabled=1,disabled=0</br> 
	 *      - alertSound : enabled=1, disabled=0</br>
	 * @throws OBException
	 */
	/**
	 * 티커 메시지 경보 구하기: 각 사용자의 최신 경보 1건을 구한다.
	 * @param accountIndex
	 * @return
	 * @throws OBException
	 */
	public OBDtoAlert getAlertTicker(Integer accountIndex) throws OBException;
	
	public OBDtoAlertConfig getAlertConfig(Integer accountIndex) throws OBException;
	
	/**
	 * User 의 마지막 Alert 조회시간을 Update 한다.
	 * @param accountIndex
	 * @throws OBException
	 */
	public void updateUserAlertTime(Integer accountIndex) throws OBException;
	/**
	 * 추가하려는 계정의 마지막 초기 AlertTime을 현재시간으로 Update 한다.
	 * @param accountName
	 * @throws OBException
	 */
	public void updateUserAlertTimebyName(String accountName) throws OBException;
}