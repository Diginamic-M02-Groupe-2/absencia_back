package com.absencia.diginamic.entity.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.Email;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(length=255)
	private String firstName;

	@Column(length=255)
	private String lastName;

	@Column(length=128, unique=true)
	@Email
	private String email;

	@Column(length=255)
	@JsonIgnore
	private String password;

	@Enumerated(EnumType.ORDINAL)
	private Role role;

	@Enumerated(EnumType.ORDINAL)
	private Service service;

	@Override
	public boolean equals(final Object user) {
		if (!(user instanceof User)) {
			return false;
		}

		return id.equals(((User) user).getId());
	}

	@Override
	@JsonIgnore
	public String getUsername() {
		return email;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		final Set<GrantedAuthority> authorities = new HashSet<>();

		authorities.add(role);

		return authorities;
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public User setFirstName(final String firstName) {
		this.firstName = firstName;

		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public User setLastName(final String lastName) {
		this.lastName = lastName;

		return this;
	}

	public String getEmail() {
		return email;
	}

	public User setEmail(final String email) {
		this.email = email;

		return this;
	}

	public String getPassword() {
		return password;
	}

	public User setPassword(final String password) {
		this.password = password;

		return this;
	}

	public Role getRole() {
		return role;
	}

	public User setRole(final Role role) {
		this.role = role;

		return this;
	}

	public Service getService() {
		return service;
	}

	public User setService(final Service service) {
		this.service = service;

		return this;
	}
}