package kr.openbase.adcsmart.service.vdirect.dto;

import java.util.ArrayList;
import java.util.HashMap;

public class RunSLBTemplateDto
{
	private HashMap<String, Object> parameters;
	private HashMap<String, Object> devices;
	private ArrayList<String> tenants;

	public HashMap<String, Object> getParameters()
	{
		return parameters;
	}

	public void setParameters(HashMap<String, Object> parameters)
	{
		this.parameters = parameters;
	}

	public HashMap<String, Object> getDevices()
	{
		return devices;
	}

	public void setDevices(HashMap<String, Object> devices)
	{
		this.devices = devices;
	}

	public ArrayList<String> getTenants()
	{
		return tenants;
	}

	public void setTenants(ArrayList<String> tenants)
	{
		this.tenants = tenants;
	}
}
