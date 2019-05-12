package kr.openbase.adcsmart.service;


import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoNoticeInfo;
import kr.openbase.adcsmart.service.utility.OBException;

public interface OBNoticeMessage
{
	/**
	 * adcsmart의 공지 메세지를 추출한다.
	 * @param accountIndex : account 인덱스. null 불가
	 * @return <OBDtoNotice>
	 * @throws OBException
	 */
	
	public ArrayList<OBDtoNoticeInfo> getNoticeMessage(Integer accountIndex) throws OBException;
	
	
	/**
	 * adcsmart의 공지 메세지를 띄울지 안띄울지 구별하는 key를 추출한다.
	 * @param accountIndex : account 인덱스. null 불가
	 * @return <OBDtoNotice>
	 * @throws OBException
	 */
	
	public void addNoticeMessage(Integer accountIndex, String message) throws OBException;

}
