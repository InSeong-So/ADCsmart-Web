package kr.openbase.adcsmart.service.dto;

public class OBDtoAlteonInterface
{
	private int ifNum;
	private int ipVer;
	private String addr;
	private String mask;
	private String broad;
	private int enableDisable;
	
	@Override
	public String toString() 
	{
		return "OBDtoAlteonInterface [ifNum=" + this.ifNum + ", ipVer=" + this.ipVer + ", addr="
				+ this.addr + ", mask=" + this.mask + ", broad=" + this.broad
				+ ", enableDisable=" + this.enableDisable + "]";
	}
	
	public void setEnableDisable(int enableDisable)
	{
		this.enableDisable=enableDisable;
	}
	public int getEnableDisable()
	{
		return this.enableDisable;
	}
	
	public void setIfNum(int ifNum)
	{
		this.ifNum = ifNum;
	}
	public int getIfNum()
	{
		return this.ifNum;
	}
	
	public void setIpVer(int ipVer)
	{
		this.ipVer = ipVer;
	}
	public int getIpVer()
	{
		return this.ipVer;
	}
	
	public void setAddr(String addr)
	{
		this.addr = addr;
	}
	public String getAddr()
	{
		return this.addr;
	}
	
	public void setMask(String mask)
	{
		this.mask = mask;
	}
	public String getMask()
	{
		return this.mask;
	}

	public void setBroad(String broad)
	{
		this.broad = broad;
	}
	public String getBroad()
	{
		return this.broad;
	}
}
