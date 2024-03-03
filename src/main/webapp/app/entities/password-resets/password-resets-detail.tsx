import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './password-resets.reducer';

export const PasswordResetsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const passwordResetsEntity = useAppSelector(state => state.passwordResets.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="passwordResetsDetailsHeading">
          <Translate contentKey="dailiesApp.passwordResets.detail.title">PasswordResets</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{passwordResetsEntity.id}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="dailiesApp.passwordResets.email">Email</Translate>
            </span>
          </dt>
          <dd>{passwordResetsEntity.email}</dd>
          <dt>
            <span id="token">
              <Translate contentKey="dailiesApp.passwordResets.token">Token</Translate>
            </span>
          </dt>
          <dd>{passwordResetsEntity.token}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.passwordResets.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {passwordResetsEntity.createdAt ? (
              <TextFormat value={passwordResetsEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/password-resets" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/password-resets/${passwordResetsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PasswordResetsDetail;
