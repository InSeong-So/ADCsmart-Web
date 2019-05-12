package kr.openbase.adcsmart.service.vdirect;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.net.util.Base64;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import com.google.gson.Gson;

import kr.openbase.adcsmart.service.vdirect.dto.CommandDto;
import kr.openbase.adcsmart.service.vdirect.dto.ConfigAlteonDto;
import kr.openbase.adcsmart.service.vdirect.dto.ConfigSlbDto;
import kr.openbase.adcsmart.service.vdirect.dto.RunAlteonCreateTemplateDto;
import kr.openbase.adcsmart.service.vdirect.dto.RunSLBTemplateDto;
import kr.openbase.adcsmart.service.vdirect.dto.RunSnmpCliTemplateDto;
import kr.openbase.adcsmart.service.vdirect.dto.ServerEnableDisableDto;

public class RestRequest implements RestService
{

	@Override
	public CloseableHttpClient init() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException
	{

		return httpsOpen();
	}

	private CloseableHttpClient httpsOpen() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException
	{
		javax.net.ssl.SSLContext sslContext = new SSLContextBuilder()
				.loadTrustMaterial(null, (certificate, authType) -> true).build();
		CloseableHttpClient client = HttpClients.custom().setSSLContext(sslContext)
				.setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

		return client;
	}
	
	@Override
	public HttpPost createAlteon(ConfigAlteonDto group) throws UnsupportedEncodingException
	{
		byte[] credentials = Base64.encodeBase64(("tcpdump01" + ":" + "tcpdump12#$").getBytes(StandardCharsets.UTF_8));
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/container");
		StringEntity params = new StringEntity(addDevice(group));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type", "application/vnd.com.radware.vdirect.container+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String addDevice(ConfigAlteonDto alteon)
	{
		RunAlteonCreateTemplateDto adcCreate = new RunAlteonCreateTemplateDto();
		HashMap<String, Object> configuration = createDevice(alteon);

		adcCreate.setConfiguration(configuration);
		adcCreate.setName(alteon.getName());
		adcCreate.setType(alteon.getType());

		Gson gson = new Gson();
		String trans = gson.toJson(adcCreate);
		return trans;
	}

	private HashMap<String, Object> createDevice(ConfigAlteonDto alteon)
	{
		HashMap<String, Object> configuration = new HashMap<String, Object>();
		configuration.put("host", alteon.getHost());
		if (alteon.getConnectionType().equals("SSH"))
		{
			configuration.put("cli.ssh", "true");
			configuration.put("cli.port", alteon.getPort());
		}
		else
		{
			configuration.put("cli.ssh", "false");
			configuration.put("cli.port", alteon.getPort());
		}

		if (alteon.getSnmpVersion().equals("VersionThree"))
		{
			configuration.put("snmp.version", "VersionThree");
			configuration.put("snmp.v3.privacy.type", alteon.getPrivacyType());
			configuration.put("snmp.v3.auth.type", alteon.getAuthType());
		}
		else
		{
			configuration.put("snmp.version", "VersionTwo");
		}
		return configuration;
	}
	
	@Override
	public HttpPost configSnmp(ConfigAlteonDto snmp) throws UnsupportedEncodingException
	{
		byte[] credentials = Base64.encodeBase64(("tcpdump01" + ":" + "tcpdump12#$").getBytes(StandardCharsets.UTF_8));
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/credentials");
		StringEntity params = new StringEntity(addSnmp(snmp));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type", "application/vnd.com.radware.vdirect.credentials+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String addSnmp(ConfigAlteonDto snmp)
	{
		RunSnmpCliTemplateDto snmpCreate = new RunSnmpCliTemplateDto();
		HashMap<String, String> credentials = configSnmpCommunity(snmp);

		snmpCreate.setService("alteon");
		snmpCreate.setProtocol("SNMP");
		snmpCreate.setHost(snmp.getHost());
		snmpCreate.setDescription(snmp.getHost());
		snmpCreate.setCredentials(credentials);

		Gson gson = new Gson();
		String trans = gson.toJson(snmpCreate);
		return trans;
	}

	private HashMap<String, String> configSnmpCommunity(ConfigAlteonDto alteon)
	{
		HashMap<String, String> credentials = new HashMap<String, String>();
		if (alteon.getSnmpVersion().equals("VersionThree"))
		{
			credentials.put("type", "snmpV3");
			credentials.put("userName", alteon.getSnmpV3UserName());
			credentials.put("authenicationProtocol", alteon.getSnmpV3AuthenticationProtocol());
			credentials.put("authenticationPassword", alteon.getSnmpV3AuthenticationPassword());
			credentials.put("privacyProtocol", alteon.getSnmpV3PrivacyProtocol());
			credentials.put("privacyPassword", alteon.getSnmpV3PrivacyPassword());

		}
		else
		{
			credentials.put("type", "communityString");
			credentials.put("value", alteon.getCommunity());
		}

		return credentials;
	}
	
	@Override
	public HttpPost configAccount(ConfigAlteonDto cli) throws UnsupportedEncodingException
	{
		byte[] credentials = Base64.encodeBase64(("tcpdump01" + ":" + "tcpdump12#$").getBytes(StandardCharsets.UTF_8));
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/credentials");
		StringEntity params = new StringEntity(addCli(cli));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type", "application/vnd.com.radware.vdirect.credentials+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String addCli(ConfigAlteonDto cli)
	{
		RunSnmpCliTemplateDto snmpCreate = new RunSnmpCliTemplateDto();
		HashMap<String, String> credentials = configCliAccount(cli);

		snmpCreate.setService("alteon");
		snmpCreate.setProtocol(cli.getConnectionType());
		snmpCreate.setHost(cli.getHost());
		snmpCreate.setDescription(cli.getHost());
		snmpCreate.setCredentials(credentials);

		Gson gson = new Gson();
		String trans = gson.toJson(snmpCreate);
		return trans;
	}

	private HashMap<String, String> configCliAccount(ConfigAlteonDto alteon)
	{
		HashMap<String, String> credentials = new HashMap<String, String>();
		credentials.put("type", "userNamePassword");
		credentials.put("userName", alteon.getUserName());
		credentials.put("password", alteon.getPassword());

		return credentials;
	}
	
	@Override
	public HttpDelete deleteAlteon(ConfigAlteonDto alteon) throws UnsupportedEncodingException
	{
		byte[] credentials = Base64.encodeBase64(("tcpdump01" + ":" + "tcpdump12#$").getBytes(StandardCharsets.UTF_8));
		HttpDelete httpPost = new HttpDelete("https://localhost:2189/api/container/" + alteon.getName());
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type", "application/vnd.com.radware.vdirect.credentials+json; charset=utf8");

		return httpPost;
	}

	@Override
	public HttpDelete deleteSnmp(ConfigAlteonDto snmp) throws UnsupportedEncodingException
	{
		byte[] credentials = Base64.encodeBase64(("tcpdump01" + ":" + "tcpdump12#$").getBytes(StandardCharsets.UTF_8));
		HttpDelete httpPost = new HttpDelete(
				"https://localhost:2189/api/credentials;service=alteon;protocol=SNMP;host=" + snmp.getName());
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type", "application/vnd.com.radware.vdirect.credentials+json; charset=utf8");

		return httpPost;
	}

	@Override
	public HttpDelete deleteAccount(ConfigAlteonDto account) throws UnsupportedEncodingException
	{
		byte[] credentials = Base64.encodeBase64(("tcpdump01" + ":" + "tcpdump12#$").getBytes(StandardCharsets.UTF_8));
		HttpDelete httpPost = new HttpDelete("https://localhost:2189/api/credentials;service=alteon;protocol="
				+ account.getConnectionType() + ";host=" + account.getName());
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type", "application/vnd.com.radware.vdirect.credentials+json; charset=utf8");

		return httpPost;
	}
	
	@Override
	public HttpPost setVirtualServerCreate(ConfigSlbDto vs) throws KeyManagementException, UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/VirtualServerCreate");
		StringEntity params = new StringEntity(createVirtualServer(vs));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);
		return httpPost;
	}
	
	private String createVirtualServer(ConfigSlbDto vs)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = virtualServerCreate(vs);

		HashMap<String, Object> devices = setAlteonName(vs.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	private HashMap<String, Object> virtualServerCreate(ConfigSlbDto vs)
	{
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("vid", vs.getVirtualServerID());		
		parameters.put("base_name", vs.getVirtualServerName());
		parameters.put("vip", vs.getVirtualServerIp());
		return parameters;
	}
	
	@Override
	public HttpPost setVirtualServerDelete(ConfigSlbDto virtualService) throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/VirtualServerDelete");
		StringEntity params = new StringEntity(virtualServerDelete(virtualService));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String virtualServerDelete(ConfigSlbDto virtualService)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = getVirtualServerDelete(virtualService);

		HashMap<String, Object> devices = setAlteonName(virtualService.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	private HashMap<String, Object> getVirtualServerDelete(ConfigSlbDto virtualService)
	{
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("vid", virtualService.getVirtualServerID());

		return parameters;
	}

	@Override
	public HttpPost setSLBCreate(ConfigSlbDto slb) throws KeyManagementException, UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/SLBCreate");
		StringEntity params = new StringEntity(createSLB(slb));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;

	}

	private String createSLB(ConfigSlbDto slb)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = setVirtualServer(slb);

		setRealServers(parameters, slb.getRealServerIps());

		HashMap<String, Object> devices = setAlteonName(slb.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	private HashMap<String, Object> setAlteonName(String alteonName)
	{
		HashMap<String, Object> devices = new HashMap<String, Object>();
		HashMap<String, Object> deviceId = new HashMap<String, Object>();
		HashMap<String, Object> device = new HashMap<String, Object>();
		device.put("name", alteonName);
		deviceId.put("deviceId", device);
		devices.put("adc", deviceId);
		return devices;
	}

	private HashMap<String, Object> setVirtualServer(ConfigSlbDto slb)
	{
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("vip", slb.getVirtualServerIp());
		parameters.put("base_name", slb.getVirtualServerName());
		return parameters;
	}

	private void setRealServers(HashMap<String, Object> parameters, ArrayList<String> realServers)
	{
		ArrayList<String> ips = new ArrayList<String>();
		for (String realserver : realServers)
		{
			ips.add(realserver);
		}
		parameters.put("server_ips", ips);
	}

	@Override
	public HttpPost setRealServerEnable(ServerEnableDisableDto realServers) throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/ServerEnable");
		StringEntity params = new StringEntity(realServerEnable(realServers));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String realServerEnable(ServerEnableDisableDto realServers)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = getID(realServers.getServerID(), realServers.getState());

		HashMap<String, Object> devices = setAlteonName(realServers.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	private HashMap<String, Object> getID(ArrayList<String> realServerIDs, String state)
	{
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("server_ips", realServerIDs);
		parameters.put("state", state);
		
		return parameters;
	}
	
	private HashMap<String, Object> getGroupID(String group, String state)
	{
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("group_id", group);
		parameters.put("real_state", state);
		
		return parameters;
	}

	@Override
	public HttpPost setRealServerDisable(ServerEnableDisableDto realServers) throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/ServerEnable");
		StringEntity params = new StringEntity(realServerDisable(realServers));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String realServerDisable(ServerEnableDisableDto realServers)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = getID(realServers.getServerID(), realServers.getState());

		HashMap<String, Object> devices = setAlteonName(realServers.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}
	
	@Override
	public HttpPost setRealServerState(ServerEnableDisableDto virtualServers)
			throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/RealEnableDisable");
		StringEntity params = new StringEntity(realServerState(virtualServers));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String realServerState(ServerEnableDisableDto real)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = getID(real.getServerID(), real.getState());

		HashMap<String, Object> devices = setAlteonName(real.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	@Override
	public HttpPost setVirtualServerState(ServerEnableDisableDto virtualServers)
			throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/VirtualEnableDisable");
		StringEntity params = new StringEntity(virtualServerState(virtualServers));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String virtualServerState(ServerEnableDisableDto virtualServers)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = getID(virtualServers.getServerID(), virtualServers.getState());

		HashMap<String, Object> devices = setAlteonName(virtualServers.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	
	@Override
	public HttpPost setVirtualServerDisable(ServerEnableDisableDto virtualServers)
			throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/VirtualEnableDisable");
		StringEntity params = new StringEntity(virtualServerDisable(virtualServers));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String virtualServerDisable(ServerEnableDisableDto virtualServers)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = getID(virtualServers.getServerID(), virtualServers.getState());

		HashMap<String, Object> devices = setAlteonName(virtualServers.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	@Override
	public HttpPost setCreateVirtualService(ConfigSlbDto virtualService) throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/ServiceCreate");
		StringEntity params = new StringEntity(virtualServiceCreate(virtualService));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String virtualServiceCreate(ConfigSlbDto virtualService)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = getServiceCreate(virtualService);

		HashMap<String, Object> devices = setAlteonName(virtualService.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	private HashMap<String, Object> getServiceCreate(ConfigSlbDto virtualService)
	{
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("vip_id", virtualService.getVirtualServerID());
		parameters.put("service", virtualService.getService());
		parameters.put("type", virtualService.getType());
		parameters.put("group_id", virtualService.getGroupID());
		parameters.put("rport", virtualService.getRport());

		return parameters;
	}

	@Override
	public HttpPost setDelteVirtualService(ConfigSlbDto virtualService) throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/ServiceDelete");
		StringEntity params = new StringEntity(virtualServiceDelete(virtualService));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String virtualServiceDelete(ConfigSlbDto virtualService)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = getServiceDelete(virtualService);

		HashMap<String, Object> devices = setAlteonName(virtualService.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	private HashMap<String, Object> getServiceDelete(ConfigSlbDto virtualService)
	{
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("vip_id", virtualService.getVirtualServerID());
		parameters.put("service", virtualService.getService());

		return parameters;
	}

	@Override
	public HttpPost setCommandApply(String alteonName) throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/CommandApply");
		StringEntity params = new StringEntity(commandApply(alteonName));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String commandApply(String alteonName)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = getCommand();

		HashMap<String, Object> devices = setAlteonName(alteonName);

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	private HashMap<String, Object> getCommand()
	{
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		return parameters;
	}

	@Override
	public HttpPost setCommandRevert(String alteonName) throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/CommandApply");
		StringEntity params = new StringEntity(commandRevert(alteonName));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String commandRevert(String alteonName)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = getCommand();

		HashMap<String, Object> devices = setAlteonName(alteonName);

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	@Override
	public HttpPost setGroupCreate(ConfigSlbDto group) throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/GroupCreate");
		StringEntity params = new StringEntity(createGroup(group));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String createGroup(ConfigSlbDto group)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = setGroupName(group);

		HashMap<String, Object> devices = setAlteonName(group.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	private HashMap<String, Object> setGroupName(ConfigSlbDto group)
	{
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("group_id", group.getGroupID());
		parameters.put("real_id", group.getRealServerID());
		return parameters;
	}

	@Override
	public HttpPost setGroupDelete(ConfigSlbDto group) throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/GroupDelete");
		StringEntity params = new StringEntity(deleteGroup(group));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String deleteGroup(ConfigSlbDto group)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = setGroupNameDelete(group);

		HashMap<String, Object> devices = setAlteonName(group.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	private HashMap<String, Object> setGroupNameDelete(ConfigSlbDto group)
	{
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("group_id", group.getGroupID());
		parameters.put("real_id", group.getRealServerID());
		return parameters;
	}
	
	private byte[] setVdirectAccount()
	{
		byte[] credentials = Base64.encodeBase64(("tcpdump01" + ":" + "tcpdump12#$").getBytes(StandardCharsets.UTF_8));
		return credentials;
	}
	
	@Override
	public HttpPost setRealServerCreate(ConfigSlbDto realServer) throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/RealServerCreate");
		StringEntity params = new StringEntity(realServerCreate(realServer));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String realServerCreate(ConfigSlbDto realServer)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = getRealCreate(realServer);

		HashMap<String, Object> devices = setAlteonName(realServer.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	private HashMap<String, Object> getRealCreate(ConfigSlbDto realServer)
	{
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("server_ids", realServer.getRealServerIDs());
		
		return parameters;
	}

	@Override
	public HttpPost setRealServerDelete(ConfigSlbDto realServer) throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/RealServerDelete");
		StringEntity params = new StringEntity(realServerDelete(realServer));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String realServerDelete(ConfigSlbDto realServer)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = getRealServerDelete(realServer);

		HashMap<String, Object> devices = setAlteonName(realServer.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	private HashMap<String, Object> getRealServerDelete(ConfigSlbDto realServer)
	{
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("vip_id", realServer.getVirtualServerID());
		parameters.put("service", realServer.getService());

		return parameters;
	}

	@Override
	public HttpPost setGroupState(ServerEnableDisableDto groups)
			throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/GroupEnableDisable");
		StringEntity params = new StringEntity(groupState(groups));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String groupState(ServerEnableDisableDto groups)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = getGroupID(groups.getGroupID(), groups.getState());

		HashMap<String, Object> devices = setAlteonName(groups.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}
	
	@Override
	public HttpPost setVrrpCreate(ConfigSlbDto vrrp) throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/CreateVrrp");
		StringEntity params = new StringEntity(vrrpCreate(vrrp));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String vrrpCreate(ConfigSlbDto virtualService)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = getVrrpCreate(virtualService);

		HashMap<String, Object> devices = setAlteonName(virtualService.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	private HashMap<String, Object> getVrrpCreate(ConfigSlbDto vrrps)
	{
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		vrrps.getVrrp().forEach((key,value) -> { 
			parameters.put(key, value);
		});

		return parameters;
	}
	
	@Override
	public HttpPost setVrrpDelete(ConfigSlbDto realServer) throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/DeleteVrrp");
		StringEntity params = new StringEntity(vrrpDelete(realServer));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String vrrpDelete(ConfigSlbDto realServer)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = getVrrpDelete(realServer);

		HashMap<String, Object> devices = setAlteonName(realServer.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	private HashMap<String, Object> getVrrpDelete(ConfigSlbDto vrrps)
	{
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		vrrps.getVrrp().forEach((key,value) -> { 
			parameters.put(key, value);
		});

		return parameters;
	}
	
	@Override
	public HttpPost sendCommand(CommandDto command) throws UnsupportedEncodingException
	{
		byte[] credentials = setVdirectAccount();
		HttpPost httpPost = new HttpPost("https://localhost:2189/api/template/Command");
		StringEntity params = new StringEntity(sendCommandApi(command));
		httpPost.setHeader("Authorization", "Basic " + new String(credentials, StandardCharsets.UTF_8));
		httpPost.addHeader("Content-Type",
				"application/vnd.com.radware.vdirect.template-parameters+json; charset=utf8");
		httpPost.setEntity(params);

		return httpPost;
	}

	private String sendCommandApi(CommandDto command)
	{
		RunSLBTemplateDto slbcreate = new RunSLBTemplateDto();
		ArrayList<String> tenants = new ArrayList<String>();
		HashMap<String, Object> parameters = getCommand(command);

		HashMap<String, Object> devices = setAlteonName(command.getAlteonName());

		slbcreate.setParameters(parameters);
		slbcreate.setDevices(devices);
		slbcreate.setTenants(tenants);

		Gson gson = new Gson();
		String trans = gson.toJson(slbcreate);
		return trans;
	}

	private HashMap<String, Object> getCommand(CommandDto command)
	{
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		command.getCommand().forEach((key,value) -> { 
			parameters.put(key, value);
		});

		return parameters;
	}
}
