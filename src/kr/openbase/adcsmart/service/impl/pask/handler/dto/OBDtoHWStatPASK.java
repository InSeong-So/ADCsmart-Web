package kr.openbase.adcsmart.service.impl.pask.handler.dto;

public class OBDtoHWStatPASK
{
	private	int		ac1Status		=0;		
	private	int		ac2Status		=0;		
	private	int		fan1Status		=0;		
	private	int		fan2Status		=0;		
	private	int		fan3Status		=0;		
	private	int		fan4Status		=0;
	@Override
	public String toString()
	{
		return "OBDtoHWStatPASK [ac1Status=" + ac1Status + ", ac2Status=" + ac2Status + ", fan1Status=" + fan1Status + ", fan2Status=" + fan2Status + ", fan3Status=" + fan3Status + ", fan4Status=" + fan4Status + "]";
	}
	public int getAc1Status()
	{
		return ac1Status;
	}
	public void setAc1Status(int ac1Status)
	{
		this.ac1Status = ac1Status;
	}
	public int getAc2Status()
	{
		return ac2Status;
	}
	public void setAc2Status(int ac2Status)
	{
		this.ac2Status = ac2Status;
	}
	public int getFan1Status()
	{
		return fan1Status;
	}
	public void setFan1Status(int fan1Status)
	{
		this.fan1Status = fan1Status;
	}
	public int getFan2Status()
	{
		return fan2Status;
	}
	public void setFan2Status(int fan2Status)
	{
		this.fan2Status = fan2Status;
	}
	public int getFan3Status()
	{
		return fan3Status;
	}
	public void setFan3Status(int fan3Status)
	{
		this.fan3Status = fan3Status;
	}
	public int getFan4Status()
	{
		return fan4Status;
	}
	public void setFan4Status(int fan4Status)
	{
		this.fan4Status = fan4Status;
	}
}
