package com.hrbeu5162.service;

import com.hrbeu5162.po.User;

import java.util.List;

public interface UserService {
    User checkUser(String username, String password);

    User findUserByUsername(String userName);

    User findUserByEmail(String email);

    User findUserById(Long id);

    User updateUserMessage(Long id, User user);

    User save(User user);

    Long countBlogByUser(Long id);

    List<User> listStudent();

    List<User> listTeacher();

    List<User> listUser();
}
