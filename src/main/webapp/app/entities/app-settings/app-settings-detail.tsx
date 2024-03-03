import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './app-settings.reducer';

export const AppSettingsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const appSettingsEntity = useAppSelector(state => state.appSettings.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="appSettingsDetailsHeading">
          <Translate contentKey="dailiesApp.appSettings.detail.title">AppSettings</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{appSettingsEntity.id}</dd>
          <dt>
            <span id="settingId">
              <Translate contentKey="dailiesApp.appSettings.settingId">Setting Id</Translate>
            </span>
          </dt>
          <dd>{appSettingsEntity.settingId}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="dailiesApp.appSettings.title">Title</Translate>
            </span>
          </dt>
          <dd>{appSettingsEntity.title}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="dailiesApp.appSettings.value">Value</Translate>
            </span>
          </dt>
          <dd>{appSettingsEntity.value}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.appSettings.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {appSettingsEntity.createdAt ? <TextFormat value={appSettingsEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.appSettings.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {appSettingsEntity.updatedAt ? <TextFormat value={appSettingsEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/app-settings" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/app-settings/${appSettingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AppSettingsDetail;
