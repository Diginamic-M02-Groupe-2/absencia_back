package com.absencia.diginamic.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PostLoginModel {
	@NotBlank(message="Ce champ est requis.")
	@Email(message="Veuillez saisir un email valide.")
	private String email;

	@NotBlank(message="Ce champ est requis.")
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}
}