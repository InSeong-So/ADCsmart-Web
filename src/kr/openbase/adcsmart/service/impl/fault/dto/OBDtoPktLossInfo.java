package kr.openbase.adcsmart.service.impl.fault.dto;

import java.util.ArrayList;

public class OBDtoPktLossInfo
{
	private int lossPktCount = 0;
	private int totalPktCount = 0;
	private int validPktCount = 0;
	private ArrayList<OBDtoFaultCheckPacketLossInfo> pktInfoList;
	@Override
	public String toString()
	{
		return String.format("OBDtoPktLossInfo [lossPktCount=%s, totalPktCount=%s, validPktCount=%s, pktInfoList=%s]", lossPktCount, totalPktCount, validPktCount, pktInfoList);
	}
	public int getTotalPktCount()
	{
		return totalPktCount;
	}
	public void setTotalPktCount(int totalPktCount)
	{
		this.totalPktCount = totalPktCount;
	}
	public int getValidPktCount()
	{
		return validPktCount;
	}
	public void setValidPktCount(int validPktCount)
	{
		this.validPktCount = validPktCount;
	}
	public int getLossPktCount()
	{
		return lossPktCount;
	}
	public void setLossPktCount(int lossPktCount)
	{
		this.lossPktCount = lossPktCount;
	}
	public ArrayList<OBDtoFaultCheckPacketLossInfo> getPktInfoList()
	{
		return pktInfoList;
	}
	public void setPktInfoList(ArrayList<OBDtoFaultCheckPacketLossInfo> pktInfoList)
	{
		this.pktInfoList = pktInfoList;
	} 
}
