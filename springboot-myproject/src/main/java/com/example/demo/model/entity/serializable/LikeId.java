package com.example.demo.model.entity.serializable;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//複合主鍵
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LikeId implements Serializable {
 private Integer userId;
 private Integer artworkId;
 // equals, hashCode...
}