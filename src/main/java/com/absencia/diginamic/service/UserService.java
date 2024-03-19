package com.absencia.diginamic.service;

import com.absencia.diginamic.Model.User.User;

public interface UserService {

    User findOne(String email);
    User createUser(User user);
    
}
