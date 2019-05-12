package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;

public class FaultGroupMemberPerfInfoDto
{
	private FaultGroupPerfInfoDto groupInfo;
	private ArrayList<FaultRealInfoDto> memberList = new ArrayList<FaultRealInfoDto>();
	public FaultGroupPerfInfoDto getGroupInfo()
	{
		return groupInfo;
	}
	public void setGroupInfo(FaultGroupPerfInfoDto groupInfo)
	{
		this.groupInfo = groupInfo;
	}
	public ArrayList<FaultRealInfoDto> getMemberList()
	{
		return memberList;
	}
	public void setMemberList(ArrayList<FaultRealInfoDto> memberList)
	{
		this.memberList = memberList;
	}
	@Override
	public String toString()
	{
		return "FaultGroupMemberPerfInfoDto [groupInfo=" + groupInfo + ", memberList=" + memberList + "]";
	}	
}
