package com.nullsafe.daily.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Files.
 */
@Entity
@Table(name = "files")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Files implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 250)
    @Column(name = "name", length = 250, nullable = false)
    private String name;

    @NotNull
    @Size(max = 65535)
    @Column(name = "file_url", length = 65535, nullable = false)
    private String fileUrl;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    /**
     * 1&#61;admin.2&#61;school table,3&#61;user table, 4&#61; emp tabel
     */
    @NotNull
    @Column(name = "file_for", nullable = false)
    private Integer fileFor;

    @NotNull
    @Column(name = "file_for_id", nullable = false)
    private Integer fileForId;

    /**
     * 1&#61;profile image
     */
    @NotNull
    @Column(name = "file_cat", nullable = false)
    private Boolean fileCat;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Files id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Files name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public Files fileUrl(String fileUrl) {
        this.setFileUrl(fileUrl);
        return this;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Files createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Files updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Files deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getFileFor() {
        return this.fileFor;
    }

    public Files fileFor(Integer fileFor) {
        this.setFileFor(fileFor);
        return this;
    }

    public void setFileFor(Integer fileFor) {
        this.fileFor = fileFor;
    }

    public Integer getFileForId() {
        return this.fileForId;
    }

    public Files fileForId(Integer fileForId) {
        this.setFileForId(fileForId);
        return this;
    }

    public void setFileForId(Integer fileForId) {
        this.fileForId = fileForId;
    }

    public Boolean getFileCat() {
        return this.fileCat;
    }

    public Files fileCat(Boolean fileCat) {
        this.setFileCat(fileCat);
        return this;
    }

    public void setFileCat(Boolean fileCat) {
        this.fileCat = fileCat;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Files)) {
            return false;
        }
        return getId() != null && getId().equals(((Files) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Files{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fileUrl='" + getFileUrl() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", fileFor=" + getFileFor() +
            ", fileForId=" + getFileForId() +
            ", fileCat='" + getFileCat() + "'" +
            "}";
    }
}
