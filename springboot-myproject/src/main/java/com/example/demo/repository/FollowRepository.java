package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.dto.followDto.FollowDto;
import com.example.demo.model.entity.Follow;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.serializable.FollowId;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId> {

	@EntityGraph(attributePaths = { "following" }) // 追蹤對象
	List<Follow> findByFollower(User user);

	@EntityGraph(attributePaths = { "follower" }) // 粉絲
	List<Follow> findByFollowing(User user);

	int countByFollowerId(Integer followerId); // 追蹤別人

	int countByFollowingId(Integer followingId); // 被追蹤

	@Query("""
				select new com.example.demo.model.dto.followDto.FollowDto(
				    u.id, u.username, u.email, u.role, u.created, f.createdAt
				)
				from Follow f
				join f.follower u
				where f.following.id = :userId
				order by f.createdAt desc
			""")
	List<FollowDto> fetchFollowersDto(@Param("userId") Integer userId);

	@Query("""
				select new com.example.demo.model.dto.followDto.FollowDto(
				    u.id, u.username, u.email, u.role, u.created, f.createdAt
				)
				from Follow f
				join f.following u
				where f.follower.id = :userId
				order by f.createdAt desc
			""")
	List<FollowDto> fetchFollowingsDto(@Param("userId") Integer userId);

}