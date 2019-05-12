package kr.openbase.adcsmart.service.impl.pas.dto;

public class OBDtoConfigResultPAS
{
	private int change;
	private boolean writeMemory;
	private boolean writeHistory;
	private boolean writeConfig;
	private OBDtoAdcVServerPAS virtualServer;
	
	@Override
	public String toString()
	{
		return "OBDtoConfigResultPAS [change=" + change + ", writeMemory=" + writeMemory + ", writeHistory=" + writeHistory + ", writeConfig=" + writeConfig + ", virtualServer=" + virtualServer + "]";
	}
	
	public int getChange()
	{
		return change;
	}
	public void setChange(int change)
	{
		this.change = change;
	}
	public boolean isWriteMemory()
	{
		return writeMemory;
	}
	public void setWriteMemory(boolean writeMemory)
	{
		this.writeMemory = writeMemory;
	}
	public OBDtoAdcVServerPAS getVirtualServer()
	{
		return virtualServer;
	}
	public void setVirtualServer(OBDtoAdcVServerPAS virtualServer)
	{
		this.virtualServer = virtualServer;
	}
	public boolean isWriteHistory()
	{
		return writeHistory;
	}
	public void setWriteHistory(boolean writeHistory)
	{
		this.writeHistory = writeHistory;
	}
	public boolean isWriteConfig()
	{
		return writeConfig;
	}
	public void setWriteConfig(boolean writeConfig)
	{
		this.writeConfig = writeConfig;
	}
}
