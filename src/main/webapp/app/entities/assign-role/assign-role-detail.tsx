import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './assign-role.reducer';

export const AssignRoleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const assignRoleEntity = useAppSelector(state => state.assignRole.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="assignRoleDetailsHeading">
          <Translate contentKey="dailiesApp.assignRole.detail.title">AssignRole</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{assignRoleEntity.id}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.assignRole.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {assignRoleEntity.createdAt ? <TextFormat value={assignRoleEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.assignRole.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {assignRoleEntity.updatedAt ? <TextFormat value={assignRoleEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="dailiesApp.assignRole.user">User</Translate>
          </dt>
          <dd>{assignRoleEntity.user ? assignRoleEntity.user.email : ''}</dd>
          <dt>
            <Translate contentKey="dailiesApp.assignRole.role">Role</Translate>
          </dt>
          <dd>{assignRoleEntity.role ? assignRoleEntity.role.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/assign-role" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/assign-role/${assignRoleEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AssignRoleDetail;
