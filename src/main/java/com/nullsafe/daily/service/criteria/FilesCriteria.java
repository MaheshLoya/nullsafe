package com.nullsafe.daily.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nullsafe.daily.domain.Files} entity. This class is used
 * in {@link com.nullsafe.daily.web.rest.FilesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /files?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FilesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter fileUrl;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private BooleanFilter deleted;

    private IntegerFilter fileFor;

    private IntegerFilter fileForId;

    private BooleanFilter fileCat;

    private Boolean distinct;

    public FilesCriteria() {}

    public FilesCriteria(FilesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.fileUrl = other.fileUrl == null ? null : other.fileUrl.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.deleted = other.deleted == null ? null : other.deleted.copy();
        this.fileFor = other.fileFor == null ? null : other.fileFor.copy();
        this.fileForId = other.fileForId == null ? null : other.fileForId.copy();
        this.fileCat = other.fileCat == null ? null : other.fileCat.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FilesCriteria copy() {
        return new FilesCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getFileUrl() {
        return fileUrl;
    }

    public StringFilter fileUrl() {
        if (fileUrl == null) {
            fileUrl = new StringFilter();
        }
        return fileUrl;
    }

    public void setFileUrl(StringFilter fileUrl) {
        this.fileUrl = fileUrl;
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

    public BooleanFilter getDeleted() {
        return deleted;
    }

    public BooleanFilter deleted() {
        if (deleted == null) {
            deleted = new BooleanFilter();
        }
        return deleted;
    }

    public void setDeleted(BooleanFilter deleted) {
        this.deleted = deleted;
    }

    public IntegerFilter getFileFor() {
        return fileFor;
    }

    public IntegerFilter fileFor() {
        if (fileFor == null) {
            fileFor = new IntegerFilter();
        }
        return fileFor;
    }

    public void setFileFor(IntegerFilter fileFor) {
        this.fileFor = fileFor;
    }

    public IntegerFilter getFileForId() {
        return fileForId;
    }

    public IntegerFilter fileForId() {
        if (fileForId == null) {
            fileForId = new IntegerFilter();
        }
        return fileForId;
    }

    public void setFileForId(IntegerFilter fileForId) {
        this.fileForId = fileForId;
    }

    public BooleanFilter getFileCat() {
        return fileCat;
    }

    public BooleanFilter fileCat() {
        if (fileCat == null) {
            fileCat = new BooleanFilter();
        }
        return fileCat;
    }

    public void setFileCat(BooleanFilter fileCat) {
        this.fileCat = fileCat;
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
        final FilesCriteria that = (FilesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(fileUrl, that.fileUrl) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(deleted, that.deleted) &&
            Objects.equals(fileFor, that.fileFor) &&
            Objects.equals(fileForId, that.fileForId) &&
            Objects.equals(fileCat, that.fileCat) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, fileUrl, createdAt, updatedAt, deleted, fileFor, fileForId, fileCat, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FilesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (fileUrl != null ? "fileUrl=" + fileUrl + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (deleted != null ? "deleted=" + deleted + ", " : "") +
            (fileFor != null ? "fileFor=" + fileFor + ", " : "") +
            (fileForId != null ? "fileForId=" + fileForId + ", " : "") +
            (fileCat != null ? "fileCat=" + fileCat + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
