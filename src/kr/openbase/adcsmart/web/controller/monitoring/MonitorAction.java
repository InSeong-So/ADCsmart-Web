package kr.openbase.adcsmart.web.controller.monitoring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.adcman.AdcFacade;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcGroupDto;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope(value = "prototype")
public class MonitorAction extends BaseAction 
{
	private transient Logger log = LoggerFactory.getLogger(MonitorAction.class);
	
	@Autowired
	private AdcFacade adcFacade;
	
	private Map<String, AdcGroupDto> adcGroupMap;
	private Map<String, List<AdcDto>> adcGroupToAdcsMap;
	private int adcCount;
	private String searchKey = "";
	
	public String loadLeftPane() throws Exception 
	{
		prepareAdcGroupMapAndGroupToAdcsMap(searchKey);
		log.debug("adcGroupToAdcsMap: {}", adcGroupToAdcsMap);
		adcCount = 0;
		for (List<AdcDto> adcs : adcGroupToAdcsMap.values())
			adcCount += adcs.size();

		return SUCCESS;
	}
	
	private void prepareAdcGroupMapAndGroupToAdcsMap(String searchKey) throws Exception 
	{
		try 
		{
			adcGroupMap = adcFacade.getAdcGroupsMap();
			log.debug("{}", adcGroupMap);
			adcGroupToAdcsMap = adcFacade.getAdcGroupToAdcsMap(session.getAccountIndex(), StringUtils.isEmpty(searchKey) ? "" : searchKey);
			log.debug("{}", adcGroupToAdcsMap);
			removeUnunsedAdcGroupsFromAdcGroupMap(adcGroupToAdcsMap);
			log.debug("{}", adcGroupToAdcsMap);
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	
	private void removeUnunsedAdcGroupsFromAdcGroupMap(Map<String, List<AdcDto>> adcGroupToAdcsMap) 
	{
		for (String key : new ArrayList<String>(adcGroupMap.keySet()))
		{
			if (!adcGroupToAdcsMap.containsKey(key))
				adcGroupMap.remove(key);
		}
	}

	public Map<String, AdcGroupDto> getAdcGroupMap() 
	{
		return adcGroupMap;
	}

	public void setAdcGroupMap(Map<String, AdcGroupDto> adcGroupMap) 
	{
		this.adcGroupMap = adcGroupMap;
	}

	public Map<String, List<AdcDto>> getAdcGroupToAdcsMap() 
	{
		return adcGroupToAdcsMap;
	}

	public void setAdcGroupToAdcsMap(Map<String, List<AdcDto>> adcGroupToAdcsMap) 
	{
		this.adcGroupToAdcsMap = adcGroupToAdcsMap;
	}

	public int getAdcCount() 
	{
		return adcCount;
	}

	public void setAdcCount(int adcCount) 
	{
		this.adcCount = adcCount;
	}

	public String getSearchKey() 
	{
		return searchKey;
	}

	public void setSearchKey(String searchKey) 
	{
		this.searchKey = searchKey;
	}	
}
