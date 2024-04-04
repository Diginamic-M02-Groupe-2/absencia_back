package com.absencia.diginamic.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="user_administrator")
public class Administrator extends User {}