package kr.openbase.adcsmart.service.impl.fault.dto;

public class OBDtoRespTimeTempInfo
{
	private Integer iCVAvgTime=0;// msec. client<->vip
	private Integer iCVMaxTime=0;// msec
	private Integer iCVMinTime=0;// msec

	private Integer iVRAvgTime=0;// msec. vip<->real
	private Integer iVRMaxTime=0;// msec
	private Integer iVRMinTime=0;// msec

	private Integer iCVReqCount=0;
	private Integer iCVRespCount=0;
	private Integer iVRReqCount=0;
	private Integer iVRRespCount=0;
	@Override
    public String toString()
    {
        return "OBDtoRespTimeTempInfo [iCVAvgTime=" + iCVAvgTime + ", iCVMaxTime=" + iCVMaxTime + ", iCVMinTime=" + iCVMinTime + ", iVRAvgTime=" + iVRAvgTime + ", iVRMaxTime=" + iVRMaxTime + ", iVRMinTime=" + iVRMinTime + ", iCVReqCount=" + iCVReqCount + ", iCVRespCount=" + iCVRespCount + ", iVRReqCount=" + iVRReqCount + ", iVRRespCount=" + iVRRespCount + "]";
    }
	public Integer getiCVAvgTime()
	{
		return iCVAvgTime;
	}
	public void setiCVAvgTime(Integer iCVAvgTime)
	{
		this.iCVAvgTime = iCVAvgTime;
	}
	public Integer getiCVMaxTime()
	{
		return iCVMaxTime;
	}
	public void setiCVMaxTime(Integer iCVMaxTime)
	{
		this.iCVMaxTime = iCVMaxTime;
	}
	public Integer getiCVMinTime()
	{
		return iCVMinTime;
	}
	public void setiCVMinTime(Integer iCVMinTime)
	{
		this.iCVMinTime = iCVMinTime;
	}
	public Integer getiVRAvgTime()
	{
		return iVRAvgTime;
	}
	public void setiVRAvgTime(Integer iVRAvgTime)
	{
		this.iVRAvgTime = iVRAvgTime;
	}
	public Integer getiVRMaxTime()
	{
		return iVRMaxTime;
	}
	public void setiVRMaxTime(Integer iVRMaxTime)
	{
		this.iVRMaxTime = iVRMaxTime;
	}
	public Integer getiVRMinTime()
	{
		return iVRMinTime;
	}
	public void setiVRMinTime(Integer iVRMinTime)
	{
		this.iVRMinTime = iVRMinTime;
	}
	public Integer getiCVReqCount()
	{
		return iCVReqCount;
	}
	public void setiCVReqCount(Integer iCVReqCount)
	{
		this.iCVReqCount = iCVReqCount;
	}
	
	public void setiCVRespCount(Integer iCVRespCount)
	{
		this.iCVRespCount = iCVRespCount;
	}
	
	public void setiVRReqCount(Integer iVRReqCount)
	{
		this.iVRReqCount = iVRReqCount;
	}
	
	public void setiVRRespCount(Integer iVRRespCount)
	{
		this.iVRRespCount = iVRRespCount;
	}
}
