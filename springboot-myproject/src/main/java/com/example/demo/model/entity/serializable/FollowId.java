package com.example.demo.model.entity.serializable;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable // 表示這個類別不是一張獨立的表，而是嵌入到另一個實體
public class FollowId implements Serializable{
	
	private Integer followerId; // 追蹤者
	
	private Integer followingId; // 被追蹤者
}
