package com.nullsafe.daily.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nullsafe.daily.domain.Migrations} entity. This class is used
 * in {@link com.nullsafe.daily.web.rest.MigrationsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /migrations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MigrationsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private IntegerFilter id;

    private StringFilter migration;

    private IntegerFilter batch;

    private Boolean distinct;

    public MigrationsCriteria() {}

    public MigrationsCriteria(MigrationsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.migration = other.migration == null ? null : other.migration.copy();
        this.batch = other.batch == null ? null : other.batch.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MigrationsCriteria copy() {
        return new MigrationsCriteria(this);
    }

    public IntegerFilter getId() {
        return id;
    }

    public IntegerFilter id() {
        if (id == null) {
            id = new IntegerFilter();
        }
        return id;
    }

    public void setId(IntegerFilter id) {
        this.id = id;
    }

    public StringFilter getMigration() {
        return migration;
    }

    public StringFilter migration() {
        if (migration == null) {
            migration = new StringFilter();
        }
        return migration;
    }

    public void setMigration(StringFilter migration) {
        this.migration = migration;
    }

    public IntegerFilter getBatch() {
        return batch;
    }

    public IntegerFilter batch() {
        if (batch == null) {
            batch = new IntegerFilter();
        }
        return batch;
    }

    public void setBatch(IntegerFilter batch) {
        this.batch = batch;
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
        final MigrationsCriteria that = (MigrationsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(migration, that.migration) &&
            Objects.equals(batch, that.batch) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, migration, batch, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MigrationsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (migration != null ? "migration=" + migration + ", " : "") +
            (batch != null ? "batch=" + batch + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
