import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product.reducer';

export const ProductDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productEntity = useAppSelector(state => state.product.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productDetailsHeading">
          <Translate contentKey="dailiesApp.product.detail.title">Product</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="dailiesApp.product.title">Title</Translate>
            </span>
          </dt>
          <dd>{productEntity.title}</dd>
          <dt>
            <span id="qtyText">
              <Translate contentKey="dailiesApp.product.qtyText">Qty Text</Translate>
            </span>
          </dt>
          <dd>{productEntity.qtyText}</dd>
          <dt>
            <span id="stockQty">
              <Translate contentKey="dailiesApp.product.stockQty">Stock Qty</Translate>
            </span>
          </dt>
          <dd>{productEntity.stockQty}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="dailiesApp.product.price">Price</Translate>
            </span>
          </dt>
          <dd>{productEntity.price}</dd>
          <dt>
            <span id="tax">
              <Translate contentKey="dailiesApp.product.tax">Tax</Translate>
            </span>
          </dt>
          <dd>{productEntity.tax}</dd>
          <dt>
            <span id="mrp">
              <Translate contentKey="dailiesApp.product.mrp">Mrp</Translate>
            </span>
          </dt>
          <dd>{productEntity.mrp}</dd>
          <dt>
            <span id="offerText">
              <Translate contentKey="dailiesApp.product.offerText">Offer Text</Translate>
            </span>
          </dt>
          <dd>{productEntity.offerText}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="dailiesApp.product.description">Description</Translate>
            </span>
          </dt>
          <dd>{productEntity.description}</dd>
          <dt>
            <span id="disclaimer">
              <Translate contentKey="dailiesApp.product.disclaimer">Disclaimer</Translate>
            </span>
          </dt>
          <dd>{productEntity.disclaimer}</dd>
          <dt>
            <span id="subscription">
              <Translate contentKey="dailiesApp.product.subscription">Subscription</Translate>
            </span>
            <UncontrolledTooltip target="subscription">
              <Translate contentKey="dailiesApp.product.help.subscription" />
            </UncontrolledTooltip>
          </dt>
          <dd>{productEntity.subscription ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.product.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{productEntity.createdAt ? <TextFormat value={productEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.product.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{productEntity.updatedAt ? <TextFormat value={productEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dailiesApp.product.isActive">Is Active</Translate>
            </span>
            <UncontrolledTooltip target="isActive">
              <Translate contentKey="dailiesApp.product.help.isActive" />
            </UncontrolledTooltip>
          </dt>
          <dd>{productEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="dailiesApp.product.subCat">Sub Cat</Translate>
          </dt>
          <dd>{productEntity.subCat ? productEntity.subCat.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/product" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product/${productEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductDetail;
