package kr.openbase.adcsmart.service.dto.fault;

public class OBDtoSessionSearchOption
{
	public static final  int	OPTION_TYPE_SRC_IP		=	1;
	public static final  int	OPTION_TYPE_SRC_PORT	=	2;
	public static final  int	OPTION_TYPE_DST_IP 		=	3;
	public static final  int	OPTION_TYPE_DST_PORT	=	4;
	public static final  int	OPTION_TYPE_PROTOCOL	=	5;
	public static final  int	OPTION_TYPE_REAL_IP		=	6;
	public static final  int	OPTION_TYPE_REAL_PORT	=	7;
	public static final  int    OPTION_TYPE_AGING_TIME    =   8;
	
	private String content;
	private Integer type;
	private String lbType;
	@Override
	public String toString()
	{
		return String.format("OBDtoSessionSearchOption [content=%s, type=%s, lbType=%s]", content, type, lbType);
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
	}
	public String getLbType()
	{
		return lbType;
	}
	public void setLbType(String lbType)
	{
		this.lbType = lbType;
	}
}
