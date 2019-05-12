package kr.openbase.adcsmart.service.dto.fault;

public class OBDtoL2SearchOption
{
	public static final  int	OPTION_TYPE_IP_ADDR		=	1;
	public static final  int	OPTION_TYPE_MAC_ADDR	=	2;
	public static final  int	OPTION_TYPE_VLAN 		=	3;
	public static final  int	OPTION_TYPE_INTERFACE	=	4;
	
	private String content;
	private Integer type;
	
	@Override
	public String toString()
	{
		return String.format("OBDtoL2SearchOption [content=%s, type=%s]", content, type);
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
