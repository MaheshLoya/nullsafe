package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.UserHoliday;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserHoliday entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserHolidayRepository extends JpaRepository<UserHoliday, Long>, JpaSpecificationExecutor<UserHoliday> {}
