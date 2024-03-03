import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './specific-notification.reducer';

export const SpecificNotificationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const specificNotificationEntity = useAppSelector(state => state.specificNotification.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="specificNotificationDetailsHeading">
          <Translate contentKey="dailiesApp.specificNotification.detail.title">SpecificNotification</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{specificNotificationEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="dailiesApp.specificNotification.title">Title</Translate>
            </span>
          </dt>
          <dd>{specificNotificationEntity.title}</dd>
          <dt>
            <span id="body">
              <Translate contentKey="dailiesApp.specificNotification.body">Body</Translate>
            </span>
          </dt>
          <dd>{specificNotificationEntity.body}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.specificNotification.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {specificNotificationEntity.createdAt ? (
              <TextFormat value={specificNotificationEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.specificNotification.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {specificNotificationEntity.updatedAt ? (
              <TextFormat value={specificNotificationEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="dailiesApp.specificNotification.user">User</Translate>
          </dt>
          <dd>{specificNotificationEntity.user ? specificNotificationEntity.user.email : ''}</dd>
        </dl>
        <Button tag={Link} to="/specific-notification" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/specific-notification/${specificNotificationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SpecificNotificationDetail;
