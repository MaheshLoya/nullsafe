package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.PasswordResets;
import com.nullsafe.daily.repository.PasswordResetsRepository;
import com.nullsafe.daily.service.dto.PasswordResetsDTO;
import com.nullsafe.daily.service.mapper.PasswordResetsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.PasswordResets}.
 */
@Service
@Transactional
public class PasswordResetsService {

    private final Logger log = LoggerFactory.getLogger(PasswordResetsService.class);

    private final PasswordResetsRepository passwordResetsRepository;

    private final PasswordResetsMapper passwordResetsMapper;

    public PasswordResetsService(PasswordResetsRepository passwordResetsRepository, PasswordResetsMapper passwordResetsMapper) {
        this.passwordResetsRepository = passwordResetsRepository;
        this.passwordResetsMapper = passwordResetsMapper;
    }

    /**
     * Save a passwordResets.
     *
     * @param passwordResetsDTO the entity to save.
     * @return the persisted entity.
     */
    public PasswordResetsDTO save(PasswordResetsDTO passwordResetsDTO) {
        log.debug("Request to save PasswordResets : {}", passwordResetsDTO);
        PasswordResets passwordResets = passwordResetsMapper.toEntity(passwordResetsDTO);
        passwordResets = passwordResetsRepository.save(passwordResets);
        return passwordResetsMapper.toDto(passwordResets);
    }

    /**
     * Update a passwordResets.
     *
     * @param passwordResetsDTO the entity to save.
     * @return the persisted entity.
     */
    public PasswordResetsDTO update(PasswordResetsDTO passwordResetsDTO) {
        log.debug("Request to update PasswordResets : {}", passwordResetsDTO);
        PasswordResets passwordResets = passwordResetsMapper.toEntity(passwordResetsDTO);
        passwordResets = passwordResetsRepository.save(passwordResets);
        return passwordResetsMapper.toDto(passwordResets);
    }

    /**
     * Partially update a passwordResets.
     *
     * @param passwordResetsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PasswordResetsDTO> partialUpdate(PasswordResetsDTO passwordResetsDTO) {
        log.debug("Request to partially update PasswordResets : {}", passwordResetsDTO);

        return passwordResetsRepository
            .findById(passwordResetsDTO.getId())
            .map(existingPasswordResets -> {
                passwordResetsMapper.partialUpdate(existingPasswordResets, passwordResetsDTO);

                return existingPasswordResets;
            })
            .map(passwordResetsRepository::save)
            .map(passwordResetsMapper::toDto);
    }

    /**
     * Get all the passwordResets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PasswordResetsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PasswordResets");
        return passwordResetsRepository.findAll(pageable).map(passwordResetsMapper::toDto);
    }

    /**
     * Get one passwordResets by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PasswordResetsDTO> findOne(Long id) {
        log.debug("Request to get PasswordResets : {}", id);
        return passwordResetsRepository.findById(id).map(passwordResetsMapper::toDto);
    }

    /**
     * Delete the passwordResets by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PasswordResets : {}", id);
        passwordResetsRepository.deleteById(id);
    }
}
