package com.absencia.diginamic.service;

import com.absencia.diginamic.model.User;

public interface UserService {

    User findOne(String email);
    User createUser(User user);
    
}
