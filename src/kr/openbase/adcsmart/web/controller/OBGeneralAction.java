package kr.openbase.adcsmart.web.controller;

import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.OBGeneralFacade;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope(value = "prototype")
public class OBGeneralAction extends BaseAction
{
//	private transient Logger log = LoggerFactory.getLogger(OBGeneralAction.class);
	
	@Autowired
	private OBGeneralFacade generalFacade;
	
	public String getOBExceptionInfo()
	{
		adcexception.setOBException(null);
		try 
		{
//			log.debug("getErrorInfo: {}");
			OBException rtn = generalFacade.getOBExceptionInfo();
			if(null != rtn)
			{
				adcexception.setOBException(rtn);
			}
		} 
		catch (OBException e) 
		{
			adcexception.setOBException(e);
		}
		catch (Exception e) 
		{
			adcexception.setOBException(new OBException(e.getMessage()));
		}
		
		return SUCCESS;
	}
}

