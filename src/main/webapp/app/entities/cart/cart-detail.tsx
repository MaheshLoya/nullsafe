import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cart.reducer';

export const CartDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cartEntity = useAppSelector(state => state.cart.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cartDetailsHeading">
          <Translate contentKey="dailiesApp.cart.detail.title">Cart</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cartEntity.id}</dd>
          <dt>
            <span id="qty">
              <Translate contentKey="dailiesApp.cart.qty">Qty</Translate>
            </span>
          </dt>
          <dd>{cartEntity.qty}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="dailiesApp.cart.price">Price</Translate>
            </span>
          </dt>
          <dd>{cartEntity.price}</dd>
          <dt>
            <span id="totalPrice">
              <Translate contentKey="dailiesApp.cart.totalPrice">Total Price</Translate>
            </span>
          </dt>
          <dd>{cartEntity.totalPrice}</dd>
          <dt>
            <span id="mrp">
              <Translate contentKey="dailiesApp.cart.mrp">Mrp</Translate>
            </span>
          </dt>
          <dd>{cartEntity.mrp}</dd>
          <dt>
            <span id="tax">
              <Translate contentKey="dailiesApp.cart.tax">Tax</Translate>
            </span>
          </dt>
          <dd>{cartEntity.tax}</dd>
          <dt>
            <span id="qtyText">
              <Translate contentKey="dailiesApp.cart.qtyText">Qty Text</Translate>
            </span>
          </dt>
          <dd>{cartEntity.qtyText}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.cart.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{cartEntity.createdAt ? <TextFormat value={cartEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.cart.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{cartEntity.updatedAt ? <TextFormat value={cartEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="dailiesApp.cart.product">Product</Translate>
          </dt>
          <dd>{cartEntity.product ? cartEntity.product.id : ''}</dd>
          <dt>
            <Translate contentKey="dailiesApp.cart.user">User</Translate>
          </dt>
          <dd>{cartEntity.user ? cartEntity.user.email : ''}</dd>
        </dl>
        <Button tag={Link} to="/cart" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cart/${cartEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CartDetail;
