package com.absencia.diginamic.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LoginModel {
	@NotBlank(message="Veuillez saisir votre email.")
	@NotNull(message="Veuillez saisir votre email.")
	@Email(message="Veuillez saisir un email valide.")
	private String email;

	@NotBlank(message="Veuillez saisir votre mot de passe.")
	@NotNull(message="Veuillez saisir votre mot de passe.")
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