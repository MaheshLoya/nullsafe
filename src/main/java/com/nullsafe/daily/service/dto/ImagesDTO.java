package com.nullsafe.daily.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.Images} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImagesDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 250)
    private String tableName;

    @NotNull
    private Long tableId;

    /**
     * 1&#61; profile image, 2&#61;slider image
     */
    @NotNull
    @Schema(description = "1&#61; profile image, 2&#61;slider image", required = true)
    private Boolean imageType;

    @NotNull
    @Size(max = 65535)
    private String image;

    private Instant createdAt;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Boolean getImageType() {
        return imageType;
    }

    public void setImageType(Boolean imageType) {
        this.imageType = imageType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImagesDTO)) {
            return false;
        }

        ImagesDTO imagesDTO = (ImagesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, imagesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImagesDTO{" +
            "id=" + getId() +
            ", tableName='" + getTableName() + "'" +
            ", tableId=" + getTableId() +
            ", imageType='" + getImageType() + "'" +
            ", image='" + getImage() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
