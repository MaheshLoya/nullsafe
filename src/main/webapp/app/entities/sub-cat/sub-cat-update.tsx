import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICat } from 'app/shared/model/cat.model';
import { getEntities as getCats } from 'app/entities/cat/cat.reducer';
import { ISubCat } from 'app/shared/model/sub-cat.model';
import { getEntity, updateEntity, createEntity, reset } from './sub-cat.reducer';

export const SubCatUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cats = useAppSelector(state => state.cat.entities);
  const subCatEntity = useAppSelector(state => state.subCat.entity);
  const loading = useAppSelector(state => state.subCat.loading);
  const updating = useAppSelector(state => state.subCat.updating);
  const updateSuccess = useAppSelector(state => state.subCat.updateSuccess);

  const handleClose = () => {
    navigate('/sub-cat' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCats({}));
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

    const entity = {
      ...subCatEntity,
      ...values,
      cat: cats.find(it => it.id.toString() === values.cat.toString()),
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
          ...subCatEntity,
          createdAt: convertDateTimeFromServer(subCatEntity.createdAt),
          updatedAt: convertDateTimeFromServer(subCatEntity.updatedAt),
          cat: subCatEntity?.cat?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dailiesApp.subCat.home.createOrEditLabel" data-cy="SubCatCreateUpdateHeading">
            <Translate contentKey="dailiesApp.subCat.home.createOrEditLabel">Create or edit a SubCat</Translate>
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
                  id="sub-cat-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dailiesApp.subCat.title')}
                id="sub-cat-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  maxLength: { value: 250, message: translate('entity.validation.maxlength', { max: 250 }) },
                }}
              />
              <ValidatedField
                label={translate('dailiesApp.subCat.createdAt')}
                id="sub-cat-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('dailiesApp.subCat.updatedAt')}
                id="sub-cat-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('dailiesApp.subCat.isActive')}
                id="sub-cat-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <UncontrolledTooltip target="isActiveLabel">
                <Translate contentKey="dailiesApp.subCat.help.isActive" />
              </UncontrolledTooltip>
              <ValidatedField id="sub-cat-cat" name="cat" data-cy="cat" label={translate('dailiesApp.subCat.cat')} type="select" required>
                <option value="" key="0" />
                {cats
                  ? cats.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/sub-cat" replace color="info">
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

export default SubCatUpdate;
