package kr.openbase.adcsmart.web.facade;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBNoticeMessage;
import kr.openbase.adcsmart.service.dto.OBDtoNoticeInfo;
import kr.openbase.adcsmart.service.impl.OBNoticeMessageImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.adcman.AdcSettingAction;

@Component
public class NoticeFacade 
{
	private static transient Logger log = LoggerFactory.getLogger(AdcSettingAction.class);
	
	
	private OBNoticeMessage NoticeMgmt;
	
	public NoticeFacade() {
		NoticeMgmt = new OBNoticeMessageImpl();
	}
	
/*	public static String getNoticeMessage(Integer accountIndex) throws OBException, Exception
	{
		String retVal = "Notic Test 중입니다.";
		return retVal;
	}*/	
	public ArrayList<OBDtoNoticeInfo> getNoticeMessage(Integer accountIndex) throws OBException, Exception
	{
		ArrayList<OBDtoNoticeInfo> retVal = NoticeMgmt.getNoticeMessage(accountIndex);
		log.debug("retVal : {}", retVal);
		return retVal;
	}	
}
