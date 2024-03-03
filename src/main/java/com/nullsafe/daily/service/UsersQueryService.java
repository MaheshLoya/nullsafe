package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.Users;
import com.nullsafe.daily.repository.UsersRepository;
import com.nullsafe.daily.service.criteria.UsersCriteria;
import com.nullsafe.daily.service.dto.UsersDTO;
import com.nullsafe.daily.service.mapper.UsersMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Users} entities in the database.
 * The main input is a {@link UsersCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UsersDTO} or a {@link Page} of {@link UsersDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UsersQueryService extends QueryService<Users> {

    private final Logger log = LoggerFactory.getLogger(UsersQueryService.class);

    private final UsersRepository usersRepository;

    private final UsersMapper usersMapper;

    public UsersQueryService(UsersRepository usersRepository, UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
    }

    /**
     * Return a {@link List} of {@link UsersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UsersDTO> findByCriteria(UsersCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Users> specification = createSpecification(criteria);
        return usersMapper.toDto(usersRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UsersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UsersDTO> findByCriteria(UsersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Users> specification = createSpecification(criteria);
        return usersRepository.findAll(specification, page).map(usersMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UsersCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Users> specification = createSpecification(criteria);
        return usersRepository.count(specification);
    }

    /**
     * Function to convert {@link UsersCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Users> createSpecification(UsersCriteria criteria) {
        Specification<Users> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Users_.id));
            }
            if (criteria.getWalletAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWalletAmount(), Users_.walletAmount));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Users_.email));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Users_.phone));
            }
            if (criteria.getEmailVerifiedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEmailVerifiedAt(), Users_.emailVerifiedAt));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), Users_.password));
            }
            if (criteria.getRememberToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRememberToken(), Users_.rememberToken));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Users_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Users_.updatedAt));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Users_.name));
            }
            if (criteria.getFcm() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFcm(), Users_.fcm));
            }
            if (criteria.getSubscriptionAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubscriptionAmount(), Users_.subscriptionAmount));
            }
            if (criteria.getAssignRoleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssignRoleId(),
                            root -> root.join(Users_.assignRoles, JoinType.LEFT).get(AssignRole_.id)
                        )
                    );
            }
            if (criteria.getCartId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCartId(), root -> root.join(Users_.carts, JoinType.LEFT).get(Cart_.id))
                    );
            }
            if (criteria.getOrderUserAssignId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrderUserAssignId(),
                            root -> root.join(Users_.orderUserAssigns, JoinType.LEFT).get(OrderUserAssign_.id)
                        )
                    );
            }
            if (criteria.getOrdersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOrdersId(), root -> root.join(Users_.orders, JoinType.LEFT).get(Orders_.id))
                    );
            }
            if (criteria.getSpecificNotificationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSpecificNotificationId(),
                            root -> root.join(Users_.specificNotifications, JoinType.LEFT).get(SpecificNotification_.id)
                        )
                    );
            }
            if (criteria.getSubscribedOrderDeliveryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubscribedOrderDeliveryId(),
                            root -> root.join(Users_.subscribedOrderDeliveries, JoinType.LEFT).get(SubscribedOrderDelivery_.id)
                        )
                    );
            }
            if (criteria.getSubscribedOrdersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubscribedOrdersId(),
                            root -> root.join(Users_.subscribedOrders, JoinType.LEFT).get(SubscribedOrders_.id)
                        )
                    );
            }
            if (criteria.getTransactionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransactionsId(),
                            root -> root.join(Users_.transactions, JoinType.LEFT).get(Transactions_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
