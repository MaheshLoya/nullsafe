package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.WebPages;
import com.nullsafe.daily.repository.WebPagesRepository;
import com.nullsafe.daily.service.dto.WebPagesDTO;
import com.nullsafe.daily.service.mapper.WebPagesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.WebPages}.
 */
@Service
@Transactional
public class WebPagesService {

    private final Logger log = LoggerFactory.getLogger(WebPagesService.class);

    private final WebPagesRepository webPagesRepository;

    private final WebPagesMapper webPagesMapper;

    public WebPagesService(WebPagesRepository webPagesRepository, WebPagesMapper webPagesMapper) {
        this.webPagesRepository = webPagesRepository;
        this.webPagesMapper = webPagesMapper;
    }

    /**
     * Save a webPages.
     *
     * @param webPagesDTO the entity to save.
     * @return the persisted entity.
     */
    public WebPagesDTO save(WebPagesDTO webPagesDTO) {
        log.debug("Request to save WebPages : {}", webPagesDTO);
        WebPages webPages = webPagesMapper.toEntity(webPagesDTO);
        webPages = webPagesRepository.save(webPages);
        return webPagesMapper.toDto(webPages);
    }

    /**
     * Update a webPages.
     *
     * @param webPagesDTO the entity to save.
     * @return the persisted entity.
     */
    public WebPagesDTO update(WebPagesDTO webPagesDTO) {
        log.debug("Request to update WebPages : {}", webPagesDTO);
        WebPages webPages = webPagesMapper.toEntity(webPagesDTO);
        webPages = webPagesRepository.save(webPages);
        return webPagesMapper.toDto(webPages);
    }

    /**
     * Partially update a webPages.
     *
     * @param webPagesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WebPagesDTO> partialUpdate(WebPagesDTO webPagesDTO) {
        log.debug("Request to partially update WebPages : {}", webPagesDTO);

        return webPagesRepository
            .findById(webPagesDTO.getId())
            .map(existingWebPages -> {
                webPagesMapper.partialUpdate(existingWebPages, webPagesDTO);

                return existingWebPages;
            })
            .map(webPagesRepository::save)
            .map(webPagesMapper::toDto);
    }

    /**
     * Get all the webPages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WebPagesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WebPages");
        return webPagesRepository.findAll(pageable).map(webPagesMapper::toDto);
    }

    /**
     * Get one webPages by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WebPagesDTO> findOne(Long id) {
        log.debug("Request to get WebPages : {}", id);
        return webPagesRepository.findById(id).map(webPagesMapper::toDto);
    }

    /**
     * Delete the webPages by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WebPages : {}", id);
        webPagesRepository.deleteById(id);
    }
}
