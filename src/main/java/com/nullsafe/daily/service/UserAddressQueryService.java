package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.UserAddress;
import com.nullsafe.daily.repository.UserAddressRepository;
import com.nullsafe.daily.service.criteria.UserAddressCriteria;
import com.nullsafe.daily.service.dto.UserAddressDTO;
import com.nullsafe.daily.service.mapper.UserAddressMapper;
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
 * Service for executing complex queries for {@link UserAddress} entities in the database.
 * The main input is a {@link UserAddressCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UserAddressDTO} or a {@link Page} of {@link UserAddressDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserAddressQueryService extends QueryService<UserAddress> {

    private final Logger log = LoggerFactory.getLogger(UserAddressQueryService.class);

    private final UserAddressRepository userAddressRepository;

    private final UserAddressMapper userAddressMapper;

    public UserAddressQueryService(UserAddressRepository userAddressRepository, UserAddressMapper userAddressMapper) {
        this.userAddressRepository = userAddressRepository;
        this.userAddressMapper = userAddressMapper;
    }

    /**
     * Return a {@link List} of {@link UserAddressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UserAddressDTO> findByCriteria(UserAddressCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UserAddress> specification = createSpecification(criteria);
        return userAddressMapper.toDto(userAddressRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UserAddressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserAddressDTO> findByCriteria(UserAddressCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserAddress> specification = createSpecification(criteria);
        return userAddressRepository.findAll(specification, page).map(userAddressMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserAddressCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserAddress> specification = createSpecification(criteria);
        return userAddressRepository.count(specification);
    }

    /**
     * Function to convert {@link UserAddressCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserAddress> createSpecification(UserAddressCriteria criteria) {
        Specification<UserAddress> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserAddress_.id));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserId(), UserAddress_.userId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), UserAddress_.name));
            }
            if (criteria.getsPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getsPhone(), UserAddress_.sPhone));
            }
            if (criteria.getFlatNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFlatNo(), UserAddress_.flatNo));
            }
            if (criteria.getApartmentName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApartmentName(), UserAddress_.apartmentName));
            }
            if (criteria.getArea() != null) {
                specification = specification.and(buildStringSpecification(criteria.getArea(), UserAddress_.area));
            }
            if (criteria.getLandmark() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLandmark(), UserAddress_.landmark));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), UserAddress_.city));
            }
            if (criteria.getPincode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPincode(), UserAddress_.pincode));
            }
            if (criteria.getLat() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLat(), UserAddress_.lat));
            }
            if (criteria.getLng() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLng(), UserAddress_.lng));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), UserAddress_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), UserAddress_.updatedAt));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), UserAddress_.isActive));
            }
            if (criteria.getOrdersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOrdersId(), root -> root.join(UserAddress_.orders, JoinType.LEFT).get(Orders_.id))
                    );
            }
            if (criteria.getSubscribedOrdersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubscribedOrdersId(),
                            root -> root.join(UserAddress_.subscribedOrders, JoinType.LEFT).get(SubscribedOrders_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
