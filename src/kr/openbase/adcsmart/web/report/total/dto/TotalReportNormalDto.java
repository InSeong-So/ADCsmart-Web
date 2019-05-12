package kr.openbase.adcsmart.web.report.total.dto;

/*
 * 정기정검 보고서
 */
public class TotalReportNormalDto
{

	private String hostName; // Host Name
	private String serial; // 시리얼
	private String modelName; // 모델명
	private Integer adcType; // ADCTYPE
	private String os; // OS
	private String powerStatus; // Power 상태
	private String cpu; // CPU
	private String portStatus; // Port 상태
	private String failover; // 이중화 기능
	private String direct; // Direct 기능
	private String realServerStatus; // Real Server 상태
	private String virtualServerStatus; // Virtual Server 상태
	private String slbSession; // SLB 세션 통계
	private String result; // 종합결과

	public TotalReportNormalDto()
	{
	}

	public String getHostName()
	{
		return hostName;
	}

	public void setHostName(String hostName)
	{
		this.hostName = hostName;
	}

	public String getModelName()
	{
		return modelName;
	}

	public void setModelName(String modelName)
	{
		this.modelName = modelName;
	}

	public String getOs()
	{
		return os;
	}

	public void setOs(String os)
	{
		this.os = os;
	}

	public String getPowerStatus()
	{
		return powerStatus;
	}

	public void setPowerStatus(String powerStatus)
	{
		this.powerStatus = powerStatus;
	}

	public String getCpu()
	{
		return cpu;
	}

	public void setCpu(String cpu)
	{
		this.cpu = cpu;
	}

	public String getPortStatus()
	{
		return portStatus;
	}

	public void setPortStatus(String portStatus)
	{
		this.portStatus = portStatus;
	}

	public String getRealServerStatus()
	{
		return realServerStatus;
	}

	public void setRealServerStatus(String realServerStatus)
	{
		this.realServerStatus = realServerStatus;
	}

	public String getVirtualServerStatus()
	{
		return virtualServerStatus;
	}

	public void setVirtualServerStatus(String virtualServerStatus)
	{
		this.virtualServerStatus = virtualServerStatus;
	}

	public String getSlbSession()
	{
		return slbSession;
	}

	public void setSlbSession(String slbSession)
	{
		this.slbSession = slbSession;
	}

	public String getResult()
	{
		return result;
	}

	public void setResult(String result)
	{
		this.result = result;
	}

	public Integer getAdcType()
	{
		return adcType;
	}

	public void setAdcType(Integer adcType)
	{
		this.adcType = adcType;
	}

	public String getFailover()
	{
		return failover;
	}

	public void setFailover(String failover)
	{
		this.failover = failover;
	}

	public String getDirect()
	{
		return direct;
	}

	public void setDirect(String direct)
	{
		this.direct = direct;
	}

	public String getSerial()
	{
		return serial;
	}

	public void setSerial(String serial)
	{
		this.serial = serial;
	}

	@Override
	public String toString()
	{
		return "TotalReportNormalDto [hostName=" + hostName + ", serial=" + serial + ", modelName=" + modelName
				+ ", adcType=" + adcType + ", os=" + os + ", powerStatus=" + powerStatus + ", cpu=" + cpu
				+ ", portStatus=" + portStatus + ", failover=" + failover + ", direct=" + direct + ", realServerStatus="
				+ realServerStatus + ", virtualServerStatus=" + virtualServerStatus + ", slbSession=" + slbSession
				+ ", result=" + result + "]";
	}
}
