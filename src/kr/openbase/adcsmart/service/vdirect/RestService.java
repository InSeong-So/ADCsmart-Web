package kr.openbase.adcsmart.service.vdirect;

import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

import kr.openbase.adcsmart.service.vdirect.dto.CommandDto;
import kr.openbase.adcsmart.service.vdirect.dto.ConfigAlteonDto;
import kr.openbase.adcsmart.service.vdirect.dto.ConfigSlbDto;
import kr.openbase.adcsmart.service.vdirect.dto.ServerEnableDisableDto;

public interface RestService
{
	public CloseableHttpClient init() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException;

	public HttpPost createAlteon(ConfigAlteonDto alteon) throws UnsupportedEncodingException;
	public HttpDelete deleteAlteon(ConfigAlteonDto alteon) throws UnsupportedEncodingException;
	public HttpPost configSnmp(ConfigAlteonDto alteon) throws UnsupportedEncodingException;
	public HttpDelete deleteSnmp(ConfigAlteonDto alteon) throws UnsupportedEncodingException;
	public HttpPost configAccount(ConfigAlteonDto alteon) throws UnsupportedEncodingException;
	public HttpDelete deleteAccount(ConfigAlteonDto alteon) throws UnsupportedEncodingException;
	
	
	public HttpPost setVirtualServerCreate(ConfigSlbDto vs) throws KeyManagementException, UnsupportedEncodingException;
	public HttpPost setVirtualServerDelete(ConfigSlbDto vs) throws KeyManagementException, UnsupportedEncodingException;
	
	public HttpPost setSLBCreate(ConfigSlbDto slb) throws KeyManagementException, UnsupportedEncodingException;

	public HttpPost setRealServerCreate(ConfigSlbDto realServers) throws UnsupportedEncodingException;
	public HttpPost setRealServerDelete(ConfigSlbDto realServers) throws UnsupportedEncodingException;
	public HttpPost setRealServerState(ServerEnableDisableDto realServers) throws UnsupportedEncodingException;
	public HttpPost setRealServerEnable(ServerEnableDisableDto realServers) throws UnsupportedEncodingException;
	public HttpPost setRealServerDisable(ServerEnableDisableDto realServers) throws UnsupportedEncodingException;

	public HttpPost setVirtualServerState(ServerEnableDisableDto virtualServers)
			throws UnsupportedEncodingException;
	public HttpPost setVirtualServerDisable(ServerEnableDisableDto virtualServers)
			throws UnsupportedEncodingException;

	public HttpPost setCreateVirtualService(ConfigSlbDto virtualService) throws UnsupportedEncodingException;
	public HttpPost setDelteVirtualService(ConfigSlbDto virtualService) throws UnsupportedEncodingException;

	public HttpPost setCommandApply(String alteonName) throws UnsupportedEncodingException;
	public HttpPost setCommandRevert(String alteonName) throws UnsupportedEncodingException;

	public HttpPost setGroupCreate(ConfigSlbDto group) throws UnsupportedEncodingException;
	public HttpPost setGroupState(ServerEnableDisableDto realServers) throws UnsupportedEncodingException;
	public HttpPost setGroupDelete(ConfigSlbDto group) throws UnsupportedEncodingException;
	
	public HttpPost setVrrpCreate(ConfigSlbDto group) throws UnsupportedEncodingException;
	public HttpPost setVrrpDelete(ConfigSlbDto group) throws UnsupportedEncodingException;
	
	public HttpPost sendCommand(CommandDto command) throws UnsupportedEncodingException;
}
