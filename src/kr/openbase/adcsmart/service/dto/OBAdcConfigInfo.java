package kr.openbase.adcsmart.service.dto;

public class OBAdcConfigInfo
{
	public static final int CONFIG_ID_VSTAT 			= 1;// vstat 설정 상태.   	
	public static final int CONFIG_ID_SNMP_ENABLED 		= 11;// snmp 설정 상태.  	
	public static final int CONFIG_ID_SNMP_COMMUNITY 	= 12;// snmp 설정 상태.  	
	public static final int CONFIG_ID_SNMP_ALLOWLIST 	= 13;// snmp 설정 상태.  only f5	
	public static final int CONFIG_ID_SYSLOG_ENABLED 	= 21;// syslog 설정 상태. only alteon
	public static final int CONFIG_ID_SYSLOG_IPADDR 	= 22;// syslog 설정 상태.
	public static final int CONFIG_ID_HTTPS_ALLOWLIST 	= 31;// HTTPS 설정 상태. only f5
	public static final int CONFIG_ID_SSH_ALLOWLIST 	= 32;// SSH 설정 상태. only f5
	
	public static final int CONFIG_ID_SNMP_USER		 	= 14;// snmp 설정 상태. only v3  	
	public static final int CONFIG_ID_SNMP_AUTHPW 		= 15;// snmp 설정 상태. only v3 	 
	public static final int CONFIG_ID_SNMP_PRIVPW	 	= 16;// snmp 설정 상태. only v3 	
	
	public static final int STATUS_INIT 		= 0;// 초기상태.
	public static final int STATUS_NORMAL 		= 1;// 정상.
	public static final int STATUS_ABNORMAL		= 2;// 비정상.
	public static final int STATUS_FAIL 		= 3;// 점검 실패.
	
	private Integer configID;
	private Integer status;
	private String  localInfo;// 로컬 장비에 설정된 내용.
	private String  adcInfo;// ADC 장비에 설정된 내용.
	@Override
	public String toString()
	{
		return "OBAdcConfigInfo [configID=" + configID + ", status=" + status + ", localInfo=" + localInfo + ", adcInfo=" + adcInfo + "]";
	}
	public Integer getConfigID()
	{
		return configID;
	}
	public void setConfigID(Integer configID)
	{
		this.configID = configID;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public String getLocalInfo()
	{
		return localInfo;
	}
	public void setLocalInfo(String localInfo)
	{
		this.localInfo = localInfo;
	}
	public String getAdcInfo()
	{
		return adcInfo;
	}
	public void setAdcInfo(String adcInfo)
	{
		this.adcInfo = adcInfo;
	}
}
