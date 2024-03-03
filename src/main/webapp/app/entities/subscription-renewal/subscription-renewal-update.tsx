import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISubscriptionRenewal } from 'app/shared/model/subscription-renewal.model';
import { getEntity, updateEntity, createEntity, reset } from './subscription-renewal.reducer';

export const SubscriptionRenewalUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const subscriptionRenewalEntity = useAppSelector(state => state.subscriptionRenewal.entity);
  const loading = useAppSelector(state => state.subscriptionRenewal.loading);
  const updating = useAppSelector(state => state.subscriptionRenewal.updating);
  const updateSuccess = useAppSelector(state => state.subscriptionRenewal.updateSuccess);

  const handleClose = () => {
    navigate('/subscription-renewal' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.userId !== undefined && typeof values.userId !== 'number') {
      values.userId = Number(values.userId);
    }
    if (values.orderId !== undefined && typeof values.orderId !== 'number') {
      values.orderId = Number(values.orderId);
    }
    if (values.transactionId !== undefined && typeof values.transactionId !== 'number') {
      values.transactionId = Number(values.transactionId);
    }
    if (values.paidRenewalAmount !== undefined && typeof values.paidRenewalAmount !== 'number') {
      values.paidRenewalAmount = Number(values.paidRenewalAmount);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...subscriptionRenewalEntity,
      ...values,
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
          ...subscriptionRenewalEntity,
          createdAt: convertDateTimeFromServer(subscriptionRenewalEntity.createdAt),
          updatedAt: convertDateTimeFromServer(subscriptionRenewalEntity.updatedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailiesApp.subscriptionRenewal.home.createOrEditLabel" data-cy="SubscriptionRenewalCreateUpdateHeading">
            <Translate contentKey="dailiesApp.subscriptionRenewal.home.createOrEditLabel">Create or edit a SubscriptionRenewal</Translate>
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
                  id="subscription-renewal-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dailiesApp.subscriptionRenewal.userId')}
                id="subscription-renewal-userId"
                name="userId"
                data-cy="userId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.subscriptionRenewal.orderId')}
                id="subscription-renewal-orderId"
                name="orderId"
                data-cy="orderId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.subscriptionRenewal.transactionId')}
                id="subscription-renewal-transactionId"
                name="transactionId"
                data-cy="transactionId"
                type="text"
              />
              <ValidatedField
                label={translate('dailiesApp.subscriptionRenewal.renewalDate')}
                id="subscription-renewal-renewalDate"
                name="renewalDate"
                data-cy="renewalDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.subscriptionRenewal.paidRenewalAmount')}
                id="subscription-renewal-paidRenewalAmount"
                name="paidRenewalAmount"
                data-cy="paidRenewalAmount"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.subscriptionRenewal.status')}
                id="subscription-renewal-status"
                name="status"
                data-cy="status"
                check
                type="checkbox"
              />
              <UncontrolledTooltip target="statusLabel">
                <Translate contentKey="dailiesApp.subscriptionRenewal.help.status" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('dailiesApp.subscriptionRenewal.startDate')}
                id="subscription-renewal-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.subscriptionRenewal.endDate')}
                id="subscription-renewal-endDate"
                name="endDate"
                data-cy="endDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.subscriptionRenewal.createdAt')}
                id="subscription-renewal-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.subscriptionRenewal.updatedAt')}
                id="subscription-renewal-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/subscription-renewal" replace color="info">
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

export default SubscriptionRenewalUpdate;
