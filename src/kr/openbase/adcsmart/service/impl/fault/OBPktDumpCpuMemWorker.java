package kr.openbase.adcsmart.service.impl.fault;

import java.sql.Timestamp;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.impl.dto.OBDtoCpuMemStatus;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoPktdumpInfo;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBPktDumpCpuMemWorker implements Runnable
{
	private OBDtoPktdumpInfo threadInfo;
	
	public OBPktDumpCpuMemWorker(OBDtoPktdumpInfo threadInfo)
	{
		setThreadInfo(threadInfo);
	}
	
	public void setThreadInfo(OBDtoPktdumpInfo threadInfo)
	{
		this.threadInfo = threadInfo;
	}
	
	public OBDtoPktdumpInfo getThreadInfo()
	{
		return threadInfo;
	}
	
	@Override
	public void run()
	{
		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d] start. try to get cpu/mem info for pkt dump", this.threadInfo.getAdcIndex()));
		OBDatabase db = new OBDatabase();
		try
		{
			db.openDB();
			
			OBFaultMngImpl mngClass = new OBFaultMngImpl();
			OBDtoADCObject object = new OBDtoADCObject();
			object.setCategory(OBDtoADCObject.CATEGORY_ADC);
			object.setIndex(this.threadInfo.getAdcIndex());
			int reTryCount = 0;
			while(true)
			{
				OBDateTime.Sleep(1000);
				if(mngClass.isPktdumpCancelStopped(this.threadInfo.getLogIndex(), db)==true)
					break;
				try
				{
					Integer statusFlag = mngClass.getPktdumpStatusFlag(this.threadInfo.getLogIndex(), db);
					if(statusFlag>=OBDtoPktdumpInfo.STATUS_SUCCESS)
						break;
					OBDtoCpuMemStatus cpuMem = mngClass.getFaultAdcCpuMemoryUsage(object);
					Timestamp occurTime = OBDateTime.toTimestamp(OBDateTime.now());
					new OBFaultMonitoringDB().writeCpuMemStatus(this.threadInfo.getAdcIndex(), occurTime, cpuMem);
					reTryCount=0;
				}
				catch(OBException e)
				{// 일시적인 현상으로 간주하고 계속해서 retry한다.
					reTryCount++;
					if(reTryCount==10)
						break;// 10회 연속 실패하면 종료한다.
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d] failed to get cpu/mem info for pkt dump. %s", this.threadInfo.getAdcIndex(), e.getErrorMessage()));
				}
			}
		}
		catch(OBException e)
		{
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d] failed to get cpu/mem info for pkt dump. %s", this.threadInfo.getAdcIndex(), e.getErrorMessage()));
			return;
		}
		catch(Exception e)
		{
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d] failed to get cpu/mem info for pkt dump. %s", this.threadInfo.getAdcIndex(), new OBUtility().getStackTrace()));
			return;
		}
		finally
		{
			if(db!=null) db.closeDB();
		}
		
		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d] end", this.threadInfo.getAdcIndex()));
	}
}
