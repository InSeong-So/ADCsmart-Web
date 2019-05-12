package kr.openbase.adcsmart.service.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBAdcAlteonHandler;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBAdcAlteonV22;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBAdcAlteonV28;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBAdcAlteonV29;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBCLIParserAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBCLIParserAlteonV28;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBCLIParserAlteonV29;
import kr.openbase.adcsmart.service.impl.f5.handler.OBAdcF5Handler;
import kr.openbase.adcsmart.service.impl.f5.handler.OBAdcF5HandlerV11;
import kr.openbase.adcsmart.service.impl.f5.handler.OBCLIParserF5;
import kr.openbase.adcsmart.service.impl.f5.handler.OBCLIParserF5V11;
import kr.openbase.adcsmart.service.impl.pas.handler.OBAdcPASHandler;
import kr.openbase.adcsmart.service.impl.pas.handler.OBCLIParserPAS;
import kr.openbase.adcsmart.service.impl.pas.handler.dto.OBDtoSystemInfoPAS;
import kr.openbase.adcsmart.service.impl.pask.handler.OBAdcPASKHandler;
import kr.openbase.adcsmart.service.impl.pask.handler.OBCLIParserPASK;
import kr.openbase.adcsmart.service.impl.pask.handler.dto.OBDtoSystemInfoPASK;
import kr.openbase.adcsmart.service.snmp.alteon.OBSnmpAlteon;
import kr.openbase.adcsmart.service.snmp.alteon.OBSnmpAlteonV295;
import kr.openbase.adcsmart.service.snmp.f5.OBSnmpF5;
import kr.openbase.adcsmart.service.snmp.f5.OBSnmpF5V11;
import kr.openbase.adcsmart.service.snmp.pas.OBSnmpPAS;
import kr.openbase.adcsmart.service.snmp.pask.OBSnmpPASK;

public class OBCommon {
	private static Properties webProperties;

	private static String localString = "";
	private static HashMap<String, String> wellKnownPortMap = new HashMap<String, String>();

	public static String getProperties(String key) {
		if (webProperties == null) {
//			String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
//			String fileName = rootPath + "adcsmart-web.conf";
//			File f = new File(fileName);
//			if (!f.exists()) {
//				fileName = "/opt/adcsmart/cfg/adcsmart-web.conf";
//			}
			try {
				Properties properties = new Properties();
				properties.load(new FileInputStream("/opt/adcsmart/cfg/adcsmart-web.conf"));
				webProperties = properties;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
		return webProperties.getProperty(key);
	}

	public static void makeStorageDirectory(String directory) {
		try {
			File desti = new File(directory);
			if (!desti.exists()) {
				desti.mkdirs();
			}
		} catch (Exception e) {
//            e.printStackTrace();
		}
	}

	public static boolean isLockFileExist(String fileName) {
		try {
//            String fullPathName = OBDefine.DIR_LOCKFILE_SYSLOGD + fileName;
			File f = new File(fileName);
			if (f.exists() && !f.isDirectory())
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean createLockFile(String fileName) throws OBException {
		try {
//            String fullPathName = OBDefine.DIR_LOCKFILE_SYSLOGD + fileName;
			String sysDelectory = OBDefine.DIR_LOCKFILE_SYSLOGD;
			File desti = new File(sysDelectory);
			if (!desti.exists()) {
				desti.mkdirs();
			}

			File file = new File(fileName);
			if (!file.isFile()) {
				return file.createNewFile();
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, "general exception: " + e.getMessage());
		}
	}

	public static void deleteLockFile(String fileName) {
		try {
//            String fullPathName = OBDefine.DIR_LOCKFILE_SYSLOGD + fileName;
			File file = new File(fileName);
			if (file.isFile()) {
				file.delete();
			}
		} catch (Exception e) {
		}
	}

	// 테이블 이름에 로케일 정보를 붙여 리턴한다. ko_KR인 경우에는 그대로 사용한다.
	public static String makeProperTableName(String tableName) {
		String retVal = tableName;

		String locale = getLocalInfo().toUpperCase();
		if (locale.compareToIgnoreCase("KO_KR") == 0)
			return tableName;
		retVal = tableName + "_" + locale;
		return retVal;
	}

	public static String getLocalInfo() {
		if (localString != null && !localString.isEmpty())
			return localString;

		String retVal = "ko_KR";
		try {
			FileInputStream fis = new FileInputStream(OBUtility.getPropOsPath() + OBDefine.PROPERTIES_BASE);
			;
			Properties props = new Properties();
			props.load(fis);
			retVal = props.getProperty("lang.code", "ko_KR");
			localString = retVal;
		} catch (Exception e) {
			localString = "";
		}
		return retVal;
	}

	public static String getLanguageCode(String langCode) {
		String elements[] = langCode.split("_");
		if (elements.length != 2)
			return "ko";// default language is korean
		return elements[0];
	}

	public ArrayList<OBDtoAdcCoverage> getAdcSwVersionSupport(Integer adcType) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoAdcCoverage> list = new ArrayList<OBDtoAdcCoverage>();

		String sqlText = "";
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT CONDITION_VALUE															"
							+ " FROM MNG_PRODUCT_COVERAGE                        									"
							+ " WHERE ADC_TYPE = %d AND CONDITION_TYPE = 'SW_VERSION' AND COVER_ACTION = 1   	    ",
					adcType);

			ResultSet rs;
			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcCoverage obj = new OBDtoAdcCoverage();
				obj.setConditionVersion(db.getString(rs, "CONDITION_VALUE"));
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public ArrayList<OBDtoAdcCoverage> getAdcSwVersionDeny(Integer adcType) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoAdcCoverage> list = new ArrayList<OBDtoAdcCoverage>();

		String sqlText = "";
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT CONDITION_VALUE															"
							+ " FROM MNG_PRODUCT_COVERAGE                        									"
							+ " WHERE ADC_TYPE = %d AND CONDITION_TYPE = 'SW_VERSION' AND COVER_ACTION = 0   	    ",
					adcType);

			ResultSet rs;
			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcCoverage obj = new OBDtoAdcCoverage();
				obj.setConditionVersion(db.getString(rs, "CONDITION_VALUE"));
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public static String getCountryCode(String langCode) {
		String elements[] = langCode.split("_");
		if (elements.length != 2)
			return "KR";// default language is korean
		return elements[1];
	}

	public static String getWellknownPort(String name) {
		if (wellKnownPortMap.size() == 0) {
//			wellKnownPortMap = new HashMap<String, String>();
			// /etc/services 파일에서 읽어 들인다.
			BufferedReader br;
			try {
				br = new BufferedReader(new FileReader("/etc/services"));
				String line;
				while ((line = br.readLine()) != null) {
					if (line.length() <= 0)
						continue;
					if (line.charAt(0) == '#')
						continue;// # comment 처리.
					// process the line.
					line = OBParser.convertMultipleSpaces2SingleSpace(line);
					if (line.charAt(0) == '#')
						continue;// # comment 처리.
					String element[] = line.split(" "); // fxuptp 19539/udp # FXUPTP
					if (element.length < 2)
						continue;
					String portInfo[] = element[1].split("/");
					wellKnownPortMap.put(element[0], portInfo[0]);
				}
				br.close();
			}
//			catch(FileNotFoundException e)
//			{
//				wellKnownPortMap = new HashMap<String, Integer>();
//				wellKnownPortMap.put("http", 80);
//				wellKnownPortMap.put("dns", 53);
//				wellKnownPortMap.put("https", 443);
//				wellKnownPortMap.put("ntp", 123);
//				wellKnownPortMap.put("nntp", 119);
//				wellKnownPortMap.put("sftp", 115);
//				wellKnownPortMap.put("pop3", 110);
//				wellKnownPortMap.put("ftp", 21);
//				wellKnownPortMap.put("ssh", 22);
//				wellKnownPortMap.put("telnet", 23);
//				wellKnownPortMap.put("smtp", 25);
//				wellKnownPortMap.put("tftp", 69);
//				wellKnownPortMap.put("pop2", 109);
//				wellKnownPortMap.put("netbios", 137);
//				wellKnownPortMap.put("snmp", 161);
//				wellKnownPortMap.put("snmptrap", 162);
//				wellKnownPortMap.put("bgp", 179);
//				wellKnownPortMap.put("rlogin", 513);
//				wellKnownPortMap.put("rpc", 530);
//				wellKnownPortMap.put("ntp", 123);
//				wellKnownPortMap.put("rtsp", 554);
//				wellKnownPortMap.put("ftp-data", 20);
//			}
//			catch(IOException e)
//			{
//				wellKnownPortMap = new HashMap<String, Integer>();
//				wellKnownPortMap.put("http", 80);
//				wellKnownPortMap.put("dns", 53);
//				wellKnownPortMap.put("https", 443);
//				wellKnownPortMap.put("ntp", 123);
//				wellKnownPortMap.put("nntp", 119);
//				wellKnownPortMap.put("sftp", 115);
//				wellKnownPortMap.put("pop3", 110);
//				wellKnownPortMap.put("ftp", 21);
//				wellKnownPortMap.put("ssh", 22);
//				wellKnownPortMap.put("telnet", 23);
//				wellKnownPortMap.put("smtp", 25);
//				wellKnownPortMap.put("tftp", 69);
//				wellKnownPortMap.put("pop2", 109);
//				wellKnownPortMap.put("netbios", 137);
//				wellKnownPortMap.put("snmp", 161);
//				wellKnownPortMap.put("snmptrap", 162);
//				wellKnownPortMap.put("bgp", 179);
//				wellKnownPortMap.put("rlogin", 513);
//				wellKnownPortMap.put("rpc", 530);
//				wellKnownPortMap.put("ntp", 123);
//				wellKnownPortMap.put("rtsp", 554);
//				wellKnownPortMap.put("ftp-data", 20);
//			}
			catch (Exception e) {
				wellKnownPortMap = new HashMap<String, String>();
				wellKnownPortMap = new HashMap<String, String>();
				wellKnownPortMap.put("http", "80");
				wellKnownPortMap.put("dns", "53");
				wellKnownPortMap.put("https", "443");
				wellKnownPortMap.put("ntp", "123");
				wellKnownPortMap.put("nntp", "119");
				wellKnownPortMap.put("sftp", "115");
				wellKnownPortMap.put("pop3", "110");
				wellKnownPortMap.put("ftp", "21");
				wellKnownPortMap.put("ssh", "22");
				wellKnownPortMap.put("telnet", "23");
				wellKnownPortMap.put("smtp", "25");
				wellKnownPortMap.put("tftp", "69");
				wellKnownPortMap.put("pop2", "109");
				wellKnownPortMap.put("netbios", "137");
				wellKnownPortMap.put("snmp", "161");
				wellKnownPortMap.put("snmptrap", "162");
				wellKnownPortMap.put("bgp", "179");
				wellKnownPortMap.put("rlogin", "513");
				wellKnownPortMap.put("rpc", "530");
				wellKnownPortMap.put("ntp", "123");
				wellKnownPortMap.put("rtsp", "554");
				wellKnownPortMap.put("ftp-data", "20");
			}
		}
		String retVal = wellKnownPortMap.get(name);
		if (retVal == null) {
			return name;
		}
		return retVal;
	}

	/**
	 * ADC 장비에 접속하여 adc type 정보를 리턴한다.
	 * 
	 * @param adcInfo
	 * @return
	 * @throws OBException
	 */
	public static Integer checkAdcType(String ipAddress, String accountID, String passwd, String cliAccountID,
			String cliPasswd, Integer adcType, int connService, int connPort) throws OBException {
		if (adcType == OBDefine.ADC_TYPE_PIOLINK_UNKNOWN) {
			OBAdcPASHandler pasHandler = getValidPASHandler("");
			pasHandler.setConnectionInfo(ipAddress, accountID, passwd, connService, connPort);
			OBAdcPASKHandler paskHandler = getValidPASKHandler("");
			paskHandler.setConnectionInfo(ipAddress, accountID, passwd, connService, connPort);
			try {
				pasHandler.login();
				// 순차적으로 시스템 정보를 추출해 본다. PAS, PASK
				String systemInfo = pasHandler.cmndSystem();
				OBDtoSystemInfoPAS pasInfo = new OBCLIParserPAS().parseSystem(systemInfo);
				if (pasInfo.getProductName().contains("PAS ")) {
					pasHandler.disconnect();
					return OBDefine.ADC_TYPE_PIOLINK_PAS;
				}
				pasHandler.disconnect();
			} catch (OBException e) {
				pasHandler.disconnect();
			} catch (Exception e) {
				pasHandler.disconnect();
			}

			try {
				paskHandler.login();
				// 순차적으로 시스템 정보를 추출해 본다. PAS, PASK
				String systemInfo = paskHandler.cmndSystem();
				OBDtoSystemInfoPASK pasInfo = new OBCLIParserPASK().parseSystem(systemInfo);
				if (pasInfo.getProductName().contains("PAS-K")) {
					paskHandler.disconnect();
					return OBDefine.ADC_TYPE_PIOLINK_PASK;
				}
				paskHandler.disconnect();
			} catch (OBException e) {
				paskHandler.disconnect();
			} catch (Exception e) {
				paskHandler.disconnect();
			}
			return OBDefine.ADC_TYPE_PIOLINK_UNKNOWN;
		}
		return adcType;
	}

	public static boolean checkVersionPAS(String adcVersion) throws OBException {
		if (null == adcVersion)
			return false;
		if (adcVersion.length() == 0)
			return false;

		ArrayList<OBDtoAdcCoverage> listSupport = new OBCommon().getAdcSwVersionSupport(OBDefine.ADC_TYPE_PIOLINK_PAS);
		ArrayList<OBDtoAdcCoverage> listDeny = new OBCommon().getAdcSwVersionDeny(OBDefine.ADC_TYPE_PIOLINK_PAS);
		int listSupportLength = listSupport.size();
		int listDenyLength = listDeny.size();
		if (!listDeny.isEmpty()) {
			for (int i = 0; i < listDenyLength; i++) {
				if (adcVersion.indexOf(listDeny.get(i).getConditionVersion()) == 0) {
					return false;
				}
			}
		}
		for (int i = 0; i < listSupportLength; i++) {
			if (adcVersion.indexOf(listSupport.get(i).getConditionVersion()) == 0) {
				return true;
			}
		}

		return false;
	}

	public static boolean checkVersionPASK(String adcVersion) throws OBException {
		if (null == adcVersion)
			return false;
		if (adcVersion.length() == 0)
			return false;

		ArrayList<OBDtoAdcCoverage> listSupport = new OBCommon().getAdcSwVersionSupport(OBDefine.ADC_TYPE_PIOLINK_PASK);
		ArrayList<OBDtoAdcCoverage> listDeny = new OBCommon().getAdcSwVersionDeny(OBDefine.ADC_TYPE_PIOLINK_PASK);
		int listSupportLength = listSupport.size();
		int listDenyLength = listDeny.size();
		if (!listDeny.isEmpty()) {
			for (int i = 0; i < listDenyLength; i++) {
				if (adcVersion.indexOf(listDeny.get(i).getConditionVersion()) == 0) {
					return false;
				}
			}
		}
		for (int i = 0; i < listSupportLength; i++) {
			if (adcVersion.indexOf(listSupport.get(i).getConditionVersion()) == 0) {
				return true;
			}
		}

		return false;
	}

	public static boolean checkVersionAlteon(String adcVersion) throws OBException {
		if (null == adcVersion)
			return false;
		if (adcVersion.length() == 0)
			return false;

		ArrayList<OBDtoAdcCoverage> listSupport = new OBCommon().getAdcSwVersionSupport(OBDefine.ADC_TYPE_ALTEON);
		ArrayList<OBDtoAdcCoverage> listDeny = new OBCommon().getAdcSwVersionDeny(OBDefine.ADC_TYPE_ALTEON);
		int listSupportLength = listSupport.size();
		int listDenyLength = listDeny.size();
		if (!listDeny.isEmpty()) {
			for (int i = 0; i < listDenyLength; i++) {
				if (adcVersion.indexOf(listDeny.get(i).getConditionVersion()) == 0) {
					return false;
				}
			}
		}
		for (int i = 0; i < listSupportLength; i++) {
			if (adcVersion.indexOf(listSupport.get(i).getConditionVersion()) == 0) {
				return true;
			}
		}

		return false;
	}

	public static boolean checkVersionF5(String adcVersion) throws OBException {
		if (null == adcVersion)
			return false;
		if (adcVersion.length() == 0)
			return false;

		ArrayList<OBDtoAdcCoverage> listSupport = new OBCommon().getAdcSwVersionSupport(OBDefine.ADC_TYPE_F5);
		ArrayList<OBDtoAdcCoverage> listDeny = new OBCommon().getAdcSwVersionDeny(OBDefine.ADC_TYPE_F5);
		int listSupportLength = listSupport.size();
		int listDenyLength = listDeny.size();
		if (!listDeny.isEmpty()) {
			for (int i = 0; i < listDenyLength; i++) {
				if (adcVersion.indexOf(listDeny.get(i).getConditionVersion()) == 0) {
					return false;
				}
			}
		}
		for (int i = 0; i < listSupportLength; i++) {
			if (adcVersion.indexOf(listSupport.get(i).getConditionVersion()) == 0) {
				return true;
			}
		}

		return false;
	}

	public static boolean checkSysIdByAlteon(String adcVersion) throws OBException {
		float sysIdByNumVersion = 29.5f;

		if (null == adcVersion)
			return false;
		if (adcVersion.length() == 0)
			return false;

		float version = Float.parseFloat(adcVersion.substring(0, 4));

		if (version >= sysIdByNumVersion) {
			return true;
		}

		return false;
	}

//	public static void main(String[] args)
//	{
////		try
////		{
////			boolean aaa = OBCommon.checkSysIdByAlteon("29.4.0");
////			if(aaa == true)
////			{
////				System.out.println("true");
////			}
////			else
////			{
////				System.out.println("false");
////			}
//		stringToascii("aa");
//
////		}
////		catch(OBException e)
////		{
////			e.printStackTrace();
////		}
//	}

	public static OBAdcAlteonHandler getValidAlteonHandler(String swVersion) throws OBException {
		OBAdcAlteonHandler alteon = null;

		if (swVersion == null || swVersion.isEmpty()) {
			return new OBAdcAlteonHandler();
		}

		String[] verElements = swVersion.split("\\.", 2); // 버전 요소 두개까지 문자열로 건진다. 지금은 하나만 쓴다.
		int version = Integer.parseInt(verElements[0]);
		if (version == 22) {
			alteon = new OBAdcAlteonV22();
		} else if (version == 28) {
			alteon = new OBAdcAlteonV28();
		} else if (version >= 29) {
			alteon = new OBAdcAlteonV29();
		}
//        else if(version >= 30)
//		{
//			alteon = new OBAdcAlteonV30();
//		}
		else {
			alteon = new OBAdcAlteonHandler();
		}
		return alteon;
	}

	public static OBCLIParserAlteon getValidAlteonCLIParser(String swVersion) throws OBException {
		OBCLIParserAlteon alteon = null;

		if (swVersion == null || swVersion.isEmpty()) {
			return new OBCLIParserAlteon();
		}

		String[] verElements = swVersion.split("\\.", 2); // 버전 요소 두개까지 문자열로 건진다. 지금은 하나만 쓴다.
		int version = Integer.parseInt(verElements[0]);
		if (version == 28) {
			alteon = new OBCLIParserAlteonV28();
		} else if (version >= 29) {
			alteon = new OBCLIParserAlteonV29();
		} else {
			alteon = new OBCLIParserAlteon();
		}
		return alteon;
	}

	public static OBCLIParserPAS getValidPASCLIParser(String swVersion) throws OBException {
		if (swVersion == null || swVersion.isEmpty()) {
			return new OBCLIParserPAS();
		}

		return new OBCLIParserPAS();
	}

	public static OBCLIParserPASK getValidPASKCLIParser(String swVersion) throws OBException {
		if (swVersion == null || swVersion.isEmpty()) {
			return new OBCLIParserPASK();
		}

		return new OBCLIParserPASK();
	}

	public static OBCLIParserF5 getValidF5CLIParser(String swVersion) throws OBException {
		OBCLIParserF5 f5 = null;

		if (swVersion == null || swVersion.isEmpty()) {
			return new OBCLIParserF5();
		}

		String[] verElements = swVersion.split("\\.", 2); // 버전 요소 두개까지 문자열로 건진다. 지금은 하나만 쓴다.
		if (Integer.parseInt(verElements[0]) >= 11) {
			f5 = new OBCLIParserF5V11();
		} else {
			f5 = new OBCLIParserF5();
		}
		return f5;
	}

	public static OBAdcF5Handler getValidF5Handler(String swVersion) throws OBException {
		OBAdcF5Handler f5 = null;

		if (swVersion == null || swVersion.isEmpty()) {
			return new OBAdcF5Handler();
		}

		String[] verElements = swVersion.split("\\.", 2); // 버전 요소 두개까지 문자열로 건진다. 지금은 하나만 쓴다.
		if (Integer.parseInt(verElements[0]) >= 11) {
			f5 = new OBAdcF5HandlerV11();
		} else {
			f5 = new OBAdcF5Handler();
		}
		return f5;
	}

	public static OBSnmpF5 getValidSnmpF5Handler(String swVersion, String host, OBDtoAdcSnmpInfo snmpInfo)
			throws OBException {
		OBSnmpF5 f5 = null;

		if (swVersion == null || swVersion.isEmpty()) {
			return new OBSnmpF5(host, snmpInfo);
		}

		String[] verElements = swVersion.split("\\.", 2); // 버전 요소 두개까지 문자열로 건진다. 지금은 하나만 쓴다.
		if (Integer.parseInt(verElements[0]) >= 11) {
			f5 = new OBSnmpF5V11(host, snmpInfo);
		} else {
			f5 = new OBSnmpF5(host, snmpInfo);
		}
		return f5;
	}

	public static OBSnmpAlteon getValidSnmpAlteonHandler(String swVersion, String host, OBDtoAdcSnmpInfo snmpInfo)
			throws OBException {
		OBSnmpAlteon alteon = null;

		if (swVersion == null || swVersion.isEmpty()) {
			return new OBSnmpAlteon(host, snmpInfo);
		}

		String[] verElements = swVersion.split("\\."); // 버전 요소 두개까지 문자열로 건진다. 지금은 하나만 쓴다.
		if (Integer.parseInt(verElements[0]) >= 30
				|| (Integer.parseInt(verElements[0]) == 29 && Integer.parseInt(verElements[1]) == 5)) {
			alteon = new OBSnmpAlteonV295(host, snmpInfo);
		} else {
			alteon = new OBSnmpAlteon(host, snmpInfo);
		}
		return alteon;
	}

	public static OBSnmpPAS getValidSnmpPASHandler(String swVersion, String host, OBDtoAdcSnmpInfo snmpInfo)
			throws OBException {
		return new OBSnmpPAS(host, snmpInfo);
	}

	public static OBSnmpPASK getValidSnmpPASKHandler(String swVersion, String host, OBDtoAdcSnmpInfo snmpInfo)
			throws OBException {
		return new OBSnmpPASK(host, snmpInfo);
	}

	public static OBAdcPASHandler getValidPASHandler(String swVersion) throws OBException {
		if (swVersion == null || swVersion.isEmpty()) {
			return new OBAdcPASHandler();
		}
		return new OBAdcPASHandler();
	}

	public static OBAdcPASKHandler getValidPASKHandler(String swVersion) throws OBException {
		if (swVersion == null || swVersion.isEmpty()) {
			return new OBAdcPASKHandler();
		}
		return new OBAdcPASKHandler();
	}

	public static String makeVSrcIndexPASByIndex(Integer adcIndex, String poolIndex) {
		return poolIndex;
	}

	public static String makePoolMemberIndexPASK(Integer adcIndex, String vsIndex, String nodeIndex, Integer nodePort) {
		return adcIndex + "_" + vsIndex + "_" + nodeIndex + "_" + nodePort;
	}

	public static String makePoolMemberIndex(Integer adcIndex, String poolIndex, String nodeIndex, Integer nodePort) {
		return adcIndex + "_" + poolIndex + "_" + nodeIndex + "_" + nodePort;
	}

	public static String makeVSPoolMemberIndex(String vsIndex, String poolMemberIndex) {
		return vsIndex + "_" + poolMemberIndex;
	}

	public static String makePoolMemberIndexPAS(Integer adcIndex, String vsIndex, String nodeIndex, Integer nodePort) {
		return adcIndex + "_" + vsIndex + "_" + nodeIndex + "_" + nodePort;
	}

	public static String makePoolMemberStatusIndexAlteon(Integer adcIndex, String vsAlteonID, Integer vsPort,
			Integer srcPort, String poolID, String nodeID, Integer nodePort) {
		return adcIndex + "_" + vsAlteonID + "_" + vsPort + "_" + srcPort + "_" + poolID + "_" + nodeID + "_"
				+ nodePort;
	}

	public static String makePoolMemberStatusIndexF5(Integer adcIndex, String vsID, Integer vsPort, Integer srcPort,
			String poolID, String nodeID, Integer nodePort) {
		return adcIndex + "_" + vsID + "_" + vsPort + "_" + srcPort + "_" + poolID + "_" + nodeID + "_" + nodePort;
	}

	public static String makePoolMemberStatusIndexPAS(Integer adcIndex, String vsName, String poolName,
			Integer nodeID) {
		return adcIndex + "_" + "_" + vsName + "_" + poolName + "_" + nodeID;
	}

	public static String makePoolMemberStatusIndexPASK(Integer adcIndex, String vsName, String poolName,
			Integer nodeID) {
		return adcIndex + "_" + "_" + vsName + "_" + poolName + "_" + nodeID;
	}

	public static String makePoolMemberIndexAlteon(Integer adcIndex, String poolID, String nodeID, Integer nodePort) {
		String poolIndex = adcIndex + "_" + poolID;
		String nodeIndex = adcIndex + "_" + nodeID;

		return adcIndex + "_" + poolIndex + "_" + nodeIndex + "_" + nodePort;
	}

	public static String makePoolMemberIndexF5(Integer adcIndex, String poolID, String nodeID, Integer nodePort) {
		String poolIndex = adcIndex + "_" + poolID;
		String nodeIndex = adcIndex + "_" + nodeID;

		return adcIndex + "_" + poolIndex + "_" + nodeIndex + "_" + nodePort;
	}

	public static String makePoolMemberIndexF5ByIndex(Integer adcIndex, String poolIndex, String nodeIndex,
			Integer nodePort) {
		return adcIndex + "_" + poolIndex + "_" + nodeIndex + "_" + nodePort;
	}

	public static String makeVSvcIndex(Integer adcIndex, String vsIndex, Integer vport) {
		return adcIndex + "_" + vsIndex + "_" + vport;
	}

	public static String makeVSrcIndexAlteon(Integer adcIndex, String vsIndex, Integer vport) {
		return adcIndex + "_" + vsIndex + "_" + vport;
	}

	public static String makeVSrcIndexF5(Integer adcIndex, String vsIndex, Integer vport) {
		return adcIndex + "_" + vsIndex + "_" + vport;
	}

	public static String makeVSIndexF5(Integer adcIndex, String vsName) {
		return adcIndex + "_" + vsName;
	}

	public static String makeVSIndex(Integer adcIndex, String vsNameID) {
		return adcIndex + "_" + vsNameID;
	}

	public static String makeVSIndexAlteon(Integer adcIndex, String alteonID) {
		return adcIndex + "_" + alteonID;
	}

	public static String makeFilterIndexAlteon(Integer adcIndex, Integer alteonID) {
		return adcIndex + "_" + alteonID;
	}

	public static String makeVSIndexPAS(Integer adcIndex, String vsName) {
		return adcIndex + "_" + vsName;
	}

	public static String makeVSIndexPASK(Integer adcIndex, String vsName) {
		return adcIndex + "_" + vsName;
	}

	public static String makeHealthDbIndexPAS(Integer adcIndex, String vsDBIndex) {
		return adcIndex + "_" + vsDBIndex;
	}

	public static String makeHealthDbIndexPASK(Integer adcIndex, Integer healthID) {
		return adcIndex + "_" + healthID;
	}

	public static String makeHealthDbIndexAlteon(Integer adcIndex, String healthID) {
		return adcIndex + "_" + healthID;
	}

	public static String makeNodeIndexPASK(Integer adcIndex, Integer nodeIndex) {
		return adcIndex + "_" + nodeIndex;
	}

	public static String makeNodeIndexPAS(Integer adcIndex, String poolName, Integer nodeIndex) {
		return adcIndex + "_" + poolName + "_" + nodeIndex;
	}

	public static String makeNodeIndexF5(Integer adcIndex, String ipAddress) {
		return adcIndex + "_" + ipAddress;
	}

	public static String makeNodeIndexAlteon(Integer adcIndex, String nodeIndex) {
		return adcIndex + "_" + nodeIndex;
	}

	public static String makePoolIndexAlteon(Integer adcIndex, String poolID) {
		return adcIndex + "_" + poolID;
	}

	public static String makeNodeBakupIndexAlteon(Integer adcIndex, String nodeIndex) {
		return adcIndex + "_" + nodeIndex;
	}

	public static String makePoolBackupIndexAlteon(Integer adcIndex, String poolID) {
		return adcIndex + "_" + poolID;
	}

	public static String makePoolIndex(Integer adcIndex, Integer poolID) {
		return adcIndex + "_" + poolID;
	}

//	public static String makePoolIndexF5(Integer adcIndex, String poolName)
//	{
//		return adcIndex+"_"+poolName;
//	}
//	public static String makePoolIndexPAS(Integer adcIndex, String poolName)
//	{
//		return adcIndex+"_"+poolName;
//	}
	public static String makePoolIndex(Integer adcIndex, String poolName) {
		return adcIndex + "_" + poolName;
	}

	public static String makePoolIndex11(Integer adcIndex, Integer adcType, String poolName, String poolIndex) {
		if (adcType == OBDefine.ADC_TYPE_ALTEON)
			return makePoolIndex(adcIndex, poolIndex);
		else if (adcType == OBDefine.ADC_TYPE_F5)
			return makePoolIndex(adcIndex, poolName);
		else if (adcType == OBDefine.ADC_TYPE_PIOLINK_PAS)
			return makePoolIndex(adcIndex, poolName);
		else if (adcType == OBDefine.ADC_TYPE_PIOLINK_PASK)
			return makePoolIndex(adcIndex, poolIndex);
		else
			return "";
	}

	public static String makeProfileIndexF5(Integer adcIndex, String poolName) {
		return adcIndex + "_" + poolName;
	}

	public static String makeCertIndexF5(Integer adcIndex, String certName) {
		return adcIndex + "_" + certName;
	}

	// healthcheck: Integer --> String : 문자열 형태로 설정에 쓴다. PAS, PASK
	public static String convertHealthCheckTypeInteger2String(Integer healthType) {
		if (healthType == null) {
			return null;
		} else if (healthType.equals(OBDefine.HEALTH_CHECK.TCP)) {
			return OBDefine.HEALTH_CHECK.TCP_STR;
		} else if (healthType.equals(OBDefine.HEALTH_CHECK.HTTP)) {
			return OBDefine.HEALTH_CHECK.HTTP_STR;
		} else if (healthType.equals(OBDefine.HEALTH_CHECK.UDP)) {
			return OBDefine.HEALTH_CHECK.UDP_STR;
		} else if (healthType.equals(OBDefine.HEALTH_CHECK.ICMP)) {
			return OBDefine.HEALTH_CHECK.ICMP_STR;
		} else {
			return OBDefine.HEALTH_CHECK.NA_STR;
		}
	}

	// healthcheck: String --> Integer
	public static Integer convertHealthCheckTypeString2Integer(String healthType) {
		if (healthType == null) {
			return null;
		} else if (healthType.equals(OBDefine.HEALTH_CHECK.TCP_STR) == true) {
			return OBDefine.HEALTH_CHECK.TCP;
		} else if (healthType.equals(OBDefine.HEALTH_CHECK.HTTP_STR) == true) {
			return OBDefine.HEALTH_CHECK.HTTP;
		} else if (healthType.equals(OBDefine.HEALTH_CHECK.UDP_STR)) {
			return OBDefine.HEALTH_CHECK.UDP;
		} else if (healthType.equals(OBDefine.HEALTH_CHECK.ICMP_STR) == true) {
			return OBDefine.HEALTH_CHECK.ICMP;
		} else {
			return OBDefine.HEALTH_CHECK.NA;
		}
		// return OBDefine.COMMON_NOT_ALLOWED;
	}

	// ADCsmart DB에서 꺼내서 순수하게 디스플레이 하는 용도로만 쓴다. int --> str
	public String convertHealthCheckTypeForDisplay(int healthcheck) {
		String ret = "";
		switch (healthcheck) {
		case OBDefine.HEALTH_CHECK.TCP:
			ret = OBDefine.HEALTH_CHECK.TCP_VIEW;
			break;
		case OBDefine.HEALTH_CHECK.HTTP:
			ret = OBDefine.HEALTH_CHECK.HTTP_VIEW;
			break;
		case OBDefine.HEALTH_CHECK.HTTPS:
			ret = OBDefine.HEALTH_CHECK.HTTPS_VIEW;
			break;
		case OBDefine.HEALTH_CHECK.UDP:
			ret = OBDefine.HEALTH_CHECK.UDP_VIEW;
			break;
		case OBDefine.HEALTH_CHECK.ICMP:
			ret = OBDefine.HEALTH_CHECK.ICMP_VIEW;
			break;
		case OBDefine.HEALTH_CHECK.GATEWAY_ICMP:
			ret = OBDefine.HEALTH_CHECK.GATEWAY_ICMP_VIEW;
			break;
		case OBDefine.HEALTH_CHECK.ARP:
			ret = OBDefine.HEALTH_CHECK.ARP_VIEW;
			break;
		case OBDefine.HEALTH_CHECK.LINK:
			ret = OBDefine.HEALTH_CHECK.LINK_VIEW;
			break;
		default:
			ret = "etc"; // not allowed
		}
		return ret;
	}

	// protocol : Integer --> String
	public static Integer convertProtocolString2Integer(String protocol) {
		if (protocol == null) {
			return null;
		} else if (protocol.equals(OBDefine.PROTOCOL_TCP_STRING)) {
			return OBDefine.PROTOCOL_TCP;
		} else if (protocol.equals(OBDefine.PROTOCOL_UDP_STRING)) {
			return OBDefine.PROTOCOL_UDP;
		} else if (protocol.equals(OBDefine.PROTOCOL_ICMP_STRING)) {
			return OBDefine.PROTOCOL_ICMP;
		} else {
			return OBDefine.COMMON_NOT_ALLOWED;
		}
	}

	// protocol : Integer --> String
	public static String convertProtocolInteger2String(Integer protocol) {
		if (protocol == null) {
			return null;
		} else if (protocol.equals(OBDefine.PROTOCOL_TCP)) {
			return OBDefine.PROTOCOL_TCP_STRING;
		} else if (protocol.equals(OBDefine.PROTOCOL_UDP)) {
			return OBDefine.PROTOCOL_UDP_STRING;
		} else if (protocol.equals(OBDefine.PROTOCOL_ICMP)) {
			return OBDefine.PROTOCOL_ICMP_STRING;
		} else {
			return OBDefine.PROTOCOL_NA_STRING;
		}
	}

	// Load Balancing Method
	public static String convertLBMethodInteger2StringPASK(Integer lbMethod) {
		if (lbMethod == null) {
			return null;
		} else if (lbMethod == OBDefine.LB_METHOD_ROUND_ROBIN) {
			return OBDefine.LB_METHOD_ROUND_ROBIN_STR;
		} else if (lbMethod == OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER) {
			return OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER_STR;
		} else if (lbMethod == OBDefine.LB_METHOD_HASH) {
			return OBDefine.LB_METHOD_PASK_HASH_STR;
		} else {
			return OBDefine.LB_METHOD_NA_STR;
		}
	}

	public static String decodeURIComponent(String encodedURI) {
		char actualChar;

		StringBuffer buffer = new StringBuffer();

		int bytePattern, sumb = 0;

		for (int i = 0, more = -1; i < encodedURI.length(); i++) {
			actualChar = encodedURI.charAt(i);

			switch (actualChar) {
			case '%':
				actualChar = encodedURI.charAt(++i);
				int hb = (Character.isDigit(actualChar) ? actualChar - '0'
						: 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
				actualChar = encodedURI.charAt(++i);
				int lb = (Character.isDigit(actualChar) ? actualChar - '0'
						: 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
				bytePattern = (hb << 4) | lb;
				break;
			case '+':
				bytePattern = ' ';
				break;
			default:
				bytePattern = actualChar;
				break;
			}

			if ((bytePattern & 0xc0) == 0x80) { // 10xxxxxx
				sumb = (sumb << 6) | (bytePattern & 0x3f);
				if (--more == 0)
					buffer.append((char) sumb);
			} else if ((bytePattern & 0x80) == 0x00) { // 0xxxxxxx
				buffer.append((char) bytePattern);
			} else if ((bytePattern & 0xe0) == 0xc0) { // 110xxxxx
				sumb = bytePattern & 0x1f;
				more = 1;
			} else if ((bytePattern & 0xf0) == 0xe0) { // 1110xxxx
				sumb = bytePattern & 0x0f;
				more = 2;
			} else if ((bytePattern & 0xf8) == 0xf0) { // 11110xxx
				sumb = bytePattern & 0x07;
				more = 3;
			} else if ((bytePattern & 0xfc) == 0xf8) { // 111110xx
				sumb = bytePattern & 0x03;
				more = 4;
			} else { // 1111110x
				sumb = bytePattern & 0x01;
				more = 5;
			}
		}
		return buffer.toString();
	}

	public static ArrayList<Integer> stringToascii(String index) {
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		for (int i = 0; i < index.length(); ++i) {
			char c = index.charAt(i);
			int j = c;
			retVal.add(j);
		}
		return retVal;
	}

	public static String makeColumnSql(ArrayList<Integer> indexList, String columnName) throws OBException {
		String retVal = "";
		int i = 66;
		if (indexList == null)
			return retVal;

		for (Integer index : indexList) {
			String next = Character.toString((char) i);
			retVal += String.format(" , %s.%s AS %s_%s            \n", next, columnName, next, columnName);
			i++;
		}

		return retVal;
	}

	public static String makeTableSql(ArrayList<Integer> indexList, String tableName, String indexName, String column,
			String timeSql) throws OBException {
		String retVal = "";
		int i = 66;
		if (indexList == null)
			return retVal;

		for (Integer index : indexList) {
			String next = Character.toString((char) i);
			String whereSql = String.format(" WHERE %s = %d", indexName, index);
			String join = String.format(" ON A.OCCUR_TIME = %s.OCCUR_TIME", next);
			retVal += String.format(
					" \n LEFT JOIN (SELECT OCCUR_TIME %s  FROM %s             \n"
							+ " %s %s) %s                                                              \n"
							+ " %s                                                                  \n",
					column, tableName, whereSql, timeSql, next, join);
			i++;
		}

		return retVal;
	}

	public static double calcRateOfChange(Long currentValue, Long preValue, Long subtraction) throws OBException {
		double result = -1L;

		if (preValue == 0)
			preValue = 1L;

		if (currentValue == 0)
			currentValue = 1L;

		if (preValue != 0 && currentValue != 0) {
			result = (((double) subtraction) / (double) preValue) * 100d;
			result = Math.round(result * 100d) / 100d;
		}
		return result;
	}

	public static double calcRateOfChange(int currentValue, int preValue, int subtraction) throws OBException {
		double result = -1L;

		if (preValue == 0)
			preValue = 1;

		if (currentValue == 0)
			currentValue = 1;

		if (preValue != 0 && currentValue != 0) {
			result = (((double) subtraction) / (double) preValue) * 100d;
			result = Math.round(result * 100d) / 100d;
		}
		return result;
	}
}
