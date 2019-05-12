package kr.openbase.adcsmart.service.dto;

public class OBDtoReturnInfo
{
	public static final  int	CODE_FALSE		=0;
	public static final  int	CODE_TRUE		=1;
	public static final  int	CODE_OK			=2;
//	public static final  int	CODE_STOP		=3;
//	public static final  int	CODE_INSTOP		=3;

	private Integer retCode;
	private String  retValue;
	private Object  detailInfo;
	
	@Override
	public String toString()
	{
		return String.format("OBDtoReturnInfo [retCode=%s, retValue=%s, detailInfo=%s]", retCode, retValue, detailInfo);
	}
	public Integer getRetCode()
	{
		return retCode;
	}
	public Object getDetailInfo()
	{
		return detailInfo;
	}
	public void setDetailInfo(Object detailInfo)
	{
		this.detailInfo = detailInfo;
	}
	public void setRetCode(Integer retCode)
	{
		this.retCode = retCode;
	}
	public String getRetValue()
	{
		return retValue;
	}
	public void setRetValue(String retValue)
	{
		this.retValue = retValue;
	}
}
