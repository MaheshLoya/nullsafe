package com.nullsafe.daily.service.mapper;

import com.nullsafe.daily.domain.FailedJobs;
import com.nullsafe.daily.service.dto.FailedJobsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FailedJobs} and its DTO {@link FailedJobsDTO}.
 */
@Mapper(componentModel = "spring")
public interface FailedJobsMapper extends EntityMapper<FailedJobsDTO, FailedJobs> {}
