package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoLicense
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
	
	public static final	int VERSION_1_LENGTH = 348;
	
	public static final int MODULE_ADCSMART_SLB = 1;
	
	public static final int MODEL_CODE_ADCSMART_DEFAULT = 1000;
	public static final int MODEL_CODE_ADCSMART_2220 = 2220;
	public static final int MODEL_CODE_ADCSMART_3220 = 3220;
	public static final int MODEL_CODE_ADCSMART_5220 = 5220;
	public static final int MODEL_CODE_ADCSMART_5240 = 5240;
	public static final int MODEL_CODE_ADCSMART_5420 = 5420;
	public static final int MODEL_CODE_ADCSMART_5440 = 5440;
	
	public static final int PRODUCT_CODE_DEFAULT = 0;//
	public static final int PRODUCT_CODE_ADCSMART = 1;//ADCSMart

	public static final int VERSION_1	= 1;
	public static final int VERSION_2	= 0x02000000;//33554432;// jna를 통해 데이터 추출하는 버전. 0x02000000
	
	private Integer version;   //라이센스 (체계) 버전, 제품 버전이 아님, 현재 1
	private Date 	issueDate;
	private Date 	beginDate;
	private Date 	endDate;
	private String 	ipAddress;
	private String 	macAddress;//"AA:BB:CC:11:22:33"
	private Integer productCode;
	private Integer modelCode;
	private Integer subCondition1;//maxVS;
	private Integer subCondition2;//maxADC;
	private Integer subCondition3;//maxAccount;
	private Integer subCondition4;//하드디스크 용량, GBytes 단위. 사용안함
	private Integer subCondition5;//supportModule;//0x01:SLB, 0x02:xxx, 0x04:xxx....
	private Integer subCondition6;//
	private Integer subCondition7;//
	private Integer subCondition8;//
	private Integer subCondition9;//
	private Integer subCondition10;//
	private String	userName;
	private String 	serialNum;
	
	private Integer state;//LIC_XXXXX
	
	@Override
	public String toString()
	{
		return "OBDtoLicense [version=" + version + ", issueDate=" + issueDate
				+ ", beginDate=" + beginDate + ", endDate=" + endDate
				+ ", ipAddress=" + ipAddress + ", macAddress=" + macAddress
				+ ", productCode=" + productCode + ", modelCode=" + modelCode
				+ ", subCondition1=" + subCondition1 + ", subCondition2="
				+ subCondition2 + ", subCondition3=" + subCondition3
				+ ", subCondition4=" + subCondition4 + ", subCondition5="
				+ subCondition5 + ", subCondition6=" + subCondition6
				+ ", subCondition7=" + subCondition7 + ", subCondition8="
				+ subCondition8 + ", subCondition9=" + subCondition9
				+ ", subCondition10=" + subCondition10 + ", userName="
				+ userName + ", serialNum=" + serialNum + ", state=" + state
				+ "]";
	}
	
	public String toStringV1()
	{
		return "OBDtoLicense [version=" + version + ", issueDate=" + issueDate.getTime()
				+ ", beginDate=" + beginDate.getTime() + ", endDate=" + endDate.getTime()
				+ ", ipAddress=" + ipAddress + ", macAddress=" + macAddress
				+ ", modelCode=" + modelCode + ", productCode=" + productCode
				+ ", subCondition1=" + subCondition1 + ", subCondition2="
				+ subCondition2 + ", subCondition3=" + subCondition3
				+ ", subCondition4=" + subCondition4 + ", subCondition5="
				+ subCondition5 + ", subCondition6=" + subCondition6
				+ ", subCondition7=" + subCondition7 + ", subCondition8="
				+ subCondition8 + ", subCondition9=" + subCondition9
				+ ", subCondition10=" + subCondition10 + ", userName="
				+ userName + "]";
	}
	public Integer getVersion()
	{
		return version;
	}
	public Integer getState()
	{
		return state;
	}

	public void setState(Integer state)
	{
		this.state = state;
	}

	public void setVersion(Integer version)
	{
		this.version = version;
	}
	public Date getIssueDate()
	{
		return issueDate;
	}
	public void setIssueDate(Date issueDate)
	{
		this.issueDate = issueDate;
	}
	public Date getBeginDate()
	{
		return beginDate;
	}
	public void setBeginDate(Date beginDate)
	{
		this.beginDate = beginDate;
	}
	public Date getEndDate()
	{
		return endDate;
	}
	public void setEndDate(Date endDate)
	{
		this.endDate = endDate;
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public String getMacAddress()
	{
		return macAddress;
	}
	public void setMacAddress(String macAddress)
	{
		this.macAddress = macAddress;
	}
	public Integer getModelCode()
	{
		return modelCode;
	}
	public void setModelCode(Integer modelCode)
	{
		this.modelCode = modelCode;
	}
	public Integer getProductCode()
	{
		return productCode;
	}
	public void setProductCode(Integer productCode)
	{
		this.productCode = productCode;
	}
	public Integer getSubCondition1()
	{
		return subCondition1;
	}
	public void setSubCondition1(Integer subCondition1)
	{
		this.subCondition1 = subCondition1;
	}
	public Integer getSubCondition2()
	{
		return subCondition2;
	}
	public void setSubCondition2(Integer subCondition2)
	{
		this.subCondition2 = subCondition2;
	}
	public Integer getSubCondition3()
	{
		return subCondition3;
	}
	public void setSubCondition3(Integer subCondition3)
	{
		this.subCondition3 = subCondition3;
	}
	public Integer getSubCondition4()
	{
		return subCondition4;
	}
	public void setSubCondition4(Integer subCondition4)
	{
		this.subCondition4 = subCondition4;
	}
	public Integer getSubCondition5()
	{
		return subCondition5;
	}
	public void setSubCondition5(Integer subCondition5)
	{
		this.subCondition5 = subCondition5;
	}
	public Integer getSubCondition6()
	{
		return subCondition6;
	}
	public void setSubCondition6(Integer subCondition6)
	{
		this.subCondition6 = subCondition6;
	}
	public Integer getSubCondition7()
	{
		return subCondition7;
	}
	public void setSubCondition7(Integer subCondition7)
	{
		this.subCondition7 = subCondition7;
	}
	public Integer getSubCondition8()
	{
		return subCondition8;
	}
	public void setSubCondition8(Integer subCondition8)
	{
		this.subCondition8 = subCondition8;
	}
	public Integer getSubCondition9()
	{
		return subCondition9;
	}
	public void setSubCondition9(Integer subCondition9)
	{
		this.subCondition9 = subCondition9;
	}
	public Integer getSubCondition10()
	{
		return subCondition10;
	}
	public void setSubCondition10(Integer subCondition10)
	{
		this.subCondition10 = subCondition10;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getSerialNum()
	{
		return serialNum;
	}
	public void setSerialNum(String serialNum)
	{
		this.serialNum = serialNum;
	}
}
