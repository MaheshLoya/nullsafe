package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.WebAppSettings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WebAppSettings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WebAppSettingsRepository extends JpaRepository<WebAppSettings, Integer>, JpaSpecificationExecutor<WebAppSettings> {}
