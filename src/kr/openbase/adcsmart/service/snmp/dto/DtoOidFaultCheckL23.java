package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidFaultCheckL23
{
	private String vlanInfoId;
	private String vlanInfoName;
	private String vlanInfoStatus;
	private String vlanInfoJumbo;
	private String vlanInfoBwmContract;
	private String vlanInfoLearn;
	private String vlanInfoPorts;
	private String vlanInterfaceType;
	
	private String sysSelfIpAddr;
	private String sysSelfIpNetmask;
	private String sysSelfIpIsFloatging;
	private String stgCurCfgState;
	private String stgCurCfgVlanBmap;
	private String sysSelfIpVlanName;
	private String vrrpInfoVirtRtrState;
	
	private String arpInfoDestIp;
	private String arpInfoMacAddr;
	private String arpInfoSrcPort;
	private String arpInfoVlanInfo;
	
	@Override
	public String toString()
	{
		return String.format("DtoOidFaultCheckL23 [vlanInfoId=%s, vlanInfoName=%s, vlanInfoStatus=%s, vlanInfoJumbo=%s, vlanInfoBwmContract=%s, vlanInfoLearn=%s, vlanInfoPorts=%s, vlanInterfaceType=%s, sysSelfIpAddr=%s, sysSelfIpNetmask=%s, sysSelfIpIsFloatging=%s, stgCurCfgState=%s, stgCurCfgVlanBmap=%s, sysSelfIpVlanName=%s, vrrpInfoVirtRtrState=%s, arpInfoDestIp=%s, arpInfoMacAddr=%s, arpInfoSrcPort=%s, arpInfoVlanInfo=%s]", vlanInfoId, vlanInfoName, vlanInfoStatus, vlanInfoJumbo, vlanInfoBwmContract, vlanInfoLearn, vlanInfoPorts, vlanInterfaceType, sysSelfIpAddr, sysSelfIpNetmask, sysSelfIpIsFloatging, stgCurCfgState, stgCurCfgVlanBmap, sysSelfIpVlanName, vrrpInfoVirtRtrState, arpInfoDestIp, arpInfoMacAddr, arpInfoSrcPort, arpInfoVlanInfo);
	}
	public String getVlanInterfaceType()
	{
		return vlanInterfaceType;
	}
	public void setVlanInterfaceType(String vlanInterfaceType)
	{
		this.vlanInterfaceType = vlanInterfaceType;
	}
	public String getArpInfoVlanInfo()
	{
		return arpInfoVlanInfo;
	}
	public void setArpInfoVlanInfo(String arpInfoVlanInfo)
	{
		this.arpInfoVlanInfo = arpInfoVlanInfo;
	}
	public String getArpInfoDestIp()
	{
		return arpInfoDestIp;
	}
	public void setArpInfoDestIp(String arpInfoDestIp)
	{
		this.arpInfoDestIp = arpInfoDestIp;
	}
	public String getArpInfoMacAddr()
	{
		return arpInfoMacAddr;
	}
	public void setArpInfoMacAddr(String arpInfoMacAddr)
	{
		this.arpInfoMacAddr = arpInfoMacAddr;
	}
	public String getArpInfoSrcPort()
	{
		return arpInfoSrcPort;
	}
	public void setArpInfoSrcPort(String arpInfoSrcPort)
	{
		this.arpInfoSrcPort = arpInfoSrcPort;
	}
	public String getVrrpInfoVirtRtrState()
	{
		return vrrpInfoVirtRtrState;
	}
	public void setVrrpInfoVirtRtrState(String vrrpInfoVirtRtrState)
	{
		this.vrrpInfoVirtRtrState = vrrpInfoVirtRtrState;
	}
	public String getSysSelfIpVlanName()
	{
		return sysSelfIpVlanName;
	}
	public void setSysSelfIpVlanName(String sysSelfIpVlanName)
	{
		this.sysSelfIpVlanName = sysSelfIpVlanName;
	}
	public String getVlanInfoId()
	{
		return vlanInfoId;
	}
	public void setVlanInfoId(String vlanInfoId)
	{
		this.vlanInfoId = vlanInfoId;
	}
	public String getVlanInfoName()
	{
		return vlanInfoName;
	}
	public void setVlanInfoName(String vlanInfoName)
	{
		this.vlanInfoName = vlanInfoName;
	}
	public String getVlanInfoStatus()
	{
		return vlanInfoStatus;
	}
	public void setVlanInfoStatus(String vlanInfoStatus)
	{
		this.vlanInfoStatus = vlanInfoStatus;
	}
	public String getVlanInfoJumbo()
	{
		return vlanInfoJumbo;
	}
	public void setVlanInfoJumbo(String vlanInfoJumbo)
	{
		this.vlanInfoJumbo = vlanInfoJumbo;
	}
	public String getVlanInfoBwmContract()
	{
		return vlanInfoBwmContract;
	}
	public void setVlanInfoBwmContract(String vlanInfoBwmContract)
	{
		this.vlanInfoBwmContract = vlanInfoBwmContract;
	}
	public String getVlanInfoLearn()
	{
		return vlanInfoLearn;
	}
	public void setVlanInfoLearn(String vlanInfoLearn)
	{
		this.vlanInfoLearn = vlanInfoLearn;
	}
	public String getVlanInfoPorts()
	{
		return vlanInfoPorts;
	}
	public void setVlanInfoPorts(String vlanInfoPorts)
	{
		this.vlanInfoPorts = vlanInfoPorts;
	}
	public String getSysSelfIpAddr()
	{
		return sysSelfIpAddr;
	}
	public void setSysSelfIpAddr(String sysSelfIpAddr)
	{
		this.sysSelfIpAddr = sysSelfIpAddr;
	}
	public String getSysSelfIpNetmask()
	{
		return sysSelfIpNetmask;
	}
	public void setSysSelfIpNetmask(String sysSelfIpNetmask)
	{
		this.sysSelfIpNetmask = sysSelfIpNetmask;
	}
	public String getSysSelfIpIsFloatging()
	{
		return sysSelfIpIsFloatging;
	}
	public void setSysSelfIpIsFloatging(String sysSelfIpIsFloatging)
	{
		this.sysSelfIpIsFloatging = sysSelfIpIsFloatging;
	}
	public String getStgCurCfgState()
	{
		return stgCurCfgState;
	}
	public void setStgCurCfgState(String stgCurCfgState)
	{
		this.stgCurCfgState = stgCurCfgState;
	}
	public String getStgCurCfgVlanBmap()
	{
		return stgCurCfgVlanBmap;
	}
	public void setStgCurCfgVlanBmap(String stgCurCfgVlanBmap)
	{
		this.stgCurCfgVlanBmap = stgCurCfgVlanBmap;
	}
}
