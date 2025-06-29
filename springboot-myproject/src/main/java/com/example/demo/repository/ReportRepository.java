package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.dto.reportDto.ReportDisplayDto;
import com.example.demo.model.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

	// 查詢所有檢舉列
	@Query("""
			    SELECT new com.example.demo.model.dto.reportDto.ReportDisplayDto(
			        r.id,
			        r.reporter.username,
			        r.reporter.id,
			        r.artwork.id,
			        r.artwork.title,
			        r.reason,
			        r.reportedAt
			    )
			    FROM Report r
			    ORDER BY r.reportedAt DESC
			""")
	List<ReportDisplayDto> findAllReports();

	// 檢查是否已檢舉
	boolean existsByReporterIdAndArtworkId(Integer reporterId, Integer artworkId);
	
	@EntityGraph(attributePaths = { "artwork", "artwork.user", "artwork.tags", "reporter" })
	Optional<Report> findById(Integer id);

}