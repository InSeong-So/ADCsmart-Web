package kr.openbase.adcsmart.service.snmp.alteon.dto;

public class DtoSnmpVirtServiceAlteon
{
	private String vsID;
	private int vsrvIndex;
	private int vsrvVirtPort;
	private int vsrvRealPort;
	private String poolID;
	private int 	status;
	
	@Override
    public String toString()
    {
        return "DtoSnmpVirtServiceAlteon [vsID=" + vsID + ", vsrvIndex="
                + vsrvIndex + ", vsrvVirtPort=" + vsrvVirtPort
                + ", vsrvRealPort=" + vsrvRealPort + ", poolID=" + poolID
                + ", status=" + status + "]";
    }

	public String getVsID()
    {
        return vsID;
    }

    public void setVsID(String vsID)
    {
        this.vsID = vsID;
    }

    public int getVsrvIndex()
	{
		return vsrvIndex;
	}

	public void setVsrvIndex(int vsrvIndex)
	{
		this.vsrvIndex = vsrvIndex;
	}

	public int getVsrvVirtPort()
	{
		return vsrvVirtPort;
	}

	public void setVsrvVirtPort(int vsrvVirtPort)
	{
		this.vsrvVirtPort = vsrvVirtPort;
	}

	public int getVsrvRealPort()
	{
		return vsrvRealPort;
	}

	public void setVsrvRealPort(int vsrvRealPort)
	{
		this.vsrvRealPort = vsrvRealPort;
	}

	public String getPoolID()
	{
		return poolID;
	}

	public void setPoolID(String poolID)
	{
		this.poolID = poolID;
	}

	public int getStatus()
	{
		return status;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}	
}
