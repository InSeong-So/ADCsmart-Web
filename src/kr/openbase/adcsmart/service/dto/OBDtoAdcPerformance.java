package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

public class OBDtoAdcPerformance
{
	private ArrayList<OBDtoAdcPerfHttpReq> httpReqList;
	private Long	httpReqMax;
	private Long	httpReqMin;
	private Long	httpReqAvg;
	private Long	httpReqCurr;
	
	private ArrayList<OBDtoAdcPerfSslTrans> sshTransList;
	private Long	sslTransMax;
	private Long	sslTransMin;
	private Long	sslTransAvg;
	private Long	sslTransCurr;
	
	private ArrayList<OBDtoAdcPerfConnection> connList;
	private Long	connMax;
	private Long	connMin;
	private Long	connAvg;
	private Long	connCurr;
	
	private ArrayList<OBDtoAdcPerfTroughput> throughputList;
	private Long	bpsMax;
	private Long	bpsMin;
	private Long	bpsAvg;
	private Long	bpsCurr;
	private Long	ppsMax;
	private Long	ppsMin;
	private Long	ppsAvg;
	private Long	ppsCurr;
	
	@Override
	public String toString()
	{
		return "OBDtoAdcPerformance [httpReqList=" + httpReqList
				+ ", httpReqMax=" + httpReqMax + ", httpReqMin=" + httpReqMin
				+ ", httpReqAvg=" + httpReqAvg + ", httpReqCurr=" + httpReqCurr
				+ ", sshTransList=" + sshTransList + ", sslTransMax="
				+ sslTransMax + ", sslTransMin=" + sslTransMin
				+ ", sslTransAvg=" + sslTransAvg + ", sslTransCurr="
				+ sslTransCurr + ", connList=" + connList + ", connMax="
				+ connMax + ", connMin=" + connMin + ", connAvg=" + connAvg
				+ ", connCurr=" + connCurr + ", throughputList="
				+ throughputList + ", bpsMax=" + bpsMax + ", bpsMin=" + bpsMin
				+ ", bpsAvg=" + bpsAvg + ", bpsCurr=" + bpsCurr + ", ppsMax="
				+ ppsMax + ", ppsMin=" + ppsMin + ", ppsAvg=" + ppsAvg
				+ ", ppsCurr=" + ppsCurr + "]";
	}

	public ArrayList<OBDtoAdcPerfHttpReq> getHttpReqList()
	{
		return httpReqList;
	}

	public void setHttpReqList(ArrayList<OBDtoAdcPerfHttpReq> httpReqList)
	{
		this.httpReqList = httpReqList;
	}

	public Long getHttpReqMax()
	{
		return httpReqMax;
	}

	public void setHttpReqMax(Long httpReqMax)
	{
		this.httpReqMax = httpReqMax;
	}

	public Long getHttpReqMin()
	{
		return httpReqMin;
	}

	public void setHttpReqMin(Long httpReqMin)
	{
		this.httpReqMin = httpReqMin;
	}

	public Long getHttpReqAvg()
	{
		return httpReqAvg;
	}

	public void setHttpReqAvg(Long httpReqAvg)
	{
		this.httpReqAvg = httpReqAvg;
	}

	public Long getHttpReqCurr()
	{
		return httpReqCurr;
	}

	public void setHttpReqCurr(Long httpReqCurr)
	{
		this.httpReqCurr = httpReqCurr;
	}

	public ArrayList<OBDtoAdcPerfSslTrans> getSshTransList()
	{
		return sshTransList;
	}

	public void setSshTransList(ArrayList<OBDtoAdcPerfSslTrans> sshTransList)
	{
		this.sshTransList = sshTransList;
	}

	public Long getSslTransMax()
	{
		return sslTransMax;
	}

	public void setSslTransMax(Long sslTransMax)
	{
		this.sslTransMax = sslTransMax;
	}

	public Long getSslTransMin()
	{
		return sslTransMin;
	}

	public void setSslTransMin(Long sslTransMin)
	{
		this.sslTransMin = sslTransMin;
	}

	public Long getSslTransAvg()
	{
		return sslTransAvg;
	}

	public void setSslTransAvg(Long sslTransAvg)
	{
		this.sslTransAvg = sslTransAvg;
	}

	public Long getSslTransCurr()
	{
		return sslTransCurr;
	}

	public void setSslTransCurr(Long sslTransCurr)
	{
		this.sslTransCurr = sslTransCurr;
	}

	public ArrayList<OBDtoAdcPerfConnection> getConnList()
	{
		return connList;
	}

	public void setConnList(ArrayList<OBDtoAdcPerfConnection> connList)
	{
		this.connList = connList;
	}

	public Long getConnMax()
	{
		return connMax;
	}

	public void setConnMax(Long connMax)
	{
		this.connMax = connMax;
	}

	public Long getConnMin()
	{
		return connMin;
	}

	public void setConnMin(Long connMin)
	{
		this.connMin = connMin;
	}

	public Long getConnAvg()
	{
		return connAvg;
	}

	public void setConnAvg(Long connAvg)
	{
		this.connAvg = connAvg;
	}

	public Long getConnCurr()
	{
		return connCurr;
	}

	public void setConnCurr(Long connCurr)
	{
		this.connCurr = connCurr;
	}

	public ArrayList<OBDtoAdcPerfTroughput> getThroughputList()
	{
		return throughputList;
	}

	public void setThroughputList(ArrayList<OBDtoAdcPerfTroughput> throughputList)
	{
		this.throughputList = throughputList;
	}

	public Long getBpsMax()
	{
		return bpsMax;
	}

	public void setBpsMax(Long bpsMax)
	{
		this.bpsMax = bpsMax;
	}

	public Long getBpsMin()
	{
		return bpsMin;
	}

	public void setBpsMin(Long bpsMin)
	{
		this.bpsMin = bpsMin;
	}

	public Long getBpsAvg()
	{
		return bpsAvg;
	}

	public void setBpsAvg(Long bpsAvg)
	{
		this.bpsAvg = bpsAvg;
	}

	public Long getBpsCurr()
	{
		return bpsCurr;
	}

	public void setBpsCurr(Long bpsCurr)
	{
		this.bpsCurr = bpsCurr;
	}

	public Long getPpsMax()
	{
		return ppsMax;
	}

	public void setPpsMax(Long ppsMax)
	{
		this.ppsMax = ppsMax;
	}

	public Long getPpsMin()
	{
		return ppsMin;
	}

	public void setPpsMin(Long ppsMin)
	{
		this.ppsMin = ppsMin;
	}

	public Long getPpsAvg()
	{
		return ppsAvg;
	}

	public void setPpsAvg(Long ppsAvg)
	{
		this.ppsAvg = ppsAvg;
	}

	public Long getPpsCurr()
	{
		return ppsCurr;
	}

	public void setPpsCurr(Long ppsCurr)
	{
		this.ppsCurr = ppsCurr;
	}	
}
