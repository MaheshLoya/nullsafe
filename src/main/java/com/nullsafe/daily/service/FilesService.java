package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.Files;
import com.nullsafe.daily.repository.FilesRepository;
import com.nullsafe.daily.service.dto.FilesDTO;
import com.nullsafe.daily.service.mapper.FilesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.Files}.
 */
@Service
@Transactional
public class FilesService {

    private final Logger log = LoggerFactory.getLogger(FilesService.class);

    private final FilesRepository filesRepository;

    private final FilesMapper filesMapper;

    public FilesService(FilesRepository filesRepository, FilesMapper filesMapper) {
        this.filesRepository = filesRepository;
        this.filesMapper = filesMapper;
    }

    /**
     * Save a files.
     *
     * @param filesDTO the entity to save.
     * @return the persisted entity.
     */
    public FilesDTO save(FilesDTO filesDTO) {
        log.debug("Request to save Files : {}", filesDTO);
        Files files = filesMapper.toEntity(filesDTO);
        files = filesRepository.save(files);
        return filesMapper.toDto(files);
    }

    /**
     * Update a files.
     *
     * @param filesDTO the entity to save.
     * @return the persisted entity.
     */
    public FilesDTO update(FilesDTO filesDTO) {
        log.debug("Request to update Files : {}", filesDTO);
        Files files = filesMapper.toEntity(filesDTO);
        files = filesRepository.save(files);
        return filesMapper.toDto(files);
    }

    /**
     * Partially update a files.
     *
     * @param filesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FilesDTO> partialUpdate(FilesDTO filesDTO) {
        log.debug("Request to partially update Files : {}", filesDTO);

        return filesRepository
            .findById(filesDTO.getId())
            .map(existingFiles -> {
                filesMapper.partialUpdate(existingFiles, filesDTO);

                return existingFiles;
            })
            .map(filesRepository::save)
            .map(filesMapper::toDto);
    }

    /**
     * Get all the files.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FilesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Files");
        return filesRepository.findAll(pageable).map(filesMapper::toDto);
    }

    /**
     * Get one files by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FilesDTO> findOne(Long id) {
        log.debug("Request to get Files : {}", id);
        return filesRepository.findById(id).map(filesMapper::toDto);
    }

    /**
     * Delete the files by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Files : {}", id);
        filesRepository.deleteById(id);
    }
}
