package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.UserNotification;
import com.nullsafe.daily.repository.UserNotificationRepository;
import com.nullsafe.daily.service.dto.UserNotificationDTO;
import com.nullsafe.daily.service.mapper.UserNotificationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.UserNotification}.
 */
@Service
@Transactional
public class UserNotificationService {

    private final Logger log = LoggerFactory.getLogger(UserNotificationService.class);

    private final UserNotificationRepository userNotificationRepository;

    private final UserNotificationMapper userNotificationMapper;

    public UserNotificationService(UserNotificationRepository userNotificationRepository, UserNotificationMapper userNotificationMapper) {
        this.userNotificationRepository = userNotificationRepository;
        this.userNotificationMapper = userNotificationMapper;
    }

    /**
     * Save a userNotification.
     *
     * @param userNotificationDTO the entity to save.
     * @return the persisted entity.
     */
    public UserNotificationDTO save(UserNotificationDTO userNotificationDTO) {
        log.debug("Request to save UserNotification : {}", userNotificationDTO);
        UserNotification userNotification = userNotificationMapper.toEntity(userNotificationDTO);
        userNotification = userNotificationRepository.save(userNotification);
        return userNotificationMapper.toDto(userNotification);
    }

    /**
     * Update a userNotification.
     *
     * @param userNotificationDTO the entity to save.
     * @return the persisted entity.
     */
    public UserNotificationDTO update(UserNotificationDTO userNotificationDTO) {
        log.debug("Request to update UserNotification : {}", userNotificationDTO);
        UserNotification userNotification = userNotificationMapper.toEntity(userNotificationDTO);
        userNotification = userNotificationRepository.save(userNotification);
        return userNotificationMapper.toDto(userNotification);
    }

    /**
     * Partially update a userNotification.
     *
     * @param userNotificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserNotificationDTO> partialUpdate(UserNotificationDTO userNotificationDTO) {
        log.debug("Request to partially update UserNotification : {}", userNotificationDTO);

        return userNotificationRepository
            .findById(userNotificationDTO.getId())
            .map(existingUserNotification -> {
                userNotificationMapper.partialUpdate(existingUserNotification, userNotificationDTO);

                return existingUserNotification;
            })
            .map(userNotificationRepository::save)
            .map(userNotificationMapper::toDto);
    }

    /**
     * Get all the userNotifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserNotificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserNotifications");
        return userNotificationRepository.findAll(pageable).map(userNotificationMapper::toDto);
    }

    /**
     * Get one userNotification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserNotificationDTO> findOne(Long id) {
        log.debug("Request to get UserNotification : {}", id);
        return userNotificationRepository.findById(id).map(userNotificationMapper::toDto);
    }

    /**
     * Delete the userNotification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserNotification : {}", id);
        userNotificationRepository.deleteById(id);
    }
}
