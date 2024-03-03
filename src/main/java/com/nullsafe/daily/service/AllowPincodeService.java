package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.AllowPincode;
import com.nullsafe.daily.repository.AllowPincodeRepository;
import com.nullsafe.daily.service.dto.AllowPincodeDTO;
import com.nullsafe.daily.service.mapper.AllowPincodeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.AllowPincode}.
 */
@Service
@Transactional
public class AllowPincodeService {

    private final Logger log = LoggerFactory.getLogger(AllowPincodeService.class);

    private final AllowPincodeRepository allowPincodeRepository;

    private final AllowPincodeMapper allowPincodeMapper;

    public AllowPincodeService(AllowPincodeRepository allowPincodeRepository, AllowPincodeMapper allowPincodeMapper) {
        this.allowPincodeRepository = allowPincodeRepository;
        this.allowPincodeMapper = allowPincodeMapper;
    }

    /**
     * Save a allowPincode.
     *
     * @param allowPincodeDTO the entity to save.
     * @return the persisted entity.
     */
    public AllowPincodeDTO save(AllowPincodeDTO allowPincodeDTO) {
        log.debug("Request to save AllowPincode : {}", allowPincodeDTO);
        AllowPincode allowPincode = allowPincodeMapper.toEntity(allowPincodeDTO);
        allowPincode = allowPincodeRepository.save(allowPincode);
        return allowPincodeMapper.toDto(allowPincode);
    }

    /**
     * Update a allowPincode.
     *
     * @param allowPincodeDTO the entity to save.
     * @return the persisted entity.
     */
    public AllowPincodeDTO update(AllowPincodeDTO allowPincodeDTO) {
        log.debug("Request to update AllowPincode : {}", allowPincodeDTO);
        AllowPincode allowPincode = allowPincodeMapper.toEntity(allowPincodeDTO);
        allowPincode = allowPincodeRepository.save(allowPincode);
        return allowPincodeMapper.toDto(allowPincode);
    }

    /**
     * Partially update a allowPincode.
     *
     * @param allowPincodeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AllowPincodeDTO> partialUpdate(AllowPincodeDTO allowPincodeDTO) {
        log.debug("Request to partially update AllowPincode : {}", allowPincodeDTO);

        return allowPincodeRepository
            .findById(allowPincodeDTO.getId())
            .map(existingAllowPincode -> {
                allowPincodeMapper.partialUpdate(existingAllowPincode, allowPincodeDTO);

                return existingAllowPincode;
            })
            .map(allowPincodeRepository::save)
            .map(allowPincodeMapper::toDto);
    }

    /**
     * Get all the allowPincodes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AllowPincodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AllowPincodes");
        return allowPincodeRepository.findAll(pageable).map(allowPincodeMapper::toDto);
    }

    /**
     * Get one allowPincode by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AllowPincodeDTO> findOne(Long id) {
        log.debug("Request to get AllowPincode : {}", id);
        return allowPincodeRepository.findById(id).map(allowPincodeMapper::toDto);
    }

    /**
     * Delete the allowPincode by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AllowPincode : {}", id);
        allowPincodeRepository.deleteById(id);
    }
}
