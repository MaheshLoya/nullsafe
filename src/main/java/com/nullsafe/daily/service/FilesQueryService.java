package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.*; // for static metamodels
import com.nullsafe.daily.domain.Files;
import com.nullsafe.daily.repository.FilesRepository;
import com.nullsafe.daily.service.criteria.FilesCriteria;
import com.nullsafe.daily.service.dto.FilesDTO;
import com.nullsafe.daily.service.mapper.FilesMapper;
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
 * Service for executing complex queries for {@link Files} entities in the database.
 * The main input is a {@link FilesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FilesDTO} or a {@link Page} of {@link FilesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FilesQueryService extends QueryService<Files> {

    private final Logger log = LoggerFactory.getLogger(FilesQueryService.class);

    private final FilesRepository filesRepository;

    private final FilesMapper filesMapper;

    public FilesQueryService(FilesRepository filesRepository, FilesMapper filesMapper) {
        this.filesRepository = filesRepository;
        this.filesMapper = filesMapper;
    }

    /**
     * Return a {@link List} of {@link FilesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FilesDTO> findByCriteria(FilesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Files> specification = createSpecification(criteria);
        return filesMapper.toDto(filesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FilesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FilesDTO> findByCriteria(FilesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Files> specification = createSpecification(criteria);
        return filesRepository.findAll(specification, page).map(filesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FilesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Files> specification = createSpecification(criteria);
        return filesRepository.count(specification);
    }

    /**
     * Function to convert {@link FilesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Files> createSpecification(FilesCriteria criteria) {
        Specification<Files> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Files_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Files_.name));
            }
            if (criteria.getFileUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileUrl(), Files_.fileUrl));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), Files_.createdAt));
            }
            if (criteria.getUpdatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedAt(), Files_.updatedAt));
            }
            if (criteria.getDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getDeleted(), Files_.deleted));
            }
            if (criteria.getFileFor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFileFor(), Files_.fileFor));
            }
            if (criteria.getFileForId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFileForId(), Files_.fileForId));
            }
            if (criteria.getFileCat() != null) {
                specification = specification.and(buildSpecification(criteria.getFileCat(), Files_.fileCat));
            }
        }
        return specification;
    }
}
