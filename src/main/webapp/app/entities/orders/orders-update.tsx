import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUsers } from 'app/shared/model/users.model';
import { getEntities as getUsers } from 'app/entities/users/users.reducer';
import { ITransactions } from 'app/shared/model/transactions.model';
import { getEntities as getTransactions } from 'app/entities/transactions/transactions.reducer';
import { IProduct } from 'app/shared/model/product.model';
import { getEntities as getProducts } from 'app/entities/product/product.reducer';
import { IUserAddress } from 'app/shared/model/user-address.model';
import { getEntities as getUserAddresses } from 'app/entities/user-address/user-address.reducer';
import { IOrders } from 'app/shared/model/orders.model';
import { getEntity, updateEntity, createEntity, reset } from './orders.reducer';

export const OrdersUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.users.entities);
  const transactions = useAppSelector(state => state.transactions.entities);
  const products = useAppSelector(state => state.product.entities);
  const userAddresses = useAppSelector(state => state.userAddress.entities);
  const ordersEntity = useAppSelector(state => state.orders.entity);
  const loading = useAppSelector(state => state.orders.loading);
  const updating = useAppSelector(state => state.orders.updating);
  const updateSuccess = useAppSelector(state => state.orders.updateSuccess);

  const handleClose = () => {
    navigate('/orders' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getTransactions({}));
    dispatch(getProducts({}));
    dispatch(getUserAddresses({}));
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
    if (values.orderType !== undefined && typeof values.orderType !== 'number') {
      values.orderType = Number(values.orderType);
    }
    if (values.orderAmount !== undefined && typeof values.orderAmount !== 'number') {
      values.orderAmount = Number(values.orderAmount);
    }
    if (values.price !== undefined && typeof values.price !== 'number') {
      values.price = Number(values.price);
    }
    if (values.mrp !== undefined && typeof values.mrp !== 'number') {
      values.mrp = Number(values.mrp);
    }
    if (values.tax !== undefined && typeof values.tax !== 'number') {
      values.tax = Number(values.tax);
    }
    if (values.qty !== undefined && typeof values.qty !== 'number') {
      values.qty = Number(values.qty);
    }
    if (values.subscriptionType !== undefined && typeof values.subscriptionType !== 'number') {
      values.subscriptionType = Number(values.subscriptionType);
    }
    if (values.status !== undefined && typeof values.status !== 'number') {
      values.status = Number(values.status);
    }
    if (values.deliveryStatus !== undefined && typeof values.deliveryStatus !== 'number') {
      values.deliveryStatus = Number(values.deliveryStatus);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...ordersEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      trasation: transactions.find(it => it.id.toString() === values.trasation.toString()),
      product: products.find(it => it.id.toString() === values.product.toString()),
      address: userAddresses.find(it => it.id.toString() === values.address.toString()),
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
          ...ordersEntity,
          createdAt: convertDateTimeFromServer(ordersEntity.createdAt),
          updatedAt: convertDateTimeFromServer(ordersEntity.updatedAt),
          user: ordersEntity?.user?.id,
          trasation: ordersEntity?.trasation?.id,
          product: ordersEntity?.product?.id,
          address: ordersEntity?.address?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailiesApp.orders.home.createOrEditLabel" data-cy="OrdersCreateUpdateHeading">
            <Translate contentKey="dailiesApp.orders.home.createOrEditLabel">Create or edit a Orders</Translate>
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
                  id="orders-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dailiesApp.orders.orderType')}
                id="orders-orderType"
                name="orderType"
                data-cy="orderType"
                type="text"
              />
              <UncontrolledTooltip target="orderTypeLabel">
                <Translate contentKey="dailiesApp.orders.help.orderType" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('dailiesApp.orders.orderAmount')}
                id="orders-orderAmount"
                name="orderAmount"
                data-cy="orderAmount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.orders.price')}
                id="orders-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.orders.mrp')}
                id="orders-mrp"
                name="mrp"
                data-cy="mrp"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.orders.tax')}
                id="orders-tax"
                name="tax"
                data-cy="tax"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField label={translate('dailiesApp.orders.qty')} id="orders-qty" name="qty" data-cy="qty" type="text" />
              <ValidatedField
                label={translate('dailiesApp.orders.selectedDaysForWeekly')}
                id="orders-selectedDaysForWeekly"
                name="selectedDaysForWeekly"
                data-cy="selectedDaysForWeekly"
                type="text"
                validate={{
                  maxLength: { value: 65535, message: translate('entity.validation.maxlength', { max: 65535 }) },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.orders.startDate')}
                id="orders-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
              />
              <ValidatedField
                label={translate('dailiesApp.orders.subscriptionType')}
                id="orders-subscriptionType"
                name="subscriptionType"
                data-cy="subscriptionType"
                type="text"
              />
              <UncontrolledTooltip target="subscriptionTypeLabel">
                <Translate contentKey="dailiesApp.orders.help.subscriptionType" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('dailiesApp.orders.status')}
                id="orders-status"
                name="status"
                data-cy="status"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <UncontrolledTooltip target="statusLabel">
                <Translate contentKey="dailiesApp.orders.help.status" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('dailiesApp.orders.deliveryStatus')}
                id="orders-deliveryStatus"
                name="deliveryStatus"
                data-cy="deliveryStatus"
                type="text"
              />
              <UncontrolledTooltip target="deliveryStatusLabel">
                <Translate contentKey="dailiesApp.orders.help.deliveryStatus" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('dailiesApp.orders.orderStatus')}
                id="orders-orderStatus"
                name="orderStatus"
                data-cy="orderStatus"
                check
                type="checkbox"
              />
              <UncontrolledTooltip target="orderStatusLabel">
                <Translate contentKey="dailiesApp.orders.help.orderStatus" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('dailiesApp.orders.createdAt')}
                id="orders-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('dailiesApp.orders.updatedAt')}
                id="orders-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="orders-user"
                name="user"
                data-cy="user"
                label={translate('dailiesApp.orders.user')}
                type="select"
                required
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.email}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="orders-trasation"
                name="trasation"
                data-cy="trasation"
                label={translate('dailiesApp.orders.trasation')}
                type="select"
              >
                <option value="" key="0" />
                {transactions
                  ? transactions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="orders-product"
                name="product"
                data-cy="product"
                label={translate('dailiesApp.orders.product')}
                type="select"
                required
              >
                <option value="" key="0" />
                {products
                  ? products.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="orders-address"
                name="address"
                data-cy="address"
                label={translate('dailiesApp.orders.address')}
                type="select"
                required
              >
                <option value="" key="0" />
                {userAddresses
                  ? userAddresses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/orders" replace color="info">
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

export default OrdersUpdate;
