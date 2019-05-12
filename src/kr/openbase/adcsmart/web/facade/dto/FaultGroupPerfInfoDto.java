package kr.openbase.adcsmart.web.facade.dto;
import kr.openbase.adcsmart.service.dto.OBDtoTargetObject;

public class FaultGroupPerfInfoDto
{
	private OBDtoTargetObject  object;
	private String  groupDbIndex;
	private String  groupId;
	private String 	groupName;
	private Integer memberCount;
	private Integer groupStatus;
	private String bpsIn;
	private String bpsOut;
	private	String bpsTotal;
	private String connCurr;

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
	public String getBpsIn()
	{
		return bpsIn;
	}
	public void setBpsIn(String bpsIn)
	{
		this.bpsIn = bpsIn;
	}
	public String getBpsOut()
	{
		return bpsOut;
	}
	public void setBpsOut(String bpsOut)
	{
		this.bpsOut = bpsOut;
	}
	public String getBpsTotal()
	{
		return bpsTotal;
	}
	public void setBpsTotal(String bpsTotal)
	{
		this.bpsTotal = bpsTotal;
	}
	public String getConnCurr()
	{
		return connCurr;
	}
	public void setConnCurr(String connCurr)
	{
		this.connCurr = connCurr;
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
	public OBDtoTargetObject getObject()
	{
		return object;
	}
	public void setObject(OBDtoTargetObject object)
	{
		this.object = object;
	}
	@Override
	public String toString()
	{
		return "FaultGroupPerfInfoDto [object=" + object + ", groupDbIndex=" + groupDbIndex + ", groupId=" + groupId + ", groupName=" + groupName + ", memberCount=" + memberCount + ", groupStatus=" + groupStatus + ", bpsIn=" + bpsIn + ", bpsOut=" + bpsOut + ", bpsTotal=" + bpsTotal + ", connCurr=" + connCurr + "]";
	}
}