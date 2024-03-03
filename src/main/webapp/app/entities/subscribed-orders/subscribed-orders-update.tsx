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
import { ISubscribedOrders } from 'app/shared/model/subscribed-orders.model';
import { getEntity, updateEntity, createEntity, reset } from './subscribed-orders.reducer';

export const SubscribedOrdersUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.users.entities);
  const transactions = useAppSelector(state => state.transactions.entities);
  const products = useAppSelector(state => state.product.entities);
  const userAddresses = useAppSelector(state => state.userAddress.entities);
  const subscribedOrdersEntity = useAppSelector(state => state.subscribedOrders.entity);
  const loading = useAppSelector(state => state.subscribedOrders.loading);
  const updating = useAppSelector(state => state.subscribedOrders.updating);
  const updateSuccess = useAppSelector(state => state.subscribedOrders.updateSuccess);

  const handleClose = () => {
    navigate('/subscribed-orders' + location.search);
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
    if (values.paymentType !== undefined && typeof values.paymentType !== 'number') {
      values.paymentType = Number(values.paymentType);
    }
    if (values.orderAmount !== undefined && typeof values.orderAmount !== 'number') {
      values.orderAmount = Number(values.orderAmount);
    }
    if (values.subscriptionBalanceAmount !== undefined && typeof values.subscriptionBalanceAmount !== 'number') {
      values.subscriptionBalanceAmount = Number(values.subscriptionBalanceAmount);
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
    if (values.offerId !== undefined && typeof values.offerId !== 'number') {
      values.offerId = Number(values.offerId);
    }
    if (values.subscriptionType !== undefined && typeof values.subscriptionType !== 'number') {
      values.subscriptionType = Number(values.subscriptionType);
    }
    if (values.approvalStatus !== undefined && typeof values.approvalStatus !== 'number') {
      values.approvalStatus = Number(values.approvalStatus);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...subscribedOrdersEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      transaction: transactions.find(it => it.id.toString() === values.transaction.toString()),
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
          ...subscribedOrdersEntity,
          createdAt: convertDateTimeFromServer(subscribedOrdersEntity.createdAt),
          updatedAt: convertDateTimeFromServer(subscribedOrdersEntity.updatedAt),
          user: subscribedOrdersEntity?.user?.id,
          transaction: subscribedOrdersEntity?.transaction?.id,
          product: subscribedOrdersEntity?.product?.id,
          address: subscribedOrdersEntity?.address?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailiesApp.subscribedOrders.home.createOrEditLabel" data-cy="SubscribedOrdersCreateUpdateHeading">
            <Translate contentKey="dailiesApp.subscribedOrders.home.createOrEditLabel">Create or edit a SubscribedOrders</Translate>
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
                  id="subscribed-orders-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.paymentType')}
                id="subscribed-orders-paymentType"
                name="paymentType"
                data-cy="paymentType"
                type="text"
              />
              <UncontrolledTooltip target="paymentTypeLabel">
                <Translate contentKey="dailiesApp.subscribedOrders.help.paymentType" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.orderAmount')}
                id="subscribed-orders-orderAmount"
                name="orderAmount"
                data-cy="orderAmount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.subscriptionBalanceAmount')}
                id="subscribed-orders-subscriptionBalanceAmount"
                name="subscriptionBalanceAmount"
                data-cy="subscriptionBalanceAmount"
                type="text"
              />
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.price')}
                id="subscribed-orders-price"
                name="price"
                data-cy="price"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.mrp')}
                id="subscribed-orders-mrp"
                name="mrp"
                data-cy="mrp"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.tax')}
                id="subscribed-orders-tax"
                name="tax"
                data-cy="tax"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.qty')}
                id="subscribed-orders-qty"
                name="qty"
                data-cy="qty"
                type="text"
              />
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.offerId')}
                id="subscribed-orders-offerId"
                name="offerId"
                data-cy="offerId"
                type="text"
              />
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.selectedDaysForWeekly')}
                id="subscribed-orders-selectedDaysForWeekly"
                name="selectedDaysForWeekly"
                data-cy="selectedDaysForWeekly"
                type="text"
                validate={{
                  maxLength: { value: 65535, message: translate('entity.validation.maxlength', { max: 65535 }) },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.startDate')}
                id="subscribed-orders-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.endDate')}
                id="subscribed-orders-endDate"
                name="endDate"
                data-cy="endDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.lastRenewalDate')}
                id="subscribed-orders-lastRenewalDate"
                name="lastRenewalDate"
                data-cy="lastRenewalDate"
                type="date"
              />
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.subscriptionType')}
                id="subscribed-orders-subscriptionType"
                name="subscriptionType"
                data-cy="subscriptionType"
                type="text"
              />
              <UncontrolledTooltip target="subscriptionTypeLabel">
                <Translate contentKey="dailiesApp.subscribedOrders.help.subscriptionType" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.approvalStatus')}
                id="subscribed-orders-approvalStatus"
                name="approvalStatus"
                data-cy="approvalStatus"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <UncontrolledTooltip target="approvalStatusLabel">
                <Translate contentKey="dailiesApp.subscribedOrders.help.approvalStatus" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.orderStatus')}
                id="subscribed-orders-orderStatus"
                name="orderStatus"
                data-cy="orderStatus"
                check
                type="checkbox"
              />
              <UncontrolledTooltip target="orderStatusLabel">
                <Translate contentKey="dailiesApp.subscribedOrders.help.orderStatus" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.createdAt')}
                id="subscribed-orders-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.updatedAt')}
                id="subscribed-orders-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.createdBy')}
                id="subscribed-orders-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.subscribedOrders.updatedBy')}
                id="subscribed-orders-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                id="subscribed-orders-user"
                name="user"
                data-cy="user"
                label={translate('dailiesApp.subscribedOrders.user')}
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
                id="subscribed-orders-transaction"
                name="transaction"
                data-cy="transaction"
                label={translate('dailiesApp.subscribedOrders.transaction')}
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
                id="subscribed-orders-product"
                name="product"
                data-cy="product"
                label={translate('dailiesApp.subscribedOrders.product')}
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
                id="subscribed-orders-address"
                name="address"
                data-cy="address"
                label={translate('dailiesApp.subscribedOrders.address')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/subscribed-orders" replace color="info">
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

export default SubscribedOrdersUpdate;
