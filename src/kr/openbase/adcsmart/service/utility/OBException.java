package kr.openbase.adcsmart.service.utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Properties;

public class OBException extends Exception implements Serializable 
{
	private static Properties msgProps=null;// = LoggerFactory.getLogger(AlertFacade.class);
	private	int		errorCode;
	private String  errorMessage;
	private String  callstack;
	
	public static final String	ERRCODE_LICENSE_EXPIRED	  		= "ERRCODE_LICENSE_EXPIRED";//
	public static final String	ERRCODE_LICENSE_FORMAT	  		= "ERRCODE_LICENSE_FORMAT";//
	public static final String	ERRCODE_SYSTEM_FILE_NOT_FOUND	= "ERRCODE_SYSTEM_FILE_NOT_FOUND";//
	public static final String	ERRCODE_SYSTEM_FILE_MOVE	  = "ERRCODE_SYSTEM_FILE_MOVE";//
	public static final String	ERRCODE_INVALID_PORT_INFO	  = "ERRCODE_INVALID_PORT_INFO";//
	
	public static final String	ERRCODE_NOT_SUPPORT_VENDOR	  = "ERRCODE_NOT_SUPPORT_VENDOR";//비정상적인 VIRTUAl SERVER ID가 검출되었습니다.
	public static final String	ERRCODE_GET_PORT_INFO		  = "ERRCODE_GET_PORT_INFO";//포트 인터페이스 정보 추출에 실패했습니다.
	public static final String	ERRCODE_SEND_COMMAND		  = "ERRCODE_SEND_COMMAND";//CLI 명령 전송에 실패했습니다.
	
	public static final String 	ERRCODE_ADC_EXIST			  = "ERRORCODE_SAME_ADC_EXIST";//{"1014", "동일한 이름의 ADC 장비가 존재합니다.", "다시 시도해 보십시오"};
	public static final String 	ERRCODE_ADC_SYSLOG		      = "ERRORCODE_ADC_LOGCOUNT_QUERY";//{"1015", "ADC 로그 조회에 실패했습니다.", "다시 시도해 보십시오"};
	public static final String 	ERRCODE_ADC_COUNT			  = "ERRORCODE_ADC_COUNT_QUERY";//{"1016", "ADC 장비 개수 조회에 실패했습니다.", "다시 시도해 보십시오"};
	public static final String 	ERRCODE_ADC_VENDOR		      = "ERRORCODE_NOT_SUPPORT_ADCVENDER";//{"1018", "지원하지 않은 ADC 벤더입니다. ", "재 확인 후 다시 시도해 보십시오"};
	public static final String 	ERRCODE_ADC_VERSION		      = "ERRORCODE_NOT_SUPPORT_SWVERSION";//{"1020", "지원하지 않은 S/W 버전입니다. ", "지원 목록을 확인하시기 바랍니다"};
	public static final String 	ERRCODE_ADC_INFO		      = "ERRCODE_ADC_INFO";//{"1021", "ADC 정보 조회에 실패했습니다. DB 연결 또는 등록되지 않은 ADC일 가능성이 있습니다. ", "다시 시도해 보십시오."};
	public static final String 	ERRCODE_ADC_CONNECTION        = "ERRORCODE_ADC_CONN";//{"1022", "ADC 접속에 실패했습니다. 일시적인 네트워크 장애를 가능성이 있습니다. 연결 점검해 보십시오. ", "다시 시도해 보십시오."};
	
	public static final String 	ERRCODE_SLB_VS_TIME 		  = "ERRORCODE_VS_TIME";//설정을 시도하기 전에 설정 변경 내역이 있습니다. 다시 설정해 주십시오.(Sync failed)
	public static final String	ERRCODE_LICENSE_INVALID	      = "ERRCODE_LICENSE_INVALID";//{"1502", "라이선스가 정상적이지 않습니다.", "정상적인 라이선스를 적용하십시오."};
	public static final String	ERRCODE_DB_QEURY			  = "ERRCODE_DB_QEURY";//{"1601", "DB 쿼리에 실패했습니다.", "DB 상태를 점검하십시오."};
	public static final String	ERRCODE_DB_OPEN			      = "ERRCODE_DB_OPEN";//{"1603", "DB 오픈에 실패했습니다.", "DB 상태를 점검하십시오."};
	
	public static final String	ERRCODE_SYSCFG_FILTERSYNTAX   = "ERRCODE_SYSCFG_FILTERSYNTAX";//{"1816", "ADC 로그 필터링 패턴의 문법 오류입니다.", "다시 시도해 보십시오."};
	public static final String	ERRCODE_BACKUP_SQLTYPE 	  	  = "ERRCODE_BACKUP_SQLTYPE";// {"2109", "지원되지 않은 SQL 형식입니다.", ""};
	public static final String	ERRCODE_FAULT_DUP_SCHEDULE  = "ERRORCODE_ALREADY_REG_FAILCHECK_RESERVE";//{"3002", "이미 등록된 장애검사 예약입니다. ", "다시 시도해 보십시오."};
	
	public static final String	ERRCODE_SYSTEM_PARAMETER	  = "ERRCODE_SYSTEM_PARAMETER";//{"5001", "입력인자가 비정상적입니다.", "다시 시도해 보십시오."};
	public static final String	ERRCODE_SYSTEM_FILENOTFOUND   = "ERRORCODE_FILE_DIRECTORY_NOT_FIND";//{"5002", "파일이나 디렉토리를 찾을 수 없습니다.", "다시 시도해 보십시오."};
	public static final String	ERRCODE_SYSTEM_DATAENCRYPT    = "ERRCODE_SYSTEM_DATAENCRYPT";//{"5004", "데이터 암호화에 실패했습니다.", "암호화할 데이터를 확인하신 후 다시 시도해 보십시오."};
	public static final String	ERRCODE_SYSTEM_UNREACHABLE    = "ERRCODE_SYSTEM_UNREACHABLE";//{"5005", "지정된 장비에 접속할 수 없습니다.",  "지정된 장비가 동작중이며 네트워크가 연결되어 있는지 확인해 보십시오."};
	public static final String	ERRCODE_SYSTEM_LOGIN		  = "ERRCODE_SYSTEM_LOGIN";//{"5006", "지정된 장비에 로그인할 수 없습니다.", "장비의 아이디 및 패스워드를 확인하십시오."};
//	public static final String	ERRCODE_SYSTEM_CLILOGIN		  = "ERRCODE_SYSTEM_CLILOGIN";//{"5006", F5에서만 사용 ,"F5 CLI Authorization Required."};
	public static final String	ERRCODE_SYSTEM_ILLEGALNULL    = "ERRCODE_SYSTEM_ILLEGALNULL";//{"5008", "비정상적인 포멧 또는 널포인트 오류입니다.", "다시 시도해 보십시오."};
	public static final String	ERRCODE_SYSTEM_GETSNMP        = "ERRCODE_SYSTEM_GETSNMP";//{"5010", "지정된 장비로부터 SNMP 데이터를 가져올 수 없습니다.", "IP, OID, Community 정보를 확인하시기 바랍니다."};
	public static final String	ERRCODE_SYSTEM_INVALIDDATA    = "ERRCODE_SYSTEM_INVALIDDATA";//{"5011", "수집된 데이터가 정상적이지 않습니다.", "다시 시도해 보십시오"};
	public static final String	ERRCODE_SYSTEM_DATA_NOT_EXIST = "ERRORCODE_DATA_NOT_EXIST";//{"5012", "데이터가 존재하지 않습니다. ", "다시 시도해 보십시오"};
	public static final String	ERRCODE_SYSTEM_CREATE_THREAD  = "ERRORCODE_PROCESS_CREATE";//{"5013", "처리 프로세스 생성에 실패했습니다.", "다시 시도해 보십시오"};
	public static final String	ERRCODE_SYSTEM_COMM_READ      = "ERRORCODE_SOCKET_ERROR_DATA_READ";//{"5014", "소켓 통신 오류입니다. 데이터 읽기에 실패했습니다.", "다시 시도해 보십시오"};
	public static final String	ERRCODE_SYSTEM_COMM_ACK       = "ERRORCODE_SOCKET_ERROR_ACK_DATA_READ";//{"5015", "소켓 통신 오류입니다. ACK 데이터 읽기에 실패했습니다.", "다시 시도해 보십시오"};

	public static final String	ERRCODE_GET_FAULT_CHECK_INFO  = "ERRCODE_GET_FAULT_CHECK_INFO";//{"6001", "장애진단 정보 추출에 실패했습니다.", ""};
	public static final String	ERRCODE_GET_FAULT_TEMPLATE_INFO= "ERRORCODE_FAIL_DIAGNOSIS_TEMPLET_INFO";//{"6002", "장애진단 템플릿 정보 추출에 실패했습니다.", ""};
	
	public static final String	ERRCODE_LOCK_FILE_EXCEPTION   = "ERRCODE_LOCK_FILE_EXCEPTION";//현재 SLB 정보가 업데이트 중입니다. 잠시 후에 다시 시도해 보시기 바랍니다" 라는 문구를 제공하고 종료한다.;
	public static final String	ERRCODE_SLB_MOST_RECENT  	  = "ERRCODE_SLB_MOST_RECENT";//현재 SLB가 최신 정보 입니다.;
	public static final String  ERRCODE_ADCADD_SLB_LOCK_EXCEPTION = "ERRCODE_ADCADD_SLB_LOCK_EXCEPTION";  // ADC 추가에 성공하였습니다. SLB정보는 현재 동기화중입니다. 잠시 후에 확인해보시기 바랍니다. 
	
	static final long serialVersionUID 			= -3781729165235737608L;

	public String stackTraceToString(StackTraceElement[] stackTraceElements)
	{
		String retVal = "";
		for(int i=2;i<stackTraceElements.length;i++)
		{
			StackTraceElement s=stackTraceElements[i];
			retVal = retVal + s.toString() + "\n\t\t";
		}
		return retVal;
	}
	
	public OBException(String msgName)
	{
		this.errorMessage = OBException.getExceptionMessage(msgName);

		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String className=stackTraceElements[2].getClassName();
		String methodName=stackTraceElements[2].getMethodName();
		String moduleName = className + "::" + methodName;
		this.callstack = stackTraceToString(stackTraceElements);
		
		OBSystemLog.error3(OBDefine.LOGFILE_SYSTEM, String.format("msg:%s, module:%s", this.errorMessage, moduleName));
	}
	
	public synchronized static String getExceptionMessage(String code)
	{
		try
		{
			if(msgProps==null)
			{
				String langCode = OBCommon.getLocalInfo();
				String language = OBCommon.getLanguageCode(langCode);
				String country = OBCommon.getCountryCode(langCode);
				String fileName = String.format(OBDefine.PROPERTIES_Errors,  language, country);
				String fullPathFileName = OBUtility.getPropOsPath()+fileName;
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fullPathFileName), "UTF8"));
				msgProps = new Properties();
				msgProps.load(in);
			}
			String retVal = msgProps.getProperty(code).trim();
			if(retVal==null)
			{
				OBSystemLog.error3(OBDefine.LOGFILE_SYSTEM, String.format("Exception. not defined. code:%s", code));
				return "not defined";
			}
			return retVal;
		}
		catch(Exception e)
		{
			OBSystemLog.error3(OBDefine.LOGFILE_SYSTEM, String.format("Exception. not defined. code:%s. exception occured:%s. stack:%s", code, e.getMessage(), new OBUtility().getStackTrace()));
			return "not defined";
		}
	}
	
	public OBException()
	{
	}

	public OBException(String msgName, String extraMsg)
	{
		this.errorMessage = OBException.getExceptionMessage(msgName);
		this.errorMessage += "(" + extraMsg + ")";
		
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String className=stackTraceElements[2].getClassName();
		String methodName=stackTraceElements[2].getMethodName();
		String moduleName = className + "::" + methodName;
		this.callstack = stackTraceToString(stackTraceElements);
		OBSystemLog.error3(OBDefine.LOGFILE_SYSTEM, String.format("msg:%s, module:%s", this.errorMessage, moduleName));
	}
	
	public int getErrorCode()
	{
		return this.errorCode;
	}

	public String getErrorMessage()
	{
		return this.errorMessage;
	}

	public String getCallstack()
	{
		return this.callstack;
	}
	
	@Override
	public String getMessage() 
	{
		return this.errorMessage;
	}
}
