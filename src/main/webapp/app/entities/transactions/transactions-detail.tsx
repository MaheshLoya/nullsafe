import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './transactions.reducer';

export const TransactionsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const transactionsEntity = useAppSelector(state => state.transactions.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="transactionsDetailsHeading">
          <Translate contentKey="dailiesApp.transactions.detail.title">Transactions</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{transactionsEntity.id}</dd>
          <dt>
            <span id="paymentId">
              <Translate contentKey="dailiesApp.transactions.paymentId">Payment Id</Translate>
            </span>
          </dt>
          <dd>{transactionsEntity.paymentId}</dd>
          <dt>
            <span id="amount">
              <Translate contentKey="dailiesApp.transactions.amount">Amount</Translate>
            </span>
          </dt>
          <dd>{transactionsEntity.amount}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="dailiesApp.transactions.description">Description</Translate>
            </span>
          </dt>
          <dd>{transactionsEntity.description}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="dailiesApp.transactions.type">Type</Translate>
            </span>
            <UncontrolledTooltip target="type">
              <Translate contentKey="dailiesApp.transactions.help.type" />
            </UncontrolledTooltip>
          </dt>
          <dd>{transactionsEntity.type}</dd>
          <dt>
            <span id="paymentMode">
              <Translate contentKey="dailiesApp.transactions.paymentMode">Payment Mode</Translate>
            </span>
            <UncontrolledTooltip target="paymentMode">
              <Translate contentKey="dailiesApp.transactions.help.paymentMode" />
            </UncontrolledTooltip>
          </dt>
          <dd>{transactionsEntity.paymentMode}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.transactions.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {transactionsEntity.createdAt ? <TextFormat value={transactionsEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.transactions.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {transactionsEntity.updatedAt ? <TextFormat value={transactionsEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="dailiesApp.transactions.order">Order</Translate>
          </dt>
          <dd>{transactionsEntity.order ? transactionsEntity.order.id : ''}</dd>
          <dt>
            <Translate contentKey="dailiesApp.transactions.user">User</Translate>
          </dt>
          <dd>{transactionsEntity.user ? transactionsEntity.user.email : ''}</dd>
        </dl>
        <Button tag={Link} to="/transactions" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/transactions/${transactionsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TransactionsDetail;
