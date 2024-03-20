package com.absencia.diginamic.model;

import jakarta.persistence.Entity;

@Entity
public class Administrator extends User {
	public Administrator() {
		super();

		setRole(Role.ADMINISTRATOR);
	}
}