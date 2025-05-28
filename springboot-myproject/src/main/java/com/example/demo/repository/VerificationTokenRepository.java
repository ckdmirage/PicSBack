package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.VerificationToken;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer>{
	@Query(value = "select id, token, expiry_date, user_id from verification_token where token = :token", nativeQuery = true)
	Optional<VerificationToken> getToken(String token);
}
