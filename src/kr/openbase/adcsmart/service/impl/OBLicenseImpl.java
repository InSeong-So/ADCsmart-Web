package kr.openbase.adcsmart.service.impl;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.OBLicenseManagement;
import kr.openbase.adcsmart.service.OBSystemAudit;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoLicense;
import kr.openbase.adcsmart.service.dto.OBDtoLicenseInfo;
import kr.openbase.adcsmart.service.dto.OBDtoValidLicense;
import kr.openbase.adcsmart.service.impl.dto.OBDtoVersionInfo;
import kr.openbase.adcsmart.service.jna.JNALicenseLibrary;
import kr.openbase.adcsmart.service.jna.dto.STLicenseInfo;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBFileNotFoundException;
import kr.openbase.adcsmart.service.utility.OBLicenseInvalidFormatException;
import kr.openbase.adcsmart.service.utility.OBNetwork;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBLicenseImpl implements OBLicenseManagement {
//	/**
//	* Convert the int to an byte array.
//	* @param integer The integer
//	* @return The byte array
//	*/
//	private static byte[] intToByteArray(final int integer) 
//	{
//		ByteBuffer buff = ByteBuffer.allocate(Integer.SIZE / 8);
//		buff.putInt(integer);
//		buff.order(ByteOrder.BIG_ENDIAN);
//		return buff.array();
//	}
//	
//	private static byte[] longToByteArray(final long longInt) 
//	{
//		ByteBuffer buff = ByteBuffer.allocate(Long.SIZE / 8);
//		buff.putLong(longInt);
//		buff.order(ByteOrder.BIG_ENDIAN);
//		return buff.array();
//	}
//	
//	private static long ipToLong(String addr) 
//	{
//        String[] addrArray = addr.split("\\.");
//
//        long num = 0;
//        for (int i = 0; i < addrArray.length; i++) 
//        {
//            int power = 3 - i;
//            num += ((Integer.parseInt(addrArray[i]) % 256 * Math.pow(256, power)));
//        }
//        return num;
//    }

	private static String longToIp(long l) {
		return ((l >> 24) & 0xFF) + "." + ((l >> 16) & 0xFF) + "." + ((l >> 8) & 0xFF) + "." + (l & 0xFF);
	}

	private static String longToMac(long l) {
		return String.format("%02X:%02X:%02X:%02X:%02X:%02X", ((l >> 40) & 0xFF), ((l >> 32) & 0xFF),
				((l >> 24) & 0xFF), ((l >> 16) & 0xFF), ((l >> 8) & 0xFF), (l & 0xFF));
	}

//	private static long macToLong(String addr) 
//	{
//        String[] addrArray = addr.split(":");
//
//        long num = 0;
//        for (int i = 0; i < addrArray.length; i++) 
//        {
//            int power = 5 - i;
//            num += ((Integer.parseInt(addrArray[i], 16) % 256 * Math.pow(256, power)));
//        }
//        return num;
//    }

//	public static void main(String[] args)
//	{
//		try
//		{
//			System.out.println(new OBLicenseImpl().getLicenseInfo());
//		}
//		catch(OBFileNotFoundException e)
//		{
//			e.printStackTrace();
//		}
//		catch(OBLicenseInvalidFormatException e)
//		{
//			e.printStackTrace();
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}
//	
	public OBDtoLicense getLicenseInfo() throws OBFileNotFoundException, OBLicenseInvalidFormatException, OBException {
		String fileName = OBDefine.LICENSEFILE;
		return getLicenseInfo(fileName);
	}

	public OBDtoLicense getLicenseInfo(String fileName) throws OBException {
		OBDtoLicense result = new OBDtoLicense();
		try (FileInputStream inFile = new FileInputStream(fileName);) {
			ByteBuffer buf = ByteBuffer.allocate(1024);
			FileChannel inChannel = inFile.getChannel();
			int iIndex = 0;
			inChannel.read(buf);
			int version = buf.getInt(iIndex);
			iIndex += 4;

			if (version == OBDtoLicense.VERSION_1) {
				// version 정보.
				result.setVersion(version);

				// 날짜 정보.
				long issueTime = buf.getLong(iIndex);
				iIndex += 8;

				long beginTime = buf.getLong(iIndex);
				iIndex += 8;

				long endTime = buf.getLong(iIndex);
				iIndex += 8;

				result.setIssueDate(OBDateTime.toDate(new Timestamp(issueTime)));
				result.setBeginDate(OBDateTime.toDate(new Timestamp(beginTime)));
				result.setEndDate(OBDateTime.toDate(new Timestamp(endTime)));

				// IP 주소 정보.
				long ipAddress = buf.getLong(iIndex);
				iIndex += 8;
				result.setIpAddress(longToIp(ipAddress));

				// MAC 주소 정보.
				long macAddress = buf.getLong(iIndex);
				iIndex += 8;
				result.setMacAddress(longToMac(macAddress));

				// product code
				int productCode = buf.getInt(iIndex);
				iIndex += 4;
				result.setProductCode(productCode);

				// model code
				int modelCode = buf.getInt(iIndex);
				iIndex += 4;
				result.setModelCode(modelCode);

				// maxVS
				int maxVS = buf.getInt(iIndex);
				iIndex += 4;
				result.setSubCondition1(maxVS);

				// maxADC
				int maxADC = buf.getInt(iIndex);
				iIndex += 4;
				result.setSubCondition2(maxADC);

				// maxAccount;
				int maxAccount = buf.getInt(iIndex);
				iIndex += 4;
				result.setSubCondition3(maxAccount);

				// maxStorage;
				int maxStorage = buf.getInt(iIndex);
				iIndex += 4;
				result.setSubCondition4(maxStorage);

				// supportModule;
				int suportModule = buf.getInt(iIndex);
				iIndex += 4;
				result.setSubCondition5(suportModule);

				int reserved6 = buf.getInt(iIndex);
				iIndex += 4;
				result.setSubCondition6(reserved6);

				int reserved7 = buf.getInt(iIndex);
				iIndex += 4;
				result.setSubCondition7(reserved7);

				int reserved8 = buf.getInt(iIndex);
				iIndex += 4;
				result.setSubCondition8(reserved8);

				int reserved9 = buf.getInt(iIndex);
				iIndex += 4;
				result.setSubCondition9(reserved9);

				int reserved10 = buf.getInt(iIndex);
				iIndex += 4;
				result.setSubCondition10(reserved10);

				// user name 256 바이트
				byte[] byteName = new byte[256];
				int len = 0;
				for (int i = 0; i < 256; i++) {
					byteName[i] = buf.get(iIndex + i);
					if (byteName[i] != 0)
						len++;
				}
				String name = new String(byteName, 0, len, "US-ASCII");
				iIndex += 256;
				result.setUserName(name);
//				System.out.println(name);
				// serialNum
				byte[] byteSerial = new byte[256];
				len = 0;
				for (int i = 0; i < 256; i++) {
					byteSerial[i] = buf.get(iIndex + i);
					if (byteSerial[i] != 0)
						len++;
				}
				String serialNum = new String(byteSerial, 0, len, "US-ASCII");
				iIndex += 256;
//				String temp = result.toStringV1();
//				System.out.println(result.toStringV1());
				String calcSerialNum = calculateSerialKey(result.toStringV1());

//				System.out.println(result.toStringV1());
//				System.out.println(calcSerialNum);
//				System.out.println(serialNum);
				if (calcSerialNum.compareToIgnoreCase(serialNum) != 0)
					throw new OBLicenseInvalidFormatException(String.format("invalid serial num:%s", serialNum));

				result.setSerialNum(serialNum);

				result.setState(OBDtoLicense.LIC_SUCCESS);
				if (false == checkDate(result)) {
					result.setState(OBDtoLicense.LIC_TIME_ERROR);
				} else if (false == checkMac(result)) {
					result.setState(OBDtoLicense.LIC_MAC_ERROR);
				}
			} else if (OBDtoLicense.VERSION_2 == version) {// JNA를 통해 데이터 추출.
				STLicenseInfo stInfo = new STLicenseInfo();
				Integer ret = JNALicenseLibrary.INSTANCE.getLicenseInfo(fileName, stInfo);
				switch (ret) {
				case STLicenseInfo.LIC_SUCCESS://
					break;
				case STLicenseInfo.LIC_FILE_ERROR://
					throw new OBException(OBException.ERRCODE_SYSTEM_FILE_NOT_FOUND);// throw new
																						// FileNotFoundException(JNALicenseLibrary.INSTANCE.getErrString(ret));
				case STLicenseInfo.LIC_FORMAT_ERROR://
				case STLicenseInfo.LIC_INTEGRITY_ERROR://
				case STLicenseInfo.LIC_TYPE_ERROR://
				case STLicenseInfo.LIC_VERSION_ERROR://
				case STLicenseInfo.LIC_IP_ERROR://
				case STLicenseInfo.LIC_MAC_ERROR://
				case STLicenseInfo.LIC_TIME_ERROR://
				case STLicenseInfo.LIC_ARGUMENT_ERROR://
				case STLicenseInfo.LIC_UNKOWN://
				default:
					throw new OBException(OBException.ERRCODE_LICENSE_FORMAT);// OBLicenseInvalidFormatException(JNALicenseLibrary.INSTANCE.getErrString(ret));
				}
				// version 정보.
				result.setVersion(stInfo.iVersion);
				// 날짜 정보.
				result.setIssueDate(OBDateTime.toDate(new Timestamp(stInfo.lIssueTime.longValue())));
				result.setBeginDate(OBDateTime.toDate(new Timestamp(stInfo.lBeginTime.longValue())));
				result.setEndDate(OBDateTime.toDate(new Timestamp(stInfo.lEndTime.longValue())));
				result.setIpAddress(stInfo.getIPAddress());
				result.setMacAddress(stInfo.getMACAddress());
				result.setProductCode(stInfo.iProductCode);
				result.setModelCode(stInfo.iModelCode);
				result.setSubCondition1(stInfo.iMaxVS);
				result.setSubCondition2(stInfo.iMaxADC);
				result.setSubCondition3(stInfo.iMaxAccount);
				result.setSubCondition4(0);
				result.setSubCondition5(stInfo.iSupportModule);
				result.setSubCondition6(0);
				result.setSubCondition7(0);
				result.setSubCondition8(0);
				result.setSubCondition9(0);
				result.setSubCondition10(0);
				result.setUserName(stInfo.getUserName());
				result.setSerialNum(stInfo.getSerial());

				result.setState(OBDtoLicense.LIC_SUCCESS);
				if (false == checkDate(result)) {
					result.setState(OBDtoLicense.LIC_TIME_ERROR);
				} else if (false == checkMac(result)) {
					result.setState(OBDtoLicense.LIC_MAC_ERROR);
				}
			} else {
				throw new OBException(OBException.ERRCODE_LICENSE_FORMAT);// throw new
																			// OBLicenseInvalidFormatException(String.format("invalid
																			// version:%d", version));
			}
			inChannel.close();
			inFile.close();
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}

		return result;
	}

//	public static void main(String[] args)
//	{

//		try
//		{
//			System.out.println(new OBLicenseImpl().calculateSecurityHash("aaaa","SHA-256"));
//			System.out.println(new OBLicenseImpl().calculateSecurityHash("aaaa","SHA-512"));
//			System.out.println(new OBLicenseImpl().calculateSecurityHash("aaaa","MD2"));
//			System.out.println(new OBLicenseImpl().calculateSecurityHash("aaaa","MD5"));
//			
//			OBLicenseImpl licManager = new OBLicenseImpl();
//			OBDtoLicense lic = new OBDtoLicense();			
//			SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
//
//			//SDS 라이센스
//			Date beginDate = ft.parse("2012-12-11");
//			Date endDate = ft.parse("2013-03-10");
//			endDate.setTime(0x7fffffffffffffffL); //end date = unlimited
//			String customerName = "SAMSUNG";
//			lic = licManager.setLicenseData5440(beginDate, endDate, customerName);
//			//SDS 라이센스 끝
//			
//			//1U 2220 1 months 데모 라이센스
////			Date beginDate = ft.parse("2013-01-16");
////			Date endDate = ft.parse("2013-02-16");
////			String customerName = "DONGA.COM DEMO";
////			lic = licManager.setLicenseData2220(beginDate, endDate, customerName);
//			//1U 2220 데모 라이센스 끝
//			
//			System.out.println(beginDate.getTime());
//			System.out.println(endDate.getTime());
//
//			String fileName=licManager.makeFileName(lic.getModelCode());
//			String currentDir = System.getProperty("user.dir");
//
//			licManager.makeLicense(fileName, lic);
//
//			String fileFullName = currentDir+"/"+fileName;			
//			OBDtoLicense licInfo = new OBLicenseImpl().getLicenseInfo(fileFullName);
//			System.out.println(fileFullName);
//			System.out.println(licInfo.getSerialNum());
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	private String makeFileName(int model)
//	{
//		Date time = new Date();
//		if(model==OBDtoLicense.MODEL_CODE_ADCSMART_5440)
//			return "adcsmart_5440_"+time.getTime()+".lic";
//		else if(model==OBDtoLicense.MODEL_CODE_ADCSMART_5420)
//			return "adcsmart_5420_"+time.getTime()+".lic";
//		else if(model==OBDtoLicense.MODEL_CODE_ADCSMART_5240)
//			return "adcsmart_5240_"+time.getTime()+".lic";
//		else if(model==OBDtoLicense.MODEL_CODE_ADCSMART_5220)
//			return "adcsmart_5220_"+time.getTime()+".lic";
//		else if(model==OBDtoLicense.MODEL_CODE_ADCSMART_3220)
//			return "adcsmart_3220_"+time.getTime()+".lic";
//		else 
//			return "adcsmart_2220_"+time.getTime()+".lic";
//	}
//	
//	private OBDtoLicense setLicenseData5440(Date beginTime, Date endTime, String customerName) throws OBException
//	{
//		try
//		{
//			OBDtoLicense lic = new OBDtoLicense();
//			Integer modelCode = OBDtoLicense.MODEL_CODE_ADCSMART_5440;
//			
//			lic.setIssueDate(OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now())));
//			lic.setBeginDate(beginTime);
//			lic.setEndDate(endTime);
//			lic.setIpAddress("0.0.0.0");
//			lic.setMacAddress("00:00:00:00:00:00");
//			lic.setModelCode(modelCode);
//			lic.setProductCode(OBDtoLicense.PRODUCT_CODE_ADCSMART);
//			lic.setSerialNum("");
//			lic.setSubCondition1(5000); //maxVS
//			lic.setSubCondition2(40);  //maxADC
//			lic.setSubCondition3(100); //maxAccount
//			lic.setSubCondition4(0);   //하드디스크용량, 사용안함.
//			lic.setSubCondition5(OBDtoLicense.MODULE_ADCSMART_SLB);
//			lic.setSubCondition6(0);
//			lic.setSubCondition7(0);
//			lic.setSubCondition8(0);
//			lic.setSubCondition9(0);
//			lic.setSubCondition10(0);
//			lic.setUserName(customerName);  // customer name
//			lic.setVersion(1);
//			
//			return lic;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}		
//	}
//	
//	private OBDtoLicense setLicenseData2220(Date beginTime, Date endTime, String customerName) throws OBException
//	{
//		try
//		{
//			OBDtoLicense lic = new OBDtoLicense();
//			Integer modelCode = OBDtoLicense.MODEL_CODE_ADCSMART_2220;
//			
//			lic.setIssueDate(OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now())));
//			lic.setBeginDate(beginTime);
//			lic.setEndDate(endTime);
//			lic.setIpAddress("0.0.0.0");
//			lic.setMacAddress("00:00:00:00:00:00");
//			lic.setModelCode(modelCode);
//			lic.setProductCode(OBDtoLicense.PRODUCT_CODE_ADCSMART);
//			lic.setSerialNum("");
//			lic.setSubCondition1(100); //maxVS
//			lic.setSubCondition2(5);  //maxADC
//			lic.setSubCondition3(10);  //maxAccount
//			lic.setSubCondition4(0); //하드디스크 용량, 사용안함
//			lic.setSubCondition5(OBDtoLicense.MODULE_ADCSMART_SLB);
//			lic.setSubCondition6(0);
//			lic.setSubCondition7(0);
//			lic.setSubCondition8(0);
//			lic.setSubCondition9(0);
//			lic.setSubCondition10(0);
//			lic.setUserName(customerName);  // customer name
//			lic.setVersion(1);
//			
//			return lic;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}		
//	}
//	private OBDtoLicense setLicenseData2220Extended(Date beginTime, Date endTime, String customerName) throws OBException
//	{ //2220 기본사양에서 max ADC만 10개로 확장
//		try
//		{
//			OBDtoLicense lic = new OBDtoLicense();
//			Integer modelCode = OBDtoLicense.MODEL_CODE_ADCSMART_2220;
//			
//			lic.setIssueDate(OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now())));
//			lic.setBeginDate(beginTime);
//			lic.setEndDate(endTime);
//			lic.setIpAddress("0.0.0.0");
//			lic.setMacAddress("00:00:00:00:00:00");
//			lic.setModelCode(modelCode);
//			lic.setProductCode(OBDtoLicense.PRODUCT_CODE_ADCSMART);
//			lic.setSerialNum("");
//			lic.setSubCondition1(100); //maxVS
//			lic.setSubCondition2(10);  //maxADC
//			lic.setSubCondition3(10);  //maxAccount
//			lic.setSubCondition4(0); //하드디스크 용량, 사용안함
//			lic.setSubCondition5(OBDtoLicense.MODULE_ADCSMART_SLB);
//			lic.setSubCondition6(0);
//			lic.setSubCondition7(0);
//			lic.setSubCondition8(0);
//			lic.setSubCondition9(0);
//			lic.setSubCondition10(0);
//			lic.setUserName(customerName);  // customer name
//			lic.setVersion(1);
//			
//			return lic;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}		
//	}
//	private String makeLicense(String fileName, OBDtoLicense licenseInfo) throws OBException
//	{
//		try
//		{
//			FileOutputStream out = new FileOutputStream(fileName);
//			out.write(intToByteArray(1));//version
//			out.write(longToByteArray(licenseInfo.getIssueDate().getTime()));//issueDate
//			out.write(longToByteArray(licenseInfo.getBeginDate().getTime()));//beginDate
//			out.write(longToByteArray(licenseInfo.getEndDate().getTime()));//endDate
//			out.write(longToByteArray(ipToLong(licenseInfo.getIpAddress())));//ipAddress
//			out.write(longToByteArray(macToLong(licenseInfo.getMacAddress())));//macaddress
//			out.write(intToByteArray(licenseInfo.getProductCode()));//productCode
//			out.write(intToByteArray(licenseInfo.getModelCode()));//modelCode
//			out.write(intToByteArray(licenseInfo.getSubCondition1()));//subCondition1. max virtual server
//			out.write(intToByteArray(licenseInfo.getSubCondition2()));//subCondition2. max ADC
//			out.write(intToByteArray(licenseInfo.getSubCondition3()));//subCondition3. max Account
//			out.write(intToByteArray(licenseInfo.getSubCondition4()));//subCondition4. max DB Storage
//			out.write(intToByteArray(licenseInfo.getSubCondition5()));//subCondition5. support module
//			out.write(intToByteArray(licenseInfo.getSubCondition6()));//subCondition6. reserved
//			out.write(intToByteArray(licenseInfo.getSubCondition7()));//subCondition7. reserved
//			out.write(intToByteArray(licenseInfo.getSubCondition8()));//subCondition8. reserved
//			out.write(intToByteArray(licenseInfo.getSubCondition9()));//subCondition9. reserved
//			out.write(intToByteArray(licenseInfo.getSubCondition10()));//subCondition10. reserved
//			if(licenseInfo.getUserName().getBytes().length>256)
//				out.write(licenseInfo.getUserName().getBytes(), 0, 256);//userName
//			else
//			{
//				int remain = 256-licenseInfo.getUserName().getBytes().length;
//				out.write(licenseInfo.getUserName().getBytes(), 0, licenseInfo.getUserName().getBytes().length);//userName
//				byte [] buffer = new byte[remain];
//				out.write(buffer, 0, remain);//userName
//			}
//			
////			System.out.println(licenseInfo.toStringV1());
//			String serial = calculateSerialKey(licenseInfo.toStringV1());
////			System.out.println(licenseInfo.toStringV1());
////			System.out.println(serial);
//			 
//			out.write(serial.getBytes());//serialNum
//			
//			out.close();
//			return serial;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}
//	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			String fullNameString = "1abacdasdfasdfasdfas";
//			String serial= new OBLicenseImpl().calculateSerialKey(fullNameString);
//			System.out.println(serial);
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}
//	
	public String calculateSerialKey(String stringInput) throws OBException {
		String serialNumberEncoded = new OBLicenseImpl().calculateSecurityHash(stringInput, "MD2")
				+ new OBLicenseImpl().calculateSecurityHash(stringInput, "MD5")
				+ new OBLicenseImpl().calculateSecurityHash(stringInput, "SHA-512");

		String serialNumber = "" + serialNumberEncoded.charAt(32) + serialNumberEncoded.charAt(176)
				+ serialNumberEncoded.charAt(40) + serialNumberEncoded.charAt(50) + "-" + serialNumberEncoded.charAt(2)
				+ serialNumberEncoded.charAt(91) + serialNumberEncoded.charAt(173) + serialNumberEncoded.charAt(172)
				+ serialNumberEncoded.charAt(98) + "-" + serialNumberEncoded.charAt(47) + serialNumberEncoded.charAt(65)
				+ serialNumberEncoded.charAt(18) + serialNumberEncoded.charAt(185) + "-"
				+ serialNumberEncoded.charAt(57) + serialNumberEncoded.charAt(153) + serialNumberEncoded.charAt(102)
				+ serialNumberEncoded.charAt(15) + serialNumberEncoded.charAt(99);

		return serialNumber;
	}

	private String calculateSecurityHash(String stringInput, String algorithmName) throws OBException {
		return calculateSecurityHash(stringInput.getBytes(), algorithmName);
	}

	private String calculateSecurityHash(byte[] buffer, String algorithmName) throws OBException {
		String hexMessageEncode = "";
		MessageDigest messageDigest;
		try {
			messageDigest = MessageDigest.getInstance(algorithmName);

			messageDigest.update(buffer);
			byte[] messageDigestBytes = messageDigest.digest();
			for (int index = 0; index < messageDigestBytes.length; index++) {
				int countEncode = messageDigestBytes[index] & 0xff;
				if (Integer.toHexString(countEncode).length() == 1)
					hexMessageEncode = hexMessageEncode + "0";
				hexMessageEncode = hexMessageEncode + Integer.toHexString(countEncode);
			}
		} catch (Exception e) {
			throw new OBException(e.getMessage());// MD2, MD5, SHA-1, SHA-256, SHA-384, SHA-512
		}
		return hexMessageEncode;
	}

	private OBDtoLicense getDefaultLicenseInfo() throws OBException {
		OBDtoLicense lic = new OBDtoLicense();
		lic.setBeginDate(new Date());
		Timestamp endTime = new Timestamp(OBDateTime.toTimestamp(OBDateTime.now()).getTime() + 1000000);
		lic.setEndDate(endTime);
		lic.setIssueDate(new Date());
		lic.setIpAddress("0.0.0.0");
		lic.setMacAddress("00:00:00:00:00:00");
		lic.setModelCode(OBDtoLicense.MODEL_CODE_ADCSMART_DEFAULT);
		lic.setProductCode(OBDtoLicense.PRODUCT_CODE_DEFAULT);
		lic.setVersion(OBDtoLicense.VERSION_1);
		lic.setSubCondition1(5);
		lic.setSubCondition2(5);
		lic.setSubCondition3(5);
		lic.setSubCondition4(5);
		lic.setSubCondition5(5);
		lic.setUserName("openbase default user");

		return lic;
	}

//	public boolean isValidDateAndAdcCnt(int currAdcCnt, OBDatabase db) throws OBException
//	{
//		OBDtoLicense licInfo = null;
//		
//		try
//		{
//			licInfo = getLicenseInfo();
//		}
//		catch(OBFileNotFoundException e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("license file not found."));
//			new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_SYSTEM_LICENSE_FILENOTFOUNDL, db);
//			licInfo = getDefaultLicenseInfo();
//		}
//		catch(OBLicenseInvalidFormatException e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("invalid license file format."));
//			new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_SYSTEM_LICENSE_FILENOTFOUNDL, db);
//			licInfo = getDefaultLicenseInfo();
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}
//		// 시간 검사.
//		if(isValidDate(licInfo)==false)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("invalid license. invalid date"));
//			return false;
//		}
//		
//		// adcCnt 검사.
//		if(licInfo.getSubCondition2()<=currAdcCnt)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("invalid license. lic adc cnt:%d, current cnt:%d", licInfo.getSubCondition2(), currAdcCnt));
//			return false;
//		}
//		
//		return true;
//	}

//	public boolean isValidDateAndAccntCnt(int accntCnt, OBDatabase db) throws OBException
//	{
//		OBDtoLicense licInfo = null;
//		
//		try
//		{
//			licInfo = getLicenseInfo();
//		}
//		catch(OBFileNotFoundException e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("license file not found."));
//			new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_SYSTEM_LICENSE_FILENOTFOUNDL, db);
//			licInfo = getDefaultLicenseInfo();
//		}
//		catch(OBLicenseInvalidFormatException e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("invalid license file format."));
//			new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_SYSTEM_LICENSE_FILENOTFOUNDL, db);
//			licInfo = getDefaultLicenseInfo();
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}
//		
//		// 시간 검사.
//		if(isValidDate(licInfo)==false)
//			return false;
//		
//		// adcCnt 검사.
//		if(licInfo.getSubCondition3()<=accntCnt)
//			return false;
//		
//		return true;
//	}

	private boolean checkAdcCnt(OBDtoLicense licInfo, int adcCnt) throws OBException {
		// adcCnt 검사.
		if (licInfo.getSubCondition2() <= adcCnt)
			return false;

		return true;
	}

//	public boolean isValidDateAndVSCnt(int vsCnt, OBDatabase db) throws OBException
//	{
//		OBDtoLicense licInfo = null;
//		
//		try
//		{
//			licInfo = getLicenseInfo();
//		}
//		catch(OBFileNotFoundException e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("license file not found."));
//			new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_SYSTEM_LICENSE_FILENOTFOUNDL, db);
//			licInfo = getDefaultLicenseInfo();
//		}
//		catch(OBLicenseInvalidFormatException e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("invalid license file format."));
//			new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_SYSTEM_LICENSE_FILENOTFOUNDL, db);
//			licInfo = getDefaultLicenseInfo();
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}
//		
//		// 시간 검사.
//		if(isValidDate(licInfo)==false)
//			return false;
//		
//		// adcCnt 검사.
//		if(licInfo.getSubCondition1()<=vsCnt)
//			return false;
//		
//		return true;
//	}

//	private boolean isValidDate(OBDtoLicense licInfo) throws OBException
//	{
//		Date dateTime= new Date();
//		
//		// 시간 검사.
//		if(licInfo.getBeginDate().compareTo(dateTime)>0)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("invalid license. invalid date. lic begin:%d, current:%d", licInfo.getBeginDate().getTime(), dateTime.getTime()));
//			return false;
//		}
//		if(licInfo.getEndDate().compareTo(dateTime)<0)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("invalid license. invalid date. lic end:%d, current:%d", licInfo.getEndDate().getTime(), dateTime.getTime()));
//			return false;
//		}
//		return true;
//	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			if(new OBLicenseImpl().isValidDate(db)==false)
//				System.out.println("invalid license");
//			else
//				System.out.println("valid license");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	public boolean isValidLicense(Integer adcCnt) throws OBException {
		OBDatabase db = new OBDatabase();

		OBDtoLicense licInfo = null;
		try {
			db.openDB();

			licInfo = getLicenseInfo();
		} catch (OBFileNotFoundException e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("license file not found."));
			new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_SYSTEM_LICENSE_FILENOTFOUNDL);
			licInfo = getDefaultLicenseInfo();
		} catch (OBLicenseInvalidFormatException e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("invalid license file format."));
			new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_SYSTEM_LICENSE_FILENOTFOUNDL);
			licInfo = getDefaultLicenseInfo();
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_LICENSE_INVALID, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		try {
			if (checkDate(licInfo) == false)
				return false;
			if (checkMac(licInfo) == false)
				return false;
			if (adcCnt != null && adcCnt.equals(0) == false && checkAdcCnt(licInfo, adcCnt) == false)
				return false;

			return true;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_LICENSE_INVALID, e.getMessage());
		}
	}

	// isValidLicenseExt , 각 valid 조건 마다 message를 다르게 띄워 주기위한 확장 기능
	public OBDtoValidLicense isValidLicenseExt(Integer adcCnt) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoValidLicense retVal = new OBDtoValidLicense();
		OBDtoLicense licInfo = null;
		String model = null;
		try {
			db.openDB();
			licInfo = getLicenseInfo();
		} catch (OBFileNotFoundException e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("license file not found."));
			new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_SYSTEM_LICENSE_FILENOTFOUNDL);
			licInfo = getDefaultLicenseInfo();
		} catch (OBLicenseInvalidFormatException e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("invalid license file format."));
			new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_SYSTEM_LICENSE_FILENOTFOUNDL);
			licInfo = getDefaultLicenseInfo();
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_LICENSE_INVALID, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		try {
			if (licInfo.getModelCode().equals(1120)) // 조달 모델은 AS로 받았기 때문에 AS로 표기
			{
				model = "AS" + licInfo.getModelCode();
			} else // 일반 장비 AX
			{
				model = "AX" + licInfo.getModelCode();
			}
			retVal.setDeviceModel(model);
			retVal.setMaxAdc(licInfo.getSubCondition2());

			if (checkDate(licInfo) == false) {
				retVal.setMsgKey(OBDefine.LICENSE_INVALID_DATE);
			}
			if (checkMac(licInfo) == false) {
				retVal.setMsgKey(OBDefine.LICENSE_INVALID_MAC);
			}
			if (adcCnt != null && adcCnt.equals(0) == false && checkAdcCnt(licInfo, adcCnt) == false) {
				retVal.setMsgKey(OBDefine.LICENSE_INVALID_ADC_CNT);
			}

			return retVal;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_LICENSE_INVALID, e.getMessage());
		}
	}

//	public boolean isValidDate() throws OBException
//	{
//		OBDatabase db = new OBDatabase();
//		try
//		{
//			db.openDB();
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}
//		
//
//		
//		
//		try
//		{
//			boolean ret = isValidDate(db);
//			db.closeDB();
//			return ret;
//		}
//		catch(Exception e)
//		{
//			db.closeDB();
//			throw new OBException(e.getMessage());
//		}
//	}
//	
//	public boolean isValidDate(OBDatabase db) throws OBException
//	{
//		OBDtoLicense licInfo = null;
//		
//		try
//		{
//			licInfo = getLicenseInfo();
//		}
//		catch(OBFileNotFoundException e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("license file not found."));
//			new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_SYSTEM_LICENSE_FILENOTFOUNDL, db);
//			licInfo = getDefaultLicenseInfo();
//		}
//		catch(OBLicenseInvalidFormatException e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("invalid license file format."));
//			new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_SYSTEM_LICENSE_FILENOTFOUNDL, db);
//			licInfo = getDefaultLicenseInfo();
//		}
//		catch(Exception e)
//		{
//			throw new OBException(e.getMessage());
//		}
//		
//		// 시간 검사.
//		return checkDate(licInfo);
//	}

	private boolean checkDate(OBDtoLicense licInfo) throws OBException {
		Date dateTime = new Date();
		if (licInfo.getBeginDate().compareTo(dateTime) > 0) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
					String.format("lnvalid license. begin date:%s", licInfo.getEndDate()));
			return false;
		}
		if (licInfo.getEndDate().compareTo(dateTime) < 0) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
					String.format("lnvalid license. end date:%s", licInfo.getEndDate()));
			return false;
		}
		return true;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			new OBLicenseImpl().updateMacAddress("08:00:27:1F:B4:66");
//			new OBLicenseImpl().isValidMac();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	public boolean isValidMac() throws OBException {
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			boolean ret = isValidMac(db);
			return ret;
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public boolean isValidMac(OBDatabase db) throws OBException {
		OBDtoLicense licInfo = null;

		try {
			licInfo = getLicenseInfo();
		} catch (OBFileNotFoundException e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("license file not found."));
			new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_SYSTEM_LICENSE_FILENOTFOUNDL);
			licInfo = getDefaultLicenseInfo();
		} catch (OBLicenseInvalidFormatException e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("invalid license file format."));
			new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_SYSTEM_LICENSE_FILENOTFOUNDL);
			licInfo = getDefaultLicenseInfo();
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}

		// MAC 주소 검사.
		return checkMac(licInfo);
	}

	private boolean checkMac(OBDtoLicense licInfo) throws OBException {
		String unlimited = "00:00:00:00:00:00";
		int retVal = licInfo.getMacAddress().compareTo(unlimited);
		if (retVal != 0) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("retVal:%d,  mac:%s. len:%d, unlimited:%s)", retVal,
					licInfo.getMacAddress(), licInfo.getMacAddress().length(), unlimited.toString()));

			ArrayList<String> macAddress = new OBNetwork().getMacAddress();
			boolean isDetected = false;
			for (String mac : macAddress) {
				if (mac.compareToIgnoreCase(licInfo.getMacAddress()) == 0) {
					isDetected = true;
					break;
				}
			}
			if (isDetected == false) {
				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
						String.format("lnvalid license. not found mac address(list:%s, mac:%s)", macAddress,
								licInfo.getMacAddress()));
				return false;
			}
		}

		return true;
	}
//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
//			extraInfo.setAccountIndex(1);
//			extraInfo.setClientIPAddress("172.172.2.2");
//			new OBLicenseImpl().offlineUpdateLicense("/home/bwpark/aaa.kkk", extraInfo);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public boolean offlineUpdateLicense(String fileName, OBDtoExtraInfo extraInfo) throws OBException {
		try {
			// 포멧 검사.
			getLicenseInfo(fileName);
			OBParser.moveFile(fileName, OBDefine.LICENSEFILE);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// 감사로그 생성함.
		new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
				OBSystemAudit.AUDIT_SYSTEM_LICENSE_UPDATE_SUCCESS);
		return true;
	}

	private OBDtoVersionInfo getVersionInfo() throws OBException {
		OBDatabase db = new OBDatabase();

		OBDtoVersionInfo result = new OBDtoVersionInfo();
		String sqlText = "";
		try {
			db.openDB();

			sqlText = String.format(
					" SELECT PRODUCT_NAME, PRODUCT_MODEL, VERSION1, VERSION2, VERSION3, VERSION4, REVISION, UPDATE_TIME, UPDATE_NOTE "
							+ " FROM SYSTEM_VERSION " + " WHERE INDEX = (SELECT MAX(INDEX) FROM SYSTEM_VERSION) ;");

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "data not found");
			}
			String version = db.getString(rs, "VERSION1") + "." + db.getString(rs, "VERSION2") + "."
					+ db.getString(rs, "VERSION3");
			String version4 = db.getString(rs, "VERSION4");
			if (version4 != null && version4.isEmpty() == false) // 패치가 있으면 붙인다.
			{
				version += ("." + version4);
			}

			result.setProductName(db.getString(rs, "PRODUCT_NAME"));
			result.setProductModel(db.getString(rs, "PRODUCT_MODEL"));
			result.setVersion(version);
			result.setUpdateTime(db.getTimestamp(rs, "UPDATE_TIME"));
			result.setUpdateNote(db.getString(rs, "UPDATE_NOTE"));
			result.setRevision(db.getInteger(rs, "REVISION"));
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return result;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			System.out.println(new OBLicenseImpl().getLicenseGeneralInfo());
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	public String getVersion() throws OBException {
		return getVersionInfo().getVersion();
	}

	@Override
	public OBDtoLicenseInfo getLicenseGeneralInfo() throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start."));

		try {
			OBDtoVersionInfo version = getVersionInfo();
			OBDtoLicense licInfo = getLicenseInfo();
			OBDtoLicenseInfo result = new OBDtoLicenseInfo();

			String versionFull = version.getProductName() + " " + version.getVersion(); // 제품은 OS버전으로 가기로 했으므로 model 이름은
																						// 뺀다.

			result.setIssueDate(OBDateTime.toString(new Timestamp(licInfo.getIssueDate().getTime())));
			result.setMacAddress(licInfo.getMacAddress());
			result.setMaxAdcNum(licInfo.getSubCondition2());
			result.setMaxUserNum(licInfo.getSubCondition3());
			result.setMaxVSnum(licInfo.getSubCondition1());
			result.setLicStatus(licInfo.getState());
			String model = "";
			if (licInfo.getModelCode().equals(1120)) // 조달 모델은 AS로 받았기 때문에 AS로 표기
			{
				model = "AS" + licInfo.getModelCode();
			} else // 일반 장비 AX
			{
				model = "AX" + licInfo.getModelCode();
			}

			result.setModel(model);
			if (licInfo.getEndDate().getTime() == 0x7fffffffffffffffL)
				result.setPeriod("unlimited");
			else {
				String period = OBDateTime.toString(new Timestamp(licInfo.getBeginDate().getTime())) + " - "
						+ OBDateTime.toString(new Timestamp(licInfo.getEndDate().getTime()));
				result.setPeriod(period);
			}

			result.setSerial(licInfo.getSerialNum());
			result.setVersion(versionFull);
			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
			return result;
		}
//		catch(OBFileNotFoundException e)
//		{
//			OBException e1 = new OBException(OBException.ERRCODE_LICENSE_NO_FILE, e.getMessage());
//			throw new OBException(OBException.ERRCODE_LICENSE_GETINFO, e1);
//		}
//		catch(OBLicenseInvalidFormatException e)
//		{
//			OBException e1 = new OBException(OBException.ERRCODE_LICENSE_FORMAT, e.getMessage());
//			throw new OBException(OBException.ERRCODE_LICENSE_GETINFO, e1);
//		}
		catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
	}

	@Override
	public boolean isValidLicenseFormat(String fullPathName) throws OBException {
		try {
			getLicenseInfo(fullPathName);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

//	private String convertByteArrayToString(byte [] byteArray) 
//	{
//		int lastGoodChar=0;
//		
//        for(int i=0;i<byteArray.length;i++)
//        {
//        	if(byteArray[i] == 0x00)
//        	{
//        		lastGoodChar = i;
//        		break;
//        	}
//        }
//        String value = new String(byteArray, 0, lastGoodChar);
//        return value;
//    }

//	private String getOSVersion()
//	{
//		ByteBuffer szOSVer = ByteBuffer.allocate(32);
//		ByteBuffer szWEBOver = ByteBuffer.allocate(32);
//		ByteBuffer szSRVVer = ByteBuffer.allocate(32);
//		try
//		{
//			OBSystem.INSTANCE.OBGetVersion(szOSVer, szWEBOver, szSRVVer);
//			return "v"+convertByteArrayToString(szOSVer.array());
//		}
//		catch(Exception e)
//		{
//			return new String("v1.2");
//		}
//	}
}
