package kr.openbase.adcsmart.service.impl.pask.handler.dto;

public class DtoRptStpInfoPASK
{
	private int state;//enabled?disabled?
	private String portList;
	@Override
	public String toString()
	{
		return "DtoRptStpInfoPASK [state=" + state + ", portList=" + portList + "]";
	}
	public int getState()
	{
		return state;
	}
	public void setState(int state)
	{
		this.state = state;
	}
	public String getPortList()
	{
		return portList;
	}
	public void setPortList(String portList)
	{
		this.portList = portList;
	}
}
