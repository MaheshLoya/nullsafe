import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './payment-gateway.reducer';

export const PaymentGatewayDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paymentGatewayEntity = useAppSelector(state => state.paymentGateway.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paymentGatewayDetailsHeading">
          <Translate contentKey="dailiesApp.paymentGateway.detail.title">PaymentGateway</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paymentGatewayEntity.id}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="dailiesApp.paymentGateway.active">Active</Translate>
            </span>
          </dt>
          <dd>{paymentGatewayEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="dailiesApp.paymentGateway.title">Title</Translate>
            </span>
          </dt>
          <dd>{paymentGatewayEntity.title}</dd>
          <dt>
            <span id="keyId">
              <Translate contentKey="dailiesApp.paymentGateway.keyId">Key Id</Translate>
            </span>
          </dt>
          <dd>{paymentGatewayEntity.keyId}</dd>
          <dt>
            <span id="secretId">
              <Translate contentKey="dailiesApp.paymentGateway.secretId">Secret Id</Translate>
            </span>
          </dt>
          <dd>{paymentGatewayEntity.secretId}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.paymentGateway.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {paymentGatewayEntity.createdAt ? (
              <TextFormat value={paymentGatewayEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.paymentGateway.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {paymentGatewayEntity.updatedAt ? (
              <TextFormat value={paymentGatewayEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/payment-gateway" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/payment-gateway/${paymentGatewayEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaymentGatewayDetail;
