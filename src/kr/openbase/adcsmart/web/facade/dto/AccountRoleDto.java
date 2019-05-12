package kr.openbase.adcsmart.web.facade.dto;

import java.util.List;

public class AccountRoleDto {
	private Integer id;
	private String name;
	private String description;
	private List<String> accountIds;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(List<String> accountIds) {
		this.accountIds = accountIds;
	}

	@Override
	public String toString() {
		return "AccountRoleDto [id=" + id + ", name=" + name + ", description=" + description + ", accountIds="
				+ accountIds + "]";
	}
}
