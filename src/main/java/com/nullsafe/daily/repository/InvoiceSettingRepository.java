package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.InvoiceSetting;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InvoiceSetting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvoiceSettingRepository extends JpaRepository<InvoiceSetting, Long>, JpaSpecificationExecutor<InvoiceSetting> {}
