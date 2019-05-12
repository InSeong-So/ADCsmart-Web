package kr.openbase.adcsmart.service.impl.fault.dto;

public class OBDtoPktdumpOption
{
	public static final  int	OPTION_TYPE_SRC_IP		=	1;
	public static final  int	OPTION_TYPE_SRC_PORT	=	2;
	public static final  int	OPTION_TYPE_DST_IP 		=	3;
	public static final  int	OPTION_TYPE_DST_PORT	=	4;
	public static final  int	OPTION_TYPE_PROTOCOL	=	5;
	public static final  int	OPTION_TYPE_HOST		=	6;
	public static final  int	OPTION_TYPE_PORT		=	7;
	
	private String content;
	private Integer type;
	@Override
	public String toString()
	{
		return String.format("OBDtoPktdumpOption [content=%s, type=%s]", content, type);
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
}
