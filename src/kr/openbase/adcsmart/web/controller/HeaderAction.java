package kr.openbase.adcsmart.web.controller;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import kr.openbase.adcsmart.web.facade.LoginFacade;
import kr.openbase.adcsmart.web.facade.dto.AccountDto;

@Controller
@Scope(value = "prototype")
public class HeaderAction extends BaseAction 
{
	private transient Logger log = LoggerFactory.getLogger(HeaderAction.class);
	
	@Autowired
	private LoginFacade loginFacade;
	
	private AccountDto account;
	
	@Value("#{settings['site.isSDSSite']}") 
	private boolean varIsSDSSite;						// ftl에서 사용하는 변수임.
	
	@Value("#{settings['account.isIpChk']}")
	private boolean ipChkFlag;
	
	public String loadHeader() throws Exception
	{
		try
		{
			account = loginFacade.getAccount(session.getAccountId());	
//			ipChkFlag = loginFacade.getIpCheck(account.getIndex(), request.getRemoteAddr());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception(e);
		}
		
		log.debug("{}", account);
		return SUCCESS;
	}
	
	public String loadLeftNavi() throws Exception
	{
		return SUCCESS;
	}
	
	public String loadPickView() throws Exception
	{
		return SUCCESS;
	}
	
	public String loadListContaier() throws Exception
	{
		return SUCCESS;
	}
	
	public String loadAdcSearchBar() throws Exception
	{
		return SUCCESS;
	}	
	
	public String loadContent() throws Exception
	{		
		return SUCCESS;
	}
		
	public String loadLoginCheck() throws Exception
	{
//	    System.out.println("loginCheck : " + session.getAccountId());	   
		account = loginFacade.getAccount(session.getAccountId());
		Calendar cal = Calendar.getInstance();	 
		isSuccessful = true;
		try
		{
//		    System.out.println("loginCheck :" + request.getSession().isNew());
//		    session.setAccountId(null);
		    if((session.getAccountId() == null) || (session.getAccountId() == "") || (request.getSession().isNew() == true))
	            isSuccessful = false;
//		    if(request.getSession().isNew() == true)
//		        isSuccessful = false;
		    else
		        loginFacade.setLastAliveTime(account.getIndex(), cal.getTime());			
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception(e);
		}
		
		return SUCCESS;
	}
	
	public AccountDto getAccount()
	{
		return account;
	}

	public void setAccount(AccountDto account)
	{
		this.account = account;
	}
	
	public boolean isVarIsSDSSite()
	{
		return varIsSDSSite;
	}

	public void setVarIsSDSSite(boolean varIsSDSSite)
	{
		this.varIsSDSSite = varIsSDSSite;
	}

    public boolean isIpChkFlag()
    {
        return ipChkFlag;
    }

    public void setIpChkFlag(boolean ipChkFlag)
    {
        this.ipChkFlag = ipChkFlag;
    }
}
