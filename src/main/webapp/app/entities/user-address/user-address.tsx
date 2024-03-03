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

import { getEntities } from './user-address.reducer';

export const UserAddress = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const userAddressList = useAppSelector(state => state.userAddress.entities);
  const loading = useAppSelector(state => state.userAddress.loading);
  const totalItems = useAppSelector(state => state.userAddress.totalItems);

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
      <h2 id="user-address-heading" data-cy="UserAddressHeading">
        <Translate contentKey="dailiesApp.userAddress.home.title">User Addresses</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dailiesApp.userAddress.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-address/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dailiesApp.userAddress.home.createLabel">Create new User Address</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userAddressList && userAddressList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dailiesApp.userAddress.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('userId')}>
                  <Translate contentKey="dailiesApp.userAddress.userId">User Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userId')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="dailiesApp.userAddress.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('sPhone')}>
                  <Translate contentKey="dailiesApp.userAddress.sPhone">S Phone</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sPhone')} />
                </th>
                <th className="hand" onClick={sort('flatNo')}>
                  <Translate contentKey="dailiesApp.userAddress.flatNo">Flat No</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('flatNo')} />
                </th>
                <th className="hand" onClick={sort('apartmentName')}>
                  <Translate contentKey="dailiesApp.userAddress.apartmentName">Apartment Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('apartmentName')} />
                </th>
                <th className="hand" onClick={sort('area')}>
                  <Translate contentKey="dailiesApp.userAddress.area">Area</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('area')} />
                </th>
                <th className="hand" onClick={sort('landmark')}>
                  <Translate contentKey="dailiesApp.userAddress.landmark">Landmark</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('landmark')} />
                </th>
                <th className="hand" onClick={sort('city')}>
                  <Translate contentKey="dailiesApp.userAddress.city">City</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('city')} />
                </th>
                <th className="hand" onClick={sort('pincode')}>
                  <Translate contentKey="dailiesApp.userAddress.pincode">Pincode</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('pincode')} />
                </th>
                <th className="hand" onClick={sort('lat')}>
                  <Translate contentKey="dailiesApp.userAddress.lat">Lat</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lat')} />
                </th>
                <th className="hand" onClick={sort('lng')}>
                  <Translate contentKey="dailiesApp.userAddress.lng">Lng</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lng')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="dailiesApp.userAddress.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="dailiesApp.userAddress.updatedAt">Updated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="dailiesApp.userAddress.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userAddressList.map((userAddress, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-address/${userAddress.id}`} color="link" size="sm">
                      {userAddress.id}
                    </Button>
                  </td>
                  <td>{userAddress.userId}</td>
                  <td>{userAddress.name}</td>
                  <td>{userAddress.sPhone}</td>
                  <td>{userAddress.flatNo}</td>
                  <td>{userAddress.apartmentName}</td>
                  <td>{userAddress.area}</td>
                  <td>{userAddress.landmark}</td>
                  <td>{userAddress.city}</td>
                  <td>{userAddress.pincode}</td>
                  <td>{userAddress.lat}</td>
                  <td>{userAddress.lng}</td>
                  <td>
                    {userAddress.createdAt ? <TextFormat type="date" value={userAddress.createdAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {userAddress.updatedAt ? <TextFormat type="date" value={userAddress.updatedAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{userAddress.isActive ? 'true' : 'false'}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-address/${userAddress.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/user-address/${userAddress.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/user-address/${userAddress.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="dailiesApp.userAddress.home.notFound">No User Addresses found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={userAddressList && userAddressList.length > 0 ? '' : 'd-none'}>
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

export default UserAddress;
