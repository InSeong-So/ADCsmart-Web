package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidInfoSessionTable
{
	private String maintAllocFails;
	private String auxIndex;
	private String auxCurrConn;
	private String auxMaxConn;
	private String auxAllocFails;
	@Override
	public String toString()
	{
		return "DtoOidInfoSessionTable [maintAllocFails=" + maintAllocFails + ", auxIndex=" + auxIndex + ", auxCurrConn=" + auxCurrConn + ", auxMaxConn=" + auxMaxConn + ", auxAllocFails=" + auxAllocFails + "]";
	}
	public String getMaintAllocFails()
	{
		return maintAllocFails;
	}
	public void setMaintAllocFails(String maintAllocFails)
	{
		this.maintAllocFails = maintAllocFails;
	}
	public String getAuxIndex()
	{
		return auxIndex;
	}
	public void setAuxIndex(String auxIndex)
	{
		this.auxIndex = auxIndex;
	}
	public String getAuxCurrConn()
	{
		return auxCurrConn;
	}
	public void setAuxCurrConn(String auxCurrConn)
	{
		this.auxCurrConn = auxCurrConn;
	}
	public String getAuxMaxConn()
	{
		return auxMaxConn;
	}
	public void setAuxMaxConn(String auxMaxConn)
	{
		this.auxMaxConn = auxMaxConn;
	}
	public String getAuxAllocFails()
	{
		return auxAllocFails;
	}
	public void setAuxAllocFails(String auxAllocFails)
	{
		this.auxAllocFails = auxAllocFails;
	}
}
