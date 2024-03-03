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
import { ITransactions } from 'app/shared/model/transactions.model';
import { getEntity, updateEntity, createEntity, reset } from './transactions.reducer';

export const TransactionsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const orders = useAppSelector(state => state.orders.entities);
  const users = useAppSelector(state => state.users.entities);
  const transactionsEntity = useAppSelector(state => state.transactions.entity);
  const loading = useAppSelector(state => state.transactions.loading);
  const updating = useAppSelector(state => state.transactions.updating);
  const updateSuccess = useAppSelector(state => state.transactions.updateSuccess);

  const handleClose = () => {
    navigate('/transactions' + location.search);
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
    if (values.amount !== undefined && typeof values.amount !== 'number') {
      values.amount = Number(values.amount);
    }
    if (values.type !== undefined && typeof values.type !== 'number') {
      values.type = Number(values.type);
    }
    if (values.paymentMode !== undefined && typeof values.paymentMode !== 'number') {
      values.paymentMode = Number(values.paymentMode);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...transactionsEntity,
      ...values,
      order: orders.find(it => it.id.toString() === values.order.toString()),
      user: users.find(it => it.id.toString() === values.user.toString()),
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
          ...transactionsEntity,
          createdAt: convertDateTimeFromServer(transactionsEntity.createdAt),
          updatedAt: convertDateTimeFromServer(transactionsEntity.updatedAt),
          order: transactionsEntity?.order?.id,
          user: transactionsEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailiesApp.transactions.home.createOrEditLabel" data-cy="TransactionsCreateUpdateHeading">
            <Translate contentKey="dailiesApp.transactions.home.createOrEditLabel">Create or edit a Transactions</Translate>
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
                  id="transactions-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dailiesApp.transactions.paymentId')}
                id="transactions-paymentId"
                name="paymentId"
                data-cy="paymentId"
                type="text"
                validate={{
                  maxLength: { value: 65535, message: translate('entity.validation.maxlength', { max: 65535 }) },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.transactions.amount')}
                id="transactions-amount"
                name="amount"
                data-cy="amount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.transactions.description')}
                id="transactions-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  maxLength: { value: 65535, message: translate('entity.validation.maxlength', { max: 65535 }) },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.transactions.type')}
                id="transactions-type"
                name="type"
                data-cy="type"
                type="text"
              />
              <UncontrolledTooltip target="typeLabel">
                <Translate contentKey="dailiesApp.transactions.help.type" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('dailiesApp.transactions.paymentMode')}
                id="transactions-paymentMode"
                name="paymentMode"
                data-cy="paymentMode"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <UncontrolledTooltip target="paymentModeLabel">
                <Translate contentKey="dailiesApp.transactions.help.paymentMode" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('dailiesApp.transactions.createdAt')}
                id="transactions-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('dailiesApp.transactions.updatedAt')}
                id="transactions-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="transactions-order"
                name="order"
                data-cy="order"
                label={translate('dailiesApp.transactions.order')}
                type="select"
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
              <ValidatedField
                id="transactions-user"
                name="user"
                data-cy="user"
                label={translate('dailiesApp.transactions.user')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/transactions" replace color="info">
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

export default TransactionsUpdate;
