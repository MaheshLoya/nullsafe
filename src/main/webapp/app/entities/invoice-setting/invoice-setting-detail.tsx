import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './invoice-setting.reducer';

export const InvoiceSettingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const invoiceSettingEntity = useAppSelector(state => state.invoiceSetting.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="invoiceSettingDetailsHeading">
          <Translate contentKey="dailiesApp.invoiceSetting.detail.title">InvoiceSetting</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{invoiceSettingEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="dailiesApp.invoiceSetting.title">Title</Translate>
            </span>
          </dt>
          <dd>{invoiceSettingEntity.title}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="dailiesApp.invoiceSetting.value">Value</Translate>
            </span>
          </dt>
          <dd>{invoiceSettingEntity.value}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="dailiesApp.invoiceSetting.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {invoiceSettingEntity.createdAt ? (
              <TextFormat value={invoiceSettingEntity.createdAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="dailiesApp.invoiceSetting.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {invoiceSettingEntity.updatedAt ? (
              <TextFormat value={invoiceSettingEntity.updatedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/invoice-setting" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/invoice-setting/${invoiceSettingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default InvoiceSettingDetail;
