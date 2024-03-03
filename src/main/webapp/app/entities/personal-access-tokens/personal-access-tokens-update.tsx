import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPersonalAccessTokens } from 'app/shared/model/personal-access-tokens.model';
import { getEntity, updateEntity, createEntity, reset } from './personal-access-tokens.reducer';

export const PersonalAccessTokensUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const personalAccessTokensEntity = useAppSelector(state => state.personalAccessTokens.entity);
  const loading = useAppSelector(state => state.personalAccessTokens.loading);
  const updating = useAppSelector(state => state.personalAccessTokens.updating);
  const updateSuccess = useAppSelector(state => state.personalAccessTokens.updateSuccess);

  const handleClose = () => {
    navigate('/personal-access-tokens' + location.search);
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
    if (values.tokenableId !== undefined && typeof values.tokenableId !== 'number') {
      values.tokenableId = Number(values.tokenableId);
    }
    values.lastUsedAt = convertDateTimeToServer(values.lastUsedAt);
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...personalAccessTokensEntity,
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
          lastUsedAt: displayDefaultDateTime(),
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...personalAccessTokensEntity,
          lastUsedAt: convertDateTimeFromServer(personalAccessTokensEntity.lastUsedAt),
          createdAt: convertDateTimeFromServer(personalAccessTokensEntity.createdAt),
          updatedAt: convertDateTimeFromServer(personalAccessTokensEntity.updatedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailiesApp.personalAccessTokens.home.createOrEditLabel" data-cy="PersonalAccessTokensCreateUpdateHeading">
            <Translate contentKey="dailiesApp.personalAccessTokens.home.createOrEditLabel">Create or edit a PersonalAccessTokens</Translate>
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
                  id="personal-access-tokens-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dailiesApp.personalAccessTokens.tokenableType')}
                id="personal-access-tokens-tokenableType"
                name="tokenableType"
                data-cy="tokenableType"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.personalAccessTokens.tokenableId')}
                id="personal-access-tokens-tokenableId"
                name="tokenableId"
                data-cy="tokenableId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.personalAccessTokens.name')}
                id="personal-access-tokens-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.personalAccessTokens.token')}
                id="personal-access-tokens-token"
                name="token"
                data-cy="token"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 64, message: translate('entity.validation.maxlength', { max: 64 }) },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.personalAccessTokens.abilities')}
                id="personal-access-tokens-abilities"
                name="abilities"
                data-cy="abilities"
                type="text"
                validate={{
                  maxLength: { value: 65535, message: translate('entity.validation.maxlength', { max: 65535 }) },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.personalAccessTokens.lastUsedAt')}
                id="personal-access-tokens-lastUsedAt"
                name="lastUsedAt"
                data-cy="lastUsedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('dailiesApp.personalAccessTokens.createdAt')}
                id="personal-access-tokens-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('dailiesApp.personalAccessTokens.updatedAt')}
                id="personal-access-tokens-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/personal-access-tokens" replace color="info">
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

export default PersonalAccessTokensUpdate;
