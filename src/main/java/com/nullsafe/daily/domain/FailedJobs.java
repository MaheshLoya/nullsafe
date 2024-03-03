package com.nullsafe.daily.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A FailedJobs.
 */
@Entity
@Table(name = "failed_jobs")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FailedJobs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "uuid", length = 255, nullable = false, unique = true)
    private String uuid;

    @NotNull
    @Size(max = 65535)
    @Column(name = "connection", length = 65535, nullable = false)
    private String connection;

    @NotNull
    @Size(max = 65535)
    @Column(name = "queue", length = 65535, nullable = false)
    private String queue;

    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    @Lob
    @Column(name = "exception", nullable = false)
    private String exception;

    @NotNull
    @Column(name = "failed_at", nullable = false)
    private Instant failedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FailedJobs id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public FailedJobs uuid(String uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getConnection() {
        return this.connection;
    }

    public FailedJobs connection(String connection) {
        this.setConnection(connection);
        return this;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getQueue() {
        return this.queue;
    }

    public FailedJobs queue(String queue) {
        this.setQueue(queue);
        return this;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getPayload() {
        return this.payload;
    }

    public FailedJobs payload(String payload) {
        this.setPayload(payload);
        return this;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getException() {
        return this.exception;
    }

    public FailedJobs exception(String exception) {
        this.setException(exception);
        return this;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Instant getFailedAt() {
        return this.failedAt;
    }

    public FailedJobs failedAt(Instant failedAt) {
        this.setFailedAt(failedAt);
        return this;
    }

    public void setFailedAt(Instant failedAt) {
        this.failedAt = failedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FailedJobs)) {
            return false;
        }
        return getId() != null && getId().equals(((FailedJobs) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FailedJobs{" +
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
