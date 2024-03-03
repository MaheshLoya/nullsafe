package com.nullsafe.daily.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.WebPages} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WebPagesDTO implements Serializable {

    private Long id;

    /**
     * 1&#61;about us,2&#61;privacy,3&#61;terms
     */
    @NotNull
    @Schema(description = "1&#61;about us,2&#61;privacy,3&#61;terms", required = true)
    private Integer pageId;

    @NotNull
    @Size(max = 250)
    private String title;

    @NotNull
    @Size(max = 65535)
    private String body;

    private Instant createdAt;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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
        if (!(o instanceof WebPagesDTO)) {
            return false;
        }

        WebPagesDTO webPagesDTO = (WebPagesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, webPagesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WebPagesDTO{" +
            "id=" + getId() +
            ", pageId=" + getPageId() +
            ", title='" + getTitle() + "'" +
            ", body='" + getBody() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
