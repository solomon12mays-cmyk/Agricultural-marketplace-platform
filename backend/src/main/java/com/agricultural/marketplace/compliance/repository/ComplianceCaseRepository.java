package com.agricultural.marketplace.compliance.repository;

import java.util.List;

import com.agricultural.marketplace.compliance.entity.ComplianceCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplianceCaseRepository extends JpaRepository<ComplianceCase, Long> {

    List<ComplianceCase> findBySubjectTypeAndSubjectId(String subjectType, String subjectId);

    Page<ComplianceCase> findByStatus(String status, Pageable pageable);

    Page<ComplianceCase> findByCaseType(String caseType, Pageable pageable);
}

