package kr.openbase.adcsmart.service.dto.report;

public class OBDtoRptSystemInfo
{
	private OBDtoRptSysInfoBasic 	basicInfo;
	private OBDtoRptSysInfoL2	 	l2Info;
	private OBDtoRptSysInfoL3	 	l3Info;
	private OBDtoRptSysInfoL4		l4Info;
	private OBDtoRptSysInfoL7		l7Info;
	private OBDtoRptSysInfoEtc		etcInfo;
	@Override
	public String toString()
	{
		return "OBDtoRptSystemInfo [basicInfo=" + basicInfo + ", l2Info="
				+ l2Info + ", l3Info=" + l3Info + ", l4Info=" + l4Info
				+ ", l7Info=" + l7Info + ", etcInfo=" + etcInfo + "]";
	}
	public OBDtoRptSysInfoBasic getBasicInfo()
	{
		return basicInfo;
	}
	public void setBasicInfo(OBDtoRptSysInfoBasic basicInfo)
	{
		this.basicInfo = basicInfo;
	}
	public OBDtoRptSysInfoL2 getL2Info()
	{
		return l2Info;
	}
	public void setL2Info(OBDtoRptSysInfoL2 l2Info)
	{
		this.l2Info = l2Info;
	}
	public OBDtoRptSysInfoL3 getL3Info()
	{
		return l3Info;
	}
	public void setL3Info(OBDtoRptSysInfoL3 l3Info)
	{
		this.l3Info = l3Info;
	}
	public OBDtoRptSysInfoL4 getL4Info()
	{
		return l4Info;
	}
	public void setL4Info(OBDtoRptSysInfoL4 l4Info)
	{
		this.l4Info = l4Info;
	}
	public OBDtoRptSysInfoL7 getL7Info()
	{
		return l7Info;
	}
	public void setL7Info(OBDtoRptSysInfoL7 l7Info)
	{
		this.l7Info = l7Info;
	}
	public OBDtoRptSysInfoEtc getEtcInfo()
	{
		return etcInfo;
	}
	public void setEtcInfo(OBDtoRptSysInfoEtc etcInfo)
	{
		this.etcInfo = etcInfo;
	}
}
