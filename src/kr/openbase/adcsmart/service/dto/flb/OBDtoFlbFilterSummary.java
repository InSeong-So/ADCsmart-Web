package kr.openbase.adcsmart.service.dto.flb;


/**
 * Filter 정보 요약형, 그룹에 관련되어 있는 Filter 정보를 간략하게 보여줄 때 쓴다.
 * 모니터링할 그룹을 선택하는 목록에서 참고하고 있다. 
 */

public class OBDtoFlbFilterSummary
{
	private String  dbIndex;
	private Integer filterId;
	private String  name;
	
	@Override
	public String toString()
	{
		return "OBDtoFlbFilterSummary [dbIndex=" + dbIndex + ", filterId="
				+ filterId + ", name=" + name + "]";
	}
	public String getDbIndex()
	{
		return dbIndex;
	}
	public void setDbIndex(String dbIndex)
	{
		this.dbIndex = dbIndex;
	}
	public Integer getFilterId()
	{
		return filterId;
	}
	public void setFilterId(Integer filterId)
	{
		this.filterId = filterId;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
}
