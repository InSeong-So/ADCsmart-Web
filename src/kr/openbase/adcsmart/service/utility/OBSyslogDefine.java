package kr.openbase.adcsmart.service.utility;

//참고 : syslog.h
//RFC 3164

public class OBSyslogDefine
{
	public enum LEVEL 	// SyslogLevel
	{
		EMERG	(0, "EMERG"  ), // system is unusable
		ALERT	(1, "ALERT"  ), // action must be taken immediately
		CRIT	(2, "CRIT"   ), // critical conditions
		ERR		(3, "ERR"    ), // error conditions
		WARNING	(4, "WARNING"), // warning conditions
		NOTICE	(5, "NOTICE" ), // normal but significant condition
		INFO	(6, "INFO"   ), // informational
		DEBUG	(7, "DEBUG"  ), // debug-level messages
		ALL		(8, "ALL"    ); // '*' in config, all levels
		
		private int code;
		private String name;
		LEVEL(int code, String name)
		{
			this.code = code;
			this.name = name;
		}		
		public int getCode()
		{
			return this.code;
		}
		public void setCode(int code)
		{
			this.code = code;
		}
		public String getName()
		{
			return this.name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
	}
	
	public enum FACILITY    // SyslogFac
	{
		KERN	(0,  "KERN"  ), // kernel messages
		USER	(1,  "USER"  ), // random user-level messages
		MAIL	(2,  "MAIL"  ), // mail system
		DAEMON	(3,  "DAEMON"), // system daemons
		AUTH	(4,  "AUTH"  ), // security/authorization messages
		SYSLOG	(5,  "SYSLOG"), // messages generated internally by syslogd
		LPR		(6,  "LPR"   ), // line printer subsystem
		NEWS	(7,  "NEWS"  ), // network news subsystem
		UUCP	(8,  "UUCP"  ), // UUCP subsystem
		CRON	(9,  "CRON"  ), // clock daemon
		//other codes through 15 reserved for system use
		LOCAL0	(16, "LOCAL0"), // reserved for local use
		LOCAL1	(17, "LOCAL1"), // reserved for local use
		LOCAL2	(18, "LOCAL2"), // reserved for local use
		LOCAL3	(19, "LOCAL3"), // reserved for local use
		LOCAL4	(20, "LOCAL4"), // reserved for local use
		LOCAL5	(21, "LOCAL5"), // reserved for local use
		LOCAL6	(22, "LOCAL6"), // reserved for local use
		LOCAL7	(23, "LOCAL7"); // reserved for local use
		
		private int code;
		private String name;
		
		FACILITY(int code, String name)
		{
			this.code = code;
			this.name = name;
		}		
		public int getCode()
		{
			return code;
		}
		public void setCode(int code)
		{
			this.code = code;
		}
		public String getName()
		{
			return this.name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
	}

	public enum FLAG
	{
		PID		(0x01, "PID"   ),  // log the pid with each message
		CONS	(0x02, "CONS"  ),  // log on the console if errors in sending
		ODELAY	(0x04, "ODELAY"),  // delay open until first syslog() (default)
		NDELAY	(0x08, "NDELAY"),  // don't delay open
		NOWAIT	(0x10, "NOWAIT"),  // don't wait for console forks: DEPRECATED
		PERROR	(0x20, "PERROR");  // log to stderr as well
		
		private int code;
		private String name;
		
		FLAG(int code, String name)
		{
			this.code = code;
			this.name = name;
		}
		public int getCode()
		{
			return this.code;
		}
		public void setCode(int code)
		{
			this.code = code;
		}
		public String getName()
		{
			return this.name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
	}
	
	public static LEVEL [] LEVEL_ARRAY = LEVEL.values();
	public static FACILITY [] FACILITY_ARRAY = FACILITY.values();
	public static final int NUM_FACILITIES = FACILITY_ARRAY.length; //total number of facilities
	public static FLAG [] FLAG_ARRAY = FLAG.values();
	
	public static final int	DEFAULT_PORT = 514;
	
	public static final int LOG_PRIMASK	= 0x07;		// mask to extract priority part (internal)			
	public static final int LOG_FACMASK	= 0x03F8;	// mask to extract facility part
	public static final int INTERNAL_NOPRI = 0x10;	// the "no priority" priority
}
