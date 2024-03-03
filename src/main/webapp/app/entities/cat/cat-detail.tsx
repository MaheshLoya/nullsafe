import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cat.reducer';

export const CatDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const catEntity = useAppSelector(state => state.cat.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="catDetailsHeading">
          <Translate contentKey="dailiesApp.cat.detail.title">Cat</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{catEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="dailiesApp.cat.title">Title</Translate>
            </span>
          </dt>
          <dd>{catEntity.title}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.cat.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{catEntity.createdAt ? <TextFormat value={catEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.cat.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{catEntity.updatedAt ? <TextFormat value={catEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dailiesApp.cat.isActive">Is Active</Translate>
            </span>
            <UncontrolledTooltip target="isActive">
              <Translate contentKey="dailiesApp.cat.help.isActive" />
            </UncontrolledTooltip>
          </dt>
          <dd>{catEntity.isActive ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/cat" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cat/${catEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CatDetail;
