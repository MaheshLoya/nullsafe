import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './order-user-assign.reducer';

export const OrderUserAssignDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const orderUserAssignEntity = useAppSelector(state => state.orderUserAssign.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="orderUserAssignDetailsHeading">
          <Translate contentKey="dailiesApp.orderUserAssign.detail.title">OrderUserAssign</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{orderUserAssignEntity.id}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.orderUserAssign.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {orderUserAssignEntity.createdAt ? (
              <TextFormat value={orderUserAssignEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.orderUserAssign.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {orderUserAssignEntity.updatedAt ? (
              <TextFormat value={orderUserAssignEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="dailiesApp.orderUserAssign.order">Order</Translate>
          </dt>
          <dd>{orderUserAssignEntity.order ? orderUserAssignEntity.order.id : ''}</dd>
          <dt>
            <Translate contentKey="dailiesApp.orderUserAssign.user">User</Translate>
          </dt>
          <dd>{orderUserAssignEntity.user ? orderUserAssignEntity.user.email : ''}</dd>
        </dl>
        <Button tag={Link} to="/order-user-assign" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/order-user-assign/${orderUserAssignEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrderUserAssignDetail;
