package kr.openbase.adcsmart.service.dto;


public class OBAdcCheckResult
{
	public static final int CHECK_ID_PORTOPEN 		= 1;// 포트 오픈 테스트   
	public static final int CHECK_ID_LOGIN 			= 2;// 로그인 테스트  
	public static final int CHECK_ID_VERSION		= 3;// version  
	public static final int CHECK_ID_PORTREVERSE 	= 4;// 포트 오픈 테스트(역방향). alteon만 해당됨. 
	public static final int CHECK_ID_SNMP 			= 5;// snmp 수신 테스트 
	public static final int CHECK_ID_SYSLOG 		= 6;// syslog 수신 테스트. 
	
	public static final int CHECK_STATUS_INIT		= 0;// 상태: 초기.  
	public static final int CHECK_STATUS_OK			= 1;// 상태: 정상  
	public static final int CHECK_STATUS_FAIL 		= 2;// 상태: 실패.  
	public static final int CHECK_STATUS_ABNORMAL	= 3;// 상태: 비정상. ???
	
	private Integer checkID;
	private Integer status;
	private String  summary;
	private String  extraInfo;
	
	@Override
	public String toString()
	{
		return "OBAdcCheckResult [checkID=" + checkID + ", status=" + status + ", summary=" + summary + ", extraInfo=" + extraInfo + "]";
	}
	public String getExtraInfo()
	{
		return extraInfo;
	}
	public void setExtraInfo(String extraInfo)
	{
		this.extraInfo = extraInfo;
	}
	public Integer getCheckID()
	{
		return checkID;
	}
	public void setCheckID(Integer checkID)
	{
		this.checkID = checkID;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
}
