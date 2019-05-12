package kr.openbase.adcsmart.web.facade.dto;

import java.util.List;

public class UserAddDto {
	private AccountDto user;
	private List<AdcDto> adcs;
	
	public AccountDto getUser() {
		return user;
	}

	public void setUser(AccountDto user) {
		this.user = user;
	}

	public List<AdcDto> getAdcs() {
		return adcs;
	}

	public void setAdcs(List<AdcDto> adcs) {
		this.adcs = adcs;
	}

	@Override
	public String toString() {
		return "UserAddDto [user=" + user + ", adcs=" + adcs + "]";
	}
	
}
