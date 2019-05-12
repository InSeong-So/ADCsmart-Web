package kr.openbase.adcsmart.service.snmp.dto;

public class DtoSnmpStatPool extends DtoSnmpStat
{
	private String poolIDName;//F5의 경우에 이름. alteon인 경우에는 id가 문자열 형태로 저장.
	
	@Override
	public String toString() 
	{
		return "OBDtoStatPool [poolIDName=" + this.poolIDName + " " + super.toString() + "]";
	}
	
	public void setPoolIDName(String poolIDName)
	{
		this.poolIDName = poolIDName;
	}
	public void setPoolIDName(int poolIDName)
	{
		this.poolIDName = Integer.toString(poolIDName);
	}
	public String getPoolIDNameSting()
	{
		return this.poolIDName;
	}
	public int getPoolIDNameInt()
	{
		return Integer.parseInt(this.poolIDName);
	}	
}
