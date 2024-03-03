import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './available-delivery-location.reducer';

export const AvailableDeliveryLocationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const availableDeliveryLocationEntity = useAppSelector(state => state.availableDeliveryLocation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="availableDeliveryLocationDetailsHeading">
          <Translate contentKey="dailiesApp.availableDeliveryLocation.detail.title">AvailableDeliveryLocation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{availableDeliveryLocationEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="dailiesApp.availableDeliveryLocation.title">Title</Translate>
            </span>
          </dt>
          <dd>{availableDeliveryLocationEntity.title}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.availableDeliveryLocation.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {availableDeliveryLocationEntity.createdAt ? (
              <TextFormat value={availableDeliveryLocationEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.availableDeliveryLocation.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {availableDeliveryLocationEntity.updatedAt ? (
              <TextFormat value={availableDeliveryLocationEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/available-delivery-location" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/available-delivery-location/${availableDeliveryLocationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AvailableDeliveryLocationDetail;
