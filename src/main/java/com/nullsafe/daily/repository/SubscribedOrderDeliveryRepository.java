package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.SubscribedOrderDelivery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubscribedOrderDelivery entity.
 */
@Repository
public interface SubscribedOrderDeliveryRepository
    extends JpaRepository<SubscribedOrderDelivery, Long>, JpaSpecificationExecutor<SubscribedOrderDelivery> {
    default Optional<SubscribedOrderDelivery> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SubscribedOrderDelivery> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SubscribedOrderDelivery> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select subscribedOrderDelivery from SubscribedOrderDelivery subscribedOrderDelivery left join fetch subscribedOrderDelivery.entryUser",
        countQuery = "select count(subscribedOrderDelivery) from SubscribedOrderDelivery subscribedOrderDelivery"
    )
    Page<SubscribedOrderDelivery> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select subscribedOrderDelivery from SubscribedOrderDelivery subscribedOrderDelivery left join fetch subscribedOrderDelivery.entryUser"
    )
    List<SubscribedOrderDelivery> findAllWithToOneRelationships();

    @Query(
        "select subscribedOrderDelivery from SubscribedOrderDelivery subscribedOrderDelivery left join fetch subscribedOrderDelivery.entryUser where subscribedOrderDelivery.id =:id"
    )
    Optional<SubscribedOrderDelivery> findOneWithToOneRelationships(@Param("id") Long id);
}
