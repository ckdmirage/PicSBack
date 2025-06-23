package com.example.demo.model.dto.artworkdto;

import java.time.LocalDateTime;

public record ArtworkDetailFlatDto(
	    Integer artworkId,
	    String title,
	    String imageUrl,
	    LocalDateTime uploaded,
	    
	    Integer authorId,
	    String authorName,
	    String authorEmail,
	    String authorAvatarUrl, // ✅ 作者頭像（主頁才需要）
	    LocalDateTime authorRegisted,
	    Long likes // ✅ 總點讚數
	) {}