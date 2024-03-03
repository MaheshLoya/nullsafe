import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './allow-pincode.reducer';

export const AllowPincodeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const allowPincodeEntity = useAppSelector(state => state.allowPincode.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="allowPincodeDetailsHeading">
          <Translate contentKey="dailiesApp.allowPincode.detail.title">AllowPincode</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{allowPincodeEntity.id}</dd>
          <dt>
            <span id="pinCode">
              <Translate contentKey="dailiesApp.allowPincode.pinCode">Pin Code</Translate>
            </span>
          </dt>
          <dd>{allowPincodeEntity.pinCode}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.allowPincode.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {allowPincodeEntity.createdAt ? <TextFormat value={allowPincodeEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.allowPincode.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {allowPincodeEntity.updatedAt ? <TextFormat value={allowPincodeEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/allow-pincode" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/allow-pincode/${allowPincodeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AllowPincodeDetail;
