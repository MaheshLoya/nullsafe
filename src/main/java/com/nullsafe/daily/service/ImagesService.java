package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.Images;
import com.nullsafe.daily.repository.ImagesRepository;
import com.nullsafe.daily.service.dto.ImagesDTO;
import com.nullsafe.daily.service.mapper.ImagesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.Images}.
 */
@Service
@Transactional
public class ImagesService {

    private final Logger log = LoggerFactory.getLogger(ImagesService.class);

    private final ImagesRepository imagesRepository;

    private final ImagesMapper imagesMapper;

    public ImagesService(ImagesRepository imagesRepository, ImagesMapper imagesMapper) {
        this.imagesRepository = imagesRepository;
        this.imagesMapper = imagesMapper;
    }

    /**
     * Save a images.
     *
     * @param imagesDTO the entity to save.
     * @return the persisted entity.
     */
    public ImagesDTO save(ImagesDTO imagesDTO) {
        log.debug("Request to save Images : {}", imagesDTO);
        Images images = imagesMapper.toEntity(imagesDTO);
        images = imagesRepository.save(images);
        return imagesMapper.toDto(images);
    }

    /**
     * Update a images.
     *
     * @param imagesDTO the entity to save.
     * @return the persisted entity.
     */
    public ImagesDTO update(ImagesDTO imagesDTO) {
        log.debug("Request to update Images : {}", imagesDTO);
        Images images = imagesMapper.toEntity(imagesDTO);
        images = imagesRepository.save(images);
        return imagesMapper.toDto(images);
    }

    /**
     * Partially update a images.
     *
     * @param imagesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ImagesDTO> partialUpdate(ImagesDTO imagesDTO) {
        log.debug("Request to partially update Images : {}", imagesDTO);

        return imagesRepository
            .findById(imagesDTO.getId())
            .map(existingImages -> {
                imagesMapper.partialUpdate(existingImages, imagesDTO);

                return existingImages;
            })
            .map(imagesRepository::save)
            .map(imagesMapper::toDto);
    }

    /**
     * Get all the images.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ImagesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Images");
        return imagesRepository.findAll(pageable).map(imagesMapper::toDto);
    }

    /**
     * Get one images by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ImagesDTO> findOne(Long id) {
        log.debug("Request to get Images : {}", id);
        return imagesRepository.findById(id).map(imagesMapper::toDto);
    }

    /**
     * Delete the images by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Images : {}", id);
        imagesRepository.deleteById(id);
    }
}
