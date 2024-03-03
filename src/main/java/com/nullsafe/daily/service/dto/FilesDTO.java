package com.nullsafe.daily.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.Files} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FilesDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 250)
    private String name;

    @NotNull
    @Size(max = 65535)
    private String fileUrl;

    private Instant createdAt;

    private Instant updatedAt;

    @NotNull
    private Boolean deleted;

    /**
     * 1&#61;admin.2&#61;school table,3&#61;user table, 4&#61; emp tabel
     */
    @NotNull
    @Schema(description = "1&#61;admin.2&#61;school table,3&#61;user table, 4&#61; emp tabel", required = true)
    private Integer fileFor;

    @NotNull
    private Integer fileForId;

    /**
     * 1&#61;profile image
     */
    @NotNull
    @Schema(description = "1&#61;profile image", required = true)
    private Boolean fileCat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getFileFor() {
        return fileFor;
    }

    public void setFileFor(Integer fileFor) {
        this.fileFor = fileFor;
    }

    public Integer getFileForId() {
        return fileForId;
    }

    public void setFileForId(Integer fileForId) {
        this.fileForId = fileForId;
    }

    public Boolean getFileCat() {
        return fileCat;
    }

    public void setFileCat(Boolean fileCat) {
        this.fileCat = fileCat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FilesDTO)) {
            return false;
        }

        FilesDTO filesDTO = (FilesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, filesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FilesDTO{" +
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
