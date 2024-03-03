import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISubCat } from 'app/shared/model/sub-cat.model';
import { getEntities as getSubCats } from 'app/entities/sub-cat/sub-cat.reducer';
import { IProduct } from 'app/shared/model/product.model';
import { getEntity, updateEntity, createEntity, reset } from './product.reducer';

export const ProductUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const subCats = useAppSelector(state => state.subCat.entities);
  const productEntity = useAppSelector(state => state.product.entity);
  const loading = useAppSelector(state => state.product.loading);
  const updating = useAppSelector(state => state.product.updating);
  const updateSuccess = useAppSelector(state => state.product.updateSuccess);

  const handleClose = () => {
    navigate('/product' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSubCats({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.stockQty !== undefined && typeof values.stockQty !== 'number') {
      values.stockQty = Number(values.stockQty);
    }
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }
    if (values.tax !== undefined && typeof values.tax !== 'number') {
      values.tax = Number(values.tax);
    }
    if (values.mrp !== undefined && typeof values.mrp !== 'number') {
      values.mrp = Number(values.mrp);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...productEntity,
      ...values,
      subCat: subCats.find(it => it.id.toString() === values.subCat.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...productEntity,
          createdAt: convertDateTimeFromServer(productEntity.createdAt),
          updatedAt: convertDateTimeFromServer(productEntity.updatedAt),
          subCat: productEntity?.subCat?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailiesApp.product.home.createOrEditLabel" data-cy="ProductCreateUpdateHeading">
            <Translate contentKey="dailiesApp.product.home.createOrEditLabel">Create or edit a Product</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="product-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dailiesApp.product.title')}
                id="product-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 250, message: translate('entity.validation.maxlength', { max: 250 }) },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.product.qtyText')}
                id="product-qtyText"
                name="qtyText"
                data-cy="qtyText"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 250, message: translate('entity.validation.maxlength', { max: 250 }) },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.product.stockQty')}
                id="product-stockQty"
                name="stockQty"
                data-cy="stockQty"
                type="text"
              />
              <ValidatedField
                label={translate('dailiesApp.product.price')}
                id="product-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.product.tax')}
                id="product-tax"
                name="tax"
                data-cy="tax"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.product.mrp')}
                id="product-mrp"
                name="mrp"
                data-cy="mrp"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.product.offerText')}
                id="product-offerText"
                name="offerText"
                data-cy="offerText"
                type="text"
                validate={{
                  maxLength: { value: 250, message: translate('entity.validation.maxlength', { max: 250 }) },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.product.description')}
                id="product-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 65535, message: translate('entity.validation.maxlength', { max: 65535 }) },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.product.disclaimer')}
                id="product-disclaimer"
                name="disclaimer"
                data-cy="disclaimer"
                type="text"
                validate={{
                  maxLength: { value: 65535, message: translate('entity.validation.maxlength', { max: 65535 }) },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.product.subscription')}
                id="product-subscription"
                name="subscription"
                data-cy="subscription"
                check
                type="checkbox"
              />
              <UncontrolledTooltip target="subscriptionLabel">
                <Translate contentKey="dailiesApp.product.help.subscription" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('dailiesApp.product.createdAt')}
                id="product-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('dailiesApp.product.updatedAt')}
                id="product-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('dailiesApp.product.isActive')}
                id="product-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <UncontrolledTooltip target="isActiveLabel">
                <Translate contentKey="dailiesApp.product.help.isActive" />
              </UncontrolledTooltip>
              <ValidatedField
                id="product-subCat"
                name="subCat"
                data-cy="subCat"
                label={translate('dailiesApp.product.subCat')}
                type="select"
                required
              >
                <option value="" key="0" />
                {subCats
                  ? subCats.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ProductUpdate;
