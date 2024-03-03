import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './subscribed-order-delivery.reducer';

export const SubscribedOrderDeliveryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const subscribedOrderDeliveryEntity = useAppSelector(state => state.subscribedOrderDelivery.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subscribedOrderDeliveryDetailsHeading">
          <Translate contentKey="dailiesApp.subscribedOrderDelivery.detail.title">SubscribedOrderDelivery</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{subscribedOrderDeliveryEntity.id}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="dailiesApp.subscribedOrderDelivery.date">Date</Translate>
            </span>
          </dt>
          <dd>
            {subscribedOrderDeliveryEntity.date ? (
              <TextFormat value={subscribedOrderDeliveryEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="paymentMode">
              <Translate contentKey="dailiesApp.subscribedOrderDelivery.paymentMode">Payment Mode</Translate>
            </span>
            <UncontrolledTooltip target="paymentMode">
              <Translate contentKey="dailiesApp.subscribedOrderDelivery.help.paymentMode" />
            </UncontrolledTooltip>
          </dt>
          <dd>{subscribedOrderDeliveryEntity.paymentMode}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.subscribedOrderDelivery.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {subscribedOrderDeliveryEntity.createdAt ? (
              <TextFormat value={subscribedOrderDeliveryEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.subscribedOrderDelivery.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {subscribedOrderDeliveryEntity.updatedAt ? (
              <TextFormat value={subscribedOrderDeliveryEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="dailiesApp.subscribedOrderDelivery.order">Order</Translate>
          </dt>
          <dd>{subscribedOrderDeliveryEntity.order ? subscribedOrderDeliveryEntity.order.id : ''}</dd>
          <dt>
            <Translate contentKey="dailiesApp.subscribedOrderDelivery.entryUser">Entry User</Translate>
          </dt>
          <dd>{subscribedOrderDeliveryEntity.entryUser ? subscribedOrderDeliveryEntity.entryUser.email : ''}</dd>
        </dl>
        <Button tag={Link} to="/subscribed-order-delivery" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscribed-order-delivery/${subscribedOrderDeliveryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubscribedOrderDeliveryDetail;
