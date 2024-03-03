import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './failed-jobs.reducer';

export const FailedJobsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const failedJobsEntity = useAppSelector(state => state.failedJobs.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="failedJobsDetailsHeading">
          <Translate contentKey="dailiesApp.failedJobs.detail.title">FailedJobs</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{failedJobsEntity.id}</dd>
          <dt>
            <span id="uuid">
              <Translate contentKey="dailiesApp.failedJobs.uuid">Uuid</Translate>
            </span>
          </dt>
          <dd>{failedJobsEntity.uuid}</dd>
          <dt>
            <span id="connection">
              <Translate contentKey="dailiesApp.failedJobs.connection">Connection</Translate>
            </span>
          </dt>
          <dd>{failedJobsEntity.connection}</dd>
          <dt>
            <span id="queue">
              <Translate contentKey="dailiesApp.failedJobs.queue">Queue</Translate>
            </span>
          </dt>
          <dd>{failedJobsEntity.queue}</dd>
          <dt>
            <span id="payload">
              <Translate contentKey="dailiesApp.failedJobs.payload">Payload</Translate>
            </span>
          </dt>
          <dd>{failedJobsEntity.payload}</dd>
          <dt>
            <span id="exception">
              <Translate contentKey="dailiesApp.failedJobs.exception">Exception</Translate>
            </span>
          </dt>
          <dd>{failedJobsEntity.exception}</dd>
          <dt>
            <span id="failedAt">
              <Translate contentKey="dailiesApp.failedJobs.failedAt">Failed At</Translate>
            </span>
          </dt>
          <dd>
            {failedJobsEntity.failedAt ? <TextFormat value={failedJobsEntity.failedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/failed-jobs" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/failed-jobs/${failedJobsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FailedJobsDetail;
