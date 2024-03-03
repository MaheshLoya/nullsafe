package com.nullsafe.daily.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.nullsafe.daily.domain.UserHoliday} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserHolidayDTO implements Serializable {

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private LocalDate date;

    private Instant createdAt;

    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
        if (!(o instanceof UserHolidayDTO)) {
            return false;
        }

        UserHolidayDTO userHolidayDTO = (UserHolidayDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userHolidayDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserHolidayDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", date='" + getDate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
