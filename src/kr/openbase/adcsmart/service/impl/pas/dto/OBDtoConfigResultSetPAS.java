package kr.openbase.adcsmart.service.impl.pas.dto;

import java.util.ArrayList;

public class OBDtoConfigResultSetPAS
{
	private int change;
	private boolean writeMemory;
	private boolean writeHistory;
	private boolean writeConfig;
	private ArrayList<OBDtoConfigResultPAS> resultList;
	
	@Override
	public String toString()
	{
		return "OBDtoConfigResultSetPAS [change=" + change + ", writeMemory=" + writeMemory + ", writeHistory=" + writeHistory + ", writeConfig=" + writeConfig + ", resultList=" + resultList + "]";
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
	public ArrayList<OBDtoConfigResultPAS> getResultList()
	{
		return resultList;
	}
	public void setResultList(ArrayList<OBDtoConfigResultPAS> resultList)
	{
		this.resultList = resultList;
	}
}
