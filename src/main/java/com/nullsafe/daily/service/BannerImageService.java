package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.BannerImage;
import com.nullsafe.daily.repository.BannerImageRepository;
import com.nullsafe.daily.service.dto.BannerImageDTO;
import com.nullsafe.daily.service.mapper.BannerImageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.BannerImage}.
 */
@Service
@Transactional
public class BannerImageService {

    private final Logger log = LoggerFactory.getLogger(BannerImageService.class);

    private final BannerImageRepository bannerImageRepository;

    private final BannerImageMapper bannerImageMapper;

    public BannerImageService(BannerImageRepository bannerImageRepository, BannerImageMapper bannerImageMapper) {
        this.bannerImageRepository = bannerImageRepository;
        this.bannerImageMapper = bannerImageMapper;
    }

    /**
     * Save a bannerImage.
     *
     * @param bannerImageDTO the entity to save.
     * @return the persisted entity.
     */
    public BannerImageDTO save(BannerImageDTO bannerImageDTO) {
        log.debug("Request to save BannerImage : {}", bannerImageDTO);
        BannerImage bannerImage = bannerImageMapper.toEntity(bannerImageDTO);
        bannerImage = bannerImageRepository.save(bannerImage);
        return bannerImageMapper.toDto(bannerImage);
    }

    /**
     * Update a bannerImage.
     *
     * @param bannerImageDTO the entity to save.
     * @return the persisted entity.
     */
    public BannerImageDTO update(BannerImageDTO bannerImageDTO) {
        log.debug("Request to update BannerImage : {}", bannerImageDTO);
        BannerImage bannerImage = bannerImageMapper.toEntity(bannerImageDTO);
        bannerImage = bannerImageRepository.save(bannerImage);
        return bannerImageMapper.toDto(bannerImage);
    }

    /**
     * Partially update a bannerImage.
     *
     * @param bannerImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BannerImageDTO> partialUpdate(BannerImageDTO bannerImageDTO) {
        log.debug("Request to partially update BannerImage : {}", bannerImageDTO);

        return bannerImageRepository
            .findById(bannerImageDTO.getId())
            .map(existingBannerImage -> {
                bannerImageMapper.partialUpdate(existingBannerImage, bannerImageDTO);

                return existingBannerImage;
            })
            .map(bannerImageRepository::save)
            .map(bannerImageMapper::toDto);
    }

    /**
     * Get all the bannerImages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BannerImageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BannerImages");
        return bannerImageRepository.findAll(pageable).map(bannerImageMapper::toDto);
    }

    /**
     * Get one bannerImage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BannerImageDTO> findOne(Long id) {
        log.debug("Request to get BannerImage : {}", id);
        return bannerImageRepository.findById(id).map(bannerImageMapper::toDto);
    }

    /**
     * Delete the bannerImage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BannerImage : {}", id);
        bannerImageRepository.deleteById(id);
    }
}
