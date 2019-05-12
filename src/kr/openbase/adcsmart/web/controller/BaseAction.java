package kr.openbase.adcsmart.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBUtility;
import kr.openbase.adcsmart.web.facade.CommonFacade;
import kr.openbase.adcsmart.web.facade.LogFacade;
import kr.openbase.adcsmart.web.facade.LoginFacade;
import kr.openbase.adcsmart.web.facade.dto.AccountDto;
import kr.openbase.adcsmart.web.facade.dto.AdcExceptionDto;
import kr.openbase.adcsmart.web.session.AdcSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateHashModel;

@Controller
@Scope(value = "prototype")
public class BaseAction implements Action, Preparable, ServletRequestAware, ServletResponseAware 
{
	@Autowired
	protected CommonFacade commonFacade;
	
	@Autowired
	protected LogFacade logFacade;
	
	@Autowired
    private LoginFacade accountFacade;
	
	protected AdcSession session;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected String message;
	protected boolean isSuccessful=true;
	protected String accountRole;	
	protected long contentLength;
	protected String contentDisposition;
	protected InputStream inputStream;
//	protected String sessionIp;
	
	@Value("#{settings['lang.code']}") 
	private String langCode;// 	
//	protected Boolean isSecurityNoticeShown;	

//	OBException - Service단에서 발생하는 Exception 에러시 오류메세지 처리 
	protected boolean isOBException=false;
	protected AdcExceptionDto adcexception = new AdcExceptionDto();		
	
	private static Map<String, String> LANGCODEMAP;// = LoggerFactory.getLogger(AlertFacade.class);
//	static private Map<String, String> testMap;//=new HashMap<String, String>();
	
	@Value("#{settings['account.isIpChk']}")
    private boolean ipChkFlag=false;
	
	@Override
	public synchronized void prepare() throws Exception 
	{
		try
		{
			session = new AdcSession(request.getSession());
			accountRole = session.getAccountRole();
//			sessionIp = session.getClientIp();
			
//			Log.debug("sessionIp: {}", sessionIp);
//			Log.debug("request.getRemoteAddr(): {}", request.getRemoteAddr());
			if (session.getAccountId() != null)
			{
			    AccountDto account = accountFacade.getAccount(session.getAccountId());
//			    System.out.println("clientIP : " + account.getUserIp());
//			    System.out.println("sessionIP : " + request.getRemoteAddr());
			    ipChkFlag = accountFacade.getIpCheck(account.getIndex(), request.getRemoteAddr());
			}
			if (ipChkFlag)
			{
	            session.setAccountRole("readOnly");
			}
			
			BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
			TemplateHashModel statics = (TemplateHashModel) wrapper.getStaticModels().get("kr.openbase.adcsmart.web.constant.AdcConstants");
			request.setAttribute("AdcConstants", statics);
			if(LANGCODEMAP==null)
			{
				LANGCODEMAP = new ConcurrentHashMap<String, String>();

				String langCode = OBCommon.getLocalInfo();
				String language = OBCommon.getLanguageCode(langCode);
				String country = OBCommon.getCountryCode(langCode);
				String fileName = String.format(OBDefine.PROPERTIES_Freemarker,  language, country);
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(OBUtility.getPropOsPath()+fileName), "UTF8"));
				Properties msgProps = new Properties();
				msgProps.load(in);
				
		        Enumeration<?> keys = msgProps.propertyNames();
		        while (keys.hasMoreElements()) 
		        {
		            String key = (String) keys.nextElement();
		            LANGCODEMAP.put(key, msgProps.getProperty(key));            
		        }
			}
		}
		catch(Exception e)
		{
			throw e;
		}
	}
//	
//	private String getLanguageCode(String langCode)
//	{
//		String elements[] = langCode.split("_");
//		if(elements.length!=2)
//			return "ko";// default language is korean
//		return elements[0];
//	}
//	
//	private String getCountryCode(String langCode)
//	{
//		String elements[] = langCode.split("_");
//		if(elements.length!=2)
//			return "KR";// default language is korean
//		return elements[1];
//	}

	@Override
	public void setServletRequest(HttpServletRequest request) 
	{
		this.request = request;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) 
	{
		this.response = response;
	}

	@Override
	public String execute() throws Exception
	{
		return SUCCESS;
	}
//	
//	public String showsSecurityNotice() throws Exception {
//		isSecurityNoticeShown = commonFacade.getIsSecurityNoticeShown();
//		return SUCCESS;
//	}
	
	protected void setStrutsStream(File file) throws UnsupportedEncodingException, FileNotFoundException 
	{
		contentLength = file.length();
		contentDisposition = "attachment; filename=" + URLEncoder.encode(file.getName(), "UTF-8").replace("+", "%20");
		inputStream = new FileInputStream(file);
	}
	
	public String getMessage() 
	{
		return message;
	}

	public void setMessage(String message) 
	{
		this.message = message;
	}
	
	public boolean getIsSuccessful() 
	{
		return isSuccessful;
	}

	public void setIsSuccessful(boolean isSuccessful) 
	{
		this.isSuccessful = isSuccessful;
	}

	public String getAccountRole() 
	{
		return accountRole;
	}

	public void setAccountRole(String accountRole) 
	{
		this.accountRole = accountRole;
	}

	public long getContentLength() 
	{
		return contentLength;
	}

	public void setContentLength(long contentLength) 
	{
		this.contentLength = contentLength;
	}

	public String getContentDisposition() 
	{
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) 
	{
		this.contentDisposition = contentDisposition;
	}

	public InputStream getInputStream() 
	{
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) 
	{
		this.inputStream = inputStream;
	}

//	public Boolean getIsSecurityNoticeShown() {
//		return isSecurityNoticeShown;
//	}
//
//	public void setIsSecurityNoticeShown(Boolean isSecurityNoticeShown) {
//		this.isSecurityNoticeShown = isSecurityNoticeShown;
//	}

	public boolean getIsOBException()
	{
		return isOBException;
	}

	public void setIsOBException(boolean isOBException)
	{
		this.isOBException = isOBException;
	}
		
	public AdcExceptionDto getAdcexception()
	{
		return adcexception;
	}

	public void setAdcexception(AdcExceptionDto adcexception)
	{
		this.adcexception = adcexception;
	}

	public Map<String, String> getLANGCODEMAP()
	{
		return LANGCODEMAP;
	}

	public void setLANGCODEMAP(Map<String, String> lANGCODEMAP)
	{
		LANGCODEMAP = lANGCODEMAP;
	}

	public String getLangCode()
	{
		return langCode;
	}

	public void setLangCode(String langCode)
	{
		this.langCode = langCode;
	}

//    public String getSessionIp()
//    {
//        return sessionIp;
//    }
//
//    public void setSessionIp(String sessionIp)
//    {
//        this.sessionIp = sessionIp;
//    }

    public boolean isIpChkFlag()
    {
        return ipChkFlag;
    }

    public void setIpChkFlag(boolean ipChkFlag)
    {
        this.ipChkFlag = ipChkFlag;
    }	
}
