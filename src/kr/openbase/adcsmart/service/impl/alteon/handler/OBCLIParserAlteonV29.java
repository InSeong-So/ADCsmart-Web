package kr.openbase.adcsmart.service.impl.alteon.handler;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import kr.openbase.adcsmart.service.dto.OBDtoUptimeInfo;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoGateWayInfoAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoGeneralAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoIPAddrInfoAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoIPInfoAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoInfoLogsAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoInfoStgAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoInfoVlanAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoLinkInfoAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoMPResourceAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoSnmpCfgAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoStatLinkAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.dto.OBDtoSyslogCfgAlteon;
import kr.openbase.adcsmart.service.impl.dto.OBDtoStatsSlbAuxTable;
import kr.openbase.adcsmart.service.impl.dto.OBDtoStatsSlbMaint;
import kr.openbase.adcsmart.service.impl.fault.alteon.dto.OBDtoSWKeyAlteon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

/*
 * CLI 명령에 의해 출력된 데이터를 파싱한다.
 */
public class OBCLIParserAlteonV29 extends OBCLIParserAlteon {
	final static String SUFFIX_MORE = "any other key to continue[0m";

	public OBCLIParserAlteonV29() {
	}

	private String removeMoreText(String line) {
		String retVal = line;
		int strIndex = 0;
		strIndex = line.indexOf(SUFFIX_MORE);
		if (strIndex >= 0) {
			retVal = line.substring(strIndex + SUFFIX_MORE.length());
		}
		// remove white space
		byte[] byteArray = retVal.getBytes();
		int iPos = 0;
		for (int i = 0; i < byteArray.length; i++) {
			if (byteArray[i] != ' ') {
				iPos = i;
				break;
			}
		}

		retVal = retVal.substring(iPos);
		return retVal;
	}

	private final static String SUFFIX_GENERAL_UPTIME = "Switch is up ";// Switch is up 18 days, 1 hour, 11 minutes and
																		// 20 seconds.
	private final static String SUFFIX_GENERAL_MODEL = "Alteon Application Switch ";// Alteon Application Switch 3408
	private final static String SUFFIX_GENERAL_LASTBOOT = "Last boot: ";// 16:35:47 Mon Apr 22, 2013 (hard reset from
																		// console)
	private final static String SUFFIX_GENERAL_LASTAPPLY = "Last apply: ";// 15:42:23 Thu May 10, 2013
	private final static String SUFFIX_GENERAL_LASTSAVE = "Last save: ";// 10:25:20 Thu May 10, 2013
	private final static String SUFFIX_GENERAL_SWVERSION = "Software Version ";// Software Version 25.3.0 (FLASH
																				// image1), active configuration.
	private final static String SUFFIX_GENERAL_MADADDR = "MAC Address : ";// MAC Address: 00:13:0a:51:bc:00 IP (If 1)
																			// Address: 192.168.100.11
	private final static String SUFFIX_GENERAL_IPADDR = ") Address: ";// MAC Address: 00:13:0a:51:bc:00 IP (If 1)
																		// Address: 192.168.100.11
	private final static String SUFFIX_GENERAL_SERIALNUM = "Serial Number : ";// Hardware Order No: EB1412004 Serial No:
																				// SSCPC80159 Rev: 09

	public OBDtoGeneralAlteon parseGeneralInfo(String input) throws OBException {
		OBDtoGeneralAlteon retVal = new OBDtoGeneralAlteon();
		int strIndex = 0;
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;

			// upTime검출..
			strIndex = line.indexOf(SUFFIX_GENERAL_UPTIME);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_GENERAL_UPTIME.length());
				String element[] = item.split(",");// space를 기준으로 분리.
				if (element.length <= 1) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse uptime(%s)", line));
					continue;
				}
				String time = "";
				for (int i = 0; i < element.length; i++) {
					if (!time.isEmpty())
						time += " ";
					time += element[i];
				}
				retVal.setUpTime(time);
				continue;
			}
			// Model 추출
			strIndex = line.indexOf(SUFFIX_GENERAL_MODEL);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_GENERAL_MODEL.length());
				String element[] = item.split("\r");// space를 기준으로 분리.
				if (element.length <= 0) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse switch model(%s)", line));
					continue;
				}
				retVal.setModelName(element[0]);
				continue;
			}
			// last boot time
			strIndex = line.indexOf(SUFFIX_GENERAL_LASTBOOT);
			if (strIndex >= 0) {// 16:35:47 Mon Apr 22, 2013 (hard reset from console)
				String item = line.substring(strIndex + SUFFIX_GENERAL_LASTBOOT.length());
				String element[] = item.split("\\(");//
				if (element.length <= 0) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse boot time(%s)", line));
					continue;
				}
				Timestamp time = OBDateTime.toTimestamp("HH:mm:ss EEE MMM dd, yyyy", element[0]);
				if (time != null)
					retVal.setLastBootTime(time);
				else
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse boot time(%s)", line));
				continue;
			}
			// apply time
			strIndex = line.indexOf(SUFFIX_GENERAL_LASTAPPLY);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_GENERAL_LASTAPPLY.length());
				String element[] = item.split("\r");//
				if (element.length <= 0) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse apply time(%s)", line));
					continue;
				}
				Timestamp time = OBDateTime.toTimestamp("HH:mm:ss EEE MMM dd, yyyy", element[0]);
				if (time != null)
					retVal.setLastApplyTime(time);
				else
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a apply time(%s)", line));
				continue;
			}
			// save time
			strIndex = line.indexOf(SUFFIX_GENERAL_LASTSAVE);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_GENERAL_LASTSAVE.length());
				String element[] = item.split("\r");//
				if (element.length <= 0) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a save time(%s)", line));
					continue;
				}
				Timestamp time = OBDateTime.toTimestamp("HH:mm:ss EEE MMM dd, yyyy", element[0]);
				if (time != null)
					retVal.setLastSaveTime(time);
				else
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a save time(%s)", line));
				continue;
			}
			// mac addr
			strIndex = line.indexOf(SUFFIX_GENERAL_MADADDR);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_GENERAL_MADADDR.length());
				String element[] = item.split(" ");//
				if (element.length <= 0) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to parse a mac address(%s)", line));
					continue;
				}
				retVal.setMacAddr(element[0]);
				continue;
			}
			// ip addr
			strIndex = line.indexOf(SUFFIX_GENERAL_IPADDR);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_GENERAL_IPADDR.length());
				String element[] = item.split(" ");//
				if (element.length <= 0) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a ip address(%s)", line));
					continue;
				}
				retVal.setIpAddr(element[0]);
				continue;
			}
			// serial num
			strIndex = line.indexOf(SUFFIX_GENERAL_SERIALNUM);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_GENERAL_SERIALNUM.length());
				String element[] = item.split(" ");//
				if (element.length <= 0) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to parse a serial number(%s)", line));
					continue;
				}
				retVal.setSerialNum(element[0]);
				continue;
			}
			// sw version
			strIndex = line.indexOf(SUFFIX_GENERAL_SWVERSION);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_GENERAL_SWVERSION.length());
				String element[] = item.split(" ");//
				if (element.length <= 0) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to parse a software version(%s)", line));
					continue;
				}
				retVal.setSwVersion(element[0]);
				continue;
			}
		}
		return retVal;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndCfgSnmp();//
//			System.out.println(new OBCLIParserAlteon().parseSnmpCfg(cfgDump));
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	private final static String SUFFIX_SNMPCFG_HOSTNAME = "sysName: ";// sysName: "scard_alt_slb1"
	private final static String SUFFIX_SNMPCFG_READCOMM = "Read community string: ";// Read community string:
																					// "anwkddo6619"
	private final static String SUFFIX_SNMPCFG_WRITECOMM = "Write community string: ";// Write community string:
																						// "private"
	private final static String SUFFIX_SNMPCFG_ENABLED = " access enabled";// Current v1/v2 access enabled

	public OBDtoSnmpCfgAlteon parseSnmpCfg(String input) throws OBException {
		OBDtoSnmpCfgAlteon retVal = new OBDtoSnmpCfgAlteon();
		int strIndex = 0;
		String lines[] = input.split("\n");
		retVal.setEnabled(false);
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			// sysName 검출.
			strIndex = line.indexOf(SUFFIX_SNMPCFG_HOSTNAME);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_SNMPCFG_HOSTNAME.length());
				String element[] = item.split("\"");// space를 기준으로 분리.
				if (element.length <= 1) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a hostname(%s)", line));
					continue;
				}
				retVal.setSysName(element[1]);
				continue;
			}
			// read community 검출.
			strIndex = line.indexOf(SUFFIX_SNMPCFG_READCOMM);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_SNMPCFG_READCOMM.length());
				String element[] = item.split("\"");// space를 기준으로 분리.
				if (element.length <= 0) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to parse a read community string(%s)", line));
					continue;
				}
				retVal.setReadCommunity(element[1]);
				continue;
			}
			// write community 검출.
			strIndex = line.indexOf(SUFFIX_SNMPCFG_WRITECOMM);
			if (strIndex >= 0) {//
				String item = line.substring(strIndex + SUFFIX_SNMPCFG_WRITECOMM.length());
				String element[] = item.split("\"");//
				if (element.length <= 0) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to parse a write community string(%s)", line));
					continue;
				}
				retVal.setWriteCommunity(element[1]);
				continue;
			}
			// is enabled?
			strIndex = line.indexOf(SUFFIX_SNMPCFG_ENABLED);
			if (strIndex >= 0) {//
				retVal.setEnabled(true);
				continue;
			}
		}
		return retVal;
	}

//    public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndStatMPResources();//
////			System.out.println(cfgDump);
//			System.out.println(new OBCLIParserAlteon().parseStatMPResources(cfgDump));
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	private final static String SUFFIX_STATS_MP_CPU = "cpuUtil64Seconds: ";// cpuUtil1Second: 17%
	private final static String SUFFIX_STATS_MP_MEM_TOTAL = "Total: ";// Total: 130088960 bytes
	private final static String SUFFIX_STATS_MP_MEM_FREE = "Free: ";// Used: 102436864 bytes

	public OBDtoMPResourceAlteon parseStatMPResources(String input) throws OBException {
		OBDtoMPResourceAlteon retVal = new OBDtoMPResourceAlteon();
		int strIndex = 0;
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			// mp cpu usage 검출.
			strIndex = line.indexOf(SUFFIX_STATS_MP_CPU);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_STATS_MP_CPU.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split("%");//
				if (element.length <= 0) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a mp's cpu(%s)", line));
					continue;
				}
				retVal.setCpuUsage(Integer.parseInt(element[0]));
				continue;
			}
			// memory toal 검출.
			strIndex = line.indexOf(SUFFIX_STATS_MP_MEM_TOTAL);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_STATS_MP_MEM_TOTAL.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length <= 0) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to parse a mp's memory total(%s)", line));
					continue;
				}
				retVal.setMemTotal(Long.parseLong(element[0]));
				continue;
			}
			// memorfy used 검출.
			strIndex = line.indexOf(SUFFIX_STATS_MP_MEM_FREE);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_STATS_MP_MEM_FREE.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length <= 0) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("failed to parse a mp's memory used(%s)", line));
					continue;
				}
				retVal.setMemFree(Long.parseLong(element[0]));
				continue;
			}
		}
		if (retVal.getMemTotal() != 0) {
			int usage = (int) ((float) (retVal.getMemTotal() - retVal.getMemFree()) * 100.0 / retVal.getMemTotal());
			retVal.setMemUsage(usage);
		}
		return retVal;
	}

//    public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndStatSPCpuResources();//
////			System.out.println(cfgDump);
//			System.out.println(new OBCLIParserAlteon().parseStatSPUsage(cfgDump));
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//	private final static String SUFFIX_STATS_SP_CPU 		= "cpuUtil64Seconds: ";//cpuUtil1Second:            17%
//	public ArrayList<Integer> parseStatSPUsage(String input) throws OBException
//	{
//		ArrayList<Integer> retVal= new ArrayList<Integer>();
//	  	int strIndex=0;
//	  	String lines[] = input.split("\n");
//	  	
//	    for(String line:lines)
//	    {
//	    	if(line.isEmpty())
//	    		continue;
//	    	// sp cpu usage 검출.
//	    	strIndex=line.indexOf(SUFFIX_STATS_SP_CPU);
//	    	if(strIndex>=0)
//	    	{
//	    		String item = line.substring(strIndex+SUFFIX_STATS_SP_CPU.length());
//	    		String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
//	    		String element[] = rmWhiteSpace.split("%");//
//				if(element.length<=0)
//				{
//					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a sp's cpu(%s)", line));
//					continue;
//				}
//				retVal.add(Integer.parseInt(element[0]));
//				continue;
//	    	}
//	    }
//	  	return retVal;
//	}

//    public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndStatFans();//
////			System.out.println(cfgDump);
//			System.out.println(new OBCLIParserAlteon().parseFanStatus(cfgDump));
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//	private final static String SUFFIX_STATS_FAN 		= "Fans OK";//Fans OK.
//	public int parseFanStatus(String input) throws OBException
//	{
//		int retVal=OBDefine.SYS_FAN_STATUS_FAIL;
//	  	int strIndex=0;
//	  	String lines[] = input.split("\n");
//	  	
//	    for(String line:lines)
//	    {
//	    	if(line.isEmpty())
//	    		continue;
//	    	// fan status 검출.
//	    	strIndex=line.indexOf(SUFFIX_STATS_FAN);
//	    	if(strIndex>=0)
//	    	{
//	    		retVal=OBDefine.SYS_FAN_STATUS_OK;
//				return retVal;
//	    	}
//	    }
//	  	return retVal;
//	}

//    public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndInfoLicense();//
////			System.out.println(cfgDump);
//			System.out.println(new OBCLIParserAlteon().parseInfoLicense(cfgDump));
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//	private String convertLicenseType(String code)
//	{
//		if(code.equals("Permanent"))
//			return 	OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_LICENSE_UNLIMIT);
//		
//		return 	OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_LICENSE_TEMP);
//	}
//    >> scard_alt_slb1 - Information# /info/swkey 
//    Permanent License: aas-slb-cookie-MRGviFSi
//
//    Software feature              Status    
//    ----------------              ------
//    slb                           Permanent
//    cookie                        Permanent
//    private final static String SUFFIX_LICENSE_START 		= "----------------";//software feature 시작 문자열.
//	public ArrayList<OBDtoLicenseInfoAlteon> parseInfoLicense(String input) throws OBException
//	{
//		ArrayList<OBDtoLicenseInfoAlteon> retVal=new ArrayList<OBDtoLicenseInfoAlteon>();
//	  	int strIndex=0;
//	  	String lines[] = input.split("\n");
//	  	boolean startFlag=false;
//	    for(String line:lines)
//	    {
//	    	if(line.isEmpty())
//	    		continue;
//	    	// start string 검출.
//	    	if(startFlag==false)
//	    	{
//		    	strIndex=line.indexOf(SUFFIX_LICENSE_START);
//		    	if(strIndex>=0)
//		    	{
//		    		startFlag=true;
//		    	}
//	    		continue;
//	    	}
//    		String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
//    		String element[] = rmWhiteSpace.split(" ");//
//			if(element.length!=2)
//			{
//				break;
//			}
//			OBDtoLicenseInfoAlteon obj = new OBDtoLicenseInfoAlteon();
//			obj.setName(element[0]);
//			obj.setStatus(convertLicenseType(element[1]));
//			retVal.add(obj);
//	    }
//	  	return retVal;
//	}

//    public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndInfoLink();//
////			System.out.println(cfgDump);
//			System.out.println(new OBCLIParserAlteon().parseInfoLink(cfgDump));
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//    >> scard_alt_slb1 - Layer 2# /info/link 
//    ------------------------------------------------------------------
//    Alias    Port   Phy Type    Speed    Duplex     Flow Ctrl      Link 
//    ------   ----   --------    -----   --------  --TX-----RX--   ------
//    1          1     GE Cu        any      any     yes    yes      down
//    2          2     GE Cu        100     full     yes    yes       up 
//    3          3     GE Cu       1000     full     yes    yes       up 
//    4          4   GE Cu/SFP      any      any     yes    yes      down
//    5          5   GE Cu/SFP      any      any     yes    yes      down
	private final static String SUFFIX_LINK_START = "------ ";// software feature 시작 문자열.

	public ArrayList<OBDtoLinkInfoAlteon> parseInfoLink(String input) throws OBException {
		ArrayList<OBDtoLinkInfoAlteon> retVal = new ArrayList<OBDtoLinkInfoAlteon>();
		int strIndex = 0;
		String lines[] = input.split("\n");
		boolean startFlag = false;
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			// start string 검출.
			if (startFlag == false) {
				strIndex = line.indexOf(SUFFIX_LINK_START);
				if (strIndex >= 0) {
					startFlag = true;
				}
				continue;
			}
			String rmWhiteSpace = OBParser.removeFirstWhitespace(line);
			String element[] = rmWhiteSpace.split(" ");//
			if (element.length != 7) {
				continue;
			}
			OBDtoLinkInfoAlteon obj = new OBDtoLinkInfoAlteon();
			obj.setAliasName(element[0]);
			obj.setPortName(element[1]);
			if (element[6].equals("up"))
				obj.setStatus(OBDefine.L2_LINK_STATUS_UP);
			else
				obj.setStatus(OBDefine.L2_LINK_STATUS_DOWN);
			retVal.add(obj);
		}
		return retVal;
	}

//    public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndStatPort(2);//
////			System.out.println(cfgDump);
//			System.out.println(new OBCLIParserAlteon().parseStateLink(new String("2"), cfgDump));
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//    ------------------------------------------------------------------
//    Interface statistics for port 2:
//                         ifHCIn Counters       ifHCOut Counters
//    Octets:                   3959114137              818850664
//    UcastPkts:                   9092517               11581043
//    BroadcastPkts:                  1585                   3407
//    MulticastPkts:                     0                  69487
//    Discards:                          0                      0
//    Errors:                            0                      0
//    ------------------------------------------------------------------
	private final static String SUFFIX_STATLINK_START = "ifHCIn Counters";//
	private final static String SUFFIX_STATLINK_END = "---------------------------------------------------------";//
	private final static String SUFFIX_STATLINK_BYTES = "Octets: ";//
	private final static String SUFFIX_STATLINK_PKTS = "UcastPkts: ";//
	private final static String SUFFIX_STATLINK_ERRORS = "Errors: ";//
	private final static String SUFFIX_STATLINK_DISCARDS = "Discards: ";//

	public OBDtoStatLinkAlteon parseStateLink(String name, String input) throws OBException {
		OBDtoStatLinkAlteon retVal = new OBDtoStatLinkAlteon();
		int strIndex = 0;
		String lines[] = input.split("\n");
		boolean startFlag = false;
		retVal.setLinkName(name);
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			// start string 검출.
			if (startFlag == false) {
				strIndex = line.indexOf(SUFFIX_STATLINK_START);
				if (strIndex >= 0) {
					startFlag = true;
				}
				continue;
			}
			strIndex = line.indexOf(SUFFIX_STATLINK_END);
			if (strIndex >= 0) {
				break;
			}

			// octets 검출.
			strIndex = line.indexOf(SUFFIX_STATLINK_BYTES);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_STATLINK_BYTES.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");//
				if (element.length != 2) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a bytes stats(%s)", line));
					continue;
				}
				retVal.setBytes(Long.parseLong(element[0]) + Long.parseLong(element[1]));
				continue;
			}
			// pkts 검출.
			strIndex = line.indexOf(SUFFIX_STATLINK_PKTS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_STATLINK_PKTS.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");//
				if (element.length != 2) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a pkts stats(%s)", line));
					continue;
				}
				retVal.setPkts(Long.parseLong(element[0]) + Long.parseLong(element[1]));
				continue;
			}
			// errors 검출.
			strIndex = line.indexOf(SUFFIX_STATLINK_ERRORS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_STATLINK_ERRORS.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");//
				if (element.length != 2) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a pkts stats(%s)", line));
					continue;
				}
				retVal.setErrors(Long.parseLong(element[0]) + Long.parseLong(element[1]));
				continue;
			}
			// discards 검출.
			strIndex = line.indexOf(SUFFIX_STATLINK_DISCARDS);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_STATLINK_DISCARDS.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");//
				if (element.length != 2) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a pkts stats(%s)", line));
					continue;
				}
				retVal.setDiscards(Long.parseLong(element[0]) + Long.parseLong(element[1]));
				continue;
			}
		}
		return retVal;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndInfoVlan();//
////			System.out.println(cfgDump);
//			System.out.println(new OBCLIParserAlteon().parseInfoVlan(cfgDump));
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//>> scard_alt_slb1 - Main# /info/l2/vlan 
//VLAN                Name               Status Jumbo BWC  Learn Ports
//----  -------------------------------- ------ ----- ---- ----- ----------------
//1     Default VLAN                       ena    n  1024   ena  1 3-6 9-12
//2     VLAN 2                             ena    n  1024   ena  2 7 8
//
//>> scard_alt_slb1 - Layer 2#
	private final static String SUFFIX_INFOVLAN_START = "----  ------- -------------------------------- ------ ----- ---- ----- ----------------";//

	public ArrayList<OBDtoInfoVlanAlteon> parseInfoVlan(String input) throws OBException {// 원본 데이터를 이용해야 함. 지정된 데이터 위치가
																							// 있음.
		ArrayList<OBDtoInfoVlanAlteon> retVal = new ArrayList<OBDtoInfoVlanAlteon>();
		int strIndex = 0;
		String lines[] = input.split("\n");
		boolean startFlag = false;
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			// start string 검출.
			if (startFlag == false) {
				strIndex = line.indexOf(SUFFIX_INFOVLAN_START);
				if (strIndex >= 0) {
					startFlag = true;
				}
				continue;
			}
//	    	
			if (line.length() <= 71)
				continue;

			OBDtoInfoVlanAlteon obj = new OBDtoInfoVlanAlteon();
			// index 검출
			String item = line.substring(0, 4);
			String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
			obj.setVlanId(Integer.parseInt(rmWhiteSpace));
			// name 검출.
			item = line.substring(14, 48);
			obj.setName(item);
			// status 검출.
			item = line.substring(49, 56);
			rmWhiteSpace = OBParser.removeFirstWhitespace(item);
			if (rmWhiteSpace.equals("ena"))
				obj.setStatus(OBDefine.L2_VLAN_STATE_ENABLED);
			else
				obj.setStatus(OBDefine.L2_VLAN_STATE_DISABLED);
			// ports
			item = line.substring(71);
			obj.setPorts(item);

			retVal.add(obj);
		}
		return retVal;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndInfoStg();//
////			System.out.println(cfgDump);
//			System.out.println(new OBCLIParserAlteon().parseInfoStg(cfgDump));
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//>> scard_alt_slb1 - Layer 2# stg 
//
//------------------------------------------------------------------
//Spanning Tree Group 1: Off (STP/PVST), FDB aging timer 300
//VLANs:  1 2
//
//Port    Priority   Cost      State       Designated Bridge     Des Port
//------  --------   ----    ----------  ----------------------  --------
//1             0      0      DISABLED  *
//2             0      0     FORWARDING *
//3             0      0     FORWARDING *
//4             0      0      DISABLED  *
//5             0      0      DISABLED  *
//6             0      0      DISABLED  *
//7             0      0      DISABLED  *
//8             0      0      DISABLED  *
//9             0      0      DISABLED  *
//10            0      0      DISABLED  *
//11            0      0      DISABLED  *
//12            0      0      DISABLED  *
//* = STP turned off for this port.
//Transmission of PVST frames on untagged ports: Disabled
//
//Number of topology changes - 0
//Time since last topology change - N/A
//
//>> scard_alt_slb1 - Layer 2# ls
	private final static String SUFFIX_INFOSTG_ENABLED = "Spanning Tree Group ";//

	public ArrayList<OBDtoInfoStgAlteon> parseInfoStg(String input) throws OBException {
		ArrayList<OBDtoInfoStgAlteon> retVal = new ArrayList<OBDtoInfoStgAlteon>();
		int strIndex = 0;
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			strIndex = line.indexOf(SUFFIX_INFOSTG_ENABLED);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_INFOSTG_ENABLED.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(": ");//
				if (element.length != 2) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a stg info(%s)", line));
					continue;
				}
				OBDtoInfoStgAlteon obj = new OBDtoInfoStgAlteon();
				obj.setStgID(Integer.parseInt(element[0]));
				// OFF/ON 조사.
				rmWhiteSpace = OBParser.removeFirstWhitespace(element[1]);
				String onoff[] = rmWhiteSpace.split(" ");//
				if (onoff.length < 2) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a stg info(%s)", line));
					continue;
				}
				if (onoff[0].endsWith("Off"))
					obj.setState(OBDefine.L2_STP_STATE_DISABLED);
				else
					obj.setState(OBDefine.L2_STP_STATE_ENABLED);
				retVal.add(obj);
			}
		}
		return retVal;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndInfoTrunk();//
////			System.out.println(cfgDump);
//			System.out.println(new OBCLIParserAlteon().parseInfoTrunk(cfgDump));
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	>> scard_alt_slb1 - Layer 2# /info/l2/trunk 
//	Trunk group 1, bw contract 1024, port state:
//	   10: STG  1 DOWN
//	   11: STG  1 DOWN

	private final static String SUFFIX_INFOTRUNK_ENABLED = "Trunk group ";//

	public ArrayList<Integer> parseInfoTrunk(String input) throws OBException {
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		int strIndex = 0;
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			strIndex = line.indexOf(SUFFIX_INFOTRUNK_ENABLED);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_INFOTRUNK_ENABLED.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(", ");//
				if (element.length < 2) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a trunk info(%s)", line));
					continue;
				}
				retVal.add(Integer.parseInt(element[0]));
			}
		}
		return retVal;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndInfoL3IP();//
////			System.out.println(cfgDump);
//			System.out.println(new OBCLIParserAlteon().parseIPGateway(cfgDump));
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	>> scard_alt_slb1 - Main# /info/l3/ip
//	IP information:
//	  AS number 0
//
//	Interface information:
//	  1: IP4 192.168.100.11  255.255.255.0   192.168.100.255, vlan 1, up
//	  2: IP4 192.168.101.11  255.255.255.0   192.168.101.255, vlan 2, up
//
//	Default gateway information: metric strict
//	  1: 192.168.100.1,                                 vlan any,  up
//
//	Current IP forwarding settings: ON, dirbr disabled, noicmprd disabled, rtcache enabled
//
//	Current local networks:
//	None
//	-----------------------------------------------
//	Current IPv6 local networks:
//
//	Current IP port settings:
//	  All other ports have forwarding ON
//
//	Current network filter settings:
//	  none
//
//	Current route map settings:
//
//	>> scard_alt_slb1 - Layer 3# /info/l3/rou
	private final static String SUFFIX_L3IP_INTERFACE_START = "Interface information:";//
	private final static String SUFFIX_L3IP_GATEWAY_START = "Default gateway information: ";//
	private final static String SUFFIX_L3IP_GATEWAY_END = "Current IP forwarding settings:";//
	private final static String SUFFIX_IPV6_LINK_LOCAL_START = "IPv6 Link Local Address Information:";

	public OBDtoIPInfoAlteon parseIPGateway(String input) throws OBException {
		OBDtoIPInfoAlteon retVal = new OBDtoIPInfoAlteon();
		int strIndex = 0;
		String lines[] = input.split("\n");
		boolean interfaceFlag = false;
		boolean gatewayFlag = false;
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			if (line.indexOf(SUFFIX_IPV6_LINK_LOCAL_START) >= 0) // v28인지 부터 들어간 듯. SUFFIX_L3IP_INTERFACE_START 다음이다.
			{
				interfaceFlag = false;
				continue;
			}
			if (line.indexOf(SUFFIX_L3IP_GATEWAY_END) >= 0) {
				break;
			}
			if (interfaceFlag == false) {
				strIndex = line.indexOf(SUFFIX_L3IP_INTERFACE_START);
				if (strIndex >= 0) {
					interfaceFlag = true;
					continue;
				}
			}
			if (gatewayFlag == false) {
				strIndex = line.indexOf(SUFFIX_L3IP_GATEWAY_START);
				if (strIndex >= 0) {
					gatewayFlag = true;
					interfaceFlag = false;
					continue;
				}
			}
			if (interfaceFlag == true) {
				// interface 처리.
				String item = OBParser.removeFirstWhitespace(line);
				String element[] = item.split(", ");//
				if (element.length != 3) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a ip info-1(%s)", line));
					continue;
				}
				// ip/netmask/bcast
				String ipmask[] = element[0].split(" ");
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
						String.format("failed to parse a ip info-2(%s, %s)", ipmask[2], ipmask[3]));

				// 다음과 같이 두개 형식이 있을 수 있으므로 길이가 4또는 5여야 한다.
				// v22.x 에서
				// Interface information:
				// 1: 210.94.0.74 255.255.255.0 210.94.0.255 , vlan 1, up
				// 2: 192.168.59.74 255.255.255.0 192.168.59.255 , vlan 1, up
				// 3: 192.168.60.1 255.255.255.0 192.168.60.255 , vlan 1, up
				//
				// Interface information:
				// 1: IP4 192.168.100.11 255.255.255.0 192.168.100.255, vlan 1, up
				// 2: IP4 192.168.101.11 255.255.255.0 192.168.101.255, vlan 2, up

				if ((ipmask.length != 4) && (ipmask.length != 5)) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a ip info-2(%s)", line));
					continue;
				}
				OBDtoIPAddrInfoAlteon obj = new OBDtoIPAddrInfoAlteon();
				obj.setIpAddr(ipmask[ipmask.length - 3]); // 길이 5일때 2
				obj.setNetmask(ipmask[ipmask.length - 2]); // 길이 5일때 3
				obj.setBcast(ipmask[ipmask.length - 1]); // 길이 5일때 4
				// vlan
				String vlan[] = element[1].split(" ");
				if (vlan.length != 2) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a ip info-3(%s)", line));
					continue;
				}
				obj.setVlan(vlan[1]);
				// status
				if (element[2].equals("up"))
					obj.setStatus(OBDefine.L2_LINK_STATUS_UP);
				else
					obj.setStatus(OBDefine.L2_LINK_STATUS_DOWN);

				retVal.getIpList().add(obj);

				continue;
			}
			// gateway 처리.
			if (gatewayFlag == true) {
				String item = OBParser.removeFirstWhitespace(line);
				String element[] = item.split(", ");//
				if (element.length != 3) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a trunk info(%s)", line));
					continue;
				}
				// ip
				String ipmask[] = element[0].split(" ");
				if (ipmask.length != 2) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a ip info(%s)", line));
					continue;
				}
				OBDtoGateWayInfoAlteon obj = new OBDtoGateWayInfoAlteon();
				obj.setIpAddr(ipmask[1]);
				// vlan
				String vlan[] = element[1].split(" ");
				if (vlan.length != 2) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a ip info(%s)", line));
					continue;
				}
				obj.setVlan(vlan[1]);
				// status
				if (element[2].equals("up"))
					obj.setStatus(OBDefine.L3_GW_STATUS_UP);
				else
					obj.setStatus(OBDefine.L3_GW_STATUS_FAILED);
				retVal.getGatewayList().add(obj);
			}
		}
		return retVal;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndCfgSyslog();//
////			System.out.println(cfgDump);
//			System.out.println(new OBCLIParserAlteon().parseCfgSyslog(cfgDump));
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	>> scard_alt_slb1 - Main# /cfg/sys/syslog/cur 
//	Current syslog configuration:
//	  hst1 211.189.47.58, severity 7, facility 0
//	  hst2 172.172.2.209, severity 7, facility 0
//	  hst3 172.172.2.222, severity 7, facility 0
//	  hst4 172.172.2.13, severity 7, facility 0
//	  hst5 172.172.2.56, severity 7, facility 0, console enabled
//	  syslogging all features
//
//	>> scard_alt_slb1 - Syslog# 
	private final static String SUFFIX_CFG_SYSLOG_START = "Current syslog configuration:";//

	public ArrayList<OBDtoSyslogCfgAlteon> parseCfgSyslog(String input) throws OBException {
		ArrayList<OBDtoSyslogCfgAlteon> retVal = new ArrayList<OBDtoSyslogCfgAlteon>();
		int strIndex = 0;
		String lines[] = input.split("\n");
		boolean startFlag = false;
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			if (startFlag == false) {
				strIndex = line.indexOf(SUFFIX_CFG_SYSLOG_START);
				if (strIndex >= 0) {
					startFlag = true;
				}
				continue;
			}
			String item = OBParser.removeFirstWhitespace(line);
			String element[] = item.split(", ");//
			if (element.length < 3) {
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a syslog cfg(%s)", line));
				continue;
			}
			// server ip 처리.
			String server[] = element[0].split(" ");
			if (server.length != 2) {
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a syslog cfg(%s)", line));
				continue;
			}
			OBDtoSyslogCfgAlteon obj = new OBDtoSyslogCfgAlteon();
			obj.setServerIP(server[1]);
			// severity
			String severity[] = element[1].split(" ");
			if (severity.length != 2) {
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a syslog cfg(%s)", line));
				continue;
			}
			obj.setSeverity(severity[1]);
			// facility
			String facility[] = element[2].split(" ");
			if (facility.length != 2) {
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a syslog cfg(%s)", line));
				continue;
			}
			if (line.contains("hst5")) {
				startFlag = false;
			}
			obj.setFacility(facility[1]);
			retVal.add(obj);
		}
		return retVal;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndCfgNtp();//
////			System.out.println(cfgDump);
//			System.out.println(new OBCLIParserAlteon().parseCfgNtp(cfgDump));
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//>> scard_alt_slb1 - System# /cfg/sys/ntp/cur 
//Current NTP state: disabled
//Current primary NTP server: 141.223.182.106
//Current resync interval: 1 minutes
//Current GMT timezone offset: +9:00
//
//>> scard_alt_slb1 - NTP Server# 
//    private final static String SUFFIX_CFG_NTP_STATE 	= "Current NTP state: ";//
//    private final static String SUFFIX_CFG_NTP_SERVER 	= "Current primary NTP server: ";//
//    private final static String SUFFIX_CFG_NTP_INTERVAL	= "Current resync interval: ";//
//    private final static String SUFFIX_CFG_NTP_OFFSET 	= "Current GMT timezone offset: ";//
//	public OBDtoNtpCfgAlteon parseCfgNtp(String input) throws OBException
//	{
//		OBDtoNtpCfgAlteon retVal = new OBDtoNtpCfgAlteon();
//	  	int strIndex=0;
//	  	String lines[] = input.split("\n");
//	    for(String line:lines)
//	    {
//	    	if(line.isEmpty())
//	    		continue;
//	    	//state
//	    	strIndex=line.indexOf(SUFFIX_CFG_NTP_STATE);
//	    	if(strIndex>=0)
//	    	{
//	    		String item = line.substring(strIndex+SUFFIX_CFG_NTP_STATE.length());
//	    		if(item.equals("disabled"))
//	    			retVal.setState(OBDefine.NTP_STATE_DISABLED);
//	    		else
//	    			retVal.setState(OBDefine.NTP_STATE_ENABLED);
//
//				continue;
//	    	}
//	    	//server
//	    	strIndex=line.indexOf(SUFFIX_CFG_NTP_SERVER);
//	    	if(strIndex>=0)
//	    	{
//	    		String item = line.substring(strIndex+SUFFIX_CFG_NTP_SERVER.length());
//	    		retVal.setServer(item);
//				continue;
//	    	}
//	    	//interval
//	    	strIndex=line.indexOf(SUFFIX_CFG_NTP_INTERVAL);
//	    	if(strIndex>=0)
//	    	{
//	    		String item = line.substring(strIndex+SUFFIX_CFG_NTP_INTERVAL.length());
//	    		String interval[] = item.split(" ");
//				if(interval.length != 2)
//				{
//					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a ntp cfg(%s)", line));
//					continue;
//				}	
//	    		retVal.setInterval(Integer.parseInt(interval[0]));
//				continue;
//	    	}
//	    	//offset
//	    	strIndex=line.indexOf(SUFFIX_CFG_NTP_OFFSET);
//	    	if(strIndex>=0)
//	    	{
//	    		String item = line.substring(strIndex+SUFFIX_CFG_NTP_OFFSET.length());
//	    		retVal.setTimeZone(item);
//				continue;
//	    	}
//	    	
//	    }
//	  	return retVal;
//	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndInfoLog();//
////			System.out.println(cfgDump);
//			System.out.println(new OBCLIParserAlteon().parseInfoLogs(cfgDump));
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//>> scard_alt_slb1 - System# /cfg/sys/ntp/cur 
//Current NTP state: disabled
//Current primary NTP server: 141.223.182.106
//Current resync interval: 1 minutes
//Current GMT timezone offset: +9:00
//
//>> scard_alt_slb1 - NTP Server# 
	public ArrayList<OBDtoInfoLogsAlteon> parseInfoLogs(String input) throws OBException {
		ArrayList<OBDtoInfoLogsAlteon> retVal = new ArrayList<OBDtoInfoLogsAlteon>();

		// 년도 추출.
		String dateFormat = "yyyy";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
		String yyyy = sdf.format(cal.getTime());

		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			if (line.contains(">>"))
				continue;

			line = removeMoreText(line);

			if (line.length() < 25) {
				continue;
			}

			String content = line.substring(24);
			String date = line.substring(0, 15);
			String level = line.substring(16, 23);

			Timestamp time = null;
			try {
				String rmWhiteSpace = OBParser.removeFirstWhitespace(date);
				String[] items = rmWhiteSpace.split(" ");
				time = OBDateTime.toTimestamp("yyyy MMM dd HH:mm:ss",
						yyyy + " " + items[0] + " " + items[1] + " " + items[2]);
			} catch (Exception e) {
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a log info(%s)", line));
				continue;
			}

			OBDtoInfoLogsAlteon obj = new OBDtoInfoLogsAlteon();
			obj.setDateTime(time);
			obj.setLevel(level);
			obj.setContent(content);

			retVal.add(obj);
		}
		return retVal;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin", OBDefine.SERVICE.TELNET, OBDefine.SERVICE.TELNET);
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndDumpInfoSlb();//
////			System.out.println(cfgDump);
//			System.out.println(new OBCLIParserAlteon().parseInfoSlbDump(cfgDump));
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//>> scard_alt_slb1 - System# /cfg/sys/ntp/cur 
//Current NTP state: disabled
//Current primary NTP server: 141.223.182.106
//Current resync interval: 1 minutes
//Current GMT timezone offset: +9:00
//
//>> scard_alt_slb1 - NTP Server# 
//	private final static String SUFFIX_SLBDUMP_START 	= "Virtual server state:";//
//    private final static String SUFFIX_SLBDUMP_END 		= "IDS group state:";//
//    private final static String SUFFIX_SLBDUMP_VSVCSINFO= " rport ";//
//    private final static String SUFFIX_SLBDUMP_REALSTART= "real servers:";//
//    private final static String SUFFIX_SLBDUMP_NO_SRV_UP= "NO SERVICES UP";//

//	public ArrayList<OBDtoVSvcStatusAlteon> parseInfoSlbDump(String input) throws OBException
//	{
//		ArrayList<OBDtoVSvcStatusAlteon> retVal = new ArrayList<OBDtoVSvcStatusAlteon>();
//	  	int strIndex=0;
//	  	String lines[] = input.split("\n");
//	  	boolean startFlag = false;
//	  	boolean realStartFlag = false;
//	  	OBDtoVSvcStatusAlteon vsObj = null;
//	  	
//	    for(String line:lines)
//	    {
//	    	if(line.isEmpty())
//	    		continue;
//
//	    	if(startFlag == false)
//	    	{
//		    	strIndex=line.indexOf(SUFFIX_SLBDUMP_START);
//		    	if(strIndex>=0)
//		    	{
//		    		startFlag = true;
//		    	}
//		    	continue;
//	    	}
//	    	// end position
//	    	strIndex=line.indexOf(SUFFIX_SLBDUMP_END);
//	    	if(strIndex>=0)
//	    	{
//	    		break;
//	    	}
//	    	
//	    	line = removeMoreText(line);
//	    	// vs index, vs ip, status 추출
//	    	// 다음과 같이 두가지 형식이 있으므로 "IPV4"를 확인해서 작업하면 안된다.
//	    	//Virtual server state:
//	    	//    1: 221.139.13.130,  00:11:f9:c8:c3:0e
//	    	//
//	    	//Virtual server state:
//	    	//   55: IP4 192.168.100.55,  00:00:5e:00:01:37, vname alteon_vs_m, NO SERVICES UP
//	       	//strIndex=line.indexOf(SUFFIX_SLBDUMP_VSINFO);
//	    	//아래줄 정규식, 이렇게 할 수밖에 없다. 이런 줄을 인식한다.
//	    	// 55: IP4 ip주소, mac 그리고 뒤에 아무거나 오던말던...
//	    	if(line.matches("\\d+\\:\\s+(IP4\\s+)?(\\d{1,3}\\.){3}\\d{1,3},\\s+([0-9a-fA-F]{2}\\:){5}[0-9a-fA-F]{2}.*")==true)
//	    	{
//	    		if(vsObj != null)
//	    		{
//	    			retVal.add(vsObj);
//	    		}
//	    		vsObj = new OBDtoVSvcStatusAlteon();
//	    	
////	    		String oneLine = line.substring(strIndex+SUFFIX_SLBDUMP_VSINFO.length());
//	    		String [] items = line.split(", ");
//	    		
//	    		// status 검사.
//	    		if(line.contains(SUFFIX_SLBDUMP_NO_SRV_UP)==true)
//	    		{
//	    			vsObj.setStatus(OBDefine.VS_STATUS.UNAVAILABLE);
//	    		}
//	    		else
//	    		{
//	    			vsObj.setStatus(OBDefine.VS_STATUS.AVAILABLE);
//	    		}
//
//	    		String rmWhiteSpace = OBParser.removeFirstWhitespace(items[0]);
//	    		String [] ipItems = rmWhiteSpace.split(" ");
//	    		// index 검사
//	    		String [] indexItems = ipItems[0].split(":");
//	    		vsObj.setVsId(Integer.parseInt(indexItems[0]));
//	    		// ip 추출: 맨 뒤가 IP다.
//	    		vsObj.setVsIP(ipItems[ipItems.length-1]);
//	    		realStartFlag = false;
//	    		continue;
//	    	}
//	    	
//	    	if((strIndex=line.indexOf(SUFFIX_SLBDUMP_VSVCSINFO))>0)  // srvice port 추출.
//    			{	// 사용하지 않는 정보, 분석하지 않는다.
//	    		realStartFlag = false;
//	    		continue;
//	    	}
//	    	
//	    	if(realStartFlag == false)
//	    	{
//		    	if((strIndex=line.indexOf(SUFFIX_SLBDUMP_REALSTART))>=0)
//		    	{	// 사용하지 않는 정보, 분석하지 않는다.
//		    		realStartFlag = true;
//		    		continue;
//		    	}
//	    	}
//	    	
//	    	if(realStartFlag==true)	    	//real server
//	    	{
//	    		String [] commaItems = line.split(", ");
//		    	if(commaItems.length != 5)
//		    	{
//		    		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse realserver's status(%s)", line));
//		    		continue;
//		    	}
//	    	
//		    	OBDtoRSrvStatusAlteon realObj = new OBDtoRSrvStatusAlteon();
//		    	String ipItems[] = commaItems[0].split(": ");
//		    	realObj.setRealIP(ipItems[ipItems.length-1]);
//		    	realObj.setStatus(OBDefine.STATUS_UNAVAILABLE);
//		    	if(commaItems[4].equals("up"))
//		    	{
//		    		realObj.setStatus(OBDefine.STATUS_AVAILABLE);
//		    	}
//		    	vsObj.getRealSrvStatusList().add(realObj);
//	    	}
//	    }
//
//		if(vsObj != null)
//			retVal.add(vsObj);
//	
//	  	return retVal;
//	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndStatSlbDump();//
////			System.out.println(cfgDump);
//			System.out.println(new OBCLIParserAlteon().parseStatSlbDump(cfgDump));
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//>> scard_alt_slb1 - System# /cfg/sys/ntp/cur 
//Current NTP state: disabled
//Current primary NTP server: 141.223.182.106
//Current resync interval: 1 minutes
//Current GMT timezone offset: +9:00
//
//>> scard_alt_slb1 - NTP Server# 
//	private final static String SUFFIX_STATSLB_MAX_SESSION		= "Maximum sessions:";//
//    private final static String SUFFIX_STATSLB_4SECAVG_SESSION	= "4 second average: ";//
//	public OBDtoStatSlbSessionInfoAlteon parseStatSlbDump(String input) throws OBException
//	{
//		OBDtoStatSlbSessionInfoAlteon retVal = new OBDtoStatSlbSessionInfoAlteon();
//	  	int strIndex=0;
//	  	String lines[] = input.split("\n");
//	  	
//	    for(String line:lines)
//	    {
//	    	if(line.isEmpty())
//	    		continue;
//	    	line = removeMoreText(line);
//	    	// 
//	       	strIndex=line.indexOf(SUFFIX_STATSLB_MAX_SESSION);
//	    	if(strIndex>=0)
//	    	{
//	    		String item = line.substring(strIndex+SUFFIX_STATSLB_MAX_SESSION.length());
//	    		String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
//	    		retVal.setMaxSession(Long.parseLong(rmWhiteSpace));
//	    		continue;
//	    	}
//	       	strIndex=line.indexOf(SUFFIX_STATSLB_4SECAVG_SESSION);
//	    	if(strIndex>=0)
//	    	{
//	    		String item = line.substring(strIndex+SUFFIX_STATSLB_4SECAVG_SESSION.length());
//	    		String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
//	    		retVal.setSec4Session(Long.parseLong(rmWhiteSpace));
//	    		break;
//	    	}
//	    }
//	
//	    if(retVal.getMaxSession()!=0)
//	    {
//	    	long usage = retVal.getSec4Session()*100/retVal.getMaxSession();
//	    	retVal.setSessionUsage(usage);
//	    }
//	
//	  	return retVal;
//	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
////			handler.login();
//			System.out.println("login OK");
//			String cfgDump = ">> Main# /i/swkey\n";//handler.cmndDumpInfoSlb();//
//			cfgDump += "Throughput License: 4Gbps-n9imDvY1\n";//handler.cmndDumpInfoSlb();//
//			cfgDump += "Permanent License: aas-slb-cookie-aTV1yJzQ\n";//handler.cmndDumpInfoSlb();//
//			cfgDump += "\n";//handler.cmndDumpInfoSlb();//
//			cfgDump += "Software feature              Status \n";//handler.cmndDumpInfoSlb();//
//			cfgDump += "----------------              ------\n";//handler.cmndDumpInfoSlb();//
//			cfgDump += "slb                           Permanent\n";//handler.cmndDumpInfoSlb();//
//			cfgDump += "cookie                        Permanent\n";//handler.cmndDumpInfoSlb();//
//			cfgDump += "\n";//handler.cmndDumpInfoSlb();//
//			cfgDump += "Capacity License   License      Peak Usage        Current Usage    \n";//handler.cmndDumpInfoSlb();//
//			cfgDump += "----------------   ---------    --------------    --------------  \n";//handler.cmndDumpInfoSlb();//
//			cfgDump += "Throughput         4Gbps        1.35 Mbps         4.61 Kbps \n";//handler.cmndDumpInfoSlb();//
////			System.out.println(cfgDump);
//			System.out.println(new OBCLIParserAlteon().parseSWKey(cfgDump));
////			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}	
	/**
	 * >> Main# /i/swkey Throughput License: 4Gbps-n9imDvY1 Permanent License:
	 * aas-slb-cookie-aTV1yJzQ
	 * 
	 * Software feature Status ---------------- ------ slb Permanent cookie
	 * Permanent
	 * 
	 * Capacity License License Peak Usage Current Usage ---------------- ---------
	 * -------------- -------------- Throughput 4Gbps 1.35 Mbps 4.61 Kbps
	 * 
	 * @param input
	 * @return
	 * @throws OBException
	 */
	final static String SUFFIX_INFO_SWKEY = "Throughput ";//

	public OBDtoSWKeyAlteon parseSWKey(String input) throws OBException {
		OBDtoSWKeyAlteon retVal = new OBDtoSWKeyAlteon();

		String lines[] = input.split("\n");
		int strIndex = 0;
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			line = removeMoreText(line);

			strIndex = line.indexOf(SUFFIX_INFO_SWKEY);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_INFO_SWKEY.length());
				String rmWhiteSpace = OBParser.removeFirstWhitespace(item);
				String element[] = rmWhiteSpace.split(" ");// space를 기준으로 분리.
				if (element.length <= 3)
					continue;
				int unit = 0;
				int index = 0;
				// license throughput 정보 추출.
				unit = findUnit(element[index]);
				if (unit == 0) {
					String value = element[index];
					index++;
					unit = findUnit(element[index]);
					retVal.setLicense(calValue(value, unit));
				} else {// 숫자와 단위가 붙어 있는 경우.
					String value = findValue(element[index], unit);
					retVal.setLicense(calValue(value, unit));
				}
				// peak usage
				index++;
				unit = findUnit(element[index]);
				if (unit == 0) {
					String value = element[index];
					index++;
					unit = findUnit(element[index]);
					retVal.setPeakUsage(calValue(value, unit));
				} else {// 숫자와 단위가 붙어 있는 경우.
					String value = findValue(element[index], unit);
					retVal.setPeakUsage(calValue(value, unit));
				}

				// current usage
				index++;
				unit = findUnit(element[index]);
				if (unit == 0) {
					String value = element[index];
					index++;
					unit = findUnit(element[index]);
					retVal.setCurrentUsage(calValue(value, unit));
				} else {// 숫자와 단위가 붙어 있는 경우.
					String value = findValue(element[index], unit);
					retVal.setCurrentUsage(calValue(value, unit));
				}
			}
		}
		return retVal;
	}

	private Long calValue(String value, int unit) {
		if (unit == 0) {
			return (long) Float.parseFloat(value);
		}

		if (unit == 1) {
			return (long) Float.parseFloat(value);
		}
		if (unit == 2) {// Kbps
			return (long) Float.parseFloat(value) * 1000L;
		}
		if (unit == 3) {// Mbps
			return (long) Float.parseFloat(value) * 1000000L;
		}
		if (unit == 4) {// GBps
			return (long) Float.parseFloat(value) * 1000000000L;
		}

		return (long) Float.parseFloat(value);
	}

	private int findUnit(String input)// 0: not found, 1:bps, 2:Kbps, 3: Mbps, 4:Gbps
	{
		int strIndex = 0;
		strIndex = input.indexOf("Gbps");
		if (strIndex >= 0) {
			return 4;
		}
		strIndex = input.indexOf("Mbps");
		if (strIndex >= 0) {
			return 3;
		}
		strIndex = input.indexOf("Kbps");
		if (strIndex >= 0) {
			return 2;
		}
		strIndex = input.indexOf("bps");
		if (strIndex >= 0) {
			return 1;
		}

		return 0;
	}

	private String findValue(String input, int unit)// 0: not found, 1:bps, 2:Kbps, 3: Mbps, 4:Gbps
	{
		if (unit == 0)
			return input;

		if (unit == 1) {
			String element[] = input.split("bps");
			return element[0];
		}
		if (unit == 2) {
			String element[] = input.split("Kbps");
			return element[0];
		}
		if (unit == 3) {
			String element[] = input.split("Mbps");
			return element[0];
		}
		if (unit == 4) {
			String element[] = input.split("Gbps");
			return element[0];
		}

		return input;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
//			handler.telnetLogin();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndSysGeneral();//
//			System.out.println(new OBCLIParserAlteon().parseUptimeInfo(cfgDump));
//			handler.telnetDisconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}		
//	
	private final static String SUFFIX_BOOT_TYPE_POWER_CYCLE = "power cycle";
	private final static String SUFFIX_BOOT_TYPE_TELNET = "reset from Telnet";
	private final static String SUFFIX_BOOT_TYPE_PANIC = "software PANIC";
	private final static String SUFFIX_BOOT_TYPE_RESET_KEY = "console RESET KEY";

	private Integer convertBootType(String input) {
		int strIndex = 0;
		strIndex = input.indexOf(SUFFIX_BOOT_TYPE_POWER_CYCLE);
		if (strIndex >= 0) {
			return OBDtoUptimeInfo.BOOT_TYPE_POWER_CYCLE;
		}
		strIndex = input.indexOf(SUFFIX_BOOT_TYPE_TELNET);
		if (strIndex >= 0) {
			return OBDtoUptimeInfo.BOOT_TYPE_TELNET;
		}
		strIndex = input.indexOf(SUFFIX_BOOT_TYPE_PANIC);
		if (strIndex >= 0) {
			return OBDtoUptimeInfo.BOOT_TYPE_PANIC;
		}
		strIndex = input.indexOf(SUFFIX_BOOT_TYPE_RESET_KEY);
		if (strIndex >= 0) {
			return OBDtoUptimeInfo.BOOT_TYPE_RESET;
		}
		return OBDtoUptimeInfo.BOOT_TYPE_TELNET;
	}

	private final static String SUFFIX_UPTIME_CURRENT = "System Information at ";// Switch is up 18 days, 1 hour, 11
																					// minutes and 20 seconds.

	public OBDtoUptimeInfo parseUptimeInfo(String input) throws OBException {
		OBDtoUptimeInfo retVal = new OBDtoUptimeInfo();
		int strIndex = 0;
		String lines[] = input.split("\n");
		for (String line : lines) {
			if (line.isEmpty())
				continue;
			// upTime검출..
			strIndex = line.indexOf(SUFFIX_UPTIME_CURRENT);
			if (strIndex >= 0) {
				String item = line.substring(strIndex + SUFFIX_UPTIME_CURRENT.length());
				Timestamp time = OBDateTime.toTimestamp("HH:mm:ss EEE MMM dd, yyyy", item);
				if (time != null)
					retVal.setCurrentTime(time);
				else
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse a apply time(%s)", line));// retVal.setUpTime(element[0]);
				continue;
			}
			// last boot 원인 검출.
			strIndex = line.indexOf(SUFFIX_GENERAL_LASTBOOT);
			if (strIndex >= 0) {// 16:35:47 Mon Apr 22, 2013 (hard reset from console)
				String item = line.substring(strIndex + SUFFIX_GENERAL_LASTBOOT.length());
				String element[] = item.split("\\(");//
				if (element.length <= 0) {
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse boot time(%s)", line));
					continue;
				}
				retVal.setBootType(convertBootType(element[1]));
				return retVal;
			}
		}
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to parse uptime info");
	}

	private final static String SUFFIX_STATS_SLB_MAINT_ALLOC_FAILS = "Allocation failures: ";//
	private final static String SUFFIX_STATS_SLB_MAINT_VIP_DROPS = "Packets drops: vip is not up ";//

	public OBDtoStatsSlbMaint parseStatSlbMaint(String input) throws OBException {// 입력 데이터는 normalized된 상태로 입력되어야 한다.
																					// space가 두개 연속 존재해서는 안된다.
		OBDtoStatsSlbMaint retVal = new OBDtoStatsSlbMaint();
		try {
			int strIndex = 0;
			String lines[] = input.split("\n");
			for (String line : lines) {
				if (line.isEmpty())
					continue;
				// allocation failures
				strIndex = line.indexOf(SUFFIX_STATS_SLB_MAINT_ALLOC_FAILS);
				if (strIndex >= 0) {
					String item = line.substring(strIndex + SUFFIX_STATS_SLB_MAINT_ALLOC_FAILS.length());
					retVal.setAllocFails(Long.parseLong(item));
					continue;
				}
				// last boot 원인 검출.
				strIndex = line.indexOf(SUFFIX_STATS_SLB_MAINT_VIP_DROPS);
				if (strIndex >= 0) {
					String item = line.substring(strIndex + SUFFIX_STATS_SLB_MAINT_VIP_DROPS.length());
					retVal.setVipPktDrops(Long.parseLong(item));
					continue;
				}
			}

		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}

		return retVal;
	}

	private final static String SUFFIX_STATS_SLB_AUX_START = "----- ---";//

	public ArrayList<OBDtoStatsSlbAuxTable> parseStatSlbAux(String input) throws OBException {// 입력 데이터는 normalized된 상태로
																								// 입력되어야 한다. space가 두개
																								// 연속 존재해서는 안된다.
		ArrayList<OBDtoStatsSlbAuxTable> retVal = new ArrayList<OBDtoStatsSlbAuxTable>();

		try {
			int strIndex = 0;
			String lines[] = input.split("\n");
			boolean isStarted = false;
			for (String line : lines) {
				if (line.isEmpty())
					continue;

				strIndex = line.indexOf(SUFFIX_STATS_SLB_AUX_START);
				if (strIndex >= 0) {
					if (isStarted == false)
						isStarted = true;
					else// end of parsing
						break;
					continue;
				}
				if (isStarted == false)
					continue;

				String element[] = line.split(" ");//
				if (element.length != 4) {
					continue;
				}
				OBDtoStatsSlbAuxTable obj = new OBDtoStatsSlbAuxTable();
				obj.setIndex(Integer.parseInt(element[0]));
				obj.setCurrConn(Long.parseLong(element[1]));
				obj.setMaxConn(Long.parseLong(element[2]));
				obj.setAllocFails(Long.parseLong(element[3]));

				retVal.add(obj);
			}

		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}

		return retVal;
	}

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteon handler = new OBAdcAlteon("192.168.100.11", "admin", "admin");
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			boolean isOK = handler.cmndPktDumpStop();//
//			String cfgDump = handler.getCmndRetString();
//			System.out.println(new OBCLIParserAlteon().parsePktDumpInfo(cfgDump));
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}	

//    private final static String SUFFIX_PKT_DUMP_INFO 		= "packets captured;";//1000 packets captured; 149 packets dropped
//    public OBDtoPktDumpInfo parsePktDumpInfo(String input) throws OBException
//    {
//    	try
//    	{
//	    	OBDtoPktDumpInfo retVal=new OBDtoPktDumpInfo();
//	    	int strIndex=0;
//	    	String lines[] = input.split("\n");
//		    for(String line:lines)
//		    {
//		    	if(line.isEmpty())
//		    		continue;
//		    	//
//		    	strIndex=line.indexOf(SUFFIX_PKT_DUMP_INFO);
//		    	if(strIndex>=0)
//		    	{
//		    		String element[] = line.split(" ");// 
//					if(element.length<=0)
//					{
//						OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to parse boot time(%s)", line));
//						continue;
//					}
//					boolean isCaptureFlag = true;
//					for(int i=0;i<element.length;i++)
//					{
//						if(OBUtility.isInteger(element[i]))
//						{
//							if(isCaptureFlag==true)
//							{
//								isCaptureFlag=false;
//								retVal.setCaptureCount(Integer.parseInt(element[i]));
//							}
//							else
//							{
//								retVal.setDropCoun(Integer.parseInt(element[i]));
//							}
//						}
//					}
//					return retVal;
//		    	}
//		    }
//    	}
//    	catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
//		}
//	    return null;
//    }

//	public static void main(String [] args) 
//	{
//		try
//		{
//			OBAdcAlteonHandler handler = new OBAdcAlteonHandler("192.168.100.11", "admin", "admin", OBDefine.SERVICE.TELNET, OBDefine.SERVICE.TELNET);
//			//handler.enter();
//			handler.login();
//			System.out.println("login OK");
//			String cfgDump = handler.cmndInfoSessCip("172.172.2.209", 100);//100개 결과만 조회
//			System.out.println(new OBCLIParserAlteonV29().parseSeesionDumpList(1, cfgDump, -1, "SLB")); //-1:세션 결과수 제한 없음
//			handler.disconnect();
//			System.out.println("logout OK");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}	

	// 4,04: 172.172.2.209 57205, 192.168.100.231 http -> 15659 192.168.199.45 http
	// age 0 v:1
	// |-sp number
	// |- Ingress port
	// |- source ip address
	// |- source port
	// |- destination ip address
	// |- destination port
//    private final static String SUFFIX_PKT_DUMP_INFO 		= "packets captured";//1000 packets captured; 149 packets dropped

//    public ArrayList<OBDtoFaultSessionInfo> remanufactoringParsedSessionList (Integer adcIndex, ArrayList<OBDtoFaultSessionInfo> sessionList, ArrayList<OBDtoSessionSearchOption> seachKeyList ) throws OBException
//    {
//        Integer srcPort = null;
//        Integer dstPort = null;
//        Integer realPort = null;
//        String dstIP = "";
//        String protocolToString = "";
//        String srcIP = "";
//        Integer protocol = 0;
//        
//        for(OBDtoSessionSearchOption option : seachKeyList)
//        {
//            if(option.getType()==OBDtoSessionSearchOption.OPTION_TYPE_DST_PORT)
//            {    
//                dstPort = Integer.parseInt(option.getContent());
//            }
//            else if(option.getType()==OBDtoSessionSearchOption.OPTION_TYPE_SRC_IP)
//            {    
//                srcIP = option.getContent();
//            }
//            else if(option.getType()==OBDtoSessionSearchOption.OPTION_TYPE_DST_IP)
//            {    
//           	 	dstIP = option.getContent();
//            }            
//            else if(option.getType()==OBDtoSessionSearchOption.OPTION_TYPE_SRC_PORT)
//            {    
//                srcPort = Integer.parseInt(option.getContent());
//            }
//            else if(option.getType()==OBDtoSessionSearchOption.OPTION_TYPE_REAL_PORT)
//            {    
//            	realPort = Integer.parseInt(option.getContent());
//            }            
//            else if(option.getType()==OBDtoSessionSearchOption.OPTION_TYPE_PROTOCOL)
//            {    
//                 protocolToString = option.getContent();
//                 if(STRING_TO_PROTOCOL_TCP == protocolToString)
//                 {
//                	protocol = INT_TO_PROTOCOL_TCP;
//                 }
//                 else if(STRING_TO_PROTOCOL_UDP == protocolToString)
//                 {
//               	  	protocol = INT_TO_PROTOCOL_UDP;
//                 }
//                 else if(STRING_TO_PROTOCOL_ICMP == protocolToString)
//                 {
//               	  	protocol = INT_TO_PROTOCOL_ICMP;
//                 }            	  
//            }
//        }
//        ArrayList<OBDtoFaultSessionInfo> retVal = new  ArrayList<OBDtoFaultSessionInfo>();
//        for(OBDtoFaultSessionInfo info: sessionList)
//        {
//        	if(srcPort != null)
//        	{
//        		if(srcPort.equals(info.getSrcPort()) == false)
//        			continue;
//        	}
//        	if(srcIP != null && srcIP.isEmpty() == false)
//        	{
//        		if(srcIP.equals(info.getSrcIP()) == false)
//        			continue;
//        	}
//        	if(dstPort != null)
//        	{
//        		if(dstPort.equals(info.getDstPort()) == false)
//        			continue;
//        	}
//        	if(realPort != null)
//        	{
//        		if(realPort.equals(info.getRealPort()) == false)
//        			continue;
//        	}       	
//        	if(dstIP != null && dstIP.isEmpty() == false)
//        	{
//        		if(dstIP.equals(info.getDstIP()) == false)
//        			continue;
//        	}
//        	if(protocol != null && protocol != 0)
//        	{
//        		if(protocol.equals(info.getProtocol()) == false)
//        		{
//        			continue;
//        		}   			
//        	}
//        	retVal.add(info);
//        }
//    	return retVal;
//    }
}
