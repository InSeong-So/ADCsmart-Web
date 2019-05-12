package kr.openbase.adcsmart.service.impl;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBAdcConfigInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBAdcAlteonHandler;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;

public class OBCheckAdcConfig
{
	public boolean setAdcConfigInfo(Integer adcIndex, OBAdcConfigInfo adcCheckInfo, OBDatabase db) throws OBException
	{
		boolean retVal = false;
		OBDtoAdcInfo adcInfo = null;
		try
		{
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
		}
		catch(OBException e)
		{
			throw e;
		}
		
		try
		{
			switch (adcInfo.getAdcType())
			{
			case OBDefine.ADC_TYPE_ALTEON:
				retVal = setAdcConfigInfoAlteon(adcInfo, adcCheckInfo, db);
				break;
			case OBDefine.ADC_TYPE_F5:
				retVal = setAdcConfigInfoF5(adcInfo, adcCheckInfo, db);
				break;
			case OBDefine.ADC_TYPE_PIOLINK_PAS:
				retVal = setAdcConfigInfoPAS(adcInfo, adcCheckInfo, db);
				break;
			case OBDefine.ADC_TYPE_PIOLINK_PASK:
				retVal = setAdcConfigInfoPASK(adcInfo, adcCheckInfo, db);
				break;
			case OBDefine.ADC_TYPE_PIOLINK_UNKNOWN:
				retVal = setAdcConfigInfoPASUnknown(adcInfo, adcCheckInfo, db);
				break;
			default:
				throw new OBException(OBException.ERRCODE_NOT_SUPPORT_VENDOR);
			}
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}
	
	private boolean setAdcConfigInfoAlteon(OBDtoAdcInfo adcInfo, OBAdcConfigInfo adcCheckInfo, OBDatabase db) throws OBException
	{
		boolean retVal = false;
		try
		{
			switch (adcCheckInfo.getConfigID())
			{
			case OBAdcConfigInfo.CONFIG_ID_VSTAT:
				retVal = setAdcConfigInfoAlteonVstat(adcInfo, adcCheckInfo, db);
				break;
			case OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED:
				retVal = setAdcConfigInfoAlteonSnmpEnabled(adcInfo, adcCheckInfo, db);
				break;
			case OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY:
				retVal = setAdcConfigInfoAlteonSnmpCommunity(adcInfo, adcCheckInfo, db);
				break;
			case OBAdcConfigInfo.CONFIG_ID_SYSLOG_ENABLED:
				retVal = setAdcConfigInfoAlteonSyslogEnabled(adcInfo, adcCheckInfo, db);
				break;
			case OBAdcConfigInfo.CONFIG_ID_SYSLOG_IPADDR:
				retVal = setAdcConfigInfoAlteonSyslogHost(adcInfo, adcCheckInfo, db);
				break;
			default:
				throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, adcCheckInfo.getConfigID().toString());
			}
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}
	
	// 지정된 장비에 접근하여 vstat을 enable한다.
	private boolean setAdcConfigInfoAlteonVstat(OBDtoAdcInfo adcInfo, OBAdcConfigInfo adcCheckInfo, OBDatabase db) throws OBException
	{
		OBAdcAlteonHandler handler = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
		handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
		
		try
		{
			handler.login();
		}
		catch(OBExceptionUnreachable e)
		{// 로그인 실패한 경우임.
			return false;
		}
		catch(OBExceptionLogin e)
		{// 로그인 실패한 경우임.
			return false;
		}
		catch(Exception e)
		{// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		// vstat 상태 변경.
		try
		{
			handler.cmndCfgSlbVstat("e");// enable
			handler.cmndApply();
		}
		catch(OBException e)
		{// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		}
		catch(Exception e)
		{// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		handler.disconnect();
		return true;
	}
	
	// 지정된 장비에 접근하여 snmp를 enabled한다.
	private boolean setAdcConfigInfoAlteonSnmpEnabled(OBDtoAdcInfo adcInfo, OBAdcConfigInfo adcCheckInfo, OBDatabase db) throws OBException
	{
		OBAdcAlteonHandler handler = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
		handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
		
		try
		{
			handler.login();
		}
		catch(OBExceptionUnreachable e)
		{// 로그인 실패한 경우임.
			return false;
		}
		catch(OBExceptionLogin e)
		{// 로그인 실패한 경우임.
			return false;
		}
		catch(Exception e)
		{// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		// snmp enabled 상태 변경.
		try
		{
			handler.cmndCfgSnmpSnmpv3("enable");
			handler.cmndApply();
		}
		catch(OBException e)
		{// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		}
		catch(Exception e)
		{// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		handler.disconnect();
		return true;
	}
	
	// 지정된 장비에 접근하여 snmp community 변경한다.
	private boolean setAdcConfigInfoAlteonSnmpCommunity(OBDtoAdcInfo adcInfo, OBAdcConfigInfo adcCheckInfo, OBDatabase db) throws OBException
	{
		OBAdcAlteonHandler handler = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
		handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
		
		try
		{
			handler.login();
		}
		catch(OBExceptionUnreachable e)
		{// 로그인 실패한 경우임.
			return false;
		}
		catch(OBExceptionLogin e)
		{// 로그인 실패한 경우임.
			return false;
		}
		catch(Exception e)
		{// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		// snmp string 변경.
		try
		{
			handler.cmndCfgSnmpRcomm(adcInfo.getSnmpRComm());
			handler.cmndApply();
		}
		catch(OBException e)
		{// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		}
		catch(Exception e)
		{// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		handler.disconnect();
		return true;
	}
	
	// 지정된 장비에 접근하여 syslog를 enable한다.
	private boolean setAdcConfigInfoAlteonSyslogEnabled(OBDtoAdcInfo adcInfo, OBAdcConfigInfo adcCheckInfo, OBDatabase db) throws OBException
	{
		OBAdcAlteonHandler handler = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
		handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
		
		try
		{
			handler.login();
		}
		catch(OBExceptionUnreachable e)
		{// 로그인 실패한 경우임.
			return false;
		}
		catch(OBExceptionLogin e)
		{// 로그인 실패한 경우임.
			return false;
		}
		catch(Exception e)
		{// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		// syslog enabled
		try
		{
			handler.cmndCfgSysSyslog("all enable");
			handler.cmndApply();
		}
		catch(OBException e)
		{// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		}
		catch(Exception e)
		{// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		handler.disconnect();
		return true;
	}
	
	// 지정된 장비에 접근하여 syslog host를 지정한다. 
	private boolean setAdcConfigInfoAlteonSyslogHost(OBDtoAdcInfo adcInfo, OBAdcConfigInfo adcCheckInfo, OBDatabase db) throws OBException
	{
		return false;/// ip가 여러개임으로 지정하기 어렵다.
//		OBAdcAlteonHandler handler = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
//		handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
//		
//		try
//		{
//			handler.login();
//		}
//		catch(OBExceptionUnreachable e)
//		{// 로그인 실패한 경우임.
//			return false;
//		}
//		catch(OBExceptionLogin e)
//		{// 로그인 실패한 경우임.
//			return false;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		
//		// syslog host
//		try
//		{
//			handler.cmndCfgSysSyslog("all enable");
//			handler.cmndApply();
//		}
//		catch(OBException e)
//		{// 검사에 실패한 경우임.
//			handler.disconnect();
//			throw e;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			handler.disconnect();
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		
//		handler.disconnect();
//		return true;
	}
	
	private boolean setAdcConfigInfoF5(OBDtoAdcInfo adcInfo, OBAdcConfigInfo adcCheckInfo, OBDatabase db) throws OBException
	{
		boolean retVal = false;
		//TODO
		return retVal;
	}
	private boolean setAdcConfigInfoPAS(OBDtoAdcInfo adcInfo, OBAdcConfigInfo adcCheckInfo, OBDatabase db) throws OBException
	{
		boolean retVal = false;
		//TODO
		return retVal;
	}
	private boolean setAdcConfigInfoPASK(OBDtoAdcInfo adcInfo, OBAdcConfigInfo adcCheckInfo, OBDatabase db) throws OBException
	{
		boolean retVal = false;
		//TODO
		return retVal;
	}
	
	private boolean setAdcConfigInfoPASUnknown(OBDtoAdcInfo adcInfo, OBAdcConfigInfo adcCheckInfo, OBDatabase db) throws OBException
	{
		boolean retVal = false;
		//TODO
		return retVal;
	}
}
