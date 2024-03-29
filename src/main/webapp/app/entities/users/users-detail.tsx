import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './users.reducer';

export const UsersDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const usersEntity = useAppSelector(state => state.users.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="usersDetailsHeading">
          <Translate contentKey="dailiesApp.users.detail.title">Users</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{usersEntity.id}</dd>
          <dt>
            <span id="walletAmount">
              <Translate contentKey="dailiesApp.users.walletAmount">Wallet Amount</Translate>
            </span>
          </dt>
          <dd>{usersEntity.walletAmount}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="dailiesApp.users.email">Email</Translate>
            </span>
          </dt>
          <dd>{usersEntity.email}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="dailiesApp.users.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{usersEntity.phone}</dd>
          <dt>
            <span id="emailVerifiedAt">
              <Translate contentKey="dailiesApp.users.emailVerifiedAt">Email Verified At</Translate>
            </span>
          </dt>
          <dd>
            {usersEntity.emailVerifiedAt ? <TextFormat value={usersEntity.emailVerifiedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="password">
              <Translate contentKey="dailiesApp.users.password">Password</Translate>
            </span>
          </dt>
          <dd>{usersEntity.password}</dd>
          <dt>
            <span id="rememberToken">
              <Translate contentKey="dailiesApp.users.rememberToken">Remember Token</Translate>
            </span>
          </dt>
          <dd>{usersEntity.rememberToken}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.users.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{usersEntity.createdAt ? <TextFormat value={usersEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.users.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{usersEntity.updatedAt ? <TextFormat value={usersEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="dailiesApp.users.name">Name</Translate>
            </span>
          </dt>
          <dd>{usersEntity.name}</dd>
          <dt>
            <span id="fcm">
              <Translate contentKey="dailiesApp.users.fcm">Fcm</Translate>
            </span>
          </dt>
          <dd>{usersEntity.fcm}</dd>
          <dt>
            <span id="subscriptionAmount">
              <Translate contentKey="dailiesApp.users.subscriptionAmount">Subscription Amount</Translate>
            </span>
          </dt>
          <dd>{usersEntity.subscriptionAmount}</dd>
        </dl>
        <Button tag={Link} to="/users" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/users/${usersEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UsersDetail;
