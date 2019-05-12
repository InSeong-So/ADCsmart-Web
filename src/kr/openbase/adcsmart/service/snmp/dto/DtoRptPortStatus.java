package kr.openbase.adcsmart.service.snmp.dto;

public class DtoRptPortStatus
{
	private String name;
	private int changedTime;
	private int status;
	private long discardsIn;
	private long discardsOut;
	private long errorIn;
	private long errorOut;
	@Override
	public String toString()
	{
		return "DtoRptPortStatus [name=" + name + ", changedTime="
				+ changedTime + ", satutus=" + status + ", discardsIn="
				+ discardsIn + ", discardsOut=" + discardsOut + ", errorIn="
				+ errorIn + ", errorOut=" + errorOut + "]";
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getChangedTime()
	{
		return changedTime;
	}
	public void setChangedTime(int changedTime)
	{
		this.changedTime = changedTime;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public long getDiscardsIn()
	{
		return discardsIn;
	}
	public void setDiscardsIn(long discardsIn)
	{
		this.discardsIn = discardsIn;
	}
	public long getDiscardsOut()
	{
		return discardsOut;
	}
	public void setDiscardsOut(long discardsOut)
	{
		this.discardsOut = discardsOut;
	}
	public long getErrorIn()
	{
		return errorIn;
	}
	public void setErrorIn(long errorIn)
	{
		this.errorIn = errorIn;
	}
	public long getErrorOut()
	{
		return errorOut;
	}
	public void setErrorOut(long errorOut)
	{
		this.errorOut = errorOut;
	}
}
