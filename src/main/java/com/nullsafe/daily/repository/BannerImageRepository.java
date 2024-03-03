package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.BannerImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BannerImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BannerImageRepository extends JpaRepository<BannerImage, Long>, JpaSpecificationExecutor<BannerImage> {}
