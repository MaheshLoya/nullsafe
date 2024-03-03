package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.SubCat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubCat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubCatRepository extends JpaRepository<SubCat, Long>, JpaSpecificationExecutor<SubCat> {}
