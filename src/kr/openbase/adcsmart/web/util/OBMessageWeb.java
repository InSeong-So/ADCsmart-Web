package kr.openbase.adcsmart.web.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBMessageWeb
{	
	private transient static Logger log = LoggerFactory.getLogger(OBMessageWeb.class);
	private static Properties msgProps=null;// = LoggerFactory.getLogger(AlertFacade.class);

	public static final String LOGIN_ALIVE_TIMEOUT 					= "LOGIN_ALIVE_TIMEOUT";			                // 동일 계정의 동시 접근은 금지되어 있습니다. 
	public static final String MSG_IP_DUPLICATED 					= "MSG_IP_DUPLICATED";				                // 동일한 IP가 등록되어 있습니다. 다르게 설정하십시오.                 
	public static final String MSG_ALTEONID_DUPLICATED 				= "MSG_ALTEONID_DUPLICATED";		                // 동일한 IP 혹은 Alteon ID가 등록되어 있습니다. 다르게 설정하십시오.   
	public static final String MSG_PEER_VSRV_EXIST 					= "MSG_PEER_VSRV_EXIST";			                // Peer 장비에 지정된 Virtual Server가 존재합니다. 다르게 설정하십시오.
	public static final String MSG_PEER_VSRV_NOT_EXIST 				= "MSG_PEER_VSRV_NOT_EXIST";		                // Peer 장비에 지정된 Virtual Server가 존재하지 않습니다.
	public static final String MSG_NAME_CHECK 						= "MSG_NAME_CHECK";					                // 입력하신 아이디 혹은 비밀번호가 일치하지 않습니다.
	public static final String MSG_NAME_DUPLICATED				 	= "MSG_NAME_DUPLICATED";			                // 동일한 이름이 등록되어 있습니다. 다르게 설정하십시오. 
	public static final String MSG_NAME_DUPLICATED_F5               = "MSG_NAME_DUPLICATED_F5";                         // 동일한 이름 또는 동일한 IP와 포트가 있습니다. 다르게 설정하십시오. 
	public static final String MSG_NAME_DUPLICATED_PEER 			= "MSG_NAME_DUPLICATED_PEER";		                // Peer 장비에 동일한 이름이 등록되어 있습니다. 다르게 설정하십시오. 
	public static final String MSG_NAME_EXIST_PEER 					= "MSG_NAME_EXIST_PEER";			                // Peer 장비에 존재하지 않습니다.
	public static final String MSG_PWD_CHECK 						= "MSG_PWD_CHECK";					                // 비밀번호가 일치하지 않습니다.
	public static final String MSG_PWD_VALIDATE 					= "MSG_PWD_VALIDATE";				                // 비밀번호와 비밀번호 확인 입력 값이 일치하지 않습니다.	    	
	public static final String MSG_LOGIN_FAIL 						= "MSG_LOGIN_FAIL";					                // 3번 이상 로그인을 실패하였습니다. 아이디와 비밀번호를 정확히 입력하십시오.	
	public static final String MSG_PROFILE_ADD 						= "MSG_PROFILE_ADD"; 				                // "Profile 추가 중 오류가 발생했습니다.";
	public static final String MSG_PROFILE_MODIFY 					= "MSG_PROFILE_MODIFY"; 			                // "Profile 수정 중 오류가 발생했습니다.";
	public static final String MSG_PROFILE_DEL 						= "MSG_PROFILE_DEL"; 				                // "Profile 삭제 중 오류가 발생했습니다.";
	public static final String MSG_PROFILE_RETRIEVE 				= "MSG_PROFILE_RETRIEVE"; 			                // "Profile 조회 중 오류가 발생했습니다.";
	public static final String MSG_ADC_RETRIEVE 					= "MSG_ADC_RETRIEVE"; 				                // "ADC 이력 개수를 조회하는 중 오류가 발생했습니다.";	
	public static final String MSG_ALERT_RETRIEVE 					= "MSG_ALERT_RETRIEVE"; 			                // "Alert을 조회하는 중 오류가 발생했습니다.";
	public static final String MSG_ALERT_SETTING 					= "MSG_ALERT_SETTING"; 				                // "Alert 설정을 조회하는 중 오류가 발생했습니다.";
	public static final String MSG_ALERT_CHECK 						= "MSG_ALERT_CHECK"; 				                // "Alert을 확인 처리하는 중 오류가 발생했습니다.";
	public static final String MSG_SYSTEM_ERR 						= "MSG_SYSTEM_ERR"; 				                // "시스템에 오류가 있습니다. 관리자에게 문의하십시오.";
	public static final String MSG_DISABLE_FAIL 					= "MSG_DISABLE_FAIL"; 				                // "Virtual Server Disable 중 오류가 발생하였습니다.\n관리자에게 문의 하십시오.";
	public static final String MSG_CONFIRM_ACTIVE_STANDBY 			= "MSG_CONFIRM_ACTIVE_STANDBY"; 	                // "SLB Config 설정에 성공했습니다. \nADC 장비에 Peer 장비가 등록되어 있습니다. (이름:%s, IP주소:%s)\n동기화 하시겠습니까?";
	public static final String MSG_CONFIG_PEER_SUCCESS 				= "MSG_CONFIG_PEER_SUCCESS"; 		                // "Peer ADC 장비의 SLB 설정에 성공했습니다. ";
	public static final String MSG_CONFIG_SLB_SUCCESS 				= "MSG_CONFIG_SLB_SUCCESS"; 		                // "SLB 설정에 성공했습니다. ";
	public static final String MSG_REVERT_SLB_SUCCESS 				= "MSG_REVERT_SLB_SUCCESS"; 		                // "SLB 복구에 성공했습니다. ";
	public static final String MSG_REVERT_CONFIRM 					= "MSG_REVERT_CONFIRM"; 			                // "복구를 진행할 경우 이전 설정으로 변경됩니다.\n복구를 진행하시겠습니까? ";
	public static final String MSG_REVERT_CHECK_ERROR				= "MSG_REVERT_CHECK_ERROR";			                // "복구할 수 없는 상태입니다. SLB 설정 항목이 아니거나 ADC 장비에서 변경된 경우에는 복구할 수 없습니다. ";
	public static final String MSG_REVERT_CONFIRM_ACTIVE_STANDBY	= "MSG_REVERT_CONFIRM_ACTIVE_STANDBY";		        // "ADC 장비에 Peer 장비가 등록되어 있습니다. (이름:%s, IP주소:%s)\nPeer 장비도 복구 하시겠습니까?";
	public static final String MSG_SYSTOOL_NEED_IPADDRESS   		= "MSG_SYSTOOL_NEED_IPADDRESS";   	                // "IP 주소를 입력하십시오.";
	public static final String MSG_SYSTOOL_NEED_ADCPASSWD   		= "MSG_SYSTOOL_NEED_ADCPASSWD";   	                // "비밀번호를 입력하십시오.";
	public static final String MSG_SYSTOOL_NOT_SUPPORT_ADC  		= "MSG_SYSTOOL_NOT_SUPPORT_ADC";  	                // "지원하지 않은 ADC 장비입니다. ";
	public static final String MSG_SYSTEM_FILE_NOT_FOUND    		= "MSG_SYSTEM_FILE_NOT_FOUND";    	                // "지정된 파일을 찾을 수 없습니다.";
	public static final String MSG_EXPORT_DATA_NOT_EXIST			= "MSG_EXPORT_DATA_NOT_EXIST";		                // "내보내기 자료가 없습니다.";
	public static final String MSG_DOWNLOAD_REPORT_NOT_EXIST 		= "MSG_DOWNLOAD_REPORT_NOT_EXIST"; 	                // "다운로드 가능한 보고서가 없습니다.";
	public static final String MSG_SLB_DOWNLOAD_FAIL	  			= "MSG_SLB_DOWNLOAD_FAIL";	  		                // "SLB 데이터 다운로드에 실패했습니다. \n네트워크 연결 상태를 확인하신 후 설정 동기화(SLB관리>>새로고침)하시기 바랍니다.";
	public static final String MSG_FLB_DOWNLOAD_FAIL	  			= "MSG_FLB_DOWNLOAD_FAIL";	  		                // "FLB 데이터 다운로드에 실패했습니다. \n네트워크 연결 상태를 확인하신 후 설정 동기화(FLB정보>>새로고침)하시기 바랍니다.";
	public static final String MSG_SESSION_SEARCH_KEYWORD   		= "MSG_SESSION_SEARCH_KEYWORD";   	                // "Source IP또는 Destination IP둘중 하나는 반드시 입력 하여야 합니다.";
	public static final String MSG_PACKET_DUMP_FILE_NOT_EXIST 		= "MSG_PACKET_DUMP_FILE_NOT_EXIST";                 // "다운로드 가능한 파일이 없습니다.";	
	public static final String MSG_EXPIRE_ACCOUNT					= "MSG_EXPIRE_ACCOUNT";				                // 사용기한이 아니거나 만료된 계정입니다.
	public static final String MSG_SAME_ACCOUNT_CANNOT_ACCESS		= "MSG_SAME_ACCOUNT_CANNOT_ACCESS";	                // 동일 계정의 동시 접근은 금지되어 있습니다. 아이디 (%s) 은(는) (%s) 에서 접근하여 사용되고 있습니다. 확인 후 재 시도하시기 바랍니다.
	public static final String MSG_NOTICE_GROUP_USING               = "MSG_NOTICE_GROUP_USING";                         // 공지그룹이 사용중입니다. 
	public static final String MSG_FCHKMSG_LICENSE_ABNORMAL         = "MSG_FCHKMSG_LICENSE_ABNORMAL";                   //라이센스 : 비정상	
	public static final String MSG_REALSERVERGROUP_DUPLICATED 		= "MSG_REALSERVERGROUP_DUPLICATED";	                // 동일한 RealServerGroup 이 등록되어 있습니다.
	public static final String MSG_REALSERVERGROUP_ADD 				= "MSG_REALSERVERGROUP_ADD";		                // 동일한 RealServerGroup 이 등록되어 있습니다.
	public static final String MSG_REALSERVERGROUP_MODIFY			= "MSG_REALSERVERGROUP_MODIFY";		                // Real Server Group이 수정되었습니다.
	public static final String MSG_REALSERVERGROUP_MOVE				= "MSG_REALSERVERGROUP_MOVE";		                // Real Server Group이 이동되었습니다.
	public static final String MSG_REALSERVERGROUP_DELETE           = "MSG_REALSERVERGROUP_DELETE";       	            //Real Server Group이 삭제되었습니다.
	public static final String MSG_FCHKMSG_LICENSE_INVALID_DATE            = "MSG_FCHKMSG_LICENSE_INVALID_DATE";        // ADCsmart 라이선스 기간이 만료되었습니다.
	public static final String MSG_FCHKMSG_LICENSE_INVALID_MAC             = "MSG_FCHKMSG_LICENSE_INVALID_MAC";         // MAC인증 오류 입니다.
	public static final String MSG_FCHKMSG_LICENSE_INVALID_ADC_CNT_ONE     = "MSG_FCHKMSG_LICENSE_INVALID_ADC_CNT_ONE"; // (ADCsmart
	public static final String MSG_FCHKMSG_LICENSE_INVALID_ADC_CNT_TWO     = "MSG_FCHKMSG_LICENSE_INVALID_ADC_CNT_TWO"; // 의 최대등록 대수는
	public static final String MSG_FCHKMSG_LICENSE_INVALID_ADC_CNT_THREE   = "MSG_FCHKMSG_LICENSE_INVALID_ADC_CNT_THREE"; // 대 입니다.)
	public static final String MSG_FCHKMSG_LICENSE_INVALID_ADC_CNT         = "MSG_FCHKMSG_LICENSE_INVALID_ADC_CNT";     // ADC 초과 등록 되었습니다.
	public static final String MSG_FCHKMSG_LICENSE_MOVE_PAGE               = "MSG_FCHKMSG_LICENSE_MOVE_PAGE";           // 라이선스 페이지로 이동하시겠습니까?
	public static final String MSG_ADC_CHECK_STATUS_MGMTMODE_FAIL          = "MSG_ADC_CHECK_STATUS_MGMTMODE_FAIL";      // 장비와 데이터 / MGMT 설정 모드가 다릅니다.
	public static final String MSG_SLB_CONFIG_NOT_SETTING                  = "MSG_SLB_CONFIG_NOT_SETTING";              // 건의 요청은 설정 불일치로 인해 실패했습니다.
	public static final String MSG_SLB_SETTING_FAIL_RESETTING              = "MSG_SLB_SETTING_FAIL_RESETTING";          // 동기화 되었으니 다시 설정해 주십시오.
	public static final String MSG_NOTICE_CONFIRM_ACTIVE_STANDBY           = "MSG_NOTICE_CONFIRM_ACTIVE_STANDBY";       // ADC 장비에 Peer 장비가 등록되어 있습니다. (이름:%s, IP주소:%s)\n공지전환을 진햏 하시겠습니까?
	public static final String MSG_NOTICE_PEER_VSRV_NOT_EXIST              = "MSG_NOTICE_PEER_VSRV_NOT_EXIST";          // Peer 장비에 지정된 Pool Member가 존재하지 않습니다.
	public static final String MSG_NOTICE_CONFIG_SLB_SUCCESS               = "MSG_NOTICE_CONFIG_SLB_SUCCESS";           // 공지전환에 성공했습니다.
    public static final String MSG_NOTICE_REVERT_SLB_SUCCESS               = "MSG_NOTICE_REVERT_SLB_SUCCESS";           // 공지복구에 성공했습니다.
        
	private String getLanguageCode(String langCode)
	{
		String elements[] = langCode.split("_");
		if(elements.length!=2)
			return "ko";// default language is korean
		return elements[0];
	}
	
	private String getCountryCode(String langCode)
	{
		String elements[] = langCode.split("_");
		if(elements.length!=2)
			return "KR";// default language is korean
		return elements[1];
	}

	public synchronized static String getMessageWeb(String code)
	{
		try
		{
			if(msgProps==null)
			{
				String langCode = OBCommon.getLocalInfo();
				String language = new OBMessageWeb().getLanguageCode(langCode);
				String country = new OBMessageWeb().getCountryCode(langCode);
				String fileName = String.format(OBDefine.PROPERTIES_MESSAGES,  language, country);
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(OBUtility.getPropOsPath()+fileName), "UTF8"));
				msgProps = new Properties();
				log.debug("{}", msgProps);								
				msgProps.load(in);
				log.debug("{}", msgProps.size());			
			}
			String retVal = msgProps.getProperty(code).trim();
			if(retVal==null)
			{
				log.debug("{}", code);
				return "not defined";
			}
			return retVal;
		}
		catch(Exception e)
		{
			log.debug("{}", code);
			return "not defined";
		}
	}
	
	public OBMessageWeb()
	{
	}	
}
