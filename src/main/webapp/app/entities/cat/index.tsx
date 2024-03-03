import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Cat from './cat';
import CatDetail from './cat-detail';
import CatUpdate from './cat-update';
import CatDeleteDialog from './cat-delete-dialog';

const CatRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Cat />} />
    <Route path="new" element={<CatUpdate />} />
    <Route path=":id">
      <Route index element={<CatDetail />} />
      <Route path="edit" element={<CatUpdate />} />
      <Route path="delete" element={<CatDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CatRoutes;
