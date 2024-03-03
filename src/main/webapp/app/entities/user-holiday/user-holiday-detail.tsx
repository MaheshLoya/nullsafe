import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-holiday.reducer';

export const UserHolidayDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userHolidayEntity = useAppSelector(state => state.userHoliday.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userHolidayDetailsHeading">
          <Translate contentKey="dailiesApp.userHoliday.detail.title">UserHoliday</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userHolidayEntity.id}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="dailiesApp.userHoliday.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{userHolidayEntity.userId}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="dailiesApp.userHoliday.date">Date</Translate>
            </span>
          </dt>
          <dd>
            {userHolidayEntity.date ? <TextFormat value={userHolidayEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.userHoliday.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {userHolidayEntity.createdAt ? <TextFormat value={userHolidayEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.userHoliday.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {userHolidayEntity.updatedAt ? <TextFormat value={userHolidayEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/user-holiday" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-holiday/${userHolidayEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserHolidayDetail;
