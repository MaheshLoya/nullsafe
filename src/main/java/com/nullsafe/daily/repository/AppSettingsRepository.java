package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.AppSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AppSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppSettingsRepository extends JpaRepository<AppSettings, Long>, JpaSpecificationExecutor<AppSettings> {}
