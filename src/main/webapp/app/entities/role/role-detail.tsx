import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './role.reducer';

export const RoleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const roleEntity = useAppSelector(state => state.role.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="roleDetailsHeading">
          <Translate contentKey="dailiesApp.role.detail.title">Role</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{roleEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="dailiesApp.role.title">Title</Translate>
            </span>
          </dt>
          <dd>{roleEntity.title}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.role.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{roleEntity.createdAt ? <TextFormat value={roleEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.role.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{roleEntity.updatedAt ? <TextFormat value={roleEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="deleted">
              <Translate contentKey="dailiesApp.role.deleted">Deleted</Translate>
            </span>
          </dt>
          <dd>{roleEntity.deleted ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/role" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/role/${roleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RoleDetail;
