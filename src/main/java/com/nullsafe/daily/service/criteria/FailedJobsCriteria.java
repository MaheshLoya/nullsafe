package com.nullsafe.daily.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nullsafe.daily.domain.FailedJobs} entity. This class is used
 * in {@link com.nullsafe.daily.web.rest.FailedJobsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /failed-jobs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FailedJobsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter uuid;

    private StringFilter connection;

    private StringFilter queue;

    private InstantFilter failedAt;

    private Boolean distinct;

    public FailedJobsCriteria() {}

    public FailedJobsCriteria(FailedJobsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uuid = other.uuid == null ? null : other.uuid.copy();
        this.connection = other.connection == null ? null : other.connection.copy();
        this.queue = other.queue == null ? null : other.queue.copy();
        this.failedAt = other.failedAt == null ? null : other.failedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FailedJobsCriteria copy() {
        return new FailedJobsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUuid() {
        return uuid;
    }

    public StringFilter uuid() {
        if (uuid == null) {
            uuid = new StringFilter();
        }
        return uuid;
    }

    public void setUuid(StringFilter uuid) {
        this.uuid = uuid;
    }

    public StringFilter getConnection() {
        return connection;
    }

    public StringFilter connection() {
        if (connection == null) {
            connection = new StringFilter();
        }
        return connection;
    }

    public void setConnection(StringFilter connection) {
        this.connection = connection;
    }

    public StringFilter getQueue() {
        return queue;
    }

    public StringFilter queue() {
        if (queue == null) {
            queue = new StringFilter();
        }
        return queue;
    }

    public void setQueue(StringFilter queue) {
        this.queue = queue;
    }

    public InstantFilter getFailedAt() {
        return failedAt;
    }

    public InstantFilter failedAt() {
        if (failedAt == null) {
            failedAt = new InstantFilter();
        }
        return failedAt;
    }

    public void setFailedAt(InstantFilter failedAt) {
        this.failedAt = failedAt;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FailedJobsCriteria that = (FailedJobsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(uuid, that.uuid) &&
            Objects.equals(connection, that.connection) &&
            Objects.equals(queue, that.queue) &&
            Objects.equals(failedAt, that.failedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, connection, queue, failedAt, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FailedJobsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (uuid != null ? "uuid=" + uuid + ", " : "") +
            (connection != null ? "connection=" + connection + ", " : "") +
            (queue != null ? "queue=" + queue + ", " : "") +
            (failedAt != null ? "failedAt=" + failedAt + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
