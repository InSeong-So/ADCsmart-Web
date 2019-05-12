package kr.openbase.adcsmart.service.jna.dto;

import com.sun.jna.NativeLong;
import com.sun.jna.Structure;

public class STLicenseInfo extends Structure
{
	public static final int  LIC_SUCCESS = 0;
	public static final int  LIC_FORMAT_ERROR = 1;
	public static final int  LIC_FILE_ERROR = 2;
	public static final int  LIC_INTEGRITY_ERROR = 3;
	public static final int  LIC_TYPE_ERROR = 4;// program type
	public static final int  LIC_VERSION_ERROR = 5;// version
	public static final int  LIC_IP_ERROR = 6;
	public static final int  LIC_MAC_ERROR = 7;
	public static final int  LIC_TIME_ERROR = 8;
	public static final int  LIC_ARGUMENT_ERROR = 9;
	public static final int  LIC_UNKOWN = 10;
	
	public int 			iVersion;
	public int 			iProductCode;
	public int 			iModelCode;
	public int 			iMaxVS;
	public int 			iMaxADC;
	public int 			iMaxAccount;
	public int 			iSupportModule;
	public int[] 		iReserved = new int[5];
	/// millisecond, int ftime(struct timeb *tp);
	public NativeLong 	lIssueTime;
	/// millisecond
	public NativeLong 	lBeginTime;
	/// millisecond
	public NativeLong 	lEndTime;
	/// C type : char[64]
	public byte[] szUserName = new byte[64];
	/// C type : char[64]
	public byte[] szIPAddress = new byte[32];
	/// C type : char[64]
	public byte[] szMACAddress = new byte[32];
	/// C type : char[64]
	public byte[] szSerial = new byte[64];
	/// C type : BYTE[HASH_LENGTH]
	public byte[] abHash = new byte[96];
	
	private String convertByteArrayToString(byte [] byteArray) 
	{
		int lastGoodChar=0;
		
        for(int i=0;i<byteArray.length;i++)
        {
        	if(byteArray[i] == 0x00)
        	{
        		lastGoodChar = i;
        		break;
        	}
        }
        String value = new String(byteArray, 0, lastGoodChar);
        return value;
    }

	
	public STLicenseInfo() 
	{
		super();
		initFieldOrder();
	}
	
	public String getIPAddress()
	{
		return convertByteArrayToString(szIPAddress);
	}
	public String getMACAddress()
	{
		return convertByteArrayToString(szMACAddress);
//		return new String(szMACAddress, 0, szMACAddress.length);
	}
	public void setMACAddress(byte[] szMACAddress)
	{
		this.szMACAddress = szMACAddress;
	}
	public String getUserName()
	{
		return new String(szUserName, 0, szUserName.length);
	}

	public String getSerial()
	{
		return new String(szSerial, 0, szSerial.length);
	}
	protected void initFieldOrder() 
	{
		setFieldOrder(new String[]{"iVersion","iProductCode", "iModelCode", "iMaxVS", "iMaxADC", "iMaxAccount", "iSupportModule", "iReserved",  "lIssueTime", "lBeginTime", "lEndTime", "szUserName", "szIPAddress", "szMACAddress",  "szSerial", "abHash"});
	}
	public static class ByReference extends STLicenseInfo implements Structure.ByReference 
	{
	};
	public static class ByValue extends STLicenseInfo implements Structure.ByValue 
	{
	};
}
