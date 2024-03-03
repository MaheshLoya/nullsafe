package com.nullsafe.daily.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Migrations.
 */
@Entity
@Table(name = "migrations")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Migrations implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Size(max = 255)
    @Column(name = "migration", length = 255, nullable = false)
    private String migration;

    @NotNull
    @Column(name = "batch", nullable = false)
    private Integer batch;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return this.id;
    }

    public Migrations id(Integer id) {
        this.setId(id);
        return this;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMigration() {
        return this.migration;
    }

    public Migrations migration(String migration) {
        this.setMigration(migration);
        return this;
    }

    public void setMigration(String migration) {
        this.migration = migration;
    }

    public Integer getBatch() {
        return this.batch;
    }

    public Migrations batch(Integer batch) {
        this.setBatch(batch);
        return this;
    }

    public void setBatch(Integer batch) {
        this.batch = batch;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Migrations)) {
            return false;
        }
        return getId() != null && getId().equals(((Migrations) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Migrations{" +
            "id=" + getId() +
            ", migration='" + getMigration() + "'" +
            ", batch=" + getBatch() +
            "}";
    }
}
