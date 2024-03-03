package com.nullsafe.daily.service;

import com.nullsafe.daily.domain.PaymentGateway;
import com.nullsafe.daily.repository.PaymentGatewayRepository;
import com.nullsafe.daily.service.dto.PaymentGatewayDTO;
import com.nullsafe.daily.service.mapper.PaymentGatewayMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nullsafe.daily.domain.PaymentGateway}.
 */
@Service
@Transactional
public class PaymentGatewayService {

    private final Logger log = LoggerFactory.getLogger(PaymentGatewayService.class);

    private final PaymentGatewayRepository paymentGatewayRepository;

    private final PaymentGatewayMapper paymentGatewayMapper;

    public PaymentGatewayService(PaymentGatewayRepository paymentGatewayRepository, PaymentGatewayMapper paymentGatewayMapper) {
        this.paymentGatewayRepository = paymentGatewayRepository;
        this.paymentGatewayMapper = paymentGatewayMapper;
    }

    /**
     * Save a paymentGateway.
     *
     * @param paymentGatewayDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentGatewayDTO save(PaymentGatewayDTO paymentGatewayDTO) {
        log.debug("Request to save PaymentGateway : {}", paymentGatewayDTO);
        PaymentGateway paymentGateway = paymentGatewayMapper.toEntity(paymentGatewayDTO);
        paymentGateway = paymentGatewayRepository.save(paymentGateway);
        return paymentGatewayMapper.toDto(paymentGateway);
    }

    /**
     * Update a paymentGateway.
     *
     * @param paymentGatewayDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentGatewayDTO update(PaymentGatewayDTO paymentGatewayDTO) {
        log.debug("Request to update PaymentGateway : {}", paymentGatewayDTO);
        PaymentGateway paymentGateway = paymentGatewayMapper.toEntity(paymentGatewayDTO);
        paymentGateway = paymentGatewayRepository.save(paymentGateway);
        return paymentGatewayMapper.toDto(paymentGateway);
    }

    /**
     * Partially update a paymentGateway.
     *
     * @param paymentGatewayDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentGatewayDTO> partialUpdate(PaymentGatewayDTO paymentGatewayDTO) {
        log.debug("Request to partially update PaymentGateway : {}", paymentGatewayDTO);

        return paymentGatewayRepository
            .findById(paymentGatewayDTO.getId())
            .map(existingPaymentGateway -> {
                paymentGatewayMapper.partialUpdate(existingPaymentGateway, paymentGatewayDTO);

                return existingPaymentGateway;
            })
            .map(paymentGatewayRepository::save)
            .map(paymentGatewayMapper::toDto);
    }

    /**
     * Get all the paymentGateways.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentGatewayDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentGateways");
        return paymentGatewayRepository.findAll(pageable).map(paymentGatewayMapper::toDto);
    }

    /**
     * Get one paymentGateway by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentGatewayDTO> findOne(Long id) {
        log.debug("Request to get PaymentGateway : {}", id);
        return paymentGatewayRepository.findById(id).map(paymentGatewayMapper::toDto);
    }

    /**
     * Delete the paymentGateway by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PaymentGateway : {}", id);
        paymentGatewayRepository.deleteById(id);
    }
}
