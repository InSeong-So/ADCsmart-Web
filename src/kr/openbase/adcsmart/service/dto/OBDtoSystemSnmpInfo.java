package kr.openbase.adcsmart.service.dto;

public class OBDtoSystemSnmpInfo
{
    private String community;
    private String accessType;
    private String userId;
    private String authPassword;
    private String algorithm;
    private String privPassword; 
    
    @Override
	public String toString()
	{
		return "OBDtoSystemSnmpInfo [community=" + community + ", accessType=" + accessType + ", userId=" + userId + ", authPassword=" + authPassword + ", algorithm=" + algorithm + ", privPassword=" + privPassword + "]";
	}
    
    public String getCommunity()
    {
        return community;
    }
    public void setCommunity(String community)
    {
        this.community = community;
    }
    public String getAccessType()
    {
        return accessType;
    }
    public void setAccessType(String accessType)
    {
        this.accessType = accessType;
    }

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getAuthPassword()
	{
		return authPassword;
	}

	public void setAuthPassword(String authPassword)
	{
		this.authPassword = authPassword;
	}

	public String getAlgorithm()
	{
		return algorithm;
	}

	public void setAlgorithm(String algorithm)
	{
		this.algorithm = algorithm;
	}

	public String getPrivPassword()
	{
		return privPassword;
	}

	public void setPrivPassword(String privPassword)
	{
		this.privPassword = privPassword;
	}

}
