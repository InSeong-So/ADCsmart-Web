package kr.openbase.adcsmart.service.dto.fault;

import kr.openbase.adcsmart.service.dto.OBDtoTargetObject;

public class OBDtoFaultGroupPerfInfo
{
	private OBDtoTargetObject  object;
	private String  groupDbIndex;
	private String  groupId;
	private String 	groupName;
	private Integer memberCount;
	private Integer groupStatus;
	private Long bpsIn;
	private Long bpsOut;
	private	Long bpsTotal;
	private Long connCurr;


	@Override
	public String toString()
	{
		return "OBDtoFaultGroupPerfInfo [object=" + object + ", groupDbIndex="
				+ groupDbIndex + ", groupId=" + groupId + ", groupName="
				+ groupName + ", memberCount=" + memberCount + ", groupStatus="
				+ groupStatus + ", bpsIn=" + bpsIn + ", bpsOut=" + bpsOut
				+ ", bpsTotal=" + bpsTotal + ", connCurr=" + connCurr + "]";
	}
	public OBDtoTargetObject getObject()
	{
		return object;
	}
	public void setObject(OBDtoTargetObject object)
	{
		this.object = object;
	}
	public String getGroupDbIndex()
	{
		return groupDbIndex;
	}
	public void setGroupDbIndex(String groupDbIndex)
	{
		this.groupDbIndex = groupDbIndex;
	}
	public String getGroupId()
	{
		return groupId;
	}
	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}
	public String getGroupName()
	{
		return groupName;
	}
	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}
	public Integer getMemberCount()
	{
		return memberCount;
	}
	public void setMemberCount(Integer memberCount)
	{
		this.memberCount = memberCount;
	}
	public Integer getGroupStatus()
	{
		return groupStatus;
	}
	public void setGroupStatus(Integer groupStatus)
	{
		this.groupStatus = groupStatus;
	}
	public Long getBpsIn()
	{
		return bpsIn;
	}
	public void setBpsIn(Long bpsIn)
	{
		this.bpsIn = bpsIn;
	}
	public Long getBpsOut()
	{
		return bpsOut;
	}
	public void setBpsOut(Long bpsOut)
	{
		this.bpsOut = bpsOut;
	}
	public Long getBpsTotal()
	{
		return bpsTotal;
	}
	public void setBpsTotal(Long bpsTotal)
	{
		this.bpsTotal = bpsTotal;
	}
	public Long getConnCurr()
	{
		return connCurr;
	}
	public void setConnCurr(Long connCurr)
	{
		this.connCurr = connCurr;
	}
}
