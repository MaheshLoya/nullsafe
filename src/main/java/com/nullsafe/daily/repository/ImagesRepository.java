package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.Images;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Images entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImagesRepository extends JpaRepository<Images, Long>, JpaSpecificationExecutor<Images> {}
