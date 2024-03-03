package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.InvoiceSetting;
import com.nullsafe.daily.repository.InvoiceSettingRepository;
import com.nullsafe.daily.service.dto.InvoiceSettingDTO;
import com.nullsafe.daily.service.mapper.InvoiceSettingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.InvoiceSetting}.
 */
@Service
@Transactional
public class InvoiceSettingService {

    private final Logger log = LoggerFactory.getLogger(InvoiceSettingService.class);

    private final InvoiceSettingRepository invoiceSettingRepository;

    private final InvoiceSettingMapper invoiceSettingMapper;

    public InvoiceSettingService(InvoiceSettingRepository invoiceSettingRepository, InvoiceSettingMapper invoiceSettingMapper) {
        this.invoiceSettingRepository = invoiceSettingRepository;
        this.invoiceSettingMapper = invoiceSettingMapper;
    }

    /**
     * Save a invoiceSetting.
     *
     * @param invoiceSettingDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceSettingDTO save(InvoiceSettingDTO invoiceSettingDTO) {
        log.debug("Request to save InvoiceSetting : {}", invoiceSettingDTO);
        InvoiceSetting invoiceSetting = invoiceSettingMapper.toEntity(invoiceSettingDTO);
        invoiceSetting = invoiceSettingRepository.save(invoiceSetting);
        return invoiceSettingMapper.toDto(invoiceSetting);
    }

    /**
     * Update a invoiceSetting.
     *
     * @param invoiceSettingDTO the entity to save.
     * @return the persisted entity.
     */
    public InvoiceSettingDTO update(InvoiceSettingDTO invoiceSettingDTO) {
        log.debug("Request to update InvoiceSetting : {}", invoiceSettingDTO);
        InvoiceSetting invoiceSetting = invoiceSettingMapper.toEntity(invoiceSettingDTO);
        invoiceSetting = invoiceSettingRepository.save(invoiceSetting);
        return invoiceSettingMapper.toDto(invoiceSetting);
    }

    /**
     * Partially update a invoiceSetting.
     *
     * @param invoiceSettingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InvoiceSettingDTO> partialUpdate(InvoiceSettingDTO invoiceSettingDTO) {
        log.debug("Request to partially update InvoiceSetting : {}", invoiceSettingDTO);

        return invoiceSettingRepository
            .findById(invoiceSettingDTO.getId())
            .map(existingInvoiceSetting -> {
                invoiceSettingMapper.partialUpdate(existingInvoiceSetting, invoiceSettingDTO);

                return existingInvoiceSetting;
            })
            .map(invoiceSettingRepository::save)
            .map(invoiceSettingMapper::toDto);
    }

    /**
     * Get all the invoiceSettings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InvoiceSettingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all InvoiceSettings");
        return invoiceSettingRepository.findAll(pageable).map(invoiceSettingMapper::toDto);
    }

    /**
     * Get one invoiceSetting by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvoiceSettingDTO> findOne(Long id) {
        log.debug("Request to get InvoiceSetting : {}", id);
        return invoiceSettingRepository.findById(id).map(invoiceSettingMapper::toDto);
    }

    /**
     * Delete the invoiceSetting by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InvoiceSetting : {}", id);
        invoiceSettingRepository.deleteById(id);
    }
}
