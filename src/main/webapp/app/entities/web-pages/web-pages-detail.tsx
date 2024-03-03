import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './web-pages.reducer';

export const WebPagesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const webPagesEntity = useAppSelector(state => state.webPages.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="webPagesDetailsHeading">
          <Translate contentKey="dailiesApp.webPages.detail.title">WebPages</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{webPagesEntity.id}</dd>
          <dt>
            <span id="pageId">
              <Translate contentKey="dailiesApp.webPages.pageId">Page Id</Translate>
            </span>
            <UncontrolledTooltip target="pageId">
              <Translate contentKey="dailiesApp.webPages.help.pageId" />
            </UncontrolledTooltip>
          </dt>
          <dd>{webPagesEntity.pageId}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="dailiesApp.webPages.title">Title</Translate>
            </span>
          </dt>
          <dd>{webPagesEntity.title}</dd>
          <dt>
            <span id="body">
              <Translate contentKey="dailiesApp.webPages.body">Body</Translate>
            </span>
          </dt>
          <dd>{webPagesEntity.body}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.webPages.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{webPagesEntity.createdAt ? <TextFormat value={webPagesEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.webPages.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{webPagesEntity.updatedAt ? <TextFormat value={webPagesEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
        </dl>
        <Button tag={Link} to="/web-pages" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/web-pages/${webPagesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WebPagesDetail;
