package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoDashboardSdsMemberConnection
{
	private String memberIp;
	private Integer memberPort;
	private Long	conns;
	private Date occurTime;
	
	@Override
	public String toString()
	{
		return "OBDtoMemberConnection [memberIp=" + memberIp + ", memberPort="
				+ memberPort + ", conns=" + conns + ", occurTime=" + occurTime
				+ "]";
	}

	public String getMemberIp()
	{
		return memberIp;
	}
	public void setMemberIp(String memberIp)
	{
		this.memberIp = memberIp;
	}
	public Integer getMemberPort()
	{
		return memberPort;
	}
	public void setMemberPort(Integer memberPort)
	{
		this.memberPort = memberPort;
	}
	public Long getConns()
	{
		return conns;
	}
	public void setConns(Long conns)
	{
		this.conns = conns;
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
}
