import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './banner-image.reducer';

export const BannerImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bannerImageEntity = useAppSelector(state => state.bannerImage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bannerImageDetailsHeading">
          <Translate contentKey="dailiesApp.bannerImage.detail.title">BannerImage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bannerImageEntity.id}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="dailiesApp.bannerImage.image">Image</Translate>
            </span>
          </dt>
          <dd>{bannerImageEntity.image}</dd>
          <dt>
            <span id="imageType">
              <Translate contentKey="dailiesApp.bannerImage.imageType">Image Type</Translate>
            </span>
            <UncontrolledTooltip target="imageType">
              <Translate contentKey="dailiesApp.bannerImage.help.imageType" />
            </UncontrolledTooltip>
          </dt>
          <dd>{bannerImageEntity.imageType ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.bannerImage.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {bannerImageEntity.createdAt ? <TextFormat value={bannerImageEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.bannerImage.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {bannerImageEntity.updatedAt ? <TextFormat value={bannerImageEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/banner-image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/banner-image/${bannerImageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BannerImageDetail;
