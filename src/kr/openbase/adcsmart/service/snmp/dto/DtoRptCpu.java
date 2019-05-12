package kr.openbase.adcsmart.service.snmp.dto;

public class DtoRptCpu
{
	private int type;//1:MP, 2:SP
	private int usage;
	@Override
	public String toString()
	{
		return "DtoRptCpu [type=" + type + ", usage=" + usage + "]";
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public int getUsage()
	{
		return usage;
	}
	public void setUsage(int usage)
	{
		this.usage = usage;
	}
}
