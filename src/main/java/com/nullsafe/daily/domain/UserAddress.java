package com.nullsafe.daily.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A UserAddress.
 */
@Entity
@Table(name = "user_address")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Size(max = 250)
    @Column(name = "name", length = 250, nullable = false)
    private String name;

    @NotNull
    @Size(max = 250)
    @Column(name = "s_phone", length = 250, nullable = false)
    private String sPhone;

    @Size(max = 250)
    @Column(name = "flat_no", length = 250)
    private String flatNo;

    @Size(max = 250)
    @Column(name = "apartment_name", length = 250)
    private String apartmentName;

    @NotNull
    @Size(max = 250)
    @Column(name = "area", length = 250, nullable = false)
    private String area;

    @NotNull
    @Size(max = 250)
    @Column(name = "landmark", length = 250, nullable = false)
    private String landmark;

    @NotNull
    @Size(max = 250)
    @Column(name = "city", length = 250, nullable = false)
    private String city;

    @NotNull
    @Column(name = "pincode", nullable = false)
    private Integer pincode;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lng")
    private Double lng;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "address")
    @JsonIgnoreProperties(
        value = { "user", "trasation", "product", "address", "orderUserAssigns", "subscribedOrderDeliveries", "transactions" },
        allowSetters = true
    )
    private Set<Orders> orders = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "address")
    @JsonIgnoreProperties(value = { "user", "transaction", "product", "address" }, allowSetters = true)
    private Set<SubscribedOrders> subscribedOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserAddress id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public UserAddress userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return this.name;
    }

    public UserAddress name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getsPhone() {
        return this.sPhone;
    }

    public UserAddress sPhone(String sPhone) {
        this.setsPhone(sPhone);
        return this;
    }

    public void setsPhone(String sPhone) {
        this.sPhone = sPhone;
    }

    public String getFlatNo() {
        return this.flatNo;
    }

    public UserAddress flatNo(String flatNo) {
        this.setFlatNo(flatNo);
        return this;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getApartmentName() {
        return this.apartmentName;
    }

    public UserAddress apartmentName(String apartmentName) {
        this.setApartmentName(apartmentName);
        return this;
    }

    public void setApartmentName(String apartmentName) {
        this.apartmentName = apartmentName;
    }

    public String getArea() {
        return this.area;
    }

    public UserAddress area(String area) {
        this.setArea(area);
        return this;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLandmark() {
        return this.landmark;
    }

    public UserAddress landmark(String landmark) {
        this.setLandmark(landmark);
        return this;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return this.city;
    }

    public UserAddress city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPincode() {
        return this.pincode;
    }

    public UserAddress pincode(Integer pincode) {
        this.setPincode(pincode);
        return this;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }

    public Double getLat() {
        return this.lat;
    }

    public UserAddress lat(Double lat) {
        this.setLat(lat);
        return this;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return this.lng;
    }

    public UserAddress lng(Double lng) {
        this.setLng(lng);
        return this;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public UserAddress createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public UserAddress updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public UserAddress isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Orders> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Orders> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setAddress(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setAddress(this));
        }
        this.orders = orders;
    }

    public UserAddress orders(Set<Orders> orders) {
        this.setOrders(orders);
        return this;
    }

    public UserAddress addOrders(Orders orders) {
        this.orders.add(orders);
        orders.setAddress(this);
        return this;
    }

    public UserAddress removeOrders(Orders orders) {
        this.orders.remove(orders);
        orders.setAddress(null);
        return this;
    }

    public Set<SubscribedOrders> getSubscribedOrders() {
        return this.subscribedOrders;
    }

    public void setSubscribedOrders(Set<SubscribedOrders> subscribedOrders) {
        if (this.subscribedOrders != null) {
            this.subscribedOrders.forEach(i -> i.setAddress(null));
        }
        if (subscribedOrders != null) {
            subscribedOrders.forEach(i -> i.setAddress(this));
        }
        this.subscribedOrders = subscribedOrders;
    }

    public UserAddress subscribedOrders(Set<SubscribedOrders> subscribedOrders) {
        this.setSubscribedOrders(subscribedOrders);
        return this;
    }

    public UserAddress addSubscribedOrders(SubscribedOrders subscribedOrders) {
        this.subscribedOrders.add(subscribedOrders);
        subscribedOrders.setAddress(this);
        return this;
    }

    public UserAddress removeSubscribedOrders(SubscribedOrders subscribedOrders) {
        this.subscribedOrders.remove(subscribedOrders);
        subscribedOrders.setAddress(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAddress)) {
            return false;
        }
        return getId() != null && getId().equals(((UserAddress) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAddress{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", name='" + getName() + "'" +
            ", sPhone='" + getsPhone() + "'" +
            ", flatNo='" + getFlatNo() + "'" +
            ", apartmentName='" + getApartmentName() + "'" +
            ", area='" + getArea() + "'" +
            ", landmark='" + getLandmark() + "'" +
            ", city='" + getCity() + "'" +
            ", pincode=" + getPincode() +
            ", lat=" + getLat() +
            ", lng=" + getLng() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
