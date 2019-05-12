package kr.openbase.adcsmart.service.impl.dto;

import java.sql.Timestamp;

public class OBDtoVersionInfo
{
	private String 	  productCode;
	private String    productName;
	private String 	  productModel;
	private String    version;
	private int       revision;
	private int       updateType;
	private Timestamp updateTime;
	private String    updateNote;
	@Override
	public String toString()
	{
		return "OBDtoVersionInfo [productCode=" + productCode
				+ ", productName=" + productName + ", productModel="
				+ productModel + ", version=" + version + ", revision="
				+ revision + ", updateType=" + updateType + ", updateTime="
				+ updateTime + ", updateNote=" + updateNote + "]";
	}
	public String getProductCode()
	{
		return productCode;
	}
	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}
	public String getProductName()
	{
		return productName;
	}
	public void setProductName(String productName)
	{
		this.productName = productName;
	}
	public String getProductModel()
	{
		return productModel;
	}
	public void setProductModel(String productModel)
	{
		this.productModel = productModel;
	}
	public String getVersion()
	{
		return version;
	}
	public void setVersion(String version)
	{
		this.version = version;
	}
	public int getRevision()
	{
		return revision;
	}
	public void setRevision(int revision)
	{
		this.revision = revision;
	}
	public int getUpdateType()
	{
		return updateType;
	}
	public void setUpdateType(int updateType)
	{
		this.updateType = updateType;
	}
	public Timestamp getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
	}
	public String getUpdateNote()
	{
		return updateNote;
	}
	public void setUpdateNote(String updateNote)
	{
		this.updateNote = updateNote;
	}
}
