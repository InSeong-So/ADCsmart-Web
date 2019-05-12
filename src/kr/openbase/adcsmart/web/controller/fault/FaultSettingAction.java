package kr.openbase.adcsmart.web.controller.fault;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoElement;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoVirtualServiceInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckSchedule;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckTemplate;
import kr.openbase.adcsmart.service.dto.fault.OBDtoObjectIndexInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoScheduleDateTime;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.fault.FaultSettingFacade;
import kr.openbase.adcsmart.web.json.ElementJsonAdapter;

@Controller
@Scope(value = "prototype")
public class FaultSettingAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(FaultHistoryAction.class);
	
	@Autowired
	private FaultSettingFacade faultSettingFacade;
	
	private AdcDto adc;
	private Integer orderDir; 								// 오른차순 = 1, 내림차순 = 2
	private Integer orderType; 	
	private Integer curCategory=0; 							//0: 전체, 1: 그룹, 2: 개별 adc, 3:virtual server, 4:virtual service
	private OBDtoADCObject adcObject;
	private OBDtoSearch searchObj;
	private OBDtoOrdering orderObj;
	private Integer checkSpeed;
	private ArrayList<OBDtoFaultCheckTemplate> faultCheckTempleteList;			//정책 템플릿 리스트		
	private ArrayList<OBDtoVirtualServiceInfo> faultVirtualSvcList;				//서비스 리스트
//	private ArrayList<OBDtoFaultCheckIndexInfo> faultCheckIndex;				//진단
	private ArrayList<OBDtoObjectIndexInfo> faultCheckIndex;				//진단
	private OBDtoFaultCheckTemplate faultCheckTemplate;							//선택된 템플릿 
	private ArrayList<String> faultClientIpList;								//사용자 IP 리스트
	private ArrayList<OBDtoFaultCheckSchedule> faultCheckScheduleList;			//예약 리스트
	private OBDtoFaultCheckSchedule faultCheckScheduleInfo;						//선택된 예약정보	
	private Long scheduleIndex = 0L;
	private Long templateIndex = 0L;
	private String templateNm;
	private Integer faultMaxDays;
	private Integer flbFaultMaxDays;
	private Integer adcLogCount;
	private String clientIp;
	private String svcValue;
	private String vsIndex;
	private String vsIp;
	private String vsName;	
	private String hwCheckInString;
	private String l23CheckInString;
	private String l47CheckInString;
	private String svcCheckInString;	
	private	ArrayList<OBDtoElement> hwCheckItems; 
	private	ArrayList<OBDtoElement> l23CheckItems;
	private	ArrayList<OBDtoElement> l47CheckItems;   
	private	ArrayList<OBDtoElement> svcCheckItems;	
	private Integer everyHr;
	private Integer everyMin;
	private Integer everyDayOfWeek;
	private Integer everyDayOfMonth;
	private Integer everyDayMonth;	
	private Integer scheduleType;
	private Integer svcFlag;
	
	public FaultSettingAction()
	{
		hwCheckItems = new ArrayList<OBDtoElement>();
		l23CheckItems = new ArrayList<OBDtoElement>();
		l47CheckItems = new ArrayList<OBDtoElement>();
		svcCheckItems = new ArrayList<OBDtoElement>();	
	}
	
	private ArrayList<OBDtoElement> convertHwCheckToJSON(String elementString)
	{
		if (StringUtils.isEmpty(elementString))
			return null;
		
		hwCheckItems.clear();
		Gson gson = new GsonBuilder().registerTypeAdapter(OBDtoElement.class, new ElementJsonAdapter()).create();
		JsonParser parser = new JsonParser();
		JsonArray jarray = parser.parse(elementString).getAsJsonArray();
		for (JsonElement e : jarray)
			hwCheckItems.add(gson.fromJson(e, OBDtoElement.class));
		
		return hwCheckItems;		
	}
	private ArrayList<OBDtoElement> convertL23CheckToJson(String elementString)
	{
		if (StringUtils.isEmpty(elementString))
			return null;
		
		l23CheckItems.clear();
		Gson gson = new GsonBuilder().registerTypeAdapter(OBDtoElement.class, new ElementJsonAdapter()).create();
		JsonParser parser = new JsonParser();
		JsonArray jarray = parser.parse(elementString).getAsJsonArray();
		for (JsonElement e : jarray)
			l23CheckItems.add(gson.fromJson(e, OBDtoElement.class));
		
		return l23CheckItems;		
	}
	private ArrayList<OBDtoElement> convertL47CheckToJson(String elementString)
	{
		if (StringUtils.isEmpty(elementString))
			return null;
		
		l47CheckItems.clear();
		Gson gson = new GsonBuilder().registerTypeAdapter(OBDtoElement.class, new ElementJsonAdapter()).create();
		JsonParser parser = new JsonParser();
		
		JsonArray jarray = parser.parse(elementString).getAsJsonArray();
		for (JsonElement e : jarray)
			l47CheckItems.add(gson.fromJson(e, OBDtoElement.class));
		
		return l47CheckItems;
	}
	private ArrayList<OBDtoElement> convertSvcCheckToJson(String elementString)
	{
		if (StringUtils.isEmpty(elementString))
			return null;
		
		svcCheckItems.clear();
		Gson gson = new GsonBuilder().registerTypeAdapter(OBDtoElement.class, new ElementJsonAdapter()).create();
		JsonParser parser = new JsonParser();
		JsonArray jarray = parser.parse(elementString).getAsJsonArray();
		for (JsonElement e : jarray)
			svcCheckItems.add(gson.fromJson(e, OBDtoElement.class));
		
		return svcCheckItems;
	}
	//진단설정 기본 페이지 load
	public String loadFaultSettingContent() throws OBException
	{		
		//사용자 지정
		//ADC 장비 진단 (H/W, L2-3, 4-7) , 서비스 진단 default 값 표현
		//mng_fatul_check_items
		try
		{				
			log.debug("adcObject:{}", adcObject); 
			
			faultCheckTempleteList = faultSettingFacade.getFaultCheckTemplateDefaultList(adcObject, null);	//정책 템플릿 리스트
//			OBDtoFaultCheckTemplate temp = new OBDtoFaultCheckTemplate();
//			temp.setIndex(1l);
//			temp.getHwCheckItems().add(new OBDtoElement(1, "111", "222", 15, 0));
//			temp.getHwCheckItems().add(new OBDtoElement(2, "333", "222", 15, 1));
//			faultCheckTemplate = temp;
			log.debug("faultCheckTempleteList:{}", faultCheckTempleteList);	
			
//			if(scheduleIndex != null)
//			{	
//				faultCheckScheduleInfo = faultHistoryFacade.getFaultCheckScheduleInfo(scheduleIndex);	
//			}
			
			faultVirtualSvcList = faultSettingFacade.getVServiceList(adcObject);
			faultClientIpList = faultSettingFacade.getUsedClientIPList();
			log.debug("faultVirtualSvcList:{}", faultVirtualSvcList);
			log.debug("faultClientIpList:{}", faultClientIpList); 
			
			if (templateIndex == 0 )
			{
				faultCheckTemplate = faultSettingFacade.getFaultCheckTemplateInfo(0L);
			}
			else
			{
				faultCheckTemplate = faultSettingFacade.getFaultCheckTemplateInfo(templateIndex);
//				loadFaultTableInListContent();
			}
//			if(faultCheckTemplate.getThresholdHWAdcLogCount() == null)
//			{
//				faultCheckTemplate.setThresholdHWAdcLogCount(0);
//			}				 
			log.debug("faultCheckTemplate:{}", faultCheckTemplate);
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return SUCCESS;
	}
	
	//장애 점검을 위한 템플릿 정보를 조회
	public String loadFaultTableInListContent() throws OBException
	{
		try
		{
			faultCheckTemplate = faultSettingFacade.getFaultCheckTemplateInfo(templateIndex);
			log.debug("faultCheckTemplate: {}", faultCheckTemplate);
			log.debug("templateIndex: {}", templateIndex);
			
			faultVirtualSvcList = faultSettingFacade.getVServiceList(adcObject);
			faultClientIpList = faultSettingFacade.getUsedClientIPList();
			log.debug("faultVirtualSvcList:{}", faultVirtualSvcList);
			log.debug("faultClientIpList:{}", faultClientIpList); 
		}
		catch (OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}
	//TODO
	//장애 진단 시작
	public String startFaultCheck() throws OBException
	{
		isSuccessful = true;
		try
		{	
			OBDtoFaultCheckTemplate templateObj = new OBDtoFaultCheckTemplate();
			templateObj.setIndex(templateIndex);
			templateObj.setName(templateNm);
//			templateObj.setName("");
			templateObj.setHwCheckItems(convertHwCheckToJSON(hwCheckInString));
			templateObj.setL23CheckItems(convertL23CheckToJson(l23CheckInString));
			templateObj.setL47CheckItems(convertL47CheckToJson(l47CheckInString));
			templateObj.setSvcCheckItems(convertSvcCheckToJson(svcCheckInString));			
			templateObj.setSvcCheckFlg(svcFlag);
			templateObj.setSvcClientIPAddress(clientIp);
			templateObj.setSvcVSIndex(vsIndex);
			templateObj.setSvcVSIPAddress(vsIp);
			templateObj.setSvcVSName(vsName);
			templateObj.setThresholdHWAdcLogCount(adcLogCount);
			templateObj.setThresholdL47SleepVSDay(faultMaxDays);
			
//			log.debug("selectedObj:{}", selectedObj); 
			
			log.debug("adcObject:{}, templateObj:{}", adcObject, templateObj);
			faultCheckIndex = faultSettingFacade.startFaultCheck(adcObject, templateObj, checkSpeed, session.getSessionDto());
//			System.out.println(faultCheckIndex.get(0)); 
			
			log.debug("faultCheckIndex:{}", faultCheckIndex); 
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}
	
	//장애 진단 취소
	/**
	 * 
	 * params : checkKey
	*/
	public String cancelFaultCheck() throws OBException
	{
		return SUCCESS;
	}
		
	//장애 진단 예약을 등록 - 예약버튼 click
	public String registerFaultCheckSchedule() throws OBException
	{
		isSuccessful = true;
		try
		{				
			OBDtoFaultCheckTemplate templateObj = new OBDtoFaultCheckTemplate();
			templateObj.setIndex(templateIndex);
			templateObj.setName(templateNm);
//			templateObj.setName("test");
			templateObj.setHwCheckItems(convertHwCheckToJSON(hwCheckInString));
			templateObj.setL23CheckItems(convertL23CheckToJson(l23CheckInString));
			templateObj.setL47CheckItems(convertL47CheckToJson(l47CheckInString));
			templateObj.setSvcCheckItems(convertSvcCheckToJson(svcCheckInString));
			templateObj.setSvcCheckFlg(svcFlag);
			templateObj.setSvcClientIPAddress(clientIp);
			templateObj.setSvcVSIndex(vsIndex);
			templateObj.setSvcVSIPAddress(vsIp);
			templateObj.setSvcVSName(vsName);
			templateObj.setThresholdHWAdcLogCount(adcLogCount);
			templateObj.setThresholdHWCpuUsage(50);
			templateObj.setThresholdHWFanMax(20);
			templateObj.setThresholdHWFanMin(30);
			templateObj.setThresholdHWFanMin(10);
			templateObj.setThresholdHWMemoryUsage(70);
			templateObj.setThresholdL47SleepVSDay(faultMaxDays);
			OBDtoScheduleDateTime timeObj = new OBDtoScheduleDateTime();
			
			timeObj.setEveryHour(everyHr);
			timeObj.setEveryMinute(everyMin);
			timeObj.setEveryDayOfWeek(everyDayOfWeek);
			timeObj.setEveryDayOfMonth(everyDayOfMonth);
			timeObj.setEveryMonth(everyDayMonth);
			
//			OBDtoFaultCheckSchedule scheduleObj = new OBDtoFaultCheckSchedule();
//			scheduleObj.setCheckItem(checkItem);
			
			log.debug("adcObject:{}", adcObject);
			log.debug("templateObj:{}", templateObj);
			log.debug("timeObj:{}", timeObj);
			faultSettingFacade.registerFaultCheckSchedule(adcObject, adcObject.getDesciption(), "", templateObj, timeObj, checkSpeed, scheduleType, session.getSessionDto());
			
//			faultCheckIndex = faultSettingFacade.startFaultCheck(adcObject, templateObj, checkSpeed, session.getSessionDto());
//			System.out.println(faultCheckIndex.get(0)); 
			
		}
		catch (OBException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return SUCCESS;
	}

	//장애 진단 템플릿을 저장
	public String saveFaultCheckTemplate() throws OBException
	{
		isSuccessful = true;
		try
		{			
			OBDtoFaultCheckTemplate templateObj = new OBDtoFaultCheckTemplate();
			templateObj.setIndex(templateIndex);
			templateObj.setName(templateNm);			
			templateObj.setHwCheckItems(convertHwCheckToJSON(hwCheckInString));
			templateObj.setL23CheckItems(convertL23CheckToJson(l23CheckInString));
			templateObj.setL47CheckItems(convertL47CheckToJson(l47CheckInString));
			templateObj.setSvcCheckItems(convertSvcCheckToJson(svcCheckInString));
			templateObj.setSvcCheckFlg(svcFlag);
			templateObj.setSvcClientIPAddress(clientIp);
			templateObj.setSvcVSIndex(vsIndex);
			templateObj.setSvcVSIPAddress(vsIp);
			templateObj.setSvcVSName(vsName);
			templateObj.setThresholdHWAdcLogCount(adcLogCount);
			templateObj.setThresholdL47SleepVSDay(faultMaxDays);
			
			log.debug("templateObj:{}", templateObj); 
			faultSettingFacade.saveFaultCheckTemplate(templateObj, session.getSessionDto());
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return SUCCESS;
	}
	
	//지정된 템플릿 삭제
	public String delFaultCheckTemplate() throws OBException
	{
		isSuccessful = true;
		try
		{
			OBDtoFaultCheckTemplate templateObj = new OBDtoFaultCheckTemplate();
			templateObj.setIndex(templateIndex);
			templateObj.setName(templateNm);
			
			log.debug("templateObj:{}", templateObj); 
			
			faultSettingFacade.delFaultCheckTemplate(templateObj, session.getSessionDto());
		}
		catch (OBException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return SUCCESS;
	}

	public AdcDto getAdc()
	{
		return adc;
	}

	public void setAdc(AdcDto adc)
	{
		this.adc = adc;
	}

	public Integer getOrderDir()
	{
		return orderDir;
	}

	public void setOrderDir(Integer orderDir)
	{
		this.orderDir = orderDir;
	}

	public Integer getOrderType()
	{
		return orderType;
	}

	public void setOrderType(Integer orderType)
	{
		this.orderType = orderType;
	}

	public Integer getCurCategory()
	{
		return curCategory;
	}

	public void setCurCategory(Integer curCategory)
	{
		this.curCategory = curCategory;
	}

	public OBDtoADCObject getAdcObject()
	{
		return adcObject;
	}

	public void setAdcObject(OBDtoADCObject adcObject)
	{
		this.adcObject = adcObject;
	}

	public OBDtoSearch getSearchObj()
	{
		return searchObj;
	}

	public void setSearchObj(OBDtoSearch searchObj)
	{
		this.searchObj = searchObj;
	}

	public OBDtoOrdering getOrderObj()
	{
		return orderObj;
	}

	public void setOrderObj(OBDtoOrdering orderObj)
	{
		this.orderObj = orderObj;
	}

	public Integer getCheckSpeed()
	{
		return checkSpeed;
	}

	public void setCheckSpeed(Integer checkSpeed)
	{
		this.checkSpeed = checkSpeed;
	}

	public ArrayList<OBDtoFaultCheckTemplate> getFaultCheckTempleteList()
	{
		return faultCheckTempleteList;
	}

	public void setFaultCheckTempleteList(ArrayList<OBDtoFaultCheckTemplate> faultCheckTempleteList)
	{
		this.faultCheckTempleteList = faultCheckTempleteList;
	}

	public ArrayList<OBDtoVirtualServiceInfo> getFaultVirtualSvcList()
	{
		return faultVirtualSvcList;
	}

	public void setFaultVirtualSvcList(ArrayList<OBDtoVirtualServiceInfo> faultVirtualSvcList)
	{
		this.faultVirtualSvcList = faultVirtualSvcList;
	}

	public ArrayList<OBDtoObjectIndexInfo> getFaultCheckIndex()
	{
		return faultCheckIndex;
	}

	public void setFaultCheckIndex(ArrayList<OBDtoObjectIndexInfo> faultCheckIndex)
	{
		this.faultCheckIndex = faultCheckIndex;
	}

	public OBDtoFaultCheckTemplate getFaultCheckTemplate()
	{
		return faultCheckTemplate;
	}

	public void setFaultCheckTemplate(OBDtoFaultCheckTemplate faultCheckTemplate)
	{
		this.faultCheckTemplate = faultCheckTemplate;
	}

	public ArrayList<String> getFaultClientIpList()
	{
		return faultClientIpList;
	}

	public void setFaultClientIpList(ArrayList<String> faultClientIpList)
	{
		this.faultClientIpList = faultClientIpList;
	}

	public ArrayList<OBDtoFaultCheckSchedule> getFaultCheckScheduleList()
	{
		return faultCheckScheduleList;
	}

	public void setFaultCheckScheduleList(ArrayList<OBDtoFaultCheckSchedule> faultCheckScheduleList)
	{
		this.faultCheckScheduleList = faultCheckScheduleList;
	}

	public OBDtoFaultCheckSchedule getFaultCheckScheduleInfo()
	{
		return faultCheckScheduleInfo;
	}

	public void setFaultCheckScheduleInfo(OBDtoFaultCheckSchedule faultCheckScheduleInfo)
	{
		this.faultCheckScheduleInfo = faultCheckScheduleInfo;
	}

	public Long getScheduleIndex()
	{
		return scheduleIndex;
	}

	public void setScheduleIndex(Long scheduleIndex)
	{
		this.scheduleIndex = scheduleIndex;
	}

	public Long getTemplateIndex()
	{
		return templateIndex;
	}

	public void setTemplateIndex(Long templateIndex)
	{
		this.templateIndex = templateIndex;
	}

	public String getTemplateNm()
	{
		return templateNm;
	}

	public void setTemplateNm(String templateNm)
	{
		this.templateNm = templateNm;
	}

	public Integer getFaultMaxDays()
	{
		return faultMaxDays;
	}

	public void setFaultMaxDays(Integer faultMaxDays)
	{
		this.faultMaxDays = faultMaxDays;
	}

	public Integer getAdcLogCount()
	{
		return adcLogCount;
	}

	public void setAdcLogCount(Integer adcLogCount)
	{
		this.adcLogCount = adcLogCount;
	}

	public String getClientIp()
	{
		return clientIp;
	}

	public void setClientIp(String clientIp)
	{
		this.clientIp = clientIp;
	}

	public String getSvcValue()
	{
		return svcValue;
	}

	public void setSvcValue(String svcValue)
	{
		this.svcValue = svcValue;
	}

	public String getVsIndex()
	{
		return vsIndex;
	}

	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
	}

	public String getVsIp()
	{
		return vsIp;
	}

	public void setVsIp(String vsIp)
	{
		this.vsIp = vsIp;
	}

	public String getVsName()
	{
		return vsName;
	}

	public void setVsName(String vsName)
	{
		this.vsName = vsName;
	}

	public String getHwCheckInString()
	{
		return hwCheckInString;
	}

	public void setHwCheckInString(String hwCheckInString)
	{
		this.hwCheckInString = hwCheckInString;
	}

	public String getL23CheckInString()
	{
		return l23CheckInString;
	}

	public void setL23CheckInString(String l23CheckInString)
	{
		this.l23CheckInString = l23CheckInString;
	}

	public String getL47CheckInString()
	{
		return l47CheckInString;
	}

	public void setL47CheckInString(String l47CheckInString)
	{
		this.l47CheckInString = l47CheckInString;
	}

	public String getSvcCheckInString()
	{
		return svcCheckInString;
	}

	public void setSvcCheckInString(String svcCheckInString)
	{
		this.svcCheckInString = svcCheckInString;
	}

	public ArrayList<OBDtoElement> getHwCheckItems()
	{
		return hwCheckItems;
	}

	public void setHwCheckItems(ArrayList<OBDtoElement> hwCheckItems)
	{
		this.hwCheckItems = hwCheckItems;
	}

	public ArrayList<OBDtoElement> getL23CheckItems()
	{
		return l23CheckItems;
	}

	public void setL23CheckItems(ArrayList<OBDtoElement> l23CheckItems)
	{
		this.l23CheckItems = l23CheckItems;
	}

	public ArrayList<OBDtoElement> getL47CheckItems()
	{
		return l47CheckItems;
	}

	public void setL47CheckItems(ArrayList<OBDtoElement> l47CheckItems)
	{
		this.l47CheckItems = l47CheckItems;
	}

	public ArrayList<OBDtoElement> getSvcCheckItems()
	{
		return svcCheckItems;
	}

	public void setSvcCheckItems(ArrayList<OBDtoElement> svcCheckItems)
	{
		this.svcCheckItems = svcCheckItems;
	}

	public Integer getEveryHr()
	{
		return everyHr;
	}

	public void setEveryHr(Integer everyHr)
	{
		this.everyHr = everyHr;
	}

	public Integer getEveryMin()
	{
		return everyMin;
	}

	public void setEveryMin(Integer everyMin)
	{
		this.everyMin = everyMin;
	}
	
	public Integer getEveryDayOfWeek()
	{
		return everyDayOfWeek;
	}

	public void setEveryDayOfWeek(Integer everyDayOfWeek)
	{
		this.everyDayOfWeek = everyDayOfWeek;
	}

	public Integer getEveryDayOfMonth()
	{
		return everyDayOfMonth;
	}

	public void setEveryDayOfMonth(Integer everyDayOfMonth)
	{
		this.everyDayOfMonth = everyDayOfMonth;
	}

	public Integer getEveryDayMonth()
	{
		return everyDayMonth;
	}

	public void setEveryDayMonth(Integer everyDayMonth)
	{
		this.everyDayMonth = everyDayMonth;
	}

	public Integer getScheduleType()
	{
		return scheduleType;
	}

	public void setScheduleType(Integer scheduleType)
	{
		this.scheduleType = scheduleType;
	}
	
	public Integer getSvcFlag()
	{
		return svcFlag;
	}

	public void setSvcFlag(Integer svcFlag)
	{
		this.svcFlag = svcFlag;
	}

	public Integer getFlbFaultMaxDays()
	{
		return flbFaultMaxDays;
	}

	public void setFlbFaultMaxDays(Integer flbFaultMaxDays)
	{
		this.flbFaultMaxDays = flbFaultMaxDays;
	}

	@Override
	public String toString()
	{
		return "FaultSettingAction [faultSettingFacade=" + faultSettingFacade + ", adc=" + adc + ", orderDir=" + orderDir + ", orderType=" + orderType + ", curCategory=" + curCategory + ", adcObject=" + adcObject + ", searchObj=" + searchObj + ", orderObj=" + orderObj + ", checkSpeed=" + checkSpeed + ", faultCheckTempleteList=" + faultCheckTempleteList + ", faultVirtualSvcList=" + faultVirtualSvcList + ", faultCheckIndex=" + faultCheckIndex + ", faultCheckTemplate=" + faultCheckTemplate + ", faultClientIpList=" + faultClientIpList + ", faultCheckScheduleList=" + faultCheckScheduleList + ", faultCheckScheduleInfo=" + faultCheckScheduleInfo + ", scheduleIndex=" + scheduleIndex + ", templateIndex=" + templateIndex + ", templateNm=" + templateNm + ", faultMaxDays=" + faultMaxDays + ", adcLogCount=" + adcLogCount + ", clientIp=" + clientIp + ", svcValue=" + svcValue + ", vsIndex=" + vsIndex + ", vsIp=" + vsIp + ", vsName=" + vsName + ", hwCheckInString=" + hwCheckInString + ", l23CheckInString=" + l23CheckInString + ", l47CheckInString=" + l47CheckInString + ", svcCheckInString=" + svcCheckInString + ", hwCheckItems=" + hwCheckItems + ", l23CheckItems=" + l23CheckItems + ", l47CheckItems=" + l47CheckItems + ", svcCheckItems=" + svcCheckItems + ", everyHr=" + everyHr + ", everyMin=" + everyMin + ", everyDayOfWeek=" + everyDayOfWeek + ", everyDayOfMonth=" + everyDayOfMonth + ", everyDayMonth=" + everyDayMonth + ", scheduleType=" + scheduleType + "]";
	}
}
