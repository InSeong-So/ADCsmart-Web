/*
 * ADC 
 */
package kr.openbase.adcsmart.service.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.utility.OBCipherAES;

public class OBDtoAdcInfo
{
	public static final int ADC_TYPE_F5 = 1;
	public static final int ADC_TYPE_ALTEON = 2;
	public static final int ADC_TYPE_PIOLINK_PAS = 3;
	public static final int ADC_TYPE_PIOLINK_PASK = 4;
	public static final int ADC_TYPE_PIOLINK_UNKNOWN = -2;
	public static final int ADC_TYPE_UNKNOWN = -1;
	
	public static final int ACTIVE_STANDBY_STATE_UNKNOWN = 0;
	public static final int ACTIVE_STANDBY_STATE_ACTIVE  = 1;
	public static final int ACTIVE_STANDBY_STATE_STANDBY = 2;

	public static final int ACTIVE_STANDBY_DIR_UNKNOWN = 0;
	public static final int ACTIVE_STANDBY_DIR_UP      = 1;
	public static final int ACTIVE_STANDBY_DIR_DOWN    = 2;

	private Integer 	index;
	private String 		name;
	private String 		adcIpAddress;
	private String 		adcAccount;
	private String 		adcPassword;
	private String 		adcCliAccount;
	private String 		adcCliPassword;
	private Integer 	adcType; 
	private String  	snmpRComm;
	private String 		model;
	private Integer 	groupIndex;
	private String 		description;
	private Integer 	activePairIndex;
	private String 		peerAdcIPAddress;
	private Integer 	connService;
	private Integer 	connPort;
	private Integer     connProtocol;               // 0:TCP, 1:ICMP

	//private Integer available;

	private String 		swVersion;
	private String 		hostName;
	private Timestamp 	applyTime;
	private Timestamp 	saveTime;
	private Timestamp 	lastBootTime;
	private Timestamp 	lastDisconnectedTime;// 가장 최근에 단절된 시간을 제공.
	private Integer 	status;
	private Integer 	activeStandbyState;
	private Integer     activeStandbyDirection;// active/standby 장비를 icon을 이용해서 묶어 표현한다. 장비를 묶기 위한 용도로 사용됨.
	
	private ArrayList<Integer> 	accountIndexList;
	private Date				purchaseDate;//장비 구매일
	private Timestamp 	registerTime;
	private String		syslogIpAddress;
	private Integer     roleFlbYn;
	
	private OBDtoAdcSnmpInfo snmpInfo;
	
	private Integer		spSessionMax;
	private Integer     opMode; // 운영모드
	private Integer     mgmtMode;
	private Integer     ssoMode = 0;
	
    @Override
    public String toString()
    {
        return "OBDtoAdcInfo [index=" + index + ", name=" + name + ", adcIpAddress=" + adcIpAddress + ", adcAccount=" + adcAccount + ", adcPassword=" + adcPassword + ", adcCliAccount=" + adcCliAccount + ", adcCliPassword=" + adcCliPassword + ", adcType=" + adcType + ", snmpRComm=" + snmpRComm + ", model=" + model + ", groupIndex=" + groupIndex + ", description=" + description + ", activePairIndex=" + activePairIndex + ", peerAdcIPAddress=" + peerAdcIPAddress + ", connService=" + connService + ", connPort=" + connPort + ", connProtocol=" + connProtocol + ", swVersion=" + swVersion + ", hostName=" + hostName + ", applyTime=" + applyTime + ", saveTime=" + saveTime + ", lastBootTime=" + lastBootTime + ", lastDisconnectedTime=" + lastDisconnectedTime + ", status=" + status + ", activeStandbyState=" + activeStandbyState + ", activeStandbyDirection=" + activeStandbyDirection + ", accountIndexList=" + accountIndexList + ", purchaseDate=" + purchaseDate + ", registerTime=" + registerTime + ", syslogIpAddress=" + syslogIpAddress + ", roleFlbYn=" + roleFlbYn + ", snmpInfo=" + snmpInfo + ", spSessionMax=" + spSessionMax + ", opMode=" + opMode + ", mgmtMode=" + mgmtMode + ", ssoMode=" + ssoMode + "]";
    }
    public Timestamp getRegisterTime()
	{
		return registerTime;
	}
	public void setRegisterTime(Timestamp registerTime)
	{
		this.registerTime = registerTime;
	}
	public Timestamp getLastDisconnectedTime()
	{
		return lastDisconnectedTime;
	}
	public void setLastDisconnectedTime(Timestamp lastDisconnectedTime)
	{
		this.lastDisconnectedTime = lastDisconnectedTime;
	}	public Integer getActiveStandbyDirection()
	{
		return activeStandbyDirection;
	}
	public void setActiveStandbyDirection(Integer activeStandbyDirection)
	{
		this.activeStandbyDirection = activeStandbyDirection;
	}
	public Integer getActiveStandbyState()
	{
		return activeStandbyState;
	}
	public void setActiveStandbyState(Integer activeStandbyState)
	{
		this.activeStandbyState = activeStandbyState;
	}
	public String getAdcCliAccount()
	{
		return adcCliAccount;
	}
	public void setAdcCliAccount(String adcCliAccount)
	{
		this.adcCliAccount = adcCliAccount;
	}
	public String getAdcCliPassword()
	{
		return adcCliPassword;
	}
	public String getAdcCliPasswordDecrypt()
	{
		String result = new OBCipherAES().Decrypt(this.adcCliPassword);
		return result;
	}
	
	public void setAdcCliPassword(String adcCliPassword)
	{
		this.adcCliPassword = adcCliPassword;
	}
	public String getPeerAdcIPAddress()
	{
		return peerAdcIPAddress;
	}
	public void setPeerAdcIPAddress(String peerAdcIPAddress)
	{
		this.peerAdcIPAddress = peerAdcIPAddress;
	}
	public ArrayList<Integer> getAccountIndexList()
	{
		return this.accountIndexList;
	}
	public void setAccountIndexList(ArrayList<Integer> accountIndexList)
	{
		this.accountIndexList = accountIndexList;
	}
	public Integer getIndex()
	{
		return this.index;
	}
	public void setIndex(Integer index)
	{
		this.index = index;
	}
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getAdcIpAddress()
	{
		return this.adcIpAddress;
	}
	public void setAdcIpAddress(String adcIpAddress)
	{
		this.adcIpAddress = adcIpAddress;
	}
	public String getAdcAccount()
	{
		return this.adcAccount;
	}
	public void setAdcAccount(String adcAccount)
	{
		this.adcAccount = adcAccount;
	}
	public String getAdcPasswordDecrypt()
	{
		String result = new OBCipherAES().Decrypt(this.adcPassword);
		return result;
	}
	public String getAdcPassword()
	{
		return this.adcPassword;
	}
	public void setAdcPassword(String adcPassword)
	{
		this.adcPassword = adcPassword;
	}
	/**
	 * ADC 장비 종류 추출
	 * @return 1: F5, 2: Alteon
	 */
	public Integer getAdcType()
	{
		return this.adcType;
	}
	/**
	 * ADC 장비의 종류 설정.
	 * @param adcType
	 * 			-- 1: F5, 2: Alteon
	 */
	public void setAdcType(Integer adcType)
	{
		this.adcType = adcType;
	}
	public String getModel()
	{
		return this.model;
	}
	public void setModel(String model)
	{
		this.model = model;
	}
	public Integer getGroupIndex()
	{
		return this.groupIndex;
	}
	public void setGroupIndex(Integer groupIndex)
	{
		this.groupIndex = groupIndex;
	}
	public String getDescription()
	{
		return this.description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public Integer getActivePairIndex()
	{
		return this.activePairIndex;
	}
	public void setActivePairIndex(Integer activePairIndex)
	{
		this.activePairIndex = activePairIndex;
	}	
	public Integer getConnService()
	{
		return connService;
	}
	public void setConnService(Integer connService)
	{
		this.connService = connService;
	}
	public Integer getConnPort()
	{
		return connPort;
	}
	public void setConnPort(Integer connPort)
	{
		this.connPort = connPort;
	}
	//	public Integer getAvailable()
//	{
//		return this.available;
//	}
//	public void setAvailable(Integer available)
//	{
//		this.available = available;
//	}
	public String getSwVersion()
	{
		return this.swVersion;
	}
	public void setSwVersion(String swVersion)
	{
		this.swVersion = swVersion;
	}
	public String getHostName()
	{
		return this.hostName;
	}
	public void setHostName(String hostName)
	{
		this.hostName = hostName;
	}
	public Timestamp getApplyTime()
	{
		return this.applyTime;
	}
	public void setApplyTime(Timestamp applyTime)
	{
		this.applyTime = applyTime;
	}
	public Timestamp getSaveTime()
	{
		return this.saveTime;
	}
	public void setSaveTime(Timestamp saveTime)
	{
		this.saveTime = saveTime;
	}
	public Timestamp getLastBootTime()
	{
		return this.lastBootTime;
	}
	public void setLastBootTime(Timestamp lastBootTime)
	{
		this.lastBootTime = lastBootTime;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public Integer getStatus()
	{
		return this.status;
	}
	public String getSnmpRComm()
	{
		return snmpRComm;
	}
	public void setSnmpRComm(String snmpRComm)
	{
		this.snmpRComm = snmpRComm;
	}
	public Date getPurchaseDate()
	{
		return purchaseDate;
	}
	public void setPurchaseDate(Date purchaseDate)
	{
		this.purchaseDate = purchaseDate;
	}
	public String getSyslogIpAddress() 
	{
		return syslogIpAddress;
	}
	public void setSyslogIpAddress(String syslogIpAddress) 
	{
		this.syslogIpAddress = syslogIpAddress;
	}
	public Integer getRoleFlbYn()
	{
		return roleFlbYn;
	}
	public void setRoleFlbYn(Integer roleFlbYn)
	{
		this.roleFlbYn = roleFlbYn;
	}
	public OBDtoAdcSnmpInfo getSnmpInfo()
	{
		return snmpInfo;
	}
	public void setSnmpInfo(OBDtoAdcSnmpInfo snmpInfo)
	{
		this.snmpInfo = snmpInfo;
	}
	public Integer getSpSessionMax()
	{
		return spSessionMax;
	}
	public void setSpSessionMax(Integer spSessionMax)
	{
		this.spSessionMax = spSessionMax;
	}
    public Integer getOpMode()
    {
        return opMode;
    }
    public void setOpMode(Integer opMode)
    {
        this.opMode = opMode;
    }
    public Integer getMgmtMode()
    {
        return mgmtMode;
    }
    public void setMgmtMode(Integer mgmtMode)
    {
        this.mgmtMode = mgmtMode;
    }
    public Integer getSsoMode()
    {
        return ssoMode;
    }
    public void setSsoMode(Integer ssoMode)
    {
        this.ssoMode = ssoMode;
    }
    public Integer getConnProtocol()
    {
        return connProtocol;
    }
    public void setConnProtocol(Integer connProtocol)
    {
        this.connProtocol = connProtocol;
    }
}
