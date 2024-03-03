import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './images.reducer';

export const ImagesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const imagesEntity = useAppSelector(state => state.images.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="imagesDetailsHeading">
          <Translate contentKey="dailiesApp.images.detail.title">Images</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.id}</dd>
          <dt>
            <span id="tableName">
              <Translate contentKey="dailiesApp.images.tableName">Table Name</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.tableName}</dd>
          <dt>
            <span id="tableId">
              <Translate contentKey="dailiesApp.images.tableId">Table Id</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.tableId}</dd>
          <dt>
            <span id="imageType">
              <Translate contentKey="dailiesApp.images.imageType">Image Type</Translate>
            </span>
            <UncontrolledTooltip target="imageType">
              <Translate contentKey="dailiesApp.images.help.imageType" />
            </UncontrolledTooltip>
          </dt>
          <dd>{imagesEntity.imageType ? 'true' : 'false'}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="dailiesApp.images.image">Image</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.image}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.images.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.createdAt ? <TextFormat value={imagesEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.images.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{imagesEntity.updatedAt ? <TextFormat value={imagesEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/images" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/images/${imagesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ImagesDetail;
