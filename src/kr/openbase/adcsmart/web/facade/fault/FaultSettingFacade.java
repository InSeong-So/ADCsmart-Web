package kr.openbase.adcsmart.web.facade.fault;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBFaultMng;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoVirtualServiceInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckSchedule;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckTemplate;
import kr.openbase.adcsmart.service.dto.fault.OBDtoObjectIndexInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoScheduleDateTime;
import kr.openbase.adcsmart.service.impl.fault.OBFaultMngImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;

@Component
public class FaultSettingFacade
{
	private static transient Logger log = LoggerFactory.getLogger(FaultSettingFacade.class);
	
	private OBFaultMng faultMngSvc;
	
	public FaultSettingFacade()
	{
		faultMngSvc = new OBFaultMngImpl();
	}	
	
	//장애 진단 설정 default 화면 load
	public ArrayList<OBDtoFaultCheckTemplate> getFaultCheckTemplateDefaultList(OBDtoADCObject object, OBDtoFaultCheckTemplate templateObj) throws OBException, Exception
	{	
		ArrayList<OBDtoFaultCheckTemplate> faultChkListFromSvc = new ArrayList<OBDtoFaultCheckTemplate>();
		faultChkListFromSvc = faultMngSvc.getFaultCheckTemplateList(null, null);
		log.debug("{}", faultChkListFromSvc);
		
		return faultChkListFromSvc;
	}
	
	public OBDtoFaultCheckTemplate getFaultCheckTemplateInfo(Long index) throws OBException, Exception
	{
		return faultMngSvc.getFaultCheckTemplateInfo(index);
	}
	
	//등록된 템플릿 목록 제공
	public ArrayList<OBDtoFaultCheckTemplate> getFaultCheckTemplateList(OBDtoADCObject object, OBDtoSearch searchObj, OBDtoOrdering orderObj) throws OBException, Exception
	{
		return faultMngSvc.getFaultCheckTemplateList(null, null);		
	}
	
	//Client IP주소 목록
	public ArrayList<String> getUsedClientIPList() throws OBException, Exception
	{
		ArrayList<String> clientIpList = faultMngSvc.getUsedClientIPList();
	
		log.debug("clientIpList : {}", clientIpList);
		return clientIpList;
	}
	
	//VirtualService 목록
	public ArrayList<OBDtoVirtualServiceInfo> getVServiceList(OBDtoADCObject object) throws OBException, Exception
	{
		return faultMngSvc.getVServiceList(object);
	}
		
	//장애 진단을 시작	
//	public ArrayList<OBDtoFaultCheckIndexInfo> startFaultCheck(OBDtoADCObject object, OBDtoFaultCheckTemplate templateObj, Integer checkSpeed, SessionDto session) throws OBException, Exception
	public ArrayList<OBDtoObjectIndexInfo> startFaultCheck(OBDtoADCObject object, OBDtoFaultCheckTemplate templateObj, Integer checkSpeed, SessionDto session) throws OBException, Exception
	{	
//		ArrayList<OBDtoFaultCheckIndexInfo> faultChkIndexFromSvc = new ArrayList<OBDtoFaultCheckIndexInfo>();
		ArrayList<OBDtoObjectIndexInfo> faultChkIndexFromSvc = new ArrayList<OBDtoObjectIndexInfo>();
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();	
		extraInfo.setExtraMsg1(templateObj.getName());
		faultChkIndexFromSvc = faultMngSvc.startFaultCheck(object, templateObj, 0, extraInfo);
		log.debug("{}", faultChkIndexFromSvc);
		return faultChkIndexFromSvc;
	}
		
	//진행중인 장애 검사를 취소
	public void cancelFaultCheck(AdcDto adc, Long checkKey, SessionDto session) throws OBException, Exception
	{		
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();	
		extraInfo.setExtraMsg1(adc.getName());
		faultMngSvc.cancelFaultCheck(checkKey, extraInfo);
	}
		
	//장애 진단 예약을 등록
	public void registerFaultCheckSchedule(OBDtoADCObject object, String name, String description, OBDtoFaultCheckTemplate templateObj, OBDtoScheduleDateTime timeObj, Integer checkSpeed, Integer repeatFlag, SessionDto session) throws OBException, Exception
	{
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();	
		extraInfo.setExtraMsg1(name);
		faultMngSvc.registerFaultCheckSchedule(object, name, description, templateObj, timeObj, checkSpeed, repeatFlag, extraInfo);
	}
	
	//지정된 장비의 할당된 장애 점검 예약 리스트를 조회
	public ArrayList<OBDtoFaultCheckSchedule> getFaultCheckScheduleList(OBDtoADCObject object, OBDtoSearch searchObj, OBDtoOrdering orderObj) throws OBException, Exception	
	{		
		return faultMngSvc.getFaultCheckScheduleList(object, searchObj, orderObj);
	}
	
	//장애 점검 예약 정보를 조회
	public OBDtoFaultCheckSchedule getFaultCheckScheduleInfo(Long index) throws OBException, Exception	
	{		
		return faultMngSvc.getFaultCheckScheduleInfo(index);
	}

	//장애 진단 템플릿을 저장
	public void saveFaultCheckTemplate(OBDtoFaultCheckTemplate templateObj, SessionDto session) throws OBException, Exception	
	{		
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();	
		extraInfo.setExtraMsg1(templateObj.getName());
//		faultMngSvc.startFaultCheck(object, templateObj, checkSpeed, extraInfo);
		faultMngSvc.saveFaultCheckTemplate(templateObj, extraInfo);

	}
	
	//장애 진단 템플릿 삭제
	public void delFaultCheckTemplate(OBDtoFaultCheckTemplate templateObj, SessionDto session) throws OBException, Exception	
	{
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();	
		extraInfo.setExtraMsg1(templateObj.getName());
		faultMngSvc.deleteFaultCheckTemplate(templateObj, extraInfo);
	}
}
