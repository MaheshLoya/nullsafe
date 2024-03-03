package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.Cat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatRepository extends JpaRepository<Cat, Long>, JpaSpecificationExecutor<Cat> {}
