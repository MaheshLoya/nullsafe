package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.PersonalAccessTokens;
import com.nullsafe.daily.repository.PersonalAccessTokensRepository;
import com.nullsafe.daily.service.dto.PersonalAccessTokensDTO;
import com.nullsafe.daily.service.mapper.PersonalAccessTokensMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.PersonalAccessTokens}.
 */
@Service
@Transactional
public class PersonalAccessTokensService {

    private final Logger log = LoggerFactory.getLogger(PersonalAccessTokensService.class);

    private final PersonalAccessTokensRepository personalAccessTokensRepository;

    private final PersonalAccessTokensMapper personalAccessTokensMapper;

    public PersonalAccessTokensService(
        PersonalAccessTokensRepository personalAccessTokensRepository,
        PersonalAccessTokensMapper personalAccessTokensMapper
    ) {
        this.personalAccessTokensRepository = personalAccessTokensRepository;
        this.personalAccessTokensMapper = personalAccessTokensMapper;
    }

    /**
     * Save a personalAccessTokens.
     *
     * @param personalAccessTokensDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonalAccessTokensDTO save(PersonalAccessTokensDTO personalAccessTokensDTO) {
        log.debug("Request to save PersonalAccessTokens : {}", personalAccessTokensDTO);
        PersonalAccessTokens personalAccessTokens = personalAccessTokensMapper.toEntity(personalAccessTokensDTO);
        personalAccessTokens = personalAccessTokensRepository.save(personalAccessTokens);
        return personalAccessTokensMapper.toDto(personalAccessTokens);
    }

    /**
     * Update a personalAccessTokens.
     *
     * @param personalAccessTokensDTO the entity to save.
     * @return the persisted entity.
     */
    public PersonalAccessTokensDTO update(PersonalAccessTokensDTO personalAccessTokensDTO) {
        log.debug("Request to update PersonalAccessTokens : {}", personalAccessTokensDTO);
        PersonalAccessTokens personalAccessTokens = personalAccessTokensMapper.toEntity(personalAccessTokensDTO);
        personalAccessTokens = personalAccessTokensRepository.save(personalAccessTokens);
        return personalAccessTokensMapper.toDto(personalAccessTokens);
    }

    /**
     * Partially update a personalAccessTokens.
     *
     * @param personalAccessTokensDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PersonalAccessTokensDTO> partialUpdate(PersonalAccessTokensDTO personalAccessTokensDTO) {
        log.debug("Request to partially update PersonalAccessTokens : {}", personalAccessTokensDTO);

        return personalAccessTokensRepository
            .findById(personalAccessTokensDTO.getId())
            .map(existingPersonalAccessTokens -> {
                personalAccessTokensMapper.partialUpdate(existingPersonalAccessTokens, personalAccessTokensDTO);

                return existingPersonalAccessTokens;
            })
            .map(personalAccessTokensRepository::save)
            .map(personalAccessTokensMapper::toDto);
    }

    /**
     * Get all the personalAccessTokens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonalAccessTokensDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PersonalAccessTokens");
        return personalAccessTokensRepository.findAll(pageable).map(personalAccessTokensMapper::toDto);
    }

    /**
     * Get one personalAccessTokens by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonalAccessTokensDTO> findOne(Long id) {
        log.debug("Request to get PersonalAccessTokens : {}", id);
        return personalAccessTokensRepository.findById(id).map(personalAccessTokensMapper::toDto);
    }

    /**
     * Delete the personalAccessTokens by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PersonalAccessTokens : {}", id);
        personalAccessTokensRepository.deleteById(id);
    }
}
