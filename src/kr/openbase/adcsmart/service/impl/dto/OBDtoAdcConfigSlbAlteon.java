package kr.openbase.adcsmart.service.impl.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcAdditional;
import kr.openbase.adcsmart.service.dto.OBDtoAdcHealthCheckAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoNetworkInterface;
import kr.openbase.adcsmart.service.dto.flb.OBDtoFlbFilterInfo;
import kr.openbase.adcsmart.service.snmp.alteon.dto.DtoSnmpFilterPortAlteon;

public class OBDtoAdcConfigSlbAlteon
{
	private ArrayList<OBDtoAdcVServerAlteon> virtServers;// = new ArrayList<OBAdcVirtualServer>();
	private ArrayList<OBDtoAdcNodeAlteon> realServers;// = new ArrayList<OBAdcRealServer>();
	private ArrayList<OBDtoAdcPoolAlteon> serverPools;//ups = new ArrayList<OBAdcServerPool>();
	private ArrayList<OBDtoNetworkInterface> ifList;
	private ArrayList<OBDtoAdcHealthCheckAlteon> healthcheckList;
	private OBDtoAdcAdditional adcAdditionalInfo;
	private ArrayList<OBDtoFlbFilterInfo> filterList;
	private ArrayList<DtoSnmpFilterPortAlteon> filterPhysicalPortList;

	@Override
	public String toString()
	{
		return "OBDtoAdcConfigSlbAlteon [virtServers=" + virtServers
				+ ", realServers=" + realServers + ", serverPools="
				+ serverPools + ", ifList=" + ifList + ", healthcheckList="
				+ healthcheckList + ", adcAdditionalInfo=" + adcAdditionalInfo
				+ ", filterList=" + filterList + ", filterPhysicalPortList="
				+ filterPhysicalPortList + "]";
	}
	public OBDtoAdcAdditional getAdcAdditionalInfo()
	{
		return adcAdditionalInfo;
	}
	public void setAdcAdditionalInfo(OBDtoAdcAdditional adcAdditionalInfo)
	{
		this.adcAdditionalInfo = adcAdditionalInfo;
	}
	public ArrayList<OBDtoAdcVServerAlteon> getVirtServers()
	{
		return virtServers;
	}
	public void setVirtServers(ArrayList<OBDtoAdcVServerAlteon> virtServers)
	{
		this.virtServers = virtServers;
	}
	public ArrayList<OBDtoAdcNodeAlteon> getRealServers()
	{
		return realServers;
	}
	public void setRealServers(ArrayList<OBDtoAdcNodeAlteon> realServers)
	{
		this.realServers = realServers;
	}
	public ArrayList<OBDtoAdcPoolAlteon> getServerPools()
	{
		return serverPools;
	}
	public void setServerPools(ArrayList<OBDtoAdcPoolAlteon> serverPools)
	{
		this.serverPools = serverPools;
	}
	public ArrayList<OBDtoNetworkInterface> getIfList()
	{
		return ifList;
	}
	public void setIfList(ArrayList<OBDtoNetworkInterface> ifList)
	{
		this.ifList = ifList;
	}
	public ArrayList<OBDtoAdcHealthCheckAlteon> getHealthcheckList()
	{
		return healthcheckList;
	}
	public void setHealthcheckList(
			ArrayList<OBDtoAdcHealthCheckAlteon> healthcheckList)
	{
		this.healthcheckList = healthcheckList;
	}
	public ArrayList<OBDtoFlbFilterInfo> getFilterList()
	{
		return filterList;
	}
	public void setFilterList(ArrayList<OBDtoFlbFilterInfo> filterList)
	{
		this.filterList = filterList;
	}
	public ArrayList<DtoSnmpFilterPortAlteon> getFilterPhysicalPortList()
	{
		return filterPhysicalPortList;
	}
	public void setFilterPhysicalPortList(
			ArrayList<DtoSnmpFilterPortAlteon> filterPhysicalPortList)
	{
		this.filterPhysicalPortList = filterPhysicalPortList;
	}
}