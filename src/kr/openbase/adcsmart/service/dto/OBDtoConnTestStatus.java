package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

public class OBDtoConnTestStatus
{
	public static final int TEST_STATUS_INIT 	= 1;// 초기 상태.
	public static final int TEST_STATUS_OK 		= 2;// 정상 상태.
	public static final int TEST_STATUS_FAIL 	= 3;// 비정상 상태.
	public static final int TEST_STATUS_DONE 	= 4;// 끝난 상태.
	public static final int TEST_STATUS_ING 	= 5;// 진행중인 상태. 
	
	public static final int TEST_ID_MAIN 		= 0;//  
	public static final int TEST_ID_PORTOPEN 	= 1;// 포트 오픈 테스트   
	public static final int TEST_ID_LOGIN 		= 2;// 로그인 테스트  
	public static final int TEST_ID_VERSION		= 3;// version  
	public static final int TEST_ID_PORTREVERSE = 4;// 포트 오픈 테스트(역방향). alteon만 해당됨. 
	public static final int TEST_ID_SNMP 		= 5;// snmp 수신 테스트 
	public static final int TEST_ID_SYSLOG 		= 6;// syslog 수신 테스트. 
	
//	private Integer currStatus;// current status.  TEST_STATUS_INIT, TEST_STATUS_INIT, TEST_STATUS_ING 중 하나의 상태임.
	private Integer testID;//
	private Integer testResult;
	private String  testResultStr;
	
	ArrayList<OBDtoConnTestStatus> testItemList;

	@Override
	public String toString()
	{
		return "OBDtoConnTestStatus [testID=" + testID + ", testResult=" + testResult + ", testResultStr=" + testResultStr + ", testItemList=" + testItemList + "]";
	}

	public Integer getTestID()
	{
		return testID;
	}

	public void setTestID(Integer testID)
	{
		this.testID = testID;
	}

	public Integer getTestResult()
	{
		return testResult;
	}

	public void setTestResult(Integer testResult)
	{
		this.testResult = testResult;
	}

	public String getTestResultStr()
	{
		return testResultStr;
	}

	public void setTestResultStr(String testResultStr)
	{
		this.testResultStr = testResultStr;
	}

	public ArrayList<OBDtoConnTestStatus> getTestItemList()
	{
		return testItemList;
	}

	public void setTestItemList(ArrayList<OBDtoConnTestStatus> testItemList)
	{
		this.testItemList = testItemList;
	}
}
