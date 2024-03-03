import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './testimonials.reducer';

export const TestimonialsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const testimonialsEntity = useAppSelector(state => state.testimonials.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="testimonialsDetailsHeading">
          <Translate contentKey="dailiesApp.testimonials.detail.title">Testimonials</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{testimonialsEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="dailiesApp.testimonials.title">Title</Translate>
            </span>
          </dt>
          <dd>{testimonialsEntity.title}</dd>
          <dt>
            <span id="subTitle">
              <Translate contentKey="dailiesApp.testimonials.subTitle">Sub Title</Translate>
            </span>
          </dt>
          <dd>{testimonialsEntity.subTitle}</dd>
          <dt>
            <span id="rating">
              <Translate contentKey="dailiesApp.testimonials.rating">Rating</Translate>
            </span>
          </dt>
          <dd>{testimonialsEntity.rating}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="dailiesApp.testimonials.description">Description</Translate>
            </span>
          </dt>
          <dd>{testimonialsEntity.description}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.testimonials.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {testimonialsEntity.createdAt ? <TextFormat value={testimonialsEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.testimonials.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {testimonialsEntity.updatedAt ? <TextFormat value={testimonialsEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/testimonials" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/testimonials/${testimonialsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TestimonialsDetail;
