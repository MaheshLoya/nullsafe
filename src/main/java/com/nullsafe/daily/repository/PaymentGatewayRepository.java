package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.PaymentGateway;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PaymentGateway entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentGatewayRepository extends JpaRepository<PaymentGateway, Long>, JpaSpecificationExecutor<PaymentGateway> {}
