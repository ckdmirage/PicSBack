package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	@Query(value = """
			    SELECT user_id, username, email, password_hash,
			           email_verified, user_created_at, role, avatar_url
			    FROM user
			    WHERE username = :username
			""", nativeQuery = true)
	Optional<User> getUser(String username);
	
	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);
	


	// 模糊查詢
	List<User> findByUsernameContainingIgnoreCase(String keyword);
	
	
	List<User> findByRoleNotIn(List<String> roles);
}
