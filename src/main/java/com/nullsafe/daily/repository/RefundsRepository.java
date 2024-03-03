package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.Refunds;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Refunds entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RefundsRepository extends JpaRepository<Refunds, Integer>, JpaSpecificationExecutor<Refunds> {}
