package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.PasswordResets;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PasswordResets entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PasswordResetsRepository extends JpaRepository<PasswordResets, Long>, JpaSpecificationExecutor<PasswordResets> {}
