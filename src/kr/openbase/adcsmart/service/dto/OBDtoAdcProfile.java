package kr.openbase.adcsmart.service.dto;

public class OBDtoAdcProfile
{
	private String index;
	private Integer adcIndex;
	private String profileName;
	private Integer persistenceType;
	private String parentProfile;
	private Integer matchAcrossServiceYN;
	private Long timeout;

	@Override
	public String toString()
	{
		return "OBDtoAdcProfile [index=" + index + ", adcIndex=" + adcIndex
				+ ", profileName=" + profileName + ", persistenceType="
				+ persistenceType + ", parentProfile=" + parentProfile
				+ ", matchAcrossServiceYN=" + matchAcrossServiceYN
				+ ", timeout=" + timeout + "]";
	}
	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
	{
		this.index = index;
	}
	public Integer getAdcIndex()
	{
		return this.adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	
	public String getProfileName()
	{
		return profileName;
	}
	public void setProfileName(String profileName)
	{
		this.profileName = profileName;
	}
	public Integer getPersistenceType()
	{
		return persistenceType;
	}
	public void setPersistenceType(Integer persistenceType)
	{
		this.persistenceType = persistenceType;
	}
	public String getParentProfile()
	{
		return parentProfile;
	}
	public void setParentProfile(String parentProfile)
	{
		this.parentProfile = parentProfile;
	}
	public Integer getMatchAcrossServiceYN()
	{
		return matchAcrossServiceYN;
	}
	public void setMatchAcrossServiceYN(Integer matchAcrossServiceYN)
	{
		this.matchAcrossServiceYN = matchAcrossServiceYN;
	}
	public Long getTimeout()
	{
		return timeout;
	}
	public void setTimeout(Long timeout)
	{
		this.timeout = timeout;
	}
}