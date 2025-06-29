package com.example.demo.model.dto.reportDto;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// 扁平化前端展示用Dto
public class ReportDisplayDto {
    private Integer reportId;
    private String reporterUsername;
    private Integer artworkId;
    private String artworkTitle;
    private String reason;
    private LocalDateTime reportedAt;
}