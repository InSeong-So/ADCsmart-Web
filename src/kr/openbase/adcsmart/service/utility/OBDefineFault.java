package kr.openbase.adcsmart.service.utility;

public class OBDefineFault
{
	//장애 type DEFINE. main 장애 유형
	public enum TYPE
	{
		SYSTEM_OFF       (1 , "systemOff",        LEVEL.CRITICAL, OBJECT_ADC,  "MSG_DEFFAULT_SYSTEM_OFF_TITLE", "MSG_DEFFAULT_SYSTEM_OFF_CONTENT", "MSG_DEFFAULT_SYSTEM_OFF_SYSLOG"),
		SYSTEM_ON        (8 , "systemOn",         LEVEL.INFO    , OBJECT_ADC,  "MSG_DEFFAULT_SYSTEM_ON_TITLE", "MSG_DEFFAULT_SYSTEM_ON_CONTENT", "MSG_DEFFAULT_SYSTEM_ON_SYSLOG"),
		VIRTUALSRV_DOWN  (2 , "virtualSrvDown",   LEVEL.CRITICAL, OBJECT_VIRTUALSERVER,"MSG_DEFFAULT_VIRTUALSRV_DOWN_TITLE", "MSG_DEFFAULT_VIRTUALSRV_DOWN_CONTENT", "MSG_DEFFAULT_VIRTUALSRV_DOWN_SYSLOG"),
		VIRTUALSRV_UP    (9 , "virtualSrvUp",     LEVEL.INFO    , OBJECT_VIRTUALSERVER,"MSG_DEFFAULT_VIRTUALSRV_UP_TITLE", "MSG_DEFFAULT_VIRTUALSRV_UP_CONTENT", "MSG_DEFFAULT_VIRTUALSRV_UP_SYSLOG"),
		POOLMEMS_DOWN    (3 , "poolMemsDown",     LEVEL.CRITICAL, OBJECT_MEMBER,       "MSG_DEFFAULT_POOLMEMS_DOWN_TITLE", "MSG_DEFFAULT_POOLMEMS_DOWN_CONTENT", "MSG_DEFFAULT_POOLMEMS_DOWN_SYSLOG"),
		POOLMEMS_UP      (10, "poolMemsUp",       LEVEL.INFO    , OBJECT_MEMBER,       "MSG_DEFFAULT_POOLMEMS_UP_TITLE", "MSG_DEFFAULT_POOLMEMS_UP_CONTENT", "MSG_DEFFAULT_POOLMEMS_UP_SYSLOG"),
		LINKS_DOWN       (4 , "linksDown",        LEVEL.CRITICAL, OBJECT_INTERFACE,    "MSG_DEFFAULT_LINKS_DOWN_TITLE", "MSG_DEFFAULT_LINKS_DOWN_CONTENT", "MSG_DEFFAULT_LINKS_DOWN_SYSLOG"),
		LINKS_UP         (11, "linksUp",          LEVEL.INFO    , OBJECT_INTERFACE,    "MSG_DEFFAULT_LINKS_UP_TITLE", "MSG_DEFFAULT_LINKS_UP_CONTENT", "MSG_DEFFAULT_LINKS_UP_SYSLOG"),
		BOOT             (5 , "boot",             LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_BOOT_TITLE", "MSG_DEFFAULT_BOOT_CONTENT", "MSG_DEFFAULT_BOOT_SYSLOG"),
		STANDBY          (6 , "standby",          LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_STANDBY_TITLE","MSG_DEFFAULT_STANDBY_CONTENT", "MSG_DEFFAULT_STANDBY_SYSLOG"),
		ACTIVE           (7 , "active",           LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_ACTIVE_TITLE", "MSG_DEFFAULT_ACTIVE_CONTENT", "MSG_DEFFAULT_ACTIVE_SYSLOG"),
		//이 위쪽은 다시 SUB_TYPE을 없애는 작업을 다시 해야한다.     
		CPU_USAGE_GENERAL(12, "cpuUsageGeneral",  LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_CPU_USAGE_GENERAL_TITLE", "MSG_DEFFAULT_CPU_USAGE_GENERAL_CONTENT", "MSG_DEFFAULT_CPU_USAGE_GENERAL_SYSLOG"),
		CPU_USAGE_MP     (13, "cpuUsageMp",       LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_CPU_USAGE_MP_TITLE", "MSG_DEFFAULT_CPU_USAGE_MP_CONTENT", "MSG_DEFFAULT_CPU_USAGE_MP_SYSLOG"),
		CPU_USAGE_SP     (46, "cpuUsageSp",       LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_CPU_USAGE_SP_TITLE", "MSG_DEFFAULT_CPU_USAGE_SP_CONTENT", "MSG_DEFFAULT_CPU_USAGE_SP_SYSLOG"),
		MEMORY           (14, "memory",           LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_MEMORY_TITLE", "MSG_DEFFAULT_MEMORY_CONTENT", "MSG_DEFFAULT_MEMORY_SYSLOG"),
		CONNECTION       (15, "connection",       LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_CONNECTION_TITLE", "MSG_DEFFAULT_CONNECTION_CONTENT", "MSG_DEFFAULT_CONNECTION_SYSLOG"),
		CONNECTION_LOW   (45, "connectionLow",    LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_CONNECTION_LOW_TITLE", "MSG_DEFFAULT_CONNECTION_LOW_CONTENT", "MSG_DEFFAULT_CONNECTION_LOW_SYSLOG"),
		SSL_TRANSACTION  (16, "sslTransaction",   LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_SSL_TRANSACTION_TITLE", "MSG_DEFFAULT_SSL_TRANSACTION_CONTENT", "MSG_DEFFAULT_SSL_TRANSACTION_SYSLOG"),
		UPTIME           (17, "uptime",           LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_UPTIME_TITLE", "MSG_DEFFAULT_UPTIME_CONTENT", "MSG_DEFFAULT_UPTIME_SYSLOG"),
		ADC_AGE          (18, "adcAge",           LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_ADC_AGE_TITLE", "MSG_DEFFAULT_ADC_AGE_CONTENT", "MSG_DEFFAULT_ADC_AGE_SYSLOG"),
		SSL_CERTIFICATE  (19, "sslCertificate",   LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_SSL_CERTIFICATE_TITLE", "MSG_DEFFAULT_SSL_CERTIFICATE_CONTENT", "MSG_DEFFAULT_SSL_CERTIFICATE_SYSLOG"),
		INTERFACE_ERROR  (20, "interfaceError",   LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_INTERFACE_ERROR_TITLE", "MSG_DEFFAULT_INTERFACE_ERROR_CONTENT", "MSG_DEFFAULT_INTERFACE_ERROR_SYSLOG"),
		INTERFACE_USAGE  (21, "interfaceUsage",   LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_INTERFACE_USAGE_TITLE", "MSG_DEFFAULT_INTERFACE_USAGE_CONTENT", "MSG_DEFFAULT_INTERFACE_USAGE_SYSLOG"),
		DUPLEX_FULL      (22, "duplexFull",       LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_DUPLEX_FULL_TITLE", "MSG_DEFFAULT_DUPLEX_FULL_CONTENT", "MSG_DEFFAULT_DUPLEX_FULL_SYSLOG"),
		DUPLEX_HALF      (23, "duplexHalf",       LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_DUPLEX_HALF_TITLE", "MSG_DEFFAULT_DUPLEX_HALF_CONTENT", "MSG_DEFFAULT_DUPLEX_HALF_SYSLOG"),
		SPEED_CHANGE     (24, "speedChange",      LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_SPEED_CHANGE_TITLE", "MSG_DEFFAULT_SPEED_CHANGE_CONTENT", "MSG_DEFFAULT_SPEED_CHANGE_SYSLOG"),
		ADC_CONFIG_BACKUP(25, "adcConfigBackup",  LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_ADC_CONFIG_BACKUP_TITLE", "MSG_DEFFAULT_ADC_CONFIG_BACKUP_CONTENT", "MSG_DEFFAULT_ADC_CONFIG_BACKUP_SYSLOG"),
		ADC_CONFIG_SYNC  (26, "adcConfigSync",    LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_ADC_CONFIG_SYNC_TITLE", "MSG_DEFFAULT_ADC_CONFIG_SYNC_CONTENT", "MSG_DEFFAULT_ADC_CONFIG_SYNC_SYSLOG"),

		FAN_STOP         (27, "fanStop",          LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_FAN_STOP_TITLE", "MSG_DEFFAULT_FAN_STOP_CONTENT", "MSG_DEFFAULT_FAN_STOP_SYSLOG"),
		TEMP_HIGH        (28, "tempHigh",         LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_TEMP_HIGH_TITLE", "MSG_DEFFAULT_TEMP_HIGH_CONTENT", "MSG_DEFFAULT_TEMP_HIGH_SYSLOG"),
		POWER_SINGLE     (29, "powerSingle",      LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_POWER_SINGLE_TITLE", "MSG_DEFFAULT_POWER_SINGLE_CONTENT", "MSG_DEFFAULT_POWER_SINGLE_SYSLOG"),
		CPU_TEMP_HIGH    (30, "cpuTempHigh",      LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_CPU_TEMP_HIGH_TITLE", "MSG_DEFFAULT_CPU_TEMP_HIGH_CONTENT", "MSG_DEFFAULT_CPU_TEMP_HIGH_SYSLOG"),
		CPU_FAN_SLOW     (31, "cpuFanSlow",       LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_CPU_FAN_SLOW_TITLE", "MSG_DEFFAULT_CPU_FAN_SLOW_CONTENT", "MSG_DEFFAULT_CPU_FAN_SLOW_SYSLOG"),
		CPU_FAN_BAD      (32, "cpuFanBad",        LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_CPU_FAN_BAD_TITLE", "MSG_DEFFAULT_CPU_FAN_BAD_CONTENT", "MSG_DEFFAULT_CPU_FAN_BAD_SYSLOG"),
		CHASSIS_TEMP_HIGH(33, "chassisTempHigh",  LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_CHASSIS_TEMP_HIGH_TITLE", "MSG_DEFFAULT_CHASSIS_TEMP_HIGH_CONTENT", "MSG_DEFFAULT_CHASSIS_TEMP_HIGH_SYSLOG"),
		CHASSIS_FAN_BAD  (34, "chassisFanBad",    LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_CHASSIS_FAN_BAD_TITLE", "MSG_DEFFAULT_CHASSIS_FAN_BAD_CONTENT", "MSG_DEFFAULT_CHASSIS_FAN_BAD_SYSLOG"),
		CHASSIS_POWER_BAD(35, "chassisPowerBad",  LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_CHASSIS_POWER_BAD_TITLE", "MSG_DEFFAULT_CHASSIS_POWER_BAD_CONTNENT", "MSG_DEFFAULT_CHASSIS_POWER_BAD_SYSLOG"),
		DOSATTACK_BLOCK  (36, "dosAttackBlock",   LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_DOSATTACK_BLOCK_TITLE", "MSG_DEFFAULT_DOSATTACK_BLOCK_CONTENT", "MSG_DEFFAULT_DOSATTACK_BLOCK_SYSLOG"),
		VOLTAGE_HIGH     (37, "voltageHigh",      LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_VOLTAGE_HIGH_TITLE", "MSG_DEFFAULT_VOLTAGE_HIGH_CONTENT", "MSG_DEFFAULT_VOLTAGE_HIGH_SYSLOG"),
		CHASSIS_FAN_LOW  (38, "chassisFanLow",    LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_CHASSIS_FAN_LOW_TITLE", "MSG_DEFFAULT_CHASSIS_FAN_LOW_CONTENT", "MSG_DEFFAULT_CHASSIS_FAN_LOW_SYSLOG"),
		VS_RESPONSE_TIME (39, "vsResponseTime",   LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_VS_RESPONSE_TIME_TITLE", "MSG_DEFFAULT_VS_RESPONSE_TIME_CONTENT", "MSG_DEFFAULT_VS_RESPONSE_TIME_SYSLOG"),
		REDUNDANCY_ACTIVE(40, "redundancyActive", LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_REDUNDANCY_ACTIVE_TITLE", "MSG_DEFFAULT_REDUNDANCY_ACTIVE_CONTENT", "MSG_DEFFAULT_REDUNDANCY_ACTIVE_SYSLOG"),
		REDUNDANCY_STANDBY(41,"redundancyStandby",LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_REDUNDANCY_STANDBY_TITLE", "MSG_DEFFAULT_REDUNDANCY_STANDBY_CONTENT", "MSG_DEFFAULT_REDUNDANCY_STANDBY_SYSLOG"),
		VRRP_COLLISION   (42, "vrrpCollision",    LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_VRRP_COLLISION_TITLE", "MSG_DEFFAULT_VRRP_COLLISION_CONTENT", "MSG_DEFFAULT_VRRP_COLLISION"),
		GATEWAY_FAIL_DOWN(43, "gatewayFailDown",  LEVEL.CRITICAL , OBJECT_ADC, "MSG_DEFFAULT_GATEWAY_FAIL_DOWN_TITLE", "MSG_DEFFAULT_GATEWAY_FAIL_DOWN_CONTENT", "MSG_DEFFAULT_GATEWAY_FAIL_DOWN_SYSLOG"),
		GATEWAY_FAIL_UP	 (44, "gatewayFailUp",    LEVEL.INFO , OBJECT_ADC,     "MSG_DEFFAULT_GATEWAY_FAIL_UP_TITLE", "MSG_DEFFAULT_GATEWAY_FAIL_UP_CONTENT", "MSG_DEFFAULT_GATEWAY_FAIL_UP_SYSLOG"),
		FILTER_SESSION_HIGH(47, "filterSessionHigh",LEVEL.WARNING , OBJECT_ADC,  "MSG_DEFFAULT_FILTER_SESSION_HIGH_TITLE", "MSG_DEFFAULT_FILTER_SESSION_HIGH_CONTENT", "MSG_DEFFAULT_FILTER_SESSION_HIGH_SYSLOG");

		private int code;
		private String name;
		private LEVEL level;
		private String title;
		private String content;
		//private String extra;
		private String syslogContent; //syslog. syslog로 전송시에는 영문으로 전송한다.
		private int objectType;
		//private String extraEng; //영문 extra message, syslog에서 씀
		
		private TYPE(int code, String name, LEVEL level, int objectType, String title, String content, String syslogContent)
		{
			this.code = code;
			this.name = name;
			this.level = level;
			this.title = title;
			this.objectType = objectType;
			this.content = content;
			//this.extra = extra;
			this.syslogContent = syslogContent;
		}

		public int getCode()
		{
			return code;
		}
	    public String getName()
        {
            return name;
        }
		public void setCode(int code)
		{
			this.code = code;
		}
		public String getTitle()
		{
			return OBMessages.getMessage(title);
		}
		public void setTitle(String title)
		{
			this.title = title;
		}
		public LEVEL getLevel()
		{
			return level;
		}
		public void setLevel(LEVEL level)
		{
			this.level = level;
		}
		public String getContent()
		{
			return OBMessages.getMessage(content);
		}
		public void setContent(String content)
		{
			this.content = content;
		}
		public String getSyslogContent()
		{
			return OBMessages.getMessage(syslogContent);
		}
		public void setSyslogContent(String syslogContent)
		{
			this.syslogContent = syslogContent;
		}
		public int getObjectType()
		{
			return objectType;
		}
		public void setObjectType(int objectType)
		{
			this.objectType = objectType;
		}
	}
	//위 enum class는 기존의 다음 define들과, mng_adc_fault_content를 합해서 재구성한다.
//	public static final int ADC_FAULT_SYSTEM = 1;// adc 시스템 reachable, unreachable
//	public static final int ADC_FAULT_VIRTSRV = 2;// virtual server up/down
//	public static final int ADC_FAULT_POOLMEMS = 3;// pool member up/down
//	public static final int ADC_FAULT_LINKS = 4;// link up/down
//	public static final String [] ADC_FAULT_TITLE = {"NONE", "ADC 끊김", "Virtual Server 끊김", "Pool Member 끊김", "ADC Link Down", "ADC Rebooting", "ADC가 Active에서 Standby로 전환" };
	//syslog를 보고 장애를 인식하는데 쓰는 패턴(DB TABLE = mng_syslog_filter)의 유형을 정의. mng_syslog_filter에 있는 CODE가 모두 여기 define과 맞게 존재해야 함
//	public static final int ADC_FAULT_VIRTSRV_UP = 1;        // virtual server up/down
//	public static final int ADC_FAULT_VIRTSRV_DOWN = 2;      // virtual server up/down
//	public static final int ADC_FAULT_POOLMEMS_UP = 3;       // pool member up/down
//	public static final int ADC_FAULT_POOLMEMS_DOWN = 4;     // pool member up/down
//	public static final int ADC_FAULT_LINKS_UP = 5;          // link up/down
//	public static final int ADC_FAULT_LINKS_DOWN = 6;        // link up/down
//	public static final int ADC_FAULT_OS_BOOT = 7;           // OS boot/reboot 
//	public static final int ADC_FAULT_ACTIVE_TO_STANDBY = 8; // ADC priority change - active -> standby
//	public static final int ADC_FAULT_STANDBY_TO_ACTIVE = 9; // ADC priority change - standby -> active

	public enum SYSLOG_FILTER_TYPE //table MNG_SYSLOG_FILTER.TYPE 필드 값. 상태 변화 감지는 보통 OBJECT_STATUS_CHANGE(3)로 한다.
	{
		ETC(1), //현재는 안 쓰는 잉여 패턴, 원래는 예외처리(drop)할 패턴들이었다.
		SLB_CHANGE(2), //apply, save, revert, virtual server create/modify/delete 등 slb 설정 변경 패턴
		OBJECT_STATUS_CHANGE(3); //pool member, virtual server, ADC 자체의 상태 변화 패턴 
		
		private int code;
		
		SYSLOG_FILTER_TYPE(int code)
		{
			this.code = code;
		}
		public int getCode()
		{
			return code;
		}
		public void setCode(int code)
		{
			this.code = code;
		}
	}

	public enum LEVEL //
	{
		INFO    (0, "INFO", OBSyslogDefine.LEVEL.INFO),
		WARNING (1, "WARNING", OBSyslogDefine.LEVEL.WARNING), 
		CRITICAL(2, "CRITICAL", OBSyslogDefine.LEVEL.CRIT), 
		FATAL   (3, "FATAL", OBSyslogDefine.LEVEL.CRIT);  
		
		private int code;
		private String caption;
		private OBSyslogDefine.LEVEL syslogLevel;
		
		LEVEL(int code, String caption, OBSyslogDefine.LEVEL syslogLevel)
		{
			this.code = code;
			this.caption = caption;
			this.syslogLevel = syslogLevel;
		}
		public int getCode()
		{
			return code;
		}
		public void setCode(int code)
		{
			this.code = code;
		}
		public String getCaption()
		{
			return caption;
		}
		public void setCaption(String caption)
		{
			this.caption = caption;
		}
		public OBSyslogDefine.LEVEL getSyslogLevel()
		{
			return syslogLevel;
		}
		public void setSyslogLevel(OBSyslogDefine.LEVEL syslogLevel)
		{
			this.syslogLevel = syslogLevel;
		}
	}
	public static TYPE [] TYPE_ARRAY = TYPE.values();
	public static final int STATUS_UNSOLVED = 0;
	public static final int STATUS_SOLVED = 1;
	public static final int STATUS_WARN = 2; //boot, active to standby, standby to active 같은 명시적인 장애가 아니지만 주목해야할 이벤트에 사용할 분류, 2012.11 추가함
	public static final int TYPE_FAULT = 0;
	public static final int TYPE_WARN = 2;

	public static final int OBJECT_NONE          = 0;
	public static final int OBJECT_ADC           = 1;
	public static final int OBJECT_VIRTUALSERVER = 2;
	public static final int OBJECT_MEMBER        = 3;
	public static final int OBJECT_INTERFACE     = 4;
	public static final int OBJECT_ETC           = 9;
	
	public String getTitle(int code)
	{
		OBDefineFault.TYPE foundType = null; 
		for(OBDefineFault.TYPE type: OBDefineFault.TYPE_ARRAY)
		{
			if(code==type.getCode())
			{
				foundType = type;
				break;
			}
		}
		if(foundType!=null)
		{
			return foundType.getTitle();
		}
		else
		{
			return null;
		}
	}
}