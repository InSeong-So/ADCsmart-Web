package kr.openbase.adcsmart.web.controller.adcman;

import java.util.List;

import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.adcman.ProfileFacade;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.ProfileAddDto;
import kr.openbase.adcsmart.web.facade.dto.ProfileDto;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope(value = "prototype")
public class ProfileAction extends BaseAction 
{
	private transient Logger log = LoggerFactory.getLogger(ProfileAction.class);
	
	@Autowired
	private ProfileFacade profileFacade;
	
	private AdcDto adc;
	private List<ProfileDto> profiles;
	private ProfileAddDto profileAdd;
	private List<String> profileIndices;		// for delete
	private String searchKey;
	private Integer orderDir; // 오른차순 = 1, 내림차순 = 2
	private Integer orderType; // occurTime = 11 , vsName = 1 , vsIpaddress =2, content=3
	
	public String loadProfileListContent() throws Exception 
	{
		log.debug("{}, {}", adc, searchKey);
		try 
		{
			profiles = profileFacade.getProfiles(adc.getIndex(), searchKey, orderType, orderDir);
			log.debug("{}", profiles);
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		return SUCCESS;
	}
	
	public String loadProfileAddContent() 
	{
		log.debug("{}", profileAdd);
		return SUCCESS;
	}
	
	public String loadProfileModifyContent() throws Exception 
	{
		log.debug("{}", profileAdd);
		try 
		{
			profileAdd = profileFacade.getProfileAdd(profileAdd.getAdcIndex(), profileAdd.getIndex());
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		return SUCCESS;
	}
	
	public String addProfile() throws Exception
	{
		isSuccessful = true;
		try 
		{
			log.debug("{}", profileAdd);
			profileFacade.addProfile(profileAdd, session.getSessionDto());
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			isSuccessful = false;
//			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_PROFILE_ADD);
			throw e; // #3984-6 #8: 14.07.30 sw.jung SLB 프로필 추가시 중복이름 검사를 위해 throw
		}
		
		return SUCCESS;
	}
	
	public String modifyProfile() throws Exception
	{
		isSuccessful = true;
		try 
		{
			log.debug("{}", profileAdd);
			profileFacade.modifyProfile(profileAdd, session.getSessionDto());
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			isSuccessful = false;
//			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_PROFILE_MODIFY);
			throw e; // #3984-6 #9: 14.07.30 sw.jung 작업 진행중 장비에서 설정 변경사항 있을 시 에러메세지 처리
		}
		
		return SUCCESS;
	}
	
	public String delProfiles() throws Exception  
	{
		isSuccessful = true;
		try 
		{
			log.debug("{}", profileIndices);
			profileFacade.delProfiles(adc.getIndex(), profileIndices, session.getSessionDto());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			isSuccessful = false;
//			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_PROFILE_DEL);
			throw e; // #3984-1 #2 사용중 프로필 검출을 위해 에러메세지 throw
		}
		
		return SUCCESS;
	}
	
	public String retrieveProfileAdd() 
	{
		isSuccessful = true;
		try 
		{
			log.debug("{}, {}", adc, profileAdd);
			profileAdd = profileFacade.getProfileAdd(adc.getIndex(), profileAdd.getIndex());
			log.debug("{}", profileAdd);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			isSuccessful = false;
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_PROFILE_RETRIEVE);
		}
		
		return SUCCESS;
	}

	public AdcDto getAdc() 
	{
		return adc;
	}

	public void setAdc(AdcDto adc) 
	{
		this.adc = adc;
	}

	public List<ProfileDto> getProfiles() 
	{
		return profiles;
	}

	public void setProfiles(List<ProfileDto> profiles) 
	{
		this.profiles = profiles;
	}

	public ProfileAddDto getProfileAdd() 
	{
		return profileAdd;
	}

	public void setProfileAdd(ProfileAddDto profileAdd) 
	{
		this.profileAdd = profileAdd;
	}

	public List<String> getProfileIndices() 
	{
		return profileIndices;
	}

	public void setProfileIndices(List<String> profileIndices) 
	{
		this.profileIndices = profileIndices;
	}

	public String getSearchKey() 
	{
		return searchKey;
	}

	public void setSearchKey(String searchKey) 
	{
		this.searchKey = searchKey;
	}

	public Integer getOrderDir()
	{
		return orderDir;
	}

	public void setOrderDir(Integer orderDir)
	{
		this.orderDir = orderDir;
	}

	public Integer getOrderType()
	{
		return orderType;
	}

	public void setOrderType(Integer orderType)
	{
		this.orderType = orderType;
	}	
}
