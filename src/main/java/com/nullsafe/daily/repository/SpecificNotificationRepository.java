package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.SpecificNotification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SpecificNotification entity.
 */
@Repository
public interface SpecificNotificationRepository
    extends JpaRepository<SpecificNotification, Long>, JpaSpecificationExecutor<SpecificNotification> {
    default Optional<SpecificNotification> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SpecificNotification> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SpecificNotification> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select specificNotification from SpecificNotification specificNotification left join fetch specificNotification.user",
        countQuery = "select count(specificNotification) from SpecificNotification specificNotification"
    )
    Page<SpecificNotification> findAllWithToOneRelationships(Pageable pageable);

    @Query("select specificNotification from SpecificNotification specificNotification left join fetch specificNotification.user")
    List<SpecificNotification> findAllWithToOneRelationships();

    @Query(
        "select specificNotification from SpecificNotification specificNotification left join fetch specificNotification.user where specificNotification.id =:id"
    )
    Optional<SpecificNotification> findOneWithToOneRelationships(@Param("id") Long id);
}
