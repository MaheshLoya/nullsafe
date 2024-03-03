package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.FailedJobs;
import com.nullsafe.daily.repository.FailedJobsRepository;
import com.nullsafe.daily.service.dto.FailedJobsDTO;
import com.nullsafe.daily.service.mapper.FailedJobsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.FailedJobs}.
 */
@Service
@Transactional
public class FailedJobsService {

    private final Logger log = LoggerFactory.getLogger(FailedJobsService.class);

    private final FailedJobsRepository failedJobsRepository;

    private final FailedJobsMapper failedJobsMapper;

    public FailedJobsService(FailedJobsRepository failedJobsRepository, FailedJobsMapper failedJobsMapper) {
        this.failedJobsRepository = failedJobsRepository;
        this.failedJobsMapper = failedJobsMapper;
    }

    /**
     * Save a failedJobs.
     *
     * @param failedJobsDTO the entity to save.
     * @return the persisted entity.
     */
    public FailedJobsDTO save(FailedJobsDTO failedJobsDTO) {
        log.debug("Request to save FailedJobs : {}", failedJobsDTO);
        FailedJobs failedJobs = failedJobsMapper.toEntity(failedJobsDTO);
        failedJobs = failedJobsRepository.save(failedJobs);
        return failedJobsMapper.toDto(failedJobs);
    }

    /**
     * Update a failedJobs.
     *
     * @param failedJobsDTO the entity to save.
     * @return the persisted entity.
     */
    public FailedJobsDTO update(FailedJobsDTO failedJobsDTO) {
        log.debug("Request to update FailedJobs : {}", failedJobsDTO);
        FailedJobs failedJobs = failedJobsMapper.toEntity(failedJobsDTO);
        failedJobs = failedJobsRepository.save(failedJobs);
        return failedJobsMapper.toDto(failedJobs);
    }

    /**
     * Partially update a failedJobs.
     *
     * @param failedJobsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FailedJobsDTO> partialUpdate(FailedJobsDTO failedJobsDTO) {
        log.debug("Request to partially update FailedJobs : {}", failedJobsDTO);

        return failedJobsRepository
            .findById(failedJobsDTO.getId())
            .map(existingFailedJobs -> {
                failedJobsMapper.partialUpdate(existingFailedJobs, failedJobsDTO);

                return existingFailedJobs;
            })
            .map(failedJobsRepository::save)
            .map(failedJobsMapper::toDto);
    }

    /**
     * Get all the failedJobs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FailedJobsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FailedJobs");
        return failedJobsRepository.findAll(pageable).map(failedJobsMapper::toDto);
    }

    /**
     * Get one failedJobs by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FailedJobsDTO> findOne(Long id) {
        log.debug("Request to get FailedJobs : {}", id);
        return failedJobsRepository.findById(id).map(failedJobsMapper::toDto);
    }

    /**
     * Delete the failedJobs by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FailedJobs : {}", id);
        failedJobsRepository.deleteById(id);
    }
}
