package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.FailedJobs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FailedJobs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FailedJobsRepository extends JpaRepository<FailedJobs, Long>, JpaSpecificationExecutor<FailedJobs> {}
