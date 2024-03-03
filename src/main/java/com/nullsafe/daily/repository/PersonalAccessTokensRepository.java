package com.nullsafe.daily.repository;

import com.nullsafe.daily.domain.PersonalAccessTokens;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PersonalAccessTokens entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonalAccessTokensRepository
    extends JpaRepository<PersonalAccessTokens, Long>, JpaSpecificationExecutor<PersonalAccessTokens> {}
