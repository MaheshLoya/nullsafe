package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.AssignRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AssignRole entity.
 */
@Repository
public interface AssignRoleRepository extends JpaRepository<AssignRole, Long>, JpaSpecificationExecutor<AssignRole> {
    default Optional<AssignRole> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AssignRole> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AssignRole> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select assignRole from AssignRole assignRole left join fetch assignRole.user",
        countQuery = "select count(assignRole) from AssignRole assignRole"
    )
    Page<AssignRole> findAllWithToOneRelationships(Pageable pageable);

    @Query("select assignRole from AssignRole assignRole left join fetch assignRole.user")
    List<AssignRole> findAllWithToOneRelationships();

    @Query("select assignRole from AssignRole assignRole left join fetch assignRole.user where assignRole.id =:id")
    Optional<AssignRole> findOneWithToOneRelationships(@Param("id") Long id);
}
