package com.absencia.diginamic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LoginRequest {
	@NotBlank(message="Veuillez saisir votre email.")
	@NotNull(message="Veuillez saisir votre email.")
	public String email;

	@NotBlank(message="Veuillez saisir votre mot de passe.")
	@NotNull(message="Veuillez saisir votre mot de passe.")
	public String password;
}