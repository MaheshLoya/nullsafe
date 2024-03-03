import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-notification.reducer';

export const UserNotificationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userNotificationEntity = useAppSelector(state => state.userNotification.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userNotificationDetailsHeading">
          <Translate contentKey="dailiesApp.userNotification.detail.title">UserNotification</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userNotificationEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="dailiesApp.userNotification.title">Title</Translate>
            </span>
          </dt>
          <dd>{userNotificationEntity.title}</dd>
          <dt>
            <span id="body">
              <Translate contentKey="dailiesApp.userNotification.body">Body</Translate>
            </span>
          </dt>
          <dd>{userNotificationEntity.body}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.userNotification.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {userNotificationEntity.createdAt ? (
              <TextFormat value={userNotificationEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.userNotification.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {userNotificationEntity.updatedAt ? (
              <TextFormat value={userNotificationEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/user-notification" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-notification/${userNotificationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserNotificationDetail;
