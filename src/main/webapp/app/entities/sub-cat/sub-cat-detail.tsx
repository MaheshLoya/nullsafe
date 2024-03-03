import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sub-cat.reducer';

export const SubCatDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const subCatEntity = useAppSelector(state => state.subCat.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subCatDetailsHeading">
          <Translate contentKey="dailiesApp.subCat.detail.title">SubCat</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{subCatEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="dailiesApp.subCat.title">Title</Translate>
            </span>
          </dt>
          <dd>{subCatEntity.title}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.subCat.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{subCatEntity.createdAt ? <TextFormat value={subCatEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.subCat.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{subCatEntity.updatedAt ? <TextFormat value={subCatEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dailiesApp.subCat.isActive">Is Active</Translate>
            </span>
            <UncontrolledTooltip target="isActive">
              <Translate contentKey="dailiesApp.subCat.help.isActive" />
            </UncontrolledTooltip>
          </dt>
          <dd>{subCatEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="dailiesApp.subCat.cat">Cat</Translate>
          </dt>
          <dd>{subCatEntity.cat ? subCatEntity.cat.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/sub-cat" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sub-cat/${subCatEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubCatDetail;
