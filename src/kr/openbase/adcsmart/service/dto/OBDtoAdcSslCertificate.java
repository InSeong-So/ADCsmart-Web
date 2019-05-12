package kr.openbase.adcsmart.service.dto;

import java.sql.Timestamp;

public class OBDtoAdcSslCertificate
{
	private String index;
	private Integer adcIndex;
	private String certificateName;
	private String commonName;
	private String organizationName;
	private Timestamp expirationDate;
	
	@Override
	public String toString()
	{
		return "OBDtoAdcSslCertificate [index=" + index + ", adcIndex="
				+ adcIndex + ", certificateName=" + certificateName
				+ ", commonName=" + commonName + ", organizationName="
				+ organizationName + ", expirationDate=" + expirationDate + "]";
	}
	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
	{
		this.index = index;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getCertificateName()
	{
		return certificateName;
	}
	public void setCertificateName(String certificateName)
	{
		this.certificateName = certificateName;
	}
	public String getCommonName()
	{
		return commonName;
	}
	public void setCommonName(String commonName)
	{
		this.commonName = commonName;
	}
	public String getOrganizationName()
	{
		return organizationName;
	}
	public void setOrganizationName(String organizationName)
	{
		this.organizationName = organizationName;
	}
	public Timestamp getExpirationDate()
	{
		return expirationDate;
	}
	public void setExpirationDate(Timestamp expirationDate)
	{
		this.expirationDate = expirationDate;
	}
}