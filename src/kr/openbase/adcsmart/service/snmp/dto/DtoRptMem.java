package kr.openbase.adcsmart.service.snmp.dto;

public class DtoRptMem
{
	private int type;//1:MP, 2:SP, 3:TMM
	private long total;
	private long used;
	@Override
	public String toString()
	{
		return "DtoRptMem [type=" + type + ", total=" + total + ", used="
				+ used + "]";
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public long getTotal()
	{
		return total;
	}
	public void setTotal(long total)
	{
		this.total = total;
	}
	public long getUsed()
	{
		return used;
	}
	public void setUsed(long used)
	{
		this.used = used;
	}
}
