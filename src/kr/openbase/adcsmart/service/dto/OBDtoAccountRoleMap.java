/*
 * a 사용자 계정-역할 매핑.  각 역할에 할당된 사용자 계정 정보.
 */
package kr.openbase.adcsmart.service.dto;

import java.util.*;

import kr.openbase.adcsmart.service.dto.OBDtoAccountRole;

public class OBDtoAccountRoleMap
{
	private OBDtoAccountRole roleInfo;
	private ArrayList<OBDtoAccount> accountList;// = new ArrayList<OBDtoAccount>();

	public void setRoleInfo(OBDtoAccountRole roleInfo)
	{
		this.roleInfo = roleInfo;
	}
	public OBDtoAccountRole getRoleInfo()
	{
		return this.roleInfo;
	}

	public void setAccountList(ArrayList<OBDtoAccount> accountList)
	{
		this.accountList = accountList;
	}
	public ArrayList<OBDtoAccount> getAccountList()
	{
		return this.accountList;
	}
}
