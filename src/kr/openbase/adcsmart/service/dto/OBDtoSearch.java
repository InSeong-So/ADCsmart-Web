/**
 * 검색 기능을 위한 검색 조건을 담기 위한 객체.
 */
package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoSearch
{
	private String  	searchKey;
	private Date 		fromTime;
	private Date 		toTime;
	private Date        preFromTime;
	private Date        preToTime;
	
	private Integer		beginIndex;
	private Integer 	endIndex;
	public String getSearchKey()
	{
		return searchKey;
	}
	public void setSearchKey(String searchKey)
	{
		this.searchKey = searchKey;
	}
	public Date getFromTime()
	{
		return fromTime;
	}
	public void setFromTime(Date fromTime)
	{
		this.fromTime = fromTime;
	}
	public Date getToTime()
	{
		return toTime;
	}
	public void setToTime(Date toTime)
	{
		this.toTime = toTime;
	}
	public Integer getBeginIndex()
	{
		return beginIndex;
	}
	public void setBeginIndex(Integer beginIndex)
	{
		this.beginIndex = beginIndex;
	}
	public Integer getEndIndex()
	{
		return endIndex;
	}
	public void setEndIndex(Integer endIndex)
	{
		this.endIndex = endIndex;
	}
    public Date getPreFromTime()
    {
        return preFromTime;
    }
    public void setPreFromTime(Date preFromTime)
    {
        this.preFromTime = preFromTime;
    }
    public Date getPreToTime()
    {
        return preToTime;
    }
    public void setPreToTime(Date preToTime)
    {
        this.preToTime = preToTime;
    }
}
