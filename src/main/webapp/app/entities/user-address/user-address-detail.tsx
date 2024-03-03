import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-address.reducer';

export const UserAddressDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userAddressEntity = useAppSelector(state => state.userAddress.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userAddressDetailsHeading">
          <Translate contentKey="dailiesApp.userAddress.detail.title">UserAddress</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.id}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="dailiesApp.userAddress.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.userId}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="dailiesApp.userAddress.name">Name</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.name}</dd>
          <dt>
            <span id="sPhone">
              <Translate contentKey="dailiesApp.userAddress.sPhone">S Phone</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.sPhone}</dd>
          <dt>
            <span id="flatNo">
              <Translate contentKey="dailiesApp.userAddress.flatNo">Flat No</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.flatNo}</dd>
          <dt>
            <span id="apartmentName">
              <Translate contentKey="dailiesApp.userAddress.apartmentName">Apartment Name</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.apartmentName}</dd>
          <dt>
            <span id="area">
              <Translate contentKey="dailiesApp.userAddress.area">Area</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.area}</dd>
          <dt>
            <span id="landmark">
              <Translate contentKey="dailiesApp.userAddress.landmark">Landmark</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.landmark}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="dailiesApp.userAddress.city">City</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.city}</dd>
          <dt>
            <span id="pincode">
              <Translate contentKey="dailiesApp.userAddress.pincode">Pincode</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.pincode}</dd>
          <dt>
            <span id="lat">
              <Translate contentKey="dailiesApp.userAddress.lat">Lat</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.lat}</dd>
          <dt>
            <span id="lng">
              <Translate contentKey="dailiesApp.userAddress.lng">Lng</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.lng}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.userAddress.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {userAddressEntity.createdAt ? <TextFormat value={userAddressEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.userAddress.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {userAddressEntity.updatedAt ? <TextFormat value={userAddressEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dailiesApp.userAddress.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{userAddressEntity.isActive ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/user-address" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-address/${userAddressEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserAddressDetail;
