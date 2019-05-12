package kr.openbase.adcsmart.web.session;

import java.sql.Timestamp;

import javax.servlet.http.HttpSession;

import kr.openbase.adcsmart.web.facade.dto.SessionDto;

public class AdcSession {
	private HttpSession session;
	
	public AdcSession(HttpSession session) {
		this.session = session;
	}
	
	public void invalidate() {
		if (session != null)
			session.invalidate();
	}
	
	public Timestamp getLoginTime() {
		Long occurTime = (Long)session.getAttribute("loginTime");
		Timestamp loginTime = new Timestamp(occurTime);
		return loginTime;
	}
	
	public String getAccountId() {
		return (String) session.getAttribute("accountId");
	}
	
	public String getAccountPassword() {
		return (String) session.getAttribute("accountPassword");
	}
	
	public String getAccountRole() {
		return (String) session.getAttribute("accountRole");
	}
	
	public String getClientIp() {
		return (String) session.getAttribute("clientIp");
	}
	
	public Integer getAccountIndex() {
		return (Integer) session.getAttribute("accountIndex");
	}
	
	public void setAccountId(String accountId) {
		session.setAttribute("accountId", accountId);
	}
	
	public void setAccountPassword(String accountPassword) {
		session.setAttribute("accountPassword", accountPassword);
	}
	
	public void setAccountRole(String accountRole) {
		session.setAttribute("accountRole", accountRole);
	}
	
	public void setClientIp(String ip) {
		session.setAttribute("clientIp", ip);
	}
	
	public void setAccountIndex(Integer index) {
		session.setAttribute("accountIndex", index);
	}
	
	public void setLoginTime(Timestamp occurTime) {
		session.setAttribute("loginTime", occurTime.getTime());
	}

	public void setAlertCheckboxStatus(Integer alertCheckboxStatus )
	{
		session.setAttribute("alertCheckboxStatus", alertCheckboxStatus);
	}
	public Integer getAlertCheckboxStatus()
	{	
		return (Integer)session.getAttribute("alertCheckboxStatus"); 	
	} 
	
	
	public SessionDto getSessionDto() {
		SessionDto sessionDto = new SessionDto();
		sessionDto.setAccountIndex(getAccountIndex());
		sessionDto.setClientIp(getClientIp());
		return sessionDto;
	}
}
