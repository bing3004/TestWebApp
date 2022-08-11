package com.bing.TestWebApp.service;

import com.bing.TestWebApp.exception.UserAlreadyExistException;
import com.bing.TestWebApp.model.Authority;
import com.bing.TestWebApp.model.User;
import com.bing.TestWebApp.model.UserRole;


import com.bing.TestWebApp.repository.AuthorityRepository;
import com.bing.TestWebApp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;


import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;



@Service
public class RegisterService {
    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService (UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder){
        this.userRepository =  userRepository;
        this.authorityRepository =  authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void add(User user, UserRole role) throws UserAlreadyExistException{
        if(userRepository.existsById(user.getUsername())){
            throw new UserAlreadyExistException("username alread exists.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        authorityRepository.save(new Authority(user.getUsername(), role.name()));
    }


}