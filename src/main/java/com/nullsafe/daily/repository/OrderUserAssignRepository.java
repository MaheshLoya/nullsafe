package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.OrderUserAssign;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderUserAssign entity.
 */
@Repository
public interface OrderUserAssignRepository extends JpaRepository<OrderUserAssign, Long>, JpaSpecificationExecutor<OrderUserAssign> {
    default Optional<OrderUserAssign> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<OrderUserAssign> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<OrderUserAssign> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select orderUserAssign from OrderUserAssign orderUserAssign left join fetch orderUserAssign.user",
        countQuery = "select count(orderUserAssign) from OrderUserAssign orderUserAssign"
    )
    Page<OrderUserAssign> findAllWithToOneRelationships(Pageable pageable);

    @Query("select orderUserAssign from OrderUserAssign orderUserAssign left join fetch orderUserAssign.user")
    List<OrderUserAssign> findAllWithToOneRelationships();

    @Query("select orderUserAssign from OrderUserAssign orderUserAssign left join fetch orderUserAssign.user where orderUserAssign.id =:id")
    Optional<OrderUserAssign> findOneWithToOneRelationships(@Param("id") Long id);
}
