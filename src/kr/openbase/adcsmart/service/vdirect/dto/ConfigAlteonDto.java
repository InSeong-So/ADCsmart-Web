package kr.openbase.adcsmart.service.vdirect.dto;

public class ConfigAlteonDto
{
	private String host;
	private String name;
	private String type;
	private String port;
	private String userName;
	private String password;
	private String connectionType;
	private String snmpVersion;
	private String community;
	private String privacyType;
	private String authType;
	private String snmpV3UserName;
	private String snmpV3AuthenticationPassword;
	private String snmpV3AuthenticationProtocol;
	private String snmpV3PrivacyProtocol;
	private String snmpV3PrivacyPassword;
	private String description;

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getSnmpVersion()
	{
		return snmpVersion;
	}

	public void setSnmpVersion(String snmpVersion)
	{
		this.snmpVersion = snmpVersion;
	}

	public String getCommunity()
	{
		return community;
	}

	public void setCommunity(String community)
	{
		this.community = community;
	}

	public String getPrivacyType()
	{
		return privacyType;
	}

	public void setPrivacyType(String privacyType)
	{
		this.privacyType = privacyType;
	}

	public String getAuthType()
	{
		return authType;
	}

	public void setAuthType(String authType)
	{
		this.authType = authType;
	}

	public String getConnectionType()
	{
		return connectionType;
	}

	public void setConnectionType(String connectionType)
	{
		this.connectionType = connectionType;
	}

	public String getSnmpV3UserName()
	{
		return snmpV3UserName;
	}

	public void setSnmpV3UserName(String snmpV3UserName)
	{
		this.snmpV3UserName = snmpV3UserName;
	}

	public String getSnmpV3AuthenticationPassword()
	{
		return snmpV3AuthenticationPassword;
	}

	public void setSnmpV3AuthenticationPassword(String snmpV3AuthenticationPassword)
	{
		this.snmpV3AuthenticationPassword = snmpV3AuthenticationPassword;
	}

	public String getSnmpV3AuthenticationProtocol()
	{
		return snmpV3AuthenticationProtocol;
	}

	public void setSnmpV3AuthenticationProtocol(String snmpV3AuthenticationProtocol)
	{
		this.snmpV3AuthenticationProtocol = snmpV3AuthenticationProtocol;
	}

	public String getSnmpV3PrivacyProtocol()
	{
		return snmpV3PrivacyProtocol;
	}

	public void setSnmpV3PrivacyProtocol(String snmpV3PrivacyProtocol)
	{
		this.snmpV3PrivacyProtocol = snmpV3PrivacyProtocol;
	}

	public String getSnmpV3PrivacyPassword()
	{
		return snmpV3PrivacyPassword;
	}

	public void setSnmpV3PrivacyPassword(String snmpV3PrivacyPassword)
	{
		this.snmpV3PrivacyPassword = snmpV3PrivacyPassword;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getPort()
	{
		return port;
	}

	public void setPort(String port)
	{
		this.port = port;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

}
