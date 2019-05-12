package kr.openbase.adcsmart.service.impl.fault.alteon.dto;

public class OBDtoSWKeyAlteon
{
	private	Long	license=0L;
	private Long	peakUsage=0L;
	private Long    currentUsage=0L;
	@Override
	public String toString()
	{
		return String.format("OBDtoSWKeyAlteon [license=%s, peakUsage=%s, currentUsage=%s]", license, peakUsage, currentUsage);
	}
	public Long getLicense()
	{
		return license;
	}
	public void setLicense(Long license)
	{
		this.license = license;
	}
	public Long getPeakUsage()
	{
		return peakUsage;
	}
	public void setPeakUsage(Long peakUsage)
	{
		this.peakUsage = peakUsage;
	}
	public Long getCurrentUsage()
	{
		return currentUsage;
	}
	public void setCurrentUsage(Long currentUsage)
	{
		this.currentUsage = currentUsage;
	}
}
