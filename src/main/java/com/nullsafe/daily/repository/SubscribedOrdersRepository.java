package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.SubscribedOrders;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubscribedOrders entity.
 */
@Repository
public interface SubscribedOrdersRepository extends JpaRepository<SubscribedOrders, Long>, JpaSpecificationExecutor<SubscribedOrders> {
    default Optional<SubscribedOrders> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SubscribedOrders> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SubscribedOrders> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select subscribedOrders from SubscribedOrders subscribedOrders left join fetch subscribedOrders.user",
        countQuery = "select count(subscribedOrders) from SubscribedOrders subscribedOrders"
    )
    Page<SubscribedOrders> findAllWithToOneRelationships(Pageable pageable);

    @Query("select subscribedOrders from SubscribedOrders subscribedOrders left join fetch subscribedOrders.user")
    List<SubscribedOrders> findAllWithToOneRelationships();

    @Query(
        "select subscribedOrders from SubscribedOrders subscribedOrders left join fetch subscribedOrders.user where subscribedOrders.id =:id"
    )
    Optional<SubscribedOrders> findOneWithToOneRelationships(@Param("id") Long id);
}
