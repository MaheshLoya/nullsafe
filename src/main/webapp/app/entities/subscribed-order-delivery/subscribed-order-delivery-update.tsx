import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IOrders } from 'app/shared/model/orders.model';
import { getEntities as getOrders } from 'app/entities/orders/orders.reducer';
import { IUsers } from 'app/shared/model/users.model';
import { getEntities as getUsers } from 'app/entities/users/users.reducer';
import { ISubscribedOrderDelivery } from 'app/shared/model/subscribed-order-delivery.model';
import { getEntity, updateEntity, createEntity, reset } from './subscribed-order-delivery.reducer';

export const SubscribedOrderDeliveryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const orders = useAppSelector(state => state.orders.entities);
  const users = useAppSelector(state => state.users.entities);
  const subscribedOrderDeliveryEntity = useAppSelector(state => state.subscribedOrderDelivery.entity);
  const loading = useAppSelector(state => state.subscribedOrderDelivery.loading);
  const updating = useAppSelector(state => state.subscribedOrderDelivery.updating);
  const updateSuccess = useAppSelector(state => state.subscribedOrderDelivery.updateSuccess);

  const handleClose = () => {
    navigate('/subscribed-order-delivery' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getOrders({}));
    dispatch(getUsers({}));
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
    if (values.paymentMode !== undefined && typeof values.paymentMode !== 'number') {
      values.paymentMode = Number(values.paymentMode);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...subscribedOrderDeliveryEntity,
      ...values,
      order: orders.find(it => it.id.toString() === values.order.toString()),
      entryUser: users.find(it => it.id.toString() === values.entryUser.toString()),
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
          ...subscribedOrderDeliveryEntity,
          createdAt: convertDateTimeFromServer(subscribedOrderDeliveryEntity.createdAt),
          updatedAt: convertDateTimeFromServer(subscribedOrderDeliveryEntity.updatedAt),
          order: subscribedOrderDeliveryEntity?.order?.id,
          entryUser: subscribedOrderDeliveryEntity?.entryUser?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailiesApp.subscribedOrderDelivery.home.createOrEditLabel" data-cy="SubscribedOrderDeliveryCreateUpdateHeading">
            <Translate contentKey="dailiesApp.subscribedOrderDelivery.home.createOrEditLabel">
              Create or edit a SubscribedOrderDelivery
            </Translate>
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
                  id="subscribed-order-delivery-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dailiesApp.subscribedOrderDelivery.date')}
                id="subscribed-order-delivery-date"
                name="date"
                data-cy="date"
                type="date"
              />
              <ValidatedField
                label={translate('dailiesApp.subscribedOrderDelivery.paymentMode')}
                id="subscribed-order-delivery-paymentMode"
                name="paymentMode"
                data-cy="paymentMode"
                type="text"
              />
              <UncontrolledTooltip target="paymentModeLabel">
                <Translate contentKey="dailiesApp.subscribedOrderDelivery.help.paymentMode" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('dailiesApp.subscribedOrderDelivery.createdAt')}
                id="subscribed-order-delivery-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('dailiesApp.subscribedOrderDelivery.updatedAt')}
                id="subscribed-order-delivery-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="subscribed-order-delivery-order"
                name="order"
                data-cy="order"
                label={translate('dailiesApp.subscribedOrderDelivery.order')}
                type="select"
                required
              >
                <option value="" key="0" />
                {orders
                  ? orders.map(otherEntity => (
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
                id="subscribed-order-delivery-entryUser"
                name="entryUser"
                data-cy="entryUser"
                label={translate('dailiesApp.subscribedOrderDelivery.entryUser')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/subscribed-order-delivery" replace color="info">
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

export default SubscribedOrderDeliveryUpdate;
