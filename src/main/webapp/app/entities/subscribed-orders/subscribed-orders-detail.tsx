import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './subscribed-orders.reducer';

export const SubscribedOrdersDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const subscribedOrdersEntity = useAppSelector(state => state.subscribedOrders.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subscribedOrdersDetailsHeading">
          <Translate contentKey="dailiesApp.subscribedOrders.detail.title">SubscribedOrders</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{subscribedOrdersEntity.id}</dd>
          <dt>
            <span id="paymentType">
              <Translate contentKey="dailiesApp.subscribedOrders.paymentType">Payment Type</Translate>
            </span>
            <UncontrolledTooltip target="paymentType">
              <Translate contentKey="dailiesApp.subscribedOrders.help.paymentType" />
            </UncontrolledTooltip>
          </dt>
          <dd>{subscribedOrdersEntity.paymentType}</dd>
          <dt>
            <span id="orderAmount">
              <Translate contentKey="dailiesApp.subscribedOrders.orderAmount">Order Amount</Translate>
            </span>
          </dt>
          <dd>{subscribedOrdersEntity.orderAmount}</dd>
          <dt>
            <span id="subscriptionBalanceAmount">
              <Translate contentKey="dailiesApp.subscribedOrders.subscriptionBalanceAmount">Subscription Balance Amount</Translate>
            </span>
          </dt>
          <dd>{subscribedOrdersEntity.subscriptionBalanceAmount}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="dailiesApp.subscribedOrders.price">Price</Translate>
            </span>
          </dt>
          <dd>{subscribedOrdersEntity.price}</dd>
          <dt>
            <span id="mrp">
              <Translate contentKey="dailiesApp.subscribedOrders.mrp">Mrp</Translate>
            </span>
          </dt>
          <dd>{subscribedOrdersEntity.mrp}</dd>
          <dt>
            <span id="tax">
              <Translate contentKey="dailiesApp.subscribedOrders.tax">Tax</Translate>
            </span>
          </dt>
          <dd>{subscribedOrdersEntity.tax}</dd>
          <dt>
            <span id="qty">
              <Translate contentKey="dailiesApp.subscribedOrders.qty">Qty</Translate>
            </span>
          </dt>
          <dd>{subscribedOrdersEntity.qty}</dd>
          <dt>
            <span id="offerId">
              <Translate contentKey="dailiesApp.subscribedOrders.offerId">Offer Id</Translate>
            </span>
          </dt>
          <dd>{subscribedOrdersEntity.offerId}</dd>
          <dt>
            <span id="selectedDaysForWeekly">
              <Translate contentKey="dailiesApp.subscribedOrders.selectedDaysForWeekly">Selected Days For Weekly</Translate>
            </span>
          </dt>
          <dd>{subscribedOrdersEntity.selectedDaysForWeekly}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="dailiesApp.subscribedOrders.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {subscribedOrdersEntity.startDate ? (
              <TextFormat value={subscribedOrdersEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="dailiesApp.subscribedOrders.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {subscribedOrdersEntity.endDate ? (
              <TextFormat value={subscribedOrdersEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastRenewalDate">
              <Translate contentKey="dailiesApp.subscribedOrders.lastRenewalDate">Last Renewal Date</Translate>
            </span>
          </dt>
          <dd>
            {subscribedOrdersEntity.lastRenewalDate ? (
              <TextFormat value={subscribedOrdersEntity.lastRenewalDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="subscriptionType">
              <Translate contentKey="dailiesApp.subscribedOrders.subscriptionType">Subscription Type</Translate>
            </span>
            <UncontrolledTooltip target="subscriptionType">
              <Translate contentKey="dailiesApp.subscribedOrders.help.subscriptionType" />
            </UncontrolledTooltip>
          </dt>
          <dd>{subscribedOrdersEntity.subscriptionType}</dd>
          <dt>
            <span id="approvalStatus">
              <Translate contentKey="dailiesApp.subscribedOrders.approvalStatus">Approval Status</Translate>
            </span>
            <UncontrolledTooltip target="approvalStatus">
              <Translate contentKey="dailiesApp.subscribedOrders.help.approvalStatus" />
            </UncontrolledTooltip>
          </dt>
          <dd>{subscribedOrdersEntity.approvalStatus}</dd>
          <dt>
            <span id="orderStatus">
              <Translate contentKey="dailiesApp.subscribedOrders.orderStatus">Order Status</Translate>
            </span>
            <UncontrolledTooltip target="orderStatus">
              <Translate contentKey="dailiesApp.subscribedOrders.help.orderStatus" />
            </UncontrolledTooltip>
          </dt>
          <dd>{subscribedOrdersEntity.orderStatus ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.subscribedOrders.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {subscribedOrdersEntity.createdAt ? (
              <TextFormat value={subscribedOrdersEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.subscribedOrders.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {subscribedOrdersEntity.updatedAt ? (
              <TextFormat value={subscribedOrdersEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dailiesApp.subscribedOrders.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{subscribedOrdersEntity.createdBy}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="dailiesApp.subscribedOrders.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{subscribedOrdersEntity.updatedBy}</dd>
          <dt>
            <Translate contentKey="dailiesApp.subscribedOrders.user">User</Translate>
          </dt>
          <dd>{subscribedOrdersEntity.user ? subscribedOrdersEntity.user.email : ''}</dd>
          <dt>
            <Translate contentKey="dailiesApp.subscribedOrders.transaction">Transaction</Translate>
          </dt>
          <dd>{subscribedOrdersEntity.transaction ? subscribedOrdersEntity.transaction.id : ''}</dd>
          <dt>
            <Translate contentKey="dailiesApp.subscribedOrders.product">Product</Translate>
          </dt>
          <dd>{subscribedOrdersEntity.product ? subscribedOrdersEntity.product.id : ''}</dd>
          <dt>
            <Translate contentKey="dailiesApp.subscribedOrders.address">Address</Translate>
          </dt>
          <dd>{subscribedOrdersEntity.address ? subscribedOrdersEntity.address.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/subscribed-orders" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/subscribed-orders/${subscribedOrdersEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubscribedOrdersDetail;
