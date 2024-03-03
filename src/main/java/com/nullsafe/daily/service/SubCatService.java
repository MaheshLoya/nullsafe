package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.SubCat;
import com.nullsafe.daily.repository.SubCatRepository;
import com.nullsafe.daily.service.dto.SubCatDTO;
import com.nullsafe.daily.service.mapper.SubCatMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.SubCat}.
 */
@Service
@Transactional
public class SubCatService {

    private final Logger log = LoggerFactory.getLogger(SubCatService.class);

    private final SubCatRepository subCatRepository;

    private final SubCatMapper subCatMapper;

    public SubCatService(SubCatRepository subCatRepository, SubCatMapper subCatMapper) {
        this.subCatRepository = subCatRepository;
        this.subCatMapper = subCatMapper;
    }

    /**
     * Save a subCat.
     *
     * @param subCatDTO the entity to save.
     * @return the persisted entity.
     */
    public SubCatDTO save(SubCatDTO subCatDTO) {
        log.debug("Request to save SubCat : {}", subCatDTO);
        SubCat subCat = subCatMapper.toEntity(subCatDTO);
        subCat = subCatRepository.save(subCat);
        return subCatMapper.toDto(subCat);
    }

    /**
     * Update a subCat.
     *
     * @param subCatDTO the entity to save.
     * @return the persisted entity.
     */
    public SubCatDTO update(SubCatDTO subCatDTO) {
        log.debug("Request to update SubCat : {}", subCatDTO);
        SubCat subCat = subCatMapper.toEntity(subCatDTO);
        subCat = subCatRepository.save(subCat);
        return subCatMapper.toDto(subCat);
    }

    /**
     * Partially update a subCat.
     *
     * @param subCatDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubCatDTO> partialUpdate(SubCatDTO subCatDTO) {
        log.debug("Request to partially update SubCat : {}", subCatDTO);

        return subCatRepository
            .findById(subCatDTO.getId())
            .map(existingSubCat -> {
                subCatMapper.partialUpdate(existingSubCat, subCatDTO);

                return existingSubCat;
            })
            .map(subCatRepository::save)
            .map(subCatMapper::toDto);
    }

    /**
     * Get all the subCats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SubCatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SubCats");
        return subCatRepository.findAll(pageable).map(subCatMapper::toDto);
    }

    /**
     * Get one subCat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubCatDTO> findOne(Long id) {
        log.debug("Request to get SubCat : {}", id);
        return subCatRepository.findById(id).map(subCatMapper::toDto);
    }

    /**
     * Delete the subCat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SubCat : {}", id);
        subCatRepository.deleteById(id);
    }
}
