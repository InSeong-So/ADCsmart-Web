package kr.openbase.adcsmart.web.facade.adcman;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.OBAdcProfile;
import kr.openbase.adcsmart.service.dto.OBDtoAdcProfile;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.impl.f5.OBAdcPersistenceF5;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.web.facade.dto.ProfileAddDto;
import kr.openbase.adcsmart.web.facade.dto.ProfileDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProfileFacade 
{
	private static transient Logger log = LoggerFactory.getLogger(ProfileFacade.class);
	
	private OBAdcProfile profileSvc;
	
	public ProfileFacade() 
	{
		profileSvc = new OBAdcPersistenceF5();
	}
	
	public List<ProfileDto> getProfiles(int adcIndex, String searchKey, Integer orderType, Integer orgerDir) throws Exception 
	{
		log.debug("getProfiles: {}, {}", adcIndex, searchKey);
		List<ProfileDto> profiles = new ArrayList<ProfileDto>(); 
		try 
		{
			profiles.addAll(ProfileDto.toProfileDto(profileSvc.searchProfileList(adcIndex, StringUtils.isEmpty(searchKey) ? null : searchKey, orderType, orgerDir)));
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}		
		
		return profiles;
	}
	public List<ProfileDto> getProfiles(int adcIndex, String searchKey) throws Exception 
	{
		log.debug("getProfiles: {}, {}", adcIndex, searchKey);
		List<ProfileDto> profiles = new ArrayList<ProfileDto>(); 
		try 
		{
			profiles.addAll(ProfileDto.toProfileDto(profileSvc.searchProfileList(adcIndex, StringUtils.isEmpty(searchKey) ? null : searchKey)));
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}		
		
		return profiles;
	}
	
	public ProfileAddDto getProfileAdd(int adcIndex, String index) throws Exception 
	{
		log.debug("{}, {}", adcIndex, index);
		ArrayList<OBDtoAdcProfile> profilesFromSvc = null;
		try 
		{
			profilesFromSvc = profileSvc.getProfileList(adcIndex, index);
			log.debug("{}", profilesFromSvc);
		} 
		catch (OBExceptionUnreachable e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		} 
		catch (OBExceptionLogin e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		} 
		catch (OBException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
		
		return CollectionUtils.isEmpty(profilesFromSvc) ? null : ProfileAddDto.toProfileAddDto(profilesFromSvc.get(0));
	}
	
	public void addProfile(ProfileAddDto profileAdd, SessionDto session) throws Exception 
	{
		try 
		{
			OBDtoAdcProfile profileFromSvc = ProfileAddDto.toOBDtoAdcProfile(profileAdd);
			log.debug("{}", profileFromSvc);
			OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
			extraInfo.setExtraMsg1(profileAdd.getName());
			profileSvc.addProfile(profileAdd.getAdcIndex(), profileFromSvc, extraInfo);
			log.debug("-- added profile.");
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}		
	}
	
	public void modifyProfile(ProfileAddDto profileAdd, SessionDto session) throws Exception 
	{
		try 
		{
			OBDtoAdcProfile profileFromSvc = ProfileAddDto.toOBDtoAdcProfile(profileAdd);
			log.debug("{}", profileFromSvc);
			OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
			extraInfo.setExtraMsg1(profileAdd.getName());
			profileSvc.setProfile(profileAdd.getAdcIndex(), profileFromSvc, extraInfo);
			log.debug("-- modified profile.");			
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	
	public void delProfiles(int adcIndex, List<String> profileIndices, SessionDto session) throws Exception 
	{
		try 
		{
			OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
			extraInfo.setExtraMsg1(profileIndices.toString());
			profileSvc.delProfile(adcIndex, new ArrayList<String>(profileIndices), extraInfo);			
		} 
		catch (OBExceptionUnreachable e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		} 
		catch (OBExceptionLogin e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e);
		}
	}	
}