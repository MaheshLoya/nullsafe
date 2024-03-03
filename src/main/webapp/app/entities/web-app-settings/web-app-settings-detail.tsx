import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './web-app-settings.reducer';

export const WebAppSettingsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const webAppSettingsEntity = useAppSelector(state => state.webAppSettings.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="webAppSettingsDetailsHeading">
          <Translate contentKey="dailiesApp.webAppSettings.detail.title">WebAppSettings</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="dailiesApp.webAppSettings.id">Id</Translate>
            </span>
          </dt>
          <dd>{webAppSettingsEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="dailiesApp.webAppSettings.title">Title</Translate>
            </span>
          </dt>
          <dd>{webAppSettingsEntity.title}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="dailiesApp.webAppSettings.value">Value</Translate>
            </span>
          </dt>
          <dd>{webAppSettingsEntity.value}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.webAppSettings.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {webAppSettingsEntity.createdAt ? (
              <TextFormat value={webAppSettingsEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.webAppSettings.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {webAppSettingsEntity.updatedAt ? (
              <TextFormat value={webAppSettingsEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/web-app-settings" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/web-app-settings/${webAppSettingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WebAppSettingsDetail;
