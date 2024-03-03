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

import { getEntities } from './users.reducer';

export const Users = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const usersList = useAppSelector(state => state.users.entities);
  const loading = useAppSelector(state => state.users.loading);
  const totalItems = useAppSelector(state => state.users.totalItems);

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
      <h2 id="users-heading" data-cy="UsersHeading">
        <Translate contentKey="dailiesApp.users.home.title">Users</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dailiesApp.users.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/users/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dailiesApp.users.home.createLabel">Create new Users</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {usersList && usersList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dailiesApp.users.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('walletAmount')}>
                  <Translate contentKey="dailiesApp.users.walletAmount">Wallet Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('walletAmount')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="dailiesApp.users.email">Email</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('phone')}>
                  <Translate contentKey="dailiesApp.users.phone">Phone</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('phone')} />
                </th>
                <th className="hand" onClick={sort('emailVerifiedAt')}>
                  <Translate contentKey="dailiesApp.users.emailVerifiedAt">Email Verified At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('emailVerifiedAt')} />
                </th>
                <th className="hand" onClick={sort('password')}>
                  <Translate contentKey="dailiesApp.users.password">Password</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('password')} />
                </th>
                <th className="hand" onClick={sort('rememberToken')}>
                  <Translate contentKey="dailiesApp.users.rememberToken">Remember Token</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('rememberToken')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="dailiesApp.users.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="dailiesApp.users.updatedAt">Updated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="dailiesApp.users.name">Name</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('fcm')}>
                  <Translate contentKey="dailiesApp.users.fcm">Fcm</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('fcm')} />
                </th>
                <th className="hand" onClick={sort('subscriptionAmount')}>
                  <Translate contentKey="dailiesApp.users.subscriptionAmount">Subscription Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subscriptionAmount')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {usersList.map((users, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/users/${users.id}`} color="link" size="sm">
                      {users.id}
                    </Button>
                  </td>
                  <td>{users.walletAmount}</td>
                  <td>{users.email}</td>
                  <td>{users.phone}</td>
                  <td>
                    {users.emailVerifiedAt ? <TextFormat type="date" value={users.emailVerifiedAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{users.password}</td>
                  <td>{users.rememberToken}</td>
                  <td>{users.createdAt ? <TextFormat type="date" value={users.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{users.updatedAt ? <TextFormat type="date" value={users.updatedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{users.name}</td>
                  <td>{users.fcm}</td>
                  <td>{users.subscriptionAmount}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/users/${users.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/users/${users.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/users/${users.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="dailiesApp.users.home.notFound">No Users found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={usersList && usersList.length > 0 ? '' : 'd-none'}>
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

export default Users;
