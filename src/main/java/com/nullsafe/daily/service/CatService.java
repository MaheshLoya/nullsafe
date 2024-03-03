package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.Cat;
import com.nullsafe.daily.repository.CatRepository;
import com.nullsafe.daily.service.dto.CatDTO;
import com.nullsafe.daily.service.mapper.CatMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.Cat}.
 */
@Service
@Transactional
public class CatService {

    private final Logger log = LoggerFactory.getLogger(CatService.class);

    private final CatRepository catRepository;

    private final CatMapper catMapper;

    public CatService(CatRepository catRepository, CatMapper catMapper) {
        this.catRepository = catRepository;
        this.catMapper = catMapper;
    }

    /**
     * Save a cat.
     *
     * @param catDTO the entity to save.
     * @return the persisted entity.
     */
    public CatDTO save(CatDTO catDTO) {
        log.debug("Request to save Cat : {}", catDTO);
        Cat cat = catMapper.toEntity(catDTO);
        cat = catRepository.save(cat);
        return catMapper.toDto(cat);
    }

    /**
     * Update a cat.
     *
     * @param catDTO the entity to save.
     * @return the persisted entity.
     */
    public CatDTO update(CatDTO catDTO) {
        log.debug("Request to update Cat : {}", catDTO);
        Cat cat = catMapper.toEntity(catDTO);
        cat = catRepository.save(cat);
        return catMapper.toDto(cat);
    }

    /**
     * Partially update a cat.
     *
     * @param catDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CatDTO> partialUpdate(CatDTO catDTO) {
        log.debug("Request to partially update Cat : {}", catDTO);

        return catRepository
            .findById(catDTO.getId())
            .map(existingCat -> {
                catMapper.partialUpdate(existingCat, catDTO);

                return existingCat;
            })
            .map(catRepository::save)
            .map(catMapper::toDto);
    }

    /**
     * Get all the cats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CatDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cats");
        return catRepository.findAll(pageable).map(catMapper::toDto);
    }

    /**
     * Get one cat by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CatDTO> findOne(Long id) {
        log.debug("Request to get Cat : {}", id);
        return catRepository.findById(id).map(catMapper::toDto);
    }

    /**
     * Delete the cat by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cat : {}", id);
        catRepository.deleteById(id);
    }
}
