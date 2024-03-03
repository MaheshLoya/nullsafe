package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.Product;
import com.nullsafe.daily.repository.ProductRepository;
import com.nullsafe.daily.service.criteria.ProductCriteria;
import com.nullsafe.daily.service.dto.ProductDTO;
import com.nullsafe.daily.service.mapper.ProductMapper;
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
 * Service for executing complex queries for {@link Product} entities in the database.
 * The main input is a {@link ProductCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductDTO} or a {@link Page} of {@link ProductDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService extends QueryService<Product> {

    private final Logger log = LoggerFactory.getLogger(ProductQueryService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductQueryService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Return a {@link List} of {@link ProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findByCriteria(ProductCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productMapper.toDto(productRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductDTO> findByCriteria(ProductCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.findAll(specification, page).map(productMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Product> specification = createSpecification(criteria);
        return productRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Product> createSpecification(ProductCriteria criteria) {
        Specification<Product> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Product_.id));
            }
            if (criteria.getTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitle(), Product_.title));
            }
            if (criteria.getQtyText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQtyText(), Product_.qtyText));
            }
            if (criteria.getStockQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStockQty(), Product_.stockQty));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Product_.price));
            }
            if (criteria.getTax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTax(), Product_.tax));
            }
            if (criteria.getMrp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMrp(), Product_.mrp));
            }
            if (criteria.getOfferText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOfferText(), Product_.offerText));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Product_.description));
            }
            if (criteria.getDisclaimer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDisclaimer(), Product_.disclaimer));
            }
            if (criteria.getSubscription() != null) {
                specification = specification.and(buildSpecification(criteria.getSubscription(), Product_.subscription));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Product_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Product_.updatedAt));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Product_.isActive));
            }
            if (criteria.getSubCatId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSubCatId(), root -> root.join(Product_.subCat, JoinType.LEFT).get(SubCat_.id))
                    );
            }
            if (criteria.getCartId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCartId(), root -> root.join(Product_.carts, JoinType.LEFT).get(Cart_.id))
                    );
            }
            if (criteria.getOrdersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOrdersId(), root -> root.join(Product_.orders, JoinType.LEFT).get(Orders_.id))
                    );
            }
            if (criteria.getSubscribedOrdersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubscribedOrdersId(),
                            root -> root.join(Product_.subscribedOrders, JoinType.LEFT).get(SubscribedOrders_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
