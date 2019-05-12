package kr.openbase.adcsmart.service.impl.fault.alteon;

import java.util.ArrayList;
import java.util.HashMap;

import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoFaultCheckPacketLossInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoPktLossInfo;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;

class OBAnalPktLossAlteon
{
	//#define TH_FIN  0x01
	//#define TH_SYN  0x02
	//#define TH_RST  0x04
	//#define TH_PUSH 0x08
	//#define TH_ACK  0x10
	//#define TH_URG  0x20
	
	private OBDtoFaultCheckPacketLossInfo isNextPktInfoAvailableSyn(String clientIPAddress, String vsIPAddress, 
			ArrayList<String>realIPAddrList, OBDtoFaultCheckPacketLossInfo pktInfo, 
			HashMap<String, OBDtoFaultCheckPacketLossInfo> pktInfoHashMap) throws OBException
	{
		OBDtoFaultCheckPacketLossInfo retVal = null;
		switch(pktInfo.getDirection())
		{
		case OBDtoFaultCheckPacketLossInfo.DIR_CLNT_REAL:// real->client syn+ack를 확인한다. real이 여러개일 가능성이 있으므로 순환 검사한다.
			for(String realIP: realIPAddrList)
			{
				String hashKey = makePktLossHashKey(realIP, clientIPAddress, 0, 0, 0, pktInfo.getSeqNo()+1L);	
				if(pktInfoHashMap.get(hashKey)!=null)
					return retVal;
			}
			retVal = new OBDtoFaultCheckPacketLossInfo();
			retVal.setDirection(OBDtoFaultCheckPacketLossInfo.DIR_REAL_CLNT);
			retVal.setValid(false);
			retVal.setTimeDiff(0);
			retVal.setDataLength(0);
			retVal.setSummary("SYNACK");
			break;
		case OBDtoFaultCheckPacketLossInfo.DIR_CLNT_VIP:// client->real syn을 확인한다.
			for(String realIP: realIPAddrList)
			{
				String hashKey = makePktLossHashKey(clientIPAddress, realIP, 0, 0, pktInfo.getSeqNo(), 0L);	
				if(pktInfoHashMap.get(hashKey)!=null)
					return retVal;
			}
			retVal = new OBDtoFaultCheckPacketLossInfo();
			retVal.setDirection(OBDtoFaultCheckPacketLossInfo.DIR_CLNT_REAL);
			retVal.setValid(false);
			retVal.setTimeDiff(0);
			retVal.setDataLength(0);
			retVal.setSummary("SYN");
			break;
		case OBDtoFaultCheckPacketLossInfo.DIR_NA:
		case OBDtoFaultCheckPacketLossInfo.DIR_REAL_CLNT:
		case OBDtoFaultCheckPacketLossInfo.DIR_REAL_VIP:
		case OBDtoFaultCheckPacketLossInfo.DIR_VIP_CLNT:
		case OBDtoFaultCheckPacketLossInfo.DIR_VIP_REAL:
		default:
			return retVal;
		}
		return retVal;
	}
	
	// synack 처리..
	private OBDtoFaultCheckPacketLossInfo isNextPktInfoAvailableSynAck(String clientIPAddress, String vsIPAddress, 
			ArrayList<String>realIPAddrList, OBDtoFaultCheckPacketLossInfo pktInfo, 
			HashMap<String, OBDtoFaultCheckPacketLossInfo> pktInfoHashMap) throws OBException
	{
		OBDtoFaultCheckPacketLossInfo retVal = null;
		String hashKey = "";
		switch(pktInfo.getDirection())
		{
		case OBDtoFaultCheckPacketLossInfo.DIR_REAL_CLNT:// vip->client syc+ack를 확인한다. real이 여러개일 가능성이 있으므로 순환 검사한다.
			hashKey = makePktLossHashKey(vsIPAddress, clientIPAddress, 0, 0, 0, pktInfo.getAckNo());	
			if(pktInfoHashMap.get(hashKey)!=null)
				return retVal;
	
			retVal = new OBDtoFaultCheckPacketLossInfo();
			retVal.setDirection(OBDtoFaultCheckPacketLossInfo.DIR_VIP_CLNT);
			retVal.setValid(false);
			retVal.setTimeDiff(0);
			retVal.setDataLength(0);
			retVal.setSummary("SYNACK");
			break;
		case OBDtoFaultCheckPacketLossInfo.DIR_VIP_CLNT:// client->vip ack을 확인한다.
			hashKey = makePktLossHashKey(clientIPAddress, vsIPAddress, 0, 0, pktInfo.getAckNo(), pktInfo.getSeqNo()+1L);	
			if(pktInfoHashMap.get(hashKey)!=null)
				return retVal;

			retVal = new OBDtoFaultCheckPacketLossInfo();
			retVal.setDirection(OBDtoFaultCheckPacketLossInfo.DIR_CLNT_VIP);
			retVal.setValid(false);
			retVal.setTimeDiff(0);
			retVal.setDataLength(0);
			retVal.setSummary("ACK");
			break;
		case OBDtoFaultCheckPacketLossInfo.DIR_NA:
		case OBDtoFaultCheckPacketLossInfo.DIR_CLNT_REAL:
		case OBDtoFaultCheckPacketLossInfo.DIR_REAL_VIP:
		case OBDtoFaultCheckPacketLossInfo.DIR_CLNT_VIP:
		case OBDtoFaultCheckPacketLossInfo.DIR_VIP_REAL:
		default:
			return retVal;
		}
		return retVal;
	}
	
	// pushack 처리..
	private OBDtoFaultCheckPacketLossInfo isNextPktInfoAvailablePushAck(String clientIPAddress, String vsIPAddress, 
			ArrayList<String>realIPAddrList, OBDtoFaultCheckPacketLossInfo pktInfo, 
			HashMap<String, OBDtoFaultCheckPacketLossInfo> pktInfoHashMap) throws OBException
	{
		OBDtoFaultCheckPacketLossInfo retVal = null;
		String hashKey = "";
		switch(pktInfo.getDirection())
		{
		case OBDtoFaultCheckPacketLossInfo.DIR_REAL_CLNT:// vip->client push+ack를 확인한다. 
			hashKey = makePktLossHashKey(vsIPAddress, clientIPAddress, 0, 0, pktInfo.getSeqNo(), pktInfo.getAckNo());	
			if(pktInfoHashMap.get(hashKey)!=null)
				return retVal;
	
			retVal = new OBDtoFaultCheckPacketLossInfo();
			retVal.setDirection(OBDtoFaultCheckPacketLossInfo.DIR_VIP_CLNT);
			retVal.setValid(false);
			retVal.setTimeDiff(0);
			retVal.setDataLength(pktInfo.getDataLength());
			retVal.setSummary("PUSHACK");
			break;

		case OBDtoFaultCheckPacketLossInfo.DIR_CLNT_REAL:// real->client pushack 확인. real이 여러개일 가능성이 있으므로 순환 검사한다.
			for(String realIP: realIPAddrList)
			{
				hashKey = makePktLossHashKey(realIP, clientIPAddress, 0, 0, pktInfo.getAckNo(), pktInfo.getSeqNo()+pktInfo.getDataLength());	
				if(pktInfoHashMap.get(hashKey)!=null)
					return retVal;
			}

			retVal = new OBDtoFaultCheckPacketLossInfo();
			retVal.setDirection(OBDtoFaultCheckPacketLossInfo.DIR_REAL_CLNT);
			retVal.setValid(false);
			retVal.setTimeDiff(0);
			retVal.setDataLength(pktInfo.getDataLength());
			retVal.setSummary("PUSHACK");
			break;
		case OBDtoFaultCheckPacketLossInfo.DIR_CLNT_VIP:// client->real pushack 확인. real이 여러개일 가능성이 있으므로 순환 검사한다.
			for(String realIP: realIPAddrList)
			{
				hashKey = makePktLossHashKey(clientIPAddress, realIP, 0, 0, pktInfo.getSeqNo(), pktInfo.getAckNo());	
				if(pktInfoHashMap.get(hashKey)!=null)
					return retVal;
			}

			retVal = new OBDtoFaultCheckPacketLossInfo();
			retVal.setDirection(OBDtoFaultCheckPacketLossInfo.DIR_CLNT_REAL);
			retVal.setValid(false);
			retVal.setTimeDiff(0);
			retVal.setDataLength(pktInfo.getDataLength());
			retVal.setSummary("PUSHACK");
			break;
		case OBDtoFaultCheckPacketLossInfo.DIR_NA:
		case OBDtoFaultCheckPacketLossInfo.DIR_VIP_CLNT:
		case OBDtoFaultCheckPacketLossInfo.DIR_REAL_VIP:
		case OBDtoFaultCheckPacketLossInfo.DIR_VIP_REAL:
		default:
			return retVal;
		}
		return retVal;
	}
		
	private String makePktLossHashKey(String srcIPAddr, String dstIPAddr, Integer srcPort, Integer dstPort, long seqNo, long ackNo) throws OBException
	{// alteon은 sequecnce 번호가 같음.
		// sip+sport+dip+dport+seq+ack
		String retVal = String.format("%s_%x_%s_%x_%x_%x", srcIPAddr, srcPort, dstIPAddr, dstPort, seqNo, ackNo);
		return retVal;
	}
	
	private OBDtoFaultCheckPacketLossInfo isNextPktInfoAvailable(String clientIPAddress, String vsIPAddress, 
								ArrayList<String>realIPAddrList, OBDtoFaultCheckPacketLossInfo pktInfo, HashMap<String, OBDtoFaultCheckPacketLossInfo> pktInfoHashMap) throws OBException
	{
		OBDtoFaultCheckPacketLossInfo retVal = null;
		switch(pktInfo.getTcpFlag())
		{
		case OBDefine.TH_SYN:
			return isNextPktInfoAvailableSyn(clientIPAddress, vsIPAddress, realIPAddrList, pktInfo, pktInfoHashMap);
		case OBDefine.TH_SYN+OBDefine.TH_ACK:
			return isNextPktInfoAvailableSynAck(clientIPAddress, vsIPAddress, realIPAddrList, pktInfo, pktInfoHashMap);
		case OBDefine.TH_ACK+OBDefine.TH_PUSH:
		case OBDefine.TH_PUSH:
			return isNextPktInfoAvailablePushAck(clientIPAddress, vsIPAddress, realIPAddrList, pktInfo, pktInfoHashMap);
		case OBDefine.TH_ACK:
			break;
		case OBDefine.TH_FIN:
			break;
		case OBDefine.TH_FIN+OBDefine.TH_ACK:
			break;
		case OBDefine.TH_RST:
			break;
		case OBDefine.TH_RST+OBDefine.TH_ACK:
			break;
		}
		return retVal;
	}
	
	private String getTcpFlagInfo(OBDtoFaultCheckPacketLossInfo pktInfo)
	{
		switch(pktInfo.getTcpFlag())
		{
		case OBDefine.TH_SYN:
			return "SYN";
		case OBDefine.TH_SYN+OBDefine.TH_ACK:
			return "SYNACK";
		case OBDefine.TH_ACK+OBDefine.TH_PUSH:
			return "PUSHACK";
		case OBDefine.TH_PUSH:
			return "PUSH";
		case OBDefine.TH_ACK:
			return "ACK";
		case OBDefine.TH_FIN:
			return "FIN";
		case OBDefine.TH_FIN+OBDefine.TH_ACK:
			return "FINACK";
		case OBDefine.TH_RST:
			return "RST";
		case OBDefine.TH_RST+OBDefine.TH_ACK:
			return "RSTACK";
		default:
			return "";
		}
	}

	private static final  int	MAX_LOSS_PKT_COUNT		=100;
	
	OBDtoPktLossInfo analyzePacketLoss(String clientIPAddress, String vsIPAddress, ArrayList<String>realIPAddrList, ArrayList<OBDtoFaultCheckPacketLossInfo> readInfoList) throws OBException
	{// alteon은 sequecnce 번호가 같음.
		OBDtoPktLossInfo retVal = new OBDtoPktLossInfo();
		ArrayList<OBDtoFaultCheckPacketLossInfo> pktInfoList = new ArrayList<OBDtoFaultCheckPacketLossInfo>();
		
		HashMap<String, OBDtoFaultCheckPacketLossInfo> pktInfoHashMap = makePacketLossHashMap(readInfoList);
		int lossPktCount = 0;
		int totalPktCount = 0;
		int validPktCount = 0;
		OBDtoFaultCheckPacketLossInfo prevInfo = null;
		for(OBDtoFaultCheckPacketLossInfo pktInfo:readInfoList)
		{// 현재 입력받은 패킷의 다음 패킷이 있는지 검사한다.
			if(prevInfo!=null)
			{
				int timeDiff = (int) (pktInfo.getRcvTime().getTime()-prevInfo.getRcvTime().getTime());
				pktInfo.setTimeDiff(timeDiff); //microsecond 단위
			}
			prevInfo = pktInfo;
			if(pktInfo.getDirection() == OBDtoFaultCheckPacketLossInfo.DIR_NA)
				continue;
			
			if(pktInfo.getSummary()==null || pktInfo.getSummary().isEmpty())
				pktInfo.setSummary(getTcpFlagInfo(pktInfo));
			
			pktInfoList.add(pktInfo);
			
			OBDtoFaultCheckPacketLossInfo lossInfo = isNextPktInfoAvailable(clientIPAddress, vsIPAddress, realIPAddrList, pktInfo, pktInfoHashMap);
			if(lossInfo!=null)
			{// 신규 정보를 추가한다.
				lossPktCount++;
				pktInfoList.add(lossInfo);
			}
			else
			{
				validPktCount++;
			}
			totalPktCount++;
			
			if(totalPktCount>=MAX_LOSS_PKT_COUNT)
				break;
		}
		
		retVal.setLossPktCount(lossPktCount);
		retVal.setTotalPktCount(totalPktCount);
		retVal.setValidPktCount(validPktCount);
		retVal.setPktInfoList(pktInfoList);
		return retVal;
	}
	
	private HashMap<String, OBDtoFaultCheckPacketLossInfo> makePacketLossHashMap(ArrayList<OBDtoFaultCheckPacketLossInfo> readInfoList) throws OBException
	{// alteon은 sequecnce 번호가 같음.
		// sip+sport+dip+dport+seq+ack
		HashMap<String, OBDtoFaultCheckPacketLossInfo> retVal = new HashMap<String, OBDtoFaultCheckPacketLossInfo>();
		for(OBDtoFaultCheckPacketLossInfo pktInfo:readInfoList)
		{
			String hashKey = makePktLossHashKey(pktInfo);
			retVal.put(hashKey, pktInfo);
		}
		return retVal;
	}
	
	private String makePktLossHashKey(OBDtoFaultCheckPacketLossInfo pktInfo) throws OBException
	{// alteon은 sequecnce 번호가 같음.
		// sip+sport+dip+dport+seq+ack
		String retVal = "";
		switch(pktInfo.getTcpFlag())
		{
		case OBDefine.TH_SYN:
			retVal = String.format("%s_%x_%s_%x_%x_%x", pktInfo.getSrcIPAddress(), 0,
					   pktInfo.getDstIPAddress(), 0,
					   pktInfo.getSeqNo(), pktInfo.getAckNo());
			
			return retVal;
		case OBDefine.TH_SYN+OBDefine.TH_ACK:
			retVal = String.format("%s_%x_%s_%x_%x_%x", pktInfo.getSrcIPAddress(), 0,
					   pktInfo.getDstIPAddress(), 0,
					   0, pktInfo.getAckNo());
			return retVal;
		default:
			retVal = String.format("%s_%x_%s_%x_%x_%x", pktInfo.getSrcIPAddress(), 0,
					   pktInfo.getDstIPAddress(), 0,
					   pktInfo.getSeqNo(), pktInfo.getAckNo());
			return retVal;
		}
	}
}
