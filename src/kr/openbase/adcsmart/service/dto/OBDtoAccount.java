/*
 * a 사용자 계정 DTO
 */
package kr.openbase.adcsmart.service.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OBDtoAccount
{
	private Integer accountIndex;
	private String 	accountId;
	private String 	accountName;
	private String 	accountComment;
	private String 	accountPassword;
	private String telephone;
	private String mobilePhone;
	private String email;
	private Timestamp lastLogin;
	private Integer roleNo;
	private boolean isInitailizedPasswd=false;
	private List<OBDtoAdcVSInfo> adcVsList;	// 계정에 관리권한이 부여된 adc의 이름 목록
	private List<OBDtoAdcRSInfo> adcRsList;	// 계정에 관리권한이 부여된 adc의 Real 목록
	private Integer alertWindow;
	private Integer alertSound;	
	private long periodStartTime;					// 설정한 사용 시작 기간 
	private long periodEndTime;						// 설정한 사용 종료 기간 
	private String ipFilter;   						// 특정 IP 대역에서 모니터링 모드 접속
	private String histroy;							// 변경된 비밀번호들(2018-12-11 추가)
	private Timestamp changedTime;						// 비밀번호 변경일자(2018-12-11 추가)
	
//	private ArrayList<Integer> adcList;	//계정에 관리권한이 부여된 adc의 이름 목록
	
	public long getPeriodStartTime()
	{
		return periodStartTime;
	}
	@Override
	public String toString() {
		return "OBDtoAccount [accountIndex=" + accountIndex + ", accountId=" + accountId + ", accountName="
				+ accountName + ", accountComment=" + accountComment + ", accountPassword=" + accountPassword
				+ ", telephone=" + telephone + ", mobilePhone=" + mobilePhone + ", email=" + email + ", lastLogin="
				+ lastLogin + ", roleNo=" + roleNo + ", isInitailizedPasswd=" + isInitailizedPasswd + ", adcVsList="
				+ adcVsList + ", adcRsList=" + adcRsList + ", alertWindow=" + alertWindow + ", alertSound=" + alertSound
				+ ", periodStartTime=" + periodStartTime + ", periodEndTime=" + periodEndTime + ", ipFilter=" + ipFilter
				+ ", histroy=" + histroy + ", changedTime=" + changedTime + "]";
	}
	public void setPeriodStartTime(long periodStartTime)
	{
		this.periodStartTime = periodStartTime;
	}
	public long getPeriodEndTime()
	{
		return periodEndTime;
	}
	public void setPeriodEndTime(long periodEndTime)
	{
		this.periodEndTime = periodEndTime;
	}
	public List<OBDtoAdcVSInfo> getAdcVsList()
	{
		return adcVsList;
	}
	public void setAdcVsList(List<OBDtoAdcVSInfo> adcVsList)
	{
		this.adcVsList = adcVsList;
	}
	public Integer getAccountIndex()
	{
		return accountIndex;
	}
	public void setAccountIndex(Integer accountIndex)
	{
		this.accountIndex = accountIndex;
	}
	public boolean isInitailizedPasswd()
	{
		return isInitailizedPasswd;
	}
	public void setInitailizedPasswd(boolean isInitailizedPasswd)
	{
		this.isInitailizedPasswd = isInitailizedPasswd;
	}
	public String getAccountId()
	{
		return accountId;
	}
	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
	}
	public String getAccountName()
	{
		return accountName;
	}
	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}
	public String getAccountComment()
	{
		return accountComment;
	}
	public void setAccountComment(String accountComment)
	{
		this.accountComment = accountComment;
	}
	public String getAccountPassword()
	{
		return accountPassword;
	}
	public void setAccountPassword(String accountPassword)
	{
		this.accountPassword = accountPassword;
	}
	public String getTelephone()
	{
		return telephone;
	}
	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
	}
	public String getMobilePhone()
	{
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone)
	{
		this.mobilePhone = mobilePhone;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public Integer getRoleNo()
	{
		return roleNo;
	}
	public void setRoleNo(Integer roleNo)
	{
		this.roleNo = roleNo;
	}
	public Integer getAlertWindow()
	{
		return alertWindow;
	}
	public void setAlertWindow(Integer alertWindow)
	{
		this.alertWindow = alertWindow;
	}
	public Integer getAlertSound()
	{
		return alertSound;
	}
	public void setAlertSound(Integer alertSound)
	{
		this.alertSound = alertSound;
	}
	public Timestamp getLastLogin()
	{
		return lastLogin;
	}
	public void setLastLogin(Timestamp lastLogin)
	{
		this.lastLogin = lastLogin;
	}
	public List<OBDtoAdcRSInfo> getAdcRsList()
	{
		return adcRsList;
	}
	public void setAdcRsList(List<OBDtoAdcRSInfo> adcRsList)
	{
		this.adcRsList = adcRsList;
	}
    public String getIpFilter()
    {
        return ipFilter;
    }
    public void setIpFilter(String ipFilter)
    {
        this.ipFilter = ipFilter;
    }
	public String getHistroy() {
		return histroy;
	}
	public void setHistroy(String histroy) {
		this.histroy = histroy;
	}
	public Timestamp getChangedTime() {
		return changedTime;
	}
	public void setChangedTime(Timestamp changedTime) {
		this.changedTime = changedTime;
	}

}
