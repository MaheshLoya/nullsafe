package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.Transactions;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Transactions entity.
 */
@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Long>, JpaSpecificationExecutor<Transactions> {
    default Optional<Transactions> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Transactions> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Transactions> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select transactions from Transactions transactions left join fetch transactions.user",
        countQuery = "select count(transactions) from Transactions transactions"
    )
    Page<Transactions> findAllWithToOneRelationships(Pageable pageable);

    @Query("select transactions from Transactions transactions left join fetch transactions.user")
    List<Transactions> findAllWithToOneRelationships();

    @Query("select transactions from Transactions transactions left join fetch transactions.user where transactions.id =:id")
    Optional<Transactions> findOneWithToOneRelationships(@Param("id") Long id);
}
