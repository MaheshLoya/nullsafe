import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IFiles } from 'app/shared/model/files.model';
import { getEntity, updateEntity, createEntity, reset } from './files.reducer';

export const FilesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const filesEntity = useAppSelector(state => state.files.entity);
  const loading = useAppSelector(state => state.files.loading);
  const updating = useAppSelector(state => state.files.updating);
  const updateSuccess = useAppSelector(state => state.files.updateSuccess);

  const handleClose = () => {
    navigate('/files' + location.search);
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
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);
    if (values.fileFor !== undefined && typeof values.fileFor !== 'number') {
      values.fileFor = Number(values.fileFor);
    }
    if (values.fileForId !== undefined && typeof values.fileForId !== 'number') {
      values.fileForId = Number(values.fileForId);
    }

    const entity = {
      ...filesEntity,
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
          ...filesEntity,
          createdAt: convertDateTimeFromServer(filesEntity.createdAt),
          updatedAt: convertDateTimeFromServer(filesEntity.updatedAt),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailiesApp.files.home.createOrEditLabel" data-cy="FilesCreateUpdateHeading">
            <Translate contentKey="dailiesApp.files.home.createOrEditLabel">Create or edit a Files</Translate>
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
                  id="files-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dailiesApp.files.name')}
                id="files-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 250, message: translate('entity.validation.maxlength', { max: 250 }) },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.files.fileUrl')}
                id="files-fileUrl"
                name="fileUrl"
                data-cy="fileUrl"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 65535, message: translate('entity.validation.maxlength', { max: 65535 }) },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.files.createdAt')}
                id="files-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('dailiesApp.files.updatedAt')}
                id="files-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('dailiesApp.files.deleted')}
                id="files-deleted"
                name="deleted"
                data-cy="deleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dailiesApp.files.fileFor')}
                id="files-fileFor"
                name="fileFor"
                data-cy="fileFor"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <UncontrolledTooltip target="fileForLabel">
                <Translate contentKey="dailiesApp.files.help.fileFor" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('dailiesApp.files.fileForId')}
                id="files-fileForId"
                name="fileForId"
                data-cy="fileForId"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.files.fileCat')}
                id="files-fileCat"
                name="fileCat"
                data-cy="fileCat"
                check
                type="checkbox"
              />
              <UncontrolledTooltip target="fileCatLabel">
                <Translate contentKey="dailiesApp.files.help.fileCat" />
              </UncontrolledTooltip>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/files" replace color="info">
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

export default FilesUpdate;
