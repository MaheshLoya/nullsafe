import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './subscription-renewal.reducer';

export const SubscriptionRenewal = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const subscriptionRenewalList = useAppSelector(state => state.subscriptionRenewal.entities);
  const loading = useAppSelector(state => state.subscriptionRenewal.loading);
  const totalItems = useAppSelector(state => state.subscriptionRenewal.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="subscription-renewal-heading" data-cy="SubscriptionRenewalHeading">
        <Translate contentKey="dailiesApp.subscriptionRenewal.home.title">Subscription Renewals</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dailiesApp.subscriptionRenewal.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/subscription-renewal/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dailiesApp.subscriptionRenewal.home.createLabel">Create new Subscription Renewal</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {subscriptionRenewalList && subscriptionRenewalList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dailiesApp.subscriptionRenewal.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('userId')}>
                  <Translate contentKey="dailiesApp.subscriptionRenewal.userId">User Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userId')} />
                </th>
                <th className="hand" onClick={sort('orderId')}>
                  <Translate contentKey="dailiesApp.subscriptionRenewal.orderId">Order Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('orderId')} />
                </th>
                <th className="hand" onClick={sort('transactionId')}>
                  <Translate contentKey="dailiesApp.subscriptionRenewal.transactionId">Transaction Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('transactionId')} />
                </th>
                <th className="hand" onClick={sort('renewalDate')}>
                  <Translate contentKey="dailiesApp.subscriptionRenewal.renewalDate">Renewal Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('renewalDate')} />
                </th>
                <th className="hand" onClick={sort('paidRenewalAmount')}>
                  <Translate contentKey="dailiesApp.subscriptionRenewal.paidRenewalAmount">Paid Renewal Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('paidRenewalAmount')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="dailiesApp.subscriptionRenewal.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  <Translate contentKey="dailiesApp.subscriptionRenewal.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('endDate')}>
                  <Translate contentKey="dailiesApp.subscriptionRenewal.endDate">End Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endDate')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="dailiesApp.subscriptionRenewal.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="dailiesApp.subscriptionRenewal.updatedAt">Updated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {subscriptionRenewalList.map((subscriptionRenewal, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/subscription-renewal/${subscriptionRenewal.id}`} color="link" size="sm">
                      {subscriptionRenewal.id}
                    </Button>
                  </td>
                  <td>{subscriptionRenewal.userId}</td>
                  <td>{subscriptionRenewal.orderId}</td>
                  <td>{subscriptionRenewal.transactionId}</td>
                  <td>
                    {subscriptionRenewal.renewalDate ? (
                      <TextFormat type="date" value={subscriptionRenewal.renewalDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{subscriptionRenewal.paidRenewalAmount}</td>
                  <td>{subscriptionRenewal.status ? 'true' : 'false'}</td>
                  <td>
                    {subscriptionRenewal.startDate ? (
                      <TextFormat type="date" value={subscriptionRenewal.startDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {subscriptionRenewal.endDate ? (
                      <TextFormat type="date" value={subscriptionRenewal.endDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {subscriptionRenewal.createdAt ? (
                      <TextFormat type="date" value={subscriptionRenewal.createdAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {subscriptionRenewal.updatedAt ? (
                      <TextFormat type="date" value={subscriptionRenewal.updatedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/subscription-renewal/${subscriptionRenewal.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/subscription-renewal/${subscriptionRenewal.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/subscription-renewal/${subscriptionRenewal.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="dailiesApp.subscriptionRenewal.home.notFound">No Subscription Renewals found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={subscriptionRenewalList && subscriptionRenewalList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default SubscriptionRenewal;
