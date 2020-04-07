package com.hrbeu5162.dao;

import com.hrbeu5162.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserNameAndPassword(String username, String password);

    User findByUserName(String username);

    User findByEmail(String email);

    List<User> findUsersByTeacherTrue();

    List<User> findUsersByTeacherFalse();
}
