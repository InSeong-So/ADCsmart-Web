package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

public class OBDtoStatSlbSessionInfoAlteon
{
	private long	maxSession;
	private	long	sec4Session;//4sec session
	private long	sessionUsage;
	@Override
	public String toString()
	{
		return "OBDtoStatSlbSessionInfoVSvcStatusAlteon [maxSession=" + maxSession + ", sec4Session=" + sec4Session + ", sessionUsage=" + sessionUsage + "]";
	}
	public long getMaxSession()
	{
		return maxSession;
	}
	public void setMaxSession(long maxSession)
	{
		this.maxSession = maxSession;
	}
	public long getSec4Session()
	{
		return sec4Session;
	}
	public void setSec4Session(long sec4Session)
	{
		this.sec4Session = sec4Session;
	}
	public long getSessionUsage()
	{
		return sessionUsage;
	}
	public void setSessionUsage(long sessionUsage)
	{
		this.sessionUsage = sessionUsage;
	}
}
