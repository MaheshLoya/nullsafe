import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './personal-access-tokens.reducer';

export const PersonalAccessTokensDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const personalAccessTokensEntity = useAppSelector(state => state.personalAccessTokens.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="personalAccessTokensDetailsHeading">
          <Translate contentKey="dailiesApp.personalAccessTokens.detail.title">PersonalAccessTokens</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{personalAccessTokensEntity.id}</dd>
          <dt>
            <span id="tokenableType">
              <Translate contentKey="dailiesApp.personalAccessTokens.tokenableType">Tokenable Type</Translate>
            </span>
          </dt>
          <dd>{personalAccessTokensEntity.tokenableType}</dd>
          <dt>
            <span id="tokenableId">
              <Translate contentKey="dailiesApp.personalAccessTokens.tokenableId">Tokenable Id</Translate>
            </span>
          </dt>
          <dd>{personalAccessTokensEntity.tokenableId}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="dailiesApp.personalAccessTokens.name">Name</Translate>
            </span>
          </dt>
          <dd>{personalAccessTokensEntity.name}</dd>
          <dt>
            <span id="token">
              <Translate contentKey="dailiesApp.personalAccessTokens.token">Token</Translate>
            </span>
          </dt>
          <dd>{personalAccessTokensEntity.token}</dd>
          <dt>
            <span id="abilities">
              <Translate contentKey="dailiesApp.personalAccessTokens.abilities">Abilities</Translate>
            </span>
          </dt>
          <dd>{personalAccessTokensEntity.abilities}</dd>
          <dt>
            <span id="lastUsedAt">
              <Translate contentKey="dailiesApp.personalAccessTokens.lastUsedAt">Last Used At</Translate>
            </span>
          </dt>
          <dd>
            {personalAccessTokensEntity.lastUsedAt ? (
              <TextFormat value={personalAccessTokensEntity.lastUsedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.personalAccessTokens.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {personalAccessTokensEntity.createdAt ? (
              <TextFormat value={personalAccessTokensEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.personalAccessTokens.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {personalAccessTokensEntity.updatedAt ? (
              <TextFormat value={personalAccessTokensEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/personal-access-tokens" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/personal-access-tokens/${personalAccessTokensEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PersonalAccessTokensDetail;
