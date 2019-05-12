package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigInfoF5;

public class AdcSettingF5Dto {
	private Date setTime;
	private VirtualSvrF5Dto virtualSvr;
	private String changeSummary;
	private String persistenceName;
	
//	public static void main(String [] args) 
//	{
//		try
//		{
//			System.out.println(AdcSettingF5Dto.toVsPersistenceName("2_b2bi_persistence"));
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	/**
	 * persistence 이름을 추출한다. 입력값은 persistence index값이 입력됨. index는 id_이름 형태로 구성. 예: 2_b2bi_persistence
	 * @param persistenceName
	 * @return
	 */
	public static String toVsPersistenceName(String persistenceName) 
	{
		if (persistenceName == null || persistenceName.isEmpty())
			return "";
		String element[] = persistenceName.split("_");// space를 기준으로 분리.
		if(element.length == 1)
		{
			return persistenceName;
		}

		String retVal = "";
		for(int i=1;i<element.length;i++)
		{
			if(!retVal.isEmpty())
				retVal += "_";
			retVal += element[i];
		}
		return retVal;
	}
	
	public static AdcSettingF5Dto toAdcSettingF5Dto(OBDtoAdcConfigInfoF5 adcSettingFromSvc) {
		if (adcSettingFromSvc == null)
			return null;
		
		AdcSettingF5Dto adcSetting = new AdcSettingF5Dto();
		adcSetting.virtualSvr = toVirtualSvrF5Dto(adcSettingFromSvc);
		adcSetting.setTime = adcSettingFromSvc.getLastTime();
		adcSetting.changeSummary = adcSettingFromSvc.getSummary();
		adcSetting.persistenceName = toVsPersistenceName(adcSettingFromSvc.getVsPersistenceName());// persistence 이름 변환 필수.
		return adcSetting;
	}

	private static VirtualSvrF5Dto toVirtualSvrF5Dto(OBDtoAdcConfigInfoF5 adcSettingFromSvc) {
		if (adcSettingFromSvc == null)
			return null;
		
		VirtualSvrF5Dto virtualSvr = new VirtualSvrF5Dto();
		virtualSvr.setName(adcSettingFromSvc.getVsName());
		virtualSvr.setVirtualIp(adcSettingFromSvc.getVsIPAddress());
		virtualSvr.setServicePort(adcSettingFromSvc.getVsServicePort());
		virtualSvr.setState(adcSettingFromSvc.getState());
		virtualSvr.setPool(AdcPoolDto.toAdcPoolDto(adcSettingFromSvc.getPool()));
		return virtualSvr;
	}

	public Date getSetTime() {
		return setTime;
	}

	public void setSetTime(Date setTime) {
		this.setTime = setTime;
	}

	public VirtualSvrF5Dto getVirtualSvr() {
		return virtualSvr;
	}

	public void setVirtualSvr(VirtualSvrF5Dto virtualSvr) {
		this.virtualSvr = virtualSvr;
	}

	public String getChangeSummary() {
		return changeSummary;
	}

	public void setChangeSummary(String changeSummary) {
		this.changeSummary = changeSummary;
	}

	public String getPersistenceName() {
		return persistenceName;
	}

	public void setPersistenceName(String persistenceName) {
		this.persistenceName = persistenceName;
	}

	@Override
	public String toString() {
		return "AdcSettingF5Dto [setTime=" + setTime + ", virtualSvr=" + virtualSvr + ", changeSummary="
				+ changeSummary + ", persistenceName=" + persistenceName + "]";
	}
	
}
