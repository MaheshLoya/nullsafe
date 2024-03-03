package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.AvailableDeliveryLocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AvailableDeliveryLocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AvailableDeliveryLocationRepository
    extends JpaRepository<AvailableDeliveryLocation, Long>, JpaSpecificationExecutor<AvailableDeliveryLocation> {}
