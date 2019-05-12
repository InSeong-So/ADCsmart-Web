/**
 * 정렬 기능을 위한 오브젝트.
 */
package kr.openbase.adcsmart.service.dto;

public class OBDtoOrdering
{
	public static final  int	DIR_ASCEND		=	1;
	public static final  int	DIR_DESCEND		=	2;
	
	public static final  int	TYPE_1FIRST		=	33;
	public static final  int	TYPE_2SECOND	=	34;
	public static final  int	TYPE_3THIRD		=	35;
	public static final  int	TYPE_4FOURTH	=	36;
	public static final  int	TYPE_5FIFTH		=	37;
	public static final  int	TYPE_6SIXTH		=	38;
	public static final  int	TYPE_7SEVENTH	=	39;	
	public static final  int	TYPE_8EIGHTH	=	40;	
	public static final  int	TYPE_9NINTH		=	41;	
	public static final  int	TYPE_10TENTH	=	42;	
	public static final  int	TYPE_11ELEVENTH	=	43;	
	public static final  int	TYPE_12TWELFTH	=	44;	
	public static final  int	TYPE_13THIRTEENTH=	45;	
	public static final  int	TYPE_14FOURTEENTH=	46;	

	private int orderType;
	private int orderDirection;
	public int getOrderType()
	{
		return orderType;
	}
	public void setOrderType(int orderType)
	{
		this.orderType = orderType;
	}
	public int getOrderDirection()
	{
		return orderDirection;
	}
	public void setOrderDirection(int orderDirection)
	{
		this.orderDirection = orderDirection;
	}
}
