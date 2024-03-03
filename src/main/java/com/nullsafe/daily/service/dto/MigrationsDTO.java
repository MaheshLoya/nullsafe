package com.nullsafe.daily.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.Migrations} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MigrationsDTO implements Serializable {

    @NotNull
    private Integer id;

    @NotNull
    @Size(max = 255)
    private String migration;

    @NotNull
    private Integer batch;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMigration() {
        return migration;
    }

    public void setMigration(String migration) {
        this.migration = migration;
    }

    public Integer getBatch() {
        return batch;
    }

    public void setBatch(Integer batch) {
        this.batch = batch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MigrationsDTO)) {
            return false;
        }

        MigrationsDTO migrationsDTO = (MigrationsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, migrationsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MigrationsDTO{" +
            "id=" + getId() +
            ", migration='" + getMigration() + "'" +
            ", batch=" + getBatch() +
            "}";
    }
}
