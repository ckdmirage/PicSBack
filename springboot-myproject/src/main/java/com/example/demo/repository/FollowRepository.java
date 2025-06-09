package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Follow;
import com.example.demo.model.entity.User;
import com.example.demo.model.entity.serializable.FollowId;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId>{
	//追蹤/粉絲列表
	List<Follow> findByFollower(User follower);
    List<Follow> findByFollowing(User following);
    //統計追蹤/粉絲總數
    int countByFollower(User follower);
    int countByFollowing(User following);
}
