package kr.openbase.adcsmart.service.impl;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.OBNoticeMessage;
import kr.openbase.adcsmart.service.dto.OBDtoNoticeInfo;
import kr.openbase.adcsmart.service.utility.OBException;

public class OBNoticeMessageImpl implements OBNoticeMessage
{

	@Override
	public ArrayList<OBDtoNoticeInfo> getNoticeMessage(Integer accountIndex) throws OBException
	{
		ArrayList<OBDtoNoticeInfo> retVal = new ArrayList<OBDtoNoticeInfo>();
		return retVal;
	}

	@Override
	public void addNoticeMessage(Integer accountIndex, String message) throws OBException
	{
		// TODO Auto-generated method stub

	}

}
