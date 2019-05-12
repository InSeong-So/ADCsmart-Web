/*
 * a 계정에 할당된 권한 자료 구조.
 */
package kr.openbase.adcsmart.service.dto;

public class OBDtoAccountRole
{
	private Integer index;
	private String name;
	private String comment;
	
	public Integer getIndex()
	{
		return this.index;
	}
	public void setIndex(Integer index)
	{
		this.index = index;
	}

	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getComment()
	{
		return this.comment;
	}
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	
	@Override
	public String toString()
	{
		return "OBDtoAccountRole [index=" + index + ", name=" + name
				+ ", comment=" + comment + "]";
	}
}
