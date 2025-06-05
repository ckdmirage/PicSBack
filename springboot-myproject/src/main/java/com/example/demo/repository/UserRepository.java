package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.dto.userdto.UserCertDto;
import com.example.demo.model.dto.userdto.UserDto;
import com.example.demo.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	@Query(value = "select user_id, username, email, password_hash, email_verified, user_created_at, role from user where username=:username", nativeQuery = true)
	Optional<User> getUser(String username);
	

}
