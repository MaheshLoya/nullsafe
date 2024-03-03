import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './migrations.reducer';

export const MigrationsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const migrationsEntity = useAppSelector(state => state.migrations.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="migrationsDetailsHeading">
          <Translate contentKey="dailiesApp.migrations.detail.title">Migrations</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="dailiesApp.migrations.id">Id</Translate>
            </span>
          </dt>
          <dd>{migrationsEntity.id}</dd>
          <dt>
            <span id="migration">
              <Translate contentKey="dailiesApp.migrations.migration">Migration</Translate>
            </span>
          </dt>
          <dd>{migrationsEntity.migration}</dd>
          <dt>
            <span id="batch">
              <Translate contentKey="dailiesApp.migrations.batch">Batch</Translate>
            </span>
          </dt>
          <dd>{migrationsEntity.batch}</dd>
        </dl>
        <Button tag={Link} to="/migrations" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/migrations/${migrationsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MigrationsDetail;
