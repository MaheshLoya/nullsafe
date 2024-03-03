package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.AllowPincode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AllowPincode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AllowPincodeRepository extends JpaRepository<AllowPincode, Long>, JpaSpecificationExecutor<AllowPincode> {}
