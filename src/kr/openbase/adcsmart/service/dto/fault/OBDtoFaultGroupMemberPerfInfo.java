package kr.openbase.adcsmart.service.dto.fault;

import java.util.ArrayList;

public class OBDtoFaultGroupMemberPerfInfo
{
	private OBDtoFaultGroupPerfInfo groupInfo;
	private ArrayList<OBDtoFaultRealInfo> memberList = new ArrayList<OBDtoFaultRealInfo>();
	@Override
	public String toString()
	{
		return "OBDtoFaultGroupMemberPerfInfo [groupInfo=" + groupInfo
				+ ", memberList=" + memberList + "]";
	}
	public OBDtoFaultGroupPerfInfo getGroupInfo()
	{
		return groupInfo;
	}
	public void setGroupInfo(OBDtoFaultGroupPerfInfo groupInfo)
	{
		this.groupInfo = groupInfo;
	}
	public ArrayList<OBDtoFaultRealInfo> getMemberList()
	{
		return memberList;
	}
	public void setMemberList(ArrayList<OBDtoFaultRealInfo> memberList)
	{
		this.memberList = memberList;
	}
}
