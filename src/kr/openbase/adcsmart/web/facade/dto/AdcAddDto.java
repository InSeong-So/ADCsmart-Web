package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.LoginFacade;

public class AdcAddDto 
{
	private AdcDto adc;
	private List<String> accountIds;
	private Boolean isStandbyEnabled = false;
	private String standbyIp;
	
//	public static OBDtoAdcInfo toOBDtoAdcInfo(AdcAddDto adcAdd) throws Exception {
	public static OBDtoAdcInfo toOBDtoAdcInfo(AdcAddDto adcAdd) throws OBException 
	{
		OBDtoAdcInfo adcFromSvc = AdcDto.toOBDtoAdcInfo(adcAdd.getAdc());
		try 
		{
			List<Integer> accountIndices = LoginFacade.retrieveAccountIndicesFromIds(adcAdd.getAccountIds());
			adcFromSvc.setAccountIndexList(new ArrayList<Integer>(accountIndices));
		} 
		catch (OBException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
//			throw new Exception(e);
		}
		
		return adcFromSvc;
	}
	
	public AdcDto getAdc() 
	{
		return adc;
	}
	public void setAdc(AdcDto adc) 
	{
		this.adc = adc;
	}
	public List<String> getAccountIds() 
	{
		return accountIds;
	}
	public void setAccountIds(List<String> accountIds) 
	{
		this.accountIds = accountIds;
	}
	public Boolean getIsStandbyEnabled() 
	{
		return isStandbyEnabled;
	}
	public void setIsStandbyEnabled(Boolean isStandbyEnabled) 
	{
		this.isStandbyEnabled = isStandbyEnabled;
	}
	public String getStandbyIp() 
	{
		return standbyIp;
	}
	public void setStandbyIp(String standbyIp) 
	{
		this.standbyIp = standbyIp;
	}

	@Override
	public String toString()
	{
		return "AdcAddDto [adc=" + adc + ", accountIds=" + accountIds + ", isStandbyEnabled=" + isStandbyEnabled + ", standbyIp=" + standbyIp + "]";
	}
	
}
