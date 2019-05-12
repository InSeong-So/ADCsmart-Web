package kr.openbase.adcsmart.service.dto;

public class OBDtoSystemEnv
{
	private OBDtoSystemEnvNetwork 		networkInfo;
	private OBDtoSystemEnvAdditional 	additionalInfo;
	private OBDtoSystemEnvView			viewInfo;
	private OBDtoScheduleBackupInfo 	schBackupInfo;
	@Override
	public String toString()
	{
		return "OBDtoSystemEnv [networkInfo=" + networkInfo
				+ ", additionalInfo=" + additionalInfo + ", viewInfo="
				+ viewInfo + ", schBackupInfo=" + schBackupInfo + "]";
	}
	public OBDtoSystemEnvNetwork getNetworkInfo()
	{
		return networkInfo;
	}
	public OBDtoScheduleBackupInfo getSchBackupInfo()
	{
		return schBackupInfo;
	}
	public void setSchBackupInfo(OBDtoScheduleBackupInfo schBackupInfo)
	{
		this.schBackupInfo = schBackupInfo;
	}
	public void setNetworkInfo(OBDtoSystemEnvNetwork networkInfo)
	{
		this.networkInfo = networkInfo;
	}
	public OBDtoSystemEnvAdditional getAdditionalInfo()
	{
		return additionalInfo;
	}
	public void setAdditionalInfo(OBDtoSystemEnvAdditional additionalInfo)
	{
		this.additionalInfo = additionalInfo;
	}
	public OBDtoSystemEnvView getViewInfo()
	{
		return viewInfo;
	}
	public void setViewInfo(OBDtoSystemEnvView viewInfo)
	{
		this.viewInfo = viewInfo;
	}	
}
