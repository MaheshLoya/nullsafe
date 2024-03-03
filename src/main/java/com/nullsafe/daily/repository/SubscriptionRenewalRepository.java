package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.SubscriptionRenewal;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubscriptionRenewal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionRenewalRepository
    extends JpaRepository<SubscriptionRenewal, Long>, JpaSpecificationExecutor<SubscriptionRenewal> {}
