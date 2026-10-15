package com.agricultural.marketplace.organization.repository;

import java.util.List;

import com.agricultural.marketplace.organization.entity.OrganizationDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationDocumentRepository extends JpaRepository<OrganizationDocument, Long> {

    List<OrganizationDocument> findByOrganizationId(Long organizationId);

    List<OrganizationDocument> findByOrganizationIdAndStatus(Long organizationId, String status);
}

