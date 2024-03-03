import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './social-media.reducer';

export const SocialMediaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const socialMediaEntity = useAppSelector(state => state.socialMedia.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="socialMediaDetailsHeading">
          <Translate contentKey="dailiesApp.socialMedia.detail.title">SocialMedia</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{socialMediaEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="dailiesApp.socialMedia.title">Title</Translate>
            </span>
          </dt>
          <dd>{socialMediaEntity.title}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="dailiesApp.socialMedia.image">Image</Translate>
            </span>
          </dt>
          <dd>{socialMediaEntity.image}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="dailiesApp.socialMedia.url">Url</Translate>
            </span>
          </dt>
          <dd>{socialMediaEntity.url}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.socialMedia.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {socialMediaEntity.createdAt ? <TextFormat value={socialMediaEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.socialMedia.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {socialMediaEntity.updatedAt ? <TextFormat value={socialMediaEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/social-media" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/social-media/${socialMediaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SocialMediaDetail;
