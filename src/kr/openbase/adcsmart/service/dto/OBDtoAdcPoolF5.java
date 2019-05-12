package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.impl.f5.DtoPoolMember;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

import java.io.Serializable;

public class OBDtoAdcPoolF5 implements Serializable, Cloneable
{
	private static final long serialVersionUID = 10L;
	private String index;
	private String name;
	private Integer lbMethod;
	private Integer healthCheck;
	private ArrayList<OBDtoAdcPoolMemberF5> memberList;
	private ArrayList<OBDtoAdcPoolMemberF5> selectMemberList;
	private ArrayList<DtoPoolMember> dtoMemberList;
	

	public OBDtoAdcPoolF5()
	{
	}
	public OBDtoAdcPoolF5(OBDtoAdcPoolF5 obj)
	{
		if(obj!=null)
		{
			this.index = obj.getIndex();
			this.name = obj.getName();
			this.lbMethod = obj.getLbMethod();
			this.healthCheck = obj.getHealthCheck();
			this.selectMemberList = new ArrayList<OBDtoAdcPoolMemberF5>();
			if(obj.getSelectMemberList() != null)
			{
		         for(OBDtoAdcPoolMemberF5 obj2: obj.getSelectMemberList())
		            {
		                if(obj2!=null)
		                {
		                    OBDtoAdcPoolMemberF5 temp = new OBDtoAdcPoolMemberF5(obj2);
		                    this.selectMemberList.add(temp);
		                }
		            }
			}
			
	        this.memberList = new ArrayList<OBDtoAdcPoolMemberF5>();
            for(OBDtoAdcPoolMemberF5 obj2: obj.getMemberList())
            {
                if(obj2!=null)
                {
                    OBDtoAdcPoolMemberF5 temp = new OBDtoAdcPoolMemberF5(obj2);
                    this.memberList.add(temp);
                }
            }
		}
	}

    public boolean configEquals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        OBDtoAdcPoolF5 other = (OBDtoAdcPoolF5) obj;
        if(healthCheck == null)
        {
            if(other.healthCheck != null)
                return false;
        }
        else if(!healthCheck.equals(other.healthCheck))
            return false;
        if(lbMethod == null)
        {
            if(other.lbMethod != null)
                return false;
        }
        else if(!lbMethod.equals(other.lbMethod))
            return false;
        if(memberList == null)
        {
            if(other.memberList != null)
                return false;
        }
        else if(!memberListEquals(this.memberList, other.memberList))
            return false;
        OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "MEMBER FIN");
        if(name == null)
        {
            if(other.name != null)
                return false;
        }
        else if(!name.equals(other.name))
            return false;
        OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "NAME FIN");
        return true;
    }

    private boolean memberListEquals(ArrayList<OBDtoAdcPoolMemberF5> thisList, ArrayList<OBDtoAdcPoolMemberF5> otherList)
    {
        //비교전 보정. 장비와 DB 구성 방식이 다를 수 있음
        if(thisList.isEmpty()) thisList = null;
        if(otherList.isEmpty()) otherList = null;

        if(thisList==otherList)
            return true;

        if((thisList!=null && otherList==null) || (thisList==null && otherList!=null))
            return false;

        int i, j;
        boolean bMatched;
        ArrayList<OBDtoAdcPoolMemberF5> poolMemberCommonList = new ArrayList<OBDtoAdcPoolMemberF5>();
        OBDtoAdcPoolMemberF5 thisMember = null;
        OBDtoAdcPoolMemberF5 otherMember = null;
        OBDtoAdcPoolMemberF5 commonMember = null;
        for(i = 0; i < thisList.size(); i++)
        {
            bMatched = false;
            thisMember = thisList.get(i);

            for(j = 0; j < otherList.size(); j++)
            {
                otherMember = otherList.get(j);
                if(thisMember.getIpAddress().equals(otherMember.getIpAddress()) && 
                   thisMember.getPort().equals(otherMember.getPort()) &&
                   thisMember.getState().equals(otherMember.getState())
                   )
                {
                    bMatched = true;
                    break;
                }
            }
            if(bMatched == true)
            {
                poolMemberCommonList.add(new OBDtoAdcPoolMemberF5(thisMember));
            }
            else
            {
                OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "diff1");
                return false;
            }
        }

        for(i = 0; i < otherList.size(); i++)
        {
            bMatched = false;
            otherMember = otherList.get(i);

            for(j = 0; j < poolMemberCommonList.size(); j++)
            {
                commonMember = poolMemberCommonList.get(j);
                if(otherMember.getIpAddress().equals(commonMember.getIpAddress()) && otherMember.getPort().equals(commonMember.getPort()))
                {
                    bMatched = true;
                    break;
                }
            }
            if(bMatched == false) // thisList에는 없는 멤버
            {
                OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "diff2");
                return false;
            }
        }
        poolMemberCommonList = null;// 쓴 것 비우기

        return true;
    }

    @Override
    public String toString()
    {
        return "OBDtoAdcPoolF5 [index=" + index + ", name=" + name + ", lbMethod=" + lbMethod + ", healthCheck=" + healthCheck + ", memberList=" + memberList + ", selectMemberList=" + selectMemberList + ", dtoMemberList=" + dtoMemberList + "]";
    }

	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
	{
		this.index = index;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Integer getLbMethod()
	{
		return lbMethod;
	}
	public void setLbMethod(Integer lbMethod)
	{
		this.lbMethod = lbMethod;
	}
	public Integer getHealthCheck()
	{
		return healthCheck;
	}
	public void setHealthCheck(Integer healthCheck)
	{
		this.healthCheck = healthCheck;
	}
	public ArrayList<OBDtoAdcPoolMemberF5> getMemberList()
	{
		return memberList;
	}
	public void setMemberList(ArrayList<OBDtoAdcPoolMemberF5> memberList)
	{
		this.memberList = memberList;
	}
	public ArrayList<DtoPoolMember> getDtoMemberList() {
		return dtoMemberList;
	}
	public void setDtoMemberList(ArrayList<DtoPoolMember> dtoMemberList) {
		this.dtoMemberList = dtoMemberList;
	}
    public ArrayList<OBDtoAdcPoolMemberF5> getSelectMemberList()
    {
        return selectMemberList;
    }
    public void setSelectMemberList(ArrayList<OBDtoAdcPoolMemberF5> selectMemberList)
    {
        this.selectMemberList = selectMemberList;
    }
}
