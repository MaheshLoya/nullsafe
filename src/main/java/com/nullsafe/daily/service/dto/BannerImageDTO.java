package com.nullsafe.daily.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.BannerImage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BannerImageDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 65535)
    private String image;

    /**
     * 1&#61;mobile
     */
    @NotNull
    @Schema(description = "1&#61;mobile", required = true)
    private Boolean imageType;

    private Instant createdAt;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getImageType() {
        return imageType;
    }

    public void setImageType(Boolean imageType) {
        this.imageType = imageType;
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
        if (!(o instanceof BannerImageDTO)) {
            return false;
        }

        BannerImageDTO bannerImageDTO = (BannerImageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bannerImageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BannerImageDTO{" +
            "id=" + getId() +
            ", image='" + getImage() + "'" +
            ", imageType='" + getImageType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
