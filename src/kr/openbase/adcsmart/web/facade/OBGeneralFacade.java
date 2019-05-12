package kr.openbase.adcsmart.web.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBGeneral;
import kr.openbase.adcsmart.service.impl.OBGeneralImpl;
import kr.openbase.adcsmart.service.utility.OBException;

@Component
public class OBGeneralFacade
{
	private static transient Logger log = LoggerFactory.getLogger(OBGeneralFacade.class);
	
	private OBGeneral generalObj;
	
	public OBGeneralFacade() 
	{
		generalObj = new OBGeneralImpl();
	}
	
	public OBException getOBExceptionInfo() throws OBException
	{
		log.debug("getOBExceptionInfo: {} start");
		OBException ret = generalObj.getOBExceptionInfo();
		log.debug("getOBExceptionInfo: {} end");
		return ret;
	}
}
