package kr.openbase.adcsmart.web.facade.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.OBAccount;
import kr.openbase.adcsmart.service.dto.OBDtoAccount;
import kr.openbase.adcsmart.service.dto.OBDtoAdcRSInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVSInfo;
import kr.openbase.adcsmart.service.impl.OBAccountImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.util.OBDateTimeWeb;
import kr.openbase.adcsmart.web.util.OBDefineWeb;

/**
 * @author openbase
 *
 */
public class AccountDto {
	private Integer index;
	private String id;
	private String password;
	private String confirmPassword;
	private String name;
	private String emailBeforeDomain;
	private String emailDomain;
	private String phone;
	private String userIp;
	private String description;
	private Integer usesAlertWnd;
	private Boolean usesAlertBeep = false;
	private Date lastLoginTime;
	private Integer roleId;
	private Integer loginFailCount;
	private List<Integer> adcIndices;
	private List<String> adcNames;
	private Boolean isPasswordReset;
	private String nameList; // 관리 ADC 리스트에서 "[" 값 제거하기위해 만듬
	private String accountMode;
	private Date startTime;
	private Date endTime;
	private Long LstartTime;
	private Long LendTime;
	private ArrayList<OBDtoAdcVSInfo> adcVsList;
	private ArrayList<OBDtoAdcRSInfo> adcRsList;
	private String ipFilter;
	private String history; 
	private Timestamp changedTime;
	

	public static AccountDto toAccountDto(OBDtoAccount accountFromSvc) throws Exception {
		if (accountFromSvc == null) {
			return null;
		}
		AccountDto account = new AccountDto();
		account.setIndex(accountFromSvc.getAccountIndex());
		account.setId(accountFromSvc.getAccountId());
		account.setPassword(accountFromSvc.getAccountPassword());
		account.setName(accountFromSvc.getAccountName());
		if (accountFromSvc.getPeriodEndTime() == 0L) {
			account.setAccountMode("unLimitedMode");
			account.setStartTime(null);
			account.setEndTime(null);
			account.setLstartTime(null);
			account.setLendTime(null);
		} else {
			account.setAccountMode("LimitedMode");
			account.setLstartTime(accountFromSvc.getPeriodStartTime());
			account.setLendTime(accountFromSvc.getPeriodEndTime());
			Date StartTimeDate = new Date(accountFromSvc.getPeriodStartTime());
			Date EndTimeDate = new Date(accountFromSvc.getPeriodEndTime());
			account.setStartTime(StartTimeDate); // 시작 기간 매핑
			account.setEndTime(EndTimeDate); // 끝나는 기간 매핑
		}

		if (accountFromSvc.getEmail() != null) {
			String[] splitEmail = accountFromSvc.getEmail().split("@");

			if (splitEmail.length > 1) {
				account.setEmailBeforeDomain(splitEmail[0]);
				account.setEmailDomain(splitEmail[1]);
			} else if (splitEmail.length > 0) {
				account.setEmailBeforeDomain(splitEmail[0]);
				account.setEmailDomain("");
			} else {
				account.setEmailBeforeDomain("");
				account.setEmailDomain("");
			}
		}

		account.setPhone(accountFromSvc.getMobilePhone());
		account.setRoleId(accountFromSvc.getRoleNo());
		account.setDescription(accountFromSvc.getAccountComment());
		account.setUsesAlertWnd(accountFromSvc.getAlertWindow());
		account.setUsesAlertBeep(
				accountFromSvc.getAlertSound() != null && accountFromSvc.getAlertSound() == OBDefineWeb.ALERT_SOUND_ON
						? true
						: false);
		List<OBDtoAdcVSInfo> adcsFromSvc = accountFromSvc.getAdcVsList();
		account.setAdcIndices(new ArrayList<Integer>(adcsFromSvc.size()));
		account.setAdcNames(new ArrayList<String>(adcsFromSvc.size()));
		account.setUserIp(accountFromSvc.getIpFilter());

		String nameListString = "";
		if (adcsFromSvc.size() > 0 && adcsFromSvc != null) {
			for (OBDtoAdcVSInfo e : adcsFromSvc) {
				account.getAdcIndices().add(e.getAdcIndex());
				account.getAdcNames().add(e.getAdcName()); // 서비스에서 adcName 값을 받아와 List에 넣는다.
				nameListString += e.getAdcName() + " "; // 서비스에서 adcName을 받아와 String 형으로 저장한다.
			}
			account.setNameList(nameListString);
		} else {
			account.setNameList("");
		}

		account.setIsPasswordReset(accountFromSvc.isInitailizedPasswd());
		account.setIpFilter(accountFromSvc.getIpFilter());
		try {
			OBAccount accountSvc = new OBAccountImpl();
			account.setLastLoginTime(accountSvc.getLastloginTime(accountFromSvc.getAccountIndex()));
			account.setLoginFailCount(accountSvc.getLoginFailCount(accountFromSvc.getAccountIndex()));
		} catch (OBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		account.setHistory(accountFromSvc.getHistroy());
		account.setChangedTime(accountFromSvc.getChangedTime());
		
		return account;
	}

	public static List<AccountDto> toAccountDto(List<OBDtoAccount> accountsFromSvc) throws Exception {
		List<AccountDto> accounts = new ArrayList<AccountDto>();
		for (OBDtoAccount e : accountsFromSvc) {
			accounts.add(toAccountDto(e));

		}
		return accounts;
	}

	public static OBDtoAccount toOBDtoAccount(AccountDto account) throws Exception {
		OBDtoAccount accountFromSvc = new OBDtoAccount();
		accountFromSvc.setAccountIndex(account.getIndex());
		accountFromSvc.setAccountId(account.getId());
		accountFromSvc.setAccountName(account.getName());
		accountFromSvc.setAccountPassword(account.getPassword());
		accountFromSvc.setRoleNo(account.getRoleId());
		accountFromSvc.setMobilePhone(account.getPhone());
		accountFromSvc.setAccountComment(account.getDescription());
		accountFromSvc.setAlertWindow(account.getUsesAlertWnd());
		accountFromSvc
				.setAlertSound(account.getUsesAlertBeep() ? OBDefineWeb.ALERT_SOUND_ON : OBDefineWeb.ALERT_SOUND_OFF);
		accountFromSvc.setEmail(account.getEmailBeforeDomain() + "@" + account.getEmailDomain());
		if (account.getAccountMode() != null) {
			if (account.getAccountMode().equals("unLimitedMode")) {
				accountFromSvc.setPeriodStartTime(0L);
				accountFromSvc.setPeriodEndTime(0L);
			} else {
				Date endPeriod = new Date();
				endPeriod = OBDateTimeWeb.initTimeOfDate(account.getEndTime(), true);
				accountFromSvc.setPeriodStartTime(account.getStartTime().getTime());
				accountFromSvc.setPeriodEndTime(endPeriod.getTime());
			}
		}
		if (account.getAdcVsList() != null) {
			accountFromSvc.setAdcVsList(account.getAdcVsList());
		}
		if (account.getAdcRsList() != null) {
			accountFromSvc.setAdcRsList(account.getAdcRsList());
		}

		accountFromSvc.setIpFilter(account.getIpFilter());
//		if (account.getAdcIndices() != null) {
//			ArrayList<OBDtoAdcVSInfo> adcsFromSvc = new ArrayList<OBDtoAdcVSInfo>(account.getAdcIndices().size());
//			for (Integer e : account.getAdcIndices()) {
//				OBDtoAdcVSInfo adcFromSvc = new OBDtoAdcVSInfo();
//				adcFromSvc.setAdcIndex(e);
//				adcsFromSvc.add(adcFromSvc);
//			}		
//			accountFromSvc.setAdcVsList(adcsFromSvc);
//		}

		accountFromSvc.setHistroy(account.getHistory());
		accountFromSvc.setChangedTime(account.getChangedTime());
		
		return accountFromSvc;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailBeforeDomain() {
		return emailBeforeDomain;
	}

	public void setEmailBeforeDomain(String emailBeforeDomain) {
		this.emailBeforeDomain = emailBeforeDomain;
	}

	public String getEmailDomain() {
		return emailDomain;
	}

	public void setEmailDomain(String emailDomain) {
		this.emailDomain = emailDomain;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getUsesAlertWnd() {
		return usesAlertWnd;
	}

	public void setUsesAlertWnd(Integer usesAlertWnd) {
		this.usesAlertWnd = usesAlertWnd;
	}

	public Boolean getUsesAlertBeep() {
		return usesAlertBeep;
	}

	public void setUsesAlertBeep(Boolean usesAlertBeep) {
		this.usesAlertBeep = usesAlertBeep;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getLoginFailCount() {
		return loginFailCount;
	}

	public void setLoginFailCount(Integer loginFailCount) {
		this.loginFailCount = loginFailCount;
	}

	public List<Integer> getAdcIndices() {
		return adcIndices;
	}

	public void setAdcIndices(List<Integer> adcIndices) {
		this.adcIndices = adcIndices;
	}

	public List<String> getAdcNames() {
		return adcNames;
	}

	public void setAdcNames(List<String> adcNames) {
		this.adcNames = adcNames;
	}

	public Boolean getIsPasswordReset() {
		return isPasswordReset;
	}

	public void setIsPasswordReset(Boolean isPasswordReset) {
		this.isPasswordReset = isPasswordReset;
	}

	public String getNameList() {
		return nameList;
	}

	public void setNameList(String nameList) {
		this.nameList = nameList;
	}

	public String getAccountMode() {
		return accountMode;
	}

	public void setAccountMode(String accountMode) {
		this.accountMode = accountMode;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getLstartTime() {
		return LstartTime;
	}

	public void setLstartTime(Long lstartTime) {
		LstartTime = lstartTime;
	}

	public Long getLendTime() {
		return LendTime;
	}

	public void setLendTime(Long lendTime) {
		LendTime = lendTime;
	}

	public ArrayList<OBDtoAdcVSInfo> getAdcVsList() {
		return adcVsList;
	}

	public void setAdcVsList(ArrayList<OBDtoAdcVSInfo> adcVsList) {
		this.adcVsList = adcVsList;
	}

	public ArrayList<OBDtoAdcRSInfo> getAdcRsList() {
		return adcRsList;
	}

	public void setAdcRsList(ArrayList<OBDtoAdcRSInfo> adcRsList) {
		this.adcRsList = adcRsList;
	}

	public String getIpFilter() {
		return ipFilter;
	}

	public void setIpFilter(String ipFilter) {
		this.ipFilter = ipFilter;
	}
	
	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	
	public Timestamp getChangedTime() {
		return changedTime;
	}

	public void setChangedTime(Timestamp changedTime) {
		this.changedTime = changedTime;
	}

	@Override
	public String toString() {
		return "AccountDto [index=" + index + ", id=" + id + ", password=" + password + ", confirmPassword="
				+ confirmPassword + ", name=" + name + ", emailBeforeDomain=" + emailBeforeDomain + ", emailDomain="
				+ emailDomain + ", phone=" + phone + ", userIp=" + userIp + ", description=" + description
				+ ", usesAlertWnd=" + usesAlertWnd + ", usesAlertBeep=" + usesAlertBeep + ", lastLoginTime="
				+ lastLoginTime + ", roleId=" + roleId + ", loginFailCount=" + loginFailCount + ", adcIndices="
				+ adcIndices + ", adcNames=" + adcNames + ", isPasswordReset=" + isPasswordReset + ", nameList="
				+ nameList + ", accountMode=" + accountMode + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", LstartTime=" + LstartTime + ", LendTime=" + LendTime + ", adcVsList=" + adcVsList + ", adcRsList="
				+ adcRsList + ", ipFilter=" + ipFilter + ", history=" + history + ", changedTime=" + changedTime + "]";
	}

	

}