package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.UserHoliday;
import com.nullsafe.daily.repository.UserHolidayRepository;
import com.nullsafe.daily.service.dto.UserHolidayDTO;
import com.nullsafe.daily.service.mapper.UserHolidayMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.UserHoliday}.
 */
@Service
@Transactional
public class UserHolidayService {

    private final Logger log = LoggerFactory.getLogger(UserHolidayService.class);

    private final UserHolidayRepository userHolidayRepository;

    private final UserHolidayMapper userHolidayMapper;

    public UserHolidayService(UserHolidayRepository userHolidayRepository, UserHolidayMapper userHolidayMapper) {
        this.userHolidayRepository = userHolidayRepository;
        this.userHolidayMapper = userHolidayMapper;
    }

    /**
     * Save a userHoliday.
     *
     * @param userHolidayDTO the entity to save.
     * @return the persisted entity.
     */
    public UserHolidayDTO save(UserHolidayDTO userHolidayDTO) {
        log.debug("Request to save UserHoliday : {}", userHolidayDTO);
        UserHoliday userHoliday = userHolidayMapper.toEntity(userHolidayDTO);
        userHoliday = userHolidayRepository.save(userHoliday);
        return userHolidayMapper.toDto(userHoliday);
    }

    /**
     * Update a userHoliday.
     *
     * @param userHolidayDTO the entity to save.
     * @return the persisted entity.
     */
    public UserHolidayDTO update(UserHolidayDTO userHolidayDTO) {
        log.debug("Request to update UserHoliday : {}", userHolidayDTO);
        UserHoliday userHoliday = userHolidayMapper.toEntity(userHolidayDTO);
        userHoliday = userHolidayRepository.save(userHoliday);
        return userHolidayMapper.toDto(userHoliday);
    }

    /**
     * Partially update a userHoliday.
     *
     * @param userHolidayDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserHolidayDTO> partialUpdate(UserHolidayDTO userHolidayDTO) {
        log.debug("Request to partially update UserHoliday : {}", userHolidayDTO);

        return userHolidayRepository
            .findById(userHolidayDTO.getId())
            .map(existingUserHoliday -> {
                userHolidayMapper.partialUpdate(existingUserHoliday, userHolidayDTO);

                return existingUserHoliday;
            })
            .map(userHolidayRepository::save)
            .map(userHolidayMapper::toDto);
    }

    /**
     * Get all the userHolidays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserHolidayDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserHolidays");
        return userHolidayRepository.findAll(pageable).map(userHolidayMapper::toDto);
    }

    /**
     * Get one userHoliday by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserHolidayDTO> findOne(Long id) {
        log.debug("Request to get UserHoliday : {}", id);
        return userHolidayRepository.findById(id).map(userHolidayMapper::toDto);
    }

    /**
     * Delete the userHoliday by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserHoliday : {}", id);
        userHolidayRepository.deleteById(id);
    }
}
