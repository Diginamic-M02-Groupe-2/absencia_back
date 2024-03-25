package com.absencia.diginamic.entity;

import jakarta.persistence.Entity;

@Entity
public class Administrator extends User {
	public Administrator() {
		super();

		setRole(Role.ADMINISTRATOR);
	}
}