package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.UserNotification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserNotification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification, Long>, JpaSpecificationExecutor<UserNotification> {}
