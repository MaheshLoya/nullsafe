package com.nullsafe.daily.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.WebAppSettings} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WebAppSettingsDTO implements Serializable {

    @NotNull
    private Integer id;

    @NotNull
    @Size(max = 250)
    private String title;

    @NotNull
    @Size(max = 65535)
    private String value;

    private Instant createdAt;

    private Instant updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        if (!(o instanceof WebAppSettingsDTO)) {
            return false;
        }

        WebAppSettingsDTO webAppSettingsDTO = (WebAppSettingsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, webAppSettingsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WebAppSettingsDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", value='" + getValue() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
