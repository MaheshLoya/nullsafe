package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.WebPages;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WebPages entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WebPagesRepository extends JpaRepository<WebPages, Long>, JpaSpecificationExecutor<WebPages> {}
