package com.hrbeu5162.service;

import com.hrbeu5162.NotFoundException;
import com.hrbeu5162.dao.BlogRepository;
import com.hrbeu5162.dao.UserRepository;
import com.hrbeu5162.po.Blog;
import com.hrbeu5162.po.User;
import com.hrbeu5162.util.MD5Utils;
import com.hrbeu5162.util.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public User checkUser(String username, String password) {

        User user = userRepository.findByUserNameAndPassword(username, MD5Utils.code(password));
        return user;
    }

    @Override
    public User findUserByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public User updateUserMessage(Long id, User user) {
        User user1 = userRepository.findById(id).get();
        if (user1 == null) {
            throw new NotFoundException("该用户不存在");
        }
        BeanUtils.copyProperties(user, user1, MyBeanUtils.getNullPropertyNames(user));
        user.setUpdateTime(new Date());
        return userRepository.save(user1);
    }

    @Transactional
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Long countBlogByUser(Long id) {
        User user = userRepository.findById(id).get();
        return blogRepository.countBlogByUser(user);
    }

    @Override
    public List<User> listStudent() {
        return userRepository.findUsersByTeacherFalse();
    }

    @Override
    public List<User> listTeacher() {
        return userRepository.findUsersByTeacherTrue();
    }

    @Override
    public List<User> listUser() {
        List<User> users = userRepository.findAll();
        Collections.sort(users);
        return users;
    }
}
