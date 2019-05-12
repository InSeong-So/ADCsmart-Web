package kr.openbase.adcsmart.service.impl.alteon.vdirect;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

import kr.openbase.adcsmart.service.impl.alteon.OBVdirectService;
import kr.openbase.adcsmart.service.utility.OBUtility;
import kr.openbase.adcsmart.service.vdirect.RestRequest;
import kr.openbase.adcsmart.service.vdirect.dto.CommandDto;
import kr.openbase.adcsmart.service.vdirect.dto.ConfigAlteonDto;
import kr.openbase.adcsmart.service.vdirect.dto.ConfigSlbDto;
import kr.openbase.adcsmart.service.vdirect.dto.ServerEnableDisableDto;

public class OBVdirectServiceImpl implements OBVdirectService
{
	@Override
	public String createAlteon(ConfigAlteonDto alteon)
	{
		RestRequest request = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(request, client);
        
        HttpPost post = setHttpCreateAlteon(request, alteon);
        
        

        return responseResultAlteonData(request, client, alteon, post);
	}
	
    private HttpPost setHttpCreateAlteon(RestRequest request, ConfigAlteonDto alteon)
    {
        HttpPost post = null;
        try
        {
            post = request.createAlteon(alteon);
        } 
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return post;
    }
    
	private String responseResultAlteonData(RestRequest request, CloseableHttpClient client,
			ConfigAlteonDto slb, HttpPost post)
	{
		String result = "";

		HttpResponse response = null;
		try
		{
			response = client.execute(post);
		} 
		catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final HttpEntity entity = response.getEntity();
		result = getResponseContentData(result, entity);
		result = Integer.toString(response.getStatusLine().getStatusCode());
		return result;
	}
	
	private String responseResultAlteonDelete(RestRequest request, CloseableHttpClient client,
			ConfigAlteonDto slb, HttpDelete post)
	{
		String result = "";

		HttpResponse response = null;
		try
		{
			response = client.execute(post);
		} 
		catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final HttpEntity entity = response.getEntity();
		result = getResponseContentData(result, entity);
		result = Integer.toString(response.getStatusLine().getStatusCode());
		return result;
	}
	
	@Override
	public String configSnmp(ConfigAlteonDto alteon)
	{
		RestRequest request = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(request, client);
        
        HttpPost post = setHttpConfigSnmp(request, alteon);
        
        
        
        return responseResultAlteonData(request, client, alteon, post);
	}
	
    private HttpPost setHttpConfigSnmp(RestRequest request, ConfigAlteonDto alteon)
    {
        HttpPost post = null;
        try
        {
            post = request.configSnmp(alteon);
        } 
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return post;
    }
    
	@Override
	public String configCli(ConfigAlteonDto alteon)
	{
		RestRequest request = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(request, client);
        
        HttpPost post = setHttpConfigCli(request, alteon);
        
        
        
        return responseResultAlteonData(request, client, alteon, post);
	}
	
    private HttpPost setHttpConfigCli(RestRequest request, ConfigAlteonDto alteon)
    {
        HttpPost post = null;
        try
        {
            post = request.configAccount(alteon);
        } 
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return post;
    }
	
	@Override
	public String deleteAlteon(ConfigAlteonDto alteon)
	{
		RestRequest request = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(request, client);
        
        HttpDelete post = setHttpDeleteAlteon(request, alteon);
        
        
        
        return responseResultAlteonDelete(request, client, alteon, post);
	}
	
    private HttpDelete setHttpDeleteAlteon(RestRequest request, ConfigAlteonDto alteon)
    {
    	HttpDelete post = null;
        try
        {
            post = request.deleteAlteon(alteon);
        } 
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return post;
    }
	
	@Override
	public String deleteSnmp(ConfigAlteonDto alteon)
	{
		RestRequest request = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(request, client);
        
        HttpDelete post = setHttpDeleteSnmp(request, alteon);
        
        
        
        return responseResultAlteonDelete(request, client, alteon, post);
	}
	
    private HttpDelete setHttpDeleteSnmp(RestRequest request, ConfigAlteonDto alteon)
    {
    	HttpDelete post = null;
        try
        {
            post = request.deleteSnmp(alteon);
        } 
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return post;
    }
	
	@Override
	public String deleteCli(ConfigAlteonDto alteon)
	{
		RestRequest request = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(request, client);
        
        HttpDelete post = setHttpDeleteAccount(request, alteon);
        
        
        
        return responseResultAlteonDelete(request, client, alteon, post);
	}
	
    private HttpDelete setHttpDeleteAccount(RestRequest request, ConfigAlteonDto alteon)
    {
    	HttpDelete post = null;
        try
        {
            post = request.deleteAlteon(alteon);
        } 
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return post;
    }

    
    // SLB 생성
    @Override
    public ConfigSlbDto createSlb()
    {       
        RestRequest request = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(request, client);

        ConfigSlbDto slb = inputSLBServer();
        HttpPost post = setHttpCreateSlb(request, slb);
        
        
        
        responseResultData(request, client, post);
        
        return slb;
    }
    
    private ConfigSlbDto inputSLBServer()
    {
        ConfigSlbDto slb = new ConfigSlbDto();
        slb.setAlteonName("alteon");
        ArrayList<String> realServerIp = new ArrayList<String>();
        realServerIp.add("4.4.4.2");
        realServerIp.add("4.4.4.3");

        slb.setVirtualServerIp("4.4.4.101");
        slb.setVirtualServerName("virtualserver1");

        slb.setRealServerIps(realServerIp);
        return slb;
    }
    
    private HttpPost setHttpCreateSlb(RestRequest request, ConfigSlbDto slb)
    {
        HttpPost post = null;
        try
        {
            post = request.setSLBCreate(slb);
        } 
        catch (KeyManagementException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return post;
    }
    
	@Override
	public String createVirtualServer(ConfigSlbDto virtualServer)
	{
        RestRequest request = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(request, client);

        HttpPost post = setHttpCreateVirtualServer(request, virtualServer);
        
        

        return responseResultData(request, client, post);
    }
    
    private HttpPost setHttpCreateVirtualServer(RestRequest request, ConfigSlbDto virtualServer)
    {
        HttpPost post = null;
        try
        {
            post = request.setVirtualServerCreate(virtualServer);
        } 
        catch (KeyManagementException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return post;
    }

	@Override
	public String deleteVirtualServer(ConfigSlbDto service)
	{
        RestRequest request = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(request, client);

        HttpPost post = setHttpDeleteServer(request, service);
        
        

        return responseResultData(request, client, post);
    }

    private HttpPost setHttpDeleteServer(RestRequest request, ConfigSlbDto slb)
    {
        HttpPost post = null;
        try
        {
            post = request.setVirtualServerDelete(slb);
        } 
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return post;
    }
    
    // 서비스 생성
    @Override
    public String createVirtualService(ConfigSlbDto service)
    {
        RestRequest request = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(request, client);

        HttpPost post = setHttpCreateService(request, service);
        
        

        return responseResultData(request, client, post);
    }

    private HttpPost setHttpCreateService(RestRequest request, ConfigSlbDto slb)
    {
        HttpPost post = null;
        try
        {
            post = request.setCreateVirtualService(slb);
        } 
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return post;
    }
    
    // 서비스 삭제
    @Override
    public String deleteVirtualService(ConfigSlbDto service)
    {
        RestRequest request = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(request, client);

        HttpPost post = setHttpDeleteService(request, service);
        
        

        return responseResultData(request, client, post);
    }

    private HttpPost setHttpDeleteService(RestRequest request, ConfigSlbDto slb)
    {
        HttpPost post = null;
        try
        {
            post = request.setDelteVirtualService(slb);
        } 
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return post;
    }
    
    // 그룹 생성    
	@Override
	public String createGroupServer(ConfigSlbDto group)
    {
        RestRequest resquest = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(resquest, client);
        
        HttpPost post = setHttpCreateGroup(resquest, group);

        return responseResultData(resquest, client, post);
    }

	private HttpPost setHttpCreateGroup(RestRequest request, ConfigSlbDto slb)
	{
		HttpPost post = null;
		try
		{
			post = request.setGroupCreate(slb);
		} 
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return post;
	}
	
	// 그룹 삭제
	@Override
    public String deleteGroupServer(ConfigSlbDto group)
    {        
        RestRequest request = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(request, client);

        HttpPost post = setHttpDeleteGroup(request, group);
        
        

        return responseResultData(request, client, post);
    }
	
	private HttpPost setHttpDeleteGroup(RestRequest request, ConfigSlbDto slb)
    {
        HttpPost post = null;
        try
        {
            post = request.setGroupDelete(slb);
        } 
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return post;
    }
	
	// 서비스 Enable , Disable
	
	@Override
    public String changeVirtualServerState(ServerEnableDisableDto virtualServer)
    {
        RestRequest request = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(request, client);
        
        HttpPost post = setHttpChangeVsState(request, virtualServer);
        
        
        
        return responseStatusResultData(request, client, virtualServer, post);        
    }
	
	public HttpPost setHttpChangeVsState(RestRequest request, ServerEnableDisableDto virtualServer)
	{
	    HttpPost post = null;
	    try
        {
            post = request.setVirtualServerState(virtualServer);
        }	    
	    catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    return post;
	}
   
	
	// Real Enable, Disable
    @Override
    public String changeRealSeverStatus(ServerEnableDisableDto real)
    {
        RestRequest request = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(request, client);
                
        HttpPost post = setHttpChangeRsStaus(request, real);
        
        
        
        return responseStatusResultData(request, client, real, post);        
    }
        
    public HttpPost setHttpChangeRsStaus(RestRequest request, ServerEnableDisableDto real)
    {
        HttpPost post = null;
        try
        {            
            post = request.setRealServerState(real);
        }       
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return post;
    }
    
	private String responseResultData(RestRequest request, CloseableHttpClient client, HttpPost post)
	{
		String result = "";

		HttpResponse response = null;
		try
		{
			response = client.execute(post);
		} 
		catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final HttpEntity entity = response.getEntity();
		result = getResponseContentData(result, entity);
		result = Integer.toString(response.getStatusLine().getStatusCode());
		return result;
	}
	
	private String responseStatusResultData(RestRequest request, CloseableHttpClient client,
	        ServerEnableDisableDto slb, HttpPost post)
    {
        String result = "";

        HttpResponse response = null;
        try
        {
            response = client.execute(post);
        } 
        catch (ClientProtocolException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final HttpEntity entity = response.getEntity();
        result = getResponseContentData(result, entity);
        result = Integer.toString(response.getStatusLine().getStatusCode());
        return result;
    }
	
	private String getResponseContentData(String result, final HttpEntity entity)
	{
		if (entity == null)
		{
		}
		else
		{
			try
			{
				result = OBUtility.inputStreamToString(entity.getContent());
			} 
			catch (UnsupportedOperationException e)
			{
				e.printStackTrace();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
			String line = System.getProperty("line.separator");
			result = result.replace("\\n", line);
		}
		return result;
	}
	
	private CloseableHttpClient connect(RestRequest request, CloseableHttpClient client)
	{
		try
		{
			client = request.init();
		} 
		catch (KeyManagementException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		catch (NoSuchAlgorithmException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		catch (KeyStoreException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return client;
	}

	@Override
	public String createRealServer(ConfigSlbDto realserver)
	{
        RestRequest request = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(request, client);

        HttpPost post = setHttpCreateRealServer(request, realserver);
        
        

        return responseResultData(request, client, post);
    }
    
    private HttpPost setHttpCreateRealServer(RestRequest request, ConfigSlbDto realserver)
    {
        HttpPost post = null;
        try
        {
            post = request.setRealServerCreate(realserver);
        } 
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return post;
    }

	@Override
	public String deleteRealServer(ConfigSlbDto real)
	{
		return null;
	}

	@Override
    public String changeGroupState(ServerEnableDisableDto group)
    {
        RestRequest request = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(request, client);
        
        HttpPost post = setHttpChangeGroupState(request, group);
        
        
        
        return responseStatusResultData(request, client, group, post);        
    }
	
	public HttpPost setHttpChangeGroupState(RestRequest request, ServerEnableDisableDto group)
	{
	    HttpPost post = null;
	    try
        {
            post = request.setGroupState(group);
        }	    
	    catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    return post;
	}

	@Override
	public String createVrrp(ConfigSlbDto vrrp)
    {
        RestRequest resquest = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(resquest, client);
        
        HttpPost post = setHttpCreateVrrp(resquest, vrrp);
        
        

        return responseResultData(resquest, client, post);
    }

	private HttpPost setHttpCreateVrrp(RestRequest request, ConfigSlbDto vrrp)
	{
		HttpPost post = null;
		try
		{
			post = request.setVrrpCreate(vrrp);
		} 
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return post;
	}
	
	@Override
    public String deleteVrrp(ConfigSlbDto vrrp)
    {        
        RestRequest request = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(request, client);

        HttpPost post = setHttpDeleteVrrp(request, vrrp);
        
        

        return responseResultData(request, client, post);
    }
	
	private HttpPost setHttpDeleteVrrp(RestRequest request, ConfigSlbDto vrrp)
    {
        HttpPost post = null;
        try
        {
            post = request.setVrrpDelete(vrrp);
        } 
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return post;
    }
	
	@Override
	public String sendCommand(CommandDto command)
    {
        RestRequest resquest = new RestRequest();
        CloseableHttpClient client = null;
        client = connect(resquest, client);
        
        HttpPost post = setHttpCreateVrrp(resquest, command);
        
        

        return responseResultData(resquest, client, post);
    }

	private HttpPost setHttpCreateVrrp(RestRequest request, CommandDto command)
	{
		HttpPost post = null;
		try
		{
			post = request.sendCommand(command);
		} 
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return post;
	}
}
