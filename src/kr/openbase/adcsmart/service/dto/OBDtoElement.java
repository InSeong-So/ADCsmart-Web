package kr.openbase.adcsmart.service.dto;

public class OBDtoElement
{
	public static final  int	TARGET_F5=1;
	public static final  int	TARGET_ALTEON=2;
	public static final  int	TARGET_PAS=4;
	public static final  int	TARGET_PASK=8;
	
	private Integer     category;
	private	Integer 	index;
	private	String		name;
	private	String		description;
	private Integer		target;
	private	Integer 	state;//0:disable, 1:enable
	@Override
	public String toString()
	{
		return String.format("OBDtoElement [category=%s, index=%s, name=%s, description=%s, target=%s, state=%s]", category, index, name, description, target, state);
	}
	public Integer getCategory()
	{
		return category;
	}
	public void setCategory(Integer category)
	{
		this.category = category;
	}
	public Integer getIndex()
	{
		return index;
	}
	public void setIndex(Integer index)
	{
		this.index = index;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public Integer getTarget()
	{
		return target;
	}
	public void setTarget(Integer target)
	{
		this.target = target;
	}
	public Integer getState()
	{
		return state;
	}
	public void setState(Integer state)
	{
		this.state = state;
	}
	public static int getTargetF5()
	{
		return TARGET_F5;
	}
	public static int getTargetAlteon()
	{
		return TARGET_ALTEON;
	}
	public static int getTargetPas()
	{
		return TARGET_PAS;
	}
	public static int getTargetPask()
	{
		return TARGET_PASK;
	}
}
