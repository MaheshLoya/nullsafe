package com.nullsafe.daily.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Cat.
 */
@Entity
@Table(name = "cat")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 250)
    @Column(name = "title", length = 250, nullable = false)
    private String title;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * 0 is Inactive, 1 is Active
     */
    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cat")
    @JsonIgnoreProperties(value = { "cat", "products" }, allowSetters = true)
    private Set<SubCat> subCats = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Cat title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Cat createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Cat updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Cat isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<SubCat> getSubCats() {
        return this.subCats;
    }

    public void setSubCats(Set<SubCat> subCats) {
        if (this.subCats != null) {
            this.subCats.forEach(i -> i.setCat(null));
        }
        if (subCats != null) {
            subCats.forEach(i -> i.setCat(this));
        }
        this.subCats = subCats;
    }

    public Cat subCats(Set<SubCat> subCats) {
        this.setSubCats(subCats);
        return this;
    }

    public Cat addSubCat(SubCat subCat) {
        this.subCats.add(subCat);
        subCat.setCat(this);
        return this;
    }

    public Cat removeSubCat(SubCat subCat) {
        this.subCats.remove(subCat);
        subCat.setCat(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cat)) {
            return false;
        }
        return getId() != null && getId().equals(((Cat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cat{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
