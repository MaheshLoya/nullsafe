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

import { getEntities } from './files.reducer';

export const Files = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const filesList = useAppSelector(state => state.files.entities);
  const loading = useAppSelector(state => state.files.loading);
  const totalItems = useAppSelector(state => state.files.totalItems);

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
      <h2 id="files-heading" data-cy="FilesHeading">
        <Translate contentKey="dailiesApp.files.home.title">Files</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dailiesApp.files.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/files/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dailiesApp.files.home.createLabel">Create new Files</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {filesList && filesList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dailiesApp.files.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="dailiesApp.files.name">Name</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('fileUrl')}>
                  <Translate contentKey="dailiesApp.files.fileUrl">File Url</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileUrl')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="dailiesApp.files.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="dailiesApp.files.updatedAt">Updated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th className="hand" onClick={sort('deleted')}>
                  <Translate contentKey="dailiesApp.files.deleted">Deleted</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deleted')} />
                </th>
                <th className="hand" onClick={sort('fileFor')}>
                  <Translate contentKey="dailiesApp.files.fileFor">File For</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileFor')} />
                </th>
                <th className="hand" onClick={sort('fileForId')}>
                  <Translate contentKey="dailiesApp.files.fileForId">File For Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileForId')} />
                </th>
                <th className="hand" onClick={sort('fileCat')}>
                  <Translate contentKey="dailiesApp.files.fileCat">File Cat</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileCat')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {filesList.map((files, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/files/${files.id}`} color="link" size="sm">
                      {files.id}
                    </Button>
                  </td>
                  <td>{files.name}</td>
                  <td>{files.fileUrl}</td>
                  <td>{files.createdAt ? <TextFormat type="date" value={files.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{files.updatedAt ? <TextFormat type="date" value={files.updatedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{files.deleted ? 'true' : 'false'}</td>
                  <td>{files.fileFor}</td>
                  <td>{files.fileForId}</td>
                  <td>{files.fileCat ? 'true' : 'false'}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/files/${files.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/files/${files.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/files/${files.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="dailiesApp.files.home.notFound">No Files found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={filesList && filesList.length > 0 ? '' : 'd-none'}>
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

export default Files;
