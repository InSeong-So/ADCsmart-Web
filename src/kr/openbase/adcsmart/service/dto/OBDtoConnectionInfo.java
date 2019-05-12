package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;

public class OBDtoConnectionInfo
{
	private Date maxDate;
	private Long maxConns;
	private Date minDate;
	private Long minConns;
	private Long avgConns;
	private ArrayList<OBDtoConnectionData> data;
	private ArrayList<OBDtoAdcConfigHistory> confEventList;
	public ArrayList<OBDtoAdcConfigHistory> getConfEventList()
	{
		return confEventList;
	}
	public void setConfEventList(ArrayList<OBDtoAdcConfigHistory> confEventList)
	{
		this.confEventList = confEventList;
	}
	@Override
	public String toString()
	{
		return "OBDtoConnectionInfo [maxDate=" + maxDate + ", maxConns="
				+ maxConns + ", minDate=" + minDate + ", minConns=" + minConns
				+ ", avgConns=" + avgConns + ", data=" + data
				+ ", confEventList=" + confEventList + "]";
	}
	public Date getMaxDate()
	{
		return maxDate;
	}
	public void setMaxDate(Date maxDate)
	{
		this.maxDate = maxDate;
	}
	public Long getMaxConns()
	{
		return maxConns;
	}
	public void setMaxConns(Long maxConns)
	{
		this.maxConns = maxConns;
	}
	public Date getMinDate()
	{
		return minDate;
	}
	public void setMinDate(Date minDate)
	{
		this.minDate = minDate;
	}
	public Long getMinConns()
	{
		return minConns;
	}
	public void setMinConns(Long minConns)
	{
		this.minConns = minConns;
	}
	public Long getAvgConns()
	{
		return avgConns;
	}
	public void setAvgConns(Long avgConns)
	{
		this.avgConns = avgConns;
	}
	public ArrayList<OBDtoConnectionData> getData()
	{
		return data;
	}
	public void setData(ArrayList<OBDtoConnectionData> data)
	{
		this.data = data;
	}
}
