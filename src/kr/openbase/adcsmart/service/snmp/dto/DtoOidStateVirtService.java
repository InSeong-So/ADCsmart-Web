package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidStateVirtService
{
	private String virtServiceState;
	
	@Override
	public String toString() 
	{
		return "DtoOidStateVirtService [virtServiceState=" + this.virtServiceState + "]";
	}
	
	public void setVirtServiceState(String virtServiceState)
	{
		this.virtServiceState = virtServiceState;
	}
	public String getVirtServiceState()
	{
		return this.virtServiceState;
	}
}
