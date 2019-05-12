package kr.openbase.adcsmart.service;

import java.util.*;

import kr.openbase.adcsmart.service.dto.*;
import kr.openbase.adcsmart.service.utility.*;

public interface OBAdcProfile
{
	/**
	 * ADC의 persistence profile 목록을 가져온다. profileName을 주면 해당 profile의 정보를 가져온다.
	 * 
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @param profileIndex : 
	 *  - profile index.
	 *  - null 가능. null이면 전체 persistence profile 목록을 가져온다.
	 * @return
	 *  - profileIndex null : 전체 persistence profile 정보 목록을 반환한다.
	 *  - profileIndex 유효값 : 해당 persistence profile 정보를 반환한다.
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcProfile> getProfileList(Integer adcIndex, String profileIndex) throws OBExceptionUnreachable, OBExceptionLogin, OBException;

	/**
	 * 새 persistence profile을 추가한다.
	 *  
	 * @param adcIndex	: ADC 인덱스 
	 * @param profile : 추가할 persistence profile 정보. null 불가
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public void addProfile(Integer adcIndex, OBDtoAdcProfile profile, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException;

	/**
	 * 복수의 persistence profile을 삭제한다.
	 * @param profileIndexList : 삭제할 persistence profile index들의 목록. null 불가
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public void delProfile(Integer adcIndex, ArrayList<String> profileIndexList, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException;

	/**
	 * persistence profile 정보를 수정한다.
	 * 
	 * @param profile : 수정할 persistence profile 정보. null 불가
	 * @param extraInfo : 부가적인 정보. accountIndex, 부가적인 설명을 위한 message가 전달된다. null 불가.
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 * @throws Exception
	 */
	public void setProfile(Integer adcIndex, OBDtoAdcProfile profile, OBDtoExtraInfo extraInfo) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
	
	/**
	 * persistence profile 중에서 검색조건(searchKey)에 맞는 목록을 가져온다.
	 * persistence profile 이름이 검색조건(searchKey)을 포함하는 persistence profile 목록을 가져온다.
	 * searchKey가 null이면 전체 persistence profile 목록을 가져온다.
	 * F5 ADC에만 있다.
	 * @param adcIndex : ADC 인덱스. null 불가
	 * @param searchKey : persistence profile 이름/IP에서 검색할 문자열. null 가능
	 * @return
	 *  - persistence profile 목록
	 */
	public ArrayList<OBDtoAdcProfile> searchProfileList(Integer adcIndex, String searchKey) throws OBExceptionUnreachable, OBExceptionLogin, OBException;

	// 모니터링 항목 추가 필요..

	/**
	 * 
	 * @param adcIndex
	 * @param searchKey
	 * @param orderType
	 * @param orderDir. ORDER_DIR_ASCEND, ORDER_DIR_DESCEND
	 * @return
	 * @throws OBExceptionUnreachable
	 * @throws OBExceptionLogin
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcProfile> searchProfileList(Integer adcIndex, String searchKey, Integer orderType, Integer orderDir) throws OBExceptionUnreachable, OBExceptionLogin, OBException;
}
