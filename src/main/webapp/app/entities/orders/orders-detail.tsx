import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './orders.reducer';

export const OrdersDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ordersEntity = useAppSelector(state => state.orders.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ordersDetailsHeading">
          <Translate contentKey="dailiesApp.orders.detail.title">Orders</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{ordersEntity.id}</dd>
          <dt>
            <span id="orderType">
              <Translate contentKey="dailiesApp.orders.orderType">Order Type</Translate>
            </span>
            <UncontrolledTooltip target="orderType">
              <Translate contentKey="dailiesApp.orders.help.orderType" />
            </UncontrolledTooltip>
          </dt>
          <dd>{ordersEntity.orderType}</dd>
          <dt>
            <span id="orderAmount">
              <Translate contentKey="dailiesApp.orders.orderAmount">Order Amount</Translate>
            </span>
          </dt>
          <dd>{ordersEntity.orderAmount}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="dailiesApp.orders.price">Price</Translate>
            </span>
          </dt>
          <dd>{ordersEntity.price}</dd>
          <dt>
            <span id="mrp">
              <Translate contentKey="dailiesApp.orders.mrp">Mrp</Translate>
            </span>
          </dt>
          <dd>{ordersEntity.mrp}</dd>
          <dt>
            <span id="tax">
              <Translate contentKey="dailiesApp.orders.tax">Tax</Translate>
            </span>
          </dt>
          <dd>{ordersEntity.tax}</dd>
          <dt>
            <span id="qty">
              <Translate contentKey="dailiesApp.orders.qty">Qty</Translate>
            </span>
          </dt>
          <dd>{ordersEntity.qty}</dd>
          <dt>
            <span id="selectedDaysForWeekly">
              <Translate contentKey="dailiesApp.orders.selectedDaysForWeekly">Selected Days For Weekly</Translate>
            </span>
          </dt>
          <dd>{ordersEntity.selectedDaysForWeekly}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="dailiesApp.orders.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {ordersEntity.startDate ? <TextFormat value={ordersEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="subscriptionType">
              <Translate contentKey="dailiesApp.orders.subscriptionType">Subscription Type</Translate>
            </span>
            <UncontrolledTooltip target="subscriptionType">
              <Translate contentKey="dailiesApp.orders.help.subscriptionType" />
            </UncontrolledTooltip>
          </dt>
          <dd>{ordersEntity.subscriptionType}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="dailiesApp.orders.status">Status</Translate>
            </span>
            <UncontrolledTooltip target="status">
              <Translate contentKey="dailiesApp.orders.help.status" />
            </UncontrolledTooltip>
          </dt>
          <dd>{ordersEntity.status}</dd>
          <dt>
            <span id="deliveryStatus">
              <Translate contentKey="dailiesApp.orders.deliveryStatus">Delivery Status</Translate>
            </span>
            <UncontrolledTooltip target="deliveryStatus">
              <Translate contentKey="dailiesApp.orders.help.deliveryStatus" />
            </UncontrolledTooltip>
          </dt>
          <dd>{ordersEntity.deliveryStatus}</dd>
          <dt>
            <span id="orderStatus">
              <Translate contentKey="dailiesApp.orders.orderStatus">Order Status</Translate>
            </span>
            <UncontrolledTooltip target="orderStatus">
              <Translate contentKey="dailiesApp.orders.help.orderStatus" />
            </UncontrolledTooltip>
          </dt>
          <dd>{ordersEntity.orderStatus ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.orders.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{ordersEntity.createdAt ? <TextFormat value={ordersEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.orders.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{ordersEntity.updatedAt ? <TextFormat value={ordersEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="dailiesApp.orders.user">User</Translate>
          </dt>
          <dd>{ordersEntity.user ? ordersEntity.user.email : ''}</dd>
          <dt>
            <Translate contentKey="dailiesApp.orders.trasation">Trasation</Translate>
          </dt>
          <dd>{ordersEntity.trasation ? ordersEntity.trasation.id : ''}</dd>
          <dt>
            <Translate contentKey="dailiesApp.orders.product">Product</Translate>
          </dt>
          <dd>{ordersEntity.product ? ordersEntity.product.id : ''}</dd>
          <dt>
            <Translate contentKey="dailiesApp.orders.address">Address</Translate>
          </dt>
          <dd>{ordersEntity.address ? ordersEntity.address.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/orders" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/orders/${ordersEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrdersDetail;
