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

import { getEntities } from './orders.reducer';

export const Orders = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const ordersList = useAppSelector(state => state.orders.entities);
  const loading = useAppSelector(state => state.orders.loading);
  const totalItems = useAppSelector(state => state.orders.totalItems);

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
      <h2 id="orders-heading" data-cy="OrdersHeading">
        <Translate contentKey="dailiesApp.orders.home.title">Orders</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dailiesApp.orders.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/orders/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dailiesApp.orders.home.createLabel">Create new Orders</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {ordersList && ordersList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dailiesApp.orders.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('orderType')}>
                  <Translate contentKey="dailiesApp.orders.orderType">Order Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('orderType')} />
                </th>
                <th className="hand" onClick={sort('orderAmount')}>
                  <Translate contentKey="dailiesApp.orders.orderAmount">Order Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('orderAmount')} />
                </th>
                <th className="hand" onClick={sort('price')}>
                  <Translate contentKey="dailiesApp.orders.price">Price</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('price')} />
                </th>
                <th className="hand" onClick={sort('mrp')}>
                  <Translate contentKey="dailiesApp.orders.mrp">Mrp</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('mrp')} />
                </th>
                <th className="hand" onClick={sort('tax')}>
                  <Translate contentKey="dailiesApp.orders.tax">Tax</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('tax')} />
                </th>
                <th className="hand" onClick={sort('qty')}>
                  <Translate contentKey="dailiesApp.orders.qty">Qty</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('qty')} />
                </th>
                <th className="hand" onClick={sort('selectedDaysForWeekly')}>
                  <Translate contentKey="dailiesApp.orders.selectedDaysForWeekly">Selected Days For Weekly</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('selectedDaysForWeekly')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  <Translate contentKey="dailiesApp.orders.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('subscriptionType')}>
                  <Translate contentKey="dailiesApp.orders.subscriptionType">Subscription Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subscriptionType')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="dailiesApp.orders.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('deliveryStatus')}>
                  <Translate contentKey="dailiesApp.orders.deliveryStatus">Delivery Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deliveryStatus')} />
                </th>
                <th className="hand" onClick={sort('orderStatus')}>
                  <Translate contentKey="dailiesApp.orders.orderStatus">Order Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('orderStatus')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="dailiesApp.orders.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="dailiesApp.orders.updatedAt">Updated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th>
                  <Translate contentKey="dailiesApp.orders.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="dailiesApp.orders.trasation">Trasation</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="dailiesApp.orders.product">Product</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="dailiesApp.orders.address">Address</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ordersList.map((orders, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/orders/${orders.id}`} color="link" size="sm">
                      {orders.id}
                    </Button>
                  </td>
                  <td>{orders.orderType}</td>
                  <td>{orders.orderAmount}</td>
                  <td>{orders.price}</td>
                  <td>{orders.mrp}</td>
                  <td>{orders.tax}</td>
                  <td>{orders.qty}</td>
                  <td>{orders.selectedDaysForWeekly}</td>
                  <td>{orders.startDate ? <TextFormat type="date" value={orders.startDate} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{orders.subscriptionType}</td>
                  <td>{orders.status}</td>
                  <td>{orders.deliveryStatus}</td>
                  <td>{orders.orderStatus ? 'true' : 'false'}</td>
                  <td>{orders.createdAt ? <TextFormat type="date" value={orders.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{orders.updatedAt ? <TextFormat type="date" value={orders.updatedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{orders.user ? <Link to={`/users/${orders.user.id}`}>{orders.user.email}</Link> : ''}</td>
                  <td>{orders.trasation ? <Link to={`/transactions/${orders.trasation.id}`}>{orders.trasation.id}</Link> : ''}</td>
                  <td>{orders.product ? <Link to={`/product/${orders.product.id}`}>{orders.product.id}</Link> : ''}</td>
                  <td>{orders.address ? <Link to={`/user-address/${orders.address.id}`}>{orders.address.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/orders/${orders.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/orders/${orders.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/orders/${orders.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="dailiesApp.orders.home.notFound">No Orders found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={ordersList && ordersList.length > 0 ? '' : 'd-none'}>
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

export default Orders;
