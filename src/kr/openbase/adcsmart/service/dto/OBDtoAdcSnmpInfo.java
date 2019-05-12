package kr.openbase.adcsmart.service.dto;

import kr.openbase.adcsmart.service.utility.OBCipherAES;

public class OBDtoAdcSnmpInfo
{
	private int    version;
	
    private String rcomm;        // read community - v1 and v2
    
    private String securityName; // v3
    private String authProto;    // v3
    private String authPassword; // v3
    private String privProto;    // v3
    private String privPassword; // v3

	@Override
	public String toString()
	{
		return "OBDtoAdcSnmpInfo [version=" + version + ", rcomm=" + rcomm
				+ ", securityName=" + securityName + ", authProto=" + authProto
				+ ", authPassword=" + authPassword + ", privProto=" + privProto
				+ ", privPassword=" + privPassword + "]";
	}

	public int getVersion()
	{
		return version;
	}
	public void setVersion(int version)
	{
		this.version = version;
	}
	public String getRcomm()
	{
		return rcomm;
	}
	public void setRcomm(String rcomm)
	{
		this.rcomm = rcomm;
	}
	public String getSecurityName()
	{
		return securityName;
	}
	public void setSecurityName(String securityName)
	{
		this.securityName = securityName;
	}
	public String getAuthProto()
	{
		return authProto;
	}
	public void setAuthProto(String authProto)
	{
		this.authProto = authProto;
	}
	public String getAuthPassword()
	{
		return authPassword;
	}
	public String getAuthPasswordDecrypt()
	{
		return new OBCipherAES().Decrypt(this.authPassword);
	}
	public void setAuthPassword(String authPassword)
	{
		this.authPassword = authPassword;
	}
	public String getPrivProto()
	{
		return privProto;
	}
	public void setPrivProto(String privProto)
	{
		this.privProto = privProto;
	}
	public String getPrivPassword()
	{
		return privPassword;
	}
	public String getPrivPasswordDecrypt()
	{
		return new OBCipherAES().Decrypt(this.privPassword);
	}
	public void setPrivPassword(String privPassword)
	{
		this.privPassword = privPassword;
	}
}
