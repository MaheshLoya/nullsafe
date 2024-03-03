package com.nullsafe.daily.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.nullsafe.daily.domain.UserAddress} entity. This class is used
 * in {@link com.nullsafe.daily.web.rest.UserAddressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-addresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAddressCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter userId;

    private StringFilter name;

    private StringFilter sPhone;

    private StringFilter flatNo;

    private StringFilter apartmentName;

    private StringFilter area;

    private StringFilter landmark;

    private StringFilter city;

    private IntegerFilter pincode;

    private DoubleFilter lat;

    private DoubleFilter lng;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private BooleanFilter isActive;

    private LongFilter ordersId;

    private LongFilter subscribedOrdersId;

    private Boolean distinct;

    public UserAddressCriteria() {}

    public UserAddressCriteria(UserAddressCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.sPhone = other.sPhone == null ? null : other.sPhone.copy();
        this.flatNo = other.flatNo == null ? null : other.flatNo.copy();
        this.apartmentName = other.apartmentName == null ? null : other.apartmentName.copy();
        this.area = other.area == null ? null : other.area.copy();
        this.landmark = other.landmark == null ? null : other.landmark.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.pincode = other.pincode == null ? null : other.pincode.copy();
        this.lat = other.lat == null ? null : other.lat.copy();
        this.lng = other.lng == null ? null : other.lng.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.ordersId = other.ordersId == null ? null : other.ordersId.copy();
        this.subscribedOrdersId = other.subscribedOrdersId == null ? null : other.subscribedOrdersId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public UserAddressCriteria copy() {
        return new UserAddressCriteria(this);
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

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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

    public StringFilter getsPhone() {
        return sPhone;
    }

    public StringFilter sPhone() {
        if (sPhone == null) {
            sPhone = new StringFilter();
        }
        return sPhone;
    }

    public void setsPhone(StringFilter sPhone) {
        this.sPhone = sPhone;
    }

    public StringFilter getFlatNo() {
        return flatNo;
    }

    public StringFilter flatNo() {
        if (flatNo == null) {
            flatNo = new StringFilter();
        }
        return flatNo;
    }

    public void setFlatNo(StringFilter flatNo) {
        this.flatNo = flatNo;
    }

    public StringFilter getApartmentName() {
        return apartmentName;
    }

    public StringFilter apartmentName() {
        if (apartmentName == null) {
            apartmentName = new StringFilter();
        }
        return apartmentName;
    }

    public void setApartmentName(StringFilter apartmentName) {
        this.apartmentName = apartmentName;
    }

    public StringFilter getArea() {
        return area;
    }

    public StringFilter area() {
        if (area == null) {
            area = new StringFilter();
        }
        return area;
    }

    public void setArea(StringFilter area) {
        this.area = area;
    }

    public StringFilter getLandmark() {
        return landmark;
    }

    public StringFilter landmark() {
        if (landmark == null) {
            landmark = new StringFilter();
        }
        return landmark;
    }

    public void setLandmark(StringFilter landmark) {
        this.landmark = landmark;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city() {
        if (city == null) {
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public IntegerFilter getPincode() {
        return pincode;
    }

    public IntegerFilter pincode() {
        if (pincode == null) {
            pincode = new IntegerFilter();
        }
        return pincode;
    }

    public void setPincode(IntegerFilter pincode) {
        this.pincode = pincode;
    }

    public DoubleFilter getLat() {
        return lat;
    }

    public DoubleFilter lat() {
        if (lat == null) {
            lat = new DoubleFilter();
        }
        return lat;
    }

    public void setLat(DoubleFilter lat) {
        this.lat = lat;
    }

    public DoubleFilter getLng() {
        return lng;
    }

    public DoubleFilter lng() {
        if (lng == null) {
            lng = new DoubleFilter();
        }
        return lng;
    }

    public void setLng(DoubleFilter lng) {
        this.lng = lng;
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

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getOrdersId() {
        return ordersId;
    }

    public LongFilter ordersId() {
        if (ordersId == null) {
            ordersId = new LongFilter();
        }
        return ordersId;
    }

    public void setOrdersId(LongFilter ordersId) {
        this.ordersId = ordersId;
    }

    public LongFilter getSubscribedOrdersId() {
        return subscribedOrdersId;
    }

    public LongFilter subscribedOrdersId() {
        if (subscribedOrdersId == null) {
            subscribedOrdersId = new LongFilter();
        }
        return subscribedOrdersId;
    }

    public void setSubscribedOrdersId(LongFilter subscribedOrdersId) {
        this.subscribedOrdersId = subscribedOrdersId;
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
        final UserAddressCriteria that = (UserAddressCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(sPhone, that.sPhone) &&
            Objects.equals(flatNo, that.flatNo) &&
            Objects.equals(apartmentName, that.apartmentName) &&
            Objects.equals(area, that.area) &&
            Objects.equals(landmark, that.landmark) &&
            Objects.equals(city, that.city) &&
            Objects.equals(pincode, that.pincode) &&
            Objects.equals(lat, that.lat) &&
            Objects.equals(lng, that.lng) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(ordersId, that.ordersId) &&
            Objects.equals(subscribedOrdersId, that.subscribedOrdersId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            userId,
            name,
            sPhone,
            flatNo,
            apartmentName,
            area,
            landmark,
            city,
            pincode,
            lat,
            lng,
            createdAt,
            updatedAt,
            isActive,
            ordersId,
            subscribedOrdersId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAddressCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (sPhone != null ? "sPhone=" + sPhone + ", " : "") +
            (flatNo != null ? "flatNo=" + flatNo + ", " : "") +
            (apartmentName != null ? "apartmentName=" + apartmentName + ", " : "") +
            (area != null ? "area=" + area + ", " : "") +
            (landmark != null ? "landmark=" + landmark + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (pincode != null ? "pincode=" + pincode + ", " : "") +
            (lat != null ? "lat=" + lat + ", " : "") +
            (lng != null ? "lng=" + lng + ", " : "") +
            (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
            (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (ordersId != null ? "ordersId=" + ordersId + ", " : "") +
            (subscribedOrdersId != null ? "subscribedOrdersId=" + subscribedOrdersId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
