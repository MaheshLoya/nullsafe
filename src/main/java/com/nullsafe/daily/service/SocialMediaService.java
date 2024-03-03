package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.SocialMedia;
import com.nullsafe.daily.repository.SocialMediaRepository;
import com.nullsafe.daily.service.dto.SocialMediaDTO;
import com.nullsafe.daily.service.mapper.SocialMediaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.SocialMedia}.
 */
@Service
@Transactional
public class SocialMediaService {

    private final Logger log = LoggerFactory.getLogger(SocialMediaService.class);

    private final SocialMediaRepository socialMediaRepository;

    private final SocialMediaMapper socialMediaMapper;

    public SocialMediaService(SocialMediaRepository socialMediaRepository, SocialMediaMapper socialMediaMapper) {
        this.socialMediaRepository = socialMediaRepository;
        this.socialMediaMapper = socialMediaMapper;
    }

    /**
     * Save a socialMedia.
     *
     * @param socialMediaDTO the entity to save.
     * @return the persisted entity.
     */
    public SocialMediaDTO save(SocialMediaDTO socialMediaDTO) {
        log.debug("Request to save SocialMedia : {}", socialMediaDTO);
        SocialMedia socialMedia = socialMediaMapper.toEntity(socialMediaDTO);
        socialMedia = socialMediaRepository.save(socialMedia);
        return socialMediaMapper.toDto(socialMedia);
    }

    /**
     * Update a socialMedia.
     *
     * @param socialMediaDTO the entity to save.
     * @return the persisted entity.
     */
    public SocialMediaDTO update(SocialMediaDTO socialMediaDTO) {
        log.debug("Request to update SocialMedia : {}", socialMediaDTO);
        SocialMedia socialMedia = socialMediaMapper.toEntity(socialMediaDTO);
        socialMedia = socialMediaRepository.save(socialMedia);
        return socialMediaMapper.toDto(socialMedia);
    }

    /**
     * Partially update a socialMedia.
     *
     * @param socialMediaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SocialMediaDTO> partialUpdate(SocialMediaDTO socialMediaDTO) {
        log.debug("Request to partially update SocialMedia : {}", socialMediaDTO);

        return socialMediaRepository
            .findById(socialMediaDTO.getId())
            .map(existingSocialMedia -> {
                socialMediaMapper.partialUpdate(existingSocialMedia, socialMediaDTO);

                return existingSocialMedia;
            })
            .map(socialMediaRepository::save)
            .map(socialMediaMapper::toDto);
    }

    /**
     * Get all the socialMedias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SocialMediaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SocialMedias");
        return socialMediaRepository.findAll(pageable).map(socialMediaMapper::toDto);
    }

    /**
     * Get one socialMedia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SocialMediaDTO> findOne(Long id) {
        log.debug("Request to get SocialMedia : {}", id);
        return socialMediaRepository.findById(id).map(socialMediaMapper::toDto);
    }

    /**
     * Delete the socialMedia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SocialMedia : {}", id);
        socialMediaRepository.deleteById(id);
    }
}
