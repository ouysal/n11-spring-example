package com.n11.dto;

import com.n11.domain.User;

public class CreateUserDto {

	private User user;
	private Captcha captcha;
	
	public CreateUserDto() {

	}

	public CreateUserDto(User user, Captcha captcha) {
		this.user = user;
		this.captcha = captcha;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Captcha getCaptcha() {
		return captcha;
	}

	public void setCaptcha(Captcha captcha) {
		this.captcha = captcha;
	}
}