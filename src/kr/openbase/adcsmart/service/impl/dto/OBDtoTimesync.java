package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoTimesync
{
	private int useYN;
	private String ntpServer;
	private int wkdy;
	private int date;
	private int time;
	
	public void setUseYN(int useYN)
	{
		this.useYN = useYN;
	}
	public int getUseYN()
	{
		return this.useYN;
	}

	public void setNtpServer(String ntpServer)
	{
		this.ntpServer = ntpServer;
	}
	public String getNtpServer()
	{
		return this.ntpServer;
	}

	public void setWkdy(int wkdy)
	{
		this.wkdy = wkdy;
	}
	public int getWkdy()
	{
		return this.wkdy;
	}
	
	public void setDate(int date)
	{
		this.date = date;
	}
	public int getDate()
	{
		return this.date;
	}

	public void setTime(int time)
	{
		this.time = time;
	}
	public int getTime()
	{
		return this.time;
	}
}
