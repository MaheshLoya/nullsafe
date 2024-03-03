package com.nullsafe.daily.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.FailedJobs} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FailedJobsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String uuid;

    @NotNull
    @Size(max = 65535)
    private String connection;

    @NotNull
    @Size(max = 65535)
    private String queue;

    @Lob
    private String payload;

    @Lob
    private String exception;

    @NotNull
    private Instant failedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Instant getFailedAt() {
        return failedAt;
    }

    public void setFailedAt(Instant failedAt) {
        this.failedAt = failedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FailedJobsDTO)) {
            return false;
        }

        FailedJobsDTO failedJobsDTO = (FailedJobsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, failedJobsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FailedJobsDTO{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", connection='" + getConnection() + "'" +
            ", queue='" + getQueue() + "'" +
            ", payload='" + getPayload() + "'" +
            ", exception='" + getException() + "'" +
            ", failedAt='" + getFailedAt() + "'" +
            "}";
    }
}
