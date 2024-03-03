package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.Migrations;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Migrations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MigrationsRepository extends JpaRepository<Migrations, Integer>, JpaSpecificationExecutor<Migrations> {}
