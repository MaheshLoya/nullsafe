import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './subscription-renewal.reducer';

export const SubscriptionRenewalDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const subscriptionRenewalEntity = useAppSelector(state => state.subscriptionRenewal.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subscriptionRenewalDetailsHeading">
          <Translate contentKey="dailiesApp.subscriptionRenewal.detail.title">SubscriptionRenewal</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{subscriptionRenewalEntity.id}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="dailiesApp.subscriptionRenewal.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{subscriptionRenewalEntity.userId}</dd>
          <dt>
            <span id="orderId">
              <Translate contentKey="dailiesApp.subscriptionRenewal.orderId">Order Id</Translate>
            </span>
          </dt>
          <dd>{subscriptionRenewalEntity.orderId}</dd>
          <dt>
            <span id="transactionId">
              <Translate contentKey="dailiesApp.subscriptionRenewal.transactionId">Transaction Id</Translate>
            </span>
          </dt>
          <dd>{subscriptionRenewalEntity.transactionId}</dd>
          <dt>
            <span id="renewalDate">
              <Translate contentKey="dailiesApp.subscriptionRenewal.renewalDate">Renewal Date</Translate>
            </span>
          </dt>
          <dd>
            {subscriptionRenewalEntity.renewalDate ? (
              <TextFormat value={subscriptionRenewalEntity.renewalDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="paidRenewalAmount">
              <Translate contentKey="dailiesApp.subscriptionRenewal.paidRenewalAmount">Paid Renewal Amount</Translate>
            </span>
          </dt>
          <dd>{subscriptionRenewalEntity.paidRenewalAmount}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="dailiesApp.subscriptionRenewal.status">Status</Translate>
            </span>
            <UncontrolledTooltip target="status">
              <Translate contentKey="dailiesApp.subscriptionRenewal.help.status" />
            </UncontrolledTooltip>
          </dt>
          <dd>{subscriptionRenewalEntity.status ? 'true' : 'false'}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="dailiesApp.subscriptionRenewal.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {subscriptionRenewalEntity.startDate ? (
              <TextFormat value={subscriptionRenewalEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="dailiesApp.subscriptionRenewal.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {subscriptionRenewalEntity.endDate ? (
              <TextFormat value={subscriptionRenewalEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.subscriptionRenewal.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {subscriptionRenewalEntity.createdAt ? (
              <TextFormat value={subscriptionRenewalEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.subscriptionRenewal.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {subscriptionRenewalEntity.updatedAt ? (
              <TextFormat value={subscriptionRenewalEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/subscription-renewal" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscription-renewal/${subscriptionRenewalEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubscriptionRenewalDetail;
