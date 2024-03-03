package com.nullsafe.daily.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nullsafe.daily.domain.Images} entity. This class is used
 * in {@link com.nullsafe.daily.web.rest.ImagesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /images?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImagesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tableName;

    private LongFilter tableId;

    private BooleanFilter imageType;

    private StringFilter image;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private Boolean distinct;

    public ImagesCriteria() {}

    public ImagesCriteria(ImagesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tableName = other.tableName == null ? null : other.tableName.copy();
        this.tableId = other.tableId == null ? null : other.tableId.copy();
        this.imageType = other.imageType == null ? null : other.imageType.copy();
        this.image = other.image == null ? null : other.image.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ImagesCriteria copy() {
        return new ImagesCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTableName() {
        return tableName;
    }

    public StringFilter tableName() {
        if (tableName == null) {
            tableName = new StringFilter();
        }
        return tableName;
    }

    public void setTableName(StringFilter tableName) {
        this.tableName = tableName;
    }

    public LongFilter getTableId() {
        return tableId;
    }

    public LongFilter tableId() {
        if (tableId == null) {
            tableId = new LongFilter();
        }
        return tableId;
    }

    public void setTableId(LongFilter tableId) {
        this.tableId = tableId;
    }

    public BooleanFilter getImageType() {
        return imageType;
    }

    public BooleanFilter imageType() {
        if (imageType == null) {
            imageType = new BooleanFilter();
        }
        return imageType;
    }

    public void setImageType(BooleanFilter imageType) {
        this.imageType = imageType;
    }

    public StringFilter getImage() {
        return image;
    }

    public StringFilter image() {
        if (image == null) {
            image = new StringFilter();
        }
        return image;
    }

    public void setImage(StringFilter image) {
        this.image = image;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public InstantFilter createdAt() {
        if (createdAt == null) {
            createdAt = new InstantFilter();
        }
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public InstantFilter updatedAt() {
        if (updatedAt == null) {
            updatedAt = new InstantFilter();
        }
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ImagesCriteria that = (ImagesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tableName, that.tableName) &&
            Objects.equals(tableId, that.tableId) &&
            Objects.equals(imageType, that.imageType) &&
            Objects.equals(image, that.image) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableName, tableId, imageType, image, createdAt, updatedAt, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImagesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tableName != null ? "tableName=" + tableName + ", " : "") +
            (tableId != null ? "tableId=" + tableId + ", " : "") +
            (imageType != null ? "imageType=" + imageType + ", " : "") +
            (image != null ? "image=" + image + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
