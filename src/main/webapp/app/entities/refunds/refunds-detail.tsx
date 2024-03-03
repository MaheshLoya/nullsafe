import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './refunds.reducer';

export const RefundsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const refundsEntity = useAppSelector(state => state.refunds.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="refundsDetailsHeading">
          <Translate contentKey="dailiesApp.refunds.detail.title">Refunds</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="dailiesApp.refunds.id">Id</Translate>
            </span>
          </dt>
          <dd>{refundsEntity.id}</dd>
          <dt>
            <span id="orderId">
              <Translate contentKey="dailiesApp.refunds.orderId">Order Id</Translate>
            </span>
          </dt>
          <dd>{refundsEntity.orderId}</dd>
          <dt>
            <span id="transactionId">
              <Translate contentKey="dailiesApp.refunds.transactionId">Transaction Id</Translate>
            </span>
          </dt>
          <dd>{refundsEntity.transactionId}</dd>
          <dt>
            <span id="razorpayRefundId">
              <Translate contentKey="dailiesApp.refunds.razorpayRefundId">Razorpay Refund Id</Translate>
            </span>
          </dt>
          <dd>{refundsEntity.razorpayRefundId}</dd>
          <dt>
            <span id="razorpayPaymentId">
              <Translate contentKey="dailiesApp.refunds.razorpayPaymentId">Razorpay Payment Id</Translate>
            </span>
          </dt>
          <dd>{refundsEntity.razorpayPaymentId}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="dailiesApp.refunds.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{refundsEntity.amount}</dd>
          <dt>
            <span id="currency">
              <Translate contentKey="dailiesApp.refunds.currency">Currency</Translate>
            </span>
          </dt>
          <dd>{refundsEntity.currency}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="dailiesApp.refunds.status">Status</Translate>
            </span>
          </dt>
          <dd>{refundsEntity.status}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dailiesApp.refunds.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{refundsEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.refunds.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{refundsEntity.createdAt ? <TextFormat value={refundsEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.refunds.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{refundsEntity.updatedAt ? <TextFormat value={refundsEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/refunds" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/refunds/${refundsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RefundsDetail;
