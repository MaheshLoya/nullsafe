package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.Files;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Files entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FilesRepository extends JpaRepository<Files, Long>, JpaSpecificationExecutor<Files> {}
