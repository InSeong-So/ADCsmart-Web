package kr.openbase.adcsmart.service.impl.dto;

import java.util.ArrayList;
import java.util.Arrays;

import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcProfile;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSslCertificate;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;

public class OBDtoAdcConfigSlbF5
{
	private ArrayList<OBDtoAdcVServerF5> virtServers;// = new ArrayList<OBAdcVirtualServer>();
	private ArrayList<OBDtoAdcVlan> vlanList;
	private ArrayList<OBDtoAdcNodeF5> realServers;// = new ArrayList<OBAdcRealServer>();
	private ArrayList<OBDtoAdcPoolF5> serverPools;//ups = new ArrayList<OBAdcServerPool>();
	private ArrayList<OBDtoAdcPoolMemberF5> poolMembers;
	private ArrayList<OBDtoAdcProfile> persistenceProfiles;
	private ArrayList<OBDtoAdcSslCertificate> sslCertificates; //2013.1.22 새로 들어감
	private String [] peerAddress;
	
	@Override
	public String toString()
	{
		return "OBDtoAdcConfigSlbF5 [virtServers=" + virtServers + ", vlanList=" + vlanList + ", realServers=" + realServers + ", serverPools=" + serverPools + ", poolMembers=" + poolMembers + ", persistenceProfiles=" + persistenceProfiles + ", sslCertificates=" + sslCertificates + ", peerAddress=" + Arrays.toString(peerAddress) + "]";
	}
	public ArrayList<OBDtoAdcVServerF5> getVirtServers()
	{
		return virtServers;
	}
	public void setVirtServers(ArrayList<OBDtoAdcVServerF5> virtServers)
	{
		this.virtServers = virtServers;
	}
	public ArrayList<OBDtoAdcNodeF5> getRealServers()
	{
		return realServers;
	}
	public void setRealServers(ArrayList<OBDtoAdcNodeF5> realServers)
	{
		this.realServers = realServers;
	}
	public ArrayList<OBDtoAdcPoolF5> getServerPools()
	{
		return serverPools;
	}
	public void setServerPools(ArrayList<OBDtoAdcPoolF5> serverPools)
	{
		this.serverPools = serverPools;
	}
	public ArrayList<OBDtoAdcPoolMemberF5> getPoolMembers()
	{
		return poolMembers;
	}
	public void setPoolMembers(ArrayList<OBDtoAdcPoolMemberF5> poolMembers)
	{
		this.poolMembers = poolMembers;
	}
	public ArrayList<OBDtoAdcProfile> getPersistenceProfiles()
	{
		return persistenceProfiles;
	}
	public void setPersistenceProfiles(
			ArrayList<OBDtoAdcProfile> persistenceProfiles)
	{
		this.persistenceProfiles = persistenceProfiles;
	}
	public ArrayList<OBDtoAdcSslCertificate> getSslCertificates()
	{
		return sslCertificates;
	}
	public void setSslCertificates(ArrayList<OBDtoAdcSslCertificate> sslCertificates)
	{
		this.sslCertificates = sslCertificates;
	}
	public String[] getPeerAddress()
	{
		return peerAddress;
	}
	public void setPeerAddress(String[] peerAddress)
	{
		this.peerAddress = peerAddress;
	}
	public ArrayList<OBDtoAdcVlan> getVlanList()
	{
		return vlanList;
	}
	public void setVlanList(ArrayList<OBDtoAdcVlan> vlanList)
	{
		this.vlanList = vlanList;
	}
}