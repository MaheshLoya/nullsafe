package com.nullsafe.daily.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Images.
 */
@Entity
@Table(name = "images")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Images implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 250)
    @Column(name = "table_name", length = 250, nullable = false)
    private String tableName;

    @NotNull
    @Column(name = "table_id", nullable = false)
    private Long tableId;

    /**
     * 1&#61; profile image, 2&#61;slider image
     */
    @NotNull
    @Column(name = "image_type", nullable = false)
    private Boolean imageType;

    @NotNull
    @Size(max = 65535)
    @Column(name = "image", length = 65535, nullable = false)
    private String image;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Images id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return this.tableName;
    }

    public Images tableName(String tableName) {
        this.setTableName(tableName);
        return this;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getTableId() {
        return this.tableId;
    }

    public Images tableId(Long tableId) {
        this.setTableId(tableId);
        return this;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Boolean getImageType() {
        return this.imageType;
    }

    public Images imageType(Boolean imageType) {
        this.setImageType(imageType);
        return this;
    }

    public void setImageType(Boolean imageType) {
        this.imageType = imageType;
    }

    public String getImage() {
        return this.image;
    }

    public Images image(String image) {
        this.setImage(image);
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Images createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Images updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Images)) {
            return false;
        }
        return getId() != null && getId().equals(((Images) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Images{" +
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
