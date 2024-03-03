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

import { getEntities } from './subscribed-orders.reducer';

export const SubscribedOrders = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const subscribedOrdersList = useAppSelector(state => state.subscribedOrders.entities);
  const loading = useAppSelector(state => state.subscribedOrders.loading);
  const totalItems = useAppSelector(state => state.subscribedOrders.totalItems);

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
      <h2 id="subscribed-orders-heading" data-cy="SubscribedOrdersHeading">
        <Translate contentKey="dailiesApp.subscribedOrders.home.title">Subscribed Orders</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dailiesApp.subscribedOrders.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/subscribed-orders/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dailiesApp.subscribedOrders.home.createLabel">Create new Subscribed Orders</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {subscribedOrdersList && subscribedOrdersList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('paymentType')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.paymentType">Payment Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('paymentType')} />
                </th>
                <th className="hand" onClick={sort('orderAmount')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.orderAmount">Order Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('orderAmount')} />
                </th>
                <th className="hand" onClick={sort('subscriptionBalanceAmount')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.subscriptionBalanceAmount">Subscription Balance Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subscriptionBalanceAmount')} />
                </th>
                <th className="hand" onClick={sort('price')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.price">Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('price')} />
                </th>
                <th className="hand" onClick={sort('mrp')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.mrp">Mrp</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mrp')} />
                </th>
                <th className="hand" onClick={sort('tax')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.tax">Tax</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tax')} />
                </th>
                <th className="hand" onClick={sort('qty')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.qty">Qty</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('qty')} />
                </th>
                <th className="hand" onClick={sort('offerId')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.offerId">Offer Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('offerId')} />
                </th>
                <th className="hand" onClick={sort('selectedDaysForWeekly')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.selectedDaysForWeekly">Selected Days For Weekly</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('selectedDaysForWeekly')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('endDate')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.endDate">End Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endDate')} />
                </th>
                <th className="hand" onClick={sort('lastRenewalDate')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.lastRenewalDate">Last Renewal Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastRenewalDate')} />
                </th>
                <th className="hand" onClick={sort('subscriptionType')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.subscriptionType">Subscription Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subscriptionType')} />
                </th>
                <th className="hand" onClick={sort('approvalStatus')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.approvalStatus">Approval Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('approvalStatus')} />
                </th>
                <th className="hand" onClick={sort('orderStatus')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.orderStatus">Order Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('orderStatus')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.updatedAt">Updated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('updatedBy')}>
                  <Translate contentKey="dailiesApp.subscribedOrders.updatedBy">Updated By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedBy')} />
                </th>
                <th>
                  <Translate contentKey="dailiesApp.subscribedOrders.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="dailiesApp.subscribedOrders.transaction">Transaction</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="dailiesApp.subscribedOrders.product">Product</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="dailiesApp.subscribedOrders.address">Address</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {subscribedOrdersList.map((subscribedOrders, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/subscribed-orders/${subscribedOrders.id}`} color="link" size="sm">
                      {subscribedOrders.id}
                    </Button>
                  </td>
                  <td>{subscribedOrders.paymentType}</td>
                  <td>{subscribedOrders.orderAmount}</td>
                  <td>{subscribedOrders.subscriptionBalanceAmount}</td>
                  <td>{subscribedOrders.price}</td>
                  <td>{subscribedOrders.mrp}</td>
                  <td>{subscribedOrders.tax}</td>
                  <td>{subscribedOrders.qty}</td>
                  <td>{subscribedOrders.offerId}</td>
                  <td>{subscribedOrders.selectedDaysForWeekly}</td>
                  <td>
                    {subscribedOrders.startDate ? (
                      <TextFormat type="date" value={subscribedOrders.startDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {subscribedOrders.endDate ? (
                      <TextFormat type="date" value={subscribedOrders.endDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {subscribedOrders.lastRenewalDate ? (
                      <TextFormat type="date" value={subscribedOrders.lastRenewalDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{subscribedOrders.subscriptionType}</td>
                  <td>{subscribedOrders.approvalStatus}</td>
                  <td>{subscribedOrders.orderStatus ? 'true' : 'false'}</td>
                  <td>
                    {subscribedOrders.createdAt ? (
                      <TextFormat type="date" value={subscribedOrders.createdAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {subscribedOrders.updatedAt ? (
                      <TextFormat type="date" value={subscribedOrders.updatedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{subscribedOrders.createdBy}</td>
                  <td>{subscribedOrders.updatedBy}</td>
                  <td>
                    {subscribedOrders.user ? <Link to={`/users/${subscribedOrders.user.id}`}>{subscribedOrders.user.email}</Link> : ''}
                  </td>
                  <td>
                    {subscribedOrders.transaction ? (
                      <Link to={`/transactions/${subscribedOrders.transaction.id}`}>{subscribedOrders.transaction.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {subscribedOrders.product ? (
                      <Link to={`/product/${subscribedOrders.product.id}`}>{subscribedOrders.product.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {subscribedOrders.address ? (
                      <Link to={`/user-address/${subscribedOrders.address.id}`}>{subscribedOrders.address.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/subscribed-orders/${subscribedOrders.id}`}
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
                        to={`/subscribed-orders/${subscribedOrders.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/subscribed-orders/${subscribedOrders.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="dailiesApp.subscribedOrders.home.notFound">No Subscribed Orders found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={subscribedOrdersList && subscribedOrdersList.length > 0 ? '' : 'd-none'}>
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

export default SubscribedOrders;
