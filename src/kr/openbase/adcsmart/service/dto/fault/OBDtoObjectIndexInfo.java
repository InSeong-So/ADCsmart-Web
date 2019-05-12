package kr.openbase.adcsmart.service.dto.fault;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;

public class OBDtoObjectIndexInfo
{
	private OBDtoADCObject 	obj;
	private Long 			logKey;
	
	@Override
	public String toString()
	{
		return "OBDtoFaultCheckIndexInfo [obj=" + obj + ", logKey=" + logKey + "]";
	}
	public OBDtoADCObject getObj()
	{
		return obj;
	}
	public void setObj(OBDtoADCObject obj)
	{
		this.obj = obj;
	}
	public Long getLogKey()
	{
		return logKey;
	}
	public void setLogKey(Long logKey)
	{
		this.logKey = logKey;
	}
}
