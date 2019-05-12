package kr.openbase.adcsmart.web.controller;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoNoticeInfo;
import kr.openbase.adcsmart.web.facade.NoticeFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope(value = "prototype")
public class NoticeAction extends BaseAction
{
	@Autowired
	private NoticeFacade noticeFacade;
	
	@Value("#{settings['header.noticeTime']}") 
	private Integer noticeTime;
	
//	private transient Logger log = LoggerFactory.getLogger(NoticeAction.class);
	private Integer	accountIndex;
	private ArrayList<OBDtoNoticeInfo> noticeMessage;
	
	public String loadNoticeInfo() throws Exception 
	{
		try
		{
			accountIndex = session.getAccountIndex();				
			noticeMessage = noticeFacade.getNoticeMessage(accountIndex);			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw e;
		}		
		
		return SUCCESS;
	}
	
	public Integer getNoticeTime()
	{
		return noticeTime;
	}
	
	public void setNoticeTime(Integer noticeTime)
	{
		this.noticeTime = noticeTime;
	}
	
	public Integer getAccountIndex()
	{
		return accountIndex;
	}
	
	public void setAccountIndex(Integer accountIndex)
	{
		this.accountIndex = accountIndex;
	}

	public ArrayList<OBDtoNoticeInfo> getNoticeMessage()
	{
		return noticeMessage;
	}

	public void setNoticeMessage(ArrayList<OBDtoNoticeInfo> noticeMessage)
	{
		this.noticeMessage = noticeMessage;
	}

	@Override
	public String toString()
	{
		return "NoticeAction [accountIndex=" + accountIndex + ", noticeMessage=" + noticeMessage + ", noticeFacade=" + noticeFacade + "]";
	}	
}