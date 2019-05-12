package kr.openbase.adcsmart.web.controller.system;

import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import kr.openbase.adcsmart.service.dto.OBDtoSyncSystemTime;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dto.AdcLogFilterDto;
import kr.openbase.adcsmart.web.facade.dto.LicenseInfoDto;
import kr.openbase.adcsmart.web.facade.dto.SystemEnvAdditionalDto;
import kr.openbase.adcsmart.web.facade.dto.SystemEnvNetworkDto;
import kr.openbase.adcsmart.web.facade.dto.SystemEnvViewDto;
import kr.openbase.adcsmart.web.facade.system.ConfigSettingFacade;
import kr.openbase.adcsmart.web.util.OBDateTimeWeb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


@Controller
@Scope(value = "prototype")
public class ConfigSettingAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(ConfigSettingAction.class);
	
	@Autowired
	private ConfigSettingFacade configSettingFacade;
	
	private List<Integer> accountIndices;
	private String searchKey;
	//환경설정 - 기본설정
	private List<SystemEnvNetworkDto> envNetwork;
	private SystemEnvNetworkDto enetwork;

	//환경설정 - 부가기능 설정
	private List<SystemEnvAdditionalDto> envAdditional; 
	private SystemEnvAdditionalDto eadditional;
	//환경설정 - 화면표시 설정
	private List<SystemEnvViewDto> envView; 
	private SystemEnvViewDto eview;
	//환경설정 - Adc로그필터링
	private List<AdcLogFilterDto> logFilter;													//AdcLogFilter load용
	private AdcLogFilterDto lfilter;	
	// 환경설정 - 시스템 시간 동기화
	private OBDtoSyncSystemTime syncSystemTimeInfo;
	private Date systemTime;	
	private Long manuallyTimeL;
	
	private static ArrayList<AdcLogFilterDto> orgLogFilter = new ArrayList<AdcLogFilterDto>();	//AdcLogFilter modify용	
	private List<String> filterUserPattern;
	private List<Integer> filterType;	
	private List<String> filterRegPattern;
	private List<Integer> filterIndex;		
	//라이선스
	private LicenseInfoDto licInfo;
	private File upload; //업로드할 실제파일
	private String uploadFileName; //업로드할 로컬 파일명
	private String serverFullPath; //저장활 실제 파일의 전체경로
//	private HttpServletRequest servletRequest;	
	//환경설정 - 경보 설정
//	private List<SystemEnvAlertDto> envAlert;
//	private SystemEnvAlertDto ealert;	
	
	public String loadLeftPane() throws  OBException
	{
		return SUCCESS;
	}
	
	public String loadSysMgmtContent() throws OBException
	{
		return SUCCESS;
	}
	
	public String loadBackupAndRestoreContent() throws OBException 
	{
		return SUCCESS;
	}
	
	public String loadBackupAddContent() throws OBException 
	{
		return SUCCESS;
	}
	
	public String loadSystemInfoContent() throws OBException 
	{
		return SUCCESS;
	}
	
	public String loadLicenseContent() throws OBException 
	{
		try 
		{
			licInfo = configSettingFacade.getLicenseGeneralInfo();				//\opt\adcsmart\cfg\license.lic
			log.debug("{}", licInfo);
		}
		catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
		
		return SUCCESS;
	}	
	
//	public String uploadFileContent() throws Exception {
	public String uploadFileContent() throws OBException
	{		
//		String basePath = getText("path.upload_file");		
//		String basePath = "/opt/adcsmart/temp";
//		String basePath = "/usr/local/apache-tomcat-6.0.35/webapps/temp";
		String basePath = "/tmp";
//		String basePath = servletRequest.getSession().getServletContext().getRealPath("/");
		
//		ServletContext servletContext = ServletActionContext.getServletContext(); 
//      String basePath =servletContext.getRealPath("/"); 

		log.debug("{}", basePath);
		log.debug("{}", upload);
		log.debug("{}", uploadFileName);
		
		isSuccessful = true;
//		log.debug("{}",isSuccessful);
//		flag = "true";
		
		try 
		{			
			serverFullPath = configSettingFacade.saveFile(upload, basePath, uploadFileName);
			configSettingFacade.validLicenseFormat(serverFullPath);
			configSettingFacade.updateLicenseConfig(serverFullPath, session.getSessionDto());						
		}
		catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
		
		return SUCCESS;
	}
	
	//환경설정
	public String loadConfigContent() throws OBException 
	{
		try 
		{
			//기본설정
			envNetwork = configSettingFacade.getNetworkConfigList();
			log.debug("{}", envNetwork);
			//부가기능 설정
			envAdditional = configSettingFacade.getAdditionalConfigList();
			log.debug("{}", envAdditional);
			//화면 표시 설정
			envView = configSettingFacade.getViewConfigList();
			log.debug("{}", envView);
			//경보 설정
//			envAlert = configSettingFacade.getAlertConfigList();
//			log.debug("{}", envAlert);
			// 시스템 시간 Sync config			
			syncSystemTimeInfo = configSettingFacade.getSyncSystemTimeConfig();
			log.debug("{}", syncSystemTimeInfo);
			systemTime = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.now()));
	
			//Adc로그필터링
			logFilter = configSettingFacade.getAdcLogFilterList();
			log.debug("{}", logFilter);

			orgLogFilter.clear();
			orgLogFilter.addAll(logFilter);
			
//			log.debug("{}", orgLogFilter);												
//			log.debug("{}", orgLogFilter.size());
		
		}  
		catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
		
		return SUCCESS;
	}	
	
	public String modifyConfigContent() throws OBException
	{
		log.debug("{}", enetwork);
		log.debug("{}", eview);
		log.debug("{}", eadditional);
		
		log.debug("{}", orgLogFilter);
		log.debug("{}", manuallyTimeL);
		log.debug("{}", syncSystemTimeInfo);
		if(manuallyTimeL != null)
		{
		    Timestamp  inputTime= new Timestamp(manuallyTimeL);
	        syncSystemTimeInfo.setManuallyTime(inputTime); 
		}		       
		log.debug("{}", syncSystemTimeInfo);
//		log.debug("{}", logFilter);
//		log.debug("{}", orgLogFilter.size());
		
//		log.debug("{}", filterUserPattern);	//userPattern list
//		log.debug("{}", filterType);		//type list

//		log.debug("{}", testString);		
		isSuccessful = true;		
		//List<AdcLogFilterDto> logFilter
		
		ArrayList<AdcLogFilterDto> addFilter = new ArrayList<AdcLogFilterDto>();
		ArrayList<Integer> delFilter = new ArrayList<Integer>();
		
		AdcLogFilterDto data = null;
//		Integer index = 0;
		
		boolean exist = false;
		//addFilter 새로 추가한 리스트를 기존의 리스트와 비교하여 동일한 경우 pass, 기존의 리스트에 없는 것은 add
		
		//기존데이터가 null인경우
		if (filterUserPattern == null) 
		{
			for (int i=0; i<orgLogFilter.size(); i++) 
			{			
				AdcLogFilterDto delAll = orgLogFilter.get(i);				
				delFilter.add(delAll.getIndex());						
			}	
			
			log.debug("{}", delFilter);
		} 		
		else 
		{
		//null이 아닌 경우
			for(int i=0; i<filterUserPattern.size(); i++) 
			{
				String pattern = filterUserPattern.get(i);
				Integer type = filterType.get(i);
				
				exist = false;
				for(int j=0; j<orgLogFilter.size(); j++) 
				{
					AdcLogFilterDto org = orgLogFilter.get(j);
					if (pattern.equals(org.getUserPattern())) 
					{
						exist = true;	// 기존 가져온 데이터 리스트와 동일한 경우
						break;
					}
				}			
				if (!exist) 
				{						//add
					// 기존 가져온 DB에 없는 경우 add
					data = new AdcLogFilterDto();
					data.setUserPattern(pattern);
					data.setType(type);
					addFilter.add(data);
				}
			}				
//			log.debug("{}", addFilter);
				
			//delFilter	
			for (int i=0; i<orgLogFilter.size(); i++) 
			{
				AdcLogFilterDto org = orgLogFilter.get(i);
				
				exist = false;			
				for (int j=0; j<filterUserPattern.size(); j++) 
				{				
					String pattern = filterUserPattern.get(j);
	//				Integer type = filterType.get(j);
	//				index = filterIndex.get(j);				
					if (org.getUserPattern().equals(pattern))
					{
						exist = true;
						break;
					}				
				}			
				if (!exist) 
				{
					delFilter.add(org.getIndex());
				}
			}
			
//			log.debug("{}", delFilter);
		}	
		/*
		 * add 는 추가한 리스트 filterUserPattern.size()를 가져온 리스트logFilter.size() 와 비교
		 * pattern, type의 값이 org와 동일하면 add pass, 같지 않으면 add 함  
		 * 
		 * del 은 삭제한 리스트 값이 있으면 del 함.
		 * 삭제한 List Count - filterUserPattern.size() 가  가져온  List Count - logFilter.size() 보다 작은 경우
		 */
		
		try 
		{
			configSettingFacade.modifyConfigContent(enetwork, session.getSessionDto());		//기본설정
			configSettingFacade.modifyConfigContent(eview, session.getSessionDto());		//부가기능설정
			configSettingFacade.modifyConfigContent(eadditional, session.getSessionDto());	//화면표시설정
			configSettingFacade.modifyConfigContent(addFilter, delFilter, session.getSessionDto());		//ADC 로그 필터링
			configSettingFacade.setSyncSystemTimeConfig(syncSystemTimeInfo, session.getSessionDto());   // ADCsmart System Time Sync 설정
			 if(configSettingFacade.setSyncSystemTimeConfig(syncSystemTimeInfo, session.getSessionDto()))  // ADCsmart System Time Sync 설정
	         { 
	             isSuccessful = true;
	         }
	         else
	         {
	             isSuccessful = false;
	         }
		
		
		} 
		catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
		
		return SUCCESS;
	}
	// 시스템 시간 동기화 단일 동작 메서드
	public String modifySyncSystemTimeConfig() throws OBException
	{
	    isSuccessful = true;
	    try 
        {
	        if(manuallyTimeL != null)
	        {
	            Timestamp  inputTime= new Timestamp(manuallyTimeL);
	            syncSystemTimeInfo.setManuallyTime(inputTime); 
	        }  
	        log.debug("{}", syncSystemTimeInfo);
	        if(configSettingFacade.setSyncSystemTimeConfig(syncSystemTimeInfo, session.getSessionDto()))  // ADCsmart System Time Sync 설정
	        { 
	            isSuccessful = true;
	        }
	        else
	        {
	            isSuccessful = false;
	        }
        } 
        catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }        
        return SUCCESS;
    }
	
	
	public SystemEnvAdditionalDto getEadditional()
	{
		return eadditional;
	}

	public void setEadditional(SystemEnvAdditionalDto eadditional)
	{
		this.eadditional = eadditional;
	}

	public List<SystemEnvAdditionalDto> getEnvAdditional()
	{
		return envAdditional;
	}

	public void setEnvAdditional(List<SystemEnvAdditionalDto> envAdditional)
	{
		this.envAdditional = envAdditional;
	}

	public List<SystemEnvViewDto> getEnvView()
	{
		return envView;
	}

	public void setEnvView(List<SystemEnvViewDto> envView)
	{
		this.envView = envView;
	}

	public List<SystemEnvNetworkDto> getEnvNetwork()
	{
		return envNetwork;
	}

	public void setEnvNetwork(List<SystemEnvNetworkDto> envNetwork)
	{
		this.envNetwork = envNetwork;
	}

	public List<Integer> getAccountIndices() {
		return accountIndices;
	}

	public void setAccountIndices(List<Integer> accountIndices) {
		this.accountIndices = accountIndices;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public SystemEnvViewDto getEview()
	{
		return eview;
	}

	public void setEview(SystemEnvViewDto eview)
	{
		this.eview = eview;
	}

	public SystemEnvNetworkDto getEnetwork()
	{
		return enetwork;
	}

	public void setEnetwork(SystemEnvNetworkDto enetwork)
	{
		this.enetwork = enetwork;
	}

	public LicenseInfoDto getLicInfo()
	{
		return licInfo;
	}

	public void setLicInfo(LicenseInfoDto licInfo)
	{
		this.licInfo = licInfo;
	}

	public File getUpload()
	{
		return upload;
	}

	public void setUpload(File upload)
	{
		this.upload = upload;
	}

	public String getUploadFileName()
	{
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName)
	{
		this.uploadFileName = uploadFileName;
	}

	public String getServerFullPath()
	{
		return serverFullPath;
	}

	public void setServerFullPath(String serverFullPath)
	{
		this.serverFullPath = serverFullPath;
	}

	public List<AdcLogFilterDto> getLogFilter()
	{
		return logFilter;
	}

	public void setLogFilter(List<AdcLogFilterDto> logFilter)
	{
		this.logFilter = logFilter;
	}

	public AdcLogFilterDto getLfilter()
	{
		return lfilter;
	}

	public void setLfilter(AdcLogFilterDto lfilter)
	{
		this.lfilter = lfilter;
	}

	public List<String> getFilterUserPattern()
	{
		return filterUserPattern;
	}

	public void setFilterUserPattern(List<String> filterUserPattern)
	{
		this.filterUserPattern = filterUserPattern;
	}

	public List<Integer> getFilterType()
	{
		return filterType;
	}

	public void setFilterType(List<Integer> filterType)
	{
		this.filterType = filterType;
	}

	public List<String> getFilterRegPattern()
	{
		return filterRegPattern;
	}

	public void setFilterRegPattern(List<String> filterRegPattern)
	{
		this.filterRegPattern = filterRegPattern;
	}

	public List<Integer> getFilterIndex()
	{
		return filterIndex;
	}

	public void setFilterIndex(List<Integer> filterIndex)
	{
		this.filterIndex = filterIndex;
	}

    public OBDtoSyncSystemTime getSyncSystemTimeInfo()
    {
        return syncSystemTimeInfo;
    }

    public void setSyncSystemTimeInfo(OBDtoSyncSystemTime syncSystemTimeInfo)
    {
        this.syncSystemTimeInfo = syncSystemTimeInfo;
    }

    public Date getSystemTime()
    {
        return systemTime;
    }

    public void setSystemTime(Date systemTime)
    {
        this.systemTime = systemTime;
    }

    public Long getManuallyTimeL()
    {
        return manuallyTimeL;
    }

    public void setManuallyTimeL(Long manuallyTimeL)
    {
        this.manuallyTimeL = manuallyTimeL;
    }    
    
/*	public List<SystemEnvAlertDto> getEnvAlert()
	{
		return envAlert;
	}

	public void setEnvAlert(List<SystemEnvAlertDto> envAlert)
	{
		this.envAlert = envAlert;
	}

	public SystemEnvAlertDto getEalert()
	{
		return ealert;
	}

	public void setEalert(SystemEnvAlertDto ealert)
	{
		this.ealert = ealert;
	}	*/
}
