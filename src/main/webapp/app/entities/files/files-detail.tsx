import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './files.reducer';

export const FilesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const filesEntity = useAppSelector(state => state.files.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="filesDetailsHeading">
          <Translate contentKey="dailiesApp.files.detail.title">Files</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{filesEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="dailiesApp.files.name">Name</Translate>
            </span>
          </dt>
          <dd>{filesEntity.name}</dd>
          <dt>
            <span id="fileUrl">
              <Translate contentKey="dailiesApp.files.fileUrl">File Url</Translate>
            </span>
          </dt>
          <dd>{filesEntity.fileUrl}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.files.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{filesEntity.createdAt ? <TextFormat value={filesEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.files.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{filesEntity.updatedAt ? <TextFormat value={filesEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="deleted">
              <Translate contentKey="dailiesApp.files.deleted">Deleted</Translate>
            </span>
          </dt>
          <dd>{filesEntity.deleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="fileFor">
              <Translate contentKey="dailiesApp.files.fileFor">File For</Translate>
            </span>
            <UncontrolledTooltip target="fileFor">
              <Translate contentKey="dailiesApp.files.help.fileFor" />
            </UncontrolledTooltip>
          </dt>
          <dd>{filesEntity.fileFor}</dd>
          <dt>
            <span id="fileForId">
              <Translate contentKey="dailiesApp.files.fileForId">File For Id</Translate>
            </span>
          </dt>
          <dd>{filesEntity.fileForId}</dd>
          <dt>
            <span id="fileCat">
              <Translate contentKey="dailiesApp.files.fileCat">File Cat</Translate>
            </span>
            <UncontrolledTooltip target="fileCat">
              <Translate contentKey="dailiesApp.files.help.fileCat" />
            </UncontrolledTooltip>
          </dt>
          <dd>{filesEntity.fileCat ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/files" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/files/${filesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FilesDetail;
