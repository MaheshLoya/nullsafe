package com.nullsafe.daily.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nullsafe.daily.domain.PersonalAccessTokens} entity. This class is used
 * in {@link com.nullsafe.daily.web.rest.PersonalAccessTokensResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /personal-access-tokens?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonalAccessTokensCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tokenableType;

    private LongFilter tokenableId;

    private StringFilter name;

    private StringFilter token;

    private StringFilter abilities;

    private InstantFilter lastUsedAt;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private Boolean distinct;

    public PersonalAccessTokensCriteria() {}

    public PersonalAccessTokensCriteria(PersonalAccessTokensCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tokenableType = other.tokenableType == null ? null : other.tokenableType.copy();
        this.tokenableId = other.tokenableId == null ? null : other.tokenableId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.token = other.token == null ? null : other.token.copy();
        this.abilities = other.abilities == null ? null : other.abilities.copy();
        this.lastUsedAt = other.lastUsedAt == null ? null : other.lastUsedAt.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PersonalAccessTokensCriteria copy() {
        return new PersonalAccessTokensCriteria(this);
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

    public StringFilter getTokenableType() {
        return tokenableType;
    }

    public StringFilter tokenableType() {
        if (tokenableType == null) {
            tokenableType = new StringFilter();
        }
        return tokenableType;
    }

    public void setTokenableType(StringFilter tokenableType) {
        this.tokenableType = tokenableType;
    }

    public LongFilter getTokenableId() {
        return tokenableId;
    }

    public LongFilter tokenableId() {
        if (tokenableId == null) {
            tokenableId = new LongFilter();
        }
        return tokenableId;
    }

    public void setTokenableId(LongFilter tokenableId) {
        this.tokenableId = tokenableId;
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

    public StringFilter getToken() {
        return token;
    }

    public StringFilter token() {
        if (token == null) {
            token = new StringFilter();
        }
        return token;
    }

    public void setToken(StringFilter token) {
        this.token = token;
    }

    public StringFilter getAbilities() {
        return abilities;
    }

    public StringFilter abilities() {
        if (abilities == null) {
            abilities = new StringFilter();
        }
        return abilities;
    }

    public void setAbilities(StringFilter abilities) {
        this.abilities = abilities;
    }

    public InstantFilter getLastUsedAt() {
        return lastUsedAt;
    }

    public InstantFilter lastUsedAt() {
        if (lastUsedAt == null) {
            lastUsedAt = new InstantFilter();
        }
        return lastUsedAt;
    }

    public void setLastUsedAt(InstantFilter lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
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
        final PersonalAccessTokensCriteria that = (PersonalAccessTokensCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tokenableType, that.tokenableType) &&
            Objects.equals(tokenableId, that.tokenableId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(token, that.token) &&
            Objects.equals(abilities, that.abilities) &&
            Objects.equals(lastUsedAt, that.lastUsedAt) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tokenableType, tokenableId, name, token, abilities, lastUsedAt, createdAt, updatedAt, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonalAccessTokensCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tokenableType != null ? "tokenableType=" + tokenableType + ", " : "") +
            (tokenableId != null ? "tokenableId=" + tokenableId + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (token != null ? "token=" + token + ", " : "") +
            (abilities != null ? "abilities=" + abilities + ", " : "") +
            (lastUsedAt != null ? "lastUsedAt=" + lastUsedAt + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
