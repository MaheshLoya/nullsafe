package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.SocialMedia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SocialMedia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SocialMediaRepository extends JpaRepository<SocialMedia, Long>, JpaSpecificationExecutor<SocialMedia> {}
