package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.utility.OBCipherAES;

public class OBAdcCfgInfo
{
	private String 			snmpState;
	private String          snmpStateResult;
	private String  		snmpRCommunityDB;
	private String          snmpRcommunityADC;
	private String          snmpRcommunityResult;
	private String	 		syslogState;
	private String          syslogStateResult;
	private ArrayList<String> syslogServerList;
	private String 			allowList;
	private String  		syslogServerListResult;
	private String          allowListResult;
	private String 			syslogLevel;
	private String    		syslogLevelResult;
	private String			vstatState;
	private String			vstatStateResult;
	private String          adcIpaddress;
	private String          adcId;
	private String 			adcPassword;
	private Integer			adcType;
	private Integer 		function_loginState;
	private String 			function_loginStateResult;
	private Date 			function_loginStateTime;
	private Integer 		function_cliLoginState;
	private String 			function_cliLoginStateResult;
	private Date 			function_cliLoginStateTime;
	private String          function_snmpState;
	private String 			function_snmpStateResult;
	private Date			function_snmpStateTime;
	private String 			function_syslogState;
	private String  		function_syslogStateResult;
	private Date 			function_syslogStateTime;
	private String 			nameList; //관리 ADC 리스트에서 "[" 값 제거하기위해 만듬
	@Override
	public String toString()
	{
		return "OBAdcCfgInfo [snmpState=" + snmpState + ", snmpStateResult=" + snmpStateResult 
				+ ", snmpRCommunityDB=" + snmpRCommunityDB + ", snmpRcommunityADC=" + snmpRcommunityADC 
				+ ", snmpRcommunityResult=" + snmpRcommunityResult + ", syslogState=" + syslogState 
				+ ", syslogStateResult=" + syslogStateResult + ", syslogServerList=" + syslogServerList 
				+ ", allowList=" + allowList + ", syslogServerListResult=" + syslogServerListResult 
				+ ", syslogLevel=" + syslogLevel + ", syslogLevelResult=" + syslogLevelResult 
				+ ", vstatState=" + vstatState + ", vstatStateResult=" + vstatStateResult 
				+ ", adcIpaddress=" + adcIpaddress + ", adcId=" + adcId + ", adcPassword=" + adcPassword 
				+ ", adcType=" + adcType + ", function_loginState=" + function_loginState 
				+ ", function_loginStateTime=" + function_loginStateTime + ", function_cliLoginState=" + function_cliLoginState 
				+ ", function_cliLoginStateTime=" + function_cliLoginStateTime+ ", function_snmpState=" + function_snmpState 
				+ ", function_snmpStateResult=" + function_snmpStateResult + ", function_snmpStateTime=" + function_snmpStateTime 
				+ ", function_syslogState=" + function_syslogState + ", function_syslogStateResult=" + function_syslogStateResult 
				+ ", function_syslogStateTime=" + function_syslogStateTime + "]";
	}

	public String getSnmpState()
	{
		return snmpState;
	}

	public void setSnmpState(String snmpState)
	{
		this.snmpState = snmpState;
	}

	public String getSnmpStateResult()
	{
		return snmpStateResult;
	}

	public void setSnmpStateResult(String snmpStateResult)
	{
		this.snmpStateResult = snmpStateResult;
	}

	public String getSnmpRCommunityDB()
	{
		return snmpRCommunityDB;
	}

	public void setSnmpRCommunityDB(String snmpRCommunityDB)
	{
		this.snmpRCommunityDB = snmpRCommunityDB;
	}

	public String getSnmpRcommunityADC()
	{
		return snmpRcommunityADC;
	}

	public void setSnmpRcommunityADC(String snmpRcommunityADC)
	{
		this.snmpRcommunityADC = snmpRcommunityADC;
	}

	public String getSnmpRcommunityResult()
	{
		return snmpRcommunityResult;
	}

	public void setSnmpRcommunityResult(String snmpRcommunityResult)
	{
		this.snmpRcommunityResult = snmpRcommunityResult;
	}

	public String getSyslogState()
	{
		return syslogState;
	}

	public void setSyslogState(String syslogState)
	{
		this.syslogState = syslogState;
	}

	public String getSyslogStateResult()
	{
		return syslogStateResult;
	}

	public void setSyslogStateResult(String syslogStateResult)
	{
		this.syslogStateResult = syslogStateResult;
	}

	public ArrayList<String> getSyslogServerList()
	{
		return syslogServerList;
	}

	public void setSyslogServerList(ArrayList<String> syslogServerList)
	{
		this.syslogServerList = syslogServerList;
	}

	public String getAllowList()
	{
		return allowList;
	}

	public void setAllowList(String allowList)
	{
		this.allowList = allowList;
	}

	public String getSyslogLevel()
	{
		return syslogLevel;
	}

	public void setSyslogLevel(String syslogLevel)
	{
		this.syslogLevel = syslogLevel;
	}

	public String getAllowListResult()
	{
		return allowListResult;
	}

	public void setAllowListResult(String allowListResult)
	{
		this.allowListResult = allowListResult;
	}

	public String getSyslogLevelResult()
	{
		return syslogLevelResult;
	}

	public void setSyslogLevelResult(String syslogLevelResult)
	{
		this.syslogLevelResult = syslogLevelResult;
	}

	public String getVstatState()
	{
		return vstatState;
	}

	public void setVstatState(String vstatState)
	{
		this.vstatState = vstatState;
	}
	
	public String getVstatStateResult()
	{
		return vstatStateResult;
	}

	public void setVstatStateResult(String vstatStateResult)
	{
		this.vstatStateResult = vstatStateResult;
	}

	public String getAdcIpaddress()
	{
		return adcIpaddress;
	}

	public void setAdcIpaddress(String adcIpaddress)
	{
		this.adcIpaddress = adcIpaddress;
	}

	public String getAdcId()
	{
		return adcId;
	}

	public void setAdcId(String adcId)
	{
		this.adcId = adcId;
	}

	public String getAdcPassword()
	{
		return adcPassword;
	}
	
	public void setAdcPassword(String adcPassword)
	{
		String psWord = new OBCipherAES().Decrypt(adcPassword);
		this.adcPassword = psWord;
	}

	public Integer getAdcType()
	{
		return adcType;
	}

	public void setAdcType(Integer adcType)
	{
		this.adcType = adcType;
	}

	public Integer getFunction_loginState()
	{
		return function_loginState;
	}

	public void setFunction_loginState(Integer function_loginState)
	{
		this.function_loginState = function_loginState;
	}

	public Integer getFunction_cliLoginState()
	{
		return function_cliLoginState;
	}

	public void setFunction_cliLoginState(Integer function_cliLoginState)
	{
		this.function_cliLoginState = function_cliLoginState;
	}

	public String getFunction_cliLoginStateResult()
	{
		return function_cliLoginStateResult;
	}

	public void setFunction_cliLoginStateResult(String function_cliLoginStateResult)
	{
		this.function_cliLoginStateResult = function_cliLoginStateResult;
	}

	public Date getFunction_cliLoginStateTime()
	{
		return function_cliLoginStateTime;
	}

	public void setFunction_cliLoginStateTime(Date function_cliLoginStateTime)
	{
		this.function_cliLoginStateTime = function_cliLoginStateTime;
	}

	public String getFunction_snmpStateResult()
	{
		return function_snmpStateResult;
	}

	public void setFunction_snmpStateResult(String function_snmpStateResult)
	{
		this.function_snmpStateResult = function_snmpStateResult;
	}

	public String getFunction_snmpState()
	{
		return function_snmpState;
	}

	public void setFunction_snmpState(String function_snmpState)
	{
		this.function_snmpState = function_snmpState;
	}
	
	public String getFunction_syslogState()
	{
		return function_syslogState;
	}

	public void setFunction_syslogState(String function_syslogState)
	{
		this.function_syslogState = function_syslogState;
	}

	public String getFunction_syslogStateResult()
	{
		return function_syslogStateResult;
	}

	public void setFunction_syslogStateResult(String function_syslogStateResult)
	{
		this.function_syslogStateResult = function_syslogStateResult;
	}

	public String getSyslogServerListResult()
	{
		return syslogServerListResult;
	}

	public void setSyslogServerListResult(String syslogServerListResult)
	{
		this.syslogServerListResult = syslogServerListResult;
	}	

	public Date getFunction_syslogStateTime()
	{
		return function_syslogStateTime;
	}

	public void setFunction_syslogStateTime(Date function_syslogStateTime)
	{
		this.function_syslogStateTime = function_syslogStateTime;
	}

	public Date getFunction_snmpStateTime()
	{
		return function_snmpStateTime;
	}

	public void setFunction_snmpStateTime(Date function_snmpStateTime)
	{
		this.function_snmpStateTime = function_snmpStateTime;
	}

	public Date getFunction_loginStateTime()
	{
		return function_loginStateTime;
	}

	public void setFunction_loginStateTime(Date function_loginStateTime)
	{
		this.function_loginStateTime = function_loginStateTime;
	}
	
	public String getNameList()
	{
		return nameList;
	}

	public void setNameList(String nameList)
	{
		this.nameList = nameList;
	}

	public String getFunction_loginStateResult()
	{
		return function_loginStateResult;
	}

	public void setFunction_loginStateResult(String function_loginStateResult)
	{
		this.function_loginStateResult = function_loginStateResult;
	}	
}